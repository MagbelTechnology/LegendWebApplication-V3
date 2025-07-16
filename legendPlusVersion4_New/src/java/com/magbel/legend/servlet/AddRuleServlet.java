package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.handlers.AdminHandler;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddRuleServlet extends HttpServlet {
	public AddRuleServlet() {
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {

	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		 Properties prop = new Properties();
	        File file = new File("C:\\Property\\LegendPlus.properties");
	        FileInputStream input = new FileInputStream(file);
	        prop.load(input);
	        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
	 //       System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
	        String uploadPath = UPLOAD_FOLDER;

			String slaId = request.getParameter("choosen");
			String crt = request.getParameter("crt");
			String desc = request.getParameter("choosen");
			String userId = request.getParameter("userId");
			String slaDeptCode = request.getParameter("DeptCode");
			String sla_name = request.getParameter("sla_name");
			String Sladesc = request.getParameter("sla_description");
			String ruleSubmit = request.getParameter("Submit4");
			String slaStartDate = request.getParameter("slaStartDate");	
			String slaEndDate = request.getParameter("slaEndDate");
			
			 String[] ScrtSplit = crt.split("-", 2);
//				System.out.println("-----ScrtSplit[0] ---- "+ScrtSplit[0]+"    ========desc: "+desc+"    ======userId: "+userId+"    ===ScrtSplit[1]: "+ScrtSplit[1]);
				crt = ScrtSplit[0];
				AdminHandler admin = new AdminHandler();  
				
				 legend.admin.objects.User user = null;
			//	 String user_Id =(String)session.getAttribute("CurrentUser");
					
		  			admin.createCriteriaRule(slaId,crt,desc,userId);
			//			out.print("<script>alert('We shall get back to you for the new Password.');</script>");
			//			out.print("<script>window.location = 'DocumentHelp.jsp?np=slajsp'</script>");
		                out.print("<script>window.location='DocumentHelp.jsp?np=slajsp&"+"slaId=" + slaId + "&slaDeptCode=" + slaDeptCode +"&PC=2"+"&crt=" + crt+"&desc=" + desc +"&SlaName=" + sla_name +"&Sladesc=" + Sladesc +"&ruleSubmit=" + ruleSubmit+"&slaStartDate=" + slaStartDate+"&slaEndDate=" + slaEndDate +  "&Status=ACTIVE'</script>");

			
	}
}				