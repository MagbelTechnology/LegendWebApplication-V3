package magma.net.vao;

public class Bid_Period {
	private String id;
	private String startDate;
	private String endDate;
	private int userId ;  
	private String createDate;
	private String status;
	private String bid_title;
	private String location;
	public Bid_Period(String startDate,String endDate, String bid_title,int userId,String createDate,String status,String location)
	{
		this.startDate = startDate;
		this.endDate = endDate;
		this.bid_title = bid_title;
		this.userId = userId;
		this.createDate = createDate;
		this.status = status;
		this.location = location;
	}
	public Bid_Period()
	{
		
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}	
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the bid_title
	 */
	public String getBid_title() {
		return bid_title;
	}
	/**
	 * @param bid_title the bid_title to set
	 */
	public void setBid_title(String bid_title) {
		this.bid_title = bid_title;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}	
	

}
