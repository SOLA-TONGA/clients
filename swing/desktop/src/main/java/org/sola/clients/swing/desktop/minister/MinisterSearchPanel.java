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
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()));
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
                    MinisterLeaseForm form = new MinisterLeaseForm(MinisterLeaseBean.getMinisterLease(ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getId()));
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
                    MinisterApplicationForm form = new MinisterApplicationForm(MinisterApplicationBean.getMinisterApplication(ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getId()));
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
                    MinisterInwardForm form = new MinisterInwardForm(MinisterInwardBean.getMinisterInward(ministerInwardSearchResultListBean.getSelectedMinisterInwardSearchResult().getId()));
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
                    MinisterLeaseForm form = new MinisterLeaseForm(MinisterLeaseBean.getMinisterLease(ministerLeaseSearchResultListBean.getSelectedMinisterLeaseSearchResult().getId()));
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
                    MinisterApplicationForm form = new MinisterApplicationForm(MinisterApplicationBean.getMinisterApplication(ministerApplicationSearchResultListBean.getSelectedMinisterApplicationSearchResult().getId()));
                    MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_MINISTER_APPLICATION_FORM, true);
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
    
    public void clearMinisterLease() {
        nameLeaseTextField.setText(null);
        locationTextField.setText(null);
        receiptNumberTextField.setText(null);
        payDateTextField.setText(null);
        receivedFromTextField.setText(null);
        receivedToTextField.setText(null);
    }
    
    public void clearMinisterApplication() {
        nameAppTextField.setText(null);
        locationAppTextField.setText(null);
        receiptAppTextField.setText(null);
        receivedFromAppTextField.setText(null);
        receivedToAppTextField.setText(null);
        payDateAppTextField.setText(null);
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
        inwardPanel = new javax.swing.JPanel();
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
        jPanel4 = new javax.swing.JPanel();
        dateOutTextField = new WatermarkDate();
        jLabel5 = new javax.swing.JLabel();
        dateInTextField = new WatermarkDate();
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
        payDateTextField = new WatermarkDate();
        lblPayDate = new javax.swing.JLabel();
        receiptNumberTextField = new javax.swing.JTextField();
        lblReceiptNumber = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableLease = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        btnPayDate = new javax.swing.JButton();
        btnDateReceivedTo = new javax.swing.JButton();
        btnDateReceivedFrom = new javax.swing.JButton();
        appPanel = new javax.swing.JPanel();
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
        payDateAppTextField = new WatermarkDate();
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
        inwardToolBar.add(lblInwardSearchCount);

        dateOutTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateOut}"), dateOutTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel5.setText("Date Out");

        dateInTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateIn}"), dateInTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        dateInTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateInTextFieldActionPerformed(evt);
            }
        });

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

        btnDateIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDateIn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateInActionPerformed(evt);
            }
        });

        btnDateOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
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
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateInTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnDateIn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateOutTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDateOut, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(nameInwardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(497, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(subjectTextField)
                                .addContainerGap())))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(subjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(dateInTextField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDateIn, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateOutTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDateOut, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        jTableInward.setMaximumSize(new java.awt.Dimension(2147483647, 100000));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerInwardResultList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerInwardSearchResultListBean, eLProperty, jTableInward);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fromWhom}"));
        columnBinding.setColumnName("From Whom");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fileNumber}"));
        columnBinding.setColumnName("File Number");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${subject}"));
        columnBinding.setColumnName("Subject");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateIn}"));
        columnBinding.setColumnName("Date In");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateOut}"));
        columnBinding.setColumnName("Date Out");
        columnBinding.setColumnClass(java.util.Date.class);
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
            .addComponent(inwardToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
        );
        inwardPanelLayout.setVerticalGroup(
            inwardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inwardPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inwardToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ministerTab.addTab("Inward", inwardPanel);

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
        leaseToolBar.add(lblLeaseSearchCount);

        lblName.setText("Name");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${name}"), nameLeaseTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${location}"), locationTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblLocation.setText("Location");

        payDateTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDate}"), payDateTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        lblPayDate.setText("Pay Date");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"), receiptNumberTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblReceiptNumber.setText("Receipt Number");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerLeaseResultList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchResultListBean, eLProperty, jTableLease, "jTableLeaseResultList");
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${location}"));
        columnBinding.setColumnName("Location");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"));
        columnBinding.setColumnName("Receipt Number");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${payDate}"));
        columnBinding.setColumnName("Pay Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateReceived}"));
        columnBinding.setColumnName("Date Received");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerLeaseSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterLeaseSearchResult}"), jTableLease, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setViewportView(jTableLease);

        btnPayDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnPayDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPayDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDateActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout leasePanelLayout = new javax.swing.GroupLayout(leasePanel);
        leasePanel.setLayout(leasePanelLayout);
        leasePanelLayout.setHorizontalGroup(
            leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaseToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(leasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leasePanelLayout.createSequentialGroup()
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDateReceivedFrom)
                            .addComponent(lblReceiptNumber))
                        .addGap(18, 18, 18)
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(leasePanelLayout.createSequentialGroup()
                                .addComponent(lblPayDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(payDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(receiptNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(leasePanelLayout.createSequentialGroup()
                                    .addComponent(receivedFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(btnDateReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(47, 47, 47)
                                    .addComponent(lblDateReceivedTo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(receivedToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnDateReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(leasePanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblName)
                            .addComponent(lblLocation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(locationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                            .addComponent(nameLeaseTextField))))
                .addContainerGap(396, Short.MAX_VALUE))
        );
        leasePanelLayout.setVerticalGroup(
            leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(nameLeaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocation)
                    .addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReceiptNumber)
                        .addComponent(receiptNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(payDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPayDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblDateReceivedTo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(receivedToTextField)
                    .addComponent(receivedFromTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDateReceivedFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDateReceivedFrom, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDateReceivedTo, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leaseToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE))
        );

        ministerTab.addTab("Lease", leasePanel);

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

        payDateAppTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${payDate}"), payDateAppTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        receivedFromAppTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedFrom}"), receivedFromAppTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        receivedFromAppTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receivedFromAppTextFieldActionPerformed(evt);
            }
        });

        receivedToAppTextField.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        receivedToAppTextField.setText(" ");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchParamsBean, org.jdesktop.beansbinding.ELProperty.create("${dateReceivedTo}"), receivedToAppTextField, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel9.setText("Date Received (From)");

        jLabel10.setText("Date Received (To)");

        jLabel11.setText("Pay Date");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${ministerApplicationResultList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchResultListBean, eLProperty, jTableApp);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${location}"));
        columnBinding.setColumnName("Location");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${surveyFee}"));
        columnBinding.setColumnName("Survey Fee");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${payDate}"));
        columnBinding.setColumnName("Pay Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptNumber}"));
        columnBinding.setColumnName("Receipt Number");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dateReceived}"));
        columnBinding.setColumnName("Date Received");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ministerApplicationSearchResultListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedMinisterApplicationSearchResult}"), jTableApp, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(jTableApp);

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

        javax.swing.GroupLayout appPanelLayout = new javax.swing.GroupLayout(appPanel);
        appPanel.setLayout(appPanelLayout);
        appPanelLayout.setHorizontalGroup(
            appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(appPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(appPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(nameAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(appPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(locationAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, appPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(receiptAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(receivedFromAppTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(payDateAppTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appPanelLayout.createSequentialGroup()
                        .addComponent(btnAppReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(receivedToAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAppReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAppPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        appPanelLayout.setVerticalGroup(
            appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(nameAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(receivedFromAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(appPanelLayout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addComponent(btnAppReceivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAppReceivedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(receivedToAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(locationAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(receiptAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(payDateAppTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addComponent(btnAppPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE))
        );

        ministerTab.addTab("Land Application", appPanel);

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
        clearMinisterInward();
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
        clearMinisterLease();
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

    private void btnPayDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDateActionPerformed
        showCalendar(payDateTextField);
    }//GEN-LAST:event_btnPayDateActionPerformed

    private void btnDateInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateInActionPerformed
        showCalendar(dateInTextField);
    }//GEN-LAST:event_btnDateInActionPerformed

    private void btnDateOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateOutActionPerformed
        showCalendar(dateOutTextField);
    }//GEN-LAST:event_btnDateOutActionPerformed

    private void btnAppSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppSearchActionPerformed
        executeApplicationSearch(ministerApplicationSearchParamsBean, lblApplicationSearchCount, ministerApplicationSearchResultListBean);
    }//GEN-LAST:event_btnAppSearchActionPerformed

    private void btnClearAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAppActionPerformed
        clearMinisterApplication();
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
        showCalendar(payDateAppTextField);
    }//GEN-LAST:event_btnAppPayDateActionPerformed

    private void btnAppReceivedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppReceivedFromActionPerformed
        showCalendar(receivedFromAppTextField);
    }//GEN-LAST:event_btnAppReceivedFromActionPerformed

    private void btnAppReceivedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppReceivedToActionPerformed
        showCalendar(receivedToAppTextField);
    }//GEN-LAST:event_btnAppReceivedToActionPerformed

    private void receivedFromAppTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receivedFromAppTextFieldActionPerformed

    }//GEN-LAST:event_receivedFromAppTextFieldActionPerformed

    private void dateInTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateInTextFieldActionPerformed

    }//GEN-LAST:event_dateInTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel appPanel;
    private javax.swing.JToolBar appToolBar;
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
    private javax.swing.JButton btnDateIn;
    private javax.swing.JButton btnDateOut;
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
    private javax.swing.JButton btnPayDate;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveApp;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveInward;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveLease;
    private javax.swing.JFormattedTextField dateInTextField;
    private javax.swing.JFormattedTextField dateOutTextField;
    private javax.swing.JTextField fileNumberTextField;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel inwardPanel;
    private javax.swing.JToolBar inwardToolBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
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
    private javax.swing.JLabel lblInwardSearchCount;
    private javax.swing.JLabel lblLeaseSearchCount;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPayDate;
    private javax.swing.JLabel lblReceiptNumber;
    private javax.swing.JLabel lblSearchResult;
    private javax.swing.JLabel lblSearchResult1;
    private javax.swing.JLabel lblSearchResult2;
    private javax.swing.JPanel leasePanel;
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
    private javax.swing.JFormattedTextField payDateAppTextField;
    private javax.swing.JFormattedTextField payDateTextField;
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
