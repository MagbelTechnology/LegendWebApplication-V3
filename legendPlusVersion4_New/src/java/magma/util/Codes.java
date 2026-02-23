package magma.util;
import magma.net.dao.MagmaDBConnection;

import java.sql.*;


/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class Codes {
    private MagmaDBConnection dbConnection;

    private String delimiter;
    
	private String priority1;

	private String priority2;

	private String priority3;

	private String priority4;

	private String priority5;

	private String priority6;

    
	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public Codes(String priority1, String priority2, String priority3,
			String priority4, String priority5, String priority6,
			 String delimiter) {
		this.priority1 = priority1;
		this.priority2 = priority2;
		this.priority3 = priority3;
		this.priority4 = priority4;
		this.priority5 = priority5;
		this.priority6 = priority6;
		this.delimiter = delimiter;
	}


    public Codes() {
    	        dbConnection = new MagmaDBConnection();
    }

    public String getBranchCode(String BranchId)
    {
        String query =
               "SELECT BRANCH_CODE  FROM am_ad_branch  " +
               "WHERE BRANCH_ID = ? ";
      //  System.out.println("<<<<<query: "+query);
          
       String branchcode = "0";
       try {
    	   Connection con = dbConnection.getConnection("legendPlus");
           PreparedStatement stmt = con.prepareStatement(query);
           stmt.setString(1, BranchId);
           ResultSet rs = stmt.executeQuery();
           while (rs.next()) {

               branchcode = rs.getString(1);

           }

       } catch (Exception ex) {
           ex.printStackTrace();
       } 

       return branchcode;

    }
    public String getDeptCode(String DeptId)
   {
       String query =
              "SELECT DEPT_CODE  FROM am_ad_department  " +
              "WHERE DEPT_ID = ? ";
//       System.out.println("<<<<<query: "+query);
        
      String deptcode = "0";
      try {
    	  Connection con = dbConnection.getConnection("legendPlus");
          PreparedStatement stmt = con.prepareStatement(query);
          stmt.setString(1, DeptId);
          ResultSet rs = stmt.executeQuery();
          while (rs.next()) {

              deptcode = rs.getString(1);

          }

      } catch (Exception ex) {
          ex.printStackTrace();
      }

      return deptcode;

   }
   public String getSectionCode(String SectionId)
     {
         String query =
                "SELECT SECTION_CODE  FROM am_ad_section  " +
                "WHERE SECTION_ID = ? ";
 //        System.out.println("<<<<<query: "+query);
           
        String sectioncode = "0";
        try {
        	 Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement stmt = con.prepareStatement(query);
             stmt.setString(1, SectionId);
             ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                sectioncode = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } 

        return sectioncode;

     }
     public String getCategoryCode(String categoryId)
         {
             String query =
                 "SELECT CATEGORY_CODE  FROM am_ad_category  " +
                "WHERE category_id = ? ";
//             System.out.println("<<<<<query: "+query);
             
            String categorycode = "0";
            try {
            	 Connection con = dbConnection.getConnection("legendPlus");
                 PreparedStatement stmt = con.prepareStatement(query);
                 stmt.setString(1, categoryId);
                 ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    categorycode = rs.getString(1);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } 
            return categorycode;

     }
     
     
     
     public String MailMessage(String Mail_Code,String Transaction_Type)
 	{
 		String message="";
 		String query="SELECT Mail_Description FROM am_mail_statement where Mail_Code=? and Transaction_Type=? ";
 		
 		
 		try {
 			 Connection con = dbConnection.getConnection("legendPlus");
 	           PreparedStatement stmt = con.prepareStatement(query);
 	          stmt.setString(1, Mail_Code);
 	         stmt.setString(2, Transaction_Type);
 	           ResultSet rs = stmt.executeQuery();

 			while (rs.next()) 
 			 {

 				message  = rs.getString("Mail_Description");
 				

 			 }
 		   }
 			catch (Exception er) 
 			{
 			 er.printStackTrace();	
 				
 			} 
 			
 	return 	message;
 	}
     public String MailTo(String Mail_Code,String Transaction_Type)
  	{
  		String to="";
  		String query="SELECT mail_address FROM am_mail_statement where Mail_Code=? and Transaction_Type=? ";
  		
  		
  		try {
  			 Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement stmt = con.prepareStatement(query);
             stmt.setString(1, Mail_Code);
 	         stmt.setString(2, Transaction_Type);
             ResultSet rs = stmt.executeQuery();

  			while (rs.next()) 
  			 {

  				to  = rs.getString("mail_address");
  				

  			 }
  		   }
  			catch (Exception er) 
  			{
  			 er.printStackTrace();	
  				
  			} 
  			
  	return 	to;
  	}

     //database string to date format
   public String correctStringToDateformat(String Date)
 	{
 		String date = Date.substring(0,10);
 		String []parts = date.split("-");
 		String output = parts[2]+"-"+parts[1]+"-"+parts[0];
 	return 	output;
 	}

   public String getSubCategoryCode(String sub_categoryId)
   { 
       String query =
           "SELECT SUB_CATEGORY_CODE  FROM am_ad_sub_category  " +
          "WHERE sub_category_ID = ? ";
 
       
      String sub_categorycode = "0";
      try {
    	  Connection con = dbConnection.getConnection("legendPlus");
          PreparedStatement stmt = con.prepareStatement(query);
          stmt.setString(1, sub_categoryId);
          ResultSet rs = stmt.executeQuery();
          while (rs.next()) {

              sub_categorycode = rs.getString(1);

          }

      } catch (Exception ex) {
          ex.printStackTrace();
      }

      return sub_categorycode;

}
 
   public String ProofMailTo(String Mail_Code,String Transaction_Type)
	{
		String to="";
		String query="SELECT EMAIL_ADDRESS FROM am_branch_Manager where Branch_Code=? and Status='ACTIVE'";
		
		
		try {
			 Connection con = dbConnection.getConnection("legendPlus");
	           PreparedStatement stmt = con.prepareStatement(query);
	           stmt.setString(1, Mail_Code);
	           ResultSet rs = stmt.executeQuery();
			while (rs.next()) 
			 {

				to  = rs.getString("EMAIL_ADDRESS");
				

			 }
		   }
			catch (Exception er) 
			{
			 er.printStackTrace();	
				
			} 
			
	return 	to;
	}

	
	/**
	 * @return the priority1
	 */
	public String getPriority1() {
		return priority1;
	}

	/**
	 * @param priority1
	 *            the priority1 to set
	 */
	public void setPriority1(String priority1) {
		this.priority1 = priority1;
	}

	/**
	 * @return the priority2
	 */
	public String getPriority2() {
		return priority2;
	}

	/**
	 * @param priority2
	 *            the priority2 to set
	 */
	public void setPriority2(String priority2) {
		this.priority2 = priority2;
	}

	/**
	 * @return the priority3
	 */
	public String getPriority3() {
		return priority3;
	}

	/**
	 * @param priority3
	 *            the priority3 to set
	 */
	public void setPriority3(String priority3) {
		this.priority3 = priority3;
	}

	/**
	 * @return the priority4
	 */
	public String getPriority4() {
		return priority4;
	}

	/**
	 * @param priority4
	 *            the priority4 to set
	 */
	public void setPriority4(String priority4) {
		this.priority4 = priority4;
	}

	/**
	 * @return the priority5
	 */
	public String getPriority5() {
		return priority5;
	}

	/**
	 * @param priority5
	 *            the priority5 to set
	 */
	public void setPriority5(String priority5) {
		this.priority5 = priority5;
	}

	/**
	 * @return the priority6
	 */
	public String getPriority6() {
		return priority6;
	}

	/**
	 * @param priority6
	 *            the priority6 to set
	 */
	public void setPriority6(String priority6) {
		this.priority6 = priority6;
	}


}
