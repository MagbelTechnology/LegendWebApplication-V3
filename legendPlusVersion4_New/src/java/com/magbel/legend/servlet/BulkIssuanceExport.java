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
   
public class BulkIssuanceExport extends HttpServlet
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
        String fileName = "Bulk Stock Issuance Details.xls";
        
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

//    if(exists==false){


            String chargeYear = request.getParameter("reportYear");
            String reportDate = request.getParameter("reportDate");
            String branch_Id = request.getParameter("branch");
            String branchCode = request.getParameter("BRANCH_CODE");
            String tranId = request.getParameter("tranId");
            String categoryId = request.getParameter("category");
            String mails = request.getParameter("mails1");
            String subject = request.getParameter("subject1");
            String msgText   = request.getParameter("msgText1");
            String otherparam  = request.getParameter("otherparam");
            Report rep = new Report();
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode);
            java.util.ArrayList list =rep.findStockIssuanceDisplayByBatchId(String.valueOf(tranId));
            if(list.size()!=0){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Bulk Stock Issuance Details Export");
                HSSFRow rowhead = sheet.createRow((int) 0);

                rowhead.createCell((int) 0).setCellValue("ASSET ID");
                rowhead.createCell((int) 1).setCellValue("DESCRIPTION");
                rowhead.createCell((int) 2).setCellValue("REQUEST DEPT NAME");
                rowhead.createCell((int) 3).setCellValue("REQUEST SBU CODE");
                rowhead.createCell((int) 4).setCellValue("REQUEST SECTION");
                rowhead.createCell((int) 5).setCellValue("REQUEST BRANCH NAME");
                rowhead.createCell((int) 6).setCellValue("QUANTITY REQUESTED");
                rowhead.createCell((int) 7).setCellValue("QUANTITY ISSUED");
                rowhead.createCell((int) 8).setCellValue("ISSUANCE STATUS");
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                	magma.StockRecordsBean  aset = (magma.StockRecordsBean)list.get(k);
                    String assetId =  aset.getAsset_id();
                    String oldassetId =  aset.getOldId();
                    String barcode =  aset.getBar_code();
                    String Description = aset.getDescription();
                    int assetcode = aset.getAssetCode();
                    branchCode = aset.getBranch_Code();
                    String sbucode = aset.getSbu_code();
                    String receiveBranch = aset.getNewbranch_id();
                    String receiveDept = aset.getReceiveDept();
                    String receiveSection = aset.getNewsection_id();
                    String quantityRequest = aset.getRequestedQuantity();
                    int quantityIssued = aset.getQtyIssued();
                    int issuanceStatus = aset.getIssuanceStatus();
			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+receiveBranch+"");
//			String deptName = records.getCodeName("select DEPT_NAME from am_ad_branch where BRANCH_ID = "+receiveDept+"");
			
			String requestSbuCode = records.getCodeName("select SBU_CODE  from am_ad_TransferRequisition WHERE Batch_id = '"+tranId+"' ");
			String requestDeptId = records.getCodeName("select DEPT_ID  from am_ad_TransferRequisition WHERE Batch_id = '"+tranId+"' ");
			String requestSectId = records.getCodeName("select REQUESTSECT_ID  from am_ad_TransferRequisition WHERE Batch_id = '"+tranId+"' ");
			
			String newsectionName = records.getCodeName("select Section_Name from am_ad_section where Section_ID = '"+requestSectId+"'");
			String newdeptName = records.getCodeName("select DEPT_NAME from am_ad_department where dept_ID = '"+requestDeptId+"'");
			String statusName = records.getCodeName("SELECT NARRATION FROM ISSUANCE_STATUS WHERE ID = '"+issuanceStatus+"'");
			String quantityRequested = records.getCodeName("select quantity  from am_ad_TransferRequisition WHERE Batch_id = '"+tranId+"' AND Description = '"+Description+"' ");
//			String sectionName = records.getCodeName("select SECTION_NAME from am_ad_branch where BRANCH_ID = "+receiveSection+"");
//			String status = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+issuanceStatus+"");
					System.out.println("<<<<<<branchName: "+branchName);
                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(assetId);
                    row.createCell((int) 1).setCellValue(Description);
                    row.createCell((int) 2).setCellValue(newdeptName);
                    row.createCell((int) 3).setCellValue(requestSbuCode);
                    row.createCell((int) 4).setCellValue(newsectionName);
                    row.createCell((int) 5).setCellValue(branchName);
                    row.createCell((int) 6).setCellValue(quantityRequested);
                    row.createCell((int) 7).setCellValue(quantityIssued);
                    row.createCell((int) 8).setCellValue(statusName);
                    i++;
                }
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
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