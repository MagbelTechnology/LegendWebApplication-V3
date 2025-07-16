

package magma.net.vao;

/**
 *
 * @author Kareem Wasiu Aderemi
 */
public class RFQItems {
    private String id;
    private String desc;
    private String reqId;
    private String vendorCode;
    private String Delivery_Date;
    private String qty;
    private String unitPrice;
    private String costPrice;
    private String awarded;



     public RFQItems(String id,String desc,String reqId,String vendorCode,String Delivery_Date,
     String qty,String unitPrice,String costPrice,String awarded){

this.id= id;
this.desc= desc;
this.reqId= reqId;
this.vendorCode= vendorCode;
this.Delivery_Date= Delivery_Date;
this.qty= qty;
this.unitPrice= unitPrice;
this.costPrice= costPrice;
this.awarded= awarded;


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
     * @return the Delivery_Date
     */
    public String getDelivery_Date() {
        return Delivery_Date;
    }

    /**
     * @param Delivery_Date the Delivery_Date to set
     */
    public void setDelivery_Date(String Delivery_Date) {
        this.Delivery_Date = Delivery_Date;
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
     * @return the awarded
     */
    public String getAwarded() {
        return awarded;
    }

    /**
     * @param awarded the awarded to set
     */
    public void setAwarded(String awarded) {
        this.awarded = awarded;
    }


}
