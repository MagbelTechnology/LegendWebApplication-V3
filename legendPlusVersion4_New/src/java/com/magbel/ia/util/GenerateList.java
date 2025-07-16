package com.magbel.ia.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.magbel.ia.vao.*;
import com.magbel.ia.dao.PersistenceServiceDAO;

public class GenerateList extends PersistenceServiceDAO
{
	Connection con = null;
	Statement stmt = null;
	ResultSet  rs = null;
	PreparedStatement ps = null;
	Mail mailDAO = null;
	UserTransactionCode userTxnDAO =null;
	TransactionCode TxnDAO =null;
	Requisition reqn = null;
	ApplicationHelper helper;
	ApplicationHelper2 applhelper;
	CodeGenerator cg = null;
	DistributionOrder DstOrd;
	
	public ArrayList getMailInfoByCompanyCode(String filter)
	{
		ArrayList _list = new ArrayList();
		String query = " SELECT Mail_code,Mail_description,Mail_address,Create_date, company_code," +
        			   " User_id,Status,Tran_code FROM IA_mail_statement "+ filter;
		
		//System.out.println("query in MailInfo >>>>> " + query);
		
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				mailDAO = new Mail ();
				mailDAO.setMail_code(rs.getString("Mail_code"));
				mailDAO.setMail_description(rs.getString("Mail_description"));
				mailDAO.setMail_address(rs.getString("Mail_address"));
				mailDAO.setCreateDate(rs.getString("Create_date"));
				mailDAO.setTran_code(rs.getString("Tran_code"));
				mailDAO.setUser_ID(rs.getString("User_id"));
				mailDAO.setStatus(rs.getString("Status"));
				mailDAO.setCompany_Code(rs.getString("company_code"));
				_list.add(mailDAO);
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving MailInfoByCompanyCode ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return _list;
	}
	
	public ArrayList getApprovalLevelInfoByCompanyCode(String filter)
	{
		ArrayList _list = new ArrayList();
		String query = " SELECT mtid,Transaction_Type,ApprovalLevel,company_Code FROM IA_APPROVAL_LEVEL_SETUP "+ filter;
		
	//	System.out.println("query in ApprovalLevelInfo >>>>> " + query);
		
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				userTxnDAO = new UserTransactionCode();
				userTxnDAO.setCode(rs.getString("mtid"));
				userTxnDAO.setcompanyTranType(rs.getString("Transaction_Type"));
				userTxnDAO.setCompanyApprovalLevel(rs.getString("ApprovalLevel"));
				userTxnDAO.setCompanyCode(rs.getString("company_Code"));
				_list.add(userTxnDAO);
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving ApprovalLevelInfoByCompanyCode ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return _list;
	}
	
	public ArrayList getStatusInfoByCompanyCode(String filter)
	{
		ArrayList _list = new ArrayList();
		String query = " SELECT Status_Code,Status_Name,company_Code,mtid FROM IA_STATUS "+ filter;
		
		//System.out.println("query in getStatusInfoByCompanyCode >>>>> " + query);
		
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				TxnDAO = new TransactionCode();
				TxnDAO.setId(rs.getString("mtid"));
				TxnDAO.setStatusCode(rs.getString("Status_Code"));
				TxnDAO.setDescription(rs.getString("Status_Name"));
				TxnDAO.setCompanyCode(rs.getString("company_Code"));
				_list.add(TxnDAO);
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving getStatusInfoByCompanyCode ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return _list;
	}
	
	public TransactionCode getStatusInfoByCriteria(String statusCode,String companyCd)
	{
		String query = 
		" SELECT Status_Code,Status_Name,company_Code,mtid FROM IA_STATUS WHERE Status_Code='"+ statusCode+"' AND " +
				"company_Code ='"+companyCd+"'";
		
		//System.out.println("query in getStatusInfoByCriteria >>>>> " + query);
		TxnDAO = new TransactionCode();
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				TxnDAO.setId(rs.getString("mtid"));
				TxnDAO.setStatusCode(rs.getString("Status_Code"));
				TxnDAO.setDescription(rs.getString("Status_Name"));
				TxnDAO.setCompanyCode(rs.getString("company_Code"));
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving getStatusInfoByCriteria ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return TxnDAO;
	}
	
	public Mail getMailInfoByCriteria(String mailCd,String companyCd,String tran_code)
	{
		String qryMailInfo=
			" SELECT * FROM IA_mail_statement  where Mail_code='"+mailCd+"' and company_code='"+companyCd+"'" +
			" and Tran_code ='" +tran_code+"'";
		
		//System.out.println("qryMailInfo >>>>>>>>>>>>> " + qryMailInfo);
		mailDAO = new Mail ();
		
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(qryMailInfo);
			while(rs.next())
			{
				mailDAO.setMail_code(rs.getString("Mail_code"));
				mailDAO.setMail_description(rs.getString("Mail_description"));
				mailDAO.setMail_address(rs.getString("Mail_address"));
				mailDAO.setCreateDate(rs.getString("Create_date"));
				mailDAO.setTran_code(rs.getString("Tran_code"));
				mailDAO.setUser_ID(rs.getString("User_id"));
				mailDAO.setStatus(rs.getString("Status"));
				mailDAO.setCompany_Code(rs.getString("company_code"));
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving MailInfoByCriteria ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return mailDAO;
	}
	
	public UserTransactionCode getApprovalLevelSetupInfoByCriteria(String txnCode,String compCode)
	{
		String qryApprovalSetup=
			"select * from IA_APPROVAL_LEVEL_SETUP where mtid='"+txnCode+"' and company_code='"+compCode+"'";
		
		//System.out.println("qryApprovalSetup >>>>>>>>>>>>> " + qryApprovalSetup);
		
		userTxnDAO = new UserTransactionCode();
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(qryApprovalSetup);
			while(rs.next())
			{
				userTxnDAO.setCode(rs.getString("mtid"));
				userTxnDAO.setcompanyTranType(rs.getString("Transaction_Type"));
				userTxnDAO.setCompanyApprovalLevel(rs.getString("ApprovalLevel"));
				userTxnDAO.setCompanyCode(rs.getString("company_Code"));
			}
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error Retrieving getApprovalLevelSetupInfoByCriteria ->" + e.getMessage());
		}
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		return userTxnDAO;
	}
	
	public String getOptionItems(String selected, String query) {

	
		String html;
		html = "";
		String id = "";
		if (selected == null || selected.equals("null")) {
			selected = "0";
		}
		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				id = rs.getString(1);
				html = html + "<option  " +
					   ((id != null) && id.equalsIgnoreCase(selected) ? " selected" : "") +
					   " value='" + id + "'>" +
					   rs.getString(2) + "</option> ";
			}

		} catch (Exception ee) {
			System.out.println("WARN:HtmlComboRecord.getOptionItems:->" + ee);
		} finally {
			closeConnection(con,ps,rs);
		}

		return html;
	}
	public DeptSection findSectionInDeptComp(String branchid,String deptId, String sectionId,String companyCode) 
    {
		DeptSection dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode "
				+ ",gl_suffix,branchId,deptId,sectionId,mtid,Company_Code "
				+ " FROM ia_sbu_dept_section WHERE branchId='" + branchid
				+ "' AND deptId='" + deptId + "' AND sectionId='"
				+ sectionId + "' AND Company_Code='"+companyCode+"'";
		
		//System.out.println("findSectionInDept Query >>>>>>>>>>> " + query);
	

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");
				String sectioncode = rs.getString("sectionCode");
				String sectiondi = rs.getString("sectionId");
				String Company_Code = rs.getString("Company_Code");
				
				dept = new DeptSection();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);
				dept.setSectionCode(sectioncode);
				dept.setSectionId(sectiondi);
				dept.setCompCode(Company_Code);
				// _list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error findSectionInDept ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return dept;
	}
	
	public BranchDept findDeptInBranchComp(String branchid,String deptId,String companyCode)
	{
		BranchDept dept = null;

		String query = "SELECT branchCode,deptCode,gl_prefix"
				+ ",gl_suffix,branchId,deptId,mtid,Company_Code "
				+ " FROM ia_sbu_branch_dept WHERE branchId='" + branchid
				+ "' AND deptId='" + deptId +  "' AND Company_Code='"+companyCode+"'";
		
		System.out.println("query in findDeptInBranchComp >>>> " + query);

		
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String branchcode = rs.getString("branchCode");
				String deptcode = rs.getString("deptCode");
				String glprefix = rs.getString("gl_prefix");
				String glsuffix = rs.getString("gl_suffix");
				String branchId = rs.getString("branchId");
				String deptid = rs.getString("branchId");
				String mtid = rs.getString("mtid");
				String compCode =  rs.getString("Company_Code");

				dept = new BranchDept();
				dept.setBranchCode(branchcode);
				dept.setBranchId(branchId);
				dept.setDeptId(deptid);
				dept.setDeptCode(deptcode);
				dept.setGl_prefix(glprefix);
				dept.setGl_suffix(glsuffix);
				dept.setMtid(mtid);
				dept.setCompCode(compCode);
				// _list.add(dept);
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error findDeptInBranchComp ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return dept;
	}
	
	
	public Requisition findRequisitionById(String compCode,String reqnID)
	{
		reqn = new Requisition();
		
		String query="select * from am_ad_Requisition where ReqnId='"+reqnID+"' and Company_Code='"+compCode+"'";
		//System.out.println("query in findRequisitionById >>>> " + query);

		try 
		{
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next())
			{
				reqn.setUserId(rs.getString("UserId"));
				reqn.setRequestingBranch(rs.getString("ReqnBranch"));
				reqn.setRequestingSection(rs.getString("ReqnSection"));
				reqn.setRequestingDept(rs.getString("ReqnDepartment"));
				reqn.setReqnUserId(rs.getString("ReqnUserID"));
				reqn.setItemType(rs.getString("ItemType"));
				reqn.setItemRequested(rs.getString("ItemRequested"));
				reqn.setApprovLevel(rs.getInt("ApprovalLevel"));
				reqn.setAprovLevelLimit(rs.getInt("ApprovalLevelLimit"));
				reqn.setRemark(rs.getString("Remark"));
				reqn.setNo_Of_Items(rs.getString("Quantity"));
				reqn.setProjCode(rs.getString("projectCode"));
				reqn.setReturnedCategory(rs.getString("ReturnedCategory"));
				reqn.setReqnType(rs.getString("ReqnType"));
				reqn.setMeasuringCode(rs.getString("MEASURING_CODE"));
				reqn.setReturnedStock(rs.getString("RETURNED_STOCK"));
				reqn.setCategory(rs.getString("Category"));
		//		System.out.println(">>>>>>>Project Code: "+rs.getString("projectCode"));
			}    
		} 
		catch (Exception ex) 
		{
			System.out.println("WARN: Error findRequisitionById ->" + ex);
		} 
		finally 
		{
			closeConnection(con, ps);
		}
			
		return reqn;
	}
	
	public ArrayList findRequisitionRemarksByQry(String filterQry)
	{
		ArrayList _list = new ArrayList();
		
		String rmarkQry="Select * from IA_Approval_Remark " + filterQry;
		System.out.println("rmarkQry >>> " + rmarkQry);
		
		try 
		{
			con = getConnection();
			ps = con.prepareStatement(rmarkQry);
			rs = ps.executeQuery();
			while(rs.next())
			{
				reqn = new Requisition();
				reqn.setSupervisor(rs.getString("supervisorID"));
				reqn.setRemark(rs.getString("remark"));
				reqn.setRemarkDate(rs.getString("remarkDate"));
				reqn.setStatus(rs.getString("status"));
				_list.add(reqn);
			}
			
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error  findRequisitionRemarksById ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		
		return _list;
	}
	
	public ArrayList<DistributionOrder> findDistributionRemarksByQry(String filterQry)
	{
		ArrayList<DistributionOrder> _list = new ArrayList<DistributionOrder>();
		
		String rmarkQry="Select * from IA_Approval_Remark " + filterQry;
		System.out.println("rmarkQry >>> " + rmarkQry);
		
		try 
		{
			con = getConnection();
			ps = con.prepareStatement(rmarkQry);
			rs = ps.executeQuery();
			while(rs.next())
			{
				DstOrd = new DistributionOrder();
				DstOrd.setSupervisor(rs.getString("supervisorID"));
				DstOrd.setRemark(rs.getString("remark"));
				DstOrd.setRemarkDate(rs.getString("remarkDate"));
				DstOrd.setStatus(rs.getString("status"));
				_list.add(DstOrd);
			}
			
		} 
		catch (Exception e) 
		{
			System.out.println(this.getClass().getName()+ " Error  findDistributionRemarksByQry ->" + e.getMessage());
		}
		
		finally 
		{
			closeConnection(con, stmt, rs);
		}
		
		return _list;
	}
	public SecurityClass findSecurityClassByName(String filter) {

		SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date,is_storekeeper"
				+ "  FROM MG_GB_CLASS WHERE  class_name='" + filter + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("class_id");
				String description = rs.getString("class_desc");

				String className = rs.getString("class_name");

				String isSupervisor = rs.getString("is_supervisor");

				String classStatus = rs.getString("class_status");

				String userId = rs.getString("user_id");

				String createDate = rs.getString("create_date");
				
				String is_storekeeper = rs.getString("is_storekeeper");

				//String fleetAdmin = rs.getString("fleet_admin");
				sc = new SecurityClass(classId, description,
						className, isSupervisor, classStatus, userId,
						createDate,is_storekeeper);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}
	
	public SecurityClass findSecurityClassById(String filter)
	{

		com.magbel.ia.vao.SecurityClass sc = null;
		String query = "SELECT class_id ,class_desc,class_name ,is_supervisor"
				+ "  ,class_status,user_id,create_date,is_storekeeper "
				+ "  FROM MG_GB_CLASS WHERE  class_id='" + filter+"'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String classId = rs.getString("class_id");
				String description = rs.getString("class_desc");

				String className = rs.getString("class_name");

				String isSupervisor = rs.getString("is_supervisor");

				String classStatus = rs.getString("class_status");

				String userId = rs.getString("user_id");

				String createDate = rs.getString("create_date");
				
				String is_storekeeper = rs.getString("is_storekeeper");

				//String fleetAdmin = rs.getString("fleet_admin");
				sc = new com.magbel.ia.vao.SecurityClass(classId, description,
						className, isSupervisor, classStatus, userId,
						createDate,is_storekeeper);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return sc;

	}

	public User findUserByUserID(String UserID) {
		User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch,password_changed,approve_level,COMPANY_CODE,is_storekeeper,branch_restriction,User_Restrict "
				+ " FROM MG_GB_USER WHERE User_Id = '" + UserID + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                                String approveLevel = rs.getString("approve_level");
				String companyCode = rs.getString("COMPANY_CODE");
				String is_Storekeeper = rs.getString("is_storekeeper");
		        String branchRestrict= rs.getString("branch_restriction");
		        String userRestrict= rs.getString("User_Restrict");
				user = new User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setUserStatus(userStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);
				user.setUserStatus(userStatus);
				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                user.setApproveLevel(approveLevel);
				
				user.setCompanyCode(companyCode);
				user.setIsStorekeeper(is_Storekeeper);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}
	
	public User findUserByUserName(String UserName) {
		com.magbel.ia.vao.User user = null;
		String query = "SELECT User_Id, User_Name, Full_Name"
				+ ", Legacy_Sys_Id, Class, Branch, Password"
				+ ", Phone_No, is_Supervisor, Must_Change_Pwd"
				+ ", Login_Status, User_Status, UserId, Create_Date"
				+ ", Password_Expiry, Login_Date, Login_System"
				+ ", email, Branch,password_changed,approve_level,COMPANY_CODE , is_storekeeper,branch_restriction,User_Restrict "
				+ " FROM MG_GB_USER WHERE User_Name = '" + UserName + "'";

		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String userId = rs.getString("User_Id");
				String userName = rs.getString("User_Name");
				String fullName = rs.getString("Full_Name");
				String legacySysId = rs.getString("Legacy_Sys_Id");
				String Class = rs.getString("Class");
				String branch = rs.getString("Branch");
				String password = rs.getString("Password");
				String phoneNo = rs.getString("Phone_No");
				String isSupervisor = rs.getString("Is_Supervisor");
				String mustChangePwd = rs.getString("Must_Change_Pwd");
				String loginStatus = rs.getString("Login_Status");
				String userStatus = rs.getString("User_Status");
				String user_Id = rs.getString("UserId");
				String createDate = rs.getString("Create_Date");
				String passwordExpiry = rs.getString("Password_Expiry");
				String loginDate = rs.getString("Login_Date");
				String loginSystem = rs.getString("Login_System");
				//String fleetAdmin = rs.getString("Fleet_Admin");
				String email = rs.getString("email");
				String branchCode = rs.getString("Branch");
				String pwdChanged = rs.getString("password_changed");
                                String approveLevel = rs.getString("approve_level");
				String companyCode = rs.getString("COMPANY_CODE");
				String is_storekeeper = rs.getString("is_storekeeper");
		        String branchRestrict= rs.getString("branch_restriction");
		        String userRestrict= rs.getString("User_Restrict");

				user = new User();
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserFullName(fullName);
				user.setLegacySystemId(legacySysId);
				user.setUserClass(Class);
				user.setBranch(branch);
				user.setPassword(password);
				user.setPhoneNo(phoneNo);
				user.setIsSupervisor(isSupervisor);
				user.setMustChangePwd(mustChangePwd);
				user.setLoginStatus(loginStatus);
				user.setUserStatus(userStatus);
				user.setCreatedBy(user_Id);
				user.setCreateDate(createDate);

				user.setPwdExpiry(passwordExpiry);
				user.setLastLogindate(loginDate);
				user.setLoginSystem(loginSystem);
				//user.setFleetAdmin(fleetAdmin);
				user.setEmail(email);
				user.setBranch(branchCode);
				user.setPwdChanged(pwdChanged);
                user.setApproveLevel(approveLevel);
				user.setCompanyCode(companyCode);
				user.setIsStorekeeper(is_storekeeper);
				user.setBranchRestrict(branchRestrict);
				user.setUserRestrict(userRestrict);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return user;
	}
	
	public void insertIntoProcurement(String compCode,String reqnID,String IpAdd)
	{
		reqn = new Requisition();
		helper = new ApplicationHelper();
		cg = new CodeGenerator();
		boolean result = false;
		
		String sel_Ia_Reqn_Qry="Select ReqnBranch,ReqnSection,ReqnDepartment,ReqnDate,ReqnUserID," +
				"ItemType,ItemRequested,Image,Remark,Quantity,UserID from IA_Requisition " +
				" where ReqnID='"+reqnID+"' and company_code='"+compCode+"'";
		
		String procID = cg.generateCode("PROCUREMENT", "", "", "");
				
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sel_Ia_Reqn_Qry);
			while(rs.next())
			{
				reqn.setRequestingBranch(rs.getString("ReqnBranch"));
				reqn.setRequestingSection(rs.getString("ReqnSection"));
				reqn.setRequestingDept(rs.getString("ReqnDepartment"));
				reqn.setRequisitionDate(rs.getString("ReqnDate"));
				reqn.setReqnUserId(rs.getString("ReqnUserID"));
				reqn.setItemType(rs.getString("ItemType"));
				reqn.setItemRequested(rs.getString("ItemRequested"));
				reqn.setIsImage(rs.getString("Image"));
				reqn.setRemark(rs.getString("Remark"));
				reqn.setNo_Of_Items(rs.getString("Quantity"));
				reqn.setUserId(rs.getString("UserID"));
			}
			reqn.setCompany_code(compCode);
			reqn.setReqnID(reqnID);
			
			String procurementInsertQry=
			" insert into IA_PROCUREMENT  (mtid,ProcID,ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
			" ReqnDate, ReqnUserID,ItemType,ItemRequested,company_code,Image,Remark,workStationIP,Quantity,ordered )" +
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
			/*System.out.println("RequisitionDate >>>>>>>>>> " + reqn.getRequisitionDate());
			System.out.println("UserId >>>>>>>>>> " + reqn.getUserId());
			System.out.println("RequestingBranch >>>>>>>>>> " + reqn.getRequestingBranch());
			System.out.println("RequestingSection >>>>>>>>>> " + reqn.getRequestingSection());
			System.out.println("getRequestingDept >>>>>>>>>> " + reqn.getRequestingDept());
			System.out.println("getReqnUserId >>>>>>>>>> " + reqn.getReqnUserId());
			System.out.println("getItemType >>>>>>>>>> " + reqn.getItemType());
			System.out.println("getItemRequested >>>>>>>>>> " + reqn.getItemRequested());
			System.out.println("getCompany_code >>>>>>>>>> " + reqn.getCompany_code());
			System.out.println("getIsImage >>>>>>>>>> " + reqn.getIsImage());
			System.out.println("getRemark >>>>>>>>>> " + reqn.getRemark());
			System.out.println("getNo_Of_Items >>>>>>>>>> " + reqn.getNo_Of_Items());
			System.out.println("IpAdd >>>>>>>>>> " + IpAdd);*/
						
			ps = con.prepareStatement(procurementInsertQry);
			ps.setString(1, helper.getGeneratedId("IA_PROCUREMENT"));
			ps.setString(2, procID);
			ps.setString(3, reqn.getReqnID());
			ps.setString(4, reqn.getUserId());
			ps.setString(5, reqn.getRequestingBranch());
			ps.setString(6, reqn.getRequestingSection());
			ps.setString(7, reqn.getRequestingDept());
			ps.setString(8, reqn.getRequisitionDate());
			ps.setString(9, reqn.getReqnUserId());
			ps.setString(10, reqn.getItemType());
			ps.setString(11, reqn.getItemRequested());
			ps.setString(12, reqn.getCompany_code());
			ps.setString(13, reqn.getIsImage());
			ps.setString(14, reqn.getRemark());
			ps.setString(15, IpAdd);
			ps.setString(16, reqn.getNo_Of_Items());
			ps.setString(17, "N");
			result = (ps.executeUpdate() == -1);
           // System.out.println("result >>>>>>>>> " + result);
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally 
		{
			closeConnection(con, stmt, rs);
		}
		
	}
	
	public ArrayList<Requisition> findAllRequisition(String filter) 
    {
		ArrayList<Requisition> _list = new ArrayList<Requisition>();
		applhelper = new ApplicationHelper2();
		
		String query = " SELECT ReqnID,itemRequested,Remark,distributedQty,quantity,projectCode,measuring_code,ReqnBranch " +
				" "+ filter;
		String itemNameQry=" SELECT description from st_inventory_items where item_code='";
		
		try 
		{
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				reqn = new Requisition();
				reqn.setReqnID(rs.getString("reqnID"));
				//reqn.setItemRequested(applhelper.descCode(itemNameQry + rs.getString("itemRequested")+"'"));
				reqn.setItemRequested(rs.getString("itemRequested"));
				reqn.setRemark(rs.getString("Remark"));
				reqn.setNo_Of_Items(rs.getString("quantity"));
				reqn.setDistributedQty(rs.getString("distributedQty"));
				reqn.setProjCode(rs.getString("projectCode"));
				reqn.setUnitCode(rs.getString("measuring_code"));
				reqn.setRequestingBranch(rs.getString("ReqnBranch"));
				_list.add(reqn);
			}
		} 
		catch (Exception e) 
		{
		System.out.println(this.getClass().getName()+ " Error findAllRequisition ->" + e.getMessage());
		}
		
		finally 
		{
		closeConnection(con, stmt, rs);
		}
				return _list;
				
    }
}
