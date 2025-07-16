package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import legend.admin.handlers.CompanyHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class LegacyTransactionGeneration extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private  CompanyHandler comp; 
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 PrintWriter out = response.getWriter();
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String BankingApp = prop.getProperty("BankingApp");	 
		
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
	 comp = new CompanyHandler();
    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    String userName = request.getParameter("user");
    String start_Date = request.getParameter("FromDate");
    String end_Date = request.getParameter("ToDate");   
    end_Date = end_Date+" 23:59:59:000";
    String endDate = "";
    String startDate = "";
 //   System.out.println("<<<<<<startDate: "+start_Date+"   end_Date: "+end_Date);
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
    //String branchCode = request.getParameter("BRANCH_CODE");
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
   // System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String fileName = branch_Code+"By"+userName+"LegacyTransactionDownloadReport.xlsx";    	
//    String filePath = System.getProperty("user.home")+"\\Downloads";
//	File tmpDir = new File(filePath);
//	boolean exists = tmpDir.exists();	
	boolean delete = comp.deleteObject("DELETE FROM LEGACY_TRANSACTION WHERE POSTING_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'");
//    response.setContentType("application/vnd.ms-excel");
//    response.setHeader("Content-Disposition", 
//   "attachment; filename="+fileName+"");
//	System.out.println("<<<<<<We are here: "); 
    try 
    {
    	ad = new AssetRecordsBean();

    	 java.util.ArrayList list =comp.getLegacyTransactionRecords(startDate,endDate,BankingApp);   
    	 System.out.println("<<<<<<list.size(): "+ list.size());
     if(BankingApp.equalsIgnoreCase("FLEXICUBE")) {   
	     for(int k=0;	k<list.size();k++)
	     {
	    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
				String makerId = newTransaction.getAssetId();
				String serialNo = newTransaction.getBarCode();
				String checkerId = newTransaction.getSbuCode();
				String transDescription = newTransaction.getDescription();
				String batchNo = newTransaction.getAssetUser();
				String accountNo = newTransaction.getVendorAC();
				double amount = newTransaction.getCostPrice();
				String tranType = newTransaction.getAssetType();
				branchCode = newTransaction.getBranchCode();
				String postingDate = newTransaction.getPostingDate();
//		if(!branchCode.equals("")){
			boolean sent = comp.InsertLegacyTransactions(makerId,serialNo, checkerId, transDescription, batchNo,accountNo,amount,tranType,branchCode,postingDate,BankingApp);	
//	     }
			
	     } 
	     
    }
     if(BankingApp.equalsIgnoreCase("FINACLE")) {     	
	     for(int k=0;	k<list.size();k++)
	     {
	    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
				String makerId = newTransaction.getAssetId();
				String serialNo = newTransaction.getBarCode();
				String checkerId = newTransaction.getSbuCode();
				String transDescription = newTransaction.getDescription();
				String batchNo = newTransaction.getAssetUser();
				String accountNo = newTransaction.getVendorAC();
				double amount = newTransaction.getCostPrice();
				String tranType = newTransaction.getAssetType();
				branchCode = newTransaction.getBranchCode();
				String postingDate = newTransaction.getPostingDate();
//		if(!branchCode.equals("")){
			boolean sent = comp.InsertLegacyTransactions(makerId,serialNo, checkerId, transDescription, batchNo,accountNo,amount,tranType,branchCode,postingDate,BankingApp);	
//	     }
	     }  
	    
    }     
     out.println("<script type='text/javascript'>alert('Total No ("+list.size()+") Of Records From Legacy System.')</script>");
	  out.println((new StringBuilder("<script> window.location ='DocumentHelp.jsp?np=rptMenuLegacyTransactionCompare'</script>"))); 
    } catch (Exception e)
    {
     e.getMessage();
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