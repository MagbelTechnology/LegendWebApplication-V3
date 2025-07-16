package com.magbel.ia.vao;

import java.io.Serializable;

public class SmsTransaction
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String mtId;
    private String admin_no;
    private String companyCode;
    private String phoneNo;
    private String smsdescription;  
    private String tranDescription;
    private String schoolcode;
    private String schoolname;
    private String transactionId;
    private String status;
    private int userId;
    private double tranamount;
    private String transactionDate;
   public SmsTransaction()
    {
          
    }

	public SmsTransaction(String mtId,String admin_no,String companyCode,String phoneNo, String status,
                          String smsdescription, String tranDescription, 
                          String schoolcode, String transactionId,int userId,double tranamount,
                          String transactionDate,String schoolname) {
	
        this.mtId = mtId;
        this.admin_no = admin_no;
		this.companyCode = companyCode;
		this.phoneNo = phoneNo;
		this.status = status;		
		this.smsdescription = smsdescription;
		this.tranDescription = tranDescription;
		this.schoolcode = schoolcode;
		this.transactionId = transactionId;
        this.userId = userId;
        this.tranamount = tranamount;
        this.transactionDate = transactionDate;  
        this.schoolname = schoolname;
	}
    public final String getMtId()
    {
        return mtId;
    }

    public final void setMtId(String MtId)
    {
        mtId = MtId;
    }
    public final String getAdmin_no()
    {
        return admin_no;
    }

    public final void setAdmin_no(String admin_no)
    {
        this.admin_no = admin_no;
    }    
    public final String getCompanyCode()
    {
        return companyCode;
    }

    public final void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }    

    public final String getPhoneNo()
    {
        return phoneNo;
    }

    public final void setPhoneNo(String PhoneNo)
    {
    	phoneNo = PhoneNo;
    }
    public final String getSmsdescription()
    {
        return smsdescription;
    }

    public final void setSmsdescription(String Smsdescription)
    {
    	smsdescription = Smsdescription;
    }

    public final String getTranDescription()
    {
        return tranDescription;
    }

    public final void setTranDescription(String TranDescription)
    {
    	tranDescription = TranDescription;
    }
    public final String getSchoolcode()
    {
        return schoolcode;
    }

    public final void setSchoolcode(String Schoolcode)
    {
    	schoolcode = Schoolcode;
    }    
    public final String getTransactionId()
    {
        return transactionId;
    }

    public final void setTransactionId(String TransactionId)
    {
    	transactionId = TransactionId;
    }
    public final String getStatus()
    {
        return status;
    }
    public final void setStatus(String Status)
    {
    	status = Status;
    }    
    public final double getTranamount()
    {
        return tranamount;
    }

    public final void setTranamount(double tranamount)
    {
        this.tranamount = tranamount;
    }    
    public final String getTransactionDate()
    {
        return transactionDate;
    }

    public final void setTransactionDate(String TransactionDate)
    {
    	transactionDate = TransactionDate;
    }
    public final int getUserId()
    {
        return userId;
    }

    public final void setUserId(int UserId)
    {
        userId = UserId;
    }
    public final String getSchoolname()
    {
        return schoolname;
    }

    public final void setSchoolname(String Schoolname)
    {
    	schoolname = Schoolname;
    }      
 
    
}
