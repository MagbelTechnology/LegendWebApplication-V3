/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
		String IDType = request.getParameter("ID");
		if (IDType.equalsIgnoreCase("item"))
		{
			categoryItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Branch"))
		{
			branchRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUser(request,response);
		}
                else if (IDType.equalsIgnoreCase("BranchByCode"))
		{
			branchRequestByCode(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IDType = request.getParameter("ID");
		if (IDType.equalsIgnoreCase("item"))
		{
			categoryItemTypeRequest(request,response);
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

	private void branchRequest(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
//		// TODO Auto-generated method stub

		String brnchID = request.getParameter("brnchID");
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();

		String deptRequestquery ="select d.dept_id, d.Dept_name from sbu_branch_dept s, am_ad_department d "+
		"where s.deptId = d.Dept_ID and s.branchId ="+brnchID+" order by d.Dept_name";

		//System.out.println("branchDeptQry >>>>>>>>> " + branchDeptQry);

		con = mgDbCon.getConnection("helpDesk");
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
			  }
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
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();

		String deptRequestquery ="select d.dept_Code, d.Dept_name from sbu_branch_dept s, am_ad_department d " +
                        "where s.deptCode = d.Dept_Code and s.branchcode ='"+brnchID+"'order by d.Dept_name";



		//System.out.println("branchDeptQry >>>>>>>>> " + branchDeptQry);

		con = mgDbCon.getConnection("helpDesk");
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
			  }
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
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");

		String cartItemsRequestQry="select itemcode,itemName from am_ad_CategoryItems where status='ACTIVE' and category_code='"+catCode+"'";


		con = mgDbCon.getConnection("helpDesk");
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
			  }
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
}
