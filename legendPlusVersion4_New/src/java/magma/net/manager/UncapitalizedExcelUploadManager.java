package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.legend.bus.ApprovalRecords;

import magma.net.dao.MagmaDBConnection;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class UncapitalizedExcelUploadManager extends MagmaDBConnection
{
	SimpleDateFormat sdf;
    private ArrayList assetList;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    private MagmaDBConnection dbConnection;
    public ApprovalRecords approve;
    com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
    String userid;
    String excelType;
    String groupID;
    
    public UncapitalizedExcelUploadManager(String uid, String groupAssetByAsset,String groupId)
    {
        userid = uid;
        groupID = groupId;
        excelType = groupAssetByAsset;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        dbConnection = new MagmaDBConnection();
        approve = new ApprovalRecords(); 
        //System.out.println("INFO:Excel UncapitalizedExcelUploadManager instatiated.");
    }

    public ArrayList getAssetList()
    {
        return assetList;
    }

    public void acceptExcel(String fileObj)
    {
        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet sheets[] = (Sheet[])null;
        assetList = new ArrayList();
        try
        {
            Workbook workbook = Workbook.getWorkbook(new File(fileObj));
            sheets = workbook.getSheets();
            performReading(sheets);
        }
        catch(Exception io)
        {
            io.printStackTrace();
            System.out.println((new StringBuilder("WARN: Error uploading file ->")).append(io.getMessage()).toString());
        }
    }

    public void acceptExcel(File file)
    {
        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet sheets[] = (Sheet[])null;
        assetList = new ArrayList();
        if(assetList.isEmpty())
        {
            try
            {
                Workbook workbook = Workbook.getWorkbook(file);
                sheets = workbook.getSheets();
                performReading(sheets);
            }
            catch(Exception io)
            {
                io.printStackTrace();
                System.out.println((new StringBuilder("WARN: Error uploading file ->")).append(io.toString()).toString());
            }
        }
    }

    public ArrayList getFileFromServer(File uploadedFile)
    {
        acceptExcel(uploadedFile);
        return getAssetList();
    }

    private void performReading(Sheet sheets[])
    {
        //System.out.println((new StringBuilder("TOTAL SHEETS FOUND IS:")).append(sheets.length).append(" sheets").toString());
        boolean sucessful = true;
        for(int index = 0; index < sheets.length; index++)
        {
            System.out.print((new StringBuilder("Sheet:")).append(index).toString());
            int rows = sheets[index].getRows();
            int cols = sheets[index].getColumns();
            int counter = 0;
            for(int x = 1; x < rows; x++)
            {
                Cell cell[] = sheets[index].getRow(x);
                String cellTest = cell[0].getContents();
                if(cellTest != null && !cellTest.equalsIgnoreCase(""))
                {
                    insertCellContents(cell);
                }
            }

        }

    }

    private void insertCellContents(Cell cell[])
    {
        int[] statusList = new int[15];
        int size = cell.length;
        ExcelAssetBean asset = null;
        boolean bad = false;
        String empty = "";
        String group_Id = groupID;
        String branch_code = "";
        String department_code = "";
        String category_code = "";
        String subcategory_code = "";
        String depreciation_start_date = null;
        String depreciation_end_date = null;
        String description = "";
        String vendor_account = "";
        String vatable_cost = "0";
        String vat_amount = "0";
        String serial_number = "";
        String subject_to_vat = "N";
        String date_of_purchase = null;
        String registration_no = "";
        String require_depreciation = "";
        String section_code = "";
        String user_id = userid;
        String residualValue = "";
        String wh_tax_cb = "N";
        String wh_tax_amount = "0";
        String wh_taxPercent = "0";
        String vatPercent = "0";
        double vatrate = 0.0;
        double whtrate = 0.0;
        String wh_tax = "";
        String serial_no = "";
        String invoice_No = "";
        String lpo_no = "";
        String bar_code = "";
        String Vendor_Account ="";
        String sbu_code = "";
        String asset_user = "";
        String spare1 = "";
        String spare2 = "";
        String spare3 = "";
        String spare4 = "";
        String spare5 = "";
        String spare6 = "";
        String model = "";
        String make = "";
        String vendor = "";
        String assetMaintainBy = "";
        String purchaseReason = "";
        String location = "";   
        String projectCode = "";
        String errorMessage = "";
        String sNo = "";
        String regionCode = "";
        String zoneCode = "";
        String vendorName = "";
        int sheetno = 0;        
        boolean status = true;
        boolean status0 = true;
        boolean status1 = true;
        double cost = 0.00;
        try
        {
        	if(excelType.equalsIgnoreCase("Y")){
            sNo = cell[0].getContents();
            branch_code = cell[1].getContents();
            department_code = cell[2].getContents();
            section_code = cell[3].getContents();
            category_code = cell[4].getContents();
            subcategory_code = cell[5].getContents();
            sbu_code = cell[6].getContents();
            description = cell[7].getContents();
            vatable_cost = cell[8].getContents();             
            date_of_purchase = cell[9].getContents();        
            depreciation_start_date = cell[10].getContents();          
            wh_tax = cell[11].getContents();    
            wh_taxPercent = cell[12].getContents();  
            subject_to_vat = cell[13].getContents();  
            vatPercent = cell[14].getContents();      
            require_depreciation = cell[15].getContents();         
            registration_no = cell[16].getContents();    
            serial_no = cell[17].getContents();      
            lpo_no = cell[18].getContents();         
            bar_code = cell[19].getContents();
            invoice_No = cell[20].getContents();
            Vendor_Account = cell[21].getContents();
            asset_user = cell[22].getContents();
            spare1 = cell[23].getContents();
            spare2 = cell[24].getContents();
            spare3 = cell[25].getContents();
            spare4 = cell[26].getContents();
            spare5 = cell[27].getContents();
            spare6 = cell[28].getContents();
            model = cell[29].getContents();
            make = cell[30].getContents();
            vendor = cell[31].getContents();
            assetMaintainBy = cell[32].getContents();
            purchaseReason = cell[33].getContents();
            location = cell[34].getContents();
            projectCode = cell[35].getContents();
            regionCode = cell[36].getContents();
            zoneCode = cell[37].getContents();
			//System.out.println("<<<<<<branch_code: "+branch_code+"   sNo: "+sNo+"   model: "+model+"  make: "+make+"  vendor: "+vendor+"  assetMaintainBy: "+assetMaintainBy+"  purchaseReason: "+purchaseReason+"  location: "+location+"   projectCode: "+projectCode);
			sheetno = sheetno + 1;
        	   }
        	if(excelType.equalsIgnoreCase("N")){
                sNo = cell[0].getContents();
                branch_code = cell[1].getContents();
                department_code = cell[2].getContents();
                section_code = cell[3].getContents();
                category_code = cell[4].getContents();
 //               subcategory_code = cell[5].getContents();
                description = cell[5].getContents();
                vatable_cost = cell[6].getContents();             
                date_of_purchase = cell[7].getContents();        
                depreciation_start_date = cell[7].getContents();            
                require_depreciation = "N";            
                lpo_no = cell[9].getContents();         
                bar_code = cell[10].getContents();
                invoice_No = cell[11].getContents();
                Vendor_Account = cell[12].getContents();
                asset_user = cell[13].getContents();
                make = "0";
                location = "0";
                vendor = cell[14].getContents();
                assetMaintainBy = vendor;
                purchaseReason = cell[15].getContents();
                vendorName = cell[16].getContents();
    			//System.out.println("<<<<<<branch_code: "+branch_code+"   sNo: "+sNo+"   model: "+model+"  make: "+make+"  vendor: "+vendor+"  assetMaintainBy: "+assetMaintainBy+"  purchaseReason: "+purchaseReason+"  location: "+location+"   projectCode: "+projectCode);
    			sheetno = sheetno + 1;
            	   }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
        //vendor = approve.getCodeName("select Vendor_Id from am_ad_vendor where account_number = '"+Vendor_Account+"'");
        System.out.println("<<<<<<<vendor: "+vendor);
        //assetMaintainBy = vendor;
        String threshold = approve.getCodeName("select Cost_Threshold from am_gb_company");
        String invnumb = vendor+'-'+invoice_No;
        System.out.println("<<<<<<<invnumb: "+invnumb);
		
        if(!sNo.equalsIgnoreCase("S/No")){
        	if(excelType.equalsIgnoreCase("Y")){
        status = true;        	
        String error = "";
//        System.out.println("<<<<<<Inside S/No Loop>>>>>>>");
        String branchcodeValid = approve.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branch_code+"'");
        if(branchcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[0] = '0';}
        else{error = "pass; ";status = true; statusList[0] = '1';}
        errorMessage = "Record "+sNo+" branch_code "+error;
        String deptcodeValid = approve.getCodeName("select DEPT_NAME from am_ad_department where DEPT_CODE = '"+department_code+"'");
        if(deptcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[1] = '0';}
        else{error = "pass; ";status = true; statusList[1] = '1';}
        errorMessage = errorMessage+" department_code "+error;
        String sectcodeValid = approve.getCodeName("select SECTION_NAME from am_ad_section where SECTION_CODE = '"+section_code+"'");
        if(sectcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[2] = '0';}
        else{error = "pass; ";status = true; statusList[2] = '1';}
        errorMessage = errorMessage+" section_code "+error;
        String catcodeValid = approve.getCodeName("select CATEGORY_NAME from am_ad_category where CATEGORY_CODE = '"+category_code+"' AND category_type = 'U'");
        if(catcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[3] = '0';}
        else{error = "pass; ";status = true; statusList[3] = '1';}
        errorMessage = errorMessage+" category_code "+error;
        String subcatcodeValid = approve.getCodeName("select SUB_CATEGORY_NAME from am_ad_sub_category where SUB_CATEGORY_CODE = '"+subcategory_code+"'");
        if(subcatcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[4] = '0';}
        else{error = "pass; ";status = true; statusList[4] = '1';}
        errorMessage = errorMessage+" sub_category_code "+error;
        String tresh_hold = approve.getCodeName("select Cost_Threshold from am_gb_company");
 //       System.out.println("<<<<vatable_cost: "+vatable_cost+"  totalcost: "+(Double.parseDouble(vatable_cost)*1.05)+"    Double.parseDouble(tresh_hold): "+Double.parseDouble(tresh_hold));
        if((Double.parseDouble(vatable_cost)*1.05) > Double.parseDouble(tresh_hold) && subject_to_vat =="Y" ){error = " "+"fail; ";
        status = false; statusList[5] = '0';}
        else{error = "pass; ";status = true; statusList[5] = '1';}
        errorMessage = errorMessage+" Cost after adding vat "+error;        
        String sbucodeValid = approve.getCodeName("select SBU_NAME from Sbu_SetUp where SBU_CODE = '"+sbu_code+"'");
        if(sbucodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[6] = '0';}
        else{error = "pass; ";status = true; statusList[6] = '1';}
        errorMessage = errorMessage+" sbu_code "+error;
        String projectcodeValid = approve.getCodeName("select DESCRIPTION from ST_GL_PROJECT where CODE = '"+projectCode+"'");
        if(projectcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[7] = '0';}
        else{error = "pass; ";status = true; statusList[7] = '1';}
        errorMessage = errorMessage+" projectCode "+error;
        String vendorcodeValid = approve.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_CODE = '"+vendor+"'");
        if(vendorcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[8] = '0';}
        else{error = "pass; ";status = true; statusList[8] = '1';}
        errorMessage = errorMessage+" vendor "+error;
        String locationcodeValid = approve.getCodeName("select LOCATION from AM_GB_LOCATION where LOCATION_CODE = '"+location+"'");
        if(locationcodeValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[9] = '0';}
        else{error = "pass; ";status = true; statusList[9] = '1';}
        errorMessage = errorMessage+" Location "+error;
        String regioncodeValid = approve.getCodeName("select REGION_NAME from am_ad_region where REGION_CODE = '"+regionCode+"'");
        if(regioncodeValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[10] = '0';}
        else{error = "pass; ";status = true; statusList[10] = '1';}
        errorMessage = errorMessage+" RegionCode "+error;
        String zonecodeValid = approve.getCodeName("select ZONE_NAME from am_ad_Zone where ZONE_CODE = '"+zoneCode+"'");
        if(zonecodeValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[11] = '0';}
        else{error = "pass; ";status = true; statusList[11] = '1';}        
        errorMessage = errorMessage+" ZoneCode "+error;
        vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);
        
        String invoiceNumValid = approve.getCodeName("select INVOICE_NO from AM_INVOICE_NO where INVOICE_NO = '"+invnumb+"'");
        if(!invoiceNumValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[12] = '0';}
	  else{error = "pass; ";status = true; statusList[12] = '1';}  
        errorMessage = errorMessage+" Invoice "+error;
        
        
       // if(invoiceNumValid.equalsIgnoreCase("")) {htmlUtil.insToAm_Invoice_No(group_Id,lpo_no,invnumb,"Uncap Asset Creation Upload");}
        
        if(subject_to_vat.equalsIgnoreCase("Y"))
        {
			 vatrate =  Double.parseDouble(vatPercent)/100;
			 vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);
         //   vat_amount = String.valueOf(ftm.getVatAmount(Double.parseDouble(vatable_cost)));
        } else
        {
            vat_amount = "0.00";
        }
        if(!wh_tax.equalsIgnoreCase(""))
        {
        	whtrate =  Double.parseDouble(wh_taxPercent)/100;
        	wh_tax_amount = String.valueOf(Double.parseDouble(vatable_cost)*whtrate);
//                 wh_tax_amount = String.valueOf(ftm.getWhtAmount(Double.parseDouble(vatable_cost)));
        } else
        {
            wh_tax_amount = "0.00";
        }
        // double cost = Double.parseDouble(wh_tax_amount) + Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
        cost = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
        
        double totalcost = Double.parseDouble(vatable_cost)+(Double.parseDouble(vatable_cost)*vatrate);
        if(cost>Double.parseDouble(threshold)-0.01){error = " "+"fail; "; 
        status = false; statusList[12] = '0';}
        else{error = "pass; ";status = true; statusList[12] = '1';}        
        errorMessage = errorMessage+" Cost Price Above ThresHold "+error;
//        System.out.println("<<<<errorMessage: "+errorMessage+"  status: "+status);
        for (int i = 0; i < 13; ++i) {
        	if(statusList[i] == '0'){status0 = false;}else{status1 = true;}        	
        }
        if(!status0 || !status1){status = false;}
        if(status0 && status1){status = true;}
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
        	   } 
        if(excelType.equalsIgnoreCase("N")){
        status = true;        	
        String error = "";
//        System.out.println("<<<<<<Inside S/No Loop>>>>>>>");
        String branchcodeValid = approve.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branch_code+"'");
        if(branchcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[0] = '0';}
        else{error = "pass; ";status = true; statusList[0] = '1';}
        errorMessage = "Record "+sNo+" branch_code "+error;
        String deptcodeValid = approve.getCodeName("select DEPT_NAME from am_ad_department where DEPT_CODE = '"+department_code+"'");
        if(deptcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[1] = '0';}
        else{error = "pass; ";status = true; statusList[1] = '1';}
        errorMessage = errorMessage+" department_code "+error;
        String sectcodeValid = approve.getCodeName("select SECTION_NAME from am_ad_section where SECTION_CODE = '"+section_code+"'");
        if(sectcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[2] = '0';}
        else{error = "pass; ";status = true; statusList[2] = '1';}
        errorMessage = errorMessage+" section_code "+error;
        String catcodeValid = approve.getCodeName("select CATEGORY_NAME from am_ad_category where CATEGORY_CODE = '"+category_code+"' AND category_type = 'U'");
        if(catcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[3] = '0';}
        else{error = "pass; ";status = true; statusList[3] = '1';}
        errorMessage = errorMessage+" category_code "+error;
//        String subcatcodeValid = approve.getCodeName("select SUB_CATEGORY_NAME from am_ad_sub_category where SUB_CATEGORY_CODE = '"+subcategory_code+"'");
//        if(subcatcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
//        status = false; statusList[4] = '0';}
//        else{error = "pass; ";status = true; statusList[4] = '1';}
//        errorMessage = errorMessage+" sub_category_code "+error;
        String tresh_hold = approve.getCodeName("select Cost_Threshold from am_gb_company");
 //       System.out.println("<<<<vatable_cost: "+vatable_cost+"  totalcost: "+(Double.parseDouble(vatable_cost)*1.05)+"    Double.parseDouble(tresh_hold): "+Double.parseDouble(tresh_hold));
        if((Double.parseDouble(vatable_cost)*1.05) > Double.parseDouble(tresh_hold) && subject_to_vat =="Y" ){error = " "+"fail; ";
        status = false; statusList[4] = '0';}
        else{error = "pass; ";status = true; statusList[4] = '1';}
        errorMessage = errorMessage+" Cost after adding vat "+error;        
        String vendorcodeValid = approve.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_CODE = '"+vendor+"'");
        if(vendorcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[5] = '0';}
        else{error = "pass; ";status = true; statusList[5] = '1';}
        errorMessage = errorMessage+" vendor "+error;
//        vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);

        // double cost = Double.parseDouble(wh_tax_amount) + Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
//        cost = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
        cost = Double.parseDouble(vatable_cost);
//        double totalcost = Double.parseDouble(vatable_cost)+(Double.parseDouble(vatable_cost)*vatrate);
        double totalcost = Double.parseDouble(vatable_cost);
        if(cost>Double.parseDouble(threshold)-0.01){error = " "+"fail; "; 
        status = false; statusList[6] = '0';}
        else{error = "pass; ";status = true; statusList[6] = '1';}        
        errorMessage = errorMessage+" Cost Price Above ThresHold "+error;
        
        String invoiceNumValid = approve.getCodeName("select INVOICE_NO from AM_INVOICE_NO where INVOICE_NO = '"+invnumb+"'");
        if(!invoiceNumValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[7] = '0';}
        else{error = "pass; ";status = true; statusList[7] = '1';}
        errorMessage = errorMessage+" Invoice "+error;
        
       // if(invoiceNumValid.equalsIgnoreCase("")) {htmlUtil.insToAm_Invoice_No(group_Id,lpo_no,invnumb,"Uncap Asset Creation Upload");}

//        System.out.println("<<<<errorMessage: "+errorMessage+"  status: "+status);
        for (int i = 0; i < 13; ++i) {
        	if(statusList[i] == '0'){status0 = false;}else{status1 = true;}        	
        }
        if(!status0 || !status1){status = false;}
        if(status0 && status1){status = true;}
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
        	   } 
         }        
        if(status && !branch_code.equals(empty)&&(!sNo.equalsIgnoreCase("S/No")))
        {
            try
            {
                String branchId = "0";
                String deptId = "0";
                String sectionId = "0";
                String cateId = "0";
                String subcateId = "0";
                String depreciation_rate = "0";
                Branch branch = admin.getBranchByBranchCode(branch_code);
                if(branch != null)
                {
                    branchId = branch.getBranchId();
                } else
                {
                    bad = true;
                }
                Department dept = admin.getDeptByDeptCode(department_code);
                if(dept != null)
                {
                    deptId = dept.getDept_id();
                } else
                if(department_code != "0")
                {
                    bad = true;
                }
                Section sect = admin.getSectionByCode(section_code);
                if(sect != null)
                {
                    sectionId = sect.getSection_id();
                } else
                if(section_code != "0")
                {
                    bad = true;
                }
                Category cat = admin.getCategoryByCode(category_code);
                if(cat != null)
                {
                    cateId = cat.getCategoryId();
                    depreciation_rate = cat.getDepRate();
                    residualValue = cat.getResidualValue();
                } else
                {
                    bad = true;
                }
                SubCategory subcat = admin.getSubCategoryByCode(subcategory_code);
                if(subcat != null)
                {
                    subcateId = subcat.getAssetSubCategoryId();
                } else
                {
                    bad = true;
                }   
                if(excelType.equalsIgnoreCase("Y")){
                if(subject_to_vat.equalsIgnoreCase("Y"))
                {
					 vatrate =  Double.parseDouble(vatPercent)/100;
					 vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);
                 //   vat_amount = String.valueOf(ftm.getVatAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    vat_amount = "0.00";
                }
                if(!wh_tax.equalsIgnoreCase(""))
                {
                	whtrate =  Double.parseDouble(wh_taxPercent)/100;
                	wh_tax_amount = String.valueOf(Double.parseDouble(vatable_cost)*whtrate);
   //                 wh_tax_amount = String.valueOf(ftm.getWhtAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    wh_tax_amount = "0.00";
                }
                }
                asset = new ExcelAssetBean();
                asset.setBranch_id(branchId);
                asset.setCategory_id(cateId);
                asset.setSubCategory_id(subcateId);
                asset.setVatable_cost(vatable_cost);
                asset.setDate_of_purchase(date_of_purchase);
                asset.setDepartment_id(deptId);
                asset.setDepreciation_end_date(depreciation_end_date);
              //  asset.setDepreciation_rate(depreciation_rate);
                asset.setDepreciation_start_date(depreciation_start_date);
                asset.setDescription(description);
                asset.setRegistration_no(registration_no);
                asset.setRequire_depreciation(require_depreciation);
                asset.setSection_id(sectionId);
                asset.setSerial_number(serial_number);
                asset.setSubject_to_vat(subject_to_vat);
                asset.setUser_id(user_id);
                asset.setVat_amount(vat_amount);
                asset.setVatable_cost(vatable_cost);
                asset.setWh_tax_amount(wh_tax_amount);
                asset.setWh_tax_cb(wh_tax_cb);
                asset.setResidual_value(residualValue);
                asset.setCost_price(String.valueOf(cost));
                asset.setGid("1");
                asset.setBranch_code(branch_code);
                asset.setDepartment_code(department_code);
                asset.setCategory_code(category_code);
                asset.setSubCategory_code(subcategory_code);
                asset.setSection_code(section_code);
                asset.setSerial_no(serial_no);
                asset.setInvoice_No(invnumb);
                asset.setLpo_no(lpo_no);
                asset.setBar_code(bar_code); 
                asset.setWh_tax(wh_tax);
                asset.setVendor_account(Vendor_Account);
                asset.setSbu_code(sbu_code);
                asset.setAssetuser(asset_user);
                asset.setSpare_1(spare1);
                asset.setSpare_2(spare2);
                asset.setSpare_3(spare3);
                asset.setSpare_4(spare4);
                asset.setSpare_5(spare5);
                asset.setSpare_6(spare6);
                asset.setModel(model);
                asset.setMake(make);
                asset.setSupplied_by(vendor);
                asset.setMaintained_by(assetMaintainBy);
                asset.setPurchaseReason(purchaseReason);
                asset.setProjectCode(projectCode);
                asset.setRegionCode(regionCode);
                asset.setZoneCode(zoneCode);
                asset.setVendorName(vendorName);
                location = approve.getCodeName("select LOCATION_ID from AM_GB_LOCATION where LOCATION_CODE = '"+location+"'");
                if(location.equalsIgnoreCase("")){location = "0"; }
                asset.setLocation(location);                
                addCellContent(asset,group_Id);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addCellContent(ExcelAssetBean asset,String groupId)
    {
        int staux = 0;
        try
        {
            staux = asset.insertGroupUncapitalizedAssetRecordUpload(groupId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
        if(staux == 1 || staux == 2)
        {
            assetList.add(asset);
        }
    }

    public static String getDepEndDate(String vals)
    {
        if(vals != null)
        {
            StringTokenizer st1 = new StringTokenizer(vals, ",");
            if(st1.countTokens() == 2)
            {
                String s1 = st1.nextToken();
                String s2 = st1.nextToken();
                Float rate = Float.valueOf(Float.parseFloat(s1));
                s2 = s2.replaceAll("-", "/");
                StringTokenizer st2 = new StringTokenizer(s2, "/");
                if(st2.countTokens() == 3)
                {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();
                    if(year.length() == 4 && day.length() > 0 && day.length() < 3 && month.length() > 0 && month.length() < 3)
                    {
                        Calendar c = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                        int months = (int)((100F / rate.floatValue()) * 12F);
                        c.add(2, months);
                        c.add(6, -1);
                        int endDay = c.get(5);
                        int endMonth = c.get(2) + 1;
                        int endYear = c.get(1);
                        return (new StringBuilder(String.valueOf(endDay))).append("/").append(endMonth).append("/").append(endYear).toString();
                    }
                }
            }
        }
        return "Error";
    }

    public boolean insertErrorTransaction(String errorMessge,String userId) {
  	  boolean done=true;

  		   Connection con = null; 
  	  PreparedStatement ps = null;
  	  String transtype = "Asset Creation";
  	  String query2 = "INSERT INTO [am_uploadCheckErrorLog](USER_ID,ERRORDESCRIPTION,TRANSACTION_TYPE,ERRORDATE)" +
                " VALUES('"+userId+"','"+errorMessge+"','"+transtype+"','"+dbConnection.getDateTime(new java.util.Date())+"')";
//  	  System.out.println("<<<<<query: "+query2);
  	  try {
  	      con = dbConnection.getConnection("legendPlus");
  	      ps = con.prepareStatement(query2);      
  	      ps.execute();

  	  }
  	  catch (Exception ex)
  	  {
  		   done = false;
  	      System.out.println("WARNING:cannot insert error_transaction table in insertErrorTransaction->" );
  	      ex.printStackTrace();
  	  } finally {
  	      closeConnect(con, ps);
  	  }
  	  return done;
  	}

    public void closeConnect(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING:Error closing Connection ->" +
                               e.getMessage());
        }

    }  
    
}
