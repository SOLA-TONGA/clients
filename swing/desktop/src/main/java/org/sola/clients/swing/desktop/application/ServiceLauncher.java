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

        //Register Lease
        serviceMap.put(RequestTypeBean.CODE_REGISTER_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Transfer Lease
        serviceMap.put(RequestTypeBean.CODE_TRANSFER_LEASE,
                new String[]{TongaPropertyPanel.class.getName(), MainContentPanel.CARD_PROPERTY_PANEL,
            ClientMessage.PROGRESS_MSG_OPEN_PROPERTY});

        // Cancel Property (Lease or Allotment)
        serviceMap.put(RequestTypeBean.CODE_CANCEL_PROPERTY,
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
                    // is declared with boxed data types instead of primitive data types. 
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
     * @return true if the form is successfully launched, false otherwise.
     */
    public static boolean launch(final String requestType,
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
                    if (taskDone != null) {
                        taskDone.run();
                    }
                }
            };
            TaskManager.getInstance().runTask(t);
        }
        return result[0];
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
