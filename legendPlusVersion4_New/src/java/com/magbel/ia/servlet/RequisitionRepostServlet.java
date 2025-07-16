package com.magbel.ia.servlet;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.util.MailSender;
import com.magbel.ia.vao.User;

/**
 * Servlet implementation class RequisitionServlet
 */
public class RequisitionRepostServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	PersistenceServiceDAO persistenceServiceDAO = null; 
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	ApplicationHelper2 applHelper = null;
    ApplicationHelper helper;
	CodeGenerator cg = null;
	MailSender mailSender = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequisitionRepostServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		processRequisition(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		processRequisition(request,response);
	}

	private void processRequisition(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		applHelper = new ApplicationHelper2();
		helper = new ApplicationHelper();
		persistenceServiceDAO = new PersistenceServiceDAO();
		mailSender = new MailSender ();
		
		User loginId = (User) session.getAttribute("CurrentUser");
		cg = new CodeGenerator();
		PrintWriter out = response.getWriter();
		System.out.println("loginId >>>>>>>>>>> " + loginId);
		
		String realPath=getServletConfig().getServletContext().getRealPath("/mailConfig/ias.properties");
		System.out.println("realPath >>>>>>>>>>> " + realPath);
		
		FileInputStream fis =  new FileInputStream(realPath);
		
			        
		String reqnID = request.getParameter("reqnID");
		String compCode = loginId.getCompanyCode();
		String userID = loginId.getUserId();
		String reqnBranch = request.getParameter("requestBranch");
		String reqnSection = request.getParameter("ReqSection");
		String reqnDepartment = request.getParameter("requestDepartment");
		String reqnUserID = request.getParameter("requestBy");
		String remark = request.getParameter("remark");
		String itemType = request.getParameter("itemType");
		String itemRequested = request.getParameter("itemRequested");
		String itemQty = request.getParameter("no_of_items");
		String reqnCode = request.getParameter("reqnCode");
		String projectCode = request.getParameter("projectCode");
		String supervisorID = userID;
			
		String status="A";
		//String selStatus="select status_name from IA_Status where status_code ='' and company_code='"+compCode+"'";
		String supervisorName ="";
		String supervisorNameQry="select Full_name,email,approve_level from mg_gb_user where user_ID ='"+request.getParameter("supervisor")+"'" +
				" and company_code='"+compCode+"'";
		int apprvLevel=0;
		String adm_Approv_Lvl_Qry="select approvallevel from IA_Approval_Level_Setup where transaction_type=" +
				"'ADMIN APPROVAL'"+ "and company_code='"+compCode+"'";
		 int var =Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
		
		System.out.println("reqnBranch >>>>>>>>>>> " + reqnBranch);
		System.out.println("reqnID >>>>>>>>>>> " + reqnID);
		System.out.println("compCode >>>>>>>>>>> " + compCode);
		System.out.println("userID >>>>>>>>>>> " + userID);
		System.out.println("reqnSection >>>>>>>>>>> " + reqnSection);
		System.out.println("reqnDepartment >>>>>>>>>>> " + reqnDepartment);
		System.out.println("reqnUserID >>>>>>>>>>> " + reqnUserID);
		System.out.println("itemType >>>>>>>>>>> " + itemType);
		System.out.println("itemRequested >>>>>>>>>>> " + itemRequested);
		System.out.println("txnLevel >>>>>>>>>>> " + request.getParameter("txnLevel"));
		System.out.println("itemQty >>>>>>>>>>> " + itemQty);
		System.out.println("reqnCode >>>>>>>>>>> " + reqnCode);
		
		int  approvalLevelLimit =Integer.parseInt(request.getParameter("txnLevel"));
		
		String IA_ReqnDelQry="Delete   from  IA_Requisition where ReqnID='"+reqnID+"' and company_code='"+compCode+"'";
		
		String IA_TxnApprovDelQry="Delete   from  IA_TRANSACTION_APPROVAL where Trans_code='"+reqnID+"' and company_code='"+compCode+"'";
		
		String image_qry="select ID from IA_Requisition_Image where ReqnID='"+reqnID+"' and company_code='"+compCode+"'";
		
		String image="N";
		
		if(applHelper.descCode(image_qry)!= "")
		{
			image="Y";
		}
		applHelper.updateTable(IA_ReqnDelQry);
		applHelper.updateTable(IA_TxnApprovDelQry);
		
		if (approvalLevelLimit > 0)
		{
			status = "U";
			supervisorID = request.getParameter("supervisor");
			 String[] sprvResult=(applHelper.retrieveArray(supervisorNameQry)).split(":");
			supervisorName = sprvResult[0];
			if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
			{
				mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID);
			}
		}
		else
			
		{
			//send a mail to all members of the department
			//supervisor = userid
			status = "E";
			approvalLevelLimit=var;
			supervisorID = userID;
			mailSender.sendMailToAdmin(fis,reqnID,compCode);
		}
		
		String ReqnInsertQry="insert into IA_Requisition (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
				" ReqnUserID,ItemType,ItemRequested,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor," +
				"company_code,Image,Remark,workStationIP,Quantity,projectCode)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		
		
		String query = "INSERT INTO IA_TRANSACTION_APPROVAL (MTID,TRANS_CODE,TRANS_TYPE,DESCRIPTION,USERID,APPROVE_OFFICER," +
				"TRANS_DATE,STATUS,MAX_APPROVE_LEVEL,CONCURRENCE,ITEM_CODE,QUANTITY,REASON,COMPANY_CODE,WORKSTATIONIP)"+
        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		boolean done = false;
		boolean result = false;
		try 
	    {
			con = persistenceServiceDAO.getConnection();
			pstmt = con.prepareStatement(ReqnInsertQry);
			pstmt.setString(1, reqnID);
			pstmt.setString(2, userID);
			pstmt.setString(3, reqnBranch);
			pstmt.setString(4, reqnSection);
			pstmt.setString(5, reqnDepartment);
			pstmt.setString(6, reqnUserID);
			pstmt.setString(7, itemType);
			pstmt.setString(8, itemRequested);
			pstmt.setString(9, status);
			pstmt.setInt(10, 0);
			pstmt.setInt(11, approvalLevelLimit);
			pstmt.setString(12, supervisorID);
			pstmt.setString(13, compCode);
			pstmt.setString(14, image);
			pstmt.setString(15, remark);
			pstmt.setString(16, request.getRemoteAddr());
			pstmt.setString(17, itemQty);
			pstmt.setString(18,projectCode);
			done = (pstmt.executeUpdate() == -1);
			System.out.println("done >>>>>>>>> " + done);
			
			if (! done)//successful
			{
				pstmt = con.prepareStatement(query);
				String id = helper.getGeneratedId("IA_TRANSACTION_APPROVAL");
				pstmt.setString(1,id);
                pstmt.setString(2,reqnID);
                pstmt.setString(3,reqnCode);
                pstmt.setString(4,"REQUISITION");
                pstmt.setString(5,userID);
                pstmt.setString(6,supervisorID);
                pstmt.setDate(7,persistenceServiceDAO.dateConvert(new java.util.Date()));
                pstmt.setString(8,status);
                pstmt.setInt(9,approvalLevelLimit);
                pstmt.setInt(10,0);
                pstmt.setString(11,itemRequested);
                pstmt.setInt(12,Integer.parseInt(itemQty));
                pstmt.setString(13,remark);
                pstmt.setString(14, compCode);
                pstmt.setString(15, request.getRemoteAddr());
                result = (pstmt.executeUpdate() == -1);
                System.out.println("result >>>>>>>>> " + result);
			}
			
			
		}
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
			  if(con !=null) 
			  {
				 con.close();
				 
			  }
			  if(pstmt !=null) 
			  {
				  pstmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	    
	    if ((! result)&&(status.equalsIgnoreCase("U")))
	    {
	    	out.print("<script>alert('Requisition Successfully sent to "+ supervisorName+ "  for Approval.')</script>");
	    	out.print("<script>window.location='DocumentHelp.jsp?np=RequisitionFormRepostUpdate&" +
	    			"ReqnID="+reqnID+"&CompCode="+compCode+"&image="+image+"'</script>");   	
	    }
	    else if ((! result)&&(status.equalsIgnoreCase("E")))
	    {
	    	out.print("<script>alert('Requisition Successfully sent to Admin Department  for Approval.')</script>");
	    	out.print("<script>window.location='DocumentHelp.jsp?np=RequisitionFormRepostUpdate&" +
	    			"ReqnID="+reqnID+"&CompCode="+compCode+"&image="+image+"'</script>");   	
	    }
	    else
	    {
	    	
	    }
		
	}

}
