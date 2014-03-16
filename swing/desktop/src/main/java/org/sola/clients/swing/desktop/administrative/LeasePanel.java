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
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.validation.groups.Default;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.ConditionForRrrBean;
import org.sola.clients.beans.administrative.RelatedBaUnitInfoBean;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.administrative.RrrPaymentHistoryListBean;
import org.sola.clients.beans.administrative.RrrReportBean;
import org.sola.clients.beans.administrative.validation.LeaseValidationGroup;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.ConditionTypeListBean;
import org.sola.clients.beans.referencedata.StatusConstants;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.controls.WatermarkDate;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.party.PartyPanelForm;
import org.sola.clients.swing.desktop.party.PartySearchPanelForm;
import org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.BooleanCellRenderer2;
import org.sola.clients.swing.ui.renderers.DateTimeRenderer;
import org.sola.clients.swing.common.utils.FormattersFactory;
import org.sola.clients.swing.ui.renderers.MoneyCellRenderer;
import org.sola.clients.swing.ui.renderers.TableCellTextAreaRenderer;
import org.sola.clients.swing.ui.reports.FreeTextDialog;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Form for managing simple ownership right. {@link RrrBean} is used to bind the
 * data on the form.
 */
public class LeasePanel extends ContentPanel {

    private ApplicationBean applicationBean;
    private ApplicationServiceBean appService;
    private RrrBean.RRR_ACTION rrrAction;
    private BaUnitBean baUnit;
    java.util.ResourceBundle resourceBundle;
    public static final String UPDATED_RRR = "updatedRRR";

    private DocumentsManagementExtPanel createDocumentsPanel() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        if (applicationBean == null) {
            applicationBean = new ApplicationBean();
        }

        boolean allowEdit = true;
        if (rrrAction == RrrBean.RRR_ACTION.VIEW) {
            allowEdit = false;
        }

        DocumentsManagementExtPanel panel = new DocumentsManagementExtPanel(
                rrrBean.getSourceList(), applicationBean, allowEdit);
        return panel;
    }

    private RrrBean CreateRrrBean() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        return rrrBean;
    }

    private RrrPaymentHistoryListBean createPaymentHistoryListBean() {
        if (paymentHistoryListBean == null) {
            paymentHistoryListBean = new RrrPaymentHistoryListBean();
        }
        return paymentHistoryListBean;
    }

    /**
     * Creates new form SimpleOwhershipPanel
     */
    public LeasePanel(BaUnitBean baUnit, RrrBean rrrBean, ApplicationBean applicationBean,
            ApplicationServiceBean applicationService, RrrBean.RRR_ACTION rrrAction) {
        this.baUnit = baUnit;
        this.applicationBean = applicationBean;
        this.appService = applicationService;
        this.rrrAction = rrrAction;
        resourceBundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle");
        prepareRrrBean(rrrBean, rrrAction);
        initComponents();
        postInit();
    }

    private void postInit() {
        if (rrrBean != null && rrrBean.getId() != null) {
            paymentHistoryListBean.loadList(rrrBean.getId());
        }
        // Populate lease conditions list with standard conditions for new RrrBean
        if (rrrAction == RrrBean.RRR_ACTION.NEW) {
            rrrBean.addConditions(conditionTypes.getLeaseConditionList());
        }

        conditionTypes.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ConditionTypeListBean.SELECTED_CONDITION_TYPE_PROPERTY)) {
                    customizeAddStandardConditionButton();
                }
            }
        });

        rrrBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(RrrBean.SELECTED_CONDITION_PROPERTY)) {
                    customizeLeaseConditionsButtons();
                }
            }
        });

        customizeForm();
        customizeOwnerButtons(null);
        customizeAddStandardConditionButton();
        customizeLeaseConditionsButtons();
        saveRrrState();
    }

    private void customizeLeaseConditionsButtons() {
        boolean enabled = rrrBean.getSelectedCondition() != null && rrrAction != RrrBean.RRR_ACTION.VIEW;

        btnRemoveCondition.setEnabled(enabled);
        if (enabled) {
            btnEditCondition.setEnabled(rrrBean.getSelectedCondition().isCustomCondition());
        } else {
            btnEditCondition.setEnabled(enabled);
        }
        menuEditCondition.setEnabled(btnEditCondition.isEnabled());
        menuRemoveCondition.setEnabled(btnRemoveCondition.isEnabled());
    }

    private void customizeAddStandardConditionButton() {
        if (rrrAction != RrrBean.RRR_ACTION.VIEW) {
            return;
        }
        btnAddStandardCondition.setEnabled(conditionTypes.getSelectedConditionType() != null);
    }

    private void customizeForm() {
        headerPanel.setTitleText(rrrBean.getRrrType().getDisplayValue());
        if (rrrAction == RrrBean.RRR_ACTION.NEW) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_CREATE_AND_CLOSE).getMessage());
        }
        if (rrrAction == RrrBean.RRR_ACTION.CANCEL) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_TERMINATE_AND_CLOSE).getMessage());
        }

        if (rrrAction != RrrBean.RRR_ACTION.EDIT && rrrAction != RrrBean.RRR_ACTION.VIEW
                && appService != null) {
            // Set default noation text from the selected application service
            txtNotationText.setText(appService.getRequestType().getNotationTemplate());
        }

        boolean enabled = rrrAction != RrrBean.RRR_ACTION.VIEW;

        btnSave.setEnabled(enabled);
        txtRegistrationNumber.setEnabled(enabled);
        txtRegistrationDate.setEnabled(enabled);
        txtTerm.setEnabled(enabled);
        txtNotationText.setEnabled(enabled);
        txtExpirationDate.setEnabled(enabled);
        txtRent.setEnabled(enabled);
        txtStartDate.setEnabled(enabled);
        txtReceiptRef.setEnabled(enabled);
        txtReceiptDate.setEnabled(enabled);
        txtDueDate.setEnabled(enabled);
        txtPaymentAmount.setEnabled(enabled);
        txtNotationText.setEnabled(enabled);
        btnRegDate.setEnabled(enabled);
        btnExpirationDate.setEnabled(enabled);
        btnReceiptDate.setEnabled(enabled);
        btnStartDate.setEnabled(enabled);
        btnDueDate.setEnabled(enabled);
        // Don't allow the Landholder field to be edited if there is a 
        // relationship to an allotment. 
        txtLandholder.setEnabled(enabled
                && !baUnit.hasParentRelationship(RelatedBaUnitInfoBean.CODE_ALLOTMENT));

        /*
         btnPrintDraftLease.setEnabled(enabled);
         btnPrintDraftOffer.setEnabled(enabled);
         btnPrintLease.setEnabled(enabled);
         btnPrintOffer.setEnabled(enabled);
         btnPrintRejection.setEnabled(enabled);
         btnAddCustomCondition.setEnabled(enabled);
         btnAddStandardCondition.setEnabled(enabled);
         cbxStandardConditions.setEnabled(enabled);
         menuAddCustomCondition.setEnabled(btnAddCustomCondition.isEnabled());*/

        // SOLA Tonga Customisation - Remove Print buttons
        btnPrintDraftLease.setVisible(false);
        btnPrintDraftOffer.setVisible(false);
        btnPrintLease.setVisible(false);
        btnPrintOffer.setVisible(false);
        btnPrintRejection.setVisible(false);
        jLabel1.setVisible(false);
        lblStatus.setVisible(false);
        jSeparator1.setVisible(false);
        jSeparator2.setVisible(false);
        jSeparator3.setVisible(false);
        jSeparator4.setVisible(false);

        // SOLA Tonga Customisation - Remove /Hide Lease Conditions Tab
        if (tabPayments.indexOfComponent(jPanel12) >= 0) {
            tabPayments.removeTabAt(tabPayments.indexOfComponent(jPanel12));
        }

        if (baUnit.isSublease()) {
            // This is a sublease so make some minor adjustments to the form
            lblLandholder.setText(resourceBundle.getString("LeasePanel.Sublease.lblLandholder.text"));
        }
    }

    private void prepareRrrBean(RrrBean rrrBean, RrrBean.RRR_ACTION rrrAction) {
        if (rrrBean == null) {
            this.rrrBean = new RrrBean();
            this.rrrBean.setStatusCode(StatusConstants.PENDING);
        } else {
            this.rrrBean = rrrBean.makeCopyByAction(rrrAction);
        }
        this.rrrBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(RrrBean.SELECTED_RIGHTHOLDER_PROPERTY)) {
                    customizeOwnerButtons((PartySummaryBean) evt.getNewValue());
                }
            }
        });
    }

    private void customizeOwnerButtons(PartySummaryBean owner) {
        boolean isChangesAllowed = false;
        if (rrrAction == RrrBean.RRR_ACTION.VARY || rrrAction == RrrBean.RRR_ACTION.EDIT
                || rrrAction == RrrBean.RRR_ACTION.NEW) {
            isChangesAllowed = true;
        }

        btnAddOwner.setEnabled(isChangesAllowed);
        btnSelectExisting.setEnabled(isChangesAllowed);

        if (owner == null) {
            btnRemoveOwner.setEnabled(false);
            btnEditOwner.setEnabled(false);
            btnViewOwner.setEnabled(false);
        } else {
            btnRemoveOwner.setEnabled(isChangesAllowed);
            btnEditOwner.setEnabled(isChangesAllowed);
            btnViewOwner.setEnabled(true);
        }

        menuAddOwner.setEnabled(btnAddOwner.isEnabled());
        menuRemoveOwner.setEnabled(btnRemoveOwner.isEnabled());
        menuEditOwner.setEnabled(btnEditOwner.isEnabled());
        menuViewOwner.setEnabled(btnViewOwner.isEnabled());


    }

    private boolean saveRrr() {
        WindowUtility.commitChanges(this);
        if (rrrBean.validate(true, Default.class, LeaseValidationGroup.class).size() < 1) {
            firePropertyChange(UPDATED_RRR,
                    null, rrrBean);
            close();

            return true;
        }

        return false;
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
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

    private void viewOwner() {
        if (rrrBean.getSelectedRightHolder() != null) {
            openRightHolderForm(rrrBean.getSelectedRightHolder(), true);
        }
    }

    private void removeOwner() {
        if (rrrBean.getSelectedRightHolder() != null
                && MessageUtility.displayMessage(ClientMessage.CONFIRM_DELETE_RECORD) == MessageUtility.BUTTON_ONE) {
            rrrBean.removeSelectedRightHolder();
        }
    }

    private void addOwner() {
        openRightHolderForm(null, false);
    }

    private void editOwner() {
        if (rrrBean.getSelectedRightHolder() != null) {
            openRightHolderForm(rrrBean.getSelectedRightHolder(), false);


        }
    }

    private class RightHolderFormListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(PartyPanelForm.PARTY_SAVED)) {
                rrrBean.addOrUpdateRightholder((PartySummaryBean) ((PartyPanelForm) evt.getSource()).getParty());
                tableOwners.clearSelection();
            }
        }
    }

    private void openRightHolderForm(final PartySummaryBean partySummaryBean, final boolean isReadOnly) {
        final RightHolderFormListener listener = new RightHolderFormListener();

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PERSON));
                PartyPanelForm partyForm;

                if (partySummaryBean != null) {
                    partyForm = new PartyPanelForm(true, partySummaryBean, isReadOnly, true);
                } else {
                    partyForm = new PartyPanelForm(true, null, isReadOnly, true);
                }
                partyForm.addPropertyChangeListener(listener);
                getMainContentPanel().addPanel(partyForm, MainContentPanel.CARD_PERSON, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openSelectRightHolderForm() {
        final RightHolderFormListener listener = new RightHolderFormListener();

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PERSON));
                PartySearchPanelForm partySearchForm = null;

                partySearchForm = initializePartySearchForm(partySearchForm);

                partySearchForm.addPropertyChangeListener(listener);
                getMainContentPanel().addPanel(partySearchForm, MainContentPanel.CARD_SEARCH_PERSONS, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private PartySearchPanelForm initializePartySearchForm(PartySearchPanelForm partySearchForm) {
        partySearchForm = new PartySearchPanelForm(true, this.rrrBean);
        return partySearchForm;

    }

    private void addCustomCondition() {
        CustomConditionDialog form = new CustomConditionDialog(null, MainForm.getInstance(), true);
        WindowUtility.centerForm(form);

        form.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CustomConditionDialog.CONDITION_SAVED)) {
                    rrrBean.addRrrCondition((ConditionForRrrBean) evt.getNewValue());
                }
            }
        });
        form.setVisible(true);
    }

    private void editCustomCondition() {
        CustomConditionDialog form = new CustomConditionDialog(
                (ConditionForRrrBean) rrrBean.getSelectedCondition().copy(),
                MainForm.getInstance(), true);
        WindowUtility.centerForm(form);

        form.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CustomConditionDialog.CONDITION_SAVED)) {
                    ConditionForRrrBean cond = (ConditionForRrrBean) evt.getNewValue();
                    rrrBean.getSelectedCondition().setCustomConditionText(cond.getCustomConditionText());
                }
            }
        });
        form.setVisible(true);
    }

    private void addStandardCondition() {
        rrrBean.addCondition(conditionTypes.getSelectedConditionType());
    }

    private void removeCondition() {
        rrrBean.removeSelectedRrrCondition();
    }

    private RrrReportBean prepareReportBean() {
        RrrReportBean reportBean = new RrrReportBean(baUnit, rrrBean, applicationBean, appService);
        String warnings = "";
        String warning;

        if (applicationBean == null || applicationBean.isNew()) {
            warnings = warnings + MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_NOT_FOUND);
        }

        if (reportBean.getRrrRegNumber().isEmpty()) {
            warning = MessageUtility.getLocalizedMessageText(
                    ClientMessage.BAUNIT_RRR_NO_REGISTRATION_NUMBER,
                    new Object[]{rrrBean.getRrrType().getDisplayValue()});
            if (warnings.isEmpty()) {
                warnings = "- " + warning;
            } else {
                warnings = warnings + "\n- " + warning;
            }
        }

        if (reportBean.getBaUnit().getCadastreObjectFilteredList().size() < 1) {
            warning = MessageUtility.getLocalizedMessageText(ClientMessage.BAUNIT_HAS_NO_PARCELS);
            if (warnings.isEmpty()) {
                warnings = "- " + warning;
            } else {
                warnings = warnings + "\n- " + warning;
            }
        }

        if (!warnings.isEmpty()) {
            warnings = MessageUtility.getLocalizedMessageText(ClientMessage.BAUNIT_RRR_REPORT_WARNINGS)
                    + "\n\n" + warnings;
            if (JOptionPane.showConfirmDialog(MainForm.getInstance(), warnings, "",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                return reportBean;
            } else {
                return null;
            }
        }
        return reportBean;
    }

    private void printRejectionLetter() {
        final RrrReportBean reportBean = prepareReportBean();
        if (reportBean != null) {
            // Show free text form
            FreeTextDialog form = new FreeTextDialog(
                    MessageUtility.getLocalizedMessageText(ClientMessage.BAUNIT_LEASE_REJECTION_REASON_TITLE),
                    null, MainForm.getInstance(), true);
            WindowUtility.centerForm(form);

            form.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(FreeTextDialog.TEXT_TO_SAVE)) {
                        reportBean.setFreeText((String) evt.getNewValue());
                    }
                }
            });
            form.setVisible(true);
            showReport(ReportManager.getLeaseRejectionReport(reportBean));
        }
    }

    private void printOfferLetter(boolean isDraft) {
        final RrrReportBean reportBean = prepareReportBean();
        if (reportBean != null) {
            showReport(ReportManager.getLeaseOfferReport(reportBean, isDraft));
        }
    }

    private void printLease(boolean isDraft) {
        final RrrReportBean reportBean = prepareReportBean();
        if (reportBean != null) {
            showReport(ReportManager.getLeaseReport(reportBean, isDraft));
        }
    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        rrrBean = CreateRrrBean();
        popUpOwners = new javax.swing.JPopupMenu();
        menuAddOwner = new javax.swing.JMenuItem();
        menuEditOwner = new javax.swing.JMenuItem();
        menuRemoveOwner = new javax.swing.JMenuItem();
        menuViewOwner = new javax.swing.JMenuItem();
        conditionTypes = new org.sola.clients.beans.referencedata.ConditionTypeListBean();
        leaseConditionsPopUp = new javax.swing.JPopupMenu();
        menuAddCustomCondition = new javax.swing.JMenuItem();
        menuEditCondition = new javax.swing.JMenuItem();
        menuRemoveCondition = new javax.swing.JMenuItem();
        paymentHistoryListBean = new org.sola.clients.beans.administrative.RrrPaymentHistoryListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPrintDraftOffer = new javax.swing.JButton();
        btnPrintDraftLease = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnPrintOffer = new javax.swing.JButton();
        btnPrintLease = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnPrintRejection = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        tabPayments = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        lblRegistryNumber = new javax.swing.JLabel();
        txtRegistrationNumber = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lblRegistrationDate = new javax.swing.JLabel();
        txtRegistrationDate = new WatermarkDate();
        btnRegDate = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        lblTerm = new javax.swing.JLabel();
        txtTerm = new javax.swing.JFormattedTextField();
        pnlStartDate = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtStartDate = new WatermarkDate();
        btnStartDate = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lblExpirationDate = new javax.swing.JLabel();
        txtExpirationDate = new WatermarkDate();
        btnExpirationDate = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblRent = new javax.swing.JLabel();
        txtRent = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnAddOwner = new javax.swing.JButton();
        btnEditOwner = new javax.swing.JButton();
        btnRemoveOwner = new javax.swing.JButton();
        btnViewOwner = new javax.swing.JButton();
        btnSelectExisting = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableOwners = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel3 = new javax.swing.JPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        documentsManagementPanel = createDocumentsPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        txtLandholder = new javax.swing.JTextField();
        lblLandholder = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblNotationText = new javax.swing.JLabel();
        txtNotationText = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLeaseConditions = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar3 = new javax.swing.JToolBar();
        btnAddCustomCondition = new javax.swing.JButton();
        btnEditCondition = new javax.swing.JButton();
        btnRemoveCondition = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(10, 32767));
        cbxStandardConditions = new javax.swing.JComboBox();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(10, 32767));
        btnAddStandardCondition = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblReceiptRef = new javax.swing.JLabel();
        lblReceiptDate = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDueDate = new javax.swing.JLabel();
        txtReceiptRef = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        txtReceiptDate = new WatermarkDate();
        btnReceiptDate = new javax.swing.JButton();
        txtPaymentAmount = new javax.swing.JFormattedTextField();
        jPanel19 = new javax.swing.JPanel();
        btnDueDate = new javax.swing.JButton();
        txtDueDate = new WatermarkDate();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles1 = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        groupPanel3 = new org.sola.clients.swing.ui.GroupPanel();

        menuAddOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        menuAddOwner.setText(bundle.getString("SimpleOwhershipPanel.menuAddOwner.text")); // NOI18N
        menuAddOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddOwnerActionPerformed(evt);
            }
        });
        popUpOwners.add(menuAddOwner);

        menuEditOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuEditOwner.setText(bundle.getString("SimpleOwhershipPanel.menuEditOwner.text")); // NOI18N
        menuEditOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditOwnerActionPerformed(evt);
            }
        });
        popUpOwners.add(menuEditOwner);

        menuRemoveOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveOwner.setText(bundle.getString("SimpleOwhershipPanel.menuRemoveOwner.text")); // NOI18N
        menuRemoveOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveOwnerActionPerformed(evt);
            }
        });
        popUpOwners.add(menuRemoveOwner);

        menuViewOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        menuViewOwner.setText(bundle.getString("SimpleOwhershipPanel.menuViewOwner.text")); // NOI18N
        menuViewOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuViewOwnerActionPerformed(evt);
            }
        });
        popUpOwners.add(menuViewOwner);

        menuAddCustomCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        menuAddCustomCondition.setText(bundle.getString("LeasePanel.menuAddCustomCondition.text")); // NOI18N
        menuAddCustomCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddCustomConditionActionPerformed(evt);
            }
        });
        leaseConditionsPopUp.add(menuAddCustomCondition);

        menuEditCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuEditCondition.setText(bundle.getString("LeasePanel.menuEditCondition.text")); // NOI18N
        menuEditCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditConditionActionPerformed(evt);
            }
        });
        leaseConditionsPopUp.add(menuEditCondition);

        menuRemoveCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveCondition.setText(bundle.getString("LeasePanel.menuRemoveCondition.text")); // NOI18N
        menuRemoveCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveConditionActionPerformed(evt);
            }
        });
        leaseConditionsPopUp.add(menuRemoveCondition);

        setHeaderPanel(headerPanel);

        headerPanel.setTitleText(bundle.getString("SimpleOwhershipPanel.headerPanel.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSave.setText(bundle.getString("SimpleOwhershipPanel.btnSave.text")); // NOI18N
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);
        jToolBar1.add(jSeparator1);

        btnPrintDraftOffer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintDraftOffer.setText(bundle.getString("LeasePanel.btnPrintDraftOffer.text")); // NOI18N
        btnPrintDraftOffer.setFocusable(false);
        btnPrintDraftOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDraftOfferActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintDraftOffer);

        btnPrintDraftLease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintDraftLease.setText(bundle.getString("LeasePanel.btnPrintDraftLease.text")); // NOI18N
        btnPrintDraftLease.setFocusable(false);
        btnPrintDraftLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDraftLeaseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintDraftLease);
        jToolBar1.add(jSeparator3);

        btnPrintOffer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintOffer.setText(bundle.getString("LeasePanel.btnPrintOffer.text")); // NOI18N
        btnPrintOffer.setFocusable(false);
        btnPrintOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintOfferActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintOffer);

        btnPrintLease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintLease.setText(bundle.getString("LeasePanel.btnPrintLease.text")); // NOI18N
        btnPrintLease.setFocusable(false);
        btnPrintLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLeaseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintLease);
        jToolBar1.add(filler1);
        jToolBar1.add(jSeparator4);

        btnPrintRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintRejection.setText(bundle.getString("LeasePanel.btnPrintRejection.text")); // NOI18N
        btnPrintRejection.setFocusable(false);
        btnPrintRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintRejectionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintRejection);
        jToolBar1.add(jSeparator2);

        jLabel1.setText(bundle.getString("SimpleOwhershipPanel.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);

        lblStatus.setFont(LafManager.getInstance().getLabFontBold());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"), lblStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jToolBar1.add(lblStatus);

        jPanel9.setLayout(new java.awt.GridLayout(1, 5, 15, 0));

        lblRegistryNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        lblRegistryNumber.setText(bundle.getString("LeasePanel.lblRegistryNumber.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registryBookReference}"), txtRegistrationNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRegistrationNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
            .addComponent(lblRegistryNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(lblRegistryNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRegistrationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel13);

        lblRegistrationDate.setText(bundle.getString("SimpleOwhershipPanel.jLabel2.text")); // NOI18N

        txtRegistrationDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtRegistrationDate.setText(bundle.getString("LeasePanel.txtRegistrationDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"), txtRegistrationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnRegDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnRegDate.setText(bundle.getString("LeasePanel.btnRegDate.text")); // NOI18N
        btnRegDate.setBorder(null);
        btnRegDate.setBorderPainted(false);
        btnRegDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblRegistrationDate)
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtRegistrationDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegDate)))
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(lblRegistrationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegDate)
                    .addComponent(txtRegistrationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel5);

        lblTerm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        lblTerm.setText(bundle.getString("LeasePanel.lblTerm.text")); // NOI18N

        txtTerm.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory(1));
        txtTerm.setText(bundle.getString("LeasePanel.txtTerm.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${term}"), txtTerm, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTerm)
            .addComponent(lblTerm, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(lblTerm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTerm)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel6);

        jLabel3.setText(bundle.getString("LeasePanel.jLabel3.text")); // NOI18N

        txtStartDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtStartDate.setText(bundle.getString("LeasePanel.txtStartDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${startDate}"), txtStartDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnStartDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnStartDate.setText(bundle.getString("LeasePanel.btnStartDate.text")); // NOI18N
        btnStartDate.setBorder(null);
        btnStartDate.setBorderPainted(false);
        btnStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlStartDateLayout = new javax.swing.GroupLayout(pnlStartDate);
        pnlStartDate.setLayout(pnlStartDateLayout);
        pnlStartDateLayout.setHorizontalGroup(
            pnlStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
            .addGroup(pnlStartDateLayout.createSequentialGroup()
                .addComponent(txtStartDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStartDate))
        );
        pnlStartDateLayout.setVerticalGroup(
            pnlStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStartDateLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStartDate)
                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(pnlStartDate);

        lblExpirationDate.setText(bundle.getString("LeasePanel.lblExpirationDate.text")); // NOI18N

        txtExpirationDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtExpirationDate.setText(bundle.getString("LeasePanel.txtExpirationDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${expirationDate}"), txtExpirationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnExpirationDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnExpirationDate.setText(bundle.getString("LeasePanel.btnExpirationDate.text")); // NOI18N
        btnExpirationDate.setBorder(null);
        btnExpirationDate.setBorderPainted(false);
        btnExpirationDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpirationDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblExpirationDate, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(txtExpirationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExpirationDate))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(lblExpirationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExpirationDate)
                    .addComponent(txtExpirationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel7);

        lblRent.setText(bundle.getString("LeasePanel.lblRent.text")); // NOI18N

        txtRent.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        txtRent.setText(bundle.getString("LeasePanel.txtRent.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${amount}"), txtRent, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRent)
            .addComponent(lblRent, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblRent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRent)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel8);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        groupPanel1.setTitleText(bundle.getString("SimpleOwhershipPanel.groupPanel1.titleText")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnAddOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddOwner.setText(bundle.getString("SimpleOwhershipPanel.btnAddOwner.text")); // NOI18N
        btnAddOwner.setFocusable(false);
        btnAddOwner.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOwnerActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAddOwner);

        btnEditOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEditOwner.setText(bundle.getString("SimpleOwhershipPanel.btnEditOwner.text")); // NOI18N
        btnEditOwner.setFocusable(false);
        btnEditOwner.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditOwnerActionPerformed(evt);
            }
        });
        jToolBar2.add(btnEditOwner);

        btnRemoveOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveOwner.setText(bundle.getString("SimpleOwhershipPanel.btnRemoveOwner.text")); // NOI18N
        btnRemoveOwner.setFocusable(false);
        btnRemoveOwner.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveOwnerActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRemoveOwner);

        btnViewOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnViewOwner.setText(bundle.getString("SimpleOwhershipPanel.btnViewOwner.text")); // NOI18N
        btnViewOwner.setFocusable(false);
        btnViewOwner.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnViewOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewOwnerActionPerformed(evt);
            }
        });
        jToolBar2.add(btnViewOwner);

        btnSelectExisting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSelectExisting.setText(bundle.getString("LeasePanel.btnSelectExisting.text_1")); // NOI18N
        btnSelectExisting.setFocusable(false);
        btnSelectExisting.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelectExisting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectExistingActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSelectExisting);

        tableOwners.setComponentPopupMenu(popUpOwners);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredRightHolderList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, eLProperty, tableOwners);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fullName}"));
        columnBinding.setColumnName("Full Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${selectedRightHolder}"), tableOwners, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tableOwners);
        tableOwners.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("LeasePanel.tableOwners.columnModel.title0")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        groupPanel2.setTitleText(bundle.getString("SimpleOwhershipPanel.groupPanel2.titleText")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
            .addComponent(documentsManagementPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentsManagementPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

        jPanel14.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${otherRightholderName}"), txtLandholder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblLandholder.setText(bundle.getString("LeasePanel.lblLandholder.text")); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLandholder, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
            .addComponent(txtLandholder)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addComponent(lblLandholder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLandholder)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel17);

        lblNotationText.setText(bundle.getString("SimpleOwhershipPanel.jLabel3.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${notation.notationText}"), txtNotationText, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNotationText, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
            .addComponent(txtNotationText)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(lblNotationText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNotationText)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel15);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPayments.addTab(bundle.getString("LeasePanel.jPanel11.TabConstraints.tabTitle"), jPanel11); // NOI18N

        tableLeaseConditions.setComponentPopupMenu(leaseConditionsPopUp);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${conditionsFilteredList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, eLProperty, tableLeaseConditions);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${conditionType.displayValue}"));
        columnBinding.setColumnName("Condition Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${conditionText}"));
        columnBinding.setColumnName("Condition Text");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${customCondition}"));
        columnBinding.setColumnName("Custom Condition");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${selectedCondition}"), tableLeaseConditions, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(tableLeaseConditions);
        tableLeaseConditions.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableLeaseConditions.getColumnModel().getColumn(0).setMaxWidth(200);
        tableLeaseConditions.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("LeasePanel.tableLeaseConditions.columnModel.title2_2")); // NOI18N
        tableLeaseConditions.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("LeasePanel.tableLeaseConditions.columnModel.title0_2")); // NOI18N
        tableLeaseConditions.getColumnModel().getColumn(1).setCellRenderer(new TableCellTextAreaRenderer());
        tableLeaseConditions.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableLeaseConditions.getColumnModel().getColumn(2).setMaxWidth(100);
        tableLeaseConditions.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("LeasePanel.tableLeaseConditions.columnModel.title1_2")); // NOI18N
        tableLeaseConditions.getColumnModel().getColumn(2).setCellRenderer(new BooleanCellRenderer2());

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAddCustomCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddCustomCondition.setText(bundle.getString("LeasePanel.btnAddCustomCondition.text")); // NOI18N
        btnAddCustomCondition.setFocusable(false);
        btnAddCustomCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomConditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddCustomCondition);

        btnEditCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEditCondition.setText(bundle.getString("LeasePanel.btnEditCondition.text")); // NOI18N
        btnEditCondition.setFocusable(false);
        btnEditCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditConditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnEditCondition);

        btnRemoveCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveCondition.setText(bundle.getString("LeasePanel.btnRemoveCondition.text")); // NOI18N
        btnRemoveCondition.setFocusable(false);
        btnRemoveCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveConditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRemoveCondition);
        jToolBar3.add(jSeparator5);
        jToolBar3.add(filler3);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${leaseConditionList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, conditionTypes, eLProperty, cbxStandardConditions);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, conditionTypes, org.jdesktop.beansbinding.ELProperty.create("${selectedConditionType}"), cbxStandardConditions, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jToolBar3.add(cbxStandardConditions);
        jToolBar3.add(filler2);

        btnAddStandardCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddStandardCondition.setText(bundle.getString("LeasePanel.btnAddStandardCondition.text")); // NOI18N
        btnAddStandardCondition.setFocusable(false);
        btnAddStandardCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStandardConditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddStandardCondition);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPayments.addTab(bundle.getString("LeasePanel.jPanel12.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif")), jPanel12); // NOI18N

        jPanel4.setLayout(new java.awt.GridLayout(2, 4, 15, 3));

        lblReceiptRef.setText(bundle.getString("LeasePanel.lblReceiptRef.text")); // NOI18N
        jPanel4.add(lblReceiptRef);

        lblReceiptDate.setText(bundle.getString("LeasePanel.lblReceiptDate.text")); // NOI18N
        jPanel4.add(lblReceiptDate);

        jLabel2.setText(bundle.getString("LeasePanel.jLabel2.text")); // NOI18N
        jPanel4.add(jLabel2);

        lblDueDate.setText(bundle.getString("LeasePanel.lblDueDate.text")); // NOI18N
        jPanel4.add(lblDueDate);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${receiptReference}"), txtReceiptRef, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jPanel4.add(txtReceiptRef);

        txtReceiptDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtReceiptDate.setText(bundle.getString("LeasePanel.txtReceiptDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${receiptDate}"), txtReceiptDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnReceiptDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnReceiptDate.setText(bundle.getString("LeasePanel.btnReceiptDate.text")); // NOI18N
        btnReceiptDate.setBorder(null);
        btnReceiptDate.setBorderPainted(false);
        btnReceiptDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiptDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(txtReceiptDate, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReceiptDate)
                .addGap(0, 0, 0))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReceiptDate)
            .addComponent(txtReceiptDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.add(jPanel18);

        txtPaymentAmount.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        txtPaymentAmount.setText(bundle.getString("LeasePanel.txtPaymentAmount.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${receiptAmount}"), txtPaymentAmount, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jPanel4.add(txtPaymentAmount);

        btnDueDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDueDate.setText(bundle.getString("LeasePanel.btnDueDate.text")); // NOI18N
        btnDueDate.setBorder(null);
        btnDueDate.setBorderPainted(false);
        btnDueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDueDateActionPerformed(evt);
            }
        });

        txtDueDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtDueDate.setText(bundle.getString("LeasePanel.txtDueDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${dueDate}"), txtDueDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(txtDueDate, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDueDate))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnDueDate)
        );

        jPanel4.add(jPanel19);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jTableWithDefaultStyles1.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${paymentHistoryList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, paymentHistoryListBean, eLProperty, jTableWithDefaultStyles1);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptReference}"));
        columnBinding.setColumnName("Receipt Reference");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptDate}"));
        columnBinding.setColumnName("Receipt Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptAmount}"));
        columnBinding.setColumnName("Receipt Amount");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nextPaymentDate}"));
        columnBinding.setColumnName("Next Payment Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${changeUser}"));
        columnBinding.setColumnName("Change User");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(jTableWithDefaultStyles1);
        jTableWithDefaultStyles1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("LeasePanel.jTableWithDefaultStyles1.columnModel.title0_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("LeasePanel.jTableWithDefaultStyles1.columnModel.title1_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(1).setCellRenderer(new DateTimeRenderer());
        jTableWithDefaultStyles1.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("LeasePanel.jTableWithDefaultStyles1.columnModel.title2_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(2).setCellRenderer(new MoneyCellRenderer());
        jTableWithDefaultStyles1.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("LeasePanel.jTableWithDefaultStyles1.columnModel.title3_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(3).setCellRenderer(new DateTimeRenderer());
        jTableWithDefaultStyles1.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("LeasePanel.jTableWithDefaultStyles1.columnModel.title4")); // NOI18N

        groupPanel3.setTitleText(bundle.getString("LeasePanel.groupPanel3.titleText")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(groupPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPayments.addTab(bundle.getString("LeasePanel.jPanel10.TabConstraints.tabTitle"), jPanel10); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabPayments, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOwnerActionPerformed
        addOwner();
    }//GEN-LAST:event_btnAddOwnerActionPerformed

    private void btnEditOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditOwnerActionPerformed
        editOwner();
    }//GEN-LAST:event_btnEditOwnerActionPerformed

    private void btnRemoveOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOwnerActionPerformed
        removeOwner();
    }//GEN-LAST:event_btnRemoveOwnerActionPerformed

    private void btnViewOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewOwnerActionPerformed
        viewOwner();
    }//GEN-LAST:event_btnViewOwnerActionPerformed

    private void menuAddOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddOwnerActionPerformed
        addOwner();
    }//GEN-LAST:event_menuAddOwnerActionPerformed

    private void menuEditOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditOwnerActionPerformed
        editOwner();
    }//GEN-LAST:event_menuEditOwnerActionPerformed

    private void menuRemoveOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveOwnerActionPerformed
        removeOwner();
    }//GEN-LAST:event_menuRemoveOwnerActionPerformed

    private void menuViewOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuViewOwnerActionPerformed
        viewOwner();
    }//GEN-LAST:event_menuViewOwnerActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveRrr();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSelectExistingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectExistingActionPerformed
        openSelectRightHolderForm();
    }//GEN-LAST:event_btnSelectExistingActionPerformed

    private void btnAddCustomConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomConditionActionPerformed
        addCustomCondition();
    }//GEN-LAST:event_btnAddCustomConditionActionPerformed

    private void menuAddCustomConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddCustomConditionActionPerformed
        addCustomCondition();
    }//GEN-LAST:event_menuAddCustomConditionActionPerformed

    private void btnEditConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditConditionActionPerformed
        editCustomCondition();
    }//GEN-LAST:event_btnEditConditionActionPerformed

    private void menuEditConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditConditionActionPerformed
        editCustomCondition();
    }//GEN-LAST:event_menuEditConditionActionPerformed

    private void btnRemoveConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveConditionActionPerformed
        removeCondition();
    }//GEN-LAST:event_btnRemoveConditionActionPerformed

    private void menuRemoveConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveConditionActionPerformed
        removeCondition();
    }//GEN-LAST:event_menuRemoveConditionActionPerformed

    private void btnAddStandardConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStandardConditionActionPerformed
        addStandardCondition();
    }//GEN-LAST:event_btnAddStandardConditionActionPerformed

    private void btnPrintRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintRejectionActionPerformed
        printRejectionLetter();
    }//GEN-LAST:event_btnPrintRejectionActionPerformed

    private void btnPrintDraftOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDraftOfferActionPerformed
        printOfferLetter(true);
    }//GEN-LAST:event_btnPrintDraftOfferActionPerformed

    private void btnPrintOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintOfferActionPerformed
        printOfferLetter(false);
    }//GEN-LAST:event_btnPrintOfferActionPerformed

    private void btnPrintDraftLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDraftLeaseActionPerformed
        printLease(true);
    }//GEN-LAST:event_btnPrintDraftLeaseActionPerformed

    private void btnPrintLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLeaseActionPerformed
        printLease(false);
    }//GEN-LAST:event_btnPrintLeaseActionPerformed

    private void btnExpirationDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpirationDateActionPerformed
        showCalendar(txtExpirationDate);
    }//GEN-LAST:event_btnExpirationDateActionPerformed

    private void btnStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartDateActionPerformed
       showCalendar(txtStartDate);
    }//GEN-LAST:event_btnStartDateActionPerformed

    private void btnRegDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegDateActionPerformed
        showCalendar(txtRegistrationDate);
    }//GEN-LAST:event_btnRegDateActionPerformed

    private void btnReceiptDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiptDateActionPerformed
        showCalendar(txtReceiptDate);
    }//GEN-LAST:event_btnReceiptDateActionPerformed

    private void btnDueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDueDateActionPerformed
        showCalendar(txtDueDate);
    }//GEN-LAST:event_btnDueDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCustomCondition;
    private javax.swing.JButton btnAddOwner;
    private javax.swing.JButton btnAddStandardCondition;
    private javax.swing.JButton btnDueDate;
    private javax.swing.JButton btnEditCondition;
    private javax.swing.JButton btnEditOwner;
    private javax.swing.JButton btnExpirationDate;
    private javax.swing.JButton btnPrintDraftLease;
    private javax.swing.JButton btnPrintDraftOffer;
    private javax.swing.JButton btnPrintLease;
    private javax.swing.JButton btnPrintOffer;
    private javax.swing.JButton btnPrintRejection;
    private javax.swing.JButton btnReceiptDate;
    private javax.swing.JButton btnRegDate;
    private javax.swing.JButton btnRemoveCondition;
    private javax.swing.JButton btnRemoveOwner;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelectExisting;
    private javax.swing.JButton btnStartDate;
    private javax.swing.JButton btnViewOwner;
    private javax.swing.JComboBox cbxStandardConditions;
    private org.sola.clients.beans.referencedata.ConditionTypeListBean conditionTypes;
    private org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel documentsManagementPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.GroupPanel groupPanel3;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel lblDueDate;
    private javax.swing.JLabel lblExpirationDate;
    private javax.swing.JLabel lblLandholder;
    private javax.swing.JLabel lblNotationText;
    private javax.swing.JLabel lblReceiptDate;
    private javax.swing.JLabel lblReceiptRef;
    private javax.swing.JLabel lblRegistrationDate;
    private javax.swing.JLabel lblRegistryNumber;
    private javax.swing.JLabel lblRent;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTerm;
    private javax.swing.JPopupMenu leaseConditionsPopUp;
    private javax.swing.JMenuItem menuAddCustomCondition;
    private javax.swing.JMenuItem menuAddOwner;
    private javax.swing.JMenuItem menuEditCondition;
    private javax.swing.JMenuItem menuEditOwner;
    private javax.swing.JMenuItem menuRemoveCondition;
    private javax.swing.JMenuItem menuRemoveOwner;
    private javax.swing.JMenuItem menuViewOwner;
    private org.sola.clients.beans.administrative.RrrPaymentHistoryListBean paymentHistoryListBean;
    private javax.swing.JPanel pnlStartDate;
    private javax.swing.JPopupMenu popUpOwners;
    private org.sola.clients.beans.administrative.RrrBean rrrBean;
    private javax.swing.JTabbedPane tabPayments;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableLeaseConditions;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableOwners;
    private javax.swing.JFormattedTextField txtDueDate;
    private javax.swing.JFormattedTextField txtExpirationDate;
    private javax.swing.JTextField txtLandholder;
    private javax.swing.JTextField txtNotationText;
    private javax.swing.JFormattedTextField txtPaymentAmount;
    private javax.swing.JFormattedTextField txtReceiptDate;
    private javax.swing.JTextField txtReceiptRef;
    private javax.swing.JFormattedTextField txtRegistrationDate;
    private javax.swing.JTextField txtRegistrationNumber;
    private javax.swing.JFormattedTextField txtRent;
    private javax.swing.JFormattedTextField txtStartDate;
    private javax.swing.JFormattedTextField txtTerm;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
