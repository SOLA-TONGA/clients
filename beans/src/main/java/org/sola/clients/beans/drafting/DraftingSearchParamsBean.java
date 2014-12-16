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
package org.sola.clients.beans.drafting;

import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author Admin
 */
public class DraftingSearchParamsBean extends AbstractBindingBean{
    public static final String ITEM_NUMBER_PROPERTY = "itemNumber";
    public static final String FIRST_NAME_PROPERTY = "firstName";
    public static final String LAST_NAME_PROPERTY = "lastName";
    public static final String DATE_RECEIVED_PROPERTY = "dateReceived";
    public static final String LOCATION_PROPERTY = "location";
    public static final String PLAN_NUMBER_PROPERTY = "planNumber";
    private String itemNumber;
    private String firstName;
    private String lastName;
    private Date dateReceived;
    private String location;
    private String planNumber;
   
    public DraftingSearchParamsBean(){
        super();
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String value) {
        String oldValue = this.itemNumber;
        this.itemNumber = value;
        propertySupport.firePropertyChange(ITEM_NUMBER_PROPERTY, oldValue, value);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String value) {
        String oldValue = this.firstName;
        this.firstName = value;
        propertySupport.firePropertyChange(FIRST_NAME_PROPERTY, oldValue, value);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String value) {
        String oldValue = this.lastName;
        this.lastName = value;
        propertySupport.firePropertyChange(LAST_NAME_PROPERTY, oldValue, value);
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date value) {
        Date oldValue = this.dateReceived;
        this.dateReceived = value;
        propertySupport.firePropertyChange(DATE_RECEIVED_PROPERTY, oldValue, value);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        String oldValue = this.location;
        this.location = value;
        propertySupport.firePropertyChange(LOCATION_PROPERTY, oldValue, value);
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String value) {
        String oldValue = this.planNumber;
        this.planNumber = value;
        propertySupport.firePropertyChange(PLAN_NUMBER_PROPERTY, oldValue, value);
    }
    
    public void clear() {
        this.setItemNumber(null);
        this.setDateReceived(null);
        this.setFirstName(null);
        this.setLastName(null);
        this.setLocation(null);
        this.setPlanNumber(null);
    }
}
