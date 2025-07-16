package magma.net.vao;


import java.io.Serializable;

public class ComponentView implements Serializable {

    private String id;
    private String parentAssetId;
    private String parentCompId;
    private String assetId;
    private String category;
    private String description;
    private String serialNumber;
    private String make;
    private String model;
    private String additionalField;
    private String status;
    private String component;
    private double cost; 
    private double assigned; 
    private double depreciation;
    
	public ComponentView(String id, String parentAssetId, String parentCompId, String assetId, String category, String description, String serialNumber, String make, String model, String additionalField, String status, String component, double cost, double assigned, double depreciation) {
		super();
		this.id = id;
		this.parentAssetId = parentAssetId;
		this.parentCompId = parentCompId;
		this.assetId = assetId;
		this.category = category;
		this.description = description;
		this.serialNumber = serialNumber;
		this.make = make;
		this.model = model;
		this.additionalField = additionalField;
		this.status = status;
		this.component = component;
		this.cost = cost;
		this.assigned = assigned;
		this.depreciation = depreciation;
		
		
	}

	public String getAdditionalField() {
		return additionalField;
	}

	public void setAdditionalField(String additionalField) {
		this.additionalField = additionalField;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public double getAssigned() {
		return assigned;
	}

	public void setAssigned(double assigned) {
		this.assigned = assigned;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(double depreciation) {
		this.depreciation = depreciation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getParentAssetId() {
		return parentAssetId;
	}

	public void setParentAssetId(String parentAssetId) {
		this.parentAssetId = parentAssetId;
	}

	public String getParentCompId() {
		return parentCompId;
	}

	public void setParentCompId(String parentCompId) {
		this.parentCompId = parentCompId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
    

    
}