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
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JDialog;
import org.sola.clients.beans.administrative.BaUnitAreaBean;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.BaUnitSummaryBean;
import org.sola.clients.beans.administrative.RelatedBaUnitInfoBean;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationPropertyBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.referencedata.BaUnitTypeBean;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.clients.beans.referencedata.StatusConstants;
import org.sola.clients.swing.desktop.application.PropertiesList;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.StringUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Helper class to manage configuring property records for Tonga
 *
 * @author soladev
 */
public class PropertyHelper {

    public static final String NAME_PART_LEASE = "Lease";
    private static ApplicationPropertyBean appProperty;

    /**
     * Obtains the BaUnitBean to use for a service based on the details in the
     * Application Property. If the application property is linked to a lease,
     * then retrieve the lease details. If the application property is not
     * linked to a lease, but is linked to an allotment, use the allotment
     * details. Also checks the type of service as this may impact what property
     * record to return (e.g. if Register Lease, create a new Lease record)
     *
     * @param appBean The ApplicationBean representing the application
     * @param appService The Service to retrieve the BaUnitBean for (where the
     * service has already been started).
     * @param appProperty The property to retrieve the BaUnitBean for.
     */
    public static BaUnitBean getBaUnitBeanForService(ApplicationBean appBean,
            ApplicationServiceBean appService, ContentPanel window) {

        if (appBean == null || appService == null) {
            return null;
        }

        BaUnitBean result = null;
        appProperty = null;
        List<BaUnitBean> baUnitList = BaUnitBean.getBaUnitsByServiceId(appService.getId());
        if (baUnitList != null && baUnitList.size() == 1) {
            // Serivce has previously linked to an BA Unit, so return that for the service
            result = baUnitList.get(0);
        } else {

            // Determine the property to use for this service
            if (appBean.getPropertyList().getFilteredList().size() == 1) {
                appProperty = appBean.getPropertyList().getFilteredList().get(0);
            } else if (appBean.getPropertyList().getFilteredList().size() > 1) {
                // Prompt the user to indicate which property record is required. 
                PropertiesList propertyListForm = new PropertiesList(appBean.getPropertyList());
                propertyListForm.setLocationRelativeTo(window);

                propertyListForm.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(PropertiesList.SELECTED_PROPERTY)
                                && evt.getNewValue() != null) {
                            appProperty = (ApplicationPropertyBean) evt.getNewValue();
                            ((JDialog) evt.getSource()).dispose();
                        }
                    }
                });
                propertyListForm.setVisible(true);
            } else {
                MessageUtility.displayMessage(ClientMessage.APPLICATION_PROPERTY_LIST_EMPTY);
            }

            if (appProperty != null) {
                if (RequestTypeBean.CODE_REGISTER_TOWN_API.equals(appService.getRequestTypeCode())
                        || RequestTypeBean.CODE_REGISTER_TAX_API.equals(appService.getRequestTypeCode())) {
                    result = prepareNewAllotment(appBean, appService.getRequestTypeCode());
                } else if (RequestTypeBean.CODE_REGISTER_LEASE.equals(appService.getRequestTypeCode())) {
                    result = prepareNewLease(appBean);
                } else if (RequestTypeBean.CODE_REGISTER_SUBLEASE.equals(appService.getRequestTypeCode())) {
                    result = prepareNewSublease(appBean);
                } else {
                    if (!appProperty.isVerifiedExists()) {
                        // The property may not have been verified, attempt to verify the
                        // property to obtain the correct baUnitId...
                        ApplicationBean.verifyProperty(appProperty, appBean.getNr(), true);
                    }
                    // Determine whether to apply the service to a sublease, lease or allotment
                    String baUnitId = null;
                    if (appProperty.isTaxAllotment() || appProperty.isTownAllotment()) {
                        baUnitId = appProperty.getBaUnitId();
                    } else if (appProperty.isLease()) {
                        baUnitId = appProperty.getLeaseBaUnitId();
                    } else if (appProperty.isSublease()) {
                        baUnitId = appProperty.getSubleaseBaUnitId();
                    }
                    if (baUnitId != null) {
                        result = BaUnitBean.getBaUnitsById(baUnitId);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Creates a new BaUnitBean representing the new allotment details. Used for
     * the Register Tax Api and Town Api services.
     *
     * @param appBean
     */
    private static BaUnitBean prepareNewAllotment(ApplicationBean appBean, String serviceType) {
        BaUnitBean result = new BaUnitBean();
        if (RequestTypeBean.CODE_REGISTER_TOWN_API.equals(serviceType)) {
            result.setTypeCode(BaUnitTypeBean.CODE_TOWN_ALLOTMENT_UNIT);
        } else {
            result.setTypeCode(BaUnitTypeBean.CODE_TAX_UNIT);
        }
        result.setStatusCode(StatusConstants.PENDING);

        // Set the Deed and Folio for the allotment if specified. 
        result.setNameFirstpart(appProperty.getNameFirstpart());
        result.setNameLastpart(appProperty.getNameLastpart());
        if (!StringUtility.isEmpty(appProperty.getNameFirstpart())
                && !StringUtility.isEmpty(appProperty.getNameLastpart())) {
            result.setName(result.getNameFirstpart() + "/" + result.getNameLastpart());
        }

        result.setLandUseTypeCode(appProperty.getLandUseCode());
        result.setRegisteredName(appProperty.getRegisteredName());

        // Set details of Lease RRR
        RrrBean landHolderRrr = new RrrBean();
        landHolderRrr.setTypeCode(RrrBean.CODE_OWNERSHIP);
        landHolderRrr.setPrimary(true);
        landHolderRrr.setStatusCode(StatusConstants.PENDING);

        // Add the property description as a Notation on the allotment
        if (appProperty.getDescription() != null) {
            landHolderRrr.getNotation().setNotationText(appProperty.getDescription());
        }

        result.addRrr(landHolderRrr);

        if (appProperty.getArea() != null) {
            // Set Lease Area
            BaUnitAreaBean areaBean = new BaUnitAreaBean();
            areaBean.setTypeCode(BaUnitAreaBean.CODE_OFFICIAL_AREA);
            areaBean.setSize(appProperty.getArea());
            result.setOfficialArea(areaBean);
        }

        // Associate the town to the allotment
        RelatedBaUnitInfoBean town = createRelatedBaUnit(
                appProperty.getTownId(), RelatedBaUnitInfoBean.CODE_TOWN);
        if (town != null) {
            result.getParentBaUnits().addAsNew(town);
        }

        // Associate estate to the allotment
        RelatedBaUnitInfoBean estate = createRelatedBaUnit(
                appProperty.getNobleEstateId(), RelatedBaUnitInfoBean.CODE_ESTATE);
        if (town != null) {
            result.getParentBaUnits().addAsNew(estate);
        }
        return result;
    }

    /**
     * Creates a new BaUnitBean representing the new lease details. Used for the
     * Register Lease service.
     *
     * @param appBean
     */
    public static BaUnitBean prepareNewLease(ApplicationBean appBean) {
        BaUnitBean result = new BaUnitBean();
        result.setTypeCode(BaUnitTypeBean.CODE_LEASED_UNIT);
        result.setStatusCode(StatusConstants.PENDING);
        result.setNameLastpart(NAME_PART_LEASE);

        // Set the lease number if it has been specified. 
        result.setNameFirstpart(appProperty.getLeaseNumber());

        if (!StringUtility.isEmpty(appProperty.getLeaseNumber())) {
            result.setName(appProperty.getLeaseNumber());
        }

        result.setLandUseTypeCode(appProperty.getLandUseCode());

        // Set details of Lease RRR
        RrrBean leaseRrr = new RrrBean();
        leaseRrr.setTypeCode(RrrBean.CODE_LEASE);
        leaseRrr.setPrimary(true);
        leaseRrr.setStatusCode(StatusConstants.PENDING);
        leaseRrr.setAmount(appProperty.getAmount());
        leaseRrr.setTerm(appProperty.getLeaseTerm());
        leaseRrr.setRegistryBookReference(appProperty.getLeaseNumber());
        leaseRrr.setOtherRightholderName(appProperty.getLessorName());

        // Add the property description as a Notation on the lease
        if (appProperty.getDescription() != null) {
            leaseRrr.getNotation().setNotationText(appProperty.getDescription());
        }

        result.addRrr(leaseRrr);

        if (appProperty.getArea() != null) {
            // Set Lease Area
            BaUnitAreaBean areaBean = new BaUnitAreaBean();
            areaBean.setTypeCode(BaUnitAreaBean.CODE_OFFICIAL_AREA);
            areaBean.setSize(appProperty.getArea());
            result.setOfficialArea(areaBean);
        }

        // Associate the allotment to the lease
        RelatedBaUnitInfoBean allotment = createRelatedBaUnit(
                appProperty.getBaUnitId(), RelatedBaUnitInfoBean.CODE_ALLOTMENT);
        if (allotment != null) {
            result.getParentBaUnits().addAsNew(allotment);
        }

        // Associate the town to the lease
        RelatedBaUnitInfoBean town = createRelatedBaUnit(
                appProperty.getTownId(), RelatedBaUnitInfoBean.CODE_TOWN);
        if (town != null) {
            result.getParentBaUnits().addAsNew(town);
        }

        // Associate estate to the lease
        RelatedBaUnitInfoBean estate = createRelatedBaUnit(
                appProperty.getNobleEstateId(), RelatedBaUnitInfoBean.CODE_ESTATE);
        if (town != null) {
            result.getParentBaUnits().addAsNew(estate);
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

    /**
     * Creates a new BaUnitBean representing the new sublease details. Used for
     * the Register Sublease service. Note that most of the details for the
     * sublease are left blank for the user to fill in manually as the Property
     * tab on the TongaApplicationPanel does not capture much information for
     * subleases.
     *
     * @param appBean
     */
    public static BaUnitBean prepareNewSublease(ApplicationBean appBean) {
        BaUnitBean result = new BaUnitBean();
        result.setTypeCode(BaUnitTypeBean.CODE_SUBLEASE_UNIT);
        result.setStatusCode(StatusConstants.PENDING);

        // Set the sublease number if it has been specified. 
        if (!StringUtility.isEmpty(appProperty.getSubleaseNumber())) {
            result.setNameFirstpart(appProperty.getSubleaseNumber().split("/")[0]);
            result.setNameLastpart(appProperty.getSubleaseNumber().split("/")[1]);
            result.setName(appProperty.getSubleaseNumber());
        }

        // Set details of Sublease RRR
        RrrBean subleaseRrr = new RrrBean();
        subleaseRrr.setTypeCode(RrrBean.CODE_SUBLEASE);
        subleaseRrr.setPrimary(true);
        subleaseRrr.setStatusCode(StatusConstants.PENDING);
        subleaseRrr.setRegistryBookReference(appProperty.getSubleaseNumber());

        subleaseRrr.setAmount(appProperty.getAmount());
        subleaseRrr.setTerm(appProperty.getLeaseTerm());
        if (appProperty.getLesseeName() != null) {
            subleaseRrr.setOtherRightholderName(appProperty.getLesseeName());
        } else {
            subleaseRrr.setOtherRightholderName(appProperty.getLessorName());
        }

        // Add the property description as a Notation on the lease
        if (appProperty.getDescription() != null) {
            subleaseRrr.getNotation().setNotationText(appProperty.getDescription());
        }

        result.addRrr(subleaseRrr);

        // Associate the lease to the sublease
        RelatedBaUnitInfoBean lease = createRelatedBaUnit(
                appProperty.getLeaseBaUnitId(), RelatedBaUnitInfoBean.CODE_SUBLEASE);
        if (lease != null) {
            result.getParentBaUnits().addAsNew(lease);
        } else {
            // Check if there is an allotment to associate with the sublease instead
            RelatedBaUnitInfoBean allotment = createRelatedBaUnit(
                    appProperty.getBaUnitId(), RelatedBaUnitInfoBean.CODE_SUBLEASE);
            if (allotment != null) {
                result.getParentBaUnits().addAsNew(allotment);
            }
        }

        // Associate the town to the sublease
        RelatedBaUnitInfoBean town = createRelatedBaUnit(
                appProperty.getTownId(), RelatedBaUnitInfoBean.CODE_TOWN);
        if (town != null) {
            result.getParentBaUnits().addAsNew(town);
        }

        // Associate estate to the sublease
        RelatedBaUnitInfoBean estate = createRelatedBaUnit(
                appProperty.getNobleEstateId(), RelatedBaUnitInfoBean.CODE_ESTATE);
        if (town != null) {
            result.getParentBaUnits().addAsNew(estate);
        }
        return result;
    }
}
