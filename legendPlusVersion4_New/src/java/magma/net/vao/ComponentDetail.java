package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: DistributionDetail.java</p>
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
public class ComponentDetail implements Serializable {

    private String id;
    private String assetId;
    private String type;
    private String status;
    private String expenseAccount;
    private double costValue;
    private double amount;
    private String remainder;
    private double depAssigned;
    private String serialNumber;
    private String category;
    private String description;
    private String defaultValue;
    private String distType;
    private double percentage;
    private String creationDate;

	public ComponentDetail(String id, String category, String description, String status, String creationDate,String defaultValue,String distType, double amount, double percentage) {
		super();
		this.id = id;
		this.category = category;
		this.description = description;
		this.status = status;
		this.creationDate = creationDate;
		this.defaultValue = defaultValue;
		this.distType = distType;
		this.amount = amount;
		this.percentage = percentage;
	}    

	public ComponentDetail(String id, String assetId, String type, String status, String expenseAccount, double costValue, double amount, String remainder, double depAssigned,String serialNumber) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.type = type;
		this.status = status;
		this.expenseAccount = expenseAccount;
		this.costValue = costValue;
		this.amount = amount;
		this.remainder = remainder;
		this.depAssigned = depAssigned;
		this.serialNumber=serialNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public double getCostValue() {
		return costValue;
	}
	public void setCostValue(double costValue) {
		this.costValue = costValue;
	}
	public double getDepAssigned() {
		return depAssigned;
	}
	public void setDepAssigned(double depAssigned) {
		this.depAssigned = depAssigned;
	}
	public String getExpenseAccount() {
		return expenseAccount;
	}
	public void setExpenseAccount(String expenseAccount) {
		this.expenseAccount = expenseAccount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRemainder() {
		return remainder;
	}
	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDistType() {
		return distType;
	}
	public void setDistType(String distType) {
		this.distType = distType;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}	
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
}
