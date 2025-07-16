package com.magbel.ia.vao;

	public class APInvoice{
		private String invoiceCode;
		private String vendorCode;
		private String desc;
		private double amount;
		private String invoiceDate;
		private String period;
		private String dueDate;
		private double discount;
		private double percDiscount;
                private double amountPaid;
                private double amountOwing;
                private String glAccount;
                private double glAmount;
		private String id;
                private int userId;

		public APInvoice(String id,String invoiceCode,String vendorCode,String desc,double amount,
		              String invoiceDate,String period,String dueDate,
                            double discount,double percDiscount,double amountPaid,
                            double amountOwing,String glAccount,double glAmount,int userId){

					  setId(id);
					  setInvoiceCode(invoiceCode);
					  setVendorCode(vendorCode);
                                          setDesc(desc);
					  setAmount(amount);
					  setInvoiceDate(invoiceDate);
					  setPeriod(period);
					  setDueDate(dueDate);
					  setDiscount(discount);
					  setPercDiscount(percDiscount);
                                          setAmountPaid(amountPaid);
                                          setAmountOwing(amountOwing);
                                          setGlAccount(glAccount);
                                          setGlAmount(glAmount);
                                          setUserId(userId);
		}



			public void setInvoiceCode(String invoiceCode){
				this.invoiceCode = invoiceCode;
			}
			
			
			public void setAmount(double amount){
				this.amount = amount;
			}
			public void setInvoiceDate(String invoiceDate){
				this.invoiceDate = invoiceDate;
			}
			public void setPeriod(String period){
				this.period = period;
			}
			public void setDueDate(String dueDate){
				this.dueDate = dueDate;
			}
			public void setDiscount(double discount){
				this.discount = discount;
			}
			
			public String getInvoiceCode(){
				return this.invoiceCode;
			}
			
			public double getAmount(){
				return this.amount;
			}
			public String getInvoiceDate(){
				return this.invoiceDate;
			}
			public String getPeriod(){
				return this.period;
			}
			public String getDueDate(){
				return this.dueDate;
			}
			public double getDiscount(){
				return this.discount;
			}
			
			/**
			 * @return the id
			 */
			public String getId() {
				return id;
			}



			/**
			 * @param id the id to set
			 */
			public void setId(String id) {
				this.id = id;
			}

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setPercDiscount(double percDiscount) {
        this.percDiscount = percDiscount;
    }

    public double getPercDiscount() {
        return percDiscount;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountOwing(double amountOwing) {
        this.amountOwing = amountOwing;
    }

    public double getAmountOwing() {
        return amountOwing;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAmount(double glAmount) {
        this.glAmount = glAmount;
    }

    public double getGlAmount() {
        return glAmount;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
