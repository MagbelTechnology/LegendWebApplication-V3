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
   
public class DepreciationValidationReportExport extends HttpServlet
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
    String branch_Id = request.getParameter("branch") != null ? "0" : request.getParameter("branch");
    String category = request.getParameter("category");
    String disposalReason = request.getParameter("DisposalReason");
    String fromDate = request.getParameter("FromDate");
  //  String endDate = request.getParameter("ToDate");
    if(!fromDate.equals("")){
    	String fromDD = fromDate.substring(0, 2);
    	String fromMM = fromDate.substring(3, 5);
    	String fromYYYY = fromDate.substring(6, 10);
    	fromDate = fromYYYY+"-"+fromMM+"-"+fromDD;
    }
//    if(!endDate.equals("")){
//    	String toDD = endDate.substring(0, 2);
//    	String toMM = endDate.substring(3, 5);
//    	String toYYYY = endDate.substring(6, 10);
//    	endDate = toYYYY+"-"+toMM+"-"+toDD;
//    }
    System.out.println("<<<<<<fromDate: "+fromDate+"    <<<<<<endDate: ");
    //String branchCode = request.getParameter("BRANCH_CODE");
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
//    if(!branch_Id.equals("0")){
//    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
//    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = records.getCodeName("select User_Name from am_gb_User where User_Id = ? ",user_Id);
    String report = request.getParameter("report");
    String fileName = "";
    if(report.equalsIgnoreCase("rptMenuDepreciationValid")){fileName = "Depreciation-Validation-Report.xlsx";}
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
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  disposalReason: "+disposalReason+"  fromDate: "+fromDate);
     String ColQuery = "";
     if(!fromDate.equals("")){
    	 System.out.println("======>>>>>>>Date Selected: ");
	     ColQuery ="select ROUND(CAST(DATEDIFF(MONTH, effective_date, ?) AS float) -\n"
	     		+ "  (DATEPART(dd,effective_date)*1.0 - 1.0) / DAY(EOMONTH(effective_date)) +\n"
	     		+ "  ((DATEPART(dd,?)*1.0 ) / DAY(EOMONTH(?))),0) AS USED,Monthly_Dep, USEFUL_LIFE,effective_date,\n"
	     		+ "  (ROUND(CAST(DATEDIFF(MONTH, effective_date,?) AS float) -\n"
	     		+ "  (DATEPART(dd,effective_date)*1.0 - 1.0) / DAY(EOMONTH(effective_date)) +\n"
	     		+ "  ((DATEPART(dd,?)*1.0 ) / DAY(EOMONTH(?))),0)) - USEFUL_LIFE AS DIFF,ASSET_ID, DESCRIPTION, NBV,\n"
	     		+ "  a.BRANCH_CODE,a.CATEGORY_CODE,b.BRANCH_NAME,c.category_name from AM_ASSET a, am_ad_branch b, am_ad_category c\n"
	     		+ "  WHERE ASSET_STATUS = 'ACTIVE' AND NBV > 20\n"
	     		+ "  AND (ROUND(CAST(DATEDIFF(MONTH, effective_date, ?) AS float) -\n"
	     		+ "  (DATEPART(dd,effective_date)*1.0 - 1.0) / DAY(EOMONTH(effective_date)) +\n"
	     		+ "  ((DATEPART(dd,?)*1.0 ) / DAY(EOMONTH(?))),0)) - USEFUL_LIFE  <> 0\n"
	     		+ "  AND a.BRANCH_CODE = b.BRANCH_CODE\n"
	     		+ "  AND a.CATEGORY_CODE = c.CATEGORY_CODE";
	}      
	 
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getDepreciationValidationReportRecords(ColQuery,branch_Id,categoryCode,fromDate,"");
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("S/NO");
         rowhead.createCell((short) 1).setCellValue("ASSET ID");
         rowhead.createCell((short) 2).setCellValue("DESCRIPTION");
         rowhead.createCell((short) 3).setCellValue("MONTHLY_DEP");
         rowhead.createCell((short) 4).setCellValue("NBV");
         rowhead.createCell((short) 5).setCellValue("USED");
         rowhead.createCell((short) 6).setCellValue("USEFUL LIFE");
		 rowhead.createCell((short) 7).setCellValue("EFFECTIVE DATE");
		 rowhead.createCell((short) 8).setCellValue("DIFF");
		 rowhead.createCell((short) 9).setCellValue("BRANCH NAME");
		 rowhead.createCell((short) 10).setCellValue("CATEGORY NAME");

     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String asset_Id = newassettrans.getAssetId();
			String description = newassettrans.getDescription();
			double monthlyDep = newassettrans.getMonthlyDep();
			double nbv = newassettrans.getNbv();
			int used = newassettrans.getRemainLife();
			int useful_Life = newassettrans.getUsefullife();
			String effectiveDate = newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";
			int diff = newassettrans.getCalcLifeSpan();
			String branchName = newassettrans.getBranchName();
         String categoryName = newassettrans.getCategoryName();

			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(String.valueOf(i));
            row.createCell((short) 1).setCellValue(asset_Id);
            row.createCell((short) 2).setCellValue(description);
            row.createCell((short) 3).setCellValue(monthlyDep);
            row.createCell((short) 4).setCellValue(nbv);
            row.createCell((short) 5).setCellValue(used);
			row.createCell((short) 6).setCellValue(useful_Life);
			row.createCell((short) 7).setCellValue(effectiveDate);
			row.createCell((short) 8).setCellValue(diff);
			row.createCell((short) 9).setCellValue(branchName);
			row.createCell((short) 10).setCellValue(categoryName);
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
   
   public String getDate(String date) {
	   //System.out.println("<<<<<<<<<<<< Date: " + date);
	   String yyyy = date.substring(0, 4);
		String mm = date.substring(5, 7);
		String dd = date.substring(8, 10);
		date = dd+"/"+mm+"/"+yyyy;
	   
		
	   return date;
   }
}