/*
 * MIT License
 *
 * Copyright (c) 2016-2018 Federico Vera <https://github.com/dktcoding>
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
package com.dkt.mrft.models;

import com.dkt.mrft.utils.BundleDecorator;
import com.dkt.mrft.utils.Pair;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import libai.common.functions.ArcTangent;
import libai.common.functions.Function;
import libai.common.functions.Gaussian;
import libai.common.functions.HyperbolicTangent;
import libai.common.functions.Identity;
import libai.common.functions.Sigmoid;
import libai.common.functions.Sinc;

/**
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
public class LayersModel extends AbstractTableModel {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.models");   
    private final Class<?>[] clazz = {String.class, Integer.class, String.class};
    private final String[] cols = {
        i18n.__("Layers"),
        i18n.__("Neurons"), 
        i18n.__("Activation")
    };
    
    private final LinkedList<Pair<Integer, String>> data = new LinkedList<>();
    
    public LayersModel () {
        data.add(new Pair<>(1, "Identity"));
        data.add(new Pair<>(4, "Gaussian" ));
        data.add(new Pair<>(1, "Identity"));
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0: {
                if (rowIndex == 0) {
                    return i18n.__("Input");
                } 
                if (rowIndex == data.size() - 1) {
                    return i18n.__("Output");
                }
                return i18n.__("Hidden #%d", rowIndex);
            }
            case 1: return data.get(rowIndex).first;
            case 2: return data.get(rowIndex).second;
            default: return null;
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        synchronized (data) {
            if (rowIndex == 0 || rowIndex == data.size() - 1) {
                return;
            }

            if (columnIndex == 1) {
                if (((Integer)aValue) <= 0)  {
                    return;
                } else {
                    data.get(rowIndex).first = (Integer)aValue;
                }
            } else {
                data.get(rowIndex).second = (String)aValue;
            }
        }
        
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return rowIndex != 0 && rowIndex != data.size() - 1 && columnIndex != 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return clazz[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    public int addRow() {
        synchronized (data) {
            final Pair<Integer, String> last = data.pollLast();
            data.addLast(new Pair<>(4, "Logistic"));
            data.addLast(last);
            fireTableRowsInserted(data.size() - 2, data.size() - 2);
            return data.size();
        }
    }
    
    public int removeRow() {
        synchronized (data) {
            if (data.size() == 2) {
                return 2;
            }

            final Pair<Integer, String> last = data.pollLast();
            data.pollLast();
            data.addLast(last);
            fireTableRowsDeleted(data.size() - 2, data.size() - 2);
            return data.size();
        }
    }
    
    public int[] getLayers() {
        synchronized (data) {
            final int[] ret = new int[data.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = data.get(i).first;
            }
            return ret;
        }
    }
    
    public Function[] getLayerActivation() {
        synchronized (data) {
            final Function[] ret = new Function[data.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = getFunction(data.get(i).second);
            }
            return ret;
        }
    }

    private Function getFunction(String func) {
        switch(func.trim()) {
            case "Identity": return new Identity();
            case "Logistic": return new Sigmoid();
            case "ArcTan":   return new ArcTangent();
            case "Gaussian": return new Gaussian();
            case "Sinc":     return new Sinc();
            case "TanH":     return new HyperbolicTangent();
            default: return null;
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(128);
        sb.append('{');
        for (int i = 0; i < data.size(); i++) {
            sb.append(data.get(i).second);
            sb.append('(').append(data.get(i).first).append(')');
            if (i != data.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public int load(String... foo) {
        while (removeRow() != 2);
        
        for (int i = 0; i < foo.length; i++) {
            String f = foo[i];
            if (i == 0 || i == foo.length -1) continue;
            int nneurons = Integer.parseInt(f.substring(f.indexOf('(') + 1, f.indexOf(')')));
            f = f.substring(0, f.indexOf('(')).trim();
            addRow();
            setValueAt(nneurons, i, 1);
            setValueAt(f, i, 2);
        }
        
        return foo.length - 2;
    }
}
