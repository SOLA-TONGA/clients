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

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.sola.clients.beans.minister.MinisterApplicationBean;
import org.sola.clients.beans.minister.MinisterApplicationSearchParamsBean;
import org.sola.clients.beans.minister.MinisterApplicationSearchResultListBean;
import org.sola.clients.beans.minister.MinisterInwardBean;
import org.sola.clients.beans.minister.MinisterInwardSearchParamsBean;
import org.sola.clients.beans.minister.MinisterInwardSearchResultListBean;
import org.sola.clients.beans.minister.MinisterLeaseBean;
import org.sola.clients.beans.minister.MinisterLeaseSearchParamsBean;
import org.sola.clients.beans.minister.MinisterLeaseSearchResultListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.controls.WatermarkDate;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.common.utils.FormattersFactory;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.DateTimeRenderer;
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
        ministerApplicationSearchResultListBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(MinisterApplicationSearchResultListBean.SELECTED_MINISTER_APP_SEARCH_RESULT_PROPERTY)) {
                    customizeButtons();
                }
            }
        });
        ministerTab.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                
            }
        });
            KeyAdapter evtInward=  new KeyAdapter() {
                        public void keyReleased(KeyEvent evt) {
                            if (evt.getKeyCode() == evt.VK_ENTER) {
                                executeInwardSearch(ministerInwardSearchParamsBean, lblInwardSearchCount, ministerInwardSearchResultListBean);
                            }
                        }
                        public void keyTyped(KeyEvent e) {}
                        public void keyPressed(KeyEvent e) {}

                    };
                    nameInwardTextField.addKeyListener(evtInward);
                    fileNumberTextField.addKeyListener(evtInward);
                    subjectTextField.addKeyListener(evtInward);
                    dateInFromTextField.addKeyListener(evtInward);
                    dateInToTextField.addKeyListener(evtInward);
                    dateOutFromTextField.addKeyListener(evtInward);
                    dateOutToTextField.addKeyListener(evtInward);
            KeyAdapter evtLease=  new KeyAdapter() {
                        public void keyReleased(KeyEvent evt) {
                            if (evt.getKeyCode() == evt.VK_ENTER) {
                                executeLeaseSearch(ministerLeaseSearchParamsBean, lblLeaseSearchCount, ministerLeaseSearchResultListBean);
                            }
                        }
                        public void keyTyped(KeyEvent e) {}
                        public void keyPressed(KeyEvent e) {}
                    };
                    nameLeaseTextField.addKeyListener(evtLease);
                    locationTextField.addKeyListener(evtLease);
                    receiptNumberTextField.addKeyListener(evtLease);
                    receivedFromTextField.addKeyListener(evtLease);
                    receivedToTextField.addKeyListener(evtLease);
                    payDateFromTextField.addKeyListener(evtLease);
                    payDateToTextField.addKeyListener(evtLease);
            KeyAdapter evtApp=  new KeyAdapter() {
                        public void keyReleased(KeyEvent evt) {
                            if (evt.getKeyCode() == evt.VK_ENTER) {
                                executeApplicationSearch(ministerApplicationSearchParamsBean, lblApplicationSearchCount, ministerApplicationSearchResultListBean);
                            }
                        }
                        public void keyTyped(KeyEvent e) {}
                        public void keyPressed(KeyEvent e) {}
                    };
                    nameAppTextField.addKeyListener(evtApp);
                    locationAppTextField.addKeyListener(evtApp);
                    receiptAppTextField.addKeyListener(evtApp);
                    receivedFromAppTextField.addKeyListener(evtApp);
                    receivedToAppTextField.addKeyListener(evtApp);
                    payDateAppFromTextField.addKeyListener(evtApp);
                    payDateAppToTextField.addKeyListener(evtApp);
        customizeButtons();
    }
    
    private void customizeButtons() {
        boolean hasEditRole = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        boolean inwardEnabled = ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null;
        boolean leaseEnabled = ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null;
        boolean applicationEnabled = ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult() != null;

        inwardEnabled = inwardEnabled && hasEditRole;
        leaseEnabled = leaseEnabled && hasEditRole;
        applicationEnabled = applicationEnabled && hasEditRole;

        if (inwardEnabled && ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult() != null) {
            inwardEnabled = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        }
        if (leaseEnabled && ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null) {
            leaseEnabled = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        }
        if (applicationEnabled && ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult() != null) {
            applicationEnabled = SecurityBean.isInRole(RolesConstants.MINISTER_EDIT);
        }
        btnAddInward.setEnabled(hasEditRole);
        btnEditInward.setEnabled(inwardEnabled);
        btnRemoveInward.setEnabled(inwardEnabled);
        btnOpenInward.setEnabled(inwardEnabled);
        btnAddLease.setEnabled(hasEditRole);
        btnEditLease.setEnabled(leaseEnabled);
        btnRemoveLease.setEnabled(leaseEnabled);
        btnOpenLease.setEnabled(leaseEnabled);
        btnAddApp.setEnabled(hasEditRole);
        btnEditApp.setEnabled(applicationEnabled);
        btnRemoveApp.setEnabled(applicationEnabled);
        btnOpenApp.setEnabled(applicationEnabled);

    }
    
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
    
    private void executeInwardSearch(final MinisterInwardSearchParamsBean params,
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
    
    private void executeLeaseSearch(final MinisterLeaseSearchParamsBean params,
            final JLabel lblSearchCount, final MinisterLeaseSearchResultListBean results) {

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                results.search(params);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchCount.setText(Integer.toString(results.getMinisterLeaseResultList().size()));
                if (results.getMinisterLeaseResultList().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (results.getMinisterLeaseResultList().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    
    private void executeApplicationSearch(final MinisterApplicationSearchParamsBean params,
            final JLabel lblSearchCount, final MinisterApplicationSearchResultListBean results) {

        
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                results.search(params);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchCount.setText(Integer.toString(results.getMinisterApplicationResultList().size()));
                if (results.getMinisterApplicationResultList().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (results.getMinisterApplicationResultList().size() > 100) {
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
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_INWARD_FORM));
                MinisterInwardForm form = new MinisterInwardForm();
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_INWARD_FORM, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }
    
    public void addMinisterLease() {

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_LEASE_FORM));
                MinisterLeaseForm form = new MinisterLeaseForm();
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_LEASE_FORM, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);

    }
    
    public void addMinisterApplication() {

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_APPLICATION_FORM));
                MinisterApplicationForm form = new MinisterApplicationForm();
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_APPLICATION_FORM, true);
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
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_INWARD_FORM));
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()), true);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_INWARD_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void openMinisterLease() {
        if (ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_LEASE_FORM));
                    MinisterLeaseForm form = new MinisterLeaseForm(MinisterLeaseBean.getMinisterLease(ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getId()), true);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_LEASE_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void openMinisterApplication() {
        if (ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_APPLICATION_FORM));
                    MinisterApplicationForm form = new MinisterApplicationForm(MinisterApplicationBean.getMinisterApplication(ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getId()), true);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_APPLICATION_FORM, true);
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
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_SEARCH));
                        MinisterInwardBean.removeMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId());
                        return null;
                    }

                    @Override
                    public void taskDone() {
                        // Rerun the search to show that the inward is removed. 
                        executeInwardSearch(ministerInwardSearchParamsBean, lblInwardSearchCount, ministerInwardSearchResultListBean);
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }

    }
    
    public void removeMinisterLease() {

        if (ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null) {
            int button = MessageUtility.displayMessage(ClientMessage.MINISTER_REMOVE_LEASE,
                    new Object [] {ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getName()});
            if (button == MessageUtility.BUTTON_ONE) {
                SolaTask t = new SolaTask<Void, Void>() {

                    @Override
                    public Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_SEARCH));
                        MinisterLeaseBean.removeMinisterLease(ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getId());
                        return null;
                    }

                    @Override
                    public void taskDone() {
                        // Rerun the search to show that the inward is removed. 
                        executeLeaseSearch(ministerLeaseSearchParamsBean, lblInwardSearchCount, ministerLeaseSearchResultListBean);
                    }
                };
                TaskManager.getInstance().runTask(t);
            }
        }

    }
    
    public void removeMinisterApplication() {

        if (ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult() != null) {
            int button = MessageUtility.displayMessage(ClientMessage.MINISTER_REMOVE_APP,
                    new Object [] {ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getName()});
            if (button == MessageUtility.BUTTON_ONE) {
                SolaTask t = new SolaTask<Void, Void>() {

                    @Override
                    public Void doTask() {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_SEARCH));
                        MinisterApplicationBean.removeMinisterApplication(ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getId());
                        return null;
                    }

                    @Override
                    public void taskDone() {
                        // Rerun the search to show that the inward is removed. 
                        executeApplicationSearch(ministerApplicationSearchParamsBean, lblInwardSearchCount, ministerApplicationSearchResultListBean);
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
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_INWARD_FORM));
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()), false);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_INWARD_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void editMinisterLease() {
        if (ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_LEASE_FORM));
                    MinisterLeaseForm form = new MinisterLeaseForm(MinisterLeaseBean.getMinisterLease(ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getId()), false);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_LEASE_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    public void editMinisterApplication() {
        if (ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_MINISTER_APPLICATION_FORM));
                    MinisterApplicationForm form = new MinisterApplicationForm(MinisterApplicationBean.getMinisterApplication(ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getId()), false);
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_APPLICATION_FORM, true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
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
        ministerApplicationSearchParamsBean = new org.sola.clients.beans.minister.MinisterApplicationSearchParamsBean();
        ministerApplicationSearchResultListBean = new org.sola.clients.beans.minister.MinisterApplicationSearchResultListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        ministerTab = new javax.swing.JTabbedPane();
        inwardTab = new javax.swing.JPanel();
        inwardToolBar = new javax.swing.JToolBar();
        btnInwardSearch = new javax.swing.JButton();
        btnClearInward = new javax.swing.JButton();
        btnOpenInward = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnAddInward = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEditInward = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemoveInward = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        lblSearchResult = new javax.swing.JLabel();
        lblInwardSearchCount = new javax.swing.JLabel();
        inwardSearchPanel = new javax.swing.JPanel();
        dateOutFromTextField = new WatermarkDate();
        jLabel5 = new javax.swing.JLabel();
        dateInFromTextField = new WatermarkDate();
        jLabel4 = new javax.swing.JLabel();
        subjectTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        nameInwardTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fileNumberTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        dateInToTextField = new WatermarkDate();
        btnDateInTo = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        dateOutToTextField = new WatermarkDate();
        btnDateOutTo = new javax.swing.JButton();
        btnDateInFrom = new javax.swing.JButton();
        btnDateOutFrom = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableInward = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        leaseTab = new javax.swing.JPanel();
        lblDateReceivedFrom = new javax.swing.JLabel();
        receivedFromTextField = new WatermarkDate();
        lblDateReceivedTo = new javax.swing.JLabel();
        receivedToTextField = new WatermarkDate();
        leaseToolBar = new javax.swing.JToolBar();
        btnLeaseSearch = new javax.swing.JButton();
        btnClearLease = new javax.swing.JButton();
        btnOpenLease = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnAddLease = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEditLease = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemoveLease = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        lblSearchResult1 = new javax.swing.JLabel();
        lblLeaseSearchCount = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        nameLeaseTextField = new javax.swing.JTextField();
        locationTextField = new javax.swing.JTextField();
        lblLocation = new javax.swing.JLabel();
        lblPayDate = new javax.swing.JLabel();
        receiptNumberTextField = new javax.swing.JTextField();
        lblReceiptNumber = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableLease = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        btnDateReceivedTo = new javax.swing.JButton();
        btnDateReceivedFrom = new javax.swing.JButton();
        payDateFromTextField = new WatermarkDate();
        btnPayDateFrom = new javax.swing.JButton();
        lblDateReceivedTo1 = new javax.swing.JLabel();
        payDateToTextField = new WatermarkDate();
        btnPayDateTo = new javax.swing.JButton();
        applicationTab = new javax.swing.JPanel();
        appToolBar = new javax.swing.JToolBar();
        btnAppSearch = new javax.swing.JButton();
        btnClearApp = new javax.swing.JButton();
        btnOpenApp = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnAddApp = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEditApp = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemoveApp = new org.sola.clients.swing.common.buttons.BtnRemove();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        lblSearchResult2 = new javax.swing.JLabel();
        lblApplicationSearchCount = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nameAppTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        locationAppTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        receiptAppTextField = new javax.swing.JTextField();
        payDateAppFromTextField = new WatermarkDate();
        receivedFromAppTextField = new WatermarkDate();
        receivedToAppTextField = new WatermarkDate();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableApp = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        btnAppReceivedFrom = new javax.swing.JButton();
        btnAppPayDate = new javax.swing.JButton();
        btnAppReceivedTo = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        payDateAppToTextField = new WatermarkDate();
        btnPayDateAppTo = new javax.swing.JButton();

        setHeaderPanel(headerPanel);

        headerPanel.setTitleText("Minister's Office");

        inwardToolBar.setFloatable(false);
        inwardToolBar.setRollover(true);

        btnInwardSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        btnInwardSearch.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnInwardSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInwardSearchActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnInwardSearch);

        btnClearInward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClearInward.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClearInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearInwardActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnClearInward);

        btnOpenInward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenInward.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenInward.setEnabled(false);
        btnOpenInward.setFocusable(false);
        btnOpenInward.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenInward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenInwardActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnOpenInward);
        inwardToolBar.add(jSeparator3);

        btnAddInward.setEnabled(false);
        btnAddInward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInwardActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnAddInward);

        btnEditInward.setEnabled(false);
        btnEditInward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditInwardActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnEditInward);

        btnRemoveInward.setEnabled(false);
        btnRemoveInward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveInwardActionPerformed(evt);
            }
        });
        inwardToolBar.add(btnRemoveInward);
        inwardToolBar.add(jSeparator1);

        lblSearchResult.setText("Search results: ");
        inwardToolBar.add(lblSearchResult);

        lblInwardSearchCount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inwardToolBar.add(lblInwardSearchCount);

        dateOutFromTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateOutFrom}"), dateOutFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel5.setText("Date Out (From)");

        dateInFromTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateInFrom}"), dateInFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel4.setText("Date In (From)");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${subject}"), subjectTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel1.setText("Subject");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${fromWhom}"), nameInwardTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel2.setText("Name");

        jLabel3.setText("File Number");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${fileNumber}"), fileNumberTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel12.setText("Date In (To)");

        dateInToTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateInTo}"), dateInToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnDateInTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateInTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateInTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateInTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateInToActionPerformed(evt);
            }
        });

        jLabel13.setText("Date Out (To)");

        dateOutToTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateOutTo}"), dateOutToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnDateOutTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateOutTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateOutTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateOutTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateOutToActionPerformed(evt);
            }
        });

        btnDateInFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateInFrom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateInFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateInFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateInFromActionPerformed(evt);
            }
        });

        btnDateOutFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateOutFrom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateOutFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateOutFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateOutFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inwardSearchPanelLayout = new javax.swing.GroupLayout(inwardSearchPanel);
        inwardSearchPanel.setLayout(inwardSearchPanelLayout);
        inwardSearchPanelLayout.setHorizontalGroup(
            inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subjectTextField)
                    .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                        .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                                .addComponent(nameInwardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                                .addComponent(dateOutFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDateOutFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateOutToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDateOutTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                                .addComponent(dateInFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDateInFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateInToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDateInTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 482, Short.MAX_VALUE)))
                .addGap(13, 13, 13))
        );
        inwardSearchPanelLayout.setVerticalGroup(
            inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameInwardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(fileNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inwardSearchPanelLayout.createSequentialGroup()
                        .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateInFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dateInToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addComponent(btnDateInTo)
                            .addComponent(btnDateInFrom, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dateOutFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(inwardSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dateOutToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnDateOutTo, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addComponent(btnDateOutFrom))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerInwardResultList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchResultListBean, eLProperty, jTableInward);
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
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterInwardSearchResult}"), jTableInward, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jTableInward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableInwardMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableInward);
        jTableInward.getColumnModel().getColumn(3).setCellRenderer(new DateTimeRenderer());
        jTableInward.getColumnModel().getColumn(4).setCellRenderer(new DateTimeRenderer());

        javax.swing.GroupLayout inwardTabLayout = new javax.swing.GroupLayout(inwardTab);
        inwardTab.setLayout(inwardTabLayout);
        inwardTabLayout.setHorizontalGroup(
            inwardTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inwardSearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(inwardToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE)
            .addComponent(jScrollPane4)
        );
        inwardTabLayout.setVerticalGroup(
            inwardTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inwardTabLayout.createSequentialGroup()
                .addComponent(inwardSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inwardToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE))
        );

        ministerTab.addTab("Inward", inwardTab);

        lblDateReceivedFrom.setText("Date Received (From)");

        receivedFromTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedFrom}"), receivedFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        lblDateReceivedTo.setText("Date Received (To)");

        receivedToTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedTo}"), receivedToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        leaseToolBar.setFloatable(false);
        leaseToolBar.setRollover(true);

        btnLeaseSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnLeaseSearch.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnLeaseSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseSearchActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnLeaseSearch);

        btnClearLease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClearLease.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClearLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearLeaseActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnClearLease);

        btnOpenLease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenLease.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenLease.setEnabled(false);
        btnOpenLease.setFocusable(false);
        btnOpenLease.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenLease.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLeaseActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnOpenLease);
        leaseToolBar.add(jSeparator4);

        btnAddLease.setEnabled(false);
        btnAddLease.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLeaseActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnAddLease);

        btnEditLease.setEnabled(false);
        btnEditLease.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditLeaseActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnEditLease);

        btnRemoveLease.setEnabled(false);
        btnRemoveLease.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveLeaseActionPerformed(evt);
            }
        });
        leaseToolBar.add(btnRemoveLease);
        leaseToolBar.add(jSeparator2);

        lblSearchResult1.setText("Search results: ");
        leaseToolBar.add(lblSearchResult1);

        lblLeaseSearchCount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        leaseToolBar.add(lblLeaseSearchCount);

        lblName.setText("Name");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${name}"), nameLeaseTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${location}"), locationTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblLocation.setText("Location");

        lblPayDate.setText("Pay Date (From)");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"), receiptNumberTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblReceiptNumber.setText("Receipt #");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerLeaseResultList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchResultListBean, eLProperty, jTableLease, "jTableLeaseResultList");
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${location}"));
        columnBinding.setColumnName("Location");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"));
        columnBinding.setColumnName("Receipt Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${payDate}"));
        columnBinding.setColumnName("Pay Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateReceived}"));
        columnBinding.setColumnName("Date Received");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterLeaseSearchResult}"), jTableLease, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jTableLease.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLeaseMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableLease);
        jTableLease.getColumnModel().getColumn(3).setCellRenderer(new DateTimeRenderer());
        jTableLease.getColumnModel().getColumn(4).setCellRenderer(new DateTimeRenderer());

        btnDateReceivedTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateReceivedTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateReceivedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateReceivedToActionPerformed(evt);
            }
        });

        btnDateReceivedFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateReceivedFrom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateReceivedFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateReceivedFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateReceivedFromActionPerformed(evt);
            }
        });

        payDateFromTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDateFrom}"), payDateFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnPayDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnPayDateFrom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPayDateFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPayDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDateFromActionPerformed(evt);
            }
        });

        lblDateReceivedTo1.setText("Pay Date (To)");

        payDateToTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDateTo}"), payDateToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnPayDateTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnPayDateTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPayDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDateToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leaseTabLayout = new javax.swing.GroupLayout(leaseTab);
        leaseTab.setLayout(leaseTabLayout);
        leaseTabLayout.setHorizontalGroup(
            leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaseToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(leaseTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReceiptNumber)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaseTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLocation, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(receiptNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(locationTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLeaseTextField, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPayDate)
                    .addComponent(lblDateReceivedFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaseTabLayout.createSequentialGroup()
                        .addComponent(receivedFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnDateReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(leaseTabLayout.createSequentialGroup()
                        .addComponent(payDateFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leaseTabLayout.createSequentialGroup()
                        .addComponent(lblDateReceivedTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receivedToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDateReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leaseTabLayout.createSequentialGroup()
                        .addComponent(lblDateReceivedTo1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(payDateToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        leaseTabLayout.setVerticalGroup(
            leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaseTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leaseTabLayout.createSequentialGroup()
                        .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(nameLeaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLocation)
                            .addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(leaseTabLayout.createSequentialGroup()
                        .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblDateReceivedTo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(receivedToTextField)
                                .addComponent(receivedFromTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblDateReceivedFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDateReceivedFrom, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(btnDateReceivedTo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnPayDateFrom, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(payDateFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblDateReceivedTo1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(payDateToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnPayDateTo))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReceiptNumber)
                    .addComponent(receiptNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(leaseToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE))
        );

        ministerTab.addTab("Lease", leaseTab);

        appToolBar.setFloatable(false);
        appToolBar.setRollover(true);

        btnAppSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnAppSearch.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnAppSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppSearchActionPerformed(evt);
            }
        });
        appToolBar.add(btnAppSearch);

        btnClearApp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClearApp.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClearApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAppActionPerformed(evt);
            }
        });
        appToolBar.add(btnClearApp);

        btnOpenApp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenApp.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenApp.setEnabled(false);
        btnOpenApp.setFocusable(false);
        btnOpenApp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenApp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenAppActionPerformed(evt);
            }
        });
        appToolBar.add(btnOpenApp);
        appToolBar.add(jSeparator5);

        btnAddApp.setEnabled(false);
        btnAddApp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAppActionPerformed(evt);
            }
        });
        appToolBar.add(btnAddApp);

        btnEditApp.setEnabled(false);
        btnEditApp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditAppActionPerformed(evt);
            }
        });
        appToolBar.add(btnEditApp);

        btnRemoveApp.setEnabled(false);
        btnRemoveApp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveAppActionPerformed(evt);
            }
        });
        appToolBar.add(btnRemoveApp);
        appToolBar.add(jSeparator6);

        lblSearchResult2.setText("Search results: ");
        appToolBar.add(lblSearchResult2);

        lblApplicationSearchCount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        appToolBar.add(lblApplicationSearchCount);

        jLabel6.setText("Name");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${name}"), nameAppTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel7.setText("Location");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${location}"), locationAppTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel8.setText("Receipt #");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"), receiptAppTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        payDateAppFromTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDateFrom}"), payDateAppFromTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        receivedFromAppTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedFrom}"), receivedFromAppTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        receivedToAppTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        receivedToAppTextField.setText(" ");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedTo}"), receivedToAppTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel9.setText("Date Received (From)");

        jLabel10.setText("Date Received (To)");

        jLabel11.setText("Pay Date (From)");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerApplicationResultList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchResultListBean, eLProperty, jTableApp);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${location}"));
        columnBinding.setColumnName("Location");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${surveyFee}"));
        columnBinding.setColumnName("Survey Fee");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${payDate}"));
        columnBinding.setColumnName("Pay Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"));
        columnBinding.setColumnName("Receipt Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateReceived}"));
        columnBinding.setColumnName("Date Received");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterApplicationSearchResult}"), jTableApp, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jTableApp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAppMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableApp);
        jTableApp.getColumnModel().getColumn(3).setCellRenderer(new DateTimeRenderer());
        jTableApp.getColumnModel().getColumn(5).setCellRenderer(new DateTimeRenderer());

        btnAppReceivedFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnAppReceivedFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppReceivedFromActionPerformed(evt);
            }
        });

        btnAppPayDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnAppPayDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppPayDateActionPerformed(evt);
            }
        });

        btnAppReceivedTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnAppReceivedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppReceivedToActionPerformed(evt);
            }
        });

        jLabel14.setText("Pay Date (To)");

        payDateAppToTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        payDateAppToTextField.setText(" ");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDateTo}"), payDateAppToTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnPayDateAppTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnPayDateAppTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDateAppToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout applicationTabLayout = new javax.swing.GroupLayout(applicationTab);
        applicationTab.setLayout(applicationTabLayout);
        applicationTabLayout.setHorizontalGroup(
            applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(applicationTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(applicationTabLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(applicationTabLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(locationAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, applicationTabLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receiptAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(receivedFromAppTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(payDateAppFromTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(applicationTabLayout.createSequentialGroup()
                        .addComponent(btnAppReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receivedToAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAppReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(applicationTabLayout.createSequentialGroup()
                        .addComponent(btnAppPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(payDateAppToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayDateAppTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(166, Short.MAX_VALUE))
        );
        applicationTabLayout.setVerticalGroup(
            applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(applicationTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(receivedToAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAppReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(nameAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(receivedFromAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnAppReceivedTo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(applicationTabLayout.createSequentialGroup()
                        .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(locationAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(payDateAppFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11))
                            .addComponent(btnAppPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(receiptAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(applicationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(payDateAppToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addComponent(btnPayDateAppTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE))
        );

        ministerTab.addTab("Land Application", applicationTab);

        ministerTab.setSelectedComponent(inwardTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ministerTab)
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

    private void btnInwardSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInwardSearchActionPerformed
        executeInwardSearch(ministerInwardSearchParamsBean, lblInwardSearchCount, ministerInwardSearchResultListBean);
    }//GEN-LAST:event_btnInwardSearchActionPerformed

    private void btnClearInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearInwardActionPerformed
        ministerInwardSearchParamsBean.clear();
    }//GEN-LAST:event_btnClearInwardActionPerformed

    private void btnOpenInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenInwardActionPerformed
        openMinisterInward();
    }//GEN-LAST:event_btnOpenInwardActionPerformed

    private void btnAddInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInwardActionPerformed
        addMinisterInward();
    }//GEN-LAST:event_btnAddInwardActionPerformed

    private void btnEditInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditInwardActionPerformed
        editMinisterInward();
    }//GEN-LAST:event_btnEditInwardActionPerformed

    private void btnRemoveInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveInwardActionPerformed
        removeMinisterInward();
    }//GEN-LAST:event_btnRemoveInwardActionPerformed

    private void btnLeaseSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseSearchActionPerformed
        executeLeaseSearch(ministerLeaseSearchParamsBean, lblLeaseSearchCount, ministerLeaseSearchResultListBean);
    }//GEN-LAST:event_btnLeaseSearchActionPerformed

    private void btnClearLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearLeaseActionPerformed
        ministerLeaseSearchParamsBean.clear();
    }//GEN-LAST:event_btnClearLeaseActionPerformed

    private void btnOpenLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenLeaseActionPerformed
        openMinisterLease();
    }//GEN-LAST:event_btnOpenLeaseActionPerformed

    private void btnAddLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLeaseActionPerformed
        addMinisterLease();
    }//GEN-LAST:event_btnAddLeaseActionPerformed

    private void btnEditLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditLeaseActionPerformed
        editMinisterLease();
    }//GEN-LAST:event_btnEditLeaseActionPerformed

    private void btnRemoveLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveLeaseActionPerformed
        removeMinisterLease();
    }//GEN-LAST:event_btnRemoveLeaseActionPerformed

    private void btnDateReceivedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateReceivedToActionPerformed
        showCalendar(receivedToTextField);
    }//GEN-LAST:event_btnDateReceivedToActionPerformed

    private void btnDateReceivedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateReceivedFromActionPerformed
        showCalendar(receivedFromTextField);
    }//GEN-LAST:event_btnDateReceivedFromActionPerformed

    private void btnAppSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppSearchActionPerformed
        executeApplicationSearch(ministerApplicationSearchParamsBean, lblApplicationSearchCount, ministerApplicationSearchResultListBean);
    }//GEN-LAST:event_btnAppSearchActionPerformed

    private void btnClearAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAppActionPerformed
        ministerApplicationSearchParamsBean.clear();
    }//GEN-LAST:event_btnClearAppActionPerformed

    private void btnOpenAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenAppActionPerformed
        openMinisterApplication();
    }//GEN-LAST:event_btnOpenAppActionPerformed

    private void btnAddAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAppActionPerformed
        addMinisterApplication();
    }//GEN-LAST:event_btnAddAppActionPerformed

    private void btnEditAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditAppActionPerformed
        editMinisterApplication();
    }//GEN-LAST:event_btnEditAppActionPerformed

    private void btnRemoveAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveAppActionPerformed
        removeMinisterApplication();
    }//GEN-LAST:event_btnRemoveAppActionPerformed

    private void btnAppPayDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppPayDateActionPerformed
        showCalendar(payDateAppFromTextField);
    }//GEN-LAST:event_btnAppPayDateActionPerformed

    private void btnAppReceivedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppReceivedFromActionPerformed
        showCalendar(receivedFromAppTextField);
    }//GEN-LAST:event_btnAppReceivedFromActionPerformed

    private void btnAppReceivedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppReceivedToActionPerformed
        showCalendar(receivedToAppTextField);
    }//GEN-LAST:event_btnAppReceivedToActionPerformed

    private void jTableLeaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLeaseMouseClicked
        if (evt.getClickCount() == 2) {
            openMinisterLease();
        }
    }//GEN-LAST:event_jTableLeaseMouseClicked

    private void jTableAppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAppMouseClicked
        if (evt.getClickCount() == 2) {
            openMinisterApplication();
        }
    }//GEN-LAST:event_jTableAppMouseClicked

    private void btnDateInToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateInToActionPerformed
        showCalendar(dateInToTextField);
    }//GEN-LAST:event_btnDateInToActionPerformed

    private void btnDateOutToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateOutToActionPerformed
        showCalendar(dateOutToTextField);
    }//GEN-LAST:event_btnDateOutToActionPerformed

    private void btnPayDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDateFromActionPerformed
        showCalendar(payDateFromTextField);
    }//GEN-LAST:event_btnPayDateFromActionPerformed

    private void btnPayDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDateToActionPerformed
        showCalendar(payDateToTextField);
    }//GEN-LAST:event_btnPayDateToActionPerformed

    private void btnPayDateAppToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDateAppToActionPerformed
        showCalendar(payDateAppToTextField);
    }//GEN-LAST:event_btnPayDateAppToActionPerformed

    private void jTableInwardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInwardMouseClicked
        if (evt.getClickCount() == 2) {
            openMinisterInward();
        }
    }//GEN-LAST:event_jTableInwardMouseClicked

    private void btnDateInFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateInFromActionPerformed
        showCalendar(dateInFromTextField);
    }//GEN-LAST:event_btnDateInFromActionPerformed

    private void btnDateOutFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateOutFromActionPerformed
        showCalendar(dateOutFromTextField);
    }//GEN-LAST:event_btnDateOutFromActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar appToolBar;
    private javax.swing.JPanel applicationTab;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddApp;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddInward;
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddLease;
    private javax.swing.JButton btnAppPayDate;
    private javax.swing.JButton btnAppReceivedFrom;
    private javax.swing.JButton btnAppReceivedTo;
    public javax.swing.JButton btnAppSearch;
    private javax.swing.JButton btnClearApp;
    private javax.swing.JButton btnClearInward;
    private javax.swing.JButton btnClearLease;
    private javax.swing.JButton btnDateInFrom;
    private javax.swing.JButton btnDateInTo;
    private javax.swing.JButton btnDateOutFrom;
    private javax.swing.JButton btnDateOutTo;
    private javax.swing.JButton btnDateReceivedFrom;
    private javax.swing.JButton btnDateReceivedTo;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEditApp;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEditInward;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEditLease;
    public javax.swing.JButton btnInwardSearch;
    public javax.swing.JButton btnLeaseSearch;
    private javax.swing.JButton btnOpenApp;
    private javax.swing.JButton btnOpenInward;
    private javax.swing.JButton btnOpenLease;
    private javax.swing.JButton btnPayDateAppTo;
    private javax.swing.JButton btnPayDateFrom;
    private javax.swing.JButton btnPayDateTo;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveApp;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveInward;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveLease;
    private javax.swing.JFormattedTextField dateInFromTextField;
    private javax.swing.JFormattedTextField dateInToTextField;
    private javax.swing.JFormattedTextField dateOutFromTextField;
    private javax.swing.JFormattedTextField dateOutToTextField;
    private javax.swing.JTextField fileNumberTextField;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel inwardSearchPanel;
    private javax.swing.JPanel inwardTab;
    private javax.swing.JToolBar inwardToolBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableApp;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableInward;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableLease;
    private javax.swing.JLabel lblApplicationSearchCount;
    private javax.swing.JLabel lblDateReceivedFrom;
    private javax.swing.JLabel lblDateReceivedTo;
    private javax.swing.JLabel lblDateReceivedTo1;
    private javax.swing.JLabel lblInwardSearchCount;
    private javax.swing.JLabel lblLeaseSearchCount;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPayDate;
    private javax.swing.JLabel lblReceiptNumber;
    private javax.swing.JLabel lblSearchResult;
    private javax.swing.JLabel lblSearchResult1;
    private javax.swing.JLabel lblSearchResult2;
    private javax.swing.JPanel leaseTab;
    private javax.swing.JToolBar leaseToolBar;
    private javax.swing.JTextField locationAppTextField;
    private javax.swing.JTextField locationTextField;
    private org.sola.clients.beans.minister.MinisterApplicationSearchParamsBean ministerApplicationSearchParamsBean;
    private org.sola.clients.beans.minister.MinisterApplicationSearchResultListBean ministerApplicationSearchResultListBean;
    private org.sola.clients.beans.minister.MinisterInwardSearchParamsBean ministerInwardSearchParamsBean;
    private org.sola.clients.beans.minister.MinisterInwardSearchResultListBean ministerInwardSearchResultListBean;
    private org.sola.clients.beans.minister.MinisterLeaseSearchParamsBean ministerLeaseSearchParamsBean;
    private org.sola.clients.beans.minister.MinisterLeaseSearchResultListBean ministerLeaseSearchResultListBean;
    private javax.swing.JTabbedPane ministerTab;
    private javax.swing.JTextField nameAppTextField;
    private javax.swing.JTextField nameInwardTextField;
    private javax.swing.JTextField nameLeaseTextField;
    private javax.swing.JFormattedTextField payDateAppFromTextField;
    private javax.swing.JFormattedTextField payDateAppToTextField;
    private javax.swing.JFormattedTextField payDateFromTextField;
    private javax.swing.JFormattedTextField payDateToTextField;
    private javax.swing.JTextField receiptAppTextField;
    private javax.swing.JTextField receiptNumberTextField;
    private javax.swing.JFormattedTextField receivedFromAppTextField;
    private javax.swing.JFormattedTextField receivedFromTextField;
    private javax.swing.JFormattedTextField receivedToAppTextField;
    private javax.swing.JFormattedTextField receivedToTextField;
    private javax.swing.JTextField subjectTextField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
