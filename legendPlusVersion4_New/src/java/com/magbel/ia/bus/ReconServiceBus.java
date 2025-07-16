package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
//import com.magbel.ia.servlet.branchId;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;
import com.magbel.util.DatetimeFormat;

import java.io.PrintStream;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReconServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    DatetimeFormat df;
    ApplicationHelper helper;

    public ReconServiceBus()
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
    }

    public String createProject(String code, String desc, String cCode, String startDt, String endDt, String cost, String capital, 
            String other, String transDt, String status,String branchId,String departmentId,String sbuCode,
            String onlineNumber,String projectAcct,String projectSponsor,String projectOwner,String projectManager,String projectFundBal,String projectAmtUtilized)
    {
        Connection con;
        PreparedStatement ps;
        String id;
        String query;
        con = null;
        ps = null;
        boolean done = false;
        id = "";
        query = "INSERT INTO ST_GL_PROJECT(MTID,CODE,DESCRIPTION,CUSTOMER_CODE,START_DATE, END_DA" +
"TE,COST,CAPITAL,OTHERS,TRANS_DATE,STATUS,BRANCH_ID,DEPT_ID,SBU_CODE,ONLINE_NUMBER,PROJECT_ACCOUNT," +
"PROJECT_SPONSOR,PROJECT_OWNER,PROJECT_MANAGER,PROJECT_BALANCE,AMOUNT_UTILIZED) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,? ,?,?,?,?,?,?,?,?,?,?,?)"
;
//        System.out.println("<<<<<<<createProject query: "+query);
    //    System.out.println("======>Cost Value: "+cost+"     capital: "+capital);
        id = (new ApplicationHelper()).getGeneratedId("ST_GL_PROJECT");
        String lastUpdateDate = sdf.format(new java.util.Date());
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, desc);
        ps.setString(4, cCode);
        ps.setDate(5, dateConvert(startDt));
        ps.setDate(6, dateConvert(endDt));
        cost = cost.replaceAll(",","");
    //    System.out.println("======>Cost Value: "+cost+"      Double.valueOf(cost): "+Double.valueOf(cost));
        ps.setDouble(7, Double.valueOf(cost));
        capital = capital.replaceAll(",","");
     //   System.out.println("======>capital Value: "+capital+"      Double.valueOf(capital): "+Double.valueOf(capital));
        ps.setDouble(8, Double.valueOf(capital));
        ps.setString(9, other);
        ps.setString(10, sdf.format(new java.util.Date()));
        ps.setString(11, status);
        ps.setString(12, branchId);
        ps.setString(13, departmentId);
        ps.setString(14, sbuCode);
        ps.setString(15, onlineNumber);
        ps.setString(16, projectAcct);
        ps.setString(17, projectSponsor);
        ps.setString(18, projectOwner);
        ps.setString(19, projectManager);
        projectFundBal = projectFundBal.replaceAll(",","");
        ps.setDouble(20, Double.valueOf(projectFundBal));
        projectAmtUtilized = projectAmtUtilized.replaceAll(",","");
        ps.setDouble(21, Double.valueOf(projectAmtUtilized));
        
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating Project ->" + e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
        return id;
    }

    public String createColumn(String bank, String column, String sort, String status)
    {
        Connection con;
        PreparedStatement ps;
        String id;
        String query;
        con = null;
        ps = null;
        boolean done = false;
        id = "";
        query = "INSERT INTO IA_PARAMETERS(MTID,BANK,NAME,SORT,STATUS) VALUES (?, ?, ?, ?, ?)";
        id = (new ApplicationHelper()).getGeneratedId("IA_PARAMETERS");
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        ps.setString(2, bank);
        ps.setString(3, column);
        ps.setString(4, sort);
        ps.setString(5, status);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error creating Bank Reconcilation ->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
        return id;
    }

    public String createPosition(int currId, String isoCode, String lcPosAcc, String fcPosAcc, String revalGain, String revalLoss, String createDt, 
            int userId)
    {
        Connection con;
        PreparedStatement ps;
        String id;
        String query;
        con = null;
        ps = null;
        boolean done = false;
        id = "";
        query = "INSERT INTO IA_GL_POS_ACCT(MTID,CRNCY_ID,ISO_CODE,LC_POS_ACCT,    FC_POS_ACCT,RE" +
"VAL_GAINS_ACCT,REVAL_LOSS_ACCT,CREATE_DT,USER_ID) VALUES("
;
        id = (new ApplicationHelper()).getGeneratedId("IA_PARAMETERS");
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        ps.setInt(2, currId);
        ps.setString(3, isoCode);
        ps.setString(4, lcPosAcc);
        ps.setString(5, fcPosAcc);
        ps.setString(6, revalGain);
        ps.setString(7, revalLoss);
        ps.setString(8, createDt);
        ps.setInt(9, userId);
        done = ps.executeUpdate() != -1;
    	} catch (Exception e) {
    		System.out.println((new StringBuilder()).append(" ERROR:Error Creating Position ->").append(e.getMessage()).toString());
    	} finally {
    		closeConnection(con, ps);
    	}
        return id;
    }

    public String createRevaluation(int exchId, int posId, int revalRate, String period, String freq, int trm, String posDesc, 
            String lastRevalDt, String nxtRevaDt, String createDt, String userId)
    {
        Connection con;
        PreparedStatement ps;
        String id;
        String query;
        con = null;
        ps = null;
        boolean done = false;
        id = "";
        query = "INSERT INTO MG_REV_FREQUENCY(MTID,EXCH_INDEX_ID,POS_TYPE_ID,REVAL_RATE, PERIOD,F" +
"REQUENCY,TRM,POS_TYPE_DESC,LAST_REVAL_DT,NXT_REVAL_DT,CREATE_DT,USER_ID) VALUES(" +
"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
;
        id = (new ApplicationHelper()).getGeneratedId("ST_GL_PROJECT");
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        ps.setInt(2, exchId);
        ps.setInt(3, posId);
        ps.setInt(4, revalRate);
        ps.setDate(5, dateConvert(period));
        ps.setString(6, freq);
        ps.setInt(7, trm);
        ps.setString(8, posDesc);
        ps.setDate(9, dateConvert(lastRevalDt));
        ps.setDate(10, dateConvert(nxtRevaDt));
        ps.setDate(11, dateConvert(createDt));
        ps.setString(12, userId);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error creating Revaluation ->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
        return id;
    }

    public boolean updateProject(String id, String code, String desc, String cCode, String startDt, String endDt, String cost, 
            String capital, String other, String transDt, String status,String branchId,String departmentId,String sbuCode,
            String onlineNumber,String projectAcct,String projectSponsor,String projectOwner,String projectManager,String projectFundBal,String projectAmtUtilized)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE ST_GL_PROJECT SET CODE=?,DESCRIPTION=?,CUSTOMER_CODE=?,START_DATE=?, END_" +
		"DATE=?,COST=?,CAPITAL=?,OTHERS=?,TRANS_DATE =?, STATUS = ?,BRANCH_ID = ?,DEPT_ID = ?,SBU_CODE = ?," +
		"ONLINE_NUMBER =?,PROJECT_ACCOUNT =?,PROJECT_SPONSOR =?,PROJECT_OWNER =?,PROJECT_MANAGER =?,PROJECT_BALANCE =?,"+
		"AMOUNT_UTILIZED =? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
        System.out.println(query);
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, code);
        ps.setString(2, desc);
        ps.setString(3, cCode);
        ps.setDate(4, dateConvert(startDt));
        ps.setDate(5, dateConvert(endDt));
        ps.setDouble(6, Double.valueOf(cost));
        ps.setDouble(7, Double.valueOf(capital));
        ps.setString(8, other);
        ps.setString(9, transDt);
        ps.setString(10, status);
        ps.setString(11, branchId);
        ps.setString(12, departmentId);
        ps.setString(13, sbuCode);
        ps.setString(14, onlineNumber);
        ps.setString(15, projectAcct);
        ps.setString(16, projectSponsor);
        ps.setString(17, projectOwner);
        ps.setString(18, projectManager);
        ps.setDouble(19, Double.valueOf(projectFundBal));
        ps.setDouble(20, Double.valueOf(projectAmtUtilized));
        ps.setString(21, id);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error updating Project ->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}

        return done;
    }

    public boolean updateColumn(String id, String bank, String column, String sort, String status)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE IA_PARAMETERS SET BANK=?,NAME=?,SORT=?,STATUS=? WHERE MTID=?";
        con = null;
        ps = null;
        done = false;
        System.out.println(query);
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, bank);
        ps.setString(2, column);
        ps.setString(3, sort);
        ps.setString(4, status); 
        ps.setString(5, id);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error Upadating Bank Reconsilation->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
        return done;
    }

    public boolean updatePosition(String id, int currId, String isoCode, String lcPosAcc, String fcPosAcc, String revalGain, String revalLoss, 
            String createDt, int userId)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE IA_GL_POS_ACCT SET CRNCY_ID=?,ISO_CODE=?,LC_POS_ACCT=?,FC_POS_ACCT=?, REV" +
"AL_GAINS_ACCT=?,REVAL_LOSS_ACCT=?,CREATE_DT=? WHERE MTID=?"
;
        con = null;
        ps = null;
        done = false;
        System.out.println(query);
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, currId);
        ps.setString(2, isoCode);
        ps.setString(3, lcPosAcc);
        ps.setString(4, fcPosAcc);
        ps.setString(5, revalGain);
        ps.setString(6, revalLoss);
        ps.setDate(7, dateConvert(createDt));
        ps.setInt(8, userId);
        ps.setString(9, id);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error updating Position ->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
        return done;
    }

    public boolean updateRevaluation(String id, int exchId, int posId, int revalRate, String period, String freq, int trm, 
            String posDesc, String lastRevalDt, String nxtRevaDt, String createDt, String userId)
    {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        query = "UPDATE MG_REV_FREQUENCY SET EXCH_INDEX_ID=?,POS_TYPE_ID=?,REVAL_RATE=?,PERIOD=?." +
"FREQUENCY =?,  TRM=?,POS_TYPE_DESC=?,LAST_REVAL_DT=?,NXT_REVAL_DT=?,CREATE_DT=?," +
"USER_ID=? WHERE MTID=?"
;
        con = null;
        ps = null;
        done = false;
        System.out.println(query);
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        ps.setInt(1, exchId);
        ps.setInt(2, posId);
        ps.setInt(3, revalRate);
        ps.setString(4, period);
        ps.setString(5, freq);
        ps.setInt(6, trm);
        ps.setString(7, posDesc);
        ps.setDate(8, dateConvert(lastRevalDt));
        ps.setDate(9, dateConvert(nxtRevaDt));
        ps.setDate(10, dateConvert(createDt));
        ps.setString(11, userId);
        ps.setString(12, id);
        done = ps.executeUpdate() != -1;
	} catch (Exception e) {
		System.out.println((new StringBuilder()).append(" ERROR:Error Upadating Revaluation->").append(e.getMessage()).toString());
	} finally {
		closeConnection(con, ps);
	}
        return done;
    }

    public ArrayList findProjectByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Project pro = null;
//        query = "SELECT MTID,CODE,DESCRIPTION,CUSTOMER_CODE,START_DATE, END_DATE,COST,CAPITAL,OTH" +
//"ERS,TRANS_DATE,STATUS,BRANCH_ID,DEPT_ID,SBU_CODE,ONLINE_NUMBER,PROJECT_ACCOUNT,PROJECT_SPONSOR," +
//"PROJECT_OWNER,PROJECT_MANAGER,PROJECT_BALANCE,AMOUNT_UTILIZED FROM ST_GL_PROJECT"
//;
        // query = (new StringBuilder()).append(query).append(filter).toString();
        query = "SELECT s.MTID,s.CODE,s.DESCRIPTION,s.CUSTOMER_CODE,s.START_DATE,s.END_DATE,s.TRANS_DATE,s.COST,s.CAPITAL,s.OTHERS,s.STATUS,s.BRANCH_ID,s.DEPT_ID,s.SBU_CODE," +
        		"a.PROJECT_CODE,SUM(a.COST_PRICE)/2 AS AMOUNT_UTILIZED,s.COST AS AMT_APPROVED," +
        		"(CAST(REPLACE(REPLACE(COST, ',', ''), '.', '') AS DECIMAL(18,2))/100) - SUM(a.COST_PRICE)/2 AS PROJECT_BALANCE,"+
        		"b.BRANCH_ID,b.BRANCH_CODE,b.BRANCH_NAME,st.STATUS_NAME,s.ONLINE_NUMBER,s.PROJECT_ACCOUNT,s.PROJECT_SPONSOR,s.PROJECT_OWNER,s.PROJECT_MANAGER "+
        		"FROM VENDOR_TRANSACTIONS a, ST_GL_PROJECT s,am_ad_branch b,am_ad_department d,AM_SBU_ATTACHEMENT sb, AM_GB_STATUS st "+
        		" "+filter+" AND  s.BRANCH_ID = b.BRANCH_ID AND a.PROJECT_CODE = s.CODE AND s.DEPT_ID = d.Dept_ID AND s.SBU_CODE = sb.SBU_CODE AND s.STATUS = st.STATUS_CODE AND a.VENDOR_CODE = s.CUSTOMER_CODE "+
        		"GROUP BY s.MTID,s.CODE,a.PROJECT_CODE,s.DESCRIPTION,s.CUSTOMER_CODE,s.OTHERS,a.PROJECT_CODE,s.COST,s.CAPITAL,s.STATUS,s.ONLINE_NUMBER,s.PROJECT_ACCOUNT,"+
        		"s.BRANCH_ID,s.DEPT_ID,s.SBU_CODE,b.BRANCH_ID,b.BRANCH_CODE,b.BRANCH_NAME,st.STATUS_NAME,s.START_DATE,s.TRANS_DATE,s.END_DATE,s.PROJECT_SPONSOR,s.PROJECT_OWNER,s.PROJECT_MANAGER "+
        		"UNION "+
        		"SELECT s.MTID,s.CODE,s.DESCRIPTION,s.CUSTOMER_CODE,s.START_DATE,s.END_DATE,s.TRANS_DATE,s.COST,s.CAPITAL,s.OTHERS,s.STATUS,s.BRANCH_ID,s.DEPT_ID,s.SBU_CODE," +
        		"'' AS PROJECT_CODE,0.00 AS AMOUNT_UTILIZED,s.COST AS AMT_APPROVED," +
        		"((CAST(REPLACE(REPLACE(COST, ',', ''), '.', '') AS DECIMAL(18,2))/100) - 0.00) AS PROJECT_BALANCE,"+
        		"b.BRANCH_ID,b.BRANCH_CODE,b.BRANCH_NAME,st.STATUS_NAME,s.ONLINE_NUMBER,s.PROJECT_ACCOUNT,s.PROJECT_SPONSOR,s.PROJECT_OWNER,s.PROJECT_MANAGER "+
        		"FROM ST_GL_PROJECT s,am_ad_branch b,am_ad_department d,AM_SBU_ATTACHEMENT sb, AM_GB_STATUS st "+
        		" "+filter+" AND  s.BRANCH_ID = b.BRANCH_ID AND s.DEPT_ID = d.Dept_ID AND s.SBU_CODE = sb.SBU_CODE AND s.STATUS = st.STATUS_CODE "+
        		"GROUP BY s.MTID,s.CODE,s.DESCRIPTION,s.CUSTOMER_CODE,s.OTHERS,s.COST,s.CAPITAL,s.STATUS,s.ONLINE_NUMBER,s.PROJECT_ACCOUNT,"+
        		"s.BRANCH_ID,s.DEPT_ID,s.SBU_CODE,b.BRANCH_ID,b.BRANCH_CODE,b.BRANCH_NAME,st.STATUS_NAME,s.START_DATE,s.TRANS_DATE,s.END_DATE,s.PROJECT_SPONSOR,s.PROJECT_OWNER,s.PROJECT_MANAGER";
        c = null;
        rs = null;
        s = null;
        System.out.println(query);
        try {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(pro))
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
            String branchId = rs.getString("BRANCH_ID");
            String departmentId = rs.getString("DEPT_ID");
            String sbuCode = rs.getString("SBU_CODE");
            String onlineNumber = rs.getString("ONLINE_NUMBER");
            String projectAcct = rs.getString("PROJECT_ACCOUNT");
            String projectSponsor = rs.getString("PROJECT_SPONSOR");
            String projectOwner = rs.getString("PROJECT_OWNER");
            String projectManager = rs.getString("PROJECT_MANAGER");
            String projectFundBal = rs.getString("PROJECT_BALANCE");
            String projectAmtUtilized = rs.getString("AMOUNT_UTILIZED");
            pro = new Project(id, code, desc, cCode, startDt, endDt, cost, capital, other, transDt, status);
            pro.setBranchId(branchId);
            pro.setDepartmentId(departmentId);
            pro.setSbuCode(sbuCode);
            pro.setOnlineNumber(onlineNumber); 
            pro.setProjectAcct(projectAcct);
            pro.setProjectSponsor(projectSponsor);
            pro.setProjectOwner(projectOwner); 
            pro.setProjectManager(projectManager); 
            pro.setProjectFundBal(projectFundBal); 
            pro.setProjectAmtUtilized(projectAmtUtilized); 
        }
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error Selecting  Project ->" + e.getMessage());
	} finally {
		closeConnection(c, s, rs);
	}
        return _list;
    }

    public ArrayList findColumnByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Column exc = null;
        query = "SELECT MTID,BANK,NAME,SORT,STATUS FROM IA_PARAMETERS ";
        query = (new StringBuilder()).append(query).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        System.out.println(query);
        try {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(exc))
        {
            String id = String.valueOf(rs.getInt("MTID"));
            String bank = rs.getString("BANK");
            String column = rs.getString("NAME");
            String sort = rs.getString("SORT");
            String status = rs.getString("STATUS");
            exc = new Column(id, bank, column, sort, status);
        }
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error finding Column  ->" + e.getMessage());
	} finally {
		closeConnection(c, s, rs);
	}
        return _list;
    }
/*
    public ArrayList findPositionByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Position posi = null;
        query = "SELECT MTID,CURRENCY_ID,ISO_CODE,LOCAL_POSITION,FC_POSITION,REVALUE_GAIN, REVALU" +
"E_LOSS, CREATE_DT,USER_ID FROM IA_GL_POSITION_ACCT"
;
        query = (new StringBuilder()).append(query).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        System.out.println(query);
        try {
        c = getConnection();
        s = c.createStatement();
        for(rs = s.executeQuery(query); rs.next(); _list.add(posi))
        {
            String id = String.valueOf(rs.getInt("MTID"));
            int currId = rs.getInt("CURRENCY_ID");
            String isoCode = rs.getString("ISO_CODE");
            String lcPosAcc = rs.getString("LOCAL_POSITION");
            String fcPosAcc = rs.getString("FC_POSITION");
            String revalGain = rs.getString("REVALUE_GAIN");
            String revalLoss = rs.getString("REVALUE_LOSS");
            String createDt = sdf.format(rs.getDate("CREATE_DT"));
            int userId = rs.getInt("USER_ID");
            posi = new Position(id, currId, isoCode, lcPosAcc, fcPosAcc, revalGain, revalLoss, createDt, userId);
        }
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating Project ->" + e.getMessage());
	} finally {
		closeConnection(c, s, rs);
	}
        return _list;
    }

    public ArrayList findRevaluationByQuery(String filter)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        Revaluation rev = null;
        query = "SELECT MTID,EXCH_INDEX_ID,POS_TYPE_ID,REVAL_RATE,PERIOD,FREQUENCY,TRM, POS_TYPE_" +
"DESC,LAST_REVAL_DT,NXT_REVAL_DT,CREATE_DT,USER_ID FROM MG_REV_FREQUENCY"
;
        query = (new StringBuilder()).append(query).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        System.out.println(query);
        c = getConnection();
        s = c.createStatement();
        Revaluation rev;
        for(rs = s.executeQuery(query); rs.next(); _list.add(rev))
        {
            String id = String.valueOf(rs.getInt("MTID"));
            int exchId = rs.getInt("EXCH_INDEX_ID");
            int posId = rs.getInt("POS_TYPE_ID");
            int revalRate = rs.getInt("REVAL_RATE");
            String period = rs.getString("PERIOD");
            String freq = rs.getString("FREQUENCY");
            int trm = rs.getInt("TRM");
            String posDesc = rs.getString("POS_TYPE_DESC");
            String lastRevalDt = sdf.format(rs.getDate("LAST_REVAL_DT"));
            String nxtRevaDt = sdf.format(rs.getDate("NXT_REVAL_DT"));
            String createDt = sdf.format(rs.getDate("CREATE_DT"));
            String userId = rs.getString("USER_ID");
            rev = new Revaluation(id, exchId, posId, revalRate, period, freq, trm, posDesc, lastRevalDt, nxtRevaDt, createDt, userId);
        }
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating Project ->" + e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
        closeConnection(c, s, rs);
        break MISSING_BLOCK_LABEL_333;
        Exception e;
        e;
        e.printStackTrace();
        closeConnection(c, s, rs);
        break MISSING_BLOCK_LABEL_333;
        Exception exception;
        exception;
        closeConnection(c, s, rs);
        throw exception;
        return _list;
    }
*/
    public ArrayList findAllProject()
    {
        String filter = "";
        return findProjectByQuery(filter);
    }

    public Project findProjectById(String id)
    {
        Project pro = null;
        String filter = (new StringBuilder()).append(" WHERE s.MTID = '").append(id).append("'").toString();
        ArrayList list = findProjectByQuery(filter);
        if(list.size() > 0)
        {
            pro = (Project)list.get(0);
        }
        return pro;
    }

    public Project findProjectByProjectId(String code)
    {
        Project pro = null;
        String filter = (new StringBuilder()).append(" WHERE CODE = '").append(code).append("'").toString();
        ArrayList list = findProjectByQuery(filter);
        if(list.size() > 0)
        {
            pro = (Project)list.get(0);
        }
        return pro;
    }

    public Project findProjectByProject(String currency)
    {
        String filter = (new StringBuilder()).append(" WHERE CURRENCY='").append(currency).append("'").toString();
        ArrayList _list = findProjectByQuery(filter);
        Project pro = (Project)_list.iterator().next();
        return pro;
    }

    public ArrayList findAllColumn()
    {
        String filter = "";
        return findColumnByQuery(filter);
    }

    public Column findColumnById(String id)
    {
        Column col = null;
        String filter = (new StringBuilder()).append(" WHERE MTID =").append(id).toString();
        ArrayList list = findColumnByQuery(filter);
        if(list.size() > 0)
        {
            col = (Column)list.get(0);
        }
        return col;
    }

    public Column findColumnByColumnId(String code)
    {
        Column colu = null;
        String filter = (new StringBuilder()).append(" WHERE CODE = '").append(code).append("'").toString();
        ArrayList list = findColumnByQuery(filter);
        if(list.size() > 0)
        {
            colu = (Column)list.get(0);
        }
        return colu;
    }

    public Column findColumnByBank(String bank)
    {
        String filter = (new StringBuilder()).append(" WHERE BANK='").append(bank).append("'").toString();
        ArrayList _list = findColumnByQuery(filter);
        Column colu = (Column)_list.iterator().next();
        return colu;
    }

    public Column findColumnByColumnSoleId(String bank, String sort)
    {
        Column col = null;
        String filter = (new StringBuilder()).append(" WHERE BANK = '").append(bank).append("' AND  SORT= '").append(sort).append("'").toString();
        ArrayList list = findColumnByQuery(filter);
        if(list.size() > 0)
        {
            col = (Column)list.get(0);
        }
        return col;
    }
/*
    public ArrayList findAllPosition()
    {
        String filter = "";
        return findPositionByQuery(filter);
    }

    public Position findPositionById(String id)
    {
        Position pro = null;
        String filter = (new StringBuilder()).append("WHERE MTID =").append(id).toString();
        ArrayList list = findPositionByQuery(filter);
        if(list.size() > 0)
        {
            pro = (Position)list.get(0);
        }
        return pro;
    }

    public Position findPositionByCode(String code)
    {
        Position pro = null;
        String filter = (new StringBuilder()).append(" WHERE CODE = '").append(code).append("'").toString();
        ArrayList list = findPositionByQuery(filter);
        if(list.size() > 0)
        {
            pro = (Position)list.get(0);
        }
        return pro;
    }

    public Position findPositionByPosition(String isoCode)
    {
        Position pro = null;
        String filter = (new StringBuilder()).append("WHERE ISO_CODE='").append(isoCode).append("'").toString();
        ArrayList list = findPositionByQuery(filter);
        if(list.size() > 0)
        {
            pro = (Position)list.get(0);
        }
        return pro;
    }

    public ArrayList findAllRevaluation()
    {
        String filter = "";
        return findRevaluationByQuery(filter);
    }

    public Revaluation findRevaluationById(String id)
    {
        Revaluation rev = null;
        String filter = (new StringBuilder()).append("WHERE MTID =").append(id).toString();
        ArrayList list = findRevaluationByQuery(filter);
        if(list.size() > 0)
        {
            rev = (Revaluation)list.get(0);
        }
        return rev;
    }

    public Revaluation findRevaluationByCode(int exchId)
    {
        Revaluation rev = null;
        String filter = (new StringBuilder()).append(" WHERE EXCH_INDEX_ID = '").append(exchId).append("'").toString();
        ArrayList list = findRevaluationByQuery(filter);
        if(list.size() > 0)
        {
            rev = (Revaluation)list.get(0);
        }
        return rev;
    }
    */
}
