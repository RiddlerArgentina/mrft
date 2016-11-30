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

import com.dkt.mrft.examples.ekg.EcgCalc;
import com.dkt.mrft.examples.ekg.EcgParam;
import com.dkt.mrft.models.DatasetTableModel;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ExampleSyntheticEKG extends Example {

    @Override
    public String getName() {
        return "SynthEKG(x)";
    }

    @Override
    public int maxEpochs() {
        return 50000;
    }

    @Override
    public int saveEvery() {
        return 25;
    }

    @Override
    public double mse() {
        return 0.001;
    }

    @Override
    public double learnRate() {
        return 0.0001;
    }

    @Override
    public double degradation() {
        return 1;
    }

    @Override
    public int backpropagation() {
        return 2;
    }
    
    @Override
    public String[] topology() {
        return new String[]{"Identity(1)",  "TanH(16)", "Logistic(16)", "Identity(1)"};
    }

    
    private final EcgParam param;
    private final EcgCalc calc;
    {
        param = new EcgParam();
        param.setN(1);
        param.setANoise(0.01);
        calc = new EcgCalc(param);
        calc.calculateEcg();
    }
    @Override
    public void loadTrainData(DatasetTableModel data) {
        for (int i = 0; i < calc.getEcgResultNumRows(); i++) {
            if (calc.getEcgResultTime(i) > 0.5) {
                data.addRow(calc.getEcgResultTime(i) - 1, calc.getEcgResultVoltage(i));
            } else {
                data.addRow(calc.getEcgResultTime(i), calc.getEcgResultVoltage(i));
            }
        }
    }

    @Override
    public void loadValidationData(DatasetTableModel data) {
        int i = 0;
        Random rand = ThreadLocalRandom.current();
        while (i < 40) {
            int idx = rand.nextInt(calc.getEcgResultNumRows());
            if (calc.getEcgResultTime(idx) > 0.5) {
                data.addRow(calc.getEcgResultTime(idx) - 1, calc.getEcgResultVoltage(idx));
            } else {
                data.addRow(calc.getEcgResultTime(idx), calc.getEcgResultVoltage(idx));
            }
            i++;
        }
    }

    @Override
    public void loadGeneralizationData(DatasetTableModel data) {
        load(data, 100);
    }
    
    void load(DatasetTableModel data, int num) {
        for (double x = -0.5; x < 0.5; x += 0.01) {
            data.addRow(x, 0.0);
        }
    }

    @Override
    public String getFolder() {
        return super.getFolder()
             + File.separator + "mrft"
             + File.separator + "Examples" 
             + File.separator + "EKG" 
             + File.separator;
    }

    @Override
    public String getWeightFile() {
        return "EKG_{epoch}.dat";
    }

    @Override
    public boolean plotTraining() {
        return true;
    }

    @Override
    public int trainingFormat() {
        return 2;
    }

    @Override
    public boolean plotValidation() {
        return true;
    }

    @Override
    public int validationFormat() {
        return 0;
    }

    @Override
    public boolean plotGeneralization() {
        return true;
    }

    @Override
    public int generalizationFormat() {
        return 1;
    }
    
}
