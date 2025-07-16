package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2020</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Lekan.Matanmi
 * @version 3.0
 */
public class FMppmAllocation implements Serializable {

    private String id;
    private String branchCode;
    private String categoryCode;
    private String subCategoryCode;
    private String branchName;
    private String firstQauterDate;
    private String secondQuaterDate;
    private String thirdQauterDate;
    private String fourthQuaterDate;
    private double totalAmount;
    private double balanceAmount;
    private String type;
    private String vendorCode;
    private String vendorName;
    private String groupId;
    private String description;
    private String lastServiceDate;
    private String fq_DueStatus1;
    private String fq_DueStatus2;
    private String fq_DueStatus3;
    private String fq_DueStatus4;
    private String fq_Due1;
    private String fq_Due2;
    private String fq_Due3;
    private String fq_Due4;
    private String fq_Status1;
    private String fq_Status2;
    private String fq_Status3;
    private String fq_Status4;
    private String status;
    private String postingDate;
    private String sbuCode;
    private String transId;
    private String duration;
    private String zoneCode;
    private String zoneName;

    public FMppmAllocation(String id, String branchCode,String vendorCode,String sbuCode, 
    						String branchName,String vendorName,
                            String description, String firstQauterDate,
                            String secondQuaterDate, String thirdQauterDate,
                            String fourthQuaterDate,String type) { 

        setId(id);
        setBranchCode(branchCode);
        setVendorCode(vendorCode);
        setSbuCode(sbuCode);
        setBranchName(branchName);
        setVendorName(vendorName);
        setDescription(description);
        setFirstQauterDate(firstQauterDate);
        setSecondQuaterDate(secondQuaterDate);
        setThirdQauterDate(thirdQauterDate);
        setFourthQuaterDate(fourthQuaterDate);
        setType(type);
    }

    public FMppmAllocation(String id,String transId, String branchCode,String categoryCode,String subCategoryCode,String vendorCode, 
                            String description,String lastServiceDate, String fq_DueStatus1,String fq_DueStatus2,String fq_DueStatus3,String fq_DueStatus4,
                            String fq_Status1,String fq_Status2,String fq_Status3,String fq_Status4,String type,
                            String status,String postingDate) { 

        setId(id);
        setTransId(transId);
        setBranchCode(branchCode);
        setVendorCode(vendorCode);
        setCategoryCode(categoryCode);
        setSubCategoryCode(subCategoryCode);
        setDescription(description);
        setVendorCode(vendorCode);
        setFq_DueStatus1(fq_DueStatus1);
        setFq_DueStatus2(fq_DueStatus2);
        setFq_DueStatus3(fq_DueStatus3);
        setFq_DueStatus4(fq_DueStatus4);
        setLastServiceDate(lastServiceDate);
        setFq_Due1(fq_Due1);
        setFq_Due2(fq_Due2);
        setFq_Due3(fq_Due3);
        setFq_Due4(fq_Due4);
        setFq_Status1(fq_Status1);
        setFq_Status2(fq_Status2);
        setFq_Status3(fq_Status3);
        setFq_Status4(fq_Status4);
        setType(type);
        setStatus(status);
        setPostingDate(postingDate);
    }

    public FMppmAllocation() {
		// TODO Auto-generated constructor stub
	}

	public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setFirstQauterDate(String firstQauterDate) {
        this.firstQauterDate = firstQauterDate;
    }

    public void setSecondQuaterDate(String secondQuaterDate) {
        this.secondQuaterDate = secondQuaterDate;
    }

    public void setThirdQauterDate(String thirdQauterDate) {
        this.thirdQauterDate = thirdQauterDate;
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

    public void setFourthQuaterDate(String fourthQuaterDate) {
        this.fourthQuaterDate = fourthQuaterDate;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getFirstQauterDate() {
        return firstQauterDate;
    }

    public String getSecondQuaterDate() {
        return secondQuaterDate;
    }

    public String getThirdQauterDate() {
        return thirdQauterDate;
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

    public String getFourthQuaterDate() {
        return fourthQuaterDate;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getVendorName() {
        return vendorName;
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
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public String getCategoryCode() {
        return categoryCode;
    }
    
    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }
    
    public String getSubCategoryCode() {
        return subCategoryCode;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }    
    
    public void setFq_DueStatus1(String fq_DueStatus1) {
        this.fq_DueStatus1 = fq_DueStatus1;
    }
    
    public String getFq_DueStatus1() {
        return fq_DueStatus1; 
    }  
    
    public void setFq_DueStatus2(String fq_DueStatus2) {
        this.fq_DueStatus2 = fq_DueStatus2;
    }
    
    public String getFq_DueStatus2() {
        return fq_DueStatus2;
    }  
    
    public void setFq_DueStatus3(String fq_DueStatus3) {
        this.fq_DueStatus3 = fq_DueStatus3;
    }
    
    public String getFq_DueStatus3() {
        return fq_DueStatus3;
    }  
    
    public void setFq_DueStatus4(String fq_DueStatus4) {
        this.fq_DueStatus4 = fq_DueStatus4;
    }
    
    public String getFq_DueStatus4() {
        return fq_DueStatus4;
    }  
    
public void setLastServiceDate(String lastServiceDate) {
    this.lastServiceDate = lastServiceDate;
}

public String getLastServiceDate() {
    return lastServiceDate;
}    

    public void setFq_Due1(String fq_Due1) {
        this.fq_Due1 = fq_Due1;
    }
    
    public String getFq_Due1() {
        return fq_Due1;
    }    
    
    public void setFq_Due2(String fq_Due2) {
        this.fq_Due2 = fq_Due2;
    }
    
    public String getFq_Due2() {
        return fq_Due2;
    }  
    
    public void setFq_Due3(String fq_Due3) {
        this.fq_Due3 = fq_Due3;
    }
    
    public String getFq_Due3() {
        return fq_Due3;
    }  
    
    public void setFq_Due4(String fq_Due4) {
        this.fq_Due4 = fq_Due4;
    }
    
    public String getFq_Due4() {
        return fq_Due4;
    }
    
    public void setFq_Status1(String fq_Status1) {
        this.fq_Status1 = fq_Status1;
    }
    
    public String getFq_Status1() {
        return fq_Status1;
    }    
    
    public void setFq_Status2(String fq_Status2) {
        this.fq_Status2 = fq_Status2;
    }
    
    public String getFq_Status2() {
        return fq_Status2;
    }    
    
    public void setFq_Status3(String fq_Status3) {
        this.fq_Status3 = fq_Status3;
    }
    
    public String getFq_Status3() {
        return fq_Status3;
    }    
    
    public void setFq_Status4(String fq_Status4) {
        this.fq_Status4 = fq_Status4;
    }
    
    public String getFq_Status4() {
        return fq_Status4;
    }        
    
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }
    
    public String getPostingDate() {
        return postingDate;
    } 
    
    public void setTransId(String transId) {
        this.transId = transId;
    }
    
    public String getTransId() {
        return transId;
    }     
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getDuration() {
        return duration;
    }     
    
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
    
    public String getZoneCode() {
        return zoneCode;
    }      
    
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    
    public String getZoneName() {
        return zoneName;
    }           
}
