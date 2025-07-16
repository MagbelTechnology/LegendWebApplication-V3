package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.HtmlUtility;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class DepreciationChargesMonthlyExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	HtmlUtility htmlUtil;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   String userClass = (String) request.getSession().getAttribute("UserClass");
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        htmlUtil = new HtmlUtility();
        String branch_Code = request.getParameter("initiatorSOLID");
        System.out.println("<<<<<<branch_Code: "+branch_Code);
        String status = "COST PRICE";
        String userName = request.getParameter("userName");
        String fileName = branch_Code+"By"+userName+"DepreciationChargesExport.xlsx";
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
        response.setIntHeader("Content-Length", -1);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", 
       "attachment; filename="+fileName+"");
        try
        {
        if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();

//    if(exists==false){
            int batchSize = 1000;

            String chargeYear = request.getParameter("reportYear");
            String reportDate = request.getParameter("reportDate");
            String branch_Id = request.getParameter("branch");
            String branchCode = htmlUtil.getCodeName("SELECT BRANCH_CODE FROM AM_AD_BRANCH WHERE BRANCH_ID=?", branch_Id);
            String tranId = request.getParameter("tranId");
            String categoryId = request.getParameter("category");
            String mails = request.getParameter("mails1");
            String subject = request.getParameter("subject1");
            String msgText   = request.getParameter("msgText1");
            String otherparam  = request.getParameter("otherparam");
            Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode);
            String ColQuery = "";
            if(branch_Id.equals("0")  && categoryId.equals("0")){
                ColQuery ="SELECT *FROM MONTHLY_DEPRECIATIONCHARGE WHERE CHARGEYEAR = ? AND DEP_DATE = ?";
            }
            if(!branch_Id.equals("0")  && categoryId.equals("0")){
                ColQuery ="SELECT *FROM MONTHLY_DEPRECIATIONCHARGE WHERE CHARGEYEAR = ? AND DEP_DATE = ? AND BRANCH_CODE = ?";
            }
            if(branch_Id.equals("0")  && !categoryId.equals("0")){
                ColQuery ="SELECT *FROM MONTHLY_DEPRECIATIONCHARGE WHERE CHARGEYEAR = ? AND DEP_DATE = ? AND CATEGORY_CODE = ?";
            }
            if(!branch_Id.equals("0")  && !categoryId.equals("0")){
                ColQuery ="SELECT *FROM MONTHLY_DEPRECIATIONCHARGE WHERE CHARGEYEAR = ? AND DEP_DATE = ? AND BRANCH_CODE = ? AND CATEGORY_CODE = ?";
            }
            ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getDepreciationChargesExportRecords(ColQuery,chargeYear,reportDate,branchCode,categoryId);
            if(list.size()!=0){
            	SXSSFWorkbook workbook = new SXSSFWorkbook();
                Sheet sheet = workbook.createSheet("Depreciation Charges Export");
                Row rowhead = sheet.createRow((int) 0);
                
                rowhead.createCell((int) 0).setCellValue("ASSET_ID");
                rowhead.createCell((int) 1).setCellValue("BRANCH CODE");
                rowhead.createCell((int) 2).setCellValue("CATEGORY NAME");
                rowhead.createCell((int) 3).setCellValue("SBU CODE");
                rowhead.createCell((int) 4).setCellValue("DESCRIPTION");
                rowhead.createCell((int) 5).setCellValue("LIFE SPAN(MONTH)");
                rowhead.createCell((int) 6).setCellValue("CALC.SPAN(MONTH)");
                rowhead.createCell((int) 7).setCellValue("IMPROVE LIFE SPAN(MONTH)");
                rowhead.createCell((int) 8).setCellValue("DEPR. RATE");
                rowhead.createCell((int) 9).setCellValue("COST PRICE");
                rowhead.createCell((int) 10).setCellValue("IMPROVED COST");
                rowhead.createCell((int) 11).setCellValue("TOTAL COST PRICE");
                rowhead.createCell((int) 12).setCellValue("REMAINING USEFUL LIFE");
                rowhead.createCell((int) 13).setCellValue("DEPR. CHARGE MONTHLY");
                rowhead.createCell((int) 14).setCellValue("IMPROVE REMAINING USEFUL LIFE ");
                rowhead.createCell((int) 15).setCellValue("IMPROVE DEPR. CHARGE MONTHLY");
                rowhead.createCell((int) 16).setCellValue("TOTAL DEPR. CHARGE MONTHLY");
                rowhead.createCell((int) 17).setCellValue("DEPR. CHARGE YEAR TODATE");
                rowhead.createCell((int) 18).setCellValue("ACCUMULATED DEPR.");
                rowhead.createCell((int) 19).setCellValue("IMPROVE ACCUMULATED DEPR.");
                rowhead.createCell((int) 20).setCellValue("TOTAL ACCUMULATED DEPR.");
                rowhead.createCell((int) 21).setCellValue("NET BOOK VALUE");
                rowhead.createCell((int) 22).setCellValue("IMPROVE NET BOOK VALUE");
                rowhead.createCell((int) 23).setCellValue("TOTAL NET BOOK VALUE");
                rowhead.createCell((int) 24).setCellValue("PURCHASE DATE");
                rowhead.createCell((int) 25).setCellValue("DEPR. START DATE");
                rowhead.createCell((int) 26).setCellValue("DEPR. END DATE");
                rowhead.createCell((int) 27).setCellValue("DEPRECIATION RUN DATE ");
                rowhead.createCell((int) 28).setCellValue("STATUS ");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    String assetId =  newassettrans.getAssetId();
                    String oldassetId =  newassettrans.getOldassetId();
                    String barcode =  newassettrans.getBarCode();
                    String Description = newassettrans.getDescription();
                    String assetuser = newassettrans.getAssetUser();
                    String assetcode = newassettrans.getAssetCode();
                    branchCode = newassettrans.getBranchCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
                    double costPrice = newassettrans.getCostPrice();
                    double monthlyDepr = newassettrans.getMonthlyDep();
                    double accumDepr = newassettrans.getAccumDep();
                    double improvmonthldepr = newassettrans.getImprovmonthlyDep();
                    double totalDeprChargeMonthly = monthlyDepr+improvmonthldepr;
                    double improvcostPrice = newassettrans.getImprovcostPrice();
                    double totalCostPrice= costPrice+improvcostPrice;
                    double improvaccumDepr = newassettrans.getImprovaccumDep();
                    double totalAccumDepr = accumDepr+improvaccumDepr;
                    double totalnbv = newassettrans.getTotalnbv();
                    double nbv = newassettrans.getNbv();  
                    double improvnbv = newassettrans.getImprovnbv(); 
                    if(nbv<11){status = "INCLUDE IMPROVE COST";}
                    int remainLife = newassettrans.getRemainLife();
                    int improveremainLife = newassettrans.getImproveRemainLife();
                    int totalLife = newassettrans.getUsefullife();
                    int improvLife = newassettrans.getImprovtotallife();
                    int calcLifeSpan = newassettrans.getCalcLifeSpan();
                    double depRate = newassettrans.getDepRate();
                    String categoryCode = newassettrans.getCategoryCode();
                    String categoryName = records.getCodeName("select CATEGORY_NAME from am_ad_category where CATEGORY_CODE = '"+categoryCode+"'");
                    String batchId = newassettrans.getIntegrifyId();
                    String sighted = newassettrans.getAssetsighted();
                    String function = newassettrans.getAssetfunction();
                    String comments = newassettrans.getAssetMaintenance();
                    String make = newassettrans.getAssetMake();
                    String lpo = newassettrans.getLpo();
                    String vendoAcct = newassettrans.getVendorAC();
                    String vendorId = newassettrans.getSupplierName();
//			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
                    String sbucode = newassettrans.getSbuCode();
                    String purchaseDate = newassettrans.getDatepurchased();
                    String depr_startDate = newassettrans.getEffectiveDate();
                    String depr_endDate = newassettrans.getDependDate();
//                    if(!depr_endDate.equals("")){depr_endDate = depr_endDate.substring(0, 10);}
                    String depDate = newassettrans.getDepDate();
                    String deprciationDate = newassettrans.getDependDate();
 //                   System.out.println("========depr_endDate: "+depr_endDate);
                    Double TotalLife = Double.valueOf(totalLife);
                    Double ImprovLife = Double.valueOf(improvLife);
                    Double CostPrice = Double.valueOf(costPrice);
                    Double MonthlyDepr = Double.valueOf(monthlyDepr);
                    Double improvMonthldepr = Double.valueOf(improvmonthldepr);
                    double deprChargeToDate = newassettrans.getDeprChargeToDate();
                    Double AccumDepr = Double.valueOf(accumDepr);
                    Double improvCostPrice = Double.valueOf(improvcostPrice);
                    Double improvenbv = Double.valueOf(improvnbv);
                    Double TotalNbv = Double.valueOf(totalnbv);
                    


                    Row row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(assetId);
                    row.createCell((int) 1).setCellValue(branchCode);
                    row.createCell((int) 2).setCellValue(categoryName);
                    row.createCell((int) 3).setCellValue(sbucode);
                    row.createCell((int) 4).setCellValue(Description);
                    row.createCell((int) 5).setCellValue(TotalLife);
                    row.createCell((int) 6).setCellValue(calcLifeSpan);
                    row.createCell((int) 7).setCellValue(ImprovLife);
                    row.createCell((int) 8).setCellValue(depRate);
                    row.createCell((int) 9).setCellValue(CostPrice);
                    row.createCell((int) 10).setCellValue(improvCostPrice);
                    row.createCell((int) 11).setCellValue(totalCostPrice);
                    row.createCell((int) 12).setCellValue(remainLife);
                    row.createCell((int) 13).setCellValue(monthlyDepr);
                    row.createCell((int) 14).setCellValue(improveremainLife);
                    row.createCell((int) 15).setCellValue(improvMonthldepr);
                    row.createCell((int) 16).setCellValue(totalDeprChargeMonthly);
                    row.createCell((int) 17).setCellValue(deprChargeToDate);
                    row.createCell((int) 18).setCellValue(AccumDepr);
                    row.createCell((int) 19).setCellValue(improvaccumDepr);
                    row.createCell((int) 20).setCellValue(totalAccumDepr);
                    row.createCell((int) 21).setCellValue(nbv);
                    row.createCell((int) 22).setCellValue(improvenbv);
                    row.createCell((int) 23).setCellValue(TotalNbv);
                    row.createCell((int) 24).setCellValue(purchaseDate.substring(0, 10));
                    row.createCell((int) 25).setCellValue(depr_startDate.substring(0, 10));
                    row.createCell((int) 26).setCellValue(depr_endDate);
                    row.createCell((int) 27).setCellValue(depDate.substring(0, 10));
                    row.createCell((int) 28).setCellValue(status);
 
                    i++;
                    
                   // System.out.println("i: " + i);
                    
                }
              //  if (i % batchSize == 0) {
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
              workbook.write(stream);
              workbook.close();  
              //  }
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