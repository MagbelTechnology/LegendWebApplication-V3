package com.magbel.ia.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.vao.*;
import com.magbel.util.DatetimeFormat;

/**
 * <p><h2>Project:</h2> <h1>Integrated Accounting Software</h1></p>
 <p>Title: ApplicationHelper2.java</p>
 *
 
 *
 * @author Ayojava 
 * @ e-mail ayojava@hotmail.com
 * @version 1.0
 */
public class ApplicationHelper2 extends PersistenceServiceDAO
{

    DatetimeFormat dateFormat;
    SimpleDateFormat sdf;
    ApplicationHelper ApplHelper ;
    
    public boolean SaveMailSetup(String mail_description, String mail_address,String tran_code, String userid,
    		String status,String companyCode,String txnTypeName)
	{
    		boolean done = false;
    		Connection con = null;
    		Statement stmt = null;
    		ResultSet  rs = null;
    		PreparedStatement ps = null;
    		
    		String query = "INSERT INTO IA_mail_statement"
					+ "(Mail_code, Mail_description,Tran_Code,Mail_address,User_id,Status,company_Code,Tran_Description)"
					+ " VALUES (?,?,?,?,?,?,?,?)";
    		 
    		ApplHelper = new  ApplicationHelper();
    		
    		try
    		{
    			con = getConnection();
    			ps = con.prepareStatement(query);
    			ps.setString(1, ApplHelper.getGeneratedId("IA_mail_statement"));
    			ps.setString(2, mail_description.toUpperCase());
    			ps.setString(3, tran_code);
    			ps.setString(4, mail_address);
    			ps.setString(5, userid);
    			ps.setString(6, status);
    			ps.setString(7, companyCode);
    			ps.setString(8, txnTypeName);
    			done=(ps.executeUpdate()!=-1);
    			//System.out.println("done >>>>>>>>> " + done);
    			if (done)
    			{
    				String updateStatusQry ="update IA_TRANSACTION_TYPE set MailStatus_Flag ='Assigned' where " +
    						"Tran_Code='"+tran_code+"' and Company_Code='"+companyCode+"'";
    				//System.out.println("updateStatusQry >>>>>>>>>>> " + updateStatusQry);
    				int result =updateTable(updateStatusQry);
    				//System.out.println("result of Update >>>>>>>>>>> "+result);
    			}
    			//System.out.println("output >>>>>>>>>>> "+done);
    		}
    		catch (Exception ex)
    		{
    			
    		}
    		finally
    		{
    			closeConnection(con,ps,rs);
    		}
    		return done;
	}

    public boolean SaveTxnApprovalLevelSetup(String transType, String level,String userID, String companyCode)
	{
    		boolean done = false;
    		Connection con = null;
    		Statement stmt = null;
    		ResultSet  rs = null;
    		PreparedStatement ps = null;
    		
    		String query = 
    			"INSERT INTO IA_APPROVAL_LEVEL_SETUP (mtid, Transaction_type,ApprovalLevel,User_id,company_Code)"
					+ " VALUES (?,?,?,?,?)";
    		 
    		ApplHelper = new  ApplicationHelper();
    		
    		
    		try
    		{
    			con = getConnection();
    			ps = con.prepareStatement(query);
    			ps.setString(1, ApplHelper.getGeneratedId("IA_APPROVAL_LEVEL_SETUP"));
    			ps.setString(2,transType);
    			ps.setString(3, level);
    			ps.setString(4, userID);
    			ps.setString(5, companyCode);
    			
    			done=(ps.executeUpdate()!=-1);
    			//System.out.println("done >>>>>>>>> " + done);
    		}
    		catch (Exception ex)
    		{
    			
    		}
    		finally
    		{
    			closeConnection(con,ps,rs);
    		}
    		return true;
	}

    public boolean SaveStatusSetup(String status_code, String status_name,String compCode, String user_ID)
	{
    		boolean done = false;
    		Connection con = null;
    		Statement stmt = null;
    		ResultSet  rs = null;
    		PreparedStatement ps = null;
    		
    		String query = 
    			"INSERT INTO IA_STATUS (mtid, status_code,status_name,User_id,company_Code)"
					+ " VALUES (?,?,?,?,?)";
    		 
    		ApplHelper = new  ApplicationHelper();
    		
    		try
    		{
    			con = getConnection();
    			ps = con.prepareStatement(query);
    			ps.setString(1, ApplHelper.getGeneratedId("IA_STATUS"));
    			ps.setString(2,status_code);
    			ps.setString(3, status_name);
    			ps.setString(4, user_ID);
    			ps.setString(5, compCode);
    			
    			done=(ps.executeUpdate()!=-1);
    			//System.out.println("done >>>>>>>>> " + done);
    		}
    		catch (Exception ex)
    		{
    			
    		}
    		finally
    		{
    			closeConnection(con,ps,rs);
    		}
    		return true;
	}

    public boolean InsertSectionForDeptComp(ArrayList list) 
    {
		String query = "INSERT INTO ia_sbu_dept_section(branchCode,deptCode,sectionCode,branchId"
				+ ",deptId,sectionId,gl_prefix,gl_suffix,mtid,company_code)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (DeptSection) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getSectionCode());
				ps.setString(4, bd.getBranchId());
				ps.setString(5, bd.getDeptId());
				ps.setString(6, bd.getSectionId());
				ps.setString(7, bd.getGl_prefix());
				ps.setString(8, bd.getGl_suffix());
				ps.setString(9, new ApplicationHelper().getGeneratedId("ia_sbu_dept_section"));
				ps.setString(10, bd.getCompCode());
				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error InsertSectionForDeptComp ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}
  
    public boolean updateSectionForDeptComp(ArrayList list) 
    {
		String query = "UPDATE ia_sbu_dept_section SET branchCode = ?"
				+ ",deptCode = ?,sectionCode=?,gl_prefix = ?"
				+ ",gl_suffix = ?" + " WHERE branchId=?" + " AND deptId=?"
				+ " AND sectionId=? AND company_code=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (DeptSection) list.get(i);

				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getSectionCode());
				ps.setString(4, bd.getGl_prefix());
				ps.setString(5, bd.getGl_suffix());
				ps.setString(6, bd.getBranchId());
				ps.setString(7, bd.getDeptId());
				ps.setString(8, bd.getSectionId());
				ps.setString(9, bd.getCompCode());
				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error updateSectionForDeptComp ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}

    public boolean insertDeptForBranchComp(ArrayList list) {

		String query = "INSERT INTO ia_sbu_branch_dept(branchCode"
				+ ",deptCode,branchId" + ",deptId,gl_prefix"
				+ ",gl_suffix,mtid,company_Code)" + " VALUES(?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getBranchId());
				ps.setString(4, bd.getDeptId());
				ps.setString(5, bd.getGl_prefix());
				ps.setString(6, bd.getGl_suffix());
				ps.setString(7, new ApplicationHelper().getGeneratedId("ia_sbu_branch_dept"));
				ps.setString(8, bd.getCompCode());
				ps.addBatch();
			}
			d = ps.executeBatch();
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching all asset ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}
    
    public boolean updateDeptForBranchComp(ArrayList list) {
		String query = "UPDATE ia_sbu_branch_dept SET branchCode = ?"
				+ ",deptCode = ?,gl_prefix = ?,gl_suffix = ?"
				+ " WHERE branchId=?" + " AND deptId=? AND company_Code=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			for (int i = 0; i < list.size(); i++) {
				bd = (BranchDept) list.get(i);
				ps.setString(1, bd.getBranchCode());
				ps.setString(2, bd.getDeptCode());
				ps.setString(3, bd.getGl_prefix());
				ps.setString(4, bd.getGl_suffix());
				ps.setString(5, bd.getBranchId());
				ps.setString(6, bd.getDeptId());
				ps.setString(7, bd.getCompCode());
				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error updateDeptForBranch ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}
   
    
    public boolean removeDeptFromBranchComp(ArrayList list) {
		String query = "DELETE FROM ia_sbu_branch_dept" + " WHERE branchId=?"
				+ " AND deptId=? AND company_code=?";
		String query2 = "DELETE FROM sbu_dept_section" + " WHERE branchId=?"
				+ " AND deptId=? AND company_code=?";

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		// ResultSet rs = null;
		BranchDept bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps2 = con.prepareStatement(query2);
			for (int i = 0; i < list.size(); i++) {
				bd = (BranchDept) list.get(i);

				ps.setString(1, bd.getBranchId());
				ps.setString(2, bd.getDeptId());
				ps.setString(3, bd.getCompCode());
				
				ps.addBatch();
				ps2.setString(1, bd.getBranchId());
				ps2.setString(2, bd.getDeptId());
				ps.setString(3, bd.getCompCode());
				ps2.addBatch();

			}

			d = ps.executeBatch();
			ps2.addBatch();
		} catch (Exception ex) {
			System.out.println("WARN: Error removeDeptFromBranchComp ->" + ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}
    
    
    public boolean removeSectionsFromDeptComp(ArrayList list) {
		String query = "DELETE FROM ia_sbu_dept_section" + " WHERE branchId=?"
				+ " AND deptId=? AND sectionId=?  AND company_code=?";
		Connection con = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		DeptSection bd = null;
		int[] d = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				bd = (DeptSection) list.get(i);

				ps.setString(1, bd.getBranchId());
				ps.setString(2, bd.getDeptId());
				ps.setString(3, bd.getSectionId());
				ps.setString(4, bd.getCompCode());
				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error removing Section From Department ->"+ ex);
		} finally {
			closeConnection(con, ps);
		}

		return (d.length > 0);
	}    
    public int updateTable(String query)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int result=0;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            result=ps.executeUpdate();
        } 
        catch (Exception ex) 
        {
            System.out.println("WARNING:ApplicationHelper2: cannot update +" + ex.getMessage());
        }
        finally 
        {
            closeConnection(con, ps);
        }
        return result;
    }
    
    public String descCode(String query)
    {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = "";

		try 
		{
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
			{
				description = rs.getString(1);
		    }
		} 
		catch (Exception ee) 
		{
			System.out.println("WARN:ApplicationHelper2.describeCode:->" + ee);
		} 
		finally 
		{
			closeConnection(con,ps,rs);
		}
		return description;
	}
    
    public String retrieveArray(String query)
    {
    //	System.out.println("<<<<<<<<<query: "+query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String apprvLevel = "";
		String apprvLevelLimit="";
		String image="";
		try 
		{
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
			{
				apprvLevel = rs.getString(1);
				apprvLevelLimit = rs.getString(2);
				image = rs.getString(3);
		    }
		} 
		catch (Exception ee) 
		{
			System.out.println("WARN:ApplicationHelper2.describeCode:->" + ee);
		} 
		finally 
		{
			closeConnection(con,ps,rs);
		}
		return (apprvLevel +":"+ apprvLevelLimit+":"+image);
	}
    public String retrieveEmailAddress(String query)
    {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String emailAdd = "";
		String result="";
		String image="";
		try 
		{
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
			{
				emailAdd = emailAdd+ rs.getString(1);
				emailAdd= emailAdd +  "," ;
				//System.out.println("emailAdd >>>>>>> " + emailAdd);
			}
		} 
		catch (Exception ee) 
		{
			System.out.println("WARN:ApplicationHelper2.retrieveEmailAddress:->" + ee);
		} 
		finally 
		{
			closeConnection(con,ps,rs);
		}
		return emailAdd;
	}
    
    public String formatDate(String date)
    {
    	String dd=date.substring(8, 10);
    	//System.out.println("dd>>>>" + dd);
		String mm=date.substring(4, 8);
		//System.out.println("mm>>>>" + mm);
		String yyyy=date.substring(0, 4);
		//System.out.println("yyyy>>>>" + yyyy);
		date =dd + mm+ yyyy; 
		return date;
    }
    
    public void insApprovRemarkQry  (String reqnid,String sprvID,String reqnDate,String remark,String status,String apprvLevel,
    		String IPAdd,String compCode,String emailAdd)
    {
    	String ins_IA_Approval_Remark_Qry="insert into Ia_Approval_Remark (Id,SupervisorId,DateRequisitioned,Remark,status," +
    			"ApprovalLevel,IPAddress,company_code,emailAddress) values (?,?,?,?,?,?,?,?,?)";
    	
    	Connection con = null;
		PreparedStatement ps = null;
		ResultSet  rs = null;
		boolean done = false;
		try
		{
			con = getConnection();
			ps = con.prepareStatement(ins_IA_Approval_Remark_Qry);
			ps.setString(1,reqnid);
			ps.setString(2,sprvID);
			ps.setString(3, "");
			ps.setString(4,remark);
			ps.setString(5,status);
			ps.setString(6,apprvLevel);
			ps.setString(7,IPAdd);
			ps.setString(8,compCode);
			ps.setString(9,emailAdd);
			done=(ps.executeUpdate()!=-1);
		}
		catch (Exception ee) 
		{
			System.out.println("WARN:ApplicationHelper2.insertApprovRemarkQry:->" + ee);
		} 
		finally 
		{
			closeConnection(con,ps,rs);
		}
		
    }
    
    public boolean createSecurityClass(SecurityClass sc)
    {
    	dateFormat = new DatetimeFormat();
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO MG_GB_CLASS(class_desc,class_name,is_supervisor,class_status"
				+ "    ,user_id,create_date ,class_id,is_storekeeper)"
				+ "    VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			ps.setString(5, sc.getUserId());
			ps.setDate(6, dateFormat.dateConvert(new java.util.Date()));
			//ps.setString(7, sc.getFleetAdmin());
			ps.setString(7, new ApplicationHelper().getGeneratedId("MG_GB_CLASS"));
			ps.setString(8, sc.getIsStoreKeeper());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
    
    public boolean updateSecurityClass(SecurityClass sc)
	{
    	
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_CLASS SET class_desc = ?,class_name = ?"
				+ "   ,is_supervisor = ?,class_status = ?,is_storekeeper=? "
				    + "   WHERE class_id = ?";

		/*System.out.println("sc.getDescription() >>>>>>>>>>> " + sc.getDescription());
		System.out.println("sc.getClassName() >>>>>>>>>>> " + sc.getClassName());
		System.out.println("sc.getIsSupervisor() >>>>>>>>>>> " + sc.getIsSupervisor());
		System.out.println("sc.getClassStatus() >>>>>>>>>>> " + sc.getClassStatus());
		System.out.println("sc.getIsStoreKeeper() >>>>>>>>>>> " + sc.getIsStoreKeeper());
		System.out.println("sc.getClassId() >>>>>>>>>>> " + sc.getClassId());
		*/
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, sc.getDescription());
			ps.setString(2, sc.getClassName());
			ps.setString(3, sc.getIsSupervisor());
			ps.setString(4, sc.getClassStatus());
			ps.setString(5, sc.getIsStoreKeeper());
			ps.setString(6, sc.getClassId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Security Class ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
    
    public boolean createUser(User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		dateFormat = new DatetimeFormat();
		String query = "INSERT INTO MG_GB_USER( User_Name, Full_Name, Legacy_Sys_Id"
				+ ", Class, Branch, Password, Phone_No, Is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date, Password_Expiry"
				+ ", Email,approve_level,COMPANY_CODE,user_id,is_storekeeper, branch_restriction,User_Restrict,is_StockAdministrator)"
				+ "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());
			ps.setString(11, user.getUserStatus());
			ps.setString(12, user.getCreatedBy());
			ps.setDate(13, dateFormat.dateConvert(new java.util.Date()));
			ps.setDate(14, dateFormat.dateConvert(dateFormat.addDayToDate(new java.util.Date(),Integer.parseInt(user.getPwdExpiry()))));
			//ps.setString(15, user.getFleetAdmin());
			ps.setString(15, user.getEmail());
            ps.setString(16,user.getApproveLevel());
			ps.setString(17, user.getCompanyCode());	
			ps.setString(18, new ApplicationHelper().getGeneratedId("MG_GB_USER"));
			ps.setString(19, user.getIsStorekeeper());
			ps.setString(20, user.getBranchRestrict());
			ps.setString(21, user.getUserRestrict());
			ps.setString(22, user.getIsStockAdministrator());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
    
    public boolean updateUser(User user) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE MG_GB_USER  SET  User_Name = ?, Full_Name = ?"
				+ " ,Legacy_Sys_Id = ?, Class = ?, Branch = ?, Password = ?, Phone_No = ?"
				+ ",Is_Supervisor = ?,Must_Change_Pwd = ?, Login_Status = ?, User_Status = ?"
				+ ", Email = ?,approve_level=?, COMPANY_CODE=? , is_storekeeper=?,branch_restriction=?,User_Restrict=?,is_StockAdministrator=? "
				+ "  WHERE User_Id = ?";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getLegacySystemId());
			ps.setString(4, user.getUserClass());
			ps.setString(5, user.getBranch());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getPhoneNo());
			ps.setString(8, user.getIsSupervisor());
			ps.setString(9, user.getMustChangePwd());
			ps.setString(10, user.getLoginStatus());

			ps.setString(11, user.getUserStatus());

		//	ps.setString(12, user.getFleetAdmin());
			ps.setString(12, user.getEmail());
            ps.setString(13,user.getApproveLevel());
            System.out.println("<<<<<<isStockAdministrator: "+user.getIsStockAdministrator()+"   isStorekeeper: "+user.getIsStorekeeper());
			ps.setString(14, user.getCompanyCode());
			ps.setString(15, user.getIsStorekeeper());
			ps.setString(16, user.getBranchRestrict());
			ps.setString(17, user.getUserRestrict());
			ps.setString(18, user.getIsStockAdministrator());
			ps.setString(19, user.getUserId());
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
    public static void main (String [] args)
    {
    	ApplicationHelper2 html = new ApplicationHelper2();
		//System.out.println("TranID >>>>>>>>>>>>> " + html.formatDate("2010-01-05 15:34:00.513"));
    	String result= "10:20";
    	String [] test = result.split(":");
    	/*System.out.println("Test >>>>>>>>>> " + test[0]);
    	System.out.println("Test >>>>>>>>>> " + test[1]);*/
    	boolean ans = Integer.parseInt(test[0])>Integer.parseInt(test[0]);
    	
    	String date="2010-06-07 00:00:00.0";
    	System.out.println("Year >>>>>>> " + date.substring(0, 4));
    	//System.out.println("Month >>>>>>> " + date.length());
    	System.out.println("Month >>>>>>> " + date.substring(5, 7));
    	System.out.println("Day >>>>>>> " + date.substring(8, 10));
    }
}
