/**
 * ******************************************************************************************
 * Copyright (C) 2013 - Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.swing.bulkoperations.spatialobjects;

import java.awt.BorderLayout;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.sola.clients.swing.gis.beans.TransactionCadastreChangeBean;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForCadastreChange;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForPublicDisplay;
import org.sola.clients.swing.gis.ui.controlsbundle.SolaControlsBundle;
import org.sola.clients.swing.ui.ContentPanel;

/**
 * The panel that displays the map in the application.
 * 
 * @author Elton Manoku
 */
public class MapPanel extends ContentPanel {

    private SolaControlsBundle mapControl = null;
    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(
            "org/sola/clients/swing/bulkoperations/spatialobjects/Bundle"); 
    /**
     * It initiates the panel with the target cadastre object type as parameter.
     *
     */
    public MapPanel(TransactionCadastreChangeBean transactionBean,
            String targetCadastreObjectType, String lastPartEntry) {
        this.initializeMap(transactionBean, targetCadastreObjectType, lastPartEntry);

        initComponents();
        customizeForm();
        this.addMapToForm();
        this.setName(this.getClass().getSimpleName());
    }

    public MapPanel(ReferencedEnvelope extentToShow) {
        this.initializeMap(extentToShow);

        initComponents();
        customizeForm();
        this.addMapToForm();
        this.setName(this.getClass().getSimpleName());
    }

    private void initializeMap(
            TransactionCadastreChangeBean transactionBean,
            String targetCadastreObjectType,
            String lastPartEntry) {
        this.mapControl = new ControlsBundleForCadastreChange(
                transactionBean, targetCadastreObjectType, lastPartEntry);
    }

    private void initializeMap(ReferencedEnvelope extentToShow){
        this.mapControl = new ControlsBundleForPublicDisplay();
        if (extentToShow != null){
            this.mapControl.getMap().setDisplayArea(extentToShow);
        }
    }

    private void addMapToForm() {
        this.mapPanel.setLayout(new BorderLayout());
        this.mapPanel.add(this.mapControl, BorderLayout.CENTER);
    }

    private void customizeForm() {
        String title =
                this.mapControl.getClass().equals(ControlsBundleForCadastreChange.class)? 
                bundle.getString("MapPanel.headerPanel.titleText.cadastreChange"): 
                bundle.getString("MapPanel.headerPanel.titleText.mapViewer");
        headerPanel.setTitleText(title);
    }

    @Override
    protected boolean panelClosing() {
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mapPanel = new javax.swing.JPanel();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();

        setHeaderPanel(headerPanel);
        setName("Form"); // NOI18N

        mapPanel.setName("mapPanel"); // NOI18N

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        headerPanel.setName("headerPanel"); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/spatialobjects/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("MapPanel.headerPanel.titleText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
            .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel mapPanel;
    // End of variables declaration//GEN-END:variables
}
