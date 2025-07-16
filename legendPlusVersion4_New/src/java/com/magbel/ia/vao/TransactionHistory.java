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
public class TransactionHistory {

	private String id;
    private String effectiveDate;
	private String enteredDate;
	private String reversalDate;
    private String transCode;
	private String description;
	private double amount;
	private double balance;
	private double debit;
	private double credit;
	private String status;
    

    public TransactionHistory(String id,String effectiveDate,String enteredDate,String reversalDate, 
	                          String transCode, String description, double amount, double balance,
							  double debit, double credit, String status) {
    		        
    			
		setId(id);
        setEffectiveDate(effectiveDate);
		setEnteredDate(enteredDate);
		setReversalDate(reversalDate);
		setTransCode(transCode);
		setDescription(description);
		setAmount(amount);
		setBalance(balance);
		setDebit(debit);
		setCredit(credit);
		setStatus(status);
		
    }

	public void setId(String id) {
        this.id = id;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}

	public void setReversalDate(String reversalDate) {
		this.reversalDate = reversalDate;
	}

    public void setTransCode(String transCode){
		this.transCode = transCode;
	}
	
	public void setDescription(String description) {
        this.description = description;
    }
	
	public void setAmount(double amount) {
        this.amount = amount;
    }
	
	public void setBalance(double balance) {
        this.balance = balance;
    }
	
	public void setDebit(double debit) {
		this.debit = debit;
    }
	
	public void setCredit(double credit) {
		this.credit = credit;
    }
	
	public void setStatus(String status) {
		this.status = status;
    }
	
	
	public String getId() {
		return this.id;
    }

	public String getEffectiveDate() {
		return this.effectiveDate;
    }

    public String getEnteredDate(){
		return this.enteredDate;
	}

    public String getReversalDate() {
		return this.reversalDate;
	}

    public String getTransCode() {
        return this.transCode;
    }
	
	public String getDescription() {
		return this.description;
    }
	
	public double getAmount() {
		return this.amount;
    }
	
	public double getBalance() {
		return this.balance;
    }
	
	public double getDebit() {
		return this.debit;
    }
	
	public double getCredit() {
		return this.credit;
    }

	public String getStatus() {
	    return this.status;
    }
	
}
