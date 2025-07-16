package com.magbel.ia.bus;

import java.sql.Connection;
import com.magbel.ia.vao.Stock;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.util.DatetimeFormat;
import com.magbel.ia.vao.ApprovalRemark;   
import magma.util.Codes;

import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;
public class InventoryBus extends PersistenceServiceDAO
{
	 
	com.magbel.util.DatetimeFormat df;
    DatetimeFormat dateFormat;
	java.text.SimpleDateFormat sdf;
	magma.util.Codes code;
	CodeGenerator cg;
	 
 public InventoryBus()
 {
	cg = new CodeGenerator();
 	code = new Codes();
 	df= new com.magbel.util.DatetimeFormat(); 
 }
    
 
 public boolean rinsertAssetRecord(String branch_id , String department_id , String category_id,
         String depreciation_start_date, String depreciation_end_date,String posting_date ,
         String make ,String location , String maintained_by , String authorized_by ,  
         String supplied_by ,String reason , String inventory_id , String description, 
         String vendor_account ,String cost_price, String vatable_cost ,String vat_amount , 
         String serial_number,String engine_number ,String model, String user ,
         String depreciation_rate ,  
         String residual_value,  String subject_to_vat , String date_of_purchase ,String registration_no,   
         String require_depreciation ,String who_to_remind , String email_1 , String email2 ,  
         String raise_entry , String section ,String user_id , String state ,String driver ,
         String who_to_remind_2 ,
         String spare_1 ,String spare_2, double accum_dep, String section_id,
         String wh_tax_cb ,String wh_tax_amount,String require_redistribution,  
         String status ,String province, String multiple, String warrantyStartDate,
         String noOfMonths ,String expiryDate ,String amountPTD,String amountREM ,   
         String partPAY ,String fullyPAID , String group_id ,String auth_user,
         String authuser ,String dateFormat,String bar_code ,String sbu_code ,  
         String lpo , String supervisor ,String deferPay,int selectTax,String Item_TypeCode,String Warehouse_Code,String compCode) throws Exception, Throwable 
 {
 	Connection con = null;
     PreparedStatement ps = null;
     boolean done = true; 
     
     if (deferPay == null || deferPay.equals("")) {
     	deferPay = "N";
     }
     if (status == null || status.equals("")) {
     	status = "PENDING";
     }
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
 if (supervisor == null || supervisor.equals("")) {
 	supervisor ="";
    }
 
 if (Item_TypeCode == null || Item_TypeCode.equals("")) {
 	Item_TypeCode ="";
    }
 
 if (Warehouse_Code == null || Warehouse_Code.equals("")) {
 	Warehouse_Code ="";
    }
    

     vat_amount = vat_amount.replaceAll(",", "");
     vatable_cost = vatable_cost.replaceAll(",", "");
     wh_tax_amount = wh_tax_amount.replaceAll(",", "");
     residual_value = residual_value.replaceAll(",", "");
     amountPTD = amountPTD.replaceAll(",","");
     String createQuery = "INSERT INTO IA_INVENTORY" +
                          "(" +
                          "INVENTORY_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
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
                          "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent,Item_TypeCode,Warehouse_Code,comp_code) " +

                          "VALUES" +
                          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                          "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

   
     try {

         double costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
     	
         System.out.println( "-----------------------------------------------" );
     	System.out.println("-costPrice-"+ costPrice ); 
         System.out.println("-branch_id-"+ branch_id ); 
         System.out.println("-department_id-"+ department_id ); 
         System.out.println("-category_id-"+ category_id);
         System.out.println("-depreciation_start_date-"+ depreciation_start_date); 
         System.out.println("-depreciation_end_date-"+ depreciation_end_date);
         System.out.println("-posting_date-"+ posting_date );
         System.out.println("-make-"+ make );
         System.out.println("-location-"+ location ); 
         System.out.println("-maintained_by-"+ maintained_by ); 
         System.out.println("-authorized_by-"+ authorized_by );  
         System.out.println("-supplied_by-"+ supplied_by );
         System.out.println("-reason-"+ reason ); 
         System.out.println("-inventory_id-"+ inventory_id ); 
         System.out.println("-description-"+ description); 
         System.out.println("-vendor_account-"+ vendor_account );
         System.out.println("-cost_price-"+ cost_price); 
         System.out.println("-vatable_cost-"+ vatable_cost );
         System.out.println("-vat_amount-"+ vat_amount ); 
         System.out.println("-serial_number-"+ serial_number);
         System.out.println("-engine_number-"+ engine_number );
         System.out.println("-model-"+ model); 
         System.out.println("-user-"+ user );
         System.out.println("-depreciation_rate-"+ depreciation_rate );  
         System.out.println("-residual_value-"+ residual_value);  
         System.out.println("-subject_to_vat-"+ subject_to_vat ); 
         System.out.println("-date_of_purchase-"+ date_of_purchase );
         System.out.println("-registration_no-"+ registration_no);   
         System.out.println("-require_depreciation-"+ require_depreciation );
         System.out.println("-who_to_remind-"+ who_to_remind ); 
         System.out.println("-email_1-"+ email_1 ); 
         System.out.println("-email2-"+ email2 );  
         System.out.println("-raise_entry-"+ raise_entry ); 
         System.out.println("-section-"+ section );
         System.out.println("-user_id-"+ user_id ); 
         System.out.println("-state-"+ state );
         System.out.println("-driver-"+ driver );
         System.out.println("-who_to_remind_2-"+ who_to_remind_2 );
         System.out.println("-spare_1-"+ spare_1 );
         System.out.println("-spare_2-"+ spare_2); 
         System.out.println("-accum_dep-"+ accum_dep); 
         System.out.println("-section_id-"+ section_id);
         System.out.println("-wh_tax_cb-"+ wh_tax_cb );
         System.out.println("-wh_tax_amount-"+ wh_tax_amount);
         System.out.println("-require_redistribution-"+ require_redistribution);  
         System.out.println("-status-"+ status );
         System.out.println("-province-"+ province); 
         System.out.println("-multiple-"+ multiple); 
         System.out.println("-warrantyStartDate-"+ warrantyStartDate);
         System.out.println("-noOfMonths-"+ noOfMonths );
         System.out.println("-expiryDate-"+ expiryDate );
         System.out.println("-amountPTD-"+ amountPTD);
         System.out.println("-amountREM-"+ amountREM );   
         System.out.println("-partPAY-"+ partPAY );
         System.out.println("-fullyPAID-"+ fullyPAID ); 
         System.out.println("-group_id-"+ group_id );
         System.out.println("-auth_user-"+ auth_user);
         System.out.println("-authuser-"+ authuser );
         System.out.println("-dateFormat-"+ dateFormat);
         System.out.println("-bar_code-"+ bar_code );
         System.out.println("-sbu_code-"+ sbu_code );  
         System.out.println("-lpo-"+ lpo ); 
         System.out.println("-supervisor-"+ supervisor );
         System.out.println("-deferPay-"+ deferPay);
         System.out.println("-selectTax-"+ selectTax);
         System.out.println("-Item_TypeCode-"+ Item_TypeCode);                                                                                                                                                                                                                                                                                                                                                    
         System.out.println("-Warehouse_Code-"+ Warehouse_Code);
         System.out.println("-compCode-"+ compCode);
         
         con = getConnection();
         ps = con.prepareStatement(createQuery);
         ps.setString(1, cg.generateCode("STOCK", "", "", ""));
         ps.setString(2, registration_no);
         ps.setInt(3, Integer.parseInt(branch_id));
         ps.setInt(4, Integer.parseInt(department_id));
         ps.setInt(5, Integer.parseInt(section_id));
         ps.setInt(6, Integer.parseInt(category_id));
         ps.setString(7, description);
         ps.setString(8, vendor_account);
         ps.setDate(9, df.dateConvert(date_of_purchase));
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
         ps.setDate(22, df.dateConvert(depreciation_start_date));
         ps.setDouble(23, Double.parseDouble(residual_value));
         ps.setString(24, authorized_by); 
         ps.setDate(25, df.dateConvert(new java.util.Date()));
         ps.setDate(26, df.dateConvert(depreciation_start_date));
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
         ps.setDate(54, df.dateConvert(warrantyStartDate));
         ps.setInt(55, new Integer(noOfMonths).intValue());
         ps.setDate(56, df.dateConvert(expiryDate));
         ps.setString(57, code.getBranchCode(branch_id));
         ps.setString(58, code.getDeptCode(department_id));
         ps.setString(59, code.getSectionCode(section_id));
         ps.setString(60, code.getCategoryCode(category_id));
         ps.setDouble(61, Double.parseDouble(amountPTD));
         ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
         ps.setString(63, partPAY);
         ps.setString(64, fullyPAID);
         ps.setString(65, bar_code);
         ps.setString(66,sbu_code);
         ps.setString(67, lpo);
         ps.setString(68,supervisor);
         ps.setString(69, deferPay);
         ps.setInt(70, selectTax);
         ps.setString(71, Item_TypeCode);
         ps.setString(72, Warehouse_Code);
         ps.setString(73, compCode);
         
         ps.execute();
       
     } catch (Exception ex) {
         done = false;
         System.out.println("WARN:Error creating asset->" + ex);
         ex.printStackTrace();
     } finally {
     	closeConnection(con, ps);
     }

     return done;
 }
     
	 
    public boolean rinsertAssetRecord(String branch_id , String department_id , String category_id,
            String depreciation_start_date, String depreciation_end_date,String posting_date ,
            String make ,String location , String maintained_by , String authorized_by ,  
            String supplied_by ,String reason , String inventory_id , String description, 
            String vendor_account ,String cost_price, String vatable_cost ,String vat_amount , 
            String serial_number,String engine_number ,String model, String user ,
            String depreciation_rate ,  
            String residual_value,  String subject_to_vat , String date_of_purchase ,String registration_no,   
            String require_depreciation ,String who_to_remind , String email_1 , String email2 ,  
            String raise_entry , String section ,String user_id , String state ,String driver ,
            String who_to_remind_2 ,
            String spare_1 ,String spare_2, double accum_dep, String section_id,
            String wh_tax_cb ,String wh_tax_amount,String require_redistribution,  
            String status ,String province, String multiple, String warrantyStartDate,
            String noOfMonths ,String expiryDate ,String amountPTD,String amountREM ,   
            String partPAY ,String fullyPAID , String group_id ,String auth_user,
            String authuser ,String dateFormat,String bar_code ,String sbu_code ,  
            String lpo , String supervisor ,String deferPay,int selectTax,String Item_TypeCode,String Warehouse_Code,String compCode,String approvalLevel) throws Exception, Throwable 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = true; 
        
        if (deferPay == null || deferPay.equals("")) {
        	deferPay = "N";
        }
        if (status == null || status.equals("")) {
        	status = "PENDING";
        }
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
    if (supervisor == null || supervisor.equals("")) {
    	supervisor ="";
       }
    
    if (Item_TypeCode == null || Item_TypeCode.equals("")) {
    	Item_TypeCode ="";
       }
    
    if (Warehouse_Code == null || Warehouse_Code.equals("")) {
    	Warehouse_Code ="";
       }
       

        vat_amount = vat_amount.replaceAll(",", "");
        vatable_cost = vatable_cost.replaceAll(",", "");
        wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        residual_value = residual_value.replaceAll(",", "");
        amountPTD = amountPTD.replaceAll(",","");
        String createQuery = "INSERT INTO IA_INVENTORY_APPROVE" +
                             "(" +
                             "INVENTORY_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID," +
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
                             "AMOUNT_REM,PART_PAY,FULLY_PAID,BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay,wht_percent,Item_TypeCode,Warehouse_Code,comp_code,level_number) " +

                             "VALUES" +
                             "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                             "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

      
        try {

            double costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
        	
            System.out.println( "-----------------------------------------------" );
        	System.out.println("-costPrice-"+ costPrice ); 
            System.out.println("-branch_id-"+ branch_id ); 
            System.out.println("-department_id-"+ department_id ); 
            System.out.println("-category_id-"+ category_id);
            System.out.println("-depreciation_start_date-"+ depreciation_start_date); 
            System.out.println("-depreciation_end_date-"+ depreciation_end_date);
            System.out.println("-posting_date-"+ posting_date );
            System.out.println("-make-"+ make );
            System.out.println("-location-"+ location ); 
            System.out.println("-maintained_by-"+ maintained_by ); 
            System.out.println("-authorized_by-"+ authorized_by );  
            System.out.println("-supplied_by-"+ supplied_by );
            System.out.println("-reason-"+ reason ); 
            System.out.println("-inventory_id-"+ inventory_id ); 
            System.out.println("-description-"+ description); 
            System.out.println("-vendor_account-"+ vendor_account );
            System.out.println("-cost_price-"+ cost_price); 
            System.out.println("-vatable_cost-"+ vatable_cost );
            System.out.println("-vat_amount-"+ vat_amount ); 
            System.out.println("-serial_number-"+ serial_number);
            System.out.println("-engine_number-"+ engine_number );
            System.out.println("-model-"+ model); 
            System.out.println("-user-"+ user );
            System.out.println("-depreciation_rate-"+ depreciation_rate );  
            System.out.println("-residual_value-"+ residual_value);  
            System.out.println("-subject_to_vat-"+ subject_to_vat ); 
            System.out.println("-date_of_purchase-"+ date_of_purchase );
            System.out.println("-registration_no-"+ registration_no);   
            System.out.println("-require_depreciation-"+ require_depreciation );
            System.out.println("-who_to_remind-"+ who_to_remind ); 
            System.out.println("-email_1-"+ email_1 ); 
            System.out.println("-email2-"+ email2 );  
            System.out.println("-raise_entry-"+ raise_entry ); 
            System.out.println("-section-"+ section );
            System.out.println("-user_id-"+ user_id ); 
            System.out.println("-state-"+ state );
            System.out.println("-driver-"+ driver );
            System.out.println("-who_to_remind_2-"+ who_to_remind_2 );
            System.out.println("-spare_1-"+ spare_1 );
            System.out.println("-spare_2-"+ spare_2); 
            System.out.println("-accum_dep-"+ accum_dep); 
            System.out.println("-section_id-"+ section_id);
            System.out.println("-wh_tax_cb-"+ wh_tax_cb );
            System.out.println("-wh_tax_amount-"+ wh_tax_amount);
            System.out.println("-require_redistribution-"+ require_redistribution);  
            System.out.println("-status-"+ status );
            System.out.println("-province-"+ province); 
            System.out.println("-multiple-"+ multiple); 
            System.out.println("-warrantyStartDate-"+ warrantyStartDate);
            System.out.println("-noOfMonths-"+ noOfMonths );
            System.out.println("-expiryDate-"+ expiryDate );
            System.out.println("-amountPTD-"+ amountPTD);
            System.out.println("-amountREM-"+ amountREM );   
            System.out.println("-partPAY-"+ partPAY );
            System.out.println("-fullyPAID-"+ fullyPAID ); 
            System.out.println("-group_id-"+ group_id );
            System.out.println("-auth_user-"+ auth_user);
            System.out.println("-authuser-"+ authuser );
            System.out.println("-dateFormat-"+ dateFormat);
            System.out.println("-bar_code-"+ bar_code );
            System.out.println("-sbu_code-"+ sbu_code );  
            System.out.println("-lpo-"+ lpo ); 
            System.out.println("-supervisor-"+ supervisor );
            System.out.println("-deferPay-"+ deferPay);
            System.out.println("-selectTax-"+ selectTax);
            System.out.println("-Item_TypeCode-"+ Item_TypeCode);                                                                                                                                                                                                                                                                                                                                                    
            System.out.println("-Warehouse_Code-"+ Warehouse_Code);
            System.out.println("-compCode-"+ compCode);
            System.out.println("-approvalLevel-"+ approvalLevel);
            
            
            con = getConnection();
            ps = con.prepareStatement(createQuery);
            ps.setString(1, cg.generateCode("STOCK", "", "", ""));
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(section_id));
            ps.setInt(6, Integer.parseInt(category_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setDate(9, df.dateConvert(date_of_purchase));
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
            ps.setDate(22, df.dateConvert(depreciation_start_date));
            ps.setDouble(23, Double.parseDouble(residual_value));
            ps.setString(24, authorized_by); 
            ps.setDate(25, df.dateConvert(new java.util.Date()));
            ps.setDate(26, df.dateConvert(depreciation_start_date));
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
            ps.setDate(54, df.dateConvert(warrantyStartDate));
            ps.setInt(55, new Integer(noOfMonths).intValue());
            ps.setDate(56, df.dateConvert(expiryDate));
            ps.setString(57, code.getBranchCode(branch_id));
            ps.setString(58, code.getDeptCode(department_id));
            ps.setString(59, code.getSectionCode(section_id));
            ps.setString(60, code.getCategoryCode(category_id));
            ps.setDouble(61, Double.parseDouble(amountPTD));
            ps.setDouble(62, (costPrice-Double.parseDouble(amountPTD)));
            ps.setString(63, partPAY);
            ps.setString(64, fullyPAID);
            ps.setString(65, bar_code);
            ps.setString(66,sbu_code);
            ps.setString(67, lpo);
            ps.setString(68,supervisor);
            ps.setString(69, deferPay);
            ps.setInt(70, selectTax);
            ps.setString(71, Item_TypeCode);
            ps.setString(72, Warehouse_Code);
            ps.setString(73, compCode);
            ps.setDouble(74, Double.parseDouble(approvalLevel));
            
            ps.execute();
          
        } catch (Exception ex) {
            done = false;
            System.out.println("WARN:Error creating asset->" + ex);
            ex.printStackTrace();
        } finally {
        	closeConnection(con, ps);
        }

        return done;
    }
    

    private String getDepreciationRate(String category_id) throws Exception 
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rate = "0.0";
        String query = " SELECT ITEMTYPE_CODE FROM IA_ITEMTYPE " +
                       " WHERE ITEMTYPE_CODE = " + category_id;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                rate = rs.getString(1);
            }

        } 
        catch (Exception ex) 
        {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } 
        finally 
        {
        	closeConnection(con, ps);
        }

        return rate;
    }
    
    
    public String computeTotalLife(String depRate) 
    {
        String totalLife = "0";
        if (depRate == null || depRate.equals("")) 
        {
            depRate = "0.0";
        }
        double division = 100 / (Double.parseDouble(depRate));
        int intTotal = (int) (division * 12);
        totalLife = Integer.toString(intTotal);
        return totalLife;
     }
    
	public boolean updateInventoryTotal(String Item_TypeCode,String Warehouse_Code) 
	{

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = " UPDATE IA_INVENTORY_TOTALS SET BALANCE= BALANCE + 1   WHERE ITEM_CODE =? AND WAREHOUSE_CODE=? ";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, Item_TypeCode);
			ps.setString(2, Warehouse_Code); 
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	public boolean createInventoryHistory(String Item_TypeCode,String Warehouse_Code,String user_id)
	{

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO [IA_INVENTORY_HISTORY]([MTID], [ITEM_CODE],[TRANS_DESC], [QUANTITY],[WAREHOUSE_CODE]," +
				"[TRANS_DATE],[USERID],[BATCH_CODE])"
				+ "   VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, new ApplicationHelper().getGeneratedId("IA_INVENTORY_HISTORY"));
			ps.setString(2, Item_TypeCode);
			ps.setString(3, "NEW INVENTORY");
			ps.setString(4, "1");
			ps.setString(5, Warehouse_Code);
			ps.setDate(6, df.dateConvert(new java.util.Date()));
			ps.setString(7, user_id);
			ps.setString(8, "1");
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {
			System.out.println("WARNING:Error creating IA_INVENTORY_HISTORY ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	public boolean checkInventoryTotal(String Item_TypeCode,String Warehouse_Code) {

		Connection con = null;
		Statement ps = null;
		ResultSet rs = null;
		boolean done = false;
		String query = " SELECT * FROM IA_INVENTORY_TOTALS WHERE ITEM_CODE = '"+Item_TypeCode+"' AND WAREHOUSE_CODE='"+Warehouse_Code+"' ";

		try {
			con = getConnection();
			
			ps = con.createStatement();
			rs = ps.executeQuery(query);
			while (rs.next()) {
				done = true;
			}
			 
			 

		} catch (Exception e) {
			System.out.println("WARNING:Error executing Query ->"
					+ e.getMessage());
		} finally {
			closeConnection(con,ps, rs);
		}
		return done;

	}
	
	public boolean createInventoryTotal(String Item_TypeCode,String Warehouse_Code,String user_id,String compCode) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO [IA_INVENTORY_TOTALS]( [ITEM_CODE],[BALANCE], [WAREHOUSE_CODE], [USERID],[BALANCE_LTD],[BALANCE_YTD],[TMP_BALANCE],[COMP_CODE] )"
				+ "   VALUES (?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();
			ps = con.prepareStatement(query); 
			ps.setString(1, Item_TypeCode);
			ps.setString(2, "1");
			ps.setString(3, Warehouse_Code);
			ps.setString(4, user_id);
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, ""); 
			ps.setString(8, compCode);
			done = (ps.executeUpdate() != -1);

		} catch (Exception e) {e.printStackTrace();
			System.out.println("WARNING:Error creating IA_INVENTORY_TOTALS ->"
					+ e.getMessage());
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}
	
	   public Stock getAssetRecords(String inventory_id) throws Exception {

	      
	            String query = "SELECT * FROM IA_INVENTORY_APPROVE where inventory_id= '"+inventory_id+"'  ";

	            Connection con = null;
	            PreparedStatement ps = null;
	            ResultSet rs = null;
	            Stock stk =null;
	            try {
	                con =  getConnection();
	                ps = con.prepareStatement(query);
	                rs = ps.executeQuery();

	                while (rs.next()) {
	                	String INVENTORY_id = rs.getString("INVENTORY_id");
	                	String registration_no = rs.getString("REGISTRATION_NO");
	                	String branch_id = rs.getString("BRANCH_ID");
	                	String department_id = rs.getString("DEPT_ID");
	                	String category_id = rs.getString("CATEGORY_ID");
	                	String depreciation_start_date =  rs.getString("EFFECTIVE_DATE");
	                	String depreciation_end_date =  rs.getString("DEP_END_DATE");
	                	String date_purchased =  rs.getString("DATE_PURCHASED");
	                	String make = rs.getString("ASSET_MAKE");
	                	String location = rs.getString("LOCATION");
	                	String maintained_by = rs.getString("ASSET_MAINTENANCE");
	                    double accum_dep = rs.getDouble("ACCUM_DEP");
	                    String authorized_by = rs.getString("AUTHORIZED_BY");
	                    String supplied_by = rs.getString("SUPPLIER_NAME");
	                    String reason = rs.getString("PURCHASE_REASON"); 
	                    String aprovelevel = rs.getString("LEVEL_NUMBER"); 
	                    String description = rs.getString("DESCRIPTION");
	                    String vendor_account = rs.getString("VENDOR_AC");
	                    String cost_price = rs.getString("COST_PRICE");
	                    String vatable_cost = rs.getString("VATABLE_COST");
	                    String vat_amount = rs.getString("VAT");
	                    String serial_number = rs.getString("ASSET_SERIAL_NO");
	                    String model = rs.getString("ASSET_MODEL");
	                    String user = rs.getString("USER_ID");
	                    String depreciation_rate = rs.getString("DEP_RATE");
	                    String residual_value = rs.getString("RESIDUAL_VALUE");
	                    String require_depreciation = rs.getString("REQ_DEPRECIATION");
	                    String subject_to_vat = rs.getString("SUBJECT_TO_VAT");
	                    String date_of_purchase =  rs.getString("DATE_PURCHASED");
	                    String who_to_remind = rs.getString("WHO_TO_REM");
	                    String email_1 = rs.getString("EMAIL1");
	                    String email2 = rs.getString("EMAIL2");
	                    String raise_entry = rs.getString("RAISE_ENTRY");
	                    String section = rs.getString("SECTION_ID"); 
	                    String status = rs.getString("ASSET_STATUS");
	                    String section_id = rs.getString("SECTION");
	                    String wh_tax_cb = rs.getString("WH_TAX");
	                    String wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
	                    String require_redistribution = rs.getString("REQ_REDISTRIBUTION");
	                    String spare_1 = rs.getString("SPARE_1");
	                    String spare_2 = rs.getString("SPARE_2");
	                    String who_to_remind_2 = rs.getString("WHO_TO_REM_2");
	                    String driver = rs.getString("DRIVER");
	                    String state = rs.getString("STATE");
	                    String engine_number = rs.getString("ASSET_ENGINE_NO");
	                    String multiple = rs.getString("MULTIPLE");
	                    String posting_date =  rs.getString("POSTING_DATE");
	                    String warrantyStartDate =  rs.getString("WAR_START_DATE");
	                    String noOfMonths = rs.getString("WAR_MONTH");
	                    String expiryDate =  rs.getString("WAR_EXPIRY_DATE");
	                    String authuser = rs.getString("ASSET_USER");
	                    String amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
	                    String amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
	                    String partPAY = rs.getString("PART_PAY");
	                    String fullyPAID =rs.getString("FULLY_PAID");
	                    String group_id =rs.getString("GROUP_ID");
	                    String bar_code = rs.getString("BAR_CODE");
	                    String sbu_code = rs.getString("SBU_CODE");
	                    String lpo = rs.getString("LPO");
	                    String supervisor = rs.getString("SUPERVISOR");
	                    String deferPay = rs.getString("defer_pay");
	                    int selectTax = rs.getInt("wht_percent");
	                    String supervisor2 =rs.getString("supervisor2");
	            	    String supervisor3 =rs.getString("supervisor3");
	            	    String supervisor4=rs.getString("supervisor4");
	            	    String supervisor5=rs.getString("supervisor5");
	            	    String province=rs.getString("province");
	            	   String   Item_TypeCode=rs.getString("Item_TypeCode");
	                   String  Warehouse_Code=rs.getString("Warehouse_Code");
	                   String  compCode =rs.getString("comp_code");
	            	    
	            	    
	            	    
	            	    stk=new Stock
	            	    ( 
	            	     INVENTORY_id,
	            	     department_id ,
	            	     category_id ,
	                	 depreciation_start_date,
	                	 depreciation_end_date, 
	                	 date_purchased,
	                	 make , 
	                	 location ,
	                	 maintained_by,
	                	 authorized_by , 
	                	 supplied_by , 
	                	 reason ,
	                	 aprovelevel,
	                    description, 
	                    vendor_account , 
	                    cost_price, 
	                    vatable_cost, 
	                    vat_amount,
	                    serial_number ,
	                    engine_number, 
	                    model, 
	                    user , 
	                    depreciation_rate , 
	                    residual_value,
	                    subject_to_vat , 
	                    date_of_purchase,
	                    registration_no, 
	                    require_depreciation ,
	                    who_to_remind ,
	                    email_1, 
	                    email2 , 
	                    raise_entry , 
	                    section ,
	                    authuser, 
	                    state ,
	                    driver ,
	                    who_to_remind_2,
	                    spare_1,
	                    spare_2,
	                    accum_dep ,
	                    section_id ,
	                    wh_tax_cb,
	                    wh_tax_amount ,
	                    require_redistribution,
	                    status ,
	                    province,
	                    multiple ,  
	                    warrantyStartDate,
	                    noOfMonths ,
	                    expiryDate,
	                    amountPTD ,
	                    amountREM ,
	                    partPAY,
	                    fullyPAID,
	                    group_id ,
	                    authuser, 
	                    authuser,  
	                    bar_code ,
	                    sbu_code,
	                    lpo,
	                    supervisor, 
	                    deferPay ,
	                    selectTax, 
	                    supervisor2,
	                    supervisor3,
	                    supervisor4,
	                    supervisor5,
	                    Item_TypeCode,
	                     Warehouse_Code,
	                     compCode,
	                     branch_id
	                     );
	                }  

	            } 
	            catch (Exception ex)
	            {
	                System.out.println("WARN: Error fetching stock ->" + ex);
	            } 
	            finally
	            {
	                closeConnection(con, ps);
	            }
	       return stk;
	    }
	   
	   public boolean updateInventoryApprove(String inventoryId,String supervisor,String level) 
		{

			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			String query=" UPDATE IA_INVENTORY_APPROVE SET supervisor= ? ,LEVEL_NUMBER = LEVEL_NUMBER -1 WHERE  INVENTORY_ID = ? ";
			try {
				System.out.println(query); 
				con = getConnection();
				ps = con.prepareStatement(query);
				ps.setString(1, supervisor);
				ps.setString(2, inventoryId); 
				done = (ps.executeUpdate() != -1);

			} 
			catch (Exception e) 
			{
				System.out.println("WARNING:Error executing Query ->" + e.getMessage());
			    e.printStackTrace();	
			}
			finally {
				closeConnection(con, ps);
			} 
			return done;

		}
	   public boolean updateInventoryReject(String inventoryId,String level) 
		{

			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			String query = " UPDATE IA_INVENTORY_APPROVE SET LEVEL_NUMBER = ?,SUPERVISOR=?,SUPERVISOR2=?,SUPERVISOR3=?,SUPERVISOR4=?,SUPERVISOR5=? WHERE  INVENTORY_ID = ? ";
			 
			try {
			 
				con = getConnection();
				ps = con.prepareStatement(query); 
				ps.setString(1, level);
				ps.setString(2, ""); 
				ps.setString(3, ""); 
				ps.setString(4, ""); 
				ps.setString(5, ""); 
				ps.setString(6, ""); 
				ps.setString(7, inventoryId);  
				done = (ps.executeUpdate() != -1);

			} 
			catch (Exception e) 
			{
				System.out.println("WARNING:Error executing Query  ->" + e.getMessage());
			    e.printStackTrace();	
			}
			finally {
				closeConnection(con, ps);
			} 
			return done;

		}
	   
	  
		   
		   
		   public boolean createInventory(String inventory_id)
		   {

			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			 String query = "	insert into ia_inventory 	select [INVENTORY_id] ,[Registration_No] ,[Branch_ID] ,[Dept_ID] ,[Category_ID] ,[Section_id] , "
				    +"			[Description] ,[Vendor_AC] ,[Date_purchased] ,[Dep_Rate] , "
				    +"			[Asset_Make] ,[Asset_Model],[Asset_Serial_No] ,[Asset_Engine_No],[Supplier_Name], "
				    +"			[Asset_User]  ,[Asset_Maintenance],[Accum_Dep] , "
				    +"			[Monthly_Dep],[Cost_Price],[NBV],[Dep_End_Date],[Residual_Value],[Authorized_By] ,[Wh_Tax] , "
				    +"			[Wh_Tax_Amount] ,[Req_Redistribution],[Posting_Date],[Effective_Date] ,[Purchase_Reason] , "
				    +"			[Useful_Life] ,[Total_Life],[Location] ,[Remaining_Life] ,[Vatable_Cost] ,[Vat], "
				    +"			[Req_Depreciation],[Subject_TO_Vat],[Who_TO_Rem],[Email1],[Who_To_Rem_2] ,[Email2] , "
				    +"			[Raise_Entry] ,[Dep_Ytd],[Section] ,[Asset_Status],[State] ,[Driver] , "
				    +"			[Spare_1] ,[Spare_2] ,[User_ID] ,[Date_Disposed],[PROVINCE],[Multiple] , "
				    +"			[WAR_START_DATE] ,[WAR_MONTH],[WAR_EXPIRY_DATE] ,[Last_Dep_Date],[BRANCH_CODE] , "
				    +"			[SECTION_CODE],[DEPT_CODE] ,[CATEGORY_CODE] ,[AMOUNT_PTD],[AMOUNT_REM] , "
				    +"			[PART_PAY] ,[FULLY_PAID] ,[GROUP_ID],[BAR_CODE] ,[SBU_CODE], "
				    +"			[LPO] ,[supervisor],[defer_pay] ,[OLD_ASSET_ID],[WHT_PERCENT], "
				    +"			[Post_reject_reason] ,[Finacle_Posted_Date],[Item_TypeCode],[Warehouse_Code],[COMP_CODE]  "
				    +"			from ia_inventory_approve where inventory_id='"+inventory_id+"' " ;
				   

			try {
				con = getConnection();
				ps = con.prepareStatement(query); 
				done = (ps.executeUpdate() != -1);

			} catch (Exception e) {e.printStackTrace();
				System.out.println("WARNING:Error creating IA_INVENTORY ->"
						+ e.getMessage());
			} finally {
				closeConnection(con, ps);
			}
			return done;

		}
		   public java.util.ArrayList getInventoryApproveRecords(String userId) throws Exception {

			   java.util.ArrayList _list = new java.util.ArrayList(); 
	            String query = " select * from ia_inventory_approve where supervisor = '"+userId+"' and inventory_id not in (select inventory_id from ia_inventory)  ";
   
	           
				System.out.println(query);
				
	            Connection con = null;
	            PreparedStatement ps = null;
	            ResultSet rs = null;
	            Stock stk =null;
	            
	            try {
	                con =  getConnection();
	                ps = con.prepareStatement(query);
	                rs = ps.executeQuery();

	                while (rs.next()) {
	                	String INVENTORY_id = rs.getString("INVENTORY_id"); 
	                	String registration_no = rs.getString("REGISTRATION_NO");
	                	String branch_id = rs.getString("BRANCH_ID");
	                	String department_id = rs.getString("DEPT_ID");
	                	String category_id = rs.getString("CATEGORY_ID");
	                	String depreciation_start_date =  rs.getString("EFFECTIVE_DATE");
	                	String depreciation_end_date =  rs.getString("DEP_END_DATE");
	                	String date_purchased =  rs.getString("DATE_PURCHASED");
	                	String make = rs.getString("ASSET_MAKE");
	                	String location = rs.getString("LOCATION");
	                	String maintained_by = rs.getString("ASSET_MAINTENANCE");
	                    double accum_dep = rs.getDouble("ACCUM_DEP");
	                    String authorized_by = rs.getString("AUTHORIZED_BY");
	                    String supplied_by = rs.getString("SUPPLIER_NAME");
	                    String reason = rs.getString("PURCHASE_REASON"); 
	                    String aprovelevel = rs.getString("LEVEL_NUMBER"); 
	                    String description = rs.getString("DESCRIPTION");
	                    String vendor_account = rs.getString("VENDOR_AC");
	                    String cost_price = rs.getString("COST_PRICE");
	                    String vatable_cost = rs.getString("VATABLE_COST");
	                    String vat_amount = rs.getString("VAT");
	                    String serial_number = rs.getString("ASSET_SERIAL_NO");
	                    String model = rs.getString("ASSET_MODEL");
	                    String user = rs.getString("USER_ID");
	                    String depreciation_rate = rs.getString("DEP_RATE");
	                    String residual_value = rs.getString("RESIDUAL_VALUE");
	                    String require_depreciation = rs.getString("REQ_DEPRECIATION");
	                    String subject_to_vat = rs.getString("SUBJECT_TO_VAT");
	                    String date_of_purchase =  rs.getString("DATE_PURCHASED");
	                    String who_to_remind = rs.getString("WHO_TO_REM");
	                    String email_1 = rs.getString("EMAIL1");
	                    String email2 = rs.getString("EMAIL2");
	                    String raise_entry = rs.getString("RAISE_ENTRY");
	                    String section = rs.getString("SECTION_ID"); 
	                    String status = rs.getString("ASSET_STATUS");
	                    String section_id = rs.getString("SECTION");
	                    String wh_tax_cb = rs.getString("WH_TAX");
	                    String wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
	                    String require_redistribution = rs.getString("REQ_REDISTRIBUTION");
	                    String spare_1 = rs.getString("SPARE_1");
	                    String spare_2 = rs.getString("SPARE_2");
	                    String who_to_remind_2 = rs.getString("WHO_TO_REM_2");
	                    String driver = rs.getString("DRIVER");
	                    String state = rs.getString("STATE");
	                    String engine_number = rs.getString("ASSET_ENGINE_NO");
	                    String multiple = rs.getString("MULTIPLE");
	                    String posting_date =  rs.getString("POSTING_DATE");
	                    String warrantyStartDate =  rs.getString("WAR_START_DATE");
	                    String noOfMonths = rs.getString("WAR_MONTH");
	                    String expiryDate =  rs.getString("WAR_EXPIRY_DATE");
	                    String authuser = rs.getString("ASSET_USER");
	                    String amountPTD =String.valueOf(rs.getDouble("AMOUNT_PTD"));
	                    String amountREM =String.valueOf(rs.getDouble("AMOUNT_REM"));
	                    String partPAY = rs.getString("PART_PAY");
	                    String fullyPAID =rs.getString("FULLY_PAID");
	                    String group_id =rs.getString("GROUP_ID");
	                    String bar_code = rs.getString("BAR_CODE");
	                    String sbu_code = rs.getString("SBU_CODE");
	                    String lpo = rs.getString("LPO");
	                    String supervisor = rs.getString("SUPERVISOR");
	                    String deferPay = rs.getString("defer_pay");
	                    int selectTax = rs.getInt("wht_percent");
	                    String supervisor2 =rs.getString("supervisor2");
	            	    String supervisor3 =rs.getString("supervisor3");
	            	    String supervisor4=rs.getString("supervisor4");
	            	    String supervisor5=rs.getString("supervisor5");
	            	    String province=rs.getString("province");
	            	    String   Item_TypeCode=rs.getString("Item_TypeCode");
	            	    String  Warehouse_Code=rs.getString("Warehouse_Code");
	            	    String  compCode =rs.getString("comp_code");
	            	    
	            	    
	            	    
	            	    stk=new Stock
	            	    ( 
	            	     INVENTORY_id,
	            	     department_id ,
	            	     category_id ,
	                	 depreciation_start_date,
	                	 depreciation_end_date, 
	                	 date_purchased,
	                	 make , 
	                	 location ,
	                	 maintained_by,
	                	 authorized_by , 
	                	 supplied_by , 
	                	 reason ,
	                	 aprovelevel,
	                    description, 
	                    vendor_account , 
	                    cost_price, 
	                    vatable_cost, 
	                    vat_amount,
	                    serial_number ,
	                    engine_number, 
	                    model, 
	                    user , 
	                    depreciation_rate , 
	                    residual_value,
	                    subject_to_vat , 
	                    date_of_purchase,
	                    registration_no, 
	                    require_depreciation ,
	                    who_to_remind ,
	                    email_1, 
	                    email2 , 
	                    raise_entry , 
	                    section ,
	                    authuser, 
	                    state ,
	                    driver ,
	                    who_to_remind_2,
	                    spare_1,
	                    spare_2,
	                    accum_dep ,
	                    section_id ,
	                    wh_tax_cb,
	                    wh_tax_amount ,
	                    require_redistribution,
	                    status ,
	                    province,
	                    multiple ,  
	                    warrantyStartDate,
	                    noOfMonths ,
	                    expiryDate,
	                    amountPTD ,
	                    amountREM ,
	                    partPAY,
	                    fullyPAID,
	                    group_id ,
	                    authuser, 
	                    authuser,  
	                    bar_code ,
	                    sbu_code,
	                    lpo,
	                    supervisor, 
	                    deferPay ,
	                    selectTax, 
	                    supervisor2,
	                    supervisor3,
	                    supervisor4,
	                    supervisor5,
	                    Item_TypeCode,
	                     Warehouse_Code,
	                     compCode,
	                     branch_id
	                     );
	            	    _list.add(stk);
	                }   
	                
	            }  
	            catch (Exception ex)
	            {
	                System.out.println("WARN: Error fetching stock ->" + ex);
	                ex.printStackTrace();
	            } 
	            finally
	            {
	                closeConnection(con, ps);
	            }
	       return _list;
	    }	
		   public boolean createApprovalRemark(ApprovalRemark remark)
		   {

			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;
			 String query = "  insert into IA_Approval_Remark(Id,supervisorID,DateRequisitioned ,Remark,status,ApprovalLevel,IPAddress)  values(?,?,?,?,?,?,?)" ;
				   

			try { 
				con = getConnection();
				ps = con.prepareStatement(query); 
				ps.setString(1, remark.getId());
				ps.setString(2, remark.getSupervisorID());
				ps.setString(3, remark.getDateRequisitioned());
				ps.setString(4, remark.getRemark());
				ps.setString(5, remark.getStatus());
				ps.setString(6, remark.getApprovalLevel());
				ps.setString(7, remark.getIPAddress());
				done = (ps.executeUpdate() != -1);

			} catch (Exception e) {e.printStackTrace();
				System.out.println("WARNING:Error creating  IA_Approval_Remark ->" + e.getMessage());
			} finally {
				closeConnection(con, ps);
			}
			return done;

		}
}
