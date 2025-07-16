package com.magbel.ia.bus;
import java.text.SimpleDateFormat;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.util.*;

import java.util.Date;

import com.magbel.ia.util.ApplicationHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.magbel.ia.vao.*;
public class GLAccountServiceBus extends PersistenceServiceDAO
{

    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date date;
    CurrentDateTime cdf;
    DatetimeFormat df;
    ApplicationHelper helper;

    public GLAccountServiceBus()
    {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        helper = new ApplicationHelper();
    }

    public int modulus(String startDate)
    {
        String GET_DATE;
        Connection c;
        ResultSet rs;
        Statement s;
        String endDate;
        GET_DATE = "SELECT min(START_DATE) AS minDate FROM IA_CALENDAR_DATE ";
        c = null;
        rs = null;
        s = null;
        endDate = null;
        try{
        c = getConnection();
        s = c.createStatement();
        
        for(rs = s.executeQuery(GET_DATE); rs.next(); System.out.println((new StringBuilder()).append("The minDate is").append(endDate).toString()))
        {
            endDate = sdf.format(rs.getDate("minDate"));
        }

        closeConnection(c, s, rs);
    	} catch (Exception ex) {
      //  done = false;
        System.out.println((new StringBuilder()).append("ERROR:in MODULUS() ->").append(ex.getMessage()).toString());
        closeConnection(c, s, rs);
    	 } finally {
        closeConnection(c, s, rs);
    	 }
        if(endDate == null)
        {
            endDate = startDate;
        }
        int date1 = (int)Math.ceil((double)df.getDayDifference(startDate, endDate) / 30D);
        System.out.println((new StringBuilder()).append("Date1! is   ").append(date1).toString());
        if(date1 != 0)
        {
            date1 %= 3;
        } else
        {
            date1 = -1;
        }
        return date1;
    }

    public String tranCodeDRCR(String tranCode)
    {
        String GET_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String tranDescp;
        ArrayList records = new ArrayList();
        GET_QUERY = "SELECT (DEBIT_CREDIT) FROM IA_AD_TRAN_CODE WHERE TRAN_CODE =? ";
        con = null;
        ps = null;
        rs = null;
        tranDescp = "";
        try {
        con = getConnection();
        ps = con.prepareStatement(GET_QUERY);
        ps.setString(1, tranCode);
        for(rs = ps.executeQuery(); rs.next();)
        {
            tranDescp = rs.getString("DEBIT_CREDIT");
        }

        closeConnection(con, ps, rs);
    	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error returning tranCodeDRCR...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    	}
    	 finally {
        closeConnection(con, ps, rs);
    	}
        return tranDescp;
    }

    public String radioBut(String ledgerType)
    {
        String GET_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String ledNo;
        ArrayList records = new ArrayList();
        GET_QUERY = "SELECT (LEDGER_NO) FROM IA_GL_TOTALING_ACCOUNT WHERE LEDGER_TYPE =? ";
        con = null;
        ps = null;
        rs = null;
        ledNo = "";
        try{
        con = getConnection();
        ps = con.prepareStatement(GET_QUERY);
        ps.setString(1, ledgerType);
        for(rs = ps.executeQuery(); rs.next();)
        {
            ledNo = rs.getString("LEDGER_NO");
        }

        closeConnection(con, ps, rs);
    	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error returning radioBut...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    	}
        finally {
        closeConnection(con, ps, rs);
        }
        return ledNo;
    }

    public boolean createAmountType(String amountCode, int userId, String description, String type)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO AMOUNT_TYPE (MTID,AMOUNT_CODE,USER_ID,DESCRIPTION,TYPE) VALUES(?,?,?" +
",?,?)"
;
        con = null;
        ps = null;
        done = false;
        try{
        id = helper.getGeneratedId("AMOUNT_TYPE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, amountCode);
        ps.setInt(3, userId);
        ps.setString(4, description);
        ps.setString(5, type);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Amount Type... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    	}
        finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateAmountType(String id, String amountCode, int userId, String description, String type)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE AMOUNT_TYPE SET AMOUNT_CODE=?,USER_ID=?,DESCRIPTION=?, TYPE=?  WHERE MTID" +
"=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, amountCode);
        ps.setInt(2, userId);
        ps.setString(3, description);
        ps.setString(4, type);
        ps.setString(5, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Amount Type... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    	}
        finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList findAllAmountType()
    {
        String filter = "";
        return findAmountTypeByQuery(filter);
    }

    public AmountType findAmountTypeById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findAmountTypeByQuery(filter);
        return records.size() <= 0 ? null : (AmountType)records.get(0);
    }

    public AmountType findAmountTypeByATypeId(String amountCode)
    {
        AmountType at = null;
        String filter = (new StringBuilder()).append(" AND AMOUNT_CODE = '").append(amountCode).append("'").toString();
        ArrayList list = findAmountTypeByQuery(filter);
        if(list.size() > 0)
        {
            at = (AmountType)list.get(0);
        }
        return at;
    }

    public ArrayList findAmountTypeByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,AMOUNT_CODE,USER_ID,DESCRIPTION,TYPE FROM AMOUNT_TYPE  WHERE MTID IS" +
" NOT NULL "
).append(filter).append(" ORDER BY AMOUNT_CODE").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        AmountType at;
        for(rs = ps.executeQuery(); rs.next(); records.add(at))
        {
            String id = rs.getString("MTID");
            String amountCode = rs.getString("AMOUNT_CODE");
            int userId = 0;
            String description = rs.getString("DESCRIPTION");
            String type = rs.getString("TYPE");
            at = new AmountType(id, amountCode, userId, description, type);
        }

        closeConnection(con, ps, rs);
	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting Amount Type ...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
	}
        finally {
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public boolean createTotalingLevel(String accountType, String ledgerNo, String position, String description, String effectiveDate, String totalingLevel, String totalingLedgerNo, 
            String ledgerType, String debitCredit, String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO ia_gl_Totaling_Level (MTID,ACCOUNT_TYPE,LEDGER_NO,POSITION,DESCRIPTI" +
"ON,EFFECTIVE_DT, TOTALING_LEVEL,TOTALING_LEDGER_NO,LEDGER_TYPE,DEBIT_CREDIT,STAT" +
"US) VALUES(?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        try {
        id = helper.getGeneratedId("ia_gl_Totaling_Level");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, accountType);
        ps.setString(3, ledgerNo);
        ps.setString(4, position);
        ps.setString(5, description);
        ps.setDate(6, dateConvert(effectiveDate));
        ps.setString(7, totalingLevel);
        ps.setString(8, totalingLedgerNo);
        ps.setString(9, ledgerType);
        ps.setString(10, debitCredit);
        ps.setString(11, status);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating GLTotaling... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
	}   finally {
        closeConnection(con, ps);
	}
        return done;
    }

    public boolean updateTotalingLevel(String id, String accountType, String ledgerNo, String position, String description, String effectiveDate, String totalingLevel, 
            String totalingLedgerNo, String ledgerType, String debitCredit, String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE ia_gl_Totaling_Level SET ACCOUNT_TYPE=?,LEDGER_NO=?,POSITION=?,DESCRIPTIO" +
"N=?,EFFECTIVE_DT=?, TOTALING_LEVEL=?,TOTALING_LEDGER_NO=?,LEDGER_TYPE=?,DEBIT_CR" +
"EDIT=?, STATUS=?  WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, accountType);
        ps.setString(2, ledgerType);
        ps.setString(3, position);
        ps.setString(4, description);
        ps.setDate(5, dateConvert(effectiveDate));
        ps.setString(6, totalingLevel);
        ps.setString(7, totalingLedgerNo);
        ps.setString(8, ledgerType);
        ps.setString(9, debitCredit);
        ps.setString(10, status);
        ps.setString(11, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLTotaling... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createGLAccountLedger(String accountType, String accountNo, String mask, String levelNo, String ledgerNo, String description, String totalingLevel, 
            String totalingLedgerNo, String localTax, String stateTax, String fedTax, String debitCredit, String currency, String effectiveDate, 
            String status, String autoReplicate, String reconAccount, String ledgerType, String drNAllowed, String crNAllowed, String totalIndicator, 
            String userId, String parentId)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        String ledgerId;
        System.out.println("=====localTax====>>"+localTax +"  ===stateTax==>> "+stateTax+"===fedTax=====>> "+fedTax);
        CREATE_QUERY = "INSERT INTO IA_GL_ACCT_LEDGER (MTID,ACCT_TYPE,GL_ACCT_NO,MASK,LEVEL_NO,LEDGER_NO" +
",DESCRIPTION,TOTAL_ACCT_LEVEL, TOTAL_LEDGER_NO,LOCAL_TAX,STATE_TAX,FEDERAL_TAX,D" +
"EBIT_CREDIT,CURRENCY,EFFECTIVE_DT, STATUS,AUTO_REPLICATE,RECON_ACCT,LEDGER_TYPE," +
"DEBIT_NOTALLOWED,CREDIT_NOTALLOWED, TOTAL_INDICATOR,USER_ID,LEDGER_ID,PARENT_ID," +
"CREATE_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_GL_ACCT_LEDGER");
        ledgerId = id;
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, accountType);
        ps.setString(3, accountNo);
        ps.setString(4, mask);
        ps.setString(5, levelNo);
        ps.setString(6, ledgerNo);
        ps.setString(7, description);
        ps.setString(8, totalingLevel);
        ps.setString(9, totalingLedgerNo);
        if(localTax==null){localTax = "N";}
        ps.setString(10, localTax);
        if(stateTax==null){stateTax = "N";}        
        ps.setString(11, stateTax);
        if(fedTax==null){fedTax = "N";}           
        ps.setString(12, fedTax);
        ps.setString(13, debitCredit);
        ps.setString(14, currency);
        ps.setDate(15, dateConvert(effectiveDate));
        ps.setString(16, status);
        ps.setString(17, autoReplicate);
        ps.setString(18, reconAccount);
        ps.setString(19, ledgerType);
        ps.setString(20, drNAllowed);
        ps.setString(21, crNAllowed);
        ps.setString(22, totalIndicator);
        ps.setString(23, userId);
        ps.setString(24, ledgerId);
        ps.setString(25, parentId);
        ps.setDate(26, df.dateConvert(new Date()));
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("value of id is ").append(id).toString());
        System.out.println((new StringBuilder()).append("Error creating GLAccountLedger... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }

    public boolean updateGLAccountLedger(String id, String accountType, String accountNo, String mask, String levelNo, String ledgerNo, String description, 
            String totalingLevel, String totalingLedgerNo, String localTax, String stateTax, String fedTax, String debitCredit, String currency, 
            String effectiveDate, String status, String autoReplicate, String reconAccount, String ledgerType, String drNAllowed, String crNAllowed, 
            String totalIndicator, String userId, String parentId)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_GL_ACCT_LEDGER SET ACCT_TYPE=?,GL_ACCT_NO=?,MASK=?,LEVEL_NO=?,LEDGER_N" +
"O=?,DESCRIPTION=?, TOTAL_ACCT_LEVEL=?,TOTAL_LEDGER_NO=?,LOCAL_TAX=?,STATE_TAX=?," +
"FEDERAL_TAX=?, CURRENCY=?,EFFECTIVE_DT=?,STATUS=?,AUTO_REPLICATE=?, RECON_ACCT=?" +
",LEDGER_TYPE=?,DEBIT_NOTALLOWED=?,CREDIT_NOTALLOWED=?, TOTAL_INDICATOR=?, USER_I" +
"D=? WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, accountType);
        ps.setString(2, accountNo);
        ps.setString(3, mask);
        ps.setString(4, levelNo);
        ps.setString(5, ledgerNo);
        ps.setString(6, description);
        ps.setString(7, totalingLevel);
        ps.setString(8, totalingLedgerNo);
        ps.setString(9, localTax);
        ps.setString(10, stateTax);
        ps.setString(11, fedTax);
        ps.setString(12, currency);
        ps.setDate(13, df.dateConvert(effectiveDate));
        ps.setString(14, status);
        ps.setString(15, autoReplicate);
        ps.setString(16, reconAccount);
        ps.setString(17, ledgerType);
        ps.setString(18, drNAllowed);
        ps.setString(19, crNAllowed);
        ps.setString(20, totalIndicator);
        ps.setString(21, userId);
        ps.setString(22, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLAccountLedger... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }

    public boolean createGLTotaling(String accountType, String ledgerNo, String levelNo, String description, String effectiveDate, String totalingLevel, String totalingLedgerNo, 
            String ledgerType, String debitCredit, String status, int userId, String createDate,String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO ia_gl_Totaling_Account (MTID,ACCOUNT_TYPE,LEDGER_NO,LEVEL_NO,DESCRIP" +
"TION,EFFECTIVE_DT, TOTALING_LEVEL,TOTALING_LEDGER_NO,LEDGER_TYPE,DEBIT_CREDIT,ST" +
"ATUS,USER_ID,CREATE_DT,COMP_CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("ia_gl_Totaling_Account");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, accountType);
        ps.setString(3, ledgerNo);
        ps.setString(4, levelNo);
        ps.setString(5, description);
        ps.setDate(6, dateConvert(effectiveDate));
        ps.setString(7, totalingLevel);
        ps.setString(8, totalingLedgerNo);
        ps.setString(9, ledgerType);
        ps.setString(10, debitCredit);
        ps.setString(11, status);
        ps.setInt(12, userId);
        ps.setDate(13, dateConvert(createDate));
        ps.setString(14, companyCode);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating GLTotaling... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateGLTotaling(String id, String accountType, String ledgerNo, String levelNo, String description, String effectiveDate, String totalingLevel, 
            String totalingLedgerNo, String ledgerType, String debitCredit, String status, int userId, String createDate)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE ia_gl_Totaling_Account SET ACCOUNT_TYPE=?,LEDGER_NO=?,LEVEL_NO=?,DESCRIPT" +
"ION=?,EFFECTIVE_DT=?, TOTALING_LEVEL=?,TOTALING_LEDGER_NO=?,LEDGER_TYPE=?,DEBIT_" +
"CREDIT=?, STATUS=?,USER_ID=?,CREATE_DT=?  WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, accountType);
        ps.setString(2, ledgerType);
        ps.setString(3, levelNo);
        ps.setString(4, description);
        ps.setDate(5, dateConvert(effectiveDate));
        ps.setString(6, totalingLevel);
        ps.setString(7, totalingLedgerNo);
        ps.setString(8, ledgerType);
        ps.setString(9, debitCredit);
        ps.setString(10, status);
        ps.setInt(11, userId);
        ps.setDate(12, dateConvert(createDate));
        ps.setString(13, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLTotaling... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createLedgerType(String ledgerCode, String position, String ledgerType, String startNo, String parentId, String child, String debitCredit)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_LEDGER_TYPE (MTID,LEDGER_CODE,POSITION,LEDGER_TYPE,START_NO,PAREN" +
"T_ID,CHILD,DEBIT_CREDIT) VALUES(?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_LEDGER_TYPE");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, ledgerCode);
        ps.setString(3, position);
        ps.setString(4, ledgerType);
        ps.setString(5, startNo);
        ps.setString(6, parentId);
        ps.setString(7, child);
        ps.setString(8, debitCredit);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Ledger Type... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public void deleteLedgerType(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_LEDGER_TYPE  WHERE MTID = ?";
        con = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Deleting Ledger Type... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
    }

    public boolean updateLedgerType(String id, String ledgerCode, String position, String ledgerType, String startNo, String parentId, String child, 
            String debitCredit)
    {
        String UPDATE_QUERY;
        String UPDATE_Q;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_LEDGER_TYPE SET LEDGER_CODE=?,POSITION=?,LEDGER_TYPE=?,START_NO=?,PARE" +
"NT_ID=?, CHILD=?,DEBIT_CREDIT=?  WHERE MTID=? "
;
        UPDATE_Q = "UPDATE IA_GL_TOTALING_ACCOUNT SET LEDGER_TYPE=? WHERE MTID=? ";
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, ledgerCode);
        ps.setString(2, position);
        ps.setString(3, ledgerType);
        ps.setString(4, startNo);
        ps.setString(5, parentId);
        ps.setString(6, child);
        ps.setString(7, debitCredit);
        ps.setString(8, id);
        done = ps.executeUpdate() != -1;
        ps = con.prepareStatement(UPDATE_Q);
        ps.setString(1, ledgerType);
        ps.setString(2, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Ledger Type... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createGLStructure(String position, String name, String mask, String structureType, String delimiter, String effectiveDate, String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_GL_STRUCTURE (MTID,POSITION,NAME,MASK,STRUCTURE_TYPE,DELIMITER,EF" +
"FECTIVE_DT,STATUS) VALUES(?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_GL_STRUCTURE");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, position);
        ps.setString(3, name);
        ps.setString(4, mask);
        ps.setString(5, structureType);
        ps.setString(6, delimiter);
        ps.setDate(7, dateConvert(effectiveDate));
        ps.setString(8, status);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating GLStructure... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }

    public void deleteGLStructure(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_GL_STRUCTURE  WHERE MTID = ?";
        con = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Deleting GLStructure... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
    }

    public boolean updateGLStructure(String id, String position, String name, String mask, String structureType, String delimiter, String effectiveDate, 
            String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_GL_STRUCTURE SET POSITION=?,NAME=?,MASK=?,STRUCTURE_TYPE=?,DELIMITER=?" +
", EFFECTIVE_DT=?,STATUS=?  WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, position);
        ps.setString(2, name);
        ps.setString(3, mask);
        ps.setString(4, structureType);
        ps.setString(5, delimiter);
        ps.setDate(6, dateConvert(effectiveDate));
        ps.setString(7, status);
        ps.setString(8, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLStructure... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateDelimiter(String id, String delimiter)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = (new StringBuilder()).append("update ia_gl_structure  set delimiter = '").append(delimiter).append("' ").append("WHERE MTID =  '").append(id).append("'").toString();
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Delimiter... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createSIC(String icCode, int userId, String description, String createDate, String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_INDUSTRY (MTID,IC_CODE,USER_ID,DESCRIPTION,CREATE_DT,STATUS) VALU" +
"ES(?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        try {
        id = helper.getGeneratedId("IA_INDUSTRY");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, icCode);
        ps.setInt(3, userId);
        ps.setString(4, description);
        ps.setDate(5, dateConvert(createDate));
        ps.setString(6, status);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating SIC Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public void deleteSIC(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_INDUSTRY  WHERE MTID = ?";
        con = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Deleting SIC Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
    }

    public boolean updateSIC(String id, String icCode, int userId, String description, String createDate, String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_INDUSTRY SET IC_CODE=?,USER_ID=?,DESCRIPTION=?, CREATE_DT=?,STATUS=?  " +
"WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, icCode);
        ps.setInt(2, userId);
        ps.setString(3, description);
        ps.setDate(4, dateConvert(createDate));
        ps.setString(5, status);
        ps.setString(6, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING SIC Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createRate(int ptid, String rate, String userId, String createDate, String rateEffectiveDate, String description, String accountPayable, 
            String accountReceivable, String staff, String effectiveDate, String status, String rateCode, String indexName)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO mg_gb_rate (MTID,PTID,RATE,USER_ID,CREATE_DT,RATE_EFFECTIVE_DT,DESCR" +
"IPTION, ACCOUNT_PAYABLE,ACCOUNT_RECEIVABLE,STAFF,EFFECTIVE_DT,STATUS,RATE_CODE,I" +
"NDEX_NAME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        try {
        id = helper.getGeneratedId("mg_gb_rate");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setInt(2, ptid);
        ps.setString(3, rate);
        ps.setString(4, userId);
        ps.setDate(5, dateConvert(createDate));
        ps.setDate(6, dateConvert(rateEffectiveDate));
        ps.setString(7, description);
        ps.setString(8, accountPayable);
        ps.setString(9, accountReceivable);
        ps.setString(10, staff);
        ps.setDate(11, dateConvert(effectiveDate));
        ps.setString(12, status);
        ps.setString(13, rateCode);
        ps.setString(14, indexName);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Index Rate... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateRate(String id, int ptid, String rate, String userId, String createDate, String rateEffectiveDate, String description, 
            String accountPayable, String accountReceivable, String staff, String effectiveDate, String status, String rateCode, String indexName)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE mg_gb_rate SET PTID=?,RATE=?,USER_ID=?,CREATE_DT=?,RATE_EFFECTIVE_DT=?, D" +
"ESCRIPTION=?,ACCOUNT_PAYABLE=?,ACCOUNT_RECEIVABLE=?,STAFF=?, EFFECTIVE_DT=?,STAT" +
"US=?,RATE_CODE=?,INDEX_NAME=?  WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setInt(1, ptid);
        ps.setString(2, rate);
        ps.setString(3, userId);
        ps.setDate(4, dateConvert(createDate));
        ps.setDate(5, dateConvert(rateEffectiveDate));
        ps.setString(6, description);
        ps.setString(7, accountPayable);
        ps.setString(8, accountReceivable);
        ps.setString(9, staff);
        ps.setDate(10, dateConvert(effectiveDate));
        ps.setString(11, status);
        ps.setString(12, rateCode);
        ps.setString(13, indexName);
        ps.setString(14, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Index Rate... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createCharge(String chargeCode, String acPayable, String acReceivable, String staff, String currency, String chargeType, String basis, 
            String percent, String effectiveDate, String status, String overDrawAc, String chargeToZero, String minCharge, String maxCharge, 
            String minMaxBasis, String maxChgYTD, String maxChgLTD, String waiveCharge, String graceDays, String freeDays, String freePeriod, 
            String balance, String balanceThreshold, String balanceType, String crPercent, String threshold, String stateTax, String localTax, 
            String earnings, String createDate, String description, String userId, String chargeEvery, String description2, String triggered, 
            String chargePeriod)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO MG_GB_CC (MTID,CHARGE_CODE,AC_PAYABLE,AC_RECEIVABLE,STAFF,CURRENCY,C" +
"HARGE_TYPE,BASIS, PERCENT_BASIS,EFFECTIVE_DT,STATUS,OVER_DRAW_AC,CC_ZERO_OD,MIN_" +
"CHARGE,MAX_CHARGE, MIN_MAX_PERIOD,MAX_CHARGE_YTD,MAX_CHARGE_LTD,WAIVE,NO_GRACE_D" +
"AYS,NO_FREE_DAY, NO_FREE_PERIOD,BALANCE,BAL_THRESHOLD,BAL_TYPE,CREDIT_PERCENT,TH" +
"RESHOLD,STATE_TAX, LOCAL_TAX,EARNINGS,CREATE_DT,DESCRIPTION,USER_ID,CHARGE_EVERY" +
",DESCRIPTION2, TRIGGERED,CHARGE_PERIOD) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("MG_GB_CC");
        try {
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, chargeCode);
        ps.setString(3, acPayable);
        ps.setString(4, acReceivable);
        ps.setString(5, staff);
        ps.setString(6, currency);
        ps.setString(7, chargeType);
        ps.setString(8, basis);
        ps.setString(9, percent);
        ps.setDate(10, dateConvert(effectiveDate));
        ps.setString(11, status);
        ps.setString(12, overDrawAc);
        ps.setString(13, chargeToZero);
        ps.setString(14, minCharge);
        ps.setString(15, maxCharge);
        ps.setString(16, minMaxBasis);
        ps.setString(17, maxChgYTD);
        ps.setString(18, maxChgLTD);
        ps.setString(19, waiveCharge);
        ps.setString(20, graceDays);
        ps.setString(21, freeDays);
        ps.setString(22, freePeriod);
        ps.setString(23, balance);
        ps.setString(24, balanceThreshold);
        ps.setString(25, balanceType);
        ps.setString(26, crPercent);
        ps.setString(27, threshold);
        ps.setString(28, stateTax);
        ps.setString(29, localTax);
        ps.setString(30, earnings);
        ps.setDate(31, dateConvert(createDate));
        ps.setString(32, description);
        ps.setString(33, userId);
        ps.setString(34, chargeEvery);
        ps.setString(35, description2);
        ps.setString(36, triggered);
        ps.setString(37, chargePeriod);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Charge Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public void deleteCharge(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM MG_GB_CC  WHERE MTID = ?";
        con = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Deleting Charge Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
    }

    public boolean updateCharge(String id, String chargeCode, String acPayable, String acReceivable, String staff, String currency, String chargeType, 
            String basis, String percent, String effectiveDate, String status, String overDrawAc, String chargeToZero, String minCharge, 
            String maxCharge, String minMaxBasis, String maxChgYTD, String maxChgLTD, String waiveCharge, String graceDays, String freeDays, 
            String freePeriod, String balance, String balanceThreshold, String balanceType, String crPercent, String threshold, String stateTax, 
            String localTax, String earnings, String createDate, String description, String userId, String chargeEvery, String description2, 
            String triggered, String chargePeriod)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE MG_GB_CC SET CHARGE_CODE=?,AC_PAYABLE=?,AC_RECEIVABLE=?,STAFF=?,CURRENCY=" +
"?, CHARGE_TYPE=?,BASIS=?,PERCENT_BASIS=?,EFFECTIVE_DT=?,STATUS=?,OVER_DRAW_AC=?," +
" CC_ZERO_OD=?,MIN_CHARGE=?,MAX_CHARGE=?,MIN_MAX_PERIOD=?,MAX_CHARGE_YTD=?, MAX_C" +
"HARGE_LTD=?,WAIVE=?,NO_GRACE_DAYS=?,NO_FREE_DAY=?,NO_FREE_PERIOD=?, BALANCE=?,BA" +
"L_THRESHOLD=?,BAL_TYPE=?,CREDIT_PERCENT=?,THRESHOLD=?,STATE_TAX=?, LOCAL_TAX=?,E" +
"ARNINGS=?,CREATE_DT=?,DESCRIPTION=?,USER_ID=?,CHARGE_EVERY=?, DESCRIPTION2=?,TRI" +
"GGERED=?,CHARGE_PERIOD=? WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, chargeCode);
        ps.setString(2, acPayable);
        ps.setString(3, acReceivable);
        ps.setString(4, staff);
        ps.setString(5, currency);
        ps.setString(6, chargeType);
        ps.setString(7, basis);
        ps.setString(8, percent);
        ps.setDate(9, dateConvert(effectiveDate));
        ps.setString(10, status);
        ps.setString(11, overDrawAc);
        ps.setString(12, chargeToZero);
        ps.setString(13, minCharge);
        ps.setString(14, maxCharge);
        ps.setString(15, minMaxBasis);
        ps.setString(16, maxChgYTD);
        ps.setString(17, maxChgLTD);
        ps.setString(18, waiveCharge);
        ps.setString(19, graceDays);
        ps.setString(20, freeDays);
        ps.setString(21, freePeriod);
        ps.setString(22, balance);
        ps.setString(23, balanceThreshold);
        ps.setString(24, balanceType);
        ps.setString(25, crPercent);
        ps.setString(26, threshold);
        ps.setString(27, stateTax);
        ps.setString(28, localTax);
        ps.setString(29, earnings);
        ps.setDate(30, dateConvert(createDate));
        ps.setString(31, description);
        ps.setString(32, userId);
        ps.setString(33, chargeEvery);
        ps.setString(34, description2);
        ps.setString(35, triggered);
        ps.setString(36, chargePeriod);
        ps.setString(37, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Charge Code... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean createCalendar(String startDate, String endDate, String qtrEnd, String period, String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO IA_CALENDAR_DATE (MTID,START_DATE,END_DATE,QTR_END,PERIOD,STATUS) VA" +
"LUES(?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_CALENDAR_DATE");
        int date1 = modulus(endDate);
        if(date1 == 0)
        {
            qtrEnd = "Y";
        } else
        {
            qtrEnd = "N";
        }
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setDate(2, df.dateConvert(startDate));
        ps.setDate(3, df.dateConvert(endDate));
        ps.setString(4, qtrEnd);
        ps.setString(5, period);
        ps.setString(6, status);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Calendar... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }

    public void deleteCalendar(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_CALENDAR_DATE  WHERE MTID = ?";
        con = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Deleting Calendar... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
    }

    public boolean updateCalendar(String id, String startDate, String endDate, String qtrEnd, String period, String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_CALENDAR_DATE SET START_DATE=?,END_DATE=?,QTR_END=?, PERIOD=?,STATUS=?" +
"  WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setDate(1, df.dateConvert(startDate));
        ps.setDate(2, df.dateConvert(endDate));
        ps.setString(3, qtrEnd);
        ps.setString(4, period);
        ps.setString(5, status);
        ps.setString(6, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING Calendar... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally{
        closeConnection(con, ps);
    }
        return done;
    }

    public ArrayList findAllCalendar()
    {
        String filter = "";
        return findCalendarByQuery(filter);
    }
    public ArrayList findAllCalendar(String filter)
    {
        return findCalendarByQuery(filter);
    }
    public Calendar findCalendarById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findCalendarByQuery(filter);
        return records.size() <= 0 ? null : (Calendar)records.get(0);
    }

    public Calendar findCalendarByCalendarId(String calendarCode)
    {
        Calendar calendar = null;
        String filter = (new StringBuilder()).append(" AND CALENDAR_CODE = '").append(calendarCode).append("'").toString();
        ArrayList list = findCalendarByQuery(filter);
        if(list.size() > 0)
        {
            calendar = (Calendar)list.get(0);
        }
        return calendar;
    }

    public ArrayList findCalendarByQueryold(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,START_DATE,END_DATE,QTR_END,PERIOD,STATUS FROM IA_CALENDAR_DATE  WHE" +
"RE MTID IS NOT NULL "		
).append(filter).append(" ORDER BY START_DATE").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Calendar calendar;
        for(rs = ps.executeQuery(); rs.next(); records.add(calendar))
        {
            String id = rs.getString("MTID");
            String startDate = sdf.format(rs.getDate("START_DATE"));
            Date end = rs.getDate("END_DATE");
            String endDate = sdf.format(end);
            String qtrEnd = rs.getString("QTR_END");
            String period = rs.getString("PERIOD");
            String status = rs.getString("STATUS");
            calendar = new Calendar(id, startDate, endDate, qtrEnd, period, status);
        }

        closeConnection(con, ps, rs);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting Calendar...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally{
        closeConnection(con, ps, rs);
    }
        return records;
    }
    public ArrayList findCalendarByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
SELECT_QUERY = (new StringBuilder()).append("SELECT ID, WEEKDAY,DAY,MONTH,YEAR,PUBLIC_HOLIDAY,PROCESS_STATUS,CALENDAR_DATE,GL_CLOSING_STATUS,CLOSEDATE FROM MG_GB_CALENDAR  WHE" +
"RE ID IS NOT NULL "        		
).append(filter).append(" ORDER BY CALENDAR_DATE").toString();
        con = null;
        ps = null;
        rs = null;
 //       System.out.println("SELECT_QUERY IN findCalendarByQuery: "+SELECT_QUERY);
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Calendar calendar;
        for(rs = ps.executeQuery(); rs.next(); records.add(calendar))
        {
            String id = rs.getString("ID");
            String weekday = rs.getString("WEEKDAY");
            String calendarDate = sdf.format(rs.getDate("CALENDAR_DATE"));
            Date end = rs.getDate("CLOSEDATE");
            String endDate = sdf.format(end);
            String processstatus = rs.getString("PROCESS_STATUS");
            String holiday = rs.getString("PUBLIC_HOLIDAY");  
            String day = rs.getString("DAY"); 
            String month = rs.getString("MONTH"); 
            String year = rs.getString("YEAR"); 
            String closestatus = rs.getString("GL_CLOSING_STATUS");            
            calendar = new Calendar(id, weekday, calendarDate,  endDate, processstatus, holiday, day, month, year, closestatus);
        }

        closeConnection(con, ps, rs);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting Calendar in findCalendarByQuery...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally{
        closeConnection(con, ps, rs);
    }
        return records;
    }

    public boolean isCalendarExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        Calendar calendar = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_CALENDAR_DATE WHERE  MTID = ?";
        con = null;
        ps = null;
       // ResultSet rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        {
            if(!rs.next())
            {
                break;
            }
            int counted = rs.getInt(1);
            if(counted > 0)
            {
                exists = true;
            }
        } while(true);
        }
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error in isCalendarExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        } finally {
        closeConnection(con, ps);
        }
        return exists;
    }

    public ArrayList findAllTransactionHistory()
    {
        String filter = "";
        return findTransactionHistoryByQuery(filter);
    }

    public TransactionHistory findTransactionHistoryById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findTransactionHistoryByQuery(filter);
        return records.size() <= 0 ? null : (TransactionHistory)records.get(0);
    }

    public TransactionHistory findTransactionHistoryByTransId(String transCode)
    {
        TransactionHistory transHistory = null;
        String filter = (new StringBuilder()).append(" AND TRANS_CODE = '").append(transCode).append("'").toString();
        ArrayList list = findTransactionHistoryByQuery(filter);
        if(list.size() > 0)
        {
            transHistory = (TransactionHistory)list.get(0);
        }
        return transHistory;
    }

    public ArrayList findTransactionHistoryByQuery(String filter)
    {
        ArrayList _list;
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        _list = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,MTID,EFFECTIVE_DT,CREATE_DT,REVERSAL_DT,TRAN_CODE,DESCRIPTION, AMT,B" +
"ALANCE,STATUS FROM IA_GL_HISTORY   WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY 4,3").toString();
        con = null;
        rs = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        TransactionHistory transHistory;
        for(rs = ps.executeQuery(); rs.next(); _list.add(transHistory))
        {
            String id = rs.getString("MTID");
            Date ef = rs.getDate("EFFECTIVE_DT");
            String effectiveDate = sdf.format(ef);
            Date en = rs.getDate("CREATE_DT");
            String enteredDate = sdf.format(en);
            Date rv = rs.getDate("REVERSAL_DT");
            String reversalDate = sdf.format(rv);
            String transCode = rs.getString("TRAN_CODE");
            String description = rs.getString("DESCRIPTION");
            double amount = rs.getDouble("AMT");
            double balance = rs.getDouble("BALANCE");
            double debit = 0.0D;
            double credit = 0.0D;
            String status = rs.getString("STATUS");
            transHistory = new TransactionHistory(id, effectiveDate, enteredDate, reversalDate, transCode, description, amount, balance, debit, credit, status);
        }

        closeConnection(con, ps, rs);
    } catch (Exception e) {
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting transHistory ->").append(e.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally {
        closeConnection(con, ps, rs);
    }
        return _list;
    }

    public boolean isTransactionHistoryExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        TransactionHistory transHistory = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_GL_HISTORY WHERE  MTID = ?";
        con = null;
        ps = null;
      //  ResultSet rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        	{
            if(!rs.next())
            {
                break;
            }
            int counted = rs.getInt(1);
            if(counted > 0)
            {
                exists = true;
            }
        } while(true);
        }
        closeConnection(con, ps);
       
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error in isTransactionHistoryExisting()... ->").append(er).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return exists;
    }

    public ArrayList findAllHistory()
    {
        String filter = "";
        return findHistoryByQuery(filter);
    }

    public History findHistoryById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findHistoryByQuery(filter);
        return records.size() <= 0 ? null : (History)records.get(0);
    }

    public History findHistoryByHistoryId(String historyCode)
    {
        History history = null;
        String filter = (new StringBuilder()).append(" AND HISTORY_CODE = '").append(historyCode).append("'").toString();
        ArrayList list = findHistoryByQuery(filter);
        if(list.size() > 0)
        {
            history = (History)list.get(0);
        }
        return history;
    }

    public ArrayList findHistoryByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,GL_ACCT_NO,PERIOD_END_DT,END_BAL,AVG_BAL,AVG_BAL_YTD, LAST_YEAR_END_" +
"BAL,BUDGET_AMT,AVG_BAL-BUDGET_AMT as BUDGET_VAR,STATUS FROM IA_GL_BAL_HIST  WHER" +
"E MTID IS NOT NULL "
).append(filter).append(" ORDER BY 4,3").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        History history;
        for(rs = ps.executeQuery(); rs.next(); records.add(history))
        {
            String id = rs.getString("MTID");
            String accountNo = rs.getString("GL_ACCT_NO");
            Date ped = rs.getDate("PERIOD_END_DT");
            String periodEnding = sdf.format(ped);
            String endingBalance = rs.getString("END_BAL");
            String averageBalance = rs.getString("AVG_BAL");
            String avgBalYTD = rs.getString("AVG_BAL_YTD");
            String lastYearEndingBal = rs.getString("LAST_YEAR_END_BAL");
            String budgetAmount = rs.getString("BUDGET_AMT");
            String budgetVariance = rs.getString("BUDGET_VAR");
            String status = rs.getString("STATUS");
            history = new History(id, accountNo, periodEnding, endingBalance, averageBalance, avgBalYTD, lastYearEndingBal, budgetAmount, budgetVariance, status);
        }

        closeConnection(con, ps, rs);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting History...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally{
        closeConnection(con, ps, rs);
    }
        return records;
    }

    public boolean isHistoryExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        History history = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_GL_BAL_HIST WHERE  MTID = ?";
        con = null;
        ps = null;
      //  ResultSet rs = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if(!rs.next())
            {
                break;
            }
            int counted = rs.getInt(1);
            if(counted > 0)
            {
                exists = true;
            }
        } while(true);
       //  closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error in isHistoryExisting()... ->").append(er).toString());
        closeConnection(con, ps);
         } finally {
        closeConnection(con, ps);
         }
        return exists;
    }

    public ArrayList findAllSIC()
    {
        String filter = "";
        return findSICByQuery(filter);
    }

    public SIC findSICById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findSICByQuery(filter);
        return records.size() <= 0 ? null : (SIC)records.get(0);
    }

    public SIC findSICBySICId(String icCode)
    {
        SIC sic = null;
        String filter = (new StringBuilder()).append(" AND IC_CODE = '").append(icCode).append("'").toString();
        ArrayList list = findSICByQuery(filter);
        if(list.size() > 0)
        {
            sic = (SIC)list.get(0);
        }
        return sic;
    }

    public boolean isSICExisting(String icCode)
    {
        boolean exists = false;
        String filter = (new StringBuilder()).append(" WHERE IC_CODE = '").append(icCode).append("' ").toString();
        ArrayList records = findSICByQuery(filter);
        if(records.size() > 0)
        {
            exists = true;
        }
        return exists;
    }

    public ArrayList findSICByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,IC_CODE,USER_ID,DESCRIPTION,CREATE_DT,STATUS FROM IA_INDUSTRY  WHERE" +
" MTID IS NOT NULL "
).append(filter).append(" ORDER BY IC_CODE").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        SIC sic;
        for(rs = ps.executeQuery(); rs.next(); records.add(sic))
        {
            String id = rs.getString("MTID");
            String icCode = rs.getString("IC_CODE");
            int userId = 0;
            String description = rs.getString("DESCRIPTION");
            String createDate = rs.getString("CREATE_DT");
            String status = rs.getString("STATUS");
            sic = new SIC(id, icCode, userId, description, createDate, status);
        }

        closeConnection(con, ps, rs);
      } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting SIC Code...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
      } finally{
        closeConnection(con, ps, rs);
      }
        return records;
    }

    public ArrayList findAllLedgerType()
    {
        String filter = "";
        return findLedgerTypeByQuery(filter);
    }

    public LedgerType findLedgerTypeById(String id)
    {
        String filter = (new StringBuilder()).append("AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findLedgerTypeByQuery(filter);
        return records.size() <= 0 ? null : (LedgerType)records.get(0);
    }

    public LedgerType findLedgerTypeByDescription(String id)
    {
        String filter = (new StringBuilder()).append(" AND  DESCRIPTION = '").append(id).append("'").toString();
        ArrayList records = findLedgerTypeByQuery(filter);
        return records.size() <= 0 ? null : (LedgerType)records.get(0);
    }

    public LedgerType findLedgerTypeByLedgerCode(String ledgerCode)
    {
        LedgerType ledgerType = null;
        String filter = (new StringBuilder()).append(" AND LEDGER_CODE = '").append(ledgerCode).append("'").toString();
        ArrayList list = findLedgerTypeByQuery(filter);
        if(list.size() > 0)
        {
            ledgerType = (LedgerType)list.get(0);
        }
        return ledgerType;
    }

    public ArrayList findLedgerTypeByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,LEDGER_CODE,POSITION,LEDGER_TYPE,START_NO,PARENT_ID,CHILD, DEBIT_CRE" +
"DIT FROM IA_LEDGER_TYPE WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY LEDGER_CODE").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        LedgerType ledgerType1;
        for(rs = ps.executeQuery(); rs.next(); records.add(ledgerType1))
        {
            String id = rs.getString("MTID");
            String ledgerCode = rs.getString("LEDGER_CODE");
            String position = rs.getString("POSITION");
            String ledgerType = rs.getString("LEDGER_TYPE");
            String startNo = rs.getString("START_NO");
            String parentId = rs.getString("PARENT_ID");
            String child = rs.getString("CHILD");
            String debitCredit = rs.getString("DEBIT_CREDIT");
            ledgerType1 = new LedgerType(id, ledgerCode, position, ledgerType, startNo, parentId, child, debitCredit);
        }

        closeConnection(con, ps, rs);
        }  catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting Ledger Type...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
       } finally {
        closeConnection(con, ps, rs);
       }
        return records;
    }

    public ArrayList findAllGLAccountLedger()
    {
        String filter = "";
        return findGLAccountLedgerByQuery(filter);
    }

    public GLAccountLedger findGLAccountLedgerById(String id)
    {
        String filter = (new StringBuilder()).append("AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findGLAccountLedgerByQuery(filter);
        return records.size() <= 0 ? null : (GLAccountLedger)records.get(0);
    }

    public GLAccountLedger findGLAccountLedgerByDescription(String description)
    {
        GLAccountLedger glAccountLedger = null;
        String filter = (new StringBuilder()).append(" AND DESCRIPTION = '").append(description).append("'").toString();
        ArrayList list = findGLAccountLedgerByQuery(filter);
        if(list.size() > 0)
        {
            glAccountLedger = (GLAccountLedger)list.get(0);
        }
        return glAccountLedger;
    }

    public GLAccountLedger findGLAccountLedgerByLedgerNo(String ledgerNo)
    {
        GLAccountLedger glAccountLedger = null;
        String filter = (new StringBuilder()).append(" AND LEDGER_NO = '").append(ledgerNo).append("'").toString();
        ArrayList list = findGLAccountLedgerByQuery(filter);
        if(list.size() > 0)
        {
            glAccountLedger = (GLAccountLedger)list.get(0);
        }
        return glAccountLedger;
    }

    public ArrayList findGLAccountLedgerByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT  MTID,ACCT_TYPE,GL_ACCT_NO,MASK,LEVEL_NO,LEDGER_NO,DESCRIPTION, TOTAL_ACC" +
"T_LEVEL,TOTAL_LEDGER_NO,LOCAL_TAX,STATE_TAX,FEDERAL_TAX,DEBIT_CREDIT, CURRENCY,E" +
"FFECTIVE_DT,STATUS,AUTO_REPLICATE,RECON_ACCT,LEDGER_TYPE, DEBIT_NOTALLOWED,CREDI" +
"T_NOTALLOWED,TOTAL_INDICATOR,USER_ID,PARENT_ID FROM  IA_GL_ACCT_LEDGER  WHERE MT" +
"ID IS NOT NULL "
).append(filter).append(" ORDER BY 4,6").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        GLAccountLedger glAccountLedger;
        for(rs = ps.executeQuery(); rs.next(); records.add(glAccountLedger))
        {
            String id = rs.getString("MTID");
            String accountType = rs.getString("ACCT_TYPE");
            String accountNo = rs.getString("GL_ACCT_NO");
            String mask = rs.getString("MASK");
            String levelNo = rs.getString("LEVEL_NO");
            String ledgerNo = rs.getString("LEDGER_NO");
            String description = rs.getString("DESCRIPTION");
            String totalingLevel = rs.getString("TOTAL_ACCT_LEVEL");
            String totalingLedgerNo = rs.getString("TOTAL_LEDGER_NO");
            String localTax = rs.getString("LOCAL_TAX");
            String stateTax = rs.getString("STATE_TAX");
            String fedTax = rs.getString("FEDERAL_TAX");
            String debitCredit = rs.getString("DEBIT_CREDIT");
            String currency = rs.getString("CURRENCY");
            String effectiveDate = sdf.format(rs.getDate("EFFECTIVE_DT"));
            String status = rs.getString("STATUS");
            String autoReplicate = rs.getString("AUTO_REPLICATE");
            String reconAccount = rs.getString("RECON_ACCT");
            String ledgerType = rs.getString("LEDGER_TYPE");
            String drNAllowed = rs.getString("DEBIT_NOTALLOWED");
            String crNAllowed = rs.getString("CREDIT_NOTALLOWED");
            String totalIndicator = rs.getString("TOTAL_INDICATOR");
            String userId = rs.getString("USER_ID");
            String parentId = rs.getString("PARENT_ID");
            glAccountLedger = new GLAccountLedger(id, accountType, accountNo, mask, levelNo, ledgerNo, description, totalingLevel, totalingLedgerNo, localTax, stateTax, fedTax, debitCredit, currency, effectiveDate, status, autoReplicate, reconAccount, ledgerType, drNAllowed, crNAllowed, totalIndicator, userId, parentId);
        }

        closeConnection(con, ps, rs);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting GLAccountLedger...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally {
        closeConnection(con, ps, rs);
    }
        return records;
    }

    public ArrayList findMaxTotaling()
    {
        return findGLTotaling();
    }

    public ArrayList findGLTotaling()
    {
        ArrayList records;
        String PICK_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        PICK_QUERY = "SELECT MTID,LEDGER_NO,LEVEL_NO,DESCRIPTION,TOTALING_LEVEL, TOTALING_LEDGER_NO,LE" +
"DGER_TYPE,STATUS from ia_gl_totaling_account where level_no in (select max(b.lev" +
"el_no) from ia_gl_totaling_account b) ORDER BY LEDGER_NO"
;
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(PICK_QUERY);
        GLTotaling glTotaling;
        for(rs = ps.executeQuery(); rs.next(); records.add(glTotaling))
        {
            String id = rs.getString("MTID");
            String ledgerNo = rs.getString("LEDGER_NO");
            String levelNo = rs.getString("LEVEL_NO");
            String description = rs.getString("DESCRIPTION");
            String totalingLevel = rs.getString("TOTALING_LEVEL");
            String totalingLedgerNo = rs.getString("TOTALING_LEDGER_NO");
            String ledgerType = rs.getString("LEDGER_TYPE");
            String status = rs.getString("STATUS");
            glTotaling = new GLTotaling();
            glTotaling.setId(id);
            glTotaling.setLedgerNo(ledgerNo);
            glTotaling.setLevelNo(levelNo);
            glTotaling.setDescription(description);
            glTotaling.setTotalingLevel(totalingLevel);
            glTotaling.setTotalingLedgerNo(totalingLedgerNo);
            glTotaling.setLedgerType(ledgerType);
            glTotaling.setStatus(status);
        }

        closeConnection(con, ps, rs);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting glTotaling .......->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally {
        closeConnection(con, ps, rs);
    }
        return records;
    }

    public ArrayList findAllGLStructure()
    {
        String filter = "";
        return findGLStructureByQuery(filter);
    }

    public GLStructure findGLStructureById(String id)
    {
        String filter = (new StringBuilder()).append("AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findGLStructureByQuery(filter);
        return records.size() <= 0 ? null : (GLStructure)records.get(0);
    }

    public GLStructure findGLStructureByStructureCode(String structureCode)
    {
        GLStructure glStructure = null;
        String filter = (new StringBuilder()).append(" AND STRUCTURE_CODE = '").append(structureCode).append("'").toString();
        ArrayList list = findGLStructureByQuery(filter);
        if(list.size() > 0)
        {
            glStructure = (GLStructure)list.get(0);
        }
        return glStructure;
    }

    public ArrayList findGLStructureByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,POSITION,NAME,MASK,STRUCTURE_TYPE,DELIMITER,EFFECTIVE_DT,STATUS FROM" +
" IA_GL_STRUCTURE WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY POSITION").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        GLStructure glStructure;
        for(rs = ps.executeQuery(); rs.next(); records.add(glStructure))
        {
            String id = rs.getString("MTID");
            String position = rs.getString("POSITION");
            String name = rs.getString("NAME");
            String mask = rs.getString("MASK");
            String structureType = rs.getString("STRUCTURE_TYPE");
            String delimiter = rs.getString("DELIMITER");
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            String status = rs.getString("STATUS");
            glStructure = new GLStructure(id, position, name, mask, structureType, delimiter, effectiveDate, status);
        }

        closeConnection(con, ps, rs);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting GLStructure...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        } finally {
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public ArrayList findAllGLTotaling()
    {
        String filter = "";
        return findGLTotalingByQuery(filter);
    }

    public GLTotaling findGLTotalingById(String id)
    {
        String filter = (new StringBuilder()).append("AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findGLTotalingByQuery(filter);
        return records.size() <= 0 ? null : (GLTotaling)records.get(0);
    }

    public GLTotaling findGLTotalingByLedgerNo(String ledgerNo)
    {
        GLTotaling glTotaling = null;
        String filter = (new StringBuilder()).append(" AND LEDGER_NO = '").append(ledgerNo).append("'").toString();
        ArrayList list = findGLTotalingByQuery(filter);
        if(list.size() > 0)
        {
            glTotaling = (GLTotaling)list.get(0);
        }
        return glTotaling;
    }

    public ArrayList findGLTotalingByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,ACCOUNT_TYPE,LEDGER_NO,LEVEL_NO,DESCRIPTION,EFFECTIVE_DT, TOTALING_L" +
"EVEL,TOTALING_LEDGER_NO,LEDGER_TYPE,DEBIT_CREDIT,STATUS, USER_ID,CREATE_DT FROM " +
"ia_gl_Totaling_Account WHERE MTID IS NOT NULL  AND LEN(LEDGER_NO) = 4 "
).append(filter).append(" ORDER BY LEVEL_NO").toString();
        con = null;
        ps = null;
        rs = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        GLTotaling glTotaling;
        for(rs = ps.executeQuery(); rs.next(); records.add(glTotaling))
        {
            String id = rs.getString("MTID");
            String accountType = rs.getString("ACCOUNT_TYPE");
            String ledgerNo = rs.getString("LEDGER_NO");
            String levelNo = rs.getString("LEVEL_NO");
            String description = rs.getString("DESCRIPTION");
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            String totalingLevel = rs.getString("TOTALING_LEVEL");
            String totalingLedgerNo = rs.getString("TOTALING_LEDGER_NO");
            String ledgerType = rs.getString("LEDGER_TYPE");
            String debitCredit = rs.getString("DEBIT_CREDIT");
            String status = rs.getString("STATUS");
            int userId = 0;
            String createDate = formatDate(rs.getDate("CREATE_DT"));
            glTotaling = new GLTotaling(id, accountType, ledgerNo, levelNo, description, effectiveDate, totalingLevel, totalingLedgerNo, ledgerType, debitCredit, status, userId, createDate);
        }

        closeConnection(con, ps, rs);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting GLTotaling...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        } finally{
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public ArrayList findAllTotalingLevel()
    {
        String filter = "";
        return findTotalingLevelByQuery(filter);
    }

    public TotalingLevel findTotalingLevelById(String id)
    {
        String filter = (new StringBuilder()).append(" AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findTotalingLevelByQuery(filter);
        return records.size() <= 0 ? null : (TotalingLevel)records.get(0);
    }

    public TotalingLevel findTotalingLevelByTotalingCode(String totalingCode)
    {
        TotalingLevel level = null;
        String filter = (new StringBuilder()).append(" AND TOTALING_CODE = '").append(totalingCode).append("'").toString();
        ArrayList list = findTotalingLevelByQuery(filter);
        if(list.size() > 0)
        {
            level = (TotalingLevel)list.get(0);
        }
        return level;
    }

    public ArrayList findTotalingLevelByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,ACCOUNT_TYPE,LEDGER_NO,POSITION,DESCRIPTION,EFFECTIVE_DT, TOTALING_L" +
"EVEL,TOTALING_LEDGER_NO,LEDGER_TYPE,DEBIT_CREDIT,STATUS FROM ia_gl_Totaling_Leve" +
"l WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY LEDGER_NO").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        TotalingLevel level;
        for(rs = ps.executeQuery(); rs.next(); records.add(level))
        {
            String id = rs.getString("MTID");
            String accountType = rs.getString("ACCOUNT_TYPE");
            String ledgerNo = rs.getString("LEDGER_NO");
            String position = rs.getString("POSITION");
            String description = rs.getString("DESCRIPTION");
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            String totalingLevel = rs.getString("TOTALING_LEVEL");
            String totalingLedgerNo = rs.getString("TOTALING_LEDGER_NO");
            String ledgerType = rs.getString("LEDGER_TYPE");
            String debitCredit = rs.getString("DEBIT_CREDIT");
            String status = rs.getString("STATUS");
            level = new TotalingLevel(id, accountType, ledgerNo, position, description, effectiveDate, totalingLevel, totalingLedgerNo, ledgerType, debitCredit, status);
        }

        closeConnection(con, ps, rs);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting Totaling Level...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        } finally{
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public ArrayList findAllRate()
    {
        String filter = "";
        return findRateByQuery(filter);
    }

    public Rate findRateById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findRateByQuery(filter);
        return records.size() <= 0 ? null : (Rate)records.get(0);
    }

    public Rate findRateByRateId(String rateCode)
    {
        Rate ratee = null;
        String filter = (new StringBuilder()).append(" AND RATE_CODE = '").append(rateCode).append("'").toString();
        ArrayList list = findRateByQuery(filter);
        if(list.size() > 0)
        {
            ratee = (Rate)list.get(0);
        }
        return ratee;
    }

    public ArrayList findRateByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,PTID,RATE,USER_ID,CREATE_DT,RATE_EFFECTIVE_DT,DESCRIPTION, ACCOUNT_P" +
"AYABLE,ACCOUNT_RECEIVABLE,STAFF,EFFECTIVE_DT,STATUS,RATE_CODE, INDEX_NAME FROM m" +
"g_gb_rate WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY RATE_CODE").toString();
        con = null;
        rs = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Rate ratee;
        for(rs = ps.executeQuery(); rs.next(); records.add(ratee))
        {
            String id = rs.getString("MTID");
            int ptid = rs.getInt("PTID");
            String rate = rs.getString("RATE");
            String userId = rs.getString("USER_ID");
            String createDate = formatDate(rs.getDate("CREATE_DT"));
            String rateEffectiveDate = formatDate(rs.getDate("RATE_EFFECTIVE_DT"));
            String description = rs.getString("DESCRIPTION");
            String accountPayable = rs.getString("ACCOUNT_PAYABLE");
            String accountReceivable = rs.getString("ACCOUNT_RECEIVABLE");
            String staff = rs.getString("STAFF");
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            String status = rs.getString("STATUS");
            String rateCode = rs.getString("RATE_CODE");
            String indexName = rs.getString("INDEX_NAME");
            ratee = new Rate(id, ptid, rate, userId, createDate, rateEffectiveDate, description, accountPayable, accountReceivable, staff, effectiveDate, status, rateCode, indexName);
        }

        closeConnection(con, ps, rs);
        } catch (Exception e) {
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Index Rate ->").append(e.getMessage()).toString());
        closeConnection(con, ps, rs);
        } finally{
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public ArrayList findAllCharge()
    {
        String filter = "";
        return findChargeByQuery(filter);
    }

    public Charge findChargeById(String id)
    {
        String filter = (new StringBuilder()).append(" AND MTID = '").append(id).append("'").toString();
        ArrayList records = findChargeByQuery(filter);
        return records.size() <= 0 ? null : (Charge)records.get(0);
    }

    public Charge findChargeByChargeId(String chargeCode)
    {
        Charge charge = null;
        String filter = (new StringBuilder()).append(" AND CHARGE_CODE = '").append(chargeCode).append("'").toString();
        ArrayList list = findChargeByQuery(filter);
        if(list.size() > 0)
        {
            charge = (Charge)list.get(0);
        }
        return charge;
    }

    public ArrayList findChargeByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,CHARGE_CODE,AC_PAYABLE,AC_RECEIVABLE,STAFF,CURRENCY,CHARGE_TYPE, BAS" +
"IS,PERCENT_BASIS,EFFECTIVE_DT,STATUS,OVER_DRAW_AC,CC_ZERO_OD,MIN_CHARGE, MAX_CHA" +
"RGE,MIN_MAX_PERIOD,MAX_CHARGE_YTD,MAX_CHARGE_LTD,WAIVE,NO_GRACE_DAYS, NO_FREE_DA" +
"Y,NO_FREE_PERIOD,BALANCE,BAL_THRESHOLD,BAL_TYPE,CREDIT_PERCENT,THRESHOLD, STATE_" +
"TAX,LOCAL_TAX,EARNINGS,CREATE_DT,DESCRIPTION,USER_ID,CHARGE_EVERY, DESCRIPTION2," +
"TRIGGERED,CHARGE_PERIOD FROM MG_GB_CC WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY CHARGE_CODE").toString();
        con = null;
        rs = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Charge charge;
        for(rs = ps.executeQuery(); rs.next(); records.add(charge))
        {
            String id = rs.getString("MTID");
            String chargeCode = rs.getString("CHARGE_CODE");
            String acPayable = rs.getString("AC_PAYABLE");
            String acReceivable = rs.getString("AC_RECEIVABLE");
            String staff = rs.getString("STAFF");
            String currency = rs.getString("CURRENCY");
            String chargeType = rs.getString("CHARGE_TYPE");
            String basis = rs.getString("BASIS");
            String percent = rs.getString("PERCENT_BASIS");
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DT"));
            String status = rs.getString("STATUS");
            String overDrawAc = rs.getString("OVER_DRAW_AC");
            String chargeToZero = rs.getString("CC_ZERO_OD");
            String minCharge = rs.getString("MIN_CHARGE");
            String maxCharge = rs.getString("MAX_CHARGE");
            String minMaxBasis = rs.getString("MIN_MAX_PERIOD");
            String maxChgYTD = rs.getString("MAX_CHARGE_YTD");
            String maxChgLTD = rs.getString("MAX_CHARGE_LTD");
            String waiveCharge = rs.getString("WAIVE");
            String graceDays = rs.getString("NO_GRACE_DAYS");
            String freeDays = rs.getString("NO_FREE_DAY");
            String freePeriod = rs.getString("NO_FREE_PERIOD");
            String balance = rs.getString("BALANCE");
            String balanceThreshold = rs.getString("BAL_THRESHOLD");
            String balanceType = rs.getString("BAL_TYPE");
            String crPercent = rs.getString("CREDIT_PERCENT");
            String threshold = rs.getString("THRESHOLD");
            String stateTax = rs.getString("STATE_TAX");
            String localTax = rs.getString("LOCAL_TAX");
            String earnings = rs.getString("EARNINGS");
            String createDate = formatDate(rs.getDate("CREATE_DT"));
            String description = rs.getString("DESCRIPTION");
            String userId = rs.getString("USER_ID");
            String chargeEvery = rs.getString("CHARGE_EVERY");
            String description2 = rs.getString("DESCRIPTION2");
            String triggered = rs.getString("TRIGGERED");
            String chargePeriod = rs.getString("CHARGE_PERIOD");
            charge = new Charge(id, chargeCode, acPayable, acReceivable, staff, currency, chargeType, basis, percent, effectiveDate, status, overDrawAc, chargeToZero, minCharge, maxCharge, minMaxBasis, maxChgYTD, maxChgLTD, waiveCharge, graceDays, freeDays, freePeriod, balance, balanceThreshold, balanceType, crPercent, threshold, stateTax, localTax, earnings, createDate, description, userId, chargeEvery, description2, triggered, chargePeriod);
        }

        closeConnection(con, ps, rs);
        } catch (Exception e) {
        System.out.println((new StringBuilder()).append("ERROR:Error Selecting Charge Code ->").append(e.getMessage()).toString());
        closeConnection(con, ps, rs);
    } finally{
        closeConnection(con, ps, rs);
    }
        return records;
    }

    public boolean createglCurrency(String currency_id, String iso_code, String createDate, String ledgerId, String effectiveDate, String userId, String ledgerNo, 
            String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        CREATE_QUERY = "INSERT INTO ia_gl_ccy (MTID,currency_id,iso_code,create_dt,ledger_id,effective_d" +
"t,user_id,ledger_no,status) VALUES(?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("ia_gl_ccy");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, currency_id);
        ps.setString(3, iso_code);
        ps.setDate(4, df.dateConvert(new Date()));
        ps.setString(5, ledgerId);
        ps.setDate(6, dateConvert(effectiveDate));
        ps.setString(7, userId);
        ps.setString(8, ledgerNo);
        ps.setString(9, status);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating GLCurrency Setup... ->").append(er.getMessage()).toString());
        System.out.println((new StringBuilder()).append("createDate... ->").append(createDate).toString());
        closeConnection(con, ps);
        } finally{
        closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateglCurrency(String id, String currency_id, String iso_code, String createDate, String ledgerId, String effectiveDate, String userId, 
            String ledgerNo, String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE ia_gl_ccy SET currency_id=?,iso_code=?,ledger_id=?,effective_dt=?,user_id" +
"=?,ledger_no=?,status=? WHERE LEDGER_NO=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, currency_id);
        ps.setString(2, iso_code);
        ps.setString(3, ledgerId);
        ps.setDate(4, dateConvert(effectiveDate));
        ps.setString(5, userId);
        ps.setString(6, ledgerNo);
        ps.setString(7, status);
        ps.setString(8, ledgerNo);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLCurrency Setup... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        } finally{
        closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList findAllglCurrency()
    {
        String filter = "";
        return findglCurrencyByQuery(filter);
    }

    public glCurrency findglCurrencyById(String id)
    {
        String filter = (new StringBuilder()).append("AND  MTID = '").append(id).append("'").toString();
        ArrayList records = findglCurrencyByQuery(filter);
        return records.size() <= 0 ? null : (glCurrency)records.get(0);
    }

    public glCurrency findGLCurrencyByLedgerNo(String ledgerNo)
    {
        String filter = (new StringBuilder()).append("AND  Ledger_No = '").append(ledgerNo).append("'").toString();
        ArrayList records = findglCurrencyByQuery(filter);
        return records.size() <= 0 ? null : (glCurrency)records.get(0);
    }

    public glCurrency findglCurrencyByCurrID(String currency_id)
    {
        glCurrency gc = null;
        String filter = (new StringBuilder()).append(" AND currency_id = '").append(currency_id).append("'").toString();
        ArrayList list = findglCurrencyByQuery(filter);
        if(list.size() > 0)
        {
            gc = (glCurrency)list.get(0);
        }
        return gc;
    }

    public ArrayList findglCurrencyByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,currency_id,iso_code,create_dt,ledger_id,effective_dt,user_id, ledge" +
"r_no,status FROM ia_gl_ccy WHERE MTID IS NOT NULL "
).append(filter).append(" ORDER BY currency_id").toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        glCurrency gc;  
        for(rs = ps.executeQuery(); rs.next(); records.add(gc))
        {
            String id = rs.getString("MTID");
            String currency_id = rs.getString("currency_id");
            String iso_code = rs.getString("iso_code");
            String createDate = rs.getString("create_dt");
            String ledgerId = rs.getString("ledger_id");
            String effectiveDate = formatDate(rs.getDate("effective_dt"));
            String userId = rs.getString("user_id");
            String ledgerNo = rs.getString("ledger_no");
            String status = rs.getString("status");
            gc = new glCurrency(id, currency_id, iso_code, createDate, ledgerId, effectiveDate, userId, ledgerNo, status);
        }

        closeConnection(con, ps, rs);
        } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error Selecting glCurrency Setup...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        } finally{
        closeConnection(con, ps, rs);
        }
        return records;
    }

    public boolean createBalances(String bran, String userId, String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        CREATE_QUERY = (new StringBuilder()).append("INSERT into ia_gl_balances(gl_acct_no,branch_code,user_Id,COMP_CODE) (select led" +
"ger_no,'"
).append(bran).append("','").append(userId).append("','").append(companyCode).append("' from ia_gl_acct_ledger where ledger_no+'").append(bran).append("' not in (select gl_acct_no+branch_code  from ").append("IA_GL_BALANCES) and auto_replicate = 'Y')").toString();
        con = null;
        ps = null;
        done = false;
        System.out.println("createBalances CREATE_QUERY: "+CREATE_QUERY);
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    	} catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error creating Balances... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
    }

    public boolean createGLAccountLedger(String accountType, String accountNo, String mask, String levelNo, String ledgerNo, String description, String totalingLevel, 
            String totalingLedgerNo, String localTax, String stateTax, String fedTax, String debitCredit, String currency, String effectiveDate, 
            String status, String autoReplicate, String reconAccount, String ledgerType, String drNAllowed, String crNAllowed, String totalIndicator, 
            String userId, String parentId,String companyCode)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String id;
        String ledgerId;
        System.out.println("=====localTax====>>"+localTax +"  ===stateTax==>> "+stateTax+"===fedTax=====>> "+fedTax);
        CREATE_QUERY = "INSERT INTO IA_GL_ACCT_LEDGER (MTID,ACCT_TYPE,GL_ACCT_NO,MASK,LEVEL_NO,LEDGER_NO" +
",DESCRIPTION,TOTAL_ACCT_LEVEL, TOTAL_LEDGER_NO,LOCAL_TAX,STATE_TAX,FEDERAL_TAX,D" +
"EBIT_CREDIT,CURRENCY,EFFECTIVE_DT, STATUS,AUTO_REPLICATE,RECON_ACCT,LEDGER_TYPE," +
"DEBIT_NOTALLOWED,CREDIT_NOTALLOWED, TOTAL_INDICATOR,USER_ID,LEDGER_ID,PARENT_ID,COMP_CODE," +
"CREATE_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        done = false;
        id = helper.getGeneratedId("IA_GL_ACCT_LEDGER");
        ledgerId = id;
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, accountType);
        ps.setString(3, accountNo);
        ps.setString(4, mask);
        ps.setString(5, levelNo);
        ps.setString(6, ledgerNo);
        ps.setString(7, description);
        ps.setString(8, totalingLevel);
        ps.setString(9, totalingLedgerNo);
        if(localTax==null){localTax = "N";}
        ps.setString(10, localTax);
        if(stateTax==null){stateTax = "N";}        
        ps.setString(11, stateTax);
        if(fedTax==null){fedTax = "N";}           
        ps.setString(12, fedTax);
        ps.setString(13, debitCredit);
        ps.setString(14, currency);
        ps.setDate(15, dateConvert(effectiveDate));
        ps.setString(16, status);
        ps.setString(17, autoReplicate);
        ps.setString(18, reconAccount);
        ps.setString(19, ledgerType);
        ps.setString(20, drNAllowed);
        ps.setString(21, crNAllowed);
        ps.setString(22, totalIndicator);
        ps.setString(23, userId);
        ps.setString(24, ledgerId);
        ps.setString(25, parentId);
        ps.setString(26, companyCode);
        ps.setDate(27, df.dateConvert(new Date()));        
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("value of id is ").append(id).toString());
        System.out.println((new StringBuilder()).append("Error creating GLAccountLedger... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }

    public boolean updateGLAccountLedger(String id, String accountType, String accountNo, String mask, String levelNo, String ledgerNo, String description, 
            String totalingLevel, String totalingLedgerNo, String localTax, String stateTax, String fedTax, String debitCredit, String currency, 
            String effectiveDate, String status, String autoReplicate, String reconAccount, String ledgerType, String drNAllowed, String crNAllowed, 
            String totalIndicator, String userId, String parentId, String localtax, String statetax, String federaltax)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        boolean done;
        UPDATE_QUERY = "UPDATE IA_GL_ACCT_LEDGER SET ACCT_TYPE=?,GL_ACCT_NO=?,MASK=?,LEVEL_NO=?,LEDGER_N" +
"O=?,DESCRIPTION=?, TOTAL_ACCT_LEVEL=?,TOTAL_LEDGER_NO=?,LOCAL_TAX=?,STATE_TAX=?," +
"FEDERAL_TAX=?, CURRENCY=?,EFFECTIVE_DT=?,STATUS=?,AUTO_REPLICATE=?, RECON_ACCT=?" +
",LEDGER_TYPE=?,DEBIT_NOTALLOWED=?,CREDIT_NOTALLOWED=?, TOTAL_INDICATOR=?, USER_I" +
"D=? WHERE MTID=? "
;
        con = null;
        ps = null;
        done = false;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, accountType);
        ps.setString(2, accountNo);
        ps.setString(3, mask);
        ps.setString(4, levelNo);
        ps.setString(5, ledgerNo);
        ps.setString(6, description);
        ps.setString(7, totalingLevel);
        ps.setString(8, totalingLedgerNo);
        ps.setString(9, localTax);
        ps.setString(10, stateTax);
        ps.setString(11, fedTax);
        ps.setString(12, currency);
        ps.setDate(13, df.dateConvert(effectiveDate));
        ps.setString(14, status);
        ps.setString(15, autoReplicate);
        ps.setString(16, reconAccount);
        ps.setString(17, ledgerType);
        ps.setString(18, drNAllowed);
        ps.setString(19, crNAllowed);
        ps.setString(20, totalIndicator);
        ps.setString(21, userId);
    //    ps.setString(22, localtax);
     //   ps.setString(23, statetax);
     //   ps.setString(24, federaltax);
        ps.setString(22, id);
        done = ps.executeUpdate() != -1;
        closeConnection(con, ps);
    } catch (Exception er) {
        System.out.println((new StringBuilder()).append("Error UPDATING GLAccountLedger... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
    } finally {
        closeConnection(con, ps);
    }
        return done;
    }


    
}
