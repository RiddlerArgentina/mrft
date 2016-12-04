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
import javax.swing.table.AbstractTableModel;
import net.objecthunter.exp4j.Expression;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class DatasetTableModel extends AbstractTableModel {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.models");   
    
    private final ArrayList<Row> data = new ArrayList<>(128);
    
    private final String name;
    
    private boolean selecting = false;
    
    public DatasetTableModel(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void addRow(Double x, Double fx) {
        synchronized(data){
            data.add(new Row(false, x, fx));
            final int size = data.size() - 1;
            fireTableRowsInserted(size, size);
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        synchronized(data) {
            final Row e = data.get(row);
            if (!selecting) column++;
            
            switch (column) {
                case 0:  e.first  = (Boolean)aValue; break;
                case 1:  e.second = (Double) aValue; break;
                case 2:  e.third  = (Double) aValue;
            }
            
            if (!selecting) column--;
            
            fireTableCellUpdated(row, column);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        final Row e = data.get(row);
        if (!selecting) column++;
        switch (column) {
            case 0:  return e.first;
            case 1:  return e.second;
            case 2:  return e.third;
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    private final String[] cols = {i18n.__("Selection"), i18n.__("x"), i18n.__("f(x)")};
    @Override
    public String getColumnName(int column) {
        if (!selecting) column++;
        return cols[column];
    }

    @Override
    public int getColumnCount() {
        return selecting ? 3 : 2;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public int getSelectedCount() {
        int counter = 0;
        
        for (final Row e : data) {
            if (e.first) {
                counter++;
            }
        }
        
        return counter;
    }

    private final Class<?>[] colClass = {Boolean.class, Double.class, Double.class};
    @Override
    public Class<?> getColumnClass(int column) {
        if (!selecting) column++;
        return colClass[column];
    }
    
    public void selectAll() {
        synchronized (data) {
            for (final Row e : data) {
                if (!e.first) {
                    e.first = true;
                }
            }
            fireTableDataChanged();
        }
    }
    
    public void selectNone() {
        synchronized (data) {
            for (final Row e : data) {
                if (e.first) {
                    e.first = false;
                }
            }
            fireTableDataChanged();
        }
    }
    
    public void selectToggle() {
        synchronized (data) {
            for (final Row e : data) {
                e.first = !e.first;
            }
            fireTableDataChanged();
        }
    }
    
    public void selectEnabled(boolean enabled) {
        this.selecting = enabled;
        fireTableStructureChanged();
    }
    
    public void cleanRows(){
        synchronized (data) {
            final ArrayList<Row> foo = new ArrayList<>(data);
            int counter = 0;

            for (final Row e : data) {
                if (e.second == null || e.third == null) {
                    foo.add(e);
                    counter++;
                }
            }

            if (counter != 0) {
                data.removeAll(foo);
                fireTableDataChanged();
            }
        }
    }
    
    public ArrayList<Row> removeSelected() {
        synchronized (data) {
            final ArrayList<Row> rem = new ArrayList<>(getSelectedCount());

            for (final Row e : data) {
                if (e.first) {
                    rem.add(e);
                }
            }

            data.removeAll(rem);

            fireTableDataChanged();

            return rem;
        }
    }
    
    public void addAll(ArrayList<Row> foo) {
        synchronized (data) {
            data.addAll(foo);
        }
        fireTableDataChanged();
    }

    public double getMax() {
        double max = 0;
        for (final Row e : data) {
            max = Math.max(max, Math.abs(e.third));
        }
        return max;
    }

    public void scale(double scale) {
        for (final Row e : data) {
            e.third *= scale;
        }
        fireTableDataChanged();
    }

    public double[][] getValues() {
        final double[][] ret = new double[2][getRowCount()];
        int i = 0;
        
        for (final Row e : data) {
            ret[0][i] = e.second;
            ret[1][i] = e.third;
            i++;
        }
        
        return ret;
    }

    public double getDouble(int i, int j) {
        if (j == 0) {
            return data.get(i).second;
        }
        return data.get(i).third;
    }

    public void applyToX(Expression exp4X, String exp) {
        synchronized (data) {
            
            final boolean hasx  = exp4X.containsVariable("x");
            final boolean hasfx = exp4X.containsVariable("fx");
            for (final Row e : data) {
                if (hasx)  exp4X.setVariable("x",  e.second);
                if (hasfx) exp4X.setVariable("fx", e.third);
                e.second = exp4X.evaluate();
            }
        }
        fireTableDataChanged();
    }

    public void applyToFX(Expression exp4FX, String exp) {
        synchronized (data) {
            final boolean hasx  = exp4FX.containsVariable("x");
            final boolean hasfx = exp4FX.containsVariable("fx");
            for (final Row e : data) {
                if (hasx)  exp4FX.setVariable("x",  e.second);
                if (hasfx) exp4FX.setVariable("fx", e.third);
                e.third = exp4FX.evaluate();
            }
        }
        fireTableDataChanged();
    }

    public void applyToBoth(Expression exp4X, String expX, Expression exp4FX, String expFX) {
        synchronized (data) {
            //This is necesarry because the current version of exp4j fails when setting an
            //inexistent variable
            final boolean xhasx  = exp4X .containsVariable("x");
            final boolean xhasfx = exp4X .containsVariable("fx");
            final boolean fhasx  = exp4FX.containsVariable("x");
            final boolean fhasfx = exp4FX.containsVariable("fx");
            
            for (final Row e : data) {
                final double x  = e.second;
                final double fx = e.third;
                
                if (xhasx)  exp4X.setVariable("x", x);
                if (xhasfx) exp4X.setVariable("fx", fx);
                e.second = exp4X.evaluate();
                
                if (fhasx)  exp4FX.setVariable("x", x);
                if (fhasfx) exp4FX.setVariable("fx", fx);
                e.third = exp4FX.evaluate();
            }
        }
        fireTableDataChanged();
    }
    
    public static final class Row extends Triplet<Boolean, Double, Double> {
        public Row(Boolean x, Double y, Double z) {
            super(x, y, z);
        }
    }
}
