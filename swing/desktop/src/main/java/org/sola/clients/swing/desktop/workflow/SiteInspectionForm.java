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
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class SiteInspectionForm extends ContentPanel {
    
    /**
     * Creates new form SiteInspectionForm
     */
    public SiteInspectionForm() {
        initComponents();
    }

    public SiteInspectionForm(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
        initComponents();
        customizeForm();
    }

    private void customizeForm() {
      if (applicationBean != null) {
            headerPanel.setTitleText(String.format("Site Inspection for Application: #%s",
                    applicationBean.getNr()));
        }
    }

    private DocumentsManagementExtPanel createDocumentsPanel() {
        if (documentPanel == null) {
            if (applicationBean != null) {
                documentPanel = new DocumentsManagementExtPanel(
                        applicationBean.getSourceList(), null, applicationBean.isEditingAllowed());
            } else {
                documentPanel = new DocumentsManagementExtPanel();
            }
        }
        return documentPanel;
    }

    private ApplicationBean getApplicationBean() {
        if (applicationBean == null) {
            applicationBean = new ApplicationBean();
        }
        return applicationBean;
    }

    private void showSiteInspectionForm() {

            SolaTask t = new SolaTask<Void, Void>() {
                @Override
                protected Void doTask() {
                    setMessage("Generating report");
                    ApplicationBean appBean = new ApplicationBean();

                    ReportViewerForm form = new ReportViewerForm(
                            ReportManager.getSiteInspectionReport(appBean, null));
                    form.setVisible(true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        
    }
    
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
    
    private void save(){
        // Save site inspection
        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                /*DefaultFormatterFactory df = new DefaultFormatterFactory();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Timestamp timestamp = Timestamp.valueOf(inspectionFormattedTextField.getText());
                System.out.println(timestamp.toString());
                System.out.println(inspectionFormattedTextField.getText());
                try {
                    if(inspectionCheckBox.isSelected()){
                        applicationBean.setInspectionCompleted(true);
                    }
                    applicationBean.setExpectedCompletionDate(df.parse(inspectionFormattedTextField.getText()));
                } catch (ParseException e) {
                    System.out.println(e);
                }*/
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        applicationBean = getApplicationBean();
        btnShowCalendarFrom = new javax.swing.JButton();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar = new javax.swing.JToolBar();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        btnShowCalendarFrom1 = new javax.swing.JButton();
        inspectionFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        inspectionCheckBox = new javax.swing.JCheckBox();
        createDocumentsPanel();
        documentPanel = new org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel();

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);

        setHeaderPanel(headerPanel);
        setName("Form"); // NOI18N

        headerPanel.setName(""); // NOI18N
        headerPanel.setTitleText("Site Inspection");

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        btnPrint.setText("Print New Form");
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar.add(btnPrint);

        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar.add(btnSave);

        btnShowCalendarFrom1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom1.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom1.setBorder(null);
        btnShowCalendarFrom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFrom1ActionPerformed(evt);
            }
        });

        inspectionFormattedTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationBean, org.jdesktop.beansbinding.ELProperty.create("${expectedInspectionDate}"), inspectionFormattedTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        inspectionFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inspectionFormattedTextFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Expected Inspection Date");

        inspectionCheckBox.setText("Inspection Completed");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationBean, org.jdesktop.beansbinding.ELProperty.create("${inspectionCompleted}"), inspectionCheckBox, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        inspectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inspectionCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inspectionFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarFrom1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inspectionCheckBox)
                .addGap(14, 14, 14))
            .addComponent(documentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inspectionCheckBox)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inspectionFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(btnShowCalendarFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        showSiteInspectionForm();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnShowCalendarFrom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFrom1ActionPerformed
        showCalendar(inspectionFormattedTextField);
    }//GEN-LAST:event_btnShowCalendarFrom1ActionPerformed

    private void inspectionFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inspectionFormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inspectionFormattedTextFieldActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void inspectionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inspectionCheckBoxActionPerformed

    }//GEN-LAST:event_inspectionCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationBean applicationBean;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarFrom1;
    private org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel documentPanel;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JCheckBox inspectionCheckBox;
    private javax.swing.JFormattedTextField inspectionFormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar jToolBar;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
