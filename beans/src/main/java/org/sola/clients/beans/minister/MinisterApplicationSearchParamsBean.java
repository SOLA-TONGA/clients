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
package org.sola.clients.beans.minister;

import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author Admin
 */
public class MinisterApplicationSearchParamsBean extends AbstractBindingBean{
    public static final String DATE_RECEIVED_FROM_PROPERTY = "dateReceivedFrom";
    public static final String DATE_RECEIVED_TO_PROPERTY = "dateReceivedTo";
    public static final String NAME_PROPERTY = "name";
    public static final String LOCATION_PROPERTY = "location";
    public static final String RECEIPT_NUMBER_PROPERTY = "receiptNumber";
    public static final String PAY_DATE_PROPERTY = "payDate";
    private Date dateReceivedFrom;
    private Date dateReceivedTo;
    private String name;
    private String location;
    private String receiptNumber;
    private Date payDate;
    
    public MinisterApplicationSearchParamsBean() {
        super();
    }
    
        public Date getDateReceivedFrom() {
        return dateReceivedFrom;
    }

    public void setDateReceivedFrom(Date value) {
        Date oldValue = this.dateReceivedFrom;
        this.dateReceivedFrom = value;
        propertySupport.firePropertyChange(DATE_RECEIVED_FROM_PROPERTY, oldValue, value);
    }
    
    public Date getDateReceivedTo() {
        return dateReceivedTo;
    }

    public void setDateReceivedTo(Date value) {
        Date oldValue = this.dateReceivedTo;
        this.dateReceivedTo = value;
        propertySupport.firePropertyChange(DATE_RECEIVED_TO_PROPERTY, oldValue, value);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String value) {
        String oldValue = this.name;
        this.name = value;
        propertySupport.firePropertyChange(NAME_PROPERTY, oldValue, value);
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        String oldValue = this.location;
        this.location = value;
        propertySupport.firePropertyChange(LOCATION_PROPERTY, oldValue, value);
    }
    
    public String getReceiptNumber() {
        return receiptNumber;
    }
    
    public void setReceiptNumber(String value) {
        String oldValue = this.receiptNumber;
        this.receiptNumber = value;
        propertySupport.firePropertyChange(RECEIPT_NUMBER_PROPERTY, oldValue, value);
    }
    
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date value) {
        Date oldValue = this.payDate;
        this.payDate = value;
        propertySupport.firePropertyChange(PAY_DATE_PROPERTY, oldValue, value);
    }
}
