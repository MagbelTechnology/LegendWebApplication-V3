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
import magma.net.dao.MagmaDBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DatetimeFormat;

import java.util.ArrayList;

import magma.net.vao.Asset;

import com.magbel.util.HtmlUtility;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.ia.bus.InventoryAdjustmtServiceBus;
//import com.magbel.ia.util.ApplicationHelper;
import com.magbel.util.ApplicationHelper;
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.servlet.BulkIssuanceExport;

import magma.StockRecordsBean;
public class BulkStockUpdateManager extends legend.ConnectionClass {

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;
    HtmlUtility htmlUtil = null;
    ApprovalManager approverManager = new ApprovalManager();
    InventoryAdjustmtServiceBus serviceBus = new InventoryAdjustmtServiceBus();
    CodeGenerator cg = new CodeGenerator();
    ApplicationHelper helper = new ApplicationHelper();
    ApplicationHelper2 applHelper = new ApplicationHelper2();
    EmailSmsServiceBus mail = new EmailSmsServiceBus();
    SimpleDateFormat timer;
    ApplicationHelper apph;
    public BulkStockUpdateManager() throws Exception {

        try {  
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();
            timer = new SimpleDateFormat("kk:mm:ss");
            apph = new ApplicationHelper();
            
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
                "SPARE_1,SPARE_2,SPARE_3,SPARE_4,SPARE_5,SPARE_6,BAR_CODE, ASSET_ID,BATCH_ID,BRANCH_ID,DEPT_ID,SUB_CATEGORY_ID )" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.StockRecordsBean bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.StockRecordsBean) list.get(i);
 
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
                "BATCH_ID,TRANSFERBY_ID,CATEGORY_ID,NEW_ASSET_ID,COST_PRICE,ACCUM_DEP,MONTHLY_DEP,NBV,TRANSFER_DATE,ASSET_CODE)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.StockRecordsBean bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.StockRecordsBean) list.get(i);
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
                System.out.println("InsertBulkTransfer branchId: "+branchId);
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
            System.out.println("Error insertBulkTransfer() of BulkUpdateManager -> " + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return assetCode;
    }
    
    public boolean setBatchId(String batchId) throws Exception {

        String query = "update am_asset_approval set asset_id=? where transaction_id=? ";

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, batchId);
            ps.setInt(2, Integer.parseInt(batchId));

            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println("BulkUpdateManager: setBatchId():WARN:Error updating to am_asset_approval table->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return done;
    }

    public ArrayList[] findAssetByBatchId(String tranId) {


        String assetFilter = " and asset_id in (";
        String selectQuery =
                "select * from am_gb_bulkupdate where batch_id=? ";

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList listNew = new ArrayList();
        ArrayList listOld = new ArrayList();
        ArrayList[] listNewOld = new ArrayList[2];
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(tranId));
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
            System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
            listOld = findStockByID(assetFilter);

        } catch (Exception e) {
            System.out.println("INFO:Error findAssetByBatchId() in BulkUpdateManager-> " +
                    e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }
        System.out.println("the size of listNew is >>>>>> " + listNew.size());
        listNewOld[0] = listNew;
        listNewOld[1] = listOld;
        Asset assNew = null;
        Asset assOld = null;

        return listNewOld;

    }

    public ArrayList findStockByID(String queryFilter) {



        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO," +
                " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model,QUANTITY," +
                "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
                "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
 //       System.out.println("the query is <<<<<<<<<<<<< " + selectQuery);

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        ArrayList listOld = new ArrayList();

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);

            rs = ps.executeQuery();
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
                int quantity = rs.getInt("QUANTITY");
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
                aset.setQuantity(quantity);

                listOld.add(aset);
                aset = null;

            }



        } catch (Exception e) {
            System.out.println("INFO:Error findStockByID() in BulkStockUpdateManager-> " +
                    e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }
        System.out.println("the size of listOld is >>>>>>> " + listOld.size());
        return listOld;

    }


    public boolean updateBulkAsset(ArrayList list) {

        ArrayList newList = null;
        Asset asset = null;
        Connection con = null;
        PreparedStatement ps = null;
        htmlUtil = new HtmlUtility();
         
        int[] d =null;
        String query= "UPDATE ST_STOCK SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,Spare_3=?,Spare_4=?,Spare_5=?,Spare_6=?," +
                "BAR_CODE=?, DEPT_ID=?, DEPT_CODE=?, SUB_CATEGORY_ID=?, SUB_CATEGORY_CODE=? WHERE ASSET_ID=?" ;

        try {
             con = dbConnection.getConnection("legendPlus");

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
                 ps = con.prepareStatement(query);

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
        } finally {
            dbConnection.closeConnection(con, ps);
        }
            return (d.length > 0);
    }


    public ArrayList findAssetsByBatchId(String tranId) {


        //String assetFilter = " and asset_id in (";
        String selectQuery =
                "select * from am_gb_bulkupdate where batch_id=? ";

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList listNew = new ArrayList();
       // ArrayList listOld = new ArrayList();
       // ArrayList[] listNewOld = new ArrayList[2];
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(tranId));
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
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }
//        System.out.println("the size of listNew is >>>>>> " + listNew.size());
        return listNew;

    }

        public double[] findMinMaxAssetCost(String assetId, double minAssetCost, double maxAssetCost) {
        double costPrice = 0.0;
        double[] result = new double[2];
        String selectQuery =  "select cost_price from am_asset where asset_id=?";

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1,assetId);
            rs = ps.executeQuery();


            while (rs.next()) {
                costPrice= rs.getDouble("cost_price");
                      }

        if(costPrice > maxAssetCost )result[0] =costPrice;
	if(costPrice < minAssetCost )result[1] =costPrice;

        } catch (Exception e) {
            System.out.println("INFO:Error findMinMaxAssetCost() in BulkUpdateManager-> " +
                    e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }


        return result;

    }

        public ArrayList[] findStockTransferByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkTransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

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
            //    System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
              //      System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findStockByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findStockTransferByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public boolean BulkAssetTransfer(ArrayList list,String compCode,String userId) {

            ArrayList newList = null;
 //           StockRecordsBean asset = null;
            magma.StockRecordsBean asset = null;
            Connection con = null;
            PreparedStatement ps = null;
            htmlUtil = new HtmlUtility();
             
            int[] d =null;   
            String query= "UPDATE ST_STOCK SET ASSET_ID=?," +
                    " OLD_ASSET_ID=?,ASSET_USER=?,Branch_ID=?,Dept_ID=?,Section_id=?," +
                    "SBU_CODE=?,BRANCH_CODE=?,DEPT_CODE=?,SECTION_CODE=? WHERE ASSET_ID=?" ;
            System.out.println("I am in bulk Transfer manager is >>>>>>>>>>> "+list.size());
            try {
                 con = dbConnection.getConnection("legendPlus");

                 for (int i = 0; i < list.size(); ++i) {
            asset = (magma.StockRecordsBean) list.get(i);
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
                	String concatCode = htmlUtil.findObject("select WAREHOUSE_CODE+'#'+ITEM_CODE+'#'+ItemType from ST_STOCK where asset_id ='"+oldassetId+"'");
                	String[] cncatarray = concatCode.split("#");
                	String oldwarehouseCode = cncatarray[0];
                	String itemCode = cncatarray[1];
                	String ItemType = cncatarray[2];
                    String quantity = htmlUtil.findObject("select quantity from ST_STOCK where asset_id ='"+oldassetId+"'");
                    String oldbranchId = htmlUtil.findObject("select branch_id from ST_STOCK where asset_id ='"+oldassetId+"'");
                	String branchCode = htmlUtil.findObject("select BRANCH_CODE from AM_AD_BRANCH where BRANCH_ID ="+newbranchId+"");
                    
                    asset=null;
                     ps = con.prepareStatement(query);

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
                
            	String newwarehouseCode = htmlUtil.findObject("select WAREHOUSE_CODE from ST_WAREHOUSE where BRANCH_CODE ='"+branchCode+"'");
              	String inventTotal =htmlUtil.findObject("SELECT COUNT(*) FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+newwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
              	String frompreviousBal =htmlUtil.findObject("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+oldwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
              	String topreviousBal =htmlUtil.findObject("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+newwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
              	if(topreviousBal.equalsIgnoreCase("UNKNOWN")){topreviousBal = "0";}
              	if(frompreviousBal.equalsIgnoreCase("UNKNOWN")){frompreviousBal = "0";}
              	System.out.println("<<<<<<<<<<<=====inventTotal: "+inventTotal+"  frompreviousBal: "+frompreviousBal+"   tonewBal: "+topreviousBal);
              	if(inventTotal.equalsIgnoreCase("0")){
              		System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 0");
              		System.out.println("<<<<<<<<<<<<<compCode: "+compCode+"  itemCode: "+itemCode+"   newwarehouseCode: "+newwarehouseCode+"  quantity: "+Integer.parseInt(quantity)+"  userid: "+Integer.parseInt(userId)+"  branchId: "+Integer.parseInt(newbranchId));
              		approverManager.createInventoryTotal(compCode, itemCode, newwarehouseCode,  Integer.parseInt(quantity), Integer.parseInt(userId),Integer.parseInt(newbranchId));
              	System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 1");
              	serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",Integer.parseInt(quantity),newwarehouseCode,Integer.parseInt(userId),topreviousBal,compCode);
              	System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 2");
              	} 
        		else{approverManager.updateInventoryTotal(compCode, itemCode, newwarehouseCode,  Integer.parseInt(quantity),Integer.parseInt(newbranchId));
        			serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",Integer.parseInt(quantity),newwarehouseCode,Integer.parseInt(userId),topreviousBal,compCode);
        			}	
              	System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 3");
              	approverManager.reductionOfInventoryTotal(compCode, itemCode, oldwarehouseCode,  Integer.parseInt(quantity),Integer.parseInt(oldbranchId));
              	System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 4");
              	serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",(Integer.parseInt(quantity)*-1),oldwarehouseCode,Integer.parseInt(userId),frompreviousBal,compCode);
              	System.out.println("<<<<<<<<<<<<<<MAGBEL TECHNOLOGY 5");           
                 }
                d=ps.executeBatch();

            } catch (Exception ex) {
                System.out.println("BulkUpdateManager: BulkAssetTransfer()>>>>>" + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }
                return (d.length > 0);
        }
 

        public int insertBulkTransferRequisition(ArrayList list,int bacthId,String Project,String reqnuserId,int userBranch,int department_id, String sbuCode,int usersectionId,String transferHeading) {
            boolean re = false;
            int assetCode = 0;
            String query = "insert into am_ad_TransferRequisition ( " +
                    "ASSET_ID,Description,NEWASSET_USER," +
                    "BATCH_ID,TRANSFERBY_ID,QUANTITY,TRANSFER_DATE,ASSET_CODE,PROJECT_CODE,REQNCOLLECTOR,REQUESTBRANCH,RFID_TAG,DEPT_ID,SBU_CODE,REQUESTSECT_ID,UNIT_PRICE,REQUEST_HEADING)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            //ResultSet rs = null;
            magma.StockRecordsBean bd = null;
            int[] d = null;
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
    //            System.out.println("<<<<<<<<<<<<<< bacthId insertBulkTransferRequisition: "+bacthId);
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.StockRecordsBean) list.get(i);
                    String asset_id = bd.getAsset_id();
                    String description = bd.getDescription();
                    String newassetuser = bd.getNewuser();
                    String quantitydec = bd.getCost_price();
                    String rfidtag = bd.getBar_code();
                    double unitPrice = bd.getUnitPrice();
                    System.out.println("<<<<<<<<<<<<<< quantitydec insertBulkTransferRequisition: "+quantitydec);
                    int quantity = Double.valueOf(quantitydec).intValue();
                    assetCode = bd.getAssetCode();
                    int transferby = bd.getUser_id() == null ? 0 : Integer.parseInt(bd.getUser_id());
                    if (description == null || description.equals("")) {
                        description = "";
                    }  
                    if (newassetuser == null || newassetuser.equals("")) {
                    	newassetuser = "0";
                    }
                    if (asset_id == null || asset_id.equals("")) {
                        asset_id = "0";
                    }

                    ps.setString(1, asset_id);
                    ps.setString(2, description);
                    ps.setString(3, newassetuser);
                    ps.setString(4, String.valueOf(bacthId));
                    ps.setInt(5, transferby);
                    ps.setInt(6, quantity);
                    ps.setTimestamp(7, dbConnection.getDateTime(new java.util.Date()));
                    ps.setInt(8, assetCode);
                    ps.setString(9, Project);
                    ps.setString(10, reqnuserId);
                    ps.setInt(11, userBranch);
                    ps.setString(12, rfidtag);
                    ps.setInt(13, department_id);
                    ps.setString(14, sbuCode);
                    ps.setInt(15, usersectionId);
                    ps.setDouble(16, unitPrice);
                    ps.setString(17,transferHeading);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                //System.out.println("Executed Successfully ");


            } catch (Exception ex) {
                System.out.println("Error insertBulkTransferRequisition() of am_ad_TransferRequisition -> " + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }

            return assetCode;
        }


        public int insertBulkTransferRequisitionApproval(String bacthId,String userID,String supervisorID,String header,String reqnBranch) {
            boolean re = false;
            int assetCode = 0;
            boolean result = false;
            int[] d = null;
            String []supervisorlist = supervisorID.split("#");
//            System.out.println("<<<<<<<<<<<<<< supervisorID insertBulkTransferRequisitionApproval: "+supervisorID);
            int No = supervisorlist.length;
            String query = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
            		",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
            		",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            //ResultSet rs = null;
            String status="PENDING";
    //        String mtid = helper.getGeneratedId("am_asset_approval");
//            System.out.println("<<<<<<<<<<<<<< bacthId insertBulkTransferRequisitionApproval: "+bacthId+"    No: "+No);
   		 String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Bulk Transfer Requisition'";
   		 int var =Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
 //  		 	System.out.println("<<<<<<<<<<<<<< var insertBulkTransferRequisitionApproval: "+var);
            cg = new CodeGenerator();
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                String reqnID = cg.generateCode("REQUISITION", "", "", "");
                for(int j=0;j<No;j++){
//                System.out.println("<<<<<<<<<<<<<< supervisorlist insertBulkTransferRequisitionApproval: "+supervisorlist[j]);
	            ps = con.prepareStatement(query);
	            ps.setString(1, reqnID);
	            ps.setString(2, userID);
	            ps.setString(3, supervisorlist[j]);
	            ps.setTimestamp(4,  dbConnection.getDateTime(new java.util.Date()));
	            ps.setString(5, header);
	            ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
	            ps.setString(7, reqnBranch);
	            ps.setString(8, status);
	            ps.setString(9, "Bulk Transfer Requisition");
	            ps.setString(10, "P");
	            ps.setString(11, timer.format(new java.util.Date()));
	            ps.setString(12, bacthId);
	            ps.setString(13, bacthId);
	            ps.setInt(14, var);         
               // d = ps.executeBatch();
                
	            result = (ps.executeUpdate() == -1);
//	            System.out.println("<<<<<<<<<<<<<< result insertBulkTransferRequisitionApproval: "+result);
            }
                for(int j=0;j<No;j++){
        			String subjectr ="SCRN  Approval";
        			String msgText11 ="SCRN Transactions with Group ID: "+ bacthId +" is waiting for your approval.";
        			String otherparam = "bulkStockTransferReqApproval&id="+bacthId+"&tranId="+bacthId+"&transaction_level=1&approval_level_count=0";				
        			mail.sendMailSupervisor(supervisorlist[j], subjectr, msgText11,otherparam);	
        }			
                
            } catch (Exception ex) {
                System.out.println("Error insertBulkTransferRequisitionApproval() of am_ad_TransferRequisition -> " + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }

            return assetCode;
        }

        public int insertBulkStoreTransferRequisitionApproval(String bacthId,String userID,String supervisorID,String header,String reqnBranch) {
            boolean re = false;
            int assetCode = 0;
            boolean result = false;
            int[] d = null;
//            String []supervisorlist = supervisorID.split("#");
            System.out.println("<<<<<<<<<<<<<< supervisorID insertBulkStoreTransferRequisitionApproval: "+supervisorID+"  bacthId: "+bacthId+"  userID: "+userID+"  header: "+header+"  reqnBranch: "+reqnBranch);
//            int No = supervisorlist.length;
            String query = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
            		",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
            		",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Connection con = null;
            PreparedStatement ps = null;
            //ResultSet rs = null;
            String status="PENDING";
    //        String mtid = helper.getGeneratedId("am_asset_approval");
//            System.out.println("<<<<<<<<<<<<<< bacthId insertBulkStoreTransferRequisitionApproval: "+bacthId+"    No: "+No);
   		 String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Stock Transfer Requisition'";
   		 int var =Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
   		 	System.out.println("<<<<<<<<<<<<<< var insertBulkStoreTransferRequisitionApproval: "+var);
            cg = new CodeGenerator();
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
 //               String reqnID = cg.generateCode("REQUISITION", "", "", "");
//                for(int j=0;j<No;j++){
//                System.out.println("<<<<<<<<<<<<<< supervisorlist insertBulkStoreTransferRequisitionApproval: "+supervisorlist[j]);
	            ps = con.prepareStatement(query);
	            ps.setString(1, bacthId);
	            ps.setString(2, userID);
	            ps.setString(3, supervisorID);
	            ps.setTimestamp(4,  dbConnection.getDateTime(new java.util.Date()));
	            ps.setString(5, header);
	            ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
	            ps.setString(7, reqnBranch);
	            ps.setString(8, status);
	            ps.setString(9, "Stock Transfer Requisition");
	            ps.setString(10, "P");
	            ps.setString(11, timer.format(new java.util.Date()));
	            ps.setString(12, bacthId);
	            ps.setString(13, bacthId);
	            ps.setInt(14, var);         
               // d = ps.executeBatch();
                
	            result = (ps.executeUpdate() == -1);
	            System.out.println("<<<<<<<<<<<<<< result insertBulkStoreTransferRequisitionApproval: "+result);
//            }
//                for(int j=0;j<No;j++){
        			String subjectr ="SCRN  Approval";
        			String msgText11 ="SCRN Transactions with Group ID: "+ bacthId +" is waiting for your approval.";
        			String otherparam = "bulkStockTransferReqApproval&id="+bacthId+"&tranId="+bacthId+"&transaction_level=1&approval_level_count=0";				
        			mail.sendMailSupervisor(supervisorID, subjectr, msgText11,otherparam);	
//        }			
                
            } catch (Exception ex) {
                System.out.println("Error insertBulkStoreTransferRequisitionApproval() of am_asset_approval -> " + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }

            return assetCode;
        }


        public ArrayList[] findStockTransferaprovalByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_ad_TransferRequisition where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

                Asset aset = null;
                while (rs.next()) {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    int quantity = rs.getInt("QUANTITY");
                   // String newassetuser = rs.getString("NEWASSET_USER");
                    String projectCode = rs.getString("PROJECT_CODE");
                    String newassetuser = rs.getString("REQNCOLLECTOR");
                    double unitPrice = rs.getDouble("UNIT_PRICE");
                    
                     aset = new Asset();
                    aset.setAssetId(id);
                    aset.setDescription(description);
                    aset.setAssetUser(newassetuser);
                    aset.setQuantity(quantity);
                    aset.setProjectCode(projectCode);
                    aset.setUnitPrice(unitPrice);
                    listNew.add(aset);
//                    aset=null;
//                    assetFilter = assetFilter + "'" + id + "',";

                } 
/*
                boolean index = assetFilter.endsWith(",");
            //    System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
              //      System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findStockByID(assetFilter);
*/
            } catch (Exception e) {
                System.out.println("INFO:Error findStockTransferaprovalByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
  //          listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList[] findStockTransferAcceptByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and old_asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkTransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

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
            //    System.out.println("findAssetTransferByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
              //      System.out.println("findAssetTransferByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findStockByIDforAcceptance(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findStockTransferAcceptByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList findStockByIDforAcceptance(String queryFilter) {



            String selectQuery =
                    "SELECT ASSET_ID,REGISTRATION_NO," +
                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model," +
                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                    "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'PENDING' OR ASSET_STATUS='APPROVED') " + queryFilter;
            System.out.println("the query in findStockByIDforAcceptance is <<<<<<<<<<<<< " + selectQuery);

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;

            ArrayList listOld = new ArrayList();

            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);

                rs = ps.executeQuery();
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
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
            System.out.println("the size of listOld is >>>>>>> " + listOld.size());
            return listOld;

        }

        public int insertBulkStockTransfer(ArrayList list, int bacthId,int categoryId,String newassetId) {
            boolean re = false;
            int assetCode = 0;
            String query = "insert into am_gb_bulkStocktransfer ( " +
                    "ASSET_ID,Description,OLDBRANCH_ID," +
                    "OLDDEPT_ID,OLDSBU_CODE," +
                    "OLDSECTION_ID,OLDASSET_USER," +
                    "NEWBRANCH_ID,NEWDEPT_ID,NEWSBU_CODE,NEWSECTION_ID,NEWASSET_USER," +
                    "BATCH_ID,TRANSFERBY_ID,CATEGORY_ID,NEW_ASSET_ID,COST_PRICE,ACCUM_DEP,MONTHLY_DEP,NBV,TRANSFER_DATE,ASSET_CODE)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            Connection con = null;
            PreparedStatement ps = null;
            //ResultSet rs = null;
            magma.StockRecordsBean bd = null;
            int[] d = null;
            try {
                con = getConnection();
                ps = con.prepareStatement(query);

                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.StockRecordsBean) list.get(i);
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
                    System.out.println("insertBulkStockTransfer branchId: "+branchId);
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
                System.out.println("Error insertBulkStockTransfer() of BulkStockUpdateManager -> " + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }

            return assetCode;
        }
        

        public int insertBulkStockforIssueance(ArrayList list, int bacthId,int categoryId,String newassetId) {
            boolean re = false;
            int assetCode = 0;
            String query = "insert into am_gb_bulkStocktransfer ( " +
                    "ASSET_ID,Description,OLDBRANCH_ID," +
                    "OLDDEPT_ID,OLDSBU_CODE," +
                    "OLDSECTION_ID,OLDASSET_USER," +
                    "NEWBRANCH_ID,NEWDEPT_ID,NEWSBU_CODE,NEWSECTION_ID,NEWASSET_USER," +
                    "BATCH_ID,TRANSFERBY_ID,CATEGORY_ID,NEW_ASSET_ID,COST_PRICE,ACCUM_DEP,"+
                    "MONTHLY_DEP,NBV,TRANSFER_DATE,ASSET_CODE,QUANTITY_REQUEST,QUANTITY_ISSUED,QUANTITY_REMAIN,QUANTITY_AVAILABLE,PROJECT_CODE,ITEMTYPE,ISSUANCE_STATUS)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            Connection con = null;
            PreparedStatement ps = null;
            //ResultSet rs = null;
            magma.StockRecordsBean bd = null;
            int[] d = null;
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
//                System.out.println("insertBulkStockTransfer newassetId: "+newassetId+"   categoryId:"+categoryId+"   bacthId: "+bacthId);
                for (int i = 0; i < list.size(); i++) {
                    bd = (magma.StockRecordsBean) list.get(i);
                    int issuanceStatus = bd.getIssuanceStatus();
                    String asset_id = bd.getAsset_id();
//                    System.out.println("insertBulkStockTransfer asset_id: "+asset_id);
                    newassetId = bd.getNewasset_id();
//                    System.out.println("insertBulkStockTransfer newassetId: "+newassetId);
                    String description = bd.getDescription();
 //                   System.out.println("insertBulkStockTransfer description: "+description);
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
//                    System.out.println("<<<<<assetCode====: "+assetCode);
                    int quantityIssued = bd.getQuantity();
//                    System.out.println("<<<<<quantityIssued====: "+quantityIssued);
                    int qtyIssued = Integer.parseInt(bd.getQuantityIssue());
//                    System.out.println("<<<<<quantityIssued====: "+quantityIssued+"   qtyIssued: "+qtyIssued);
                    String projectCode = bd.getProjectCode();
                    int quantityRequested = Integer.parseInt(bd.getRequestedQuantity());
 //                   System.out.println("<<<<<quantityRequested====: "+quantityRequested);
                    int quantityAvailable = bd.getQuantity();
//                    System.out.println("insertBulkStockTransfer quantityAvailable: "+quantityAvailable);
                    int category_Id = Integer.parseInt(bd.getCategory_id());
                    String monthly_dep = bd.getMonthlydep();
                    String itemType = bd.getItemType();
//                    System.out.println("<<<<<itemType====: "+itemType);
                    int transferby = bd.getUser_id() == null ? 0 : Integer.parseInt(bd.getUser_id());
//                    System.out.println("<<<<<transferby====: "+transferby);
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
                    if (newbranchId == null || newbranchId.equals("")) {
                        newbranchId = "0";
                    }
                    if (itemType == null || itemType.equals("")) {
                    	itemType = "0";
                    }
//                    System.out.println("insertBulkStockTransfer branchId: "+branchId);
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
                    ps.setInt(23, quantityRequested);
                    ps.setInt(24, qtyIssued);
                    ps.setInt(25, quantityAvailable-qtyIssued);
                    ps.setInt(26, quantityAvailable);
                    ps.setString(27, projectCode);
                    ps.setString(28, itemType);
                    ps.setInt(29,issuanceStatus);
                    ps.addBatch();
                }
                d = ps.executeBatch();
                System.out.println("Executed Successfully "+d);

            } catch (Exception ex) {
                System.out.println("Error insertBulkStockforIssueance() of BulkStockUpdateManager -> " + ex);
            } finally {
                dbConnection.closeConnection(con, ps);
            }

            return assetCode;
        }
        

        public ArrayList[] findStockIssuanceByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkStocktransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

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
                    String receiveDept = rs.getString("RECEIVING_DEPT");
                    String receiveBranch = rs.getString("RECEIVING_BRANCH");
                    String projectCode = rs.getString("PROJECT_CODE");
                    String receivedBy = rs.getString("RECEIVED_BY");
                    int assetCode =  rs.getInt("ASSET_CODE");
                    int qtyRequested =  rs.getInt("QUANTITY_REQUEST");
                    int qtyAvailable =  rs.getInt("QUANTITY_AVAILABLE");
                    int qtyRemain =  rs.getInt("QUANTITY_REMAIN");
                    int qtyIssued =  rs.getInt("QUANTITY_ISSUED");
                    String costPrice = rs.getString("COST_PRICE");
                    
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
                    aset.setQtyRequested(qtyRequested);
                    aset.setQtyAvailable(qtyAvailable);
                    aset.setQtyRemain(qtyRemain);
                    aset.setQtyIssued(qtyIssued);
                    aset.setReceiveBranch(receiveBranch);
                    aset.setReceiveDept(receiveDept);
                    aset.setProjectCode(projectCode);
                    aset.setReceivedBy(receivedBy);
                    aset.setCost_price(costPrice);
                    listNew.add(aset);
                    aset=null;
                    assetFilter = assetFilter + "'" + id + "',";

                } 

                boolean index = assetFilter.endsWith(",");
            //    System.out.println("findStockIssuanceByBatchId index: "+index);
                if (index) {
                    assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
 //                   System.out.println("findStockIssuanceByBatchId assetFilter: "+assetFilter);
                }
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findStockByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findStockIssuanceByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }

        public ArrayList[] findBulkStockTransferAcceptByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery = "select * from am_gb_bulkStocktransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

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
                    String receiveDept = rs.getString("RECEIVING_DEPT");
                    String receiveBranch = rs.getString("RECEIVING_BRANCH");
                    String projectCode = rs.getString("PROJECT_CODE");
                    String receivedBy = rs.getString("RECEIVED_BY");
                    String costPrice =  rs.getString("COST_PRICE");
                    int qtyRequested =  rs.getInt("QUANTITY_REQUEST");
                    int qtyAvailable =  rs.getInt("QUANTITY_AVAILABLE");
                    int qtyRemain =  rs.getInt("QUANTITY_REMAIN");
                    int qtyIssued =  rs.getInt("QUANTITY_ISSUED");
                    double unitPrice =  rs.getDouble("UNIT_PRICE");
                    
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
                    aset.setQtyRequested(qtyRequested);
                    aset.setQtyAvailable(qtyAvailable);
                    aset.setQtyRemain(qtyRemain);
                    aset.setQtyIssued(qtyIssued);
                    aset.setReceiveBranch(receiveBranch);
                    aset.setReceiveDept(receiveDept);
                    aset.setProjectCode(projectCode);
                    aset.setReceivedBy(receivedBy);  
                    aset.setCost_price(costPrice);
                    aset.setUnitPrice(unitPrice);
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
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findBulkStockByIDforAcceptance(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findBulkStockTransferAcceptByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public ArrayList findBulkStockByIDforAcceptance(String queryFilter) {



            String selectQuery =
                    "SELECT ASSET_ID,REGISTRATION_NO," +
                    " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model," +
                    "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
                    "SBU_CODE,Spare_1,Spare_2,Spare_3,Spare_4,Spare_5,Spare_6,BAR_CODE,branch_id,DEPT_CODE,DEPT_ID,sub_category_ID,sub_category_code " +
                    "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL and ASSET_STATUS = 'ACTIVE' " + queryFilter;
            System.out.println("the query in findStockByIDforAcceptance is <<<<<<<<<<<<< " + selectQuery);

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;

            ArrayList listOld = new ArrayList();

            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);

                rs = ps.executeQuery();
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
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
            System.out.println("the size of listOld is >>>>>>> " + listOld.size());
            return listOld;

        }


        public ArrayList findStocksByBatchId(String tranId) {


            //String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkStocktransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;

            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
           // ArrayList listOld = new ArrayList();
           // ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
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
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
//            System.out.println("the size of listNew is >>>>>> " + listNew.size());
            return listNew;

        }

        public ArrayList[] findStockIssuanceDisplayByBatchId(String tranId) {

        //	System.out.println("findStockTransferByBatchId tranId: "+tranId);
            String assetFilter = " and asset_id in (";
            String selectQuery =
                    "select * from am_gb_bulkStocktransfer where batch_id=? ";

            Connection con = null;
            PreparedStatement ps = null;
            System.out.println("=======>selectQuery: "+selectQuery);
            ResultSet rs = null;
            ArrayList listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            ArrayList[] listNewOld = new ArrayList[2];
            try {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();

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
                    int qtyIssued = rs.getInt("QUANTITY_ISSUED");
                    int issuanceStatus = rs.getInt("ISSUANCE_STATUS");
//                    System.out.println("<<<<<<=======issuanceStatus: "+issuanceStatus);
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
                    aset.setQtyIssued(qtyIssued);
                    aset.setIssuanceStatus(issuanceStatus);
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
                assetFilter = assetFilter + ")";
            //    System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
                listOld = findStockByID(assetFilter);

            } catch (Exception e) {
                System.out.println("INFO:Error findStockIssuanceDisplayByBatchId() in BulkUpdateManager-> " +
                        e.getMessage());
            } finally {

                dbConnection.closeConnection(con, ps, rs);
            }
  //          System.out.println("the size of listNew is >>>>>> " + listNew.size());
            listNewOld[0] = listNew;
            listNewOld[1] = listOld;
            Asset assNew = null;
            Asset assOld = null;

            return listNewOld;

        }


        public int insertStockAdjustmentRequisitionApproval(String bacthId, String userID, String supervisorID, String header, String reqnBranch)
        {
            int assetCode;
            String query;
            Connection con;
            PreparedStatement ps;
            String status;
            int var;
            boolean re = false;
            assetCode = 0;
            boolean result = false;
            int d[] = null;
            System.out.println((new StringBuilder("<<<<<<<<<<<<<< supervisorID insertStockAdjustmentRequisitionApproval: ")).append(supervisorID).append("  bacthId: ").append(bacthId).append("  userID: ").append(userID).append("  header: ").append(header).append("  reqnBranch: ").append(reqnBranch).toString());
            query = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
    ",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
    ",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
    ;
            con = null;
            ps = null;
            status = "PENDING";
            String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Stock Transfer Re" +
    "quisition'"
    ;
            var = Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
            System.out.println((new StringBuilder("<<<<<<<<<<<<<< var insertStockAdjustmentRequisitionApproval: ")).append(var).toString());
            cg = new CodeGenerator();
            try
            {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps = con.prepareStatement(query);
                ps.setString(1, bacthId);
                ps.setString(2, userID);
                ps.setString(3, supervisorID);
                ps.setTimestamp(4, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(5, header);
                ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(7, reqnBranch);
                ps.setString(8, status);
                ps.setString(9, "Stock Adjustment");
                ps.setString(10, "P");
                ps.setString(11, timer.format(new java.util.Date()));
                ps.setString(12, bacthId);
                ps.setString(13, bacthId);
                ps.setInt(14, var);
                result = ps.executeUpdate() == -1;
                System.out.println((new StringBuilder("<<<<<<<<<<<<<< result insertStockAdjustmentRequisitionApproval: ")).append(result).toString());
                String subjectr = "Stock Ad  Approval";
                String msgText11 = (new StringBuilder("Stock Adjustment with Group ID: ")).append(bacthId).append(" is waiting for your approval.").toString();
                String otherparam = (new StringBuilder("StockAdjustmentApproval&id=")).append(bacthId).append("&tranId=").append(bacthId).append("&transaction_level=1&approval_level_count=0").toString();
                mail.sendMailSupervisor(supervisorID, subjectr, msgText11, otherparam);
            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("Error insertStockAdjustmentRequisitionApproval() of am_asset_approval -> ")).append(ex).toString());
            }
            dbConnection.closeConnection(con, ps);

            return assetCode;
        }

        public int insertStockAdjustment(ArrayList list, String batchId)
        {
            int assetCode;
            String query;
            Connection con;
            PreparedStatement ps;
            boolean re = false;
            assetCode = 0;
            query = "insert into ST_STOCKADJUSTMENT ( ASSET_ID,Description,QTY_AVAILABLE,QTY_ADJUSTED" +
    ",DEBIT_CREDIT,CREATE_DATE,WAREHOUSE_CODE,ITEM_CODE,BATCH_ID,REASON,UNIT_PRICE) values(?,?,?,?,?,?,?,?," +
    "?,?,?)"
    ;
            con = null;
            ps = null;
            try
            {
                con = getConnection();
                ps = con.prepareStatement(query);
                for(int i = 0; i < list.size(); i++)
                {
                    StockRecordsBean bd = (StockRecordsBean)list.get(i);
                    String asset_id = bd.getAsset_id();
                    String description = bd.getDescription();
                    double unitPrice = bd.getUnitPrice();
                    int qtyAvailable = bd.getQtyAvailable();
                    int qtyAdjusted = bd.getQtyIssued();
                    String crdr = bd.getSpare_1();
                    String reason = bd.getReason();
                    String warehouseCode = bd.getWarehouseCode();
                    String itemCode = bd.getItemCode();
                    assetCode = bd.getAssetCode();
                    int transferby = bd.getUser_id() != null ? Integer.parseInt(bd.getUser_id()) : 0;
                    if(description == null || description.equals(""))
                    {
                        description = "";
                    }
                    if(asset_id == null || asset_id.equals(""))
                    {
                        asset_id = "0";
                    }
                    ps.setString(1, asset_id);
                    ps.setString(2, description);
                    ps.setInt(3, qtyAvailable);
                    ps.setInt(4, qtyAdjusted);
                    ps.setString(5, crdr);
                    ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
                    ps.setString(7, warehouseCode);
                    ps.setString(8, itemCode);
                    ps.setString(9, batchId);
                    ps.setString(10, reason);
                    ps.setDouble(11, unitPrice);
                    ps.addBatch();
                }

                int d[] = ps.executeBatch();
            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("Error insertStockAdjustment()  -> ")).append(ex).toString());
            }
            dbConnection.closeConnection(con, ps);

            return assetCode;
        }

        public int insertStockAdjustmentApproval(String bacthId, String userID, String supervisorID, String header, String reqnBranch)
        {
            int assetCode;
            String query;
            Connection con;
            PreparedStatement ps;
            String status;
            int var;
            boolean re = false;
            assetCode = 0;
            boolean result = false;
            int d[] = null;
            query = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description" +
    ",effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time" +
    ",transaction_id,batch_id,transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
    ;
            con = null;
            ps = null;
            status = "PENDING";
            String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Stock Adjustment'";
            var = Integer.parseInt(applHelper.descCode(adm_Approv_Lvl_Qry));
            cg = new CodeGenerator();
            try
            {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps = con.prepareStatement(query);
                ps.setString(1, bacthId);
                ps.setString(2, userID);
                ps.setString(3, supervisorID);
                ps.setTimestamp(4, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(5, header);
                ps.setTimestamp(6, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(7, reqnBranch);
                ps.setString(8, status);
                ps.setString(9, "Stock Adjustment");
                ps.setString(10, "P");
                ps.setString(11, timer.format(new java.util.Date()));
                ps.setString(12, bacthId);
                ps.setString(13, bacthId);
                ps.setInt(14, var);
                result = ps.executeUpdate() == -1;
                String subjectr = "Stock Adjustment  Approval";
                String msgText11 = (new StringBuilder("Stock Adjustment Transactions with Group ID: ")).append(bacthId).append(" is waiting for your approval.").toString();
                String otherparam = (new StringBuilder("StockAdjustmentApproval&id=")).append(bacthId).append("&tranId=").append(bacthId).append("&transaction_level=1&approval_level_count=0").toString();
                mail.sendMailSupervisor(supervisorID, subjectr, msgText11, otherparam);
            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("Error insertStockAdjustmentApproval() of am_asset_approval -> ")).append(ex).toString());
            }
            dbConnection.closeConnection(con, ps);
            return assetCode;
        }
/*
        public boolean updateBulkStockAdjustment(ArrayList list, String compCode, String userId)
        {
            Connection con;
            PreparedStatement ps;
            PreparedStatement ps1;
            int d[];
            String query;
            ArrayList newList = null;
            con = null;
            ps = null;
            ps1 = null;
            htmlUtil = new HtmlUtility();
            d = null;
            boolean result = false;
            query = "UPDATE ST_STOCK SET QUANTITY= QUANTITY + ? WHERE ASSET_ID=?";
            String total_query = "UPDATE ST_INVENTORY_TOTALS SET BALANCE= BALANCE + ? WHERE ITEM_CODE = ? AND WARE" +
    "HOUSE_CODE = ?"
    ;
            System.out.println((new StringBuilder("I am in bulk Stock Adjustm is >>>>>>>>>>> ")).append(list.size()).toString());
            try
            {
                con = dbConnection.getConnection("legendPlus");
                for(int i = 0; i < list.size(); i++)
                {
                    StockRecordsBean stok = (StockRecordsBean)list.get(i);
                    String assetId = stok.getAsset_id();
                    String description = stok.getDescription();
                    int qtyAvailable = stok.getQtyAvailable();
                    int qtyAdjusted = stok.getQtyIssued();
                    String crdr = stok.getSpare_1();
                    String warehouseCode = stok.getWarehouseCode();
                    String itemCode = stok.getItemCode();
                    stok = null;
                    if(crdr.equalsIgnoreCase("DEBIT"))
                    {
                        qtyAdjusted *= -1;
                    }
                    System.out.println((new StringBuilder("Balance in bulk Stock Adjustm is >>>>>>>>>>> ")).append(qtyAdjusted).toString());
                    ps = con.prepareStatement(query);
                    ps.setInt(1, qtyAdjusted);
                    ps.setString(2, assetId);
                    ps.execute();
                    
                    ps1.setInt(1, qtyAdjusted);
                    ps1.setString(2, itemCode);
                    ps1.setString(3, warehouseCode);
                    ps1.execute();
                    result = true;
                }

            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("BulkStockUpdateManager: updateBulkStockAdjustment()>>>>>")).append(ex).toString());
            }
            dbConnection.closeConnection(con, ps);
            return result;
        }  */

        public boolean updateBulkStockAdjustment(ArrayList list, String compCode, String userId) {
        String query = "";
       
       String total_query = "UPDATE ST_INVENTORY_TOTALS SET BALANCE= BALANCE + ?,TMP_BALANCE= coalesce(TMP_BALANCE,0) + ? WHERE ITEM_CODE = ? AND WARE" +
    "HOUSE_CODE = ?"
    ;
            System.out.println((new StringBuilder("I am in bulk Stock Adjustm is >>>>>>>>>>> ")).append(list.size()).toString());

            Connection con = null;
            PreparedStatement ps = null;    
            PreparedStatement ps1 = null;
            boolean done = false;
            try {
                con = dbConnection.getConnection("legendPlus");
                for(int i = 0; i < list.size(); i++)
                {
                    StockRecordsBean stok = (StockRecordsBean)list.get(i);
                    String assetId = stok.getAsset_id();
                    String description = stok.getDescription();
                    double unitPrice = stok.getUnitPrice();
                    int qtyAvailable = stok.getQtyAvailable();
                    int qtyAdjusted = stok.getQtyIssued();
                    int totalQuantity = qtyAvailable+qtyAdjusted;
                    double newBalance = unitPrice*totalQuantity;
                    System.out.println("Balance in Stock Adjustm is >>>> "+qtyAdjusted+"  assetId: "+assetId+"   totalQuantity: "+totalQuantity+"  newBalance: "+newBalance+"   unitPrice: "+unitPrice);
                    String crdr = stok.getSpare_1();
                    String warehouseCode = stok.getWarehouseCode();
                    String itemCode = stok.getItemCode();
                    if(unitPrice>0){query = "UPDATE ST_STOCK SET QUANTITY= QUANTITY + ?,UNIT_PRICE = ?,COST_PRICE = ? WHERE ASSET_ID = ?";}
                    else{query = "UPDATE ST_STOCK SET QUANTITY= QUANTITY + ?,UNIT_PRICE = ?,COST_PRICE = ? WHERE ASSET_ID = ?";}
                    stok = null;
                    if(crdr.equalsIgnoreCase("DEBIT"))
                    {
                        qtyAdjusted *= -1;
                    }
                    System.out.println("Balance in bulk Stock Adjustm is >>>>>>>>>>> "+qtyAdjusted+"  assetId: "+assetId);
                    ps = con.prepareStatement(query);
                    ps.setInt(1, qtyAdjusted);
                    ps.setDouble(2, unitPrice);
                    ps.setDouble(3, newBalance);
                    ps.setString(4, assetId);
                    ps.execute();
                    ps1 = con.prepareStatement(total_query);
                    ps1.setInt(1, qtyAdjusted);
                    ps1.setInt(2, qtyAdjusted);
                    ps1.setString(3, itemCode);
                    ps1.setString(4, warehouseCode);
                    System.out.println("Update ST_INVENTORY_TOTALS in bulk Stock Adjustm is >>>>>>>>>>> "+qtyAdjusted+"  itemCode: "+itemCode+"    warehouseCode: "+warehouseCode);
                    ps1.execute();
                    done = true;
                }


            } catch (Exception ex) {
                done = false;
                System.out.println((new StringBuilder("BulkStockUpdateManager: updateBulkStockAdjustment()>>>>>")).append(ex).toString());
            } finally {
                dbConnection.closeConnection(con, ps);
                dbConnection.closeConnection(con, ps1);
            }
            return done;   
        }
        
        public ArrayList[] findStockAdjustByBatchId(String tranId)
        {
            String selectQuery;
            Connection con;
            PreparedStatement ps;
            ResultSet rs;
            ArrayList listNew;
            ArrayList listNewOld[];
            selectQuery = "select * from ST_STOCKADJUSTMENT where batch_id=? AND STATUS IS NULL ";
            con = null;
            ps = null;
            rs = null;
            listNew = new ArrayList();
            ArrayList listOld = new ArrayList();
            listNewOld = new ArrayList[2];
            try
            {
                con = dbConnection.getConnection("legendPlus");
                ps = con.prepareStatement(selectQuery);
                ps.setInt(1, Integer.parseInt(tranId));
                rs = ps.executeQuery();
                StockRecordsBean stok = null;
                for(; rs.next(); listNew.add(stok))
                {
                    String id = rs.getString("ASSET_ID");
                    String description = rs.getString("DESCRIPTION");
                    int qtyAdjust = rs.getInt("QTY_ADJUSTED");
                    int qtyAvailable = rs.getInt("QTY_AVAILABLE");
                    double unitPrice = rs.getDouble("UNIT_PRICE");
//                    System.out.println("<<<<<<<======unitPrice: "+unitPrice);
                    String drcr = rs.getString("DEBIT_CREDIT");
                    String itemCode = rs.getString("ITEM_CODE");
                    String warehouseCode = rs.getString("WAREHOUSE_CODE");
                    String reason = rs.getString("REASON");
                    stok = new StockRecordsBean();
                    stok.setAsset_id(id);
                    stok.setDescription(description);
                    stok.setQtyIssued(qtyAdjust);
                    stok.setQtyAvailable(qtyAvailable);
                    stok.setSpare_1(drcr);
                    stok.setItemCode(itemCode);
                    stok.setWarehouseCode(warehouseCode);
                    stok.setReason(reason);
                    stok.setUnitPrice(unitPrice);
                }

            }
            catch(Exception e)
            {
                System.out.println((new StringBuilder("INFO:Error findStockAdjustByBatchId() in BulkUpdateManager-> ")).append(e.getMessage()).toString());
            }
            dbConnection.closeConnection(con, ps, rs);

            listNewOld[0] = listNew;
            Asset assNew = null;
            Asset assOld = null;
            return listNewOld;
        }

        public int insertStockAdjustmentTransaction(ArrayList list, String batchId, String recordNo)
        {
            int assetCode;
            String query;
            Connection con;
            PreparedStatement ps;
            boolean re = false;
            assetCode = 0;
            int result = 0;
            query = "insert into ST_INVENTORY_ADJUSTMT (MTID, COMP_CODE,ITEM_CODE,WAREHOUSE_CODE,DESCRI" +
    "PTION,QUANTITY,USERID,TRANS_DATE,ADJUST_OPTION,REASON,UNIT_PRICE) values(?,?,?,?,?,?,?,?,?,?,?)"
    ;
            con = null;
            ps = null; 

            try
            {
                con = getConnection();
                ps = con.prepareStatement(query);
                for(int i = 0; i < list.size(); i++)
                {
                    StockRecordsBean bd = (StockRecordsBean)list.get(i);
                    String id = bd.getSpare_2();
                    String asset_id = bd.getAsset_id();
                    String description = bd.getDescription();
                    double unitPrice = bd.getUnitPrice();
                    int qtyAvailable = bd.getQtyAvailable();
                    int qtyAdjusted = bd.getQtyIssued();
                    String crdr = bd.getSpare_1();
                    if(crdr.equalsIgnoreCase("D")){crdr = "Debit";}
                    if(crdr.equalsIgnoreCase("C")){crdr = "Credit";}
                    String warehouseCode = bd.getWarehouseCode();
                    String itemCode = bd.getItemCode();
                    String compCode = bd.getCompCode();
                    assetCode = bd.getAssetCode();
                    String reason = bd.getReason();
                    String mtid = apph.getGeneratedId("ST_INVENTORY_ADJUSTMT");
                    System.out.println("crdr in insertStockAdjustmentTransaction: "+crdr);
                    int transferby = bd.getUser_id() != null ? Integer.parseInt(bd.getUser_id()) : 0;
                    if(description == null || description.equals(""))
                    {
                        description = "";
                    }
                    if(asset_id == null || asset_id.equals(""))
                    {
                        asset_id = "0";
                    }
                    if(crdr.equalsIgnoreCase("Debit"))
                    {
                        crdr = "REMOVE";
                    } else
                    {
                        crdr = "ADD";
                    }
                    ps.setString(1, mtid);
                    ps.setString(2, compCode);
                    ps.setString(3, itemCode);
                    ps.setString(4, warehouseCode);
                    ps.setString(5, description);
                    ps.setInt(6, qtyAdjusted);
                    System.out.println("qtyAdjusted in insertStockAdjustmentTransaction: "+qtyAdjusted);
                    ps.setInt(7, transferby);
                    ps.setTimestamp(8, dbConnection.getDateTime(new java.util.Date()));
                    ps.setString(9, crdr);
                    ps.setString(10, reason);
                    ps.setDouble(11, unitPrice);
                    ps.addBatch(); 
                }

                int d[] = ps.executeBatch();
                result = 1;
            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("Error insertStockAdjustmentTransaction()  -> ")).append(ex).toString());
            }
            dbConnection.closeConnection(con, ps);

            return result;
        }

  
}
