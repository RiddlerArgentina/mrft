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

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ExampleBatman extends Example {

    @Override
    public String getName() {
        return "Batman(x)";
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
        return 0.010;
    }

    @Override
    public double learnRate() {
        return 0.001;
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
        return new String[]{"Identity(1)", "Gaussian(16)", "Identity(1)"};
    }

    @Override
    public void loadTrainData(DatasetTableModel data) {
        load(data, 500);
    }

    @Override
    public void loadValidationData(DatasetTableModel data) {
        load(data, 100);
    }

    @Override
    public void loadGeneralizationData(DatasetTableModel data) {
        load(data, 200);
    }

    void load(DatasetTableModel data, int num) {
        for (int i = 0; i < num; i++) {
            double x = Math.random() * 14 - 7;
            double fx;
            if ((x > -3 && x < -1) || (x > 1 && x < 3)) {
                fx = f1(x);
            } else {
                fx = f(x);
            }
            data.addRow(x, fx);
        }
    }

    private double f(double x) {
        return 2 * sqrt(-abs(abs(x) - 1) * abs(3 - abs(x)) / ((abs(x) - 1) * (3 - abs(x)))) 
                 * (1 + abs(abs(x) - 3) / (abs(x) - 3))
                 * sqrt(1 - (x / 7) * (x / 7)) + (5 + 0.97 * (abs(x - .5) + abs(x + .5)) - 3 
                 * (abs(x - .75) + abs(x + .75))) * (1 + abs(1 - abs(x)) / (1 - abs(x)));
    }

    private double f1(double x) {
        return (2.71052 + (1.5 - .5 * abs(x)) - 1.35526 * sqrt(4 - (abs(x) - 1) 
                * (abs(x) - 1))) * sqrt(abs(abs(x) - 1) / (abs(x) - 1)) + 0.9;
    }

    @Override
    public String getFolder() {
        return super.getFolder()
             + File.separator + "mrft"
             + File.separator + "Examples" 
             + File.separator + "Batman"
             + File.separator;
    }

    @Override
    public String getWeightFile() {
        return "Batman_{epoch}.dat";
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
        return false;
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
