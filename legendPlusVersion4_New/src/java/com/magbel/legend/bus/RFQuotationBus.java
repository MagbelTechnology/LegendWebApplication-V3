/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.legend.bus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.DatetimeFormat;

import magma.net.vao.RFQuotation;
import magma.net.vao.QuotationRequest;
import magma.net.vao.Order;
import magma.net.vao.ItemsPerReqId;
import magma.net.vao.RFQResponse;
import magma.net.vao.Award;
import magma.net.vao.RFQResponseDetail;


/**
 *
 * @author Kareem Wasiu Aderemi
 */
public class RFQuotationBus extends MagmaDBConnection {
	public ApprovalRecords approvalRec;
    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    private java.text.SimpleDateFormat timer;
    com.magbel.util.DatetimeFormat df;
    public RFQuotationBus() {

        try {
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            timer = new java.text.SimpleDateFormat("hh:mm:ss");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            df = new com.magbel.util.DatetimeFormat();
            approvalRec = new ApprovalRecords(); 
        } catch (Exception ex) {
        	
        }
    }
	

  public ArrayList findAllReqSummary() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_AWAIT_REQUISITION";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");
                String requisitionDate = rs.getString("REQNDATE");
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

  public ArrayList findAllRequisition(){

      ArrayList collection = new ArrayList();

 return collection;

  }

   public ArrayList findAllRFQ() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,sub_category_name AS ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_REQUISITION a, am_ad_sub_category b " +
                "where a.ItemRequested = b.sub_category_code order by ReqnID ";
  
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");
                String requisitionDate = rs.getString("REQNDATE");
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


   public ArrayList findAllRFQAfterFinalApproval(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT DISTINCT ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,sub_category_name AS ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_REQUISITION a, am_ad_sub_category b " +
                "where a.ItemRequested = b.sub_category_code "+filter+" order by ReqnID ";
//        System.out.println("=====FINDER_QUERY: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");
                String requisitionDate = rs.getString("REQNDATE");
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }
   
   public ArrayList findAllRFQ(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_REQUISITION "+filter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");
                String requisitionDate = rs.getString("REQNDATE");
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

   public ArrayList findAllRFQforList() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT DISTINCT  REQNID,USERID,REQNBRANCH,REQNUSERID,STATUS,COMPANY_CODE FROM PR_REQUISITION";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String ReqnUserId = rs.getString("REQNUSERID");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");

                rfq = new RFQuotation(requestingBranch, userId, ReqnID, company_code, status,ReqnUserId);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


   public ArrayList findAllRFQforListAfterFinalAproval(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT DISTINCT  ID,REQNID,USERID,REQNBRANCH,REQNUSERID,ITEMREQUESTED,ITEMTYPE,STATUS,COMPANY_CODE FROM PR_REQUISITION "+filter;
        System.out.println("=======FINDER_QUERY: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {
            	String Id = rs.getString("ID");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String ReqnUserId = rs.getString("REQNUSERID");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                        
                rfq = new RFQuotation(requestingBranch,itemType,itemRequested,ReqnID,userId,status,company_code,ReqnUserId);
                rfq.setId(Id);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION] in findAllRFQforListAfterFinalAproval->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


 public ArrayList findItemsPerReqId(String reqId,String vendorCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ItemsPerReqId item = null;
        MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY =   "select distinct a.delivery_date,b.id,b.REQNID,b.itemrequested,b.quantity from PR_REQUEST_FOR_QUOTATION a," +
             "PR_REQUISITION b where a.reqnId = b.reqnId and  b.reqnId = ? and a.vendor_code = ? ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqId);
            ps.setString(2, vendorCode);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
           String delDate =  mag.formatDate(rs.getDate("delivery_date"));
                String desc = rs.getString("itemrequested");
                String codedesc = "SELECT sub_category_name FROM am_ad_sub_category WHERE sub_category_code = '"+desc+"' ";
                desc = approvalRec.getCodeName(codedesc);
                System.out.println("=====codedesc: "+codedesc+"      desc: "+desc);
                String qty = rs.getString("quantity");
                double uPrice = 0.0;



                item = new ItemsPerReqId( id, desc, reqId, delDate,qty,uPrice);
                collection.add(item);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findItemsPerReqId]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


      public RFQuotation findRFQReqId(String reqId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        MagmaDBConnection mag = new MagmaDBConnection();


       // ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_REQUISITION WHERE REQNID = ? ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqId);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");

                String requisitionDate = mag.formatDate(rs.getDate("REQNDATE"));
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");


                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                ///collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch findRFQReqId->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }

   public RFQuotation findFQById(String id1) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        //ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  ID,REQNID,USERID,REQNBRANCH,REQNSECTION,REQNDEPARTMENT,REQNDATE,REQNUSERID," +
                "ITEMTYPE,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY " +
                "FROM PR_AWAIT_REQUISITION WHERE REQNID = ? ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String requestingBranch = rs.getString("REQNBRANCH");
                String requestingSection = rs.getString("REQNSECTION");
                String requestingDept = rs.getString("REQNDEPARTMENT");
                String requisitionDate = rs.getString("REQNDATE");
                String ReqnUserId = rs.getString("REQNUSERID");
                String itemType = rs.getString("ITEMTYPE");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id, itemType, itemRequested, requestingBranch, no_Of_Items,
                             requestingDept, remark, userId, ReqnID, requestingSection,
                             company_code, status,ReqnUserId, requisitionDate);
                //collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_REQUISITION]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }

   public ArrayList findRFQSentByFilter(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        QuotationRequest rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT  Company_Code,UserId,RFQ_NO,ReqnID,Vendor_Code,Issue_Date,Reference_No,Instruction," +
                "Instruction1,Instruction2,Return_QuoteTo,Return_QuoteTo1,Return_QuoteTo2,Award_Status," +
                "Address,Currency,Delivery_Date,Is_Printed,Contact_person,Contact_phone,Contact_email " +
                "FROM PR_REQUEST_FOR_QUOTATION  "+ filter;
        System.out.println("====FINDER_QUERY in findRFQSentByFilter: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

                String compCode = rs.getString("Company_Code");
                String rfqNo = rs.getString("RFQ_NO");
                String reqId = rs.getString("REQNID");
                String vendorCode = rs.getString("Vendor_Code");
                String issueDate = rs.getString("Issue_Date");
                String refNo = rs.getString("Reference_No");
                String instruct = rs.getString("Instruction");
                String instruct1 = rs.getString("Instruction1");
                String instruct2 = rs.getString("Instruction2");
                String returnQuoteTo = rs.getString("Return_QuoteTo");
                String returnQuoteTo1 = rs.getString("Return_QuoteTo1");
                String returnQuoteTo2 = rs.getString("Return_QuoteTo2");
                String Address = rs.getString("Address");
                String Currency = rs.getString("Currency");
                String Delivery_Date = rs.getString("Delivery_Date");
                String Is_Printed = rs.getString("Is_Printed");
                String awardStatus = rs.getString("Award_Status");
                 String Contact_person = rs.getString("Contact_person");
                  String ContactPhone = rs.getString("Contact_phone");
                   String Contact_email = rs.getString("Contact_email");


                rfq = new QuotationRequest(compCode,rfqNo,reqId,vendorCode,issueDate,refNo, instruct,
                        instruct1,instruct2,returnQuoteTo,returnQuoteTo1,returnQuoteTo2,
                        awardStatus,Address,Currency,Delivery_Date,Is_Printed,Contact_person,ContactPhone,Contact_email);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findRFQSentByFilter]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


   public QuotationRequest findRFQByReqId(String reqnId,String vendCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        QuotationRequest rfq = null;

        MagmaDBConnection mag = new MagmaDBConnection();



        String FINDER_QUERY = "SELECT  Company_Code,UserId,RFQ_NO,ReqnID,Vendor_Code,Issue_Date,Reference_No,Instruction," +
                "Instruction1,Instruction2,Return_QuoteTo,Return_QuoteTo1,Return_QuoteTo2,Award_Status," +
                "Address,Currency,Delivery_Date,Is_Printed,Contact_person,Contact_phone,Contact_email " +
                "FROM PR_REQUEST_FOR_QUOTATION WHERE REQNID = ? AND VENDOR_CODE = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
              ps.setString(1, reqnId);
              ps.setString(2, vendCode);
            rs = ps.executeQuery();

            while (rs.next()) {

                String compCode = rs.getString("Company_Code");
                String rfqNo = rs.getString("RFQ_NO");
                String reqId = rs.getString("REQNID");
                String vendorCode = rs.getString("Vendor_Code");
                String issueDate = mag.formatDate(rs.getDate("Issue_Date"));
                //String issueDate = rs.getString("Issue_Date");
                String refNo = rs.getString("Reference_No");
                String instruct = rs.getString("Instruction");
                String instruct1 = rs.getString("Instruction1");
                String instruct2 = rs.getString("Instruction2");
                String returnQuoteTo = rs.getString("Return_QuoteTo");
                String returnQuoteTo1 = rs.getString("Return_QuoteTo1");
                String returnQuoteTo2 = rs.getString("Return_QuoteTo2");
                String Address = rs.getString("Address");
                String Currency = rs.getString("Currency");
                String Delivery_Date = mag.formatDate(rs.getDate("Delivery_Date"));
                 String Is_Printed = rs.getString("Is_Printed");
                String awardStatus = rs.getString("Award_Status");
                 String Contact_person = rs.getString("Contact_person");
                  String ContactPhone = rs.getString("Contact_phone");
                   String Contact_email = rs.getString("Contact_email");


                rfq = new QuotationRequest(compCode,rfqNo,reqId,vendorCode,issueDate,refNo, instruct,
                        instruct1,instruct2,returnQuoteTo,returnQuoteTo1,returnQuoteTo2,
                        awardStatus,Address,Currency,Delivery_Date,Is_Printed,Contact_person,ContactPhone,Contact_email);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findRFQByReqId]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }

  // public

 public ArrayList getVendorsAwaitingRFQ(String reqnID,String status1,String tableId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ID,REQNID,USERID,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY,VENDOR_CODE FROM PR_AWAIT_RQUESTFORQUOTE WHERE REQNID = ? AND STATUS = ? AND PR_TABLE_ID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnID);
            ps.setString(2, status1);
            ps.setString(3, tableId);

            rs = ps.executeQuery();


            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String vendorCode = rs.getString("Vendor_Code");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id,itemRequested,no_Of_Items,remark,
                userId,ReqnID,company_code,status,vendorCode);
                collection.add(rfq);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_RQUESTFORQUOTE]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }
 public ArrayList getVendorsResponseRFQ(String reqnID,String status1) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ID,REQNID,USERID,ITEMREQUESTED,STATUS,COMPANY_CODE,REMARK,QUANTITY,VENDOR_CODE FROM PR_AWAIT_RQUESTFORQUOTE WHERE REQNID = ? AND STATUS = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnID);
            ps.setString(2, status1);

            rs = ps.executeQuery();


            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String vendorCode = rs.getString("Vendor_Code");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id,itemRequested,no_Of_Items,remark,
                userId,ReqnID,company_code,status,vendorCode);
                collection.add(rfq);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_RQUESTFORQUOTE]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


 public ArrayList getResponseToRFQ(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQResponseDetail rfq = null;
        MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT COMPANY_CODE,REQNID,Vendor_Code,Delivery_Date,Description,Quantity,Unit_Price," +
                "Item_No,Cost_Price,Awarded FROM PR_RFQ_RESPONSE_DETAIL " + filter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {

                String compCode = rs.getString("COMPANY_CODE");
                String reqnID = rs.getString("REQNID");
                String vendorCode = rs.getString("Vendor_Code");

                String delDate = mag.formatDate(rs.getDate("Delivery_Date"));
                String desc = rs.getString("Description");
                String qty = rs.getString("Quantity");
                String unitPrice = rs.getString("Unit_Price");
                String itemNo = rs.getString("Item_No");
                String costPrice = rs.getString("Cost_Price");
                String award = rs.getString("Awarded");


                rfq = new RFQResponseDetail( compCode,reqnID,vendorCode,delDate,
                desc,qty,unitPrice,itemNo,costPrice,award);
                collection.add(rfq);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_RFQ_RESPONSE_DETAIL]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

public RFQResponseDetail getResponseDetail(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQResponseDetail rfq = null;
        MagmaDBConnection mag = new MagmaDBConnection();

        String FINDER_QUERY = "SELECT COMPANY_CODE,REQNID,Vendor_Code,Delivery_Date,Description,Quantity,Unit_Price," +
                "Item_No,Cost_Price,Awarded FROM PR_RFQ_RESPONSE_DETAIL " + filter;
// System.out.print("===query===="+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {

                String compCode = rs.getString("COMPANY_CODE");
                String reqnID = rs.getString("REQNID");
                String vendorCode = rs.getString("Vendor_Code");

                String delDate = mag.formatDate(rs.getDate("Delivery_Date"));
                String desc = rs.getString("Description");
                String qty = rs.getString("Quantity");
                String unitPrice = rs.getString("Unit_Price");
                String itemNo = rs.getString("Item_No");
                String costPrice = rs.getString("Cost_Price");
                String award = rs.getString("Awarded");


                rfq = new RFQResponseDetail( compCode,reqnID,vendorCode,delDate,
                desc,qty,unitPrice,itemNo,costPrice,award);


            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [getResponseDetail]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }

 public QuotationRequest getVendorByReqIdForQoute(String reqId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        QuotationRequest rfq = null;

        MagmaDBConnection mag = new MagmaDBConnection();



        String FINDER_QUERY = "SELECT  Company_Code,UserId,RFQ_NO,ReqnID,Vendor_Code,Issue_Date,Reference_No,Instruction," +
                "Instruction1,Instruction2,Return_QuoteTo,Return_QuoteTo1,Return_QuoteTo2,Status," +
                "Address,Currency,Delivery_Date,Is_Printed,Contact_person,Contact_phone,Contact_email " +
                "FROM PR_REQUEST_FOR_QUOTATION WHERE REQNID = ? AND VENDOR_CODE = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
              ps.setString(1, reqId);

            rs = ps.executeQuery();

            while (rs.next()) {

                String compCode = rs.getString("Company_Code");
                String rfqNo = rs.getString("RFQ_NO");
                String reqnId = rs.getString("REQNID");
                String vendorCode = rs.getString("Vendor_Code");
                String issueDate = mag.formatDate(rs.getDate("Issue_Date"));
                //String issueDate = rs.getString("Issue_Date");
                String refNo = rs.getString("Reference_No");
                String instruct = rs.getString("Instruction");
                String instruct1 = rs.getString("Instruction1");
                String instruct2 = rs.getString("Instruction2");
                String returnQuoteTo = rs.getString("Return_QuoteTo");
                String returnQuoteTo1 = rs.getString("Return_QuoteTo1");
                String returnQuoteTo2 = rs.getString("Return_QuoteTo2");
                String Address = rs.getString("Address");
                String Currency = rs.getString("Currency");
                String Delivery_Date = mag.formatDate(rs.getDate("Delivery_Date"));
                 String Is_Printed = rs.getString("Is_Printed");
                String awardStatus = rs.getString("Status");
                 String Contact_person = rs.getString("Contact_person");
                  String ContactPhone = rs.getString("Contact_phone");
                   String Contact_email = rs.getString("Contact_email");


                rfq = new QuotationRequest(compCode,rfqNo,reqId,vendorCode,issueDate,refNo, instruct,
                        instruct1,instruct2,returnQuoteTo,returnQuoteTo1,returnQuoteTo2,
                        awardStatus,Address,Currency,Delivery_Date,Is_Printed,Contact_person,ContactPhone,Contact_email);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [getVendorByReqIdForQoute]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }


          public ArrayList getResponseToRFQByVendor(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQResponse rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ReqnID,UserID,Status,Company_Code,Vendor_Code,Quoted_Vendor_code," +
                "QuotedBy,Job_Title,Email,DaysQuote_Valid,Remark,Replied_Date,Replied_By  FROM PR_RFQ_RESPONSE " + filter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {


                String ReqnID = rs.getString("REQNID");
                String UserID = rs.getString("UserID");
                String Status = rs.getString("Status");
                String Company_Code = rs.getString("Company_Code");
                String Vendor_Code = rs.getString("Vendor_Code");
                String Quoted_Vendor_code = rs.getString("Quoted_Vendor_code");
                String QuotedBy = rs.getString("QuotedBy");
                String Job_Title = rs.getString("Job_Title");
                String Email = rs.getString("Email");
                 String DaysQuote_Valid = rs.getString("DaysQuote_Valid");
                String Remark = rs.getString("Remark");
                String Replied_Date = rs.getString("Replied_Date");
                String Replied_By = rs.getString("Replied_By");


                rfq = new RFQResponse(ReqnID,UserID,Status,Company_Code,Vendor_Code,Quoted_Vendor_code,QuotedBy,
Job_Title,Email,DaysQuote_Valid,Remark,Replied_Date,Replied_By);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_RFQ_RESPONSE]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }


public RFQResponse getRFQResponseInfo(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQResponse rfq = null;


          String FINDER_QUERY = "SELECT ReqnID,UserID,Status,Company_Code,Vendor_Code,Quoted_Vendor_code," +
                "QuotedBy,Job_Title,Email,DaysQuote_Valid,Remark,Replied_Date,Replied_By," +
                "Calc_Amount,TOTAL_COST,DISCOUNT_AMOUNT,DISCOUNT_TYPE  FROM PR_RFQ_RESPONSE " + filter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {


                String ReqnID = rs.getString("REQNID");
                String UserID = rs.getString("UserID");
                String Status = rs.getString("Status");
                String Company_Code = rs.getString("Company_Code");
                String Vendor_Code = rs.getString("Vendor_Code");
                String Quoted_Vendor_code = rs.getString("Quoted_Vendor_code");
                String QuotedBy = rs.getString("QuotedBy");
                String Job_Title = rs.getString("Job_Title");
                String Email = rs.getString("Email");
                 String DaysQuote_Valid = rs.getString("DaysQuote_Valid");
                String Remark = rs.getString("Remark");
                String Replied_Date = rs.getString("Replied_Date");
                String Replied_By = rs.getString("Replied_By");

                 double Calc_Amount = rs.getDouble("Calc_Amount");
                 Calc_Amount = Calc_Amount==0.0 ? 0.00:Calc_Amount;
                 double Total_Cost = rs.getDouble("TOTAL_COST");
                 Total_Cost = Total_Cost==0.0 ?0.00:Total_Cost;
                 double Discount_Amount = rs.getDouble("DISCOUNT_AMOUNT");
                 Discount_Amount = Discount_Amount==0.0 ?0:Discount_Amount;
                 String DiscountType = rs.getString("DISCOUNT_TYPE");
                 DiscountType = DiscountType==null ?"":DiscountType;

                rfq = new RFQResponse(ReqnID,UserID,Status,Company_Code,Vendor_Code,Quoted_Vendor_code,QuotedBy,
Job_Title,Email,DaysQuote_Valid,Remark,Replied_Date,Replied_By,Calc_Amount,Total_Cost,Discount_Amount,DiscountType);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [getRFQResponseInfo]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return rfq;

    }



public ArrayList getVendorByFilter(String status1,String acquVendor,String serviceType) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;


        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ID,REQNID,USERID,ITEMREQUESTED,STATUS,COMPANY_CODE," +
                "REMARK,QUANTITY,VENDOR_CODE FROM PR_AWAIT_RQUESTFORQUOTE WHERE REQNID = ? AND STATUS = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, status1);
            ps.setString(2, acquVendor);
            ps.setString(3, serviceType);

            rs = ps.executeQuery();


            while (rs.next()) {

                String id = rs.getString("Id");
                String ReqnID = rs.getString("REQNID");
                String userId = rs.getString("USERID");
                String vendorCode = rs.getString("Vendor_Code");
                String itemRequested = rs.getString("ITEMREQUESTED");
                String status = rs.getString("STATUS");
                String company_code = rs.getString("COMPANY_CODE");
                String remark = rs.getString("REMARK");
                String no_Of_Items = rs.getString("QUANTITY");

                rfq = new RFQuotation( id,itemRequested,no_Of_Items,remark,
                userId,ReqnID,company_code,status,vendorCode);
                collection.add(rfq);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [PR_AWAIT_RQUESTFORQUOTE]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }

public ArrayList getVendorAwardByFilter(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Award award = null;
 MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ORDER_NUMBER,tran_Id,Vendor_Code,Contract_Title," +
                "AMOUNT_BALANCE,Contract_value,Contract_duration" +
      ",Payment_Type_code,Signature_code1,Signature_code2,prepared_by,prepare_date,prepare_time" +
      ",DUE_DATE,status,supervisor,workStationIP,ID,DELIVERY_COMPLETION,DELIVERY_DATE,QUANTITY_BALANCE" +
      ",ReqnID,Subject_To_Vat,Vat_amount,Total_Amount,Issue_Date FROM PR_ORDER "+ filter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {

                String ordNo = rs.getString("ORDER_NUMBER");
                String tranId = rs.getString("tran_Id");
                String vendorCode = rs.getString("Vendor_Code");
                String contTitle = rs.getString("Contract_Title");
                double amountBal = rs.getDouble("AMOUNT_BALANCE");
                String contValue = rs.getString("Contract_value");
                String contDuration = rs.getString("Contract_duration");
                String payTypeCode = rs.getString("Payment_Type_code");
                String signCode1 = rs.getString("Signature_code1");
                String signCode2 = rs.getString("Signature_code2");
                String prepBy = rs.getString("prepared_by");
                String prepDate = mag.formatDate(rs.getDate("prepare_date"));
                String prepTime = rs.getString("prepare_time");
                String status = rs.getString("status");
                 String dueDate = mag.formatDate(rs.getDate("DUE_DATE"));

                String supervisor = rs.getString("supervisor");
                String stationIp = rs.getString("workStationIP");
                String id = rs.getString("ID");
                String delivCompl = rs.getString("DELIVERY_COMPLETION");
                String delivDate = mag.formatDate(rs.getDate("DELIVERY_DATE"));
                String qtyBal = rs.getString("QUANTITY_BALANCE");
                String reqnId = rs.getString("ReqnID");
                String subjToVat = rs.getString("Subject_To_Vat");
                String vatAmount = rs.getString("Vat_amount");
                String totalAmount = rs.getString("Total_Amount");
                String issueDate = mag.formatDate(rs.getDate("Issue_Date"));



                award = new Award(ordNo,tranId,vendorCode,contTitle,amountBal,contValue,
	 contDuration, payTypeCode, signCode1,signCode2,prepBy,prepDate,
	 prepTime,dueDate,status,supervisor,stationIp, id,delivCompl,delivDate,
	qtyBal,reqnId, subjToVat, vatAmount,issueDate,issueDate);
                collection.add(award);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [getVendorAwardByFilter]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

    }



 public String getVendorName(String vendorCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String name = "legendPlus";

        String FINDER_QUERY = "SELECT VENDOR_NAME from AM_AD_VENDOR WHERE VENDOR_CODE =?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            rs = ps.executeQuery();

            while (rs.next()) {

                name = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch from AM_AD_VENDOR->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return name;

    }

public String getItemDesc(String itemCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String name = "";

        String FINDER_QUERY = "SELECT ITEMNAME from am_ad_categoryItems WHERE ITEMCODE =?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, itemCode);
            rs = ps.executeQuery();

            while (rs.next()) {

                name = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch from getItemDesc->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return name;

    }




  public void updateStatus(String vendorCode,String status,String reqId){
        Connection con = null;
        PreparedStatement ps = null;

        String deleteQuery="UPDATE PR_AWAIT_RQUESTFORQUOTE SET QUOTE_STATUS = ? WHERE VENDOR_CODE = ? AND REQNID=? ";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(deleteQuery);
           ps.setString(1, status);
           ps.setString(2, vendorCode);
           ps.setString(3, reqId);


          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error updateStatus VendorFromRFQ  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }

   public void updateAwardStatusByFilter(String filter,String status){
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;

        String upQuery="UPDATE PR_RFQ_RESPONSE_DETAIL SET AWARDED = ?  "+filter;

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(upQuery);
           pst = con.prepareStatement(upQuery);
           ps.setString(1, status);

          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error  updateAwardStatusByFilter  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }

  public void removeCanceledOrder(String vendorCode,String reqId){


        Connection con = null;
        PreparedStatement ps = null;

        String Del_QUERY = "Delete from PR_RFQ_RESPONSE_DETAIL WHERE VENDOR_CODE =? and "
                + "reqnid = ?";
        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(Del_QUERY);
           ps.setString(1, vendorCode);
           ps.setString(2, reqId);

          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error  removeCanceledOrder  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }



 public void  moveCanceledOrder(String vendorCode,String reqId,String canceledReason) {

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String name = "";
        String status = "RQL_01";

        boolean ok = false;

      String FINDER_QUERY = "SELECT distinct reqnid from PR_RFQ_RESPONSE_DETAIL "
              + "WHERE VENDOR_CODE =? and "
                + "reqnid = ?";

      String updQuery = "UPDATE  PR_REQUEST_FOR_QUOTATION SET STATUS="+"'"+status+"'"+" "
        + "WHERE REQNID ="+"'"+reqId+"'"+" and vendor_Code="+"'"+vendorCode+"'";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            ps.setString(2, reqId);

            rs = ps.executeQuery();


            String queryMove = this.getDataMigrationQuery(canceledReason);
 //System.out.println("queryMove outside next()::"+queryMove);

 while (rs.next()) {
 //         System.out.println("queryMove inside next()::"+queryMove);

     pst = con.prepareStatement(queryMove);

     pst.setString(1, vendorCode);
            pst.setString(2, reqId);
              pst.execute();
              //name = rs.getString(1);
             this.updateQuery(updQuery);
            //updateQuery();

            this.removeCanceledOrder(vendorCode,reqId);

 }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot do moveCanceledOrder::->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
            closeConnection(con, pst, rs);
        }



    }

    private String getDataMigrationQuery(String canceledReason) {

         SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
         String date = sdf.format(new java.util.Date());
        String status = "RQL_09";

        return  "  insert into PR_CANCEL_ORDER(reqnid,Item_No,vendor_code," +
                "delivery_date,description,quantity,unit_price,cost_price,Awarded,Canceled_Date,Reason_For_Canceled"+
                ")     " +

                " SELECT reqnid,Item_No,vendor_code," +
                "delivery_date,description,quantity,unit_price,cost_price, "
                +"'"+status+"'"+" AS  Awarded,  "+date+" As Canceled_Date, "+ "'"+canceledReason+"'"+ "  As Reason_For_Canceled"
                + "   FROM PR_RFQ_RESPONSE_DETAIL    " +
                "WHERE vendor_code  = ? AND  reqnid = ? " ;

    }
//"+"'"+vendorCode+"'"

public boolean isQualify(String vendorCode,String qStatus,String reqnId) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT QUOTE_STATUS FROM PR_AWAIT_RQUESTFORQUOTE WHERE QUOTE_STATUS = ? AND VENDOR_CODE=? AND REQNID = ?";
//        System.out.println("<<<<<<FINDER_QUERY in isQualify: "+FINDER_QUERY+"    qStatus: "+qStatus+"    vendorCode: "+vendorCode);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, qStatus);
             ps.setString(2, vendorCode);
             ps.setString(3, reqnId);
            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isQualify->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
    }

public boolean isQouted(String reqnId,String vendorCode,String id){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;



        String FINDER_QUERY = "SELECT REQNID FROM PR_RFQ_RESPONSE_DETAIL WHERE REQNID = ? " +
                "AND VENDOR_CODE=? AND ITEM_NO = ?";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnId);
            ps.setString(2, vendorCode);
            ps.setString(3, id);

            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNewQoute->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
}

public boolean isAwarded(String vendorCode,String status){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;



        String FINDER_QUERY = "SELECT AWARDED FROM PR_RFQ_RESPONSE_DETAIL WHERE  " +
                " VENDOR_CODE=? AND AWARDED != ?";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            ps.setString(1, vendorCode);
            ps.setString(2, status);

            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isAwarded->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
}

public boolean isTrue(String vendorCode,String reqId, String status,String tableName,String columnName){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;



        String FINDER_QUERY = "SELECT  "+ columnName+" FROM "+tableName +" WHERE  " +
                " VENDOR_CODE=? AND REQNID =?  AND "+columnName+" = ?";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            ps.setString(1, vendorCode);
            ps.setString(2, reqId);
            ps.setString(3, status);

            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isTrue->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
}

public void updateQuery(String queryString){

        Connection con = null;
        PreparedStatement ps = null;


        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(queryString);

          ps.execute();




        } catch (Exception ex) {
            System.out.println("WARNING: cannot get updateQuery->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }


}


public boolean isQouted(String reqnId,String vendorCode){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;



        String FINDER_QUERY = "SELECT REQNID FROM PR_RFQ_RESPONSE_DETAIL WHERE REQNID = ? " +
                "AND VENDOR_CODE=?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnId);
            ps.setString(2, vendorCode);


            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNewQoute->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
}

public boolean isDeclined(String reqnId,String vendorCode){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;
        String status="DECLINED";



        String FINDER_QUERY = "SELECT REQNID FROM PR_RFQ_RESPONSE WHERE REQNID = ? " +
                "AND VENDOR_CODE=? AND STATUS=?";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnId);
            ps.setString(2, vendorCode);
            ps.setString(3, status);



            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isDeclined->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
}


public boolean existRFQ(String vendorCode,String reqnID) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT REQNID FROM PR_REQUEST_FOR_QUOTATION WHERE REQNID = ? AND VENDOR_CODE=?";
//System.out.println("=====FINDER_QUERY in existRFQ: "+FINDER_QUERY+"    vendorCode: "+vendorCode+"      reqnID: "+reqnID);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnID);
             ps.setString(2, vendorCode);
            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get existRFQ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
    }




         public void updateRFQ( String issueDate,String deliveryDate,String address1,
                                String currency,String instruction1,String contact,String phone
                                ,String email,String instruction2,String instruction3,
                                String address2,String address3,String reqId){
        Connection con = null;
        PreparedStatement ps = null;
        MagmaDBConnection dbFormat = new MagmaDBConnection();


        String updateQuery="UPDATE PR_REQUEST_FOR_QUOTATION SET  "+
                "Issue_Date = ?,Delivery_Date= ?,Address= ?,Currency= ?, "+
                "Instruction= ?,Address1= ?,Address2= ?,Instruction1= ?," +
                "Instruction2= ?,Contact_person= ?,Contact_email= ?,Contact_phone = ? WHERE REQNID = ? ";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(updateQuery);
           //ps.setString(1, issueDate);
            ps.setDate(1, dbFormat.dateConvert(issueDate));
           ps.setDate(2, dbFormat.dateConvert(deliveryDate));
           //ps.setString(2, deliveryDate);
           ps.setString(3, address1);
           ps.setString(4, currency);
           ps.setString(5, instruction1);
           ps.setString(6, address2);
           ps.setString(7, address3);
           ps.setString(8, instruction2);
           ps.setString(9, instruction3);
           ps.setString(10, contact);
           ps.setString(11, email);
           ps.setString(12, phone);
           ps.setString(13, reqId);



          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error  updateRFQ  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }



 public void addVendor(String vendorCode,String reqnID,String Id){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String name = "";
        String userId  = "";
        String status = "NO REPLY";
        String vCode = "";
        String servType = "";


	String addQuery="insert into PR_AWAIT_RQUESTFORQUOTE(REQNID,USERID,STATUS,VENDOR_CODE,SERVICE_TYPE,PR_TABLE_ID)" +
			" values (?,?,?,?,?,? )";
        String addValues = "SELECT USER_ID,SERVICE_TYPE,VENDOR_CODE FROM AM_AD_VENDOR WHERE VENDOR_CODE =?";
// diff satatus
        try {
            con = getConnection("legendPlus");

              pst = con.prepareStatement(addValues);

            pst.setString(1, vendorCode);
           rs = pst.executeQuery();

            while (rs.next()) {

                userId = rs.getString("USER_ID");
                servType= rs.getString("SERVICE_TYPE");
                vCode = rs.getString("VENDOR_CODE");


              //  System.out.println("reqnID,userId,status and vCode values fetched are "+reqnID+userId+status+vCode);


            }

           ps = con.prepareStatement(addQuery);
           ps.setString(1, reqnID);
           ps.setString(2, userId);
           ps.setString(3, status);
           ps.setString(4, vCode);
           ps.setString(5, servType);
           ps.setString(6, Id);
  //          System.out.println("reqnID,userId,status and vCode values inserted are "+reqnID+userId+status+vCode);
          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error adding Vendor1111... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);
      closeConnection(con,pst);
          }

 }

 public void addQoutationRequest(String vendorCode,String reqID,String rfqNo,
                                String issueDate,String deliveryDate,String address1,
                                String currency,String instruction1,String print,String contact,String phone
                                ,String email,String instruction2,String instruction3,
                                String address2,String address3,String desc,String status){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        MagmaDBConnection dbFormat = new MagmaDBConnection();

        String name = "legendPlus";
	String addQuery="INSERT INTO PR_REQUEST_FOR_QUOTATION(VENDOR_CODE,ReqnID,RFQ_NO," +
                "Issue_Date,Delivery_Date,Address,Currency,Reference_No," +
                "Instruction,Is_Printed,Address1,Address2,Instruction1," +
                "Instruction2,Contact_person,Contact_email,Contact_phone,Status)" +
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

System.out.println("==addQuery in addQoutationRequest: "+addQuery);

	    try {
            con = getConnection("legendPlus");
              ps = con.prepareStatement(addQuery);

              ps.setString(1, vendorCode);
              ps.setString(2, reqID);
              ps.setString(3, rfqNo);
              //ps.setString(4, issueDate);
              ps.setDate(4, dbFormat.dateConvert(issueDate));
              //ps.setTimestamp(parameterIndex, null)
               ps.setDate(5, dbFormat.dateConvert(deliveryDate));
              //ps.setString(5, deliveryDate);
              ps.setString(6, address1);
              ps.setString(7, currency);
              ps.setString(8, rfqNo);
              ps.setString(9, instruction1);
              ps.setString(10, print);
              ps.setString(11, address2);
              ps.setString(12, address3);
              ps.setString(13, instruction2);
              ps.setString(14, instruction3);
              ps.setString(15, contact);
              ps.setString(16, email);
              ps.setString(17, phone);
              ps.setString(18, status);

              ps.execute();


          } catch(Exception er)
		  {
              System.out.println("Error adding addQoutationRequest ... ->"+er.getMessage());

		  }
		  finally
		  {
              closeConnection(con,ps);
              closeConnection(con,ps2);
		  }

 }

 public void addQoutationRequest(String vendorCode,String reqID,String rfqNo,
                                String issueDate,String deliveryDate,String address1,
                                String currency,String instruction1,String print,String contact,String phone
                                ,String email,String instruction2,String instruction3,
                                String address2,String address3,String desc,String status,String userId){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        MagmaDBConnection dbFormat = new MagmaDBConnection();
//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userId);
        String name = "";
	String addQuery="INSERT INTO PR_REQUEST_FOR_QUOTATION(VENDOR_CODE,ReqnID,RFQ_NO," +
                "Issue_Date,Delivery_Date,Address,Currency,Reference_No," +
                "Instruction,Is_Printed,Address1,Address2,Instruction1," +
                "Instruction2,Contact_person,Contact_email,Contact_phone,Status,userId)" +
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//	System.out.println(">>>>>>>>>>>>>addQuery in addQoutationRequest: "+addQuery);

	    try {
            con = getConnection("legendPlus");
              ps = con.prepareStatement(addQuery);

              ps.setString(1, vendorCode);
              ps.setString(2, reqID);
              ps.setString(3, rfqNo);
              //ps.setString(4, issueDate);
              ps.setDate(4, dbFormat.dateConvert(issueDate));
              //ps.setTimestamp(parameterIndex, null)
               ps.setDate(5, dbFormat.dateConvert(deliveryDate));
              //ps.setString(5, deliveryDate);
              ps.setString(6, address1);
              ps.setString(7, currency);
              ps.setString(8, rfqNo);
              ps.setString(9, instruction1);
              ps.setString(10, print);
              ps.setString(11, address2);
              ps.setString(12, address3);
              ps.setString(13, instruction2);
              ps.setString(14, instruction3);
              ps.setString(15, contact);
              ps.setString(16, email);
              ps.setString(17, phone);
              ps.setString(18, status);
              ps.setString(19, userId);





              ps.execute();


          } catch(Exception er)
		  {
              System.out.println("Error adding addQoutationRequest ... ->"+er.getMessage());

		  }
		  finally
		  {
              closeConnection(con,ps);
              closeConnection(con,ps2);
		  }

 }



 public void prepareRFQForVendor(String reqID,String rfqNo,
                                String issueDate,String deliveryDate,String address1,
                                String currency,String instruction1,String print,String contact,String phone
                                ,String email,String instruction2,String instruction3,
                                String address2,String address3,String desc,String stat){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String name = "";
        String userId  = "";
        String vCode = "";
        String servType = "";
        String status = "RQL_08";
        

//        System.out.println("=======Status in prepareRFQForVendor====: "+stat);

        String addValues = "SELECT VENDOR_CODE FROM PR_AWAIT_RQUESTFORQUOTE WHERE ReqnID =? AND Quote_status=?";
// diff satatus
        try {
            con = getConnection("legendPlus");

              pst = con.prepareStatement(addValues);

            pst.setString(1, reqID);
             pst.setString(2, stat);
           rs = pst.executeQuery();

            while (rs.next()) {

                vCode = rs.getString("VENDOR_CODE");
//                System.out.println("======= vCode in prepareRFQForVendor====: "+vCode+"    reqID: "+reqID);
        if(!existRFQ(vCode,reqID)){
 //       	System.out.println("====vCode: "+vCode+"   reqID: "+reqID+"  issueDate: "+issueDate+"   rfqNo: "+rfqNo);
    addQoutationRequest(vCode,reqID,rfqNo,issueDate,deliveryDate,address1,currency,instruction1,
                    print,contact,phone,email,instruction2,instruction3,address2,address3,desc,stat);
}
              //  System.out.println("reqnID,userId,status and vCode values fetched are "+reqnID+userId+status+vCode);


            }

          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error adding addRFQToVendor in prepareRFQForVendor... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);
      closeConnection(con,pst);
          }

 }

 public void prepareRFQForVendor(String reqID,String rfqNo,
                                String issueDate,String deliveryDate,String address1,
                                String currency,String instruction1,String print,String contact,String phone
                                ,String email,String instruction2,String instruction3,
                                String address2,String address3,String desc,String stat,String userId){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String name = "";
     //   String userId  = "legendPlus";
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userId);
        String vCode = "";
        String servType = "";
        String status = "RQL_08";



        String addValues = "SELECT VENDOR_CODE FROM PR_AWAIT_RQUESTFORQUOTE WHERE ReqnID =? AND Quote_status=?";
// diff satatus
        try {
            con = getConnection("legendPlus");

              pst = con.prepareStatement(addValues);

            pst.setString(1, reqID);
             pst.setString(2, status);
           rs = pst.executeQuery();

            while (rs.next()) {

                vCode = rs.getString("VENDOR_CODE");

        if(!existRFQ(vCode,reqID)){
    addQoutationRequest(vCode,reqID,rfqNo,issueDate,deliveryDate,address1,currency,instruction1,
                    print,contact,phone,email,instruction2,instruction3,address2,address3,desc,stat,userId);
}
              //  System.out.println("reqnID,userId,status and vCode values fetched are "+reqnID+userId+status+vCode);


            }

          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error adding addRFQToVendor in prepareRFQForVendor 2... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);
      closeConnection(con,pst);
          }

 }
public void  addRFQResponseInfo(String vendorCode,String reqnID,String qouteBy,
                                String jobTitle,String email,String comment,String qvalidDay,String status){

        Connection con = null;

        PreparedStatement pst = null;

        MagmaDBConnection dbFormat = new MagmaDBConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");



	String addQuery="insert into PR_RFQ_RESPONSE(VENDOR_CODE,REQNID,QUOTEDBY,JOB_TITLE,EMAIL,DAYSQUOTE_VALID,REMARK,STATUS,Replied_Date)" +
			" values (?,?,?,?,?,?,?,?,?)";


        try {
            con = getConnection("legendPlus");

              pst = con.prepareStatement(addQuery);

            pst.setString(1, vendorCode);
            pst.setString(2, reqnID);
            pst.setString(3, qouteBy);
            pst.setString(4, jobTitle);
            pst.setString(5, email);
            pst.setString(6, qvalidDay);
            pst.setString(7, comment);
            pst.setString(8, status);
            pst.setDate(9, dbFormat.dateConvert(sdf.format(new java.util.Date())));

            pst.execute();


  }
          catch(Exception er)
          {
      System.out.println("Error adding addRFQResponseInfo... ->"+er.getMessage());

          }
          finally
          {

      closeConnection(con,pst);
          }

 }

public  int getSumByFilter(String filter ){

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    int total = 0;

    try{
    String query = "select reqnid from PR_RFQ_RESPONSE_DETAIL   "+filter;
    con = getConnection("legendPlus");

    ps = con.prepareStatement(query);

      rs = ps.executeQuery();
while(rs.next()){
     total+= 1;
System.out.println("total"+total);
        }

    }catch(Exception e){
    System.out.println("Error @ getSumByStatus:: "+e.getMessage());
    }finally {
    closeConnection(con,ps,rs);
    }


System.out.println("returned total "+total);

       return total;

}

 public void prepareRespToQout(String reqnId,String id,String unitPrice,String vendorCode){
       Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ItemsPerReqId item = null;

        String delDate = "";
        String desc ="";
        String qty = "";
        int intQty = 0;
        double price =0.0;
        double costPrice = 0.0;



             String FINDER_QUERY =   "select distinct qout.delivery_date,req.id,req.REQNID,req.itemrequested,req.quantity from PR_REQUEST_FOR_QUOTATION qout," +
             "PR_REQUISITION req where req.reqnId = qout.reqnId and  req.reqnId =? AND req.id =  ?  ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnId);
            ps.setString(2, id);

            rs = ps.executeQuery();

            while (rs.next()) {

                //String id = rs.getString("Id");
                 delDate = rs.getString("delivery_date");
                 desc = rs.getString("itemrequested");
                 qty = rs.getString("quantity");
                 intQty = Integer.parseInt(qty);
                 price = Double.parseDouble(unitPrice);
                 costPrice = price*intQty;


            }

            addQouteRespDetailToRFQ(reqnId,vendorCode,delDate,desc,intQty,price,id ,costPrice);

        } catch (Exception ex) {
            System.out.println("WARNING: cannot do [prepareRespToQout]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }



    }




 public void addQouteRespDetailToRFQ(String reqnId,String vendorCode,
         String deliveryDate,String itemType,int quantity,
         double unitPrice,String itemNo ,double costPrice){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "RQL_02";


	String addQoute = "insert into PR_RFQ_RESPONSE_DETAIL(reqnid,Item_No,vendor_code," +
                "delivery_date,description,quantity,unit_price,cost_price,Awarded) VALUES(?,?,?,?,?,?,?,?,?)";


        try {
            con = getConnection("legendPlus");

              ps= con.prepareStatement(addQoute);

            ps.setString(1, reqnId);
            ps.setString(2, itemNo);
            ps.setString(3, vendorCode);
            ps.setString(4, deliveryDate);
            ps.setString(5, itemType);
            ps.setInt(6, quantity);
            ps.setDouble(7, unitPrice);
            ps.setDouble(8, costPrice);
            ps.setString(9, status);


            ps.execute();
 String query = "UPDATE  PR_REQUEST_FOR_QUOTATION SET STATUS= "+"'"+status+"'"+" WHERE REQNID = "+"'"+reqnId+"'"+" and vendor_Code="+"'"+vendorCode+"'";

 updateQuery(query);


        }catch(Exception er)
          {
      System.out.println("Error adding addQouteRespDetailToRFQ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }

 }


 public void removeVendorFromRFQ(String vendorCode){
        Connection con = null;
        PreparedStatement ps = null;

        String deleteQuery="DELETE FROM PR_AWAIT_RQUESTFORQUOTE WHERE VENDOR_CODE = ? ";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(deleteQuery);
           ps.setString(1, vendorCode);


          ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error removeVendorFromRFQ  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }
 

 }


 public String getStatusByCode(String code){
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet  rs  = null;
     String desc = "";

     String query = "SELECT DESCRIPTION FROM PR_STATUS WHERE CODE =?";
      try{
          con = getConnection("legendPlus");
     ps = con.prepareStatement(query);
     ps.setString(1,code);

    rs = ps.executeQuery();

     while(rs.next()){
      desc = rs.getString("DESCRIPTION");

     }

     }catch(Exception e)
      {
      System.out.println("Error getiing getStatusByCode:: "+e.getMessage());
     }finally{
     closeConnection(con,ps,rs);
     }


return desc;

 }

 public String getItemId(String vendorCode,String reqId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String name = "";

        String FINDER_QUERY = "SELECT ITEM_NO  from PR_RFQ_RESPONSE_DETAIL WHERE VENDOR_CODE =? AND REQNID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            ps.setString(2, reqId);
            rs = ps.executeQuery();

            while (rs.next()) {

                name = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch from getItemId->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return name;

    }

 public double getTotalCost(String vendorCode,String reqId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double cost = 0.0;

        String FINDER_QUERY = "SELECT COST_PRICE  from PR_RFQ_RESPONSE_DETAIL WHERE VENDOR_CODE =? AND REQNID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            ps.setString(2, reqId);
            rs = ps.executeQuery();
 //cost = rs.getDouble(1);
            while (rs.next()) {

                cost += rs.getDouble(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch from getTotalCost->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return cost;

    }
 public String getRFQNoByFilter(String vendorCode,String reqId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String name = "";

        String FINDER_QUERY = "SELECT RFQ_NO  from PR_REQUEST_FOR_QUOTATION WHERE VENDOR_CODE =? AND REQNID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            ps.setString(2, reqId);
            rs = ps.executeQuery();

            while (rs.next()) {

                name = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch from getRFQNoByFilter->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return name;

    }


 public ArrayList  findQoutedItemsByFIlter(String filter){


    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ItemsPerReqId item = null;
 MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT Item_No," +
                "description,quantity,unit_price,Delivery_date from PR_RFQ_RESPONSE_DETAIL  " +
                filter ;



        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);


            rs = ps.executeQuery();


            while (rs.next()) {


                String id = rs.getString("Item_No");
                String desc = rs.getString("description");
                String reqId = rs.getString("reqnid");
                String qty = rs.getString("quantity");
                 String delDate = mag.formatDate(rs.getDate("Delivery_date"));

                double uPrice  = rs.getDouble("UNIT_PRICE");

                item = new ItemsPerReqId(id, desc, reqId, delDate,qty,uPrice);
                collection.add(item);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findQoutedItems]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

 }



public ArrayList  findQoutedItems(String reqId,String vendorCode){


    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ItemsPerReqId item = null;
 MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT Item_No," +
                "description,quantity,unit_price,Delivery_date from PR_RFQ_RESPONSE_DETAIL" +
                " where  reqnid =? and vendor_code =? and ITEM_NO " +
                "in(select item_no from PR_RFQ_RESPONSE_DETAIL where reqnid ="+" '"+reqId+"'"+
                "and vendor_code = "+"'"+vendorCode+"'"+" ) " ;

//System.out.println("======FINDER_QUERY in findQoutedItems: "+FINDER_QUERY);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqId);
            ps.setString(2, vendorCode);
            //ps.setString(3,getItemId(vendorCode,reqId));

            rs = ps.executeQuery();


            while (rs.next()) {


                String id = rs.getString("Item_No");
                String desc = rs.getString("description");
                String qty = rs.getString("quantity");
                 String delDate = mag.formatDate(rs.getDate("Delivery_date"));

                double uPrice  = rs.getDouble("UNIT_PRICE");

                item = new ItemsPerReqId(id, desc, reqId, delDate,qty,uPrice);
                collection.add(item);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findQoutedItems]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

 }

public ArrayList  findQoutedItemsByFilter(String reqId,String vendorCode,String status){


    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ItemsPerReqId item = null;
 MagmaDBConnection mag = new MagmaDBConnection();

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT Item_No," +
                "description,quantity,unit_price,Delivery_date from PR_RFQ_RESPONSE_DETAIL" +
                " where  reqnid =? and vendor_code =? and ITEM_NO " +
                "in(select item_no from PR_RFQ_RESPONSE_DETAIL where reqnid ="+" '"+reqId+"'"+
                "and vendor_code = "+"'"+vendorCode+"'"+" and Awarded = "+"'"+status+"'"+") " ;



        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqId);
            ps.setString(2, vendorCode);
            //ps.setString(3,getItemId(vendorCode,reqId));

            rs = ps.executeQuery();


            while (rs.next()) {


                String id = rs.getString("Item_No");
                String desc = rs.getString("description");
                String qty = rs.getString("quantity");
                String delDate = mag.formatDate(rs.getDate("Delivery_date"));
                double uPrice  = rs.getDouble("UNIT_PRICE");

                item = new ItemsPerReqId(id, desc, reqId, delDate,qty,uPrice);
                collection.add(item);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [findQoutedItemsByFilter]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;

 }

public void  updateQoute(String reqnId,String id,String unitPrice,String qty,String vendorCode){

    Connection con = null;
    PreparedStatement ps = null;



        String updateQuery="UPDATE PR_RFQ_RESPONSE_DETAIL SET  "+
                "UNIT_PRICE = ?,COST_PRICE= ? WHERE REQNID = ? AND VENDOR_CODE = ? AND ITEM_NO = ?";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(updateQuery);
           double uPrice = Double.parseDouble(unitPrice);
           double cPrice = uPrice*Integer.parseInt(qty);

           ps.setDouble(1, uPrice);
           ps.setDouble(2, cPrice);
           ps.setString(3, reqnId);
           ps.setString(4, vendorCode);
           ps.setString(5, id);




         ps.executeUpdate();



  }
          catch(Exception er)
          {
      System.out.println("Error  updateQoute  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }


public void changeRFQByStatus(String reqId,String vendorCode,String filter){


     Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement pst = null;



        String updateQuery="UPDATE PR_REQUEST_FOR_QUOTATION SET  "+
               " STATUS = ? WHERE REQNID = ? AND VENDOR_CODE = ?";

        String updateQuery2="UPDATE PR_RFQ_RESPONSE_DETAIL SET  "+
               " AWARDED = ? WHERE REQNID = ? AND VENDOR_CODE = ?";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(updateQuery);

           pst = con.prepareStatement(updateQuery2);


           ps.setString(1,filter );
           ps.setString(2,reqId );
           ps.setString(3,vendorCode);

           pst.setString(1,filter );
           pst.setString(2,reqId );
           pst.setString(3,vendorCode);

           ps.executeUpdate();

           pst.executeUpdate();
           //ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error  changeRFQByStatus  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }

public void updateRFQResponseInfo(String vendorCode,String reqnId,String qouteBy,
          String jobTitle,String email,String comment,String qvalidDay,
          double calcAmount,double discAmount,double totalCost,String disType){


     Connection con = null;
    PreparedStatement ps = null;



        String updateQuery="UPDATE PR_RFQ_RESPONSE SET  "+
               "QUOTEDBY = ?,JOB_TITLE = ?," +
                "EMAIL = ?,DAYSQUOTE_VALID = ?,REMARK = ?,Calc_Amount=?,TOTAL_COST=?,"
                + "DISCOUNT_AMOUNT=?,DISCOUNT_TYPE=? WHERE REQNID = ? AND VENDOR_CODE = ?";

        try {
            con = getConnection("legendPlus");


           ps = con.prepareStatement(updateQuery);

           ps.setString(1, qouteBy);
           ps.setString(2, jobTitle);
           ps.setString(3, email);
           ps.setString(4, qvalidDay);
           ps.setString(5, comment);
           ps.setDouble(6,calcAmount);
           ps.setDouble(7,totalCost);
           ps.setDouble(8,discAmount);
           ps.setString(9,disType);
           ps.setString(10, reqnId);
           ps.setString(11, vendorCode);

           ps.executeUpdate();
           //ps.execute();



  }
          catch(Exception er)
          {
      System.out.println("Error  updateRFQResponseInfo  ... ->"+er.getMessage());

          }
          finally
          {
      closeConnection(con,ps);

          }


 }



  public ArrayList getVendorAnalysisByFilter(String filter){

      Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order ord = null;
        MagmaDBConnection mag = new MagmaDBConnection();



        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT ORDER_NUMBER,tran_Id,Vendor_Code,Contract_Title,AMOUNT_BALANCE," +
                "Contract_value,Contract_duration,Payment_Type_code,Signature_code1,Signature_code2,prepared_by" +
      ",prepare_date,prepare_time,DUE_DATE,status,supervisor,workStationIP,ID,DELIVERY_COMPLETION,DELIVERY_DATE" +
      ",QUANTITY_BALANCE,ReqnID,Subject_To_Vat,Vat_amount,Total_Amount,Issue_Date" +
            " FROM PR_ORDER  "+ filter;
//        System.out.println("===FINDER_QUERY in getVendorAnalysisByFilter: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            rs = ps.executeQuery();

            while (rs.next()) {

               String orderNo	 = rs.getString("ORDER_NUMBER");
String tran_Id	 = rs.getString("tran_Id");
String Vendor_Code   = rs.getString("Vendor_Code");
String Contract_Title  = rs.getString("Contract_Title");
String amtBal1  = rs.getString("AMOUNT_BALANCE");
String Contract_value  = rs.getString("Contract_value");
String Contract_duration  = rs.getString("Contract_duration");
String Payment_Type_code = rs.getString("Payment_Type_code");
String Signature_code1  = rs.getString("Signature_code1");
String Signature_code2 = rs.getString("Signature_code2");
String prepared_by  = rs.getString("prepared_by");
String prepare_date = mag.formatDate(rs.getDate("prepare_date"));

String prepare_time = rs.getString("prepare_time");
String due_date = mag.formatDate(rs.getDate("due_date"));

String status   = rs.getString("status");
String supervisor  = rs.getString("supervisor");
String workStationIP  = rs.getString("workStationIP");
String id	  = rs.getString("ID");
String delivery_completion = rs.getString("DELIVERY_COMPLETION");
String delivery_date = mag.formatDate(rs.getDate("delivery_date"));

String quantity_balance	  = rs.getString("QUANTITY_BALANCE");
String reqnid	 = rs.getString("ReqnID");
String subject_to_vat   = rs.getString("Subject_To_Vat");
String vat_amount    = rs.getString("Vat_amount");
String total_amount     = rs.getString("Total_Amount");
String issue_date = mag.formatDate(rs.getDate("issue_date"));



              ord = new Order( orderNo,tran_Id,Vendor_Code,Contract_Title,amtBal1,Contract_value,
    Contract_duration,Payment_Type_code,Signature_code1,Signature_code2,
    prepared_by,prepare_date,prepare_time,due_date,status,supervisor,workStationIP,
    id,delivery_completion,delivery_date,quantity_balance,reqnid,
    subject_to_vat, vat_amount,total_amount,issue_date);
                collection.add(ord);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [getVendorAnalysisByFilter]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;




 }




public void createOrder(String ordNo,String trnId,String vendorCode, double contactValue ,
	String jobTitle,String contactDuration,	String signatureCode1,String signatureCode2,
	String subjectToVat,String vatAmount,String whtAmount,
	String preparedBy,String prepareDate, String status,
        String reqId, String delDate){

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        MagmaDBConnection dbFormat = new MagmaDBConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String filter = "ORDERED";

        String name = "";
	String addQuery="INSERT INTO PR_ORDER(ORDER_NUMBER,tran_Id,Vendor_Code," +
                "Contract_value,Contract_duration,Signature_code1,Signature_code2,prepared_by" +
      ",prepare_date,prepare_time,DUE_DATE,status,DELIVERY_DATE" +
      ",ReqnID,Subject_To_Vat,Vat_amount,WHT_Amount,Issue_Date)" +
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//		System.out.println("====addQuery in createOrder: "+addQuery);
		

	    try {
            con = getConnection("legendPlus");
              ps = con.prepareStatement(addQuery);
              ps.setString(1, ordNo);
              ps.setInt(2, Integer.parseInt(trnId));
              ps.setString(3, vendorCode);
              ps.setDouble(4, contactValue);
              ps.setInt(5,Integer.parseInt(contactDuration));
             // ps.setDate(5, dbFormat.dateConvert(deliveryDate));
              //ps.setString(5, deliveryDate);
              ps.setString(6, signatureCode1);
              ps.setString(7, signatureCode2);
              ps.setString(8, preparedBy);
              ps.setDate(9, dbFormat.dateConvert(prepareDate));
              ps.setString(10, prepareDate.substring(0, 10));//time
              ps.setDate(11, dbFormat.dateConvert(prepareDate));//due date
              ps.setString(12, status);
              ps.setDate(13, dbFormat.dateConvert(delDate));
              ps.setString(14, reqId);
              if(subjectToVat == "on"){subjectToVat = "Y";}else{subjectToVat = "N";}
              ps.setString(15, subjectToVat);
               ps.setDouble(16, Double.parseDouble(vatAmount));
              ps.setDouble(17, Double.parseDouble(whtAmount));
              ps.setDate(18, dbFormat.dateConvert(sdf.format(new java.util.Date())));

              ps.execute();

               changeRFQByStatus(reqId,vendorCode,status);

          } catch(Exception er)
		  {
             System.out.println("Error adding createOrder ... ->"+er.getMessage());

		  }
		  finally
		  {
              closeConnection(con,ps);

		  }

 }



 public ArrayList getInsertValues(String vendorCode) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
       // int count = selCol.length;



        String FINDER_QUERY = "SELECT REQNID,USER_ID,STATUS,VENDOR_CODE FROM AM_AD_VENDOR WHERE VENDOR_CODE =?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, vendorCode);
            rs =  ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";
int i = 1;
            while (rs.next()) {

                collection.add(rs.getString(i));
//               System.out.println("Selected values: " + i + " " +rs.getString(i));

                   i++;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch the Selected values" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }


public boolean isRequested(String vendorCode,String reqnId) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RFQuotation rfq = null;
        boolean flag = false;

        ArrayList collection = new ArrayList();

        String FINDER_QUERY = "SELECT CONTACT_EMAIL FROM PR_REQUEST_FOR_QUOTATION WHERE REQNID = ? AND VENDOR_CODE=?";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, reqnId);
             ps.setString(2, vendorCode);
            rs = ps.executeQuery();

            while (rs.next()) {

               flag = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isRequested->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return flag;
    }


}
