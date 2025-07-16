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
import magma.net.vao.ProcureRequisition;

public class ProcurementManager extends legend.ConnectionClass {

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    ApplicationHelper applHelper = null;
    GenerateList genList = null;

    public ProcurementManager() throws Exception {

        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            genList = new GenerateList();

        } catch (Exception ex) {
        }
    }

    public ProcureRequisition findProcurementReqnByCode(String reqnId) {

        ProcureRequisition procureReqn = null;


        String rmarkQry = "select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity," +
                "itemtype,ReqnBranch,ReqnDepartment,ReqnSection from PR_AWAIT_REQUISITION where reqnId='" + reqnId + "'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                procureReqn = new ProcureRequisition();
                //procureReqn.setUserId(rs.getString("ReqnUserID"));
                procureReqn.setReqnUserId(rs.getString("ReqnUserID"));
                procureReqn.setItemRequested(rs.getString("itemRequested"));
                procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
                procureReqn.setReqnID(rs.getString("ReqnID"));
                //procureReqn.setTranID(rs.getString("transaction_id"));
                procureReqn.setStatus(rs.getString("status"));
                procureReqn.setQuantity(rs.getInt("quantity"));
                procureReqn.setItemType(rs.getString("itemtype"));
                procureReqn.setRequestingBranch(rs.getString("ReqnBranch"));
                procureReqn.setRequestingDept(rs.getString("ReqnDepartment"));
                procureReqn.setRequestingSection(rs.getString("ReqnSection"));


            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByCode() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return procureReqn;

    }

    public ProcureRequisition findProcurementReqnByReqnCode(String reqnId) {

        ProcureRequisition procureReqn = null;


        String rmarkQry = "select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity," +
                "itemtype,ReqnBranch,ReqnDepartment,ReqnSection,Remark,supervisor from PR_REQUISITION where reqnId='" + reqnId + "'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                procureReqn = new ProcureRequisition();
                //procureReqn.setUserId(rs.getString("ReqnUserID"));
                procureReqn.setReqnUserId(rs.getString("ReqnUserID"));
                procureReqn.setItemRequested(rs.getString("itemRequested"));
                procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
                procureReqn.setReqnID(rs.getString("ReqnID"));
                //procureReqn.setTranID(rs.getString("transaction_id"));
                procureReqn.setStatus(rs.getString("status"));
                procureReqn.setQuantity(rs.getInt("quantity"));
                procureReqn.setItemType(rs.getString("itemtype"));
                procureReqn.setRequestingBranch(rs.getString("ReqnBranch"));
                procureReqn.setRequestingDept(rs.getString("ReqnDepartment"));
                procureReqn.setRequestingSection(rs.getString("ReqnSection"));
                procureReqn.setRemark(rs.getString("Remark"));
                procureReqn.setSupervisor(rs.getString("supervisor"));


            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByReqnCode() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return procureReqn;

    }

    public ArrayList findProcurmentWorkInProgressQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ProcureRequisition procureReqn = null;
        String rmarkQry = "select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity,itemtype,id from " +
                "PR_REQUISITION " + filterQry;
       // System.out.println("the filter is >>>>> " + rmarkQry);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                procureReqn = new ProcureRequisition();
                procureReqn.setUserId(rs.getString("ReqnUserID"));
                procureReqn.setItemRequested(rs.getString("itemRequested"));
                procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
                procureReqn.setReqnID(rs.getString("ReqnID"));
                //procureReqn.setTranID(rs.getString("transaction_id"));
                procureReqn.setStatus(rs.getString("status"));
                procureReqn.setQuantity(rs.getInt("quantity"));
                procureReqn.setItemType(rs.getString("itemtype"));
                procureReqn.setRequisitionNum(rs.getInt("id"));
                _list.add(procureReqn);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentWorkInProgressQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ArrayList findProcurmentPIPQry(String filterQry) {
        ArrayList _list = new ArrayList();
        ProcureRequisition procureReqn = null;
       
        String rmarkQry = "select distinct ReqnID,ReqTitle,Create_Date,Status,trans_id,supervisor " +
                "from PR_REQUISITION_SUMMARY "+ filterQry;
       // System.out.println(" the query is >>>>>>>>  " + rmarkQry);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                procureReqn = new ProcureRequisition();

                procureReqn.setRequisitionDate(rs.getString("Create_Date"));
                procureReqn.setReqnID(rs.getString("ReqnID"));
                procureReqn.setStatus(rs.getString("status"));
                procureReqn.setRequistionTitle(rs.getString("ReqTitle"));
                procureReqn.setTranID(rs.getString("trans_id"));
                procureReqn.setSupervisor(rs.getString("supervisor"));

                _list.add(procureReqn);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error  findProcurmentPIPQry ->" + e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);

        }

        return _list;
    }

    public ProcureRequisition findProcurementReqnByReqnCodes(String filter) {

        ProcureRequisition procureReqn = null;


        String rmarkQry = "select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity," +
                "itemtype,ReqnBranch,ReqnDepartment,ReqnSection,Remark,supervisor from PR_REQUISITION " + filter;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(rmarkQry);

            rs = ps.executeQuery();

            while (rs.next()) {
                procureReqn = new ProcureRequisition();
                //procureReqn.setUserId(rs.getString("ReqnUserID"));
                procureReqn.setReqnUserId(rs.getString("ReqnUserID"));
                procureReqn.setItemRequested(rs.getString("itemRequested"));
                procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
                procureReqn.setReqnID(rs.getString("ReqnID"));
                //procureReqn.setTranID(rs.getString("transaction_id"));
                procureReqn.setStatus(rs.getString("status"));
                procureReqn.setQuantity(rs.getInt("quantity"));
                procureReqn.setItemType(rs.getString("itemtype"));
                procureReqn.setRequestingBranch(rs.getString("ReqnBranch"));
                procureReqn.setRequestingDept(rs.getString("ReqnDepartment"));
                procureReqn.setRequestingSection(rs.getString("ReqnSection"));
                procureReqn.setRemark(rs.getString("Remark"));
                procureReqn.setSupervisor(rs.getString("supervisor"));



            }



        } catch (Exception ex) {
            System.out.println("Error occurred in findProcurementReqnByReqnCodes() of " + this.getClass().getName() + " --> ");
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return procureReqn;

    }

    public int updateProcurementRequisition(ProcureRequisition procRequisition) {
        String query_r = "update pr_requisition set ItemType=?,ItemRequested=?,Remark=?,workStationIP=?,Quantity=?," +
                "ReqnBranch=?,ReqnSection=?,ReqnDepartment=?,ReqnDate=?" +
                " where reqnId = '" + procRequisition.getReqnID() + "'and id=" + procRequisition.getRequisitionNum();

        Connection con = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            con = dbConnection.getConnection("legendPlus");

            ps = con.prepareStatement(query_r);
            ps.setString(1, procRequisition.getItemType());
            ps.setString(2, procRequisition.getItemRequested());
            ps.setString(3, procRequisition.getRemark());
            ps.setString(4, procRequisition.getIpAddress());
            ps.setInt(5, procRequisition.getQuantity());
            ps.setString(6, procRequisition.getRequestingBranch());
            ps.setString(7, procRequisition.getRequestingSection());
            ps.setString(8, procRequisition.getRequestingDept());
            ps.setTimestamp(9, dbConnection.getDateTime(new java.util.Date()));

            i = ps.executeUpdate();

        } catch (Exception ex) {

            System.out.println("Error occured in " + this.getClass().getName() + ": updateProcurementRequisition()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

       // System.out.println(" the value of updated code is >>>>>>"+ i);
        return i;
    }//updateProcurementRequisition
}
