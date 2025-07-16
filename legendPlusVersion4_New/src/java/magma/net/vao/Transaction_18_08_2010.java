package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: Transaction.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class Transaction_18_08_2010 implements Serializable {
    private String id;
    private String debitAccount;
    private String creditAccount;
    private String creditNarration;
    private String debitNarration;
    private double amount;
    private String userId;
    private String superId;
    private String legacyCode;
    private String code;
    private String postingDate;
    private String effectiveDate;
    private String status;
    private String supervisor;
    private String rejectReason;
    private String batchId;
  private String fullName;
  private String assetId;
  private String drAcctType;
  private String crAcctType;
  private String drTranCode;
  private String crTranCode;
  private int branchId;
  private int deptId;
  private int categoryId;
  private String regNo;
  private String description;
  private String assetStatus;
  private String tranType;
  private String sentTime;
  private String transId;
  private String trans_sent_time;
  private int transaction_level;
  private int approval_level_count;

    public Transaction_18_08_2010(String id, String debitAccount, String creditAccount,
                       String creditNarration, String debitNarration,
                       double amount, String userId,
                       String superId, String legacyCode, String code,
                       String postingDate,
                       String effectiveDate, String status, String supervisor,
                       String rejectReason,
                       String batchId,String fullName,String assetId,String drAcctType,String crAcctType,
                       String drTranCode,String crTranCode,int branchId, int deptId,int categoryId,
                       String regNo,String desc,String assetStatus,String tranType,String sentTime) {

        setId(id);
        setDebitAccount(debitAccount);
        setCreditAccount(creditAccount);
        setCreditNarration(creditNarration);
        setDebitNarration(debitNarration);
        setAmount(amount);
        setUserId(userId);
        setSuperId(superId);
        setLegacyCode(legacyCode);
        setCode(code);
        setPostingDate(postingDate);
        setEffectiveDate(effectiveDate);
        setStatus(status);
        setSupervisor(supervisor);
        setRejectReason(rejectReason);
        setBatchId(batchId);
        setFullName(fullName);
        setAssetId(assetId);
        setDrAcctType(drAcctType);
        setCrAcctType(crAcctType);
        setDrTranCode(drTranCode);
        setCrTranCode(crTranCode);
        setBranchId(branchId);
        setDeptId(deptId);
        setCategoryId(categoryId);
        setRegNo(regNo);
        setDescription(desc);
        setAssetStatus(assetStatus);
        setTranType(tranType);
        setSentTime(sentTime);
    }





        public Transaction_18_08_2010(String id, String debitAccount, String creditAccount,
                       String creditNarration, String debitNarration,
                       double amount, String userId,
                       String superId, String legacyCode, String code,
                       String postingDate,
                       String effectiveDate, String status, String supervisor,
                       String rejectReason,
                       String batchId,String fullName) {

        setId(id);
        setDebitAccount(debitAccount);
        setCreditAccount(creditAccount);
        setCreditNarration(creditNarration);
        setDebitNarration(debitNarration);
        setAmount(amount);
        setUserId(userId);
        setSuperId(superId);
        setLegacyCode(legacyCode);
        setCode(code);
        setPostingDate(postingDate);
        setEffectiveDate(effectiveDate);
        setStatus(status);
        setSupervisor(supervisor);
        setRejectReason(rejectReason);
        setBatchId(batchId);
        setFullName(fullName);

    }


        //ganiyu transaction constructor
public Transaction_18_08_2010(String debitAccount,
						 String creditAccount, String creditNarration,String debitNarration,double amount,
						String postingDate,String fullName, String assetId,String description, String trans_Id, String trans_sent_time,String tran_type,int transactionLevel,int approvalLevelCount){

        setAssetId(assetId);
        setDebitAccount(debitAccount);
        setCreditAccount(creditAccount);
        setCreditNarration(creditNarration);
        setDebitNarration(debitNarration);
        setAmount(amount);
        setDescription(description);
        //setUserId(userId);
        //setSuperId(superId);
        //setLegacyCode(legacyCode);
       // setCode(code);
        setPostingDate(postingDate);
       // setEffectiveDate(effectiveDate);
        ///setStatus(status);
        //setSupervisor(supervisor);
        //setRejectReason(rejectReason);
        //setBatchId(batchId);
       setTrans_sent_time(trans_sent_time);
        setTransId(trans_Id);
        setTranType(tran_type);
        setFullName(fullName);
        setTransaction_level(transactionLevel);
        setApproval_level_count(approvalLevelCount);
}


    public void setId(String id) {
        this.id = id;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setCreditNarration(String creditNarration) {
        this.creditNarration = creditNarration;
    }

    public void setDebitNarration(String debitNarration) {
        this.debitNarration = debitNarration;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public void setLegacyCode(String legacyCode) {
        this.legacyCode = legacyCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getId() {
        return id;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public String getCreditNarration() {
        return creditNarration;
    }

    public String getDebitNarration() {
        return debitNarration;
    }

    public double getAmount() {
        
        return amount;
    }

    public String getUserId() {
        return userId;
    }

    public String getSuperId() {
        return superId;
    }

    public String getLegacyCode() {
        return legacyCode;
    }

    public String getCode() {
        return code;
    }

    public String getPostingDate() {
       //if(postingDate.equalsIgnoreCase("null")){postingDate="";}
        return postingDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getStatus() {
        return status;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public String getBatchId() {
        return batchId;
    }

  public String getFullName()
  {
    return fullName;
  }

  public void setFullName(String fullName)
  {
    this.fullName = fullName;
  }

  public String getAssetId()
  {
      //if(assetId.equalsIgnoreCase("null")){assetId="";}
    return assetId;
  }

  public void setAssetId(String assetId)
  {
    this.assetId = assetId;
  }

  public String getDrAcctType()
  {
    return drAcctType;
  }

  public void setDrAcctType(String drAcctType)
  {
    this.drAcctType = drAcctType;
  }

  public String getCrAcctType()
  {
    return crAcctType;
  }

  public void setCrAcctType(String crAcctType)
  {
    this.crAcctType = crAcctType;
  }

  public String getDrTranCode()
  {
    return drTranCode;
  }

  public void setDrTranCode(String drTranCode)
  {
    this.drTranCode = drTranCode;
  }

  public String getCrTranCode()
  {
    return crTranCode;
  }

  public void setCrTranCode(String crTranCode)
  {
    this.crTranCode = crTranCode;
  }

  public int getBranchId()
  {
    return branchId;
  }

  public void setBranchId(int branchId)
  {
    this.branchId = branchId;
  }

  public int getDeptId()
  {
    return deptId;
  }

  public void setDeptId(int deptId)
  {
    this.deptId = deptId;
  }

  public int getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId(int categoryId)
  {
    this.categoryId = categoryId;
  }

  public String getRegNo()
  {
    return regNo;
  }

  public void setRegNo(String regNo)
  {
    this.regNo = regNo;
  }

  public String getDescription()
  {
      //if(description.equalsIgnoreCase("null")){description="";}
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getAssetStatus()
  {
    return assetStatus;
  }

  public void setAssetStatus(String assetStatus)
  {
    this.assetStatus = assetStatus;
  }

  public String getTranType()
  {
    return tranType;
  }

  public void setTranType(String tranType)
  {
    this.tranType = tranType;
  }

  public String getSentTime()
  {
      //if(sentTime.equalsIgnoreCase("null")){sentTime="";}
    return sentTime;
  }

  public void setSentTime(String sentTime)
  {
    this.sentTime = sentTime;
  }

    /**
     * @return the transId
     */
    public String getTransId() {
        //if(transId.equalsIgnoreCase("null")){transId="";}
        return transId;
    }

    /**
     * @param transId the transId to set
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    /**
     * @return the trans_sent_time
     */
    public String getTrans_sent_time() {
        return trans_sent_time;
    }

    /**
     * @param trans_sent_time the trans_sent_time to set
     */
    public void setTrans_sent_time(String trans_sent_time) {
        this.trans_sent_time = trans_sent_time;
    }

    /**
     * @return the transaction_level
     */
    public int getTransaction_level() {
        return transaction_level;
    }

    /**
     * @param transaction_level the transaction_level to set
     */
    public void setTransaction_level(int transaction_level) {
        this.transaction_level = transaction_level;
    }

    /**
     * @return the approval_level_count
     */
    public int getApproval_level_count() {
        return approval_level_count;
    }

    /**
     * @param approval_level_count the approval_level_count to set
     */
    public void setApproval_level_count(int approval_level_count) {
        this.approval_level_count = approval_level_count;
    }
}
