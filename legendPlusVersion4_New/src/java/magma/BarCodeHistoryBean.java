/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magma;
import java.beans.*;
import java.io.Serializable;
import legend.BarCodeServlet;
/**
 *
 * @author Olabo
 */
public class BarCodeHistoryBean implements Serializable {

    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

    private String sampleProperty;

    private PropertyChangeSupport propertySupport;
//BarCodeServlet bcs = new BarCodeServlet();

    private String asset_id;
    private String description;
    private String bar_code;
    private String branch_code;
    private String create_date;
    private String creat_user;
    private String checked;
    private String category;


    public BarCodeHistoryBean() {
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
     * @return the asset_id
     */
    public String getAsset_id() {
        return asset_id;
    }

    /**
     * @param asset_id the asset_id to set
     */
    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the branch_code
     */
    public String getBranch_code() {
        return branch_code;
    }

    /**
     * @param branch_code the branch_code to set
     */
    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    /**
     * @return the create_date
     */
    public String getCreate_date() {
        return create_date;
    }

    /**
     * @param create_date the create_date to set
     */
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    /**
     * @return the creat_user
     */
    public String getCreat_user() {
        return creat_user;
    }

    /**
     * @param creat_user the creat_user to set
     */
    public void setCreat_user(String creat_user) {
        this.creat_user = creat_user;
    }

    /**
     * @return the checked
     */
    public String getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(String checked) {
        this.checked = checked;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }



}
