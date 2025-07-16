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
import com.magbel.util.DatetimeFormat;
import java.util.ArrayList;
import magma.net.vao.Asset;

public class BulkUpdateUncapitalisedManager extends legend.ConnectionClass {

    private MagmaDBConnection dbConnection;
    private DatetimeFormat dateFormat;
    private java.text.SimpleDateFormat sdf;

    public BulkUpdateUncapitalisedManager() throws Exception {

        try {
            freeResource();
            sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            dbConnection = new MagmaDBConnection();
            dateFormat = new DatetimeFormat();

        } catch (Exception ex) {
        }
    }

    public boolean insertBulkUpdate(ArrayList list, int bacthId) {
        boolean re = false;

        String query = "insert into am_gb_UncapitalisedbulkUpdate ( " +
                "REGISTRATION_NO,Description,VENDOR_AC," +
                "ASSET_MODEL,ASSET_SERIAL_NO," +
                "ASSET_ENGINE_NO,SUPPLIER_NAME," +
                "ASSET_USER,ASSET_MAINTENANCE," +
                "AUTHORIZED_BY,PURCHASE_REASON,SBU_CODE," +
                "SPARE_1,SPARE_2,BAR_CODE, ASSET_ID,BATCH_ID,branch_id )" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.UncapitalisedRecordsBean bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.UncapitalisedRecordsBean) list.get(i);

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
                String spare1 = bd.getSpare_1();
                String spare2 = bd.getSpare_2();
                String barcode = bd.getBar_code();
                String lpo1 = bd.getLpo();
                String asset_id1 = bd.getAsset_id();
                String branchId = bd.getBranch_id();

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
                if (spare1 == null || spare1.equals("")) {
                    spare1 = "0";
                }
                if (spare2 == null || spare2.equals("")) {
                    spare2 = "0";
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
                ps.setString(15, barcode);
                ps.setString(16, asset_id1);
                ps.setInt(17, bacthId);
                ps.setString(18, branchId);


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
                "select * from am_gb_UncapitalisedbulkUpdate where batch_id=? ";

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
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);

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
            listOld = findAssetByID(assetFilter);

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
        /*
        for(int i=0; i< listNewOld.length;++i){
          assNew =    (Asset) listNew.get(i);
          assOld =    (Asset) listOld.get(i);
            System.out.println("the content of new asset sis DDDDDDDDDDDD "+assNew.getId() );
             System.out.println("the content of old asset sis DDDDDDDDDDDD "+assOld.getId() );
        }
        */
        return listNewOld;

    }

    public ArrayList findAssetByID(String queryFilter) {



        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO," +
                " DESCRIPTION,ASSET_USER,ASSET_MAINTENANCE,Vendor_AC,Asset_Model," +
                "Asset_Serial_No,Asset_Engine_No,Supplier_Name,Authorized_By,Purchase_Reason," +
                "SBU_CODE,Spare_1,Spare_2,BAR_CODE,branch_id " +
                "FROM AM_ASSET_UNCAPITALIZED  WHERE ASSET_ID IS NOT NULL and (ASSET_STATUS = 'ACTIVE' OR ASSET_STATUS='APPROVED') " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< " + selectQuery);

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
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String barCode = rs.getString("BAR_CODE");
                String branchId = String.valueOf(rs.getInt("branch_id"));

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
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);

                listOld.add(aset);
                aset = null;

            }



        } catch (Exception e) {
            System.out.println("INFO:Error findAssetByID() in BulkUpdaUncapialisedteManager-> " +
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
        int[] d =null;
        String query= "UPDATE AM_ASSET_UNCAPITALIZED SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,BAR_CODE=? WHERE ASSET_ID=?" ;

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
                String barCode = asset.getBarCode();
                 System.out.println("the asset id in bulk update manager is >>>>>>>>>>> " + assetId);
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
             ps.setString(15,barCode);
             ps.setString(16,assetId);

            // ps.addBatch();
            ps.execute();
             }
            d=ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("BulkUpdateManager: updateBulkUncapitaliseAsset()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
            return (d.length > 0);
    }


    public ArrayList findAssetsByBatchId(String tranId) {


        //String assetFilter = " and asset_id in (";
        String selectQuery =
                "select * from am_gb_UncapitalisedbulkUpdate where batch_id=? ";

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
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
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
                aset.setBarCode(barCode);
                aset.setBranchId(branchId);

                listNew.add(aset);
                aset=null;
               // assetFilter = assetFilter + "'" + id + "',";

            }
            /*
            boolean index = assetFilter.endsWith(",");
            if (index) {
                assetFilter = assetFilter.substring(0, assetFilter.length() - 1);
            }
            assetFilter = assetFilter + ")";
            System.out.println("the filter that will be sent to FleetHistoryManager is >>>>> " + assetFilter);
            listOld = findAssetByID(assetFilter);
            */
        } catch (Exception e) {
            System.out.println("INFO:Error findAssetsByBatchId() in BulkUpdateUncapitalisedManager-> " +
                    e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }
        System.out.println("the size of listNew is >>>>>> " + listNew.size());
        /*
        listNewOld[0] = listNew;
        listNewOld[1] = listOld;
        Asset assNew = null;
        Asset assOld = null;

        for(int i=0; i< listNewOld.length;++i){
          assNew =    (Asset) listNew.get(i);
          assOld =    (Asset) listOld.get(i);
            System.out.println("the content of new asset sis DDDDDDDDDDDD "+assNew.getId() );
             System.out.println("the content of old asset sis DDDDDDDDDDDD "+assOld.getId() );
        }
        */
        return listNew;

    }

    /*

    public boolean updateBulkAsset(ArrayList list) {

        ArrayList newList = null;
        Asset asset = null;
        Connection con = null;
        PreparedStatement ps = null;
        int[] d =null;
        String query= "UPDATE AM_ASSET SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,BAR_CODE=? WHERE ASSET_ID=?" ;

        try {
              con.setAutoCommit(false) ;
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
                String barCode = asset.getBarCode();
                 System.out.println("the asset id in bulk update manager is >>>>>>>>>>> " + assetId);
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
             ps.setString(15,barCode);
             ps.setString(16,assetId);

            ps.addBatch();
           // ps.execute();
             }
            //d=ps.executeBatch();
             int[] numOfUpdates = ps.executeBatch();
             d = numOfUpdates;

             System.out.println("%%%%%%%%%%% the value of numOfUpdates ="+numOfUpdates.length);
             System.out.println("%%%%%%%%%% the value of d ="+d.length);

             for(int i = 0; i<numOfUpdates.length ; ++i){
             if(numOfUpdates[i]== -2 ){
                 System.out.println("%%%%%%%%%%%%%%%%%% Execution "+i+" unknown numbers of rows executed.");
             }
             else{
                 System.out.println("%%%%%%%%%%%%%%%%%% the number "+i+ " successful "+numOfUpdates[i]+" rows updated.");
             }
             con.commit();
             }
        } catch (Exception ex) {
            System.out.println("BulkUpdateManager: updateBulkAsset()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
            return (d.length > 0);
    }

*/


        public double[] findMinMaxAssetCost(String assetId, double minAssetCost, double maxAssetCost) {
        double costPrice = 0.0;
        double[] result = new double[2];
        String selectQuery =  "select cost_price from AM_ASSET_UNCAPITALIZED where asset_id=?";

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


}
