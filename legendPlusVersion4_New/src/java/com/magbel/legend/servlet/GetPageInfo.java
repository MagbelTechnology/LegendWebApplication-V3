package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;
import java.io.*;
import magma.AssetRecordsBean;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.util.*;

public class GetPageInfo extends HttpServlet
{

	private ApprovalRecords service;
	private AssetRecordsBean bean;
    public GetPageInfo()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service = new ApprovalRecords();
        bean = new AssetRecordsBean();
        }
        catch(Exception et)
        {et.printStackTrace();}
    }
    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
       
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("asset_id");
        System.out.println("id "+id);
        String description = request.getParameter("description");
        System.out.println("desc "+description);
        String page = request.getParameter("pageName");
        System.out.println("page "+page);
        String flag="";
        String partPay = request.getParameter("partPayValue");
        System.out.println("part "+partPay);
        String operation = request.getParameter("operationSave");
        System.out.println("op "+operation);
        String branch_id1 = request.getParameter("branch_id1");
        System.out.println("branch_id1 "+branch_id1);
        String userId="";
        String branch = "";
        String Name= "";
	    String branchName= "";
	    //String subjectToVat = request.getParameter("subjectToVat");
	    //String whTax = request.getParameter("whTax");
	    String url = request.getParameter("url");
	    
	    
	   
 try
 {
	
	 legend.admin.objects.User user = null;
	 if(session.getAttribute("_user")!=null) 
	 { 
		 user =(legend.admin.objects.User)session.getAttribute("_user"); 
	     userId=user.getUserId(); 
	     branch=user.getBranch();
	     Name= service.getCodeName(" SELECT full_name from am_gb_user where user_id="+userId+"");
	     branchName= service.getCodeName(" SELECT branch_name from am_ad_branch where branch_id="+branch+"");
	 }
	 System.out.println("userId "+userId);
	 System.out.println("branch "+branch);
        if(operation==null)
        {
        	if(service.isApprovalExisting(id,page))
        	{
        		out.println("<script>alert('Entry already raised!')</script>");
        		out.println("<script>");
//    			out.println("window.location='DocumentHelp.jsp?np=assetDetails&status=ACTIVE&select=ACTIVE';");
    			 out.println("window.location='"+url+"';");
    			out.println("</script>");
        	}
        	else if(service.insertApproval(id, description, page, flag, partPay,Name,branchName,bean.subjectToVat(id),bean.whTax(id),url))
        	{ 
        	   url=url.replaceAll("&pageDirect=Y", "");
        	   service.updateRaiseEntry(id);
        	   out.println("<script>alert('Entry submitted for posting !')</script>");
        	   out.println("<script>");
        	   out.println("window.location='"+url+"';");
        	   out.println("</script>");
        	}
        	
        }
      if(operation=="update")
        {
        	service.updateApproval(id);
        	out.println("<script>");
			out.println("window.location='DocumentHelp.jsp';");
			out.println("</script>");
        }
    }
   catch(Exception e){e.printStackTrace();}
  }   
  
    public String getServletInfo()
    {
        return "Confirmation Action Servlet";
    }
}