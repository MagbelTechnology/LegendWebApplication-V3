package magma.net.vao;


import java.io.Serializable;

public class Component implements Serializable {

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


    public Component(String id, String parentAssetId, String parentCompId,
                     String assetId, String category, String description,
                     String serialNumber, String make, String model,
                     String additionalField, String status) {

        setId(id);
        setParentAssetId(parentAssetId);
        setParentCompId(parentCompId);
        setAssetId(assetId);
        setCategory(category);
        setDescription(description);
        setSerialNumber(serialNumber);
        setMake(make);
        setModel(model);
        setAdditionalField(additionalField);
        setStatus(status);

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentAssetId(String parentAssetId) {
        this.parentAssetId = parentAssetId;
    }

    public void setParentCompId(String parentCompId) {
        this.parentCompId = parentCompId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAdditionalField(String additionalField) {
        this.additionalField = additionalField;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getParentAssetId() {
        return this.parentAssetId;
    }

    public String getParentCompId() {
        return this.parentCompId;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public String getAdditionalField() {
        return this.additionalField;
    }

    public String getStatus() {
        return status;
    }

}