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
package com.dkt.mrft.utils;

import com.dkt.mrft.examples.Example;
import com.dkt.mrft.gui.MainWindow;
import com.dkt.mrft.models.DatasetTableModel;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class simply wraps an mlp config file in an {@link Example}, it <i>should</i> be done
 * differently, but it seems easier since examples are actually the first thing I implemented...
 * 
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ConfigWrapper extends Example {
    private final CommentedProperties conf = new CommentedProperties();
    private final MainWindow father;
    private final String path;
    
    public ConfigWrapper (FileInputStream fis, String path, MainWindow father) throws IOException {
        this.father = father;
        this.path = path;
        conf.load(fis);
    }
    
    @Override
    public String getName() {
        return "";
    }

    @Override
    public int maxEpochs() {
        return conf.getInt("max.epochs");
    }

    @Override
    public int saveEvery() {
        return conf.getInt("save.every");
    }

    @Override
    public double mse() {
        return conf.getDouble("mce");
    }

    @Override
    public double learnRate() {
        return conf.getDouble("leaning.rate");
    }

    @Override
    public double degradation() {
        return conf.getDouble("degradation");
    }

    @Override
    public int backpropagation() {
        return conf.getInt("backpropagation");
    }

    @Override
    public String[] topology() {
        return conf.get("topology").replace("{", "").replace("}", "").split(",");
    }

    @Override
    public void loadTrainData(DatasetTableModel data) {
        data.addAll(father.readFile(new File(path + conf.get("data.train"))));
    }

    @Override
    public void loadValidationData(DatasetTableModel data) {
        data.addAll(father.readFile(new File(path + conf.get("data.valid"))));
    }

    @Override
    public void loadGeneralizationData(DatasetTableModel data) {
        data.addAll(father.readFile(new File(path + conf.get("data.gener"))));
    }

    @Override
    public String getWeightFile() {
        return conf.get("weight.file");
    }

    @Override
    public boolean plotTraining() {
        return conf.getBool("plot.train");
    }

    @Override
    public int trainingFormat() {
        return conf.getInt("plot.train.format");
    }

    @Override
    public boolean plotValidation() {
        return conf.getBool("plot.valid");
    }

    @Override
    public int validationFormat() {
        return conf.getInt("plot.valid.format");
    }

    @Override
    public boolean plotGeneralization() {
        return conf.getBool("plot.gener");
    }

    @Override
    public int generalizationFormat() {
        return conf.getInt("plot.gener.format");
    }
    
    @Override
    public String getFolder() {
        return path;
    }
    
    public File errorFile() {
        return new File(path + conf.get("data.error"));
    }
    
    public void setColors(Color[] colors) {
        colors[0] = conf.getColor("plot.train.color");
        colors[1] = conf.getColor("plot.valid.color");
        colors[2] = conf.getColor("plot.gener.color");
    }
    
    @Override
    public boolean smoothError() {
        return conf.getBool("smooth.errors");
    }
    
    @Override
    public boolean showLabels() {
        return conf.getBool("show.labels");
    }
}
