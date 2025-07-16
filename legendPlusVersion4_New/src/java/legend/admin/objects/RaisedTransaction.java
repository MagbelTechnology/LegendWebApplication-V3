package legend.admin.objects;

public class RaisedTransaction {
	private String id;;
	private String Description;
	private String debitAccount;
	private String creditAccount;
	private double amount;
	private String iso;
	private String asset_id;
	private String createDate;
	private String page1;
	public RaisedTransaction(String id, String description, String debitAccount, String creditAccount, double amount, String iso, String asset_id, String createDate, String page1) {
		super();
		this.id = id;
		Description = description;
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.iso = iso;
		this.asset_id = asset_id;
		this.createDate = createDate;
		this.page1 = page1;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreditAccount() {
		return creditAccount;
	}
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
	public String getDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIso() {
		return iso;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
	public String getPage1() {
		return page1;
	}
	public void setPage1(String page1) {
		this.page1 = page1;
	}
	
	
	  
}
