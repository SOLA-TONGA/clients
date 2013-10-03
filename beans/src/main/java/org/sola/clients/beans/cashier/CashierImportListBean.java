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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.utils.CsvFileUtility;

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
    
        public void loadCashierCsv(String filePath){
        int count = 0;
         List<String[]> lines = CsvFileUtility.importFile(filePath);
         for (String[] line: lines) {
                count++;
                if (count <= 1) {
                    continue;
                }
                CashierImportBean bean = new CashierImportBean();
                bean.setRentGov(new BigDecimal(cleanCsv(line[0])));
                bean.setDeedLease(new BigDecimal(cleanCsv(line[1])));
                bean.setRentalTax(new BigDecimal(cleanCsv(line[2])));
                bean.setLeaseNumber(line[3]);
                getCashierImportList().add(bean);
        }
    }
        
    public String cleanCsv(String text){
       
        if (text != null){
            text = text.replaceAll("[^0-9\\.]", "");
        }
        return text;
    }
    
    public void setCashierImport(CashierImportBean selectedCashier) {
        this.selectedCashierImportBean = selectedCashier;
        propertySupport.firePropertyChange(SELECTED_CASHIER_IMPORT, null, selectedCashier);
    }
    
    public SolaObservableList<CashierImportBean> getCashierImportList() {
        if (cashierImportList == null) {
            cashierImportList = new SolaObservableList<CashierImportBean>();
        }
        return cashierImportList;
    }

}