// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HtmlUtilily.java

package com.magbel.ia.util;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.magbel.ia.dao.PersistenceServiceDAO;


public class HtmlComboRecord  extends PersistenceServiceDAO {

public HtmlComboRecord() {
}

public String getYesNoForCombo(String selected) {

	String html;
	html = "";
	String id = "";
	if (selected == null) {
		selected = "";
	}

	html = html + "<option " +
				   ((selected != null) && (selected.equals("N")) ? " selected='true' " : "") +
				   " value='N' >No</option> "+
				   "<option  " +
				   ((selected != null) && (selected.equals("Y")) ? " selected='true' " : "") +
				   " value='Y'>Yes</option> ";


	return html;
}

public String getDescriptionCombo(String selected){

	String html = "";
	String query = "SELECT DESCRIPTION FROM IA_INDUSTRY ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}
public String getIcCodeCombo(String selected){

	String html = "";
	String query = "SELECT IC_CODE FROM IA_INDUSTRY ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}
public String getCurrencyCombo(String selected){

	String html = "";
	String query = "SELECT currency_id,description,currency_symbol FROM IA_GB_CURRENCY_CODE ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}


public String describeBranch(String branchCode){
	String query = "SELECT BRANCH_NAME FROM cs_gb_branch "+
				   "WHERE BRANCH_CODE = '"+branchCode+"'";
	return describeCode(query);
}

public String getStatusCombo(String selected){

	String html = "";
	String query = "SELECT STATUS_CODE,STATUS_NAME FROM IA_GB_STATUS ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}


public String getClubCombo(String selected){

	String html = "";
	String query = "SELECT Club_Code,Club_Name FROM CS_GB_Club_Information ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}

public String describeStatus(String code){
	String query = "SELECT STATUS_NAME FROM IA_GB_STATUS "+
				   "WHERE STATUS_CODE = '"+code+"'";
	return describeCode(query);
}

public String describeRole(String roleid){

	String query = "SELECT DESCRIPTION FROM cs_gb_Role "+
				   " WHERE CODE = '"+roleid+"'";
	return describeCode(query);

}

public String describeConfirmationStatus(String status){

	String description = "";
	if(status != null){

		if(status.equalsIgnoreCase("r")){
			description = " Rejected";
		}else if(status.equalsIgnoreCase("O")){
			description = "Pending";
		}else if(status.equalsIgnoreCase("E")){
			description = "Exception";
		}else if(status.equalsIgnoreCase("V")){
			description = "Verified";
		}else{
			description = "Approved";
		}
    }

    return description;
}

private String getOptionItems(String selected, String query) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String html;
		html = "";
		String id = "";
		if (selected == null || selected.equals("null")) {
			selected = "0";
		}
		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				id = rs.getString(1);
				html = html + "<option  " +
					   ((id != null) && id.equalsIgnoreCase(selected) ? " selected" : "") +
					   " value='" + id + "'>" +
					   rs.getString(2) + "</option> ";
			}

		} catch (Exception ee) {
			System.out.println("WARN:HtmlComboRecord.getOptionItems:->" + ee);
		} finally {
			closeConnection(con,ps,rs);
		}

		return html;
	}

private String describeCode(String query) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = "";

		try {

			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				description = rs.getString(1);
		    }

		} catch (Exception ee) {
			System.out.println("WARN:HtmlComboRecord.describeCode:->" + ee);
		} finally {
			closeConnection(con,ps,rs);
		}

		return description;
	}
}
