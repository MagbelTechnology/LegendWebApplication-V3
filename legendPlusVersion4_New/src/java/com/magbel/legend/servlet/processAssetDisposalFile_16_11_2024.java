package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import magma.net.manager.DisposalExcelUploadManager;

@WebServlet(
   name = "processAssetDisposalFile"
)
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class processAssetDisposalFile_16_11_2024 extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("Calling Process Asset Disposal File");
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
	System.out.println("groupAssetByAsset: " + groupAssetByAsset);
  	
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
          File savedFile = new File(location,part.getSubmittedFileName());
          //   File savedFile = new File(home + "\\Downloads",part.getSubmittedFileName());
//             System.out.println("Trimed file to be saved to the disk is "+savedFile)
             DisposalExcelUploadManager excelManager = new DisposalExcelUploadManager(uid,groupAssetByAsset);
           
  		//it.write(savedFile);
             part.write(savedFile.toString());
          ArrayList list = excelManager.getFileFromServer(savedFile);
  		session.setAttribute("GAsset",list);
  		if(!list.isEmpty())
  		{
  			out.print("<script>location.replace('DocumentHelp.jsp?np=RejectedAssets');</script>");
  		}
  			out.print("<script>location.replace('DocumentHelp.jsp?np=uploadAssetDisposalDetail');</script>");
              
  			
  			session.setAttribute("PATH",payFile);
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