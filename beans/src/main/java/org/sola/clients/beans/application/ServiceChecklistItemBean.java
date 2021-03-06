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
package org.sola.clients.beans.application;

import org.hibernate.validator.constraints.Length;
import org.sola.clients.beans.AbstractVersionedBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/**
 *
 * @author Admin
 */
public class ServiceChecklistItemBean extends AbstractVersionedBean {

    public static final String RESULT_PROPERTY = "result";
    public static final String COMMENT_PROPERTY = "comment";
    public static final String SERVICE_ID_PROPERTY = "serviceId";
    public static final String CHECKLIST_ITEM_CODE_PROPERTY = "checklistItemCode";
    public static final String COMPLIES_PROPERTY = "complies";
    private String serviceId;
    private String checklistItemCode;
    private String checklistItemDisplayValue;
    private String checklistItemDescription;
    @Length(max = 1000, message = ClientMessage.CHECK_COMMENT_LENGTH, payload = Localized.class)
    private String comment;
    private String result;
    private Boolean complies;

    public ServiceChecklistItemBean() {
        super();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String value) {
        String oldValue = serviceId;
        serviceId = value;
        propertySupport.firePropertyChange(SERVICE_ID_PROPERTY, oldValue, value);
    }

    public String getChecklistItemCode() {
        return checklistItemCode;
    }

    public void setChecklistItemCode(String value) {
        String oldValue = checklistItemCode;
        checklistItemCode = value;
        propertySupport.firePropertyChange(CHECKLIST_ITEM_CODE_PROPERTY, oldValue, value);
    }

    public String getChecklistItemDisplayValue() {
        return checklistItemDisplayValue;
    }

    public void setChecklistItemDisplayValue(String checklistItemDisplayValue) {
        this.checklistItemDisplayValue = checklistItemDisplayValue;
    }

    public String getChecklistItemDescription() {
        return checklistItemDescription;
    }

    public void setChecklistItemDescription(String checklistItemDescription) {
        this.checklistItemDescription = checklistItemDescription;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        String oldValue = comment;
        comment = value;
        propertySupport.firePropertyChange(COMMENT_PROPERTY, oldValue, value);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String value) {
        String oldValue = result;
        result = value;
        Boolean temp = "t".equalsIgnoreCase(value) ? true : false;
        if (temp != getComplies()) {
            setComplies(temp);
        }
        propertySupport.firePropertyChange(RESULT_PROPERTY, oldValue, value);
    }

    /**
     * Use boolResult to convert the result string to a boolean for display on
     * the ChecklistForm.
     *
     * @return
     */
    public Boolean getComplies() {
        return complies;
    }

    public void setComplies(Boolean value) {
        Boolean oldValue = complies;
        complies = value;
        String temp = value ? "t" : "f";
        if (!temp.equalsIgnoreCase(getResult())) {
            setResult(temp);
        }
        propertySupport.firePropertyChange(COMPLIES_PROPERTY, oldValue, value);
    }
}
