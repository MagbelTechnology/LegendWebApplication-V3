package magma.net.vao;

/**
 * <p>Title: AcquisitionBudget.java</p>
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
public class AcquisitionBudget {

    private String id;
    private String branchCode;
    private String categoryCode;
    private double firstQurterBudget;
    private double secondQurterBudget;
    private double thirdQurterBudget;
    private double allotedAmount;

    public AcquisitionBudget(String id, String branchCode, String categoryCode,
                             double firstQurterBudget,
                             double secondQuarterBudget,
                             double thirdQurterBudget,
                             double allotedAmount) {

        setId(id);
        setBranchCode(branchCode);
        setCategoryCode(categoryCode);
        setFirstQurterBudget(firstQurterBudget);
        setSecondQurterBudget(secondQurterBudget);
        setThirdQurterBudget(thirdQurterBudget);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setFirstQurterBudget(double firstQurterBudget) {
        this.firstQurterBudget = firstQurterBudget;
    }

    public void setSecondQurterBudget(double secondQurterBudget) {
        this.secondQurterBudget = secondQurterBudget;
    }

    public void setThirdQurterBudget(double thirdQurterBudget) {
        this.thirdQurterBudget = thirdQurterBudget;
    }

    public void setAllotedAmount(double allotedAmount) {
        this.allotedAmount = allotedAmount;
    }

    public String getId() {
        return id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public double getFirstQurterBudget() {
        return firstQurterBudget;
    }

    public double getSecondQurterBudget() {
        return secondQurterBudget;
    }

    public double getThirdQurterBudget() {
        return thirdQurterBudget;
    }

    public double getAllotedAmount() {
        return allotedAmount;
    }
}
