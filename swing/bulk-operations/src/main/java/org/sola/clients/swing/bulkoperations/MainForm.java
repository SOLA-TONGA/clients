/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations;

import org.sola.clients.beans.security.SecurityBean;

/**
 *
 * @author Elton Manoku
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        
    }

/** Runs post initialization tasks. */
private void postInit(){
	lblUserName.setText(SecurityBean.getCurrentUser().getUserName());
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
        btnHelp = new javax.swing.JButton();
        pnlStatusBar = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        pnlTaskPanel = new org.sola.clients.swing.common.tasks.TaskPanel();
        pnlMain = new org.sola.clients.swing.ui.MainContentPanel();
        menuMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuExit = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuHelpTophics = new javax.swing.JMenuItem();
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
        toolbarMain.add(btnLoadSpatialObjects);

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/help.png"))); // NOI18N
        btnHelp.setText(bundle.getString("MainForm.btnHelp.text")); // NOI18N
        btnHelp.setFocusable(false);
        btnHelp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbarMain.add(btnHelp);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(pnlTaskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        menuHelpTophics.setText(bundle.getString("MainForm.menuHelpTophics.text")); // NOI18N
        menuHelp.add(menuHelpTophics);

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
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnLoadSpatialObjects;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuHelpTophics;
    private javax.swing.JMenuBar menuMain;
    private org.sola.clients.swing.ui.MainContentPanel pnlMain;
    private javax.swing.JPanel pnlStatusBar;
    private org.sola.clients.swing.common.tasks.TaskPanel pnlTaskPanel;
    private javax.swing.JToolBar toolbarMain;
    // End of variables declaration//GEN-END:variables
}
