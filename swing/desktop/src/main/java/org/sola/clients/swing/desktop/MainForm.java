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
package org.sola.clients.swing.desktop;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.source.PowerOfAttorneyBean;
import org.sola.clients.swing.common.DefaultExceptionHandler;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.LocalizationManager;
import org.sola.clients.swing.common.controls.LanguageCombobox;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.administrative.BaUnitSearchPanel;
import org.sola.clients.swing.desktop.administrative.CashierImportForm;
import org.sola.clients.swing.desktop.drafting.DraftingSearchPanel;
import org.sola.clients.swing.desktop.administrative.RightsExportForm;
import org.sola.clients.swing.desktop.application.ApplicationSearchPanel;
import org.sola.clients.swing.desktop.application.TongaApplicationPanel;
import org.sola.clients.swing.desktop.cadastre.MapPanelForm;
import org.sola.clients.swing.desktop.cadastre.MapPublicDisplayPanel;
import org.sola.clients.swing.desktop.cadastre.MapSpatialUnitGroupEditPanel;
import org.sola.clients.swing.desktop.minister.MinisterSearchPanel;
import org.sola.clients.swing.desktop.party.PartySearchPanelForm;
import org.sola.clients.swing.desktop.reports.LodgementReportParamsForm;
import org.sola.clients.swing.desktop.reports.SysRegCertParamsForm;
import org.sola.clients.swing.desktop.reports.SysRegListingParamsForm;
import org.sola.clients.swing.desktop.reports.SysRegManagementParamsForm;
import org.sola.clients.swing.desktop.source.DocumentSearchForm;
import org.sola.clients.swing.desktop.source.PowerOfAttorneyViewForm;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.RolesConstants;
import org.sola.common.WindowUtility;
import org.sola.common.help.HelpUtility;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Main form of the application.
 */
public class MainForm extends javax.swing.JFrame {

    public static final String MAIN_FORM_HEIGHT = "mainFormHeight";
    public static final String MAIN_FORM_WIDTH = "mainFormWitdh";
    public static final String MAIN_FORM_TOP = "mainFormTop";
    public static final String MAIN_FORM_LEFT = "mainFormLeft";
    private ApplicationSearchPanel searchApplicationPanel;
    private DocumentSearchForm searchDocPanel;
    private PartySearchPanelForm searchPartyPanel;
    private BaUnitSearchPanel searchBaUnitPanel;
    private DraftingSearchPanel draughtingSearch;
    private MinisterSearchPanel ministerSearch;
    // Create a variable holding the listener
    KeyAdapter keyAdapterAppSearch = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                launchAppSearchMethod(searchApplicationPanel);
            }
        }
    };
    // Create a variable holding the listener
    KeyAdapter keyAdapterDocSearch = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                launchDocSearchMethod(searchDocPanel);
            }
        }
    };
    // Create a variable holding the listener
    KeyAdapter keyAdapterBaUnitSearch = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                launchBaUnitSearchMethod(searchBaUnitPanel);
            }
        }
    };
    // Create a variable holding the listener
    KeyAdapter keyAdapterPartySearch = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                launchPartySearchMethod(searchPartyPanel);
            }
        }
    };

    public ApplicationSearchPanel getSearchApplicationPanel() {
        return searchApplicationPanel;
    }

    public void setSearchApplicationPanel(ApplicationSearchPanel searchApplicationPanel) {
        this.searchApplicationPanel = searchApplicationPanel;
    }

    public BaUnitSearchPanel getSearchBaUnitPanel() {
        return searchBaUnitPanel;
    }

    public void setSearchBaUnitPanel(BaUnitSearchPanel searchBaUnitPanel) {
        this.searchBaUnitPanel = searchBaUnitPanel;
    }
    
    public DraftingSearchPanel getDraughtingSearch() {
        return draughtingSearch;
    }

    public void setDraughtingSearch(DraftingSearchPanel draughtingSearch) {
        this.draughtingSearch = draughtingSearch;
    }
    
     public MinisterSearchPanel getMinisterInwardSearch() {
        return ministerSearch;
    }

    public void setMinisterInwardSearch(MinisterSearchPanel ministerSearch) {
        this.ministerSearch = ministerSearch;
    }

    public DocumentSearchForm getSearchDocPanel() {
        return searchDocPanel;
    }

    public void setSearchDocPanel(DocumentSearchForm searchDocPanel) {
        this.searchDocPanel = searchDocPanel;
    }

    public PartySearchPanelForm getSearchPartyPanel() {
        return searchPartyPanel;
    }

    public void setSearchPartyPanel(PartySearchPanelForm searchPartyPanel) {
        this.searchPartyPanel = searchPartyPanel;
    }

    /**
     * Private class to hold singleton instance of the MainForm.
     */
    private static class MainFormHolder {

        private static final MainForm INSTANCE = new MainForm();
    }

    /**
     * Returns a singleton instance of {@link MainForm}.
     */
    public static MainForm getInstance() {
        return MainFormHolder.INSTANCE;
    }

    /**
     * Default constructor.
     */
    private MainForm() {
        URL imgURL = this.getClass().getResource("/images/sola/logo_icon.jpg");
        this.setIconImage(new ImageIcon(imgURL).getImage());

        initComponents();
        HelpUtility.getInstance().registerHelpMenu(jmiContextHelp, "overview");
        this.setTitle("SOLA Desktop - " + LocalizationManager.getVersionNumber());

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                postInit();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                preClose();
            }
        });
    }

    /**
     * Create combobox with languages
     */
    private LanguageCombobox createLanguageCombobox() {
        return new LanguageCombobox();
    }

    /**
     * Runs post initialization tasks. Enables or disables toolbar buttons and
     * menu items depending on user rights. Loads various data after the form
     * has been opened. It helps to display form with no significant delays.
     */
    private void postInit() {
        // Set screen size and location 
        configureForm();

        // Customize buttons
        btnNewApplication.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_CREATE_APPS));
        btnOpenMap.setEnabled(SecurityBean.isInRole(RolesConstants.GIS_VIEW_MAP));
        btnOpenMap.setVisible(btnOpenMap.isEnabled()); 
        btnSearchApplications.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_VIEW_APPS));
        btnShowDashboard.setEnabled(SecurityBean.isInRole(RolesConstants.DASHBOARD_VIEW_ASSIGNED_APPS,
                RolesConstants.DASHBOARD_VIEW_UNASSIGNED_APPS));
        btnManageParties.setEnabled(SecurityBean.isInRole(RolesConstants.PARTY_SEARCH));
        btnOpenBaUnitSearch.setEnabled(SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_BA_UNIT_SEARCH));
        btnDocumentSearch.setEnabled(SecurityBean.isInRole(RolesConstants.SOURCE_SEARCH));
        btnSetPassword.setEnabled(SecurityBean.isInRole(RolesConstants.ADMIN_CHANGE_PASSWORD));
        btnDraughtingSearch.setVisible(SecurityBean.isInRole(RolesConstants.DRAFTING_SEARCH));
        btnMinisterSearch.setVisible(SecurityBean.isInRole(RolesConstants.MINISTER_SEARCH));

        menuSearchApplication.setEnabled(btnSearchApplications.isEnabled());
        menuNewApplication.setEnabled(btnNewApplication.isEnabled());
        menuExportRights.setEnabled(SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_RIGHTS_EXPORT));
        menuCashierImport.setEnabled(SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_CASHIER_IMPORT));
        menuBaUnitSearch.setEnabled(btnOpenBaUnitSearch.isEnabled());
        menuPersons.setEnabled(btnManageParties.isEnabled());
        menuShowMap.setEnabled(btnOpenMap.isEnabled());
        menuLodgementReport.setEnabled(SecurityBean.isInRole(RolesConstants.REPORTS_VIEW));
        menuDocumentSearch.setEnabled(btnDocumentSearch.isEnabled());
        menuDraftingSearch.setVisible(btnDraughtingSearch.isVisible());
        menuMinisterSearch.setVisible(btnMinisterSearch.isVisible()); 
        
        // AM 17-04-13 Systematic Registration is not required in Tonga.
        menuMap.setEnabled(btnOpenMap.isEnabled());
        menuMap.setVisible(btnOpenMap.isVisible());   
        menuSystematic.setVisible(false);
        
        // Hide other menu's that are not required for Tonga
        menuExportRights.setVisible(false);
        menuLangIT.setVisible(false);

        if (SecurityBean.isPasswordChangeReqd(false)) {
            // Load the user profile page
            showPasswordPanel();
        } else {
            if (btnShowDashboard.isEnabled()) {
                // Load dashboard
                openDashBoard();
            }
        }

        txtUserName.setText(SecurityBean.getCurrentUser().getUserName());
    }

    /**
     * Sets the screen size and location based on the settings stored in the
     * users preferences.
     */
    private void configureForm() {

        int height = this.getHeight();
        int width = this.getWidth();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((dim.width) / 2) - (width / 2);
        int y = ((dim.height) / 2) - (height / 2);

        if (WindowUtility.hasUserPreferences()) {
            // Set the size of the screen
            Preferences prefs = WindowUtility.getUserPreferences();
            height = Integer.parseInt(prefs.get(MAIN_FORM_HEIGHT, Integer.toString(height)));
            width = Integer.parseInt(prefs.get(MAIN_FORM_WIDTH, Integer.toString(width)));
            y = Integer.parseInt(prefs.get(MAIN_FORM_TOP, Integer.toString(y)));
            x = Integer.parseInt(prefs.get(MAIN_FORM_LEFT, Integer.toString(x)));

            // Check if the screen sizes are within the bounds of the users 
            // physical screen. e.g. may have been using dual monitor. 
            if (height > dim.height || height < 50) {
                height = dim.height - 50 - y;
            }
            if (width > dim.width || width < 200) {
                width = dim.width - 20 - x;
            }
            if (y + 10 > dim.height || y + height - 100 < 0) {
                y = 5;
            }
            if (x + 10 > dim.width || x + width - 100 < 0) {
                x = 10;
            }
            this.setSize(width, height);
        }
        this.setLocation(x, y);
    }

    /**
     * Captures the screen size and location and saves them as the users
     * preference just before the screen is is closed.
     */
    private void preClose() {
        if (WindowUtility.hasUserPreferences()) {
            Preferences prefs = WindowUtility.getUserPreferences();
            prefs.put(MAIN_FORM_HEIGHT, Integer.toString(this.getHeight()));
            prefs.put(MAIN_FORM_WIDTH, Integer.toString(this.getWidth()));
            prefs.put(MAIN_FORM_TOP, Integer.toString(this.getY()));
            prefs.put(MAIN_FORM_LEFT, Integer.toString(this.getX()));
        }
    }

    private void setAllLogLevel() {
        LogUtility.setLogLevel(Level.ALL);
    }

    private void setDefaultLogLevel() {
        LogUtility.setLogLevel(Level.INFO);
    }

    private void setOffLogLevel() {
        LogUtility.setLogLevel(Level.OFF);
    }

    private void openMap() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MAP));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_MAP)) {
                    MapPanelForm mapPanel = new MapPanelForm();
                    pnlContent.addPanel(mapPanel, MainContentPanel.CARD_MAP);
                }
                pnlContent.showPanel(MainContentPanel.CARD_MAP);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openMapPublicDisplay() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MAP));
                if (!pnlContent.isPanelOpened(MapPublicDisplayPanel.PANEL_NAME)) {
                    MapPublicDisplayPanel mapPanel = new MapPublicDisplayPanel();
                    pnlContent.addPanel(mapPanel, MapPublicDisplayPanel.PANEL_NAME);
                }
                pnlContent.showPanel(MapPublicDisplayPanel.PANEL_NAME);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openMapSpatialUnitGroupEditor() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MAP));
                if (!pnlContent.isPanelOpened(MapSpatialUnitGroupEditPanel.PANEL_NAME)) {
                    MapSpatialUnitGroupEditPanel mapPanel = new MapSpatialUnitGroupEditPanel();
                    pnlContent.addPanel(mapPanel, MapSpatialUnitGroupEditPanel.PANEL_NAME);
                }
                pnlContent.showPanel(MapSpatialUnitGroupEditPanel.PANEL_NAME);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void searchApplications() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APPSEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_APPSEARCH)) {
                    ApplicationSearchPanel searchApplicationPanel = new ApplicationSearchPanel();
                    setSearchApplicationPanel(searchApplicationPanel);
                    pnlContent.addPanel(searchApplicationPanel, MainContentPanel.CARD_APPSEARCH);

                }

                pnlContent.showPanel(MainContentPanel.CARD_APPSEARCH);
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_APPSEARCH);
            }
        };
        TaskManager.getInstance().runTask(t);


    }

    private void searchBaUnit() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTYSEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_BAUNIT_SEARCH)) {
                    BaUnitSearchPanel baUnitSearchPanel = new BaUnitSearchPanel();
                    setSearchBaUnitPanel(baUnitSearchPanel);
                    pnlContent.addPanel(baUnitSearchPanel, MainContentPanel.CARD_BAUNIT_SEARCH);
                }
                pnlContent.showPanel(MainContentPanel.CARD_BAUNIT_SEARCH);
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_BAUNIT_SEARCH);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void searchDocuments() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DOCUMENTSEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_DOCUMENT_SEARCH)) {
                    DocumentSearchForm documentSearchPanel = new DocumentSearchForm();
                    setSearchDocPanel(documentSearchPanel);
                    pnlContent.addPanel(documentSearchPanel, MainContentPanel.CARD_DOCUMENT_SEARCH);
                }
                pnlContent.showPanel(MainContentPanel.CARD_DOCUMENT_SEARCH);
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_DOCUMENT_SEARCH);
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    private void searchDrafting() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_DRAFTING_SEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_DRAUGHTING_SEARCH)) {
                    DraftingSearchPanel draughtingSearch = new DraftingSearchPanel();
                    setDraughtingSearch(draughtingSearch);
                    pnlContent.addPanel(draughtingSearch, MainContentPanel.CARD_DRAUGHTING_SEARCH);
                }
                pnlContent.showPanel(MainContentPanel.CARD_DRAUGHTING_SEARCH);
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_DRAUGHTING_SEARCH);
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    private void searchMinister() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_SEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_DRAUGHTING_SEARCH)) {
                    MinisterSearchPanel ministerSearch = new MinisterSearchPanel();
                    setMinisterInwardSearch(ministerSearch);
                    pnlContent.addPanel(ministerSearch, MainContentPanel.CARD_MINISTER_SEARCH);
                }
                pnlContent.showPanel(MainContentPanel.CARD_MINISTER_SEARCH);
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_MINISTER_SEARCH);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openSearchParties() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PERSONSEARCH));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_SEARCH_PERSONS)) {
                    PartySearchPanelForm partySearchPanelForm = new PartySearchPanelForm();
                    pnlContent.addPanel(partySearchPanelForm, MainContentPanel.CARD_SEARCH_PERSONS, true);
                    setSearchPartyPanel(partySearchPanelForm);
                } else {
                    pnlContent.showPanel(MainContentPanel.CARD_SEARCH_PERSONS);
                }
                return null;
            }

            @Override
            protected void taskDone() {
                addKeyListeners(MainContentPanel.CARD_SEARCH_PERSONS);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void addKeyListeners(final String card) {

        if (card.contentEquals(MainContentPanel.CARD_APPSEARCH)) {
            pnlContent.addKeyListener(keyAdapterAppSearch);
            pnlContent.removeKeyListener(keyAdapterDocSearch);
            pnlContent.removeKeyListener(keyAdapterBaUnitSearch);
            pnlContent.removeKeyListener(keyAdapterPartySearch);
        }
        if (card.contentEquals(MainContentPanel.CARD_DOCUMENT_SEARCH)) {
            pnlContent.addKeyListener(keyAdapterDocSearch);
            pnlContent.removeKeyListener(keyAdapterAppSearch);
            pnlContent.removeKeyListener(keyAdapterBaUnitSearch);
            pnlContent.removeKeyListener(keyAdapterPartySearch);
        }
        if (card.contentEquals(MainContentPanel.CARD_BAUNIT_SEARCH)) {
            pnlContent.addKeyListener(keyAdapterBaUnitSearch);
            pnlContent.removeKeyListener(keyAdapterDocSearch);
            pnlContent.removeKeyListener(keyAdapterAppSearch);
            pnlContent.removeKeyListener(keyAdapterPartySearch);

        }
        if (card.contentEquals(MainContentPanel.CARD_SEARCH_PERSONS)) {
            pnlContent.addKeyListener(keyAdapterPartySearch);
            pnlContent.removeKeyListener(keyAdapterDocSearch);
            pnlContent.removeKeyListener(keyAdapterBaUnitSearch);
            pnlContent.removeKeyListener(keyAdapterAppSearch);

        }
        pnlContent.setFocusable(true);
        pnlContent.requestFocusInWindow();

    }

    public void launchAppSearchMethod(final ApplicationSearchPanel panel) {
        panel.clickFind();
    }

    public void launchDocSearchMethod(DocumentSearchForm panel) {
        panel.clickFind();
    }

    public void launchBaUnitSearchMethod(BaUnitSearchPanel panel) {
        panel.clickFind();
    }

    public void launchPartySearchMethod(PartySearchPanelForm panel) {
        panel.clickFind();
    }

    private void openDashBoard() {
        if (!pnlContent.isPanelOpened(MainContentPanel.CARD_DASHBOARD)) {
            DashBoardPanel dashBoard = new DashBoardPanel();
            pnlContent.addPanel(dashBoard, MainContentPanel.CARD_DASHBOARD);
        }
        pnlContent.showPanel(MainContentPanel.CARD_DASHBOARD);
    }

    public MainContentPanel getMainContentPanel() {
        return pnlContent;
    }

    private void showAboutBox() {
        AboutForm aboutBox = new AboutForm(this);
        aboutBox.setLocationRelativeTo(this);
        aboutBox.setVisible(true);
    }

    private void setLanguage(String code, String country) {
        LocalizationManager.setLanguage(code, country);
        MessageUtility.displayMessage(ClientMessage.GENERAL_UPDATE_LANG);
    }

    /**
     * Calls {@link AbstractBindingBean#saveStateHash()} method to make a hash
     * of object's state
     */
    public static void saveBeanState(AbstractBindingBean bean) {
        try {
            bean.saveStateHash();
        } catch (IOException ex) {
            DefaultExceptionHandler.handleException(ex);
        } catch (NoSuchAlgorithmException ex) {
            DefaultExceptionHandler.handleException(ex);
        }
    }

    /**
     * Calls {@link AbstractBindingBean#hasChanges()} method to detect if there
     * are any changes on the provided bean. <br /> Note, to check for the
     * changes, you should call {@link AbstractBindingBean#saveStateHash()}
     * before calling this method.
     */
    public static boolean checkBeanState(AbstractBindingBean bean) {
        try {
            return bean.hasChanges();
        } catch (IOException ex) {
            DefaultExceptionHandler.handleException(ex);
            return true;
        } catch (NoSuchAlgorithmException ex) {
            DefaultExceptionHandler.handleException(ex);
            return true;
        }
    }

    /**
     * Calls
     * {@link MainForm#checkBeanState(org.sola.clients.beans.AbstractBindingBean)}
     * method to detect if there are any changes on the provided bean. If it
     * returns true, warning message is shown and the result of user selection
     * is returned. If user clicks <b>Yes</b> button to confirm saving changes,
     * true is returned.
     */
    public static boolean checkSaveBeforeClose(AbstractBindingBean bean) {
        boolean hasChanges = false;
        if (checkBeanState(bean)) {
            if (MessageUtility.displayMessage(ClientMessage.GENERAL_FORM_CHANGES_WARNING) == MessageUtility.BUTTON_ONE) {
                hasChanges = true;
            } else {
                hasChanges = false;
            }
        }
        return hasChanges;
    }

    /**
     * Opens Application form and shows provided application.
     *
     * @param app Application to show on the form.
     */
    public void openApplicationForm(final ApplicationBean app) {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APP));
                TongaApplicationPanel form = new TongaApplicationPanel(app);
                getMainContentPanel().addPanel(form, MainContentPanel.CARD_APPLICATION, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Opens Application form and shows provided application.
     *
     * @param ID Application ID to load application by and show on the form.
     */
    public void openApplicationForm(final String id) {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APP));
                TongaApplicationPanel form = new TongaApplicationPanel(id);
                getMainContentPanel().addPanel(form, MainContentPanel.CARD_APPLICATION, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Opens Application form to create new application.
     */
    public void openApplicationForm() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APPNEW));
                TongaApplicationPanel applicationPanel = new TongaApplicationPanel();
                getMainContentPanel().addPanel(applicationPanel, MainContentPanel.CARD_APPLICATION, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void showRightsExportPanel() {
        if (getMainContentPanel().isPanelOpened(MainContentPanel.CARD_RIGHT_EXPORT)) {
            getMainContentPanel().showPanel(MainContentPanel.CARD_RIGHT_EXPORT);
        } else {
            RightsExportForm form = new RightsExportForm();
            getMainContentPanel().addPanel(form, MainContentPanel.CARD_RIGHT_EXPORT, true);
        }
    }
    
    private void showCashierImportPanel() {
        if (getMainContentPanel().isPanelOpened(MainContentPanel.CARD_CASHIER_IMPORT)) {
            getMainContentPanel().showPanel(MainContentPanel.CARD_CASHIER_IMPORT);
        } else {
            CashierImportForm form = new CashierImportForm();
            getMainContentPanel().addPanel(form, MainContentPanel.CARD_CASHIER_IMPORT, true);
        }
    }

    /**
     * Opens {@link PowerOfAttorneyViewForm} form and shows provided document.
     *
     * @param powerOfAttorney Power of attorney to show on the form.
     */
    public void openDocumentViewForm(final PowerOfAttorneyBean powerOfAttorney) {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_DOCUMENT_FORM_OPENING));
                PowerOfAttorneyViewForm form = new PowerOfAttorneyViewForm(powerOfAttorney);
                getMainContentPanel().addPanel(form, MainContentPanel.CARD_VIEW_POWER_OF_ATTORNEY, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applicationsMain = new javax.swing.JToolBar();
        btnShowDashboard = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnNewApplication = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnSearchApplications = new javax.swing.JButton();
        btnOpenBaUnitSearch = new javax.swing.JButton();
        btnDocumentSearch = new javax.swing.JButton();
        btnDraughtingSearch = new javax.swing.JButton();
        btnMinisterSearch = new org.sola.clients.swing.common.buttons.BtnSearch();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnManageParties = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOpenMap = new javax.swing.JButton();
        btnSetPassword = new javax.swing.JButton();
        languageCombobox = createLanguageCombobox();
        statusPanel = new javax.swing.JPanel();
        labStatus = new javax.swing.JLabel();
        taskPanel1 = new org.sola.clients.swing.common.tasks.TaskPanel();
        txtUserName = new javax.swing.JLabel();
        pnlContent = new org.sola.clients.swing.ui.MainContentPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuExportRights = new javax.swing.JMenuItem();
        menuCashierImport = new javax.swing.JMenuItem();
        javax.swing.JMenuItem menuExitItem = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        menuLanguage = new javax.swing.JMenu();
        menuLangEN = new javax.swing.JMenuItem();
        menuLangIT = new javax.swing.JMenuItem();
        menuLogLevel = new javax.swing.JMenu();
        menuAllLogLevel = new javax.swing.JMenuItem();
        menuDefaultLogLevel = new javax.swing.JMenuItem();
        menuOffLogLevel = new javax.swing.JMenuItem();
        menuApplications = new javax.swing.JMenu();
        menuNewApplication = new javax.swing.JMenuItem();
        menuSearch = new javax.swing.JMenu();
        menuSearchApplication = new javax.swing.JMenuItem();
        menuBaUnitSearch = new javax.swing.JMenuItem();
        menuDocumentSearch = new javax.swing.JMenuItem();
        menuDraftingSearch = new javax.swing.JMenuItem();
        menuMinisterSearch = new javax.swing.JMenuItem();
        menuPersons = new javax.swing.JMenuItem();
        menuMap = new javax.swing.JMenu();
        menuShowMap = new javax.swing.JMenuItem();
        menuReportsDesktop = new javax.swing.JMenu();
        menuLodgementReport = new javax.swing.JMenuItem();
        menuSystematic = new javax.swing.JMenu();
        menuPublicDisplay = new javax.swing.JMenu();
        menuPublicNotification = new javax.swing.JMenuItem();
        menuOwnerName = new javax.swing.JMenuItem();
        menuStateLand = new javax.swing.JMenuItem();
        menuItemMapPublicDisplay = new javax.swing.JMenuItem();
        menuCertificates = new javax.swing.JMenuItem();
        menuReports = new javax.swing.JMenu();
        menuStatus = new javax.swing.JMenuItem();
        menuProgress = new javax.swing.JMenuItem();
        menuSpatialUnitGroup = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jmiContextHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/Bundle"); // NOI18N
        setTitle(bundle.getString("MainForm.title")); // NOI18N

        applicationsMain.setFloatable(false);
        applicationsMain.setRollover(true);
        applicationsMain.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        applicationsMain.setMaximumSize(new java.awt.Dimension(32769, 32769));
        applicationsMain.setMinimumSize(new java.awt.Dimension(90, 45));
        applicationsMain.setPreferredSize(new java.awt.Dimension(980, 45));
        applicationsMain.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        btnShowDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/home.png"))); // NOI18N
        btnShowDashboard.setText(bundle.getString("MainForm.btnShowDashboard.text")); // NOI18N
        btnShowDashboard.setFocusable(false);
        btnShowDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnShowDashboard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnShowDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDashboardActionPerformed(evt);
            }
        });
        applicationsMain.add(btnShowDashboard);
        applicationsMain.add(jSeparator2);

        btnNewApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/new.png"))); // NOI18N
        btnNewApplication.setText(bundle.getString("MainForm.btnNewApplication.text")); // NOI18N
        btnNewApplication.setFocusable(false);
        btnNewApplication.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNewApplication.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewApplicationActionPerformed(evt);
            }
        });
        applicationsMain.add(btnNewApplication);
        applicationsMain.add(jSeparator4);

        btnSearchApplications.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSearchApplications.setText(bundle.getString("MainForm.btnSearchApplications.text")); // NOI18N
        btnSearchApplications.setFocusable(false);
        btnSearchApplications.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSearchApplications.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSearchApplications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchApplicationsActionPerformed(evt);
            }
        });
        applicationsMain.add(btnSearchApplications);

        btnOpenBaUnitSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnOpenBaUnitSearch.setText(bundle.getString("MainForm.btnOpenBaUnitSearch.text")); // NOI18N
        btnOpenBaUnitSearch.setFocusable(false);
        btnOpenBaUnitSearch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenBaUnitSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenBaUnitSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenBaUnitSearchActionPerformed(evt);
            }
        });
        applicationsMain.add(btnOpenBaUnitSearch);

        btnDocumentSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnDocumentSearch.setText(bundle.getString("MainForm.btnDocumentSearch.text")); // NOI18N
        btnDocumentSearch.setFocusable(false);
        btnDocumentSearch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDocumentSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDocumentSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentSearchActionPerformed(evt);
            }
        });
        applicationsMain.add(btnDocumentSearch);

        btnDraughtingSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnDraughtingSearch.setText(bundle.getString("MainForm.btnDraughtingSearch.text")); // NOI18N
        btnDraughtingSearch.setFocusable(false);
        btnDraughtingSearch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDraughtingSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDraughtingSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDraughtingSearchActionPerformed(evt);
            }
        });
        applicationsMain.add(btnDraughtingSearch);

        btnMinisterSearch.setText(bundle.getString("MainForm.btnMinisterSearch.text")); // NOI18N
        btnMinisterSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMinisterSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinisterSearchActionPerformed(evt);
            }
        });
        applicationsMain.add(btnMinisterSearch);
        applicationsMain.add(jSeparator3);

        btnManageParties.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/users.png"))); // NOI18N
        btnManageParties.setText(bundle.getString("MainForm.btnManageParties.text")); // NOI18N
        btnManageParties.setFocusable(false);
        btnManageParties.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnManageParties.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnManageParties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagePartiesActionPerformed(evt);
            }
        });
        applicationsMain.add(btnManageParties);
        applicationsMain.add(jSeparator1);

        btnOpenMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/network.png"))); // NOI18N
        btnOpenMap.setText(bundle.getString("MainForm.btnOpenMap.text")); // NOI18N
        btnOpenMap.setFocusable(false);
        btnOpenMap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenMap.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenMapActionPerformed(evt);
            }
        });
        applicationsMain.add(btnOpenMap);

        btnSetPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lock--pencil.png"))); // NOI18N
        btnSetPassword.setText(bundle.getString("MainForm.btnSetPassword.text")); // NOI18N
        btnSetPassword.setFocusable(false);
        btnSetPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSetPassword.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPasswordActionPerformed(evt);
            }
        });
        applicationsMain.add(btnSetPassword);

        languageCombobox.setPreferredSize(new java.awt.Dimension(150, 20));
        languageCombobox.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        languageCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageComboboxActionPerformed(evt);
            }
        });
        applicationsMain.add(languageCombobox);

        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusPanel.setPreferredSize(new java.awt.Dimension(1024, 24));

        labStatus.setFont(LafManager.getInstance().getLabFontBold());
        labStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labStatus.setText(bundle.getString("MainForm.labStatus.text")); // NOI18N

        txtUserName.setText(bundle.getString("MainForm.txtUserName.text")); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taskPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
            .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
            .addComponent(taskPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        menuBar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        menuBar.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        fileMenu.setText(bundle.getString("MainForm.fileMenu.text")); // NOI18N

        menuExportRights.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/export.png"))); // NOI18N
        menuExportRights.setText(bundle.getString("MainForm.menuExportRights.text")); // NOI18N
        menuExportRights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExportRightsActionPerformed(evt);
            }
        });
        fileMenu.add(menuExportRights);

        menuCashierImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/export.png"))); // NOI18N
        menuCashierImport.setText(bundle.getString("MainForm.menuCashierImport.text")); // NOI18N
        menuCashierImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCashierImportActionPerformed(evt);
            }
        });
        fileMenu.add(menuCashierImport);

        menuExitItem.setText(bundle.getString("MainForm.menuExitItem.text")); // NOI18N
        menuExitItem.setToolTipText(bundle.getString("MainForm.menuExitItem.toolTipText")); // NOI18N
        menuExitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitItemActionPerformed(evt);
            }
        });
        fileMenu.add(menuExitItem);

        menuBar.add(fileMenu);

        menuView.setText(bundle.getString("MainForm.menuView.text")); // NOI18N

        menuLanguage.setText(bundle.getString("MainForm.menuLanguage.text")); // NOI18N

        menuLangEN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flags/en.jpg"))); // NOI18N
        menuLangEN.setText(bundle.getString("MainForm.menuLangEN.text")); // NOI18N
        menuLangEN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLangENActionPerformed(evt);
            }
        });
        menuLanguage.add(menuLangEN);

        menuLangIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flags/it.jpg"))); // NOI18N
        menuLangIT.setText(bundle.getString("MainForm.menuLangIT.text")); // NOI18N
        menuLangIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLangITActionPerformed(evt);
            }
        });
        menuLanguage.add(menuLangIT);

        menuView.add(menuLanguage);

        menuLogLevel.setText(bundle.getString("MainForm.menuLogLevel.text")); // NOI18N

        menuAllLogLevel.setText(bundle.getString("MainForm.menuAllLogLevel.text")); // NOI18N
        menuAllLogLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAllLogLevelActionPerformed(evt);
            }
        });
        menuLogLevel.add(menuAllLogLevel);

        menuDefaultLogLevel.setText(bundle.getString("MainForm.menuDefaultLogLevel.text")); // NOI18N
        menuDefaultLogLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDefaultLogLevelActionPerformed(evt);
            }
        });
        menuLogLevel.add(menuDefaultLogLevel);

        menuOffLogLevel.setText(bundle.getString("MainForm.menuOffLogLevel.text")); // NOI18N
        menuOffLogLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOffLogLevelActionPerformed(evt);
            }
        });
        menuLogLevel.add(menuOffLogLevel);

        menuView.add(menuLogLevel);

        menuBar.add(menuView);

        menuApplications.setText(bundle.getString("MainForm.menuApplications.text")); // NOI18N

        menuNewApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/new.png"))); // NOI18N
        menuNewApplication.setText(bundle.getString("MainForm.menuNewApplication.text")); // NOI18N
        menuNewApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewApplicationActionPerformed(evt);
            }
        });
        menuApplications.add(menuNewApplication);

        menuBar.add(menuApplications);

        menuSearch.setText(bundle.getString("MainForm.menuSearch.text")); // NOI18N

        menuSearchApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        menuSearchApplication.setText(bundle.getString("MainForm.menuSearchApplication.text")); // NOI18N
        menuSearchApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSearchApplicationActionPerformed(evt);
            }
        });
        menuSearch.add(menuSearchApplication);

        menuBaUnitSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        menuBaUnitSearch.setText(bundle.getString("MainForm.menuBaUnitSearch.text")); // NOI18N
        menuBaUnitSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaUnitSearchActionPerformed(evt);
            }
        });
        menuSearch.add(menuBaUnitSearch);

        menuDocumentSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        menuDocumentSearch.setText(bundle.getString("MainForm.menuDocumentSearch.text")); // NOI18N
        menuDocumentSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDocumentSearchActionPerformed(evt);
            }
        });
        menuSearch.add(menuDocumentSearch);

        menuDraftingSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        menuDraftingSearch.setText(bundle.getString("MainForm.menuDraftingSearch.text")); // NOI18N
        menuDraftingSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDraftingSearchActionPerformed(evt);
            }
        });
        menuSearch.add(menuDraftingSearch);

        menuMinisterSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        menuMinisterSearch.setText(bundle.getString("MainForm.menuMinisterSearch.text")); // NOI18N
        menuMinisterSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMinisterSearchActionPerformed(evt);
            }
        });
        menuSearch.add(menuMinisterSearch);

        menuPersons.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/users.png"))); // NOI18N
        menuPersons.setText(bundle.getString("MainForm.menuPersons.text")); // NOI18N
        menuPersons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPersonsActionPerformed(evt);
            }
        });
        menuSearch.add(menuPersons);

        menuBar.add(menuSearch);

        menuMap.setText(bundle.getString("MainForm.menuMap.text")); // NOI18N

        menuShowMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/network.png"))); // NOI18N
        menuShowMap.setText(bundle.getString("MainForm.menuShowMap.text")); // NOI18N
        menuShowMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuShowMapActionPerformed(evt);
            }
        });
        menuMap.add(menuShowMap);

        menuBar.add(menuMap);

        menuReportsDesktop.setText(bundle.getString("MainForm.menuReportsDesktop.text_1")); // NOI18N

        menuLodgementReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        menuLodgementReport.setText(bundle.getString("MainForm.menuLodgementReportDesktop.text_1")); // NOI18N
        menuLodgementReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLodgementReportActionPerformed(evt);
            }
        });
        menuReportsDesktop.add(menuLodgementReport);
        menuLodgementReport.getAccessibleContext().setAccessibleName(bundle.getString("MainForm.menuLodgementReport.AccessibleContext.accessibleName")); // NOI18N

        menuBar.add(menuReportsDesktop);

        menuSystematic.setText(bundle.getString("MainForm.menuSystematic.text")); // NOI18N

        menuPublicDisplay.setText(bundle.getString("MainForm.menuPublicDisplay.text")); // NOI18N

        menuPublicNotification.setText(bundle.getString("MainForm.menuPublicNotification.text")); // NOI18N
        menuPublicNotification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPublicNotificationActionPerformed(evt);
            }
        });
        menuPublicDisplay.add(menuPublicNotification);

        menuOwnerName.setText(bundle.getString("MainForm.menuOwnerName.text")); // NOI18N
        menuOwnerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOwnerNameActionPerformed(evt);
            }
        });
        menuPublicDisplay.add(menuOwnerName);

        menuStateLand.setText(bundle.getString("MainForm.menuStateLand.text")); // NOI18N
        menuStateLand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStateLandActionPerformed(evt);
            }
        });
        menuPublicDisplay.add(menuStateLand);

        menuSystematic.add(menuPublicDisplay);

        menuItemMapPublicDisplay.setText(bundle.getString("MainForm.menuItemMapPublicDisplay.text")); // NOI18N
        menuItemMapPublicDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemMapPublicDisplayActionPerformed(evt);
            }
        });
        menuSystematic.add(menuItemMapPublicDisplay);

        menuCertificates.setText(bundle.getString("MainForm.menuCertificates.text")); // NOI18N
        menuCertificates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCertificatesActionPerformed(evt);
            }
        });
        menuSystematic.add(menuCertificates);

        menuReports.setText(bundle.getString("MainForm.menuReports.text")); // NOI18N

        menuStatus.setText(bundle.getString("MainForm.menuStatus.text")); // NOI18N
        menuStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStatusActionPerformed(evt);
            }
        });
        menuReports.add(menuStatus);

        menuProgress.setText(bundle.getString("MainForm.menuProgress.text")); // NOI18N
        menuProgress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProgressActionPerformed(evt);
            }
        });
        menuReports.add(menuProgress);

        menuSystematic.add(menuReports);

        menuSpatialUnitGroup.setText(bundle.getString("MainForm.menuSpatialUnitGroup.text")); // NOI18N
        menuSpatialUnitGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSpatialUnitGroupActionPerformed(evt);
            }
        });
        menuSystematic.add(menuSpatialUnitGroup);

        menuBar.add(menuSystematic);

        helpMenu.setText(bundle.getString("MainForm.helpMenu.text")); // NOI18N

        aboutMenuItem.setText(bundle.getString("MainForm.aboutMenuItem.text")); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jmiContextHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/help.png"))); // NOI18N
        jmiContextHelp.setText(bundle.getString("MainForm.jmiContextHelp.text")); // NOI18N
        helpMenu.add(jmiContextHelp);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
            .addComponent(applicationsMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(applicationsMain, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuShowMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuShowMapActionPerformed
        openMap();
    }//GEN-LAST:event_menuShowMapActionPerformed

    private void menuExitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuExitItemActionPerformed

    private void menuAllLogLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAllLogLevelActionPerformed
        setAllLogLevel();
    }//GEN-LAST:event_menuAllLogLevelActionPerformed

    private void menuDefaultLogLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDefaultLogLevelActionPerformed
        setDefaultLogLevel();
    }//GEN-LAST:event_menuDefaultLogLevelActionPerformed

    private void menuOffLogLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOffLogLevelActionPerformed
        setOffLogLevel();
    }//GEN-LAST:event_menuOffLogLevelActionPerformed

    private void menuNewApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewApplicationActionPerformed
        openApplicationForm();
    }//GEN-LAST:event_menuNewApplicationActionPerformed

    private void menuSearchApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSearchApplicationActionPerformed
        searchApplications();
    }//GEN-LAST:event_menuSearchApplicationActionPerformed

    private void menuBaUnitSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaUnitSearchActionPerformed
        searchBaUnit();
    }//GEN-LAST:event_menuBaUnitSearchActionPerformed

    private void menuDocumentSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDocumentSearchActionPerformed
        searchDocuments();
    }//GEN-LAST:event_menuDocumentSearchActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        showAboutBox();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void btnShowDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDashboardActionPerformed
        openDashBoard();
    }//GEN-LAST:event_btnShowDashboardActionPerformed

    private void btnNewApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewApplicationActionPerformed
        openApplicationForm();
    }//GEN-LAST:event_btnNewApplicationActionPerformed

    private void btnSearchApplicationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchApplicationsActionPerformed
        searchApplications();
    }//GEN-LAST:event_btnSearchApplicationsActionPerformed

    private void btnDocumentSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentSearchActionPerformed
        searchDocuments();
    }//GEN-LAST:event_btnDocumentSearchActionPerformed

    private void btnManagePartiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagePartiesActionPerformed
        openSearchParties();
    }//GEN-LAST:event_btnManagePartiesActionPerformed

    private void btnOpenMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenMapActionPerformed
        openMap();
    }//GEN-LAST:event_btnOpenMapActionPerformed

    private void menuLangENActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLangENActionPerformed
        setLanguage("en", "US");
    }//GEN-LAST:event_menuLangENActionPerformed

    private void menuLangITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLangITActionPerformed
        setLanguage("it", "IT");
    }//GEN-LAST:event_menuLangITActionPerformed

    private void btnOpenBaUnitSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenBaUnitSearchActionPerformed
        searchBaUnit();
    }//GEN-LAST:event_btnOpenBaUnitSearchActionPerformed

    private void openLodgementReportParamsForm() {
        LodgementReportParamsForm reportDateChooser = new LodgementReportParamsForm(this, true);
        reportDateChooser.setVisible(true);
    }

    private void openSysRegListingParamsForm(String report) {
        SysRegListingParamsForm reportDateChooser = new SysRegListingParamsForm(this, true, report);
        reportDateChooser.setVisible(true);
    }

    private void openSysRegCertificatesParamsForm() {
        SysRegCertParamsForm certificateGenerator = new SysRegCertParamsForm(null, true);
        certificateGenerator.setVisible(true);
    }

    private void openSysRegManagementParamsForm(String whichReport) {
        SysRegManagementParamsForm managementGenerator = new SysRegManagementParamsForm(this, true, whichReport);
        managementGenerator.setVisible(true);
    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setVisible(true);
    }

    private void menuLodgementReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLodgementReportActionPerformed
        openLodgementReportParamsForm();
    }//GEN-LAST:event_menuLodgementReportActionPerformed

    private void menuPersonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPersonsActionPerformed
        openSearchParties();
    }//GEN-LAST:event_menuPersonsActionPerformed

    private void btnSetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPasswordActionPerformed
        editPassword();
    }//GEN-LAST:event_btnSetPasswordActionPerformed

    private void menuPublicNotificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPublicNotificationActionPerformed
        openSysRegListingParamsForm("ParcelNumber");
    }//GEN-LAST:event_menuPublicNotificationActionPerformed

    private void menuOwnerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOwnerNameActionPerformed
        openSysRegListingParamsForm("Owners");
    }//GEN-LAST:event_menuOwnerNameActionPerformed

    private void menuStateLandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStateLandActionPerformed
        openSysRegListingParamsForm("StateLand");
    }//GEN-LAST:event_menuStateLandActionPerformed

    private void menuCertificatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCertificatesActionPerformed
        openSysRegCertificatesParamsForm();
    }//GEN-LAST:event_menuCertificatesActionPerformed

    private void menuItemMapPublicDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemMapPublicDisplayActionPerformed
        openMapPublicDisplay();
    }//GEN-LAST:event_menuItemMapPublicDisplayActionPerformed

    private void menuStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStatusActionPerformed
        openSysRegManagementParamsForm("sysRegStatusBean");
    }//GEN-LAST:event_menuStatusActionPerformed

    private void languageComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageComboboxActionPerformed

        if (!(MessageUtility.displayMessage(ClientMessage.CONFIRM_CHANGE_LANGUAGE) == MessageUtility.BUTTON_ONE)) {
            languageCombobox.confirmedChange = false;
        } else {
            languageCombobox.confirmedChange = true;
        }


        if (languageCombobox.confirmedChange) {
            final MainForm mainForm = new MainForm();
            this.dispose();

            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainForm.setVisible(true);
                }
            });
        }

        languageCombobox.confirmedChange = true;
    }//GEN-LAST:event_languageComboboxActionPerformed

    private void menuExportRightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExportRightsActionPerformed
        showRightsExportPanel();
    }//GEN-LAST:event_menuExportRightsActionPerformed

    private void menuProgressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProgressActionPerformed
        openSysRegManagementParamsForm("sysRegProgressBean");
    }//GEN-LAST:event_menuProgressActionPerformed

    private void menuSpatialUnitGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSpatialUnitGroupActionPerformed
        openMapSpatialUnitGroupEditor();
    }//GEN-LAST:event_menuSpatialUnitGroupActionPerformed

    private void menuCashierImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCashierImportActionPerformed
        showCashierImportPanel();
    }//GEN-LAST:event_menuCashierImportActionPerformed

    private void btnDraughtingSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDraughtingSearchActionPerformed
        searchDrafting();
    }//GEN-LAST:event_btnDraughtingSearchActionPerformed

    private void btnMinisterSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinisterSearchActionPerformed
        searchMinister();
    }//GEN-LAST:event_btnMinisterSearchActionPerformed

    private void menuDraftingSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDraftingSearchActionPerformed
        searchDrafting();
    }//GEN-LAST:event_menuDraftingSearchActionPerformed

    private void menuMinisterSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMinisterSearchActionPerformed
        searchMinister();
    }//GEN-LAST:event_menuMinisterSearchActionPerformed

    private void editPassword() {
        showPasswordPanel();
    }

    /**
     * Shows password panel.
     */
    private void showPasswordPanel() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
//                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MAP));
                if (!pnlContent.isPanelOpened(MainContentPanel.CARD_USER_PROFILE)) {
                    UserProfileForm panel = new UserProfileForm(SecurityBean.getCurrentUser().getUserName());
                    pnlContent.addPanel(panel, MainContentPanel.CARD_USER_PROFILE);
                }
                pnlContent.showPanel(MainContentPanel.CARD_USER_PROFILE);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar applicationsMain;
    private javax.swing.JButton btnDocumentSearch;
    private javax.swing.JButton btnDraughtingSearch;
    private javax.swing.JButton btnManageParties;
    private org.sola.clients.swing.common.buttons.BtnSearch btnMinisterSearch;
    private javax.swing.JButton btnNewApplication;
    private javax.swing.JButton btnOpenBaUnitSearch;
    private javax.swing.JButton btnOpenMap;
    private javax.swing.JButton btnSearchApplications;
    private javax.swing.JButton btnSetPassword;
    private javax.swing.JButton btnShowDashboard;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JMenuItem jmiContextHelp;
    private javax.swing.JLabel labStatus;
    private org.sola.clients.swing.common.controls.LanguageCombobox languageCombobox;
    private javax.swing.JMenuItem menuAllLogLevel;
    private javax.swing.JMenu menuApplications;
    private javax.swing.JMenuItem menuBaUnitSearch;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuCashierImport;
    private javax.swing.JMenuItem menuCertificates;
    private javax.swing.JMenuItem menuDefaultLogLevel;
    private javax.swing.JMenuItem menuDocumentSearch;
    private javax.swing.JMenuItem menuDraftingSearch;
    private javax.swing.JMenuItem menuExportRights;
    private javax.swing.JMenuItem menuItemMapPublicDisplay;
    private javax.swing.JMenuItem menuLangEN;
    private javax.swing.JMenuItem menuLangIT;
    private javax.swing.JMenu menuLanguage;
    private javax.swing.JMenuItem menuLodgementReport;
    private javax.swing.JMenu menuLogLevel;
    private javax.swing.JMenu menuMap;
    private javax.swing.JMenuItem menuMinisterSearch;
    private javax.swing.JMenuItem menuNewApplication;
    private javax.swing.JMenuItem menuOffLogLevel;
    private javax.swing.JMenuItem menuOwnerName;
    private javax.swing.JMenuItem menuPersons;
    private javax.swing.JMenuItem menuProgress;
    private javax.swing.JMenu menuPublicDisplay;
    private javax.swing.JMenuItem menuPublicNotification;
    private javax.swing.JMenu menuReports;
    private javax.swing.JMenu menuReportsDesktop;
    private javax.swing.JMenu menuSearch;
    private javax.swing.JMenuItem menuSearchApplication;
    private javax.swing.JMenuItem menuShowMap;
    private javax.swing.JMenuItem menuSpatialUnitGroup;
    private javax.swing.JMenuItem menuStateLand;
    private javax.swing.JMenuItem menuStatus;
    private javax.swing.JMenu menuSystematic;
    private javax.swing.JMenu menuView;
    private org.sola.clients.swing.ui.MainContentPanel pnlContent;
    private javax.swing.JPanel statusPanel;
    private org.sola.clients.swing.common.tasks.TaskPanel taskPanel1;
    private javax.swing.JLabel txtUserName;
    // End of variables declaration//GEN-END:variables
}
