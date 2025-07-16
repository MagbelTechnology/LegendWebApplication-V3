
package com.magbel.util;
import com.magbel.dao.PersistenceServiceDAO;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;



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

public String getDesignationCombo(String selected){

	String html = "";
	String query = "SELECT CODE,DESCRIPTION FROM cp_gb_designation ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}

public String getBranchCombo(String selected){

	String html = "";
	String query = "SELECT SOL_ID,DESCRIPTION FROM cp_gb_branch ORDER BY 2 ";
	html = getOptionItems(selected,query);

	return html;
}
public String getBranchSolId(String selected){

	String html = "";
	String query = "SELECT SOL_ID,SOL_ID FROM cp_gb_branch ORDER BY 2 ";
	html = getOptionItems(selected,query);

	return html;
}
//for upload drop down
public String getZoneCode(String selected){

	String html = "";
	
	String query = "SELECT distinct (ZONE_CODE),ZONE_CODE FROM ccms.finacle_zone_table ORDER BY 2 ";
	html = getOptionItemsUba(selected,query);

	return html;
}

public String getSolId(String selected){

	String html = "";
	String query = "SELECT distinct (SOL_ID),SOL_ID FROM ccms.finacle_zone_table ORDER BY 2 ";
	html = getOptionItemsUba(selected,query);

	return html;
}





public String getCancelReason(String selected){

	String html = "";
	String query = "SELECT CODE,REASON FROM cp_cancel_reason ORDER BY 2 ";
	html = getOptionItems(selected,query);

	return html;
}

public String getSourceCombo(String selected)
{
	String html = "";
	String query = "SELECT CODE,DESCRIPTION FROM CP_CONFIRMATION_SOURCE ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}
public String getFrequencyCombo(String selected)
{
	String html = "";
	String query = "SELECT DESCRIPTION,DESCRIPTION FROM CP_FREQUENCY ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}
public String describeBranch(String branchCode){
	String query = "SELECT DESCRIPTION FROM cp_gb_branch "+
				   "WHERE SOL_ID = '"+branchCode+"'";
	return describeCode(query);
}

public String getStatusCombo(String selected){

	String html = "";
	String query = "SELECT CODE,DESCRIPTION FROM cp_gb_status ORDER BY 2";
	html = getOptionItems(selected,query);

	return html;
}

public String describeStatus(String code){
	String query = "SELECT DESCRIPTION FROM cp_gb_status "+
				   "WHERE CODE = '"+code+"'";
	return describeCode(query);
}

public String describeRole(String roleId){

	String query = "SELECT DESCRIPTION FROM cp_gb_designation "+
				   " WHERE CODE = '"+roleId+"'";
	return describeCode(query);

}
public String describeUserStatus(String status){

	String description = "";
	if(status != null){

		if(status.equalsIgnoreCase("O")){
			description = "On Leave";
		}else if(status.equalsIgnoreCase("C")){
			description = "Closed";
		}else if(status.equalsIgnoreCase("P")){
			description = "Pending";
		}
		else{
			description = "Active";
		}
    }

    return description;
}
/*public String describeEmpStatus(String status){

	String description = "";
	if(status != null){

		if(status.equalsIgnoreCase("O")){
			description = "On Leave";
		}else if(status.equalsIgnoreCase("C")){
			description = "Closed";
		}
		else{
			description = "Active";
		}
    }

    return description;
}*/
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
		}
		else if(status.equalsIgnoreCase("D")){
			description = "Deleted";
		}
		else{
			description = "Confirmed";
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
private String getOptionItemsUba(String selected, String query) {

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

		con = getConnection("finacleView");
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
public String getSupervisor(String selected){
	String html = "";
	String query = "SELECT DISTINCT EMPLOYEE_ID,FIRST_NAME FROM CP_GB_EMPLOYEE WHERE SUPERVISORIGHT = 'Y' ORDER BY 2";
	html = getOptionItems(selected,query);
	return html;
}
public String describeEmpFirsName(String empCode){
	String query = "SELECT FIRST_NAME FROM CP_GB_EMPLOYEE "+
				   "WHERE EMPLOYEE_ID = '"+empCode+"'";
	//FIRST_NAME,MIDDLE_NAME,SURNAME
	return describeCode(query);
}
public String describeEmpSurname(String empCode){
	String query = "SELECT SURNAME FROM CP_GB_EMPLOYEE "+
				   "WHERE EMPLOYEE_ID = '"+empCode+"'";
	//FIRST_NAME,MIDDLE_NAME,SURNAME
	return describeCode(query);
}
public String describeEmpMiddleName(String empCode){
	String query = "SELECT MIDDLE_NAME FROM CP_GB_EMPLOYEE "+
				   "WHERE EMPLOYEE_ID = '"+empCode+"'";
	//FIRST_NAME,MIDDLE_NAME,SURNAME
	return describeCode(query);
}
public String getJosh(String selected){

	String html = "";
	String query = "SELECT TRANS_CODE,DESCRIPTION FROM  Am_Transaction_Type ORDER BY 2 ";
	html = getOptionItems(selected,query);

	return html;
}
public String getTransaction(String selected){

	String html = "";
	String query = "SELECT Transaction_Type FROM User_Trans where ID = '"+selected+"' ";
	html = getOptionItems(selected,query);

	return html;
}


}
