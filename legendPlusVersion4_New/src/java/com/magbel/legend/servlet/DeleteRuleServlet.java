package com.magbel.legend.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;



import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


//import legend.admin.objects.lost_password;
import legend.admin.objects.mail_setup;
import legend.admin.handlers.AdminHandler;

import com.magbel.legend.bus.ApprovalRecords;

import audit.AuditTrailGen;

import com.magbel.util.CheckIntegerityContraint;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class DeleteRuleServlet extends HttpServlet {
	public DeleteRuleServlet() {
	}

	/**
	 * Initializes the servlet.
	 * 
	 * @param config
	 *            ServletConfig
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		try {
			
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			response.setDateHeader("Expires", -1);
			
			 String userId="";
	         String branch = "";
	         String slaId = request.getParameter("slaId");
	         String rule_name = request.getParameter("rule_name");
	 		String DeptCode = "";
			String status = "";
			String slaStartDate = "";
			String slaEndDate = "";
			String SlaName = "";
			String Sladesc = "";
			String Category_Name = request.getParameter("Category_Name");
	        com.magbel.legend.bus.ApprovalRecords aprecords = new com.magbel.legend.bus.ApprovalRecords();
	        
	        DeptCode = aprecords.getCodeName("select Dept_code from am_gb_sla  where sla_ID='"+slaId+"'");
			 status = aprecords.getCodeName("select Status from am_gb_sla  where sla_ID='"+slaId+"'");
			 slaStartDate = aprecords.getCodeName("select SLA_Start_Date from am_gb_sla  where sla_ID='"+slaId+"'");
			 slaEndDate = aprecords.getCodeName("select SLA_End_Date from am_gb_sla  where sla_ID='"+slaId+"'");
			 SlaName = aprecords.getCodeName("select sla_name from am_gb_sla  where sla_ID='"+slaId+"'");
			 Sladesc = aprecords.getCodeName("select sla_description from am_gb_sla  where sla_ID='"+slaId+"'");
//		String slaId = request.getParameter("slaId");
//		String rule_name = request.getParameter("rule_name");
		System.out.println("-----slaId ---- "+slaId); 
		System.out.println("-----rule_name ---- "+rule_name);
		System.out.println("-----Category_Name ---- "+Category_Name);
		
		AdminHandler admin = new AdminHandler();
		
		 legend.admin.objects.User user = null;
		 
			admin.deleteRule(slaId,rule_name);
			//			out.print("<script>alert('We shall get back to you for the new Password.');</script>");
//						out.print("<script>window.location = 'DocumentHelp.jsp?np=slajsp'</script>");
		  			out.print("<script>window.location = 'DocumentHelp.jsp?np=slajsp&slaId="+ slaId + "&slaDeptCode="+DeptCode+ "&SlaName="+ SlaName+ "&Sladesc="+ Sladesc+ "&Status="+ status+ "&slaStartDate="+ slaStartDate+ "&slaEndDate="+ slaEndDate+ "&PC=2';</script>");

		}catch(Exception e) {
			e.getMessage();
		}
		
	}
       
}				