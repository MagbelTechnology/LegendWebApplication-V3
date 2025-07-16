/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.handlers;
/**
 *
 * @author Ganiyu
 */
import com.magbel.util.ApplicationHelper;
import com.magbel.admin.dao.MagmaDBConnection;
import java.sql.*;

import com.magbel.util.DatetimeFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.magbel.admin.objects.ComplaintRequisition;
import com.magbel.admin.handlers.MailSender;
import java.io.FileInputStream;
import com.magbel.admin.objects.ComplaintResponse;
import com.magbel.admin.dao.ConnectionClass;

public class ProblemManager extends ConnectionClass {
    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    ApplicationHelper applHelper = null;
    GenerateList genList = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
	  com.magbel.util.DatetimeFormat df;
    public ProblemManager() throws Exception {

        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            genList = new GenerateList();
            aprecords = new ApprovalRecords();
             df = new   com.magbel.util.DatetimeFormat();
        } catch (Exception ex) {
        }
    }


    public ArrayList findOldProblemResponses(String complaintId, int complaintNumber) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ID,recipient_Id,request_Description,user_id,status,create_date,create_time  "
               // + "from hd_response where complaint_id='" + complaintId + "' and complaint_Number=" + complaintNumber + " order by id";
                 + "from hd_response where complaint_id='" + complaintId + "' order by id";
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setId(rs.getInt(1));
                compResponse.setNewRecipient(rs.getInt(2));
                compResponse.setNewResponse(rs.getString(3));
                compResponse.setUserId(rs.getInt(4));
                compResponse.setStatus(rs.getString(5));
                compResponse.setReturnDate(dbConnection.formatDate(rs.getDate(6)));
                compResponse.setReturnTime(rs.getString(7));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findOldComplaintResponses ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
 

    public ComplaintResponse findProblemReqnByReqnCodes(String filter) {

      //  ComplaintRequisition compRequisition = null;
        ComplaintResponse compResponse = null;

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,services_affected,request_Section,request_Subject,request_Description,"
                + "create_time,technician from HD_PROBLEM " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
            	compResponse = new ComplaintResponse();
            	compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setservicesAffected(rs.getString("services_affected"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setTechnician(rs.getString("technician"));

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProblemReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compResponse;
    }


    public ComplaintResponse findsSolutionReqnByReqnCodes(String filter) {

      //  ComplaintRequisition compRequisition = null;
        ComplaintResponse compResponse = null;

      //  System.out.println(">>>>> filter <<<< "+filter);

        String rmarkQry = "select id,solution_id,Solution_title,Solution_topic,"
                + " Solution_Content,Solution_Keywords,create_date,status,Solution_Comments,user_id,"
                + " work_Station_IP,Company_Code,create_time from HD_SOLUTION " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
            	compResponse = new ComplaintResponse();
            	compResponse.setId(rs.getInt("id"));
                compResponse.setSolution_id(rs.getString("solution_id"));
                compResponse.setSolution_Title(rs.getString("Solution_title"));
                compResponse.setSolution_Topic(rs.getString("Solution_topic"));
                compResponse.setSolution_Content(rs.getString("Solution_Content"));
         //       System.out.print("++++ Solution_Content +++++ "+ rs.getString("Solution_Content"));
                compResponse.setSolution_Keywords(rs.getString("Solution_Keywords"));                
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));                
                compResponse.setSolution_Comments(rs.getString("Solution_Comments"));              
          //      System.out.print("++++ Solution_Comments +++++ "+ rs.getString("Solution_Comments"));     
          //      System.out.print("++++ solution_id +++++ "+ rs.getString("solution_id"));  
                compResponse.setUserID(rs.getInt("user_id"));                
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setCreatTime(rs.getString("create_time"));

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findsSolutionReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compResponse;
    }
    

    public ComplaintResponse findsTaskReqnByReqnCodes(String filter) {

      //  ComplaintRequisition compRequisition = null;
        ComplaintResponse compResponse = null;

     //   System.out.println(">>>>> filter <<<< "+filter);

        String rmarkQry = "select id,Task_id,Task_title,Task_topic,"
                + " Task_Content,create_date,status,Task_Comments,user_id,"
                + " work_Station_IP,Schedule_Start_Date,Schedule_End_Date,Actual_Start_Date,Actual_End_Date,Email_Me_Before,Technician from HD_TASK " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
            	compResponse = new ComplaintResponse();
            	compResponse.setId(rs.getInt("id"));
                compResponse.setTask_id(rs.getString("Task_id"));
                compResponse.setTask_Title(rs.getString("Task_title"));
                compResponse.setTask_Topic(rs.getString("Task_topic"));
                compResponse.setTask_Content(rs.getString("Task_Content"));
    //            System.out.println(">>>>> create_date <<<< ");
                compResponse.setCreateDate(rs.getString("create_date")); 
                compResponse.setStatus(rs.getString("status")); 
                compResponse.setTask_Comments(rs.getString("Task_Comments")); 
                compResponse.setUserID(rs.getInt("user_id"));  
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
   //             System.out.println(">>>>> Before Schedule_Start_Date <<<< ");
                compResponse.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
   //             System.out.println(">>>>> Schedule_Start_Date <<<< ");
                compResponse.setScheduleEndDate(rs.getString("Schedule_End_Date"));
      //          System.out.println(">>>>> Schedule_End_Date <<<< ");
                compResponse.setActualStartDate(rs.getString("Actual_Start_Date"));
     //           System.out.println(">>>>> Actual_Start_Date <<<< ");
                compResponse.setActualEndDate(rs.getString("Actual_End_Date"));
     //           System.out.println(">>>>> Actual_End_Date <<<< ");
                compResponse.setEmailMeBefore(rs.getString("Email_Me_Before"));
     //           System.out.println(">>>>> Email_Me_Before <<<< ");
                compResponse.setTechnician(rs.getString("technician")); 
    //            System.out.println(">>>>> technician <<<< ");
               
         //       System.out.print("++++ status +++++ "+ rs.getString("status"));     
         //       System.out.print("++++ Email_Me_Before +++++ "+ rs.getString("Email_Me_Before"));  
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in findsTaskReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compResponse;
    }
        

    public ComplaintResponse findsAnnouncementReqnByReqnCodes(String filter) {

      //  ComplaintRequisition compRequisition = null;
        ComplaintResponse compResponse = null;
String ToDate = "";
    //    System.out.println(">>>>> filter <<<< "+filter);
        String rmarkQry = "select id,Announce_id,create_date,status,user_id,"
            + " Announce_Title,work_Station_IP,Announce_Content,SEND_MAIL,Email_TO,Email_COPY,Company_Code,"
            + " Schedule_Start_Date,Schedule_End_Date,"
            + "Announcement_limit,create_time from HD_ANNOUNCEMENT " + filter;

         Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
            	compResponse = new ComplaintResponse();
            	compResponse.setId(rs.getInt("id"));
                compResponse.setAnnounce_id(rs.getString("Announce_id"));
                compResponse.setAnnounce_Title(rs.getString("Announce_Title"));                
                compResponse.setAnnounce_Content(rs.getString("Announce_Content"));
                compResponse.setCreateDate(rs.getString("create_date")); 
                compResponse.setStatus(rs.getString("status")); 
                compResponse.setSend_Mail(rs.getString("Send_Mail"));
                compResponse.setEmail_To(rs.getString("Email_TO"));
                compResponse.setEmail_Copy(rs.getString("Email_Copy"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setUserID(rs.getInt("user_id"));  
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
            //    compResponse.setScheduleEndDate(rs.getString("Schedule_End_Date"));
                compResponse.setRestrictAnnouncement(rs.getString("Announcement_limit"));
                String scheduleEndDate = rs.getString("Schedule_End_Date");
          
                
                if (scheduleEndDate == null || scheduleEndDate == "") {
                	ToDate = "N";
       	        } else {
       	        	ToDate = "Y";
               }	
         //       System.out.println("=====scheduleEndDate=====>>>> "+scheduleEndDate);
        /*        if (ToDate.equalsIgnoreCase("N")){ 
                    System.out.println("=====scheduleEndDate=====>>>> 2 "+scheduleEndDate);
                	compResponse.setScheduleEndDate(rs.getString("NULL"));
                 }   
        */
                if (ToDate.equalsIgnoreCase("Y")){ 
               //     System.out.println("=====scheduleEndDate=====>>>> 3 "+scheduleEndDate);
                	   compResponse.setScheduleEndDate(rs.getString("Schedule_End_Date"));  	   
                 }
              
             
                compResponse.setCreatTime(rs.getString("create_time"));
         //       System.out.print("++++ Send_Mail +++++ "+ rs.getString("Send_Mail"));     
         //       System.out.print("++++ Email_Me_Before +++++ "+ rs.getString("Email_Me_Before"));  
            }

             
        } catch (Exception ex) {
            System.out.println("Error occurred in findsAnnouncementReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compResponse;
    }
        
    
    public int updateProblemRes(ComplaintResponse compResponse) {
        String query_r = "update HD_PROBLEM set complain_category=?,complain_sub_category=?,priority=?,complaint_Type=?,"
                + "incident_Mode=?,asset_id=?,request_Subject=?,request_Description=?,technician=?,status=?,services_affected=?,Sla_id=?"
                + " where complaint_id = '" + compResponse.getReqnID()+"'" ;
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {  
           // System.out.println("===compResponse.getSlaid()== "+compResponse.getSlaid());
        	con = dbConnection.getConnection("helpDesk");
            
            ps = con.prepareStatement(query_r);
            ps.setString(1, compResponse.getCategory());
            ps.setString(2, compResponse.getSubCategory());
            ps.setString(3, compResponse.getPriority());
            ps.setString(4, compResponse.getComplainType());
            ps.setString(5, compResponse.getIncidentMode());
            ps.setString(6, compResponse.getAssetId());
            ps.setString(7, compResponse.getRequestSubject());
            ps.setString(8, compResponse.getRequestSubject() );
            ps.setString(9, compResponse.getTechnician());
            ps.setString(10, compResponse.getStatus());
            ps.setString(11, compResponse.getservicesAffected());
            ps.setInt(12, compResponse.getSlaid());
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateCompRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        
        return i;
    }//updateCompRequisition
    
    public int updateSolution(ComplaintResponse compResponse) {
    	
        String query_r = "update HD_SOLUTION set Solution_title=?,Solution_topic=?,Solution_Content=?,Solution_Keywords=?,"
                + "Solution_Comments=?,Status=?"
                + " where solution_id = '" + compResponse.getSolution_id()+"'" ;      
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
        	String Task_id = compResponse.getTask_id();
        	con = dbConnection.getConnection("helpDesk");            
            ps = con.prepareStatement(query_r);
            ps.setString(1, compResponse.getTask_Title());
            ps.setString(2, compResponse.getTask_Topic());
            ps.setString(3, compResponse.getTask_Content());
           // ps.setString(4, compResponse.getTask_Keywords());
            ps.setString(5, compResponse.getTask_Comments());
            ps.setString(6, compResponse.getStatus());
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateSolution()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        
        return i;
    }//updateCompRequisition

    public int updateTask(ComplaintResponse compResponse) {
        String query_r = "update HD_TASK set Task_title=?,Task_topic=?,Task_Content=?,Email_Me_Before=?,"
                + "Task_Comments=?,Status=?,Schedule_Start_Date=?,Schedule_End_Date=?,Actual_Start_Date=?,Actual_End_Date=?,Technician=?"
                + " where Task_id = '" + compResponse.getTask_id()+"'" ;      
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
             	
        	//System.out.println("==== compResponse.getScheduleStarteDate)== "+df.dateConvert(compResponse.getScheduleStarteDate()));
        	//System.out.println("==== compResponse.getScheduleEndDate()== "+compResponse.getScheduleEndDate());
        	//System.out.println("==== compResponse.getActualStartDate() == "+compResponse.getActualStartDate());
        	//System.out.println("==== compResponse.getActualEndDatee() == "+compResponse.getActualEndDatee());
        	//System.out.println("==== query_r== "+query_r);
        	String Task_id = compResponse.getTask_id();
        	con = dbConnection.getConnection("helpDesk");            
            ps = con.prepareStatement(query_r);
            ps.setString(1, compResponse.getTask_Title());
            ps.setString(2, compResponse.getTask_Topic());
            ps.setString(3, compResponse.getTask_Content());
            ps.setString(4, compResponse.getEmailMeBefore());
            ps.setString(5, compResponse.getTask_Comments());
            ps.setString(6, compResponse.getStatus());
            ps.setDate(7, df.dateConvert(compResponse.getScheduleStarteDate()));
            ps.setDate(8, df.dateConvert(compResponse.getScheduleEndDate()));
            ps.setDate(9, df.dateConvert(compResponse.getActualStartDate()));
            ps.setDate(10, df.dateConvert(compResponse.getActualEndDatee()));
            ps.setString(11, compResponse.getTechnician());
            i = ps.executeUpdate();
     
        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateTask()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        
        return i;
    }

    public int updateAnnouncements(ComplaintResponse compResponse) {
        String query_r = "update HD_ANNOUNCEMENT set Announce_Title=?,Announce_Content=?,SEND_MAIL=?,"
                + "Email_TO=?,Email_COPY=?,Schedule_Start_Date=?,Schedule_End_Date=?,Announcement_limit=?"
                + " where Announce_id = '" + compResponse.getAnnounce_id()+"'" ;      
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
    //    	System.out.print("===== updateAnnouncements =====> ");
        	String Announce_id = compResponse.getAnnounce_id();
        	con = dbConnection.getConnection("helpDesk");      
        	 String scheduleEndTime = compResponse.getScheduleEndDate();
   //     	   	System.out.print("===== updateAnnouncements scheduleEndTime =====> "+scheduleEndTime);
            ps = con.prepareStatement(query_r);
            ps.setString(1, compResponse.getAnnounce_Title());
            ps.setString(2, compResponse.getAnnounce_Content());
            ps.setString(3, compResponse.getSend_Mail());
            ps.setString(4, compResponse.getEmail_To());
            ps.setString(5, compResponse.getEmail_Copy());          
            ps.setDate(6, df.dateConvert(compResponse.getScheduleStarteDate()));
      //      System.out.println("=====Date from JSP ==== "+scheduleEndTime);
            if (scheduleEndTime ==("")){ 
         //   	   System.out.println("=====Date from JSP ==== 1 "+scheduleEndTime);
            	ps.setDate(7, null);
            }            			
            if (scheduleEndTime!=""){ 
        //    	   System.out.println("=====Date from JSP ==== 2 "+scheduleEndTime);
            ps.setDate(7, df.dateConvert(compResponse.getScheduleEndDate()));
            }         
            ps.setString(8, compResponse.getRestrictAnnouncement());
            i = ps.executeUpdate();
     
        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateAnnouncements()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        
        return i;
    }



    public ArrayList getRecipientEmail(String complaintId) {


        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String emailQry = "select recipient_Id,complainant,remark "
                + "from HD_COMPLAINT where COMPLAINT_ID='" + complaintId + "' and status='ACTIVE'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(emailQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();

                compResponse.setRemark(rs.getString("remark"));
                compResponse.setComplaint(rs.getString("complainant"));
                String recipientEmail = aprecords.getCodeName("select email from am_gb_user where user_id =" + rs.getInt("recipient_Id"));
                compResponse.setRecipientEmail(recipientEmail);
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  getRecipientEmail ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;

    }

    public void sendRecipientMail(FileInputStream fis, ArrayList recipientList, String complaintID) {

        if (recipientList != null && recipientList.size() > 0) {
            ComplaintRequisition compResponse = null;
            MailSender mailsender = new MailSender();

            for (int i = 0; i < recipientList.size(); i++) {
                compResponse = (ComplaintRequisition) recipientList.get(i);
                String recipientEmial = compResponse.getRecipientEmail();
                String mailHead = "Service Request:" + complaintID + " from " + compResponse.getComplaint();
                String mailBody = compResponse.getRemark();
                mailsender.sendMailToAUser(fis, recipientEmial, mailHead, mailBody);

            }


        }


    }

    public ArrayList findComplaintResponse(String complaintId, int complaintNumber) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ID,recipient_Id,returnTo_id,Action_on_self,self_reminder,self_reminder_mode, "
                + " return_time,return_time_mode,return_date,action_to_perform,response,user_id,close_activate_reason,status  "
                + "from hd_response where complaint_id='" + complaintId + "' and complaint_Number=" + complaintNumber + " order by id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setId(rs.getInt(1));
                compResponse.setNewRecipient(rs.getInt(2));
                compResponse.setNewReturnTo(rs.getInt(3));
                compResponse.setActionOnSelf(rs.getString(4));
                compResponse.setRemindTimeValue(rs.getInt(5));
                compResponse.setRemindTimeMode(rs.getString(6));
                compResponse.setReturnTimeValue(rs.getInt(7));
                compResponse.setReturnTimeMode(rs.getString(8));
                compResponse.setReturnDate(dbConnection.formatDate(rs.getDate(9)));
                compResponse.setActiontoPerform(rs.getString(10));
                compResponse.setNewResponse(rs.getString(11));
                compResponse.setUserId(rs.getInt(12));
                compResponse.setCloseActivateReason(rs.getString(13));
                compResponse.setStatus(rs.getString(14));

                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findComplaintResponse ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public void sendRecipientsMail(FileInputStream fis, String[] recipientList, String complaintID, String subject, String description, String mailSender) {



        if (recipientList != null && recipientList.length > 0) {

            MailSender mailsender = new MailSender();

            for (int i = 0; i < recipientList.length; i++) {
                String recipientEmial = recipientList[i];
                String mailHead = "Service Request:" + complaintID + " " + subject;
                String mailBody = description + "\n\n Regards,  \n\n" + mailSender;
                mailsender.sendMailToAUser(fis, recipientEmial, mailHead, mailBody);

            }


        }


    }

    public ArrayList findOldComplaintResponses(String complaintId, int complaintNumber) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ID,recipient_Id,request_Description,user_id,status,create_date,create_time  "
               // + "from hd_response where complaint_id='" + complaintId + "' and complaint_Number=" + complaintNumber + " order by id";
                 + "from hd_response where complaint_id='" + complaintId + "' order by id";
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setId(rs.getInt(1));
                compResponse.setNewRecipient(rs.getInt(2));
                compResponse.setNewResponse(rs.getString(3));
                compResponse.setUserId(rs.getInt(4));
                compResponse.setStatus(rs.getString(5));
                compResponse.setReturnDate(dbConnection.formatDate(rs.getDate(6)));
                compResponse.setReturnTime(rs.getString(7));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findOldComplaintResponses ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
 
    
    public int updatecompResponse(ComplaintRequisition compResponse) {
        String query_r = "update HD_COMPLAINT set complain_category=?,complain_sub_category=?,priority=?,complaint_Type=?,"
                + "incident_Mode=?,asset_id=?,request_Subject=?,request_Description=?,technician=?"
                + " where complaint_id = '" + compResponse.getReqnID()+"'" ;

        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            con = dbConnection.getConnection("helpDesk");

            ps = con.prepareStatement(query_r);
            ps.setString(1, compResponse.getCategory());
            ps.setString(2, compResponse.getSubCategory());
            ps.setString(3, compResponse.getPriority());
            ps.setString(4, compResponse.getComplainType());
            ps.setString(5, compResponse.getIncidentMode());
            ps.setString(6, compResponse.getAssetId());
            ps.setString(7, compResponse.getRequestSubject());
            ps.setString(8, compResponse.getRequestSubject() );
            ps.setString(9, compResponse.getTechnician());
            ps.setString(10, compResponse.getservicesAffected());
           
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updatecompResponse()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        
        return i;
    }//updatecompResponse
    public ArrayList findProcurmentWorkInProgressQryOverdueDate() {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where due_date < ? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, dateFormat.textDate2());
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQrydueDate() {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where due_date = ? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, dateFormat.textDate2());
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryRequestProblem(String complaintCode) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where complain_category = ? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, complaintCode);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryincidentMode(String incidentMode) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where incident_Mode = ? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, incidentMode);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryincidentMode(String incidentMode,String status) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where incident_Mode = ? and status=?"  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, incidentMode);
            ps.setString(2, status);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryincidentMode2(String status) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where incident_Mode is null and status=? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
      
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry); 
            ps.setString(1, status); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryincidentMode() 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where incident_Mode is null "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryTechnician()  
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compResponse = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT  where technician is null "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("helpDesk");
            ps = con.prepareStatement(rmarkQry); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compResponse = new ComplaintRequisition();
                compResponse.setId(rs.getInt("id"));
                compResponse.setComplaint(rs.getString("complaint_id"));
                compResponse.setCategory(rs.getString("complain_category"));
                compResponse.setSubCategory(rs.getString("complain_sub_category"));
                compResponse.setAssetId(rs.getString("asset_id"));
                compResponse.setPriority(rs.getString("priority"));
                compResponse.setCreateDate(rs.getString("create_date"));
                compResponse.setStatus(rs.getString("status"));
                compResponse.setComplainType(rs.getString("complaint_Type"));
                compResponse.setIncidentMode(rs.getString("incident_Mode"));
                compResponse.setUserID(rs.getInt("user_id"));
                compResponse.setResponseMode(rs.getString("response_Mode"));
                compResponse.setRequesterName(rs.getString("requester_Name"));
                compResponse.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compResponse.setIpAddress(rs.getString("work_Station_IP"));
                compResponse.setRecipientEmail(rs.getString("notify_Email"));
                compResponse.setRequestingBranch(rs.getString("request_Branch"));
                compResponse.setRequestingDept(rs.getString("request_Department"));
                compResponse.setCompany_code(rs.getString("Company_Code"));
                compResponse.setRequestingSection(rs.getString("request_Section"));
                compResponse.setRequestSubject(rs.getString("request_Subject"));
                compResponse.setRequestDescription(rs.getString("request_Description"));
                compResponse.setCreatTime(rs.getString("create_time"));
                compResponse.setDueDate(rs.getString("due_date"));
                _list.add(compResponse);
            }

        } catch (Exception e) {  
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }  
}
