/**
 * 
 */
package com.magbel.ia.vao;

/**
 * @author Lekan Matanmi
 * @company magbel Technology Ltd.
 * @Table IA_GL_BALANCE_NOTES
 * @version 1.00
 */
public class NoteMapping {  

	private String noteId;

	private String companyCode;
	
	private String noteCode;

	private String notedescription;

	private String noteNo;

	private String ledgerNo;

	private String reportName;

	private String noteName;
	
	private String status;

	private String userId;

	private String create_date;	

	/**
	 * 
	 */
	public NoteMapping() {
		// TODO Auto-generated constructor stub
	}

	public NoteMapping(String noteId, String companyCode, String noteCode,
			String notedescription, String noteNo, String ledgerNo,String reportName,
			String noteName, String status, String userId, String create_date) {

		this.noteId = noteId;
		this.companyCode = companyCode;
		this.noteCode = noteCode;
		this.notedescription =notedescription;
		this.noteNo = noteNo;
		this.ledgerNo = ledgerNo;
		this.reportName = reportName;
		this.noteName = noteName;		
		this.status = status;
		this.userId = userId;
		this.create_date = create_date;
	}

	/**
	 * @param noteId
	 *            the noteId to set
	 */
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	/**
	 * @return the noteId
	 */
	public String getNoteId() {
		return noteId;
	}

	/**
	 * @param noteCode
	 *            the noteCode to set
	 */
	public void setNoteCode(String noteCode) {
		this.noteCode = noteCode;
	}

	/**
	 * @return the noteCode
	 */
	public String getNoteCode() {
		return noteCode;
	}

	/**
	 * @param notedescription
	 *            the notedescription to set
	 */
	public void setNotedescription(String notedescription) {
		this.notedescription = notedescription;
	}

	/**
	 * @return the notedescription
	 */
	public String getNotedescription() {
		return notedescription;
	}

	/**
	 * @param noteNo
	 *            the noteNo to set
	 */
	public void setNoteNo(String noteNo) {
		this.noteNo = noteNo;
	}

	/**
	 * @return the noteNo
	 */
	public String getNoteNo() {
		return noteNo;
	}

	/**
	 * @param lefgerNo
	 *            the lefgerNo to set
	 */
	public void setLedgerNo(String ledgerNo) {
		this.ledgerNo = ledgerNo;
	}

	/**
	 * @return the lefgerNo
	 */
	public String getLedgerNo() {
		return ledgerNo;
	}

	/**
	 * @param reportName
	 *            the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName
	 *            the reportName to set
	 */
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	/**
	 * @return the noteName
	 */
	public String getNoteName() {
		return noteName;
	}	
	
	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param create_date
	 *            the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}
	
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}
	
}
