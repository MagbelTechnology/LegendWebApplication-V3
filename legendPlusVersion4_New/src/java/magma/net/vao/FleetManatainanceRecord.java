package magma.net.vao;

/**
 * <p>Title: FleetManatainanceRecord.java</p>
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
public class FleetManatainanceRecord extends FleetRecord {
    private String dateOfRepair;
    private String technicianType;
    private String technicianName;
    private double milleageBeforeMaintenance;
    private double milleageAfterMaintenance;
    private String details;
    private String componentReplaced;
    private String lastPerformedDate;
    private String nextPerformedDate;
    private String notificationFreq;
    private String firstNotificationDate;
    private String nextNotificationDate;
    private String maintenanceType;
    private String histId;
    private String invoiceNo;
    private double vatAmt;
    private double whtAmt;
    private String projectCode;
    private String suppliedBy;
    private String transDate;
    private String type;
    private double cost;
    private String branchId;
    private String deptId;
    private String sectId;
    private String sbuCode;
    private int ltId;
    private String id;
    private String assetId; 
    private int assetCode;
    private String userId; 
    private String raiseEntry;
    private String reversal;
    private String integrifyId;
    private String superId;
    private String description;
    private String errorMessage;
    private String subjectTOVat;
    private String subjectToWhTax;
    private double vatRate;
    private double whtRate;
    
    public FleetManatainanceRecord(){

    }
    
    public FleetManatainanceRecord(String id, String type, double cost,
                                   String assetCode,
                                   String registrationNo, String dateOfRepair,
                                   String technicianType,
                                   String technicianName,
                                   double milleageBeforeMaintenance,
                                   double milleageAfterMaintenance,
                                   String details, String componentReplaced,
                                   String lastPerformedDate,
                                   String nextPerformedDate,
                                   String maintenanceType, String status,
                                   String histId, String invoiceNo,
                                   double vatAmt, double whtAmt,String projectCode) {

        super(id, type, cost, assetCode, registrationNo, status);
        setDateOfRepair(dateOfRepair);
        setTechnicianType(technicianType);
        setTechnicianName(technicianName);
        setMilleageBeforeMaintenance(milleageBeforeMaintenance);
        setMilleageAfterMaintenance(milleageAfterMaintenance);
        setDetails(details);
        setComponentReplaced(componentReplaced);
        setLastPerformedDate(lastPerformedDate);
        setNextPerformedDate(nextPerformedDate);
        setMaintenanceType(maintenanceType);
        setHistId(histId);
        setInvoiceNo(invoiceNo);
        setVatAmt(vatAmt);
        setWhtAmt(whtAmt);
        setProjectCode(projectCode);
    }
    
    public FleetManatainanceRecord(String id, String type, double cost,
            String assetCode,
            String registrationNo, String dateOfRepair,
            String technicianType,
            String technicianName,
            double milleageBeforeMaintenance,
            double milleageAfterMaintenance,
            String details, String componentReplaced,
            String lastPerformedDate,
            String nextPerformedDate,
            String maintenanceType, String status,
            String histId, String invoiceNo,
            double vatAmt, double whtAmt,String projectCode,double vatRate,double whtRate) {

super(id, type, cost, assetCode, registrationNo, status);
setDateOfRepair(dateOfRepair);
setTechnicianType(technicianType);
setTechnicianName(technicianName);
setMilleageBeforeMaintenance(milleageBeforeMaintenance);
setMilleageAfterMaintenance(milleageAfterMaintenance);
setDetails(details);
setComponentReplaced(componentReplaced);
setLastPerformedDate(lastPerformedDate);
setNextPerformedDate(nextPerformedDate);
setMaintenanceType(maintenanceType);
setHistId(histId);
setInvoiceNo(invoiceNo);
setVatAmt(vatAmt);
setWhtAmt(whtAmt);
setProjectCode(projectCode);
setVatRate(vatRate);
setWhtRate(whtRate);
}
    
    public FleetManatainanceRecord(String id, String type, double cost,
    							   String transDate,
                                   String details, String status,
                                   String histId, String invoiceNo,
                                   double vatAmt, double whtAmt,String suppliedBy,String projectCode,double vatRate,double whtRate) {    	
        super(id, type, cost, status);
        setTransDate(transDate);
        setDetails(details);
        setHistId(histId);
        setInvoiceNo(invoiceNo);
        setVatAmt(vatAmt);
        setWhtAmt(whtAmt);  
        setSuppliedBy(suppliedBy);
        setProjectCode(projectCode);
        setVatRate(vatRate);
        setWhtRate(whtRate);
    }
    
    public FleetManatainanceRecord(String id, String type, double cost,
    							   String transDate,
                                   String details, String status,
                                   String histId, String invoiceNo,
                                   double vatAmt, double whtAmt,String suppliedBy,String projectCode) {    	
        super(id, type, cost, status);
        setTransDate(transDate);
        setDetails(details);
        setHistId(histId);
        setInvoiceNo(invoiceNo);
        setVatAmt(vatAmt);
        setWhtAmt(whtAmt);  
        setSuppliedBy(suppliedBy);
        setProjectCode(projectCode);
        setVatRate(vatRate);
        setWhtRate(whtRate);
    }


    public void setLtId(int ltId) {
        this.ltId = ltId;

    }
    
    public void setId(String id) {
        this.id = id;

    }
    
    public void setAssetId(String assetId) {
        this.assetId = assetId;

    }
    
    public void setAssetCode(int assetCode) {
        this.assetCode = assetCode;

    }
    
    public void setDateOfRepair(String dateOfRepair) {
        this.dateOfRepair = dateOfRepair;

    }

    public void setTechnicianType(String technicianType) {
        this.technicianType = technicianType;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public void setMilleageBeforeMaintenance(double milleageBeforeMaintenance) {
        this.milleageBeforeMaintenance = milleageBeforeMaintenance;
    }

    public void setMilleageAfterMaintenance(double milleageAfterMaintenance) {
        this.milleageAfterMaintenance = milleageAfterMaintenance;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setComponentReplaced(String componentReplaced) {
        this.componentReplaced = componentReplaced;
    }

    public void setLastPerformedDate(String lastPerformedDate) {
        this.lastPerformedDate = lastPerformedDate;
    }

    public void setNextPerformedDate(String nextPerformedDate) {
        this.nextPerformedDate = nextPerformedDate;
    }

    public void setNextNotificationDate(String nextNotificationDate) {
        this.nextNotificationDate = nextNotificationDate;
    }    

    public void setFirstNotificationDate(String firstNotificationDate) {
        this.firstNotificationDate = firstNotificationDate;
    }  

    public void setNotificationFreq(String notificationFreq) {
        this.notificationFreq = notificationFreq;
    }  
        
    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public int getLtId() {
        return ltId;
    }

    public String getDateOfRepair() {
        return dateOfRepair;
    }

    public String getId() {
        return id;
    }

    public String getTechnicianType() {
        return technicianType;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public double getMilleageBeforeMaintenance() {
        return milleageBeforeMaintenance;
    }

    public double getMilleageAfterMaintenance() {
        return milleageAfterMaintenance;
    }

    public String getDetails() {
        return details;
    }

    public String getComponentReplaced() {
        return componentReplaced;
    }

    public String getLastPerformedDate() {
        return lastPerformedDate;
    }

    public String getNextPerformedDate() {
        return nextPerformedDate;
    }

    public String getNextNotificationDate() {
        return nextNotificationDate;
    }    

    public String getFirstNotificationDate() {
        return firstNotificationDate;
    }  

    public String getNotificationFreq() {
        return notificationFreq;
    }  
   
    public String getMaintenanceType() {
        return maintenanceType;
    }

    public String getHistId() {
        return histId;
    }

    public void setHistId(String histId) {
        this.histId = histId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getVatAmt() {
        return vatAmt;
    }

    public void setVatAmt(double vatAmt) {
        this.vatAmt = vatAmt;
    }

    public double getWhtAmt() {
        return whtAmt;
    }

    public void setWhtAmt(double whtAmt) {
        this.whtAmt = whtAmt;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(String suppliedBy) {
        this.suppliedBy = suppliedBy;
    }    

    public String getTransDate() {
        return transDate;
    }
    
    public void setTransDate(String transDate) {
        this.transDate = transDate;

    }
    
    public String getType() {
        return type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    } 
    
    public String getBranchId() {
        return branchId;
    }
    
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getSectId() {
        return sectId;
    }
    
    public void setSectId(String sectId) {
        this.sectId = sectId;
    }
    
    public String getSbuCode() {
        return sbuCode;
    }
    
    public void setSbuCode(String sbuCode) {
        this.sbuCode = sbuCode;
    }

    public String getUserId() {
        return userId;
    }
    
    public int getAsseCode() {
        return assetCode;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;

    }    

    public String getRaiseEntry() {
        return raiseEntry;
    }
    
    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;

    }

    public String getReversal() {
        return reversal;
    }
    
    public void setReversal(String reversal) {
        this.reversal = reversal;
    }  

    public String getIntegrifyId()
    {
        return integrifyId;
    }

    public void setIntegrifyId(String integrifyId)
    {
        this.integrifyId = integrifyId;
    }

    public String getSuperId()
    {
        return superId;
    }

    public void setSuperId(String superId)
    {
        this.superId = superId;
    }    
    
    public String getAsseId() {
        return assetId;
    } 

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }   

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }  

    public String getSubjectTOVat()
    {
        return subjectTOVat;
    }

    public void setSubjectTOVat(String subjectTOVat)
    {
        this.subjectTOVat = subjectTOVat;
    }  

    public String getSubjectToWhTax()
    {
        return subjectToWhTax;
    }

    public void setSubjectToWhTax(String subjectToWhTax)
    {
        this.subjectToWhTax = subjectToWhTax;
    }  

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getWhtRate() {
        return whtRate;
    }

    public void setWhtRate(double whtRate) {
        this.whtRate = whtRate;
    }

        
}
