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
package org.sola.clients.beans.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.common.DateUtility;
import org.sola.common.StringUtility;

/**
 * Bean used to format and enhance the ApplicationBean for reporting purposes.
 *
 * @author solaDev
 */
public class ApplicationReportBean {

    private static final String MEDIUM_DATE_FORMAT = "d MMM yyyy";
    private static final String MEDIUM_DATETIME_FORMAT = "d MMM yyyy h:mm a";
    private static final String MEDIUM_TIME_FORMAT = "h:mm a";
    private static final String MONEY_FORMAT = "T$#,##0.00";
    private ApplicationBean appBean;
    private DefaultFormatter moneyFormat;
    // This field allows all public methods on the ApplicationReportBean to be exposed
    // directly in JasperReports
    private transient ApplicationReportBean appReportBean;

    public ApplicationReportBean() {
        super();
        // Format for Tongan $
        moneyFormat = new NumberFormatter(new DecimalFormat(MONEY_FORMAT));
        moneyFormat.setValueClass(BigDecimal.class);
    }

    public ApplicationReportBean(ApplicationBean appBean) {
        this();
        this.appBean = appBean;

    }

    public ApplicationBean getAppBean() {
        return appBean;
    }

    public void setAppBean(ApplicationBean appBean) {
        this.appBean = appBean;
    }

    /**
     * Allows the methods on the ApplicationReportBean class to be exposed to
     * JasperReports without the need to explicitly map a number of dummy fields
     */
    public ApplicationReportBean getAppReportBean() {
        return this;
    }

    /**
     * Formats a date value using the custom medium date format mask
     */
    public String formatDate(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_DATE_FORMAT);
        }
        return result;
    }

    /**
     * Formats a datetime value using the custom medium date format mask
     */
    public String formatDateTime(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_DATETIME_FORMAT);
        }
        return result;
    }

    /**
     * Formats a time value using the custom medium time format mask
     */
    public String formatTime(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_TIME_FORMAT);
        }
        return result;
    }

    /**
     * Formats money values with Tongan currency symbol.
     */
    public String formatMoney(BigDecimal amount) {
        String result = "";
        if (amount != null) {
            try {
                result = moneyFormat.valueToString(amount);
            } catch (ParseException ex) {
            }
        }
        return result;
    }

    /**
     * Determines the primary contact information for the contact person.
     * Returns the first populated value from mobile, phone and email. If none
     * are populated, empty string ("") is returned.
     */
    public String getPrimaryContactText() {
        return this.appBean.getContactPerson() == null ? ""
                : StringUtility.isEmpty(this.appBean.getContactPerson().getMobile()) == false
                ? "Ph: " + this.appBean.getContactPerson().getMobile()
                : StringUtility.isEmpty(this.appBean.getContactPerson().getPhone()) == false
                ? "Ph: " + this.appBean.getContactPerson().getPhone()
                : StringUtility.isEmpty(this.appBean.getContactPerson().getEmail()) == false
                ? "eMail: " + this.appBean.getContactPerson().getEmail()
                : "";
    }

    /**
     * Generates a sentence to use as the introduction for the Lodgement Notice
     */
    public String getLodgementNoticeIntroText() {
        String result = "Application " + this.appBean.getNr()
                + " lodged on " + formatDate(this.appBean.getLodgingDatetime())
                + " at " + formatTime(this.appBean.getLodgingDatetime());

        // Determine what the application is for and generate a suitable application description
        if (this.appBean.hasService(RequestTypeBean.CODE_REGISTER_SUBLEASE)) {
            result += " for a new sublease";
        } else if (this.appBean.hasService(RequestTypeBean.CODE_REGISTER_LEASE)) {
            result += " for a new lease";
        } else if (this.appBean.hasService(RequestTypeBean.CODE_REGISTER_TAX_API)) {
            result += " for a new tax allotment";
        } else if (this.appBean.hasService(RequestTypeBean.CODE_REGISTER_TOWN_API)) {
            result += " for a new town allotment";
        } else if (!StringUtility.isEmpty(this.appBean.getSelectedProperty().getSubleaseNumber())) {
            result += " affecting sublease " + this.appBean.getSelectedProperty().getSubleaseNumber();
        } else if (!StringUtility.isEmpty(this.appBean.getSelectedProperty().getLeaseNumber())) {
            result += " affecting lease " + this.appBean.getSelectedProperty().getLeaseNumber();
        } else {
            if (!StringUtility.isEmpty(this.appBean.getSelectedProperty().getNameFirstpart())
                    && !StringUtility.isEmpty(this.appBean.getSelectedProperty().getNameLastpart())) {
                result += " affecting allotment " + this.appBean.getSelectedProperty().getNameFirstpart()
                        + "/" + this.appBean.getSelectedProperty().getNameLastpart();
            } else {
                result += " affecting allotment";
            }
        }
        result += this.appBean.getSelectedProperty().getTown() != null
                ? " located at " + this.appBean.getSelectedProperty().getTown().getDisplayValue()
                : "";
        result += ".";
        return result;
    }
}
