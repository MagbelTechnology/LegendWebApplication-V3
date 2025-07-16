package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
   
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
   

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class ExcelVendorExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        String branch_Code = request.getParameter("initiatorSOLID");
        System.out.println("<<<<<<branch_Code: "+branch_Code);
        String userName = request.getParameter("userName");
//        String fileName = branch_Code+"By"+userName+"BulkStockIssued.xls";
        String fileName = "Manage Vendor Details.xls";
        
        String filePath = System.getProperty("user.home")+"\\Downloads";
        File tmpDir = new File(filePath);
        boolean exists = tmpDir.exists();
/*	if(exists==true){
		File f1 = new File(filePath);
		if (f1.delete()) {
		    System.out.println("File " + f1.getName() + " is deleted.");
		} else {
		    System.out.println("File " + f1.getName() + " not found.");
		}
	}*/
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment; filename="+fileName+"");
        String userClass = (String) request.getSession().getAttribute("UserClass");
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();



            Report rep = new Report();

            String colQuery = "SELECT Vendor_ID,Vendor_Code,Vendor_Name,VendorBranchId,Contact_Person,Contact_Address,Vendor_Phone ,"
            		+ "Vendor_fax,Vendor_email,Vendor_State ,Acquisition_Vendor,Maintenance_Vendor,account_number ,"
            		+ "Vendor_Status,User_ID,Create_date ,account_type  FROM am_ad_vendor WHERE Vendor_ID IS NOT NULL "
            		+ "AND VENDOR_STATUS='ACTIVE' ORDER BY VENDOR_NAME ASC";
            java.util.ArrayList list =rep.getVendorDetails(colQuery);
            if(list.size()!=0){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Bulk Stock Issuance Details Export");
                HSSFRow rowhead = sheet.createRow((int) 0);

                rowhead.createCell((short) 0).setCellValue("S/NO");
                rowhead.createCell((short) 1).setCellValue("VENDOR ID");
                rowhead.createCell((short) 2).setCellValue("VENDOR CODE");
                rowhead.createCell((short) 3).setCellValue("VENDOR NAME");
                rowhead.createCell((short) 4).setCellValue("VENDOR BRANCH ID");
                rowhead.createCell((short) 5).setCellValue("CONTACT PERSON");
                rowhead.createCell((short) 6).setCellValue("CONTACT ADDRESS");
                rowhead.createCell((short) 7).setCellValue("VENDOR PHONE");
                rowhead.createCell((short) 8).setCellValue("VENDOR FAX");
                rowhead.createCell((short) 9).setCellValue("VENDOR EMAIL");
                rowhead.createCell((short) 10).setCellValue("VENDOR STATE");
                rowhead.createCell((short) 11).setCellValue("ACQUISITION VENDOR");
                rowhead.createCell((short) 12).setCellValue("MAINTENANCE VENDOR");
                rowhead.createCell((short) 13).setCellValue("ACCOUNT NUMBER");
                rowhead.createCell((short) 14).setCellValue("VENDOR STATUS");
                rowhead.createCell((short) 15).setCellValue("USER ID");
                rowhead.createCell((short) 16).setCellValue("CREATE DATE");
                rowhead.createCell((short) 17).setCellValue("ACCOUNT TYPE");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    
                	 String vendorId =  newassettrans.getAssetId();
                     String vendorCode =  newassettrans.getAssetCode();
                     String vendorName = newassettrans.getVendorName();
                     String vendorBranchId = newassettrans.getBranchId();
                     String contactPerson = newassettrans.getAction();
                     String contactAddress = newassettrans.getComments();
                     String vendorPhone = newassettrans.getAssetCode();
                     String vendorFax = newassettrans.getAssetLedger();
                     String vendorEmail = newassettrans.getAssetfunction();
                     String vendorState =  newassettrans.getAssetModel();
                     String acquisitionVendor = newassettrans.getAssetSerialNo();
                     String maintenanceVendor = newassettrans.getAssetMaintenance();
                     String accountNumber = newassettrans.getGlAccount();
                     String vendorStatus = newassettrans.getAssetStatus();
                     String userId = newassettrans.getUserID();
                     String createDate = newassettrans.getPostingDate();
                     String accountType = newassettrans.getAssetType();

                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((short) 0).setCellValue(String.valueOf(i));
                    row.createCell((short) 1).setCellValue(vendorId);
                    row.createCell((short) 2).setCellValue(vendorCode);
                    row.createCell((short) 3).setCellValue(vendorName);
                    row.createCell((short) 4).setCellValue(vendorBranchId);
                    row.createCell((short) 5).setCellValue(contactPerson);
                    row.createCell((short) 6).setCellValue(contactAddress);
                    row.createCell((short) 7).setCellValue(vendorPhone);
                    row.createCell((short) 8).setCellValue(vendorFax);
                    row.createCell((short) 9).setCellValue(vendorEmail);
                    row.createCell((short) 10).setCellValue(vendorState);
                    row.createCell((short) 11).setCellValue(acquisitionVendor);
                    row.createCell((short) 12).setCellValue(maintenanceVendor);
                    row.createCell((short) 13).setCellValue(accountNumber);
                    row.createCell((short) 14).setCellValue(vendorStatus);
                    row.createCell((short) 15).setCellValue(userId);
                    row.createCell((short) 16).setCellValue(createDate);
                    row.createCell((short) 17).setCellValue(accountType);
                   
                    i++;
                }
                OutputStream stream = response.getOutputStream();
              workbook.write(stream);
              stream.close();
              System.out.println("Data is saved in excel file.");

            }
        }
        } catch (Exception e)
        {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally
        {
//     if (out != null)
//      out.close();
        }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}