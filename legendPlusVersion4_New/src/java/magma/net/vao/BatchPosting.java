package magma.net.vao;


import java.io.Serializable;

public class BatchPosting implements Serializable {

    private String id;
    private String idgroupId;
    private String branchCode;
    private double costprice;
    private String batchNo;
    private String dateField;
    private String description;
    private String accountNo;
    private String transType;
    private String transCode;
    private String currency;
    private String purposeCode;
    private String maker;
    private String userId;
    private String status;

    public BatchPosting(String id, String idgroupId, String branchCode,double costprice,
                     String batchNo, String dateField, String description,
                     String accountNo, String transType, String transCode,
                     String currency,String purposeCode,String maker,String userId, String status) {

        setId(id);
        setIdgroupId(idgroupId);
        setBranchCode(branchCode);
        setCostprice(costprice);
        setBatchNo(batchNo);
        setDateField(dateField);
        setDescription(description);
        setAccountNo(accountNo);
        setTransType(transType);
        setTransCode(transCode);
        setCurrency(currency);
        setPurposeCode(purposeCode);
        setMaker(maker);
        setUserId(userId);
        setStatus(status);

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdgroupId(String idgroupId) {
        this.idgroupId = idgroupId;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setCostprice(double costprice) {
        this.costprice = costprice;
    }
    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getIdgroupId() {
        return this.idgroupId;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public double getCostprice() {
        return this.costprice;
    }

    public String getbatchNo() {
        return this.batchNo;
    }

    public String getDateField() {
        return this.dateField;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public String getTransType() {
        return this.transType;
    }

    public String getTransCode() {
        return this.transCode;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getPurposeCode() {
        return this.purposeCode;
    }

    public String getMaker() {
        return this.maker;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getStatus() {
        return status;
    }

}