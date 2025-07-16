package legend.admin.objects;


public class Sla
{	private String sla_ID;

private String sla_name;

private String sla_description;
private String DeptCode; 

private String CatCode;

private String userId;

private String createDate;
private String Status;

private String slaStartDate;

private String slaEndDate;

private String slaAlertStartDate;

private int diffDate;

private int remainDay;

private String slaUserId;

private String slaFullName;

private String slaEmail;

private String slaName;

private int alertFreq;

private int alertStart;

private String RESOLVE_DAY;
private String RESOLVE_HOUR;
private String RESOLVE_MINUTE;

private String slaEscalateName;

public Sla() {

}   

public Sla(String sla_ID, String DeptCode, String sla_name, String sla_description,
		String userId, String createDate, String Status) {
	super();
	this.sla_ID = sla_ID;
	this.DeptCode = DeptCode;
	this.sla_name = sla_name;
	this.sla_description = sla_description;
	this.userId = userId;
	this.createDate = createDate;
	this.Status = Status;
}

public Sla(String sla_ID, String DeptCode, String sla_name, String sla_description,
		String userId, String createDate, String Status, String slaStartDate, String slaEndDate) {
	super();
	this.sla_ID = sla_ID;
	this.DeptCode = DeptCode;
	this.sla_name = sla_name;
	this.sla_description = sla_description;
	this.userId = userId;
	this.createDate = createDate;
	this.Status = Status;
	this.slaStartDate = slaStartDate;
	this.slaEndDate = slaEndDate;
}

public String getSla_ID() {
	return sla_ID;
}

public void setSla_ID(String sla_ID) {
	this.sla_ID = sla_ID;
}

public String getSla_name() {
	return sla_name;
}

public void setSla_name(String sla_name) {
	this.sla_name = sla_name;
}

public String getSla_description() {
	return sla_description;
}

public void setSla_description(String sla_description) {
	this.sla_description = sla_description;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getCreateDate() {
	return createDate;
}

public void setCreateDate(String createDate) {
	this.createDate = createDate;
}

public String getDeptCode() {
	return DeptCode;
}

public void setDeptCode(String DeptCode) {
	this.DeptCode = DeptCode;
}
public String getCatCode() {
	return CatCode;
}

public void setCatCode(String CatCode) {
	this.CatCode = CatCode;
}
public String getStatus() {
	return Status;
}

public void setStatus(String Status) {
	this.Status = Status;
}

public String getSlaStartDate() {
	return slaStartDate;
}

public void setSlaStartDate(String slaStartDate) {
	this.slaStartDate = slaStartDate;
}

public String getSlaAlertStartDate() {
	return slaAlertStartDate;
}

public void setSlaAlertStartDate(String slaAlertStartDate) {
	this.slaAlertStartDate = slaAlertStartDate;
}

public String getSlaEndDate() {
	return slaEndDate;
}

public void setSlaEndDate(String slaEndDate) {
	this.slaEndDate = slaEndDate;
}

public int getDiffDate() {
	return diffDate;
}

public void setDiffDate(int diffDate) {
	this.diffDate = diffDate;
}

public int getRemainDay() {
	return remainDay;
}

public void setRemainDay(int remainDay) {
	this.remainDay = remainDay;
}

public String getSlaUserId() {
	return slaUserId;
}

public void setSlaUserId(String slaUserId) {
	this.slaUserId = slaUserId;
}

public String getSlaFullName() {
	return slaFullName;
}

public void setSlaFullName(String slaFullName) {
	this.slaFullName = slaFullName;
}

public String getSlaEmail() {
	return slaEmail;
}

public void setSlaEmail(String slaEmail) {
	this.slaEmail = slaEmail;
}

public String getSlaName() {
	return slaName;
}

public void setSlaName(String slaName) {
	this.slaName = slaName;
}

public int getAlertFreq() {
	return alertFreq;
}

public void setAlertFreq(int alertFreq) {
	this.alertFreq = alertFreq;
}

public int getAlertStart() {
	return alertStart;
}

public void setAlertStart(int alertStart) {
	this.alertStart = alertStart;
}

public String getRESOLVE_DAY()
{
    return RESOLVE_DAY;
}

public void setRESOLVE_DAY(String resolve_day)
{
    RESOLVE_DAY = resolve_day;
}

public String getRESOLVE_HOUR()
{
    return RESOLVE_HOUR;
}

public void setRESOLVE_HOUR(String resolve_hour)
{
    RESOLVE_HOUR = resolve_hour;
}

public String getRESOLVE_MINUTE()
{
    return RESOLVE_MINUTE;
}

public void setRESOLVE_MINUTE(String resolve_minute)
{
    RESOLVE_MINUTE = resolve_minute;
}

public String getSlaEscalateName() {
	return slaEscalateName;
}

public void setSlaEscalateName(String slaEscalateName) {
	this.slaEscalateName = slaEscalateName;
}

}
