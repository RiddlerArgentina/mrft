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

import com.dkt.mrft.models.DatasetTableModel;
import java.io.File;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ExampleVeryJumpy extends Example {

    @Override
    public String getName() {
        return "VeryJumpy(x)";
    }

    @Override
    public String getFormula() {
        return "if(x < -2, 2, if(x < -1, x + 2, if (x < 1, -2, if (x < 2, -(x^2) + 1, log(x)))))";
    }

    @Override
    public int maxEpochs() {
        return 25000;
    }

    @Override
    public int saveEvery() {
        return 50;
    }

    @Override
    public double mse() {
        return 0.005;
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
        return new String[]{"Identity(1)",  "Logistic(8)", "Logistic(8)", "Identity(1)"};
    }

    @Override
    public void loadTrainData(DatasetTableModel data) {
        load(data, 250);
    }

    @Override
    public void loadValidationData(DatasetTableModel data) {
        load(data, 40);
    }

    @Override
    public void loadGeneralizationData(DatasetTableModel data) {
        load(data, 200);
    }
    
    void load(DatasetTableModel data, int num) {
        for (int i = 0; i < num; i++) {
            double x  = Math.random() * 6 - 3;
            double fx = f(x);
            data.addRow(x, fx);
        }
    }
    
    private double f(double x) {
        if (x < -2) {
            return 2;
        } 
        if (x < -1) {
            return x +2;
        } 
        if (x < 1){
            return -2;
        }
        if (x < 2) {
            return -(x*x)+1;
        }
        return Math.log(x);
    }

    @Override
    public String getFolder() {
        return super.getFolder()
             + File.separator + "mrft"
             + File.separator + "Examples" 
             + File.separator + "VeryJumpy" 
             + File.separator;
    }

    @Override
    public String getWeightFile() {
        return "FVeryJumpy_{epoch}.dat";
    }

    @Override
    public boolean plotTraining() {
        return true;
    }

    @Override
    public int trainingFormat() {
        return 1;
    }

    @Override
    public boolean plotValidation() {
        return false;
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
