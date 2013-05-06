/*
 * Copyright 2013 Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.beans.referencedata;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 *
 * @author Admin
 */
public class ChecklistItemListBean extends AbstractBindingBean{
    public static final String SELECTED_CHECKLIST_ITEM = "selectedChecklistItem";
    private SolaCodeList<ChecklistItemBean> checklistItemList;
    private ChecklistItemBean selectedChecklistItem;

    /**
     * Initializes object's instance and populates {@link ObservableList}&lt;
     * {@link LandUseTypeBean} &gt; with values from the cache.
     */
    public ChecklistItemListBean() {
        // Load from cache by default
        checklistItemList = new SolaCodeList<ChecklistItemBean>(CacheManager.GET_CHECKLIST_ITEM);
    }

    public ObservableList<ChecklistItemBean> getChecklistItemList() {
        return checklistItemList.getFilteredList();
    }

    public ChecklistItemBean getSelectedChecklistItem() {
        return selectedChecklistItem;
    }

    public void setSelectedChecklistItem(ChecklistItemBean selectedChecklistItem) {
        this.selectedChecklistItem = selectedChecklistItem;
        propertySupport.firePropertyChange(SELECTED_CHECKLIST_ITEM,
                null, selectedChecklistItem);
    }
}
