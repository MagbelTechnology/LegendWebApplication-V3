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
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkAssetTransferPostingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ApprovalRecords records;
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	
    public BulkAssetTransferPostingsServlet()
    {
        super();
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
		PrintWriter out = response.getWriter();
		
		String batch_Id = request.getParameter("batchId");
		String startdd = request.getParameter("start_dd");
		String startmm = request.getParameter("start_mm");
		String startyy = request.getParameter("start_yy");
		String enddd = request.getParameter("end_dd");
		String endmm = request.getParameter("end_mm");
		String endyy = request.getParameter("end_yy");
		String fromDate = request.getParameter("FromDate");
		String toDate = request.getParameter("ToDate");
		String StartDate = startyy+"-"+startmm+"-"+startdd;
		String EndDate = endyy+"-"+endmm+"-"+enddd;

		HttpSession session = request.getSession();
		String userClass = (String) session.getAttribute("UserClass");
		String []result = null ;
			String batchNo = "";
			String assetId = "";
		  try
		        {  
				if (!userClass.equals("NULL") || userClass!=null){
			  	DeletebulktransferPostings();
			  	if((batch_Id!=null) || (batch_Id!="")){
			     java.util.ArrayList list =bulkTransferRecordforsReport(batch_Id);
			     
			     System.out.println("-->size>--> "+list.size()+"  Batch Id:  "+batch_Id);
			     for(int i=0;i<list.size();i++)
			     {
			    	 String assetId_batchNo=(String) list.get(i);
			    	 result = assetId_batchNo.split("#");
			    	 batchNo = result[0];
			    	 assetId = result[1];
			    	 if(!assetId.equalsIgnoreCase(""))
			    		 insertBulkTransferPostingEntries(assetId, batchNo);
			     }
		        }
				  	if((batch_Id==null) || (batch_Id=="")){
					     java.util.ArrayList list1 =bulkTransferRecordforsReport(StartDate,EndDate);
					     for(int i=0;i<list1.size();i++)
					     {
					    	 String assetId_batchNo=(String) list1.get(i);
					    	 result = assetId_batchNo.split("#");
					    	 batchNo = result[0];
					    	 assetId = result[1];
					    	 if(!assetId.equalsIgnoreCase(""))
					    		 insertBulkTransferPostingEntries(assetId, batchNo);
					     }		
				  	}			     
			     out.print("<script>window.location='DocumentHelp.jsp?np=rptMenuBulkTransfer&batchId="+batch_Id+"&FromDate="+fromDate+"&ToDate="+toDate+"'</script>");
		        }else {
		    		out.print("<script>alert('You have No Right')</script>");
		    	}
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}

	public java.util.ArrayList bulkTransferRecordforsReport(String batchId)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from am_gb_bulkTransfer where STATUS = 'POSTED' and batch_id = ?  ";
			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
//				s = c.createStatement();
//				rs = s.executeQuery(query);
	            s = c.prepareStatement(query.toString());
	            s.setString(1, batchId);
	            rs = s.executeQuery();
				while (rs.next())
				   {
					String batch_Id = rs.getString("Batch_id");
					String asset_id = rs.getString("Asset_id");
					result = batch_Id + "#"+asset_id;
//					System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id);
					_list.add(result);
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

	public java.util.ArrayList bulkTransferRecordforsReport(String StartDate, String EndDate)
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from am_gb_bulkTransfer where TRANSFER_DATE BETWEEN ? AND ? ";
//			System.out.println("====bulkTransferRecordforsReport query-----> "+query);
		Connection c = null;
		ResultSet rs = null;
//		Statement s = null;
		PreparedStatement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("legendPlus");
//				s = c.createStatement();
//				rs = s.executeQuery(query);
	            s = c.prepareStatement(query.toString());
	            s.setString(1, StartDate);
	            s.setString(2, EndDate);
	            rs = s.executeQuery();
				while (rs.next())
				   {
					String batch_Id = rs.getString("Batch_id");
					String asset_id = rs.getString("Asset_id");
					result = batch_Id + "#"+asset_id;
//					System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id);
					_list.add(result);
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
		
	public boolean insertBulkTransferPostingEntries(String assetId, String batchId) {
		records = new ApprovalRecords();
//		String query = "INSERT INTO am_gb_bulktransferPostings (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration)"+
//	    		"(select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
//	    		"d.branch_code +	a.Asset_Ledger ACCOUNT,'C' AS CR_DR,e.Batch_id,e.newSBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"+
//	    		"e.Description+'   '+e.NEW_ASSET_ID AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
//	    		"am_gb_company b, am_gb_bulkTransfer e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//	    		"and d.branch_id = e.oldbranch_id and e.Batch_id = ? and e.Asset_id = ? "+
//	    		"UNION "+
//	    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
//	    		"d.branch_code +	a.Asset_Ledger ACCOUNT,'D' AS CR_DR,e.Batch_id,e.newSBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT, "+
//	    		"e.Description+'   '+e.NEW_ASSET_ID AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,  "+
//	    		"am_gb_company b, am_gb_bulkTransfer e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//	    		"and d.branch_id = e.newbranch_id and e.Batch_id = ? and e.Asset_id = ? "+
//	    		"UNION "+
//	    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+ "+
//	    		"d.branch_code +	a.Accum_Dep_ledger ACCOUNT,'C' AS CR_DR,e.Batch_id,e.newSBU_CODE AS SBU_CODE,e.ACCUM_DEP AS AMOUNT, "+
//	    		"e.Description+'   '+e.NEW_ASSET_ID AS NARRATION "+
//	    		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, am_gb_bulkTransfer e "+
//	    		"where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//	    		"and d.branch_id = e.newbranch_id and e.Batch_id = ? and e.Asset_id = ? "+
//	    		"UNION "+
//	    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+ "+
//	    		"d.branch_code +	a.Accum_Dep_ledger ACCOUNT,'D' AS CR_DR,e.Batch_id,e.newSBU_CODE AS SBU_CODE,e.ACCUM_DEP  AS AMOUNT, "+
//	    		"e.Description+'   '+e.NEW_ASSET_ID AS NARRATION "+
//	    		"from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, am_gb_bulkTransfer e "+
//	    		"where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//	    		"and d.branch_id = e.oldbranch_id and e.Batch_id = ? and e.Asset_id = ?)";
	String query = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGBULKTRANSFER' AND TYPE = 'GENERATION'");		
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	 System.out.println("insertBulkTransferPostingEntries query: "+query);
	try {
	con = mgDbCon.getConnection("legendPlus");
//	ps = con.prepareStatement(query);
    ps = con.prepareStatement(query.toString());
    ps.setString(1, batchId);
    ps.setString(2, assetId);
    ps.setString(3, batchId);
    ps.setString(4, assetId);
    ps.setString(5, batchId);
    ps.setString(6, assetId);
    ps.setString(7, batchId);
    ps.setString(8, assetId);
	done = (ps.executeUpdate() != -1);
	
	} catch (Exception ex) {
	done = false;
	System.out.println("Error insertBulkTransferPostingEntries() of BulkAssetTransferPostingsServlet -> " + ex.getMessage());
	ex.printStackTrace();
	} finally {
	closeConnection(con, ps);
	}
	return done;
	}
//	
//	public void BulkTransferPostingReport(String assetId, String batchId) {
//		
//		Properties prop = new Properties();
//		File file = new File("C:\\Property\\LegendPlus.properties");
//		FileInputStream input = new FileInputStream(file);
//		prop.load(input);
//
//		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
//		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
//		String singleApproval = prop.getProperty("singleApproval");
//		System.out.println("singleApproval: " + singleApproval);
//		String BatchApiUrl = prop.getProperty("BatchApiUrl");
//		System.out.println("BatchApiUrl: " + BatchApiUrl);
//		String BatchChannel = prop.getProperty("BatchChannel");
//		System.out.println("BatchChannel: " + BatchChannel);
//		Report rep = new Report();
//        String appName = records.getCodeName("SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG"); 
//		String query = records.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'LEGBULKTRANSFER' AND TYPE = 'GENERATION'");		
//        java.util.ArrayList list =rep.getLegacyAssetGrpPostRecords(query,batchId);
//        System.out.println("<<<<<<list.size()====: "+list.size());
//        if(list.size()!=0){
//    	if((appName.equalsIgnoreCase("FINACLE-7.0.9")) && (BatchApiUrl.equals(""))){
////    	System.out.println("======1>>>>>>>appName: "+appName);
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
//        HSSFRow rowhead = sheet.createRow((int) 0);
//        
//        rowhead.createCell((int) 0).setCellValue("Account No.");
//        rowhead.createCell((int) 1).setCellValue("DR CR");
//        rowhead.createCell((int) 2).setCellValue("Amount ");
//        rowhead.createCell((int) 3).setCellValue("Description");
//        rowhead.createCell((int) 4).setCellValue("E");
//        rowhead.createCell((int) 5).setCellValue("F");
//        rowhead.createCell((int) 6).setCellValue("Asset Id");
//        rowhead.createCell((int) 7).setCellValue("H");
//        rowhead.createCell((int) 8).setCellValue("I");
//        rowhead.createCell((int) 9).setCellValue("SBU CODE");
//        int i = 1;
//
////System.out.println("<<<<<<list.size(): "+list.size());
//        for(int k=0;k<list.size();k++)
//        {
//            com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
//            String assetId =  newassettrans.getAssetId();
//            String drCr =  newassettrans.getBarCode();
//            String sbuCode =  newassettrans.getSbuCode();
//            double costPrice = newassettrans.getCostPrice();
//            String Description = newassettrans.getDescription();
//            String E = newassettrans.getAssetUser();
//            String F = newassettrans.getAssetCode();
//            String accountNo = newassettrans.getVendorAC();
//            String H = newassettrans.getCategoryCode();
//            String I = newassettrans.getIntegrifyId();
//
//            HSSFRow row = sheet.createRow((int) i);
//
//            row.createCell((int) 0).setCellValue(accountNo);
//            row.createCell((int) 1).setCellValue(drCr);
//            row.createCell((int) 2).setCellValue(costPrice);
//            row.createCell((int) 3).setCellValue(Description);
//            row.createCell((int) 4).setCellValue(E);
//            row.createCell((int) 5).setCellValue(F);
//            row.createCell((int) 6).setCellValue(assetId);
//            row.createCell((int) 7).setCellValue(H);
//            row.createCell((int) 8).setCellValue(I);
//            row.createCell((int) 9).setCellValue(sbuCode);
//            i++;
//        }
//        OutputStream stream = response.getOutputStream();
////      new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//      workbook.write(stream);
//      stream.close();
//      System.out.println("Data is saved in excel file.");
//    	}
//    	if((appName.equalsIgnoreCase("FINACLE-10.2.18")) && (BatchApiUrl.equals(""))){
////      System.out.println("======2>>>>>>>appName: "+appName);
//      HSSFWorkbook workbook = new HSSFWorkbook();
//      HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
//      HSSFRow rowhead = sheet.createRow((int) 0);
//      
//      rowhead.createCell((int) 0).setCellValue("Account No.");
//      rowhead.createCell((int) 1).setCellValue("Curency");
//      rowhead.createCell((int) 2).setCellValue("Narration (30 xters max)");
//      rowhead.createCell((int) 3).setCellValue("Remarks1 (30 xters max)");
//      rowhead.createCell((int) 4).setCellValue("Remarks2 (50 xters max)");
//      rowhead.createCell((int) 5).setCellValue("Amount");
//      rowhead.createCell((int) 6).setCellValue("ValueDate(DD/MM/YYYY)");
//      rowhead.createCell((int) 7).setCellValue("ReportCode");
//      rowhead.createCell((int) 8).setCellValue("Narration Checker");
//      rowhead.createCell((int) 9).setCellValue("Remarks1 Checker");
//      int i = 1;
//      
////System.out.println("<<<<<<list.size(): "+list.size());
//      for(int k=0;k<list.size();k++)
//      {
//          com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
//          String assetId =  newassettrans.getAssetId();
//          String drCr =  newassettrans.getBarCode();
//          String sbuCode =  newassettrans.getSbuCode();
//          double costPrice = newassettrans.getCostPrice();
//          String Description = newassettrans.getDescription();
//          String E = newassettrans.getAssetUser();
//          String F = newassettrans.getAssetCode();
//          String accountNo = newassettrans.getVendorAC();
//          String H = newassettrans.getCategoryCode();
//          String I = newassettrans.getIntegrifyId();
//          String J = newassettrans.getCategoryCode();
//          HSSFRow row = sheet.createRow((int) i);
//
//          row.createCell((int) 0).setCellValue(accountNo);
//          row.createCell((int) 1).setCellValue(currency);
//          row.createCell((int) 2).setCellValue(Description);
//          row.createCell((int) 3).setCellValue(assetId);
//          row.createCell((int) 4).setCellValue(E);
//          row.createCell((int) 5).setCellValue(costPrice);
//          row.createCell((int) 6).setCellValue(new java.util.Date());
//          row.createCell((int) 7).setCellValue(sbuCode);
//          row.createCell((int) 8).setCellValue(I);
//          row.createCell((int) 9).setCellValue(J);
//          i++;
//      }
//      OutputStream stream = response.getOutputStream();
////    new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//    workbook.write(stream);
//    stream.close();
//    System.out.println("Data is saved in excel file.");
//    }
//    	if((appName.equalsIgnoreCase("FLEXCUBE-7.0.9")) && (BatchApiUrl.equals(""))){
////    		 System.out.println("======3>>>>>>>appName: "+appName);
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet = workbook.createSheet("Finacle Asset Export Record");
//            HSSFRow rowhead = sheet.createRow((int) 0);
//            
//            rowhead.createCell((int) 0).setCellValue("Account No.");
//            rowhead.createCell((int) 1).setCellValue("DR CR");
//            rowhead.createCell((int) 2).setCellValue("Amount ");
//            rowhead.createCell((int) 3).setCellValue("Description");
//            rowhead.createCell((int) 4).setCellValue("E");
//            rowhead.createCell((int) 5).setCellValue("F");
//            rowhead.createCell((int) 6).setCellValue("Asset Id");
//            rowhead.createCell((int) 7).setCellValue("H");
//            rowhead.createCell((int) 8).setCellValue("I");
//            rowhead.createCell((int) 9).setCellValue("SBU CODE");
//            int i = 1;
//
////   System.out.println("<<<<<<list.size(): "+list.size());
//            for(int k=0;k<list.size();k++)
//            {
//                com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
//                String assetId =  newassettrans.getAssetId();
//                String drCr =  newassettrans.getBarCode();
//                String sbuCode =  newassettrans.getSbuCode();
//                double costPrice = newassettrans.getCostPrice();
//                String Description = newassettrans.getDescription();
//                String E = newassettrans.getAssetUser();
//                String F = newassettrans.getAssetCode();
//                String accountNo = newassettrans.getVendorAC();
//                String H = newassettrans.getCategoryCode();
//                String I = newassettrans.getIntegrifyId();
//
//                HSSFRow row = sheet.createRow((int) i);
//
//                row.createCell((int) 0).setCellValue(accountNo);
//                row.createCell((int) 1).setCellValue(drCr);
//                row.createCell((int) 2).setCellValue(costPrice);
//                row.createCell((int) 3).setCellValue(Description);
//                row.createCell((int) 4).setCellValue(E);
//                row.createCell((int) 5).setCellValue(F);
//                row.createCell((int) 6).setCellValue(assetId);
//                row.createCell((int) 7).setCellValue(H);
//                row.createCell((int) 8).setCellValue(I);
//                row.createCell((int) 9).setCellValue(sbuCode);
//                row.createCell((int) 10).setCellValue(sbuCode);
//                i++;
//            }
//            OutputStream stream = response.getOutputStream();
////          new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//          workbook.write(stream);
//          stream.close();
//          System.out.println("Data is saved in excel file.");
//          }                  
//    	
//    	if((appName.equalsIgnoreCase("FLEXICUBE-10.2.18")) && (!BatchApiUrl.equals(""))){
//        	System.out.println("======1>>>>>>>appName: "+appName);
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
//            HSSFRow rowhead = sheet.createRow((int) 0);
//            
//            rowhead.createCell((int) 0).setCellValue("Date");
//            rowhead.createCell((int) 1).setCellValue("Acc number");
//            rowhead.createCell((int) 2).setCellValue("Amt");
//            rowhead.createCell((int) 3).setCellValue("Transaction type");
//            rowhead.createCell((int) 4).setCellValue("Desc ");
//            rowhead.createCell((int) 5).setCellValue("Transaction Code ");
//            rowhead.createCell((int) 6).setCellValue("Currency");
//            rowhead.createCell((int) 7).setCellValue("Branch code ");
//            rowhead.createCell((int) 8).setCellValue("Purpose Code ");
//            rowhead.createCell((int) 9).setCellValue("Batch number ");
//            rowhead.createCell((int) 10).setCellValue("Maker");
//            rowhead.createCell((int) 11).setCellValue("Checker");
//            int i = 1;
//			
//            System.out.println("<<<<<<list.size(): "+list.size());
//            for(int k=0;k<list.size();k++)
//            {
//                com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);
//                String assetId =  newassettrans.getAssetId();
//                String recType =  newassettrans.getBarCode();
//                String sbuCode =  newassettrans.getSbuCode();
//                double costPrice = newassettrans.getCostPrice();
//                String Description = newassettrans.getDescription();
//                String transType = newassettrans.getAssetType();
//                String transCode = newassettrans.getAssetUser();
//                String accountNo = newassettrans.getVendorAC();
//                String maker = "";
//                String checker = "";
//                String purposeCode = "";
//                Description = assetId+"**"+Description+"**"+transCode;
//                HSSFRow row = sheet.createRow((int) i);
//
//                row.createCell((int) 0).setCellValue(dateField+",");
//                row.createCell((int) 1).setCellValue(accountNo+",");
//                row.createCell((int) 2).setCellValue(costPrice+",");
//                row.createCell((int) 3).setCellValue(transType+",");
//                row.createCell((int) 4).setCellValue(Description+",");
//                row.createCell((int) 5).setCellValue(transCode+",");
//                row.createCell((int) 6).setCellValue(currency+",");
//                row.createCell((int) 7).setCellValue(branchCode+",");
//                row.createCell((int) 8).setCellValue(purposeCode+",");
//                row.createCell((int) 9).setCellValue(maker+",");
//                row.createCell((int) 10).setCellValue(batchNo+",");
//                row.createCell((int) 11).setCellValue(checker);
//                i++;
//            }
//            OutputStream stream = response.getOutputStream();
////          new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
//          workbook.write(stream);
//          stream.close();
//          System.out.println("Data is saved in excel file.");
//        	}
//    }
//    
//		
//	}
	public void DeletebulktransferPostings(){
		String query_r ="delete from am_gb_bulktransferPostings";
//		System.out.println(">>>>>===DeletebulktransferPostings  query_r: "+query_r);
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query_r);
		           int i =ps.executeUpdate();
		        } catch (Exception ex) {
		  
		            System.out.println("BulkAssetTransferPostingsServlet: DeletebulktransferPostings()>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
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
