/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations
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
package org.sola.clients.swing.desktop.drafting;

import java.awt.ComponentOrientation;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.sola.clients.beans.drafting.DraftingBean;
import org.sola.clients.beans.drafting.DraftingSearchParamsBean;
import org.sola.clients.beans.drafting.DraftingSearchResultListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.controls.WatermarkDate;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.common.utils.FormattersFactory;
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
public class DraftingSearchPanel extends ContentPanel {

    public static final String SELECT_DRAFTING_ITEM = "selectDrafting";
    public static final String ADD_PROPERTY = "addDrafting";
    public static final String EDIT_PROPERTY = "editDrafting";
    public static final String REMOVE_PROPERTY = "removeDrafting";
    public static final String OPEN_PROPERTY = "openDrafting";

    /**
     * Creates new form DraftingSearchPanel
     */
    public DraftingSearchPanel() {
        initComponents();

        draftingSearchResultListBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DraftingSearchResultListBean.SELECTED_DRAFTING_SEARCH_RESULT_PROPERTY)) {
                    customizeDraftingButtons();
                }
            }
        });
        customizeDraftingButtons();
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    private void executeSearch(final DraftingSearchParamsBean params,
            final JLabel lblSearchCount, final DraftingSearchResultListBean results) {

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                results.search(params);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchCount.setText(Integer.toString(results.getDraftingResultList().size()));
                if (results.getDraftingResultList().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (results.getDraftingResultList().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Enables or disables Drafting management buttons, based on security
     * rights.
     */
    private void customizeDraftingButtons() {
        boolean hasDraftingEditRole = SecurityBean.isInRole(RolesConstants.DRAFTING_EDIT);
        boolean enabled = draftingSearchResultListBean.getSelectedDraftingSearchResult() != null;

        enabled = enabled && hasDraftingEditRole;

        if (enabled && draftingSearchResultListBean.getSelectedDraftingSearchResult() != null) {
            enabled = SecurityBean.isInRole(RolesConstants.DRAFTING_EDIT);
        }
        btnAdd.setEnabled(hasDraftingEditRole);
        btnEdit.setEnabled(enabled);
        btnRemove.setEnabled(enabled && SecurityBean.isInRole(RolesConstants.DRAFTING_REMOVE));
        btnOpenItem.setEnabled(enabled);

    }

    public void addDrafting() {

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DRAFTING_FORM));
                DraftingForm form = new DraftingForm();
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_DRAFTING_FORM, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    public void removeDrafting() {

        if (draftingSearchResultListBean.getSelectedDraftingSearchResult() != null) {
            int button = MessageUtility.displayMessage(ClientMessage.DRAFT_REMOVE_ITEM,
                    new Object [] {draftingSearchResultListBean.getSelectedDraftingSearchResult().getItemNumber()});
            if (button == MessageUtility.BUTTON_ONE) {
                SolaTask t = new SolaTask<Void, Void>() {

                    @Override
                    public Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DRAFTING_FORM));
                        DraftingBean.removeDrafting(draftingSearchResultListBean.getSelectedDraftingSearchResult().getId());
                        return null;
                    }

                    @Override
                    public void taskDone() {
                        // Rerun the search to show that the item is removed. 
                        executeSearch(draftingSearchParamsBean, lblSearchCount, draftingSearchResultListBean);
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }

    }

    public void editDrafting() {
        if (draftingSearchResultListBean.getSelectedDraftingSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DRAFTING_FORM));
                    DraftingForm form = new DraftingForm(DraftingBean.getDrafting(draftingSearchResultListBean.getSelectedDraftingSearchResult().getId()), false);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_DRAFTING_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    public void openDrafting() {
        if (draftingSearchResultListBean.getSelectedDraftingSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DRAFTING_FORM));
                    DraftingForm form = new DraftingForm(DraftingBean.getDrafting(draftingSearchResultListBean.getSelectedDraftingSearchResult().getId()), true);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_DRAFTING_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    public void clearDrafting() {
        txtItemNumber.setText(null);
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtPlanNumber.setText(null);
        txtLocation.setText(null);
        txtActionDateTo.setValue(null);
        txtActionDateFrom.setValue(null);
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

        jComboBox1 = new javax.swing.JComboBox();
        draftingSearchResultListBean = new org.sola.clients.beans.drafting.DraftingSearchResultListBean();
        lblSearchResults2 = new javax.swing.JLabel();
        draftingSearchParamsBean = new org.sola.clients.beans.drafting.DraftingSearchParamsBean();
        draftingBean = new org.sola.clients.beans.drafting.DraftingBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnFind = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnOpenItem = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        lblSearchResults = new javax.swing.JLabel();
        labResults = new javax.swing.JLabel();
        lblLeaseResultsCount = new javax.swing.JLabel();
        btnAdd = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEdit = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemove = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        labSearchResult = new javax.swing.JLabel();
        lblSearchCount = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtItemNumber = new javax.swing.JTextField();
        labItemNumber = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        txtFirstName = new javax.swing.JTextField();
        labFirstName = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        txtLastName = new javax.swing.JTextField();
        labLastName = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        txtPlanNumber = new javax.swing.JTextField();
        labPlanNumber = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labTown = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtActionDateFrom = new WatermarkDate();
        btnShowCalendarFrom = new javax.swing.JButton();
        labActionDateFrom = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        labActionDateTo = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        txtActionDateTo = new WatermarkDate();
        btnShowCalendarTo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        draftingTable = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblSearchResults2.setFont(LafManager.getInstance().getLabFontBold());
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        lblSearchResults2.setText(bundle.getString("ApplicationSearchPanel.lblSearchResults.text")); // NOI18N

        setHeaderPanel(headerPanel);

        headerPanel.setTitleText(bundle.getString("ApplicationSearchPanel.headerPanel1.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnFind.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFind);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClear.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClear);

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
        jToolBar1.add(btnOpenItem);
        jToolBar1.add(jSeparator3);

        lblSearchResults.setFont(LafManager.getInstance().getLabFontBold());
        lblSearchResults.setText(bundle.getString("ApplicationSearchPanel.lblSearchResults.text")); // NOI18N
        jToolBar1.add(lblSearchResults);

        labResults.setText(bundle.getString("ApplicationSearchPanel.labResults.text")); // NOI18N
        labResults.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        labResults.setHorizontalAlignment(JLabel.LEADING);
        jToolBar1.add(labResults);

        lblLeaseResultsCount.setFont(LafManager.getInstance().getLabFontBold());
        lblLeaseResultsCount.setText(bundle.getString("ApplicationSearchPanel.lblSearchResults.text")); // NOI18N
        jToolBar1.add(lblLeaseResultsCount);

        btnAdd.setEnabled(false);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAdd);

        btnEdit.setEnabled(false);
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        btnRemove.setEnabled(false);
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemove);
        jToolBar1.add(jSeparator1);

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/drafting/Bundle"); // NOI18N
        labSearchResult.setText(bundle1.getString("DraftingSearchPanel.labSearchResult.text")); // NOI18N
        jToolBar1.add(labSearchResult);

        lblSearchCount.setFont(LafManager.getInstance().getLabFontBold());
        lblSearchCount.setText(bundle1.getString("DraftingSearchPanel.lblSearchCount.text")); // NOI18N
        jToolBar1.add(lblSearchCount);

        jPanel1.setLayout(new java.awt.GridLayout(2, 4, 12, 0));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${itemNumber}"), txtItemNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labItemNumber.setText(bundle1.getString("DraftingSearchPanel.labItemNumber.text")); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(txtItemNumber)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labItemNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${firstName}"), txtFirstName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labFirstName.setText(bundle1.getString("DraftingSearchPanel.labFirstName.text")); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(txtFirstName)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(labFirstName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${lastName}"), txtLastName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labLastName.setText(bundle1.getString("DraftingSearchPanel.labLastName.text")); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(txtLastName)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(labLastName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel15);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${planNumber}"), txtPlanNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labPlanNumber.setText(bundle1.getString("DraftingSearchPanel.labPlanNumber.text")); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPlanNumber)
            .addComponent(labPlanNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(labPlanNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPlanNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel13);

        labTown.setText(bundle1.getString("DraftingSearchPanel.labTown.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${location}"), txtLocation, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labTown, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(txtLocation)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(labTown)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7);

        txtActionDateFrom.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtActionDateFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedFrom}"), txtActionDateFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txtActionDateFrom.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtActionDateFrom.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtActionDateFrom, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtActionDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);
        btnShowCalendarFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowCalendarFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFromActionPerformed(evt);
            }
        });

        labActionDateFrom.setText(bundle1.getString("DraftingSearchPanel.labActionDateFrom.text")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(labActionDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(labActionDateFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowCalendarFrom))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6);

        labActionDateTo.setText(bundle.getString("ApplicationSearchPanel.labFrom.text")); // NOI18N

        txtActionDateFrom.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtActionDateTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedTo}"), txtActionDateTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txtActionDateFrom.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtActionDateFrom.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtActionDateTo, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtActionDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnShowCalendarTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarTo.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarTo.setBorder(null);
        btnShowCalendarTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowCalendarTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarTo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(labActionDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(labActionDateTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowCalendarTo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);

        draftingTable.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${draftingResultList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchResultListBean, eLProperty, draftingTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${itemNumber}"));
        columnBinding.setColumnName("Item Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${firstName}"));
        columnBinding.setColumnName("First Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${lastName}"));
        columnBinding.setColumnName("Last Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiveDate}"));
        columnBinding.setColumnName("Receive Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${location}"));
        columnBinding.setColumnName("Location");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${planNumber}"));
        columnBinding.setColumnName("Plan Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${referInfo}"));
        columnBinding.setColumnName("Refer Info");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${natureOfSurvey}"));
        columnBinding.setColumnName("Nature Of Survey");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${deedNumber}"));
        columnBinding.setColumnName("Deed Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${plottingDate}"));
        columnBinding.setColumnName("Plotting Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, draftingSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedDraftingSearchResult}"), draftingTable, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setViewportView(draftingTable);
        if (draftingTable.getColumnModel().getColumnCount() > 0) {
            draftingTable.getColumnModel().getColumn(0).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title0")); // NOI18N
            draftingTable.getColumnModel().getColumn(1).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title1")); // NOI18N
            draftingTable.getColumnModel().getColumn(2).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title2")); // NOI18N
            draftingTable.getColumnModel().getColumn(3).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title3")); // NOI18N
            draftingTable.getColumnModel().getColumn(4).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title4")); // NOI18N
            draftingTable.getColumnModel().getColumn(5).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title5")); // NOI18N
            draftingTable.getColumnModel().getColumn(6).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title6")); // NOI18N
            draftingTable.getColumnModel().getColumn(7).setHeaderValue(bundle1.getString("DraftingSearchPanel.tableLease.columnModel.title8")); // NOI18N
            draftingTable.getColumnModel().getColumn(8).setHeaderValue(bundle1.getString("DraftingSearchPanel.tableLease.columnModel.title9")); // NOI18N
            draftingTable.getColumnModel().getColumn(9).setHeaderValue(bundle1.getString("DraftingSearchPanel.draftingTable.columnModel.title9")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowCalendarFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFromActionPerformed
        showCalendar(txtActionDateFrom);
    }//GEN-LAST:event_btnShowCalendarFromActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        executeSearch(draftingSearchParamsBean, lblSearchCount, draftingSearchResultListBean);
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearDrafting();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnOpenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenItemActionPerformed
        openDrafting();
    }//GEN-LAST:event_btnOpenItemActionPerformed

    private void btnShowCalendarToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarToActionPerformed
        showCalendar(txtActionDateTo);
    }//GEN-LAST:event_btnShowCalendarToActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addDrafting();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editDrafting();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeDrafting();
    }//GEN-LAST:event_btnRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnAdd btnAdd;
    private javax.swing.JButton btnClear;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEdit;
    public javax.swing.JButton btnFind;
    private javax.swing.JButton btnOpenItem;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemove;
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarTo;
    private org.sola.clients.beans.drafting.DraftingBean draftingBean;
    private org.sola.clients.beans.drafting.DraftingSearchParamsBean draftingSearchParamsBean;
    private org.sola.clients.beans.drafting.DraftingSearchResultListBean draftingSearchResultListBean;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles draftingTable;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labActionDateFrom;
    private javax.swing.JLabel labActionDateTo;
    private javax.swing.JLabel labFirstName;
    private javax.swing.JLabel labItemNumber;
    private javax.swing.JLabel labLastName;
    private javax.swing.JLabel labPlanNumber;
    private javax.swing.JLabel labResults;
    private javax.swing.JLabel labSearchResult;
    private javax.swing.JLabel labTown;
    private javax.swing.JLabel lblLeaseResultsCount;
    private javax.swing.JLabel lblSearchCount;
    private javax.swing.JLabel lblSearchResults;
    private javax.swing.JLabel lblSearchResults2;
    private javax.swing.JFormattedTextField txtActionDateFrom;
    private javax.swing.JFormattedTextField txtActionDateTo;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtItemNumber;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtPlanNumber;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
