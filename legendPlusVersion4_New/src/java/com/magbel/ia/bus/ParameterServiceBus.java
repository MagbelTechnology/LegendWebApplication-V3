package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;

public class ParameterServiceBus extends PersistenceServiceDAO
{

    ApplicationHelper helper;

    public ParameterServiceBus()
    {
        helper = new ApplicationHelper();
    }

    public void createMandatoryField(String formId, String formField, String formLabel, String message, String flag)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_MANDATORY_FIELD( MTID,FORM_ID,FORM_FIELD,FORM_LABEL,MESSAGE,FLAG)" +
" VALUES(?,?,?,?,?,?)"
;
      
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_MANDATORY_FIELD");
        try{
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, formId);
        ps.setString(3, formField);
        ps.setString(4, formLabel);
        ps.setString(5, message);
        ps.setString(6, "F");
        ps.execute();
        closeConnection(con, ps);
        }catch(Exception er){
        System.out.println((new StringBuilder()).append("Error creating MandatoryField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        }finally{
        	 closeConnection(con, ps);
        }
    }

    public void deleteMandatoryField(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_MANDATORY_FIELD  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        try{
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        }
        catch(Exception er){
        System.out.println((new StringBuilder()).append("Error Deleting MandatoryField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        }finally{
       	 closeConnection(con, ps);
       }
    }

    public void updateMandatoryField(String id, String message, String flag)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_MANDATORY_FIELD SET  MESSAGE = ?,FLAG = ? WHERE  MTID = ?";
        con = null;
        ps = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, message);
        ps.setString(2, flag);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
	    }
	    catch(Exception er){
        System.out.println((new StringBuilder()).append("Error UPDATING MandatoryField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
	    }finally{
	   	 closeConnection(con, ps);
	    }
	    }

    public ArrayList findAllMandatoryField()
    {
        String filter = "";
        ArrayList records = findMandatoryFieldByQuery(filter);
        return records;
    }

    public ArrayList findMandatoryFieldFormId(String formId)
    {
        String filter = (new StringBuilder()).append(" WHERE FORM_ID = '").append(formId).append("' ").toString();
        ArrayList records = findMandatoryFieldByQuery(filter);
        return records;
    }

    public MandatoryField findMandatoryFieldById(String id)
    {
        MandatoryField mandatoryfield = null;
        String filter = (new StringBuilder()).append(" WHERE MTID = '").append(id).append("' ").toString();
        ArrayList records = findMandatoryFieldByQuery(filter);
        if(records.size() > 0)
        {
            mandatoryfield = (MandatoryField)records.get(0);
        }
        return mandatoryfield;
    }

    public boolean isMandatoryFieldExisting(String formId, String formField)
    {
        boolean exists = false;
        String filter = (new StringBuilder()).append(" WHERE FORM_ID = '").append(formId).append("' AND FORM_FIELD = '").append(formField).append("' ").toString();
        ArrayList records = findMandatoryFieldByQuery(filter);
        if(records.size() > 0)
        {
            exists = true;
        }
        return exists;
    }

    public ArrayList findMandatoryFieldByQuery(String filter)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = (new StringBuilder()).append("SELECT MTID,FORM_ID,FORM_FIELD,FORM_LABEL,MESSAGE FROM IA_MANDATORY_FIELD ").append(filter).toString();
        con = null;
        ps = null;
        rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        MandatoryField mandatoryfield;
        for(rs = ps.executeQuery(); rs.next(); records.add(mandatoryfield))
        {
            String id = rs.getString("MTID");
            String formId = rs.getString("FORM_ID");
            String formField = rs.getString("FORM_FIELD");
            String formLabel = rs.getString("FORM_LABEL");
            String message = rs.getString("MESSAGE");
            String flag = rs.getString("FLAG");
            mandatoryfield = new MandatoryField(id, formId, formField, formLabel, message, flag);
        }

        closeConnection(con, ps, rs);
	    }
	    catch(Exception er){
        System.out.println((new StringBuilder()).append("Error finding All MandatoryField...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
	    }finally{
	    	closeConnection(con, ps, rs);
		    }
        return records;
    }
/*
    public void createErrorCode(String code, String description)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_ERROR_CODE (MTID,CODE,DESCRIPTION) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_ERROR_CODE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, description);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating ErrorCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteErrorCode(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_ERROR_CODE  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting ErrorCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateErrorCode(String id, String code, String description)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_ERROR_CODE SET  CODE = ?,DESCRIPTION = ? WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, description);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING ErrorCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllErrorCode()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_ERROR_CODE";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        ErrorCode errorcode;
        for(rs = ps.executeQuery(); rs.next(); records.add(errorcode))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            errorcode = new ErrorCode(id, code, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All ErrorCode...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public ErrorCode findErrorCodeById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ErrorCode errorcode;
        FIND_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_ERROR_CODE WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        errorcode = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String qcode = rs.getString("CODE");
            String qdescription = rs.getString("DESCRIPTION");
            errorcode = new ErrorCode(id, qcode, qdescription);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding ErrorCodeByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return errorcode;
    }

    public boolean isErrorCodeExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM  IA_ERROR_CODE WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isErrorCodeExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createTransactionCode(String code, String description)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_TRANSACTION_CODE (MTID,CODE,DESCRIPTION) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_TRANSACTION_CODE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, description);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating TransactionCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteTransactionCode(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_TRANSACTION_CODE  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting TransactionCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateTransactionCode(String id, String code, String description)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_TRANSACTION_CODE SET  CODE = ?,DESCRIPTION = ? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, description);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING TransactionCode... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllTransactionCode()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_TRANSACTION_CODE";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        TransactionCode transactioncode;
        for(rs = ps.executeQuery(); rs.next(); records.add(transactioncode))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            transactioncode = new TransactionCode(id, code, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All TransactionCode...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public TransactionCode findTransactionCodeById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        TransactionCode transactioncode;
        FIND_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_TRANSACTION_CODE  WHERE MTID=?";
        con = null;
        ps = null;
        rs = null;
        transactioncode = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            transactioncode = new TransactionCode(id, code, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding TransactionCodeByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return transactioncode;
    }

    public boolean isTransactionCodeExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_TRANSACTION_CODE WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isTransactionCodeExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createMessage(String code, String description)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_MESSAGE (MTID,CODE,DESCRIPTION) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_MESSAGE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, description);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Message... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteMessage(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_MESSAGE  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Message... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateMessage(String id, String code, String description)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_MESSAGE  SET  CODE =?,DESCRIPTION =? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, description);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Message... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllMessage()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_MESSAGE";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Message message;
        for(rs = ps.executeQuery(); rs.next(); records.add(message))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            message = new Message(id, code, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Messages...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Message findMessageById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Message message;
        FIND_QUERY = "SELECT MTID,CODE,DESCRIPTION FROM  IA_MESSAGE WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        message = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            message = new Message(id, code, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding MessageByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return message;
    }

    public boolean isMessageExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_MESSAGE WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isMessageExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createProcessingDate(String code, String day)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_PROCESSING_DATE (MTID,CODE,DAY)  VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_PROCESSING_DATE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, day);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Processing Date... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteProcessingDate(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_PROCESSING_DATE  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Bank... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateProcessingDate(String id, String code, String day)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_PROCESSING_DATE SET  CODE= ?,DAY = ? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, day);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Processing Date... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllProcessingDate()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,DAY FROM IA_PROCESSING_DATE";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        ProcessingDate processingdate;
        for(rs = ps.executeQuery(); rs.next(); records.add(processingdate))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String day = rs.getString("DAY");
            processingdate = new ProcessingDate(id, code, day);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Processing Date...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public ProcessingDate findProcessingDateById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ProcessingDate processingdate;
        FIND_QUERY = "SELECT MTID,CODE,DAY FROM IA_PROCESSING_DATE WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        processingdate = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String day = rs.getString("DAY");
            processingdate = new ProcessingDate(id, code, day);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding BankByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return processingdate;
    }

    public boolean isProcessingDateExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        ProcessingDate processingdate = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_PROCESSING_DATE WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isProcessingDateExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createHoliday(String code, String name, String date)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_HOLIDAY (MTID,CODE,NAME,DATE) VALUES(?,?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_HOLIDAY");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, name);
        ps.setString(4, date);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_149;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Holiday... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_149;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteHoliday(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_HOLIDAY  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Holiday... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateHoliday(String id, String code, String name, String date)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_HOLIDAY SET  CODE =?,NAME =?,DATE =? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, name);
        ps.setString(3, date);
        ps.setString(4, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Holiday... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllHoliday()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,NAME,DATE  FROM IA_HOLIDAY";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Holiday holiday;
        for(rs = ps.executeQuery(); rs.next(); records.add(holiday))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            String date = rs.getString("DATE");
            holiday = new Holiday(id, code, name, date);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_192;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Holiday...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_192;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Holiday findHolidayById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Holiday holiday;
        FIND_QUERY = "SELECT MTID,CODE,NAME,DATE FROM IA_HOLIDAY WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        holiday = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            String date = rs.getString("DATE");
            holiday = new Holiday(id, code, name, date);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_174;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding BankByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_174;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return holiday;
    }

    public boolean isHolidayExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        Holiday holiday = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_HOLIDAY WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isHolidayExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createFrequency(String shortHand, String description)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_FREQUENCY (MTID,SHORTHAND,DESCRIPTION) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_FREQUENCY");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, shortHand);
        ps.setString(3, description);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Frequency... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteFrequency(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_FREQUENCY  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Frequency... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateFrequency(String id, String shortHand, String description)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_FREQUENCY SET  SHORTHAND =?,DESCRIPTION =? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, shortHand);
        ps.setString(2, description);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Frequency... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllFrequency()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,SHORTHAND,DESCRIPTION FROM IA_FREQUENCY";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Frequency frequency;
        for(rs = ps.executeQuery(); rs.next(); records.add(frequency))
        {
            String id = rs.getString("MTID");
            String shortHand = rs.getString("SHORTHAND");
            String description = rs.getString("DESCRIPTION");
            frequency = new Frequency(id, shortHand, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Frequency...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Frequency findFrequencyById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Frequency frequency;
        FIND_QUERY = "SELECT MTID,SHORTHAND,DESCRIPTION FROM IA_FREQUENCY WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        frequency = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String shortHand = rs.getString("SHORTHAND");
            String description = rs.getString("DESCRIPTION");
            frequency = new Frequency(id, shortHand, description);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding FrequencyByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return frequency;
    }

    public boolean isFrequencyExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_FREQUENCY WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isFrequencyExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createCountry(String code, String name)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_COUNTRY (MTID,CODE,NAME) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_COUNTRY");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, name);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Country... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteCountry(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_COUNTRY  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Country... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateCountry(String id, String code, String name)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_COUNTRY SET  CODE =?,NAME =? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, name);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Country... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllCountry()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,NAME FROM IA_COUNTRY";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Country country;
        for(rs = ps.executeQuery(); rs.next(); records.add(country))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            country = new Country(id, code, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Country...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Country findCountryById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Country country;
        FIND_QUERY = "SELECT MTID,CODE,NAME FROM IA_COUNTRY WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        country = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            country = new Country(id, code, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding CountryByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return country;
    }

    public boolean isCountryExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        Country country = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_COUNTRY WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isCountryExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_145;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createAdditionalField(String name)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_ADDITIONAL_FIELD (MTID,NAME) VALUES(?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_ADDITIONAL_FIELD");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, name);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_123;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating AdditionalField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_123;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteAdditionalField(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_ADDITIONAL_FIELD  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting AdditionalField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateAdditionalField(String id, String name)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_ADDITIONAL_FIELD  SET NAME =? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, name);
        ps.setString(2, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_117;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING AdditionalField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_117;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllAdditionalField()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,NAME FROM IA_ADDITIONAL_FIELD";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        AdditionalField additionalfield;
        for(rs = ps.executeQuery(); rs.next(); records.add(additionalfield))
        {
            String id = rs.getString("MTID");
            String name = rs.getString("NAME");
            additionalfield = new AdditionalField(id, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_166;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All AdditionalField...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_166;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public AdditionalField findAdditionalFieldById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        AdditionalField additionalfield;
        FIND_QUERY = "SELECT MTID,NAME FROM IA_ADDITIONAL_FIELD WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        additionalfield = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String name = rs.getString("NAME");
            additionalfield = new AdditionalField(id, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_148;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding AdditionalFieldByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_148;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return additionalfield;
    }

    public boolean isAdditionalFieldExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_ADDITIONAL_FIELD WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isAdditionalFieldExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createIndustry(String code, String name)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_INDUSTRY (MTID,CODE,NAME) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_INDUSTRY");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, name);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Industry... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteIndustry(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_INDUSTRY  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting IndustryField... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateIndustry(String id, String code, String name)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_INDUSTRY  SET CODE=?,NAME=? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, name);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Industry... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllIndustry()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,CODE,NAME FROM IA_INDUSTRY";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Industry industry;
        for(rs = ps.executeQuery(); rs.next(); records.add(industry))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            industry = new Industry(id, code, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Industry...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_179;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Industry findIndustryById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Industry industry;
        FIND_QUERY = "SELECT MTID,CODE,NAME FROM IA_INDUSTRY WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        industry = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("CODE");
            String name = rs.getString("NAME");
            industry = new Industry(id, code, name);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding IndustryByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_161;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return industry;
    }

    public boolean isIndustryExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_INDUSTRY WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isIndustryExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createAccountSegment(String position, String totalingLevel, String accountType, String code, String description, String level, String ledgerNo, 
            String type, String callerType, String incomeTax, String balanceType, String normalBalance, String status, String effectiveDate)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_ACCOUNT_SEGMENT (POSITION,TOTALING_LEVEL,ACCOUNT_TYPE,CODE,DESCRI" +
"PTION,LEVEL, LEDGER_NO,TYPE,CALLER_TYPE,INCOME_TAX,BALANCE_TYPE,NORMAL_BALANCE, " +
"STATUS,EFFECTIVE_DATE,MTID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_ACCOUNT_SEGMENT");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, position);
        ps.setString(2, totalingLevel);
        ps.setString(3, accountType);
        ps.setString(4, code);
        ps.setString(5, description);
        ps.setString(6, level);
        ps.setString(7, ledgerNo);
        ps.setString(8, type);
        ps.setString(9, callerType);
        ps.setString(10, incomeTax);
        ps.setString(11, balanceType);
        ps.setString(12, normalBalance);
        ps.setString(13, status);
        ps.setString(14, effectiveDate);
        ps.setString(15, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_269;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating AccountSegment... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_269;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteAccountSegment(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_ACCOUNT_SEGMENT  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting AccountSegment... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateAccountSegment(String position, String totalingLevel, String accountType, String code, String description, String level, String ledgerNo, 
            String type, String callerType, String incomeTax, String balanceType, String normalBalance, String status, String effectiveDate, 
            String id)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_ACCOUNT_SEGMENT SET POSITION=?,TOTALING_LEVEL=?,ACCOUNT_TYPE=?,CODE=?," +
"DESCRIPTION=?,LEVEL=?, LEDGER_NO=?,TYPE=?,CALLER_TYPE=?,INCOME_TAX=?,BALANCE_TYP" +
"E=?,NORMAL_BALANCE=?, STATUS=?,EFFECTIVE_DATE=? WHERE  MTID = ?"
;
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, position);
        ps.setString(2, totalingLevel);
        ps.setString(3, accountType);
        ps.setString(4, code);
        ps.setString(5, description);
        ps.setString(6, level);
        ps.setString(7, ledgerNo);
        ps.setString(8, type);
        ps.setString(9, callerType);
        ps.setString(10, incomeTax);
        ps.setString(11, balanceType);
        ps.setString(12, normalBalance);
        ps.setString(13, status);
        ps.setString(14, effectiveDate);
        ps.setString(15, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_258;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING AccountSegment... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_258;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllAccountSegment()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID, POSITION,TOTALING_LEVEL,ACCOUNT_TYPE,CODE,DESCRIPTION,LEVEL, LEDGER" +
"_NO,TYPE,CALL_TYPE,INCOME_TAX,BALANCE_TYPE,NORMAL_TYPE, STATUS,EFFECTIVE_DATE FR" +
"OM IA_ACCOUNT_SEGMENT"
;
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        AccountSegment accountsegment;
        for(rs = ps.executeQuery(); rs.next(); records.add(accountsegment))
        {
            String position = rs.getString("POSITION");
            String totalingLevel = rs.getString("TOTALING_LEVEL");
            String accountType = rs.getString("ACCOUNT_TYPE");
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            String level = rs.getString("LEVEL");
            String ledgerNo = rs.getString("LEDGER");
            String type = rs.getString("TYPE");
            String callerType = rs.getString("CALLER_TYPE");
            String incomeTax = rs.getString("INCOME_TAX");
            String balanceType = rs.getString("BALANCE_TYPE");
            String normalType = rs.getString("NORMAL_TYPE");
            String status = rs.getString("STATUS");
            String effectiveDate = rs.getString("EFFECTIVE_DATE");
            String id = rs.getString("MTID");
            accountsegment = new AccountSegment(id, position, totalingLevel, accountType, code, description, level, ledgerNo, type, callerType, incomeTax, balanceType, normalType, status, effectiveDate);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_335;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Industry...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_335;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public AccountSegment findAccountSegmentById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        AccountSegment accountsegment;
        FIND_QUERY = "SELECT MTID,POSITION,TOTALING_LEVEL,ACCOUNT_TYPE,CODE,DESCRIPTION,LEVEL, LEDGER_" +
"NO,TYPE,CALL_TYPE,INCOME_TAX,BALANCE_TYPE,NORMAL_TYPE, STATUS,EFFECTIVE_DATE FRO" +
"M IA_ACCOUNT_SEGMENT WHERE  MTID=?"
;
        con = null;
        ps = null;
        rs = null;
        accountsegment = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String position = rs.getString("POSITION");
            String totalingLevel = rs.getString("TOTALING_LEVEL");
            String accountType = rs.getString("ACCOUNT_TYPE");
            String code = rs.getString("CODE");
            String description = rs.getString("DESCRIPTION");
            String level = rs.getString("LEVEL");
            String ledgerNo = rs.getString("LEDGER_NO");
            String type = rs.getString("TYPE");
            String callerType = rs.getString("CALLER_TYPE");
            String incomeTax = rs.getString("INCOME_TAX");
            String balanceType = rs.getString("BALANCE_TYPE");
            String normalType = rs.getString("NORMAL_TYPE");
            String status = rs.getString("STATUS");
            String effectiveDate = rs.getString("EFFECTIVE_DATE");
            accountsegment = new AccountSegment(id, position, totalingLevel, accountType, code, description, level, ledgerNo, type, callerType, incomeTax, balanceType, normalType, status, effectiveDate);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_317;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding AccountSegmentByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_317;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return accountsegment;
    }

    public boolean isAccountSegmentExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_ACCOUNT_SEGMENT WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isAccountSegmentExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createTotalingLevel(int level, String ledgerNo)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_TOTALING_LEVEL (LEVEL,LEDGER_NO,MTID) VALUES(?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_TOTALING_LEVEL");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setInt(1, level);
        ps.setString(2, ledgerNo);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating TotalingLevel... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteTotalingLevel(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_TOTALING_LEVEL  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Totalinglevel... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateTotalingLevel(String id, int level, String ledgerNo)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_TOTALING_LEVEL SET LEVEL=?,LEDGER_NO=? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setInt(1, level);
        ps.setString(2, ledgerNo);
        ps.setString(3, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING TotalingLevel... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_128;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllTotalingLevel()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,LEVEL,LEDGER_NO FROM IA_TOTALING_LEVEL";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        for(rs = ps.executeQuery(); rs.next();)
        {
            int level = rs.getInt("LEVEL");
            String ledgerNo = rs.getString("LEDGER_NO");
            String id = rs.getString("MTID");
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_157;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All TotalingLevel...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_157;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public TotalingLevel findTotalingLevelById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        TotalingLevel totalinglevel;
        FIND_QUERY = "SELECT MTID,LEVEL,LEDGER_NO FROM IA_TOTALING_LEVEL WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        totalinglevel = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            int level = rs.getInt("LEVEL");
            String ledgerNo = rs.getString("LEDGER_NO");
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_147;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding TotalingLevelByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_147;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return totalinglevel;
    }

    public boolean isTotalingLevelExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_TOTALING_LEVEL WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isTotalingLevelExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createCalendarDate(String startDate, String endDate, String period)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_CALENDAR_DATE (MTID,START_DATE,END_DATE,PERIOD) VALUES(?,?,?,?)";
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_CALENDAR_DATE");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.setString(4, period);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_149;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating CalendarDate... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_149;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteCalendarDate(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_CALENDAR_DATE  WHERE MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting CalendarDate... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateCalendarDate(String id, String startDate, String endDate, String period)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE  IA_CALENDAR_DATE SET START_DATE=?,END_DATE=? ,PERIOD=? WHERE  MTID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.setString(4, period);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING CalendarDate.... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_138;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllCalendarDate()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,START_DATE,END_DATE,PERIOD FROM IA_SET_UP_CALENDAR_DATE";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        CalendarDate calendardate;
        for(rs = ps.executeQuery(); rs.next(); records.add(calendardate))
        {
            String id = rs.getString("MTID");
            String startDay = rs.getString("START_DAY");
            String endStart = rs.getString("END_START");
            String period = rs.getString("PERIOD");
            calendardate = new CalendarDate(id, startDay, endStart, period);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_192;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All CalendarDate...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_192;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public CalendarDate findCalendarDateById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        CalendarDate calendardate;
        FIND_QUERY = "SELECT MTID,START_DATE,END_START,PERIOD FROM IA_CALENDAR_DATE WHERE  MTID=?";
        con = null;
        ps = null;
        rs = null;
        calendardate = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String startDate = rs.getString("START_DATE");
            String endDate = rs.getString("END_DATE");
            String period = rs.getString("PERIOD");
            calendardate = new CalendarDate(id, startDate, endDate, period);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_174;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding CalendarDateByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_174;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return calendardate;
    }

    public boolean isCalendarDateExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_CALENDAR_DATE WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isCalendarDateExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_141;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public static void main(String args1[])
    {
    }

    public void createConsolidation(String code, String bank, String fax, String personno, String currency, String description, String address, 
            String contactPerson, String accountNo, String status, String sort)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_CONSOLIDATION( MTID,CODE,BANK,FAX,PERSONNO,CURRENCY,DESCRIPTION,A" +
"DDRESS,CONTACT_PERSON,ACCOUNT_NO,STATUS,SORT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_CONSOLIDATION");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, bank);
        ps.setString(4, fax);
        ps.setString(5, personno);
        ps.setString(6, currency);
        ps.setString(7, description);
        ps.setString(8, address);
        ps.setString(9, contactPerson);
        ps.setString(10, accountNo);
        ps.setString(11, status);
        ps.setString(12, sort);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_244;
        Exception ex;
        ex;
        System.out.println((new StringBuilder()).append("Error creating Consolidation... ->").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_244;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void createParameter(String code, String bank, String fax, String personno, String currency, String description, String address, 
            String contactPerson, String accountNo, String status, String sort)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_Parameter( MTID,CODE,BANK,FAX,PERSONNO,CURRENCY,DESCRIPTION,ADDRE" +
"SS,CONTACT_PERSON,ACCOUNT_NO,STATUS,SORT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_PARAMETER");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, bank);
        ps.setString(4, fax);
        ps.setString(5, personno);
        ps.setString(6, currency);
        ps.setString(7, description);
        ps.setString(8, address);
        ps.setString(9, contactPerson);
        ps.setString(10, accountNo);
        ps.setString(11, status);
        ps.setString(12, sort);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_244;
        Exception ex;
        ex;
        System.out.println((new StringBuilder()).append("Error creating Parameter... ->").append(ex.getMessage()).toString());
        ex.printStackTrace();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_244;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateConsolidation(String id, String code, String bank, String fax, String personno, String currency, String description, 
            String address, String contactPerson, String accountNo, String status, String sort)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_CONSOLIDATION SET CODE=?,BANK=?,FAX = ?,PERSONNO = ?,CURRENCY = ?,DESC" +
"RIPTION=?,ADDRESS=?, CONTACT_PERSON=?,ACCOUNT_NO=?,STATUS = ?,SORT = ?  WHERE MT" +
"ID = ?"
;
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, bank);
        ps.setString(3, fax);
        ps.setString(4, personno);
        ps.setString(5, currency);
        ps.setString(6, description);
        ps.setString(7, address);
        ps.setString(8, contactPerson);
        ps.setString(9, accountNo);
        ps.setString(10, status);
        ps.setString(11, sort);
        ps.setString(12, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_227;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Consolidation... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_227;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public ArrayList findAllConsolidation()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT * FROM IA_CONSOLIDATION";
        con = null;
        ps = null;
        rs = null;
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Consolidation consolidation;
        for(rs = ps.executeQuery(); rs.next(); records.add(consolidation))
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String bank = rs.getString("BANK");
            String fax = rs.getString("FAX");
            String personno = rs.getString("PERSONNO");
            String currency = rs.getString("CURRENCY");
            String description = rs.getString("DESCRIPTION");
            String address = rs.getString("ADDRESS");
            String contactPerson = rs.getString("CONTACT_PERSON");
            String accountNo = rs.getString("ACCOUNT_NO");
            String status = rs.getString("STATUS");
            String sort = rs.getString("SORT");
            consolidation = new Consolidation(id, code, bank, fax, personno, currency, description, address, contactPerson, accountNo, status, sort);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_306;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding All Consolidation...->").append(er.getMessage()).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_306;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return records;
    }

    public Consolidation findConsolidationByQuery(String bank)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Consolidation consolidation;
        FIND_QUERY = "SELECT MTID,CODE,BANK,FAX,PERSONNO,CURRENCY,DESCRIPTION,ADDRESS, CONTACT_PERSON," +
"ACCOUNT_NO,STATUS FROM IA_CONSOLIDATION WHERE  MTID=?"
;
        con = null;
        ps = null;
        rs = null;
        consolidation = null;
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, bank);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String id = rs.getString("MTID");
            String code = rs.getString("CODE");
            String fax = rs.getString("FAX");
            String personno = rs.getString("PERSONNO");
            String currency = rs.getString("CURRENCY");
            String description = rs.getString("DESCRIPTION");
            String address = rs.getString("ADDRESS");
            String contactPerson = rs.getString("CONTACT_PERSON");
            String accountNo = rs.getString("ACCOUNT_NO");
            String status = rs.getString("STATUS");
            String sort = rs.getString("SORT");
            consolidation = new Consolidation(id, code, bank, fax, personno, currency, description, address, contactPerson, accountNo, status, sort);
        }

        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_287;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error finding ConsolidationByID ->").append(er).toString());
        closeConnection(con, ps, rs);
        break MISSING_BLOCK_LABEL_287;
        Exception exception;
        exception;
        closeConnection(con, ps, rs);
        throw exception;
        return consolidation;
    }

    public boolean isConsolidationExisting(String bank)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        Consolidation consolidation = null;
        exists = false;
        updateQuery = "SELECT count(BANK) FROM IA_CONSOLIDATION WHERE  BANK = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, bank);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_147;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isConsolidationExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_147;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }

    public void createBank(String code, String name, String fax, String personno, String currency, String description, String address, 
            String contactPerson, String accountNo, String status)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_BANK( MTID,CODE,NAME,FAX,PERSONNO,CURRENCY,DESCRIPTION,ADDRESS,CO" +
"NTACT_PERSON,ACCOUNT_NO,STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?)"
;
        con = null;
        ps = null;
        id = helper.getGeneratedId("IA_BANK");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setString(3, name);
        ps.setString(4, fax);
        ps.setString(5, personno);
        ps.setString(6, currency);
        ps.setString(7, description);
        ps.setString(8, address);
        ps.setString(9, contactPerson);
        ps.setString(10, accountNo);
        ps.setString(11, status);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_228;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error creating Bank... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_228;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void deleteBank(String id)
    {
        String DELETE_QUERY;
        Connection con;
        PreparedStatement ps;
        DELETE_QUERY = "DELETE FROM IA_BANK  WHERE ID = ?";
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(DELETE_QUERY);
        ps.setString(1, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_103;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error Deleting Bank... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_103;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public void updateBank(String id, String code, String name, String fax, String personno, String currency, String description, 
            String address, String contactPerson, String accountNo, String status)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_BANK SET CODE=?,NAME=?,FAX = ?,PERSONNO = ?,CURRENCY = ?,DESCRIPTION=?" +
",ADDRESS=?, CONTACT_PERSON=?,ACCOUNT_NO=?,STATUS = ?  WHERE MTID = ?"
;
        con = null;
        ps = null;
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setString(2, name);
        ps.setString(3, fax);
        ps.setString(4, personno);
        ps.setString(5, currency);
        ps.setString(6, description);
        ps.setString(7, address);
        ps.setString(8, contactPerson);
        ps.setString(9, accountNo);
        ps.setString(10, status);
        ps.setString(11, id);
        ps.execute();
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_216;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error UPDATING Bank... ->").append(er.getMessage()).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_216;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
    }

    public boolean isBankExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        com.magbel.ia.vao.Bank bank = null;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM IA_BANK WHERE  MTID = ?";
        con = null;
        ps = null;
        ResultSet rs = null;
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_147;
        Exception er;
        er;
        System.out.println((new StringBuilder()).append("Error in isBankExisting()... ->").append(er).toString());
        closeConnection(con, ps);
        break MISSING_BLOCK_LABEL_147;
        Exception exception;
        exception;
        closeConnection(con, ps);
        throw exception;
        return exists;
    }
*/
    public void createApproval(String code, double min, double max, String desc)
    {
        String CREATE_QUERY;
        Connection con;
        PreparedStatement ps;
        String id;
        CREATE_QUERY = "INSERT INTO IA_APPROVAL_LEVEL (MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT,DESCRIPTION" +
") VALUES(?,?,?,?,?)"
;
        System.out.println(CREATE_QUERY);
        con = null;
        ps = null;
        try {
        id = helper.getGeneratedId("IA_APPROVAL_LEVEL");
        con = getConnection();
        ps = con.prepareStatement(CREATE_QUERY);
        ps.setString(1, id);
        ps.setString(2, code);
        ps.setDouble(3, min);
        ps.setDouble(4, max);
        ps.setString(5, desc);
        ps.execute();
        closeConnection(con, ps);
    	}catch(Exception er){
        System.out.println((new StringBuilder()).append("Error creating Approval... ->").append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
	    }finally{
	    	closeConnection(con, ps);
		   }
    }

    public void updateApproval(String id, String code, double min, double max, String desc)
    {
        String UPDATE_QUERY;
        Connection con;
        PreparedStatement ps;
        UPDATE_QUERY = "UPDATE IA_APPROVAL_LEVEL SET LEVEL_CODE= ?,MIN_AMOUNT=?,MAX_AMOUNT=?,DESCRIPTION" +
"=? WHERE  MTID = ?"
;
        System.out.println(UPDATE_QUERY);
        con = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, code);
        ps.setDouble(2, min);
        ps.setDouble(3, max);
        ps.setString(4, desc);
        ps.setString(5, id);
        ps.execute();
        closeConnection(con, ps);
        }catch(Exception er){
        System.out.println((new StringBuilder()).append("Error UPDATING Approval... ->").append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps);
	    }finally{
	    	closeConnection(con, ps);
		   }
    }

    public ArrayList findAllApproval()
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        records = new ArrayList();
        SELECT_QUERY = "SELECT MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT,DESCRIPTION FROM ST_APPROVAL_LEVEL";
        System.out.println(SELECT_QUERY);
        con = null;
        ps = null;
        rs = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(SELECT_QUERY);
        Approval approval;
        for(rs = ps.executeQuery(); rs.next(); records.add(approval))
        {
            String code = rs.getString("LEVEL_CODE");
            double min = rs.getDouble("MIN_AMOUNT");
            double max = rs.getDouble("MAX_AMOUNT");
            String desc = rs.getString("DESCRIPTION");
            String id = rs.getString("MTID");
            approval = new Approval(id, code, min, max, desc);
        }

        closeConnection(con, ps, rs);
    	}catch(Exception er){
        System.out.println((new StringBuilder()).append("Error finding All Approval...->").append(er.getMessage()).toString());
        er.printStackTrace();
        closeConnection(con, ps, rs);
	    }finally{
	    	closeConnection(con, ps, rs);
		   }
        return records;
    }

    public Approval findApprovalById(String id)
    {
        String FIND_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Approval approval;
        FIND_QUERY = "SELECT MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT,DESCRIPTION FROM ST_APPROVAL_LEVEL " +
"WHERE  MTID=?"
;
        System.out.println(FIND_QUERY);
        con = null;
        ps = null;
        rs = null;
        approval = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(FIND_QUERY);
        ps.setString(1, id);
        for(rs = ps.executeQuery(); rs.next();)
        {
            String code = rs.getString("LEVEL_CODE");
            double min = rs.getDouble("MIN_AMOUNT");
            double max = rs.getDouble("MAX_AMOUNT");
            String desc = rs.getString("DESCRIPTION");
            approval = new Approval(id, code, min, max, desc);
        }

        closeConnection(con, ps, rs);
        }catch(Exception er){
        System.out.println((new StringBuilder()).append("Error finding ApprovalByID ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps, rs);
	    }finally{
	    	closeConnection(con, ps, rs);
		   }
        return approval;
    }

    public boolean isApprovalExisting(String id)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(MTID) FROM ST_APPROVAL_LEVEL WHERE  MTID = ?";
        System.out.println(updateQuery);
        con = null;
        ps = null;
        ResultSet rs = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, id);
        rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
        }catch(Exception er){
        System.out.println((new StringBuilder()).append("Error in isApprovalExisting()... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
	    }finally{
		   	 closeConnection(con, ps);
		   }
        return exists;
    }

    public boolean isApprovalLevelExisting(String code)
    {
        boolean exists;
        String updateQuery;
        Connection con;
        PreparedStatement ps;
        exists = false;
        updateQuery = "SELECT count(LEVEL_CODE) FROM ST_APPROVAL_LEVEL WHERE  LEVEL_CODE = ?";
        System.out.println(updateQuery);
        con = null;
        ps = null;
        ResultSet rs = null;
        try{
        con = getConnection();
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, code);
        rs = ps.executeQuery();
        do
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
        closeConnection(con, ps);
	    }catch(Exception er){
        System.out.println((new StringBuilder()).append("Error in isApprovalLevelExisting()... ->").append(er).toString());
        er.printStackTrace();
        closeConnection(con, ps);
	    }finally{
	   	 closeConnection(con, ps);
	   }
        return exists;
    }
}
