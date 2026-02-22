package magma;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.SendMail;
import com.magbel.util.ApplicationHelper; 
import com.magbel.util.DataConnect;

import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetHistoryManager;
import magma.net.manager.AssetPaymentManager;

import com.magbel.util.DatetimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import magma.util.Codes;
import magma.net.vao.ComponentViewDetail;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.Transaction;
import magma.net.manager.ComponentManager;
import legend.admin.objects.RaisedTransaction;
import legend.admin.objects.User;
/*
//
 THESE CLASSESS ARE NEEDED TO MAKE PURCHASEORDER METHODS BELOW WORK
//import magma.net.vao.PurchaseOrder;
//import magma.net.vao.PurchaseOrderDeliveryItem;
//import magma.net.vao.PurchaseOrderItem;


*/


//import magma.net.dao.MagmaDBConnection;
//import magma.net.manager.FleetHistoryManager;

//import magma.util.Codes;



  

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
   @author Modified by Lekan Matanmi


 * @version 1.0
 */
public class AssetRecordsBean extends legend.ConnectionClass 
{ 

    private String branch_id = "";
    private String branch_Code = "";
    private String department_id = "";
    private String department_code = "";
    private String category_id = "";
    private String sub_category_id = "";
    private String integrifyId = "";
    private String depreciation_start_date = null;
    private String depreciation_end_date = null;
    private String posting_date = null;
    private String make = "";
    private String location = "";
    private String maintained_by = "";
    private String authorized_by = "";
    private String supplied_by = "";
    private String reason = "";
    private String asset_id = "";
    private String description = "";
    private String vendor_account = "";
    private String cost_price = "0";
    private String vatable_cost = "0";
    private String vat_amount = "0";
    private String transport_cost = "0";
    private String other_cost = "0";
    private String serial_number = "";
    private String engine_number = "";
    private String model = "";
    private String user = "";
    private String depreciation_rate = "100";
    private String residual_value = "0.00";
    private String subject_to_vat = "";
    private String date_of_purchase = null;
    private String registration_no = "";
    private String require_depreciation = "";
    private String who_to_remind = "";
    private String email_1 = "";
    private String email2 = ""; 
    private String raise_entry = "N";
    private String section = "";
    private String user_id = "1";
    private String state = "";
    private String driver = "";
    private String who_to_remind_2 = "";
    private String spare_1 = "";
    private String spare_2 = "";
    private String spare_3 = "";
    private String spare_4 = "";
    private String spare_5 = "";
    private String spare_6 = "";
    private double accum_dep = 0.0d;
    private String section_id = "";
    private String wh_tax_cb = "N";
    private String wh_tax_amount = "0";
    private String require_redistribution = "";
    private String status = "PENDING";
    private String province = "";
    private String multiple = "N";
    private String warrantyStartDate = "";
    private String noOfMonths = "";
    private String expiryDate = "";
    private String amountPTD;
    private String amountREM;
    private String partPAY="N";
    private String fullyPAID="N";
    private String group_id;
    private MagmaDBConnection dbConnection;
    private String auth_user;
    private String authuser;
    private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    private Codes code;
    private String bar_code="";
    private String sbu_code ="";
    private String lpo ="";
    private String supervisor ="";
    private String deferPay ="N";
    private String selectTax = "0.00";
    private String systemIp;
    private String macAddress;
    private int assetCode=0;
	public ApprovalRecords approvalRec;
    private String memoValue="";
    private String memo=""; 
    private String newasset_id = "";
    private String newbranch_id = "";
    private String newdepartment_id = "";
    private String newsbu_code ="";
    private String newsection_id = "";
    private String newuser = "";
    private String nbv = "0";
    private String monthlydep = "0";
    private String accumdep = "0";
    private String batchId ="";
    private String improveCost = "0";
    private String improveNbv = "0";
    private String improveMonthlydep = "0";
    private String improveTotalNbv = "0";
    private String improveAccumdep = "0";
    private int quantity=0;
    private String warehouseCode = "";
    private String itemCode = "";
    private String itemType = "";
    private String projectCode = "";
    private String comments = "";
    private String assetsighted = "";
    private String assetfunction = "";
    private String existBranchCode;
    private String qty;
    private String regionCode = "";
    private String old_asset_id = "";
    private String vendorCode = "";
    private int id = 0;
    private String vatRate = "0.0";
    private String whtRate = "0.0";
    private String vendorName = "";
    
    ComponentManager comp;
 
    public AssetRecordsBean() throws Exception {
        approvalRec = new ApprovalRecords(); 
        //super();
        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            code = new Codes();
            
            comp = new ComponentManager();
        } catch (Exception ex) {
        }
    }   
 
    /**
     * insertAssetRecord
     *
     * @return boolean
     */
    private boolean rinsertAssetRecord(String branch,String param) throws Exception, Throwable {
    	
        System.out.println("=====>>>>branch_id: "+branch_id+"  ======>>>branch: "+branch);
		if (branch_id == null || branch_id.equals("") || branch_id.equals("0")) {
			branch_id = branch;
		}
        asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
                department_id, section_id, category_id);
       
        boolean done = true;
AssetPaymentManager payment = null;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }

        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (sub_category_id == null || sub_category_id.equals("")) {
            sub_category_id = "0";
        }

        if (integrifyId == null || integrifyId.equals("")) {
        	integrifyId = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }

        if (memo == null || memo.equals("")) {
        	memo = "N";
		}

        if (memoValue == null || memoValue.equals("")) {
        	memoValue = "0";
		}

        if (vendorName == null || vendorName.equals("")) {
        	vendorName = "";
        }
        
    	int memoValueS = Integer.parseInt(memoValue);
    	cost_price = cost_price.replaceAll(",", "");
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");
        assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
        System.out.println("<<<<<<rinsertAssetRecord assetCode: "+assetCode+"    selectTax: "+selectTax+"    integrifyId: "+integrifyId);
        String createQuery = "INSERT INTO AM_ASSET         " +
                             "(" +
                             "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                             "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                             "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                             "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                             "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                             "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                             "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                             "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                             "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                             "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                             "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
                             "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                             "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                             "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                             "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent," +
                             "system_ip,mac_address,asset_code,memo,memoValue, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE, " +
                             "WAREHOUSE_CODE,ITEMTYPE, ITEM_CODE, PROJECT_CODE,REGION_CODE,INTEGRIFY,TRANPORT_COST,OTHER_COST,VENDOR_NAME)  "+
                             "VALUES" +
                             "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                             "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
        if (this.chkidExists(asset_id)) {
            done = false;
            return done;
        }  
        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(createQuery)) {
//            double costPrice = Double.parseDouble(vat_amount) +
//                               Double.parseDouble(vatable_cost);
            double costPrice = Double.parseDouble(cost_price);
            ps.setString(1, asset_id);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description.toUpperCase());
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, costPrice);
            ps.setDouble(21, (costPrice-Double.parseDouble(residual_value)));
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, raise_entry);
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, new Integer(noOfMonths).intValue());
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57, code.getBranchCode(branch_id));
            ps.setString(58, code.getDeptCode(department_id));
            ps.setString(59, code.getSectionCode(section_id));
            ps.setString(60, code.getCategoryCode(category_id));
            ps.setDouble(61, Double.parseDouble(amountPTD));
            //ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
            //Use Vatable_cost instead of costPrice for Manage Asset Payment.====11/08/2009 ayojava
            ps.setDouble(62, (Double.parseDouble(cost_price)));
            ps.setString(63, partPAY);
            ps.setString(64, fullyPAID);
            ps.setString(65, bar_code);
            ps.setString(66,sbu_code);
            ps.setString(67, lpo);
            ps.setString(68,getSupervisor());
            ps.setString(69, deferPay);
            ps.setDouble(70, Double.parseDouble(selectTax));
            ps.setString(71, getSystemIp());
            ps.setString(72,getMacAddress());
            ps.setInt(73,assetCode);
            ps.setString(74, memo); 
            ps.setString(75, memoValue);
            ps.setString(76, spare_3);
            ps.setString(77, spare_4);
            ps.setString(78, spare_5);  
            ps.setString(79, spare_6);
            ps.setInt(80, Integer.parseInt(sub_category_id));
            ps.setString(81, code.getSubCategoryCode(sub_category_id));
            ps.setString(82, warehouseCode);
            ps.setString(83, itemType);
            ps.setString(84, itemCode);
            ps.setString(85, projectCode);
            ps.setString(86, regionCode);
            ps.setString(87, integrifyId);
            ps.setDouble(88, Double.parseDouble(transport_cost));
            ps.setDouble(89, Double.parseDouble(other_cost));
            ps.setString(90, vendorName);
  //          System.out.println("<<<<<warehouseCode in rinsertAssetRecord: "+warehouseCode+"    itemType: "+itemType);
            ps.execute();  
            
            FleetHistoryManager fm = new FleetHistoryManager();
            if (fm.isRequiredForFleet(asset_id))
            {
                /*
                 *Copy asset data to Fleet Master
                 */
                fm.copyAssetDataToFleet(asset_id);
            }
            
            rinsertAssetRecord(asset_id);
        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error creating asset in AM_ASSET Table->" + ex);
        } 

        return done;
    }

    /**
     * insertAssetRecord
     *
     * @return boolean
     */
    private boolean rinsertAssetTwoRecord() throws Exception, Throwable {
        asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
                department_id, section_id, category_id);
        
        boolean done = true;
AssetPaymentManager payment = null;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (sub_category_id == null || sub_category_id.equals("")) {
            sub_category_id = "0";
        }
        
        if (integrifyId == null) {
        	integrifyId = "";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }

        if (memo == null || memo.equals("")) {
        	memo = "N";
		}

        if (memoValue == null || memoValue.equals("")) {
        	memoValue = "0";
		}

    	int memoValueS = Integer.parseInt(memoValue);
    	cost_price = cost_price.replaceAll(",", "");
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");
        assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
       // System.out.println("<<<<<<rinsertAssetRecord assetCode: "+assetCode);
        String createQuery = "INSERT INTO AM_ASSET2         " +
                             "(" +
                             "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                             "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                             "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                             "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                             "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                             "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                             "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                             "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                             "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                             "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                             "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
                             "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                             "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                             "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                             "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent," +
                             "system_ip,mac_address,asset_code,memo,memoValue, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE, " +
                             "WAREHOUSE_CODE,ITEMTYPE, ITEM_CODE, PROJECT_CODE,REGION_CODE,INTEGRIFY,VENDOR_NAME)  "+
                             "VALUES" +
                             "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                             "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */
        if (this.chkidExists(asset_id)) {
            done = false;
            return done;
        }  
        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(createQuery)) {

//            double costPrice = Double.parseDouble(vat_amount) +
//                               Double.parseDouble(vatable_cost);
            double costPrice = Double.parseDouble(cost_price);
           
            ps.setString(1, asset_id);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description.toUpperCase());
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, costPrice);
            ps.setDouble(21, (costPrice-10.00));
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, raise_entry);
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, new Integer(noOfMonths).intValue());
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57, code.getBranchCode(branch_id));
            ps.setString(58, code.getDeptCode(department_id));
            ps.setString(59, code.getSectionCode(section_id));
            ps.setString(60, code.getCategoryCode(category_id));
            ps.setDouble(61, Double.parseDouble(amountPTD));
            //ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
            //Use Vatable_cost instead of costPrice for Manage Asset Payment.====11/08/2009 ayojava
            ps.setDouble(62, (Double.parseDouble(vatable_cost)));
            ps.setString(63, partPAY);
            ps.setString(64, fullyPAID);
            ps.setString(65, bar_code);
            ps.setString(66,sbu_code);
            ps.setString(67, lpo);
            ps.setString(68,getSupervisor());
            ps.setString(69, deferPay);
            ps.setDouble(70, Double.parseDouble(selectTax));
            ps.setString(71, getSystemIp());
            ps.setString(72,getMacAddress());
            ps.setInt(73,assetCode);
            ps.setString(74, memo); 
            ps.setString(75, memoValue);
            ps.setString(76, spare_3);
            ps.setString(77, spare_4);
            ps.setString(78, spare_5);  
            ps.setString(79, spare_6);
            ps.setInt(80, Integer.parseInt(sub_category_id));
            ps.setString(81, code.getSubCategoryCode(sub_category_id));
            ps.setString(82, warehouseCode);
            ps.setString(83, itemType);
            ps.setString(84, itemCode);
            ps.setString(85, projectCode);
            ps.setString(86, regionCode);
            ps.setString(87, integrifyId);
            ps.setString(88, vendorName);
  //          System.out.println("<<<<<warehouseCode in rinsertAssetRecord: "+warehouseCode+"    itemType: "+itemType);
            ps.execute();  
            
            FleetHistoryManager fm = new FleetHistoryManager();
            if (fm.isRequiredForFleet(asset_id))
            {
                /*
                 *Copy asset data to Fleet Master
                 */
                fm.copyAssetDataToFleet(asset_id);
            }
            
            rinsertAssetTwoRecord(asset_id);
        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error creating asset in AM_ASSET TWO Table->" + ex);
        } 
        
        return done;
    }


    /**
     * updateAssetRecord
     *
     * @return boolean
     */
    public boolean updateAssetRecord() throws Exception {

        String query = "update am_asset SET  " +
                       "REGISTRATION_NO=?,DESCRIPTION=?,VENDOR_AC=?," +
                       "DATE_PURCHASED=?,ASSET_MAKE=?,ASSET_MODEL=?," +
                       "ASSET_SERIAL_NO=?,ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                       "ASSET_USER=?,ASSET_MAINTENANCE=?,DEP_END_DATE=?," +

                       "RESIDUAL_VALUE=?,AUTHORIZED_BY=?,POSTING_DATE=?," +
                       "EFFECTIVE_DATE=?,PURCHASE_REASON=?,LOCATION=?," +
                       "WHO_TO_REM = ?,EMAIL1 = ?,WHO_TO_REM_2 = ?," +
                       "EMAIL2 = ?,RAISE_ENTRY = ?,SECTION=?,STATE=?,DRIVER = ?," +

                       "SPARE_1 = ?,SPARE_2 = ?,ASSET_STATUS = ?,PROVINCE=? , " +
                       "DEPT_ID = ?,req_redistribution = ?,Req_Depreciation = ?,  " +
                       "SUBJECT_TO_VAT=?,VATABLE_COST=?,VAT=? ,    " +
                       "wh_tax = ?,wh_tax_amount  = ?,BRANCH_ID  = ?,MULTIPLE = ?,COST_PRICE=?,    " +
                       "BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? ,CATEGORY_CODE=?,WAR_START_DATE=?," +
                       "WAR_MONTH=?,WAR_EXPIRY_DATE=?, BAR_CODE =?,SBU_CODE = ?, LPO = ?, supervisor=?, defer_pay=?,wht_percent=?,memo=?,memoValue=?, " +
                       "SPARE_3 = ?,SPARE_4 = ?,SPARE_5 = ?,SPARE_6 = ?,sub_category_code = ?,CATEGORY_ID=? ,SUB_CATEGORY_ID=?,  "+
                       "WAREHOUSE_CODE = ?,ITEMTYPE = ?,ITEM_CODE = ?,REGION_CODE = ?, TRANPORT_COST = ?,OTHER_COST = ?  "+
                       "WHERE	ASSET_ID = '" + asset_id + "'";

        
        boolean done = true;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")|| driver.equals("-1")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

    
        if (bar_code == null || bar_code.equals("")) {
            bar_code = "0";
        }

        if (sbu_code == null || sbu_code.equals("")) {
            sbu_code = "0";
        }

        if (lpo == null || lpo.equals("")) {
            lpo = "0";
        }
        if (memo == null || memo.equals("")) {
        	memo = "N";
		}

        if (memoValue == null || memoValue.equals("")) {
        	memoValue = "0";
		}
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");

        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query)) {

            double costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);

           
            ps.setString(1, registration_no);
            ps.setString(2, description.toUpperCase());
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(5, Integer.parseInt(make));
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setString(9, supplied_by);
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(13, Double.parseDouble(residual_value));
            ps.setString(14, authorized_by);
            ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
            ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(17, reason);
            ps.setInt(18, Integer.parseInt(location));
            ps.setString(19, who_to_remind);
            ps.setString(20, email_1);
            ps.setString(21, who_to_remind_2);
            ps.setString(22, email2);
            ps.setString(23, raise_entry);
            ps.setString(24, section);
            ps.setInt(25, Integer.parseInt(state));
            ps.setInt(26, Integer.parseInt(driver));
            ps.setString(27, spare_1);
            ps.setString(28, spare_2);
            ps.setString(29, status);
            ps.setString(30, province);
            ps.setInt(31, Integer.parseInt(department_id));
            ps.setString(32, require_redistribution);
            ps.setString(33, require_depreciation);
            ps.setString(34, subject_to_vat);
            ps.setDouble(35, Double.parseDouble(vatable_cost));
            ps.setDouble(36, Double.parseDouble(vat_amount));
            ps.setString(37, wh_tax_cb);
            ps.setDouble(38, Double.parseDouble(wh_tax_amount));
            ps.setInt(39, Integer.parseInt(branch_id));
            ps.setString(40, multiple);
            ps.setDouble(41, costPrice);
            ps.setString(42, code.getBranchCode(branch_id));
            ps.setString(43, code.getDeptCode(department_id));
            ps.setString(44, code.getSectionCode(section_id));
            ps.setString(45, code.getCategoryCode(category_id));
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49, bar_code);
            ps.setString(50,sbu_code);
            ps.setString(51, lpo);
            ps.setString(52,getSupervisor());
            ps.setString(53,deferPay);
//            System.out.println("<<<<<<selectTax: "+selectTax);
            ps.setDouble(54, Double.parseDouble(selectTax));
            ps.setString(55,memo);
            ps.setString(56,memoValue); 
            ps.setString(57, spare_3);
            ps.setString(58, spare_4);
            ps.setString(59, spare_5);
            ps.setString(60, spare_6);
            ps.setString(61, code.getSubCategoryCode(sub_category_id));
            ps.setInt(62, Integer.parseInt(category_id));
            ps.setInt(63, Integer.parseInt(sub_category_id));
            ps.setString(64, warehouseCode);
            ps.setString(65, itemType);
            ps.setString(66, itemCode);
            ps.setString(67, regionCode);
            ps.setDouble(68, Double.parseDouble(transport_cost));
            ps.setDouble(69, Double.parseDouble(other_cost));
            ps.execute();
            
        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error updating asset->" + ex);
        } 
        
        return done;

    }

    /**
     * updateAsset2Record
     *
     * @return boolean
     */
    public boolean updateAsset2Record() throws Exception {

        String query = "update am_asset2 SET  " +
                       "REGISTRATION_NO=?,DESCRIPTION=?,VENDOR_AC=?," +
                       "DATE_PURCHASED=?,ASSET_MAKE=?,ASSET_MODEL=?," +
                       "ASSET_SERIAL_NO=?,ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                       "ASSET_USER=?,ASSET_MAINTENANCE=?,DEP_END_DATE=?," +

                       "RESIDUAL_VALUE=?,AUTHORIZED_BY=?,POSTING_DATE=?," +
                       "EFFECTIVE_DATE=?,PURCHASE_REASON=?,LOCATION=?," +
                       "WHO_TO_REM = ?,EMAIL1 = ?,WHO_TO_REM_2 = ?," +
                       "EMAIL2 = ?,RAISE_ENTRY = ?,SECTION=?,STATE=?,DRIVER = ?," +

                       "SPARE_1 = ?,SPARE_2 = ?,ASSET_STATUS = ?,PROVINCE=? , " +
                       "DEPT_ID = ?,req_redistribution = ?,Req_Depreciation = ?,  " +
                       "SUBJECT_TO_VAT=?,VATABLE_COST=?,VAT=? ,    " +
                       "wh_tax = ?,wh_tax_amount  = ?,BRANCH_ID  = ?,MULTIPLE = ?,COST_PRICE=?,    " +
                       "BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? ,CATEGORY_CODE=?,WAR_START_DATE=?," +
                       "WAR_MONTH=?,WAR_EXPIRY_DATE=?, BAR_CODE =?,SBU_CODE = ?, LPO = ?, supervisor=?, defer_pay=?,wht_percent=?,memo=?,memoValue=?, " +
                       "SPARE_3 = ?,SPARE_4 = ?,SPARE_5 = ?,SPARE_6 = ?,sub_category_code = ?,CATEGORY_ID=? ,SUB_CATEGORY_ID=?  "+
                       "WAREHOUSE_CODE = ?,ITEMTYPE = ?,ITEMCODE = ?,REGION_CODE = ?   "+
                       "WHERE	ASSET_ID = '" + asset_id + "'";

       
        boolean done = true;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")|| driver.equals("-1")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

    
        if (bar_code == null || bar_code.equals("")) {
            bar_code = "0";
        }

        if (sbu_code == null || sbu_code.equals("")) {
            sbu_code = "0";
        }

        if (lpo == null || lpo.equals("")) {
            lpo = "0";
        }
        if (memo == null || memo.equals("")) {
        	memo = "N";
		}

        if (memoValue == null || memoValue.equals("")) {
        	memoValue = "0";
		}
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");

        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query)) {

            double costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);

           
            ps.setString(1, registration_no);
            ps.setString(2, description.toUpperCase());
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(5, Integer.parseInt(make));
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setString(9, supplied_by);
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(13, Double.parseDouble(residual_value));
            ps.setString(14, authorized_by);
            ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
            ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(17, reason);
            ps.setInt(18, Integer.parseInt(location));
            ps.setString(19, who_to_remind);
            ps.setString(20, email_1);
            ps.setString(21, who_to_remind_2);
            ps.setString(22, email2);
            ps.setString(23, raise_entry);
            ps.setString(24, section);
            ps.setInt(25, Integer.parseInt(state));
            ps.setInt(26, Integer.parseInt(driver));
            ps.setString(27, spare_1);
            ps.setString(28, spare_2);
            ps.setString(29, status);
            ps.setString(30, province);
            ps.setInt(31, Integer.parseInt(department_id));
            ps.setString(32, require_redistribution);
            ps.setString(33, require_depreciation);
            ps.setString(34, subject_to_vat);
            ps.setDouble(35, Double.parseDouble(vatable_cost));
            ps.setDouble(36, Double.parseDouble(vat_amount));
            ps.setString(37, wh_tax_cb);
            ps.setDouble(38, Double.parseDouble(wh_tax_amount));
            ps.setInt(39, Integer.parseInt(branch_id));
            ps.setString(40, multiple);
            ps.setDouble(41, costPrice);
            ps.setString(42, code.getBranchCode(branch_id));
            ps.setString(43, code.getDeptCode(department_id));
            ps.setString(44, code.getSectionCode(section_id));
            ps.setString(45, code.getCategoryCode(category_id));
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49, bar_code);
            ps.setString(50,sbu_code);
            ps.setString(51, lpo);
            ps.setString(52,getSupervisor());
            ps.setString(53,deferPay);
            ps.setDouble(54, Double.parseDouble(selectTax));
            ps.setString(55,memo);
            ps.setString(56,memoValue); 
            ps.setString(57, spare_3);
            ps.setString(58, spare_4);
            ps.setString(59, spare_5);
            ps.setString(60, spare_6);
            ps.setString(61, code.getSubCategoryCode(sub_category_id));
            ps.setInt(62, Integer.parseInt(category_id));
            ps.setInt(63, Integer.parseInt(sub_category_id));
            ps.setString(64, warehouseCode);
            ps.setString(65, itemType);
            ps.setString(66, itemCode);
            ps.setString(67, regionCode);
            ps.execute();
            
        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error updating asset->" + ex);
        } 
        
        return done;

    }


    /**
     * getAssetRecords
     */
    public void getAssetRecords() throws Exception {

        if (asset_id != "") {
            String query = "SELECT A.ASSET_ID,INTEGRIFY, A.REGISTRATION_NO," +
                           "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID,A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                           "A.VENDOR_AC, A.DATE_PURCHASED," +
                           "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                           "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                           "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                           "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                           "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                           "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                           "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                           "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                           "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                           "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                           "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                           "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code,A.memo,A.memovalue, " +
                           "A.IMPROV_NBV,A.IMPROV_ACCUMDEP,A.IMPROV_COST,A.IMPROV_MONTHLYDEP,A.TOTAL_NBV,TRANPORT_COST,OTHER_COST, " +
                           "A.WAREHOUSE_CODE, A.ITEM_CODE,A.ITEMTYPE,A.PROJECT_CODE,REGION_CODE,OLD_ASSET_ID,VENDOR_NAME FROM AM_ASSET A " +
                           "WHERE A.ASSET_ID = '" + asset_id + "'";
//			System.out.println("getAssetRecords query: "+query);
           

            try (Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(query)) {
            	
               
                try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {  

                    registration_no = rs.getString("REGISTRATION_NO");
                    branch_id = rs.getString("BRANCH_ID");
                    department_id = rs.getString("DEPT_ID");
                    category_id = rs.getString("CATEGORY_ID");
                    integrifyId = rs.getString("INTEGRIFY");
                    sub_category_id = rs.getString("SUB_CATEGORY_ID");
                    depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnection.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    make = rs.getString("ASSET_MAKE");
                    location = rs.getString("LOCATION");
                    maintained_by = rs.getString("ASSET_MAINTENANCE");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("SUPPLIER_NAME");
                    reason = rs.getString("PURCHASE_REASON");
                    asset_id = rs.getString("ASSET_ID");
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    nbv = rs.getString("NBV");
                    monthlydep = rs.getString("MONTHLY_DEP");
                    vatable_cost = rs.getString("VATABLE_COST");
                    transport_cost = rs.getString("TRANPORT_COST");
                    other_cost = rs.getString("OTHER_COST");
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("USER_ID");
                    depreciation_rate = rs.getString("DEP_RATE");
                    residual_value = rs.getString("RESIDUAL_VALUE");
                    require_depreciation = rs.getString("REQ_DEPRECIATION");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    date_of_purchase = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    who_to_remind = rs.getString("WHO_TO_REM");
                    email_1 = rs.getString("EMAIL1");
                    email2 = rs.getString("EMAIL2");
                    raise_entry = rs.getString("RAISE_ENTRY");
                    section = rs.getString("SECTION_ID");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    status = rs.getString("ASSET_STATUS");
                    section_id = rs.getString("SECTION");
                    wh_tax_cb = rs.getString("WH_TAX");
                    wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                    require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                    spare_1 = rs.getString("SPARE_1");
                    spare_2 = rs.getString("SPARE_2");
                    spare_3 = rs.getString("SPARE_3");
                    spare_4 = rs.getString("SPARE_4");
                    spare_5 = rs.getString("SPARE_5");
                    spare_6 = rs.getString("SPARE_6");
                    who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                    driver = rs.getString("DRIVER");
                    state = rs.getString("STATE");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    multiple = rs.getString("MULTIPLE");
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "POSTING_DATE"));
                    warrantyStartDate = dbConnection.formatDate(rs.getDate(
                            "WAR_START_DATE"));
                    noOfMonths = rs.getString("WAR_MONTH");
                    expiryDate = dbConnection.formatDate(rs.getDate(
                            "WAR_EXPIRY_DATE"));
                    authuser = rs.getString("ASSET_USER");
                    amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                    amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                    partPAY = rs.getString("PART_PAY");
                    fullyPAID =rs.getString("FULLY_PAID");
                    group_id =rs.getString("GROUP_ID");
                    bar_code = rs.getString("BAR_CODE");
                    sbu_code = rs.getString("SBU_CODE");
                    lpo = rs.getString("LPO");
                    setSupervisor(rs.getString("SUPERVISOR"));
                    deferPay = rs.getString("defer_pay");
                    selectTax = String.valueOf(rs.getDouble("wht_percent"));
                    this.section_id = rs.getString("SECTION_ID");
                    assetCode = rs.getInt("asset_code");
                    memo = rs.getString("memo");
                    memoValue = rs.getString("memoValue");
                    improveCost = rs.getString("IMPROV_COST");
                    improveNbv = rs.getString("IMPROV_NBV");
                    improveAccumdep = rs.getString("IMPROV_ACCUMDEP");
                    improveMonthlydep = rs.getString("IMPROV_MONTHLYDEP");
                    improveTotalNbv = rs.getString("TOTAL_NBV");
                    warehouseCode = rs.getString("WAREHOUSE_CODE");
                    itemCode = rs.getString("ITEM_CODE");
                    itemType = rs.getString("ITEMTYPE");
                    projectCode = rs.getString("PROJECT_CODE");
                    regionCode = rs.getString("REGION_CODE");
                    vendorName = rs.getString("VENDOR_NAME");
                    old_asset_id = rs.getString("OLD_ASSET_ID");
                    
                } else {
                    System.out.print("nothing");
                }
                }

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching all asset ->" + ex);
            } 
        }
    }

    /**
     * createAssetId
     *
     * @return String
     */
    private String createAssetId() {
        String id = "";
        try {
            id = new legend.ConnectionClass().getIdentity().substring(24);
        } catch (Exception er) {
            System.out.println("WARN: Error creating id->" + er);
        }
        return id;
    }

    /**
     * depreciationRate
     *
     * @return String
     */
    private String depreciationRate() {
        return "";
    }

    public void setBranch_id(String branch_id) {
        if (branch_id != null) {
            this.branch_id = branch_id;
        }
    }
    public void setBranch_Code(String branch_Code) {
        if (branch_Code != null) {
            this.branch_Code = branch_Code;
        }
    }
    public void setProjectCode(String projectCode) {
        if (projectCode != null) {
            this.projectCode = projectCode;
        }
    }    
    public void setRegionCode(String regionCode) {
        if (regionCode != null) {
            this.regionCode = regionCode;
        }
    }       
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        }
    } 
    public void setAssetsighted(String assetsighted) {
        if (assetsighted != null) {
            this.assetsighted = assetsighted;
        }
    } 
    public void setAssetfunction(String assetfunction) {
        if (assetfunction != null) {
            this.assetfunction = assetfunction;
        }
    } 
    public void setDepartment_id(String department_id) {
        if (department_id != null) {
            this.department_id = department_id;
        }
    }
    public void setDepartment_code(String department_code) {
        if (department_code != null) {
            this.department_code = department_code;
        }
    }
    public void setNewasset_id(String newasset_id) {
        if (newasset_id != null) {
            this.newasset_id = newasset_id;
        }
    }
    public void setNewbranch_id(String newbranch_id) {
        if (newbranch_id != null) {
            this.newbranch_id = newbranch_id;
        }
    }
    public void setNewdepartment_id(String newdepartment_id) {
        if (newdepartment_id != null) {
            this.newdepartment_id = newdepartment_id;
        }
    }
    
    public void setCategory_id(String category_id) throws Exception {
        if (category_id != null) {
            this.category_id = category_id;
            depreciation_rate = getDepreciationRate(category_id);
        }
    }
    
    public void setSub_category_id(String sub_category_id) throws Exception {
        if (sub_category_id != null) {
            this.sub_category_id = sub_category_id;
        }
    }

    
    public void setDepreciation_start_date(String depreciation_start_date) {
        this.depreciation_start_date = depreciation_start_date;
    }

    public void setDepreciation_end_date(String depreciation_end_date) {
        this.depreciation_end_date = depreciation_end_date;
    }

    public void setPosting_date(String posting_date) {
        this.depreciation_end_date = depreciation_end_date;
    }

    public void setMake(String make) {
        if (make != null) {
            this.make = make;
        }
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location;
        }
    }

    public void setMaintained_by(String maintained_by) {
        if (maintained_by != null) {
            this.maintained_by = maintained_by;
        }
    }

    public void setAuthorized_by(String authorized_by) {
        if (maintained_by != null) {
            this.authorized_by = authorized_by;
        }
    }

    public void setSupplied_by(String supplied_by) {
        if (supplied_by != null) {
            this.supplied_by = supplied_by;
        }
    }

    public void setReason(String reason) {
        if (reason != null) {
            this.reason = reason;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setOld_asset_id(String old_asset_id) {
        if (old_asset_id != null) {
            this.old_asset_id = old_asset_id;
        }
    }    

    public void setVendorCode(String vendorCode) {
        if (vendorCode != null) {
            this.vendorCode = vendorCode;
        }
    }      
    public void setBatchId(String batchId) {
        if (batchId != null) {
            this.batchId = batchId;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description.replaceAll("'", "");
        }
    }

    public void setVendor_account(String vendor_account) {
        if (vendor_account != null) {
            this.vendor_account = vendor_account;
        }
    }

    public void setCost_price(String cost_price) {
        if (cost_price != null) {
            this.cost_price = cost_price.replaceAll(",", "");
            ;
        }
    }
    public void setNbv(String nbv) {
        if (nbv != null) {
            this.nbv = nbv.replaceAll(",", "");
            ;
        }
    }
    public void setMonthlydep(String monthlydep) {
        if (monthlydep != null) {
            this.monthlydep = monthlydep.replaceAll(",", "");
            ;
        }
    }
    public void setAccumdep(String accumdep) {
        if (accumdep != null) {
            this.accumdep = accumdep.replaceAll(",", "");
            ;
        }
    }

    public void setVatable_cost(String vatable_cost) {
        if (vatable_cost != null) {
            this.vatable_cost = vatable_cost.replaceAll(",", "");
            ;
        }
    }

    public void setTransport_cost(String transport_cost) {
        if (transport_cost != null) {
            this.transport_cost = transport_cost.replaceAll(",", "");
            ;
        }
    }

    public void setOther_cost(String other_cost) {
        if (other_cost != null) {
            this.other_cost = other_cost.replaceAll(",", "");
            ;
        }
    }


    public void setVat_amount(String vat_amount) {
        if (vat_amount != null) {
            this.vat_amount = vat_amount.replaceAll(",", "");
            ;
        }
    }

    public void setSerial_number(String serial_number) {
        if (serial_number != null) {
            this.serial_number = serial_number;
        }
    }

    public void setEngine_number(String engine_number) {
        if (engine_number != null) {
            this.engine_number = engine_number;
        }
    }

    public void setModel(String model) {
        if (model != null) {
            this.model = model;
        }
    }

    public void setUser(String user) {
        if (user != null) {
            this.user = user;
        }
    }
    public void setNewuser(String newuser) {
        if (newuser != null) {
            this.newuser = newuser;
        }
    }
    public void setDepreciation_rate(String depreciation_rate) {
        if (depreciation_rate != null) {
            this.depreciation_rate = depreciation_rate;
        }
    }

    public void setResidual_value(String residual_value) {
        if (residual_value != null) {
            this.residual_value = residual_value;
        }
    }

    public void setSubject_to_vat(String subject_to_vat) {
        if (subject_to_vat != null) {
            this.subject_to_vat = subject_to_vat;
        }
    }

    public void setProvince(String province) {
        if (province != null) {
            this.province = province;
        }
    }

    public void setDate_of_purchase(String date_of_purchase) {
        this.date_of_purchase = date_of_purchase;
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setRequire_depreciation(String require_depreciation) {
        if (require_depreciation != null) {
            this.require_depreciation = require_depreciation;
        }
    }

    public void setWho_to_remind(String who_to_remind) {
        if (who_to_remind != null) {
            this.who_to_remind = who_to_remind;
        }
    }

    public void setEmail_1(String email_1) {
        if (email_1 != null) {
            this.email_1 = email_1;
        }
    }

    public void setEmail2(String email2) {
        if (email2 != null) {
            this.email2 = email2;
        }
    }

    public void setRaise_entry(String raise_entry) {
        if (raise_entry != null) {
            this.raise_entry = raise_entry;
        }
    }

    public void setSection(String section) {
        if (section != null) {
            this.section = section;
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setState(String state) {
        if (state != null) {
            this.state = state;
        }
    }

    public void setDriver(String driver) {
        if (driver != null) {
            this.driver = driver;
        }
    }

    public void setWho_to_remind_2(String who_to_remind_2) {
        if (who_to_remind_2 != null) {
            this.who_to_remind_2 = who_to_remind_2;
        }
    }

    public void setSpare_1(String spare_1) {
        if (spare_1 != null) {
            this.spare_1 = spare_1;
        }
    }

    public void setSpare_2(String spare_2) {
        if (spare_2 != null) {
            this.spare_2 = spare_2;
        }
    }

    public void setSpare_3(String spare_3) {
        if (spare_3 != null) {
            this.spare_3 = spare_3;
        }
    }

    public void setSpare_4(String spare_4) {
        if (spare_4 != null) {
            this.spare_4 = spare_4;
        }
    }

    public void setSpare_5(String spare_5) {
        if (spare_5 != null) {
            this.spare_5 = spare_5;
        }
    }

    public void setSpare_6(String spare_6) {
        if (spare_6 != null) {
            this.spare_6 = spare_6;
        }
    }
    
    public void setAccum_dep(String accum_dep) {
        if (accum_dep != null) {
            this.accum_dep = Double.parseDouble(accum_dep);
        }
    }

    public void setSection_id(String section_id) {
        if (section_id != null) {
            this.section_id = section_id;
        }
    }

    public void setItemType(String itemType) {
        if (itemType != null) {
            this.itemType = itemType;
        }
    }

    public void setItemCode(String itemCode) {
        if (itemCode != null) {
            this.itemCode = itemCode;
        }
    }

    public void setWarehouseCode(String warehouseCode) {
        if (warehouseCode != null) {
            this.warehouseCode = warehouseCode;
        }
    }
        
    public void setNewsection_id(String newsection_id) {
        if (newsection_id != null) {
            this.newsection_id = newsection_id;
        }
    }
    
    public void setWh_tax_cb(String wh_tax_cb) {
        if (wh_tax_cb != null) {
            this.wh_tax_cb = wh_tax_cb;
        }
    }

    public void setWh_tax_amount(String wh_tax_amount) {
        if (wh_tax_amount != null) {
            this.wh_tax_amount = wh_tax_amount.replaceAll(",", "");
            ;
        }
    }

    public void setRequire_redistribution(String require_redistribution) {
        if (require_redistribution != null) {
            this.require_redistribution = require_redistribution;
        }
    }

    public void setStatus(String status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setMultiple(String multiple) {
        if (multiple != null) {
            this.multiple = multiple;
        }
    }
    
    /**
     * @return the selectTax
     */
    public String getIntegrifyId() {

        return integrifyId;
    }

    /**
     * @param integrifyId the integrifyId to set
     */
    public void setIntegrifyId(String integrifyId) {
        this.integrifyId = integrifyId;
    }
    /**
     * @return the warrantyStartDate
     */ 
    public void setWarrantyStartDate(String warDate) {
        if (warrantyStartDate != null) {
            this.warrantyStartDate = warDate;
        }
    }

    public void setNoOfMonths(String months) {
        if (noOfMonths != null) {
            this.noOfMonths = months;
        }
    }

    public void setExpiryDate(String expiryDate) {
        if (expiryDate != null) {
            this.expiryDate = expiryDate;
        }
    }

    public void setAuth_user(String auth_user) {
        this.auth_user = auth_user;
    }

    public void setAuthuser(String authuser) {
        this.authuser = authuser;
    }

    public String getBranch_id() {
        return branch_id;
    }    

    public String getBranch_Code() {
        return branch_Code;
    }    
     
    public String getProjectCode() {
        return projectCode;
    }
    
   public String getRegionCode() {
       return regionCode;
   }
   
    public String getComments() { 
        return comments;
    }
    
    public String getAssetsighted() {
        return assetsighted;
    }
    
    public String getAssetfunction() {
        return assetfunction;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getDepartment_code() {
        return department_code;
    }
    
    public String getNewasset_id() {
        return newasset_id;
    }
    public String getNewbranch_id() {
        return newbranch_id;
    }
    public String getNewdepartment_id() {
        return newdepartment_id;
    }
    
    public String getCategory_id() {
        return category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public String getDepreciation_start_date() {
        return depreciation_start_date;
    }

    public String getDepreciation_end_date() {
        return depreciation_end_date;
    }

    public String getPosting_date() {
        return posting_date;
    }

    public String getMake() {
        return make;
    }

    public String getLocation() {
        return location;
    }

    public String getMaintained_by() {
        return maintained_by;
    }

    public String getAuthorized_by() {
        return authorized_by;
    }

    public String getSupplied_by() {
        return supplied_by;
    }

    public String getReason() {
        return reason;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getOld_asset_id() {
        return old_asset_id;
    }    

    public String getVendorCode() {
        return vendorCode;
    }    
     
    public String getBatchId() {
        return batchId;
    }
    public String getDescription() {
        return description;
    }

    public String getVendor_account() {
        return vendor_account;
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getNbv() {
        return nbv;
    }
    public String getMonthlydep() {
        return monthlydep;
    }
    public String getAccumdep() {
        return accumdep;
    }
    public String getVatable_cost() {
        return vatable_cost;
    }
    
    public String getTransport_cost() {
        return transport_cost;
    }
    
    public String getOther_cost() {
        return other_cost;
    }

    
    public String getVat_amount() {
        return vat_amount;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public String getModel() {
        return model;
    }

    public String getUser() {
        return user;
    }
    public String getNewuser() {
        return newuser;
    }
    public String getDepreciation_rate() {
        return depreciation_rate;
    }

    public String getResidual_value() {
        return residual_value;
    }

    public String getSubject_to_vat() {
        return subject_to_vat;
    }

    public String getDate_of_purchase() {
        return date_of_purchase;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getRequire_depreciation() {
        return require_depreciation;
    }

    public String getWho_to_remind() {
        return who_to_remind;
    }

    public String getEmail_1() {
        return email_1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getSection() {
        return section;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getState() {
        return state;
    }

    public String getDriver() {
        return driver;
    }

    public String getWho_to_remind_2() {
        return who_to_remind_2;
    }

    public String getSpare_1() {
        return spare_1;
    }

    public String getSpare_2() {
        return spare_2;
    }

    public String getSpare_3() {
        return spare_3;
    }

    public String getSpare_4() {
        return spare_4;
    }

    public String getSpare_5() {
        return spare_5;
    }

    public String getSpare_6() {
        return spare_6;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }
    
    public double getAccum_dep() {
        return accum_dep;
    }


    public String getSection_id() {
        return section_id;
    }
    public String getNewsection_id() {
        return newsection_id;
    }
    public String getWh_tax_cb() {
        return wh_tax_cb;
    }

    public String getWh_tax_amount() {
        return wh_tax_amount;
    }

    public String getRequire_redistribution() {
        return require_redistribution;
    }

    public String getStatus() {
        return status;
    }

    public String getProvince() {
        return province;
    }

    public String getMultiple() {
        return multiple;
    }

    public String getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public String getNoOfMonths() {
        return noOfMonths;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getAuth_user() {
        return auth_user;
    }

    public String getAuthuser() {
        return authuser;
    }
    public void setImproveCost(String improveCost) {
        if (improveCost != null) {
            this.improveCost = improveCost.replaceAll(",", "");
        }
        }
    public String getImproveCost() {
        return improveCost;
    }
    public void setImproveNbv(String improveNbv) {
        if (improveNbv != null) {
            this.improveNbv = improveNbv.replaceAll(",", "");
        }
        }
    public String getImproveNbv() {
        return improveNbv;
    }
    public void setImproveMonthlydep(String improveMonthlydep) {
        if (improveMonthlydep != null) {
            this.improveMonthlydep = improveMonthlydep.replaceAll(",", "");
        }
        }
    public String getImproveMonthlydep() {
        return improveMonthlydep;
    }
    public void setImproveTotalNbv(String improveTotalNbv) {
        if (improveTotalNbv != null) {
            this.improveTotalNbv = improveTotalNbv.replaceAll(",", "");
        }
        }
    public String getImproveTotalNbv() {
        return improveTotalNbv;
    }
    public void setImproveAccumdep(String improveAccumdep) {
        if (improveAccumdep != null) {
            this.improveAccumdep = improveAccumdep.replaceAll(",", "");
        }
        }
    public String getImproveAccumdep() {
        return improveAccumdep;
    }  
    
    public String getCost_Price() {
        return cost_price;
    }
    
    public String getExistBranchCode()
    {
        return existBranchCode;
    }
    public void setExistBranchCode(String existBranchCode)
    {
        this.existBranchCode = existBranchCode;
    }   
    
    public String getWhtRate()
    {
        return whtRate;
    }
    public void setWhtRate(String whtRate)
    {
        this.whtRate = whtRate;
    } 
    
    public String getVatRate()
    {
        return vatRate;
    }
    public void setVatRate(String vatRate)
    {
        this.vatRate = vatRate;
    }     
    
    public String getQty()
    {
        return qty;
    }
    public void setQty(String qty)
    {
        this.qty = qty;
    }    
    
    /**
     * @return the vendorName
     */
    public String getVendorName() {

        return vendorName;
    }

    /**
     * @param vendorName the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public String computeTotalLife(String depRate) {

        String totalLife = "0";
        if (depRate == null || depRate.equals("")) {
            depRate = "0.0";
        }

        double division = 100 / (Double.parseDouble(depRate));
        int intTotal = (int) (division * 12);

        totalLife = Integer.toString(intTotal);

        return totalLife;

    }

    /**
     * isMultipleComponent
     *
     * @param category_id String
     * @return boolean
     */
    public boolean isMultipleComponentOld(String category_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean multipleComponent = false;
        String query = "SELECT COUNT(AM_ID) FROM AM_CT_COMPONENT  " +
                       "WHERE CATEGORY = " + category_id;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int numRecords = rs.getInt(1);
                if (numRecords > 0) {
                    multipleComponent = true;
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error determining  MultipleComponent->" +
                               ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return multipleComponent;
    }

    public boolean isMultipleComponent(String categoryId) {
        boolean multipleComponent = false;
        String query = "SELECT COUNT(AM_ID) FROM AM_CT_COMPONENT WHERE CATEGORY = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    multipleComponent = rs.getInt(1) > 0;
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error determining multiple components -> " + ex.getMessage());
            ex.printStackTrace();
        }

        return multipleComponent;
    }
    
    
    /**
     * isDepreciationReCalculatable
     *
     * @param assetid String
     * @return boolean
     */
    public boolean isDepreciationReCalculatableOld(String assetid) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final double ZERO = 0.00d;

        boolean calculatable = false;
        String query = "SELECT ACCUM_DEP FROM AM_ASSET  " +
                       "WHERE ASSET_ID = ?";
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, assetid);
            rs = ps.executeQuery();

            while (rs.next()) {

                double accumDep = rs.getDouble(1);
                if (accumDep > ZERO) {
                    calculatable = true;
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error isDepreciationReCalculatable ->" +
                               ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return calculatable;
    }
    
    public boolean isDepreciationReCalculatable(String assetid) {

        boolean calculatable = false;
        final double ZERO = 0.00d;
        String query = "SELECT ACCUM_DEP FROM AM_ASSET WHERE ASSET_ID = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, assetid);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    double accumDep = rs.getDouble("ACCUM_DEP");

                    if (accumDep > ZERO) {
                        calculatable = true;
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error isDepreciationReCalculatable -> " + ex);
        }

        return calculatable;
    }

    /**
     * getDepreciationRate
     *
     * @param category_id String
     * @return String
     */
    private String getDepreciationRateOld(String category_id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rate = "0.0";
        String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_ID = " + category_id;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rate = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return rate;
    }
    
    public String getDepreciationRate(String category_id) {
    	 String rate = "0.0";
        String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY WHERE CATEGORY_ID = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	rate = rs.getString("DEP_RATE") ;
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate -> " + ex.getMessage());
            ex.printStackTrace();
        }

        return rate;
    }

    /**
     * residualValue
     *
     * @return String
     */
    public String findResidualValueOld() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String residual = "0.0";

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(
                    "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY");
            rs = ps.executeQuery();

            while (rs.next()) {
                residual = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return residual;
    }

    
    public String findResidualValue() {
        String residual = "0.0";
        String query = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                residual = rs.getString(1) != null ? rs.getString(1) : "0.0";
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching residual value -> " + ex.getMessage());
            ex.printStackTrace();
        }

        return residual;
    }

    public static String getDepreciationEndDate(String vals) {
        String endDate = "ERROR";


        String checkDepRate = vals.substring(0, vals.indexOf("-")-3);
      
//        System.out.println("the depre rate is ===================== "+ checkDepRate);
       if(checkDepRate != null && Double.parseDouble(checkDepRate)==0){
        try {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
Date date = new Date();
   endDate = sdf1.format(date);
//output = sdf.parse(sdf.format(date));
     System.out.println("the pre output is " + sdf1.toPattern());
}
        catch(Exception e){
            e.printStackTrace();
}



       }else{


        if (vals != null) {

            StringTokenizer st1 = new StringTokenizer(vals, ",");
            //if(){

            if (st1.countTokens() == 2) {

                String s1 = st1.nextToken();
                String s2 = st1.nextToken();

                Float rate = Float.parseFloat(s1);
                if (s2 != null) {
                    s2 = s2.replaceAll("/", "-");
                }

                StringTokenizer st2 = new StringTokenizer(s2, "-");
                if (st2.countTokens() == 3) {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();

                    if ((year.length() == 4) && (day.length() > 0) &&
                        (day.length() < 3) && (month.length() > 0) &&
                        (month.length() < 3)) {
                        Calendar c = new GregorianCalendar(Integer.parseInt(
                                year), Integer.parseInt(month) - 1,
                                Integer.parseInt(day) - 1);

                        int months = (int) (100 / rate * 12);
                        c.add(Calendar.MONTH, months);
                        //c.add(Calendar.DAY_OF_YEAR, -1 * Integer.parseInt(day));

                        int endDay = c.get(Calendar.DAY_OF_MONTH);
                        int endMonth = c.get(Calendar.MONTH) + 1;
                        int endYear = c.get(Calendar.YEAR);

                        endDate = endMonth + "-" + endDay + "-" + endYear;
                    }

                }
            }
        }
       }
        return endDate;
    }

    public boolean chkidExistsOld(String assetid) {
        boolean exists = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String SQL = "SELECT * FROM am_asset WHERE asset_id='" + assetid + "'";
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            exists = rs.next();

        } catch (Exception ex) {
            System.out.println("WARN: Error isDepreciationReCalculatable ->" +
                               ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return exists;

    }
    
    public boolean chkidExists(String assetid) {

        String sql = "SELECT 1 FROM am_asset WHERE asset_id = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, assetid);
            return ps.executeQuery().next();

        } catch (Exception e) {
            System.out.println("WARN: chkidExists -> " + e);
            return false;
        }
    }

    public int insertAssetRecord(String branch) throws Exception, Throwable {
        String[] budget = getBudgetInfo();
        double[] bugdetvalues = getBudgetValues();
        int DONE = 0; //everything is oK
        int BUDGETENFORCED = 1; //EF budget = Yes, CF = NO, ERROR_FLAG
        int BUDGETENFORCEDCF = 2; //EF budget = Yes, CF = Yes, ERROR_FLAG
        int ASSETPURCHASEBD = 3; //asset falls into no quarter purchase date older than bugdet
        String Q = getQuarter(); 
        System.out.println("<<<<<<===Q: "+Q+"    bugdetvalues[0]: "+bugdetvalues[0]+"     bugdetvalues[1]: "+ bugdetvalues[1]+"     bugdetvalues[2]: "+ bugdetvalues[2]+"     bugdetvalues[3]: "+ bugdetvalues[3]);
        System.out.println("<<<<<<===bugdetvalues[4]: "+bugdetvalues[4]+"    bugdetvalues[5]: "+bugdetvalues[5]+"     bugdetvalues[6]: "+ bugdetvalues[6]+"     bugdetvalues[7]: "+ bugdetvalues[7]+"     bugdetvalues[8]: "+ bugdetvalues[8]+"     bugdetvalues[9]: "+ bugdetvalues[9]);
        if(budget[3].equalsIgnoreCase("N")){
			rinsertAssetRecord(branch,"");
			return DONE;
		}
		else if(budget[3].equalsIgnoreCase("Y")){
        if (!Q.equalsIgnoreCase("NQ")) {
            if (budget[3].equalsIgnoreCase("Y") &&
                budget[4].equalsIgnoreCase("N")) {
                if (chkBudgetAllocation(Q, bugdetvalues, false)) {
                    updateBudget(Q, budget);
                    rinsertAssetRecord(branch,"");
                    return DONE;
                } else {
                    return BUDGETENFORCED;
                }

            } else if (budget[3].equalsIgnoreCase("Y") &&
                       budget[4].equalsIgnoreCase("Y")) {
                if (chkBudgetAllocation(Q, bugdetvalues, true)) {
                    updateBudget(Q, budget);
                    rinsertAssetRecord(branch,"");
                    return DONE;
                } else {
                    return BUDGETENFORCEDCF;
                }

            } else {
                rinsertAssetRecord(branch,"");
                return DONE;
            }
        } else {
            if (budget[3].equalsIgnoreCase("Y") &&
                    budget[4].equalsIgnoreCase("N")) {
                    if (chkBudgetAllocation(Q, bugdetvalues, false)) {
                        updateBudget(Q, budget);
                        rinsertAssetRecord(branch,"");
                        return DONE;
                    } else {
                        return BUDGETENFORCED;
                    }

                } 
 //           return ASSETPURCHASEBD;
        }}
        return 0;
    }

    public String[] getBudgetInfoOld() {
        String[] result = new String[5];

        String query = " SELECT financial_start_date,financial_no_ofmonths"
                       +
                       ",financial_end_date,enforce_acq_budget,quarterly_surplus_cf"
                       + " FROM am_gb_company";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = sdf.format(rs.getDate("financial_start_date"));
                result[1] = rs.getString("financial_no_ofmonths");
                result[2] = sdf.format(rs.getDate("financial_end_date"));
                result[3] = rs.getString("enforce_acq_budget");
                result[4] = rs.getString("quarterly_surplus_cf");

            }
        } catch (Exception e) {
            String warning = "WARNING:Error Fetching Company Details" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return result;
    }
    
    public String[] getBudgetInfo() {

        String[] result = new String[5];

        String query = "SELECT financial_start_date, financial_no_ofmonths, " +
                       "financial_end_date, enforce_acq_budget, quarterly_surplus_cf " +
                       "FROM am_gb_company";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                Date startDate = rs.getDate("financial_start_date");
                Date endDate = rs.getDate("financial_end_date");

                result[0] = (startDate != null) ? sdf.format(startDate) : null;
                result[1] = rs.getString("financial_no_ofmonths");
                result[2] = (endDate != null) ? sdf.format(endDate) : null;
                result[3] = rs.getString("enforce_acq_budget");
                result[4] = rs.getString("quarterly_surplus_cf");
            }

        } catch (Exception e) {
            System.out.println("WARNING: Error Fetching Company Details -> " + e.getMessage());
        }

        return result;
    }

    public double[] getBudgetValuesOld() {
        double[] result = new double[12];
	  	String qrylevel =" SELECT BRANCH_CODE FROM AM_AD_BRANCH WHERE BRANCH_ID = " + branch_id + " ";
  	    String branchCode = approvalRec.getCodeName(qrylevel);
  	    String subcategoryCode = approvalRec.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = " + sub_category_id + " "); 
        String query = " SELECT Q1_ALLOCATION,Q1_ACTUAL,Q2_ALLOCATION"
                       +
                       ",Q2_ACTUAL,Q3_ALLOCATION,Q3_ACTUAL,Q4_ALLOCATION,Q4_ACTUAL,BALANCE_ALLOCATION,TOTAL_ACTUAL"
                       + " FROM AM_ACQUISITION_BUDGET WHERE CATEGORY_CODE='" +
                       getCatCode() + "' AND "
                       + " BRANCH_ID='" + branchCode + "' AND SBU_CODE = '" + sbu_code + "' AND SUB_CATEGORY_CODE = '"+subcategoryCode+"'";
//        System.out.println("=====>>>>query in getBudgetValues: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null; 
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getDouble("Q1_ALLOCATION");
                result[1] = rs.getDouble("Q1_ACTUAL");
                result[2] = rs.getDouble("Q2_ALLOCATION");
                result[3] = rs.getDouble("Q2_ACTUAL");
                result[4] = rs.getDouble("Q3_ALLOCATION");
                result[5] = rs.getDouble("Q3_ACTUAL");
                result[6] = rs.getDouble("Q4_ALLOCATION");
                result[7] = rs.getDouble("Q4_ACTUAL");
                result[8] = rs.getDouble("BALANCE_ALLOCATION");
                result[9] = rs.getDouble("TOTAL_ACTUAL");
                //result[4] = rs.getDouble("quarterly_surplus_cf");


            }
        } catch (Exception e) {
            String warning = "WARNING:Error Fetching Company Details" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return result;
    }
    
    public double[] getBudgetValues() {

        double[] result = new double[10];

        String branchSql = "SELECT BRANCH_CODE FROM AM_AD_BRANCH WHERE BRANCH_ID = ?";
        String subCatSql = "SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = ?";
        
        String query = "SELECT Q1_ALLOCATION, Q1_ACTUAL, Q2_ALLOCATION, Q2_ACTUAL, " +
                       "Q3_ALLOCATION, Q3_ACTUAL, Q4_ALLOCATION, Q4_ACTUAL, " +
                       "BALANCE_ALLOCATION, TOTAL_ACTUAL " +
                       "FROM AM_ACQUISITION_BUDGET " +
                       "WHERE CATEGORY_CODE = ? " +
                       "AND BRANCH_ID = ? " +
                       "AND SBU_CODE = ? " +
                       "AND SUB_CATEGORY_CODE = ?";

        try (Connection con = dbConnection.getConnection("legendPlus")) {

            String branchCode;
            try (PreparedStatement ps1 = con.prepareStatement(branchSql)) {
                ps1.setString(1, branch_id);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    if (!rs1.next()) return result;
                    branchCode = rs1.getString(1);
                }
            }

            String subcategoryCode;
            try (PreparedStatement ps2 = con.prepareStatement(subCatSql)) {
                ps2.setString(1, sub_category_id);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (!rs2.next()) return result;
                    subcategoryCode = rs2.getString(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, getCatCode());
                ps.setString(2, branchCode);
                ps.setString(3, sbu_code);
                ps.setString(4, subcategoryCode);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        result[0] = rs.getDouble("Q1_ALLOCATION");
                        result[1] = rs.getDouble("Q1_ACTUAL");
                        result[2] = rs.getDouble("Q2_ALLOCATION");
                        result[3] = rs.getDouble("Q2_ACTUAL");
                        result[4] = rs.getDouble("Q3_ALLOCATION");
                        result[5] = rs.getDouble("Q3_ACTUAL");
                        result[6] = rs.getDouble("Q4_ALLOCATION");
                        result[7] = rs.getDouble("Q4_ACTUAL");
                        result[8] = rs.getDouble("BALANCE_ALLOCATION");
                        result[9] = rs.getDouble("TOTAL_ACTUAL");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("WARNING: Error Fetching Budget Details -> " + e.getMessage());
        }

        return result;
    }

    public String getQuarter() {
        System.out.println("getting quarters");
        String quarter = "NQ";
        String[] budg = getBudgetInfo();
        //System.out.println("fsdate  " + budg[0]);
        ///System.out.println("pdate  " + date_of_purchase);
        double q1 = (double) (Double.parseDouble(budg[1]) / 4);
        int month = (int) dateFormat.getDayDifference(date_of_purchase, budg[0]) /
                    30;
        //System.out.println("pdate  " + date_of_purchase);
        //System.out.println("financial start and pdate diff months " + month);
        boolean btw = dateFormat.isDateBetween(budg[0], budg[2],
                                               date_of_purchase);
        if (btw) {
            if ((double) month <= q1) {
                quarter = "FIRST";
               // System.out.println("1st Quarter");
            } else if ((double) month > q1 && (double) month <= (q1 * 2.0)) {
                quarter = "2ND";
                //System.out.println("2nd Quarter");
            } else if ((double) month > (q1 * 2.0) &&
                       (double) month <= (q1 * 3.0)) {
                quarter = "3RD";
                //System.out.println("3rd Quarter");
            } else if (month > (q1 * 3.0)) {
                quarter = "4TH";
                //System.out.println("4th Quarter");
            }

        }
        //System.out.println("the assets quarter is  " + quarter);
        return quarter;

    }

    public String getCatCodeOld() {

        String query =
                "SELECT CATEGORY_CODE  FROM am_ad_category  " +
                "WHERE category_id = '" + category_id + "' ";
        String catid = "0";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                catid = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return catid;

    }
    
    public String getCatCode() {

        String query = "SELECT CATEGORY_CODE FROM am_ad_category WHERE category_id = ?";
        String catCode = null;

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    catCode = rs.getString("CATEGORY_CODE");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return catCode;
    }

    public String getSubcatCodeOld() {

        String query =
                "SELECT SUB_CATEGORY_CODE  FROM am_ad_sub_category  " +
                "WHERE sub_category_id = '" + sub_category_id + "' ";
        String catid = "0";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                catid = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return catid;

    }
    
    public String getSubcatCode() {

        String query = "SELECT SUB_CATEGORY_CODE FROM am_ad_sub_category WHERE sub_category_id = ?";
        String subCatCode = null;

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, sub_category_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    subCatCode = rs.getString("SUB_CATEGORY_CODE");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return subCatCode;
    }

    public boolean chkBudgetAllocation(String quarter, double values[],
                                       boolean cf) {
        boolean allocation = true;
        double result = 0.00;
        if (cf) {
//            if (quarter.equalsIgnoreCase("FIRST")) {
//                result = values[0] -
//                         (values[1] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("2ND")) {
//                result = (values[0] + values[2]) -
//                         (values[1] + values[3] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("3RD")) {
//                result = (values[0] + values[2] + values[4]) -
//                         (values[1] + values[3] + values[5] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("4TH")) {
//                c = (values[0] + values[2] + values[4] + values[6]) -
//                         (values[1] + values[3] + values[5] + values[7] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            }
//
            result = (values[0] + values[2] + values[4]) -
                    (values[1] + values[3] + values[5] +
                     Double.parseDouble(vatable_cost.replaceAll(",", "")));
        	} 
          else {
//            if (quarter.equalsIgnoreCase("FIRST")) {
//                result = values[0] -
//                         (values[1] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("2ND")) {
//                result = values[2] -
//                         (values[3] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("3RD")) {
//                result = values[4] -
//                         (values[5] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            } else if (quarter.equalsIgnoreCase("4TH")) {
//                result = values[6] -
//                         (values[7] +
//                          Double.parseDouble(vatable_cost.replaceAll(",", "")));
//            }
//
            result = values[8] -
            (values[9] +
             Double.parseDouble(vatable_cost.replaceAll(",", "")));        	  
        }

        if (result < 0) {
            allocation = false;
        }
        return allocation;
    }

    public void updateBudgetOld(String quarter, String[] bugdetinfo) throws SQLException {

        String fisdate = "";
        int finomonth = 0;
        String fiedate = "";
        Connection conn = dbConnection.getConnection("legendPlus"); ;
        Statement stmt = null;
	  	String qrylevel =" SELECT BRANCH_CODE FROM AM_AD_BRANCH WHERE BRANCH_ID = " + branch_id + " ";
  	    String branchCode = approvalRec.getCodeName(qrylevel);
  	    String subCategoryCode = approvalRec.getCodeName(" SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = " + sub_category_id + " ");
  	  
        try {
            stmt = conn.createStatement();
            /*System.out.println("pdate  " + date_of_purchase);
            System.out.println(
                    "Commencing update of Aquicisition Budget due to Asset Creation");
            System.out.println(category_id);*/
            String old_category = getCatCode();
            if (quarter.equalsIgnoreCase("FIRST")) {
                String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                       +
                                       " SET TOTAL_ACTUAL = (COALESCE(TOTAL_ACTUAL,0) + " +
                                       cost_price.replaceAll(",", "") +
                                       "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+cost_price.replaceAll(",", "")+" WHERE BRANCH_ID='" +
                                       branchCode +
                                       "' AND CATEGORY_CODE='" + old_category +
                                       "' AND SBU_CODE = '" + sbu_code + "' AND ACC_START_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[0])
                                       + "' AND ACC_END_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[2]) +
                                       "'";
 //               System.out.println(budgetUpdate1);
                stmt.executeUpdate(budgetUpdate1);
 //               System.out.println("Updated 1st Quarter");
            } else if (quarter.equalsIgnoreCase("2ND")) {
                String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                       +
                                       " SET Q2_ACTUAL = (COALESCE(Q2_ACTUAL,0) + " +
                                       cost_price.replaceAll(",", "") +
                                       "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+cost_price.replaceAll(",", "")+" WHERE BRANCH_ID='" +
                                       branchCode +
                                       "' AND CATEGORY_CODE='" + old_category +
                                       "' AND SBU_CODE = '" + sbu_code + "' AND ACC_START_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[0])
                                       + "' AND ACC_END_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[2]) +
                                       "'";

//                System.out.println(budgetUpdate1);

                stmt.executeUpdate(budgetUpdate1);
 //               System.out.println("Updated 2nd Quarter");
            } else if (quarter.equalsIgnoreCase("3RD")) {
                String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                       +
                                       " SET Q3_ACTUAL =(COALESCE(Q3_ACTUAL,0) + " +
                                       cost_price.replaceAll(",", "") +
                                       "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+cost_price.replaceAll(",", "")+" WHERE BRANCH_ID='" +
                                       branchCode +
                                       "' AND CATEGORY_CODE='" + old_category +
                                       "' AND SBU_CODE = '" + sbu_code + "' AND ACC_START_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[0])
                                       + "' AND ACC_END_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[2]) +
                                       "'";

//                System.out.println(budgetUpdate1);

                stmt.executeUpdate(budgetUpdate1);

 //               System.out.println("Updated 3rd Quarter");
            } else if (quarter.equalsIgnoreCase("4TH")) {
                String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                                       +
                                       " SET Q4_ACTUAL = (COALESCE(Q4_ACTUAL,0) + " +
                                       cost_price.replaceAll(",", "") +
                                       "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+cost_price.replaceAll(",", "")+"  WHERE BRANCH_ID='" +
                                       branchCode +
                                       "' AND CATEGORY_CODE='" + old_category +
                                       "' AND SBU_CODE = '" + sbu_code + "' AND ACC_START_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[0])
                                       + "' AND ACC_END_DATE='" +
                                       dateFormat.dateConvert(bugdetinfo[2]) +
                                       "'";

//                System.out.println(budgetUpdate1);

                stmt.executeUpdate(budgetUpdate1);

//                System.out.println("Updated 4th Quarter");
            }else {
                String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
                        +
                        " SET TOTAL_ACTUAL = (COALESCE(TOTAL_ACTUAL,0) + " +
                        cost_price.replaceAll(",", "") +
                        "),BALANCE_ALLOCATION = BALANCE_ALLOCATION - "+cost_price.replaceAll(",", "")+"  WHERE BRANCH_ID='" +
                        branchCode +
                        "' AND CATEGORY_CODE='" + old_category +
                        "' AND SBU_CODE = '" + sbu_code + "' AND SUB_CATEGORY_CODE = '" + subCategoryCode + "' AND ACC_START_DATE='" +
                        dateFormat.dateConvert(bugdetinfo[0])
                        + "' AND ACC_END_DATE='" +
                        dateFormat.dateConvert(bugdetinfo[2]) +
                        "'";

// System.out.println(budgetUpdate1);

 stmt.executeUpdate(budgetUpdate1);

// System.out.println("Updated 4th Quarter");
}

        } catch (Exception ex) {
            System.out.println("ERROR_ " + this.getClass().getName() + "---" +
                               ex.getMessage() + "--");
            ex.printStackTrace();
        } finally {
            //freeResource();
            dbConnection.closeConnection(conn, stmt);
        }
        //System.out.println(
               // "Exiting update of Aquicisition Budget due to Asset Creation");
    }
    
    public void updateBudget(String quarter, String[] budgetInfo) throws SQLException {
        String branchCode = approvalRec.getCodeName(
            "SELECT BRANCH_CODE FROM AM_AD_BRANCH WHERE BRANCH_ID = " + branch_id
        );
        String subCategoryCode = approvalRec.getCodeName(
            "SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = " + sub_category_id
        );

        String oldCategory = getCatCode();
        String cost = cost_price.replaceAll(",", "");

        String columnToUpdate;
        switch (quarter.toUpperCase()) {
            case "FIRST":
                columnToUpdate = "TOTAL_ACTUAL";
                break;
            case "2ND":
                columnToUpdate = "Q2_ACTUAL";
                break;
            case "3RD":
                columnToUpdate = "Q3_ACTUAL";
                break;
            case "4TH":
                columnToUpdate = "Q4_ACTUAL";
                break;
            default:
                columnToUpdate = "TOTAL_ACTUAL";
                break;
        }

        String sql = "UPDATE AM_ACQUISITION_BUDGET " +
                     "SET " + columnToUpdate + " = COALESCE(" + columnToUpdate + ",0) + ?, " +
                     "BALANCE_ALLOCATION = BALANCE_ALLOCATION - ? " +
                     "WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SBU_CODE = ? AND SUB_CATEGORY_CODE = ? " +
                     "AND ACC_START_DATE = ? AND ACC_END_DATE = ?";

        try (Connection conn = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, Double.parseDouble(cost));
            ps.setDouble(2, Double.parseDouble(cost));
            ps.setString(3, branchCode);
            ps.setString(4, oldCategory);
            ps.setString(5, sbu_code);
            ps.setString(6, subCategoryCode);
            ps.setDate(7, dateFormat.dateConvert(budgetInfo[0]));
            ps.setDate(8, dateFormat.dateConvert(budgetInfo[2]));

            int updated = ps.executeUpdate();
            System.out.println("Budget updated for " + quarter + " quarter. Rows affected: " + updated);

        } catch (Exception ex) {
            System.out.println("ERROR in updateBudget: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

	/**
	 * @return the amountPTD
	 */
	public String getAmountPTD() {
		return amountPTD;
	}

	/**
	 * @param amountPTD the amountPTD to set
	 */
	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	/**
	 * @return the amountREM
	 */
	public String getAmountREM() {
		return amountREM;
	}

	/**
	 * @param amountREM the amountREM to set
	 */
	public void setAmountREM(String amountREM) {
		this.amountREM = amountREM;
	}

	/**
	 * @return the fullyPAID
	 */
	public String getFullyPAID() {
		return fullyPAID;
	}

	/**
	 * @param fullyPAID the fullyPAID to set
	 */
	public void setFullyPAID(String fullyPAID) {
		if(fullyPAID!=null){
		this.fullyPAID = fullyPAID;}
	}

	/**
	 * @return the partPAY
	 */
	public String getPartPAY() {
		return partPAY;
	}

	/**
	 * @param partPAY the partPAY to set
	 */
	public void setPartPAY(String partPAY) {
		if(partPAY!=null){
		this.partPAY = partPAY;}
	}

	/**
	 * @return the group_id
	 */
	public String getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

    /**
     * @return the bar_code
     */
    public String getBar_code() {
        if(bar_code == null){
       bar_code ="";
         }

       return bar_code;
        }

    /**
     * @param bar_code the bar_code to set
     */
    public void setBar_code(String bar_code) {
    if (bar_code != null) {
            this.bar_code = bar_code.replaceAll("'", "");
        }

        //this.bar_code = bar_code;
    }



public String subjectToVatOld(String id){
String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query =
                "SELECT Subject_TO_Vat FROM am_asset  " +
                "WHERE asset_id = '" + id + "' ";


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching Subject to VAT  ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;
}

public String subjectToVat(String assetId) {
    String result = "";
    String query = "SELECT Subject_TO_Vat FROM am_asset WHERE asset_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString("Subject_TO_Vat");
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching Subject to VAT -> " + ex);
        ex.printStackTrace();
    }

    return result;
}

 /*

    public String subjectToVat(String Asset_id)
    {
        String subjectVat;
        String query;
        Connection con;
        PreparedStatement ps;
        subjectVat = "";
        query = (new StringBuilder("SELECT Subject_TO_Vat FROM AM_ASSET where Asset_id='")).append(Asset_id).append("'").toString();
        con = null;
        ps = null;
        //ResultSet rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(ResultSet rs = ps.executeQuery(); rs.next();)
            {
                subjectVat = rs.getString("Subject_TO_Vat");
            }

        }
        catch(Exception er)
        {
            er.printStackTrace();
           // break MISSING_BLOCK_LABEL_134;
        }finally{
        //break MISSING_BLOCK_LABEL_120;
        //Exception exception;
        //exception;
        dbConnection.closeConnection(con, ps);
        //throw exception;
        dbConnection.closeConnection(con, ps);
        //break MISSING_BLOCK_LABEL_145;
        dbConnection.closeConnection(con, ps);
        }
        return subjectVat;
    }

    */

















public String whTaxOld(String id){
String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query =
                "SELECT wh_tax FROM am_asset  " +
                "WHERE asset_id = '" + id + "' ";


        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching WHTAX ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;
}

public String whTax(String assetId) {
    String result = "";
    String query = "SELECT wh_tax FROM am_asset WHERE asset_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString("wh_tax");
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching WHTAX -> " + ex);
        ex.printStackTrace();
    }

    return result;
}


public String checkCategoryCodeOld()
    {
       String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query =
                "SELECT CATEGORY_CODE FROM am_asset" ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;
    }

public String checkCategoryCode() {
    String result = "";
    String query = "SELECT CATEGORY_CODE FROM am_asset";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            result = rs.getString("CATEGORY_CODE");
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching CategoryCode -> " + ex);
        ex.printStackTrace();
    }

    return result;
}


public String checkBranchCodeOld()
    {
       String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query =
                "SELECT BRANCH_CODE FROM am_asset" ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching BranchCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;
    }

public String checkBranchCode() {
    String result = "";
    String query = "SELECT BRANCH_CODE FROM am_asset";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            result = rs.getString("BRANCH_CODE");
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching BranchCode -> " + ex);
        ex.printStackTrace();
    }

    return result;
}

 public String getCodeNameOld(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        //ResultSet rs = null;
        ps = null;
//        System.out.println("Query in getCodeName of AssetRecordsBean: "+query);
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(ResultSet rs = ps.executeQuery(); rs.next();)
            {
                result = rs.getString(1) != null ? rs.getString(1) : "";
            }

        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("Error in getCodeName()... ->")).append(er).toString());
            er.printStackTrace();
            //break MISSING_BLOCK_LABEL_143;
        }
        finally{
        //break MISSING_BLOCK_LABEL_130;
        //Exception exception;
        //exception;
        dbConnection.closeConnection(con, ps);
        //throw exception;
        dbConnection.closeConnection(con, ps);
        //break MISSING_BLOCK_LABEL_153;
        dbConnection.closeConnection(con, ps);
        }
        return result;
    }
 
 
 public String getCodeName(String query) {
	    String result = "";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            result = rs.getString(1) != null ? rs.getString(1) : "";
	        }

	    } catch (Exception e) {
	        System.out.println("Error in getCodeName() -> " + e);
	        e.printStackTrace();
	    }

	    return result;
	}


 public boolean createRequisitionFormOld(String itemRequest, String requestBranch, String requestDepartment, String remark, String requestTo, String approval)
    {
        boolean done;
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        done = false;
        query = "INSERT INTO REQUISITIONFORM VALUES(?,?,?,?,?,?)";
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, itemRequest);
            ps.setString(2, requestBranch);
            ps.setString(3, requestDepartment);
            ps.setString(4, remark);
            ps.setString(5, requestTo);
            ps.setString(6, approval);
            ps.execute();
            if(rs.next())
            {
                done = true;
            }
        }
        catch(Exception er)
        {
            er.printStackTrace();
           // break MISSING_BLOCK_LABEL_162;
        }finally{
        //break MISSING_BLOCK_LABEL_148;
       // Exception exception;
        //exception;
        dbConnection.closeConnection(con, ps);
        //throw exception;
        dbConnection.closeConnection(con, ps);
        //break MISSING_BLOCK_LABEL_173;
        dbConnection.closeConnection(con, ps);
        }
        return done;
    }
 
 public boolean createRequisitionForm(String itemRequest, String requestBranch, String requestDepartment,
         String remark, String requestTo, String approval) {
boolean done = false;
String query = "INSERT INTO REQUISITIONFORM VALUES(?,?,?,?,?,?)";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, itemRequest);
ps.setString(2, requestBranch);
ps.setString(3, requestDepartment);
ps.setString(4, remark);
ps.setString(5, requestTo);
ps.setString(6, approval);


done = ps.executeUpdate() > 0;

} catch (Exception e) {
System.out.println("Error creating requisition form -> " + e);
e.printStackTrace();
}

return done;
}

    /**
     * @return the sbu
     */
    public String getSbu_code() {
         if(sbu_code == null){
       sbu_code ="";
         }

       return sbu_code;
    }
    public String getNewsbu_code() {
        if(newsbu_code == null){
      newsbu_code ="";
        }

      return newsbu_code;
   }

    /**
     * @param sbu the sbu to set
     */
    public void setSbu_code(String sbu_code) {
        
        if (sbu_code != null) {
            this.sbu_code = sbu_code;
        }
        //this.sbu = sbu;
    }
    public void setNewsbu_code(String newsbu_code) {
        
        if (newsbu_code != null) {
            this.newsbu_code = newsbu_code;
        }
        //this.sbu = sbu;
    }
    /**
     * @return the lpo
     */
    public String getLpo() {
        if(lpo == null){
       lpo ="";
         }

       return lpo;
    }

    /**
     * @param lpo the lpo to set
     */
    public void setLpo(String lpo) {
         if (lpo != null) {
            this.lpo = lpo;
         }
    }
 public String getMemoValue() {
		return memoValue;
	}

	public void setMemoValue(String memoValue) {
		this.memoValue = memoValue;
	}


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
        
    public String[] setUpInfoOld(){

        String[] result= new String[4];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query =
                "SELECT LPO_Required,Barcode_Fld,Trans_Threshold,Cost_Threshold FROM am_gb_company" ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
                result[3] = rs.getString(4);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;


    }
    
    
    public String[] setUpInfo() {
        String[] result = new String[4];
        String query = "SELECT LPO_Required, Barcode_Fld, Trans_Threshold, Cost_Threshold FROM am_gb_company";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result[0] = rs.getString("LPO_Required");
                result[1] = rs.getString("Barcode_Fld");
                result[2] = rs.getString("Trans_Threshold");
                result[3] = rs.getString("Cost_Threshold");
            } else {
                System.out.println("WARN: setUpInfo() query returned no rows");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching company setup info -> " + ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * @return the supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }


    public void setPendingTransOld(String[] a, String code){
//    	System.out.println("====code 0====> "+code);
//    	System.out.println("TEST a[0])===> "+a[0]);
//    	System.out.println("TEST a[1])===> "+a[1]);
//    	System.out.println("TEST a[2])===> "+a[2]);
//    	System.out.println("TEST a[5])===> "+a[5]); 
        int transaction_level=0;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level,Transaction_type from approval_level_setup where code ='"+code+"'";
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");

            
            /////////////To get transaction level
             ps = con.prepareStatement(tranLevelQuery);
              rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);
 
       
            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13, (a[0]==null)?"":a[0]);
            ps.setString(13,mtid);
            ps.setString(14, mtid);
//            ps.setString(14, (a[0]==null)?"":a[0]);
            ps.setInt(15, transaction_level);

            
            
            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans>>>>>>" + er);

        }finally{
        dbConnection.closeConnection(con, ps);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";






    }//staticApprovalInfo()
    
    
    public void setPendingTrans(String[] a, String code) {
        int transactionLevel = 0;
        String pq = "INSERT INTO am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"
                + "effective_date,branchCode,asset_status,tran_type,process_status,tran_sent_time,transaction_id,batch_id,"
                + "transaction_level) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String tranLevelQuery = "SELECT level FROM approval_level_setup WHERE code = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement psLevel = con.prepareStatement(tranLevelQuery)) {

            // ===== Get transaction level =====
            psLevel.setString(1, code);
            try (ResultSet rs = psLevel.executeQuery()) {
                if (rs.next()) {
                    transactionLevel = rs.getInt(1);
                }
            }

            // ===== Prepare insert =====
            try (PreparedStatement ps = con.prepareStatement(pq)) {

                String mtid = new ApplicationHelper().getGeneratedId("am_asset_approval");
                SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

                ps.setString(1, a[0] != null ? a[0] : "");  // asset_id
                ps.setString(2, a[1] != null ? a[1] : "");  // user_id
                ps.setString(3, a[2] != null ? a[2] : "");  // super_id
                ps.setDouble(4, (a[3] != null && !a[3].isEmpty()) ? Double.parseDouble(a[3]) : 0);  // amount
                ps.setTimestamp(5, dbConnection.getDateTime(new java.util.Date()));  // posting_date
                ps.setString(6, a[5] != null ? a[5] : "");  // description
                ps.setDate(7, a[6] != null ? dbConnection.dateConvert(a[6]) : null);  // effective_date
                ps.setString(8, a[7] != null ? a[7] : "");  // branchCode
                ps.setString(9, a[8] != null ? a[8] : "");  // asset_status
                ps.setString(10, a[9] != null ? a[9] : ""); // tran_type
                ps.setString(11, a[10] != null ? a[10] : ""); // process_status
                ps.setString(12, timer.format(new java.util.Date())); // tran_sent_time
                ps.setString(13, mtid);   // transaction_id
                ps.setString(14, mtid);   // batch_id
                ps.setInt(15, transactionLevel);

                ps.executeUpdate();
            }

        } catch (Exception er) {
            System.out.println(">>> AssetRecordBeans:setPendingTrans >>>> " + er);
            er.printStackTrace();
        }
    }


public String timeInstance() {
        DateFormat t = DateFormat.getTimeInstance();
        return t.format(new Date());

    }




public String[] setApprovalDataOld(String id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
 //String currentDate  = reArrangeDate(getCurrentDate1());
   // System.out.println("the $$$$$$$$$$$ "+currentDate);
    String[] result= new String[12];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id ='" +id+"'";

//         System.out.println("query in setApprovalData $$$$$$$$$$$ "+query);

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
result[9] = "Asset Creation";
result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()

public String[] setApprovalData(String assetId) {
    String[] result = new String[12]; 

    String query = "SELECT asset_id, user_ID, supervisor, Cost_Price, Posting_Date, "
                 + "description, effective_date, BRANCH_CODE, Asset_Status "
                 + "FROM am_asset WHERE asset_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result[0] = rs.getString("asset_id");
                result[1] = rs.getString("user_ID");
                result[2] = rs.getString("supervisor");
                result[3] = rs.getString("Cost_Price");
                result[4] = rs.getString("Posting_Date");
                result[5] = rs.getString("description");
                result[6] = rs.getString("effective_date");
                result[7] = rs.getString("BRANCH_CODE");
                result[8] = rs.getString("Asset_Status");
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching approval data -> " + ex);
        ex.printStackTrace();
    }

    
    result[9]  = "Asset Creation";  // transaction type
    result[10] = "P";               // process status
  //  result[11] = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());  

    return result;
}


public String[] setApprovalDataforAsset2Old(String id,String code){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
 //String currentDate  = reArrangeDate(getCurrentDate1());
   // System.out.println("the $$$$$$$$$$$ "+currentDate);
    String[] result= new String[12];
    String transType = "";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from am_asset2 where asset_id ='" +id+"'";

         String tranLevelQuery = "select Transaction_type from approval_level_setup where code ='"+code+"'";

        try {
            con = dbConnection.getConnection("legendPlus");
            
            ps = con.prepareStatement(tranLevelQuery);
            rs = ps.executeQuery();
          while(rs.next()){
          transType = rs.getString(1);

          }//if
            
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
//result[9] = "Asset Creation";
result[9] = transType;
result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()

public String[] setApprovalDataforAsset2(String assetId, String code) {
    String[] result = new String[12];
    String transType = "";

    String assetQuery = "SELECT asset_id, user_ID, supervisor, Cost_Price, Posting_Date, "
                      + "description, effective_date, BRANCH_CODE, Asset_Status "
                      + "FROM am_asset2 WHERE asset_id = ?";

    String tranLevelQuery = "SELECT Transaction_type FROM approval_level_setup WHERE code = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement psTran = con.prepareStatement(tranLevelQuery);
         PreparedStatement psAsset = con.prepareStatement(assetQuery)) {

        // Fetch transaction type
        psTran.setString(1, code);
        try (ResultSet rsTran = psTran.executeQuery()) {
            if (rsTran.next()) {
                transType = rsTran.getString(1);
            }
        }

        // Fetch asset data
        psAsset.setString(1, assetId);
        try (ResultSet rsAsset = psAsset.executeQuery()) {
            if (rsAsset.next()) {
                result[0] = rsAsset.getString("asset_id");
                result[1] = rsAsset.getString("user_ID");
                result[2] = rsAsset.getString("supervisor");
                result[3] = rsAsset.getString("Cost_Price");
                result[4] = rsAsset.getString("Posting_Date");
                result[5] = rsAsset.getString("description");
                result[6] = rsAsset.getString("effective_date");
                result[7] = rsAsset.getString("BRANCH_CODE");
                result[8] = rsAsset.getString("Asset_Status");
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching asset approval data -> " + ex);
        ex.printStackTrace();
    }

    result[9]  = transType;  // transaction type
    result[10] = "P";        // process status
  //  result[11] = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());

    return result;
}

    /**
     * @return the processin_status
     */
   public String getTransDetailsOld(String asset_id){
   String query = "select process_status from am_asset_approval where asset_id ='"+asset_id+"'";
   String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
               
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;

   
   } //getTransactionDetails()
   
   public String getTransDetails(String assetId) {
	    String result = "";
	    String query = "SELECT process_status FROM am_asset_approval WHERE asset_id = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, assetId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getString("process_status");
	            }
	        }

	    } catch (Exception ex) {
	        System.out.println("WARN: Error fetching transaction details -> " + ex);
	        ex.printStackTrace();
	    }

	    return result;
	}

public void getTransFeedbackOld(String id, String process_status, String reject_reason,long tran_id){
String query_r ="update am_asset_approval set process_status=?,reject_reason=? where asset_id = '"+id+"'and transaction_id="+tran_id;
    //System.out.println("the value of  ==============" + id);
Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");

if(process_status.equalsIgnoreCase("a")||process_status.equalsIgnoreCase("p")){
reject_reason = "";
}
ps = con.prepareStatement(query_r);



            ps.setString(1,process_status);
            ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();
//System.out.println("the value of i is =========="+ i);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: getTransFeedback()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

}//getTransFeedback()


public void getTransFeedback(String assetId, String processStatus, String rejectReason, long tranId) {
    String query = "UPDATE am_asset_approval SET process_status = ?, reject_reason = ? " +
                   "WHERE asset_id = ? AND transaction_id = ?";

    // Clear reject reason if approved or pending
    if ("a".equalsIgnoreCase(processStatus) || "p".equalsIgnoreCase(processStatus)) {
        rejectReason = "";
    }

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, processStatus);
        ps.setString(2, rejectReason);
        ps.setString(3, assetId);
        ps.setLong(4, tranId);

        int updated = ps.executeUpdate();
       

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: getTransFeedback() >>>>> " + ex);
        ex.printStackTrace();
    }
}

 
public void updateAssetStatusApprovalOld(int transId,String supervisorId){
	//Date approveddate = new java.util.Date();
	Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
	//System.out.println("Approveddate in updateAssetStatusApproval: "+approveddate);
//String query_r ="update am_asset_approval set asset_status=? where asset_id = '"+assetId+"'";
String query_r ="update am_asset_approval set asset_status='APPROVED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '"+transId+"' and super_id = '"+supervisorId+"' ";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



          //  ps.setString(1,"ACTIVE");
         //   ps.setInt(2, transId);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusApproval() with two Parameters>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatusApproval()

public void updateAssetStatusApproval(int transId, String supervisorId) {
    Timestamp approvedDate = dbConnection.getDateTime(new java.util.Date());

    String query = "UPDATE am_asset_approval " +
                   "SET asset_status = ?, date_approved = ? " +
                   "WHERE transaction_id = ? AND super_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, "APPROVED");
        ps.setTimestamp(2, approvedDate);
        ps.setInt(3, transId);
        ps.setString(4, supervisorId);

        int updated = ps.executeUpdate();
        

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: updateAssetStatusApproval() >>>>> " + ex);
        ex.printStackTrace();
    }
}

public void updateAssetStatusApprovalOld(Long transId){
	//Date approveddate = new java.util.Date();
	Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
	//System.out.println("Approveddate in updateAssetStatusApproval: "+approveddate);
//String query_r ="update am_asset_approval set asset_status=? where asset_id = '"+assetId+"'";
String query_r ="update am_asset_approval set asset_status='APPROVED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '"+transId+"' ";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



          //  ps.setString(1,"ACTIVE");
         //   ps.setInt(2, transId);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusApproval() with One Parameters>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatusApproval()

public void updateAssetStatusApproval(Long transId) {
    Timestamp approvedDate = dbConnection.getDateTime(new java.util.Date());
    String query = "UPDATE am_asset_approval " +
                   "SET asset_status = ?, date_approved = ? " +
                   "WHERE transaction_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, "APPROVED");
        ps.setTimestamp(2, approvedDate);
        ps.setLong(3, transId);

        int updated = ps.executeUpdate();
        

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: updateAssetStatusApproval() >>>>> " + ex);
        ex.printStackTrace();
    }
}

public void updateAssetStatusOld(String assetId,String supervisorId){
String query_r ="update am_asset set asset_status=?,raise_entry=? where asset_id = '"+assetId+"' and super_id = '"+supervisorId+"' ";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"ACTIVE");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatus() with Two Parameters>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()

public void updateAssetStatus(String assetId, String supervisorId) {
    String query = "UPDATE am_asset SET asset_status = ?, raise_entry = ? " +
                   "WHERE asset_id = ? AND super_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, "ACTIVE");
        ps.setString(2, "Y");
        ps.setString(3, assetId);
        ps.setString(4, supervisorId);

        int updated = ps.executeUpdate();
        // Optional: System.out.println("Rows updated: " + updated);

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: updateAssetStatus() >>>>> " + ex);
        ex.printStackTrace();
    }
}

public void updateAssetStatusOld(String assetId){
String query_r ="update am_asset set asset_status=?,raise_entry=? where asset_id = '"+assetId+"' ";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"ACTIVE");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatus() with One Parameters>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()

public void updateAssetStatus(String assetId) {
    String query = "UPDATE am_asset SET asset_status = ?, raise_entry = ? WHERE asset_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, "ACTIVE");
        ps.setString(2, "Y");
        ps.setString(3, assetId);

        int updated = ps.executeUpdate();
        

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: updateAssetStatus() >>>>> " + ex);
        ex.printStackTrace();
    }
}


public String getDisposalSupervisorOld(String id, String trans_type){
  String query = "select super_id from am_asset_approval where asset_id ='"+id+"'and tran_type ='"+trans_type+"'";
   String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;





        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;





}//getDisposalSupervisor()

public String getDisposalSupervisor(String assetId, String tranType) {
    String result = "";
    String query = "SELECT super_id FROM am_asset_approval WHERE asset_id = ? AND tran_type = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);
        ps.setString(2, tranType);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching Disposal Supervisor -> " + ex);
        ex.printStackTrace();
    }

    return result;
}

public String[] setDisposalDataOld(String id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id ='" +id+"'"; ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
result[9] = "Asset Creation";
result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


public String[] setDisposalData(String assetId) {
    String[] result = new String[12];
    String query = "SELECT asset_id, user_ID, supervisor, Cost_Price, Posting_Date, "
                 + "description, effective_date, BRANCH_CODE, Asset_Status "
                 + "FROM am_asset WHERE asset_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result[0] = rs.getString("asset_id");
                result[1] = rs.getString("user_ID");
                result[2] = rs.getString("supervisor");
                result[3] = rs.getString("Cost_Price");
                result[4] = rs.getString("Posting_Date");
                result[5] = rs.getString("description");
                result[6] = rs.getString("effective_date");
                result[7] = rs.getString("BRANCH_CODE");
                result[8] = rs.getString("Asset_Status"); 
            }
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fetching disposal data -> " + ex);
        ex.printStackTrace();
    }


    result[9] = "Asset Creation";
    result[10] = "P";
    // result[11] = timeInstance(); 

    return result;
}



public void updateAssetStatusChangeOld(String query_r){
//String query_r ="update" +tableName+ "set"+columnName+"=? where asset_id = '"+assetId+"'";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try { 
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);

//    System.out.println("in update asset status change of asset records beans================: "+query_r);

            //ps.setString(1,status);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();
           dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusChange()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()


public void updateAssetStatusChange(String query) {
    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        // ps.setString(1, value1);
        // ps.setInt(2, value2);

        int rowsUpdated = ps.executeUpdate();
       // System.out.println("Rows updated: " + rowsUpdated);

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: updateAssetStatusChange() >>>>> " + ex);
        ex.printStackTrace();
    }
}

public void insertMailsOld(String createdby,String subject, String msgText1){
String query_r ="INSERT INTO MAILS_TO_SEND (MAIL_ADDRESS,MAIL_HEADER,MAIL_BODY) VALUES(?,?,?) ";
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

try { 
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);

 //   System.out.println("insert Mail records beans================");

            ps.setString(1,createdby);
            ps.setString(2,subject);
            ps.setString(3,msgText1);
            ps.execute();
           dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: InsertMails()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()


public void insertMails(String createdBy, String subject, String msgText) {
    String query = "INSERT INTO MAILS_TO_SEND (MAIL_ADDRESS, MAIL_HEADER, MAIL_BODY) VALUES (?, ?, ?)";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, createdBy);
        ps.setString(2, subject);
        ps.setString(3, msgText);

        int rowsInserted = ps.executeUpdate();
       // System.out.println("Rows inserted: " + rowsInserted);

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: insertMails() >>>>> " + ex);
        ex.printStackTrace();
    }
}

public void updateGroupAssetStatusChangeOld(long id,String status)
{
	//String query_r ="update" +tableName+ "set"+columnName+"=? where asset_id = '"+assetId+"'";
	Connection con = null;
    PreparedStatement ps = null;
    String query_r ="update am_group_asset set asset_status=? " +
	" where Group_id = '"+id+"'";
    try 
    	{
    	con = dbConnection.getConnection("legendPlus");
    	ps = con.prepareStatement(query_r);
    	ps.setString(1,status);
       	int i =ps.executeUpdate();
        updateGroupAssetMainStatusChange(id,status);
        } 
	catch (Exception ex)
	    {
	        System.out.println("AssetRecordBean: Error Updating am_group_asset " + ex);
	    } 
	finally 
		{
            dbConnection.closeConnection(con, ps);
        }

}//updateAssetStatus()

public void updateGroupAssetStatusChange(long id, String status) {
    String query = "UPDATE am_group_asset SET asset_status = ? WHERE Group_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, status);
        ps.setLong(2, id);

        int rowsUpdated = ps.executeUpdate();
      //  System.out.println("Rows updated in am_group_asset: " + rowsUpdated);

        updateGroupAssetMainStatusChange(id, status);

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: Error updating am_group_asset >>> " + ex);
        ex.printStackTrace();
    }
}

public void updateGroupAssetMainStatusChangeOld(long id,String status)
{
	String query_r ="update am_group_asset_main set asset_status=? " +
	"where Group_id = '"+id+"'";
	Connection con = null;
    PreparedStatement ps = null;
    try 
	{
	con = dbConnection.getConnection("legendPlus");
	ps = con.prepareStatement(query_r);
	ps.setString(1,status);
    int i =ps.executeUpdate();
    } 
catch (Exception ex)
    {
        System.out.println("AssetRecordBean: Error Updating am_group_asset_main : " + ex);
    } 
finally 
	{
        dbConnection.closeConnection(con, ps);
    }
}


public void updateGroupAssetMainStatusChange(long id, String status) {
    String query = "UPDATE am_group_asset_main SET asset_status = ? WHERE Group_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, status);
        ps.setLong(2, id);

        int rowsUpdated = ps.executeUpdate();
        //System.out.println("Rows updated in am_group_asset_main: " + rowsUpdated);

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: Error updating am_group_asset_main >>> " + ex);
        ex.printStackTrace();
    }
}

///////////////lanre modification to assetRecordsBean//////////////////////////////////////////
/*
public String checkBranchCode()
	{
		String branch="";
		String query="SELECT BRANCH_CODE FROM AM_ASSET";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next())
			 {

				branch  = rs.getString("BRANCH_CODE");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	branch;
	}

*/
	public String checkBranchCodeOld(String Asset_id)
	{
		String branch="";
		String query="SELECT BRANCH_CODE FROM AM_ASSET where Asset_id='"+Asset_id+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next())
			 {

				branch  = rs.getString("BRANCH_CODE");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	branch;
	}
	
	public String checkBranchCode(String assetId) {
	    String branch = "";
	    String query = "SELECT BRANCH_CODE FROM AM_ASSET WHERE Asset_id = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, assetId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                branch = rs.getString("BRANCH_CODE");
	            }
	        }

	    } catch (Exception er) {
	        System.out.println("Error in checkBranchCode() >>> " + er);
	        er.printStackTrace();
	    }

	    return branch;
	}

	public String checkCategoryCodeOld(String Asset_id)
	{
		String category="";
		String query="SELECT CATEGORY_CODE FROM AM_ASSET  where Asset_id='"+Asset_id+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next())
			 {

				category  = rs.getString("CATEGORY_CODE");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	category;
	}

	
	public String checkCategoryCode(String assetId) {
	    String category = "";
	    String query = "SELECT CATEGORY_CODE FROM AM_ASSET WHERE Asset_id = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, assetId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                category = rs.getString("CATEGORY_CODE");
	            }
	        }

	    } catch (Exception er) {
	        System.out.println("Error in checkCategoryCode() >>> " + er);
	        er.printStackTrace();
	    }

	    return category;
	}
  
	public String checkAssetLedgerAccountOld(String category,String branch)
	{
		String assetledgeraccount="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1)),"
//					+" b.default_branch,"
//					+" b.asset_acq_ac,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1))+"
//					+" b.default_branch + b.asset_acq_ac asd"
//					+" from am_ad_category a,am_ad_branch d, "
//					+" AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		//System.out.println("query in checkAssetLedgerAccount >>>> " + query);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				 assetledgeraccount  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetledgeraccount;
	}
	
	public String checkAssetLedgerAccount(String category, String branch) {

	    String assetLedgerAccount = "";

	    
	    String baseQuery = approvalRec.getCodeName(
	            "SELECT PREFIX FROM ACCOUNT_GLPREFIX_PARAM WHERE type = 'AS'"
	    );

	    String query = baseQuery + " AND a.category_code = ? AND d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetLedgerAccount = rs.getString("asd");
	            }
	        }

	    } catch (Exception er) {
	        System.out.println("Error in checkAssetLedgerAccount() >>> " + er);
	        er.printStackTrace();
	    }

	    return assetLedgerAccount;
	}
//Added by ayojava-retrieves Part Pay Account For Posting -07-08-09
        public String checkAssetLedgerAccountPartPayOld(String category,String branch)
	{
		String assetledgeraccount="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//                        +" (select accronym from am_ad_ledger_type where series = substring(b.part_pay_acct,1,1)),"
//                        +" b.default_branch,"
//                        +" b.asset_acq_ac,"
//                        +" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.part_pay_acct,1,1))+"
//                        +" b.default_branch + b.part_pay_acct asd"
//                        +" from am_ad_category a,am_ad_branch d, "
//                        +" AM_GB_CURRENCY_CODE c, am_gb_company b"
//                        +" where a.currency_id = c.currency_id"
//                        +" and a.category_code = '"+category+"'"
//                        +" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in checkAssetLedgerAccount >>>> " + query);
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI2'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				 assetledgeraccount  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetledgeraccount;
	}

        public String checkAssetLedgerAccountPartPay(String category, String branch) {

            String assetLedgerAccount = "";

           
            String baseQuery = approvalRec.getCodeName(
                    "SELECT PREFIX FROM ACCOUNT_GLPREFIX_PARAM WHERE type = 'LI2'"
            );

         
            String query = baseQuery + " AND a.category_code = ? AND d.branch_code = ?";

            try (Connection con = dbConnection.getConnection("legendPlus");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, category);
                ps.setString(2, branch);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        assetLedgerAccount = rs.getString("asd");
                    }
                }

            } catch (Exception er) {
                System.out.println("Error in checkAssetLedgerAccountPartPay() >>> " + er);
                er.printStackTrace();
            }

            return assetLedgerAccount;
        }
        
	public String checkAccounAcqusitionSuspenseOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" b.default_branch,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" b.default_branch +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in checkAccounAcqusitionSuspense >>>> " + query);
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS4'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String checkAccounAcqusitionSuspense(String category, String branch) {

	    String assetAcqusitionSuspense = "";

	    String script = approvalRec.getCodeName(
	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS4'"
	    );

	    String query = script + 
	        " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetAcqusitionSuspense = rs.getString("asd");
	            }
	        }

	    } catch (Exception er) {
	        er.printStackTrace();
	    }

	    return assetAcqusitionSuspense;
	}
	
//edited by ayojava .Query uses branch_code to generate account number instead of default_branch
        public String checkAccounAcqusitionSuspense2Old (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" d.branch_code,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" d.branch_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		//System.out.println("query in checkAccounAcqusitionSuspense >>>> " + query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}

        public String checkAccounAcqusitionSuspense2(String category, String branch) {

            String assetAcqusitionSuspense = "";

            String script = approvalRec.getCodeName(
                "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'"
            );

            String query = script + 
                " and a.category_code = ? and d.branch_code = ?";

            try (Connection con = dbConnection.getConnection("legendPlus");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, category);
                ps.setString(2, branch);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        assetAcqusitionSuspense = rs.getString("asd");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace(); 
            }

            return assetAcqusitionSuspense;
        }

    	public String checkAccounAcqusitionSuspenseBranchOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" b.default_branch,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" d.branch_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";

	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
//	     System.out.println("====query in checkAccounAcqusitionSuspenseBranch: "+query);   
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
    	
    	public String checkAccounAcqusitionSuspenseBranch(String category, String branch) {

    	    String assetAcqusitionSuspense = "";

    	    String script = approvalRec.getCodeName(
    	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'"
    	    );

    	    String query = script +
    	            " and a.category_code = ? and d.branch_code = ?";

    	    try (Connection con = dbConnection.getConnection("legendPlus");
    	         PreparedStatement ps = con.prepareStatement(query)) {

    	        ps.setString(1, category);
    	        ps.setString(2, branch);

    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	                assetAcqusitionSuspense = rs.getString("asd");
    	            }
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace(); 
    	    }

    	    return assetAcqusitionSuspense;
    	}
    	
	public String assetProcurementDrOld(String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" d.branch_code,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" d.branch_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in assetProcurementDr >>>> " + query);
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String assetProcurementDr(String category, String branch) {

	    String assetAcqusitionSuspense = "";

	    String script = approvalRec.getCodeName(
	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'"
	    );

	    String query = script +
	            " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetAcqusitionSuspense = rs.getString("asd");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return assetAcqusitionSuspense;
	}
	
	public String assetProcurementCrOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" d.branch_code,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" d.branch_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";

	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String assetProcurementCr(String category, String branch) {

	    String assetAcqusitionSuspense = "";

	    String script = approvalRec.getCodeName(
	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'"
	    );

	    String query = script +
	            " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetAcqusitionSuspense = rs.getString("asd");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return assetAcqusitionSuspense;
	}
	
	public String assetVatDrOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" d.branch_code,"
//					+" a.Asset_Ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
//					+" d.branch_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";

	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String assetVatDr(String category, String branch) {

	    String assetAcqusitionSuspense = "";

	    String script = approvalRec.getCodeName(
	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'"
	    );

	    String query = script +
	            " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetAcqusitionSuspense = rs.getString("asd");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return assetAcqusitionSuspense;
	}

	public String witholdingFedTaxCrOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//					+" b.default_branch,"
//					+" b.fed_wht_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1))+"
//					+" b.default_branch +	b.fed_wht_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		System.out.println("query in witholdingFedTaxCr >>>> " + query);
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
//	     System.out.println("query in witholdingFedTaxCr >>>> " + query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String witholdingFedTaxCr(String category, String branch) {

	    String assetAcqusitionSuspense = "";

	    String script = approvalRec.getCodeName(
	        "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI'"
	    );

	    String query = script +
	            " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                assetAcqusitionSuspense = rs.getString("asd");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return assetAcqusitionSuspense;
	}
	

	public String witholdingFedTaxCrOld (String category,String branch,String whtax)
	{
		String assetAcqusitionSuspense="";
		String query = "";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		System.out.println("whtax in =====: "+whtax);
		if(whtax.equalsIgnoreCase("S")){
//			String queryState=" select c.iso_code,"
//						+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//						+" b.default_branch,"
//						+" b.wht_account,"
//						+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1))+"
//						+" b.default_branch +	b.wht_account asd"
//						+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//						+" where a.currency_id = c.currency_id"
//						+" and a.category_code = '"+category+"'"
//						+" and d.branch_code = '"+branch+"'";
		     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI'");
		     String queryState = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		     query= queryState;
//		     System.out.println("query in witholdingFedTaxCr >>>> " + query);
			
		}
			if(whtax.equalsIgnoreCase("F")){
//			String queryFederal=" select c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//				+" b.default_branch,"
//				+" b.fed_wht_account,"
//				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1))+"
//				+" b.default_branch +	b.fed_wht_account asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//				+" where a.currency_id = c.currency_id"
//				+" and a.category_code = '"+category+"'"
//				+" and d.branch_code = '"+branch+"'";		
//			query= queryFederal;
			     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI'");
			     String queryFederal = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
			     query= queryFederal;
//			     System.out.println("query in witholdingFedTaxCr >>>> " + query);				
			}
				
//		System.out.println("query in witholdingFedTaxCr >>>> " + query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}	
	
	public String witholdingFedTaxCr(String category, String branch, String whtax) {

	    String result = "";

	    if (!"S".equalsIgnoreCase(whtax) &&
	        !"F".equalsIgnoreCase(whtax)) {
	        return "";
	    }

	    String script = AccountPrefixCache.get("LI"); 

	    if (script == null) {
	        return "";
	    }

	    String query = script +
	            " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getString("asd");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return result;
	}
	
	
	public String stateWHTaxCrOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" d.branch_code,"
//					+" b.wht_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1))+"
//					+" d.branch_code +	b.wht_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
////		System.out.println("query in stateWHTaxCr >>>> " + query);
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI'");
	     String queryFederal = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     String query= queryFederal;
//	     System.out.println("query in witholdingFedTaxCr >>>> " + query);		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}


	public String stateWHTaxCr(String category, String branch) {
	    String result = "";

	    String script = AccountPrefixCache.get("LI"); 

	    if (script == null || script.isEmpty()) {
	        return ""; 
	    }

	    String query = script + " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getString("asd"); 
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}



	public String statePLDisposalAccountOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String old_query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" d.branch_code,"
//					+" b.PL_Disposal_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.PL_Disposal_account,1,1))+"
//					+" d.branch_code +	b.PL_Disposal_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//
//                String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" d.branch_code,"
//					+" b.PL_Disposal_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.PL_Disposal_account,1,1))+"
//					+" b.default_branch +	(substring(b.PL_Disposal_account,2,9)) asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";

       	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'IN'");
    	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
 //   	     System.out.println("query in witholdingFedTaxCr >>>> " + query);	
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}
	
	public String statePLDisposalAccount(String category, String branch) {
	    String result = "";

	    String script = AccountPrefixCache.get("IN"); 

	    if (script == null || script.isEmpty()) {
	        return "";
	    }

	    String query = script + " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getString("asd"); 
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    return result;
	}
	
	
	public String stateAccumDepLedgerOld (String category,String branch)
	{
		String assetAcqusitionSuspense="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" d.branch_code,"
//					+" a.Accum_Dep_ledger,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+"
//					+" d.branch_code +	a.Accum_Dep_ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
  	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
//	     System.out.println("====query in stateAccumDepLedger: "+query);  
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAcqusitionSuspense;
	}

	
	public String stateAccumDepLedger(String category, String branch) {
	    String result = "";

	    String script = AccountPrefixCache.get("AS"); 

	    if (script == null || script.isEmpty()) {
	        return ""; 
	    }
	    
	    String query = script + " and a.category_code = ? and d.branch_code = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, category);
	        ps.setString(2, branch);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getString("asd"); 
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

        public String checkAccumDepLedgerOld (String category,String branch)
	{
		String assetAccumDepLedger="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//                +" (select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1)),"
//                +" d.branch_code,"
//                +" a.Accum_Dep_ledger,"
//                +" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+"
//                +" d.branch_code  + a.Accum_Dep_ledger asd"
//                +" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//                +" where a.currency_id = c.currency_id"
//                +" and a.category_code = '"+category+"'"
//                +" and d.branch_code = '"+branch+"'";
 	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS2'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAccumDepLedger  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAccumDepLedger;
	}
        
    	public String checkAccumDepLedger (String category,String branch) {
    	    String result = "";

    	    String script = AccountPrefixCache.get("AS2"); 

    	    if (script == null || script.isEmpty()) {
    	        return ""; 
    	    }
    	    
    	    String query = script + " and a.category_code = ? and d.branch_code = ?";

    	    try (Connection con = dbConnection.getConnection("legendPlus");
    	         PreparedStatement ps = con.prepareStatement(query)) {

    	        ps.setString(1, category);
    	        ps.setString(2, branch);

    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	                result = rs.getString("asd"); 
    	            }
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }

    	    return result;
    	}

        public String checkDepLedgerAccountOld (String category,String branch)
	{
		String assetAccumDepLedger="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//                +" (select accronym from am_ad_ledger_type where series = substring(a.Dep_ledger,1,1)),"
//                +" d.branch_code,"
//                +" a.Dep_ledger,"
//                +" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Dep_ledger,1,1))+"
//                +" d.branch_code  + a.Dep_ledger asd"
//                +" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//                +" where a.currency_id = c.currency_id"
//                +" and a.category_code = '"+category+"'"
//                +" and d.branch_code = '"+branch+"'";
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'EX'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	     
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAccumDepLedger  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	assetAccumDepLedger;
	}
        
        public String checkDepLedgerAccount (String category,String branch) {
    	    String result = "";

    	    String script = AccountPrefixCache.get("EX"); 

    	    if (script == null || script.isEmpty()) {
    	        return ""; 
    	    }
    	    
    	    String query = script + " and a.category_code = ? and d.branch_code = ?";

    	    try (Connection con = dbConnection.getConnection("legendPlus");
    	         PreparedStatement ps = con.prepareStatement(query)) {

    	        ps.setString(1, category);
    	        ps.setString(2, branch);

    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	                result = rs.getString("asd"); 
    	            }
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }

    	    return result;
    	}

	/*
    public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null;
     PreparedStatement ps = null;

     try
     {
      con = dbConnection.getConnection("legendPlus");
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();


      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);

      }
     }
     catch(Exception er)
     {
        System.out.println("Error in Query- getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
    	 dbConnection.closeConnection(con, ps);
      }
      return result;
     }
*/
    	public boolean checkApprovalStatusOld(String id)
	{
            boolean status = false;

            String asset_status="";
		String query="SELECT asset_status FROM AM_ASSET where asset_id ='"+id+"'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = dbConnection.getConnection("legendPlus");

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next())
			 {

				asset_status  = rs.getString("asset_status");


			 }
            if(asset_status.equalsIgnoreCase("active")){
            status = true;
            }
		   }

			catch (Exception er)
			{
			 er.printStackTrace();

			}
			finally
			{
				dbConnection.closeConnection(con, ps);
			}
	return 	status;
	}

    	public boolean checkApprovalStatus(String assetId) {
    	    boolean status = false;
    	    String query = "SELECT asset_status FROM AM_ASSET WHERE asset_id = ?";

    	    try (Connection con = dbConnection.getConnection("legendPlus");
    	         PreparedStatement ps = con.prepareStatement(query)) {

    	        ps.setString(1, assetId); 

    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	                String assetStatus = rs.getString("asset_status");
    	                status = "active".equalsIgnoreCase(assetStatus);
    	            }
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace(); 
    	    }

    	    return status;
    	}

        public void setPendingTranValuesOld(String query){
//            System.out.println("in method setPendingTranValues with queryJJJJJJJJJJJJJJJJ" +query);
        Connection con;
        PreparedStatement ps;
        ResultSet rs;

        //query = "INSERT INTO REQUISITIONFORM VALUES(?,?,?,?,?,?)";
        con = null;
        ps = null;
        //rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.execute();

        }
        catch(Exception er)
        {
            er.printStackTrace();

        }finally{

        dbConnection.closeConnection(con, ps);


        }


        }//setPendingTranValues
        
        
        public void setPendingTranValues(String query) {
            try (Connection con = dbConnection.getConnection("legendPlus");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.execute();

            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }



public void incrementApprovalCountOld(long tran_id,int count,int nextSupervisor){
String query_r ="update am_asset_approval set approval_level_count=?,super_id=? where transaction_id =?";
String query_Archive ="update am_asset_approval_archive set approval_level_count=?,super_id=? where transaction_id =?";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);
ps.setInt(1,count);
ps.setInt(2, nextSupervisor);
ps.setLong(3,tran_id);
            //ps.setString(2,reject_reason);
int i =ps.executeUpdate();
 ps = con.prepareStatement(query_Archive);
ps.setInt(1,count);
ps.setInt(2, nextSupervisor);
ps.setLong(3,tran_id);           //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: incrementApprovalCount()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//incrementApprovalCount()


public void incrementApprovalCount(long tran_id, int count, int nextSupervisor) {
    String queryActive = "UPDATE am_asset_approval SET approval_level_count=?, super_id=? WHERE transaction_id=?";
    String queryArchive = "UPDATE am_asset_approval_archive SET approval_level_count=?, super_id=? WHERE transaction_id=?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement psActive = con.prepareStatement(queryActive);
         PreparedStatement psArchive = con.prepareStatement(queryArchive)) {

        psActive.setInt(1, count);
        psActive.setInt(2, nextSupervisor);
        psActive.setLong(3, tran_id);
        psActive.executeUpdate();

        psArchive.setInt(1, count);
        psArchive.setInt(2, nextSupervisor);
        psArchive.setLong(3, tran_id);
        psArchive.executeUpdate();

    } catch (Exception ex) {
        System.out.println("AssetRecordBean: incrementApprovalCount() >>>>> " + ex);
    }
}

    /**
     * @return the deferPay
     */
    public String getDeferPay() {
        return deferPay;
    }

    /**
     * @param deferPay the deferPay to set
     */
    public void setDeferPay(String deferPay) {
        if(deferPay!=null){
		this.deferPay = deferPay;}

        //this.deferPay = deferPay;
    }



public ArrayList selectComponentOld(String  comp_Id)
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ComponentViewDetail app = null;
        ArrayList collection = new ArrayList();
        String FINDER_QUERY = "SELECT  * from AM_AD_COMPONENT WHERE comp_Id = ? ";

        try {
        	con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, comp_Id);

            rs = ps.executeQuery();

            while (rs.next())
             {



            	String id = rs.getString("COMP_ID");
            	String parentAssetId=rs.getString("PARENT_ASSET_ID") ;
            	String parentCompId=rs.getString("PARENT_COMP_ID");
            	String assetId=rs.getString("ASSET_ID");
            	String category=rs.getString("CATEGORY");
            	String description=rs.getString("DESCRIPTION");
            	String serialNumber=rs.getString("SERIAL_NO");
            	String make=rs.getString("MAKE");
            	String model=rs.getString("MODEL");
            	String additionalField=rs.getString("ADDITIONAL_FIELD");
            	String status=rs.getString("STATUS");
            	String component=rs.getString("COMPONENT");
            	double cost=rs.getDouble("COST");
            	double assigned=rs.getDouble("ASSIGNED");
            	double depreciation=rs.getDouble("DEPRECIATION");
            	String Registration_No=rs.getString("Registration_No");
            	int Branch_ID=rs.getInt("Branch_ID");
            	int Dept_ID=rs.getInt("Dept_ID");
            	int Section_id=rs.getInt("Section_id");
            	String Vendor_AC=rs.getString("Vendor_AC");
            	Date Date_purchased=rs.getDate("Date_purchased");
            	double Dep_Rate=rs.getDouble("Dep_Rate");
            	String Asset_Engine_No=rs.getString("Asset_Engine_No");
            	int Supplier_Name=rs.getInt("Supplier_Name");
            	String Asset_User=rs.getString("Asset_User");
            	int Asset_Maintenance=rs.getInt("Asset_Maintenance");
            	double Accum_Dep=rs.getDouble("Accum_Dep");
            	double Monthly_Dep=rs.getDouble("Monthly_Dep");
            	double Cost_Price=rs.getDouble("Cost_Price");
            	double NBV=rs.getDouble("NBV");
            	Date Dep_End_Date=rs.getDate("Dep_End_Date");
            	double Residual_Value=rs.getDouble("Residual_Value");
            	String Authorized_By=rs.getString("Authorized_By");
            	String Wh_Tax=rs.getString("Wh_Tax");
            	double Wh_Tax_Amount=rs.getDouble("Wh_Tax_Amount");
            	String Req_Redistribution=rs.getString("Req_Redistribution");
            	Date Posting_Date=rs.getDate("Posting_Date");
            	Date Effective_Date=rs.getDate("Effective_Date");
            	String Purchase_Reason=rs.getString("Purchase_Reason");
            	int Useful_Life=rs.getInt("Useful_Life");
            	int Total_Life=rs.getInt("Total_Life");
            	int Location=rs.getInt("Location");
            	int Remaining_Life=rs.getInt("Remaining_Life");
            	double Vatable_Cost=rs.getDouble("Vatable_Cost");
            	double Vat=rs.getDouble("Vat");
            	String Req_Depreciation=rs.getString("Req_Depreciation");
            	String Subject_TO_Vat=rs.getString("Subject_TO_Vat");
            	String Who_TO_Rem=rs.getString("Who_TO_Rem");
            	String Email1=rs.getString("Email1");
            	String Who_To_Rem_2=rs.getString("Who_To_Rem_2");
            	String Email2=rs.getString("Email2");
            	String Raise_Entry=rs.getString("Raise_Entry");
            	double Dep_Ytd=rs.getDouble("Dep_Ytd");
            	String Section=rs.getString("Section");
            	int State=rs.getInt("State");
            	int Driver=rs.getInt("Driver");
            	String Spare_2=rs.getString("Spare_2");
            	int User_ID=rs.getInt("User_ID");
            	Date Date_Disposed=rs.getDate("Date_Disposed");
            	int PROVINCE=rs.getInt("PROVINCE");
            	String Multiple=rs.getString("Multiple");
            	Date WAR_START_DATE=rs.getDate("WAR_START_DATE");
            	int WAR_MONTH=rs.getInt("WAR_MONTH");
            	Date WAR_EXPIRY_DATE=rs.getDate("WAR_EXPIRY_DATE");
            	Date Last_Dep_Date=rs.getDate("Last_Dep_Date");
            	String BRANCH_CODE=rs.getString("BRANCH_CODE");
            	String SECTION_CODE=rs.getString("SECTION_CODE");
            	String DEPT_CODE=rs.getString("DEPT_CODE");
            	String CATEGORY_CODE=rs.getString("CATEGORY_CODE");
            	double AMOUNT_PTD=rs.getDouble("AMOUNT_PTD");
            	double AMOUNT_REM=rs.getDouble("AMOUNT_REM");
            	String PART_PAY=rs.getString("PART_PAY");
            	String FULLY_PAID=rs.getString("FULLY_PAID");
            	int GROUP_ID=rs.getInt("GROUP_ID");
            	String BAR_CODE=rs.getString("BAR_CODE");
            	String SBU_CODE=rs.getString("SBU_CODE");
            	String LPO=rs.getString("LPO");
            	String supervisor=rs.getString("supervisor");

            	 app=new ComponentViewDetail(id,parentAssetId,parentCompId,assetId,
                     	category,description,serialNumber,make,model,additionalField,
                    	status,component,cost,assigned,depreciation,Registration_No,
                    	Branch_ID,Dept_ID,Section_id,Vendor_AC,Date_purchased,Dep_Rate,
                    	Asset_Engine_No,Supplier_Name,Asset_User,Asset_Maintenance,Accum_Dep,
                    	Monthly_Dep,Cost_Price,NBV,Dep_End_Date,Residual_Value,Authorized_By,
                    	Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason,Useful_Life,
                    	Total_Life,Location,Remaining_Life,Vatable_Cost,Vat,Req_Depreciation,
                    	Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry,
                    	Dep_Ytd,Section,State,Driver,Spare_2,User_ID,Date_Disposed,
                    	PROVINCE,Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,
                    	BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,
                    	AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,LPO,supervisor);
            	 collection.add(app);


            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch AM_AD_COMPONENT->" +
                    ex.getMessage());
        } finally {
        	dbConnection.closeConnection(con, ps);
        }

        return collection;

    }


public ArrayList selectComponent(String  comp_Id) throws SQLException
{
	ArrayList collection = new ArrayList();
	String FINDER_QUERY = "SELECT  * from AM_AD_COMPONENT WHERE comp_Id = ? ";
	
	try(Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(FINDER_QUERY)){
		
		  ps.setString(1, comp_Id); 

	        try (ResultSet rs = ps.executeQuery()) {
	        	while (rs.next()) {
	            	   ComponentViewDetail app = new ComponentViewDetail(
	                           rs.getString("COMP_ID"),
	                           rs.getString("PARENT_ASSET_ID"),
	                           rs.getString("PARENT_COMP_ID"),
	                           rs.getString("ASSET_ID"),
	                           rs.getString("CATEGORY"),
	                           rs.getString("DESCRIPTION"),
	                           rs.getString("SERIAL_NO"),
	                           rs.getString("MAKE"),
	                           rs.getString("MODEL"),
	                           rs.getString("ADDITIONAL_FIELD"),
	                           rs.getString("STATUS"),
	                           rs.getString("COMPONENT"),
	                           rs.getDouble("COST"),
	                           rs.getDouble("ASSIGNED"),
	                           rs.getDouble("DEPRECIATION"),
	                           rs.getString("Registration_No"),
	                           rs.getInt("Branch_ID"),
	                           rs.getInt("Dept_ID"),
	                           rs.getInt("Section_id"),
	                           rs.getString("Vendor_AC"),
	                           rs.getDate("Date_purchased"),
	                           rs.getDouble("Dep_Rate"),
	                           rs.getString("Asset_Engine_No"),
	                           rs.getInt("Supplier_Name"),
	                           rs.getString("Asset_User"),
	                           rs.getInt("Asset_Maintenance"),
	                           rs.getDouble("Accum_Dep"),
	                           rs.getDouble("Monthly_Dep"),
	                           rs.getDouble("Cost_Price"),
	                           rs.getDouble("NBV"),
	                           rs.getDate("Dep_End_Date"),
	                           rs.getDouble("Residual_Value"),
	                           rs.getString("Authorized_By"),
	                           rs.getString("Wh_Tax"),
	                           rs.getDouble("Wh_Tax_Amount"),
	                           rs.getString("Req_Redistribution"),
	                           rs.getDate("Posting_Date"),
	                           rs.getDate("Effective_Date"),
	                           rs.getString("Purchase_Reason"),
	                           rs.getInt("Useful_Life"),
	                           rs.getInt("Total_Life"),
	                           rs.getInt("Location"),
	                           rs.getInt("Remaining_Life"),
	                           rs.getDouble("Vatable_Cost"),
	                           rs.getDouble("Vat"),
	                           rs.getString("Req_Depreciation"),
	                           rs.getString("Subject_TO_Vat"),
	                           rs.getString("Who_TO_Rem"),
	                           rs.getString("Email1"),
	                           rs.getString("Who_To_Rem_2"),
	                           rs.getString("Email2"),
	                           rs.getString("Raise_Entry"),
	                           rs.getDouble("Dep_Ytd"),
	                           rs.getString("Section"),
	                           rs.getInt("State"),
	                           rs.getInt("Driver"),
	                           rs.getString("Spare_2"),
	                           rs.getInt("User_ID"),
	                           rs.getDate("Date_Disposed"),
	                           rs.getInt("PROVINCE"),
	                           rs.getString("Multiple"),
	                           rs.getDate("WAR_START_DATE"),
	                           rs.getInt("WAR_MONTH"),
	                           rs.getDate("WAR_EXPIRY_DATE"),
	                           rs.getDate("Last_Dep_Date"),
	                           rs.getString("BRANCH_CODE"),
	                           rs.getString("SECTION_CODE"),
	                           rs.getString("DEPT_CODE"),
	                           rs.getString("CATEGORY_CODE"),
	                           rs.getDouble("AMOUNT_PTD"),
	                           rs.getDouble("AMOUNT_REM"),
	                           rs.getString("PART_PAY"),
	                           rs.getString("FULLY_PAID"),
	                           rs.getInt("GROUP_ID"),
	                           rs.getString("BAR_CODE"),
	                           rs.getString("SBU_CODE"),
	                           rs.getString("LPO"),
	                           rs.getString("supervisor")
	                       );
	                       collection.add(app);
	            }
	        }
		
	}catch (Exception ex) {
        System.out.println("WARNING: cannot fetch AM_AD_COMPONENT -> " + ex.getMessage());
    }

	
	
	return collection;
}




public String getAssetTransInfoOld(int tran_id){
   String query = "select process_status from am_asset_approval where transaction_id = "+tran_id;
   String result="";
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);

            }

        } catch (Exception ex) {
            System.out.println("AssetRecordsBean WARN: Error fetching getAssetTransInfo(int tran_id) ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;


   } //getTransactionDetails()

public String getAssetTransInfo(int tran_id){
	   String query = "select process_status from am_asset_approval where transaction_id = ? ";
	   String result="";
	   
	   try(Connection con = dbConnection.getConnection("legendPlus");
	            PreparedStatement ps = con.prepareStatement(query)){
		   ps.setInt(1, tran_id); 

	        try (ResultSet rs = ps.executeQuery()) {
	        	result = rs.getString("process_status");
	        }
		   
	   }catch(Exception e) {
		   System.out.println("AssetRecordsBean WARN: Error fetching getAssetTransInfo(int tran_id) ->" + e); 
	   }
	   
	        return result;

	   } 

public int getNumOfTransactionLevelOld(String levelCode){
   String query = "select level from approval_level_setup where code = '"+levelCode+"'";
   int result=100;
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);

            }

        } catch (Exception ex) {
            System.out.println("AssetRecordsBean: getNumOfTransactionLevel(String levelCode) WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return result;
   } //getNumOfTransactionLevel()

public int getNumOfTransactionLevel(String levelCode) {
    int result = 100; 
    String query = "SELECT level FROM approval_level_setup WHERE code = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, levelCode);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getInt(1);
            }
        }

    } catch (SQLException ex) {
        System.out.println("AssetRecordsBean: getNumOfTransactionLevel WARN: Error fetching level -> " + ex.getMessage());
        ex.printStackTrace();
    }

    return result;
}



public boolean updateNewAssetStatusOld(String assetId) throws Exception {

        String query = "update am_asset SET  asset_status = 'ACTIVE' ,Finacle_Posted_Date= ? where asset_id ='" +assetId+"'";
         boolean done = true;
        Connection con = null;
        PreparedStatement ps = null;

        try {



            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setTimestamp(1, dbConnection.getDateTime(new java.util.Date()));
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: updateNewAssetStatus: WARN:Error updating asset->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return done;

    }

public boolean updateNewAssetStatus(String assetId) throws Exception {
    String query = "UPDATE am_asset SET asset_status = 'ACTIVE', Finacle_Posted_Date = ? WHERE asset_id = ?";
    boolean done = true;

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setTimestamp(1, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(2, assetId);

        int updatedRows = ps.executeUpdate();
        if (updatedRows == 0) {
            done = false; 
            System.out.println("AssetRecordsBean: updateNewAssetStatus: WARN: No asset found with ID " + assetId);
        }

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateNewAssetStatus: WARN: Error updating asset -> " + ex.getMessage());
        ex.printStackTrace();
    }

    return done;
}




public boolean insertComponent(String parentAssetId, String parentCompId,
            String category, String description, String serialNumber, String make, String model,
            String additionalField,String Componet,double cost,double assigned,double depreciation,
            String registration_no,String branch_id,String department_id,String section_id,String  vendor_account,
            String date_of_purchase,String  getDepreciationRate,String engine_number,String supplied_by,
            String user,String  maintained_by,int A,int B,String costPrice1,double costPrice2,String depreciation_end_date,
            String residual_value,String authorized_by,String wh_tax_cb,String wh_tax_amount,String require_depreciation,
            String depreciation_start_date,String reason,String C,String computeTotalLife,String location,String computeTotalLife2,
            String vatable_cost,String vat_amount,String require_redistribution,String subject_to_vat,String who_to_remind,
            String email_1,String who_to_remind_2,String email2,String raise_entry,String D,String section,String state,String driver,
            String spare_2,String user_id,String  province,String  multiple,String warrantyStartDate,int noOfMonths,String expiryDate,
            String getBranchCode,String getSectionCode,String getDeptCode,String getCategoryCode,String amountPTD,double  amountPTD2,
            String partPAY,String fullyPAID,String  group_id,String  bar_code,String sbu_code,String lpo,String getSupervisor ) throws Exception, Throwable
    {
    	String assetId = comp.createComponentAssetIdDelimiter(parentAssetId,description);
       
        boolean done = true;


        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (sub_category_id == null || sub_category_id.equals("")) {
            sub_category_id = "0";
        }

        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }


        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }

        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");



        String createQuery = "INSERT INTO AM_AD_COMPONENT ( PARENT_ASSET_ID,PARENT_COMP_ID ,ASSET_ID ,CATEGORY    " +
                             "  ,DESCRIPTION,SERIAL_NO,MAKE,MODEL ,ADDITIONAL_FIELD,STATUS,COMPONENT ,COST    " +
                             "  ,ASSIGNED,DEPRECIATION " +
                             "  ,Registration_No,Branch_ID,Dept_ID,Section_id,Vendor_AC    " +
                             " ,Date_purchased,Dep_Rate ,Asset_Engine_No ,Supplier_Name,Asset_User,Asset_Maintenance    " +
                             " ,Accum_Dep ,Monthly_Dep,Cost_Price ,NBV ,Dep_End_Date,Residual_Value ,Authorized_By    " +
                             " ,Wh_Tax   ,Wh_Tax_Amount  ,Req_Redistribution    " +
                             " ,Posting_Date  ,Effective_Date  ,Purchase_Reason ,Useful_Life ,Total_Life    " +
                             " ,Location  ,Remaining_Life  ,Vatable_Cost  ,Vat ,Req_Depreciation    " +
                             " ,Subject_TO_Vat   ,Who_TO_Rem  ,Email1 ,Who_To_Rem_2  ,Email2    " +
                             " ,Raise_Entry ,Dep_Ytd  ,Section  ,State ,Driver  ,Spare_2    " +
                             " ,User_ID  ,Date_Disposed ,PROVINCE ,Multiple  ,WAR_START_DATE    " +
                             " ,WAR_MONTH ,WAR_EXPIRY_DATE ,Last_Dep_Date ,BRANCH_CODE ,SECTION_CODE    " +
                             " ,DEPT_CODE ,CATEGORY_CODE  ,AMOUNT_PTD ,AMOUNT_REM  ,PART_PAY    " +
                             " ,FULLY_PAID  ,GROUP_ID ,BAR_CODE  ,SBU_CODE ,LPO ,supervisor,asset_code ) " +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  " +
                             " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(createQuery)) {
            double costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
            int Accum_Dep = 0;//Integer.parseInt(String.valueOf(assigned));
            int Monthly_Dep=0;
            ps.setString(1, parentAssetId);
            ps.setInt(2, new Integer(parentCompId).intValue());
            ps.setString(3, assetId);
            ps.setInt(4, new Integer(category).intValue());
            ps.setString(5, description.toUpperCase());
            ps.setString(6, serialNumber);
            ps.setString(7, make);
            ps.setString(8, model);
            ps.setString(9, additionalField);
            ps.setString(10, "ACTIVE");
            ps.setString(11, Componet);
            ps.setDouble(12, cost);
            ps.setDouble(13, assigned);
            ps.setDouble(14, depreciation);
            ps.setString(15, registration_no);
            ps.setInt(16, Integer.parseInt(branch_id));
            ps.setInt(17, Integer.parseInt(department_id));
            ps.setInt(18, Integer.parseInt(section_id));
            ps.setString(19, vendor_account);
            ps.setDate(20, dbConnection.dateConvert(date_of_purchase));

            ps.setString(21, getDepreciationRate(category));
            ps.setString(22, engine_number);
            ps.setInt(23, Integer.parseInt(supplied_by));
            ps.setString(24, user);
            ps.setInt(25, Integer.parseInt(maintained_by));
            ps.setInt(26, Accum_Dep);
            ps.setInt(27, Monthly_Dep);
            ps.setDouble(28, costPrice);
            ps.setDouble(29, costPrice);
            ps.setDate(30, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(31, Double.parseDouble(residual_value));
            ps.setString(32, authorized_by);
            ps.setString(33, wh_tax_cb);
            ps.setDouble(34, Double.parseDouble(wh_tax_amount));
            ps.setString(35, require_depreciation);

            ps.setTimestamp(36, dbConnection.getDateTime(new java.util.Date()));
            ps.setDate(37, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(38, reason);
            ps.setString(39, "0");
            ps.setString(40, computeTotalLife(getDepreciationRate(category)));
            ps.setInt(41, Integer.parseInt(location));
            ps.setString(42, computeTotalLife(getDepreciationRate(category)));
            ps.setDouble(43, Double.parseDouble(vatable_cost));
            ps.setDouble(44, Double.parseDouble(vat_amount));
            ps.setString(45, require_redistribution);
            ps.setString(46, subject_to_vat);
            ps.setString(47, who_to_remind);
            ps.setString(48, email_1);
            ps.setString(49, who_to_remind_2);
            ps.setString(50, email2);
            ps.setString(51, raise_entry);
            ps.setString(52, "0");
            ps.setString(53, section);
            ps.setInt(54, Integer.parseInt(state));
            ps.setInt(55, Integer.parseInt(driver));
            ps.setString(56, spare_2);
            ps.setString(57, user_id);
            ps.setTimestamp(58, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(59, province);
            ps.setString(60, multiple);
            ps.setDate(61, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(62, new Integer(noOfMonths).intValue());
            ps.setDate(63, dbConnection.dateConvert(expiryDate));
            ps.setTimestamp(64, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(65, code.getBranchCode(branch_id));
            ps.setString(66, code.getSectionCode(section_id));
            ps.setString(67, code.getDeptCode(department_id));
            ps.setString(68, code.getCategoryCode(category));
            ps.setDouble(69, Double.parseDouble(amountPTD));
            ps.setDouble(70, (costPrice-Double.parseDouble(amountPTD)));
            ps.setString(71, partPAY);
            ps.setString(72, fullyPAID);
            ps.setString(73, group_id);
            ps.setString(74, bar_code);
            ps.setString(75,sbu_code);
            ps.setString(76, lpo);
            ps.setString(77,getSupervisor);
            ps.setInt(78,assetCode);

           
            ps.execute();


        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error creating asset->" + ex);
        } 

        return done;
    }



 
 public void updateComponentStatusOld(String parentAssetId,String description2,double cost)
 {
 	String query_r ="update AM_COMPONENT_DIST set created = ? where ASSET_ID=? AND COMPONENT_EXP_ACCT=? AND COST_VALUE=?  ";

 	        Connection con = null;
 	        PreparedStatement ps = null;
 	        //ResultSet rs = null;

 	try {
 	    con = dbConnection.getConnection("legendPlus");


 	ps = con.prepareStatement(query_r);



 	            ps.setString(1,"Y");
 	            ps.setString(2, parentAssetId);
 	            ps.setString(3,description2);
 	            ps.setDouble(4,cost);


// 	            System.out.println("parentAssetId "+parentAssetId);
// 	            System.out.println("description2 "+description2);
// 	            System.out.println("cost "+cost);


 	            int i =ps.executeUpdate();


 	        } catch (Exception ex) {

 	            System.out.println("update not successful" + ex);
 	        } finally {
 	            dbConnection.closeConnection(con, ps);
 	        }


 	}
 
 
 public void updateComponentStatus(String parentAssetId,String description2,double cost)
 {
 	String query_r ="update AM_COMPONENT_DIST set created = ? where ASSET_ID=? AND COMPONENT_EXP_ACCT=? AND COST_VALUE=?  ";      

 	try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query_r)) {


 	            ps.setString(1,"Y");
 	            ps.setString(2, parentAssetId);
 	            ps.setString(3,description2);
 	            ps.setDouble(4,cost);

 	            int i =ps.executeUpdate();

 	        } catch (Exception ex) {

 	            System.out.println("update not successful" + ex);
 	        } 


 	}

public String[] setApprovalDataUpdateOld(String id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE,Asset_Status from am_assetUpdate where asset_id ='" +id+"'"; ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
result[9] = "Asset Update";
result[10] = "P";

//    System.out.println("the value of supervisor is *************setApprovalDataUpdate************************" + result[2]);
//result[11] = timeInstance();
        return result;

}//setApprovalData()

public String[] setApprovalDataUpdate(String id){

		   String[] result= new String[12];

	         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE,Asset_Status from am_assetUpdate where asset_id = ? "; ;



	         try (Connection con = dbConnection.getConnection("legendPlus");
	                 PreparedStatement ps = con.prepareStatement(query)) {
	        	 
	        	 ps.setString(1, id);
	           
	            try(ResultSet rs = ps.executeQuery();){
	            while (rs.next()) {
	                result[0] = rs.getString("asset_id");
	                result[1]= rs.getString("user_ID");
	                result[2] = rs.getString("supervisor");
	               result[3] = rs.getString("Cost_Price");
	               result[4] = rs.getString("Posting_Date");
	               result[5] = rs.getString("description");
	               result[6] = rs.getString("effective_date");
	               result[7] = rs.getString("BRANCH_CODE");
	               result[8] = rs.getString("Asset_Status");

	            }
	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
	        } 
	result[9] = "Asset Update";
	result[10] = "P";

//	    System.out.println("the value of supervisor is *************setApprovalDataUpdate************************" + result[2]);
	//result[11] = timeInstance();
	        return result;

	}//setApprovalData()


public String[] setApprovalDataUpdate2Old(String id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date,description," +
                 "effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id ='" +id+"'"; ;



        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = rs.getString(1);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
result[9] = "Asset Status Update";
result[10] = "P";

   // System.out.println("the value of supervisor is *************setApprovalDataUpdate************************" + result[2]);
//result[11] = timeInstance();
        return result;

}

public String[] setApprovalDataUpdate2(String id){

	   String[] result= new String[12];

	   String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date,description," +
               "effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id = ? " ;

      try (Connection con = dbConnection.getConnection("legendPlus");
              PreparedStatement ps = con.prepareStatement(query)) {
     	 
     	 ps.setString(1, id);
        
         try(ResultSet rs = ps.executeQuery();){
         while (rs.next()) {
             result[0] = rs.getString("asset_id");
             result[1]= rs.getString("user_ID");
             result[2] = rs.getString("supervisor");
            result[3] = rs.getString("Cost_Price");
            result[4] = rs.getString("Posting_Date");
            result[5] = rs.getString("description");
            result[6] = rs.getString("effective_date");
            result[7] = rs.getString("BRANCH_CODE");
            result[8] = rs.getString("Asset_Status");

         }
         }

     } catch (Exception ex) {
    	 System.out.println("WARN: Error fetching CategoryCode ->" + ex);
     } 
result[9] = "Asset Update";
result[10] = "P";

// System.out.println("the value of supervisor is *************setApprovalDataUpdate************************" + result[2]);
//result[11] = timeInstance();
     return result;

}


public boolean insertInToAssetRecordTemp(String assetId,String userId) throws Exception {

        String query = "insert into am_assetUpdate " +
                       "(REGISTRATION_NO,DESCRIPTION,VENDOR_AC," +
                       "DATE_PURCHASED,ASSET_MAKE,ASSET_MODEL," +
                       "ASSET_SERIAL_NO,ASSET_ENGINE_NO,SUPPLIER_NAME," +
                       "ASSET_USER,ASSET_MAINTENANCE,DEP_END_DATE," +
                       "RESIDUAL_VALUE,AUTHORIZED_BY,POSTING_DATE," +
                       "EFFECTIVE_DATE,PURCHASE_REASON,LOCATION," +
                       "WHO_TO_REM,EMAIL1,WHO_TO_REM_2," +
                       "EMAIL2,RAISE_ENTRY,SECTION,STATE,DRIVER," +
                       "SPARE_1 ,SPARE_2 ,ASSET_STATUS ,PROVINCE ," +
                       "DEPT_ID ,req_redistribution ,Req_Depreciation , " +
                       "SUBJECT_TO_VAT,VATABLE_COST,VAT ," +
                       "wh_tax ,wh_tax_amount ,BRANCH_ID ,MULTIPLE ,COST_PRICE," +
                       "BRANCH_CODE,DEPT_CODE,SECTION_CODE ,CATEGORY_CODE,WAR_START_DATE," +
                       "WAR_MONTH,WAR_EXPIRY_DATE, BAR_CODE ,SBU_CODE , LPO , supervisor, defer_pay ,asset_id,user_id,category_id,dep_rate,wht_percent,"+
                       "SPARE_3 ,SPARE_4 ,SPARE_5 ,SPARE_6,sub_category_id,SUB_CATEGORY_CODE) "+
                       "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

      
        boolean done = true;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }


        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")|| driver.equals("-1")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }


        if (bar_code == null || bar_code.equals("")) {
            bar_code = "0";
        }

        if (sbu_code == null || sbu_code.equals("")) {
            sbu_code = "0";
        }

        if (lpo == null || lpo.equals("")) {
            lpo = "0";
        }

         if (depreciation_rate == null || depreciation_rate.equals("")) {
            depreciation_rate = "0";
        }

        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");

        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query)) {

            double costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);

           
            ps.setString(1, registration_no);
            ps.setString(2, description.toUpperCase());
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(5, Integer.parseInt(make));
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setString(9, supplied_by);
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(13, Double.parseDouble(residual_value));
            ps.setString(14, authorized_by);
            ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
            ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(17, reason);
            ps.setInt(18, Integer.parseInt(location));
            ps.setString(19, who_to_remind);
            ps.setString(20, email_1);
            ps.setString(21, who_to_remind_2);
            ps.setString(22, email2);
            ps.setString(23, raise_entry);
            ps.setString(24, section);
            ps.setInt(25, Integer.parseInt(state));
            ps.setInt(26, Integer.parseInt(driver));
            ps.setString(27, spare_1);
            ps.setString(28, spare_2);
            ps.setString(29, status);
            ps.setString(30, province);
            ps.setInt(31, Integer.parseInt(department_id));
            ps.setString(32, require_redistribution);
            ps.setString(33, require_depreciation);
            ps.setString(34, subject_to_vat);
            ps.setDouble(35, Double.parseDouble(vatable_cost));
            ps.setDouble(36, Double.parseDouble(vat_amount));
            ps.setString(37, wh_tax_cb);
            ps.setDouble(38, Double.parseDouble(wh_tax_amount));
            ps.setInt(39, Integer.parseInt(branch_id));
            ps.setString(40, multiple);
            ps.setDouble(41, costPrice);
            ps.setString(42, code.getBranchCode(branch_id));
            ps.setString(43, code.getDeptCode(department_id));
            ps.setString(44, code.getSectionCode(section_id));
            ps.setString(45, code.getCategoryCode(category_id));
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49, bar_code);
            ps.setString(50,sbu_code);
            ps.setString(51, lpo);
            ps.setString(52,getSupervisor());
            ps.setString(53,deferPay);
            ps.setString(54, assetId);
            ps.setString(55,userId);
            ps.setString(56, category_id);
            ps.setString(57, depreciation_rate);
            ps.setDouble(58, Double.parseDouble(selectTax));
            ps.setString(59, spare_1);
            ps.setString(60, spare_2);
            ps.setString(61, spare_1);
            ps.setString(62, spare_2);
            ps.setString(63, sub_category_id);
            ps.setString(64, code.getSubCategoryCode(sub_category_id));
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: insertInToAssetRecordTemp():WARN:Error inserting to am_assetUpdate table->" + ex);
        } 
        
        return done;

    }

public boolean insertInToAssetRecordTempOld(String assetId,String userId,String sup_id,String status) throws Exception {

        String query = "insert into am_assetUpdate " +
                       "(ASSET_ID,USER_ID,SUPERVISOR,ASSET_STATUS,BRANCH_CODE,CATEGORY_CODE)"+
                       "values(?,?,?,?,?,?)";

       
        boolean done = true;
         
        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query)) {
        	
           
            ps.setString(1, assetId);
            ps.setString(2, userId);
            ps.setString(3, sup_id);
            ps.setString(4,status);
            ps.setString(5,"");
            ps.setString(6,"");
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: insertInToAssetRecordTemp():WARN:Error inserting to am_assetUpdate table->" + ex);
        } 
        
        return done;

    }


public boolean insertInToAssetRecordTemp(String assetId, String userId, String sup_id, String status) {
    String query = "INSERT INTO am_assetUpdate " +
                   "(ASSET_ID, USER_ID, SUPERVISOR, ASSET_STATUS, BRANCH_CODE, CATEGORY_CODE) " +
                   "VALUES (?, ?, ?, ?, ?, ?)";

    boolean done = true;

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, assetId != null ? assetId.trim() : "");
        ps.setString(2, userId != null ? userId.trim() : "");
        ps.setString(3, sup_id != null ? sup_id.trim() : "");
        ps.setString(4, status != null ? status.trim() : "");
        ps.setString(5, "");  
        ps.setString(6, ""); 

        ps.executeUpdate();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: insertInToAssetRecordTemp(): WARN: Error inserting assetId " + assetId + " -> " + ex);
    }

    return done;
}


public void updateComponentStatusOld(String parentAssetId,String description2,double cost,String DepSeqValue)
    {
    	String query_r ="update AM_COMPONENT_DIST set created = ? where ASSET_ID=? AND COMPONENT_EXP_ACCT=? AND COST_VALUE=? AND SEQUENCE_NO=? ";

    	        Connection con = null;
    	        PreparedStatement ps = null;
    	        //ResultSet rs = null;

    	try {
    	    con = dbConnection.getConnection("legendPlus");


    	ps = con.prepareStatement(query_r);



    	            ps.setString(1,"Y");
    	            ps.setString(2, parentAssetId);
    	            ps.setString(3,description2);
    	            ps.setDouble(4,cost);
    	            ps.setString(5, DepSeqValue);


//    	            System.out.println("parentAssetId "+parentAssetId);
//    	            System.out.println("description2 "+description2);
//    	            System.out.println("cost "+cost);


    	            int i =ps.executeUpdate();


    	        } catch (Exception ex) {

    	            System.out.println("update not successful" + ex);
    	        } finally {
    	            dbConnection.closeConnection(con, ps);
    	        }


    	}

public void updateComponentStatus(String parentAssetId,String description2,double cost,String DepSeqValue)
{
	String query_r ="update AM_COMPONENT_DIST set created = ? where ASSET_ID=? AND COMPONENT_EXP_ACCT=? AND COST_VALUE=? AND SEQUENCE_NO=? ";

	      

	 try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query_r)) {



	            ps.setString(1,"Y");
	            ps.setString(2, parentAssetId);
	            ps.setString(3,description2);
	            ps.setDouble(4,cost);
	            ps.setString(5, DepSeqValue);


	            int i =ps.executeUpdate();


	        } catch (Exception ex) {

	            System.out.println("update not successful" + ex);
	        } 


	}

public int changeAssetIDOld(String oldAssetId,String newAssetId)
    {
    int value =0;

    String query_r ="update am_asset set asset_id = ? where ASSET_ID=? ";

    	        Connection con = null;
    	        PreparedStatement ps = null;
    	        //ResultSet rs = null;

    	try {
    	    con = dbConnection.getConnection("legendPlus");


    	ps = con.prepareStatement(query_r);



    	            ps.setString(1,newAssetId);
    	            ps.setString(2, oldAssetId);

    	            value =ps.executeUpdate();


    	        } catch (Exception ex) {

    	            System.out.println("AssetRecordsBean: changeAssetID(): asset id update not successful" + ex);
    	        } finally {
    	            dbConnection.closeConnection(con, ps);
    	        }

return value;
    	}


public int changeAssetID(String oldAssetId,String newAssetId)
{
int value =0;

String query_r ="update am_asset set asset_id = ? where ASSET_ID=? ";

	        try (Connection con = dbConnection.getConnection("legendPlus");
	   	         PreparedStatement ps = con.prepareStatement(query_r)) {

	            ps.setString(1,newAssetId);
	            ps.setString(2, oldAssetId);

	            value =ps.executeUpdate();


	        } catch (Exception ex) {

	            System.out.println("AssetRecordsBean: changeAssetID(): asset id update not successful" + ex);
	        } 

return value;
	}


public String setPendingTrans2Old(String[] a, String code){
//	System.out.println("====code 1====> "+code);
    String mtid_id ="";
        int transaction_level=0;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");


            /////////////To get transaction level
             ps = con.prepareStatement(tranLevelQuery);
              rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);

            ps.execute();

            mtid_id = mtid;
        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans2 Two 2>>>>>>" + er);

        }finally{
        dbConnection.closeConnection(con, ps);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";



return mtid_id;


    }//setPendingTrans2()


public String setPendingTrans2(String[] a, String code) {
    String mtid_id = "";
    int transaction_level = 0;

    String pq = "INSERT INTO am_asset_approval(" +
            "asset_id, user_id, super_id, amount, posting_date, description, " +
            "effective_date, branchCode, asset_status, tran_type, process_status, tran_sent_time, " +
            "transaction_id, batch_id, transaction_level) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String tranLevelQuery = "SELECT level FROM approval_level_setup WHERE code = ?";

    try (Connection con = dbConnection.getConnection("legendPlus")) {


        try (PreparedStatement psLevel = con.prepareStatement(tranLevelQuery)) {
            psLevel.setString(1, code);
            try (ResultSet rs = psLevel.executeQuery()) {
                if (rs.next()) {
                    transaction_level = rs.getInt(1);
                }
            }
        }

        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
        mtid_id = new ApplicationHelper().getGeneratedId("am_asset_approval");


        try (PreparedStatement ps = con.prepareStatement(pq)) {
            ps.setString(1, a[0] != null ? a[0] : "");
            ps.setString(2, a[1] != null ? a[1] : "");
            ps.setString(3, a[2] != null ? a[2] : "");
            ps.setDouble(4, a[3] != null ? Double.parseDouble(a[3]) : 0);
            ps.setTimestamp(5, dbConnection.getDateTime(new java.util.Date())); // posting_date
            ps.setString(6, a[5] != null ? a[5] : "");
            ps.setDate(7, a[6] != null ? dbConnection.dateConvert(a[6]) : null); // effective_date
            ps.setString(8, a[7] != null ? a[7] : "");
            ps.setString(9, a[8] != null ? a[8] : "");
            ps.setString(10, a[9] != null ? a[9] : "");
            ps.setString(11, a[10] != null ? a[10] : "");
            ps.setString(12, timer.format(new java.util.Date())); // tran_sent_time
            ps.setString(13, mtid_id); // transaction_id
            ps.setString(14, mtid_id); // batch_id
            ps.setInt(15, transaction_level);

            ps.executeUpdate();
        }

    } catch (Exception er) {
        System.out.println("AssetRecordBeans:setPendingTrans2() >>>>> " + er);
        er.printStackTrace();
    }

    return mtid_id;
}



//this method is to update am_group_asset_main

//lanre update asset with asset_id and user_id
public boolean updateAsset(String assetId,String userId) throws Exception {

    String query = "update am_asset set " +
                   "REGISTRATION_NO=?,DESCRIPTION=?,VENDOR_AC=?," +
                   "DATE_PURCHASED=?,ASSET_MAKE=?,ASSET_MODEL=?," +
                   "ASSET_SERIAL_NO=?,ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                   "ASSET_USER=?,ASSET_MAINTENANCE=?,DEP_END_DATE=?," +
                   "RESIDUAL_VALUE=?,AUTHORIZED_BY=?,POSTING_DATE=?," +
                   "EFFECTIVE_DATE=?,PURCHASE_REASON=?,LOCATION=?," +
                   "WHO_TO_REM=?,EMAIL1=?,WHO_TO_REM_2=?," +
                   "EMAIL2=?,RAISE_ENTRY=?,SECTION=?,STATE=?,DRIVER=?," +
                   "SPARE_1=? ,SPARE_2=? ,ASSET_STATUS=? ,PROVINCE=? ," +
                   "DEPT_ID=? ,req_redistribution=? ,Req_Depreciation=? , " +
                   "SUBJECT_TO_VAT=?,VATABLE_COST=?,VAT=? ," +
                   "wh_tax=? ,wh_tax_amount=? ,BRANCH_ID=? ,MULTIPLE=? ,COST_PRICE=?," +
                   "BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? ,CATEGORY_CODE=?,WAR_START_DATE=?," +
                   "WAR_MONTH=?,WAR_EXPIRY_DATE=?, BAR_CODE=? ,SBU_CODE=? , LPO=? , supervisor=?, defer_pay=? ,category_id=?,"+
                   "dep_rate=?,wht_percent=?,SPARE_3=? ,SPARE_4=? ,SPARE_5=? ,SPARE_6=?,sub_category_id=?,SUB_CATEGORY_CODE=?,TRANPORT_COST = ?,OTHER_COST = ?  where asset_id=? and user_id=? " ;

   
    boolean done = true;
    /*if (require_redistribution.equalsIgnoreCase("Y")) {
        status = "Z";
             }*/
    if (make == null || make.equals("")) {
        make = "0";
    }
    if (maintained_by == null || maintained_by.equals("")) {
        maintained_by = "0";
    }
    if (location == null || location.equals("")) {
        location = "0";
    }
    if (driver == null || driver.equals("")|| driver.equals("-1")) {
        driver = "0";
    }
    if (state == null || state.equals("")) {
        state = "0";
    }
    if (department_id == null || department_id.equals("")) {
        department_id = "0";
    }
    if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
    if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
    if (transport_cost == null || transport_cost.equals("")) {
    	transport_cost = "0.0";
    }
    if (other_cost == null || other_cost.equals("")) {
    	other_cost = "0.0";
    }
    if (wh_tax_amount == null || wh_tax_amount.equals("")) {
        wh_tax_amount = "0";
    }
    if (branch_id == null || branch_id.equals("")) {
        branch_id = "0";
    }
    if (residual_value == null || residual_value.equals("")) {
        residual_value = "0";
    }
    if (section_id == null || section_id.equals("")) {
        section_id = "0";
    }


    if (bar_code == null || bar_code.equals("")) {
        bar_code = "0";
    }

    if (sbu_code == null || sbu_code.equals("")) {
        sbu_code = "0";
    }

    if (lpo == null || lpo.equals("")) {
        lpo = "0";
    }

     if (depreciation_rate == null || depreciation_rate.equals("")) {
        depreciation_rate = "0";
    }

    vat_amount = vat_amount.replaceAll(",", "");
    vatable_cost = vatable_cost.replaceAll(",", "");
    wh_tax_amount = wh_tax_amount.replaceAll(",", "");
    residual_value = residual_value.replaceAll(",", "");

    try (Connection con = dbConnection.getConnection("legendPlus");
  	         PreparedStatement ps = con.prepareStatement(query)) {

        double costPrice = Double.parseDouble(vat_amount) +
                           Double.parseDouble(vatable_cost);
//System.out.println("vatable_cost "+vatable_cost);
       
        ps.setString(1, registration_no);
        ps.setString(2, description.toUpperCase());
        ps.setString(3, vendor_account);
        ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
        ps.setInt(5, Integer.parseInt(make));
        ps.setString(6, model);
        ps.setString(7, serial_number);
        ps.setString(8, engine_number);
        ps.setString(9, supplied_by);
        ps.setString(10, user);
        ps.setInt(11, Integer.parseInt(maintained_by));
        ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
        ps.setDouble(13, Double.parseDouble(residual_value));
        ps.setString(14, authorized_by);
        ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
        ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
        ps.setString(17, reason);
        ps.setInt(18, Integer.parseInt(location));
        ps.setString(19, who_to_remind);
        ps.setString(20, email_1);
        ps.setString(21, who_to_remind_2);
        ps.setString(22, email2);
        ps.setString(23, raise_entry);
        ps.setString(24, section);
        ps.setInt(25, Integer.parseInt(state));
        ps.setInt(26, Integer.parseInt(driver));
        ps.setString(27, spare_1);
        ps.setString(28, spare_2);
        ps.setString(29, status);
        ps.setString(30, province);
        ps.setInt(31, Integer.parseInt(department_id));
        ps.setString(32, require_redistribution);
        ps.setString(33, require_depreciation);
        ps.setString(34, subject_to_vat);
        ps.setDouble(35, Double.parseDouble(vatable_cost));
        ps.setDouble(36, Double.parseDouble(vat_amount));
        ps.setString(37, wh_tax_cb);
        ps.setDouble(38, Double.parseDouble(wh_tax_amount));
        ps.setInt(39, Integer.parseInt(branch_id));
        ps.setString(40, multiple);
        ps.setDouble(41, costPrice);
        ps.setString(42, code.getBranchCode(branch_id));
        ps.setString(43, code.getDeptCode(department_id));
        ps.setString(44, code.getSectionCode(section_id));
        ps.setString(45, code.getCategoryCode(category_id));
        ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
        ps.setInt(47, Integer.parseInt(noOfMonths));
        ps.setDate(48, dbConnection.dateConvert(expiryDate));
        ps.setString(49, bar_code);
        ps.setString(50,sbu_code);
        ps.setString(51, lpo);
        ps.setString(52,getSupervisor());
        ps.setString(53,deferPay);

        ps.setString(54, category_id);
        ps.setString(55, depreciation_rate);
        ps.setDouble(56, Double.parseDouble(selectTax));
        ps.setString(57, spare_3);
        ps.setString(58, spare_4);
        ps.setString(59, spare_5);
        ps.setString(60, spare_6);
        ps.setString(61, category_id);
        ps.setString(62, code.getSubCategoryCode(sub_category_id));
        ps.setDouble(63, Double.parseDouble(transport_cost));
        ps.setDouble(64, Double.parseDouble(other_cost));
        
        ps.setString(65, assetId);
        ps.setString(66,userId);

        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateasset():WARN:Error updating to am_asset table->" + ex);
    } 
    
    return done;

}

 /**
     * *Bulk asset update
     */
    public boolean bulkAssetUpdate(java.util.ArrayList list) {
	
     String query = "update am_asset SET  " +
                       "REGISTRATION_NO=?,Description=?,VENDOR_AC=?," +
                       "ASSET_MODEL=?,ASSET_SERIAL_NO=?," +
                       "ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                       "ASSET_USER=?,ASSET_MAINTENANCE=?," +
                       "AUTHORIZED_BY=?," +
                       "PURCHASE_REASON=?,SBU_CODE=?," +
                       "SPARE_1=?,SPARE_2=?,SPARE_3=?,SPARE_4=?,SPARE_5=?,SPARE_6=?," +
                       "BAR_CODE=?,SUB_CATEGORY_ID =?,SUB_CATEGORY_CODE =? " +
                       "WHERE ASSET_ID=? ";


	magma.AssetRecordsBean bd = null;
	int[] d = null;
	 try (Connection con = dbConnection.getConnection("legendPlus");
   	         PreparedStatement ps = con.prepareStatement(query)) {

		for (int i = 0; i < list.size(); i++) {
			bd = (magma.AssetRecordsBean) list.get(i);

            String registration = bd.getRegistration_no();
            String description = bd.getDescription();
            String vendoracc = bd.getVendor_account();
            String model1 = bd.getModel();
            String serial = bd.getSerial_number();
            String engine = bd.getEngine_number();
            String suppliedby = bd.getSupplied_by();
            String assetuser = bd.getUser();
            String maintained = bd.getMaintained_by();
            String authorized = bd.getAuthorized_by();
            String reason1 = bd.getReason();
            String sbu = bd.getSbu_code();
            String spare1 = bd.getSpare_1();
            String spare2 = bd.getSpare_2();
            String spare3 = bd.getSpare_3();
            String spare4 = bd.getSpare_4();
            String spare5 = bd.getSpare_5();
            String spare6 = bd.getSpare_6();
            String barcode = bd.getBar_code();
            String lpo1 = bd.getLpo();
            String asset_id1 = bd.getAsset_id();
            String subcatid = bd.getSub_category_id();
            String subcatQuery = "SELECT sub_category_code FROM   am_ad_sub_category WHERE sub_category_id = '"+subcatid+"'";
            String subcatcode = approvalRec.getCodeName(subcatQuery);
//System.out.println("subcatcode in bulkAssetUpdate: "+subcatcode);
            //String subcatcode = bd.getSubcatCode();

            if (registration == null || registration.equals("")) {
                registration = "0";
            }
            if (description == null || description.equals("")) {
            	description = "";
            }
            if (vendoracc == null || vendoracc.equals("")) {
                vendoracc = "0";
            }
            if (subcatid == null || subcatid.equals("")) {
            	subcatid = "0";
            }
            if (subcatcode == null || subcatcode.equals("")) {
            	subcatcode = "0";
            }            
            if (model1 == null || model1.equals("")) {
                model1 = "0";
            }
            
            if (serial == null || serial.equals("")) {
                serial = "0";
            }
            if (engine == null || engine.equals("")) {
                engine = "0";
            }
            if (suppliedby == null || suppliedby.equals("")) {
                suppliedby = "0";
            }
            if (assetuser == null || assetuser.equals("")) {
                assetuser = "0";
            }
            if (maintained == null || maintained.equals("")) {
                maintained = "0";
            }
            if (authorized == null || authorized.equals("")) {
                authorized = "0";
            }
            if (reason1 == null || reason1.equals("")) {
                reason1 = "0";
            }
            if (sbu == null || sbu.equals("")) {
            	sbu = "0";
            }
            if (spare1 == null || spare1.equals("")) {
                spare1 = "0";
            }
            if (spare2 == null || spare2.equals("")) {
                spare2 = "0";
            }
            if (spare3 == null || spare3.equals("")) {
                spare3 = "0";
            }
            if (spare4 == null || spare4.equals("")) {
                spare4 = "0";
            }
            if (spare5 == null || spare5.equals("")) {
                spare5 = "0";
            }
            if (spare6 == null || spare6.equals("")) {
                spare6 = "0";
            }            
           if (asset_id1 == null || asset_id1.equals("")) {
                asset_id1 = "0";
            }
            if (barcode == null || barcode.equals("")) {
                barcode = "0";
            }
            if (lpo1 == null || lpo1.equals("")) {
            lpo1 = "0";
            }
//            System.out.println("<<<<<<<<<>>>>>>>>>>>> ");
            ps.setString(1, registration);
            ps.setString(2, description.toUpperCase());
            ps.setString(3, vendoracc);
            ps.setString(4, model1);
            ps.setString(5, serial);
            ps.setString(6, engine);
            ps.setInt(7, Integer.parseInt(suppliedby));
            ps.setString(8, assetuser);
//            System.out.println("<<<<maintained: "+maintained);
            ps.setInt(9, Integer.parseInt(maintained));
            ps.setString(10, authorized);
            ps.setString(11, reason1);
            ps.setString(12, sbu);
            ps.setString(13, spare1);
            ps.setString(14, spare2 );
            ps.setString(15, spare3);
            ps.setString(16, spare4 );
            ps.setString(17, spare5);
            ps.setString(18, spare6 );
            ps.setString(19, barcode);
            ps.setInt(20, Integer.parseInt(subcatid));
            ps.setString(21, subcatcode);
            ps.setString(22, asset_id1);
 //           System.out.println("<<<<asset_id1: "+asset_id1);
            ps.addBatch();
	}
		d = ps.executeBatch();
		//System.out.println("Executed Successfully ");


	} catch (Exception ex) {
		System.out.println("Error Updating all asset ->" + ex);
	} 
	 

	return (d.length > 0);

}

    /**
     * Closing connection for prepared statements
     * @param con
     * @param ps
     */
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
    
public boolean updateStatusOld(String status,String batchId) throws Exception {

    String query = "update am_asset_approval set PROCESS_STATUS=? where BATCH_ID=? " ;

    Connection con = null;
    PreparedStatement ps = null;
    boolean done = true;
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, status);
        ps.setString(2, batchId);

        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("am_asset_approval: update am_asset_approval():WARN:Error updating to am_asset_approval table->" + ex);
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;
}


public boolean updateStatus(String status,String batchId) throws Exception {

    String query = "update am_asset_approval set PROCESS_STATUS=? where BATCH_ID=? " ;

    boolean done = true;
    try (Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query)) {
    	
        ps.setString(1, status);
        ps.setString(2, batchId);

        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("am_asset_approval: update am_asset_approval():WARN:Error updating to am_asset_approval table->" + ex);
    } 
    return done;
}



private String getCurrentDate1(){
   String output = null;

 try {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
Date date = new Date();
   output = sdf1.format(date);
//output = sdf.parse(sdf.format(date));
     System.out.println("the pre output is " + sdf1.toPattern());
}catch(Exception e){e.printStackTrace();}

return output;


    }


//lanre new modification 25-5-09
public String checkAccounPartPaymentOld (String category,String branch)
{
	String assetAcqusitionSuspense="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);
	String query=" select c.iso_code,"
				+" (select accronym from am_ad_ledger_type where series = substring(b.part_pay_acct,1,1)),"
				+" d.branch_code,"
				+" a.Asset_Ledger,"
				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
				+" b.default_branch + b.part_pay_acct asd"
				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
				+" where a.currency_id = c.currency_id"
				+" and a.category_code = '"+category+"'"
				+" and d.branch_code = '"+branch+"'";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

		con = dbConnection.getConnection("legendPlus");

		ps = con.prepareStatement(query);

		rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		finally
		{
			dbConnection.closeConnection(con, ps);
		}
return 	assetAcqusitionSuspense;
}

public String checkAccounPartPayment(String category, String branch) {
    String assetAcqusitionSuspense = "";
    
    String query=" select c.iso_code,"
			+" (select accronym from am_ad_ledger_type where series = substring(b.part_pay_acct,1,1)),"
			+" d.branch_code,"
			+" a.Asset_Ledger,"
			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
			+" b.default_branch + b.part_pay_acct asd"
			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
			+" where a.currency_id = c.currency_id"
			+" and a.category_code = ? "
			+" and d.branch_code = ? ";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, category);
        ps.setString(2, branch);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                assetAcqusitionSuspense = rs.getString("asd");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return assetAcqusitionSuspense;
}


public String checkAccounDeferPaymentOld (String category,String branch)
{
	String assetAcqusitionSuspense="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);
	String query=" select c.iso_code,"
				+" (select accronym from am_ad_ledger_type where series = substring(b.defer_account,1,1)),"
				+" d.branch_code,"
				+" a.Asset_Ledger,"
				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
				+" b.default_branch + b.defer_account asd"
				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
				+" where a.currency_id = c.currency_id"
				+" and a.category_code = '"+category+"'"
				+" and d.branch_code = '"+branch+"'";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

		con = dbConnection.getConnection("legendPlus");

		ps = con.prepareStatement(query);

		rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		finally
		{
			dbConnection.closeConnection(con, ps);
		}
return 	assetAcqusitionSuspense;
}

public String checkAccounDeferPayment (String category,String branch)
{
	String assetAcqusitionSuspense="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);
	String query=" select c.iso_code,"
				+" (select accronym from am_ad_ledger_type where series = substring(b.defer_account,1,1)),"
				+" d.branch_code,"
				+" a.Asset_Ledger,"
				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1))+"
				+" b.default_branch + b.defer_account asd"
				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
				+" where a.currency_id = c.currency_id"
				+" and a.category_code = ? "
				+" and d.branch_code = ? ";


	try (Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query)) {
		
		 ps.setString(1, category);
	        ps.setString(2, branch);

		try(ResultSet rs = ps.executeQuery();){

		if (rs.next())
		 {
			assetAcqusitionSuspense  = rs.getString("asd");

		 }
	   }
	}
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}

public boolean insertRaiseEntryTransactionOld(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId ) {
    boolean done=true;

	   Connection con = null;
    PreparedStatement ps = null;
    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
            "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?)";
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;
}


public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
            "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?)";
    try (Connection con = dbConnection.getConnection("legendPlus");
    	    PreparedStatement ps = con.prepareStatement(query)) {
    	
       
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    
    return done;
}

public boolean insertRaiseEntryTransactionTranIdOld(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String tranId )
{
    boolean done=true;

	   Connection con = null;
    PreparedStatement ps = null;
    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
            "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,Trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setInt(11,Integer.parseInt(tranId));

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;
}


public boolean insertRaiseEntryTransactionTranId(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String tranId )
{
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
            "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,Trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    try (Connection con = dbConnection.getConnection("legendPlus");
    	    PreparedStatement ps = con.prepareStatement(query)) {
    	
        
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setInt(11,Integer.parseInt(tranId));

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    
    return done;
}

public ArrayList findRaisedTransactionOld(String filter) {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    RaisedTransaction app = null;
    ArrayList collection = new ArrayList();
    String FINDER_QUERY = "SELECT User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transaction_date from am_raisentry_transaction  " + filter;

    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(FINDER_QUERY);

        rs = ps.executeQuery();

        while (rs.next()) {

            String id = rs.getString("User_id");
            String Description = rs.getString("Description");
            String debitAccount = rs.getString("debitAccount");
            String creditAccount = rs.getString("creditAccount");
            double amount = rs.getDouble("amount");
           String iso =rs.getString("iso");
           String assetId = rs.getString("ASSET_ID");
           String page1 = rs.getString("page1");
           String transaction_date= rs.getString("transaction_date");


            app = new RaisedTransaction(id,Description,debitAccount,creditAccount,amount,iso,assetId,page1,transaction_date);
            collection.add(app);
        }

    } catch (Exception ex) {
        System.out.println("WARNING: cannot fetch [am_raisentry_transaction]->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }

    return collection;

}

public ArrayList findRaisedTransaction(String filter) {

   
    RaisedTransaction app = null;
    ArrayList collection = new ArrayList();
    String FINDER_QUERY = "SELECT User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transaction_date from am_raisentry_transaction  " + filter;

    try (Connection con = dbConnection.getConnection("legendPlus");
    	    PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {

       try( ResultSet rs = ps.executeQuery();){

        while (rs.next()) {

            String id = rs.getString("User_id");
            String Description = rs.getString("Description");
            String debitAccount = rs.getString("debitAccount");
            String creditAccount = rs.getString("creditAccount");
            double amount = rs.getDouble("amount");
           String iso =rs.getString("iso");
           String assetId = rs.getString("ASSET_ID");
           String page1 = rs.getString("page1");
           String transaction_date= rs.getString("transaction_date");


            app = new RaisedTransaction(id,Description,debitAccount,creditAccount,amount,iso,assetId,page1,transaction_date);
            collection.add(app);
        }
       }

    } catch (Exception ex) {
        System.out.println("WARNING: cannot fetch [am_raisentry_transaction]->" +
                ex.getMessage());
    } 
    
    return collection;

}
    /**
     * @return the selectTax
     */
    public String getSelectTax() {

        return selectTax;
    }

    /**
     * @param selectTax the selectTax to set
     */
    public void setSelectTax(String selectTax) {
        this.selectTax = selectTax;
    }
    /**
     * @return the selectTax
     */
   
    public int getQuantity() {

        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }    
 public String isoCheckOld(  String asset_id,String page1,String transactionId) 
	   {
		 	
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        String iso = "1414";
	     //   System.out.print("isoCheck asset_id: "+asset_id+" page1: "+page1+" transactionId: "+transactionId);
	        //String FINDER_QUERY = "SELECT iso from am_raisentry_transaction WHERE transactionId="+transactionId+"   and ASSET_ID='"+asset_id+"'  and page1='"+page1.trim()+"' ";
	        String FINDER_QUERY = "SELECT iso from am_raisentry_transaction WHERE transactionId=?   and ASSET_ID=?  and page1=? ";
	 //       System.out.print("isoCheck FINDER_QUERY: "+FINDER_QUERY);
	        try {
	        	con = dbConnection.getConnection("legendPlus");
	            ps = con.prepareStatement(FINDER_QUERY);
	            ps.setString(1, transactionId);
	            ps.setString(2, asset_id);
	            ps.setString(3, page1);	            	            
	            rs = ps.executeQuery();

	            while (rs.next()) 
	            {
	            	iso = rs.getString("iso");
	            }
//	            System.out.print("isoCheck iso: "+iso);
	        } 
	        catch (Exception ex) 
	        {
	            System.out.println("WARNING: cannot fetch am_raisentry_transaction->" +ex.getMessage());
	            ex.printStackTrace();
	        } 
	        finally 
	        {
	        	closeConnection(con, ps);
	        }

	        return iso;

	    }
 

 public String isoCheck(String assetId, String page1, String transactionId, String tranId) {

	    String iso = "1414"; 

	    String query = "SELECT iso " +
	                   "FROM am_raisentry_transaction " +
	                   "WHERE transactionId = ? " +
	                   "AND ASSET_ID = ? " +
	                   "AND page1 = ? " +
	                   "AND Trans_id = ?";

	    try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, transactionId);
	        ps.setString(2, assetId);
	        ps.setString(3, page1);
	        ps.setString(4, tranId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                iso = rs.getString("iso");
	            }
	        }

	    } catch (Exception ex) {
	        System.out.println("WARNING: cannot fetch am_raisentry_transaction -> " + ex.getMessage());
	        ex.printStackTrace();
	    }

	    return iso;
	}
 
	 public boolean isoCheckingOld (  String asset_id,String page1,String transactionId) 
	   {
		 	
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        boolean done = false;
	       
	        String FINDER_QUERY = "SELECT * from am_raisentry_transaction WHERE transactionId=?  " +
                        " and ASSET_ID=?  and page1=? ";

	        try {
	        	con = dbConnection.getConnection("legendPlus");
	            ps = con.prepareStatement(FINDER_QUERY);
	            ps.setString(1, transactionId);
	            ps.setString(2, asset_id);
	            ps.setString(3, page1);
	            
	            
	            
	            rs = ps.executeQuery();

	            while (rs.next()) 
	            {
	            	done = true;
	            }

	        } 
	        catch (Exception ex) 
	        {
	            System.out.println("WARNING: cannot fetch am_raisentry_transaction->" +ex.getMessage());
	            ex.printStackTrace();
	        } 
	        finally 
	        {
	        	closeConnection(con, ps);
	        }

	        return done;

	    }
	 
	 public boolean isoChecking (  String asset_id,String page1,String transactionId) 
	   {
		 	
	       
	        boolean done = false;
	       
	        String FINDER_QUERY = "SELECT * from am_raisentry_transaction WHERE transactionId=?  " +
                      " and ASSET_ID=?  and page1=? ";

	        try (Connection con = dbConnection.getConnection("legendPlus");
	   	         PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {
	        	
	            ps.setString(1, transactionId);
	            ps.setString(2, asset_id);
	            ps.setString(3, page1);
	            
	            
	            
	          try(ResultSet  rs = ps.executeQuery();){

	            while (rs.next()) 
	            {
	            	done = true;
	            }

	        } 
	        }
	        catch (Exception ex) 
	        {
	            System.out.println("WARNING: cannot fetch am_raisentry_transaction->" +ex.getMessage());
	            ex.printStackTrace();
	        } 
	        

	        return done;

	    }

         public boolean isoCheckingOld (  String asset_id,String page1,String transactionId,String tranId)
	   {

	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        boolean done = false;

	        String FINDER_QUERY = "SELECT * from am_raisentry_transaction WHERE transactionId=?  " +
                        " and ASSET_ID=?  and page1=?  and Trans_id = ? ";

	        try {
	        	con = dbConnection.getConnection("legendPlus");
	            ps = con.prepareStatement(FINDER_QUERY);
	            ps.setString(1, transactionId.trim());
	            ps.setString(2, asset_id.trim());
	            ps.setString(3, page1.trim());
	             ps.setString(4,tranId);


	            rs = ps.executeQuery();

	            while (rs.next())
	            {
	            	done = true;
	            }

	        }
	        catch (Exception ex)
	        {
	            System.out.println("WARNING: cannot fetch am_raisentry_transaction->" +ex.getMessage());
	            ex.printStackTrace();
	        }
	        finally
	        {
	        	closeConnection(con, ps);
	        }

	        return done;

	    }
         
         public boolean isoChecking (  String asset_id,String page1,String transactionId,String tranId)
  	   {

  	       
  	        boolean done = false;

  	        String FINDER_QUERY = "SELECT * from am_raisentry_transaction WHERE transactionId=?  " +
                          " and ASSET_ID=?  and page1=?  and Trans_id = ? ";


	        try (Connection con = dbConnection.getConnection("legendPlus");
	   	         PreparedStatement ps = con.prepareStatement(FINDER_QUERY)) {
  	            ps.setString(1, transactionId.trim());
  	            ps.setString(2, asset_id.trim());
  	            ps.setString(3, page1.trim());
  	             ps.setString(4,tranId);


  	           try(ResultSet  rs = ps.executeQuery();){

  	            while (rs.next())
  	            {
  	            	done = true;
  	            }
  	           }

  	        }
  	        catch (Exception ex)
  	        {
  	            System.out.println("WARNING: cannot fetch am_raisentry_transaction->" +ex.getMessage());
  	            ex.printStackTrace();
  	        }
  	        

  	        return done;

  	    }

	 public boolean updateRaiseEntryTransactionOld( String asset_id,String page1,String transactionId ,String iso) {
		    boolean done=true;
			  
			   Connection con = null;
		    PreparedStatement ps = null;
		    String query = "update am_raisentry_transaction set iso=?,transaction_date=? where transactionId=?    and ASSET_ID=? and page1=? " ;
		                  
		    try {
		    	con = dbConnection.getConnection("legendPlus");
		        ps = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3,transactionId);
		       
		        ps.setString(4,asset_id);
		        ps.setString(5, page1);
		        
		        
		        ps.execute();
		        
		    } 
		    catch (Exception ex) 
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
		        ex.printStackTrace();
		    } finally {
		    	closeConnection(con, ps);
		    }
		    return done;
		}
	 
	 public boolean updateRaiseEntryTransaction( String asset_id,String page1,String transactionId ,String iso) {
		    boolean done=true;
			  
			
		    String query = "update am_raisentry_transaction set iso=?,transaction_date=? where transactionId=?    and ASSET_ID=? and page1=? " ;
		                  
		    try (Connection con = dbConnection.getConnection("legendPlus");
		   	         PreparedStatement ps = con.prepareStatement(query)) {
		    	
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3,transactionId);
		       
		        ps.setString(4,asset_id);
		        ps.setString(5, page1);
		        
		        
		        ps.execute();
		        
		    } 
		    catch (Exception ex) 
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
		        ex.printStackTrace();
		    } 
		    
		    return done;
		}

          public boolean updateRaiseEntryTransaction2_Old( String asset_id,String page1,String transactionId ,String iso,String tranId) {
		    boolean done=true;

			   Connection con = null;
		    PreparedStatement ps = null;
		    String query = "update am_raisentry_transaction set iso=?,transaction_date=? where transactionId=?    and ASSET_ID=? and page1=?   and Trans_id = ? " ;

		    try {
		    	con = dbConnection.getConnection("legendPlus");
		        ps = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3,transactionId);

		        ps.setString(4,asset_id);
		        ps.setString(5, page1);
		        ps.setString(6,tranId);

		        ps.execute();

		    }
		    catch (Exception ex)
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
		        ex.printStackTrace();
		    } finally {
		    	closeConnection(con, ps);
		    }
		    return done;
		}
          
          public boolean updateRaiseEntryTransaction2( String asset_id,String page1,String transactionId ,String iso,String tranId) {
  		    boolean done=true;

  			  
  		    String query = "update am_raisentry_transaction set iso=?,transaction_date=? where transactionId=?    and ASSET_ID=? and page1=?   and Trans_id = ? " ;

  		  try (Connection con = dbConnection.getConnection("legendPlus");
		   	         PreparedStatement ps = con.prepareStatement(query)) {
  		        ps.setString(1,iso);
  		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
  		        ps.setString(3,transactionId);

  		        ps.setString(4,asset_id);
  		        ps.setString(5, page1);
  		        ps.setString(6,tranId);

  		        ps.execute();

  		    }
  		    catch (Exception ex)
  		    {
  		 	   done = false;
  		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
  		        ex.printStackTrace();
  		    } 
  		    return done;
  		}

		public String lossDisposalAccountOld (String category,String branch)
		{
			String lossDisposalAccount="";
//			System.out.println("category "+category);
//			System.out.println("branch "+branch);
//			String old_query=" select c.iso_code,"
//						+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//						+" d.branch_code,"
//						+" b.Loss_Disposal_Account,"
//						+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Loss_Disposal_Account,1,1))+"
//						+" b.default_branch +	b.Loss_Disposal_Account asd"
//						+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//						+" where a.currency_id = c.currency_id"
//						+" and a.category_code = '"+category+"'"
//						+" and d.branch_code = '"+branch+"'";
//
//                        String query=" select c.iso_code,"
//						+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//						+" d.branch_code,"
//						+" b.Loss_Disposal_Account,"
//						+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Loss_Disposal_Account,1,1))+"
//						+" b.default_branch +	(substring(b.Loss_Disposal_Account,2,9)) asd"
//						+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//						+" where a.currency_id = c.currency_id"
//						+" and a.category_code = '"+category+"'"
//						+" and d.branch_code = '"+branch+"'";

           		     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'EX2'");
        		     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
        		     
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				con = dbConnection.getConnection("legendPlus");

				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				if (rs.next())
				 {

					lossDisposalAccount  = rs.getString("asd");


				 }
			   }
				catch (Exception er)
				{
				 er.printStackTrace();

				}
				finally
				{
					dbConnection.closeConnection(con, ps);
				}
		return 	lossDisposalAccount;
		}
		
	
		public String lossDisposalAccount(String category, String branch) {

		    String lossDisposalAccount = "";

		    String baseQuery = approvalRec.getCodeName(
		            "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'EX2'"
		    );

		   
		    String query = baseQuery + 
		                   " AND a.category_code = ? " +
		                   " AND d.branch_code = ?";

		    try (Connection con = dbConnection.getConnection("legendPlus");
		         PreparedStatement ps = con.prepareStatement(query)) {

		        ps.setString(1, category);
		        ps.setString(2, branch);

		        try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) {
		                lossDisposalAccount = rs.getString("asd");
		            }
		        }

		    } catch (Exception e) {
		        System.out.println("Error in lossDisposalAccount(): " + e.getMessage());
		        e.printStackTrace();
		    }

		    return lossDisposalAccount;
		}
		


		public String stateSuspenseAcctOld (String category,String branch)
		{
			String assetAcqusitionSuspense="";
//			System.out.println("category "+category);
//			System.out.println("branch "+branch);
//			String query=" select c.iso_code,"
//						+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//						+" d.branch_code,"
//						+" b.suspense_acct,"
//						+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.suspense_acct,1,1))+"
//						+" b.default_branch +	b.suspense_acct asd"
//						+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//						+" where a.currency_id = c.currency_id"
//						+" and a.category_code = '"+category+"'"
//						+" and d.branch_code = '"+branch+"'";

		     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI3'");
		     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		     
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				con = dbConnection.getConnection("legendPlus");

				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				if (rs.next())
				 {

					assetAcqusitionSuspense  = rs.getString("asd");


				 }
			   }
				catch (Exception er)
				{
				 er.printStackTrace();

				}
				finally
				{
					dbConnection.closeConnection(con, ps);
				}
		return 	assetAcqusitionSuspense;
		}
		
		
		public String stateSuspenseAcct(String category, String branch) {

		    String assetAcqusitionSuspense = "";

		    String baseQuery = approvalRec.getCodeName(
		            "select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LI3'"
		    );

		   
		    String query = baseQuery + 
		                   " AND a.category_code = ? " +
		                   " AND d.branch_code = ?";

		    try (Connection con = dbConnection.getConnection("legendPlus");
		         PreparedStatement ps = con.prepareStatement(query)) {

		        ps.setString(1, category);
		        ps.setString(2, branch);

		        try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) {
		            	assetAcqusitionSuspense = rs.getString("asd");
		            }
		        }

		    } catch (Exception e) {
		        System.out.println("Error in lossDisposalAccount(): " + e.getMessage());
		        e.printStackTrace();
		    }

		    return assetAcqusitionSuspense;
		}


public void updateGroupAssetStatus(String group_Id)
{
//String query_r ="update am_group_asset set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";
	String query_r ="update am_group_asset set asset_status=?,raise_entry=? " +
			"where Group_id = '"+group_Id+"'";
Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try
{
    con = dbConnection.getConnection("legendPlus");
    ps = con.prepareStatement(query_r);
    ps.setString(1,"ACTIVE");
    ps.setString(2,"Y");
    //ps.setString(2,reject_reason);
    int i =ps.executeUpdate();
    //ps.execute();
    // the same thing should also be done in the am_group_asset_main
    updateGroupAssetStatusMain(group_Id);
}
catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatus()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()

public void updateGroupAssetStatusMainOld(String group_Id)
{
	String query_r ="update am_group_asset_main set asset_status=?,raise_entry=? " +
	"where Group_id = '"+group_Id+"'";
Connection con = null;
PreparedStatement ps = null;
//ResultSet rs = null;

try
{
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query_r);
ps.setString(1,"ACTIVE");
ps.setString(2,"Y");
//ps.setString(2,reject_reason);
int i =ps.executeUpdate();
//ps.execute();
}
catch (Exception ex) {

    System.out.println("AssetRecordBean: updateGroupAssetStatusMain()>>>>>" + ex);
} finally {
    dbConnection.closeConnection(con, ps);
}}

public void updateGroupAssetStatusMain(String group_Id)
{
	String query_r ="update am_group_asset_main set asset_status=?,raise_entry=? " +
	"where Group_id = ? ";


	 try (Connection con = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = con.prepareStatement(query_r)) {
ps.setString(1,"ACTIVE");
ps.setString(2,"Y");
ps.setString(3,group_Id);

int i =ps.executeUpdate();
}
catch (Exception ex) {

    System.out.println("AssetRecordBean: updateGroupAssetStatusMain()>>>>>" + ex);
} 
}


public String reArrangeDate(String date){
       String re="";
       String year =date.substring(0,4);
       String month =date.substring(5,7);
       String day = date.substring(8,10);

       re = day +"-"+month+"-"+year;



       return re;



       }//reArrangeDate()


public String[] setApprovalDataGroupOld(long id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
    Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //int groupId = Integer.parseInt(id);
         String query ="select group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
         		"		description,effective_date,BRANCH_CODE," +
         				"Asset_Status from am_group_asset_main where group_id =" +id ;
//         System.out.println("Query in setApprovalDataGroup : " + query);
//         String groupidQry = "select group_id from am_group_asset_main where group_id =" +id;
//         String Qrygroupid = approvalRec.getCodeName(groupidQry); 
//        		 System.out.println("Qrygroupid in setApprovalDataGroup : " + Qrygroupid);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = String.valueOf(id);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = dbConnection.formatDate(rs.getDate(5));
//               System.out.println("Before Conversion" + rs.getDate(5));
//               System.out.println("Posting_Date in setApprovalDataGroup : " +  result[4]);
               result[5] = rs.getString(6);
               result[6] = dbConnection.formatDate(rs.getDate(7));
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }
//            System.out.println("Final Conversion");
        } catch (Exception ex) {
            System.out.println("AssetRecordsBean : setApprovalData()WARN: Error setting approval data for group asset creation ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        	result[9] = "Group Asset Creation";
        	result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


public String[] setApprovalDataGroup(long id) {

    String[] result = new String[12];

    String query = "SELECT group_id, user_ID, supervisor, Cost_Price, Posting_Date, " +
                   "       description, effective_date, BRANCH_CODE, Asset_Status " +
                   "FROM am_group_asset_main " +
                   "WHERE group_id = ?";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setLong(1, id);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                result[0] = String.valueOf(id);
                result[1] = rs.getString("user_ID");
                result[2] = rs.getString("supervisor");
                result[3] = rs.getString("Cost_Price");

                result[4] = rs.getDate("Posting_Date") != null
                        ? dbConnection.formatDate(rs.getDate("Posting_Date"))
                        : "";

                result[5] = rs.getString("description");

                result[6] = rs.getDate("effective_date") != null
                        ? dbConnection.formatDate(rs.getDate("effective_date"))
                        : "";

                result[7] = rs.getString("BRANCH_CODE");
                result[8] = rs.getString("Asset_Status");
            }
        }

    } catch (Exception ex) {
        System.out.println("AssetRecordsBean: setApprovalDataGroup() WARN -> " + ex.getMessage());
        ex.printStackTrace();
    }

    result[9] = "Group Asset Creation";
    result[10] = "P";

    return result;
}




public boolean updateNewApprovalAssetStatusOld(String groupID, int supervise) throws Exception {

    /*String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
    		"super_id =?, approval1 =? where asset_id =?";*/
    String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
	"super_id =?, approval1 =? where asset_id =?";
     boolean done = true;
    Connection con = null;
    PreparedStatement ps = null;

    try {



        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, "ACTIVE");
        ps.setString(2, "A");
        ps.setInt(3, supervise);
        ps.setInt(4, supervise);
        ps.setString(5,groupID);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateNewApprovalAssetStatus: WARN:Error updating asset->" + ex);
    } finally {
        dbConnection.closeConnection(con, ps);
    }
    return done;

}

public boolean updateNewApprovalAssetStatus(String groupID, int supervise) throws Exception {


    String query = "update am_asset_approval set  asset_status = ?, process_status=?, " +
	"super_id =?, approval1 =? where asset_id =?";
     boolean done = true;
   

     try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, "ACTIVE");
        ps.setString(2, "A");
        ps.setInt(3, supervise);
        ps.setInt(4, supervise);
        ps.setString(5,groupID);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateNewApprovalAssetStatus: WARN:Error updating asset->" + ex);
    } 
     
    return done;

}

public void updateAssetStatuxOld(String assetId){
String query_r ="update am_asset set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"APPROVED");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatux()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatus()

public void updateAssetStatux(String assetId){
String query_r ="update am_asset set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";


try (Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query_r)) {


            ps.setString(1,"APPROVED");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatux()>>>>>" + ex);
        } 


}//updateAssetStatus()

public boolean updateNewAssetStatuxOld(String assetId) throws Exception {

        String query = "update am_asset SET  asset_status = 'APPROVED' where asset_id ='" +assetId+"'";
         boolean done = true;
        Connection con = null;
        PreparedStatement ps = null;

        try {



            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: updateNewAssetStatux(): WARN:Error updating asset->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return done;
    }

public boolean updateNewAssetStatux(String assetId) throws Exception {

    String query = "update am_asset SET  asset_status = 'APPROVED' where asset_id ='" +assetId+"'";
     boolean done = true;
    
     try (Connection con = dbConnection.getConnection("legendPlus");
    	        PreparedStatement ps = con.prepareStatement(query)) {

        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateNewAssetStatux(): WARN:Error updating asset->" + ex);
    } 
     
    return done;
}

    public String vendorNameOld(String assetId)
		{


			String vendor="";
			String query="select  vendor_name from am_ad_vendor where vendor_id in " +
                    "(select supplier_name from am_asset where asset_id='"+assetId+"')";

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				con = dbConnection.getConnection("legendPlus");

				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				if (rs.next())
				 {

					vendor  = rs.getString("vendor_name");


				 }
			   }
				catch (Exception er)
				{
				 er.printStackTrace();

				}
				finally
				{
					dbConnection.closeConnection(con, ps);
				}
		return 	vendor;
		}
    
    public String vendorName(String assetId) {

        String vendor = "";

        String query = "SELECT v.vendor_name " +
                       "FROM am_ad_vendor v " +
                       "JOIN am_asset a ON v.vendor_id = a.supplier_name " +
                       "WHERE a.asset_id = ?";

        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, assetId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vendor = rs.getString("vendor_name");
                }
            }

        } catch (Exception e) {
            System.out.println("Error in vendorNameOld(): " + e.getMessage());
            e.printStackTrace();
        }

        return vendor;
    }

    /**
     * @return the systemIp
     */
    public String getSystemIp() {
        return systemIp;
    }

    /**
     * @param systemIp the systemIp to set
     */
    public void setSystemIp(String systemIp) {
        this.systemIp = systemIp;
    }



        private void rinsertAssetRecord(String assetId) throws Exception, Throwable {
       // asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
              //  department_id, section_id, category_id);
       
        boolean done = true;
    AssetPaymentManager payment = null;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (sub_category_id == null || sub_category_id.equals("")) {
            sub_category_id = "0";
        }
        
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }
    //if (supervisor == null || expiryDate.equals("")) {
        //    expiryDate = null;
       // }

/*
if (bar_code == null || bar_code.equals("")) {
            bar_code = null;
        }
*/
  /*      if (sbu_code == null || sbu_code.equals("")) {
            sbu_code=("0");
        }
  */

/*
if (lpo == null || lpo.equals("")) {
            lpo=("0");
        }
*/
        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");
        String createQuery = "INSERT INTO AM_ASSET_ARCHIVE        " +
                             "(" +
                             "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                             "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                             "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                             "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                             "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                             "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                             "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                             "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                             "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                             "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                             "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
                             "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                             "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                             "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                             "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,"+
                             "defer_pay,wht_percent,system_ip,mac_address,asset_code,SPARE_3, SPARE_4,SPARE_5, SPARE_6,sub_category_id, SUB_CATEGORY_CODE,TRANPORT_COST,OTHER_COST) " +
                             "VALUES" +
                             "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                              "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        /*
         *First Create Asset Records
         * and then determine if it
         * should be made available for fleet.
         */

        try (Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(createQuery)) {

            double costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);

           
            ps.setString(1, assetId);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description.toUpperCase());
            ps.setString(8, vendor_account);
            ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setInt(18, 0);
            ps.setInt(19, 0);
            ps.setDouble(20, costPrice);
            ps.setDouble(21, costPrice);
            ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by);
            ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setString(28, "0");
            ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
            ps.setInt(30, Integer.parseInt(location));
            ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
            ps.setDouble(32, Double.parseDouble(vatable_cost));
            ps.setDouble(33, Double.parseDouble(vat_amount));
            ps.setString(34, wh_tax_cb);
            ps.setDouble(35, Double.parseDouble(wh_tax_amount));
            ps.setString(36, require_depreciation);
            ps.setString(37, require_redistribution);
            ps.setString(38, subject_to_vat);
            ps.setString(39, who_to_remind);
            ps.setString(40, email_1);
            ps.setString(41, who_to_remind_2);
            ps.setString(42, email2);
            ps.setString(43, raise_entry);
            ps.setString(44, "0");
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare_1);
            ps.setString(49, spare_2);
            ps.setString(50, status);
            ps.setString(51, user_id);
            ps.setString(52, multiple);
            ps.setString(53, province);
            ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(55, new Integer(noOfMonths).intValue());
            ps.setDate(56, dbConnection.dateConvert(expiryDate));
            ps.setString(57, code.getBranchCode(branch_id));
            ps.setString(58, code.getDeptCode(department_id));
            ps.setString(59, code.getSectionCode(section_id));
            ps.setString(60, code.getCategoryCode(category_id));
            ps.setDouble(61, Double.parseDouble(amountPTD));
            //ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
            //Use Vatable_cost instead of costPrice for Manage Asset Payment.====11/08/2009 ayojava
            ps.setDouble(62, (Double.parseDouble(vatable_cost)));
            ps.setString(63, partPAY);
            ps.setString(64, fullyPAID);
            ps.setString(65, bar_code);
            ps.setString(66,sbu_code);
            ps.setString(67, lpo);
            ps.setString(68,getSupervisor());
            ps.setString(69, deferPay);
            ps.setDouble(70, Double.parseDouble(selectTax));
            ps.setString(71,getSystemIp());
            ps.setString(72, getMacAddress());
            ps.setInt(73,assetCode);
            ps.setString(74, spare_3);
            ps.setString(75, spare_4);
            ps.setString(76, spare_5);
            ps.setString(77, spare_6);
            ps.setInt(78, Integer.parseInt(sub_category_id));
            ps.setString(79, code.getSubCategoryCode(sub_category_id));
            ps.setDouble(80, Double.parseDouble(transport_cost));
            ps.setDouble(81, Double.parseDouble(other_cost));
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error inserting into  asset creation archive->" + ex);
        } 

       // return done;
    }

public boolean insertRaiseEntryTransactionArchiveOld(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String systemIp,String mac_Address ) {
    boolean done=true;

	   Connection con = null;
    PreparedStatement ps = null;
    String query = "INSERT INTO [am_raisentry_transaction_archive](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,mac_address)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
        con = dbConnection.getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11, systemIp);
        ps.setString(12, mac_Address);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction_archive->" );
        ex.printStackTrace();
    } finally {
        closeConnection(con, ps);
    }
    return done;
}

public boolean insertRaiseEntryTransactionArchive(
        String userId,
        String description,
        String debitAccount,
        String creditAccount,
        double amount,
        String iso,
        String assetId,
        String page1,
        String transactionId,
        String systemIp,
        String macAddress) {

    String query = "INSERT INTO am_raisentry_transaction_archive " +
            "(User_id, Description, debitAccount, creditAccount, amount, iso, " +
            "ASSET_ID, page1, transactionId, transaction_date, system_ip, mac_address) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, userId);
        ps.setString(2, description);
        ps.setString(3, debitAccount);
        ps.setString(4, creditAccount);
        ps.setDouble(5, amount);
        ps.setString(6, iso);
        ps.setString(7, assetId);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11, systemIp);
        ps.setString(12, macAddress);

        int rows = ps.executeUpdate();

        return rows > 0;  

    } catch (Exception ex) {
        System.out.println("WARNING: cannot insert am_raisentry_transaction_archive -> " + ex.getMessage());
        ex.printStackTrace();
        return false;
    }
}

public boolean updateRaiseEntryTransactionArchiveOld( String asset_id,String page1,String transactionId ,String iso,String systemIp,String mac_Address) {
		    boolean done=true;

			   Connection con = null;
		    PreparedStatement ps = null;
		    String query = "update am_raisentry_transaction_archive set iso=?,transaction_date=? ,system_ip=?,mac_address=? where transactionId=?    and ASSET_ID=? and page1=? " ;

		    try {
		    	con = dbConnection.getConnection("legendPlus");
		        ps = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3, systemIp);
                        ps.setString(4, mac_Address);
                        ps.setString(5,transactionId);
		        ps.setString(6,asset_id);
		        ps.setString(7, page1);


		        ps.execute();

		    }
		    catch (Exception ex)
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction_archive->" );
		        ex.printStackTrace();
		    } finally {
		    	closeConnection(con, ps);
		    }
		    return done;
		}

public boolean updateRaiseEntryTransactionArchive( String asset_id,String page1,String transactionId ,String iso,String systemIp,String mac_Address) {
    boolean done=true;
    
    String query = "update am_raisentry_transaction_archive set iso=?,transaction_date=? ,system_ip=?,mac_address=? where transactionId=?    and ASSET_ID=? and page1=? " ;

    try (Connection con = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = con.prepareStatement(query)) {
    	
        ps.setString(1,iso);
        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(3, systemIp);
                ps.setString(4, mac_Address);
                ps.setString(5,transactionId);
        ps.setString(6,asset_id);
        ps.setString(7, page1);


        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot update am_raisentry_transaction_archive->" );
        ex.printStackTrace();
    } 
    
    return done;
}
    /**
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

public void setPendingTransArchiveOld(String[] a, String code,long mtid){
//System.out.println("====code 2====> "+code);
        int transaction_level=0;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
 String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");


            /////////////To get transaction level
             ps = con.prepareStatement(tranLevelQuery);
              rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setLong(13,mtid);
            ps.setString(14, String.valueOf(mtid));
            ps.setInt(15, transaction_level);

            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>> 1 AssetRecordBeans:setPendingTransArchive()>>>>>>" + er);

        }finally{
        dbConnection.closeConnection(con, ps);

        }
   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
    }//staticApprovalInfo()

public void setPendingTransArchive(String[] a, String code, long mtid) {

    String insertQuery = "INSERT INTO am_asset_approval_archive " +
            "(asset_id, user_id, super_id, amount, posting_date, description, " +
            "effective_date, branchCode, asset_status, tran_type, process_status, " +
            "tran_sent_time, transaction_id, batch_id, transaction_level) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String levelQuery = "SELECT level FROM approval_level_setup WHERE code = ?";

    int transactionLevel = 0;

    try (Connection con = dbConnection.getConnection("legendPlus")) {

        // -------- Get Transaction Level --------
        try (PreparedStatement psLevel = con.prepareStatement(levelQuery)) {

            psLevel.setString(1, code);

            try (ResultSet rs = psLevel.executeQuery()) {
                if (rs.next()) {
                    transactionLevel = rs.getInt("level");
                }
            }
        }

        try (PreparedStatement ps = con.prepareStatement(insertQuery)) {

            ps.setString(1,  a[0] != null ? a[0] : "");
            ps.setString(2,  a[1] != null ? a[1] : "");
            ps.setString(3,  a[2] != null ? a[2] : "");
            ps.setDouble(4,  a[3] != null ? Double.parseDouble(a[3]) : 0.0);
            ps.setTimestamp(5, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6,  a[5] != null ? a[5] : "");
            ps.setDate(7,    a[6] != null ? dbConnection.dateConvert(a[6]) : null);
            ps.setString(8,  a[7] != null ? a[7] : "");
            ps.setString(9,  a[8] != null ? a[8] : "");
            ps.setString(10, a[9] != null ? a[9] : "");
            ps.setString(11, a[10] != null ? a[10] : "");
            ps.setString(12, new SimpleDateFormat("kk:mm:ss").format(new java.util.Date()));
            ps.setLong(13,   mtid);
            ps.setString(14, String.valueOf(mtid));
            ps.setInt(15,    transactionLevel);

            ps.executeUpdate();
        }

    } catch (Exception e) {
        System.out.println("AssetRecordBeans:setPendingTransArchive() ERROR -> " + e.getMessage());
        e.printStackTrace();
    }
}

public void getTransFeedbackArchiveOld(String id, String process_status, String reject_reason,long tran_id){
String query_r ="update am_asset_approval_archive set process_status=?,reject_reason=? where asset_id = '"+id+"'and transaction_id="+tran_id;
    //System.out.println("the value of  ==============" + id);
Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");

if(process_status.equalsIgnoreCase("a")||process_status.equalsIgnoreCase("p")){
reject_reason = "";
}
ps = con.prepareStatement(query_r);



            ps.setString(1,process_status);
            ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();
//System.out.println("the value of i is =========="+ i);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: getTransFeedbackArchive()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

}//getTransFeedback()

public void getTransFeedbackArchive(String id,
        String processStatus,
        String rejectReason,
        long tranId) {

String query = "UPDATE am_asset_approval_archive " +
"SET process_status = ?, reject_reason = ? " +
"WHERE asset_id = ? AND transaction_id = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

if ("a".equalsIgnoreCase(processStatus) ||
"p".equalsIgnoreCase(processStatus)) {
rejectReason = "";
}

ps.setString(1, processStatus);
ps.setString(2, rejectReason);
ps.setString(3, id);
ps.setLong(4, tranId);

ps.executeUpdate();

} catch (Exception ex) {
System.out.println("AssetRecordBean: getTransFeedbackArchiveOld() ERROR -> " + ex.getMessage());
ex.printStackTrace();
}
}

public void updateAssetStatusApprovalArchiveOld(long transId){
//String query_r ="update am_asset_approval set asset_status=? where asset_id = '"+assetId+"'";
String query_r ="update am_asset_approval_archive set asset_status=? where transaction_id = ?";
Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"ACTIVE");
            ps.setLong(2, transId);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusApprovalArchive()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }


}//updateAssetStatusApproval()

public void updateAssetStatusApprovalArchive(long transId){
	//String query_r ="update am_asset_approval set asset_status=? where asset_id = '"+assetId+"'";
	String query_r ="update am_asset_approval_archive set asset_status=? where transaction_id = ?";
	try (Connection con = dbConnection.getConnection("legendPlus");
			PreparedStatement ps = con.prepareStatement(query_r)) {

	            ps.setString(1,"ACTIVE");
	            ps.setLong(2, transId);
	           int i =ps.executeUpdate();

	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateAssetStatusApprovalArchive()>>>>>" + ex);
	        } 


	}//updateAssetStatusApproval()


public void updateAssetStatusArchiveOld(String assetId){
String query_r ="update am_asset_archive set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"ACTIVE");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusArchive()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
}//updateAssetStatus()

public void updateAssetStatusArchive(String assetId){
String query_r ="update am_asset_archive set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";

try (Connection con = dbConnection.getConnection("legendPlus");
		PreparedStatement ps = con.prepareStatement(query_r)) {



            ps.setString(1,"ACTIVE");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusArchive()>>>>>" + ex);
        } 
}//updateAssetStatus()

public void updateAssetStatuxArchiveOld(String assetId){
String query_r ="update am_asset_archive set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setString(1,"APPROVED");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatuxArchive()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
}//updateAssetStatus()

public void updateAssetStatuxArchive(String assetId){
String query_r ="update am_asset_archive set asset_status=?,raise_entry=? where asset_id = '"+assetId+"'";

try (Connection con = dbConnection.getConnection("legendPlus");
		PreparedStatement ps = con.prepareStatement(query_r)) {



            ps.setString(1,"APPROVED");
            ps.setString(2,"Y");
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatuxArchive()>>>>>" + ex);
        } 
}//updateAssetStatus()
/*
public void incrementApprovalCountArchive(int tran_id,int count,int nextSupervisor){
String query_r ="update am_asset_approval_archive set approval_level_count=?,super_id=? where transaction_id =?";

Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

try {
    con = dbConnection.getConnection("legendPlus");


ps = con.prepareStatement(query_r);



            ps.setInt(1,count);
            ps.setInt(2, nextSupervisor);
            ps.setInt(3,tran_id);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: incrementApprovalCountArchive()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
}//incrementApprovalCount()
*/



    public String getCurrentMtid(String tableName) {

        String query =
                "select max(mt_id) from IA_MTID_TABLE where mt_tablename=?";// +tableName + "' ";
        String mtid = "0";
        Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
                       ps = con.prepareStatement(query);
                       ps.setString(1, tableName);
                       rs = ps.executeQuery();

            while (rs.next()) {

                mtid = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return mtid;

    }

   public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address, String costdebitAcctName,String costcreditAcctName) {
    boolean done=true;


    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	 Connection  con = dbConnection.getConnection("legendPlus");
    	 PreparedStatement  ps = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    return done;
}


     public boolean updateRaiseEntryTransaction( String asset_id,String page1,String transactionId ,String iso,String ip_address) {
		    boolean done=true;

			 
		    String query = "update am_raisentry_transaction set iso=?,transaction_date=?,system_ip=? where transactionId=?    and ASSET_ID=? and page1=? " ;

		    try {
		    	Connection	con = dbConnection.getConnection("legendPlus");
		    	PreparedStatement ps = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3, ip_address);
                        ps.setString(4,transactionId);

		        ps.setString(5,asset_id);
		        ps.setString(6, page1);


		        ps.execute();

		    }
		    catch (Exception ex)
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
		        ex.printStackTrace();
		    } 
		    return done;
		}


 public void incrementApprovalCount2(int tran_id,int count,int nextSupervisor){
String query_r ="update am_asset_approval set approval_level_count=?,super_id=?,posting_date=? where transaction_id =?";


try {
	Connection con = dbConnection.getConnection("legendPlus");
	PreparedStatement ps = con.prepareStatement(query_r);



            ps.setInt(1,count);
            ps.setInt(2, nextSupervisor);
            ps.setTimestamp(3, dbConnection.getDateTime(new java.util.Date()));
            ps.setInt(4,tran_id);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: incrementApprovalCount()>>>>>" + ex);
        } 


}//incrementApprovalCount()
 
 public void incrementApprovalCount2(long tran_id,int count,int nextSupervisor){
String query_r ="update am_asset_approval set approval_level_count=?,super_id=?,posting_date=? where transaction_id =?";

try {
	Connection con = dbConnection.getConnection("legendPlus");
	PreparedStatement ps = con.prepareStatement(query_r);



            ps.setInt(1,count);
            ps.setInt(2, nextSupervisor);
            ps.setTimestamp(3, dbConnection.getDateTime(new java.util.Date()));
            ps.setLong(4,tran_id);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: incrementApprovalCount()>>>>>" + ex);
        } 


}//incrementApprovalCount()

 public void incrementApprovalCountArchive(int tran_id,int count,int nextSupervisor){
String query_r ="update am_asset_approval_archive set approval_level_count=?,super_id=?,posting_date=? where transaction_id =?";

try {
	Connection con = dbConnection.getConnection("legendPlus");
	PreparedStatement ps = con.prepareStatement(query_r);



            ps.setInt(1,count);
            ps.setInt(2, nextSupervisor);
            ps.setTimestamp(3, dbConnection.getDateTime(new java.util.Date()));
            ps.setInt(4,tran_id);
            //ps.setString(2,reject_reason);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: incrementApprovalCountArchive()>>>>>" + ex);
        } 


}//incrementApprovalCount()

 public void getAssetRecordsForApprovalArchive(String assetId, String update_changes) throws Exception {

        if (!(assetId.equalsIgnoreCase(""))) {
            String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                           "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                           "A.VENDOR_AC, A.DATE_PURCHASED," +
                           "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                           "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                           "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                           "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                           "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                           "A.REMAINING_LIFE, A.VATABLE_COST,TRANPORT_COST,OTHER_COST, A.VAT, A.REQ_DEPRECIATION," +
                           "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                           "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                           "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                           "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                           "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE,A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent " +
                           "FROM AM_ASSETUPDATE A " +
                           "WHERE A.ASSET_ID = ? ";

 

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
            	PreparedStatement ps = con.prepareStatement(query);
            	ps.setString(1, assetId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    registration_no = rs.getString("REGISTRATION_NO");
                    branch_id = rs.getString("BRANCH_ID");
                    department_id = rs.getString("DEPT_ID");
                    category_id = rs.getString("CATEGORY_ID");
                    sub_category_id = rs.getString("SUB_CATEGORY_ID");
                    depreciation_start_date = dbConnection.formatDate(rs.
                            getDate("EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnection.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    make = rs.getString("ASSET_MAKE");
                    location = rs.getString("LOCATION");
                    maintained_by = rs.getString("ASSET_MAINTENANCE");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("SUPPLIER_NAME");
                    reason = rs.getString("PURCHASE_REASON");
                    asset_id = rs.getString("ASSET_ID");
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    vatable_cost = rs.getString("VATABLE_COST");
                    transport_cost = rs.getString("TRANPORT_COST");
                    other_cost = rs.getString("OTHER_COST");
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("USER_ID");
                    depreciation_rate = rs.getString("DEP_RATE");
                    residual_value = rs.getString("RESIDUAL_VALUE");
                    require_depreciation = rs.getString("REQ_DEPRECIATION");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    date_of_purchase = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    who_to_remind = rs.getString("WHO_TO_REM");
                    email_1 = rs.getString("EMAIL1");
                    email2 = rs.getString("EMAIL2");
                    raise_entry = rs.getString("RAISE_ENTRY");
                    section = rs.getString("SECTION_ID");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    status = rs.getString("ASSET_STATUS");
                    section_id = rs.getString("SECTION");
                    wh_tax_cb = rs.getString("WH_TAX");
                    wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                    require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                    spare_1 = rs.getString("SPARE_1");
                    spare_2 = rs.getString("SPARE_2");
                    spare_3 = rs.getString("SPARE_3");
                    spare_4 = rs.getString("SPARE_4");
                    spare_5 = rs.getString("SPARE_5");
                    spare_6 = rs.getString("SPARE_6");
                    who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                    driver = rs.getString("DRIVER");
                    state = rs.getString("STATE");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    multiple = rs.getString("MULTIPLE");
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "POSTING_DATE"));
                    warrantyStartDate = dbConnection.formatDate(rs.getDate(
                            "WAR_START_DATE"));
                    noOfMonths = rs.getString("WAR_MONTH");
                    expiryDate = dbConnection.formatDate(rs.getDate(
                            "WAR_EXPIRY_DATE"));
                    authuser = rs.getString("ASSET_USER");
                    amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                    amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                    partPAY = rs.getString("PART_PAY");
                    fullyPAID =rs.getString("FULLY_PAID");
                    group_id =rs.getString("GROUP_ID");
                    bar_code = rs.getString("BAR_CODE");
                    sbu_code = rs.getString("SBU_CODE");
                    lpo = rs.getString("LPO");
                    setSupervisor(rs.getString("SUPERVISOR"));
                    deferPay = rs.getString("defer_pay");
                    selectTax = String.valueOf(rs.getDouble("wht_percent"));

                } else {
                    System.out.print("nothing");
                }//else

                if(update_changes.equalsIgnoreCase("YES")){

                    String updateQuery = "UPDATE am_asset_archive SET REGISTRATION_NO = ?," +
         "ASSET_MAINTENANCE = ?, AUTHORIZED_BY = ? , SUPPLIER_NAME = ?, PURCHASE_REASON = ?,"+
          "DESCRIPTION = ?, VENDOR_AC = ? , ASSET_SERIAL_NO = ?, ASSET_MODEL = ?,"+
          "USER_ID = ?, SUBJECT_TO_VAT = ? , WHO_TO_REM = ?, EMAIL1 = ?,"+
          "EMAIL2 = ?, WH_TAX = ? , SPARE_1 = ?, SPARE_2 = ?,"+
          "WHO_TO_REM_2 = ?, DRIVER = ? , ASSET_ENGINE_NO = ?, WAR_MONTH = ?,"+
          "BAR_CODE = ?, LPO = ?, wht_percent=?,SPARE_3 = ?, SPARE_4 = ?,SPARE_5 = ?, SPARE_6 = ? WHERE ASSET_ID = ? ";

                    PreparedStatement ps2 = con.prepareStatement(updateQuery);
                    ps2.setString(1, registration_no);
                    ps2.setString(2, maintained_by);
                    ps2.setString(3, authorized_by);
                    ps2.setString(4, supplied_by);
                    ps2.setString(5, reason);
                    ps2.setString(6, description.toUpperCase());
                    ps2.setString(7, vendor_account);
                    ps2.setString(8, serial_number);
                    ps2.setString(9, model);
                    ps2.setString(10, user);
                    ps2.setString(11, subject_to_vat);
                    ps2.setString(12, who_to_remind);
                    ps2.setString(13, email_1);
                    ps2.setString(14, email2);
                    ps2.setString(15, wh_tax_cb);
                    ps2.setString(16, spare_1);
                    ps2.setString(17, spare_2);
                    ps2.setString(18, who_to_remind_2);
                    ps2.setString(19, driver);
                    ps2.setString(20, engine_number);
                    ps2.setString(21, noOfMonths);
                    ps2.setString(22, bar_code);
                    ps2.setString(23, lpo);
                    ps2.setDouble(24,Double.parseDouble(selectTax));
                    ps2.setString(25, spare_3);
                    ps2.setString(26, spare_4);
                    ps2.setString(27, spare_5);
                    ps2.setString(28, spare_6);
                    ps2.setString(29, asset_id);

                int i =ps2.executeUpdate();


                }//if(update_changes.equalsIgnoreCase("YES"))


            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getAssetRecordsForApprovalArchive()WARN: Error fetching all asset/ updating am_assetUpdate table ->" + ex);
            } 
        }
    }

 public void getAsset2RecordsForApprovalArchive(String assetId, String update_changes) throws Exception {

        if (!(assetId.equalsIgnoreCase(""))) {
            String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                           "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                           "A.VENDOR_AC, A.DATE_PURCHASED," +
                           "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                           "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                           "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                           "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                           "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                           "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                           "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                           "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                           "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                           "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                           "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE,A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent " +
                           "FROM AM_ASSETUPDATE A " +
                           "WHERE A.ASSET_ID = ? ";

           

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
            	PreparedStatement ps = con.prepareStatement(query);
            	ps.setString(1, assetId);
            	ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    registration_no = rs.getString("REGISTRATION_NO");
                    branch_id = rs.getString("BRANCH_ID");
                    department_id = rs.getString("DEPT_ID");
                    category_id = rs.getString("CATEGORY_ID");
                    sub_category_id = rs.getString("SUB_CATEGORY_ID");
                    depreciation_start_date = dbConnection.formatDate(rs.
                            getDate("EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnection.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    make = rs.getString("ASSET_MAKE");
                    location = rs.getString("LOCATION");
                    maintained_by = rs.getString("ASSET_MAINTENANCE");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("SUPPLIER_NAME");
                    reason = rs.getString("PURCHASE_REASON");
                    asset_id = rs.getString("ASSET_ID");
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    vatable_cost = rs.getString("VATABLE_COST");
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("USER_ID");
                    depreciation_rate = rs.getString("DEP_RATE");
                    residual_value = rs.getString("RESIDUAL_VALUE");
                    require_depreciation = rs.getString("REQ_DEPRECIATION");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    date_of_purchase = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    who_to_remind = rs.getString("WHO_TO_REM");
                    email_1 = rs.getString("EMAIL1");
                    email2 = rs.getString("EMAIL2");
                    raise_entry = rs.getString("RAISE_ENTRY");
                    section = rs.getString("SECTION_ID");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    status = rs.getString("ASSET_STATUS");
                    section_id = rs.getString("SECTION");
                    wh_tax_cb = rs.getString("WH_TAX");
                    wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                    require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                    spare_1 = rs.getString("SPARE_1");
                    spare_2 = rs.getString("SPARE_2");
                    spare_3 = rs.getString("SPARE_3");
                    spare_4 = rs.getString("SPARE_4");
                    spare_5 = rs.getString("SPARE_5");
                    spare_6 = rs.getString("SPARE_6");
                    who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                    driver = rs.getString("DRIVER");
                    state = rs.getString("STATE");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    multiple = rs.getString("MULTIPLE");
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "POSTING_DATE"));
                    warrantyStartDate = dbConnection.formatDate(rs.getDate(
                            "WAR_START_DATE"));
                    noOfMonths = rs.getString("WAR_MONTH");
                    expiryDate = dbConnection.formatDate(rs.getDate(
                            "WAR_EXPIRY_DATE"));
                    authuser = rs.getString("ASSET_USER");
                    amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                    amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                    partPAY = rs.getString("PART_PAY");
                    fullyPAID =rs.getString("FULLY_PAID");
                    group_id =rs.getString("GROUP_ID");
                    bar_code = rs.getString("BAR_CODE");
                    sbu_code = rs.getString("SBU_CODE");
                    lpo = rs.getString("LPO");
                    setSupervisor(rs.getString("SUPERVISOR"));
                    deferPay = rs.getString("defer_pay");
                    selectTax = String.valueOf(rs.getDouble("wht_percent"));

                } else {
                    System.out.print("nothing");
                }//else

                if(update_changes.equalsIgnoreCase("YES")){

                    String updateQuery = "UPDATE am_asset2_archive SET REGISTRATION_NO = ?," +
         "ASSET_MAINTENANCE = ?, AUTHORIZED_BY = ? , SUPPLIER_NAME = ?, PURCHASE_REASON = ?,"+
          "DESCRIPTION = ?, VENDOR_AC = ? , ASSET_SERIAL_NO = ?, ASSET_MODEL = ?,"+
          "USER_ID = ?, SUBJECT_TO_VAT = ? , WHO_TO_REM = ?, EMAIL1 = ?,"+
          "EMAIL2 = ?, WH_TAX = ? , SPARE_1 = ?, SPARE_2 = ?,"+
          "WHO_TO_REM_2 = ?, DRIVER = ? , ASSET_ENGINE_NO = ?, WAR_MONTH = ?,"+
          "BAR_CODE = ?, LPO = ?, wht_percent=?,SPARE_3 = ?, SPARE_4 = ?,SPARE_5 = ?, SPARE_6 = ? WHERE ASSET_ID = ? ";

                    PreparedStatement ps2 = con.prepareStatement(updateQuery);
                    ps2.setString(1, registration_no);
                    ps2.setString(2, maintained_by);
                    ps2.setString(3, authorized_by);
                    ps2.setString(4, supplied_by);
                    ps2.setString(5, reason);
                    ps2.setString(6, description.toUpperCase());
                    ps2.setString(7, vendor_account);
                    ps2.setString(8, serial_number);
                    ps2.setString(9, model);
                    ps2.setString(10, user);
                    ps2.setString(11, subject_to_vat);
                    ps2.setString(12, who_to_remind);
                    ps2.setString(13, email_1);
                    ps2.setString(14, email2);
                    ps2.setString(15, wh_tax_cb);
                    ps2.setString(16, spare_1);
                    ps2.setString(17, spare_2);
                    ps2.setString(18, who_to_remind_2);
                    ps2.setString(19, driver);
                    ps2.setString(20, engine_number);
                    ps2.setString(21, noOfMonths);
                    ps2.setString(22, bar_code);
                    ps2.setString(23, lpo);
                    ps2.setDouble(24,Double.parseDouble(selectTax));
                    ps2.setString(25, spare_3);
                    ps2.setString(26, spare_4);
                    ps2.setString(27, spare_5);
                    ps2.setString(28, spare_6);
                    ps2.setString(29, asset_id);

                int i =ps2.executeUpdate();


                }//if(update_changes.equalsIgnoreCase("YES"))


            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getAssetRecordsForApprovalArchive()WARN: Error fetching all asset/ updating am_assetUpdate table ->" + ex);
            } 
        }
    }

public void getAssetRecordsForApproval(String assetId, String update_changes) throws Exception {

        if (!(assetId.equalsIgnoreCase(""))) {
            String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                           "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                           "A.VENDOR_AC, A.DATE_PURCHASED," +
                           "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                           "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                           "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                           "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                           "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                           "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                           "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                           "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                           "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                           "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                           "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE,A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent " +
                           "FROM AM_ASSETUPDATE A " +
                           "WHERE A.ASSET_ID = '? ";

           

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
            	PreparedStatement ps = con.prepareStatement(query);
            	ps.setString(1, assetId);
            	ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    registration_no = rs.getString("REGISTRATION_NO");
                    branch_id = rs.getString("BRANCH_ID");
                    department_id = rs.getString("DEPT_ID");
                    category_id = rs.getString("CATEGORY_ID");
                    sub_category_id = rs.getString("SUB_CATEGORY_ID");
                    depreciation_start_date = dbConnection.formatDate(rs.
                            getDate("EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnection.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    make = rs.getString("ASSET_MAKE");
                    location = rs.getString("LOCATION");
                    maintained_by = rs.getString("ASSET_MAINTENANCE");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("SUPPLIER_NAME");
                    reason = rs.getString("PURCHASE_REASON");
                    asset_id = rs.getString("ASSET_ID");
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    vatable_cost = rs.getString("VATABLE_COST");
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("USER_ID");
                    depreciation_rate = rs.getString("DEP_RATE");
                    residual_value = rs.getString("RESIDUAL_VALUE");
                    require_depreciation = rs.getString("REQ_DEPRECIATION");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    date_of_purchase = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    who_to_remind = rs.getString("WHO_TO_REM");
                    email_1 = rs.getString("EMAIL1");
                    email2 = rs.getString("EMAIL2");
                    raise_entry = rs.getString("RAISE_ENTRY");
                    section = rs.getString("SECTION_ID");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    status = rs.getString("ASSET_STATUS");
                    section_id = rs.getString("SECTION");
                    wh_tax_cb = rs.getString("WH_TAX");
                    wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                    require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                    spare_1 = rs.getString("SPARE_1");
                    spare_2 = rs.getString("SPARE_2");
                    spare_3 = rs.getString("SPARE_3");
                    spare_4 = rs.getString("SPARE_4");
                    spare_5 = rs.getString("SPARE_5");
                    spare_6 = rs.getString("SPARE_6");
                    who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                    driver = rs.getString("DRIVER");
                    state = rs.getString("STATE");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    multiple = rs.getString("MULTIPLE");
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "POSTING_DATE"));
                    warrantyStartDate = dbConnection.formatDate(rs.getDate(
                            "WAR_START_DATE"));
                    noOfMonths = rs.getString("WAR_MONTH");
                    expiryDate = dbConnection.formatDate(rs.getDate(
                            "WAR_EXPIRY_DATE"));
                    authuser = rs.getString("ASSET_USER");
                    amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                    amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                    partPAY = rs.getString("PART_PAY");
                    fullyPAID =rs.getString("FULLY_PAID");
                    group_id =rs.getString("GROUP_ID");
                    bar_code = rs.getString("BAR_CODE");
                    sbu_code = rs.getString("SBU_CODE");
                    lpo = rs.getString("LPO");
                    setSupervisor(rs.getString("SUPERVISOR"));
                    deferPay = rs.getString("defer_pay");
                    selectTax = String.valueOf(rs.getInt("wht_percent"));

                } else {
                    System.out.print("nothing");
                }//else

                if(update_changes.equalsIgnoreCase("YES")){

                    String updateQuery = "UPDATE am_asset SET REGISTRATION_NO = ?," +
         "ASSET_MAINTENANCE = ?, AUTHORIZED_BY = ? , SUPPLIER_NAME = ?, PURCHASE_REASON = ?,"+
          "DESCRIPTION = ?, VENDOR_AC = ? , ASSET_SERIAL_NO = ?, ASSET_MODEL = ?,"+
          "USER_ID = ?, SUBJECT_TO_VAT = ? , WHO_TO_REM = ?, EMAIL1 = ?,"+
          "EMAIL2 = ?, WH_TAX = ? , SPARE_1 = ?, SPARE_2 = ?,"+
          "WHO_TO_REM_2 = ?, DRIVER = ? , ASSET_ENGINE_NO = ?, WAR_MONTH = ?,"+
          "BAR_CODE = ?, LPO = ?, wht_percent=?, SPARE_3 = ?, SPARE_4 = ?, SPARE_5 = ?, SPARE_6 = ?  WHERE ASSET_ID = ?";

                    PreparedStatement ps2  = con.prepareStatement(updateQuery);
                    ps2.setString(1, registration_no);
                    ps2.setString(2, maintained_by);
                    ps2.setString(3, authorized_by);
                    ps2.setString(4, supplied_by);
                    ps2.setString(5, reason);
                    ps2.setString(6, description.toUpperCase());
                    ps2.setString(7, vendor_account);
                    ps2.setString(8, serial_number);
                    ps2.setString(9, model);
                    ps2.setString(10, user);
                    ps2.setString(11, subject_to_vat);
                    ps2.setString(12, who_to_remind);
                    ps2.setString(13, email_1);
                    ps2.setString(14, email2);
                    ps2.setString(15, wh_tax_cb);
                    ps2.setString(16, spare_1);
                    ps2.setString(17, spare_2);
                    ps2.setString(18, who_to_remind_2);
                    ps2.setString(19, driver);
                    ps2.setString(20, engine_number);
                    ps2.setString(21, noOfMonths);
                    ps2.setString(22, bar_code);
                    ps2.setString(23, lpo);
                    ps2.setDouble(24,Double.parseDouble(selectTax));
                    ps2.setString(25, spare_3);
                    ps2.setString(26, spare_4);
                    ps2.setString(27, spare_5);
                    ps2.setString(28, spare_6);
                    ps2.setString(29, asset_id);


                int i =ps2.executeUpdate();


                }//if(update_changes.equalsIgnoreCase("YES"))


            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getAssetRecordsForApproval()WARN: Error fetching all asset/ updating am_assetUpdate table ->" + ex);
            } 
            }
        }



public void getAsset2RecordsForApproval(String assetId, String update_changes) throws Exception {

        if (!(assetId.equalsIgnoreCase(""))) {
            String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                           "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                           "A.VENDOR_AC, A.DATE_PURCHASED," +
                           "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                           "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                           "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                           "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                           "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                           "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                           "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                           "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                           "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                           "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                           "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE,A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent " +
                           "FROM AM_ASSETUPDATE A " +
                           "WHERE A.ASSET_ID = ? ";

           

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
            	PreparedStatement ps = con.prepareStatement(query);
            	ps.setString(1, assetId);
            	ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    registration_no = rs.getString("REGISTRATION_NO");
                    branch_id = rs.getString("BRANCH_ID");
                    department_id = rs.getString("DEPT_ID");
                    category_id = rs.getString("CATEGORY_ID");
                    sub_category_id = rs.getString("SUB_CATEGORY_ID");
                    depreciation_start_date = dbConnection.formatDate(rs.
                            getDate("EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnection.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    make = rs.getString("ASSET_MAKE");
                    location = rs.getString("LOCATION");
                    maintained_by = rs.getString("ASSET_MAINTENANCE");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("SUPPLIER_NAME");
                    reason = rs.getString("PURCHASE_REASON");
                    asset_id = rs.getString("ASSET_ID");
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    vatable_cost = rs.getString("VATABLE_COST");
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("USER_ID");
                    depreciation_rate = rs.getString("DEP_RATE");
                    residual_value = rs.getString("RESIDUAL_VALUE");
                    require_depreciation = rs.getString("REQ_DEPRECIATION");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    date_of_purchase = dbConnection.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    who_to_remind = rs.getString("WHO_TO_REM");
                    email_1 = rs.getString("EMAIL1");
                    email2 = rs.getString("EMAIL2");
                    raise_entry = rs.getString("RAISE_ENTRY");
                    section = rs.getString("SECTION_ID");
                    accum_dep = rs.getDouble("ACCUM_DEP");
                    status = rs.getString("ASSET_STATUS");
                    section_id = rs.getString("SECTION");
                    wh_tax_cb = rs.getString("WH_TAX");
                    wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                    require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                    spare_1 = rs.getString("SPARE_1");
                    spare_2 = rs.getString("SPARE_2");
                    spare_3 = rs.getString("SPARE_3");
                    spare_4 = rs.getString("SPARE_4");
                    spare_5 = rs.getString("SPARE_5");
                    spare_6 = rs.getString("SPARE_6");
                    who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                    driver = rs.getString("DRIVER");
                    state = rs.getString("STATE");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    multiple = rs.getString("MULTIPLE");
                    posting_date = dbConnection.formatDate(rs.getDate(
                            "POSTING_DATE"));
                    warrantyStartDate = dbConnection.formatDate(rs.getDate(
                            "WAR_START_DATE"));
                    noOfMonths = rs.getString("WAR_MONTH");
                    expiryDate = dbConnection.formatDate(rs.getDate(
                            "WAR_EXPIRY_DATE"));
                    authuser = rs.getString("ASSET_USER");
                    amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                    amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                    partPAY = rs.getString("PART_PAY");
                    fullyPAID =rs.getString("FULLY_PAID");
                    group_id =rs.getString("GROUP_ID");
                    bar_code = rs.getString("BAR_CODE");
                    sbu_code = rs.getString("SBU_CODE");
                    lpo = rs.getString("LPO");
                    setSupervisor(rs.getString("SUPERVISOR"));
                    deferPay = rs.getString("defer_pay");
                    selectTax = String.valueOf(rs.getInt("wht_percent"));

                } else {
                    System.out.print("nothing");
                }//else

                if(update_changes.equalsIgnoreCase("YES")){

                    String updateQuery = "UPDATE am_asset2 SET REGISTRATION_NO = ?," +
         "ASSET_MAINTENANCE = ?, AUTHORIZED_BY = ? , SUPPLIER_NAME = ?, PURCHASE_REASON = ?,"+
          "DESCRIPTION = ?, VENDOR_AC = ? , ASSET_SERIAL_NO = ?, ASSET_MODEL = ?,"+
          "USER_ID = ?, SUBJECT_TO_VAT = ? , WHO_TO_REM = ?, EMAIL1 = ?,"+
          "EMAIL2 = ?, WH_TAX = ? , SPARE_1 = ?, SPARE_2 = ?,"+
          "WHO_TO_REM_2 = ?, DRIVER = ? , ASSET_ENGINE_NO = ?, WAR_MONTH = ?,"+
          "BAR_CODE = ?, LPO = ?, wht_percent=?, SPARE_3 = ?, SPARE_4 = ?, SPARE_5 = ?, SPARE_6 = ?  WHERE ASSET_ID = ?";

                    PreparedStatement updatePs = con.prepareStatement(updateQuery);
                    
                    updatePs.setString(1, registration_no);
                    updatePs.setString(2, maintained_by);
                    updatePs.setString(3, authorized_by);
                    updatePs.setString(4, supplied_by);
                    updatePs.setString(5, reason);
                    updatePs.setString(6, description != null ? description.toUpperCase() : null);
                    updatePs.setString(7, vendor_account);
                    updatePs.setString(8, serial_number);
                    updatePs.setString(9, model);
                    updatePs.setString(10, user);
                    updatePs.setString(11, subject_to_vat);
                    updatePs.setString(12, who_to_remind);
                    updatePs.setString(13, email_1);
                    updatePs.setString(14, email2);
                    updatePs.setString(15, wh_tax_cb);
                    updatePs.setString(16, spare_1);
                    updatePs.setString(17, spare_2);
                    updatePs.setString(18, who_to_remind_2);
                    updatePs.setString(19, driver);
                    updatePs.setString(20, engine_number);
                    updatePs.setString(21, noOfMonths);
                    updatePs.setString(22, bar_code);
                    updatePs.setString(23, lpo);
                    updatePs.setDouble(24, Double.parseDouble(selectTax));
                    updatePs.setString(25, spare_3);
                    updatePs.setString(26, spare_4);
                    updatePs.setString(27, spare_5);
                    updatePs.setString(28, spare_6);
                    updatePs.setString(29, asset_id);



                int i =ps.executeUpdate();


                }//if(update_changes.equalsIgnoreCase("YES"))


            } catch (Exception ex) {
                System.out.println("AssetRecordsBean: getAsset2RecordsForApproval()WARN: Error fetching all asset/ updating am_assetUpdate table ->" + ex);
            } 
        }
    }

 public void setPendingTransRepost(String[] a, String code,int mtid){
//	 System.out.println("====code 3====> "+code);
        int transaction_level=0;
     
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code = ? ";
    
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
        	PreparedStatement ps = con.prepareStatement(tranLevelQuery);
        	ps.setString(1, code);
        	ResultSet rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if

            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setLong(13,mtid);
            ps.setString(14, String.valueOf(mtid));
            ps.setInt(15, transaction_level);

            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTransRepost()>>>>>>" + er);

        }
   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
    }//staticApprovalInfo()

    public void updateAssetStatus(String assetid, String status,String space) {
    	
        String NOTIFY_QUERY = " update am_asset_approval set process_status=?,approval_level_count=?,approval1=?,approval2=?,approval3=?"+
                ",approval4=?,approval5=?,super_id=?,amount=? where asset_id=? ";
     
        if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
              if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
        vatable_cost = vatable_cost.replaceAll(",", "");
        vat_amount = vat_amount.replaceAll(",", "");
        
        try {

              double costPrice = Double.parseDouble(vat_amount) +
                           Double.parseDouble(vatable_cost);

            Connection con = dbConnection.getConnection("legendPlus");
          	PreparedStatement ps  = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, status);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setInt(8,Integer.parseInt(getSupervisor()));
            ps.setDouble(9, costPrice);
            ps.setString(10, assetid);


            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset_approval+" + ex.getMessage());
        } 

    }



 public boolean insertRaiseEntryTransactionTranId(String userId,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,long transId ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,userId);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);
        ps.setLong(12, transId);

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction insertRaiseEntryTransactionTranId ->" );
        ex.printStackTrace();
    } 
    return done;
}

public boolean insertRaiseEntryTransactionArchiveTranId(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String systemIp,String mac_Address,long tranId ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction_archive](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,mac_address,Trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11, systemIp);
        ps.setString(12, mac_Address);
        ps.setLong(13,tranId);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction_archive->" );
        ex.printStackTrace();
    } 
    
    return done;
}
     public boolean updateRaiseEntryTransaction( String asset_id,String page1,String transactionId ,String iso,String ip_address,String tranId) {
		    boolean done=true;
		    
		    String query = "update am_raisentry_transaction set iso=?,transaction_date=?,system_ip=? where transactionId=?    and ASSET_ID=? and page1=? and Trans_id=?" ;

		    try {
		    	Connection con = dbConnection.getConnection("legendPlus");
	        	PreparedStatement ps  = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3, ip_address);
                ps.setString(4,transactionId);
		        ps.setString(5,asset_id);
		        ps.setString(6, page1);
                         ps.setString(7, tranId);


		        ps.execute();

		    }
		    catch (Exception ex)
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction->" );
		        ex.printStackTrace();
		    } 
		    return done;
		}

     public boolean updateRaiseEntryTransactionArchive( String asset_id,String page1,String transactionId ,String iso,String systemIp,String mac_Address,String tranId) {
		    boolean done=true;

		    String query = "update am_raisentry_transaction_archive set iso=?,transaction_date=? ,system_ip=?,mac_address=? where transactionId=?    and ASSET_ID=? and page1=?  and Trans_id=?" ;

		    try {
		    	Connection con = dbConnection.getConnection("legendPlus");
	        	PreparedStatement ps  = con.prepareStatement(query);
		        ps.setString(1,iso);
		        ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
		        ps.setString(3, systemIp);
                        ps.setString(4, mac_Address);
                        ps.setString(5,transactionId);
		        ps.setString(6,asset_id);
		        ps.setString(7, page1);
                        ps.setString(8, tranId);

		        ps.execute();

		    }
		    catch (Exception ex)
		    {
		 	   done = false;
		        System.out.println("WARNING:cannot update am_raisentry_transaction_archive->" );
		        ex.printStackTrace();
		    } 
		    return done;
		}
        public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,String tranId ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,Trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);
        ps.setString(12, tranId);

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    return done;
}

        public boolean insertRaiseEntryTransactionArchive(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String systemIp,String mac_Address,String tranId ) {
    boolean done=true;

	
    String query = "INSERT INTO [am_raisentry_transaction_archive](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,mac_address,Trans_id)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11, systemIp);
        ps.setString(12, mac_Address);
        ps.setString(13, tranId);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction_archive->" );
        ex.printStackTrace();
    } 
    return done;
}

  public int insertAssetRecordUnclassified(String branch) throws Exception, Throwable {
    String[] budget = getBudgetInfo();
    double[] bugdetvalues = getBudgetValues();
    int DONE = 0; //everything is oK
    int BUDGETENFORCED = 1; //EF budget = Yes, CF = NO, ERROR_FLAG
    int BUDGETENFORCEDCF = 2; //EF budget = Yes, CF = Yes, ERROR_FLAG
    int ASSETPURCHASEBD = 3; //asset falls into no quarter purchase date older than bugdet
    String Q = getQuarter();
    budget[3] = "N";
//    System.out.println("======budget[3]: "+budget[3]);
    if(budget[3].equalsIgnoreCase("N")){
//    	System.out.println("======1======== ");
    	rinsertAssetRecordUnclassified(branch);
//    	System.out.println("======2======== ");
		return DONE;
	}
	else if(budget[3].equalsIgnoreCase("Y")){
    if (!Q.equalsIgnoreCase("NQ")) {
        if (budget[3].equalsIgnoreCase("Y") &&
            budget[4].equalsIgnoreCase("N")) {
            if (chkBudgetAllocation(Q, bugdetvalues, false)) {
                updateBudget(Q, budget);
                rinsertAssetRecordUnclassified(branch);
                return DONE;
            } else {
                return BUDGETENFORCED;
            }

        } else if (budget[3].equalsIgnoreCase("Y") &&
                   budget[4].equalsIgnoreCase("Y")) {
            if (chkBudgetAllocation(Q, bugdetvalues, true)) {
                updateBudget(Q, budget);
                rinsertAssetRecordUnclassified(branch);
                return DONE;
            } else {
                return BUDGETENFORCEDCF;
            }

        } else {
            //rinsertAssetRecord();
        	rinsertAssetRecordUnclassified(branch);
            return DONE;
        }
    } else {
        //rinsertAssetRecord();
        return ASSETPURCHASEBD;
    }}
    return 0;
}

private boolean rinsertAssetRecordUnclassified(String branch) throws Exception, Throwable {
	
//    System.out.println("=====>>>>branch_id: "+branch_id+"  ======>>>branch: "+branch);
	if (branch_id == null || branch_id.equals("") || branch_id.equals("0")) {
		branch_id = branch;
	}	
    asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
            department_id, section_id, category_id);
   
    boolean done = true;
AssetPaymentManager payment = null;
//System.out.println("====Uncapitlized Asset Id: "+asset_id);
    /*if (require_redistribution.equalsIgnoreCase("Y")) {
        status = "Z";
             }*/
    if (make == null || make.equals("")) {
        make = "0";
    }
    if (maintained_by == null || maintained_by.equals("")) {
        maintained_by = "0";
    }
    if (supplied_by == null || supplied_by.equals("")) {
        supplied_by = "0";
    }
    if (user == null || user.equals("")) {
        user = "";
    }
    if (location == null || location.equals("")) {
        location = "0";
    }
    if (driver == null || driver.equals("")) {
        driver = "0";
    }
    if (state == null || state.equals("")) {
        state = "0";
    }
    if (department_id == null || department_id.equals("")) {
        department_id = "0";
    }
    if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
    if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
    if (transport_cost == null || transport_cost.equals("")) {
    	transport_cost = "0.0";
    }
    if (other_cost == null || other_cost.equals("")) {
    	other_cost = "0.0";
    }
    if (wh_tax_amount == null || wh_tax_amount.equals("")) {
        wh_tax_amount = "0";
    }
    if (province == null || province.equals("")) {
        province = "0";
    }
    if (category_id == null || category_id.equals("")) {
        category_id = "0";
    }
    if (sub_category_id == null || sub_category_id.equals("")) {
    	sub_category_id = "0";
    }
    if (integrifyId == null || integrifyId.equals("")) {
    	integrifyId = "0";
    }

    if (residual_value == null || residual_value.equals("")) {
        residual_value = "0";
    }
    if (section_id == null || section_id.equals("")) {
        section_id = "0";
    }

    if (noOfMonths == null || noOfMonths.equals("")) {
        noOfMonths = "0";
    }
    if (warrantyStartDate == null || warrantyStartDate.equals("")) {
        warrantyStartDate = null;
    }
    if (expiryDate == null || expiryDate.equals("")) {
        expiryDate = null;
    }
//if (supervisor == null || expiryDate.equals("")) {
    //    expiryDate = null;
   // }

/*
if (bar_code == null || bar_code.equals("")) {
        bar_code = null;
    }
*/
/*      if (sbu_code == null || sbu_code.equals("")) {
        sbu_code=("0");
    }
*/

/*
if (lpo == null || lpo.equals("")) {
        lpo=("0");
    }
*/
    vat_amount = vat_amount.replaceAll(",", "");
    vatable_cost = vatable_cost.replaceAll(",", "");
    wh_tax_amount = wh_tax_amount.replaceAll(",", "");
    residual_value = residual_value.replaceAll(",", "");
    amountPTD = amountPTD.replaceAll(",","");

    assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET_UNCAPITALIZED"));

//    System.out.println("<<<<<<rinsertAssetRecordUnclassified assetCode: "+assetCode+"    selectTax: "+selectTax+"    integrifyId: "+integrifyId);
    
    String createQuery = "INSERT INTO AM_ASSET_UNCAPITALIZED         " +
                         "(" +
                         "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                         "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                         "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                         "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                         "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                         "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                         "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                         "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                         "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                         "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                         "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
                         "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                         "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                         "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                         "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent," +
                         "system_ip,mac_address,asset_code, SPARE_3, SPARE_4, SPARE_5, SPARE_6, SUB_CATEGORY_ID,SUB_CATEGORY_CODE,INTEGRIFY) " +

                         "VALUES" +
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                         " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";





    /*
     *First Create Asset Records
     * and then determine if it
     * should be made available for fleet.
     */
    if (this.chkidExists(asset_id)) {
        done = false;
        return done;
    }
    try {

        double costPrice = Double.parseDouble(vat_amount) +
                           Double.parseDouble(vatable_cost);

        Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(createQuery);
        ps.setString(1, asset_id);
        ps.setString(2, registration_no);
        ps.setInt(3, Integer.parseInt(branch_id));
        ps.setInt(4, Integer.parseInt(department_id));
        ps.setInt(5, Integer.parseInt(section_id));
        ps.setInt(6, Integer.parseInt(category_id));
//        System.out.println("TEST 1");
        ps.setString(7, description.toUpperCase());
        ps.setString(8, vendor_account);
        ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
        ps.setString(10, getDepreciationRate(category_id));
        ps.setString(11, make);
        ps.setString(12, model);
//        System.out.println("TEST 2");
        ps.setString(13, serial_number);
        ps.setString(14, engine_number);
        ps.setInt(15, Integer.parseInt(supplied_by));
        ps.setString(16, user);
        ps.setInt(17, Integer.parseInt(maintained_by));
        ps.setInt(18, 0);
        ps.setInt(19, 0);
        ps.setDouble(20, costPrice);
        ps.setDouble(21, costPrice);
//        System.out.println("TEST 3");
        ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
        ps.setDouble(23, Double.parseDouble(residual_value));
        ps.setString(24, authorized_by);
        ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
        ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
        ps.setString(27, reason);
        ps.setString(28, "0");
//        System.out.println("TEST 4");
        ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
        ps.setInt(30, Integer.parseInt(location));
        ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
//        System.out.println("TEST 5");
        ps.setDouble(32, Double.parseDouble(vatable_cost));
        ps.setDouble(33, Double.parseDouble(vat_amount));
        ps.setString(34, wh_tax_cb);
        ps.setDouble(35, Double.parseDouble(wh_tax_amount));
//        System.out.println("TEST 6");
        ps.setString(36, require_depreciation);
        ps.setString(37, require_redistribution);
        ps.setString(38, subject_to_vat);
        ps.setString(39, who_to_remind);
        ps.setString(40, email_1);
        ps.setString(41, who_to_remind_2);
        ps.setString(42, email2);
        ps.setString(43, raise_entry);
        ps.setString(44, "0");
        ps.setString(45, section);
//        System.out.println("TEST 7");
        ps.setInt(46, Integer.parseInt(state));
        ps.setInt(47, Integer.parseInt(driver));
//        System.out.println("TEST 8");
        ps.setString(48, spare_1);
        ps.setString(49, spare_2);
        ps.setString(50, status);
        ps.setString(51, user_id);
        ps.setString(52, multiple);
        ps.setString(53, province);
//        System.out.println("TEST 9");
        ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
        ps.setInt(55, new Integer(noOfMonths).intValue());
        ps.setDate(56, dbConnection.dateConvert(expiryDate));
        ps.setString(57, code.getBranchCode(branch_id));
        ps.setString(58, code.getDeptCode(department_id));
        ps.setString(59, code.getSectionCode(section_id));
        ps.setString(60, code.getCategoryCode(category_id));
        ps.setDouble(61, Double.parseDouble(amountPTD));
//        System.out.println("TEST 10");
        //ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
        //Use Vatable_cost instead of costPrice for Manage Asset Payment.====11/08/2009 ayojava
        ps.setDouble(62, (Double.parseDouble(vatable_cost)));
        ps.setString(63, partPAY);
        ps.setString(64, fullyPAID);
        ps.setString(65, bar_code);
        ps.setString(66,sbu_code);
        ps.setString(67, lpo);
        ps.setString(68,getSupervisor());
        ps.setString(69, deferPay);
        ps.setDouble(70, Double.parseDouble(selectTax));
        ps.setString(71, getSystemIp());
        ps.setString(72,getMacAddress());
        ps.setInt(73,assetCode);
        ps.setString(74, spare_3);
        ps.setString(75, spare_4);
        ps.setString(76, spare_5);
        ps.setString(77, spare_6);
//        System.out.println("TEST 11");
        ps.setInt(78, Integer.parseInt(sub_category_id));
        ps.setString(79, code.getSubCategoryCode(sub_category_id));
        ps.setString(80, integrifyId);
        ps.execute();
//        System.out.println("TEST 12");

        FleetHistoryManager fm = new FleetHistoryManager();
        if (fm.isRequiredForFleet(asset_id))
        {
            /*
             *Copy asset data to Fleet Master
             */
            fm.copyAssetDataToFleet(asset_id);
        }

        rinsertAssetRecord(asset_id);
    } catch (Exception ex) {
        done = false;
        System.out.println("WARN:Error creating asset->" + ex);
    } 

    return done;
}
public void getAssetRecordsUnclassified() throws Exception {

    if (asset_id != "") {
        String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code,INTEGRIFY " +
                       "FROM AM_ASSET_UNCAPITALIZED A " +
                       "WHERE A.ASSET_ID = '" + asset_id + "'";


        try {
        	Connection con = dbConnection.getConnection("legendPlus");
        	PreparedStatement ps  = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID");
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getInt("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("ASSET_CODE");
                integrifyId = rs.getString("INTEGRIFY");
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } 
    }
}

public String[] setApprovalDataBranch(String id){

	//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
	 //String currentDate  = reArrangeDate(getCurrentDate1());
	   // System.out.println("the $$$$$$$$$$$ "+currentDate);
	    String[] result= new String[12];
	    
	         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
	                 " description,effective_date,BRANCH_CODE,Asset_Status from AM_ASSET_UNCAPITALIZED where asset_id ='" +id+"'";



	        try {
	        	Connection con = dbConnection.getConnection("legendPlus");
	        	PreparedStatement ps  = con.prepareStatement(query);
	           ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                result[0] = rs.getString(1);
	                result[1]= rs.getString(2);
	                result[2] = rs.getString(3);
	               result[3] = rs.getString(4);
	               result[4] = rs.getString(5);
	               result[5] = rs.getString(6);
	               result[6] = rs.getString(7);
	               result[7] = rs.getString(8);
	               result[8] = rs.getString(9);//asset_status

	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
	        }
	result[9] = "Asset Creation Uncapitalized";
	result[10] = "P";
	//result[11] = timeInstance();
	        return result;

	}
public String subjectToVatBranch(String id){
	String result="";

	         String query =
	                "SELECT Subject_TO_Vat FROM AM_ASSET_UNCAPITALIZED  " +
	                "WHERE asset_id = '" + id + "' ";


	        try {
	        	Connection con = dbConnection.getConnection("legendPlus");
	        	PreparedStatement ps  = con.prepareStatement(query);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                result = rs.getString(1);
	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching Subject To Vat from Uncapitalized ->" + ex);
	        } 

	        return result;
	}
public String whTaxBranch(String id){
	String result="";
	    

	         String query =
	                "SELECT wh_tax FROM AM_ASSET_UNCAPITALIZED  " +
	                "WHERE asset_id = '" + id + "' ";


	        try {
	        	Connection con = dbConnection.getConnection("legendPlus");
	        	PreparedStatement ps = con.prepareStatement(query);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                result = rs.getString(1);
	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching WHTAX ->" + ex);
	        } 

	        return result;
	}
public String checkAccounAcqusitionSuspenseBranch2 (String category,String branch)
{
	String assetAcqusitionSuspense="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);

//	String query=" select c.iso_code, (select accronym from am_ad_ledger_type where series = substring(d.Uncapitalized_account,1,1)), "+
//	" d.branch_code, "+
//	" d.Uncapitalized_account, "+
//	" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(d.Uncapitalized_account,1,1))+ "+
//	" d.branch_code + d.Uncapitalized_account asd "+
//	" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
//	" where a.currency_id = c.currency_id "+
//	" and a.category_code = '"+category+"' "+
//	" and d.branch_code = '"+branch+"' "; 
//
////	System.out.println("query in checkAccounAcqusitionSuspenseBranch2: "+query);
	String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ACTACQSUP'");
	String query = script+" and a.category_code = ? and d.branch_code = ? ";

	try {

		Connection con = dbConnection.getConnection("legendPlus");
    	PreparedStatement ps  = con.prepareStatement(query);
    	ps.setString(1, category);
    	ps.setString(2, branch);
    	ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {
			assetAcqusitionSuspense  = rs.getString("asd");
		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}

    /**
     * @return the assetCode
     */
    public int getAssetCode() {
        return assetCode;
    }

    /**
     * @param assetCode the assetCode to set
     */
    public void setAssetCode(int assetCode) {
        this.assetCode = assetCode;
    }


    //the methods below are to set the asset code in am_asset_approval and am_asset_approval_archive

    public void setPendingTransOld(String[] a, String code,int assetCode){

        int transaction_level=0;
        
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 
 String tranLevelQuery = "select level from approval_level_setup where code = ? ";
        
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
        	PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
        	ps.setString(1, code);
             ResultSet rs = ps.executeQuery();


            while(rs.next())
            {
            transaction_level = rs.getInt(1);
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//             System.out.println(transaction_level);
//              System.out.println(code);
            }//if

//            System.out.println("a[6] in setPendingTrans: "+a[6]);

            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
            String dd = a[6].substring(0,2);
            String mm = a[6].substring(3,5);
            String yyyy = a[6].substring(6,10);
            String effDate = yyyy+"-"+mm+"-"+dd;
//            System.out.println("effDate in setPendingTrans: "+effDate);
            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
 /*           
            String pq1 = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
                    "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                    "transaction_level,asset_code) values('"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+a[5]+"',"
                    		+ "'"+effDate+"','"+a[7]+"','"+a[8]+"','"+a[9]+"','"+a[10]+"','"+timer.format(new java.util.Date())+"',"
                    				+ "'"+mtid+"','"+mtid+"','"+transaction_level+"','"+assetCode+"')";
            System.out.println("pq1 In setPendingTrans: "+pq1);
            */
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//            ps.setString(7,effDate);
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);
            ps.setInt(16,assetCode);

            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans Three 3>>>>>>" + er);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

    }//staticApprovalInfo()
    
    public void setPendingTrans(String[] a, String code, int assetCode) {
        int transactionLevel = 0;

        String insertQuery = "INSERT INTO am_asset_approval(" +
                "asset_id, user_id, super_id, amount, posting_date, description," +
                "effective_date, branchCode, asset_status, tran_type, process_status, tran_sent_time," +
                "transaction_id, batch_id, transaction_level, asset_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String tranLevelQuery = "SELECT level FROM approval_level_setup WHERE code = ?";

        
        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement psLevel = con.prepareStatement(tranLevelQuery)) {

            psLevel.setString(1, code);
            try (ResultSet rs = psLevel.executeQuery()) {
                if (rs.next()) {
                    transactionLevel = rs.getInt(1);
                }
            }
            


            try (PreparedStatement psInsert = con.prepareStatement(insertQuery)) {
                SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
                String dd = a[6].substring(0,2);
                String mm = a[6].substring(3,5);
                String yyyy = a[6].substring(6,10);
                String effDate = yyyy+"-"+mm+"-"+dd;
                String mtid = new ApplicationHelper().getGeneratedId("am_asset_approval");

                psInsert.setString(1, a[0] != null ? a[0] : "");
                psInsert.setString(2, a[1] != null ? a[1] : "");
                psInsert.setString(3, a[2] != null ? a[2] : "");
                psInsert.setDouble(4, a[3] != null ? Double.parseDouble(a[3]) : 0);
                psInsert.setTimestamp(5, dbConnection.getDateTime(new java.util.Date()));
                psInsert.setString(6, a[5] != null ? a[5] : "");
                psInsert.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
                psInsert.setString(8, a[7] != null ? a[7] : "");
                psInsert.setString(9, a[8] != null ? a[8] : "");
                psInsert.setString(10, a[9] != null ? a[9] : "");
                psInsert.setString(11, a[10] != null ? a[10] : "");
                psInsert.setString(12, timer.format(new java.util.Date()));
                psInsert.setString(13, mtid);
                psInsert.setString(14, mtid);
                psInsert.setInt(15, transactionLevel);
                psInsert.setInt(16, assetCode);

                psInsert.execute();
            }

        } catch (Exception er) {
            System.out.println(">>>AssetRecordBeans:setPendingTrans ERROR>>>>>>" + er);
            er.printStackTrace();
        }
    }

    public String setPendingTrans2(String[] a, String code,int assetCode){

    String mtid_id ="";
        int transaction_level=0;
       
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code,VATABLE_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
       
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(tranLevelQuery);
            ResultSet  rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);
            ps.setInt(16,assetCode);
            ps.setDouble(17, (a[11]==null)?0:Double.parseDouble(a[11]));
            ps.execute();

            mtid_id = mtid;
        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans2 for Three Parameter>>>>>>" + er);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";



return mtid_id;


    }//setPendingTrans2()


  public void setPendingTransArchive(String[] a, String code,long mtid, int assetCode){

        int transaction_level=0;
        
 String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
       
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
            ResultSet  rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
            String dd = a[6].substring(0,2);
            String mm = a[6].substring(3,5);
            String yyyy = a[6].substring(6,10);
            String effDate = yyyy+"-"+mm+"-"+dd;
            
            //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
//            ps.setString(7,effDate);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setLong(13,mtid);
            ps.setString(14, String.valueOf(mtid));
            ps.setInt(15, transaction_level);
            ps.setInt(16,assetCode);

            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>2 AssetRecordBeans:setPendingTransArchive()>>>>>>" + er);

        }
   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
    }//staticApprovalInfo()
  public void setPendingTransRepost(String[] a, String code,long mtid,int assetCode){

        int transaction_level=0;
        
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
       
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
             ResultSet rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setLong(13,mtid);
            ps.setString(14, String.valueOf(mtid));
            ps.setInt(15, transaction_level);
            ps.setInt(16, assetCode);
            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTransRepost()>>>>>>" + er);

        }
   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
    }//staticApprovalInfo()


  public boolean insertRaiseEntryTransactionTranId(String userId,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,long transId,int assetCode ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,trans_id,asset_code)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,userId);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);
        ps.setLong(12, transId);
        ps.setInt(13,assetCode);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction insertRaiseEntryTransactionTranId ->" );
        ex.printStackTrace();
    } 
    return done;
}


  public boolean insertRaiseEntryTransactionArchive(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String systemIp,String mac_Address,String tranId,int assetCode ) {
    boolean done=true;

	  
    String query = "INSERT INTO [am_raisentry_transaction_archive](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,mac_address,Trans_id,asset_code)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11, systemIp);
        ps.setString(12, mac_Address);
        ps.setString(13, tranId);
        ps.setInt(14,assetCode);

        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction_archive->" );
        ex.printStackTrace();
    } 
    return done;
}


   public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,String tranId, int assetCode ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,Trans_id,asset_code)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);
        ps.setString(12, tranId);
        ps.setInt(13,assetCode);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    return done;
}

 public boolean insertRaiseEntryTransactiongroup(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,int assetCode,String costdebitAcctName,String costcreditAcctName,String postedBy ) {
    boolean done=true;

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
            "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,asset_code)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,id);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setInt(11,assetCode);
        ps.setString(12, costdebitAcctName);
        ps.setString(13, costcreditAcctName);
        ps.setString(14, postedBy);
        
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
        ex.printStackTrace();
    } 
    return done;
}

 public boolean insertRaiseEntryTransactionTranId(String userId,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,String transId,int assetCode ) {
    boolean done=true;
int tranid = transId == null ? 0 : Integer.parseInt(transId);

    String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,trans_id,asset_code)" +
                   " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1,userId);
        ps.setString(2,Description);
        ps.setString(3, debitAccount);
        ps.setString(4,creditAccount);
        ps.setDouble(5,amount);
        ps.setString(6,iso);
        ps.setString(7,asset_id);
        ps.setString(8, page1);
        ps.setString(9, transactionId);
        ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(11,ip_address);
        ps.setString(12, transId);
        ps.setInt(13,assetCode);
        ps.execute();

    }
    catch (Exception ex)
    {
 	   done = false;
        System.out.println("WARNING:cannot insert am_raisentry_transaction insertRaiseEntryTransactionTranId ->" );
        ex.printStackTrace();
    } 
    return done;
}


 public boolean updateNewAssetStatuxUncapitalized(String assetId) throws Exception {

    String query = "update am_asset_Uncapitalized SET  asset_status = 'APPROVED' where asset_id ='" +assetId+"'";
     boolean done = true;

    try {

    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1, assetId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateNewAssetStatux(): WARN:Error updating asset->" + ex);
    } 
    return done;
}

     public boolean updateAssetRecordBranch() throws Exception {

        String query = "update am_asset_uncapitalized SET  " +
                       "REGISTRATION_NO=?,DESCRIPTION=?,VENDOR_AC=?," +
                       "DATE_PURCHASED=?,ASSET_MAKE=?,ASSET_MODEL=?," +
                       "ASSET_SERIAL_NO=?,ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                       "ASSET_USER=?,ASSET_MAINTENANCE=?,DEP_END_DATE=?," +

                       "RESIDUAL_VALUE=?,AUTHORIZED_BY=?,POSTING_DATE=?," +
                       "EFFECTIVE_DATE=?,PURCHASE_REASON=?,LOCATION=?," +
                       "WHO_TO_REM = ?,EMAIL1 = ?,WHO_TO_REM_2 = ?," +
                       "EMAIL2 = ?,RAISE_ENTRY = ?,SECTION=?,STATE=?,DRIVER = ?," +

                       "SPARE_1 = ?,SPARE_2 = ?,ASSET_STATUS = ?,PROVINCE=? , " +
                       "DEPT_ID = ?,req_redistribution = ?,Req_Depreciation = ?,  " +
                       "SUBJECT_TO_VAT=?,VATABLE_COST=?,VAT=? ,    " +
                       "wh_tax = ?,wh_tax_amount  = ?,BRANCH_ID  = ?,MULTIPLE = ?,COST_PRICE=?,    " +
                       "BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? ,CATEGORY_CODE=?,WAR_START_DATE=?," +
                       "WAR_MONTH=?,WAR_EXPIRY_DATE=?, BAR_CODE =?,SBU_CODE = ?, LPO = ?, supervisor=?, defer_pay=?,wht_percent=?, " +
                       "SPARE_3 = ?,SPARE_4 = ?,SPARE_5 = ?,SPARE_6 = ?, SUB_CATEGORY_CODE=?  " +
                       "WHERE	ASSET_ID = '" + asset_id + "'";

       
        boolean done = true;
        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")|| driver.equals("-1")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }


        if (bar_code == null || bar_code.equals("")) {
            bar_code = "0";
        }

        if (sbu_code == null || sbu_code.equals("")) {
            sbu_code = "0";
        }

        if (lpo == null || lpo.equals("")) {
            lpo = "0";
        }

        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        status ="APPROVED";
        try {

            double costPrice = Double.parseDouble(vat_amount) +
                               Double.parseDouble(vatable_cost);

            Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setString(1, registration_no);
            ps.setString(2, description.toUpperCase());
            ps.setString(3, vendor_account);
            ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
            ps.setInt(5, Integer.parseInt(make));
            ps.setString(6, model);
            ps.setString(7, serial_number);
            ps.setString(8, engine_number);
            ps.setString(9, supplied_by);
            ps.setString(10, user);
            ps.setInt(11, Integer.parseInt(maintained_by));
            ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(13, Double.parseDouble(residual_value));
            ps.setString(14, authorized_by);
            ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
            ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(17, reason);
            ps.setInt(18, Integer.parseInt(location));
            ps.setString(19, who_to_remind);
            ps.setString(20, email_1);
            ps.setString(21, who_to_remind_2);
            ps.setString(22, email2);
            ps.setString(23, raise_entry);
            ps.setString(24, section);
            ps.setInt(25, Integer.parseInt(state));
            ps.setInt(26, Integer.parseInt(driver));
            ps.setString(27, spare_1);
            ps.setString(28, spare_2);
            ps.setString(29, status);
            ps.setString(30, province);
            ps.setInt(31, Integer.parseInt(department_id));
            ps.setString(32, require_redistribution);
            ps.setString(33, require_depreciation);
            ps.setString(34, subject_to_vat);
            ps.setDouble(35, Double.parseDouble(vatable_cost));
            ps.setDouble(36, Double.parseDouble(vat_amount));
            ps.setString(37, wh_tax_cb);
            ps.setDouble(38, Double.parseDouble(wh_tax_amount));
            ps.setInt(39, Integer.parseInt(branch_id));
            ps.setString(40, multiple);
            ps.setDouble(41, costPrice);
            ps.setString(42, code.getBranchCode(branch_id));
            ps.setString(43, code.getDeptCode(department_id));
            ps.setString(44, code.getSectionCode(section_id));
            ps.setString(45, code.getCategoryCode(category_id));
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49, bar_code);
            ps.setString(50,sbu_code);
            ps.setString(51, lpo);
            ps.setString(52,getSupervisor());
            ps.setString(53,deferPay);
            ps.setDouble(54, Double.parseDouble(selectTax));
            ps.setString(55, spare_3);
            ps.setString(56, spare_4);
            ps.setString(57, spare_5);
            ps.setString(58, spare_6);
            ps.setString(59, code.getSubCategoryCode(sub_category_id));
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error updating asset->" + ex);
        } 
        return done;

    }
public void updateAssetStatuxBranch(String assetId){
String query_r ="update am_asset_uncapitalized set asset_status=?,raise_entry=? where asset_id = ? ";


try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps  = con.prepareStatement(query_r);



            ps.setString(1,"APPROVED");
            ps.setString(2,"Y");
            ps.setString(3,assetId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatux()>>>>>" + ex);
        } 


}//updateAssetStatus()

//lanre update asset with asset_id and user_id
public boolean updateAssetBranch(String assetId,String userId) throws Exception {

    String query = "update am_asset_uncapitalized set " +
                   "REGISTRATION_NO=?,DESCRIPTION=?,VENDOR_AC=?," +
                   "DATE_PURCHASED=?,ASSET_MAKE=?,ASSET_MODEL=?," +
                   "ASSET_SERIAL_NO=?,ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                   "ASSET_USER=?,ASSET_MAINTENANCE=?,DEP_END_DATE=?," +
                   "RESIDUAL_VALUE=?,AUTHORIZED_BY=?,POSTING_DATE=?," +
                   "EFFECTIVE_DATE=?,PURCHASE_REASON=?,LOCATION=?," +
                   "WHO_TO_REM=?,EMAIL1=?,WHO_TO_REM_2=?," +
                   "EMAIL2=?,RAISE_ENTRY=?,SECTION=?,STATE=?,DRIVER=?," +
                   "SPARE_1=? ,SPARE_2=? ,ASSET_STATUS=? ,PROVINCE=? ," +
                   "DEPT_ID=? ,req_redistribution=? ,Req_Depreciation=? , " +
                   "SUBJECT_TO_VAT=?,VATABLE_COST=?,VAT=? ," +
                   "wh_tax=? ,wh_tax_amount=? ,BRANCH_ID=? ,MULTIPLE=? ,COST_PRICE=?," +
                   "BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? ,CATEGORY_CODE=?,WAR_START_DATE=?," +
                   "WAR_MONTH=?,WAR_EXPIRY_DATE=?, BAR_CODE=? ,SBU_CODE=? , LPO=? , supervisor=?, defer_pay=? ,category_id=?,dep_rate=?,"+
                   "wht_percent=?,SPARE_3=? ,SPARE_4=? ,SPARE_5=? ,SPARE_6=?, sub_category_id=?,SUB_CATEGORY_CODE=?  where asset_id=? and user_id=? " ;

    boolean done = true;
    /*if (require_redistribution.equalsIgnoreCase("Y")) {
        status = "Z";
             }*/
    if (make == null || make.equals("")) {
        make = "0";
    }
    if (maintained_by == null || maintained_by.equals("")) {
        maintained_by = "0";
    }
    if (location == null || location.equals("")) {
        location = "0";
    }
    if (driver == null || driver.equals("")|| driver.equals("-1")) {
        driver = "0";
    }
    if (state == null || state.equals("")) {
        state = "0";
    }
    if (department_id == null || department_id.equals("")) {
        department_id = "0";
    }
    if (vat_amount == null || vat_amount.equals("")) {
        vat_amount = "0.0";
    }
    if (vatable_cost == null || vatable_cost.equals("")) {
        vatable_cost = "0.0";
    }
    if (transport_cost == null || transport_cost.equals("")) {
    	transport_cost = "0.0";
    }
    if (other_cost == null || other_cost.equals("")) {
    	other_cost = "0.0";
    }
    if (wh_tax_amount == null || wh_tax_amount.equals("")) {
        wh_tax_amount = "0";
    }
    if (branch_id == null || branch_id.equals("")) {
        branch_id = "0";
    }
    if (residual_value == null || residual_value.equals("")) {
        residual_value = "0";
    }
    if (section_id == null || section_id.equals("")) {
        section_id = "0";
    }


    if (bar_code == null || bar_code.equals("")) {
        bar_code = "0";
    }

    if (sbu_code == null || sbu_code.equals("")) {
        sbu_code = "0";
    }

    if (lpo == null || lpo.equals("")) {
        lpo = "0";
    }

     if (depreciation_rate == null || depreciation_rate.equals("")) {
        depreciation_rate = "0";
    }

    vat_amount = vat_amount.replaceAll(",", "");
    vatable_cost = vatable_cost.replaceAll(",", "");
    wh_tax_amount = wh_tax_amount.replaceAll(",", "");
    residual_value = residual_value.replaceAll(",", "");

    try {

        double costPrice = Double.parseDouble(vat_amount) +
                           Double.parseDouble(vatable_cost);
//System.out.println("vatable_cost "+vatable_cost);
        Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setString(1, registration_no);
        ps.setString(2, description.toUpperCase());
        ps.setString(3, vendor_account);
        ps.setDate(4, dbConnection.dateConvert(date_of_purchase));
        ps.setInt(5, Integer.parseInt(make));
        ps.setString(6, model);
        ps.setString(7, serial_number);
        ps.setString(8, engine_number);
        ps.setString(9, supplied_by);
        ps.setString(10, user);
        ps.setInt(11, Integer.parseInt(maintained_by));
        ps.setDate(12, dbConnection.dateConvert(depreciation_end_date));
        ps.setDouble(13, Double.parseDouble(residual_value));
        ps.setString(14, authorized_by);
        ps.setDate(15, dbConnection.dateConvert(date_of_purchase));
        ps.setDate(16, dbConnection.dateConvert(depreciation_start_date));
        ps.setString(17, reason);
        ps.setInt(18, Integer.parseInt(location));
        ps.setString(19, who_to_remind);
        ps.setString(20, email_1);
        ps.setString(21, who_to_remind_2);
        ps.setString(22, email2);
        ps.setString(23, raise_entry);
        ps.setString(24, section);
        ps.setInt(25, Integer.parseInt(state));
        ps.setInt(26, Integer.parseInt(driver));
        ps.setString(27, spare_1);
        ps.setString(28, spare_2);
        ps.setString(29, status);
        ps.setString(30, province);
        ps.setInt(31, Integer.parseInt(department_id));
        ps.setString(32, require_redistribution);
        ps.setString(33, require_depreciation);
        ps.setString(34, subject_to_vat);
        ps.setDouble(35, Double.parseDouble(vatable_cost));
        ps.setDouble(36, Double.parseDouble(vat_amount));
        ps.setString(37, wh_tax_cb);
        ps.setDouble(38, Double.parseDouble(wh_tax_amount));
        ps.setInt(39, Integer.parseInt(branch_id));
        ps.setString(40, multiple);
        ps.setDouble(41, costPrice);
        ps.setString(42, code.getBranchCode(branch_id));
        ps.setString(43, code.getDeptCode(department_id));
        ps.setString(44, code.getSectionCode(section_id));
        ps.setString(45, code.getCategoryCode(category_id));
        ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
        ps.setInt(47, Integer.parseInt(noOfMonths));
        ps.setDate(48, dbConnection.dateConvert(expiryDate));
        ps.setString(49, bar_code);
        ps.setString(50,sbu_code);
        ps.setString(51, lpo);
        ps.setString(52,getSupervisor());
        ps.setString(53,deferPay);

        ps.setString(54, category_id);
        ps.setString(55, depreciation_rate);
        ps.setDouble(56, Double.parseDouble(selectTax));
        ps.setString(57, spare_3);
        ps.setString(58, spare_4);
        ps.setString(59, spare_5);
        ps.setString(60, spare_6);
        ps.setString(61, category_id);
        ps.setString(62, code.getSubCategoryCode(sub_category_id));
        
        ps.setString(63, assetId);
        ps.setString(64,userId);
        ps.execute();

    } catch (Exception ex) {
        done = false;
        System.out.println("AssetRecordsBean: updateasset():WARN:Error updating to am_asset table->" + ex);
    } 
    return done;

}
public void updateAssetStatusBranch(String assetId){
String query_r ="update am_asset_uncapitalized set asset_status=?,raise_entry=? where asset_id = ? ";


try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query_r);

            ps.setString(1,"ACTIVE");
            ps.setString(2,"Y");
            ps.setString(3,assetId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatus()>>>>>" + ex);
        } 


}//updateAssetStatus()

public boolean updateNewAssetStatusBranch(String assetId) throws Exception {

        String query = "update am_asset_uncapitalized SET  asset_status = 'ACTIVE' ,Finacle_Posted_Date= ? where asset_id = ? ";
         boolean done = true;

        try {



        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setTimestamp(1, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(2, assetId);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: updateNewAssetStatus: WARN:Error updating asset->" + ex);
        } 
        return done;

    }


public String checkCategoryCodeByAssetCode(int assetCode)
	{
		String category="";
		String query="SELECT CATEGORY_CODE FROM AM_ASSET  where asset_Code= ? ";

		try {

			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setInt(1, assetCode);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			 {

				category  = rs.getString("CATEGORY_CODE");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	category;
	}
public String checkCategoryCodeByAssetCodeTransfer(int assetCode)
{
	String category="";
	String query="SELECT CATEGORY_CODE FROM AM_ASSET_UNCAPITALIZED  where asset_Code = ? ";

	

	try {

		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);
        ps.setInt(1, assetCode);
		ResultSet rs = ps.executeQuery();

		while (rs.next())
		 {

			category  = rs.getString("CATEGORY_CODE");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	category;
}

public void getAssetRecords3() throws Exception {

    if (asset_id != "") {
    	String asset_code = asset_id;
        String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE,TRANPORT_COST,OTHER_COST, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code " +
                       "FROM AM_ASSET A " +
                       "WHERE A.asset_code = '" + asset_code + "'";

       
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setString(1, asset_code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID");
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                transport_cost = rs.getString("TRANPORT_COST");
                other_cost = rs.getString("OTHER_COST");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getInt("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("asset_code");
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } 
    }
}

public String getDisposalSupervisor3(String asset_code, String trans_type){
	  String query = "select super_id from am_asset_approval where asset_code = ? and tran_type = ? ";
	   String result="";
	  
	        try {
	        	Connection con = dbConnection.getConnection("legendPlus");
	            PreparedStatement ps  = con.prepareStatement(query);
	            ps.setString(1, asset_code);
	            ps.setString(2, trans_type);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                result = rs.getString(1);

	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
	        } 

	        return result;





	}
public String[] getAssetReclassification(String assetCode){

	//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
	 //String currentDate  = reArrangeDate(getCurrentDate1());
	   // System.out.println("the $$$$$$$$$$$ "+currentDate);
	    String[] result= new String[8];
	   

	         String query = " select new_asset_id,new_category_id,new_depr_rate,new_accum_dep,new_total_life, "+
	         				" new_remaining_life,new_dep_end_date,new_monthly_dep from  dbo.am_assetReclassification "+
                            " where asset_code = ? ";



	        try {
	        	Connection con = dbConnection.getConnection("legendPlus");
	            PreparedStatement ps  = con.prepareStatement(query);
	            ps.setString(1, assetCode);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {

	                result[0] = rs.getString(1);
	                result[1]= rs.getString(2);
	                result[2] = rs.getString(3);
	               result[3] = rs.getString(4);
	               result[4] = rs.getString(5);
	               result[5] = rs.getString(6);
	               result[6] = rs.getString(7);
	               result[7] = rs.getString(8);

	            }

	        } catch (Exception ex) {
	            System.out.println("WARN: Error fetching CategoryCode ->" + ex);
	        } 
	        
	        return result;

	}
 private String oldId="";

    public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
public boolean rinsertAssetMemoRecord() throws Exception, Throwable {
       
        boolean done = true;

        /*if (require_redistribution.equalsIgnoreCase("Y")) {
            status = "Z";
                 }*/
        if (make == null || make.equals("")) {
            make = "0";
        }
        if (maintained_by == null || maintained_by.equals("")) {
            maintained_by = "0";
        }
        if (supplied_by == null || supplied_by.equals("")) {
            supplied_by = "0";
        }
        if (user == null || user.equals("")) {
            user = "";
        }
        if (location == null || location.equals("")) {
            location = "0";
        }
        if (driver == null || driver.equals("")) {
            driver = "0";
        }
        if (state == null || state.equals("")) {
            state = "0";
        }
        if (department_id == null || department_id.equals("")) {
            department_id = "0";
        }
        if (vat_amount == null || vat_amount.equals("")) {
            vat_amount = "0.0";
        }
        if (vatable_cost == null || vatable_cost.equals("")) {
            vatable_cost = "0.0";
        }
        if (transport_cost == null || transport_cost.equals("")) {
        	transport_cost = "0.0";
        }
        if (other_cost == null || other_cost.equals("")) {
        	other_cost = "0.0";
        }
        if (wh_tax_amount == null || wh_tax_amount.equals("")) {
            wh_tax_amount = "0";
        }
        if (branch_id == null || branch_id.equals("")) {
            branch_id = "0";
        }
        if (province == null || province.equals("")) {
            province = "0";
        }
        if (category_id == null || category_id.equals("")) {
            category_id = "0";
        }
        if (sub_category_id == null || sub_category_id.equals("")) {
            sub_category_id = "0";
        }        

        if (residual_value == null || residual_value.equals("")) {
            residual_value = "0";
        }
        if (section_id == null || section_id.equals("")) {
            section_id = "0";
        }

        if (noOfMonths == null || noOfMonths.equals("")) {
            noOfMonths = "0";
        }
        if (warrantyStartDate == null || warrantyStartDate.equals("")) {
            warrantyStartDate = null;
        }
        if (expiryDate == null || expiryDate.equals("")) {
            expiryDate = null;
        }


        if (memo == null || memo.equals("")) {
        	memo = "N";
		}

        if (memoValue == null || memoValue.equals("")) {
        	memoValue = "0";
		}
    	int memoValueS = Integer.parseInt(memoValue);




        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");


        try 
		{
			String memo_asset_id = new ApplicationHelper().getGeneratedId("am_memo_asset");
			String createQueryMemo = "INSERT INTO AM_ASSET_MEMO         " +
            "(" +
            "memo_asset_id, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
            "SECTION_ID, CATEGORY_ID, DESCRIPTION, VENDOR_AC," +
            "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
            "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

            "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
            "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
            "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
            "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

            "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
            "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
            "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
            "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
            "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
            "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
            "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor," +
            "defer_pay,wht_percent," +
            "system_ip,mac_address,asset_code,memo,oldId,SPARE_3, SPARE_4,SPARE_5, SPARE_6,"+
            "SUB_CATEGORY_ID,SUB_CATEGORY_CODE) " +

            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+
             "?,?,?,?,?,?,?,?,?,?,?,?,?)";





double costPriceMemo = Double.parseDouble(vat_amount) +  Double.parseDouble(vatable_cost);

Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps  = con.prepareStatement(createQueryMemo);
ps.setString(1, memo_asset_id);
ps.setString(2, registration_no);
ps.setInt(3, Integer.parseInt(branch_id));
ps.setInt(4, Integer.parseInt(department_id));
ps.setInt(5, Integer.parseInt(section_id));
ps.setInt(6, Integer.parseInt(category_id));
ps.setString(7, description.toUpperCase());
ps.setString(8, vendor_account);
ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
ps.setString(10, getDepreciationRate(category_id));
ps.setString(11, make);
ps.setString(12, model);
ps.setString(13, serial_number);
ps.setString(14, engine_number);
ps.setInt(15, Integer.parseInt(supplied_by));
ps.setString(16, user);
ps.setInt(17, Integer.parseInt(maintained_by));
ps.setInt(18, 0);
ps.setInt(19, 0);
 ps.setDouble(20, costPriceMemo);
ps.setDouble(21, costPriceMemo);
ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));

ps.setDouble(23, Double.parseDouble(residual_value));
ps.setString(24, authorized_by);
ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
ps.setString(27, reason);
ps.setString(28, "0");
ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
ps.setInt(30, Integer.parseInt(location));
ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
ps.setDouble(32, Double.parseDouble(vatable_cost));
ps.setDouble(33, Double.parseDouble(vat_amount));
ps.setString(34, wh_tax_cb);
ps.setDouble(35, Double.parseDouble(wh_tax_amount));
ps.setString(36, require_depreciation);
ps.setString(37, require_redistribution);
ps.setString(38, subject_to_vat);
ps.setString(39, who_to_remind);
ps.setString(40, email_1);
ps.setString(41, who_to_remind_2);
ps.setString(42, email2);
ps.setString(43, raise_entry);
ps.setString(44, "0");
ps.setString(45, section);
ps.setInt(46, Integer.parseInt(state));
ps.setInt(47, Integer.parseInt(driver));
ps.setString(48, spare_1);
ps.setString(49, spare_2);
ps.setString(50, status);
ps.setString(51, user_id);
ps.setString(52, multiple);
ps.setString(53, province);
ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
ps.setInt(55, new Integer(noOfMonths).intValue());
ps.setDate(56, dbConnection.dateConvert(expiryDate));
ps.setString(57, code.getBranchCode(branch_id));
ps.setString(58, code.getDeptCode(department_id));
ps.setString(59, code.getSectionCode(section_id));
ps.setString(60, code.getCategoryCode(category_id));
ps.setDouble(61, Double.parseDouble(amountPTD));
ps.setDouble(62, (Double.parseDouble(vatable_cost)));
ps.setString(63, partPAY);
ps.setString(64, fullyPAID);
ps.setString(65, bar_code);
ps.setString(66,sbu_code);
ps.setString(67, lpo);
ps.setString(68,getSupervisor());
ps.setString(69, deferPay);
ps.setDouble(70, Double.parseDouble(selectTax));
ps.setString(71, getSystemIp());
ps.setString(72,getMacAddress());
ps.setInt(73,assetCode);
ps.setString(74, memo);
ps.setString(75, oldId);
ps.setString(76, spare_1);
ps.setString(77, spare_2);
ps.setString(78, spare_1);
ps.setString(79, spare_2);
ps.setInt(80, Integer.parseInt(sub_category_id));
ps.setString(81, code.getSubCategoryCode(sub_category_id));

done= ps.execute();

		}

                    catch (Exception r)
                    {
                    //	System.out.println("-------------------------------");
                    	r.printStackTrace();
//                    	System.out.println("-------------------------------");
		}
                    finally
                    {
			freeResource();
		   }




        return done;
    }
public void getMemoAssetRecords() throws Exception {

    if (asset_id != "") {
        String query = "SELECT A.MEMO_ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code " +
                       "FROM AM_ASSET_MEMO A " +
                       "WHERE A.MEMO_ASSET_ID = ? ";

       

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setString(1, asset_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID");
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("MEMO_ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getInt("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("asset_code");
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } 
    }
}  

//new------------------
public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,String tranId, int assetCode,String Finacle_trans_id,String debitAccountName, String creditAccountName,String postedBy,String integrifyId ) {
  boolean done=true;
/*  String integrifyId = "";
  if(recType.equalsIgnoreCase("Uncapitalized")){
   integrifyId = approvalRec.getCodeName("select INTEGRIFY from AM_ASSET_UNCAPITALIZED where ASSET_ID = '"+asset_id+"'");
  }else{
	  integrifyId = approvalRec.getCodeName("select INTEGRIFY from AM_ASSET where ASSET_ID = '"+asset_id+"'"); 
  }*/
	  
  String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,Trans_id,asset_code,Finacle_trans_id,debitAccountName,creditAccountName,INTEGRIFY_ID,POSTED_BY)" +
                 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,id);
      System.out.println("========Description: "+Description);
      ps.setString(2,Description);
      ps.setString(3, debitAccount);
      ps.setString(4,creditAccount);
      ps.setDouble(5,amount);
      ps.setString(6,iso);
      System.out.println("========asset_id: "+asset_id);
      ps.setString(7,asset_id);
      ps.setString(8, page1);
      ps.setString(9, transactionId);
      ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
      ps.setString(11,ip_address);
      ps.setString(12, tranId);
      ps.setInt(13,assetCode);
      ps.setString(14, Finacle_trans_id);
      ps.setString(15, debitAccountName);
      ps.setString(16, creditAccountName);
      ps.setString(17, integrifyId);
      ps.setString(18, postedBy);
      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  
  return done;
}

public boolean updateRaiseEntryTransaction( String asset_id,String page1,String transactionId ,String iso,String Finacle_trans_id,String spare,String spare2) {
  boolean done=true;

	
  String query = "update am_raisentry_transaction set iso=?,transaction_date=?,Finacle_trans_id=? where transactionId=?    and ASSET_ID=? and page1=? " ;

  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,iso);
      ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
      ps.setString(3,Finacle_trans_id);
      ps.setString(4,transactionId);

      ps.setString(5,asset_id);
      ps.setString(6, page1);


      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot update am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  return done;
}

public boolean updateRaiseEntryTransaction( String asset_id,String page1,String transactionId ,String iso,String ip_address,String tranId,String Finacle_trans_id,String spare,String spare2) {
  boolean done=true;

  String query = "update am_raisentry_transaction set iso=?,transaction_date=?,system_ip=?,Finacle_trans_id=? where transactionId=?    and ASSET_ID=? and page1=? and Trans_id=?" ;

  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,iso);
      ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
      ps.setString(3, ip_address);
      ps.setString(4,Finacle_trans_id);
      ps.setString(5,transactionId);
      ps.setString(6,asset_id);
      ps.setString(7, page1);
      ps.setString(8, tranId);


      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot update am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  
  return done;
}

public boolean insertRaiseEntryTransaction(String id,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,int assetCode,String Finacle_trans_id, String costdebitAcctName,String costcreditAcctName,String postedBy) {
  boolean done=true;

	  
  String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount," +
          "creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,asset_code,Finacle_trans_id,debitAccountName,creditAccountName,POSTED_BY)" +
                 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,id);
      ps.setString(2,Description);
      ps.setString(3, debitAccount);
      ps.setString(4,creditAccount);
      ps.setDouble(5,amount);
      ps.setString(6,iso);
      ps.setString(7,asset_id);
      ps.setString(8, page1);
      ps.setString(9, transactionId);
      ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
      ps.setInt(11,assetCode);
      ps.setString(12, Finacle_trans_id);
      ps.setString(13, costdebitAcctName);
      ps.setString(14, costcreditAcctName);
      ps.setString(15, postedBy);
      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot insert am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  return done;
}
public boolean updateRaiseEntryTransaction2( String asset_id,String page1,String transactionId ,String iso,String tranId,String Finacle_trans_id) {
  boolean done=true;

	 
  String query = "update am_raisentry_transaction set iso=?,transaction_date=?,Finacle_trans_id=? where transactionId=?    and ASSET_ID=? and page1=?   and Trans_id = ? " ;

  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,iso);
      ps.setTimestamp(2, dbConnection.getDateTime(new java.util.Date()));
      ps.setString(3,Finacle_trans_id);
      ps.setString(4,transactionId);

      ps.setString(5,asset_id);
      ps.setString(6, page1);
      ps.setString(7,tranId);

      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot update am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  return done;
}

public boolean insertRaiseEntryTransactionTranId(String userId,String Description,String debitAccount,String creditAccount,double amount,String iso,String asset_id,String page1,String transactionId,String ip_address,String transId,int assetCode,String Finacle_trans_id ) {
  boolean done=true;
//int tranid = transId == null ? 0 : Integer.parseInt(transId);

	
  String query = "INSERT INTO [am_raisentry_transaction](User_id,Description,debitAccount,creditAccount,amount,iso,ASSET_ID,page1,transactionId,transaction_date,system_ip,trans_id,asset_code,Finacle_trans_id)" +
                 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ps.setString(1,userId);
      ps.setString(2,Description);
      ps.setString(3, debitAccount);
      ps.setString(4,creditAccount);
      ps.setDouble(5,amount);
      ps.setString(6,iso);
      ps.setString(7,asset_id);
      ps.setString(8, page1);
      ps.setString(9, transactionId);
      ps.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
      ps.setString(11,ip_address);
      ps.setString(12, transId);
      ps.setInt(13,assetCode);
      ps.setString(14, Finacle_trans_id);

      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot insert am_raisentry_transaction insertRaiseEntryTransactionTranId ->" );
      ex.printStackTrace();
  } 
  return done;
}
public boolean updateUncapitalizedNewAssetStatus(String assetId) throws Exception {

        String query = "update am_asset_uncapitalized SET  asset_status = 'ACTIVE' ,Finacle_Posted_Date= ? where asset_id = ? ";
         boolean done = true;

        try {



        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps  = con.prepareStatement(query);
            ps.setTimestamp(1, dbConnection.getDateTime(new java.util.Date()));
            ps.setString(2, assetId);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("AssetRecordsBean: updateNewAssetStatus: WARN:Error updating asset->" + ex);
        } 
        return done;

    }



public String nbv(String id){
String result="";
    

         String query =
                "SELECT nbv FROM am_asset  " +
                "WHERE asset_id = ? ";


        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching NBV ->" + ex);
        } 

        return result;
}

public void setUploadPendingTrans(int supervisor, String code,String AssetId,int AssetCode,int userid,double amount,String description,String branchcode,String Trantype){
//	System.out.println("setUploadPendingTrans supervisor===> "+supervisor);
//	System.out.println("setUploadPendingTrans AssetId===> "+AssetId);	
    int transaction_level=0;
  
	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
//  	  System.out.println("setUploadPendingTrans transaction_level===> "+transaction_level);
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"+
"effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
"transaction_level,Asset_code) values('"+AssetId+"',"+userid+",'"+supervisor+"','"+amount+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+description+"','"+dbConnection.getDateTime(new java.util.Date())+"',"+
"'"+branchcode+"','PENDING','"+Trantype+"','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"','"+AssetId+"','"+transaction_level+"','"+AssetCode+"')";
/*
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"+
        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
        "transaction_level,Asset_code) select asset_id,user_ID,'"+supervisor+"',Cost_Price,'"+dbConnection.getDateTime(new java.util.Date())+"',description,effective_date,"+
        "BRANCH_CODE,'PENDING','Asset Upload','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"',Group_id,'"+transaction_level+"','"+AssetCode+"' from am_asset where Asset_Id = '"+AssetId+"'";
*/        
//System.out.println("setUploadPendingTrans Query pq===> "+pq);
    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(pq);

        ps.execute();
        //dbConnection.closeConnection(con, ps);
    }
    
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setUploadPendingTrans(>>>>>>" + er);

    }
}
public void setUploadPendingTransArchive(String supervisor, String code,String AssetId, int AssetCode){
    int transaction_level=0;

	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
  	   	
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description,"+
        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
        "transaction_level,Asset_code) select asset_id,user_ID,'"+supervisor+"',Cost_Price,'"+dbConnection.getDateTime(new java.util.Date())+"',description,effective_date,"+
        "BRANCH_CODE,Asset_Status,'Asset Upload','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"',Group_id,'"+transaction_level+"','"+AssetCode+"' from am_asset Asset_Id = '"+AssetId+"'";
  
//System.out.println("setUploadPendingTransArchive Query pq ===== "+pq);

    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(pq);

        ps.execute();
        //dbConnection.closeConnection(con, ps);
    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setUploadPendingTransArchive(>>>>>>" + er);

    }
}


public boolean setUploadCompleteTrans(String AssetId){
    //	   System.out.println("Before Update Asset Id ====  "+AssetId);
    //	   System.out.println("Before Update Asset Code ====  "+AssetCode);
    	   
    	    boolean done;
    	    done = false;
    	    String UpadteQuerry = "UPDATE am_asset set Asset_Status = 'ACTIVE' where ASSET_ID = ? ";
    	    try {
    	    	Connection con = dbConnection.getConnection("legendPlus");
    	        PreparedStatement ps = con.prepareStatement(UpadteQuerry);
    	        ps.setString(1, AssetId);
    	        done = (ps.executeUpdate() != -1);

    	    } catch (Exception ex) {
    	        System.out.println("WARN: Error fetching all asset ->" + ex);
    	    }
    	    return done;
    	}

public boolean setUncapUploadCompleteTrans(String AssetId){
    //	   System.out.println("Before Update Asset Id ====  "+AssetId);
    //	   System.out.println("Before Update Asset Code ====  "+AssetCode);
    	   
    	    boolean done;
    	    done = false;
    	    String UpadteQuerry = "UPDATE AM_ASSET_UNCAPITALIZED set Asset_Status = 'ACTIVE' where ASSET_ID = '"+AssetId+"'";
    	    try {
    	    	Connection con = dbConnection.getConnection("legendPlus");
    	        PreparedStatement ps = con.prepareStatement(UpadteQuerry);
    	        ps.setString(1, AssetId);
    	        done = (ps.executeUpdate() != -1);

    	    } catch (Exception ex) {
    	        System.out.println("WARN: Error fetching all asset ->" + ex);
    	    } 
    	    return done;
    	}
public boolean setUpdateUploadTrans(String groupid,String userid){
    	    
    	    boolean done;
    	    done = false;    	    
    	    String UpadteQuerry = "update am_group_asset set process_flag = ? where  GROUP_ID=? AND USER_ID=? ";
    	    try {
    	    	Connection con = dbConnection.getConnection("legendPlus");
    	        PreparedStatement ps = con.prepareStatement(UpadteQuerry);
    	        ps.setString(1, "Y");
    	        ps.setString(2, groupid);
    	        ps.setString(3, userid);
    	        done = (ps.executeUpdate() != -1);

    	    } catch (Exception ex) {
    	        System.out.println("WARN: Error Updateing all Am asset Group ->" + ex);
    	    } 
    	    return done;
    	}      
public boolean setUpdateUnccapUploadTrans(String groupid,String userid){
    
    boolean done;
    done = false;    	    
    String UpadteQuerry = "update AM_GROUP_ASSET_UNCAPITALIZED set process_flag = ? where  GROUP_ID=? AND USER_ID=? ";
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(UpadteQuerry);
        ps.setString(1, "Y");
        ps.setString(2, groupid);
        ps.setString(3, userid);
        done = (ps.executeUpdate() != -1);

    } catch (Exception ex) {
        System.out.println("WARN: Error Updateing all Am asset Group Uncapitalized ->" + ex);
    } 
    return done;
}      


public void setInsertGroupMainTrans(int quantity){
//	System.out.println("setUploadPendingTrans quantity===> "+quantity);
//	System.out.println("setUploadPendingTrans AssetId===> "+AssetId);	
    int transaction_level=0;
 
    boolean done = false;
//	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
//  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
  	   	
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String insertquery = "insert into AM_GROUP_ASSET_MAIN(QUANTITY,BRANCH_ID,DEPT_ID,"+ 
		"CATEGORY_ID,SECTION_ID,DESCRIPTION,DATE_PURCHASED,DEP_RATE,"+
		"POSTING_DATE,EFFECTIVE_DATE,raise_entry,process_flag,asset_make,cost_price," +
		"residual_value,invoice_no,LPO,vatable_cost,supervisor,Fully_paid,part_pay,state," +
		"driver,amount_ptd,user_id,amount_rem,section_code,vat,dept_code,branch_code," +
		"Asset_Status,Req_depreciation,wh_tax_amount,wh_tax"+
		")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("setUploadPendingTrans Query pq===> "+pq);
    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(insertquery);
    	ps.setInt(1, quantity);    
    	ps.setInt(2, '1');
    	ps.setInt(3, '1');
    	ps.setInt(4, '1');
    	ps.setInt(5, '1');
    	ps.setString(6, "Asset Upload"); 
    	ps.setDate(7, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDouble(8, 0.00);
    	ps.setDate(9, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDate(10, dbConnection.dateConvert(depreciation_start_date));
    	ps.setString(11, "N");
    	ps.setString(12, "Y");
    	ps.setInt(13, '0');
    	ps.setDouble(14, 0.00);
    	ps.setDouble(15, 10.00);
    	ps.setString(16, "1");
    	ps.setString(17, "1");
    	ps.setDouble(18, 0.00);
    	ps.setInt(19, '2');
    	ps.setString(20, "Y");
    	ps.setString(21, "N");
    	ps.setInt(22, '1');
    	ps.setInt(23, '0');
    	ps.setDouble(24, 0.00);
    	ps.setInt(25, '2');
    	ps.setDouble(26, 0.00);
    	ps.setString(27, "001");
    	ps.setDouble(28, 0.00);
    	ps.setString(29, "011");
    	ps.setString(30, "014");
    	ps.setString(31, "PENDING");
    	ps.setString(32, "N");
    	ps.setDouble(33, 0.00);
    	ps.setString(34, "S");
//        con = dbConnection.getConnection("legendPlus");

   //     ps = con.prepareStatement(pq);

   //     ps.execute();
    	done = (ps.executeUpdate() != -1);
    }
    
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setInsertGroupMainTrans(>>>>>>" + er);
    } 
    
}

public boolean revaluationsumation( String asset_id,int assetCode,double amount) {
  boolean done=true;

	   
  String query = "update am_raisentry_transaction set Revalue_Cost = Revalue_Cost + ? where ASSET_ID=? " ;

  try {
	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps = con.prepareStatement(query);
      ps.setDouble(1, amount);
      ps.setString(2,asset_id);
      ps.execute();

  }
  catch (Exception ex)
  {
	   done = false;
      System.out.println("WARNING:cannot update am_raisentry_transaction->" );
      ex.printStackTrace();
  } 
  return done;
}
public void setInsertGroupUncapitalizedMainTrans(int quantity){
//	System.out.println("setUploadPendingTrans supervisor===> "+supervisor);
//	System.out.println("setUploadPendingTrans AssetId===> "+AssetId);	
    int transaction_level=0;
    
    boolean done = false;
//	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
//  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
  	   	
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String insertquery = "insert into AM_GROUP_ASSET_MAIN(QUANTITY,BRANCH_ID,DEPT_ID,"+ 
		"CATEGORY_ID,SECTION_ID,DESCRIPTION,DATE_PURCHASED,DEP_RATE,"+
		"POSTING_DATE,EFFECTIVE_DATE,raise_entry,process_flag,asset_make,cost_price," +
		"residual_value,invoice_no,LPO,vatable_cost,supervisor,Fully_paid,part_pay,state," +
		"driver,amount_ptd,user_id,amount_rem,section_code,vat,dept_code,branch_code," +
		"Asset_Status,Req_depreciation,wh_tax_amount,wh_tax"+
		")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("setInsertGroupUncapitalizedMainTrans Query pq===> "+insertquery);
    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(insertquery);
    	ps.setInt(1, quantity);    
    	ps.setInt(2, '1');
    	ps.setInt(3, '1');
    	ps.setInt(4, '1');
    	ps.setInt(5, '1');
    	ps.setString(6, "Uncapitalized Asset Upload"); 
    	ps.setDate(7, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDouble(8, 0.00);
    	ps.setDate(9, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDate(10, dbConnection.dateConvert(depreciation_start_date));
    	ps.setString(11, "N");
    	ps.setString(12, "Y");
    	ps.setInt(13, '0');
    	ps.setDouble(14, 0.00);
    	ps.setDouble(15, 10.00);
    	ps.setString(16, "1");
    	ps.setString(17, "1");
    	ps.setDouble(18, 0.00);
    	ps.setInt(19, '2');
    	ps.setString(20, "Y");
    	ps.setString(21, "N");
    	ps.setInt(22, '1');
    	ps.setInt(23, '0');
    	ps.setDouble(24, 0.00);
    	ps.setInt(25, '2');
    	ps.setDouble(26, 0.00);
    	ps.setString(27, "001");
    	ps.setDouble(28, 0.00);
    	ps.setString(29, "011");
    	ps.setString(30, "014");
    	ps.setString(31, "PENDING");
    	ps.setString(32, "N");
    	ps.setDouble(33, 0.00);
    	ps.setString(34, "S");
//        con = dbConnection.getConnection("legendPlus");

   //     ps = con.prepareStatement(pq);

   //     ps.execute();
    	done = (ps.executeUpdate() != -1);
    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setInsertGroupUncapitalizedMainTrans(>>>>>>" + er);

    }
}

public void setUploadUncapitalPendingTrans(int supervisor, String code,String AssetId,int AssetCode,int userid,double amount,String description,String branchcode,String TranType){
//	System.out.println("setUploadPendingTrans supervisor===> "+supervisor);
//	System.out.println("setUploadPendingTrans AssetId===> "+AssetId);	
    int transaction_level=0;
    
	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
//  	  System.out.println("setUploadPendingTrans transaction_level===> "+transaction_level);
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"+
        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
        "transaction_level,Asset_code) values('"+AssetId+"',"+userid+",'"+supervisor+"','"+amount+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+description+"','"+dbConnection.getDateTime(new java.util.Date())+"',"+
        "'"+branchcode+"','PENDING','"+TranType+"','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"','"+AssetId+"','"+transaction_level+"','"+AssetCode+"')";
//from AM_ASSET_UNCAPITALIZED where Asset_Id = '"+AssetId+"'
//System.out.println("setUploadPendingTrans Query pq===> "+pq);
    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(pq);

        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setUploadPendingTrans(>>>>>>" + er);

    } 
}
public void getUncapitalizedAssetRecords() throws Exception {

    if (asset_id != "") {
        String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID, A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code,A.memo,A.memovalue " +
                       "FROM AM_ASSET_UNCAPITALIZED A " +
                       "WHERE A.ASSET_ID = '" + asset_id + "'";
 //       System.out.println("Query getUncapitalizedAssetRecords>>>> "+query);
       

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID");
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getInt("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("asset_code");
                memo = rs.getString("memo");
                memoValue = rs.getString("memoValue");
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } 
    }
}


public void updateAssetPendingTrans(String batchId){
String query_r ="update AM_GROUP_ASSET set process_flag='Y' where GROUP_ID = '"+batchId+"'";

try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query_r);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("AssetRecordBean: updateAssetPendingTrans()>>>>>" + ex);
        } 

}

public void updateAssetBidPendingTrans(String batchId){
String query_r ="update AM_GROUP_ASSET_BID set process_flag='Y' where GROUP_ID = ? ";

try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query_r);
    ps.setString(1, batchId);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("AssetRecordBean: updateAssetBidPendingTrans()>>>>>" + ex);
        } 

}

public void updateAsset2PendingTrans(String batchId){
String query_r ="update AM_GROUP_ASSET2 set process_flag='Y' where GROUP_ID = ? ";

try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query_r);
    ps.setString(1, batchId);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
  
            System.out.println("AssetRecordBean: updateAssetPendingTrans() for Asset2>>>>>" + ex);
        } 

}

public void updateUncapitalizedAssetPendingTrans(String batchId){
String query_r ="update AM_GROUP_ASSET_UNCAPITALIZED set process_flag='Y' where GROUP_ID = ? ";
//System.out.println("query_r updateUncapitalizedAssetPendingTrans>>>> "+query_r);
		
try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query_r);
    ps.setString(1, batchId);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("AssetRecordBean: updateUncapitalizedAssetPendingTrans()>>>>>" + ex);
        } 
}

public void updateamgroupassetmainTotal(int quantity,String batchId,double total, int supervisor,String branchcode,int userid,String deptcode){
	String query_r ="update am_group_asset_main set vatable_cost="+total+", supervisor = "+supervisor+", " +
			"cost_price = "+total+", branch_code='"+branchcode+"',user_id="+userid+", dept_code='"+deptcode+"', " +
					"quantity = "+quantity+" where GROUP_ID = '"+batchId+"'";
//	System.out.println("query_r updateamgroupassetmainTotal>>>> "+query_r);


	try { 
		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps= con.prepareStatement(query_r);
	           int i =ps.executeUpdate();
	        } catch (Exception ex) {  

	            System.out.println("AssetRecordBean: updateamgroupassetmainTotal()>>>>>" + ex);
	        } 
	}

public void updateamgroupasset2mainTotal(int quantity,String batchId,double total, int supervisor,String branchcode,int userid,String deptcode){
	String query_r ="update am_group_asset2_main set vatable_cost="+total+", supervisor = "+supervisor+", " +
			"cost_price = "+total+", branch_code="+branchcode+",user_id="+userid+", dept_code="+deptcode+", " +
					"quantity = "+quantity+" where GROUP_ID = '"+batchId+"'";
//	System.out.println("query_r updateamgroupasset2mainTotal>>>> "+query_r);


	try {
		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query_r);
	           int i =ps.executeUpdate();
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateamgroupasset2mainTotal()>>>>>" + ex);
	        }


	}

/**
 * *Bulk asset Transfer
 */
public String bulkAssetTransfer(java.util.ArrayList list) {
 String query = "update am_asset SET  " +
				 "BRANCH_ID=?,BRANCH_CODE=?,DEPT_ID=?," +
				 "DEPT_CODE=?,SECTION_ID=?," +
				 "SECTION_CODE=?,SBU_CODE=?," +
				 "ASSET_USER=?,ASSET_ID=?,OLD_ASSET_ID=? " +
				 "WHERE ASSET_ID=? ";
 
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
int assetcode = 0;
int[] d = null;
try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);

        String branchid = bd.getNewbranch_id();
        String branchcode = bd.getBranch_Code();
        String deptid = bd.getNewdepartment_id();
        String deptcode = bd.getDepartment_code();
        String sectionid = bd.getNewsection_id();
        String sectioncode = bd.getSection();
        String sbu = bd.getSbu_code();
        String assetuser = bd.getNewuser();
        String asset_id1 = bd.getAsset_id();
        String newAsset_id = bd.getNewasset_id();
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        String newmtid = transactId+String.valueOf(assetcode);
//        System.out.println("====>newAsset_id: "+newAsset_id+"  asset_id1: "+asset_id1+"  newmtid: "+newmtid);
        if (branchid == null || branchid.equals("")) {
        	branchid = "0";
        }
        if (branchcode == null || branchcode.equals("")) {
        	branchcode = "0";
        }
        if (deptid == null || deptid.equals("")) {
            deptid = "0";
        }
        if (deptcode == null || deptcode.equals("")) {
            deptcode = "0";
        }
        if (sectionid == null || sectionid.equals("")) {
        	sectionid = "0";
        }
        if (sectioncode == null || sectioncode.equals("")) {
        	sectioncode = "0";
        }
        if (assetuser == null || assetuser.equals("")) {
            assetuser = "0";
        }
        if (sbu == null || sbu.equals("")) {
        	sbu = "0";
        }
       if (asset_id1 == null || asset_id1.equals("")) {
            asset_id1 = "0";
        }
        ps.setString(1, branchid);
        ps.setString(2, branchcode);
        ps.setString(3, deptid);
        ps.setString(4, deptcode);
        ps.setString(5, sectionid);
        ps.setString(6, sectioncode);
        ps.setString(7, sbu);
        ps.setString(8, assetuser);
        ps.setString(9, newAsset_id);
        ps.setString(10, asset_id1);
        ps.setString(11, asset_id1);        
        
        ps.addBatch();
//        System.out.println("bulkAssetTransfer newmtid: "+newmtid);
    	String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '"+newmtid+"'";
//    	System.out.println("bulkAssetTransfer revalue_query: "+revalue_query);
    	updateAssetStatusChange(revalue_query);	 
//    	System.out.println("I AM DONE WITH UPDATE ");
    	
}
	d = ps.executeBatch();
	//System.out.println("Executed Successfully ");


} catch (Exception ex) {
	System.out.println("Error Updating asset for Transfer ->" + ex);
} 
mtidassetcode =mtid+String.valueOf(assetcode);
//System.out.println("bulkAssetTransfer mtidassetcode: "+mtidassetcode);
return mtidassetcode;

}
/**
 * *Bulk asset Transfer
 */
public boolean bulkAssetTransfer(java.util.ArrayList list,String newAsset_id) {
 String query = "update am_asset SET  " +
                   "BRANCH_ID=?,BRANCH_CODE=?,DEPT_ID=?," +
                   "DEPT_CODE=?,SECTION_ID=?," +
                   "SECTION_CODE=?,SBU_CODE=?," +
                   "ASSET_USER=?,ASSET_ID=?,OLD_ASSET_ID=? " +
                   "WHERE ASSET_ID=? ";



magma.AssetRecordsBean bd = null;
int[] d = null;
try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);

        String branchid = bd.getNewbranch_id();
        String branchcode = bd.getBranch_Code();
        String deptid = bd.getNewdepartment_id();
        String deptcode = bd.getDepartment_code();
        String sectionid = bd.getNewsection_id();
        String sectioncode = bd.getSection();
        String sbu = bd.getSbu_code();
        String assetuser = bd.getNewuser();
        String asset_id1 = bd.getAsset_id();
        int assetcode = bd.getAssetCode();
        String transactId = bd.getEngine_number();
        String mtidassetcode = transactId+String.valueOf(assetcode);

        if (branchid == null || branchid.equals("")) {
        	branchid = "0";
        }
        if (branchcode == null || branchcode.equals("")) {
        	branchcode = "0";
        }
        if (deptid == null || deptid.equals("")) {
            deptid = "0";
        }
        if (deptcode == null || deptcode.equals("")) {
            deptcode = "0";
        }
        if (sectionid == null || sectionid.equals("")) {
        	sectionid = "0";
        }
        if (sectioncode == null || sectioncode.equals("")) {
        	sectioncode = "0";
        }
        if (assetuser == null || assetuser.equals("")) {
            assetuser = "0";
        }
        if (sbu == null || sbu.equals("")) {
        	sbu = "0";
        }
       if (asset_id1 == null || asset_id1.equals("")) {
            asset_id1 = "0";
        }
        ps.setString(1, branchid);
        ps.setString(2, branchcode);
        ps.setString(3, deptid);
        ps.setString(4, deptcode);
        ps.setString(5, sectionid);
        ps.setString(6, sectioncode);
        ps.setString(7, sbu);
        ps.setString(8, assetuser);
        ps.setString(9, newAsset_id);
        ps.setString(10, asset_id1);
        ps.setString(11, asset_id1);

        ps.addBatch();
}
	d = ps.executeBatch();
	//System.out.println("Executed Successfully ");



} catch (Exception ex) {
	System.out.println("Error Updating asset for Transfer ->" + ex);
} 

return (d.length > 0);

}

public String accountReturn(String assetId) {

  
    String accountNo = "";
//    String query = "select c.category_name,(d.country_prefix + d.dr_prefix + b.branch_code + c.Dep_ledger) AS DRACCT,(d.country_prefix + "+
//    		"d.cr_prefix + b.branch_code + c.Accum_Dep_ledger) AS CRACCT,sum(a.monthly_dep),d.processing_date from "+
//    		"am_ad_branch b,am_ad_category c,am_asset a,am_gb_company d where a.category_code = c.category_code "+
//    		"and a.Asset_id = ? "+
//    		"And a.branch_code = b.branch_code and a.asset_status ='active' and a.cost_price > d.cost_threshold "+
//    		"and a.dep_rate > 0 and c.Accum_Dep_ledger <> '" +" "+"' group by c.category_name,b.branch_code, "+
//    		"c.Accum_Dep_ledger,c.Dep_ledger,d.processing_date,d.country_prefix,d.dr_prefix,d.cr_prefix order "+
//    		"by b.branch_code asc";
    String query = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ACTRET'");

    
    try {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, assetId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
			String dracct = rs.getString("DRACCT");
			String cracct = rs.getString("CRACCT");
			accountNo = dracct + "#"+ cracct;

        }

    } catch (Exception ex) {
        System.out.println("WARN: Error determining  DR & CR accountReturn->" +
                           ex);
    } 

    return accountNo;
}
/**
 * *Bulk asset Transfer
 */
public String bulkAssetTransferUpdate(java.util.ArrayList list) {
 String query = "update am_assettransfer SET  " +
				 "approval_Status=? " +
				 "WHERE transfer_id=? ";
//System.out.println("bulkAssetTransferUpdate query: "+query);
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
String newmtid = "";
int assetcode = 0;
int[] d = null;
try {
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        newmtid = transactId+String.valueOf(assetcode);
  //      System.out.println("bulkAssetTransferUpdate newmtid: "+newmtid+"   MTID: "+mtid);
        ps.setString(1, newmtid);
        ps.setString(2, "ACTIVE");        
        ps.addBatch();
}
	d = ps.executeBatch();
	

} catch (Exception ex) {
	System.out.println("Error Updating asset for Transfer ->" + ex);
} 
mtidassetcode =mtid+String.valueOf(assetcode);
return mtidassetcode;

}

public String checkAccounDepreciationDR (String category,String branch)
{
String assetAcqusitionSuspense="";
/*System.out.println("category "+category);
System.out.println("branch "+branch);*/
//String query=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(a.Dep_ledger,1,1)),"
//			+" b.default_branch,"
//			+" a.Dep_ledger,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Dep_ledger,1,1))+"
//			+" b.default_branch +	a.Dep_ledger asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";
////System.out.println("query in checkAccounAcqusitionSuspense >>>> " + query);
String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'EX'");
String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";

try {

	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query);
	ResultSet rs = ps.executeQuery();

	if (rs.next())
	 {

		assetAcqusitionSuspense  = rs.getString("asd");


	 }
   }
	catch (Exception er)
	{
	 er.printStackTrace();

	}
	
return 	assetAcqusitionSuspense;
}

public String checkAccounDepreciationCR (String category,String branch)
{
String assetAcqusitionSuspense="";
/*System.out.println("category "+category);
System.out.println("branch "+branch);*/
//String query=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1)),"
//			+" b.default_branch,"
//			+" a.Accum_Dep_ledger,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(a.Accum_Dep_ledger,1,1))+"
//			+" b.default_branch +	a.Accum_Dep_ledger asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";
//System.out.println("query in checkAccounAcqusitionSuspense >>>> " + query);
String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'DEPCR'");
String query = script+" and a.category_code = ? and d.branch_code = ? ";

try {

	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(query);
    ps.setString(1, category);
    ps.setString(2, branch);
    ResultSet rs = ps.executeQuery();

	if (rs.next())
	 {

		assetAcqusitionSuspense  = rs.getString("asd");


	 }
   }
	catch (Exception er)
	{
	 er.printStackTrace();

	}
	
return 	assetAcqusitionSuspense;
}

public boolean insertVendorTransaction(String userId,String Description,String debitAccount,String creditAccount,double amount,String location,String transactionId,String transactionType,String projectCode,String vendorCode) {
	  boolean done=true;

	  String query = "INSERT INTO [VENDOR_TRANSACTIONS](USER_ID,DESCRIPTION,DRACCOUNT_NO,CRACCOUNT_NO,COST_PRICE,LOCATION,TRANS_ID,TRANSACTION_TYPE,TRANSACTION_DATE,PROJECT_CODE,VENDOR_CODE)" +
	                 " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	  try {
		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query);
	      ps.setString(1,userId);
	      ps.setString(2,Description);
	      ps.setString(3, debitAccount);
	      ps.setString(4,creditAccount);
	      ps.setDouble(5,amount);
	      ps.setString(6,location);
	      ps.setString(7,transactionId);
	      ps.setString(8, transactionType);
	      ps.setTimestamp(9, dbConnection.getDateTime(new java.util.Date()));
	      ps.setString(10,projectCode);
	      ps.setString(11,vendorCode);
	      ps.execute();

	  }
	  catch (Exception ex)
	  {
		   done = false;
	      System.out.println("WARNING:cannot insert vendor_transaction table->" );
	      ex.printStackTrace();
	  } 
	  return done;
	}

public void updateamgroupassetImprovement(String groupId){
	String query_r ="UPDATE AM_GROUP_IMPROVEMENT SET IMPROVED='P' WHERE REVALUE_ID = ? ";
	String query_del ="DELETE FROM AM_GROUP_IMPROVEMENT WHERE REVALUE_ID = '"+groupId+"'";
//	System.out.println("query_r updateamgroupassetImprovement>>>> "+query_r);
	
 
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query_r);
	        ps.setString(1, groupId);
	           int i =ps.executeUpdate();
//	       	ps1 = con.prepareStatement(query_del);
//	           int j =ps1.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateamgroupassetImprovement()>>>>>" + ex);
	        } 


	}

/**
    * *Bulk asset update
    */
/*
   public boolean bulkAssetUpdateFromVerification(java.util.ArrayList list,int tranId) {
    String query = "UPDATE AM_ASSET SET  " +
                      "AM_ASSET.Description = am_gb_workbookselection.Description, AM_ASSET.BAR_CODE = am_gb_workbookselection.BAR_CODE, " +
                      "AM_ASSET.ASSET_USER = am_gb_workbookselection.ASSET_USER " +
                      "FROM AM_ASSET  INNER JOIN  am_gb_workbookselection ON AM_ASSET.ASSET_ID = am_gb_workbookselection.ASSET_ID " +
                      "AND am_gb_workbookselection.batch_id = "+tranId+" AND am_gb_workbookselection.PROCESS_STATUS = 'APPROVED'" +
                      " AND AM_ASSET.ASSET_ID = ? ";


	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	magma.AssetRecordsBean bd = null;
	boolean result=true;
	int[] d = null;
	try {  
		con = getConnection();
		ps = con.prepareStatement(query);

		for (int i = 0; i < list.size(); i++) {
			bd = (magma.AssetRecordsBean) list.get(i);

           String description = bd.getDescription();
           String assetUser = bd.getUser();
           String barcode = bd.getBar_code();
           String asset_id1 = bd.getAsset_id();
           if (assetUser == null || assetUser.equals("")) {
        	   assetUser = "";
           }
           if (description == null || description.equals("")) {
           	description = "";
           }       
          if (asset_id1 == null || asset_id1.equals("")) {
               asset_id1 = "0";
           }
           if (barcode == null || barcode.equals("")) {
               barcode = "0";
           }
  //         System.out.println("<<<<<<<<<>>>>>>>>>>>> ");
           ps.setString(1, description);
           ps.setString(2, barcode);
           ps.setString(3, assetUser);
           ps.setString(4, asset_id1);
 //          System.out.println("<<<<asset_id1: "+asset_id1+"  description: "+description+"  barcode: "+barcode+"   assetUser: "+assetUser);
     //      ps.addBatch();
           ps.execute();
	}
	//	d = ps.executeBatch();
		//System.out.println("Executed Successfully ");
		
		ps.execute();

	} catch (Exception ex) {
		result = false;
		System.out.println("Error Updating all asset in bulkAssetUpdateFromVerification ->" + ex);
	} finally {
		closeConnection(con, ps);
	}

	//return (d.length > 0);
	return result;

}
*/
   public boolean bulkAssetUpdateFromVerification(Long tranId){
	    String query = "UPDATE AM_ASSET SET  " +
                "AM_ASSET.Description = am_gb_workbookselection.Description, AM_ASSET.BAR_CODE = am_gb_workbookselection.BAR_CODE, " +
                "AM_ASSET.ASSET_USER = am_gb_workbookselection.ASSET_USER " +
                "FROM AM_ASSET  INNER JOIN  am_gb_workbookselection ON AM_ASSET.ASSET_ID = am_gb_workbookselection.ASSET_ID " +
                "AND am_gb_workbookselection.batch_id = ? AND am_gb_workbookselection.PROCESS_STATUS = 'APPROVED'" ;
	//   System.out.println("query bulkAssetUpdateFromVerification>>>> "+query);
	  
	           boolean result=true;
	   try {

			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps  = con.prepareStatement(query);
		        ps.setLong(1, tranId);
	              int i =ps.executeUpdate();
	           } catch (Exception ex) {

	               System.out.println("AssetRecordBean: bulkAssetUpdateFromVerification()>>>>>" + ex);
	           } 
	   return result;

	   }


public void updateFleetGroupUpload(String groupId){
	String query_r ="UPDATE FT_GROUP_DUE_PERIOD SET IMPROVED='P' WHERE GROUP_ID = ? ";
	String query_del ="DELETE FROM FT_GROUP_DUE_PERIOD WHERE GROUP_ID = ? ";
//	System.out.println("query_r updateFleetGroupUpload>>>> "+query_r);
	
 
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_r);
	        ps.setString(1, groupId);
	           int i =ps.executeUpdate();
	      PreparedStatement ps1 = con.prepareStatement(query_del);
	      ps1.setString(1, groupId);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateFleetGroupUpload()>>>>>" + ex);
	        } 
	}

 
public String[] setApprovalDataTranType(String id,String asset_id,String tranType, String supervisor,String userId,String amount){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
 //String currentDate  = reArrangeDate(getCurrentDate1());
 //  System.out.println("the $$$$$$$$$$$ id: "+id+"  asset_id: "+asset_id+"   tranType: "+tranType+"  supervisor: "+supervisor+"   userId: "+userId+"  amount: "+amount);
    String[] result= new String[12];
   

         String query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id = ? ";



        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query);
  	        ps.setString(1, asset_id);
           ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //
            	result[0] = rs.getString(1);
                // result[1]= rs.getString(2);
               // result[2] = rs.getString(3);
               result[0] = id;
               result[1] = userId;
               result[2] = supervisor;
               result[3] = amount;
             //  result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching setApprovalDataTranType ->" + ex);
        } 
result[9] = tranType;
result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


	public String checkFTLedgerAccount(String tranTYpe,String branch)
	{
		String assetledgeraccount="";
//		System.out.println("tranTYpe "+tranTYpe);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1)),"
//					+" b.default_branch,"
//					+" a.gl_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1))+"
//					+" b.default_branch + a.gl_account asd"
//					+" from FT_PROCESSING_TYPE a,am_ad_branch d, "
//					+" AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where c.currency_id = '1'"
//					+" and a.FT_TYPE_CODE = '"+tranTYpe+"'"
//					+" and d.branch_Code = '"+branch+"'";
//		String query=" select c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1)),"
//				+" '"+branch+"' AS default_branch,"
//				+" a.gl_account,"
//				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1))+"
//				+" '"+branch+"' + a.gl_account asd"
//				+" from FT_PROCESSING_TYPE a,am_ad_branch d, "
//				+" AM_GB_CURRENCY_CODE c, am_gb_company b"
//				+" where c.currency_id = '1'"
//				+" and a.FT_TYPE_CODE = '"+tranTYpe+"'"
//				+" and d.branch_Code = '"+branch+"'";		
////		System.out.println("query in checkAssetLedgerAccount >>>> " + query);
		String query = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'FTACT'");

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps  = con.prepareStatement(query);
		        ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {
				 assetledgeraccount  = rs.getString("asd");

			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	assetledgeraccount;
	}


public String setPendingTransforAssetProof(String[] a, String code,String mtid){
//	System.out.println("====code 1====> "+code);
    String mtid_id ="";
        int transaction_level=0;
       
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
     
        try
        {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
             ResultSet rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);


            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String transId =  new ApplicationHelper().getGeneratedId("am_asset_approval");
//            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(1,mtid);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (a[5]==null)?"":a[5]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,transId);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);

            ps.execute();

            mtid_id = mtid;
        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTransforAssetProof Two 2>>>>>>" + er);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";



return mtid_id;


    }//setPendingTrans2()

public boolean bulkAssetUpdateFromProof(String prooftranId){
    String query = "UPDATE AM_ASSET SET  " +
            "AM_ASSET.Description = am_Asset_Proof_Selection.Description, AM_ASSET.BAR_CODE = am_Asset_Proof_Selection.BAR_CODE, " +
            "AM_ASSET.ASSET_USER = am_Asset_Proof_Selection.ASSET_USER " +
            "FROM AM_ASSET  INNER JOIN  am_Asset_Proof_Selection ON AM_ASSET.ASSET_ID = am_Asset_Proof_Selection.ASSET_ID " +
            "AND am_Asset_Proof_Selection.batch_id = ? AND am_Asset_Proof_Selection.PROCESS_STATUS = 'APPROVED'" ;
//   System.out.println("query bulkAssetUpdateFromVerification>>>> "+query);
 
           boolean result=true;
   try {
		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, prooftranId);
              int i =ps.executeUpdate();
           } catch (Exception ex) {

               System.out.println("AssetRecordBean: bulkAssetUpdateFromProof()>>>>>" + ex);
           } 
   return result;

   }

public void setPendingTransDisposal(String[] a, String code,int assetCode,String batchId){

    int transaction_level=0;
    
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
   
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
         ResultSet rs = ps.executeQuery();


        while(rs.next())
        {
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if



        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
        ps.setString(1, (a[0]==null)?"":a[0]);
        ps.setString(2, (a[1]==null)?"":a[1]);
        ps.setString(3, (a[2]==null)?"":a[2]);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
        ps.setString(6, (a[5]==null)?"":a[5]);
        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
        ps.setString(8, (a[7]==null)?"":a[7]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
        ps.setString(10, (a[9]==null)?"":a[9]);
        ps.setString(11, a[10]);
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,mtid);
        ps.setString(14, batchId);
        ps.setInt(15, transaction_level);
        ps.setInt(16,assetCode);

        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans setPendingTransDisposal>>>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()


public int insertAssetTwoRecord() throws Exception, Throwable {
    String[] budget = getBudgetInfo();
    double[] bugdetvalues = getBudgetValues();
    int DONE = 0; //everything is oK
    int BUDGETENFORCED = 1; //EF budget = Yes, CF = NO, ERROR_FLAG
    int BUDGETENFORCEDCF = 2; //EF budget = Yes, CF = Yes, ERROR_FLAG
    int ASSETPURCHASEBD = 3; //asset falls into no quarter purchase date older than bugdet
    String Q = getQuarter();
    if(budget[3].equalsIgnoreCase("N")){
		rinsertAssetTwoRecord();
		return DONE;
	}
	else if(budget[3].equalsIgnoreCase("Y")){
    if (!Q.equalsIgnoreCase("NQ")) {
        if (budget[3].equalsIgnoreCase("Y") &&
            budget[4].equalsIgnoreCase("N")) {
            if (chkBudgetAllocation(Q, bugdetvalues, false)) {
                updateBudget(Q, budget);
                rinsertAssetTwoRecord();
                return DONE;
            } else {
                return BUDGETENFORCED;
            }

        } else if (budget[3].equalsIgnoreCase("Y") &&
                   budget[4].equalsIgnoreCase("Y")) {
            if (chkBudgetAllocation(Q, bugdetvalues, true)) {
                updateBudget(Q, budget);
                rinsertAssetTwoRecord();
                return DONE;
            } else {
                return BUDGETENFORCEDCF;
            }

        } else {
        	rinsertAssetTwoRecord();
            return DONE;
        }
    } else {
        //rinsertAssetRecord();
        return ASSETPURCHASEBD;
    }}
    return 0;
}


private void rinsertAssetTwoRecord(String assetId) throws Exception, Throwable {
// asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
      //  department_id, section_id, category_id);

boolean done = true;
AssetPaymentManager payment = null;
/*if (require_redistribution.equalsIgnoreCase("Y")) {
    status = "Z";
         }*/
if (make == null || make.equals("")) {
    make = "0";
}
if (maintained_by == null || maintained_by.equals("")) {
    maintained_by = "0";
}
if (supplied_by == null || supplied_by.equals("")) {
    supplied_by = "0";
}
if (user == null || user.equals("")) {
    user = "";
}
if (location == null || location.equals("")) {
    location = "0";
}
if (driver == null || driver.equals("")) {
    driver = "0";
}
if (state == null || state.equals("")) {
    state = "0";
}
if (department_id == null || department_id.equals("")) {
    department_id = "0";
}
if (vat_amount == null || vat_amount.equals("")) {
    vat_amount = "0.0";
}
if (vatable_cost == null || vatable_cost.equals("")) {
    vatable_cost = "0.0";
}
if (transport_cost == null || transport_cost.equals("")) {
	transport_cost = "0.0";
}
if (other_cost == null || other_cost.equals("")) {
	other_cost = "0.0";
}
if (wh_tax_amount == null || wh_tax_amount.equals("")) {
    wh_tax_amount = "0";
}
if (branch_id == null || branch_id.equals("")) {
    branch_id = "0";
}
if (province == null || province.equals("")) {
    province = "0";
}
if (category_id == null || category_id.equals("")) {
    category_id = "0";
}
if (sub_category_id == null || sub_category_id.equals("")) {
    sub_category_id = "0";
}

if (residual_value == null || residual_value.equals("")) {
    residual_value = "0";
}
if (section_id == null || section_id.equals("")) {
    section_id = "0";
}

if (noOfMonths == null || noOfMonths.equals("")) {
    noOfMonths = "0";
}
if (warrantyStartDate == null || warrantyStartDate.equals("")) {
    warrantyStartDate = null;
}
if (expiryDate == null || expiryDate.equals("")) {
    expiryDate = null;
}
//if (supervisor == null || expiryDate.equals("")) {
//    expiryDate = null;
// }

/*
if (bar_code == null || bar_code.equals("")) {
    bar_code = null;
}
*/
/*      if (sbu_code == null || sbu_code.equals("")) {
    sbu_code=("0");
}
*/

/*
if (lpo == null || lpo.equals("")) {
    lpo=("0");
}
*/
vat_amount = vat_amount.replaceAll(",", "");
vatable_cost = vatable_cost.replaceAll(",", "");
wh_tax_amount = wh_tax_amount.replaceAll(",", "");
residual_value = residual_value.replaceAll(",", "");
amountPTD = amountPTD.replaceAll(",","");
String createQuery = "INSERT INTO AM_ASSET2_ARCHIVE        " +
                     "(" +
                     "ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
                     "SECTION_ID, CATEGORY_ID, [DESCRIPTION], VENDOR_AC," +
                     "DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL," +
                     "ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME," +

                     "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP," +
                     "COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE," +
                     "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON," +
                     "USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE," +

                     "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION," +
                     "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1," +
                     "WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, [SECTION]," +
                     "STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, [USER_ID]," +
                     "MULTIPLE,PROVINCE,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                     "BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,	AMOUNT_PTD," +
                     "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,"+
                     "defer_pay,wht_percent,system_ip,mac_address,asset_code,SPARE_3, SPARE_4,SPARE_5, SPARE_6,sub_category_id, SUB_CATEGORY_CODE) " +
                     "VALUES" +
                     "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                      "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*
 *First Create Asset Records
 * and then determine if it
 * should be made available for fleet.
 */

try {

    double costPrice = Double.parseDouble(vat_amount) +
                       Double.parseDouble(vatable_cost);


	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(createQuery);
    ps.setString(1, assetId);
    ps.setString(2, registration_no);
    ps.setInt(3, Integer.parseInt(branch_id));
    ps.setInt(4, Integer.parseInt(department_id));
    ps.setInt(5, Integer.parseInt(section_id));
    ps.setInt(6, Integer.parseInt(category_id));
    ps.setString(7, description.toUpperCase());
    ps.setString(8, vendor_account);
    ps.setDate(9, dbConnection.dateConvert(date_of_purchase));
    ps.setString(10, getDepreciationRate(category_id));
    ps.setString(11, make);
    ps.setString(12, model);
    ps.setString(13, serial_number);
    ps.setString(14, engine_number);
    ps.setInt(15, Integer.parseInt(supplied_by));
    ps.setString(16, user);
    ps.setInt(17, Integer.parseInt(maintained_by));
    ps.setInt(18, 0);
    ps.setInt(19, 0);
    ps.setDouble(20, costPrice);
    ps.setDouble(21, costPrice);
    ps.setDate(22, dbConnection.dateConvert(depreciation_end_date));
    ps.setDouble(23, Double.parseDouble(residual_value));
    ps.setString(24, authorized_by);
    ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
    ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
    ps.setString(27, reason);
    ps.setString(28, "0");
    ps.setString(29, computeTotalLife(getDepreciationRate(category_id)));
    ps.setInt(30, Integer.parseInt(location));
    ps.setString(31, computeTotalLife(getDepreciationRate(category_id)));
    ps.setDouble(32, Double.parseDouble(vatable_cost));
    ps.setDouble(33, Double.parseDouble(vat_amount));
    ps.setString(34, wh_tax_cb);
    ps.setDouble(35, Double.parseDouble(wh_tax_amount));
    ps.setString(36, require_depreciation);
    ps.setString(37, require_redistribution);
    ps.setString(38, subject_to_vat);
    ps.setString(39, who_to_remind);
    ps.setString(40, email_1);
    ps.setString(41, who_to_remind_2);
    ps.setString(42, email2);
    ps.setString(43, raise_entry);
    ps.setString(44, "0");
    ps.setString(45, section);
    ps.setInt(46, Integer.parseInt(state));
    ps.setInt(47, Integer.parseInt(driver));
    ps.setString(48, spare_1);
    ps.setString(49, spare_2);
    ps.setString(50, status);
    ps.setString(51, user_id);
    ps.setString(52, multiple);
    ps.setString(53, province);
    ps.setDate(54, dbConnection.dateConvert(warrantyStartDate));
    ps.setInt(55, new Integer(noOfMonths).intValue());
    ps.setDate(56, dbConnection.dateConvert(expiryDate));
    ps.setString(57, code.getBranchCode(branch_id));
    ps.setString(58, code.getDeptCode(department_id));
    ps.setString(59, code.getSectionCode(section_id));
    ps.setString(60, code.getCategoryCode(category_id));
    ps.setDouble(61, Double.parseDouble(amountPTD));
    //ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
    //Use Vatable_cost instead of costPrice for Manage Asset Payment.====11/08/2009 ayojava
    ps.setDouble(62, (Double.parseDouble(vatable_cost)));
    ps.setString(63, partPAY);
    ps.setString(64, fullyPAID);
    ps.setString(65, bar_code);
    ps.setString(66,sbu_code);
    ps.setString(67, lpo);
    ps.setString(68,getSupervisor());
    ps.setString(69, deferPay);
    ps.setDouble(70, Double.parseDouble(selectTax));
    ps.setString(71,getSystemIp());
    ps.setString(72, getMacAddress());
    ps.setInt(73,assetCode);
    ps.setString(74, spare_3);
    ps.setString(75, spare_4);
    ps.setString(76, spare_5);
    ps.setString(77, spare_6);
    ps.setInt(78, Integer.parseInt(sub_category_id));
    ps.setString(79, code.getSubCategoryCode(sub_category_id));
    
    ps.execute();

} catch (Exception ex) {
    done = false;
    System.out.println("WARN:Error inserting into  asset two creation archive->" + ex);
} 

// return done;
}

/**
 * getAssetTwoRecords
 */
public void getAssetTwoRecords() throws Exception {

    if (asset_id != "") {
        String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID,A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code,A.memo,A.memovalue, " +
                       "A.IMPROV_NBV,A.IMPROV_ACCUMDEP,A.IMPROV_COST,A.IMPROV_MONTHLYDEP,A.TOTAL_NBV, " +
                       "A.WAREHOUSE_CODE, A.ITEM_CODE,A.ITEMTYPE,A.PROJECT_CODE,REGION_CODE FROM AM_ASSET2 A " +
                       "WHERE A.ASSET_ID = '" + asset_id + "'";
//		System.out.println("getAssetRecords query: "+query);
      

        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query); 
           ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID"); 
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                nbv = rs.getString("NBV");
                monthlydep = rs.getString("MONTHLY_DEP");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getInt("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("asset_code");
                memo = rs.getString("memo");
                memoValue = rs.getString("memoValue");
                improveCost = rs.getString("IMPROV_COST");
                improveNbv = rs.getString("IMPROV_NBV");
                improveAccumdep = rs.getString("IMPROV_ACCUMDEP");
                improveMonthlydep = rs.getString("IMPROV_MONTHLYDEP");
                improveTotalNbv = rs.getString("TOTAL_NBV");
                warehouseCode = rs.getString("WAREHOUSE_CODE");
                itemCode = rs.getString("ITEM_CODE");
                itemType = rs.getString("ITEMTYPE");
                projectCode = rs.getString("PROJECT_CODE");
                regionCode = rs.getString("REGION_CODE");
                
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } 
    }
}



public String[] setApprovalDataGroupAsset2(long id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
  
         String query ="select group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
         		"		description,effective_date,BRANCH_CODE," +
         				"Asset_Status from am_group_asset2_main where group_id = ? " ;
        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query);
  	        ps.setLong(1, id);
           ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = String.valueOf(id);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = dbConnection.formatDate(rs.getDate(5));
               result[5] = rs.getString(6);
               result[6] = dbConnection.formatDate(rs.getDate(7));
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }
//            System.out.println("Final Conversion for Asset2");
        } catch (Exception ex) {
            System.out.println("AssetRecordsBean : setApprovalDataGroupAsset2()WARN: Error setting approval data for group asset2 creation ->" + ex);
        } 
        	result[9] = "Group Asset2 Creation";
        	result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


public void setInsertAsset2GroupMainTrans(int quantity){
    int transaction_level=0;
  
    boolean done = false;

SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String insertquery = "insert into AM_GROUP_ASSET2_MAIN(QUANTITY,BRANCH_ID,DEPT_ID,"+ 
		"CATEGORY_ID,SECTION_ID,DESCRIPTION,DATE_PURCHASED,DEP_RATE,"+
		"POSTING_DATE,EFFECTIVE_DATE,raise_entry,process_flag,asset_make,cost_price," +
		"residual_value,invoice_no,LPO,vatable_cost,supervisor,Fully_paid,part_pay,state," +
		"driver,amount_ptd,user_id,amount_rem,section_code,vat,dept_code,branch_code," +
		"Asset_Status,Req_depreciation,wh_tax_amount,wh_tax"+
		")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("setUploadPendingTrans Query pq===> "+pq);
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(insertquery);
    	ps.setInt(1, quantity);    
    	ps.setInt(2, '1');
    	ps.setInt(3, '1');
    	ps.setInt(4, '1');
    	ps.setInt(5, '1');
    	ps.setString(6, "Asset Upload"); 
    	ps.setDate(7, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDouble(8, 0.00);
    	ps.setDate(9, dbConnection.dateConvert(depreciation_start_date));
    	ps.setDate(10, dbConnection.dateConvert(depreciation_start_date));
    	ps.setString(11, "N");
    	ps.setString(12, "Y");
    	ps.setInt(13, '0');
    	ps.setDouble(14, 0.00);
    	ps.setDouble(15, 10.00);
    	ps.setString(16, "1");
    	ps.setString(17, "1");
    	ps.setDouble(18, 0.00);
    	ps.setInt(19, '2');
    	ps.setString(20, "Y");
    	ps.setString(21, "N");
    	ps.setInt(22, '1');
    	ps.setInt(23, '0');
    	ps.setDouble(24, 0.00);
    	ps.setInt(25, '2');
    	ps.setDouble(26, 0.00);
    	ps.setString(27, "001");
    	ps.setDouble(28, 0.00);
    	ps.setString(29, "011");
    	ps.setString(30, "014");
    	ps.setString(31, "PENDING");
    	ps.setString(32, "N");
    	ps.setDouble(33, 0.00);
    	ps.setString(34, "S");
//        con = dbConnection.getConnection("legendPlus");

   //     ps = con.prepareStatement(pq);

   //     ps.execute();
    	done = (ps.executeUpdate() != -1);
    }
    
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setInsertAsset2GroupMainTrans(>>>>>>" + er);
    } 
    
}

/**
 * *Bulk asset2 Transfer
 */
public String bulkAsset2Transfer(java.util.ArrayList list) {
 String query = "update am_asset2 SET  " +
				 "BRANCH_ID=?,BRANCH_CODE=?,DEPT_ID=?," +
				 "DEPT_CODE=?,SECTION_ID=?," +
				 "SECTION_CODE=?,SBU_CODE=?," +
				 "ASSET_USER=?,ASSET_ID=?,OLD_ASSET_ID=? " +
				 "WHERE ASSET_ID=? ";
 
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
int assetcode = 0;
int[] d = null;
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);

        String branchid = bd.getNewbranch_id();
        String branchcode = bd.getBranch_Code();
        String deptid = bd.getNewdepartment_id();
        String deptcode = bd.getDepartment_code();
        String sectionid = bd.getNewsection_id();
        String sectioncode = bd.getSection();
        String sbu = bd.getSbu_code();
        String assetuser = bd.getNewuser();
        String asset_id1 = bd.getAsset_id();
        String newAsset_id = bd.getNewasset_id();
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        String newmtid = transactId+String.valueOf(assetcode);
        
        if (branchid == null || branchid.equals("")) {
        	branchid = "0";
        }
        if (branchcode == null || branchcode.equals("")) {
        	branchcode = "0";
        }
        if (deptid == null || deptid.equals("")) {
            deptid = "0";
        }
        if (deptcode == null || deptcode.equals("")) {
            deptcode = "0";
        }
        if (sectionid == null || sectionid.equals("")) {
        	sectionid = "0";
        }
        if (sectioncode == null || sectioncode.equals("")) {
        	sectioncode = "0";
        }
        if (assetuser == null || assetuser.equals("")) {
            assetuser = "0";
        }
        if (sbu == null || sbu.equals("")) {
        	sbu = "0";
        }
       if (asset_id1 == null || asset_id1.equals("")) {
            asset_id1 = "0";
        }
        ps.setString(1, branchid);
        ps.setString(2, branchcode);
        ps.setString(3, deptid);
        ps.setString(4, deptcode);
        ps.setString(5, sectionid);
        ps.setString(6, sectioncode);
        ps.setString(7, sbu);
        ps.setString(8, assetuser);
        ps.setString(9, newAsset_id);
        ps.setString(10, asset_id1);
        ps.setString(11, asset_id1);        
        
        ps.addBatch();
 //       System.out.println("bulkAsset2Transfer newmtid: "+newmtid);
    	String revalue_query = "update am_asset2transfer set approval_Status='ACTIVE' where transfer_id = '"+newmtid+"'";
 //   	System.out.println("bulkAsset2Transfer revalue_query: "+revalue_query);
    	updateAssetStatusChange(revalue_query);	 
 //   	System.out.println("I AM DONE WITH UPDATE OF ASSET2 BULK TRANSFER ");
    	
}
	d = ps.executeBatch();
	//System.out.println("Executed Successfully ");


} catch (Exception ex) {
	System.out.println("Error Updating asset2 for Transfer ->" + ex);
} 
mtidassetcode =mtid+String.valueOf(assetcode);
System.out.println("bulkAsset2Transfer mtidassetcode: "+mtidassetcode);
return mtidassetcode;

}

/**
 * *Bulk asset Transfer
 */
public boolean bulkAsset2Transfer(java.util.ArrayList list,String newAsset_id) {
 String query = "update am_asset2 SET  " +
                   "BRANCH_ID=?,BRANCH_CODE=?,DEPT_ID=?," +
                   "DEPT_CODE=?,SECTION_ID=?," +
                   "SECTION_CODE=?,SBU_CODE=?," +
                   "ASSET_USER=?,ASSET_ID=?,OLD_ASSET_ID=? " +
                   "WHERE ASSET_ID=? ";



magma.AssetRecordsBean bd = null;
int[] d = null;
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);

        String branchid = bd.getNewbranch_id();
        String branchcode = bd.getBranch_Code();
        String deptid = bd.getNewdepartment_id();
        String deptcode = bd.getDepartment_code();
        String sectionid = bd.getNewsection_id();
        String sectioncode = bd.getSection();
        String sbu = bd.getSbu_code();
        String assetuser = bd.getNewuser();
        String asset_id1 = bd.getAsset_id();
        int assetcode = bd.getAssetCode();
        String transactId = bd.getEngine_number();
        String mtidassetcode = transactId+String.valueOf(assetcode);

        if (branchid == null || branchid.equals("")) {
        	branchid = "0";
        }
        if (branchcode == null || branchcode.equals("")) {
        	branchcode = "0";
        }
        if (deptid == null || deptid.equals("")) {
            deptid = "0";
        }
        if (deptcode == null || deptcode.equals("")) {
            deptcode = "0";
        }
        if (sectionid == null || sectionid.equals("")) {
        	sectionid = "0";
        }
        if (sectioncode == null || sectioncode.equals("")) {
        	sectioncode = "0";
        }
        if (assetuser == null || assetuser.equals("")) {
            assetuser = "0";
        }
        if (sbu == null || sbu.equals("")) {
        	sbu = "0";
        }
       if (asset_id1 == null || asset_id1.equals("")) {
            asset_id1 = "0";
        }
        ps.setString(1, branchid);
        ps.setString(2, branchcode);
        ps.setString(3, deptid);
        ps.setString(4, deptcode);
        ps.setString(5, sectionid);
        ps.setString(6, sectioncode);
        ps.setString(7, sbu);
        ps.setString(8, assetuser);
        ps.setString(9, newAsset_id);
        ps.setString(10, asset_id1);
        ps.setString(11, asset_id1);

        ps.addBatch();
}
	d = ps.executeBatch();
	//System.out.println("Executed Successfully ");



} catch (Exception ex) {
	System.out.println("Error Updating asset2 for Transfer ->" + ex);
} 

return (d.length > 0);

}


/**
 * getAsset2Records
 */
public void getAsset2Records() throws Exception {

    if (asset_id != "") {
        String query = "SELECT A.ASSET_ID, A.REGISTRATION_NO," +
                       "A.BRANCH_ID, A.DEPT_ID, A.CATEGORY_ID,A.SUB_CATEGORY_ID,A.SECTION, A.DESCRIPTION," +
                       "A.VENDOR_AC, A.DATE_PURCHASED," +
                       "A.DEP_RATE, A.ASSET_MAKE, A.ASSET_MODEL, A.ASSET_SERIAL_NO, A.ASSET_ENGINE_NO," +
                       "A.SUPPLIER_NAME, A.ASSET_USER, A.ASSET_MAINTENANCE, A.ACCUM_DEP, A.MONTHLY_DEP," +
                       "A.COST_PRICE, A.NBV,A.STATE,A.DRIVER,A.WH_TAX,A.WH_TAX_AMOUNT," +
                       "DEP_END_DATE, A.RESIDUAL_VALUE, A.AUTHORIZED_BY, A.POSTING_DATE,A.EFFECTIVE_DATE," +
                       "A.PURCHASE_REASON, A.USEFUL_LIFE, A.TOTAL_LIFE, A.LOCATION," +
                       "A.REMAINING_LIFE, A.VATABLE_COST, A.VAT, A.REQ_DEPRECIATION," +
                       "A.SUBJECT_TO_VAT, A.WHO_TO_REM, A.EMAIL1, A.EMAIL2," +
                       "A.RAISE_ENTRY, A.DEP_YTD, A.SECTION_ID, A.ASSET_STATUS," +
                       "A.VENDOR_AC,A.SPARE_1,A.SPARE_2,A.SPARE_3,A.SPARE_4,A.SPARE_5,A.SPARE_6,A.REQ_REDISTRIBUTION,A.DATE_DISPOSED," +
                       "A.[USER_ID], A.WHO_TO_REM_2,A.MULTIPLE,A.WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE," +
                       "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID, GROUP_ID,A.BAR_CODE,A.SBU_CODE," +
                       "A.LPO,A.SUPERVISOR,A.defer_pay,A.wht_percent,A.asset_code,A.memo,A.memovalue, " +
                       "A.IMPROV_NBV,A.IMPROV_ACCUMDEP,A.IMPROV_COST,A.IMPROV_MONTHLYDEP,A.TOTAL_NBV, " +
                       "A.WAREHOUSE_CODE, A.ITEM_CODE,A.ITEMTYPE,A.PROJECT_CODE,REGION_CODE FROM AM_ASSET2 A " +
                       "WHERE A.ASSET_ID = ? ";
//		System.out.println("getAssetRecords query: "+query);
        

        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query);
  	        ps.setString(1, asset_id);
           ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                registration_no = rs.getString("REGISTRATION_NO");
                branch_id = rs.getString("BRANCH_ID");
                department_id = rs.getString("DEPT_ID");
                category_id = rs.getString("CATEGORY_ID");
                sub_category_id = rs.getString("SUB_CATEGORY_ID");
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                depreciation_end_date = dbConnection.formatDate(rs.getDate(
                        "DEP_END_DATE"));
                posting_date = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                make = rs.getString("ASSET_MAKE");
                location = rs.getString("LOCATION");
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                accum_dep = rs.getDouble("ACCUM_DEP");
                authorized_by = rs.getString("AUTHORIZED_BY");
                supplied_by = rs.getString("SUPPLIER_NAME");
                reason = rs.getString("PURCHASE_REASON");
                asset_id = rs.getString("ASSET_ID");
                description = rs.getString("DESCRIPTION");
                vendor_account = rs.getString("VENDOR_AC");
                cost_price = rs.getString("COST_PRICE");
                nbv = rs.getString("NBV");
                monthlydep = rs.getString("MONTHLY_DEP");
                vatable_cost = rs.getString("VATABLE_COST");
                vat_amount = rs.getString("VAT");
                serial_number = rs.getString("ASSET_SERIAL_NO");
                model = rs.getString("ASSET_MODEL");
                user = rs.getString("USER_ID");
                depreciation_rate = rs.getString("DEP_RATE");
                residual_value = rs.getString("RESIDUAL_VALUE");
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                date_of_purchase = dbConnection.formatDate(rs.getDate(
                        "DATE_PURCHASED"));
                who_to_remind = rs.getString("WHO_TO_REM");
                email_1 = rs.getString("EMAIL1");
                email2 = rs.getString("EMAIL2");
                raise_entry = rs.getString("RAISE_ENTRY");
                section = rs.getString("SECTION_ID");
                accum_dep = rs.getDouble("ACCUM_DEP");
                status = rs.getString("ASSET_STATUS");
                section_id = rs.getString("SECTION");
                wh_tax_cb = rs.getString("WH_TAX");
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                spare_1 = rs.getString("SPARE_1");
                spare_2 = rs.getString("SPARE_2");
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                who_to_remind_2 = rs.getString("WHO_TO_REM_2");
                driver = rs.getString("DRIVER");
                state = rs.getString("STATE");
                engine_number = rs.getString("ASSET_ENGINE_NO");
                multiple = rs.getString("MULTIPLE");
                posting_date = dbConnection.formatDate(rs.getDate(
                        "POSTING_DATE"));
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                noOfMonths = rs.getString("WAR_MONTH");
                expiryDate = dbConnection.formatDate(rs.getDate(
                        "WAR_EXPIRY_DATE"));
                authuser = rs.getString("ASSET_USER");
                amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
                amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
                partPAY = rs.getString("PART_PAY");
                fullyPAID =rs.getString("FULLY_PAID");
                group_id =rs.getString("GROUP_ID");
                bar_code = rs.getString("BAR_CODE");
                sbu_code = rs.getString("SBU_CODE");
                lpo = rs.getString("LPO");
                setSupervisor(rs.getString("SUPERVISOR"));
                deferPay = rs.getString("defer_pay");
                selectTax = String.valueOf(rs.getDouble("wht_percent"));
                this.section_id = rs.getString("SECTION_ID");
                assetCode = rs.getInt("asset_code");
                memo = rs.getString("memo");
                memoValue = rs.getString("memoValue");
                improveCost = rs.getString("IMPROV_COST");
                improveNbv = rs.getString("IMPROV_NBV");
                improveAccumdep = rs.getString("IMPROV_ACCUMDEP");
                improveMonthlydep = rs.getString("IMPROV_MONTHLYDEP");
                improveTotalNbv = rs.getString("TOTAL_NBV");
                warehouseCode = rs.getString("WAREHOUSE_CODE");
                itemCode = rs.getString("ITEM_CODE");
                itemType = rs.getString("ITEMTYPE");
                projectCode = rs.getString("PROJECT_CODE");
                regionCode = rs.getString("REGION_CODE");
                
            } else {
                System.out.print("nothing");
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset2 ->" + ex);
        } 
    }
}

/**
 * *Bulk asset2 Transfer
 */
public String bulkAsset2TransferUpdate(java.util.ArrayList list) {
 String query = "update am_asset2transfer SET  " +
				 "approval_Status=? " +
				 "WHERE transfer_id=? ";
System.out.println("bulkAsset2TransferUpdate query: "+query);
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
String newmtid = "";
int assetcode = 0;
int[] d = null;
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        newmtid = transactId+String.valueOf(assetcode);
  //      System.out.println("bulkAssetTransferUpdate newmtid: "+newmtid+"   MTID: "+mtid);
        ps.setString(1, newmtid);
        ps.setString(2, "ACTIVE");        
        ps.addBatch();
}
	d = ps.executeBatch();
	

} catch (Exception ex) {
	System.out.println("Error Updating asset2 for Transfer ->" + ex);
} 
mtidassetcode =mtid+String.valueOf(assetcode);
return mtidassetcode;

}

/**
    * *Bulk asset2 update
    */
   public boolean bulkAsset2Update(java.util.ArrayList list) {

    String query = "update am_asset2 SET  " +
                      "REGISTRATION_NO=?,Description=?,VENDOR_AC=?," +
                      "ASSET_MODEL=?,ASSET_SERIAL_NO=?," +
                      "ASSET_ENGINE_NO=?,SUPPLIER_NAME=?," +
                      "ASSET_USER=?,ASSET_MAINTENANCE=?," +
                      "AUTHORIZED_BY=?," +
                      "PURCHASE_REASON=?,SBU_CODE=?," +
                      "SPARE_1=?,SPARE_2=?,SPARE_3=?,SPARE_4=?,SPARE_5=?,SPARE_6=?," +
                      "BAR_CODE=?,SBU_CATEGORY_ID =?,SBU_CATEGORY_CODE =? " +
                      "WHERE ASSET_ID=? ";


	
	magma.AssetRecordsBean bd = null;
	int[] d = null;
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);

		for (int i = 0; i < list.size(); i++) {
			bd = (magma.AssetRecordsBean) list.get(i);

           String registration = bd.getRegistration_no();
           String description = bd.getDescription();
           String vendoracc = bd.getVendor_account();
           String model1 = bd.getModel();
           String serial = bd.getSerial_number();
           String engine = bd.getEngine_number();
           String suppliedby = bd.getSupplied_by();
           String assetuser = bd.getUser();
           Integer maintained = Integer.parseInt(bd.getMaintained_by());
           String authorized = bd.getAuthorized_by();
           String reason1 = bd.getReason();
           String sbu = bd.getSbu_code();
           String spare1 = bd.getSpare_1();
           String spare2 = bd.getSpare_2();
           String spare3 = bd.getSpare_3();
           String spare4 = bd.getSpare_4();
           String spare5 = bd.getSpare_5();
           String spare6 = bd.getSpare_6();
           String barcode = bd.getBar_code();
           String lpo1 = bd.getLpo();
           String asset_id1 = bd.getAsset_id();
           String subcatid = bd.getSub_category_id();
           String subcatQuery = "SELECT sub_category_code FROM   am_ad_sub_category WHERE sub_category_id = '"+subcatid+"'";
           String subcatcode = approvalRec.getCodeName(subcatQuery);
//System.out.println("subcatcode in bulkAssetUpdate: "+subcatcode);
           //String subcatcode = bd.getSubcatCode();

           if (registration == null || registration.equals("")) {
               registration = "0";
           }
           if (description == null || description.equals("")) {
           	description = "";
           }
           if (vendoracc == null || vendoracc.equals("")) {
               vendoracc = "0";
           }
           if (subcatid == null || subcatid.equals("")) {
           	subcatid = "0";
           }
           if (subcatcode == null || subcatcode.equals("")) {
           	subcatcode = "0";
           }            
           if (model1 == null || model1.equals("")) {
               model1 = "0";
           }
           
           if (serial == null || serial.equals("")) {
               serial = "0";
           }
           if (engine == null || engine.equals("")) {
               engine = "0";
           }
           if (suppliedby == null || suppliedby.equals("")) {
               suppliedby = "0";
           }
           if (assetuser == null || assetuser.equals("")) {
               assetuser = "0";
           }
           if (maintained == null) {
               maintained = 0;
           }
           if (authorized == null || authorized.equals("")) {
               authorized = "0";
           }
           if (reason1 == null || reason1.equals("")) {
               reason1 = "0";
           }
           if (sbu == null || sbu.equals("")) {
           	sbu = "0";
           }
           if (spare1 == null || spare1.equals("")) {
               spare1 = "0";
           }
           if (spare2 == null || spare2.equals("")) {
               spare2 = "0";
           }
           if (spare3 == null || spare3.equals("")) {
               spare3 = "0";
           }
           if (spare4 == null || spare4.equals("")) {
               spare4 = "0";
           }
           if (spare5 == null || spare5.equals("")) {
               spare5 = "0";
           }
           if (spare6 == null || spare6.equals("")) {
               spare6 = "0";
           }            
          if (asset_id1 == null || asset_id1.equals("")) {
               asset_id1 = "0";
           }
           if (barcode == null || barcode.equals("")) {
               barcode = "0";
           }
           if (lpo1 == null || lpo1.equals("")) {
           lpo1 = "0";
           }
//           System.out.println("<<<<<<<<<>>>>>>>>>>>> ");
           ps.setString(1, registration);
           ps.setString(2, description.toUpperCase());
           ps.setString(3, vendoracc);
           ps.setString(4, model1);
           ps.setString(5, serial);
           ps.setString(6, engine);
           ps.setString(7, suppliedby);
           ps.setString(8, assetuser);
//           System.out.println("<<<<maintained: "+maintained);
           ps.setInt(9, maintained);
           ps.setString(10, authorized);
           ps.setString(11, reason1);
           ps.setString(12, sbu);
           ps.setString(13, spare1);
           ps.setString(14, spare2 );
           ps.setString(15, spare3);
           ps.setString(16, spare4 );
           ps.setString(17, spare5);
           ps.setString(18, spare6 );
           ps.setString(19, barcode);
           ps.setString(20, subcatid);
           ps.setString(21, subcatcode);
           ps.setString(22, asset_id1);
//           System.out.println("<<<<asset_id1: "+asset_id1);
           ps.addBatch();
	}
		d = ps.executeBatch();
		//System.out.println("Executed Successfully ");


	} catch (Exception ex) {
		System.out.println("Error Updating all asset2 ->" + ex);
	} 

	return (d.length > 0);
    
} 
  
    
   public String setPendingTrans2(String[] a, String code,int assetCode,String transType){

   String mtid_id ="";
       int transaction_level=0; 
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
        "transaction_level,asset_code,trans_type,VATABLE_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
       
       try
       {

 		  Connection con = dbConnection.getConnection("legendPlus");
 	        PreparedStatement ps = con.prepareStatement(tranLevelQuery);
 	       ResultSet rs = ps.executeQuery();
           while(rs.next()){
           transaction_level = rs.getInt(1);

           }//if
           ps = con.prepareStatement(pq);
           SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

           String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
           ps.setString(1, (a[0]==null)?"":a[0]);
           ps.setString(2, (a[1]==null)?"":a[1]);
           ps.setString(3, (a[2]==null)?"":a[2]);
           ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
           //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
           ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
           ps.setString(6, (a[5]==null)?"":a[5]);
           ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
           ps.setString(8, (a[7]==null)?"":a[7]);
           ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
           ps.setString(10, (a[9]==null)?"":a[9]); 
           ps.setString(11, a[10]);
           ps.setString(12, timer.format(new java.util.Date()));
           ps.setString(13,mtid);
           ps.setString(14, mtid);
           ps.setInt(15, transaction_level);
           ps.setInt(16,assetCode);
           ps.setString(17, transType);
           ps.setDouble(18, (a[11]==null)?0:Double.parseDouble(a[11]));
           ps.execute();

           mtid_id = mtid;
       }
       catch(Exception er)
       {
           System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans2 with Four Parameters>>>>>>" + er);

       }
       
       return mtid_id;

   }

   
 	public String checkStockLedgerAccount(String branch)
 	{
 		String assetledgeraccount="";
// 		System.out.println("category "+category);
// 		System.out.println("branch "+branch);
// 		String query=" select distinct c.iso_code,"
// 					+" (select accronym from am_ad_ledger_type where series = substring(e.Asset_Ledger,1,1)),"
// 					+" b.default_branch,"
// 					+" e.Asset_Ledger,"
// 					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(e.Asset_Ledger,1,1))+"
// 					+" b.default_branch + e.Asset_Ledger asd"
// 					+" from am_ad_category a,am_ad_branch d,"
// 					+" AM_GB_CURRENCY_CODE c, am_gb_company b, ST_STOCK_CATEGORY e "
// 					+" where a.currency_id = c.currency_id"
// 					+" and d.branch_code = '"+branch+"'";
//// 		System.out.println("query in checkStockLedgerAccount >>>> " + query);

	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS5'");
	     String query = script+" and d.branch_code = '"+branch+"'";
	     
 		

 		try {


 			  Connection con = dbConnection.getConnection("legendPlus");
 		        PreparedStatement ps  = con.prepareStatement(query);
 			ResultSet rs = ps.executeQuery();

 			if (rs.next())
 			 { 

 				 assetledgeraccount  = rs.getString("asd");


 			 }
 		   }  
 			catch (Exception er)
 			{
 			 er.printStackTrace();

 			}
 			
 	return 	assetledgeraccount;
 	}


	public String witholdingforStockFedTaxCr (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select distinct c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//					+" b.default_branch,"
//					+" b.fed_wht_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1))+"
//					+" b.default_branch +	b.fed_wht_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and d.branch_code = '"+branch+"'";
////		System.out.println("query in witholdingforStockFedTaxCr >>>> " + query);
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'WHTSTKCR'");
		String query = script+" and d.branch_code = '"+branch+"'";
		
		

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps  = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	assetAcqusitionSuspense;
	}

/*
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

*/

	   
 	public String witholdingFedTaxCrforENV(String branch)
 	{
 		String assetledgeraccount="";
// 		System.out.println("category "+category);
// 		System.out.println("branch "+branch);
// 		String query=" select distinct c.iso_code,"
// 					+" (select accronym from am_ad_ledger_type where series = substring(e.GL_ACCOUNT,1,1)),"
// 					+" b.default_branch,"
// 					+" e.GL_ACCOUNT,"
// 					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(e.GL_ACCOUNT,1,1))+"
// 					+" b.default_branch + e.GL_ACCOUNT asd"
// 					+" from am_ad_category a,am_ad_branch d,"
// 					+" AM_GB_CURRENCY_CODE c, am_gb_company b, FM_SOCIAL_TYPE e "
// 					+" where c.currency_id = 1"
// 					+" and d.branch_code = '"+branch+"'";
//// 		System.out.println("query in witholdingFedTaxCrforENV >>>> " + query);
 		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'FWTACRENV'");
 		String query = script+" and d.branch_code = '"+branch+"'";
 		

 		try {


 			  Connection con = dbConnection.getConnection("legendPlus");
 		        PreparedStatement ps  = con.prepareStatement(query);
 			ResultSet rs = ps.executeQuery();

 			if (rs.next())
 			 { 

 				 assetledgeraccount  = rs.getString("asd");


 			 }
 		   }  
 			catch (Exception er)
 			{
 			 er.printStackTrace();

 			}
 			
 	return 	assetledgeraccount;
 	}

	public String checkENVDRLedgerAccount (String tranType,String branchCode)
	{
		String costDrAcct="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select distinct c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(fm.GL_ACCOUNT,1,1)),"
//					+" b.default_branch,"
//					+" fm.GL_ACCOUNT,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(fm.GL_ACCOUNT,1,1))+"
//					+" b.default_branch +	fm.GL_ACCOUNT asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b,FM_SOCIAL_TYPE fm"
//					+" where c.currency_id = 1"
//					+" and fm.TYPE_CODE = '"+tranType+"'"
//					+" and d.branch_code = '"+branchCode+"'";
////		System.out.println("query in checkENVDRLedgerAccount >>>> " + query);
		
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVDRLDGR'");
		String query = script+" and fm.TYPE_CODE = '"+tranType+"' and d.branch_code = '"+branchCode+"'";
		

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps  = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				costDrAcct  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	costDrAcct;
	}



    public void setPendingTrans(String assetId,String userId,String superId,String value,String description,String branchCode,String status,String tranType,String processStatus, String code){
//    	System.out.println("====code 0====> "+code);
        int transaction_level=0;
       
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level,Transaction_type from approval_level_setup where code ='"+code+"'";
       
        try
        {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
            ResultSet rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);
 
       
            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (assetId==null)?"":assetId);
            ps.setString(2, (userId==null)?"":userId);
            ps.setString(3, (superId==null)?"":superId);
            ps.setDouble(4, (value==null)?0:Double.parseDouble(value));
            ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(6, (description==null)?"":description);
            ps.setTimestamp(7,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(8, (branchCode==null)?"":branchCode);
            ps.setString(9, (status==null)?"":status); //asset_status
            ps.setString(10, (tranType==null)?"":tranType);
            ps.setString(11, processStatus);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13, mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);
            
            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans>>>>>>" + er);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";






    }//staticApprovalInfo()

    public void setPendingTrans(String assetId,String userId,String superId,String description,String branchCode,String status,String tranType,String processStatus, String code){
        int transaction_level=0;
        
 String pq = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
         "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
         "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level,Transaction_type from approval_level_setup where code ='"+code+"'";
       
        try
        {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps = con.prepareStatement(tranLevelQuery);
            ResultSet rs = ps.executeQuery();


            while(rs.next()){
            transaction_level = rs.getInt(1);

            }//if



            ////////////To set values for approval table

            ps = con.prepareStatement(pq);
 
       
            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
            ps.setString(1, (assetId==null)?"":assetId);
            ps.setString(2, (userId==null)?"":userId);
            ps.setString(3, (superId==null)?"":superId);
            ps.setTimestamp(4,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(5, (description==null)?"":description);
            ps.setTimestamp(6,dbConnection.getDateTime(new java.util.Date()));
            ps.setString(7, (branchCode==null)?"":branchCode);
            ps.setString(8, (status==null)?"":status); //asset_status
            ps.setString(9, (tranType==null)?"":tranType);
            ps.setString(10, processStatus);
            ps.setString(11, timer.format(new java.util.Date()));
            ps.setString(12, mtid);
            ps.setString(13, mtid);
            ps.setInt(14, transaction_level);
            
            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTrans>>>>>>" + er);

        }

   //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

    }//staticApprovalInfo()


public java.util.ArrayList gettransactionRecords(String tranId)
 {
 	java.util.ArrayList list = new java.util.ArrayList();

 	String durationquery = "select transactionId,iso,asset_id,page1, *from am_Raisentry_Transaction where trans_id=? ";

	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(durationquery);
	        ps.setString(1, tranId);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {
				String transId = rs.getString("transactionId");
				String iso = rs.getString("iso");
				String assetId = rs.getString("asset_id");
				String page1 = rs.getString("page1");
				Transaction trans = new Transaction();
				trans.setTransId(transId);
				trans.setCode(iso);
				trans.setAssetId(assetId);
				trans.setTranType(page1);
				list.add(page1);
			   }
	 }
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
 	return list;
 }

public boolean setUpdateAssetBidUploadTrans(String groupid,String userid){
   
    boolean done;
    done = false;    	    
    String UpadteQuerry = "update am_group_asset_Bid set process_flag = 'Y' where  GROUP_ID=? AND USER_ID=? ";
    try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(UpadteQuerry);
	        ps.setString(1, groupid);
	        ps.setString(2, userid);
        done = (ps.executeUpdate() != -1);

    } catch (Exception ex) {
        System.out.println("WARN: Error Updateing all Am asset Group ->" + ex);
    } 
    return done;
}      

public boolean setAssetBidUploadCompleteTrans(String AssetId){
    //	   System.out.println("Before Update Asset Id ====  "+AssetId);
    //	   System.out.println("Before Update Asset Code ====  "+AssetCode);
    	   
    	    boolean done;
    	    done = false;
    	    String UpadteQuerry = "UPDATE am_asset_bid set Asset_Status = 'ACTIVE' where ASSET_ID = ? ";
    	    try {

    			  Connection con = dbConnection.getConnection("legendPlus");
    		        PreparedStatement ps  = con.prepareStatement(UpadteQuerry);
    		        ps.setString(1, AssetId);
    	        done = (ps.executeUpdate() != -1);

    	    } catch (Exception ex) {
    	        System.out.println("WARN: Error fetching all asset ->" + ex);
    	    } 
    	    
    	    return done;
    	}

public void setPendingTransArchive(String assetId,String userId,String superId,String value, String description,String branchCode,String status,String tranType,String processStatus, String code,String assetCode,String mtid){

      int transaction_level=0;
      
String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
       "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
       "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
     
      try
      {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
           ResultSet rs = ps.executeQuery();


          while(rs.next()){
          transaction_level = rs.getInt(1);

          }//if



          ////////////To set values for approval table

          ps = con.prepareStatement(pq);


          SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
  //        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval_archive");
          ps.setString(1, (assetId==null)?"":assetId);
          ps.setString(2, (userId==null)?"":userId);
          ps.setString(3, (superId==null)?"":superId);
          ps.setDouble(4, (value==null)?0:Double.parseDouble(value));
          ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
          ps.setString(6, (description==null)?"":description);
          ps.setTimestamp(7,dbConnection.getDateTime(new java.util.Date()));
          ps.setString(8, (branchCode==null)?"":branchCode);
          ps.setString(9, (status==null)?"":status); //asset_status
          ps.setString(10, (tranType==null)?"":tranType);
          ps.setString(11, processStatus);
          ps.setString(12, timer.format(new java.util.Date()));
          ps.setString(13,mtid);
          ps.setString(14, mtid);
          ps.setInt(15, transaction_level);
          ps.setInt(16,Integer.parseInt(assetCode));
          ps.execute();

      }
      catch(Exception er)
      {
          System.out.println(">>> 3 AssetRecordBeans:setPendingTransArchive()>>>>>>" + er);

      }
 //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
  }//staticApprovalInfo()

public String[] setApprovalStockDataGroup(long id){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
    
        
        //int groupId = Integer.parseInt(id);
         String query ="select group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
         		"		description,effective_date,BRANCH_CODE," +
         				"Asset_Status from AM_GROUP_STOCK_MAIN where group_id = ? " ;
 //        System.out.println("Query in setApprovalDataGroup : " + query);
//         String groupidQry = "select group_id from am_group_asset_main where group_id =" +id;
//         String Qrygroupid = approvalRec.getCodeName(groupidQry); 
//        		 System.out.println("Qrygroupid in setApprovalDataGroup : " + Qrygroupid);
        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query);
  	        ps.setLong(1, id);
           ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = String.valueOf(id);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = dbConnection.formatDate(rs.getDate(5));
//               System.out.println("Before Conversion" + rs.getDate(5));
//               System.out.println("Posting_Date in setApprovalDataGroup : " +  result[4]);
               result[5] = rs.getString(6);
               result[6] = dbConnection.formatDate(rs.getDate(7));
 //              System.out.println("Effective_Date in setApprovalDataGroup : " +  result[6]);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }
//            System.out.println("Final Conversion");
        } catch (Exception ex) {
            System.out.println("AssetRecordsBean : setApprovalData()WARN: Error setting approval data for group asset creation ->" + ex);
        } 
        	result[9] = "Group Asset Creation";
        	result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


public String checkENVCRLedgerAccount (String tranType,String branchCode)
{
	String costDrAcct="";
	/*System.out.println("category "+category);
	System.out.println("branch "+branch);*/
//	String query=" select distinct c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(fm.CR_ACCOUNT,1,1)),"
//				+" b.default_branch,"
//				+" fm.CR_ACCOUNT,"
//				+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(fm.CR_ACCOUNT,1,1))+"
//				+" b.default_branch +	fm.CR_ACCOUNT asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b,FM_SOCIAL_TYPE fm"
//				+" where c.currency_id = 1"
//				+" and fm.TYPE_CODE = '"+tranType+"'"
//				+" and d.branch_code = '"+branchCode+"'";
////	System.out.println("query in checkENVCRLedgerAccount >>>> " + query);
	String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVCRLDGR'");
	String query = script+" and fm.TYPE_CODE = '"+tranType+"' and d.branch_code = '"+branchCode+"'";
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			costDrAcct  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	costDrAcct;
}

//the methods below are to set the asset code in am_asset_approval and am_asset_approval_archive

public void setPendingTransFleetMaintenance(String[] a, String code,int assetCode){

    int transaction_level=0;

String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
   
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
         ResultSet rs = ps.executeQuery();


        while(rs.next())
        { 
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if  

//        System.out.println("a[6] in setPendingTrans: "+a[6]);

        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
        String dd = a[6].substring(0,2);
        String mm = a[6].substring(3,5);
        String yyyy = a[6].substring(6,10);
        String effDate = yyyy+"-"+mm+"-"+dd;
//        System.out.println("effDate in setPendingTrans: "+effDate);
        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
/*           
        String pq1 = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
                "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                "transaction_level,asset_code) values('"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+a[5]+"',"
                		+ "'"+effDate+"','"+a[7]+"','"+a[8]+"','"+a[9]+"','"+a[10]+"','"+timer.format(new java.util.Date())+"',"
                				+ "'"+mtid+"','"+mtid+"','"+transaction_level+"','"+assetCode+"')";
        System.out.println("pq1 In setPendingTrans: "+pq1);
        */
        ps.setString(1, (a[0]==null)?"":a[0]);
        ps.setString(2, (a[1]==null)?"":a[1]);
        ps.setString(3, (a[2]==null)?"":a[2]);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
        ps.setString(6, (a[5]==null)?"":a[5]);
//        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
        ps.setString(7,a[6]);
        ps.setString(8, (a[7]==null)?"":a[7]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
        ps.setString(10, (a[9]==null)?"":a[9]);
        ps.setString(11, a[10]);
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,mtid);
        ps.setString(14, mtid);
        ps.setInt(15, transaction_level);
        ps.setInt(16,assetCode);

        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTransFleetMaintenance Three 3>>>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()


public void setPendingTransArchiveFleetMaintenance(String[] a, String code,long mtid, int assetCode){

      int transaction_level=0;
      
String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
       "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
       "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
     
      try
      {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
           ResultSet  rs = ps.executeQuery();


          while(rs.next()){
          transaction_level = rs.getInt(1);

          }//if



          ////////////To set values for approval table

          ps = con.prepareStatement(pq);


          SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
          String dd = a[6].substring(0,2);
          String mm = a[6].substring(3,5);
          String yyyy = a[6].substring(6,10);
          String effDate = yyyy+"-"+mm+"-"+dd;
          
          //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
          ps.setString(1, (a[0]==null)?"":a[0]);
          ps.setString(2, (a[1]==null)?"":a[1]);
          ps.setString(3, (a[2]==null)?"":a[2]);
          ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
          //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
          ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
          ps.setString(6, (a[5]==null)?"":a[5]);
          ps.setString(7,a[6]);
          ps.setString(8, (a[7]==null)?"":a[7]);
          ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
          ps.setString(10, (a[9]==null)?"":a[9]);
          ps.setString(11, a[10]);
          ps.setString(12, timer.format(new java.util.Date()));
          ps.setLong(13,mtid);
          ps.setString(14, String.valueOf(mtid));
          ps.setInt(15, transaction_level);
          ps.setInt(16,assetCode);

          ps.execute();

      }
      catch(Exception er)
      {
          System.out.println(">>>AssetRecordBeans:setPendingTransArchiveFleetMaintenance()>>>>>>" + er);

      }
 //String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";
  }//staticApprovalInfo()

public String witholdingFedTaxCrFT (String category,String branch,String whtax)
{
	String assetAcqusitionSuspense="";
	String query = "";
	/*System.out.println("category "+category);
	System.out.println("branch "+branch);*/
	if(whtax.equalsIgnoreCase("S")){
//		String queryState=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" '"+branch+"' AS default_branch,"
//					+" b.wht_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1))+"
//					+" '"+branch+"' +	b.wht_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'SWHTCRFT'");
		query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	}
		if(whtax.equalsIgnoreCase("F")){
//		String queryFederal=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//			+" '"+branch+"' AS default_branch,"
//			+" b.fed_wht_account,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1))+"
//			+" '"+branch+"' +	b.fed_wht_account asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'FWHTCRFT'");
		query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in witholdingFedTaxCr >>>> " + query);
		
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}	

public String checkAccounAcqusitionSuspenseParaBranch (String category,String branch)
{
String assetAcqusitionSuspense="";
//System.out.println("category "+category);
//System.out.println("branch "+branch);
//String query=" select c.iso_code,(select accronym from am_ad_ledger_type"
//			+" where series = substring(a.Asset_Ledger,1,1)), b.default_branch,a.Asset_Ledger, "
//			+" substring(d.BRANCH_CODE,3,3)+c.iso_code +"
//			+" a.Asset_Ledger asd "
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "
//			+" where a.currency_id = c.currency_id and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";

String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";



try {


	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ResultSet rs = ps.executeQuery();

	if (rs.next())
	 {

		assetAcqusitionSuspense  = rs.getString("asd");


	 }
   }
	catch (Exception er)
	{
	 er.printStackTrace();

	}
	
return 	assetAcqusitionSuspense;
}

public String witholdingFedTaxCrPara (String category,String branch,String whtax)
{
	String assetAcqusitionSuspense="";
	String query = "";
	/*System.out.println("category "+category);
	System.out.println("branch "+branch);*/
	if(whtax.equalsIgnoreCase("S")){
//		String queryState=" select c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//				+" b.default_branch,"
//				+" b.fed_wht_account,"
//				+" substring(d.BRANCH_CODE,3,3)+c.iso_code+"
//				+" b.fed_wht_account asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//				+" where a.currency_id = c.currency_id"
//				+" and a.category_code = '"+category+"'"
//				+" and d.branch_code = '"+branch+"'";
//		query= queryState;
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS4'");
	     query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	}
		if(whtax.equalsIgnoreCase("F")){
//		String queryFederal=" select c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//				+" b.default_branch,"
//				+" b.fed_wht_account,"
//				+" substring(d.BRANCH_CODE,3,3)+c.iso_code+"
//				+" b.fed_wht_account asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//				+" where a.currency_id = c.currency_id"
//				+" and a.category_code = '"+category+"'"
//				+" and d.branch_code = '"+branch+"'";	
//		query= queryFederal;
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS5'");
	     query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in witholdingFedTaxCr >>>> " + query);
	

	try {

		
		  Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps  = con.prepareStatement(query);

		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}	


public void setPendingTransAdmin(String[] a, String code,int assetCode,String recType){

    int transaction_level=0;
   
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code,RecordType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("PQ in setPendingTransAdmin: "+pq);
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
    
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
         ResultSet rs = ps.executeQuery();


        while(rs.next())
        {
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if

//        System.out.println("a[6] in setPendingTransAdmin: "+a[6]);

        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
        String dd = a[6].substring(0,2);
        String mm = a[6].substring(3,5);
        String yyyy = a[6].substring(6,10);
        String effDate = yyyy+"-"+mm+"-"+dd;
//        System.out.println("effDate in setPendingTransAdmin: "+effDate);
        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
/*           
        String pq1 = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
                "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                "transaction_level,asset_code) values('"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+a[5]+"',"
                		+ "'"+effDate+"','"+a[7]+"','"+a[8]+"','"+a[9]+"','"+a[10]+"','"+timer.format(new java.util.Date())+"',"
                				+ "'"+mtid+"','"+mtid+"','"+transaction_level+"','"+assetCode+"')";
        System.out.println("pq1 In setPendingTrans: "+pq1);
        */
        ps.setString(1, (a[0]==null)?"":a[0]);
        ps.setString(2, (a[1]==null)?"":a[1]);
        ps.setString(3, (a[2]==null)?"":a[2]);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
        ps.setString(6, (a[5]==null)?"":a[5]);
        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//        ps.setString(7,effDate);
        ps.setString(8, (a[7]==null)?"":a[7]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
        ps.setString(10, (a[9]==null)?"":a[9]);
        ps.setString(11, a[10]);
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,mtid);
        ps.setString(14, mtid);
        ps.setInt(15, Integer.parseInt(a[11]));
        ps.setInt(16,assetCode);
        ps.setString(17, recType);
        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTransAdmin >>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()


public void setPendingTransAdminMultiApp(String[] a, String code,int assetCode,String recType,String supervisorId,String mtid){

    int transaction_level=0;
   
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code,RecordType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("PQ in setPendingTransAdmin: "+pq);
String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
   
    try
    {  

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
        ResultSet rs = ps.executeQuery();


        while(rs.next())
        {
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if

//        System.out.println("a[6] in setPendingTransAdmin: "+a[6]);

        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
//        String dd = a[6].substring(0,2);
//        String mm = a[6].substring(3,5);
//        String yyyy = a[6].substring(6,10);
//        String effDate = yyyy+"-"+mm+"-"+dd;
//        System.out.println("effDate in setPendingTransAdmin: "+effDate);
//        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
        ps.setString(1, (a[0]==null)?"":a[0]);
        ps.setString(2, (a[1]==null)?"":a[1]);
        ps.setString(3, supervisorId);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
        ps.setString(6, (a[5]==null)?"":a[5]);
        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//        ps.setString(7,effDate);
        ps.setString(8, (a[7]==null)?"":a[7]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
        ps.setString(10, (a[9]==null)?"":a[9]);
        ps.setString(11, a[10]);
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,mtid);
        ps.setString(14, mtid);
        ps.setInt(15, Integer.parseInt(a[11]));
        ps.setInt(16,assetCode);
        ps.setString(17, recType);
        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTransAdminMultiApp >>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()

public String vatAcctCr (String category,String branch,String vat)
{
	String vatAccount="";
	String query = "";
	if(vat.equalsIgnoreCase("Y")){
//		String queryState=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1)),"
//					+" b.default_branch,"
//					+" b.Vat_Account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1))+"
//					+" b.default_branch +	b.Vat_Account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATACTCRYES'");
		query = script+" and a.category_code = ? and d.branch_code = ?";
//		query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
//		System.out.println("query in vatAcctCr Yes>>>> " + query);
	} 
		if(vat.equalsIgnoreCase("N")){
//		String queryFederal=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1)),"
//			+" b.default_branch,"
//			+" b.SelfChargeVAT,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1))+"
//			+" b.default_branch +	b.SelfChargeVAT asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATACTCRNO'");
		query = script+" and a.category_code = ? and d.branch_code = ?";
//		System.out.println("query in vatAcctCr No >>>> " + query);
		}
			
//	System.out.println("query in vatAcctCr Final>>>> " + query);
	

	try {
 

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query);
		  ps.setString(1, category);
		  ps.setString(2, branch);
		  
//		 ps = con.prepareStatement(query);

		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			vatAccount  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	vatAccount;
}	

public String checkAssetLedgerAccount2(String category,String branch)
{
	String assetledgeraccount="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);
//	String query=" select c.iso_code,(select accronym from am_ad_ledger_type"
//			+" where series = substring(b.asset_acq_ac,1,1)), b.default_branch,a.Asset_Ledger, "
//			+" substring(d.BRANCH_CODE,3,3)+c.iso_code +"
//			+" a.Asset_Ledger asd "
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "
//			+" where a.currency_id = c.currency_id "
//			+ "and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";
//	//System.out.println("query in checkAssetLedgerAccount >>>> " + query);
	String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ASLDGRACT2'");
	String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	
	

	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			 assetledgeraccount  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetledgeraccount;
}

public String vatAcctCr2 (String category,String branch,String vat)
{
	String vatAccount="";
	String query = "";
	if(vat.equalsIgnoreCase("Y")){
//		String queryState=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1)),"
//					+" b.default_branch,"
//					+" b.Vat_Account,"
//					+" substring(d.BRANCH_CODE,3,3)+c.iso_code+	b.Vat_Account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATACTCRYES'");
		query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	} 
		if(vat.equalsIgnoreCase("N")){
//		String queryFederal=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1)),"
//			+" b.default_branch,"
//			+" b.SelfChargeVAT,"
//			+" substring(d.BRANCH_CODE,3,3)+c.iso_code +	b.SelfChargeVAT asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'VATACTCRYES'");
		query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in vatAcctCr >>>> " + query);
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			vatAccount  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	vatAccount;
}	

public String checkAccounAcqusitionSuspenseParaBranch2 (String category,String branch)
{
	String assetAcqusitionSuspense="";
//	System.out.println("category "+category);
//	System.out.println("branch "+branch);



//	String query=" select c.iso_code, (select accronym from am_ad_ledger_type where series = substring(d.Uncapitalized_account,1,1)), "+
//	" d.branch_code, "+
//	" d.Uncapitalized_account, "+
//	" substring(d.BRANCH_CODE,3,3)+c.iso_code + "+
//	" d.Uncapitalized_account asd "+
//	" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b "+
//	" where a.currency_id = c.currency_id "+
//	" and a.category_code = '"+category+"' "+
//	" and d.branch_code = '"+branch+"' "; 
//
////	System.out.println("query in checkAccounAcqusitionSuspenseBranch2: "+query);
    String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS4'");
    String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}


public String assetProcurementParaDr (String category,String branch)
{
String assetAcqusitionSuspense="";
/*System.out.println("category "+category);
System.out.println("branch "+branch);*/
//String query=" select c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//			+" d.branch_code,"
//			+" a.Asset_Ledger,"
//			+" substring(d.BRANCH_CODE,3,3)+c.iso_code +	a.Asset_Ledger asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and a.category_code = '"+category+"'"
//			+" and d.branch_code = '"+branch+"'";
////System.out.println("query in assetProcurementDr >>>> " + query);
String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS3'");
String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";


try {


	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query);
      ResultSet rs = ps.executeQuery();

	if (rs.next())
	 {

		assetAcqusitionSuspense  = rs.getString("asd");


	 }
   }
	catch (Exception er)
	{
	 er.printStackTrace();

	}
	
return 	assetAcqusitionSuspense;
}

	public String checkAssetLedgerParaAccount(String category,String branch)
	{
		String assetledgeraccount="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1)),"
//					+" b.default_branch,"
//					+" b.asset_acq_ac,"
//					+" substring(d.BRANCH_CODE,3,3)+c.iso_code + b.asset_acq_ac asd"
//					+" from am_ad_category a,am_ad_branch d, "
//					+" AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in checkAssetLedgerAccount >>>> " + query);
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps = con.prepareStatement(query);
		        ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				 assetledgeraccount  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	assetledgeraccount;
	}

    
	public String checkAccounAcqusitionParaSuspense (String category,String branch)
	{
		String assetAcqusitionSuspense="";
		/*System.out.println("category "+category);
		System.out.println("branch "+branch);*/
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(a.Asset_Ledger,1,1)),"
//					+" b.default_branch,"
//					+" a.Asset_Ledger,"
//					+" substring(d.BRANCH_CODE,3,3)+c.iso_code +	a.Asset_Ledger asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in checkAccounAcqusitionSuspense >>>> " + query);
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'AS4'");
	     String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps = con.prepareStatement(query);
		        ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				assetAcqusitionSuspense  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	assetAcqusitionSuspense;
	}

	public String checkAssetLedgerAccountReformat(String category,String branch)
	{
		String assetledgeraccount="";
//		System.out.println("category "+category);
//		System.out.println("branch "+branch);
//		String query=" select c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.asset_acq_ac,1,1)),"
//					+" b.default_branch,"
//					+" b.asset_acq_ac,"
//					+" substring(d.BRANCH_CODE,3,3)+c.iso_code +	b.asset_acq_ac asd"
//					+" from am_ad_category a,am_ad_branch d, "
//					+" AM_GB_CURRENCY_CODE c, am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and a.category_code = '"+category+"'"
//					+" and d.branch_code = '"+branch+"'";
//		//System.out.println("query in checkAssetLedgerAccount >>>> " + query);
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LDGRACTREFORM'");
		String query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		

		try {


			  Connection con = dbConnection.getConnection("legendPlus");
		        PreparedStatement ps = con.prepareStatement(query);
		        ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				 assetledgeraccount  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	assetledgeraccount;
	}

public String[] setApprovalDataTranTypeFT(String id,String asset_id,String tranType, String supervisor,String userId,String amount,String tableType){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
 //String currentDate  = reArrangeDate(getCurrentDate1());
 //  System.out.println("the $$$$$$$$$$$ id: "+id+"  asset_id: "+asset_id+"   tranType: "+tranType+"  supervisor: "+supervisor+"   userId: "+userId+"  amount: "+amount);
    String[] result= new String[12];
    
        String query = "";
          query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from am_asset where asset_id ='" +asset_id+"'";

          if(tableType.equals("ASSET2")){query ="select asset_id,user_ID,supervisor,Cost_Price,Posting_Date," +
                 " description,effective_date,BRANCH_CODE,Asset_Status from am_asset2 where asset_id ='" +asset_id+"'";}

//          System.out.println("the $$$$$$$$$$$ query: "+query+"    tableType: "+tableType);
        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(query);
           ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //
            	result[0] = rs.getString(1);
                // result[1]= rs.getString(2);
               // result[2] = rs.getString(3);
               result[0] = id;
               result[1] = userId;
               result[2] = supervisor;
               result[3] = amount;
             //  result[3] = rs.getString(4);
               result[4] = rs.getString(5);
               result[5] = rs.getString(6);
               result[6] = rs.getString(7);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching setApprovalDataTranType ->" + ex);
        } 
        
result[9] = tranType;
result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()


public String ENVvatAcctCr (String category,String branch,String vat)
{
	String vatAccount="";
	String query = "";
	if(vat.equalsIgnoreCase("Y")){
//		String queryState=" select Distinct c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1)),"
//					+" b.default_branch,"
//					+" b.Vat_Account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1))+"
//					+" b.default_branch +	b.Vat_Account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVVATACTCRYES'");
		query = script+" and d.branch_code = '"+branch+"'";
	} 
		if(vat.equalsIgnoreCase("N")){
//		String queryFederal=" select Distinct c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1)),"
//			+" b.default_branch,"
//			+" b.SelfChargeVAT,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1))+"
//			+" b.default_branch +	b.SelfChargeVAT asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVVATACTCRNO'");
		query = script+" and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in vatAcctCr >>>> " + query);
	
	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			vatAccount  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	vatAccount;
}	

public String ENVvatAcctCr2 (String category,String branch,String vat)
{
	String vatAccount="";
	String query = "";
	if(vat.equalsIgnoreCase("Y")){
//		String queryState=" select Distinct c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.Vat_Account,1,1)),"
//					+" b.default_branch,"
//					+" b.Vat_Account,"
//					+" substring(d.BRANCH_CODE,3,3)+c.iso_code+	b.Vat_Account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVVATACTCRYES'");
		query = script+" and d.branch_code = '"+branch+"'";
	} 
		if(vat.equalsIgnoreCase("N")){
//		String queryFederal=" select Distinct c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.SelfChargeVAT,1,1)),"
//			+" b.default_branch,"
//			+" b.SelfChargeVAT,"
//			+" substring(d.BRANCH_CODE,3,3)+c.iso_code +	b.SelfChargeVAT asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'ENVVATACTCRNO'");
		query = script+" and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in vatAcctCr >>>> " + query);
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			vatAccount  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	vatAccount;
}	

public String ENVwitholdingFedTaxCr (String category,String branch,String whtax)
{
	String assetAcqusitionSuspense="";
	String query = "";
	/*System.out.println("category "+category);
	System.out.println("branch "+branch);*/
//	System.out.println("whtax in =====: "+whtax);
	if(whtax.equalsIgnoreCase("S")){
//		String queryState=" select distinct c.iso_code,"
//					+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//					+" b.default_branch,"
//					+" b.wht_account,"
//					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1))+"
//					+" b.default_branch +	b.wht_account asd"
//					+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//					+" where a.currency_id = c.currency_id"
//					+" and d.branch_code = '"+branch+"'";
//		query= queryState;  
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS4'");
	      query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
	}
		if(whtax.equalsIgnoreCase("F")){
//		String queryFederal=" select distinct c.iso_code,"
//			+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//			+" b.default_branch,"
//			+" b.fed_wht_account,"
//			+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1))+"
//			+" b.default_branch +	b.fed_wht_account asd"
//			+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//			+" where a.currency_id = c.currency_id"
//			+" and d.branch_code = '"+branch+"'";		
//		query= queryFederal;
		
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS5'");
	      query = script+" and a.category_code = '"+category+"' and d.branch_code = '"+branch+"'";
		}

	     
//	System.out.println("query in witholdingFedTaxCr >>>> " + query);
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}	

public String ENVwitholdingFedTaxCrPara (String category,String branch,String whtax)
{
	String assetAcqusitionSuspense="";
	String query = "";
	/*System.out.println("category "+category);
	System.out.println("branch "+branch);*/
	if(whtax.equalsIgnoreCase("S")){
//		String queryState=" select distinct c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.wht_account,1,1)),"
//				+" b.default_branch,"
//				+" b.fed_wht_account,"
//				+" substring(d.BRANCH_CODE,3,3)+c.iso_code+"
//				+" b.fed_wht_account asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//				+" where a.currency_id = c.currency_id"
//				+" and d.branch_code = '"+branch+"'";
//		query= queryState;
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS4'");
	      query = script+" and d.branch_code = '"+branch+"'";
	}
		if(whtax.equalsIgnoreCase("F")){
//		String queryFederal=" select distinct c.iso_code,"
//				+" (select accronym from am_ad_ledger_type where series = substring(b.fed_wht_account,1,1)),"
//				+" b.default_branch,"
//				+" b.fed_wht_account,"
//				+" substring(d.BRANCH_CODE,3,3)+c.iso_code+"
//				+" b.fed_wht_account asd"
//				+" from am_ad_category a,am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b"
//				+" where a.currency_id = c.currency_id"
//				+" and d.branch_code = '"+branch+"'";	
//		query= queryFederal;
	     String script = approvalRec.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where type = 'LIS5'");
	      query = script+" and d.branch_code = '"+branch+"'";
		}
			
//	System.out.println("query in witholdingFedTaxCr >>>> " + query);
	

	try {


		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		 {

			assetAcqusitionSuspense  = rs.getString("asd");


		 }
	   }
		catch (Exception er)
		{
		 er.printStackTrace();

		}
		
return 	assetAcqusitionSuspense;
}	

public boolean setUpdateVerifyUploadTrans(String groupid,String userid){
   
    boolean done;
    done = false;    	    
    String UpadteQuerry = "update AM_GROUP_ASSET_VERIFICATION set process_flag = 'Y' where  GROUP_ID='"+groupid+"' AND USER_ID='"+userid+"'";
    try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(UpadteQuerry);
        done = (ps.executeUpdate() != -1);

    } catch (Exception ex) {
        System.out.println("WARN: Error Updateing all Am asset Verification Group ->" + ex);
    } 
    
    return done;
}      

public void updateAmAssetWithVerification(String batchId){
String query_r ="UPDATE a SET a.BAR_CODE = b.BAR_CODE,a.SPARE_1 = b.SPARE_1 "+
		"from  AM_ASSET a, AM_GROUP_ASSET_VERIFICATION b "+
		 " where a.ASSET_ID = b.ASSET_ID AND b.GROUP_ID = '"+batchId+"'";


try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query_r);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("AssetRecordBean: updateAmAssetWithVerification()>>>>>" + ex);
        } 

}

public void setVerifyUploadPendingTrans(int supervisor, String code,String AssetId,int AssetCode,int userid,double amount,String description,String branchcode,String Trantype){
//	System.out.println("setUploadPendingTrans supervisor===> "+supervisor);
//	System.out.println("setUploadPendingTrans AssetId===> "+AssetId);	
    int transaction_level=0;
   
	  	String qrylevel =" select level from approval_level_setup  where code='"+code+"'";
  	    transaction_level = Integer.parseInt(approvalRec.getCodeName(qrylevel));
//  	  System.out.println("setUploadPendingTrans transaction_level===> "+transaction_level);
SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"+
"effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
"transaction_level,Asset_code) values('"+AssetId+"',"+userid+",'"+supervisor+"','"+amount+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+description+"','"+dbConnection.getDateTime(new java.util.Date())+"',"+
"'"+branchcode+"','PENDING','"+Trantype+"','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"','"+AssetId+"','"+transaction_level+"','"+AssetCode+"')";
/*
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,"+
        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id,"+
        "transaction_level,Asset_code) select asset_id,user_ID,'"+supervisor+"',Cost_Price,'"+dbConnection.getDateTime(new java.util.Date())+"',description,effective_date,"+
        "BRANCH_CODE,'PENDING','Asset Upload','P','"+timer.format(new java.util.Date())+"','"+new ApplicationHelper().getGeneratedId("am_asset_approval")+"',Group_id,'"+transaction_level+"','"+AssetCode+"' from am_asset where Asset_Id = '"+AssetId+"'";
*/        
//System.out.println("setUploadPendingTrans Query pq===> "+pq);
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(pq);

        ps.execute();
        dbConnection.closeConnection(con, ps);
    }
    
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setVerifyUploadPendingTrans(>>>>>>" + er);

    }
}

public void updateAssetVerifyPendingTrans(String batchId){
String query_r ="update AM_GROUP_ASSET_VERIFICATION set process_flag='Y' where GROUP_ID = ?";


try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query_r);
      ps.setString(1, batchId);
           int i =ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("AssetRecordBean: updateAssetVerifyPendingTrans()>>>>>" + ex);
        } 

}

public void updateamgroupassetDisposal(String groupId){
	String query_r ="UPDATE AM_GROUP_DISPOSAL SET DISPOSED='P' WHERE DISPOSAL_ID = ?";
	String query_del ="DELETE FROM AM_GROUP_DISPOSAL WHERE DISPOSAL_ID = ?";
//	System.out.println("query_r updateamgroupassetImprovement>>>> "+query_r);
	
 
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_r);
			  ps.setString(1, groupId);
	           int i =ps.executeUpdate();
	       PreparedStatement ps1 = con.prepareStatement(query_del);
			  ps.setString(1, groupId);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateamgroupassetDisposal()>>>>>" + ex);
	        } 


	}

public void updateAssetStatusChange(String query_r,String param){
//String query_r ="update" +tableName+ "set"+columnName+"=? where asset_id = '"+assetId+"'";

        //ResultSet rs = null;
try { 

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query_r);
			ps.setString(1, param);
           int i =ps.executeUpdate();
         //  dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusChange() with Two Parameter>>>>>" + ex);
        }
}//updateAssetStatus()


public void updateAssetStatusChange(String query_r,String param1,String param2){
//String query_r ="update" +tableName+ "set"+columnName+"=? where asset_id = '"+assetId+"'";


try { 

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query_r);
			ps.setString(1, param1);
			ps.setString(2, param2);
           int i =ps.executeUpdate();
           //dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusChange() with Three Parameter>>>>>" + ex);
        } 
}//updateAssetStatus()


public void updateAssetStatusChange(String query_r,String param1,String param2,String param3){
//String query_r ="update" +tableName+ "set"+columnName+"=? where asset_id = '"+assetId+"'";


        //ResultSet rs = null;
try { 

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps = con.prepareStatement(query_r);
			ps.setString(1, param1);
			ps.setString(2, param2);
			ps.setString(3, param3);
           int i =ps.executeUpdate();
          // dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {

            System.out.println("AssetRecordBean: updateAssetStatusChange() with Four Parameter>>>>>" + ex);
        } 
}//updateAssetStatus()



public void setPendingTransMultiApp(String[] a, String code,int assetCode, String supervisorId,String mtid){

    int transaction_level=0;
   
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code,VATABLE_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
   
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	       PreparedStatement ps = con.prepareStatement(tranLevelQuery);
         ResultSet rs = ps.executeQuery();


        while(rs.next())
        {
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if

//        System.out.println("a[6] in setPendingTrans: "+a[6]);

        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
//        String dd = a[6].substring(0,2);
//        String mm = a[6].substring(3,5);
//        String yyyy = a[6].substring(6,10);
//        String effDate = yyyy+"-"+mm+"-"+dd;
//        System.out.println("effDate in setPendingTrans: "+effDate);
//        String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
/*           
        String pq1 = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
                "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                "transaction_level,asset_code) values('"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+dbConnection.getDateTime(new java.util.Date())+"','"+a[5]+"',"
                		+ "'"+effDate+"','"+a[7]+"','"+a[8]+"','"+a[9]+"','"+a[10]+"','"+timer.format(new java.util.Date())+"',"
                				+ "'"+mtid+"','"+mtid+"','"+transaction_level+"','"+assetCode+"')";
        System.out.println("pq1 In setPendingTrans: "+pq1);
        */
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[0]: "+a[0]);
        ps.setString(1, (a[0]==null)?"":a[0]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[1]: "+a[1]);
        ps.setString(2, (a[1]==null)?"":a[1]);
//        ps.setString(3, (a[2]==null)?"":a[2]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  supervisorId: "+supervisorId);
        ps.setString(3, supervisorId);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[3]: "+a[3]);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[5]: "+a[5]);
        ps.setString(6, (a[5]==null)?"":a[5]);
        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//        ps.setString(7,effDate);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[7]: "+a[7]);
        ps.setString(8, (a[7]==null)?"":a[7]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[8]: "+a[8]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
        ps.setString(10, (a[9]==null)?"":a[9]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[10]: "+a[10]);
        ps.setString(11, a[10]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  timer.format(new java.util.Date()): "+timer.format(new java.util.Date()));
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,mtid);
        ps.setString(14, mtid);
        ps.setInt(15, transaction_level);
        ps.setInt(16,assetCode);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[11]: "+a[11]);
        ps.setDouble(17, (a[11]==null)?0:Double.parseDouble(a[11]));

        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTransMultiApp Three 3>>>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()



//public void setPendingMultiApprTransArchive(String[] a, String code,int mtid, int assetCode,String supervisorId){
//
//      int transaction_level=0;
//      Connection con;
//      PreparedStatement ps;
//      ResultSet rs;
//String pq = "insert into am_asset_approval_archive(asset_id,user_id,super_id,amount,posting_date,description," +
//       "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
//       "transaction_level,asset_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
//      con = null;
//      ps = null;
//      rs = null; 
////      System.out.println("tranLevelQuery$$$$$$$$$$$$$$$$$$$$$$: "+tranLevelQuery);
//      try
//      {
//          con = dbConnection.getConnection("legendPlus");
//
//
//          /////////////To get transaction level
//           ps = con.prepareStatement(tranLevelQuery);
//            rs = ps.executeQuery();
//
//
//          while(rs.next()){
//          transaction_level = rs.getInt(1);
//
//          }//if
//
//
//
//          ////////////To set values for approval table
//
//          ps = con.prepareStatement(pq);
//
//
//          SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
////          String dd = a[6].substring(0,2);
////          String mm = a[6].substring(3,5);
////          String yyyy = a[6].substring(6,10);
////          String effDate = yyyy+"-"+mm+"-"+dd;
//          //String mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");
////          System.out.println("a[0]$$$$$$$$$$$$$$$$$$$$$$: "+a[0]);
//          ps.setString(1, (a[0]==null)?"":a[0]);
////          System.out.println("a[1]$$$$$$$$$$$$$$$$$$$$$$: "+a[1]);
//          ps.setString(2, (a[1]==null)?"":a[1]);
////          ps.setString(3, (a[2]==null)?"":a[2]);
////          System.out.println("supervisorId$$$$$$$$$$$$$$$$$$$$$$: "+supervisorId);
//          ps.setString(3, supervisorId);
////          System.out.println("a[3]$$$$$$$$$$$$$$$$$$$$$$: "+a[3]);
//          ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
////          System.out.println("a[4]$$$$$$$$$$$$$$$$$$$$$$: "+a[4]);
//          //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
//          ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
////          System.out.println("a[5]$$$$$$$$$$$$$$$$$$$$$$: "+a[5]);
//          ps.setString(6, (a[5]==null)?"":a[5]);
////          ps.setString(7,effDate);
////          System.out.println("a[6]$$$$$$$$$$$$$$$$$$$$$$: "+a[6]);
//          ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
////          System.out.println("a[7]$$$$$$$$$$$$$$$$$$$$$$: "+a[7]);
//          ps.setString(8, (a[7]==null)?"":a[7]);
//          ps.setString(9, "ACTIVE"); //asset_status
////          System.out.println("a[9]$$$$$$$$$$$$$$$$$$$$$$: "+a[9]);
//          ps.setString(10, (a[9]==null)?"":a[9]);
//          ps.setString(11, "A");
//          ps.setString(12, timer.format(new java.util.Date()));
////          System.out.println("mtid$$$$$$$$$$$$$$$$$$$$$$: "+mtid);
//          ps.setInt(13,mtid);
//          ps.setString(14, String.valueOf(mtid));
////          System.out.println("transaction_level$$$$$$$$$$$$$$$$$$$$$$: "+transaction_level);
//          ps.setInt(15, transaction_level);
////          System.out.println("assetCode$$$$$$$$$$$$$$$$$$$$$$: "+assetCode);
//          ps.setInt(16,assetCode);
//
//          ps.execute();
//
//      }
//      catch(Exception er)
//      {
//          System.out.println(">>>2 AssetRecordBeans:setPendingMultiApprTransArchive()>>>>>>" + er);
//
//      }finally{
//      dbConnection.closeConnection(con, ps);
//
//      }
//  }

public void setPendingMultiApprTransArchive(String[] a, String code, long mtid, long assetCode, String supervisorId) {

	 System.out.println(" Welcome to  setPendingMultiApprTransArchive >>>>>>>>>>>>>>>>> ");
    int transaction_level = 0;
    

    String pq = "INSERT INTO am_asset_approval_archive(asset_id, user_id, super_id, amount, posting_date, description, " +
            "effective_date, branchCode, asset_status, tran_type, process_status, tran_sent_time, transaction_id, batch_id, " +
            "transaction_level, asset_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String tranLevelQuery = "SELECT level FROM approval_level_setup WHERE code = ?";

    try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            transaction_level = rs.getInt(1);
        }
       

        System.out.println(" Preparing to insert into setPendingMultiApprTransArchive >>>>>>>>>>>>>>>>> ");
        ps = con.prepareStatement(pq);
        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

        ps.setString(1, a[0] == null ? "" : a[0]);
        ps.setString(2, a[1] == null ? "" : a[1]);
        ps.setString(3, supervisorId);
        ps.setDouble(4, a[3] == null ? 0 : Double.parseDouble(a[3]));
        ps.setTimestamp(5, dbConnection.getDateTime(new java.util.Date()));
        ps.setString(6, a[5] == null ? "" : a[5]);
        ps.setDate(7, a[6] == null ? null : dbConnection.dateConvert(a[6]));
        ps.setString(8, a[7] == null ? "" : a[7]);
        ps.setString(9, "ACTIVE");
        ps.setString(10, a[9] == null ? "" : a[9]);
        ps.setString(11, "A");
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setLong(13, mtid); // transaction_id
        ps.setString(14, String.valueOf(mtid)); // batch_id
        ps.setInt(15, transaction_level);
        ps.setLong(16, assetCode); // asset_code

        ps.executeUpdate();

    } catch (Exception er) {
        System.out.println(">>>2 AssetRecordBeans:setPendingMultiApprTransArchive()>>>>>>" + er);
    } 
}


public java.util.ArrayList getApprovalsId(String branchId, String deptCode, String userName)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
    String query = " select USER_ID,User_Name,email,full_name from am_gb_User where is_supervisor = 'Y' and User_Status = 'ACTIVE' and Branch = '"+branchId+"' and User_Name != '"+userName+"' "
    		+ "and dept_code = '"+deptCode+"' order by branch, dept_code ";
//    System.out.println("$$$$$$$$$$$$$$$$$$$$$$  query in getApprovalsId: "+query);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {				
				String strUserId = rs.getString("USER_ID");
				String strUserName = rs.getString("User_Name");
				String strfullName= rs.getString("full_name");
				String stremail= rs.getString("email");
//				System.out.println("$$$$$$$$$$$$$$$$$$$$$$  strUserId: "+strUserId);
				User user = new User();
				user.setUserId(strUserId);
				user.setUserName(strUserName);
				user.setEmail(stremail);
				user.setUserFullName(strfullName);
				_list.add(user);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
} 

public void updateAssetStatusChange(String query_r,Timestamp date,int tranId){
	
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps = con.prepareStatement(query_r.toString());
	  ps.setTimestamp(1, date);
	  ps.setInt(2, tranId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
        } 


}//updateAssetStatus()

public void updateAssetStatusChange(String query_r,String naration,Timestamp date,int tranId){
		
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps  = con.prepareStatement(query_r.toString());
	  ps.setString(1, naration);
	  ps.setTimestamp(2, date);
	  ps.setInt(3, tranId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
        } 


}//updateAssetStatus()

public void deleteOtherSupervisors(String batchId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE Asset_Id = '"+batchId+"' and super_id != '"+supervisorId+"' and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherSupervisors>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisors()>>>>>" + ex);
	        } 
    

	}

public String[] setApprovalDataUploadGroup(long id,String tableName){

//String q ="select asset_id, asset_status,user_ID,supervisor,Cost_Price,Posting_Date,description,effective_date,BRANCH_CODE from am_asset where asset_id ='" +id+"'";
   String[] result= new String[12];
   
        String uploadquery = "";
        dbConnection.getDateTime(new java.util.Date());
        Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
        //int groupId = Integer.parseInt(id);
        if(tableName.equalsIgnoreCase("AM_GROUP_DISPOSAL")){
        	uploadquery ="select TOP 1 disposal_ID AS group_id,user_ID,'' AS supervisor,Cost_Price,"
         		+ "Disposal_Date AS Posting_Date,description,effDate AS effective_date,BRANCH_CODE,"
         		+ "approval_status AS Asset_Status from AM_GROUP_DISPOSAL where disposal_Id =" +id ;
        }
        if(tableName.equalsIgnoreCase("AM_GROUP_ASSET")){
        	uploadquery ="select TOP 1 group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
             		"		description,effective_date,BRANCH_CODE," +
             				"Asset_Status from AM_GROUP_ASSET where group_id =" +id ;
        }
        if(tableName.equalsIgnoreCase("AM_GROUP_IMPROVEMENT")){
        	uploadquery ="select TOP 1 Revalue_ID AS group_id,user_ID,'' AS supervisor,Cost_Price,"
             		+ "Revalue_Date AS Posting_Date,description,effDate AS effective_date,BRANCH_CODE,"
             		+ "approval_status AS Asset_Status from AM_GROUP_IMPROVEMENT where Revalue_ID =" +id ;
        }        
        if(tableName.equalsIgnoreCase("am_gb_bulkTransfer")){
        	uploadquery ="select TOP 1 ASSET_ID AS group_id,'' AS user_ID,'' AS supervisor,Cost_Price,"
             		+ "transfer_Date AS Posting_Date,description,transfer_Date AS effective_date,NEW_BRANCH_CODE AS BRANCH_CODE,"
             		+ "status AS Asset_Status from am_gb_bulkTransfer where Batch_id =" +id ;
        }        
        if(tableName.equalsIgnoreCase("am_gb_bulkUpdate")){
        	uploadquery ="select TOP 1 ASSET_ID AS group_id,'' AS user_ID,'' AS supervisor,0 as Cost_Price,"
             		+ "'"+approveddate+"' AS Posting_Date,description,'"+approveddate+"' AS effective_date, branch_id AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from am_gb_bulkUpdate where Batch_id =" +id ;
        }        
        if(tableName.equalsIgnoreCase("am_Asset_Proof")){
        	uploadquery ="select TOP 1 ASSET_ID AS group_id,'' AS user_ID,'' AS supervisor,Cost_Price,"
             		+ "PROOF_DATE AS Posting_Date,description,effective_date, branch_id AS BRANCH_CODE,"
             		+ "PROCESS_STATUS AS Asset_Status from am_Asset_Proof where Batch_id =" +id ;
        }        
        if(tableName.equalsIgnoreCase("am_Asset_Proof_Selection")){
        	uploadquery ="select TOP 1 ASSET_ID AS group_id,'' AS user_ID,'' AS supervisor,Cost_Price,"
             		+ "PROOF_DATE AS Posting_Date,description,effective_date, branch_id AS BRANCH_CODE,"
             		+ "PROCESS_STATUS AS Asset_Status from am_Asset_Proof_Selection where Batch_id =" +id ;
        }  
        if(tableName.equalsIgnoreCase("am_gb_UncapitalisedbulkUpdate")){
        	uploadquery ="select TOP 1 ASSET_ID AS group_id,'' AS user_ID,'' AS supervisor,0 as Cost_Price,"
             		+ "'"+approveddate+"' AS Posting_Date,description,'"+approveddate+"' AS effective_date, branch_id AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from am_gb_UncapitalisedbulkUpdate where Batch_id =" +id ;
        }  
        if(tableName.equalsIgnoreCase("AM_GROUP_ASSET_UNCAPITALIZED")){
        	uploadquery ="select TOP 1 group_id,user_ID,supervisor,Cost_Price,Posting_Date," +
             		"		description,effective_date,BRANCH_CODE," +
             				"Asset_Status from AM_GROUP_ASSET_UNCAPITALIZED where group_id =" +id ;
        }
        if(tableName.equalsIgnoreCase("am_gb_UserTmp")){
        	uploadquery ="select TOP 1 tmpid AS group_id,'' AS user_ID,'' AS supervisor,0 as Cost_Price,"
             		+ "create_Date AS Posting_Date,Full_Name AS description,create_Date AS effective_date, branch AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from am_gb_UserTmp where tmpid =" +id ;
        }  
        if(tableName.equalsIgnoreCase("am_ad_vendorTmp")){
        	uploadquery ="select TOP 1 Vendor_ID AS group_id,'' AS user_ID,'' AS supervisor,0 as Cost_Price,"
             		+ "create_Date AS Posting_Date,Vendor_Name AS description,create_Date AS effective_date, '' AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from am_ad_vendorTmp where Vendor_ID =" +id ;
        }  
        if(tableName.equalsIgnoreCase("AM_GROUP_ASSET_VERIFICATION")){
        	uploadquery ="select TOP 1 group_id,'' AS user_ID,'' AS supervisor,0 as Cost_Price,"
             		+ "Posting_Date AS Posting_Date,description,Posting_Date AS effective_date, '' AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from AM_GROUP_ASSET_VERIFICATION where group_ID =" +id ;
        }    
        if(tableName.equalsIgnoreCase("FT_MAINTENANCE_HISTORY")){
        	uploadquery ="select TOP 1 LT_ID,'' AS user_ID,'' AS supervisor,COST_PRICE as Cost_Price,"
             		+ "CREATE_DATE AS Posting_Date,DETAILS AS description,CREATE_DATE AS effective_date, '' AS BRANCH_CODE,"
             		+ "'' AS Asset_Status from FT_MAINTENANCE_HISTORY where LT_ID =" +id ;
        }         
//         System.out.println("Query in setApprovalDataUploadGroup : " + uploadquery);
//         String groupidQry = "select group_id from am_group_asset_main where group_id =" +id;
//         String Qrygroupid = approvalRec.getCodeName(groupidQry); 
//        		 System.out.println("Qrygroupid in setApprovalDataGroup : " + Qrygroupid);
        try {

  		  Connection con = dbConnection.getConnection("legendPlus");
  	        PreparedStatement ps  = con.prepareStatement(uploadquery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = String.valueOf(id);
                result[1]= rs.getString(2);
                result[2] = rs.getString(3);
               result[3] = rs.getString(4);
               result[4] = dbConnection.formatDate(rs.getDate(5));
//               System.out.println("Before Conversion" + rs.getDate(5));
//               System.out.println("Posting_Date in setApprovalDataGroup : " +  result[4]);
               result[5] = rs.getString(6);
               result[6] = dbConnection.formatDate(rs.getDate(7));
//               System.out.println("Effective_Date in setApprovalDataGroup : " +  result[6]);
               result[7] = rs.getString(8);
               result[8] = rs.getString(9);//asset_status

            }
//            System.out.println("Final Conversion");
        } catch (Exception ex) {
            System.out.println("AssetRecordsBean : setApprovalDataUploadGroup()WARN: Error setting approval data for group asset creation ->" + ex);
        } 
        	result[9] = "Group Asset Creation";
        	result[10] = "P";
//result[11] = timeInstance();
        return result;

}//setApprovalData()

public void deleteOtherSupervisorswithBatchId(String batchId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE Batch_Id = '"+batchId+"' and super_id != '"+supervisorId+"' and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherSupervisorswithBatchId>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisorswithBatchId()>>>>>" + ex);
	        } 
  

	}

public void deleteOtherSupervisorswithAssetId(String batchId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE Asset_Id = '"+batchId+"' and super_id != '"+supervisorId+"' and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherSupervisorswithBatchId>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisorswithBatchId()>>>>>" + ex);
	        } 
  

	}



public void deleteOtherSupervisorswithBatchIdSupervisor(String batchId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE Batch_Id = '"+batchId+"' and super_id != '"+supervisorId+"' ";
//	System.out.println("query_r deleteOtherSupervisorswithBatchId>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisorswithBatchId()>>>>>" + ex);
	        } 
  

	}
/**
 * *Bulk asset Transfer
 */
public String bulkAssetTransferUpdateforApproval(java.util.ArrayList list) {
 String query = "update am_assettransfer SET  " +
				 "approval_Status=? " +
				 "WHERE transfer_id=? ";
//System.out.println("bulkAssetTransferUpdate query: "+query);
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
String newmtid = "";
int assetcode = 0;
int[] d = null;
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        newmtid = transactId+String.valueOf(assetcode);
  //      System.out.println("bulkAssetTransferUpdate newmtid: "+newmtid+"   MTID: "+mtid);
        ps.setString(1, newmtid);
        ps.setString(2, "APPROVED");        
        ps.addBatch();
}
	d = ps.executeBatch();
	

} catch (Exception ex) {
	System.out.println("Error Updating asset for Transfer in bulkAssetTransferUpdateforApproval ->" + ex);
} 
mtidassetcode =mtid+String.valueOf(assetcode);
return mtidassetcode;

}

/**
 * *Bulk asset Transfer
 */
public String bulkAssetTransferUpdateBulkTable(java.util.ArrayList list,String batchId) {
 String query = "update am_gb_bulkTransfer SET  " +
				 "Status=? " +
				 "WHERE Batch_id=? ";
//System.out.println("bulkAssetTransferUpdate query: "+query);
String mtidassetcode = "";

magma.AssetRecordsBean bd = null;
String mtid = "";
String newmtid = "";
int assetcode = 0;
int[] d = null;
try {

	  Connection con = dbConnection.getConnection("legendPlus");
      PreparedStatement ps = con.prepareStatement(query);

	for (int i = 0; i < list.size(); i++) {
		bd = (magma.AssetRecordsBean) list.get(i);
        assetcode = bd.getAssetCode();
        mtid = bd.getAsset_id();
        String transactId = bd.getEngine_number();
        newmtid = transactId+String.valueOf(assetcode);
  //      System.out.println("bulkAssetTransferUpdate newmtid: "+newmtid+"   MTID: "+mtid);
        ps.setString(1, "APPROVED");  
        ps.setString(2, batchId);
        ps.addBatch();
}
	d = ps.executeBatch();
	

} catch (Exception ex) {
	System.out.println("Error Updating asset for Transfer ->" + ex);
}
mtidassetcode =mtid+String.valueOf(assetcode);
return mtidassetcode;

}


public void deleteOtherUsers(String batchId, String userId){
	String query_del ="DELETE FROM am_asset_approval WHERE Asset_Id = '"+batchId+"' and user_id != '"+userId+"' and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherUsers>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherUsers()>>>>>" + ex);
	        } 
  

	}

public void setPendingTransMultiProofApp(String[] a, String code,int assetCode, String supervisorId,String mtid){

    int transaction_level=0;
    
String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
     "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
     "transaction_level,asset_code,VATABLE_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
  
    try
    {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(tranLevelQuery);
         ResultSet rs = ps.executeQuery();


        while(rs.next())
        {
        transaction_level = rs.getInt(1);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
//         System.out.println(transaction_level);
//          System.out.println(code);
        }//if

//        System.out.println("a[6] in setPendingTrans: "+a[6]);

        ////////////To set values for approval table

        ps = con.prepareStatement(pq);


        SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");
        ps.setString(1, mtid);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[1]: "+a[1]);
        ps.setString(2, (a[1]==null)?"":a[1]);
//        ps.setString(3, (a[2]==null)?"":a[2]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  supervisorId: "+supervisorId);
        ps.setString(3, supervisorId);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[3]: "+a[3]);
        ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
        //ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
        ps.setTimestamp(5,dbConnection.getDateTime(new java.util.Date()));
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[5]: "+a[5]);
        ps.setString(6, (a[5]==null)?"":a[5]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[6]: "+a[6]);
        ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//        ps.setString(7,effDate);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[7]: "+a[7]);
        ps.setString(8, (a[7]==null)?"":a[7]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[8]: "+a[8]);
        ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[9]: "+a[9]);
        ps.setString(10, (a[9]==null)?"":a[9]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[10]: "+a[10]);
        ps.setString(11, a[10]);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  timer.format(new java.util.Date()): "+timer.format(new java.util.Date()));
        ps.setString(12, timer.format(new java.util.Date()));
        ps.setString(13,a[0]);
        ps.setString(14, mtid);
        ps.setInt(15, transaction_level);
        ps.setInt(16,assetCode);
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$  a[11]: "+a[11]);
        ps.setDouble(17, (a[11]==null)?0:Double.parseDouble(a[11]));

        ps.execute();

    }
    catch(Exception er)
    {
        System.out.println(">>>AssetRecordBeans:setPendingTrans in setPendingTransMultiProofApp Three 3>>>>>>" + er);

    }

//String pq = "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description,effective_date,branchCode,tran_type, process_status,tran_sent_time) values(?,?,?,?,?,?,?,?,?,?,?)";

}//staticApprovalInfo()


public void deleteOtherSupervisorswithTransactionId(String transactionId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE transaction_id = ? and super_id != ? and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherSupervisorswithTransactionId>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_del);
			  ps.setString(1, transactionId);
			  ps.setString(2, supervisorId);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisorswithTransactionId()>>>>>" + ex);
	        } 
  

	}

public void deleteOtherSupervisorswithBatchId(String batchId, String supervisorId,String status){
	String query_del ="DELETE FROM am_asset_approval WHERE Batch_Id = '"+batchId+"' and super_id != '"+supervisorId+"' and Asset_Status = '"+status+"' ";
//	System.out.println("query_r deleteOtherSupervisorswithBatchId>>>> "+query_del);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisorswithBatchId()>>>>>" + ex);
	        } 
  

	}

public void deleteRecords(String query){
//	System.out.println("query deleteRecords>>>> "+query);
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteRecords()>>>>>" + ex);
	        } 
  

	}


public void bulkAssetTransferWithZeroUpdateBulkTable(String batchId){
	 String query_r = "update a set a.newAsset_User = b.New_Asset_user from am_gb_bulkTransfer a, am_assetTransfer b where a.Asset_id = b.asset_id "
		 		+ "and a.NEW_ASSET_ID = b.new_asset_id "
		 		+ "and a.Batch_id = ? "
		 		+ "and a.newAsset_User = '0' ";	
	
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_r);
			  ps.setString(1, batchId);
	           int i =ps.executeUpdate();           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: bulkAssetTransferWithZeroUpdateBulkTable()>>>>>" + ex);
	        } 
	}


public void WrongAssetWithCodes(String asseId){
	 String query_BranchCode = "update a set a.BRANCH_CODE=b.BRANCH_CODE from am_asset a, am_ad_branch b where a.BRANCH_ID = b.BRANCH_ID and a.BRANCH_CODE = 0 and a.Asset_id = ? ";
	 String query_CategoryCode = "update a set a.CATEGORY_CODE=b.category_code from am_asset a, am_ad_category b where a.CATEGORY_ID = b.category_ID and a.CATEGORY_CODE = 0 and a.Asset_id = ? ";
	 String query_DeptCode = "update a set a.DEPT_CODE=b.Dept_code from am_asset a, am_ad_department b where a.Dept_ID = b.Dept_ID and a.DEPT_CODE = 0 and a.Asset_id = ? ";
	 String query_SubCategory = "update am_asset set SUB_CATEGORY_CODE='000',SUB_CATEGORY_ID='1' from am_asset where SUB_CATEGORY_ID = '0' and Asset_id = ? ";
	
 
	try {

		  Connection con = dbConnection.getConnection("legendPlus");
	        PreparedStatement ps  = con.prepareStatement(query_BranchCode);
	        PreparedStatement ps1 = con.prepareStatement(query_CategoryCode);
	        PreparedStatement ps2 = con.prepareStatement(query_DeptCode);
	        PreparedStatement ps3 = con.prepareStatement(query_SubCategory);
			   ps.setString(1, asseId);
	           int i =ps.executeUpdate();     
			   ps1.setString(1, asseId);
		       int j =ps1.executeUpdate(); 	
		       ps2.setString(1, asseId);
		       int k =ps2.executeUpdate(); 
		       ps3.setString(1, asseId);
			   int l =ps3.executeUpdate(); 		           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: WrongAssetWithCodes()>>>>>" + ex);
	        } 
	}

}
