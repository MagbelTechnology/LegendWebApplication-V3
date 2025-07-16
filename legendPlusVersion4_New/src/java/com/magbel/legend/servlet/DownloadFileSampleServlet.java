package com.magbel.legend.servlet;
import java.io.IOException;
import java.io.OutputStream;
   








import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
   








import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class DownloadFileSampleServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
    OutputStream out = null;
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
	String userClass = (String) request.getSession().getAttribute("UserClass");
    try
    {
     if (!userClass.equals("NULL") || userClass!=null){
    	ad = new AssetRecordsBean();
     response.setContentType("application/vnd.ms-excel");

     response.setHeader("Content-Disposition", 
    "attachment; filename=AssetProof.xls");
     String branch_Id = request.getParameter("BRANCH_ID");
     String prooftranId = request.getParameter("prooftranId");
     Report rep = new Report();
     String ColQuery = "";
     if(branch_Id!=null){ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BRANCH_ID = '"+branch_Id+"' AND BATCH_ID = '"+prooftranId+"'";}
     else{ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BATCH_ID = '"+prooftranId+"'";}
     
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
//     s.addCell(new Label(38, 0, "BRANCH ID");
     s.addCell(new Label(39, 0, "BATCH ID"));     
     java.util.ArrayList list =rep.getWorkBookSqlRecords(ColQuery);
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
		 System.out.println("<<<<<<I: "+i+"    K: "+k);
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
			
 //           HSSFRow row = sheet.createRow((short) i);

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
           s.addCell(new Label(19, i, Double.toString(costprice)));
           s.addCell(new Label(20, i,  Double.toString(monthlyDepr)));
           s.addCell(new Label(21, i,  Double.toString(accumDepr)));
           s.addCell(new Label(22, i,  Double.toString(nbv)));
           s.addCell(new Label(23, i,  Double.toString(improvcostPrice)));
           s.addCell(new Label(24, i,  Double.toString(improvmonthldepr)));
           s.addCell(new Label(25, i,  Double.toString(improvaccumDep)));
           s.addCell(new Label(26, i,  Double.toString(improvnbv)));
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
    }
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
     if (out != null)
      out.close();
    }
   }
}