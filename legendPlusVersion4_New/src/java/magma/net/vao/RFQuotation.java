/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Kareem Wasiu Aderemi
 */
public class RFQuotation {

    private String id;
    private String itemType;
    private String itemRequested;
    private String requestingBranch;
    private String no_Of_Items;
    private String requestingDept;
    private String remark;
    private String userId;
    private String ReqnID;
    private String requestingSection;
    private String company_code;
    private String status;
    private String ReqnUserId;
    private String requisitionDate;
    private String vendorCode;


        public RFQuotation(String id,String itemType,String itemRequested,String requestingBranch,String no_Of_Items,
                            String requestingDept,String remark,String userId,String ReqnID,String requestingSection,
                            String company_code,String status,String ReqnUserId,String requisitionDate){



            setId(id);
            setReqnID(ReqnID);
            setItemType(itemType);
            setItemRequested(itemRequested);
            setRequestingBranch(requestingBranch);
            setNo_Of_Items(no_Of_Items) ;
            setRequestingDept(requestingDept);
            setRemark(remark);
            setUserId(userId);
            setRequestingSection(requestingSection);
            setCompany_code(company_code);
            setReqnUserId(ReqnUserId);
            setRequisitionDate(requisitionDate);
            setRequestingDept(requestingDept);
            setStatus(status);

        }
    
        public RFQuotation(String requestingBranch,String itemType,String itemRequested,
                           String ReqnID,String userId,String status,String company_code,String ReqnUserId){



            setReqnID(ReqnID);
            setItemType(itemType);
            setItemRequested(itemRequested);
            setRequestingBranch(requestingBranch);
            setUserId(userId);
            setCompany_code(company_code);
            setReqnUserId(ReqnUserId);
            setStatus(status);

        }

        public RFQuotation(String id,String itemRequested,String no_Of_Items,String remark,
                String userId,String ReqnID,String company_code,String status,String vendorCode){

             setId(id);
            setReqnID(ReqnID);
            setNo_Of_Items(no_Of_Items) ;
            setRemark(remark);
            setUserId(userId);
            setCompany_code(company_code);
            setStatus(status);
            setVendorCode(vendorCode);



        }

        public RFQuotation(String requestingBranch, String userId, String ReqnID, String company_code, String status,String ReqnUserId){

            setReqnID(ReqnID);
            setRequestingBranch(requestingBranch);
            setUserId(userId);
            setCompany_code(company_code);
            setReqnUserId(ReqnUserId);
            setStatus(status);
        }
        
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the itemRequested
     */
    public String getItemRequested() {
        return itemRequested;
    }

    /**
     * @param itemRequested the itemRequested to set
     */
    public void setItemRequested(String itemRequested) {
        this.itemRequested = itemRequested;
    }

    /**
     * @return the requestingBranch
     */
    public String getRequestingBranch() {
        return requestingBranch;
    }

    /**
     * @param requestingBranch the requestingBranch to set
     */
    public void setRequestingBranch(String requestingBranch) {
        this.requestingBranch = requestingBranch;
    }

    /**
     * @return the no_Of_Items
     */
    public String getNo_Of_Items() {
        return no_Of_Items;
    }

    /**
     * @param no_Of_Items the no_Of_Items to set
     */
    public void setNo_Of_Items(String no_Of_Items) {
        this.no_Of_Items = no_Of_Items;
    }

    /**
     * @return the requestingDept
     */
    public String getRequestingDept() {
        return requestingDept;
    }

    /**
     * @param requestingDept the requestingDept to set
     */
    public void setRequestingDept(String requestingDept) {
        this.requestingDept = requestingDept;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the ReqnID
     */
    public String getReqnID() {
        return ReqnID;
    }

    public void setReqnID(String ReqnID) {
        this.ReqnID = ReqnID;
    }

    /**
     * @return the requestingSection
     */
    public String getRequestingSection() {
        return requestingSection;
    }

    /**
     * @param requestingSection the requestingSection to set
     */
    public void setRequestingSection(String requestingSection) {
        this.requestingSection = requestingSection;
    }

    /**
     * @return the company_code
     */
    public String getCompany_code() {
        return company_code;
    }

    /**
     * @param company_code the company_code to set
     */
    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the ReqnUserId
     */
    public String getReqnUserId() {
        return ReqnUserId;
    }

    /**
     * @param ReqnUserId the ReqnUserId to set
     */
    public void setReqnUserId(String ReqnUserId) {
        this.ReqnUserId = ReqnUserId;
    }

    /**
     * @return the requisitionDate
     */
    public String getRequisitionDate() {
        return requisitionDate;
    }

    /**
     * @param requisitionDate the requisitionDate to set
     */
    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
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

}
