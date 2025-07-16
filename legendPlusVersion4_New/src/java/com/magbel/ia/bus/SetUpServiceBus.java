package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;
import com.magbel.util.DatetimeFormat;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SetUpServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    SimpleDateFormat timeFormat;
    Date date;
    DatetimeFormat df;
    ApplicationHelper helper;

    public SetUpServiceBus()
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        timeFormat = new SimpleDateFormat("hh:mm");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
    }

    public void createApprovalLevel(String code, double minAmt, double maxAmt, String desc, int adjMin, 
            int adjMax, int adjCon, int concur)
    {
        String id = helper.getGeneratedId("IA_APPROVAL_LEVEL");
        String query = (new StringBuilder()).append("INSERT INTO IA_APPROVAL_LEVEL(MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT, DESCRIPTION" +
",ADJUST_MIN_AMOUNT,ADJUST_MAX_AMOUNT,ADJUST_CONCURRENCE,  CONCURRENCE) VALUES('"
).append(id).append("','").append(code).append("','").append(minAmt).append("','").append(maxAmt).append("','").append(desc).append("', ").append("'").append(adjMin).append("','").append(adjMax).append("','").append(adjCon).append("','").append(concur).append("')").toString();
        executeQuery(query);
    }

    public void createProject(String code, String desc, String startDt, String endDt, double cost, String capital, 
            String other, String transDt, String status)
    {
        String id = helper.getGeneratedId("IA_GL_PROJECT");
        String query = (new StringBuilder()).append("INSERT INTO IA_GL_PROJECT(MTID,CODE,DESCRIPTION,START_DATE, END_DATE,COST,CAPITA" +
"L,TRANS_DATE,OTHERS,STATUS) VALUES('"
).append(id).append("','").append(code).append("','").append(desc).append("','").append(startDt).append("','").append(endDt).append("', ").append("'").append(cost).append("','").append(capital).append("','").append(other).append("','").append(other).append("','").append(status).append("')").toString();
        executeQuery(query);
    }

    public void createAccount(String no)
    {
        String id = helper.getGeneratedId("IA_GB_ACCOUNT");
        String query = (new StringBuilder()).append("INSERT INTO IA_GB_ACCOUNT(MTID,ACCOUNTNO) VALUES('").append(id).append("','").append(no).append("')").toString();
        executeQuery(query);
    }

    public void createBank(String code, String name, int phone, int fax, String address, int accountNo, int bankAcct, 
            String currency, String description, String branch, String contactPerson, int personno, String status)
    {
        String id = helper.getGeneratedId("IA_BANK");
        String query = (new StringBuilder()).append("INSERT INTO IA_BANK(MTID,BANK_CODE,NAME,PHONE,FAX,ADDRESS,ACCOUNT_NO,BANK_ACCOUN" +
"T,CURRENCY,DESCRIPTION,BRANCH,CONTACT_PERSON,PERSONNO,STATUS) VALUES('"
).append(id).append("','").append(code).append("','").append(name).append("','").append(phone).append("','").append(fax).append("','").append(address).append("','").append(accountNo).append("','").append(bankAcct).append("', ").append("'").append(currency).append("','").append(description).append("','").append(branch).append("','").append(contactPerson).append("','").append(personno).append("','").append(status).append("')").toString();
        executeQuery(query);
    }

    public void createColumn(String bank, String column, String sort, String status)
    {
        String id = helper.getGeneratedId("IA_APPROVAL_LEVEL");
        String query = (new StringBuilder()).append("INSERT INTO IA_PARAMETERS( MTID,BANK,NAME,SORT,STATUS) VALUES('").append(id).append("','").append(bank).append("','").append(column).append("','").append(sort).append("','").append(status).append("')").toString();
        executeQuery(query);
    }

    public void createPosition(int currId, String isoCode, String fcPosAcc, String lcPosAcc, String revalGain, String revalLoss, String createDt, 
            int userId)
    {
        String id = helper.getGeneratedId("IA_GL_POS_ACCT");
        String query = (new StringBuilder()).append("INSERT INTO IA_GL_POS_ACCT(CRNCY_ID,ISO_CODE,FC_POS_ACCT,    LC_POS_ACCT,REVAL_G" +
"AINS_ACCT,REVAL_LOSS_ACCT,CREATE_DT,USER_ID) VALUES('"
).append(currId).append("','").append(isoCode).append("','").append(fcPosAcc).append("','").append(lcPosAcc).append("','").append(revalGain).append("', ").append("'").append(revalLoss).append("',").append(dateConvert(createDt)).append(",'").append(userId).append("')").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void createRevaluation(int exchId, int posId, int revalRate, String period, String freq, int trm, String posDesc, 
            String lastRevalDt, String nxtRevaDt, String createDt, String userId)
    {
        String id = helper.getGeneratedId("MG_REV_FREQUENCY");
        String query = (new StringBuilder()).append("INSERT INTO MG_REV_FREQUENCY(MTID,EXCH_INDEX_ID,POS_TYPE_ID,REVAL_RATE, PERIOD,F" +
"REQUENCY,TRM,POS_TYPE_DESC,LAST_REVAL_DT,NXT_REVAL_DT,CREATE_DT,USER_ID) VALUES(" +
"'"
).append(id).append("','").append(exchId).append("','").append(posId).append("','").append(revalRate).append("','").append(period).append("','").append(freq).append("','").append(trm).append("', ").append("'").append(posDesc).append("','").append(dateConvert(lastRevalDt)).append("','").append(dateConvert(nxtRevaDt)).append("','").append(dateConvert(createDt)).append("','").append(userId).append("')").toString();
        executeQuery(query);
    }

    public void updateApprovalLevel(String id, String code, double minAmt, double maxAmt, String desc, 
            int adjMin, int adjMax, int adjCon, int concur)
    {
        String query = (new StringBuilder()).append("UPDATE IA_APPROVAL_LEVEL SET LEVEL_CODE = '").append(code).append("', ").append("MIN_AMOUNT = '").append(minAmt).append("',MAX_AMOUNT = '").append(maxAmt).append("',DESCRIPTION = '").append(desc).append("', ").append("ADJUST_MIN_AMOUNT = '").append(adjMin).append("',ADJUST_MAX_AMOUNT = '").append(adjMax).append("', ").append("ADJUST_CONCURRENCE = '").append(adjCon).append("',CONCURRENCE = '").append(concur).append("' ").append(" WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updateProject(String id, String code, String desc, String startDt, String endDt, double cost, 
            String capital, String other, String transDt, String status)
    {
        String query = (new StringBuilder()).append("UPDATE IA_GL_PROJECT SET CODE = '").append(code).append("', ").append("DESCRIPTION = '").append(desc).append("',START_DATE = '").append(startDt).append("',END_DATE = '").append(endDt).append("', ").append("COST = '").append(cost).append("',CAPITAL = '").append(capital).append("', ").append("OTHERS = '").append(other).append("',TRANS_DATE = '").append(transDt).append("',STATUS = '").append(status).append("' ").append(" WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updateAccount(String id, String no)
    {
        String query = (new StringBuilder()).append("UPDATE IA_GL_PROJECT SET ACCOUNTNO = '").append(no).append("', ").append(" WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updateBank(String id, String code, String name, int phone, int fax, String address, int accountNo, 
            int bankAcct, String currency, String description, String branch, String contactPerson, int personno, String status)
    {
        String query = (new StringBuilder()).append("UPDATE IA_BANK SET BANK_CODE = '").append(code).append("',NAME = '").append(name).append("', ").append("PHONE = '").append(phone).append("',FAX = '").append(fax).append("',ADDRESS = '").append(address).append("', ").append("ACCOUNT_NO = '").append(accountNo).append("',BANK_ACCOUNT = '").append(bankAcct).append("',CURRENCY = '").append(currency).append("', ").append("DESCRIPTION = '").append(description).append("',BRANCH = '").append(branch).append("',CONTACT_PERSON = '").append(contactPerson).append("', ").append("PERSONNO = '").append(personno).append("',STATUS = '").append(status).append("' ").append("WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updateColumn(String id, String bank, String column, String sort, String status)
    {
        String query = (new StringBuilder()).append("UPDATE IA_PARAMETERS SET BANK = '").append(bank).append("',NAME = '").append(column).append("', ").append("SORT = '").append(sort).append("',STATUS = '").append(status).append("' WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updatePosition(int currId, String isoCode, String fcPosAcc, String lcPosAcc, String revalGain, String revalLoss, String createDt, 
            int userId, String id)
    {
        String query = (new StringBuilder()).append("UPDATE IA_GL_POS_ACCT SET CRNCY_ID = '").append(currId).append("',ISO_CODE = '").append(isoCode).append("', ").append("FC_POS_ACCT = '").append(fcPosAcc).append("',LC_POS_ACCT = '").append(lcPosAcc).append("', ").append("REVAL_GAINS_ACCT = '").append(revalGain).append("',REVAL_LOSS_ACCT = '").append(revalLoss).append("', ").append("CREATE_DT = '").append(createDt).append("',USER_ID = '").append(userId).append("' WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public void updateRevaluation(String id, int exchId, int posId, int revalRate, String period, String freq, int trm, 
            String posDesc, String lastRevalDt, String nxtRevaDt, String createDt, String userId)
    {
        String query = (new StringBuilder()).append("UPDATE MG_REV_FREQUENCY SET EXCH_INDEX_ID = '").append(exchId).append("'").append("POS_TYPE_ID = '").append(posId).append("',REVAL_RATE = '").append(revalRate).append("',PERIOD = '").append(period).append("',FREQUENCY = '").append(freq).append("', ").append("TRM = '").append(trm).append("',POS_TYPE_DESC = '").append(posDesc).append("',LAST_REVAL_DT = '").append(lastRevalDt).append("',  ").append("NXT_REVAL_DT = '").append(nxtRevaDt).append("',CREATE_DT = '").append(createDt).append("',USER_ID = '").append(userId).append("' ").append(" WHERE MTID = '").append(id).append("'").toString();
        System.out.println(query);
        executeQuery(query);
    }

    public ArrayList findAllApprovalLevel()
    {
        String filter = "";
        return findApprovalLevelByQuery(filter);
    }

    public ArrayList findApprovalLevelByMinAmount(String minAmt)
    {
        String filter = (new StringBuilder()).append(" WHERE MIN_AMOUNT = '").append(minAmt).append("'").toString();
        return findApprovalLevelByQuery(filter);
    }

    public ApprovalLevel findApprovalLevelById(String id)
    {
        ApprovalLevel approval = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findApprovalLevelByQuery(filter);
        if(list.size() > 0)
        {
            approval = (ApprovalLevel)list.get(0);
        }
        return approval;
    }

    public ApprovalLevel findApprovalLevelByApprovalLevelId(String code)
    {
        ApprovalLevel approval = null;
        String filter = (new StringBuilder()).append(" WHERE LEVEL_CODE = '").append(code).append("'").toString();
        ArrayList list = findApprovalLevelByQuery(filter);
        if(list.size() > 0)
        {
            approval = (ApprovalLevel)list.get(0);
        }
        return approval;
    }

    public ArrayList findAllProject()
    {
        String filter = "";
        return findProjectByQuery(filter);
    }

    public ArrayList getProjectByStatus()
    {
        String filter = "";
        return findProjectByQuery(filter);
    }

    public ArrayList findProjectByCost(String cost)
    {
        String filter = (new StringBuilder()).append(" WHERE COST = '").append(cost).append("'").toString();
        return findProjectByQuery(filter);
    }

    public Project findProjectById(String id)
    {
        Project proj = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findProjectByQuery(filter);
        if(list.size() > 0)
        {
            proj = (Project)list.get(0);
        }
        return proj;
    }

    public Project findProjectByProjectId(String code)
    {
        Project proj = null;
        String filter = (new StringBuilder()).append(" WHERE CODE = '").append(code).append("'").toString();
        ArrayList list = findProjectByQuery(filter);
        if(list.size() > 0)
        {
            proj = (Project)list.get(0);
        }
        return proj;
    }

    public ArrayList findAllBank()
    {
        String filter = "";
        return findBankByQuery(filter);
    }

    public ArrayList findBankByCost(String cost)
    {
        String filter = (new StringBuilder()).append(" WHERE COST = '").append(cost).append("'").toString();
        return findBankByQuery(filter);
    }

    public Bank findBankById(String id)
    {
        Bank bank = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findBankByQuery(filter);
        if(list.size() > 0)
        {
            bank = (Bank)list.get(0);
        }
        return bank;
    }

    public Bank findBankByBankId(String code)
    {
        Bank bank = null;
        String filter = (new StringBuilder()).append(" WHERE BANK_CODE = '").append(code).append("'").toString();
        ArrayList list = findBankByQuery(filter);
        if(list.size() > 0)
        {
            bank = (Bank)list.get(0);
        }
        return bank;
    }

    public ArrayList findAllColumn()
    {
        String filter = "";
        return findColumnByQuery(filter);
    }

    public ArrayList findAllColumnBank()
    {
        String filter = "";
        return findColumnByBankQuery(filter);
    }

    public ArrayList findColumnByBank(String bank)
    {
        String filter = (new StringBuilder()).append(" WHERE BANK = '").append(bank).append("'").toString();
        return findColumnByQuery(filter);
    }

    public Column findColumnById(String id)
    {
        Column col = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findColumnByQuery(filter);
        if(list.size() > 0)
        {
            col = (Column)list.get(0);
        }
        return col;
    }

    public Column findColumnByColumnId(String sort, String bank)
    {
        Column col = null;
        String filter = (new StringBuilder()).append(" WHERE SORT = '").append(sort).append("' AND BANK = '").append(bank).append("'").toString();
        ArrayList list = findColumnByQuery(filter);
        if(list.size() > 0)
        {
            col = (Column)list.get(0);
        }
        return col;
    }

    public ArrayList findAllAccount()
    {
        String filter = "";
        return findAccountByQuery(filter);
    }

    public Account findAccountById(String id)
    {
        Account account = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findAccountByQuery(filter);
        if(list.size() > 0)
        {
            account = (Account)list.get(0);
        }
        return account;
    }

    public ArrayList findAllPosition()
    {
        String filter = "";
        return findPositionByQuery(filter);
    }

    public ArrayList getPositionByListen()
    {
        String filter = "";
        return findPositionByQuery(filter);
    }

    public Position findPositionById(String currId)
    {
        Position posi = null;
        String filter = (new StringBuilder()).append(" WHERE crncy_id = '").append(currId).append("'").toString();
        ArrayList list = findPositionByQuery(filter);
        if(list.size() > 0)
        {
            posi = (Position)list.get(0);
        }
        return posi;
    }

    public Position findPositionByPositionISO(String isoCode)
    {
        Position posi = null;
        String filter = (new StringBuilder()).append(" WHERE ISO_CODE = '").append(isoCode).append("'").toString();
        ArrayList list = findPositionByQuery(filter);
        if(list.size() > 0)
        {
            posi = (Position)list.get(0);
        }
        return posi;
    }

    public ArrayList findAllRevaluation()
    {
        String filter = "";
        return findRevaluationByQuery(filter);
    }

    public ArrayList findRevaluationByRevalRate(String revalRate)
    {
        String filter = (new StringBuilder()).append(" WHERE  REVAL_RATE= '").append(revalRate).append("'").toString();
        return findRevaluationByQuery(filter);
    }

    public ArrayList findRevaluationByPeriod(String period)
    {
        String filter = (new StringBuilder()).append(" WHERE  PERIOD = '").append(period).append("'").toString();
        return findRevaluationByQuery(filter);
    }

    public Revaluation findRevaluationById(String id)
    {
        Revaluation reval = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("'").toString();
        ArrayList list = findRevaluationByQuery(filter);
        if(list.size() > 0)
        {
            reval = (Revaluation)list.get(0);
        }
        return reval;
    }

    public Revaluation findRevaluationByExchangeId(String exchId)
    {
        Revaluation reval = null;
        String filter = (new StringBuilder()).append(" WHERE EXCH_INDEX_ID = '").append(exchId).append("'").toString();
        ArrayList list = findRevaluationByQuery(filter);
        if(list.size() > 0)
        {
            reval = (Revaluation)list.get(0);
        }
        return reval;
    }

    public ArrayList findApprovalLevelByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT, DESCRIPTION,ADJUST_MIN_AMOUNT,ADJU" +
"ST_MAX_AMOUNT,ADJUST_CONCURRENCE,CONCURRENCE FROM IA_APPROVAL_LEVEL "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        ApprovalLevel approval;
        for(rs = s.executeQuery(query); rs.next(); records.add(approval))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("LEVEL_CODE");
            double minAmt = rs.getDouble("MIN_AMOUNT");
            double maxAmt = rs.getDouble("MAX_AMOUNT");
            String desc = rs.getString("DESCRIPTION");
            int adjMin = rs.getInt("ADJUST_MIN_AMOUNT");
            int adjMax = rs.getInt("ADJUST_MAX_AMOUNT");
            int adjCon = rs.getInt("ADJUST_CONCURRENCE");
            int concur = rs.getInt("CONCURRENCE");
            approval = new ApprovalLevel(id, code, minAmt, maxAmt, desc, adjMin, adjMax, adjCon, concur);
        }

        closeConnection(c, s, rs);
    }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Approval Level ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
    }finally{
    	closeConnection(c, s, rs);
	   }
        return records;
    }

    public ArrayList findProjectByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,CODE,DESCRIPTION,START_DATE, END_DATE,COST,CAPITAL,OTHERS,TRANS_DATE" +
",STATUS FROM IA_GL_PROJECT "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Project posi;
        for(rs = s.executeQuery(query); rs.next(); records.add(posi))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String desc = rs.getString("DESCRIPTION");
            String startDt = sdf.format(rs.getDate("START_DATE"));
            Date end = rs.getDate("END_DATE");
            String endDt = sdf.format(end);
            double cost = rs.getDouble("COST");
            String capital = rs.getString("CAPITAL");
            String other = rs.getString("OTHERS");
            String transDt = sdf.format(rs.getDate("TRANS_DATE"));
            String status = rs.getString("STATUS");
            posi = new Project(id, code, desc, code, startDt, endDt, String.valueOf(cost), capital, other, transDt, status);

        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Projects ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList getProjectByStatus(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT * FROM IA_GL_PROJECT WHERE STATUS = 'A' ORDER BY CODE ASC'").append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Project proj;
        for(rs = s.executeQuery(query); rs.next(); records.add(proj))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String desc = rs.getString("DESCRIPTION");
            String startDt = rs.getString("START_DATE");
            String endDt = rs.getString("END_DATE");
            double cost = rs.getDouble("COST");
            String capital = rs.getString("CAPITAL");
            String other = rs.getString("OTHERS");
            String transDt = sdf.format(rs.getDate("TRANS_DATE"));
            String status = rs.getString("STATUS");
            proj = new Project(id, code, desc, code, startDt, endDt, String.valueOf(cost), capital, other, transDt, status);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Projects ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findBankByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,BANK_CODE,NAME,PHONE,FAX,ADDRESS,ACCOUNT_NO,BANK_ACCOUNT,CURRENCY,DE" +
"SCRIPTION,BRANCH,CONTACT_PERSON,PERSONNO,STATUS FROM IA_BANK "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Banks bank;
        for(rs = s.executeQuery(query); rs.next(); records.add(bank))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("BANK_CODE");
            String name = rs.getString("NAME");
            int phone = rs.getInt("PHONE");
            int fax = rs.getInt("FAX");
            String address = rs.getString("ADDRESS");
            int accountNo = rs.getInt("ACCOUNT_NO");
            int bankAcct = rs.getInt("BANK_ACCOUNT");
            String currency = rs.getString("CURRENCY");
            String description = rs.getString("DESCRIPTION");
            String branch = rs.getString("BRANCH");
            String contactPerson = rs.getString("CONTACT_PERSON");
            int personno = rs.getInt("PERSONNO");
            String status = rs.getString("STATUS");
            bank = new Banks(id, code, name, phone, fax, address, accountNo, bankAcct, currency, description, branch, contactPerson, personno, status);
        }

        closeConnection(c, s, rs);
    	}catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Banks ->").append(e.getMessage()).toString());
        e.printStackTrace();
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findColumnByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,BANK,NAME,SORT,STATUS FROM IA_PARAMETERS ").append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Column col;
        for(rs = s.executeQuery(query); rs.next(); records.add(col))
        {
            String id = rs.getString("MTID");
            String bank = rs.getString("BANK");
            String column = rs.getString("NAME");
            String sort = rs.getString("SORT");
            String status = rs.getString("STATUS");
            col = new Column(id, bank, column, sort, status);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Columns ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findColumnByBankQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,NAME,BANK,STATUS,SORT FROM IA_PARAMETERS WHERE  BANK = ? ORDER BY SO" +
"RT "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Column col;
        for(rs = s.executeQuery(query); rs.next(); records.add(col))
        {
            String id = rs.getString("MTID");
            String bank = rs.getString("BANK");
            String column = rs.getString("NAME");
            String sort = rs.getString("SORT");
            String status = rs.getString("STATUS");
            col = new Column(id, bank, column, sort, status);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Columns ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findAccountByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,ACCOUNT_NO FROM IA_GB_ACCOUNT ").append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Account account;
        for(rs = s.executeQuery(query); rs.next(); records.add(account))
        {
            String id = rs.getString("MTID");
            String no = rs.getString("ACCOUNT_NO");
            account = new Account(id, no);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Account  ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findPositionByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,CRNCY_ID,ISO_CODE,FC_POS_ACCT,LC_POS_ACCT, REVAL_GAINS_ACCT,REVAL_LO" +
"SS_ACCT,CREATE_DT,USER_ID  FROM IA_GL_POS_ACCT "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Position posi;
        for(rs = s.executeQuery(query); rs.next(); records.add(posi))
        {
            String id = rs.getString("MTID");
            int currId = rs.getInt("CRNCY_ID");
            String isoCode = rs.getString("ISO_CODE");
            String fcPosAcc = rs.getString("FC_POS_ACCT");
            String lcPosAcc = rs.getString("LC_POS_ACCT");
            String revalGain = rs.getString("REVAL_GAINS_ACCT");
            String revalLoss = rs.getString("REVAL_LOSS_ACCT");
            String createDt = sdf.format(rs.getDate("CREATE_DT"));
            int userId = rs.getInt("USER_ID");
            posi = new Position(id, isoCode, currId, isoCode, fcPosAcc, lcPosAcc, revalGain, revalLoss, createDt, userId);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selectings Positions ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList getPositionByListen(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT * FROM IA_GB_CURRENCY_CODE WHERE STATUS = 'A' AND LOCAL_CURRENCY != 'Y'  " +
" AND ISO_CODE NOT IN (SELECT ISO_CODE FROM IA_GL_POS_ACCT) ORDER BY CURRENCY_ID " +
"ASC'"
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Position posi;
        for(rs = s.executeQuery(query); rs.next(); records.add(posi))
        {
            String id = rs.getString("MTID");
            int currId = rs.getInt("CRNCY_ID");
            String isoCode = rs.getString("ISO_CODE");
            String fcPosAcc = rs.getString("FC_POS_ACCT");
            String lcPosAcc = rs.getString("LC_POS_ACCT");
            String revalGain = rs.getString("REVAL_GAINS_ACCT");
            String revalLoss = rs.getString("REVAL_LOSS_ACCT");
            String createDt = sdf.format(rs.getDate("CREATE_DT"));
            int userId = rs.getInt("USER_ID");
            posi = new Position(id, isoCode,currId, isoCode, fcPosAcc, lcPosAcc, revalGain, revalLoss, createDt, userId);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Positions ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }

    public ArrayList findRevaluationByQuery(String filter)
    {
        ArrayList records;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        records = new ArrayList();
        query = (new StringBuilder()).append("SELECT MTID,EXCH_INDEX_ID,POS_TYPE_ID,REVAL_RATE,PERIOD,FREQUENCY,TRM,POS_TYPE_D" +
"ESC, LAST_REVAL_DT,NXT_REVAL_DT,CREATE_DT,USER_ID FROM MG_REV_FREQUENCY "
).append(filter).toString();
        c = null;
        rs = null;
        s = null;
        try{
        c = getConnection();
        s = c.createStatement();
        Revaluation reval;
        for(rs = s.executeQuery(query); rs.next(); records.add(reval))
        {
            String id = rs.getString("MTID");
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
            reval = new Revaluation(id, exchId, posId, revalRate, period, freq, trm, posDesc, lastRevalDt, nxtRevaDt, createDt, userId);
        }

        closeConnection(c, s, rs);
        }catch(Exception e){
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Revaluation Level ->").append(e.getMessage()).toString());
        closeConnection(c, s, rs);
	    }finally{
	    	closeConnection(c, s, rs);
		   }
        return records;
    }
}
