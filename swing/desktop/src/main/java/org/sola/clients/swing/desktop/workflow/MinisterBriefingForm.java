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
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class MinisterBriefingForm extends ContentPanel {

    ApplicationBean applicationBean;
    boolean readOnly = false;

    /**
     * Creates new form MinisterBriefingForm
     */
    public MinisterBriefingForm() {
        initComponents();
    }

    public MinisterBriefingForm(ApplicationBean appBean, ApplicationServiceBean appServiceBean,
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
            headerPanel.setTitleText(String.format(bundle.getString("MinisterBriefingForm.headerPanel.titleText"),
                    applicationBean.getNr()));
        }
        // Set the labels on the application service panel
        appServicePanel.lblActionDate.setText(bundle.getString("MinisterBriefingForm.appServicePanel.lblActionDate.text"));
        appServicePanel.lblActionCompleted.setText(bundle.getString("MinisterBriefingForm.appServicePanel.lblActionCompleted.text"));
        btnSave.setEnabled(!readOnly);
        reportOneTextField.setEnabled(!readOnly);
        reportTwoTextField.setEnabled(!readOnly);
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

    private ApplicationServiceBean createServiceBean() {

        if (applicationServiceBean == null) {
            applicationServiceBean = new ApplicationServiceBean();
        }
        return applicationServiceBean;
    }

    private void save() {
        WindowUtility.commitChanges(this);
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

    public void showMinisterialBriefingReport() {
        ServiceReportBean serviceReportBean = new ServiceReportBean();
        serviceReportBean.setAppBean(applicationBean);
        serviceReportBean.setAppServiceBean(applicationServiceBean);
        ReportViewerForm form = new ReportViewerForm(ReportManager.getMinisterialBriefingReport(serviceReportBean, null));
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    public void showSurveySavingramReport() {
        ServiceReportBean reportBean = new ServiceReportBean(applicationBean, applicationServiceBean);
        ReportViewerForm form = new ReportViewerForm(ReportManager.getSurveySavingramReport(reportBean, null));
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        applicationServiceBean = createServiceBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        btnPrintSurveySavingram = new org.sola.clients.swing.common.buttons.BtnPrint();
        appServicePanel = createAppServicePanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        reportOneTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        reportTwoTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setHeaderPanel(headerPanel);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/workflow/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("MinisterBriefingForm.headerPanel.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        btnPrint.setText(bundle.getString("MinisterBriefingForm.btnPrint.text")); // NOI18N
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);

        btnPrintSurveySavingram.setText(bundle.getString("MinisterBriefingForm.btnPrintSurveySavingram.text")); // NOI18N
        btnPrintSurveySavingram.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrintSurveySavingram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSurveySavingramActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintSurveySavingram);

        jPanel3.setLayout(new java.awt.GridLayout());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationServiceBean, org.jdesktop.beansbinding.ELProperty.create("${reportTextOne}"), reportOneTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel1.setText(bundle.getString("MinisterBriefingForm.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(reportOneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportOneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel1);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationServiceBean, org.jdesktop.beansbinding.ELProperty.create("${reportTextTwo}"), reportTwoTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel2.setText(bundle.getString("MinisterBriefingForm.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(reportTwoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportTwoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(appServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        showMinisterialBriefingReport();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnPrintSurveySavingramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSurveySavingramActionPerformed
        showSurveySavingramReport();
    }//GEN-LAST:event_btnPrintSurveySavingramActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.desktop.workflow.ApplicationServicePanel appServicePanel;
    private org.sola.clients.beans.application.ApplicationServiceBean applicationServiceBean;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrintSurveySavingram;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField reportOneTextField;
    private javax.swing.JTextField reportTwoTextField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
