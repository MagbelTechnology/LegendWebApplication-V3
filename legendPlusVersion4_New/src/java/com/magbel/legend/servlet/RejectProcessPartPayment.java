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

public class RejectProcessPartPayment extends HttpServlet {

    private ApprovalRecords service;
    private AssetRecordsBean bean;
    //private BulkMail mail;
    private EmailSmsServiceBus email;
    private AssetManager assetManager;
    private ApprovalManager approvalManager;
    private AssetRecordsBean assetRecordBeans;
    public RejectProcessPartPayment() {
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
        String reject_reason = request.getParameter("reject_reason");
        System.out.println("VVVVVVVVVVVVVVVV tranId " + request.getParameter("tranId"));
        int tranId = Integer.parseInt(request.getParameter("tranId"));
        String systemIp= request.getRemoteAddr();
        reject_reason = "Posting Level: " + reject_reason;
        int userId = request.getParameter("userid")==null?Integer.parseInt(userID):Integer.parseInt(request.getParameter("userid"));
        //System.out.println("JJJJJJJJJJJ the asset id is JJJJJJ "+ id);
       // System.out.println("JJJJJJJJJJJ the page1 is JJJJJJ "+ page1);

        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            //int tranId = 0;

            approvalManager.infoFromRejection(tranId,userId,systemIp,reject_reason);

            if (service.deleteRaiseEntry(id, page1,tranId) && !setRejectReason(id, reject_reason)) {
                service.updateRaiseEntry(id, "N");
                service.updateAssetStatus3(tranId, "PR", "Rejected",reject_reason);
               if (page1 != null && page1.equalsIgnoreCase("ASSET PART PAYMENT ENTRY")){
                 assetRecordBeans.updateAssetStatusChange("update AM_ASSET set asset_status ='ACTIVE' where asset_ID ='"+id +"'");
                  assetRecordBeans.updateAssetStatusChange("update AM_ASSET_PAYMENT  set status ='RR', Reject_reason='"+reject_reason +"' where tranId =" +tranId);
                  service.updateAssetStatus2(tranId, "PR", "Rejected");
                double lastDeduction = Double.parseDouble(service.getCodeName("select payment from AM_ASSET_PAYMENT where tranId=" +tranId));
                  //  System.out.println(" in reject >>>>> part payement >>> " + lastDeduction);
                    String reverseQuery ="update AM_ASSET set amount_ptd=amount_ptd -"+lastDeduction+",amount_rem = amount_rem + "+lastDeduction+" where asset_id= '" +id+"'";
                  // System.out.println(">>>>>> Query "+reverseQuery);

                    assetRecordBeans.updateAssetStatusChange(reverseQuery);
                }

                String from = "";
                String msgText1 = "Rejection of asset with Asset Id '" + id + "' due to '" + reject_reason + "'";
                String subject = "Asset Creation Rejection";
                String url = "E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war";
                String to = "";
                String userid = "";
                //send mail
                email.sendMailUser(id, subject, msgText1);
                ///email.sendEMailAsset(from, subject, msgText1, url, to, userid, id);


                out.println("<script>alert('Entry removed from posting !')</script>");
                out.println("<script>");
                out.println("window.close();");
//                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry'</script>");
                out.println("</script>");
            } else {
                out.println("<script>alert('Entry removal not successful!')</script>");
                out.println("<script>");
                out.println("window.close();");
//                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry'</script>");
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

    private boolean setRejectReason(String asset_id, String rejectReason) {

        boolean b = true;
        String query = "update am_asset set asset_status='Rejected', post_reject_reason='" + rejectReason + "' where asset_id='" + asset_id + "'";
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = (new DataConnect("")).getConnection();

            ps = con.prepareStatement(query);
            b = ps.execute();
            // System.out.println("=====the after successful updating with execute() command is " + b);

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset [setRejectReason]- > " +
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
