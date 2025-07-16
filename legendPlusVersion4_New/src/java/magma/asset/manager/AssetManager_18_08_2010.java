package magma.asset.manager;

import magma.CurrentDateTime;
import magma.net.dao.MagmaDBConnection;

import java.sql.*;
import java.util.*;

import magma.asset.dto.*;
import magma.util.Codes;

import com.magbel.util.ApplicationHelper;

public class AssetManager_18_08_2010 extends MagmaDBConnection {
    private Codes code;

    public AssetManager_18_08_2010() {
        System.out.println("USING magma.asset.manager.AssetManager");
        code = new Codes();
    }

    public ArrayList getAsset() {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        Asset _obj = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depRate = rs.getDouble("DEP_RATE");
                String make = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double monthDep = rs.getDouble("MONTHLY_DEP");
                double costPrice = rs.getDouble("COST_PRICE");
                String depEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double NBV = rs.getDouble("NBV");
                //String effDate = rs.getString("EFFECTIVE_DATE");
                String vendorAcct = rs.getString("VENDOR_AC");
                String model = rs.getString("ASSET_MODEL");
                String engineNo = rs.getString("ASSET_ENGINE_NO");
                String email1 = rs.getString("EMAIL1");
                String email2 = rs.getString("EMAIL2");
                //String vendorName = rs.getString("VENDOR_NAME");
                int regionId = rs.getInt("REGION_ID");
                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");

                _obj = new Asset(assetId, regNo, branchId, branchName, deptId,
                                 deptName, sectionId, sectionName,
                                 categoryId, categoryName, regionId, regionName,
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);

                list.add(_obj);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList getAssetByQuery(String query) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE " + query;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        Asset _obj = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depRate = rs.getDouble("DEP_RATE");
                String make = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double monthDep = rs.getDouble("MONTHLY_DEP");
                double costPrice = rs.getDouble("COST_PRICE");
                String depEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double NBV = rs.getDouble("NBV");
                //String effDate = rs.getString("EFFECTIVE_DATE");
                String vendorAcct = rs.getString("VENDOR_AC");
                String model = rs.getString("ASSET_MODEL");
                String engineNo = rs.getString("ASSET_ENGINE_NO");
                String email1 = rs.getString("EMAIL1");
                String email2 = rs.getString("EMAIL2");
                //String vendorName = rs.getString("VENDOR_NAME");
                int regionId = rs.getInt("REGION_ID");
                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");

                _obj = new Asset(assetId, regNo, branchId, branchName, deptId,
                                 deptName, sectionId, sectionName,
                                 categoryId, categoryName, regionId, regionName,
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);

                list.add(_obj);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset by Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public Asset getAsset(String assetId) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_ID = '" +
                             assetId + "' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depRate = rs.getDouble("DEP_RATE");
                String make = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double monthDep = rs.getDouble("MONTHLY_DEP");
                double costPrice = rs.getDouble("COST_PRICE");
                String depEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double NBV = rs.getDouble("NBV");
                //String effDate = rs.getString("EFFECTIVE_DATE");
                String vendorAcct = rs.getString("VENDOR_AC");
                String model = rs.getString("ASSET_MODEL");
                String engineNo = rs.getString("ASSET_ENGINE_NO");
                String email1 = rs.getString("EMAIL1");
                String email2 = rs.getString("EMAIL2");
                //String vendorName = rs.getString("VENDOR_NAME");
                int regionId = rs.getInt("REGION_ID");
                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");
               // int wht_percent = rs.getInt("WHT_PERCENT");

                _obj = new Asset(assetId, regNo, branchId, branchName, deptId,
                                 deptName, sectionId, sectionName,
                                 categoryId, categoryName, regionId, regionName,
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);

              //  _obj.setWht_percent(wht_percent);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset BY ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return _obj;

    }

     public Asset getOldAssetDetails(String assetId,String newAssetId)
     {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset assetObj = new Asset();

        String selOldDetails_qry =
        "select b.old_dept_id,b.old_branch_id,b.old_asset_user,b.old_section,b.old_branch_code," +
        "b.old_section_code,b.old_dept_code,a.cost_price,a.description,a.registration_no," +
        "a.who_to_rem,a.email1,a.who_to_rem_2,a.email2,"+
        "a.NBV,a.Accum_Dep,b.old_cat_id from am_wip_reclassification b, am_asset a where b.asset_id='"+assetId+"'" +
        " and a.asset_id='"+newAssetId+"'";

       // System.out.println("selOldDetails_qry >>>> " + selOldDetails_qry);

         try
         {

            con = getConnection("fixedasset");
            ps = con.prepareStatement(selOldDetails_qry);
            rs = ps.executeQuery();

            while (rs.next())
            {
                assetObj.setDeptId(rs.getInt("OLD_DEPT_ID"));
                assetObj.setBranchId(rs.getInt("OLD_BRANCH_ID"));
                assetObj.setAssetUser(rs.getString("OLD_ASSET_USER"));
                assetObj.setSectionId(rs.getInt("OLD_SECTION"));
                assetObj.setCost(rs.getDouble("COST_PRICE"));
                assetObj.setDescription(rs.getString("DESCRIPTION"));
                assetObj.setRegNo(rs.getString("REGISTRATION_NO"));
                assetObj.setNbv(rs.getDouble("NBV"));
                assetObj.setAccumDep(rs.getDouble("ACCUM_DEP"));
                assetObj.setCategoryId(rs.getInt("old_cat_id"));
                assetObj.setWhoToRem1(rs.getString("who_to_rem"));
                assetObj.setWhoToRem2(rs.getString("who_to_rem_2"));
                assetObj.setEmail1(rs.getString("email1"));
                assetObj.setEmail2(rs.getString("email2"));
            }
         }

        catch (Exception e)
        {
            System.out.println("INFO:Error fetching OldAssetDetails BY ID ->" +   e.getMessage());
        }
        finally
        {
            closeConnection(con, ps, rs);
        }

        return assetObj;

     }
    //methods for disposal
    public int insertAssetDisposal(String assetId, String reason,
                                   String buyerAcct,
                                   double disposalAmount, String raiseEntry,
                                   double profitLoss,
                                   String disposalDate, String effDate,
                                   int userId,String supervise) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CurrentDateTime cdt = new CurrentDateTime();
        int i = 0;

        String updateFleetMaster =
                "UPDATE FT_FLEET_MASTER SET STATUS=? WHERE ASSET_ID = ?";
        String updateQuery =
                "UPDATE AM_ASSET SET ASSET_STATUS=?,DATE_DISPOSED = ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETDISPOSAL(ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT," +
                "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setString(2, reason);
            ps.setString(3, buyerAcct);
            ps.setDouble(4, disposalAmount);
            ps.setString(5, raiseEntry);
            ps.setDouble(6, profitLoss);
            ps.setDate(7, dateConvert(disposalDate));
            ps.setDate(8, dateConvert(effDate));
            ps.setInt(9, userId);
			ps.setString(10, new ApplicationHelper()
					.getGeneratedId("AM_ASSETDISPOSAL"));
            ps.setString(11,supervise);
            ps.setString(12, "P");

            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, "PENDING");
                ps.setDate(2, dateConvert(disposalDate));
                ps.setString(3, assetId);
                i = ps.executeUpdate();
                //update fleet master
                ps = con.prepareStatement(updateFleetMaster);
                ps.setString(1, "PENDING");
                ps.setString(2, assetId);
                i = ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Inserting Disposal Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }

    public int updateAssetDisposal(String assetId, String reason,
                                   String buyerAcct,
                                   String raiseEntry, int userId) {
        // RaiseEntryManager re = new RaiseEntryManager();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery =
                "UPDATE AM_ASSETDISPOSAL SET DISPOSAL_REASON = ?,BUYER_ACCOUNT = ?," +
                "USER_ID = ? WHERE ASSET_ID = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, reason);
            ps.setString(2, buyerAcct);
            //ps.setString(3,raiseEntry);
            ps.setInt(3, userId);
            ps.setString(4, assetId);

            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Disposal Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }

    //update if entry has not been raised
    public int updateAssetDisposal(String assetId, String reason,
                                   String buyerAcct,
                                   double disposalAmount, String raiseEntry,
                                   double profitLoss,
                                   String disposalDate, String effDate,
                                   int userId) {
        // RaiseEntryManager re = new RaiseEntryManager();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery =
                "UPDATE AM_ASSETDISPOSAL SET USER_ID = ?,DISPOSAL_REASON = ?,BUYER_ACCOUNT = ?," +
                "DISPOSAL_AMOUNT = ?,RAISE_ENTRY = ?,PROFIT_LOSS = ?, DISPOSAL_DATE = ?," +
                "EFFECTIVE_DATE = ? WHERE ASSET_ID = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, userId);
            ps.setString(2, reason);
            ps.setString(3, buyerAcct);
            ps.setDouble(4, disposalAmount);
            ps.setString(5, raiseEntry);
            ps.setDouble(6, profitLoss);
            ps.setDate(7, dateConvert(disposalDate));
            ps.setDate(8, dateConvert(effDate));
            ps.setString(9, assetId);

            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Disposal Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }

    public Disposal getDisposedAsset(String id) {

        String query = "SELECT DISPOSAL_ID,ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT,DISPOSAL_AMOUNT,RAISE_ENTRY," +
                       "PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,DISPOSAL_STATUS " +
                       "FROM AM_ASSETDISPOSAL WHERE ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Disposal dispose = null;
        //DataConnect connect = new DataConnect();
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String disposalId = rs.getString("DISPOSAL_ID");
                String assetId = rs.getString("ASSET_ID");
                String reason = rs.getString("DISPOSAL_REASON");
                String buyerAcct = rs.getString("BUYER_ACCOUNT");
                double amount = rs.getDouble("DISPOSAL_AMOUNT");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double profitLoss = rs.getDouble("PROFIT_LOSS");
                String disposalDate = formatDate(rs.getDate("DISPOSAL_DATE"));
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String userId = rs.getString("USER_ID");
                String status = rs.getString("DISPOSAL_STATUS");

                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status, "", "", "", "",
                                       "", 0, "", "");

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching disposed Asset by ID->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return dispose;
    }

    public Disposal getDisposedAssetByQuery(String query_) {

        String query = "SELECT DISPOSAL_ID,ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT,DSIPOSAL_AMOUNT,RAISE_ENTRY," +
                       "PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,DISPOSAL_STATUS " +
                       "FROM AM_ASSETDISPOSAL WHERE " + query_;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Disposal dispose = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String disposalId = rs.getString("DISPOSAL_ID");
                String assetId = rs.getString("ASSET_ID");
                String reason = rs.getString("DISPOSAL_REASON");
                String buyerAcct = rs.getString("BUYER_ACCOUNT");
                double amount = rs.getDouble("DISPOSAL_AMOUNT");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double profitLoss = rs.getDouble("PROFIT_LOSS");
                String disposalDate = formatDate(rs.getDate("DISPOSAL_DATE"));
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String userId = rs.getString("USER_ID");
                String status = rs.getString("DISPOSAL_STATUS");

                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status, "", "", "", "",
                                       "", 0, "", "");

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching disposed Asset by query->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return dispose;
    }

    //get all disposed asset
    public ArrayList getDisposedAssetList(String query_) {

        String query = "SELECT A.DISPOSAL_ID,A.ASSET_ID,A.DISPOSAL_REASON,A.BUYER_ACCOUNT,A.DISPOSAL_AMOUNT,A.RAISE_ENTRY," +
                       " A.PROFIT_LOSS,A.DISPOSAL_DATE,A.EFFECTIVE_DATE,A.USER_ID,A.DISPOSAL_STATUS,B.BRANCH_ID,B.DEPT_ID," +
                       " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.COST_PRICE,B.ASSET_USER,B.ASSET_STATUS" +
                       " FROM AM_ASSETDISPOSAL A,AM_ASSET_MAIN B WHERE A.ASSET_ID = B.ASSET_ID " +
                       query_;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Disposal dispose = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String disposalId = rs.getString("DISPOSAL_ID");
                String assetId = rs.getString("ASSET_ID");
                String reason = rs.getString("DISPOSAL_REASON");
                String buyerAcct = rs.getString("BUYER_ACCOUNT");
                double amount = rs.getDouble("DISPOSAL_AMOUNT");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double profitLoss = rs.getDouble("PROFIT_LOSS");
                String disposalDate = formatDate(rs.getDate("DISPOSAL_DATE"));
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String userId = rs.getString("USER_ID");
                String status = rs.getString("DISPOSAL_STATUS");
                String branchId = rs.getString("BRANCH_ID");
                String deptId = rs.getString("DEPT_ID");
                String categoryId = rs.getString("CATEGORY_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                String desc = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");

                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status,
                                       branchId, deptId, categoryId, regNo,
                                       desc, cost, assetUser, assetStatus);

                list.add(dispose);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching All disposed Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    //get tranfered asset
    public ArrayList getTransferedAssetList(String query_) {

        String query = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                       " A.OLD_SECTION,D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO," +
                       " E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.ASSET_ID " + query_;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");
                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                //String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                //String categoryId = rs.getString("CATEGORY_ID");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");

//                transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId, sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost, "",
//                                        regNo, effDate, categoryId, assetUser,
//                                        assetStatus);
                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        "", regNo, effDate,
                        categoryId, "", "","");
                list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    public String getObjectName(String query) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception ex) {
            System.out.println("Error executing SQL Code ->\n" + query + "\n" +
                               ex);
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;
    }

    //get transfered Asset by ID
    public Transfer getTransferedAsset(String id) {

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                       " A.OLD_SECTION,D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.OLD_ASSET_ID AND A.ASSET_ID = '" + id +
                       "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");
                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                //String assetUser = rs.getString("ASSET_USER");
                //String assetStatus = rs.getString("ASSET_STATUS");

//                transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId, sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost,
//                                        datePurchased, regNo, effDate,
//                                        categoryId, "", "");
                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        "", regNo, effDate,
                        categoryId, "", "","");
                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }

    //insert asset transfer
    public int insertAssetTransfer(String assetId, int oldDept, int newDept,
                                   int oldBranch,
                                   int newBranch, String oldUser,
                                   String newUser, int oldSection,
                                   int newSection,
                                   String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2, String effDate,String mtid) {
        int i = 0;

        /*
        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + newBranch +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";

        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newDept +
                             "',BRANCH_ID = '" + newBranch + "'," +
                             "SECTION_ID = '" + newSection +
                             "',ASSET_USER = '" + newUser +
                             "',WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";

        */
        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
            ps.setString(20,mtid);


            i = ps.executeUpdate();

            /*
            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }
            */
        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAssetTransfer() :Error inserting Asset Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


     public int insertWipReclassificationWithSupervisor
             (
               String assetId, int oldDept, int newDept,int oldBranch,int newBranch, String oldUser,
               String newUser, int oldSection,int newSection,String raiseEntry, String transferDate,
               int userId, String whoToRem1,String email1, String whoToRem2,
               String email2, String effDate,String mtid,String newcat_id,String old_cat_id,String Approval_Status,
               String old_email1,String old_email2,String old_who_rem1,String old_who_rem2
               )
     {
        int i = 0;


        String insertQuery = "INSERT INTO AM_WIP_RECLASSIFICATION(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID," +
                             "NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                             ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CAT_ID,NEW_CAT_ID,OLD_CAT_CODE," +
                             "NEW_CAT_CODE,NEW_WHO_TO_REM,NEW_EMAIL1,NEW_WHO_TO_REM2,NEW_EMAIL2,Approval_Status," +
                             "old_who_to_rem,old_email1,old_who_to_rem2,old_email2)" +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
	    ps.setString(20, mtid);
            ps.setString(21, old_cat_id);
            ps.setString(22, newcat_id);
            ps.setString(23, code.getCategoryCode(String.valueOf(old_cat_id)));
            ps.setString(24, code.getCategoryCode(String.valueOf(newcat_id)));
            ps.setString(25, whoToRem1);
            ps.setString(26, email1);
            ps.setString(27, whoToRem2);
            ps.setString(28, email2);
            ps.setString(29, Approval_Status);
            ps.setString(30, old_who_rem1);
            ps.setString(31, old_email1);
            ps.setString(32, old_who_rem2);
            ps.setString(33, old_email2);

            i = ps.executeUpdate();

            /*
            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }
            */
        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAssetWIPTransfer():->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

    //update if entry has been raised
    public int updateAssetTransfer(String assetId, int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2) {
        int i = 0;

        String updateQuery = "UPDATE AM_ASSET SET WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 + "',WHO_TO_REM_2 = '" +
                             whoToRem2 + "',EMAIL2 = '" + email2 +
                             "' WHERE ASSET_ID = '" + assetId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            i = ps.executeUpdate();
        } catch (Exception e) {
            String warning = "WARNING:Error updating Asset Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

    public void deleteAssetTransfer(String id) {
        String query = "DELETE FROM AM_ASSETTRANSFER WHERE ASSET_ID = '" + id +
                       "'";
        excuteSQLCode(query);
    }

    private void excuteSQLCode(String sqlCode) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(sqlCode);
            ps.execute();
        } catch (Exception ex) {
            System.out.println("Error executing SQL Code ->\n" + sqlCode + "\n" +
                               ex);
        } finally {
            closeConnection(con, ps);
        }

    }

    public void deleteAssetRevalue(String id) {
        String query = "DELETE FROM AM_ASSETREVALUE WHERE ASSET_ID = '" + id +
                       "'";
        excuteSQLCode(query);
    }

    public int insertAssetRevalue(double cost, double nbv, String assetId,
                                  double costIncrease, String revalueReason,
                                  String revalueDate,
                                  int userId, double vatableCost, double vatAmt,
                                  double whtAmt,
                                  String vendorAcct, String raiseEntry,
                                  double accumDep,
                                  double oldCost, double oldVatableCost,
                                  double oldVatAmt, double oldWhtAmt,
                                  double oldNbv, double oldAccumDep,
                                  String effDate,String mtid) {

        //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in insert asset revalue KKKKKKKKKKKKKKKKKK");
        //String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                          //   " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection("fixedasset");

            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, mtid);

            i = ps.executeUpdate();

            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

    //UPDATE ASSET REVALUE
    public int updateAssetRevalue(String assetId, int userId, String reason,
                                  String vendorAcct, String raiseEntry) {

        //nbv += cost;
        //costPrice += cost;
        int i = 0;

        String updateQuery = "UPDATE AM_ASSETREVALUE SET USER_ID = ?," +
                             "REVALUE_REASON = ?, R_VENDOR_AC = ? WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection("fixedasset");

            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, userId);
            ps.setString(2, reason);
            ps.setString(3, vendorAcct);
            // ps.setString(4,raiseEntry);
            ps.setString(4, assetId);

            i = ps.executeUpdate();

        } catch (Exception e) {
            String warning = "WARNING:Error updating Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

    //get revalued asset by ID
    public Revaluation getRevaluedAsset(String id) {

        String query =
                "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM AM_ASSETREVALUE " +
                "WHERE ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Revaluation revalue = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int revalueId = rs.getInt("REVALUE_ID");
                String assetId = rs.getString("ASSET_ID");
                double costIncrease = rs.getDouble("COST_INCREASE");
                String reason = rs.getString("REVALUE_REASON");
                String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                int userId = rs.getInt("USER_ID");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String revVendorAcct = rs.getString("R_VENDOR_AC");
                double cost = rs.getDouble("COST_PRICE");
                double vatableCost = rs.getDouble("VATABLE_COST");
                double vatAmt = rs.getDouble("VAT_AMOUNT");
                double whtAmt = rs.getDouble("WHT_AMOUNT");
                double nbv = rs.getDouble("NBV");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double oldCost = rs.getDouble("OLD_COST_PRICE");
                double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                double oldNbv = rs.getDouble("OLD_NBV");
                double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                String effDate = formatDate(rs.getDate("EFFDATE"));

                revalue = new Revaluation(revalueId, assetId, costIncrease,
                                          reason, revalueDate,
                                          userId, raiseEntry, revVendorAcct,
                                          cost, vatableCost, vatAmt, whtAmt,
                                          nbv, accumDep,
                                          oldCost, oldVatableCost, oldVatAmt,
                                          oldWhtAmt, oldNbv, oldAccumDep,
                                          effDate, 0, 0, 0, "", "", "", "");

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Revalued Asset by ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return revalue;
    }


    public ArrayList getRevaluedAssetList(String query_) {

        String query = "SELECT A.REVALUE_ID,A.ASSET_ID,A.COST_INCREASE,A.REVALUE_REASON,A.REVALUE_DATE," +
                       " A.USER_ID,A.RAISE_ENTRY,A.R_VENDOR_AC,A.COST_PRICE,A.VATABLE_COST,A.VAT_AMOUNT," +
                       " A.WHT_AMOUNT,A.NBV,A.ACCUM_DEP,A.OLD_COST_PRICE,A.OLD_VATABLE_COST,A.OLD_VAT_AMOUNT," +
                       " A.OLD_WHT_AMOUNT,A.OLD_NBV,A.OLD_ACCUM_DEP,A.EFFDATE,B.BRANCH_ID,B.DEPT_ID," +
                       " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS" +
                       " FROM AM_ASSETREVALUE A,AM_ASSET_MAIN B " +
                       " WHERE A.ASSET_ID = B.ASSET_ID " + query_;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Revaluation revalue = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int revalueId = rs.getInt("REVALUE_ID");
                String assetId = rs.getString("ASSET_ID");
                double costIncrease = rs.getDouble("COST_INCREASE");
                String reason = rs.getString("REVALUE_REASON");
                String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                int userId = rs.getInt("USER_ID");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String revVendorAcct = rs.getString("R_VENDOR_AC");
                double cost = rs.getDouble("COST_PRICE");
                double vatableCost = rs.getDouble("VATABLE_COST");
                double vatAmt = rs.getDouble("VAT_AMOUNT");
                double whtAmt = rs.getDouble("WHT_AMOUNT");
                double nbv = rs.getDouble("NBV");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double oldCost = rs.getDouble("OLD_COST_PRICE");
                double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                double oldNbv = rs.getDouble("OLD_NBV");
                double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                String effDate = formatDate(rs.getDate("EFFDATE"));
                int branchId = rs.getInt("BRANCH_ID");
                int deptId = rs.getInt("DEPT_ID");
                int categoryId = rs.getInt("CATEGORY_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                String desc = rs.getString("DESCRIPTION");
                //double cost = rs.getDouble("COST_PRICE");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");

                revalue = new Revaluation(revalueId, assetId, costIncrease,
                                          reason, revalueDate,
                                          userId, raiseEntry, revVendorAcct,
                                          cost, vatableCost, vatAmt, whtAmt,
                                          nbv, accumDep,
                                          oldCost, oldVatableCost, oldVatAmt,
                                          oldWhtAmt, oldNbv, oldAccumDep,
                                          effDate, branchId, deptId,
                                          categoryId, regNo, desc, assetUser,
                                          assetStatus);

                list.add(revalue);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Revalued Asset by Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }


    //Stolen Asset Methods
    public int insertAssetStolen(String assetId, String stolenDate,
                                 String location,
                                 String details, String policeStation,
                                 String policeDate, String insuranceCompany,
                                 String insuranceDate, String effDate,
                                 int userId, String raiseEntry, String status) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CurrentDateTime cdt = new CurrentDateTime();
        int i = 0;

        String updateFleetMaster =
                "UPDATE FT_FLEET_MASTER SET STATUS=? WHERE ASSET_ID = ?";
        String updateQuery =
                "UPDATE AM_ASSET SET ASSET_STATUS=? WHERE ASSET_ID = ?";

        String insertQuery = "INSERT INTO AM_ASSETSTOLEN(ASSET_ID,STOLEN_DATE,LOCATION,DETAILS,POLICE_STATION," +
                             "POLICE_NOTICE_DATE,INSURANCE_COMPANY,INSURANCE_NOTICE_DATE,EFFECTIVE_DATE,USER_ID," +
                             "RAISE_ENTRY,STATUS) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setDate(2, dateConvert(stolenDate));
            ps.setString(3, location);
            ps.setString(4, details);
            ps.setString(5, policeStation);
            ps.setDate(6, dateConvert(policeDate));
            ps.setString(7, insuranceCompany);
            ps.setDate(8, dateConvert(insuranceDate));
            ps.setDate(9, dateConvert(effDate));
            ps.setInt(10, userId);
            ps.setString(11, raiseEntry);
            ps.setString(12, status);

            //ps.setString(13,recoverLocation);

            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, status);
                ps.setString(2, assetId);
                i = ps.executeUpdate();
                //update fleet master
                ps = con.prepareStatement(updateFleetMaster);
                ps.setString(1, status);
                ps.setString(2, assetId);
                i = ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Inserting Stolen Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }

    //update stolen asset
    public int updateAssetStolen(String assetId, String stolenDate,
                                 String location,
                                 String details, String policeStation,
                                 String policeDate, String insuranceCompany,
                                 String insuranceDate, String effDate,
                                 int userId, String raiseEntry,
                                 String recoverDate, String recoverLocation,
                                 String recoverBy, String status) {
        String updateFleetMaster = "";
        String updateAsset = "";

        if (status.equalsIgnoreCase("R")) {
            updateFleetMaster =
                    "UPDATE FT_FLEET_MASTER SET STATUS=? WHERE ASSET_ID = ?";
            updateAsset =
                    "UPDATE AM_ASSET SET ASSET_STATUS=? WHERE ASSET_ID = ?";
        }

        int i = 0;

        String updateQuery = "UPDATE AM_ASSETSTOLEN SET STOLEN_DATE = ?,LOCATION = ?, DETAILS = ?,POLICE_STATION = ?,POLICE_NOTICE_DATE = ?," +
                             " INSURANCE_COMPANY = ?,INSURANCE_NOTICE_DATE = ?,EFFECTIVE_DATE = ?,USER_ID = ?,RAISE_ENTRY = ?," +
                             " RECOVER_DATE = ?, RECOVER_LOCATION = ?, RECOVER_BY = ?,STATUS = ? WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);

            ps.setDate(1, dateConvert(stolenDate));
            ps.setString(2, location);
            ps.setString(3, details);
            ps.setString(4, policeStation);
            ps.setDate(5, dateConvert(policeDate));
            ps.setString(6, insuranceCompany);
            ps.setDate(7, dateConvert(insuranceDate));
            ps.setDate(8, dateConvert(effDate));
            ps.setInt(9, userId);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(effDate));
            ps.setString(12, recoverLocation);
            ps.setString(13, recoverBy);
            ps.setString(14, status);
            ps.setString(15, assetId);

            i = ps.executeUpdate();
            if (i > 0 && status.equalsIgnoreCase("R")) {
                ps = con.prepareStatement(updateAsset);
                ps.setString(1, "ACTIVE");
                ps.setString(2, assetId);
                i = ps.executeUpdate();
                //update fleet master
                ps = con.prepareStatement(updateFleetMaster);
                ps.setString(1, "A");
                ps.setString(2, assetId);
                i = ps.executeUpdate();
            }

        } catch (Exception e) {
            String warning = "WARNING:Error updating Stolen Asset ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

    public Stolen getStolenAsset(String id) {

        String query =
                "SELECT STOLEN_ID,ASSET_ID,STOLEN_DATE,LOCATION,DETAILS,POLICE_STATION," +
                "POLICE_NOTICE_DATE,INSURANCE_COMPANY,INSURANCE_NOTICE_DATE,EFFECTIVE_DATE,USER_ID,RAISE_ENTRY," +
                "RECOVER_DATE,RECOVER_LOCATION,RECOVER_BY,STATUS " +
                "FROM AM_ASSETSTOLEN WHERE ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Stolen stolen = null;
        //DataConnect connect = new DataConnect();
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String stolenId = rs.getString("STOLEN_ID");
                String assetId = rs.getString("ASSET_ID");
                String stolenDate = formatDate(rs.getDate("STOLEN_DATE"));
                String location = rs.getString("LOCATION");
                String details = rs.getString("DETAILS");
                String policeStation = rs.getString("POLICE_STATION");
                String policeDate = formatDate(rs.getDate("POLICE_NOTICE_DATE"));
                String insuranceCompany = rs.getString("INSURANCE_COMPANY");
                String insuranceDate = formatDate(rs.getDate(
                        "INSURANCE_NOTICE_DATE"));
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId = rs.getInt("USER_ID");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String recoverDate = formatDate(rs.getDate("RECOVER_DATE"));
                String recoverLocation = rs.getString("RECOVER_LOCATION");
                String recoverBy = rs.getString("RECOVER_BY");
                String status = rs.getString("STATUS");

                stolen = new Stolen(stolenId, assetId, stolenDate, location,
                                    details, policeStation, policeDate,
                                    insuranceCompany, insuranceDate, effDate,
                                    userId, raiseEntry, 0, 0, 0, "", "", 0, "",
                                    "", recoverDate, recoverLocation, recoverBy,
                                    status);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Stolen Asset by ID->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return stolen;
    }

    //get all stolen asset
    public ArrayList getStolenAssetList(String query_) {

        String query = "SELECT A.STOLEN_ID,A.ASSET_ID,A.STOLEN_DATE,A.LOCATION,A.DETAILS,A.POLICE_STATION," +
                       "A.POLICE_NOTICE_DATE,A.INSURANCE_COMPANY,A.INSURANCE_NOTICE_DATE,A.EFFECTIVE_DATE,A.USER_ID,A.RAISE_ENTRY, " +
                       "A.RECOVER_DATE,A.RECOVER_LOCATION,A.RECOVER_BY,A.STATUS,B.BRANCH_ID,B.DEPT_ID,B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.COST_PRICE,B.ASSET_USER,B.ASSET_STATUS " +
                       "FROM AM_ASSETSTOLEN A,AM_ASSET_MAIN B WHERE A.ASSET_ID = B.ASSET_ID " +
                       query_;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Stolen stolen = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String stolenId = rs.getString("STOLEN_ID");
                String assetId = rs.getString("ASSET_ID");
                String stolenDate = formatDate(rs.getDate("STOLEN_DATE"));
                String location = rs.getString("LOCATION");
                String details = rs.getString("DETAILS");
                String policeStation = rs.getString("POLICE_STATION");
                String policeDate = formatDate(rs.getDate("POLICE_NOTICE_DATE"));
                String insuranceCompany = rs.getString("INSURANCE_COMPANY");
                String insuranceDate = formatDate(rs.getDate(
                        "INSURANCE_NOTICE_DATE"));
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                int userId = rs.getInt("USER_ID");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                int branchId = rs.getInt("BRANCH_ID");
                int deptId = rs.getInt("DEPT_ID");
                int categoryId = rs.getInt("CATEGORY_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                String desc = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");
                String recoverDate = formatDate(rs.getDate("RECOVER_DATE"));
                String recoverLocation = rs.getString("RECOVER_LOCATION");
                String recoverBy = rs.getString("RECOVER_BY");
                String status = rs.getString("STATUS");

                stolen = new Stolen(stolenId, assetId, stolenDate, location,
                                    details, policeStation, policeDate,
                                    insuranceCompany, insuranceDate, effDate,
                                    userId, raiseEntry, branchId, deptId,
                                    categoryId,
                                    regNo, desc, cost, assetUser, assetStatus,
                                    recoverDate, recoverLocation, recoverBy,
                                    status);

                list.add(stolen);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching All Stolen Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    public void recoverStolenAsset(String[] tranId) {
        if (tranId != null) {
            for (int x = 0; x < tranId.length; x++) {
                recoverStolenAsset(tranId[x]);
            }
        }

    }

    public void recoverStolenAsset(String tranId) {
        String UPDATE_QUERY =
                "UPDATE AM_ASSET SET ASSET_STATUS = ? WHERE ASSET_ID = ?";
        String status = "ACTIVE";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(UPDATE_QUERY);

            ps.setString(1, status);
            ps.setString(2, tranId);
            ps.execute();

        } catch (Exception e) {
            String warning = "WARNING:Error Reover Stolen Asset " +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            closeConnection(con, ps);
        }

    }
//  get dep. adjusted asset by ID
    public DepAdjustment getDepAdjustment(String id)
    {

     String query = "SELECT ID,ASSET_ID,ACCUM_DEP,NEW_ACCUM_DEP,NBV,NEW_NBV,REASON,ADJUST_DATE,"+
                      "USER_ID,RAISE_ENTRY,EFFDATE FROM AM_ASSET_DEP_ADJUSTMENT "+
                      "WHERE ASSET_ID = '"+id+"'";


       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

       ArrayList list = new ArrayList();
       //declare DTO object
       DepAdjustment adjust = null;

       try
       {
        con = getConnection("fixedasset");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next())
        {
         int adjustId = rs.getInt("ID");
         String assetId = rs.getString("ASSET_ID");
         double accumDep = rs.getDouble("ACCUM_DEP");
         double newAccumDep = rs.getDouble("NEW_ACCUM_DEP");
         String adjustDate = formatDate(rs.getDate("ADJUST_DATE"));
         int userId = rs.getInt("USER_ID");
         String raiseEntry = rs.getString("RAISE_ENTRY");
         String reason = rs.getString("REASON");
         double nbv = rs.getDouble("NBV");
         double newNbv = rs.getDouble("NEW_NBV");
         String effDate = formatDate(rs.getDate("EFFDATE"));

//         adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
//                      raiseEntry,adjustDate,effDate,userId,0,0,0,"","","","");
         adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
                 raiseEntry,adjustDate,effDate,userId,0,0,0,"","","","","");
         }

        }catch (Exception e) {
              System.out.println("INFO:Error fetching Depreciation Adjustment Asset by ID ->" +
                                 e.getMessage());
         } finally {
              closeConnection(con, ps, rs);
         }

         return adjust;
      }

    public void deleteAssetDepAdjustment(String id)
     {
      String query = "DELETE FROM AM_ASSET_DEP_ADJUSTMENT WHERE ASSET_ID = '"+id+"'";
      excuteSQLCode(query);
     }
    /*public boolean updateAssetDepAdjustment(String assetId,double accumDep,double newAccumDep,double nbv,
  		                                 double newNbv,String reason,String adjustDate,String effDate,int userId,String raiseEntry)
    {

  		String updateQuery1 = "UPDATE AM_ASSET SET ACCUM_DEP = ACCUM_DEP - ?,"+
  			                    " NBV = ?, DEP_YTD = DEP_YTD - ? WHERE ASSET_ID = ?";

  		String insertQuery2 = "UPDATE AM_ASSET_DEP_ADJUSTMENT SET ACCUM_DEP = ?,NEW_ACCUM_DEP = ?,NBV = ?,NEW_NBV = ?,"+
                            "REASON=?,ADJUST_DATE=?,EFFDATE=?,USER_ID=?,RAISE_ENTRY=? WHERE ASSET_ID = ?";

  		Connection con = null;
  		PreparedStatement ps = null;
      boolean result = false;
      boolean autoCommit = false;

  		try
  		{
  		 con = getConnection("fixedasset");
       autoCommit = con.getAutoCommit();
       con.setAutoCommit(false);
  		 ps = con.prepareStatement(insertQuery);
  		 ps.setString(1,assetId);
  		 ps.setDouble(2,accumDep);
       ps.setDouble(3,newAccumDep);
       ps.setDouble(4,nbv);
       ps.setDouble(5,newNbv);
  		 ps.setString(6,reason);
  		 ps.setDate(7,dateConvert(adjustDate));
       ps.setDate(8,dateConvert(effDate));
  		 ps.setInt(9,userId);
       ps.setString(10,raiseEntry);

       int i = ps.executeUpdate();

       ps = con.prepareStatement(updateQuery);
  		 ps.setDouble(1,accumDep);
  		 ps.setDouble(2,newNbv);
       ps.setDouble(3,accumDep);
       ps.setString(4,assetId);

  		 int k = ps.executeUpdate();

       if(((i == 1) || (i == 2)) && ((k == 1) || (k == 2)))
       {
         //con.rollback();
         con.commit();
         con.setAutoCommit(autoCommit);
         result = true;
       }

  		}
  		catch(Exception e)
  		{
  		 String warning = "WARNING:Error Updating Asset Depr. Adjustment ->" + e.getMessage();
  		 System.out.println(warning);
  		 e.printStackTrace();
  		}
  		finally
  		{
  		 closeConnection(con, ps);
  		}

  		return result;
  	}
    */
    //UPDATE ASSET DEP ADJUSTMENT
    public boolean updateAssetDepAdjustment(String assetId,int userId,String reason,String raiseEntry)
    {

  		int i = 0;
      boolean result = false;

  		String updateQuery = "UPDATE AM_ASSET_DEP_ADJUSTMENT SET USER_ID = ?,"+
  			"REASON = ? WHERE ASSET_ID = ?";

  		Connection con = null;
  		PreparedStatement ps = null;

  		try
  		{
  		 con = getConnection("fixedasset");

  		 ps = con.prepareStatement(updateQuery);
  		 ps.setInt(1,userId);
  		 ps.setString(2,reason);
       ps.setString(3,assetId);

  		 i = ps.executeUpdate();
       if((i == 1) || (i == 2))
       {
        result = true;
       }
     	}
  		catch(Exception e)
  		{
  		 String warning = "WARNING:Error updating Asset Depr. Adjustment ->" + e.getMessage();
  		 System.out.println(warning);
  		 e.printStackTrace();
  		}
  		finally
  		{
  		 closeConnection(con, ps);
  		}
  		return result;
  	}

    public boolean insertAssetDepAdjustment(String assetId,double accumDep,double newAccumDep,double nbv,
  		                                 double newNbv,String reason,String adjustDate,String effDate,int userId,String raiseEntry,String mtid)
    {
  		//String updateQuery = "UPDATE AM_ASSET SET ACCUM_DEP = ACCUM_DEP + ?,"+
  		//	" NBV = ?, DEP_YTD = DEP_YTD + ? WHERE ASSET_ID = ?";

        //System.out.println(" I am inside the insertAssetDepAdjustment method ()//////////////////");
  		String insertQuery = "INSERT INTO AM_ASSET_DEP_ADJUSTMENT(ASSET_ID,ACCUM_DEP,NEW_ACCUM_DEP,NBV,NEW_NBV,REASON,"+
  			                   "ADJUST_DATE,EFFDATE,USER_ID,RAISE_ENTRY,ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

  		Connection con = null;
  		PreparedStatement ps = null;
      boolean result = false;
      //boolean autoCommit = false;

  		try
  		{
  		 con = getConnection("fixedasset");
       //autoCommit = con.getAutoCommit();
      // con.setAutoCommit(false);
  		 ps = con.prepareStatement(insertQuery);
  		 ps.setString(1,assetId);
  		 ps.setDouble(2,accumDep);
       ps.setDouble(3,newAccumDep);
       ps.setDouble(4,nbv);
       ps.setDouble(5,newNbv);
  		 ps.setString(6,reason);
  		 ps.setDate(7,dateConvert(adjustDate));
       ps.setDate(8,dateConvert(effDate));
  		 ps.setInt(9,userId);
       ps.setString(10,raiseEntry);
       ps.setString(11, mtid);
       //ps.setLong(11,System.currentTimeMillis());

       int i = ps.executeUpdate();

            //System.out.println("the value of insert of asset in asset manager is //////////" +i);

      // ps = con.prepareStatement(updateQuery);
  		// ps.setDouble(1,accumDep);
  		 //ps.setDouble(2,newNbv);
       //ps.setDouble(3,accumDep);
      // ps.setString(4,assetId);

  		 //int k = ps.executeUpdate();

       //if(((i == 1) || (i == 2)) && ((k == 1) || (k == 2)))
       //{
         //con.rollback();
        // con.commit();
        // con.setAutoCommit(autoCommit);
        // result = true;
      // }

  		}
  		catch(Exception e)
  		{
  		 String warning = "WARNING:Error inserting Asset Depr. Adjustment ->" + e.getMessage();
  		 System.out.println(warning);
  		 e.printStackTrace();
  		}
  		finally
  		{
  		 closeConnection(con, ps);
  		}

  		return result;
  	}

      public ArrayList getDepAdjustmentAssetList(String query_)
      {

       String query = "SELECT A.ID,A.ASSET_ID,A.accum_dep,A.new_accum_dep,A.nbv,A.new_nbv,"+
                        " A.reason,A.adjust_date,A.effDate,A.USER_ID,A.RAISE_ENTRY,"+
                        " B.BRANCH_ID,B.DEPT_ID,"+
                        " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS"+
                        " FROM AM_ASSET_DEP_ADJUSTMENT A,AM_ASSET_MAIN B "+
                        " WHERE A.ASSET_ID = B.ASSET_ID "+query_;

         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;

         ArrayList list = new ArrayList();
         //declare DTO object
         DepAdjustment adjust = null;

         try
         {
          con = getConnection("fixedasset");
          ps = con.prepareStatement(query);
          rs = ps.executeQuery();

          while (rs.next())
          {
           int adjustId = rs.getInt("ID");
           String assetId = rs.getString("ASSET_ID");
           double accumDep = rs.getDouble("accum_dep");
           double newAccumDep = rs.getDouble("new_accum_dep");
           double nbv = rs.getDouble("nbv");
           double newNbv = rs.getDouble("new_nbv");
           String reason = rs.getString("reason");
           String adjustDate = formatDate(rs.getDate("adjust_DATE"));
           String effDate = formatDate(rs.getDate("EFFDATE"));
           int userId = rs.getInt("USER_ID");
           String raiseEntry = rs.getString("RAISE_ENTRY");
           int branchId = rs.getInt("BRANCH_ID");
           int deptId = rs.getInt("DEPT_ID");
           int categoryId = rs.getInt("CATEGORY_ID");
           String regNo = rs.getString("REGISTRATION_NO");
           String desc = rs.getString("DESCRIPTION");
           //double cost = rs.getDouble("COST_PRICE");
           String assetUser = rs.getString("ASSET_USER");
           String assetStatus = rs.getString("ASSET_STATUS");

//           adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
//                      raiseEntry,adjustDate,effDate,userId,branchId,deptId,
//                         categoryId,regNo,desc,assetUser,assetStatus);
           adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
                   raiseEntry,adjustDate,effDate,userId,categoryId,0,0,desc,assetUser,"","",assetStatus);
           list.add(adjust);
           }

          }catch (Exception e) {
                System.out.println("INFO:Error fetching Depr. Adjustment Asset by Query ->" +
                                   e.getMessage());
           } finally {
                closeConnection(con, ps, rs);
           }

           return list;
        }

//modification by lanre
      public int insertAssetMaintanance(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate,String mtid) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);

  i = ps.executeUpdate();


 // ps = con.prepareStatement(updateQuery);
  //ps.setDouble(1, cost);
  //ps.setDouble(2, nbv);
  //ps.setDouble(3, vatableCost);
  //ps.setDouble(4, vatAmt);
  //ps.setDouble(5, whtAmt);
  ///ps.setDouble(6, accumDep);
  //ps.setString(7, assetId);

  //i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

      public ArrayList getAssetMaintenanceList(String query_) {

          String query = "SELECT A.REVALUE_ID,A.ASSET_ID,A.COST_INCREASE,A.REVALUE_REASON,A.REVALUE_DATE," +
                         " A.USER_ID,A.RAISE_ENTRY,A.R_VENDOR_AC,A.COST_PRICE,A.VATABLE_COST,A.VAT_AMOUNT," +
                         " A.WHT_AMOUNT,A.NBV,A.ACCUM_DEP,A.OLD_COST_PRICE,A.OLD_VATABLE_COST,A.OLD_VAT_AMOUNT," +
                         " A.OLD_WHT_AMOUNT,A.OLD_NBV,A.OLD_ACCUM_DEP,A.EFFDATE,B.BRANCH_ID,B.DEPT_ID," +
                         " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS" +
                         " FROM am_asset_improvement A,AM_ASSET_MAIN B " +
                         " WHERE A.ASSET_ID = B.ASSET_ID " + query_;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));
                  int branchId = rs.getInt("BRANCH_ID");
                  int deptId = rs.getInt("DEPT_ID");
                  int categoryId = rs.getInt("CATEGORY_ID");
                  String regNo = rs.getString("REGISTRATION_NO");
                  String desc = rs.getString("DESCRIPTION");
                  //double cost = rs.getDouble("COST_PRICE");
                  String assetUser = rs.getString("ASSET_USER");
                  String assetStatus = rs.getString("ASSET_STATUS");

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, branchId, deptId,
                                            categoryId, regNo, desc, assetUser,
                                            assetStatus);

                  list.add(revalue);
              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching am_asset_improvement Asset by Query ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return list;
      }

      public void deleteAssetmMaintenance(String id) {
          String query = "DELETE FROM am_asset_improvement WHERE ASSET_ID = '" + id +
                         "'";
          excuteSQLCode(query);
      }

      public int updateAssetMaintenance(String assetId, int userId, String reason,
              String vendorAcct, String raiseEntry) {

//nbv += cost;
//costPrice += cost;
int i = 0;

String updateQuery = "UPDATE am_asset_improvement SET USER_ID = ?," +
         "REVALUE_REASON = ?, R_VENDOR_AC = ? WHERE ASSET_ID = ?";

Connection con = null;
PreparedStatement ps = null;

try {
con = getConnection("fixedasset");

ps = con.prepareStatement(updateQuery);
ps.setInt(1, userId);
ps.setString(2, reason);
ps.setString(3, vendorAcct);
// ps.setString(4,raiseEntry);
ps.setString(4, assetId);

i = ps.executeUpdate();

} catch (Exception e) {
String warning = "WARNING:Error updating Asset am_asset_improvement ->" +
         e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
return i;
}
      //get Maintenance asset by ID
      public Revaluation getMaintenanceAsset(String id) {

          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM am_asset_improvement " +
                  "WHERE ASSET_ID = '" + id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");


              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching am_asset_improvement Asset by ID ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }


public void updateAssetRevalue(String id){

    double cost_price=0;
     double nbv=0;
      double vatable_cost=0;
       double vat_amount=0;
        double wht_amount=0;
         double accum_dep=0;

      //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");

        String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP from AM_ASSETREVALUE where asset_id = '"+id+"'";

        String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                            " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";
/*
        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        */
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
           cost_price= rs.getDouble(1);
           nbv= rs.getDouble(2);
           vatable_cost= rs.getDouble(3);
           vat_amount= rs.getDouble(4);
           wht_amount= rs.getDouble(5);
           accum_dep= rs.getDouble(6);


            }//while
                 //////update am_asset table with approve values
              ps = con.prepareStatement(updateQuery);
              ps.setDouble(1, cost_price);
               ps.setDouble(2, nbv);
                ps.setDouble(3, vatable_cost);
                 ps.setDouble(4, vat_amount);
                  ps.setDouble(5, wht_amount);
                   ps.setDouble(6, accum_dep);
              ps.setString(7, id);
               i = ps.executeUpdate();




             /*
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, new ApplicationHelper()
					.getGeneratedId("AM_ASSETREVALUE"));
*/
          //  i = ps.executeUpdate();





            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }







}


public void updateAssetTransfer(String id){

   int dept_id = 0;
   int branch_id = 0;
   int section_id =0;
   String  asset_user ="";
   String  who_to_rem="";
   String e_mail1 ="";
   String who_to_rem2="";
   String e_mail2="";
   String branch_code="";
   String section_code="";
   String dept_code="";

        int i = 0;
        //System.out.println("i am in update asset transfer of asset manager UUUUUUUUUUUUUUUUUUUU");

        String query1= "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE from AM_ASSETTRANSFER where asset_id = '"+id+"'";

       // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                       //     " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";





         String updateFleetMaster= "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                 "SECTION_CODE=?,DEPT_CODE=? where asset_id ='"+id+"'";

         /*
         String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + branch_id +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";
        */


        /*
        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = " +   dept_id +
                             ",BRANCH_ID = " + branch_id+ "," +
                             "SECTION_ID = " + section_id= +
                             "',ASSET_USER = '" + asset_user +

                             "',WHO_TO_REM = '"
                             + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";
                             */

String updateQuery="update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, "+
        "BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=? where asset_id ='"+id+"'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next())
            {
           dept_id= rs.getInt(1);
           branch_id= rs.getInt(2);
           section_id= rs.getInt(3);
           asset_user= rs.getString(4);
          branch_code= rs.getString(9);
          section_code = rs.getString(10);
          dept_code = rs.getString(11);
           // wht_amount= rs.getDouble(5);
           //accum_dep= rs.getDouble(6);
            }//while

              ps = con.prepareStatement(updateQuery);
              ps.setInt(1, dept_id);
               ps.setInt(2, branch_id);
                ps.setInt(3, section_id);
                 ps.setString(4,asset_user );
                 ps.setString(5, branch_code);
                 ps.setString(6,dept_code);
                 ps.setString(7,section_code);

     System.out.println("#######################about to update am_asset with  new dept_id "+dept_id+"" +
             " new branch_id "+branch_id+" new section_id"+section_id);
               i = ps.executeUpdate();


                 //////update ft_fleet_master table with approve values
              ps = con.prepareStatement(updateFleetMaster);
              ps.setInt(1, dept_id);
               ps.setInt(2, branch_id);
                ps.setString(3, code.getBranchCode(String.valueOf(branch_id)));
                 ps.setString(4,code.getSectionCode(String.valueOf(section_id)) );
                  ps.setString(5, code.getDeptCode(String.valueOf(dept_id)));
                  // ps.setDouble(6, accum_dep);
              //ps.setString(7, id);
               i = ps.executeUpdate();

               //////update am_asset table with approve values




             /*
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, new ApplicationHelper()
					.getGeneratedId("AM_ASSETREVALUE"));
*/
          //  i = ps.executeUpdate();





            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error updating asset transfer information ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }







}

public void updateAssetDepreciation(String id){

    double accum_dep=0;
     double nbv=0;


      //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");


            String updateQuery = "UPDATE AM_ASSET SET ACCUM_DEP = ?,"+
  			" NBV = ?, DEP_YTD = ? WHERE ASSET_ID = '"+id+"'";

        String query1= "select new_accum_dep,new_nbv from AM_ASSET_DEP_ADJUSTMENT where asset_id = '"+id+"'";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
           accum_dep= rs.getDouble(1);
           nbv= rs.getDouble(2);



            }//while
                 //////update am_asset table with approve values
              ps = con.prepareStatement(updateQuery);
              ps.setDouble(1, accum_dep);
               ps.setDouble(2, nbv);
                ps.setDouble(3, accum_dep);

               i = ps.executeUpdate();





        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: updateAssetDepreciation(): Error adjusting asset depreciation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
}

    public void updateWIPReclassification(String id)
    {
        int i = 0;
        int dept_id = 0;
        int branch_id = 0;
        int section_id = 0;
        int category_id = 0;
        String asset_user = "";
        String who_to_rem = "";
        String e_mail1 = "";
        String who_to_rem2 = "";
        String e_mail2 = "";
        String branch_code = "";
        String section_code = "";
        String dept_code = "";
        String category_code="";
        java.sql.Date effDate =null;

        String query1 = "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE,NEW_CAT_ID,NEW_CAT_CODE,effDate from AM_WIP_RECLASSIFICATION where asset_id = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                "SECTION_CODE=?,DEPT_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=? where asset_id ='" + id + "'";

        String updateQuery = "update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, " +
                "BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,Effective_Date=?" +
                //"WHO_TO_REM=?,EMAIL1=?,WHO_TO_REM_2=?,EMAIL2=?" +
                " where asset_id ='" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next())
            {
                dept_id = rs.getInt(1);
                branch_id = rs.getInt(2);
                section_id = rs.getInt(3);
                asset_user = rs.getString(4);
                who_to_rem = rs.getString(5);
                e_mail1 = rs.getString(6);
                who_to_rem2 = rs.getString(7);
                e_mail2 = rs.getString(8);
                category_id = rs.getInt(12);

                branch_code = rs.getString(9);
                section_code = rs.getString(10);
                dept_code = rs.getString(11);
                category_code = rs.getString(13);
                effDate = rs.getDate(14);
            }

            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, dept_id);
            ps.setInt(2, branch_id);
            ps.setInt(3, section_id);
            ps.setString(4, asset_user);
            ps.setString(5, branch_code);
            ps.setString(6, dept_code);
            ps.setString(7, section_code);
            ps.setInt(8, category_id);
            ps.setString(9, category_code);
            ps.setDate(10, effDate);
           // ps.setString(10, who_to_rem);
            //ps.setString(11, e_mail1);
            //ps.setString(12, who_to_rem2);
            //ps.setString(13, e_mail2);

            System.out.println("updating  am_asset with  new dept_id = " + dept_id + "" +
                    " new branch_id = " + branch_id + " new section_id = " + section_id +
                    " new Category_id = " +category_id );

            i = ps.executeUpdate();

            ps = con.prepareStatement(updateFleetMaster);
            ps.setInt(1, dept_id);
            ps.setInt(2, branch_id);
            ps.setString(3, code.getBranchCode(String.valueOf(branch_id)));
            ps.setString(4, code.getSectionCode(String.valueOf(section_id)));
            ps.setString(5, code.getDeptCode(String.valueOf(dept_id)));
            ps.setInt(6, category_id);
            ps.setString(7, code.getCategoryCode(String.valueOf(category_id)));

            i = ps.executeUpdate();
        }
        catch (Exception e)
        {
            String warning = "WARNING:Asset Manager class: Error updateWIPAssetTransfer information ->" + e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        }
        finally
        {
            closeConnection(con, ps, rs);
        }
    }

     public void updateWIPReclassification(String id,String otherID)
    {
        int i = 0;
        int dept_id = 0;
        int branch_id = 0;
        int section_id = 0;
        int category_id = 0;
        String asset_user = "";
        String who_to_rem = "";
        String e_mail1 = "";
        String who_to_rem2 = "";
        String e_mail2 = "";
        String branch_code = "";
        String section_code = "";
        String dept_code = "";
        String category_code="";
        java.sql.Date effDate=null;

        String query1 = "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE,NEW_CAT_ID,NEW_CAT_CODE,effDate from AM_WIP_RECLASSIFICATION where asset_id = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                "SECTION_CODE=?,DEPT_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=? where asset_id ='" + id + "'";

        String updateQuery = "update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, " +
                "BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,Effective_Date=?" +
                //"WHO_TO_REM=?,EMAIL1=?,WHO_TO_REM_2=?,EMAIL2=?" +
               " where asset_id ='" + otherID + "'";
               // " where asset_id ='" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next())
            {
                dept_id = rs.getInt(1);
                branch_id = rs.getInt(2);
                section_id = rs.getInt(3);
                asset_user = rs.getString(4);
                who_to_rem = rs.getString(5);
                e_mail1 = rs.getString(6);
                who_to_rem2 = rs.getString(7);
                e_mail2 = rs.getString(8);
                category_id = rs.getInt(12);

                branch_code = rs.getString(9);
                section_code = rs.getString(10);
                dept_code = rs.getString(11);
                category_code = rs.getString(13);
                effDate =rs.getDate(14);
            }

            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, dept_id);
            ps.setInt(2, branch_id);
            ps.setInt(3, section_id);
            ps.setString(4, asset_user);
            ps.setString(5, branch_code);
            ps.setString(6, dept_code);
            ps.setString(7, section_code);
            ps.setInt(8, category_id);
            ps.setString(9, category_code);
            ps.setDate(10, effDate);
           // ps.setString(10, who_to_rem);
            //ps.setString(11, e_mail1);
            //ps.setString(12, who_to_rem2);
            //ps.setString(13, e_mail2);

            System.out.println("updating  am_asset with  new dept_id = " + dept_id + "" +
                    " new branch_id = " + branch_id + " new section_id = " + section_id +
                    " new Category_id = " +category_id );

            i = ps.executeUpdate();

            ps = con.prepareStatement(updateFleetMaster);
            ps.setInt(1, dept_id);
            ps.setInt(2, branch_id);
            ps.setString(3, code.getBranchCode(String.valueOf(branch_id)));
            ps.setString(4, code.getSectionCode(String.valueOf(section_id)));
            ps.setString(5, code.getDeptCode(String.valueOf(dept_id)));
            ps.setInt(6, category_id);
            ps.setString(7, code.getCategoryCode(String.valueOf(category_id)));

            i = ps.executeUpdate();
        }
        catch (Exception e)
        {
            String warning = "WARNING:Asset Manager class: Error updateWIPAssetTransfer information ->" + e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        }
        finally
        {
            closeConnection(con, ps, rs);
        }
    }

public void updateAssetMaintenance(String id){

    double cost_price=0;
     double nbv=0;
      double vatable_cost=0;
       double vat_amount=0;
        double wht_amount=0;
         double accum_dep=0;

      //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");

        String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP from am_asset_improvement where asset_id = '"+id+"'";

        String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                            " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";


         //String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        //                  " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

/*
        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        */
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
           cost_price= rs.getDouble(1);
           nbv= rs.getDouble(2);
           vatable_cost= rs.getDouble(3);
           vat_amount= rs.getDouble(4);
           wht_amount= rs.getDouble(5);
           accum_dep= rs.getDouble(6);


            }//while
                 //////update am_asset table with approve values
              ps = con.prepareStatement(updateQuery);
              ps.setDouble(1, cost_price);
               ps.setDouble(2, nbv);
                ps.setDouble(3, vatable_cost);
                 ps.setDouble(4, vat_amount);
                  ps.setDouble(5, wht_amount);
                   ps.setDouble(6, accum_dep);
              ps.setString(7, id);
               i = ps.executeUpdate();




             /*
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, new ApplicationHelper()
					.getGeneratedId("AM_ASSETREVALUE"));
*/
          //  i = ps.executeUpdate();





            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }







}



public int[] getUsedSupervisors(int tranid){

    int[] supervisors = new int[6];

        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");




        String query1= "select user_id,approval1,approval2,approval3,approval4,approval5 from am_asset_approval where transaction_id = "+tranid;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {

            supervisors[0]=rs.getInt(1);
            supervisors[1]=rs.getInt(2);
            supervisors[2]=rs.getInt(3);
            supervisors[3]=rs.getInt(4);
            supervisors[4]=rs.getInt(5);
            supervisors[5]=rs.getInt(6);




            }//while







        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: updateAssetDepreciation(): Error adjusting asset depreciation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
  return supervisors;
}

 public int insertAssetDisposalNoSupervisor(String assetId, String reason,
                                   String buyerAcct,
                                   double disposalAmount, String raiseEntry,
                                   double profitLoss,
                                   String disposalDate, String effDate,
                                   int userId,String supervise) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CurrentDateTime cdt = new CurrentDateTime();
        int i = 0;

        String updateFleetMaster =
                "UPDATE FT_FLEET_MASTER SET STATUS=? WHERE ASSET_ID = ?";
        String updateQuery =
                "UPDATE AM_ASSET SET ASSET_STATUS=?,DATE_DISPOSED = ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETDISPOSAL(ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT," +
                "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setString(2, reason);
            ps.setString(3, buyerAcct);
            ps.setDouble(4, disposalAmount);
            ps.setString(5, raiseEntry);
            ps.setDouble(6, profitLoss);
            ps.setDate(7, dateConvert(disposalDate));
            ps.setDate(8, dateConvert(effDate));
            ps.setInt(9, userId);
			ps.setString(10, new ApplicationHelper()
					.getGeneratedId("AM_ASSETDISPOSAL"));
            ps.setString(11,supervise);
            ps.setString(12, "D");

            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, "DISPOSED");
                ps.setDate(2, dateConvert(disposalDate));
                ps.setString(3, assetId);
                i = ps.executeUpdate();
                //update fleet master
                ps = con.prepareStatement(updateFleetMaster);
                ps.setString(1, "DISPOSED");
                ps.setString(2, assetId);
                i = ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("AssetManager: insertAssetDisposalNoSupervisor(): INFO:Error Inserting Disposal Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }


  public int insertAssetRevalueNoSupervisor(double cost, double nbv, String assetId,
                                  double costIncrease, String revalueReason,
                                  String revalueDate,
                                  int userId, double vatableCost, double vatAmt,
                                  double whtAmt,
                                  String vendorAcct, String raiseEntry,
                                  double accumDep,
                                  double oldCost, double oldVatableCost,
                                  double oldVatAmt, double oldWhtAmt,
                                  double oldNbv, double oldAccumDep,
                                  String effDate,String mtid) {

        //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in insert asset revalue KKKKKKKKKKKKKKKKKK");
        String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                             " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection("fixedasset");

            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, mtid);

            i = ps.executeUpdate();

          //To update am_asset table with new asset revaluation values

            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


   public int insertAssetTransferNoSupervisor(String assetId, int oldDept, int newDept,
                                   int oldBranch,
                                   int newBranch, String oldUser,
                                   String newUser, int oldSection,
                                   int newSection,
                                   String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2, String effDate, String mtid) {
        int i = 0;


        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + newBranch +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";

        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newDept +
                             "',BRANCH_ID = '" + newBranch + "'," +
                             "SECTION_ID = '" + newSection +
                             "',ASSET_USER = '" + newUser +
                             "',WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";


        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID," +
                "OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
	    ps.setString(20, mtid);


            i = ps.executeUpdate();


            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }

        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAssetTransfer() :Error inserting Asset Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }

 public int insertWipReclassificationNoSupervisor(String assetId, int oldDept, int newDept,int oldBranch,
                                   int newBranch, String oldUser,String newUser, int oldSection,
                                   int newSection,String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,String email1, String whoToRem2,
                                   String email2, String effDate, String mtid,String newcat_id,
                                   String old_cat_id,String status,
                                   String old_email1,String old_email2,String old_who_rem1,String old_who_rem2
                                  )
 {
        int i = 0;


        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + newBranch +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";


        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newDept +
                             "',BRANCH_ID = '" + newBranch + "'," +
                              "',CATEGORY_ID = '" + newcat_id + "'," +
                             "SECTION_ID = '" + newSection +
                             "',ASSET_USER = '" + newUser +
                             "',WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "',CATEGORY_CODE = '" +
                             code.getCategoryCode(String.valueOf(newcat_id)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";

       // System.out.println("updateQuery in insertAssetWipTransferNoSupervisor >>>> " + updateQuery);

        String insertQuery = "INSERT INTO AM_WIP_RECLASSIFICATION(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID," +
                             "NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                             ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CAT_ID,NEW_CAT_ID,OLD_CAT_CODE," +
                             "NEW_CAT_CODE,NEW_WHO_TO_REM,NEW_EMAIL1,NEW_WHO_TO_REM2,NEW_EMAIL2,Approval_Status," +
                             "old_who_to_rem,old_email1,old_who_to_rem2,old_email2)" +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
	    ps.setString(20, mtid);
            ps.setString(21, old_cat_id);
            ps.setString(22, newcat_id);
            ps.setString(23, code.getCategoryCode(String.valueOf(old_cat_id)));
            ps.setString(24, code.getDeptCode(String.valueOf(newcat_id)));
            ps.setString(25, whoToRem1);
            ps.setString(26, email1);
            ps.setString(27, whoToRem2);
            ps.setString(28, email2);
            ps.setString(29, status);
            ps.setString(30, old_who_rem1);
            ps.setString(31, old_email1);
            ps.setString(32, old_who_rem2);
            ps.setString(33, old_email2);
            i = ps.executeUpdate();


            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }

        } catch (Exception e) {
            String warning = "Error:AssetManager: insertAssetWipTransferNoSupervisor()  ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


       public boolean insertAssetDepAdjustmentNoSupervisor(String assetId,double accumDep,double newAccumDep,double nbv,
  		                                 double newNbv,String reason,String adjustDate,String effDate,int userId,String raiseEntry,String mtid)
    {
  		String updateQuery = "UPDATE AM_ASSET SET ACCUM_DEP = ACCUM_DEP + ?,"+
  			" NBV = ?, DEP_YTD = DEP_YTD + ? WHERE ASSET_ID = ?";

        //System.out.println(" I am inside the insertAssetDepAdjustment method ()//////////////////");
  		String insertQuery = "INSERT INTO AM_ASSET_DEP_ADJUSTMENT(ASSET_ID,ACCUM_DEP,NEW_ACCUM_DEP,NBV,NEW_NBV,REASON,"+
  			                   "ADJUST_DATE,EFFDATE,USER_ID,RAISE_ENTRY,ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

  		Connection con = null;
  		PreparedStatement ps = null;
      boolean result = false;
      //boolean autoCommit = false;

  		try
  		{
  		 con = getConnection("fixedasset");
       //autoCommit = con.getAutoCommit();
      // con.setAutoCommit(false);
  		 ps = con.prepareStatement(insertQuery);
  		 ps.setString(1,assetId);
  		 ps.setDouble(2,accumDep);
       ps.setDouble(3,newAccumDep);
       ps.setDouble(4,nbv);
       ps.setDouble(5,newNbv);
  		 ps.setString(6,reason);
  		 ps.setDate(7,dateConvert(adjustDate));
       ps.setDate(8,dateConvert(effDate));
  		 ps.setInt(9,userId);
       ps.setString(10,raiseEntry);
       ps.setString(11, mtid);
       //ps.setLong(11,System.currentTimeMillis());

       int i = ps.executeUpdate();

            //System.out.println("the value of insert of asset in asset manager is //////////" +i);

       ps = con.prepareStatement(updateQuery);
  		 ps.setDouble(1,accumDep);
  		 ps.setDouble(2,newNbv);
       ps.setDouble(3,accumDep);
       ps.setString(4,assetId);

  		 int k = ps.executeUpdate();

       //if(((i == 1) || (i == 2)) && ((k == 1) || (k == 2)))
       //{
         //con.rollback();
        // con.commit();
        // con.setAutoCommit(autoCommit);
        // result = true;
      // }

  		}
  		catch(Exception e)
  		{
  		 String warning = "WARNING:Error inserting Asset Depr. Adjustment ->" + e.getMessage();
  		 System.out.println(warning);
  		 e.printStackTrace();
  		}
  		finally
  		{
  		 closeConnection(con, ps);
  		}

  		return result;
  	}

   public int insertAssetMaintananceNoSupervisor(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate, String mtid) {

 // nbv += cost;
// costPrice += cost;
  int i = 0;

  String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);

  i = ps.executeUpdate();


  ps = con.prepareStatement(updateQuery);
  ps.setDouble(1, cost);
  ps.setDouble(2, nbv);
  ps.setDouble(3, vatableCost);
  ps.setDouble(4, vatAmt);
  ps.setDouble(5, whtAmt);
  ps.setDouble(6, accumDep);
  ps.setString(7, assetId);

  i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

    public Transfer getTransferedAsset2(String id)
    {
        checkOldSection(id);

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,D.SECTION_NAME," +
                       " A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,new_dept_id,new_section,new_branch_id," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E, AM_AD_SECTION D" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID" +
                       " AND A.ASSET_ID = E.ASSET_ID AND A.ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");

                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                int newBranch_id=rs.getInt("new_branch_id");
                int newDept_id= rs.getInt("new_dept_id");
                int newSection_id =rs.getInt("new_section");
                //String assetUser = rs.getString("ASSET_USER");
                //String assetStatus = rs.getString("ASSET_STATUS");

//                 transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId,sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost,
//                                        datePurchased, regNo, effDate,
//                                        categoryId, "", "",newBranch_id,newDept_id,newSection_id);

                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        "", regNo, effDate,
                        categoryId, "", "","");

                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }

   public Transfer getWIPReclassificationAsset(String id)
    {
        //checkOldSection(id);

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
                "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
                "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
                " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E," +
                " AM_AD_SECTION D WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
                "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_ID = E.ASSET_ID AND A.ASSET_ID = '" + id + "'";

        System.out.println("query >>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");

                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("NEW_CAT_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                //String categoryName = "";
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                int newBranch_id=rs.getInt("new_branch_id");
                int newDept_id= rs.getInt("new_dept_id");
                int newSection_id =rs.getInt("new_section");
                String assetUser = rs.getString("NEW_ASSET_USER");
                String NEW_WHO_TO_REM = rs.getString("NEW_WHO_TO_REM");
                String NEW_EMAIL1 = rs.getString("NEW_EMAIL1");
                String NEW_WHO_TO_REM2 = rs.getString("NEW_WHO_TO_REM2");
                String NEW_EMAIL2 = rs.getString("NEW_EMAIL2");

                 transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId,sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, assetUser, "",newBranch_id,newDept_id,newSection_id,
                                        NEW_WHO_TO_REM,NEW_WHO_TO_REM2,NEW_EMAIL1,NEW_EMAIL2);

                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }


   public Transfer getWIPReclassificationAsset2(String id)
    {
        //checkOldSection(id);

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
                "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
                "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
                " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E," +
                " AM_AD_SECTION D WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
                "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_ID = E.OLD_ASSET_ID AND A.ASSET_ID = '" + id + "'";

        System.out.println("query >>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");

                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("NEW_CAT_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                //String categoryName = "";
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                int newBranch_id=rs.getInt("new_branch_id");
                int newDept_id= rs.getInt("new_dept_id");
                int newSection_id =rs.getInt("new_section");
                String assetUser = rs.getString("NEW_ASSET_USER");
                String NEW_WHO_TO_REM = rs.getString("NEW_WHO_TO_REM");
                String NEW_EMAIL1 = rs.getString("NEW_EMAIL1");
                String NEW_WHO_TO_REM2 = rs.getString("NEW_WHO_TO_REM2");
                String NEW_EMAIL2 = rs.getString("NEW_EMAIL2");

                 transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId,sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, assetUser, "",newBranch_id,newDept_id,newSection_id,
                                        NEW_WHO_TO_REM,NEW_WHO_TO_REM2,NEW_EMAIL1,NEW_EMAIL2);

                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }

    public void checkOldSection(String assetID){
    int old_section=0;
     int new_section=0;

    String query = " SELECT old_section, new_section from AM_ASSETTRANSFER " +
                       " WHERE asset_id = '"+assetID+"'";

    //String updateQuery ="update am_assettransfer set old_section =? where asset_id ='"+assetID+"'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
              try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
               old_section = rs.getInt("old_section");
               new_section = rs.getInt("new_section");

                        }

            if(old_section == 0){
                System.out.println("the old_section value is 0 >>>> old_section "+old_section+">>>> new_section" +new_section);
                String updateQuery ="update am_assettransfer set old_section = "+new_section+" where asset_id ='"+assetID+"'";

               // ps.setInt(1, new_section);
            ps = con.prepareStatement(updateQuery);

            int i = ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(
                    "AssetManager: getSectionCodeInfo(int assetID):INFO:Error checking value of old section ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

    }//public int getSectionCodeInfo(int assetID)


    public ArrayList getTransferedAssetList2(String query_) {
/*
        String query_old = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.new_DEPT_ID,B.DEPT_NAME,A.new_BRANCH_ID,C.BRANCH_NAME," +
                       " A.new_SECTION,D.SECTION_NAME,A.new_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO," +
                       " E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.new_DEPT_ID = B.DEPT_ID AND A.new_BRANCH_ID = C.BRANCH_ID AND A.new_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.ASSET_ID " + query_;
*/
        String query = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.new_DEPT_ID,B.DEPT_NAME,A.new_BRANCH_ID,C.BRANCH_NAME," +
                       " A.new_SECTION,D.SECTION_NAME,A.new_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO," +
                       " E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.new_DEPT_ID = B.DEPT_ID AND A.new_BRANCH_ID = C.BRANCH_ID AND A.new_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.OLD_ASSET_ID " + query_;
       // System.out.println("query >>>>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("new_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("new_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("new_SECTION");
                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("new_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                //String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                //String categoryId = rs.getString("CATEGORY_ID");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");

//                transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId, sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost, "",
//                                        regNo, effDate, categoryId, assetUser,
//                                        assetStatus);
                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        "", regNo, effDate,
                        categoryId, "", "","");
                list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }


public ArrayList getTransferedAssetList3(String query_) {
/*
        String query_old = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.new_DEPT_ID,B.DEPT_NAME,A.new_BRANCH_ID,C.BRANCH_NAME," +
                       " A.new_SECTION,D.SECTION_NAME,A.new_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO," +
                       " E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.new_DEPT_ID = B.DEPT_ID AND A.new_BRANCH_ID = C.BRANCH_ID AND A.new_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.ASSET_ID " + query_;
*/
        String query = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.new_DEPT_ID,B.DEPT_NAME,A.new_BRANCH_ID,C.BRANCH_NAME," +
                       " A.new_SECTION,D.SECTION_NAME,A.new_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE," +
                       " A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,A.NEW_CAT_ID,E.REGISTRATION_NO," +
                       " E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS " +
                       " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.new_DEPT_ID = B.DEPT_ID AND A.new_BRANCH_ID = C.BRANCH_ID AND A.new_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_ID = E.OLD_ASSET_ID " + query_;
        System.out.println("query >>>>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("new_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("new_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("new_SECTION");
                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("new_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("NEW_CAT_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                //String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                //String categoryId = rs.getString("CATEGORY_ID");
                String assetUser = rs.getString("ASSET_USER");
                String assetStatus = rs.getString("ASSET_STATUS");

//                transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId, sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost, "",
//                                        regNo, effDate, categoryId, assetUser,
//                                        assetStatus);
                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        "", regNo, effDate,
                        categoryId, "", "","");
                list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    public String getTransferedOldAssetDetails(String newAssetID) {

        //String result = new String();
        String old_asset_id="";

        String oldAssetQuery ="select old_asset_id from am_asset where asset_id='"+newAssetID+"'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ArrayList list = new ArrayList();


        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(oldAssetQuery);
            //rs = ps.executeQuery();



            rs=ps.executeQuery();

             while (rs.next()) {
               old_asset_id = rs.getString("old_asset_id");

            }

            System.out.println("the old asset id is @@@@@@@@@@@@ "+old_asset_id);



        } catch (Exception e) {
            System.out.println(
                    "AssetManager class: getTransferedOldAssetDetails() method: Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return old_asset_id;
    }



    public String[] getTransferedOldAssetID(String old_asset_id) {

        String[] result = new String[4];
        //String old_asset_id="";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ArrayList list = new ArrayList();


        try {
           //String query = "select old_branch_id, dept_id, section_id, asset_user from am_asset where asset_id ='"+old_asset_id+"'";
            String query = "select old_branch_id, old_dept_id, old_section, old_asset_user " +
                    "from am_assettransfer where asset_id ='"+old_asset_id+"'";
           System.out.println("the query @@@@@@@@@@@@@@@@@@@ "+query );
           con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();





           // rs = ps.executeQuery(query);


            while (rs.next()) {
                result[0] = Integer.toString(rs.getInt("old_branch_id"));
                result[1] = Integer.toString(rs.getInt("old_dept_id"));
                result[2] = Integer.toString(rs.getInt("old_section"));
                result[3] = rs.getString("old_asset_user");

            }

             for(int j=0; j<result.length;++j){
                 System.out.println("the value of a is " +result[j]);

             }

        } catch (Exception e) {
            System.out.println(
                    "AssetManager class: getTransferedOldAssetID() method: Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;
    }


     public String[] getWIPTransferedOldAssetID(String old_asset_id) {

        String[] result = new String[4];
        //String old_asset_id="";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ArrayList list = new ArrayList();


        try
        {
           String query = "select old_branch_id, old_dept_id, old_section, old_asset_user " +
                    "from am_wip_reclassification where asset_id ='"+old_asset_id+"'";
           System.out.println("the query >>>> "+query );
           con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                result[0] = Integer.toString(rs.getInt("old_branch_id"));
                result[1] = Integer.toString(rs.getInt("old_dept_id"));
                result[2] = Integer.toString(rs.getInt("old_section"));
                result[3] = rs.getString("old_asset_user");

            }

             for(int j=0; j<result.length;++j){
                 System.out.println("the value of a is " +result[j]);

             }

        } catch (Exception e) {
            System.out.println(
                    "AssetManager class: getWIPTransferedOldAssetID() method: " +
                    "Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;
    }

   public String[] getReclassedOldAssetID(String old_asset_id) {

        String[] result = new String[4];
        //String old_asset_id="";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ArrayList list = new ArrayList();


        try {
           String query = "select old_category_id, reclassify_reason,old_depr_rate,recalc_depr from am_assetReclassification where asset_id ='"+old_asset_id+"'";
            System.out.println("the query @@@@@@@@@@@@@@@@@@@ " + query);
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();





           // rs = ps.executeQuery(query);


            while (rs.next()) {
                result[0] = Integer.toString(rs.getInt("old_category_id"));
                result[1] = rs.getString("reclassify_reason");
                result[2] = Double.toString(rs.getDouble("old_depr_rate"));
                result[3] = rs.getString("recalc_depr");
                //result[3] = rs.getString("asset_user");

            }

             for(int j=0; j<result.length;++j){
                 System.out.println("the value of a is " +result[j]);

             }

        } catch (Exception e) {
            System.out.println(
                    "AssetManager class: getReclassedOldAssetID() method: Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;
    }



   public void updateAssetReclass(String id){

    int new_cat_id=0;
     double new_dep_rate=0;

     int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");

        String query1= "select new_category_id, new_depr_rate from AM_ASSETRECLASSIFICATION where asset_id = '"+id+"'";

        String updateQuery = "UPDATE AM_ASSET SET category_id = ?," +
                            " dep_rate = ? WHERE ASSET_ID = ?";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
           new_cat_id= rs.getInt(1);
           new_dep_rate= rs.getDouble(2);

            }//while
                 //////update am_asset table with approve values
              ps = con.prepareStatement(updateQuery);
              ps.setInt(1, new_cat_id);
               ps.setDouble(2, new_dep_rate);
               ps.setString(3, id);

               i = ps.executeUpdate();





        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: updateAssetReclass(): Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }


}



   public int insertAssetMaintanance(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate,String mtid,String wh_tax_cb, int selectTax,String subject_to_vat,
              double newVC,double newCP,double newNBV,double newVA,double newWA) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,"+
          "WHT_PERCENT,SUBJECT_TO_VAT,NEW_VATABLE_COST, NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22, wh_tax_cb);
  ps.setInt(23, selectTax);
  ps.setString(24, subject_to_vat);
  ps.setDouble(25, newVC);
  ps.setDouble(26, newCP);
  ps.setDouble(27, newNBV);
  ps.setDouble(28, newVA);
  ps.setDouble(29, newWA);

  i = ps.executeUpdate();


 // ps = con.prepareStatement(updateQuery);
  //ps.setDouble(1, cost);
  //ps.setDouble(2, nbv);
  //ps.setDouble(3, vatableCost);
  //ps.setDouble(4, vatAmt);
  //ps.setDouble(5, whtAmt);
  ///ps.setDouble(6, accumDep);
  //ps.setString(7, assetId);

  //i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }


   /*
   public Revaluation getMaintenanceAsset(String id, int mtid) {

          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
                  "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT "+
                  "FROM am_Maintenance " +
                  "WHERE ASSET_ID = '" + id + "' AND REVALUE_ID = "+mtid;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));
                  String wh_tax = rs.getString("WH_TAX");
                  int wht_percent = rs.getInt("WHT_PERCENT");
                  String subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                  double vatable_cst= rs.getDouble("NEW_VATABLE_COST");
                  double cst = rs.getDouble("NEW_COST_PRICE");
                  double n_b_v = rs.getDouble("NEW_NBV");
                  double vat_Amount = rs.getDouble("NEW_VAT_AMOUNT");
                  double wht_Amt =rs.getDouble("NEW_WHT_AMOUNT");


                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");
               revalue.setWh_tax(wh_tax);
               revalue.setWht_percent(wht_percent);
               revalue.setSubject_to_vat(subject_to_vat);

               revalue.setNew_vatable_cost(vatable_cst);
               revalue.setNew_cost_price(cst);
               revalue.setNew_nbv(n_b_v);
               revalue.setNew_vat_amount(vat_Amount);
               revalue.setNew_wht_amount(wht_Amt);
              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching am_Maintenance Asset by ID ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }
*/



public int insertAssetMaintananceNoSupervisor(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate, String mtid, String wh_tax,int wht_percent, String subject_to_vat) {

 // nbv += cost;
// costPrice += cost;
  int i = 0;

  String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ?, "+
        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ? "+
         " WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22,wh_tax);
  ps.setInt(23,wht_percent);
  ps.setString(24,subject_to_vat);


  i = ps.executeUpdate();


  ps = con.prepareStatement(updateQuery);
  ps.setDouble(1, cost);
  ps.setDouble(2, nbv);
  ps.setDouble(3, vatableCost);
  ps.setDouble(4, vatAmt);
  ps.setDouble(5, whtAmt);
  ps.setDouble(6, accumDep);
  ps.setString(7, wh_tax);
  ps.setInt(8, wht_percent);
  ps.setString(9, subject_to_vat);
  ps.setString(10, assetId);

  i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }



public Asset getAssetTransfer(String assetId) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE OLD_ASSET_ID = '" +
                             assetId + "' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depRate = rs.getDouble("DEP_RATE");
                String make = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double monthDep = rs.getDouble("MONTHLY_DEP");
                double costPrice = rs.getDouble("COST_PRICE");
                String depEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String raiseEntry = rs.getString("RAISE_ENTRY");
                double NBV = rs.getDouble("NBV");
                //String effDate = rs.getString("EFFECTIVE_DATE");
                String vendorAcct = rs.getString("VENDOR_AC");
                String model = rs.getString("ASSET_MODEL");
                String engineNo = rs.getString("ASSET_ENGINE_NO");
                String email1 = rs.getString("EMAIL1");
                String email2 = rs.getString("EMAIL2");
                //String vendorName = rs.getString("VENDOR_NAME");
                int regionId = rs.getInt("REGION_ID");
                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");
               // int wht_percent = rs.getInt("WHT_PERCENT");

                _obj = new Asset(assetId, regNo, branchId, branchName, deptId,
                                 deptName, sectionId, sectionName,
                                 categoryId, categoryName, regionId, regionName,
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);

              //  _obj.setWht_percent(wht_percent);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset BY ID ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return _obj;

    }


/*
 public Revaluation getMaintenanceAssetRepost(String id) {

  String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM am_Maintenance " +
                  "WHERE REVALUE_ID = '" + revalue_id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");


              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaintenanceAssetRepost  ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }

 */

 public String getMaxTransaction(String id) {

          String query =
                  "SELECT max(revalue_id) from dbo.am_asset_improvement " +
                  "WHERE ASSET_ID = '" + id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;
          String  revalueId="";
          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                   revalueId = rs.getString(1);


              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaxTransaction  ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalueId;
      }

public HashSet getUsedSupervisors2(int tranid){

       HashSet hashSet = new HashSet();
   // com.magbel.legend.vao.ApprovalRemark approvalRemark = null;
        int i = 0;

        String query1= "select supervisorID from am_approval_remark where transaction_id = "+tranid;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
             //   rs.getInt(1);
            //approvalRemark = new com.magbel.legend.vao.ApprovalRemark();

            //approvalRemark.setSupervisorID(rs.getInt(1));
          hashSet.add(new Integer(rs.getInt(1)));

            }//while

             // approvalRemark = new com.magbel.legend.vao.ApprovalRemark();
             // approvalRemark.setSupervisorID(findTranInitiator(tranid));

              //list.add(approvalRemark);
              hashSet.add(new Integer(findTranInitiator(tranid)));

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: getUsedSupervisors2() ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }

  return hashSet;
}

public int findTranInitiator(int tranid){


        int user_id = 0;

        String query1= "select user_id from am_asset_approval where transaction_id = "+tranid;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {


            user_id =rs.getInt(1);


            }//while

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: findTranInitiator() ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
  return user_id;
}

public String findUserName(int userID){


        String user_name = "";

        String query1= "select user_name from am_gb_user where user_id = "+userID;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {


            user_name =rs.getString(1);


            }//while

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: findUserName() ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
  return user_name;
}
public String getVendor(String assetId){


        String supplier_id = "";

        String query1= "select supplier_name from am_asset where asset_id = '"+assetId+"'";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {


            supplier_id =rs.getString(1);


            }//while

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: getVendor() ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
  return supplier_id;
}

public int insertAssetMaintananceNoSupervisor(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate, String mtid, String wh_tax,int wht_percent, String subject_to_vat,String vendorId,String vendorIdOld,String vendorAccOld) {

 // nbv += cost;
// costPrice += cost;
  int i = 0;

  String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ?, "+
        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=? "+
         " WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22,wh_tax);
  ps.setInt(23,wht_percent);
  ps.setString(24,subject_to_vat);
  ps.setInt(25, Integer.parseInt(vendorId));
  ps.setString(26, vendorAccOld);
  ps.setInt(27, Integer.parseInt(vendorIdOld));



  i = ps.executeUpdate();


  ps = con.prepareStatement(updateQuery);
  ps.setDouble(1, cost);
  ps.setDouble(2, nbv);
  ps.setDouble(3, vatableCost);
  ps.setDouble(4, vatAmt);
  ps.setDouble(5, whtAmt);
  ps.setDouble(6, accumDep);
  ps.setString(7, wh_tax);
  ps.setInt(8, wht_percent);
  ps.setString(9, subject_to_vat);
  ps.setString(10,vendorAcct);
  ps.setInt(11, Integer.parseInt(vendorId));
  ps.setString(12, assetId);

  i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

public int insertAssetMaintanance(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate,String mtid,String wh_tax_cb, int selectTax,String subject_to_vat,
              double newVC,double newCP,double newNBV,double newVA,double newWA,String vendorId,String vendorIdOld,String vendorAccOld) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,"+
  "WHT_PERCENT,SUBJECT_TO_VAT,NEW_VATABLE_COST, NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22, wh_tax_cb);
  ps.setInt(23, selectTax);
  ps.setString(24, subject_to_vat);
  ps.setDouble(25, newVC);
  ps.setDouble(26, newCP);
  ps.setDouble(27, newNBV);
  ps.setDouble(28, newVA);
  ps.setDouble(29, newWA);
  ps.setInt(30, Integer.parseInt(vendorId));
  ps.setString(31, vendorAccOld);
  ps.setInt(32, Integer.parseInt(vendorIdOld));

  i = ps.executeUpdate();


 // ps = con.prepareStatement(updateQuery);
  //ps.setDouble(1, cost);
  //ps.setDouble(2, nbv);
  //ps.setDouble(3, vatableCost);
  //ps.setDouble(4, vatAmt);
  //ps.setDouble(5, whtAmt);
  ///ps.setDouble(6, accumDep);
  //ps.setString(7, assetId);

  //i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

 public Revaluation getMaintenanceAsset(String id, int mtid) {

          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
                  "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID "+
                  "FROM am_asset_improvement " +
                  "WHERE ASSET_ID = '" + id + "' AND REVALUE_ID = "+mtid;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));
                  String wh_tax = rs.getString("WH_TAX");
                  int wht_percent = rs.getInt("WHT_PERCENT");
                  String subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                  double vatable_cst= rs.getDouble("NEW_VATABLE_COST");
                  double cst = rs.getDouble("NEW_COST_PRICE");
                  double n_b_v = rs.getDouble("NEW_NBV");
                  double vat_Amount = rs.getDouble("NEW_VAT_AMOUNT");
                  double wht_Amt =rs.getDouble("NEW_WHT_AMOUNT");
                  int rVendorId = rs.getInt("R_VENDOR_ID");
                  String oldVendorAcc = rs.getString("OLD_VENDOR_ACC");
                  int oldVendorId = rs.getInt("OLD_VENDOR_ID");

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");
               revalue.setWh_tax(wh_tax);
               revalue.setWht_percent(wht_percent);
               revalue.setSubject_to_vat(subject_to_vat);

               revalue.setNew_vatable_cost(vatable_cst);
               revalue.setNew_cost_price(cst);
               revalue.setNew_nbv(n_b_v);
               revalue.setNew_vat_amount(vat_Amount);
               revalue.setNew_wht_amount(wht_Amt);
               revalue.setNewVendorId(rVendorId);
               revalue.setOldVendorAcc(oldVendorAcc);;
               revalue.setOldVendorId(oldVendorId);
              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching am_asset_improvement Asset by ID ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
 }

public void updateAssetMaintenance(String id,int mtid){

       double cost_price = 0;
       double nbv = 0;
       double vatable_cost = 0;
       double vat_amount = 0;
       double wht_amount = 0;
       double accum_dep = 0;
       String wh_tax = "";
       int wht_percent = 0;
       String subject_to_vat = "";

       double vatable_cst = 0;
       double cst = 0;
       double n_b_v = 0;
       double vat_Amt = 0;
       double wht_Amt =0;
       int supplier_id=0;
       String vendorAc="";
       //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");

        String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
                "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,R_VENDOR_AC "+
                "from am_asset_improvement where asset_id = '"+id+"' and revalue_id= "+mtid;

        String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = ?," +
                            " NBV =?, VATABLE_COST = ?, VAT = ?, WH_TAX_AMOUNT = ?, ACCUM_DEP = ?, "+
                            "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=? "+
                            "WHERE ASSET_ID = ?";


         //String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        //                  " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

/*
        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        */
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
                cost_price = rs.getDouble(1);
                nbv = rs.getDouble(2);
                vatable_cost = rs.getDouble(3);
                vat_amount = rs.getDouble(4);
                wht_amount = rs.getDouble(5);
                accum_dep = rs.getDouble(6);
                wh_tax = rs.getString(7);
                wht_percent = rs.getInt(8);
                subject_to_vat = rs.getString(9);

                vatable_cst = rs.getDouble(10);
                cst = rs.getDouble(11);
                n_b_v = rs.getDouble(12);
                vat_Amt = rs.getDouble(13);
                wht_Amt = rs.getDouble(14);
                supplier_id = rs.getInt(15);
                vendorAc= rs.getString(16);
            }//while
            System.out.println("=====================11 wh_tax " + wh_tax);
            System.out.println("===================== wht_percent " + wht_percent);
            System.out.println("===================== subject_to_vat " + subject_to_vat);
            System.out.println("===================== vatable_cst " + vatable_cst);
            System.out.println("===================== cst " + cst);
            System.out.println("===================== n_b_v " + n_b_v);
            System.out.println("===================== vat_Amt " + vat_Amt);
            System.out.println("===================== wht_Amt " + wht_Amt);
            //////update am_asset table with approve values

            ps = con.prepareStatement(updateQuery);
            /*
            ps.setDouble(1, cost_price);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatable_cost);
            ps.setDouble(4, vat_amount);
            ps.setDouble(5, wht_amount);
             */
            ps.setDouble(1, cst);
            ps.setDouble(2, n_b_v);
            ps.setDouble(3, vatable_cst);
            ps.setDouble(4, vat_Amt);
            ps.setDouble(5, wht_Amt);
            ps.setDouble(6, accum_dep);
            ps.setString(7, wh_tax);
            ps.setInt(8, wht_percent);
            ps.setString(9, subject_to_vat);
            ps.setInt(10, supplier_id);
            ps.setString(11, vendorAc);
            ps.setString(12, id);
               i = ps.executeUpdate();


              // System.out.println("------------------------here 1");

             /*
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, new ApplicationHelper()
					.getGeneratedId("AM_ASSETREVALUE"));
*/
          //  i = ps.executeUpdate();





            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }
}

public Revaluation getMaintenanceAssetRepost(String id) {

  String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID FROM am_asset_improvement " +
                  "WHERE REVALUE_ID = '" + revalue_id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));
                  int rVendorId = rs.getInt("R_VENDOR_ID");
                  String oldVendorAcc = rs.getString("OLD_VENDOR_ACC");
                  int oldVendorId = rs.getInt("OLD_VENDOR_ID");

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");
                 revalue.setOldVendorId(oldVendorId);
                 revalue.setOldVendorAcc(oldVendorAcc);
                 revalue.setNewVendorId(rVendorId);

              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaintenanceAssetRepost  ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }


 public int updateRepostInfo(int mtid) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery =
                "UPDATE AM_ASSET_APPROVAL SET PROCESS_STATUS ='RP'WHERE TRANSACTION_ID = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, mtid);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Repost Information AssetManager:updateRepostInfo() -> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }


public Transfer getTransferedAsset2Repost(String id)
    {
        checkOldSection(id);

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,D.SECTION_NAME," +
                       " A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,new_dept_id,new_section,new_branch_id," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED, " +
                       " E.NBV,E.ACCUM_DEP,E.Monthly_Dep "+
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E, AM_AD_SECTION D" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID" +
                       " AND A.ASSET_ID = E.OLD_ASSET_ID AND A.ASSET_ID = '" + id + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
                String deptName = rs.getString("DEPT_NAME");
                int oldBranchId = rs.getInt("OLD_BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                int oldSectionId = rs.getInt("OLD_SECTION");

                String sectionName = rs.getString("SECTION_NAME");
                String user = rs.getString("OLD_ASSET_USER");
                String raiseEntry = rs.getString("RAISE_ENTRY");
                String transferDate = formatDate(rs.getDate("TRANSFER_DATE"));
                int userId = rs.getInt("USER_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int categoryId = rs.getInt("CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String description = rs.getString("DESCRIPTION");
                double cost = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                String effDate = formatDate(rs.getDate("EFFDATE"));
                int newBranch_id=rs.getInt("new_branch_id");
                int newDept_id= rs.getInt("new_dept_id");
                int newSection_id =rs.getInt("new_section");
                double nbv = rs.getDouble("NBV");
                double accumDep = rs.getDouble("ACCUM_DEP");
                double montDep = rs.getDouble("Monthly_Dep");


//                 transfer = new Transfer(transferId, assetId, oldDeptId,
//                                        deptName, oldBranchId, branchName,
//                                        oldSectionId,sectionName, user,
//                                        raiseEntry, transferDate, userId,
//                                        categoryName, description, cost,
//                                        datePurchased, regNo, effDate,
//                                        categoryId, "", "",newBranch_id,newDept_id,newSection_id);
                transfer = new Transfer(transferId, assetId, oldDeptId,
                        deptName, oldBranchId, branchName,
                        oldSectionId, sectionName, user,
                        raiseEntry, transferDate, userId,
                        categoryName, description, cost,
                        datePurchased, regNo, effDate,
                        categoryId, "", "","");
                       transfer.setNbv(nbv);
                       transfer.setAccumDep(accumDep);
                       transfer.setMonthDep(montDep);
                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID getTransferedAsset2Repost() ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }







public int updateAssetAfterRepost(String assetId) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery ="update am_asset set asset_status='Active',post_reject_reason='' where asset_id =?";


        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, assetId);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Repost Information updateAssetAfterRepost() -> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }










public void updateAssetTransfer(String id,int tranId){

   int dept_id = 0;
   int branch_id = 0;
   int section_id =0;
   String  asset_user ="";
   String  who_to_rem="";
   String e_mail1 ="";
   String who_to_rem2="";
   String e_mail2="";
   String branch_code="";
   String section_code="";
   String dept_code="";

        int i = 0;
        //System.out.println("i am in update asset transfer of asset manager UUUUUUUUUUUUUUUUUUUU");

        String query1= "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE from AM_ASSETTRANSFER where asset_id = '"+id+"' and transfer_id="+tranId ;

       // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                       //     " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";





         String updateFleetMaster= "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                 "SECTION_CODE=?,DEPT_CODE=? where asset_id ='"+id+"'";

         /*
         String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + branch_id +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";
        */


        /*
        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = " +   dept_id +
                             ",BRANCH_ID = " + branch_id+ "," +
                             "SECTION_ID = " + section_id= +
                             "',ASSET_USER = '" + asset_user +

                             "',WHO_TO_REM = '"
                             + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";
                             */

String updateQuery="update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, "+
        "BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=? where old_asset_id ='"+id+"'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");


            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next())
            {
           dept_id= rs.getInt(1);
           branch_id= rs.getInt(2);
           section_id= rs.getInt(3);
           asset_user= rs.getString(4);
          branch_code= rs.getString(9);
          section_code = rs.getString(10);
          dept_code = rs.getString(11);
           // wht_amount= rs.getDouble(5);
           //accum_dep= rs.getDouble(6);
            }//while

              ps = con.prepareStatement(updateQuery);
              ps.setInt(1, dept_id);
               ps.setInt(2, branch_id);
                ps.setInt(3, section_id);
                 ps.setString(4,asset_user );
                 ps.setString(5, branch_code);
                 ps.setString(6,dept_code);
                 ps.setString(7,section_code);

     System.out.println("#######################about to update am_asset with  new dept_id "+dept_id+"" +
             " new branch_id "+branch_id+" new section_id"+section_id);
               i = ps.executeUpdate();


                 //////update ft_fleet_master table with approve values
              ps = con.prepareStatement(updateFleetMaster);
              ps.setInt(1, dept_id);
               ps.setInt(2, branch_id);
                ps.setString(3, code.getBranchCode(String.valueOf(branch_id)));
                 ps.setString(4,code.getSectionCode(String.valueOf(section_id)) );
                  ps.setString(5, code.getDeptCode(String.valueOf(dept_id)));
                  // ps.setDouble(6, accum_dep);
              //ps.setString(7, id);
               i = ps.executeUpdate();

               //////update am_asset table with approve values




             /*
            ps.setString(1, assetId);
            ps.setDouble(2, costIncrease);
            ps.setString(3, revalueReason);
            ps.setDate(4, dateConvert(revalueDate));
            ps.setInt(5, userId);
            ps.setDouble(6, cost);
            ps.setDouble(7, vatableCost);
            ps.setDouble(8, vatAmt);
            ps.setDouble(9, whtAmt);
            ps.setString(10, vendorAcct);
            ps.setString(11, raiseEntry);
            ps.setDouble(12, nbv);
            ps.setDouble(13, accumDep);
            ps.setDouble(14, oldCost);
            ps.setDouble(15, oldVatableCost);
            ps.setDouble(16, oldVatAmt);
            ps.setDouble(17, oldWhtAmt);
            ps.setDouble(18, oldNbv);
            ps.setDouble(19, oldAccumDep);
            ps.setDate(20, dateConvert(effDate));
			ps.setString(21, new ApplicationHelper()
					.getGeneratedId("AM_ASSETREVALUE"));
*/
          //  i = ps.executeUpdate();





            /*
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, cost);
            ps.setDouble(2, nbv);
            ps.setDouble(3, vatableCost);
            ps.setDouble(4, vatAmt);
            ps.setDouble(5, whtAmt);
            ps.setDouble(6, accumDep);
            ps.setString(7, assetId);

            i = ps.executeUpdate();
            */
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error updating asset transfer information ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }







}



  //insert asset transfer
    public int insertAssetTransfer(String assetId, int oldDept, int newDept,
                                   int oldBranch,
                                   int newBranch, String oldUser,
                                   String newUser, int oldSection,
                                   int newSection,
                                   String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2, String effDate,String mtid,String categoryCode,String description,double cost,double nbv,double monthDep,double accumDep) {
        int i = 0;

        /*
        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + newBranch +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";

        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newDept +
                             "',BRANCH_ID = '" + newBranch + "'," +
                             "SECTION_ID = '" + newSection +
                             "',ASSET_USER = '" + newUser +
                             "',WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";

        */
        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
            ps.setString(20,mtid);
            ps.setString(21,categoryCode);
            ps.setString(22,description);
            ps.setDouble(23,cost);
            ps.setDouble(24,nbv);
            ps.setDouble(25,monthDep);
            ps.setDouble(26,accumDep);

            i = ps.executeUpdate();

            /*
            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }
            */
        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAssetTransfer() :Error inserting Asset Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


public int insertAssetTransferNoSupervisor(String assetId, int oldDept, int newDept,
                                   int oldBranch,
                                   int newBranch, String oldUser,
                                   String newUser, int oldSection,
                                   int newSection,
                                   String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2, String effDate, String mtid,String categoryCode, String description,
                                   double cost, double nbv,double monthDep,double accumDep) {
        int i = 0;


        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
                                   newDept +
                                   "',BRANCH_ID = '" + newBranch +
                                   "',BRANCH_CODE = '" +
                                   code.getBranchCode(String.valueOf(newBranch)) +
                                   "',SECTION_CODE = '" +
                                   code.getSectionCode(
                                   String.valueOf(newSection)) +
                                   "',DEPT_CODE = '" +
                                   code.getDeptCode(String.valueOf(newDept)) +
                                   "' WHERE ASSET_ID = '" + assetId + "'";

        String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newDept +
                             "',BRANCH_ID = '" + newBranch + "'," +
                             "SECTION_ID = '" + newSection +
                             "',ASSET_USER = '" + newUser +
                             "',WHO_TO_REM = '" + whoToRem1 +
                             "',EMAIL1 = '" + email1 +
                             "',WHO_TO_REM_2 = '" + whoToRem2 +
                             "',EMAIL2 = '" + email2 +
                             "',BRANCH_CODE = '" +
                             code.getBranchCode(String.valueOf(newBranch)) +
                             "',SECTION_CODE = '" +
                             code.getSectionCode(String.valueOf(newSection)) +
                             "',DEPT_CODE = '" +
                             code.getDeptCode(String.valueOf(newDept)) +
                             "' WHERE ASSET_ID = '" + assetId + "'";


        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,"
+
                "OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, assetId);
            ps.setInt(2, oldDept);
            ps.setInt(3, newDept);
            ps.setInt(4, oldBranch);
            ps.setInt(5, newBranch);
            ps.setString(6, oldUser);
            ps.setString(7, newUser);
            ps.setInt(8, oldSection);
            ps.setInt(9, newSection);
            ps.setString(10, raiseEntry);
            ps.setDate(11, dateConvert(transferDate));
            ps.setInt(12, userId);
            ps.setDate(13, dateConvert(effDate));
            ps.setString(14, code.getBranchCode(String.valueOf(oldBranch)));
            ps.setString(15, code.getSectionCode(String.valueOf(oldSection)));
            ps.setString(16, code.getDeptCode(String.valueOf(oldDept)));
            ps.setString(17, code.getBranchCode(String.valueOf(newBranch)));
            ps.setString(18, code.getSectionCode(String.valueOf(newSection)));
            ps.setString(19, code.getDeptCode(String.valueOf(newDept)));
	    ps.setString(20, mtid);
            ps.setString(21, categoryCode);
            ps.setString(22, description);
            ps.setDouble(23,cost);
            ps.setDouble(24,nbv);
            ps.setDouble(25,monthDep);
            ps.setDouble(26,accumDep);

            i = ps.executeUpdate();


            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
                ps = con.prepareStatement(updateFleetMaster);
                i = ps.executeUpdate();

            }

        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAssetTransfer() :Error inserting Asset Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


public int insertAssetMaintananceNoSupervisor(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate, String mtid, String wh_tax,int wht_percent, String subject_to_vat,
              String vendorId,String vendorIdOld,String vendorAccOld,String description,String categoryCode, String
branchCode,String lpo,String invoiceNo, double newCP) {

 // nbv += cost;
// costPrice += cost;
  int i = 0;

  String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP =ACCUM_DEP + ?, "+
        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=? "+
         " WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC," +
  "RAISE_ENTRY,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT," +
          "OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,new_cost_price)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22,wh_tax);
  ps.setInt(23,wht_percent);
  ps.setString(24,subject_to_vat);
  ps.setInt(25, Integer.parseInt(vendorId));
  ps.setString(26, vendorAccOld);
  ps.setInt(27, Integer.parseInt(vendorIdOld));
  ps.setString(28, description);
  ps.setString(29, categoryCode);
  ps.setString(30, branchCode);
  ps.setString(31, lpo);
  ps.setString(32, invoiceNo);
  ps.setDouble(33, newCP);

  i = ps.executeUpdate();


  ps = con.prepareStatement(updateQuery);
  ps.setDouble(1, cost);
  ps.setDouble(2, nbv);
  ps.setDouble(3, vatableCost);
  ps.setDouble(4, vatAmt);
  ps.setDouble(5, whtAmt);
  ps.setDouble(6, accumDep);
  ps.setString(7, wh_tax);
  ps.setInt(8, wht_percent);
  ps.setString(9, subject_to_vat);
  ps.setString(10,vendorAcct);
  ps.setInt(11, Integer.parseInt(vendorId));
  ps.setString(12, assetId);

  i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }


public int insertAssetMaintanance(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate,String mtid,String wh_tax_cb, int selectTax,String subject_to_vat,
              double newVC,double newCP,double newNBV,
              double newVA,double newWA,String vendorId,String vendorIdOld,
              String vendorAccOld, String description,String categoryCode,String branchCode,String lpo,String invoiceNum) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,"+
  "WHT_PERCENT,SUBJECT_TO_VAT,NEW_VATABLE_COST, NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("fixedasset");

  ps = con.prepareStatement(insertQuery);
  ps.setString(1, assetId);
  ps.setDouble(2, costIncrease);
  ps.setString(3, revalueReason);
  ps.setDate(4, dateConvert(revalueDate));
  ps.setInt(5, userId);
  ps.setDouble(6, cost);
  ps.setDouble(7, vatableCost);
  ps.setDouble(8, vatAmt);
  ps.setDouble(9, whtAmt);
  ps.setString(10, vendorAcct);
  ps.setString(11, raiseEntry);
  ps.setDouble(12, nbv);
  ps.setDouble(13, accumDep);
  ps.setDouble(14, oldCost);
  ps.setDouble(15, oldVatableCost);
  ps.setDouble(16, oldVatAmt);
  ps.setDouble(17, oldWhtAmt);
  ps.setDouble(18, oldNbv);
  ps.setDouble(19, oldAccumDep);
  ps.setDate(20, dateConvert(effDate));
  ps.setString(21, mtid);
  ps.setString(22, wh_tax_cb);
  ps.setInt(23, selectTax);
  ps.setString(24, subject_to_vat);
  ps.setDouble(25, newVC);
  ps.setDouble(26, newCP);
  ps.setDouble(27, newNBV);
  ps.setDouble(28, newVA);
  ps.setDouble(29, newWA);
  ps.setInt(30, Integer.parseInt(vendorId));
  ps.setString(31, vendorAccOld);
  ps.setInt(32, Integer.parseInt(vendorIdOld));
   ps.setString(33, description);
    ps.setString(34, categoryCode);
     ps.setString(35, branchCode);
     ps.setString(36, lpo);
     ps.setString(37, invoiceNum);
  i = ps.executeUpdate();


 // ps = con.prepareStatement(updateQuery);
  //ps.setDouble(1, cost);
  //ps.setDouble(2, nbv);
  //ps.setDouble(3, vatableCost);
  //ps.setDouble(4, vatAmt);
  //ps.setDouble(5, whtAmt);
  ///ps.setDouble(6, accumDep);
  //ps.setString(7, assetId);

  //i = ps.executeUpdate();

  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_improvement Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

 
public String effectiveDate(String  assetId){

     String result="";

        String query1= "select effDate from AM_WIP_RECLASSIFICATION where asset_id = '"+assetId+"'";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {

        result =formatDate(rs.getDate(1));

            }//while

        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: effectiveDate() ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
        }

  return result;
}


 public int updateRepostInfo2(int mtid) {


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;

        String updateQuery =
                "UPDATE AM_ASSET_APPROVAL SET PROCESS_STATUS ='RR'WHERE TRANSACTION_ID = ?";

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, mtid);


            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("INFO:Error Updating Repost Information AssetManager:updateRepostInfo() -> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return i;
    }




 public Revaluation getMaintenanceAssetRepost(String id,int tranId) {

  //String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID FROM am_asset_improvement " +
                  "WHERE REVALUE_ID = " + tranId ;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("fixedasset");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
                  int userId = rs.getInt("USER_ID");
                  String raiseEntry = rs.getString("RAISE_ENTRY");
                  String revVendorAcct = rs.getString("R_VENDOR_AC");
                  double cost = rs.getDouble("COST_PRICE");
                  double vatableCost = rs.getDouble("VATABLE_COST");
                  double vatAmt = rs.getDouble("VAT_AMOUNT");
                  double whtAmt = rs.getDouble("WHT_AMOUNT");
                  double nbv = rs.getDouble("NBV");
                  double accumDep = rs.getDouble("ACCUM_DEP");
                  double oldCost = rs.getDouble("OLD_COST_PRICE");
                  double oldVatableCost = rs.getDouble("OLD_VATABLE_COST");
                  double oldVatAmt = rs.getDouble("OLD_VAT_AMOUNT");
                  double oldWhtAmt = rs.getDouble("OLD_WHT_AMOUNT");
                  double oldNbv = rs.getDouble("OLD_NBV");
                  double oldAccumDep = rs.getDouble("OLD_ACCUM_DEP");
                  String effDate = formatDate(rs.getDate("EFFDATE"));
                  int rVendorId = rs.getInt("R_VENDOR_ID");
                  String oldVendorAcc = rs.getString("OLD_VENDOR_ACC");
                  int oldVendorId = rs.getInt("OLD_VENDOR_ID");

                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");
                 revalue.setOldVendorId(oldVendorId);
                 revalue.setOldVendorAcc(oldVendorAcc);
                 revalue.setNewVendorId(rVendorId);

              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaintenanceAssetRepost with tranId ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }


}
