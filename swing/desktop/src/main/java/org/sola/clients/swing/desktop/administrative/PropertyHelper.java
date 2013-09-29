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
package org.sola.clients.swing.desktop.administrative;

import org.sola.clients.beans.administrative.BaUnitAreaBean;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.BaUnitSummaryBean;
import org.sola.clients.beans.administrative.RelatedBaUnitInfoBean;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.referencedata.BaUnitTypeBean;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.clients.beans.referencedata.StatusConstants;

/**
 * Helper class to manage configuring property records for Tonga
 *
 * @author soladev
 */
public class PropertyHelper {

    public static final String NAME_PART_LEASE = "Lease";

    public static BaUnitBean getBaUnitBeanForService(ApplicationBean appBean,
            String requestType) {
        BaUnitBean result = null;
        if (RequestTypeBean.CODE_REGISTER_LEASE.equals(requestType)) {
            result = prepareNewLease(appBean);
        } else if (appBean != null && appBean.getSelectedProperty() != null) {
            if (appBean.getSelectedProperty().getLeaseBaUnitId() != null) {
                result = BaUnitBean.getBaUnitsById(appBean.getSelectedProperty().getLeaseBaUnitId());
            } else {
                result = BaUnitBean.getBaUnitsById(appBean.getSelectedProperty().getBaUnitId());
            }
        }
        return result;
    }

    public static BaUnitBean prepareNewLease(ApplicationBean appBean) {
        BaUnitBean result = new BaUnitBean();
        result.setTypeCode(BaUnitTypeBean.CODE_LEASED_UNIT);
        result.setStatusCode(StatusConstants.PENDING);
        result.setNameLastpart(NAME_PART_LEASE);
        result.setName("");
        if (appBean.getSelectedProperty() != null) {
            if (appBean.getSelectedProperty().getLeaseNumber() != null
                    && appBean.getSelectedProperty().getLeaseBaUnitId() == null) {
                // Set the lease number if it has been specified. 
                result.setNameFirstpart(appBean.getSelectedProperty().getLeaseNumber());
                result.setName(appBean.getSelectedProperty().getLeaseNumber());
            }

            // Set details of Lease RRR
            RrrBean leaseRrr = new RrrBean();
            leaseRrr.setTypeCode(RrrBean.CODE_LEASE);
            leaseRrr.setPrimary(true);
            leaseRrr.setStatusCode(StatusConstants.PENDING);
            leaseRrr.setAmount(appBean.getSelectedProperty().getAmount());
            leaseRrr.setTerm(appBean.getSelectedProperty().getLeaseTerm());
            // Add the property description as a Notation on the lease
            if (appBean.getSelectedProperty().getDescription() != null) {
                leaseRrr.getNotation().setNotationText(appBean.getSelectedProperty().getDescription());
            }

            result.addRrr(leaseRrr);

            if (appBean.getSelectedProperty().getLeaseArea() != null) {
                // Set Lease Area
                BaUnitAreaBean areaBean = new BaUnitAreaBean();
                areaBean.setTypeCode(BaUnitAreaBean.CODE_OFFICIAL_AREA);
                areaBean.setSize(appBean.getSelectedProperty().getLeaseArea());
                result.setOfficialArea(areaBean);
            }

            // Associate the allotment to the lease
            RelatedBaUnitInfoBean allotment = createRelatedBaUnit(
                    appBean.getSelectedProperty().getBaUnitId(), RelatedBaUnitInfoBean.CODE_ALLOTMENT);
            if (allotment != null) {
                result.getParentBaUnits().addAsNew(allotment);
            }

            // Associate the town to the lease
            RelatedBaUnitInfoBean town = createRelatedBaUnit(
                    appBean.getSelectedProperty().getTownId(), RelatedBaUnitInfoBean.CODE_TOWN);
            if (town != null) {
                result.getParentBaUnits().addAsNew(town);
            }
        }
        return result;
    }

    /**
     * Creates a RelatedBaUnitBean based on the id of the related BA Unit and
     * the relationship code
     *
     * @param relatedBaUnitId Id of BA Unit to associate as either a parent or
     * child BA Unit
     * @param relationCode The relationship code
     * @return The RelatedBaUnitInfoBean if the relatedBaUnitId is valid or null
     */
    public static RelatedBaUnitInfoBean createRelatedBaUnit(String relatedBaUnitId, String relationCode) {
        RelatedBaUnitInfoBean result = null;
        if (relatedBaUnitId != null) {
            BaUnitSummaryBean summary = BaUnitBean.getBaUnitsById(relatedBaUnitId);
            if (summary != null) {
                result = new RelatedBaUnitInfoBean();
                result.setRelationCode(relationCode);
                result.setRelatedBaUnit(summary);
                result.setRelatedBaUnitId(summary.getId());
            }
        }
        return result;
    }
}
