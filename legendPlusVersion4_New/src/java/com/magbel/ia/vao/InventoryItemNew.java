package com.magbel.ia.vao;

public class InventoryItemNew
{
 private String mtid = null;
  private String itemNo = null;
  private String status = null;
  private String type = null;
  private String description = null;
  private String taxcode = null;
  private int minimumQuantity = 0;
  private double weightAverageCost = 0;
  private double standardCost = 0;
  private double weight = 0;
  private String backOrderable = null;
  private String userId;
  private int reorderLevel;
  private String salesAcct;
  private String COGSoldAcct;
  private String inventoryAcct;
  private String reqApproval;
  private int maxApproveLevel;
  private double minAmtLimit;
  private double maxAmtLimit; 
  private String adjustAcct;
    private int adjustMaxApproveLevel;
    private double adjustMinAmtLimit;
    private double adjustMaxAmtLimit;  
	private String company_Code;
	private String FIFO;
  
  public  InventoryItemNew(String MtId, String ItemNo, String Status, String Type, String Description, String TaxCode, 
                        int MinimumQuantity, double WeightAvgCost, double StandardCost, double Weight, 
                        String BackOrderable,String userId,int reorderLevel,String salesAcct,String COGSoldAcct,
                        String inventoryAcct,String reqApproval,int maxApproveLevel,double minAmtLimit,double maxAmtLimit,
                        String adjustAcct,int adjustMaxApproveLevel,double adjustMinAmtLimit,double adjustMaxAmtLimit,
						String company_Code,String FIFO
                        )
  {
  
    this.mtid = MtId;
    this.itemNo = ItemNo;
    this.status = Status;
    this.type = Type;
    this.description =  Description;
    this.taxcode = TaxCode;
    this.minimumQuantity = MinimumQuantity;
    this.weightAverageCost = WeightAvgCost;
    this.standardCost = StandardCost;
    this.weight = Weight;
    this.backOrderable = BackOrderable;
    this.userId = userId;
    this.reorderLevel = reorderLevel;
    this.salesAcct = salesAcct;
    this.COGSoldAcct = COGSoldAcct;
    this.inventoryAcct = inventoryAcct;
    this.reqApproval = reqApproval;
    this.maxApproveLevel = maxApproveLevel;
    this.minAmtLimit = minAmtLimit;
    this.maxAmtLimit = maxAmtLimit;
    this.adjustAcct = adjustAcct;
    this.adjustMaxApproveLevel = adjustMaxApproveLevel;
    this.adjustMinAmtLimit = adjustMinAmtLimit;
    this.adjustMaxAmtLimit = adjustMaxAmtLimit;
	this.company_Code=company_Code;
	this.FIFO=FIFO;
    
}
  
  public void setMtId(String MtId)
  {
    this.mtid = MtId.trim();
  }
  public String getMtId()
	  {
	    return this.mtid;
	  }
  public void setItemNo(String ItemNo)
  {
    this.itemNo = ItemNo.trim();
  }
  public void setStatus(String Status)
  {
   this.status = Status.trim();
  }


 public void setType(String Type)
  {
    this.type = Type.trim();
  }


 public void setDescription(String Description)
  {
    this.description = Description.trim();
  }


 public void setTaxCode(String TaxCode)
  {
    this.taxcode = TaxCode.trim();
  }


 public  void  setMinimumQuantity(int  MinimumQuantity)
  {
    this.minimumQuantity =  MinimumQuantity;
  }


 public  void  setWeightAverageCost(double WeightAverageCost)
  {
    this.weightAverageCost =  WeightAverageCost;
  }



 public void setStandardCost(double  StandardCost)
  {
    this.standardCost  =  StandardCost;
  }


 public void setWeight(double  Weight)
  {
    this.weight  = Weight;
  }


 public void  backOrderable(String  BackOrderable)
  {
    this.backOrderable =  BackOrderable.trim();
  }



public String getItemNo()
 {
   return this.itemNo;
 }


public String getStatus()
 {
   return  this.status;
 }


public String getType()
 {
   return  this.type;
 }


public String getDescription()
 {
   return this.description;
  }


public String getTaxCode()
 {
   return  this.taxcode;
 }


public int getMinimumQuantity()
 {
   return  this.minimumQuantity;
  }



public  double getWeightAverageCost()
 {
   return  this.weightAverageCost;
 }



public double getStandardCost()
 {
   return  this.standardCost;
 }


public  double getWeight()
 {
   return  this.weight;
  }



public  String getBackOrderable()
 {
   return   this.backOrderable;
 }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setSalesAcct(String salesAcct) {
        this.salesAcct = salesAcct;
    }

    public String getSalesAcct() {
        return salesAcct;
    }

    public void setCOGSoldAcct(String cOGSoldAcct) {
        this.COGSoldAcct = cOGSoldAcct;
    }

    public String getCOGSoldAcct() {
        return COGSoldAcct;
    }

    public void setInventoryAcct(String inventoryAcct) {
        this.inventoryAcct = inventoryAcct;
    }

    public String getInventoryAcct() {
        return inventoryAcct;
    }


    public void setReqApproval(String reqApproval) {
        this.reqApproval = reqApproval;
    }

    public String getReqApproval() {
        return reqApproval;
    }


    public void setMaxApproveLevel(int maxApproveLevel) {
        this.maxApproveLevel = maxApproveLevel;
    }

    public int getMaxApproveLevel() {
        return maxApproveLevel;
    }

    public void setMinAmtLimit(double minAmtLimit) {
        this.minAmtLimit = minAmtLimit;
    }

    public double getMinAmtLimit() {
        return minAmtLimit;
    }

    public void setMaxAmtLimit(double maxAmtLimit) {
        this.maxAmtLimit = maxAmtLimit;
    }

    public double getMaxAmtLimit() {
        return maxAmtLimit;
    }

    public void setAdjustMaxApproveLevel(int adjustMaxApproveLevel) {
        this.adjustMaxApproveLevel = adjustMaxApproveLevel;
    }

    public int getAdjustMaxApproveLevel() {
        return adjustMaxApproveLevel;
    }

    public void setAdjustMinAmtLimit(double adjustMinAmtLimit) {
        this.adjustMinAmtLimit = adjustMinAmtLimit;
    }

    public double getAdjustMinAmtLimit() {
        return adjustMinAmtLimit;
    }

    public void setAdjustMaxAmtLimit(double adjustMaxAmtLimit) {
        this.adjustMaxAmtLimit = adjustMaxAmtLimit;
    }

    public double getAdjustMaxAmtLimit() {
        return adjustMaxAmtLimit;
    }

    public void setAdjustAcct(String adjustAcct) {
        this.adjustAcct = adjustAcct;
    }

    public String getAdjustAcct() {
        return adjustAcct;
    }
	
	public void setCompanyCode(String company_Code){
	 this.company_Code=company_Code;
	 }
	public String getCompanyCode(){
          return company_Code;
     }

     public void setFIFO(String FIFO){
      this.FIFO=FIFO;
      }
     public String getFIFO(){
         return FIFO;
       }		 
}


