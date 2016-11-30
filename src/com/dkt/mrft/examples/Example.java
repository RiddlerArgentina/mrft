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
    abstract public String getName();
    abstract public int maxEpochs();
    abstract public int saveEvery();
    abstract public double mse();
    abstract public double learnRate();
    abstract public double degradation();
    abstract public int backpropagation();
    abstract public String[] topology();
    public final int loadTable(LayersModel model) {
        return model.load(topology());
    }
    abstract public void loadTrainData(DatasetTableModel data);
    abstract public void loadValidationData(DatasetTableModel data);
    abstract public void loadGeneralizationData(DatasetTableModel data);
    public String getFolder() {
        String home = Config.get().get("last.path").isEmpty()
                    ? System.getProperty("user.home") 
                    : Config.get().get("last.path");
        return home;
    }
    abstract public String getWeightFile();
    abstract public boolean plotTraining();
    abstract public int trainingFormat();
    abstract public boolean plotValidation();
    abstract public int validationFormat();
    abstract public boolean plotGeneralization();
    abstract public int generalizationFormat();
    public boolean smoothError(){
        return true;
    }
    public boolean showLabels(){
        return true;
    }
    
    public String getFormula() {
        return "";
    }
    
}
