package com.magbel.ia.vao;


/**
 * <p>Title: filestartDate.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */
public class SIC {

	private String id;
	private String icCode;
	private int userId;
	private String description;
    private String createDate;
	private String status;
    

    public SIC(String id,String icCode,int userId, String description,
    		        String createDate, String status) {
    			
		setId(id);
		setIcCode(icCode);
		setUserId(userId);
		setDescription(description);
		setCreateDate(createDate);
		setStatus(status);
		
    }

	public void setId(String id) {
        this.id = id;
    }
	
	public void setIcCode(String icCode) {
		this.icCode = icCode;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

    public void setDescription(String description){
		this.description = description;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
    }
	
	public void setStatus(String status) {
		this.status = status;
    }
	
	
	public String getId() {
		return this.id;
    }
	
    public String getIcCode(){
		return this.icCode;
	}

    public int getUserId() {
		return this.userId;
	}

    public String getDescription() {
        return this.description;
    }

	public String getCreateDate() {
	    return this.createDate;
    }
	
	public String getStatus() {
	    return this.status;
    }
	
}
