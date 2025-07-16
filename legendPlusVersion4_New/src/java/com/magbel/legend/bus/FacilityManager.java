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
 *
 * @author Ganiyu
 */
import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import java.sql.*;

import com.magbel.util.DatetimeFormat;

import java.util.ArrayList;

import magma.net.vao.CompletedFacMgtWork;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.FacilityAcceptance;
import magma.net.vao.FacilityRequisition;
import magma.net.vao.ProcureRequisition;
import magma.net.vao.WorkDescription;
import magma.net.vao.WorkOrder;

public class FacilityManager extends legend.ConnectionClass {

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    ApplicationHelper applHelper = null;
    GenerateList genList = null;
    public ApprovalRecords approvalRec;

    public FacilityManager() throws Exception {

        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            genList = new GenerateList();
            approvalRec = new ApprovalRecords(); 

        } catch (Exception ex) {
        }
    }

    public int[] creatWorkDescription(ArrayList workDescriptionlist) {

        boolean result = false;
        int[] results = new int[2];
        applHelper = new ApplicationHelper();
        int workId = Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
        results[0] = workId;
        String query = "insert into  FM_WORK_DESCRIPTION (work_id,asset_id,work_number,work_description,ReqnId) " +
                "values(?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());
                ps.setString(5, workDescription.getReqnId());
                results[1] = ps.executeUpdate();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescription --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }

    public ArrayList getWorkDescriptions(int workId,String reqnId) {

        int workNumber = 0;
        String workDescription = "";
        WorkDescription work = null;
        ArrayList workList = new ArrayList();

        String query =
                "select  work_number,work_description from FM_WORK_DESCRIPTION where work_id =" + workId + " and ReqnId = '"+reqnId+"'order by work_number";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                //staffRecord = new StaffRecord();

                workNumber = rs.getInt("work_number");
                workDescription = rs.getString("work_description");
                reqnId = rs.getString("work_number");
                work = new WorkDescription(workNumber, workDescription, reqnId);
                workList.add(work);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getWorkDescriptions --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;

    }
    
    public FacilityAcceptance findFacilityAcceptanceByCode(String acceptanceCode) {

        FacilityAcceptance facilityAcceptance = null;
        String requistionID = "";
        String acceptanceRemark = "";
        int supervisor = 0;
        String receiveMeans = "";
        int workId =0;
        int numOfWorkDesc=0;

        String query =
                "select  ReqnID,ACCEPTANCE_REMARK,Supervisor,receive_means,work_id,No_Work_Description from FM_MAITENANCE_DUE where ACCEPTANCE_CODE ='" + acceptanceCode + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                requistionID = rs.getString("ReqnID");
                acceptanceRemark = rs.getString("ACCEPTANCE_REMARK");
                supervisor = rs.getInt("Supervisor");
                receiveMeans = rs.getString("receive_means");
                workId = rs.getInt("work_id");
                numOfWorkDesc = rs.getInt("No_Work_Description");
                facilityAcceptance = new FacilityAcceptance(acceptanceCode, requistionID, acceptanceRemark, supervisor, receiveMeans,workId,numOfWorkDesc);

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findFacilityAcceptanceByCode --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return facilityAcceptance;

    }

    public ArrayList findApprovedFacManagement(String filter) {

         FacilityAcceptance facilityAcceptance = null;
        ArrayList workList = new ArrayList();

        String acceptanceCode = "";
        String assetId = "";
        int receivedBy = 0;
        String receivedDate = "";
        int tranId=0;
        String status="";
        String processWorkOrder="";
        String isImage ="";
        String reqnID="";

        String query =
        "select ACCEPTANCE_CODE,ASSET_ID,RECEIVE_DATE,RECEIVE_BY,TRAN_ID,Status,PROCESS_WORK_ORDER,image,ReqnID  "+
                "from FM_MAITENANCE_DUE where acceptance_code is not null " + filter;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                acceptanceCode = rs.getString("ACCEPTANCE_CODE");
                assetId = rs.getString("ASSET_ID");
                receivedBy = rs.getInt("RECEIVE_BY");
                receivedDate = rs.getString("RECEIVE_DATE");
                tranId = rs.getInt("TRAN_ID");
                status = rs.getString("Status");
                processWorkOrder =rs.getString("PROCESS_WORK_ORDER");
                isImage= rs.getString("image");
                reqnID= rs.getString("ReqnID");
                facilityAcceptance = new FacilityAcceptance(acceptanceCode, assetId, receivedBy, receivedDate,tranId,status,processWorkOrder);
                facilityAcceptance.setIsImage(isImage);
                facilityAcceptance.setRequistionID(reqnID);
                workList.add(facilityAcceptance);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findApprovedFacManagement --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;

    }


public ArrayList getWorkOrderDescriptions_old(String acceptanceCode) {

        int workNumber = 0;
        String workDescription = "";
        int workId =0;
        String reqnID="";

        WorkDescription work = null;
        ArrayList workList = new ArrayList();

   String query = "select work_number, work_description,work_id,reqnID from FM_WORK_DESCRIPTION "+
           "where work_id  in (select a.work_id from FM_Requisition a, FM_MAITENANCE_DUE b, FM_WORK_DESCRIPTION"+
           " c where a.reqnid = b.reqnid and b.acceptance_code='"+acceptanceCode+"')order by work_number";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {


                workNumber = rs.getInt("work_number");
                workDescription = rs.getString("work_description");
                 workId= rs.getInt("work_id");
                 reqnID= rs.getString("ReqnID");

                work = new WorkDescription(workNumber, workDescription,reqnID);
                work.setWorkId(workId);

                workList.add(work);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getWorkOrderDescriptions --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;
    }

    public WorkOrder findFacilityWorkOrderByCode(String acceptanceCode) {

        WorkOrder facilityWorkOrder = null;

        String vendorCode = "";
        String contractTitle = "";
        double contractValue =0.00;
        int contractDuration =0;
        String paymentCode="";
        String signatureOrderCode ="";
        String signature = "";
        double contractBalance =0.00;
        String reqnId ="";
        String jobCompletion="";
        String subjectToVat ="";
        double vatAmount=0.00;
        double totalAmount=0.00;
        String query =
                "select Vendor_Code,Contract_Title,Contract_value,Contract_duration," +
                " Payment_Type_code,Signature_code1,signature_code2,Contract_Balance,ReqnID,Job_Completion,Subject_To_Vat,Vat_amount,total_amount" +
                " from FM_WORK_ORDER where WORK_ORDER_CODE ='" + acceptanceCode + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                vendorCode = rs.getString("Vendor_Code");
                contractTitle = rs.getString("Contract_Title");
                contractValue = rs.getDouble("Contract_value");
                contractDuration = rs.getInt("Contract_duration");
                paymentCode = rs.getString("Payment_Type_code");
                signatureOrderCode = rs.getString("Signature_code1");
                signature = rs.getString("Signature_code2");
                contractBalance = rs.getDouble("Contract_Balance");
                reqnId = rs.getString("ReqnID");
                jobCompletion = rs.getString("Job_Completion");
               subjectToVat = rs.getString("Subject_To_Vat");
                vatAmount = rs.getDouble("Vat_amount");
                totalAmount = rs.getDouble("total_amount");
                facilityWorkOrder = new WorkOrder(acceptanceCode, vendorCode, contractTitle, contractValue, contractDuration,paymentCode,signatureOrderCode,signature);
                facilityWorkOrder.setContractBalance(contractBalance);
                facilityWorkOrder.setReqnId(reqnId);
                facilityWorkOrder.setIsJobCompleted(jobCompletion);
                facilityWorkOrder.setIsSubjectToVat(subjectToVat);
                facilityWorkOrder.setVatAmount(vatAmount);
                facilityWorkOrder.setTotalAmount(totalAmount);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findFacilityWorkOrderByCode --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return facilityWorkOrder;

    }


    public String[] getVendorInfo(String vendorCode){

    String[] vendor= new String[2];

        String query =
                "select Contact_Person,Contact_Address" +
                " from AM_AD_VENDOR where Vendor_Code ='" + vendorCode + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                vendor[0] = rs.getString("Contact_Person");
                vendor[1] = rs.getString("Contact_Address");


            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getVendorInfo --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return vendor;
    }

    public ArrayList getWorkOrderDescriptions(String acceptanceCode, String reqnId) {

        int workNumber = 0;
        String workDescription = "";
        int workId =0;
        String reqnID = "";
        WorkDescription work = null;
        ArrayList workList = new ArrayList();

   String query = "select work_number, work_description,work_id,reqnID from FM_WORK_DESCRIPTION "+
          " where work_id  in (select work_id from FM_MAITENANCE_DUE  where acceptance_code='"+acceptanceCode+"') and reqnId = '"+reqnId+"' order by work_number";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {


                workNumber = rs.getInt("work_number");
                workDescription = rs.getString("work_description");
                 workId= rs.getInt("work_id");
                 reqnID = rs.getString("reqnID");
                work = new WorkDescription(workNumber, workDescription,reqnID);
                work.setWorkId(workId);

                workList.add(work);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getWorkOrderDescriptions --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;
    }

    public int[] creatWorkDescriptionRepost(ArrayList workDescriptionlist,int repostWorkID) {

        boolean result = false;
        int[] results = new int[2];
        applHelper = new ApplicationHelper();
        int workId = repostWorkID; //Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
         String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION where work_id="+repostWorkID;
        genList.updateTable(deleteWorks_Query);
         results[0] = workId;

         String query = "insert into  FM_WORK_DESCRIPTION (work_id,asset_id,work_number,work_description) " +
                "values(?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());

                //result = (ps.executeUpdate() != -1);
                results[1] = ps.executeUpdate();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescriptionRepost --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }
    public ArrayList getWorkOrderDescriptionsByWorkID(int workid) {

        int workNumber = 0;
        String workDescription = "";
        int workId =0;
        String assetid="";
        String reqnID = "";
        WorkDescription work = null;
        ArrayList workList = new ArrayList();

   String query = "select work_number, work_description,work_id,asset_id,reqnID from FM_WORK_DESCRIPTION "+
          " where work_id  = "+workid+" order by work_number";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {


                workNumber = rs.getInt("work_number");
                workDescription = rs.getString("work_description");
                 workId= rs.getInt("work_id");
                 assetid = rs.getString("asset_id");
                 reqnID = rs.getString("reqnID"); 
                work = new WorkDescription(workNumber, workDescription,reqnID);
                work.setWorkId(workId);
                work.setAssetId(assetid);
                workList.add(work);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getWorkOrderDescriptionsByWorkID --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;
    }


    public int[] creatWorkDescriptionBackUp(ArrayList workDescriptionlist) {

        boolean result = false;
        int[] results = new int[2];
        //applHelper = new ApplicationHelper();
        //int workId = Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));

        String query = "insert into  FM_WORK_DESCRIPTION_TEMP (work_id,asset_id,work_number,work_description) " +
                "values(?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workDescription.getWorkId());
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());

                //result = (ps.executeUpdate() != -1);
                results[1] = ps.executeUpdate();
                results[0] = workDescription.getWorkId();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescriptionBackUp --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }

    public ArrayList getWorkDescriptionsBackup(int workId,String reqnId) {

        int workNumber = 0;
        String workDescription = "";
        String assetId ="";
        String reqnID = "";
        WorkDescription work = null;
        ArrayList workList = new ArrayList();


        String query =
                "select  work_number,work_description,asset_id,reqnID from FM_WORK_DESCRIPTION_TEMP where work_id =" + workId + " AND ReqnId = '"+reqnId+"' order by work_number";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                //staffRecord = new StaffRecord();

                workNumber = rs.getInt("work_number");
                workDescription = rs.getString("work_description");
                assetId = rs.getString("asset_id");
                reqnID = rs.getString("reqnID");
                work = new WorkDescription(workNumber, workDescription,reqnID);
                work.setAssetId(assetId);
                workList.add(work);

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in getWorkDescriptionsBackup --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workList;

    }


        public ArrayList findApprovedWorkOrder(String filter) {

         WorkOrder workOrder = null;
        ArrayList workOrderList = new ArrayList();

        String workOrderCode = "";
        String assetId = "";
        int receivedBy = 0;
        String receivedDate = "";
        int tranId=0;
        String contractTitle="";
        double contractValue=0.00;
        int workId =0;
        String reqnId = "";
        String query =
        "select ReqnID,WORK_ORDER_CODE,ASSET_ID,PREPARE_DATE,PREPARED_BY,TRAN_ID,CONTRACT_TITLE,CONTRACT_VALUE,WORK_ID  "+
                "from FM_WORK_ORDER where WORK_ORDER_CODE is not null " + filter;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
            	reqnId = rs.getString("ReqnID");
                workOrderCode = rs.getString("WORK_ORDER_CODE");
                assetId = rs.getString("ASSET_ID");
                receivedBy = rs.getInt("PREPARED_BY");
                receivedDate = rs.getString("PREPARE_DATE");
                tranId = rs.getInt("TRAN_ID");
                contractTitle = rs.getString("CONTRACT_TITLE");
                contractValue =rs.getDouble("CONTRACT_VALUE");
                workId = rs.getInt("WORK_ID");
                workOrder = new WorkOrder(assetId, workOrderCode, contractTitle, contractValue,tranId,receivedBy,receivedDate);
                workOrder.setWorkId(workId);
                workOrder.setReqnId(reqnId);
                workOrderList.add(workOrder);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findApprovedWorkOrder --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workOrderList;

    }

        public int[] creatWorkDescriptionTemp(ArrayList workDescriptionlist,String reqnId) {

        boolean result = false;
        int[] results = new int[2];
        applHelper = new ApplicationHelper();
        int workId = Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
        results[0] = workId;
        String query = "insert into  FM_WORK_DESCRIPTION_TEMP (work_id,asset_id,work_number,work_description,ReqnId) " +
                "values(?,?,?,?,?)";
        String queryupdate = "update FM_WORK_DESCRIPTION_TEMP set work_description= ? where ReqnId=? AND work_id=?  AND work_number=? ";
        
        int recNo =Integer.parseInt(approvalRec.getCodeName("select count(*) from FM_WORK_DESCRIPTION_TEMP where ReqnId = '"+reqnId+"'"));
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps1 = con.prepareStatement(queryupdate);
            if(recNo==0){
            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());
                ps.setString(5, workDescription.getReqnId());
                results[1] = ps.executeUpdate();
            }
            }else{
                for (int i = 0; i < workDescriptionlist.size(); ++i) {
                    WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                    ps1.setString(1, workDescription.getWorkDescription());
                    ps1.setString(2, workDescription.getReqnId());
                    ps1.setInt(3, workId);
                    ps1.setInt(4, workDescription.getWorkNumber());
                    results[1] = 1;
                    ps1.execute();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescriptionTemp --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
            dbConnection.closeConnection(con, ps1);
        }

        return results;

    }

public int[] creatWorkDescription(ArrayList workDescriptionlist,int workID) {

        boolean result = false;
        int[] results = new int[3];
        applHelper = new ApplicationHelper();
        int workId = workID; //Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
        // String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION where work_id="+workID;
        //genList.updateTable(deleteWorks_Query);
         results[0] = workId;

         String query = "insert into  FM_WORK_DESCRIPTION (work_id,asset_id,work_number,work_description,ReqnId) " +
                "values(?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());
                ps.setString(5, workDescription.getReqnId());
                //result = (ps.executeUpdate() != -1);
                results[1] = ps.executeUpdate();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescription --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }


 public int[] creatWorkDescriptionBackUp(ArrayList workDescriptionlist,int repostWorkID) {

        boolean result = false;
        int[] results = new int[2];
        applHelper = new ApplicationHelper();
        int workId = repostWorkID; //Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
         String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION_TEMP where work_id="+repostWorkID;
        genList.updateTable(deleteWorks_Query);
         results[0] = workId;

         String query = "insert into  FM_WORK_DESCRIPTION_TEMP (work_id,asset_id,work_number,work_description,ReqnId) " +
                "values(?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());
                ps.setString(5, workDescription.getReqnId());
                //result = (ps.executeUpdate() != -1);
                results[1] = ps.executeUpdate();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescriptionBackUp --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }

 public int[] creatWorkDescriptionBackUp(ArrayList workDescriptionlist,int repostWorkID,String reqnId) {

        boolean result = false;
        int[] results = new int[2];
        applHelper = new ApplicationHelper();
        int workId = repostWorkID; //Integer.parseInt(applHelper.getGeneratedId("FM_WORK_DESCRIPTION"));
         String deleteWorks_Query = "delete from FM_WORK_DESCRIPTION_TEMP where work_id="+repostWorkID;
        genList.updateTable(deleteWorks_Query);
         results[0] = workId;

         String query = "insert into  FM_WORK_DESCRIPTION_TEMP (work_id,asset_id,work_number,work_description,ReqnId) " +
                "values(?,?,?,?,?)";
         String queryupdate = "update FM_WORK_DESCRIPTION set work_description= ? where ReqnId=? AND work_id=?  AND work_number=? ";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        //System.out.println("===queryupdate in creatWorkDescriptionBackUp:"+queryupdate);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps1 = con.prepareStatement(queryupdate);
            
            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps.setInt(1, workId);
                ps.setString(2, workDescription.getAssetId());
                ps.setInt(3, workDescription.getWorkNumber());
                ps.setString(4, workDescription.getWorkDescription());
                ps.setString(5, reqnId);
                
                //result = (ps.executeUpdate() != -1);
                results[1] = ps.executeUpdate();
            }
            for (int i = 0; i < workDescriptionlist.size(); ++i) {
                WorkDescription workDescription = (WorkDescription) workDescriptionlist.get(i);
                ps1.setString(1, workDescription.getWorkDescription());
                ps1.setString(2, reqnId);
                ps1.setInt(3, workId);
                ps1.setInt(4, workDescription.getWorkNumber());
//                System.out.println("====reqnId in creatWorkDescriptionBackUp: "+reqnId+"   workId: "+workId+"  workDescription.getWorkNumber(): "+workDescription.getWorkNumber());
                results[1] = 1;
                ps1.execute();
            }

        } catch (Exception ex) {
            System.out.println("Error occurred in creatWorkDescriptionBackUp --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
            dbConnection.closeConnection(con, ps);
        }

        return results;

    }


     public CompletedFacMgtWork findCompletedWorkByReqnCode(String reqn) {

        CompletedFacMgtWork completedWork = null;
        //String requistionID = "";
        //String acceptanceRemark = "";
        //int supervisor = 0;
        //String receiveMeans = "";
        //int workId =0;
        //int numOfWorkDesc=0;

        String assetId="";
        String workOrderCode="";
        int workCompletionRecepient =0;
        String image ="";
        String query =
                "select  asset_id,WORK_ORDER_CODE,work_completion_recepient,image from FM_COMPLETED_WORK where ReqnID ='" + reqn + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                assetId = rs.getString("asset_id");
                workOrderCode = rs.getString("WORK_ORDER_CODE");
                image = rs.getString("image");
                workCompletionRecepient = rs.getInt("work_completion_recepient");

                completedWork = new CompletedFacMgtWork(assetId,workOrderCode);
                completedWork.setImage(image);
                completedWork.setWorkCompletionRecepient(workCompletionRecepient);

            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findCompletedWorkByReqnCode of "+this.getClass().getName()+" --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return completedWork;

    }


     public int[] createWorkDescriptionsFromTemp(int workId,String reqnId)  {

        int[] numb = new int[3];
        ArrayList workList = new ArrayList();

        try {

            FacilityManager facilityManager = new FacilityManager();
            //int workId = (Integer) request.getSession().getAttribute("facilityMgtWorkId");
            System.out.println("the >>>>>>>>>>value of workid is " + workId);
            workList = facilityManager.getWorkDescriptionsBackup(workId,reqnId);
            numb = facilityManager.creatWorkDescription(workList, workId);
            numb[2] = workList.size();

        } catch (Exception e) {
            System.out.println(">>Error occured in createWorkDescriptionsTemp of "+this.getClass().getName()+"  " + e);

        }


        return numb;


    }

     public ArrayList findCompletedWorkForConfirmation(String filter) {

         WorkOrder workOrder = null;
        ArrayList workOrderList = new ArrayList();

        String workOrderCode = "";
        String assetId = "";
        int receivedBy = 0;
        String receivedDate = "";
        int tranId=0;
        String contractTitle="";
        double contractValue=0.00;
        int workId =0;
        String reqnId="";
        String query =
        "select b.WORK_ORDER_CODE,b.ASSET_ID,b.RECEIVE_DATE,b.RECEIVE_BY,b.TRAN_ID,b.WORK_ID,b.ReqnID,a.Contract_value, a.CONTRACT_TITLE from  " +
        "FM_WORK_ORDER a, FM_COMPLETED_WORK b where b.WORK_ORDER_CODE is not null " +
        "and a.work_order_code=b.work_order_code " + filter;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                workOrderCode = rs.getString("WORK_ORDER_CODE");
                assetId = rs.getString("ASSET_ID");
                receivedBy = rs.getInt("RECEIVE_BY");
                receivedDate = rs.getString("RECEIVE_DATE");
                tranId = rs.getInt("TRAN_ID");
                contractTitle = rs.getString("CONTRACT_TITLE");
                contractValue =rs.getDouble("CONTRACT_VALUE");
                workId = rs.getInt("WORK_ID");
                reqnId = rs.getString("ReqnID");

                workOrder = new WorkOrder(assetId, workOrderCode, contractTitle, contractValue,tranId,receivedBy,receivedDate);
                workOrder.setWorkId(workId);
                workOrder.setReqnId(reqnId);
                workOrderList.add(workOrder);
            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findApprovedWorkOrder --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return workOrderList;

    }

     public CompletedFacMgtWork findAcceptedWorkByCode(String wOrderCode) {

         CompletedFacMgtWork workOrder = null;
        ArrayList workOrderList = new ArrayList();

        String workOrderCode = "";
        String assetId = "";
        int receivedBy = 0;
        String receivedDate = "";
        int tranId=0;
        String contractTitle="";
        double contractValue=0.00;
        int workId =0;
        String reqnId="";
        String receivedTime="";
        String acceptanceRemark="";
        int recepientId=0;
        int supervisor =0;

        String query =
        "select RECEIVE_DATE,RECEIVE_BY,TRAN_ID,ReqnID,Supervisor," +
        "work_completion_recepient,ACCEPTANCE_REMARK,RECEIVE_TIME from  " +
        " FM_COMPLETED_WORK_ACCEPTANCE where  WORK_ORDER_CODE= '"+wOrderCode+"'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                receivedBy = rs.getInt("RECEIVE_BY");
                receivedDate = rs.getString("RECEIVE_DATE");
                tranId = rs.getInt("TRAN_ID");
                reqnId = rs.getString("ReqnID");
                receivedTime = rs.getString("RECEIVE_TIME");
                supervisor = rs.getInt("Supervisor");
                recepientId = rs.getInt("work_completion_recepient");
                acceptanceRemark = rs.getString("ACCEPTANCE_REMARK");

                workOrder = new CompletedFacMgtWork(tranId,receivedBy,receivedDate,reqnId,recepientId,supervisor,receivedTime,acceptanceRemark);
               
                //workOrderList.add(workOrder);
            }
        } catch (Exception ex) {
            System.out.println("Error occurred in findAcceptedWorkByCode --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        return workOrder;
    }

     public int[] creatWorkDescriptionTempUpdate(ArrayList workDescriptionlist, int workId,String reqnId)
     {
         int results[];
         String query;
         Connection con;
         PreparedStatement ps;
         boolean result = false;
         results = new int[2];
         applHelper = new ApplicationHelper();
         results[0] = workId;
         query = "insert into  FM_WORK_DESCRIPTION_TEMP (work_id,asset_id,work_number,work_description,ReqnId) values(?,?,?,?,?)" ;
         con = null;
         ps = null;
         
         try {
        	 con = dbConnection.getConnection("legendPlus");
        	 ps = con.prepareStatement(query);
         for(int i = 0; i < workDescriptionlist.size(); i++)
         {
        	 System.out.println("<<=====workDescriptionlist: "+i+"     reqnId: "+reqnId);
             WorkDescription workDescription = (WorkDescription)workDescriptionlist.get(i);
             ps.setInt(1, workId);
             ps.setString(2, workDescription.getAssetId());
             ps.setInt(3, workDescription.getWorkNumber());
             ps.setString(4, workDescription.getWorkDescription());
             ps.setString(5, reqnId);
             results[1] = ps.executeUpdate();
         }
     } catch (Exception ex) {
         System.out.println("Error occurred in creatWorkDescriptionTemp --> ");
         ex.printStackTrace();
     } finally {
         dbConnection.closeConnection(con, ps);
     }
         return results;
     }


     public ArrayList findPPMFacManagementList(String filter) {

    	 FMppmAllocation scheduleList = null;
         ArrayList ppmList = new ArrayList();

         String query =
         "select b.ZONE_CODE, c.Zone_Name,a.BRANCH_CODE, * from FM_PPM a, am_ad_branch b, am_ad_Zone c where a.BRANCH_CODE = b.BRANCH_CODE and b.ZONE_CODE = c.Zone_Code " + filter;

         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;


         try {
             con = dbConnection.getConnection("legendPlus");
             ps = con.prepareStatement(query);

             rs = ps.executeQuery();

             while (rs.next()) {

                 String id = rs.getString("ID");
                 String transId = rs.getString("transId");
                 String branchCode = rs.getString("BRANCH_CODE");
                 String categoryCode = rs.getString("CATEGORY_CODE");
                 String subCategoryCode = rs.getString("SUB_CATEGORY_CODE");
                 String groupId = rs.getString("GROUP_ID");
                 String vendorCode = rs.getString("VENDOR_CODE");
                 String description = rs.getString("DESCRIPTION");
                 String lastserviceDate = rs.getString("LASTSERVICE_DATE");
                 String q1DueDate = rs.getString("Q1_DUE_DATE");
                 String q2DueDate = rs.getString("Q2_DUE_DATE");
                 String q3DueDate = rs.getString("Q3_DUE_DATE");
                 String q4DueDate = rs.getString("Q4_DUE_DATE");
                 String q1Status = rs.getString("Q1_STATUS"); 
                 String q2Status = rs.getString("Q2_STATUS"); 
                 String q3Status = rs.getString("Q3_STATUS"); 
                 String q4Status = rs.getString("Q4_STATUS"); 
                 String type = rs.getString("TYPE");   
                 String zoneCode = rs.getString("ZONE_CODE");   
                 String zoneName = rs.getString("Zone_Name");   
//                 String status = rs.getString("STATUS"); 
                 String postingDate = rs.getString("POSTING_DATE"); 
                 
                 scheduleList = new FMppmAllocation(id, transId, branchCode, categoryCode, 
                 		subCategoryCode, vendorCode, 
                         description,  lastserviceDate,q1DueDate,  q2DueDate, q3DueDate, q4DueDate,
                         q1Status, q2Status, q3Status, q4Status, type,
                         "", postingDate);
                 

                 
                 scheduleList.setZoneCode(zoneCode);
                 scheduleList.setZoneName(zoneName);
                 ppmList.add(scheduleList);


             }



         } catch (Exception ex) {
             System.out.println("Error occurred in findApprovedFacManagement --> ");
             ex.printStackTrace();
         } finally {
             dbConnection.closeConnection(con, ps, rs);
         }

         return ppmList;

     }

public ArrayList findFacilityRequisitionListByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();
	         Connection con = null;
	         PreparedStatement ps = null;
	         ResultSet rs = null;
	         FacilityRequisition facilityReqn = null;
String rmarkQry="select *from FM_PPM_AWAIT_REQUISITION where status='X'"+ filterQry;
			System.out.println("rmarkQry >>> " + rmarkQry);

			try
			{
	             con = dbConnection.getConnection("legendPlus");
	             ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{  
					facilityReqn = new FacilityRequisition();
					facilityReqn.setUserId(rs.getString("ReqnUserID"));
					facilityReqn.setItemRequested(rs.getString("itemRequested"));
					facilityReqn.setReqnID(rs.getString("ReqnID"));
					facilityReqn.setTranID(rs.getString("TRANSID"));
					facilityReqn.setRequisitionDate(rs.getString("ReqnDate"));
					facilityReqn.setBranchCode(rs.getString("ReqnBranch"));
					facilityReqn.setRequestingDept(rs.getString("ReqnDepartment"));
					facilityReqn.setCategoryCode(rs.getString("CATEGORY_CODE"));
					facilityReqn.setSubCategoryCode(rs.getString("SUB_CATEGORY_CODE"));
					facilityReqn.setDescription(rs.getString("DESCRIPTION"));
					facilityReqn.setStatus(rs.getString("status"));
//					facilityReqn.setQuantity(rs.getInt("quantity"));
					facilityReqn.setItemType(rs.getString("itemtype"));
					_list.add(facilityReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error findFacilityRequisitionListByQry ->" + e.getMessage());
			}

			finally
			{
				 dbConnection.closeConnection(con, ps, rs);
			}

			return _list;
		}
public int updateTable(String query)
{
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int result=0;
    try 
    {
    	con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        result=ps.executeUpdate();
    } 
    catch (Exception ex) 
    {
        System.out.println("WARNING:FacilityManager: cannot update +" + ex.getMessage());
    }
    finally 
    {
    	 dbConnection.closeConnection(con, ps, rs);
    }
    return result;
}

public String formatDate(String date)
   {
   	String dd=date.substring(8, 10);
   	//System.out.println("dd>>>>" + dd);
		String mm=date.substring(4, 8);
		//System.out.println("mm>>>>" + mm);
		String yyyy=date.substring(0, 4);
		//System.out.println("yyyy>>>>" + yyyy);
		date =dd + mm+ yyyy; 
		return date;
   }

}
