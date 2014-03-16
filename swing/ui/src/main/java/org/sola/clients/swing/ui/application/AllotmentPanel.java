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
package org.sola.clients.swing.ui.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.referencedata.DistrictListBean;
import org.sola.clients.beans.referencedata.EstateListBean;
import org.sola.clients.beans.referencedata.LandUseTypeListBean;
import org.sola.clients.beans.referencedata.TownListBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.utils.FormattersFactory;

/**
 *
 * @author soladev
 */
public class AllotmentPanel extends javax.swing.JPanel {
    
    boolean readOnly = true; 

    /**
     * Creates new form AllotmentPanel
     */
    public AllotmentPanel() {
        this(null, true);
    }

    public AllotmentPanel(ApplicationPropertyBean appPropBean, boolean readOnly) {
        this.appProperty = appPropBean;
        this.readOnly = readOnly;
        initComponents();
        postInit();
    }

    /**
     * Used to create an empty appPropety object if the appProperty is not set
     * by the panel constructor.
     */
    private ApplicationPropertyBean createAppProperty() {
        if (appProperty == null) {
            appProperty = new ApplicationPropertyBean();
            appProperty.setTownAllotment(true);
        }
        return appProperty;
    }

    private void postInit() {
        customiseForm(); 
        // Property listener to update the lists of estates and towns when the user
        // changes the island/district. 
        appProperty.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationPropertyBean.ISLAND_PROPERTY)) {
                    applyIslandFilter();
                }
                if (evt.getPropertyName().equals(ApplicationPropertyBean.IS_VERIFIED_EXISTS_PROPERTY)) {
                    customiseForm();
                }
            }
        });
        
        applyIslandFilter();
    }
    
    public void customiseForm() {
        boolean enabled = !readOnly; 
        boolean enableIfNotExists = enabled && !appProperty.isVerifiedExists(); 
        txtDeedNum.setEnabled(enableIfNotExists);
        txtFolio.setEnabled(enableIfNotExists);
        txtAllotmentHolder.setEnabled(enableIfNotExists);
        txtDateOfRegistration.setEnabled(enableIfNotExists);
        btnDateOfRegistration.setEnabled(enableIfNotExists);
        txtArea.setEnabled(enableIfNotExists);
        txtAreaImperial.setEnabled(enableIfNotExists);
        cbxPurpose.setEnabled(enabled);
        txtParcelName.setEnabled(enableIfNotExists);
        cbxDistrict.setEnabled(enabled);
        cbxEstate.setEnabled(enabled);
        cbxTown.setEnabled(enabled);
        radTownAllot.setEnabled(enableIfNotExists);
        radTaxAllot.setEnabled(enableIfNotExists);
        txtDescription.setEnabled(enabled);
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    /**
     * Filters the list of Towns and Estates by Island (a.k.a District) Tonga
     * Customization.
     */
    private void applyIslandFilter() {
        String islandId = appProperty.getIslandId();
        estateListBean1.setIslandFilter(islandId);
        townListBean1.setIslandFilter(islandId);
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

        appProperty = createAppProperty();
        landUseTypeListBean1 = new LandUseTypeListBean(true);
        districtListBean1 = new DistrictListBean(true);
        estateListBean1 = new EstateListBean(true);
        townListBean1 = new TownListBean(true);
        allotmentTypeGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtDeedNum = new javax.swing.JTextField();
        lblDeedNum = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblFolio = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblAllotmentHolder = new javax.swing.JLabel();
        txtAllotmentHolder = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lblDateOfRegistration = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtDateOfRegistration = new org.sola.clients.swing.common.controls.WatermarkDate();
        btnDateOfRegistration = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        lblArea = new javax.swing.JLabel();
        txtArea = new org.sola.clients.swing.common.controls.WatermarkFormattedTextField();
        jPanel12 = new javax.swing.JPanel();
        lblAreaImperial = new javax.swing.JLabel();
        txtAreaImperial = new org.sola.clients.swing.common.controls.WatermarkFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        lblPurpose = new javax.swing.JLabel();
        cbxPurpose = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        lblParcelName = new javax.swing.JLabel();
        txtParcelName = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lblDistrict = new javax.swing.JLabel();
        cbxDistrict = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        lblTown = new javax.swing.JLabel();
        cbxTown = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        lblEstate = new javax.swing.JLabel();
        cbxEstate = new javax.swing.JComboBox();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        lblTownAllot = new javax.swing.JLabel();
        radTownAllot = new javax.swing.JRadioButton();
        jPanel17 = new javax.swing.JPanel();
        lblTaxAllot = new javax.swing.JLabel();
        radTaxAllot = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();

        jPanel1.setLayout(new java.awt.GridLayout(3, 4, 15, 0));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${nameFirstpart}"), txtDeedNum, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/application/Bundle"); // NOI18N
        lblDeedNum.setText(bundle.getString("AllotmentPanel.lblDeedNum.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDeedNum)
            .addComponent(lblDeedNum, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(lblDeedNum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDeedNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        lblFolio.setText(bundle.getString("AllotmentPanel.lblFolio.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${nameLastpart}"), txtFolio, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFolio, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(txtFolio)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(lblFolio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

        lblAllotmentHolder.setText(bundle.getString("AllotmentPanel.lblAllotmentHolder.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${lessorName}"), txtAllotmentHolder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAllotmentHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(txtAllotmentHolder)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(lblAllotmentHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAllotmentHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        lblDateOfRegistration.setText(bundle.getString("AllotmentPanel.lblDateOfRegistration.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"), txtDateOfRegistration, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnDateOfRegistration.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/source/Bundle"); // NOI18N
        btnDateOfRegistration.setText(bundle1.getString("DocumentSearchPanel.btnDateFrom.text")); // NOI18N
        btnDateOfRegistration.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDateOfRegistration.setBorderPainted(false);
        btnDateOfRegistration.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateOfRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateOfRegistrationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(txtDateOfRegistration, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDateOfRegistration, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDateOfRegistration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDateOfRegistration)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDateOfRegistration, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(lblDateOfRegistration)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5);

        lblArea.setText(bundle.getString("AllotmentPanel.lblArea.text")); // NOI18N

        txtArea.setFormatterFactory(FormattersFactory.getInstance().getMetricAreaFormatterFactory());
        txtArea.setWatermarkText(bundle.getString("AllotmentPanel.txtArea.watermarkText")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${area}"), txtArea, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblArea, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(txtArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(lblArea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11);

        lblAreaImperial.setText(bundle.getString("AllotmentPanel.lblAreaImperial.text")); // NOI18N

        txtAreaImperial.setText(bundle.getString("AllotmentPanel.txtAreaImperial.text")); // NOI18N
        txtAreaImperial.setToolTipText(bundle.getString("AllotmentPanel.txtAreaImperial.toolTipText")); // NOI18N
        txtAreaImperial.setFormatterFactory(FormattersFactory.getInstance().getImperialFormatterFactory());
        txtAreaImperial.setWatermarkText(bundle.getString("AllotmentPanel.txtAreaImperial.watermarkText")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${area}"), txtAreaImperial, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAreaImperial, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(txtAreaImperial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(lblAreaImperial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAreaImperial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel12);

        lblPurpose.setText(bundle.getString("AllotmentPanel.lblPurpose.text")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${landUseTypeList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, landUseTypeListBean1, eLProperty, cbxPurpose);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${landUseType}"), cbxPurpose, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPurpose, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(cbxPurpose, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(lblPurpose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxPurpose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7);

        lblParcelName.setText(bundle.getString("AllotmentPanel.lblParcelName.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${registeredName}"), txtParcelName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblParcelName, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(txtParcelName)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(lblParcelName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParcelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel14);

        lblDistrict.setText(bundle.getString("AllotmentPanel.lblDistrict.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredDistrictList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, districtListBean1, eLProperty, cbxDistrict);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${island}"), cbxDistrict, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDistrict, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(cbxDistrict, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblDistrict)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8);

        lblTown.setText(bundle.getString("AllotmentPanel.lblTown.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredTownList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, townListBean1, eLProperty, cbxTown);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${town}"), cbxTown, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cbxTown, 0, 118, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(lblTown)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9);

        lblEstate.setText(bundle.getString("AllotmentPanel.lblEstate.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredEstateList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, estateListBean1, eLProperty, cbxEstate);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${nobleEstate}"), cbxEstate, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEstate, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(cbxEstate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(lblEstate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxEstate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel10);

        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        lblTownAllot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTownAllot.setText(bundle.getString("AllotmentPanel.lblTownAllot.text")); // NOI18N

        allotmentTypeGroup.add(radTownAllot);
        radTownAllot.setText(bundle.getString("AllotmentPanel.radTownAllot.text")); // NOI18N
        radTownAllot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${townAllotment}"), radTownAllot, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTownAllot, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(radTownAllot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(lblTownAllot)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radTownAllot)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel16);

        lblTaxAllot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaxAllot.setText(bundle.getString("AllotmentPanel.lblTaxAllot.text")); // NOI18N
        lblTaxAllot.setInheritsPopupMenu(false);

        allotmentTypeGroup.add(radTaxAllot);
        radTaxAllot.setText(bundle.getString("AllotmentPanel.radTaxAllot.text")); // NOI18N
        radTaxAllot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${taxAllotment}"), radTaxAllot, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTaxAllot, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(radTaxAllot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(lblTaxAllot)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radTaxAllot)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel17);

        jPanel1.add(jPanel15);

        lblDescription.setText(bundle.getString("AllotmentPanel.lblDescription.text")); // NOI18N

        txtDescription.setColumns(20);
        txtDescription.setRows(5);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appProperty, org.jdesktop.beansbinding.ELProperty.create("${description}"), txtDescription, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(txtDescription);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(lblDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDateOfRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateOfRegistrationActionPerformed
        showCalendar(txtDateOfRegistration);
    }//GEN-LAST:event_btnDateOfRegistrationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup allotmentTypeGroup;
    private org.sola.clients.beans.application.ApplicationPropertyBean appProperty;
    private javax.swing.JButton btnDateOfRegistration;
    private javax.swing.JComboBox cbxDistrict;
    private javax.swing.JComboBox cbxEstate;
    private javax.swing.JComboBox cbxPurpose;
    private javax.swing.JComboBox cbxTown;
    private org.sola.clients.beans.referencedata.DistrictListBean districtListBean1;
    private org.sola.clients.beans.referencedata.EstateListBean estateListBean1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.beans.referencedata.LandUseTypeListBean landUseTypeListBean1;
    private javax.swing.JLabel lblAllotmentHolder;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblAreaImperial;
    private javax.swing.JLabel lblDateOfRegistration;
    private javax.swing.JLabel lblDeedNum;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDistrict;
    private javax.swing.JLabel lblEstate;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblParcelName;
    private javax.swing.JLabel lblPurpose;
    private javax.swing.JLabel lblTaxAllot;
    private javax.swing.JLabel lblTown;
    private javax.swing.JLabel lblTownAllot;
    private javax.swing.JRadioButton radTaxAllot;
    private javax.swing.JRadioButton radTownAllot;
    private org.sola.clients.beans.referencedata.TownListBean townListBean1;
    private javax.swing.JTextField txtAllotmentHolder;
    private org.sola.clients.swing.common.controls.WatermarkFormattedTextField txtArea;
    private org.sola.clients.swing.common.controls.WatermarkFormattedTextField txtAreaImperial;
    private org.sola.clients.swing.common.controls.WatermarkDate txtDateOfRegistration;
    private javax.swing.JTextField txtDeedNum;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtParcelName;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
