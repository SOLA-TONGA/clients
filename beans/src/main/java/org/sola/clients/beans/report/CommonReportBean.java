/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.beans.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import org.sola.common.DateUtility;
import org.sola.common.NumberUtility;

/**
 * Contains formatting methods that are common to all reporting beans.
 *
 * @author soladev
 */
public class CommonReportBean {

    private static final String MEDIUM_DATE_FORMAT = "d MMM yyyy";
    private static final String MEDIUM_DATETIME_FORMAT = "d MMM yyyy h:mm a";
    private static final String MEDIUM_TIME_FORMAT = "h:mm a";
    private static final String MONEY_FORMAT = "T$#,##0.00";
    private DefaultFormatter moneyFormat;

    public CommonReportBean() {
        super();
         // Format for Tongan $
        moneyFormat = new NumberFormatter(new DecimalFormat(MONEY_FORMAT));
        moneyFormat.setValueClass(BigDecimal.class);
    }

    /**
     * Formats an area value to metric and optionally imperial
     *
     * @param area The area to format
     * @param metricOnly Flag to indicate only the metric value should be
     * displayed.
     * @return
     */
    public String formatArea(BigDecimal area, boolean metricOnly) {
        String result = null;
        if (area != null) {
            if (metricOnly) {
                result = NumberUtility.formatAreaMetric(area);
            } else {
                result = NumberUtility.formatAreaMetric(area) + " ("
                        + NumberUtility.formatAreaImperial(area) + ")";
            }
        }
        return result;
    }

    /**
     * Formats a date value using the custom medium date format mask
     */
    public String formatDate(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_DATE_FORMAT);
        }
        return result;
    }
    
        /**
     * Formats a datetime value using the custom medium date format mask
     */
    public String formatDateTime(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_DATETIME_FORMAT);
        }
        return result;
    }

    /**
     * Formats a time value using the custom medium time format mask
     */
    public String formatTime(Date date) {
        String result = "";
        if (date != null) {
            result = DateUtility.simpleFormat(date, MEDIUM_TIME_FORMAT);
        }
        return result;
    }

    /**
     * Formats money values with Tongan currency symbol.
     */
    public String formatMoney(BigDecimal amount) {
        String result = "";
        if (amount != null) {
            try {
                result = moneyFormat.valueToString(amount);
            } catch (ParseException ex) {
            }
        }
        return result;
    }
    
}
