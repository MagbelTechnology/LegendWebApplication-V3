package com.magbel.admin.objects;

public class Resignation {
	
	 	
	 	private String staffname;
	 	private int staffId;
	 	private String department;
	 	private String date_of_resumption;
	 	private String exit_type;
	 	private String exit_reason;
	 	private String section_unit;
	 	private int UserId = 12;
	 	
	    
   
		public Resignation()
		{
			 		
		}	 	

		public Resignation(String staffname,String department,String date_of_resumption,String exit_type,String exit_reason,String section_unit, String UserId, int staffId )
		{
				this.staffname = staffname;
				this.department = department;
				this.date_of_resumption = date_of_resumption;
				this.exit_type = exit_type;
				this.exit_reason = exit_reason;
				this.section_unit= section_unit;
				
				//this.UserId = UserId;
		
		}

		/**
		 * @return the date_of_resumption
		 */
		public String getDate_of_resumption() {
			return date_of_resumption;
		}

		/**
		 * @param date_of_resumption the date_of_resumption to set
		 */
		public void setDate_of_resumption(String date_of_resumption) {
			this.date_of_resumption = date_of_resumption;
		}

		/**
		 * @return the department
		 */
		public String getDepartment() {
			return department;
		}

		/**
		 * @param department the department to set
		 */
		public void setDepartment(String department) {
			this.department = department;
		}

		/**
		 * @return the exit_reason
		 */
		public String getExit_reason() {
			return exit_reason;
		}

		/**
		 * @param exit_reason the exit_reason to set
		 */
		public void setExit_reason(String exit_reason) {
			this.exit_reason = exit_reason;
		}

		/**
		 * @return the exit_type
		 */
		public String getExit_type() {
			return exit_type;
		}

		/**
		 * @param exit_type the exit_type to set
		 */
		public void setExit_type(String exit_type) {
			this.exit_type = exit_type;
		}

		/**
		 * @return the section_unit
		 */
		public String getSection_unit() {
			return section_unit;
		}

		/**
		 * @param section_unit the section_unit to set
		 */
		public void setSection_unit(String section_unit) {
			this.section_unit = section_unit;
		}

		/**
		 * @return the staffname
		 */
		public String getStaffname() {
			return staffname;
		}

		/**
		 * @param staffname the staffname to set
		 */
		public void setStaffname(String staffname) {
			this.staffname = staffname;
		}

		/**
		 * @return the userId
		 */
		public int getUserId() {
			return UserId;
		}

		/**
		 * @param userId the userId to set
		 */
		public void setUserId(int userId) {
			UserId = userId;
		}

		/**
		 * @return the staffId
		 */
		public int getStaffId() {
			return staffId;
		}

		/**
		 * @param staffId the staffId to set
		 */
		public void setStaffId(int staffId) {
			this.staffId = staffId;
		}

		

		
	  

}
