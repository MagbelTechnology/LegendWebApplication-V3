package com.magbel.ia.legend.admin.handlers;
  
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*; 
import magma.net.dao.MagmaDBConnection;

//import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.legend.admin.objects.Menu;

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
		
		String query = "SELECT MAX(Levels)" 
				+ "  FROM am_ad_privileges ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int max =1;

		try {
			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				max= rs.getInt("1");
				
			}
		} catch (Exception ex) {
			System.out.println("WARN: Error fetching MENU XXX ->" + ex);
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return max;

	}

	
	public List<Menu> findMenus(String classCode) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.ia.legend.admin.objects.Menu menu = null;
		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid ,A.Levels" 
				+ "  FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "+
				" A.LEVELS = 1 AND B.CLSS_UUID = '"
				+ classCode+"' ORDER BY LEVELS";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl= rs.getString("role_wurl") == null? "":rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int level= rs.getInt("Levels");
				//Set<Menu> children=Major;
				menu = new com.magbel.ia.legend.admin.objects.Menu( menuId,  menuName,
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

	public List<Menu> findMenusChild(String uid, int level,String classCode) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.ia.legend.admin.objects.Menu menu = null;
		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid,A.Levels " 
				+ " FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "
				+ " A.parentid="+uid+" AND A.LEVELS="+level +" AND B.CLSS_UUID = '"
					+ classCode+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl= rs.getString("role_wurl") == null? "":rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int levelx= rs.getInt("Levels");
				//Set<Menu> children=Major;
				menu = new com.magbel.ia.legend.admin.objects.Menu( menuId,  menuName,
						 menuUrl,  parentId,level);
				menu.setChildren(findMenusChildLevel(menuId,levelx+1,classCode));
				
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
	
	public List<Menu> findMenusChildLevel(String uid, int level,String classCode) {
		List<Menu> _list = new java.util.ArrayList<Menu>();
		com.magbel.ia.legend.admin.objects.Menu menu = null;
		String query = "SELECT A.role_uuid,A.role_name,A.role_wurl"
				+ " ,A.priority,A.parentid,A.Levels " 
				+ "  FROM am_ad_privileges A, am_AD_CLASS_PRIVILEGES B "
				+ " WHERE B.ROLE_UUID = A.ROLE_UUID   AND "+
				"A.parentid="+uid+" AND A.LEVELS="+level +" AND B.CLSS_UUID = '"
				+ classCode+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			con = getConnection("helpDesk");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String menuId = rs.getString("role_uuid");
				String menuName= rs.getString("role_name");
				String menuUrl=rs.getString("role_wurl") == null? "": rs.getString("role_wurl");
				String parentId= rs.getString("parentid");
				int levelx= rs.getInt("Levels");
				//Set<Menu> children=Major;
				menu = new com.magbel.ia.legend.admin.objects.Menu( menuId,  menuName,
						 menuUrl,  parentId,level);
				menu.setChildren(findMenusChildLevel(menuId,levelx+1,classCode));
				
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
	

	

public static void main(String [] args)	{
	MenuHandler sec = new MenuHandler();
	java.util.List list = sec.findMenus("5");
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





}
