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
public class CategoryTypeReqnServlet2 extends HttpServlet 
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
    public CategoryTypeReqnServlet2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IDType = request.getParameter("ID");
		
		String userClass = (String) request.getSession().getAttribute("UserClass");
		 if (!userClass.equals("NULL") || userClass!=null){
			 
		if (IDType.equalsIgnoreCase("item"))
		{
			//categoryItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Sec"))
		{
			sectionRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUserFromBranch(request,response);
		}
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
			//categoryItemTypeRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Sec"))
		{
			sectionRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUserFromBranch(request,response);
		}
	}
	}

	private void sectionRequest(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
//		// TODO Auto-generated method stub

		String deptID = request.getParameter("deptID");
		String brnchID = request.getParameter("brnchID");
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		
		String branchDeptQry = "SELECT  sectionId FROM sbu_dept_section "
            + " WHERE [branchid]='" + brnchID +"' AND [deptid]='" + deptID + "'";
		
		//System.out.println("branchDeptQry >>>>>>>>> " + branchDeptQry);
		
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<bDept>");
	    
	    try 
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(branchDeptQry);
			String output="<bSect>";
			while (rs.next()) 
			{
				out.write("<section>");
				out.write("<sectcode>");
				out.write(rs.getString(1));
				out.write("</sectcode>");
				out.write("<sectname>");
				out.write(rs.getString(2).replaceAll("&", "&amp;"));
				out.write("</sectname>");
				out.write("</section>");
				//output= output + "<section><sectcode>" + rs.getString(1)+"</sectcode><sectname>"+ 
						//rs.getString(2)+ "</sectname></section>"+ "\n";
			}
			out.write("</bSect>");
			output = output + "</bSect>";
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

	/*private void categoryItemTypeRequest(HttpServletRequest request,	HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		String catCode = request.getParameter("catCode");
		
		String cartItemsRequestQry="select itemcode,itemName from am_ad_CategoryItems where category_code='"+catCode+"'";
		
		
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
	}*/
	
	private void requisitionUserFromBranch(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
		
		int reqnBranchID = request.getParameter("brnchID")==null || request.getParameter("brnchID").equals("")?0:Integer.parseInt(request.getParameter("brnchID"));

		String reqUserQry="select user_id,full_name from am_gb_user where branch = "+reqnBranchID+" and user_status='Active'" +
				" order by full_name ";
		
		//System.out.println("reqUserQry >>>>>>>>>>>>>>> " + reqUserQry);
		
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		con = mgDbCon.getConnection("legendPlus");
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<user>");
		String output="<user>";
	    try 
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(reqUserQry);
			while (rs.next()) 
			{
                out.write("<Details>");
                out.write("<userID>");
                out.write(rs.getString(1));
                out.write("</userID>");
                out.write("<fullName>");
                out.write(rs.getString(2));
                out.write("</fullName>");
                out.write("</Details>");
                
                output= output + "<Details><userID>" + rs.getString(1)+"</userID><fullName>"+ 
				rs.getString(2)+ "</fullName></Details>"+ "\n";
			}
			out.write("</user>");
			//output = output + "</user>";
			//System.out.println("output >>>>>>>> "+ "\n" + output);
		} 
	    catch (SQLException e) 
	    {
                System.out.println(">>>>>>>>>>>>>> sql error is "+ e);
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
                    System.out.println(">>>>>>>>>>>>>> java error is "+ ex);
	         ex.printStackTrace();
	       }     
	    }
	}
}
