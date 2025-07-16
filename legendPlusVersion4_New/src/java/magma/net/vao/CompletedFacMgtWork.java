/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Ganiyu
 */
public class CompletedFacMgtWork {

    private String assetId;
    private String workOrderCode;
    private int workId;
    private int tranId;
    private int userid;
    private String prepareDate;
    private String reqnId;
    private String status;
    private String image;
    private int workCompletionRecepient;
    private int supervisor;
    private String prepareTime;
    private String acceptanceRemark;
    private int preparedBy;

    public CompletedFacMgtWork(String assetId, String workOrderCode, int workId, int tranId, int userid, String prepareDate, String reqnId, String status) {
        this.assetId = assetId;
        this.workOrderCode = workOrderCode;
        this.workId = workId;
        this.tranId = tranId;
        this.userid = userid;  
        this.prepareDate = prepareDate;
        this.reqnId = reqnId;
        this.status = status;
    }

    public CompletedFacMgtWork( int tranId, int preparedBy, String prepareDate, String reqnId, int workCompletionRecepient, int supervisor, String prepareTime, String acceptanceRemark) {

        this.tranId = tranId;
        this.preparedBy = preparedBy;
        this.prepareDate = prepareDate;
        this.reqnId = reqnId;
        this.workCompletionRecepient = workCompletionRecepient;
        this.supervisor = supervisor;
        this.prepareTime = prepareTime;
        this.acceptanceRemark = acceptanceRemark;
    }

    public CompletedFacMgtWork() {
    }

    public CompletedFacMgtWork(String assetId, String workOrderCode) {
        this.assetId = assetId;
        this.workOrderCode = workOrderCode;
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
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the workCompletionRecepient
     */
    public int getWorkCompletionRecepient() {
        return workCompletionRecepient;
    }

    /**
     * @param workCompletionRecepient the workCompletionRecepient to set
     */
    public void setWorkCompletionRecepient(int workCompletionRecepient) {
        this.workCompletionRecepient = workCompletionRecepient;
    }

    /**
     * @return the supervisor
     */
    public int getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(int supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return the prepareTime
     */
    public String getPrepareTime() {
        return prepareTime;
    }

    /**
     * @param prepareTime the prepareTime to set
     */
    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime;
    }

    /**
     * @return the acceptanceRemark
     */
    public String getAcceptanceRemark() {
        return acceptanceRemark;
    }

    /**
     * @param acceptanceRemark the acceptanceRemark to set
     */
    public void setAcceptanceRemark(String acceptanceRemark) {
        this.acceptanceRemark = acceptanceRemark;
    }

    /**
     * @return the preparedBy
     */
    public int getPreparedBy() {
        return preparedBy;
    }

    /**
     * @param preparedBy the preparedBy to set
     */
    public void setPreparedBy(int preparedBy) {
        this.preparedBy = preparedBy;
    }



}
