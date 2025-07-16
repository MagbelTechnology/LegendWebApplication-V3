package com.magbel.ia.vao;


/**
 * <p>Title: fileperiodEnding.java</p>
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
public class History {

	private String id;
	private String accountNo;
    private String historyCode;
	private String periodEnding;
	private String endingBalance;
    private String averageBalance;
	private String avgBalYTD;
	private String lastYearEndingBal;
	private String budgetAmount;
	private String budgetVariance;
	private String status;
    

    public History(String id, String accountNo, String periodEnding, String endingBalance, 
	               String averageBalance, String avgBalYTD, String lastYearEndingBal,
				   String budgetAmount, String budgetVariance, String status) {
    		        
    			
		setId(id);
		setAccountNo(accountNo);
        setHistoryCode(historyCode);
		setPeriodEnding(periodEnding);
		setEndingBalance(endingBalance);
		setAverageBalance(averageBalance);
		setAvgBalYTD(avgBalYTD);
		setLastYearEndingBal(lastYearEndingBal);
		setBudgetAmount(budgetAmount);
		setBudgetVariance(budgetVariance);
		setStatus(status);
		
    }

	public void setId(String id) {
        this.id = id;
    }
	
	public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setHistoryCode(String historyCode) {
        this.historyCode = historyCode;
    }

	public void setPeriodEnding(String periodEnding) {
		this.periodEnding = periodEnding;
	}

	public void setEndingBalance(String endingBalance) {
		this.endingBalance = endingBalance;
	}

    public void setAverageBalance(String averageBalance){
		this.averageBalance =averageBalance;
	}
	
	public void setAvgBalYTD(String avgBalYTD) {
		this.avgBalYTD = avgBalYTD;
    }
	
	public void setLastYearEndingBal(String lastYearEndingBal) {
		this.lastYearEndingBal = lastYearEndingBal;
    }
	
	public void setBudgetAmount(String budgetAmount) {
		this.budgetAmount = budgetAmount;
    }
	
	public void setBudgetVariance(String budgetVariance) {
		this.budgetVariance = budgetVariance;
    }

	public void setStatus(String status) {
		this.status = status;
    }
	
	
	public String getId() {
		return this.id;
    }
	
	public String getAccountNo() {
		return this.accountNo;
    }

	public String getHistoryCode() {
		return this.historyCode;
    }

    public String getPeriodEnding(){
		return this.periodEnding;
	}

    public String getEndingBalance() {
		return this.endingBalance;
	}

    public String getAverageBalance() {
        return this.averageBalance;
    }

	public String getAvgBalYTD() {
	    return this.avgBalYTD;
    }
	
	public String getLastYearEndingBal() {
	    return this.lastYearEndingBal;
    }
	
	public String getBudgetAmount() {
	    return this.budgetAmount;
    }
	
	public String getBudgetVariance() {
	    return this.budgetVariance;
    }
	
	public String getStatus() {
	    return this.status;
    }
	
}
