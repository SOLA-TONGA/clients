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
import javax.swing.JOptionPane;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.reports.ReportManager;
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
        
        //System.out.println(date);
            String str = JOptionPane.showInputDialog(null, "Enter Date : ", "Expected Inspection Date", 1);
            Date date = null;
            try {
                date = new SimpleDateFormat("dd/mm/yyyy").parse(str);
            } catch (ParseException ex) {
                Logger.getLogger(SiteInspectionForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            applicationBean.setExpectedCompletionDate(date);
            //System.out.println(date); // Sat Jan 02 00:00:00 BOT 2010
            if (str != null){
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
    }
    
    public void showInputDialog(){
        
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
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar = new javax.swing.JToolBar();
        btnAdd = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationBean applicationBean1;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAdd;
    private org.sola.clients.swing.common.buttons.BtnOpen btnOpen;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
}
