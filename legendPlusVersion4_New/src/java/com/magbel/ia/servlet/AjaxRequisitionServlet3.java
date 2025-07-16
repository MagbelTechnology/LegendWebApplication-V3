package com.magbel.ia.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.magbel.ia.vao.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.ia.dao.PersistenceServiceDAO;
/**
 * Servlet implementation class AjaxRequisitionServlet
 */
public class AjaxRequisitionServlet3 extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/xml";
	PersistenceServiceDAO persistenceServiceDAO = null; 
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxRequisitionServlet3() {
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
			//requisitionRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Branch"))
		{
			//branchRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUserSupervisor(request,response);
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
			//requisitionRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Branch"))
		{
			//branchRequest(request,response);
		}
		else if (IDType.equalsIgnoreCase("Req"))
		{
			requisitionUserSupervisor(request,response);
		}
	}

	private void requisitionUserSupervisor(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
		persistenceServiceDAO = new PersistenceServiceDAO();
		HttpSession session = request.getSession();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		String reqType_User = request.getParameter("type");
				
		String reqUserQry="select user_id,full_name from mg_gb_user where branch =(select branch_id from "+ 
		" mg_ad_branch where branch_code='" +reqType_User+"' and company_code='" +compCode+"') and company_code='"+compCode+"'" +
				" and is_supervisor='Y' and user_status='A'";
		
		//System.out.println("reqUserQry >>>>>>>>>>>>>>> " + reqUserQry);
		
		
		con = persistenceServiceDAO.getConnection();
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<supervisor>");
		String output="<supervisor>";
	    try 
	    {
			stmt = con.createStatement();
			rs = stmt.executeQuery(reqUserQry);
			while (rs.next()) 
			{
                out.write("<spDetails>");
                out.write("<spuserID>");
                out.write(rs.getString(1));
                out.write("</spuserID>");
                out.write("<spfullName>");
                out.write(rs.getString(2));
                out.write("</spfullName>");
                out.write("</spDetails>");
                
                output= output + "<spDetails><spuserID>" + rs.getString(1)+"</spuserID><spfullName>"+ 
				rs.getString(2)+ "</spfullName></spDetails>"+ "\n";
			}
			out.write("</supervisor>");
			output = output + "</supervisor>";
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
}
