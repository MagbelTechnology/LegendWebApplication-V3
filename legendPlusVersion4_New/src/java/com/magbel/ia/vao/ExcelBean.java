package com.magbel.ia.vao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.DateManipulations;

import magma.util.Codes;

public class ExcelBean  extends PersistenceServiceDAO
    implements Serializable
{
  
    private static final long serialVersionUID = 1L;
    private String mtId;
    private String customerNo;
    private String adminNo;    
    private String draccountNo;
    private String customerType;
    private String customerClass;
    private String customerClassCode;
    private String customerTypeCode;
    private String accountType;
    private String accountTypeCode;
    private String accountClass;
    private String craccountNo;
    private String accountName;
    private String currency;
    private String currencyCode;
    private double accountBalance;
    private double perctDiscount;
    private String industry;
    private String industryCode;
    private String lastTransDate;
    private double mTD;
    private double qTD;
    private double yTD;
    private double lYTD;
    private double witholdingAmount;
    private double vat;
    private String status;
    private String createDate;
    private String effectiveDate;
    private String closedDate;
    private int userId;
    private ArrayList accountNos;
    private String accountOfficer;
    private int transactionCount;
    private String companyCode;
    private String schoolCode;
    private String localgovmt;
    private String denomination;
    private String presentschool;
    private String presentschaddr;
    private double clearbalance;
    private double termbalance;
    private String transamount;
    private String transdescription;
    private Calendar posting_date;
    private String draccountType;
    private String craccountType;
    private String term;
    public ExcelBean()
    {/*
    	companyCode = "";
    	schoolCode = "";
    	adminNo = "";
    	draccountNo = "";
    	craccountNo = "";
    	transdescription = "";
    	transamount = "";
        accountBalance = 0.0D;
        perctDiscount = 0.0D;
        mTD = 0.0D;
        qTD = 0.0D;
        yTD = 0.0D;
        lYTD = 0.0D;
        witholdingAmount = 0.0D;
        userId = 0;
        accountOfficer = null;
        transactionCount = 0;
        clearbalance = 0.0D;
        termbalance = 0.0D;*/
        posting_date = new GregorianCalendar();
        
    }
    

    
    
	public void createExcel()
            throws Exception
        {
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String processTag = "N";
    	con = getConnection();
            StringBuffer b;
            b = new StringBuffer(400);
            Codes code = new Codes();
            System.out.println("About to prepare Record to Be Inserted: ");
            String query = "INSERT INTO REVERSAL_UPLOAD_TMP(COMP_CODE,SCHOOL_CODE,ADMIN_NO,DRACCOUNT," +
    "DRACCT_TYPE,CRACCOUNT,CRACCT_TYPE,TRANSNARRATION,PROCESS_FLAG,AMOUNT,TERM,CREATION_DATE" +
    " )\tVALUES('"
    ;
            b.append(query);
            b.append(companyCode);
            b.append("','");
            b.append(schoolCode);
            b.append("','");
            b.append(adminNo);
            b.append("','");
            b.append(draccountNo);
            b.append("','");
            b.append(draccountType);
            b.append("','");
            b.append(craccountNo);
            b.append("','");
            b.append(craccountType);
            b.append("','");
            b.append(transdescription);
            b.append("','");
            b.append(processTag);
            b.append("','");
            b.append(transamount);
            b.append("','");
            b.append(term);
            b.append("','");
            b.append(DateManipulations.CalendarToDb(posting_date));
            b.append("')");
            try
            {  
            	System.out.println("Record to Be Inserted: "+b.toString());
            	 getStatement().executeUpdate(b.toString());
         //   	done = (ps.executeUpdate(b.toString()) != -1);
            }
            catch(Exception r)
            {
                System.out.println((new StringBuilder("INFO: Error creating Record >>")).append(r).toString());
            }

        }
    
    public int CreateRecord()
    throws Exception, Throwable
{
    	System.out.println("Inside  CreateRecord: ");
	int DONE = 0;
	int value = 0;
	createExcel();
	value = DONE;

    return value;
}	

    public final String getCustomerClass()
    {
        return customerClass;
    }

    public final void setCustomerClass(String CustomerClass)
    {
        customerClass = CustomerClass;
    }

    public final String getCompanyCode()
    {
        return companyCode;
    }

    public final void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }
    public final String getSchoolCode()
    {
        return schoolCode;
    }

    public final void setSchoolCode(String schoolCode)
    {
        this.schoolCode = schoolCode;
    }
    public final String getAccountOfficer()
    {
        return accountOfficer;
    }

    public final void setAccountOfficer(String AccountOfficer)
    {
        accountOfficer = AccountOfficer;
    }

    public final int getTransactionCount()
    {
        return transactionCount;
    }

    public final void setTransactionCount(int TransactionCount)
    {
        transactionCount = TransactionCount;
    }

    public final double getAccountBalance()
    {
        return accountBalance;
    }

    public final void setAccountBalance(double AccountBalance)
    {
        accountBalance = AccountBalance;
    }

    public final String getAccountName()
    {
        return accountName;
    }

    public final void setAccountName(String AccountName)
    {
        accountName = AccountName;
    }

    public final String getDraccountNo()
    {
        return draccountNo;
    }

    public final void setDraccountNo(String DraccountNo)
    {
        draccountNo = DraccountNo;
    }

    public final String getCurrency()
    {
        return currency;
    }

    public final void setCurrency(String Currency)
    {
        currency = Currency;
    }

    public final String getAccountType()
    {
        return accountType;
    }

    public final void setAccountType(String AccountType)
    {
        accountType = AccountType;
    }

    public final String getAccountTypeCode()
    {
        return accountTypeCode;
    }

    public final void setAccountTypeCode(String AccountTypeCode)
    {
        accountTypeCode = AccountTypeCode;
    }

    public final String getCustomerNo()
    {
        return customerNo;
    }

    public final void setCustomerNo(String CustomerNo)
    {
        customerNo = CustomerNo;
    }
    public final String getAdminNo()
    {
        return adminNo;
    }

    public final void setAdminNo(String AdminNo)
    {
    	adminNo = AdminNo;
    }
    
    public final String getIndustry()
    {
        return industry;
    }

    public final void setIndustry(String Industry)
    {
        industry = Industry;
    }

    public final String getIndustryCode()
    {
        return industryCode;
    }

    public final void setIndustryCode(String IndustryCode)
    {
        industryCode = IndustryCode;
    }

    public final String getCurrencyCode()
    {
        return currencyCode;
    }

    public final void setCurrencyCode(String CurrencyCode)
    {
        currencyCode = CurrencyCode;
    }

    public final String getLastTransDate()
    {
        return lastTransDate;
    }

    public final void setLastTransDate(String LastTransDate)
    {
        lastTransDate = LastTransDate;
    }

    public final String getMtId()
    {
        return mtId;
    }

    public final void setMtId(String MtId)
    {
        mtId = MtId;
    }

    public final String getCraccountNo()
    {
        return craccountNo;
    }

    public final void setCraccountNo(String CraccountNo)
    {
        craccountNo = CraccountNo;
    }

    public final double getPerctDiscount()
    {
        return perctDiscount;
    }

    public final void setPerctDiscount(double PerctDiscount)
    {
        perctDiscount = PerctDiscount;
    }

    public final double getLTD()
    {
        return lYTD;
    }

    public final void setLTD(double LYTD)
    {
        lYTD = LYTD;
    }

    public final double getMTD()
    {
        return mTD;
    }

    public final void setMTD(double MTD)
    {
        mTD = MTD;
    }

    public final double getWitholdingAmount()
    {
        return witholdingAmount;
    }

    public final void setWitholdingAmount(double WitholdingAmount)
    {
        witholdingAmount = WitholdingAmount;
    }

    public final double getVat()
    {
        return vat;
    }

    public final void setVat(double Vat)
    {
        vat = Vat;
    }

    public final double getQTD()
    {
        return qTD;
    }

    public final void setQTD(double QTD)
    {
        qTD = QTD;
    }

    public final double getYTD()
    {
        return yTD;
    }

    public final void setYTD(double YTD)
    {
        yTD = YTD;
    }

    public final String getStatus()
    {
        return status;
    }

    public final void setStatus(String Status)
    {
        status = Status;
    }

    public final String getCreateDate()
    {
        return createDate;
    }

    public final void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public final String getEffectiveDate()
    {
        return effectiveDate;
    }

    public final void setEffectiveDate(String EffectiveDate)
    {
        effectiveDate = EffectiveDate;
    }

    public final String getClosedDate()
    {
        return closedDate;
    }

    public final void setClosedDate(String ClosedDate)
    {
        closedDate = ClosedDate;
    }

    public final int getUserId()
    {
        return userId;
    }

    public final void setUserId(int UserId)
    {
        userId = UserId;
    }

    public final String getAccountClass()
    {
        return accountClass;
    }

    public final void setAccountClass(String AccountClass)
    {
        accountClass = AccountClass;
    }

    public final String getCustomerType()
    {
        return customerType;
    }

    public final void setCustomerType(String CustomerType)
    {
        customerType = CustomerType;
    }

    public final void setCustomerClassCode(String customerClassCode)
    {
        this.customerClassCode = customerClassCode;
    }

    public final String getCustomerClassCode()
    {
        return customerClassCode;
    }
    public final void setLocalgovmt(String localgovmt)
    {
        this.localgovmt = localgovmt;
    }

    public final String getLocalgovmt()
    {
        return localgovmt;
    }    
    public final void setDenomination(String denomination)
    {
        this.denomination = denomination;
    }

    public final String getDenomination()
    {
        return denomination;
    }    
    public final void setPresentschool(String presentschool)
    {
        this.presentschool = presentschool;
    }

    public final String getPresentschool()
    {
        return presentschool;
    } 
    
    public final String getPresentschaddr()
    {
        return presentschaddr;
    }
    public final void setPresentschaddr(String presentschaddr)
    {
        this.presentschaddr = presentschaddr;
    }     
    public final double getClearbalance()
    {
        return clearbalance;
    }

    public final void setClearbalance(double Clearbalance)
    {
    	clearbalance = Clearbalance;
    }
    public final double getTermbalance()
    {
        return termbalance;
    }

    public final void setTermbalance(double Termbalance)
    {
    	termbalance = Termbalance;
    }    
    public final String getTransamount()
    {
        return transamount;
    }

    public final void setTransamount(String Transamount)
    {
    	transamount = Transamount;
    }  
    public final String getTransdescription()
    {
        return transdescription;
    }

    public final void setTransdescription(String Transdescription)
    {
    	transdescription = Transdescription;
    }   
    public void setPosting_date(String s)
    {
    }    
    public String getPosting_date()
    {
        return DateManipulations.CalendarToDate(posting_date);
    }

    public final String getDraccountType()
    {
        return draccountType;
    }

    public final void setDraccountType(String DraccountType)
    {
    	draccountType = DraccountType;
    }
    public final String getCraccountType()
    {
        return craccountType;
    }

    public final void setCraccountType(String CraccountType)
    {
    	craccountType = CraccountType;
    }
    
    public final String getTerm()
    {
        return term;
    }

    public final void setTerm(String Term)
    {
    	term = Term;
    }
    
    public int insertExcelRecordUpload()
    throws Exception, Throwable
{

    //System.out.println("=====insertGroupAssetRecordUpload asset_id=====> "+asset_id);
    int DONE = 0;
    int value = 0;
        createExcel();
        value = DONE;
    return value;
}
    
    
}
