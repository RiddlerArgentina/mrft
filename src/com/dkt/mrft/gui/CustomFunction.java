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

import com.dkt.mrft.funcs.FunctionsRandom;
import com.dkt.mrft.utils.BundleDecorator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.extras.FunctionsBoolean;
import net.objecthunter.exp4j.extras.FunctionsMisc;
import net.objecthunter.exp4j.extras.OperatorsComparison;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public final class CustomFunction extends javax.swing.JDialog {
    private static final BundleDecorator i18n = new BundleDecorator("res.i18n.dialogs");
    
    public CustomFunction() {
        super((JFrame)null, true);
        initComponents();
        initListners();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JLabel expXLabel = new JLabel();
        JLabel expFXLabel = new JLabel();
        JLabel jLabel3 = new JLabel();
        JButton closeButton = new JButton();

        ResourceBundle bundle = ResourceBundle.getBundle("res/i18n/dialogs"); // NOI18N
        setTitle(bundle.getString("F_CUSTOM_TITLE")); // NOI18N

        expXLabel.setText(bundle.getString("F_CUSTOM_FOR_X")); // NOI18N

        expFXLabel.setText(bundle.getString("F_CUSTOM_FOR_FX")); // NOI18N

        jLabel3.setText(bundle.getString("F_CUSTOM_VARS")); // NOI18N

        applyButton.setText(bundle.getString("F_CUSTOM_APPLY")); // NOI18N
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        closeButton.setText(bundle.getString("F_CUSTOM_CLOSE")); // NOI18N
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        validXLabel.setIcon(new ImageIcon(getClass().getResource("/res/icons/a_ok.png"))); // NOI18N

        validFXLabel.setIcon(new ImageIcon(getClass().getResource("/res/icons/a_ok.png"))); // NOI18N

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(expXLabel)
                        .addGap(18, 18, 18)
                        .addComponent(expXTextField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(validXLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(expFXLabel)
                        .addGap(5, 5, 5)
                        .addComponent(expFXTextField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(validFXLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(expXLabel)
                    .addComponent(expXTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(validXLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(expFXLabel)
                        .addComponent(expFXTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(validFXLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(applyButton)
                    .addComponent(closeButton)
                    .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean noOutput;
    private void closeButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        noOutput = true;
        setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void applyButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        noOutput = false;
        setVisible(false);
    }//GEN-LAST:event_applyButtonActionPerformed

    private final ImageIcon warn = new ImageIcon(getClass().getResource("/res/icons/warn.png"));
    private final ImageIcon a_ok = new ImageIcon(getClass().getResource("/res/icons/a_ok.png"));
    private void initListners() {
        DocumentListener dl1 = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                checkApplyButton(expXTextField, validXLabel);
            }
            @Override public void removeUpdate(DocumentEvent e) {
                checkApplyButton(expXTextField, validXLabel);
            }
            @Override public void changedUpdate(DocumentEvent e) {
                checkApplyButton(expXTextField, validXLabel);
            }
        };
        
        DocumentListener dl2 = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                checkApplyButton(expFXTextField, validFXLabel);
            }
            @Override public void removeUpdate(DocumentEvent e) {
                checkApplyButton(expFXTextField, validFXLabel);
            }
            @Override public void changedUpdate(DocumentEvent e) {
                checkApplyButton(expFXTextField, validFXLabel);
            }
        };
        
        expXTextField .getDocument().addDocumentListener(dl1);
        expFXTextField.getDocument().addDocumentListener(dl2);
        
        expXTextField .setText("x");
        expFXTextField.setText("fx");
    }
    

    private void checkApplyButton(JTextField txtField, JLabel expLabel) {
        applyButton.setEnabled(validateFunc(txtField, expLabel) 
                            && validXLabel .getIcon() == a_ok
                            && validFXLabel.getIcon() == a_ok);
    }
    
    private boolean validateFunc(JTextField tField, JLabel label) {
        String expString = tField.getText();
        if (expString.trim().isEmpty()) {
            label.setIcon(warn);
            return false;
        }
        
        try {
            ExpressionBuilder exp = new ExpressionBuilder(expString);
            HashMap<String, Double> map = new HashMap<>(2);
            //@TODO Remove this dirty hack with the next exp4j version
            if (expString.replace("fx", "").contains("x" )) map.put("x", 1d);
            if (expString.contains("fx"))map.put("fx", 1d);
            
            exp.variables(map.keySet());            
            exp.functions(FunctionsMisc.getFunctions());
            exp.functions(FunctionsBoolean.getFunctions());
            exp.functions(FunctionsRandom.getFunctions());
            exp.operator (OperatorsComparison.getOperators());
            
            Expression e = exp.build().setVariables(map);
            ValidationResult vr = e.validate();
            
            if (vr.isValid()) {
                label.setIcon(a_ok);
                label.setToolTipText("");
            } else {
                label.setToolTipText(i18n.__("F_CUSTOM_CHECK_EXP"));
                label.setIcon(warn);
                return false;
            }
            
        } catch (Exception ex) {
            label.setToolTipText(ex.getMessage());
            label.setIcon(warn);
            return false;
        }
        return true;
    }
    
    public String getExp4XStr() {
        if (noOutput) return null;
        return expXTextField.getText();
    }
    
    public String getExp4FXStr() {
        if (noOutput) return null;
        return expFXTextField.getText();
    }
    
    public Expression getExp4X() {
        if (noOutput) return null;
        
        String expString = expXTextField.getText();
        ExpressionBuilder exp = new ExpressionBuilder(expString);
        //@TODO Remove this dirty hack with the next exp4j version
        if (expString.replace("fx", "").contains("x" ))exp.variable("x" );
        if (expString.contains("fx"))exp.variable("fx");
        
        exp.functions(FunctionsMisc.getFunctions());
        exp.functions(FunctionsBoolean.getFunctions());
        exp.functions(FunctionsRandom.getFunctions());
        exp.operator (OperatorsComparison.getOperators());
        
        return exp.build(true);
    }
    
    public Expression getExp4FX() {
        if (noOutput) return null;
        
        String expString = expFXTextField.getText();
        ExpressionBuilder exp = new ExpressionBuilder(expString);
        //@TODO Remove this dirty hack with the next exp4j version
        if (expString.replace("fx", "").contains("x" ))exp.variable("x" );
        if (expString.contains("fx"))exp.variable("fx");
        
        exp.functions(FunctionsMisc.getFunctions());
        exp.functions(FunctionsBoolean.getFunctions());
        exp.functions(FunctionsRandom.getFunctions());
        exp.operator (OperatorsComparison.getOperators());
        
        return exp.build(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final JButton applyButton = new JButton();
    private final JTextField expFXTextField = new JTextField();
    private final JTextField expXTextField = new JTextField();
    private final JLabel validFXLabel = new JLabel();
    private final JLabel validXLabel = new JLabel();
    // End of variables declaration//GEN-END:variables
}
