package  com.magbel.admin.objects;


public class  BranchContact
{
  private  int     mtId = 0;
  private  String  branchCode;
  private  String  staffName;
  private  String  email;
  private  String  phone ;
  private  String  status;
  private  String  userId;
  private String  createDate;
  
  
  
  
public final String getBranchCode() {
	return branchCode;
}
public final void setBranchCode(String branchCode) {
	this.branchCode = branchCode;
}
public final String getEmail() {
	return email;
}
public final void setEmail(String email) {
	this.email = email;
}
public final int getMtId() {
	return mtId;
}
public final void setMtId(int mtId) {
	this.mtId = mtId;
}
public final String getPhone() {
	return phone;
}
public final void setPhone(String phone) {
	this.phone = phone;
}
public final String getStaffName() {
	return staffName;
}
public final void setStaffName(String staffName) {
	this.staffName = staffName;
}
public final String getStatus() {
	return status;
}
public final void setStatus(String status) {
	this.status = status;
}
  
  
public final String getUserId() {
	return userId;
}
public final void setUserId(String UserId) {
	this.userId = UserId;
}

public final String getCreateDate() {
	return createDate;
}
public final void setCreateDate(String CreateDate) {
	this.createDate = CreateDate;
}  
 } 