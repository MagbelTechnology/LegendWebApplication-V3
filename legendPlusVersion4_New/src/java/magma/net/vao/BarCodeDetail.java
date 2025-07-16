package magma.net.vao;


/**
 * <p>Title: BarCodeDetail.java</p>
 *
 * <p>Description: Holds details of an Asset's BarCode.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */


public class BarCodeDetail implements java.io.Serializable {

    private String assetId;
    private String assetReg;
    private String assetDesc;

    public BarCodeDetail(String assetId, String assetReg, String assetDesc) {
        setAssetId(assetId);
        setAssetReg(assetReg);
        setAssetDesc(assetDesc);
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setAssetReg(String assetReg) {
        this.assetReg = assetReg;
    }

    public void setAssetDesc(String assetDesc) {
        this.assetDesc = assetDesc;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public String getAssetReg() {
        return this.assetReg;
    }

    public String getAssetDesc() {
        return this.assetDesc;
    }


}