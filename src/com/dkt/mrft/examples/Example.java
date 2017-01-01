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
import com.dkt.mrft.models.LayersModel;
import com.dkt.mrft.utils.Config;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public abstract class Example {
    /**
     * @return Name of the example
     */
    abstract public String getName();

    /**
     * @return Maximum number of training epochs
     * @see Example#mse()
     */
    abstract public int maxEpochs();

    /**
     * @return Number of epochs on which to save the weights
     */
    abstract public int saveEvery();

    /**
     * @return Mean square error of training (max)
     * @see Example#maxEpochs()
     */
    abstract public double mse();

    /**
     * @return Learning rate of the backpropagation algorithm
     */
    abstract public double learnRate();

    /**
     * If {@code 1} the learning rate will be constant through the training.<br>
     * Else every {@code saveEvery()} iterations the new learning rate will be calculated as:
     * {@code learnEvery() * degradation}
     * @return Degradation of the learning rate
     */
    abstract public double degradation();

    /**
     * @return Backpropagation algorithm
     * @see libai.nn.supervised.MLP#MOMEMTUM_BACKPROPAGATION
     * @see libai.nn.supervised.MLP#RESILENT_BACKPROPAGATION
     * @see libai.nn.supervised.MLP#STANDARD_BACKPROPAGATION
     */
    abstract public int backpropagation();

    /**
     * @return String representation of the topology as expressed by {@link LayersModel#toString()}
     */
    abstract public String[] topology();

    /**
     * @param model The model in which to load the topology
     * @return Number of hidden layers
     */
    public final int loadTable(LayersModel model) {
        return model.load(topology());
    }

    /**
     * @param data {@code DatasetTableModel} in which to load the training data
     */
    abstract public void loadTrainData(DatasetTableModel data);

    /**
     * @param data {@code DatasetTableModel} in which to load the validation data
     */
    abstract public void loadValidationData(DatasetTableModel data);

    /**
     * @param data {@code DatasetTableModel} in which to load the generalization data
     */
    abstract public void loadGeneralizationData(DatasetTableModel data);

    /**
     * @return Working directory
     */
    public String getFolder() {
        String home = Config.get().get("last.path").isEmpty()
                    ? System.getProperty("user.home")
                    : Config.get().get("last.path");
        return home;
    }

    /**
     * @return Weight file pattern
     */
    abstract public String getWeightFile();

    /**
     * @return {@code true} if the training data should be plotted and {@code false} otherwise
     */
    abstract public boolean plotTraining();

    /**
     * @return How to plot the training data
     * @see com.dkt.mrft.gui.MainWindow#PLOT_POINT
     * @see com.dkt.mrft.gui.MainWindow#PLOT_PATHS
     * @see com.dkt.mrft.gui.MainWindow#PLOT_CROSS
     */
    abstract public int trainingFormat();

    /**
     * @return {@code true} if the validation data should be plotted and {@code false} otherwise
     */
    abstract public boolean plotValidation();

    /**
     * @return How to plot the validation data
     * @see com.dkt.mrft.gui.MainWindow#PLOT_POINT
     * @see com.dkt.mrft.gui.MainWindow#PLOT_PATHS
     * @see com.dkt.mrft.gui.MainWindow#PLOT_CROSS
     */
    abstract public int validationFormat();

    /**
     * @return {@code true} if the generalization data should be plotted and {@code false} otherwise
     */
    abstract public boolean plotGeneralization();

    /**
     * @return How to plot the generalization data
     * @see com.dkt.mrft.gui.MainWindow#PLOT_POINT
     * @see com.dkt.mrft.gui.MainWindow#PLOT_PATHS
     * @see com.dkt.mrft.gui.MainWindow#PLOT_CROSS
     */
    abstract public int generalizationFormat();

    /**
     * @return {@code true} if the error should be smoothed and {@code false} otherwise
     */
    public boolean smoothError(){
        return true;
    }

    /**
     * @return {@code true} if the labels should be plotted and {@code false} otherwise
     */
    public boolean showLabels(){
        return true;
    }

    /**
     * @return String representation of the formula
     */
    public String getFormula() {
        return "";
    }

}
