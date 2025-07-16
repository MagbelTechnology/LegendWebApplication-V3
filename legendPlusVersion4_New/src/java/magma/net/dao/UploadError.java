package magma.net.dao;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2018</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Lekan Matanmi
 * @version 2.0
 */
public class UploadError {
    private String transactiontype;
    private String errorDescription;
    private String errorDate;
    private String mtid;
    private String batchNo;
    private String sequenceNo;
    private String errorCode;
    private String accountNo;
    public UploadError() {
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public void setMtid(String mtid) {
        this.mtid = mtid;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public String getMtid() {
        return mtid;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getAccountNo() {
        return accountNo;
    }
    
}
