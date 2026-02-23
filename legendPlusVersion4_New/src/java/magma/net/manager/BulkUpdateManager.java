/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magma.net.manager;

/**
 *
 * @author Ganiyu
 *
 *
 */
import magma.asset.dto.Improvement;
import magma.net.dao.MagmaDBConnection;

import java.sql.*;

import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;

import java.util.ArrayList;

import magma.net.vao.Asset;
import magma.net.vao.BranchVisit;
import magma.net.vao.FMppmAllocation;

import com.magbel.util.HtmlUtility;

import magma.AssetRecordsBean;
import magma.GroupAssetToAssetBean;
import magma.GroupInventoryBean;
import magma.AssetRecordsBean;
import magma.StockRecordsBean;
public class BulkUpdateManager extends legend.ConnectionClass {

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    HtmlUtility htmlUtil = null; 
    private AssetRecordsBean ad;
    private String asset_id_new="";
    public GroupInventoryBean adGroup;
    public ApprovalRecords approvalRec;
    public AssetRecordsBean arb;
    public ApprovalManager approverManager;
    public GroupAssetToAssetBean grpAsset;
    public EmailSmsServiceBus mail;
    
    private String upd_am_stock_status_RaiseEntry="update am_stock set asset_status='APPROVED' , raise_entry='Y' where group_id = ";
    private String upd_am_grp_stock_RaiseEntry="update am_group_stock set raise_entry='Y' where group_id = ";
    private String upd_am_grp_stock_main_RaiseEntry="update am_group_stock_main set raise_entry='Y' where group_id = ";
    private String upd_am_stock_status_RaiseEntry_Archive="update ST_STOCK_ARCHIVE set asset_status='APPROVED' , raise_entry='Y' where group_id = ";
    private String upd_am_grp_stock_RaiseEntry_Archive="update am_group_stock_archive set raise_entry='Y' where group_id = ";
    private String upd_am_grp_stock_main_RaiseEntry_Archive="update am_group_stock_main_archive set raise_entry='Y' where group_id = ";

    
    public BulkUpdateManager() throws Exception {

        try {  
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            htmlUtil = new HtmlUtility();
            ad = new AssetRecordsBean();
            approvalRec = new ApprovalRecords();
            adGroup = new GroupInventoryBean(); 
            arb = new AssetRecordsBean();
            mail = new EmailSmsServiceBus();
            approverManager = new ApprovalManager();
            grpAsset = new GroupAssetToAssetBean();
        } catch (Exception ex) {
        }
    }

    public boolean insertBulkUpdate(ArrayList list, int bacthId) {
        boolean re = false;

        String query = "insert into am_gb_bulkupdate ( " +
                "REGISTRATION_NO,Description,VENDOR_AC," +
                "ASSET_MODEL,ASSET_SERIAL_NO," +
                "ASSET_ENGINE_NO,SUPPLIER_NAME," +
                "ASSET_USER,ASSET_MAINTENANCE," +
                "AUTHORIZED_BY,PURCHASE_REASON,SBU_CODE," +
                "SPARE_1,SPARE_2,SPARE_3,SPARE_4,SPARE_5,SPARE_6,BAR_CODE, ASSET_ID,BATCH_ID,BRANCH_ID,DEPT_ID,SUB_CATEGORY_ID,LOCATION )" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.AssetRecordsBean bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.AssetRecordsBean) list.get(i);

                String registration = bd.getRegistration_no();
                String description = bd.getDescription();
                String vendoracc = bd.getVendor_account();
                String model1 = bd.getModel();
                String serial = bd.getSerial_number();
                String engine = bd.getEngine_number();
                int suppliedby = bd.getSupplied_by() == null ? 0 : Integer.parseInt(bd.getSupplied_by());
                String assetuser = bd.getUser();
                int maintained = bd.getMaintained_by() == null ? 0 : Integer.parseInt(bd.getMaintained_by());
                String authorized = bd.getAuthorized_by();
                String reason1 = bd.getReason();
                String sbu = bd.getSbu_code();
                String dept = bd.getDepartment_id();
                String spare1 = bd.getSpare_1();
                String spare2 = bd.getSpare_2();
                String spare3 = bd.getSpare_3();
                String spare4 = bd.getSpare_4();
                String spare5 = bd.getSpare_5();
                String spare6 = bd.getSpare_6();                
                String barcode = bd.getBar_code();
                String lpo1 = bd.getLpo();
                String asset_id1 = bd.getAsset_id();
                String branchId = bd.getBranch_id();
                String subcat = bd.getSub_category_id();
                int location = Integer.parseInt(bd.getLocation());

                if (registration == null || registration.equals("")) {
                    registration = "0";
                }
                if (description == null || description.equals("")) {
                    description = "";
                }
                if (vendoracc == null || vendoracc.equals("")) {
                    vendoracc = "0";
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
                //if (suppliedby == null || suppliedby.equals("")) {
                //    suppliedby = "0";
                // }
                if (assetuser == null || assetuser.equals("")) {
                    assetuser = "0";
                }
                //if (maintained == null) {
                //    maintained = 0;
                //}
                if (authorized == null || authorized.equals("")) {
                    authorized = "0";
                }
                if (reason1 == null || reason1.equals("")) {
                    reason1 = "0";
                }
                if (sbu == null || sbu.equals("")) {
                    sbu = "0";
                }
                if (dept == null || dept.equals("")) {
                	dept = "0";
                }                
                if (subcat == null || subcat.equals("")) {
                    subcat = "0";
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
                if (branchId == null || branchId.equals("")) {
                    branchId = "0";
                }

                ps.setString(1, registration);
                ps.setString(2, description);
                ps.setString(3, vendoracc);
                ps.setString(4, model1);
                ps.setString(5, serial);
                ps.setString(6, engine);
                ps.setInt(7, suppliedby);
                ps.setString(8, assetuser);
                ps.setInt(9, maintained);
                ps.setString(10, authorized);
                ps.setString(11, reason1);
                ps.setString(12, sbu);
                ps.setString(13, spare1);
                ps.setString(14, spare2);
                ps.setString(15, spare3);
                ps.setString(16, spare4);
                ps.setString(17, spare5);
                ps.setString(18, spare6);
                ps.setString(19, barcode);
                ps.setString(20, asset_id1);
                ps.setInt(21, bacthId);
                ps.setString(22, branchId);
                ps.setString(23, dept);
                ps.setString(24, subcat);
                ps.setInt(25, location);

                ps.addBatch();
            }
            d = ps.executeBatch();
            //System.out.println("Executed Successfully ");


        } catch (Exception ex) {
            System.out.println("Error insertBulkUpdate() of BulkUpdateManager -> " + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public int insertBulkTransfer(ArrayList list, int bacthId,int categoryId,String newassetId) {
        boolean re = false;
        int assetCode = 0;
        String query = "insert into am_gb_bulktransfer ( " +
                "ASSET_ID,Description,OLDBRANCH_ID," +
                "OLDDEPT_ID,OLDSBU_CODE," +
                "OLDSECTION_ID,OLDASSET_USER," +
                "NEWBRANCH_ID,NEWDEPT_ID,NEWSBU_CODE,NEWSECTION_ID,NEWASSET_USER," +
                "BATCH_ID,TRANSFERBY_ID,CATEGORY_ID,NEW_ASSET_ID,COST_PRICE,ACCUM_DEP,MONTHLY_DEP," +
                "NBV,TRANSFER_DATE,ASSET_CODE,NEW_BRANCH_CODE,NEW_SECTION_CODE,NEW_DEPT_CODE,Registration_No)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        
        //ResultSet rs = null;
        magma.AssetRecordsBean bd = null;
        int[] d = null;
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.AssetRecordsBean) list.get(i);
                String asset_id = bd.getAsset_id();
                newassetId = bd.getNewasset_id();
                String description = bd.getDescription();
                String branchId = bd.getBranch_id();
                String dept = bd.getDepartment_id();
                String sbu = bd.getSbu_code();
                String section = bd.getSection_id();
                String assetuser = bd.getUser();
                String newbranchId = bd.getNewbranch_id();
                String newdept = bd.getNewdepartment_id();
                String newsbu = bd.getNewsbu_code();
                String newsection = bd.getNewsection_id();
                String newassetuser = bd.getNewuser();
                String cost_price = bd.getCost_price();
                String accum_dep = bd.getAccumdep();
                String nbv = bd.getNbv();
                String Registration_No = bd.getRegistration_no();
                assetCode = bd.getAssetCode();
                int category_Id = Integer.parseInt(bd.getCategory_id());
                String monthly_dep = bd.getMonthlydep();
                
                String dept_code = htmlUtil.findObject("SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+newdept+"'");
                String section_code = htmlUtil.findObject("SELECT section_code FROM   am_ad_section WHERE Section_ID = '"+newsection+"'");
                String branch_code = htmlUtil.findObject("SELECT branch_code FROM   am_ad_branch WHERE Branch_Id = '"+newbranchId+"'");
                int transferby = bd.getUser_id() == null ? 0 : Integer.parseInt(bd.getUser_id());
                if (branchId == null || branchId.equals("")) {
                	branchId = "0";
                }
                if (description == null || description.equals("")) {
                    description = "";
                }
                if (section == null || section.equals("")) {
                	section = "0";
                }
                if (newbranchId == null || newbranchId.equals("")) {
                	newbranchId = "0";
                }
                if (newdept == null || newdept.equals("")) {
                	newdept = "0";
                }
                if (newsbu == null || newsbu.equals("")) {
                	newsbu = "0";
                }
                if (assetuser == null || assetuser.equals("")) {
                    assetuser = "0";
                }
                if (newsection == null || newsection.equals("")) {
                	newsection = "0";
                }
                if (newassetuser == null || newassetuser.equals("")) {
                	newassetuser = "0";
                }
                if (sbu == null || sbu.equals("")) {
                    sbu = "0";
                }
                if (dept == null || dept.equals("")) {
                	dept = "0";
                }                
                if (asset_id == null || asset_id.equals("")) {
                    asset_id = "0";
                }
                if (newbranchId == null || branchId.equals("")) {
                    newbranchId = "0";
                }
//                System.out.println("InsertBulkTransfer branchId: "+branchId);
                ps.setString(1, asset_id);
                ps.setString(2, description);
                ps.setString(3, branchId);
                ps.setString(4, dept);  
                ps.setString(5, sbu);
                ps.setString(6, section);
                ps.setString(7, assetuser);
                ps.setString(8, newbranchId);
                ps.setString(9, newdept);
                ps.setString(10, newsbu);
                ps.setString(11, newsection);
                ps.setString(12, newassetuser);
                ps.setString(13, String.valueOf(bacthId));
                ps.setInt(14, transferby);
                ps.setInt(15, category_Id);
                ps.setString(16, newassetId);
                ps.setString(17, cost_price);
                ps.setString(18, accum_dep);
                ps.setString(19, monthly_dep);
                ps.setString(20, nbv);
                ps.setTimestamp(21, dbConnection.getDateTime(new java.util.Date()));
                ps.setInt(22, assetCode);
                ps.setString(23, branch_code);
                ps.setString(24, section_code);
                ps.setString(25, dept_code);
                ps.setString(26, Registration_No);
                ps.addBatch();
            }
            d = ps.executeBatch();

//            System.out.println("Executed Successfully ");


        } catch (Exception ex) {
            System.out.println("Error insertBulkTransfer() of BulkUpdateManager with Array -> " + ex);
        } 
 //       System.out.println("InsertBulkTransfer assetCode: "+assetCode);
        return assetCode;
    }
    
   
    
    public boolean setBatchId(String batchId) throws Exception {

        String query = "update am_asset_approval set asset_id=? where transaction_id=? ";

       
        boolean done = true;
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, batchId);
            ps.setInt(2, Integer.parseInt(batchId));

            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("BulkUpdateManager: setBatchId():WARN:Error updating to am_asset_approval table->" + ex);
        } 
        return done;   
    }

    public ArrayList[] findAssetByBatchId(String tranId) {


        String assetFilter = " and asset_id in (";
        String selectQuery =
                "select * from am_gb_bulkupdate where batch_id=? order by asset_id ";

        

        ResultSet rs = null;
        ArrayList listNew = new ArrayList();
        ArrayList listOld = new ArrayList();
        ArrayList[] listNewOld = new ArrayList[2];
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            Asset aset = null;
            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String vendorAC = rs.getString("Vendor_AC");
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                int SupplierName = rs.getInt("Supplier_Name");
                String assetUser = rs.getString("ASSET_USER");
                int assetMaintenance = rs.getInt("Asset_Maintenance");
                String authorizedBy = rs.getString("Authorized_By");
                String purchaseReason = rs.getString("Purchase_Reason");
                int location = rs.getInt("LOCATION");
                String sbuCode = rs.getString("SBU_CODE");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");
                String barCode = rs.getString("BAR_CODE");
                String branchId = rs.getString("branch_id");
                String deptId = rs.getString("dept_id");
                String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                
                 aset = new Asset();
                aset.setId(id);
                aset.setDescription(description);
                aset.setRegistrationNo(registrationNo);
                aset.setVendorAc(vendorAC);
                aset.setAssetMake(assetModel);
                aset.setSerialNo(AssetSerialNo);
                aset.setEngineNo(AssetEngineNo);
                aset.setSupplierName(SupplierName);
                aset.setLocation(location);
                aset.setAssetUser(assetUser);
                aset.setAssetMaintain(assetMaintenance);
                aset.setAuthorizeBy(authorizedBy);
                aset.setPurchaseReason(purchaseReason);
                aset.setSbuCode(sbuCode);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);
                aset.setDepartmentId(deptId);
                aset.setSubcategoryId(subcategoryId);
                
                listNew.add(aset);
                aset=null;
                assetFilter = assetFilter + "'" + id + "',";

            }

            boolean index = assetFilter.endsWith(",");
            if (index) {
                assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
            }
            assetFilter = assetFilter + ")";
//            System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
            listOld = findAssetByID(assetFilter+" order by ASSET_ID");
        } catch (Exception e) {
            System.out.println("INFO:Error findAssetByBatchId() in BulkUpdateManager-> " +
                    e.getMessage());
        } 
        System.out.println("the size of listNew is >>>>>> " + listNew.size());
        listNewOld[0] = listNew;
        listNewOld[1] = listOld;
        Asset assNew = null;
        Asset assOld = null;

        return listNewOld;

    }

    public ArrayList findAssetByID(String queryFilter) {



        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO," +
                " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model,COST_PRICE,Asset_Make," +
                "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason,Date_purchased," +
                "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID," +
                "Section_id,sub_category_ID,sub_category_code,Location,state,accum_dep " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
//        System.out.println("the selectQuery  in findAssetByID is <<<<<<<<<<<<< " + selectQuery);

       
        ResultSet rs = null;

        ArrayList listOld = new ArrayList();

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);

            rs = ps.executeQuery();
            Asset aset =null;
            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String vendorAC = rs.getString("Vendor_AC");
                String assetMake = rs.getString("Asset_Make");
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                int SupplierName = rs.getInt("Supplier_Name");
                String assetUser = rs.getString("ASSET_USER");
                int assetMaintenance = rs.getInt("Asset_Maintenance");
                String authorizedBy = rs.getString("Authorized_By");
                String purchaseReason = rs.getString("Purchase_Reason");
                String sbuCode = rs.getString("SBU_CODE");
                String deptCode = rs.getString("DEPT_CODE"); 
                String deptid = rs.getString("Dept_ID");                 
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");
                String barCode = rs.getString("BAR_CODE");
                double costPrice = rs.getDouble("COST_PRICE");
                double accumDep = rs.getDouble("ACCUM_DEP");
                String purchaseDate = rs.getString("Date_purchased");
                String branchId = String.valueOf(rs.getInt("branch_id"));
                String subcategoryCode = rs.getString("sub_category_code"); 
                String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
                
                String departmentId = rs.getString("Dept_ID");
                String sectionId = rs.getString("Section_id");
                int Location = rs.getInt("Location");
                int state = rs.getInt("State");   
                 aset = new Asset();
                aset.setId(id);
                aset.setDescription(description);
                aset.setRegistrationNo(registrationNo);
                aset.setVendorAc(vendorAC);
                aset.setAssetMake(assetModel);
                aset.setSerialNo(AssetSerialNo);
                aset.setEngineNo(AssetEngineNo);
                aset.setSupplierName(SupplierName);
                aset.setAssetUser(assetUser);
                aset.setAssetMaintain(assetMaintenance);
                aset.setAuthorizeBy(authorizedBy);
                aset.setPurchaseReason(purchaseReason);
                aset.setSbuCode(sbuCode);
                aset.setDepcode(deptCode);
                aset.setDepartmentId(deptid);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);     
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);
                aset.setSubcategoryCode(subcategoryCode);
                aset.setSubcategoryId(subcategoryId);
                aset.setCost(costPrice);
                aset.setDatePurchased(purchaseDate);
                aset.setAssetMake(assetMake);
                aset.setLocation(Location);
                aset.setState(state);
                aset.setSectionId(sectionId);
                aset.setDepartmentId(departmentId);
                aset.setCost(costPrice);
                aset.setAccumulatedDepreciation(accumDep);
                listOld.add(aset);
                aset = null;

            }



        } catch (Exception e) {
            System.out.println("INFO:Error findAssetByID() in BulkUpdateManager-> " +
                    e.getMessage());
        } 
 //       System.out.println("the size of listOld is >>>>>>> " + listOld.size());
        return listOld;

    }

    public ArrayList findAssetProofByID(String queryFilter,String tranId) {

        String selectQuery =
                "SELECT ASSET_ID,OLD_ASSET_ID,REGISTRATION_NO," +
                " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model,COST_PRICE,Asset_Make," +
                "Monthly_Dep,Accum_Dep,NBV,IMPROV_COST,IMPROV_ACCUMDEP,IMPROV_MONTHLYDEP,IMPROV_NBV,TOTAL_NBV,"+
                "CATEGORY_ID,Section_id,Location,State,Vendor_AC,LPO,EFFECTIVE_DATE,DEP_END_DATE,Section_id,Location,State,"+
                "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason,Date_purchased," +
                "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
        System.out.println("the query in findAssetProofByID is <<<<<<<<<<<<< " + selectQuery);

        String groupNoQuery =
                "select * from am_Asset_Proof where batch_id=? and group_no = 2 order by group_no,asset_id ";
     //   System.out.println("the query in findAssetProofByID is <<<<<<<<<<<<< " + groupNoQuery);
       

      

        ArrayList listOld = new ArrayList();

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);

            ResultSet rs = ps.executeQuery();
            Asset aset =null;
            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String oldAssetId = rs.getString("OLD_ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String vendorAC = rs.getString("Vendor_AC");
                String assetMake = rs.getString("Asset_Make");
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                int SupplierName = rs.getInt("Supplier_Name");
                String assetUser = rs.getString("ASSET_USER");
                int assetMaintenance = rs.getInt("Asset_Maintenance");
                String authorizedBy = rs.getString("Authorized_By");
                String purchaseReason = rs.getString("Purchase_Reason");
                String lpo = rs.getString("LPO");
                String vendorAccount = rs.getString("Vendor_AC");
                String sbuCode = rs.getString("SBU_CODE");
                String deptCode = rs.getString("DEPT_CODE"); 
                String deptid = rs.getString("Dept_ID");                 
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");
                String barCode = rs.getString("BAR_CODE");
                double costPrice = rs.getDouble("COST_PRICE");
                double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                double nbv = rs.getDouble("NBV");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improvTotalNbv = rs.getDouble("TOTAL_NBV");                
                String purchaseDate = rs.getString("Date_purchased");
                String branchId = String.valueOf(rs.getInt("branch_id"));
                String subcategoryCode = rs.getString("sub_category_code"); 
                String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
 //               String departmentId = rs.getString("Dept_ID");
                int CategoryId = rs.getInt("CATEGORY_ID");
                String sectionId = rs.getString("Section_id");
                int Location = rs.getInt("Location");
                int state = rs.getInt("State");
                String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                
                 aset = new Asset();
                aset.setId(id);
                aset.setOLD_ASSET_ID(oldAssetId);
                aset.setDescription(description);
                aset.setRegistrationNo(registrationNo);
                aset.setVendorAc(vendorAC);
                aset.setAssetMake(assetModel);
                aset.setSerialNo(AssetSerialNo);
                aset.setEngineNo(AssetEngineNo);
                aset.setSupplierName(SupplierName);
                aset.setAssetUser(assetUser);
                aset.setAssetMaintain(assetMaintenance);
                aset.setAuthorizeBy(authorizedBy);
                aset.setPurchaseReason(purchaseReason);
                aset.setSbuCode(sbuCode);
                aset.setDepcode(deptCode);
                aset.setDepartmentId(deptid);
                aset.setVendorAc(vendorAccount);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);                
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);
                aset.setSubcategoryCode(subcategoryCode);
                aset.setSubcategoryId(subcategoryId);
                aset.setCost(costPrice);
                aset.setMonthlyDepreciation(monthlyDepreciation);
                aset.setAccumulatedDepreciation(accumulatedDepreciation);
                aset.setNbv(nbv);  
                aset.setImprovcost(improvcost);
                aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                aset.setImprovnbv(improvnbv);
                aset.setImprovTotalNbv(improvTotalNbv);
                aset.setDatePurchased(purchaseDate);
                aset.setAssetMake(assetMake);
                aset.setBranchId(branchId);
                aset.setCategoryId(CategoryId);
                aset.setSectionId(sectionId);
                aset.setLocation(Location);
                aset.setState(state);
                aset.setLPO(lpo);
                aset.setDepreciationEndDate(depreciation_end_date);
                aset.setEffectivedate2(depreciation_start_date);
    //            System.out.println("<<<<<<<===sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                listOld.add(aset);
                aset = null;

            }
/*            ps = con.prepareStatement(groupNoQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String oldassetId = rs.getString("OLD_ASSET_ID");
                String description = rs.getString("DESCRIPTION");
                String barCode = rs.getString("BAR_CODE");
                String assetuser = rs.getString("ASSET_USER");
                String comments = rs.getString("COMMENTS");
                String sighted = rs.getString("ASSETSIGHTED");
                String functional = rs.getString("ASSETFUNCTION");
                int assetcode = rs.getInt("ASSET_CODE");
                String branchCode = rs.getString("BRANCH_ID");
                String categoryId = rs.getString("CATEGORY_ID");
                String sbuCode = rs.getString("SBU_CODE");
                String model = rs.getString("Asset_Model");
                String make = rs.getString("Asset_Make");
                String vendorAccount = rs.getString("Vendor_AC");
                String registrationNo = rs.getString("Registration_No");
                double costPrice = rs.getDouble("Cost_Price");
                String serialNo = rs.getString("Asset_Serial_No");
                String engineNo = rs.getString("Asset_Engine_No");
                String purchaseDate = rs.getString("Date_purchased");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");   
                double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                double nbv = rs.getDouble("NBV");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improvTotalNbv = rs.getDouble("TOTAL_NBV");                
                String branchId = String.valueOf(rs.getInt("branch_id"));
//                String subcategoryCode = rs.getString("sub_category_code"); 
                String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
 //               String departmentId = rs.getString("Dept_ID");
                int CategoryId = rs.getInt("CATEGORY_ID");
                String sectionId = rs.getString("Section_id");
                int Location = rs.getInt("Location");
                int state = rs.getInt("State");
                String lpo = rs.getString("LPO");
                String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                   
                aset = new Asset();
                aset.setId(id);
                aset.setOLD_ASSET_ID(oldassetId);
                aset.setDescription(description);
                aset.setBarCode(barCode);
                aset.setSbuCode(sbuCode);
                aset.setAssetUser(assetuser);
                aset.setComments(comments);
                aset.setAssetsighted(sighted);
                aset.setAssetfunction(functional);
                aset.setAssetCode(assetcode);
                aset.setBranchCode(branchCode);
                aset.setSubcategoryId(subcategoryId);
                aset.setCost(costPrice);
                aset.setAssetmodel(model);
                aset.setAssetMake(make);
                aset.setEngineNo(engineNo);
                aset.setRegistrationNo(registrationNo);
                aset.setVendorAc(vendorAccount);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);
                aset.setSerialNo(serialNo);
                aset.setDatePurchased(purchaseDate);
                aset.setCategoryId(Integer.parseInt(categoryId));
                aset.setMonthlyDepreciation(monthlyDepreciation);
                aset.setAccumulatedDepreciation(accumulatedDepreciation);
                aset.setNbv(nbv);  
                aset.setImprovcost(improvcost);
                aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                aset.setImprovnbv(improvnbv);
                aset.setImprovTotalNbv(improvTotalNbv);
                aset.setDatePurchased(purchaseDate);
                aset.setBranchId(branchId);
                aset.setCategoryId(CategoryId);
                aset.setSectionId(sectionId);
                aset.setLocation(Location);
                aset.setState(state);
                aset.setLPO(lpo);
                aset.setDepreciationEndDate(depreciation_end_date);
                aset.setEffectivedate2(depreciation_start_date);                
                listOld.add(aset);
                aset=null;

            }

*/

        } catch (Exception e) {
            System.out.println("INFO:Error findAssetProofByID() in BulkUpdateManager-> " +
                    e.getMessage());
        } 
 //       System.out.println("the size of listOld is >>>>>>> " + listOld.size());
        return listOld;

    }



    public boolean updateBulkAsset(ArrayList list) {

        ArrayList newList = null;
        Asset asset = null;
        
        htmlUtil = new HtmlUtility();
         
        int[] d =null;
        String query= "UPDATE AM_ASSET SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,Spare_3=?,Spare_4=?,Spare_5=?,Spare_6=?," +
                "BAR_CODE=?, DEPT_ID=?, DEPT_CODE=?, SUB_CATEGORY_ID=?, SUB_CATEGORY_CODE=? WHERE ASSET_ID=?" ;

        try {
        	 Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query);
             
             for (int i = 0; i < list.size(); ++i) {
        asset = (Asset)list.get(i);
                String assetId = asset.getId();
//                System.out.println("the asset id in bulk update manager is >>>>>>>>>>> " + assetId);
                String registrationNo = asset.getRegistrationNo();
                String description = asset.getDescription();
                String vendorAC = asset.getVendorAc();
                String assetModel = asset.getAssetMake();
                String AssetSerialNo = asset.getSerialNo();
                String AssetEngineNo = asset.getEngineNo();
                int SupplierName = asset.getSupplierName();
                String assetUser = asset.getAssetUser();
                int assetMaintenance = asset.getAssetMaintain();
                String authorizedBy = asset.getAuthorizeBy();
                String purchaseReason = asset.getPurchaseReason();
                String sbuCode = asset.getSbuCode();
                String spare1 = asset.getSpare1();
                String spare2 = asset.getSpare2(); 
                String spare3 = asset.getSpare3();
                String spare4 = asset.getSpare4(); 
                String spare5 = asset.getSpare5();
                String spare6 = asset.getSpare6(); 
                String deptId = asset.getDepartmentId();
                String subcategoryId = asset.getSubcategoryId();
 //               String subcategoryCode = asset.getSubcategoryCode();
                String deptQuery = "SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+deptId+"'";
                String dept_code = htmlUtil.findObject(deptQuery);
                String subcatQuery = "SELECT sub_category_code FROM   am_ad_sub_category WHERE sub_category_id = '"+subcategoryId+"'";
                String subcategoryCode = htmlUtil.findObject(subcatQuery);
                String barCode = asset.getBarCode();
                 
                asset=null;
               
             ps.setString(1,registrationNo);
             ps.setString(2,description);
             ps.setString(3,assetUser);
             ps.setInt(4,assetMaintenance);
             ps.setString(5,vendorAC);
             ps.setString(6,assetModel);
             ps.setString(7,AssetSerialNo);
             ps.setString(8,AssetEngineNo);
             ps.setInt(9,SupplierName);
             ps.setString(10,authorizedBy);
             ps.setString(11,purchaseReason);
             ps.setString(12,sbuCode);
             ps.setString(13,spare1);
             ps.setString(14,spare2);
             ps.setString(15,spare3);
             ps.setString(16,spare4);
             ps.setString(17,spare5);
             ps.setString(18,spare6);
             ps.setString(19,barCode);
             ps.setString(20,deptId);
             ps.setString(21,dept_code);
             ps.setString(22,subcategoryId);
             ps.setString(23,subcategoryCode);
             ps.setString(24,assetId);
            // ps.addBatch();
            ps.execute();
             }
            d=ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("BulkUpdateManager: updateBulkAsset()>>>>>" + ex);
        } 
            return (d.length > 0);
    }


    public boolean updateBulkAsset2(ArrayList list) {

        ArrayList newList = null;
        Asset asset = null;
       
        htmlUtil = new HtmlUtility();
         
        int[] d =null; 
        String query= "UPDATE AM_ASSET2 SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,Spare_3=?,Spare_4=?,Spare_5=?,Spare_6=?," +
                "BAR_CODE=?, DEPT_ID=?, DEPT_CODE=?, SUB_CATEGORY_ID=?, SUB_CATEGORY_CODE=? WHERE ASSET_ID=?" ;

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);

             for (int i = 0; i < list.size(); ++i) {
        asset = (Asset)list.get(i);
                String assetId = asset.getId();
                String registrationNo = asset.getRegistrationNo();
                String description = asset.getDescription();
                String vendorAC = asset.getVendorAc();
                String assetModel = asset.getAssetMake();
                String AssetSerialNo = asset.getSerialNo();
                String AssetEngineNo = asset.getEngineNo();
                int SupplierName = asset.getSupplierName();
                String assetUser = asset.getAssetUser();
                int assetMaintenance = asset.getAssetMaintain();
                String authorizedBy = asset.getAuthorizeBy();
                String purchaseReason = asset.getPurchaseReason();
                String sbuCode = asset.getSbuCode();
                String spare1 = asset.getSpare1();
                String spare2 = asset.getSpare2(); 
                String spare3 = asset.getSpare3();
                String spare4 = asset.getSpare4(); 
                String spare5 = asset.getSpare5();
                String spare6 = asset.getSpare6(); 
                String deptId = asset.getDepartmentId();
                String subcategoryId = asset.getSubcategoryId();
 //               String subcategoryCode = asset.getSubcategoryCode();
                String deptQuery = "SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+deptId+"'";
                String dept_code = htmlUtil.findObject(deptQuery);
                String subcatQuery = "SELECT sub_category_code FROM   am_ad_sub_category WHERE sub_category_id = '"+subcategoryId+"'";
                String subcategoryCode = htmlUtil.findObject(subcatQuery);
                String barCode = asset.getBarCode();
 //                System.out.println("the asset id in bulk update manager is >>>>>>>>>>> " + assetId);
                asset=null;
                 

             ps.setString(1,registrationNo);
             ps.setString(2,description);
             ps.setString(3,assetUser);
             ps.setInt(4,assetMaintenance);
             ps.setString(5,vendorAC);
             ps.setString(6,assetModel);
             ps.setString(7,AssetSerialNo);
             ps.setString(8,AssetEngineNo);
             ps.setInt(9,SupplierName);
             ps.setString(10,authorizedBy);
             ps.setString(11,purchaseReason);
             ps.setString(12,sbuCode);
             ps.setString(13,spare1);
             ps.setString(14,spare2);
             ps.setString(15,spare3);
             ps.setString(16,spare4);
             ps.setString(17,spare5);
             ps.setString(18,spare6);
             ps.setString(19,barCode);
             ps.setString(20,deptId);
             ps.setString(21,dept_code);
             ps.setString(22,subcategoryId);
             ps.setString(23,subcategoryCode);
             ps.setString(24,assetId);

            // ps.addBatch();
            ps.execute();
             }
            d=ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("BulkUpdateManager: updateBulkAsset()>>>>>" + ex);
        } 
            return (d.length > 0);
    }


    public ArrayList findAssetsByBatchId(String tranId) {


        //String assetFilter = " and asset_id in (";
        String selectQuery =
                "select * from am_gb_bulkupdate where batch_id=? ";

       

        ResultSet rs = null;
        ArrayList listNew = new ArrayList();
       // ArrayList listOld = new ArrayList();
       // ArrayList[] listNewOld = new ArrayList[2];
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();
            Asset aset = null;
            while (rs.next()) {
                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String vendorAC = rs.getString("Vendor_AC");
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                int SupplierName = rs.getInt("Supplier_Name");
                String assetUser = rs.getString("ASSET_USER");
                int assetMaintenance = rs.getInt("Asset_Maintenance");
                String authorizedBy = rs.getString("Authorized_By");
                String purchaseReason = rs.getString("Purchase_Reason");
                String sbuCode = rs.getString("SBU_CODE");
                String deptCode = rs.getString("DEPT_CODE"); 
                String deptId = rs.getString("Dept_ID");   
                String subcategoryCode = rs.getString("sub_category_code"); 
                String subcategoryId = rs.getString("sub_category_ID"); 
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");                
                String barCode = rs.getString("BAR_CODE");
                String branchId = rs.getString("branch_id");

                 aset = new Asset();
                aset.setId(id);
                aset.setDescription(description);
                aset.setRegistrationNo(registrationNo);
                aset.setVendorAc(vendorAC);
                aset.setAssetMake(assetModel);
                aset.setSerialNo(AssetSerialNo);
                aset.setEngineNo(AssetEngineNo);
                aset.setSupplierName(SupplierName);
                aset.setAssetUser(assetUser);
                aset.setAssetMaintain(assetMaintenance);
                aset.setAuthorizeBy(authorizedBy);
                aset.setPurchaseReason(purchaseReason);
                aset.setSbuCode(sbuCode);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);
                aset.setDepartmentId(deptId);
                aset.setSubcategoryId(subcategoryId);
                aset.setSubcategoryCode(subcategoryCode);
                aset.setDepcode(deptCode);

                
                listNew.add(aset);
                aset=null;
               // assetFilter = assetFilter + "'" + id + "',";

            }

        } catch (Exception e) {
            System.out.println("INFO:Error findAssetsByBatchId() in BulkUpdateManager-> " +
                    e.getMessage());
        } 
//        System.out.println("the size of listNew is >>>>>> " + listNew.size());
        return listNew;

    }

        public double[] findMinMaxAssetCost(String assetId, double minAssetCost, double maxAssetCost) {
        double costPrice = 0.0;
        double[] result = new double[2];
        String selectQuery =  "select cost_price from am_asset where asset_id=?";

        

        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ps.setString(1,assetId);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                costPrice= rs.getDouble("cost_price");
                      }

        if(costPrice > maxAssetCost )result[0] =costPrice;
	if(costPrice < minAssetCost )result[1] =costPrice;

        } catch (Exception e) {
            System.out.println("INFO:Error findMinMaxAssetCost() in BulkUpdateManager-> " +
                    e.getMessage());
        } 


        return result;  

    }

        public ArrayList[] findAssetTransferByBatchId(String tranId) {

        //	System.out.println("findAssetTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkTransfer where batch_id=?  order by ASSET_ID ";
//            System.out.println("findAssetTransferByBatchId selectQuery: "+selectQuery);
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                AssetRecordsBean aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String oldbranchId = rs.getString("oldbranch_id");
                    String olddeptId = rs.getString("olddept_id");
                    String oldsbucode = rs.getString("oldSBU_CODE");
                    String oldsectionId = rs.getString("oldsection_id");
                    String oldassetuser = rs.getString("oldAsset_User");
                    String newassetId = rs.getString("NEW_ASSET_ID");
                    String newbranchId = rs.getString("newbranch_id");
                    String newdeptId = rs.getString("newdept_id");
                    String newsbucode = rs.getString("newSBU_CODE");
                    String newsectionId = rs.getString("newsection_id");
                    String newassetuser = rs.getString("newAsset_User");
                    String batchId = rs.getString("Batch_id");
                    String transferbyId = rs.getString("Transferby_id");
                    int assetCode =  rs.getInt("ASSET_CODE");
                    String costPrice = rs.getString("COST_PRICE");
                    String accumDep = rs.getString("ACCUM_DEP");
                    
                     aset = new AssetRecordsBean();
                    aset.setAsset_id(id);
                    aset.setDescription(description);
                    aset.setRegistration_no(registrationNo);
                    aset.setBranch_id(oldbranchId);
                    aset.setDepartment_id(olddeptId);
                    aset.setSbu_code(oldsbucode);
                    aset.setSection_id(oldsectionId);
                    aset.setUser(oldassetuser);
                    aset.setNewbranch_id(newbranchId);
                    aset.setNewdepartment_id(newdeptId);
                    aset.setNewsbu_code(newsbucode);
                    aset.setNewsection_id(newsectionId);
                    aset.setNewuser(newassetuser);
                    aset.setNewasset_id(newassetId);
                    aset.setAssetCode(assetCode);
                    aset.setCost_price(costPrice);
                    aset.setAccum_dep(accumDep);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                } 

                boolean index = assetFilter.endsWith(",");
            //    System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
//                    System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter +  ")  order by ASSET_ID";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetTransferByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public boolean BulkAssetTransfer(ArrayList list) {

            ArrayList newList = null;
 //           AssetRecordsBean asset = null;
            magma.AssetRecordsBean asset = null;
            
            htmlUtil = new HtmlUtility();
             
            int[] d =null;   
            String query= "UPDATE AM_ASSET SET ASSET_ID=?," +
                    " OLD_ASSET_ID=?,ASSET_USER=?,Branch_ID=?,Dept_ID=?,Section_id=?," +
                    "SBU_CODE=?,BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? WHERE ASSET_ID=?" ;
//            System.out.println("I am in bulk Transfer manager is >>>>>>>>>>> "+list.size());
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement  ps = con.prepareStatement(query);

                 for (int i = 0; i < list.size(); ++i) {
            asset = (magma.AssetRecordsBean) list.get(i);
                    String newassetId = asset.getNewasset_id();
//                    System.out.println("the New asset id in bulk Transfer manager is >>>>>>>>>>> "+newassetId);
                    String oldassetId = asset.getAsset_id();
//                    System.out.println("the Old asset id in bulk Transfer manager is >>>>>>>>>>> "+oldassetId);
                    String newbranchId = asset.getNewbranch_id();
                    String newdeptId = asset.getNewdepartment_id();
                    String description = asset.getDescription();
                    String newsbucode = asset.getNewsbu_code();
                    String newsectionId = asset.getNewsection_id();
                    String newassetUser = asset.getNewuser();
                    String branchQuery = "SELECT branch_code FROM   am_ad_branch WHERE branch_id = '"+newbranchId+"'";
                    String branch_code = htmlUtil.findObject(branchQuery);
                    String deptQuery = "SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+newdeptId+"'";
                    String dept_code = htmlUtil.findObject(deptQuery);
                    String sectionQuery = "SELECT section_code FROM   am_ad_section WHERE section_id = '"+newsectionId+"'";
                    String section_code = htmlUtil.findObject(sectionQuery);                    
//                    System.out.println("the Old asset id in bulk Transfer manager is >>>>>>>>>>> " + oldassetId+"   New Asset Id:  "+newassetId);
                    asset=null;
                    

                 ps.setString(1,newassetId);
                 ps.setString(2,oldassetId);
                 ps.setString(3,newassetUser);
                 ps.setInt(4,Integer.parseInt(newbranchId));
                 ps.setInt(5,Integer.parseInt(newdeptId));
                 ps.setInt(6,Integer.parseInt(newsectionId));
                 ps.setString(7,newsbucode);
                 ps.setString(8,branch_code);
                 ps.setString(9,dept_code);
                 ps.setString(10,section_code);
                 ps.setString(11,oldassetId);

                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: BulkAssetTransfer()>>>>>" + ex);
            } 
                return (d.length > 0);
        }

        public boolean insertWorkBookUpdate(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into am_gb_workbookupdate ( " +
                    "ASSET_ID,Description,BAR_CODE,ASSET_USER," +
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID )" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    int assetcode = bd.getAssetCode();      
//                    System.out.println("<<<<<<assetId: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                    ps.setString(1, assetId);
                    ps.setString(2, description);
                    ps.setString(3, barcode);
                    ps.setString(4, assetuser);
                    ps.setString(5, comments);
                    ps.setString(6, sighted);
                    ps.setString(7, functional);
                    ps.setInt(8, assetcode);
                    ps.setString(9, branchCode);
                    ps.setInt(10, Integer.parseInt(categoryId));
                    ps.setInt(11, bacthId);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertWorkBookUpdate() of BulkUpdateManager -> " + ex);
            } 
            return (d.length > 0);
        }

        public ArrayList[] findAssetWorkBookByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_workbookupdate where batch_id=? and process_status is null ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";

            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");

                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to BulkUpdateManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetWorkBookByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public boolean updateBulkAssetVerification(ArrayList list) {

            ArrayList newList = null;
            Asset asset = null;
           
            htmlUtil = new HtmlUtility();
             
            int[] d =null;
            String query= "UPDATE AM_ASSET SET " +
                    " DESCRIPTION=?,ASSET_USER=?," +
                    "BAR_CODE=? WHERE ASSET_ID=?" ;

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement   ps = con.prepareStatement(query);
                
                 for (int i = 0; i < list.size(); ++i) {
            asset = (Asset)list.get(i);
                    String assetId = asset.getId();
                    String description = asset.getDescription();
                    String barCode = asset.getBarCode();
                    String assetUser = asset.getAssetUser();
                    asset=null;
                   

                 ps.setString(1,description);
                 ps.setString(2,assetUser);
                 ps.setString(3,barCode);
                 ps.setString(4,assetId);

                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: updateBulkAssetVerification()>>>>>" + ex);
            } 
            
                return (d.length > 0);
        }

        public boolean insertWorkBookVerificationSelection(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into am_gb_workbookselection ( " +
                    "ASSET_ID,Description,BAR_CODE,ASSET_USER," +
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID )" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";


            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    int assetcode = bd.getAssetCode();      
//                    System.out.println("<<<<<<assetId=======: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                    ps.setString(1, assetId);
                    ps.setString(2, description);
                    ps.setString(3, barcode);
                    ps.setString(4, assetuser);
                    ps.setString(5, comments);
                    ps.setString(6, sighted);
                    ps.setString(7, functional);
                    ps.setInt(8, assetcode);
                    ps.setString(9, branchCode);
                    ps.setInt(10, Integer.parseInt(categoryId));
                    ps.setInt(11, bacthId);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertWorkBookVerificationSelection() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        
        public ArrayList[] findAssetVerificationSelectionBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
            		"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";

            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");

                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent From findAssetVerificationSelectionBatchIds is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetVerificationSelectionBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList[] findAssetVerificationSelectionforApprovalBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
            		"select * from am_gb_workbookselection where batch_id=? and process_status is null ";

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null; 
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");

                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent  from findAssetVerificationSelectionforApprovalBatchId is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetVerificationSelectionforApprovalBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }
        
        public boolean insertFleetTransctionDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into FT_MAINTENANCE_DETAILS ( " +
                    "HIST_ID,COST_PRICE,QUANTITY,DESCRIPTION,DETAILS)" +
                    " values(?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String description = bd.getDescription();
                    String costprice = bd.getCost_price();
                    String qty = bd.getQty();
                    String details = bd.getVendorName();                   
                    if (qty == null || qty.equals("")) {
                    	qty = "0";
                    }
                    if (costprice == null || costprice.equals("")) {
                    	costprice = "0.0";
                    }
                    if(!costprice.equals("0.0") || (!description.equals(""))) {
//                    	 System.out.println("<<<<<<description in insertFleetTransctionDetails: "+description+"  costprice: "+costprice+"  qty: "+qty+"   details: "+details);
                    ps.setString(1, histId);
                    ps.setDouble(2, Double.valueOf(costprice));
                    ps.setInt(3, Integer.parseInt(qty));
                    ps.setString(4, description);
                    ps.setString(5, details);
                    ps.addBatch();
                    }
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertFleetTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }


        public boolean insertAssetProofInsert(ArrayList list, String bacthId, String processExport) {
            boolean re = false;
            String tableName = "Temp"+bacthId;
//            System.out.println("<<<<<<<tableName in insertAssetProofInsert: "+tableName+"   bacthId: "+bacthId);
 //           String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
 //        	ad.updateAssetStatusChange(q1);
            String createquery = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].["+tableName+"]') AND type in (N'U')) BEGIN " +
            "CREATE TABLE "+tableName+"( " +
            "[ASSET_ID] [varchar](50) NULL,	[Description] [varchar](255) NULL,	[BAR_CODE] [varchar](50) NULL,"+
            "[Cost_Price] [decimal](18, 2) NULL,	[Registration_No] [varchar](50) NULL,	[Asset_Make] [nvarchar](50) NULL,"+
            "[Asset_Model] [varchar](255) NULL,	[Asset_Serial_No] [varchar](255) NULL,	[Asset_Engine_No] [varchar](255) NULL,"+
            "[SBU_CODE] [varchar](50) NULL, [SuppliedBy] [varchar](50) NULL, [Vendor_AC] [varchar](50) NULL, [MaintainedBy] [varchar](50) NULL, [Date_purchased] [datetime] NULL,"+
            "[Spare_1] [varchar](150) NULL,	[Spare_2] [varchar](150) NULL,[Spare_3] [varchar](150) NULL,[Spare_4] [varchar](150) NULL,"+
            "[Spare_5] [varchar](150) NULL,	[Spare_6] [varchar](150) NULL,[ASSET_USER] [varchar](255) NULL,"+
            "[COMMENTS] [varchar](255) NULL,	[ASSETSIGHTED] [nchar](1) NULL,	[ASSETFUNCTION] [nchar](1) NULL,"+
            "[State] [int] NULL,[OLD_ASSET_ID] [varchar](50) NULL,[GROUP_NO] [int] NULL,[GROUP_ID] [varchar](50) NULL,"+
            "[Section_id] [int] NULL,[Accum_Dep] [decimal](18, 2) NULL,[Monthly_Dep] [decimal](18, 2) NULL,[NBV] [decimal](18, 2) NULL,"+
            "[Dept_ID] [int] NULL,	[Location] [int] NULL,[LPO] [varchar](50) NULL,[IMPROV_NBV] [decimal](18, 2),[IMPROV_VATABLECOST] [decimal](18, 2),"+
            "[IMPROV_COST] [decimal](18, 2) NULL,	[IMPROV_MONTHLYDEP] [decimal](18, 2) NULL,[IMPROV_ACCUMDEP] [decimal](18, 2) NULL,[PROOF_DATE] [date] NULL,"+
           "[Dep_End_Date] [varchar](50) NULL,	[Effective_Date] [varchar](50) NULL,[TOTAL_NBV] [decimal](18, 2) NULL,[SUB_CATEGORY_ID] [int] NULL,"+
            "[ASSET_CODE] [int] NULL,	[BRANCH_ID] [varchar](50) NULL,	[CATEGORY_ID] [int] NULL,"+
            "[BATCH_ID] [varchar](50) NULL,	[PROCESS_STATUS] [varchar](50) NULL,[SELECT_DATE] [date] NULL,"+
            "[PROOFED_BY] [int] NULL, [INITIATEDBRANCHCODE] [varchar](50) NULL) ON [PRIMARY]  END";
//            System.out.println("<<<<<<<createquery in insertAssetProofInsert: "+createquery);  INITIATEDBRANCHCODE
            
            ad.updateAssetStatusChange(createquery);
            String insertquery = "insert into "+tableName+" ( " +
                    "ASSET_ID,Description,BAR_CODE,Registration_No,Asset_Make,Asset_Model,Asset_Serial_No,"+  //7
                    "Asset_Engine_No,Date_purchased,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,SBU_CODE,ASSET_USER," + //17
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID,PROOF_DATE,GROUP_NO,GROUP_ID," +  //27
                    "Cost_Price,Accum_Dep,Monthly_Dep,NBV,IMPROV_COST,IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,TOTAL_NBV,"+  //36
                    "OLD_ASSET_ID,SuppliedBy,Vendor_AC,MaintainedBy,Dep_End_Date,Effective_Date,SUB_CATEGORY_ID,Dept_ID,Section_id,Location,State,LPO,PROOFED_BY,INITIATEDBRANCHCODE )" +  //50
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String query = "insert into am_Asset_Proof ( " +
                    "ASSET_ID,Description,BAR_CODE,Cost_Price,Registration_No,Asset_Make,Asset_Model,Asset_Serial_No,"+
                    "Asset_Engine_No,Date_purchased,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,SBU_CODE,ASSET_USER," +
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID,PROOF_DATE,GROUP_NO,GROUP_ID,"+
                    "Effective_Date, Dep_End_Date,Supplier_Name,Vendor_AC,LPO,Purchase_Reason,Asset_Maintenance,Accum_Dep,Monthly_Dep,NBV,"+
                    "IMPROV_COST,IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,TOTAL_NBV,OLD_ASSET_ID,SUB_CATEGORY_ID,Dept_ID,Section_id,Location,State,REC_ADD,USER_ID,INITIATEDBRANCHCODE)"+
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {  
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    
//                    System.out.println("<<<<<assetId: "+assetId);
                    String description = bd.getDescription();
//                    System.out.println("<<<<<description: "+description);
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    String deptId = bd.getDepartment_id();
                    String sectionId = bd.getSection_id();
                    String locationId = bd.getLocation();
                    String stateId = bd.getState();
                    String subCategoryId = bd.getSub_category_id();
                    if(categoryId.equalsIgnoreCase("")){categoryId = "0";}
                    if(subCategoryId.equalsIgnoreCase("")){subCategoryId = "0";}
                    if(deptId.equalsIgnoreCase("")){deptId = "0";}
                    if(sectionId.equalsIgnoreCase("")){sectionId = "0";}
                    if(locationId.equalsIgnoreCase("")){locationId = "0";}
                    if(stateId.equalsIgnoreCase("")){stateId = "0";}
//                    System.out.println("<<<<<<=====deptId: "+deptId+"  sectionId: "+sectionId+"  locationId: "+locationId+"  stateId: "+stateId+"  subCategoryId: "+subCategoryId+"   categoryId: "+categoryId);
                    String costprice = bd.getCost_price();
                    if(costprice.equals("")){costprice = "0";}
//                    System.out.println("<<<<<costprice: "+costprice);
                    String model = bd.getModel();
                    String make = bd.getMake();
                    String depreciation_start_date = bd.getDepreciation_start_date();
//                    System.out.println("<<<<<depreciation_start_date 1: "+depreciation_start_date);
                	if(depreciation_start_date!=""){
                	String dd = depreciation_start_date.substring(0,2);
                	String mm = depreciation_start_date.substring(3,5);
                	String yyyy = depreciation_start_date.substring(6,10);
                	depreciation_start_date = yyyy+"-"+mm+"-"+dd;
                	}                    
//                    System.out.println("<<<<<depreciation_start_date 2: "+depreciation_start_date);
                    String depreciation_end_date = bd.getDepreciation_end_date();
//                    System.out.println("<<<<<depreciation_end_date 1: "+depreciation_end_date);
                    String userId = bd.getUser_id();
                	if(!depreciation_end_date.equalsIgnoreCase("")){
//                		System.out.println("<<<<<depreciation_end_date 2: "+depreciation_end_date+"   userId: "+userId);
                	String dd = depreciation_end_date.substring(0,2);
                	String mm = depreciation_end_date.substring(3,5);
                	String yyyy = depreciation_end_date.substring(6,10);
                	depreciation_end_date = yyyy+"-"+mm+"-"+dd;
                	}
                	if(depreciation_end_date.equalsIgnoreCase("")){depreciation_end_date = depreciation_start_date;}
//                    System.out.println("<<<<<depreciation_end_date 3: "+depreciation_end_date);
                    String monthlyDep = bd.getMonthlydep() == null ? "0" : bd.getMonthlydep();
                    if(monthlyDep.equals("")){monthlyDep = "0";}
                    String accumdep = bd.getAccumdep() == null ? "0" : bd.getAccumdep();
                    if(accumdep.equals("")){accumdep = "0";}
                    String nbv = bd.getNbv() == null ? "0" : bd.getNbv();
                    if(nbv.equals("")){nbv = "0";}
//                    System.out.println("<<<<<accumdep: "+accumdep+"   nbv: "+nbv);
                 //   String improveCost = bd.getImproveCost();
//                    System.out.println("<<<<<costprice: "+costprice+"  monthlyDep: "+monthlyDep+"   accumdep: "+accumdep+"   nbv: "+nbv);
                    String improveCost = bd.getImproveCost() == null ? "0" : bd.getImproveCost();
                    if(improveCost.equals("")){improveCost = "0";}
                 //   String improveMonthlydep = bd.getImproveMonthlydep();
                    String improveMonthlydep = bd.getImproveMonthlydep() == null ? "0" : bd.getImproveMonthlydep();
                    String improveAccumdep = bd.getImproveAccumdep() == null ? "0" : bd.getImproveAccumdep();
                    if(improveAccumdep.equals("")){improveAccumdep = "0";}
                    String improveNBV =  bd.getImproveNbv() == null ? "0" : bd.getImproveNbv();
                    if(improveNBV.equals("")){improveNBV = "0";}
                    String improvetotalnbv = bd.getImproveTotalNbv() == null ? "0" : bd.getImproveTotalNbv();
//                    System.out.println("<<<<<improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"   improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
                    String supplied_by = bd.getSupplied_by();
                    String vendor_account = bd.getVendor_account();
                    String lpo = bd.getLpo();
                	String sbucode = bd.getSbu_code();
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String spare3 = bd.getSpare_3();
                	String spare4 = bd.getSpare_4();
                	String spare5 = bd.getSpare_5();
                	String spare6 = bd.getSpare_6();  
//                	System.out.println("<<<<<supplied_by: "+supplied_by);
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
                	String suppliedBy = bd.getSupplied_by();
                	String maintianedBy = bd.getMaintained_by();
                	String authorized = bd.getAuthorized_by();
                	String purchaseReason = bd.getReason();	
                	String purchaseDate = bd.getDate_of_purchase();	
                	String itemType = bd.getItemType();
                	String user_Id = bd.getUser_id();
                	String intiatedbranchCode = approvalRec.getCodeName("select b.BRANCH_CODE from am_gb_User a, am_ad_branch b where a.Branch = b.BRANCH_ID and a.user_id = "+user_Id+" ");
//                	if(maintianedBy==""){maintianedBy = "0";}
//                	System.out.println("<<<<<intiatedbranchCode: "+intiatedbranchCode);
                	int groupNo = bd.getQuantity();
                	String groupId = bd.getProjectCode();
                	String oldAssetId = bd.getOld_asset_id();
//                	System.out.println("<<<<<purchaseDate: "+purchaseDate);
 //               	if(purchaseDate!=""){
                	if(!purchaseDate.equalsIgnoreCase("")){
                	String dd = purchaseDate.substring(0,2);
                	String mm = purchaseDate.substring(3,5);
                	String yyyy = purchaseDate.substring(6,10);
                	purchaseDate = yyyy+"-"+mm+"-"+dd;
                	}
                	if(purchaseDate.equalsIgnoreCase("")){purchaseDate = depreciation_start_date;}
//                	System.out.println("<<<<<bd.getAssetCode(): "+bd.getAssetCode());
                    int assetcode = bd.getAssetCode();    
//                    System.out.println("<<<<<<assetId: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
//                    System.out.println("<<<<<<costprice: "+costprice+"  registrationNo: "+registrationNo+"  make: "+make+"  model: "+model+"  serialNo: "+serialNo+"  engineNo: "+engineNo+"  purchaseDate: "+purchaseDate+"  spare1: "+spare1+"  spare2: "+spare2+"  spare3: "+spare3+"  spare4: "+spare4+"  spare5: "+spare5+"  spare6: "+spare6+"  sbucode: "+sbucode+"  categoryId: "+categoryId);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }
                    String recNo = approvalRec.getCodeName("SELECT count(*) FROM am_Asset_Proof WHERE Asset_Id = '"+assetId+"' and group_id = '"+groupId+"' ");
                    ps.setString(1, assetId.toUpperCase());
                    ps.setString(2, description.toUpperCase());
                    ps.setString(3, barcode.toUpperCase());
//                    System.out.println("<<<<<costprice: "+costprice);
                    ps.setDouble(4, Double.parseDouble(costprice));
//                    System.out.println("<<<<<assetcode: "+assetcode);
                    ps.setString(5, registrationNo.toUpperCase());
                    ps.setString(6, make);
                    ps.setString(7, model);
                    ps.setString(8, serialNo);
                    ps.setString(9, engineNo);
                    ps.setString(10, purchaseDate);
                    ps.setString(11, spare1.toUpperCase());
                    ps.setString(12, spare2.toUpperCase());
                    ps.setString(13, spare3.toUpperCase());
                    ps.setString(14, spare4.toUpperCase());
                    ps.setString(15, spare5.toUpperCase());
                    ps.setString(16, spare6.toUpperCase());                    
                    ps.setString(17, sbucode);
                    ps.setString(18, assetuser.toUpperCase());
                    ps.setString(19, comments);
                    ps.setString(20, sighted);
                    ps.setString(21, functional);
                    ps.setInt(22, assetcode);
                    ps.setString(23, branchCode);
//                    System.out.println("<<<<<branchCode: "+branchCode);
//                    System.out.println("<<<<<categoryId: "+categoryId);
                    ps.setInt(24, Integer.parseInt(categoryId));
                    ps.setString(25, bacthId);
                    ps.setTimestamp(26, dbConnection.getDateTime(new java.util.Date()));
                    ps.setInt(27, groupNo); 
//                    System.out.println("<<<<<groupId: "+groupId);
                    ps.setString(28, groupId);
                    ps.setString(29, depreciation_start_date);
                    ps.setString(30, depreciation_end_date);
                    ps.setString(31, suppliedBy);
                    ps.setString(32, vendor_account);
//                    System.out.println("<<<<<vendor_account: "+vendor_account);
                    ps.setString(33, lpo);
                    ps.setString(34, purchaseReason.toUpperCase());
                    ps.setString(35, maintianedBy);
//                    System.out.println("<<<<<accumdep: "+accumdep);
                    ps.setDouble(36, Double.parseDouble(accumdep));
//                    System.out.println("<<<<<monthlyDep: "+monthlyDep);
                    ps.setDouble(37, Double.parseDouble(monthlyDep));
//                    System.out.println("<<<<<nbv: "+nbv);
                    ps.setDouble(38, Double.parseDouble(nbv));
//                    System.out.println("<<<<<improveCost: "+improveCost);
                    ps.setDouble(39, Double.parseDouble(improveCost));
//                    System.out.println("<<<<<improveMonthlydep: "+improveMonthlydep);
                    ps.setDouble(40, Double.parseDouble(improveMonthlydep));
//                    System.out.println("<<<<<improveAccumdep: "+improveAccumdep);
                    ps.setDouble(41, Double.parseDouble(improveAccumdep));
//                    System.out.println("<<<<<improveNBV: "+improveNBV);
                    ps.setDouble(42, Double.parseDouble(improveNBV));
//                    System.out.println("<<<<<accumdep: "+accumdep+"  monthlyDep: "+monthlyDep+"  nbv: "+nbv+"   improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"  improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
                    ps.setDouble(43, Double.parseDouble(improveNBV)+Double.parseDouble(nbv));
                    ps.setString(44, oldAssetId);
//                    System.out.println("<<<<<<subCategoryId: "+subCategoryId+"  deptId: "+deptId+"  sectionId: "+sectionId+"  locationId: "+locationId+"  stateId: "+stateId+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
                    ps.setInt(45, Integer.parseInt(subCategoryId));
//                    System.out.println("<<<<<deptId: "+deptId);
                    ps.setInt(46, Integer.parseInt(deptId));
//                    System.out.println("<<<<<sectionId: "+sectionId);
                    ps.setInt(47, Integer.parseInt(sectionId));
//                    System.out.println("<<<<<locationId: "+locationId);
                    ps.setInt(48, Integer.parseInt(locationId));
//                    System.out.println("<<<<<stateId: "+stateId);
                    ps.setInt(49, Integer.parseInt(stateId));
//                    System.out.println("<<<<<assetcode: "+assetcode);
                    ps.setString(50, itemType);
//                    System.out.println("<<<<<userId: "+userId);
                    ps.setInt(51, Integer.parseInt(userId));
                    ps.setString(52, intiatedbranchCode);
         //           ps.setString(45, groupId);
 //                   ps.addBatch();
 //                   d = ps.executeBatch();
                    if(recNo.equals("0")){
                    ps.execute();
                    }
                    PreparedStatement ps1 = con.prepareStatement(insertquery);
                    ps1.setString(1, assetId.toUpperCase());
                    ps1.setString(2, description.toUpperCase());
                    ps1.setString(3, barcode.toUpperCase());
         //           ps1.setDouble(4, Double.parseDouble(costprice));
                    ps1.setString(4, registrationNo.toUpperCase());
                    ps1.setString(5, make);
                    ps1.setString(6, model);
                    ps1.setString(7, serialNo);
                    ps1.setString(8, engineNo);
                    ps1.setString(9, purchaseDate);
                    ps1.setString(10, spare1.toUpperCase());
                    ps1.setString(11, spare2.toUpperCase());
                    ps1.setString(12, spare3.toUpperCase());
                    ps1.setString(13, spare4.toUpperCase());
                    ps1.setString(14, spare5.toUpperCase());
                    ps1.setString(15, spare6.toUpperCase());
                    ps1.setString(16, sbucode);
                    ps1.setString(17, assetuser.toUpperCase());
                    ps1.setString(18, comments);
                    ps1.setString(19, sighted);
                    ps1.setString(20, functional);
                    ps1.setInt(21, assetcode);
                    ps1.setString(22, branchCode);
                    ps1.setInt(23, Integer.parseInt(categoryId));
                    ps1.setString(24, bacthId);
                    ps1.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
                    ps1.setInt(26, groupNo);
                    ps1.setString(27, groupId);
//                    System.out.println("<<<<<groupId: "+groupId+"    groupNo: "+groupNo);
                    ps1.setDouble(28, Double.parseDouble(costprice));

					ps1.setDouble(29, Double.parseDouble(accumdep));
					ps1.setDouble(30, Double.parseDouble(monthlyDep));
					ps1.setDouble(31, Double.parseDouble(nbv));
					ps1.setDouble(32, Double.parseDouble(improveCost));
					ps1.setDouble(33, Double.parseDouble(improveMonthlydep));
					ps1.setDouble(34, Double.parseDouble(improveAccumdep));
					ps1.setDouble(35, Double.parseDouble(improveNBV));
//					System.out.println("<<<<<accumdep: "+accumdep+"  monthlyDep: "+monthlyDep+"  nbv: "+nbv+"   improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"  improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
					ps1.setDouble(36, Double.parseDouble(improveNBV)+Double.parseDouble(nbv));
					ps1.setString(37, oldAssetId);
					ps1.setString(38, suppliedBy);
					ps1.setString(39, vendor_account);
					ps1.setString(40, maintianedBy);
					ps1.setString(41, depreciation_end_date);
					ps1.setString(42, depreciation_start_date);
//					System.out.println("<<<<<<subCategoryId: "+subCategoryId+"  deptId: "+deptId+"  sectionId: "+sectionId+"  locationId: "+locationId+"  stateId: "+stateId+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
					ps1.setInt(43, Integer.parseInt(subCategoryId));
					ps1.setInt(44, Integer.parseInt(deptId));
					ps1.setInt(45, Integer.parseInt(sectionId));
					ps1.setInt(46, Integer.parseInt(locationId));
//					System.out.println("<<<<<locationId: "+locationId);
					ps1.setInt(47, Integer.parseInt(stateId));
					ps1.setString(48, lpo);
					ps1.setInt(49, Integer.parseInt(userId));
					ps1.setString(50, intiatedbranchCode);
 //                   ps1.addBatch();
 //                   d = ps1.executeBatch(); 
					
                    if(recNo.equals("0")){
                    	ps1.execute();
                    }
                }
  //              d = ps.executeBatch();
                //System.out.println("Executed Successfully ");
                re = true;

            } catch (Exception ex) {
                System.out.println("Error insertAssetProofInsert() of BulkUpdateManager -> " + ex);
            } 

            return re;
        }

        public ArrayList findAddedAssetProofByBatchId(String tranId) {


 //           String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id='"+tranId+"' and process_status = 'APPROVED' and rec_add = 'YES' ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";

            
            ArrayList listadd = new ArrayList();
            ArrayList listaddrec = new ArrayList(2);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
//                ps.setInt(1, Integer.parseInt(tranId));
                ResultSet rs = ps.executeQuery();
//                System.out.println("<<<<<selectQuery in findAddedAssetProofByBatchId: "+selectQuery);
                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    String vendorAc = rs.getString("vendor_ac");
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    int initiator = rs.getInt("USER_ID");  
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                    aset = new Asset();
                    aset.setId(id);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDescription(description);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
    //                System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);                    
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setVendorAc(vendorAc);
                    aset.setUserid(initiator);
                    listadd.add(aset);
                    aset=null;

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAddedAssetProofByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            }

            return listadd;

        }
        

        public ArrayList[] findAssetProofByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id=? and process_status ='WFA' and new_rec = 'N' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
            String id = "";
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    String vendorAc = rs.getString("vendor_ac");
                    String assetMaintenance = rs.getString("Asset_Maintenance");
                    String supplierName = rs.getString("Supplier_Name");
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                    aset = new Asset();
                    aset.setId(id);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
    //                System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setAssetMaintain(Integer.parseInt(assetMaintenance));
                    aset.setSupplierName(Integer.parseInt(supplierName));
                    listNew.add(aset);
                    
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";
                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ") ";
                System.out.println("the filter that will be sent to findAssetProofByBatchId is >>>>> " + assetFilter);
             //   listOld = findAssetByID(assetFilter+" order by ASSET_ID");
                listOld = findAssetProofByID(assetFilter+" order by ASSET_ID",tranId);
                
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
//            System.out.println("the size of listOld is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList[] findAssetProofRejectionByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id=? order by group_no,asset_id ";
 //           "select * from am_Asset_Proof where batch_id=? and process_status ='REJECTED' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList listNewAdd = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    String vendorAc = rs.getString("vendor_ac");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);                    
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setVendorAc(vendorAc);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
///                System.out.println("the filter that will be sent from findAssetProofRejectionByBatchId is >>>>> " + assetFilter);
                listOld = findAssetProofByID(assetFilter+" order by ASSET_ID",tranId);
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofRejectionByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
  //          listNewOld[1] = listNewAdd;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList[] findAssetProofRejectionByBatchId(String tranId,int branch_Id) {

//        	System.out.println("====tranId in findAssetProofRejectionByBatchId: "+tranId+"    branch_Id: "+branch_Id);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id=? and branch_Id = "+branch_Id+" order by group_no,asset_id ";
 //           "select * from am_Asset_Proof where batch_id=? and process_status ='REJECTED' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
          
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList listNewAdd = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    String vendorAc = rs.getString("vendor_ac");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);                    
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setVendorAc(vendorAc);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent from findAssetProofRejectionByBatchId is >>>>> " + assetFilter);
                listOld = findAssetProofByID(assetFilter+" order by ASSET_ID",tranId);
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofRejectionByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
  //          listNewOld[1] = listNewAdd;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

                
        
        public ArrayList[] findAssetProofSelectionBatchId(String tranId) {

/*
            String assetFilter = " and asset_id in (";
            String selectQuery =
            		"select * from am_Asset_Proof where batch_id=? and process_status = 'APPROVED' and rec_add ='NO' order by group_no,asset_id  ";
*/
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id=? order by group_no,asset_id ";
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();
                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");   
                    int initiator = rs.getInt("USER_ID"); 
                    String vendorAc = rs.getString("vendor_ac");
                    String assetMaintenance = rs.getString("Asset_Maintenance");
                    String supplierName = rs.getString("Supplier_Name");
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
    //                System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);
                    aset.setUserid(initiator);
                    aset.setAssetMaintain(Integer.parseInt(assetMaintenance));
                    aset.setSupplierName(Integer.parseInt(supplierName));
                    aset.setVendorAc(vendorAc);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
 //               System.out.println("the filter that will be sent to FleetHistoryManager from findAssetProofSelectionBatchId is >>>>> " + assetFilter);
                listOld = findAssetProofByID(assetFilter+" order by ASSET_ID",tranId);
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofSelectionBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            }
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public boolean insertAssetProofSelection(ArrayList list, String bacthId) {
            boolean re = false;

            String queryOld = "insert into am_Asset_Proof_Selection ( " +
                    "ASSET_ID,Description,BAR_CODE,ASSET_USER," +
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID )" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";

            String query = "insert into am_Asset_Proof_Selection ( " +
                    "ASSET_ID,Description,BAR_CODE,Cost_Price,Registration_No,Asset_Make,Asset_Model,Asset_Serial_No,"+  //8
                    "Asset_Engine_No,Date_purchased,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,SBU_CODE,ASSET_USER," + //18
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID,PROOF_DATE,GROUP_NO,GROUP_ID," +  //28
                    "Accum_Dep,Monthly_Dep,NBV,IMPROV_COST,IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,TOTAL_NBV,"+  //36
                    "Supplier_Name,Vendor_AC,Asset_Maintenance,Dep_End_Date,Effective_Date,OLD_ASSET_ID,SUB_CATEGORY_ID,Dept_ID,Section_id,Location,State,LPO,PROOFED_BY,REC_TYPE )" +  //47
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<List Size in insertAssetProofSelection: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String oldassetId = bd.getOld_asset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    String costprice = bd.getCost_price();
                    String model = bd.getModel();
                    String make = bd.getMake();
                	String sbucode = bd.getSbu_code();
                	String costPrice = bd.getCost_price();
                    String deptId = bd.getDepartment_id();
                    String sectionId = bd.getSection_id();
                    String locationId = bd.getLocation();
                    String stateId = bd.getState();
                    String initiator = bd.getUser_id();
                    String itemType = bd.getItemType();
 //                   System.out.println("<<<<<initiator: "+initiator);
                    String subCategoryId = bd.getSub_category_id();
                	String depreciation_start_date = bd.getDepreciation_start_date();
                    String vendor_account = bd.getVendor_account();
                    String lpo = bd.getLpo();
                    String purchaseDate = bd.getDate_of_purchase();	
                	if(depreciation_start_date!=""){
                	String dd = depreciation_start_date.substring(0,2);
                	String mm = depreciation_start_date.substring(3,5);
                	String yyyy = depreciation_start_date.substring(6,10);
                	depreciation_start_date = yyyy+"-"+mm+"-"+dd;
                	}                    
//                	System.out.println("<<<<<depreciation_start_date: "+depreciation_start_date);
                	String depreciation_end_date = bd.getDepreciation_end_date();
                	if(depreciation_end_date!=""){
                	String dd = depreciation_end_date.substring(0,2);
                	String mm = depreciation_end_date.substring(3,5);
                	String yyyy = depreciation_end_date.substring(6,10);
                	depreciation_end_date = yyyy+"-"+mm+"-"+dd;
                	}
//                	System.out.println("<<<<<depreciation_end_date: "+depreciation_end_date);
                	String monthlyDep = bd.getMonthlydep() == null ? "" : bd.getMonthlydep();
                	String accumdep = bd.getAccumdep() == null ? "" : bd.getAccumdep();
                	String nbv = bd.getNbv() == null ? "" : bd.getNbv();
                	//   String improveCost = bd.getImproveCost();
                	String improveCost = bd.getImproveCost() == null ? "" : bd.getImproveCost();
                	//   if(improveCost==null){improveCost = "0";}
                	//   String improveMonthlydep = bd.getImproveMonthlydep();
                	String improveMonthlydep = bd.getImproveMonthlydep() == null ? "" : bd.getImproveMonthlydep();
                	String improveAccumdep = bd.getImproveAccumdep() == null ? "" : bd.getImproveAccumdep();
                	String improveNBV =  bd.getImproveNbv() == null ? "" : bd.getImproveNbv();
                	String improvetotalnbv = bd.getImproveTotalNbv() == null ? "" : bd.getImproveTotalNbv();
//                	System.out.println("<<<<<improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"   improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
                	String oldAssetId = bd.getOld_asset_id();
                	purchaseDate = purchaseDate.substring(0,10);
//                	                	System.out.println("<<<<<purchaseDate: "+purchaseDate);
/*                	if(purchaseDate!=""){
                	String dd = purchaseDate.substring(0,2);
                	String mm = purchaseDate.substring(3,5);
                	String yyyy = purchaseDate.substring(6,10);
                	purchaseDate = yyyy+"-"+mm+"-"+dd;
                	} */
                	
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String spare3 = bd.getSpare_3();
                	String spare4 = bd.getSpare_4();
                	String spare5 = bd.getSpare_5();
                	String spare6 = bd.getSpare_6();                	
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
                	String suppliedBy = bd.getSupplied_by();
                	String maintainedBy = bd.getMaintained_by();
                	String purchaseReason = bd.getReason();	
                	String groupNo = String.valueOf(bd.getQuantity());
                	String groupId = bd.getProjectCode();
//                	if(purchaseDate==null){purchaseDate = "";}
//                	System.out.println("<<<<<purchaseDate: "+purchaseDate);
                	if(subCategoryId==""){subCategoryId = "0";}
                	if(deptId==""){deptId="0";}
                	if(sectionId==""){sectionId="0";}
                	if(locationId==""){locationId="0";}
                	if(stateId==""){stateId="0";}
                	
//                	System.out.println("<<<<<purchaseDate: "+purchaseDate+"  groupNo: "+groupNo+"  subCategoryId: "+subCategoryId+"   deptId: "+deptId+"  sectionId: "+sectionId+"   locationId: "+locationId+"  stateId: "+stateId);
                	if((subCategoryId=="") || (subCategoryId.equals("UNKNOWN"))){subCategoryId = "0";}
//                	System.out.println("<<<subCategoryId Value :" + subCategoryId);
                	if((deptId=="") || (deptId.equals("UNKNOWN"))){deptId="0";}
//                	System.out.println("<<<deptId Value :" + deptId);
                	if((sectionId.equals("")) || (sectionId.equals("UNKNOWN"))){sectionId="0";}
//                	System.out.println("<<<sectionId Value :" + sectionId);
                	if((locationId.equals("")) || (locationId.equals("UNKNOWN"))){locationId="0";}
//                	System.out.println("<<<locationId Value :" + locationId);
                	if((stateId.equals("")) || (stateId.equals("UNKNOWN"))){stateId="0";}
//                	System.out.println("<<<stateId Value :" + stateId);
//                	stateId = approvalRec.getCodeName("SELECT state_code FROM am_gb_states WHERE state_name = '"+stateId+"' ");
//                	System.out.println("<<<<<stateId: "+stateId+"  stateId After: "+stateId);
    //            	locationId = approvalRec.getCodeName("SELECT location_id FROM AM_GB_LOCATION WHERE location = '"+locationId+"' ");
//                	System.out.println("<<<<<locationId: "+locationId+"  locationId After: "+locationId);
//                    branchCode = approvalRec.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = "+initiatorBranchId+" ");
//                    subCategoryId = approvalRec.getCodeName("SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_name = '"+subCategoryId+"' ");
//                    System.out.println("<<<<<subCategoryId: "+subCategoryId+"  subCategoryId After: "+subCategoryId);
                    String subcategory_code = approvalRec.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = '"+subCategoryId+"' ");
//                    System.out.println("<<<<<subcategory_code: "+subcategory_code+"  subCategoryId After: "+subCategoryId);
                    String deptCode = approvalRec.getCodeName("SELECT Dept_code FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
//                    System.out.println("<<<<<deptCode: "+deptCode+"  deptId After: "+deptId);
  //                  deptId = approvalRec.getCodeName("SELECT Dept_Id FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
//                    System.out.println("<<<<<deptId: "+deptId+"  deptId After: "+deptId);
                    String sectionCode = approvalRec.getCodeName("SELECT Section_Code FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionCode: "+sectionCode+"  sectionId After: "+sectionId);
               //     sectionId = approvalRec.getCodeName("SELECT Section_Id FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionId: "+sectionId+"  sectionId After: "+sectionId);
                    if(deptId==""){deptId="0";}
                    if(subCategoryId==""){subCategoryId = "0";}
                    if(sectionId.equals("")){sectionId="0";}
                    if(locationId.equals("")){locationId="0";}
                    if(stateId.equals("")){stateId="0";}
                	
                    int assetcode = bd.getAssetCode();    

//                    System.out.println("<<<<<<assetId=======: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser+"  costprice"+costprice);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                    ps.setString(1, assetId);
                    ps.setString(2, description);
                    ps.setString(3, barcode);
                    ps.setDouble(4, Double.parseDouble(costprice));
                    ps.setString(5, registrationNo);
                    ps.setString(6, make);
                    ps.setString(7, model);
                    ps.setString(8, serialNo);
                    ps.setString(9, engineNo);
                    ps.setString(10, purchaseDate);
                    ps.setString(11, spare1);
                    ps.setString(12, spare2);
                    ps.setString(13, spare3);
                    ps.setString(14, spare4);
                    ps.setString(15, spare5);
                    ps.setString(16, spare6);
                    ps.setString(17, sbucode);
                    ps.setString(18, assetuser);
                    ps.setString(19, comments);
                    ps.setString(20, sighted);
                    ps.setString(21, functional);
                    ps.setInt(22, assetcode);
                    ps.setString(23, branchCode);
                    ps.setInt(24, Integer.parseInt(categoryId));
                    ps.setString(25, bacthId);
                    ps.setTimestamp(26, dbConnection.getDateTime(new java.util.Date()));
                    ps.setString(27, groupNo);
                    ps.setString(28, groupId);
//                    System.out.println("<<<<<groupId: "+groupId+"    groupNo: "+groupNo);
					ps.setDouble(29, Double.parseDouble(accumdep));
					ps.setDouble(30, Double.parseDouble(monthlyDep));
					ps.setDouble(31, Double.parseDouble(nbv));
					ps.setDouble(32, Double.parseDouble(improveCost));
					ps.setDouble(33, Double.parseDouble(improveMonthlydep));
					ps.setDouble(34, Double.parseDouble(improveAccumdep));
					ps.setDouble(35, Double.parseDouble(improveNBV));
//					System.out.println("<<<<<accumdep: "+accumdep+"  monthlyDep: "+monthlyDep+"  nbv: "+nbv+"   improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"  improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
					ps.setDouble(36, Double.parseDouble(improveNBV)+Double.parseDouble(nbv));
					ps.setString(37, suppliedBy);
					ps.setString(38, vendor_account);
					ps.setString(39, maintainedBy);
					ps.setString(40, depreciation_end_date);
					ps.setString(41, depreciation_start_date);
					ps.setString(42, oldassetId);
//					System.out.println("<<<<<<subCategoryId: "+subCategoryId+"  deptId: "+deptId+"  sectionId: "+sectionId+"  locationId: "+locationId+"  stateId: "+stateId+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
					ps.setInt(43, Integer.parseInt(subCategoryId));
					ps.setInt(44, Integer.parseInt(deptId));
					ps.setInt(45, Integer.parseInt(sectionId));
					ps.setInt(46, Integer.parseInt(locationId));
					ps.setInt(47, Integer.parseInt(stateId));
					ps.setString(48, lpo);
					ps.setInt(49, Integer.parseInt(initiator));
					ps.setString(50, itemType);
					
//					System.out.println("<<<<<vendor_account: "+vendor_account);

                    ps.addBatch();
                }
                
                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertAssetProofSelection() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }


        public ArrayList[] findAssetProofSelectionforApprovalBatchId(String tranId,String tranStatus) {


            String assetFilter = " and asset_id in (";
            String selectQuery = "";
            if(tranStatus.equals("R")){selectQuery = "select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'OLD' and process_status = 'REJECTED' ";}
            else{selectQuery = "select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'OLD' and process_status is null ";}
//            String selectQuery =
//                    "select * from am_Asset_Proof where batch_id=? and process_status ='WFA' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
//            System.out.print("selectQuery in : "+selectQuery);
           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
 //               ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");  
                    String supplierName = rs.getString("Supplier_Name"); 
                    String assetMaintain = rs.getString("Asset_Maintenance");
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
 //                   System.out.println("<<<<<supplierName: "+supplierName+"  assetMaintain: "+assetMaintain);
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
 //                   System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);  
                    aset.setAssetMaintain(Integer.parseInt(assetMaintain));
                    aset.setSupplierName(Integer.parseInt(supplierName));
                    listNew.add(aset);
                    
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";
                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofSelectionforApprovalBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
//            System.out.println("the size of listOld is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }
        public ArrayList[] findAssetProofSelectionforApprovalBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery = 
            	 "select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'OLD' and process_status is null ";
//            String selectQuery =
//                    "select * from am_Asset_Proof where batch_id=? and process_status ='WFA' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
//            System.out.print("selectQuery in : "+selectQuery);
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
 //               ps.setString(1, tranId);
               ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");  
                    String supplierName = rs.getString("Supplier_Name"); 
                    String assetMaintain = rs.getString("Asset_Maintenance");
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
  //                  System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);  
                    aset.setAssetMaintain(Integer.parseInt(assetMaintain));
                    aset.setSupplierName(Integer.parseInt(supplierName));
                    listNew.add(aset);
                    
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";
                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofSelectionforApprovalBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
//            System.out.println("the size of listOld is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList findAddedAssetProofApprovalByBatchId(String tranId,String recType) {


 //           String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id='"+tranId+"' and rec_add = '"+recType+"' and new_rec = 'N' and approved_date is null ";
 //           "select * from am_Asset_Proof where batch_id='"+tranId+"' and rec_add = 'YES' and branch_Id = ?  ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
//            System.out.println("selectQuery in findAddedAssetProofApprovalByBatchId>>>>>> " +selectQuery);
           
            ArrayList listadd = new ArrayList();
            ArrayList listaddrec = new ArrayList(2);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
              //  ps.setInt(1, Integer.parseInt(tranId));
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    int supplierName = rs.getInt("Supplier_Name");
                    int maintainedBy = rs.getInt("Asset_Maintenance");
                    String vendorAc = rs.getString("vendor_ac");
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                    aset = new Asset();
                    aset.setId(id);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDescription(description);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
    //                System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);                    
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setAssetMaintain(maintainedBy);
                    aset.setSupplierName(supplierName);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setVendorAc(vendorAc);
                    listadd.add(aset);
                    aset=null;

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAddedAssetProofApprovalByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
            return listadd;

        }
        

        public ArrayList findAddedAssetProofApprovalByBatchId(String tranId,int branch_Id) {


 //           String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof where batch_id='"+tranId+"' and rec_add = 'YES' and branch_Id = "+branch_Id+"  ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
//            System.out.println("=====selectQuery in findAddedAssetProofApprovalBy BatchId: "+selectQuery);
           
            ArrayList listadd = new ArrayList();
            ArrayList listaddrec = new ArrayList(2);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
              //  ps.setInt(1, Integer.parseInt(tranId));
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");
                    String lpo = rs.getString("LPO");
                    int supplierName = rs.getInt("Supplier_Name");
                    int maintainedBy = rs.getInt("Asset_Maintenance");
                    String vendorAc = rs.getString("vendor_ac");
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");
                    String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                    String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                    java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                    aset = new Asset();
                    aset.setId(id);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDescription(description);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
    //                System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);                    
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setSerialNo(serialNo);
                    aset.setLPO(lpo);
                    aset.setVendorAc(vendorAc);
                    aset.setAssetMaintain(maintainedBy);
                    aset.setSupplierName(supplierName);
                    aset.setDatePurchased(purchaseDate);
                    aset.setDepreciationEndDate(depreciationEndDate);
                    aset.setEffectiveDate(effectiveDate);
                    aset.setEffectivedate2(depreciationStartDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setVendorAc(vendorAc);
                    listadd.add(aset);
                    aset=null;

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAddedAssetProofApprovalByBatchId(String tranId,int branch_Id) in BulkUpdateManager-> " +
                        e.getMessage());
            } 

            return listadd;

        }
        

/*
        public ArrayList[] findAssetProofSelectionforApprovalBatchIdOld(String tranId) { 
        	Matanmi
            String assetFilter = " and asset_id in (";
            String validAsset = "";
            String selectQuery =
            "select * from am_Asset_Proof_Selection where batch_id=? and process_status is null ";
            Connection con = null;
            PreparedStatement ps = null;
            System.out.println("<<<<<<tranId: "+tranId);
            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");     
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
                    System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);                    
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";
                    validAsset = approvalRec.getCodeName("SELECT ASSET_ID FROM AM_ASSET WHERE Asset_Id = '"+id+"' ");
                }
                System.out.println("<<<<<<validAsset: "+validAsset);
                if(!validAsset.equals("")){
                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);
                	}
            } catch (Exception e) {
                System.out.println("INFO:Error findAssetProofSelectionforApprovalBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {
                dbConnection.closeConnection(con, ps, rs);
            }
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }
    */    

        public boolean updateAssetProofInsert(ArrayList list, String bacthId,String tranStatus) {
            boolean re = false;
            String query = "";
//            System.out.println("<<<<<<<bacthId: "+bacthId+"    tranStatus: "+tranStatus);  
            if(tranStatus.equalsIgnoreCase("SAVE")){
            query = "update am_Asset_Proof set Description = ?,BAR_CODE = ?,Registration_No = ?,Asset_Make = ?,Asset_Model = ?,Asset_Serial_No = ?,"+
                    "Asset_Engine_No = ?,Spare_1 = ?,Spare_2 = ?,Spare_3 = ?,Spare_4 = ?,Spare_5 = ?,Spare_6 = ?,SBU_CODE = ?,SUB_CATEGORY_ID=?,Dept_ID=?,"+
                    "Section_id=?,Location=?,State=?,LPO=?,ASSET_USER = ?," +
                    "COMMENTS = ?,ASSETSIGHTED = ?,ASSETFUNCTION = ?,Supplier_Name = ?,Vendor_AC = ?,Asset_Maintenance = ?,process_status = NULL,EXPORTED ='NO' where batch_id = ? and ASSET_ID = ?  ";
            }else{
            query = "update am_Asset_Proof set Description = ?,BAR_CODE = ?,Registration_No = ?,Asset_Make = ?,Asset_Model = ?,Asset_Serial_No = ?,"+
                    "Asset_Engine_No = ?,Spare_1 = ?,Spare_2 = ?,Spare_3 = ?,Spare_4 = ?,Spare_5 = ?,Spare_6 = ?,SBU_CODE = ?,SUB_CATEGORY_ID=?,Dept_ID=?,"+
                    "Section_id=?,Location=?,State=?,LPO=?,ASSET_USER = ?," +
                    "COMMENTS = ?,ASSETSIGHTED = ?,ASSETFUNCTION = ?,Supplier_Name = ?,Vendor_AC = ?,Asset_Maintenance = ?,process_status ='WFA',EXPORTED ='NO' where batch_id = ? and ASSET_ID = ?  ";
            }
            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    String subCatId = bd.getSub_category_id();
                    String deptId = bd.getDepartment_id();
                    String sectionId = bd.getSection_id(); 
                    String location = bd.getLocation();
                    String state = bd.getState();
                    String costprice = bd.getCost_price();
                    String model = bd.getModel();
                    String make = bd.getMake();
                    String lpo = bd.getLpo();  
                	String sbucode = bd.getSbu_code();
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String spare3 = bd.getSpare_3();
                	String spare4 = bd.getSpare_4();
                	String spare5 = bd.getSpare_5();
                	String spare6 = bd.getSpare_6();
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
                	String suppliedBy = bd.getSupplied_by();
                	String vendorAc = bd.getVendor_account();
                	String maintianedBy = bd.getMaintained_by();
                	String authorized = bd.getAuthorized_by();
                	String purchaseReason = bd.getReason();	
                	String purchaseDate = bd.getDate_of_purchase();	
                	
                    int assetcode = bd.getAssetCode();      
//                    System.out.println("<<<<<<assetId: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
//                    System.out.println("<<<<<<model: "+model+"  make: "+make+"  sbucode: "+sbucode+"  spare1: "+spare1+"  spare2: "+spare2+"  engineNo: "+engineNo+"  branchCode: "+branchCode+"  registrationNo: "+registrationNo+"  assetcode: "+assetcode+"  serialNo: "+serialNo);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                	if((subCatId=="") || (subCatId.equals("UNKNOWN"))){subCatId = "0";}
                	if((deptId=="") || (deptId.equals("UNKNOWN"))){deptId="0";}
//                	System.out.println("<<<deptId Value :" + deptId);
                	if((sectionId.equals("")) || (sectionId.equals("UNKNOWN"))){sectionId="0";}
                	if((location.equals("")) || (location.equals("UNKNOWN"))){location="0";}
                	if((state.equals("")) || (state.equals("UNKNOWN"))){state="0";}
                	if((make.equals("")) || (make.equals("UNKNOWN"))){make="0";}
//                	state = approvalRec.getCodeName("SELECT state_code FROM am_gb_states WHERE state_name = '"+state+"' ");
 //               	location = approvalRec.getCodeName("SELECT location_id FROM AM_GB_LOCATION WHERE location = '"+location+"' ");
//                    branchCode = approvalRec.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = "+initiatorBranchId+" ");
//                	subCatId = approvalRec.getCodeName("SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_name = '"+subCatId+"' ");
                    String subcategory_code = approvalRec.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = '"+subCatId+"' ");
                    String deptCode = approvalRec.getCodeName("SELECT Dept_code FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
//                    System.out.println("<<<<<deptCode: "+deptCode+"  deptId After: "+deptId);
 //                   deptId = approvalRec.getCodeName("SELECT Dept_Id FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
//                    System.out.println("<<<<<deptId: "+deptId+"  deptId After: "+deptId);
                    String sectionCode = approvalRec.getCodeName("SELECT Section_Code FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionCode: "+sectionCode+"  sectionId After: "+sectionId);
 //                   sectionId = approvalRec.getCodeName("SELECT Section_Id FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionId: "+sectionId+"  sectionId After: "+sectionId);
                    if(deptId==""){deptId="0";}
                    if(subCatId==""){subCatId = "0";}
                    if(sectionId.equals("")){sectionId="0";}
                    if(location.equals("")){location="0";}
                    if(state.equals("")){state="0";}                    
 //                   System.out.println("<<<<<subCatId: "+subCatId+"  sectionId: "+sectionId+"   deptId: "+deptId+"  location: "+location+"   state: "+state);
                    ps.setString(1, description.toUpperCase());
                    ps.setString(2, barcode.toUpperCase());
                    ps.setString(3, registrationNo.toUpperCase());
                    ps.setString(4, make);
                    ps.setString(5, model);
                    ps.setString(6, serialNo);
                    ps.setString(7, engineNo);
                    ps.setString(8, spare1.toUpperCase());
                    ps.setString(9, spare2.toUpperCase());
                    ps.setString(10, spare3.toUpperCase());
                    ps.setString(11, spare4.toUpperCase());
                    ps.setString(12, spare5.toUpperCase());
                    ps.setString(13, spare6.toUpperCase());
                    ps.setString(14, sbucode);
                    ps.setString(15, subCatId);
                    ps.setString(16, deptId);
                    ps.setString(17, sectionId);
                    ps.setString(18, location);
                    ps.setString(19, state);
                    ps.setString(20, lpo);
                    ps.setString(21, assetuser.toUpperCase());
                    ps.setString(22, comments);
                    ps.setString(23, sighted);
                    ps.setString(24, functional);
                    ps.setString(25, suppliedBy);
                    ps.setString(26, vendorAc);
                    ps.setString(27, maintianedBy);
                    ps.setString(28, bacthId);
                    ps.setString(29, assetId);
                    
//                    System.out.println("<<<<<<<assetId: "+assetId);
                    //ps.addBatch();
                    ps.execute();
                }
                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");
                
            } catch (Exception ex) {
                System.out.println("Error updateAssetProofInsert() of BulkUpdateManager -> " + ex);
            } 
            
            System.out.println("Record Lenght: "+d.length);
            return (d.length > 0);
        }

        public ArrayList[] findBulkAssetProofRejectionByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_Asset_Proof_Selection where batch_id=? order by group_no,asset_id ";
 //           "select * from am_Asset_Proof where batch_id=? and process_status ='REJECTED' order by group_no,asset_id ";
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
            
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList listNewAdd = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setCost(costPrice);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
 //               System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetProofByID(assetFilter+" order by ASSET_ID",tranId);
            } catch (Exception e) {
                System.out.println("INFO:Error findBulkAssetProofRejectionByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
            
            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            System.out.println("the size of listOld is >>>>>> " + listOld.size());
            listNewOld[1] = listOld;
  //          listNewOld[1] = listNewAdd;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public boolean updateBulkAssetProofInsert(ArrayList list, String bacthId) {
            boolean re = false;
//            System.out.println("<<<<<<<bacthId: "+bacthId);
            String query = "update am_Asset_Proof set Description = ?,BAR_CODE = ?,Registration_No = ?,Asset_Make = ?,Asset_Model = ?,Asset_Serial_No = ?,"+
                    "Asset_Engine_No = ?,Spare_1 = ?,Spare_2 = ?,SBU_CODE = ?,ASSET_USER = ?," +
                    "COMMENTS = ?,ASSETSIGHTED = ?,ASSETFUNCTION = ? where batch_id = ? and ASSET_ID = ? and PROCESS_STATUS = 'REJECTED' ";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    String costprice = bd.getCost_price();
                    String model = bd.getModel();
                    String make = bd.getMake();
                	String sbucode = bd.getSbu_code();
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
                	String suppliedBy = bd.getSupplied_by();
                	String vendorAc = bd.getVendor_account();
                	String maintianedBy = bd.getMaintained_by();
                	String authorized = bd.getAuthorized_by();
                	String purchaseReason = bd.getReason();	
                	String purchaseDate = bd.getDate_of_purchase();	
                	
                    int assetcode = bd.getAssetCode();      
//                    System.out.println("<<<<<<assetId: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                   
                    ps.setString(1, description.toUpperCase());
                    ps.setString(2, barcode.toUpperCase());
                    ps.setString(3, registrationNo);
                    ps.setString(4, make);
                    ps.setString(5, model);
                    ps.setString(6, serialNo);
                    ps.setString(7, engineNo);
                    ps.setString(8, spare1.toUpperCase());
                    ps.setString(9, spare2.toUpperCase());
                    ps.setString(10, sbucode);
                    ps.setString(11, assetuser);
                    ps.setString(12, comments);
                    ps.setString(13, sighted);
                    ps.setString(14, functional);
                    ps.setString(15, bacthId);
                    ps.setString(16, assetId);
//                    System.out.println("<<<<<<<assetId: "+assetId);
                    ps.addBatch();
                }
                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error updateAssetProofInsert() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }


        public int insertBulkAsset2Transfer(ArrayList list, int bacthId,int categoryId,String newassetId) {
            boolean re = false;
            int assetCode = 0;
            String query = "insert into am_gb_bulkAsset2transfer ( " +
                    "ASSET_ID,Description,OLDBRANCH_ID," +
                    "OLDDEPT_ID,OLDSBU_CODE," +
                    "OLDSECTION_ID,OLDASSET_USER," +
                    "NEWBRANCH_ID,NEWDEPT_ID,NEWSBU_CODE,NEWSECTION_ID,NEWASSET_USER," +
                    "BATCH_ID,TRANSFERBY_ID,CATEGORY_ID,NEW_ASSET_ID,COST_PRICE,ACCUM_DEP,MONTHLY_DEP,NBV,TRANSFER_DATE,ASSET_CODE)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String asset_id = bd.getAsset_id();
                    newassetId = bd.getNewasset_id();
                    String description = bd.getDescription();
                    String branchId = bd.getBranch_id();
                    String dept = bd.getDepartment_id();
                    String sbu = bd.getSbu_code();
                    String section = bd.getSection_id();
                    String assetuser = bd.getUser();
                    String newbranchId = bd.getNewbranch_id();
                    String newdept = bd.getNewdepartment_id();
                    String newsbu = bd.getNewsbu_code();
                    String newsection = bd.getNewsection_id();
                    String newassetuser = bd.getNewuser();
                    String cost_price = bd.getCost_price();
                    String accum_dep = bd.getAccumdep();
                    String nbv = bd.getNbv();
                    assetCode = bd.getAssetCode();
                    int category_Id = Integer.parseInt(bd.getCategory_id());
                    String monthly_dep = bd.getMonthlydep();
                    int transferby = bd.getUser_id() == null ? 0 : Integer.parseInt(bd.getUser_id());
                    if (branchId == null || branchId.equals("")) {
                    	branchId = "0";
                    }
                    if (description == null || description.equals("")) {
                        description = "";
                    }
                    if (section == null || section.equals("")) {
                    	section = "0";
                    }
                    if (newbranchId == null || newbranchId.equals("")) {
                    	newbranchId = "0";
                    }
                    if (newdept == null || newdept.equals("")) {
                    	newdept = "0";
                    }
                    if (newsbu == null || newsbu.equals("")) {
                    	newsbu = "0";
                    }
                    if (assetuser == null || assetuser.equals("")) {
                        assetuser = "0";
                    }
                    if (newsection == null || newsection.equals("")) {
                    	newsection = "0";
                    }
                    if (newassetuser == null || newassetuser.equals("")) {
                    	newassetuser = "0";
                    }
                    if (sbu == null || sbu.equals("")) {
                        sbu = "0";
                    }
                    if (dept == null || dept.equals("")) {
                    	dept = "0";
                    }                
                    if (asset_id == null || asset_id.equals("")) {
                        asset_id = "0";
                    }
                    if (newbranchId == null || branchId.equals("")) {
                        newbranchId = "0";
                    }
//                    System.out.println("InsertBulkAsset2Transfer branchId: "+branchId);
                    ps.setString(1, asset_id);
                    ps.setString(2, description);
                    ps.setString(3, branchId);
                    ps.setString(4, dept);
                    ps.setString(5, sbu);
                    ps.setString(6, section);
                    ps.setString(7, assetuser);
                    ps.setString(8, newbranchId);
                    ps.setString(9, newdept);
                    ps.setString(10, newsbu);
                    ps.setString(11, newsection);
                    ps.setString(12, newassetuser);
                    ps.setString(13, String.valueOf(bacthId));
                    ps.setInt(14, transferby);
                    ps.setInt(15, category_Id);
                    ps.setString(16, newassetId);
                    ps.setString(17, cost_price);
                    ps.setString(18, accum_dep);
                    ps.setString(19, monthly_dep);
                    ps.setString(20, nbv);
                    ps.setTimestamp(21, dbConnection.getDateTime(new java.util.Date()));
                    ps.setInt(22, assetCode);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBulkAsset2Transfer() of BulkUpdateManager -> " + ex);
            } 

            return assetCode;
        }

        public ArrayList[] findAsset2TransferByBatchId(String tranId) {

        //	System.out.println("findAsset2TransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkAsset2Transfer where batch_id=? ";

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                AssetRecordsBean aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String oldbranchId = rs.getString("oldbranch_id");
                    String olddeptId = rs.getString("olddept_id");
                    String oldsbucode = rs.getString("oldSBU_CODE");
                    String oldsectionId = rs.getString("oldsection_id");
                    String oldassetuser = rs.getString("oldAsset_User");
                    String newassetId = rs.getString("NEW_ASSET_ID");
                    String newbranchId = rs.getString("newbranch_id");
                    String newdeptId = rs.getString("newdept_id");
                    String newsbucode = rs.getString("newSBU_CODE");
                    String newsectionId = rs.getString("newsection_id");
                    String newassetuser = rs.getString("newAsset_User");
                    String batchId = rs.getString("Batch_id");
                    String transferbyId = rs.getString("Transferby_id");
                    int assetCode =  rs.getInt("ASSET_CODE");
                    
                     aset = new AssetRecordsBean();
                    aset.setAsset_id(id);
                    aset.setDescription(description);
                    aset.setRegistration_no(registrationNo);
                    aset.setBranch_id(oldbranchId);
                    aset.setDepartment_id(olddeptId);
                    aset.setSbu_code(oldsbucode);
                    aset.setSection_id(oldsectionId);
                    aset.setUser(oldassetuser);
                    aset.setNewbranch_id(newbranchId);
                    aset.setNewdepartment_id(newdeptId);
                    aset.setNewsbu_code(newsbucode);
                    aset.setNewsection_id(newsectionId);
                    aset.setNewuser(newassetuser);
                    aset.setNewasset_id(newassetId);
                    aset.setAssetCode(assetCode);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                } 

                boolean index = assetFilter.endsWith(",");
            //    System.out.println("findAsset2TransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
              //      System.out.println("findAsset2TransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAsset2ByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAsset2TransferByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList findAsset2ByID(String queryFilter) {



            String selectQuery =
                    "SELECT ASSET_ID,REGISTRATION_NO," +
                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model,COST_PRICE,Asset_Make," +
                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason,Date_purchased," +
                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                    "FROM AM_ASSET2  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
          //  System.out.println("the query in findAssetByID is <<<<<<<<<<<<< " + selectQuery);

            

            ArrayList listOld = new ArrayList();

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);

                ResultSet rs = ps.executeQuery();
                Asset aset =null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String vendorAC = rs.getString("Vendor_AC");
                    String assetMake = rs.getString("Asset_Make");
                    String assetModel = rs.getString("Asset_Model");
                    String AssetSerialNo = rs.getString("Asset_Serial_No");
                    String AssetEngineNo = rs.getString("Asset_Engine_No");
                    int SupplierName = rs.getInt("Supplier_Name");
                    String assetUser = rs.getString("ASSET_USER");
                    int assetMaintenance = rs.getInt("Asset_Maintenance");
                    String authorizedBy = rs.getString("Authorized_By");
                    String purchaseReason = rs.getString("Purchase_Reason");
                    String sbuCode = rs.getString("SBU_CODE");
                    String deptCode = rs.getString("DEPT_CODE"); 
                    String deptid = rs.getString("Dept_ID");                 
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String barCode = rs.getString("BAR_CODE");
                    double costPrice = rs.getDouble("COST_PRICE");
                    String purchaseDate = rs.getString("Date_purchased");
                    String branchId = String.valueOf(rs.getInt("branch_id"));
                    String subcategoryCode = rs.getString("sub_category_code"); 
                    String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
                     aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description.toUpperCase());
                    aset.setRegistrationNo(registrationNo.toUpperCase());
                    aset.setVendorAc(vendorAC);
                    aset.setAssetMake(assetModel);
                    aset.setSerialNo(AssetSerialNo);
                    aset.setEngineNo(AssetEngineNo);
                    aset.setSupplierName(SupplierName);
                    aset.setAssetUser(assetUser);
                    aset.setAssetMaintain(assetMaintenance);
                    aset.setAuthorizeBy(authorizedBy);
                    aset.setPurchaseReason(purchaseReason);
                    aset.setSbuCode(sbuCode);
                    aset.setDepcode(deptCode);
                    aset.setDepartmentId(deptid);
                    aset.setSpare1(spare1.toUpperCase());
                    aset.setSpare2(spare2.toUpperCase());
                    aset.setBarCode(barCode.toUpperCase());
                    aset.setBranchId(branchId);
                    aset.setSubcategoryCode(subcategoryCode);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setCost(costPrice);
                    aset.setDatePurchased(purchaseDate);
                    aset.setAssetMake(assetMake);

                    listOld.add(aset);
                    aset = null;

                }



            } catch (Exception e) {
                System.out.println("INFO:Error findAsset2ByID() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
     //       System.out.println("the size of listOld is >>>>>>> " + listOld.size());
            return listOld;

        }

        public boolean BulkAsset2Transfer(ArrayList list) {

            ArrayList newList = null;
 //           AssetRecordsBean asset = null;
            magma.AssetRecordsBean asset = null;
           
            htmlUtil = new HtmlUtility();
             
            int[] d =null;   
            String query= "UPDATE AM_ASSET2 SET ASSET_ID=?," +
                    " OLD_ASSET_ID=?,ASSET_USER=?,Branch_ID=?,Dept_ID=?,Section_id=?," +
                    "SBU_CODE=?,BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? WHERE ASSET_ID=?" ;
            System.out.println("I am in bulk Transfer2 manager is >>>>>>>>>>> "+list.size());
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                 for (int i = 0; i < list.size(); ++i) {
            asset = (magma.AssetRecordsBean) list.get(i);
                    String newassetId = asset.getNewasset_id();
     //               System.out.println("the New asset id in bulk Transfer manager is >>>>>>>>>>> "+newassetId);
                    String oldassetId = asset.getAsset_id();
     //               System.out.println("the Old asset id in bulk Transfer manager is >>>>>>>>>>> "+oldassetId);
                    String newbranchId = asset.getNewbranch_id();
                    String newdeptId = asset.getNewdepartment_id();
                    String description = asset.getDescription();
                    String newsbucode = asset.getNewsbu_code();
                    String newsectionId = asset.getNewsection_id();
                    String newassetUser = asset.getNewuser();
                    String branchQuery = "SELECT branch_code FROM   am_ad_branch WHERE branch_id = '"+newbranchId+"'";
                    String branch_code = htmlUtil.findObject(branchQuery);
                    String deptQuery = "SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+newdeptId+"'";
                    String dept_code = htmlUtil.findObject(deptQuery);
                    String sectionQuery = "SELECT section_code FROM   am_ad_section WHERE section_id = '"+newsectionId+"'";
                    String section_code = htmlUtil.findObject(sectionQuery);                    
      //              System.out.println("the Old asset id in bulk Transfer manager is >>>>>>>>>>> " + oldassetId+"   New Asset Id:  "+newassetId);
                    asset=null;
                   

                 ps.setString(1,newassetId);
                 ps.setString(2,oldassetId);
                 ps.setString(3,newassetUser);
                 ps.setInt(4,Integer.parseInt(newbranchId));
                 ps.setInt(5,Integer.parseInt(newdeptId));
                 ps.setInt(6,Integer.parseInt(newsectionId));
                 ps.setString(7,newsbucode);
                 ps.setString(8,branch_code);
                 ps.setString(9,dept_code);
                 ps.setString(10,section_code);
                 ps.setString(11,oldassetId);

                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: BulkAsset2Transfer()>>>>>" + ex);
            } 
                return (d.length > 0);
        }

        public ArrayList findAssets2ByBatchId(String tranId) {


            //String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkAsset2Transfer where batch_id=? ";

            
            ArrayList listNew = new ArrayList();
           // ArrayList listOld = new ArrayList();
           // ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();
                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String vendorAC = rs.getString("Vendor_AC");
                    String assetModel = rs.getString("Asset_Model");
                    String AssetSerialNo = rs.getString("Asset_Serial_No");
                    String AssetEngineNo = rs.getString("Asset_Engine_No");
                    int SupplierName = rs.getInt("Supplier_Name");
                    String assetUser = rs.getString("ASSET_USER");
                    int assetMaintenance = rs.getInt("Asset_Maintenance");
                    String authorizedBy = rs.getString("Authorized_By");
                    String purchaseReason = rs.getString("Purchase_Reason");
                    String sbuCode = rs.getString("SBU_CODE");
                    String deptCode = rs.getString("DEPT_CODE"); 
                    String deptId = rs.getString("Dept_ID");   
                    String subcategoryCode = rs.getString("sub_category_code"); 
                    String subcategoryId = rs.getString("sub_category_ID"); 
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");                
                    String barCode = rs.getString("BAR_CODE");
                    String branchId = rs.getString("branch_id");

                     aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setRegistrationNo(registrationNo);
                    aset.setVendorAc(vendorAC);
                    aset.setAssetMake(assetModel);
                    aset.setSerialNo(AssetSerialNo);
                    aset.setEngineNo(AssetEngineNo);
                    aset.setSupplierName(SupplierName);
                    aset.setAssetUser(assetUser);
                    aset.setAssetMaintain(assetMaintenance);
                    aset.setAuthorizeBy(authorizedBy);
                    aset.setPurchaseReason(purchaseReason);
                    aset.setSbuCode(sbuCode);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setBarCode(barCode);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(deptId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSubcategoryCode(subcategoryCode);
                    aset.setDepcode(deptCode);

                    
                    listNew.add(aset);
                    aset=null;
                   // assetFilter = assetFilter + "'" + id + "',";

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAssets2ByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            return listNew;

        }


        public boolean insertBulkAsset2Update(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into am_gb_bulkAsset2Update ( " +
                    "REGISTRATION_NO,Description,VENDOR_AC," +
                    "ASSET_MODEL,ASSET_SERIAL_NO," +
                    "ASSET_ENGINE_NO,SUPPLIER_NAME," +
                    "ASSET_USER,ASSET_MAINTENANCE," +
                    "AUTHORIZED_BY,PURCHASE_REASON,SBU_CODE," +
                    "SPARE_1,SPARE_2,SPARE_3,SPARE_4,SPARE_5,SPARE_6,BAR_CODE, ASSET_ID,BATCH_ID,BRANCH_ID,DEPT_ID,SUB_CATEGORY_ID )" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String registration = bd.getRegistration_no();
                    String description = bd.getDescription();
                    String vendoracc = bd.getVendor_account();
                    String model1 = bd.getModel();
                    String serial = bd.getSerial_number();
                    String engine = bd.getEngine_number();
                    int suppliedby = bd.getSupplied_by() == null ? 0 : Integer.parseInt(bd.getSupplied_by());
                    String assetuser = bd.getUser();
                    int maintained = bd.getMaintained_by() == null ? 0 : Integer.parseInt(bd.getMaintained_by());
                    String authorized = bd.getAuthorized_by();
                    String reason1 = bd.getReason();
                    String sbu = bd.getSbu_code();
                    String dept = bd.getDepartment_id();
                    String spare1 = bd.getSpare_1();
                    String spare2 = bd.getSpare_2();
                    String spare3 = bd.getSpare_3();
                    String spare4 = bd.getSpare_4();
                    String spare5 = bd.getSpare_5();
                    String spare6 = bd.getSpare_6();                
                    String barcode = bd.getBar_code();
                    String lpo1 = bd.getLpo();
                    String asset_id1 = bd.getAsset_id();
                    String branchId = bd.getBranch_id();
                    String subcat = bd.getSub_category_id();

                    if (registration == null || registration.equals("")) {
                        registration = "0";
                    }
                    if (description == null || description.equals("")) {
                        description = "";
                    }
                    if (vendoracc == null || vendoracc.equals("")) {
                        vendoracc = "0";
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
                    //if (suppliedby == null || suppliedby.equals("")) {
                    //    suppliedby = "0";
                    // }
                    if (assetuser == null || assetuser.equals("")) {
                        assetuser = "0";
                    }
                    //if (maintained == null) {
                    //    maintained = 0;
                    //}
                    if (authorized == null || authorized.equals("")) {
                        authorized = "0";
                    }
                    if (reason1 == null || reason1.equals("")) {
                        reason1 = "0";
                    }
                    if (sbu == null || sbu.equals("")) {
                        sbu = "0";
                    }
                    if (dept == null || dept.equals("")) {
                    	dept = "0";
                    }                
                    if (subcat == null || subcat.equals("")) {
                        subcat = "0";
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
                    if (branchId == null || branchId.equals("")) {
                        branchId = "0";
                    }

                    ps.setString(1, registration);
                    ps.setString(2, description);
                    ps.setString(3, vendoracc);
                    ps.setString(4, model1);
                    ps.setString(5, serial);
                    ps.setString(6, engine);
                    ps.setInt(7, suppliedby);
                    ps.setString(8, assetuser);
                    ps.setInt(9, maintained);
                    ps.setString(10, authorized);
                    ps.setString(11, reason1);
                    ps.setString(12, sbu);
                    ps.setString(13, spare1);
                    ps.setString(14, spare2);
                    ps.setString(15, spare3);
                    ps.setString(16, spare4);
                    ps.setString(17, spare5);
                    ps.setString(18, spare6);
                    ps.setString(19, barcode);
                    ps.setString(20, asset_id1);
                    ps.setInt(21, bacthId);
                    ps.setString(22, branchId);
                    ps.setString(23, dept);
                    ps.setString(24, subcat);

                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBulkAsset2Update() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }


        public ArrayList[] findAsset2ByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkAsset2Update where batch_id=? ";

            

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String vendorAC = rs.getString("Vendor_AC");
                    String assetModel = rs.getString("Asset_Model");
                    String AssetSerialNo = rs.getString("Asset_Serial_No");
                    String AssetEngineNo = rs.getString("Asset_Engine_No");
                    int SupplierName = rs.getInt("Supplier_Name");
                    String assetUser = rs.getString("ASSET_USER");
                    int assetMaintenance = rs.getInt("Asset_Maintenance");
                    String authorizedBy = rs.getString("Authorized_By");
                    String purchaseReason = rs.getString("Purchase_Reason");
                    String sbuCode = rs.getString("SBU_CODE");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String barCode = rs.getString("BAR_CODE");
                    String branchId = rs.getString("branch_id");
                    String deptId = rs.getString("dept_id");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    
                     aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setRegistrationNo(registrationNo);
                    aset.setVendorAc(vendorAC);
                    aset.setAssetMake(assetModel);
                    aset.setSerialNo(AssetSerialNo);
                    aset.setEngineNo(AssetEngineNo);
                    aset.setSupplierName(SupplierName);
                    aset.setAssetUser(assetUser);
                    aset.setAssetMaintain(assetMaintenance);
                    aset.setAuthorizeBy(authorizedBy);
                    aset.setPurchaseReason(purchaseReason);
                    aset.setSbuCode(sbuCode);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);
                    aset.setBarCode(barCode);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(deptId);
                    aset.setSubcategoryId(subcategoryId);
                    
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAsset2ByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAsset2ByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList[] findAssetTransferDisplayByBatchId(String tranId) {

        	System.out.println("findAssetTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkTransfer where batch_id=?  order by ASSET_ID ";

             ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                AssetRecordsBean aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String oldbranchId = rs.getString("oldbranch_id");
                    String olddeptId = rs.getString("olddept_id");
                    String oldsbucode = rs.getString("oldSBU_CODE");
                    String oldsectionId = rs.getString("oldsection_id");
                    String oldassetuser = rs.getString("oldAsset_User");
                    String newassetId = rs.getString("NEW_ASSET_ID");
                    String newbranchId = rs.getString("newbranch_id");
                    String newdeptId = rs.getString("newdept_id");
                    String newsbucode = rs.getString("newSBU_CODE");
                    String newsectionId = rs.getString("newsection_id");
                    String newassetuser = rs.getString("newAsset_User");
                    String batchId = rs.getString("Batch_id");
                    String transferbyId = rs.getString("Transferby_id");
                    int assetCode =  rs.getInt("ASSET_CODE");
                    
                     aset = new AssetRecordsBean();
                    aset.setAsset_id(id);
                    aset.setDescription(description);
                    aset.setRegistration_no(registrationNo);
                    aset.setBranch_id(oldbranchId);
                    aset.setDepartment_id(olddeptId);
                    aset.setSbu_code(oldsbucode);
                    aset.setSection_id(oldsectionId);
                    aset.setUser(oldassetuser);
                    aset.setNewbranch_id(newbranchId);
                    aset.setNewdepartment_id(newdeptId);
                    aset.setNewsbu_code(newsbucode);
                    aset.setNewsection_id(newsectionId);
                    aset.setNewuser(newassetuser);
                    aset.setNewasset_id(newassetId);
                    aset.setAssetCode(assetCode);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                } 

                boolean index = assetFilter.endsWith(",");
            //    System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
              //      System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter +  ")  order by ASSET_ID";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetforDisplayByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetTransferDisplayByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList findAssetforDisplayByID(String queryFilter) {
            String selectQuery =
                    "SELECT ASSET_ID,REGISTRATION_NO," +
                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model,COST_PRICE,Asset_Make," +
                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason,Date_purchased," +
                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                    "FROM AM_ASSET  WHERE OLD_ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
            System.out.println("the query in findAssetforDisplayByID is <<<<<<<<<<<<< " + selectQuery);

           

            ArrayList listOld = new ArrayList();

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);

                ResultSet rs = ps.executeQuery();
                Asset aset =null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String vendorAC = rs.getString("Vendor_AC");
                    String assetMake = rs.getString("Asset_Make");
                    String assetModel = rs.getString("Asset_Model");
                    String AssetSerialNo = rs.getString("Asset_Serial_No");
                    String AssetEngineNo = rs.getString("Asset_Engine_No");
                    int SupplierName = rs.getInt("Supplier_Name");
                    String assetUser = rs.getString("ASSET_USER");
                    int assetMaintenance = rs.getInt("Asset_Maintenance");
                    String authorizedBy = rs.getString("Authorized_By");
                    String purchaseReason = rs.getString("Purchase_Reason");
                    String sbuCode = rs.getString("SBU_CODE");
                    String deptCode = rs.getString("DEPT_CODE"); 
                    String deptid = rs.getString("Dept_ID");                 
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String barCode = rs.getString("BAR_CODE");
                    double costPrice = rs.getDouble("COST_PRICE");
                    String purchaseDate = rs.getString("Date_purchased");
                    String branchId = String.valueOf(rs.getInt("branch_id"));
                    String subcategoryCode = rs.getString("sub_category_code"); 
                    String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
                     aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setRegistrationNo(registrationNo);
                    aset.setVendorAc(vendorAC);
                    aset.setAssetMake(assetModel);
                    aset.setSerialNo(AssetSerialNo);
                    aset.setEngineNo(AssetEngineNo);
                    aset.setSupplierName(SupplierName);
                    aset.setAssetUser(assetUser);
                    aset.setAssetMaintain(assetMaintenance);
                    aset.setAuthorizeBy(authorizedBy);
                    aset.setPurchaseReason(purchaseReason);
                    aset.setSbuCode(sbuCode);
                    aset.setDepcode(deptCode);
                    aset.setDepartmentId(deptid);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setBarCode(barCode);
                    aset.setBranchId(branchId);
                    aset.setSubcategoryCode(subcategoryCode);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setCost(costPrice);
                    aset.setDatePurchased(purchaseDate);
                    aset.setAssetMake(assetMake);

                    listOld.add(aset);
                    aset = null;

                }



            } catch (Exception e) {
                System.out.println("INFO:Error findAssetforDisplayByID() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
            return listOld;

        }

        

        /**
         * insertStockDetails
         *
         * @return boolean
         */
        public boolean insertStockDetails(ArrayList list,String histId,String invoiceNo,String warehouseCode) throws Exception, Throwable
        {
        	//subcategory_id =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+group_id+"'");
    /*    	System.out.println("branch_id ::::::::: " + branch_id);
            System.out.println("department_id ::::::::: " + department_id);
            System.out.println("section_id ::::::::: " + section_id);
            System.out.println("category_id ::::::::: " + category_id);
            System.out.println("Branch Code : " +code.getBranchCode(branch_id));
            System.out.println("Category Code " + code.getCategoryCode(category_id));
            System.out.println("Sub Category Id : " +subcategory_id);
            System.out.println("Sub Category Code " + code.getSubCategoryCode(subcategory_id));        */
        	 magma.AssetRecordsBean bd = null;
        	 boolean result = true;
            
             String lpo = "";
             String branch_id = "";
             String user_id = "";
             int assetCode = 0;
             int[] d = null;
             String create_Query = "INSERT INTO ST_STOCK        " +
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
                     "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
                     "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," +
                     "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code," +
                     "memo,memoValue,INTEGRIFY,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,QUANTITY,WAREHOUSE_CODE,ITEM_CODE,ZONE_CODE, REGION_CODE,PROJECT_CODE,UNIT_PRICE,ITEMTYPE) " +

                     "VALUES" +
                     "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                     "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                     String create_Archive_Query = "INSERT INTO ST_STOCK_ARCHIVE         " +
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
                     "MULTIPLE,PROVINCE, WAR_START_DATE, WAR_MONTH, " +
                     "WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE ," + 
                     "GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,system_ip,asset_code," +
                     "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, REGION_CODE,PROJECT_CODE,ITEMTYPE ) " +
                     "VALUES" +
                     "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                     "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                     /*
                      *First Create Asset Records
                      * and then determine if it
                      * should be made available for fleet.
                      */
                     try 
                     {
                  	   
        	 System.out.println("list size in insertStockDetails::::::::: " + list.size());
            for (int i = 0; i < list.size(); i++) {
                bd = (magma.AssetRecordsBean) list.get(i);

                String itemTypeCode = bd.getDescription();
                String itemType = bd.getItemType();
                String costprice = bd.getCost_price();
                int qty = Integer.parseInt(bd.getQty());
                branch_id = bd.getBranch_id();
                String AssetMake = bd.getMake();
                String dept_id = bd.getDepartment_id();
                String section_id = bd.getSection_id();
                String categoryCode = bd.getCategory_id();
                String Datepurchased = bd.getDate_of_purchase();
//                String categoryCode = bd.getCatCode();
                String category_id = approvalRec.getCodeName(" SELECT CATEGORY_ID FROM ST_STOCK_CATEGORY WHERE CATEGORY_CODE = '"+categoryCode+"' ");
                String deptCode = approvalRec.getCodeName(" SELECT Dept_code FROM am_ad_department WHERE Dept_Id = "+dept_id+" ");
                String sectionCode = approvalRec.getCodeName(" SELECT Section_Code FROM am_ad_section WHERE Section_ID = "+section_id+" ");
                String AssetMaintenance = bd.getMaintained_by();
                String SupplierName = bd.getSupplied_by();
                String AssetUser = bd.getUser();
                String Driver = bd.getDriver();
                String State = bd.getState();
                String location = bd.getLocation();
                String sub_category_id = bd.getSub_category_id();
                String itemtypeCode = bd.getDescription();
                String VatableCost = bd.getVatable_cost();
                String vatrate = bd.getVatRate();
                String whtrate = bd.getWhtRate();
                String Description =approvalRec.getCodeName(" SELECT DESCRIPTION from ST_INVENTORY_ITEMS where ITEM_CODE = '"+itemTypeCode+"'");
                int packQuantity =Integer.parseInt(approvalRec.getCodeName("SELECT COALESCE(b.PACK_QUANTITY, 0) FROM ST_INVENTORY_ITEMS a, ST_MEASURING_UNIT b WHERE a.MEASURING_CODE = b.UNIT_CODE AND ITEM_CODE = '"+itemTypeCode+"'"));
                if(packQuantity>0 ){qty = qty/packQuantity;}
//              System.out.println("<<<<<<qty " +qty+"   branch_id: "+branch_id+"  costprice: "+costprice+"  Description: "+Description+"  category_id: "+category_id+"   vatrate: "+vatrate+"  warehouseCode: "+warehouseCode+"  itemTypeCode: "+itemTypeCode);
//                System.out.println("<<<<<<section_id " +section_id+"   dept_id: "+dept_id+"  itemTypeCode: "+itemTypeCode+"  Description: "+Description+"  category_id: "+category_id+"   vatrate: "+vatrate+"  warehouseCode: "+warehouseCode+"  itemType: "+itemType);
//                asset_id_new = new legend.AutoIDSetup().getIdentity(branch_id,dept_id, section_id, category_id);
                String SubjectTOVat = "N";
                String WhTax = "N";
                lpo = bd.getLpo();
                String barCode = "0";
                String branchCode = bd.getBranch_Code();
                String groupid = bd.getGroup_id();
                String sbuCode = bd.getSbu_code();
//                String sectionCode = bd.getSection();
//                String deptCode = bd.getDepartment_code();
                String systemIp = "0";
                String integrifyId = "0";
                String supervisor = bd.getSupervisor();
                String subcategoryCode = bd.getSubcatCode();
                String projectCode = bd.getProjectCode();
                String spare_1 = "";
                String spare_2 = "";
                String spare_3 = "";
                String spare_4 = ""; 
                String spare_5 = "";
                String spare_6 = "";
                String zoneCode = "";
                String regionCode = "";
                String AssetStatus = "PENDING";
                if(vatrate!="0"){SubjectTOVat = "Y";}
                if(whtrate!="0"){WhTax = "Y";}
 //               System.out.print("=====>VatableCost: "+VatableCost+"   vatrate: "+vatrate);
                double vatamount = Double.parseDouble(VatableCost)*(Double.parseDouble(vatrate)/100);
                double CostPrice = Double.parseDouble(VatableCost)+vatamount;
                double whtaxamount = Double.parseDouble(VatableCost)*(Double.parseDouble(whtrate)/100);
//                System.out.print("=====>CostPrice: "+CostPrice+"  vatamount: "+vatamount+"  whtaxamount: "+whtaxamount+"  vatrate: "+vatrate+"  whtrate: "+whtrate);
                String residualvalue = "0";
                String VendorAC = "0";
                String AssetModel = "0"; 
                String AssetSerialNo = "0";
                String AssetEngineNo = "0";
                String AuthorizedBy = "0";
                String PurchaseReason = "";
				String province = "0";
				String noOfMonths = "0";
				String residual_value = "0";
				String warrantyStartDate = null;   
				String expiryDate = null;
				String memo = "N";
				String memoValue = "0";			
				String amountPTD = "0.0";
				String require_depreciation = "Y";
			    String require_redistribution = "N";
			    String who_to_remind = "";
			    String email_1 = "";
			    String who_to_remind_2 = "";
			    String email2 = "";
			    String raise_entry = "N";    
//        			    String spare_1 = "";
			   // String status = "";   
			    
			    String multiple = "N";
//        			    String spare_2 = "";
			    String partPAY = "N";
			    String fullyPAID = "Y";
			    String deferPay = "N";
			    int selectTax = 0;
			    String MacAddress = "";
			    String SystemIp = "";
			    String section = "";  
			    String strnewDateMonth = "";
			    String createQuery = "";
			    String RegistrationNo = "0";
			    String rate = "0";
            assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("ST_STOCK"));
//            System.out.println("New Asset_ID ::::::::: " + asset_id_new);

            if (AssetMake == null || AssetMake.equals("")) {
            	AssetMake = "0";
            }
            if (AssetMaintenance == null || AssetMaintenance.equals("")) {
            	AssetMaintenance = "0";
            }
            if (SupplierName == null || SupplierName.equals("")) {
            	SupplierName = "0";
            }
            if (AssetUser == null || AssetUser.equals("")) {
            	AssetUser = "";
            }
            if (location == null || location.equals("")) {
                location = "0";
            }
            if (Driver == null || Driver.equals("")) {
            	Driver = "0";
            }
            if (State == null || State.equals("")) {
            	State = "0";
            }
            if (dept_id == null || dept_id.equals("")) {
            	dept_id = "0";
            }

            if (branch_id == null || branch_id.equals("")) {
                branch_id = "0";
            }
            if (category_id == null || category_id.equals("")) {
                category_id = "0";
            }
            if (sub_category_id == null || sub_category_id.equals("")) {
                sub_category_id = "0";
            }
            
            if (residualvalue == null || residualvalue.equals("")) {
            	residualvalue = "0";
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
//            System.out.println("======category_id: "+category_id);
                   asset_id_new = new legend.AutoIDSetup().getIdentityforStock(branch_id,
                		   dept_id, section_id, category_id);
//                   System.out.println("======asset_id_new: "+asset_id_new);
               //    double residualValue = Double.parseDouble(residualvalue);
                //asset_costPrice = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
                   Connection con = dbConnection.getConnection("legendPlus");
                   PreparedStatement ps = con.prepareStatement(create_Query);
                ps.setString(1, asset_id_new);
                ps.setString(2, RegistrationNo);
                ps.setInt(3, Integer.parseInt(branch_id));
                ps.setInt(4, Integer.parseInt(dept_id));
                ps.setInt(5, Integer.parseInt(section_id));
                ps.setInt(6, Integer.parseInt(category_id));
                ps.setString(7, Description);
                ps.setString(8, VendorAC);
                ps.setString(9, Datepurchased);
                ps.setString(10, rate);
                ps.setString(11, AssetMake);
                ps.setString(12, AssetModel);
                ps.setString(13, AssetSerialNo);
                ps.setString(14, AssetEngineNo);
                ps.setInt(15, Integer.parseInt(SupplierName));
                ps.setString(16, AssetUser);
                ps.setInt(17, Integer.parseInt(AssetMaintenance));
                ps.setInt(18, 0);
                ps.setInt(19, 0);
                ps.setDouble(20, CostPrice);
                ps.setDouble(21, CostPrice-CostPrice-Double.parseDouble(residualvalue));
                ps.setString(22, Datepurchased);
                ps.setDouble(23, Double.parseDouble(residualvalue));
                ps.setString(24, AuthorizedBy);
                ps.setString(25, Datepurchased);
                ps.setString(26, Datepurchased);
                ps.setString(27, PurchaseReason);
                ps.setString(28, "0");       
                ps.setString(29, "0");
                ps.setInt(30, Integer.parseInt(location));
                ps.setString(31, "0");;
                ps.setDouble(32, Double.parseDouble(VatableCost));
                ps.setDouble(33, vatamount);
                ps.setString(34, WhTax);
                ps.setDouble(35, whtaxamount);
                ps.setString(36, require_depreciation);
                ps.setString(37, require_redistribution);
                ps.setString(38, SubjectTOVat);
                ps.setString(39, who_to_remind);
                ps.setString(40, email_1);
                ps.setString(41, who_to_remind_2);
                ps.setString(42, email2);
                ps.setString(43, "N");
                ps.setString(44, "0");
                ps.setString(45, section);
                ps.setInt(46, Integer.parseInt(State));
                ps.setInt(47, Integer.parseInt(Driver));
                ps.setString(48, spare_1);
                ps.setString(49, spare_2);
                ps.setString(50, AssetStatus);
                ps.setString(51, user_id);
                ps.setString(52, multiple);
                ps.setString(53, province);
                ps.setString(54, Datepurchased);
                ps.setInt(55, Integer.parseInt(noOfMonths));
                ps.setString(56, Datepurchased);
                ps.setString(57,lpo);   
                ps.setString(58,barCode);
                ps.setString(59,branchCode);
                ps.setString(60,categoryCode);
                ps.setString(61,histId);
                ps.setString(62, "N");
                ps.setString(63, "Y");
                ps.setString(64, "N");
                ps.setString(65,sbuCode);
                ps.setString(66,sectionCode);
                ps.setString(67,deptCode);
                ps.setString(68,systemIp);
                ps.setInt(69, assetCode);
                ps.setString(70,memo);
                ps.setString(71, memoValue);
                ps.setString(72, integrifyId);
                ps.setInt(73, Integer.parseInt(sub_category_id));
                ps.setString(74, subcategoryCode);
                ps.setString(75, spare_3);
                ps.setString(76, spare_4);
                ps.setString(77, spare_5);
                ps.setString(78, spare_6);
                ps.setInt(79, qty);
                ps.setString(80, warehouseCode);  
                ps.setString(81, itemTypeCode);
                ps.setString(82, zoneCode);
                ps.setString(83,regionCode);
                ps.setString(84, projectCode);
                ps.setDouble(85, CostPrice/qty);
                ps.setString(86, itemType);
                ps.addBatch();
 //               done = ps.execute();
                d = ps.executeBatch();
//                System.out.println("<<<<<<=====result: "+result);
                if(d.length > 0){result = true;}
                

                
               PreparedStatement ps1 = con.prepareStatement(create_Archive_Query);
                ps1.setString(1, asset_id_new);
                ps1.setString(2, RegistrationNo);
                ps1.setInt(3, Integer.parseInt(branch_id));
                ps1.setInt(4, Integer.parseInt(dept_id));
                ps1.setInt(5, Integer.parseInt(section_id));
                ps1.setInt(6, Integer.parseInt(category_id));
                ps1.setString(7, Description);
                ps1.setString(8, VendorAC);
                ps1.setString(9, Datepurchased);
                ps1.setString(10, rate);
                ps1.setString(11, AssetMake);
                ps1.setString(12, AssetModel);
                ps1.setString(13, AssetSerialNo);
                ps1.setString(14, AssetEngineNo);
                ps1.setInt(15, Integer.parseInt(SupplierName));
                ps1.setString(16, AssetUser);
                ps1.setInt(17, Integer.parseInt(AssetMaintenance));
                ps1.setInt(18, 0);
                ps1.setInt(19, 0);
                ps1.setDouble(20, CostPrice);
                ps1.setDouble(21, (CostPrice-10.00));
                ps1.setString(22, Datepurchased);
                ps1.setDouble(23, Double.parseDouble(residual_value));
                ps1.setString(24, AuthorizedBy);
                ps1.setString(25, Datepurchased);
                ps1.setString(26, Datepurchased);
                ps1.setString(27, PurchaseReason);
                ps1.setString(28, "0");
                ps1.setString(29, "0");
                ps1.setInt(30, Integer.parseInt(location));
                ps1.setString(31, "0");
                ps1.setDouble(32, Double.parseDouble(VatableCost));
                ps1.setDouble(33, vatamount);
 //               System.out.println("Group Creation5 "+vatamount);
                ps1.setString(34, WhTax);
                ps1.setDouble(35, whtaxamount);
                ps1.setString(36, require_depreciation);
                ps1.setString(37, require_redistribution);
                ps1.setString(38, SubjectTOVat);
                ps1.setString(39, who_to_remind);
                ps1.setString(40, email_1);
                ps1.setString(41, who_to_remind_2);
                ps1.setString(42, email2);
                ps1.setString(43, "N");
                ps1.setString(44, "0");
                ps1.setString(45, section);
                ps1.setInt(46, Integer.parseInt(State));
                ps1.setInt(47, Integer.parseInt(Driver));
                ps1.setString(48, spare_1);
                ps1.setString(49, spare_2);
                ps1.setString(50, AssetStatus);
                ps1.setString(51, user_id);
                ps1.setString(52, multiple);
                ps1.setString(53, province);
                ps1.setString(54, Datepurchased);
                ps1.setInt(55, Integer.parseInt(noOfMonths));
                ps1.setString(56, Datepurchased);
                ps1.setString(57,lpo);
                ps1.setString(58,barCode);
                ps1.setString(59,branchCode);
                ps1.setString(60,categoryCode);
                ps1.setString(61,histId);
                ps1.setString(62, "N");
                ps1.setString(63, "Y");
                ps1.setString(64, "N");
                ps1.setString(65,sbuCode);
                ps1.setString(66,sectionCode);
                ps1.setString(67,deptCode);
                ps1.setString(68,systemIp);
                ps1.setInt(69, assetCode);
                ps1.setInt(70, Integer.parseInt(sub_category_id));
                ps1.setString(71,subcategoryCode);
                ps1.setString(72, spare_3);
                ps1.setString(73, spare_4);
                ps1.setString(74, spare_5);
                ps1.setString(75, spare_6);  
                ps1.setString(76, zoneCode);  
                ps1.setString(77, regionCode);  
                ps1.setString(78, projectCode);
                ps1.setString(86, itemType);
//                System.out.println("=====================================================");
//                System.out.println("Result Of Insertion into Asset Table From group Asset : " + result);
//                System.out.println("=====================================================");
                ps1.addBatch();
 //               done = ps.execute();
                d = ps1.executeBatch();
                if(d.length > 0){result = true;}
//                System.out.println("insGrpToAm_Invoice_No asset_id_new : "+asset_id_new+"   lpo: "+lpo+"    invoiceNo: "+invoiceNo+"   histId: "+histId);
                htmlUtil.insGrpToAm_Invoice_No(asset_id_new,lpo,invoiceNo,"Stock Creation",histId);
            }
            
                String page1 = "STOCK CREATION RAISE ENTRY";
                String flag= "";
          	  	String partPay="";
          	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+branch_id+"'";
          	   	String Branch = approvalRec.getCodeName(qryBranch);
          	   	String subjectT= adGroup.subjectToVat(histId);
          	   	String whT= adGroup.whTax(histId);
          	   	String Name =approvalRec.getCodeName(" SELECT full_name from am_gb_user where user_id='"+user_id+"'");
          	   	String url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + histId + "&pageDirect=Y";
          	   	boolean approval_level_val =checkApprovalStatus("57");
          	//  	boolean status = updateCreatedAssetStatus(asset_id,histId,asset_id_new,assetCode);
          	  	
          	  	if (!approval_level_val)
    	      	  	{
//          	  			System.out.println(">>>>>>>>>>>> Inserting Into RaiseEntry <<<<<<<<<<");
          	  			String [] approvalResult=ad.setApprovalStockDataGroup(Long.parseLong(histId));
          	  			approvalResult[10]="A";
          	  		//	String trans_id = adGroup.setGroupPendingTrans(approvalResult,"57",assetCode);

                                   ad.setPendingTransArchive(approvalResult,"57",Integer.parseInt(approvalResult[0]),assetCode);

                                    String assetRaiseEntry =approvalRec.getCodeName(" SELECT raise_entry from am_gb_company ");
          	  			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                                    approvalRec.insertApprovalx2
    	      	  		(histId, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
                                            whT, url,Integer.parseInt(approvalResult[0]),assetCode);
                                    }
          	  			 
          	  			String qry = upd_am_stock_status_RaiseEntry + histId;
          	  			String qry2 = upd_am_grp_stock_RaiseEntry + histId;
          	  			String qry3 = upd_am_grp_stock_main_RaiseEntry +  histId;
                                    String qry4 = upd_am_stock_status_RaiseEntry_Archive + histId;
          	  			String qry5 = upd_am_grp_stock_RaiseEntry_Archive + histId;
          	  			String qry6 = upd_am_grp_stock_main_RaiseEntry_Archive +  histId;
    	      	  		updateStatusUtil(qry);
    	      	  		updateStatusUtil(qry2);
    	      	  		updateStatusUtil(qry3);
                                    updateStatusUtil(qry4);
    	      	  		updateStatusUtil(qry5);
    	      	  		updateStatusUtil(qry6);
    	      	  	}
          	  	
    	      	if(approval_level_val)
    		      	{
//    		      	 System.out.println("====== Inserting into Approval ======");
    		      	 changeGroupAssetStatus(histId,"PENDING");
    		        String trans_id = adGroup.setGroupPendingTrans(ad.setApprovalStockDataGroup(Long.parseLong(histId)),"57",assetCode);
    		      	String [] approvalResult=ad.setApprovalStockDataGroup(Long.parseLong(histId));
                             ad.setPendingTransArchive(ad.setApprovalStockDataGroup(Long.parseLong(histId)),"57",Integer.parseInt(approvalResult[0]),assetCode);
    		      	 //write a method to change status to pending
    		      	}	
  //          }//Mat

                     } catch (Exception ex) {
                         System.out.println("Error insertStockDetails() of BulkUpdateManager -> " + ex);
                     } 

                     return result;
                 }            
/*
            catch (Exception ex)
            {
                done = false;
                System.out.println("WARN:Error creating Stock->" + ex);
            }
            finally 
            {
                dbConnection.closeConnection(con, ps);
                dbConnection.closeConnection(con, ps1);
            }
            }
            return done;

        }
*/
      
    	/**
         * getDepreciationRate
         *
         * @param category_id String
         * @return String
         */
        private String getDepreciationRate(String category_id) throws Exception {

           

            String rate = "0.0";
            String query = "SELECT DEP_RATE FROM ST_STOCK_CATEGORY " +
                           "WHERE CATEGORY_ID = " + category_id;
//            System.out.println("<<<<<query: "+query);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    rate = rs.getString(1);
                }

            } catch (Exception ex) {
                System.out.println("WARN: Error fetching DepreciationRate for Stock ->" + ex);
            } 

            return rate;
        }

        private boolean checkApprovalStatus(String code)
        {
    		// TODO Auto-generated method stub
    		boolean status = false;
    		String approval_status_qry = "select level from approval_level_setup where code ='"+code+"'";
    		
    	    int level = 0;
    	    try
    	    {
    	    	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(approval_status_qry);
    	    	ResultSet rs = ps.executeQuery();
    	    	if(rs.next())
    	    	{
    	    		level= rs.getInt(1);
    	    	}
    	    	if (level > 0)
    	    	{
    	    		status = true;
    	    	}
    	    }
    	    catch(Exception ex) 
    	    {
    	        System.out.println("WARN: Error checkApprovalStatus ->" + ex);
    	    }
    	     
    	    return status;
    	}
        

    	public void changeGroupAssetStatus(String id,String status) 
    	{
    		// TODO Auto-generated method stub
    		
    	    String query_r ="update am_group_asset set asset_status=? " +
    		" where Group_id = '"+id+"'";

                String query_archive="update am_group_asset_archive set asset_status=? " +
    		" where Group_id = '"+id+"'";
    	    try 
    	    	{
    	    	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query_r);
    	    	ps.setString(1,status);
    	       	int i =ps.executeUpdate();

                    ps = con.prepareStatement(query_archive);
    	    	ps.setString(1,status);
    	       	i =ps.executeUpdate();

    	        changeGroupAssetMainStatus(id,status);
    	        } 
    		catch (Exception ex)
    		    {
    		        System.out.println("GroupAssetToAssetBean: Error Updating am_group_asset " + ex);
    		    } 
    		

    		
    	}


    	public void changeGroupAssetMainStatus(String id, String status2)
    	{
    		// TODO Auto-generated method stub
    		String query_r ="update am_group_asset_main set asset_status=? " +
    		"where Group_id = '"+id+"'";

             String query_archive ="update am_group_asset_main_archive set asset_status=? " +
    		"where Group_id = '"+id+"'";
                    
    		
    	    try 
    		{
    	    	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query_r);
    		ps.setString(1,status2);
    	    int i =ps.executeUpdate();

                ps = con.prepareStatement(query_archive);
    		ps.setString(1,status2);
    	     i =ps.executeUpdate();

    	    } 
    	    catch (Exception ex)
    	    {
    	        System.out.println("GroupAssetToAssetBean: Error Updating am_group_asset_main : " + ex);
    	    } 
    	    
    	}


    	public void updateStatusUtil(String query) 
        {
        	
            try 
            {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
                int i =ps.executeUpdate();
             } 
            catch (Exception ex)
            {
                System.out.println("GroupAssetToAssetBean: updateStatusUtil -" + ex);
            }

    	}


        public ArrayList findAddedAssetProofSelectionforApprovalByBatchId(String tranId, String tranStatus) {


 //           String assetFilter = " and asset_id in (";
            String selectQuery = "";
            if(tranStatus.equals("R")){selectQuery = "select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'NEW' and process_status = 'REJECTED' ";}
            else{selectQuery = "select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'NEW' and process_status is null ";}
            
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
//            System.out.print("selectQuery in : "+selectQuery);
           
            ArrayList listadd = new ArrayList();
            ArrayList listaddrec = new ArrayList(2);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
              //  ps.setInt(1, Integer.parseInt(tranId));
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");     
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
 //                   System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);           
                    listadd.add(aset);
                    aset=null;

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAddedAssetProofSelectionforApprovalByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 

            return listadd;

        }
        

        public boolean insertAssetProofSelectionTemp(ArrayList list, String bacthId) {
            boolean re = false;
            String tableName = "Temp"+bacthId;
            String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
         	ad.updateAssetStatusChange(q1);
            String createquery = "CREATE TABLE "+tableName+"( " +
            "[ASSET_ID] [varchar](50) NULL,	[Description] [varchar](255) NULL,	[BAR_CODE] [varchar](50) NULL,"+
            "[Cost_Price] [decimal](18, 2) NULL,	[Registration_No] [varchar](50) NULL,	[Asset_Make] [nvarchar](50) NULL,"+
            "[Asset_Model] [varchar](255) NULL,	[Asset_Serial_No] [varchar](255) NULL,	[Asset_Engine_No] [varchar](255) NULL,"+
            "[SBU_CODE] [varchar](50) NULL, [SuppliedBy] [varchar](50) NULL, [Vendor_AC] [varchar](50) NULL, [MaintainedBy] [varchar](50) NULL, [Date_purchased] [datetime] NULL,"+
            "[Spare_1] [varchar](150) NULL,	[Spare_2] [varchar](150) NULL,[Spare_3] [varchar](150) NULL,[Spare_4] [varchar](150) NULL,"+
            "[Spare_5] [varchar](150) NULL,	[Spare_6] [varchar](150) NULL,[ASSET_USER] [varchar](255) NULL,"+
            "[COMMENTS] [varchar](255) NULL,	[ASSETSIGHTED] [nchar](1) NULL,	[ASSETFUNCTION] [nchar](1) NULL,"+
            "[State] [int] NULL,[OLD_ASSET_ID] [varchar](50) NULL,[GROUP_NO] [int] NULL,[GROUP_ID] [varchar](50) NULL,"+
            "[Section_id] [int] NULL,[Accum_Dep] [decimal](18, 2) NULL,[Monthly_Dep] [decimal](18, 2) NULL,[NBV] [decimal](18, 2) NULL,"+
            "[Dept_ID] [int] NULL,	[Location] [int] NULL,[LPO] [varchar](50) NULL,[IMPROV_NBV] [decimal](18, 2),[IMPROV_VATABLECOST] [decimal](18, 2),"+
            "[IMPROV_COST] [decimal](18, 2) NULL,	[IMPROV_MONTHLYDEP] [decimal](18, 2) NULL,[IMPROV_ACCUMDEP] [decimal](18, 2) NULL,[PROOF_DATE] [date] NULL,"+
           "[Dep_End_Date] [varchar](50) NULL,	[Effective_Date] [varchar](50) NULL,[TOTAL_NBV] [decimal](18, 2) NULL,[SUB_CATEGORY_ID] [int] NULL,"+
            "[ASSET_CODE] [int] NULL,	[BRANCH_ID] [varchar](50) NULL,	[CATEGORY_ID] [int] NULL,"+
            "[BATCH_ID] [varchar](50) NULL,	[PROCESS_STATUS] [varchar](50) NULL,[SELECT_DATE] [date] NULL,[Asset_Maintenance] [int] NULL,"+
            "[PROOFED_BY] [int] NULL) ON [PRIMARY]";
                
//            System.out.println("<<<<<createquery in insertAssetProofSelectionTemp: "+createquery);
//            System.out.println("<<<<<tableName in insertAssetProofSelectionTemp: "+tableName);
            ad.updateAssetStatusChange(createquery);
            String insertquery = "insert into "+tableName+" ( " +
                    "ASSET_ID,Description,BAR_CODE,Registration_No,Asset_Make,Asset_Model,Asset_Serial_No,"+  //7
                    "Asset_Engine_No,Date_purchased,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,SBU_CODE,ASSET_USER," + //17
                    "COMMENTS,ASSETSIGHTED,ASSETFUNCTION, ASSET_CODE,BRANCH_ID,CATEGORY_ID,BATCH_ID,PROOF_DATE,GROUP_NO,GROUP_ID," +  //27
                    "Cost_Price,Accum_Dep,Monthly_Dep,NBV,IMPROV_COST,IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,TOTAL_NBV,"+  //36
                    "OLD_ASSET_ID,SuppliedBy,Vendor_AC,MaintainedBy,Dep_End_Date,Effective_Date,SUB_CATEGORY_ID,Dept_ID,Section_id,Location,State,LPO,PROOFED_BY )" +  //47
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(insertquery);
 //               System.out.println("<<<<<List Size in insertAssetProofSelectionTemp: "+list.size());

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
//                    System.out.println("<<<<<assetId: "+assetId);
                    String oldassetId = bd.getOld_asset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    if(categoryId=="UNKNOWN"){categoryId="0";}
                    String costprice = bd.getCost_price();
                    String model = bd.getModel();
                    String make = bd.getMake();
                	String sbucode = bd.getSbu_code();
                	String costPrice = bd.getCost_price();
                    String deptId = bd.getDepartment_id();
                    String sectionId = bd.getSection_id();
                    String locationId = bd.getLocation();
                    String stateId = bd.getState();
                    String initiator = bd.getUser_id();
 //                   System.out.println("<<<<<initiator: "+initiator);
                    String subCategoryId = bd.getSub_category_id();
                	String depreciation_start_date = bd.getDepreciation_start_date();
                    String vendor_account = bd.getVendor_account();
                    String suppliedBy  = bd.getSupplied_by();
                    String maintainedBy = bd.getMaintained_by();
                    String lpo = bd.getLpo();
                    String purchaseDate = bd.getDate_of_purchase();	
                    purchaseDate = purchaseDate.substring(0,10);
                	if(depreciation_start_date!=""){
                	String dd = depreciation_start_date.substring(0,2);
                	String mm = depreciation_start_date.substring(3,5);
                	String yyyy = depreciation_start_date.substring(6,10);
                	depreciation_start_date = yyyy+"-"+mm+"-"+dd;
                	}                    
//                	System.out.println("<<<<<depreciation_start_date: "+depreciation_start_date);
                	String depreciation_end_date = bd.getDepreciation_end_date();
                	if(depreciation_end_date!=""){
                	String dd = depreciation_end_date.substring(0,2);
                	String mm = depreciation_end_date.substring(3,5);
                	String yyyy = depreciation_end_date.substring(6,10);
                	depreciation_end_date = yyyy+"-"+mm+"-"+dd;
                	}
//                	System.out.println("<<<<<depreciation_end_date: "+depreciation_end_date);
//                	System.out.println("<<<<<depreciation_start_date: "+depreciation_start_date+" <<<<<depreciation_end_date: "+depreciation_end_date+" <<<<<purchaseDate: "+purchaseDate);
                	String monthlyDep = bd.getMonthlydep() == null ? "" : bd.getMonthlydep();
                	String accumdep = bd.getAccumdep() == null ? "" : bd.getAccumdep();
                	String nbv = bd.getNbv() == null ? "" : bd.getNbv();
                	//   String improveCost = bd.getImproveCost();
                	String improveCost = bd.getImproveCost() == null ? "" : bd.getImproveCost();
                	//   if(improveCost==null){improveCost = "0";}
                	//   String improveMonthlydep = bd.getImproveMonthlydep();
                	String improveMonthlydep = bd.getImproveMonthlydep() == null ? "" : bd.getImproveMonthlydep();
                	String improveAccumdep = bd.getImproveAccumdep() == null ? "" : bd.getImproveAccumdep();
                	String improveNBV =  bd.getImproveNbv() == null ? "" : bd.getImproveNbv();
                	String improvetotalnbv = bd.getImproveTotalNbv() == null ? "" : bd.getImproveTotalNbv();
//                	System.out.println("<<<<<improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"   improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
                	String oldAssetId = bd.getOld_asset_id();
//                	System.out.println("<<<<<purchaseDate: "+purchaseDate);
        /*        	if(purchaseDate!=""){
                	String dd = purchaseDate.substring(0,4);
                	String mm = purchaseDate.substring(5,7);
                	String yyyy = purchaseDate.substring(8,10);
                	purchaseDate = yyyy+"-"+mm+"-"+dd;
                	}*/
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String spare3 = bd.getSpare_3();
                	String spare4 = bd.getSpare_4();
                	String spare5 = bd.getSpare_5();
                	String spare6 = bd.getSpare_6();                	
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
//                	String suppliedBy = bd.getSupplied_by();
                	String vendorAc = bd.getVendor_account();
                	String purchaseReason = bd.getReason();	
                	int groupNo = bd.getQuantity();
//                	System.out.println("<<<The Group No Value :" + groupNo);
                	String groupId = bd.getProjectCode();
//                	if(purchaseDate==null){purchaseDate = "";}
//                	System.out.println("<<<<<purchaseDate: "+purchaseDate+"  groupNo: "+groupNo+"  subCategoryId: "+subCategoryId+"   deptId: "+deptId+"  sectionId: "+sectionId+"   locationId: "+locationId+"  stateId: "+stateId);
                	if((subCategoryId=="") || (subCategoryId.equals("UNKNOWN"))){subCategoryId = "0";}
//                	System.out.println("<<<subCategoryId Value :" + subCategoryId);
                	if((deptId=="") || (deptId.equals("UNKNOWN"))){deptId="0";}
//                	System.out.println("<<<deptId Value :" + deptId);
                	if((sectionId.equals("")) || (sectionId.equals("UNKNOWN"))){sectionId="0";}
//                	System.out.println("<<<sectionId Value :" + sectionId);
                	if((locationId.equals("")) || (locationId.equals("UNKNOWN"))){locationId="0";}
//                	System.out.println("<<<locationId Value :" + locationId);
                	if((stateId.equals("")) || (stateId.equals("UNKNOWN"))){stateId="0";}
//                	System.out.println("<<<stateId Value :" + stateId);
                	stateId = approvalRec.getCodeName("SELECT state_code FROM am_gb_states WHERE state_Id = '"+stateId+"' ");
//                	System.out.println("<<<<<stateId: "+stateId+"  stateId After: "+stateId);
                	locationId = approvalRec.getCodeName("SELECT location_id FROM AM_GB_LOCATION WHERE location_Id = '"+locationId+"' ");
//                	System.out.println("<<<<<locationId: "+locationId+"  locationId After: "+locationId);
//                    branchCode = approvalRec.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = "+initiatorBranchId+" ");
                    subCategoryId = approvalRec.getCodeName("SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_Id = '"+subCategoryId+"' ");
//                    System.out.println("<<<<<subCategoryId: "+subCategoryId+"  subCategoryId After: "+subCategoryId);
                    String subcategory_code = approvalRec.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = '"+subCategoryId+"' ");
//                    System.out.println("<<<<<subcategory_code: "+subcategory_code+"  subCategoryId After: "+subCategoryId);
                    String deptCode = approvalRec.getCodeName("SELECT Dept_code FROM am_ad_department WHERE dept_Id = '"+deptId+"' ");
//                    System.out.println("<<<<<deptCode: "+deptCode+"  deptId After: "+deptId);
                    deptId = approvalRec.getCodeName("SELECT Dept_Id FROM am_ad_department WHERE dept_Id = '"+deptId+"' ");
//                    System.out.println("<<<<<deptId: "+deptId+"  deptId After: "+deptId);
                    String sectionCode = approvalRec.getCodeName("SELECT Section_Code FROM am_ad_section WHERE Section_Id = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionCode: "+sectionCode+"  sectionId After: "+sectionId);
                    sectionId = approvalRec.getCodeName("SELECT Section_Id FROM am_ad_section WHERE Section_Id = '"+sectionId+"' ");
//                    System.out.println("<<<<<sectionId: "+sectionId+"  sectionId After: "+sectionId);
                    if(deptId==""){deptId="0";}
                    if(subCategoryId==""){subCategoryId = "0";}
                    if(sectionId.equals("")){sectionId="0";}
                    if(locationId.equals("")){locationId="0";}
                    if(stateId.equals("")){stateId="0";} 
                    int assetcode = bd.getAssetCode();    

//                    System.out.println("<<<<<<assetId=======: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser+"  costprice"+costprice);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                    ps.setString(1, assetId.toUpperCase());
                    ps.setString(2, description.toUpperCase());
                    ps.setString(3, barcode.toUpperCase());
         //           ps.setDouble(4, Double.parseDouble(costprice));
                    ps.setString(4, registrationNo.toUpperCase());
                    ps.setString(5, make);
                    ps.setString(6, model);
                    ps.setString(7, serialNo);
                    ps.setString(8, engineNo);
                    ps.setString(9, purchaseDate);
                    ps.setString(10, spare1.toUpperCase());
                    ps.setString(11, spare2.toUpperCase());
                    ps.setString(12, spare3.toUpperCase());
                    ps.setString(13, spare4.toUpperCase());
                    ps.setString(14, spare5.toUpperCase());
                    ps.setString(15, spare6.toUpperCase());
//                    System.out.println("<<<<<sbucode: "+sbucode+"  spare1: "+spare1+"  spare2: "+spare2+"  spare3: "+spare3+"  spare4: "+spare4+"  spare5: "+spare5+"  spare6: "+spare6);
                    ps.setString(16, sbucode);
                    ps.setString(17, assetuser.toUpperCase());
                    ps.setString(18, comments);
                    ps.setString(19, sighted);
                    ps.setString(20, functional);
                    ps.setInt(21, assetcode);
                    ps.setString(22, branchCode);
                    ps.setInt(23, Integer.parseInt(categoryId));
                    ps.setString(24, bacthId);
                    ps.setTimestamp(25, dbConnection.getDateTime(new java.util.Date()));
                    ps.setInt(26, groupNo);
                    ps.setString(27, groupId);
//                    System.out.println("<<<<<groupId: "+groupId+"    groupNo: "+groupNo);
                    ps.setDouble(28, Double.parseDouble(costPrice));

					ps.setDouble(29, Double.parseDouble(accumdep));
					ps.setDouble(30, Double.parseDouble(monthlyDep));
					ps.setDouble(31, Double.parseDouble(nbv));
					ps.setDouble(32, Double.parseDouble(improveCost));
					ps.setDouble(33, Double.parseDouble(improveMonthlydep));
					ps.setDouble(34, Double.parseDouble(improveAccumdep));
					ps.setDouble(35, Double.parseDouble(improveNBV));
//					System.out.println("<<<<<accumdep: "+accumdep+"  monthlyDep: "+monthlyDep+"  nbv: "+nbv+"   improveCost: "+improveCost+"  improveMonthlydep: "+improveMonthlydep+"   improveAccumdep: "+improveAccumdep+"  improveNBV: "+improveNBV+"  improvetotalnbv: "+improvetotalnbv);
					ps.setDouble(36, Double.parseDouble(improveNBV)+Double.parseDouble(nbv));
					ps.setString(37, oldAssetId);
					ps.setString(38, suppliedBy);
					ps.setString(39, vendor_account);
					ps.setString(40, maintainedBy);
					ps.setString(41, depreciation_end_date);
					ps.setString(42, depreciation_start_date);
//					System.out.println("<<<<<<subCategoryId: "+subCategoryId+"  deptId: "+deptId+"  sectionId: "+sectionId+"  locationId: "+locationId+"  stateId: "+stateId+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
					ps.setInt(43, Integer.parseInt(subCategoryId));
					ps.setInt(44, Integer.parseInt(deptId));
					ps.setInt(45, Integer.parseInt(sectionId));
					ps.setInt(46, Integer.parseInt(locationId));
//					System.out.println("<<<<<locationId: "+locationId);
					ps.setInt(47, Integer.parseInt(stateId));
					ps.setString(48, lpo);
					ps.setInt(49, Integer.parseInt(initiator));
					
//					System.out.println("<<<<<vendor_account: "+vendor_account);
                    ps.addBatch();
                    d = ps.executeBatch();
                }
//                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertAssetProofSelectionTemp() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }
        
        public boolean AssetProofSelectionRejectionToInitiator(String bacthId, String rejectReason,int tranIdApproval,String userId) {
        	boolean done = false;
            
        	try 
            {
        		Connection con = dbConnection.getConnection("legendPlus");

        	Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
//	    	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+bacthId+"' and process_status IS NULL ");
//	    	System.out.println("#���� tranIdApproval: "+tranIdApproval);
	        String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rejectReason + "',DATE_APPROVED = '"+approveddate+"',PROCESSING = 'Processing Rejection' where transaction_id=" + tranIdApproval;
//	        System.out.println("#���� q: "+q);
	        arb.updateAssetStatusChange(q);
	        String qw = "update am_Asset_Proof set PROCESS_STATUS=NULL where BATCH_ID= '"+bacthId+"' ";
//	        System.out.println("#���� qw: "+qw);
	        arb.updateAssetStatusChange(qw);
	  		String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where asset_id='"+bacthId+"'");
			String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
			String subject ="Branch Asset Proof Rejected";
			String msgText1 = "Your Branch Asset Proof with Batch Id: "+ bacthId +" has been rejected. Reasons: "+ rejectReason +" ";
//			System.out.println("#$$$$$$$$$$$ "+createdby);
//			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
			mail.sendMail(createdby,subject,msgText1);
            }
            catch (Exception ex)
            {
                done = false;
                System.out.println("WARN:Error creating asset->" + ex);
            }
           
            return done;
        }
        public boolean updateAssetProofSelectionInsert(ArrayList list, String bacthId, String tranStatus) {
            boolean re = false;
            String query = "";
//            System.out.println("<<<<<<<bacthId in updateAssetProofSelectionInsert: "+bacthId);  
            if(tranStatus.equalsIgnoreCase("SAVE")){
            query = "update am_Asset_Proof set Description = ?,BAR_CODE = ?,Registration_No = ?,Asset_Make = ?,Asset_Model = ?,Asset_Serial_No = ?,"+
                    "Asset_Engine_No = ?,Spare_1 = ?,Spare_2 = ?,Spare_3 = ?,Spare_4 = ?,Spare_5 = ?,Spare_6 = ?,SBU_CODE = ?,SUB_CATEGORY_ID=?,Dept_ID=?,"+
                    "Section_id=?,Location=?,State=?,LPO=?,ASSET_USER = ?," +
                    "COMMENTS = ?,ASSETSIGHTED = ?,ASSETFUNCTION = ?,Supplier_Name = ?,Vendor_AC = ?,Asset_Maintenance = ?,EXPORTED ='NO' where batch_id = ? and ASSET_ID = ?  ";
            }else{
            query = "update am_Asset_Proof set Description = ?,BAR_CODE = ?,Registration_No = ?,Asset_Make = ?,Asset_Model = ?,Asset_Serial_No = ?,"+
                    "Asset_Engine_No = ?,Spare_1 = ?,Spare_2 = ?,Spare_3 = ?,Spare_4 = ?,Spare_5 = ?,Spare_6 = ?,SBU_CODE = ?,SUB_CATEGORY_ID=?,Dept_ID=?,"+
                    "Section_id=?,Location=?,State=?,LPO=?,ASSET_USER = ?," +
                    "COMMENTS = ?,ASSETSIGHTED = ?,ASSETFUNCTION = ?,Supplier_Name = ?,Vendor_AC = ?,Asset_Maintenance = ?,process_status ='WFA',EXPORTED ='NO' where batch_id = ? and ASSET_ID = ?  ";
            }
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String description = bd.getDescription();
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String sighted = bd.getAssetsighted();
                    String functional = bd.getAssetfunction();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String categoryId = bd.getCategory_id();
                    String subCatId = bd.getSub_category_id();
                    String deptId = bd.getDepartment_id();
                    String sectionId = bd.getSection_id(); 
                    String location = bd.getLocation();
                    String state = bd.getState();
                    String costprice = bd.getCost_price();
                    String model = bd.getModel();
                    String make = bd.getMake();
                    String lpo = bd.getLpo();
                	String sbucode = bd.getSbu_code();
                	String spare1 = bd.getSpare_1();
                	String spare2 = bd.getSpare_2();
                	String spare3 = bd.getSpare_3();
                	String spare4 = bd.getSpare_4();
                	String spare5 = bd.getSpare_5();
                	String spare6 = bd.getSpare_6();
                	String registrationNo = bd.getRegistration_no();
                	String serialNo = bd.getSerial_number();
                	String engineNo = bd.getEngine_number();
                	String suppliedBy = bd.getSupplied_by();
                	String vendorAc = bd.getVendor_account();
                	String maintianedBy = bd.getMaintained_by();
                	String authorized = bd.getAuthorized_by();
                	String purchaseReason = bd.getReason();	
                	String purchaseDate = bd.getDate_of_purchase();	
                	
                    int assetcode = bd.getAssetCode();      
//                    System.out.println("<<<<<<assetId: "+assetId+"  description: "+description+"  barcode: "+barcode+"  comments: "+comments+"  sighted: "+sighted+"  functional: "+functional+"  branchCode: "+branchCode+"  categoryId: "+categoryId+"  assetcode: "+assetcode+"  assetuser: "+assetuser);
//                    System.out.println("<<<<<<model: "+model+"  make: "+make+"  sbucode: "+sbucode+"  spare1: "+spare1+"  spare2: "+spare2+"  engineNo: "+engineNo+"  branchCode: "+branchCode+"  registrationNo: "+registrationNo+"  assetcode: "+assetcode+"  serialNo: "+serialNo);
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }

                	if((subCatId=="") || (subCatId.equals("UNKNOWN"))){subCatId = "0";}
                	if((deptId=="") || (deptId.equals("UNKNOWN"))){deptId="0";}
//                	System.out.println("<<<deptId Value :" + deptId);
                	if((sectionId.equals("")) || (sectionId.equals("UNKNOWN"))){sectionId="0";}
                	if((location.equals("")) || (location.equals("UNKNOWN"))){location="0";}
                	if((state.equals("")) || (state.equals("UNKNOWN"))){state="0";}
//                	state = approvalRec.getCodeName("SELECT state_code FROM am_gb_states WHERE state_name = '"+state+"' ");
 //               	location = approvalRec.getCodeName("SELECT location_id FROM AM_GB_LOCATION WHERE location = '"+location+"' ");
//                    branchCode = approvalRec.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = "+initiatorBranchId+" ");
//                	subCatId = approvalRec.getCodeName("SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_name = '"+subCatId+"' ");
                    String subcategory_code = approvalRec.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = '"+subCatId+"' ");
                    String deptCode = approvalRec.getCodeName("SELECT Dept_code FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
 //                   System.out.println("<<<<<deptCode: "+deptCode+"  deptId After: "+deptId);
 //                   deptId = approvalRec.getCodeName("SELECT Dept_Id FROM am_ad_department WHERE dept_name = '"+deptId+"' ");
//                    System.out.println("<<<<<deptId: "+deptId+"  deptId After: "+deptId);
                    String sectionCode = approvalRec.getCodeName("SELECT Section_Code FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
 //                   System.out.println("<<<<<sectionCode: "+sectionCode+"  sectionId After: "+sectionId);
 //                   sectionId = approvalRec.getCodeName("SELECT Section_Id FROM am_ad_section WHERE Section_Name = '"+sectionId+"' ");
 //                   System.out.println("<<<<<sectionId: "+sectionId+"  sectionId After: "+sectionId);
                    if(deptId==""){deptId="0";}
                    if(subCatId==""){subCatId = "0";}
                    if(sectionId.equals("")){sectionId="0";}
                    if(location.equals("")){location="0";}
                    if(state.equals("")){state="0";}                    
 //                   System.out.println("<<<<<subCatId: "+subCatId+"  sectionId: "+sectionId+"   deptId: "+deptId+"  location: "+location+"   state: "+state);
                    ps.setString(1, description.toUpperCase());
                    ps.setString(2, barcode.toUpperCase());
                    ps.setString(3, registrationNo.toUpperCase());
                    ps.setString(4, make);
                    ps.setString(5, model);
                    ps.setString(6, serialNo);
                    ps.setString(7, engineNo);
                    ps.setString(8, spare1.toUpperCase());
                    ps.setString(9, spare2.toUpperCase());
                    ps.setString(10, spare3.toUpperCase());
                    ps.setString(11, spare4.toUpperCase());
                    ps.setString(12, spare5.toUpperCase());
                    ps.setString(13, spare6.toUpperCase());
                    ps.setString(14, sbucode);
                    ps.setString(15, subCatId);
                    ps.setString(16, deptId);
                    ps.setString(17, sectionId);
                    ps.setString(18, location);
                    ps.setString(19, state);
                    ps.setString(20, lpo);
                    ps.setString(21, assetuser.toUpperCase());
                    ps.setString(22, comments);
                    ps.setString(23, sighted);
                    ps.setString(24, functional);
                    ps.setString(25, suppliedBy);
                    ps.setString(26, vendorAc);
                    ps.setString(27, maintianedBy);
                    ps.setString(28, bacthId);
                    ps.setString(29, assetId);
                    
 //                   System.out.println("<<<<<<<assetId: "+assetId);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error updateAssetProofSelectionInsert() of BulkUpdateManager -> " + ex);
            } 
            
            return (d.length > 0);
        }


        public boolean updateProofApprovalCommentsOld(ArrayList list) {

            ArrayList newList = null;
            Asset asset = null;
           
//            htmlUtil = new HtmlUtility();
             
            int[] d =null;
            String query= "UPDATE am_Asset_Proof SET COMMENTS=? WHERE ASSET_ID=?" ;

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                 for (int i = 0; i < list.size(); ++i) {
                	 asset = (Asset)list.get(i);
                    String assetId = asset.getId();
                    String comments = asset.getComments();
//                    System.out.println("<<<<<<assetId: "+assetId+"    comments: "+comments);
      //              asset=null;

                 ps.setString(1,comments);
                 ps.setString(2,assetId);

                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: updateProofApprovalComments()>>>>>" + ex);
            }
                return (d.length > 0);
        }


        public ArrayList findAddedAssetProofSelectionforApprovalByBatchId(String tranId) {


 //           String assetFilter = " and asset_id in (";
            String selectQuery = 
            		"select * from am_Asset_Proof_Selection where batch_id='"+tranId+"' and REC_TYPE = 'NEW' and process_status is null ";
            
            	//	"select * from am_gb_workbookupdate where batch_id=? and process_status = 'APPROVED' ";
 //           System.out.print("selectQuery in : "+selectQuery);
           
            ArrayList listadd = new ArrayList();
            ArrayList listaddrec = new ArrayList(2);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
              //  ps.setInt(1, Integer.parseInt(tranId));
               ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String oldAssetId = rs.getString("OLD_ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    String barCode = rs.getString("BAR_CODE");
                    String assetuser = rs.getString("ASSET_USER");
                    String comments = rs.getString("COMMENTS");
                    String sighted = rs.getString("ASSETSIGHTED");
                    String functional = rs.getString("ASSETFUNCTION");
                    int assetcode = rs.getInt("ASSET_CODE");
                    String branchCode = rs.getString("BRANCH_ID");
                    String categoryId = rs.getString("CATEGORY_ID");
                    String subcategoryId = rs.getString("SUB_CATEGORY_ID");                    
                    String sbuCode = rs.getString("SBU_CODE");
                    String model = rs.getString("Asset_Model");
                    String make = rs.getString("Asset_Make");
                    String registrationNo = rs.getString("Registration_No");
                    double costPrice = rs.getDouble("Cost_Price");
                    double monthlyDepreciation = rs.getDouble("Monthly_Dep");
                    double accumulatedDepreciation = rs.getDouble("Accum_Dep");
                    double nbv = rs.getDouble("NBV");
                    double improvcost = rs.getDouble("IMPROV_COST");
                    double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
                    double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
                    double improvnbv = rs.getDouble("IMPROV_NBV");
                    double improvTotalNbv = rs.getDouble("TOTAL_NBV");                    
                    String serialNo = rs.getString("Asset_Serial_No");
                    String engineNo = rs.getString("Asset_Engine_No");
                    String purchaseDate = rs.getString("Date_purchased");
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");     
                    String branchId = rs.getString("Branch_ID");
                    String departmentId = rs.getString("Dept_ID");
                    int CategoryId = rs.getInt("CATEGORY_ID");
                    String sectionId = rs.getString("Section_id");
                    int Location = rs.getInt("Location");
                    int state = rs.getInt("State");     
                    String date_of_purchase = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                    String depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                    String depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));                    
                    aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setBarCode(barCode);
                    aset.setSbuCode(sbuCode);
                    aset.setAssetUser(assetuser);
                    aset.setComments(comments);
                    aset.setAssetsighted(sighted);
                    aset.setAssetfunction(functional);
                    aset.setAssetCode(assetcode);
                    aset.setBranchCode(branchCode);
                    aset.setOLD_ASSET_ID(oldAssetId);
                    aset.setBranchId(branchId);
                    aset.setDepartmentId(departmentId);
                    aset.setCategoryId(CategoryId);
                    aset.setSubcategoryId(subcategoryId);
                    aset.setSectionId(sectionId);
                    aset.setLocation(Location);
                    aset.setState(state);
//                    System.out.println("<<<<<<sectionId: "+sectionId+"   Location: "+Location+"    state: "+state);
                    aset.setCost(costPrice);
                    aset.setMonthlyDepreciation(monthlyDepreciation);
                    aset.setAccumulatedDepreciation(accumulatedDepreciation);
                    aset.setNbv(nbv);
                    aset.setImprovcost(improvcost);
                    aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
                    aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
                    aset.setImprovnbv(improvnbv);
                    aset.setImprovTotalNbv(improvTotalNbv);
                    aset.setAssetmodel(model);
                    aset.setAssetMake(make);
                    aset.setEngineNo(engineNo);
                    aset.setRegistrationNo(registrationNo);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setSpare3(spare3);
                    aset.setSpare4(spare4);
                    aset.setSpare5(spare5);
                    aset.setSpare6(spare6);                    
                    aset.setSerialNo(serialNo);
                    aset.setDatePurchased(purchaseDate);
                    aset.setCategoryId(Integer.parseInt(categoryId));
                    aset.setDepreciationEndDate(depreciation_end_date);
                    aset.setEffectivedate2(depreciation_start_date);           
                    listadd.add(aset);
                    aset=null;

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findAddedAssetProofSelectionforApprovalByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 

            return listadd;

        }
        


        public boolean updateProofApprovalComments(ArrayList list) {

            ArrayList newList = null;
            Asset asset = null;
            
            htmlUtil = new HtmlUtility();
             
            int[] d =null;
            String query= "UPDATE am_Asset_Proof SET COMMENTS=? WHERE ASSET_ID=?" ;
            
            try {

            	 Connection con = dbConnection.getConnection("legendPlus");
                 PreparedStatement ps = con.prepareStatement(query);
                 
                 for (int i = 0; i < list.size(); ++i) {
            asset = (Asset)list.get(i);
            String assetId = asset.getId();
            String comments = asset.getComments();                    
            asset=null;
           

                     ps.setString(1,comments);
                     ps.setString(2,assetId);
                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: updateBulkAsset()>>>>>" + ex);
            }
                return (d.length > 0);
        }

        public boolean AssetProofBranchApproval(ArrayList list, String bacthId,String tranStatus,String transactionId,int approvalCount,int tranLevel,String tranId) {
            boolean result = false;
            String query = "";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
                String branchID = approvalRec.getCodeName(" select DISTINCT BRANCH_ID from am_Asset_Proof  where BATCH_ID = '"+bacthId+"'");
                String branchName = approvalRec.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);
            	ApprovalRemark approvalRemark = new ApprovalRemark();
                approvalRemark.setApprovalLevel(approvalCount);
                approvalRemark.setRemark("");
                approvalRemark.setStatus("Approved");
                approvalRemark.setTranType("Branch Asset Proof");

                if (approvalCount == tranLevel - 1) {
             //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+bacthId+"' and process_status ='P' ");
                    String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranIdApproval;
                    arb.updateAssetStatusChange(q);
                    String qw = "update am_Asset_Proof set PROCESS_STATUS='APPROVED', APPROVED_DATE = '"+approveddate+"' where BATCH_ID= '"+bacthId+"' and PROCESS_STATUS ='WFA' ";
                    arb.updateAssetStatusChange(qw);

              //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
//                    BulkUpdateManager bum = new BulkUpdateManager();
//                     bum.updateBulkAsset(newAssetList);

                    // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                    //    arb.updateAssetStatusChange(q3);

                    approverManager.createApprovalRemark(approvalRemark);

                    // asset_manager.updateAssetDepreciation(asset_id);
           		 String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
        		 String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		 String subject ="Branch Asset Proof Approved";
        			String msgText1 = "Your Branch Asset Proof with Batch Id: "+ bacthId +" has been approved.";
//        			System.out.println("#$$$$$$$$$$$ "+createdby);
//        			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
        			mail.sendMail(createdby,subject,msgText1);
        			msgText1 = "Branch Asset Proof with Batch Id: "+ bacthId +" has been sent to you from Branch '"+branchName+"' for Processing.";
        			String tomail = approvalRec.getCodeName("select mail_address from am_mail_statement where mail_code='44'");
        			String otherparam = "AssetProofByBranchApproval&tranId="+tranId+"&transaction_level=1&approval_level_count=0";
        			 subject ="Asset Proof From Branch";
//        			System.out.println("tomail #$$$$$$$$$$$ "+tomail);
        			otherparam = "bulkAssetProofSelection&tranId="+tranId+"&transaction_level=1&approval_level_count=0";
//        			mail.sendMailwithAddress(tomail, subject, msgText1,otherparam);	
        			mail.sendMailwithMultipeAddress(tomail, subject, msgText1,otherparam);	
                } else {
             //       arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                    approverManager.createApprovalRemark(approvalRemark);
                }
                
            } catch (Exception ex) {
                System.out.println("Error AssetProofBranchApproval() of BulkUpdateManager -> " + ex);
            } 
            return result;
        }

  

        public boolean updateAssetProofBranchApprovalComments(ArrayList list, String bacthId,String tranStatus,String rr,String transactionId,int approvalCount,String tranId) {
            boolean re = false;
            String query = "";
//            System.out.println("<<<<<<<bacthId: "+bacthId+"    tranStatus: "+tranStatus+"     tranId: "+tranId);  
            query = "update am_Asset_Proof set COMMENTS = ? where batch_id = ? and ASSET_ID = ? ";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
   //             System.out.println("<<<<<<<LIST SIZE: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String comments = bd.getComments();
 //                   System.out.println("<<<<<<<assetId: "+assetId+"   comments: "+comments);
                    ps.setString(1, comments);
                    ps.setString(2, bacthId);
                    ps.setString(3, assetId);
                    
//                    System.out.println("<<<<<<<assetId: "+assetId);
                    //ps.addBatch();
                    ps.execute();
                }
                d = ps.executeBatch();
            	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+bacthId+"' and process_status ='P' ");
                // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranIdApproval;
                arb.updateAssetStatusChange(q);
                String qw = "update am_Asset_Proof set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+bacthId+"' and PROCESS_STATUS = 'WFA' ";
                arb.updateAssetStatusChange(qw);
                ApprovalRemark approvalRemark = new ApprovalRemark();
                String alertmessage = "Transaction Rejected";
                approvalRemark.setApprovalLevel(approvalCount += 1);
                approvalRemark.setRemark(rr);
                approvalRemark.setStatus("Rejected");
                approvalRemark.setTranType("Branch Asset Proof");
                approverManager.createApprovalRemark(approvalRemark);
          		String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
        		String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		String subject ="Branch Asset Proof Rejected";
    			String msgText1 = "Your Branch Asset Proof with Batch Id: "+ bacthId +" has been rejected. Reasons: "+ rr +" ";
//    			System.out.println("#$$$$$$$$$$$ "+createdby);
//    			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
    			mail.sendMail(createdby,subject,msgText1);
                
                
            } catch (Exception ex) {
                System.out.println("Error updateAssetProofBranchApprovalComments() of BulkUpdateManager -> " + ex);
            } 
            System.out.println("Record Lenght: "+d.length);
            return (d.length > 0);
        }


        public boolean updateAssetProofSelectionComments(ArrayList list, String bacthId,String tranStatus,String rr,String transactionId,int approvalCount) {
            boolean re = false;
            String query = "";
//            System.out.println("<<<<<<<bacthId: "+bacthId+"    tranStatus: "+tranStatus);  
            query = "update am_Asset_Proof_Selection set COMMENTS = ? where batch_id = ? and ASSET_ID = ? ";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
   //             System.out.println("<<<<<<<LIST SIZE: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
                    String comments = bd.getComments();
 //                   System.out.println("<<<<<<<assetId: "+assetId+"   comments: "+comments);
                    ps.setString(1, comments);
                    ps.setString(2, bacthId);
                    ps.setString(3, assetId);
                    
//                    System.out.println("<<<<<<<assetId: "+assetId);
                    //ps.addBatch();
                    ps.execute();
                }
                d = ps.executeBatch();
 //               System.out.println("Executed Successfully ");
                String branchID = approvalRec.getCodeName(" select DISTINCT BRANCH_ID from am_Asset_Proof_Selection  where BATCH_ID = '"+bacthId+"'");
                String branchName = approvalRec.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);
                ApprovalRemark approvalRemark = new ApprovalRemark();
                String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + transactionId;
                arb.updateAssetStatusChange(q);
                String qw = "update am_Asset_Proof set PROCESS_STATUS='R' where BATCH_ID= '"+bacthId+"' ";
                arb.updateAssetStatusChange(qw);       
                String qm = "update am_Asset_Proof_Selection set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+bacthId+"' ";
                arb.updateAssetStatusChange(qm);                             
                String alertmessage = "Transaction Rejected";
                approvalRemark.setApprovalLevel(approvalCount += 1);
                approvalRemark.setRemark(rr);
                approvalRemark.setStatus("Rejected");
                approvalRemark.setTranType("Asset Proof Processing Approval");
                approverManager.createApprovalRemark(approvalRemark);
          		String createdUserId = approvalRec.getCodeName("select USER_ID from am_asset_approval where ASSET_ID='"+bacthId+"'");
//          		System.out.println("createdUserId#$$$$$$$$$$$: "+createdUserId);
        		String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		String subject ="Branch Asset Proof Processing Rejected";
    			String msgText1 = "Branch Asset Proof Processing with Batch Id: "+ bacthId +" and Branch Name '"+branchName+"' has been rejected. Reasons: "+ rr +" ";
//    			System.out.println("#$$$$$$$$$$$ "+createdby);
//    			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
    //			 System.out.println("here prooftranId>>>>>>>>>>>>>>: "+prooftranId+"   tranId: "+tranId+"   full_status: "+full_status);
    			mail.sendMail(createdby,subject,msgText1);
                
                
            } catch (Exception ex) {
                System.out.println("Error updateAssetProofComments() of BulkUpdateManager -> " + ex);
            } 
//            System.out.println("Record Lenght: "+d.length);
            return (d.length > 0);
        }


        public boolean AssetProofSelectionApproval(ArrayList list, String bacthId,String tranStatus,String transactionId,int approvalCount,int tranLevel) {
            boolean result = false;
            String query = "";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
                String branchID = approvalRec.getCodeName(" select DISTINCT BRANCH_ID from am_Asset_Proof_Selection  where BATCH_ID = '"+bacthId+"'");
                String branchName = approvalRec.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);                
                ApprovalRemark approvalRemark = new ApprovalRemark();
                approvalRemark.setApprovalLevel(approvalCount);
                approvalRemark.setRemark("");
                approvalRemark.setStatus("Approved");
                String alertmessage = "Transaction Approved";
                approvalRemark.setTranType("Asset Proof Processing Approval");
//                System.out.println("here approvalCount>>>>>>>>>>>>>> "+approvalCount+"    tranLevel: "+tranLevel);
                if (approvalCount == tranLevel - 1) {
//                    System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                    String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + transactionId;
                    arb.updateAssetStatusChange(q);
                    String qw = "update am_Asset_Proof_Selection set SELECT_DATE = '"+approveddate+"',PROCESS_STATUS = 'APPROVED' where BATCH_ID= '"+bacthId+"'";
                    arb.updateAssetStatusChange(qw);                    
                    arb.bulkAssetUpdateFromProof(bacthId); 
                    approverManager.createApprovalRemark(approvalRemark);
                    result = grpAsset.UpdateAssetProofApproval(bacthId);
                    // asset_manager.updateAssetDepreciation(asset_id);
           		 String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where transaction_id="+transactionId+" ");
        		 String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		 String subject ="Branch Asset Proof Processing Approved";
        			String msgText1 = "Your Branch Asset Proof Processing with Batch Id: "+ bacthId +" and Branch Name '"+branchName+"' has been approved.";
//        			System.out.println("#$$$$$$$$$$$ "+createdby);
//        			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
        			mail.sendMail(createdby,subject,msgText1);
                }
                
            } catch (Exception ex) {
                System.out.println("Error AssetProofSelectionApproval() of BulkUpdateManager -> " + ex);
            } 
            return result;
        }


        public boolean insertBulkSBUUpdate(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into am_gb_bulkSBUupdate (ASSET_ID,OLD_SBU_CODE,NEW_SBU_CODE,Description,BATCH_ID)" +
                    " values(?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String asset_id1 = bd.getAsset_id();
                    String oldsbuCode = bd.getSbu_code();
                    String newsbuCode = bd.getNewsbu_code();
                    String description = bd.getDescription();
                    String branchId = bd.getBranch_id();

                    if (description == null || description.equals("")) {
                        description = "";
                    }
                    if (oldsbuCode == null || oldsbuCode.equals("")) {
                    	oldsbuCode = "0";
                    }
                    if (newsbuCode == null || newsbuCode.equals("")) {
                    	newsbuCode = "0";
                    }                                  
                    if (asset_id1 == null || asset_id1.equals("")) {
                        asset_id1 = "0";
                    }
                    ps.setString(1, asset_id1);
                    ps.setString(2, oldsbuCode);
                    ps.setString(3, newsbuCode);
                    ps.setString(4, description);
                    ps.setInt(5, bacthId);
                  
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");

            } catch (Exception ex) {
                System.out.println("Error insertBulkSBUUpdate() of BulkUpdateManager -> " + ex);
            }

            return (d.length > 0);
        }


        public ArrayList[] findAssetSbuByBatchId(String tranId) {


            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkSBUupdate where batch_id=? ";

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String branchId = rs.getString("BRANCH_ID");
                    String description = rs.getString("DESCRIPTION");
                    String oldsbuCode = rs.getString("OLD_SBU_CODE");
                    String newsbuCode = rs.getString("NEW_SBU_CODE");
                    
                     aset = new Asset();
                    aset.setId(id);
                    aset.setBranchId(branchId);
                    aset.setDescription(description);
                    aset.setSbuCode(oldsbuCode);
                    aset.setNewsbuCode(newsbuCode);
                    
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findAssetSbuByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public boolean BulkAssetSbuUpdateApproval(ArrayList list, String bacthId,String tranStatus,String transactionId,int approvalCount,int tranLevel,String tranId) {
            boolean result = false;
            String query = "";
            query = "update AM_ASSET set SBU_CODE = ? where ASSET_ID = ? ";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
           
            
            //ResultSet rs = null;
            String branchId = "";
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {  
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String assetId = bd.getAsset_id();
                    String sbuCode = bd.getNewsbu_code();
                    branchId = bd.getBranch_id();
//                    System.out.println("<<<<<<<assetId: "+assetId+"   sbuCode: "+sbuCode);
                    ps.setString(1, sbuCode);
                    ps.setString(2, assetId);
                    ps.execute();
                }
                d = ps.executeBatch();
                if(d.length > 0){result = true;}
//                System.out.println("<<<<<<<Done>>>>>>> ");
           //     String branchID = approvalRec.getCodeName(" select DISTINCT BRANCH_ID from am_Asset_Proof  where BATCH_ID = '"+bacthId+"'");
            //    String branchName = approvalRec.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchId);
            	ApprovalRemark approvalRemark = new ApprovalRemark();

                approvalRemark.setApprovalLevel(approvalCount);
                approvalRemark.setRemark("");
                approvalRemark.setStatus("Approved");
                approvalRemark.setTranType("Bulk Asset SBU Update");
                
                if (approvalCount == tranLevel - 1) {
//                    System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+bacthId+"' and process_status ='P' ");
                    String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Updated',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                    arb.updateAssetStatusChange(q);
                    String qw = "update am_gb_bulkSBUupdate set PROCESS_STATUS='APPROVED', APPROVED_DATE = '"+approveddate+"' where BATCH_ID= '"+bacthId+"' and PROCESS_STATUS IS NULL ";
                    arb.updateAssetStatusChange(qw);
                    approverManager.createApprovalRemark(approvalRemark);
           		 String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
        		 String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		 String subject ="Bulk Asset SBU Update Approved";
        			String msgText1 = "Your Bulk Asset SBU Update with Batch Id: "+ bacthId +" has been approved.";
//        			System.out.println("#$$$$$$$$$$$ "+createdby);
//        			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
        			mail.sendMail(createdby,subject,msgText1);
                } else {
             //       arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                    approverManager.createApprovalRemark(approvalRemark);
                }
                
            } catch (Exception ex) {
                System.out.println("Error BulkAssetSbuUpdateApproval() of BulkUpdateManager -> " + ex);
            } 
            return result;
        }


        public boolean BulkAssetSbuUpdateRejection(ArrayList list, String bacthId,String tranStatus,String rr,String transactionId,int approvalCount,String tranId) {
            boolean re = false;
            String query = "";
 //           System.out.println("<<<<<<<bacthId: "+bacthId+"    tranStatus: "+tranStatus+"     tranId: "+tranId);  
//            query = "update am_Asset_Proof set COMMENTS = ? where batch_id = ? and ASSET_ID = ? ";
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 

            	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+bacthId+"' and process_status ='P' ");
                String q = "update am_asset_approval set process_status='R', asset_status='Bulk Asset SBU Updated ', reject_reason='" + rr + "',APPROVED_DATE = '"+approveddate+"' where transaction_id=" + tranId;
                arb.updateAssetStatusChange(q);
                
                ApprovalRemark approvalRemark = new ApprovalRemark();
                String alertmessage = "Transaction Rejected";
                approvalRemark.setApprovalLevel(approvalCount += 1);
                approvalRemark.setRemark(rr);
                approvalRemark.setStatus("Rejected");
                approvalRemark.setTranType("Bulk Asset SBU Update");
          		String createdUserId = approvalRec.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
        		String createdby = approvalRec.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
        		String subject ="Bulk Asset SBU Update Rejected";
    			String msgText1 = "Your Bulk Asset SBU Update with Batch Id: "+ bacthId +" has been rejected. Reasons: "+ rr +" ";
//    			System.out.println("#$$$$$$$$$$$ "+createdby);
//    			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
    			mail.sendMail(createdby,subject,msgText1);
                
                
            } catch (Exception ex) {
                System.out.println("Error BulkAssetSbuUpdateRejection() of BulkUpdateManager -> " + ex);
            }
            System.out.println("Record Lenght: "+d.length);
            return (d.length > 0);
        }

        public boolean insertAssetFleetRenewalInsert(ArrayList list, String bacthId, String processExport) {
            boolean re = false;
            String tableName = "Temp"+bacthId;
            String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
         	ad.updateAssetStatusChange(q1);
            String createquery = "CREATE TABLE "+tableName+"( " +
            "[ASSET_ID] [varchar](50) NULL,	[Description] [varchar](255) NULL,	[BAR_CODE] [varchar](50) NULL,"+
            "[Registration_No] [varchar](50) NULL,	[Asset_Make] [nvarchar](50) NULL, [SBU_CODE] [varchar](50) NULL,"+
            "[ASSET_USER] [varchar](255) NULL,[COMMENTS] [varchar](255),[INSURANCE_CODE] [varchar](50) NULL) ON [PRIMARY]";
             
            ad.updateAssetStatusChange(createquery);
            String insertquery = "insert into "+tableName+" ( " +
                    "ASSET_ID,Description,BAR_CODE,Registration_No,Asset_Make,"+  //7
                    "SBU_CODE,ASSET_USER," + //17
                    "COMMENTS,INSURANCE_CODE)" +  //47
                    " values(?,?,?,?,?,?,?,?,?)";
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
               
//                ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String assetId = bd.getAsset_id();
 //                   System.out.println("<<<<<assetId: "+assetId);
                    String description = bd.getDescription();
//                    System.out.println("<<<<<description: "+description);
                    String barcode = bd.getBar_code();
                    String comments = bd.getComments();
                    String assetuser = bd.getNewuser();
                    String branchCode = bd.getBranch_Code();
                    String make = bd.getMake();               
                    String userId = bd.getUser_id();
//                    System.out.println("<<<<<make: "+make);
                	String sbucode = bd.getSbu_code();
                	String vendorCode = bd.getVendorCode();
                	String registrationNo = bd.getRegistration_no();
                    if (assetId == null || assetId.equals("")) {
                    	assetId = "0";
                    }
                    if (barcode == null || barcode.equals("")) {
                        barcode = "0";
                    }
                    Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps1 = con.prepareStatement(insertquery);
                    ps1.setString(1, assetId.toUpperCase());
                    ps1.setString(2, description.toUpperCase());
                    ps1.setString(3, barcode.toUpperCase());
                    ps1.setString(4, registrationNo.toUpperCase());
                    ps1.setString(5, make);
                    ps1.setString(6, sbucode);
                    ps1.setString(7, assetuser.toUpperCase());
                    ps1.setString(8, comments);
                    ps1.setString(9, vendorCode);
//                    ps1.setTimestamp(10, dbConnection.getDateTime(new java.util.Date()));
//					ps1.setInt(11, Integer.parseInt(userId));
                    ps1.addBatch();
                    d = ps1.executeBatch(); 
                }
  //              d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertAssetFleetRenewalInsert() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        public boolean updateAssetFleetRenewal(ArrayList list) {

            ArrayList newList = null;
            Asset asset = null;
           
            htmlUtil = new HtmlUtility();
             
            int[] d =null;
            String query= "UPDATE FLEET_SUMINSURED SET SUM_INSURED=?,EFFECTIVE_DATE=? WHERE ASSET_ID=?" ;
            magma.AssetRecordsBean bd = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                 for (int i = 0; i < list.size(); ++i) {
                     bd = (magma.AssetRecordsBean) list.get(i);

                     String assetId = bd.getAsset_id();
  //                   System.out.println("<<<<<assetId: "+assetId);
                     String description = bd.getDescription();
//                     System.out.println("<<<<<description: "+description);
                     String barcode = bd.getBar_code();
                     String comments = bd.getComments();
                     String assetuser = bd.getNewuser();
                     String branchCode = bd.getBranch_Code();
                     String make = bd.getMake();               
                     String userId = bd.getUser_id();
//                     System.out.println("<<<<<make: "+make);
                 	String sbucode = bd.getSbu_code();
                 	String registrationNo = bd.getRegistration_no();
                 	String sumInsured = bd.getCost_price();
                 	String effectiveDate = bd.getDate_of_purchase();
                     if (assetId == null || assetId.equals("")) {
                     	assetId = "0";
                     }
                     if (barcode == null || barcode.equals("")) {
                         barcode = "0";
                     }
                    
                     ps.setDouble(1,Double.parseDouble(sumInsured));
                     ps.setString(2,effectiveDate);
                     ps.setString(3,assetId);
                // ps.addBatch();
                ps.execute();
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: updateAssetFleetRenewal()>>>>>" + ex);
            } 
                return (d.length > 0);
        }

        
        public boolean insertFacilityTransctionDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into FM_MAINTENANCE_DETAILS ( " +
                    "HIST_ID,COST_PRICE,QUANTITY,DESCRIPTION )" +
                    " values(?,?,?,?)";


            
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {   
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String description = bd.getDescription();
                    String costprice = bd.getCost_price();
                    String qty = bd.getQty();
//                    System.out.println("<<<<<<description: "+description+"  costprice: "+costprice+"  qty: "+qty);
                    if (qty == null || qty.equals("")) {
                    	qty = "0";
                    }
                    if (costprice == null || costprice.equals("")) {
                    	costprice = "0.0";
                    }
                    ps.setString(1, histId);
                    ps.setDouble(2, Double.valueOf(costprice));
                    ps.setInt(3, Integer.parseInt(qty));
                    ps.setString(4, description);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertFacilityTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }


        public int insertScheduleDetails(String branchCode,String categoryId,String sub_categoryCode,String vendorCode,String description,String FQ_DueStatus1,String FQ_DueStatus2,String FQ_DueStatus3,String FQ_DueStatus4,
        		String FQ_Due1,String FQ_Due2,String FQ_Due3,String FQ_Due4,String FQ_Status1,String FQ_Status2,String FQ_Status3,String FQ_Status4,String transId,String typeCode) {
            boolean re = false;
            int result = 0;
            String query = "insert into FM_PPM_TMP ( " +
                    "BRANCH_CODE,CATEGORY_CODE,SUB_CATEGORY_CODE,VENDOR_CODE,DESCRIPTION,LASTSERVICE_DATE," +
                    "Q1_STATUS,Q2_STATUS,Q3_STATUS,Q4_STATUS," +
                    "Q1_DUE_DATE,Q2_DUE_DATE,Q3_DUE_DATE,Q4_DUE_DATE," +
                    "POSTING_DATE,TRANSID,TYPE)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

 //                   System.out.println("insertScheduleDetails branchId: "+branchCode);
                    ps.setString(1, branchCode);
                    String categoryCode = approvalRec.getCodeName("select category_code from am_ad_category where category_ID  = "+categoryId+" ");
//                    System.out.println("insertScheduleDetails categoryCode: "+categoryCode+"     categoryId: "+categoryId);
                    ps.setString(2, categoryCode);
                    ps.setString(3, sub_categoryCode);
                    ps.setString(4, vendorCode);
                    ps.setString(5, description);
                    ps.setTimestamp(6, dbConnection.getDateTime(FQ_DueStatus1));
/*                    ps.setTimestamp(7, dbConnection.getDateTime(FQ_DueStatus2));
                    ps.setTimestamp(8, dbConnection.getDateTime(FQ_DueStatus3));
                    ps.setTimestamp(9, dbConnection.getDateTime(FQ_DueStatus4));*/
                    ps.setString(7, FQ_Status1);
                    ps.setString(8, FQ_Status2);
                    ps.setString(9, FQ_Status3);
                    ps.setString(10, FQ_Status4);
                    ps.setTimestamp(11, dbConnection.getDateTime(FQ_Due1));
                    ps.setTimestamp(12, dbConnection.getDateTime(FQ_Due2));
                    ps.setTimestamp(13, dbConnection.getDateTime(FQ_Due3));
                    ps.setTimestamp(14, dbConnection.getDateTime(FQ_Due4));
                    ps.setTimestamp(15, dbConnection.getDateTime(new java.util.Date()));
                    ps.setString(16, transId);
                    ps.setString(17, typeCode);
                    ps.execute();
                    result = 1;
 //                   ps.addBatch();
 //               d = ps.executeBatch();
                System.out.println("Executed Successfully Result: "+result);
 //               if(d.length>0){result = 1;}

            } catch (Exception ex) {
                System.out.println("Error insertBulkTransfer() of BulkUpdateManager -> " + ex);
            } 

            return result;
        }
         

        public ArrayList findPPMByBatchId(String tranId) {

            String selectQuery = "select * from FM_PPM_TMP where ID=? ";

            
            ArrayList listNew = new ArrayList();
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();
                FMppmAllocation ppm = null;
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String branchCode = rs.getString("BRANCH_CODE");
                    String categoryCode = rs.getString("CATEGORY_CODE");
                    String subCategoryCode = rs.getString("SUB_CATEGORY_CODE");
                    String sbuCode = rs.getString("SBU_CODE");
                    String groupId = rs.getString("GROUP_ID");
                    String vendorCode = rs.getString("VENDOR_CODE");
                    String description = rs.getString("DESCRIPTION");
                    String q1Date = rs.getString("LASTSERVICE_DATE");
/*                    String q2Date = rs.getString("Q2_DATE");
                    String q3Date = rs.getString("Q3_DATE");
                    String q4Date = rs.getString("Q4_DATE");*/
                    String q1DueDate = rs.getString("Q1_DUE_DATE");
                    String q2DueDate = rs.getString("Q2_DUE_DATE");
                    String q3DueDate = rs.getString("Q3_DUE_DATE");
                    String q4DueDate = rs.getString("Q4_DUE_DATE");
                    String q1Status = rs.getString("Q1_STATUS"); 
                    String q2Status = rs.getString("Q2_STATUS"); 
                    String q3Status = rs.getString("Q3_STATUS"); 
                    String q4Status = rs.getString("Q4_STATUS"); 
                    String type = rs.getString("TYPE");   
                    String status = rs.getString("STATUS"); 
                    String postingDate = rs.getString("POSTING_DATE"); 

                     ppm = new FMppmAllocation();
                    ppm.setId(id);
                    ppm.setGroupId(groupId);
                    ppm.setDescription(description);
                    ppm.setBranchCode(branchCode);
                    ppm.setCategoryCode(subCategoryCode);
                    ppm.setSubCategoryCode(subCategoryCode);
                    ppm.setSbuCode(sbuCode);
                    ppm.setFq_DueStatus1(q1Date);
/*                    ppm.setFq_DueStatus2(q2Date);
                    ppm.setFq_DueStatus3(q3Date);
                    ppm.setFq_DueStatus3(q4Date);  */
                    ppm.setFq_Due1(q1DueDate);
                    ppm.setFq_Due2(q2DueDate);
                    ppm.setFq_Due3(q3DueDate);
                    ppm.setFq_Due4(q4DueDate);
                    ppm.setFq_Status1(q1Status);
                    ppm.setFq_Status2(q2Status);
                    ppm.setFq_Status3(q3Status);
                    ppm.setFq_Status4(q4Status);
                    ppm.setType(type);
                    ppm.setStatus(status);
                    ppm.setPostingDate(postingDate);
                    listNew.add(ppm);
                    ppm=null;
                   // assetFilter = assetFilter + "'" + id + "',";

                }

            } catch (Exception e) {
                System.out.println("INFO:Error findPPMByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            return listNew;

        }

        public FMppmAllocation getPPMById(String recId) {

            String query = "select * from FM_PPM_TMP where transId= '"+recId+"' ";
           
//            System.out.println("getPPMById in BulkUpdateManager >>>>>> " +query);
            ArrayList list = new ArrayList();
            //declare DTO object
            FMppmAllocation schedule = null;

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
               ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("ID");
                    String transId = rs.getString("transId");
                    String branchCode = rs.getString("BRANCH_CODE");
                    String categoryCode = rs.getString("CATEGORY_CODE");
                    String subCategoryCode = rs.getString("SUB_CATEGORY_CODE");
                    String groupId = rs.getString("GROUP_ID");
                    String vendorCode = rs.getString("VENDOR_CODE");
                    String description = rs.getString("DESCRIPTION");
//                    String q1Date = rs.getString("Q2_DATE");
                    String lastServiceDate = rs.getString("LASTSERVICE_DATE");
/*                    String q2Date = rs.getString("Q2_DATE");
                    String q3Date = rs.getString("Q3_DATE");
                    String q4Date = rs.getString("Q4_DATE");  */
                    String q1DueDate = rs.getString("Q1_DUE_DATE");
                    String q2DueDate = rs.getString("Q2_DUE_DATE");
                    String q3DueDate = rs.getString("Q3_DUE_DATE");
                    String q4DueDate = rs.getString("Q4_DUE_DATE");
                    String q1Status = rs.getString("Q1_STATUS"); 
                    String q2Status = rs.getString("Q2_STATUS"); 
                    String q3Status = rs.getString("Q3_STATUS"); 
                    String q4Status = rs.getString("Q4_STATUS"); 
                    String type = rs.getString("TYPE");   
//                    String status = rs.getString("STATUS"); 
                    String postingDate = rs.getString("POSTING_DATE"); 

                    schedule = new FMppmAllocation(id, transId, branchCode, categoryCode, 
                     		subCategoryCode, vendorCode, 
                             description,  lastServiceDate,q1DueDate,  q2DueDate, q3DueDate, q4DueDate,
                             q1Status, q2Status, q3Status, q4Status, type,
                             "", postingDate);

                }

            } catch (Exception e) {
                System.out.println("INFO:Error fetching PPM Schedule by ID ->" +
                                   e.getMessage());
            } 

            return schedule;
        }
        
        public boolean insertFacilityBulkTransctionDetails(ArrayList list,String reqnBranch,String supervisor, String txnLevel,String groupId, String reqnID) {
            boolean re = false;

            String query = "insert into FM_REQUISITION_DETAILS ( " +
                    "GROUP_ID,BRANCH_CODE,DEPT_CODE,ASSET_ID,SUB_CATEGORY_CODE,ASSET_DESCRIPTION,BAR_CODE)" +
                    " values(?,?,?,?,?,?,?)";


            
  //          System.out.println("<<<<<<groupId: "+groupId+"  reqnID: "+reqnID);
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String assetId = bd.getAsset_id();
                    String branchCode = bd.getBranch_Code();
                    String deptCode = bd.getDepartment_code();
                    String description = bd.getDescription();
                    String subcatCode = bd.getSubcatCode();
                    String barCode = bd.getBar_code();
    //                System.out.println("<<<<<<assetId: "+assetId+"    branchCode: "+branchCode+"   deptCode: "+deptCode+"   description: "+description+"   barCode: "+barCode);
                    ps.setString(1, reqnID);
                    ps.setString(2, branchCode);
                    ps.setString(3, deptCode);
                    ps.setString(4, assetId);
                    ps.setString(5, subcatCode);
                    ps.setString(6, description);
                    ps.setString(7, barCode);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                System.out.println("Executed Successfully ");
                
                String supervisorID = supervisor;
                String status = "A";
                String supervisorName = "";
                String supervisorNameQry = "select Full_name,email,approval_limit from am_gb_user where user_ID ='" + supervisor + "'";
                int apprvLevel = 0;
                String reqnStatus = "";
                
                String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Requisition'";

                String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;

                String branchCode = approvalRec.getCodeName(branchCode_Qry);

    //            System.out.println("the value of >>>>>>>>>> is aprecords.getCodeName(adm_Approv_Lvl_Qry) "+ approvalRec.getCodeName(adm_Approv_Lvl_Qry) );

                int var = Integer.parseInt(approvalRec.getCodeName(adm_Approv_Lvl_Qry));

                int approvalLevelLimit = Integer.parseInt(txnLevel);
                
                String emailMSg="Requisition with ID " + reqnID + " is waiting for your approval.";
                String subject = "Facility Management Requisition";
                String otherparam = "";
                
                if (approvalLevelLimit > 0) {  
                    status = "P";
                    reqnStatus = "PENDING";
                    supervisorID = supervisor;
                    String[] sprvResult = (approvalRec.retrieveArray(supervisorNameQry)).split(":");
                    supervisorName = sprvResult[0];
                    if ((sprvResult[1] != null) && !(sprvResult[1].equals(""))) {
                        mail.sendMailSupervisor(supervisorID,subject,emailMSg,otherparam);
                    }
                } else {
                    //send a mail to all members of the department
                    //supervisor = userid
                    status = "FX";
                   // status = "A";
                    reqnStatus = "APPROVED FOR FACILITY MGT DEPT.";
                    approvalLevelLimit = var;
                    supervisorID = supervisor;
                    //work on mail to be sent to admin department
                   
                    mail.sendMailSupervisor(supervisorID,subject,emailMSg,otherparam);
                   
                }
               
                
            } catch (Exception ex) {
                System.out.println("Error insertFacilityBulkTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }
        
        
        public boolean insertVendorAssessmentDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into VENDOR_ASSESSMENT_DETAILS ( " +
                    "GROUP_ID,CRITERIA,CRITERIONA,CRITERIONB,CRITERIONC,CRITERIOND,CRITERIONE)" +
                    " values(?,?,?,?,?,?,?)";

           
            //ResultSet rs = null;
            magma.net.vao.VendorAssessment bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.net.vao.VendorAssessment) list.get(i);

                    String criteria = bd.getCriteria();
                    String criteriaA = bd.getCriteriaA();
                    String criteriaB = bd.getCriteriaB();
                    String criteriaC = bd.getCriteriaC();
                    String criteriaD = bd.getCriteriaD();
                    String criteriaE = bd.getCriteriaE();
                    System.out.println("<<<<<<criteria: "+criteria+"  criteriaA: "+criteriaA+"  criteriaB: "+criteriaB+"  criteriaC: "+criteriaC+"  criteriaD: "+criteriaD+"  criteriaE: "+criteriaE+"    histId: "+histId);
                    ps.setString(1, histId);
                    ps.setString(2, criteria);
                    ps.setString(3, criteriaA);
                    ps.setString(4, criteriaB);
                    ps.setString(5, criteriaC);
                    ps.setString(6, criteriaD);
                    ps.setString(7, criteriaE);
                    ps.addBatch();  
                }
                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertVendorAssessmentDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        
        public boolean insertBranchVisitDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into FM_BRANCH_VISIT_DETAILS ( " +
                    "GROUP_ID,SNO,ELEMENT,CONDITION,REMARK,ACTION,DUEDATE)" +
                    " values(?,?,?,?,?,?,?)";

           
            //ResultSet rs = null;
            BranchVisit bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.net.vao.BranchVisit) list.get(i);

                    String sNo = bd.getSNo();
                    String element = bd.getElement();
                    String condition = bd.getCondition();
                    String remark = bd.getRemark();
                    String actionby = bd.getActionby();
                    String dueDate = bd.getDueDate();
  //                  System.out.println("<<<<<<sNo: "+sNo+"  element: "+element+"  condition: "+condition);
                    ps.setString(1, histId);
                    ps.setString(2, sNo);
                    ps.setString(3, element);
                    ps.setString(4, condition);
                    ps.setString(5, remark);
                    ps.setString(6, actionby);
                    ps.setString(7, dueDate);
                    ps.addBatch();  
                }
                d = ps.executeBatch();
//                System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBranchVisitDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        
        public boolean insertEnvironmenteDetails(ArrayList list,String histId,String itemType) {
            boolean re = false;

            String query = "insert into FM_SOCIALENVIRONMENT_DETAILS ( " +
                    "HIST_ID,AMOUNT,QUANTITY,DESCRIPTION,ITEM_TYPE )" +
                    " values(?,?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {   
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String description = bd.getDescription();
                    String costprice = bd.getCost_price();
                    String qty = bd.getQty();
 //                   System.out.println("<<<description: "+description+"  costprice: "+costprice+"  qty: "+qty+"  itemType: "+itemType);
                    if (qty == null || qty.equals("")) {
                    	qty = "0";
                    }
                    if (costprice == null || costprice.equals("")) {
                    	costprice = "0.0";
                    }
                    ps.setString(1, histId);
                    ps.setDouble(2, Double.valueOf(costprice));
                    ps.setInt(3, Integer.parseInt(qty));
                    ps.setString(4, description);
                    ps.setString(5, itemType);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertEnvironmenteDetails of BulkUpdateManager -> " + ex);
            } 
            return (d.length > 0);
        }
        
        public boolean updateVendorAssessmentDetailsRec(ArrayList list,String histId) {
            boolean re = false;
            String deleterec = "DELETE FROM VENDOR_ASSESSMENT_DETAILS WHERE GROUP_ID = '"+histId+"'";
//            System.out.println("Query for  Delete of Assessment>>> "+deleterec);
            arb.updateAssetStatusChange(deleterec);
            
            String query = "insert into VENDOR_ASSESSMENT_DETAILS ( " +
                    "GROUP_ID,CRITERIA,CRITERIONA,CRITERIONB,CRITERIONC,CRITERIOND,CRITERIONE)" +
                    " values(?,?,?,?,?,?,?)";

           
            //ResultSet rs = null;
            magma.net.vao.VendorAssessment bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.net.vao.VendorAssessment) list.get(i);

                    String criteria = bd.getCriteria();
                    String criteriaA = bd.getCriteriaA();
                    String criteriaB = bd.getCriteriaB();
                    String criteriaC = bd.getCriteriaC();
                    String criteriaD = bd.getCriteriaD();
                    String criteriaE = bd.getCriteriaE();
 //                   System.out.println("<<<<<<criteria: "+criteria+"  criteriaA: "+criteriaA+"  criteriaB: "+criteriaB+"  criteriaC: "+criteriaC+"  criteriaD: "+criteriaD+"  criteriaE: "+criteriaE+"    histId: "+histId);
                    ps.setString(1, histId);
                    ps.setString(2, criteria);
                    ps.setString(3, criteriaA);
                    ps.setString(4, criteriaB);
                    ps.setString(5, criteriaC);
                    ps.setString(6, criteriaD);
                    ps.setString(7, criteriaE);
                    ps.addBatch();  
                }
                d = ps.executeBatch();
                System.out.println("Executed Successfully ");
                String prq = "update am_asset_approval set process_status='PR' where asset_id='"+histId+"' and (process_status='R' or process_status IS NULL or process_status = 'RP')";
//                System.out.println("<<<<<<prq: "+prq);
                arb.updateAssetStatusChange(prq);   
            } catch (Exception ex) {
                System.out.println("Error insertVendorAssessmentDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }
        
        public boolean expensesTransctionDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into PR_EXPENSES_DETAILS ( " +
                    "HIST_ID,COST_PRICE,QUANTITY,DESCRIPTION )" +
                    " values(?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {   
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
//                System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String description = bd.getDescription();
                    String costprice = bd.getCost_price();
                    String qty = bd.getQty();
//                    System.out.println("<<<<<<description: "+description+"  costprice: "+costprice+"  qty: "+qty);
                    if (qty == null || qty.equals("")) {
                    	qty = "0";
                    }
                    if (costprice == null || costprice.equals("")) {
                    	costprice = "0.0";
                    }
                    ps.setString(1, histId);
                    ps.setDouble(2, Double.valueOf(costprice));
                    ps.setInt(3, Integer.parseInt(qty));
                    ps.setString(4, description);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error expensesTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        public ArrayList[] findAssetTransferAcceptByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkTransfer where batch_id=? ";

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                ResultSet rs = ps.executeQuery();

                StockRecordsBean aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String oldbranchId = rs.getString("oldbranch_id");
                    String olddeptId = rs.getString("olddept_id");
                    String oldsbucode = rs.getString("oldSBU_CODE");
                    String oldsectionId = rs.getString("oldsection_id");
                    String oldassetuser = rs.getString("oldAsset_User");
                    String newassetId = rs.getString("NEW_ASSET_ID");
                    String newbranchId = rs.getString("newbranch_id");
                    String newdeptId = rs.getString("newdept_id");
                    String newsbucode = rs.getString("newSBU_CODE");
                    String newsectionId = rs.getString("newsection_id");
                    String newassetuser = rs.getString("newAsset_User");
                    String batchId = rs.getString("Batch_id");
                    String transferbyId = rs.getString("Transferby_id");
                    int assetCode =  rs.getInt("ASSET_CODE");
                    
                     aset = new StockRecordsBean();
                    aset.setAsset_id(id);
                    aset.setDescription(description);
                    aset.setRegistration_no(registrationNo);
                    aset.setBranch_id(oldbranchId);
                    aset.setDepartment_id(olddeptId);
                    aset.setSbu_code(oldsbucode);
                    aset.setSection_id(oldsectionId);
                    aset.setUser(oldassetuser);
                    aset.setNewbranch_id(newbranchId);
                    aset.setNewdepartment_id(newdeptId);
                    aset.setNewsbu_code(newsbucode);
                    aset.setNewsection_id(newsectionId);
                    aset.setNewuser(newassetuser);
                    aset.setNewasset_id(newassetId);
                    aset.setAssetCode(assetCode);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                } 

                boolean index = assetFilter.endsWith(",");
//                System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
//                    System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findAssetByIDforAcceptance(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error () in BulkUpdateManager-> " +
                        e.getMessage());
            } 
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList findAssetByIDforAcceptance(String queryFilter) {


//
//            String selectQuery =
//                    "SELECT ASSET_ID,REGISTRATION_NO," +
//                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model," +
//                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
//                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
//                    "FROM AM_ASSET  WHERE ASSET_ID IN (SELECT ASSET_ID FROM am_assettransfer WHERE ASSET_STATUS = 'APPROVED') " + queryFilter;
//            
            String selectQuery =
                    "SELECT ASSET_ID,REGISTRATION_NO," +
                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model," +
                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                    "FROM AM_ASSET  WHERE ASSET_ID IN (SELECT ASSET_ID FROM am_gb_bulkTransfer) " + queryFilter;
  
//            System.out.println("the query in findAssetByIDforAcceptance is <<<<<<<<<<<<< " + selectQuery);

           

            ArrayList listOld = new ArrayList();

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);

               ResultSet  rs = ps.executeQuery();
                Asset aset =null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String registrationNo = rs.getString("REGISTRATION_NO");
                    String description = rs.getString("DESCRIPTION");
                    String vendorAC = rs.getString("Vendor_AC");
                    String assetModel = rs.getString("Asset_Model");
                    String AssetSerialNo = rs.getString("Asset_Serial_No");
                    String AssetEngineNo = rs.getString("Asset_Engine_No");
                    int SupplierName = rs.getInt("Supplier_Name");
                    String assetUser = rs.getString("ASSET_USER");
                    int assetMaintenance = rs.getInt("Asset_Maintenance");
                    String authorizedBy = rs.getString("Authorized_By");
                    String purchaseReason = rs.getString("Purchase_Reason");
                    String sbuCode = rs.getString("SBU_CODE");
                    String deptCode = rs.getString("DEPT_CODE"); 
                    String deptid = rs.getString("Dept_ID");                 
                    String spare1 = rs.getString("Spare_1");
                    String spare2 = rs.getString("Spare_2");
                    String spare3 = rs.getString("Spare_3");
                    String spare4 = rs.getString("Spare_4");
                    String spare5 = rs.getString("Spare_5");
                    String spare6 = rs.getString("Spare_6");
                    String barCode = rs.getString("BAR_CODE");
                    String branchId = String.valueOf(rs.getInt("branch_id"));
                    String subcategoryCode = rs.getString("sub_category_code"); 
                    String subcategoryId = String.valueOf(rs.getInt("sub_category_ID"));
                     aset = new Asset();
                    aset.setId(id);
                    aset.setDescription(description);
                    aset.setRegistrationNo(registrationNo);
                    aset.setVendorAc(vendorAC);
                    aset.setAssetMake(assetModel);
                    aset.setSerialNo(AssetSerialNo);
                    aset.setEngineNo(AssetEngineNo);
                    aset.setSupplierName(SupplierName);
                    aset.setAssetUser(assetUser);
                    aset.setAssetMaintain(assetMaintenance);
                    aset.setAuthorizeBy(authorizedBy);
                    aset.setPurchaseReason(purchaseReason);
                    aset.setSbuCode(sbuCode);
                    aset.setDepcode(deptCode);
                    aset.setDepartmentId(deptid);
                    aset.setSpare1(spare1);
                    aset.setSpare2(spare2);
                    aset.setBarCode(barCode);
                    aset.setBranchId(branchId);
                    aset.setSubcategoryCode(subcategoryCode);
                    aset.setSubcategoryId(subcategoryId);

                    listOld.add(aset);
                    aset = null;

                }



            } catch (Exception e) {
                System.out.println("INFO:Error findStockByID() in BulkStockUpdateManager-> " +
                        e.getMessage());
            } 
            System.out.println("the size of listOld is >>>>>>> " + listOld.size());
            return listOld;

        }
        
        public boolean insertAssetProofMenuManager(String batchId, String userId, String menuType) {
            boolean re = false;

            String query = "insert into ASSET_PROOF_MENU_MGR (BATCH_ID,USER_ID,PROOF_DATE,MENU_TYPE ) values(?,?,?,?)";
           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, batchId);
                    ps.setString(2, userId);
                    ps.setTimestamp(3, dbConnection.getDateTime(new java.util.Date()));
                    ps.setString(4, menuType);
                    ps.addBatch();

                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertFleetTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        
        public boolean insertMaterialTransctionDetails(ArrayList list,String histId) {
            boolean re = false;

            String query = "insert into FT_MATERIALRETRIEVAL_DETAILS ( " +
                    "HIST_ID,COST_PRICE,QUANTITY,DESCRIPTION )" +
                    " values(?,?,?,?)";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try { 
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
 //               System.out.println("<<<<<<List Size: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);

                    String description = bd.getDescription();
                    String costprice = bd.getCost_price();
                    String qty = bd.getQty();
 //                   System.out.println("<<<<<<description: "+description+"  costprice: "+costprice+"  qty: "+qty);
                    if (qty == null || qty.equals("")) {
                    	qty = "0";
                    }
                    if (costprice == null || costprice.equals("")) {
                    	costprice = "0.0";
                    }
                    ps.setString(1, histId);
                    ps.setDouble(2, Double.valueOf(costprice));
                    ps.setInt(3, Integer.parseInt(qty));
                    ps.setString(4, description);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertMaterialTransctionDetails of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        public boolean insertMaterialRetrievalUpdateTmp(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into FT_MAINTENANCE_DETAILS_TMP ( " +
                    "LT_ID,HIST_ID,SERIAL_NO,MAKE,COST_PRICE,QUANTITY,DESCRIPTION,RET_SERIAL_NO,RET_MAKE,RET_QUANTITY,BATCH_ID)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?) ";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String id = bd.getOldId();
                    String registration = bd.getRegistration_no();
                    String description = bd.getDescription();
                    String serial = bd.getSerial_number();
                    String engine = bd.getEngine_number();
                    String assetuser = bd.getUser();
                    String reason1 = bd.getReason();
                    String sbu = bd.getSbu_code();
                    String dept = bd.getDepartment_id();
                    String spare1 = bd.getSpare_1();  //componetReplace
                    String spare2 = bd.getSpare_2();
                    String retMake = bd.getSpare_3();  //retAssetMake
                    String retSerialNo = bd.getSpare_4();  //retSerialNo
                    String spare5 = bd.getSpare_5();
                    String spare6 = bd.getSpare_6();                
                    String barcode = bd.getBar_code();
                    String lpo1 = bd.getLpo();
                    String asset_id = bd.getAsset_id();
                    String branchId = bd.getBranch_id();
                    String histId =	bd.getBatchId();
                    String quantity = bd.getQty();  //quantity
                    int retQuantity = bd.getQuantity();  //retquantity
                    String make = bd.getMake();
                    int assetCode = bd.getAssetCode();
                    String assetMaintenance = bd.getMaintained_by();  // Details
                    String costPrice = bd.getCost_price();
//                    System.out.println("======retMake in insertMaterialRetrievalUpdateTmp: "+retMake);
                    if (registration == null || registration.equals("")) {
                        registration = "0";
                    }
                    if (description == null || description.equals("")) {
                        description = "";
                    }
                    if (serial == null || serial.equals("")) {
                        serial = "0";
                    }
                    if (engine == null || engine.equals("")) {
                        engine = "0";
                    }
                    if (assetuser == null || assetuser.equals("")) {
                        assetuser = "0";
                    }
                    if (reason1 == null || reason1.equals("")) {
                        reason1 = "0";
                    }
                    if (sbu == null || sbu.equals("")) {
                        sbu = "0";
                    }
                    if (dept == null || dept.equals("")) {
                    	dept = "0";
                    }                              
                    if (spare1 == null || spare1.equals("")) {
                        spare1 = "0";
                    }
                    if (spare2 == null || spare2.equals("")) {
                        spare2 = "0";
                    }
                    if (retMake == null || retMake.equals("")) {
                    	retMake = "0";
                    }
                    if (retSerialNo == null || retSerialNo.equals("")) {
                    	retSerialNo = "0";
                    }
                    if (spare5 == null || spare5.equals("")) {
                        spare5 = "0";
                    }
                    if (spare6 == null || spare6.equals("")) {
                        spare6 = "0";
                    }                
                    if (branchId == null || branchId.equals("")) {
                        branchId = "0";
                    }

                    ps.setInt(1, Integer.parseInt(id));
                    ps.setString(2, histId);
                    ps.setString(3, serial);
                    ps.setString(4, make);
                    ps.setDouble(5, Double.parseDouble(costPrice));
                    ps.setInt(6, Integer.parseInt(quantity));
                    ps.setString(7, description);
                    ps.setString(8, retSerialNo);
                    ps.setString(9, retMake);
                    ps.setInt(10, retQuantity);
                    ps.setString(11, String.valueOf(bacthId));
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBulkUpdate() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }

        public boolean materialRetrievalUpdate(ArrayList list,String bacthId) {

            ArrayList newList = null;
          
            magma.net.vao.Asset bd = null;
            htmlUtil = new HtmlUtility();
             
            int[] d =null;
            String query= "UPDATE FT_MAINTENANCE_DETAILS SET RET_SERIAL_NO = ?," +
                    "RET_MAKE = ?,RET_QUANTITY = ? WHERE LT_ID = ? AND HIST_ID = ? " ;

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
                 for (int i = 0; i < list.size(); ++i) {
                	 
                     bd = (magma.net.vao.Asset) list.get(i);
                     String ltId = bd.getId();
                     String description = bd.getDescription();
                     String serial = bd.getSpare4();
                     String histId =	bd.getSpare2();
                     int retQuantity = bd.getTotalLife();  //retquantity
                     String make = bd.getSpare3();
                     int assetCode = bd.getAssetCode();
                 bd=null;
                 
                 
                 ps.setString(1,serial);
                 ps.setString(2,make);
                 ps.setInt(3,retQuantity);
                 ps.setString(4,ltId);
                 ps.setString(5,histId);
                // ps.addBatch();  
                ps.execute();
                String ap = "update FT_MAINTENANCE_DETAILS_TMP set Status='APPROVED' where STATUS IS NULL AND LT_ID = '"+ltId+"' AND HIST_ID= '"+histId+"' ";
                arb.updateAssetStatusChange(ap);
                 }
                d=ps.executeBatch();  

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: FT_MAINTENANCE_DETAILS()>>>>>" + ex);
            } 
                return (d.length > 0);
        }

        public ArrayList[] findMaterialRetrievalByBatchId(String tranId) {

            String assetFilter = " and LT_ID in (";
            String selectQuery =
                    "select * from FT_MAINTENANCE_DETAILS_TMP where BATCH_ID=? order by LT_ID ";

           
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
//            System.out.println("=====>>>>selectQuery: "+selectQuery);
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, tranId);
                ResultSet rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {                   
                    String id = rs.getString("LT_ID");
//                    System.out.println("=====>>>>Id: "+id);
                    String histId = rs.getString("HIST_ID");
//                    System.out.println("=====>>>>histId: "+histId);
//                    String assetId = rs.getString("ASSET_ID");
//                    String registrationNo = rs.getString("REGISTRATION_NO");
//                    String branchId = rs.getString("BRANCH_ID");
                    String description = rs.getString("DESCRIPTION");
//                    String componetReplace = rs.getString("COMPONENT_REPLACED");
//                    String assetUser = rs.getString("USER_ID");
                    String assetMake = rs.getString("MAKE");
                    String serialNo = rs.getString("SERIAL_NO");
                    String assetMaintenance = rs.getString("DETAILS");
                    double cost = rs.getDouble("COST_PRICE");
//                      String postingDate = rs.getString("REPAIRED_DATE");
//                      double nbv = rs.getDouble("TOTAL_COST");
                    int quantity = rs.getInt("QUANTITY");
//                    String asset_status=rs.getString("STATUS");
//                    int assetCode = rs.getInt("asset_code");
//                    String invoiceNo = rs.getString("INVOICE_NO"); 
                    String retAssetMake = rs.getString("RET_MAKE");
                    String retSerialNo = rs.getString("RET_SERIAL_NO");
                    int retquantity = rs.getInt("RET_QUANTITY");
                    int quantitySold = rs.getInt("RET_QUANTITY");
                    double amountSold = rs.getDouble("AMOUNT_SOLD");
                    
				     aset = new Asset();
				     aset.setId(id);
				     aset.setDescription(description);   
//				     aset.setRegistrationNo(registrationNo);
//				     aset.setBranchId(branchId);
				     aset.setAssetMake(assetMake);
//				     aset.setAssetUser(assetUser);
				     aset.setAssetMaintenance(assetMaintenance);
				     aset.setCost(cost);
//				     aset.setPostingDate(postingDate);
//				     aset.setNbv(nbv);
//				     aset.setAsset_status(asset_status);
//				     aset.setAssetCode(assetCode);
				     aset.setQuantity(quantity);
//				     aset.setOLD_ASSET_ID(assetId);
				     aset.setSpare2(histId);
//				     aset.setRegistrationNo(registrationNo);
				     aset.setAssetMake(assetMake);
				     aset.setSerialNo(serialNo);
//				     aset.setSpare1(componetReplace);
				     aset.setTotalLife(retquantity);               
				     aset.setSpare3(retAssetMake);
				     aset.setSpare4(retSerialNo);
				     aset.setAMOUNT_PTD(amountSold);
				     aset.setDriver(quantitySold);                                       
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                }

                boolean index = assetFilter.endsWith(",");
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
                }
                assetFilter = assetFilter + ")";
//                System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findMateriaRetrievalByID(assetFilter+" order by LT_ID");
            } catch (Exception e) {
                System.out.println("INFO:Error findMaterialRetrievalByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList findMateriaRetrievalByID(String queryFilter) {



            String selectQuery =
                    "select * from FT_MAINTENANCE_DETAILS WHERE HIST_ID IS NOT NULL " + queryFilter;
//            System.out.println("the selectQuery  in findAssetByID is <<<<<<<<<<<<< " + selectQuery);

           
            ArrayList listOld = new ArrayList();

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(selectQuery);

                ResultSet rs = ps.executeQuery();
                Asset aset =null;
                while (rs.next()) {
                    String id = rs.getString("LT_ID");
//                    System.out.println("=====>>>>Id: "+id);
                    String histId = rs.getString("HIST_ID");
//                    System.out.println("=====>>>>histId: "+histId);
                    String description = rs.getString("DESCRIPTION");
                    String assetMake = rs.getString("MAKE");
                    String serialNo = rs.getString("SERIAL_NO");
                    String assetMaintenance = rs.getString("DETAILS");
                    double cost = rs.getDouble("COST_PRICE");
                    int quantity = rs.getInt("QUANTITY");
                    String retAssetMake = rs.getString("RET_MAKE");
                    String retSerialNo = rs.getString("RET_SERIAL_NO");
                    int retquantity = rs.getInt("RET_QUANTITY");
                    int quantitySold = rs.getInt("RET_QUANTITY");
                    double amountSold = rs.getDouble("AMOUNT_SOLD");
                     aset = new Asset();
				     aset.setId(id);
				     aset.setDescription(description);   
				     aset.setAssetMake(assetMake);
				     aset.setAssetMaintenance(assetMaintenance);
				     aset.setCost(cost);
				     aset.setQuantity(quantity);
				     aset.setSpare2(histId);
				     aset.setAssetMake(assetMake);
				     aset.setSerialNo(serialNo);
				     aset.setTotalLife(retquantity);               
				     aset.setSpare3(retAssetMake);
				     aset.setSpare4(retSerialNo);
				     aset.setAMOUNT_PTD(amountSold);
				     aset.setDriver(quantitySold);  
                    listOld.add(aset);
                    aset = null;

                }



            } catch (Exception e) {
                System.out.println("INFO:Error findMateriaRetrievalByID() in BulkUpdateManager-> " +
                        e.getMessage());
            } 
     //       System.out.println("the size of listOld is >>>>>>> " + listOld.size());
            return listOld;

        }

        public boolean materialRetrievalSoldUpdate(ArrayList list,String bacthId) {

            ArrayList newList = null;
            magma.AssetRecordsBean bd = null;
//            htmlUtil = new HtmlUtility();
//            System.out.println("=====bacthId in materialRetrievalSoldUpdate:"+bacthId);
            int[] d =null;
            String query= "UPDATE FT_MAINTENANCE_DETAILS SET " +
                    "QUANTITY_SOLD = ?,AMOUNT_SOLD = ? WHERE LT_ID = ? AND HIST_ID = ? " ;

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                 for (int i = 0; i < list.size(); ++i) {
                	 
                     bd = (magma.AssetRecordsBean) list.get(i);
                     String description = bd.getDescription();
//                     System.out.println("=====description in materialRetrievalSoldUpdate:"+description);
                     String quantutySold = bd.getQty();
//                     System.out.println("=====quantutySold in materialRetrievalSoldUpdate:"+quantutySold);
                     String amountSold = bd.getCost_price();
//                     System.out.println("=====amountSold in materialRetrievalSoldUpdate:"+amountSold);
                     int assetCode = bd.getAssetCode();    
//                     System.out.println("=====assetCode in materialRetrievalSoldUpdate:"+assetCode);
                     String ltId = bd.getBar_code();
//                     System.out.println("=====ltId in materialRetrievalSoldUpdate:"+ltId);
                 bd=null;
                 ps.setInt(1,Integer.parseInt(quantutySold));
                 ps.setDouble(2,Double.parseDouble(amountSold));
                 ps.setString(3,ltId);
                 ps.setString(4,bacthId);
                ps.execute();
//                arb.updateAssetStatusChange(ap);
                 }
                d=ps.executeBatch();  

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: UPDATE FT_MAINTENANCE_DETAILS() FOR AMOUNT SOLD>>>>>" + ex);
            } 
                return (d.length > 0);
        }

        public boolean insertMaterialRetrievalSold(ArrayList list, int bacthId) {
            boolean re = false;

            String query = "insert into FT_MAINTENANCE_DETAILS_TMP ( " +
                    "LT_ID,HIST_ID,SERIAL_NO,MAKE,COST_PRICE,QUANTITY,DESCRIPTION,RET_SERIAL_NO,RET_MAKE,RET_QUANTITY,QUANTITY_SOLD,AMOUNT_SOLD,VENDOR_CODE,BATCH_ID)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


           
            //ResultSet rs = null;
            magma.AssetRecordsBean bd = null;
            int[] d = null;
            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.AssetRecordsBean) list.get(i);
                    String id = bd.getOldId();
                    String registration = bd.getRegistration_no();
                    String description = bd.getDescription();
                    String serial = bd.getSerial_number();
                    String engine = bd.getEngine_number();
                    String assetuser = bd.getUser();
                    String reason1 = bd.getReason();
                    String sbu = bd.getSbu_code();
                    String dept = bd.getDepartment_id();
                    String spare1 = bd.getSpare_1();  //componetReplace
                    String spare2 = bd.getSpare_2();
                    String retMake = bd.getSpare_3();  //retAssetMake
                    String retSerialNo = bd.getSpare_4();  //retSerialNo
                    String spare5 = bd.getSpare_5();
                    String spare6 = bd.getSpare_6();                
                    String barcode = bd.getBar_code();
                    String lpo1 = bd.getLpo();
                    String asset_id = bd.getAsset_id();
                    String branchId = bd.getBranch_id();
                    String histId =	bd.getBatchId();
                    String quantity = bd.getQty();  //quantity
                    int retQuantity = bd.getQuantity();  //retquantity
                    String soldQuantity = bd.getSpare_5();  //QuantitySold
                    String make = bd.getMake();
                    int assetCode = bd.getAssetCode();
                    String assetMaintenance = bd.getMaintained_by();  // Details
                    String costPrice = bd.getCost_price();
                    String vendorCode = bd.getVendorCode(); 
                    String amountSold = bd.getAmountPTD();
//                    System.out.println("======retMake in insertMaterialRetrievalUpdateTmp: "+retMake);
                    if (registration == null || registration.equals("")) {
                        registration = "0";
                    }
                    if (description == null || description.equals("")) {
                        description = "";
                    }
                    if (serial == null || serial.equals("")) {
                        serial = "0";
                    }
                    if (engine == null || engine.equals("")) {
                        engine = "0";
                    }
                    if (assetuser == null || assetuser.equals("")) {
                        assetuser = "0";
                    }
                    if (reason1 == null || reason1.equals("")) {
                        reason1 = "0";
                    }
                    if (sbu == null || sbu.equals("")) {
                        sbu = "0";
                    }
                    if (dept == null || dept.equals("")) {
                    	dept = "0";
                    }                              
                    if (spare1 == null || spare1.equals("")) {
                        spare1 = "0";
                    }
                    if (spare2 == null || spare2.equals("")) {
                        spare2 = "0";
                    }
                    if (retMake == null || retMake.equals("")) {
                    	retMake = "0";
                    }
                    if (retSerialNo == null || retSerialNo.equals("")) {
                    	retSerialNo = "0";
                    }
                    if (spare6 == null || spare6.equals("")) {
                        spare6 = "0";
                    }                
                    if (branchId == null || branchId.equals("")) {
                        branchId = "0";
                    }

                    ps.setInt(1, Integer.parseInt(id));
                    ps.setString(2, histId);
                    ps.setString(3, serial);
                    ps.setString(4, make);
                    ps.setDouble(5, Double.parseDouble(costPrice));
                    ps.setInt(6, Integer.parseInt(quantity));
                    ps.setString(7, description);
                    ps.setString(8, retSerialNo);
                    ps.setString(9, retMake);
                    ps.setInt(10, retQuantity);
                    ps.setInt(11, Integer.parseInt(soldQuantity));
                    ps.setDouble(12, Double.parseDouble(amountSold));
                    ps.setString(13, vendorCode);
                    ps.setString(14, String.valueOf(bacthId));
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBulkUpdate() of BulkUpdateManager -> " + ex);
            } 

            return (d.length > 0);
        }



        
        
}
