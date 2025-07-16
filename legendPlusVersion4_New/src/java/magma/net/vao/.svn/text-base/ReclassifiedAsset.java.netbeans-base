/*
 am_assetReclassification
 [Reclassify_ID] [int] IDENTITY (1, 1) NOT NULL ,
 [asset_id] [varchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
 [old_category_id] [int] NULL ,
 [new_category_id] [int] NULL ,
 [old_depr_rate] [decimal](18, 2) NULL ,
 [new_depr_rate] [decimal](18, 2) NULL ,
 [old_accum_dep] [decimal](18, 2) NULL ,
 [new_accum_dep] [decimal](18, 2) NULL ,
 [reclassify_date] [datetime] NULL ,
 [reclassify_reason] [varchar] (150) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
 [User_ID] [int] NULL ,
 [recalc_depr] [char] (1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
 [raise_entry]
 */
package magma.net.vao;

public class ReclassifiedAsset implements java.io.Serializable {

    private String id;
    private String assetId;
    private int oldCategory;
    private int newCategory;
    private double oldDepRate;
    private double newDepRate;
    private double oldAccumDep;
    private double newAccumDep;
    private String date;
    private String reason;
    private int userId;
    private String recalculateDepreciation;
    private String raiseEntry;

    public ReclassifiedAsset(String id, String assetId, int oldCategory,
                             int newCategory, double oldDepRate,
                             double newDepRate, double oldAccumDep,
                             double newAccumDep, String date,
                             String reason, int userId,
                             String recalculateDepreciation, String raiseEntry) {

        setId(id);
        setAssetId(assetId);
        setOldCategory(oldCategory);
        setNewCategory(newCategory);
        setOldDepRate(oldDepRate);
        setNewDepRate(newDepRate);
        setOldAccumDep(oldAccumDep);
        setNewAccumDep(newAccumDep);
        setDate(date);
        setReason(reason);
        setUserId(userId);
        setRecalculateDepreciation(recalculateDepreciation);
        setRaiseEntry(raiseEntry);

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setOldCategory(int oldCategory) {
        this.oldCategory = oldCategory;
    }

    public void setNewCategory(int newCategory) {
        this.newCategory = newCategory;
    }

    public void setOldDepRate(double oldDepRate) {
        this.oldDepRate = oldDepRate;
    }

    public void setNewDepRate(double newDepRate) {
        this.newDepRate = newDepRate;
    }

    public void setOldAccumDep(double oldAccumDep) {
        this.oldAccumDep = oldAccumDep;
    }

    public void setNewAccumDep(double newAccumDep) {
        this.newAccumDep = newAccumDep;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRecalculateDepreciation(String recalculateDepreciation) {
        this.recalculateDepreciation = recalculateDepreciation;
    }

    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;
    }

    public String getId() {
        return id;
    }

    public String getAssetId() {
        return assetId;
    }

    public int getOldCategory() {
        return oldCategory;
    }

    public int getNewCategory() {
        return newCategory;
    }

    public double getOldDepRate() {
        return oldDepRate;
    }

    public double getNewDepRate() {
        return newDepRate;
    }

    public double getOldAccumDep() {
        return oldAccumDep;
    }

    public double getNewAccumDep() {
        return newAccumDep;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public int getUserId() {
        return userId;
    }

    public String getRecalculateDepreciation() {
        return recalculateDepreciation;
    }

    public String getRaiseEntry() {
        return raiseEntry;
    }


}
