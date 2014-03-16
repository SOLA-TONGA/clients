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
package org.sola.clients.beans.referencedata;

import java.util.ArrayList;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 *
 * @author Andrew
 */
public class EstateListBean extends AbstractBindingListBean {

    public static final String SELECTED_ESTATE_PROPERTY = "selectedEstateBean";
    private SolaCodeList<EstateBean> estateListBean;
    private EstateBean selectedEstateBean;

    public EstateListBean() {
        this(false);
    }

    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public EstateListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }

    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public EstateListBean(boolean createDummy, String... excludedCodes) {
        super();
        estateListBean = new SolaCodeList<EstateBean>(excludedCodes);
        loadList(createDummy);
    }

    /**
     * Loads list of {@link EstateBean}.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(EstateBean.class, estateListBean,
                CacheManager.getEstates(), createDummy);
    }

    /*
     * Returns list of estates filtered by the appropriate criteria
     */
    public ObservableList<EstateBean> getFilteredEstateList() {
        return estateListBean.getFilteredList();
    }

    /*
     * Sets a list of codes that will remain in the filtered list
     */
    public void setAllowedCodes(String... codes) {
        estateListBean.setAllowedCodes(codes);
    }

    /**
     * Filters the list of estates based on the islandId. If the islandId for
     * the estate is null, then the estate is left in the list.
     *
     * @param islandId The identifier for the island to use as the list filter
     */
    public void setIslandFilter(String islandId) {
        if (islandId == null) {
            // Clear the filter
            setAllowedCodes((String[]) null);
            return;
        }
        List<String> estates = new ArrayList<String>();
        for (EstateBean bean : estateListBean) {
            if (bean.getIslandId() == null || bean.getIslandId().matches(".*" + islandId + ".*")) {
                estates.add(bean.getCode());
            }
        }
        setAllowedCodes(estates.toArray(new String[0]));
    }

    public EstateBean getSelectedEstate() {
        return selectedEstateBean;
    }

    public void setSelectedEstate(EstateBean value) {
        selectedEstateBean = value;
        propertySupport.firePropertyChange(SELECTED_ESTATE_PROPERTY, null, value);
    }
}
