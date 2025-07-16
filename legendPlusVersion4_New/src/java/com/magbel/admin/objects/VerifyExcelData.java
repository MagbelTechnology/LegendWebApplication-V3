/**
 * 
 */
package com.magbel.admin.objects;

/**
 * @author Lekan Matanmi
 * @email lekanmatanmi@magbeltech.com
 *
 */
public class VerifyExcelData {
	
	private  String Id;
	private  String IssueSubject; 
	private  String IssueDescription; 
	private  String Assignee; 
	private  String CreationDate;
	private String StatusCode;
	private String phoneNo;
	
	
	public VerifyExcelData(){
		   
	} 

	public String getIssueSubject() {
		return IssueSubject;
	}

	public void setIssueSubject(String IssueSubject) {
		this.IssueSubject = IssueSubject;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getIssueDescription() {
		return IssueDescription;
	}

	public void setIssueDescription(String IssueDescription) {
		this.IssueDescription = IssueDescription;
	}

	public String getAssignee() {
		return Assignee;
	}

	public void setAssignee(String Assignee) {
		this.Assignee = Assignee;
	}

	public String getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(String CreationDate) {
		this.CreationDate = CreationDate;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String StatusCode) {
		this.StatusCode = StatusCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}
