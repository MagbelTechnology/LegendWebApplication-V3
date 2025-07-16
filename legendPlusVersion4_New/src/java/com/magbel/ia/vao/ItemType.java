package com.magbel.ia.vao;

	public class ItemType{
		
		private String code;
		private String name;
		private String id;
        private String userId;
        private String inventory;
        private String costMethod;
        private String status;
        private String categoryCode;
        private String unitMeasurment;
		
		public ItemType(String id,String code, String name,String userId,
                String inventory,String costMethod,String status){
			setId(id);
			setCode(code);
			setName(name);
            setUserId(userId);
            setInventory(inventory);
            setCostMethod(costMethod);
            setStatus(status);
		}
		
			public void setCode(String code){
			this.code = code;
			}
			public void setName(String name){
			this.name = name;
			}
		
			public String getCode(){
			return this.code;
			}
			public String getName(){
			return this.name;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getInventory() {
        return inventory;
    }

    public void setCostMethod(String costMethod) {
        this.costMethod = costMethod;
    }

    public String getCostMethod() {
        return costMethod;
    }

   

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setUnitMeasurment(String unitMeasurment) {
        this.unitMeasurment = unitMeasurment;
    }

    public String getUnitMeasurment() {
        return unitMeasurment;
    }
        
}
