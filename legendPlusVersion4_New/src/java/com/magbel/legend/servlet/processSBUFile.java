package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import com.magbel.util.ApplicationHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import magma.AssetRecordsBean;
import magma.net.manager.AssetExcelUploadManager;
import magma.net.manager.ExcelSbuUploadManager;

@WebServlet(
   name = "processSBUFile"
)
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class processSBUFile extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("Calling Process Asset File");
  	String payFile = "";
  	String dname = "";
  	int quantity = 1;
  	HttpSession session = request.getSession(); 
  	AssetRecordsBean ad = null;
  	
	Properties prop = new Properties();
	File file = new File("C:\\Property\\LegendPlus.properties");
	FileInputStream input = new FileInputStream(file);
	prop.load(input);
	String groupAssetByAsset = prop.getProperty("groupAssetByAsset");
	String location = prop.getProperty("uploadFile");
	
	File locationPath = new File(location);
	if(!locationPath.exists()) {
		locationPath.mkdir();
	}
	
	try {
		ad = new AssetRecordsBean();
	} catch (Exception e) {
		
		e.printStackTrace();
	}
  	PrintWriter out = response.getWriter();
  	String  groupid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
  	Part part = request.getPart("file");
  			ad.setInsertGroupMainTrans(quantity);
  		 String toRead = part.getSubmittedFileName();
              session.setAttribute("RealPath", toRead);
  			String uid = (String)session.getAttribute("CurrentUser");
  			payFile = part.getSubmittedFileName();
  			 System.out.println("payFile: "+payFile);
  			if(payFile.endsWith(".xlsx")|| payFile.endsWith(".xls")) {
          	//File savedFile = new File(getServletContext().getRealPath("/"),part.getSubmittedFileName());
  				File savedFile = new File(location,part.getSubmittedFileName());
             String home = System.getProperty("user.home"); 
           //File savedFile = new File(home + "\\Downloads",part.getSubmittedFileName());
             System.out.println("Trimed file to be saved to the disk is "+savedFile);
          		ExcelSbuUploadManager excelManager = new ExcelSbuUploadManager(uid);
  		//it.write(savedFile);
      part.write(savedFile.toString());
          ArrayList list = excelManager.getFileFromServer(savedFile);
  		session.setAttribute("GAsset",list);
  		if(!list.isEmpty())
  		{
  			//out.print("<script>location.replace('DocumentHelp.jsp?np=RejectedAssets');</script>");
  			out.println("<script>window.location='DocumentHelp.jsp?np=RejectedAssets';</script>");
  		}
  			//out.print("<script>location.replace('DocumentHelp.jsp?np=uploadSBUDetail');</script>");
  			out.println("<script>window.location='DocumentHelp.jsp?np=uploadSBUDetail';</script>");
              
  			
  			session.setAttribute("PATH",payFile);
  			}else {
  				out.println("<script type=\"text/javascript\">");
				   out.println("alert('This File is not Excel File..');");
				   out.println("location='DocumentHelp.jsp?np=groupExcelUpload';");
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
   
   public void deleteFile(String code) {
	   try {
   	Properties prop = new Properties();
   	File file = new File("C:\\Property\\LegendPlus.properties");
       FileInputStream input = new FileInputStream(file);
       prop.load(input);
       String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
   	  File folder = new File(UPLOAD_FOLDER);
	        File[] listOfFiles = folder.listFiles();
	        
	       String listFiles = Arrays.toString(listOfFiles);
	       
	       String newCodeName = "W" + code;
	       	       
	       for(int a=0; a<listOfFiles.length; a++) {
	    	   String value = String.valueOf(listOfFiles[a]);
	    	   File extType = new File(value);
	    	   if(extType.getName().contains(newCodeName+".xls") || extType.getName().contains(newCodeName+".xlsx")) {
   	    	   	extType.delete();
               	System.out.println(extType.getName() + " deleted");
	    	   }

	       }
	   }catch(Exception e) {
		   e.getMessage();
	   }
   }
}