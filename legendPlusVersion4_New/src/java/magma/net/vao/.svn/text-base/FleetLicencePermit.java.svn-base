package magma.net.vao;

/**
 * <p>Title: FleetLicencePermit.java</p>
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
public class FleetLicencePermit extends FleetRecord {
    private String dateObtained;
    private String dateExpired;
    private String type;
    private String freq;
    private String noticeDate;
    private String invoiceNo;

    public FleetLicencePermit(String id, String type, double cost,
                              String assetCode,
                              String registrationNo,
                              String dateObtained, String dateExpired,
                              String status, String freq, String noticeDate,
                              String invoiceNo) {

        super(id, type, cost, assetCode, registrationNo, status);
        setDateObtained(dateObtained);
        setDateExpired(dateExpired);
        setCost(cost);
        setType(type);
        setFreq(freq);
        setNoticeDate(noticeDate);
        setInvoiceNo(invoiceNo);
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    public void setDateExpired(String dateExpired) {
        this.dateExpired = dateExpired;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public String getDateExpired() {
        return dateExpired;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
