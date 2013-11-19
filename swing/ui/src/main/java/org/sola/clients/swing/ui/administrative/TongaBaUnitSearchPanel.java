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
package org.sola.clients.swing.ui.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import org.sola.clients.beans.administrative.BaUnitSearchParamsBean;
import org.sola.clients.beans.administrative.BaUnitSearchResultBean;
import org.sola.clients.beans.administrative.BaUnitSearchResultListBean;
import org.sola.clients.beans.referencedata.DistrictListBean;
import org.sola.clients.beans.referencedata.TownListBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.renderers.BooleanCellRenderer2;
import org.sola.clients.swing.ui.renderers.CellDelimitedListRenderer;
import org.sola.clients.swing.ui.renderers.DateTimeRenderer;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author soladev
 */
public class TongaBaUnitSearchPanel extends javax.swing.JPanel {

    public static final String SELECTED_BAUNIT_SEARCH_RESULT = "openSelectedBaUnit";

    /**
     * Creates new form TongaBaUnitSearchPanel
     */
    public TongaBaUnitSearchPanel() {
        initComponents();

        allotmentResultsList.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    btnAllotmentOpen.setEnabled(evt.getNewValue() != null);
                }
            }
        });
        leaseResultsList.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    btnLeaseOpen.setEnabled(evt.getNewValue() != null);
                }
            }
        });
        subleaseResultsList.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    btnSubleaseOpen.setEnabled(evt.getNewValue() != null);
                }
            }
        });
        estateResultsList.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    btnEstateOpen.setEnabled(evt.getNewValue() != null);
                }
            }
        });

        townResultsList.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    btnTownOpen.setEnabled(evt.getNewValue() != null);
                }
            }
        });
        customizeButtons();
    }

    private void customizeButtons() {
        btnAllotmentOpen.setEnabled(false);
        btnLeaseOpen.setEnabled(false);
        btnSubleaseOpen.setEnabled(false);
        btnEstateOpen.setEnabled(false);
        btnTownOpen.setEnabled(false);
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    private void executeSearch(final BaUnitSearchParamsBean params,
            final JLabel lblResultCount, final BaUnitSearchResultListBean results) {

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                results.search(params);
                return null;
            }

            @Override
            public void taskDone() {
                lblResultCount.setText(Integer.toString(results.getBaUnitSearchResults().size()));
                if (results.getBaUnitSearchResults().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (results.getBaUnitSearchResults().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    public void clickFind() {
        // Execute a search to be completed...
    }

    private void openBaUnit(BaUnitSearchResultBean selectedResult) {
        if (selectedResult != null) {
            firePropertyChange(SELECTED_BAUNIT_SEARCH_RESULT, null, selectedResult);
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

        allotmentResultsList = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        leaseResultsList = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        estateResultsList = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        townResultsList = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        townListBean = new TownListBean(true);
        islandListBean = new DistrictListBean(true);
        allotmentParams = new BaUnitSearchParamsBean(BaUnitSearchParamsBean.SEARCH_TYPE_ALLOTMENT);
        leaseParams = new BaUnitSearchParamsBean(BaUnitSearchParamsBean.SEARCH_TYPE_LEASE);
        estateParams = new BaUnitSearchParamsBean(BaUnitSearchParamsBean.SEARCH_TYPE_ESTATE);
        townParams = new BaUnitSearchParamsBean(BaUnitSearchParamsBean.SEARCH_TYPE_TOWN);
        subleaseParams = new BaUnitSearchParamsBean(BaUnitSearchParamsBean.SEARCH_TYPE_SUBLEASE);
        subleaseResultsList = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        mainTabPane = new javax.swing.JTabbedPane();
        tabAllotment = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtAllotmentDeedNum = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtAllotmentFolio = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtAllotmentBookNum = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtAllotmentPage = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        txtAllotmentRegDateFrom = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        btnAllotmentDateFrom = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        txtAllotmentRegDateTo = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        btnAllotmentDateTo = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAllotmentLandHolder = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtAllotmentParcelName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        cbAllotmentTown = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        cbxTown = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        cbxTax = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnAllotmentSearch = new javax.swing.JButton();
        btnAllotmentClear = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnAllotmentOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel10 = new javax.swing.JLabel();
        lblAllotmentSearchCount = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAllotment = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        tabLease = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtLeaseNumber = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cbxLeaseTown = new javax.swing.JComboBox();
        jPanel25 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtLesseeName = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtLeaseLandHolder = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        txtLeaseRegDateFrom = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        btnLeaseRegDateFrom = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        txtLeaseRegDateTo = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        btnLeaseDateTo = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        btnLeaseSearch = new javax.swing.JButton();
        btnLeaseClear = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnLeaseOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        lblLeaseResultsCount = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLease = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        tabSublease = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtLeaseNumber1 = new javax.swing.JTextField();
        jPanel52 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        cbxLeaseTown1 = new javax.swing.JComboBox();
        jPanel48 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtSubleaseLandHolder = new javax.swing.JTextField();
        jPanel49 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        txtSublesseeName = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        txtSubleaseRegDateFrom = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        btnSubleaseRegDateFrom = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        txtSubleaseRegDateTo = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        btnSubleaseDateTo = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableSublease = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar6 = new javax.swing.JToolBar();
        btnSubleaseSearch = new javax.swing.JButton();
        btnSubleaseClear = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        btnSubleaseOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jLabel26 = new javax.swing.JLabel();
        lblSubleaseResultsCount = new javax.swing.JLabel();
        tabTownIsland = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtTownName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTown = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar1 = new javax.swing.JToolBar();
        btnTownSearch = new javax.swing.JButton();
        btnTownClear = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnTownOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel25 = new javax.swing.JLabel();
        lblTownResultsCount = new javax.swing.JLabel();
        tabEstate = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtNobleName = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtEstateName = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel37 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        btnEstateSearch = new javax.swing.JButton();
        btnEstateClear = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btnEstateOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel18 = new javax.swing.JLabel();
        lblEstateResultCount = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableEstate = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        jPanel1.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel9.setLayout(new java.awt.GridLayout(3, 2, 12, 0));

        jLabel6.setText("Deed #:");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), txtAllotmentDeedNum, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addComponent(txtAllotmentDeedNum)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentDeedNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.add(jPanel12);

        jLabel7.setText("Folio:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${nameLastPart}"), txtAllotmentFolio, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAllotmentFolio)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.add(jPanel13);

        jLabel8.setText("Registry Book:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${registryBook}"), txtAllotmentBookNum, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addComponent(txtAllotmentBookNum)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentBookNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        jPanel9.add(jPanel15);

        jLabel9.setText("Registry Page/Ref.:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${registryPageRef}"), txtAllotmentPage, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAllotmentPage)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        jPanel9.add(jPanel14);

        txtAllotmentRegDateFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateFrom}"), txtAllotmentRegDateFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel4.setText("Registered Date From:");

        btnAllotmentDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/source/Bundle"); // NOI18N
        btnAllotmentDateFrom.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnAllotmentDateFrom.setBorder(null);
        btnAllotmentDateFrom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAllotmentDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllotmentDateFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(txtAllotmentRegDateFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAllotmentDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAllotmentRegDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAllotmentDateFrom))
                .addContainerGap())
        );

        jPanel9.add(jPanel10);

        txtAllotmentRegDateTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateTo}"), txtAllotmentRegDateTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel5.setText("Registered Date To:");

        btnAllotmentDateTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnAllotmentDateTo.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnAllotmentDateTo.setBorder(null);
        btnAllotmentDateTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAllotmentDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllotmentDateToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(txtAllotmentRegDateTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAllotmentDateTo)
                .addGap(1, 1, 1))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAllotmentRegDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAllotmentDateTo))
                .addContainerGap())
        );

        jPanel9.add(jPanel11);

        jPanel1.add(jPanel9);

        jPanel5.setLayout(new java.awt.GridLayout(3, 1, 12, 0));

        jLabel1.setText("Landholder / Rightholder:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtAllotmentLandHolder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAllotmentLandHolder)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentLandHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel16);

        jLabel2.setText("Parcel Name:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${parcelName}"), txtAllotmentParcelName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
            .addComponent(txtAllotmentParcelName)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentParcelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel17);

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredTownList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townListBean, eLProperty, cbAllotmentTown);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${town}"), cbAllotmentTown, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel3.setText("Town:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbAllotmentTown, 0, 128, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbAllotmentTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.add(jPanel7);

        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        cbxTown.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${townAllotment}"), cbxTown, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Town Allot.");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxTown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxTown)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel44);

        cbxTax.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentParams, org.jdesktop.beansbinding.ELProperty.create("${taxAllotment}"), cbxTax, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Tax Allot.");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxTax, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxTax)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel45);

        jPanel6.add(jPanel8);

        jPanel5.add(jPanel6);

        jPanel1.add(jPanel5);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnAllotmentSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnAllotmentSearch.setText("Search");
        btnAllotmentSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllotmentSearchActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAllotmentSearch);

        btnAllotmentClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnAllotmentClear.setText("Clear");
        btnAllotmentClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllotmentClearActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAllotmentClear);
        jToolBar2.add(jSeparator5);

        btnAllotmentOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAllotmentOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllotmentOpenActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAllotmentOpen);
        jToolBar2.add(jSeparator1);

        jLabel10.setText("Search results: ");
        jToolBar2.add(jLabel10);

        lblAllotmentSearchCount.setText(" ");
        jToolBar2.add(lblAllotmentSearchCount);

        tableAllotment.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentResultsList, eLProperty, tableAllotment);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Deed");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registryBookRef}"));
        columnBinding.setColumnName("Book Ref.");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rrrType.displayValue}"));
        columnBinding.setColumnName("Right Type");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholders}"));
        columnBinding.setColumnName("Landholder / Rightholder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${parcelName}"));
        columnBinding.setColumnName("Parcel Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registeredDate}"));
        columnBinding.setColumnName("Registration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${townName}"));
        columnBinding.setColumnName("Town");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${townAllotment}"));
        columnBinding.setColumnName("Town Allot.");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${taxAllotment}"));
        columnBinding.setColumnName("Tax Allot.");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationStatus.displayValue}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, allotmentResultsList, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableAllotment, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableAllotment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAllotmentMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableAllotment);
        tableAllotment.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableAllotment.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableAllotment.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableAllotment.getColumnModel().getColumn(3).setCellRenderer(new org.sola.clients.swing.ui.renderers.CellDelimitedListRenderer(",", false));
        tableAllotment.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableAllotment.getColumnModel().getColumn(5).setCellRenderer(new DateTimeRenderer());
        tableAllotment.getColumnModel().getColumn(7).setMaxWidth(70);
        tableAllotment.getColumnModel().getColumn(7).setCellRenderer(new BooleanCellRenderer2());
        tableAllotment.getColumnModel().getColumn(8).setMaxWidth(70);
        tableAllotment.getColumnModel().getColumn(8).setCellRenderer(new BooleanCellRenderer2());
        tableAllotment.getColumnModel().getColumn(9).setPreferredWidth(80);

        javax.swing.GroupLayout tabAllotmentLayout = new javax.swing.GroupLayout(tabAllotment);
        tabAllotment.setLayout(tabAllotmentLayout);
        tabAllotmentLayout.setHorizontalGroup(
            tabAllotmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAllotmentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabAllotmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
                .addContainerGap())
        );
        tabAllotmentLayout.setVerticalGroup(
            tabAllotmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAllotmentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabPane.addTab("Allotment", tabAllotment);

        jPanel28.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel19.setLayout(new java.awt.GridLayout(2, 1));

        jPanel21.setLayout(new java.awt.GridLayout(1, 0, 12, 0));

        jLabel13.setText("Lease #:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), txtLeaseNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addComponent(txtLeaseNumber)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel21.add(jPanel23);

        jLabel14.setText("Town:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredTownList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townListBean, eLProperty, cbxLeaseTown);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${town}"), cbxLeaseTown, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxLeaseTown, 0, 122, Short.MAX_VALUE)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLeaseTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel24);

        jPanel19.add(jPanel21);

        jLabel15.setText("Lessee / Rightholder:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtLesseeName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
            .addComponent(txtLesseeName)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLesseeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel19.add(jPanel25);

        jPanel28.add(jPanel19);

        jPanel20.setLayout(new java.awt.GridLayout(2, 1, 12, 0));

        jLabel12.setText("Landholder:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${otherRightholder}"), txtLeaseLandHolder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtLeaseLandHolder)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseLandHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel20.add(jPanel22);

        jPanel26.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        txtLeaseRegDateFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateFrom}"), txtLeaseRegDateFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel16.setText("Registed Date From:");

        btnLeaseRegDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnLeaseRegDateFrom.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnLeaseRegDateFrom.setBorder(null);
        btnLeaseRegDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseRegDateFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(txtLeaseRegDateFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLeaseRegDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLeaseRegDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLeaseRegDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel26.add(jPanel29);

        txtLeaseRegDateTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateTo}"), txtLeaseRegDateTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel17.setText("Registered Date To:");

        btnLeaseDateTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnLeaseDateTo.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnLeaseDateTo.setBorder(null);
        btnLeaseDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseDateToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(txtLeaseRegDateTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLeaseDateTo)
                .addGap(1, 1, 1))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLeaseRegDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLeaseDateTo))
                .addContainerGap())
        );

        jPanel26.add(jPanel27);

        jPanel20.add(jPanel26);

        jPanel28.add(jPanel20);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnLeaseSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnLeaseSearch.setText("Search");
        btnLeaseSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseSearchActionPerformed(evt);
            }
        });
        jToolBar3.add(btnLeaseSearch);

        btnLeaseClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnLeaseClear.setText("Clear");
        btnLeaseClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseClearActionPerformed(evt);
            }
        });
        jToolBar3.add(btnLeaseClear);
        jToolBar3.add(jSeparator6);

        btnLeaseOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLeaseOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaseOpenActionPerformed(evt);
            }
        });
        jToolBar3.add(btnLeaseOpen);
        jToolBar3.add(jSeparator2);

        jLabel11.setText("Search results: ");
        jToolBar3.add(jLabel11);

        lblLeaseResultsCount.setText(" ");
        jToolBar3.add(lblLeaseResultsCount);

        tableLease.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseResultsList, eLProperty, tableLease);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Lease #");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rrrType.displayValue}"));
        columnBinding.setColumnName("Right Type");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registryBookRef}"));
        columnBinding.setColumnName("Book Ref.");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholders}"));
        columnBinding.setColumnName("Lessee / Rightholder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${otherRightholders}"));
        columnBinding.setColumnName("Landholder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registeredDate}"));
        columnBinding.setColumnName("Registration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${townName}"));
        columnBinding.setColumnName("Town");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationStatus.displayValue}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, leaseResultsList, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableLease, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableLease.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLeaseMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableLease);
        tableLease.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableLease.getColumnModel().getColumn(3).setCellRenderer(new CellDelimitedListRenderer(",", false));
        tableLease.getColumnModel().getColumn(4).setPreferredWidth(200);
        tableLease.getColumnModel().getColumn(4).setCellRenderer(new CellDelimitedListRenderer(",", false));
        tableLease.getColumnModel().getColumn(5).setCellRenderer(new DateTimeRenderer());
        tableLease.getColumnModel().getColumn(7).setPreferredWidth(80);

        javax.swing.GroupLayout tabLeaseLayout = new javax.swing.GroupLayout(tabLease);
        tabLease.setLayout(tabLeaseLayout);
        tabLeaseLayout.setHorizontalGroup(
            tabLeaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabLeaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabLeaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(tabLeaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabLeaseLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        tabLeaseLayout.setVerticalGroup(
            tabLeaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabLeaseLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(tabLeaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabLeaseLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(253, Short.MAX_VALUE)))
        );

        mainTabPane.addTab("Lease", tabLease);

        jPanel2.setLayout(new java.awt.GridLayout(2, 2, 12, 0));

        jPanel50.setLayout(new java.awt.GridLayout(1, 0, 12, 0));

        jLabel32.setText("Sublease #:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${nameLastPart}"), txtLeaseNumber1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addComponent(txtLeaseNumber1)
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel50.add(jPanel51);

        jLabel33.setText("Town:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredTownList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townListBean, eLProperty, cbxLeaseTown1);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${town}"), cbxLeaseTown1, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxLeaseTown1, 0, 122, Short.MAX_VALUE)
            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLeaseTown1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel50.add(jPanel52);

        jPanel2.add(jPanel50);

        jLabel30.setText("Lessee / Landholder:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${otherRightholder}"), txtSubleaseLandHolder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSubleaseLandHolder)
            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSubleaseLandHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel48);

        jLabel31.setText("Sublessee / Rightholder:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtSublesseeName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
            .addComponent(txtSublesseeName)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSublesseeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel49);

        jPanel30.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        txtSubleaseRegDateFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateFrom}"), txtSubleaseRegDateFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel27.setText("Registed Date From:");

        btnSubleaseRegDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubleaseRegDateFrom.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnSubleaseRegDateFrom.setBorder(null);
        btnSubleaseRegDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubleaseRegDateFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(txtSubleaseRegDateFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubleaseRegDateFrom))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubleaseRegDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubleaseRegDateFrom))
                .addContainerGap())
        );

        jPanel30.add(jPanel38);

        txtSubleaseRegDateTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseParams, org.jdesktop.beansbinding.ELProperty.create("${registeredDateTo}"), txtSubleaseRegDateTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel28.setText("Registered Date To:");

        btnSubleaseDateTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubleaseDateTo.setText(bundle.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnSubleaseDateTo.setBorder(null);
        btnSubleaseDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubleaseDateToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addComponent(txtSubleaseRegDateTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubleaseDateTo)
                .addGap(1, 1, 1))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubleaseRegDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubleaseDateTo))
                .addContainerGap())
        );

        jPanel30.add(jPanel46);

        jPanel2.add(jPanel30);

        tableSublease.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseResultsList, eLProperty, tableSublease);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Sublease #");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rrrType.displayValue}"));
        columnBinding.setColumnName("Right Type");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registryBookRef}"));
        columnBinding.setColumnName("Book Ref.");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholders}"));
        columnBinding.setColumnName("Sublessee / Rightholder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${otherRightholders}"));
        columnBinding.setColumnName("Lessee / Landholder");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registeredDate}"));
        columnBinding.setColumnName("Registered Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${townName}"));
        columnBinding.setColumnName("Town");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationStatus.displayValue}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, subleaseResultsList, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableSublease, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableSublease.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSubleaseMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableSublease);
        tableSublease.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableSublease.getColumnModel().getColumn(3).setCellRenderer(new CellDelimitedListRenderer(",", false));
        tableSublease.getColumnModel().getColumn(4).setPreferredWidth(200);
        tableSublease.getColumnModel().getColumn(4).setCellRenderer(new CellDelimitedListRenderer(",", false));
        tableSublease.getColumnModel().getColumn(5).setCellRenderer(new DateTimeRenderer());
        tableSublease.getColumnModel().getColumn(7).setPreferredWidth(80);

        jToolBar6.setFloatable(false);
        jToolBar6.setRollover(true);

        btnSubleaseSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSubleaseSearch.setText("Search");
        btnSubleaseSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubleaseSearchActionPerformed(evt);
            }
        });
        jToolBar6.add(btnSubleaseSearch);

        btnSubleaseClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnSubleaseClear.setText("Clear");
        btnSubleaseClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubleaseClearActionPerformed(evt);
            }
        });
        jToolBar6.add(btnSubleaseClear);
        jToolBar6.add(jSeparator9);

        btnSubleaseOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSubleaseOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubleaseOpenActionPerformed(evt);
            }
        });
        jToolBar6.add(btnSubleaseOpen);
        jToolBar6.add(jSeparator10);

        jLabel26.setText("Search results: ");
        jToolBar6.add(jLabel26);

        lblSubleaseResultsCount.setText(" ");
        jToolBar6.add(lblSubleaseResultsCount);

        javax.swing.GroupLayout tabSubleaseLayout = new javax.swing.GroupLayout(tabSublease);
        tabSublease.setLayout(tabSubleaseLayout);
        tabSubleaseLayout.setHorizontalGroup(
            tabSubleaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSubleaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabSubleaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        tabSubleaseLayout.setVerticalGroup(
            tabSubleaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSubleaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabPane.addTab("Sublease", tabSublease);

        jLabel22.setText("Town / Island Name:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townParams, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), txtTownName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTownName, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTownName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        tableTown.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townResultsList, eLProperty, tableTown);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${islandName}"));
        columnBinding.setColumnName("Island");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townResultsList, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableTown, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableTown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTownMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableTown);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnTownSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnTownSearch.setText("Search");
        btnTownSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTownSearchActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTownSearch);

        btnTownClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnTownClear.setText("Clear");
        btnTownClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTownClearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTownClear);
        jToolBar1.add(jSeparator8);

        btnTownOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTownOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTownOpenActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTownOpen);
        jToolBar1.add(jSeparator4);

        jLabel25.setText("Search results: ");
        jToolBar1.add(jLabel25);

        lblTownResultsCount.setText(" ");
        jToolBar1.add(lblTownResultsCount);

        javax.swing.GroupLayout tabTownIslandLayout = new javax.swing.GroupLayout(tabTownIsland);
        tabTownIsland.setLayout(tabTownIslandLayout);
        tabTownIslandLayout.setHorizontalGroup(
            tabTownIslandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabTownIslandLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabTownIslandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        tabTownIslandLayout.setVerticalGroup(
            tabTownIslandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabTownIslandLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabPane.addTab("Town / Island", tabTownIsland);

        jPanel31.setLayout(new java.awt.GridLayout(2, 2, 12, 0));

        jLabel20.setText("Noble:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateParams, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtNobleName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
            .addComponent(txtNobleName)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNobleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel31.add(jPanel33);

        jLabel19.setText("Estate Name:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateParams, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), txtEstateName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtEstateName)
            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEstateName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel31.add(jPanel32);

        jPanel34.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jLabel21.setText("Island");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredDistrictList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, islandListBean, eLProperty, jComboBox1);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateParams, org.jdesktop.beansbinding.ELProperty.create("${island}"), jComboBox1, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, 0, 122, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel34.add(jPanel36);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        jPanel34.add(jPanel37);

        jPanel31.add(jPanel34);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        jPanel31.add(jPanel35);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnEstateSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnEstateSearch.setText("Search");
        btnEstateSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstateSearchActionPerformed(evt);
            }
        });
        jToolBar4.add(btnEstateSearch);

        btnEstateClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnEstateClear.setText("Clear");
        btnEstateClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstateClearActionPerformed(evt);
            }
        });
        jToolBar4.add(btnEstateClear);
        jToolBar4.add(jSeparator7);

        btnEstateOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEstateOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstateOpenActionPerformed(evt);
            }
        });
        jToolBar4.add(btnEstateOpen);
        jToolBar4.add(jSeparator3);

        jLabel18.setText("Search results: ");
        jToolBar4.add(jLabel18);

        lblEstateResultCount.setText(" ");
        jToolBar4.add(lblEstateResultCount);

        tableEstate.getTableHeader().setReorderingAllowed(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateResultsList, eLProperty, tableEstate);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholders}"));
        columnBinding.setColumnName("Noble");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"));
        columnBinding.setColumnName("Estate");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${islandName}"));
        columnBinding.setColumnName("Island");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateResultsList, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableEstate, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableEstate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEstateMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tableEstate);
        tableEstate.getColumnModel().getColumn(0).setCellRenderer(new org.sola.clients.swing.ui.renderers.CellDelimitedListRenderer(";", false));

        javax.swing.GroupLayout tabEstateLayout = new javax.swing.GroupLayout(tabEstate);
        tabEstate.setLayout(tabEstateLayout);
        tabEstateLayout.setHorizontalGroup(
            tabEstateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEstateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabEstateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(tabEstateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabEstateLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        tabEstateLayout.setVerticalGroup(
            tabEstateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEstateLayout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(tabEstateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabEstateLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(253, Short.MAX_VALUE)))
        );

        mainTabPane.addTab("Estate", tabEstate);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabPane)
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAllotmentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllotmentClearActionPerformed
        allotmentParams.clear();
    }//GEN-LAST:event_btnAllotmentClearActionPerformed

    private void btnAllotmentDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllotmentDateFromActionPerformed
        showCalendar(txtAllotmentRegDateFrom);
    }//GEN-LAST:event_btnAllotmentDateFromActionPerformed

    private void btnAllotmentDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllotmentDateToActionPerformed
        showCalendar(txtAllotmentRegDateTo);
    }//GEN-LAST:event_btnAllotmentDateToActionPerformed

    private void btnLeaseClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseClearActionPerformed
        leaseParams.clear();
    }//GEN-LAST:event_btnLeaseClearActionPerformed

    private void btnLeaseRegDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseRegDateFromActionPerformed
        showCalendar(txtLeaseRegDateFrom);
    }//GEN-LAST:event_btnLeaseRegDateFromActionPerformed

    private void btnLeaseDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseDateToActionPerformed
        showCalendar(txtLeaseRegDateTo);
    }//GEN-LAST:event_btnLeaseDateToActionPerformed

    private void btnEstateSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstateSearchActionPerformed
        executeSearch(estateParams, lblEstateResultCount, estateResultsList);
    }//GEN-LAST:event_btnEstateSearchActionPerformed

    private void btnEstateClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstateClearActionPerformed
        estateParams.clear();
    }//GEN-LAST:event_btnEstateClearActionPerformed

    private void btnTownClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTownClearActionPerformed
        townParams.clear();
    }//GEN-LAST:event_btnTownClearActionPerformed

    private void btnTownSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTownSearchActionPerformed
        executeSearch(townParams, lblTownResultsCount, townResultsList);
    }//GEN-LAST:event_btnTownSearchActionPerformed

    private void btnLeaseSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseSearchActionPerformed
        executeSearch(leaseParams, lblLeaseResultsCount, leaseResultsList);
    }//GEN-LAST:event_btnLeaseSearchActionPerformed

    private void btnAllotmentSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllotmentSearchActionPerformed
        executeSearch(allotmentParams, lblAllotmentSearchCount, allotmentResultsList);
    }//GEN-LAST:event_btnAllotmentSearchActionPerformed

    private void btnAllotmentOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllotmentOpenActionPerformed
        openBaUnit(allotmentResultsList.getSelectedBaUnitSearchResult());
    }//GEN-LAST:event_btnAllotmentOpenActionPerformed

    private void btnLeaseOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaseOpenActionPerformed
        openBaUnit(leaseResultsList.getSelectedBaUnitSearchResult());
    }//GEN-LAST:event_btnLeaseOpenActionPerformed

    private void btnEstateOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstateOpenActionPerformed
        openBaUnit(estateResultsList.getSelectedBaUnitSearchResult());
    }//GEN-LAST:event_btnEstateOpenActionPerformed

    private void btnTownOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTownOpenActionPerformed
        openBaUnit(townResultsList.getSelectedBaUnitSearchResult());
    }//GEN-LAST:event_btnTownOpenActionPerformed

    private void btnSubleaseSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubleaseSearchActionPerformed
        executeSearch(subleaseParams, lblSubleaseResultsCount, subleaseResultsList);
    }//GEN-LAST:event_btnSubleaseSearchActionPerformed

    private void btnSubleaseClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubleaseClearActionPerformed
        subleaseParams.clear();
    }//GEN-LAST:event_btnSubleaseClearActionPerformed

    private void btnSubleaseOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubleaseOpenActionPerformed
        openBaUnit(subleaseResultsList.getSelectedBaUnitSearchResult());
    }//GEN-LAST:event_btnSubleaseOpenActionPerformed

    private void btnSubleaseRegDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubleaseRegDateFromActionPerformed
        showCalendar(txtSubleaseRegDateFrom);
    }//GEN-LAST:event_btnSubleaseRegDateFromActionPerformed

    private void btnSubleaseDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubleaseDateToActionPerformed
        showCalendar(txtSubleaseRegDateTo);
    }//GEN-LAST:event_btnSubleaseDateToActionPerformed

    private void tableAllotmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAllotmentMouseClicked
        if (evt.getClickCount() == 2) {
            openBaUnit(allotmentResultsList.getSelectedBaUnitSearchResult());
        }
    }//GEN-LAST:event_tableAllotmentMouseClicked

    private void tableLeaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLeaseMouseClicked
        if (evt.getClickCount() == 2) {
            openBaUnit(leaseResultsList.getSelectedBaUnitSearchResult());
        }
    }//GEN-LAST:event_tableLeaseMouseClicked

    private void tableSubleaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSubleaseMouseClicked
        if (evt.getClickCount() == 2) {
            openBaUnit(subleaseResultsList.getSelectedBaUnitSearchResult());
        }
    }//GEN-LAST:event_tableSubleaseMouseClicked

    private void tableTownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTownMouseClicked
        if (evt.getClickCount() == 2) {
            openBaUnit(townResultsList.getSelectedBaUnitSearchResult());
        }
    }//GEN-LAST:event_tableTownMouseClicked

    private void tableEstateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEstateMouseClicked
        if (evt.getClickCount() == 2) {
            openBaUnit(estateResultsList.getSelectedBaUnitSearchResult());
        }
    }//GEN-LAST:event_tableEstateMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean allotmentParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean allotmentResultsList;
    private javax.swing.JButton btnAllotmentClear;
    private javax.swing.JButton btnAllotmentDateFrom;
    private javax.swing.JButton btnAllotmentDateTo;
    private org.sola.clients.swing.common.buttons.BtnOpen btnAllotmentOpen;
    private javax.swing.JButton btnAllotmentSearch;
    private javax.swing.JButton btnEstateClear;
    private org.sola.clients.swing.common.buttons.BtnOpen btnEstateOpen;
    private javax.swing.JButton btnEstateSearch;
    private javax.swing.JButton btnLeaseClear;
    private javax.swing.JButton btnLeaseDateTo;
    private org.sola.clients.swing.common.buttons.BtnOpen btnLeaseOpen;
    private javax.swing.JButton btnLeaseRegDateFrom;
    private javax.swing.JButton btnLeaseSearch;
    private javax.swing.JButton btnSubleaseClear;
    private javax.swing.JButton btnSubleaseDateTo;
    private org.sola.clients.swing.common.buttons.BtnOpen btnSubleaseOpen;
    private javax.swing.JButton btnSubleaseRegDateFrom;
    private javax.swing.JButton btnSubleaseSearch;
    private javax.swing.JButton btnTownClear;
    private org.sola.clients.swing.common.buttons.BtnOpen btnTownOpen;
    private javax.swing.JButton btnTownSearch;
    private javax.swing.JComboBox cbAllotmentTown;
    private javax.swing.JComboBox cbxLeaseTown;
    private javax.swing.JComboBox cbxLeaseTown1;
    private javax.swing.JCheckBox cbxTax;
    private javax.swing.JCheckBox cbxTown;
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean estateParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean estateResultsList;
    private org.sola.clients.beans.referencedata.DistrictListBean islandListBean;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JLabel lblAllotmentSearchCount;
    private javax.swing.JLabel lblEstateResultCount;
    private javax.swing.JLabel lblLeaseResultsCount;
    private javax.swing.JLabel lblSubleaseResultsCount;
    private javax.swing.JLabel lblTownResultsCount;
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean leaseParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean leaseResultsList;
    private javax.swing.JTabbedPane mainTabPane;
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean subleaseParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean subleaseResultsList;
    private javax.swing.JPanel tabAllotment;
    private javax.swing.JPanel tabEstate;
    private javax.swing.JPanel tabLease;
    private javax.swing.JPanel tabSublease;
    private javax.swing.JPanel tabTownIsland;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableAllotment;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableEstate;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableLease;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableSublease;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableTown;
    private org.sola.clients.beans.referencedata.TownListBean townListBean;
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean townParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean townResultsList;
    private javax.swing.JTextField txtAllotmentBookNum;
    private javax.swing.JTextField txtAllotmentDeedNum;
    private javax.swing.JTextField txtAllotmentFolio;
    private javax.swing.JTextField txtAllotmentLandHolder;
    private javax.swing.JTextField txtAllotmentPage;
    private javax.swing.JTextField txtAllotmentParcelName;
    private javax.swing.JFormattedTextField txtAllotmentRegDateFrom;
    private javax.swing.JFormattedTextField txtAllotmentRegDateTo;
    private javax.swing.JTextField txtEstateName;
    private javax.swing.JTextField txtLeaseLandHolder;
    private javax.swing.JTextField txtLeaseNumber;
    private javax.swing.JTextField txtLeaseNumber1;
    private javax.swing.JFormattedTextField txtLeaseRegDateFrom;
    private javax.swing.JFormattedTextField txtLeaseRegDateTo;
    private javax.swing.JTextField txtLesseeName;
    private javax.swing.JTextField txtNobleName;
    private javax.swing.JTextField txtSubleaseLandHolder;
    private javax.swing.JFormattedTextField txtSubleaseRegDateFrom;
    private javax.swing.JFormattedTextField txtSubleaseRegDateTo;
    private javax.swing.JTextField txtSublesseeName;
    private javax.swing.JTextField txtTownName;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
