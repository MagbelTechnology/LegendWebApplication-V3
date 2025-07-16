/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Developers
 */
public class RFQResponseDetail {
private String compCode;
    private String reqnID;
    private String vendorCode;
    private String delDate;
    private String desc;
    private String qty;
    private String unitPrice;
    private String itemNo;
    private String costPrice;
    private String award;

    public RFQResponseDetail(){}

     public RFQResponseDetail( String compCode,
                        String reqnID,
                        String vendorCode,
                        String delDate,
                        String desc,
                        String qty,
                        String unitPrice,
                        String itemNo,
                        String costPrice,
                        String award){
         
 this.compCode =compCode;  
this.reqnID =reqnID;  
this.vendorCode = vendorCode;  
this.delDate = delDate; 
this.desc = desc; 
this.qty = qty; 
this.unitPrice = unitPrice; 
this.itemNo = itemNo;  
this.costPrice = costPrice;  
this.award = award;



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
     * @return the reqnID
     */
    public String getReqnID() {
        return reqnID;
    }

    /**
     * @param reqnID the reqnID to set
     */
    public void setReqnID(String reqnID) {
        this.reqnID = reqnID;
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
     * @return the delDate
     */
    public String getDelDate() {
        return delDate;
    }

    /**
     * @param delDate the delDate to set
     */
    public void setDelDate(String delDate) {
        this.delDate = delDate;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the unitPrice
     */
    public String getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the itemNo
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * @param itemNo the itemNo to set
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * @return the costPrice
     */
    public String getCostPrice() {
        return costPrice;
    }

    /**
     * @param costPrice the costPrice to set
     */
    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * @return the award
     */
    public String getAward() {
        return award;
    }

    /**
     * @param award the award to set
     */
    public void setAward(String award) {
        this.award = award;
    }

}
