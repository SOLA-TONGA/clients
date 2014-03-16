/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.swing.ui.source;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultRowSorter;
import javax.swing.JFormattedTextField;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.source.PowerOfAttorneyBean;
import org.sola.clients.beans.source.PowerOfAttorneySearchResultBean;
import org.sola.clients.beans.source.PowerOfAttorneySearchResultListBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.controls.WatermarkDate;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.common.utils.InternalNumberComparator;
import org.sola.clients.swing.ui.renderers.AttachedDocumentCellRenderer;
import org.sola.clients.swing.common.utils.FormattersFactory;
import org.sola.clients.swing.ui.renderers.DateTimeRenderer;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to search Power of attorney.
 */
public class PowerOfAttorneySearchPanel extends javax.swing.JPanel {

    public static final String SELECT_POWER_OF_ATTORNEY = "selectedPowerOfAttorney";
    public static final String SELECTED_POWER_OF_ATTORNEY_SEARCH_RESULT = "selectedPowerOfAttorneySearchResult";
    public static final String OPEN_APPLICATION = "openApplication";
    public static final String VIEW_POWER_OF_ATTORNEY = "viewPowerOfAttorney";
    
    /**
     * Default form constructor.
     */
    public PowerOfAttorneySearchPanel() {
        initComponents();
        customizeButtons();
        
        InternalNumberComparator comp = new InternalNumberComparator();
        DefaultRowSorter rowSorter= (DefaultRowSorter) this.tableSearchResults.getRowSorter();
        rowSorter.setComparator(1, comp);

        
        
        powerOfAttorneySearchResults.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PowerOfAttorneySearchResultListBean.SELECTED_POWER_OF_ATTORNEY_PROPERTY)) {
                    firePropertyChange(SELECTED_POWER_OF_ATTORNEY_SEARCH_RESULT, null, 
                            powerOfAttorneySearchResults.getSelectedPowerOfAttorney());
                    customizeButtons();
                }
            }
        });
    }

    public boolean isShowSelectButton() {
        return btnSelect.isVisible();
    }

    public void setShowSelectButton(boolean showSelectButton) {
        btnSelect.setVisible(showSelectButton);
        menuSelect.setVisible(showSelectButton);
    }
    
    public boolean isShowOpenApplicationButton() {
        return btnSelect.isVisible();
    }

    public void setShowOpenApplicationButton(boolean show) {
        btnOpenApplication.setVisible(show);
        menuOpenApplication.setVisible(show);
    }
    
    public boolean isShowViewButton() {
        return btnView.isVisible();
    }

    public void setShowViewButton(boolean show) {
        btnView.setVisible(show);
        menuView.setVisible(show);
    }
    
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
    
    public PowerOfAttorneySearchResultBean getSelectedPowerOfAttorney() {
        return powerOfAttorneySearchResults.getSelectedPowerOfAttorney();
    }
    
    private void clearForm() {
        txtAttorneyName.setText(null);
        txtPersonName.setText(null);
        txtNumber.setText(null);
        txtRefNumber.setText(null);
        txtSubmissionDateFrom.setValue(null);
        txtSubmissionDateTo.setValue(null);
    }
    
    /**
     * Enables or disables printing button.
     */
    private void customizeButtons() {
        boolean selected = powerOfAttorneySearchResults.getSelectedPowerOfAttorney() != null;
        boolean enabled = false;
        if (selected
                && powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getArchiveDocumentId() != null
                && !powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getArchiveDocumentId().isEmpty()) {
            enabled = true;
        }
        btnOpenAttachment.setEnabled(enabled);
        menuOpenAttachment.setEnabled(enabled);
        btnSelect.setEnabled(selected);
        menuSelect.setEnabled(selected);
        btnView.setEnabled(selected);
        menuView.setEnabled(selected);
        if(selected && powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getTransactionId()!=null){
            btnOpenApplication.setEnabled(true);
        } else {
            btnOpenApplication.setEnabled(false);
        }
        menuOpenApplication.setEnabled(btnOpenApplication.isEnabled());
    }
    
    private void openDocument() {
        if (powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getArchiveDocumentId() == null
                || powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getArchiveDocumentId().isEmpty()) {
            return;
        }

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_DOCUMENT_OPENING));
                SourceBean selectedSource = SourceBean.getSource(powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getId());
                if (selectedSource != null && selectedSource.getArchiveDocument() != null) {
                    DocumentBean.openDocument(selectedSource.getArchiveDocument().getId(),
                            selectedSource.getArchiveDocument().getFileName());
                } else {
                    throw new SOLAException(ClientMessage.SOURCE_NO_DOCUMENT);
                }
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    private void firePowerOfAttorneyEvent(final String evtName){
        if (powerOfAttorneySearchResults.getSelectedPowerOfAttorney() == null) {
            return;
        }
        
        SolaTask t = new SolaTask<Void, Void>() {

            PowerOfAttorneyBean powerOfAttorney;

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_DOCUMENT_GETTING));
                powerOfAttorney = PowerOfAttorneyBean.getPowerOfAttorney(
                    powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getId());
                return null;
            }

            @Override
            protected void taskDone() {
                if (powerOfAttorney == null) {
                    MessageUtility.displayMessage(ClientMessage.SOURCE_NOT_FOUND);
                } else {
                    firePropertyChange(evtName, null, powerOfAttorney);
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    private void fireViewPowerOfAttorney() {
        firePowerOfAttorneyEvent(VIEW_POWER_OF_ATTORNEY);
    }
    
    private void fireSelect() {
        firePowerOfAttorneyEvent(SELECT_POWER_OF_ATTORNEY);
    }
    
    private void fireOpenApplication() {
        if (powerOfAttorneySearchResults.getSelectedPowerOfAttorney() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                ApplicationBean app;

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_GETTING));
                    app = ApplicationBean.getApplicationByTransactionId(
                            powerOfAttorneySearchResults.getSelectedPowerOfAttorney().getTransactionId());
                    return null;
                }

                @Override
                protected void taskDone() {
                    if (app == null) {
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_NOT_FOUND);
                    } else {
                        firePropertyChange(OPEN_APPLICATION, null, app);
                    }
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void searchDocuments() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_POWER_OF_ATTORNEY_SEARCHING));
                powerOfAttorneySearchResults.search(searchParams);
                return null;
            }

            @Override
            public void taskDone() {
                if (powerOfAttorneySearchResults.getPowerOfAttorneySearchResultsList().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                } else if (powerOfAttorneySearchResults.getPowerOfAttorneySearchResultsList().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                }
                lblSearchResultCount.setText(String.format("(%s)", 
                        powerOfAttorneySearchResults.getPowerOfAttorneySearchResultsList().size()));
            }
        };
        TaskManager.getInstance().runTask(t);
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        powerOfAttorneySearchResults = new org.sola.clients.beans.source.PowerOfAttorneySearchResultListBean();
        popUpPowerOfAttorneySearchResults = new javax.swing.JPopupMenu();
        menuView = new javax.swing.JMenuItem();
        menuOpenAttachment = new javax.swing.JMenuItem();
        menuOpenApplication = new javax.swing.JMenuItem();
        menuSelect = new javax.swing.JMenuItem();
        searchParams = new org.sola.clients.beans.source.PowerOfAttorneySearchParamsBean();
        jToolBar1 = new javax.swing.JToolBar();
        btnSearch = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnView = new javax.swing.JButton();
        btnOpenAttachment = new javax.swing.JButton();
        btnOpenApplication = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel6 = new javax.swing.JLabel();
        lblSearchResultCount = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSearchResults = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtRefNumber = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtSubmissionDateFrom = new WatermarkDate();
        btnSubmissionDateFrom = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSubmissionDateTo = new WatermarkDate();
        btnSubmissionDateTo = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtPersonName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAttorneyName = new javax.swing.JTextField();

        menuView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/source/Bundle"); // NOI18N
        menuView.setText(bundle.getString("PowerOfAttorneySearchPanel.menuView.text")); // NOI18N
        menuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuViewActionPerformed(evt);
            }
        });
        popUpPowerOfAttorneySearchResults.add(menuView);

        menuOpenAttachment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        menuOpenAttachment.setText(bundle.getString("PowerOfAttorneySearchPanel.menuOpenAttachment.text")); // NOI18N
        menuOpenAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenAttachmentActionPerformed(evt);
            }
        });
        popUpPowerOfAttorneySearchResults.add(menuOpenAttachment);

        menuOpenApplication.setText(bundle.getString("PowerOfAttorneySearchPanel.menuOpenApplication.text")); // NOI18N
        menuOpenApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenApplicationActionPerformed(evt);
            }
        });
        popUpPowerOfAttorneySearchResults.add(menuOpenApplication);

        menuSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/select.png"))); // NOI18N
        menuSelect.setText(bundle.getString("PowerOfAttorneySearchPanel.menuSelect.text")); // NOI18N
        menuSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSelectActionPerformed(evt);
            }
        });
        popUpPowerOfAttorneySearchResults.add(menuSelect);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSearch.setText(bundle.getString("PowerOfAttorneySearchPanel.btnSearch.text")); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSearch);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClear.setText(bundle.getString("PowerOfAttorneySearchPanell.btnClear.text")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClear);
        jToolBar1.add(jSeparator3);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnView.setText(bundle.getString("PowerOfAttorneySearchPanel.btnView.text")); // NOI18N
        btnView.setFocusable(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnView);

        btnOpenAttachment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenAttachment.setText(bundle.getString("PowerOfAttorneySearchPanel.btnOpenAttachment.text")); // NOI18N
        btnOpenAttachment.setFocusable(false);
        btnOpenAttachment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenAttachmentActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenAttachment);

        btnOpenApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document-text.png"))); // NOI18N
        btnOpenApplication.setText(bundle.getString("PowerOfAttorneySearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenApplication.setFocusable(false);
        btnOpenApplication.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenApplicationActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenApplication);

        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/select.png"))); // NOI18N
        btnSelect.setText(bundle.getString("PowerOfAttorneySearchPanel.btnSelect.text")); // NOI18N
        btnSelect.setFocusable(false);
        btnSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSelect);
        jToolBar1.add(jSeparator1);

        jLabel6.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel6.text")); // NOI18N
        jToolBar1.add(jLabel6);

        lblSearchResultCount.setText(bundle.getString("PowerOfAttorneySearchPanel.lblSearchResultCount.text")); // NOI18N
        jToolBar1.add(lblSearchResultCount);

        tableSearchResults.setComponentPopupMenu(popUpPowerOfAttorneySearchResults);
        tableSearchResults.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${powerOfAttorneySearchResultsList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, powerOfAttorneySearchResults, eLProperty, tableSearchResults);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${referenceNr}"));
        columnBinding.setColumnName("Reference Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${laNr}"));
        columnBinding.setColumnName("La Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${submission}"));
        columnBinding.setColumnName("Submission");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${personName}"));
        columnBinding.setColumnName("Person Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${attorneyName}"));
        columnBinding.setColumnName("Attorney Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${recordation}"));
        columnBinding.setColumnName("Recordation");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${statusDisplayValue}"));
        columnBinding.setColumnName("Status Display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${archiveDocumentId}"));
        columnBinding.setColumnName("Archive Document Id");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, powerOfAttorneySearchResults, org.jdesktop.beansbinding.ELProperty.create("${selectedPowerOfAttorney}"), tableSearchResults, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tableSearchResults);
        tableSearchResults.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title1_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title0_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title2_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(2).setCellRenderer(new DateTimeRenderer());
        tableSearchResults.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title3_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title4")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title7")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(5).setCellRenderer(new DateTimeRenderer());
        tableSearchResults.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title6")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(7).setPreferredWidth(30);
        tableSearchResults.getColumnModel().getColumn(7).setMaxWidth(30);
        tableSearchResults.getColumnModel().getColumn(7).setHeaderValue(bundle.getString("PowerOfAttorneySearchPanel.tableSearchResults.columnModel.title5")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(7).setCellRenderer(new AttachedDocumentCellRenderer());

        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel9.setLayout(new java.awt.GridLayout(2, 1, 12, 0));

        jPanel10.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jLabel1.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel1.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${refNumber}"), txtRefNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRefNumber)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRefNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel1);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${laNumber}"), txtNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel7.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNumber)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel2);

        jPanel9.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        txtSubmissionDateFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${fromSubmissionDate}"), txtSubmissionDateFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnSubmissionDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubmissionDateFrom.setText(bundle.getString("DocumentSearchPanel.btnSubmissionDateFrom.text")); // NOI18N
        btnSubmissionDateFrom.setBorder(null);
        btnSubmissionDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmissionDateFromActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(txtSubmissionDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubmissionDateFrom))
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSubmissionDateFrom)
                    .addComponent(txtSubmissionDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel8);

        jLabel5.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel5.text")); // NOI18N

        txtSubmissionDateTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtSubmissionDateTo.setText(bundle.getString("PowerOfAttorneySearchPanel.txtSubmissionDateTo.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${toSubmissionDate}"), txtSubmissionDateTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnSubmissionDateTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubmissionDateTo.setText(bundle.getString("DocumentSearchPanel.btnSubmissionDateTo.text")); // NOI18N
        btnSubmissionDateTo.setBorder(null);
        btnSubmissionDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmissionDateToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(txtSubmissionDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubmissionDateTo))
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSubmissionDateTo)
                    .addComponent(txtSubmissionDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel7);

        jPanel9.add(jPanel11);

        jPanel3.add(jPanel9);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jLabel4.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel4.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${personName}"), txtPersonName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPersonName)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPersonName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6);

        jLabel3.setText(bundle.getString("PowerOfAttorneySearchPanel.jLabel3.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${attorneyName}"), txtAttorneyName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAttorneyName)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAttorneyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5);

        jPanel3.add(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmissionDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmissionDateToActionPerformed
        showCalendar(txtSubmissionDateTo);
    }//GEN-LAST:event_btnSubmissionDateToActionPerformed

    private void btnSubmissionDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmissionDateFromActionPerformed
        showCalendar(txtSubmissionDateFrom);
    }//GEN-LAST:event_btnSubmissionDateFromActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchDocuments();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnOpenAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenAttachmentActionPerformed
        openDocument();
    }//GEN-LAST:event_btnOpenAttachmentActionPerformed

    private void menuOpenAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenAttachmentActionPerformed
        openDocument();
    }//GEN-LAST:event_menuOpenAttachmentActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        fireSelect();
    }//GEN-LAST:event_btnSelectActionPerformed

    private void btnOpenApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenApplicationActionPerformed
        fireOpenApplication();
    }//GEN-LAST:event_btnOpenApplicationActionPerformed

    private void menuOpenApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenApplicationActionPerformed
        fireOpenApplication();
    }//GEN-LAST:event_menuOpenApplicationActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        fireViewPowerOfAttorney();
    }//GEN-LAST:event_btnViewActionPerformed

    private void menuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuViewActionPerformed
        fireViewPowerOfAttorney();
    }//GEN-LAST:event_menuViewActionPerformed

    private void menuSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSelectActionPerformed
        fireSelect();
    }//GEN-LAST:event_menuSelectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnOpenApplication;
    private javax.swing.JButton btnOpenAttachment;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelect;
    private javax.swing.JButton btnSubmissionDateFrom;
    private javax.swing.JButton btnSubmissionDateTo;
    private javax.swing.JButton btnView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblSearchResultCount;
    private javax.swing.JMenuItem menuOpenApplication;
    private javax.swing.JMenuItem menuOpenAttachment;
    private javax.swing.JMenuItem menuSelect;
    private javax.swing.JMenuItem menuView;
    private javax.swing.JPopupMenu popUpPowerOfAttorneySearchResults;
    private org.sola.clients.beans.source.PowerOfAttorneySearchResultListBean powerOfAttorneySearchResults;
    private org.sola.clients.beans.source.PowerOfAttorneySearchParamsBean searchParams;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableSearchResults;
    private javax.swing.JTextField txtAttorneyName;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtPersonName;
    private javax.swing.JTextField txtRefNumber;
    private javax.swing.JFormattedTextField txtSubmissionDateFrom;
    private javax.swing.JFormattedTextField txtSubmissionDateTo;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
