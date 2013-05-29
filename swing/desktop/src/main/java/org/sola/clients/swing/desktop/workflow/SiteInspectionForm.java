/*
 * Copyright 2013 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.swing.desktop.workflow;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.source.FileBrowserForm;

/**
 *
 * @author Admin
 */
public class SiteInspectionForm extends ContentPanel {

    private ApplicationBean applicationBean;
    private ApplicationServiceBean applicationService;
    private SourceBean document;
        
    /**
     * Creates new form SiteInspectionForm
     */
    public SiteInspectionForm() {
        initComponents();
    }
    
    public SiteInspectionForm(ApplicationBean applicationBean, ApplicationServiceBean applicationService){
        this.applicationBean = applicationBean;
        this.applicationService = applicationService;
        initComponents();
        customizeForm();
    }
    
    private void customizeForm() {
        if (applicationService != null && applicationBean != null) {
            headerPanel.setTitleText(String.format("%s for Application: #%s",
                    applicationService.getRequestType().getDisplayValue(),
                    applicationBean.getNr()));
        }
    }
    
    public void addSiteInspection(){
        FileBrowserForm fileBrowser = new FileBrowserForm(null, true, FileBrowserForm.AttachAction.CLOSE_WINDOW);
        fileBrowser.setLocationRelativeTo(this);
        fileBrowser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(FileBrowserForm.ATTACHED_DOCUMENT)) {
                    if (e.getNewValue() != null) {
                        DocumentBean document = (DocumentBean) e.getNewValue();
                        getSiteInspectionDocument().setArchiveDocument(document);
                    }
                }
            }
        });
        fileBrowser.setVisible(true);
    }
    
    public SourceBean getSiteInspectionDocument() {
        if (document == null) {
            document = new SourceBean();
        }
        return document;
    }
    
    public void openSiteInspection(){
        if (document != null){ 
            DocumentBean.openDocument(getSiteInspectionDocument().getArchiveDocument().getId(),
                        getSiteInspectionDocument().getArchiveDocument().getFileName());
        }       
    }
   
    private void showSiteInspectionForm(){
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

     /** Adds new source into the list. 
    private void addDocument() {
        if (addDocumentForm != null) {
            addDocumentForm.dispose();
        }

        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                attachDocument(e);
            }
        };

        addDocumentForm = new AddDocumentForm(applicationBean, null, true);
        addDocumentForm.setLocationRelativeTo(this);
        addDocumentForm.addPropertyChangeListener(SourceListBean.SELECTED_SOURCE_PROPERTY, listener);
        addDocumentForm.allowAddingOfNewDocuments(allowAddingOfNewDocuments);
        addDocumentForm.setVisible(true);
        addDocumentForm.removePropertyChangeListener(SourceListBean.SELECTED_SOURCE_PROPERTY, listener);
    }
    
    public boolean saveDocument() {
        if (validateDocument(true)) {
            
           if (!(this.archiveDocument==null)){ 
            if (!this.archiveDocument.getId().equals("")) {
                getDocument().setArchiveDocument(this.archiveDocument);
            }
           } 
            getDocument().save();
            fireDocumentChangeEvent();
            return true;
        } else {
            return false;
        }
    }
    
    private void viewDocument(){
        firePropertyChange(VIEW_DOCUMENT, null, documentsPanel.getSourceListBean().getSelectedSource());
    }
    
        ** Attach file to the selected source. 
    private void attachDocument(PropertyChangeEvent e) {
        SourceBean document = null;
        if (e.getPropertyName().equals(AddDocumentForm.SELECTED_SOURCE)
                && e.getNewValue() != null) {
            document = (SourceBean) e.getNewValue();
            documentsPanel.addDocument(document);
        }
    }
    
     /**
     Opens attached digital copy of document in the document's list.
     
    public void viewAttachment() {
        if (sourceListBean.getSelectedSource() != null
                && sourceListBean.getSelectedSource().getArchiveDocument() != null) {
            // Try to open attached file
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_DOCUMENT_OPENING));
                    DocumentBean.openDocument(sourceListBean.getSelectedSource().getArchiveDocument().getId(),
                            sourceListBean.getSelectedSource().getArchiveDocument().getFileName());
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    
    Removes selected document.
     
    public void removeSelectedDocument() {
        if (sourceListBean.getSelectedSource() != null) {
            if (MessageUtility.displayMessage(ClientMessage.CONFIRM_REMOVE_RECORD)
                    == MessageUtility.BUTTON_ONE) {
                sourceListBean.safeRemoveSelectedSource();
            }
        }
    }

   
    public void addDocument(SourceBean document) {
        sourceListBean.getSourceBeanList().addAsNew(document);
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applicationBean1 = new org.sola.clients.beans.application.ApplicationBean();
        btnShowCalendarFrom = new javax.swing.JButton();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar = new javax.swing.JToolBar();
        btnAdd = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        btnShowCalendarFrom1 = new javax.swing.JButton();
        inspectionFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        inspectionCheckBox = new javax.swing.JCheckBox();

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);

        setName("Form"); // NOI18N

        headerPanel.setName(""); // NOI18N
        headerPanel.setTitleText("Site Inspection");

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar.add(btnAdd);

        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        jToolBar.add(btnOpen);

        btnPrint.setText("Print New Form");
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar.add(btnPrint);

        jTableWithDefaultStyles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableWithDefaultStyles);

        btnShowCalendarFrom1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom1.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom1.setBorder(null);
        btnShowCalendarFrom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFrom1ActionPerformed(evt);
            }
        });

        inspectionFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inspectionFormattedTextFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Expected Inspection Date");

        inspectionCheckBox.setText("Inspection Completed");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inspectionFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarFrom1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inspectionCheckBox))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inspectionFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(btnShowCalendarFrom1)
                    .addComponent(inspectionCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        openSiteInspection();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addSiteInspection();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        showSiteInspectionForm();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnShowCalendarFrom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFrom1ActionPerformed
        showCalendar(inspectionFormattedTextField);
    }//GEN-LAST:event_btnShowCalendarFrom1ActionPerformed

    private void inspectionFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inspectionFormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inspectionFormattedTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationBean applicationBean1;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAdd;
    private org.sola.clients.swing.common.buttons.BtnOpen btnOpen;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarFrom1;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JCheckBox inspectionCheckBox;
    private javax.swing.JFormattedTextField inspectionFormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
}
