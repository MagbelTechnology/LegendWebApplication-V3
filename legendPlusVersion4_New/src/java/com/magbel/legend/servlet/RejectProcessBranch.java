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


import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.magbel.util.DataConnect;

import magma.asset.manager.AssetManager;

import com.magbel.legend.bus.ApprovalManager;

import magma.AssetRecordsBean;

import legend.admin.handlers.CompanyHandler;

public class RejectProcessBranch extends HttpServlet {

    private ApprovalRecords service;
    private AssetRecordsBean bean;
    //private BulkMail mail;
    private EmailSmsServiceBus email;
    private AssetManager assetManager;
    private ApprovalManager approvalManager;
    private AssetRecordsBean assetRecordBeans;
    private CompanyHandler comp;
    java.text.SimpleDateFormat sdf;
    public RejectProcessBranch() {
    }
  
    public void init(ServletConfig config)
            throws ServletException {
        super.init(config);
        try {
            service = new ApprovalRecords();
            bean = new AssetRecordsBean();
            email = new EmailSmsServiceBus();
            assetManager = new AssetManager();
            approvalManager = new ApprovalManager();
            assetRecordBeans = new AssetRecordsBean();
            comp = new CompanyHandler();
            sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            // mail = new BulkMail();
        } catch (Exception et) {
            et.printStackTrace();
        }
    }

    public void destroy() {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String userClass = (String)request.getSession().getAttribute("UserClass");
        
        String userID =(String)request.getSession().getAttribute("CurrentUser");
        //System.out.println(" The current user in reject part is ============== "+ userID);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("asset_id");
        String page1 = request.getParameter("page1");
        String thirdPartyLabel = request.getParameter("ThirdPartyLabel"); 
        String reject_reason = request.getParameter("reject_reason");
//        System.out.println("VVVVVVVVVVVVVVVV tranId " + request.getParameter("tranId"));
        long tranIdRepost = Long.parseLong(request.getParameter("tranId"));
        String systemIp= request.getRemoteAddr();
        String dateApproved = sdf.format(new java.util.Date());
        //--reject_reason = "Posting Level: " + reject_reason;
        int userId = request.getParameter("userid")==null?Integer.parseInt(userID):Integer.parseInt(request.getParameter("userid"));
//       System.out.println("JJJJJJJJJJJ the asset id is JJJJJJ "+ id);
//       System.out.println("JJJJJJJJJJJ the page1 is JJJJJJ "+ page1);
       //stop update on AM_ASSET_UNCAPITALIZED continued
       String oldAsseID  =id;
         String assetCode = request.getParameter("assetCode");
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            int tranId = 0;
            String newAssetId="";
//            approvalManager.infoFromRejection(tranIdRepost,userId,systemIp,reject_reason);
             
            if (page1 != null && page1.equalsIgnoreCase("ASSET IMPROVEMENT RAISE ENTRY")) {
                  tranId = Integer.parseInt(service.getCodeName("select transaction_id from am_asset_approval a,am_Uncapitalized_improvement b where b.asset_id='" +
                        id + "' and a.tran_type='Uncapitalized Improvement' and  a.transaction_id = b.Revalue_ID "));
            }
            else
            if (page1 != null && page1.equalsIgnoreCase("ASSET TRANSFER RAISE ENTRY")) {
               
                 newAssetId =  service.getCodeName("select NEW_ASSET_ID from am_UncapitalizedTransfer where asset_id ='"+id+"'");
               
                  tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval a,AM_ASSET_UNCAPITALIZED b where b.asset_id='" +
                        id + "' and a.tran_type='Asset Transfer' and  a.asset_id = b.asset_id "));
                 id= newAssetId;

                 // tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval where asset_id ='"+oldAssetid+"'and tran_type='Asset Transfer'"));
               //-- reject_reason=reject_reason + " : new ID before rejection is "+id;
              
            }
            else if (page1 != null && page1.equalsIgnoreCase("ASSET RECLASSIFICATION RAISE ENTRY")) {
                // String oldAssetid =  service.getCodeName("select OLD_ASSET_ID from AM_ASSET_UNCAPITALIZED where asset_id ='"+id+"'");
                // tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval where asset_id ='"+oldAssetid+"'and tran_type='Asset Transfer'"));
               // reject_reason=reject_reason + " : new ID before rejection is "+id;
             //stop update on AM_ASSET_UNCAPITALIZED continued
              /* tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval a,AM_ASSET_UNCAPITALIZED b where b.asset_id='" +
                        id + "' and a.tran_type='Asset Reclassification' and  a.asset_id = b.old_asset_id ")); */
                tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval a,AM_ASSET_UNCAPITALIZED b where b.asset_code='" +
                		assetCode + "' and a.tran_type='Asset Reclassification'  "));
            }
            else
            if (page1 != null && page1.equalsIgnoreCase("FLEET MAINTENANCE RAISE ENTRY")) {   
            	String histId = request.getParameter("histId");
//            	System.out.println("the Id in reject servlet is <<<<<<<<<< " +histId+"   reject_reason: "+reject_reason);
                  tranId = Integer.parseInt(service.getCodeName("select max(transaction_id) from am_asset_approval a,FT_MAINTENANCE_HISTORY b where b.HIST_ID='" +
                 histId + "' and a.tran_type='Fleet Maintenance' and  a.asset_id = b.HIST_ID "));
                  assetRecordBeans.updateAssetStatusChange("update FT_MAINTENANCE_HISTORY set STATUS ='REJECTED' where HIST_ID ='"+histId +"'");
                  assetRecordBeans.updateAssetStatusChange("update am_asset_approval set process_status ='R',Asset_Status = 'REJECTED',reject_reason='"+reject_reason +"' where HIST_ID ='"+histId +"'");
                  assetRecordBeans.updateAssetStatusChange("update am_raisentry_post set entryPostFlag ='Y',GroupIdStatus = 'Y' where ID ='"+histId +"'");
            }
            else {
                tranId = service.getTranIdForRejetPost(page1, id);
            }

            //delete record from raise entry list base on asset id and page name
//            System.out.println(",,,,,,,,,,,,,,,,,,, the asset id is " + id);
//           System.out.println(",,,,,,,,,,,,,,,,,,, the page name is " + page1);
            if (service.deleteRaiseEntry(id, page1,tranId)) {

                service.updateRaiseEntry(id, "N");
                service.updateAssetStatus3(tranId, "R", "Rejected",reject_reason);
                //service.updateAssetStatus2(tranIdRepost, "RP", "Rejected");
                
                if (page1 != null && page1.equalsIgnoreCase("ASSET RECLASSIFICATION RAISE ENTRY")){
                
                String oldAssetId=service.getCodeName("select old_asset_id from AM_ASSET_UNCAPITALIZED where asset_id ='"+id+"'");
                
                service.reverseAssetReclassification(oldAssetId,id);
                   // System.out.println("the transaction id in reject servlet is <<<<<<<<<< " +tranIdRepost);
                assetRecordBeans.updateAssetStatusChange("update AM_ASSETRECLASSIFICATION set status ='REJECTED' where Reclassify_ID =" +tranIdRepost);
                }

                if (page1 != null && page1.equalsIgnoreCase("ASSET PART PAYMENT ENTRY")){
                  assetRecordBeans.updateAssetStatusChange("update AM_ASSET_UNCAPITALIZED set asset_status ='ACTIVE' where asset_ID ='"+id +"'");
//                  assetRecordBeans.updateAssetStatusChange("update AM_ASSET_PAYMENT  set status ='R', Reject_reason='"+reject_reason +"' where tranId =" +tranIdRepost);
                  service.updateAssetStatus2(tranIdRepost, "R", "Rejected");
                double lastDeduction = Double.parseDouble(service.getCodeName("select payment from AM_ASSET_PAYMENT where tranId=" +tranId));
                  //  System.out.println(" in reject >>>>> part payement >>> " + lastDeduction);
                    String reverseQuery ="update AM_ASSET_UNCAPITALIZED set amount_ptd=amount_ptd -"+lastDeduction+",amount_rem = amount_rem + "+lastDeduction+" where asset_id= '" +id+"'";
                  // System.out.println(">>>>>> Query "+reverseQuery);
                    
                    assetRecordBeans.updateAssetStatusChange(reverseQuery);

                    
                }

                String msgText1 = "";
                String subject = "";
//                String url = "E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war";
                String to = "";
                String userid = "";
                 String from = "";

                 if (page1 != null && page1.equalsIgnoreCase("ASSET RECLASSIFICATION RAISE ENTRY"))
                 {
                        msgText1=  "Asset with Asset Id '" + id + "' has been rejected during posting due to " + reject_reason + ".  Please re-initiate the Asset Reclassification.";
                         subject = "Asset Reclassification Rejection";
                        String mailId=  service.getCodeName("select asset_id from AM_ASSET_UNCAPITALIZED where asset_code='"+assetCode+"'");
                        assetRecordBeans.updateAssetStatusChange("update am_asset_approval  set process_status='R',asset_status ='REJECTED' where transaction_id =" +tranIdRepost);
 //                       email.sendMailUser(mailId, subject, msgText1);
                 	   String createdby = service.userEmail(service.userToEmail(mailId));
                       comp.insertMailRecords(createdby,subject,msgText1);
                 }
                 if (page1 != null && page1.equalsIgnoreCase("ASSET IMPROVEMENT RAISE ENTRY"))
                 {
                        msgText1=  "Asset with Asset Id '" + id + "' has been rejected during posting due to " + reject_reason + ".  Please re-initiate the Asset Improvement.";
                         subject = "Asset Improvement Rejection";
                        String mailId=  service.getCodeName("select asset_id from AM_ASSET_UNCAPITALIZED where asset_code='"+assetCode+"'");
                        email.sendMailUser(mailId, subject, msgText1);
                 	   String createdby = service.userEmail(service.userToEmail(mailId));
                       comp.insertMailRecords(createdby,subject,msgText1);   
                 }
                if(page1 != null && page1.equalsIgnoreCase("ASSET CREATION RAISE ENTRY")){
   //             	System.out.println("the message body >>>>> "+page1+"   Asset Id: "+id);
                	assetRecordBeans.updateAssetStatusChange("update am_asset_approval  set process_status='R',asset_status ='REJECTED' where transaction_id =" +tranIdRepost);
                	assetRecordBeans.updateAssetStatusChange("update AM_ASSET_UNCAPITALIZED set Asset_Status='REJECTED',post_reject_reason='" + reject_reason + "' where Asset_Id ='" + id + "' ");
                    subject = "Asset Creation Rejection";
                msgText1=  "Asset with Asset Id '" + id + "' has been rejected during posting due to " + reject_reason + ". Please re-create the asset.";
//               email.sendMailUser(id, subject, msgText1);
        	   String createdby = service.userEmail(service.userToEmail(id));
        	   comp.insertMailRecords(createdby,subject,msgText1);
                }
                if (page1 != null && page1.equalsIgnoreCase("FLEET MAINTENANCE RAISE ENTRY"))
                {
                	 String histId = request.getParameter("histId");
                	
                       msgText1=  "Fleet with Id " + histId + " and Asset Id " + id + " has been rejected during posting due to " + reject_reason + ".  Please re-initiate the Fleet  Transaction.";
                        subject = "Fleet Transaction Rejection";
 //                      email.sendMailUser(histId, subject, msgText1);
                       String createdby = service.userEmail(service.userToEmail(histId));
                       comp.insertMailRecords(createdby,subject,msgText1);
                       out.println("<script>alert('Entry removal not successful!')</script>");
                       out.println("<script>");
                       out.println("window.close(DocumentHelp.jsp?np=maintenanceRepsRecordDetails)");
                       out.println("</script>");
                }
                service.clearTransactionEntry(id,page1,tranId,thirdPartyLabel);

                 if(page1 != null && page1.equalsIgnoreCase("ASSET DISPOSAL RAISE ENTRY")){
                    subject = "Asset Disposal Rejection";
                msgText1=  "Asset with Asset Id '" + id + "' has been rejected during posting due to " + reject_reason + ". Please re-initiate the Asset Disposal.";
                }

                   if(page1 != null && page1.equalsIgnoreCase("ASSET TRANSFER RAISE ENTRY")){
                	   assetRecordBeans.updateAssetStatusChange("update am_asset_approval  set process_status='R',asset_status ='Rejected',DATE_APPROVED = '"+dateApproved+"' where transaction_id =" +tranIdRepost);
                	   assetRecordBeans.updateAssetStatusChange("update am_UncapitalizedTransfer  set approval_status ='Rejected' where TRANSFER_ID =" +tranIdRepost);
                       subject = "Asset Transfer Rejection";
              msgText1=  "Asset with Asset Id '" + id + "' has been rejected during posting due to " + reject_reason + ". Please re-initiate the Asset Transfer.";
                     //  msgText1= "Rejection of asset transfer with Asset Id '" + id + "' due to '" + reject_reason + "'. Please re-initiate the asset transfer.";
              id=oldAsseID;
                   }

                
                //send mail
//                System.out.println("the message body >>>>> "+msgText1);
               if(page1 != null && !page1.equalsIgnoreCase("ASSET CREATION RAISE ENTRY")){
               if (!setRejectReason(id, reject_reason,tranId,thirdPartyLabel)) {
  //              email.sendMailUser(id, subject, msgText1);   
            	   String createdby = service.userEmail(service.userToEmail(id));
                comp.insertMailRecords(createdby,subject,msgText1);
               }
               }
               if(page1 != null && page1.equalsIgnoreCase("ASSET DISPOSAL RAISE ENTRY")){
              	 assetRecordBeans.updateAssetStatusChange("update AM_ASSET_UNCAPITALIZED set asset_status ='ACTIVE' where asset_ID ='"+id +"'");
              	assetRecordBeans.updateAssetStatusChange("update am_asset_approval  set process_status='R',asset_status ='Rejected',DATE_APPROVED = '"+dateApproved+"' where transaction_id =" +tranIdRepost);
//              	 System.out.println("update AM_ASSET_UNCAPITALIZED set asset_status ='ACTIVE' where asset_ID ='"+id +"'");
               }
                out.println("<script>alert('Entry Asset successfully Rejected  !')</script>");
                out.println("<script>");
                out.println("window.close('updateAsset.jsp');");
                out.println("</script>");
            } else {
                out.println("<script>alert('Entry Asset successfully Rejected  !')</script>");
                out.println("<script>");
                out.println("window.close('updateAsset.jsp');");
                out.println("</script>");
            }
        }else {
    		out.print("<script>alert('You have No Right')</script>");
    	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServletInfo() {
        return "Confirmation Action Servlet";
    }

    private boolean setRejectReason(String asset_id, String rejectReason,int tranId, String thirdPartyLabel) {

        boolean b = true;
        String lpo =  "";
        String query = "update AM_ASSET_UNCAPITALIZED set asset_status='Rejected', post_reject_reason='" + rejectReason + "' where asset_id='" + asset_id + "'";
//        String query = "update AM_ASSET_UNCAPITALIZED set asset_status='REJECTED', post_reject_reason='' where asset_id='" + asset_id + "'";
        String trantype =  service.getCodeName("select TRAN_TYPE from am_asset_approval where batch_id='"+tranId+"'");
        if(trantype.equalsIgnoreCase("UNCAPITALIZED IMPROVEMENT RAISE ENTRY")){lpo = service.getCodeName("select lpoNum from am_Uncapitalized_improvement where REVALUE_ID='"+tranId+"'");
        query = "update AM_ASSET_UNCAPITALIZED set asset_status='ACTIVE', post_reject_reason=' ' where asset_id='" + asset_id + "'";
        assetRecordBeans.updateAssetStatusChange("update am_Uncapitalized_improvement set APPROVAL_STATUS = 'REJECTED',STATUS = 'REJECTED' where REVALUE_ID='"+tranId+"'");
        }
//        if(trantype.equalsIgnoreCase("Asset Reclassification")){lpo = service.getCodeName("select lpoNum from am_Uncapitalized_improvement where REVALUE_ID='"+tranId+"'");
////        System.out.println("=====trantype: "+trantype);
////        String Test = "update am_assetReclassification set STATUS = 'REJECTED' where Reclassify_ID='"+tranId+"'";
////        System.out.println("=====Test: "+Test);;
//        if(trantype.equalsIgnoreCase("Asset Reclassification")){
//        	assetRecordBeans.updateAssetStatusChange("update am_assetReclassification set STATUS = 'REJECTED' where Reclassify_ID='"+tranId+"'");}
//        }
 //       System.out.println("<<<<<lpo======: "+lpo+"     trantype======: "+trantype+"     thirdPartyLabel======: "+thirdPartyLabel);
        if(lpo!="" && thirdPartyLabel.equalsIgnoreCase("Y")){boolean querydel = service.deleteQuery("DELETE FROM  AM_INVOICE_NO WHERE LPO = '"+lpo+"' AND TRANS_TYPE = '"+trantype+"'");}
//        System.out.println("<<<<<lpo: "+lpo+"     trantype: "+trantype);
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = (new DataConnect("")).getConnection();

            ps = con.prepareStatement(query);
            b = ps.execute();
            // System.out.println("=====the after successful updating with execute() command is " + b);

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update AM_ASSET_UNCAPITALIZED [setRejectReason]- > " +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
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
