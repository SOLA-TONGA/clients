/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.sources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import org.sola.clients.swing.bulkoperations.beans.SourceBulkMoveBean;
import org.sola.clients.swing.bulkoperations.ValidationResultPanel;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * The panel that handles the bulk operations for loading documents (sources).
 *
 * @author Elton Manoku
 */
public class LoadSourcesPanel extends ContentPanel {

    private static String PANEL_NAME = "LOAD_SOURCES_PANEL";
    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(
            "org/sola/clients/swing/bulkoperations/sources/Bundle");
    private SourceBulkMoveBean bulkMove = new SourceBulkMoveBean();

    /**
     * Creates new form LoadSourcesPanel
     */
    public LoadSourcesPanel() {
        initComponents();
        this.setName(PANEL_NAME);
        folderChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return bundle.getString("LoadSourcesPanel.folderChooser.DirectoriesOnlyText");
            }
        });
        setPostLoadEnabled(false);
    }

    private void setPostLoadEnabled(boolean enable) {
        btnRollback.setEnabled(enable);
        String informationResourceName = "LoadSourcesPanel.lblInformationText.text";
        if (!enable) {
            bulkMove.reset();
        } else {
            informationResourceName = bulkMove.getValidationResults().isEmpty()
                    ? "LoadSourcesPanel.lblInformationText.text.success"
                    : "LoadSourcesPanel.lblInformationText.text.problem";
        }
        lblInformationText.setText(bundle.getString(informationResourceName));
        btnValidations.setEnabled(!bulkMove.getValidationResults().isEmpty());
    }

    private void convertAndSendToServer() {
        setPostLoadEnabled(false);
        if (folderChooser.getSelectedFile() == null) {
            folderChooser.setSelectedFile(folderChooser.getCurrentDirectory());
        }
        bulkMove.setBaseFolder(folderChooser.getSelectedFile());

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(
                        ClientMessage.BULK_OPERATIONS_LOAD_SOURCE_AND_SENDTOSERVER));
                bulkMove.sendToServer();
                return null;
            }

            @Override
            protected void taskDone() {
                super.taskDone();
                setPostLoadEnabled(true);
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    private void rollback() {
        if (bulkMove.getTransaction() != null) {
            bulkMove.getTransaction().reject();
        }
        setPostLoadEnabled(false);
    }

    private void openValidations() {
        ValidationResultPanel validationPanel = new ValidationResultPanel();
        validationPanel.getValidationResultList().addAll(bulkMove.getValidationResults());
        getMainContentPanel().addPanel(validationPanel, validationPanel.getName(), true);
    }

    /**
     * Load documents from the Network folder as a background task. Run a timer
     * to refresh the progress message every 10 seconds.
     */
    private void loadDocuments() {
        final String result[] = {""};
        txtProgressMsg.setText("");

        // Create a timer to refresh the progress message every 15 seconds
        final Timer timer = new Timer(15000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshProgressMessage();
            }
        });
        timer.start();

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage("Loading documents...");
                result[0] = WSManager.getInstance().getBulkOperationsService().
                        loadDocuments(txtDocType.getText(), txtSourceFolder.getText());
                return null;
            }

            @Override
            public void taskDone() {
                timer.stop();
                txtProgressMsg.setText(result[0]);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void refreshProgressMessage() {
        txtProgressMsg.setText("");
        String progressMsg = WSManager.getInstance().getBulkOperationsService().getBulkLoadProgressMessage();
        txtProgressMsg.setText(progressMsg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        folderChooser = new javax.swing.JFileChooser();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        jPanel2 = new javax.swing.JPanel();
        btnLoad = new javax.swing.JButton();
        lblInformation = new javax.swing.JLabel();
        lblInformationText = new javax.swing.JLabel();
        btnValidations = new javax.swing.JButton();
        btnRollback = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtProgressMsg = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtDocType = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtSourceFolder = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNetworkLoad = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setHeaderPanel(headerPanel);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/sources/Bundle"); // NOI18N
        setHelpTopic(bundle.getString("LoadSourcePanel.helptopic")); // NOI18N

        headerPanel.setTitleText(bundle.getString("LoadSourcesPanel.headerPanel.titleText")); // NOI18N

        folderChooser.setAcceptAllFileFilterUsed(false);
        folderChooser.setControlButtonsAreShown(false);
        folderChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        groupPanel1.setTitleText(bundle.getString("LoadSourcesPanel.groupPanel1.titleText")); // NOI18N

        groupPanel2.setTitleText(bundle.getString("LoadSourcesPanel.groupPanel2.titleText")); // NOI18N

        btnLoad.setText(bundle.getString("LoadSourcesPanel.btnLoad.text")); // NOI18N
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        lblInformation.setText(bundle.getString("LoadSourcesPanel.lblInformation.text")); // NOI18N

        lblInformationText.setText(bundle.getString("LoadSourcesPanel.lblInformationText.text")); // NOI18N

        btnValidations.setText(bundle.getString("LoadSourcesPanel.btnValidations.text")); // NOI18N
        btnValidations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidationsActionPerformed(evt);
            }
        });

        btnRollback.setText(bundle.getString("LoadSourcesPanel.btnRollback.text")); // NOI18N
        btnRollback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollbackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInformation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInformationText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnValidations)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRollback)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblInformation)
                .addComponent(lblInformationText)
                .addComponent(btnRollback)
                .addComponent(btnValidations))
            .addComponent(btnLoad)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(folderChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(folderChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("LoadSourcesPanel.jPanel1.TabConstraints.tabTitle_1"), jPanel1); // NOI18N

        txtProgressMsg.setColumns(20);
        txtProgressMsg.setRows(5);
        jScrollPane1.setViewportView(txtProgressMsg);

        jPanel4.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        txtDocType.setText(bundle.getString("LoadSourcesPanel.txtDocType.text")); // NOI18N

        jLabel2.setText(bundle.getString("LoadSourcesPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDocType)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDocType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5);

        txtSourceFolder.setText(bundle.getString("LoadSourcesPanel.txtSourceFolder.text")); // NOI18N

        jLabel1.setText(bundle.getString("LoadSourcesPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSourceFolder)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSourceFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnNetworkLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/source-load.png"))); // NOI18N
        btnNetworkLoad.setText(bundle.getString("LoadSourcesPanel.btnNetworkLoad.text")); // NOI18N
        btnNetworkLoad.setFocusable(false);
        btnNetworkLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetworkLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetworkLoadActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNetworkLoad);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/refresh.png"))); // NOI18N
        btnRefresh.setText(bundle.getString("LoadSourcesPanel.btnRefresh.text")); // NOI18N
        btnRefresh.setFocusable(false);
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefresh);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/cancel.png"))); // NOI18N
        btnCancel.setText(bundle.getString("LoadSourcesPanel.btnCancel.text")); // NOI18N
        btnCancel.setFocusable(false);
        btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancel);

        jLabel3.setText(bundle.getString("LoadSourcesPanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("LoadSourcesPanel.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        convertAndSendToServer();
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnRollbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollbackActionPerformed
        rollback();
    }//GEN-LAST:event_btnRollbackActionPerformed

    private void btnValidationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidationsActionPerformed
        openValidations();
    }//GEN-LAST:event_btnValidationsActionPerformed

    private void btnNetworkLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetworkLoadActionPerformed
        loadDocuments();
    }//GEN-LAST:event_btnNetworkLoadActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshProgressMessage();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        WSManager.getInstance().getBulkOperationsService().cancelLoad();
    }//GEN-LAST:event_btnCancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnNetworkLoad;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRollback;
    private javax.swing.JButton btnValidations;
    private javax.swing.JFileChooser folderChooser;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblInformation;
    private javax.swing.JLabel lblInformationText;
    private javax.swing.JTextField txtDocType;
    private javax.swing.JTextArea txtProgressMsg;
    private javax.swing.JTextField txtSourceFolder;
    // End of variables declaration//GEN-END:variables
}
