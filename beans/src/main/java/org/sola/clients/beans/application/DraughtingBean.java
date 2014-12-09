/*
 * Copyright 2014 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.beans.application;

import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author Admin
 */
public class DraughtingBean extends AbstractBindingBean{
    public static final String DRAUGHTING_ID_PROPERTY = "id";
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
    private int id; 
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

    public DraughtingBean(){
        super();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
