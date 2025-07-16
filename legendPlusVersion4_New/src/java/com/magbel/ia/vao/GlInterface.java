package com.magbel.ia.vao;

public class GlInterface {

	private String id;

	private String interCode;

	private String interName;

	private String ledgeNo;

	private String userId;

	private String createDt;

	public GlInterface() {

	}


	public GlInterface(String id, String interCode, String interName,
			String ledgeNo, String userId, String createDt) {

		this.id = id;
		this.interCode = interCode;
		this.interName = interName;
		this.ledgeNo = ledgeNo;
		this.userId = userId;
		this.createDt = createDt;

	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param interCode
	 *            the interCode to set
	 */
	public void setInterCode(String interCode) {
		this.interCode = interCode;
	}

	/**
	 * @return the interCode
	 */
	public String getInterCode() {
		return interCode;
	}

	/**
	 *            the interName to set
	 */
	public void setInterName(String interName) {
		this.interName = interName;
	}

	/**
	 * @return the interName
	 */
	public String getInterName() {
		return interName;
	}

	/**
	 *            the ledgeNo to set
	 */
	public void setLedgeNo(String ledgeNo) {
		this.ledgeNo = ledgeNo;
	}

	/**
	 * @return the ledgeNo
	 */
	public String getLedgeNo() {
		return ledgeNo;
	}

	/**
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
	 *            the userId to set
	 */
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	/**
	 * @return the userId
	 */
	public String getCreateDt() {
		return createDt;
	}

}
