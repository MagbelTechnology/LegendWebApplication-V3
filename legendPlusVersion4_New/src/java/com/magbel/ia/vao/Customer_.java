package com.magbel.ia.vao;

	public class Customer_{

		private String id;
		private String code;
		private String name;
		private String status;
		private String address;
		private String phone;
		private String fax;
		private String contact;
		private String email;
		private double discount;
		private int discountDays;
		private String currency;
		private String shipTo;
		private String shipAddress;
		private double creditLimit;
		private double cod;
		private String message;
		private String exceptionNo;
		private String type;


		public Customer_(String id,String code,String name,String status,String address,
						String phone,String fax,String contact,String email,
						double discount,int discountDays,String currency,
						String shipTo,String shipAddress,double creditLimit,
						double cod,String message,String exceptionNo){

					  setId(id);
					  setCode(code);
					  setName(name);
					  setStatus(status);
					  setAddress(address);
					  setPhone(phone);
					  setFax(fax);
					  setContact(contact);
					  setEmail(email);
					  setDiscount(discount);
					  setDiscountDays(discountDays);
					  setCurrency(currency);
					  setShipTo(shipTo);
					  setShipAddress(shipAddress);
					  setCreditLimit(creditLimit);
					  setCod(cod);
					  setMessage(message);
					  setExceptionNo(exceptionNo);
			}

			public void setId(String id){
				this.id = id;
			}

			public void setCode(String code){
				this.code = code;
			}
			public void setName(String name){
				this.name = name;
			}
			public void setStatus(String status){
				this.status = status;
			}
			public void setAddress(String address){
				this.address = address;
			}
			public void setPhone(String phone){
				this.phone = phone;
			}
			public void setFax(String fax){
				this.fax = fax;
			}
			public void setContact(String contact){
				this.contact = contact;
			}
			public void setEmail(String email){
				this.email = email;
			}
			public void setDiscount(double discount){
				this.discount = discount;
			}
			public void setDiscountDays(int discountDays){
				this.discountDays = discountDays;
			}
			public void setCurrency(String currency){
				this.currency = currency;
			}

			public void setShipTo(String shipTo){
				this.shipTo = shipTo;
			}

			public void setShipAddress(String shipAddress){
				this.shipAddress = shipAddress;
			}

			public void setCreditLimit(double creditLimit){
				this.creditLimit = creditLimit;
			}

			public void setCod(double cod){
				this.cod = cod;
			}

			public void setMessage(String message){
				this.message = message;
			}

			public void setExceptionNo(String exceptionNo){
				this.exceptionNo = exceptionNo;
			}

			public String getId(){
				return this.id;
			}

			public String getCode(){
				return this.code;
			}

			public String getName(){
				return this.name;
			}

			public String getStatus(){
				return this.status;
			}

			public String getAddress(){
				return this.address;
			}

			public String getPhone(){
				return this.phone;
			}

			public String getFax(){
				return this.fax;
			}

			public String getContact(){
				return this.contact;
			}

			public String getEmail(){
				return this.email;
			}
			public double getDiscount(){
				return this.discount;
			}
			public int getDiscountDays(){
				return this.discountDays;
			}
			public String getCurrency(){
				return this.currency;
			}
			public String getType(){
				return this.type;
			}

			public String getShipAddress(){
				return this.shipAddress ;
			}

			public double getCreditLimit(){
				return this.creditLimit;
			}

			public double getCod(){
				return this.cod;
			}

			public String getMessage(){
				return this.message;
			}

			public String getExceptionNo(){
				return this.exceptionNo;
			}

	}