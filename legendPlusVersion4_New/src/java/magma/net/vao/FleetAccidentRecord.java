package magma.net.vao;

/**
 * <p>Title: FleetAccidentRecord.java</p>
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
public class FleetAccidentRecord extends FleetRecord {
    private String driverInvolved;
    private String location;
    private String accidentDate;
    private boolean policeReportObtained;
    private String reportDate;
    private double costBorneByCompany;
    private double costBorneByInssurance;
    private int duration;
    private String otherDetails;
    private boolean issuranceNotified;
    private String notificationDate;
    private boolean replacementRequired;
    private String invoiceNo;
    private String insurer;
    private String transDate;
    private String raised_Entry;
   // private String compDate;

    public FleetAccidentRecord(String id, String type, double cost,
                               String assetCode,
                               String registrationNo,
                               String driverInvolved, String location,
                               String accidentDate,
                               boolean policeReportObtained, String reportDate,
                               double costBorneByCompany,
                               double costBorneByInssurance, int duration,
                               String otherDetails, boolean issuranceNotified,
                               String notificationDate, boolean replaceRequired,
                               String status, String invoiceNo, String insurer,
                               String transDate) {

        super(id, type, cost, assetCode, registrationNo, status);
        setDriverInvolved(driverInvolved);
        setLocation(location);
        setAccidentDate(accidentDate);
        setPoliceReportObtained(policeReportObtained);
        setReportDate(reportDate);
        setCostBorneByCompany(costBorneByCompany);
        setCostBorneByInssurance(costBorneByInssurance);
        setDuration(duration);
        setOtherDetails(otherDetails);
        setIssuranceNotified(issuranceNotified);
        setNotificationDate(notificationDate);
        setReplacementRequired(replacementRequired);
        setInvoiceNo(invoiceNo);
        setInsurer(insurer);
        setTransDate(transDate);
       // setCompDate(compDate);

    }



     public FleetAccidentRecord(String id, String type, double cost,
                               String assetCode,
                               String registrationNo,
                               String driverInvolved, String location,
                               String accidentDate,
                               boolean policeReportObtained, String reportDate,
                               double costBorneByCompany,
                               double costBorneByInssurance, int duration,
                               String otherDetails, boolean issuranceNotified,
                               String notificationDate, boolean replaceRequired,
                               String status, String invoiceNo, String insurer) {

        super(id, type, cost, assetCode, registrationNo, status);
        setDriverInvolved(driverInvolved);
        setLocation(location);
        setAccidentDate(accidentDate);
        setPoliceReportObtained(policeReportObtained);
        setReportDate(reportDate);
        setCostBorneByCompany(costBorneByCompany);
        setCostBorneByInssurance(costBorneByInssurance);
        setDuration(duration);
        setOtherDetails(otherDetails);
        setIssuranceNotified(issuranceNotified);
        setNotificationDate(notificationDate);
        setReplacementRequired(replacementRequired);
        setInvoiceNo(invoiceNo);
        setInsurer(insurer);

    }




    public void setDriverInvolved(String driverInvolved) {
        this.driverInvolved = driverInvolved;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public void setPoliceReportObtained(boolean policeReportObtained) {
        this.policeReportObtained = policeReportObtained;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public void setCostBorneByCompany(double costBorneByCompany) {
        this.costBorneByCompany = costBorneByCompany;
    }

    public void setCostBorneByInssurance(double costBorneByInssurance) {
        this.costBorneByInssurance = costBorneByInssurance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public void setIssuranceNotified(boolean issuranceNotified) {
        this.issuranceNotified = issuranceNotified;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public void setReplacementRequired(boolean replacementRequired) {
        this.replacementRequired = replacementRequired;
    }

    public String getDriverInvolved() {
        return driverInvolved;
    }

    public String getLocation() {
        return location;
    }

    public String getAccidentDate() {
        return accidentDate;
    }

    public boolean isPoliceReportObtained() {
        return policeReportObtained;
    }

    public String getReportDate() {
        return reportDate;
    }

    public double getCostBorneByCompany() {
        return costBorneByCompany;
    }

    public double getCostBorneByInssurance() {
        return costBorneByInssurance;
    }

    public int getDuration() {
        return duration;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public boolean isIssuranceNotified() {
        return issuranceNotified;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public boolean getReplacementRequired() {
        return replacementRequired;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInsurer() {
        return insurer;
    }

    public void setInsurer(String insurer) {
        this.insurer = insurer;
    }



	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the raised_Entry
	 */
	public String getRaised_Entry() {
		return raised_Entry;
	}

	/**
	 * @param raised_Entry the raised_Entry to set
	 */
	public void setRaised_Entry(String raised_Entry) {
		this.raised_Entry = raised_Entry;
	}
}
