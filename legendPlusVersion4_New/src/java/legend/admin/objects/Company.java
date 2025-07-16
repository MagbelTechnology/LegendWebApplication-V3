
package legend.admin.objects;

public class Company {

	private String companyCode = "";

	private String companyName = "";

	private String acronym = "";

	private String companyAddress = "";

	private double vatRate = 0;

	private double whtRate = 0;
	
	private double fedwhtRate = 0; 						//ganiyu's code

	private String financialStartDate = ""; // smalldatetime

	private int financialNoOfMonths = 0;

	private String financialEndDate = ""; // smalldatetime

	private int minimumPassword = 4;

	private int passwordExpiry = 1;

	private int sessionTimeout = 1;

	private String enforceAcqBudget = "N";

	private String enforcePmBudget = "N";

	private String enforceFuelAllocation = "N";

	private String requireQuarterlyPM = "N";

	private String quarterlySurplusCf = "N";

	private String userId;

	private String processingStatus;

	private double transWaitTime = 0;
	
	private String logUserAudit;

	private String processingDate;

    private int log_on;
    private String comp_delimiter = "";

    private String password_upper;
    private String password_lower;
    private String password_numeric;
    private String password_special;

    private int passwordLimit;
    private int proofSessionTimeout = 1;

	private String processingFrequency;

	private String nextProcessingDate;

	private String defaultBranch;

	private String branchName;

	private String suspenseAcct;

	private String autoGenId="N";

	private String residualValue;

	private String depreciationMethod;

	private String vatAccount;

	private String whtAccount;

    private String fedWhtAccount;
	private String PLDisposalAccount;

	private String PLDStatus;

	private String vatAcctStatus;

	private String whtAcctStatus;

    private String fedWhtAcctStatus;
	private String suspenseAcctStatus;

	private String sbuRequired;

	private String sbuLevel;
	private String sysDate;
	
	private String assetSuspenseAcct;

    private String lpo_r;
    private String bar_code_r;
    private double cp_threshold;

    private String deferAccount;
    private double trans_threshold;
    private String part_pay;
    private String asset_acq_status;
    private String asset_defer_status;
    private String part_pay_status;
    private String thirdpartytransaction="N";
    private String raiseEntry="N";
    
    private String lossDisposalAcct;
    private String LDAcctStatus;
    private String groupAssetAcct;
    private String GAAStatus;
    private String selfChargeAcct;
    private String selfChargeStatus;
    private String databaseName;
    private String recordType;
    
	public Company() {
	}

	public Company(String companyCode, String companyName, String acronym,
			String companyAddress, double vatRate, double whtRate,
			String financialStartDate, int financialNoOfMonths,
			String financialEndDate, int minimumPassword, int passwordExpiry,
			int sessionTimeout, String enforceAcqBudget,
			String enforcePmBudget, String enforceFuelAllocation,
			String requireQuarterlyPM, String quarterlySurplusCf,
			String userId, String processingStatus, double transWaitTime) {

		this.companyCode = companyCode;
		this.companyName = companyName;
		this.acronym = acronym;
		this.companyAddress = companyAddress;
		this.vatRate = vatRate;
		this.whtRate = whtRate;
		this.financialStartDate = financialStartDate;
		this.financialNoOfMonths = financialNoOfMonths;
		this.financialEndDate = financialEndDate;
		this.minimumPassword = minimumPassword;
		this.passwordExpiry = passwordExpiry;
		this.sessionTimeout = sessionTimeout;
		this.enforceAcqBudget = enforceAcqBudget;
		this.enforcePmBudget = enforcePmBudget;
		this.enforceFuelAllocation = enforceFuelAllocation;
		this.requireQuarterlyPM = requireQuarterlyPM;
		this.quarterlySurplusCf = quarterlySurplusCf;
		this.userId = userId;
		// this.processingStatus = processingStatus;
		this.transWaitTime = transWaitTime;
	}



//ganiyu's code,constructor with fedwhtRate field.

public Company(String companyCode, String companyName, String acronym,
			String companyAddress, double vatRate, double whtRate, double fedwhtRate,
			String financialStartDate, int financialNoOfMonths,
			String financialEndDate, int minimumPassword, int passwordExpiry,
			int sessionTimeout, String enforceAcqBudget,
			String enforcePmBudget, String enforceFuelAllocation,
			String requireQuarterlyPM, String quarterlySurplusCf,
			String userId, String processingStatus, double transWaitTime,int log_on) {

		this.companyCode = companyCode;
		this.companyName = companyName;
		this.acronym = acronym;
		this.companyAddress = companyAddress;
		this.vatRate = vatRate;
		this.whtRate = whtRate;
		this.financialStartDate = financialStartDate;
		this.financialNoOfMonths = financialNoOfMonths;
		this.financialEndDate = financialEndDate;
		this.minimumPassword = minimumPassword;
		this.passwordExpiry = passwordExpiry;
		this.sessionTimeout = sessionTimeout;
		this.enforceAcqBudget = enforceAcqBudget;
		this.enforcePmBudget = enforcePmBudget;
		this.enforceFuelAllocation = enforceFuelAllocation;
		this.requireQuarterlyPM = requireQuarterlyPM;
		this.quarterlySurplusCf = quarterlySurplusCf;
		this.userId = userId;
		// this.processingStatus = processingStatus;
		this.transWaitTime = transWaitTime;
		this.fedwhtRate = fedwhtRate;	//ganiyu's code
        this.log_on =log_on;
}

 public Company(String companyCode, String companyName, String acronym,
			String companyAddress, double vatRate, double whtRate,
			double fedwhtRate, String financialStartDate,
			int financialNoOfMonths, String financialEndDate,
			int minimumPassword, int passwordExpiry, int sessionTimeout,
			String enforceAcqBudget, String enforcePmBudget,
			String enforceFuelAllocation, String requireQuarterlyPM,
			String quarterlySurplusCf, String userId, String processingStatus,
			double transWaitTime, String logUserAudit, int log_on,
			String comp_delimiter, String password_upper,
			String password_lower, String password_numeric,
			String password_special) {
		super();
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.acronym = acronym;
		this.companyAddress = companyAddress;
		this.vatRate = vatRate;
		this.whtRate = whtRate;
		this.fedwhtRate = fedwhtRate;
		this.financialStartDate = financialStartDate;
		this.financialNoOfMonths = financialNoOfMonths;
		this.financialEndDate = financialEndDate;
		this.minimumPassword = minimumPassword;
		this.passwordExpiry = passwordExpiry;
		this.sessionTimeout = sessionTimeout;
		this.enforceAcqBudget = enforceAcqBudget;
		this.enforcePmBudget = enforcePmBudget;
		this.enforceFuelAllocation = enforceFuelAllocation;
		this.requireQuarterlyPM = requireQuarterlyPM;
		this.quarterlySurplusCf = quarterlySurplusCf;
		this.userId = userId;
		this.processingStatus = processingStatus;
		this.transWaitTime = transWaitTime;
		this.logUserAudit = logUserAudit;
		this.log_on = log_on;
		this.comp_delimiter = comp_delimiter;
		this.password_upper = password_upper;
		this.password_lower = password_lower;
		this.password_numeric = password_numeric;
		this.password_special = password_special;
	}

    public String getPassword_lower() {
        return password_lower;
    }

    public void setPassword_lower(String password_lower) {
        this.password_lower = password_lower;
    }

    public String getPassword_numeric() {
        return password_numeric;
    }

    public void setPassword_numeric(String password_numeric) {
        this.password_numeric = password_numeric;
    }

    public String getPassword_special() {
        return password_special;
    }

    public void setPassword_special(String password_special) {
        this.password_special = password_special;
    }

    public String getPassword_upper() {
        return password_upper;
    }

    public void setPassword_upper(String password_upper) {
        this.password_upper = password_upper;
    }

 
	/**
	 * @param companyCode
	 *            the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param acronym
	 *            the acronym to set
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * @return the acronym
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * @param companyAddress
	 *            the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	/**
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}

	/**
	 * @param vatRate
	 *            the vatRate to set
	 */
	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}

	/**
	 * @return the vatRate
	 */
	public double getVatRate() {
		return vatRate;
	}

	/**
	 * @param whtRate
	 *            the whtRate to set
	 */
	public void setWhtRate(double whtRate) {
		this.whtRate = whtRate;
	}

	/**
	 * @return the whtRate
	 */
	public double getWhtRate() {
		return whtRate;
	}

	//ganiyu's code
	
	public double getFedwhtRate() {
		return fedwhtRate;
	}
	
	public void setFedwhtRate(double fedwhtRate) {
		this.fedwhtRate = fedwhtRate;
	}
	
	
	/**
	 * @param financialStartDate
	 *            the financialStartDate to set
	 */
	public void setFinancialStartDate(String financialStartDate) {
		this.financialStartDate = financialStartDate;
	}

	/**
	 * @return the financialStartDate
	 */
	public String getFinancialStartDate() {
		return financialStartDate;
	}

	/**
	 * @param financialNoOfMonths
	 *            the financialNoOfMonths to set
	 */
	public void setFinancialNoOfMonths(int financialNoOfMonths) {
		this.financialNoOfMonths = financialNoOfMonths;
	}

	/**
	 * @return the financialNoOfMonths
	 */
	public int getFinancialNoOfMonths() {
		return financialNoOfMonths;
	}

	/**
	 * @param financialEndDate
	 *            the financialEndDate to set
	 */
	public void setFinancialEndDate(String financialEndDate) {
		this.financialEndDate = financialEndDate;
	}

	/**
	 * @return the financialEndDate
	 */
	public String getFinancialEndDate() {
		return financialEndDate;
	}

	/**
	 * @param minimumPassword
	 *            the minimumPassword to set
	 */
	public void setMinimumPassword(int minimumPassword) {
		this.minimumPassword = minimumPassword;
	}

	/**
	 * @return the minimumPassword
	 */
	public int getMinimumPassword() {
		return minimumPassword;
	}

	/**
	 * @param passwordExpiry
	 *            the passwordExpiry to set
	 */
	public void setPasswordExpiry(int passwordExpiry) {
		this.passwordExpiry = passwordExpiry;
	}

	/**
	 * @return the passwordExpiry
	 */
	public int getPasswordExpiry() {
		return passwordExpiry;
	}

	/**
	 * @param sessionTimeout
	 *            the sessionTimeout to set
	 */
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * @return the sessionTimeout
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * @param enforceAcqBudget
	 *            the enforceAcqBudget to set
	 */
	public void setEnforceAcqBudget(String enforceAcqBudget) {
		if (enforceAcqBudget != null || enforceAcqBudget != "")
			this.enforceAcqBudget = enforceAcqBudget;
		else
			this.enforceAcqBudget = "N";
	}

	/**
	 * @return the enforceAcqBudget
	 */
	public String getEnforceAcqBudget() {
		return enforceAcqBudget;
	}

	/**
	 * @param enforcePmBudget
	 *            the enforcePmBudget to set
	 */
	public void setEnforcePmBudget(String enforcePmBudget) {
		if (enforcePmBudget != null || enforcePmBudget != "")
			this.enforcePmBudget = enforcePmBudget;
		else
			this.enforcePmBudget = "N";
	}

	/**
	 * @return the enforcePmBudget
	 */
	public String getEnforcePmBudget() {
		return enforcePmBudget;
	}

	/**
	 * @param enforceFuelAllocation
	 *            the enforceFuelAllocation to set
	 */
	public void setEnforceFuelAllocation(String enforceFuelAllocation) {
		if (enforceFuelAllocation != null || enforceFuelAllocation != "")
			this.enforceFuelAllocation = enforceFuelAllocation;
		else
			this.enforceFuelAllocation = "N";
	}

	/**
	 * @return the enforceFuelAllocation
	 */
	public String getEnforceFuelAllocation() {
		return enforceFuelAllocation;
	}

	/**
	 * @param requireQuarterlyPM
	 *            the requireQuarterlyPM to set
	 */
	public void setRequireQuarterlyPM(String requireQuarterlyPM) {
		if (requireQuarterlyPM != null || requireQuarterlyPM != "")
			this.requireQuarterlyPM = requireQuarterlyPM;
		else
			this.requireQuarterlyPM = "N";
	}

	/**
	 * @return the requireQuarterlyPM
	 */
	public String getRequireQuarterlyPM() {
		return requireQuarterlyPM;
	}

	/**
	 * @param quarterlySurplusCf
	 *            the quarterlySurplusCf to set
	 */
	public void setQuarterlySurplusCf(String quarterlySurplusCf) {
		if (quarterlySurplusCf != null || quarterlySurplusCf != "")
			this.quarterlySurplusCf = quarterlySurplusCf;
		else
			this.quarterlySurplusCf = "N";
	}

	/**
	 * @return the quarterlySurplusCf
	 */
	public String getQuarterlySurplusCf() {
		return quarterlySurplusCf;
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
	 * @param processingStatus
	 *            the processingStatus to set
	 */
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	/**
	 * @return the processingStatus
	 */
	public String getProcessingStatus() {
		return processingStatus;
	}

	/**
	 * @param transWaitTime
	 *            the transWaitTime to set
	 */
	public void setTransWaitTime(double transWaitTime) {
		this.transWaitTime = transWaitTime;
	}

	/**
	 * @return the transWaitTime
	 */
	public double getTransWaitTime() {
		return transWaitTime;
	}

	/**
	 * @return the logUserAudit
	 */
	public String getLogUserAudit() {
		return logUserAudit;
	}

	/**
	 * @param logUserAudit the logUserAudit to set
	 */
	public void setLogUserAudit(String logUserAudit) {
		this.logUserAudit = logUserAudit;
	}

    /**
     * @return the attempt_logon
     */
    public int getLog_on() {
       
        return log_on;
    }

    /**
     * @param attempt_logon the attempt_logon to set
     */
    public void setLog_on(int log_on) {
        this.log_on = log_on;
    }

    /**
     * @return the comp_delimiter
     */
    public String getComp_delimiter() {
        return comp_delimiter;
    }

    /**
     * @param comp_delimiter the comp_delimiter to set
     */
    public void setComp_delimiter(String comp_delimiter) {
        this.comp_delimiter = comp_delimiter;
    }
  public String[][] getYesNoForCombo() {
        String[][] a = new String[2][3];

        a[0][0] = "Y";
        a[0][1] = "";
        a[0][2] = "Yes";

        a[1][0] = "N";
        a[1][1] = "";
        a[1][2] = "No";

        return a;
    }

     public String getProperties(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }

        if (vals != null) {
            for (int i = 0; i < vals.length; i++) {
                html = html + "<option value='" + vals[i][0] + "' " +
                       (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                       ">" + vals[i][2] + "</option> ";
            }

        }

        return html;
    }
 
    /**
     * @return the passwordLimit
     */
    public int getPasswordLimit() {
        return passwordLimit;
    }

    /**
     * @param passwordLimit the passwordLimit to set
     */
    public void setPasswordLimit(int passwordLimit) {
        this.passwordLimit = passwordLimit;
    }
    
    /**
     * @return the proofSessionTimeout
     */
    public int getProofSessionTimeout() {
        return proofSessionTimeout;
    }
    /**
     * @param proofSessionTimeout the proofSessionTimeout to set
     */    
    public void setProofSessionTimeout(int proofSessionTimeout) {
        this.proofSessionTimeout = proofSessionTimeout;
    }
    
   	/**
   	 * @param processingDate
   	 *            the processingDate to set
   	 */
   	public void setProcessingDate(String processingDate) {
   		this.processingDate = processingDate;
   	}

   	/**
   	 * @return the processingDate
   	 */
   	public String getProcessingDate() {
   		return processingDate;
   	}  
   	

	/**
	 * @param processingFrequency
	 *            the processingFrequency to set
	 */
	public void setProcessingFrequency(String processingFrequency) {
		this.processingFrequency = processingFrequency;
	}

	/**
	 * @return the processingFrequency
	 */
	public String getProcessingFrequency() {
		return processingFrequency;
	}

	/**
	 * @param nextProcessingDate
	 *            the nextProcessingDate to set
	 */
	public void setNextProcessingDate(String nextProcessingDate) {
		this.nextProcessingDate = nextProcessingDate;
	}

	/**
	 * @return the nextProcessingDate
	 */
	public String getNextProcessingDate() {
		return nextProcessingDate;
	}

	/**
	 * @param defaultBranch
	 *            the defaultBranch to set
	 */
	public void setDefaultBranch(String defaultBranch) {
		this.defaultBranch = defaultBranch;
	}

	/**
	 * @return the defaultBranch
	 */
	public String getDefaultBranch() {
		return defaultBranch;
	}

	/**
	 * @param branchName
	 *            the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param suspenseAcct
	 *            the suspenseAcct to set
	 */
	public void setSuspenseAcct(String suspenseAcct) {
		this.suspenseAcct = suspenseAcct;
	}

	/**
	 * @return the suspenseAcct
	 */
	public String getSuspenseAcct() {
		return suspenseAcct;
	}

	/**
	 * @param autoGenId
	 *            the autoGenId to set
	 */
	public void setAutoGenId(String autoGenId) {
		if (autoGenId != null || autoGenId != "")
			this.autoGenId = autoGenId;
		else
			this.autoGenId = "N";
	}

	/**
	 * @return the autoGenId
	 */
	public String getAutoGenId() {
		return autoGenId;
	}

	/**
	 * @param residualValue
	 *            the residualValue to set
	 */
	public void setResidualValue(String residualValue) {
		this.residualValue = residualValue;
	}

	/**
	 * @return the residualValue
	 */
	public String getResidualValue() {
		return residualValue;
	}

	/**
	 * @param depreciationMethod
	 *            the depreciationMethod to set
	 */
	public void setDepreciationMethod(String depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}

	/**
	 * @return the depreciationMethod
	 */
	public String getDepreciationMethod() {
		return depreciationMethod;
	}

	/**
	 * @param vatAccount
	 *            the vatAccount to set
	 */
	public void setVatAccount(String vatAccount) {
		this.vatAccount = vatAccount;
	}

	/**
	 * @return the vatAccount
	 */
	public String getVatAccount() {
		return vatAccount;
	}

	/**
	 * @param whtAccount
	 *            the whtAccount to set
	 */
	public void setWhtAccount(String whtAccount) {
		this.whtAccount = whtAccount;
	}

    public void setFedWhtAccount(String fedWhtAccount){
    this.fedWhtAccount = fedWhtAccount;
    }
	/**
	 * @return the whtAccount
	 */
	public String getWhtAccount() {
		return whtAccount;
	}

    public String getFedWhtAccount(){
    return fedWhtAccount;
    }
	/**
	 * @param pLDisposalAccount
	 *            the pLDisposalAccount to set
	 */
	public void setPLDisposalAccount(String pLDisposalAccount) {
		PLDisposalAccount = pLDisposalAccount;
	}

	/**
	 * @return the pLDisposalAccount
	 */
	public String getPLDisposalAccount() {
		return PLDisposalAccount;
	}

	/**
	 * @param pLDStatus
	 *            the pLDStatus to set
	 */
	public void setPLDStatus(String pLDStatus) {
		PLDStatus = pLDStatus;
	}

	/**
	 * @return the pLDStatus
	 */
	public String getPLDStatus() {
		return PLDStatus;
	}

	/**
	 * @param vatAcctStatus
	 *            the vatAcctStatus to set
	 */
	public void setVatAcctStatus(String vatAcctStatus) {
		this.vatAcctStatus = vatAcctStatus;
	}

	/**
	 * @return the vatAcctStatus
	 */
	public String getVatAcctStatus() {
		return vatAcctStatus;
	}

	/**
	 * @param whtAcctStatus
	 *            the whtAcctStatus to set
	 */
	public void setWhtAcctStatus(String whtAcctStatus) {
		this.whtAcctStatus = whtAcctStatus;
	}

	/**
	 * @return the whtAcctStatus
	 */
	public String getWhtAcctStatus() {
		return whtAcctStatus;
	}
public String getFedWhtAcctStatus(){
return fedWhtAcctStatus;
}

public void setFedWhtAcctStatus(String fedWhtAcctStatus){
this.fedWhtAcctStatus = fedWhtAcctStatus;
}

/**
	 * @param suspenseAcctStatus
	 *            the suspenseAcctStatus to set
	 */
	public void setSuspenseAcctStatus(String suspenseAcctStatus) {
		this.suspenseAcctStatus = suspenseAcctStatus;
	}

	/**
	 * @return the suspenseAcctStatus
	 */
	public String getSuspenseAcctStatus() {
		return suspenseAcctStatus;
	}

	/**
	 * @param sbuRequired
	 *            the sbuRequired to set
	 */
	public void setSbuRequired(String sbuRequired) {
		if (sbuRequired != null || sbuRequired != "")
			this.sbuRequired = sbuRequired;
		else
			this.sbuRequired = "N";
	}

	/**
	 * @return the sbuRequired
	 */
	public String getSbuRequired() {
		return sbuRequired;
	}

	/**
	 * @param sbuLevel
	 *            the sbuLevel to set
	 */
	public void setSbuLevel(String sbuLevel) {
		this.sbuLevel = sbuLevel;
	}

	/**
	 * @return the sbuLevel
	 */
	public String getSbuLevel() {
		return sbuLevel;
	}

	/**
	 * @param sysDate the sysDate to set
	 */
	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	/**
	 * @return the sysDate
	 */
	public String getSysDate() {
		return sysDate;
	}

	/**
	 * @return the assetSuspenseAcct
	 */
	public String getAssetSuspenseAcct() {
		if(assetSuspenseAcct.equalsIgnoreCase("null") || assetSuspenseAcct==null){
        assetSuspenseAcct ="";
        }

        return assetSuspenseAcct;
	}

	/**
	 * @param assetSuspenseAcct the assetSuspenseAcct to set
	 */
	public void setAssetSuspenseAcct(String assetSuspenseAcct) {
		this.assetSuspenseAcct = assetSuspenseAcct;
	}

    /**
     * @return the lpo_r
     */
    public String getLpo_r() {
        return lpo_r;
    }

    /**
     * @param lpo_r the lpo_r to set
     */
    public void setLpo_r(String lpo_r) {
        this.lpo_r = lpo_r;
    }

    /**
     * @return the bar_code_r
     */
    public String getBar_code_r() {
        return bar_code_r;
    }

    /**
     * @param bar_code_r the bar_code_r to set
     */
    public void setBar_code_r(String bar_code_r) {
        this.bar_code_r = bar_code_r;
    }

    /**
     * @return the cp_threshold
     */
    public double getCp_threshold() {
        return cp_threshold;
    }

    /**
     * @param cp_threshold the cp_threshold to set
     */
    public void setCp_threshold(double cp_threshold) {
        this.cp_threshold = cp_threshold;
    }

    /**
     * @return the deferAccountStatus
     */
    public String getDeferAccount() {
       if(deferAccount.equalsIgnoreCase("null") || deferAccount == null){
       deferAccount = "";
       }

        return deferAccount;
    }

    /**
     * @param deferAccountStatus the deferAccountStatus to set
     */
    public void setDeferAccount(String deferAccount) {

        this.deferAccount = deferAccount;
    }

    /**
     * @return the trans_threshold
     */
    public double getTrans_threshold() {
        return trans_threshold;
    }

    /**
     * @param trans_threshold the trans_threshold to set
     */
    public void setTrans_threshold(double trans_threshold) {
        this.trans_threshold = trans_threshold;
    }

    /**
     * @return the part_pay_acct
     */
    public String getPart_pay(){
        return part_pay;
    }

    /**
     * @param part_pay_acct the part_pay_acct to set
     */
    public void setPart_pay(String part_pay) {
        this.part_pay = part_pay;
    }

    /**
     * @return the asset_acq_status
     */
    public String getAsset_acq_status() {
        return asset_acq_status;
    }

    /**
     * @param asset_acq_status the asset_acq_status to set
     */
    public void setAsset_acq_status(String asset_acq_status) {
        this.asset_acq_status = asset_acq_status;
    }

    /**
     * @return the asset_defer_status
     */
    public String getAsset_defer_status() {
        return asset_defer_status;
    }

    /**
     * @param asset_defer_status the asset_defer_status to set
     */
    public void setAsset_defer_status(String asset_defer_status) {
        this.asset_defer_status = asset_defer_status;
    }

    /**
     * @return the part_pay_status
     */
    public String getPart_pay_status() {
        return part_pay_status;
    }

    /**
     * @param part_pay_status the part_pay_status to set
     */
    public void setPart_pay_status(String part_pay_status) {
        this.part_pay_status = part_pay_status;
    }


    /**
     * @return the thirdpartytransaction
     */
    public String getThirdpartytransaction() {
        return thirdpartytransaction;
    }

    /**
     * @param thirdpartytransaction the thirdpartytransaction to set
     */
    public void setThirdpartytransaction(String thirdpartytransaction) {
        this.thirdpartytransaction = thirdpartytransaction;
    }
    

    /**
     * @return the raiseEntry
     */
    public String getRaiseEntry() {
        return raiseEntry;
    }

    /**
     * @param raiseEntry the raiseEntry to set
     */
    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;
    }

    /**
     * @return the lossDisposalAcct
     */
    public String getLossDisposalAcct() {
//       if(lossDisposalAcct.equalsIgnoreCase("null") || lossDisposalAcct == null){
//    	   lossDisposalAcct = "";
//       }

        return lossDisposalAcct;
    }

    /**
     * @param lossDisposalAcct the lossDisposalAcct to set
     */
    public void setLossDisposalAcct(String lossDisposalAcct) {

        this.lossDisposalAcct = lossDisposalAcct;
    }

	/**
	 * @param lDAcctStatus
	 *            the lDAcctStatus to set
	 */
	public void setLDAcctStatus(String lDAcctStatus) {
		LDAcctStatus = lDAcctStatus;
	}

	/**
	 * @return the lDAcctStatus
	 */
	public String getLDAcctStatus() {
		return LDAcctStatus;
	}

    /**
     * @return the groupAssetAcct
     */
    public String getGroupAssetAcct() {
//       if(groupAssetAcct.equalsIgnoreCase("null") || groupAssetAcct == null){
//    	   groupAssetAcct = "";
//       }

        return groupAssetAcct;
    }

    /**
     * @param groupAssetAcct the groupAssetAcct to set
     */
    public void setGroupAssetAcct(String groupAssetAcct) {

        this.groupAssetAcct = groupAssetAcct;
    }

	/**
	 * @param gAAStatus
	 *            the gAAStatus to set
	 */
	public void setGAAStatus(String gAAStatus) {
		GAAStatus = gAAStatus;
	}

	/**
	 * @return the gAAStatus
	 */
	public String getGAAStatus() {
		return GAAStatus;
	} 


	/**
	 * @return the selfChargeAcct
	 */
	public String getSelfChargeAcct() {
//		if(selfChargeAcct.equalsIgnoreCase("null") || selfChargeAcct==null){
//			selfChargeAcct ="";
//        }

        return selfChargeAcct;
	}

	/**
	 * @param selfChargeAcct the selfChargeAcct to set
	 */
	public void setSelfChargeAcct(String selfChargeAcct) {
		this.selfChargeAcct = selfChargeAcct;
	}
	
    /**
     * @return the selfChargeStatus
     */
    public String getSelfChargeStatus() {
        return selfChargeStatus;
    }
    /**
     * @param selfChargeStatus the selfChargeStatus to set
     */
    public void setSelfChargeStatus(String selfChargeStatus) {
        this.selfChargeStatus = selfChargeStatus;
    }

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
//		if(databaseName.equalsIgnoreCase("null") || databaseName==null){
//			databaseName ="";
//        }

        return databaseName;
	}

	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
		 

	/**
	 * @return the recordType
	 */
	public String getRecordType() {
//		if(recordType.equalsIgnoreCase("null") || recordType==null){
//			recordType ="";
//        }

        return recordType;
	}

	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
		 	
}
