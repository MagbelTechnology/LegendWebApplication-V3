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

import magma.net.manager.AssetExcelUploadManager;
import magma.net.manager.ExcelStaffUploadManager;
import magma.net.manager.ExcelUserUploadManager;

@WebServlet(
   name = "ProcessStaffCreationUpload"
)
@MultipartConfig(
   fileSizeThreshold = 3145728,
   maxFileSize = 10485760L,
   maxRequestSize = 52428800L
)
public class ProcessStaffCreationUpload extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("Calling Process Staff Creation File");
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
    			if(payFile.endsWith(".xlsx")|| payFile.endsWith(".xls")) {
            	File savedFile = new File(location,part.getSubmittedFileName());
               String home = System.getProperty("user.home");
           //  File savedFile = new File(home + "\\Downloads",part.getSubmittedFileName());
    		System.out.println("Trimed file to be saved to the disk is "+savedFile);
    		 ExcelStaffUploadManager excelManager = new ExcelStaffUploadManager(userid);
    		//it.write(savedFile);
          part.write(savedFile.toString());
            ArrayList list = excelManager.getFileFromServer(savedFile);
    		session.setAttribute("GAsset",list);
    		if(!list.isEmpty())
    		{
    			System.out.println("We are here!");  
    			// out.println("<script>alert('Staff file uploaded before!')</script>");
    	          out.print("<script>window.location='DocumentHelp.jsp?np=excelStaffListUpload';</script>");
    			//out.println("<script>alert('Staff Upload Successful!')</script>");
               // out.print("<script>window.location='DocumentHelp.jsp?np=uploadStaffDetail';</script>");
    		}
    		System.out.println("We are here 2!");
    		out.print("<script>location.replace('DocumentHelp.jsp?np=uploadStaffDetail');</script>");
       	 //out.print("<script>window.location='DocumentHelp.jsp?np=uploadStaffCreationDetail';</script>");
                
    			
    			session.setAttribute("PATH",payFile);
    			}else {
      				out.println("<script type=\"text/javascript\">");
 				   out.println("alert('This File is not Excel File..');");
 				   out.println("location='DocumentHelp.jsp?np=excelStaffListUpload';");
 				   out.println("</script>");
   			}
    	/*
    	out.print("<script>");
    	out.print("window.close()");
    	out.println("</script>");
    	*/

   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
   }
}