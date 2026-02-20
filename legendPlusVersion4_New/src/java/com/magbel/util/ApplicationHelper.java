package com.magbel.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

//import legend.ConnectionClass;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;

import java.text.SimpleDateFormat;

import com.magbel.dao.PersistenceServiceDAO;

import legend.admin.objects.MandatoryField;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

/**
 * <p><h2>Project:</h2> <h1>Integrated Accounting Software</h1></p>
 <p>Title: ApplicationHelper.java</p>
 *
 * <p>Description:
 [1].To help in generating a uniquely sequencial
 ID for TABLES where records need to be inserted.<br><br>
 [2].To find and present a defined error message to
  a field identified as been mandated.</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: MagBel Technology LTD.</p>
 *
 * @author N.A
 * @version 1.0
 */
public class ApplicationHelper extends PersistenceServiceDAO{

    DatetimeFormat dateFormat;
    SimpleDateFormat sdf;


public static String getAllFieldErrors(ArrayList mandatoryFields,String formId,
									HttpServletRequest request){

	StringBuffer errorFields = new StringBuffer();

	if((mandatoryFields != null) && (mandatoryFields.size() > 0)){
			for(int x = 0;  x < mandatoryFields.size(); x++){

				MandatoryField mandatoryField = (MandatoryField)mandatoryFields.get(x);
				String fieldValue = request.getParameter(mandatoryField.getFormField());
					if(
						mandatoryField.getFlag().equalsIgnoreCase("T") &&
					   (fieldValue == null) || (fieldValue.equalsIgnoreCase(""))
					  ){
						errorFields.append("<li><font size='10' color='red'>");
						errorFields.append(mandatoryField);
						errorFields.append("</font></li>");
					}

			}
	}

	return errorFields.toString();
}

public static boolean isFieldMandated(ArrayList mandatoryFields,String formId,
									HttpServletRequest request){

	boolean isMandated = false;
	String[] fields = request.getParameterValues("");

	if(fields != null){
		for(int x = 0; x < fields.length; x++){
			if(isFieldMandated(mandatoryFields,fields[x])){
				isMandated = true;
				break;
			}
		}
	}

	return isMandated;

}

public static boolean isFieldMandated(ArrayList records,String formField){

	boolean isMandated = false;

	if((records != null) && (records.size() > 0)){
		for(int x = 0;  x < records.size(); x++){

			MandatoryField mandatoryField = (MandatoryField)records.get(x);
			if(mandatoryField.getFormField().equalsIgnoreCase(formField)){
				if(mandatoryField.getFlag().equalsIgnoreCase("T")){
					isMandated = true;
					break;
				}
		  }
		}
	}

	return isMandated;
}



public  String getGeneratedIdOld(String tableName) {

	dateFormat = new DatetimeFormat();
    sdf = new SimpleDateFormat("dd-MM-yyyy");

    int counter = 0;
	final String FINDER_QUERY = "SELECT MT_ID FROM IA_MTID_TABLE "+
							    "WHERE MT_TABLENAME = '"+tableName+"'";
	final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 "+
								"WHERE MT_TABLENAME = '"+tableName+"'";
	String id = "";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	PreparedStatement ps1 = null;

	try{

		con = getConnection();
		ps = con.prepareStatement(FINDER_QUERY);
		rs = ps.executeQuery();

		while(rs.next()){
			counter = rs.getInt(1);
		}

		ps1 = con.prepareStatement(UPDATE_QUERY);
		ps1.execute();


	}catch(Exception ex){
		System.out.println("WARN:Error generating id for table:"+
		tableName+"\n"+ex);
	}finally{
		closeConnection(con,ps,rs);
	}

	/*if(counter < 10){
		id = "0000"+Integer.toString(counter);
	}else if((counter > 9) && (counter < 100)){
		id = "000"+Integer.toString(counter);
	}else if((counter > 99) && (counter < 1000)){
		id = "00"+Integer.toString(counter);
	}else if((counter > 999) && (counter < 10000)){
		id = "0"+Integer.toString(counter);
	}else{
		id = Integer.toString(counter);
	}*/
	 id = Integer.toString(counter);
	return id;

   }

public String getGeneratedId(String tableName) {
    int counter = 0;
    String id = "";

    String FINDER_QUERY = "SELECT MT_ID FROM IA_MTID_TABLE WHERE MT_TABLENAME = ?";
    String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 WHERE MT_TABLENAME = ?";

    try (Connection con = getConnection()) {

        // Start a transaction for concurrency safety
        con.setAutoCommit(false);

        // Get current MT_ID
        try (PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    counter = rs.getInt(1);
                } else {
                    counter = 1;
                    String insertQuery = "INSERT INTO IA_MTID_TABLE(MT_TABLENAME, MT_ID) VALUES (?, ?)";
                    try (PreparedStatement psInsert = con.prepareStatement(insertQuery)) {
                        psInsert.setString(1, tableName);
                        psInsert.setInt(2, counter);
                        psInsert.executeUpdate();
                    }
                }
            }
        }

        // Increment MT_ID
        try (PreparedStatement psUpdate = con.prepareStatement(UPDATE_QUERY)) {
            psUpdate.setString(1, tableName);
            psUpdate.executeUpdate();
        }

        con.commit(); // commit the transaction

        //Format ID (zero-padded to 5 digits, e.g., 00001)
        id = String.format("%05d", counter);

    } catch (SQLException e) {
        e.printStackTrace();
        id = null; 
    }

    return id;
}
   
   public  String getGeneratedId2Old(String tableName) {

	dateFormat = new DatetimeFormat();
    sdf = new SimpleDateFormat("dd-MM-yyyy");

    int counter = 0;
	final String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE "+
							    "WHERE MT_TABLENAME = '"+tableName+"'";
	final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID  = MT_ID + 1 "+
								"WHERE MT_TABLENAME = '"+tableName+"'";
	String id = "";
	
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	try{

		con = getConnection();
		ps = con.prepareStatement(FINDER_QUERY);
		rs = ps.executeQuery();

		while(rs.next()){
			counter = rs.getInt(1);
		}

		ps = con.prepareStatement(UPDATE_QUERY);
		ps.execute();


	}catch(Exception ex){
		System.out.println("WARN:Error generating id for table:"+
		tableName+"\n"+ex);
	}finally{
		closeConnection(con,ps,rs);
	}

	if(counter < 10){
		id = "0000"+Integer.toString(counter);
	}else if((counter > 9) && (counter < 100)){
		id = "000"+Integer.toString(counter);
	}else if((counter > 99) && (counter < 1000)){
		id = "00"+Integer.toString(counter);
	}else if((counter > 999) && (counter < 10000)){
		id = "0"+Integer.toString(counter);
	}else{
		id = Integer.toString(counter);
	}

	return id;

   }

   public String getGeneratedId2(String tableName) {
	    int counter = 0;
	    String id = "";

	    String FINDER_QUERY = "SELECT MT_ID FROM IA_MTID_TABLE WHERE MT_TABLENAME = ?";
	    String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 WHERE MT_TABLENAME = ?";

	    try (Connection con = getConnection()) {
	        con.setAutoCommit(false); // start transaction

	        // Get current MT_ID
	        try (PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {
	            ps.setString(1, tableName);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    counter = rs.getInt(1) + 1; // MT_ID + 1
	                } else {
	                    counter = 1; // initialize if missing
	                    String insertQuery = "INSERT INTO IA_MTID_TABLE(MT_TABLENAME, MT_ID) VALUES (?, ?)";
	                    try (PreparedStatement psInsert = con.prepareStatement(insertQuery)) {
	                        psInsert.setString(1, tableName);
	                        psInsert.setInt(2, counter);
	                        psInsert.executeUpdate();
	                    }
	                }
	            }
	        }

	        // Increment MT_ID
	        try (PreparedStatement psUpdate = con.prepareStatement(UPDATE_QUERY)) {
	            psUpdate.setString(1, tableName);
	            psUpdate.executeUpdate();
	        }

	        con.commit(); 

	        // Format ID (zero-padded to 5 digits, e.g., 00001)
	        id = String.format("%05d", counter);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        id = null;
	    }

	    return id;
	}

public  String getSelectIdOld(String tableName) {

	dateFormat = new DatetimeFormat();
    sdf = new SimpleDateFormat("dd-MM-yyyy");

    int counter = 0;
	final String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE "+
							    "WHERE MT_TABLENAME = '"+tableName+"'";
//	final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 "+
//								"WHERE MT_TABLENAME = '"+tableName+"'";
	String id = "";

	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	try{

		con = getConnection();
		ps = con.prepareStatement(FINDER_QUERY);
		rs = ps.executeQuery();

		while(rs.next()){
			counter = rs.getInt(1);
		}

	}catch(Exception ex){
		System.out.println("WARN:Error generating id for table:"+
		tableName+"\n"+ex);
	}finally{
		closeConnection(con,ps,rs);
	}

	/*if(counter < 10){
		id = "0000"+Integer.toString(counter);
	}else if((counter > 9) && (counter < 100)){
		id = "000"+Integer.toString(counter);
	}else if((counter > 99) && (counter < 1000)){
		id = "00"+Integer.toString(counter);
	}else if((counter > 999) && (counter < 10000)){
		id = "0"+Integer.toString(counter);
	}else{
		id = Integer.toString(counter);
	}*/
	 id = Integer.toString(counter);
	return id;

   }

public String getSelectId(String tableName) {
    int counter = 0;
    String id = "";

    String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE WHERE MT_TABLENAME = ?";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {

        ps.setString(1, tableName);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) { 
                counter = rs.getInt(1);
            } else {
                counter = 1; 
            }
        }

        id = String.format("%05d", counter); // e.g., 00001

    } catch (SQLException e) {
        e.printStackTrace();
        id = null; 
    }

    return id;
}
   
}
