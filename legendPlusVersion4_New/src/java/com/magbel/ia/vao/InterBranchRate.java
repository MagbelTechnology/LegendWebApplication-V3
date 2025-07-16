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
public class InterBranchRate {

	private String id;
	private String debitRate;
	private String creditRate;
	private String accrualBasis;
	private String effectiveDate;
    private String createDate;
	private String status;
    

    public InterBranchRate(String id, String debitRate, String creditRate, String accrualBasis,
    		   String effectiveDate, String createDate, String status) {
    			
		setId(id);
		setDebitRate(debitRate);
		setCreditRate(creditRate);
		setAccrualBasis(accrualBasis);
		setEffectiveDate(effectiveDate);
		setCreateDate(createDate);
		setStatus(status);
		
    }

	public void setId(String id) {
        this.id = id;
    }
	
	public void setDebitRate(String debitRate) {
		this.debitRate = debitRate;
	}

	public void setCreditRate(String creditRate) {
		this.creditRate = creditRate;
	}

    public void setAccrualBasis(String accrualBasis){
		this.accrualBasis = accrualBasis;
	}
	
	public void setEffectiveDate(String createDate) {
		this.createDate = createDate;
    }
	
	public void setCreateDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
    }
	
	public void setStatus(String status) {
		this.status = status;
    }
	
	
	public String getId() {
		return this.id;
    }
	
    public String getDebitRate(){
		return this.debitRate;
	}

    public String getCreditRate() {
		return this.creditRate;
	}

    public String getAccrualBasis() {
        return this.accrualBasis;
    }
	
	public String getEffectiveDate() {
	    return this.effectiveDate;
    }
	
	public String getCreateDate() {
	    return this.createDate;
    }
	
	public String getStatus() {
	    return this.status;
    }
	
}
