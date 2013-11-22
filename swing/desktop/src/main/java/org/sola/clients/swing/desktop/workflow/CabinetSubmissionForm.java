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

import javax.swing.JFormattedTextField;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.report.ServiceReportBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
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
public class CabinetSubmissionForm extends ContentPanel {

    ApplicationBean applicationBean;
    boolean readOnly = false;

    /**
     * Creates new form CabinetSubmissionForm
     */
    public CabinetSubmissionForm() {
        initComponents();
    }

    public CabinetSubmissionForm(ApplicationBean appBean, ApplicationServiceBean appServiceBean,
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
            headerPanel.setTitleText(String.format(bundle.getString("CabinetSubmissionForm.headerPanel.titleText"),
                    applicationBean.getNr()));
        }
        // Set the labels on the application service panel
        appServicePanel.lblActionDate.setText(bundle.getString("CabinetSubmissionForm.appServicePanel.lblActionDate.text"));
        appServicePanel.lblActionCompleted.setText(bundle.getString("CabinetSubmissionForm.appServicePanel.lblActionCompleted.text"));
        btnSave.setEnabled(!readOnly);
        txtApprovalDate.setEnabled(!readOnly);
        txtApprovalNumber.setEnabled(!readOnly);
        btnApprovalDateCalender.setEnabled(!readOnly);
    }
    
    private ApplicationServiceBean createServiceBean() {

        if (applicationServiceBean == null) {
            applicationServiceBean = new ApplicationServiceBean();
        }
        return applicationServiceBean;
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
    
    public void showCabinetSubmissionReport() {
        ServiceReportBean serviceReportBean = new ServiceReportBean();
        serviceReportBean.setAppBean(applicationBean);
        serviceReportBean.setAppServiceBean(applicationServiceBean);
        ReportViewerForm form = new ReportViewerForm(ReportManager.getCabinetSubmissionReport(serviceReportBean, null));
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }
    
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
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
        appServicePanel = createAppServicePanel();
        btnApprovalDateCalender = new javax.swing.JButton();
        approvalDateLabel = new javax.swing.JLabel();
        approvalNumberLabel = new javax.swing.JLabel();
        txtApprovalNumber = new javax.swing.JTextField();
        txtApprovalDate = new javax.swing.JFormattedTextField();

        setHeaderPanel(headerPanel);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/workflow/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("CabinetSubmissionForm.headerPanel.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        btnPrint.setText(bundle.getString("CabinetSubmissionForm.btnPrint.text")); // NOI18N
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);

        btnApprovalDateCalender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnApprovalDateCalender.setBorder(null);
        btnApprovalDateCalender.setName("btnApprovalDateCalender"); // NOI18N
        btnApprovalDateCalender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApprovalDateCalenderActionPerformed(evt);
            }
        });

        approvalDateLabel.setText(bundle.getString("CabinetSubmissionForm.approvalDateLabel.text")); // NOI18N

        approvalNumberLabel.setText(bundle.getString("CabinetSubmissionForm.approvalNumberLabel.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationServiceBean, org.jdesktop.beansbinding.ELProperty.create("${approvalNumber}"), txtApprovalNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtApprovalNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApprovalNumberActionPerformed(evt);
            }
        });

        txtApprovalDate.setText(bundle.getString("CabinetSubmissionForm.txtApprovalDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationServiceBean, org.jdesktop.beansbinding.ELProperty.create("${approvalDate}"), txtApprovalDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(appServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(approvalDateLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtApprovalDate, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApprovalDateCalender, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(approvalNumberLabel)
                    .addComponent(txtApprovalNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(approvalDateLabel)
                    .addComponent(approvalNumberLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnApprovalDateCalender)
                    .addComponent(txtApprovalNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApprovalDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appServicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        showCabinetSubmissionReport();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnApprovalDateCalenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApprovalDateCalenderActionPerformed
        showCalendar(txtApprovalDate);
    }//GEN-LAST:event_btnApprovalDateCalenderActionPerformed

    private void txtApprovalNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApprovalNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApprovalNumberActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.desktop.workflow.ApplicationServicePanel appServicePanel;
    private org.sola.clients.beans.application.ApplicationServiceBean applicationServiceBean;
    private javax.swing.JLabel approvalDateLabel;
    private javax.swing.JLabel approvalNumberLabel;
    private javax.swing.JButton btnApprovalDateCalender;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JFormattedTextField txtApprovalDate;
    private javax.swing.JTextField txtApprovalNumber;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
