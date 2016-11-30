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
package com.dkt.mrft.models;

import com.dkt.mrft.utils.BundleDecorator;
import com.dkt.mrft.utils.Triplet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class ErrorTableModel extends AbstractTableModel {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.models");   
    
    private final ArrayList<Row> data = new ArrayList<>(2048);
    
    public void addRow(int epo, double errtr, double errval) {
        addRow(new Row(epo, errtr, errval));
    }
    
    public void addRow(Row row) {
        synchronized(data) {
            data.add(row);
            final int size = data.size() - 1;
            fireTableRowsInserted(size, size);
        }
    }

    public void addRows(List<Row> rows) {
        synchronized(data) {
            final int start = data.size();
            for (final Row row : rows) {
                data.add(row);
            }
            fireTableRowsInserted(start, data.size() - 1);
        }
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }
        
        final Row e = data.get(rowIndex);
        if (e == null) return null;
        
        switch (columnIndex) {
            case 0: return e.first;
            case 1: return e.second;
            case 2: return e.third;
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private final Class<?> clazz[] = {Integer.class, Double.class, Double.class};
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return clazz[columnIndex];
    }

    private final String[] cols = {
        i18n.__("Epoch"), 
        i18n.__("Training Error"), 
        i18n.__("Validation Error")
    };
    @Override
    public String getColumnName(int column) {
        return cols[column];
    }

    public void removeAll() {        
        synchronized (data) {
            final int size = data.size();
            
            if (size != 0) {
                data.clear();
                fireTableRowsDeleted(0, size - 1);
            }
        }
    }

    public double getDouble(int row, int col) {
        if (col == 0) {
            return data.get(row).second;
        }
        return data.get(row).third;
    }

    public double getEpoch(int row) {
        if (row >= data.size()) {
            return -1;
        }
        return data.get(row).first;
    }
    
    public double[][] getData(){
        final double[][] ret = new double[3][data.size()];
        for (int i = 0, m = ret[0].length; i < m; i++) {
            final Row e = data.get(i);
            ret[0][i] = e.first;
            ret[1][i] = e.second;
            ret[2][i] = e.third;
            
        }
        return ret;
    }

    public int getLastEpoch() {
        if (data.isEmpty()) {
            return -1;
        }
        return data.get(data.size() - 1).first;
    }
    
    public static final class Row extends Triplet<Integer, Double, Double> {
        public Row(Integer x, Double y, Double z) {
            super(x, y, z);
        }
    }
}
