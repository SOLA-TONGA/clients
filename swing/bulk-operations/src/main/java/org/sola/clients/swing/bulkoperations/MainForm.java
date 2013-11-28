/**
 * ******************************************************************************************
 * Copyright (C) 2013 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations;

import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.bulkoperations.sources.LoadSourcesPanel;
import org.sola.clients.swing.bulkoperations.spatialobjects.ImportSpatialPanel;
import org.sola.clients.swing.bulkoperations.spatialobjects.MapPanel;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.common.help.HelpUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * The main form of the application. This is the form where everything is open.
 * 
 * @author Elton Manoku
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        HelpUtility.getInstance().registerHelpMenu(jmiContextHelp, "1_blk_overview");

        this.setExtendedState(MAXIMIZED_BOTH);

    }

    /**
     * Runs post initialization tasks.
     */
    private void postInit() {
        lblUserName.setText(SecurityBean.getCurrentUser().getUserName());
    }

    private void loadSpatialObjectPanel() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(
                        ClientMessage.PROGRESS_MSG_SPATIAL_OBJECT_LOAD_STARTING));
                ImportSpatialPanel panel = new ImportSpatialPanel();
                pnlMain.addPanel(panel, panel.getName(), true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    private void loadSourcesPanel() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(
                        ClientMessage.PROGRESS_MSG_SOURCE_LOAD_STARTING));
                LoadSourcesPanel panel = new LoadSourcesPanel();
                pnlMain.addPanel(panel, panel.getName(), true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    private void openMap() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(
                        ClientMessage.PROGRESS_MSG_OPEN_MAP));
                MapPanel mapPanel = new MapPanel(null);
                pnlMain.addPanel(mapPanel, mapPanel.getName(), true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbarMain = new javax.swing.JToolBar();
        btnLoadSpatialObjects = new javax.swing.JButton();
        btnLoadSources = new javax.swing.JButton();
        btnOpenMap = new javax.swing.JButton();
        pnlStatusBar = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        pnlTaskPanel = new org.sola.clients.swing.common.tasks.TaskPanel();
        pnlMain = new org.sola.clients.swing.ui.MainContentPanel();
        menuMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuExit = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        jmiContextHelp = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/Bundle"); // NOI18N
        setTitle(bundle.getString("MainForm.title")); // NOI18N
        setPreferredSize(new java.awt.Dimension(600, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        toolbarMain.setFloatable(false);
        toolbarMain.setRollover(true);

        btnLoadSpatialObjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/load-spatial-objects.png"))); // NOI18N
        btnLoadSpatialObjects.setText(bundle.getString("MainForm.btnLoadSpatialObjects.text")); // NOI18N
        btnLoadSpatialObjects.setFocusable(false);
        btnLoadSpatialObjects.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnLoadSpatialObjects.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLoadSpatialObjects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSpatialObjectsActionPerformed(evt);
            }
        });
        toolbarMain.add(btnLoadSpatialObjects);

        btnLoadSources.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/source-load.png"))); // NOI18N
        btnLoadSources.setText(bundle.getString("MainForm.btnLoadSources.text")); // NOI18N
        btnLoadSources.setFocusable(false);
        btnLoadSources.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnLoadSources.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLoadSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSourcesActionPerformed(evt);
            }
        });
        toolbarMain.add(btnLoadSources);

        btnOpenMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/network.png"))); // NOI18N
        btnOpenMap.setText(bundle.getString("MainForm.btnOpenMap.text")); // NOI18N
        btnOpenMap.setFocusable(false);
        btnOpenMap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnOpenMap.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenMapActionPerformed(evt);
            }
        });
        toolbarMain.add(btnOpenMap);

        pnlStatusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblUser.setText(bundle.getString("MainForm.lblUser.text")); // NOI18N

        lblUserName.setText(bundle.getString("MainForm.lblUserName.text")); // NOI18N

        javax.swing.GroupLayout pnlStatusBarLayout = new javax.swing.GroupLayout(pnlStatusBar);
        pnlStatusBar.setLayout(pnlStatusBarLayout);
        pnlStatusBarLayout.setHorizontalGroup(
            pnlStatusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusBarLayout.createSequentialGroup()
                .addComponent(lblUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addComponent(pnlTaskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlStatusBarLayout.setVerticalGroup(
            pnlStatusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblUserName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
            .addComponent(pnlTaskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuFile.setText(bundle.getString("MainForm.menuFile.text")); // NOI18N

        menuExit.setText(bundle.getString("MainForm.menuExit.text")); // NOI18N
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        menuFile.add(menuExit);

        menuMain.add(menuFile);

        menuHelp.setText(bundle.getString("MainForm.menuHelp.text")); // NOI18N

        jmiContextHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/help.png"))); // NOI18N
        jmiContextHelp.setText(bundle.getString("MainForm.jmiContextHelp.text")); // NOI18N
        menuHelp.add(jmiContextHelp);

        menuAbout.setText(bundle.getString("MainForm.menuAbout.text")); // NOI18N
        menuHelp.add(menuAbout);

        menuMain.add(menuHelp);

        setJMenuBar(menuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbarMain, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        postInit();
    }//GEN-LAST:event_formWindowOpened

    private void btnLoadSpatialObjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSpatialObjectsActionPerformed
        loadSpatialObjectPanel();
    }//GEN-LAST:event_btnLoadSpatialObjectsActionPerformed

    private void btnOpenMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenMapActionPerformed
        openMap();
    }//GEN-LAST:event_btnOpenMapActionPerformed

    private void btnLoadSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSourcesActionPerformed
        loadSourcesPanel();
    }//GEN-LAST:event_btnLoadSourcesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadSources;
    private javax.swing.JButton btnLoadSpatialObjects;
    private javax.swing.JButton btnOpenMap;
    private javax.swing.JMenuItem jmiContextHelp;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuBar menuMain;
    private org.sola.clients.swing.ui.MainContentPanel pnlMain;
    private javax.swing.JPanel pnlStatusBar;
    private org.sola.clients.swing.common.tasks.TaskPanel pnlTaskPanel;
    private javax.swing.JToolBar toolbarMain;
    // End of variables declaration//GEN-END:variables
}
