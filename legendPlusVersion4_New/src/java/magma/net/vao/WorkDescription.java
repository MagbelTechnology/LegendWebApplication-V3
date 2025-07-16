/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Ganiyu
 */
public class WorkDescription {
private int workId;
private String assetId;
private int workNumber;
private String workDescription;
private String reqnId;

    public WorkDescription(String assetId, int workNumber, String workDescription,String reqnId) {
        this.assetId = assetId;
        this.workNumber = workNumber;
        this.workDescription = workDescription;
        this.reqnId = reqnId;
    }

    public WorkDescription(int workNumber, String workDescription,String reqnId) {
        this.workNumber = workNumber;
        this.workDescription = workDescription;
        this.reqnId = reqnId;
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
     * @return the workNumber
     */
    public int getWorkNumber() {
        return workNumber;
    }

    /**
     * @param workNumber the workNumber to set
     */
    public void setWorkNumber(int workNumber) {
        this.workNumber = workNumber;
    }

    /**
     * @return the workDescription
     */
    public String getWorkDescription() {
        return workDescription;
    }

    /**
     * @param workDescription the workDescription to set
     */
    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    /**
     * @return the ReqnId
     */
    public String getReqnId() {
        return reqnId;
    }

    /**
     * @param ReqnId the ReqnId to set
     */
    public void setReqnId(String reqnId) {
        this.reqnId = reqnId;
    }
   
}
