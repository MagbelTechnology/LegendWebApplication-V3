package com.magbel.ia.vao;

	public class Freight{
		
		private String code;
		private String name;
		private String id;
		
		public Freight(String id,String code, String name){
			setCode(code);
			setName(name);
			setId(id);
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
	}
	