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
package org.sola.clients.beans.cashier;

import java.math.BigDecimal;
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
                if (!"Null".equals(line[6])){
                    CashierImportBean bean = new CashierImportBean();
                    bean.setRentGov(new BigDecimal(cleanCsv(line[0])));    
                    bean.setRentalTax(new BigDecimal(cleanCsv(line[1])));
                    bean.setDeedLease(new BigDecimal(cleanCsv(line[2])));
                    bean.setRegisterFee(new BigDecimal(cleanCsv(line[3])));
                    bean.setTransferFee(new BigDecimal(cleanCsv(line[4])));
                    bean.setSurveyFee(new BigDecimal(cleanCsv(line[5])));
                    bean.setLeaseNumber(line[6]);
                    getCashierImportList().add(bean); 
                }
        }
    }
        
    public String cleanCsv(String text){
       
        if (text != null){
            text = text.replaceAll("[^0-9\\.]", "");
        }
        return text;
    }

    public CashierImportBean getSelectedCashierImportBean() {
        return selectedCashierImportBean;
    }

    public void setSelectedCashierImportBean(CashierImportBean selectedCashierImportBean) {
        this.selectedCashierImportBean = selectedCashierImportBean;
        propertySupport.firePropertyChange(SELECTED_CASHIER_IMPORT, null, selectedCashierImportBean);
    }
     
    public SolaObservableList<CashierImportBean> getCashierImportList() {
        if (cashierImportList == null) {
            cashierImportList = new SolaObservableList<CashierImportBean>();
        }
        return cashierImportList;
    }

}