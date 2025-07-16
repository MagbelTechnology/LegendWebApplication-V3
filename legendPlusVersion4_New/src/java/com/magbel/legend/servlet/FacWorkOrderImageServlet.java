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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

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
public class FacWorkOrderImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MagmaDBConnection mgDbCon = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	GenerateList genList = null;
	ApplicationHelper applHelper= null;
	
	 private String pName="";
	 private String fName="";
	 private String workOrderCode="";
	 private String pgName="";
	 private int pgNum = 0;
	 private String urlLink="";
	 private String exitLink="";
         private String assetId ="";
         private String tranId="";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacWorkOrderImageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
//http://localhost:8080/legend3.net/DocumentHelp.jsp?np=uploadRequisitionImage&pgNo=1&image=N&ReqnID=REQN/5&CompCode=
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
		genList = new GenerateList();
		applHelper = new ApplicationHelper();
		mgDbCon = new MagmaDBConnection();
		String compCode = "";
		 		
		legend.admin.objects.User user = null;
		user =(legend.admin.objects.User)session.getAttribute("_user");
		String userID =user.getUserId();
		//cg = new CodeGenerator();
		PrintWriter out = response.getWriter();
		String userClass = (String) session.getAttribute("UserClass");
		
		 if (!userClass.equals("NULL") || userClass!=null){
			 
		String reqnImageID = "FACREQIMG/"+applHelper.getGeneratedId("FM_Requisition_image");
		//System.out.println("reqnImageID >>>>>>>>>>>>>>>> " + reqnImageID);
		
		String workstationIp = request.getRemoteAddr();

		captureData(request);
		
		if(!isRequisitionImageExisting(workOrderCode, pName, compCode))
        {
			 boolean output =insertBlob(userID,compCode,workOrderCode,workstationIp, reqnImageID, fName, pName);
			 
			 if ((pgNum==1)&&(output==true))
	            {
	            	//urlLink = "window.location='DocumentHelp.jsp?np=facilityRequisitionFormUpdate&image=y&" +
	            			//"ReqnID="+workOrderCode+"&CompCode="+compCode+"'";
	            	//System.out.println("[processRequisitionImage ] urlLink ::::::: " + urlLink);
                             urlLink = "window.location='DocumentHelp.jsp?np=facilityMgtWorkCompletionFormUpdate&workOrderCode="+workOrderCode
                                                +"&pgNo=1&image=Y&tranId="+tranId+"&assetId="+assetId+"&CompCode="+compCode+"'";
	            }
			 else if ((pgNum==1)&&(output==false))
	            {
				 	//exitLink = "window.location='DocumentHelp.jsp?np=facilityUploadRequisitionImage&pgNo=1&image=N&" +
     						//"ReqnID="+workOrderCode+"&CompCode="+compCode+"'";
                           exitLink = "window.location='DocumentHelp.jsp?np=facilityUploadWorkOrderImage&workOrderCode="+workOrderCode
                                                +"&pgNo=1&image=N&tranId="+tranId+"&assetId="+assetId+"&CompCode="+compCode+"'";

				 	//System.out.println("[processRequisitionImage ] exitLink ::::::: " + exitLink);
	            }
			 if(output==true)
		        {
				 /*String upd_IA_requisition_qry="update am_ad_requisition_image set image='Y' where reqnID='"+reqnId+"' and " +
		    		" userid='"+userID+"'";*/
				 
				 String upd_IA_requisition_qry="update FM_COMPLETED_WORK set image='Y' where work_order_code='"+workOrderCode+"'";
				// System.out.println("upd_IA_requisition_qry >>>>> " + upd_IA_requisition_qry);
				 genList.updateTable(upd_IA_requisition_qry);
				    
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
	}else {
		out.print("<script>alert('You have No Right')</script>");
	}
	}

	private void captureData(HttpServletRequest request)
	{
		try {
			for (Part part : request.getParts()) {
			   String fileName = part.getSubmittedFileName();
			   System.out.println("File Name: " + fileName);
			   File fullfile = new File(part.getName());
				File savedFile = new File(getServletContext().getRealPath("/"), fullfile.getName());
				part.write(savedFile.toString());
				fName = savedFile.toString();
				//System.out.println("FName >>>>>>>>>>  " + fName);
			}
			}catch(Exception e) {
				System.out.println("Error occured in FacWorkOrderImageServlet: captureData() method"	+ e);
			}

	}
	
	public boolean isRequisitionImageExisting(String reqnID,String pageName,String companyCode) 
	{
	 	boolean done=false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String FINDER_QUERY = "select imageID from FM_Requisition_image where reqnID=? ";

        try 
        {
        	con = mgDbCon.getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, reqnID);
            //ps.setString(2, companyCode);
            rs = ps.executeQuery();

            while (rs.next())
            {
               done=true;
            }
        } 
        catch (Exception ex)
        {
            System.out.println("WARNING: cannot fetch from [FM_Requisition_image]->" +
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
        //System.out.println("Result in  [isRequisitionImageExisting]= " + done);
        return done;
    }

	 public boolean insertBlob(String userId,String compCode ,String workOrderCode,String workStationIP,
			 String imageID,String fileName,String pgName)
	 {
		 
		  boolean done=true;
		  Connection conn = null;
		  PreparedStatement ps = null;
	      ResultSet rs = null;
	      String insertImgQry="insert into FM_Requisition_image (userID,image,company_Code,ReqnID,workstationIP,imageID,pageName)" +
	      		"values (?,?,?,?,?,?,?)";
	      /*System.out.println("[insertBlob ] fileName :::::::: " + fileName);
	      System.out.println("[insertBlob ] imageID :::::::: " + imageID);
	      System.out.println("[insertBlob ] pgName :::::::: " + pgName);*/
		  try 
		  {
			  conn = mgDbCon.getConnection("legendPlus");
			if (!fileName.equals(""))
			{
				FileInputStream fis = new FileInputStream(fileName);
				ps = conn.prepareStatement(insertImgQry);
				ps.setString(1, userId);
				ps.setBinaryStream(2, fis, fis.available());
				ps.setString(3, compCode);
				ps.setString(4, workOrderCode);
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
