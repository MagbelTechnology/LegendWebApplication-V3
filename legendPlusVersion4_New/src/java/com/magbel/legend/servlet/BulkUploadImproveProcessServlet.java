package com.magbel.legend.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import legend.admin.handlers.CompanyHandler;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.DepreciationProcessingManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;
import magma.asset.manager.AssetManager;
import magma.AssetRecordsBean;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadImproveProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AssetRecordsBean ad;
	java.text.SimpleDateFormat sdf;
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	AssetManager assetMan = null;
	HtmlUtility htmlUtil =null;
	ApprovalRecords records;
	
    public BulkUploadImproveProcessServlet()
    {
        super();
        try
        {
        ad = new AssetRecordsBean();
        }
        
        catch(Exception et)
        {et.printStackTrace();}
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	private void processCategoryItemType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		htmlUtil = new HtmlUtility();
		assetMan = new AssetManager();
		 records = new ApprovalRecords();
		
			Properties prop = new Properties();
			File file = new File("C:\\Property\\LegendPlus.properties");
			FileInputStream input = new FileInputStream(file);
			prop.load(input);

			String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
			System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
			String singleApproval = prop.getProperty("singleApproval");
			System.out.println("singleApproval: " + singleApproval);
			String BatchApiUrl = prop.getProperty("BatchApiUrl");
			System.out.println("BatchApiUrl: " + BatchApiUrl);
			String BatchChannel = prop.getProperty("BatchChannel");
			System.out.println("BatchChannel: " + BatchChannel);
			String batchNo ="";
			
//		PrintWriter out = response.getWriter();
		 EmailSmsServiceBus mail = new EmailSmsServiceBus();
		String groupid = request.getParameter("groupid");
		String raiseBtn = request.getParameter("raiseBtn");
		String reject = request.getParameter("reject");
		
		response.setContentType("text/html");
//		System.out.println("<<<<<<<<raiseBtn: "+raiseBtn+"     reject: "+reject+"     groupid: "+groupid);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
		
        String deptCode = records.getCodeName("select dept_code from am_gb_User where USER_ID = "+userId+" ");
        String appName = records.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG"); 
        String currency = records.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        String monthName = records.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
        String branchId = records.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");
        String branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
//		System.out.println("<<<<<<<<userId: "+userId+"      userClass: "+userClass);
        String accountNumber = records.getCodeName("select distinct SUBSTRING(vendor_ac, 1,len(LTRIM(RTRIM(vendor_ac)))-1) from AM_ASSET where GROUP_ID = '"+groupid+"'");
        String supervisorNameQry = records.getCodeName("select TOP 1 Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_GROUP_IMPROVEMENT a, am_ad_vendor v where v.account_number like '%"+accountNumber+"%' and a.Revalue_ID = '"+groupid+"' and v.Vendor_Status = 'ACTIVE'");
//        System.out.println("<<<<<<supervisorNameQry ====>: "+supervisorNameQry+"    accountNumber: "+accountNumber);
        String[] sprvResult = supervisorNameQry.split(":");
//      String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
      String subjectTovat = sprvResult[0];
      String subjectTowhTax = sprvResult[1];
      String TIN = sprvResult[2];
      String RCNo = sprvResult[3];
      Date date = new Date();  
//      System.out.println(sdf.format(date)); 
//      String currentDate = sdf.format(date);
//      String DD = currentDate.substring(0,2);
//      String MM = currentDate.substring(3,5);
//      String YYYY = currentDate.substring(6,10);
//      String HH = currentDate.substring(11,13);
//      String M = currentDate.substring(14,16);
//      String SS = currentDate.substring(17,19);
//      currentDate = DD+MM+YYYY+HH+M+SS;
//      String dateField = DD+"-"+monthName+"-"+YYYY;
      String dateField = "";
//      System.out.println("<<<<<<branch Id: "+branchId+"   branch_Code: "+branchCode+"  subjectTovat: "+subjectTovat+"  subjectTowhTax: "+subjectTowhTax+"  TIN: "+TIN+"    RCNo: "+RCNo);
      String userName = records.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
      if(TIN.equalsIgnoreCase("N")){TIN = RCNo; subjectTovat = "N";}
      String fileName = branchCode+"By"+userName+"FinacleAssetImprovementUploadExport.xls";
      String filePath = System.getProperty("user.home")+"\\Downloads";
      
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
				  
				  
				  
			  if(raiseBtn.equalsIgnoreCase("Posting") && !reject.equalsIgnoreCase("Reject")){
			  java.util.ArrayList list =assetMan.getImprovedAssetRecords(groupid);
			     for(int k=0;k<list.size();k++)
			     {
			    	 magma.ExcelAssetImproveBean  improvetrans = (magma.ExcelAssetImproveBean)list.get(k);    	 
					String assetid =  improvetrans.getAsset_id();
					String groupId =  improvetrans.getGid();
					String assetCode = improvetrans.getAsset_code();
//					System.out.println("<<<<<<<<assetid: "+assetid+"      groupId: "+groupId);
					branchId = improvetrans.getBranch_id();
					String vendorId = improvetrans.getVendor();
					String vendorAccount = improvetrans.getVendor_account();
					String costprice = improvetrans.getCost_price();
					String description = improvetrans.getDescription();
					String location = improvetrans.getLocation();
					String projectCode = improvetrans.getProjectCode();
					String noofMonths = improvetrans.getNoOfMonths();
					String vatablecost = improvetrans.getVatable_cost();
					String vatamount = improvetrans.getVat_amount();
					String vatrate = improvetrans.getVatRate();
					String whtAmount = improvetrans.getWh_tax_amount();
					String newcostprice = improvetrans.getNewcost_price();
					String newnbv = improvetrans.getNewnbv();
					String newvatamount = improvetrans.getNewvat_amount();
					String newvatablecost = improvetrans.getNewvatable_cost();
					String usefullife = improvetrans.getUsefullife();
					String newwhtamount = improvetrans.getNewwht_amount();
					String oldAccumDep = improvetrans.getOldaccum_dep();
					String oldCostPrice = improvetrans.getOldcost_price();
					String oldimprovAccum = improvetrans.getOldIMPROV_ACCUMDEP();
					String oldimprovCost = improvetrans.getOldIMPROV_COST();
					String oldimprovNBV = improvetrans.getOldIMPROV_NBV();
					String oldimprovvatablecost = improvetrans.getOldIMPROV_VATABLECOST();
					String oldnbv = improvetrans.getOldnbv();
					String oldvatamount = improvetrans.getOldvat_amount();
					String oldvatablecost = improvetrans.getOldvatable_cost();
					String oldwhtAmount = improvetrans.getOldwht_amount();
					String page1 = "ASSET IMPROVEMENT UPLOAD RAISE ENTRY";
//					System.out.println("<<<<<<<<newcostprice>>>>>>  "+newcostprice+"  newnbv>> "+newnbv+" newvatablecost: "+newvatablecost+"  newvatamount: "+newvatamount+"  newwhtamount: "+newwhtamount);
//					System.out.println("<<<<<<<<usefullife>>>>>>  "+usefullife+"  costprice>> "+costprice+" vatablecost: "+vatablecost+"  oldnbv: "+oldnbv);
					assetMan.processGroupImprovement(assetid,Double.parseDouble(newcostprice),Double.parseDouble(newnbv),
					Double.parseDouble(newvatablecost),Double.parseDouble(newvatamount),Double.parseDouble(newwhtamount),
					Integer.parseInt(usefullife),Double.parseDouble(costprice),Double.parseDouble(vatablecost),oldnbv,
					Integer.parseInt(groupid));
					ad.insertVendorTransaction(userId,description,vendorAccount,vendorAccount,Double.parseDouble(costprice),location,groupId,page1,projectCode,vendorId);
//					System.out.println("<<<<<<<<groupid>>>>>>>> "+groupid);
					htmlUtil.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update am_asset_improvement_Upload set approval_status = 'ACTIVE', RAISE_ENTRY = 'Y' where Revalue_ID='"+groupid+"' ");
			     }
//			     System.out.println("<<<<<<subjectTovat 1====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            String ColQuery = "";
		            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
//		            	System.out.println("<<<<<<subjectTovat 1====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'VATYWHTS'");
		            }
		            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
//		            	System.out.println("<<<<<<subjectTovat 2====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'WHTS'");
		            }
		            if(subjectTovat.equals("Y")  && !subjectTowhTax.equals("S")){
		    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//		            	System.out.println("<<<<<<subjectTovat 3====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'VATY'");
		            }
		            if(!subjectTovat.equals("Y")  && !subjectTowhTax.equals("S")){
//		            	System.out.println("<<<<<<subjectTovat 4====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = ''");
		            }
		            if(!subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){   
		    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//		            	System.out.println("<<<<<<subjectTovat 5====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'WHTF'");
		            }
		            if(subjectTovat.equals("N")  && subjectTowhTax.equals("F")){   
		    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//		            	System.out.println("<<<<<<subjectTovat 5====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'VATNWHTF'");
		            }		       
		            if(subjectTovat.equals("N")  && subjectTowhTax.equals("S")){   
		    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
//		            	System.out.println("<<<<<<subjectTovat 5====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
		            	ColQuery = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGIMPROVEUPLOAD' AND TYPE = 'VATNWHTS'");
		            }	
		            Report rep = new Report();
//		            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
		            java.util.ArrayList listrep =rep.getFinacleAssetImprovedUploadExportRecords(ColQuery,branchCode,groupid);
		            if(listrep.size()!=0){
		            	if(appName.equalsIgnoreCase("FINACLE-7.0.9")){
		            		 response.setContentType("application/vnd.ms-excel");
		            		 response.setHeader("Content-Disposition", 
		            		   "attachment; filename="+fileName+"");
		                HSSFWorkbook workbook = new HSSFWorkbook();
		                HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
		                HSSFRow rowhead = sheet.createRow((int) 0);
		                
		                rowhead.createCell((int) 0).setCellValue("Account No.");
		                rowhead.createCell((int) 1).setCellValue("DR CR");
		                rowhead.createCell((int) 2).setCellValue("Amount ");
		                rowhead.createCell((int) 3).setCellValue("Description");
		                rowhead.createCell((int) 4).setCellValue("E");
		                rowhead.createCell((int) 5).setCellValue("F");
		                rowhead.createCell((int) 6).setCellValue("Asset Id");
		                rowhead.createCell((int) 7).setCellValue("H");
		                rowhead.createCell((int) 8).setCellValue("I");
		                rowhead.createCell((int) 9).setCellValue("SBU CODE");
		                int i = 1;
		                
//				     System.out.println("<<<<<<listrep.size(): "+listrep.size());
		                for(int k=0;k<listrep.size();k++)
		                {
		                    com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)listrep.get(k);
		                    String assetId =  newassettrans.getAssetId();
		                    String drCr =  newassettrans.getBarCode();
		                    String sbuCode =  newassettrans.getSbuCode();
		                    double costPrice = newassettrans.getCostPrice();
		                    String Description = newassettrans.getDescription();
		                    String E = newassettrans.getAssetUser();
		                    String F = newassettrans.getAssetCode();
		                    String accountNo = newassettrans.getVendorAC();
		                    String H = newassettrans.getCategoryCode();
		                    String I = newassettrans.getIntegrifyId();

		                    HSSFRow row = sheet.createRow((int) i);

		                    row.createCell((int) 0).setCellValue(accountNo);
		                    row.createCell((int) 1).setCellValue(drCr);
		                    row.createCell((int) 2).setCellValue(costPrice);
		                    row.createCell((int) 3).setCellValue(Description);
		                    row.createCell((int) 4).setCellValue(E);
		                    row.createCell((int) 5).setCellValue(F);
		                    row.createCell((int) 6).setCellValue(assetId);
		                    row.createCell((int) 7).setCellValue(H);
		                    row.createCell((int) 8).setCellValue(I);
		                    row.createCell((int) 9).setCellValue(sbuCode);
		                    i++; 
		                }
		                OutputStream stream = response.getOutputStream();
//		              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
		              workbook.write(stream);
		              stream.close();
		              System.out.println("Data is saved in excel file.");
		            }
		            	if(appName.equalsIgnoreCase("FINACLE-10.2.18")){
		            		 response.setContentType("application/vnd.ms-excel");
		            		    response.setHeader("Content-Disposition", 
		            		   "attachment; filename="+fileName+"");
		                    HSSFWorkbook workbook = new HSSFWorkbook();
		                    HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
		                    HSSFRow rowhead = sheet.createRow((int) 0);
		                    
		                    rowhead.createCell((int) 0).setCellValue("Account No.");
		                    rowhead.createCell((int) 1).setCellValue("Curency");
		                    rowhead.createCell((int) 2).setCellValue("Narration (30 xters max)");
		                    rowhead.createCell((int) 3).setCellValue("Remarks1 (30 xters max)");
		                    rowhead.createCell((int) 4).setCellValue("Remarks2 (50 xters max)");
		                    rowhead.createCell((int) 5).setCellValue("Amount");
		                    rowhead.createCell((int) 6).setCellValue("ValueDate(DD/MM/YYYY)");
		                    rowhead.createCell((int) 7).setCellValue("ReportCode");
		                    rowhead.createCell((int) 8).setCellValue("Narration Checker");
		                    rowhead.createCell((int) 9).setCellValue("Remarks1 Checker");
		                    int i = 1;

//		         System.out.println("<<<<<<listrep.size(): "+listrep.size());
		                    for(int k=0;k<listrep.size();k++)
		                    {
		                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)listrep.get(k);
		                        String assetId =  newassettrans.getAssetId();
		                        String drCr =  newassettrans.getBarCode();
		                        String sbuCode =  newassettrans.getSbuCode();
		                        double costPrice = newassettrans.getCostPrice();
		                        String Description = newassettrans.getDescription();
		                        String E = newassettrans.getAssetUser();
		                        String F = newassettrans.getAssetCode();
		                        String accountNo = newassettrans.getVendorAC();
		                        String H = newassettrans.getCategoryCode();
		                        String I = newassettrans.getIntegrifyId();
		                        String J = newassettrans.getCategoryCode();

		                        HSSFRow row = sheet.createRow((int) i);

		                        row.createCell((int) 0).setCellValue(accountNo);
		                        row.createCell((int) 1).setCellValue(currency);
		                        row.createCell((int) 2).setCellValue(Description);
		                        row.createCell((int) 3).setCellValue(assetId);
		                        row.createCell((int) 4).setCellValue(E);
		                        row.createCell((int) 5).setCellValue(costPrice);
		                        row.createCell((int) 6).setCellValue(new java.util.Date());
		                        row.createCell((int) 7).setCellValue(sbuCode);
		                        row.createCell((int) 8).setCellValue(I);
		                        row.createCell((int) 9).setCellValue(J);
		                        i++;
		                    }
		                    OutputStream stream = response.getOutputStream();
//		                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
		                  workbook.write(stream);
		                  stream.close();
		                  System.out.println("Data is saved in excel file.");
		                }
		            	if(appName.equalsIgnoreCase("FLEXCUBE-7.0.9")){
		                    HSSFWorkbook workbook = new HSSFWorkbook();
		                    HSSFSheet sheet = workbook.createSheet("Depreciation Charges Export");
		                    HSSFRow rowhead = sheet.createRow((int) 0);
		                    
		                    rowhead.createCell((int) 0).setCellValue("Account No.");
		                    rowhead.createCell((int) 1).setCellValue("DR CR");
		                    rowhead.createCell((int) 2).setCellValue("Amount ");
		                    rowhead.createCell((int) 3).setCellValue("Description");
		                    rowhead.createCell((int) 4).setCellValue("E");
		                    rowhead.createCell((int) 5).setCellValue("F");
		                    rowhead.createCell((int) 6).setCellValue("Asset Id");
		                    rowhead.createCell((int) 7).setCellValue("H");
		                    rowhead.createCell((int) 8).setCellValue("I");
		                    rowhead.createCell((int) 9).setCellValue("SBU CODE");
		                    int i = 1;

//		         System.out.println("<<<<<<listrep.size(): "+listrep.size());
		                    for(int k=0;k<listrep.size();k++)
		                    {
		                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)listrep.get(k);
		                        String assetId =  newassettrans.getAssetId();
		                        String drCr =  newassettrans.getBarCode();
		                        String sbuCode =  newassettrans.getSbuCode();
		                        double costPrice = newassettrans.getCostPrice();
		                        String Description = newassettrans.getDescription();
		                        String E = newassettrans.getAssetUser();
		                        String F = newassettrans.getAssetCode();
		                        String accountNo = newassettrans.getVendorAC();
		                        String H = newassettrans.getCategoryCode();
		                        String I = newassettrans.getIntegrifyId();

		                        HSSFRow row = sheet.createRow((int) i);

		                        row.createCell((int) 0).setCellValue(accountNo);
		                        row.createCell((int) 1).setCellValue(drCr);
		                        row.createCell((int) 2).setCellValue(costPrice);
		                        row.createCell((int) 3).setCellValue(Description);
		                        row.createCell((int) 4).setCellValue(E);
		                        row.createCell((int) 5).setCellValue(F);
		                        row.createCell((int) 6).setCellValue(assetId);
		                        row.createCell((int) 7).setCellValue(H);
		                        row.createCell((int) 8).setCellValue(I);
		                        row.createCell((int) 9).setCellValue(sbuCode);
		                        i++;
		                    }
		                    OutputStream stream = response.getOutputStream();
//		                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
		                  workbook.write(stream);
		                  stream.close();
		                  System.out.println("Data is saved in excel file.");
		                }    
		            	
		            	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (!BatchApiUrl.equals(""))){
//		                	System.out.println("======1>>>>>>>appName: "+appName);
		                	 response.setContentType("application/vnd.ms-excel");
		                	    response.setHeader("Content-Disposition", 
		                	   "attachment; filename="+fileName+"");
		                    HSSFWorkbook workbook = new HSSFWorkbook();
		                    HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
		                    HSSFRow rowhead = sheet.createRow((int) 0);
		                    
		                    rowhead.createCell((int) 0).setCellValue("Date");
		                    rowhead.createCell((int) 1).setCellValue("Acc number");
		                    rowhead.createCell((int) 2).setCellValue("Amt");
		                    rowhead.createCell((int) 3).setCellValue("Transaction type");
		                    rowhead.createCell((int) 4).setCellValue("Desc ");
		                    rowhead.createCell((int) 5).setCellValue("Transaction Code ");
		                    rowhead.createCell((int) 6).setCellValue("Currency");
		                    rowhead.createCell((int) 7).setCellValue("Branch code ");
		                    rowhead.createCell((int) 8).setCellValue("Purpose Code ");
		                    rowhead.createCell((int) 9).setCellValue("Batch number ");
		                    rowhead.createCell((int) 10).setCellValue("Maker");
		                    rowhead.createCell((int) 11).setCellValue("Checker");
		                    int i = 1;

//		                    System.out.println("<<<<<<listrep.size(): "+listrep.size());
		                    for(int k=0;k<listrep.size();k++)
		                    {
		                        com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)listrep.get(k);
		                        String assetId =  newassettrans.getAssetId();
		                        String drCr =  newassettrans.getBarCode();
		                        String sbuCode =  newassettrans.getSbuCode();
		                        double costPrice = newassettrans.getCostPrice();
		                        String Description = newassettrans.getDescription();
		                        String transType = "";
		                        if(costPrice > 0){transType = "C";}
		                        if(costPrice < 0){transType = "D";}
		                        String transCode = "";
		                        String accountNo = newassettrans.getVendorAC();
		                        String maker = "";
		                        String checker = "";
		                        String purposeCode = "";
		                        
		                        HSSFRow row = sheet.createRow((int) i);

		                        row.createCell((int) 0).setCellValue(dateField);
		                        row.createCell((int) 1).setCellValue(accountNo);
		                        row.createCell((int) 2).setCellValue(costPrice);
		                        row.createCell((int) 3).setCellValue(transType);
		                        row.createCell((int) 4).setCellValue(Description);
		                        row.createCell((int) 5).setCellValue(transCode);
		                        row.createCell((int) 6).setCellValue(currency);
		                        row.createCell((int) 7).setCellValue(branchCode);
		                        row.createCell((int) 8).setCellValue(purposeCode);
		                        row.createCell((int) 9).setCellValue(maker);
		                        row.createCell((int) 10).setCellValue(batchNo);
		                        row.createCell((int) 11).setCellValue(checker);
		                        i++;
		                    }
		                    OutputStream stream = response.getOutputStream();
//		                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
		                  workbook.write(stream);
		                  stream.close();
		                  System.out.println("Data is saved in excel file.");
		                	}            	
		            }
		            response.sendRedirect("DocumentHelp.jsp?np=raiseEntrySummary");
			     
			  }
			  
			  if(reject.equalsIgnoreCase("Reject")){
				  System.out.println(">>>>>>>>>>> groupid: "+groupid);
				  String tranId = records.getCodeName("select transaction_id from am_asset_approval where asset_id = "+groupid+" ");
				  DeletebulkImprovementUploadRejections(groupid);
//	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
				 
	                String msgText1 = "Your transaction for Bulk asset Improvement Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(tranId), "Bulk Asset Upload", msgText1);
			  }
			  response.sendRedirect("DocumentHelp.jsp?np=raiseEntrySummary");
//	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&batchId="+groupid+"'</script>");
		        } 
		        }
		        catch(Exception e)
		        {  
		            e.printStackTrace();
		        }
	//	    }
		
	}  
	
	public void DeletebulkImprovementUploadRejections(String batchId){
//		String query1 ="delete from AM_GROUP_IMPROVEMENT where REVALUE_ID = '"+batchId+"'";
        String qa1 = "update AM_GROUP_IMPROVEMENT SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa1);	 
		
//		String query2 ="delete from am_asset_improvement where REVALUE_ID = '"+batchId+"'";
        String qa2 = "update am_asset_improvement SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa2);	 		
//		String query3 ="delete from am_asset_approval where batch_id = '"+batchId+"'";
        String qa3 = "update am_asset_approval SET ASSET_STATUS = 'REJECTED',PROCESS_STATUS = 'R' where asset_id = '"+batchId+"'";
        ad.updateAssetStatusChange(qa3);	 
//		String query4 ="delete from am_asset_improvement_Upload where REVALUE_ID = '"+batchId+"'";
        String qa4 = "update am_asset_improvement_Upload SET STATUS = 'REJECTED' where REVALUE_ID = '"+batchId+"'";
        ad.updateAssetStatusChange(qa4);	 
//		Connection con = null;
//		        PreparedStatement ps = null;
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query1);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_GROUP_IMPROVEMENT>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query2);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_improvement>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }		
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query3);
//				int i =ps.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }	
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps = con.prepareStatement(query4);
//				int i =ps.executeUpdate();  
//		        } catch (Exception ex) {
//		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps);
//		        }
		}	
    private void closeConnection(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}

	}	
	
	private void closeConnection(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}

}
