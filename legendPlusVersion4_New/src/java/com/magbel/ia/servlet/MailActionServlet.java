package com.magbel.ia.servlet;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.magbel.ia.util.*;

/**
 * Servlet implementation class MailActionServlet
 */
public class MailActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     ApplicationHelper2 applHelper;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailActionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		serviceRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		serviceRequest(request,response);
	}

	private void serviceRequest(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		applHelper = new ApplicationHelper2();
		
		 String flagType = request.getParameter("flag");;
		 String userId=request.getParameter("userID");;
         String mail_description=request.getParameter("mail_description");
         String tran_code =request.getParameter("transaction_type");
         String company_Code=request.getParameter("companyCode");
         String status=request.getParameter("status");
         String mail_address = request.getParameter("mail_address");
         String mailCode = request.getParameter("mailCode");
         
         String txnTypeQry = 
        	 "Select Description from IA_TRANSACTION_TYPE where Tran_code='"+tran_code+"' AND COMPANY_CODE='"+company_Code+"'" ;
         String txnTypeName = applHelper.descCode(txnTypeQry);
         
         /*System.out.println("Mail Action Servlet Parameters");
         System.out.println("FlagType >>>>>>>>>>> " + flagType);
         System.out.println("userId >>>>>>>>>>> " + userId);
         System.out.println("mail_description >>>>>>>>>>> " + mail_description);
         System.out.println("tran_code >>>>>>>>>>> " + tran_code);
         System.out.println("company_Code >>>>>>>>>>> " + company_Code);
         System.out.println("status >>>>>>>>>>> " + status);
         System.out.println("mail_address >>>>>>>>>>> " + mail_address);
         System.out.println("txnTypeName >>>>>>>>>>> " + txnTypeName);*/
         
         if (flagType.equalsIgnoreCase("insert"))
         {
        	 if(applHelper.SaveMailSetup(mail_description, mail_address, tran_code, userId, status, company_Code,txnTypeName))
        	 {
            	 out.print("<script>alert('Record saved successfully.');</script>"); 
        	 }
             else
        	 {
            	 out.print("<script>alert('Record not Saved,Please Try Again.');</script>"); 
        	 } 
        	 out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/manageMailSetup'</script>");
         }
         
         if (flagType.equalsIgnoreCase("update"))
         {
        	 String updateMailInfoQry=
        		 "update IA_mail_statement set Mail_description='"+mail_description+"',Mail_address='"+mail_address+"'," +
        		 "Status='"+status+"',User_id='"+userId +"',tran_Description='" +txnTypeName+"' "+
        		 "where company_code='"+company_Code+"' and Mail_code='"+mailCode+"'";
        	 
        	// System.out.println("updateMailInfoQry >>>>> " + updateMailInfoQry);
        	 
        	 if (applHelper.updateTable(updateMailInfoQry) > 0)
        	 {
        		 out.print("<script>alert('Record updated successfully.');</script>");	 
        	 }
        	 else
        	 {
        		 out.print("<script>alert('Update Not successfull.');</script>");
        	 }
        	 out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/manageMailSetup'</script>");
         }
	}

}
