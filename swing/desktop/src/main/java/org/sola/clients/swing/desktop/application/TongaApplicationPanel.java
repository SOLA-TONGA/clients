/**
 * ******************************************************************************************
 * Copyright (C) 2013 - Food and Agriculture Organization of the United Nations
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
package org.sola.clients.swing.desktop.application;

import org.sola.clients.swing.common.utils.FormattersFactory;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationDocumentsHelperBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.party.PartySummaryListBean;
import org.sola.clients.beans.referencedata.*;
import org.sola.clients.beans.report.ApplicationReportBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.controls.AutoCompletion;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.converters.BigDecimalMoneyConverter;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.DashBoardPanel;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.administrative.BaUnitSearchPanel;
import org.sola.clients.swing.desktop.administrative.PropertyHelper;
import org.sola.clients.swing.desktop.reports.SysRegCertParamsForm;
import org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForApplicationLocation;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.HeaderPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.administrative.TongaBaUnitSearchPanel;
import org.sola.clients.swing.ui.renderers.*;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.clients.swing.ui.validation.ValidationResultForm;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.casemanagement.ApplicationTO;

/**
 * This form is used to create new application or edit existing one. <p>The
 * following list of beans is used to bind the data on the form:<br />
 * {@link ApplicationBean}, <br />{@link RequestTypeListBean}, <br />
 * {@link PartySummaryListBean}, <br />{@link CommunicationTypeListBean}, <br />
 * {@link SourceTypeListBean}, <br />{@link ApplicationDocumentsHelperBean}</p>
 */
public class TongaApplicationPanel extends ContentPanel {

    private ControlsBundleForApplicationLocation mapControl = null;
    public static final String APPLICATION_SAVED_PROPERTY = "applicationSaved";
    private String applicationID;
    private boolean isDashboard = false;
    private PropertyChangeListener findPropertyListener;
    private PropertyChangeListener addPropertyListener;

    /**
     * This method is used by the form designer to create
     * {@link ApplicationBean}. It uses
     * <code>applicationId</code> parameter passed to the form constructor.<br
     * />
     * <code>applicationId</code> should be initialized before
     * {@link ApplicationForm#initComponents} method call.
     */
    private ApplicationBean getApplicationBean() {
        if (appBean == null) {
            if (applicationID != null && !applicationID.equals("")) {
                ApplicationTO applicationTO = WSManager.getInstance().getCaseManagementService().getApplication(applicationID);
                appBean = TypeConverters.TransferObjectToBean(applicationTO, ApplicationBean.class, null);
            } else {
                appBean = new ApplicationBean();
            }
        }

        appBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationBean.APPLICATION_PROPERTY)) {
                    firePropertyChange(ApplicationBean.APPLICATION_PROPERTY, evt.getOldValue(), evt.getNewValue());
                }
            }
        });
        return appBean;
    }

    private DocumentsManagementExtPanel createDocumentsPanel() {
        if (documentsPanel == null) {
            if (appBean != null) {
                documentsPanel = new DocumentsManagementExtPanel(
                        appBean.getSourceList(), null, appBean.isEditingAllowed());
            } else {
                documentsPanel = new DocumentsManagementExtPanel();
            }
        }
        return documentsPanel;
    }

    private CommunicationTypeListBean createCommunicationTypes() {
        if (communicationTypes == null) {
            String communicationCode = null;
            if (appBean != null && appBean.getContactPerson() != null
                    && appBean.getContactPerson().getPreferredCommunicationCode() != null) {
                communicationCode = appBean.getContactPerson().getPreferredCommunicationCode();

            }
            communicationTypes = new CommunicationTypeListBean(true, communicationCode);
        }
        return communicationTypes;
    }

    /**
     * Default constructor to create new application.
     */
    public TongaApplicationPanel() {
        this((String) null);
    }

    /**
     * This constructor is used to open existing application for editing.
     *
     * @param applicationId ID of application to open.
     */
    public TongaApplicationPanel(String applicationId) {
        this.applicationID = applicationId;
        initComponents();
        postInit();
    }

    /**
     * This constructor is used to open existing application for editing.
     *
     * @param applicationId ID of application to open.
     */
    public TongaApplicationPanel(String applicationId, Boolean dashBoard) {
        this.applicationID = applicationId;
        this.isDashboard = dashBoard;
        initComponents();
        postInit();
    }

    /**
     * This constructor is used to open existing application for editing.
     *
     * @param application {@link ApplicationBean} to show on the form.
     */
    public TongaApplicationPanel(ApplicationBean application) {
        this.appBean = application;
        initComponents();
        postInit();
    }

    /**
     * Runs post initialization actions to customize form elements.
     */
    private void postInit() {
        appBean.getSourceFilteredList().addObservableListListener(new ObservableListListener() {
            @Override
            public void listElementsAdded(ObservableList ol, int i, int i1) {
                applicationDocumentsHelper.verifyCheckList(appBean.getSourceList().getFilteredList());
            }

            @Override
            public void listElementsRemoved(ObservableList ol, int i, List list) {
                applicationDocumentsHelper.verifyCheckList(appBean.getSourceList().getFilteredList());
            }

            @Override
            public void listElementReplaced(ObservableList ol, int i, Object o) {
            }

            @Override
            public void listElementPropertyChanged(ObservableList ol, int i) {
            }
        });

        appBean.getServiceList().addObservableListListener(new ObservableListListener() {
            @Override
            public void listElementsAdded(ObservableList ol, int i, int i1) {
                applicationDocumentsHelper.updateCheckList(appBean.getServiceList(), appBean.getSourceList());
            }

            @Override
            public void listElementsRemoved(ObservableList ol, int i, List list) {
                applicationDocumentsHelper.updateCheckList(appBean.getServiceList(), appBean.getSourceList());
            }

            @Override
            public void listElementReplaced(ObservableList ol, int i, Object o) {
                customizeServicesButtons();
            }

            @Override
            public void listElementPropertyChanged(ObservableList ol, int i) {
                customizeServicesButtons();
            }
        });

        appBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationBean.SELECTED_SERVICE_PROPERTY)) {
                    customizeServicesButtons();
                } else if (evt.getPropertyName().equals(ApplicationBean.STATUS_TYPE_PROPERTY)) {
                    customizeApplicationForm();
                    customizeServicesButtons();
                    customizePropertyButtons();
                } else if (evt.getPropertyName().equals(ApplicationBean.SELECTED_PROPERTY_PROPERTY)) {
                    customizePropertyButtons();
                } else if (evt.getPropertyName().equals(ApplicationBean.FEE_PAID_PROPERTY)) {
                    setDefaultFeePaidAmount();
                }
            }
        });

        customizeServicesButtons();
        customizeApplicationForm();
        customizePropertyButtons();
    }

    /**
     * Sets the amount paid value when the Paid checkbox is set.
     */
    private void setDefaultFeePaidAmount() {
        if (appBean.isFeePaid() && appBean.getTotalAmountPaid() != null
                && BigDecimal.ZERO.compareTo(appBean.getTotalAmountPaid()) == 0) {
            appBean.setTotalAmountPaid(appBean.getTotalFee());
        }
    }

    /**
     * Applies customization of form, based on Application status.
     */
    private void customizeApplicationForm() {
        if (appBean != null && !appBean.isNew()) {
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle");
            pnlHeader.setTitleText(bundle.getString("ApplicationPanel.pnlHeader.titleText") + " #" + appBean.getNr());
            applicationDocumentsHelper.updateCheckList(appBean.getServiceList(), appBean.getSourceList());
            appBean.loadApplicationLogList();
            if (appBean.getContactPerson() != null
                    && appBean.getContactPerson().getPreferredCommunicationCode() == null) {
                cbxCommunicationWay.setSelectedIndex(-1);
            }
            tabbedControlMain.addTab(bundle.getString("ApplicationPanel.validationPanel.TabConstraints.tabTitle"), validationPanel);
            tabbedControlMain.addTab(bundle.getString("ApplicationPanel.historyPanel.TabConstraints.tabTitle"), historyPanel);
            btnValidate.setEnabled(true);
        } else {
            cbxAgents.requestFocus(true);
            tabbedControlMain.removeTabAt(tabbedControlMain.indexOfComponent(historyPanel));
            tabbedControlMain.removeTabAt(tabbedControlMain.indexOfComponent(validationPanel));
            btnValidate.setEnabled(false);
        }

        menuApprove.setEnabled(appBean.canApprove()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_APPROVE));
        menuCancel.setEnabled(appBean.canCancel()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_REJECT));
        menuArchive.setEnabled(appBean.canArchive()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_ARCHIVE));
        menuDispatch.setEnabled(appBean.canDespatch()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_DISPATCH));
        menuRequisition.setEnabled(appBean.canRequisition()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_REQUISITE));
        menuResubmit.setEnabled(appBean.canResubmit()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_RESUBMIT));
        menuLapse.setEnabled(appBean.canLapse()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_WITHDRAW));
        menuWithdraw.setEnabled(appBean.canWithdraw()
                && SecurityBean.isInRole(RolesConstants.APPLICATION_WITHDRAW));

        if (btnValidate.isEnabled()) {
            btnValidate.setEnabled(appBean.canValidate()
                    && SecurityBean.isInRole(RolesConstants.APPLICATION_VALIDATE));
        }

        if (appBean.getStatusCode() != null) {
            boolean editAllowed = appBean.isEditingAllowed()
                    && SecurityBean.isInRole(RolesConstants.APPLICATION_EDIT_APPS);
            btnSave.setEnabled(editAllowed);
            btnCalculateFee.setEnabled(editAllowed);
            btnValidate.setEnabled(editAllowed);
            cbxPaid.setEnabled(editAllowed);
            txtFirstName.setEditable(editAllowed);
            txtLastName.setEditable(editAllowed);
            txtAddress.setEditable(editAllowed);
            txtEmail.setEditable(editAllowed);
            txtPhone.setEditable(editAllowed);
            txtAlias.setEditable(editAllowed);
            cbxCommunicationWay.setEnabled(editAllowed);
            cbxAgents.setEnabled(editAllowed);
            btnCertificate.setEnabled(false);
            documentsPanel.setAllowEdit(editAllowed);
            if (appBean.getStatusCode().equals("approved")) {
                btnCertificate.setEnabled(false);
            }
        } else {
            if (!SecurityBean.isInRole(RolesConstants.APPLICATION_CREATE_APPS)) {
                btnSave.setEnabled(false);
            }
            btnCertificate.setEnabled(false);
        }

        menuPrintStatusReport.setEnabled(false);
        menuPrintApplicationForm.setEnabled(false);
        if (appBean.isLodged() || appBean.isRequisitioned()) {
            // Enable the Print option
            menuPrintStatusReport.setEnabled(true);
            if (appBean.hasService(RequestTypeBean.CODE_REGISTER_LEASE)
                    || appBean.hasService(RequestTypeBean.CODE_REGISTER_SUBLEASE)) {
                {
                    menuPrintApplicationForm.setEnabled(true);
                }
            }
        }

        //if (!SecurityBean.isInRole(RolesConstants.GIS_VIEW_MAP) &&
        if (tabbedControlMain.indexOfComponent(mapPanel) >= 0) {
            // Hide the map panel on SOLA Tonga. 
            tabbedControlMain.removeTabAt(tabbedControlMain.indexOfComponent(mapPanel));
        }
        saveAppState();
    }

    /**
     * Disables or enables buttons, related to the services list management.
     */
    private void customizeServicesButtons() {
        ApplicationServiceBean selectedService = appBean.getSelectedService();
        boolean servicesManagementAllowed = appBean.isManagementAllowed();
        boolean enableServicesButtons = appBean.isEditingAllowed();

        if (enableServicesButtons) {
            if (applicationID != null && applicationID.length() > 0) {
                enableServicesButtons = SecurityBean.isInRole(RolesConstants.APPLICATION_EDIT_APPS);
            } else {
                enableServicesButtons = SecurityBean.isInRole(RolesConstants.APPLICATION_CREATE_APPS);
            }
        }

        // Customize services list buttons
        btnAddService.setEnabled(enableServicesButtons);
        btnRemoveService.setEnabled(false);
        btnUPService.setEnabled(false);
        btnDownService.setEnabled(false);

        if (enableServicesButtons) {
            if (selectedService != null) {
                if (selectedService.isLodged()) {
                    btnRemoveService.setEnabled(true);
                    btnUPService.setEnabled(true);
                    btnDownService.setEnabled(true);
                } else {
                    btnRemoveService.setEnabled(false);
                    btnUPService.setEnabled(selectedService.isManagementAllowed());
                    btnDownService.setEnabled(selectedService.isManagementAllowed());
                }

                if (btnUPService.isEnabled()
                        && appBean.getServiceList().getFilteredList().indexOf(selectedService) == 0) {
                    btnUPService.setEnabled(false);
                }
                if (btnDownService.isEnabled()
                        && appBean.getServiceList().getFilteredList().indexOf(selectedService)
                        == appBean.getServiceList().getFilteredList().size() - 1) {
                    btnDownService.setEnabled(false);
                }
            }
        }

        // Customize service management buttons
        btnCompleteService.setEnabled(false);
        btnCancelService.setEnabled(false);
        btnStartService.setEnabled(false);
        btnViewService.setEnabled(false);
        btnRevertService.setEnabled(false);

        if (selectedService != null) {
            // Tonga customization - allow the details of the service to be viewed
            btnViewService.setEnabled(!selectedService.isNew());
        }

        if (servicesManagementAllowed) {
            if (selectedService != null) {
                boolean enableServiceAction = selectedService.isManagementAllowed()
                        && SecurityBean.getCurrentUser().getId().equals(appBean.getAssigneeId());
                btnCancelService.setEnabled(enableServiceAction
                        && SecurityBean.isInRole(RolesConstants.APPLICATION_SERVICE_CANCEL)
                        && SecurityBean.isInRole(selectedService.getRequestTypeCode()));
                btnStartService.setEnabled(enableServiceAction
                        && SecurityBean.isInRole(RolesConstants.APPLICATION_SERVICE_START)
                        && SecurityBean.isInRole(selectedService.getRequestTypeCode()));
                String serviceStatus = selectedService.getStatusCode();

                if (serviceStatus != null && serviceStatus.equals(StatusConstants.COMPLETED)) {
                    btnCompleteService.setEnabled(false);
                    btnRevertService.setEnabled(enableServiceAction
                            && SecurityBean.isInRole(RolesConstants.APPLICATION_SERVICE_REVERT)
                            && SecurityBean.isInRole(selectedService.getRequestTypeCode()));
                } else {
                    btnCompleteService.setEnabled(enableServiceAction
                            && SecurityBean.isInRole(RolesConstants.APPLICATION_SERVICE_COMPLETE)
                            && SecurityBean.isInRole(selectedService.getRequestTypeCode()));
                    btnRevertService.setEnabled(false);
                }
            }
        }

        menuAddService.setEnabled(btnAddService.isEnabled());
        menuRemoveService.setEnabled(btnRemoveService.isEnabled());
        menuMoveServiceUp.setEnabled(btnUPService.isEnabled());
        menuMoveServiceDown.setEnabled(btnDownService.isEnabled());
        menuViewService.setEnabled(btnViewService.isEnabled());
        menuStartService.setEnabled(btnStartService.isEnabled());
        menuCompleteService.setEnabled(btnCompleteService.isEnabled());
        menuRevertService.setEnabled(btnRevertService.isEnabled());
        menuCancelService.setEnabled(btnCancelService.isEnabled());
    }

    /**
     * Disables or enables buttons, related to the property list management.
     */
    private void customizePropertyButtons() {
        boolean enable = appBean.isEditingAllowed() && btnSave.isEnabled();
        boolean enableWhenSelected = enable && appBean.getSelectedProperty() != null;
        btnAddAllotmentProperty.setEnabled(enable);
        btnAddLeaseProperty.setEnabled(enable);
        btnAddSubleaseProperty.setEnabled(enable);
        btnSearchProperty.setEnabled(enable);
        btnEditProperty.setEnabled(enableWhenSelected);
        btnRemoveProperty.setEnabled(enableWhenSelected);
        btnViewProperty.setEnabled(appBean.getSelectedProperty() != null);
        btnVerifyProperty.setEnabled(enableWhenSelected);
    }

    /**
     * This method is used by the form designer to create the list of nobles.
     */
    private PartySummaryListBean createAgentList() {
        PartySummaryListBean agentsList = new PartySummaryListBean();
        agentsList.FillAgents(true);
        return agentsList;
    }

    /**
     * This method is used by the form designer to create the list of nobles.
     */
    private PartySummaryListBean createNobleList() {
        PartySummaryListBean agentsList = new PartySummaryListBean();
        agentsList.FillNobles(true);
        return agentsList;
    }

    /**
     * Opens dialog form to display status change result for application or
     * service.
     */
    private void openValidationResultForm(List<ValidationResultBean> validationResultList,
            boolean isSuccess, String message) {
        ValidationResultForm resultForm = new ValidationResultForm(
                null, true, validationResultList, isSuccess, message);
        resultForm.setLocationRelativeTo(this);
        resultForm.setVisible(true);
    }

    /**
     * Checks if there are any changes on the form before proceeding with
     * action.
     */
    private boolean checkSaveBeforeAction() {
        if (MainForm.checkBeanState(appBean)) {
            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_SAVE_BEFORE_ACTION)
                    == MessageUtility.BUTTON_ONE) {
                if (checkApplication()) {
                    saveApplication();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates application
     */
    private void validateApplication() {
        if (!checkSaveBeforeAction()) {
            return;
        }

        if (appBean.getId() != null) {
            SolaTask t = new SolaTask() {
                @Override
                public Boolean doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_VALIDATING));
                    validationResultListBean.setValidationResultList(appBean.validate());
                    tabbedControlMain.setSelectedIndex(tabbedControlMain.indexOfComponent(validationPanel));
                    return true;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    private void launchService(final ApplicationServiceBean service, final boolean readOnly) {

        if (service != null) {
            // Create property change listener to refresh the state of the appBean
            // when the service form is closed. 
            final PropertyChangeListener refreshAppBeanOnClose = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(ContentPanel.CONTENT_PANEL_CLOSED)) {
                        appBean.reload();
                        customizeApplicationForm();
                        saveAppState();
                    }
                }
            };

            // Determine the form to open for the selected service
            if (ServiceLauncher.isServiceCategory(service.getRequestTypeCode(),
                    RequestCategoryTypeBean.CODE_APPLICATION_CATEGORY)) {
                // Use the service launcher to open the service panel for an application 
                // service. Add a listener to refresh the state of the appBean when the 
                // service panel is closed. 
                ServiceLauncher.launch(service.getRequestTypeCode(), getMainContentPanel(),
                        refreshAppBeanOnClose, null, appBean, service, readOnly);
            } else if (ServiceLauncher.isServiceCategory(service.getRequestTypeCode(),
                    RequestCategoryTypeBean.CODE_DOC_REGISTRATION_CATEGORY)) {
                // Use the service launcher to open the service panel for a document 
                // registration service. Add a listener to refresh the state of the 
                // appBean when the service panel is closed. 
                ServiceLauncher.launch(service.getRequestTypeCode(), getMainContentPanel(),
                        refreshAppBeanOnClose, null, appBean, service);
            } else {
                // Try to determine the BaUnit to use for the service. 
                BaUnitBean baUnit = PropertyHelper.getBaUnitBeanForService(appBean, service, this);

                if (baUnit != null) {
                    // Use the service launcher to open the property panel for the service.
                    ServiceLauncher.launch(service.getRequestTypeCode(), getMainContentPanel(),
                            refreshAppBeanOnClose, null, appBean, service, baUnit, readOnly);
                } else {
                    // Unalbe to launch service
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_NO_BA_UNIT_FOR_SERVICE);
                }
            }
        } else {
            MessageUtility.displayMessage(ClientMessage.APPLICATION_SELECT_SERVICE);
        }
    }

    private boolean saveApplication() {
        if (this.mapControl != null) {
            appBean.setLocation(this.mapControl.getApplicationLocation());
        }
        if (applicationID != null && !applicationID.equals("")) {
            return appBean.saveApplication();
        } else {
            return appBean.lodgeApplication();
        }
    }

    private boolean checkApplication() {
        if (appBean.validate(true).size() > 0) {
            return false;
        }

        if (applicationDocumentsHelper.isAllItemsChecked() == false) {
            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_NOTALL_DOCUMENT_REQUIRED) == MessageUtility.BUTTON_TWO) {
                return false;
            }
        }

        // Check how many properties needed 
        int nrPropRequired = 0;

        for (Iterator<ApplicationServiceBean> it = appBean.getServiceList().iterator(); it.hasNext();) {
            ApplicationServiceBean appService = it.next();
            for (Iterator<RequestTypeBean> it1 = CacheManager.getRequestTypes().iterator(); it1.hasNext();) {
                RequestTypeBean requestTypeBean = it1.next();
                if (requestTypeBean.getCode().equals(appService.getRequestTypeCode())) {
                    if (requestTypeBean.getNrPropertiesRequired() > nrPropRequired) {
                        nrPropRequired = requestTypeBean.getNrPropertiesRequired();
                    }
                    break;
                }
            }
        }

        String[] params = {"" + nrPropRequired};
        if (appBean.getPropertyList().size() < nrPropRequired) {
            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_ATLEAST_PROPERTY_REQUIRED, params) == MessageUtility.BUTTON_TWO) {
                return false;
            }
        }
        return true;
    }

    private void saveApplication(final boolean closeOnSave) {

        if (!checkApplication()) {
            return;
        }

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                saveApplication();
                if (closeOnSave) {
                    close();
                }
                return null;
            }

            @Override
            public void taskDone() {
                //MessageUtility.displayMessage(ClientMessage.APPLICATION_SUCCESSFULLY_SAVED);
                customizeApplicationForm();
                saveAppState();

                if (applicationID == null || applicationID.equals("")) {
                    if (appBean.getPropertyList().getFilteredList().size() > 0) {
                        appBean.getPropertyList().getFilteredList().get(0).setSurveyFee(
                                appBean.getTotalFeeForService(RequestTypeBean.CODE_SURVEY));
                    }

                    printLodgementNotice();

                    if (appBean.hasService(RequestTypeBean.CODE_REGISTER_LEASE) || appBean.hasService(RequestTypeBean.CODE_REGISTER_SUBLEASE)) {
                        // Only display the Lease Application Report when 
                        // registering a new lease or sublease
                        printLeaseApplicationReport();
                    }

                    applicationID = appBean.getId();
                }
                firePropertyChange(APPLICATION_SAVED_PROPERTY, false, true);

                refreshDashboard();

            }
        };

        TaskManager.getInstance().runTask(t);

    }

    @Override
    public void refreshDashboard() {
        if (HeaderPanel.CLOSE_BUTTON_CLICKED.equals(whichChangeEvent)) {
            DashBoardPanel dashboard = (DashBoardPanel) getMainContentPanel().getPanel(MainContentPanel.CARD_DASHBOARD);
            if (dashboard != null) {
                dashboard.refreshApplications();
            }
        }
    }

    /**
     * Designer generated code
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        appBean = getApplicationBean();
        partySummaryList = createAgentList();
        applicationDocumentsHelper = new org.sola.clients.beans.application.ApplicationDocumentsHelperBean();
        validationResultListBean = new org.sola.clients.beans.validation.ValidationResultListBean();
        popUpServices = new javax.swing.JPopupMenu();
        menuAddService = new javax.swing.JMenuItem();
        menuRemoveService = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuMoveServiceUp = new javax.swing.JMenuItem();
        menuMoveServiceDown = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        menuViewService = new javax.swing.JMenuItem();
        menuStartService = new javax.swing.JMenuItem();
        menuCompleteService = new javax.swing.JMenuItem();
        menuRevertService = new javax.swing.JMenuItem();
        menuCancelService = new javax.swing.JMenuItem();
        communicationTypes = createCommunicationTypes();
        popupApplicationActions = new javax.swing.JPopupMenu();
        menuApprove = new javax.swing.JMenuItem();
        menuCancel = new javax.swing.JMenuItem();
        menuWithdraw = new javax.swing.JMenuItem();
        menuLapse = new javax.swing.JMenuItem();
        menuRequisition = new javax.swing.JMenuItem();
        menuResubmit = new javax.swing.JMenuItem();
        menuDispatch = new javax.swing.JMenuItem();
        menuArchive = new javax.swing.JMenuItem();
        popupPrintAction = new javax.swing.JPopupMenu();
        menuPrintStatusReport = new javax.swing.JMenuItem();
        menuPrintApplicationForm = new javax.swing.JMenuItem();
        pnlHeader = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        btnCalculateFee = new javax.swing.JButton();
        btnValidate = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        printDropDown = new org.sola.clients.swing.common.controls.DropDownButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        dropDownButton1 = new org.sola.clients.swing.common.controls.DropDownButton();
        btnCertificate = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        tabbedControlMain = new javax.swing.JTabbedPane();
        contactPanel = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtAppNumber1 = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtItemNumber = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        txtDate = new javax.swing.JFormattedTextField();
        labDate = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        labStatus = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCompleteBy = new javax.swing.JFormattedTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtFirstName = new javax.swing.JTextField();
        labName = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        labLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtAddress = new javax.swing.JTextField();
        labAddress = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblAlias = new javax.swing.JLabel();
        txtAlias = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        labPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        labEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        labPreferredWay = new javax.swing.JLabel();
        cbxCommunicationWay = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        labAgents = new javax.swing.JLabel();
        cbxAgents = new javax.swing.JComboBox();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        tongaPropertyPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabProperty = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddAllotmentProperty = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnAddLeaseProperty = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnAddSubleaseProperty = new org.sola.clients.swing.common.buttons.BtnAdd();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        btnSearchProperty = new org.sola.clients.swing.common.buttons.BtnSearch();
        btnEditProperty = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemoveProperty = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnViewProperty = new org.sola.clients.swing.common.buttons.BtnView();
        btnVerifyProperty = new javax.swing.JButton();
        servicesPanel = new javax.swing.JPanel();
        scrollFeeDetails1 = new javax.swing.JScrollPane();
        tabServices = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        tbServices = new javax.swing.JToolBar();
        btnAddService = new javax.swing.JButton();
        btnRemoveService = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnUPService = new javax.swing.JButton();
        btnDownService = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnViewService = new javax.swing.JButton();
        btnStartService = new javax.swing.JButton();
        btnCompleteService = new javax.swing.JButton();
        btnRevertService = new javax.swing.JButton();
        btnCancelService = new javax.swing.JButton();
        documentPanel = new javax.swing.JPanel();
        labDocRequired = new javax.swing.JLabel();
        scrollDocRequired = new javax.swing.JScrollPane();
        tblDocTypesHelper = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        documentsPanel = createDocumentsPanel();
        mapPanel = new javax.swing.JPanel();
        feesPanel = new javax.swing.JPanel();
        scrollFeeDetails = new javax.swing.JScrollPane();
        tabFeeDetails = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel2 = new javax.swing.JPanel();
        formTxtServiceFee = new javax.swing.JFormattedTextField();
        formTxtTaxes = new javax.swing.JFormattedTextField();
        formTxtFee = new javax.swing.JFormattedTextField();
        labTotalFee2 = new javax.swing.JLabel();
        labTotalFee = new javax.swing.JLabel();
        labTotalFee1 = new javax.swing.JLabel();
        labFixedFee = new javax.swing.JLabel();
        formTxtReceiptRef = new javax.swing.JTextField();
        labReceiptRef = new javax.swing.JLabel();
        labTotalFee3 = new javax.swing.JLabel();
        cbxPaid = new javax.swing.JCheckBox();
        formTxtPaid = new javax.swing.JFormattedTextField();
        validationPanel = new javax.swing.JPanel();
        validationsPanel = new javax.swing.JScrollPane();
        tabValidations = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        historyPanel = new javax.swing.JPanel();
        actionLogPanel = new javax.swing.JScrollPane();
        tabActionLog = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        popUpServices.setName("popUpServices"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        menuAddService.setText(bundle.getString("TongaApplicationPanel.menuAddService.text")); // NOI18N
        menuAddService.setName("menuAddService"); // NOI18N
        menuAddService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuAddService);

        menuRemoveService.setText(bundle.getString("TongaApplicationPanel.menuRemoveService.text")); // NOI18N
        menuRemoveService.setName("menuRemoveService"); // NOI18N
        menuRemoveService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuRemoveService);

        jSeparator3.setName("jSeparator3"); // NOI18N
        popUpServices.add(jSeparator3);

        menuMoveServiceUp.setText(bundle.getString("TongaApplicationPanel.menuMoveServiceUp.text")); // NOI18N
        menuMoveServiceUp.setName("menuMoveServiceUp"); // NOI18N
        menuMoveServiceUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMoveServiceUpActionPerformed(evt);
            }
        });
        popUpServices.add(menuMoveServiceUp);

        menuMoveServiceDown.setText(bundle.getString("TongaApplicationPanel.menuMoveServiceDown.text")); // NOI18N
        menuMoveServiceDown.setName("menuMoveServiceDown"); // NOI18N
        menuMoveServiceDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMoveServiceDownActionPerformed(evt);
            }
        });
        popUpServices.add(menuMoveServiceDown);

        jSeparator4.setName("jSeparator4"); // NOI18N
        popUpServices.add(jSeparator4);

        menuViewService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        menuViewService.setText(bundle.getString("TongaApplicationPanel.menuViewService.text")); // NOI18N
        menuViewService.setName("menuViewService"); // NOI18N
        menuViewService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuViewServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuViewService);

        menuStartService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/start.png"))); // NOI18N
        menuStartService.setText(bundle.getString("TongaApplicationPanel.menuStartService.text")); // NOI18N
        menuStartService.setName("menuStartService"); // NOI18N
        menuStartService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStartServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuStartService);

        menuCompleteService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/confirm.png"))); // NOI18N
        menuCompleteService.setText(bundle.getString("TongaApplicationPanel.menuCompleteService.text")); // NOI18N
        menuCompleteService.setName("menuCompleteService"); // NOI18N
        menuCompleteService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompleteServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuCompleteService);

        menuRevertService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/revert.png"))); // NOI18N
        menuRevertService.setText(bundle.getString("TongaApplicationPanel.menuRevertService.text")); // NOI18N
        menuRevertService.setName("menuRevertService"); // NOI18N
        menuRevertService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRevertServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuRevertService);

        menuCancelService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/cancel.png"))); // NOI18N
        menuCancelService.setText(bundle.getString("TongaApplicationPanel.menuCancelService.text")); // NOI18N
        menuCancelService.setName("menuCancelService"); // NOI18N
        menuCancelService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCancelServiceActionPerformed(evt);
            }
        });
        popUpServices.add(menuCancelService);

        popupApplicationActions.setName("popupApplicationActions"); // NOI18N

        menuApprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/approve.png"))); // NOI18N
        menuApprove.setText(bundle.getString("TongaApplicationPanel.menuApprove.text")); // NOI18N
        menuApprove.setName("menuApprove"); // NOI18N
        menuApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuApproveActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuApprove);

        menuCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/reject.png"))); // NOI18N
        menuCancel.setText(bundle.getString("TongaApplicationPanel.menuCancel.text")); // NOI18N
        menuCancel.setName("menuCancel"); // NOI18N
        menuCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCancelActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuCancel);

        menuWithdraw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/withdraw.png"))); // NOI18N
        menuWithdraw.setText(bundle.getString("TongaApplicationPanel.menuWithdraw.text")); // NOI18N
        menuWithdraw.setName("menuWithdraw"); // NOI18N
        menuWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuWithdrawActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuWithdraw);

        menuLapse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lapse.png"))); // NOI18N
        menuLapse.setText(bundle.getString("TongaApplicationPanel.menuLapse.text")); // NOI18N
        menuLapse.setName("menuLapse"); // NOI18N
        menuLapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLapseActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuLapse);

        menuRequisition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/requisition.png"))); // NOI18N
        menuRequisition.setText(bundle.getString("TongaApplicationPanel.menuRequisition.text")); // NOI18N
        menuRequisition.setName("menuRequisition"); // NOI18N
        menuRequisition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRequisitionActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuRequisition);

        menuResubmit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/resubmit.png"))); // NOI18N
        menuResubmit.setText(bundle.getString("TongaApplicationPanel.menuResubmit.text")); // NOI18N
        menuResubmit.setName("menuResubmit"); // NOI18N
        menuResubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuResubmitActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuResubmit);

        menuDispatch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/envelope.png"))); // NOI18N
        menuDispatch.setText(bundle.getString("TongaApplicationPanel.menuDispatch.text")); // NOI18N
        menuDispatch.setName("menuDispatch"); // NOI18N
        menuDispatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDispatchActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuDispatch);

        menuArchive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/archive.png"))); // NOI18N
        menuArchive.setText(bundle.getString("TongaApplicationPanel.menuArchive.text")); // NOI18N
        menuArchive.setName("menuArchive"); // NOI18N
        menuArchive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchiveActionPerformed(evt);
            }
        });
        popupApplicationActions.add(menuArchive);

        popupPrintAction.setName("popupPrintAction"); // NOI18N

        menuPrintStatusReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        menuPrintStatusReport.setText(bundle.getString("TongaApplicationPanel.menuPrintStatusReport.text")); // NOI18N
        menuPrintStatusReport.setActionCommand(bundle.getString("TongaApplicationPanel.menuPrintStatusReport.actionCommand")); // NOI18N
        menuPrintStatusReport.setName("menuPrintStatusReport"); // NOI18N
        menuPrintStatusReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrintStatusReportActionPerformed(evt);
            }
        });
        popupPrintAction.add(menuPrintStatusReport);

        menuPrintApplicationForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        menuPrintApplicationForm.setText(bundle.getString("TongaApplicationPanel.menuPrintApplicationForm.text")); // NOI18N
        menuPrintApplicationForm.setActionCommand(bundle.getString("TongaApplicationPanel.menuPrintApplicationForm.actionCommand")); // NOI18N
        menuPrintApplicationForm.setName("menuPrintApplicationForm"); // NOI18N
        menuPrintApplicationForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrintApplicationFormActionPerformed(evt);
            }
        });
        popupPrintAction.add(menuPrintApplicationForm);

        setHeaderPanel(pnlHeader);
        setHelpTopic(bundle.getString("TongaApplicationPanel.helpTopic")); // NOI18N
        setMinimumSize(new java.awt.Dimension(660, 458));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(700, 520));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        pnlHeader.setName("pnlHeader"); // NOI18N
        pnlHeader.setTitleText(bundle.getString("TongaApplicationPanel.pnlHeader.titleText")); // NOI18N

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setName("jToolBar3"); // NOI18N

        LafManager.getInstance().setBtnProperties(btnSave);
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSave.setText(bundle.getString("TongaApplicationPanel.btnSave.text")); // NOI18N
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSave.setName("btnSave"); // NOI18N
        btnSave.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar3.add(btnSave);

        LafManager.getInstance().setBtnProperties(btnCalculateFee);
        btnCalculateFee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calculate.png"))); // NOI18N
        btnCalculateFee.setText(bundle.getString("TongaApplicationPanel.btnCalculateFee.text")); // NOI18N
        btnCalculateFee.setName("btnCalculateFee"); // NOI18N
        btnCalculateFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateFeeActionPerformed(evt);
            }
        });
        jToolBar3.add(btnCalculateFee);

        btnValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/validation.png"))); // NOI18N
        btnValidate.setText(bundle.getString("TongaApplicationPanel.btnValidate.text")); // NOI18N
        btnValidate.setName("btnValidate"); // NOI18N
        btnValidate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        btnValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidateActionPerformed(evt);
            }
        });
        jToolBar3.add(btnValidate);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar3.add(jSeparator6);

        printDropDown.setText(bundle.getString("TongaApplicationPanel.printDropDown.text")); // NOI18N
        printDropDown.setComponentPopupMenu(popupPrintAction);
        printDropDown.setFocusable(false);
        printDropDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printDropDown.setName("printDropDown"); // NOI18N
        printDropDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(printDropDown);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar3.add(jSeparator5);

        dropDownButton1.setText(bundle.getString("TongaApplicationPanel.dropDownButton1.text")); // NOI18N
        dropDownButton1.setActionCommand(bundle.getString("TongaApplicationPanel.dropDownButton1.actionCommand")); // NOI18N
        dropDownButton1.setComponentPopupMenu(popupApplicationActions);
        dropDownButton1.setFocusable(false);
        dropDownButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dropDownButton1.setName("dropDownButton1"); // NOI18N
        dropDownButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(dropDownButton1);
        dropDownButton1.getAccessibleContext().setAccessibleDescription(bundle.getString("TongaApplicationPanel.dropDownButton1.AccessibleContext.accessibleDescription")); // NOI18N

        btnCertificate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/approve1.png"))); // NOI18N
        btnCertificate.setText(bundle.getString("TongaApplicationPanel.btnCertificate.text")); // NOI18N
        btnCertificate.setFocusable(false);
        btnCertificate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnCertificate.setName(bundle.getString("ApplicationPanel.btnCertificate.name")); // NOI18N
        btnCertificate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCertificate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCertificateActionPerformed(evt);
            }
        });
        jToolBar3.add(btnCertificate);

        jSeparator7.setName(bundle.getString("ApplicationPanel.jSeparator7.name")); // NOI18N
        jToolBar3.add(jSeparator7);

        tabbedControlMain.setName("tabbedControlMain"); // NOI18N
        tabbedControlMain.setPreferredSize(new java.awt.Dimension(440, 370));
        tabbedControlMain.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        contactPanel.setName("contactPanel"); // NOI18N
        contactPanel.setPreferredSize(new java.awt.Dimension(645, 331));
        contactPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        contactPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactPanelMouseClicked(evt);
            }
        });

        jPanel25.setName("jPanel25"); // NOI18N
        jPanel25.setLayout(new java.awt.GridLayout(2, 3, 15, 0));

        jPanel28.setEnabled(false);
        jPanel28.setFocusable(false);
        jPanel28.setName("jPanel28"); // NOI18N
        jPanel28.setPreferredSize(new java.awt.Dimension(0, 59));
        jPanel28.setRequestFocusEnabled(false);

        jLabel4.setText(bundle.getString("TongaApplicationPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        txtAppNumber1.setEditable(false);
        txtAppNumber1.setEnabled(false);
        txtAppNumber1.setFocusable(false);
        txtAppNumber1.setName("txtAppNumber1"); // NOI18N
        txtAppNumber1.setRequestFocusEnabled(false);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${nr}"), txtAppNumber1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtAppNumber1)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAppNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel28);

        jPanel27.setEnabled(false);
        jPanel27.setFocusable(false);
        jPanel27.setName("jPanel27"); // NOI18N
        jPanel27.setPreferredSize(new java.awt.Dimension(0, 59));
        jPanel27.setRequestFocusEnabled(false);

        jLabel3.setText(bundle.getString("TongaApplicationPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtItemNumber.setEditable(false);
        txtItemNumber.setEnabled(false);
        txtItemNumber.setFocusable(false);
        txtItemNumber.setName("txtItemNumber"); // NOI18N
        txtItemNumber.setRequestFocusEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${itemNumber}"), txtItemNumber, org.jdesktop.beansbinding.BeanProperty.create("text"), "a");
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtItemNumber, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27);

        jPanel13.setEnabled(false);
        jPanel13.setFocusable(false);
        jPanel13.setName("jPanel13"); // NOI18N
        jPanel13.setPreferredSize(new java.awt.Dimension(0, 59));
        jPanel13.setRequestFocusEnabled(false);

        txtDate.setFormatterFactory(FormattersFactory.getInstance().getDateTimeFormatterFactory());
        txtDate.setText(bundle.getString("TongaApplicationPanel.txtDate.text")); // NOI18N
        txtDate.setEnabled(false);
        txtDate.setFocusable(false);
        txtDate.setName(bundle.getString("ApplicationPanel.txtDate.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${lodgingDatetime}"), txtDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        LafManager.getInstance().setLabProperties(labDate);
        labDate.setText(bundle.getString("TongaApplicationPanel.labDate.text")); // NOI18N
        labDate.setName("labDate"); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labDate, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtDate, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(labDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel13);

        jPanel15.setName("jPanel15"); // NOI18N
        jPanel15.setPreferredSize(new java.awt.Dimension(0, 59));

        LafManager.getInstance().setLabProperties(labStatus);
        labStatus.setText(bundle.getString("TongaApplicationPanel.labStatus.text")); // NOI18N
        labStatus.setName("labStatus"); // NOI18N

        txtStatus.setEnabled(false);
        txtStatus.setName("txtStatus"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"), txtStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtStatus.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtStatus.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtStatus)
            .addComponent(labStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(labStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel25.add(jPanel15);

        jPanel26.setEnabled(false);
        jPanel26.setFocusable(false);
        jPanel26.setName("jPanel26"); // NOI18N
        jPanel26.setPreferredSize(new java.awt.Dimension(0, 59));
        jPanel26.setRequestFocusEnabled(false);

        jLabel2.setText(bundle.getString("TongaApplicationPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtCompleteBy.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtCompleteBy.setText(bundle.getString("TongaApplicationPanel.txtCompleteBy.text")); // NOI18N
        txtCompleteBy.setEnabled(false);
        txtCompleteBy.setFocusable(false);
        txtCompleteBy.setName(bundle.getString("ApplicationPanel.txtCompleteBy.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${expectedCompletionDate}"), txtCompleteBy, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtCompleteBy)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCompleteBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel26);

        jPanel12.setName("jPanel12"); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        jPanel3.setName("jPanel3"); // NOI18N

        txtFirstName.setName("txtFirstName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.name}"), txtFirstName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        LafManager.getInstance().setTxtProperties(txtFirstName);

        LafManager.getInstance().setLabProperties(labName);
        labName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        labName.setLabelFor(txtFirstName);
        labName.setText(bundle.getString("TongaApplicationPanel.labName.text")); // NOI18N
        labName.setIconTextGap(1);
        labName.setName("labName"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labName, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
            .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel3);

        jPanel4.setName("jPanel4"); // NOI18N

        LafManager.getInstance().setLabProperties(labLastName);
        labLastName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        labLastName.setText(bundle.getString("TongaApplicationPanel.labLastName.text")); // NOI18N
        labLastName.setIconTextGap(1);
        labLastName.setName("labLastName"); // NOI18N

        txtLastName.setName("txtLastName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.lastName}"), txtLastName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtLastName.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtLastName.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(labLastName)
                .addContainerGap(151, Short.MAX_VALUE))
            .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(labLastName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel4);

        jPanel5.setName("jPanel5"); // NOI18N

        txtAddress.setName("txtAddress"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.address.description}"), txtAddress, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtAddress.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtAddress.setHorizontalAlignment(JTextField.LEADING);

        LafManager.getInstance().setLabProperties(labAddress);
        labAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        labAddress.setText(bundle.getString("TongaApplicationPanel.labAddress.text")); // NOI18N
        labAddress.setIconTextGap(1);
        labAddress.setName("labAddress"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(labAddress)
                .addContainerGap(161, Short.MAX_VALUE))
            .addComponent(txtAddress)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(labAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel5);

        jPanel11.setName("jPanel11"); // NOI18N
        jPanel11.setLayout(new java.awt.GridLayout(2, 3, 15, 5));

        jPanel8.setName("jPanel8"); // NOI18N

        LafManager.getInstance().setLabProperties(lblAlias);
        lblAlias.setText(bundle.getString("TongaApplicationPanel.lblAlias.text")); // NOI18N
        lblAlias.setName("lblAlias"); // NOI18N

        txtAlias.setName("txtAlias"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.alias}"), txtAlias, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAlias, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtAlias)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblAlias, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel8);

        jPanel7.setName("jPanel7"); // NOI18N

        LafManager.getInstance().setLabProperties(labPhone);
        labPhone.setText(bundle.getString("TongaApplicationPanel.labPhone.text")); // NOI18N
        labPhone.setName("labPhone"); // NOI18N

        txtPhone.setName("txtPhone"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.phone}"), txtPhone, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtPhone.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtPhone.setHorizontalAlignment(JTextField.LEADING);
        txtPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhoneFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(labPhone)
                .addContainerGap(181, Short.MAX_VALUE))
            .addComponent(txtPhone, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(labPhone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel7);

        jPanel9.setName("jPanel9"); // NOI18N

        LafManager.getInstance().setLabProperties(labEmail);
        labEmail.setText(bundle.getString("TongaApplicationPanel.labEmail.text")); // NOI18N
        labEmail.setName("labEmail"); // NOI18N

        txtEmail.setName("txtEmail"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.email}"), txtEmail, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtEmail.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtEmail.setHorizontalAlignment(JTextField.LEADING);
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
            .addComponent(txtEmail)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel9);

        jPanel10.setName("jPanel10"); // NOI18N

        LafManager.getInstance().setLabProperties(labPreferredWay);
        labPreferredWay.setText(bundle.getString("TongaApplicationPanel.labPreferredWay.text")); // NOI18N
        labPreferredWay.setName("labPreferredWay"); // NOI18N

        LafManager.getInstance().setCmbProperties(cbxCommunicationWay);
        cbxCommunicationWay.setMaximumRowCount(9);
        cbxCommunicationWay.setName("cbxCommunicationWay"); // NOI18N
        cbxCommunicationWay.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${communicationTypeList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, communicationTypes, eLProperty, cbxCommunicationWay);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${contactPerson.preferredCommunication}"), cbxCommunicationWay, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxCommunicationWay.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(labPreferredWay)
                .addContainerGap(92, Short.MAX_VALUE))
            .addComponent(cbxCommunicationWay, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(labPreferredWay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxCommunicationWay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel10);

        jPanel14.setMinimumSize(new java.awt.Dimension(28, 20));
        jPanel14.setName("jPanel14"); // NOI18N

        LafManager.getInstance().setLabProperties(labAgents);
        labAgents.setText(bundle.getString("TongaApplicationPanel.labAgents.text")); // NOI18N
        labAgents.setIconTextGap(1);
        labAgents.setName("labAgents"); // NOI18N

        LafManager.getInstance().setCmbProperties(cbxAgents);
        cbxAgents.setName("cbxAgents"); // NOI18N
        AutoCompletion.enable(cbxAgents);
        cbxAgents.setRenderer(new SimpleComboBoxRenderer("getFullName"));
        cbxAgents.setRequestFocusEnabled(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partySummaryList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partySummaryList, eLProperty, cbxAgents);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${agent}"), cbxAgents, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxAgents.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(labAgents)
                .addContainerGap(182, Short.MAX_VALUE))
            .addComponent(cbxAgents, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(labAgents)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxAgents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel14);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        groupPanel1.setName("groupPanel1"); // NOI18N
        groupPanel1.setTitleText(bundle.getString("TongaApplicationPanel.groupPanel1.titleText")); // NOI18N

        javax.swing.GroupLayout contactPanelLayout = new javax.swing.GroupLayout(contactPanel);
        contactPanel.setLayout(contactPanelLayout);
        contactPanelLayout.setHorizontalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE))
                .addContainerGap())
        );
        contactPanelLayout.setVerticalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.contactPanel.TabConstraints.tabTitle"), contactPanel); // NOI18N

        tongaPropertyPanel.setName("tongaPropertyPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tabProperty.setName("tabProperty"); // NOI18N
        tabProperty.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredPropertyList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, eLProperty, tabProperty);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${propertyType}"));
        columnBinding.setColumnName("Property Type");
        columnBinding.setColumnClass(org.sola.clients.beans.referencedata.BaUnitTypeBean.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${propertyNumber}"));
        columnBinding.setColumnName("Property Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightHolder}"));
        columnBinding.setColumnName("Right Holder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${town}"));
        columnBinding.setColumnName("Town");
        columnBinding.setColumnClass(org.sola.clients.beans.referencedata.TownBean.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${area}"));
        columnBinding.setColumnName("Area");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${verifiedExists}"));
        columnBinding.setColumnName("Verified Exists");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${selectedProperty}"), tabProperty, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(tabProperty);
        tabProperty.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabProperty.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title0")); // NOI18N
        tabProperty.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabProperty.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title1")); // NOI18N
        tabProperty.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title2")); // NOI18N
        tabProperty.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabProperty.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title3")); // NOI18N
        tabProperty.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabProperty.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title4")); // NOI18N
        tabProperty.getColumnModel().getColumn(4).setCellRenderer(new AreaCellRenderer(true));
        tabProperty.getColumnModel().getColumn(5).setPreferredWidth(40);
        tabProperty.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("TongaApplicationPanel.tabProperty.columnModel.title5")); // NOI18N
        tabProperty.getColumnModel().getColumn(5).setCellRenderer(new ExistingObjectCellRenderer());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnAddAllotmentProperty.setText(bundle.getString("TongaApplicationPanel.btnAddAllotmentProperty.text")); // NOI18N
        btnAddAllotmentProperty.setName("btnAddAllotmentProperty"); // NOI18N
        btnAddAllotmentProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddAllotmentProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAllotmentPropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddAllotmentProperty);

        btnAddLeaseProperty.setText(bundle.getString("TongaApplicationPanel.btnAddLeaseProperty.text")); // NOI18N
        btnAddLeaseProperty.setName("btnAddLeaseProperty"); // NOI18N
        btnAddLeaseProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddLeaseProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLeasePropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddLeaseProperty);

        btnAddSubleaseProperty.setText(bundle.getString("TongaApplicationPanel.btnAddSubleaseProperty.text")); // NOI18N
        btnAddSubleaseProperty.setName("btnAddSubleaseProperty"); // NOI18N
        btnAddSubleaseProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddSubleaseProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSubleasePropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddSubleaseProperty);

        jSeparator10.setName("jSeparator10"); // NOI18N
        jToolBar1.add(jSeparator10);

        btnSearchProperty.setName("btnSearchProperty"); // NOI18N
        btnSearchProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSearchProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSearchProperty);

        btnEditProperty.setName("btnEditProperty"); // NOI18N
        btnEditProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditProperty);

        btnRemoveProperty.setName("btnRemoveProperty"); // NOI18N
        btnRemoveProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemoveProperty);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jToolBar1.add(jSeparator8);

        btnViewProperty.setName("btnViewProperty"); // NOI18N
        btnViewProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnViewProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnViewProperty);

        btnVerifyProperty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/verify.png"))); // NOI18N
        btnVerifyProperty.setText(bundle.getString("TongaApplicationPanel.btnVerifyProperty.text")); // NOI18N
        btnVerifyProperty.setFocusable(false);
        btnVerifyProperty.setName("btnVerifyProperty"); // NOI18N
        btnVerifyProperty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVerifyProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerifyPropertyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnVerifyProperty);

        javax.swing.GroupLayout tongaPropertyPanelLayout = new javax.swing.GroupLayout(tongaPropertyPanel);
        tongaPropertyPanel.setLayout(tongaPropertyPanelLayout);
        tongaPropertyPanelLayout.setHorizontalGroup(
            tongaPropertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tongaPropertyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(tongaPropertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tongaPropertyPanelLayout.setVerticalGroup(
            tongaPropertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tongaPropertyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(tongaPropertyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tongaPropertyPanelLayout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.tongaPropertyPanel.TabConstraints.tabTitle"), tongaPropertyPanel); // NOI18N

        servicesPanel.setName("servicesPanel"); // NOI18N

        scrollFeeDetails1.setBackground(new java.awt.Color(255, 255, 255));
        scrollFeeDetails1.setName("scrollFeeDetails1"); // NOI18N
        scrollFeeDetails1.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        tabServices.setComponentPopupMenu(popUpServices);
        tabServices.setName("tabServices"); // NOI18N
        tabServices.setNextFocusableComponent(btnSave);
        tabServices.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        tabServices.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredServiceList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, eLProperty, tabServices);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${serviceOrder}"));
        columnBinding.setColumnName("Service Order");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${requestType}"));
        columnBinding.setColumnName("Request Type");
        columnBinding.setColumnClass(org.sola.clients.beans.referencedata.RequestTypeBean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${concatenatedName}"));
        columnBinding.setColumnName("Concatenated Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(org.sola.clients.beans.referencedata.ServiceStatusTypeBean.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${selectedService}"), tabServices, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        scrollFeeDetails1.setViewportView(tabServices);
        tabServices.getColumnModel().getColumn(0).setMinWidth(70);
        tabServices.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabServices.getColumnModel().getColumn(0).setMaxWidth(70);
        tabServices.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ApplicationPanel.tabFeeDetails1.columnModel.title0")); // NOI18N
        tabServices.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ApplicationPanel.tabFeeDetails1.columnModel.title1")); // NOI18N
        tabServices.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ApplicationPanel.tabServices.columnModel.title3")); // NOI18N
        tabServices.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("ApplicationPanel.tabFeeDetails1.columnModel.title2")); // NOI18N

        tbServices.setFloatable(false);
        tbServices.setRollover(true);
        tbServices.setName("tbServices"); // NOI18N

        btnAddService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddService.setText(bundle.getString("TongaApplicationPanel.btnAddService.text")); // NOI18N
        btnAddService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddService.setName("btnAddService"); // NOI18N
        btnAddService.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        btnAddService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnAddService);

        btnRemoveService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveService.setText(bundle.getString("TongaApplicationPanel.btnRemoveService.text")); // NOI18N
        btnRemoveService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveService.setName("btnRemoveService"); // NOI18N
        btnRemoveService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnRemoveService);

        jSeparator1.setName("jSeparator1"); // NOI18N
        tbServices.add(jSeparator1);

        btnUPService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/up.png"))); // NOI18N
        btnUPService.setText(bundle.getString("TongaApplicationPanel.btnUPService.text")); // NOI18N
        btnUPService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUPService.setName("btnUPService"); // NOI18N
        btnUPService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUPServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnUPService);

        btnDownService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/down.png"))); // NOI18N
        btnDownService.setText(bundle.getString("TongaApplicationPanel.btnDownService.text")); // NOI18N
        btnDownService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDownService.setName("btnDownService"); // NOI18N
        btnDownService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnDownService);

        jSeparator2.setName("jSeparator2"); // NOI18N
        tbServices.add(jSeparator2);

        btnViewService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnViewService.setText(bundle.getString("TongaApplicationPanel.btnViewService.text")); // NOI18N
        btnViewService.setFocusable(false);
        btnViewService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnViewService.setName("btnViewService"); // NOI18N
        btnViewService.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnViewService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnViewService);

        btnStartService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/start.png"))); // NOI18N
        btnStartService.setText(bundle.getString("TongaApplicationPanel.btnStartService.text")); // NOI18N
        btnStartService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStartService.setName("btnStartService"); // NOI18N
        btnStartService.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        btnStartService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnStartService);

        btnCompleteService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/confirm.png"))); // NOI18N
        btnCompleteService.setText(bundle.getString("TongaApplicationPanel.btnCompleteService.text")); // NOI18N
        btnCompleteService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCompleteService.setName("btnCompleteService"); // NOI18N
        btnCompleteService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnCompleteService);

        btnRevertService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/revert.png"))); // NOI18N
        btnRevertService.setText(bundle.getString("TongaApplicationPanel.btnRevertService.text")); // NOI18N
        btnRevertService.setFocusable(false);
        btnRevertService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRevertService.setName("btnRevertService"); // NOI18N
        btnRevertService.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevertService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnRevertService);

        btnCancelService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/cancel.png"))); // NOI18N
        btnCancelService.setText(bundle.getString("TongaApplicationPanel.btnCancelService.text")); // NOI18N
        btnCancelService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCancelService.setName("btnCancelService"); // NOI18N
        btnCancelService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelServiceActionPerformed(evt);
            }
        });
        tbServices.add(btnCancelService);

        javax.swing.GroupLayout servicesPanelLayout = new javax.swing.GroupLayout(servicesPanel);
        servicesPanel.setLayout(servicesPanelLayout);
        servicesPanelLayout.setHorizontalGroup(
            servicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, servicesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(servicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollFeeDetails1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                    .addComponent(tbServices, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE))
                .addContainerGap())
        );
        servicesPanelLayout.setVerticalGroup(
            servicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servicesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbServices, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollFeeDetails1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.servicesPanel.TabConstraints.tabTitle"), servicesPanel); // NOI18N

        documentPanel.setName("documentPanel"); // NOI18N
        documentPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        documentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                documentPanelMouseClicked(evt);
            }
        });

        labDocRequired.setBackground(new java.awt.Color(255, 255, 204));
        labDocRequired.setText(bundle.getString("TongaApplicationPanel.labDocRequired.text")); // NOI18N
        labDocRequired.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        labDocRequired.setName("labDocRequired"); // NOI18N
        labDocRequired.setOpaque(true);

        scrollDocRequired.setBackground(new java.awt.Color(255, 255, 255));
        scrollDocRequired.setName("scrollDocRequired"); // NOI18N
        scrollDocRequired.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        tblDocTypesHelper.setBackground(new java.awt.Color(255, 255, 255));
        tblDocTypesHelper.setGridColor(new java.awt.Color(255, 255, 255));
        tblDocTypesHelper.setName("tblDocTypesHelper"); // NOI18N
        tblDocTypesHelper.setOpaque(false);
        tblDocTypesHelper.setShowHorizontalLines(false);
        tblDocTypesHelper.setShowVerticalLines(false);
        tblDocTypesHelper.getTableHeader().setResizingAllowed(false);
        tblDocTypesHelper.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${checkList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationDocumentsHelper, eLProperty, tblDocTypesHelper);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${isInList}"));
        columnBinding.setColumnName("Is In List");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${displayValue}"));
        columnBinding.setColumnName("Display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        scrollDocRequired.setViewportView(tblDocTypesHelper);
        tblDocTypesHelper.getColumnModel().getColumn(0).setMinWidth(20);
        tblDocTypesHelper.getColumnModel().getColumn(0).setPreferredWidth(20);
        tblDocTypesHelper.getColumnModel().getColumn(0).setMaxWidth(20);
        tblDocTypesHelper.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("TongaApplicationPanel.tblDocTypesHelper.columnModel.title0_1")); // NOI18N
        tblDocTypesHelper.getColumnModel().getColumn(0).setCellRenderer(new BooleanCellRenderer());
        tblDocTypesHelper.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("TongaApplicationPanel.tblDocTypesHelper.columnModel.title1_1")); // NOI18N

        documentsPanel.setMinimumSize(new java.awt.Dimension(20, 40));
        documentsPanel.setName(bundle.getString("ApplicationPanel.documentsPanel.name")); // NOI18N

        javax.swing.GroupLayout documentPanelLayout = new javax.swing.GroupLayout(documentPanel);
        documentPanel.setLayout(documentPanelLayout);
        documentPanelLayout.setHorizontalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, documentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(documentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labDocRequired, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(scrollDocRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        documentPanelLayout.setVerticalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(documentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, documentPanelLayout.createSequentialGroup()
                        .addComponent(labDocRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.documentPanel.TabConstraints.tabTitle"), documentPanel); // NOI18N

        mapPanel.setName("mapPanel"); // NOI18N

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 685, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.mapPanel.TabConstraints.tabTitle"), mapPanel); // NOI18N

        feesPanel.setName("feesPanel"); // NOI18N
        feesPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        feesPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                feesPanelMouseClicked(evt);
            }
        });

        scrollFeeDetails.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        scrollFeeDetails.setName("scrollFeeDetails"); // NOI18N
        scrollFeeDetails.setOpaque(false);

        tabFeeDetails.setColumnSelectionAllowed(true);
        tabFeeDetails.setName("tabFeeDetails"); // NOI18N
        tabFeeDetails.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${serviceList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, eLProperty, tabFeeDetails);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${requestType.displayValue}"));
        columnBinding.setColumnName("Request Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${baseFee}"));
        columnBinding.setColumnName("Base Fee");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${areaFee}"));
        columnBinding.setColumnName("Area Fee");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${valueFee}"));
        columnBinding.setColumnName("Value Fee");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${expectedCompletionDate}"));
        columnBinding.setColumnName("Expected Completion Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        scrollFeeDetails.setViewportView(tabFeeDetails);
        tabFeeDetails.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabFeeDetails.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("TongaApplicationPanel.tabFeeDetails.columnModel.title0")); // NOI18N
        tabFeeDetails.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("TongaApplicationPanel.tabFeeDetails.columnModel.title1_1")); // NOI18N
        tabFeeDetails.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("TongaApplicationPanel.tabFeeDetails.columnModel.title2_2")); // NOI18N
        tabFeeDetails.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("TongaApplicationPanel.tabFeeDetails.columnModel.title3")); // NOI18N
        tabFeeDetails.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("TongaApplicationPanel.tabFeeDetails.columnModel.title4")); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        formTxtServiceFee.setEditable(false);
        formTxtServiceFee.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        formTxtServiceFee.setEnabled(false);
        formTxtServiceFee.setInheritsPopupMenu(true);
        formTxtServiceFee.setName("formTxtServiceFee"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${servicesFee}"), formTxtServiceFee, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        formTxtServiceFee.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        formTxtServiceFee.setHorizontalAlignment(JFormattedTextField.LEADING);

        formTxtTaxes.setEditable(false);
        formTxtTaxes.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        formTxtTaxes.setEnabled(false);
        formTxtTaxes.setInheritsPopupMenu(true);
        formTxtTaxes.setName("formTxtTaxes"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${tax}"), formTxtTaxes, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        formTxtTaxes.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        formTxtTaxes.setHorizontalAlignment(JFormattedTextField.LEADING);

        formTxtFee.setEditable(false);
        formTxtFee.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        formTxtFee.setEnabled(false);
        formTxtFee.setName("formTxtFee"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${totalFee}"), formTxtFee, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        formTxtFee.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        formTxtFee.setHorizontalAlignment(JFormattedTextField.LEADING);

        LafManager.getInstance().setLabProperties(labTotalFee2);
        labTotalFee2.setText(bundle.getString("TongaApplicationPanel.labTotalFee2.text")); // NOI18N
        labTotalFee2.setName("labTotalFee2"); // NOI18N

        LafManager.getInstance().setLabProperties(labTotalFee);
        labTotalFee.setText(bundle.getString("TongaApplicationPanel.labTotalFee.text")); // NOI18N
        labTotalFee.setName("labTotalFee"); // NOI18N

        LafManager.getInstance().setLabProperties(labTotalFee1);
        labTotalFee1.setText(bundle.getString("TongaApplicationPanel.labTotalFee1.text")); // NOI18N
        labTotalFee1.setName("labTotalFee1"); // NOI18N

        labFixedFee.setBackground(new java.awt.Color(255, 255, 255));
        LafManager.getInstance().setLabProperties(labFixedFee);
        labFixedFee.setText(bundle.getString("TongaApplicationPanel.labFixedFee.text")); // NOI18N
        labFixedFee.setName("labFixedFee"); // NOI18N

        formTxtReceiptRef.setName(bundle.getString("ApplicationPanel.formTxtReceiptRef.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${receiptRef}"), formTxtReceiptRef, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labReceiptRef.setText(bundle.getString("TongaApplicationPanel.labReceiptRef.text")); // NOI18N
        labReceiptRef.setName(bundle.getString("ApplicationPanel.labReceiptRef.name")); // NOI18N

        labTotalFee3.setText(bundle.getString("TongaApplicationPanel.labTotalFee3.text")); // NOI18N
        labTotalFee3.setName("labTotalFee3"); // NOI18N

        cbxPaid.setText(bundle.getString("TongaApplicationPanel.cbxPaid.text")); // NOI18N
        cbxPaid.setActionCommand(bundle.getString("TongaApplicationPanel.cbxPaid.actionCommand")); // NOI18N
        cbxPaid.setMargin(new java.awt.Insets(2, 0, 2, 2));
        cbxPaid.setName("cbxPaid"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${feePaid}"), cbxPaid, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        formTxtPaid.setFormatterFactory(FormattersFactory.getInstance().getMoneyFormatterFactory());
        formTxtPaid.setText(bundle.getString("TongaApplicationPanel.formTxtPaid.text")); // NOI18N
        formTxtPaid.setName(bundle.getString("ApplicationPanel.formTxtPaid.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, org.jdesktop.beansbinding.ELProperty.create("${totalAmountPaid}"), formTxtPaid, org.jdesktop.beansbinding.BeanProperty.create("value"), "formTxtPaidBinding"); // NOI18N
        binding.setConverter(new BigDecimalMoneyConverter());
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labFixedFee, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(formTxtServiceFee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTotalFee1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formTxtTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formTxtFee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTotalFee2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formTxtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(formTxtReceiptRef, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxPaid))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(labReceiptRef, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labTotalFee3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {formTxtFee, formTxtPaid, formTxtReceiptRef, formTxtServiceFee});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labFixedFee, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labReceiptRef, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labTotalFee2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labTotalFee1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labTotalFee3)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(formTxtServiceFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(formTxtTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(formTxtFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(formTxtReceiptRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(formTxtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxPaid)))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout feesPanelLayout = new javax.swing.GroupLayout(feesPanel);
        feesPanel.setLayout(feesPanelLayout);
        feesPanelLayout.setHorizontalGroup(
            feesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(feesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollFeeDetails)
                    .addGroup(feesPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );
        feesPanelLayout.setVerticalGroup(
            feesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, feesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollFeeDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.feesPanel.TabConstraints.tabTitle"), feesPanel); // NOI18N

        validationPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        validationPanel.setName("validationPanel"); // NOI18N
        validationPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        validationsPanel.setBackground(new java.awt.Color(255, 255, 255));
        validationsPanel.setName("validationsPanel"); // NOI18N
        validationsPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        tabValidations.setName("tabValidations"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${validationResutlList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, validationResultListBean, eLProperty, tabValidations);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${feedback}"));
        columnBinding.setColumnName("Feedback");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${severity}"));
        columnBinding.setColumnName("Severity");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${successful}"));
        columnBinding.setColumnName("Successful");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        validationsPanel.setViewportView(tabValidations);
        tabValidations.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ApplicationPanel.tabValidations.columnModel.title1")); // NOI18N
        tabValidations.getColumnModel().getColumn(0).setCellRenderer(new TableCellTextAreaRenderer());
        tabValidations.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabValidations.getColumnModel().getColumn(1).setMaxWidth(100);
        tabValidations.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ApplicationPanel.tabValidations.columnModel.title2")); // NOI18N
        tabValidations.getColumnModel().getColumn(2).setPreferredWidth(45);
        tabValidations.getColumnModel().getColumn(2).setMaxWidth(45);
        tabValidations.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ApplicationPanel.tabValidations.columnModel.title3")); // NOI18N
        tabValidations.getColumnModel().getColumn(2).setCellRenderer(new ViolationCellRenderer());
        tabValidations.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        javax.swing.GroupLayout validationPanelLayout = new javax.swing.GroupLayout(validationPanel);
        validationPanel.setLayout(validationPanelLayout);
        validationPanelLayout.setHorizontalGroup(
            validationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(validationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(validationsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        validationPanelLayout.setVerticalGroup(
            validationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(validationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(validationsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.validationPanel.TabConstraints.tabTitle"), validationPanel); // NOI18N

        historyPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        historyPanel.setName("historyPanel"); // NOI18N
        historyPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        historyPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historyPanelMouseClicked(evt);
            }
        });

        actionLogPanel.setBorder(null);
        actionLogPanel.setName("actionLogPanel"); // NOI18N
        actionLogPanel.setPreferredSize(new java.awt.Dimension(200, 200));
        actionLogPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        tabActionLog.setName("tabActionLog"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${appLogList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appBean, eLProperty, tabActionLog);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${changeTime}"));
        columnBinding.setColumnName("Change Time");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${userFullname}"));
        columnBinding.setColumnName("User Fullname");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${notation}"));
        columnBinding.setColumnName("Notation");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        actionLogPanel.setViewportView(tabActionLog);
        tabActionLog.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("TongaApplicationPanel.tabActionLog.columnModel.title0")); // NOI18N
        tabActionLog.getColumnModel().getColumn(0).setCellRenderer(new DateTimeRenderer(true));
        tabActionLog.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("TongaApplicationPanel.tabActionLog.columnModel.title1_1")); // NOI18N
        tabActionLog.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("TongaApplicationPanel.tabActionLog.columnModel.title2_1")); // NOI18N
        tabActionLog.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("TongaApplicationPanel.tabActionLog.columnModel.title3_1")); // NOI18N
        tabActionLog.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(actionLogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addComponent(actionLogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedControlMain.addTab(bundle.getString("TongaApplicationPanel.historyPanel.TabConstraints.tabTitle"), historyPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedControlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedControlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Validates user's data input and calls save operation on the
     * {@link ApplicationBean}.
     */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveApplication(false);
}//GEN-LAST:event_btnSaveActionPerformed

    private void btnCalculateFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateFeeActionPerformed
        calculateFee();
    }//GEN-LAST:event_btnCalculateFeeActionPerformed

    private void btnValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateActionPerformed
        validateApplication();
    }//GEN-LAST:event_btnValidateActionPerformed

    private void menuApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApproveActionPerformed
        approveApplication();
    }//GEN-LAST:event_menuApproveActionPerformed

    private void menuCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCancelActionPerformed
        rejectApplication();
    }//GEN-LAST:event_menuCancelActionPerformed

    private void menuWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuWithdrawActionPerformed
        withdrawApplication();
    }//GEN-LAST:event_menuWithdrawActionPerformed

    private void menuLapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLapseActionPerformed
        lapseApplication();
    }//GEN-LAST:event_menuLapseActionPerformed

    private void menuRequisitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRequisitionActionPerformed
        requisitionApplication();
    }//GEN-LAST:event_menuRequisitionActionPerformed

    private void menuResubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuResubmitActionPerformed
        resubmitApplication();
    }//GEN-LAST:event_menuResubmitActionPerformed

    private void menuDispatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDispatchActionPerformed
        dispatchApplication();
    }//GEN-LAST:event_menuDispatchActionPerformed

    private void menuArchiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchiveActionPerformed
        archiveApplication();
    }//GEN-LAST:event_menuArchiveActionPerformed

    private void menuAddServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddServiceActionPerformed
        addService();
    }//GEN-LAST:event_menuAddServiceActionPerformed

    private void menuRemoveServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveServiceActionPerformed
        removeService();
    }//GEN-LAST:event_menuRemoveServiceActionPerformed

    private void menuMoveServiceUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMoveServiceUpActionPerformed
        moveServiceUp();
    }//GEN-LAST:event_menuMoveServiceUpActionPerformed

    private void menuMoveServiceDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMoveServiceDownActionPerformed
        moveServiceDown();
    }//GEN-LAST:event_menuMoveServiceDownActionPerformed

    private void menuViewServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuViewServiceActionPerformed
        viewService();
    }//GEN-LAST:event_menuViewServiceActionPerformed

    private void menuStartServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStartServiceActionPerformed
        startService();
    }//GEN-LAST:event_menuStartServiceActionPerformed

    private void menuCompleteServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompleteServiceActionPerformed
        completeService();
    }//GEN-LAST:event_menuCompleteServiceActionPerformed

    private void menuCancelServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCancelServiceActionPerformed
        cancelService();
    }//GEN-LAST:event_menuCancelServiceActionPerformed

    private void menuRevertServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRevertServiceActionPerformed
        revertService();
    }//GEN-LAST:event_menuRevertServiceActionPerformed

    private void btnCertificateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCertificateActionPerformed
        openSysRegCertParamsForm(appBean.getNr());
    }//GEN-LAST:event_btnCertificateActionPerformed

    private void menuPrintApplicationFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrintApplicationFormActionPerformed
        printLeaseApplicationReport();
    }//GEN-LAST:event_menuPrintApplicationFormActionPerformed

    private void menuPrintStatusReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrintStatusReportActionPerformed
        printLodgementNotice();
    }//GEN-LAST:event_menuPrintStatusReportActionPerformed

    private void historyPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyPanelMouseClicked
        cbxAgents.requestFocus(false);
    }//GEN-LAST:event_historyPanelMouseClicked

    private void feesPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_feesPanelMouseClicked
        cbxAgents.requestFocus(false);
        formTxtServiceFee.requestFocus(true);
    }//GEN-LAST:event_feesPanelMouseClicked

    private void documentPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_documentPanelMouseClicked
        cbxAgents.requestFocus(false);
    }//GEN-LAST:event_documentPanelMouseClicked

    private void btnCancelServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelServiceActionPerformed
        cancelService();
    }//GEN-LAST:event_btnCancelServiceActionPerformed

    private void btnRevertServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertServiceActionPerformed
        revertService();
    }//GEN-LAST:event_btnRevertServiceActionPerformed

    private void btnCompleteServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteServiceActionPerformed
        completeService();
    }//GEN-LAST:event_btnCompleteServiceActionPerformed

    private void btnStartServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartServiceActionPerformed
        startService();
    }//GEN-LAST:event_btnStartServiceActionPerformed

    private void btnViewServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewServiceActionPerformed
        viewService();
    }//GEN-LAST:event_btnViewServiceActionPerformed

    private void btnDownServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownServiceActionPerformed
        moveServiceDown();
    }//GEN-LAST:event_btnDownServiceActionPerformed

    private void btnUPServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUPServiceActionPerformed
        moveServiceUp();
    }//GEN-LAST:event_btnUPServiceActionPerformed

    private void btnRemoveServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveServiceActionPerformed
        removeService();
    }//GEN-LAST:event_btnRemoveServiceActionPerformed

    private void btnAddServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddServiceActionPerformed
        addService();
    }//GEN-LAST:event_btnAddServiceActionPerformed

    private void btnVerifyPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifyPropertyActionPerformed
        verifySelectedProperty();
    }//GEN-LAST:event_btnVerifyPropertyActionPerformed

    private void btnEditPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPropertyActionPerformed
        openSelectedProperty(false);
    }//GEN-LAST:event_btnEditPropertyActionPerformed

    private void contactPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactPanelMouseClicked
        cbxAgents.requestFocus(false);
        txtFirstName.requestFocus();
    }//GEN-LAST:event_contactPanelMouseClicked

    /**
     * Removes attached digital copy from selected document.
     */
    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        // Verify the email address is valid
        if (appBean.getContactPerson().getEmail() == null
                || !appBean.getContactPerson().getEmail().equals(txtEmail.getText())) {
            txtEmail.setText(appBean.getContactPerson().getEmail());
        }
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneFocusLost
        // Verify the phone number is valid
        if (appBean.getContactPerson().getPhone() == null
                || !appBean.getContactPerson().getPhone().equals(txtPhone.getText())) {
            txtPhone.setText(appBean.getContactPerson().getPhone());
        }
    }//GEN-LAST:event_txtPhoneFocusLost

    private void btnRemovePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePropertyActionPerformed
        removeSelectedProperty();
    }//GEN-LAST:event_btnRemovePropertyActionPerformed

    private void btnViewPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPropertyActionPerformed
        openSelectedProperty(true);
    }//GEN-LAST:event_btnViewPropertyActionPerformed

    private void btnSearchPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPropertyActionPerformed
        findProperty();
    }//GEN-LAST:event_btnSearchPropertyActionPerformed

    private void btnAddAllotmentPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAllotmentPropertyActionPerformed
        addProperty(BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT);
    }//GEN-LAST:event_btnAddAllotmentPropertyActionPerformed

    private void btnAddLeasePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLeasePropertyActionPerformed
        addProperty(BaUnitTypeBean.CODE_LEASED_UNIT);
    }//GEN-LAST:event_btnAddLeasePropertyActionPerformed

    private void btnAddSubleasePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSubleasePropertyActionPerformed
        addProperty(BaUnitTypeBean.CODE_SUBLEASE_UNIT);
    }//GEN-LAST:event_btnAddSubleasePropertyActionPerformed

    private void openSysRegCertParamsForm(String nr) {
        SysRegCertParamsForm certificateGenerator = new SysRegCertParamsForm(null, true, nr, null);
        certificateGenerator.setVisible(true);
    }

    /**
     * Initializes map control to display application location.
     */
    private void formComponentShown(java.awt.event.ComponentEvent evt) {
        if (this.mapControl == null && SecurityBean.isInRole(RolesConstants.GIS_VIEW_MAP)) {
            this.mapControl = new ControlsBundleForApplicationLocation();
            this.mapControl.setApplicationLocation(appBean.getLocation());
            this.mapControl.setApplicationId(appBean.getId());
            this.mapPanel.setLayout(new BorderLayout());
            this.mapPanel.add(this.mapControl, BorderLayout.CENTER);
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

    private void takeActionAgainstApplication(final String actionType) {
        String msgCode = ClientMessage.APPLICATION_ACTION_WARNING_SOFT;
        if (ApplicationActionTypeBean.WITHDRAW.equals(actionType)
                || ApplicationActionTypeBean.ARCHIVE.equals(actionType)
                || ApplicationActionTypeBean.LAPSE.equals(actionType)
                || ApplicationActionTypeBean.CANCEL.equals(actionType)
                || ApplicationActionTypeBean.APPROVE.equals(actionType)) {
            msgCode = ClientMessage.APPLICATION_ACTION_WARNING_STRONG;
        }
        String localizedActionName = CacheManager.getBeanByCode(
                CacheManager.getApplicationActionTypes(), actionType).getDisplayValue();
        if (MessageUtility.displayMessage(msgCode, new String[]{localizedActionName}) == MessageUtility.BUTTON_ONE) {

            if (!checkSaveBeforeAction()) {
                return;
            }

            SolaTask<List<ValidationResultBean>, List<ValidationResultBean>> t =
                    new SolaTask<List<ValidationResultBean>, List<ValidationResultBean>>() {
                @Override
                public List<ValidationResultBean> doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_TAKE_ACTION));
                    boolean displayValidationResultFormInSuccess = true;
                    List<ValidationResultBean> result = null;
                    if (ApplicationActionTypeBean.VALIDATE.equals(actionType)) {
                        displayValidationResultFormInSuccess = false;
                        validationResultListBean.setValidationResultList(appBean.validate());
                    } else if (ApplicationActionTypeBean.WITHDRAW.equals(actionType)) {
                        result = appBean.withdraw();
                    } else if (ApplicationActionTypeBean.CANCEL.equals(actionType)) {
                        result = appBean.reject();
                    } else if (ApplicationActionTypeBean.ARCHIVE.equals(actionType)) {
                        result = appBean.archive();
                    } else if (ApplicationActionTypeBean.DISPATCH.equals(actionType)) {
                        result = appBean.despatch();
                    } else if (ApplicationActionTypeBean.LAPSE.equals(actionType)) {
                        result = appBean.lapse();
                    } else if (ApplicationActionTypeBean.REQUISITION.equals(actionType)) {
                        result = appBean.requisition();
                    } else if (ApplicationActionTypeBean.RESUBMIT.equals(actionType)) {
                        result = appBean.resubmit();
                    } else if (ApplicationActionTypeBean.APPROVE.equals(actionType)) {
                        result = appBean.approve();
                    }

                    if (displayValidationResultFormInSuccess) {
                        return result;
                    }
                    return null;
                }

                @Override
                public void taskDone() {
                    List<ValidationResultBean> result = get();

                    if (result != null) {
                        String message = MessageUtility.getLocalizedMessage(
                                ClientMessage.APPLICATION_ACTION_SUCCESS,
                                new String[]{appBean.getNr()}).getMessage();
                        openValidationResultForm(result, true, message);
                    }
                    saveAppState();
                    refreshDashboard();
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    private void addService() {
        ServiceListForm serviceListForm = new ServiceListForm(appBean);
        serviceListForm.setLocationRelativeTo(this);
        serviceListForm.setVisible(true);
        btnCalculateFee.setEnabled(true);
    }

    /**
     * Removes selected service from the services list.
     */
    private void removeService() {
        if (appBean.getSelectedService() != null) {
            appBean.removeSelectedService();
            applicationDocumentsHelper.updateCheckList(appBean.getServiceList(), appBean.getSourceList());
        }
    }

    /**
     * Moves selected service up in the list of services.
     */
    private void moveServiceUp() {
        ApplicationServiceBean asb = appBean.getSelectedService();
        if (asb != null) {
            Integer order = (Integer) (tabServices.getValueAt(tabServices.getSelectedRow(), 0));
            if (appBean.moveServiceUp()) {
                tabServices.setValueAt(order - 1, tabServices.getSelectedRow() - 1, 0);
                tabServices.setValueAt(order, tabServices.getSelectedRow(), 0);
                tabServices.getSelectionModel().setSelectionInterval(tabServices.getSelectedRow() - 1, tabServices.getSelectedRow() - 1);
            }
        } else {
            MessageUtility.displayMessage(ClientMessage.APPLICATION_SELECT_SERVICE);

        }
    }

    /**
     * Moves selected application service down in the services list. Calls
     * {@link ApplicationBean#moveServiceDown()}
     */
    private void moveServiceDown() {
        ApplicationServiceBean asb = appBean.getSelectedService();
        if (asb != null) {
            Integer order = (Integer) (tabServices.getValueAt(tabServices.getSelectedRow(), 0));
            //            lstSelectedServices.setSelectedIndex(lstSelectedServices.getSelectedIndex() - 1);
            if (appBean.moveServiceDown()) {
                tabServices.setValueAt(order + 1, tabServices.getSelectedRow() + 1, 0);
                tabServices.setValueAt(order, tabServices.getSelectedRow(), 0);
                tabServices.getSelectionModel().setSelectionInterval(tabServices.getSelectedRow() + 1, tabServices.getSelectedRow() + 1);
            }
        } else {
            MessageUtility.displayMessage(ClientMessage.APPLICATION_SELECT_SERVICE);
        }
    }

    /**
     * Launches selected service.
     */
    private void startService() {
        final ApplicationServiceBean selectedService = appBean.getSelectedService();

        if (selectedService != null) {

            SolaTask t = new SolaTask<Void, Void>() {
                List<ValidationResultBean> result;

                @Override
                protected Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SERVICE_STARTING));
                    result = selectedService.start();
                    return null;
                }

                @Override
                protected void taskDone() {
                    appBean.reload();
                    customizeApplicationForm();
                    saveAppState();
                    launchService(appBean.getServiceById(selectedService.getId()), false);
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    /**
     * Calls "complete method for the selected service. "
     */
    private void completeService() {
        final ApplicationServiceBean selectedService = appBean.getSelectedService();

        if (selectedService != null) {

            final String serviceName = selectedService.getRequestType().getDisplayValue();

            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_SERVICE_COMPLETE_WARNING,
                    new String[]{serviceName}) == MessageUtility.BUTTON_ONE) {

                if (!checkSaveBeforeAction()) {
                    return;
                }

                SolaTask t = new SolaTask<Void, Void>() {
                    List<ValidationResultBean> result;

                    @Override
                    protected Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SERVICE_COMPLETING));
                        result = selectedService.complete();
                        return null;
                    }

                    @Override
                    protected void taskDone() {
                        String message = MessageUtility.getLocalizedMessage(
                                ClientMessage.APPLICATION_SERVICE_COMPLETE_SUCCESS,
                                new String[]{serviceName}).getMessage();

                        appBean.reload();
                        customizeApplicationForm();
                        saveAppState();
                        if (result != null) {
                            openValidationResultForm(result, true, message);
                        }
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }
    }

    private void revertService() {
        final ApplicationServiceBean selectedService = appBean.getSelectedService();

        if (selectedService != null) {

            final String serviceName = selectedService.getRequestType().getDisplayValue();

            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_SERVICE_REVERT_WARNING,
                    new String[]{serviceName}) == MessageUtility.BUTTON_ONE) {

                if (!checkSaveBeforeAction()) {
                    return;
                }

                SolaTask t = new SolaTask<Void, Void>() {
                    List<ValidationResultBean> result;

                    @Override
                    protected Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SERVICE_REVERTING));
                        result = selectedService.revert();
                        return null;
                    }

                    @Override
                    protected void taskDone() {
                        String message = MessageUtility.getLocalizedMessage(
                                ClientMessage.APPLICATION_SERVICE_REVERT_SUCCESS,
                                new String[]{serviceName}).getMessage();

                        appBean.reload();
                        customizeApplicationForm();
                        saveAppState();
                        if (result != null) {
                            openValidationResultForm(result, true, message);
                        }
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }
    }

    private void cancelService() {
        final ApplicationServiceBean selectedService = appBean.getSelectedService();

        if (selectedService != null) {

            final String serviceName = selectedService.getRequestType().getDisplayValue();
            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_SERVICE_CANCEL_WARNING,
                    new String[]{serviceName}) == MessageUtility.BUTTON_ONE) {

                if (!checkSaveBeforeAction()) {
                    return;
                }

                SolaTask t = new SolaTask<Void, Void>() {
                    List<ValidationResultBean> result;

                    @Override
                    protected Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SERVICE_CANCELING));
                        result = selectedService.cancel();
                        return null;
                    }

                    @Override
                    protected void taskDone() {
                        String message;

                        message = MessageUtility.getLocalizedMessage(
                                ClientMessage.APPLICATION_SERVICE_CANCEL_SUCCESS,
                                new String[]{serviceName}).getMessage();
                        appBean.reload();
                        customizeApplicationForm();
                        saveAppState();
                        if (result != null) {
                            openValidationResultForm(result, true, message);
                        }
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }
    }

    /**
     * Removes selected property object from the properties list. Calls
     * {@link ApplicationBean#removeSelectedProperty()}
     */
    private void removeSelectedProperty() {
        appBean.removeSelectedProperty();
    }

    /**
     * Verifies selected property object to check existence. Calls
     * {@link ApplicationBean#verifyProperty()}
     */
    private void verifySelectedProperty() {
        if (appBean.getSelectedProperty() == null) {
            MessageUtility.displayMessage(ClientMessage.APPLICATION_SELECT_PROPERTY_TOVERIFY);
            return;
        }

        SolaTask t = new SolaTask<Void, Void>() {
            boolean result = false;

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_VALIDATING));
                result = ApplicationBean.verifyProperty(appBean.getSelectedProperty(), appBean.getNr(), false);
                return null;
            }

            @Override
            protected void taskDone() {
                if (result) {
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_PROPERTY_VERIFIED);
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Method to display a form that allows the selected property to be opened
     * for edit or read only.
     */
    private void openSelectedProperty(final boolean readOnly) {
        if (appBean.getSelectedProperty() != null) {
            SolaTask t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));
                    ApplicationPropertyForm form = new ApplicationPropertyForm(appBean.getSelectedProperty(), appBean.getSelectedProperty().getTypeCode(),
                            appBean.getNr(), readOnly);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_PROPERTY, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    private void addProperty(final String propertyType) {

        if (addPropertyListener == null) {
            addPropertyListener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(ApplicationPropertyForm.RESULT_PROPERTY)) {
                        Object[] selectedResult = (Object[]) evt.getNewValue();
                        if (selectedResult != null && selectedResult.length > 0) {
                            appBean.addProperty((ApplicationPropertyBean) selectedResult[0]);
                        }
                    }
                }
            };
        }

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));
                ApplicationPropertyForm form = new ApplicationPropertyForm(null, propertyType, appBean.getNr(), false);
                form.addPropertyChangeListener(addPropertyListener);
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_PROPERTY, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    /**
     * Allows the user to search for an existing property using Property Search
     */
    private void findProperty() {

        if (findPropertyListener == null) {
            findPropertyListener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(BaUnitSearchPanel.SELECTED_RESULT_PROPERTY)) {
                        Object[] selectedResult = (Object[]) evt.getNewValue();
                        if (selectedResult != null && selectedResult.length > 0) {
                            appBean.addProperty((BaUnitBean) selectedResult[0]);
                        }
                    }
                }
            };
        }

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));

                BaUnitSearchPanel findPropertyPanel = new BaUnitSearchPanel();
                findPropertyPanel.addPropertyChangeListener(findPropertyListener);
                // Configure the SearchPanel for selection
                findPropertyPanel.getSearchPanel().setDefaultActionOpen(false);
                findPropertyPanel.getSearchPanel().showOpenButtons(false);
                findPropertyPanel.getSearchPanel().showSelectButtons(true);
                findPropertyPanel.getSearchPanel().hideTabs(TongaBaUnitSearchPanel.TAB_ESTATE, TongaBaUnitSearchPanel.TAB_TOWN);
                getMainContentPanel().addPanel(findPropertyPanel, MainContentPanel.CARD_BAUNIT_SEARCH, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void approveApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.APPROVE);
    }

    private void rejectApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.CANCEL);
    }

    private void withdrawApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.WITHDRAW);
    }

    private void requisitionApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.REQUISITION);
    }

    private void archiveApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.ARCHIVE);
    }

    private void dispatchApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.DISPATCH);
    }

    private void lapseApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.LAPSE);
    }

    private void resubmitApplication() {
        takeActionAgainstApplication(ApplicationActionTypeBean.RESUBMIT);
    }

    private void saveAppState() {
        MainForm.saveBeanState(appBean);
    }

    /**
     * Calculates fee for the application. Calls
     * {@link ApplicationBean#calculateFee()}
     */
    private void calculateFee() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_CALCULATINGFEE));
                appBean.calculateFee();
                tabbedControlMain.setSelectedIndex(tabbedControlMain.indexOfComponent(feesPanel));
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Allows to overview service.
     */
    private void viewService() {
        launchService(appBean.getSelectedService(), true);
    }

    private void printLodgementNotice() {
        showReport(ReportManager.getTongaLodgementNotice(new ApplicationReportBean(appBean)));
    }

    private void printLeaseApplicationReport() {
        showReport(ReportManager.getLeaseApplicationReport(new ApplicationReportBean(appBean)));
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(appBean)) {
            saveApplication(true);
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane actionLogPanel;
    public org.sola.clients.beans.application.ApplicationBean appBean;
    private org.sola.clients.beans.application.ApplicationDocumentsHelperBean applicationDocumentsHelper;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddAllotmentProperty;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddLeaseProperty;
    private javax.swing.JButton btnAddService;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddSubleaseProperty;
    private javax.swing.JButton btnCalculateFee;
    private javax.swing.JButton btnCancelService;
    private javax.swing.JButton btnCertificate;
    private javax.swing.JButton btnCompleteService;
    private javax.swing.JButton btnDownService;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEditProperty;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveProperty;
    private javax.swing.JButton btnRemoveService;
    private javax.swing.JButton btnRevertService;
    private javax.swing.JButton btnSave;
    private org.sola.clients.swing.common.buttons.BtnSearch btnSearchProperty;
    private javax.swing.JButton btnStartService;
    private javax.swing.JButton btnUPService;
    private javax.swing.JButton btnValidate;
    private javax.swing.JButton btnVerifyProperty;
    private org.sola.clients.swing.common.buttons.BtnView btnViewProperty;
    private javax.swing.JButton btnViewService;
    private javax.swing.JComboBox cbxAgents;
    public javax.swing.JComboBox cbxCommunicationWay;
    private javax.swing.JCheckBox cbxPaid;
    private org.sola.clients.beans.referencedata.CommunicationTypeListBean communicationTypes;
    public javax.swing.JPanel contactPanel;
    public javax.swing.JPanel documentPanel;
    private org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel documentsPanel;
    private org.sola.clients.swing.common.controls.DropDownButton dropDownButton1;
    public javax.swing.JPanel feesPanel;
    private javax.swing.JFormattedTextField formTxtFee;
    private javax.swing.JFormattedTextField formTxtPaid;
    private javax.swing.JTextField formTxtReceiptRef;
    private javax.swing.JFormattedTextField formTxtServiceFee;
    private javax.swing.JFormattedTextField formTxtTaxes;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    public javax.swing.JPanel historyPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel labAddress;
    private javax.swing.JLabel labAgents;
    private javax.swing.JLabel labDate;
    private javax.swing.JLabel labDocRequired;
    private javax.swing.JLabel labEmail;
    private javax.swing.JLabel labFixedFee;
    private javax.swing.JLabel labLastName;
    private javax.swing.JLabel labName;
    private javax.swing.JLabel labPhone;
    private javax.swing.JLabel labPreferredWay;
    private javax.swing.JLabel labReceiptRef;
    private javax.swing.JLabel labStatus;
    private javax.swing.JLabel labTotalFee;
    private javax.swing.JLabel labTotalFee1;
    private javax.swing.JLabel labTotalFee2;
    private javax.swing.JLabel labTotalFee3;
    private javax.swing.JLabel lblAlias;
    public javax.swing.JPanel mapPanel;
    private javax.swing.JMenuItem menuAddService;
    private javax.swing.JMenuItem menuApprove;
    private javax.swing.JMenuItem menuArchive;
    private javax.swing.JMenuItem menuCancel;
    private javax.swing.JMenuItem menuCancelService;
    private javax.swing.JMenuItem menuCompleteService;
    private javax.swing.JMenuItem menuDispatch;
    private javax.swing.JMenuItem menuLapse;
    private javax.swing.JMenuItem menuMoveServiceDown;
    private javax.swing.JMenuItem menuMoveServiceUp;
    private javax.swing.JMenuItem menuPrintApplicationForm;
    private javax.swing.JMenuItem menuPrintStatusReport;
    private javax.swing.JMenuItem menuRemoveService;
    private javax.swing.JMenuItem menuRequisition;
    private javax.swing.JMenuItem menuResubmit;
    private javax.swing.JMenuItem menuRevertService;
    private javax.swing.JMenuItem menuStartService;
    private javax.swing.JMenuItem menuViewService;
    private javax.swing.JMenuItem menuWithdraw;
    private org.sola.clients.beans.party.PartySummaryListBean partySummaryList;
    private org.sola.clients.swing.ui.HeaderPanel pnlHeader;
    private javax.swing.JPopupMenu popUpServices;
    private javax.swing.JPopupMenu popupApplicationActions;
    private javax.swing.JPopupMenu popupPrintAction;
    private org.sola.clients.swing.common.controls.DropDownButton printDropDown;
    private javax.swing.JScrollPane scrollDocRequired;
    private javax.swing.JScrollPane scrollFeeDetails;
    private javax.swing.JScrollPane scrollFeeDetails1;
    private javax.swing.JPanel servicesPanel;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tabActionLog;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tabFeeDetails;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tabProperty;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tabServices;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tabValidations;
    public javax.swing.JTabbedPane tabbedControlMain;
    private javax.swing.JToolBar tbServices;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tblDocTypesHelper;
    private javax.swing.JPanel tongaPropertyPanel;
    public javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAlias;
    private javax.swing.JTextField txtAppNumber1;
    private javax.swing.JFormattedTextField txtCompleteBy;
    private javax.swing.JFormattedTextField txtDate;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtItemNumber;
    public javax.swing.JTextField txtLastName;
    public javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtStatus;
    public javax.swing.JPanel validationPanel;
    private org.sola.clients.beans.validation.ValidationResultListBean validationResultListBean;
    private javax.swing.JScrollPane validationsPanel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
