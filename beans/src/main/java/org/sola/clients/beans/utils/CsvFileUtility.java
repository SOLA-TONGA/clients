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
package org.sola.clients.beans.utils;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Admin
 */
public class CsvFileUtility {
    
    public static List<String[]> importFile(String filePath) {
        List<String[]> result = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            result = reader.readAll();
        } catch (IOException ex) {
            MessageUtility.displayMessage(filePath, new Object[]{filePath});
            LogUtility.log("Error loading file " + filePath, ex);
        }
        return result;
    }
}
