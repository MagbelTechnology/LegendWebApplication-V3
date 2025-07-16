package  com.magbel.ia.vao;


public  class  CustomerAccountTransactionHist
{

  
  String mtId;
  String  custAccountNo;
  String  customerType;
  String  accountType;
  String  effectiveDate;
  int userId = 0;
  String  createDate;
  int  tranCode  = 0;
  double  amount  = 0.00d;
  int  originId = 0;
  int superUserId  = 0;
  String   sysTranCode;
  String  originTracerNo;
  String  description;
  String  reference;
  int  currencyId = 0;
  double  buySysExchRate =  0.00d;
  double  sellSysExchRate =  0.00d;
  double  buyActExchRate =  0.00d;
  double  sellActExchRate =  0.00d;
  double  exchEquivAmount  =  0.00d;
  String  exchCurrencyCode;
  String   distCode;
  long    distPtId   =  0;
  int  branchNo = 0;
  int  origBranchNo  = 0;
  long  origHistPtId =  0;
  String  SBUCODE;
  String  offsetAcctNo;
  String  offsetAcctType;
  int     tranAnalysisCode = 0;
  double  balance = 0.00d;
  String  postingDate;
  
  
  
  public final  String  getPostingDate(){
       return    this.postingDate;	
	   }
	   
	   
	public  final  void  setPostingDate(String  PostingDate)
	{
	    this.postingDate  =  PostingDate;
	}
  
  public  final  double  getBalance(){
    return  balance;
	}
	
	
public  final void  setBalance(double Balance)
{
   this.balance  =  Balance; 
 }
  
public final String getAccountType() {
	return accountType;
}
public final void setAccountType(String accountType) {
	this.accountType = accountType;
}
public final double getAmount() {
	return amount;
}
public final void setAmount(double amount) {
	this.amount = amount;
}
public final int getBranchNo() {
	return branchNo;
}
public final void setBranchNo(int branchNo) {
	this.branchNo = branchNo;
}
public final double getBuyActExchRate() {
	return buyActExchRate;
}
public final void setBuyActExchRate(double buyActExchRate) {
	this.buyActExchRate = buyActExchRate;
}
public final double getBuySysExchRate() {
	return buySysExchRate;
}
public final void setBuySysExchRate(double buySysExchRate) {
	this.buySysExchRate = buySysExchRate;
}
public final String getCreateDate() {
	return createDate;
}
public final void setCreateDate(String createDate) {
	this.createDate = createDate;
}
public final int getCurrencyId() {
	return currencyId;
}
public final void setCurrencyId(int currencyId) {
	this.currencyId = currencyId;
}
public final String getCustAccountNo() {
	return custAccountNo;
}
public final void setCustAccountNo(String custAccountNo) {
	this.custAccountNo = custAccountNo;
}
public final String getCustomerType() {
	return customerType;
}
public final void setCustomerType(String customerType) {
	this.customerType = customerType;
}
public final String getDescription() {
	return description;
}
public final void setDescription(String description) {
	this.description = description;
}
public final String getDistCode() {
	return distCode;
}
public final void setDistCode(String distCode) {
	this.distCode = distCode;
}
public final long getDistPtId() {
	return distPtId;
}
public final void setDistPtId(long distPtId) {
	this.distPtId = distPtId;
}
public final String getEffectiveDate() {
	return effectiveDate;
}
public final void setEffectiveDate(String effectiveDate) {
	this.effectiveDate = effectiveDate;
}
public final String getExchCurrencyCode() {
	return exchCurrencyCode;
}
public final void setExchCurrencyCode(String exchCurrencyCode) {
	this.exchCurrencyCode = exchCurrencyCode;
}
public final double getExchEquivAmount() {
	return exchEquivAmount;
}
public final void setExchEquivAmount(double exchEquivAmount) {
	this.exchEquivAmount = exchEquivAmount;
}
public final String getMtId() {
	return mtId;
}
public final void setMtId(String mtId) {
	this.mtId = mtId;
}
public final String getOffsetAcctNo() {
	return offsetAcctNo;
}
public final void setOffsetAcctNo(String offsetAcctNo) {
	this.offsetAcctNo = offsetAcctNo;
}
public final String getOffsetAcctType() {
	return offsetAcctType;
}
public final void setOffsetAcctType(String offsetAcctType) {
	this.offsetAcctType = offsetAcctType;
}
public final int getOrigBranchNo() {
	return origBranchNo;
}
public final void setOrigBranchNo(int origBranchNo) {
	this.origBranchNo = origBranchNo;
}
public final long getOrigHistPtId() {
	return origHistPtId;
}
public final void setOrigHistPtId(long origHistPtId) {
	this.origHistPtId = origHistPtId;
}
public final int getOriginId() {
	return originId;
}
public final void setOriginId(int originId) {
	this.originId = originId;
}
public final String getOriginTracerNo() {
	return originTracerNo;
}
public final void setOriginTracerNo(String originTracerNo) {
	this.originTracerNo = originTracerNo;
}
public final String getReference() {
	return reference;
}
public final void setReference(String reference) {
	this.reference = reference;
}
public final String getSbuCode() {
	return SBUCODE;
}
public final void setSbuCode(String sbucode) {
	SBUCODE = sbucode;
}
public final double getSellActExchRate() {
	return sellActExchRate;
}
public final void setSellActExchRate(double sellActExchRate) {
	this.sellActExchRate = sellActExchRate;
}
public final double getSellSysExchRate() {
	return sellSysExchRate;
}
public final void setSellSysExchRate(double sellSysExchRate) {
	this.sellSysExchRate = sellSysExchRate;
}
public final int getSuperUserId() {
	return superUserId;
}
public final void setSuperUserId(int superUserId) {
	this.superUserId = superUserId;
}
public final String getSysTranCode() {
	return sysTranCode;
}
public final void setSysTranCode(String sysTranCode) {
	this.sysTranCode = sysTranCode;
}
public final int getTranAnalysisCode() {
	return tranAnalysisCode;
}
public final void setTranAnalysisCode(int tranAnalysisCode) {
	this.tranAnalysisCode = tranAnalysisCode;
}
public final int getTranCode() {
	return tranCode;
}
public final void setTranCode(int tranCode) {
	this.tranCode = tranCode;
}
public final int getUserId() {
	return userId;
}
public final void setUserId(int userId) {
	this.userId = userId;
}
  
  
 
 
}
  
  