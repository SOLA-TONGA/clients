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
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 *
 * @author Admin
 */
public class ChecklistGroupListBean extends AbstractBindingListBean{

    public static final String SELECTED_CHECKLIST_GROUP = "selectedChecklistGroup";
    private SolaCodeList<ChecklistGroupBean> checklistGroupList;
    private ChecklistGroupBean selectedChecklistGroup;

    public ChecklistGroupListBean() {
        // Load from cache by default
        checklistGroupList = new SolaCodeList<ChecklistGroupBean>(CacheManager.GET_CHECKLIST_GROUP);
    }

    public ObservableList<ChecklistGroupBean> getChecklistGroupList() {
        return checklistGroupList.getFilteredList();
    }

    public ChecklistGroupBean getSelectedChecklistGroup() {
        return selectedChecklistGroup;
    }

    public void setSelectedChecklistGroup(ChecklistGroupBean selectedChecklistGroup) {
        this.selectedChecklistGroup = selectedChecklistGroup;
        propertySupport.firePropertyChange(SELECTED_CHECKLIST_GROUP,
                null, selectedChecklistGroup);
    }
    
    public final void loadList(boolean createDummy) {
        loadCodeList(ChecklistGroupBean.class, checklistGroupList, 
                CacheManager.getChecklistGroup(), createDummy);
    }
}
