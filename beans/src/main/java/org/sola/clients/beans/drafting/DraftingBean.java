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
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.casemanagement.DraftingTO;

/**
 *
 * @author Admin
 */
public class DraftingBean extends AbstractBindingBean{
    public static final String ITEM_NUMBER_PROPERTY = "itemNumber";
    public static final String RECEIVE_DATE_PROPERTY = "receiveDate";
    public static final String FIRST_NAME_PROPERTY = "firstName";
    public static final String LAST_NAME_PROPERTY = "lastName";
    public static final String NATURE_OF_SURVEY_PROPERTY = "natureOfSurvey";
    public static final String LOCATION_PROPERTY = "location";
    public static final String TRACE_BY_PROPERTY = "traceBy";
    public static final String TRACE_DATE_PROPERTY = "traceDate";
    public static final String SENT_TO_PROPERTY = "sentTo";
    public static final String SENT_DATE_PROPERTY = "sendDate";
    public static final String RETURN_DATE_PROPERTY = "returnDate";
    public static final String DRAW_DEED_PROPERTY = "drawDeed";
    public static final String DEED_NUMBER_PROPERTY = "deedNumber";
    public static final String PLOTTING_BY_PROPERTY = "plottingBy";
    public static final String PLOTTING_DATE_PROPERTY = "plottingDate";
    public static final String PLAN_NUMBER_PROPERTY = "planNumber";
    public static final String REFERENCE_PROPERTY = "reference";
    private String itemNumber; 
    private Date receiveDate;
    private String firstName;
    private String lastName;
    private String natureOfSurvey;
    private String location;
    private String traceBy;
    private Date traceDate;
    private String sentTo;
    private Date sentDate;
    private Date returnDate;
    private String drawDeed;
    private String deedNumber;
    private String plottingBy;
    private Date plottingDate;
    private String planNumber;
    private String reference;

    public DraftingBean(){
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

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date value) {
        Date oldValue = this.receiveDate;
        this.receiveDate = value;
        propertySupport.firePropertyChange(RECEIVE_DATE_PROPERTY, oldValue, value);
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

    public String getNatureOfSurvey() {
        return natureOfSurvey;
    }

    public void setNatureOfSurvey(String value) {
        String oldValue = this.natureOfSurvey;
        this.natureOfSurvey = value;
        propertySupport.firePropertyChange(NATURE_OF_SURVEY_PROPERTY, oldValue, value);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        String oldValue = this.location;
        this.location = value;
        propertySupport.firePropertyChange(LOCATION_PROPERTY, oldValue, value);
    }

    public String getTraceBy() {
        return traceBy;
    }

    public void setTraceBy(String value) {
        String oldValue = this.traceBy;
        this.traceBy = value;
        propertySupport.firePropertyChange(TRACE_BY_PROPERTY, oldValue, value);
        
    }

    public Date getTraceDate() {
        return traceDate;
    }

    public void setTraceDate(Date value) {
        Date oldValue = this.traceDate;
        this.traceDate = value;
        propertySupport.firePropertyChange(TRACE_DATE_PROPERTY, oldValue, value);
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String value) {
        String oldValue = this.sentTo;
        this.sentTo = value;
        propertySupport.firePropertyChange(SENT_TO_PROPERTY, oldValue, value);
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date value) {
        Date oldValue = this.sentDate;
        this.sentDate = value;
        propertySupport.firePropertyChange(SENT_DATE_PROPERTY, oldValue, value);
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date value) {
        Date oldValue = this.returnDate;
        this.returnDate = value;
        propertySupport.firePropertyChange(RETURN_DATE_PROPERTY, oldValue, value);
    }

    public String getDrawDeed() {
        return drawDeed;
    }

    public void setDrawDeed(String value) {
        String oldValue = this.drawDeed;
        this.drawDeed = value;
        propertySupport.firePropertyChange(DRAW_DEED_PROPERTY, oldValue, value);
    }

    public String getDeedNumber() {
        return deedNumber;
    }

    public void setDeedNumber(String value) {
        String oldValue = this.deedNumber;
        this.deedNumber = value;
        propertySupport.firePropertyChange(DEED_NUMBER_PROPERTY, oldValue, value);
    }

    public String getPlottingBy() {
        return plottingBy;
    }

    public void setPlottingBy(String value) {
        String oldValue = this.plottingBy;
        this.plottingBy = value;
        propertySupport.firePropertyChange(PLOTTING_BY_PROPERTY, oldValue, value);
    }

    public Date getPlottingDate() {
        return plottingDate;
    }

    public void setPlottingDate(Date value) {
        Date oldValue = this.plottingDate;
        this.plottingDate = value;
        propertySupport.firePropertyChange(PLOTTING_DATE_PROPERTY, oldValue, value);
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String value) {
        String oldValue = this.planNumber;
        this.planNumber = value;
        propertySupport.firePropertyChange(PLAN_NUMBER_PROPERTY, oldValue, value);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String value) {
        String oldValue = this.reference;
        this.reference = value;
        propertySupport.firePropertyChange(REFERENCE_PROPERTY, oldValue, value);
    }
    
    public static DraftingBean getDrafting(String draftingId){
        if(draftingId == null || draftingId.length()<1){
            return null;
        }
        DraftingTO draftingTO = WSManager.getInstance().getCaseManagementService().getDrafting(draftingId);
        return TypeConverters.TransferObjectToBean(draftingTO, DraftingBean.class, null);
    }
    
    public static void remove(String draftingId){
        if(draftingId == null || draftingId.length()<1){
            return;
        }
        DraftingTO draftingTO = WSManager.getInstance().getCaseManagementService().getDrafting(draftingId);
        draftingTO.setEntityAction(EntityAction.DELETE);
        WSManager.getInstance().getCaseManagementService().saveDrafting(draftingTO);
    }
    
    public void saveDrafting() {
        DraftingTO drafting = TypeConverters.BeanToTrasferObject(this, DraftingTO.class);
        drafting = WSManager.getInstance().getCaseManagementService().saveDrafting(drafting);
        TypeConverters.TransferObjectToBean(drafting, DraftingBean.class, this);
    }  
    
    public static void removeDrafting(String id){
        DraftingTO draftingTO = WSManager.getInstance().getCaseManagementService().getDrafting(id);
        draftingTO.setEntityAction(EntityAction.DELETE);
        WSManager.getInstance().getCaseManagementService().saveDrafting(draftingTO);
    }
}


