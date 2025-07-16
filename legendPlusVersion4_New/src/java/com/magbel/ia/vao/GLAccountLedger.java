package com.magbel.ia.vao;


public class GLAccountLedger
{
  
    private String id;
    private String accountType;
    private String accountNo;
    private String mask;
    private String levelNo;
    private String ledgerNo;
    private String description;
    private String totalingLevel;
    private String totalingLedgerNo;
    private String localTax;
    private String stateTax;
    private String fedTax;
    private String debitCredit;
    private String currency;
    private String effectiveDate;
    private String status;
    private String autoReplicate;
    private String reconAccount;
    private String ledgerType;
    private String drNAllowed;
    private String crNAllowed;
    private String totalIndicator;
    private String userId;
    private String parentId;

    public GLAccountLedger(String id, String accountType, String accountNo, String mask, String levelNo, String ledgerNo, String description, 
            String totalingLevel, String totalingLedgerNo, String localTax, String stateTax, String fedTax, String debitCredit, String currency, 
            String effectiveDate, String status, String autoReplicate, String reconAccount, String ledgerType, String drNAllowed, String crNAllowed, 
            String totalIndicator, String userId, String parentId)
    {
        this.id = id;
        this.accountType = accountType;
        this.accountNo = accountNo;
        this.mask = mask;
        this.levelNo = levelNo;
        this.ledgerNo = ledgerNo;
        this.description = description;
        this.totalingLevel = totalingLevel;
        this.totalingLedgerNo = totalingLedgerNo;
        this.localTax = localTax;
        this.stateTax = stateTax;
        this.fedTax = fedTax;
        this.debitCredit = debitCredit;
        this.currency = currency;
        this.effectiveDate = effectiveDate;
        this.status = status;
        this.autoReplicate = autoReplicate;
        this.reconAccount = reconAccount;
        this.ledgerType = ledgerType;
        this.drNAllowed = drNAllowed;
        this.crNAllowed = crNAllowed;
        this.totalIndicator = totalIndicator;
        this.userId = userId;
        this.parentId = parentId;
    }

    public String getAccountNo()
    {
        return accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public String getAutoReplicate()
    {
        return autoReplicate;
    }

    public void setAutoReplicate(String autoReplicate)
    {
        this.autoReplicate = autoReplicate;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getDebitCredit()
    {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit)
    {
        this.debitCredit = debitCredit;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public String getFedTax()
    {
        return fedTax;
    }

    public void setFedTax(String fedTax)
    {
        this.fedTax = fedTax;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLedgerNo()
    {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo)
    {
        this.ledgerNo = ledgerNo;
    }

    public String getLocalTax()
    {
        return localTax;
    }

    public void setLocalTax(String localTax)
    {
        this.localTax = localTax;
    }

    public String getMask()
    {
        return mask;
    }

    public void setMask(String mask)
    {
        this.mask = mask;
    }

    public String getLevelNo()
    {
        return levelNo;
    }

    public void setLevelNo(String levelNo)
    {
        this.levelNo = levelNo;
    }

    public String getReconAccount()
    {
        return reconAccount;
    }

    public void setReconAccount(String reconAccount)
    {
        this.reconAccount = reconAccount;
    }

    public String getStateTax()
    {
        return stateTax;
    }

    public void setStateTax(String stateTax)
    {
        this.stateTax = stateTax;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTotalingLedgerNo()
    {
        return totalingLedgerNo;
    }

    public void setTotalingLedgerNo(String totalingLedgerNo)
    {
        this.totalingLedgerNo = totalingLedgerNo;
    }

    public String getTotalingLevel()
    {
        return totalingLevel;
    }

    public void setTotalingLevel(String totalingLevel)
    {
        this.totalingLevel = totalingLevel;
    }

    public String getLedgerType()
    {
        return ledgerType;
    }

    public void setLedgerType(String ledgerType)
    {
        this.ledgerType = ledgerType;
    }

    public String getDebitNotAllowed()
    {
        return drNAllowed;
    }

    public void setDebitNotAllowed(String drNAllowed)
    {
        this.drNAllowed = drNAllowed;
    }

    public String getCreditNotAllowed()
    {
        return crNAllowed;
    }

    public void setCreditNotAllowed(String crNAllowed)
    {
        this.crNAllowed = crNAllowed;
    }

    public String getTotalIndicator()
    {
        return totalIndicator;
    }

    public void setTotalIndicator(String totalIndicator)
    {
        this.totalIndicator = totalIndicator;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
}
