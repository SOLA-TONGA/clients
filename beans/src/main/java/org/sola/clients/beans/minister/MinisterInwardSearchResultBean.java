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
public class MinisterInwardSearchResultBean extends AbstractBindingBean{
    public static final String SUBJECT_PROPERTY = "subject";
    public static final String DATE_IN_PROPERTY = "dateIn";
    public static final String FILE_NUMBER_PROPERTY = "fileNumber";
    public static final String DATE_OUT_PROPERTY = "dateOut";
    public static final String DIRECTED_DIVISION_PROPERTY = "directedDivision";
    public static final String DIRECTED_OFFICER_PROPERTY = "directedOfficer";
    public static final String CEO_DIRECTION_PROPERTY = "ceoDirection";
    public static final String MINISTER_DIRECTION_PROPERTY = "ministerDirection";
    public static final String FROM_WHOM_PROPERTY = "fromWhom";
    public static final String REMARK_PROPERTY = "remark";
    private String id;
    private String subject;
    private Date dateIn;
    private String fileNumber;
    private Date dateOut;
    private String directedDivision;
    private String directedOfficer;
    private String ceoDirection;
    private String ministerDirection;
    private String fromWhom;
    private String remark;
    
    public MinisterInwardSearchResultBean(){
        super();
    }
     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    } 
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        String oldValue = this.subject;
        this.subject = value;
        propertySupport.firePropertyChange(SUBJECT_PROPERTY, oldValue, value);
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date value) {
        Date oldValue = this.dateIn;
        this.dateIn = value;
        propertySupport.firePropertyChange(DATE_IN_PROPERTY, oldValue, value);
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String value) {
        String oldValue = this.fileNumber;
        this.fileNumber = value;
        propertySupport.firePropertyChange(FILE_NUMBER_PROPERTY, oldValue, value);
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date value) {
        Date oldValue = this.dateOut;
        this.dateOut = value;
        propertySupport.firePropertyChange(DATE_OUT_PROPERTY, oldValue, value);
    }

    public String getDirectedDivision() {
        return directedDivision;
    }

    public void setDirectedDivision(String value) {
        String oldValue = this.directedDivision;
        this.directedDivision = value;
        propertySupport.firePropertyChange(DIRECTED_DIVISION_PROPERTY, oldValue, value);
    }
    
    public String getDirectedOfficer() {
        return directedOfficer;
    }

    public void setDirectedOfficer(String value) {
        String oldValue = this.directedOfficer;
        this.directedOfficer = value;
        propertySupport.firePropertyChange(DIRECTED_OFFICER_PROPERTY, oldValue, value);
    }

    public String getCeoDirection() {
        return ceoDirection;
    }

    public void setCeoDirection(String value) {
        String oldValue = this.ceoDirection;
        this.ceoDirection = value;
        propertySupport.firePropertyChange(CEO_DIRECTION_PROPERTY, oldValue, value);
    }

    public String getMinisterDirection() {
        return ministerDirection;
    }

    public void setMinisterDirection(String value) {
        String oldValue = this.ministerDirection;
        this.ministerDirection = value;
        propertySupport.firePropertyChange(MINISTER_DIRECTION_PROPERTY, oldValue, value);
    }

    public String getFromWhom() {
        return fromWhom;
    }

    public void setFromWhom(String value) {
        String oldValue = this.fromWhom;
        this.fromWhom = value;
        propertySupport.firePropertyChange(FROM_WHOM_PROPERTY, oldValue, value);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String value) {
        String oldValue = this.remark;
        this.remark = value;
        propertySupport.firePropertyChange(REMARK_PROPERTY, oldValue, value);
    }
    
    
}
