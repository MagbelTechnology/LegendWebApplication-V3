package com.magbel.ia.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.dao.ConnectManager;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.vao.Codes;
import com.magbel.ia.vao.ModuleCodes;
import com.magbel.util.DatetimeFormat;
//ConnectManager
public class CodeGenerator extends PersistenceServiceDAO {

	DatetimeFormat dateFormat;

	SimpleDateFormat sdf;

	private AdminServiceBus serviceBus;

	final String COMPANY = "COMP";

	final String BRANCH = "BRCH";

	final String REGION = "REGN";

	final String DEPARTMENT = "DEPT";

	final String SECTION = "SECT";

	public CodeGenerator() {
		serviceBus = new AdminServiceBus();
	}

	public boolean createCodes(String priority1, String priority2,
			String priority3, String priority4, String priority5,
			String priority6, String delimiter) {

		String UPDATE_QUERY = "INSERT INTO ST_CODEGEN_SETUP (PRIORITY1, "
				+ "PRIORITY2, PRIORITY3, PRIORITY4, PRIORITY5, "
				+ "PRIORITY6, DELIMITER) VALUES(?,?,?,?,?,?,?)";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, priority1);
			ps.setString(2, priority2);
			ps.setString(3, priority3);
			ps.setString(4, priority4);
			ps.setString(5, priority5);
			ps.setString(6, priority6);
			ps.setString(7, delimiter);

			done = (ps.executeUpdate() == -1);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	public boolean resetCodes() {

		String UPDATE_QUERY = "DELETE FROM ST_CODEGEN_SETUP ";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);

			done = (ps.executeUpdate() == -1);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	public boolean updateModule(String moduleName, String moduleAbv,
			String is_prefix, int startNo) {

		String UPDATE_QUERY = "UPDATE ST_MODULES_CODES SET "
				+ " MODULE_ABV=?, IS_PREFIX=?, "
				+ "START_NO=? WHERE MODULE_NAME=?";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, moduleAbv);
			ps.setString(2, is_prefix);
			ps.setInt(3, startNo);
			ps.setString(4, moduleName);
			done = (ps.executeUpdate() == -1);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	public boolean notifyModule(String moduleName) {

		String UPDATE_QUERY = "UPDATE ST_MODULES_CODES SET "
				+ "START_NO=START_NO+? WHERE MODULE_NAME=?";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean done = false;

		try {

			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setInt(1, 1);
			ps.setString(2, moduleName);
			done = (ps.executeUpdate() == -1);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return done;
	}

	public ArrayList<ModuleCodes> findModules(String filter) {

		String FIND_QUERY = "SELECT MODULE_NAME, MODULE_ABV, IS_PREFIX, START_NO FROM ST_MODULES_CODES  ";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		ModuleCodes module = null;
		ArrayList<ModuleCodes> finder = new ArrayList<ModuleCodes>();

		FIND_QUERY += filter;
//		System.out.println("<<<<<FIND_QUERY in findModules: "+FIND_QUERY);
		try {

			con = getConnection();
			ps = con.prepareStatement(FIND_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {  
				String moduleName = rs.getString("MODULE_NAME");
				String moduleAbv = rs.getString("MODULE_ABV");
				String is_prefix = rs.getString("IS_PREFIX");
				int startNo = rs.getInt("START_NO");

				module = new ModuleCodes(moduleName, moduleAbv, is_prefix,
						startNo);

				finder.add(module);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return finder;
	}

	public ModuleCodes findModuleByName(String moduleName) {
		String filter = "WHERE MODULE_NAME='" + moduleName + "'";
		ArrayList<ModuleCodes> _list = findModules(filter);
		ModuleCodes mcodes = _list.iterator().next();
		return mcodes;
	}

	public Codes findCodes(String filter) {

		String FIND_QUERY = "SELECT PRIORITY1, PRIORITY2, PRIORITY3, "
				+ "PRIORITY4, PRIORITY5, PRIORITY6, "
				+ "DELIMITER FROM ST_CODEGEN_SETUP  ";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		Codes code = null;
		// ArrayList<Codes> finder = new ArrayList<Codes>();

		FIND_QUERY += filter;

		try {

			con = getConnection();
			ps = con.prepareStatement(FIND_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {

				String priority1 = rs.getString("PRIORITY1");
				String priority2 = rs.getString("PRIORITY2");
				String priority3 = rs.getString("PRIORITY3");
				String priority4 = rs.getString("PRIORITY4");
				String priority5 = rs.getString("PRIORITY5");
				String priority6 = rs.getString("PRIORITY6");
				String delimiter = rs.getString("DELIMITER");

				code = new Codes(priority1, priority2, priority3, priority4,
						priority5, priority6, delimiter);

				// finder.add(code);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WARN:Error Saving code setup " + ex);
		} finally {
			closeConnection(con, ps, rs);
		}
		return code;
	}

	public String getProperties(String sele, String[] iden, String[] vals) {
		String html = new String();
		if (sele == null) {
			sele = " ";
		}
		for (int i = 0; i < iden.length; i++) {
			html = html + "<option value='" + iden[i] + "' "
					+ (iden[i].equalsIgnoreCase(sele) ? " selected " : "")
					+ ">" + vals[i] + "</option> ";
		}

		return html;

	}

		public String generateCode2(String moduleName, String branch_id,String department_id, String section_id) 
		{
		StringBuffer generatedCode = new StringBuffer();
		String prex="";
		int start =0;
		Codes code = this.findCodes("");
   
		//System.out.println("<<<<<<Module Name: "+moduleName);
		ModuleCodes mcode = this.findModuleByName(moduleName);
		String temp = mcode.getModuleAbv();
	    start = mcode.getStartNo();
		prex = mcode.getIs_prefix();
		
		if(prex.equalsIgnoreCase("Y"))
		{
			temp+=code.getDelimiter()==null?"":code.getDelimiter();
			generatedCode.insert(0, temp);
		}
		else
		{
			generatedCode.append(temp);
			
		}
		generatedCode.append(start);
		
		notifyModule(moduleName);
		return generatedCode.toString();
	}
	
	
	
	
	
	
	
	
	
	
	public String generateCode(String moduleName, String branch_id,
			String department_id, String section_id) {
		StringBuffer generatedCode = new StringBuffer();
		 String prex="";
		int start =0;
		Codes code = this.findCodes("");
		System.out.println("<<<<<<moduleName: "+moduleName);
              /*
		if((code.getPriority1()!=null) && (!code.getPriority1().equals(""))){
		String temp = getAcronyms(code.getPriority1().trim(),branch_id,department_id,section_id);
		if(!temp.equals("")){
		//generatedCode.append(temp);
                generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}
		if((code.getPriority2()!=null) && (!code.getPriority2().equals(""))){
		String temp = getAcronyms(code.getPriority2().trim(),branch_id,department_id,section_id);
		if(!temp.equals("")){
		generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}
		
		if((code.getPriority3()!=null) && (!code.getPriority3().equals(""))){
		String temp = getAcronyms(code.getPriority3().trim(),branch_id,department_id,section_id);
		if(!temp.equals("")){
		generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}
		
		if((code.getPriority4()!=null) && (!code.getPriority4().equals(""))){
		String temp = getAcronyms(code.getPriority4().trim(),branch_id,department_id,section_id);
		if(!temp.equals("")){
		generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}
		*/
		/*if((code.getPriority5()!=null) && (!code.getPriority5().equals(""))){
		String temp = getAcronyms(code.getPriority5().trim(),branch_id,department_id,section_id);
		 if((temp!=null)&&(!temp.equals(""))){
		generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}*/
		/*if((code.getPriority6()!=null) && (!code.getPriority6().equals(""))){
		String temp = getAcronyms(code.getPriority6().trim(),branch_id,department_id,section_id);
		if(!temp.equals("")){
		generatedCode.append(temp==null?"":temp);
		generatedCode.append(code.getDelimiter());}
		}*/
		
		ModuleCodes mcode = this.findModuleByName(moduleName);
		String temp = mcode.getModuleAbv();
	        start = mcode.getStartNo();
		prex = mcode.getIs_prefix();
		if(prex.equalsIgnoreCase("Y")){
			temp+=code.getDelimiter()==null?"":code.getDelimiter();
			generatedCode.insert(0, temp);
		}
		else
		{
			
			generatedCode.append(temp);
			generatedCode.append(code.getDelimiter());
		}
		
		generatedCode.append(start);
		
		notifyModule(moduleName);
		return generatedCode.toString();
	}

	private String getAcronyms(String priority, String branch_id,
			String department_id, String section_id) {
		String Acronym;
		if (priority.equalsIgnoreCase(COMPANY)) {
			Acronym = serviceBus.findCompany().getAcronym();
		} else if (priority.equalsIgnoreCase(BRANCH)) {
			Acronym = serviceBus.findBranchByBranchID(branch_id)==null?"":
				 serviceBus.findBranchByBranchID(branch_id).getBranchAcronym();

		} else if (priority.equalsIgnoreCase(DEPARTMENT)) {
			Acronym = serviceBus.findDeptByDeptID(department_id)==null?"":
						serviceBus.findDeptByDeptID(department_id).getDept_acronym();

		} else if (priority.equalsIgnoreCase(SECTION)) {
			Acronym = serviceBus.findSectionByID(section_id)==null?"":
				 serviceBus.findSectionByID(section_id).getSection_acronym();

		} else if (priority.equalsIgnoreCase(REGION)) {
			String regid = serviceBus.findBranchByBranchID(branch_id)==null?"":
				serviceBus.findBranchByBranchID(branch_id).getRegion();
			Acronym = serviceBus.findRegionByCode(regid)==null?"":serviceBus.findRegionByID(regid).getRegionAcronym();
		} else {
			Acronym = priority;
		}

		return Acronym;

	}
	
		
}
