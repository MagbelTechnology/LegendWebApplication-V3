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
public class processAssetImproveFile extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("Calling Process Asset Improve Upload File");
  	String payFile = "";
  	String dname = "";
  	int quantity = 1;
  	HttpSession session = request.getSession(); 
  	AssetRecordsBean ad = null;
  	
  	Properties prop = new Properties();
	File file = new File("C:\\Property\\LegendPlus.properties");
	FileInputStream input = new FileInputStream(file);
	prop.load(input);
  	String location = prop.getProperty("uploadFile");
	
	File locationPath = new File(location);
	if(!locationPath.exists()) {
		locationPath.mkdir();
	}
	String groupAssetByAsset = prop.getProperty("groupAssetByAsset");
	
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
          	//File savedFile = new File(getServletContext().getRealPath("/"),fileDetail.getName());
             String home = System.getProperty("user.home");
     		if(payFile.endsWith("xlsx")|| payFile.endsWith("xls")) {
           File savedFile = new File(location,part.getSubmittedFileName());
            // File savedFile = new File(home + "\\Downloads",part.getSubmittedFileName());
             System.out.println("Trimed file to be saved to the disk is "+savedFile);
             ImproveExcelUploadManager excelManager = new ImproveExcelUploadManager(uid,groupAssetByAsset,groupid);
  		//it.write(savedFile);
        part.write(savedFile.toString());
          ArrayList list = excelManager.getFileFromServer(savedFile);
  		session.setAttribute("GAsset",list);
  		if(!list.isEmpty())
  		{
  			out.print("<script>location.replace('DocumentHelp.jsp?np=RejectedAssets&gid="+groupid+"');</script>");
  		}
  			out.print("<script>location.replace('DocumentHelp.jsp?np=uploadAssetImproveDetail&gid="+groupid+"');</script>");
              
  			
  			session.setAttribute("PATH",payFile);
     		}else {
  				out.println("<script type=\"text/javascript\">");
				   out.println("alert('This File is not Excel File..');");
				   out.println("location='DocumentHelp.jsp?np=groupExcelImproveUpload';");
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