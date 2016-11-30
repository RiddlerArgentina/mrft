/*
 * MIT License
 *
 * Copyright (c) 2016 Federico Vera <https://github.com/dktcoding>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dkt.mrft;

import com.dkt.mrft.gui.MainWindow;
import com.dkt.mrft.utils.BundleDecorator;
import com.dkt.mrft.utils.Config;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import libai.common.Matrix;
import libai.nn.supervised.MLP;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class EntryPoint {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.cli");   

    public static void main(String[] args) {        
        if (args.length != 0) {
            boolean csv = false;
            boolean tsv = false;
            boolean help = false;
            String fname = "";
            ArrayList<Double> vals = new ArrayList<>();
            for (String arg : args) {
                switch(arg) {
                    case "-csv": csv = true; break;
                    case "-tsv": tsv = true; break;
                    case "-h":
                    case "--help": help = true; break;
                    default: {
                        if (fname.isEmpty()) {
                            fname = arg;
                        } else {
                            try {
                                vals.add(Double.parseDouble(arg));
                            } catch (Exception e) {
                                System.out.println(i18n.__("'%s' doesn't seem to be a number", arg));
                                System.exit(0);
                            }
                        }
                    }
                }
            }
            
            if (!help) {
                File file = new File(fname);
                if (!file.exists()) {
                    System.out.println(i18n.__("Unable to parse '%s'", fname));
                    System.exit(0);
                }
                
                MLP mlp = MLP.open(fname);
                if (mlp == null) {
                    System.out.println(i18n.__("'%s' doesn't seem to have a valid matrix", fname));
                    System.exit(0);
                } 
                if (vals.isEmpty()) {
                    System.out.println(i18n.__("Input values needed"));
                    System.exit(0);
                }
                Matrix m = new Matrix(1,1);
                for (double v : vals) {
                    m.position(0, 0, v);
                    double res = mlp.simulate(m).position(0, 0);
                    if (csv) {
                        System.out.println(i18n.__("%f, %f%n", v, res));
                    } else if (tsv) {
                        System.out.println(i18n.__("%f\t%f%n", v, res));
                    } else {
                        System.out.println(res);
                    }
                }
            }
            
            if (help) {
                System.out.println(i18n.__("Usage:     java -jar FinalAI.jar FLAG DAT_FILR VALUES"));
                System.out.println(i18n.__("java -jar FinalAI.jar -csv matrix.dat 0.1 0.2 0.3 0.4"));
                System.out.println(i18n.__("Flags:"));
                System.out.println(i18n.__(" -h --help  -> This help"));
                System.out.println(i18n.__(" -csv       -> comma separated std::out"));
                System.out.println(i18n.__(" -tsv       -> tab separated std::out"));
                System.out.println(i18n.__("            -> returns the value of the prediction"));
            }
            
            System.exit(0);
        }
        
        try {
            String laf = Config.get().get("default.laf");
            
            if (laf.equals("system")) {
                laf = UIManager.getSystemLookAndFeelClassName();
            }
            
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException | 
                 InstantiationException | 
                 IllegalAccessException | 
                 UnsupportedLookAndFeelException ex) {
            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (Config.get().getBool("locale.force")) {
            Locale.setDefault(new Locale(Config.get().get("locale.preferred")));
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    
}
