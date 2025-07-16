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
public class RequisitionImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PersistenceServiceDAO persistenceServiceDAO = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ApplicationHelper2 applHelper = null;
	CodeGenerator cg = null;
	 private String pName="";
	 private String fName="";
	 private String reqnId="";
	 private String pgName="";
	 private int pgNum = 0;
	 private String urlLink="";
	 private String exitLink="";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RequisitionImageServlet() {
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
		processRequisitionImage(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequisitionImage(request, response);
	}

	private void processRequisitionImage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		applHelper = new ApplicationHelper2();
		persistenceServiceDAO = new PersistenceServiceDAO();
		User loginId = (User) session.getAttribute("CurrentUser");
		String compCode = loginId.getCompanyCode();
		String userID = loginId.getUserId();
		cg = new CodeGenerator();
		PrintWriter out = response.getWriter();
		System.out.println("loginId >>>>>>>>>>> " + loginId);

		String reqnImageID = cg.generateCode("REQUISITIONIMAGE", "", "", "");
		System.out.println("reqnImageID >>>>>>>>>>>>>>>> " + reqnImageID);
		//System.out.println("reqnID >>>>>>>>>>>>>>>> " + reqnID);
		String workstationIp = request.getRemoteAddr();

		captureData(request);
		
		if(!isRequisitionImageExisting(reqnId, pName, compCode))
        {
			 boolean output =insertBlob(userID,compCode,reqnId,workstationIp, reqnImageID, fName, pName);
			 
			 if ((pgNum==1)&&(output==true))
	            {
	            	urlLink = "window.location='DocumentHelp.jsp?np=RequisitionFormUpdate&image=y&" +
	            			"ReqnID="+reqnId+"&CompCode="+compCode+"'";
	            	System.out.println("[processRequisitionImage ] urlLink ::::::: " + urlLink);
	            }
			 else if ((pgNum==1)&&(output==false))
	            {
				 	exitLink = "window.location='DocumentHelp.jsp?np=uploadRequisitionImage&pgNo=1&image=N&" +
     						"ReqnID="+reqnId+"&CompCode="+compCode+"'";
				 	System.out.println("[processRequisitionImage ] exitLink ::::::: " + exitLink);
	            }
			 if(output==true)
		        {
				 String upd_IA_requisition_qry="update IA_requisition set image='Y' where reqnID='"+reqnId+"' and " +
		    		"company_code='"+compCode+"' and userid='"+userID+"'";
				    applHelper.updateTable(upd_IA_requisition_qry);
				    
			        out.println("<script>alert('Image successfully saved!')</script>");
					out.println("<script>");
					out.println(urlLink);
					out.println("</script>");
		        }
		        else
		        {
			        out.println("<script>alert('Image not saved!')</script>");
					out.println("<script>");
					out.println(exitLink);
					out.println("</script>");
				} 
        }
		else
        {
        	out.println("<script>alert('Image already exist!')</script>");
        	out.println("<script>");
        	out.println(exitLink);
        	out.println("</script>");
    	}
		
	}

	private void captureData(HttpServletRequest request)
	{
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println("isMultipart >>>>>>>>>>>>>>> " + isMultipart);
		
		
		if (isMultipart) 
		{
			try 
			{
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(request); /* FileItem */
				System.out.println("The size of list is " + items.size());

				Iterator iter = items.iterator();
 				while (iter.hasNext()) 
 				{
					FileItem item = (FileItem) iter.next();
					InputStream stream = item.getInputStream();

					if (item.isFormField()) 
					{
						String fieldname = item.getFieldName();
						System.out.println("The value of fieldname : "	+ item.getFieldName());

						if (fieldname.equals("reqnId")) 
						{
							reqnId = Streams.asString(stream);
							System.out.println(" reqnId : " + reqnId);
						}						
						if (fieldname.equals("pageNo"))
						{
							pgNum = Integer.parseInt(Streams.asString(stream));
							System.out.println(" PageNumber >>>>>  " + pgNum);
						}
						if (fieldname.equals("pageName"))
						{
							pName = Streams.asString(stream);
							System.out.println(" PageName >>>>>  " + pName);
						}
					}
					else
					{
						if (item.getFieldName().equalsIgnoreCase("file")) 
						{
							File fullfile = new File(item.getName());
							File savedFile = new File(getServletContext().getRealPath("/"), fullfile.getName());
							item.write(savedFile);
							fName = savedFile.toString();
							System.out.println("FName >>>>>>>>>>  " + fName);
						}
					}
				}
			} 
			catch (FileUploadException fue)
			{
				System.out.println("Error occured in RequisitionImageServlet: captureData() method"	+ fue);
			} 
			catch (Exception e)
			{
				System.out.println("there is an error in RequisitionImageServlet: captureData() method"	+ e);
			}
		}

	}
	
	public boolean isRequisitionImageExisting(String reqnID,String pageName,String companyCode) 
	{
	 	boolean done=false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String FINDER_QUERY = "select imageID from IA_requisition_image where reqnID=? and company_Code=?";

        try 
        {
            con = persistenceServiceDAO.getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnID);
            ps.setString(2, companyCode);
            rs = ps.executeQuery();

            while (rs.next())
            {
               done=true;
            }
        } 
        catch (Exception ex)
        {
            System.out.println("WARNING: cannot fetch from [IA_requisition_image]->" +
                    ex.getMessage());
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
        System.out.println("Result in  [isRequisitionImageExisting]= " + done);
        return done;
    }

	 public boolean insertBlob(String userId,String compCode ,String reqnID,String workStationIP,
			 String imageID,String fileName,String pgName)
	 {
		 
		  boolean done=true;
		  Connection conn = null;
		  PreparedStatement ps = null;
	      ResultSet rs = null;
	      String insertImgQry="insert into IA_REQUISITION_IMAGE (userID,image,company_Code,ReqnID,workstationIP,imageID,pageName)" +
	      		"values (?,?,?,?,?,?,?)";
	      System.out.println("[insertBlob ] fileName :::::::: " + fileName);
	      System.out.println("[insertBlob ] imageID :::::::: " + imageID);
	      System.out.println("[insertBlob ] pgName :::::::: " + pgName);
		  try 
		  {
			conn = persistenceServiceDAO.getConnection();
			if (!fileName.equals(""))
			{
				FileInputStream fis = new FileInputStream(fileName);
				ps = conn.prepareStatement(insertImgQry);
				ps.setString(1, userId);
				ps.setBinaryStream(2, fis, fis.available());
				ps.setString(3, compCode);
				ps.setString(4, reqnID);
				ps.setString(5, workStationIP);
				ps.setString(6, imageID);
				ps.setString(7, pgName);
				ps.execute();
				ps.close();
			}
			conn.close();
		  }
		  catch (Exception e)
		  {
			  e.printStackTrace();
			  done=false;
		  }
		  
		  return done;
	 }
}
