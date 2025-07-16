/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Olabo
 */
public class UniqueBean implements Serializable {

    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

    private String sampleProperty;

    private PropertyChangeSupport propertySupport;

    private String bar_code;
    private String lpo;


    public UniqueBean() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public String getSampleProperty() {
        return sampleProperty;
    }

    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * @return the bar_code
     */
    public String getBar_code() {
        return bar_code;
    }

    /**
     * @param bar_code the bar_code to set
     */
    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    /**
     * @return the lpo
     */
    public String getLpo() {
        return lpo;
    }

    /**
     * @param lpo the lpo to set
     */
    public void setLpo(String lpo) {
        this.lpo = lpo;
    }

}
