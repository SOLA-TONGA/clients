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
package org.sola.clients.beans.application;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.referencedata.ChecklistGroupBean;
import org.sola.clients.beans.referencedata.ChecklistItemBean;

/**
 *
 * @author Admin
 */
public class ServiceChecklistItemListBean extends AbstractBindingListBean{
    
    public static final String SELECTED_SERVICE_CHECKLIST_ITEM = "selectedServiceChecklistItem";
    public static final String SELECTED_SERVICE_CHECKLIST_ITEM_LIST = "serviceChecklistItemList";
    private SolaObservableList<ServiceChecklistItemBean> serviceChecklistItemList;
    private ServiceChecklistItemBean selectedServiceChecklistItem;
    private String serviceId;;
    
    public ServiceChecklistItemListBean(){
        super();
    }
    
    public ObservableList<ServiceChecklistItemBean> getServiceChecklistItemList() {
        if(serviceChecklistItemList == null){
            serviceChecklistItemList = new SolaObservableList<ServiceChecklistItemBean>();
        }
        return serviceChecklistItemList;
    }

    public ServiceChecklistItemBean getSelectedChecklistItem() {
        return selectedServiceChecklistItem;
    }
    
    public String getServiceId() { 
        return serviceId;
    }
    
    public void setServiceId(String serviceId) { 
        this.serviceId = serviceId; 
    }
    
    public void setSelectedChecklistItemBean(ServiceChecklistItemBean selectedItem) {
        ServiceChecklistItemBean oldValue = this.selectedServiceChecklistItem; 
        this.selectedServiceChecklistItem = selectedItem;
        propertySupport.firePropertyChange(SELECTED_SERVICE_CHECKLIST_ITEM, oldValue, this.selectedServiceChecklistItem);
    }
     
    public void loadList(ChecklistGroupBean checklistGroupBean){
	// Clear the contents of the service checklist List. Because serviceChecklistItemList 
	// is an observable collection, it will fire a change event to inform the table control that
	// the list contents have changed
	serviceChecklistItemList.clear(); 
        if(checklistGroupBean != null && checklistGroupBean.getChecklistItemList().size() > 0){
            // Loop through the list of ChecklistItemBeans on the GroupBean and copy the ChecklistItem data
            // to a ServiceChecklistItemBean, then add the bean to the list. e.g. 
            for (ChecklistItemBean item : checklistGroupBean.getChecklistItemList()){
                ServiceChecklistItemBean bean = new ServiceChecklistItemBean();
                bean.setChecklistItemCode(item.getCode()); 
                bean.setServiceId(serviceId);
                bean.setChecklistItemDisplayValue(item.getDisplayValue());
                bean.setChecklistItemDescription(item.getDescription());
                bean.setResult(false);
                serviceChecklistItemList.add(bean);
            }
            
        }
    }
    
}
