package com.magbel.legend.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class AjaxRequisitionServlet
 */
public class CategoryTypeReqnServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/xml";
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null; 
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryTypeReqnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
	//	con = mgDbCon.getConnection("legendPlus");
		String userClass = (String) request.getSession().getAttribute("UserClass");
		 if (!userClass.equals("NULL") || userClass!=null){
			 
		String IDType = request.getParameter("ID");
		System.out.println("Inside CategoryTypeReqnServlet: "+IDType);
		if (IDType.equalsIgnoreCase("item"))
		{
			categoryItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("pritem"))
		{ System.out.println("About to execute categoryPRItemTypeRequest: "+IDType);
			categoryPRItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Branch")) 
		{
			branchRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("BU")) 
		{
			buRequest(request,response);
		}	
		else if (IDType.equalsIgnoreCase("Requsr")) 
		{
			RequestUsrRequest(request,response);
		}		
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUser(request,response);
		}
                else if (IDType.equalsIgnoreCase("BranchByCode"))
		{
			branchRequestByCode(request,response);
		}
                else if (IDType.equalsIgnoreCase("category"))
		{
			categoryRequestByCode(request,response);
		}
		else if (IDType.equalsIgnoreCase("unit"))
		{
			measuringUnitRequest(request,response);
		}	
		else if (IDType.equalsIgnoreCase("ware"))
		{
			warehouseRequest(request,response);
		}	
		else if (IDType.equalsIgnoreCase("pack"))
		{
			unitPackRequest(request,response);
		}	
	//	mgDbCon.closeConnection(con, stmt, rs);		
	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IDType = request.getParameter("ID");
		
		String userClass = (String) request.getSession().getAttribute("UserClass");
		 if (!userClass.equals("NULL") || userClass!=null){
			 
		if (IDType.equalsIgnoreCase("item"))
		{
			categoryItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("pritem"))
		{ 
			categoryPRItemTypeRequest(request,response);
		}		
		else if (IDType.equalsIgnoreCase("Branch"))
		{
			branchRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUser(request,response);
		}else if (IDType.equalsIgnoreCase("BranchByCode"))
		{
			branchRequestByCode(request,response);
		}
	}
	}

	private void branchRequest(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
//		// TODO Auto-generated method stub

		String brnchID = request.getParameter("brnchID");
	//	mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		
		String deptRequestquery ="select d.dept_id, d.Dept_name from sbu_branch_dept s, am_ad_department d "+
		"where s.deptId = d.Dept_ID and s.branchId ="+brnchID+" order by d.Dept_name";
		
		//System.out.println("branchDeptQry >>>>>>>>> " + branchDeptQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<bDept>");
	    
	    try 
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(deptRequestquery);
			String output="<bDept>";
			while (rs.next()) 
			{
				out.write("<Department>");
				out.write("<Deptcode>");
				out.write(rs.getString(1));
				out.write("</Deptcode>");
				out.write("<Deptname>");
				out.write(rs.getString(2).replaceAll("&", "&amp;"));
				out.write("</Deptname>");
				out.write("</Department>");
				output= output + "<Department><Deptcode>" + rs.getString(1)+"</Deptcode><Deptname>"+ 
						rs.getString(2)+ "</Deptname></Department>"+ "\n";
			}
			out.write("</bDept>");
			output = output + "</bDept>";
			//System.out.println("output >>>>>>>> "+ "\n" + output);
	    }
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
	/*		  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	    
	}

         private void branchRequestByCode(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
//		// TODO Auto-generated method stub

		String brnchID = request.getParameter("brnchID");
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();

		String deptRequestquery ="select d.dept_Code, d.Dept_name from sbu_branch_dept s, am_ad_department d " +
                        "where s.deptCode = d.Dept_Code and s.branchcode ='"+brnchID+"'order by d.Dept_name";



//		System.out.println("deptRequestquery >>>>>>>>> " + deptRequestquery);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<bDept>");

	    try
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(deptRequestquery);
			String output="<bDept>";
			while (rs.next())
			{
				out.write("<Department>");
				out.write("<Deptcode>");
				out.write(rs.getString(1));
				out.write("</Deptcode>");
				out.write("<Deptname>");
				out.write(rs.getString(2).replaceAll("&", "&amp;"));
				out.write("</Deptname>");
				out.write("</Department>");
				output= output + "<Department><Deptcode>" + rs.getString(1)+"</Deptcode><Deptname>"+
						rs.getString(2)+ "</Deptname></Department>"+ "\n";
			}
			out.write("</bDept>");
			output = output + "</bDept>";
			//System.out.println("output >>>>>>>> "+ "\n" + output);
	    }
	    catch (SQLException e)
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
//	    	   System.out.println("<<<<<<<<<<<======con for dept: "+con);
/*			  if(con !=null)
			  {
				 con.close();
			  }
			  if(stmt !=null)
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }
	    }

	}

	private void categoryItemTypeRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");
		
		String cartItemsRequestQry="select ITEM_CODE,description from ST_INVENTORY_ITEMS where ITEMTYPE_CODE='"+catCode+"'";
		
//		System.out.println("<<<<<<cartItemsRequestQry: "+cartItemsRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(cartItemsRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<description>");
                out.write(rs.getString(2));
                out.write("</description>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><description>"+ 
				rs.getString(2)+ "</description></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
//	    	   System.out.println("<<<<<<<<<<<======con for item: "+con);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}

	private void categoryRequestByCode(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");
		
		String categoryRequestQry="select ITEMTYPE_CODE,NAME from ST_ITEMTYPE where category_code='"+catCode+"' order by NAME";
		
//		System.out.println("<<<<<<categoryRequestQry: "+categoryRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(categoryRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<description>");
                out.write(rs.getString(2));
                out.write("</description>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><description>"+ 
				rs.getString(2)+ "</description></Inventory>"+ "\n";
			}
			out.write("</message>");
			//output = output + "</message>";
			//System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
      	      mgDbCon.closeConnection(con, stmt, rs);
//	    	   System.out.println("<<<<<<<<<<<======con for category: "+con);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}
		
	private void requisitionUser(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
//		// TODO Auto-generated method stub
//		persistenceServiceDAO = new PersistenceServiceDAO();
//		HttpSession session = request.getSession();
//		User loginId = (User) session.getAttribute("CurrentUser");
//		String compCode = loginId.getCompanyCode();
//		String reqType_User = request.getParameter("type");
//				
//		String reqUserQry="select user_id,full_name from mg_gb_user where branch =(select branch_id from "+ 
//		" mg_ad_branch where branch_code='" +reqType_User+"' and company_code='" +compCode+"') and company_code='"+compCode+"'";
//		
//		System.out.println("reqUserQry >>>>>>>>>>>>>>> " + reqUserQry);
//		
//		
//		con = persistenceServiceDAO.getConnection();
//		response.setContentType(CONTENT_TYPE);
//	    PrintWriter out = response.getWriter();
//	    out.write("<user>");
//		String output="<user>";
//	    try 
//	    {
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(reqUserQry);
//			while (rs.next()) 
//			{
//                out.write("<Details>");
//                out.write("<userID>");
//                out.write(rs.getString(1));
//                out.write("</userID>");
//                out.write("<fullName>");
//                out.write(rs.getString(2));
//                out.write("</fullName>");
//                out.write("</Details>");
//                
//                output= output + "<Details><userID>" + rs.getString(1)+"</userID><fullName>"+ 
//				rs.getString(2)+ "</fullName></Details>"+ "\n";
//			}
//			out.write("</user>");
//			output = output + "</user>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
//		} 
//	    catch (SQLException e) 
//	    {
//			e.printStackTrace();
//		}
//	    finally
//	    {
//	       try 
//	       {
//			  if(con !=null) 
//			  {
//				 con.close();
//			  }
//			  if(stmt !=null) 
//			  {
//				 stmt.close();
//			  }
//			  if(rs !=null)
//			  {
//				 rs.close();
//			  }
//	        }
//	       catch(Exception ex)
//	       {
//	         ex.printStackTrace();
//	       }     
//	    }
	}

	private void measuringUnitRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{ 
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");
		
		String unitCodeRequestQry="select a.UNIT_MEASUREMENT,b.MEASURING_UNIT from ST_ITEMTYPE a, ST_MEASURING_UNIT b where a.UNIT_MEASUREMENT = b.UNIT_CODE and  ITEMTYPE_CODE='"+catCode+"'";
		
//		System.out.println("<<<<<<cartItemsRequestQry: "+unitCodeRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(unitCodeRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<NAME>");
                out.write(rs.getString(2));
                out.write("</NAME>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><NAME>"+ 
				rs.getString(2)+ "</NAME></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
//	    	   System.out.println("<<<<<<<<<<<======con for Unit: "+con);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}

	private void warehouseRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String wareCode = request.getParameter("wareCode");
		
		String cartItemsRequestQry="SELECT WAREHOUSE_CODE,NAME FROM ST_WAREHOUSE WHERE WAREHOUSE_CODE='"+wareCode+"'";
		
//		System.out.println("<<<<<<warehouseRequest: "+cartItemsRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(cartItemsRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<description>");
                out.write(rs.getString(2));
                out.write("</description>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><description>"+ 
				rs.getString(2)+ "</description></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}


	private void buRequest(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
//		// TODO Auto-generated method stub

		String brnchID = request.getParameter("brnchID");
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		
		String deptRequestquery ="select UTCODE, UTNAME from ST_UNDERTAKERS a,am_ad_branch b  "+
		"where b.BRANCH_ID = "+brnchID+" and  a.BU_CODE = b.BRANCH_CODE order by a.UTNAME";
		
	//	System.out.println("deptRequestquery >>>>>>>>> " + deptRequestquery);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<bDept>");
	    
	    try 
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(deptRequestquery);
			String output="<bDept>";
			while (rs.next()) 
			{
				out.write("<Department>");
				out.write("<Deptcode>");
				out.write(rs.getString(1));
				out.write("</Deptcode>");
				out.write("<Deptname>");
				out.write(rs.getString(2).replaceAll("&", "&amp;"));
				out.write("</Deptname>");
				out.write("</Department>");
				output= output + "<Department><Deptcode>" + rs.getString(1)+"</Deptcode><Deptname>"+ 
						rs.getString(2)+ "</Deptname></Department>"+ "\n";
			}
			out.write("</bDept>");
			output = output + "</bDept>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
	    }
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
/*	    	   mgDbCon.closeConnection(con, stmt, rs);
			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	    
	}

	private void RequestUsrRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String utID = request.getParameter("undertakeID");
		
		String requsrRequestquery ="select USER_CODE, USER_NAME from ST_INVENTORY_USERS "+
		"where UTCODE = '"+utID+"' and  STATUS = 'ACTIVE' order by USER_NAME";
		
//		System.out.println("<<<<<<cartItemsRequestQry: "+requsrRequestquery);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(requsrRequestquery);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<description>");
                out.write(rs.getString(2));
                out.write("</description>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><description>"+ 
				rs.getString(2)+ "</description></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
	//		System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}

	private void unitPackRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{ 
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");
		
		String packNoRequestQry="select PACK,DESCRIPTION from ST_INVENTORY_ITEMS WHERE ITEMTYPE_CODE='"+catCode+"'";
		
		System.out.println("<<<<<<cartItemsRequestQry: "+packNoRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(packNoRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<NAME>");
                out.write(rs.getString(2));
                out.write("</NAME>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><NAME>"+ 
				rs.getString(2)+ "</NAME></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
	    	   System.out.println("<<<<<<<<<<<======con for pack: "+con);
/*			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(stmt !=null) 
			  {
				 stmt.close();
			  }
			  if(rs !=null)
			  {
				 rs.close();
			  }*/
	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}

	private void categoryPRItemTypeRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
//		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catId = request.getParameter("catCode");
		
		String cartPrItemsRequestQry="select sub_category_code,sub_category_name from am_ad_sub_category where Category_ID='"+catId+"'";
		
		System.out.println("<<<<<<cartPrItemsRequestQry: "+cartPrItemsRequestQry);
		mgDbCon.closeConnection(con, stmt, rs);
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter(); 
	    out.write("<message>");
		String output="<message>";
	    try 
	    { 
			stmt = con.createStatement();
			rs = stmt.executeQuery(cartPrItemsRequestQry);
			while (rs.next()) 
			{
                out.write("<Inventory>");
                out.write("<itemCode>");
                out.write(rs.getString(1));
                out.write("</itemCode>");
                out.write("<description>");
                out.write(rs.getString(2));
                out.write("</description>");
                out.write("</Inventory>");
                
                output= output + "<Inventory><itemCode>" + rs.getString(1)+"</itemCode><description>"+ 
				rs.getString(2)+ "</description></Inventory>"+ "\n";
			}
			out.write("</message>");
			output = output + "</message>";
//			System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    finally
	    {
	       try 
	       {
	    	   mgDbCon.closeConnection(con, stmt, rs);
//	    	   System.out.println("<<<<<<<<<<<======con for item: "+con);

	        }
	       catch(Exception ex)
	       {
	         ex.printStackTrace();
	       }     
	    }
	}
	
		
}
