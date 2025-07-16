/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magma.net.vao;

/**
 *
 * @author Ganiyu
 */
public class WorkOrder {

    private String assetId;
    private String workOrderCode;
    private String vendorCode;
    private String contactTitle;
    private double contactValue;
    private int contactDuration;
    private int workId;
    private int tranId;
    private String paymentTermCode;
    private String signatureOrderCode;
    private String signatureCode;
    private int userid;
    private String prepareDate;
    private double contractBalance;
    private String reqnId;
    private String isJobCompleted;
    private String isSubjectToVat;
    private double vatAmount;
    private double totalAmount;

    public WorkOrder() {
    }

    public WorkOrder(String workOrderCode, String vendorCode, String contactTitle, double contactValue, int contactDuration, String paymentTermCode, String signatureOrderCode, String signatureCode) {
        this.workOrderCode = workOrderCode;
        this.vendorCode = vendorCode;
        this.contactTitle = contactTitle;
        this.contactValue = contactValue;
        this.contactDuration = contactDuration;
        this.paymentTermCode = paymentTermCode;
        this.signatureOrderCode = signatureOrderCode;
        this.signatureCode = signatureCode;
    }

    public WorkOrder(String assetId, String workOrderCode, String contactTitle, double contactValue, int tranId, int userid,String prepareDate) {
        this.assetId = assetId;
        this.workOrderCode = workOrderCode;
        this.contactTitle = contactTitle;
        this.contactValue = contactValue;
        this.tranId = tranId;
        this.userid = userid;
        this.prepareDate = prepareDate;
    }


    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    /**
     * @return the workOrderCode
     */
    public String getWorkOrderCode() {
        return workOrderCode;
    }

    /**
     * @param workOrderCode the workOrderCode to set
     */
    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    /**
     * @return the vendorCode
     */
    public String getVendorCode() {
        return vendorCode;
    }

    /**
     * @param vendorCode the vendorCode to set
     */
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    /**
     * @return the contactTitle
     */
    public String getContactTitle() {
        return contactTitle;
    }

    /**
     * @param contactTitle the contactTitle to set
     */
    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    /**
     * @return the contactValue
     */
    public double getContactValue() {
        return contactValue;
    }

    /**
     * @param contactValue the contactValue to set
     */
    public void setContactValue(double contactValue) {
        this.contactValue = contactValue;
    }

    /**
     * @return the contactDuration
     */
    public int getContactDuration() {
        return contactDuration;
    }

    /**
     * @param contactDuration the contactDuration to set
     */
    public void setContactDuration(int contactDuration) {
        this.contactDuration = contactDuration;
    }

    /**
     * @return the workId
     */
    public int getWorkId() {
        return workId;
    }

    /**
     * @param workId the workId to set
     */
    public void setWorkId(int workId) {
        this.workId = workId;
    }

    /**
     * @return the tranId
     */
    public int getTranId() {
        return tranId;
    }

    /**
     * @param tranId the tranId to set
     */
    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    /**
     * @return the paymentTermCode
     */
    public String getPaymentTermCode() {
        return paymentTermCode;
    }

    /**
     * @param paymentTermCode the paymentTermCode to set
     */
    public void setPaymentTermCode(String paymentTermCode) {
        this.paymentTermCode = paymentTermCode;
    }

    /**
     * @return the signatureOrderCode
     */
    public String getSignatureOrderCode() {
        return signatureOrderCode;
    }

    /**
     * @param signatureOrderCode the signatureOrderCode to set
     */
    public void setSignatureOrderCode(String signatureOrderCode) {
        this.signatureOrderCode = signatureOrderCode;
    }

    /**
     * @return the signatureCode
     */
    public String getSignatureCode() {
        return signatureCode;
    }

    /**
     * @param signatureCode the signatureCode to set
     */
    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }

    /**
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * @return the prepareDate
     */
    public String getPrepareDate() {
        return prepareDate;
    }

    /**
     * @param prepareDate the prepareDate to set
     */
    public void setPrepareDate(String prepareDate) {
        this.prepareDate = prepareDate;
    }

    /**
     * @return the contractBalance
     */
    public double getContractBalance() {
        return contractBalance;
    }

    /**
     * @param contractBalance the contractBalance to set
     */
    public void setContractBalance(double contractBalance) {
        this.contractBalance = contractBalance;
    }

    /**
     * @return the reqnId
     */
    public String getReqnId() {
        return reqnId;
    }

    /**
     * @param reqnId the reqnId to set
     */
    public void setReqnId(String reqnId) {
        this.reqnId = reqnId;
    }

    /**
     * @return the isJobCompleted
     */
    public String getIsJobCompleted() {
        return isJobCompleted;
    }

    /**
     * @param isJobCompleted the isJobCompleted to set
     */
    public void setIsJobCompleted(String isJobCompleted) {
        this.isJobCompleted = isJobCompleted;
    }

    /**
     * @return the isSubjectToVat
     */
    public String getIsSubjectToVat() {
        return isSubjectToVat;
    }

    /**
     * @param isSubjectToVat the isSubjectToVat to set
     */
    public void setIsSubjectToVat(String isSubjectToVat) {
        this.isSubjectToVat = isSubjectToVat;
    }

    /**
     * @return the vatAmount
     */
    public double getVatAmount() {
        return vatAmount;
    }

    /**
     * @param vatAmount the vatAmount to set
     */
    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


}
