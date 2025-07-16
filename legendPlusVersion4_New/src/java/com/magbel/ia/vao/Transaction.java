package com.magbel.ia.vao;

import java.io.Serializable;

public class Transaction
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String mtId;
    private String admin_no;
    private String companyCode;
    private String draccount;
    private String drnarration;  
    private String drschoolcode;
    private String craccount;
    private String crnarration;
    private String crschoolcode;
    private String transactionId;
    private String status;
    private int userId;
    private double tranamount;
    private String transactionDate;
   public Transaction()
    { 
          
    }

	public Transaction(String mtId,String admin_no,String companyCode,String draccount, String status,
                          String drnarration, String drschoolcode,String craccount, String crnarration, 
                          String crschoolcode, String transactionId,int userId,double tranamount,
                          String transactionDate) {
	
        this.mtId = mtId;
        this.admin_no = admin_no;
		this.companyCode = companyCode;
		this.draccount = draccount;
		this.status = status;		
		this.drnarration = drnarration;
		this.drschoolcode = drschoolcode;
		this.craccount = craccount;
		this.crnarration = crnarration;
		this.crschoolcode = crschoolcode;
		this.transactionId = transactionId;
        this.userId = userId;
        this.tranamount = tranamount;
        this.transactionDate = transactionDate;                               
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

    public final String getDraccount()
    {
        return draccount;
    }

    public final void setDraccount(String Draccount)
    {
    	draccount = Draccount;
    }
    public final String getDrnarration()
    {
        return drnarration;
    }

    public final void setDrnarration(String Drnarration)
    {
    	drnarration = Drnarration;
    }

    public final String getDrschoolcode()
    {
        return drschoolcode;
    }

    public final void setDrschoolcode(String Drschoolcode)
    {
    	drschoolcode = Drschoolcode;
    }

    public final String getCraccount()
    {
        return craccount;
    }

    public final void setCraccount(String Craccount)
    {
    	craccount = Craccount;
    }
    public final String getCrnarration()
    {
        return crnarration;
    }

    public final void setCrnarration(String Crnarration)
    {
    	crnarration = Crnarration;
    }
    public final String getCrschoolcode()
    {
        return crschoolcode;
    }

    public final void setCrschoolcode(String Crschoolcode)
    {
    	crschoolcode = Crschoolcode;
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
    
 
    
}
