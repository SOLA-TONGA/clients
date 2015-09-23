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
package org.sola.clients.swing.desktop.minister;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import org.sola.clients.beans.minister.MinisterInwardBean;
import org.sola.clients.beans.minister.MinisterInwardSearchParamsBean;
import org.sola.clients.beans.minister.MinisterInwardSearchResultListBean;
import org.sola.clients.beans.minister.MinisterLeaseSearchResultListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class MinisterSearchPanel extends ContentPanel {

    /**
     * Creates new form MinisterSearchPanel
     */
    public MinisterSearchPanel() {
        initComponents();
        ministerInwardSearchResultListBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(MinisterInwardSearchResultListBean.SELECTED_MINISTER_INWARD_SEARCH_RESULT_PROPERTY)) {
                    customizeButtons();
                }
            }
        });
        ministerLeaseSearchResultListBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(MinisterLeaseSearchResultListBean.SELECTED_MINISTER_LEASE_SEARCH_RESULT_PROPERTY)) {
                    customizeButtons();
                }
            }
        });
        
        customizeButtons();
    }
    
    private void customizeButtons() {
        boolean hasEditRole = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        boolean inwardEnabled = ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null;
        boolean leaseEnabled = ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null;
        inwardEnabled = inwardEnabled && hasEditRole;
        leaseEnabled = leaseEnabled && hasEditRole;
        if (inwardEnabled && ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null) {
            inwardEnabled = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        }
        if (leaseEnabled && ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null) {
            leaseEnabled = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        }
        btnAdd.setEnabled(hasEditRole);
        btnEdit.setEnabled(inwardEnabled);
        btnRemove.setEnabled(inwardEnabled);
        btnOpenItem.setEnabled(inwardEnabled);
        btnAdd1.setEnabled(hasEditRole);
        btnEdit1.setEnabled(leaseEnabled);
        btnRemove1.setEnabled(leaseEnabled);
        btnOpenItem1.setEnabled(leaseEnabled);

    }
     
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
    
    private void executeSearch(final MinisterInwardSearchParamsBean params,
            final JLabel lblSearchCount, final MinisterInwardSearchResultListBean results) {

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                results.search(params);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchCount.setText(Integer.toString(results.getMinisterInwardResultList().size()));
                if (results.getMinisterInwardResultList().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (results.getMinisterInwardResultList().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    public void addMinisterInward() {

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_FORM));
                MinisterInwardForm form = new MinisterInwardForm();
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_FORM, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }
    
    public void openMinisterInward() {
        if (ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_FORM));
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()));
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void removeMinisterInward() {

        if (ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null) {
            int button = MessageUtility.displayMessage(ClientMessage.MINISTER_REMOVE_INWARD,
                    new Object [] {ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getFromWhom()});
            if (button == MessageUtility.BUTTON_ONE) {
                SolaTask t = new SolaTask<Void, Void>() {

                    @Override
                    public Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_FORM));
                        MinisterInwardBean.removeMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId());
                        return null;
                    }

                    @Override
                    public void taskDone() {
                        // Rerun the search to show that the inward is removed. 
                        executeSearch(ministerInwardSearchParamsBean, lblSearchCount, ministerInwardSearchResultListBean);
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }

    }

    public void editMinisterInward() {
        if (ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_FORM));
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()));
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void clearMinisterInward() {
        nameInwardTextField.setText(null);
        fileNumberTextField.setText(null);
        subjectTextField.setText(null);
        dateInTextField.setText(null);
        dateOutTextField.setText(null);
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

        ministerInwardSearchResultListBean = new org.sola.clients.beans.minister.MinisterInwardSearchResultListBean();
        ministerInwardSearchParamsBean = new org.sola.clients.beans.minister.MinisterInwardSearchParamsBean();
        ministerLeaseSearchParamsBean = new org.sola.clients.beans.minister.MinisterLeaseSearchParamsBean();
        ministerLeaseSearchResultListBean = new org.sola.clients.beans.minister.MinisterLeaseSearchResultListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        ministerTab = new javax.swing.JTabbedPane();
        inwardPanel = new javax.swing.JPanel();
        jToolBar = new javax.swing.JToolBar();
        btnFind = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnOpenItem = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnAdd = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEdit = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemove = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        lblSearchResult = new javax.swing.JLabel();
        lblSearchCount = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        dateOutTextField = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        dateInTextField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        subjectTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        nameInwardTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fileNumberTextField = new javax.swing.JTextField();
        btnDateIn = new javax.swing.JButton();
        btnDateOut = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableInward = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        leasePanel = new javax.swing.JPanel();
        lblDateReceivedFrom = new javax.swing.JLabel();
        receivedFromTextField = new javax.swing.JFormattedTextField();
        lblDateReceivedTo = new javax.swing.JLabel();
        receivedToTextField = new javax.swing.JFormattedTextField();
        jToolBar1 = new javax.swing.JToolBar();
        btnFind1 = new javax.swing.JButton();
        btnClear1 = new javax.swing.JButton();
        btnOpenItem1 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnAdd1 = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEdit1 = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemove1 = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        lblSearchResult1 = new javax.swing.JLabel();
        lblSearchCount1 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        nameLeaseTextField = new javax.swing.JTextField();
        locationTextField = new javax.swing.JTextField();
        lblLocation = new javax.swing.JLabel();
        payDateTextField = new javax.swing.JFormattedTextField();
        lblPayDate = new javax.swing.JLabel();
        receiptNumberTextField = new javax.swing.JTextField();
        lblReceiptNumber = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableLease = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        btnPayDate = new javax.swing.JButton();
        btnDateReceivedTo = new javax.swing.JButton();
        btnDateReceivedFrom = new javax.swing.JButton();
        appPanel = new javax.swing.JPanel();

        setHeaderPanel(headerPanel);

        headerPanel.setTitleText("Minister's Office");

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        btnFind.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        jToolBar.add(btnFind);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClear.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jToolBar.add(btnClear);

        btnOpenItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenItem.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenItem.setEnabled(false);
        btnOpenItem.setFocusable(false);
        btnOpenItem.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenItemActionPerformed(evt);
            }
        });
        jToolBar.add(btnOpenItem);
        jToolBar.add(jSeparator3);

        btnAdd.setEnabled(false);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar.add(btnAdd);

        btnEdit.setEnabled(false);
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar.add(btnEdit);

        btnRemove.setEnabled(false);
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jToolBar.add(btnRemove);
        jToolBar.add(jSeparator1);

        lblSearchResult.setText("Search results: ");
        jToolBar.add(lblSearchResult);
        jToolBar.add(lblSearchCount);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateOut}"), dateOutTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel5.setText("Date Out");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateIn}"), dateInTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel4.setText("Date In");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${subject}"), subjectTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel1.setText("Subject");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${fromWhom}"), nameInwardTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel2.setText("Name");

        jLabel3.setText("File Number");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${fileNumber}"), fileNumberTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        btnDateIn.setIcon(new javax.swing.ImageIcon("D:\\source\\tonga\\code\\clients\\swing\\desktop\\src\\main\\resources\\images\\common\\calendar.png")); // NOI18N
        btnDateIn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateInActionPerformed(evt);
            }
        });

        btnDateOut.setIcon(new javax.swing.ImageIcon("D:\\source\\tonga\\code\\clients\\swing\\desktop\\src\\main\\resources\\images\\common\\calendar.png")); // NOI18N
        btnDateOut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectTextField))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nameInwardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateInTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(btnDateIn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateOutTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDateOut, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(233, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameInwardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(fileNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(subjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dateOutTextField)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(dateInTextField)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnDateIn)
                    .addComponent(btnDateOut))
                .addContainerGap())
        );

        jTableInward.setMaximumSize(new java.awt.Dimension(2147483647, 100000));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerInwardResultList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchResultListBean, eLProperty, jTableInward, "");
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fromWhom}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fileNumber}"));
        columnBinding.setColumnName("File Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${subject}"));
        columnBinding.setColumnName("Subject");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateIn}"));
        columnBinding.setColumnName("Date In");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateOut}"));
        columnBinding.setColumnName("Date Out");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ceoDirection}"));
        columnBinding.setColumnName("Ceo Direction");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${directedDivision}"));
        columnBinding.setColumnName("Directed Division");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${directedOfficer}"));
        columnBinding.setColumnName("Directed Officer");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ministerDirection}"));
        columnBinding.setColumnName("Minister Direction");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${remark}"));
        columnBinding.setColumnName("Remark");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterInwardSearchResult}"), jTableInward, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jTableInward);

        javax.swing.GroupLayout inwardPanelLayout = new javax.swing.GroupLayout(inwardPanel);
        inwardPanel.setLayout(inwardPanelLayout);
        inwardPanelLayout.setHorizontalGroup(
            inwardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
        );
        inwardPanelLayout.setVerticalGroup(
            inwardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inwardPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );

        ministerTab.addTab("Inward", inwardPanel);

        lblDateReceivedFrom.setText("Date Received (From)");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedFrom}"), receivedFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        lblDateReceivedTo.setText("Date Received (To)");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedTo}"), receivedToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnFind1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnFind1.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnFind1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFind1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFind1);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClear1.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClear1);

        btnOpenItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenItem1.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenItem1.setEnabled(false);
        btnOpenItem1.setFocusable(false);
        btnOpenItem1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenItem1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenItem1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenItem1);
        jToolBar1.add(jSeparator4);

        btnAdd1.setEnabled(false);
        btnAdd1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAdd1);

        btnEdit1.setEnabled(false);
        btnEdit1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit1);

        btnRemove1.setEnabled(false);
        btnRemove1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemove1);
        jToolBar1.add(jSeparator2);

        lblSearchResult1.setText("Search results: ");
        jToolBar1.add(lblSearchResult1);
        jToolBar1.add(lblSearchCount1);

        lblName.setText("Name");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${name}"), nameLeaseTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${location}"), locationTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblLocation.setText("Location");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDate}"), payDateTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        lblPayDate.setText("Pay Date");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"), receiptNumberTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblReceiptNumber.setText("Receipt Number");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerLeaseResultList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchResultListBean, eLProperty, jTableLease, "");
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(jTableLease);

        btnPayDate.setIcon(new javax.swing.ImageIcon("D:\\source\\tonga\\code\\clients\\swing\\desktop\\src\\main\\resources\\images\\common\\calendar.png")); // NOI18N
        btnPayDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPayDate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPayDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDateActionPerformed(evt);
            }
        });

        btnDateReceivedTo.setIcon(new javax.swing.ImageIcon("D:\\source\\tonga\\code\\clients\\swing\\desktop\\src\\main\\resources\\images\\common\\calendar.png")); // NOI18N
        btnDateReceivedTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateReceivedTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateReceivedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateReceivedToActionPerformed(evt);
            }
        });

        btnDateReceivedFrom.setIcon(new javax.swing.ImageIcon("D:\\source\\tonga\\code\\clients\\swing\\desktop\\src\\main\\resources\\images\\common\\calendar.png")); // NOI18N
        btnDateReceivedFrom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateReceivedFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateReceivedFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateReceivedFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leasePanelLayout = new javax.swing.GroupLayout(leasePanel);
        leasePanel.setLayout(leasePanelLayout);
        leasePanelLayout.setHorizontalGroup(
            leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(leasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leasePanelLayout.createSequentialGroup()
                        .addComponent(lblDateReceivedFrom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receivedFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnDateReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDateReceivedTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receivedToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDateReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leasePanelLayout.createSequentialGroup()
                            .addComponent(lblLocation)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(locationTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leasePanelLayout.createSequentialGroup()
                            .addComponent(lblName)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(nameLeaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(leasePanelLayout.createSequentialGroup()
                        .addComponent(lblReceiptNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(receiptNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPayDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(payDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(114, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        leasePanelLayout.setVerticalGroup(
            leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(nameLeaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocation)
                    .addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReceiptNumber)
                        .addComponent(receiptNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(payDateTextField)
                            .addComponent(btnPayDate))
                        .addComponent(lblPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(receivedToTextField)
                                .addComponent(lblDateReceivedTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(receivedFromTextField)
                            .addComponent(lblDateReceivedFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnDateReceivedTo))
                    .addComponent(btnDateReceivedFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ministerTab.addTab("Lease", leasePanel);

        javax.swing.GroupLayout appPanelLayout = new javax.swing.GroupLayout(appPanel);
        appPanel.setLayout(appPanelLayout);
        appPanelLayout.setHorizontalGroup(
            appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 695, Short.MAX_VALUE)
        );
        appPanelLayout.setVerticalGroup(
            appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        ministerTab.addTab("Land Application", appPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ministerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ministerTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        executeSearch(ministerInwardSearchParamsBean, lblSearchCount, ministerInwardSearchResultListBean);
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearMinisterInward();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnOpenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenItemActionPerformed
        openMinisterInward();
    }//GEN-LAST:event_btnOpenItemActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addMinisterInward();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editMinisterInward();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeMinisterInward();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnFind1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFind1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFind1ActionPerformed

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void btnOpenItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOpenItem1ActionPerformed

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdit1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEdit1ActionPerformed

    private void btnRemove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemove1ActionPerformed

    private void btnDateReceivedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateReceivedToActionPerformed
        showCalendar(receivedToTextField);
    }//GEN-LAST:event_btnDateReceivedToActionPerformed

    private void btnDateReceivedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateReceivedFromActionPerformed
        showCalendar(receivedFromTextField);
    }//GEN-LAST:event_btnDateReceivedFromActionPerformed

    private void btnPayDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDateActionPerformed
        showCalendar(payDateTextField);
    }//GEN-LAST:event_btnPayDateActionPerformed

    private void btnDateInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateInActionPerformed
        showCalendar(dateInTextField);
    }//GEN-LAST:event_btnDateInActionPerformed

    private void btnDateOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateOutActionPerformed
        showCalendar(dateOutTextField);
    }//GEN-LAST:event_btnDateOutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel appPanel;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAdd;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAdd1;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnDateIn;
    private javax.swing.JButton btnDateOut;
    private javax.swing.JButton btnDateReceivedFrom;
    private javax.swing.JButton btnDateReceivedTo;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEdit;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEdit1;
    public javax.swing.JButton btnFind;
    public javax.swing.JButton btnFind1;
    private javax.swing.JButton btnOpenItem;
    private javax.swing.JButton btnOpenItem1;
    private javax.swing.JButton btnPayDate;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemove;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemove1;
    private javax.swing.JFormattedTextField dateInTextField;
    private javax.swing.JFormattedTextField dateOutTextField;
    private javax.swing.JTextField fileNumberTextField;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel inwardPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableInward;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableLease;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblDateReceivedFrom;
    private javax.swing.JLabel lblDateReceivedTo;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPayDate;
    private javax.swing.JLabel lblReceiptNumber;
    private javax.swing.JLabel lblSearchCount;
    private javax.swing.JLabel lblSearchCount1;
    private javax.swing.JLabel lblSearchResult;
    private javax.swing.JLabel lblSearchResult1;
    private javax.swing.JPanel leasePanel;
    private javax.swing.JTextField locationTextField;
    private org.sola.clients.beans.minister.MinisterInwardSearchParamsBean ministerInwardSearchParamsBean;
    private org.sola.clients.beans.minister.MinisterInwardSearchResultListBean ministerInwardSearchResultListBean;
    private org.sola.clients.beans.minister.MinisterLeaseSearchParamsBean ministerLeaseSearchParamsBean;
    private org.sola.clients.beans.minister.MinisterLeaseSearchResultListBean ministerLeaseSearchResultListBean;
    private javax.swing.JTabbedPane ministerTab;
    private javax.swing.JTextField nameInwardTextField;
    private javax.swing.JTextField nameLeaseTextField;
    private javax.swing.JFormattedTextField payDateTextField;
    private javax.swing.JTextField receiptNumberTextField;
    private javax.swing.JFormattedTextField receivedFromTextField;
    private javax.swing.JFormattedTextField receivedToTextField;
    private javax.swing.JTextField subjectTextField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
