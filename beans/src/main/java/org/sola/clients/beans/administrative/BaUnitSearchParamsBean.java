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
import org.sola.clients.beans.referencedata.DistrictBean;
import org.sola.clients.beans.referencedata.TownBean;

/**
 * Represents search criteria for searching BA units.
 */
public class BaUnitSearchParamsBean extends AbstractBindingBean {

    public static final String SEARCH_TYPE_ALLOTMENT = "allotment";
    public static final String SEARCH_TYPE_LEASE = "lease";
    public static final String SEARCH_TYPE_SUBLEASE = "sublease";
    public static final String SEARCH_TYPE_ESTATE = "estate";
    public static final String SEARCH_TYPE_TOWN = "town";
    public static final String NAME_FIRST_PART_PROPERTY = "nameFirstPart";
    public static final String NAME_LAST_PART_PROPERTY = "nameLastPart";
    public static final String OWNER_NAME_PROPERTY = "ownerName";
    public static final String SEARCH_TYPE_PROPERTY = "searchType";
    public static final String REGISTRY_BOOK_PROPERTY = "registryBook";
    public static final String REGISTRY_PAGE_REF_PROPERTY = "registryPageRef";
    public static final String PARCEL_NAME_PROPERTY = "parcelName";
    public static final String REGISTERED_DATE_FROM_PROPERTY = "registeredDateFrom";
    public static final String REGISTERED_DATE_TO_PROPERTY = "registeredDateTo";
    public static final String TAX_ALLOTMENT_PROPERTY = "taxAllotment";
    public static final String TOWN_ALLOTMENT_PROPERTY = "townAllotment";
    public static final String OTHER_RIGHTHOLDER_PROPERTY = "otherRightholder";
    public static final String TOWN_PROPERTY = "town";
    public static final String ISLAND_PROPERTY = "island";
    public static final String RRR_REFERENCE = "rrrReference";
    private String nameFirstPart;
    private String nameLastPart;
    private String ownerName;
    // SOLA Tonga Customizations
    private String searchType;
    private String registryBook;
    private String registryPageRef;
    private String parcelName;
    private Date registeredDateFrom;
    private Date registeredDateTo;
    private boolean taxAllotment = true;
    private boolean townAllotment = true;
    private String otherRightholder;
    private TownBean town;
    private DistrictBean island;
    private String rrrReference;

    public BaUnitSearchParamsBean() {
        super();
    }

    public BaUnitSearchParamsBean(String searchType) {
        super();
        this.searchType = searchType;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        String oldValue = this.ownerName;
        this.ownerName = ownerName;
        propertySupport.firePropertyChange(OWNER_NAME_PROPERTY, oldValue, this.ownerName);
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getRegistryBook() {
        return registryBook;
    }

    public void setRegistryBook(String value) {
        String oldValue = this.registryBook;
        this.registryBook = value;
        propertySupport.firePropertyChange(REGISTRY_BOOK_PROPERTY, oldValue, value);
    }

    public String getRegistryPageRef() {
        return registryPageRef;
    }

    public void setRegistryPageRef(String value) {
        String oldValue = this.registryPageRef;
        this.registryPageRef = value;
        propertySupport.firePropertyChange(REGISTRY_PAGE_REF_PROPERTY, oldValue, value);
    }

    public String getParcelName() {
        return parcelName;
    }

    public void setParcelName(String value) {
        String oldValue = this.parcelName;
        this.parcelName = value;
        propertySupport.firePropertyChange(PARCEL_NAME_PROPERTY, oldValue, value);
    }

    public Date getRegisteredDateFrom() {
        return registeredDateFrom;
    }

    public void setRegisteredDateFrom(Date value) {
        Date oldValue = this.registeredDateFrom;
        this.registeredDateFrom = value;
        propertySupport.firePropertyChange(REGISTERED_DATE_FROM_PROPERTY, oldValue, value);
    }

    public Date getRegisteredDateTo() {
        return registeredDateTo;
    }

    public void setRegisteredDateTo(Date value) {
        Date oldValue = this.registeredDateTo;
        this.registeredDateTo = value;
        propertySupport.firePropertyChange(REGISTERED_DATE_TO_PROPERTY, oldValue, value);
    }

    public boolean isTaxAllotment() {
        return taxAllotment;
    }

    public void setTaxAllotment(boolean value) {
        boolean oldValue = this.taxAllotment;
        this.taxAllotment = value;
        propertySupport.firePropertyChange(TAX_ALLOTMENT_PROPERTY, oldValue, value);
    }

    public boolean isTownAllotment() {
        return townAllotment;
    }

    public void setTownAllotment(boolean value) {
        boolean oldValue = this.townAllotment;
        this.townAllotment = value;
        propertySupport.firePropertyChange(TOWN_ALLOTMENT_PROPERTY, oldValue, value);
    }

    public String getOtherRightholder() {
        return otherRightholder;
    }

    public void setOtherRightholder(String value) {
        String oldValue = this.otherRightholder;
        this.otherRightholder = value;
        propertySupport.firePropertyChange(OTHER_RIGHTHOLDER_PROPERTY, oldValue, value);
    }

    public TownBean getTown() {
        return town;
    }

    public void setTown(TownBean town) {
        this.town = town;
        propertySupport.firePropertyChange(TOWN_PROPERTY, null, town);
    }

    public String getTownId() {
        return town == null ? null : town.getCode();
    }

    public void setTownId(String value) {
    }

    public DistrictBean getIsland() {
        return island;
    }

    public void setIsland(DistrictBean island) {
        this.island = island;
        propertySupport.firePropertyChange(ISLAND_PROPERTY, null, island);
    }

    public String getIslandId() {
        return island == null ? null : island.getCode();
    }

    public void setIslandId(String value) {
    }
    
    public String getRrrReference() {
        return rrrReference;
    }

    public void setRrrReference(String value) {
        String oldValue = this.rrrReference;
        this.rrrReference = value;
        propertySupport.firePropertyChange(RRR_REFERENCE, oldValue, value);
    }

    public void clear() {
        this.setNameFirstPart(null);
        this.setNameLastPart(null);
        this.setOwnerName(null);
        this.setRegistryBook(null);
        this.setRegistryPageRef(null);
        this.setParcelName(null);
        this.setRegisteredDateFrom(null);
        this.setRegisteredDateTo(null);
        this.setTaxAllotment(true);
        this.setTownAllotment(true);
        this.setOtherRightholder(null);
        this.setTown(null);
        this.setIsland(null);
        this.setRrrReference(null);
    }
}
