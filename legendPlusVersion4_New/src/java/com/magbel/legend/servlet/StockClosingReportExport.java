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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class StockClosingReportExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String userId = (String) request.getSession().getAttribute("CurrentUser");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
//    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch") == null ? "0" : request.getParameter("branch");
    String categoryCode = request.getParameter("category") == null ? "0" : request.getParameter("category");
    String itemCode = request.getParameter("itemCode") == null ? "0" : request.getParameter("itemCode");
  

    String report = "rptMenuStockClosing";
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }

    String fileName = "";
    if(report.equalsIgnoreCase("rptMenuStockClosing")){fileName = branchCode+"By"+userName+"StockClosingReportExport.xlsx";}
//    System.out.println("<<<<<<fileName: "+fileName);
    String filePath = System.getProperty("user.home")+"\\Downloads";
//    System.out.println("<<<<<<filePath: "+filePath);
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	

	response.setIntHeader("Content-Length", -1);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        
     Report rep = new Report();

     String ColQuery = "";
    	     ColQuery ="SELECT *FROM ST_STOCK a, ST_INVENTORY_ITEMS b, ST_STOCK_CATEGORY c\n"
    	     		+ "WHERE a.ITEM_CODE = b.ITEM_CODE\n"
    	     		+ "AND b.Category_Code = c.CATEGORY_CODE\n"
    	     		+ "";
    

  
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getStockClosingRecords(ColQuery,branch_Id,categoryCode,itemCode);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuStockClosing")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell(0).setCellValue("S/No.");
         rowhead.createCell(1).setCellValue("Asset_id");
         rowhead.createCell(2).setCellValue("Registration_No");
         rowhead.createCell(3).setCellValue("Branch_ID");
         rowhead.createCell(4).setCellValue("Dept_ID");
         rowhead.createCell(5).setCellValue("Category_ID");
         rowhead.createCell(6).setCellValue("Section_id");
         rowhead.createCell(7).setCellValue("Description");
         rowhead.createCell(8).setCellValue("Vendor_AC");
         rowhead.createCell(9).setCellValue("Date_purchased");
         rowhead.createCell(10).setCellValue("Dep_Rate");
         rowhead.createCell(11).setCellValue("Asset_Make");
         rowhead.createCell(12).setCellValue("Asset_Model");
         rowhead.createCell(13).setCellValue("Asset_Serial_No");
         rowhead.createCell(14).setCellValue("Asset_Engine_No");
         rowhead.createCell(15).setCellValue("Supplier_Name");
         rowhead.createCell(16).setCellValue("Asset_User");
         rowhead.createCell(17).setCellValue("Asset_Maintenance");
         rowhead.createCell(18).setCellValue("Accum_Dep");
         rowhead.createCell(19).setCellValue("Monthly_Dep");
         rowhead.createCell(20).setCellValue("Cost_Price");
         rowhead.createCell(21).setCellValue("NBV");
         rowhead.createCell(22).setCellValue("Dep_End_Date");
         rowhead.createCell(23).setCellValue("Residual_Value");
         rowhead.createCell(24).setCellValue("Authorized_By");
         rowhead.createCell(25).setCellValue("Wh_Tax");
         rowhead.createCell(26).setCellValue("Wh_Tax_Amount");
         rowhead.createCell(27).setCellValue("Req_Redistribution");
         rowhead.createCell(28).setCellValue("Posting_Date");
         rowhead.createCell(29).setCellValue("Effective_Date");
         rowhead.createCell(30).setCellValue("Purchase_Reason");
         rowhead.createCell(31).setCellValue("Useful_Life");
         rowhead.createCell(32).setCellValue("Total_Life");
         rowhead.createCell(33).setCellValue("Location");
         rowhead.createCell(34).setCellValue("Remaining_Life");
         rowhead.createCell(35).setCellValue("Vatable_Cost");
         rowhead.createCell(36).setCellValue("Vat");
         rowhead.createCell(37).setCellValue("Req_Depreciation");
         rowhead.createCell(38).setCellValue("Subject_TO_Vat");
         rowhead.createCell(39).setCellValue("Raise_entry");
         rowhead.createCell(40).setCellValue("Dep_Ytd");
         rowhead.createCell(41).setCellValue("Section");
         rowhead.createCell(42).setCellValue("Asset_Status");
         rowhead.createCell(43).setCellValue("State");
         rowhead.createCell(44).setCellValue("Driver");
         rowhead.createCell(45).setCellValue("Spare_1");
         rowhead.createCell(46).setCellValue("Spare_2");
         rowhead.createCell(47).setCellValue("Date_Disposed");
         rowhead.createCell(48).setCellValue("Province");
         rowhead.createCell(49).setCellValue("Multiple");
         rowhead.createCell(50).setCellValue("WAR_START_DATE");
         rowhead.createCell(51).setCellValue("WAR_MONTH");
         rowhead.createCell(52).setCellValue("WAR_EXPIRY_DATE");
         rowhead.createCell(53).setCellValue("Last_Dep_Date");
         rowhead.createCell(54).setCellValue("BRANCH_CODE");
         rowhead.createCell(55).setCellValue("SECTION_CODE");
         rowhead.createCell(56).setCellValue("DEPT_CODE");
         rowhead.createCell(57).setCellValue("CATEGORY_CODE");
         rowhead.createCell(58).setCellValue("AMOUNT_PTD");
         rowhead.createCell(59).setCellValue("AMOUNT_REM");
         rowhead.createCell(60).setCellValue("PART_PAY");
         rowhead.createCell(61).setCellValue("FULLY_PAID");
         rowhead.createCell(62).setCellValue("GROUP_ID");
         rowhead.createCell(63).setCellValue("BAR_CODE");
         rowhead.createCell(64).setCellValue("SBU_CODE");
         rowhead.createCell(65).setCellValue("LPO");
         rowhead.createCell(66).setCellValue("supervisor");
         rowhead.createCell(67).setCellValue("defer_pay");
         rowhead.createCell(68).setCellValue("OLD_ASSET_ID");
         rowhead.createCell(69).setCellValue("WHT_PERCENT");
         rowhead.createCell(70).setCellValue("Post_reject_reason");
         rowhead.createCell(71).setCellValue("Finacle_Posted_Date");
         rowhead.createCell(72).setCellValue("ItemType");
         rowhead.createCell(73).setCellValue("WAREHOUSE_CODE");
         rowhead.createCell(74).setCellValue("SUB_CATEGORY_CODE");
         rowhead.createCell(75).setCellValue("SPARE_3");
         rowhead.createCell(76).setCellValue("SPARE_4");
         rowhead.createCell(77).setCellValue("SPARE_5");
         rowhead.createCell(78).setCellValue("SPARE_6");
         rowhead.createCell(79).setCellValue("MEMO");
         rowhead.createCell(80).setCellValue("memovalue");
         rowhead.createCell(81).setCellValue("INTEGRIFY");
         rowhead.createCell(82).setCellValue("IMPROV_COST");
         rowhead.createCell(83).setCellValue("IMPROV_ACCUMDEP");
         rowhead.createCell(84).setCellValue("IMPROV_NBV");
         rowhead.createCell(85).setCellValue("IMPROV_VATABLECOST");
         rowhead.createCell(86).setCellValue("TOTAL_NBV");
         rowhead.createCell(87).setCellValue("IMPROV_USEFULLIFE");
         rowhead.createCell(88).setCellValue("IMPROV_TOTALLIFE");
         rowhead.createCell(89).setCellValue("IMPROV_REMAINLIFE");
         rowhead.createCell(90).setCellValue("IMPROV_MONTHLYDEP");
         rowhead.createCell(91).setCellValue("QUANTITY");
         rowhead.createCell(92).setCellValue("UNIT_CODE");
         rowhead.createCell(93).setCellValue("ASSET_CODE");
         rowhead.createCell(94).setCellValue("REGION_CODE");
         rowhead.createCell(95).setCellValue("ZONE_CODE");
         rowhead.createCell(96).setCellValue("PROJECT_CODE");
         rowhead.createCell(97).setCellValue("UNIT_PRICE");
         rowhead.createCell(98).setCellValue("MTID");
         rowhead.createCell(99).setCellValue("COMP_CODE");
         rowhead.createCell(100).setCellValue("ITEM_CODE");
         rowhead.createCell(101).setCellValue("STATUS");
         rowhead.createCell(102).setCellValue("ITEMTYPE_CODE");
         rowhead.createCell(103).setCellValue("TAX_CODE");
         rowhead.createCell(104).setCellValue("MIN_QUANTITY");
         rowhead.createCell(105).setCellValue("WEIGHT_AVG_COST");
         rowhead.createCell(106).setCellValue("STANDARD_COST");
         rowhead.createCell(107).setCellValue("WEIGHT");
         rowhead.createCell(108).setCellValue("BACKORDERABLE");
         rowhead.createCell(109).setCellValue("USERID");
         rowhead.createCell(110).setCellValue("COG_SOLD");
         rowhead.createCell(111).setCellValue("INVENTORY_ACCT");
         rowhead.createCell(112).setCellValue("REORDER_LEVEL");
         rowhead.createCell(113).setCellValue("REQ_APPROVAL");
         rowhead.createCell(114).setCellValue("MAX_APPROVE_LEVEL");
         rowhead.createCell(115).setCellValue("FIFO");
         rowhead.createCell(116).setCellValue("MEASURING_CODE");
         rowhead.createCell(117).setCellValue("category_ID");
         rowhead.createCell(118).setCellValue("category_name");
         rowhead.createCell(119).setCellValue("category_acronym");
         rowhead.createCell(120).setCellValue("Required_for_fleet");
         rowhead.createCell(121).setCellValue("Asset_Ledger");
         rowhead.createCell(122).setCellValue("Dep_ledger");
         rowhead.createCell(123).setCellValue("Accum_Dep_ledger");
         rowhead.createCell(124).setCellValue("Category_Status");
         rowhead.createCell(125).setCellValue("create_date");

		 

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newTransaction = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			
    	    String assetId =  newTransaction.getAssetId();
			String registrationNo = newTransaction.getRegistrationNo();
			String branch_ID = newTransaction.getBranchId();
			String dept_ID = newTransaction.getDeptName();
			String category_ID = newTransaction.getInvoiceNo();
			String section_ID = newTransaction.getSectionName();
			String description = newTransaction.getDescription();
			String vendor_AC = newTransaction.getVendorAC();
			String date_Purchased = newTransaction.getDatepurchased();
			double depRate = newTransaction.getDepRate();
			String assetMake = newTransaction.getAssetMake();
			String assetModel = newTransaction.getAssetModel();
			String assetSerialNo = newTransaction.getAssetSerialNo();
			String assetEngineNo = newTransaction.getAssetEngineNo();
			String supplierName = newTransaction.getSupplierName();
			String assetUser = newTransaction.getAssetUser();
			String assetMaintenance = newTransaction.getAssetMaintenance();
			double accumDep = newTransaction.getAccumDep();
			double monthlyDep = newTransaction.getMonthlyDep();
			double costPrice = newTransaction.getCostPrice();
			double nbv = newTransaction.getNbv();
			String depEndDate = newTransaction.getDependDate();
			double residualValue = newTransaction.getDisposalAmount();
			String authorizedBy = newTransaction.getAuthorizedBy();
			String whTax = newTransaction.getWhTax();
			double whTaxAmount = newTransaction.getWhTaxValue();
			String reqDistribution = newTransaction.getOldSection();
			String postingDate = newTransaction.getPostingDate();
			String effectiveDate = newTransaction.getEffectiveDate();
			String purchaseReason = newTransaction.getPurchaseReason();
			int usefulLife = newTransaction.getUsefullife();
			String totalLife = newTransaction.getSupervisorId();
			String location = newTransaction.getLocation();
			int remainingLife = newTransaction.getRemainLife();
			double vatableCost = newTransaction.getPrevCostPrice();
			double vat = newTransaction.getVatValue();
			String reqDepreciation = newTransaction.getNewBranchCode();
			String subjectToVat = newTransaction.getSubjectTOVat();
			String raiseEntry = newTransaction.getAirCraft();
			double depYtd = newTransaction.getPrevAccumDep();
			String section = newTransaction.getNewsbuCode();
			String assetStatus = newTransaction.getAssetStatus();
			String state = newTransaction.getState();
			String driver = newTransaction.getDriver();
			String spare1 = newTransaction.getSpare1();
			String spare2 = newTransaction.getSpare2();
			String dateDisposed = newTransaction.getDisposalDate();
			String province = newTransaction.getChargeYear();
			String multiple = newTransaction.getMultiple();
			String warStartDate = newTransaction.getAssetsighted();
			String warMonth = newTransaction.getProcessingDate();
			String warExpiryDate = newTransaction.getLegacyPostedDate();
			String lastDepDate = newTransaction.getAssetfunction();
			String branch_Code = newTransaction.getBranchCode();
			String section_Code = newTransaction.getSectionCode();
			String deptCode = newTransaction.getDeptCode();
			String category_Code = newTransaction.getCategoryCode();
			double amtPtd = newTransaction.getTotalCost();
			double amountRem = newTransaction.getProfitAmount();
			String partPay = newTransaction.getNewAssetId();
			String fullPay = newTransaction.getWipGenerators();
			String group_ID = newTransaction.getOldDeptCode();
			String barCode = newTransaction.getBarCode();
			String sbuCode = newTransaction.getSbuCode();
			String lpo = newTransaction.getLpo();
			String supervisor = newTransaction.getSupervisor();
			String deferPay = newTransaction.getResponse();
			String oldAssetId = newTransaction.getOldassetId();
			double whtPercent = newTransaction.getDisposalProceed();
			String postRejectReason = newTransaction.getWipOfficeEquipmentComputers();
			String finaclePostedDate = newTransaction.getTransDate();
			String itemType = newTransaction.getAssetType();
			String warehouseCode = newTransaction.getWipMotorVehicles();
			String subCategoryCode = newTransaction.getSubcategoryCode();
			String spare3 = newTransaction.getSpare3();
			String spare4 = newTransaction.getSpare4();
			String spare5 = newTransaction.getSpare5();
			String spare6 = newTransaction.getSpare6();
			String memo = newTransaction.getMemo();
			String memoValue = newTransaction.getMemovalue();
			String integrify = newTransaction.getIntegrifyId();
			double improvCost = newTransaction.getPrevIMPROVCostPrice();
			double improvAccumDep = newTransaction.getImprovaccumDep();
			double improvNbv = newTransaction.getPrevIMPROVNBV();
			double improvVatableCost = newTransaction.getImprovnbv();
			double totalNbv = newTransaction.getTotalnbv();
			String improvUsefulLife = newTransaction.getDisposeReason();
			int improvTotalLife = newTransaction.getCalcLifeSpan();
			int improvRemainLife = newTransaction.getImproveRemainLife();
			double improvMonthlyDep = newTransaction.getPrevMonthlyDep();
			int quantity = newTransaction.getNoofitems();
			String unitCode = newTransaction.getNewSectionCode();
			String assetCode = newTransaction.getAssetCode();
			String regionCode = newTransaction.getOldBranchCode();
			String zoneCode = newTransaction.getOldDeptId();
			String projectCode = newTransaction.getProjectCode();
			double unitPrice = newTransaction.getImprovcostPrice();
			String mtid = newTransaction.getOldsbuCode();
			String compCode = newTransaction.getOldSectionCode();
			String item_Code = newTransaction.getSystemIp();
			String status = newTransaction.getApprovalStatus();
			String itemTypeCode = newTransaction.getErrormessage();
			String taxCode = newTransaction.getAction();
			double minQty = newTransaction.getLifeSpan();
			double weightAvgCost = newTransaction.getNbvDifference();
			double standardCost = newTransaction.getPrevMonthlyDifference();
			double weight = newTransaction.getPrevNBV();
			String backOrderable = newTransaction.getInitiatorId();
			String user_ID = newTransaction.getUserID();
			String cogSold = newTransaction.getWipLand();
			String inventoryAcct = newTransaction.getWipComputerSoftware();
			String reorderLevel = newTransaction.getComments();
			String reqApproval = newTransaction.getGlAccount();
			String maxApprovalLevel = newTransaction.getQrCode();
			double fifo = newTransaction.getMonthlyDifference();
			String measuringCode = newTransaction.getWipGeneratorsHouse();
			String categoryID = newTransaction.getOldCategoryCode();
			String categoryName = newTransaction.getCategoryName();
			String categoryAcronym = newTransaction.getLand();
			String reqForFleet = newTransaction.getNewDeptCode();
			String assetLedger = newTransaction.getAssetLedger();
			String depLedger = newTransaction.getDepLedger();
			String accumDepLedger = newTransaction.getAccumDepLedger();
			String categoryStatus = newTransaction.getOldBranchId();
			String createDate = newTransaction.getOldAssetUser();
			
			
			
			
		
			Row row = sheet.createRow((int) i);

			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
			row.createCell((short) 2).setCellValue(registrationNo);
			row.createCell((short) 3).setCellValue(branch_ID);
			row.createCell((short) 4).setCellValue(dept_ID);
			row.createCell((short) 5).setCellValue(category_ID);
			row.createCell((short) 6).setCellValue(section_ID);
			row.createCell((short) 7).setCellValue(description);
			row.createCell((short) 8).setCellValue(vendor_AC);
			row.createCell((short) 9).setCellValue(date_Purchased);
			row.createCell((short) 10).setCellValue(depRate);
			row.createCell((short) 11).setCellValue(assetMake);
			row.createCell((short) 12).setCellValue(assetModel);
			row.createCell((short) 13).setCellValue(assetSerialNo);
			row.createCell((short) 14).setCellValue(assetEngineNo);
			row.createCell((short) 15).setCellValue(supplierName);
			row.createCell((short) 16).setCellValue(assetUser);
			row.createCell((short) 17).setCellValue(assetMaintenance);
			row.createCell((short) 18).setCellValue(accumDep);
			row.createCell((short) 19).setCellValue(monthlyDep);
			row.createCell((short) 20).setCellValue(costPrice);
			row.createCell((short) 21).setCellValue(nbv);
			row.createCell((short) 22).setCellValue(depEndDate);
			row.createCell((short) 23).setCellValue(residualValue);
			row.createCell((short) 24).setCellValue(authorizedBy);
			row.createCell((short) 25).setCellValue(whTax);
			row.createCell((short) 26).setCellValue(whTaxAmount);
			row.createCell((short) 27).setCellValue(reqDistribution);
			row.createCell((short) 28).setCellValue(postingDate);
			row.createCell((short) 29).setCellValue(effectiveDate);
			row.createCell((short) 30).setCellValue(purchaseReason);
			row.createCell((short) 31).setCellValue(usefulLife);
			row.createCell((short) 32).setCellValue(totalLife);
			row.createCell((short) 33).setCellValue(location);
			row.createCell((short) 34).setCellValue(remainingLife);
			row.createCell((short) 35).setCellValue(vatableCost);
			row.createCell((short) 36).setCellValue(vat);
			row.createCell((short) 37).setCellValue(reqDepreciation);
			row.createCell((short) 38).setCellValue(subjectToVat);
			row.createCell((short) 39).setCellValue(raiseEntry);
			row.createCell((short) 40).setCellValue(depYtd);
			row.createCell((short) 41).setCellValue(section);
			row.createCell((short) 42).setCellValue(assetStatus);
			row.createCell((short) 43).setCellValue(state);
			row.createCell((short) 44).setCellValue(driver);
			row.createCell((short) 45).setCellValue(spare1);
			row.createCell((short) 46).setCellValue(spare2);
			row.createCell((short) 47).setCellValue(dateDisposed);
			row.createCell((short) 48).setCellValue(province);
			row.createCell((short) 49).setCellValue(multiple);
			row.createCell((short) 50).setCellValue(warStartDate);
			row.createCell((short) 51).setCellValue(warMonth);
			row.createCell((short) 52).setCellValue(warExpiryDate);
			row.createCell((short) 53).setCellValue(lastDepDate);
			row.createCell((short) 54).setCellValue(branch_Code);
			row.createCell((short) 55).setCellValue(section_Code);
			row.createCell((short) 56).setCellValue(deptCode);
			row.createCell((short) 57).setCellValue(category_Code);
			row.createCell((short) 58).setCellValue(amtPtd);
			row.createCell((short) 59).setCellValue(amountRem);
			row.createCell((short) 60).setCellValue(partPay);
			row.createCell((short) 61).setCellValue(fullPay);
			row.createCell((short) 62).setCellValue(group_ID);
			row.createCell((short) 63).setCellValue(barCode);
			row.createCell((short) 64).setCellValue(sbuCode);
			row.createCell((short) 65).setCellValue(lpo);
			row.createCell((short) 66).setCellValue(supervisor);
			row.createCell((short) 67).setCellValue(deferPay);
			row.createCell((short) 68).setCellValue(oldAssetId);
			row.createCell((short) 69).setCellValue(whtPercent);
			row.createCell((short) 70).setCellValue(postRejectReason);
			row.createCell((short) 71).setCellValue(finaclePostedDate);
			row.createCell((short) 72).setCellValue(itemType);
			row.createCell((short) 73).setCellValue(warehouseCode);
			row.createCell((short) 74).setCellValue(subCategoryCode);
			row.createCell((short) 75).setCellValue(spare3);
			row.createCell((short) 76).setCellValue(spare4);
			row.createCell((short) 77).setCellValue(spare5);
			row.createCell((short) 78).setCellValue(spare6);
			row.createCell((short) 79).setCellValue(memo);
			row.createCell((short) 80).setCellValue(memoValue);
			row.createCell((short) 81).setCellValue(integrify);
			row.createCell((short) 82).setCellValue(improvCost);
			row.createCell((short) 83).setCellValue(improvAccumDep);
			row.createCell((short) 84).setCellValue(improvNbv);
			row.createCell((short) 85).setCellValue(improvVatableCost);
			row.createCell((short) 86).setCellValue(totalNbv);
			row.createCell((short) 87).setCellValue(improvUsefulLife);
			row.createCell((short) 88).setCellValue(improvTotalLife);
			row.createCell((short) 89).setCellValue(improvRemainLife);
			row.createCell((short) 90).setCellValue(improvMonthlyDep);
			row.createCell((short) 91).setCellValue(quantity);
			row.createCell((short) 92).setCellValue(unitCode);
			row.createCell((short) 93).setCellValue(assetCode);
			row.createCell((short) 94).setCellValue(regionCode);
			row.createCell((short) 95).setCellValue(zoneCode);
			row.createCell((short) 96).setCellValue(projectCode);
			row.createCell((short) 97).setCellValue(unitPrice);
			row.createCell((short) 98).setCellValue(mtid);
			row.createCell((short) 99).setCellValue(compCode);
			row.createCell((short) 100).setCellValue(item_Code);
			row.createCell((short) 101).setCellValue(status);
			row.createCell((short) 102).setCellValue(itemTypeCode);
			row.createCell((short) 103).setCellValue(taxCode);
			row.createCell((short) 104).setCellValue(minQty);
			row.createCell((short) 105).setCellValue(weightAvgCost);
			row.createCell((short) 106).setCellValue(standardCost);
			row.createCell((short) 107).setCellValue(weight);
			row.createCell((short) 108).setCellValue(backOrderable);
			row.createCell((short) 109).setCellValue(user_ID);
			row.createCell((short) 110).setCellValue(cogSold);
			row.createCell((short) 111).setCellValue(inventoryAcct);
			row.createCell((short) 112).setCellValue(reorderLevel);
			row.createCell((short) 113).setCellValue(reqApproval);
			row.createCell((short) 114).setCellValue(maxApprovalLevel);
			row.createCell((short) 115).setCellValue(fifo);
			row.createCell((short) 116).setCellValue(measuringCode);
			row.createCell((short) 117).setCellValue(categoryID);
			row.createCell((short) 118).setCellValue(categoryName);
			row.createCell((short) 119).setCellValue(categoryAcronym);
			row.createCell((short) 120).setCellValue(reqForFleet);
			row.createCell((short) 121).setCellValue(assetLedger);
			row.createCell((short) 122).setCellValue(depLedger);
			row.createCell((short) 123).setCellValue(accumDepLedger);
			row.createCell((short) 124).setCellValue(categoryStatus);
			row.createCell((short) 125).setCellValue(createDate);

  

            i++;

     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     }
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