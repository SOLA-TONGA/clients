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
package org.sola.clients.beans.cashier;

import java.math.BigDecimal;
import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;

/**
 * Bean representing a record of data from the Cashier CSV file. The CSV file is
 * generated from the Cashier database (Access Db) and the columns that each
 * field maps back to is listed below.
 *
 * @author Admin
 */
public class CashierImportBean extends AbstractBindingBean {

    public static final String RECORD_ID_PROPERTY = "recordId";
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String PAYMENT_DATE_PROPERTY = "paymentDate";
    public static final String PAYMENT_PARTICULARS_PROPERTY = "paymentParticulars";
    public static final String PAYMENT_DESC_PROPERTY = "paymentDescription";
    public static final String TOTAL_PAYMENT_PROPERTY = "totalPayment";
    public static final String RECEIPT_NUM_PROPERTY = "receiptNumber";
    public static final String RENT_GOV_PROPERTY = "rentGov";
    public static final String RENTAL_TAX_PROPERTY = "rentalTax";
    public static final String DEED_LEASE_PROPERTY = "deedLease";
    public static final String REGISTER_FEE_PROPERTY = "registerFee";
    public static final String TRANSFER_FEE_PROPERTY = "transferFee";
    public static final String SURVEY_FEE_PROPERTY = "surveyFee";
    private int recordId; // daily_report_tbl.00_id
    private String leaseNumber; // daily_report_tbl.23_lease_number
    private Date paymentDate; // daily_report_tbl.01_payment_date
    private String paymentParticulars; // daily_report_tbl.04_payment_particulars
    private String paymentDescription; // daily_report_tbl.06_payment_description
    private BigDecimal totalPayment; // daily_report_tbl.20_total_payment
    private String receiptNumber; // daily_report_tbl.18_receipt_number + / + daily_report_tbl.27_rec_manual
    private BigDecimal rentGov; // daily_report_tbl.10_gov_land_rental
    private BigDecimal rentalTax; // daily_report_tbl.21_rental_tax_lot
    private BigDecimal deedLease; // daily_report_tbl.16_deed_lease
    private BigDecimal registerFee; // daily_report_tbl.13_register_fee
    private BigDecimal transferFee; // daily_report_tbl.14_transfer_fee
    private BigDecimal surveyFee; // daily_report_tbl.09_survey_fee

    public CashierImportBean() {
        super();
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date value) {
        Date oldValue = this.paymentDate;
        this.paymentDate = value;
        propertySupport.firePropertyChange(PAYMENT_DATE_PROPERTY, oldValue, value);;
    }

    public String getPaymentParticulars() {
        return paymentParticulars;
    }

    public void setPaymentParticulars(String value) {
        String oldValue = this.paymentParticulars;
        this.paymentParticulars = value;
        propertySupport.firePropertyChange(PAYMENT_PARTICULARS_PROPERTY, oldValue, value);
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String value) {
        String oldValue = this.paymentDescription;
        this.paymentDescription = value;
        propertySupport.firePropertyChange(PAYMENT_DESC_PROPERTY, oldValue, value);
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal value) {
        BigDecimal oldValue = this.totalPayment;
        this.totalPayment = value;
        propertySupport.firePropertyChange(TOTAL_PAYMENT_PROPERTY, oldValue, value);
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String value) {
        String oldValue = this.receiptNumber;
        this.receiptNumber = value;
        propertySupport.firePropertyChange(RECEIPT_NUM_PROPERTY, oldValue, value);
    }

    public BigDecimal getRentGov() {
        return rentGov;
    }

    public void setRentGov(BigDecimal value) {
        BigDecimal oldValue = this.rentGov;
        this.rentGov = value;
        propertySupport.firePropertyChange(RENT_GOV_PROPERTY, oldValue, value);
    }

    public BigDecimal getRentalTax() {
        return rentalTax;
    }

    public void setRentalTax(BigDecimal value) {
        BigDecimal oldValue = this.rentalTax;
        this.rentalTax = value;
        propertySupport.firePropertyChange(RENTAL_TAX_PROPERTY, oldValue, value);
    }

    public BigDecimal getDeedLease() {
        return deedLease;
    }

    public void setDeedLease(BigDecimal value) {
        BigDecimal oldValue = this.deedLease;
        this.deedLease = value;
        propertySupport.firePropertyChange(DEED_LEASE_PROPERTY, oldValue, value);
    }

    public BigDecimal getRegisterFee() {
        return registerFee;
    }

    public void setRegisterFee(BigDecimal value) {
        BigDecimal oldValue = this.registerFee;
        this.registerFee = value;
        propertySupport.firePropertyChange(REGISTER_FEE_PROPERTY, oldValue, value);
    }

    public BigDecimal getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(BigDecimal value) {
        BigDecimal oldValue = this.transferFee;
        this.transferFee = value;
        propertySupport.firePropertyChange(TRANSFER_FEE_PROPERTY, oldValue, value);
    }

    public BigDecimal getSurveyFee() {
        return surveyFee;
    }

    public void setSurveyFee(BigDecimal value) {
        BigDecimal oldValue = this.surveyFee;
        this.surveyFee = value;
        propertySupport.firePropertyChange(SURVEY_FEE_PROPERTY, oldValue, value);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String value) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = value;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, value);
    }
}
