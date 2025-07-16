package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: fileName.java</p>
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
public class BranchAllocation implements Serializable {

    private String id;
    private String branchCode;
    private String branchName;
    private double janAmount;
    private double febAmount;
    private double marAmount;
    private double aprAmount;
    private double mayAmount;
    private double junAmount;
    private double julAmount;
    private double augAmount;
    private double sepAmount;
    private double octAmount;
    private double novAmount;
    private double decAmount;
    private double firstQauterAmount;
    private double secondQuaterAmount;
    private double thirdQauterAmount;
    private double fourthQuaterAmount;
    private double totalAmount;
    private double balanceAmount;
    private String type;
    private String category;
    private String categoryName;
    private String groupId;
    private String description;
    private String subcategory;
    private String subcategoryName;
    private String sbuCode;
    private String extrabudget;

    public BranchAllocation(String id, String branchCode,String category,String subcategory,String sbuCode, String branchName,String categoryName,String subcategoryName,
    						double janAmount,double febAmount,double marAmount,double aprAmount,double mayAmount,double junAmount,
    						double julAmount,double augAmount,double sepAmount,double octAmount,double novAmount,double decAmount,
                            double firstQauterAmount, double secondQuaterAmount, double thirdQauterAmount, double fourthQuaterAmount,
                            double totalAmount, double balanceAmount,
                            String type, String extrabudget
            ) { 

        setId(id);
        setBranchCode(branchCode);
        setCategory(category);
        setSubcategory(subcategory);
        setSbuCode(sbuCode);
        setBranchName(branchName);
        setCategoryName(categoryName);
        setSubcategoryName(subcategoryName);
        setJanAmount(janAmount);
        setFebAmount(febAmount);
        setMarAmount(marAmount);
        setAprAmount(aprAmount);
        setMayAmount(mayAmount);
        setJunAmount(junAmount);
        setJulAmount(julAmount);
        setAugAmount(augAmount);
        setSepAmount(sepAmount);
        setOctAmount(octAmount);
        setNovAmount(novAmount);
        setDecAmount(decAmount);
        setFirstQauterAmount(firstQauterAmount);
        setSecondQuaterAmount(secondQuaterAmount);
        setThirdQauterAmount(thirdQauterAmount);
        setFourthQuaterAmount(fourthQuaterAmount);
        setTotalAmount(totalAmount);
        setBalanceAmount(balanceAmount);
        setType(type);
        setExtrabudget(extrabudget);
    }

    public BranchAllocation(String id, String branchCode,String category,String sbuCode, String branchName,String categoryName,
                            double firstQauterAmount, double secondQuaterAmount, double thirdQauterAmount, double fourthQuaterAmount,
                            double totalAmount, double balanceAmount,
                            String type
            ) { 
        
        setId(id);  
        setBranchCode(branchCode);
        setCategory(category);
        setSubcategory(subcategory);
        setSbuCode(sbuCode);
        setBranchName(branchName);
        setCategoryName(categoryName);
        setSubcategoryName(subcategoryName);
        setFirstQauterAmount(firstQauterAmount);
        setSecondQuaterAmount(secondQuaterAmount);
        setThirdQauterAmount(thirdQauterAmount);
        setFourthQuaterAmount(fourthQuaterAmount);
        setTotalAmount(totalAmount);
        setBalanceAmount(balanceAmount);
        setType(type);
    }


    
    public BranchAllocation(String id, String branchCode,String category,String subcategory, String branchName,String categoryName,String subcategoryName,
							double janAmount,double febAmount,double marAmount,double aprAmount,double mayAmount,double junAmount,
							double julAmount,double augAmount,double sepAmount,double octAmount,double novAmount,double decAmount,
    						double firstQauterAmount, double secondQuaterAmount, double thirdQauterAmount, double fourthQuaterAmount,
                            double totalAmount, double balanceAmount,
                            String type
            ) { 

        setId(id); 
        setBranchCode(branchCode); 
        setCategory(category);
        setSubcategory(subcategory);
        setBranchName(branchName);
        setCategoryName(categoryName);
        setSubcategoryName(subcategoryName);
        setJanAmount(janAmount);
        setFebAmount(febAmount);
        setMarAmount(marAmount);
        setAprAmount(aprAmount);
        setMayAmount(mayAmount);
        setJunAmount(junAmount);
        setJulAmount(julAmount);
        setAugAmount(augAmount);
        setSepAmount(sepAmount);
        setOctAmount(octAmount);  
        setNovAmount(novAmount);
        setDecAmount(decAmount);        
        setFirstQauterAmount(firstQauterAmount);
        setSecondQuaterAmount(secondQuaterAmount);
        setThirdQauterAmount(thirdQauterAmount);
        setFourthQuaterAmount(fourthQuaterAmount);
        setTotalAmount(totalAmount);
        setBalanceAmount(balanceAmount);
        setType(type);
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setFirstQauterAmount(double firstQauterAmount) {
        this.firstQauterAmount = firstQauterAmount;
    }

    public void setSecondQuaterAmount(double secondQuaterAmount) {
        this.secondQuaterAmount = secondQuaterAmount;
    }

    public void setThirdQauterAmount(double thirdQauterAmount) {
        this.thirdQauterAmount = thirdQauterAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setFourthQuaterAmount(double fourthQuaterAmount) {
        this.fourthQuaterAmount = fourthQuaterAmount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public void setSbuCode(String sbuCode) {
        this.sbuCode = sbuCode;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getBranchCode() {
        return branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public double getFirstQauterAmount() {
        return firstQauterAmount;
    }

    public double getSecondQuaterAmount() {
        return secondQuaterAmount;
    }

    public double getThirdQauterAmount() {
        return thirdQauterAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }

    public double getFourthQuaterAmount() {
        return fourthQuaterAmount;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryName() {
        return categoryName;
    }    
    
    public String getSbuCode() {
        return sbuCode;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }    

    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }    
    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public double getJanAmount() {
        return janAmount;
    }
    public void setJanAmount(double janAmount) {
        this.janAmount = janAmount;
    }
    public double getFebAmount() {
        return febAmount;
    }
    public void setFebAmount(double febAmount) {
        this.febAmount = febAmount;
    }
    public double getMarAmount() {
        return marAmount;
    }
    public void setMarAmount(double marAmount) {
        this.marAmount = marAmount;
    }
    public double getAprAmount() {
        return aprAmount;
    }
    public void setAprAmount(double aprAmount) {
        this.aprAmount = aprAmount;
    }
    public double getMayAmount() {
        return mayAmount;
    }
    public void setMayAmount(double mayAmount) {
        this.mayAmount = mayAmount;
    }
    public double getJunAmount() {
        return junAmount;
    }
    public void setJunAmount(double junAmount) {
        this.junAmount = junAmount;
    }
    public double getJulAmount() {
        return julAmount;
    }
    public void setJulAmount(double julAmount) {
        this.julAmount = julAmount;
    }
    public double getAugAmount() {
        return augAmount;
    }
    public void setAugAmount(double augAmount) {
        this.augAmount = augAmount;
    }
    public double getSepAmount() {
        return sepAmount;
    }
    public void setSepAmount(double sepAmount) {
        this.sepAmount = sepAmount;
    }
    public double getOctAmount() {
        return octAmount;
    }
    public void setOctAmount(double octAmount) {
        this.octAmount = octAmount;
    }
    public double getNovAmount() {
        return novAmount;
    }
    public void setNovAmount(double novAmount) {
        this.novAmount = novAmount;
    }
    public double getDecAmount() {
        return decAmount;
    }
    public void setDecAmount(double decAmount) {
        this.decAmount = decAmount;
    }
    public String getExtrabudget() {
        return extrabudget;
    }

    public void setExtrabudget(String extrabudget) {
        this.extrabudget = extrabudget;
    }
    
}
