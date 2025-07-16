package magma.net.vao;

/**
 *
 * @author Kareem Wasiu Aderemi
 */
public class ItemsPerReqId {
    private String id;
    private String desc;
    private String reqId;
    private String Delivery_Date;
    private String qty;
    private double uPrice;
    

     public ItemsPerReqId(String id,String desc,String reqId,String Delivery_Date,
     String qty,double uPrice){

this.id= id;
this.desc= desc;
this.reqId= reqId;
this.Delivery_Date= Delivery_Date;
this.qty= qty;
this.uPrice= uPrice;
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
     * @return the uPrice
     */
    public double getuPrice() {
        return uPrice;
    }

    /**
     * @param uPrice the uPrice to set
     */
    public void setuPrice(double uPrice) {
        this.uPrice = uPrice;
    }


}
