package com.magbel.ia.vao;

	public class JournalVoucher{

		private String id;
		private String no;
		private String autoReverse;
		private String date;
		private String period;
		private String description;
		private double amount;
		private String glAccount;
		private String sbu;
		private String name;


		public JournalVoucher(String id,String no,String autoReverse,String date,String period,
		                      String description,double amount,String glAccount,String sbu,
		                      String name){
			setId(id);
			setNo(no);
			setAutoReverse(autoReverse);
			setDate(date);
			setPeriod(period);
			setDescription(description);
			setAmount(amount);
			setGlAccount(glAccount);
			setSBU(sbu);
			setName(name);
		}

		public void setId(String id){
			this.id = id;
		}
		public void setNo(String no){
			this.no = no;
		}
		public void setAutoReverse(String autoReverse){
			this.autoReverse = autoReverse;
		}
		public void setDate(String date){
			this.date = date;
		}
		public void setPeriod(String period){
			this.period = period;
		}
		public void setDescription(String description){
			this.description = description;
		}
		public void setAmount(double amount){
			this.amount = amount;
		}
		public void setGlAccount(String glAccount){
			this.glAccount = glAccount;
		}
		public void setSBU(String sbu){
			this.sbu = sbu;
		}
		public void setName(String name){
			this.name = name;
		}

		public String getId(){
			return this.id;
		}
		public String getNo(){
			return this.no;
		}
		public String getAutoReverse(){
			return this.autoReverse;
		}
		public String getDate(){
			return this.date;
		}
		public String getPeriod(){
			return this.period;
		}
		public String getDescription(){
			return this.description;
		}
		public double getAmount(){
			return this.amount;
		}
		public String getGlAccount(){
			return this.glAccount;
		}
		public String getSBU(){
			return this.sbu;
		}
		public String getName(){
			return this.name;
		}

	}