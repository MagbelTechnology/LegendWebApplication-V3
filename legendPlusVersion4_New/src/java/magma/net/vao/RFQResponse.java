/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Developers
 */
public class RFQResponse {
       private String  ReqnID;
    private String UserID;
    private String Status;
    private String Company_Code;
    private String Vendor_Code;
    private String Quoted_Vendor_code;
    private String QuotedBy;
    private String Job_Title;
    private String Email;
    private String DaysQuote_Valid;
    private String Remark;
    private String Replied_Date;
    private String Replied_By;
    
    private double Calc_Amount;
    private double Total_Cost;
    private double Discount_Amount;
    private String DiscountType;

       public RFQResponse(){}

    public RFQResponse(String  ReqnID,
       String  UserID,
       String  Status,
       String  Company_Code,
       String  Vendor_Code,
       String  Quoted_Vendor_code,
       String  QuotedBy,
       String  Job_Title,
       String  Email,
       String  DaysQuote_Valid,
       String  Remark,
       String  Replied_Date,
       String  Replied_By
 ){
  
       this.ReqnID = ReqnID;
       this.UserID = UserID;
       this.Status = Status;
       this.Company_Code = Company_Code;
       this.Vendor_Code = Vendor_Code;
       this.Quoted_Vendor_code = Quoted_Vendor_code;
       this.QuotedBy = QuotedBy;
       this.Job_Title = Job_Title;
       this.Email = Email;
       this.DaysQuote_Valid = DaysQuote_Valid;
       this.Remark = Remark;
       this.Replied_Date = Replied_Date;
       this.Replied_By = Replied_By;


    }

    public RFQResponse(String ReqnID, String UserID, String Status, String Company_Code, String Vendor_Code, String Quoted_Vendor_code, String QuotedBy, String Job_Title, String Email, String DaysQuote_Valid, String Remark, String Replied_Date, String Replied_By, double Calc_Amount, double Total_Cost, double Discount_Amount, String DiscountType) {
        this.ReqnID = ReqnID;
        this.UserID = UserID;
        this.Status = Status;
        this.Company_Code = Company_Code;
        this.Vendor_Code = Vendor_Code;
        this.Quoted_Vendor_code = Quoted_Vendor_code;
        this.QuotedBy = QuotedBy;
        this.Job_Title = Job_Title;
        this.Email = Email;
        this.DaysQuote_Valid = DaysQuote_Valid;
        this.Remark = Remark;
        this.Replied_Date = Replied_Date;
        this.Replied_By = Replied_By;
        this.Calc_Amount = Calc_Amount;
        this.Total_Cost = Total_Cost;
        this.Discount_Amount = Discount_Amount;
        this.DiscountType = DiscountType;
    }

    /**
     * @return the ReqnID
     */
    public String getReqnID() {
        return ReqnID;
    }

    /**
     * @param ReqnID the ReqnID to set
     */
    public void setReqnID(String ReqnID) {
        this.ReqnID = ReqnID;
    }

    /**
     * @return the UserID
     */
    public String getUserID() {
        return UserID;
    }

    /**
     * @param UserID the UserID to set
     */
    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    /**
     * @return the Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     * @return the Company_Code
     */
    public String getCompany_Code() {
        return Company_Code;
    }

    /**
     * @param Company_Code the Company_Code to set
     */
    public void setCompany_Code(String Company_Code) {
        this.Company_Code = Company_Code;
    }

    /**
     * @return the Vendor_Code
     */
    public String getVendor_Code() {
        return Vendor_Code;
    }

    /**
     * @param Vendor_Code the Vendor_Code to set
     */
    public void setVendor_Code(String Vendor_Code) {
        this.Vendor_Code = Vendor_Code;
    }

    /**
     * @return the Quoted_Vendor_code
     */
    public String getQuoted_Vendor_code() {
        return Quoted_Vendor_code;
    }

    /**
     * @param Quoted_Vendor_code the Quoted_Vendor_code to set
     */
    public void setQuoted_Vendor_code(String Quoted_Vendor_code) {
        this.Quoted_Vendor_code = Quoted_Vendor_code;
    }

    /**
     * @return the QuotedBy
     */
    public String getQuotedBy() {
        return QuotedBy;
    }

    /**
     * @param QuotedBy the QuotedBy to set
     */
    public void setQuotedBy(String QuotedBy) {
        this.QuotedBy = QuotedBy;
    }

    /**
     * @return the Job_Title
     */
    public String getJob_Title() {
        return Job_Title;
    }

    /**
     * @param Job_Title the Job_Title to set
     */
    public void setJob_Title(String Job_Title) {
        this.Job_Title = Job_Title;
    }

    /**
     * @return the Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return the DaysQuote_Valid
     */
    public String getDaysQuote_Valid() {
        return DaysQuote_Valid;
    }

    /**
     * @param DaysQuote_Valid the DaysQuote_Valid to set
     */
    public void setDaysQuote_Valid(String DaysQuote_Valid) {
        this.DaysQuote_Valid = DaysQuote_Valid;
    }

    /**
     * @return the Remark
     */
    public String getRemark() {
        return Remark;
    }

    /**
     * @param Remark the Remark to set
     */
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    /**
     * @return the Replied_Date
     */
    public String getReplied_Date() {
        return Replied_Date;
    }

    /**
     * @param Replied_Date the Replied_Date to set
     */
    public void setReplied_Date(String Replied_Date) {
        this.Replied_Date = Replied_Date;
    }

    /**
     * @return the Replied_By
     */
    public String getReplied_By() {
        return Replied_By;
    }

    /**
     * @param Replied_By the Replied_By to set
     */
    public void setReplied_By(String Replied_By) {
        this.Replied_By = Replied_By;
    }

    /**
     * @return the Calc_Amount
     */
    public double getCalc_Amount() {
        return Calc_Amount;
    }

    /**
     * @param Calc_Amount the Calc_Amount to set
     */
    public void setCalc_Amount(double Calc_Amount) {
        this.Calc_Amount = Calc_Amount;
    }

    /**
     * @return the Total_Cost
     */
    public double getTotal_Cost() {
        return Total_Cost;
    }

    /**
     * @param Total_Cost the Total_Cost to set
     */
    public void setTotal_Cost(double Total_Cost) {
        this.Total_Cost = Total_Cost;
    }

    /**
     * @return the Discount_Amount
     */
    public double getDiscount_Amount() {
        return Discount_Amount;
    }

    /**
     * @param Discount_Amount the Discount_Amount to set
     */
    public void setDiscount_Amount(double Discount_Amount) {
        this.Discount_Amount = Discount_Amount;
    }

    /**
     * @return the DiscountType
     */
    public String getDiscountType() {
        return DiscountType;
    }

    /**
     * @param DiscountType the DiscountType to set
     */
    public void setDiscountType(String DiscountType) {
        this.DiscountType = DiscountType;
    }

}
