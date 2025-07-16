package com.magbel.legend.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.ComplaintManager;
import com.magbel.legend.mail.MailSender;
import com.magbel.util.ApplicationHelper;
import legend.admin.objects.ComplaintRequisition;
import com.magbel.util.Cryptomanager;
import legend.admin.handlers.SecurityHandler_07_11_2024;

public class HelpDeskResponseServlet extends HttpServlet {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    GenerateList genList = null;
    Cryptomanager cm = null;
    SecurityHandler_07_11_2024 sh = null;

    public HelpDeskResponseServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        processRequisition(request, response);
    }

    private void processRequisition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mgDbCon = new MagmaDBConnection();
        applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
        mailSender = new MailSender();
        aprecords = new ApprovalRecords();
        timer = new SimpleDateFormat("kk:mm:ss");
        genList = new GenerateList();

        cm = new Cryptomanager();
        sh = new SecurityHandler_07_11_2024();

        boolean doneSave = false;
        String successMessage = "";
        String errorMessage = "";



        int newRecipient = 0;
        int newReturnTo = 0;
        int remindTimeValue = 0;
        int returnTimeValue = 0;

        if ((request.getParameter("newRecipient") != null && !request.getParameter("newRecipient").equalsIgnoreCase(""))) {
            newRecipient = Integer.parseInt(request.getParameter("newRecipient"));

        }
       
        if ((request.getParameter("newReturnTo") != null && !request.getParameter("newReturnTo").equalsIgnoreCase(""))) {
            newReturnTo = Integer.parseInt(request.getParameter("newReturnTo"));

        }

        if ((request.getParameter("remindTimeValue") != null && !request.getParameter("remindTimeValue").equalsIgnoreCase(""))) {
            remindTimeValue = Integer.parseInt(request.getParameter("remindTimeValue"));

        }

        if ((request.getParameter("returnTimeValue") != null && !request.getParameter("returnTimeValue").equalsIgnoreCase(""))) {
            returnTimeValue = Integer.parseInt(request.getParameter("returnTimeValue"));

        }

        int userId = Integer.parseInt(request.getParameter("userid"));

        int complaintNumber = Integer.parseInt(request.getParameter("reqnNumber"));


        String complaintId = request.getParameter("reqnId");
        String actionOnSelf = request.getParameter("actionOnSelf");
        String remindTimeMode = request.getParameter("remindTimeMode");
        String returnTimeMode = request.getParameter("returnTimeMode");
        String returnDate = request.getParameter("returnDate");
        String actiontoPerform = request.getParameter("actiontoPerform");
        String newResponse = request.getParameter("newResponse");
        String operation = request.getParameter("operation");
        // String assetName = request.getParameter("itemRequested");

        String ackPassword = request.getParameter("ackPassword");

        String currentUserName = (String) request.getSession().getAttribute("SignInName");
        String currentpassQry = "select password from am_gb_user where user_name='" + currentUserName + "'";
        String dbpass = aprecords.getCodeName(currentpassQry);
        String pagepass = "";

        String statusMode = request.getParameter("statusMode");
         String currentStat = request.getParameter("currentStat");
         String closeReason = request.getParameter("closingReason");


         System.out.println("the statusMode " + statusMode );
         System.out.println("the currentStat " + currentStat);
         System.out.println("the closeReason " + closeReason);
         

        if (ackPassword != null && !ackPassword.equalsIgnoreCase("null")) {
            pagepass = cm.encrypt(sh.Name(currentUserName, ackPassword));

            if (dbpass.equalsIgnoreCase(pagepass)) {
                System.out.println("\n\n >>>> correect passord ");

                //to do send response
                if (operation != null && operation.equalsIgnoreCase("Send")) {

                    try {

                        if (statusMode != null && statusMode.equalsIgnoreCase("Active")) {
                            //TO DO : confirm password of user, only valid user should be able to progress beyond this point
                            //else return user to previous page.

                            String hdResponseQry = "insert into hd_response(complaint_id,recipient_Id,returnTo_id,Action_on_self,self_reminder,self_reminder_mode," +
                                    "return_time,return_time_mode,return_date,action_to_perform, response,user_id,workStationIP," +
                                    "create_date,create_time,complaint_Number,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            con = mgDbCon.getConnection("legendPlus");

                            pstmt = con.prepareStatement(hdResponseQry);
                            pstmt.setString(1, complaintId);
                            pstmt.setInt(2, newRecipient);
                            pstmt.setInt(3, newReturnTo);
                            pstmt.setString(4, actionOnSelf);
                            pstmt.setInt(5, remindTimeValue);
                            pstmt.setString(6, remindTimeMode);
                            pstmt.setInt(7, returnTimeValue);
                            pstmt.setString(8, returnTimeMode);
                            pstmt.setDate(9, mgDbCon.dateConvert(returnDate));
                            pstmt.setString(10, actiontoPerform);
                            pstmt.setString(11, newResponse);
                            pstmt.setInt(12, userId);
                            pstmt.setString(13, request.getRemoteAddr());
                            pstmt.setTimestamp(14, mgDbCon.getDateTime(new java.util.Date()));
                            pstmt.setString(15, timer.format(new java.util.Date()));
                            pstmt.setInt(16, complaintNumber);
                            pstmt.setString(17, statusMode);
                            doneSave = (pstmt.executeUpdate() == -1);
                            if (!doneSave) {
                                // update recipient_id colum of hd_complaint  with new recipient id

                                String newRecipientQry = " update HD_COMPLAINT set response_recipient_id= " + newRecipient + ", sender_id =" + userId + " where ID = " + complaintNumber;
                                //String newRecipientQry = " update HD_COMPLAINT set sender_id ="+userId+" where ID = " + complaintNumber;
                                genList.updateTable(newRecipientQry);


                                //send mail to recipient
                                String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legend.properties");
                                FileInputStream fis = new FileInputStream(realPath);
                                String recipientEmail = aprecords.getCodeName("select email from am_gb_user where user_id =" + newRecipient);
                                String msgHead = "Help Desk Request for Complaint ID " + complaintId;
                                String msgBody = actiontoPerform;
                                mailSender.sendMailToAUser(fis, recipientEmail, msgHead, msgBody);

                                String newRecipientName = aprecords.getCodeName("select full_name from am_gb_user where user_id =" + newRecipient);
                                successMessage = "Complaint Response sent to " + newRecipientName;
                                out.print("<script>alert('" + successMessage + "')</script>");
                                out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintResponseList'</script>");

                            }

                        }

                    } catch (Exception e) {

                        System.out.println(">> Error Occured in HelpDeskResponseServlet: " + e);
                    } finally {
                        try {
                            if (con != null) {
                                con.close();
                            }
                            if (pstmt != null) {
                                pstmt.close();
                            }
                            if (rs != null) {
                                rs.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }


                    // }

// to close a complaint
                    if (statusMode != null && (statusMode.equalsIgnoreCase("Reactivate") || statusMode.equalsIgnoreCase("Close"))) {

                        try {

                            //TO DO : confirm password of user, only valid user should be able to progress beyond this point
                            //else return user to previous page.
                            newRecipient = Integer.parseInt(aprecords.getCodeName( "select userid from HD_COMPLAINT where id = " +complaintNumber));
                            if (statusMode.equalsIgnoreCase("Reactivate")) {
                                statusMode = "Active";
                                 successMessage = "Complaint succesfully Activated.";
                            }

                             if (statusMode.equalsIgnoreCase("Close")) {
                              successMessage = "Complaint succesfully Closed.";
                            }
                            
                            String hdResponseQry = "insert into hd_response(complaint_id,recipient_Id,returnTo_id,Action_on_self,self_reminder,self_reminder_mode," +
                                    "return_time,return_time_mode,return_date,action_to_perform, response,user_id,workStationIP," +
                                    "create_date,create_time,complaint_Number,close_activate_reason,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            con = mgDbCon.getConnection("legendPlus");

                            pstmt = con.prepareStatement(hdResponseQry);
                            pstmt.setString(1, complaintId);
                            pstmt.setInt(2, newRecipient);
                            pstmt.setInt(3, newReturnTo);
                            pstmt.setString(4, actionOnSelf == null ? "" : actionOnSelf);
                            pstmt.setInt(5, remindTimeValue);
                            pstmt.setString(6, remindTimeMode == null ? "" : remindTimeMode);
                            pstmt.setInt(7, returnTimeValue);
                            pstmt.setString(8, returnTimeMode == null ? "" : returnTimeMode);
                            pstmt.setDate(9, returnDate == null || returnDate.equalsIgnoreCase("")  ? mgDbCon.dateConvert(new java.util.Date()) : mgDbCon.dateConvert(returnDate));
                            pstmt.setString(10, actiontoPerform == null ? "" : actiontoPerform);
                            pstmt.setString(11, newResponse == null ? "" : newResponse);
                            pstmt.setInt(12, userId);
                            pstmt.setString(13, request.getRemoteAddr());
                            pstmt.setTimestamp(14, mgDbCon.getDateTime(new java.util.Date()));
                            pstmt.setString(15, timer.format(new java.util.Date()));
                            pstmt.setInt(16, complaintNumber);
                            pstmt.setString(17, closeReason == null ? "" : closeReason);
                            pstmt.setString(18, statusMode == null ? "" : statusMode);

                            doneSave = (pstmt.executeUpdate() == -1);
                            if (!doneSave) {
                                // update recipient_id colum of hd_complaint  with new recipient id

                                String newRecipientQry = " update HD_COMPLAINT set status = '" + statusMode + "' ,response_recipient_id= " + newRecipient + ", sender_id =" + userId + " where ID = " + complaintNumber;
                                //String newRecipientQry = " update HD_COMPLAINT set sender_id ="+userId+" where ID = " + complaintNumber;
                                genList.updateTable(newRecipientQry);


                                //send mail to recipient
                                if (newRecipient != 0) {
                                    String realPath = getServletConfig().getServletContext().getRealPath("/legendPlus/legendPlus.properties");
                                    FileInputStream fis = new FileInputStream(realPath);
                                    String recipientEmail = aprecords.getCodeName("select email from am_gb_user where user_id =" + newRecipient);
                                    String msgHead = "Help Desk Request for Complaint ID " + complaintId;
                                    String msgBody = actiontoPerform;
                                    mailSender.sendMailToAUser(fis, recipientEmail, msgHead, msgBody);
                                    String newRecipientName = aprecords.getCodeName("select full_name from am_gb_user where user_id =" + newRecipient);
                                   // successMessage = "Complaint Response sent to " + newRecipientName;
                                } else {
                                   // successMessage = "Complaint succesfully Closed ";
                                }

                                out.print("<script>alert('" + successMessage + "')</script>");
                                out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintResponseList'</script>");

                            }


                        } catch (Exception e) {

                            System.out.println(">> Error Occured in HelpDeskResponseServlet: " + e);
                        } finally {
                            try {
                                if (con != null) {
                                    con.close();
                                }
                                if (pstmt != null) {
                                    pstmt.close();
                                }
                                if (rs != null) {
                                    rs.close();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        }


                    }

                }

            } else {
                errorMessage = "Incorrect Password";
                out.print("<script>alert('" + errorMessage + "')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintResponseForm&reqnId=" + complaintId + "&reqnNumber=" + complaintNumber + "'</script>");
                System.out.println("\n\n >>>>  passord NOT CORRECT");
                // to do return to previous page with invalid password message


            }


        } else {

            errorMessage = "Password field cannot be empty";
            out.print("<script>alert('" + errorMessage + "')</script>");
            out.print("<script>window.location='DocumentHelp.jsp?np=hdComplaintResponseForm&reqnId=" + complaintId + "&reqnNumber=" + complaintNumber + "'</script>");
            System.out.println("\n\n >>>>  passord CANNOT BE EMPTY");
            // to do return to previous page with password cannot be null message

        }


    }
}
