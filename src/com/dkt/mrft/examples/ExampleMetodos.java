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
package com.dkt.mrft.examples;

import com.dkt.mrft.gui.MainWindow;
import com.dkt.mrft.models.DatasetTableModel;
import java.io.File;
import libai.nn.supervised.MLP;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ExampleMetodos extends Example {

    @Override
    public String getName() {
        return "CantRemember(x)";
    }

    @Override
    public int maxEpochs() {
        return 250000;
    }

    @Override
    public int saveEvery() {
        return 25;
    }

    @Override
    public double mse() {
        return 0.0000001;
    }

    @Override
    public double learnRate() {
        return 0.0005;
    }

    @Override
    public double degradation() {
        return 1;
    }

    @Override
    public int backpropagation() {
        return MLP.RESILENT_BACKPROPAGATION;
    }
    
    @Override
    public String[] topology() {
        return new String[]{"Identity(1)",  "Logistic(8)", "Identity(1)"};
    }

    @Override
    public void loadTrainData(DatasetTableModel data) {
        data.addRow(0.0  - 10,  0.0);
        data.addRow(1.0  - 10, -0.9);
        data.addRow(2.0  - 10, -1.8);
        data.addRow(3.0  - 10, -1.9);
        data.addRow(4.0  - 10, -2.0);
        data.addRow(5.0  - 10, -3.0);
        data.addRow(6.0  - 10, -4.0);
        data.addRow(7.0  - 10, -4.0);
        data.addRow(8.0  - 10, -4.0);
        data.addRow(9.0  - 10, -5.0);
        data.addRow(10.0 - 10, -6.0);
        data.addRow(11.0 - 10, -5.0);
        data.addRow(12.0 - 10, -4.0);
        data.addRow(13.0 - 10, -3.8);
        data.addRow(14.0 - 10, -3.6);
        data.addRow(15.0 - 10, -3.5);
        data.addRow(16.0 - 10, -3.4);
        data.addRow(17.0 - 10, -3.1);
        data.addRow(18.0 - 10, -2.8);
        data.addRow(19.0 - 10, -1.4);
        data.addRow(20.0 - 10,  0.0);
    }

    @Override
    public void loadValidationData(DatasetTableModel data) {
        data.addRow(0 - 10.0, 0.0);
        data.addRow(0.1 - 10.0, -0.128);
        data.addRow(0.2 - 10.0, -0.252);
        data.addRow(0.3 - 10.0, -0.372);
        data.addRow(0.4 - 10.0, -0.488);
        data.addRow(0.5 - 10.0, -0.6);
        data.addRow(0.6 - 10.0, -0.708);
        data.addRow(0.7 - 10.0, -0.812);
        data.addRow(0.8 - 10.0, -0.912);
        data.addRow(0.9 - 10.0, -1.008);
        data.addRow(1 - 10.0, -1.1);
        data.addRow(1.1 - 10.0, -1.188);
        data.addRow(1.2 - 10.0, -1.272);
        data.addRow(1.3 - 10.0, -1.352);
        data.addRow(1.4 - 10.0, -1.428);
        data.addRow(1.5 - 10.0, -1.5);
        data.addRow(1.6 - 10.0, -1.568);
        data.addRow(1.7 - 10.0, -1.632);
        data.addRow(1.8 - 10.0, -1.692);
        data.addRow(1.9 - 10.0, -1.748);
        data.addRow(2 - 10.0, -1.8);
        data.addRow(2.1 - 10.0, -1.848);
        data.addRow(2.2 - 10.0, -1.892);
        data.addRow(2.3 - 10.0, -1.932);
        data.addRow(2.4 - 10.0, -1.968);
        data.addRow(2.5 - 10.0, -2.0);
        data.addRow(2.6 - 10.0, -2.028);
        data.addRow(2.7 - 10.0, -2.052);
        data.addRow(2.8 - 10.0, -2.072);
        data.addRow(2.9 - 10.0, -2.088);
        data.addRow(3 - 10.0, -1.675);
        data.addRow(3.1 - 10.0, -1.68725);
        data.addRow(3.2 - 10.0, -1.704);
        data.addRow(3.3 - 10.0, -1.72525);
        data.addRow(3.4 - 10.0, -1.751);
        data.addRow(3.5 - 10.0, -1.78125);
        data.addRow(3.6 - 10.0, -1.816);
        data.addRow(3.7 - 10.0, -1.85525);
        data.addRow(3.8 - 10.0, -1.899);
        data.addRow(3.9 - 10.0, -1.94725);
        data.addRow(4 - 10.0, -2.0);
        data.addRow(4.1 - 10.0, -2.05725);
        data.addRow(4.2 - 10.0, -2.119);
        data.addRow(4.3 - 10.0, -2.18525);
        data.addRow(4.4 - 10.0, -2.256);
        data.addRow(4.5 - 10.0, -2.33125);
        data.addRow(4.6 - 10.0, -2.411);
        data.addRow(4.7 - 10.0, -2.49525);
        data.addRow(4.8 - 10.0, -2.584);
        data.addRow(4.9 - 10.0, -2.67725);
        data.addRow(5 - 10.0, -2.775);
        data.addRow(5.1 - 10.0, -3.3475);
        data.addRow(5.2 - 10.0, -3.44);
        data.addRow(5.3 - 10.0, -3.5275);
        data.addRow(5.4 - 10.0, -3.61);
        data.addRow(5.5 - 10.0, -3.6875);
        data.addRow(5.6 - 10.0, -3.76);
        data.addRow(5.7 - 10.0, -3.8275);
        data.addRow(5.8 - 10.0, -3.89);
        data.addRow(5.9 - 10.0, -3.9475);
        data.addRow(6 - 10.0, -4.0);
        data.addRow(6.1 - 10.0, -4.0475);
        data.addRow(6.2 - 10.0, -4.09);
        data.addRow(6.3 - 10.0, -4.1275);
        data.addRow(6.4 - 10.0, -4.16);
        data.addRow(6.5 - 10.0, -4.1875);
        data.addRow(6.6 - 10.0, -4.21);
        data.addRow(6.7 - 10.0, -4.2275);
        data.addRow(6.8 - 10.0, -4.24);
        data.addRow(6.9 - 10.0, -4.2475);
        data.addRow(7 - 10.0, -4.25);
        data.addRow(7.1 - 10.0, -3.7525);
        data.addRow(7.2 - 10.0, -3.76);
        data.addRow(7.3 - 10.0, -3.7725);
        data.addRow(7.4 - 10.0, -3.79);
        data.addRow(7.5 - 10.0, -3.8125);
        data.addRow(7.6 - 10.0, -3.84);
        data.addRow(7.7 - 10.0, -3.8725);
        data.addRow(7.8 - 10.0, -3.91);
        data.addRow(7.9 - 10.0, -3.9525);
        data.addRow(8 - 10.0, -4.0);
        data.addRow(8.1 - 10.0, -4.0525);
        data.addRow(8.2 - 10.0, -4.11);
        data.addRow(8.3 - 10.0, -4.1725);
        data.addRow(8.4 - 10.0, -4.24);
        data.addRow(8.5 - 10.0, -4.3125);
        data.addRow(8.6 - 10.0, -4.39);
        data.addRow(8.7 - 10.0, -4.4725);
        data.addRow(8.8 - 10.0, -4.56);
        data.addRow(8.9 - 10.0, -4.6525);
        data.addRow(9 - 10.0, -4.75);
        data.addRow(9.1 - 10.0, -5.595);
        data.addRow(9.2 - 10.0, -5.68);
        data.addRow(9.3 - 10.0, -5.755);
        data.addRow(9.4 - 10.0, -5.82);
        data.addRow(9.5 - 10.0, -5.875);
        data.addRow(9.6 - 10.0, -5.92);
        data.addRow(9.7 - 10.0, -5.955);
        data.addRow(9.8 - 10.0, -5.98);
        data.addRow(9.9 - 10.0, -5.995);
        data.addRow(10 - 10.0, -6.0);
        data.addRow(10.1 - 10.0, -5.995);
        data.addRow(10.2 - 10.0, -5.98);
        data.addRow(10.3 - 10.0, -5.955);
        data.addRow(10.4 - 10.0, -5.92);
        data.addRow(10.5 - 10.0, -5.875);
        data.addRow(10.6 - 10.0, -5.82);
        data.addRow(10.7 - 10.0, -5.755);
        data.addRow(10.8 - 10.0, -5.68);
        data.addRow(10.9 - 10.0, -5.595);
        data.addRow(11 - 10.0, -5.5);
        data.addRow(11.1 - 10.0, -4.702);
        data.addRow(11.2 - 10.0, -4.608);
        data.addRow(11.3 - 10.0, -4.518);
        data.addRow(11.4 - 10.0, -4.432);
        data.addRow(11.5 - 10.0, -4.35);
        data.addRow(11.6 - 10.0, -4.272);
        data.addRow(11.7 - 10.0, -4.198);
        data.addRow(11.8 - 10.0, -4.128);
        data.addRow(11.9 - 10.0, -4.062);
        data.addRow(12 - 10.0, -4.0);
        data.addRow(12.1 - 10.0, -3.942);
        data.addRow(12.2 - 10.0, -3.888);
        data.addRow(12.3 - 10.0, -3.838);
        data.addRow(12.4 - 10.0, -3.792);
        data.addRow(12.5 - 10.0, -3.75);
        data.addRow(12.6 - 10.0, -3.712);
        data.addRow(12.7 - 10.0, -3.678);
        data.addRow(12.8 - 10.0, -3.648);
        data.addRow(12.9 - 10.0, -3.622);
        data.addRow(13 - 10.0, -3.6);
        data.addRow(13.1 - 10.0, -3.75525);
        data.addRow(13.2 - 10.0, -3.736);
        data.addRow(13.3 - 10.0, -3.71725);
        data.addRow(13.4 - 10.0, -3.699);
        data.addRow(13.5 - 10.0, -3.68125);
        data.addRow(13.6 - 10.0, -3.664);
        data.addRow(13.7 - 10.0, -3.64725);
        data.addRow(13.8 - 10.0, -3.631);
        data.addRow(13.9 - 10.0, -3.61525);
        data.addRow(14 - 10.0, -3.6);
        data.addRow(14.1 - 10.0, -3.58525);
        data.addRow(14.2 - 10.0, -3.571);
        data.addRow(14.3 - 10.0, -3.55725);
        data.addRow(14.4 - 10.0, -3.544);
        data.addRow(14.5 - 10.0, -3.53125);
        data.addRow(14.6 - 10.0, -3.519);
        data.addRow(14.7 - 10.0, -3.50725);
        data.addRow(14.8 - 10.0, -3.496);
        data.addRow(14.9 - 10.0, -3.48525);
        data.addRow(15 - 10.0, -3.475);
        data.addRow(15.1 - 10.0, -3.5395);
        data.addRow(15.2 - 10.0, -3.528);
        data.addRow(15.3 - 10.0, -3.5155);
        data.addRow(15.4 - 10.0, -3.502);
        data.addRow(15.5 - 10.0, -3.4875);
        data.addRow(15.6 - 10.0, -3.472);
        data.addRow(15.7 - 10.0, -3.4555);
        data.addRow(15.8 - 10.0, -3.438);
        data.addRow(15.9 - 10.0, -3.4195);
        data.addRow(16 - 10.0, -3.4);
        data.addRow(16.1 - 10.0, -3.3795);
        data.addRow(16.2 - 10.0, -3.358);
        data.addRow(16.3 - 10.0, -3.3355);
        data.addRow(16.4 - 10.0, -3.312);
        data.addRow(16.5 - 10.0, -3.2875);
        data.addRow(16.6 - 10.0, -3.262);
        data.addRow(16.7 - 10.0, -3.2355);
        data.addRow(16.8 - 10.0, -3.208);
        data.addRow(16.9 - 10.0, -3.1795);
        data.addRow(17 - 10.0, -3.15);
        data.addRow(17.1 - 10.0, -3.34225);
        data.addRow(17.2 - 10.0, -3.304);
        data.addRow(17.3 - 10.0, -3.26025);
        data.addRow(17.4 - 10.0, -3.211);
        data.addRow(17.5 - 10.0, -3.15625);
        data.addRow(17.6 - 10.0, -3.096);
        data.addRow(17.7 - 10.0, -3.03025);
        data.addRow(17.8 - 10.0, -2.959);
        data.addRow(17.9 - 10.0, -2.88225);
        data.addRow(18 - 10.0, -2.8);
        data.addRow(18.1 - 10.0, -2.71225);
        data.addRow(18.2 - 10.0, -2.619);
        data.addRow(18.3 - 10.0, -2.52025);
        data.addRow(18.4 - 10.0, -2.416);
        data.addRow(18.5 - 10.0, -2.30625);
        data.addRow(18.6 - 10.0, -2.191);
        data.addRow(18.7 - 10.0, -2.07025);
        data.addRow(18.8 - 10.0, -1.944);
        data.addRow(18.9 - 10.0, -1.81225);
        data.addRow(19 - 10.0, -1.675);
        data.addRow(19.1 - 10.0, -1.53225);
        data.addRow(19.2 - 10.0, -1.384);
        data.addRow(19.3 - 10.0, -1.23025);
        data.addRow(19.4 - 10.0, -1.071);
        data.addRow(19.5 - 10.0, -0.90625);
        data.addRow(19.6 - 10.0, -0.736);
        data.addRow(19.7 - 10.0, -0.56025);
        data.addRow(19.8 - 10.0, -0.379);
        data.addRow(19.9 - 10.0, -0.19225);
        data.addRow(20 - 10.0, 0.0);
    }

    @Override
    public void loadGeneralizationData(DatasetTableModel data) {
        load(data, 100);
    }
    
    void load(DatasetTableModel data, int num) {
        for (double x = -10; x < 10; x += 0.1) {
            data.addRow(x, 0.0);
        }
    }

    @Override
    public String getFolder() {
        return super.getFolder()
             + File.separator + "mrft"
             + File.separator + "Examples" 
             + File.separator + "CantRemember" 
             + File.separator;
    }

    @Override
    public String getWeightFile() {
        return "CantRemember{epoch}.dat";
    }

    @Override
    public boolean plotTraining() {
        return true;
    }

    @Override
    public int trainingFormat() {
        return MainWindow.PLOT_PATHS;
    }

    @Override
    public boolean plotValidation() {
        return true;
    }

    @Override
    public int validationFormat() {
        return MainWindow.PLOT_POINT;
    }

    @Override
    public boolean plotGeneralization() {
        return true;
    }

    @Override
    public int generalizationFormat() {
        return MainWindow.PLOT_CROSS;
    }
    
}
