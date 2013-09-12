/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
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
package org.sola.clients.beans.referencedata;

import java.util.ArrayList;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 *
 * @author Admin
 */
public class TownListBean extends AbstractBindingListBean{
    public static final String SELECTED_TOWN_PROPERTY = "selectedTownBean";
    private SolaCodeList<TownBean> townListBean;
    private TownBean selectedTownBean;
    
    public TownListBean(){
        this(false);
    }
    
    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public TownListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }
    
    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public TownListBean(boolean createDummy, String... excludedCodes) {
        super();
        townListBean = new SolaCodeList<TownBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /**
     * Loads list of {@link TownBean}.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(TownBean.class, townListBean,
                CacheManager.getTowns(), createDummy);
    }
    
    /*
     * Returns list of towns filtered by the appropriate criteria
     */
    public ObservableList<TownBean> getFilteredTownList() {
        return townListBean.getFilteredList();
    }

    /*
     * Sets a list of codes that will remain in the filtered list
     */
    public void setAllowedCodes(String... codes) {
        townListBean.setAllowedCodes(codes);
    }

    /**
     * Filters the list of towns based on the islandId. If the islandId for
     * the town is null, then the town is left in the list.
     *
     * @param islandId The identifier for the island to use as the list filter
     */
    public void setIslandFilter(String islandId) {
        if (islandId == null) {
            return;
        }
        List<String> towns = new ArrayList<String>();
        for (TownBean bean : townListBean) {
            if (bean.getIslandId() == null || islandId.equals(bean.getIslandId())) {
                towns.add(bean.getCode());
            }
        }
        setAllowedCodes(towns.toArray(new String[0]));
    }

    public TownBean getSelectedTown() {
        return selectedTownBean;
    }

    public void setSelectedTown(TownBean value) {
        selectedTownBean = value;
        propertySupport.firePropertyChange(SELECTED_TOWN_PROPERTY, null, value);
    }
}

