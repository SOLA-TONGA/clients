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
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.BaUnitSearchResultBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.administrative.TongaBaUnitSearchPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to search BA units.
 */
public class BaUnitSearchPanel extends ContentPanel {

    public static final String SELECTED_RESULT_PROPERTY = "selectedResult";

    /**
     * Default constructor.
     */
    public BaUnitSearchPanel() {
        initComponents();
        setHeaderPanel(headerPanel1);
        searchPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(TongaBaUnitSearchPanel.OPEN_BAUNIT_SEARCH_RESULT)) {
                    BaUnitSearchResultBean selectedResult = (BaUnitSearchResultBean) evt.getNewValue();
                    if (selectedResult != null) {
                        openPropertyForm(selectedResult.getId());
                    }
                }
                if (evt.getPropertyName().equals(TongaBaUnitSearchPanel.SELECT_BAUNIT_SEARCH_RESULT)) {
                    BaUnitSearchResultBean selectedResult = (BaUnitSearchResultBean) evt.getNewValue();
                    if (selectedResult != null) {
                        selectResult(selectedResult);
                    }
                }
            }
        });
    }

    private void openPropertyForm(final String baUnitId) {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));
                TongaPropertyPanel propertyPanel = new TongaPropertyPanel(BaUnitBean.getBaUnitsById(baUnitId));
                getMainContentPanel().addPanel(propertyPanel, MainContentPanel.CARD_PROPERTY_PANEL, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void selectResult(final BaUnitSearchResultBean selectedSearchBean) {
        // Retrieve the full BaUnit and return to the calling form
        if (selectedSearchBean != null) {
            SolaTask t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                    BaUnitBean selectedBaUnit = BaUnitBean.getBaUnitsById(selectedSearchBean.getId());
                    Object[] result = new Object[]{selectedBaUnit};
                    firePropertyChange(SELECTED_RESULT_PROPERTY, null, result);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
            getMainContentPanel().closePanel(this);
        }
    }
    
    /**
     * returns th search panel for additional configuration
     * @return 
     */
    public TongaBaUnitSearchPanel getSearchPanel() {
        return searchPanel; 
    }

    public void clickFind() {
        searchPanel.clickFind();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        searchPanel = new org.sola.clients.swing.ui.administrative.TongaBaUnitSearchPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        setHelpTopic(bundle.getString("BaUnitSearchPanel.helpTopic")); // NOI18N
        setName("Form"); // NOI18N

        headerPanel1.setName("headerPanel1"); // NOI18N
        headerPanel1.setTitleText(bundle.getString("BaUnitSearchPanel.headerPanel1.titleText")); // NOI18N

        searchPanel.setName("searchPanel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addComponent(headerPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private org.sola.clients.swing.ui.administrative.TongaBaUnitSearchPanel searchPanel;
    // End of variables declaration//GEN-END:variables
}
