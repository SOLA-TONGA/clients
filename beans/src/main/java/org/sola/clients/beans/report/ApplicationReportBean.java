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
package org.sola.clients.beans.report;

import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.common.StringUtility;

/**
 * Bean used to format and enhance the ApplicationBean for reporting purposes.
 *
 * @author solaDev
 */
public class ApplicationReportBean extends CommonReportBean {

    private ApplicationBean appBean;
    // This field allows all public methods on the ApplicationReportBean to be exposed
    // directly in JasperReports
    private transient ApplicationReportBean appReportBean;

    public ApplicationReportBean() {
        super();
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
        } else if (getAppProperty() != null) {
            if (!StringUtility.isEmpty(getAppProperty().getSubleaseNumber())) {
                result += " affecting sublease " + getAppProperty().getSubleaseNumber();
            } else if (!StringUtility.isEmpty(getAppProperty().getLeaseNumber())) {
                result += " affecting lease " + getAppProperty().getLeaseNumber();
            } else {
                if (!StringUtility.isEmpty(getAppProperty().getNameFirstpart())
                        && !StringUtility.isEmpty(getAppProperty().getNameLastpart())) {
                    result += " affecting allotment " + getAppProperty().getNameFirstpart()
                            + "/" + getAppProperty().getNameLastpart();
                } else {
                    result += " affecting allotment";
                }
            }
        }
        if (getAppProperty() != null) {
            result += getAppProperty().getTown() != null
                    ? " located at " + getAppProperty().getTown().getDisplayValue()
                    : "";
        }
        result += ".";
        return result;
    }

    /**
     * Uses the first property in the property list as the Application Property.
     *
     * @return
     */
    public ApplicationPropertyBean getAppProperty() {
        ApplicationPropertyBean result = null;
        if (getAppBean() != null && getAppBean().getFilteredPropertyList().size() > 0) {
            result = getAppBean().getFilteredPropertyList().get(0);
        }
        return result;
    }
}
