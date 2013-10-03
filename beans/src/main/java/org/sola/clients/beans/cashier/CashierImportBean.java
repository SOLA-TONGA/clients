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
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.utils.CsvFileUtility;

/**
 *
 * @author Admin
 */
public class CashierImportBean extends AbstractBindingBean{
    public BigDecimal rentGov;
    public BigDecimal rentalTax;
    public BigDecimal deedLease;
    public String leaseNumber;
    
    public static List<CashierImportBean> loadCashierCsv(String filePath){
        int count = 0;
         List<String[]> lines = CsvFileUtility.importFile(filePath);
         List<CashierImportBean> result = new ArrayList();
         for (String[] line: lines) {
                count++;
                if (count <= 1) {
                    break;
                }
                CashierImportBean bean = new CashierImportBean();
                bean.setRentGov(new BigDecimal(line[0]));
                bean.setDeedLease(new BigDecimal(line[1]));
                bean.setRentalTax(new BigDecimal(line[2]));
                bean.setLeaseNumber(line[3]);
                result.add(bean);
        }
        return result;
    }

    public BigDecimal getRentGov() {
        return rentGov;
    }

    public void setRentGov(BigDecimal rentGov) {
        this.rentGov = rentGov;
    }

    public BigDecimal getRentalTax() {
        return rentalTax;
    }

    public void setRentalTax(BigDecimal rentalTax) {
        this.rentalTax = rentalTax;
    }

    public BigDecimal getDeedLease() {
        return deedLease;
    }

    public void setDeedLease(BigDecimal deedLease) {
        this.deedLease = deedLease;
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        this.leaseNumber = leaseNumber;
    }    
}
