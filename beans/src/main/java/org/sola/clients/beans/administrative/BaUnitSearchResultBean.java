/**
 * ******************************************************************************************
 * Copyright (C) 2013 - Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.beans.administrative;

import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.BaUnitTypeBean;
import org.sola.clients.beans.referencedata.RegistrationStatusTypeBean;
import org.sola.clients.beans.referencedata.RrrTypeBean;

/**
 * Represents BA unit search result.
 */
public class BaUnitSearchResultBean extends AbstractBindingBean {

    public static final String ID_PROPERTY = "id";
    public static final String NAME_PROPERTY = "name";
    public static final String NAME_FIRST_PART_PROPERTY = "nameFirstPart";
    public static final String NAME_LAST_PART_PROPERTY = "nameLastPart";
    public static final String STATUS_CODE_PROPERTY = "statusCode";
    public static final String REGISTRATION_STATUS_PROPERTY = "registrationStatus";
    public static final String RIGHTHOLDERS_PROPERTY = "rightholders";
    public static final String ISLAND_NAME_PROPERTY = "islandName";
    public static final String TOWN_NAME_PROPERTY = "townName";
    public static final String REGISTERED_DATE_PROPERTY = "registeredDate";
    public static final String OTHER_RIGHTHOLDERS_PROPERTY = "otherRightholders";
    public static final String PARCEL_NAME_PROPERTY = "parcelName";
    public static final String REGISTRY_BOOK_REF_PROPERTY = "registryBookRef";
    public static final String BA_UNIT_TYPE_PROPERTY = "baUnitType";
    public static final String BA_UNIT_TYPE_CODE_PROPERTY = "baUnitTypeCode";
    public static final String RRR_TYPE_PROPERTY = "rrrType";
    public static final String RRR_TYPE_CODE_PROPERTY = "rrrTypeCode";
    public static final String RRR_REFERENCE_PROPERTY = "rrrReference";
    private String id;
    private String name;
    private String nameFirstPart;
    private String nameLastPart;
    private RegistrationStatusTypeBean registrationStatus;
    private String rightholders;
    // SOLA Tonga extensions
    private String islandName;
    private String townName;
    private Date registeredDate;
    private String otherRightholders;
    private String parcelName;
    private String registryBookRef;
    private BaUnitTypeBean baUnitType;
    private RrrTypeBean rrrType;
    private String rrrReference;
    // Used to enable binding on the BaUnitTypeCode
    private transient String baUnitTypeCode;
    private transient String rrrTypeCode;

    public BaUnitSearchResultBean() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        String oldValue = this.id;
        this.id = id;
        propertySupport.firePropertyChange(ID_PROPERTY, oldValue, this.id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        propertySupport.firePropertyChange(NAME_PROPERTY, oldValue, this.name);
    }

    public String getNameFirstPart() {
        return nameFirstPart;
    }

    public void setNameFirstPart(String nameFirstPart) {
        String oldValue = this.nameFirstPart;
        this.nameFirstPart = nameFirstPart;
        propertySupport.firePropertyChange(NAME_FIRST_PART_PROPERTY, oldValue, this.nameFirstPart);
    }

    public String getNameLastPart() {
        return nameLastPart;
    }

    public void setNameLastPart(String nameLastPart) {
        String oldValue = this.nameLastPart;
        this.nameLastPart = nameLastPart;
        propertySupport.firePropertyChange(NAME_LAST_PART_PROPERTY, oldValue, this.nameLastPart);
    }

    public String getRightholders() {
        return rightholders;
    }

    public void setRightholders(String rightholders) {
        String oldValue = this.rightholders;
        this.rightholders = rightholders;
        propertySupport.firePropertyChange(RIGHTHOLDERS_PROPERTY, oldValue, this.rightholders);
    }

    public String getStatusCode() {
        return getRegistrationStatus().getCode();
    }

    public void setStatusCode(String statusCode) {
        String oldValue = getStatusCode();
        setRegistrationStatus(CacheManager.getBeanByCode(CacheManager.getRegistrationStatusTypes(), statusCode));
        propertySupport.firePropertyChange(STATUS_CODE_PROPERTY, oldValue, statusCode);
    }

    public RegistrationStatusTypeBean getRegistrationStatus() {
        if (registrationStatus == null) {
            registrationStatus = new RegistrationStatusTypeBean();
        }
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatusTypeBean registrationStatus) {
        this.registrationStatus = registrationStatus;
        propertySupport.firePropertyChange(REGISTRATION_STATUS_PROPERTY, null, registrationStatus);
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String value) {
        String oldValue = islandName;
        islandName = value;
        propertySupport.firePropertyChange(ISLAND_NAME_PROPERTY, oldValue, value);
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String value) {
        String oldValue = townName;
        townName = value;
        propertySupport.firePropertyChange(TOWN_NAME_PROPERTY, oldValue, value);
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date value) {
        Date oldValue = registeredDate;
        registeredDate = value;
        propertySupport.firePropertyChange(REGISTERED_DATE_PROPERTY, oldValue, value);
    }

    public String getOtherRightholders() {
        return otherRightholders;
    }

    public void setOtherRightholders(String value) {
        String oldValue = otherRightholders;
        otherRightholders = value;
        propertySupport.firePropertyChange(OTHER_RIGHTHOLDERS_PROPERTY, oldValue, value);
    }

    public String getParcelName() {
        return parcelName;
    }

    public void setParcelName(String value) {
        String oldValue = parcelName;
        parcelName = value;
        propertySupport.firePropertyChange(PARCEL_NAME_PROPERTY, oldValue, value);
    }

    public String getRegistryBookRef() {
        return registryBookRef;
    }

    public void setRegistryBookRef(String value) {
        String oldValue = registryBookRef;
        registryBookRef = value;
        propertySupport.firePropertyChange(REGISTRY_BOOK_REF_PROPERTY, oldValue, value);
    }

    public BaUnitTypeBean getBaUnitType() {
        if (baUnitType == null) {
            baUnitType = new BaUnitTypeBean();
        }
        return baUnitType;
    }

    public void setBaUnitType(BaUnitTypeBean value) {
        this.baUnitType = value;
        propertySupport.firePropertyChange(BA_UNIT_TYPE_PROPERTY, null, baUnitType);
    }

    public String getBaUnitTypeCode() {
        return getBaUnitType().getCode();
    }

    public void setBaUnitTypeCode(String value) {
        String oldValue = getBaUnitTypeCode();
        this.setBaUnitType(CacheManager.getBeanByCode(CacheManager.getBaUnitTypes(), value));
        propertySupport.firePropertyChange(BA_UNIT_TYPE_CODE_PROPERTY, oldValue, value);
    }

    public RrrTypeBean getRrrType() {
        if (rrrType == null) {
            rrrType = new RrrTypeBean();
        }
        return rrrType;
    }

    public void setRrrType(RrrTypeBean value) {
        this.rrrType = value;
        propertySupport.firePropertyChange(RRR_TYPE_PROPERTY, null, rrrType);
    }

    public String getRrrTypeCode() {
        return getRrrType().getCode();
    }

    public void setRrrTypeCode(String value) {
        String oldValue = getRrrTypeCode();
        this.setRrrType(CacheManager.getBeanByCode(CacheManager.getRrrTypes(), value));
        propertySupport.firePropertyChange(RRR_TYPE_CODE_PROPERTY, oldValue, value);
    }

    public boolean isTaxAllotment() {
        return BaUnitTypeBean.CODE_TAX_UNIT.equals(getBaUnitTypeCode());
    }

    public boolean isTownAllotment() {
        return BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT.equals(getBaUnitTypeCode());
    }
    
    public String getRrrReference() {
        return rrrReference;
    }

    public void setRrrReference(String value) {
        String oldValue = rrrReference;
        rrrReference = value;
        propertySupport.firePropertyChange(RRR_REFERENCE_PROPERTY, oldValue, value);
    }
}
