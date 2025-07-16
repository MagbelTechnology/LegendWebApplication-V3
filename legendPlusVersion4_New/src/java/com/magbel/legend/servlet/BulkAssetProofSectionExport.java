package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class BulkAssetProofSectionExport extends HttpServlet
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
//            java.util.ArrayList list =rep.getBulkAssetProofSqlRecords(ColQuery);
            java.util.ArrayList list =rep.getBulkAssetProofSqlRecords(ColQuery,branch_Code,prooftranId);
            if(list.size()!=0){ 
 
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Demo");
                HSSFRow rowhead = sheet.createRow((int) 0);

                rowhead.createCell((short) 0).setCellValue("S/NO");
                rowhead.createCell((short) 1).setCellValue("ASSET_ID");
                rowhead.createCell((short) 2).setCellValue("Description");
                rowhead.createCell((short) 3).setCellValue("BAR CODE");
                rowhead.createCell((short) 4).setCellValue("SBU CODE");
                rowhead.createCell((short) 5).setCellValue("SUB CATEGORY CODE");
                rowhead.createCell((short) 6).setCellValue("DEPARTMENT CODE");
                rowhead.createCell((short) 7).setCellValue("SECTION CODE");
                rowhead.createCell((short) 8).setCellValue("LOCATION");
                rowhead.createCell((short) 9).setCellValue("STATE CODE");
                rowhead.createCell((short) 10).setCellValue("SERIAL NO.");
                rowhead.createCell((short) 11).setCellValue("REGISTRATION NO");
                rowhead.createCell((short) 12).setCellValue("ENGINE NO");
                rowhead.createCell((short) 13).setCellValue("ASSET MODEL");
                rowhead.createCell((short) 14).setCellValue("ASSET MAKE");
                rowhead.createCell((short) 15).setCellValue("VENDOR NAME");
                rowhead.createCell((short) 16).setCellValue("VENDOR ACCOUNT");
                rowhead.createCell((short) 17).setCellValue("MAINTAINED BY");
                rowhead.createCell((short) 18).setCellValue("COST PRICE");
                rowhead.createCell((short) 19).setCellValue("PURCHASE DATE");
                rowhead.createCell((short) 20).setCellValue("LPO");
                rowhead.createCell((short) 21).setCellValue("COMPONENT");
                rowhead.createCell((short) 22).setCellValue("COMPONENT BARCODE");
                rowhead.createCell((short) 23).setCellValue("SPARE FIELD1");
                rowhead.createCell((short) 24).setCellValue("SPARE FIELD2");
                rowhead.createCell((short) 25).setCellValue("SPARE FIELD3");
                rowhead.createCell((short) 26).setCellValue("SPARE FIELD4");
                rowhead.createCell((short) 27).setCellValue("BRANCH CODE");
                rowhead.createCell((short) 28).setCellValue("INITIATED BY");
                rowhead.createCell((short) 29).setCellValue("ASSET USER");
                rowhead.createCell((short) 30).setCellValue("INITIATED BRANCH ID");
                rowhead.createCell((short) 31).setCellValue("COMMENTS");
                rowhead.createCell((short) 32).setCellValue("ASSET SIGHTED");
                rowhead.createCell((short) 33).setCellValue("ASSET FUNCTIONING");
                rowhead.createCell((short) 34).setCellValue("ASSET CODE");
                rowhead.createCell((short) 35).setCellValue("CATEGORY ID");
                rowhead.createCell((short) 36).setCellValue("BATCH ID");

                int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
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
                    String branchId = newassettrans.getBranchCode();
                    System.out.println("<<<<<<sbucode: "+sbucode+"  subcat: "+subcat+"  dept: "+dept+"  sectionId: "+sectionId);
//		  String initiator = newassettrans.getUserID();
                    //           HSSFRow row = sheet.createRow((short) i);
                    System.out.println("<<<<<In Servlet sbucode: "+sbucode+"  spare1: "+spare1+"  spare2: "+spare2+"  spare3: "+spare3+"  spare4: "+spare4+"  spare5: "+spare5+"  spare6: "+spare6);

                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((short) 0).setCellValue(k);
                    row.createCell((short) 1).setCellValue(assetId);
                    row.createCell((short) 2).setCellValue(Description);
                    row.createCell((short) 3).setCellValue(barcode);
                    row.createCell((short) 4).setCellValue(sbucode);
                    row.createCell((short) 5).setCellValue(subCatCode);
                    row.createCell((short) 6).setCellValue(deptCode);
                    row.createCell((short) 7).setCellValue(sectionCode);
                    row.createCell((short) 8).setCellValue(locationCode);
                    row.createCell((short) 9).setCellValue(stateCode);
                    row.createCell((short) 10).setCellValue(serialNo);
                    row.createCell((short) 11).setCellValue(registrationNo);
                    row.createCell((short) 12).setCellValue(engineNo);
                    row.createCell((short) 13).setCellValue(model);
                    row.createCell((short) 14).setCellValue(make);
                    row.createCell((short) 15).setCellValue(suppliedBy);
                    row.createCell((short) 16).setCellValue(vendorAcct);
                    row.createCell((short) 17).setCellValue(maintainBy);
                    row.createCell((short) 18).setCellValue(costprice);
                    row.createCell((short) 19).setCellValue(purchaseDate);
                    row.createCell((short) 20).setCellValue(lpo);
                    row.createCell((short) 21).setCellValue(spare1);
                    row.createCell((short) 22).setCellValue(spare2);
                    row.createCell((short) 23).setCellValue(spare3);
                    row.createCell((short) 24).setCellValue(spare4);
                    row.createCell((short) 25).setCellValue(spare5);
                    row.createCell((short) 26).setCellValue(spare6);
                    row.createCell((short) 27).setCellValue(branch_Code);
                    row.createCell((short) 28).setCellValue(initiatorId);
                    row.createCell((short) 29).setCellValue(assetuser);
                    row.createCell((short) 30).setCellValue(branchId);
                    row.createCell((short) 31).setCellValue(comments);
                    row.createCell((short) 32).setCellValue(sighted);
                    row.createCell((short) 33).setCellValue(function);
                    row.createCell((short) 34).setCellValue(assetcode);
                    row.createCell((short) 35).setCellValue(categoryId);
                    row.createCell((short) 36).setCellValue(batchId);
                    System.out.println("<<<<<<record No: "+i);
                    i++;
                }
                System.out.println("About to Create the Stream...");
                OutputStream stream = response.getOutputStream();
                System.out.println("About to Write to Stream...");
                workbook.write(stream);
                System.out.println(" Write to Stream Finish...");
                stream.close();

 //    String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
                String q1 = "IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].["+tableName+"]') AND type in (N'U')) BEGIN drop table [dbo].["+tableName+"] END";
                ad.updateAssetStatusChange(q1);
                System.out.println("File Exported Successfully...");
            }

        } catch (Exception e) 
        {
            e.getMessage();
            //throw new ServletException("Exception in Excel Sample Servlet", e);
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