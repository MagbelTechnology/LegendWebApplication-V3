package legend.admin.handlers; 

import com.magbel.util.ApplicationHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;
import java.sql.SQLException;

/**
 * @author Rahman Oloritun
 * @Entities company,AssetmanagerInfo,Driver,Location,categoryClasses, ASSETMAKE
 */
public class OtherHandler {
	Connection con = null;

	Statement stmt = null;
 
	PreparedStatement ps = null;

	ResultSet rs = null;

	DataConnect dc;

	SimpleDateFormat sdf;

	final String space = "  ";

	final String comma = ",";  

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;

	public OtherHandler() {

		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		System.out.println("USING_ " + this.getClass().getName());
	}


	private Connection getConnection() {
		Connection con = null;
		dc = new DataConnect("fixedasset");
		try {
			con = dc.getConnection();
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection ->"
					+ e.getMessage());
		}
		return con;
	}

	private void closeConnection(Connection con, Statement s) {
		try {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error getting connection ->"
					+ e.getMessage());
		}

	}

	private void closeConnection(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}

	}

	/**
	 * 
	 * @param con
	 *            Connection
	 * @param s
	 *            Statement
	 * @param rs
	 *            ResultSet
	 */
	private void closeConnection(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @param con
	 *            Connection
	 * @param ps
	 *            PreparedStatement
	 * @param rs
	 *            ResultSet
	 */
	private void closeConnection(Connection con, PreparedStatement ps,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}
	public boolean createPrivileges(
			legend.admin.objects.PrivilegeAssign privilege) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
	/*	System.out.println("About to Generate roleId?????=== ");		
		System.out.println("getRoleName=== "+privilege.getRoleName());
		System.out.println("getRoleUrl=== "+privilege.getRoleUrl());
		System.out.println("getMenuType=== "+privilege.getMenuType());
		System.out.println("getParentId=== "+privilege.getParentId());
		System.out.println("getLevel=== "+privilege.getLevel());
		*/
		String roleId =new ApplicationHelper().getGeneratedId("am_ad_privileges");
	//	System.out.println("roleId?????=== "+roleId);
		String query = "INSERT INTO am_ad_privileges(role_uuid, "
				+ "role_name, role_wurl, menu_type,parentid,levels) "
				+ " VALUES(?, ?, ?, ?, ?, ?)";	
		try {
			con = getConnection(); 
			ps = con.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(roleId));
			ps.setString(2, privilege.getRoleName());
			ps.setString(3, privilege.getRoleUrl());
			ps.setString(4, privilege.getMenuType());
		//	ps.setInt(5, Integer.parseInt(privilege.getPriority()));			
			ps.setInt(5, Integer.parseInt(privilege.getParentId()));
			ps.setInt(6, Integer.parseInt(privilege.getLevel()));
		//	ps.setDate(8, df.dateConvert(privilege.getCreateDate()));
   
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing createPrivileges Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean updatePrivilege(
			legend.admin.objects.PrivilegeAssign privilege) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE am_ad_privileges SET role_name=?, "
				+ "role_wurl=?, menu_type=?, parentid=?, level=?"
				+ " WHERE role_uuid=?";
	//	System.out.println("====updatePrivilege query===> "+query);

		try {
			con = getConnection();
			ps = con.prepareStatement(query);

			ps.setString(1, privilege.getRoleName());
			ps.setString(2, privilege.getRoleUrl());
			ps.setString(3, privilege.getMenuType());
		//	ps.setInt(5, Integer.parseInt(privilege.getPriority()));			
			ps.setInt(4, Integer.parseInt(privilege.getParentId()));
			ps.setInt(5, Integer.parseInt(privilege.getLevel()));
			ps.setInt(6, Integer.parseInt(privilege.getRoleId()));
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

	public boolean deletePrivileges(String RoleId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false; 
		String query = "DELETE FROM am_ad_privileges WHERE role_uuid= '"+RoleId+"'";
	//	System.out.println("====deletePrivileges query=====>>>> "+query);
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WARNING:Error executing deletePrivileges Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;
	}

 public  String getGeneratedTransId()
 {



	    int counter = 0;
	    String tableDate="";
	    String presentDate="";
		final String FINDER_QUERY = "SELECT MT_ID + 1,MT_DATE FROM IA_TRANSID_TABLE " ;
		final String UPDATE_QUERY = "UPDATE IA_TRANSID_TABLE SET MT_ID = 1 , MT_DATE= getdate() ";
		final String UPDATE = "UPDATE IA_TRANSID_TABLE SET MT_ID =MT_ID + 1  ";
		String id = "";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try{

			con = getConnection();
			ps = con.prepareStatement(FINDER_QUERY);
			rs = ps.executeQuery();

			while(rs.next())
			{
				tableDate= rs.getString(2);
				//System.out.println("-----> "+tableDate);
				tableDate=tableDate.substring(0, 10);
				presentDate = String.valueOf(df.dateConvert(new java.util.Date()));
				presentDate = presentDate.substring(0, 10);
				//System.out.println("-----> "+presentDate);
				counter=rs.getInt(1);
			}
				if(tableDate.equals(presentDate))
					{
 			          ps = con.prepareStatement(UPDATE);
				 	  ps.execute();
					}
				else{
					 counter=1;
					 ps = con.prepareStatement(UPDATE_QUERY);
			 	     ps.execute();
				    }



		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			closeConnection(con,ps,rs);
		}

		 id = Integer.toString(counter);
		 System.out.println(">>-->--> "+id);
		return id;

	   }

private Connection getConnectionFinacle() {
	Connection con = null;
	dc = new DataConnect("custom");
	try {
		con = dc.getConnection();
	} catch (Exception e) {
		System.out.println("WARNING: Error getting connection ->"
				+ e.getMessage());
	}
	return con;
}


public String getCodeName(String query) {
    String result = "";
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
//System.out.println("######query##### "+query);
    try { 
      //  con = getConnection("helpDesk");
    	con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        //System.out.println("===getCodeName in ApproalRecords query=="+query);
        while (rs.next()) {
            result = rs.getString(1) == null ? "" : rs.getString(1);

        }
    } catch (Exception er) {
        System.out.println("Error in Query- getCodeName()... ->" + er);
        er.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return result;
}


public java.util.ArrayList getPrivilegesByQuery(String filter)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.PrivilegeAssign privilege = null;
		String query = "select role_uuid, role_name, role_wurl, menu_type, priority, parentid, levels from am_ad_privileges"  + filter;


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				//System.out.println("i dey here o! 333333333");
				String roleuuid = rs.getString("role_uuid");
				String rolename = rs.getString("role_name");
				String rolewurl = rs.getString("role_wurl");
				String menutype = rs.getString("menu_type");
				String priority = rs.getString("priority");
				String parentid = rs.getString("parentid");
				String level = rs.getString("levels");
				//System.out.print("joshua !");
				privilege = new legend.admin.objects.PrivilegeAssign(roleuuid, rolename, rolewurl, menutype,priority,parentid,level);

				_list.add(privilege);

			}

		} catch (Exception e) {
			System.out.println(" Error Selecting privileges ->" + e.getMessage());			
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}


public java.util.ArrayList getPrivilegeByRoleId(String RoleId)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		legend.admin.objects.PrivilegeAssign privilege = null;
		String query = "select role_uuid, role_name, role_wurl, menu_type, priority, parentid, levels from am_ad_privileges where role_uuid = '"+RoleId+"'";


		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				//System.out.println("i dey here o! 333333333");
				String roleuuid = rs.getString("role_uuid");
				String rolename = rs.getString("role_name");
				String rolewurl = rs.getString("role_wurl");
				String menutype = rs.getString("menu_type");
				String priority = rs.getString("priority");
				String parentid = rs.getString("parentid");
				String level = rs.getString("levels");
				//System.out.print("joshua !");
				privilege = new legend.admin.objects.PrivilegeAssign(roleuuid, rolename, rolewurl, menutype,priority,parentid,level);

				_list.add(privilege);

			}

		} catch (Exception e) {
			System.out.println(" Error Selecting privileges ->" + e.getMessage());			
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}


}
