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

import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.referencedata.BaUnitTypeBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.application.AllotmentPanel;
import org.sola.clients.swing.ui.application.LeasePanel;
import org.sola.clients.swing.ui.application.SubleasePanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author soladev
 */
public class ApplicationPropertyForm extends ContentPanel {

    public static final String RESULT_PROPERTY = "resultProperty";
    private final static String ALLOTMENT_CARD = "allotmentCard";
    private final static String LEASE_CARD = "leaseCard";
    private final static String SUBLEASE_CARD = "subleaseCard";
    String propertyType;
    String appNr;
    boolean readOnly;

    /**
     * Creates new form PropertyForm
     */
    public ApplicationPropertyForm() {
        this(null, BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT, "", true);
    }

    public ApplicationPropertyForm(ApplicationPropertyBean property, String propertyType,
            String appNr, boolean readOnly) {
        this.property = property;
        this.propertyType = propertyType;
        this.readOnly = readOnly;
        this.appNr = appNr;
        initComponents();
        postInit();
    }

    /**
     * Create the Application Property for the form if it is currently NULL.
     */
    private ApplicationPropertyBean createAppProperty() {
        if (property == null) {
            property = new ApplicationPropertyBean();
            property.setTypeCode(propertyType);
        }
        return property;
    }

    private void postInit() {
        // Determine the panel to display based on the property type
        if (BaUnitTypeBean.CODE_LEASED_UNIT.equals(propertyType)) {
            // Display the lease card on the form
            pnlPropertyCards.add(new LeasePanel(property, readOnly), LEASE_CARD);
        } else if (BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(propertyType)) {
            // Display the sublease card on the form
            pnlPropertyCards.add(new SubleasePanel(property, readOnly), SUBLEASE_CARD);
        } else {
            // Display the allotment card on the form
            pnlPropertyCards.add(new AllotmentPanel(property, readOnly), ALLOTMENT_CARD);
        }

        btnVerify.setEnabled(!readOnly);
        btnClear.setEnabled(!readOnly);
    }

    /**
     * Validates the property details
     */
    private void verifyProperty(final boolean isClosing) {
        SolaTask t = new SolaTask<Void, Void>() {
            boolean result = false;

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_VALIDATING));
                result = ApplicationBean.verifyProperty(property, appNr, false);
                return null;
            }

            @Override
            protected void taskDone() {
                if (isClosing) {
                    // Return the result to the calling screen after the validation is completed
                    Object[] result = new Object[]{property};
                    firePropertyChange(RESULT_PROPERTY, null, result);
                } else if (result) {
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_PROPERTY_VERIFIED);
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        property = createAppProperty();
        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSaveClose = new org.sola.clients.swing.common.buttons.BtnSave();
        btnVerify = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        pnlPropertyCards = new javax.swing.JPanel();

        setHeaderPanel(headerPanel1);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        headerPanel1.setTitleText(bundle.getString("ApplicationPropertyForm.headerPanel1.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSaveClose.setText(bundle.getString("ApplicationPropertyForm.btnSaveClose.text")); // NOI18N
        btnSaveClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCloseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveClose);

        btnVerify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/verify.png"))); // NOI18N
        btnVerify.setText(bundle.getString("ApplicationPropertyForm.btnVerify.text")); // NOI18N
        btnVerify.setFocusable(false);
        btnVerify.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerifyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnVerify);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/eraser.png"))); // NOI18N
        btnClear.setText(bundle.getString("ApplicationPropertyForm.btnClear.text")); // NOI18N
        btnClear.setFocusable(false);
        btnClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClear);

        pnlPropertyCards.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPropertyCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlPropertyCards, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCloseActionPerformed
        if (property != null) {
            if (!property.isVerifiedExists()) {
                verifyProperty(true);
            } else {
                // Notify the calling screen of the new property object. 
                Object[] result = new Object[]{property};
                firePropertyChange(RESULT_PROPERTY, null, result);
            }
        }
        close();
    }//GEN-LAST:event_btnSaveCloseActionPerformed

    private void btnVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifyActionPerformed
        verifyProperty(false);
    }//GEN-LAST:event_btnVerifyActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        property.reset();
    }//GEN-LAST:event_btnClearActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private org.sola.clients.swing.common.buttons.BtnSave btnSaveClose;
    private javax.swing.JButton btnVerify;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pnlPropertyCards;
    private org.sola.clients.beans.application.ApplicationPropertyBean property;
    // End of variables declaration//GEN-END:variables
}
