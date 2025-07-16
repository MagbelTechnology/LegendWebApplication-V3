package magma.net.dao;

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
public class DepreciationError {
    private String processid;
    private String errorDescription;
    private String errorDate;
    private String mtid;
    public DepreciationError() {
    }

    public void setProcessid(String processid) {
        this.processid = processid;
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

    public String getProcessid() {
        return processid;
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
}
