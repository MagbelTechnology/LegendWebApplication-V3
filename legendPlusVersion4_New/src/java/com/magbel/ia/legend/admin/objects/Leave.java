package com.magbel.ia.legend.admin.objects;

public class Leave {
	
	 	
	 	private String staffname;
	 	private int staffId;
	 	private String department;
	 	private String Last_Leave_Date;
	 	private String Effective_Date;
	 	private String Leave_Days;
	 	private String section_unit;
	 	private int UserId = 12;
	 	
	    
	 	public Leave()
		{
			
			
		}
		public Leave(String staffname,int staffId,String department,String Last_Leave_Date,String Effective_Date,String Leave_Days,String section_unit,int UserId)
		{
			this.staffname =staffname;
			this.staffId=staffId;
			this.department =department;
			this.Last_Leave_Date =Last_Leave_Date;
			this.Effective_Date =Effective_Date;
			this.Leave_Days =Leave_Days;
			this.section_unit =section_unit;
			this.UserId = UserId;
			
			 		
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
		 * @return the effective_Date
		 */
		public String getEffective_Date() {
			return Effective_Date;
		}
		/**
		 * @param effective_Date the effective_Date to set
		 */
		public void setEffective_Date(String effective_Date) {
			Effective_Date = effective_Date;
		}
		/**
		 * @return the last_Leave_Date
		 */
		public String getLast_Leave_Date() {
			return Last_Leave_Date;
		}
		/**
		 * @param last_Leave_Date the last_Leave_Date to set
		 */
		public void setLast_Leave_Date(String last_Leave_Date) {
			Last_Leave_Date = last_Leave_Date;
		}
		/**
		 * @return the leave_Days
		 */
		public String getLeave_Days() {
			return Leave_Days;
		}
		/**
		 * @param leave_Days the leave_Days to set
		 */
		public void setLeave_Days(String leave_Days) {
			Leave_Days = leave_Days;
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
		
		
		

		

		
	  

}
