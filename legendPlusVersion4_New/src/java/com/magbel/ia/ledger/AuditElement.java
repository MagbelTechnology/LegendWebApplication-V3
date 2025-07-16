package com.magbel.ia.ledger;

public class AuditElement implements java.io.Serializable {

    /** Holds value of property name. */
    private String name;

    /** Holds value of property value. */
    private String oldValue;

    /** Holds value of property actualTime. */
    private String newValue;

    /** Holds value of property currentValue. */
    private String actionPerformed;

    /** Holds value of property user. */
    private String user;

    /** Holds value of property effectiveDate. */
    private String effectiveDate;

    /**
     * Creates a new instance of AuditElement
     *
     * @param name String
     * @param oldValue String
     * @param newValue String
     * @param actionPerformed String
     * @param user String
     */
    public AuditElement(String name, String oldValue, String newValue,
                        String actionPerformed, String user) {
        setName(name);
        setOldValue(oldValue);
        setNewValue(newValue);
        setActionPerformed(actionPerformed);
        setUser(user);
    }

    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return this.name;
    }

    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Getter for property value.
     * @return Value of property value.
     *
     */
    public String getOldValue() {
        return this.oldValue;
    }

    /**
     * Setter for property value.
     *
     * @param val New value of property value.
     */
    public void setOldValue(String val) {
        this.oldValue = val;
    }

    /** Getter for property value.
     * @return Value of property value.
     *
     */
    public String getNewValue() {
        return this.newValue;
    }

    /** Setter for property value.
     * @param value New value of property value.
     *
     */
    public void setNewValue(String value) {
        this.newValue = value;
    }


    /** Getter for property actualPerformed.
     * @return Value of property actualPerformed.
     *
     */
    public String getActionPerformed() {
        return this.actionPerformed;
    }

    /**
     * Setter for property actualPerformed.
     *
     * @param action New value of property actualPerformed.
     */
    public void setActionPerformed(String action) {
        this.actionPerformed = action;
    }

    /** Getter for property usere.
     * @return Value of property user.
     *
     */
    public String getUser() {
        return this.user;
    }

    /** Setter for property currentValue.
     * @param user New value of property user.
     *
     */
    public void setUser(String user) {
        this.user = user;
    }

    /** Getter for property effectiveDate
     * @return Value of property effectiveDate.
     *
     */
    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    /**
     * Setter for property effectiveDate.
     *
     * @param currentValue New value of property effectiveDate.
     */
    public void setEffectiveDate(String currentValue) {
        this.effectiveDate = currentValue;
    }
}
