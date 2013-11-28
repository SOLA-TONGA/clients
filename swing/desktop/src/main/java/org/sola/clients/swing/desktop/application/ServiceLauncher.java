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
package org.sola.clients.swing.desktop.application;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.administrative.TongaPropertyPanel;
import org.sola.clients.swing.desktop.source.TransactionedDocumentsPanel;
import org.sola.clients.swing.desktop.workflow.*;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Encapsulates the logic required to determine which panel to create to for a
 * service. Also determines the constructor to use based on the constructor
 * arguments indicated.
 *
 * ServiceLauncher has been setup as a singleton with some static helper methods
 * to make it simpler to use.
 *
 * NOTE: The constructorArgs array boxes primitive types, so you must ensure the
 * panel class constructor is declared with boxed data types instead of
 * primitive data types (e.g. the constructor should use Boolean instead of
 * boolean).
 *
 * @author solaDev
 */
public final class ServiceLauncher {

    private Map<String, String[]> serviceMap;

    private ServiceLauncher() {
        loadServiceMap();
    }

    private static class ServiceLauncherHolder {

        private static final ServiceLauncher INSTANCE = new ServiceLauncher();
    }

    /**
     * Method to setup the Hashmap that links the RequestType to the details
     * used when instantiating and displaying the panel form. Each RequestType
     * is mapped to a String array that contains the following information; [0]
     * = The name of the panel class to instantiate [1] = The MainContentPanel
     * Card Name [2] = The Message Code to use when displaying the panel
     */
    private void loadServiceMap() {
        serviceMap = new HashMap<String, String[]>();

        // Checklist Form
        serviceMap.put(RequestTypeBean.CODE_CHECKLIST,
                new String[]{ChecklistForm.class.getName(), MainContentPanel.CARD_CHECKLIST,
            ClientMessage.PROGRESS_MSG_OPEN_CHECKLIST});

        // Site Inspection Form
        serviceMap.put(RequestTypeBean.CODE_SITE_INSPECTION,
                new String[]{SiteInspectionForm.class.getName(), MainContentPanel.CARD_SITE_INSPECTION,
            ClientMessage.PROGRESS_MSG_OPEN_SITE_INSPECTION});

        // Minister Briefing Form
        serviceMap.put(RequestTypeBean.CODE_MINISTER_BRIEFING,
                new String[]{MinisterBriefingForm.class.getName(), MainContentPanel.CARD_MINISTER_BRIEFING,
            ClientMessage.PROGRESS_MSG_OPEN_MINISTER_BRIEFING});

        // HOD Review Form
        serviceMap.put(RequestTypeBean.CODE_HOD_REVIEW,
                new String[]{HodReviewForm.class.getName(), MainContentPanel.CARD_HOD_REVIEW,
            ClientMessage.PROGRESS_MSG_OPEN_HOD_REVIEW});

        // Minister Decision Form
        serviceMap.put(RequestTypeBean.CODE_MINISTER_DECISION,
                new String[]{MinisterDecisionForm.class.getName(), MainContentPanel.CARD_MINISTER_DECISION,
            ClientMessage.PROGRESS_MSG_OPEN_MINISTER_DECISION});

        // Survey Form
        serviceMap.put(RequestTypeBean.CODE_SURVEY,
                new String[]{SurveyForm.class.getName(), MainContentPanel.CARD_SURVEY,
            ClientMessage.PROGRESS_MSG_OPEN_SURVEY});

        // Cabinet Submission Form
        serviceMap.put(RequestTypeBean.CODE_CABINET_SUBMISSION,
                new String[]{CabinetSubmissionForm.class.getName(), MainContentPanel.CARD_CABINET_SUBMISSION,
            ClientMessage.PROGRESS_MSG_OPEN_CABINET_SUBMISSION});

        // Draft Deed Form
        serviceMap.put(RequestTypeBean.CODE_DRAFT_DEED,
                new String[]{DraftDeedForm.class.getName(), MainContentPanel.CARD_DRAFT_DEED,
            ClientMessage.PROGRESS_MSG_OPEN_DRAFT_DEED});

        // Sign Deed Form
        serviceMap.put(RequestTypeBean.CODE_SIGN_DEED,
                new String[]{SignDeedForm.class.getName(), MainContentPanel.CARD_SIGN_DEED,
            ClientMessage.PROGRESS_MSG_OPEN_SIGN_DEED});

        //Item Number Form
        serviceMap.put(RequestTypeBean.CODE_ITEM_NUMBER,
                new String[]{ItemNumberForm.class.getName(), MainContentPanel.CARD_ITEM_NUMBER,
            ClientMessage.PROGRESS_MSG_OPEN_ITEM_NUMBER});

        //Register Tax Api
        serviceMap.put(RequestTypeBean.CODE_REGISTER_TAX_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Register Town Api
        serviceMap.put(RequestTypeBean.CODE_REGISTER_TOWN_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Surrender Api
        serviceMap.put(RequestTypeBean.CODE_SURRENDER_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Exchange Api
        serviceMap.put(RequestTypeBean.CODE_EXCHANGE_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Ejectment Api
        serviceMap.put(RequestTypeBean.CODE_EJECTMENT_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Cancel Api
        serviceMap.put(RequestTypeBean.CODE_CANCEL_API,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Register Lease
        serviceMap.put(RequestTypeBean.CODE_REGISTER_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Transfer Lease
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Transfer Lease - Mortgagee in Possession
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_LEASE_MORTGAGEE_IN_POSSESSION,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Transfer Lease - Probate
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_LEASE_PROBATE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Document Affecting Lease 
        serviceMap.put(RequestTypeBean.CODE_LEASE_DOCUMENT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Renew Lease
        serviceMap.put(RequestTypeBean.CODE_RENEW_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Surrender Lease
        serviceMap.put(RequestTypeBean.CODE_SURRENDER_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Terminate Lease
        serviceMap.put(RequestTypeBean.CODE_TERMINATE_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Register Sublease
        serviceMap.put(RequestTypeBean.CODE_REGISTER_SUBLEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Vary Sublease
        serviceMap.put(RequestTypeBean.CODE_VARY_SUBLEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Transfer Sublease
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_SUBLEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        //Transfer Sublease - Mortgagee In Possession
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_SUBLEASE_MORTGAGEE_IN_POSSESSION,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Transfer Sublease - Probate
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_SUBLEASE_PROBATE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Surrender Sublease 
        serviceMap.put(RequestTypeBean.CODE_SURRENDER_SUBLEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Terminate Sublease 
        serviceMap.put(RequestTypeBean.CODE_TERMINATE_SUBLEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Mortgage
        serviceMap.put(RequestTypeBean.CODE_REGISTER_MORTGAGE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Vary Mortgage
        serviceMap.put(RequestTypeBean.CODE_VARY_MORTGAGE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Discharge Mortgage
        serviceMap.put(RequestTypeBean.CODE_DISCHARGE_MORTGAGE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Assign Mortgage
        serviceMap.put(RequestTypeBean.CODE_ASSIGN_MORTGAGE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Document Affecting Mortgage
        serviceMap.put(RequestTypeBean.CODE_MORTGAGE_DOCUMENT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Caveat
        serviceMap.put(RequestTypeBean.CODE_REGISTER_CAVEAT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Vary Caveat
        serviceMap.put(RequestTypeBean.CODE_VARY_CAVEAT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Remove Caveat
        serviceMap.put(RequestTypeBean.CODE_REMOVE_CAVEAT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Life Estate
        serviceMap.put(RequestTypeBean.CODE_REGISTER_LIFE_ESTATE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Vary Life Estate
        serviceMap.put(RequestTypeBean.CODE_VARY_LIFE_ESTATE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Remove Life Estate
        serviceMap.put(RequestTypeBean.CODE_REMOVE_LIFE_ESTATE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Easement
        serviceMap.put(RequestTypeBean.CODE_REGISTER_EASEMENT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Vary Easement
        serviceMap.put(RequestTypeBean.CODE_VARY_EASEMENT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Remove Easement
        serviceMap.put(RequestTypeBean.CODE_REMOVE_EASEMENT,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Trustee
        serviceMap.put(RequestTypeBean.CODE_REGISTER_TRUSTEE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Vary Trustee
        serviceMap.put(RequestTypeBean.CODE_VARY_TRUSTEE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Remove Trustee
        serviceMap.put(RequestTypeBean.CODE_REMOVE_TRUSTEE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Register Power of Attorney
        serviceMap.put(RequestTypeBean.CODE_REG_POWER_OF_ATTORNEY,
                new String[]{TransactionedDocumentsPanel.class.getName(), MainContentPanel.CARD_TRANSACTIONED_DOCUMENT,
            ClientMessage.PROGRESS_MSG_OPEN_DOCREGISTRATION});

        // Cancel Power of Attorney
        serviceMap.put(RequestTypeBean.CODE_CANCEL_POWER_OF_ATTORNEY,
                new String[]{TransactionedDocumentsPanel.class.getName(), MainContentPanel.CARD_TRANSACTIONED_DOCUMENT,
            ClientMessage.PROGRESS_MSG_OPEN_DOCREGISTRATION});

        // Register Permit
        serviceMap.put(RequestTypeBean.CODE_REGISTER_PERMIT,
                new String[]{TransactionedDocumentsPanel.class.getName(), MainContentPanel.CARD_TRANSACTIONED_DOCUMENT,
            ClientMessage.PROGRESS_MSG_OPEN_DOCREGISTRATION});

        // Cancel Permit
        serviceMap.put(RequestTypeBean.CODE_CANCEL_PERMIT,
                new String[]{TransactionedDocumentsPanel.class.getName(), MainContentPanel.CARD_TRANSACTIONED_DOCUMENT,
            ClientMessage.PROGRESS_MSG_OPEN_DOCREGISTRATION});

        // Correct Registry
        serviceMap.put(RequestTypeBean.CODE_CORRECT_REGISTRY,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Correct Registry - Remove
        serviceMap.put(RequestTypeBean.CODE_CORRECT_REGISTRY_REMOVE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

    }

    /**
     * Returns the message mapped to the request type
     */
    private String getMessageCode(String requestType) {
        String result = null;
        if (serviceMap.containsKey(requestType)) {
            result = serviceMap.get(requestType)[2];
        }
        return result;
    }

    /**
     * Returns the MainContentPanel card name mapped to the request type
     */
    private String getCardName(String requestType) {
        String result = null;
        if (serviceMap.containsKey(requestType)) {
            result = serviceMap.get(requestType)[1];
        }
        return result;
    }

    /**
     * Instantiates a panel for the request type using the constructor arguments
     * supplied. If the panel cannot be instantiated, any errors are written to
     * the SOLA Log and NULL is returned.
     *
     * NOTE: The constructorArgs array boxes primitive types, so you must ensure
     * the panel class constructor is declared with boxed data types instead of
     * primitive data types (e.g. the constructor should use Boolean instead of
     * boolean).
     *
     * @param requestTypeCode The type of service to launch
     * @param constructorArgs The arguments to pass to the panel used to display
     * the service
     */
    private ContentPanel getServicePanel(String requestTypeCode, Object... constructorArgs) {
        ContentPanel panel = null;
        if (serviceMap.containsKey(requestTypeCode)) {
            try {
                // Use reflection to create a class from the class name
                Class<?> panelClass = Class.forName(serviceMap.get(requestTypeCode)[0]);
                if (constructorArgs != null && constructorArgs.length > 0) {
                    Class<?>[] constructorClasses = new Class<?>[constructorArgs.length];
                    int idx = 0;
                    for (Object arg : constructorArgs) {
                        // Determine the class of each constructor argument. Note that primitive
                        // types are boxed, so boolean becomes Boolean. 
                        constructorClasses[idx] = arg.getClass();
                        idx++;
                    }
                    // Retrieve the constructor from the class that matches the argument types.
                    // Primitive types are boxed, so you must ensure the panel class construtor
                    // is declared with boxed data types (i.e. nullable datatypes) instead of 
                    // primitive (non-nullable) data types. e.g. use Boolean instead of boolean 
                    // when declaring the readOnly parameter in the constructor arguments. 
                    Constructor<?> constructor = panelClass.getConstructor(constructorClasses);
                    panel = (ContentPanel) constructor.newInstance(constructorArgs);
                } else {
                    // No constructor arguments, - use the nullary constructor
                    panel = (ContentPanel) panelClass.newInstance();
                }
            } catch (Exception ex) {
                LogUtility.log("Unable to initialize panel for service " + requestTypeCode, ex);
            }
        }
        return panel;
    }

    /**
     * Returns a singleton instance of {@link ServiceLauncher}.
     */
    public static ServiceLauncher getInstance() {
        return ServiceLauncherHolder.INSTANCE;
    }

    /**
     * Launches the panel/form for the request type using a SolaTask worker
     * thread.
     *
     * NOTE: The constructorArgs array boxes primitive types, so you must ensure
     * the panel class constructor is declared with boxed data types instead of
     * primitive data types (e.g. the constructor should use Boolean instead of
     * boolean).
     *
     * @param requestType The request type for the service to launch
     * @param mainPanel The main content panel to show the service panel/form in
     * @param panelListener Property Change Listener that is registered on the
     * new panel to listen for property changes such as the closing of the
     * panel. Can be null.
     * @param taskDone A runnable class that defines any actions to execute at
     * the completion of loading the new panel. Can be null if no actions should
     * occur
     * @param constructorArgs The arguments to pass to the constructor for the
     * form. Null if the nullary constructor is to be used.
     */
    public static void launch(final String requestType,
            final MainContentPanel mainPanel,
            final PropertyChangeListener panelListener,
            final Runnable taskDone,
            final Object... constructorArgs) {
        final boolean result[] = {false};
        if (isMapped(requestType)) {
            SolaTask t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(getInstance().getMessageCode(requestType)));
                    ContentPanel panel = getInstance().getServicePanel(requestType, constructorArgs);
                    if (panelListener != null) {
                        panel.addPropertyChangeListener(panelListener);
                    }
                    if (panel != null) {
                        mainPanel.addPanel(panel, getInstance().getCardName(requestType), true);
                        result[0] = true;
                    }
                    return null;
                }

                @Override
                protected void taskDone() {
                    if (result[0]) {
                        // Check if additional steps need to be executed 
                        if (taskDone != null) {
                            taskDone.run();
                        }
                    } else {
                        // The service could not be started. Check it is mapped by the service launcher
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_FAIL_SERVICE_START,
                                new Object[]{CacheManager.getBeanByCode(CacheManager.getRequestTypes(),
                            requestType).getDisplayValue()});
                    }
                }
            };
            TaskManager.getInstance().runTask(t);
        } else {
            // The service could not be started. Check it is mapped by the service launcher
            MessageUtility.displayMessage(ClientMessage.APPLICATION_FAIL_SERVICE_START,
                    new Object[]{CacheManager.getBeanByCode(CacheManager.getRequestTypes(),
                requestType).getDisplayValue()});
        }
    }

    /**
     * Checks if mapping information has been configured for the request type
     *
     * @param requestType The type of service to launch
     * @return true if the ServiceLauncher has been configured with mapping
     * information for the request type.
     */
    public static boolean isMapped(String requestType) {
        return getInstance().getCardName(requestType) != null;
    }

    /**
     * Determines if the request type is within the specified request category.
     *
     * @param requestType The type of the service
     * @param requestCategoryCode The category of the the service to check
     * against
     * @return True if the request type is in the specified category, false
     * otherwise.
     */
    public static boolean isServiceCategory(String requestType, String requestCategoryCode) {
        RequestTypeBean bean = CacheManager.getBeanByCode(CacheManager.getRequestTypes(),
                requestType);
        return requestCategoryCode.equals(bean.getRequestCategoryCode());
    }
}
