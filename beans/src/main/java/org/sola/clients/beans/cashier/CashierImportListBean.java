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
package org.sola.clients.beans.cashier;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.controls.SolaCodeList;
import org.sola.clients.beans.controls.SolaObservableList;

/**
 *
 * @author Admin
 */
public class CashierImportListBean extends AbstractBindingListBean{
    public static final String SELECTED_CASHIER_IMPORT = "selectedCashierImportBean";
    private CashierImportBean selectedCashierImportBean;
    private SolaObservableList<CashierImportBean> cashierImportList;

    public CashierImportListBean(){
        super();
    }
    
    public void setCashierImport(CashierImportBean selectedCashier) {
        this.selectedCashierImportBean = selectedCashier;
        propertySupport.firePropertyChange(SELECTED_CASHIER_IMPORT, null, selectedCashier);
    }
    
    public SolaObservableList<CashierImportBean> getCashierList() {
        if (cashierImportList == null) {
            cashierImportList = new SolaObservableList<CashierImportBean>();
        }
        return cashierImportList;
    }

}