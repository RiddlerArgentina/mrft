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

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
public final class AboutDialog extends JDialog {
    private final MainWindow father;

    public AboutDialog(MainWindow father) {
        super((JFrame)null, true);
        this.father = father;
        initComponents();
        load("ABOUT");
        aboutButton  .addActionListener((ActionEvent e) -> {load("ABOUT");});
        creditsButton.addActionListener((ActionEvent e) -> {load("CREDITS");});
        licenseButton.addActionListener((ActionEvent e) -> {load("LICENSE");});
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        final ButtonGroup buttonGroup1 = new ButtonGroup();
        final JScrollPane scrollPane = new JScrollPane();
        textPane = new JTextPane();
        final JPanel buttonsPanel = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("res/i18n/dialogs"); // NOI18N
        setTitle(bundle.getString("ABOUT_TITLE")); // NOI18N

        textPane.setContentType("text/html"); // NOI18N
        scrollPane.setViewportView(textPane);

        buttonGroup1.add(aboutButton);
        aboutButton.setText(bundle.getString("ABOUT_ABOUT")); // NOI18N
        buttonsPanel.add(aboutButton);

        buttonGroup1.add(creditsButton);
        creditsButton.setText(bundle.getString("ABOUT_CREDITS")); // NOI18N
        buttonsPanel.add(creditsButton);

        buttonGroup1.add(licenseButton);
        licenseButton.setText(bundle.getString("ABOUT_LICENSE")); // NOI18N
        buttonsPanel.add(licenseButton);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(buttonsPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private final JToggleButton aboutButton = new JToggleButton();
    private final JToggleButton creditsButton = new JToggleButton();
    private final JToggleButton licenseButton = new JToggleButton();
    private JTextPane textPane;
    // End of variables declaration                   

    private void load(String file) {
        StringBuilder content = new StringBuilder(4096);

        try (InputStream is = getClass().getResourceAsStream("/res/" + file + ".html");
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String temp;
            while((temp = reader.readLine()) != null){
                content.append(temp).append('\n');
            }

            father.debug("File '%s' successfully loaded", file);
            textPane.setText(content.toString());
            textPane.setCaretPosition(0);
        } catch (Exception e) {
            father.error("Error reading file '%s' (%s)", file, e.getLocalizedMessage());
        }
    }
}
