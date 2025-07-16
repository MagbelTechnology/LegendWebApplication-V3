/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.bus;

/**


 * @author Ganiyu
 */
import com.magbel.util.ApplicationHelper;
//import com.magbel.admin.dao.MagmaDBConnection;
import magma.net.dao.MagmaDBConnection;
import java.sql.*;

import com.magbel.util.DatetimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import legend.admin.objects.ComplaintRequisition;
import com.magbel.admin.handlers.MailSender;

import java.io.FileInputStream;

import legend.admin.objects.ComplaintResponse;
import com.magbel.admin.dao.ConnectionClass;

public class ComplaintManager extends ConnectionClass {   

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    ApplicationHelper applHelper = null;
    GenerateList genList = null;
    ApprovalRecords aprecords = null;
    SimpleDateFormat timer = null;
    MagmaDBConnection mgDbCon = null;
    public ComplaintManager() throws Exception {
        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            genList = new GenerateList();
            aprecords = new ApprovalRecords();

        } catch (Exception ex) {
        }
    }

    public ComplaintRequisition findProcurementReqnByCode(String reqnId) {

        ComplaintRequisition compRequisition = null;


        String rmarkQry = "select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,"
                + "itemtype,ReqnBranch,ReqnDepartment,ReqnSection from PR_AWAIT_REQUISITION where reqnId='" + reqnId + "'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                //compRequisition.setUserId(rs.getString("ReqnUserID"));
                compRequisition.setReqnUserId(rs.getString("ReqnUserID"));
                compRequisition.setItemRequested(rs.getString("itemRequested"));
                compRequisition.setRequisitionDate(rs.getString("ReqnDate"));
                compRequisition.setReqnID(rs.getString("ReqnID"));
                //compRequisition.setTranID(rs.getString("transaction_id"));
                compRequisition.setStatus(rs.getString("status"));
                //compRequisition.setQuantity(rs.getInt("quantity"));
                compRequisition.setItemType(rs.getString("itemtype"));
                compRequisition.setRequestingBranch(rs.getString("ReqnBranch"));
                compRequisition.setRequestingDept(rs.getString("ReqnDepartment"));
                compRequisition.setRequestingSection(rs.getString("ReqnSection"));


            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByCode() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compRequisition;

    }

    public ComplaintRequisition findProcurementReqnByReqnCode(String reqnId) {

        ComplaintRequisition compRequisition = null;


        String rmarkQry = "select assetId,create_date,complaint_id,status,"
                + "equipt_category,origin_branch,origin_dept,origin_section,Remark,supervisor,complain_category,complainant from HD_COMPLAINT where complaint_id='" + reqnId + "'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                //compRequisition.setUserId(rs.getString("ReqnUserID"));
                //compRequisition.setReqnUserId(rs.getString("ReqnUserID"));
                compRequisition.setItemRequested(rs.getString("assetId"));
                compRequisition.setRequisitionDate(rs.getString("create_date"));
                compRequisition.setReqnID(rs.getString("complaint_id"));
                //compRequisition.setTranID(rs.getString("transaction_id"));
                compRequisition.setStatus(rs.getString("status"));
                //compRequisition.setQuantity(rs.getInt("quantity"));
                compRequisition.setItemType(rs.getString("equipt_category"));
                compRequisition.setRequestingBranch(rs.getString("origin_branch"));
                compRequisition.setRequestingDept(rs.getString("origin_dept"));
                compRequisition.setRequestingSection(rs.getString("origin_section"));
                compRequisition.setRemark(rs.getString("Remark"));
                compRequisition.setSupervisor(rs.getString("supervisor"));
                compRequisition.setProjectCode(rs.getString("complain_category"));
                compRequisition.setComplaint(rs.getString("complainant"));

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByReqnCode() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compRequisition;

    }

    public ArrayList findProcurmentWorkInProgressQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,create_dateTime,Close_Date from HD_COMPLAINT where complaint_id is not null " + filterQry +"";
//  System.out.println("====findProcurmentWorkInProgressQry rmarkQry== "+rmarkQry);
       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
//                compRequisition.setCreateDate(rs.getString("create_dateTime"));                
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 1 findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findProblemWorkInProgressQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time from HD_PROBLEM " + filterQry;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 2 findProblemWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    

    public ArrayList findSolutionWorkInProgressQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findSolutionWorkInProgressQry--- filterQry--- "+filterQry);
        String rmarkQry = "select id,Solution_id,create_date,status,user_id,"
                + " Solution_Title,Solution_Topic,work_Station_IP,Solution_Content,"
                + " Solution_Keywords,Company_Code,Solution_Comments,"
                + "create_time from HD_SOLUTION " + filterQry;
       // System.out.println("----IN findSolutionWorkInProgressQry--- rmarkQry--- "+rmarkQry);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("Solution_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setSolution_Title(rs.getString("Solution_Title"));
                compRequisition.setSolution_Topic(rs.getString("Solution_Topic"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setSolution_Content(rs.getString("Solution_Content"));
                compRequisition.setSolution_Keywords(rs.getString("Solution_Keywords"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));                
                compRequisition.setSolution_Comments(rs.getString("Solution_Comments"));
                compRequisition.setCreatTime(rs.getString("create_time"));

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findSolutionWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    
    public ArrayList findProcurmentPIPQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

        String rmarkQry = "select distinct complaint_id,subject,Create_Date,Status,trans_id,supervisor "
                + "from HD_COMPLAINT_SUMMARY " + filterQry;
        // System.out.println(" the query is >>>>>>>>  " + rmarkQry);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();

                compRequisition.setRequisitionDate(rs.getString("Create_Date"));
                compRequisition.setReqnID(rs.getString("complaint_id"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setRequistionTitle(rs.getString("subject"));
                compRequisition.setTranID(rs.getString("trans_id"));
                compRequisition.setSupervisor(rs.getString("supervisor"));

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentPIPQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ComplaintRequisition findProcurementReqnByReqnCodes(String filter) {

        ComplaintRequisition compRequisition = null;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,technician from HD_COMPLAINT " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setTechnician(rs.getString("technician"));

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compRequisition;

    }

    public ComplaintRequisition findProblemReqnByReqnCodes(String filter) {

        ComplaintRequisition compRequisition = null;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,services_affected,request_Section,request_Subject,request_Description,"
                + "create_time,technician from HD_PROBLEM " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setservicesAffected(rs.getString("services_affected"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setTechnician(rs.getString("technician"));

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return compRequisition;

    }


    public int updateComplaintRequisition(ComplaintRequisition compRequisition) {
        String query_r = "update HD_COMPLAINT set equipt_category=?,assetId=?,Remark=?,workStationIP=?,"
                + "origin_branch=?,origin_section=?,origin_dept=?,create_date=?,complain_category=?,complainant=?,recipient_id=?"
                + " where complaint_id = '" + compRequisition.getReqnID() + "'and id=" + compRequisition.getRequisitionNum();

        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            con = dbConnection.getConnection("legendPlus");

            ps = con.prepareStatement(query_r);
            ps.setString(1, compRequisition.getItemType());
            ps.setString(2, compRequisition.getItemRequested());
            ps.setString(3, compRequisition.getRemark());
            ps.setString(4, compRequisition.getIpAddress());
            //--ps.setInt(5, compRequisition.getQuantity());
            ps.setString(5, compRequisition.getRequestingBranch());
            ps.setString(6, compRequisition.getRequestingSection());
            ps.setString(7, compRequisition.getRequestingDept());
            ps.setTimestamp(8, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(9, compRequisition.getProjectCode());
            ps.setString(10, compRequisition.getComplaint());
            ps.setInt(11, compRequisition.getRecipientId());
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateProcurementRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        // System.out.println(" the value of updated code is >>>>>>"+ i);
        return i;
    }//updateProcurementRequisition

    public boolean insertToPrAwaitRequisition(ComplaintRequisition compRequisition) {
        // System.out.println("\n\n\n >>>>>> in insertToPrAwaitRequisition ");
        String reqnInsertQry = "insert into PR_AWAIT_REQUISITION (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment,"
                + " ReqnUserID,ItemType,ItemRequested,Status,company_code,"
                + "Remark,workStationIP,ReqnDate,ReqnTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean done = false;
        timer = new SimpleDateFormat("kk:mm:ss");
        try {
            con = dbConnection.getConnection("legendPlus");
            pstmt = con.prepareStatement(reqnInsertQry);

            pstmt.setString(1, compRequisition.getReqnID());
            pstmt.setString(2, compRequisition.getReqnUserId());
            pstmt.setString(3, compRequisition.getRequestingBranch());
            pstmt.setString(4, compRequisition.getRequestingSection());
            pstmt.setString(5, compRequisition.getRequestingDept());
            pstmt.setString(6, compRequisition.getReqnUserId());
            pstmt.setString(7, compRequisition.getItemType());
            pstmt.setString(8, compRequisition.getItemRequested());
            pstmt.setString(9, "X");
            pstmt.setString(10, "1");
            pstmt.setString(11, "");
            pstmt.setString(12, compRequisition.getIpAddress());
            //pstmt.setInt(13, compRequisition.getQuantity());
            pstmt.setTimestamp(13, dbConnection.getDateTime(new java.util.Date()));
            pstmt.setString(14, timer.format(new java.util.Date()));
            done = (pstmt.executeUpdate() == -1);

           // System.out.println("done >>>>>>>>> " + done);

        } catch (SQLException e) {
            System.out.println(">>>> erorr occured in insertToPrAwaitRequisition " + e);
            e.printStackTrace();
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
        return done;
    }//insertToPrAwaitRequisition

    public ArrayList getRecipientEmail(String complaintId) {


        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

        String emailQry = "select recipient_Id,complainant,remark "
                + "from HD_COMPLAINT where COMPLAINT_ID='" + complaintId + "' and status='ACTIVE'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(emailQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();

                compRequisition.setRemark(rs.getString("remark"));
                compRequisition.setComplaint(rs.getString("complainant"));
                String recipientEmail = aprecords.getCodeName("select email from am_gb_user where user_id =" + rs.getInt("recipient_Id"));
                compRequisition.setRecipientEmail(recipientEmail);
                _list.add(compRequisition);
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
            ComplaintRequisition compRequisition = null;
            MailSender mailsender = new MailSender();

            for (int i = 0; i < recipientList.size(); i++) {
                compRequisition = (ComplaintRequisition) recipientList.get(i);
                String recipientEmial = compRequisition.getRecipientEmail();
                String mailHead = "Service Request:" + complaintID + " from " + compRequisition.getComplaint();
                String mailBody = compRequisition.getRemark();
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
            con = dbConnection.getConnection("legendPlus");
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

    public ArrayList DeleteResponse(String complaintId, int complaintNumber) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ID,recipient_Id,returnTo_id,Action_on_self,self_reminder,self_reminder_mode, "
                + " return_time,return_time_mode,return_date,action_to_perform,response,user_id,close_activate_reason,status  "
                + "from hd_response where complaint_id='" + complaintId + "' and complaint_Number=" + complaintNumber + " order by id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
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
           // System.out.println("=====Inside sendRecipientsMail === ");
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

        String rmarkQry = "select ID,recipient_Id,response,request_Description,user_id,status,create_date,create_time "
               // + "from hd_response where complaint_id='" + complaintId + "' and complaint_Number=" + complaintNumber + " order by id";
                 + "from hd_response where complaint_id='" + complaintId + "' order by id";
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setId(rs.getInt(1));
                compResponse.setNewRecipient(rs.getInt(2));
                compResponse.setNewResponse(rs.getString(3));
                compResponse.setRequestDescription(rs.getString(4));
                compResponse.setUserId(rs.getInt(5));
                compResponse.setStatus(rs.getString(6));
                compResponse.setReturnDate(dbConnection.formatDate(rs.getDate(7)));
                compResponse.setReturnTime(rs.getString(8));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findOldComplaintResponses ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }
  
        return _list;
    }
    public ArrayList findImageattached(String complaintId) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ASSETID,pagename,sessionId from AM_AD_IMAGE where ASSETID='" + complaintId + "' order by sessionId";
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setIssueId(rs.getString(1));
                compResponse.setFileName(rs.getString(2));
                compResponse.setsessionId(rs.getString(3));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findImageattached ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }
  
        return _list;
    }

    public ArrayList findOldAttachement(String complaintId, int complaintNumber) {
        ArrayList _list = new ArrayList();
        ComplaintResponse compResponse = null;

        String rmarkQry = "select ID,sessionId,File_Name from AM_AD_IMAGE where complaint_id='" + complaintId + "' order by id";
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compResponse = new ComplaintResponse();
                compResponse.setComplaint(rs.getString(1));
                compResponse.setNewResponse(rs.getString(2));
                compResponse.setRequestDescription(rs.getString(3));
                _list.add(compResponse);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findOldComplaintResponses ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }
  
        return _list;
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
            con = dbConnection.getConnection("legendPlus");
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
 
    
    public int updateCompRequisition(ComplaintRequisition compRequisition,String Status,String priority,String complaintType, Timestamp updateDate) {
        String query_r = "update HD_COMPLAINT set complain_category=?,complain_sub_category=?,priority=?,complaint_Type=?,"
                + "incident_Mode=?,asset_id=?,request_Subject=?,request_Description=?,technician=?,status=?,Close_Date=? "
                + " where complaint_id = '" + compRequisition.getReqnID()+"'" ;
//        System.out.println("======= updateCompRequisition query_r===== "+query_r);
        Connection con = null;
        PreparedStatement ps = null;
  //      con = mgDbCon.getConnection("legendPlus");
        //System.out.println("=======mgDbCon.getDateTime(new java.util.Date())===== "+mgDbCon.getDateTime(new java.util.Date()));
//        System.out.println("=====UpdateDate====>>> "+updateDate);
        int i = 0;
        try {
            con = dbConnection.getConnection("legendPlus");
         
            ps = con.prepareStatement(query_r); 
            ps.setString(1, compRequisition.getCategory());
            ps.setString(2, compRequisition.getSubCategory());
            ps.setString(3, priority);
            ps.setString(4, complaintType);
            ps.setString(5, compRequisition.getIncidentMode());
            ps.setString(6, compRequisition.getAssetId());
            ps.setString(7, compRequisition.getRequestSubject());
            ps.setString(8, compRequisition.getRequestDescription());
            ps.setString(9, compRequisition.getTechnician());
            ps.setString(10, Status);   
            ps.setTimestamp(11, updateDate);
           // ps.setTimestamp(11, mgDbCon.getDateTime(new java.util.Date()));
           // System.out.println("=======compRequisition.getStatus()===== "+compRequisition.getStatus());
            //ps.setInt(11, compRequisition.Slaid());
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateCompRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        
        return i;
    }//updateCompRequisition
    
    public int updateDeptCompRequisition(ComplaintRequisition compRequisition) {
        String query_r = "update HD_COMPLAINT set complain_category=?,complain_sub_category=?"
                + " where complaint_id = '" + compRequisition.getReqnID()+"'" ;
       // System.out.println("Inside updateDeptCompRequisition");
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try { 
            con = dbConnection.getConnection("legendPlus");

            ps = con.prepareStatement(query_r);
            ps.setString(1, compRequisition.getCategory());
            ps.setString(2, compRequisition.getSubCategory());

            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateCompRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        
        return i;
    }//updateCompRequisition
    
    
    public int updateProblemRes(ComplaintRequisition compRequisition) {
        String query_r = "update HD_PROBLEM set complain_category=?,complain_sub_category=?,priority=?,complaint_Type=?,"
                + "incident_Mode=?,asset_id=?,request_Subject=?,request_Description=?,technician=?,status=?,services_affected=?"
                + " where complaint_id = '" + compRequisition.getReqnID()+"'" ;
   //     System.out.print("========Update of HD_PROBLEM===== "+compRequisition.getservicesAffected());
   //     System.out.print("========Update of HD_PROBLEM===== "+compRequisition.getStatus());       
        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            
        	con = dbConnection.getConnection("legendPlus");
            
            ps = con.prepareStatement(query_r);
            ps.setString(1, compRequisition.getCategory());
            ps.setString(2, compRequisition.getSubCategory());
            ps.setString(3, compRequisition.getPriority());
            ps.setString(4, compRequisition.getComplainType());
            ps.setString(5, compRequisition.getIncidentMode());
            ps.setString(6, compRequisition.getAssetId());
            ps.setString(7, compRequisition.getRequestSubject());
            ps.setString(8, compRequisition.getRequestSubject() );
            ps.setString(9, compRequisition.getTechnician());
            ps.setString(10, compRequisition.getStatus());
            ps.setString(11, compRequisition.getservicesAffected());
            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateCompRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        
        return i;
    }//updateCompRequisition
    public ArrayList findProcurmentWorkInProgressQryOverdueDate(String filterquery) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;
        //===textDate2=== 2011-10-20
        String fromDate = dateFormat.textDate2();
//        System.out.println("======fromDate====>"+fromDate);
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
//    	System.out.println("======startyy====>"+startyy);
//    	System.out.println("======startmm====>"+startmm);
//    	System.out.println("======startdd====>"+startdd);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
//    	System.out.println("======startDate====>"+startDate);
 
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,create_dateTime,due_date from HD_COMPLAINT where due_date < ? AND status = '001' " +filterquery ;

//        System.out.println("===rmarkQry=== "+rmarkQry);      
     //   System.out.println("===textDate2=== "+dateFormat.textDate2()); 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
//            ps.setString(1, startDate);  for Oracle Database
            ps.setString(1, dateFormat.textDate2());   //SQL Database
             
          //  ps.setString(2, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
//                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setCreateDate(rs.getString("create_dateTime"));                
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 3 findProcurmentWorkInProgressQryOverdueDate ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    
    public ArrayList findProcurmentWorkInProgressQryDueToday(String filterquery) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
//    	System.out.println("======startDate in findProcurmentWorkInProgressQryDueToday====>"+startDate);
   
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where due_date = ? AND status = '001' " +filterquery ;

//        System.out.println("===rmarkQry in findProcurmentWorkInProgressQryDueToday=== "+rmarkQry);      
     //   System.out.println("===textDate2=== "+dateFormat.textDate2()); 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
//          ps.setString(1, startDate);  for Oracle Database
            ps.setString(1, dateFormat.textDate2());   //SQL Database
             
          //  ps.setString(2, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 4 findProcurmentWorkInProgressQryDueToday ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
 

    public ArrayList findProcurmentWorkInProgressQrydueDate(String filterquery) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where due_date = ? "+filterquery  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, dateFormat.textDate2());
//            ps.setString(1, startDate); Oracle Database
      //      ps.setString(2, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 5 findProcurmentWorkInProgressQrydueDate ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }
 
        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryRequestProblem(String complaintCode, String UserId) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where complain_category = '"+complaintCode+"' and User_id  = '"+UserId+"' "  ;

      //  System.out.println("-----rmarkQry findProcurmentWorkInProgressQryRequestProblem ---- "+rmarkQry);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
        //    ps.setString(1, complaintCode);
           // ps.setString(2, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 6 findProcurmentWorkInProgressQryRequestProblem ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryRequestPending(String complaintStatus, String UserId) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where Status = '"+complaintStatus+"' and User_id  = '"+UserId+"' "  ;

      //  System.out.println("-----rmarkQry findProcurmentWorkInProgressQryRequestPending ---- "+rmarkQry);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
        //    ps.setString(1, complaintCode);
           // ps.setString(2, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 7 findProcurmentWorkInProgressQryRequestPending ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    
    public ArrayList findProcurmentWorkInProgressQryRequestProblem2(String complaintCode, String UserId) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;

 //        System.out.println("== UserId== "+UserId);
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,user_id,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,technician,requester_id,nature,services_affected from HD_PROBLEM where status = '001' and user_id = '"+UserId+"'"  ;
     
  //     System.out.println("== rmarkQry findProcurmentWorkInProgressQryRequestProblem2== "+rmarkQry);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
          //  ps.setString(1, complaintCode);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
               // compRequisition.setComplainType(rs.getString("complaint_Type"));
             //   compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
             //   compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
               // compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 8 findProcurmentWorkInProgressQryRequestProblem2 ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findProcurmentWorkInProgressQryincidentMode(String incidentMode) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;
//System.out.println("===incidentMode=== "+incidentMode);

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode = ? "  ;

      //  System.out.println("===rmarkQry=== "+rmarkQry);       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, incidentMode);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 9 findProcurmentWorkInProgressQryincidentMode ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryincidentMode(String incidentMode,String status) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode = ? and status=?"  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, incidentMode);
            ps.setString(2, status);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 10 findProcurmentWorkInProgressQryincidentMode ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    public ArrayList findProcurmentWorkInProgressQryincidentMode(String incidentMode,String status,String filterquery) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode = ? and status=? "+filterquery  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, incidentMode);
            ps.setString(2, status);
      //      ps.setString(3, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 11 findProcurmentWorkInProgressQryincidentMode ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    public ArrayList findProcurmentWorkInProgressQryincidentMode2(String status) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;

 
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode is null and status=? "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
      
        ResultSet rs = null;

  
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
            ps.setString(1, status); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 12 findProcurmentWorkInProgressQryincidentMode2 ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryincidentMode2(String status,String filterquery) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;

 
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode is null and status=? "+filterquery  ;

       

        Connection con = null;
        PreparedStatement ps = null;
      
        ResultSet rs = null;

  
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
            ps.setString(1, status); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 13 findProcurmentWorkInProgressQryincidentMode2 ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    
    public ArrayList findProcurmentWorkInProgressQryincidentMode() 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date,Close_Date from HD_COMPLAINT where incident_Mode is null "  ;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 14 findProcurmentWorkInProgressQryincidentMode ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    } 
    public ArrayList findProcurmentWorkInProgressQryTechnician(String filterquery)  
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT  where technician is null " +filterquery ;
      // System.out.println("===== findProcurmentWorkInProgressQryTechnicianTechnician rmarkQry===== "+rmarkQry);
       
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
          //  ps.setString(1, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {  
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQryTechnician ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }  
    public ArrayList findProcurmentWorkInProgressQryTechnician2() 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;

     //    System.out.println("== UserId== "+UserId);
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,user_id,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,technician,requester_id,nature,services_affected from HD_PROBLEM where technician ='0' " ;
     
   //     System.out.println("== rmarkQry findProcurmentWorkInProgressQryTechnician2== "+rmarkQry);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
               // compRequisition.setComplainType(rs.getString("complaint_Type"));
             //   compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
             //   compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
               // compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQryTechnician2 ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findTaskWorkInProgressQry(String filterQry) {
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findTaskWorkInProgressQry--- filterQry--- "+filterQry);
String rmarkQry = "select id,Task_id,create_date,status,user_id,"
    + " Task_Title,task_Topic,work_Station_IP,Task_Content,Task_Comments,"
    + " Schedule_Start_Date,Schedule_End_Date,Actual_Start_Date,Actual_End_Date,"
    + "Email_Me_Before,technician from HD_TASK Where Actual_End_Date > '"+startDate+"' " + filterQry;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setTask_id(rs.getString("Task_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setTask_Title(rs.getString("Task_Title"));
                compRequisition.setTask_Topic(rs.getString("Task_Topic"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setTask_Content(rs.getString("Task_Content"));           
                compRequisition.setTask_Comments(rs.getString("Task_Comments"));
                compRequisition.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
                compRequisition.setScheduleEndDate(rs.getString("Schedule_End_Date"));
                compRequisition.setActualStartDate(rs.getString("Actual_Start_Date"));
                compRequisition.setActualEndDate(rs.getString("Actual_End_Date"));
                compRequisition.setEmailMeBefore(rs.getString("Email_Me_Before")); 
                compRequisition.setTechnician(rs.getString("technician")); 

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findSolutionWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findTaskListQry(String filterQry) {
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findTaskListQry--- filterQry--- "+filterQry);
String rmarkQry = "select id,Task_id,create_date,status,user_id,"
    + " Task_Title,task_Topic,work_Station_IP,Task_Content,Task_Comments,"
    + " Schedule_Start_Date,Schedule_End_Date,Actual_Start_Date,Actual_End_Date,"
    + "Email_Me_Before,technician from HD_TASK Where Task_id is not null " + filterQry;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setTask_id(rs.getString("Task_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setTask_Title(rs.getString("Task_Title"));
                compRequisition.setTask_Topic(rs.getString("Task_Topic"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setTask_Content(rs.getString("Task_Content"));           
                compRequisition.setTask_Comments(rs.getString("Task_Comments"));
                compRequisition.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
                compRequisition.setScheduleEndDate(rs.getString("Schedule_End_Date"));
                compRequisition.setActualStartDate(rs.getString("Actual_Start_Date"));
                compRequisition.setActualEndDate(rs.getString("Actual_End_Date"));
                compRequisition.setEmailMeBefore(rs.getString("Email_Me_Before")); 
                compRequisition.setTechnician(rs.getString("technician")); 

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findTaskListQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    
    public ArrayList findTaskWorkInProgressQry1(String filterQry) {
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
  //  	System.out.println("======startDate====>"+startDate);
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findTaskWorkInProgressQry1--- filterQry--- "+filterQry);
String rmarkQry = "select id,Task_id,create_date,status,user_id,"
    + " Task_Title,task_Topic,work_Station_IP,Task_Content,Task_Comments,"
    + " Schedule_Start_Date,Schedule_End_Date,Actual_Start_Date,Actual_End_Date,"
    + "Email_Me_Before,technician from HD_TASK Where Actual_End_Date > '"+fromDate+"' " + filterQry;
//System.out.println("==== findTaskWorkInProgressQry1 rmarkQry=== "+rmarkQry);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
  
        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
          //  ps.setString(1, startDate);
           // ps.setString(1, fromDate);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setTask_id(rs.getString("Task_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setTask_Title(rs.getString("Task_Title"));
                compRequisition.setTask_Topic(rs.getString("Task_Topic"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setTask_Content(rs.getString("Task_Content"));           
                compRequisition.setTask_Comments(rs.getString("Task_Comments"));
                compRequisition.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
                compRequisition.setScheduleEndDate(rs.getString("Schedule_End_Date"));
                compRequisition.setActualStartDate(rs.getString("Actual_Start_Date"));
                compRequisition.setActualEndDate(rs.getString("Actual_End_Date"));
                compRequisition.setEmailMeBefore(rs.getString("Email_Me_Before")); 
                compRequisition.setTechnician(rs.getString("technician")); 

                _list.add(compRequisition);
            }
 
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findSolutionWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    


    public ArrayList findAnnouncementWorkInProgressQry(String filterQry) {
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findAnnouncementWorkInProgressQry--- filterQry--- "+filterQry);
String rmarkQry = "select id,Announce_id,create_date,status,user_id,"
    + " Announce_Title,work_Station_IP,Announce_Content,SEND_MAIL,Email_TO,Email_COPY,Company_Code,"
    + " Schedule_Start_Date,Schedule_End_Date,"
    + "create_time from HD_ANNOUNCEMENT Where Schedule_End_Date > '"+fromDate+"' " + filterQry;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
         //   ps.setString(1, startDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setAnnounce_id(rs.getString("Announce_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setAnnounce_Title(rs.getString("Announce_Title"));               
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setAnnounce_Content(rs.getString("Announce_Content"));           
                compRequisition.setSend_Mail(rs.getString("SEND_MAIL"));
                compRequisition.setEmail_To(rs.getString("Email_TO"));
                compRequisition.setEmail_Copy(rs.getString("Email_COPY"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
                compRequisition.setScheduleEndDate(rs.getString("Schedule_End_Date"));   
                compRequisition.setCreatTime(rs.getString("create_time"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findAnnouncementWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findAnnouncementListQry(String filterQry) {
        String fromDate = dateFormat.textDate2();
        String startyy = fromDate.substring(0,4);
    	String startmm = fromDate.substring(5,7);
    	String startdd = fromDate.substring(8,10);
    	String startDate = startdd+"-"+dateFormat.formatOracleMonth(startmm)+"-"+startyy;
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_SOLUTION " + filterQry;

//System.out.println("----IN findAnnouncementListQry--- filterQry--- "+filterQry);
String rmarkQry = "select id,Announce_id,create_date,status,user_id,"
    + " Announce_Title,work_Station_IP,Announce_Content,SEND_MAIL,Email_TO,Email_COPY,Company_Code,"
    + " Schedule_Start_Date,Schedule_End_Date,"
    + "create_time from HD_ANNOUNCEMENT Where Announce_id is not null " + filterQry;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
        //    ps.setString(1, startDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setAnnounce_id(rs.getString("Announce_id"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setAnnounce_Title(rs.getString("Announce_Title"));               
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setAnnounce_Content(rs.getString("Announce_Content"));           
                compRequisition.setSend_Mail(rs.getString("SEND_MAIL"));
                compRequisition.setEmail_To(rs.getString("Email_TO"));
                compRequisition.setEmail_Copy(rs.getString("Email_COPY"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setScheduleStarteDate(rs.getString("Schedule_Start_Date"));
                compRequisition.setScheduleEndDate(rs.getString("Schedule_End_Date"));   
                compRequisition.setCreatTime(rs.getString("create_time"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findAnnouncementListQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    
    public ArrayList findProcurmentProgressQry(String complaintCode, String UserId) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

    //     System.out.println("== UserId== "+UserId);

         String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                 + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                 + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                 + " Company_Code,request_Section,request_Subject,request_Description,"
                 + "create_time,due_date from HD_COMPLAINT  where technician is null and technician ='"+UserId+"' "  ;
 
  //      System.out.println("== rmarkQry findProcurmentProgressQry== "+rmarkQry);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    
    public ArrayList findPendingProgressQry(String Status_Code, String filterquery) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

//         System.out.println("== UserId== "+UserId);

         String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                 + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                 + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                 + " Company_Code,request_Section,request_Subject,request_Description,"
                 + "create_time,due_date,Close_Date from HD_COMPLAINT  where technician is not null and status ='"+Status_Code+"' " +filterquery ;
         		//+ "create_time,due_date from HD_COMPLAINT  where (technician is null and status ='"+Status_Code+"' and User_Id ='"+UserId+"') or (technician = '"+UserId+"' and status ='"+Status_Code+"' and User_Id ='"+UserId+"') "  ;
   
//        System.out.println("== rmarkQry findPendingProgressQry== "+rmarkQry);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                compRequisition.setCloseDate(rs.getString("Close_Date"));
                _list.add(compRequisition); 
            } 

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findPendingProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;  
    }
    public ArrayList findOpenProblem(String complaint_Code, String filterquery) 
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

     //    System.out.println("== UserId== "+UserId);

         String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                 + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                 + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                 + " Company_Code,request_Section,request_Subject,request_Description,"
                 + "create_time,due_date from HD_COMPLAINT  where technician is not null and complaint_Type ='"+complaint_Code+"' "+filterquery  ;
         		//+ "create_time,due_date from HD_COMPLAINT  where (technician is null and complaint_Type ='"+complaint_Code+"' and User_Id ='"+UserId+"') or (technician = '"+UserId+"' and complaint_Type ='"+complaint_Code+"' and User_Id ='"+UserId+"') "  ;
    //    System.out.println("== rmarkQry findOpenProblem== "+rmarkQry);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findOpenProblem ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findProcurmentPriorityQry(String Priority, String filterquery)  
    {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//System.out.println("=======Priority==== "+Priority);
        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT  where Priority ='"+Priority+"' "+filterquery  ;

 //       System.out.println("== rmarkQry findProcurmentPriorityQry== "+rmarkQry);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {  
            System.out.println(this.getClass().getName() + " Error  findProcurmentPriorityQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }  

    public ArrayList findProcurmentPriorityQry2(String filterquery)  
    { 
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT  where Priority is null "+filterquery  ;
//        System.out.println("== rmarkQry findProcurmentPriorityQry2== "+rmarkQry);
       
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry); 
          //  ps.setString(1, UserId);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {  
            System.out.println(this.getClass().getName() + " Error  findProcurmentPriorityQry2 ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }  

    public ArrayList findProcurmentTotalQry(String Status,String filterquery) 
    {  
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;
//System.out.println("===Status=== "+Status);

        String rmarkQry = "select id,complaint_id,complain_category,complain_sub_category,"
                + " asset_id,priority,create_date,status,complaint_Type,incident_Mode,user_id,response_Mode,"
                + " requester_Name,requester_Contact_No,work_Station_IP,notify_Email,request_Branch,request_Department,"
                + " Company_Code,request_Section,request_Subject,request_Description,"
                + "create_time,due_date from HD_COMPLAINT where Status = ? "+filterquery;

  //      System.out.println("===rmarkQry=== "+rmarkQry);       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            ps.setString(1, Status);
            rs = ps.executeQuery();
           
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("complaint_id"));
                compRequisition.setCategory(rs.getString("complain_category"));
                compRequisition.setSubCategory(rs.getString("complain_sub_category"));
                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("complaint_Type"));
                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("request_Subject"));
                compRequisition.setRequestDescription(rs.getString("request_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));
                compRequisition.setDueDate(rs.getString("due_date"));
                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentTotalQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }

        return _list;
    } 

    public ArrayList findChangesWorkInProgressQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ComplaintRequisition compRequisition = null;
//        String rmarkQry = "select assetId,create_date,complaint_id,status,equipt_category,id,complain_category,complainant,recipient_id,remark,sender_id from " +
//                "HD_COMPLAINT " + filterQry;


        String rmarkQry = "select id,Change_id,Category_Code,Sub_Category_Code,priority,create_date,"
                + " status,Change_Type,user_id,requester_Name,requester_Contact_No,"
                + " work_Station_IP,notify_Email,request_Branch,request_Department,Company_Code,request_Section,"
                + " services_affected,Change_Description,"
                + "create_time from HD_CHANGES " + filterQry;

       

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {  
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                compRequisition = new ComplaintRequisition();
                compRequisition.setId(rs.getInt("id"));
                compRequisition.setComplaint(rs.getString("Change_id"));
                compRequisition.setCategory(rs.getString("Category_Code"));
                compRequisition.setSubCategory(rs.getString("Sub_Category_Code"));
//                compRequisition.setAssetId(rs.getString("asset_id"));
                compRequisition.setPriority(rs.getString("priority"));
                compRequisition.setCreateDate(rs.getString("create_date"));
                compRequisition.setStatus(rs.getString("status"));
                compRequisition.setComplainType(rs.getString("Change_Type"));
//                compRequisition.setIncidentMode(rs.getString("incident_Mode"));
                compRequisition.setUserID(rs.getInt("user_id"));
//                compRequisition.setResponseMode(rs.getString("response_Mode"));
                compRequisition.setRequesterName(rs.getString("requester_Name"));
                compRequisition.setRequesterContactNo(rs.getString("requester_Contact_No"));
                compRequisition.setIpAddress(rs.getString("work_Station_IP"));
                compRequisition.setRecipientEmail(rs.getString("notify_Email"));
                compRequisition.setRequestingBranch(rs.getString("request_Branch"));
                compRequisition.setRequestingDept(rs.getString("request_Department"));
                compRequisition.setCompany_code(rs.getString("Company_Code"));
                compRequisition.setRequestingSection(rs.getString("request_Section"));
                compRequisition.setRequestSubject(rs.getString("services_affected"));
                compRequisition.setRequestDescription(rs.getString("Change_Description"));
                compRequisition.setCreatTime(rs.getString("create_time"));

                _list.add(compRequisition);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error 2 findChangesWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }
    

}
