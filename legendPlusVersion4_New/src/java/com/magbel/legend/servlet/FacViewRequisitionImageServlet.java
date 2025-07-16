package com.magbel.legend.servlet;

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

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.ApplicationHelper;
import com.magbel.legend.bus.*;;

/**
 * Servlet implementation class RequisitionServlet
 */
public class FacViewRequisitionImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MagmaDBConnection mgDbCon = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	GenerateList genList = null;
	ApplicationHelper applHelper= null;
	private byte[] image;
	ServletOutputStream stream = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacViewRequisitionImageServlet() {
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
       // System.out.println("*************reqnID*****************"+reqnID);
        String pageName = request.getParameter("pageName");
        //System.out.println("*************pageName********************"+pageName);
       // System.out.println("about to load class");
        
        HttpSession session = request.getSession();
        genList = new GenerateList();
		applHelper = new ApplicationHelper();
		String compCode = "";
		String userID = (String)session.getAttribute("UserCenter");
		
		String userClass = (String) session.getAttribute("UserClass");
		
		if (!userClass.equals("NULL") || userClass!=null){
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
	}

	private byte[] getGroupImageData(String reqnID, String pageName,String compCode)
	{
		byte[] fileBytes=null;
		mgDbCon = new MagmaDBConnection();
		String retrieveReqnImageQry="select image from FM_Requisition_Image where ReqnID='"+reqnID+"'";
		//System.out.println("retrieveReqnImageQry >>>>>>>>>>>> " + retrieveReqnImageQry);
		try
		{
			con = mgDbCon.getConnection("legendPlus");
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
