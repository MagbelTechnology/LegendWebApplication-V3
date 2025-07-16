package com.magbel.ia.vao;

	public class Invoice{
		private String invoiceCode;
		private String invoiceNo;
		private String PO;
		private String description;
		private double amount;
		private String invoiceDate;
		private String period;
		private String dueDate;
		private double discount;
		private double percentageDiscount;
		private String id;


		public Invoice(String id,String invoiceCode,String invoiceNo,String PO,String description,double amount,
		              String invoiceDate,String period,String dueDate,
					  double discount,double percentageDiscount){

					  setId(id);
					  setInvoiceCode(invoiceCode);
					  setInvoiceNo(invoiceNo);
					  setPO(PO);
					  setDescription(description);
					  setAmount(amount);
					  setInvoiceDate(invoiceDate);
					  setPeriod(period);
					  setDueDate(dueDate);
					  setDiscount(discount);
					  setPercentageDiscount(percentageDiscount);

		}



			public void setInvoiceCode(String invoiceCode){
				this.invoiceCode = invoiceCode;
			}
			public void setInvoiceNo(String invoiceNo){
				this.invoiceNo = invoiceNo;
			}
			public void setPO(String PO){
				this.PO = PO;
			}
			public void setDescription(String description){
				this.description = description;
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
			public void setPercentageDiscount(double percentageDiscount){
				this.percentageDiscount = percentageDiscount;
			}


			public String getInvoiceCode(){
				return this.invoiceCode;
			}
			public String getInvoiceNo(){
				return this.invoiceNo;
			}
			public String getPO(){
				return this.PO;
			}
			public String getDescription(){
				return this.description;
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
			public double getPercentageDiscount(){
				return this.percentageDiscount;
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

	}