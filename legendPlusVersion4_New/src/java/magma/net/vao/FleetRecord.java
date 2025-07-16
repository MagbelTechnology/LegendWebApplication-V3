package magma.net.vao;

import java.io.Serializable;

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
public class FleetRecord implements Serializable {

    private String id;
    private String type;
    private double cost;
    private String assetCode;
    private String registrationNo;
    private String status;


    public FleetRecord(String id, String type, double cost, String assetCode,
                       String registrationNo,String status) {

        setId(id);
        setType(type);
        setCost(cost);
        setAssetCode(assetCode);
        setRegistrationNo(registrationNo);
        setStatus(status);
    }


    public FleetRecord(String id, String type, double cost,String status) {

        setId(id);
        setType(type);
        setCost(cost);
        setStatus(status);
    }

    /**
     * FleetRecord
     */
    public FleetRecord() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getCost() {
        return cost;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getStatus() {
        return status;
    }
}
