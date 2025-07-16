package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
//import com.magbel.legend.mail.BulkMail;
import com.magbel.legend.vao.Approval;
import com.magbel.legend.mail.EmailSmsServiceBus;

import java.io.*;

import magma.AssetRecordsBean;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import   magma.asset.manager.*;
import   magma.net.manager.RaiseEntryManager;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.magbel.util.DataConnect;

import magma.asset.manager.WIPAssetManager;

import com.magbel.legend.bus.ApprovalManager;

public class RejectWIPProcess extends HttpServlet
{

	private ApprovalRecords service;
	private AssetRecordsBean bean;
	//private BulkMail mail;
	private EmailSmsServiceBus email;
        private WIPAssetManager wipam = new WIPAssetManager();
         private ApprovalManager approvalManager;


    public RejectWIPProcess()
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
        approvalManager = new ApprovalManager();
       // mail = new BulkMail();
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
        AssetManager assManager = new AssetManager();
        RaiseEntryManager raiseMan = new RaiseEntryManager();
        String userClass = (String) session.getAttribute("UserClass");
        
        PrintWriter out = response.getWriter();
        String id = request.getParameter("asset_id");
        String page1 = request.getParameter("page1");
        String thirdPartyLabel = request.getParameter("ThirdPartyLabel"); 
        String reject_reason = request.getParameter("reject_reason");
	reject_reason = "Posting Level: " +reject_reason;

        int tranIdRepost = Integer.parseInt(request.getParameter("tranId"));
        int userId = request.getParameter("userid")==null?0:Integer.parseInt(request.getParameter("userid"));
        String systemIp= request.getRemoteAddr();

                String assetCode = request.getParameter("assetCode");
            //System.out.println("================ Inside RejectWIPProcess ====================");
       // System.out.println("id >>>>>>>>>>>> " + id );
       // System.out.println("page1 >>>>>>>>>>>> " + page1 );
       // System.out.println("reject_reason >>>>>>>>>>>> " + reject_reason );

   try
   {
	   if (!userClass.equals("NULL") || userClass!=null){
       approvalManager.infoFromRejection(tranIdRepost,userId,systemIp,reject_reason);
        int tranId = service.getTranIdForRejetPost(page1,id);
        // System.out.println("tranId >>>>>>>>>>>> " + tranId );
	  //delete record from raise entry list base on asset id and page name
	 if(service.deleteRaiseEntry(id,page1) && !setRejectReason(id,reject_reason))
	 {
             //System.out.println("================ Successful ====================");
              wipam.updatePostRejectWIPInfo(id);
		 service.updateRaiseEntry(id, "N");
		 service.updateAssetStatus3(tranId, "RP","Rejected",reject_reason);
		 String from="";
		// String msgText1="Rejection of Asset with Asset Id '"+id+"' due to '"+reject_reason+"'";

                 String msgText1="Asset with Asset Id '"+id+"' has been rejected during posting due to '"+reject_reason+"' Please re-initiate the Asset Reclassification.";
                 String subject="WIP Reclassification Rejection";
		 String url="E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war";
		 String to="";
		 String userid="";
		 String mailId=  service.getCodeName("select asset_id from am_asset where asset_code='"+assetCode+"'");
		
		 ///email.sendEMailAsset(from, subject, msgText1, url, to, userid, id);

                 //u should update the status in am_wipassettransfer
                 String am_wip_reclassification_Qry="update am_wip_reclassification set approval_status='REJECTED'" +
                            " where asset_id='"+assManager.getTransferedOldAssetDetails(id)+"'";
                   // System.out.println("am_wip_reclassification_Qry >>>>>>> " + am_wip_reclassification_Qry);
                    raiseMan.setRaiseEntryStatus(am_wip_reclassification_Qry);
                   
                    email.sendMailUser(mailId, subject, msgText1);
                    //stop update on am_asset continued
                    service.clearTransactionEntry(id,page1,tranId,thirdPartyLabel);
		 out.println("<script>alert('Entry removed from posting !')</script>");
                 out.println("<script>");
  	         out.println("window.close();");
  	         out.println("</script>");

	 }
         else
         {
           out.println("<script>alert('Entry removal not successful!')</script>");
  	   out.println("<script>");
  	   out.println("window.close();");
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



 private boolean setRejectReason(String asset_id, String rejectReason){

     boolean b = true;
		 String query="update am_asset set asset_status='Rejected', " +
                         "post_reject_reason='"+rejectReason+"' where asset_id='"+asset_id+"'";
         Connection con = null;

	     PreparedStatement ps = null;
         ResultSet rs= null;

         try {
             con = (new DataConnect("")).getConnection();

              ps = con.prepareStatement(query);
              b =ps.execute();
            // System.out.println("=====the after successful updating with execute() command is " + b);

                    }

	         catch (Exception ex) {
		            System.out.println("WARNING: cannot update am_asset [setRejectReason]- > " +
		                    ex.getMessage());
		        } finally {
		           closeConnection(con, ps,rs);
		        }

                return b;
     }


private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
        try {
//System.out.println("=============inside closeConnection==========");
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            System.out.println("WANR:postingServlet Error closing connection >>" + e);
        }
    }


}