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
public class AccidentImage implements Serializable {
    private String assetId;
    private String registrationNo;
    private String imageId;
    private byte[] frontView;
    private byte[] sideView;
    private byte[] areaView;
    private byte[] backView;

    public AccidentImage(String assetId, String registrationNo, String imageId,
                         byte[] frontView, byte[] sideView, byte[] areaView,
                         byte[] backView) {
        setAssetId(assetId);
        setRegistrationNo(registrationNo);
        setImageId(imageId);
        setFrontView(frontView);
        setSideView(sideView);
        setAreaView(areaView);
        setBackView(backView);

    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setFrontView(byte[] frontView) {
        this.frontView = frontView;
    }

    public void setSideView(byte[] sideView) {
        this.sideView = sideView;
    }

    public void setAreaView(byte[] areaView) {
        this.areaView = areaView;
    }

    public void setBackView(byte[] backView) {
        this.backView = backView;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getImageId() {
        return imageId;
    }

    public byte[] getFrontView() {
        return frontView;
    }

    public byte[] getSideView() {
        return sideView;
    }

    public byte[] getAreaView() {
        return areaView;
    }

    public byte[] getBackView() {
        return backView;
    }
}
