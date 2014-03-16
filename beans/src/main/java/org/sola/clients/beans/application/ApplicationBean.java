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
package org.sola.clients.beans.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.RelatedBaUnitInfoBean;
import org.sola.clients.beans.application.validation.ApplicationCheck;
import org.sola.clients.beans.applicationlog.ApplicationLogBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.*;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.common.Money;
import org.sola.common.StringUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.casemanagement.ApplicationTO;
import org.sola.webservices.transferobjects.casemanagement.ServiceTO;
import org.sola.webservices.transferobjects.search.PropertyVerifierTO;

/**
 * Represents full object of the application in the domain model. Could be
 * populated from the {@link ApplicationTO} object.<br /> For more information
 * see data dictionary <b>Application</b> schema.
 */
@ApplicationCheck
public class ApplicationBean extends ApplicationSummaryBean {

    public static final String APPLICATION_ITEM_NUMBER = "itemNumber";
    public static final String ACTION_CODE_PROPERTY = "actionCode";
    public static final String ACTION_PROPERTY = "action";
    public static final String ACTION_NOTES_PROPERTY = "actionNotes";
    public static final String LOCATION_PROPERTY = "location";
    public static final String SERVICES_FEE_PROPERTY = "servicesFee";
    public static final String TAX_PROPERTY = "tax";
    public static final String TOTAL_AMOUNT_PAID_PROPERTY = "totalAmountPaid";
    public static final String TOTAL_FEE_PROPERTY = "totalFee";
    public static final String RECEIPT_REF_PROPERTY = "receiptRef";
    public static final String SELECTED_SERVICE_PROPERTY = "selectedService";
    public static final String SELECTED_PROPERTY_PROPERTY = "selectedProperty";
    public static final String SELECTED_SOURCE_PROPERTY = "selectedSource";
    public static final String CONTACT_PERSON_PROPERTY = "contactPerson";
    public static final String AGENT_PROPERTY = "agent";
    public static final String ASSIGNEE_ID_PROPERTY = "assigneeId";
    public static final String STATUS_TYPE_PROPERTY = "statusType";
    public static final String APPLICATION_PROPERTY = "application";
    public static final String SELECTED_CADASTRE_OBJECT = "selectedCadastreObject";
    public static final String LODGED_ROLE = "applicant";
    private String itemNumber;
    private ApplicationActionTypeBean actionBean;
    private String actionNotes;
    private SolaList<ApplicationPropertyBean> propertyList;
    private PartyBean contactPerson;
    private byte[] location;
    private BigDecimal servicesFee;
    private BigDecimal tax;
    private BigDecimal totalAmountPaid;
    private BigDecimal totalFee;
    private String receiptRef;
    @Size(min = 1, message = ClientMessage.CHECK_APP_SERVICES_NOT_EMPTY, payload = Localized.class)
    private SolaList<ApplicationServiceBean> serviceList;
    private SolaList<SourceBean> sourceList;
    private SolaObservableList<ApplicationLogBean> appLogList;
    private transient ApplicationServiceBean selectedService;
    private transient ApplicationPropertyBean selectedProperty;
    private transient SourceBean selectedSource;
    private PartySummaryBean agent;
    private String assigneeId;
    private ApplicationStatusTypeBean statusBean;
    private SolaList<CadastreObjectBean> cadastreObjectList;
    private transient CadastreObjectBean selectedCadastreObject;
    private DocumentBean archiveDocument;
    public static final String ARCHIVE_DOCUMENT = "archiveDocument";

    /**
     * Default constructor to create application bean. Initializes the following
     * list of beans which are the parts of the application bean: <br />
     * {@link ApplicationActionTypeBean} <br /> {@link PartySummaryBean}
     * <br /> {@link ApplicationPropertyBean} <br />
     * {@link ApplicationServiceBean} <br /> {@link SourceBean}
     */
    public ApplicationBean() {
        super();
        actionBean = new ApplicationActionTypeBean();
        statusBean = new ApplicationStatusTypeBean();
        propertyList = new SolaList();
        contactPerson = new PartyBean();
        serviceList = new SolaList();
        sourceList = new SolaList();
        appLogList = new SolaObservableList<ApplicationLogBean>();
        //propertyList.addAsNew(new ApplicationPropertyBean());
        cadastreObjectList = new SolaList();
    }

    public boolean canArchive() {
        String appStatus = getStatusCode();
        return StatusConstants.DEAD.equalsIgnoreCase(appStatus)
                || StatusConstants.APPROVED.equalsIgnoreCase(appStatus);
    }

    public boolean canDespatch() {
        return canArchive() || isRequisitioned();
    }

    public boolean canResubmit() {
        return isRequisitioned();
    }

    /**
     * Allow approval if the Application is assigned, has a status of lodged and
     * all of the services are in an finalized state.
     *
     * @return
     */
    public boolean canApprove() {
        if (!isAssigned() || !isLodged()) {
            return false;
        }
        boolean servicesFinalized = true;

        for (ApplicationServiceBean serviceBean : serviceList) {
            if (StatusConstants.LODGED.equals(serviceBean.getStatusCode())
                    || StatusConstants.PENDING.equals(serviceBean.getStatusCode())) {
                servicesFinalized = false;
                break;
            }

        }
        return servicesFinalized;

    }

    public boolean canCancel() {
        return isLodged();
    }

    public boolean canWithdraw() {
        return (isAssigned() && isLodged()) || isRequisitioned();
    }

    public boolean canRequisition() {
        return isAssigned() && isLodged();
    }

    public boolean canLapse() {
        return isRequisitioned();
    }

    public boolean canValidate() {
        String appStatus = getStatusCode();
        return !(isNew() || StatusConstants.DEAD.equalsIgnoreCase(appStatus)
                || StatusConstants.COMPLETED.equalsIgnoreCase(appStatus));
    }

    public boolean isLodged() {
        String appStatus = getStatusCode();
        return StatusConstants.LODGED.equalsIgnoreCase(appStatus);
    }

    public boolean isRequisitioned() {
        return StatusConstants.REQUISITIONED.equalsIgnoreCase(getStatusCode());
    }

    public boolean isAssigned() {
        return getAssigneeId() != null;
    }

    /**
     * Indicates whether editing of application is allowed or not
     */
    public boolean isEditingAllowed() {
        return isNew() || isLodged();
//        boolean result = true;
//        String appStatus = getStatusCode();
//        if (appStatus != null && (appStatus.equals(StatusConstants.APPROVED)
//                || appStatus.equals(StatusConstants.CANCELED)
//                || appStatus.equals(StatusConstants.ARCHIVED)
//                || appStatus.equals(StatusConstants.REJECTED))) {
//            result = false;
//        }
//        return result;
    }

    /**
     * Indicates whether application management is allowed or not
     */
    public boolean isManagementAllowed() {
        String appStatus = getStatusCode();
        boolean result = true;
        if (appStatus == null || this.isNew()) {
            result = false;
        }
        if (!isEditingAllowed() || (appStatus != null
                && appStatus.equals(StatusConstants.UNASSIGNED))
                || this.getAssigneeId() == null || this.getAssigneeId().length() < 1) {
            result = false;
        }
        return result;
    }

    public void loadApplicationLogList() {
        TypeConverters.TransferObjectListToBeanList(WSManager.getInstance().getSearchService().getApplicationLog(getId()),
                ApplicationLogBean.class, (List) appLogList);
    }

    public ApplicationStatusTypeBean getStatus() {
        return statusBean;
    }

    public void setStatus(ApplicationStatusTypeBean statusBean) {
        if (this.statusBean == null) {
            this.statusBean = new ApplicationStatusTypeBean();
        }
        this.setJointRefDataBean(this.statusBean, statusBean, STATUS_TYPE_PROPERTY);
    }

    public String getStatusCode() {
        if (statusBean == null) {
            return null;
        } else {
            return statusBean.getCode();
        }
    }

    /**
     * Sets application status code and retrieves
     * {@link ApplicationStatusTypeBean} from the cache.
     *
     * @param value Application status code.
     */
    public void setStatusCode(String value) {
        setStatus(CacheManager.getBeanByCode(
                CacheManager.getApplicationStatusTypes(), value));
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String value) {
        String old = assigneeId;
        assigneeId = value;
        propertySupport.firePropertyChange(ASSIGNEE_ID_PROPERTY, old, value);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String value) {
        String old = itemNumber;
        itemNumber = value;
        propertySupport.firePropertyChange(APPLICATION_ITEM_NUMBER, old, value);
    }

    /**
     * Returns collection of {@link ApplicationBean} objects. This method is
     * used by Jasper report designer to extract properties of application bean
     * to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        ApplicationBean bean = new ApplicationBean();
        collection.add(bean);
        return collection;
    }

    public PartySummaryBean getAgent() {
//        if(agent==null){
//            agent = new PartySummaryBean();
//        }
        return agent;
    }

    public void setAgent(PartySummaryBean value) {
        agent = value;
        propertySupport.firePropertyChange(AGENT_PROPERTY, null, value);
    }

    public String getActionCode() {
        return actionBean.getCode();
    }

    /**
     * Sets application action code and retrieves
     * {@link ApplicationActionTypeBean} from the cache.
     *
     * @param value Application action code.
     */
    public void setActionCode(String value) {
        String old = actionBean.getCode();
        setAction(CacheManager.getBeanByCode(
                CacheManager.getApplicationActionTypes(), value));
        propertySupport.firePropertyChange(ACTION_CODE_PROPERTY, old, value);
    }

    public ApplicationActionTypeBean getAction() {
        return actionBean;
    }

    public void setAction(ApplicationActionTypeBean actionBean) {
        if (this.actionBean == null) {
            this.actionBean = new ApplicationActionTypeBean();
        }
        this.setJointRefDataBean(this.actionBean, actionBean, ACTION_PROPERTY);
    }

    public String getActionNotes() {
        return actionNotes;
    }

    public void setActionDescription(String value) {
        String old = actionNotes;
        actionNotes = value;
        propertySupport.firePropertyChange(ACTION_NOTES_PROPERTY, old, value);
    }

    public SolaList<ApplicationPropertyBean> getPropertyList() {
        return propertyList;
    }

    @Valid
    public ObservableList<ApplicationPropertyBean> getFilteredPropertyList() {
        return propertyList.getFilteredList();
    }

    public PartyBean getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(PartyBean value) {
        contactPerson = value;
        contactPerson.setEmail(value.getEmail());
        propertySupport.firePropertyChange(CONTACT_PERSON_PROPERTY, null, value);
    }

    public byte[] getLocation() {
        return location;
    }

    public void setLocation(byte[] value) { //NOSONAR
        byte[] old = location;
        location = value; //NOSONAR
        propertySupport.firePropertyChange(LOCATION_PROPERTY, old, value);
    }

    public ApplicationPropertyBean getSelectedProperty() {
        return selectedProperty;
    }

    public void setSelectedProperty(ApplicationPropertyBean value) {
        selectedProperty = value;
        propertySupport.firePropertyChange(SELECTED_PROPERTY_PROPERTY, null, value);
    }

    public ApplicationServiceBean getSelectedService() {
        return selectedService;
    }

    public void setAppLogList(SolaObservableList<ApplicationLogBean> appLogList) {
        this.appLogList = appLogList;
    }

    public void setPropertyList(SolaList<ApplicationPropertyBean> propertyList) {
        this.propertyList = propertyList;
    }

    public void setServiceList(SolaList<ApplicationServiceBean> serviceList) {
        this.serviceList = serviceList;
    }

    public void setSourceList(SolaList<SourceBean> sourceList) {
        this.sourceList = sourceList;
    }

    public void setSelectedService(ApplicationServiceBean value) {
        selectedService = value;
        propertySupport.firePropertyChange(SELECTED_SERVICE_PROPERTY, null, value);
    }

    public SourceBean getSelectedSource() {
        return selectedSource;
    }

    public void setSelectedSource(SourceBean value) {
        selectedSource = value;
        propertySupport.firePropertyChange(SELECTED_SOURCE_PROPERTY, null, value);
    }

    public BigDecimal getServicesFee() {
        return servicesFee;
    }

    public void setServicesFee(BigDecimal value) {
        BigDecimal old = servicesFee;
        servicesFee = value;
        propertySupport.firePropertyChange(SERVICES_FEE_PROPERTY, old, value);
    }

    public SolaList<ApplicationServiceBean> getServiceList() {
        return serviceList;
    }

    public ObservableList<ApplicationServiceBean> getFilteredServiceList() {
        return serviceList.getFilteredList();
    }

    public SolaList<SourceBean> getSourceList() {
        return sourceList;
    }

    @Valid
    public ObservableList<SourceBean> getSourceFilteredList() {
        return sourceList.getFilteredList();
    }

    public ObservableList<ApplicationLogBean> getAppLogList() {
        return appLogList;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal value) {
        BigDecimal old = tax;
        tax = value;
        propertySupport.firePropertyChange(TAX_PROPERTY, old, value);
    }

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(BigDecimal value) {
        BigDecimal old = totalAmountPaid;
        totalAmountPaid = value;
        propertySupport.firePropertyChange(TOTAL_AMOUNT_PAID_PROPERTY, old, value);
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal value) {
        BigDecimal old = totalFee;
        totalFee = value;
        propertySupport.firePropertyChange(TOTAL_FEE_PROPERTY, old, value);
    }

    public String getReceiptRef() {
        return receiptRef;
    }

    public void setReceiptRef(String value) {
        String old = receiptRef;
        this.receiptRef = value;
        propertySupport.firePropertyChange(RECEIPT_REF_PROPERTY, old, value);
    }

    public SolaList<CadastreObjectBean> getCadastreObjectList() {
        return cadastreObjectList;
    }

    public ObservableList<CadastreObjectBean> getCadastreObjectFilteredList() {
        return cadastreObjectList.getFilteredList();
    }

    public void setCadastreObjectList(SolaList<CadastreObjectBean> cadastreObjectList) {
        this.cadastreObjectList = cadastreObjectList;
    }

    /**
     * Removes selected cadastre object from the list of CadastreObjects.
     */
    public void removeSelectedCadastreObject() {
        if (selectedCadastreObject != null && cadastreObjectList != null) {
            cadastreObjectList.safeRemove(selectedCadastreObject, EntityAction.DISASSOCIATE);
        }
    }

    /**
     * Adds new cadastre object in the list of CadastreObjects.
     */
    public void addCadastreObject(CadastreObjectBean cadastreObject) {
        if (getCadastreObjectList() != null && cadastreObject != null
                && cadastreObject.getEntityAction() != EntityAction.DELETE
                && cadastreObject.getEntityAction() != EntityAction.DISASSOCIATE) {

            for (CadastreObjectBean co : getCadastreObjectList()) {
                if (co.getId() != null && cadastreObject.getId() != null && co.getId().equals(cadastreObject.getId())) {
                    if (co.getEntityAction() == EntityAction.DELETE || co.getEntityAction() == EntityAction.DISASSOCIATE) {
                        co.setEntityAction(null);
                    }
                    return;
                }
            }
            getCadastreObjectList().addAsNew(cadastreObject);
        }
    }

    public CadastreObjectBean getSelectedCadastreObject() {
        return selectedCadastreObject;
    }

    public void setSelectedCadastreObject(CadastreObjectBean selectedCadastreObject) {
        this.selectedCadastreObject = selectedCadastreObject;
        propertySupport.firePropertyChange(SELECTED_CADASTRE_OBJECT, null, this.selectedCadastreObject);
    }

    /**
     * Adds new service ({@link ApplicationServiceBean}) into the application
     * services list.
     *
     * @param requestTypeBean Request type (service) from available services
     * list.
     */
    public void addService(RequestTypeBean requestTypeBean) {


        if (requestTypeBean != null && serviceList != null) {
            int order = 0;

//             bug #122 (The same service can be added multiple times)is not a bug,  
//             it was restricted before to duplicate services,
//             but later we found that sometimes it is needed to have duplications (it could be more than 1service with the same name).
//               for (Iterator<ApplicationServiceBean> it = serviceList.iterator(); it.hasNext();) {
//                    ApplicationServiceBean appService = it.next();
//                    System.out.println("appService.getRequestTypeCode() " + appService.getRequestTypeCode());
//
//                    if (requestTypeBean.getCode().equals(appService.getRequestTypeCode())) {
//                        MessageUtility.displayMessage(ClientMessage.APPLICATION_ALREADYSELECTED_SERVICE);
//                        return;
//                    }
//                }

            if (this.isFeePaid()) {
                MessageUtility.displayMessage(ClientMessage.APPLICATION_WARNING_ADDEDSERVICE);
            }
            for (Iterator<ApplicationServiceBean> it = serviceList.iterator(); it.hasNext();) {
                ApplicationServiceBean applicationServiceBean = it.next();
                if (applicationServiceBean.getServiceOrder() > order) {
                    order = applicationServiceBean.getServiceOrder();
                }
            }

            ApplicationServiceBean newService = new ApplicationServiceBean();
            newService.setApplicationId(this.getId());
            newService.setRequestTypeCode(requestTypeBean.getCode());
            newService.setServiceOrder(order + 1);



            serviceList.add(newService);
        }
    }

    /**
     * Removes selected service from the application services list.
     */
    public void removeSelectedService() {
        if (selectedService != null && serviceList != null) {
            serviceList.safeRemove(selectedService, EntityAction.DELETE);
            renumerateServices();
        }
    }

    /**
     * Numerates services order after the changes in the services list.
     */
    private void renumerateServices() {
        int i = 1;
        for (Iterator<ApplicationServiceBean> it = serviceList.getFilteredList().iterator(); it.hasNext();) {
            ApplicationServiceBean applicationServiceBean = it.next();
            applicationServiceBean.setServiceOrder(i);
            i++;
        }
    }

    /**
     * Moves selected service up in the list of services.
     */
    public boolean moveServiceUp() {
        if (serviceList != null && selectedService != null) {
            int idx = serviceList.getFilteredList().indexOf(selectedService);
            Collections.swap(serviceList.getFilteredList(), idx, idx - 1);
            return true;
        }
        return false;
    }

    /**
     * Moves selected service down in the list of services.
     */
    public boolean moveServiceDown() {
        if (serviceList != null && selectedService != null) {
            int idx = serviceList.getFilteredList().indexOf(selectedService);
            Collections.swap(serviceList.getFilteredList(), idx, idx + 1);
            return true;
        }
        return false;
    }

    /**
     * Removes selected document from the list of application documents.
     */
    public void removeSelectedSource() {
        if (selectedSource != null && sourceList != null) {
            sourceList.safeRemove(selectedSource, EntityAction.DISASSOCIATE);
        }
    }

    /**
     * Adds a new ApplicationPropertyBean to the list of properties on the
     * application.
     *
     * @param property
     */
    public void addProperty(ApplicationPropertyBean property) {
        if (property == null) {
            return;
        }
        propertyList.addAsNew(property);
        selectedProperty = property;
    }

    /**
     * Converts a BaUnitBean into an ApplicationPropertyBean and adds it to the
     * list of properties on the application.
     *
     * @param property
     */
    public void addProperty(BaUnitBean property) {
        if (property == null) {
            return;
        }
        ApplicationPropertyBean newProperty = new ApplicationPropertyBean();
        newProperty.setTypeCode(property.getTypeCode());
        newProperty.setVerifiedExists(true);
        newProperty.setLandUseCode(property.getLandUseTypeCode());
        if (property.getOfficialArea() != null) {
            newProperty.setArea(property.getOfficialArea().getSize());
        }
        if (property.hasParentRelationship(RelatedBaUnitInfoBean.CODE_ISLAND)) {
            newProperty.setIslandId(property.getParentRelationship(RelatedBaUnitInfoBean.CODE_ISLAND).getRelatedBaUnitId());
        }
        if (property.hasParentRelationship(RelatedBaUnitInfoBean.CODE_TOWN)) {
            newProperty.setTownId(property.getParentRelationship(RelatedBaUnitInfoBean.CODE_TOWN).getRelatedBaUnitId());
        }
        if (property.hasParentRelationship(RelatedBaUnitInfoBean.CODE_ESTATE)) {
            newProperty.setNobleEstateId(property.getParentRelationship(RelatedBaUnitInfoBean.CODE_ESTATE).getRelatedBaUnitId());
        }
        if (BaUnitTypeBean.CODE_LEASED_UNIT.equals(property.getTypeCode())) {
            // Lease
            newProperty.setLeaseBaUnitId(property.getId());
            newProperty.setLeaseNumber(property.getNameFirstpart());
            if (property.getPrimaryRrr() != null) {
                newProperty.setLeaseTerm(property.getPrimaryRrr().getTerm());
                newProperty.setAmount(property.getPrimaryRrr().getAmount());
                newProperty.setLesseeName(property.getPrimaryRrr().getRightholderNames());
            }

        } else if (BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(property.getTypeCode())) {
            // Sublease
            newProperty.setSubleaseBaUnitId(property.getId());
            newProperty.setSubleaseNumber(property.getName());
            if (property.getPrimaryRrr() != null) {
                newProperty.setLeaseTerm(property.getPrimaryRrr().getTerm());
                newProperty.setAmount(property.getPrimaryRrr().getAmount());
                newProperty.setSublesseeName(property.getPrimaryRrr().getRightholderNames());
            }
        } else {
            // Allotment
            newProperty.setBaUnitId(property.getId());
            newProperty.setNameFirstpart(property.getNameFirstpart());
            newProperty.setNameLastpart(property.getNameLastpart());
            newProperty.setRegisteredName(property.getRegisteredName());
            newProperty.setRegistrationDate(property.getFolioRegDate());
            if (property.getPrimaryRrr() != null) {
                newProperty.setLessorName(property.getPrimaryRrr().getRightholderNames());
            }

        }
        propertyList.addAsNew(newProperty);
        selectedProperty = newProperty;
    }

    /**
     * Removes selected property object from the list of properties.
     */
    public void removeSelectedProperty() {
        if (selectedProperty != null && propertyList != null) {
            propertyList.safeRemove(selectedProperty, EntityAction.DELETE);
        }
    }

    /**
     * Verifies the specified property object. Checks if object exists in the
     * database and on the map. Checks for the list of incomplete applications,
     * related to the selected property object.
     */
    public static boolean verifyProperty(ApplicationPropertyBean property, String appNr, boolean suppressMessage) {
        if (property != null) {
            PropertyVerifierTO verifier = WSManager.getInstance().getSearchService().verifyApplicationProperty(
                    appNr, property.getNameFirstpart(), property.getNameLastpart(),
                    property.getLeaseNumber(), property.getSubleaseNumber(), property.getTypeCode());
            if (verifier != null) {
                // Tonga customization, capture the details retrieved when verifying the lot and lease information
                property.setBaUnitId(verifier.getLotBaUnitId());
                property.setLeaseBaUnitId(verifier.getLeaseBaUnitId());
                property.setSubleaseBaUnitId(verifier.getSubleaseBaUnitId());

                // Determine if the verifier has located a property matching the selected property type. 
                if (property.isTaxAllotment() || property.isTownAllotment()) {
                    property.setVerifiedExists(!StringUtility.isEmpty(verifier.getLotBaUnitId()));
                }
                if (BaUnitTypeBean.CODE_LEASED_UNIT.equals(property.getTypeCode())) {
                    property.setVerifiedExists(!StringUtility.isEmpty(verifier.getLeaseBaUnitId()));
                }
                if (BaUnitTypeBean.CODE_SUBLEASE_UNIT.equals(property.getTypeCode())) {
                    property.setVerifiedExists(!StringUtility.isEmpty(verifier.getSubleaseBaUnitId()));
                }
                if (!StringUtility.isEmpty(verifier.getDeedNumber())) {
                    property.setNameFirstpart(verifier.getDeedNumber());
                }
                if (!StringUtility.isEmpty(verifier.getFolio())) {
                    property.setNameLastpart(verifier.getFolio());
                }
                if (!StringUtility.isEmpty(verifier.getHolderName())) {
                    property.setLessorName(verifier.getHolderName());
                }
                if (!StringUtility.isEmpty(verifier.getLandUseTypeCode())) {
                    property.setLandUseCode(verifier.getLandUseTypeCode());
                }
                if (verifier.getArea() != null) {
                    property.setArea(verifier.getArea());
                }
                if (!StringUtility.isEmpty(verifier.getParcelName())) {
                    property.setRegisteredName(verifier.getParcelName());
                }
                if (verifier.getRegistrationDate() != null) {
                    property.setRegistrationDate(TypeConverters.XMLDateToDate(verifier.getRegistrationDate()));
                }
                if (!StringUtility.isEmpty(verifier.getDistrictId())) {
                    property.setIslandId(verifier.getDistrictId());
                }
                if (!StringUtility.isEmpty(verifier.getTownId())) {
                    property.setTownId(verifier.getTownId());
                }
                if (!StringUtility.isEmpty(verifier.getEstateId())) {
                    property.setNobleEstateId(verifier.getEstateId());
                }
                if (!StringUtility.isEmpty(verifier.getLeaseNumber())) {
                    property.setLeaseNumber(verifier.getLeaseNumber());
                }
                if (verifier.getLeaseRental() != null) {
                    property.setAmount(verifier.getLeaseRental());
                }
                if (!StringUtility.isEmpty(verifier.getLesseeName())) {
                    property.setLesseeName(verifier.getLesseeName());
                }
                if (verifier.getLeaseTerm() != null) {
                    property.setLeaseTerm(verifier.getLeaseTerm());
                }
                if (!StringUtility.isEmpty(verifier.getSubleaseNumber())) {
                    property.setSubleaseNumber(verifier.getSubleaseNumber());
                }
                if (!StringUtility.isEmpty(verifier.getSublesseeName())) {
                    property.setSublesseeName(verifier.getSublesseeName());
                }
                property.setVerifiedApplications(true);

                if (verifier.getApplicationsWhereFound() != null
                        && !verifier.getApplicationsWhereFound().equals("") && !suppressMessage) {
                    MessageUtility.displayMessage(ClientMessage.APPLICATION_PROPERTY_HAS_INCOMPLETE_APPLICATIONS,
                            new Object[]{property.getPropertyNumber(), verifier.getApplicationsWhereFound()});
                }
            } else {
                property.setBaUnitId(null);
                property.setLeaseBaUnitId(null);
                property.setSubleaseBaUnitId(null);
                property.setVerifiedExists(false);
                property.setVerifiedApplications(false);
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates payment fee for selected services, based on application data.
     */
    public boolean calculateFee() {
        ApplicationTO app = TypeConverters.BeanToTrasferObject(this, ApplicationTO.class);
        app = WSManager.getInstance().getCaseManagementService().calculateFee(app);
        TypeConverters.TransferObjectToBean(app, ApplicationBean.class, this);

        return true;
    }

    /**
     * Validates application against business rules
     */
    public ObservableList<ValidationResultBean> validate() {
        ObservableList<ValidationResultBean> validationResults =
                ObservableCollections.observableList(
                TypeConverters.TransferObjectListToBeanList(WSManager.getInstance().getCaseManagementService().applicationActionValidate(
                this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null));
        // Validation updates the Application, so need to reload to get the
        // lastest rowversion, etc. 
        this.reload();


        ObservableList<ValidationResultBean> validationSorted1List = ObservableCollections.observableList(new ArrayList<ValidationResultBean>());
        ObservableList<ValidationResultBean> validationSorted2List = ObservableCollections.observableList(new ArrayList<ValidationResultBean>());
        ObservableList<ValidationResultBean> validationSorted3List = ObservableCollections.observableList(new ArrayList<ValidationResultBean>());
        ObservableList<ValidationResultBean> validationSorted4List = ObservableCollections.observableList(new ArrayList<ValidationResultBean>());
        ObservableList<ValidationResultBean> validationSortedList = ObservableCollections.observableList(new ArrayList<ValidationResultBean>());


        for (ValidationResultBean resultBean : validationResults) {
            if ((resultBean.getSeverity().contains("medium"))) {
                validationSorted1List.add(0, resultBean);
            } else {
                validationSorted1List.add(resultBean);
            }
        }
        validationSorted2List = validationSorted1List;

        for (ValidationResultBean resultBean : validationSorted2List) {
            if ((resultBean.getSeverity().contains("warning"))) {
                validationSorted3List.add(0, resultBean);
            } else {
                validationSorted3List.add(resultBean);
            }
        }

        validationSorted4List = validationSorted3List;

        for (ValidationResultBean resultBean : validationSorted4List) {
            if (resultBean.isSuccessful()) {
                validationSortedList.add(resultBean);
            } else {
                validationSortedList.add(0, resultBean);
            }
        }
        validationResults = validationSortedList;

        return validationResults;
    }

    /**
     * Approves application
     */
    public List<ValidationResultBean> approve() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionApprove(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Rejects application
     */
    public List<ValidationResultBean> reject() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionCancel(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Withdraws application
     */
    public List<ValidationResultBean> withdraw() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionWithdraw(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Requisitions application
     */
    public List<ValidationResultBean> requisition() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionRequisition(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Archives application
     */
    public List<ValidationResultBean> archive() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionArchive(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Despatches application
     */
    public List<ValidationResultBean> despatch() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionDespatch(this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Lapses application
     */
    public List<ValidationResultBean> lapse() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionLapse(
                this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Lapses application
     */
    public List<ValidationResultBean> resubmit() {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().applicationActionResubmit(
                this.getId(), this.getRowVersion()),
                ValidationResultBean.class, null);
        this.reload();
        return result;
    }

    /**
     * Returns service by id.
     */
    public ApplicationServiceBean getServiceById(String serviceId) {
        if (getServiceList() != null && serviceId != null) {
            for (ApplicationServiceBean service : getServiceList()) {
                if (service.getId().equals(serviceId)) {
                    return service;
                }
            }
        }
        return null;
    }

    /**
     * Assigns application to the user.
     *
     * @param userId ID of the user.
     */
    public boolean assignUser(String userId) {
        if (ApplicationBean.assignUser(this, userId)) {
            this.reload();
            return true;
        }
        return false;
    }

    /**
     * Assigns or unassigns application to the user. If userId is null,
     * application will be unassigned
     *
     * @param userId ID of the user.
     * @param app Application to assign/unassign
     */
    public static boolean assignUser(ApplicationSummaryBean app, String userId) {
        if (userId == null) {
            WSManager.getInstance().getCaseManagementService().applicationActionUnassign(
                    app.getId(), app.getRowVersion());
        } else {
            WSManager.getInstance().getCaseManagementService().applicationActionAssign(
                    app.getId(), userId, app.getRowVersion());

        }
        return true;
    }

    /**
     * set the contact person's role to applicant
     *
     */
    public boolean setApplicantRole() {
        PartyRoleTypeBean partyRoleType = new PartyRoleTypeBean();
        partyRoleType.setCode(LODGED_ROLE);
        if (!contactPerson.checkRoleExists(partyRoleType)) {
            contactPerson.addRole(partyRoleType);
        }
        return true;
    }

    /**
     *
     * /**
     * Creates new application in the database.
     *
     * @throws Exception
     */
    public boolean lodgeApplication() {
        setApplicantRole();
        ApplicationTO app = TypeConverters.BeanToTrasferObject(this, ApplicationTO.class);
        app = WSManager.getInstance().getCaseManagementService().createApplication(app);
        TypeConverters.TransferObjectToBean(app, ApplicationBean.class, this);
        propertySupport.firePropertyChange(APPLICATION_PROPERTY, null, this);
        return true;
    }

    /**
     * Saves application into the database.
     *
     * @throws Exception
     */
    public boolean saveApplication() {
        setApplicantRole();
        ApplicationTO app = TypeConverters.BeanToTrasferObject(this, ApplicationTO.class);
        app = WSManager.getInstance().getCaseManagementService().saveApplication(app);
        if (app.getServiceList() != null) {
            // Sort the services list so that they display in the correct order on the
            // Services tab of the ApplicationDetails form. 
            Collections.sort(app.getServiceList(), new Comparator<ServiceTO>() {
                @Override
                public int compare(ServiceTO s1, ServiceTO s2) {
                    if (s1.getServiceOrder() == s2.getServiceOrder()) {
                        return 0;
                    } else if (s1.getServiceOrder() > s2.getServiceOrder()) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
        TypeConverters.TransferObjectToBean(app, ApplicationBean.class, this);
        propertySupport.firePropertyChange(APPLICATION_PROPERTY, null, this);
        return true;
    }

    /**
     * Reloads application from the database.
     */
    public void reload() {
        ApplicationTO app = WSManager.getInstance().getCaseManagementService().getApplication(this.getId());
        TypeConverters.TransferObjectToBean(app, ApplicationBean.class, this);
    }

    /**
     * Returns {@link ApplicationBean} by the given transaction ID.
     */
    public static ApplicationBean getApplicationByTransactionId(String transactionId) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getCaseManagementService().getApplicationByTransactionId(transactionId),
                ApplicationBean.class, null);
    }

    public DocumentBean getArchiveDocument() {
        return archiveDocument;
    }

    public void setArchiveDocument(DocumentBean archiveDocument) {
        this.archiveDocument = archiveDocument;
        propertySupport.firePropertyChange(ARCHIVE_DOCUMENT, null, archiveDocument);
    }

    /**
     * Calculates the total fee for a service including tax
     *
     * @param requestType The service to determine the fee for
     * @return The fee for the service or null if the service was not on the
     * application or the fee for the service was 0
     */
    public BigDecimal getTotalFeeForService(String requestType) {
        BigDecimal result = null;
        for (ApplicationServiceBean bean : getServiceList()) {
            if (requestType.equals(bean.getRequestTypeCode())) {
                if (this.getServicesFee() != null
                        && BigDecimal.ZERO.compareTo(this.getServicesFee()) != 0) {
                    Money feeAmt = new Money(bean.getAreaFee().add(bean.getBaseFee())
                            .add(bean.getValueFee()));
                    // Use Rounding Style on the divide to avoid a repeating numeric overflow. 
                    BigDecimal taxPercent = this.getTax().divide(this.getServicesFee(), feeAmt.getRoundingStyle());
                    Money taxAmt = feeAmt.times(taxPercent);
                    result = feeAmt.plus(taxAmt).getAmount();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Tongan Customization. Determines if the application has the specified
     * service added to it.
     *
     * @param serviceType
     * @return true if the serviceType has been added to the application, false
     * otherwise.
     */
    public boolean hasService(String serviceType) {
        boolean result = false;
        if (serviceType != null) {
            for (ApplicationServiceBean bean : getServiceList()) {
                if (serviceType.equals(bean.getRequestTypeCode())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
