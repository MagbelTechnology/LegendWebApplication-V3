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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class PostedTransactionExport extends HttpServlet
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
        String userName = request.getParameter("user");
        String start_Date = request.getParameter("FromDate");
        String end_Date = request.getParameter("ToDate");
        end_Date = end_Date+" 23:59:59:000";
        String endDate = "";
        String startDate = "";
        System.out.println("<<<<<<startDate: "+start_Date+"   end_Date: "+end_Date);
        if(!start_Date.equals("")){
        String dd = start_Date.substring(0,2);
        String mm = start_Date.substring(3,5);
        String yyyy = start_Date.substring(6,10);
        startDate = yyyy+"-"+mm+"-"+dd;
        }
        if(!end_Date.equals("")){
        String enddd = end_Date.substring(0,2);
        String endmm = end_Date.substring(3,5);
        String endyyyy = end_Date.substring(6,10);
        endDate = endyyyy+"-"+endmm+"-"+enddd;
        endDate = endDate+" 23:59:59:000";
        }
        System.out.println("<<<<<<startDate: "+startDate+"    endDate: "+endDate);
        System.out.println("<<<<<<userName: "+userName);
        String status = "COST PRICE";
//        String userName = request.getParameter("userName");
        String fileName = "PostedTransactionBy"+userName+".xlsx";
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

//
//            String chargeYear = request.getParameter("reportYear");
//            String reportDate = request.getParameter("reportDate");
//            String branch_Id = request.getParameter("branch");
//            String tranId = request.getParameter("tranId");
//            String categoryId = request.getParameter("category");
//            String mails = request.getParameter("mails1");
//            String subject = request.getParameter("subject1");
//            String msgText   = request.getParameter("msgText1");
//            String otherparam  = request.getParameter("otherparam");
            Report rep = new Report();
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode);
            String ColQuery = "";
/*            
            if(branch_Id.equals("***")  && categoryId.equals("***")){
                ColQuery ="SELECT *FROM MonthlyDeprCharcges_View WHERE CHARGEYEAR = '"+chargeYear+"' AND DEP_DATE = '"+reportDate+"'";
            }
            if(!branch_Id.equals("***")  && categoryId.equals("***")){
                ColQuery ="SELECT *FROM MonthlyDeprCharcges_View WHERE CHARGEYEAR = '"+chargeYear+"' AND DEP_DATE = '"+reportDate+"' AND BRANCH_CODE = '"+branch_Id+"'";
            }
            if(branch_Id.equals("***")  && !categoryId.equals("***")){
                ColQuery ="SELECT *FROM MonthlyDeprCharcges_View WHERE CHARGEYEAR = '"+chargeYear+"' AND DEP_DATE = '"+reportDate+"' AND CATEGORY_CODE = '"+categoryId+"'";
            }
            if(!branch_Id.equals("***")  && !categoryId.equals("***")){
                ColQuery ="SELECT *FROM MonthlyDeprCharcges_View WHERE CHARGEYEAR = '"+chargeYear+"' AND DEP_DATE = '"+reportDate+"' AND BRANCH_CODE = '"+branch_Id+"' AND CATEGORY_CODE = '"+categoryId+"'";
            } 
            ColQuery = ColQuery+" AND ASSET_STATUS = 'ACTIVE' ";
 */
            if(!userName.equals("***")){
            ColQuery ="SELECT c.full_name AS initiatorName, d.full_name AS supervisorName, *FROM posted_transaction_view a, am_gb_User c, am_gb_User d " +
            		 "WHERE a.initiatorId = c.User_id AND a.supervisorId = d.User_id " +
            		 "AND a.User_Name = ? AND a.transaction_date BETWEEN ? AND ? order by INTEGRIFY_ID,transaction_date ";
            }
            if(userName.equals("***")){
                ColQuery ="SELECT c.full_name AS initiatorName, d.full_name AS supervisorName, *FROM posted_transaction_view a, am_gb_User c, am_gb_User d " +
               		 "WHERE a.initiatorId = c.User_id AND a.supervisorId = d.User_id " +
               		 "AND a.transaction_date BETWEEN ? AND ? order by INTEGRIFY_ID,transaction_date ";	
            }
            
            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getPostedTransactionExportRecords(ColQuery,userName,startDate,endDate);
            if(list.size()!=0){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Posted Transaction Export");
                HSSFRow rowhead = sheet.createRow((int) 0);

                rowhead.createCell((int) 0).setCellValue("Asset Id");
                rowhead.createCell((int) 1).setCellValue("Transaction Description");
                rowhead.createCell((int) 2).setCellValue("Debit Account");
                rowhead.createCell((int) 3).setCellValue("Debit Account Name");
                rowhead.createCell((int) 4).setCellValue("Credit Account");
                rowhead.createCell((int) 5).setCellValue("Credit Account Name");
                rowhead.createCell((int) 6).setCellValue("Amount)");
                rowhead.createCell((int) 7).setCellValue("Posted By");
                rowhead.createCell((int) 8).setCellValue("Response");
                rowhead.createCell((int) 9).setCellValue("Action");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
                    String assetId =  newassettrans.getAssetId();
                    String branchCode =  newassettrans.getBranchCode();
                    String branchName =  newassettrans.getBranchName();
                    String Description = newassettrans.getDescription();
                    String debitAccount = newassettrans.getDebitAccount();
                    String creditAccount = newassettrans.getCreditAccount();
                    String responseMessage = newassettrans.getResponse();
                    String action = newassettrans.getAction();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
                    double amount = newassettrans.getAmount();
                    String assetacqAct = records.getCodeName("select asset_acq_ac from am_gb_company");
                    String fedWhtAct = records.getCodeName("select Fed_Wht_Account from am_gb_company");
                    String whtAct = records.getCodeName("select wht_account from am_gb_company");
                    String vatAct = records.getCodeName("select Vat_Account from am_gb_company");
                    String debitAccountName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = ? ",debitAccount);
                    String creditAccountName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where ACCOUNT_NUMBER = ? ",creditAccount);
                    String drAcct = debitAccount.substring(8);
//                    System.out.println("<<<<<<drAcct: "+drAcct);
                    if(debitAccountName==""){debitAccountName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = ? ",drAcct);}
                    if(debitAccountName.equals("") && drAcct.equals(assetacqAct)){debitAccountName = "Asset Acquisition Account";}
                    if(debitAccountName.equals("") && drAcct.equals(fedWhtAct)){debitAccountName = "Federal Withholding Tax Account";}
                    if(debitAccountName.equals("") && drAcct.equals(whtAct)){debitAccountName = "Withholding Tax Account";}
                    if(debitAccountName.equals("") && drAcct.equals(assetacqAct)){debitAccountName = "Asset Acquisition Account";}
                    if(debitAccountName.equals("") && drAcct.equals(vatAct)){debitAccountName = "Vat Account";}
                    String crAcct = creditAccount.substring(8);
//                    System.out.println("<<<<<<crAcct: "+crAcct);
                    if(creditAccountName==""){creditAccountName = records.getCodeName("select category_name from am_ad_category where Asset_Ledger = ? ",crAcct);}
                    if(creditAccountName.equals("") && crAcct.equals(assetacqAct)){creditAccountName = "Asset Acquisition Account";}
                    if(creditAccountName.equals("") && crAcct.equals(fedWhtAct)){creditAccountName = "Federal Withholding Tax Account";}
                    if(creditAccountName.equals("") && crAcct.equals(whtAct)){creditAccountName = "Withholding Tax Account";}
                    if(creditAccountName.equals("") && crAcct.equals(assetacqAct)){creditAccountName = "Asset Acquisition Account";}
                    if(creditAccountName.equals("") && crAcct.equals(vatAct)){creditAccountName = "Vat Account";}
                    String postedBy = newassettrans.getPostedBy();
                    String transactionDate = newassettrans.getTransDate();
                    String initiatorId = newassettrans.getInitiatorId();
                    String supervisorId = newassettrans.getSupervisorId();


                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(assetId);
                    row.createCell((int) 1).setCellValue(Description);
                    row.createCell((int) 2).setCellValue(debitAccount);
                    row.createCell((int) 3).setCellValue(debitAccountName);
                    row.createCell((int) 4).setCellValue(creditAccount);
                    row.createCell((int) 5).setCellValue(creditAccountName);
                    row.createCell((int) 6).setCellValue(amount);
                    row.createCell((int) 7).setCellValue(postedBy);
                    row.createCell((int) 8).setCellValue(responseMessage);
                    row.createCell((int) 9).setCellValue(action);
                    
                    i++;
                }
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
              workbook.write(stream);
              stream.close();
              System.out.println("Data is saved in excel file.");

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