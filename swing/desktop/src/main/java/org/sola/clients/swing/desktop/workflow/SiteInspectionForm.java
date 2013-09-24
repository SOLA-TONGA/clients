/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.desktop.workflow;

import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.report.ServiceReportBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class SiteInspectionForm extends ContentPanel {

    ApplicationBean applicationBean;
    ApplicationServiceBean applicationServiceBean;
    boolean readOnly = false;

    /**
     * Creates new form SiteInspectionForm
     */
    public SiteInspectionForm() {
        initComponents();
        customizeForm();
    }

    public SiteInspectionForm(ApplicationBean appBean, ApplicationServiceBean appServiceBean,
            Boolean readOnly) {
        this.applicationBean = appBean;
        this.applicationServiceBean = appServiceBean;
        this.readOnly = readOnly;
        initComponents();
        customizeForm();
    }

    private void customizeForm() {
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/workflow/Bundle");
        if (applicationBean != null) {
            headerPanel.setTitleText(String.format(bundle.getString("SiteInspectionForm.headerPanel.titleText"),
                    applicationBean.getNr()));
        }
        // Set the labels on the application service panel     
        appServicePanel.lblActionDate.setText(bundle.getString("SiteInspectionForm.appServicePanel.lblActionDate.text"));
        appServicePanel.lblActionCompleted.setText(bundle.getString("SiteInspectionForm.appServicePanel.lblActionCompleted.text"));
        btnSave.setEnabled(!readOnly);
        btnPrint.setEnabled(!readOnly);
    }

    private ApplicationServicePanel createAppServicePanel() {
        if (applicationBean == null) {
            applicationBean = new ApplicationBean();
        }
        if (applicationServiceBean == null) {
            applicationServiceBean = new ApplicationServiceBean();
        }

        if (appServicePanel == null) {
            appServicePanel = new ApplicationServicePanel(applicationBean, applicationServiceBean, readOnly);
        }
        return appServicePanel;
    }
    
    private void save() {
        // Save survey form. Validate the applicationServiceBean to ensure
        // all user entered data is correct
        if (applicationServiceBean.validate(true).size() < 1) {
            SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                    applicationBean.saveApplication();
                    return null;
                }

                @Override
                public void taskDone() {
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_SUCCESSFULLY_SAVED);
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void showInspectionReport(){
        ServiceReportBean serviceReportBean = new ServiceReportBean();
        serviceReportBean.setAppBean(applicationBean);
        serviceReportBean.setAppServiceBean(applicationServiceBean);
        ReportViewerForm form = new ReportViewerForm(ReportManager.getSiteInspectionReport(serviceReportBean, null));
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        
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
        jToolBar = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        appServicePanel = createAppServicePanel();

        setHeaderPanel(headerPanel);
        setName("Form"); // NOI18N

        headerPanel.setName(""); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/workflow/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("SiteInspectionForm.headerPanel.titleText")); // NOI18N

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar.add(btnSave);

        btnPrint.setText(bundle.getString("SiteInspectionForm.btnPrint.text")); // NOI18N
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar.add(btnPrint);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(appServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        showInspectionReport();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.desktop.workflow.ApplicationServicePanel appServicePanel;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
}
