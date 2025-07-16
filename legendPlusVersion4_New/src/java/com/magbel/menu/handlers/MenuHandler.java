package com.magbel.menu.handlers;
  
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*; 

import magma.net.dao.MagmaDBConnection;


//import com.magbel.ia.util.ApplicationHelper;
import com.magbel.menu.objects.Menu;

/**
 * <p>
 * Title: MenuHandler.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Magbel Technology.
 * </p>
 * 
 * @author Rahman O. Oloritun
 * @version 1.0
 * 
 * @Entities user,security privileges, userclass
 */
public class MenuHandler extends MagmaDBConnection {

	SimpleDateFormat sdf;

	final String space = "  ";

	final String comma = ",";
	
	List<Menu> Major;

	java.util.Date date;

	com.magbel.util.DatetimeFormat df;
	
	 MagmaDBConnection dbConnection = new MagmaDBConnection();

	/**
	 * constructor
	 * 
	 */

	public MenuHandler() {
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		df = new com.magbel.util.DatetimeFormat();
		//System.out.println("[INFO] USING_ " + this.getClass().getName());
	}

	/**
	 * <p>
	 * Description:Returns all security privileges
	 * </p>
	 * 
	 * @return java.util.List
	 */
	
	public int findMaxLevel() {  
		
		String query = "SELECT MAX(Level)" 
				+ "  FROM am_ad_privileges ";

		Connection con = null;
		Statement ps = null;
		ResultSet rs = null;
		int max =1;

		try {
			con = getConnection("legendPlus");
			//con = dbConnection.getConnection("legendPlus");
			//ps = con.prepareStatement(query);
			ps = con.createStatement();
			rs = ps.executeQuery(query);
			while (rs.next()) {
				
				max= rs.getInt("1");
				
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching MENU XXX ->" + ex);
			ex.getMessage();
		} finally {
			closeConnection(con, ps, rs);
		}
		return max;

	}
  
	
	public List<Menu> findMenus(String classCode, String applicationType) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.menu.objects.Menu menu = null;

		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid ,A.Level" 
				+ "  FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "+
				" A.LEVEL = 1 AND B.CLSS_UUID = ? "+ applicationType+" ORDER BY LEVEL,A.role_uuid";
//		" A.LEVEL = 1 AND B.CLSS_UUID = '"
//				+ classCode+"' "+ applicationType+" ORDER BY LEVEL,A.role_uuid";
//		System.out.println("<<<<<findMenus Query: "+query);
//		System.out.println("<<<<<findMenus  applicationType: "+applicationType);
		Connection con = null;
//		Statement ps = null; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			con = getConnection("legendPlus");
			//con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, classCode);
            rs = ps.executeQuery();
			
//			ps = con.createStatement();
//			rs = ps.executeQuery(query);
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl= rs.getString("role_wurl") == null? "":rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int level= rs.getInt("Level");
				//Set<Menu> children=Major;
				menu = new com.magbel.menu.objects.Menu( menuId,  menuName,
						 menuUrl,  parentId,level);
				menu.setChildren(findMenusChild(menuId,level+1,classCode));
				_list.add(menu);

			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching MENU XXX ->" + ex);
			ex.getMessage();
		} finally {
			closeConnection(con, ps, rs);
		}
		return _list;

	}   

	public List<Menu> findMenusChild(String uid, int level,String classCode) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.menu.objects.Menu menu = null;
		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid,A.Level " 
				+ " FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "
				+ " A.parentid= ? AND A.LEVEL=? AND B.CLSS_UUID = ? ORDER BY A.role_name ";
//		System.out.println("<<<<<findMenusChild Query: "+query);
		Connection con = null;
//		Statement ps = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			con = getConnection("legendPlus");
			//con = dbConnection.getConnection("legendPlus");
//			ps = con.createStatement();
//			rs = ps.executeQuery(query);
			
            ps = con.prepareStatement(query);
            ps.setString(1, uid);
            ps.setInt(2, level);
            ps.setString(3, classCode);
            rs = ps.executeQuery();
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl= rs.getString("role_wurl") == null? "":rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int levelx= rs.getInt("Level");
				//Set<Menu> children=Major;
				menu = new com.magbel.menu.objects.Menu( menuId,  menuName,
						 menuUrl,  parentId,level);
				menu.setChildren(findMenusChildLevel(menuId,levelx+1,classCode));
				
				_list.add(menu);

			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching MENU XXX ->" + ex);
			ex.getMessage();
		}
		finally {
			closeConnection(con, ps, rs);
		}
		return _list;

	}
	
	public List<Menu> findMenusChildLevel(String uid, int level,String classCode) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.menu.objects.Menu menu = null;
		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid,A.Level " 
				+ "  FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "+
				"A.parentid=? AND A.LEVEL=? AND B.CLSS_UUID = ?  ORDER BY A.role_name ";
//		System.out.println("<<<<<findMenusChildLevel Query: "+query);
		Connection con = null;
//		Statement ps = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
//		System.out.println("<<<<<findMenusChildLevel Query: "+query);
		try {
			con = getConnection("legendPlus");
//			//con = dbConnection.getConnection("legendPlus");
//			ps = con.createStatement();
//			rs = ps.executeQuery(query);
            ps = con.prepareStatement(query);
            ps.setString(1, uid);
            ps.setInt(2, level);
            ps.setString(3, classCode);
            rs = ps.executeQuery();
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl=rs.getString("role_wurl") == null? "": rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int levelx= rs.getInt("Level");
				//Set<Menu> children=Major;
				menu = new com.magbel.menu.objects.Menu( menuId,  menuName,
						 menuUrl,  parentId,level);
				menu.setChildren(findMenusChildLevel(menuId,levelx+1,classCode));
				
				_list.add(menu);

			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching MENU XXX ->" + ex);
			ex.getMessage();
		} 
		finally {
			closeConnection(con, ps, rs);
		}
		return _list;

	}
	

	

public static void main(String [] args)	{
	MenuHandler sec = new MenuHandler();
	java.util.List list = sec.findMenus("5","450");
	for(Iterator it=list.iterator(); it.hasNext();)
	{
		Menu m =(Menu)it.next();
		System.out.println(m.getMenuName());
		Iterator ix = m.getChildren().iterator();
		while(ix.hasNext())
		{
			Menu mx =(Menu)ix.next();
			System.out.println("\t"+mx.getMenuName());
			Iterator ig = mx.getChildren().iterator();
			while(ig.hasNext())
			{
				Menu my =(Menu)ig.next();
				System.out.println("\t\t"+my.getMenuName());
				Iterator iz = my.getChildren().iterator();
				while(iz.hasNext())
				{
					Menu mz =(Menu)iz.next();
					System.out.println("\t\t\t"+mz.getMenuName());
					Iterator io = mz.getChildren().iterator();
					while(io.hasNext())
					{
						Menu mo =(Menu)io.next();
						System.out.println("\t\t\t\t"+mo.getMenuName());
						
					}	
				}
				
			}
			
		}
		
		
	}
	
	

}



public List<Menu> findMenus(String classCode) {
	List<Menu> _list = new java.util.ArrayList<Menu>();
	com.magbel.menu.objects.Menu menu = null;
	String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
			+ " ,A.priority,A.parentid ,A.Level" 
			+ "  FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
			+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "+
			" A.LEVEL = 1 AND B.CLSS_UUID = ? ORDER BY LEVEL,A.role_uuid";
//	System.out.println("<<<<<findMenus Query: "+query);
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
 
	try {
		con = getConnection("legendPlus");
//		ps = con.prepareStatement(query);
//		rs = ps.executeQuery();
        ps = con.prepareStatement(query);
        ps.setString(1, classCode);
        rs = ps.executeQuery();
		while (rs.next()) {
			String menuId = rs.getString("role_uuid");
			String menuName= rs.getString("role_name");
			String menuUrl= rs.getString("role_wurl") == null? "":rs.getString("role_wurl");
			String parentId= rs.getString("parentid");
			int level= rs.getInt("Level");
			//Set<Menu> children=Major;
			menu = new com.magbel.menu.objects.Menu( menuId,  menuName,
					 menuUrl,  parentId,level);
			menu.setChildren(findMenusChild(menuId,level+1,classCode));
			_list.add(menu);

		}
	} catch (Exception ex) {
		System.out.println("WARN: Error fetching MENU XXX ->" + ex);
		ex.printStackTrace();
	} finally {
		closeConnection(con, ps);
	}
	return _list;

}   




}
