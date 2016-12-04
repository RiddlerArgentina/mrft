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
package com.dkt.mrft.gui;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.elements.GLine;
import com.dkt.graphics.elements.GMultiPoint;
import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.elements.GPointArray;
import com.dkt.graphics.elements.GString;
import com.dkt.graphics.extras.GAxis;
import com.dkt.graphics.utils.Gif;
import com.dkt.graphics.utils.TicToc;
import com.dkt.graphics.utils.Utils;
import com.dkt.mrft.examples.Example;
import com.dkt.mrft.examples.ExampleBatman;
import com.dkt.mrft.examples.ExampleCos;
import com.dkt.mrft.examples.ExampleEKG;
import com.dkt.mrft.examples.ExampleJumpy;
import com.dkt.mrft.examples.ExampleMetodos;
import com.dkt.mrft.examples.ExampleSin;
import com.dkt.mrft.examples.ExampleSinc;
import com.dkt.mrft.examples.ExampleSquare;
import com.dkt.mrft.examples.ExampleSyntheticEKG;
import com.dkt.mrft.examples.ExampleTriangles;
import com.dkt.mrft.examples.ExampleVeryJumpy;
import com.dkt.mrft.examples.ekg.NullPrintStream;
import com.dkt.mrft.models.DatasetTableModel;
import com.dkt.mrft.models.ErrorTableModel;
import com.dkt.mrft.models.LayersModel;
import com.dkt.mrft.utils.BundleDecorator;
import com.dkt.mrft.utils.CommentedProperties;
import com.dkt.mrft.utils.Config;
import com.dkt.mrft.utils.ConfigWrapper;
import com.dkt.mrft.utils.FileDrop;
import com.dkt.mrft.utils.Spline;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import libai.common.Matrix;
import libai.nn.supervised.MLP;
import net.objecthunter.exp4j.Expression;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public final class MainWindow extends javax.swing.JFrame {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.misc");   
    private static final Config c4g = Config.get();
    
    private static final int TRAIN = 0;
    private static final int VALID = 1;
    private static final int GENER = 2;
    private static final int ERROR = 3;
    
    private static final int PLOT_POINT = 0;
    private static final int PLOT_CROSS = 1;
    private static final int PLOT_PATHS = 2;
    
    private final LayersModel       layers     = new LayersModel();
    private final DatasetTableModel train      = new DatasetTableModel(i18n.__("training"));
    private final DatasetTableModel validate   = new DatasetTableModel(i18n.__("validation"));
    private final DatasetTableModel generalize = new DatasetTableModel(i18n.__("generalization"));
    private final ErrorTableModel   errors     = new ErrorTableModel();
    private DatasetTableModel       selected   = train;
    
    private final Canvas func = new Canvas();
    private final Canvas errs = new Canvas();
    
    private static final int ERR_X_OFFSET = 15;
    private static final int ERR_Y_OFFSET = 15;
    
    public MainWindow() {
        initComponents();
        info("Program Started - %s", new Date());
        centerHeaders();
        centerCells();
        initModels();
        initGraphs();
        initMisc();
        initListners();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        final JLabel jLabel6 = new JLabel();
        final JLabel jLabel7 = new JLabel();
        final JLabel jLabel1 = new JLabel();
        final JLabel jLabel18 = new JLabel();
        final JLabel jLabel8 = new JLabel();
        final JLabel jLabel9 = new JLabel();
        final JLabel jLabel10 = new JLabel();
        final JLabel jLabel11 = new JLabel();
        final JLabel jLabel12 = new JLabel();
        final JButton jButton11 = new JButton();
        final JButton jButton12 = new JButton();
        final JButton jButton13 = new JButton();
        final JMenu jMenu1 = new JMenu();
        final JMenuItem jMenuItem37 = new JMenuItem();
        final JPopupMenu.Separator jSeparator2 = new JPopupMenu.Separator();
        final JMenuItem jMenuItem5 = new JMenuItem();
        final JMenu jMenu9 = new JMenu();
        final JMenuItem jMenuItem33 = new JMenuItem();
        final JMenuItem jMenuItem34 = new JMenuItem();
        final JMenuItem jMenuItem35 = new JMenuItem();
        final JMenuItem jMenuItem36 = new JMenuItem();
        final JMenu jMenu3 = new JMenu();
        final JMenuItem jMenuItem6 = new JMenuItem();
        final JMenuItem jMenuItem7 = new JMenuItem();
        final JMenuItem jMenuItem8 = new JMenuItem();
        final JMenu jMenu6 = new JMenu();
        final JMenuItem jMenuItem2 = new JMenuItem();
        final JMenuItem jMenuItem14 = new JMenuItem();
        final JMenuItem jMenuItem10 = new JMenuItem();
        final JMenuItem jMenuItem11 = new JMenuItem();
        final JMenu jMenu4 = new JMenu();
        final JMenuItem jMenuItem4 = new JMenuItem();
        final JMenuItem jMenuItem12 = new JMenuItem();
        final JMenuItem jMenuItem26 = new JMenuItem();
        final JMenu jMenu8 = new JMenu();
        final JMenu jMenu10 = new JMenu();
        final JMenuItem jMenuItem29 = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MLP Real Function Trainer - v1.0");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Selección", "x", "f(x)"
            }
        ) {
            Class[] types = new Class [] {
                Boolean.class, Double.class, Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        ResourceBundle bundle = ResourceBundle.getBundle("res/i18n/strings"); // NOI18N
        jLabel2.setText(bundle.getString("TBL_LAB_TRAIN")); // NOI18N

        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText(bundle.getString("TBL_LBL_VALID")); // NOI18N

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Selección", "x", "f(x)"
            }
        ) {
            Class[] types = new Class [] {
                Boolean.class, Double.class, Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText(bundle.getString("TBL_LBL_GENER")); // NOI18N

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Selección", "x", "f(x)"
            }
        ) {
            Class[] types = new Class [] {
                Boolean.class, Double.class, Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText(bundle.getString("TBL_SEL_TRAIN")); // NOI18N
        jRadioButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText(bundle.getString("TBL_SEL_VALID")); // NOI18N
        jRadioButton2.setActionCommand(bundle.getString("TBL_SEL_VALID")); // NOI18N
        jRadioButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText(bundle.getString("TBL_SEL_GENER")); // NOI18N
        jRadioButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(BorderFactory.createTitledBorder(bundle.getString("PNL_SEL"))); // NOI18N

        jLabel5.setText(bundle.getString("PNL_SEL_DESTINATION")); // NOI18N

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Training"), i18n.__("Validation"), i18n.__("Generalization") }));

        jButton1.setText(bundle.getString("PNL_SEL_ALL")); // NOI18N
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("PNL_SEL_NONE")); // NOI18N
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(bundle.getString("PNL_SEL_TOGGLE")); // NOI18N
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSeparator4.setOrientation(SwingConstants.VERTICAL);

        jFormattedTextField1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getPercentInstance())));
        jFormattedTextField1.setText("10%");

        jButton4.setText(bundle.getString("PNL_SEL_SELECT")); // NOI18N
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jFormattedTextField2.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));
        jFormattedTextField2.setText("50");

        jButton5.setText(bundle.getString("PNL_SEL_SELECT")); // NOI18N
        jButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText(bundle.getString("PNL_SEL_RAND")); // NOI18N
        jCheckBox1.setHorizontalAlignment(SwingConstants.TRAILING);
        jCheckBox1.setHorizontalTextPosition(SwingConstants.LEADING);

        jSeparator5.setOrientation(SwingConstants.VERTICAL);

        jButton6.setText(bundle.getString("PNL_SEL_MOVE")); // NOI18N
        jButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText(bundle.getString("PNL_SEL_COPY")); // NOI18N
        jButton8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField1, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBox1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jButton1, jButton2, jButton3});

        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5, GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8))
                            .addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5))
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1))
                            .addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(9, 9, 9)
                                .addComponent(jButton3)
                                .addGap(7, 7, 7)
                                .addComponent(jButton2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jRadioButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jTabbedPane1.addTab(bundle.getString("PNL_DATA"), jPanel1); // NOI18N

        jPanel10.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_LAYERS"))); // NOI18N

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {"I",  new Integer(1), "Identidad"},
                {"1",  new Integer(4), "Sigmoide"},
                {"2",  new Integer(4), "Gaussiana"},
                {"O",  new Integer(1), "Identidad"}
            },
            new String [] {
                "# Layer", "Neurons", "Activation"
            }
        ) {
            Class[] types = new Class [] {
                String.class, Integer.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable1);

        jLabel6.setText(bundle.getString("TOPO_HIDDEN_NUMBER")); // NOI18N

        jSpinner1.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        jSpinner1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jLabel7.setText(bundle.getString("TOPO_BACKPROP")); // NOI18N

        jComboBox2.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Standard"), i18n.__("Momentum"), i18n.__("Resilient") }));

        jLabel1.setText(bundle.getString("TOPO_LEARN_RATE")); // NOI18N

        jLabel18.setText(bundle.getString("TOPO_DEGRADATION")); // NOI18N

        jFormattedTextField4.setValue(0.001);
        jFormattedTextField4.setHorizontalAlignment(JTextField.TRAILING);

        jFormattedTextField5.setValue(1);
        jFormattedTextField5.setHorizontalAlignment(JTextField.TRAILING);

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField4, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, 0, 207, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField5))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jFormattedTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jFormattedTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel12.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_TRAIN"))); // NOI18N

        jLabel8.setText(bundle.getString("TOPO_MSE")); // NOI18N

        jLabel9.setText(bundle.getString("TOPO_EPOCHS")); // NOI18N

        jLabel10.setText(bundle.getString("TOPO_SAVE_WEIGHTS")); // NOI18N

        jFormattedTextField3.setValue(0.0001);
        jFormattedTextField3.setHorizontalAlignment(JTextField.TRAILING);

        jSpinner2.setModel(new SpinnerNumberModel(500000, 1, 100000000, 50));

        jSpinner3.setModel(new SpinnerNumberModel(50, 1, 1000, 50));

        GroupLayout jPanel12Layout = new GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinner2, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addComponent(jSpinner3)
                    .addComponent(jFormattedTextField3))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jFormattedTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinner3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_OUTPUT"))); // NOI18N

        jLabel11.setText(bundle.getString("TOPO_FOLDER_PATH")); // NOI18N

        jTextField1.setText("/dev/shm/FinalIA");

        jButton7.setIcon(new ImageIcon(getClass().getResource("/res/icons/find.png"))); // NOI18N
        jButton7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel12.setText(bundle.getString("TOPO_WEIGHTS_NAME")); // NOI18N

        jTextField2.setText("foo_{epoch}_algo.dat");

        GroupLayout jPanel13Layout = new GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addGap(26, 26, 26)
                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jTextField1, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7))
                    .addComponent(jTextField2))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("PNL_TOPOLOGY"), jPanel2); // NOI18N

        jPanel3.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                jPanel3ComponentShown(evt);
            }
        });

        jPanel8.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_FORMAT"))); // NOI18N

        jComboBox7.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        jComboBox7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jCheckBox2.setText(bundle.getString("PLOT_OPT_TRAIN")); // NOI18N
        jCheckBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText(bundle.getString("PLOT_OPT_VALID")); // NOI18N
        jCheckBox3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setSelected(true);
        jCheckBox4.setText(bundle.getString("PLOT_OPT_GENER")); // NOI18N
        jCheckBox4.setEnabled(false);
        jCheckBox4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        jComboBox8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jComboBox9.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        jComboBox9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        jButton11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        jButton12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        jButton13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(SwingConstants.VERTICAL);

        jProgressBar1.setStringPainted(true);

        jCheckBox5.setSelected(true);
        jCheckBox5.setText(bundle.getString("PLOT_SMOOTH")); // NOI18N
        jCheckBox5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new ImageIcon(getClass().getResource("/res/icons/play.png"))); // NOI18N
        jButton14.setEnabled(false);
        jButton14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton16.setIcon(new ImageIcon(getClass().getResource("/res/icons/stop.png"))); // NOI18N
        jButton16.setEnabled(false);
        jButton16.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jCheckBox6.setSelected(true);
        jCheckBox6.setText(bundle.getString("PLOT_SHOW_LABELS")); // NOI18N
        jCheckBox6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jCheckBox4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox9, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox7, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox8, 0, 200, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11)
                    .addComponent(jButton12)
                    .addComponent(jButton13))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBox6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox2)
                            .addComponent(jComboBox9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jCheckBox3)
                                .addComponent(jComboBox7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton12))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jCheckBox4)
                                .addComponent(jComboBox8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton13))
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBox5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox6)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton16)
                            .addComponent(jButton14)
                            .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel8Layout.linkSize(SwingConstants.VERTICAL, new Component[] {jButton16, jProgressBar1});

        jSplitPane1.setDividerLocation(250);

        jTable5.setModel(new DefaultTableModel(
            new Object [][] {
                { new Integer(0), null, null},
                { new Integer(5), null, null},
                { new Integer(10), null, null},
                { new Integer(15), null, null}
            },
            new String [] {
                "Época", "E. Entrenamiento", "E. Validación"
            }
        ) {
            Class[] types = new Class [] {
                Integer.class, Double.class, Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(jTable5);

        jSplitPane1.setLeftComponent(jScrollPane6);

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);

        jPanel6.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_FUNCTION"))); // NOI18N
        jPanel6.setDoubleBuffered(false);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane2.setTopComponent(jPanel6);

        jPanel7.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_ERRS"))); // NOI18N
        jPanel7.setDoubleBuffered(false);

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel7);

        jSplitPane1.setRightComponent(jSplitPane2);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                    .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PNL_ERR_PLOTS"), jPanel3); // NOI18N

        jScrollPane1.setViewportView(jTextPane1);

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("PNL_OUTPUT"), jPanel9); // NOI18N

        jMenu1.setMnemonic('A');
        ResourceBundle bundle1 = ResourceBundle.getBundle("res/i18n/menu"); // NOI18N
        jMenu1.setText(bundle1.getString("FILE")); // NOI18N

        jMenuItem37.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        jMenuItem37.setMnemonic('c');
        jMenuItem37.setText(bundle1.getString("FILE_LOAD_CONF")); // NOI18N
        jMenuItem37.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem37);
        jMenu1.add(jSeparator2);

        jMenuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        jMenuItem5.setMnemonic('s');
        jMenuItem5.setText(bundle1.getString("FILE_QUIT")); // NOI18N
        jMenuItem5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu9.setMnemonic('V');
        jMenu9.setText(bundle1.getString("VIEW")); // NOI18N

        jMenuItem33.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
        jMenuItem33.setMnemonic('d');
        jMenuItem33.setText(bundle1.getString("VIEW_PANEL_DATA")); // NOI18N
        jMenuItem33.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem33);

        jMenuItem34.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
        jMenuItem34.setMnemonic('t');
        jMenuItem34.setText(bundle1.getString("VIEW_PANEL_TOPOLOGY")); // NOI18N
        jMenuItem34.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem34);

        jMenuItem35.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
        jMenuItem35.setMnemonic('f');
        jMenuItem35.setText(bundle1.getString("VIEW_PANEL_ERROR_PLOT")); // NOI18N
        jMenuItem35.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem35);

        jMenuItem36.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
        jMenuItem36.setMnemonic('i');
        jMenuItem36.setText(bundle1.getString("VIEW_PANEL_OUTPUT")); // NOI18N
        jMenuItem36.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem36);

        jMenuBar1.add(jMenu9);

        jMenu2.setMnemonic('T');
        jMenu2.setText(bundle1.getString("DATA")); // NOI18N

        jMenu3.setText(bundle1.getString("DATA_FILL")); // NOI18N

        jMenuItem6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jMenuItem6.setText(bundle1.getString("DATA_FILL_MONO")); // NOI18N
        jMenuItem6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jMenuItem7.setText(bundle1.getString("DATA_FILL_RAND")); // NOI18N
        jMenuItem7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jMenuItem8.setText(bundle1.getString("DATA_FILL_RAND_GAUSS")); // NOI18N
        jMenuItem8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenu2.add(jMenu3);

        jMenu6.setText(bundle1.getString("DATA_TRANSFORM")); // NOI18N

        jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jMenuItem2.setText(bundle1.getString("DATA_TRANSFORM_AUTO_ALL")); // NOI18N
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem2);

        jMenuItem14.setText(bundle1.getString("DATA_TRANSFORM_CUST_ALL")); // NOI18N
        jMenuItem14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem14);

        jMenuItem25.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        jMenuItem25.setText(bundle1.getString("DATA_TRANSFORM_AUTO_SEL")); // NOI18N
        jMenuItem25.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem25);

        jMenuItem24.setText(bundle1.getString("DATA_TRANSFORM_CUST_SEL")); // NOI18N
        jMenuItem24.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem24);

        jMenu2.add(jMenu6);

        jMenuItem10.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
        jMenuItem10.setText(bundle1.getString("DATA_ADD_ROW")); // NOI18N
        jMenuItem10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText(bundle1.getString("DATA_REMOVE_SELECTED")); // NOI18N
        jMenuItem11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem9.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK));
        jMenuItem9.setText(bundle1.getString("DATA_CLEAR_SELECTED_DATASET")); // NOI18N
        jMenuItem9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jMenuItem1.setText(bundle1.getString("DATA_CLEAR_ALL")); // NOI18N
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu5.setMnemonic('E');
        jMenu5.setText(bundle1.getString("TRAIN")); // NOI18N

        jMenuItem15.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        jMenuItem15.setText(bundle1.getString("TRAIN_NOW")); // NOI18N
        jMenuItem15.setEnabled(false);
        jMenuItem15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem27.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        jMenuItem27.setText(bundle1.getString("TRAIN_PAUSE")); // NOI18N
        jMenuItem27.setEnabled(false);
        jMenuItem27.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        jMenuItem3.setText(bundle1.getString("TRAIN_STOP")); // NOI18N
        jMenuItem3.setEnabled(false);
        jMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);

        jMenuBar1.add(jMenu5);

        jMenu4.setMnemonic('G');
        jMenu4.setText(bundle1.getString("PLOTS")); // NOI18N

        jMenuItem4.setText(bundle1.getString("PLOTS_SAVE_IMG")); // NOI18N
        jMenuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem12.setText(bundle1.getString("PLOTS_SAVE_ERRORS")); // NOI18N
        jMenuItem12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenuItem26.setText(bundle1.getString("PLOTS_SAVE_GIF")); // NOI18N
        jMenuItem26.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem26);

        jMenuBar1.add(jMenu4);

        jMenu7.setMnemonic('j');
        jMenu7.setText(bundle1.getString("EXAMPLES")); // NOI18N

        jMenuItem32.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        jMenuItem32.setText(bundle1.getString("EXAMPLES_RELOAD")); // NOI18N
        jMenuItem32.setEnabled(false);
        jMenuItem32.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem32);
        jMenu7.add(jSeparator1);

        jMenuItem13.setText(bundle1.getString("EXAMPLES_SIN")); // NOI18N
        jMenuItem13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem13);

        jMenuItem16.setText(bundle1.getString("EXAMPLES_COS")); // NOI18N
        jMenuItem16.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem16);

        jMenuItem23.setText(bundle1.getString("EXAMPLES_SINC")); // NOI18N
        jMenuItem23.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem23);

        jMenuItem17.setText(bundle1.getString("EXAMPLES_JUMPY")); // NOI18N
        jMenuItem17.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem17);

        jMenuItem30.setText(bundle1.getString("EXAMPLES_VERY_JUMPY")); // NOI18N
        jMenuItem30.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem30);

        jMenuItem18.setText(bundle1.getString("EXAMPLES_TRIANGLE")); // NOI18N
        jMenuItem18.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem18);

        jMenuItem19.setText(bundle1.getString("EXAMPLES_SQARE")); // NOI18N
        jMenuItem19.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem19);

        jMenuItem21.setText(bundle1.getString("EXAMPLES_DUNNO")); // NOI18N
        jMenuItem21.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem21);

        jMenuItem22.setText(bundle1.getString("EXAMPLES_BATMAN")); // NOI18N
        jMenuItem22.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem22);

        jMenuItem28.setText(bundle1.getString("EXAMPLES_EKG_SYNTH")); // NOI18N
        jMenuItem28.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem28);

        jMenuItem20.setText(bundle1.getString("EXAMPLES_EKG_REAL")); // NOI18N
        jMenuItem20.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem20);

        jMenuBar1.add(jMenu7);

        jMenu8.setMnemonic('?');
        jMenu8.setText("?");

        jMenu10.setText(bundle1.getString("DEBUG")); // NOI18N

        jCheckBoxMenuItem3.setText(bundle1.getString("DEBUG_SHOW")); // NOI18N
        jCheckBoxMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxMenuItem3ActionPerformed(evt);
            }
        });
        jMenu10.add(jCheckBoxMenuItem3);

        jCheckBoxMenuItem4.setSelected(true);
        jCheckBoxMenuItem4.setText(bundle1.getString("DEBUG_STD_OUT")); // NOI18N
        jCheckBoxMenuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu10.add(jCheckBoxMenuItem4);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText(bundle1.getString("DEBUG_AA")); // NOI18N
        jCheckBoxMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu10.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem2.setText(bundle1.getString("DEBUG_FPS")); // NOI18N
        jCheckBoxMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxMenuItem2ActionPerformed(evt);
            }
        });
        jMenu10.add(jCheckBoxMenuItem2);

        jMenu8.add(jMenu10);

        jMenuItem29.setText(bundle1.getString("ABOUT")); // NOI18N
        jMenuItem29.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem29);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private FunctionMonospaced dialogMonospaced;
    private void jMenuItem6ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if(dialogMonospaced == null) dialogMonospaced = new FunctionMonospaced(MainWindow.this);
        dialogMonospaced.setVisible(true);
        selected.addAll(dialogMonospaced.getData());
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jRadioButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        setSelected(train);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        setSelected(validate);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        setSelected(generalize);
    }//GEN-LAST:event_jRadioButton3ActionPerformed
        
    private final BundleDecorator MSTR = new BundleDecorator("res.i18n.menu");
    private void setSelected(DatasetTableModel model) {
        selected = model;
        train     .selectEnabled(model == train);
        validate  .selectEnabled(model == validate);
        generalize.selectEnabled(model == generalize);
        jMenuItem9 .setText(i18n.__("Clear Selected Dataset (%s)", selected.getName()));
        jMenuItem25.setText(MSTR.__("DATA_TRANSFORM_AUTO_SEL", selected.getName()));
        jMenuItem24.setText(MSTR.__("DATA_TRANSFORM_CUST_SEL", selected.getName()));
    }
    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selected.selectAll();
        info("Selecting all elements of the '%s' dataset", selected.getName());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        selected.selectToggle();
        info("Inverting selection of the '%s' dataset", selected.getName());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selected.selectNone();
        info("Removing selection of the '%s' dataset", selected.getName());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            jFormattedTextField1.commitEdit();
            final double valor = ((Number)jFormattedTextField1.getValue()).doubleValue();
            final int nEleme = selected.getRowCount();
            selectElements((int)Math.round(nEleme * valor));     
            info("Selection %5.2f%% of the elements of %s %s", valor * 100.,
                  selected.getName(), jCheckBox1.isSelected() ? i18n.__("(Random)") : "");
        } catch (ParseException ignoreMe) {
            error("'%s' is not a decimal number", jFormattedTextField1.getText());
        }   
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            jFormattedTextField2.commitEdit();
            final int valor = ((Number)jFormattedTextField2.getValue()).intValue();
            selectElements(valor);       
            info("Selecting %d elements from the %s dataset %s", valor,
                  selected.getName(), jCheckBox1.isSelected() ? i18n.__("(Random)") : "");
        } catch (ParseException ignoreMe) {
            error("'%s' is not an integer", jFormattedTextField2.getText());
        } 
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        final int idx = jComboBox1.getSelectedIndex();
        final ArrayList<DatasetTableModel.Row> foo = selected.removeSelected();
        final String dest;
        switch (idx) {
            case  0: train     .addAll(foo); dest = train.getName(); break;
            case  1: validate  .addAll(foo); dest = validate.getName(); break;
            default: generalize.addAll(foo); dest = generalize.getName(); break;
        }
        info("Moving %d elements from '%s' -> '%s'", foo.size(), selected.getName(), dest);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem11ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        selected.removeSelected();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private FunctionMonospacedRand dialogMonospacedRand;
    private void jMenuItem7ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (dialogMonospacedRand == null) dialogMonospacedRand = new FunctionMonospacedRand(MainWindow.this);
        dialogMonospacedRand.setVisible(true);
        selected.addAll(dialogMonospacedRand.getData());
    }//GEN-LAST:event_jMenuItem7ActionPerformed
    
    private FunctionGaussianRand dialogGaussianRand;
    private void jMenuItem8ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        if (dialogGaussianRand == null) dialogGaussianRand = new FunctionGaussianRand(MainWindow.this);
        dialogGaussianRand.setVisible(true);
        selected.addAll(dialogGaussianRand.getData());
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        selected.selectAll();
        selected.removeSelected();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        train.selectAll();
        validate.selectAll();
        generalize.selectAll();
        train.removeSelected();
        validate.removeSelected();
        generalize.removeSelected();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        double maxVal = train.getMax();
        maxVal = Math.max(maxVal, validate  .getMax());
        maxVal = Math.max(maxVal, generalize.getMax());
        final double scale = 1 / maxVal;
        train     .scale(scale);
        validate  .scale(scale);
        generalize.scale(scale);
        info("Table values scaled using s = %f", scale);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jCheckBox2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (!jCheckBox2.isSelected()) {
            func.remove(points[0]);
            func.repaint();
        }
        updateGraphics(TRAIN);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jComboBox9ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        updateGraphics(TRAIN);
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jCheckBox4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        if (!jCheckBox4.isSelected()) {
            func.remove(points[2]);
            func.repaint();
        }
        updateGraphics(GENER);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        if (!jCheckBox3.isSelected()) {
            func.remove(points[1]);
            func.repaint();
        }
        updateGraphics(VALID);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jComboBox7ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        updateGraphics(VALID);
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox8ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        updateGraphics(GENER);
    }//GEN-LAST:event_jComboBox8ActionPerformed
    
    private final GString strTrain = new GString(0, 0, i18n.__("Training"));
    private final GString strValid = new GString(0, 0, i18n.__("Validation"));
    private void jButton11ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        chooseColor(i18n.__("Color for training"), TRAIN);
        drawErrors();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        chooseColor(i18n.__("Color for validation"), VALID);
        drawErrors();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        chooseColor(i18n.__("Color for generalization"), GENER);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton8ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        final int idx = jComboBox1.getSelectedIndex();
        final ArrayList<DatasetTableModel.Row> foo = selected.removeSelected();
        selected.addAll(foo);
        final String dest;
        switch (idx) {
            case  0: train     .addAll(foo); dest = train.getName(); break;
            case  1: validate  .addAll(foo); dest = validate.getName(); break;
            default: generalize.addAll(foo); dest = generalize.getName(); break;
        }
        info("Copying %d elements from '%s' -> '%s'", foo.size(), selected.getName(), dest);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jSpinner1StateChanged(ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        final int newNum = (Integer)jSpinner1.getValue();
        while (layers.getRowCount() - 2 < newNum) layers.addRow   ();
        while (layers.getRowCount() - 2 > newNum) layers.removeRow();
    }//GEN-LAST:event_jSpinner1StateChanged
    private CustomFunction dialogCustomFunc;
    private void jMenuItem14ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        if (dialogCustomFunc == null) dialogCustomFunc = new CustomFunction();
        dialogCustomFunc.setVisible(true);
        if (dialogCustomFunc.getExp4X() != null) {
            final Expression exp4x  = dialogCustomFunc.getExp4X();
            final Expression exp4fx = dialogCustomFunc.getExp4FX();
            final String    sexp4x  = dialogCustomFunc.getExp4XStr();
            final String    sexp4fx = dialogCustomFunc.getExp4FXStr();
            
            train     .applyToBoth(exp4x, sexp4x, exp4fx, sexp4fx);
            validate  .applyToBoth(exp4x, sexp4x, exp4fx, sexp4fx);
            generalize.applyToBoth(exp4x, sexp4x, exp4fx, sexp4fx);
            
            info("Applying '%s' to all the '%s'", sexp4x,  "x");
            info("Applying '%s' to all the '%s'", sexp4fx, "f(x)");
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private SwingWorker<Void, ErrorTableModel.Row> worker;
    private volatile boolean paused;
    private MLP mlp;
    private void jMenuItem15ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        if (worker != null) {
            worker.cancel(true);
        }
        
        worker = new SwingWorker<Void, ErrorTableModel.Row>() {
            @Override
            protected Void doInBackground() throws Exception {
                paused = false;
                try{
                    final String path = jTextField1.getText();
                    final String name = jTextField2.getText();
                    final File foo = new File(path);
                    
                    if (!foo.exists()) foo.mkdirs();
                    
                    setTraining(true);
                    errors.removeAll();
                    final Matrix[] trainSetI = new Matrix[train.getRowCount()];
                    final Matrix[] trainSetO = new Matrix[train.getRowCount()];
                    final Matrix[] validSetI = new Matrix[validate.getRowCount()];
                    final Matrix[] validSetO = new Matrix[validate.getRowCount()];

                    for (int i = 0, m = train.getRowCount(); i < m; i++) {
                        trainSetI[i] = new Matrix(1, 1);
                        trainSetI[i].position(0, 0, train.getDouble(i, 0));
                        trainSetO[i] = new Matrix(1, 1);
                        trainSetO[i].position(0, 0, train.getDouble(i, 1));
                    }
                    
                    for (int i = 0, m = validate.getRowCount(); i < m; i++) {
                        validSetI[i] = new Matrix(1, 1);
                        validSetI[i].position(0, 0, validate.getDouble(i, 0));
                        validSetO[i] = new Matrix(1, 1);
                        validSetO[i].position(0, 0, validate.getDouble(i, 1));
                    }

                    mlp = new MLP(layers.getLayers(), layers.getLayerActivation());

                    jFormattedTextField3.commitEdit();
                    jFormattedTextField4.commitEdit();
                    jFormattedTextField5.commitEdit();
                    
                    final int maxEpochs = (Integer)jSpinner2.getValue();
                    final double mse    = ((Number)jFormattedTextField3.getValue()).doubleValue();
                    final int saveEvery = (Integer)jSpinner3.getValue();
                    final double degradation = ((Number)jFormattedTextField5.getValue()).doubleValue();
                          double learnRate   = ((Number)jFormattedTextField4.getValue()).doubleValue();

                    switch (jComboBox2.getSelectedIndex()) {
                        case 0:  mlp.setTrainingType(MLP.STANDARD_BACKPROPAGATION); break;
                        case 1:  mlp.setTrainingType(MLP.MOMEMTUM_BACKPROPAGATION, 0.4); break;
                        default: mlp.setTrainingType(MLP.RESILENT_BACKPROPAGATION); break;
                    }

                    final double errt0 = mlp.error(trainSetI, trainSetO);
                    final double errv0 = mlp.error(validSetI, validSetO);
                    mlp.save(path + name.replace("{epoch}", "" + 0));

                    errors.addRow(0, errt0, errv0);
                    info("---------- Training ----------");
                    info("Topology: %s", layers);
                    TicToc tt = new TicToc();
                    tt.tic();
                    for (int i = 0; i < maxEpochs; i += saveEvery) {
                        final int currEpoch = i + saveEvery;
                        mlp.train(trainSetI, trainSetO, learnRate, saveEvery);
                        mlp.save(path + name.replace("{epoch}", "" + currEpoch));

                        final double errt = mlp.error(trainSetI, trainSetO);
                        final double errv = mlp.error(validSetI, validSetO);

                        publish(new ErrorTableModel.Row(currEpoch, errt, errv));

                        if (errt < mse) {
                            break;
                        }

                        while (paused && !worker.isCancelled()) {
                            jProgressBar1.setString(i18n.__("Paused"));
                            jProgressBar1.setIndeterminate(true);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ignoreMe) {
                                //This will happen if the worker is cancelled (or finishes)
                                //immediatly after the pause
                            }
                        }
                        
                        if (worker.isCancelled()) {
                            tt.toc();
                            jProgressBar1.setString(i18n.__("Aborted"));
                            jProgressBar1.setIndeterminate(false);
                            info("Training aborted by user at %.4fs", tt.getSecsTime());
                            break;
                        }

                        learnRate *= degradation;
                        
                        jProgressBar1.setIndeterminate(false);
                        jProgressBar1.setStringPainted(true);
                        jProgressBar1.setString(null);
                        jProgressBar1.setValue(currEpoch * 100 / maxEpochs);
                    }
                    
                    if (!worker.isCancelled()) {
                        tt.toc();
                        info("Training finished successfully in %.4fs", tt.getSecsTime());
                    }
                    
                    saveConfig(path);
                    saveData(path);
                    saveErrors(path);
                    jCheckBox4.setEnabled(true);
                    return null;
                } catch(Exception e) {
                    error("Training error: %s", e.toString());
                    return null;
                }
            }

            @Override
            protected void process(List<ErrorTableModel.Row> chunks) {
                debug("Processing %d sessions", chunks.size());
                errors.addRows(chunks);
            }

            @Override
            protected void done() {
                setTraining(false);
                paused = false;
            }
        };
        worker.execute();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private final ImageIcon play  = new ImageIcon(getClass().getResource("/res/icons/play.png"));
    private final ImageIcon pause = new ImageIcon(getClass().getResource("/res/icons/pause.png"));
    private boolean currently_training;
    private void setTraining(boolean training) {
        currently_training = training;
        jMenuItem15.setEnabled(!training);
        jButton14.setIcon(training ? pause : play);
        jMenuItem27.setEnabled(training);
        jMenuItem3.setEnabled(training);
        jButton16.setEnabled(training);
        jMenu7.setEnabled(!training);
        jMenu2.setEnabled(!training);
        jTable5.setEnabled(!training);
        enableComponents(jPanel1, !training);
        enableComponents(jPanel2, !training);
    }
    
    private void jButton7ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        JFileChooser jfc = new JFileChooser(jTextField1.getText()){
            @Override
            public void approveSelection() {
                if (!getSelectedFile().isFile()) {
                    super.approveSelection();
                }
            }
        };
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setDialogTitle(i18n.__("Select a folder to save the data"));
        jfc.setMultiSelectionEnabled(false);
        jfc.showDialog(null, i18n.__("Select Folder"));
        if (jfc.getSelectedFile() != null) {
            jTextField1.setText(jfc.getSelectedFile().getAbsolutePath() + File.separator);
            info("Current working directory: %s", jTextField1.getText());
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem5ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        final int opt = JOptionPane.showConfirmDialog(null, 
                i18n.__("Do really want to quit?"), 
                i18n.__("Are you sure?"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) {
            for (String s : i18n.getNotFound()) {
                warn("String '%s' not found", s);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (worker != null){
            worker.cancel(true);
            paused = false;
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        savePlot(i18n.__("Data"), func);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem12ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        savePlot(i18n.__("Errors"), errs);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        loadExample(jMenuItem13, new ExampleSin());
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem16ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        loadExample(jMenuItem16, new ExampleCos());
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        loadExample(jMenuItem17, new ExampleJumpy());
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        loadExample(jMenuItem18, new ExampleTriangles());
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        loadExample(jMenuItem19, new ExampleSquare());
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem21ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        loadExample(jMenuItem21, new ExampleMetodos());
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        loadExample(jMenuItem22, new ExampleBatman());
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        loadExample(jMenuItem23, new ExampleSinc());
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem20ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        loadExample(jMenuItem20, new ExampleEKG());
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem25ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        double maxVal = train.getMax();
        maxVal = Math.max(maxVal, validate  .getMax());
        maxVal = Math.max(maxVal, generalize.getMax());
        final double scale = 1 / maxVal;
        selected.scale(scale);
        info("Table '%s' values scaled using s = %f", selected.getName(), scale);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem24ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        if (dialogCustomFunc == null) dialogCustomFunc = new CustomFunction();
        dialogCustomFunc.setVisible(true);
        if (dialogCustomFunc.getExp4X() != null) {
            final Expression exp4x  = dialogCustomFunc.getExp4X();
            final Expression exp4fx = dialogCustomFunc.getExp4FX();
            final String sexp4x     = dialogCustomFunc.getExp4XStr();
            final String sexp4fx    = dialogCustomFunc.getExp4FXStr();
            
            selected.applyToBoth(exp4x, sexp4x, exp4fx, sexp4fx);
            
            final String sname = selected.getName();
            
            info("Applying '%s' to all the '%s' of table '%s'", sexp4x,  "x", sname);
            info("Applying '%s' to all the '%s' of table '%s'", sexp4fx, "f(x)", sname);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem10ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        selected.addRow(null, null);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem26ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        final JFileChooser jfc = new JFileChooser(jTextField1.getText());
        jfc.setSelectedFile(new File(jTextField1.getText(), "funcion.gif"));
        final int res = jfc.showSaveDialog(null);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final Gif gif = new Gif(func);
        final int pRow = jTable5.getSelectedRow();
        
        final int xoff =  func.getXSize() / 2 - 100;
        final int yoff = -func.getYSize() / 2 + 5;
        for (int i = 0; i < errors.getRowCount(); i ++) {
            final GString gs = new GString(xoff, yoff, i18n.__("Epoch: %d", errors.getEpoch(i)));
            gs.setPaint(Color.DARK_GRAY);
            func.add(gs);
            final int row = jTable5.convertRowIndexToView(i);
            jTable5.setRowSelectionInterval(row, row);
            gif.snapshot();
            func.remove(gs);
        }
        
        if (pRow != -1) jTable5.setRowSelectionInterval(pRow, pRow);
        gif.write(jfc.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jCheckBox5ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        errs.remove(errorPoints[TRAIN]);
        errs.remove(errorPoints[VALID]);
        errorPoints[TRAIN] = null;
        errorPoints[VALID] = null;
        drawErrors();
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jMenuItem27ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        paused = !paused;
        jMenuItem27.setText(paused ? i18n.__("Resume") : i18n.__("Pause"));
        jButton14.setIcon(paused ? play : pause);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jButton14ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (worker != null && !worker.isCancelled()) {
            jMenuItem27.doClick(0);
        } 
        jMenuItem15.doClick(0);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton16ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jMenuItem3.doClick(0);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jCheckBox6ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        updateGraphics(TRAIN);
        drawErrors();
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jMenuItem29ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        new AboutDialog(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem28ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        loadExample(jMenuItem28, new ExampleSyntheticEKG());
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem30ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        loadExample(jMenuItem30, new ExampleVeryJumpy());
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem32ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        loadExample(lastItem, null);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem34ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem33ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem35ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        final int opt = JOptionPane.showConfirmDialog(null, 
                i18n.__("Do really want to quit?"), 
                i18n.__("Are you sure?"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (opt == JOptionPane.NO_OPTION) {
            MainWindow.this.setVisible(true);
        } else {
            for (String s : i18n.getNotFound()) {
                debug("String '%s' not found", s);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem37ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        final JFileChooser jfc = new JFileChooser(jTextField1.getText());
        jfc.setFileFilter(new FileNameExtensionFilter("MLP config", "conf"));
        
        final int opt = jfc.showOpenDialog(null);
        if (opt == JFileChooser.CANCEL_OPTION) {
            return;
        }
        
        final File file = jfc.getSelectedFile();
        if (file != null && file.exists()) {
            readConfig(file);
        }
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jPanel3ComponentShown(ComponentEvent evt) {//GEN-FIRST:event_jPanel3ComponentShown
        updateGraphics(TRAIN);
        updateGraphics(VALID);
        updateGraphics(GENER);
    }//GEN-LAST:event_jPanel3ComponentShown

    private void jCheckBoxMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        errs.setUseAntiAliasing(jCheckBoxMenuItem1.isSelected());
        func.setUseAntiAliasing(jCheckBoxMenuItem1.isSelected());
        c4g.put("use.aa", jCheckBoxMenuItem1.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jCheckBoxMenuItem2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem2ActionPerformed
        errs.setShowFPS(jCheckBoxMenuItem2.isSelected());
        func.setShowFPS(jCheckBoxMenuItem2.isSelected());
        c4g.put("show.fps", jCheckBoxMenuItem2.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItem2ActionPerformed

    private void jCheckBoxMenuItem3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem3ActionPerformed
        DEBUG_ENABLED = jCheckBoxMenuItem3.isSelected();
        c4g.put("enable.debug", DEBUG_ENABLED);
    }//GEN-LAST:event_jCheckBoxMenuItem3ActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
        PS = jCheckBoxMenuItem4.isSelected() ? System.out : NullPrintStream.getInstance();
        c4g.put("use.std.out", jCheckBoxMenuItem4.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void jTabbedPane1FocusGained(FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        drawErrors();
    }//GEN-LAST:event_jTabbedPane1FocusGained
    
    private void chooseColor(String title, int idx) {
        final Color c = JColorChooser.showDialog(null, title, colors[idx]);
        if (c == null) {
            return;
        }
        
        debug("'%s' color was changed to 0x%h'", title, c.getRGB());
        colors[idx] = c;
        updateGraphics(idx);
        drawErrors();
    }
    
    private void selectElements(int value) {
        if (value >= selected.getRowCount()) {
            selected.selectAll();
        } else {
            if (jCheckBox1.isSelected()) {
                final Random rand = ThreadLocalRandom.current();
                while (selected.getSelectedCount() < value) {
                    selected.setValueAt(true, rand.nextInt(selected.getRowCount()), 0);
                }
            } else {
                for (int i = 0; i < value; i++) {
                    selected.setValueAt(true, i, 0);
                }
            }
        }
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final ButtonGroup buttonGroup1 = new ButtonGroup();
    private final JButton jButton1 = new JButton();
    private final JButton jButton14 = new JButton();
    private final JButton jButton16 = new JButton();
    private final JButton jButton2 = new JButton();
    private final JButton jButton3 = new JButton();
    private final JButton jButton4 = new JButton();
    private final JButton jButton5 = new JButton();
    private final JButton jButton6 = new JButton();
    private final JButton jButton7 = new JButton();
    private final JButton jButton8 = new JButton();
    private final JCheckBox jCheckBox1 = new JCheckBox();
    private final JCheckBox jCheckBox2 = new JCheckBox();
    private final JCheckBox jCheckBox3 = new JCheckBox();
    private final JCheckBox jCheckBox4 = new JCheckBox();
    private final JCheckBox jCheckBox5 = new JCheckBox();
    private final JCheckBox jCheckBox6 = new JCheckBox();
    private final JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem();
    private final JComboBox<String> jComboBox1 = new JComboBox<>();
    private final JComboBox<String> jComboBox2 = new JComboBox<>();
    private final JComboBox<String> jComboBox7 = new JComboBox<>();
    private final JComboBox<String> jComboBox8 = new JComboBox<>();
    private final JComboBox<String> jComboBox9 = new JComboBox<>();
    private final JFormattedTextField jFormattedTextField1 = new JFormattedTextField();
    private final JFormattedTextField jFormattedTextField2 = new JFormattedTextField();
    private final JFormattedTextField jFormattedTextField3 = new JFormattedTextField(FORMATTER);
    private final JFormattedTextField jFormattedTextField4 = new JFormattedTextField(FORMATTER);
    private final JFormattedTextField jFormattedTextField5 = new JFormattedTextField(FORMATTER);
    private final JLabel jLabel2 = new JLabel();
    private final JLabel jLabel3 = new JLabel();
    private final JLabel jLabel4 = new JLabel();
    private final JLabel jLabel5 = new JLabel();
    private final JMenu jMenu2 = new JMenu();
    private final JMenu jMenu5 = new JMenu();
    private final JMenu jMenu7 = new JMenu();
    private final JMenuBar jMenuBar1 = new JMenuBar();
    private final JMenuItem jMenuItem1 = new JMenuItem();
    private final JMenuItem jMenuItem13 = new JMenuItem();
    private final JMenuItem jMenuItem15 = new JMenuItem();
    private final JMenuItem jMenuItem16 = new JMenuItem();
    private final JMenuItem jMenuItem17 = new JMenuItem();
    private final JMenuItem jMenuItem18 = new JMenuItem();
    private final JMenuItem jMenuItem19 = new JMenuItem();
    private final JMenuItem jMenuItem20 = new JMenuItem();
    private final JMenuItem jMenuItem21 = new JMenuItem();
    private final JMenuItem jMenuItem22 = new JMenuItem();
    private final JMenuItem jMenuItem23 = new JMenuItem();
    private final JMenuItem jMenuItem24 = new JMenuItem();
    private final JMenuItem jMenuItem25 = new JMenuItem();
    private final JMenuItem jMenuItem27 = new JMenuItem();
    private final JMenuItem jMenuItem28 = new JMenuItem();
    private final JMenuItem jMenuItem3 = new JMenuItem();
    private final JMenuItem jMenuItem30 = new JMenuItem();
    private final JMenuItem jMenuItem32 = new JMenuItem();
    private final JMenuItem jMenuItem9 = new JMenuItem();
    private final JPanel jPanel1 = new JPanel();
    private final JPanel jPanel10 = new JPanel();
    private final JPanel jPanel12 = new JPanel();
    private final JPanel jPanel13 = new JPanel();
    private final JPanel jPanel2 = new JPanel();
    private final JPanel jPanel3 = new JPanel();
    private final JPanel jPanel5 = new JPanel();
    private final JPanel jPanel6 = new JPanel();
    private final JPanel jPanel7 = new JPanel();
    private final JPanel jPanel8 = new JPanel();
    private final JPanel jPanel9 = new JPanel();
    private final JProgressBar jProgressBar1 = new JProgressBar();
    private final JRadioButton jRadioButton1 = new JRadioButton();
    private final JRadioButton jRadioButton2 = new JRadioButton();
    private final JRadioButton jRadioButton3 = new JRadioButton();
    private final JScrollPane jScrollPane1 = new JScrollPane();
    private final JScrollPane jScrollPane2 = new JScrollPane();
    private final JScrollPane jScrollPane3 = new JScrollPane();
    private final JScrollPane jScrollPane4 = new JScrollPane();
    private final JScrollPane jScrollPane5 = new JScrollPane();
    private final JScrollPane jScrollPane6 = new JScrollPane();
    private final JPopupMenu.Separator jSeparator1 = new JPopupMenu.Separator();
    private final JSeparator jSeparator3 = new JSeparator();
    private final JSeparator jSeparator4 = new JSeparator();
    private final JSeparator jSeparator5 = new JSeparator();
    private final JSpinner jSpinner1 = new JSpinner();
    private final JSpinner jSpinner2 = new JSpinner();
    private final JSpinner jSpinner3 = new JSpinner();
    private final JSplitPane jSplitPane1 = new JSplitPane();
    private final JSplitPane jSplitPane2 = new JSplitPane();
    private final JTabbedPane jTabbedPane1 = new JTabbedPane();
    private final JTable jTable1 = new JTable();
    private final JTable jTable2 = new JTable();
    private final JTable jTable3 = new JTable();
    private final JTable jTable4 = new JTable();
    private final JTable jTable5 = new JTable();
    private final JTextField jTextField1 = new JTextField();
    private final JTextField jTextField2 = new JTextField();
    private final JTextPane jTextPane1 = new JTextPane();
    // End of variables declaration//GEN-END:variables

    private void centerHeaders() {
        jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable2.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable3.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable4.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable5.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
    }

    private void centerCells() {
        ((JLabel)jTable1.getDefaultRenderer(String.class))
                        .setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel)jTable1.getDefaultRenderer(Integer.class))
                        .setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    public void enableComponents(Container container, boolean enable) {
        for (final Component component : container.getComponents()) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

    private final String header = "<html><b>%s (%d/%d)</b></html>";
    private void initModels() {
        jTable1.setModel(layers);
        jTable2.setModel(train);
        jTable3.setModel(validate);
        jTable4.setModel(generalize);
        jTable5.setModel(errors);
        train  .selectEnabled(true);
        
        train.addTableModelListener(new TableModelListener() {
            @Override public void tableChanged(TableModelEvent e) {
                final int sel = train.getSelectedCount();
                final int row = train.getRowCount();
                jLabel2.setText(String.format(header, i18n.__("Training"), sel, row));
                checkTrainEnabled();
                updateGraphics(TRAIN);
            }
        });
        validate.addTableModelListener(new TableModelListener() {
            @Override public void tableChanged(TableModelEvent e) {
                final int sel = validate.getSelectedCount();
                final int row = validate.getRowCount();
                jLabel3.setText(String.format(header, i18n.__("Validation"), sel, row));
                checkTrainEnabled();
                updateGraphics(VALID);
            }
        });
        generalize.addTableModelListener(new TableModelListener() {
            @Override public void tableChanged(TableModelEvent e) {
                final int sel = generalize.getSelectedCount();
                final int row = generalize.getRowCount();
                jLabel4.setText(String.format(header, i18n.__("Generalization"), sel, row));
                checkTrainEnabled();
                updateGraphics(GENER);
            }
        });
        errors.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                drawErrors();
                updateGraphics(GENER);
            }
        });
    
        final ListSelectionModel tsm = jTable5.getSelectionModel();
        tsm.addListSelectionListener(new ListSelectionListener() {
            private int lasIdx = 0;
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int curIdx = e.getLastIndex();
                    if (lasIdx == curIdx) {
                        curIdx = e.getFirstIndex();
                    }
                    
                    try {
                        selectEpoch(jTable5.convertRowIndexToModel(curIdx));
                    } catch (Exception ex) {}
                    
                    lasIdx = curIdx;
                }
            }
        });
        
        //Always show epoch error first
        final TableRowSorter<ErrorTableModel> sorter = new TableRowSorter<ErrorTableModel>(errors) {
            {
                List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                setSortKeys(sortKeys);
            }
            @Override
            public int convertRowIndexToModel(int index) {
                try {
                    return super.convertRowIndexToModel(index);
                } catch (Exception e) {
                    //This tends to fail since there's no way of stopping the worker in mid training
                    //At any rate make it return 0 so we can actually see the epoch with the error
//                    error("Error retrieving columns (%s)", e.getLocalizedMessage());
                    return 0;
                }
            }
        };
        
        jTable5.setRowSorter(sorter);
        
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable5.getTableHeader().setReorderingAllowed(false);
    }
    
    private GLine epLine;
    private void selectEpoch(int epoch) {
        final int epno = Math.round((float)errors.getEpoch(epoch));
        //@TODO check if the epoch is actually in range before plotting...
        errs.remove(epLine);
        if (epno != -1) {
            final double maxx = errors.getEpoch(errors.getRowCount() - 1);        
            final double sx   = 1. / maxx * (errs.getXSize() - ERR_X_OFFSET);
            final int offset  = (int)(sx * epno);
            epLine = new GLine(offset, -50, offset, 1000);
            epLine.setPaint(Color.CYAN);
            errs.add(epLine);
            mlp = loadMlp(epno);
            updateGraphics(GENER);
            drawErrors();
        }
    }
    
    private MLP loadMlp(int epoch) {
        if (epoch == -1) return null;
        
        final String path = jTextField1.getText();
        final String name = jTextField2.getText();
        final String fname = path + name.replace("{epoch}", "" + epoch);
        debug("Reading MLP from '%s'", fname);
                
        try (FileInputStream fis = new FileInputStream(fname);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (MLP)ois.readObject();
        } catch (Exception ex) {
            error("Error reading MLP from '%s' (%s)", fname, ex);
        }
        
        return null;
    }
    private void initGraphs() {
        BorderLayout layout = new BorderLayout();
        jPanel6.setLayout(layout);
        jPanel6.add(func, BorderLayout.CENTER);
        func.setVisible(true);
        func.setUseFullArea(true);
        func.setCenterOrigin(true);
        func.setInvertYAxis(true);
        GAxis funcAxis = new GAxis(-800, 800, -250, 250);
        funcAxis.setPaint(Color.GRAY);
        funcAxis.setGridColor(Color.LIGHT_GRAY);
        funcAxis.drawLinesH(true);
        funcAxis.drawLinesV(true);
        func.addFixed(funcAxis);
        
        func.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGraphics(TRAIN);
                updateGraphics(VALID);
                updateGraphics(GENER);
            }

            @Override public void componentMoved(ComponentEvent e) {}
            @Override public void componentShown(ComponentEvent e) {}
            @Override public void componentHidden(ComponentEvent e) {}
        });
        
        layout = new BorderLayout();
        jPanel7.setLayout(layout);
        jPanel7.add(errs, BorderLayout.CENTER);
        errs.setVisible(true);
        errs.setUseFullArea(true);
        errs.setCenterOrigin(false);
        errs.setInvertYAxis(true);
        errs.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension dim = errs.getSize();
                errs.moveOrigin(ERR_X_OFFSET, dim.height - ERR_X_OFFSET);
                drawErrors();
            }

            @Override public void componentMoved(ComponentEvent e) {}
            @Override public void componentShown(ComponentEvent e) {}
            @Override public void componentHidden(ComponentEvent e) {}
        });
        
        GAxis axis = new GAxis(-50, 1500, -50, 500);
        axis.setPaint(Color.GRAY);
        axis.setGridColor(Color.LIGHT_GRAY);
        axis.drawLinesH(true);
        axis.drawLinesV(true);
        errs.addFixed(axis);
        
        updateGraphics(TRAIN);
        updateGraphics(VALID);
        updateGraphics(GENER);
    }

    private void initMisc() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Logistic");
        comboBox.addItem("ArcTan");
        comboBox.addItem("TanH");
        comboBox.addItem("Gaussian");
        comboBox.addItem("Sinc");
        
        TableColumn tc = jTable1.getColumnModel().getColumn(2);
        tc.setCellEditor(new DefaultCellEditor(comboBox));
        String home = c4g.get("last.path").isEmpty()
                    ? System.getProperty("user.home") 
                    : c4g.get("last.path");
        
        jTextField1.setText(home
                          + File.separator + "mrft"
                          + File.separator + i18n.__("Project_1")
                          + File.separator);
        
        Font tFnt = jTable1.getFont();
             tFnt = new Font(Font.MONOSPACED, tFnt.getStyle(), tFnt.getSize());
             
        jTable1.setFont(tFnt);
        jTable2.setFont(tFnt);
        
        Font lFnt = jLabel2.getFont().deriveFont(Font.BOLD);
        jLabel2.setFont(lFnt);
        jLabel3.setFont(lFnt);
        jLabel4.setFont(lFnt);
        
        jSplitPane2.setDividerLocation(0.5);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setResizeWeight(0);
        
        jCheckBoxMenuItem1.setSelected(c4g.getBool("use.aa"));
        jCheckBoxMenuItem2.setSelected(c4g.getBool("show.fps"));
        jCheckBoxMenuItem3.setSelected(c4g.getBool("enable.debug"));
        jCheckBoxMenuItem4.setSelected(c4g.getBool("use.std.out"));
        
        if (c4g.getBool("agressive.redraw")) {
            errs.setAutoRepaint(true);
            func.setAutoRepaint(true);
            errs.setRepaintDelay(c4g.getInt("agressive.redraw.interval"));
            func.setRepaintDelay(c4g.getInt("agressive.redraw.interval"));
        } 
        
        errs.setUseAntiAliasing(c4g.getBool("use.aa"));
        func.setUseAntiAliasing(c4g.getBool("use.aa"));                                                
        errs.setShowFPS(c4g.getBool("show.fps"));
        func.setShowFPS(c4g.getBool("show.fps"));
        
        jCheckBox2.setSelected(c4g.getBool("train.plot"));
        jCheckBox3.setSelected(c4g.getBool("valid.plot"));
        jCheckBox4.setSelected(c4g.getBool("gener.plot"));
        
        jComboBox9.setSelectedIndex(c4g.getInt("train.format"));
        jComboBox7.setSelectedIndex(c4g.getInt("valid.format"));
        jComboBox8.setSelectedIndex(c4g.getInt("gener.format"));
        
        colors[TRAIN] = c4g.getColor("train.color");
        colors[VALID] = c4g.getColor("valid.color");
        colors[GENER] = c4g.getColor("gener.color");
        
        jRadioButton1.doClick(0);
    }
    
    private void readFiles(File[] files, DatasetTableModel table) {
        if (table == null) {
            table = selected;
        }
        
        for (File file : files) {
            info("Reading file '%s'", file.getName());
            if (file.getName().endsWith(".csv"))  table.addAll(readFile(file));
            if (file.getName().endsWith(".conf")) readConfig(file);
        }        
    }

    public ArrayList<DatasetTableModel.Row> readFile(File file) {
        ArrayList<DatasetTableModel.Row> ret = new ArrayList<>(128);
        
        try (FileReader fis = new FileReader(file);
             BufferedReader br = new BufferedReader(fis)) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] fo = line.trim().split(",");
                ret.add(new DatasetTableModel.Row(false, 
                    Double.parseDouble(fo[0]), 
                    Double.parseDouble(fo[1]))
                );
            }
        } catch (Exception e) {
            error("Error reading file '%s' (%s)", file.getName(), e);
        }
        
        info("%d rows read from file '%s'", ret.size(), file.getName());
        
        return ret;
    }
    
    public void loadErrorFile(File file) {
        try (FileReader fis = new FileReader(file);
             BufferedReader br = new BufferedReader(fis)) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] fo = line.trim().split(",");
                errors.addRow(
                    Integer.parseInt(fo[0]), 
                    Double.parseDouble(fo[1]), 
                    Double.parseDouble(fo[2])
                );
            }
        } catch (Exception e) {
            error("Error reading file '%s' (%s)", file.getName(), e);
        }
        
        info("%d rows read from file '%s'", errors.getRowCount(), file.getName());
    }

    private void savePlot(String name, Canvas canvas) {
        final JFileChooser jfc = new JFileChooser(jTextField1.getText());
        final int out = jfc.showSaveDialog(null);
        
        if (out != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        final File file = jfc.getSelectedFile();
        if (file.exists()) {
            final int res = JOptionPane.showConfirmDialog(null,
                    i18n.__("Overwrite file?"),
                    i18n.__("Are you sure?"),
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
            );
            if (res == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        try {
            Utils.saveScreenshot(canvas, file.getAbsolutePath(), true);
            info("'%s' plot image saved as '%s'", name, file.getName());
        } catch (IOException ex) {
            error("Error writing file '%s' (%s)", file.getName(), ex);
        }
    }

    private JMenuItem lastItem;
    private void loadExample(JMenuItem menu, Example ex) {
        if (ex == null && menu != null) {
            menu.doClick(0);
            return;
        }
        
        if (menu != null) {
            info("Example '%s' loaded", ex.getName());
        }
        
        jMenuItem32.setText(i18n.__("<html>Reload <code>'%s'</code></html>", ex.getName()));
        jMenuItem32.setEnabled(true);
        lastItem = menu;
        jMenuItem1.doClick(0);
        errors.removeAll();
        jSpinner1.setValue(ex.loadTable(layers));
        ex.loadTrainData(train);
        ex.loadValidationData(validate);
        ex.loadGeneralizationData(generalize);
        jComboBox2.setSelectedIndex(ex.backpropagation());
        jSpinner2.setValue(ex.maxEpochs());
        jSpinner3.setValue(ex.saveEvery());
        jFormattedTextField4.setValue(ex.learnRate());
        jFormattedTextField5.setValue(ex.degradation());
        jFormattedTextField3.setValue(ex.mse());
        jTextField1.setText(ex.getFolder());
        jTextField2.setText(ex.getWeightFile());
        jCheckBox2.setSelected(ex.plotTraining());
        jComboBox9.setSelectedIndex(ex.trainingFormat());
        jCheckBox3.setSelected(ex.plotValidation());
        jComboBox7.setSelectedIndex(ex.validationFormat());
        jCheckBox4.setSelected(ex.plotGeneralization());
        jComboBox8.setSelectedIndex(ex.generalizationFormat());
        jCheckBox5.setSelected(ex.smoothError());
        jCheckBox6.setSelected(ex.showLabels());
    }
    
    private static final DecimalFormat FORMATTER = new DecimalFormat(c4g.get("number.format"));

    private void initListners() {
        FileDrop fd = new FileDrop(jScrollPane2, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                readFiles(files, train);
            }
        });
        fd = new FileDrop(jScrollPane3, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                readFiles(files, validate);
            }
        });
        fd = new FileDrop(jScrollPane4, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                readFiles(files, generalize);
            }
        });
        fd = new FileDrop(jPanel2, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                readFiles(files, null);
            }
        });
    }
    
    private boolean shouldDraw() {
        //@TODO should redraw after a DEICONIZATION...
        return jTabbedPane1.getSelectedIndex() == 2 && (getExtendedState() & ICONIFIED) == 0;
    }
    
    private synchronized void drawErrors() {
        if (!shouldDraw()) return;
        //@FIXME this should only be removed on selection change and on resize events... so lazy...
        errs.remove(strTrain);
        errs.remove(strValid);
        
        if (jCheckBox6.isSelected()) {
            final int xs = errs.getXSize() - ERR_X_OFFSET;
            final int ys = errs.getYSize() - ERR_Y_OFFSET;
            strTrain.move(xs - 120, ys - 15);
            strValid.move(xs - 120, ys - 30);
            errs.add(strTrain);
            errs.add(strValid);            
            strTrain.setPaint(colors[0]);
            strValid.setPaint(colors[1]);
        } 
        
        if (jCheckBox5.isSelected()) {
            drawErrors0();
        } else {
            drawErrors1();
        }
    }
    
    private final GMultiPoint[] errorPoints = new GMultiPoint[2];
    private final BasicStroke errStroke = new BasicStroke(1.5f);
    private final int errRes = c4g.getInt("smooth.error.res");
    private synchronized void drawErrors0() {
        if (errors.getRowCount() == 0) {
            errs.repaint();
            return;
        }
        
        if (errors.getRowCount() < 3) {
            return;
        }
        
        if (errorPoints[TRAIN] == null) {
            errorPoints[TRAIN] = new GPath(128);
            errorPoints[VALID] = new GPath(128);
            errorPoints[TRAIN].setStroke(errStroke);
            errorPoints[VALID].setStroke(errStroke);
            
            errs.add(errorPoints[TRAIN]);
            errs.add(errorPoints[VALID]);
        }
        
        errorPoints[TRAIN].setPaint(colors[TRAIN]);
        errorPoints[VALID].setPaint(colors[VALID]);
        
        errorPoints[TRAIN].clear();
        errorPoints[VALID].clear();
        
        //Los datos tienen que estar ordenados para spline
        final double[][] temp = errors.getData();
        final Integer [] idxs = new Integer[temp[0].length];
        
        for(int i = 0; i < idxs.length; i++) idxs[i] = i;
        
        Arrays.sort(idxs, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2){
                return Double.compare(temp[0][o1], temp[0][o2]);
            }
        });
        
        final double[][]data = new double[3][temp[0].length - 1];
        for (int j = 0; j < temp[0].length - 1; j++) {
            data[0][j] = temp[0][idxs[j]];
            data[1][j] = temp[1][idxs[j]];
            data[2][j] = temp[2][idxs[j]];
        }
        
        final Spline spt = Spline.createCubicSpline(data[0], data[1]);
        final Spline spv = Spline.createCubicSpline(data[0], data[2]);
        
        final double step = (data[0][data[0].length - 1] - data[0][0]) / errRes;
        double start = data[0][1];
        final double[][] datafinal = new double[2][errRes];
        for (int i = 0; i < errRes; i++) {
            datafinal[TRAIN][i] = spt.interpolate(i * step + start);
            datafinal[VALID][i] = spv.interpolate(i * step + start);
        }
        
        double maxx = data[0][data[0].length - 1];
        double maxy = 0;
        
        for (int i = 0, m = errRes; i < m; i++) {
            maxy = Math.max(maxy, datafinal[TRAIN][i]);
            maxy = Math.max(maxy, datafinal[VALID][i]);
        }
        
        final double sx = 1. / maxx * (errs.getXSize() - ERR_X_OFFSET);
        final double sy = 1. / maxy * (errs.getYSize() - ERR_Y_OFFSET);
        for (int i = 0; i < errRes; i++) {
            final int xx = (int)(sx * i * step);
            errorPoints[TRAIN].append(xx, (int)(sy * datafinal[TRAIN][i]));
            errorPoints[VALID].append(xx, (int)(sy * datafinal[VALID][i]));
        }
        errs.repaint();
    }
    
    private void drawErrors1() {
        //@FIXME this is mostly unnecessary... only new values should be added
        if (errors.getRowCount() == 0) {
            errs.repaint();
            return;
        }
        if (errorPoints[TRAIN] == null) {
            errorPoints[TRAIN] = new GPointArray(128);
            errorPoints[VALID] = new GPointArray(128);
            errs.add(errorPoints[TRAIN]);
            errs.add(errorPoints[VALID]);
        }
        
        errorPoints[TRAIN].clear();
        errorPoints[VALID].clear();
        errorPoints[TRAIN].setPaint(colors[TRAIN]);
        errorPoints[VALID].setPaint(colors[VALID]);
        
        ((GPointArray)errorPoints[TRAIN]).setCrossSize(1);
        ((GPointArray)errorPoints[VALID]).setCrossSize(1);
        
        double maxx = errors.getEpoch(errors.getRowCount() - 1);
        double maxy = 0;
        
        for (int i = 1, m = errors.getRowCount(); i < m; i++) {
            maxy = Math.max(maxy, errors.getDouble(i, TRAIN));
            maxy = Math.max(maxy, errors.getDouble(i, VALID));
        }
        
        final double sx = 1. / maxx * (errs.getXSize() - ERR_X_OFFSET);
        final double sy = 1. / maxy * (errs.getYSize() - ERR_Y_OFFSET);
        
        final int off = ((int)(sx * ERR_X_OFFSET));
        for (int i =  1, m = errors.getRowCount(); i < m; i++) {
            final int ep = (int)(sx * Math.round((float)errors.getEpoch(i))) - off;
            errorPoints[TRAIN].append(ep, (int)(sy * errors.getDouble(i, TRAIN)));
            errorPoints[VALID].append(ep, (int)(sy * errors.getDouble(i, VALID)));
        }
        errs.repaint();
    }
    
    private GString labTrain, labValid, labGen;
    private final GMultiPoint[] points = new GMultiPoint[3];
    private final JComboBox<?>[] list = {jComboBox9, jComboBox7, jComboBox8};
    private final DatasetTableModel[] datas = {train, validate, generalize};
    private final JCheckBox[] selections = {jCheckBox2, jCheckBox3, jCheckBox4};
    private final Color[] colors = {new Color(0x6666FF), new Color(0xFF9900), new Color(0xFF0000)};
    private synchronized void updateGraphics(int idx) {
        if (!shouldDraw()) return;
        //@FIXME this sucks... but c'mon! do you want to write it???
        func.remove(labTrain);
        func.remove(labValid);
        func.remove(labGen);
        
        if (jCheckBox6.isSelected()) {
            final int xs = func.getXSize() / 2;
            final int ys = func.getYSize() / 2;
            int posy = 15;
            
            if (jCheckBox2.isSelected()) {
                labTrain = new GString(xs - 120, ys - posy, i18n.__("Training"));
                labTrain.setPaint(colors[TRAIN]);
                func.add(labTrain);
                posy += 15;
            }
            if (jCheckBox3.isSelected()) {
                labValid = new GString(xs - 120, ys - posy, i18n.__("Validation"));
                labValid.setPaint(colors[VALID]);
                func.add(labValid);
                posy += 15;
            }
            if (jCheckBox4.isSelected()) {
                labGen = new GString(xs - 120, ys - posy, i18n.__("Generalization"));
                labGen.setPaint(colors[GENER]);
                func.add(labGen);
                posy += 15;
            }
        } 
        
        if (list[idx] == null) {
            list[TRAIN] = jComboBox9;
            list[VALID] = jComboBox7;
            list[GENER] = jComboBox8;
            selections[TRAIN] = jCheckBox2;
            selections[VALID] = jCheckBox3;
            selections[GENER] = jCheckBox4;
            func.repaint();
            return;
        }
        
        if (mlp == null && idx == GENER) {
            return;
        }
        
        func.remove(points[idx]);
        
        if (!selections[idx].isSelected()) {
            func.repaint();
            return;
        }
        
        if (list[idx].getSelectedIndex() == PLOT_PATHS) {
            points[idx] = new GPath(datas[idx].getRowCount());
        } else {
            points[idx] = new GPointArray(datas[idx].getRowCount());
            if (list[idx].getSelectedIndex() == PLOT_CROSS) {
                ((GPointArray)points[idx]).setCrossSize(1);
            }
        }
        
        points[idx].setPaint(colors[idx]);
        final double[] maxs = maxs();
        
        final double sx = 1. / maxs[0] *  func.getXSize() / 2;
        final double sy = 1. / maxs[1] * (func.getYSize() / 2 - 10);

        DatasetTableModel dtm = datas[idx];
        if (idx == GENER) {
            Matrix pattern = new Matrix(1, 1);
            //Sorry for this crap but since MLP objects aren't exactly cloneable() and we do save
            //a loooooot of instances this is kinda the best way, besides that's why you should be
            //using a ramdrive for saving training data!
            MLP mlp2 = currently_training ? loadMlp(errors.getLastEpoch()) : mlp;
            if (mlp2 == null) return;
            for (int i = 0, m = dtm.getRowCount(); i < m; i++) {
                pattern.position(0, 0, dtm.getDouble(i, 0));
                points[GENER].append(
                    (int)(sx * dtm.getDouble(i, 0)), 
                    (int)(sy * mlp2.simulate(pattern).position(0, 0))
                );
            }
        } else {
            for (int i = 0, m = dtm.getRowCount(); i < m; i++) {
                points[idx].append(
                    (int)(sx * dtm.getDouble(i, 0)), 
                    (int)(sy * dtm.getDouble(i, 1))
                );
            }
        }

        if (list[idx].getSelectedIndex() == PLOT_PATHS) {
            //Only sort by X when using paths, otherwise is useless
            points[idx].sortByX();
        }
        func.add(points[idx]);
        func.repaint();
    }
    
    private double[] maxs(){
        double maxx = 0, maxy = 0;
        for (int idx = 0; idx < 3; idx++) {
            DatasetTableModel dtm = datas[idx];
            for (int i = 0, m = dtm.getRowCount(); i < m; i++) {
                maxx = Math.max(maxx, Math.abs(dtm.getDouble(i, 0)));
                if (idx != GENER) maxy = Math.max(maxy, Math.abs(dtm.getDouble(i, 1)));
            }
        }
        return new double[]{maxx, maxy};
    }
    
    private final String[] sfiles = {
        i18n.__("__training.csv"), 
        i18n.__("__validation.csv"), 
        i18n.__("__generalization.csv"),
        i18n.__("__errors.csv")
    };
    private void saveData(String fname) {
        DatasetTableModel[] data = {train, validate, generalize};
        
        for (int j = 0; j < 3; j++) {
            DatasetTableModel model = data[j];
            try (PrintStream out = new PrintStream(fname + sfiles[j])) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    out.format("%f, %f%n", 
                        model.getDouble(i, 0),
                        model.getDouble(i, 1)
                    );
                }
                info("Saving data to '%s'", fname + sfiles[j]);
            } catch (Exception ex) {
                error("Error writing file '%s' (%s)", sfiles[j], ex.getLocalizedMessage());
            }
        }
    }

    private void saveErrors(String fname) {
        info("Saving errors to '%s'", fname + sfiles[3]);
        try (PrintStream out = new PrintStream(fname + sfiles[3])) {
            for (int i = 0; i < errors.getRowCount(); i++) {
                out.format("%d, %f, %f%n", 
                    (int)errors.getEpoch(i),
                    errors.getDouble(i, 0),
                    errors.getDouble(i, 1)
                );
            }
        } catch (Exception ex) {
            error("Error writing file '%s' (%s)", sfiles[3], ex.getLocalizedMessage());
        }
        
    }

    private void readConfig(File file) {
        info("Cleaning current data");
        jMenuItem1.doClick(0);
        System.out.println(file);
        String path = file.getParent() + File.separator;
        System.out.println(path);
        jTextField1.setText(path);
        
        info("Loading Configuration File '%s'", file.getName());
        try (FileInputStream fis = new FileInputStream(file)) {
            ConfigWrapper cw = new ConfigWrapper(fis, path, this);
            cw.setColors(colors);
            loadExample(null, cw);
            loadErrorFile(cw.errorFile());
            updateGraphics(TRAIN);
            updateGraphics(VALID);
            updateGraphics(GENER);
            drawErrors();
        } catch (Exception e) {
            error("Error reading file '%s' (%s)", file.getName(), e.getLocalizedMessage());
        }
    }
    
    private final String INTERNAL_MLP_PATH    = "/res/i18n/mlp/default_%s.properties";
    private final String INTERNAL_MLP_PATH_EN = "/res/i18n/mlp/default.properties";
    private void saveConfig(String fname) {
        info("Saving configuration file '%s'", fname + "__mlp.conf");
        Locale locale = Locale.getDefault();
        String path = null;
        
        // Yeah we now have localized configs... 
        String fooPath = String.format(INTERNAL_MLP_PATH, locale.getLanguage());
        URL url = getClass().getResource(fooPath);
        if (url != null) {
            path = fooPath;
        }
        
        if (path == null) path = INTERNAL_MLP_PATH_EN;
        
        CommentedProperties save = new CommentedProperties();
        try (InputStream is = getClass().getResourceAsStream(path)) {
            save.load(is);
        } catch (Exception ex) {
            //@TODO show error message(?)
            warn("Unable to read the default template! (%s)", ex.getLocalizedMessage());
        }
        
        save.put("max.epochs", jSpinner2.getValue());
        save.put("save.every", jSpinner3.getValue());
        save.put("mce", ((Number) jFormattedTextField3.getValue()).doubleValue());
        save.put("leaning.rate", ((Number) jFormattedTextField4.getValue()).doubleValue());
        save.put("degradation", ((Number)jFormattedTextField5.getValue()).doubleValue());
        save.put("backpropagation", jComboBox2.getSelectedIndex());
        save.put("topology", layers.toString());
        save.put("working.directory", jTextField1.getText());
        save.put("weight.file", jTextField2.getText());
        save.put("data.train", sfiles[TRAIN]);
        save.put("data.valid", sfiles[VALID]);
        save.put("data.gener", sfiles[GENER]);
        save.put("data.error", sfiles[ERROR]);
        save.put("plot.train", jCheckBox2.isSelected());
        save.put("plot.train.format", jComboBox9.getSelectedIndex());
        save.put("plot.train.color", Integer.toHexString(colors[0].getRGB()));
        save.put("plot.valid", jCheckBox3.isSelected());
        save.put("plot.valid.format", jComboBox7.getSelectedIndex());
        save.put("plot.valid.color", Integer.toHexString(colors[1].getRGB()));
        save.put("plot.gener", jCheckBox4.isSelected());
        save.put("plot.gener.format", jComboBox8.getSelectedIndex());
        save.put("plot.gener.color", Integer.toHexString(colors[2].getRGB()));
        save.put("smooth.errors", jCheckBox5.isSelected());
        save.put("show.labels", jCheckBox6.isSelected());
        
        try (PrintStream out = new PrintStream(fname + "__mlp.conf")) {
            save.store(out, "");
        } catch (Exception ex) {
            error("Error writing file '%s' (%s)", fname + "__mlp.conf", ex.getLocalizedMessage());
        }
    }
    
    private final SimpleAttributeSet STYLE_ERROR   = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_INFO    = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_DEBUG   = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_WARNING = new SimpleAttributeSet();
    {        
        StyleConstants.setFontFamily(STYLE_DEBUG,   c4g.get     ("debug.font"));
        StyleConstants.setForeground(STYLE_DEBUG,   c4g.getColor("debug.foreground"));
        StyleConstants.setBackground(STYLE_DEBUG,   c4g.getColor("debug.background"));
        StyleConstants.setFontFamily(STYLE_INFO,    c4g.get     ("info.font"));
        StyleConstants.setForeground(STYLE_INFO,    c4g.getColor("info.foreground"));
        StyleConstants.setBackground(STYLE_INFO,    c4g.getColor("info.background"));
        StyleConstants.setFontFamily(STYLE_WARNING, c4g.get     ("warn.font"));
        StyleConstants.setForeground(STYLE_WARNING, c4g.getColor("warn.foreground"));
        StyleConstants.setBackground(STYLE_WARNING, c4g.getColor("warn.background"));
        StyleConstants.setFontFamily(STYLE_ERROR,   c4g.get     ("error.font"));
        StyleConstants.setForeground(STYLE_ERROR,   c4g.getColor("error.foreground"));
        StyleConstants.setBackground(STYLE_ERROR,   c4g.getColor("error.background"));
        StyleConstants.setBold      (STYLE_ERROR,   true);
    }
        
    private final StyledDocument doc = jTextPane1.getStyledDocument();
    private final static long START = System.nanoTime();
    
    private PrintStream PS = c4g.getBool("use.std.out") ? System.out : NullPrintStream.getInstance();
    private boolean DEBUG_ENABLED = c4g.getBool("enable.debug");
    
    public final void info(String msg, Object... args) {
        write(STYLE_INFO, msg, args);
    }
    
    public final void debug(String msg, Object... args) {
        if (DEBUG_ENABLED) {
            write(STYLE_DEBUG, msg, args);
        }
    }
    
    public final void warn(String msg, Object... args) {
        write(STYLE_WARNING, msg, args);
    }
    
    public final void error(String msg, Object... args) {
        write(STYLE_ERROR, msg, args);
    }
    
    public final void write(SimpleAttributeSet type, String msg, Object... args) {
        final double time = System.nanoTime() - START;
        final String stime = String.format("[%.3fs]: ", time / 1e9);
        final String stmsg = i18n.__(msg, args) + "\n";
        
        PS.append(stime);
        PS.append(stmsg);
        
        try {
            doc.insertString(doc.getLength(), stime, type);
            doc.insertString(doc.getLength(), stmsg, type);
            jTextPane1.setCaretPosition(doc.getLength());
        } catch(BadLocationException e) { 
            PS.println(e); 
        }
    }
    
    public void checkTrainEnabled() {
        final boolean trainEnabled = train     .getRowCount() > 0 &&
                                     validate  .getRowCount() > 0 &&
                                     generalize.getRowCount() > 0;
        
        jMenuItem15.setEnabled(trainEnabled);
        jButton14.setEnabled(trainEnabled);
    }
    
    private final class DecimalFormatRenderer extends DefaultTableCellRenderer {
        public DecimalFormatRenderer() {
            setHorizontalAlignment(SwingConstants.TRAILING);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object val, boolean isSelected,
                boolean hasFocus, int row, int col) {
            try {
                if (val != null) {
                    val = FORMATTER.format(val);
                }
            } catch (Exception e) {
                warn("Unable to parse '%s' (%s)", val, e.getLocalizedMessage());
            }

            return super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
        }
    }
}

