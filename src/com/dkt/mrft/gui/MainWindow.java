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
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
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
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
public final class MainWindow extends javax.swing.JFrame {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.misc");
    private static final Config c4g = Config.get();

    // Variables declaration - do not modify
    private final JComboBox<String> backpropBox = new JComboBox<>();
    private final JMenuItem dataClearAllMI = new JMenuItem();
    private final JMenuItem dataClearSelMI = new JMenuItem();
    private final JPanel dataPanel = new JPanel();
    private final JMenu datasetMenu = new JMenu();
    private final JCheckBoxMenuItem debugPrintMI = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem debugShowFpsMI = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem debugStdoutMI = new JCheckBoxMenuItem();
    private final JCheckBoxMenuItem debugUseAAMI = new JCheckBoxMenuItem();
    private final JFormattedTextField degradationField = new JFormattedTextField(FORMATTER);
    private final JComboBox<String> destinationBox = new JComboBox<>();
    private final JPanel errorPlotPanel = new JPanel();
    private final JTable errorTable = new JTable();
    private final JMenuItem exampleBatmanMI = new JMenuItem();
    private final JMenuItem exampleCosMI = new JMenuItem();
    private final JMenuItem exampleDunnoMI = new JMenuItem();
    private final JMenuItem exampleEkgRealMI = new JMenuItem();
    private final JMenuItem exampleEkgSynthMI = new JMenuItem();
    private final JMenuItem exampleJumpyMI = new JMenuItem();
    private final JMenuItem exampleSinMI = new JMenuItem();
    private final JMenuItem exampleSincMI = new JMenuItem();
    private final JMenuItem exampleSquareMI = new JMenuItem();
    private final JMenuItem exampleTriangleMI = new JMenuItem();
    private final JMenuItem exampleVeryJumpyMI = new JMenuItem();
    private final JMenu examplesMenu = new JMenu();
    private final JPanel functPlotPanel = new JPanel();
    private final JComboBox<String> generBox = new JComboBox<>();
    private final JLabel generLabel = new JLabel();
    private final JCheckBox generPlotCheck = new JCheckBox();
    private final JTable generTable = new JTable();
    private final JSplitPane horizontalSplit = new JSplitPane();
    private final JTable layerTable = new JTable();
    private final JSpinner layersSpinner = new JSpinner();
    private final JFormattedTextField learningRateField = new JFormattedTextField(FORMATTER);
    private final JTextPane logsTextPane = new JTextPane();
    private final JSpinner maxEpochsSpinner = new JSpinner();
    private final JMenuBar menuBar = new JMenuBar();
    private final JPopupMenu.Separator menuSeparator2 = new JPopupMenu.Separator();
    private final JFormattedTextField mseField = new JFormattedTextField(FORMATTER);
    private final JSeparator optionsSeparator = new JSeparator();
    private final JTextField pathField = new JTextField();
    private final JButton pathSelectionButton = new JButton();
    private final JButton playPauseButton = new JButton();
    private final JCheckBox randomSelectionCheck = new JCheckBox();
    private final JMenuItem reloadExampleMI = new JMenuItem();
    private final JSpinner saveEverySpinner = new JSpinner();
    private final JRadioButton selGenerButton = new JRadioButton();
    private final JRadioButton selTrainButton = new JRadioButton();
    private final JRadioButton selValidButton = new JRadioButton();
    private final JFormattedTextField selectNumberField = new JFormattedTextField();
    private final JFormattedTextField selectPercentageField = new JFormattedTextField();
    private final ButtonGroup selectionButtonGroup = new ButtonGroup();
    private final JSeparator selectionSeparator2 = new JSeparator();
    private final JCheckBox showLabelsCheck = new JCheckBox();
    private final JCheckBox smoothErroCheck = new JCheckBox();
    private final JButton stopButton = new JButton();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel topologyPanel = new JPanel();
    private final JComboBox<String> trainBox = new JComboBox<>();
    private final JLabel trainLabel = new JLabel();
    private final JMenu trainMenu = new JMenu();
    private final JMenuItem trainPauseMI = new JMenuItem();
    private final JCheckBox trainPlotCheck = new JCheckBox();
    private final JMenuItem trainStartMI = new JMenuItem();
    private final JMenuItem trainStopMI = new JMenuItem();
    private final JTable trainTable = new JTable();
    private final JProgressBar trainingProgress = new JProgressBar();
    private final JMenuItem transAutoscaleSelMI = new JMenuItem();
    private final JMenuItem transCustFuncSelMI = new JMenuItem();
    private final JComboBox<String> validBox = new JComboBox<>();
    private final JLabel validLabel = new JLabel();
    private final JCheckBox validPlotCheck = new JCheckBox();
    private final JTable validTable = new JTable();
    private final JSplitPane verticalSplit = new JSplitPane();
    private final JTextField weightField = new JTextField();
    // End of variables declaration

    private static final int TRAIN = 0;
    private static final int VALID = 1;
    private static final int GENER = 2;
    private static final int ERROR = 3;

    private FunctionMonospacedRand dialogMonospacedRand;
    private FunctionGaussianRand dialogGaussianRand;

    /**
     * Plot the graphs using points
     */
    public static final int PLOT_POINT = 0;
    /**
     * Plot the graphs using crosses
     */
    public static final int PLOT_CROSS = 1;
    /**
     * Plot the graphs using paths (joining points)
     */
    public static final int PLOT_PATHS = 2;

    private final BundleDecorator MSTR = new BundleDecorator("res.i18n.menu");

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

    private final GString strTrain = new GString(0, 0, i18n.__("Training"));
    private final GString strValid = new GString(0, 0, i18n.__("Validation"));
    private CustomFunction dialogCustomFunc;
    private SwingWorker<Void, ErrorTableModel.Row> worker;
    private volatile boolean paused;
    private MLP mlp;
    private final ImageIcon play  = new ImageIcon(getClass().getResource("/res/icons/play.png"));
    private final ImageIcon pause = new ImageIcon(getClass().getResource("/res/icons/pause.png"));
    private boolean currently_training;
    private final String header = "<html><b>%s (%d/%d)</b></html>";
    private GLine epLine;
    private JMenuItem lastItem;
    private static final DecimalFormat FORMATTER = new DecimalFormat(c4g.get("number.format"));
    private final GMultiPoint[] errorPoints = new GMultiPoint[2];
    private final BasicStroke errStroke = new BasicStroke(1.5f);
    private final int errRes = c4g.getInt("smooth.error.res");
    private GString labTrain, labValid, labGen;
    private final GMultiPoint[] points = new GMultiPoint[3];
    private final JComboBox<?>[] list;
    private final DatasetTableModel[] datas;
    private final JCheckBox[] selections;
    private final Color[] colors = {new Color(0x6666FF), new Color(0xFF9900), new Color(0xFF0000)};
    private final String[] sfiles = {
        i18n.__("__training.csv"),
        i18n.__("__validation.csv"),
        i18n.__("__generalization.csv"),
        i18n.__("__errors.csv")
    };
    private final String INTERNAL_MLP_PATH    = "/res/i18n/mlp/default_%s.properties";
    private final String INTERNAL_MLP_PATH_EN = "/res/i18n/mlp/default.properties";
    private final SimpleAttributeSet STYLE_ERROR   = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_INFO    = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_DEBUG   = new SimpleAttributeSet();
    private final SimpleAttributeSet STYLE_WARNING = new SimpleAttributeSet();
    private final StyledDocument doc;
    private final static long START = System.nanoTime();

    private PrintStream PS = c4g.getBool("use.std.out") ? System.out : NullPrintStream.getInstance();
    private boolean DEBUG_ENABLED = c4g.getBool("enable.debug");

    public MainWindow() {
        loadAttributes();
        doc = logsTextPane.getStyledDocument();
        list = new JComboBox<?>[]{trainBox, validBox, generBox};
        selections = new JCheckBox[]{trainPlotCheck, validPlotCheck, generPlotCheck};
        datas = new DatasetTableModel[]{train, validate, generalize};
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        final JScrollPane trainTableScrollPane = new JScrollPane();
        final JScrollPane validTableScrollPane = new JScrollPane();
        final JScrollPane generTableScrollPane = new JScrollPane();
        final JPanel selectionPanel = new JPanel();
        final JLabel destinationLabel = new JLabel();
        final JButton selectAllButton = new JButton();
        final JButton selectNonButton = new JButton();
        final JButton toggleSelButton = new JButton();
        final JSeparator selectionSeparator1 = new JSeparator();
        final JButton selectPercentageButton = new JButton();
        final JButton selectNumberButton = new JButton();
        final JButton moveSelectedButton = new JButton();
        final JButton copySelectedButton = new JButton();
        final JPanel layersPanel = new JPanel();
        final JScrollPane layersScrollPane = new JScrollPane();
        final JLabel layersLabel = new JLabel();
        final JLabel backpropLabel = new JLabel();
        final JLabel learningRateLabel = new JLabel();
        final JLabel degradationLabel = new JLabel();
        final JPanel trainingConfPanel = new JPanel();
        final JLabel mseLabel = new JLabel();
        final JLabel maxEpochsLabel = new JLabel();
        final JLabel saveEveryLabel = new JLabel();
        final JPanel outputPanel = new JPanel();
        final JLabel pathLabel = new JLabel();
        final JLabel weightLabel = new JLabel();
        final JPanel plotsPanel = new JPanel();
        final JPanel optionsPanel = new JPanel();
        final JButton trainColorButton = new JButton();
        final JButton validColorButton = new JButton();
        final JButton generColorButton = new JButton();
        final JScrollPane errorTableScrollPane = new JScrollPane();
        final JPanel logOutputPanel = new JPanel();
        final JScrollPane logsScroll = new JScrollPane();
        final JMenu fileMenu = new JMenu();
        final JMenuItem loadConfigMI = new JMenuItem();
        final JPopupMenu.Separator menuSeparator1 = new JPopupMenu.Separator();
        final JMenuItem quitMI = new JMenuItem();
        final JMenu viewMenu = new JMenu();
        final JMenuItem dataPanelMI = new JMenuItem();
        final JMenuItem topologyPanelMI = new JMenuItem();
        final JMenuItem errorPlotPanelMI = new JMenuItem();
        final JMenuItem outputPanelMI = new JMenuItem();
        final JMenu populateMenu = new JMenu();
        final JMenuItem popFuncMonoMI = new JMenuItem();
        final JMenuItem popFuncRandMI = new JMenuItem();
        final JMenuItem popFuncGaussMI = new JMenuItem();
        final JMenu transformMenu = new JMenu();
        final JMenuItem transAutoscaleAllMI = new JMenuItem();
        final JMenuItem transCustFuncAllMI = new JMenuItem();
        final JMenuItem dataAddRowMI = new JMenuItem();
        final JMenuItem dataRemSelRowsMI = new JMenuItem();
        final JMenu plotsMenu = new JMenu();
        final JMenuItem saveImageMI = new JMenuItem();
        final JMenuItem saveErrorMI = new JMenuItem();
        final JMenuItem saveGifMI = new JMenuItem();
        final JMenu helpMenu = new JMenu();
        final JMenu debugMenu = new JMenu();
        final JMenuItem aboutMI = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                formWindowClosing();
            }
        });

        tabbedPane.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                drawErrors();
            }
        });

        trainTableScrollPane.setViewportView(trainTable);

        trainLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ResourceBundle bundle = ResourceBundle.getBundle("res/i18n/strings"); // NOI18N
        trainLabel.setText(bundle.getString("TBL_LAB_TRAIN")); // NOI18N

        validLabel.setHorizontalAlignment(SwingConstants.CENTER);
        validLabel.setText(bundle.getString("TBL_LBL_VALID")); // NOI18N

        validTableScrollPane.setViewportView(validTable);

        generLabel.setHorizontalAlignment(SwingConstants.CENTER);
        generLabel.setText(bundle.getString("TBL_LBL_GENER")); // NOI18N

        generTableScrollPane.setViewportView(generTable);

        selectionButtonGroup.add(selTrainButton);
        selTrainButton.setText(bundle.getString("TBL_SEL_TRAIN")); // NOI18N
        selTrainButton.addActionListener((evt) -> {
            setSelected(train);
        });

        selectionButtonGroup.add(selValidButton);
        selValidButton.setText(bundle.getString("TBL_SEL_VALID")); // NOI18N
        selValidButton.setActionCommand(bundle.getString("TBL_SEL_VALID")); // NOI18N
        selValidButton.addActionListener((evt) -> {
            setSelected(validate);
        });

        selectionButtonGroup.add(selGenerButton);
        selGenerButton.setText(bundle.getString("TBL_SEL_GENER")); // NOI18N
        selGenerButton.addActionListener((evt) -> {
            setSelected(generalize);
        });

        selectionPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PNL_SEL"))); // NOI18N

        destinationLabel.setText(bundle.getString("PNL_SEL_DESTINATION")); // NOI18N

        destinationBox.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Training"), i18n.__("Validation"), i18n.__("Generalization") }));

        selectAllButton.setText(bundle.getString("PNL_SEL_ALL")); // NOI18N
        selectAllButton.addActionListener((evt) -> {
            selectAllButtonActionPerformed();
        });

        selectNonButton.setText(bundle.getString("PNL_SEL_NONE")); // NOI18N
        selectNonButton.addActionListener((evt) -> {
            selectNonButtonActionPerformed();
        });

        toggleSelButton.setText(bundle.getString("PNL_SEL_TOGGLE")); // NOI18N
        toggleSelButton.addActionListener((evt) -> {
            toggleSelButtonActionPerformed();
        });

        selectionSeparator1.setOrientation(SwingConstants.VERTICAL);

        selectPercentageField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getPercentInstance())));

        selectPercentageButton.setText(bundle.getString("PNL_SEL_SELECT")); // NOI18N
        selectPercentageButton.addActionListener((evt) -> {
            selectPercentageButtonActionPerformed();
        });

        selectNumberField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));

        selectNumberButton.setText(bundle.getString("PNL_SEL_SELECT")); // NOI18N
        selectNumberButton.addActionListener((evt) -> {
            selectNumberButtonActionPerformed();
        });

        randomSelectionCheck.setText(bundle.getString("PNL_SEL_RAND")); // NOI18N
        randomSelectionCheck.setHorizontalAlignment(SwingConstants.TRAILING);
        randomSelectionCheck.setHorizontalTextPosition(SwingConstants.LEADING);

        selectionSeparator2.setOrientation(SwingConstants.VERTICAL);

        moveSelectedButton.setText(bundle.getString("PNL_SEL_MOVE")); // NOI18N
        moveSelectedButton.addActionListener((evt) -> {
            moveSelectedButtonActionPerformed();
        });

        copySelectedButton.setText(bundle.getString("PNL_SEL_COPY")); // NOI18N
        copySelectedButton.addActionListener((evt) -> {
            copySelectedButtonActionPerformed();
        });

        GroupLayout selectionPanelLayout = new GroupLayout(selectionPanel);
        selectionPanel.setLayout(selectionPanelLayout);
        selectionPanelLayout.setHorizontalGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(selectNonButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectAllButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                    .addComponent(toggleSelButton, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectionSeparator1, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(selectionPanelLayout.createSequentialGroup()
                        .addComponent(selectPercentageField, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectPercentageButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(selectionPanelLayout.createSequentialGroup()
                        .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(randomSelectionCheck, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectNumberField, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectNumberButton)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectionSeparator2, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(selectionPanelLayout.createSequentialGroup()
                        .addComponent(destinationLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(destinationBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(moveSelectedButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(copySelectedButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        selectionPanelLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {selectAllButton, selectNonButton, toggleSelButton});

        selectionPanelLayout.setVerticalGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, selectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(selectionSeparator2, GroupLayout.Alignment.LEADING)
                    .addComponent(selectionSeparator1, GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.LEADING, selectionPanelLayout.createSequentialGroup()
                        .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(GroupLayout.Alignment.LEADING, selectionPanelLayout.createSequentialGroup()
                                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(destinationLabel)
                                    .addComponent(destinationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveSelectedButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(copySelectedButton))
                            .addGroup(GroupLayout.Alignment.LEADING, selectionPanelLayout.createSequentialGroup()
                                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(selectPercentageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(selectPercentageButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(selectNumberField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(selectNumberButton))
                                .addGap(18, 18, 18)
                                .addComponent(randomSelectionCheck))
                            .addGroup(GroupLayout.Alignment.LEADING, selectionPanelLayout.createSequentialGroup()
                                .addComponent(selectAllButton)
                                .addGap(9, 9, 9)
                                .addComponent(toggleSelButton)
                                .addGap(7, 7, 7)
                                .addComponent(selectNonButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        GroupLayout dataPanelLayout = new GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(selectionPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(selTrainButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(trainTableScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(trainLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(validTableScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(validLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selValidButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(generTableScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(generLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selGenerButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        dataPanelLayout.setVerticalGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(generLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generTableScrollPane, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                        .addComponent(validLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(validTableScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(trainLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trainTableScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(selTrainButton)
                    .addComponent(selValidButton)
                    .addComponent(selGenerButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        tabbedPane.addTab(bundle.getString("PNL_DATA"), dataPanel); // NOI18N

        layersPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_LAYERS"))); // NOI18N

        layersScrollPane.setViewportView(layerTable);

        layersLabel.setText(bundle.getString("TOPO_HIDDEN_NUMBER")); // NOI18N

        layersSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        layersSpinner.addChangeListener((ChangeEvent evt) -> {
            layersSpinnerStateChanged();
        });

        backpropLabel.setText(bundle.getString("TOPO_BACKPROP")); // NOI18N

        backpropBox.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Standard"), i18n.__("Momentum"), i18n.__("Resilient") }));

        learningRateLabel.setText(bundle.getString("TOPO_LEARN_RATE")); // NOI18N

        degradationLabel.setText(bundle.getString("TOPO_DEGRADATION")); // NOI18N

        learningRateField.setValue(0.001);
        learningRateField.setHorizontalAlignment(JTextField.TRAILING);

        degradationField.setValue(1);
        degradationField.setHorizontalAlignment(JTextField.TRAILING);

        GroupLayout layersPanelLayout = new GroupLayout(layersPanel);
        layersPanel.setLayout(layersPanelLayout);
        layersPanelLayout.setHorizontalGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(layersScrollPane)
                    .addGroup(layersPanelLayout.createSequentialGroup()
                        .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layersPanelLayout.createSequentialGroup()
                                .addComponent(learningRateLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(learningRateField, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layersPanelLayout.createSequentialGroup()
                                .addComponent(layersLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(layersSpinner, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(backpropLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(degradationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(backpropBox, 0, 207, Short.MAX_VALUE)
                            .addComponent(degradationField))))
                .addContainerGap())
        );
        layersPanelLayout.setVerticalGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layersScrollPane, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(layersLabel)
                    .addComponent(layersSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(backpropLabel)
                    .addComponent(backpropBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(degradationLabel)
                        .addComponent(degradationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layersPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(learningRateLabel)
                        .addComponent(learningRateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        trainingConfPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_TRAIN"))); // NOI18N

        mseLabel.setText(bundle.getString("TOPO_MSE")); // NOI18N

        maxEpochsLabel.setText(bundle.getString("TOPO_EPOCHS")); // NOI18N

        saveEveryLabel.setText(bundle.getString("TOPO_SAVE_WEIGHTS")); // NOI18N

        mseField.setValue(0.0001);
        mseField.setHorizontalAlignment(JTextField.TRAILING);

        maxEpochsSpinner.setModel(new SpinnerNumberModel(500000, 1, 100000000, 50));

        saveEverySpinner.setModel(new SpinnerNumberModel(50, 1, 1000, 50));

        GroupLayout trainingConfPanelLayout = new GroupLayout(trainingConfPanel);
        trainingConfPanel.setLayout(trainingConfPanelLayout);
        trainingConfPanelLayout.setHorizontalGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(trainingConfPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(maxEpochsLabel)
                    .addComponent(mseLabel)
                    .addComponent(saveEveryLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(maxEpochsSpinner, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addComponent(saveEverySpinner)
                    .addComponent(mseField))
                .addContainerGap())
        );
        trainingConfPanelLayout.setVerticalGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(trainingConfPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(mseLabel)
                    .addComponent(mseField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(maxEpochsLabel)
                    .addComponent(maxEpochsSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trainingConfPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(saveEveryLabel)
                    .addComponent(saveEverySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outputPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("TOPO_OUTPUT"))); // NOI18N

        pathLabel.setText(bundle.getString("TOPO_FOLDER_PATH")); // NOI18N

        pathSelectionButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/find.png"))); // NOI18N
        pathSelectionButton.addActionListener((e) -> {
            pathSelectionButtonActionPerformed();
        });

        weightLabel.setText(bundle.getString("TOPO_WEIGHTS_NAME")); // NOI18N

        GroupLayout outputPanelLayout = new GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(weightLabel)
                    .addComponent(pathLabel))
                .addGap(26, 26, 26)
                .addGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(pathField, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pathSelectionButton))
                    .addComponent(weightField))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(pathLabel)
                    .addComponent(pathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(pathSelectionButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outputPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(weightLabel)
                    .addComponent(weightField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout topologyPanelLayout = new GroupLayout(topologyPanel);
        topologyPanel.setLayout(topologyPanelLayout);
        topologyPanelLayout.setHorizontalGroup(topologyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(topologyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topologyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(layersPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outputPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trainingConfPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        topologyPanelLayout.setVerticalGroup(topologyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(topologyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layersPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trainingConfPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("PNL_TOPOLOGY"), topologyPanel); // NOI18N

        plotsPanel.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                plotsPanelComponentShown();
            }
        });

        optionsPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_FORMAT"))); // NOI18N

        validBox.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        validBox.addActionListener((evt) -> {
            validBoxActionPerformed();
        });

        trainPlotCheck.setText(bundle.getString("PLOT_OPT_TRAIN")); // NOI18N
        trainPlotCheck.addActionListener((evt) -> {
            trainPlotCheckActionPerformed();
        });

        validPlotCheck.setText(bundle.getString("PLOT_OPT_VALID")); // NOI18N
        validPlotCheck.addActionListener((evt) -> {
            validPlotCheckActionPerformed();
        });

        generPlotCheck.setText(bundle.getString("PLOT_OPT_GENER")); // NOI18N
        generPlotCheck.addActionListener((evt) -> {
            generPlotCheckActionPerformed();
        });

        generBox.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        generBox.addActionListener((evt) -> {
            generBoxActionPerformed();
        });

        trainBox.setModel(new DefaultComboBoxModel<>(new String[] { i18n.__("Point"), i18n.__("Cross"), i18n.__("Path") }));
        trainBox.addActionListener((evt) -> {
            trainBoxActionPerformed();
        });

        trainColorButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        trainColorButton.addActionListener((evt) -> {
            trainColorButtonActionPerformed();
        });

        validColorButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        validColorButton.addActionListener((evt) -> {
            validColorButtonActionPerformed();
        });

        generColorButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/color-wheel.png"))); // NOI18N
        generColorButton.addActionListener((evt) -> {
            generColorButtonActionPerformed();
        });

        optionsSeparator.setOrientation(SwingConstants.VERTICAL);

        smoothErroCheck.setText(bundle.getString("PLOT_SMOOTH")); // NOI18N
        smoothErroCheck.addActionListener((evt) -> {
            smoothErroCheckActionPerformed();
        });

        playPauseButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/play.png"))); // NOI18N
        playPauseButton.addActionListener((evt) -> {
            playPauseButtonActionPerformed();
        });

        stopButton.setIcon(new ImageIcon(getClass().getResource("/res/icons/stop.png"))); // NOI18N
        stopButton.addActionListener((evt) -> {
            stopButtonActionPerformed();
        });

        showLabelsCheck.setText(bundle.getString("PLOT_SHOW_LABELS")); // NOI18N
        showLabelsCheck.addActionListener((evt) -> {
            showLabelsCheckActionPerformed();
        });

        GroupLayout optionsPanelLayout = new GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(generPlotCheck, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(validPlotCheck, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trainPlotCheck, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(trainBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(validBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generBox, 0, 200, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(trainColorButton)
                    .addComponent(validColorButton)
                    .addComponent(generColorButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionsSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(smoothErroCheck, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addComponent(playPauseButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trainingProgress, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addComponent(showLabelsCheck)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(optionsSeparator)
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(trainPlotCheck)
                            .addComponent(trainBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(trainColorButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(validPlotCheck)
                                .addComponent(validBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(validColorButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(generPlotCheck)
                                .addComponent(generBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(generColorButton))
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addComponent(smoothErroCheck)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showLabelsCheck)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(optionsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(stopButton)
                            .addComponent(playPauseButton)
                            .addComponent(trainingProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        optionsPanelLayout.linkSize(SwingConstants.VERTICAL, new Component[] {stopButton, trainingProgress});

        errorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        errorTableScrollPane.setViewportView(errorTable);

        horizontalSplit.setLeftComponent(errorTableScrollPane);

        verticalSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);

        functPlotPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_FUNCTION"))); // NOI18N

        GroupLayout functPlotPanelLayout = new GroupLayout(functPlotPanel);
        functPlotPanel.setLayout(functPlotPanelLayout);
        functPlotPanelLayout.setHorizontalGroup(functPlotPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        functPlotPanelLayout.setVerticalGroup(functPlotPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        verticalSplit.setTopComponent(functPlotPanel);

        errorPlotPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PLOT_ERRS"))); // NOI18N

        GroupLayout errorPlotPanelLayout = new GroupLayout(errorPlotPanel);
        errorPlotPanel.setLayout(errorPlotPanelLayout);
        errorPlotPanelLayout.setHorizontalGroup(errorPlotPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        errorPlotPanelLayout.setVerticalGroup(errorPlotPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        verticalSplit.setRightComponent(errorPlotPanel);

        horizontalSplit.setRightComponent(verticalSplit);

        GroupLayout plotsPanelLayout = new GroupLayout(plotsPanel);
        plotsPanel.setLayout(plotsPanelLayout);
        plotsPanelLayout.setHorizontalGroup(plotsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, plotsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(plotsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(horizontalSplit, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                    .addComponent(optionsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        plotsPanelLayout.setVerticalGroup(plotsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(plotsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(horizontalSplit, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("PNL_ERR_PLOTS"), plotsPanel); // NOI18N

        logsScroll.setViewportView(logsTextPane);

        GroupLayout logOutputPanelLayout = new GroupLayout(logOutputPanel);
        logOutputPanel.setLayout(logOutputPanelLayout);
        logOutputPanelLayout.setHorizontalGroup(logOutputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(logsScroll, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
        );
        logOutputPanelLayout.setVerticalGroup(logOutputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(logsScroll, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        tabbedPane.addTab(bundle.getString("PNL_OUTPUT"), logOutputPanel); // NOI18N

        fileMenu.setMnemonic('A');
        ResourceBundle bundle1 = ResourceBundle.getBundle("res/i18n/menu"); // NOI18N
        fileMenu.setText(bundle1.getString("FILE")); // NOI18N

        loadConfigMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        loadConfigMI.setMnemonic('c');
        loadConfigMI.setText(bundle1.getString("FILE_LOAD_CONF")); // NOI18N
        loadConfigMI.addActionListener((e) -> {loadConfigMIActionPerformed();});
        fileMenu.add(loadConfigMI);
        fileMenu.add(menuSeparator1);

        quitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        quitMI.setMnemonic('s');
        quitMI.setText(bundle1.getString("FILE_QUIT")); // NOI18N
        quitMI.addActionListener((e) -> {quitMIActionPerformed();});
        fileMenu.add(quitMI);

        menuBar.add(fileMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText(bundle1.getString("VIEW")); // NOI18N

        dataPanelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
        dataPanelMI.setMnemonic('d');
        dataPanelMI.setText(bundle1.getString("VIEW_PANEL_DATA")); // NOI18N
        dataPanelMI.addActionListener((e) -> {tabbedPane.setSelectedIndex(0);});
        viewMenu.add(dataPanelMI);

        topologyPanelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
        topologyPanelMI.setMnemonic('t');
        topologyPanelMI.setText(bundle1.getString("VIEW_PANEL_TOPOLOGY")); // NOI18N
        topologyPanelMI.addActionListener((e) -> {tabbedPane.setSelectedIndex(1);});
        viewMenu.add(topologyPanelMI);

        errorPlotPanelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
        errorPlotPanelMI.setMnemonic('f');
        errorPlotPanelMI.setText(bundle1.getString("VIEW_PANEL_ERROR_PLOT")); // NOI18N
        errorPlotPanelMI.addActionListener((e) -> {tabbedPane.setSelectedIndex(2);});
        viewMenu.add(errorPlotPanelMI);

        outputPanelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
        outputPanelMI.setMnemonic('i');
        outputPanelMI.setText(bundle1.getString("VIEW_PANEL_OUTPUT")); // NOI18N
        outputPanelMI.addActionListener((e) -> {tabbedPane.setSelectedIndex(3);});
        viewMenu.add(outputPanelMI);

        menuBar.add(viewMenu);

        datasetMenu.setMnemonic('T');
        datasetMenu.setText(bundle1.getString("DATA")); // NOI18N

        populateMenu.setText(bundle1.getString("DATA_FILL")); // NOI18N

        popFuncMonoMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        popFuncMonoMI.setText(bundle1.getString("DATA_FILL_MONO")); // NOI18N
        popFuncMonoMI.addActionListener((e) -> {popFuncMonoMIActionPerformed();});
        populateMenu.add(popFuncMonoMI);

        popFuncRandMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        popFuncRandMI.setText(bundle1.getString("DATA_FILL_RAND")); // NOI18N
        popFuncRandMI.addActionListener((e) -> {popFuncRandMIActionPerformed();});
        populateMenu.add(popFuncRandMI);

        popFuncGaussMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        popFuncGaussMI.setText(bundle1.getString("DATA_FILL_RAND_GAUSS")); // NOI18N
        popFuncGaussMI.addActionListener((e) -> {popFuncGaussMIActionPerformed();});
        populateMenu.add(popFuncGaussMI);

        datasetMenu.add(populateMenu);

        transformMenu.setText(bundle1.getString("DATA_TRANSFORM")); // NOI18N

        transAutoscaleAllMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        transAutoscaleAllMI.setText(bundle1.getString("DATA_TRANSFORM_AUTO_ALL")); // NOI18N
        transAutoscaleAllMI.addActionListener((evt) -> {
            transAutoscaleAllMIActionPerformed();
        });
        transformMenu.add(transAutoscaleAllMI);

        transCustFuncAllMI.setText(bundle1.getString("DATA_TRANSFORM_CUST_ALL")); // NOI18N
        transCustFuncAllMI.addActionListener((evt) -> {
            transCustFuncAllMIActionPerformed();
        });
        transformMenu.add(transCustFuncAllMI);

        transAutoscaleSelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        transAutoscaleSelMI.setText(bundle1.getString("DATA_TRANSFORM_AUTO_SEL")); // NOI18N
        transAutoscaleSelMI.addActionListener((evt) -> {
            transAutoscaleSelMIActionPerformed();
        });
        transformMenu.add(transAutoscaleSelMI);

        transCustFuncSelMI.setText(bundle1.getString("DATA_TRANSFORM_CUST_SEL")); // NOI18N
        transCustFuncSelMI.addActionListener((evt) -> {
            transCustFuncSelMIActionPerformed();
        });
        transformMenu.add(transCustFuncSelMI);

        datasetMenu.add(transformMenu);

        dataAddRowMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
        dataAddRowMI.setText(bundle1.getString("DATA_ADD_ROW")); // NOI18N
        dataAddRowMI.addActionListener((evt) -> {
            dataAddRowMIActionPerformed();
        });
        datasetMenu.add(dataAddRowMI);

        dataRemSelRowsMI.setText(bundle1.getString("DATA_REMOVE_SELECTED")); // NOI18N
        dataRemSelRowsMI.addActionListener((evt) -> {
            selected.removeSelected();
        });
        datasetMenu.add(dataRemSelRowsMI);

        dataClearSelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK));
        dataClearSelMI.setText(bundle1.getString("DATA_CLEAR_SELECTED_DATASET")); // NOI18N
        dataClearSelMI.addActionListener((evt) -> {
            dataClearSelMIActionPerformed();
        });
        datasetMenu.add(dataClearSelMI);

        dataClearAllMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        dataClearAllMI.setText(bundle1.getString("DATA_CLEAR_ALL")); // NOI18N
        dataClearAllMI.addActionListener((evt) -> {
            dataClearAllMIActionPerformed();
        });
        datasetMenu.add(dataClearAllMI);

        menuBar.add(datasetMenu);

        trainMenu.setMnemonic('E');
        trainMenu.setText(bundle1.getString("TRAIN")); // NOI18N

        trainStartMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        trainStartMI.setText(bundle1.getString("TRAIN_NOW")); // NOI18N
        trainStartMI.addActionListener((evt) -> {
            trainStartMIActionPerformed();
        });
        trainMenu.add(trainStartMI);

        trainPauseMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        trainPauseMI.setText(bundle1.getString("TRAIN_PAUSE")); // NOI18N
        trainPauseMI.addActionListener((evt) -> {
            trainPauseMIActionPerformed();
        });
        trainMenu.add(trainPauseMI);

        trainStopMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        trainStopMI.setText(bundle1.getString("TRAIN_STOP")); // NOI18N
        trainStopMI.addActionListener((evt) -> {
            trainStopMIActionPerformed();
        });
        trainMenu.add(trainStopMI);

        menuBar.add(trainMenu);

        plotsMenu.setMnemonic('G');
        plotsMenu.setText(bundle1.getString("PLOTS")); // NOI18N

        saveImageMI.setText(bundle1.getString("PLOTS_SAVE_IMG")); // NOI18N
        saveImageMI.addActionListener((evt) -> {
            saveImageMIActionPerformed();
        });
        plotsMenu.add(saveImageMI);

        saveErrorMI.setText(bundle1.getString("PLOTS_SAVE_ERRORS")); // NOI18N
        saveErrorMI.addActionListener((evt) -> {
            saveErrorMIActionPerformed();
        });
        plotsMenu.add(saveErrorMI);

        saveGifMI.setText(bundle1.getString("PLOTS_SAVE_GIF")); // NOI18N
        saveGifMI.addActionListener((evt) -> {
            saveGifMIActionPerformed();
        });
        plotsMenu.add(saveGifMI);

        menuBar.add(plotsMenu);

        examplesMenu.setMnemonic('j');
        examplesMenu.setText(bundle1.getString("EXAMPLES")); // NOI18N

        reloadExampleMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        reloadExampleMI.setText(bundle1.getString("EXAMPLES_RELOAD")); // NOI18N
        reloadExampleMI.addActionListener((evt) -> {
            reloadExampleMIActionPerformed();
        });
        examplesMenu.add(reloadExampleMI);
        examplesMenu.add(menuSeparator2);

        exampleSinMI.setText(bundle1.getString("EXAMPLES_SIN")); // NOI18N
        examplesMenu.add(exampleSinMI);

        exampleCosMI.setText(bundle1.getString("EXAMPLES_COS")); // NOI18N
        examplesMenu.add(exampleCosMI);

        exampleSincMI.setText(bundle1.getString("EXAMPLES_SINC")); // NOI18N
        examplesMenu.add(exampleSincMI);

        exampleSquareMI.setText(bundle1.getString("EXAMPLES_SQARE")); // NOI18N
        examplesMenu.add(exampleSquareMI);

        exampleJumpyMI.setText(bundle1.getString("EXAMPLES_JUMPY")); // NOI18N
        examplesMenu.add(exampleJumpyMI);

        exampleVeryJumpyMI.setText(bundle1.getString("EXAMPLES_VERY_JUMPY")); // NOI18N
        examplesMenu.add(exampleVeryJumpyMI);

        exampleTriangleMI.setText(bundle1.getString("EXAMPLES_TRIANGLE")); // NOI18N
        examplesMenu.add(exampleTriangleMI);

        exampleDunnoMI.setText(bundle1.getString("EXAMPLES_DUNNO")); // NOI18N
        examplesMenu.add(exampleDunnoMI);

        exampleBatmanMI.setText(bundle1.getString("EXAMPLES_BATMAN")); // NOI18N
        examplesMenu.add(exampleBatmanMI);

        exampleEkgRealMI.setText(bundle1.getString("EXAMPLES_EKG_REAL")); // NOI18N
        examplesMenu.add(exampleEkgRealMI);

        exampleEkgSynthMI.setText(bundle1.getString("EXAMPLES_EKG_SYNTH")); // NOI18N
        examplesMenu.add(exampleEkgSynthMI);

        menuBar.add(examplesMenu);

        helpMenu.setMnemonic('?');

        debugMenu.setText(bundle1.getString("DEBUG")); // NOI18N

        debugPrintMI.setText(bundle1.getString("DEBUG_SHOW")); // NOI18N
        debugPrintMI.addActionListener((evt) -> {
            debugPrintMIActionPerformed();
        });
        debugMenu.add(debugPrintMI);

        debugStdoutMI.setText(bundle1.getString("DEBUG_STD_OUT")); // NOI18N
        debugStdoutMI.addActionListener((evt) -> {
            debugStdoutMIActionPerformed();
        });
        debugMenu.add(debugStdoutMI);

        debugUseAAMI.setText(bundle1.getString("DEBUG_AA")); // NOI18N
        debugUseAAMI.addActionListener((evt) -> {
            debugUseAAMIActionPerformed();
        });
        debugMenu.add(debugUseAAMI);

        debugShowFpsMI.setText(bundle1.getString("DEBUG_FPS")); // NOI18N
        debugShowFpsMI.addActionListener((evt) -> {
            debugShowFpsMIActionPerformed();
        });
        debugMenu.add(debugShowFpsMI);

        helpMenu.add(debugMenu);

        aboutMI.setText(bundle1.getString("ABOUT")); // NOI18N
        aboutMI.addActionListener((evt) -> {
            new AboutDialog(MainWindow.this).setVisible(true);
        });
        helpMenu.add(aboutMI);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>

    private FunctionMonospaced dialogMonospaced;
    private void popFuncMonoMIActionPerformed() {
        if(dialogMonospaced == null) dialogMonospaced = new FunctionMonospaced(MainWindow.this);
        dialogMonospaced.setVisible(true);
        selected.addAll(dialogMonospaced.getData());
    }

    private void setSelected(DatasetTableModel model) {
        selected = model;
        train     .selectEnabled(model == train);
        validate  .selectEnabled(model == validate);
        generalize.selectEnabled(model == generalize);
        dataClearSelMI .setText(i18n.__("Clear Selected Dataset (%s)", selected.getName()));
        transAutoscaleSelMI.setText(MSTR.__("DATA_TRANSFORM_AUTO_SEL", selected.getName()));
        transCustFuncSelMI.setText(MSTR.__("DATA_TRANSFORM_CUST_SEL", selected.getName()));
    }

    private void selectAllButtonActionPerformed() {
        selected.selectAll();
        info("Selecting all elements of the '%s' dataset", selected.getName());
    }

    private void toggleSelButtonActionPerformed() {
        selected.selectToggle();
        info("Inverting selection of the '%s' dataset", selected.getName());
    }

    private void selectNonButtonActionPerformed() {
        selected.selectNone();
        info("Removing selection of the '%s' dataset", selected.getName());
    }

    private void selectPercentageButtonActionPerformed() {
        try {
            selectPercentageField.commitEdit();
            final double value = ((Number)selectPercentageField.getValue()).doubleValue();
            final int nEleme = selected.getRowCount();
            selectElements((int)Math.round(nEleme * value));
            info("Selection %5.2f%% of the elements of %s %s", value * 100.,
                  selected.getName(), randomSelectionCheck.isSelected() ? i18n.__("(Random)") : "");
        } catch (ParseException ignoreMe) {
            error("'%s' is not a decimal number", selectPercentageField.getText());
        }
    }

    private void selectNumberButtonActionPerformed() {
        try {
            selectNumberField.commitEdit();
            final int value = ((Number)selectNumberField.getValue()).intValue();
            selectElements(value);
            info("Selecting %d elements from the %s dataset %s", value,
                  selected.getName(), randomSelectionCheck.isSelected() ? i18n.__("(Random)") : "");
        } catch (ParseException ignoreMe) {
            error("'%s' is not an integer", selectNumberField.getText());
        }
    }

    private void moveSelectedButtonActionPerformed() {
        final int idx = destinationBox.getSelectedIndex();
        final ArrayList<DatasetTableModel.Row> foo = selected.removeSelected();
        final String dest;
        switch (idx) {
            case TRAIN: train     .addAll(foo); dest = train.getName(); break;
            case VALID: validate  .addAll(foo); dest = validate.getName(); break;
            default: generalize.addAll(foo); dest = generalize.getName(); break;
        }
        info("Moving %d elements from '%s' -> '%s'", foo.size(), selected.getName(), dest);
    }

    private void popFuncRandMIActionPerformed() {
        if (dialogMonospacedRand == null) dialogMonospacedRand = new FunctionMonospacedRand(MainWindow.this);
        dialogMonospacedRand.setVisible(true);
        selected.addAll(dialogMonospacedRand.getData());
    }

    private void popFuncGaussMIActionPerformed() {
        if (dialogGaussianRand == null) dialogGaussianRand = new FunctionGaussianRand(MainWindow.this);
        dialogGaussianRand.setVisible(true);
        selected.addAll(dialogGaussianRand.getData());
    }

    private void dataClearSelMIActionPerformed() {
        selected.selectAll();
        selected.removeSelected();
    }

    private void dataClearAllMIActionPerformed() {
        train.selectAll();
        validate.selectAll();
        generalize.selectAll();
        train.removeSelected();
        validate.removeSelected();
        generalize.removeSelected();
    }

    private void transAutoscaleAllMIActionPerformed() {
        double maxVal = train.getMax();
        maxVal = Math.max(maxVal, validate  .getMax());
        maxVal = Math.max(maxVal, generalize.getMax());
        final double scale = 1 / maxVal;
        train     .scale(scale);
        validate  .scale(scale);
        generalize.scale(scale);
        info("Table values scaled using s = %f", scale);
    }

    private void trainPlotCheckActionPerformed() {
        if (!trainPlotCheck.isSelected()) {
            func.remove(points[0]);
            func.repaint();
        }
        updateGraphics(TRAIN);
    }

    private void trainBoxActionPerformed() {
        updateGraphics(TRAIN);
    }

    private void generPlotCheckActionPerformed() {
        if (!generPlotCheck.isSelected()) {
            func.remove(points[2]);
            func.repaint();
        }
        updateGraphics(GENER);
    }

    private void validPlotCheckActionPerformed() {
        if (!validPlotCheck.isSelected()) {
            func.remove(points[1]);
            func.repaint();
        }
        updateGraphics(VALID);
    }

    private void validBoxActionPerformed() {
        updateGraphics(VALID);
    }

    private void generBoxActionPerformed() {
        updateGraphics(GENER);
    }

    private void trainColorButtonActionPerformed() {
        chooseColor(i18n.__("Color for training"), TRAIN);
        drawErrors();
    }

    private void validColorButtonActionPerformed() {
        chooseColor(i18n.__("Color for validation"), VALID);
        drawErrors();
    }

    private void generColorButtonActionPerformed() {
        chooseColor(i18n.__("Color for generalization"), GENER);
    }

    private void copySelectedButtonActionPerformed() {
        final int idx = destinationBox.getSelectedIndex();
        final ArrayList<DatasetTableModel.Row> foo = selected.removeSelected();
        selected.addAll(foo);
        final String dest;
        switch (idx) {
            case TRAIN: train   .addAll(foo); dest = train.getName(); break;
            case VALID: validate.addAll(foo); dest = validate.getName(); break;
            default: generalize.addAll(foo); dest = generalize.getName(); break;
        }
        info("Copying %d elements from '%s' -> '%s'", foo.size(), selected.getName(), dest);
    }

    private void layersSpinnerStateChanged() {
        final int newNum = (Integer)layersSpinner.getValue();
        while (layers.getRowCount() - 2 < newNum) layers.addRow   ();
        while (layers.getRowCount() - 2 > newNum) layers.removeRow();
    }

    private void transCustFuncAllMIActionPerformed() {
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
    }

    private void trainStartMIActionPerformed() {
        tabbedPane.setSelectedIndex(2);
        if (worker != null) {
            worker.cancel(true);
        }

        worker = new SwingWorker<Void, ErrorTableModel.Row>() {
            @Override
            protected Void doInBackground() throws Exception {
                paused = false;
                try{
                    final String path = pathField.getText();
                    final String name = weightField.getText();
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

                    mseField.commitEdit();
                    learningRateField.commitEdit();
                    degradationField.commitEdit();

                    final int maxEpochs = (Integer)maxEpochsSpinner.getValue();
                    final double mse    = ((Number)mseField.getValue()).doubleValue();
                    final int saveEvery = (Integer)saveEverySpinner.getValue();
                    final double degradation = ((Number)degradationField.getValue()).doubleValue();
                          double learnRate   = ((Number)learningRateField.getValue()).doubleValue();

                    switch (backpropBox.getSelectedIndex()) {
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
                            trainingProgress.setString(i18n.__("Paused"));
                            trainingProgress.setIndeterminate(true);

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ignoreMe) {
                                //This will happen if the worker is cancelled (or finishes)
                                //immediatly after the pause
                            }
                        }

                        if (worker.isCancelled()) {
                            tt.toc();
                            trainingProgress.setString(i18n.__("Aborted"));
                            trainingProgress.setIndeterminate(false);
                            info("Training aborted by user at %.4fs", tt.getSecsTime());
                            break;
                        }

                        learnRate *= degradation;

                        trainingProgress.setIndeterminate(false);
                        trainingProgress.setStringPainted(true);
                        trainingProgress.setString(null);
                        trainingProgress.setValue(currEpoch * 100 / maxEpochs);
                    }

                    if (!worker.isCancelled()) {
                        tt.toc();
                        info("Training finished successfully in %.4fs", tt.getSecsTime());
                    }

                    saveConfig(path);
                    saveData(path);
                    saveErrors(path);
                    generPlotCheck.setEnabled(true);
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
    }

    private void setTraining(boolean training) {
        currently_training = training;
        trainStartMI.setEnabled(!training);
        playPauseButton.setIcon(training ? pause : play);
        trainPauseMI.setEnabled(training);
        trainStopMI.setEnabled(training);
        stopButton.setEnabled(training);
        examplesMenu.setEnabled(!training);
        datasetMenu.setEnabled(!training);
        errorTable.setEnabled(!training);
        enableComponents(dataPanel, !training);
        enableComponents(topologyPanel, !training);
    }

    private void pathSelectionButtonActionPerformed() {
        JFileChooser jfc = new JFileChooser(pathField.getText()){
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
            pathField.setText(jfc.getSelectedFile().getAbsolutePath() + File.separator);
            info("Current working directory: %s", pathField.getText());
        }
    }

    private void quitMIActionPerformed() {
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
    }

    private void trainStopMIActionPerformed() {
        if (worker != null){
            worker.cancel(true);
            paused = false;
        }
    }

    private void saveImageMIActionPerformed() {
        savePlot(i18n.__("Data"), func);
    }

    private void saveErrorMIActionPerformed() {
        savePlot(i18n.__("Errors"), errs);
    }

    private void transAutoscaleSelMIActionPerformed() {
        double maxVal = train.getMax();
        maxVal = Math.max(maxVal, validate  .getMax());
        maxVal = Math.max(maxVal, generalize.getMax());
        final double scale = 1 / maxVal;
        selected.scale(scale);
        info("Table '%s' values scaled using s = %f", selected.getName(), scale);
    }

    private void transCustFuncSelMIActionPerformed() {
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
    }

    private void dataAddRowMIActionPerformed() {
        selected.addRow(null, null);
    }

    private void saveGifMIActionPerformed() {
        final JFileChooser jfc = new JFileChooser(pathField.getText());
        jfc.setSelectedFile(new File(pathField.getText(), "funcion.gif"));
        final int res = jfc.showSaveDialog(null);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final Gif gif = new Gif(func);
        final int pRow = errorTable.getSelectedRow();

        final int xoff =  func.getXSize() / 2 - 100;
        final int yoff = -func.getYSize() / 2 + 5;
        for (int i = 0; i < errors.getRowCount(); i ++) {
            final GString gs = new GString(xoff, yoff, i18n.__("Epoch: %d", errors.getEpoch(i)));
            gs.setPaint(Color.DARK_GRAY);
            func.add(gs);
            final int row = errorTable.convertRowIndexToView(i);
            errorTable.setRowSelectionInterval(row, row);
            gif.snapshot();
            func.remove(gs);
        }

        if (pRow != -1) errorTable.setRowSelectionInterval(pRow, pRow);
        gif.write(jfc.getSelectedFile().getAbsolutePath());
    }

    private void smoothErroCheckActionPerformed() {
        errs.remove(errorPoints[TRAIN]);
        errs.remove(errorPoints[VALID]);
        errorPoints[TRAIN] = null;
        errorPoints[VALID] = null;
        drawErrors();
    }

    private void trainPauseMIActionPerformed() {
        paused = !paused;
        trainPauseMI.setText(paused ? i18n.__("Resume") : i18n.__("Pause"));
        playPauseButton.setIcon(paused ? play : pause);
    }

    private void playPauseButtonActionPerformed() {
        if (worker != null && !worker.isCancelled()) {
            trainPauseMI.doClick(0);
        }
        trainStartMI.doClick(0);
    }

    private void stopButtonActionPerformed() {
        trainStopMI.doClick(0);
    }

    private void showLabelsCheckActionPerformed() {
        updateGraphics(TRAIN);
        drawErrors();
    }

    private void reloadExampleMIActionPerformed() {
        loadExample(lastItem, null);
    }

    private void formWindowClosing() {
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
    }

    private void loadConfigMIActionPerformed() {
        final JFileChooser jfc = new JFileChooser(pathField.getText());
        jfc.setFileFilter(new FileNameExtensionFilter("MLP config", "conf"));

        final int opt = jfc.showOpenDialog(null);
        if (opt == JFileChooser.CANCEL_OPTION) {
            return;
        }

        final File file = jfc.getSelectedFile();
        if (file != null && file.exists()) {
            readConfig(file);
        }
    }

    private void plotsPanelComponentShown() {
        updateGraphics(TRAIN);
        updateGraphics(VALID);
        updateGraphics(GENER);
    }

    private void debugUseAAMIActionPerformed() {
        errs.setUseAntiAliasing(debugUseAAMI.isSelected());
        func.setUseAntiAliasing(debugUseAAMI.isSelected());
        c4g.put("use.aa", debugUseAAMI.isSelected());
    }

    private void debugShowFpsMIActionPerformed() {
        errs.setShowFPS(debugShowFpsMI.isSelected());
        func.setShowFPS(debugShowFpsMI.isSelected());
        c4g.put("show.fps", debugShowFpsMI.isSelected());
    }

    private void debugPrintMIActionPerformed() {
        DEBUG_ENABLED = debugPrintMI.isSelected();
        c4g.put("enable.debug", DEBUG_ENABLED);
    }

    private void debugStdoutMIActionPerformed() {
        PS = debugStdoutMI.isSelected() ? System.out : NullPrintStream.getInstance();
        c4g.put("use.std.out", debugStdoutMI.isSelected());
    }

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
            if (randomSelectionCheck.isSelected()) {
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

    private void centerHeaders() {
        layerTable.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        trainTable.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        validTable.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        generTable.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        errorTable.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
    }

    private void centerCells() {
        ((JLabel)layerTable.getDefaultRenderer(String.class))
                           .setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel)layerTable.getDefaultRenderer(Integer.class))
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

    private void initModels() {
        layerTable.setModel(layers);
        trainTable.setModel(train);
        validTable.setModel(validate);
        generTable.setModel(generalize);
        errorTable.setModel(errors);
        train.selectEnabled(true);

        train.addTableModelListener((TableModelEvent e) -> {
            final int sel = train.getSelectedCount();
            final int row = train.getRowCount();
            trainLabel.setText(String.format(header, i18n.__("Training"), sel, row));
            checkTrainEnabled();
            updateGraphics(TRAIN);
        });
        validate.addTableModelListener((TableModelEvent e) -> {
            final int sel = validate.getSelectedCount();
            final int row = validate.getRowCount();
            validLabel.setText(String.format(header, i18n.__("Validation"), sel, row));
            checkTrainEnabled();
            updateGraphics(VALID);
        });
        generalize.addTableModelListener((TableModelEvent e) -> {
            final int sel = generalize.getSelectedCount();
            final int row = generalize.getRowCount();
            generLabel.setText(String.format(header, i18n.__("Generalization"), sel, row));
            checkTrainEnabled();
            updateGraphics(GENER);
        });
        errors.addTableModelListener((TableModelEvent e) -> {
            drawErrors();
            updateGraphics(GENER);
        });

        final ListSelectionModel tsm = errorTable.getSelectionModel();
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
                        selectEpoch(errorTable.convertRowIndexToModel(curIdx));
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

        errorTable.setRowSorter(sorter);

        trainTable.getTableHeader().setReorderingAllowed(false);
        validTable.getTableHeader().setReorderingAllowed(false);
        generTable.getTableHeader().setReorderingAllowed(false);
        layerTable.getTableHeader().setReorderingAllowed(false);
        errorTable.getTableHeader().setReorderingAllowed(false);
    }

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

        final String path = pathField.getText();
        final String name = weightField.getText();
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
        functPlotPanel.setLayout(layout);
        functPlotPanel.add(func, BorderLayout.CENTER);
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
        errorPlotPanel.setLayout(layout);
        errorPlotPanel.add(errs, BorderLayout.CENTER);
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

        TableColumn tc = layerTable.getColumnModel().getColumn(2);
        tc.setCellEditor(new DefaultCellEditor(comboBox));
        String home = c4g.get("last.path").isEmpty()
                    ? System.getProperty("user.home")
                    : c4g.get("last.path");

        pathField.setText(home
                        + File.separator + "mrft"
                        + File.separator + i18n.__("Project_1")
                        + File.separator);

        Font tFnt = layerTable.getFont();
             tFnt = new Font(Font.MONOSPACED, tFnt.getStyle(), tFnt.getSize());

        layerTable.setFont(tFnt);
        trainTable.setFont(tFnt);

        Font lFnt = trainLabel.getFont().deriveFont(Font.BOLD);
        trainLabel.setFont(lFnt);
        validLabel.setFont(lFnt);
        generLabel.setFont(lFnt);

        verticalSplit.setDividerLocation(0.5);
        verticalSplit.setResizeWeight(0.5);
        horizontalSplit.setDividerLocation(190);
        horizontalSplit.setResizeWeight(0);

        debugUseAAMI.setSelected(c4g.getBool("use.aa"));
        debugShowFpsMI.setSelected(c4g.getBool("show.fps"));
        debugPrintMI.setSelected(c4g.getBool("enable.debug"));
        debugStdoutMI.setSelected(c4g.getBool("use.std.out"));

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

        trainPlotCheck.setSelected(c4g.getBool("train.plot"));
        validPlotCheck.setSelected(c4g.getBool("valid.plot"));
        generPlotCheck.setSelected(c4g.getBool("gener.plot"));

        trainBox.setSelectedIndex(c4g.getInt("train.format"));
        validBox.setSelectedIndex(c4g.getInt("valid.format"));
        generBox.setSelectedIndex(c4g.getInt("gener.format"));

        colors[TRAIN] = c4g.getColor("train.color");
        colors[VALID] = c4g.getColor("valid.color");
        colors[GENER] = c4g.getColor("gener.color");

        selTrainButton.doClick(0);
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
        final JFileChooser jfc = new JFileChooser(pathField.getText());
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

    private void loadExample(JMenuItem menu, Example ex) {
        if (ex == null && menu != null) {
            menu.doClick(0);
            return;
        }

        if (menu != null && ex != null) {
            info("Example '%s' loaded", ex.getName());
        }

        reloadExampleMI.setText(i18n.__("<html>Reload <code>'%s'</code></html>", ex.getName()));
        reloadExampleMI.setEnabled(true);
        lastItem = menu;
        dataClearAllMI.doClick(0);
        errors.removeAll();
        layersSpinner.setValue(ex.loadTable(layers));
        ex.loadTrainData(train);
        ex.loadValidationData(validate);
        ex.loadGeneralizationData(generalize);
        backpropBox.setSelectedIndex(ex.backpropagation());
        maxEpochsSpinner.setValue(ex.maxEpochs());
        saveEverySpinner.setValue(ex.saveEvery());
        learningRateField.setValue(ex.learnRate());
        degradationField.setValue(ex.degradation());
        mseField.setValue(ex.mse());
        pathField.setText(ex.getFolder());
        weightField.setText(ex.getWeightFile());
        trainPlotCheck.setSelected(ex.plotTraining());
        trainBox.setSelectedIndex(ex.trainingFormat());
        validPlotCheck.setSelected(ex.plotValidation());
        validBox.setSelectedIndex(ex.validationFormat());
        generPlotCheck.setSelected(ex.plotGeneralization());
        generBox.setSelectedIndex(ex.generalizationFormat());
        smoothErroCheck.setSelected(ex.smoothError());
        showLabelsCheck.setSelected(ex.showLabels());
    }

    private final Map<JMenuItem, Example> exampleMap = new HashMap<>(11);
    {
        exampleMap.put(exampleSinMI,       new com.dkt.mrft.examples.ExampleSin());
        exampleMap.put(exampleCosMI,       new com.dkt.mrft.examples.ExampleCos());
        exampleMap.put(exampleSincMI,      new com.dkt.mrft.examples.ExampleSinc());
        exampleMap.put(exampleJumpyMI,     new com.dkt.mrft.examples.ExampleJumpy());
        exampleMap.put(exampleVeryJumpyMI, new com.dkt.mrft.examples.ExampleVeryJumpy());
        exampleMap.put(exampleTriangleMI,  new com.dkt.mrft.examples.ExampleTriangles());
        exampleMap.put(exampleSquareMI,    new com.dkt.mrft.examples.ExampleSquare());
        exampleMap.put(exampleDunnoMI,     new com.dkt.mrft.examples.ExampleMetodos());
        exampleMap.put(exampleBatmanMI,    new com.dkt.mrft.examples.ExampleBatman());
        exampleMap.put(exampleEkgRealMI,   new com.dkt.mrft.examples.ExampleEKG());
        exampleMap.put(exampleEkgSynthMI,  new com.dkt.mrft.examples.ExampleSyntheticEKG());
    }

    private void initListners() {
        new FileDrop(trainTable, (File[] files) -> {
            readFiles(files, train);
        });
        new FileDrop(validTable, (File[] files) -> {
            readFiles(files, validate);
        });
        new FileDrop(generTable, (File[] files) -> {
            readFiles(files, generalize);
        });
        new FileDrop(topologyPanel, (File[] files) -> {
            readFiles(files, null);
        });


        ActionListener exampleListner = (e) -> {
            loadExample((JMenuItem)e.getSource(), exampleMap.get((JMenuItem)e.getSource()));
        };

        for (JMenuItem mi : exampleMap.keySet()) {
            mi.addActionListener(exampleListner);
        }
    }

    private boolean shouldDraw() {
        //@TODO should redraw after a DEICONIZATION...
        return tabbedPane.getSelectedIndex() == 2 && (getExtendedState() & ICONIFIED) == 0;
    }

    private synchronized void drawErrors() {
        if (!shouldDraw()) return;
        //@FIXME this should only be removed on selection change and on resize events... so lazy...
        errs.remove(strTrain);
        errs.remove(strValid);

        if (showLabelsCheck.isSelected()) {
            final int xs = errs.getXSize() - ERR_X_OFFSET;
            final int ys = errs.getYSize() - ERR_Y_OFFSET;
            strTrain.move(xs - 120, ys - 15);
            strValid.move(xs - 120, ys - 30);
            errs.add(strTrain);
            errs.add(strValid);
            strTrain.setPaint(colors[0]);
            strValid.setPaint(colors[1]);
        }

        if (smoothErroCheck.isSelected()) {
            drawErrors0();
        } else {
            drawErrors1();
        }
    }

    private synchronized void drawErrors0() {
        if (errors.getRowCount() == 0) {
            errs.repaint();
            return;
        }

        if (errors.getRowCount() < 3) {
            return;
        }

        if (errorPoints[TRAIN] == null) {
            errorPoints[TRAIN] = new GPath(errRes + 10);
            errorPoints[VALID] = new GPath(errRes + 10);
            errorPoints[TRAIN].setStroke(errStroke);
            errorPoints[VALID].setStroke(errStroke);

            errs.add(errorPoints[TRAIN]);
            errs.add(errorPoints[VALID]);
        }

        errorPoints[TRAIN].setPaint(colors[TRAIN]);
        errorPoints[VALID].setPaint(colors[VALID]);

        errorPoints[TRAIN].clear();
        errorPoints[VALID].clear();

        //Spline requires data to be sorted
        final double[][] temp = errors.getData();
        final Integer [] idxs = new Integer[temp[0].length];

        for(int i = 0; i < idxs.length; i++) idxs[i] = i;

        Arrays.sort(idxs, (Integer o1, Integer o2) ->
                Double.compare(temp[0][o1], temp[0][o2])
        );

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
            errorPoints[TRAIN] = new GPointArray(512);
            errorPoints[VALID] = new GPointArray(512);
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

    private synchronized void updateGraphics(int idx) {
        if (!shouldDraw()) return;
        //@FIXME this sucks... but c'mon! do you want to write it???
        func.remove(labTrain);
        func.remove(labValid);
        func.remove(labGen);

        if (showLabelsCheck.isSelected()) {
            final int xs = func.getXSize() / 2;
            final int ys = func.getYSize() / 2;
            int posy = 15;

            if (trainPlotCheck.isSelected()) {
                labTrain = new GString(xs - 120, ys - posy, i18n.__("Training"));
                labTrain.setPaint(colors[TRAIN]);
                func.add(labTrain);
                posy += 15;
            }
            if (validPlotCheck.isSelected()) {
                labValid = new GString(xs - 120, ys - posy, i18n.__("Validation"));
                labValid.setPaint(colors[VALID]);
                func.add(labValid);
                posy += 15;
            }
            if (generPlotCheck.isSelected()) {
                labGen = new GString(xs - 120, ys - posy, i18n.__("Generalization"));
                labGen.setPaint(colors[GENER]);
                func.add(labGen);
                posy += 15;
            }
        }

        if (list[idx] == null) {
            list[TRAIN] = trainBox;
            list[VALID] = validBox;
            list[GENER] = generBox;
            selections[TRAIN] = trainPlotCheck;
            selections[VALID] = validPlotCheck;
            selections[GENER] = generPlotCheck;
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
        dataClearAllMI.doClick(0);
        String path = file.getParent() + File.separator;
        pathField.setText(path);

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

        save.put("max.epochs", maxEpochsSpinner.getValue());
        save.put("save.every", saveEverySpinner.getValue());
        save.put("mce", ((Number) mseField.getValue()).doubleValue());
        save.put("leaning.rate", ((Number) learningRateField.getValue()).doubleValue());
        save.put("degradation", ((Number)degradationField.getValue()).doubleValue());
        save.put("backpropagation", backpropBox.getSelectedIndex());
        save.put("topology", layers.toString());
        save.put("working.directory", pathField.getText());
        save.put("weight.file", weightField.getText());
        save.put("data.train", sfiles[TRAIN]);
        save.put("data.valid", sfiles[VALID]);
        save.put("data.gener", sfiles[GENER]);
        save.put("data.error", sfiles[ERROR]);
        save.put("plot.train", trainPlotCheck.isSelected());
        save.put("plot.train.format", trainBox.getSelectedIndex());
        save.put("plot.train.color", Integer.toHexString(colors[TRAIN].getRGB()));
        save.put("plot.valid", validPlotCheck.isSelected());
        save.put("plot.valid.format", validBox.getSelectedIndex());
        save.put("plot.valid.color", Integer.toHexString(colors[VALID].getRGB()));
        save.put("plot.gener", generPlotCheck.isSelected());
        save.put("plot.gener.format", generBox.getSelectedIndex());
        save.put("plot.gener.color", Integer.toHexString(colors[GENER].getRGB()));
        save.put("smooth.errors", smoothErroCheck.isSelected());
        save.put("show.labels", showLabelsCheck.isSelected());

        try (PrintStream out = new PrintStream(fname + "__mlp.conf")) {
            save.store(out, "");
        } catch (Exception ex) {
            error("Error writing file '%s' (%s)", fname + "__mlp.conf", ex.getLocalizedMessage());
        }
    }

    public void loadAttributes() {
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
            logsTextPane.setCaretPosition(doc.getLength());
        } catch(BadLocationException e) {
            PS.println(e);
        }
    }

    public void checkTrainEnabled() {
        final boolean trainEnabled = train     .getRowCount() > 0 &&
                                     validate  .getRowCount() > 0 &&
                                     generalize.getRowCount() > 0;

        trainStartMI.setEnabled(trainEnabled);
        playPauseButton.setEnabled(trainEnabled);
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

