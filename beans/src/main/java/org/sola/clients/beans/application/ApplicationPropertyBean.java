/**
 * ******************************************************************************************
 * Copyright (C) 2013 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.beans.application;

import java.math.BigDecimal;
import java.util.Date;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.BaUnitTypeBean;
import org.sola.clients.beans.referencedata.DistrictBean;
import org.sola.clients.beans.referencedata.EstateBean;
import org.sola.clients.beans.referencedata.LandUseTypeBean;
import org.sola.clients.beans.referencedata.TownBean;
import org.sola.common.NumberUtility;
import org.sola.webservices.transferobjects.casemanagement.ApplicationPropertyTO;

/**
 * Represents application property object. Could be populated from the
 * {@link ApplicationPropertyTO} object.<br /> For more information see data
 * dictionary <b>Application</b> schema.
 */
public class ApplicationPropertyBean extends AbstractIdBean {

    public static final String APPLICATION_ID_PROPERTY = "applicationId";
    public static final String AREA_PROPERTY = "area";
    public static final String NAME_FIRST_PART_PROPERTY = "nameFirstpart";
    public static final String NAME_LAST_PART_PROPERTY = "nameLastpart";
    public static final String TOTAL_VALUE_PROPERTY = "totalValue";
    public static final String IS_VERIFIED_EXISTS_PROPERTY = "verifiedExists";
    public static final String IS_VERIFIED_LOCATIONS_PROPERTY = "verifiedLocation";
    public static final String IS_VERIFIED_APPLICATIONS_PROPERTY = "verifiedHasOwners";
    public static final String BA_UNIT_ID_PROPERTY = "baUnitId";
    public static final String LAND_USE_TYPE_PROPERTY = "landUseType";
    public static final String LAND_USE_CODE_PROPERTY = "landUseCode";
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String LEASE_TERM_PROPERTY = "leaseTerm";
    public static final String AMOUNT_PROPERTY = "amount";
    public static final String REGISTRATION_DATE_PROPERTY = "registrationDate";
    public static final String LESSOR_NAME_PROPERTY = "lessorName";
    public static final String ISLAND_ID_PROPERTY = "islandId";
    public static final String ISLAND_PROPERTY = "island";
    public static final String NOBLE_ESTATE_ID_PROPERTY = "nobleEstateId";
    public static final String NOBLE_ESTATE_PROPERTY = "nobleEstate";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String TOWN_ID_PROPERTY = "townId";
    public static final String TOWN_PROPERTY = "town";
    public static final String LESSEE_NAME_PROPERTY = "lesseeName";
    public static final String SUBLEASE_NUMBER_PROPERTY = "subleaseNumber";
    public static final String SUBLESSEE_NAME_PROPERTY = "sublesseeName";
    public static final String REGISTERED_NAME_PROPERTY = "registeredName";
    public static final String TYPE_CODE_PROPERTY = "typeCode";
    public static final String PROPERTY_TYPE_PROPERTY = "propertyType";
    public static final String PROPERTY_NUMBER_PROPERTY = "propertyNumber";
    public static final String RIGHTHOLDER_PROPERTY = "rightHolder";
    public static final String TOWN_ALLOTMENT_PROPERTY = "townAllotment";
    public static final String TAX_ALLOTMENT_PROPERTY = "taxAllotment";
    public static final String LEASE_BA_UNIT_ID_PROPERTY = "leaseBaUnitId";
    public static final String SUBLEASE_BA_UNIT_ID_PROPERTY = "subleaseBaUnitId";
    private String applicationId;
    private BigDecimal area;
    //@NotEmpty(message = ClientMessage.CHECK_NOTNULL_FIRSTPART, payload = Localized.class)
    private String nameFirstpart;
    // @NotEmpty(message = ClientMessage.CHECK_NOTNULL_FIRSTPART, payload = Localized.class)
    private String nameLastpart;
    private BigDecimal totalValue;
    private String baUnitId;
    private boolean verifiedExists;
    private boolean verifiedLocation;
    private boolean verifiedApplications;
    private LandUseTypeBean landUseType;
    // SOLA Tonga extensions
    private String leaseNumber;
    private BigDecimal leaseTerm;
    private BigDecimal amount;
    private Date registrationDate;
    private String lessorName;
    private DistrictBean island;
    private String nobleEstateId;
    private EstateBean nobleEstate;
    private String description;
    private TownBean town;
    private transient BigDecimal surveyFee;
    private String lesseeName;
    private String leaseBaUnitId;
    private String subleaseNumber;
    private String subleaseBaUnitId;
    private String sublesseeName;
    private String registeredName;
    private BaUnitTypeBean propertyType;
    private transient String propertyNumber;
    private transient String rightHolder;

    public ApplicationPropertyBean() {
        super();
        this.landUseType = new LandUseTypeBean();
        this.island = new DistrictBean();
        this.nobleEstate = new EstateBean();
        this.town = new TownBean();
        this.propertyType = new BaUnitTypeBean();
    }

    public String getLandUseCode() {
        return getLandUseType().getCode();
    }

    public void setLandUseCode(String landUseCode) {
        String oldValue = getLandUseCode();
        setLandUseType(CacheManager.getBeanByCode(
                CacheManager.getLandUseTypes(), landUseCode));
        propertySupport.firePropertyChange(LAND_USE_CODE_PROPERTY, oldValue, landUseCode);
    }

    public LandUseTypeBean getLandUseType() {
        return landUseType == null ? new LandUseTypeBean() : landUseType;
    }

    public void setLandUseType(LandUseTypeBean landUseType) {
        this.landUseType = landUseType;
        propertySupport.firePropertyChange(LAND_USE_TYPE_PROPERTY, null, landUseType);
    }

    public String getApplicationId() {
        return applicationId;
    }

    public boolean isVerifiedApplications() {
        return verifiedApplications;
    }

    public void setVerifiedApplications(boolean val) {
        boolean old = verifiedApplications;
        verifiedApplications = val;
        propertySupport.firePropertyChange(IS_VERIFIED_APPLICATIONS_PROPERTY, old, val);
    }

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String val) {
        String old = baUnitId;
        baUnitId = val;
        propertySupport.firePropertyChange(BA_UNIT_ID_PROPERTY, old, val);
    }

    public boolean isVerifiedExists() {
        return verifiedExists;
    }

    public void setVerifiedExists(boolean val) {
        boolean old = verifiedExists;
        verifiedExists = val;
        propertySupport.firePropertyChange(IS_VERIFIED_EXISTS_PROPERTY, old, val);
    }

    public boolean isVerifiedLocation() {
        return verifiedLocation;
    }

    public void setVerifiedLocation(boolean val) {
        boolean old = verifiedLocation;
        verifiedLocation = val;
        propertySupport.firePropertyChange(IS_VERIFIED_LOCATIONS_PROPERTY, old, val);
    }

    public void setApplicationId(String val) {
        String old = applicationId;
        applicationId = val;
        propertySupport.firePropertyChange(APPLICATION_ID_PROPERTY, old, val);
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal val) {
        BigDecimal old = area;
        area = val;
        propertySupport.firePropertyChange(AREA_PROPERTY, old, val);
    }

    public String getNameFirstpart() {
        return nameFirstpart;
    }

    public void setNameFirstpart(String val) {
        String old = nameFirstpart;
        nameFirstpart = val;
        propertySupport.firePropertyChange(NAME_FIRST_PART_PROPERTY, old, val);
        propertySupport.firePropertyChange(PROPERTY_NUMBER_PROPERTY, old, val);
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String val) {
        String old = nameLastpart;
        nameLastpart = val;
        propertySupport.firePropertyChange(NAME_LAST_PART_PROPERTY, old, val);
        propertySupport.firePropertyChange(PROPERTY_NUMBER_PROPERTY, old, val);
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal val) {
        BigDecimal old = totalValue;
        totalValue = val;
        propertySupport.firePropertyChange(TOTAL_VALUE_PROPERTY, old, val);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String value) {
        String old = leaseNumber;
        leaseNumber = value;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, old, value);
        propertySupport.firePropertyChange(PROPERTY_NUMBER_PROPERTY, old, value);
    }

    public BigDecimal getLeaseTerm() {
        return leaseTerm;
    }

    public void setLeaseTerm(BigDecimal value) {
        BigDecimal old = leaseTerm;
        leaseTerm = value;
        propertySupport.firePropertyChange(LEASE_TERM_PROPERTY, old, value);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal value) {
        BigDecimal old = amount;
        amount = value;
        propertySupport.firePropertyChange(AMOUNT_PROPERTY, old, value);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date value) {
        Date old = registrationDate;
        registrationDate = value;
        propertySupport.firePropertyChange(REGISTRATION_DATE_PROPERTY, old, value);
    }

    public String getLessorName() {
        return lessorName;
    }

    public void setLessorName(String value) {
        String old = lessorName;
        lessorName = value;
        propertySupport.firePropertyChange(LESSOR_NAME_PROPERTY, old, value);
        propertySupport.firePropertyChange(RIGHTHOLDER_PROPERTY, old, value);
    }

    public String getIslandId() {
        return getIsland().getCode();
    }

    public void setIslandId(String value) {
        String old = getIslandId();
        setIsland(CacheManager.getBeanByCode(
                CacheManager.getDistricts(), value));
        propertySupport.firePropertyChange(ISLAND_ID_PROPERTY, old, value);

    }

    public DistrictBean getIsland() {
        return island == null ? new DistrictBean() : island;
    }

    public void setIsland(DistrictBean value) {
        this.island = value;
        propertySupport.firePropertyChange(ISLAND_PROPERTY, null, value);
    }

    public String getNobleEstateId() {
        return getNobleEstate().getCode();
    }

    public void setNobleEstateId(String value) {
        String old = getNobleEstateId();
        setNobleEstate(CacheManager.getBeanByCode(
                CacheManager.getEstates(), value));
        propertySupport.firePropertyChange(NOBLE_ESTATE_ID_PROPERTY, old, value);
    }

    public EstateBean getNobleEstate() {
        return nobleEstate == null ? new EstateBean() : nobleEstate;
    }

    public void setNobleEstate(EstateBean value) {
        this.nobleEstate = value;
        propertySupport.firePropertyChange(NOBLE_ESTATE_PROPERTY, null, value);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        String old = description;
        this.description = value;
        propertySupport.firePropertyChange(DESCRIPTION_PROPERTY, old, value);
    }

    public String getTownId() {
        return getTown().getCode();
    }

    public void setTownId(String value) {
        String old = getTownId();
        setTown(CacheManager.getBeanByCode(
                CacheManager.getTowns(), value));
        propertySupport.firePropertyChange(TOWN_ID_PROPERTY, old, value);
    }

    public TownBean getTown() {
        return town == null ? new TownBean() : town;
    }

    public void setTown(TownBean value) {
        this.town = value;
        propertySupport.firePropertyChange(TOWN_PROPERTY, null, value);
    }

    public String getLesseeName() {
        return lesseeName;
    }

    public void setLesseeName(String value) {
        String old = lesseeName;
        lesseeName = value;
        propertySupport.firePropertyChange(LESSEE_NAME_PROPERTY, old, value);
        propertySupport.firePropertyChange(RIGHTHOLDER_PROPERTY, old, value);
    }

    public String getLeaseBaUnitId() {
        return leaseBaUnitId;
    }

    public void setLeaseBaUnitId(String val) {
        String old = leaseBaUnitId;
        leaseBaUnitId = val;
        propertySupport.firePropertyChange(LEASE_BA_UNIT_ID_PROPERTY, old, val);
    }

    public BigDecimal getSurveyFee() {
        return surveyFee;
    }

    public void setSurveyFee(BigDecimal surveyFee) {
        this.surveyFee = surveyFee;
    }

    public String getSubleaseNumber() {
        return subleaseNumber;
    }

    public void setSubleaseNumber(String value) {
        String old = subleaseNumber;
        subleaseNumber = value;
        propertySupport.firePropertyChange(SUBLEASE_NUMBER_PROPERTY, old, value);
        propertySupport.firePropertyChange(PROPERTY_NUMBER_PROPERTY, old, value);
    }

    public String getSubleaseBaUnitId() {
        return subleaseBaUnitId;
    }

    public void setSubleaseBaUnitId(String val) {
        String old = subleaseBaUnitId;
        subleaseBaUnitId = val;
        propertySupport.firePropertyChange(SUBLEASE_BA_UNIT_ID_PROPERTY, old, val);
    }

    public String getSublesseeName() {
        return sublesseeName;
    }

    public void setSublesseeName(String value) {
        String old = sublesseeName;
        sublesseeName = value;
        propertySupport.firePropertyChange(SUBLESSEE_NAME_PROPERTY, old, value);
        propertySupport.firePropertyChange(RIGHTHOLDER_PROPERTY, old, value);
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String value) {
        String old = registeredName;
        registeredName = value;
        propertySupport.firePropertyChange(REGISTERED_NAME_PROPERTY, old, value);
    }

    public String getTypeCode() {
        return getPropertyType().getCode();
    }

    public void setTypeCode(String value) {
        String old = getTypeCode();
        setPropertyType(CacheManager.getBeanByCode(
                CacheManager.getBaUnitTypes(), value));
        propertySupport.firePropertyChange(TYPE_CODE_PROPERTY, old, value);
    }

    public BaUnitTypeBean getPropertyType() {
        return propertyType == null ? new BaUnitTypeBean() : propertyType;
    }

    public void setPropertyType(BaUnitTypeBean value) {
        this.propertyType = value;
        propertySupport.firePropertyChange(PROPERTY_TYPE_PROPERTY, null, value);
    }

    /**
     * Determines the number to use for the property based on the property type.
     */
    public String getPropertyNumber() {
        String number = null;
        if (BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(getTypeCode())) {
            number = getSubleaseNumber();
        }
        if (BaUnitTypeBean.CODE_LEASED_UNIT.equals(getTypeCode())) {
            number = getLeaseNumber();
        }
        if (BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT.equals(getTypeCode())
                || BaUnitTypeBean.CODE_TAX_UNIT.equals(getTypeCode())) {
            number = getNameFirstpart() + "/" + getNameLastpart();
        }
        return number == null ? "" : number;
    }

    public void setPropertyNumber(String value) {
        // propertyNumber not used!
        propertyNumber = value;
    }

    /**
     * Returns the name of the primary rightholder for the property based on the
     * property type.
     */
    public String getRightHolder() {
        String holder = null;
        if (BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(getTypeCode())) {
            holder = getSublesseeName();
        }
        if (BaUnitTypeBean.CODE_LEASED_UNIT.equals(getTypeCode())) {
            holder = getLesseeName();
        }
        if (BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT.equals(getTypeCode())
                || BaUnitTypeBean.CODE_TAX_UNIT.equals(getTypeCode())) {
            holder = getLessorName();
        }
        return holder == null ? "" : holder;
    }

    public void setRightHolder(String rightHolder) {
        this.rightHolder = rightHolder;
    }

    public boolean isTownAllotment() {
        return BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT.equals(getTypeCode());
    }

    public void setTownAllotment(boolean value) {
        if (value && !isTownAllotment()) {
            setTypeCode(BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT);
            propertySupport.firePropertyChange(TOWN_ALLOTMENT_PROPERTY, false, value);
        }
    }

    public boolean isTaxAllotment() {
        return BaUnitTypeBean.CODE_TAX_UNIT.equals(getTypeCode());
    }

    public void setTaxAllotment(boolean value) {
        if (value && !isTaxAllotment()) {
            setTypeCode(BaUnitTypeBean.CODE_TAX_UNIT);
            propertySupport.firePropertyChange(TAX_ALLOTMENT_PROPERTY, false, value);
        }
    }

    public boolean isLease() {
        return BaUnitTypeBean.CODE_LEASED_UNIT.equals(getTypeCode());
    }

    public boolean isSublease() {
        return BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(getTypeCode());
    }

    public void reset() {
        setAmount(null);
        setArea(null);
        setBaUnitId(null);
        setIslandId(null);
        setLandUseCode(null);
        setLeaseBaUnitId(null);
        //setLeaseNumber(null);
        setLeaseTerm(null);
        setLesseeName(null);
        setLessorName(null);
        //setNameFirstpart(null);
        //setNameLastpart(null);
        setNobleEstateId(null);
        setRegistrationDate(null);
        setTownId(null);
        setVerifiedApplications(false);
        setVerifiedExists(false);
        setVerifiedLocation(false);
        //setSubleaseNumber(null);
        setSubleaseBaUnitId(null);
        setSublesseeName(null);
        setRegisteredName(null);
        //setTypeCode(null);
    }
}
