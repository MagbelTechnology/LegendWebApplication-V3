package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.*;
import com.magbel.util.DatetimeFormat;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AssetServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
    ApplicationHelper helper;
    public String id;
    public String auotoGenCode;
    CodeGenerator cg;

    public AssetServiceBus()
    {
        auotoGenCode = "";
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
        cg = new CodeGenerator();
    }

    public boolean createAsset(String assetEquiptAssignmentId, String assetId, String assetName, String purchaseDate, double costPrice, String supplier, 
            String warantyPeriod, String username, String branchAssigned, String dept, String status, String lastPeriodMaintd, String lastUpgrade, 
            String contactPerson, String contactPersonEmail, String contactPersonPhoneNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        String isAutoGen = getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
        CREATE_QUERY = "INSERT INTO asset_equipt_assgnment(mtid,asset_equipt_assignment_id,asset_id,asse" +
"t_name,purchase_date,cost_price,supplier,waranty_period,username,branch_assigned" +
",dept,status,last_period_maintd,last_upgrade,contact_person,contact_person_email" +
",contact_person_phone_no)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("asset_equipt_assgnment");
        assetId = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("ASSET", "", "", "") : assetId;
        auotoGenCode = assetId;
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, assetEquiptAssignmentId);
        ps.setString(3, assetId);
        ps.setString(4, assetName);
        ps.setDate(5, dateConvert(purchaseDate));
        ps.setDouble(6, costPrice);
        ps.setString(7, supplier);
        ps.setString(8, warantyPeriod);
        ps.setString(9, username);
        ps.setString(10, branchAssigned);
        ps.setString(11, dept);
        ps.setString(12, status);
        ps.setDate(13, dateConvert(lastPeriodMaintd));
        ps.setDate(14, dateConvert(lastUpgrade));
        ps.setString(15, contactPerson);
        ps.setString(16, contactPersonEmail);
        ps.setString(17, contactPersonPhoneNo);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating Asset... ->" + e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;        
    }

    private ArrayList findAsset(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        Asset impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        query = "SELECT mtid,asset_equipt_assignment_id,asset_id,asset_name,purchase_date,cost_pr" +
"ice,supplier,waranty_period,username,branch_assigned,dept,status,last_period_mai" +
"ntd,last_upgrade,contact_person,contact_person_email,contact_person_phone_no fro" +
"m asset_equipt_assgnment"
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String mtid = rs.getString("mtid");
            String assetEquiptAssignmentId = rs.getString("asset_equipt_assignment_id");
            String assetId = rs.getString("asset_id");
            String assetName = rs.getString("asset_name");
            String purchaseDate = sdf.format(rs.getDate("purchase_date"));
            double costPrice = rs.getDouble("cost_price");
            String supplier = rs.getString("supplier");
            String warantyPeriod = rs.getString("waranty_period");
            String username = rs.getString("username");
            String branchAssigned = rs.getString("branch_assigned");
            String dept = rs.getString("dept");
            String status = rs.getString("status");
            String lastPeriodMaintd = sdf.format(rs.getDate("last_period_maintd"));
            String lastUpgrade = sdf.format(rs.getDate("last_upgrade"));
            String contactPerson = rs.getString("contact_person");
            String contactPersonEmail = rs.getString("contact_person_email");
            String contactPersonPhoneNo = rs.getString("contact_person_phone_no");
            String retiredDate = "";
            impacc = new Asset(mtid, assetEquiptAssignmentId, assetId, assetName, purchaseDate, costPrice, supplier, warantyPeriod, username, branchAssigned, dept, status, lastPeriodMaintd, lastUpgrade, contactPerson, contactPersonEmail, contactPersonPhoneNo);
        }
	} catch (Exception ex) {
		System.out.println("ERROR fetching Asset "
				+ ex.getMessage());
	} finally {
		closeConnection(con, ps);
	}
        return iaList;
    }

    public ArrayList findAssetByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findAsset(criteria);
        return iaList;
    }

    public Asset findRefNumber(String assetId)
    {
        Asset imp = null;
        String filter = (new StringBuilder()).append(" WHERE assetId = '").append(assetId).append("'").toString();
        ArrayList list = findAsset(filter);
        if(list.size() > 0)
        {
            imp = (Asset)list.get(0);
        }
        return imp;
    }

    public boolean updateAsset(String assetEquiptAssignmentId, String assetId, String assetName, String purchaseDate, double costPrice, String supplier, 
            String warantyPeriod, String username, String branchAssigned, String dept, String status, String lastPeriodMaintd, String lastUpgrade, 
            String contactPerson, String contactPersonEmail, String contactPersonPhoneNo, String mtid)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE asset_equipt_assgnment SET asset_equipt_assignment_id=?,asset_id=?,asset_" +
"name=?,purchase_date=?,cost_price=?,supplier=?,waranty_period=?,username=?,branc" +
"h_assigned=?,dept=?,status=?,last_period_maintd=?,last_upgrade=?,contact_person=" +
"?,contact_person_email=?,contact_person_phone_no=? WHERE MTID=?"
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, assetEquiptAssignmentId);
        ps.setString(2, assetId);
        ps.setString(3, assetName);
        ps.setDate(4, dateConvert(purchaseDate));
        ps.setDouble(5, costPrice);
        ps.setString(6, supplier);
        ps.setString(7, warantyPeriod);
        ps.setString(8, username);
        ps.setString(9, branchAssigned);
        ps.setString(10, dept);
        ps.setString(11, status);
        ps.setDate(12, dateConvert(lastPeriodMaintd));
        ps.setDate(13, dateConvert(lastUpgrade));
        ps.setString(14, contactPerson);
        ps.setString(15, contactPersonEmail);
        ps.setString(16, contactPersonPhoneNo);
        ps.setString(17, id);
        done = ps.executeUpdate() != -1;
	} catch (Exception er) {
		System.out.println("Error UPDATING Asset... -> "
				+ er.getMessage());
	} finally {
		closeConnection(con, ps);
	}        
        return done;
    }

    public String getCodeName(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }
	} catch (Exception er) {
		 System.out.println((new StringBuilder()).append("Error in ").append(er.getClass().getName()).append(" - getCodeName()... ->").append(er).toString());
	} finally {
		closeConnection(con, ps);
	}  
        return result;
    }

    private ArrayList findProject(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        Project impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        query = "SELECT MTID,CODE,DESCRIPTION,CUSTOMER_CODE,START_DATE, END_DATE,COST,CAPITAL,OTH" +
"ERS,TRANS_DATE,STATUS FROM ST_GL_PROJECT"
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String id = String.valueOf(rs.getInt("MTID"));
            String code = rs.getString("CODE");
            String desc = rs.getString("DESCRIPTION");
            String cCode = rs.getString("CUSTOMER_CODE");
            String startDt = sdf.format(rs.getDate("START_DATE"));
            String endDt = sdf.format(rs.getDate("END_DATE"));
            String cost = rs.getString("COST");
            String capital = rs.getString("CAPITAL");
            String other = rs.getString("OTHERS");
            String transDt = rs.getString("TRANS_DATE");
            String status = rs.getString("STATUS");
            impacc = new Project(id, code, desc, cCode, startDt, endDt, cost, capital, other, transDt, status);
        }
	} catch (Exception er) {
		System.out.println("ERROR fetching Asset  "
				+ er.getMessage());
	} finally {
		closeConnection(con, ps);
	}  
        return iaList;
    }

    public ArrayList findProByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findProject(criteria);
        return iaList;
    }

    private ArrayList findExchange(String filter)
    {
        ArrayList iaList;
        Connection con;
        PreparedStatement ps;
        String query;
        iaList = new ArrayList();
        ExchangeRate2 impacc = null;
        con = null;
        ps = null;
        ResultSet rs = null;
        query = "SELECT MTID,currency_id,create_dt,effective_dt,exchg_rate,user_id ,status,method" +
" FROM ia_GB_EXCH_RATE"
;
        query = (new StringBuilder(String.valueOf(query))).append(filter).toString();
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next(); iaList.add(impacc))
        {
            String MTID = rs.getString("MTID");
            String currency_id = rs.getString("currency_id");
            String create_dt = sdf.format(rs.getDate("create_dt"));
            String effective_dt = sdf.format(rs.getDate("effective_dt"));
            String exchg_rate = rs.getString("exchg_rate");
            String user_id = rs.getString("user_id");
            String status = rs.getString("status");
            String method = rs.getString("method");
            impacc = new ExchangeRate2(MTID, currency_id, create_dt, effective_dt, exchg_rate, user_id, status, method);
        }
	} catch (Exception er) {
		System.out.println("ERROR fetching Asset  "
				+ er.getMessage());
	} finally {
		closeConnection(con, ps);
	}  
        return iaList;
    }

    public ArrayList findExByQuery(String filter)
    {
        ArrayList iaList = new ArrayList();
        String criteria = (new StringBuilder(" WHERE MTID IS NOT NULL ")).append(filter).toString();
        iaList = findExchange(criteria);
        return iaList;
    }
}