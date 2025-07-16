package com.magbel.ia.servlet;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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
import com.magbel.util.DatetimeFormat;

/**
 * Servlet implementation class RequisitionServlet
 */
public class ReturnedServlet extends HttpServlet 
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
	SimpleDateFormat timer;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnedServlet()
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
		timer = new SimpleDateFormat("kk:mm:ss");
		
		cg = new CodeGenerator();
		PrintWriter out = response.getWriter();
		
		String realPath=getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
		System.out.println("realPath >>>>>>>>>>> " + realPath);
		
		FileInputStream fis =  new FileInputStream(realPath);
		
		String userID = request.getParameter("userid");	
		String reqnID = cg.generateCode("REQUISITION", "", "", "");
		String compCode = request.getParameter("comp_code");
		String reqnBranch = request.getParameter("requestBranch");
		String reqnSection = request.getParameter("ReqSection");
		String reqnDepartment = request.getParameter("requestDepartment");
		String reqnUserID = request.getParameter("requestFor");
		String remark = request.getParameter("remark");
		String itemType = request.getParameter("itemType");
		String itemRequested = request.getParameter("itemRequested");
		String itemQty = request.getParameter("no_of_items");
		String reqnCode = request.getParameter("reqnCode");
		String projectCode = request.getParameter("projectCode");
		String returnCategory = request.getParameter("returnCategory");
		String category = request.getParameter("category");
		String stockReturned = request.getParameter("stockReturned");
		String dstrbCode = request.getParameter("dstrbCode"); 
		String supervisorID = userID;
			
		String status="A";
		//String selStatus="select status_name from ST_Status where status_code ='' and company_code='"+compCode+"'";
		String supervisorName ="";
//		String supervisorNameQry="select Full_name,email,approve_level from mg_gb_user where user_ID ='"+request.getParameter("supervisor")+"'" +
//				" and company_code='"+compCode+"'";
		String supervisorNameQry = (new StringBuilder()).append("select Full_name,email,approval_limit from am_gb_user where user_ID ='").append(request.getParameter("supervisor")).append("'").toString();
		int apprvLevel=0;
//		String adm_Approv_Lvl_Qry="select approvallevel from ST_Approval_Level_Setup where transaction_type=" +
//				"'REQUISITION'"+ "and company_code='"+compCode+"'";
		String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='REQUISITION'";
		 int var =Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
		int  approvalLevelLimit =Integer.parseInt(request.getParameter("txnLevel"));
		String mtid = helper.getGeneratedId("am_asset_approval");
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
		
		String RetnInsertQry="insert into am_ad_Requisition (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
				" ReqnUserID,ItemType,ItemRequested,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor," +
				"company_code,Image,Remark,workStationIP,Quantity,distributedstatus,distributedQty,projectCode,category,returnedCategory,ReqnDate,ReqnType,RETURNED_STOCK,RETURNED_ORDERNO)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		String query = "INSERT INTO ST_TRANSACTION_APPROVAL (MTID,TRANS_CODE,TRANS_TYPE,DESCRIPTION,USERID,APPROVE_OFFICER," +
				"TRANS_DATE,STATUS,MAX_APPROVE_LEVEL,CONCURRENCE,ITEM_CODE,QUANTITY,REASON,COMPANY_CODE,WORKSTATIONIP)"+
        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
        		",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
        		",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
//		System.out.println(""+RetnInsertQry);
		boolean done = false;
		boolean result = false;
		boolean result2 = false;
		try 
	    {
//			System.out.println("<<<reqnID: "+reqnID+"   userID: "+userID+"  reqnBranch: "+reqnBranch+"  reqnSection: "+reqnSection+"   reqnDepartment: "+reqnDepartment+"   reqnUserID: "+reqnUserID+"  itemType: "+itemType);
//			System.out.println("<<<itemRequested: "+itemRequested+"    status: "+status+"  approvalLevelLimit: "+approvalLevelLimit+"   supervisorID: "+supervisorID+"   compCode: "+compCode+"  remark: "+remark+"    RemoteAddr: "+request.getRemoteAddr()+"   itemQty: "+itemQty);
//			System.out.println("<<<projectCode: "+projectCode+"   returnCategory: "+returnCategory+"   Date: "+persistenceServiceDAO.getDateTime(new java.util.Date()));
			con = persistenceServiceDAO.getConnection();
			pstmt = con.prepareStatement(RetnInsertQry);
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
			pstmt.setString(14, "N");
			pstmt.setString(15, remark);
			pstmt.setString(16, request.getRemoteAddr());
			pstmt.setString(17, itemQty);
			pstmt.setString(18, "pending");
			pstmt.setString(19, "0");
			pstmt.setString(20,projectCode);
			pstmt.setString(21,category);
			pstmt.setString(22,returnCategory);
			pstmt.setTimestamp(23, persistenceServiceDAO.getDateTime(new java.util.Date()));
			pstmt.setString(24,"R");  //********Requisition is Returned Materials/Goods
			pstmt.setString(25,stockReturned);
			pstmt.setString(26,dstrbCode);
			
			done = (pstmt.executeUpdate() == -1);
			System.out.println("done >>>>>>>>> " + done);
			
			if (! done)//successful
			{
				pstmt = con.prepareStatement(query);
				String id = helper.getGeneratedId("ST_TRANSACTION_APPROVAL");
				pstmt.setString(1,id);
                pstmt.setString(2,reqnID);
                pstmt.setString(3,reqnCode);
                pstmt.setString(4,"UNUSED RETURNED");
                pstmt.setString(5,userID);
                pstmt.setString(6,supervisorID);
                pstmt.setTimestamp(7, persistenceServiceDAO.getDateTime(new java.util.Date()));
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

	            pstmt = con.prepareStatement(ins_am_asset_approval_qry);
	            pstmt.setString(1, reqnID);
	            pstmt.setString(2, userID);
	            pstmt.setString(3, supervisorID);
	            pstmt.setTimestamp(4, persistenceServiceDAO.getDateTime(new java.util.Date()));
	            pstmt.setString(5, remark);
	            pstmt.setTimestamp(6, persistenceServiceDAO.getDateTime(new java.util.Date()));
	            pstmt.setString(7, reqnBranch);
	            pstmt.setString(8, status);
	            pstmt.setString(9, "UNUSED RETURNED");
	            pstmt.setString(10, "P");
	            pstmt.setString(11, timer.format(new java.util.Date()));
	            pstmt.setString(12, mtid);
	            pstmt.setString(13, mtid);
	            pstmt.setInt(14, var);         
	            result2 = pstmt.executeUpdate() == -1;
	            System.out.println("result in ReturnedServlet for am_asset_approval >>>>>>>>> " + result2);                
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
	    	out.print("<script>window.location='DocumentHelp.jsp?np=RequisitionFormUpdate&" +
	    			"ReqnID="+reqnID+"&CompCode="+compCode+"&image=N'</script>");   	
	    }
	    else if ((! result)&&(status.equalsIgnoreCase("E")))
	    {
	    	out.print("<script>alert('Requisition Successfully sent to Admin Department  for Approval.')</script>");
	    	out.print("<script>window.location='DocumentHelp.jsp?np=RequisitionFormUpdate&" +
	    			"ReqnID="+reqnID+"&CompCode="+compCode+"&image=N'</script>");   	
	    }
	    else
	    {
	    	
	    }
		
	}

}
