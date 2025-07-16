package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
   







import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class BulkAssetProofSectionExportoOld extends HttpServlet
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
    System.out.println("<<<<<<branch_Code in BulkAssetProofSectionExport: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetDownLoadForverification.xls";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
	File tmpDir = new File(fileName);
	boolean exists = tmpDir.exists();	
	System.out.println("<<<<<<exists in BulkAssetProofSectionExport: "+exists);
	if(exists==true){
		File f1 = new File(fileName);
		if (f1.delete()) {
		    System.out.println("File " + f1.getName() + " is deleted.");
		} else {
		    System.out.println("File " + f1.getName() + " not found.");
		}
	}
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();

//    if(exists==false){   	

    

     String prooftranId = request.getParameter("prooftranId");
     String initiatorId = request.getParameter("initiatorId");
     Report rep = new Report();
     String ColQuery = "";
     String tableName = "Temp"+prooftranId;
     if(branch_Code!=null){ColQuery ="SELECT *FROM "+tableName+"";}
     else{ColQuery ="SELECT *FROM "+tableName+"";}
     java.util.ArrayList list =rep.getBulkAssetProofSqlRecords(ColQuery);
     if(list.size()!=0){
     WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
     WritableSheet s = w.createSheet("Demo", 0);

     s.addCell(new Label(0, 0, "S/NO"));
     s.addCell(new Label(1, 0, "ASSET_ID"));
     s.addCell(new Label(2, 0, "DESCRITION"));
     s.addCell(new Label(3, 0, "BAR CODE"));
     s.addCell(new Label(4, 0, "SBU CODE"));
     s.addCell(new Label(5, 0, "SUB CATEGORY CODE"));
     s.addCell(new Label(6, 0, "DEPARTMENT CODE"));
     s.addCell(new Label(7, 0, "SECTION CODE"));
     s.addCell(new Label(8, 0, "LOCATION"));
     s.addCell(new Label(9, 0, "STATE CODE"));
     s.addCell(new Label(10, 0, "SERIAL NO."));
     s.addCell(new Label(11, 0, "REGISTRATION NO"));
     s.addCell(new Label(12, 0, "ENGINE NO"));
     s.addCell(new Label(13, 0, "ASSET MODEL"));
     s.addCell(new Label(14, 0, "ASSET MAKE"));
     s.addCell(new Label(15, 0, "VENDOR NAME"));
     s.addCell(new Label(16, 0, "VENDOR ACCOUNT"));
     s.addCell(new Label(17, 0, "MAINTAINED BY"));
     s.addCell(new Label(18, 0, "COST PRICE"));
     s.addCell(new Label(19, 0, "PURCHASE DATE"));
     s.addCell(new Label(20, 0, "LPO"));
     s.addCell(new Label(21, 0, "COMPONENT"));
     s.addCell(new Label(22, 0, "COMPONENT BARCODE"));
     s.addCell(new Label(23, 0, "SPARE FIELD1"));
     s.addCell(new Label(24, 0, "SPARE FIELD2"));
     s.addCell(new Label(25, 0, "SPARE FIELD3"));
     s.addCell(new Label(26, 0, "SPARE FIELD4"));
     s.addCell(new Label(27, 0, "BRANCH ID"));
     s.addCell(new Label(28, 0, "INITIATED BY"));
     s.addCell(new Label(29, 0, "ASSET USER"));
     s.addCell(new Label(30, 0, "COMMENTS"));
     s.addCell(new Label(31, 0, "ASSET SIGHTED"));
     s.addCell(new Label(32, 0, "ASSET FUNCTIONING"));
     s.addCell(new Label(33, 0, "ASSET CODE"));
     s.addCell(new Label(34, 0, "CATEGORY ID"));
     s.addCell(new Label(35, 0, "BATCH ID"));  
     

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
//		 System.out.println("<<<<<<I: "+i+"    K: "+k);
		 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	
         String registration = newassettrans.getRegistrationNo();
         String Description = newassettrans.getDescription();
         String assetuser = newassettrans.getAssetUser();
         String reason1 = newassettrans.getPurchaseReason();
         String sbu = newassettrans.getSbuCode();
         String dept = newassettrans.getDeptCode();
         String deptCode = records.getCodeName("select DEPT_CODE from am_ad_department where DEPT_ID = "+dept+"");
         String subcatId = newassettrans.getSubcategoryCode();
         String subCatCode = records.getCodeName("select sub_category_Code from am_ad_sub_category where sub_category_ID = "+subcatId+"");         
//         String subcatcode = newassettrans.getSubcategoryCode();
         String location = newassettrans.getLocation();
         String locationCode = records.getCodeName("select LOCATION_CODE from AM_GB_LOCATION where LOCATION_ID = "+location+"");
                  
         String sectionId = newassettrans.getSectionCode();
         String sectionCode = records.getCodeName("select SECTION_CODE from am_ad_section where SECTION_ID = "+sectionId+"");
         String state = newassettrans.getState();
         String stateCode = records.getCodeName("select state_code from am_gb_states where STATE_ID = "+state+"");
         String lpo = newassettrans.getLpo();
         if((lpo.equals(null))||(lpo==null)){lpo="";}
         String vendorAccount = newassettrans.getVendorAC();
         String vendorName = newassettrans.getSupplierName();
         String maintainedby = newassettrans.getAssetMaintenance();
         String comments = newassettrans.getComments();
         String sighted = newassettrans.getAssetsighted();
         String function = newassettrans.getAssetfunction();
         int assetcode = Integer.parseInt(newassettrans.getAssetCode());
         String categoryId = newassettrans.getCategoryCode();                
         String barcode = newassettrans.getBarCode();
         String assetId = newassettrans.getAssetId();
         String subcat = newassettrans.getSubcategoryCode();
         String batchId = newassettrans.getProjectCode();
         String costprice = Double.toString(newassettrans.getCostPrice());
		  String model = newassettrans.getAssetModel();
		  String make = newassettrans.getAssetMake();
		  String spare1 = newassettrans.getSpare1();
		  String spare2 = newassettrans.getSpare2();
		  String spare3 = newassettrans.getSpare3();
		  String spare4 = newassettrans.getSpare4();
		  String spare5 = newassettrans.getSpare5();
		  String spare6 = newassettrans.getSpare6();	  				  
		  String registrationNo = newassettrans.getRegistrationNo();
		  String sbucode = newassettrans.getSbuCode();
		  String serialNo = newassettrans.getSerialNo();
		  String engineNo = newassettrans.getEngineNo();
		  String purchaseDate = newassettrans.getDatepurchased();
		  String purchaseReason = newassettrans.getDatepurchased();
		  String deprecStartDate = newassettrans.getEffectiveDate();
		  String deprecEndDate = newassettrans.getPostingDate();
		  String vendorAcct = newassettrans.getVendorAC();
		  String suppliedBy = newassettrans.getSupplierName();
		  String maintainBy = newassettrans.getAssetMaintenance();
		  System.out.println("<<<<<<sbucode: "+sbucode+"  subcat: "+subcat+"  dept: "+dept+"  sectionId: "+sectionId);
//		  String initiator = newassettrans.getUserID();
 //           HSSFRow row = sheet.createRow((short) i);
          System.out.println("<<<<<In Servlet sbucode: "+sbucode+"  spare1: "+spare1+"  spare2: "+spare2+"  spare3: "+spare3+"  spare4: "+spare4+"  spare5: "+spare5+"  spare6: "+spare6);

           s.addCell(new Label(0, i, String.valueOf(k)));
           s.addCell(new Label(1, i, assetId));
           s.addCell(new Label(2, i, Description));
           s.addCell(new Label(3, i, barcode));
           s.addCell(new Label(4, i, sbucode));
           s.addCell(new Label(5, i, subCatCode));
           s.addCell(new Label(6, i, deptCode));
           s.addCell(new Label(7, i, sectionCode));
           s.addCell(new Label(8, i, locationCode));
           s.addCell(new Label(9, i, stateCode));
           s.addCell(new Label(10, i, serialNo));
           s.addCell(new Label(11, i, registrationNo));
           s.addCell(new Label(12, i, engineNo));
           s.addCell(new Label(13, i, model));
           s.addCell(new Label(14, i, make));
           s.addCell(new Label(15, i, suppliedBy));
           s.addCell(new Label(16, i, vendorAcct));
           s.addCell(new Label(17, i, maintainBy));
           s.addCell(new Label(18, i, costprice));
           s.addCell(new Label(19, i, purchaseDate));
           s.addCell(new Label(20, i, lpo));
           s.addCell(new Label(21, i, spare1));
           s.addCell(new Label(22, i, spare2));
           s.addCell(new Label(23, i, spare3));
           s.addCell(new Label(24, i, spare4));
           s.addCell(new Label(25, i, spare5));
           s.addCell(new Label(26, i, spare6));
           s.addCell(new Label(27, i, branch_Code));
           s.addCell(new Label(28, i, initiatorId));
           s.addCell(new Label(29, i, assetuser));
           s.addCell(new Label(30, i, comments));
           s.addCell(new Label(31, i, sighted));
           s.addCell(new Label(32, i, function));
           s.addCell(new Label(33, i, String.valueOf(assetcode)));
           s.addCell(new Label(34, i, categoryId));
           s.addCell(new Label(35, i, batchId));           
            i++;
     }
     w.write();
     w.close();
//     String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
	 String q1 = "IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].["+tableName+"]') AND type in (N'U')) BEGIN drop table [dbo].["+tableName+"] END";
     ad.updateAssetStatusChange(q1);
 }

    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } 
   }
   
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }   
}