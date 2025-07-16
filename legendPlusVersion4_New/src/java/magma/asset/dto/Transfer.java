package magma.asset.dto;

public class Transfer {
    private int transferId;
    private String assetId;
    // private int deptId;
    private String deptName;
    // private int branchId;
    private String branchName;
    private int sectionId;
    private String sectionName;
    private String user;
    private String raiseEntry;
    private String transferDate;
    private int userId;
    //private String description;
    //private double cost;
    private String datePurchased;
    private String categoryName;
    //private String regNo;
    private String effDate;
    private int branchId;
    private int deptId;
    private int categoryId;
    private String regNo;
    private String assetStatus;
    private String description;
    private double cost;
    private String assetUser;
    private double monthDep;
    private String newAssetID;
    private String sbucode;
    private String branchCode;
    private String deptCode;
    private String sectionCode;
    
    public String getNew_email1() {
        return new_email1;
    }

    public void setNew_email1(String new_email1) {
        this.new_email1 = new_email1;
    }

    public String getNew_email2() {
        return new_email2;
    }

    public void setNew_email2(String new_email2) {
        this.new_email2 = new_email2;
    }

    public String getNew_who_to_rem() {
        return new_who_to_rem;
    }

    public void setNew_who_to_rem(String new_who_to_rem) {
        this.new_who_to_rem = new_who_to_rem;
    }

    public String getNew_who_to_rem2() {
        return new_who_to_rem2;
    }

    public void setNew_who_to_rem2(String new_who_to_rem2) {
        this.new_who_to_rem2 = new_who_to_rem2;
    }
    private int newBrancId;
    private int newDeptId;
    private int newSectionId;
    private String newsbucode;
    
    private String new_who_to_rem;
    private String new_who_to_rem2;
    private String new_email1;
    private String new_email2;

    private double nbv;
    private double accumDep;
    
    public Transfer(int transferId, String assetId, String deptName, String branchName,
            int sectionId, String sectionName, String user, String raiseEntry, String transferDate,
            int userId, String datePurchased, String categoryName, String effDate, int branchId, int deptId,
            int categoryId, String regNo, String assetStatus, String description, double cost, String assetUser,
            int newBrancId, int newDeptId, int newSectionId, String new_who_to_rem, String new_who_to_rem2,
            String new_email1, String new_email2)
    {
        this.transferId = transferId;
        this.assetId = assetId;
        this.deptName = deptName;
        this.branchName = branchName;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.user = user;
        this.raiseEntry = raiseEntry;
        this.transferDate = transferDate;
        this.userId = userId;
        this.datePurchased = datePurchased;
        this.categoryName = categoryName;
        this.effDate = effDate;
        this.branchId = branchId;
        this.deptId = deptId;
        this.categoryId = categoryId;
        this.regNo = regNo;
        this.assetStatus = assetStatus;
        this.description = description;
        this.cost = cost;
        this.assetUser = assetUser;
        this.newBrancId = newBrancId;
        this.newDeptId = newDeptId;
        this.newSectionId = newSectionId;
        this.new_who_to_rem = new_who_to_rem;
        this.new_who_to_rem2 = new_who_to_rem2;
        this.new_email1 = new_email1;
        this.new_email2 = new_email2;
    }

    public Transfer(int transferId, String assetId, int deptId, String deptName,
                    int branchId,
                    String branchName, int sectionId, String sectionName,
                    String user, String raiseEntry,
                    String transferDate, int userId, String categoryName,
                    String description, double cost,
                    String datePurchased, String regNo, String effDate,
                    int categoryId, String assetUser, String assetStatus,String sbucode) {
        setTransferId(transferId);
        setAssetId(assetId);
        setDeptId(deptId);
        setDeptName(deptName);
        setBranchId(branchId);
        setBranchName(branchName);
        setSectionId(sectionId);
        setSectionName(sectionName);
        setUser(user);
        setRaiseEntry(raiseEntry);
        setTransferDate(transferDate);
        setUserId(userId);
        setDescription(description);
        setCost(cost);
        setDatePurchased(datePurchased);
        setCategoryName(categoryName);
        setRegNo(regNo);
        setEffDate(effDate);
        //setBranchId(branchId);
        //setDeptId(deptId);
        setCategoryId(categoryId);
        //setRegNo(regNo);
        //setDescription(desc);
        //setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);
        setSbucode(sbucode);
    }



    public Transfer(int transferId, String assetId, int deptId, String deptName,
                    int branchId,
                    String branchName, int sectionId, String sectionName,
                    String user, String raiseEntry,
                    String transferDate, int userId, String categoryName,
                    String description, double cost,
                    String datePurchased, String regNo, String effDate,
                    int categoryId, String assetUser, String assetStatus,
                    int newBranch_Id, int newDept_Id, int newSection_Id,String sbucode,String newsbucode) {
        setTransferId(transferId);
        setAssetId(assetId);
        setDeptId(deptId);
        setDeptName(deptName);
        setBranchId(branchId);
        setBranchName(branchName);
        setSectionId(sectionId);
        setSectionName(sectionName);
        setUser(user);
        setRaiseEntry(raiseEntry);
        setTransferDate(transferDate);
        setUserId(userId);
        setDescription(description);
        setCost(cost);
        setDatePurchased(datePurchased);
        setCategoryName(categoryName);
        setRegNo(regNo);
        setEffDate(effDate);
        //setBranchId(branchId);
        //setDeptId(deptId);
        setCategoryId(categoryId);
        //setRegNo(regNo);
        //setDescription(desc);
        //setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);
        setNewBrancId(newBranch_Id);
        setNewDeptId(newDept_Id);
        setNewSectionId(newSection_Id);
        setSbucode(sbucode);
        setNewsbucode(newsbucode);
        
    }

    public Transfer(int transferId, String assetId, int deptId, String deptName,
                    int branchId,
                    String branchName, int sectionId, String sectionName,
                    String user, String raiseEntry,
                    String transferDate, int userId, String categoryName,
                    String description, double cost,
                    String datePurchased, String regNo, String effDate,
                    int categoryId, String assetUser, String assetStatus,
                    int newBranch_Id, int newDept_Id, int newSection_Id,String new_who_to_rem,String new_who_to_rem2,
                    String new_email1,String new_email2) {
        setTransferId(transferId);
        setAssetId(assetId);
        setDeptId(deptId);
        setDeptName(deptName);
        setBranchId(branchId);
        setBranchName(branchName);
        setSectionId(sectionId);
        setSectionName(sectionName);
        setUser(user);
        setRaiseEntry(raiseEntry);
        setTransferDate(transferDate);
        setUserId(userId);
        setDescription(description);
        setCost(cost);
        setDatePurchased(datePurchased);
        setCategoryName(categoryName);
        setRegNo(regNo);
        setEffDate(effDate);
        //setBranchId(branchId);
        //setDeptId(deptId);
        setCategoryId(categoryId);
        //setRegNo(regNo);
        //setDescription(desc);
        //setCost(cost);
        setAssetUser(assetUser);
        setAssetStatus(assetStatus);
        setNewBrancId(newBranch_Id);
        setNewDeptId(newDept_Id);
        setNewSectionId(newSection_Id);
        setNew_email1(new_email1);
        setNew_email2(new_email2);
        setNew_who_to_rem(new_who_to_rem);
        setNew_who_to_rem2(new_who_to_rem2);
    }


    public Transfer(int transferId, String assetId, int deptId, String deptName,
                    int branchId,
                    String branchName, int sectionId, String sectionName,
                    String user, String raiseEntry,
                    String transferDate, int userId, String categoryName,
                    String description, double cost,
                    String datePurchased, String regNo, String effDate,
                    int categoryId) {
        setTransferId(transferId);
        setAssetId(assetId);
        setDeptId(deptId);
        setDeptName(deptName);
        setBranchId(branchId);
        setBranchName(branchName);
        setSectionId(sectionId);
        setSectionName(sectionName);
        setUser(user);
        setRaiseEntry(raiseEntry);
        setTransferDate(transferDate);
        setUserId(userId);
        setDescription(description);
        setCost(cost);
        setDatePurchased(datePurchased);
        setCategoryName(categoryName);
        setRegNo(regNo);
        setEffDate(effDate);
        setCategoryId(categoryId);
    }



    public int getTransferId() {

        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRaiseEntry() {
        return raiseEntry;
    }

    public void setRaiseEntry(String raiseEntry) {
        this.raiseEntry = raiseEntry;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    /**
     * @return the newBrancId
     */
    public int getNewBrancId() {
        return newBrancId;
    }

    /**
     * @param newBrancId the newBrancId to set
     */
    public void setNewBrancId(int newBrancId) {
        this.newBrancId = newBrancId;
    }

    /**
     * @return the newDeptId
     */
    public int getNewDeptId() {
        return newDeptId;
    }

    /**
     * @param newDeptId the newDeptId to set
     */
    public void setNewDeptId(int newDeptId) {
        this.newDeptId = newDeptId;
    }

    /**
     * @return the newSectionId
     */
    public int getNewSectionId() {
        return newSectionId;
    }

    /**
     * @param newSectionId the newSectionId to set
     */
    public void setNewSectionId(int newSectionId) {
        this.newSectionId = newSectionId;
    }

    /**
     * @return the nbv
     */
    public double getNbv() {
        return nbv;
    }

    /**
     * @param nbv the nbv to set
     */
    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    /**
     * @return the accumDep
     */
    public double getAccumDep() {
        return accumDep;
    }

    /**
     * @param accumDep the accumDep to set
     */
    public void setAccumDep(double accumDep) {
        this.accumDep = accumDep;
    }

    /**
     * @return the monthDep
     */
    public double getMonthDep() {
        return monthDep;
    }

    /**
     * @param monthDep the monthDep to set
     */
    public void setMonthDep(double monthDep) {
        this.monthDep = monthDep;
    }

    /**
     * @return the newAssetID
     */
    public String getNewAssetID() {
        return newAssetID;
    }

    /**
     * @param newAssetID the newAssetID to set
     */
    public void setNewAssetID(String newAssetID) {
        this.newAssetID = newAssetID;
    }

    /**
     * @return the sbucode
     */
    public String getSbucode() {
        return sbucode;
    } 

    /**
     * @param sbucode the sbucode to set
     */
    public void setSbucode(String sbucode) {
        this.sbucode = sbucode;
    }    
    /**
     * @return the newsbucode
     */
    public String getNewsbucode() {
        return newsbucode;
    }

    /**
     * @param newsbucode the newsbucode to set
     */
    public void setNewsbucode(String newsbucode) {
        this.newsbucode = newsbucode;
    }     

    /**
     * @return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    } 

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }    

    /**
     * @return the deptCode
     */
    public String getDeptCode() {
        return deptCode;
    } 

    /**
     * @param deptCode the deptCode to set
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }    

    /**
     * @return the sectionCode
     */
    public String getSectionCode() {
        return sectionCode;
    } 

    /**
     * @param sectionCode the sectionCode to set
     */
    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }    
}