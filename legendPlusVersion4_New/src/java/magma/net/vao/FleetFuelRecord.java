package magma.net.vao;

//VOLUME,FUEL_TYPE,INVOCE,EFFECTIVE_DATE,ENTRY_DATE
/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class FleetFuelRecord extends FleetRecord {

    private double volume;
    private String fuelType;
    private String invoice;
    private String effectiveDate;
    private String entryDate;
    private double unitPrice;
    private String projectCode;
    private String vendorId;

    public FleetFuelRecord(String id, String type, double cost,
                           String assetCode, String registrationNo,
                           double volume, String fuelType, String invoice,
                           String effectiveDate, String entryDate,
                           String status, double unitPrice) {
        super(id, type, cost, assetCode, registrationNo, status);
        setVolume(volume);
        setFuelType(fuelType);
        setInvoice(invoice);
        setEffectiveDate(effectiveDate);
        setEntryDate(entryDate);
        setUnitPrice(unitPrice);
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public double getVolume() {
        return volume;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }    

    public String getVendorId() {
        return vendorId;
    }
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }    
    
}
