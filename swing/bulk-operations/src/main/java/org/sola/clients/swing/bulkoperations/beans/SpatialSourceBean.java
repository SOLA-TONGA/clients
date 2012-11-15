/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.beans;

import java.io.File;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.AbstractCodeBean;
import org.sola.clients.beans.controls.SolaObservableList;

/**
 *
 * @author Elton Manoku
 */
public abstract class  SpatialSourceBean extends AbstractCodeBean {
    
    private String geometryType;
    private int featuresNumber;
    private SolaObservableList<SpatialAttributeBean> attributes =
            new SolaObservableList<SpatialAttributeBean>();
    
    private File sourceFile;
    
    public SolaObservableList<SpatialAttributeBean> getAttributes() {
        return attributes;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public Integer getFeaturesNumber() {
        return featuresNumber;
    }

    public void setFeaturesNumber(int featuresNumber) {
        this.featuresNumber = featuresNumber;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
        //The list of attributes will be populated
        loadAttributes();
    }
    
    /**
     * It loads attributes for a specific source.
     * For each kind of source type, this method should be overridden.
     */
    protected abstract void loadAttributes();
    
}