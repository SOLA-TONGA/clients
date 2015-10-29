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
public class MinisterInwardSearchParamsBean extends AbstractBindingBean{
    public static final String SUBJECT_PROPERTY = "subject";
    public static final String DATE_IN_FROM_PROPERTY = "dateInFrom";
    public static final String DATE_IN_TO_PROPERTY = "dateInTo";
    public static final String FILE_NUMBER_PROPERTY = "fileNumber";
    public static final String DATE_OUT_FROM_PROPERTY = "dateOutFrom";
    public static final String DATE_OUT_TO_PROPERTY = "dateOutTo";
    public static final String FROM_WHOM_PROPERTY = "fromWhom";
    private String subject;
    private Date dateInFrom;
    private Date dateInTo;
    private String fileNumber;
    private Date dateOutFrom;
    private Date dateOutTo;
    private String fromWhom;
    
    public MinisterInwardSearchParamsBean(){
        super();
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        String oldValue = this.subject;
        this.subject = value;
        propertySupport.firePropertyChange(SUBJECT_PROPERTY, oldValue, value);
    }

    public Date getDateInFrom() {
        return dateInFrom;
    }

    public void setDateInFrom(Date value) {
        Date oldValue = this.dateInFrom;
        this.dateInFrom = value;
        propertySupport.firePropertyChange(DATE_IN_FROM_PROPERTY, oldValue, value);
    }
    
    public Date getDateInTo() {
        return dateInTo;
    }

    public void setDateInTo(Date value) {
        Date oldValue = this.dateInTo;
        this.dateInTo = value;
        propertySupport.firePropertyChange(DATE_IN_TO_PROPERTY, oldValue, value);
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String value) {
        String oldValue = this.fileNumber;
        this.fileNumber = value;
        propertySupport.firePropertyChange(FILE_NUMBER_PROPERTY, oldValue, value);
    }

    public Date getDateOutFrom() {
        return dateOutFrom;
    }

    public void setDateOutFrom(Date value) {
        Date oldValue = this.dateOutFrom;
        this.dateOutFrom = value;
        propertySupport.firePropertyChange(DATE_OUT_FROM_PROPERTY, oldValue, value);
    }
    
    public Date getDateOutTo() {
        return dateOutTo;
    }

    public void setDateOutTo(Date value) {
        Date oldValue = this.dateOutTo;
        this.dateOutTo = value;
        propertySupport.firePropertyChange(DATE_OUT_TO_PROPERTY, oldValue, value);
    }
    
    public String getFromWhom() {
        return fromWhom;
    }

    public void setFromWhom(String value) {
        String oldValue = this.fromWhom;
        this.fromWhom = value;
        propertySupport.firePropertyChange(FROM_WHOM_PROPERTY, oldValue, value);
    }
    
    public void clear(){
        this.setSubject(null);
        this.setDateInFrom(null);
        this.setDateInTo(null);
        this.setFileNumber(null);
        this.setDateOutFrom(null);
        this.setDateOutTo(null);
        this.setFromWhom(null);
    }
    
}
