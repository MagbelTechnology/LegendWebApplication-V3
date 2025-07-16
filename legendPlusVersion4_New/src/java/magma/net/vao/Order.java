/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma.net.vao;

/**
 *
 * @author Developers
 */
public class Order {
      private String orderNo;
    private String tran_Id;
    private String Vendor_Code;
    private String Contract_Title;
    private String qtyBall;
    private String Contract_value;
    private String Contract_duration;
    private String Payment_Type_code;
    private String Signature_code1;
    private String Signature_code2;
    private String prepared_by;
    private String prepare_date;
    private String prepare_time;
    private String DUE_DATE;
    private String status;
    private String supervisor;
    private String workStationIP;
    private String ID;
    private String DELIVERY_COMPLETION;
    private String DELIVERY_DATE;
    private String QUANTITY_BALANCE;
    private String ReqnID;
    private String Subject_To_Vat;
    private String Vat_amount;
    private String Total_Amount;
    private String issue_date;

    public Order(String orderNo,String
    tran_Id,String
    Vendor_Code,String
    Contract_Title,String
    qtyBall,String
    Contract_value,String
    Contract_duration,String
    Payment_Type_code,String
    Signature_code1,String
    Signature_code2,String
    prepared_by,String
    prepare_date,String
    prepare_time,String
    DUE_DATE,String
    status,String
    supervisor,String
    workStationIP,String
    ID,String
    DELIVERY_COMPLETION,String
    DELIVERY_DATE,String
    QUANTITY_BALANCE,String
    ReqnID,String
    Subject_To_Vat,String
    Vat_amount,String
    Total_Amount,String
    issue_date
    ){
    this.orderNo=orderNo;
    this.tran_Id=tran_Id;
    this.Vendor_Code=Vendor_Code;
    this.Contract_Title=Contract_Title;
    this.qtyBall=qtyBall;
    this.Contract_value=Contract_value;
    this.Contract_duration=Contract_duration;
    this.Payment_Type_code=Payment_Type_code;
    this.Signature_code1=Signature_code1;
    this.Signature_code2=Signature_code2;
    this.prepared_by=prepared_by;
    this.prepare_date=prepare_date;
    this.prepare_time=prepare_time;
    this.DUE_DATE=DUE_DATE;
    this.status=status;
    this.supervisor=supervisor;
    this.workStationIP=workStationIP;
    this.ID=ID;
    this.DELIVERY_COMPLETION=DELIVERY_COMPLETION;
    this.DELIVERY_DATE=DELIVERY_DATE;
    this.QUANTITY_BALANCE=QUANTITY_BALANCE;
    this.ReqnID=ReqnID;
    this.Subject_To_Vat=Subject_To_Vat;
    this.Vat_amount=Vat_amount;
    this.Total_Amount=Total_Amount;
    this.issue_date=issue_date;


    }
    /**
     * @return the orderNo
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @param orderNo the orderNo to set
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * @return the tran_Id
     */
    public String getTran_Id() {
        return tran_Id;
    }

    /**
     * @param tran_Id the tran_Id to set
     */
    public void setTran_Id(String tran_Id) {
        this.tran_Id = tran_Id;
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
     * @return the Contract_Title
     */
    public String getContract_Title() {
        return Contract_Title;
    }

    /**
     * @param Contract_Title the Contract_Title to set
     */
    public void setContract_Title(String Contract_Title) {
        this.Contract_Title = Contract_Title;
    }

    /**
     * @return the qtyBall
     */
    public String getQtyBall() {
        return qtyBall;
    }

    /**
     * @param qtyBall the qtyBall to set
     */
    public void setQtyBall(String qtyBall) {
        this.qtyBall = qtyBall;
    }

    /**
     * @return the Contract_value
     */
    public String getContract_value() {
        return Contract_value;
    }

    /**
     * @param Contract_value the Contract_value to set
     */
    public void setContract_value(String Contract_value) {
        this.Contract_value = Contract_value;
    }

    /**
     * @return the Contract_duration
     */
    public String getContract_duration() {
        return Contract_duration;
    }

    /**
     * @param Contract_duration the Contract_duration to set
     */
    public void setContract_duration(String Contract_duration) {
        this.Contract_duration = Contract_duration;
    }

    /**
     * @return the Payment_Type_code
     */
    public String getPayment_Type_code() {
        return Payment_Type_code;
    }

    /**
     * @param Payment_Type_code the Payment_Type_code to set
     */
    public void setPayment_Type_code(String Payment_Type_code) {
        this.Payment_Type_code = Payment_Type_code;
    }

    /**
     * @return the Signature_code1
     */
    public String getSignature_code1() {
        return Signature_code1;
    }

    /**
     * @param Signature_code1 the Signature_code1 to set
     */
    public void setSignature_code1(String Signature_code1) {
        this.Signature_code1 = Signature_code1;
    }

    /**
     * @return the Signature_code2
     */
    public String getSignature_code2() {
        return Signature_code2;
    }

    /**
     * @param Signature_code2 the Signature_code2 to set
     */
    public void setSignature_code2(String Signature_code2) {
        this.Signature_code2 = Signature_code2;
    }

    /**
     * @return the prepared_by
     */
    public String getPrepared_by() {
        return prepared_by;
    }

    /**
     * @param prepared_by the prepared_by to set
     */
    public void setPrepared_by(String prepared_by) {
        this.prepared_by = prepared_by;
    }

    /**
     * @return the prepare_date
     */
    public String getPrepare_date() {
        return prepare_date;
    }

    /**
     * @param prepare_date the prepare_date to set
     */
    public void setPrepare_date(String prepare_date) {
        this.prepare_date = prepare_date;
    }

    /**
     * @return the prepare_time
     */
    public String getPrepare_time() {
        return prepare_time;
    }

    /**
     * @param prepare_time the prepare_time to set
     */
    public void setPrepare_time(String prepare_time) {
        this.prepare_time = prepare_time;
    }

    /**
     * @return the DUE_DATE
     */
    public String getDUE_DATE() {
        return DUE_DATE;
    }

    /**
     * @param DUE_DATE the DUE_DATE to set
     */
    public void setDUE_DATE(String DUE_DATE) {
        this.DUE_DATE = DUE_DATE;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return the workStationIP
     */
    public String getWorkStationIP() {
        return workStationIP;
    }

    /**
     * @param workStationIP the workStationIP to set
     */
    public void setWorkStationIP(String workStationIP) {
        this.workStationIP = workStationIP;
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the DELIVERY_COMPLETION
     */
    public String getDELIVERY_COMPLETION() {
        return DELIVERY_COMPLETION;
    }

    /**
     * @param DELIVERY_COMPLETION the DELIVERY_COMPLETION to set
     */
    public void setDELIVERY_COMPLETION(String DELIVERY_COMPLETION) {
        this.DELIVERY_COMPLETION = DELIVERY_COMPLETION;
    }

    /**
     * @return the DELIVERY_DATE
     */
    public String getDELIVERY_DATE() {
        return DELIVERY_DATE;
    }

    /**
     * @param DELIVERY_DATE the DELIVERY_DATE to set
     */
    public void setDELIVERY_DATE(String DELIVERY_DATE) {
        this.DELIVERY_DATE = DELIVERY_DATE;
    }

    /**
     * @return the QUANTITY_BALANCE
     */
    public String getQUANTITY_BALANCE() {
        return QUANTITY_BALANCE;
    }

    /**
     * @param QUANTITY_BALANCE the QUANTITY_BALANCE to set
     */
    public void setQUANTITY_BALANCE(String QUANTITY_BALANCE) {
        this.QUANTITY_BALANCE = QUANTITY_BALANCE;
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
     * @return the Subject_To_Vat
     */
    public String getSubject_To_Vat() {
        return Subject_To_Vat;
    }

    /**
     * @param Subject_To_Vat the Subject_To_Vat to set
     */
    public void setSubject_To_Vat(String Subject_To_Vat) {
        this.Subject_To_Vat = Subject_To_Vat;
    }

    /**
     * @return the Vat_amount
     */
    public String getVat_amount() {
        return Vat_amount;
    }

    /**
     * @param Vat_amount the Vat_amount to set
     */
    public void setVat_amount(String Vat_amount) {
        this.Vat_amount = Vat_amount;
    }

    /**
     * @return the Total_Amount
     */
    public String getTotal_Amount() {
        return Total_Amount;
    }

    /**
     * @param Total_Amount the Total_Amount to set
     */
    public void setTotal_Amount(String Total_Amount) {
        this.Total_Amount = Total_Amount;
    }

    /**
     * @return the issue_date
     */
    public String getIssue_date() {
        return issue_date;
    }

    /**
     * @param issue_date the issue_date to set
     */
    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

}
