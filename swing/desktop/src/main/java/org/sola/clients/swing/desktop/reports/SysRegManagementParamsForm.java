/*
 * Copyright 2012 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.swing.desktop.reports;

import java.awt.ComponentOrientation;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.beans.administrative.SysRegManagementBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author RizzoM
 */
public class SysRegManagementParamsForm extends javax.swing.JDialog {

    private String location;
    private String tmpLocation = "";

    /**
     * Creates new form SysRegManagementParamsForm
     */
    public SysRegManagementParamsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchParams = new org.sola.clients.beans.administrative.SysRegManagementParamsBean();
        sysRegManagementBean = createSysRegManagementBean();
        reportViewerPanel = new org.sola.clients.swing.ui.reports.ReportViewerPanel();
        labHeader = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JFormattedTextField();
        btnShowCalendarFrom = new javax.swing.JButton();
        labFromDate = new javax.swing.JLabel();
        txtToDate = new javax.swing.JFormattedTextField();
        labToDate = new javax.swing.JLabel();
        btnShowCalendarTo = new javax.swing.JButton();
        cadastreObjectSearch = new org.sola.clients.swing.ui.cadastre.LocationSearch();
        labSearchArea = new javax.swing.JLabel();
        viewReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labHeader.setBackground(new java.awt.Color(255, 153, 0));
        labHeader.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labHeader.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/reports/Bundle"); // NOI18N
        labHeader.setText(bundle.getString("SysRegManagementParamsForm.labHeader.text")); // NOI18N
        labHeader.setOpaque(true);

        txtFromDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtFromDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtFromDate.setToolTipText(bundle.getString("SysRegManagementParamsForm.txtFromDate.toolTipText")); // NOI18N
        txtFromDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFromDate.setHorizontalAlignment(JTextField.LEADING);
        txtFromDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtFromDatePropertyChange(evt);
            }
        });

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("SysRegManagementParamsForm.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);
        btnShowCalendarFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFromActionPerformed(evt);
            }
        });

        labFromDate.setText(bundle.getString("SysRegManagementParamsForm.labFromDate.text")); // NOI18N

        txtToDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtToDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtToDate.setToolTipText(bundle.getString("SysRegManagementParamsForm.txtToDate.toolTipText")); // NOI18N
        txtToDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtToDate.setHorizontalAlignment(JTextField.LEADING);

        labToDate.setText(bundle.getString("SysRegManagementParamsForm.labToDate.text")); // NOI18N

        btnShowCalendarTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarTo.setText(bundle.getString("SysRegManagementParamsForm.btnShowCalendarTo.text")); // NOI18N
        btnShowCalendarTo.setBorder(null);
        btnShowCalendarTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarToActionPerformed(evt);
            }
        });

        cadastreObjectSearch.setText(bundle.getString("SysRegManagementParamsForm.cadastreObjectSearch.text")); // NOI18N

        labSearchArea.setText(bundle.getString("SysRegManagementParamsForm.labSearchArea.text")); // NOI18N

        viewReport.setText(bundle.getString("SysRegManagementParamsForm.viewReport.text")); // NOI18N
        viewReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reportViewerPanelLayout = new javax.swing.GroupLayout(reportViewerPanel);
        reportViewerPanel.setLayout(reportViewerPanelLayout);
        reportViewerPanelLayout.setHorizontalGroup(
            reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(reportViewerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSearchArea)
                    .addGroup(reportViewerPanelLayout.createSequentialGroup()
                        .addComponent(labFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(labToDate))
                    .addGroup(reportViewerPanelLayout.createSequentialGroup()
                        .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowCalendarFrom)
                        .addGap(40, 40, 40)
                        .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowCalendarTo))
                    .addGroup(reportViewerPanelLayout.createSequentialGroup()
                        .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewReport)))
                .addContainerGap())
        );
        reportViewerPanelLayout.setVerticalGroup(
            reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportViewerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labHeader)
                .addGap(19, 19, 19)
                .addGroup(reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(reportViewerPanelLayout.createSequentialGroup()
                        .addGroup(reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labFromDate)
                            .addComponent(labToDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnShowCalendarFrom)
                            .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnShowCalendarTo))
                .addGap(30, 30, 30)
                .addComponent(labSearchArea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(reportViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewReport))
                .addContainerGap(137, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportViewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportViewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFromDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtFromDatePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromDatePropertyChange

    private void btnShowCalendarFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFromActionPerformed
        showCalendar(txtFromDate);
    }//GEN-LAST:event_btnShowCalendarFromActionPerformed

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
    private void btnShowCalendarToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarToActionPerformed
        showCalendar(txtToDate);
    }//GEN-LAST:event_btnShowCalendarToActionPerformed

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setVisible(true);
        form.setAlwaysOnTop(true);
    }
    
     private SysRegManagementBean createSysRegManagementBean() {
    if (sysRegManagementBean == null) {
            sysRegManagementBean = new SysRegManagementBean();
        }
        return sysRegManagementBean;
     }
    
    private void viewReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReportActionPerformed
        boolean dateFilled = false;
        Date tmpFrom;
        Date tmpTo = (Date) txtFromDate.getValue();
        if (cadastreObjectSearch.getSelectedElement() != null) {
            this.location = cadastreObjectSearch.getSelectedElement().toString();
            tmpLocation = (this.location.substring(this.location.indexOf("/") + 1).trim());
            searchParams.setNameLastpart(tmpLocation);
        } else {
            MessageUtility.displayMessage(ClientMessage.CHECK_SELECT_LOCATION);
            return;
        }

        if (txtFromDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATEFROM);
            dateFilled = false;
            return;
        } else {
            tmpFrom = (Date) txtFromDate.getValue();
            dateFilled = true;
            searchParams.setFromDate(tmpFrom);
        }
        if (txtToDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATETO);
            dateFilled = true;
            return;
        } else {
            tmpTo = (Date) txtToDate.getValue();
            searchParams.setToDate(tmpTo);
        }

        System.out.println(dateFilled);
        System.out.println(txtFromDate.getValue());
        System.out.println(txtToDate.getValue());
        if (dateFilled) {
            sysRegManagementBean.passParameter(searchParams);
            showReport(ReportManager.getSysRegManagementReport(sysRegManagementBean, tmpFrom, tmpTo, tmpLocation));
            this.dispose();
        }

        this.dispose();
    }//GEN-LAST:event_viewReportActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarTo;
    private org.sola.clients.swing.ui.cadastre.LocationSearch cadastreObjectSearch;
    private javax.swing.JLabel labFromDate;
    private javax.swing.JLabel labHeader;
    private javax.swing.JLabel labSearchArea;
    private javax.swing.JLabel labToDate;
    private org.sola.clients.swing.ui.reports.ReportViewerPanel reportViewerPanel;
    private org.sola.clients.beans.administrative.SysRegManagementParamsBean searchParams;
    private org.sola.clients.beans.administrative.SysRegManagementBean sysRegManagementBean;
    private javax.swing.JFormattedTextField txtFromDate;
    private javax.swing.JFormattedTextField txtToDate;
    private javax.swing.JButton viewReport;
    // End of variables declaration//GEN-END:variables
}
