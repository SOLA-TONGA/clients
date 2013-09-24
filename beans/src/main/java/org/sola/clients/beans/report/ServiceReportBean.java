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
package org.sola.clients.beans.report;

import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;

/**
 *
 * @author Admin
 */
public class ServiceReportBean {
    private ApplicationBean appBean;
    private ApplicationServiceBean appServiceBean;
    
    public ServiceReportBean(){
        super();
    }

    public ApplicationBean getAppBean() {
        return appBean;
    }

    public void setAppBean(ApplicationBean appBean) {
        this.appBean = appBean;
    }

    public ApplicationServiceBean getAppServiceBean() {
        return appServiceBean;
    }

    public void setAppServiceBean(ApplicationServiceBean appServiceBean) {
        this.appServiceBean = appServiceBean;
    }
}
