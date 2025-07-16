package com.magbel.legend.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.magbel.util.ApplicationHelper;

import magma.AssetRecordsBean;
import magma.net.manager.AssetExcelUploadManager;
import magma.net.manager.ExcelAssetDescriptionUploadManager;
import magma.net.manager.ExcelStaffUploadManager;
import magma.net.manager.ExcelUserUploadManager;
import magma.net.manager.ImproveExcelUploadManager;

@WebServlet(
   name = "processAssetImproveFile"
)
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class processUserCreationFile extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("Calling Process User Creation Upload File");
      HttpSession session = request.getSession();
      PrintWriter out = response.getWriter();
      String payFile = "";
      String dname = "";
      String userid = (String)session.getAttribute("CurrentUser");
      Properties prop = new Properties();
  	File file = new File("C:\\Property\\LegendPlus.properties");
  	FileInputStream input = new FileInputStream(file);
  	prop.load(input);
    	String location = prop.getProperty("uploadFile");
  	
  	File locationPath = new File(location);
  	if(!locationPath.exists()) {
  		locationPath.mkdir();
  	}

  	Part part = request.getPart("file");

	 String toRead = part.getSubmittedFileName();
       session.setAttribute("RealPath", toRead);
		String uid = (String)session.getAttribute("CurrentUser");
		payFile = part.getSubmittedFileName();
		 System.out.println("payFile: "+payFile);
		 if(payFile.endsWith(".xlsx")|| payFile.endsWith(".xls")) {
   	//File savedFile = new File(getServletContext().getRealPath("/"),fileDetail.getName());
      String home = System.getProperty("user.home"); 
      File savedFile = new File(location,part.getSubmittedFileName());
   // File savedFile = new File(home + "\\Downloads",part.getSubmittedFileName());
	System.out.println("Trimed file to be saved to the disk is "+savedFile);
	ExcelUserUploadManager excelManager = new ExcelUserUploadManager(userid);
	//it.write(savedFile);
	part.write(savedFile.toString());
   ArrayList list = excelManager.getFileFromServer(savedFile);
	session.setAttribute("GAsset",list);
	if(!list.isEmpty())
	{
		System.out.println("We are here!");
		// out.println("<script>alert('Staff file uploaded before!')</script>");
         out.print("<script>window.location='DocumentHelp.jsp?np=manageUsers&status=ACTIVE&select=ACTIVE';</script>");
	}
	System.out.println("We are here 2!");
	out.println("<script>window.location='DocumentHelp.jsp?np=uploadUserCreationDetail';</script>");
//	response.sendRedirect("DocumentHelp.jsp?np=uploadStaffDetail");
		
		session.setAttribute("PATH",payFile);
  	}else {
			out.println("<script type=\"text/javascript\">");
		   out.println("alert('This File is not Excel File..');");
		   out.println("location='DocumentHelp.jsp?np=excelUserCreatetionUpload';");
		   out.println("</script>");
		}
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
   }
}