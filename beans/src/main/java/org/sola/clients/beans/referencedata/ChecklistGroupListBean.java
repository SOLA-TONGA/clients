/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
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
        loadList(true);
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
                CacheManager.getChecklistGroups(), createDummy);
    }
}
