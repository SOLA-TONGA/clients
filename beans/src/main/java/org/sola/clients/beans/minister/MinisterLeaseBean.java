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
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.casemanagement.MinisterLeaseTO;

/**
 *
 * @author Admin
 */
public class MinisterLeaseBean extends AbstractIdBean{
    public static final String DATE_RECEIVED_PROPERTY = "dateReceived";
    public static final String NAME_PROPERTY = "name";
    public static final String PURPOSE_PROPERTY = "purpose";
    public static final String LOCATION_PROPERTY = "location";
    public static final String NOBLE_PROPERTY = "noble";
    public static final String LAND_TYPE_PROPERTY = "landType";
    public static final String TOTAL_AREA_PROPERTY = "totalArea";
    public static final String LEASE_AREA_PROPERTY = "leaseArea";
    public static final String TERM_PROPERTY = "term";
    public static final String RENT_PROPERTY = "rent";
    public static final String SURVEY_FEE_PROPERTY = "surveyFee";
    public static final String RECEIPT_NUMBER_PROPERTY = "receiptNumber";
    public static final String PAY_DATE_PROPERTY = "payDate";
    public static final String CEO_DIRECTION_PROPERTY = "ceoDirection";
    public static final String DIRECTED_DIVISION_PROPERTY = "directedDivision";
    public static final String REMARK_PROPERTY = "remark";
    private Date dateReceived;
    private String name;
    private String purpose;
    private String location;
    private String noble;
    private String landType;
    private String totalArea;
    private String leaseArea;
    private String term;
    private String rent;
    private String surveyFee;
    private String receiptNumber;
    private Date payDate;
    private String ceoDirection;
    private String directedDivision;
    private String remark;
    
    public MinisterLeaseBean() {
        super();
    }
    
    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date value) {
        Date oldValue = this.dateReceived;
        this.dateReceived = value;
        propertySupport.firePropertyChange(DATE_RECEIVED_PROPERTY, oldValue, value);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String value) {
        String oldValue = this.name;
        this.name = value;
        propertySupport.firePropertyChange(NAME_PROPERTY, oldValue, value);
    }
    
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String value) {
        String oldValue = this.purpose;
        this.purpose = value;
        propertySupport.firePropertyChange(PURPOSE_PROPERTY, oldValue, value);
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        String oldValue = this.location;
        this.location = value;
        propertySupport.firePropertyChange(LOCATION_PROPERTY, oldValue, value);
    }
    
    public String getNoble() {
        return noble;
    }

    public void setNoble(String value) {
        String oldValue = this.noble;
        this.noble = value;
        propertySupport.firePropertyChange(NOBLE_PROPERTY, oldValue, value);
    }
    
    public String getLandType() {
        return landType;
    }

    public void setLandType(String value) {
        String oldValue = this.landType;
        this.landType = value;
        propertySupport.firePropertyChange(LAND_TYPE_PROPERTY, oldValue, value);
    }
    
    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String value) {
        String oldValue = this.totalArea;
        this.totalArea = value;
        propertySupport.firePropertyChange(TOTAL_AREA_PROPERTY, oldValue, value);
    }
    
    public String getLeaseArea() {
        return leaseArea;
    }

    public void setLeaseArea(String value) {
        String oldValue = this.leaseArea;
        this.leaseArea = value;
        propertySupport.firePropertyChange(LEASE_AREA_PROPERTY, oldValue, value);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String value) {
        String oldValue = this.term;
        this.term = value;
        propertySupport.firePropertyChange(TERM_PROPERTY, oldValue, value);
    }
    
    public String getRent() {
        return rent;
    }

    public void setRent(String value) {
        String oldValue = this.rent;
        this.rent = value;
        propertySupport.firePropertyChange(RENT_PROPERTY, oldValue, value);
    }
    
    public String getSurveyFee() {
        return surveyFee;
    }

    public void setSurveyFee(String value) {
        String oldValue = this.surveyFee;
        this.surveyFee = value;
        propertySupport.firePropertyChange(SURVEY_FEE_PROPERTY, oldValue, value);
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
    
    public String getCeoDirection() {
        return ceoDirection;
    }

    public void setCeoDirection(String value) {
        String oldValue = this.ceoDirection;
        this.ceoDirection = value;
        propertySupport.firePropertyChange(CEO_DIRECTION_PROPERTY, oldValue, value);
    }
    
    public String getDirectedDivision() {
        return directedDivision;
    }

    public void setDirectedDivision(String value) {
        String oldValue = this.directedDivision;
        this.directedDivision = value;
        propertySupport.firePropertyChange(DIRECTED_DIVISION_PROPERTY, oldValue, value);
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String value) {
        String oldValue = this.remark;
        this.remark = value;
        propertySupport.firePropertyChange(REMARK_PROPERTY, oldValue, value);
    }
    
    public void saveMinisterLease() {
        MinisterLeaseTO ministerLeaseTO = TypeConverters.BeanToTrasferObject(this, MinisterLeaseTO.class);
        ministerLeaseTO = WSManager.getInstance().getCaseManagementService().saveMinisterLease(ministerLeaseTO);
        TypeConverters.TransferObjectToBean(ministerLeaseTO, MinisterLeaseBean.class, this);
    } 
    
    public static MinisterLeaseBean getMinisterLease(String id){
        if(id == null || id.length()<1){
            return null;
        }
        MinisterLeaseTO ministerLeaseTO = WSManager.getInstance().getCaseManagementService().getMinisterLease(id);
        return TypeConverters.TransferObjectToBean(ministerLeaseTO, MinisterLeaseBean.class, null);
    }
    
    public static void removeMinisterLease(String id){
        if(id == null || id.length()<1){
            return;
        } 
        MinisterLeaseTO ministerLeaseTO = WSManager.getInstance().getCaseManagementService().getMinisterLease(id);
        ministerLeaseTO.setEntityAction(EntityAction.DELETE);
        WSManager.getInstance().getCaseManagementService().saveMinisterLease(ministerLeaseTO);
    }
}
