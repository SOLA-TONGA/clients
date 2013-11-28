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
package org.sola.clients.swing.desktop.administrative;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.validation.groups.Default;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.administrative.validation.MortgageValidationGroup;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.StatusConstants;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.controls.BrowseControlListener;
import org.sola.clients.swing.common.utils.BindingTools;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.party.QuickSearchPartyForm;
import org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Used to create and manage mortgages. {@link RrrBean} is used to bind the data
 * on the form.
 */
public class MortgagePanel extends ContentPanel {

    private ApplicationBean appBean;
    private ApplicationServiceBean appService;
    private RrrBean.RRR_ACTION rrrAction;
    public static final String UPDATED_RRR = "updatedRRR";

    private DocumentsManagementExtPanel createDocumentsPanel() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        if (appBean == null) {
            appBean = new ApplicationBean();
        }

        boolean allowEdit = true;
        if (rrrAction == RrrBean.RRR_ACTION.VIEW) {
            allowEdit = false;
        }

        DocumentsManagementExtPanel panel = new DocumentsManagementExtPanel(
                rrrBean.getSourceList(), appBean, allowEdit);
        return panel;
    }

    private RrrBean CreateRrrBean() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        return rrrBean;
    }

    public MortgagePanel(RrrBean rrrBean, ApplicationBean applicationBean,
            ApplicationServiceBean applicationService, RrrBean.RRR_ACTION rrrAction) {
        this.appBean = applicationBean;
        this.appService = applicationService;
        this.rrrAction = rrrAction;
        prepareRrrBean(rrrBean, rrrAction);

        initComponents();

        headerPanel.setTitleText(rrrBean.getRrrType().getDisplayValue());
        customizeForm(rrrAction);
        saveRrrState();
    }

    private void prepareRrrBean(RrrBean rrrBean, RrrBean.RRR_ACTION rrrAction) {
        if (rrrBean == null) {
            this.rrrBean = new RrrBean();
            this.rrrBean.setStatusCode(StatusConstants.PENDING);
        } else {
            this.rrrBean = rrrBean.makeCopyByAction(rrrAction);
        }
    }

    private void customizeForm(RrrBean.RRR_ACTION rrrAction) {
        if (rrrAction == RrrBean.RRR_ACTION.NEW) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_CREATE_AND_CLOSE).getMessage());
        }
        if (rrrAction == RrrBean.RRR_ACTION.CANCEL) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_EXTINGUISH_AND_CLOSE).getMessage());
        }

        if (rrrAction != RrrBean.RRR_ACTION.EDIT && rrrAction != RrrBean.RRR_ACTION.VIEW
                && appService != null) {
            // Set default noation text from the selected application service
            txtNotationText.setText(appService.getRequestType().getNotationTemplate());
        }

        if (rrrAction == RrrBean.RRR_ACTION.VIEW) {
            btnSave.setEnabled(false);
            txtRegDatetime.setEnabled(false);
            txtNotationText.setEnabled(false);
            txtAmount.setEnabled(false);
            txtMortgageBookPage.setEnabled(false);
            txtInterestRate.setEnabled(false);
            txtTerm.setEnabled(false);
            txtReceiptNumber.setEnabled(false);
            txtReceiptDate.setEnabled(false);
            browseLender.setEnabled(false);
            txtMortgageNumber.setEnabled(false);
        } else {
            browseLender.addBrowseControlEventListener(new BrowseControlListener() {

                @Override
                public void deleteButtonClicked(MouseEvent e) {
                }

                @Override
                public void browseButtonClicked(MouseEvent e) {
                    openLenderForm();
                }

                @Override
                public void controlClicked(MouseEvent e) {
                }

                @Override
                public void textClicked(MouseEvent e) {
                }
            });
        }
    }

    private void openLenderForm() {
        QuickSearchPartyForm searchForm = new QuickSearchPartyForm(this, true);
        searchForm.getSearchParams().setRoleTypeCode("bank");
        searchForm.setLocationRelativeTo(this);

        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getNewValue() != null) {
                    rrrBean.setFirstRightholder((PartySummaryBean) e.getNewValue());
                    BindingTools.resetBinding(bindingGroup, "lenderGoup");
                }
            }
        };
        searchForm.addPropertyChangeListener(QuickSearchPartyForm.SELECTED_PARTY, listener);
        searchForm.setVisible(true);
        searchForm.removePropertyChangeListener(QuickSearchPartyForm.SELECTED_PARTY, listener);
    }

    private boolean saveRrr() {
        WindowUtility.commitChanges(this);
        if (rrrBean.validate(true, Default.class, MortgageValidationGroup.class).size() > 0) {
            return false;
        }
        firePropertyChange(UPDATED_RRR, null, rrrBean);
        close();
        return true;
    }

    private void saveRrrState() {
        MainForm.saveBeanState(rrrBean);
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(rrrBean)) {
            return saveRrr();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        rrrBean = CreateRrrBean();
        mortgageTypeListBean = new org.sola.clients.beans.referencedata.MortgageTypeListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 32767));
        jLabel14 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        documentsManagementPanel1 = createDocumentsPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMortgageNumber = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        txtMortgageBookPage = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        browseLender = new org.sola.clients.swing.common.controls.BrowseControl();
        jPanel13 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtRegDatetime = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTerm = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtInterestRate = new javax.swing.JFormattedTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtNotationText = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        txtReceiptNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        txtReceiptDate = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();

        setCloseOnHide(true);
        setHeaderPanel(headerPanel);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        setHelpTopic(bundle.getString("MortgagePanel.helpTopic")); // NOI18N
        setName("Form"); // NOI18N

        headerPanel.setName("headerPanel"); // NOI18N
        headerPanel.setTitleText(bundle.getString("MortgagePanel.headerPanel.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSave.setText(bundle.getString("MortgagePanel.btnSave.text")); // NOI18N
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        filler1.setName("filler1"); // NOI18N
        jToolBar1.add(filler1);

        jLabel14.setText(bundle.getString("MortgagePanel.jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        jToolBar1.add(jLabel14);

        lblStatus.setFont(LafManager.getInstance().getLabFontBold());
        lblStatus.setName("lblStatus"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"), lblStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jToolBar1.add(lblStatus);

        documentsManagementPanel1.setName(bundle.getString("MortgagePanel.documentsManagementPanel1.name")); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setLayout(new java.awt.GridLayout(3, 2, 12, 0));

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel10.setName("jPanel10"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel2.setText(bundle.getString("MortgagePanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtMortgageNumber.setName("txtMortgageNumber"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${rrrReference}"), txtMortgageNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
            .addComponent(txtMortgageNumber)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMortgageNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel10);

        jPanel1.setName("jPanel1"); // NOI18N

        txtMortgageBookPage.setName("txtMortgageBookPage"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registryBookReference}"), txtMortgageBookPage, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel3.setText(bundle.getString("MortgagePanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
            .addComponent(txtMortgageBookPage)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMortgageBookPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel1);

        jPanel8.add(jPanel5);

        jPanel3.setName("jPanel3"); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel6.setText(bundle.getString("MortgagePanel.jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        browseLender.setBrowseButtonTooltip(bundle.getString("MortgagePanel.browseLender.browseButtonTooltip")); // NOI18N
        browseLender.setDeleteButtonIcon(null);
        browseLender.setDeleteButtonTooltip(bundle.getString("MortgagePanel.browseLender.deleteButtonTooltip")); // NOI18N
        browseLender.setDisplayDeleteButton(false);
        browseLender.setName("browseLender"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${firstRightHolder.name}"), browseLender, org.jdesktop.beansbinding.BeanProperty.create("text"), "lenderGoup");
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(browseLender, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseLender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel3);

        jPanel13.setName("jPanel13"); // NOI18N
        jPanel13.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel13.setText(bundle.getString("MortgagePanel.jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        txtRegDatetime.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtRegDatetime.setName("txtRegDatetime"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"), txtRegDatetime, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRegDatetime)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRegDatetime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel2);

        jPanel6.setName("jPanel6"); // NOI18N

        jLabel4.setText(bundle.getString("MortgagePanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        txtAmount.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        txtAmount.setName("txtAmount"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${amount}"), txtAmount, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAmount)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel6);

        jPanel8.add(jPanel13);

        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel4.setName("jPanel4"); // NOI18N

        jLabel1.setText(bundle.getString("MortgagePanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        txtTerm.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory(2));
        txtTerm.setName("txtTerm"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${term}"), txtTerm, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTerm, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel4);

        jPanel7.setName("jPanel7"); // NOI18N

        jLabel5.setText(bundle.getString("MortgagePanel.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        txtInterestRate.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory(2));
        txtInterestRate.setName("txtInterestRate"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${mortgageInterestRate}"), txtInterestRate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtInterestRate, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel7);

        jPanel8.add(jPanel9);

        jPanel11.setName("jPanel11"); // NOI18N

        jLabel16.setText(bundle.getString("MortgagePanel.jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        txtNotationText.setName("txtNotationText"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${notation.notationText}"), txtNotationText, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNotationText)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNotationText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel11);

        jPanel14.setName("jPanel14"); // NOI18N
        jPanel14.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel15.setName("jPanel15"); // NOI18N

        txtReceiptNumber.setName("txtReceiptNumber"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${receiptReference}"), txtReceiptNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel7.setText(bundle.getString("MortgagePanel.jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtReceiptNumber, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtReceiptNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel15);

        jPanel16.setName("jPanel16"); // NOI18N

        txtReceiptDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtReceiptDate.setText(bundle.getString("MortgagePanel.txtReceiptDate.text")); // NOI18N
        txtReceiptDate.setName("txtReceiptDate"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${receiptDate}"), txtReceiptDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel8.setText(bundle.getString("MortgagePanel.jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtReceiptDate)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtReceiptDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel16);

        jPanel8.add(jPanel14);

        groupPanel1.setName("groupPanel1"); // NOI18N
        groupPanel1.setTitleText(bundle.getString("MortgagePanel.groupPanel1.titleText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(documentsManagementPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentsManagementPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveRrr();
    }//GEN-LAST:event_btnSaveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.controls.BrowseControl browseLender;
    private javax.swing.JButton btnSave;
    private org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel documentsManagementPanel1;
    private javax.swing.Box.Filler filler1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblStatus;
    private org.sola.clients.beans.referencedata.MortgageTypeListBean mortgageTypeListBean;
    private org.sola.clients.beans.administrative.RrrBean rrrBean;
    private javax.swing.JFormattedTextField txtAmount;
    private javax.swing.JFormattedTextField txtInterestRate;
    private javax.swing.JTextField txtMortgageBookPage;
    private javax.swing.JTextField txtMortgageNumber;
    public javax.swing.JTextField txtNotationText;
    private javax.swing.JFormattedTextField txtReceiptDate;
    private javax.swing.JTextField txtReceiptNumber;
    private javax.swing.JFormattedTextField txtRegDatetime;
    private javax.swing.JFormattedTextField txtTerm;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
