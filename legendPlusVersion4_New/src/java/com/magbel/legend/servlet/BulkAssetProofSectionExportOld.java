package com.magbel.legend.servlet;
import java.io.IOException;
import java.io.OutputStream;
   







import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
   












import javax.servlet.http.HttpSession;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class BulkAssetProofSectionExportOld extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
	magma.AssetRecordsBean bd = null;
    try
    {
 //   	ad = new AssetRecordsBean();
     response.setContentType("application/vnd.ms-excel");
 
     response.setHeader("Content-Disposition", 
    "attachment; filename=AssetProof.xls");
     String branch_Id = request.getParameter("BRANCH_ID");
     String listtotal = request.getParameter("list");
     String prooftranId = request.getParameter("prooftranId");
     Report rep = new Report();
     String ColQuery = "";
     String fileName = branch_Id+"AssetProof.xls";
//     if(branch_Id!=null){ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BRANCH_ID = '"+branch_Id+"' AND BATCH_ID = '"+prooftranId+"'";}
//     else{ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BATCH_ID = '"+prooftranId+"'";}
//     java.util.ArrayList list =rep.getWorkBookSqlRecords(ColQuery);

     
     HttpSession session = request.getSession();
     ArrayList list = (ArrayList) session.getAttribute("ulist");
     System.out.println("<<<<<<MyList listtotal: "+listtotal);
     if(Integer.parseInt(listtotal)!=0){
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
     System.out.println("<<<<<<yourList.size(): "+list.size());
     
	 for(int k=0;k<Integer.parseInt(listtotal);k++)
     {
		 bd = (magma.AssetRecordsBean) list.get(k);
		 System.out.println("<<<<<<I: "+i+"    K: "+k);
         String registration = bd.getRegistration_no();
         String Description = bd.getDescription();
         String assetuser = bd.getUser();
         String reason1 = bd.getReason();
         String sbu = bd.getSbu_code();
         String dept = bd.getDepartment_id();
         String subcatId = bd.getSub_category_id();
         String subcatcode = bd.getSubcatCode();
         String location = bd.getLocation();
         String sectionId = bd.getSection_id();
         String state = bd.getState();
         String lpo = bd.getLpo();
         String vendorAccount = bd.getVendor_account();
         String vendorName = bd.getSupplied_by();
         String maintainedby = bd.getMaintained_by();
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
         String monthlyDep = bd.getMonthlydep();
         String accumDep = bd.getAccumdep();
         String nbv = bd.getNbv();
         String improvcost = bd.getImproveCost();
         String improvmonthlydep = bd.getImproveMonthlydep();
			  String improveaccumdep = bd.getImproveAccumdep();
			  String improvnbv = bd.getImproveNbv();
			  String model = bd.getModel();
			  String make = bd.getMake();
			  String spare1 = bd.getSpare_1();
			  String spare2 = bd.getSpare_2();
			  String spare3 = bd.getSpare_3();
			  String spare4 = bd.getSpare_4();
			  String spare5 = bd.getSpare_5();
			  String spare6 = bd.getSpare_6();	  				  
			  String registrationNo = bd.getRegistration_no();
			  String sbucode = bd.getSbu_code();
			  String serialNo = bd.getSerial_number();
			  String engineNo = bd.getEngine_number();
			  String purchaseDate = bd.getDate_of_purchase();
			  String purchaseReason = bd.getReason();
			  String deprecStartDate = bd.getDepreciation_start_date();
			  String deprecEndDate = bd.getDepreciation_end_date();
			  String vendorAcct = bd.getVendor_account();
			  String suppliedBy = bd.getSupplied_by();
			  String maintainBy = bd.getMaintained_by();
			  String initiator = bd.getUser_id();
 //           HSSFRow row = sheet.createRow((short) i);
           
           s.addCell(new Label(0, i, String.valueOf(k)));
           s.addCell(new Label(1, i, assetId));
           s.addCell(new Label(2, i, Description));
           s.addCell(new Label(3, i, barcode));
           s.addCell(new Label(4, i, sbucode));
           s.addCell(new Label(5, i, subcat));
           s.addCell(new Label(6, i, dept));
           s.addCell(new Label(7, i, sectionId));
           s.addCell(new Label(8, i, location));
           s.addCell(new Label(9, i, state));
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
           s.addCell(new Label(27, i, branch_Id));
           s.addCell(new Label(28, i, initiator));
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
    
     out.print("<script>alert('Your Asset Proof Selection has been exported to Excel file in your Downloads folder with File Name "+fileName+" ')</script>");
     out.print("<script>window.location='DocumentHelp.jsp?np=AssetProofByBranchAdd'</script>");
 }
else{
    out.print("<script>alert('No Record to export to Excel ')</script>");
    out.print("<script>window.location='DocumentHelp.jsp?np=AssetProofByBranchAdd'</script>");
}
     
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
     if (out != null)
      out.close();
    }
   }
}