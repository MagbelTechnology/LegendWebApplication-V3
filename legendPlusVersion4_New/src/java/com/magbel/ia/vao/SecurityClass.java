/**
 * 
 */
package com.magbel.ia.vao;

/**
 * @author Rahman Oloritun
 * @version 1.00
 */
public class SecurityClass {

	private String classId;

	private String description;

	private String className;

	private String isSupervisor;

	private String classStatus;

	private String userId;

	private String createDate;

	private String isStoreKeeper;
	
	//private String fleetAdmin;

	public String getIsStoreKeeper() {
		return isStoreKeeper;
	}

	public void setIsStoreKeeper(String isStoreKeeper) {
		this.isStoreKeeper = isStoreKeeper;
	}

	/**
	 * 
	 */
	public SecurityClass() {
		// TODO Auto-generated constructor stub
	}

	public SecurityClass(String classId, String description, String className,
			String isSupervisor, String classStatus, String userId,
			String createDate) {
		// TODO Auto-generated constructor stub
		this.classId = classId;
		this.description = description;
		this.className = className;
		this.isSupervisor = isSupervisor;
		this.classStatus = classStatus;
		this.userId = userId;
		this.createDate = createDate;
		//this.fleetAdmin = fleetAdmin;
	}

	public SecurityClass(String classId, String description, String className,
			String isSupervisor, String classStatus, String userId,
			String createDate,String isStoreKeeper) {
		// TODO Auto-generated constructor stub
		this.classId = classId;
		this.description = description;
		this.className = className;
		this.isSupervisor = isSupervisor;
		this.classStatus = classStatus;
		this.userId = userId;
		this.createDate = createDate;
		this.isStoreKeeper = isStoreKeeper;
		//this.fleetAdmin = fleetAdmin;
	}
	/**
	 * @param classId
	 *            the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		if (description != null)
		{
		this.description = description.trim();
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param isSupervisor
	 *            the isSupervisor to set
	 */
	public void setIsSupervisor(String isSupervisor) {
		this.isSupervisor = isSupervisor;
	}

	/**
	 * @return the isSupervisor
	 */
	public String getIsSupervisor() {
		return isSupervisor;
	}

	/**
	 * @param classStatus
	 *            the classStatus to set
	 */
	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	/**
	 * @return the classStatus
	 */
	public String getClassStatus() {
		return classStatus;
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
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param fleetAdmin
	 *            the fleetAdmin to set
	 */
	/*public void setFleetAdmin(String fleetAdmin) {
		this.fleetAdmin = fleetAdmin;
	}*/

	/**
	 * @return the fleetAdmin
	 */
	/*public String getFleetAdmin() {
		return fleetAdmin;
	}*/

}
