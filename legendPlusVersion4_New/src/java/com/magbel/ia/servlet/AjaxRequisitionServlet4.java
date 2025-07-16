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
public class AjaxRequisitionServlet4 extends HttpServlet 
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
    public AjaxRequisitionServlet4() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IDType = request.getParameter("ID");
			sectionRequest(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IDType = request.getParameter("ID");
		
		
		sectionRequest(request,response);
		
		
	}

	private void sectionRequest(HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
		// TODO Auto-generated method stub
		persistenceServiceDAO = new PersistenceServiceDAO();
		HttpSession session = request.getSession();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		String deptcode = request.getParameter("type");
		
		String branchDeptQry ="select b.section_code,b.section_name from mg_ad_section b,ia_sbu_branch_dept a " +
				" where a.company_code='"+compCode+"' and a.deptcode='" + deptcode+"' and b.company_code='"+compCode+"'";
		//System.out.println("branchDeptQry >>>>>>>>> " + branchDeptQry);
		
		con = persistenceServiceDAO.getConnection();
		response.setContentType(CONTENT_TYPE);
	    PrintWriter out = response.getWriter();
	    out.write("<bSect>");
	    
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
				output= output + "<section><sectcode>" + rs.getString(1)+"</sectcode><sectname>"+ 
						rs.getString(2)+ "</sectname></section>"+ "\n";
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

		
}
