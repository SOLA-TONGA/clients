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
import org.sola.clients.beans.validation.Localized;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class ChecklistForm extends ContentPanel {

    private ApplicationBean applicationBean;
    private ApplicationServiceBean applicationService;
    private boolean readOnly = false;

    /**
     * Creates new form ChecklistForm
     */
    public ChecklistForm() {
        initComponents();
    }

    public ChecklistForm(ApplicationBean applicationBean, ApplicationServiceBean applicationService,
            boolean readOnly) {
        this.applicationBean = applicationBean;
        this.applicationService = applicationService;
        this.readOnly = readOnly;
        initComponents();
        customizeForm();
        serviceChecklistItemListBean.loadList(this.applicationService.getId());
    }

    private void customizeForm() {
        if (applicationService != null && applicationBean != null) {
            headerPanel.setTitleText(String.format("Checklist for Application: #%s",
                    applicationBean.getNr()));
        }
        // Disable the edit buttons if the form is in read only mode
        btnSave.setEnabled(!readOnly);
        cbxChecklistGroup.setEnabled(!readOnly);
        btnSelect.setEnabled(!readOnly);
        checklistTable.setEnabled(!readOnly);
    }

    private void saveChecklist() {
        // Make sure any user edits in the table are accepted
        if (this.checklistTable.getCellEditor() != null) {
            this.checklistTable.getCellEditor().stopCellEditing();
        }
<<<<<<< HEAD
        // Save the checklist items
        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                serviceChecklistItemListBean.saveList();
                return null;
            }
            @Override
            public void taskDone() {
                    //MessageUtility.displayMessage(ClientMessage.APPLICATION_SUCCESSFULLY_SAVED);  
            }
        };
        TaskManager.getInstance().runTask(t);
=======
        if (serviceChecklistItemListBean.validate(true).size() < 1) {
            // Save the checklist items
            SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                    serviceChecklistItemListBean.validate(true);
                    serviceChecklistItemListBean.saveList();
                    return null;
                }

                @Override
                public void taskDone() {
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_SUCCESSFULLY_SAVED);
                }
            };
            TaskManager.getInstance().runTask(t);
        }
>>>>>>> ed875472b758c8918377e130faec91b64458de19
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

        checklistGroupListBean = new org.sola.clients.beans.referencedata.ChecklistGroupListBean();
        serviceChecklistItemListBean = new org.sola.clients.beans.application.ServiceChecklistItemListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        leaseHoldLabel = new javax.swing.JLabel();
        cbxChecklistGroup = new javax.swing.JComboBox();
        checklistPanel = new javax.swing.JScrollPane();
        checklistTable = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        checklistToolBar = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        jToolBar1 = new javax.swing.JToolBar();
        btnSelect = new javax.swing.JButton();

        setHeaderPanel(headerPanel);
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(683, 450));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/workflow/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("ChecklistForm.headerPanel.titleText")); // NOI18N

        leaseHoldLabel.setText(bundle.getString("ChecklistForm.leaseHoldLabel.text")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${checklistGroupList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checklistGroupListBean, eLProperty, cbxChecklistGroup, "a");
        bindingGroup.addBinding(jComboBoxBinding);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checklistGroupListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedChecklistGroup}"), cbxChecklistGroup, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        checklistTable.setColumnSelectionAllowed(true);
        checklistTable.setRowHeight(20);
        checklistTable.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${serviceChecklistItemList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, serviceChecklistItemListBean, eLProperty, checklistTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${checklistItemDisplayValue}"));
        columnBinding.setColumnName("Checklist Item Display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${checklistItemDescription}"));
        columnBinding.setColumnName("Checklist Item Description");
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${complies}"));
        columnBinding.setColumnName("Complies");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${comment}"));
        columnBinding.setColumnName("Comment");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        checklistPanel.setViewportView(checklistTable);
        checklistTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        checklistTable.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ChecklistForm.checklistTable.columnModel.title0")); // NOI18N
        checklistTable.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ChecklistForm.checklistTable.columnModel.title3")); // NOI18N
        checklistTable.getColumnModel().getColumn(2).setMinWidth(40);
        checklistTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        checklistTable.getColumnModel().getColumn(2).setMaxWidth(100);
        checklistTable.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ChecklistForm.checklistTable.columnModel.title1")); // NOI18N
        checklistTable.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("ChecklistForm.checklistTable.columnModel.title2")); // NOI18N

        checklistToolBar.setFloatable(false);
        checklistToolBar.setRollover(true);

        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        checklistToolBar.add(btnSave);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnSelect.setText(bundle.getString("ChecklistForm.btnSelect.text")); // NOI18N
        btnSelect.setFocusable(false);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSelect);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checklistToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checklistPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbxChecklistGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(leaseHoldLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checklistToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leaseHoldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxChecklistGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checklistPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        MessageUtility.displayMessage(ClientMessage.APPLICATION_SUCCESSFULLY_SAVED);
        saveChecklist();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        serviceChecklistItemListBean.loadList(checklistGroupListBean.getSelectedChecklistGroup());
        saveChecklist();
    }//GEN-LAST:event_btnSelectActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private javax.swing.JButton btnSelect;
    private javax.swing.JComboBox cbxChecklistGroup;
    private org.sola.clients.beans.referencedata.ChecklistGroupListBean checklistGroupListBean;
    private javax.swing.JScrollPane checklistPanel;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles checklistTable;
    private javax.swing.JToolBar checklistToolBar;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel leaseHoldLabel;
    private org.sola.clients.beans.application.ServiceChecklistItemListBean serviceChecklistItemListBean;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
