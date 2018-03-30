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

import com.dkt.mrft.funcs.FunctionsRandom;
import com.dkt.mrft.models.DatasetTableModel;
import com.dkt.mrft.utils.BundleDecorator;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.extras.FunctionsBoolean;
import net.objecthunter.exp4j.extras.FunctionsMisc;
import net.objecthunter.exp4j.extras.OperatorsComparison;

/**
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
public final class FunctionMonospaced extends JDialog {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.dialogs");
    private final ImageIcon warn = new ImageIcon(getClass().getResource("/res/icons/warn.png"));
    private final ImageIcon a_ok = new ImageIcon(getClass().getResource("/res/icons/a_ok.png"));

    // Variables declaration - do not modify
    private final JFormattedTextField endField = new JFormattedTextField();
    private final JTextField expField = new JTextField();
    private final JButton genCloseButton = new JButton();
    private final JLabel numPointsLabel = new JLabel();
    private final JFormattedTextField startField = new JFormattedTextField();
    private final JFormattedTextField stepField = new JFormattedTextField();
    private final JLabel validExpLabel = new JLabel();
    // End of variables declaration

    private boolean out;
    private final MainWindow father;

    public FunctionMonospaced(MainWindow father) {
        super((JFrame)null, true);
        this.father = father;
        initComponents();
        initListners();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        final JLabel expLabel = new JLabel();
        final JLabel startLabel = new JLabel();
        final JLabel endLabel = new JLabel();
        final JLabel stepLabel = new JLabel();
        final JButton closeButton = new JButton();

        ResourceBundle bundle = ResourceBundle.getBundle("res/i18n/dialogs"); // NOI18N
        setTitle(bundle.getString("F_MONO_TITLE")); // NOI18N

        expLabel.setText(bundle.getString("F_MONO_FUNC")); // NOI18N

        expField.setText("sin(x)");

        validExpLabel.setIcon(new ImageIcon(getClass().getResource("/res/icons/warn.png"))); // NOI18N

        startLabel.setText(bundle.getString("F_MONO_START")); // NOI18N

        endLabel.setText(bundle.getString("F_MONO_END")); // NOI18N

        stepLabel.setText(bundle.getString("F_MONO_STEP")); // NOI18N

        genCloseButton.setText(bundle.getString("F_MONO_GENERATE")); // NOI18N
        genCloseButton.setEnabled(false);
        genCloseButton.addActionListener((ActionEvent evt) -> {
            out = true;
            setVisible(false);
        });

        closeButton.setText(bundle.getString("F_MONO_CLOSE")); // NOI18N
        closeButton.addActionListener((ActionEvent evt) -> {
            out = false;
            setVisible(false);
        });

        numPointsLabel.setText(bundle.getString("F_MONO_N_VALS")); // NOI18N

        startField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
        startField.setHorizontalAlignment(JTextField.CENTER);

        stepField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
        stepField.setHorizontalAlignment(JTextField.CENTER);

        endField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
        endField.setHorizontalAlignment(JTextField.CENTER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(numPointsLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(genCloseButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(expLabel)
                            .addComponent(startLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(expField)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validExpLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startField, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endField, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stepLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(stepField, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {closeButton, genCloseButton});

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(validExpLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(expLabel)
                        .addComponent(expField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(startLabel)
                    .addComponent(endLabel)
                    .addComponent(stepLabel)
                    .addComponent(startField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(stepField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(genCloseButton)
                    .addComponent(closeButton)
                    .addComponent(numPointsLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void initListners() {
        DocumentListener dl = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                genCloseButton.setEnabled(checkInterval() && validateFunc());
            }

            @Override public void removeUpdate(DocumentEvent e) {
                genCloseButton.setEnabled(checkInterval() && validateFunc());
            }

            @Override public void changedUpdate(DocumentEvent e) {
                genCloseButton.setEnabled(checkInterval() && validateFunc());
            }
        };

        expField.getDocument().addDocumentListener(dl);
        startField.getDocument().addDocumentListener(dl);
        stepField.getDocument().addDocumentListener(dl);
        endField.getDocument().addDocumentListener(dl);

        startField.setValue(-3);
        endField  .setValue(3);
        stepField .setValue(0.05);

        genCloseButton.setEnabled(checkInterval() && validateFunc());
    }

    private boolean checkInterval() {
        try {
            startField.commitEdit();
            stepField.commitEdit();
            endField.commitEdit();
        } catch (ParseException ignoreMe) {
            numPointsLabel.setText(i18n.__("F_MONO_INVALID"));
            return false;
        }

        double start = ((Number)startField.getValue()).doubleValue();
        double end   = ((Number)endField.getValue()).doubleValue();
        double step  = ((Number)stepField.getValue()).doubleValue();
        boolean status = start < end && step > 0;

        if (!status) {
            numPointsLabel.setText(i18n.__("F_MONO_INVALID"));
        } else {
            numPointsLabel.setText(i18n.__("F_MONO_N_VALS", Math.round((end - start) / step)));
        }

        return status;
    }

    private boolean validateFunc() {
        String expString = expField.getText();
        if (expString.trim().isEmpty()) {
            validExpLabel.setIcon(warn);
            return false;
        }

        try {
            ExpressionBuilder exp = new ExpressionBuilder(expString).variable("x");
            exp.functions(FunctionsMisc.getFunctions());
            exp.functions(FunctionsBoolean.getFunctions());
            exp.functions(FunctionsRandom.getFunctions());
            exp.operators(OperatorsComparison.getOperators());

            Expression e = exp.build().setVariable("x", 1);
            ValidationResult vr = e.validate();
            if (vr.isValid()) {
                validExpLabel.setIcon(a_ok);
                validExpLabel.setToolTipText("");
            } else {
                validExpLabel.setToolTipText(i18n.__("F_MONO_CHECK_EXP"));
                validExpLabel.setIcon(warn);
                return false;
            }
        } catch (Exception ex) {
            validExpLabel.setToolTipText(ex.getMessage());
            validExpLabel.setIcon(warn);
            return false;
        }
        return true;
    }

    public ArrayList<DatasetTableModel.Row> getData() {
        if (!out) {
            return new ArrayList<>(0);
        }

        double start = ((Number)startField.getValue()).doubleValue();
        double end   = ((Number)endField.getValue()).doubleValue();
        double step  = ((Number)stepField.getValue()).doubleValue();
        int nvals    = (int)Math.round((end - start) / step) + 1;

        ArrayList<DatasetTableModel.Row> ret = new ArrayList<>(nvals);

        String expString = expField.getText();
        try {
            ExpressionBuilder exp = new ExpressionBuilder(expString).variable("x");
            exp.functions(FunctionsBoolean.getFunctions());
            exp.functions(FunctionsMisc.getFunctions());
            exp.functions(FunctionsRandom.getFunctions());
            exp.operators(OperatorsComparison.getOperators());
            Expression f = exp.build(true);

            for (double x = start; x <= end; x += step) {
                ret.add(new DatasetTableModel.Row(false, x, f.setVariable("x", x).evaluate()));
            }

            father.info(
                "Evaluating: '%s' in [%f,%f] with step %f",
                expString, start, end, step
            );
        } catch (Exception ex) {
            father.error(
                "Error evaluating: '%s' in [%f,%f] with step %f (%s)",
                expString, start, end, step, ex.getMessage()
            );
            validExpLabel.setToolTipText(ex.getMessage());
            validExpLabel.setIcon(warn);
        }

        return ret;
    }
}
