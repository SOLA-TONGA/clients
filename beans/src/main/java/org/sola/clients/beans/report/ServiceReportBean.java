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

import java.util.List;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.system.LanguageBean;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.AbstractCodeTO;

/**
 *
 * @author Admin
 */
public class ServiceReportBean extends CommonReportBean {

    private ApplicationBean appBean;
    private ApplicationServiceBean appServiceBean;
    // This field allows all public methods on the ServiceReportBean to be exposed
    // directly in JasperReports
    private transient ServiceReportBean serviceReportBean;

    public ServiceReportBean() {
        super();
    }

    public ServiceReportBean(ApplicationBean appBean, ApplicationServiceBean appServiceBean) {
        super();
        this.appBean = appBean;
        this.appServiceBean = appServiceBean;

    }

    /** Returns this object so that JasperReports can access the public methods
     * on this class.  
     */
    public ServiceReportBean getServiceReportBean() {
        return this;
    }

    public ApplicationBean getAppBean() {
        return appBean;
    }

    public void setAppBean(ApplicationBean appBean) {
        this.appBean = appBean;
    }

    public ApplicationServiceBean getAppServiceBean() {
        return appServiceBean;
    }

    public void setAppServiceBean(ApplicationServiceBean appServiceBean) {
        this.appServiceBean = appServiceBean;
    }

    public String getLandPurposeTongan() {
        String result = null;
        if (getAppProperty() != null && getAppProperty().getLandUseCode() != null) {
            result = getTonganDisplayValue(WSManager.getInstance().getReferenceDataService().getLandUseTypes(null),
                    getAppProperty().getLandUseCode(),
                    getAppProperty().getLandUseType().getDisplayValue());
        }
        return result;
    }

    /**
     * Obtains the Tongan display value for the specified code.
     *
     * @param <T>
     * @param toList The list of code TOs correspodning to the code value with
     * no language translation (i.e. retrieved from the web service with
     * langCode = null)
     * @param code The code to find the Tongan translation for
     * @param defaultValue The default display value to use if there is no
     * Tongan translation
     * @return The Tongan display value for the code or the default value.
     */
    private <T extends AbstractCodeTO> String getTonganDisplayValue(List<T> toList,
            String code, String defaultValue) {
        String result = defaultValue;
        String displayValues = null;
        for (AbstractCodeTO to : toList) {
            if (code.equals(to.getCode())) {
                displayValues = to.getDisplayValue();
                break;
            }
        }
        if (displayValues != null) {
            String temp = LanguageBean.getLocalizedValue(displayValues, LanguageBean.TONGAN_LANG_CODE);
            // Check that a valid display value was found
            if (temp != null && temp.split(LanguageBean.delimiter).length == 1) {
                result = temp;
            }
        }
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
