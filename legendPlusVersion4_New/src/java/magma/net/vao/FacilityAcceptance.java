/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Ganiyu
 */
public class FacilityAcceptance {

    private String acceptanceCode;
            private String requistionID;
            private String acceptanceRemark;
            private String assetId;
            private String receiveMeans;
            private int supervisor;
            private String status;
            private int receivedBy;
            private  String receivedDate;
            private int tranId;
            private String processWorkOrder;
            private int workId;
            private int numOfWorkDesc;
            private String isImage;

    public FacilityAcceptance() {
    }

    public FacilityAcceptance(String acceptanceCode, String assetId, int receivedBy, String receivedDate,
            int tranId, String status, String processWorkOrder) {
        this.acceptanceCode = acceptanceCode;
        this.assetId = assetId;
        this.receivedBy = receivedBy;
        this.receivedDate = receivedDate;
        this.tranId = tranId;
        this.status = status;
        this.processWorkOrder= processWorkOrder;
    }



    public FacilityAcceptance(String acceptanceCode, String requistionID, String acceptanceRemark, int supervisor,String receiveMeans) {
        this.acceptanceCode = acceptanceCode;
        this.requistionID = requistionID;
        this.acceptanceRemark = acceptanceRemark;
        this.supervisor = supervisor;
        this.receiveMeans= receiveMeans;
    }
    public FacilityAcceptance(String acceptanceCode, String requistionID, String acceptanceRemark, int supervisor,String receiveMeans,int workId,int numOfWorkDesc) {
        this.acceptanceCode = acceptanceCode;
        this.requistionID = requistionID;
        this.acceptanceRemark = acceptanceRemark;
        this.supervisor = supervisor;
        this.receiveMeans= receiveMeans;
        this.numOfWorkDesc= numOfWorkDesc;
        this.workId = workId;
    }

    /**
     * @return the acceptanceCode
     */
    public String getAcceptanceCode() {
        return acceptanceCode;
    }

    /**
     * @param acceptanceCode the acceptanceCode to set
     */
    public void setAcceptanceCode(String acceptanceCode) {
        this.acceptanceCode = acceptanceCode;
    }

    /**
     * @return the requistionID
     */
    public String getRequistionID() {
        return requistionID;
    }

    /**
     * @param requistionID the requistionID to set
     */
    public void setRequistionID(String requistionID) {
        this.requistionID = requistionID;
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
     * @return the receiveMeans
     */
    public String getReceiveMeans() {
        return receiveMeans;
    }

    /**
     * @param receiveMeans the receiveMeans to set
     */
    public void setReceiveMeans(String receiveMeans) {
        this.receiveMeans = receiveMeans;
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
     * @return the receivedBy
     */
    public int getReceivedBy() {
        return receivedBy;
    }

    /**
     * @param receivedBy the receivedBy to set
     */
    public void setReceivedBy(int receivedBy) {
        this.receivedBy = receivedBy;
    }

    /**
     * @return the receivedDate
     */
    public String getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
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
     * @return the processWorkOrder
     */
    public String getProcessWorkOrder() {
        return processWorkOrder;
    }

    /**
     * @param processWorkOrder the processWorkOrder to set
     */
    public void setProcessWorkOrder(String processWorkOrder) {
        this.processWorkOrder = processWorkOrder;
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
     * @return the numOfWorkDesc
     */
    public int getNumOfWorkDesc() {
        return numOfWorkDesc;
    }

    /**
     * @param numOfWorkDesc the numOfWorkDesc to set
     */
    public void setNumOfWorkDesc(int numOfWorkDesc) {
        this.numOfWorkDesc = numOfWorkDesc;
    }

    /**
     * @return the isImage
     */
    public String getIsImage() {
        return isImage;
    }

    /**
     * @param isImage the isImage to set
     */
    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }

}
