/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Kareem Wasiu Aderemi
 */
public class QuotationRequest {
    private String compCode;
    private String rfqNo;
    private String reqId;
    private String vendorCode;
    private String issueDate;
    private String refNo;
    private String instruct;
    private String instruct1;
    private String instruct2;
    private String returnQuoteTo;
    private String returnQuoteTo1;
    private String returnQuoteTo2;
    private String awardStatus;
    private String Address;
    private String Currency;
    private String Delivery_Date;
    private String Is_Printed;
    private String contact;
    private String phone;
    private String email;



    public QuotationRequest(String compCode, String rfqNo, String reqId, String vendorCode, String issueDate,
            String refNo, String instruct, String instruct1, String instruct2, String returnQuoteTo,
            String returnQuoteTo1, String returnQuoteTo2, String awardStatus,String Address,String Currency,
            String Delivery_Date,String Is_Printed,String contact,String phone,String email) {
        this.compCode = compCode;
        this.rfqNo = rfqNo;
        this.reqId = reqId;
        this.vendorCode = vendorCode;
        this.issueDate = issueDate;
        this.refNo = refNo;
        this.instruct = instruct;
        this.instruct1 = instruct1;
        this.instruct2 = instruct2;
        this.returnQuoteTo = returnQuoteTo;
        this.returnQuoteTo1 = returnQuoteTo1;
        this.returnQuoteTo2 = returnQuoteTo2;
        this.awardStatus = awardStatus;
         this.Address = Address;
          this.Currency = Currency;
           this.Delivery_Date = Delivery_Date;
            this.Is_Printed = Is_Printed;
            this.contact = contact;
            this.phone = phone;
            this.email = email;
    }






    /**
     * @return the compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    /**
     * @return the rfqNo
     */
    public String getRfqNo() {
        return rfqNo;
    }

    /**
     * @param rfqNo the rfqNo to set
     */
    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }

    /**
     * @return the reqId
     */
    public String getReqId() {
        return reqId;
    }

    /**
     * @param reqId the reqId to set
     */
    public void setReqId(String reqId) {
        this.reqId = reqId;
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
     * @return the issueDate
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * @param issueDate the issueDate to set
     */
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * @return the refNo
     */
    public String getRefNo() {
        return refNo;
    }

    /**
     * @param refNo the refNo to set
     */
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    /**
     * @return the instruct
     */
    public String getInstruct() {
        return instruct;
    }

    /**
     * @param instruct the instruct to set
     */
    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    /**
     * @return the instruct1
     */
    public String getInstruct1() {
        return instruct1;
    }

    /**
     * @param instruct1 the instruct1 to set
     */
    public void setInstruct1(String instruct1) {
        this.instruct1 = instruct1;
    }

    /**
     * @return the instruct2
     */
    public String getInstruct2() {
        return instruct2;
    }

    /**
     * @param instruct2 the instruct2 to set
     */
    public void setInstruct2(String instruct2) {
        this.instruct2 = instruct2;
    }

    /**
     * @return the returnQuoteTo
     */
    public String getReturnQuoteTo() {
        return returnQuoteTo;
    }

    /**
     * @param returnQuoteTo the returnQuoteTo to set
     */
    public void setReturnQuoteTo(String returnQuoteTo) {
        this.returnQuoteTo = returnQuoteTo;
    }

    /**
     * @return the returnQuoteTo1
     */
    public String getReturnQuoteTo1() {
        return returnQuoteTo1;
    }

    /**
     * @param returnQuoteTo1 the returnQuoteTo1 to set
     */
    public void setReturnQuoteTo1(String returnQuoteTo1) {
        this.returnQuoteTo1 = returnQuoteTo1;
    }

    /**
     * @return the returnQuoteTo2
     */
    public String getReturnQuoteTo2() {
        return returnQuoteTo2;
    }

    /**
     * @param returnQuoteTo2 the returnQuoteTo2 to set
     */
    public void setReturnQuoteTo2(String returnQuoteTo2) {
        this.returnQuoteTo2 = returnQuoteTo2;
    }

    /**
     * @return the awardStatus
     */
    public String getAwardStatus() {
        return awardStatus;
    }

    /**
     * @param awardStatus the awardStatus to set
     */
    public void setAwardStatus(String awardStatus) {
        this.awardStatus = awardStatus;
    }

    /**
     * @return the Address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * @param Address the Address to set
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * @return the Currency
     */
    public String getCurrency() {
        return Currency;
    }

    /**
     * @return the Delivery_Date
     */
    public String getDelivery_Date() {
        return Delivery_Date;
    }

    /**
     * @return the Is_Printed
     */
    public String getIs_Printed() {
        return Is_Printed;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }



}
