package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: FuelAllocation.java</p>
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class FuelAllocation implements Serializable {

    private String assetMake;
    private String category;
    private int categoryCount;
    private String financialStartDate;
    private double amount;
    private String location;

    public FuelAllocation(String assetMake, String category, int categoryCount,
                          String financialStartDate, double amount,
                          String location) {

        setAssetMake(assetMake);
        setCategory(category);
        setCategoryCount(categoryCount);
        setFinancialStartDate(financialStartDate);
        setAmount(amount);
        setLocation(location);
    }

    public void setAssetMake(String assetMake) {
        this.assetMake = assetMake;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public void setFinancialStartDate(String financialStartDate) {
        this.financialStartDate = financialStartDate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public String getFinancialStartDate() {
        return financialStartDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }
}
