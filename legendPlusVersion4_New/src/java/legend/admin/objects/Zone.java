package legend.admin.objects;

public class Zone {

	private String zoneId;

	private String zoneCode;

	private String zoneName;

	private String zoneAcronym;

	private String zoneAddress;

	private String zonePhone;

	private String zoneFax;

	private String zoneStatus;

	private String userId;

	private String createDate;

	public Zone() {
		// TODO Auto-generated constructor stub
	}

	public Zone(String zoneId, String zoneCode, String zoneName,
			String zoneAcronym, String zoneAddress, String zonePhone,
			String zoneFax, String zoneStatus, String userId,
			String createDate) {

		this.zoneId = zoneId;
		this.zoneCode = zoneCode;
		this.zoneName = zoneName;
		this.zoneAcronym = zoneAcronym;
		this.zoneAddress = zoneAddress;
		this.zonePhone = zonePhone;
		this.zoneFax = zoneFax;
		this.zoneStatus = zoneStatus;
		this.userId = userId;
		this.createDate = createDate;

	}

	/**
	 * @param zoneId
	 *            the zoneId to set
	 */
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * @return the zoneId
	 */
	public String getZoneId() {
		return zoneId;
	}

	/**
	 * @param zoneCode
	 *            the zoneCode to set
	 */
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	/**
	 * @return the zoneCode
	 */
	public String getZoneCode() {
		return zoneCode;
	}

	/**
	 * @param zoneName
	 *            the zoneName to set
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	/**
	 * @return the zoneName
	 */
	public String getZoneName() {
		return zoneName;
	}

	/**
	 * @param zoneAcronym
	 *            the zoneAcronym to set
	 */
	public void setZoneAcronym(String zoneAcronym) {
		this.zoneAcronym = zoneAcronym;
	}

	/**
	 * @return the zoneAcronym
	 */
	public String getZoneAcronym() {
		return zoneAcronym;
	}

	/**
	 * @param zoneAddress
	 *            the zoneAddress to set
	 */
	public void setZoneAddress(String zoneAddress) {
		this.zoneAddress = zoneAddress;
	}

	/**
	 * @return the zoneAddress
	 */
	public String getZoneAddress() {
		return zoneAddress;
	}

	/**
	 * @param zonePhone
	 *            the zonePhone to set
	 */
	public void setZonePhone(String zonePhone) {
		this.zonePhone = zonePhone;
	}

	/**
	 * @return the zonePhone
	 */
	public String getZonePhone() {
		return zonePhone;
	}

	/**
	 * @param zoneFax
	 *            the zoneFax to set
	 */
	public void setZoneFax(String zoneFax) {
		this.zoneFax = zoneFax;
	}

	/**
	 * @return the zoneFax
	 */
	public String getZoneFax() {
		return zoneFax;
	}

	/**
	 * @param zoneStatus
	 *            the zoneStatus to set
	 */
	public void setZoneStatus(String zoneStatus) {
		this.zoneStatus = zoneStatus;
	}

	/**
	 * @return the zoneStatus
	 */
	public String getZoneStatus() {
		return zoneStatus;
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

}
