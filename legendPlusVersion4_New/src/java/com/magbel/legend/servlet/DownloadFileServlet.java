package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.write.WritableWorkbook;
import magma.AssetRecordsBean;

public class DownloadFileServlet extends HttpServlet {
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
//    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException	
{  
    	mail= new EmailSmsServiceBus();
    	records = new ApprovalRecords();
    	String userClass = (String) request.getSession().getAttribute("UserClass");
    	if (!userClass.equals("NULL") || userClass!=null){
    	try {
			ad = new AssetRecordsBean();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	PrintWriter out = response.getWriter();
        OutputStream outp = null;
    	HttpSession session=request.getSession(false);
    	WritableWorkbook wworkbook;
    	HSSFWorkbook workbook = new HSSFWorkbook();
        InputStream in = null;
        String[] args = null;
        FileOutputStream out = null;
    	boolean Result = false;
    	String bacthId = request.getParameter("bacthId");
    	String userName = request.getParameter("userName");
    	String branchId = request.getParameter("branchId");

    	 ArrayList list = (ArrayList)session.getAttribute("ulist");
    	 magma.AssetRecordsBean bd = null;
   	 
       HSSFSheet sheet = workbook.createSheet("workbook");
       HSSFRow rowhead = sheet.createRow((short) 0);
       rowhead.createCell((short) 0).setCellValue("ASSET_ID");
       rowhead.createCell((short) 1).setCellValue("DESCRITION");
       rowhead.createCell((short) 2).setCellValue("BAR CODE");
       rowhead.createCell((short) 3).setCellValue("SBU CODE");
       rowhead.createCell((short) 4).setCellValue("SERIAL NO.");
       rowhead.createCell((short) 5).setCellValue("REGISTRATION NO");
       rowhead.createCell((short) 6).setCellValue("ENGINE NO");
       rowhead.createCell((short) 7).setCellValue("ASSET MODEL");
       rowhead.createCell((short) 8).setCellValue("ASSET MAKE");
       rowhead.createCell((short) 9).setCellValue("COST PRICE");
       rowhead.createCell((short) 10).setCellValue("PURCHASE DATE");
       rowhead.createCell((short) 11).setCellValue("COMPONENT");
       rowhead.createCell((short) 12).setCellValue("COMPONENT BARCODE");
       rowhead.createCell((short) 13).setCellValue("ASSET USER");
       rowhead.createCell((short) 14).setCellValue("COMMENTS");
       rowhead.createCell((short) 15).setCellValue("ASSET SIGHTED");
       rowhead.createCell((short) 16).setCellValue("ASSET FUNCTIONING");
       rowhead.createCell((short) 17).setCellValue("ASSET CODE");
       rowhead.createCell((short) 18).setCellValue("BRANCH ID");
       rowhead.createCell((short) 19).setCellValue("CATEGORY ID");
       rowhead.createCell((short) 20).setCellValue("BATCH ID");
       int i = 1;
     for (int j = 0; j < list.size(); j++) {
         bd = (magma.AssetRecordsBean) list.get(j);
         String registration = bd.getRegistration_no();
         String Description = bd.getDescription();
         String assetuser = bd.getUser();
         String reason1 = bd.getReason();
         String sbu = bd.getSbu_code();
         String dept = bd.getDepartment_id();
         String comments = bd.getComments();
         String sighted = bd.getAssetsighted();
         String function = bd.getAssetfunction();
         int assetcode = bd.getAssetCode();
         String categoryId = bd.getCategory_id();                
         String barcode = bd.getBar_code();
         String assetId = bd.getAsset_id();
         String subcat = bd.getSub_category_id();
         String batchId = bd.getProjectCode();
         String costprice = bd.getCost_price();
			  String make = bd.getMake();
			  String model = bd.getModel();
			  String spare1 = bd.getSpare_1();
			  String spare2 = bd.getSpare_2();
			  String registrationNo = bd.getRegistration_no();
			  String sbucode = bd.getSbu_code();
			  String serialNo = bd.getSerial_number();
			  String engineNo = bd.getEngine_number();
			  String purchaseDate = bd.getDate_of_purchase();                      

         HSSFRow row = sheet.createRow((short) i);
         row.createCell((short) 0).setCellValue(assetId);
         row.createCell((short) 1).setCellValue(Description);
         row.createCell((short) 2).setCellValue(barcode);
         row.createCell((short) 3).setCellValue(sbucode);
         row.createCell((short) 4).setCellValue(serialNo);
         row.createCell((short) 5).setCellValue(registrationNo);
         row.createCell((short) 6).setCellValue(engineNo);
         row.createCell((short) 7).setCellValue(model);
         row.createCell((short) 8).setCellValue(make);
         row.createCell((short) 9).setCellValue(costprice);
         row.createCell((short) 10).setCellValue(purchaseDate);
         row.createCell((short) 11).setCellValue(spare1);
         row.createCell((short) 12).setCellValue(spare2);
         row.createCell((short) 13).setCellValue(assetuser);
         row.createCell((short) 14).setCellValue(comments);
         row.createCell((short) 15).setCellValue(sighted);
         row.createCell((short) 16).setCellValue(function);
         row.createCell((short) 17).setCellValue(assetcode);
         row.createCell((short) 18).setCellValue(branchId);
         row.createCell((short) 19).setCellValue(categoryId);
         row.createCell((short) 20).setCellValue(batchId);
         i++;
  }    	
       // reads input file from an absolute path
    //   String filePath = "E:/Test/Download/MYPIC.JPG";
       String fileName = branchId+"By"+userName+"AssetDownLoadForProof.xls";
       String filePath = System.getProperty("user.home")+"\\Downloads";
       File downloadFile = new File(filePath);
       FileInputStream inStream = new FileInputStream(downloadFile);
        
       // if you want to use a relative path to context root:
       String relativePath = getServletContext().getRealPath("");
       System.out.println("relativePath = " + relativePath);
        
       // obtains ServletContext
       ServletContext context = getServletContext();
        
       // gets MIME type of the file
       String mimeType = context.getMimeType(filePath);
       if (mimeType == null) {        
           // set to binary type if MIME mapping not found
           mimeType = "application/octet-stream";
       }
       System.out.println("MIME type: " + mimeType);
        
       // modifies response
       response.setContentType(mimeType);
       response.setContentLength((int) downloadFile.length());
        
       // forces download
       String headerKey = "Content-Disposition";
       String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
       response.setHeader(headerKey, headerValue);
        
       // obtains response's output stream
       OutputStream outStream = response.getOutputStream();
        
       byte[] buffer = new byte[4096];
       int bytesRead = -1;
        
       while ((bytesRead = inStream.read(buffer)) != -1) {
           outStream.write(buffer, 0, bytesRead);
       }
        
       inStream.close();
       outStream.close();     
}
   }
    
    }
//}


