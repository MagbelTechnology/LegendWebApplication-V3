package com.magbel.legend.servlet;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.FinancialExchangeServiceBus;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BulkUploadProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AssetRecordsBean ad;
    
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	AssetManager assetMan = null;
	HtmlUtility htmlUtil =null;
	SimpleDateFormat sdf;
	SimpleDateFormat sdfr;
	CompanyHandler comp = null;
	EmailSmsServiceBus mail = null;
    public BulkUploadProcessServlet()
    {
        super();
        try
        {
        ad = new AssetRecordsBean();
        }  
        
        catch(Exception et)
        {et.printStackTrace();}

    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		processCategoryItemType(request,response);
	}


	private void processCategoryItemType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{

	    try { 

	        mgDbCon = new MagmaDBConnection();
	        applHelper = new ApplicationHelper();
	        htmlUtil = new HtmlUtility();
	        assetMan = new AssetManager();
	        mail = new EmailSmsServiceBus();
	        sdf = new SimpleDateFormat("dd-MM-yyyy");
	        sdfr = new java.text.SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
	        // Get request/session parameters
	        HttpSession session = request.getSession();
	        String groupid = request.getParameter("groupid");
	        String raiseBtn = request.getParameter("raiseBtn");
	        String reject = request.getParameter("reject");
//	        System.out.println("======>groupid: "+groupid+"  =====raiseBtn: "+raiseBtn);
	        String userBranch = (String) session.getAttribute("UserCenter");
	        String userClass = (String) session.getAttribute("UserClass");
	        String userId = (String) session.getAttribute("CurrentUser");
 
//	        String postingDate = dbConnection.getDateTime(new java.util.Date());
	        String postingDate = sdf.format(new java.util.Date());
	        String postingDateTime = sdfr.format(new java.util.Date());
	        
//System.out.println("==========>postingDate: "+postingDate);
	        String DD = postingDate.substring(0,2);
	        String MM = postingDate.substring(3,5);
	        String YYYY = postingDate.substring(6,10);
	        postingDate = YYYY+"-"+MM+"-"+DD;
	       
	        System.out.println("==========>postingDate: "+postingDate+"     =====>postingDateTime"+postingDateTime);
	        if (userClass == null || userClass.equalsIgnoreCase("NULL")) {
	            response.sendRedirect("unauthorized.jsp");
	            return;
	        }

	        if ("Reject".equalsIgnoreCase(reject)) {
	            handleRejection(groupid, mail, response);
	            return;
	        }

	        if ("Posting".equalsIgnoreCase(raiseBtn)) {
	            boolean isExcelGenerated = handlePosting(request, response, userId, userBranch, mail, assetMan, htmlUtil, groupid,postingDate,postingDateTime);
//	            System.out.println("==========>isExcelGenerated: "+isExcelGenerated);
//	            if (isExcelGenerated) {
//	                showSuccessAlert(response, "Uploaded Assets have been Posted unto Legacy System");
//	            }
	            return;
	        }

	        showSuccessAlert(response, "No action performed.");

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while processing asset upload.");
	    }

	}
	
	private boolean handlePosting(HttpServletRequest request, HttpServletResponse response, String userId, String userBranch,
            EmailSmsServiceBus mail, AssetManager assetMan, HtmlUtility htmlUtil, String groupid, String postingDate, String postingDateTime) throws Exception {
		
			String vendordebitNarration = request.getParameter("description");
			String vendoramount = request.getParameter("totalcostPrice");
			String vendorcreditAccount = request.getParameter("vendorAccount");
			String vendorIdgrp = request.getParameter("vendorId");
			String vendordebitAccount = "UploadAccount";
			String locationgrp = request.getParameter("location");
			String projectCodegrp = request.getParameter("projectCode");
			String thirdPartyLabel = request.getParameter("ThirdPartyLabel");
			String returnPage = request.getParameter("MenuPage"); 
			String page1 = "ASSET UPLOAD CREATION RAISE ENTRY";
			String transactionId = "grp";
			
			// Process asset records
			
			boolean transfer = false;
			String []result = null ;
			String batchNo = "";
			String assetId = "";
			//System.out.println("<<<<<<<<<TEST 1>>>>>>>> ");
			java.util.ArrayList list0 =bulkUploadRecordforsReport(groupid);
			System.out.println("-->size>--> "+list0.size()+"  Batch Id:  "+groupid);
			int j = -1;
			for(int i=0;i<list0.size();i++)
			{
				 String assetId_batchNo=(String) list0.get(i);
				 result = assetId_batchNo.split("#");
				 batchNo = result[0];
				 assetId = result[1];
				 if(!assetId.equalsIgnoreCase(""))
//					 if(!thirdPartyLabel.equalsIgnoreCase("K2")){insertBulkUploadPostingEntries(assetId, batchNo,postingDate);}	 	 
//				 	 if(thirdPartyLabel.equalsIgnoreCase("K2")){insertBulkUploadPostingEntriesK2(assetId, batchNo,postingDate);}
				 	 j = insertBulkUploadPostingEntriesToLegacy(groupid,postingDate,postingDateTime);
				 	System.out.println("<<<<<<<<J: "+j);
			}
			 if(!thirdPartyLabel.equalsIgnoreCase("K2")){insertBulkUploadPostingEntries(batchNo);}	 	 
		 	 if(thirdPartyLabel.equalsIgnoreCase("K2")){insertBulkUploadPostingEntriesK2(assetId, batchNo,postingDate);}
		 	 
			if(j!=-1) {
				int k = insertBulkUploadPostingEntriesToBatchTable(groupid,postingDate,postingDateTime,userId);
				
				ArrayList<?> list = assetMan.getUploadAssetRecords(groupid);
				for (Object obj : list) {
				magma.net.vao.GroupAsset upload = (magma.net.vao.GroupAsset) obj;
				
				String assetProcurementDr = ad.checkAssetLedgerAccount(upload.getCategory().trim(), upload.getBranchCode().trim());
				ad.insertVendorTransaction(userId, upload.getDescription(), assetProcurementDr, upload.getVendorAccount(),
				upload.getCostPrice(), upload.getLocation(), groupid, page1, upload.getProjectCode(), upload.getVendorId());
				
				ad.updateAssetStatusChange("UPDATE AM_ASSET SET ASSET_STATUS = 'ACTIVE' WHERE GROUP_ID='" + groupid + "' AND ASSET_ID = '" + upload.getAssetId() + "'");
				ad.updateAssetStatusChange("UPDATE AM_GROUP_ASSET SET ASSET_STATUS = 'ACTIVE', RAISE_ENTRY = 'Y' WHERE GROUP_ID='" + groupid + "' AND ASSET_ID = '" + upload.getAssetId() + "'");
				ad.updateAssetStatusChange("UPDATE am_group_asset_main SET ASSET_STATUS = 'ACTIVE' WHERE GROUP_ID='" + groupid + "'");
				}
				
				// Posting summary
				ad.updateAssetStatusChange("UPDATE am_raisentry_post SET GroupIdStatus = 'Y', entryPostFlag = 'Y' WHERE id='" + groupid + "'");
				ad.updateAssetStatusChange("UPDATE am_asset_approval SET Process_Status = 'A' WHERE batch_id='" + groupid + "'");
				ad.insertVendorTransaction(userId, vendordebitNarration, vendordebitAccount, vendorcreditAccount,
				Double.parseDouble(vendoramount), locationgrp, transactionId, page1, projectCodegrp, vendorIdgrp);								
			}
			
//			if ("K2".equalsIgnoreCase(thirdPartyLabel)) {
//			insertBulkUploadSummaryPostingEntriesK2(groupid,postingDate);
//			} else {
//				insertBulkUploadSummaryPostingEntries(groupid,postingDate);
//			}
//			  
				
			// Generate Excel
			ArrayList<?> reportList = bulkUploadRecordListsReport(groupid);
			if (!reportList.isEmpty()) {
			generateExcelReport(response, reportList, htmlUtil, userId, userBranch, groupid);
			//mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Upload",
			//"Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Posted.");
			String msgText12 = "Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Posted.";
			String mailsubject = "Bulk Asset Upload";
			System.out.println("-->Batch Id-->:  "+groupid);
			String initaitoruserId = htmlUtil.getCodeName("SELECT user_Id from am_asset_approval WHERE ASSET_ID = '"+batchNo+"'");
			String alertMail = htmlUtil.getCodeName("SELECT email FROM am_gb_User WHERE USER_ID = " + initaitoruserId);
			//comp.sendAssetManagementMail(alertMail, mailsubject, msgText12);
			
			return true;
}

return false;
}

	
	private void handleRejection(String groupid, EmailSmsServiceBus mail, HttpServletResponse response) throws IOException {
	    DeletebulkImprovementUploadRejections(groupid);
	    String msg = "Your transaction for Bulk asset Upload with Batch ID " + groupid + " has been Rejected.";
	    mail.sendMailTransactionInitiator(Integer.parseInt(groupid), "Bulk Asset Upload", msg);
	    showSuccessAlert(response, "Upload Rejected Successfully");
	}

	
	
	private void generateExcelReport(HttpServletResponse response, ArrayList<?> data, HtmlUtility htmlUtil,
            String userId, String userBranch, String groupid) throws Exception {

String userName = htmlUtil.getCodeName("SELECT USER_NAME FROM am_gb_User WHERE USER_ID = " + userId);
String branchCode = htmlUtil.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = " + userBranch);
String fileName = branchCode + "By" + userName + "LegacyAssetExport.xls";

response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

HSSFWorkbook workbook = new HSSFWorkbook();
HSSFSheet sheet = workbook.createSheet("Legacy Asset Export Record");
HSSFRow header = sheet.createRow(0);
//String[] headers = {"Dr. Account No.","Cr. Account No.", "DR CR", "Amount", "Description", "E", "F", "Asset Id", "H", "I", "SBU CODE"};
String[] headers = {"Dr. Account No.","Cr. Account No.", "Amount", "Description", "E", "F", "Asset Id", "H", "I", "SBU CODE"};
for (int i = 0; i < headers.length; i++) {
header.createCell(i).setCellValue(headers[i]);
}

int rowIndex = 1;
for (Object obj : data) {
magma.AssetRecordsBean asset = (magma.AssetRecordsBean) obj;
HSSFRow row = sheet.createRow(rowIndex++);
row.createCell(0).setCellValue(asset.getSpare_1());
row.createCell(1).setCellValue(asset.getVendor_account());
//row.createCell(2).setCellValue(asset.getStatus());
row.createCell(2).setCellValue(Double.parseDouble(asset.getCost_price()));
row.createCell(3).setCellValue(asset.getDescription());
row.createCell(6).setCellValue(asset.getAsset_id());
row.createCell(9).setCellValue(asset.getSbu_code());
}

try (OutputStream out = response.getOutputStream()) {
workbook.write(out);
out.flush();
}
}

	
	private void showSuccessAlert(HttpServletResponse response, String message) throws IOException {
	    response.setContentType("text/html");
	    try (PrintWriter out = response.getWriter()) {
//	        out.print("<script>alert('" + message + "');</script>");
//	        out.print("<script>window.location='DocumentHelp.jsp?np=raiseEntry&tranType=Asset Upload';</script>");
	    }
	}

	
	public void DeletebulkImprovementUploadRejections(String batchId){
		String query1 ="delete from AM_GROUP_ASSET where GROUP_ID = '"+batchId+"'";
		String query2 ="delete from AM_ASSET where GROUP_ID = '"+batchId+"'";
		String query3 ="delete from am_group_asset_main where GROUP_ID = '"+batchId+"'";
		String query4 ="delete from am_asset_approval where batch_id = '"+batchId+"'";
		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query1);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_GROUP_ASSET>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query2);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for AM_ASSET>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);  
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query3);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_group_asset_main>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }		
		try {
			con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query4);
				int i =ps.executeUpdate();
		        } catch (Exception ex) {
		            System.out.println("BulkUploadRejectionServlet: DeletebulkUploadRejections() for am_asset_approval>>>>>" + ex);
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
//	
//public boolean insertBulkUploadPostingEntries(String assetId, String batchId,String postingDate) {
//	String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"+
//    		"(select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
//    		"d.branch_code +	b.asset_acq_ac ACCOUNT,'C' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"+
//    		"e.Description+'   '+e.ASSET_ID AS NARRATION,'"+postingDate+"'  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
//    		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//    		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ? "+
//    		"UNION "+
//    		"select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "+
//    		"d.branch_code +	a.Asset_Ledger ACCOUNT,'D' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT, "+
//    		"e.Description+'   '+e.ASSET_ID AS NARRATION,'"+postingDate+"' from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,  "+
//    		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
//    		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ?) ";
//Connection con = null;
//PreparedStatement ps = null;
//boolean done = false;
//// System.out.println("insertBulkUploadPostingEntries query: "+query+"   assetId: "+assetId+"    batchId: "+batchId);
//try {
//con = mgDbCon.getConnection("legendPlus");
//ps = con.prepareStatement(query);
//  ps.setString(1, batchId);
//  ps.setString(2, assetId);
//  ps.setString(3, batchId);
//  ps.setString(4, assetId);
//
//done = (ps.executeUpdate() != -1);
//} catch (Exception ex) {
//done = false;
//System.out.println("Error insertBulkUploadPostingEntries() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
//ex.printStackTrace();
//} finally {
//closeConnection(con, ps);
//}
//return done;
//}

public boolean insertBulkUploadPostingEntries(String batchId) {
	 String q3 = "delete from am_gb_bulkPostingEntries where BATCHID = '" + batchId + "'";
     ad.updateAssetStatusChange(q3);
	String accountQuery = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'ASSETCREATION' AND TYPE = 'UPLOAD'");
	String query = "INSERT INTO am_gb_bulkPostingEntries (DR_ACCOUNT,CR_ACCOUNT,STATUS,AMOUNT,narration,RECTYPE,TRANSTYPE,asset_id,BATCHID,I,E,F,H,sbu_code)"+
    		"("+accountQuery+") ";
	 
	
	
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
// System.out.println("insertBulkUploadPostingEntries query: "+query+"   assetId: "+assetId+"    batchId: "+batchId);
//System.out.println("insertBulkUploadPostingEntries query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);
ps.setString(1, batchId);
ps.setString(2, batchId);
ps.setString(3, batchId);
ps.setString(4, batchId);
ps.setString(5, batchId);
done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkUploadPostingEntries() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}



public boolean insertBulkUploadPostingEntriesK2(String assetId, String batchId,String postingDate) {
String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)"+
		"(select e.asset_id,substring(d.branch_code,3,3) + c.iso_code + b.asset_acq_ac ACCOUNT,'C' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"+
		"e.Description+'   '+e.ASSET_ID AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "+
		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ? "+
		"UNION "+
		"select e.asset_id,substring(d.branch_code,3,3) + c.iso_code + b.asset_acq_ac ACCOUNT,'D' AS CR_DR,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT, "+
		"e.Description+'   '+e.ASSET_ID AS NARRATION from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,  "+
		"am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "+
		"and d.branch_id = e.branch_id and e.GROUP_ID = ? and e.Asset_id = ?) ";
Connection con = null;
PreparedStatement ps = null;
boolean done = false;
//System.out.println("insertBulkUploadPostingEntriesK2 query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);
ps.setString(1, batchId);
ps.setString(2, assetId);
ps.setString(3, batchId);
ps.setString(4, assetId);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkUploadPostingEntriesK2() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}

public boolean insertBulkUploadSummaryPostingEntries(String batchId,String postingDate) {
String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)(select e.GROUP_ID,"+
		"c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ d.branch_code +"+
		"b.asset_acq_ac ACCOUNT,'D' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION,'"+postingDate+"'  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b, "+
		"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id and e.GROUP_ID = ? "+
		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,b.asset_acq_ac)"+
		"UNION "+
		"select e.GROUP_ID AS asset_id,e.Vendor_AC ACCOUNT,'C' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
		"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION,'"+postingDate+"' from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, "+
		"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id "+
		"and e.GROUP_ID = ? "+
		"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,e.Vendor_AC";

Connection con = null;
PreparedStatement ps = null;
boolean done = false;
//System.out.println("insertBulkUploadSummaryPostingEntries query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);
ps.setString(1, batchId);
ps.setString(2, batchId);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkUploadSummaryPostingEntries() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}

public boolean insertBulkUploadSummaryPostingEntriesK2(String batchId,String postingDate) {
String query = "INSERT INTO am_gb_bulkPostingEntries (asset_id,Account,STATUS,BatchId,sbu_code,amount, narration,POSTING_DATE)(select e.GROUP_ID,"+
	"substring(d.branch_code,3,3)+c.iso_code +b.asset_acq_ac ACCOUNT,'D' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
	"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION,'"+postingDate+"'  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b, "+
	"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id and e.GROUP_ID = ? "+
	"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,b.asset_acq_ac)"+
	"UNION "+
	"select e.GROUP_ID AS asset_id,e.Vendor_AC ACCOUNT,'C' AS CR_DR,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "+
	"'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION,'"+postingDate+"' from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b, "+
	"AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id "+
	"and e.GROUP_ID = ? "+
	"GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,e.Vendor_AC";

Connection con = null;
PreparedStatement ps = null;
boolean done = false;
//System.out.println("insertBulkUploadSummaryPostingEntriesK2 query: "+query);
try {
con = mgDbCon.getConnection("legendPlus");
ps = con.prepareStatement(query);
ps.setString(1, batchId);
ps.setString(2, batchId);

done = (ps.executeUpdate() != -1);
} catch (Exception ex) {
done = false;
System.out.println("Error insertBulkUploadPostingEntriesK2() of BulkAssetUploadPostingsServlet -> " + ex.getMessage());
ex.printStackTrace();
} finally {
closeConnection(con, ps);
}
return done;
}

public java.util.ArrayList bulkUploadRecordforsReport(String batchId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = " SELECT  * from AM_GROUP_ASSET where GROUP_ID = ?  ";
//		System.out.println("====bulkTransferRecordforsReport query-----> "+query+"   ------> Batch Id: "+batchId);
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
			rs = s.executeQuery();
			while (rs.next())
			   {
				String batch_Id = rs.getString("GROUP_ID");
				String asset_id = rs.getString("Asset_id");
				result = batch_Id + "#"+asset_id;
//				System.out.println("batch_Id:  "+batch_Id+"  asset_id: "+asset_id+"       result: "+result);
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

public java.util.ArrayList bulkUploadRecordListsReport(String batchId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = " SELECT  * FROM am_gb_bulkPostingEntries where batchId = ? ORDER BY BATCHID ";
//		System.out.println("====bulkUploadRecordListsReport query-----> "+query);
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
			rs = s.executeQuery();
			while (rs.next())
			   {
				String asset_id = rs.getString("Asset_id");
				String description = rs.getString("narration");
				batchId = rs.getString("BATCHID");
				String amount = rs.getString("amount");
				String sbuCode = rs.getString("SBU_CODE");
				String status = rs.getString("STATUS");
				String account = rs.getString("ACCOUNT");
				String draccount = rs.getString("DR_ACCOUNT");
				String craccount = rs.getString("CR_ACCOUNT");
				AssetRecordsBean trans = new AssetRecordsBean();
				description = asset_id+"*"+batchId+"*"+description;
				trans.setAsset_id(asset_id);
				trans.setDescription(description);
				trans.setCost_price(amount);
				trans.setSbu_code(sbuCode);
				trans.setStatus(status);
				trans.setBatchId(batchId);
				trans.setVendor_account(craccount);
				trans.setSpare_1(draccount);
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


public java.util.ArrayList bulkUploadRecordListsReportToLegacy(String batchId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = "select e.asset_id,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "
				+ "d.branch_code +	b.asset_acq_ac CR_ACCOUNT,c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ "
				+ "d.branch_code +	a.Asset_Ledger DR_ACCOUNT,e.GROUP_ID,e.SBU_CODE AS SBU_CODE,e.COST_PRICE AS AMOUNT,"
				+ "e.Description+'   '+e.ASSET_ID AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, "
				+ "am_gb_company b, AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID "
				+ "and d.branch_id = e.branch_id and e.GROUP_ID = ? "
				+ "UNION "
				+ "select convert(varchar ,e.GROUP_ID) ASSET_ID,"
				+ "c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+ d.branch_code + "
				+ "b.asset_acq_ac DR_ACCOUNT,e.Vendor_AC CR_ACCOUNT,e.GROUP_ID,'' AS SBU_CODE,SUM(e.COST_PRICE) AS AMOUNT, "
				+ "'Group Posting for Batch_Id - '+convert(varchar ,e.GROUP_ID) AS NARRATION  from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b, "
				+ "AM_GROUP_ASSET e where a.currency_id = c.currency_id and a.category_ID = e.CATEGORY_ID and d.branch_id = e.branch_id and e.GROUP_ID = ? "
				+ "GROUP BY e.GROUP_ID,c.iso_code,a.Asset_Ledger,d.branch_code,b.asset_acq_ac,e.Vendor_AC  ";
//		System.out.println("====bulkUploadRecordListsReportToLegacy query-----> "+query);
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
			rs = s.executeQuery();
			while (rs.next())
			   {
				String asset_id = rs.getString("ASSET_ID");
				String description = rs.getString("NARRATION");
				batchId = rs.getString("GROUP_ID");
				String amount = rs.getString("AMOUNT");
				String crAccounNo = rs.getString("CR_ACCOUNT");
				String drAccounNo = rs.getString("DR_ACCOUNT");
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


public java.util.ArrayList bulkUploadAccountParamRecordListsReportToLegacy(String batchId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
	String query = htmlUtil.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'ASSETCREATION' AND TYPE = 'UPLOAD'");
	//		System.out.println("====bulkUploadAccountParamRecordListsReportToLegacy query-----> "+query);
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
			s.setString(5, batchId);
			
			rs = s.executeQuery();
			while (rs.next())
			   {
				String asset_id = rs.getString("ASSET_ID");
				String description = rs.getString("NARRATION");
				batchId = rs.getString("GROUP_ID");
				String amount = rs.getString("AMOUNT");
				String crAccounNo = rs.getString("CR_ACCOUNT");
				String drAccounNo = rs.getString("DR_ACCOUNT");
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
//public int insertBulkUploadPostingEntriesToLegacy(String batchId,String postingDate, String postingDateTime) {
//  	int x = -1;		
//
//    java.util.ArrayList list =bulkUploadAccountParamRecordListsReportToLegacy(batchId);
//    //================================
////      String query3 = "insert into custom.fxd_asset(sbu_code,dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) values(?,?,?,?,?,?,?,?)";
////      String query3 = "insert into custom.BULK_LGD(SBU_CODE,ID,DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, "
////        		+ "    RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) values(?,?,?,?,?,?,?,?,?,?,?)";
////
//      try{
//      	Connection conn = null;
//      ConnectionClass connection = new ConnectionClass();
//      	conn = connection.getOracleConnection();
//      	
////      	System.out.print("The value of list.size() is ----======....." +list.size());
//      	
//	     for(int i=0;i<list.size();i++)
//	     {   //AssetRecordsBean trans = new AssetRecordsBean();
//	    	 magma.AssetRecordsBean  asset = (magma.AssetRecordsBean)list.get(i);   
//	    	 String craccountNo = asset.getVendor_account();
//	    	 String draccountNo = asset.getSpare_1();
////	    	 String debit_credit = asset.getStatus();
//	    	 double amount = Double.parseDouble(asset.getCost_price());
//	    	 String description = asset.getDescription();
//	    	 String asset_Id = asset.getAsset_id();
//	    	 String sbuCode = asset.getSbu_code();
//	    	 
////	    	 postingDate = "TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS')";
////	    	 postingDateTime = "TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS')";
//	         String query3 = " insert into custom.BULK_LGD(SBU_CODE,ID,DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, "
//	         		+ " RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) values('"+sbuCode+"','"+asset_Id+"','"+draccountNo+"','"+craccountNo+"',"+amount+",'"+description+"',TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS'),TO_DATE('"+postingDateTime+"', 'DD-MON-YYYY HH24:MI:SS'),'FINAPP','"+description+"','N') ";
////	         		+ " COMMIT TRANSACTION; ";
//	         System.out.print("The value of query3 is ----======....." +query3);
//	         
//      //PreparedStatement ps = conn.getPreparedStatementOracle(query3);
//      	 PreparedStatement ps = conn.prepareStatement(query3);
////	        System.out.print("The value of sbuCode is ----======....." +sbuCode);
////	        ps.setString(1,sbuCode);
////	        ps.setString(2,asset_Id);
////	        System.out.print("The value of dr_acct is ----======....." +draccountNo);
////	        ps.setString(3,draccountNo);
////	        System.out.print("The value of cr_acct is ----======....." +craccountNo);
////	        ps.setString(4,craccountNo);
////	        System.out.print("The value of amount is ----======....." +amount);
////	        ps.setDouble(5,amount);
////	        System.out.print("The value of narration is ----======....." +description);
////	        ps.setString(6,description);
////	        ps.setString(7, postingDate);
////	        ps.setString(8, postingDateTime);
////	        ps.setString(9, "FINAPP");	       
////	        System.out.print("The value of narration2 is ----======....." +description);
////	        ps.setString(10,description);
////	        System.out.print("The value of value_date is ----======....." +postingDateTime);
////	        ps.setString(11, "N");
//      	 	x = ps.executeUpdate();
//	     }  
//	   conn.commit();
//       conn.close();
// // connection.freeResource();
//  }//try
//  catch(Exception e){ 
//  e.getMessage();
//  }
//  
//      //catch
////  System.out.print("The value of x is ------------==================............." + x);
//  return x;
//  }	//		getIndividualTransaction()	

public int insertBulkUploadPostingEntriesToLegacy(
        String batchId, String postingDate, String postingDateTime) {
	

    int totalInserted = 0;
    Connection conn = null;
    PreparedStatement ps = null;

    try {
        ArrayList list = bulkUploadAccountParamRecordListsReportToLegacy(batchId);
        ConnectionClass connection = new ConnectionClass();
        conn = connection.getOracleConnection();
        conn.setAutoCommit(false);
        

        String sql = "INSERT INTO custom.BULK_LGD " +
                "(SBU_CODE, ID, DR_ACCT, CR_ACCT, AMOUNT, NARRATION, VALUE_DATE, " +
                "RCRE_TIME, FIN_USER, NARRATION2, PROCESSED_FLG) " +
                "VALUES (?,?,?,?,?, ?, TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS'), " +
                "TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS'), ?, ?, ?)";

        ps = conn.prepareStatement(sql);

        for (int i = 0; i < list.size(); i++) {
            magma.AssetRecordsBean asset = (magma.AssetRecordsBean) list.get(i);

            ps.setString(1, asset.getSbu_code());
            ps.setString(2, asset.getAsset_id());
            ps.setString(3, asset.getSpare_1());      // DR
            ps.setString(4, asset.getVendor_account());// CR
            ps.setDouble(5, Double.parseDouble(asset.getCost_price()));
            ps.setString(6, asset.getDescription());
            ps.setString(7, postingDateTime);
            ps.setString(8, postingDateTime);
            ps.setString(9, "FINAPP");
            ps.setString(10, asset.getDescription());
            ps.setString(11, "N");

            ps.addBatch();
        }

        int[] results =  ps.executeBatch();
        conn.commit();

        totalInserted = results.length;

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        e.printStackTrace(); 
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }

    return totalInserted;
}

//Transfered from jsp
public int insertBulkUploadPostingEntriesToBatchTable(String batchId,String postingDate, String postingDateTime,String userId) {
	int x = -1;		
	Connection con = null;
	PreparedStatement ps = null;	

  java.util.ArrayList list =bulkUploadAccountParamRecordListsReportToLegacy(batchId);
  //================================

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

	         String transType = "Asset Creation Upload";
	         ps = con.prepareStatement(query3);
	        ps.setString(1,batchId);
	        ps.setString(2,sbuCode);
	        ps.setString(3,asset_Id);
	        ps.setString(4,draccountNo);
	        ps.setString(5,craccountNo);
	        ps.setString(6,drdescription);
	        ps.setString(7,crdescription);
	        ps.setDouble(8,amount);
	        ps.setString(9,userId);
	        ps.setString(10, postingDate);
	        ps.setString(11, postingDateTime);
	        ps.setString(12,transType);

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


	     
	     


}
