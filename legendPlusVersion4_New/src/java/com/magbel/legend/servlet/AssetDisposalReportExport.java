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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook; 
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class AssetDisposalReportExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String user_Id =(String) request.getSession().getAttribute("CurrentUser");
	 System.out.println("<<<<<<user_Id: "+user_Id);
	
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
//    String branch_Code = request.getParameter("initiatorSOLID");
	 String initiatorBranch = records.getCodeName("select branch from am_gb_User where User_Id = ? ",user_Id);
    String branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",initiatorBranch);
    String branch_Id = request.getParameter("branch");
    String selectedBranchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    String category = request.getParameter("category");
    String disposalReason = request.getParameter("DisposalReason");
    String fromDate = request.getParameter("FromDate");
    String endDate = request.getParameter("ToDate");
    if(!fromDate.equals("")){
    	String fromDD = fromDate.substring(0, 2);
    	String fromMM = fromDate.substring(3, 5);
    	String fromYYYY = fromDate.substring(6, 10);
    	fromDate = fromYYYY+"-"+fromMM+"-"+fromDD;
    }
    if(!endDate.equals("")){
    	String toDD = endDate.substring(0, 2);
    	String toMM = endDate.substring(3, 5);
    	String toYYYY = endDate.substring(6, 10);
    	endDate = toYYYY+"-"+toMM+"-"+toDD;
    }
    //System.out.println("<<<<<<fromDate: "+fromDate+"    <<<<<<endDate: "+endDate);
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = records.getCodeName("select User_Name from am_gb_User where User_Id = ? ",user_Id);
    String fileName = branch_Code+"By"+userName+"AssetDisposalReport.xlsx";    	
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
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
        
        String Selected_CategoryCode = records.getCodeName("select Category_Code from am_ad_category where Category_ID = ? ",category);
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  disposalReason: "+disposalReason+"  fromDate: "+fromDate+"  endDate: "+endDate);
     String ColQuery = "";
     if(branch_Id.equals("0")  && categoryCode.equals("0") && fromDate.equals("") && endDate.equals("") && disposalReason.equals("0")){
  	   System.out.println("======>>>>>>>No Selection: ");
  	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
  	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
  	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
  	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
  	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
  	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed'";
     }  
     
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && fromDate.equals("") && endDate.equals("") && disposalReason.equals("0")){	   
		   System.out.println("======>>>>>>>Branch Selected: ");
		     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
		     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
		     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
		     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
		     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND b.BRANCH_CODE = ?";
		     }
     
     if(branch_Id.equals("0")  && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")){
    	 System.out.println("======>>>>>>>Date Selected: ");
	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.transaction_date BETWEEN  ? AND  ?";
	} 
     
     if(branch_Id.equals("0")  && !categoryCode.equals("0") && fromDate.equals("") && endDate.equals("") && disposalReason.equals("0")){	   
  	   System.out.println("======>>>>>>>Category Selected: ");
	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND b.CATEGORY_CODE = ? ";   
  	     }
     
	 if(branch_Id.equals("0")  && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")){	   
	   System.out.println("======>>>>>>>Category and Date Selected: ");
	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.transaction_date BETWEEN  ? AND  ? AND b.CATEGORY_CODE = ? ";   
	     }
	 if(!branch_Id.equals("0")  && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")){	   
	   System.out.println("======>>>>>>>Branch and Date Selected: ");
	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.transaction_date BETWEEN  ? AND  ? AND b.BRANCH_CODE = ?";
	     }
	 if(branch_Id.equals("0")  && categoryCode.equals("0")  && !disposalReason.equals("0") && !fromDate.equals("") && !endDate.equals("")){	   
		   System.out.println("======>>>>>>>Disposal Reason and Date Selected: ");
		     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
		     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
		     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
		     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
		     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.transaction_date BETWEEN  ? AND  ? AND a.Disposal_reason = ? ";	
		     }	 
	 if(branch_Id.equals("0")  && categoryCode.equals("0")  && !disposalReason.equals("0") && fromDate.equals("") && endDate.equals("")){	   
		   System.out.println("======>>>>>>>Disposal Reason Selected: ");
		     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
		     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
		     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
		     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
		     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.Disposal_reason = ? ";	
		     }	 
   if(!branch_Id.equals("0")  && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")){
	   System.out.println("======>>>>>>>Branch, Category and Date Selection: ");
	     ColQuery ="SELECT  c.category_name AS category_name,b.BRANCH_CODE AS BRANCH_CODE,b.Asset_id AS Asset_id,b.Description AS Description,b.TOTAL_LIFE AS LIFE_SPAN,d.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "b.Cost_Price AS COST_PRICE,b.monthly_dep AS DEPRECIATION_CHARGES,b.Accum_dep AS ACCUMULATED_DEPRECIATION,b.NBV AS NET_BOOK_VALUE,a.transaction_date AS DISPOSAL_DATE,"
	     		+ "b.Date_purchased AS PURCHASE_DATE,a.Disposal_Amount AS DISPOSAL_PROCEEDS,a.Disposal_Amount AS DISPOSAL_AMOUNT,a.Profit_Loss AS PROFIT_AMOUNT,a.Disposal_reason AS Disposal_Reason,"
	     		+ "b.Asset_User AS ASSET_USER,b.BAR_CODE AS BarCode_VehicleNo "
	     		+ "FROM Disposal_view a, am_Asset b, am_ad_category c, am_ad_branch d, am_gb_company comp "
	     		+ "WHERE a.Asset_id = b.Asset_id and b.CATEGORY_CODE = c.category_code and b.BRANCH_CODE = d.BRANCH_CODE and b.Asset_Status = 'Disposed' AND a.transaction_date BETWEEN  ? AND  ? AND b.BRANCH_CODE = ? AND b.CATEGORY_CODE = ?";
	     }   
  // System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAssetDisposalReportRecords(ColQuery,branch_Id,categoryCode,disposalReason,fromDate,endDate);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/NO");
         rowhead.createCell((short) 1).setCellValue("BRANCH CODE");
         rowhead.createCell((short) 2).setCellValue("BRANCH NAME");
         rowhead.createCell((short) 3).setCellValue("CATEGORY NAME");
         rowhead.createCell((short) 4).setCellValue("ASSET ID");
         rowhead.createCell((short) 5).setCellValue("ASSET DESCRIPTION");
         rowhead.createCell((short) 6).setCellValue("LIFE SPAN(MONTH)");
         rowhead.createCell((short) 7).setCellValue("COST_PRICE");
		 rowhead.createCell((short) 8).setCellValue("DEPRECIATION CHARGES");
		 rowhead.createCell((short) 9).setCellValue("ACCUMULATED DEPRECIATION");
		 rowhead.createCell((short) 10).setCellValue("NET BOOK VALUE");
		 rowhead.createCell((short) 11).setCellValue("DISPOSAL DATE");
		 rowhead.createCell((short) 12).setCellValue("PURCHASE DATE");
		 rowhead.createCell((short) 13).setCellValue("DISPOSAL PROCEEDS");
		 rowhead.createCell((short) 14).setCellValue("DISPOSAL AMOUNT");
		 rowhead.createCell((short) 15).setCellValue("PROFIT AMOUNT");
		 rowhead.createCell((short) 16).setCellValue("DISPOSAL  REASON");
		 rowhead.createCell((short) 17).setCellValue("ASSET USER");
		 rowhead.createCell((short) 18).setCellValue("QR CODE/VEHICLE NO");

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String oldassetId =  newassettrans.getOldassetId();
			String branchName =  newassettrans.getBranchName();
			String categoryName =  newassettrans.getCategoryName();
			String Description = newassettrans.getDescription();   
//			String assetuser = newassettrans.getAssetUser();
			String qrCode = newassettrans.getQrCode();
			branchCode = newassettrans.getBranchCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double depchargetoDate = newassettrans.getDeprChargeToDate();
			String disposalDate = newassettrans.getDisposalDate();
			String purchaseDate = newassettrans.getDatepurchased();
			double lifeSpan = newassettrans.getLifeSpan();
			double disposalAmount = newassettrans.getDisposalAmount();
			double disposalProceed = newassettrans.getDisposalProceed();
			double profitLoss = newassettrans.getProfitAmount();
			if(disposalAmount==0) {profitLoss = 0.00;}
//			String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			disposalReason = newassettrans.getDisposeReason();
			String assetUser = newassettrans.getAssetUser();
//			disposalReason = records.getCodeName("select description from am_ad_disposalReasons where reason_code = "+disposalReason+"");
//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
//			System.out.println("======>depr_startDate====: "+depr_startDate);
			String yyyy = disposalDate.substring(0, 4);
//			System.out.println("======>yyyy: "+yyyy);
			String mm = disposalDate.substring(5, 7);
//			System.out.println("======>mm: "+mm);
			String dd = disposalDate.substring(8, 10);
			disposalDate = dd+"/"+mm+"/"+yyyy;
			String puryyyy = purchaseDate.substring(0, 4);
//			System.out.println("======>puryyyy: "+puryyyy);
			String purmm = purchaseDate.substring(5, 7);
//			System.out.println("======>purmm: "+purmm);
			String purdd = purchaseDate.substring(8, 10);
			purchaseDate = purdd+"/"+purmm+"/"+puryyyy;
//			System.out.println("======>disposalDate: "+disposalDate);

			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(String.valueOf(i));
			row.createCell((short) 1).setCellValue(branchCode);
            row.createCell((short) 2).setCellValue(branchName);
            row.createCell((short) 3).setCellValue(categoryName);
            row.createCell((short) 4).setCellValue(assetId);
            row.createCell((short) 5).setCellValue(Description);
            row.createCell((short) 6).setCellValue(lifeSpan);
			row.createCell((short) 7).setCellValue(costprice);
			row.createCell((short) 8).setCellValue(depchargetoDate);
			row.createCell((short) 9).setCellValue(accumDepr);
			row.createCell((short) 10).setCellValue(nbv);
			row.createCell((short) 11).setCellValue(disposalDate);
			row.createCell((short) 12).setCellValue(purchaseDate);
			row.createCell((short) 13).setCellValue(disposalProceed);
			row.createCell((short) 14).setCellValue(disposalAmount);
			row.createCell((short) 15).setCellValue(profitLoss);
			row.createCell((short) 16).setCellValue(disposalReason);
			row.createCell((short) 17).setCellValue(assetUser);
			row.createCell((short) 18).setCellValue(qrCode);

//			System.out.println("======>Description: "+Description);
            i++;
     }
	   OutputStream stream = response.getOutputStream();
//         new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
         workbook.write(stream);
         stream.close();
         System.out.println("Data is saved in excel file.");
    /* w.write();
     w.close(); */

 }
    } catch (Exception e)
    {
     e.getMessage();
    } 
    //finally
   // {
//     if (out != null)
//      out.close();
   // }
   }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}