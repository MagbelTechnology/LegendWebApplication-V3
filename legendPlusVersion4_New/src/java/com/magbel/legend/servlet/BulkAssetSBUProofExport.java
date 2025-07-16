package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import magma.AssetRecordsBean;
   
public class BulkAssetSBUProofExport extends HttpServlet
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
	String userClass = (String) request.getSession().getAttribute("UserClass");
	if (!userClass.equals("NULL") || userClass!=null){
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
    System.out.println("<<<<<<branch_Code in BulkAssetSBUProofExport: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetProofDownLoadForFincon.xls";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
	File tmpDir = new File(fileName);
	boolean exists = tmpDir.exists();	
	System.out.println("<<<<<<exists in BulkAssetSBUProofExport: "+exists);
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

     String branch_Id = request.getParameter("BRANCH_ID");
     String branchCode = request.getParameter("BRANCH_CODE");
     String prooftranId = request.getParameter("prooftranId");
     String initiatorId = request.getParameter("initiatorId");
     String mails = request.getParameter("mails1");
     String subject = request.getParameter("subject1");
     String msgText   = request.getParameter("msgText1");
     String otherparam  = request.getParameter("otherparam"); 
//     String copymails = records.getCodeName("select EMAIL_ADDRESS from am_branch_Manager where BRANCH_CODE = '"+branchCode+"'");     
     Report rep = new Report();
     String ColQuery = "";
     ColQuery ="SELECT a.ASSET_ID,a.Description,a.BRANCH_ID,a.CATEGORY_ID,a.BAR_CODE,a.SBU_CODE AS NEW_SBUCODE,b.SBU_CODE as OLD_SBUCODE,a.SUB_CATEGORY_ID," +
     		 "a.Dept_ID,a.Section_id,a.Location,a.State,a.Registration_No,a.Asset_Serial_No,a.Asset_Make,a.Asset_Model,a.Asset_Engine_No,a.Spare_1,a.Spare_2,a.Spare_3,a.Spare_4,a.Spare_5,a.Spare_6,a.ASSET_USER " +
     		 "FROM am_Asset_Proof_Selection a, AM_ASSET b where a.ASSET_ID = b.ASSET_ID and a.BATCH_ID = '"+prooftranId+"' " +
     		 "UNION "+
    		 "SELECT a.ASSET_ID,a.Description,a.BRANCH_ID,a.CATEGORY_ID,a.BAR_CODE,a.SBU_CODE AS NEW_SBUCODE,'' as OLD_SBUCODE,a.SUB_CATEGORY_ID,a.Dept_ID," +
    		 "a.Section_id,a.Location,a.State,a.Registration_No,a.Asset_Serial_No,a.Asset_Make,a.Asset_Model,a.Asset_Engine_No,a.Spare_1,a.Spare_2,a.Spare_3,a.Spare_4,a.Spare_5," +
    		 "a.Spare_6,a.ASSET_USER FROM am_Asset_Proof_Selection a, AM_ASSET b where a.BATCH_ID = '"+prooftranId+"'";
     java.util.ArrayList list =rep.getBulkAssetProofforFinconSqlRecords(ColQuery);
     if(list.size()!=0){
     WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
     WritableSheet s = w.createSheet("Demo", 0);

     s.addCell(new Label(0, 0, "S/NO"));
     s.addCell(new Label(1, 0, "ASSET_ID"));
     s.addCell(new Label(2, 0, "DESCRITION"));
     s.addCell(new Label(3, 0, "BAR CODE"));
     s.addCell(new Label(4, 0, "NEW SBU CODE"));
     s.addCell(new Label(5, 0, "OLD SBU CODE"));
     s.addCell(new Label(6, 0, "SUB CATEGORY NAME"));
     s.addCell(new Label(7, 0, "DEPARTMENT NAME"));
     s.addCell(new Label(8, 0, "SECTION NAME"));
     s.addCell(new Label(9, 0, "LOCATION NAME"));
     s.addCell(new Label(10, 0, "STATE NAME"));
     s.addCell(new Label(11, 0, "SERIAL NO."));
     s.addCell(new Label(12, 0, "REGISTRATION NO"));
     s.addCell(new Label(13, 0, "ENGINE NO"));
     s.addCell(new Label(14, 0, "ASSET MODEL"));
     s.addCell(new Label(15, 0, "ASSET MAKE"));
     s.addCell(new Label(16, 0, "COMPONENT"));
     s.addCell(new Label(17, 0, "COMPONENT BARCODE"));
     s.addCell(new Label(18, 0, "SPARE FIELD1"));
     s.addCell(new Label(19, 0, "SPARE FIELD2"));
     s.addCell(new Label(20, 0, "SPARE FIELD3"));
     s.addCell(new Label(21, 0, "SPARE FIELD4"));
     s.addCell(new Label(22, 0, "SOL ID"));
     s.addCell(new Label(23, 0, "ASSET USER"));
     s.addCell(new Label(24, 0, "CATEGORY NAME"));

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
         String deptName = records.getCodeName("select DEPT_NAME from am_ad_department where DEPT_ID = "+dept+"");
         String subcatId = newassettrans.getSubcategoryCode();
         String subCatName = records.getCodeName("select sub_category_name from am_ad_sub_category where sub_category_ID = "+subcatId+"");
         String subcatcode = newassettrans.getSubcategoryCode();
         String location = newassettrans.getLocation();
         String locationName = records.getCodeName("select LOCATION from AM_GB_LOCATION where LOCATION_ID = "+location+"");
         String sectionId = newassettrans.getSectionCode();
         String sectionName = records.getCodeName("select SECTION_NAME from am_ad_section where SECTION_ID = "+sectionId+"");
         String state = newassettrans.getState();
         String stateName = records.getCodeName("select state_name from am_gb_states where STATE_ID = "+state+"");
/*
         String lpo = newassettrans.getLpo();
         String vendorAccount = newassettrans.getVendorAC();
         String vendorName = newassettrans.getSupplierName();
         String maintainedby = newassettrans.getAssetMaintenance();
         String comments = newassettrans.getAssetUser();
         String sighted = newassettrans.getAssetsighted();
         String function = newassettrans.getAssetfunction();
         int assetcode = Integer.parseInt(newassettrans.getAssetCode());
         */
         String categoryId = newassettrans.getCategoryCode();  
         String categoryName = records.getCodeName("select CATEGORY_NAME from am_ad_category where CATEGORY_ID = "+categoryId+"");
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
		  String newsbucode = newassettrans.getNewsbuCode();
		  String oldsbucode = newassettrans.getOldsbuCode();
		  String serialNo = newassettrans.getSerialNo();
		  String engineNo = newassettrans.getEngineNo();
/*
		  String purchaseDate = newassettrans.getDatepurchased();
		  String purchaseReason = newassettrans.getDatepurchased();
		  String deprecStartDate = newassettrans.getEffectiveDate();
		  String deprecEndDate = newassettrans.getPostingDate();
		  String vendorAcct = newassettrans.getVendorAC();
		  String suppliedBy = newassettrans.getSupplierName();
		  String maintainBy = newassettrans.getAssetMaintenance();
		  */
//		  String initiator = newassettrans.getUserID();
 //           HSSFRow row = sheet.createRow((short) i);
   
           s.addCell(new Label(0, i, String.valueOf(k)));
           s.addCell(new Label(1, i, assetId));
           s.addCell(new Label(2, i, Description));
           s.addCell(new Label(3, i, barcode));
           s.addCell(new Label(4, i, newsbucode));
           s.addCell(new Label(5, i, oldsbucode));
           s.addCell(new Label(6, i, subCatName));
           s.addCell(new Label(7, i, deptName));
           s.addCell(new Label(8, i, sectionName));
           s.addCell(new Label(9, i, locationName));
           s.addCell(new Label(10, i, stateName));
           s.addCell(new Label(11, i, serialNo));
           s.addCell(new Label(12, i, registrationNo));
           s.addCell(new Label(13, i, engineNo));
           s.addCell(new Label(14, i, model));
           s.addCell(new Label(15, i, make));
           s.addCell(new Label(16, i, spare1));
           s.addCell(new Label(17, i, spare2));
           s.addCell(new Label(18, i, spare3));
           s.addCell(new Label(19, i, spare4));
           s.addCell(new Label(20, i, spare5));
           s.addCell(new Label(21, i, spare6));
           s.addCell(new Label(22, i, branch_Code));
           s.addCell(new Label(23, i, assetuser));
           s.addCell(new Label(24, i, categoryName));          
            i++;
     }
     w.write();
     w.close();
     String updateRcord =  "UPDATE am_Asset_Proof_Selection SET EXPORTED = 'YES' WHERE BATCH_ID = '"+prooftranId+"' AND EXPORTED IS NULL ";
     ad.updateAssetStatusChange(updateRcord);
 }
     System.out.print("AssetProof mails: :>>> "+mails+"   subject: "+subject+"   msgText: "+msgText);
     mail.sendMailWithAttachment(mails,subject,msgText,branch_Id,fileName);  
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
//     if (out != null)
//      out.close();
    }
   }
   }
   
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}