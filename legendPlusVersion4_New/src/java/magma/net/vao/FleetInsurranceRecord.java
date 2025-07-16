package magma.net.vao;

/**
 * <p>Title: FleetInsurranceRecord.java</p>
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
public class FleetInsurranceRecord extends FleetRecord {

    private String company;
    private String dateObtained;
    private String dateExpired;
    private String histId;
    private String freq;
    private String noteDate;
    private String invoiceNo;
    private String ltId;
    private String raised_Entry;
    private String projectCode;
    
    public FleetInsurranceRecord(String id, String type, double cost,
                                 String assetCode,
                                 String registrationNo, String company,
                                 String dateObtained,
                                 String dateExpired, String status,
                                 String tranId,
                                 String noteDate, String freq, String invoiceNo) {
        super(id, type, cost, assetCode, registrationNo, status);
        setCompany(company);
        setDateObtained(dateObtained);
        setDateExpired(dateExpired);
        setHistId(tranId);
        setFreq(freq);
        setNoteDate(noteDate);
        setInvoiceNo(invoiceNo);

    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    public void setDateExpired(String dateExpired) {
        this.dateExpired = dateExpired;
    }

    public String getCompany() {
        return company;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public String getDateExpired() {
        return dateExpired;
    }

    public String getHistId() {
        return histId;
    }

    public void setHistId(String histId) {
        this.histId = histId;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getLtId() {
        return ltId;
    }

    public void setLtId(String ltId) {
        this.ltId = ltId;
    }

    public String getRaised_Entry() {
        return raised_Entry;
    }

    public void setRaised_Entry(String raised_Entry) {
        this.raised_Entry = raised_Entry;
    }    
    

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }      
}
