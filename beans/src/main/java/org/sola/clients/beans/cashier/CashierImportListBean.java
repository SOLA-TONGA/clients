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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.utils.CsvFileUtility;
import org.sola.common.logging.LogUtility;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.CashierImportTO;

/**
 *
 * @author Admin
 */
public class CashierImportListBean extends AbstractBindingListBean {

    public static final String SELECTED_CASHIER_IMPORT = "selectedCashierImportBean";
    public static final String CASHIER_SAVE_RESULT = "cashierSaveResult";
    private String cashierSaveResult;
    private CashierImportBean selectedCashierImportBean;
    private SolaObservableList<CashierImportBean> cashierImportList;

    public CashierImportListBean() {
        super();
    }

    public void loadCashierCsv(String filePath) {
        int count = 0;
        List<String[]> lines = CsvFileUtility.importFile(filePath);
        try {
            for (String[] line : lines) {
                count++;
                if (count <= 1) {
                    continue;
                }
                if (!"Null".equals(line[6])) {
                    CashierImportBean bean = new CashierImportBean();
                    bean.setRecordId(Integer.parseInt(line[0]));
                    bean.setPaymentDate(new SimpleDateFormat("d/MM/yyyy").parse(line[1]));
                    bean.setPaymentParticulars(line[4]);
                    bean.setPaymentDescription(line[6]);
                    bean.setSurveyFee(new BigDecimal(cleanCsv(line[9])));
                    bean.setRentGov(new BigDecimal(cleanCsv(line[10])));
                    bean.setRentalTax(new BigDecimal(cleanCsv(line[12])));
                    bean.setRegisterFee(new BigDecimal(cleanCsv(line[13])));
                    bean.setTransferFee(new BigDecimal(cleanCsv(line[14])));
                    bean.setDeedLease(new BigDecimal(cleanCsv(line[16])));
                    bean.setReceiptNumber(line[18]);
                    bean.setTotalPayment(new BigDecimal(cleanCsv(line[20])));
                    bean.setLeaseNumber(line[23]);
                    getCashierImportList().add(bean);
                }
            }
        } catch (ParseException e) {
            LogUtility.log("Cannot parse CSV date: ", e);
        }
    }

    public String cleanCsv(String text) {

        if (text != null) {
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

    public String saveCashierImport() {
        List<CashierImportTO> toList = new ArrayList<CashierImportTO>();
        String tempResult;
        TypeConverters.BeanListToTransferObjectList((List) cashierImportList, toList, CashierImportTO.class);
        tempResult = WSManager.getInstance().getAdministrative().saveCashierImport(toList);
        setCashierSaveResult(tempResult);
        return tempResult;
    }
    
    public void loadSaveResult() {
        List<CashierImportTO> toList = new ArrayList<CashierImportTO>();
        TypeConverters.TransferObjectListToBeanList(
                toList, CashierImportListBean.class, (List)cashierImportList);
    }
    
    public String getCashierSaveResult() {
        return cashierSaveResult;
    }

    public void setCashierSaveResult(String value) {
        String old = cashierSaveResult;
        cashierSaveResult = value;
        propertySupport.firePropertyChange(CASHIER_SAVE_RESULT, old, value);
    }
}