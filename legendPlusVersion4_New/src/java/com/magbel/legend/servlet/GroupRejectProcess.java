package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.BulkMail;
import com.magbel.legend.vao.Approval;
import com.magbel.legend.mail.EmailSmsServiceBus;

import java.io.*;

import magma.AssetRecordsBean;
import magma.GroupAssetToAssetBean;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.util.*;

public class GroupRejectProcess extends HttpServlet
{

	private ApprovalRecords service;
	private AssetRecordsBean bean;
	private BulkMail mail;
	private EmailSmsServiceBus email;
	private GroupAssetToAssetBean grpAsset;
	private String rej_am_grp_asset_main_status;
	private String rej_am_grp_asset_status;
	private String rej_am_asset_status;
	private String rej_am_asset_approval_status_reason;
    public GroupRejectProcess()
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
        email = new EmailSmsServiceBus();
        mail = new BulkMail();
        grpAsset = new GroupAssetToAssetBean();
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
        String userClass = (String)request.getSession().getAttribute("UserClass");
        
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String grp_Id = request.getParameter("grp_id");
        String page1 = request.getParameter("page1");
        String reject_reason = request.getParameter("reject_reason");
        System.out.println("<<<< Inside GroupRejectProcess Servlet >>>>" );
        System.out.println("reject_reason >>>>" + reject_reason);
        System.out.println("grp_Id >>>>" + grp_Id);
        System.out.println("page1 >>>>" + page1);
        
        rej_am_grp_asset_main_status = 
        	"update am_group_asset_main set Asset_Status='Rejected',raise_entry='N' where group_id = '"+grp_Id+"'";
        
    	rej_am_grp_asset_status = 
    		"update am_group_asset set Asset_Status='Rejected',raise_entry='N' where group_id = '"+grp_Id+"'";
    	
    	rej_am_asset_status = 
    		"update am_asset set Asset_Status='Rejected',raise_entry='N' where group_id = '"+grp_Id+"'";
    	
    	rej_am_asset_approval_status_reason = "update am_asset_approval set approval_level_count = 0, Asset_Status='Rejected'" +
    			",process_status='R', reject_reason='"+reject_reason + "'  where asset_id = '"+grp_Id+"'";
	    
	   
   try
   {
	   if (!userClass.equals("NULL") || userClass!=null){
	  //delete record from raise entry list using asset id and page name
	 if(grpAsset.deleteRaiseEntry(grp_Id,page1))
	 {
		 bean.updateAssetStatusChange(rej_am_grp_asset_main_status);
		 bean.updateAssetStatusChange(rej_am_grp_asset_status);
		 bean.updateAssetStatusChange(rej_am_asset_status);
		 bean.updateAssetStatusChange(rej_am_asset_approval_status_reason);
		 //service.updateRaiseEntry(grp_Id, "N");
		 //service.updateAssetStatus(grp_Id, "R");
		 String from="";
		 String msgText1="Rejection of Group Asset with Group Id '"+grp_Id+"' due to '"+reject_reason+"'";
		 String subject="Group Asset Creation Rejection";
		 String url="E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war";
		 String to="";
		 String userid="";
		 //send mail
		 /************************************************************/
		 //Temorarily commented
		//email.sendMailUser(grp_Id, subject, msgText1);
		/************************************************************/
		 ///email.sendEMailAsset(from, subject, msgText1, url, to, userid, id);

		 out.print("<script>window.opener.location.reload();</script>");
		 out.print("<script>alert('Group Asset Entry removed from posting!');window.close();</script>");
			
		 /*out.println("<script>alert('Group Asset Entry removed from posting !')</script>");
  	     out.println("<script>");
  	     out.println("window.close();");
  	     out.println("</script>");*/
  	     //response.sendRedirect("DocumentHelp.jsp?np=groupAssetPosting2&id="+ grp_Id + "&reject=Y");
  	     //out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting2&id="+ grp_Id + "&reject=Y");
  	     
	 }
	 else
	 { 
		 out.println("<script>alert('Group Asset Entry removal not successful!')</script>");
  	   	 out.println("<script>");
  	     out.println("window.close();");
  	     //out.println("window.location='DocumentHelp.jsp?np=groupAssetPosting2&id="+ grp_Id + "&reject=N");
  	     out.println("</script>");}
   }else {
		out.print("<script>alert('You have No Right')</script>");
	} 
   }
   catch(Exception e){e.printStackTrace();}
  }   
  
    
    public String getServletInfo()
    {
        return "Confirmation Action Servlet";
    }
}