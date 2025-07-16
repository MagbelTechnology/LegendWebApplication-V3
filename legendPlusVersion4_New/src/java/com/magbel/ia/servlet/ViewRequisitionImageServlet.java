package com.magbel.ia.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.User;

/**
 * Servlet implementation class RequisitionServlet
 */
public class ViewRequisitionImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PersistenceServiceDAO persistenceServiceDAO = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ApplicationHelper2 applHelper = null;
	CodeGenerator cg = null;
	private byte[] image;
	ServletOutputStream stream = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewRequisitionImageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		viewRequisitionImage(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		viewRequisitionImage(request, response);
	}

	private void viewRequisitionImage(HttpServletRequest request,HttpServletResponse response) 
	{
		System.out.println("INFO::Entered viewRequisitionImage");
        String reqnID = request.getParameter("reqnID");
        System.out.println("*************reqnID*****************"+reqnID);
        String pageName = request.getParameter("pageName");
        System.out.println("*************pageName********************"+pageName);
        System.out.println("about to load class");
        
        HttpSession session = request.getSession();
		applHelper = new ApplicationHelper2();
		persistenceServiceDAO = new PersistenceServiceDAO();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		String userID = loginId.getUserId();
		
        image = getGroupImageData(reqnID,pageName,compCode);
        System.out.println(image);
        
		try
		{
			if(this.image != null)
			{ 
				System.out.println("Image Retrieved");
				response.setContentType("application/pdf"); 
				response.setHeader("Content-disposition", "attachment; filename=" + "Example.pdf" );
			    stream = response.getOutputStream();
			    stream.write(image);
			    stream.close();
		    }
			else
			{
			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private byte[] getGroupImageData(String reqnID, String pageName,String compCode)
	{
		byte[] fileBytes=null;
		persistenceServiceDAO = new PersistenceServiceDAO();
		String retrieveReqnImageQry="select image from IA_Requisition_Image where company_code='" +
				compCode +"' and ReqnID='"+reqnID+"'";
		System.out.println("retrieveReqnImageQry >>>>>>>>>>>> " + retrieveReqnImageQry);
		try
		{
			con = persistenceServiceDAO.getConnection();
			pstmt = con.prepareStatement(retrieveReqnImageQry);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				fileBytes = rs.getBytes(1);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
	    {
	       try 
	       {
			  if(con !=null) 
			  {
				 con.close();
			  }
			  if(pstmt !=null) 
			  {
				  pstmt.close();
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
		return fileBytes;
	}

	
	
}
