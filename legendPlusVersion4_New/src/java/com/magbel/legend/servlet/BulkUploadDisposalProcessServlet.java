package com.magbel.legend.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.ConnectionClass;
import legend.admin.handlers.CompanyHandler;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.DepreciationProcessingManager;
import magma.net.manager.RaiseEntryManager;
import magma.util.Codes;
import magma.asset.manager.AssetManager;
import magma.AssetRecordsBean;
import magma.ExcelAssetDisposalBean;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadDisposalProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AssetRecordsBean ad;
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
	SimpleDateFormat sdf;
	SimpleDateFormat sdfr;
	
    public BulkUploadDisposalProcessServlet()
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
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdfr = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
//		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
		String singleApproval = prop.getProperty("singleApproval");
//		System.out.println("singleApproval: " + singleApproval);
		String BatchApiUrl = prop.getProperty("BatchApiUrl");
//		System.out.println("BatchApiUrl: " + BatchApiUrl);
		String BatchChannel = prop.getProperty("BatchChannel");
//		System.out.println("BatchChannel: " + BatchChannel);
		String batchNo ="";		
		String postingDate = sdf.format(new java.util.Date());
		String postingDateTime = sdfr.format(new java.util.Date());
		
//		PrintWriter out = response.getWriter();
		 EmailSmsServiceBus mail = new EmailSmsServiceBus();
		String groupid = request.getParameter("groupid");
		String raiseBtn = request.getParameter("raiseBtn");
		String reject = request.getParameter("reject");
		
		response.setContentType("text/html");
//		System.out.println("<<<<<<<<raiseBtn: "+raiseBtn+"     reject: "+reject);
		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String userId =(String)session.getAttribute("CurrentUser");
		
        String appName = htmlUtil.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG");
        System.out.println("<<<<<<<<==appName===>>>>>>>>: "+appName);
        String currency = htmlUtil.getCodeName("SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'"); 
        String monthName = htmlUtil.getCodeName("select CONVERT(varchar(3), getdate(), 100)");
        String branchId = htmlUtil.getCodeName("select BRANCH from am_gb_User where USER_ID = "+userId+" ");
        String branchCode = htmlUtil.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
        String userName = htmlUtil.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = "+userId+"");
        String fileName = branchCode+"By"+userName+"FinacleDisposedAssetUploadExport.xls";
        String filePath = System.getProperty("user.home")+"\\Downloads";
        String accountNumber = htmlUtil.getCodeName("select distinct SUBSTRING(buyer_ac, 1,len(LTRIM(RTRIM(buyer_ac)))-1) from AM_GROUP_DISPOSAL where disposal_ID = '"+groupid+"'");
        String supervisorNameQry = htmlUtil.getCodeName("select TOP 1 Subject_TO_Vat+':'+substring(Wh_Tax,1,1)+':'+coalesce(TIN,'N')+':'+coalesce(RCNo,'N') from AM_GROUP_DISPOSAL a, am_ad_vendor v where a.buyer_ac = v.account_number and disposal_ID = '"+groupid+"' and Vendor_Name = 'DISPOSAL ACCOUNT' and v.Vendor_Status = 'ACTIVE'");
//        System.out.println("<<<<<<<<==supervisorNameQry===>>>>>>>>: "+supervisorNameQry);
        String[] sprvResult = supervisorNameQry.split(":");
//      String[] sprvResult = (records.retrieve4Array(supervisorNameQry)).split(":");
	      String subjectTovat = sprvResult[0];
	      String subjectTowhTax = sprvResult[1];
	      String TIN = sprvResult[2];
	      String RCNo = sprvResult[3];
	      String dateField = "";
	    
			boolean transfer = false;
		  try
		        {  
			  if (!userClass.equals("NULL") || userClass!=null){
//				  System.out.println("<<<==raiseBtn===>>>>: "+raiseBtn+"     reject: "+reject);
			  if(raiseBtn.equalsIgnoreCase("Posting") && !reject.equalsIgnoreCase("Reject")){
//				  System.out.println("<<<<<<<<groupid: "+groupid);
				  int j = insertBulkUploadPostingEntriesToLegacy(groupid,postingDate,postingDateTime,userId);
				  System.out.println("<<<<<<<<J: "+j);
				  if(j!=-1) {
				  java.util.ArrayList list =assetMan.getUploadAssetDisposalRecords(groupid);
//				  System.out.println("<<<<<<<<list.size(): "+list.size());
				  
			     for(int k=0;k<list.size();k++)
			     {  
			    	 ExcelAssetDisposalBean  disposaltrans = (magma.ExcelAssetDisposalBean)list.get(k);  
//			    	 System.out.println("<<<<<<<<disposaltrans.getAsset_id(): "+disposaltrans.getAsset_id());
					String assetid =  disposaltrans.getAsset_id();
					String groupId =  disposaltrans.getGid();
					String vendorId = disposaltrans.getVendor();
					String vendorAccount = disposaltrans.getVendor_account();
					String costprice = disposaltrans.getCost_price();
					String description = disposaltrans.getDescription();
					String location = disposaltrans.getLocation();
					String projectCode = disposaltrans.getProjectCode();
					String vatablecost = disposaltrans.getDisposalAmount();
					String newcostprice = disposaltrans.getNewcost_price();
					String newnbv = disposaltrans.getNewnbv();
					String newvatamount = disposaltrans.getNewvat_amount();
					String newvatablecost = disposaltrans.getNewvatable_cost();
					String usefullife = disposaltrans.getUsefullife();
					String newwhtamount = disposaltrans.getNewwht_amount();
					String oldnbv = disposaltrans.getOldnbv();
					
					String page1 = "ASSET DISPOSAL UPLOAD RAISE ENTRY";
			
					htmlUtil.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+groupid+"'"); 
					htmlUtil.updateAssetStatusChange("update am_asset_approval set Process_Status = 'A' where batch_id='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update AM_GROUP_DISPOSAL set approval_status = 'ACTIVE', RAISE_ENTRY = 'Y',DISPOSED = 'Y', STATUS = 'POSTED' where DISPOSAL_ID='"+groupid+"' ");

//    	        	System.out.println("MMMMMMMMM: ");
					htmlUtil.updateAssetStatusChange("update am_asset_approval set asset_status = 'ACTIVE', process_status = 'A' where batch_Id='"+groupid+"'");
//    	        	System.out.println("WWWWWWWWWWWWWW: ");
//					htmlUtil.updateAssetStatusChange("update AM_GROUP_Disposal set DISPOSED = 'Y', STATUS = 'POSTED' where DISPOSAL_ID='"+groupid+"'");
					htmlUtil.updateAssetStatusChange("update a SET a.Asset_Status = 'Disposed' from AM_ASSET a, AM_GROUP_DISPOSAL b where a.Asset_Id = b.Asset_Id and b.disposal_id = '"+groupid+"'");
//    	        	System.out.println("QQQQQQQQQQ: ");
					}
//				     System.out.println("<<<<<<subjectTovat 1====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            String ColQuery = "";
			            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("S")){
			            	System.out.println("<<<<<<subjectTovat 1====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            	ColQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = 'VATYWHTS'");
			            }
			            if(!subjectTovat.equals("Y")  && !subjectTowhTax.equals("S")){
			            	System.out.println("<<<<<<subjectTovat 2====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            	ColQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = ''");
			            }
			            if(subjectTovat.equals("N")  && subjectTowhTax.equals("F")){   
			    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
			            	System.out.println("<<<<<<subjectTovat 3====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            	ColQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = 'VATNWHTF'");
			            }		       
			            if(subjectTovat.equals("N")  && subjectTowhTax.equals("S")){   
			    //        	 System.out.println("======>>>>>>>subjectTovat: "+subjectTovat+"   subjectTowhTax: "+subjectTowhTax);
			            	System.out.println("<<<<<<subjectTovat 4====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            	ColQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = 'VATNWHTS'");
			            }	
			            if(subjectTovat.equals("Y")  && subjectTowhTax.equals("F")){
			            	System.out.println("<<<<<<subjectTovat 5====>: "+subjectTovat+"    subjectTowhTax: "+subjectTowhTax);
			            	ColQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = 'VATYWHTF'");
			            }
			            Report rep = new Report();
			            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
			            java.util.ArrayList listrep =rep.getAssetDisposedUploadExportRecords(ColQuery,branchCode,groupid);
			            System.out.println("<<<<<<List Rep.size()>>>>>>=: "+listrep.size());
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
			                
					     System.out.println("<<<<<<listrep.size(): "+listrep.size());
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
//			              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
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

//			         System.out.println("<<<<<<listrep.size(): "+listrep.size());
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
//			                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
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

//			         System.out.println("<<<<<<listrep.size(): "+listrep.size());
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
//			                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
			                  workbook.write(stream);
			                  stream.close();
			                  System.out.println("Data is saved in excel file.");
			                }    
			            	
			            	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (!BatchApiUrl.equals(""))){
//			                	System.out.println("======1>>>>>>>appName: "+appName);
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

//			                    System.out.println("<<<<<<listrep.size(): "+listrep.size());
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
//			                  new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
			                  workbook.write(stream);
			                  stream.close();
			                  System.out.println("Data is saved in excel file.");
			                	}            	
			            }
			            response.sendRedirect("DocumentHelp.jsp?np=raiseEntrySummary");  
				  				  
			  }
			  }
			  if(reject.equalsIgnoreCase("Reject")){
				  DeletebulkDisposalUploadRejections(groupid);
	                System.out.println(">>>>>>>>>>> About to send Mail on the Batch "+groupid);
	                String msgText1 = "Your transaction for Bulk asset Disposal Upload with Batch ID " + groupid + " has been Rejected. The new Batch ID is " + groupid;
	                mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Disposal Upload", msgText1);
			  }
//	                out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntrySummary'</script>");
			  response.sendRedirect("DocumentHelp.jsp?np=raiseEntrySummary");
		        } 
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}
	
	public void DeletebulkDisposalUploadRejections(String batchId){
		String query0 ="update a SET a.Asset_Status = 'ACTIVE' from AM_ASSET a, AM_GROUP_DISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_ID = '"+batchId+"'";
		String query1 ="update b SET b.Approval_Status = 'REJECTED',STATUS = 'REJECTED' from AM_ASSET a, AM_GROUP_DISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_ID = '"+batchId+"'";
//		String query2 ="update b SET b.Approval_Status = 'REJECTED' from AM_ASSET a, AM_ASSETDISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_ID = '"+batchId+"'";
//		String query1 ="delete from AM_GROUP_DISPOSAL where DISPOSAL_ID = '"+batchId+"'";
//		String query2 ="delete from AM_ASSETDISPOSAL where DISPOSAL_ID = '"+batchId+"'";
		String query3 ="update am_asset_approval set process_status = 'R',Asset_Status = 'Rejected' where batch_id = '"+batchId+"'";
		String query4 ="update b SET b.Approval_Status = 'REJECTED',STATUS = 'REJECTED' from AM_ASSET a, am_asset_disposal_Upload b where a.Asset_id = b.Asset_id and b.disposal_ID = '"+batchId+"'";
//		String query4 ="delete from am_asset_disposal_Upload where DISPOSAL_ID = '"+batchId+"'";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query0);
				int i =ps.executeUpdate();
		        } catch (Exception ex) { 
		            System.out.println("DeletebulkDisposalUploadRejections: for AM_ASSET Update>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps1 = con.prepareStatement(query1);
				int i =ps1.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for AM_GROUP_DISPOSAL>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps1);
		        }		
//		try {
//			con = mgDbCon.getConnection("legendPlus");
//				ps2 = con.prepareStatement(query2);
//				int i =ps2.executeUpdate();
//		        } catch (Exception ex) {
//		            System.out.println("DeletebulkDisposalUploadRejections: for AM_ASSETDISPOSAL>>>>>" + ex);
//		        } finally {
//		            closeConnection(con, ps2);
//		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps3 = con.prepareStatement(query3);
				int i =ps3.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for am_asset_approval>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps3);
		        }	
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps4 = con.prepareStatement(query4);
				int i =ps4.executeUpdate();  
		        } catch (Exception ex) {
		            System.out.println("DeletebulkDisposalUploadRejections: for am_asset_disposal_Upload>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps4);
		        }
		}	

public java.util.ArrayList bulkDisposalUploadRecordListsReportToLegacy(String batchId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGDISPOSALUPLOAD' AND TYPE = 'UPLOADPOSTING'");
//		System.out.println("====bulkDisposalUploadRecordListsReportToLegacy query-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement s = null;
//	Statement s = null;  
String result = "";
	try {
		   // c = getConnection();
		    c = mgDbCon.getConnection("legendPlus");
//			s = c.createStatement();
			s = c.prepareStatement(query);
			s.setString(1, batchId);
			s.setString(2, batchId);
			s.setString(3, batchId);
			s.setString(4, batchId);
			rs = s.executeQuery();
			while (rs.next())
			   {
				String asset_id = rs.getString("ASSET_ID");
				String description = rs.getString("DESCRIPTION");
				batchId = rs.getString("GROUP_ID");
				String amount = rs.getString("COSTPRICE");
				String crAccounNo = rs.getString("CR_ACCOUNTNO");
				String drAccounNo = rs.getString("DR_ACCOUNTNO");
				String sbuCode = rs.getString("SBU_CODE");
//				String status = rs.getString("STATUS");
//				String account = rs.getString("ACCOUNT");
				AssetRecordsBean trans = new AssetRecordsBean();
				trans.setAsset_id(asset_id);
				trans.setDescription(description);
				trans.setCost_price(amount);
				trans.setSbu_code(sbuCode);
//				trans.setStatus(result);
				trans.setBatchId(batchId);
				trans.setVendor_account(crAccounNo);
				trans.setSpare_1(drAccounNo);
//				System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id);
				_list.add(trans);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}



//Transfered from jsp
//public int insertBulkUploadPostingEntriesToLegacy(String batchId,String postingDate,String postingDateTime,String userId) {
//  	int x = -1;		
//
//    java.util.ArrayList list =bulkDisposalUploadRecordListsReportToLegacy(batchId);
//    //================================
////      String query3 = "insert into custom.BULK_LGD(SBU_CODE,ID,DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, "
////      		+ "    RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) values(?,?,?,?,?,?,?,?,?,?,?)";
//
//
//      try{
//      	Connection conn = null;
//      ConnectionClass connection = new ConnectionClass();
//      	conn = connection.getOracleConnection();
//        AssetRecordsBean arb = new AssetRecordsBean();
//        System.out.print("========list.size() in insertBulkUploadPostingEntriesToLegacy---======: "+list.size());
//	     for(int i=0;i<list.size();i++)
//	     {   AssetRecordsBean trans = new AssetRecordsBean();
//	    	 magma.AssetRecordsBean  asset = (magma.AssetRecordsBean)list.get(i);   
//	    	 String draccountNo = asset.getVendor_account();
//	    	 String craccountNo = asset.getSpare_1();
////	    	 String debit_credit = asset.getStatus();
//	    	 double amount = Double.parseDouble(asset.getCost_price());
//	    	 String description = asset.getDescription();
//	    	 String asset_Id = asset.getAsset_id();
//	    	 String sbuCode = asset.getSbu_code();
//
//	         String query3 = " insert into custom.BULK_LGD(SBU_CODE,ID,DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, "
//		         		+ " RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) values('"+sbuCode+"','"+asset_Id+"','"+draccountNo+"','"+craccountNo+"',"+amount+",'"+description+"',TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS'),TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS'),'FINAPP','"+description+"','N') ";
//
//	               
//      //PreparedStatement ps = conn.getPreparedStatementOracle(query3);
//      	 PreparedStatement ps = conn.prepareStatement(query3);
//////	        System.out.print("The value of sbuCode is ----======....." +sbuCode);
////	        ps.setString(1,sbuCode);
////	        ps.setString(2,asset_Id);
//////	        System.out.print("The value of dr_acct is ----======....." +draccountNo);
////	        ps.setString(3,draccountNo);
//////	        System.out.print("The value of cr_acct is ----======....." +craccountNo);
////	        ps.setString(4,craccountNo);
//////	        System.out.print("The value of amount is ----======....." +amount);
////	        ps.setDouble(5,amount);
//////	        System.out.print("The value of narration is ----======....." +description);
////	        ps.setString(6,description);
////	        ps.setString(7, postingDateTime);
////	        ps.setString(8, postingDateTime);
////	        ps.setString(9, "FINAPP");	       
//////	        System.out.print("The value of narration2 is ----======....." +description);
////	        ps.setString(10,description);
//////	        System.out.print("The value of value_date is ----======....." +postingDateTime);
////	        ps.setString(11, "N");
//       x = ps.executeUpdate();
//       System.out.print("The value of x is ----======....." +x);
//  	  if(x==1) {
//  		int k = insertBulkUploadPostingEntriesToBatchTable(batchId,postingDate,postingDateTime,userId);
//       String insertDRrecord = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"
//          		+ "values('"+asset_Id+"','"+draccountNo+"','D','"+batchId+"','"+sbuCode+"',"+amount+",'"+description+"','"+postingDate+"')";
//          arb.updateAssetStatusChange(insertDRrecord);
//          System.out.print("insertDRrecord ----======: " +insertDRrecord);
//          String insertCRrecord = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"
//             		+ "values('"+asset_Id+"','"+craccountNo+"','C','"+batchId+"','"+sbuCode+"',"+amount+",'"+description+"','"+postingDate+"')";
//             arb.updateAssetStatusChange(insertCRrecord);
//             System.out.print("insertCRrecord ----======: " +insertCRrecord);       
//  	  	}
//	     }
//       conn.close();
// // connection.freeResource();
//  }//try
//  catch(Exception e){ 
//  e.getMessage();
//  }
//    
//      //catch
//  System.out.print("The value of x is ------------==================............." + x);
//  return x;
//  }	//		getIndividualTransaction()	

public int insertBulkUploadPostingEntriesToLegacy(String batchId, String postingDate,
        String postingDateTime, String userId) throws Exception {
int totalInserted = 0;
Connection conn = null;
PreparedStatement ps = null;
AssetRecordsBean arb = new AssetRecordsBean();

try {
ArrayList<AssetRecordsBean> list = bulkDisposalUploadRecordListsReportToLegacy(batchId);
System.out.println("List size: " + list.size());

ConnectionClass connection = new ConnectionClass();
conn = connection.getOracleConnection();
conn.setAutoCommit(false);



String sql = "INSERT INTO custom.BULK_LGD " +
"(SBU_CODE, ID, DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, " +
"RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) " +
"VALUES (?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS'), " +
"TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS'), ?, ?, ?)";

ps = conn.prepareStatement(sql);

for (AssetRecordsBean asset : list) {
ps.setString(1, asset.getSbu_code());
ps.setString(2, asset.getAsset_id());
ps.setString(3, asset.getVendor_account()); // DR
ps.setString(4, asset.getSpare_1());        // CR
ps.setDouble(5, Double.parseDouble(asset.getCost_price()));
ps.setString(6, asset.getDescription());
ps.setString(7, postingDateTime);
ps.setString(8, postingDateTime);
ps.setString(9, "FINAPP");
ps.setString(10, asset.getDescription());
ps.setString(11, "N");

ps.addBatch();
}

int[] results = ps.executeBatch();
conn.commit();
totalInserted = results.length;

// DR/CR entries insertion
for (AssetRecordsBean asset : list) {

String insertDRrecord = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"
+ "values('"+asset.getAsset_id()+"','"+asset.getVendor_account()+"','D','"+batchId+"','"+asset.getSbu_code()+"',"+Double.parseDouble(asset.getCost_price())+",'"+asset.getDescription()+"','"+postingDate+"')";
arb.updateAssetStatusChange(insertDRrecord);


String insertCRrecord = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"
+ "values('"+asset.getAsset_id()+"','"+asset.getVendor_account()+"','C','"+batchId+"','"+asset.getSbu_code()+"',"+Double.parseDouble(asset.getCost_price())+",'"+asset.getDescription()+"','"+postingDate+"')";
arb.updateAssetStatusChange(insertCRrecord);

}

} catch (Exception e) {
e.printStackTrace(); 
try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
} finally {
try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
}

return totalInserted;
}


public int insertBulkUploadPostingEntriesToBatchTable(String batchId,String postingDate, String postingDateTime,String userId) {
	int x = -1;		
	Connection con = null;
	PreparedStatement ps = null;	

  java.util.ArrayList list =bulkDisposalUploadRecordListsReportToLegacy(batchId);
  //================================
//    String query3 = "insert into custom.fxd_asset(sbu_code,dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) values(?,?,?,?,?,?,?,?)";
    String query3 = "insert into AM_GB_BATCH_POSTING(BATCH_ID,SBU_CODE,ASSET_ID,DRACCOUNT_NO,CRACCOUNT_NO,DR_DESCRIPTION,CR_DESCRIPTION,AMOUNT,"
    		+ "USER_ID,POSTING_DATE,TRANSACTION_DATE,TRANSTYPE) values(?,?,?,?,?,?,?,?,?,?,?,?)";

    try{
    	con = mgDbCon.getConnection("legendPlus");
    	
    	
 //   	System.out.print("The value of list.size() is ----======....." +list.size());
    	
	     for(int i=0;i<list.size();i++)
	     {   //AssetRecordsBean trans = new AssetRecordsBean();
	    	 magma.AssetRecordsBean  asset = (magma.AssetRecordsBean)list.get(i);   
	    	 String draccountNo = asset.getVendor_account();
	    	 String craccountNo = asset.getSpare_1();
//	    	 String debit_credit = asset.getStatus();
	    	 double amount = Double.parseDouble(asset.getCost_price());
	    	 String drdescription = asset.getDescription();
	    	 String crdescription = asset.getDescription();
	    	 String asset_Id = asset.getAsset_id();
	    	 String sbuCode = asset.getSbu_code();

	         String transType = "Asset Disposal Upload";
	         ps = con.prepareStatement(query3);
	        ps.setString(1,batchId);
	        ps.setString(2,sbuCode);
	        ps.setString(3,asset_Id);
	        ps.setString(4,draccountNo);
	        ps.setString(5,craccountNo);
	        ps.setString(6,drdescription);
	        ps.setString(7,crdescription);
	        ps.setDouble(8,amount);
	        ps.setString(7,userId);
	        ps.setString(8, postingDate);
	        ps.setString(9, postingDateTime);
	        ps.setString(11,transType);

    	 	x = ps.executeUpdate();
	     }  
    }
		 catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				closeConnection(con, ps, rs);
			}

    //catch
//System.out.print("The value of x is ------------==================............." + x);
return x;
}	//		getIndividualTransaction()	

	     	
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
