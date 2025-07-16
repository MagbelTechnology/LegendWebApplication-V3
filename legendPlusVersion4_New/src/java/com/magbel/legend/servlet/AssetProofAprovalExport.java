package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import magma.AssetRecordsBean;
   
public class AssetProofAprovalExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	String userClass = (String) request.getSession().getAttribute("UserClass");
	if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetProofDownLoadForBM.xls";    	
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

    
        String branch_Id = request.getParameter("BRANCH_ID");
        String branchCode = request.getParameter("BRANCH_CODE");
        String tranId = request.getParameter("tranId");
        String prooftranId = request.getParameter("prooftranId");
        String mails = request.getParameter("mails1");
        String subject = request.getParameter("subject1");
        String msgText   = request.getParameter("msgText1");
        String otherparam  = request.getParameter("otherparam");
     Report rep = new Report();
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    prooftranId: "+prooftranId+"  branchCode: "+branchCode);
   String copymails = records.getCodeName("select EMAIL_ADDRESS from am_branch_Manager where BRANCH_CODE = '"+branchCode+"'");
//   System.out.println("<<<<<<copymails: "+copymails+"   Query: "+"select EMAIL_ADDRESS from am_branch_Manager where BRANCH_CODE = '"+branchCode+"'");
   String q = "update am_Asset_Proof set exported='YES',  PROCESS_STATUS = 'WFA' where BATCH_ID = '" + prooftranId + "'  AND NEW_REC = 'N'";
   ad.updateAssetStatusChange(q);     
     String ColQuery = "";
     if(branch_Id!=null){ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND NEW_REC = 'N' AND BRANCH_ID = ? AND BATCH_ID = ?";}
     else{ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND NEW_REC = 'N' AND BATCH_ID = ?";}

     java.util.ArrayList list =rep.getBulkAssetProofSqlRecords(ColQuery,branch_Id,prooftranId);
     if(list.size()!=0){
     WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
     WritableSheet s = w.createSheet("Demo", 0);

     s.addCell(new Label(0, 0, "ASSET_ID"));
     s.addCell(new Label(1, 0, "OLD ASSET ID"));
     s.addCell(new Label(2, 0, "CATEGORY NAME"));
     s.addCell(new Label(3, 0, "BRANCH NAME"));
     s.addCell(new Label(4, 0, "DEPARTMENT NAME"));
     s.addCell(new Label(5, 0, "SECTION NAME"));
     s.addCell(new Label(6, 0, "LOCATION"));
     s.addCell(new Label(7, 0, "STATE"));
     s.addCell(new Label(8, 0, "DESCRITION"));
     s.addCell(new Label(9, 0, "BAR CODE"));
     s.addCell(new Label(10, 0, "SBU CODE"));
     s.addCell(new Label(11, 0, "SERIAL NO."));
     s.addCell(new Label(12, 0, "REGISTRATION NO"));
     s.addCell(new Label(13, 0, "ENGINE NO"));
     s.addCell(new Label(14, 0, "ASSET MODEL"));
     s.addCell(new Label(15, 0, "MAKE"));
     s.addCell(new Label(16, 0, "LPO"));
     s.addCell(new Label(17, 0, "VENDOR NAME"));
     s.addCell(new Label(18, 0, "VENDOR ACCOUNT No."));
     s.addCell(new Label(19, 0, "COST PRICE"));
     s.addCell(new Label(20, 0, "ACCUMULATED DEPRECIATION"));
     s.addCell(new Label(21, 0, "MONTHLY DEPRECIATION"));
     s.addCell(new Label(22, 0, "NBV"));
     s.addCell(new Label(23, 0, "IMPROVE COST PRICE"));
     s.addCell(new Label(24, 0, "IMPROVE MONTHLY DEPRECIATION"));
     s.addCell(new Label(25, 0, "IMPROVE ACCUMULATED DEPRECIATION"));
     s.addCell(new Label(26, 0, "IMPROVE NBV"));
     s.addCell(new Label(27, 0, "PURCHASE DATE"));
     s.addCell(new Label(28, 0, "COMPONENT"));
     s.addCell(new Label(29, 0, "COMPONENT BARCODE"));
     s.addCell(new Label(30, 0, "SPARE FIELD 1"));
     s.addCell(new Label(31, 0, "SPARE FIELD 2"));
     s.addCell(new Label(32, 0, "SPARE FIELD 3"));
     s.addCell(new Label(33, 0, "SPARE FIELD 4"));
     s.addCell(new Label(34, 0, "ASSET USER"));
     s.addCell(new Label(35, 0, "COMMENTS"));
     s.addCell(new Label(36, 0, "ASSET SIGHTED"));
     s.addCell(new Label(37, 0, "ASSET FUNCTIONING"));
     s.addCell(new Label(38, 0, "ASSET CODE"));
     s.addCell(new Label(39, 0, "BATCH ID"));
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
			String branchId = newassettrans.getBranchCode();
			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			double costprice = newassettrans.getCostPrice();
			double monthlyDepr = newassettrans.getMonthlyDep();
			double accumDepr = newassettrans.getAccumDep();
			double nbv = newassettrans.getNbv();
			double improvcostPrice = newassettrans.getImprovcostPrice();
			double improvmonthldepr = newassettrans.getImprovmonthlyDep();
			double improvaccumDep = newassettrans.getImprovaccumDep();
			double improvnbv = newassettrans.getImprovnbv();
			double totalnbv = newassettrans.getTotalnbv();
			String categoryId = newassettrans.getCategoryCode();
			String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			String batchId = newassettrans.getIntegrifyId();
			String sighted = newassettrans.getAssetsighted();
			String function = newassettrans.getAssetfunction();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();
			String lpo = newassettrans.getLpo();
			String vendoAcct = newassettrans.getVendorAC();
			String vendorId = newassettrans.getSupplierName();
			String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
			String model = newassettrans.getAssetModel();
			String spare1 = newassettrans.getSpare1();
			String spare2 = newassettrans.getSpare2();
			String spare3 = newassettrans.getSpare3();
			String spare4 = newassettrans.getSpare4();
			String spare5 = newassettrans.getSpare5();
			String spare6 = newassettrans.getSpare6();        				
			String registrationNo = newassettrans.getRegistrationNo();
			String sbucode = newassettrans.getSbuCode();
			String serialNo = newassettrans.getSerialNo();
			String engineNo = newassettrans.getEngineNo();
			String purchaseDate = newassettrans.getDatepurchased();
			String deptId = newassettrans.getDeptCode();
			String deptName = records.getCodeName("select DEPT_NAME from am_ad_department where DEPT_ID = "+deptId+"");
			String sectionId = newassettrans.getSectionCode();
			String sectionName = records.getCodeName("select SECTION_NAME from am_ad_section where SECTION_ID = "+sectionId+"");
			String locationId = newassettrans.getLocation();
			String locationName = records.getCodeName("select LOCATION from AM_GB_LOCATION where LOCATION_ID = "+locationId+"");
			String stateId = newassettrans.getState();
			String stateName = records.getCodeName("select STATE_NAME from am_gb_states where STATE_ID = "+stateId+"");

           s.addCell(new Label(0, i, assetId));
           s.addCell(new Label(1, i, oldassetId));
           s.addCell(new Label(2, i, categoryName));
           s.addCell(new Label(3, i, branchName));
           s.addCell(new Label(4, i, deptName));
           s.addCell(new Label(5, i, sectionName));
           s.addCell(new Label(6, i, locationName));
           s.addCell(new Label(7, i, stateName));                        
           s.addCell(new Label(8, i, Description));
           s.addCell(new Label(9, i, barcode));
           s.addCell(new Label(10, i, sbucode));
           s.addCell(new Label(11, i, serialNo));
           s.addCell(new Label(12, i, registrationNo));
           s.addCell(new Label(13, i, engineNo));
           s.addCell(new Label(14, i, model));
           s.addCell(new Label(15, i, make));
           s.addCell(new Label(16, i, lpo));
           s.addCell(new Label(17, i, vendorName));
           s.addCell(new Label(18, i, vendoAcct));
           s.addCell(new Label(19, i, String.valueOf(costprice)));
           s.addCell(new Label(20, i, String.valueOf(monthlyDepr)));
           s.addCell(new Label(21, i, String.valueOf(accumDepr)));
           s.addCell(new Label(22, i, String.valueOf(nbv)));
           s.addCell(new Label(23, i, String.valueOf(improvcostPrice)));
           s.addCell(new Label(24, i, String.valueOf(improvmonthldepr)));
           s.addCell(new Label(25, i, String.valueOf(improvaccumDep)));
           s.addCell(new Label(26, i, String.valueOf(improvnbv)));
//           s.addCell(new Label(18, i, totalnbv);
           s.addCell(new Label(27, i, purchaseDate));
           s.addCell(new Label(28, i, spare1));
           s.addCell(new Label(29, i, spare2));
           s.addCell(new Label(30, i, spare3));
           s.addCell(new Label(31, i, spare4));
           s.addCell(new Label(32, i, spare5));
           s.addCell(new Label(33, i, spare6));                        
           s.addCell(new Label(34, i, assetuser));
           s.addCell(new Label(35, i, comments));
           s.addCell(new Label(36, i, sighted));
           s.addCell(new Label(37, i, function));
           s.addCell(new Label(38, i, assetcode));

           s.addCell(new Label(39, i, batchId));           
            i++;
     }
     w.write();
     w.close();
     String updateRcord =  "UPDATE am_Asset_Proof SET EXPORTED = 'YES' WHERE BATCH_ID = '"+prooftranId+"' AND EXPORTED IS NULL ";
     ad.updateAssetStatusChange(updateRcord);
//     System.out.println("AssetProof Approval mails: :>>> "+copymails+"   subject: "+subject+"   msgText: "+msgText);

     
//     out.print("<script>alert('Your Asset Proof has been exported to Excel file in your Downloads folder with File Name "+fileName+" ')</script>");
//     out.print("<script>window.location='DocumentHelp.jsp?np=AssetProofByBranchAdd'</script>");
 }
//    }
//     String deleteRcord =  "DROP TABLE "+tableName+" ";
//     ad.updateAssetStatusChange(deleteRcord);
//     System.out.println("AssetProof Approval mails:======>>> "+copymails+"   subject: "+subject+"   msgText: "+msgText);
     mail.sendMailWithAttachment(copymails,subject,msgText,branch_Id,fileName);
     //response.sendRedirect("DocumentHelp.jsp?np=AssetProofByBranchApproval&tranId="+prooftranId+"&transaction_level=1&approval_level_count=0");
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
//     if (out != null)
//      out.close();
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