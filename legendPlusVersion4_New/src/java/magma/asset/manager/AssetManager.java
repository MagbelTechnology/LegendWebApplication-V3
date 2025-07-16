package magma.asset.manager;

import magma.AssetRecordsBean;
import magma.ExcelAssetDisposalBean;
//import magma.CurrentDateTime;
import magma.ExcelAssetImproveBean;
import magma.FleetExcelUploadBean;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.GroupAsset;
import magma.net.vao.BatchPosting;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import magma.asset.dto.*;
import magma.util.Codes;

import com.magbel.util.ApplicationHelper;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.newAssetTransaction;
import com.magbel.util.CurrentDateTime;

public class AssetManager extends MagmaDBConnection {
	com.magbel.util.DatetimeFormat df;
	com.magbel.util.CurrentDateTime cdt;
//	com.magbel.util.HtmlUtility htmlUtil;
//	AssetManager asset_manager = new AssetManager();
	 private ApprovalManager appManager;
    private Codes code;

    public AssetManager() {
      //  System.out.println("USING magma.asset.manager.AssetManager");
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
            con = getConnection("legendPlus");
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
                int subcategoryId = rs.getInt("SUB_CATEGORY_ID");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
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
                String integrify = rs.getString("INTEGRIFY");
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
                                 subj2Wht, vatableCost, integrify);
                _obj.setSubcategoryId(subcategoryId);
                _obj.setSubcategoryName(subcategoryName);
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

    /*
    public ArrayList getAssetByQuery(String query) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE " + query;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        Asset _obj = null;

        try {
            con = getConnection("legendPlus");
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

    */


    /*
    public Asset getAsset(String assetId) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_ID = '" +
                             assetId + "' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection("legendPlus");
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
*/
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
        "a.NBV,a.Accum_Dep,b.old_cat_id from am_wip_reclassification b, am_asset a where b.asset_id=?" +
        " and a.asset_id=?";

       // System.out.println("selOldDetails_qry >>>> " + selOldDetails_qry);

         try
         {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selOldDetails_qry);
            ps.setString(1, assetId);
            ps.setString(2, newAssetId);
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
     /*
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

            con = getConnection("legendPlus");
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

    */
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
                "PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,DISPOSAL_STATUS,DISPOSAL_TYPE, " +
                "DISPOSAL_PERCENT,PARTIAL_AMT " +
                "FROM am_asset_disposal WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Disposal dispose = null;
        //DataConnect connect = new DataConnect();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
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
                String partialType = rs.getString("DISPOSAL_TYPE");
                double partialPercent = rs.getDouble("DISPOSAL_PERCENT");
                double partialAmt = rs.getDouble("PARTIAL_AMT");
                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status, "", "", "", "",
                                       "", 0, "", "");
                dispose.setPartialAmt(partialAmt);
                dispose.setPartialPercent(partialPercent);
                dispose.setDisposalType(partialType);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching disposed Asset by ID->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return dispose;
    }

    
    public Disposal getDisposedAssetDetails(String id) {

        String query = "SELECT Trans_id AS DISPOSAL_ID,ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT,DISPOSAL_AMOUNT,'N' AS RAISE_ENTRY, "
        		+ "PROFIT_LOSS,DISPOSAL_DATE,DISPOSAL_DATE AS EFFECTIVE_DATE,USER_ID,'P' AS DISPOSAL_STATUS,DISPOSAL_TYPE, "
        		+ "DISPOSAL_PERCENT,PARTIAL_AMT "
        		+ "FROM Disposal_View WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Disposal dispose = null;
        //DataConnect connect = new DataConnect();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
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
                String partialType = rs.getString("DISPOSAL_TYPE");
                double partialPercent = rs.getDouble("DISPOSAL_PERCENT");
                double partialAmt = rs.getDouble("PARTIAL_AMT");
                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status, "", "", "", "",
                                       "", 0, "", "");
                dispose.setPartialAmt(partialAmt);
                dispose.setPartialPercent(partialPercent);
                dispose.setDisposalType(partialType);

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
                       "PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,DISPOSAL_STATUS,PARTIAL_TYPE, " +
                       "PARTIAL_PERCENT,PARTIAL_AMT " +
                       "FROM AM_ASSETDISPOSAL WHERE ASSET_ID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //declare DTO object
        Disposal dispose = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, query_);
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
                String partialType = rs.getString("PARTIAL_TYPE");
                double partialPercent = rs.getInt("PARTIAL_PERCENT");
                double partialAmt = rs.getDouble("PARTIAL_AMT");
                
                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status, "", "", "", "",
                                       "", 0, "", "");
                dispose.setPartialAmt(partialAmt);
                dispose.setPartialPercent(partialPercent);
                dispose.setDisposalType(partialType);                
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
                       " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.COST_PRICE,B.ASSET_USER,B.ASSET_STATUS," +
                       " A.DISPOSAL_PERCENT,A.PARTIAL_AMT " +
                       " FROM AM_ASSETDISPOSAL A,AM_ASSET B WHERE A.ASSET_ID = B.ASSET_ID AND ASSET_ID = ?"; 
//		System.out.println("<<<query in getDisposedAssetList: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Disposal dispose = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, query_);
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
               // String partialType = rs.getString("PARTIAL_TYPE");
     //           double partialPercent = rs.getInt("PARTIAL_PERCENT");
                double partialAmt = rs.getDouble("PARTIAL_AMT");
                
                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status,
                                       branchId, deptId, categoryId, regNo,
                                       desc, cost, assetUser, assetStatus);
						               dispose.setPartialAmt(partialAmt);
						     //          dispose.setPartialPercent(partialPercent);
				//		               dispose.setDisposalType(partialType);   
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

    //get all disposed asset
    public ArrayList getDisposedAssetList(String qryfilter,String branch,String category,String department,String regNumber,String asset_Id,String dFromDate,String toDate,String disposalStatus,String order) {

        String query = "SELECT A.DISPOSAL_ID,A.ASSET_ID,A.DISPOSAL_REASON,A.BUYER_ACCOUNT,A.DISPOSAL_AMOUNT,A.RAISE_ENTRY,"
        		+ "A.PROFIT_LOSS,A.DISPOSAL_DATE,A.EFFECTIVE_DATE,A.USER_ID,A.DISPOSAL_STATUS,B.BRANCH_ID,B.DEPT_ID,"
        		+ "B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.COST_PRICE,B.ASSET_USER,B.ASSET_STATUS,"
        		+ "A.DISPOSAL_PERCENT,A.PARTIAL_AMT "
        		+ "FROM AM_ASSETDISPOSAL A,AM_ASSET B WHERE A.ASSET_ID = B.ASSET_ID " + qryfilter
        		+ " UNION "
        		+ "SELECT disposal_ID AS DISPOSAL_ID,A.ASSET_ID,A.DISPOSAL_REASON,buyer_ac as BUYER_ACCOUNT,disposalAmount as DISPOSAL_AMOUNT,'' AS RAISE_ENTRY,"
        		+ "0.0 as PROFIT_LOSS, Disposal_Date AS DISPOSAL_DATE,effDate AS EFFECTIVE_DATE,A.USER_ID,'P' AS DISPOSAL_STATUS,B.BRANCH_ID,B.DEPT_ID,"
        		+ "B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.COST_PRICE,B.ASSET_USER,B.ASSET_STATUS,"
        		+ "0 as DISPOSAL_PERCENT,0.0 as PARTIAL_AMT FROM AM_GROUP_DISPOSAL A,AM_ASSET B WHERE A.ASSET_ID = B.ASSET_ID " + qryfilter+order; 
		//System.out.println("<<<query in getDisposedAssetList: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Disposal dispose = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query.toString());
//          System.out.println("<========qryfilter in getDisposedAssetList=======>: "+qryfilter.contains("BRANCH_ID"));
          if(qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("BRANCH_ID") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DEPT_ID") && !qryfilter.contains("ASSET_ID")){
        	  System.out.println("<========getDisposedAssetList Asset_Status =======>");
        	  ps.setString(1, disposalStatus);
        	  ps.setString(2, disposalStatus);
          }     
          if(qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("BRANCH_ID") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DEPT_ID") && !qryfilter.contains("DISPOSAL_DATE")){
        	  System.out.println("<========getDisposedAssetList Asset_Id and Asset Status =======>0 asset_Id: "+asset_Id);
        	  ps.setString(1, asset_Id);
        	  ps.setString(2, disposalStatus);
        	  ps.setString(3, asset_Id);
        	  ps.setString(4, disposalStatus);
          }    
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("DEPT_ID") && !qryfilter.contains("CATEGORY_ID")  && !qryfilter.contains("ASSET_ID") && !qryfilter.contains("DISPOSAL_DATE")){
        	  System.out.println("<========getDisposedAssetList  branch_Id and asset status =======> ");
        	  ps.setString(1, branch);
        	  ps.setString(2, disposalStatus);
        	  ps.setString(3, branch);
        	  ps.setString(4, disposalStatus);
        	  
          }  
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DISPOSAL_DATE") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DEPT_ID") && !qryfilter.contains("ASSET_ID")){
        	 System.out.println("<========getDisposedAssetList  branch_Id, Date purchased and asset status =======>1: ");
        	  ps.setString(1, branch);
        	  ps.setString(2, dFromDate);
        	  ps.setString(3, toDate);
        	  ps.setString(4, disposalStatus);
        	  ps.setString(5, branch);
        	  ps.setString(6, dFromDate);
        	  ps.setString(7, toDate);
        	  ps.setString(8, disposalStatus);
          } 
          if(qryfilter.contains("DISPOSAL_DATE") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("BRANCH_ID") && !qryfilter.contains("DEPT_ID") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("ASSET_ID")){
        	  System.out.println("<========getDisposedAssetList=======> Date Purchased and Asset Status");
        	  ps.setString(1, dFromDate);
        	  ps.setString(2, toDate);
        	  ps.setString(3, disposalStatus);
        	  ps.setString(4, dFromDate);
        	  ps.setString(5, toDate);
        	  ps.setString(6, disposalStatus);
          }   
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("ASSET_ID")){
        	System.out.println("<========getDisposedAssetList=======> Branch, Department and Asset Status");
        	ps.setString(1, branch);
      	  	ps.setString(2, department);
	      	ps.setString(3, disposalStatus);
	      	ps.setString(4, branch);
	      	ps.setString(5, department);
	      	ps.setString(6, disposalStatus);
          }  
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("ASSET_STATUS") && qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("ASSET_ID")){
          	System.out.println("<========getDisposedAssetList=======> Branch, Department, Catgeory and Asset Status");
          	ps.setString(1, branch);
        	ps.setString(2, department);
        	ps.setString(3, category);
  	      	ps.setString(4, disposalStatus);
  	      	ps.setString(5, branch);
  	      	ps.setString(6, department);
  	      	ps.setString(7, category);
  	      	ps.setString(8, disposalStatus);
            }  
          if(!qryfilter.contains("BRANCH_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("CATEGORY_ID")  && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("ASSET_ID")){
            	System.out.println("<========getDisposedAssetList=======> Department, Category and Asset Status");
            	ps.setString(1, department);
          	  	ps.setString(2, category);
    	      	ps.setString(3, disposalStatus);
    	      	ps.setString(4, department);
    	      	ps.setString(5, category);
    	      	ps.setString(6, disposalStatus);
              }  
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DISPOSAL_DATE") && qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DEPT_ID")){
         	 System.out.println("<========getDisposedAssetList  branch_Id, Date purchased, asset_id and asset status =======>: ");
         	  ps.setString(1, branch);
         	  ps.setString(2, dFromDate);
         	  ps.setString(3, toDate);
         	  ps.setString(4, asset_Id);
         	  ps.setString(5, disposalStatus);
         	  ps.setString(6, branch);
         	  ps.setString(7, dFromDate);
         	  ps.setString(8, toDate);
         	  ps.setString(9, asset_Id);
         	  ps.setString(10, disposalStatus);
           } 
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DISPOSAL_DATE") && qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && qryfilter.contains("DEPT_ID") && !qryfilter.contains("CATEGORY_ID")){
          	 System.out.println("<========getDisposedAssetList  branch_Id, Department, Date purchased, asset_id and asset status =======>: ");
          	  ps.setString(1, branch);
          	  ps.setString(2, department);
          	  ps.setString(3, dFromDate);
          	  ps.setString(4, toDate);
          	  ps.setString(5, asset_Id);
          	  ps.setString(6, disposalStatus);
          	  ps.setString(7, branch);
          	  ps.setString(8, department);
          	  ps.setString(9, dFromDate);
          	  ps.setString(10, toDate);
          	  ps.setString(11, asset_Id);
          	  ps.setString(12, disposalStatus);
            }
          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DISPOSAL_DATE") && !qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && qryfilter.contains("DEPT_ID") && !qryfilter.contains("CATEGORY_ID")){
           	 System.out.println("<========getDisposedAssetList  branch_Id, Department, Date purchased and asset status =======>: ");
           	  ps.setString(1, branch);
           	  ps.setString(2, department);
           	  ps.setString(3, dFromDate);
           	  ps.setString(4, toDate);
           	  ps.setString(5, asset_Id);
           	  ps.setString(6, disposalStatus);
           	  ps.setString(7, branch);
           	  ps.setString(8, department);
           	  ps.setString(9, dFromDate);
           	  ps.setString(10, toDate);
           	  ps.setString(11, asset_Id);
           	  ps.setString(12, disposalStatus);
             }
          if(!qryfilter.contains("BRANCH_ID") && qryfilter.contains("DISPOSAL_DATE") && qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("CATEGORY_ID") && !qryfilter.contains("DEPT_ID")){
          	 System.out.println("<========getDisposedAssetList  Date purchased, asset_id and asset status =======>: ");
          	  ps.setString(1, dFromDate);
          	  ps.setString(2, toDate);
          	  ps.setString(3, asset_Id);
          	  ps.setString(4, disposalStatus);
          	  ps.setString(5, dFromDate);
          	  ps.setString(6, toDate);
          	  ps.setString(7, asset_Id);
          	  ps.setString(8, disposalStatus);
            } 
          
//          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && !query.contains("DEPT_ID") && !query.contains("ASSET_ID")){
//        	  System.out.println("<========getDisposedAssetList=======>3");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, category);
//        	  ps.setString(3, branch);
//          }   
//          if(qryfilter.contains("ASSET_ID") && qryfilter.contains("ASSET_STATUS") && !qryfilter.contains("BRANCH_ID") && !qryfilter.contains("DEPT_ID") ){
////        	  System.out.println("<========getDisposedAssetList=======>3");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, disposalStatus);
//        	  ps.setString(3, branch);
//          }  
//        
//          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("CATEGORY_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getDisposedAssetList=======>40");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, category);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, department);
//        	  ps.setString(5, disposalStatus);
//        	  ps.setString(6, branch);
//        	  ps.setString(7, category);
//        	  ps.setString(8, branch);
//        	  ps.setString(9, department);
//          }            
//       
//          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("CATEGORY_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("DATE_PURCHASED") && !qryfilter.contains("ASSET_ID")){
////        	  System.out.println("<========getDisposedAssetList=======>5");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, category);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, department);
//        	  ps.setString(5, dFromDate);
//        	  ps.setString(6, toDate);
//          }  
//          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("DEPT_ID") && qryfilter.contains("DATE_PURCHASED") && !qryfilter.contains("CATEGORY_ID")){
////        	  System.out.println("<========getDisposedAssetList=======>6");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, department);
//        	  ps.setString(4, dFromDate);
//        	  ps.setString(5, toDate);
//          }          
//     
//          if(qryfilter.contains("BRANCH_ID") && qryfilter.contains("CATEGORY_ID") && qryfilter.contains("ASSET_ID")){
////        	  System.out.println("<========getDisposedAssetList=======>9");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, category);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, asset_Id);
//          }             
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
               // String partialType = rs.getString("PARTIAL_TYPE");
     //           double partialPercent = rs.getInt("PARTIAL_PERCENT");
                double partialAmt = rs.getDouble("PARTIAL_AMT");
                
                dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                       amount,
                                       raiseEntry, profitLoss, disposalDate,
                                       effDate, userId, status,
                                       branchId, deptId, categoryId, regNo,
                                       desc, cost, assetUser, assetStatus);
						               dispose.setPartialAmt(partialAmt);
						     //          dispose.setPartialPercent(partialPercent);
				//		               dispose.setDisposalType(partialType);   
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
                       " A.OLD_SECTION,D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A,NEW_SBU_CODE," +
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
            con = getConnection("legendPlus");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId, sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost, "",
                                        regNo, effDate, categoryId, assetUser,
                                        assetStatus,newsbucode);

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
            con = getConnection("legendPlus");
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

        String query = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,A.OLD_SECTION,"
        		+ "D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.NEW_SBU_CODE,New_branch_id,New_dept_id,New_Section,"
        		+ "NEW_BRANCH_CODE,NEW_DEPT_CODE,NEW_SECTION_CODE,New_Asset_user,A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,"
        		+ "E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED "
        		+ "FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E "
        		+ "WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID "
        		+ "AND A.ASSET_ID = E.OLD_ASSET_ID AND  A.ASSET_ID = '" + id +
                  "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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
                String sbucode = rs.getString("NEW_SBU_CODE");
                int newBranchId = rs.getInt("New_branch_id");
                int newSectionId = rs.getInt("New_Section");
                int newDeptId = rs.getInt("New_dept_id");
                String newAssetUser = rs.getString("New_Asset_user");
                String newBranchCode = rs.getString("NEW_BRANCH_CODE");
                String newDeptCode = rs.getString("NEW_DEPT_CODE");
                String newSectionCode = rs.getString("NEW_SECTION_CODE");
                
                //String assetUser = rs.getString("ASSET_USER");
                //String assetStatus = rs.getString("ASSET_STATUS");

                transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId, sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, "", "",sbucode);
                transfer.setBranchId(newBranchId);
                transfer.setSectionId(newSectionId);
                transfer.setDeptId(newDeptId);
                transfer.setAssetUser(newAssetUser);
                transfer.setBranchCode(newBranchCode);
                transfer.setDeptCode(newDeptCode);
                transfer.setSectionCode(newSectionCode);
                
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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

    /*
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
            con = getConnection("legendPlus");

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
           // ps.setDouble(1, cost);
           // ps.setDouble(2, nbv);
           // ps.setDouble(3, vatableCost);
           // ps.setDouble(4, vatAmt);
           // ps.setDouble(5, whtAmt);
           // ps.setDouble(6, accumDep);
           // ps.setString(7, assetId);

           // i = ps.executeUpdate();
            
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
*/
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
            con = getConnection("legendPlus");

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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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

            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
                      "USER_ID,RAISE_ENTRY,EFFDATE,approval_status AS PAYMENTSTATUS FROM AM_ASSET_DEP_ADJUSTMENT "+
                      "WHERE ASSET_ID = ?";


       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

       ArrayList list = new ArrayList();
       //declare DTO object
       DepAdjustment adjust = null;

       try
       {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, id);
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
         String paymentStatus = rs.getString("PAYMENTSTATUS");
         adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
                      raiseEntry,adjustDate,effDate,userId,0,0,0,"","","","",paymentStatus);

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
  		 con = getConnection("legendPlus");
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
  		 con = getConnection("legendPlus");

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
  		                                 double newNbv,String reason,String adjustDate,String effDate,int userId,String raiseEntry,String mtid,String paymentStatus)
    {
  		//String updateQuery = "UPDATE AM_ASSET SET ACCUM_DEP = ACCUM_DEP + ?,"+
  		//	" NBV = ?, DEP_YTD = DEP_YTD + ? WHERE ASSET_ID = ?";

        //System.out.println(" I am inside the insertAssetDepAdjustment method ()//////////////////");
  		String insertQuery = "INSERT INTO AM_ASSET_DEP_ADJUSTMENT(ASSET_ID,ACCUM_DEP,NEW_ACCUM_DEP,NBV,NEW_NBV,REASON,"+
  			                   "ADJUST_DATE,EFFDATE,USER_ID,RAISE_ENTRY,ID,PAYMENTSTATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

  		Connection con = null;
  		PreparedStatement ps = null;
      boolean result = false;
      //boolean autoCommit = false;

  		try
  		{
  		 con = getConnection("legendPlus");
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
       ps.setString(12, paymentStatus);
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

      public ArrayList getDepAdjustmentAssetList(String assetID,String category,String branch,String department,String regNumber,String asset_Id,String fromDate,String toDate)
      {
    	  df = new com.magbel.util.DatetimeFormat();
//
//       String query = "SELECT A.ID,A.ASSET_ID,A.accum_dep,A.new_accum_dep,A.nbv,A.new_nbv,"+
//                        " A.reason,A.adjust_date,A.effDate,A.USER_ID,A.RAISE_ENTRY,"+
//                        " B.BRANCH_ID,B.DEPT_ID,"+
//                        " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS,A.PAYMENTSTATUS"+
//                        " FROM AM_ASSET_DEP_ADJUSTMENT A,AM_ASSET_MAIN B "+
//                        " WHERE A.ASSET_ID = B.ASSET_ID "+query_;
    	  StringBuilder str3 = new StringBuilder(assetID.toString());
    	  String street3 =str3.toString();  
    
    		  
       StringBuilder query = new StringBuilder("SELECT A.ID,A.ASSET_ID,A.accum_dep,A.new_accum_dep,A.nbv,A.new_nbv,"+
               " A.reason,A.adjust_date,A.effDate,A.USER_ID,A.RAISE_ENTRY,"+
               " B.BRANCH_ID,B.DEPT_ID,"+
               " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS,A.approval_status AS PAYMENTSTATUS"+
               " FROM AM_ASSET_DEP_ADJUSTMENT A,AM_ASSET_MAIN B "+
               " WHERE A.ASSET_ID = B.ASSET_ID "); 
//       System.out.println(" query_ I am inside the getDepAdjustmentAssetList method: "+assetID+"    assetID Length: "+assetID.length());
      query.append(assetID);
//       System.out.println(" I am inside the getDepAdjustmentAssetList method: "+query);
         Connection con = null;
         PreparedStatement ps = null;
//         ResultSet rs = null; 

         Statement stmt = null;
        ResultSet rs = null;
        CallableStatement cstmt = null;
        
         ArrayList list = new ArrayList();
         //declare DTO object
         DepAdjustment adjust = null;

         try
         {
          con = getConnection("legendPlus");
          ps = con.prepareStatement(query.toString());
          //query_ = query_.replaceAll("\\s", "");
          if(assetID.contains("CATEGORY_ID")){
        	  ps.setString(1, category);
          }
          if(assetID.contains("CATEGORY_ID") && assetID.contains("BRANCH_ID") && assetID.contains("DEPT_ID")){
        	  ps.setString(1, category);
        	  ps.setString(2, branch);
        	  ps.setString(3, department);
          }
          if(assetID.contains("CATEGORY_ID") && assetID.contains("BRANCH_ID") && assetID.contains("DEPT_ID") && assetID.contains("ADJUST_DATE")){
        	  ps.setString(1, category);
        	  ps.setString(2, branch);
        	  ps.setString(3, department);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
          }
          if(assetID.contains("ASSET_ID")){
        	  ps.setString(1, asset_Id);
          }
          if(assetID.contains("BRANCH_ID")){
        	  ps.setString(1, branch);
          }     
          if(assetID.contains("DEPT_ID")){
        	  ps.setString(1, department);
          }
          if(assetID.contains("ADJUST_DATE")){
        	  ps.setString(1, fromDate);
        	  ps.setString(2, toDate);
          }
//          System.out.println(" We are here 1..." );
          rs = ps.executeQuery();
          while (rs.next())
          {
 //       	  System.out.println(" We are here 2..." );
           int adjustId = rs.getInt("ID");
           String assetId = rs.getString("ASSET_ID");
//           System.out.println(" We are here with assetId..." +assetId);
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
           String paymentStatus = rs.getString("PAYMENTSTATUS");
           
           adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
                      raiseEntry,adjustDate,effDate,userId,branchId,deptId,
                         categoryId,regNo,desc,assetUser,assetStatus,paymentStatus);

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
  con = getConnection("legendPlus");

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

          String query = "SELECT A.REVALUE_ID,A.ASSET_ID,A.COST_INCREASE,A.REVALUE_REASON,A.REVALUE_DATE, "
          		+ "A.USER_ID,A.RAISE_ENTRY,A.R_VENDOR_AC,A.COST_PRICE,A.VATABLE_COST,A.VAT_AMOUNT, "
          		+ "A.WHT_AMOUNT,A.NBV,A.ACCUM_DEP,A.OLD_COST_PRICE,A.OLD_VATABLE_COST,A.OLD_VAT_AMOUNT, "
          		+ "A.OLD_WHT_AMOUNT,A.OLD_NBV,A.OLD_ACCUM_DEP,A.EFFDATE,B.BRANCH_ID,B.DEPT_ID, "
          		+ "B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS "
          		+ "FROM am_asset_improvement A,AM_ASSET_MAIN B "
          		+ "WHERE A.ASSET_ID = B.ASSET_ID "
          		+ "AND A.STATUS = 'IMPROVED' "
          		+ query_
          		+ " UNION "
          		+ "SELECT A.REVALUE_ID,A.ASSET_ID,A.COST_INCREASE,A.REVALUE_REASON,A.REVALUE_DATE, "
          		+ "A.USER_ID,A.RAISE_ENTRY,A.R_VENDOR_AC,A.COST_PRICE,A.VATABLE_COST,A.VAT_AMOUNT, "
          		+ "A.WHT_AMOUNT,A.NBV,A.ACCUM_DEP,A.OLD_COST_PRICE,A.OLD_VATABLE_COST,A.OLD_VAT_AMOUNT, "
          		+ "A.OLD_WHT_AMOUNT,A.OLD_NBV,A.OLD_ACCUM_DEP,A.EFFDATE,B.BRANCH_ID,B.DEPT_ID, "
          		+ "B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS "
          		+ "FROM AM_GROUP_IMPROVEMENT A,AM_ASSET_MAIN B "
          		+ "WHERE A.ASSET_ID = B.ASSET_ID "
          		+ "AND A.STATUS = 'IMPROVED'" 
          		+ query_ 
          		+ " ORDER BY REVALUE_DATE DESC";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
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
      public void deleteAssetRevalueMaintenance(String id) {
          String query = "DELETE FROM am_asset_revaluation WHERE ASSET_ID = '" + id +
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
con = getConnection("legendPlus");

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
      public Improvement getMaintenanceAsset(String id) {

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
          Improvement improve = null;

          try {
              con = getConnection("legendPlus");
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

                  improve = new Improvement(revalueId, assetId, costIncrease,
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

          return improve;
      }
      public Revaluation getRevalueMaintenanceAsset(String id) {

          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE FROM am_asset_revaluation " +
                  "WHERE ASSET_ID = '" + id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
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
              System.out.println("INFO:Error fetching am_asset_revaluation Asset by ID ->" +
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
            con = getConnection("legendPlus");


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
            con = getConnection("legendPlus");


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

//     System.out.println("#######################about to update am_asset with  new dept_id "+dept_id+"" +
//             " new branch_id "+branch_id+" new section_id"+section_id);
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
            con = getConnection("legendPlus");


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

//    public void updateWIPReclassification(String id)
    public void updateWIPReclassification(String id,int totallife,String newDepRate,String dependdate)
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
        double depRateValue = Double.parseDouble(newDepRate);
        
        String query1 = "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE,NEW_CAT_ID,NEW_CAT_CODE,effDate from AM_WIP_RECLASSIFICATION where asset_id = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                "SECTION_CODE=?,DEPT_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=? where asset_id ='" + id + "'";

//        String updateQuery = "update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, " +
//                "BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,Effective_Date=?" +
//                //"WHO_TO_REM=?,EMAIL1=?,WHO_TO_REM_2=?,EMAIL2=?" +
//                " where asset_id ='" + id + "'";
        String updateQuery = "update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?,BRANCH_CODE=?, DEPT_CODE=?, " +
                "SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,DEP_RATE=?,Effective_Date=?,Who_TO_Rem=?,Who_To_Rem_2=?,Email1=?," +
                "Email2=?,total_life=?,remaining_life=?,Dep_End_Date=? " +
               " where asset_id =? ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            con = getConnection("legendPlus");
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

//            ps = con.prepareStatement(updateQuery);
//            ps.setInt(1, dept_id);
//            ps.setInt(2, branch_id);
//            ps.setInt(3, section_id);
//            ps.setString(4, asset_user);
//            ps.setString(5, branch_code);
//            ps.setString(6, dept_code);
//            ps.setString(7, section_code);
//            ps.setInt(8, category_id);
//            ps.setString(9, category_code);
//            ps.setDate(10, effDate);
//           // ps.setString(10, who_to_rem);
//            //ps.setString(11, e_mail1);
//            //ps.setString(12, who_to_rem2);
//            //ps.setString(13, e_mail2);

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
            ps.setDouble(10, depRateValue);
            ps.setDate(11, effDate);
            ps.setString(12, who_to_rem);
            ps.setString(13, who_to_rem2);
            ps.setString(14, e_mail1);            
            ps.setString(15, e_mail2);
            ps.setInt(16, totallife);
            ps.setInt(17, totallife);
            ps.setString(18, dependdate);
            ps.setString(19, id);
//            System.out.println("updating  am_asset with  new dept_id = " + dept_id + "" +
//                    " new branch_id = " + branch_id + " new section_id = " + section_id +
//                    " new Category_id = " +category_id );

         //stop update on am_asset
           // i = ps.executeUpdate();

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

     public void updateWIPReclassification(String id,String otherID,int totallife,String newDepRate,String dependdate)
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
        double depRateValue = Double.parseDouble(newDepRate);

        String query1 = "select New_dept_id,New_branch_id,New_Section,New_Asset_user,new_who_to_rem," +
                "new_email1,new_who_to_rem2,new_email2,NEW_BRANCH_CODE,NEW_SECTION_CODE," +
                "NEW_DEPT_CODE,NEW_CAT_ID,NEW_CAT_CODE,effDate from AM_WIP_RECLASSIFICATION where asset_id = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID=?,BRANCH_ID=?,BRANCH_CODE=?," +
                "SECTION_CODE=?,DEPT_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=? where asset_id ='" + id + "'";

        String updateQuery = "update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?,BRANCH_CODE=?, DEPT_CODE=?, " +
                "SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,DEP_RATE=?,Effective_Date=?,Who_TO_Rem=?,Who_To_Rem_2=?,Email1=?," +
                "Email2=?,old_asset_id=?,asset_id=?,total_life=?,remaining_life=?,Dep_End_Date=? " +
               " where asset_id =? ";
               // " where asset_id ='" + id + "'";
//        System.out.println("====updateQuery in updateWIPReclassification: "+updateQuery);
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try
        {
            con = getConnection("legendPlus");
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
            ps.setDouble(10, depRateValue);
            ps.setDate(11, effDate);
            ps.setString(12, who_to_rem);
            ps.setString(13, who_to_rem2);
            ps.setString(14, e_mail1);            
            ps.setString(15, e_mail2);
            ps.setString(16, id);
            ps.setString(17, otherID);
            ps.setInt(18, totallife);
            ps.setInt(19, totallife);
            ps.setString(20, dependdate);
            ps.setString(21, otherID);

//            System.out.println("updating  am_asset with  new dept_id = " + dept_id + "" +
//                    " new branch_id = " + branch_id + " new section_id = " + section_id +
//                    " new Category_id = " +category_id+" branch_code= "+branch_code+" dept_code= "+dept_code+" category_code= "+category_code+" newDepRate= "+newDepRate+" otherID= "+otherID);

           //stop update on am_asset
            i = ps.executeUpdate();
//            System.out.println("=====Execute Update Querry Result: "+i);
            ps1 = con.prepareStatement(updateFleetMaster);
            ps1.setInt(1, dept_id);
            ps1.setInt(2, branch_id);
            ps1.setString(3, code.getBranchCode(String.valueOf(branch_id)));
            ps1.setString(4, code.getSectionCode(String.valueOf(section_id)));
            ps1.setString(5, code.getDeptCode(String.valueOf(dept_id)));
            ps1.setInt(6, category_id);
            ps1.setString(7, code.getCategoryCode(String.valueOf(category_id)));

            i = ps1.executeUpdate();
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
            closeConnection(con, ps1, rs);
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
            con = getConnection("legendPlus");


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
            con = getConnection("legendPlus");


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

/*
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

            con = getConnection("legendPlus");
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
*/

/*

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
            con = getConnection("legendPlus");

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

*/
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
  		                                 double newNbv,String reason,String adjustDate,String effDate,int userId,String raiseEntry,String mtid,String paymentStatus)
    {
  		String updateQuery = "UPDATE AM_ASSET SET ACCUM_DEP = ACCUM_DEP + ?,"+
  			" NBV = ?, DEP_YTD = DEP_YTD + ? WHERE ASSET_ID = ?";

        //System.out.println(" I am inside the insertAssetDepAdjustment method ()//////////////////");
  		String insertQuery = "INSERT INTO AM_ASSET_DEP_ADJUSTMENT(ASSET_ID,ACCUM_DEP,NEW_ACCUM_DEP,NBV,NEW_NBV,REASON,"+
  			                   "ADJUST_DATE,EFFDATE,USER_ID,RAISE_ENTRY,ID,PAYMENTSTATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

  		Connection con = null;
  		PreparedStatement ps = null;
      boolean result = false;
      //boolean autoCommit = false;

  		try
  		{
  		 con = getConnection("legendPlus");
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
       ps.setString(12, paymentStatus);
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
  con = getConnection("legendPlus");

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
                       " A.OLD_SECTION,A.OLD_ASSET_USER,A.OLD_SBU_CODE,A.RAISE_ENTRY,A.TRANSFER_DATE,new_dept_id,new_section,new_branch_id," +
                       " A.NEW_SBU_CODE,A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
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
            con = getConnection("legendPlus");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                String oldsbucode = rs.getString("OLD_SBU_CODE");

                 transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId,sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, "", "",newBranch_id,newDept_id,newSection_id,newsbucode,oldsbucode);



                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID in getTransferedAsset2 ->" +
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

//        System.out.println("query in getWIPReclassificationAsset >>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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
/*
        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
                "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
                "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
                " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E," +
                " AM_AD_SECTION D WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
                "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_ID = E.OLD_ASSET_ID AND A.ASSET_ID = '" + id + "'";
*/
        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
                "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
                "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
                " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_ASSET_MAIN E," +
                " AM_AD_SECTION D WHERE A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
                "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_ID = E.ASSET_ID AND A.ASSET_ID = '" + id + "'";

//        System.out.println("query in getWIPReclassificationAsset2 >>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int transferId = rs.getInt("TRANSFER_ID");
                String assetId = rs.getString("ASSET_ID");
                int oldDeptId = rs.getInt("OLD_DEPT_ID");
//                String deptName = rs.getString("DEPT_NAME");
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
                                        "", oldBranchId, branchName,
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
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
               old_section = rs.getInt("old_section");
               new_section = rs.getInt("new_section");

                        }

            if(old_section == 0){
//                System.out.println("the old_section value is 0 >>>> old_section "+old_section+">>>> new_section" +new_section);
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
        String query = "SELECT DISTINCT A.TRANSFER_ID,A.ASSET_ID,A.new_DEPT_ID,B.DEPT_NAME,A.new_BRANCH_ID,C.BRANCH_NAME, "
        		+ "A.new_SECTION,'0' AS SECTION_NAME,A.new_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.NEW_SBU_CODE, "
        		+ "A.USER_ID,A.EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO, "
        		+ "E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS "
        		+ "FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E "
        		+ "WHERE A.new_DEPT_ID = B.DEPT_ID "
        		+ "AND A.new_BRANCH_ID = C.BRANCH_ID "
        		+ "AND A.ASSET_ID = E.OLD_ASSET_ID  "
        		+ "AND APPROVAL_STATUS ='POSTED' "
        		+ query_
        		+ " UNION "
        		+ "SELECT DISTINCT A.TRANSFERBY_ID,A.ASSET_ID,A.newDEPT_ID,B.DEPT_NAME,A.newBRANCH_ID,C.BRANCH_NAME, "
        		+ "A.newsection_id,'0' AS SECTION_NAME,A.newASSET_USER,'' AS RAISE_ENTRY,A.TRANSFER_DATE,A.NEWSBU_CODE, "
        		+ "'' AS USER_ID,A.TRANSFER_DATE AS EFFDATE,E.CATEGORY_NAME,E.BRANCH_ID,E.DEPT_ID,E.CATEGORY_ID,E.REGISTRATION_NO, "
        		+ "E.DESCRIPTION,E.COST_PRICE,E.ASSET_USER,E.ASSET_STATUS "
        		+ "FROM am_gb_bulkTransfer A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E "
        		+ "WHERE A.newDEPT_ID = B.DEPT_ID "
        		+ "AND A.newBRANCH_ID = C.BRANCH_ID "
        		+ "AND A.ASSET_ID = E.OLD_ASSET_ID "
        		+ "AND A.STATUS ='POSTED' "
        		+ query_
        		+ "ORDER BY TRANSFER_DATE DESC";
       // System.out.println("query >>>>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId, sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost, "",
                                        regNo, effDate, categoryId, assetUser,
                                        assetStatus,newsbucode);

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
//        System.out.println("query >>>>>> " + query);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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

                transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId, sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost, "",
                                        regNo, effDate, categoryId, assetUser,
                                        assetStatus,"");

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
            con = getConnection("legendPlus");
            ps = con.prepareStatement(oldAssetQuery);
            //rs = ps.executeQuery();



            rs=ps.executeQuery();

             while (rs.next()) {
               old_asset_id = rs.getString("old_asset_id");

            }

//            System.out.println("the old asset id is @@@@@@@@@@@@ "+old_asset_id);



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
            String query = "select old_branch_id, old_dept_id, old_section, old_sbu_code, old_asset_user " +
                    "from am_assettransfer where asset_id ='"+old_asset_id+"'";
//           System.out.println("the query for getTransferedOldAssetID @@@@@@@@@@@@@@@@@@@ "+query );
           con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();





           // rs = ps.executeQuery(query);


            while (rs.next()) {
                result[0] = Integer.toString(rs.getInt("old_branch_id"));
                result[1] = Integer.toString(rs.getInt("old_dept_id"));
                result[2] = Integer.toString(rs.getInt("old_section"));
                result[3] = rs.getString("old_sbu_code");
                result[4] = rs.getString("old_asset_user");

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
//           System.out.println("the query >>>> "+query );
           con = getConnection("legendPlus");
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
//            System.out.println("the query for getReclassedOldAssetID @@@@@@@@@@@@@@@@@@@ " + query);
            con = getConnection("legendPlus");
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
//                 System.out.println("the value of a is " +result[j]);

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
        System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");

        String query1= "select new_category_id, new_depr_rate from AM_ASSETRECLASSIFICATION where asset_id = '"+id+"'";

        String updateQuery = "UPDATE AM_ASSET SET category_id = ?," +
                            " dep_rate = ? WHERE ASSET_ID = ?";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("legendPlus");


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

              //stop update on am_asset
               //i = ps.executeUpdate();





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
  con = getConnection("legendPlus");

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
              con = getConnection("legendPlus");
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
  con = getConnection("legendPlus");

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
            con = getConnection("legendPlus");
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
              con = getConnection("legendPlus");
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
                  "SELECT max(revalue_id) from am_asset_revaluation " +
                  "WHERE ASSET_ID = '" + id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;
          String  revalueId="";
          try {
              con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");


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
            con = getConnection("legendPlus");


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
            con = getConnection("legendPlus");


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
  con = getConnection("legendPlus");

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
              String effDate,String mtid,String wh_tax_cb, double selectTax,String subject_to_vat,
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
  con = getConnection("legendPlus");

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
  ps.setDouble(23, selectTax);
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
                  "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID," +
                  "OLD_VENDOR_ACC,OLD_VENDOR_ID,INTEGRIFY,IMPROV_USEFULLIFE,SBU_CODE "+
                  "FROM am_asset_improvement " +
                  "WHERE ASSET_ID = '" + id + "' AND REVALUE_ID = "+mtid;
          //System.out.println("getMaintenanceAsset query: "+query);
          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
  
          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
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
                  String integrify = rs.getString("INTEGRIFY");
                  int usefullife = rs.getInt("IMPROV_USEFULLIFE");
                  String sbuCode = rs.getString("SBU_CODE");
                  revalue = new Revaluation(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "",integrify,usefullife);
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
               revalue.setSbuCode(sbuCode);;
              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching am_asset_improvement Asset by ID ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
 }

 public Revaluation getRevalueMaintenanceAsset(String id, int mtid) {

          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
                  "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID "+
                  "FROM am_asset_revaluation " +
                  "WHERE ASSET_ID = '" + id + "' AND REVALUE_ID = "+mtid;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
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

public void updateAssetMaintenance(String id,int mtid,int usefullife,String newimprovement){

       double cost_price = 0;
       double nbv = 0;
       double vatable_cost = 0;
       double vat_amount = 0;
       double wht_amount = 0;
       double accum_dep = 0;
       String wh_tax = "";   
       int wht_percent = 0;   
       String subject_to_vat = "";
       double oldimprovAccumDep = 0;
       double vatable_cst = 0;
       double cst = 0;
       double n_b_v = 0;
       double vat_Amt = 0;
       double wht_Amt =0;
       int supplier_id=0;
       String vendorAc="";
       String effDate = "";
       //nbv += cost;
        //costPrice += cost;
        int i = 0;
//        System.out.println("i am in update asset Imprrovement in updateAssetMaintenance UUUUUUUUUUUUUUUUUUUU");
//        System.out.println("Improvement Id: "+mtid);
        String updateQuery = "";
        String updateQuery1 = "";
        String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
                "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,R_VENDOR_AC,EFFDATE,OLDIMPROV_ACCUMDEP "+
                "from am_asset_improvement where asset_id = '"+id+"' and revalue_id= "+mtid;
if(!newimprovement.equalsIgnoreCase("P")){
if(usefullife ==0){
//	System.out.println("About to set query for usefullife equal Zero ");
         updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?,ACCUM_DEP= ?, "+
                "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=?, "+
                "TOTAL_NBV = NBV + ? "+
                "WHERE ASSET_ID = ?";        
} 
else{
//	System.out.println("About to set query for usefullife not equal Zero ");
    		updateQuery1 = "UPDATE AM_ASSET SET IMPROV_COST = coalesce(IMPROV_COST,0)+?,TOTAL_NBV = (coalesce(IMPROV_NBV,0)+NBV) + ?, " +
            "IMPROV_NBV = coalesce(IMPROV_NBV,0)+?, IMPROV_VATABLECOST = coalesce(IMPROV_VATABLECOST,0) + ?, VAT = VAT+ ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT+?, "+
            "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=?, "+
            "IMPROV_REMAINLIFE= coalesce(IMPROV_REMAINLIFE,0) + ?, IMPROV_TOTALLIFE = coalesce(IMPROV_TOTALLIFE,0) +?, "+
            "IMPROV_EffectiveDate = ?,IMPROV_ENDDATE = (SELECT DATEADD(month, "+usefullife+", ?)) "+
            "WHERE ASSET_ID = ?";	
}
String updateQuery2= "UPDATE AM_ASSET_IMPROVEMENT SET IMPROVED = 'P' WHERE ASSET_ID = '"+id+"' and REVALUE_ID = "+mtid;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            con = getConnection("legendPlus");
  //          System.out.println("About to run query ");

            ps = con.prepareStatement(query1);
              rs = ps.executeQuery();

            while (rs.next()) {
                cost_price = rs.getDouble(1);
                nbv = rs.getDouble(2);
                vatable_cost = rs.getDouble(3);
                vat_amount = rs.getDouble(4);
                wht_amount = rs.getDouble(5);
             //   accum_dep = rs.getDouble(6);
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
                effDate = rs.getString(17);
                oldimprovAccumDep = rs.getDouble(18);
            }
            double totnbv = cost_price + nbv;
            if(usefullife ==0){
            ps = con.prepareStatement(updateQuery);
 //           System.out.println("updateQuery: "+updateQuery);
            ps.setDouble(1, cost_price);
            ps.setDouble(2, cost_price);
            ps.setDouble(3, vatable_cost);
            ps.setDouble(4, vat_Amt);
            ps.setDouble(5, wht_Amt);
            ps.setDouble(6, accum_dep);
            ps.setString(7, wh_tax);
            ps.setInt(8, wht_percent);
            ps.setString(9, subject_to_vat);
            ps.setInt(10, supplier_id);
            ps.setString(11, vendorAc);
            ps.setDouble(12, cost_price);
            ps.setString(13, id);
               i = ps.executeUpdate();
            }else{
           ps = con.prepareStatement(updateQuery1);
 //          System.out.println("updateQuery1: "+updateQuery1);
           ps.setDouble(1, cost_price);
           ps.setDouble(2, cost_price);
           ps.setDouble(3, cost_price);
           ps.setDouble(4, vatable_cost);
           ps.setDouble(5, vat_Amt);
           ps.setDouble(6, wht_Amt);
 //          ps.setDouble(7, accum_dep);
           ps.setString(7, wh_tax);
           ps.setInt(8, wht_percent);
           ps.setString(9, subject_to_vat);
           ps.setInt(10, supplier_id);
           ps.setString(11, vendorAc);
           ps.setInt(12, usefullife);
           ps.setInt(13, usefullife);
           ps.setString(14, effDate);
           ps.setString(15, effDate);
           ps.setString(16, id);
              i = ps.executeUpdate();  
            }
            ps1 = con.prepareStatement(updateQuery2);
               i = ps1.executeUpdate();
        } catch (Exception e) {
            String warning = "WARNING:Asset Manager class: Error inserting Asset IMprovement ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps,rs);
            closeConnection(con, ps1,rs);
        }
}
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
//     System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");
     String updateQuery = "";
     String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
             "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,R_VENDOR_AC "+
             "from am_asset_improvement where asset_id = '"+id+"' and revalue_id= "+mtid;
    /* 
     	updateQuery = "UPDATE AM_ASSET SET COST_PRICE = ?," +
         " NBV =?, VATABLE_COST = ?, VAT = ?, WH_TAX_AMOUNT = ?, ACCUM_DEP = ?, "+
         "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=? "+
         "WHERE ASSET_ID = ?";
     	*/
        updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? "+
                "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=?, "+
                "TOTAL_NBV = NBV + ? "+
                "WHERE ASSET_ID = ?";        
     	
   	String updateQuery2= "UPDATE AM_ASSET_IMPROVEMENT SET IMPROVED = 'P' WHERE ASSET_ID = '"+id+"' and REVALUE_ID = "+mtid;
     Connection con = null;
     PreparedStatement ps = null;
     PreparedStatement ps1 = null;
     ResultSet rs = null;
     try {
         con = getConnection("legendPlus");


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
/*         System.out.println("=====================11 wh_tax " + wh_tax);
         System.out.println("===================== wht_percent " + wht_percent);
         System.out.println("===================== subject_to_vat " + subject_to_vat);
         System.out.println("===================== vatable_cst " + vatable_cst);
         System.out.println("===================== cst " + cst);
         System.out.println("===================== n_b_v " + n_b_v);
         System.out.println("===================== vat_Amt " + vat_Amt);
         System.out.println("===================== wht_Amt " + wht_Amt);  */
         //////update am_asset table with approve values

        ps = con.prepareStatement(updateQuery);
        ps.setDouble(1, cost_price);
        ps.setDouble(2, cost_price);
        ps.setDouble(3, vatable_cost);
        ps.setDouble(4, vat_Amt);
        ps.setDouble(5, wht_Amt);
        ps.setDouble(6, accum_dep);
        ps.setString(7, wh_tax);
        ps.setInt(8, wht_percent);
        ps.setString(9, subject_to_vat);
        ps.setInt(10, supplier_id);
        ps.setString(11, vendorAc);
        ps.setDouble(12, cost_price);
        ps.setString(13, id);
           i = ps.executeUpdate();

         ps1 = con.prepareStatement(updateQuery2);
         i = ps1.executeUpdate();            
     } catch (Exception e) {
         String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                          e.getMessage();
         System.out.println(warning);
         e.printStackTrace();
     } finally {
         closeConnection(con, ps,rs);
     }
}
public void updateAssetRevaluation(String id,int mtid){

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
     int i = 0;

     String query1= "select COST_PRICE,NBV,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,ACCUM_DEP,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT, "+
             "NEW_VATABLE_COST,NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT,R_VENDOR_ID,R_VENDOR_AC "+
             "from am_asset_revaluation where asset_id = '"+id+"' and revalue_id= "+mtid;

     String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = ?," +
                         " NBV =?, VATABLE_COST = ?, VAT = ?, WH_TAX_AMOUNT = ?, ACCUM_DEP = ?, "+
                         "WH_TAX =?, WHT_PERCENT = ?, SUBJECT_TO_VAT =?,SUPPLIER_NAME=?,VENDOR_AC=? "+
                         "WHERE ASSET_ID = ?";
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
         con = getConnection("legendPlus");


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
         }
         ps = con.prepareStatement(updateQuery);
     } catch (Exception e) {
         String warning = "WARNING:Asset Manager class: Error inserting Asset Revaluation ->" +
                          e.getMessage();
         System.out.println(warning);
         e.printStackTrace();
     } finally {
         closeConnection(con, ps,rs);
     }
}

public Improvement getMaintenanceAssetRepost(String id) {

  String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,INTEGRIFY FROM am_asset_improvement " +
                  "WHERE REVALUE_ID = '" + revalue_id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
//System.out.println("getMaintenanceAssetRepost query: "+query);
          ArrayList list = new ArrayList();
          //declare DTO object
          Improvement improve = null;

          try {
              con = getConnection("legendPlus");
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
                  String integrify = rs.getString("INTEGRIFY");
                  String subjectTovat = rs.getString("Subject_to_vat");
                  String whtTax = rs.getString("WH_tax");
                  improve = new Improvement(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", "", "", "");
                  improve.setOldVendorId(oldVendorId);
                  improve.setOldVendorAcc(oldVendorAcc);
                  improve.setNewVendorId(rVendorId);
                  improve.setIntegrify(integrify);
                  improve.setSubject_to_vat(subjectTovat);
                  improve.setWh_tax(whtTax);
              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaintenanceAssetRepost  ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return improve;
      }


public Revaluation getRevaluationAssetRepost(String id) {

  String improve_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID FROM am_asset_revaluation " +
                  "WHERE REVALUE_ID = '" + improve_id + "'";

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
              ps = con.prepareStatement(query);
              rs = ps.executeQuery();

              while (rs.next()) {
                  int revalueId = rs.getInt("REVALUE_ID");
                  String assetId = rs.getString("ASSET_ID");
                  double costIncrease = rs.getDouble("COST_INCREASE");
                  String reason = rs.getString("REVALUE_REASON");
                  String revalueDate = formatDate(rs.getDate("REVALUE_DATE"));
//                  System.out.println("revalueDate: "+revalueDate);
                  int userId = rs.getInt("USER_ID");
//                  System.out.println("userId: "+userId);
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
//                  System.out.println("effDate: "+effDate);
                  int rVendorId = rs.getInt("R_VENDOR_ID");
//                  System.out.println("rVendorId: "+rVendorId);
                  String oldVendorAcc = rs.getString("OLD_VENDOR_ACC");
//                  System.out.println("oldVendorAcc: "+oldVendorAcc);
                  int oldVendorId = rs.getInt("OLD_VENDOR_ID");
//                  System.out.println("oldVendorId: "+oldVendorId);
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
              System.out.println("INFO:Error fetching getRevaluationAssetRepost  ->" +
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
            con = getConnection("legendPlus");
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
                       " A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.NEW_SBU_CODE,new_dept_id,new_section,new_branch_id," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED, " +
                       " E.NBV,E.ACCUM_DEP,E.Monthly_Dep,A.OLD_SBU_CODE "+
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
            con = getConnection("legendPlus");
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
                //String oldsbucode = rs.getString("OLD_SBU_CODE");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                String oldsbucode = rs.getString("OLD_SBU_CODE");
                  

                 transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId,sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, "", "",newBranch_id,newDept_id,newSection_id,oldsbucode,newsbucode);

                       transfer.setNbv(nbv);
                       transfer.setAccumDep(accumDep);
                       transfer.setMonthDep(montDep);
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


public Transfer getTransferedAssetTwoRepost(String id)
    {
        checkOldSection(id);

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,D.SECTION_NAME," +
                       " A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.NEW_SBU_CODE,new_dept_id,new_section,new_branch_id," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED, " +
                       " E.NBV,E.ACCUM_DEP,E.Monthly_Dep,A.OLD_SBU_CODE "+
                       " FROM AM_ASSET2TRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET2_MAIN E, AM_AD_SECTION D" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID" +
                       " AND A.ASSET_ID = E.OLD_ASSET_ID AND A.ASSET_ID = '" + id + "'";
//System.out.println("<<<<<<<======query in getTransferedAssetTwoRepost: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
       Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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
                //String oldsbucode = rs.getString("OLD_SBU_CODE");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                String oldsbucode = rs.getString("OLD_SBU_CODE");
                  

                 transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId,sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, "", "",newBranch_id,newDept_id,newSection_id,oldsbucode,newsbucode);

                       transfer.setNbv(nbv);
                       transfer.setAccumDep(accumDep);
                       transfer.setMonthDep(montDep);
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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");


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

//     System.out.println("#######################about to update am_asset with  new dept_id "+dept_id+"" +
//             " new branch_id "+branch_id+" new section_id"+section_id);
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
                                   String email2, String effDate,String mtid,String categoryCode,
                                   String description,double cost,double nbv,double monthDep,double accumDep,
                                   int assetCode,String oldsbucode, String newsbucode) {
        int i = 0;

        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE," +
                             "NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION," +
                             "Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("<<<<<<<<<<<<<<<MATANMI TESTING>>>>>>>>");
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("legendPlus");
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
            ps.setInt(27,assetCode);
            ps.setString(28, oldsbucode);
            ps.setString(29, newsbucode);
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
                                   double cost, double nbv,double monthDep,double accumDep, int assetCode,String oldsbucode, String newsbucode) {
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
                             "',SBU_CODE = '" +newsbucode +                             
                             "' WHERE ASSET_ID = '" + assetId + "'";


        String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,"
+
                "OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE)" +
                 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("legendPlus");
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
            ps.setInt(27,assetCode);
//            System.out.println("insertAssetTransferNoSupervisor oldsbucode: "+oldsbucode+"  newsbucode: "+newsbucode);
            ps.setString(28, oldsbucode);
            ps.setString(29, newsbucode);
            i = ps.executeUpdate();
            
//            System.out.println("transfer i : " + i);

            if (i > 0) {
//            	 System.out.println("We are here : ");
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

public int insertAssetTransferNoSupervisor(ArrayList list, int oldDept, int newDept,
        int oldBranch,
        int newBranch, String oldUser,
        String newUser, int oldSection,
        int newSection,
        String raiseEntry, String transferDate,
        int userId, String whoToRem1,
        String email1, String whoToRem2,
        String email2, String effDate, String mtid,String categoryCode, String description,
        double cost, double nbv,double monthDep,double accumDep, int assetCode,String oldsbucode, String newsbucode,String newAsset_id) {
int i = 0;
String query = "";

magma.AssetRecordsBean bd = null;
for (int j = 0; j < list.size(); j++) {
bd = (magma.AssetRecordsBean) list.get(j);
// public String getIdentity(String bra, String dep, String sec, String cat) throws 	
String assetid = bd.getAsset_id();
newAsset_id = bd.getNewasset_id();
String oldbranch = bd.getBranch_id();
String olddept = bd.getDepartment_id();
String oldsection = bd.getSection_id();
if(olddept==""){olddept = "0";}
if(oldsection==""){oldsection = "0";}
String olddescription = bd.getDescription();
String oldcat = bd.getCatCode();
String olduser = bd.getUser();
String oldsbu = bd.getSbu_code();
String newbra = bd.getNewbranch_id();
String newdep = bd.getNewdepartment_id(); 
String newsec = bd.getNewsection_id();
String newsbu = bd.getNewsbu_code();
String newassetuser = bd.getNewuser();
double assetcost = Double.parseDouble(bd.getCost_price());
double nbvamt = Double.parseDouble(bd.getNbv());
double monthdep = Double.parseDouble(bd.getMonthlydep());
double accumdep = Double.parseDouble(bd.getAccumdep());
int assetcode = bd.getAssetCode();
//System.out.println("Asset Id: "+assetid+"  New Branch Id: "+newbra+"  New Dept Id: "+newdep+"  New Section Id: "+newsec+"  New User Name: "+newassetuser);
//String newbraCode = htmlUtil.findObject("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = '"+newbra+"'");

String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET DEPT_ID = '" +
		newdep +
        "',BRANCH_ID = '" + newbra +
        "',BRANCH_CODE = '" +
        code.getBranchCode(newbra) +
        "',SECTION_CODE = '" +
        code.getSectionCode(newsec) +
        "',DEPT_CODE = '" +
        code.getDeptCode(newdep) +
          "',OLD_ASSET_ID = '" +assetid +   
          "',ASSET_ID = '" +newAsset_id +   
        "' WHERE ASSET_ID = '" + assetid + "'";

String updateQuery = "UPDATE AM_ASSET SET DEPT_ID = '" + newdep +
  "',BRANCH_ID = '" + newbra + "'," +
  "SECTION_ID = '" + newsec +
  "',ASSET_USER = '" + newassetuser +
  "',WHO_TO_REM = '" + whoToRem1 +
  "',EMAIL1 = '" + email1 +
  "',WHO_TO_REM_2 = '" + whoToRem2 +
  "',EMAIL2 = '" + email2 +
  "',BRANCH_CODE = '" +
  code.getBranchCode(newbra) +
  "',SECTION_CODE = '" +
  code.getSectionCode(newsec) +
  "',DEPT_CODE = '" +
  code.getDeptCode(newdep) +
  "',SBU_CODE = '" +newsbu +   
  "',OLD_ASSET_ID = '" +assetid +   
  "',ASSET_ID = '" +newAsset_id +   
  "' WHERE ASSET_ID = '" + assetid + "'";


String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,"
+
"OLD_ASSET_USER,NEW_ASSET_USER," +
  "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
  "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE,NEW_ASSET_ID)" +
" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
Connection con = null;
PreparedStatement ps = null;
try {
	AssetRecordsBean arb = new AssetRecordsBean();
con = getConnection("legendPlus");
ps = con.prepareStatement(insertQuery);
ps.setString(1, assetid);
ps.setInt(2, Integer.parseInt(olddept));
ps.setInt(3, Integer.parseInt(newdep));
ps.setInt(4, Integer.parseInt(oldbranch));
ps.setInt(5, Integer.parseInt(newbra));
ps.setString(6, olduser);  
ps.setString(7, newassetuser);
ps.setInt(8, Integer.parseInt(oldsection));
ps.setInt(9, Integer.parseInt(newsec));
ps.setString(10, raiseEntry);
ps.setDate(11, dateConvert(transferDate));
ps.setInt(12, userId);
ps.setDate(13, dateConvert(effDate));
ps.setString(14, code.getBranchCode(oldbranch));
ps.setString(15, code.getSectionCode(oldsection));
ps.setString(16, code.getDeptCode(olddept));
ps.setString(17, code.getBranchCode(newbra));
ps.setString(18, code.getSectionCode(newsec));
ps.setString(19, code.getDeptCode(newdep));
//System.out.println("insertAssetTransferNoSupervisor New MTID: "+mtid+assetcode);
ps.setString(20, mtid+assetcode);
ps.setString(21, oldcat);
ps.setString(22, olddescription);
ps.setDouble(23,assetcost);
ps.setDouble(24,nbvamt);
ps.setDouble(25,monthdep);
ps.setDouble(26,accumdep);
ps.setInt(27,assetcode);
//System.out.println("insertAssetTransferNoSupervisor oldsbucode: "+oldsbucode+"  newsbucode: "+newsbucode);
ps.setString(28, oldsbu);
ps.setString(29, newsbu);
ps.setString(30, newAsset_id);
i = ps.executeUpdate();

if (i > 0) {
ps = con.prepareStatement(updateQuery);
i = ps.executeUpdate();
ps = con.prepareStatement(updateFleetMaster);
i = ps.executeUpdate();

String page1 = "ASSET TRANSFER RAISE ENTRY";
//query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
//this is were new transfer information is set to am_asset table,

updateAssetTransfer(assetid);
String change_id_query2 = "update am_asset set old_asset_id ='" + assetid + "', asset_id ='" + newAsset_id + "' where asset_id ='" + assetid + "'";
// }
arb.updateAssetStatusChange(change_id_query2);

appManager = new ApprovalManager();
appManager.infoFromApproval(assetid, page1);

}    

} catch (Exception e) {
String warning = "WARNING:AssetManager: insertBulkAssetTransfer() :Error inserting Asset Transfer ->" +
  e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
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
              String effDate, String mtid, String wh_tax,double wht_percent, String subject_to_vat,
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
  con = getConnection("legendPlus");

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
  ps.setDouble(23,wht_percent);
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
  ps.setDouble(8, wht_percent);
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
  con = getConnection("legendPlus");

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
            con = getConnection("legendPlus");
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
            con = getConnection("legendPlus");
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




 public Improvement getMaintenanceAssetRepost(String id,int tranId) {

  //String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC," +
                  "OLD_VENDOR_ID,INTEGRIFY,INVOICE_NO,LPONUM,DESCRIPTION,IMPROV_USEFULLIFE,LOCATION," +
                  "OLD_IMPROV_NBV,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST,OLDIMPROV_ACCUMDEP,PROJECT_CODE,SBU_CODE FROM am_asset_improvement " +
                  "WHERE REVALUE_ID = " + tranId ;
//System.out.println("getMaintenanceAssetRepost query:   "+query);
          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Improvement revalue = null;

          try {
              con = getConnection("legendPlus");
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
                  String integrify = rs.getString("INTEGRIFY");
                  String invNo = rs.getString("INVOICE_NO");
                  String lpoNum = rs.getString("LPONUM");
                  String decsription = rs.getString("DESCRIPTION");
                  int usefullife = rs.getInt("IMPROV_USEFULLIFE");
                  String location = rs.getString("LOCATION");
                  String projectCode = rs.getString("PROJECT_CODE");
                  String sbuCode = rs.getString("SBU_CODE");
                  
                  double oldimprovNBV = rs.getDouble("OLD_IMPROV_NBV");
                  double oldimprovCost = rs.getDouble("OLD_IMPROV_COST");
                  double oldimprovvatableCost = rs.getDouble("OLD_IMPROV_VATABLECOST");
                  double oldimprovaccum = rs.getDouble("OLDIMPROV_ACCUMDEP");
                  
                  revalue = new Improvement(revalueId, assetId, costIncrease,
                                            reason, revalueDate,
                                            userId, raiseEntry, revVendorAcct,
                                            cost, vatableCost, vatAmt, whtAmt,
                                            nbv, accumDep,
                                            oldCost, oldVatableCost, oldVatAmt,
                                            oldWhtAmt, oldNbv, oldAccumDep,
                                            effDate, 0, 0, 0, "", decsription, "", "",integrify,usefullife,lpoNum,invNo);
                 revalue.setOldVendorId(oldVendorId);
                 revalue.setOldVendorAcc(oldVendorAcc);
                 revalue.setNewVendorId(rVendorId);
                 revalue.setInvoiceNo(invNo);
                 revalue.setLocation(location);
                 revalue.setOldimprovCost(oldimprovCost);
                 revalue.setOldimprovNBV(oldimprovNBV);
                 revalue.setOldimprovvatableCost(oldimprovvatableCost);
                 revalue.setOldimprovaccum(oldimprovaccum);
                 revalue.setProjectCode(projectCode);
                 revalue.setSbuCode(sbuCode);

              }

          } catch (Exception e) {
              System.out.println("INFO:Error fetching getMaintenanceAssetRepost with tranId ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }

          return revalue;
      }

 public Revaluation getRevaluationAssetRepost(String id,int tranId) {

  //String revalue_id  = getMaxTransaction(id);
          String query =
                  "SELECT REVALUE_ID,ASSET_ID,COST_INCREASE,REVALUE_REASON,REVALUE_DATE," +
                  "USER_ID,RAISE_ENTRY,R_VENDOR_AC,COST_PRICE,VATABLE_COST,VAT_AMOUNT," +
                  "WHT_AMOUNT,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT," +
                  "OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE ,R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID FROM am_asset_revaluation " +
                  "WHERE REVALUE_ID = " + tranId ;

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;

          ArrayList list = new ArrayList();
          //declare DTO object
          Revaluation revalue = null;

          try {
              con = getConnection("legendPlus");
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


 public ArrayList getAssetByQuery(String query) {
   String selectQuery = "SELECT * FROM AM_ASSET WHERE " + query;
//   String selectQuery = "SELECT * FROM AM_ASSET WHERE ASSET_ID != ? " ;

   Connection con = null;
   PreparedStatement ps = null;
   ResultSet rs = null;
   ArrayList list = new ArrayList();
   Asset _obj = null;
//System.out.println("getAssetByQuery query: "+query);
//System.out.println("getAssetByQuery selectQuery: "+selectQuery);
   try {
       con = getConnection("legendPlus");
       ps = con.prepareStatement(selectQuery);
//       ps.setString(1, query);
       rs = ps.executeQuery();

       while (rs.next()) {
           String assetId = rs.getString("ASSET_ID");
           String regNo = rs.getString("REGISTRATION_NO");
           int branchId = rs.getInt("BRANCH_ID");
//           String branchName = rs.getString("BRANCH_NAME");
           int deptId = rs.getInt("DEPT_ID");
//           String deptName = rs.getString("DEPT_NAME");
           int sectionId = rs.getInt("SECTION_ID");
//           String sectionName = rs.getString("SECTION_NAME");
           int categoryId = rs.getInt("CATEGORY_ID");
//           String categoryName = rs.getString("CATEGORY_NAME");
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
//           int regionId = rs.getInt("REGION_ID");
//           String regionName = rs.getString("REGION_NAME");
           String whoToRem1 = rs.getString("WHO_TO_REM");
           String whoToRem2 = rs.getString("WHO_TO_REM_2");
           String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
           double vatAmt = rs.getDouble("VAT");
           double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
           String subj2Vat = rs.getString("SUBJECT_TO_VAT");
           String subj2Wht = rs.getString("WH_TAX");
           double vatableCost = rs.getDouble("VATABLE_COST");
           int assetCode = rs.getInt("asset_code");
           double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
           double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
           double imprcostPrice = rs.getDouble("IMPROV_COST");
           double imprNBV = rs.getDouble("IMPROV_NBV");
           _obj = new Asset(assetId, regNo, branchId, "", deptId,
                            "", sectionId, "",
                            categoryId, "", 0, "",
                            description, datePurchased, depRate, make,
                            assetUser,
                            accumDep, monthDep, costPrice, depEndDate,
                            residualValue, raiseEntry, NBV, effDate,
                            vendorAcct, model,
                            engineNo, email1, email2, whoToRem1, whoToRem2,
                            reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                            subj2Wht, vatableCost);
                            _obj.setAssetCode(assetCode);
                            _obj.setImpraccumDep(impraccumDep);
                            _obj.setImprcost(imprcostPrice);
                            _obj.setImprmonthDep(imprmonthDep);
                            _obj.setImprnbv(imprNBV);                                 
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

 public ArrayList getAssetByQuery(String query,String branch_Id,String dept_Id,String category,String asset_Id,String regNumber,String fromDate,String toDate, String order) {
//        String selectQuery = "SELECT * FROM AM_ASSET WHERE " + query;
        String selectQuery = "SELECT * FROM AM_ASSET WHERE ASSET_ID IS NOT NULL AND ASSET_STATUS = 'ACTIVE' " +query + order;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        Asset _obj = null;
//		System.out.println("getAssetByQuery query: "+query);
//System.out.println("getAssetByQuery selectQuery: "+selectQuery);
        try {
            con = getConnection("legendPlus");
//            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, query);
            
            ps = con.prepareStatement(selectQuery.toString());
//          System.out.println("<========query in getAssetByQuery=======>: "+query.contains("BRANCH_ID"));
          if(query.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>0 asset_Id: "+asset_Id);
        	  ps.setString(1, asset_Id);
          }     
//          if(query.contains("BRANCH_ID") && !query.contains("DEPT_ID")){
////        	  System.out.println("<========getAssetByQuery=======>1 branch_Id: "+branch_Id);
//        	  ps.setString(1, branch_Id);
//        	  ps.setString(2, branch_Id);
//          }       
          if(query.contains("BRANCH_ID") && query.contains("DEPT_ID")){
        	//  System.out.println("<========getAssetByQuery=======>2");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, branch_Id);
        	  ps.setString(3, dept_Id);
          }   
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && !query.contains("DEPT_ID") && !query.contains("ASSET_ID")){
        	//  System.out.println("<========getAssetByQuery=======>3");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
          }   
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("REGISTRATION_NO") && query.contains("ASSET_ID")){
        	//  System.out.println("<========getAssetByQuery=======>4");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, dept_Id);
        	  ps.setString(5, regNumber);
        	  ps.setString(6, asset_Id);
          }  
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("DATE_PURCHASED") && !query.contains("ASSET_ID")){
        	//  System.out.println("<========getAssetByQuery=======>5");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, dept_Id);
        	  ps.setString(5, fromDate);
        	  ps.setString(6, toDate);
          }  
          if(query.contains("BRANCH_ID") && query.contains("DEPT_ID") && query.contains("DATE_PURCHASED") && !query.contains("CATEGORY_ID")){
        	//  System.out.println("<========getAssetByQuery=======>6");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, branch_Id);
        	  ps.setString(3, dept_Id);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
          }    
          if(query.contains("BRANCH_ID") && !query.contains("DEPT_ID") && query.contains("DATE_PURCHASED") && !query.contains("CATEGORY_ID")){
        	//  System.out.println("<========getAssetByQuery=======>6B");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, branch_Id);
        	  ps.setString(3, dept_Id);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
          }    
          if(query.contains("BRANCH_ID") && query.contains("DATE_PURCHASED") && !query.contains("CATEGORY_ID")){
        	//  System.out.println("<========getAssetByQuery=======>7");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, branch_Id);
        	  ps.setString(3, fromDate);
        	  ps.setString(4, toDate);
          }   
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && !query.contains("DATE_PURCHASED")){
        	//  System.out.println("<========getAssetByQuery=======>8");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, dept_Id);
          } 
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("ASSET_ID")){
        	//  System.out.println("<========getAssetByQuery=======>9");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, asset_Id);
          } 
          if(query.contains("BRANCH_ID") && query.contains("BRANCH_ID") && query.contains("ASSET_ID")){
       	 // System.out.println("<========getAssetByQuery=======>10");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, branch_Id);
        	  ps.setString(3, asset_Id);
          } 
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("DATE_PURCHASED")){
        	//  System.out.println("<========getAssetByQuery=======>11");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, dept_Id);
        	  ps.setString(5, fromDate);
        	  ps.setString(6, toDate);
          } 
          if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && !query.contains("DEPT_ID") && query.contains("DATE_PURCHASED")){
        	//  System.out.println("<========getAssetByQuery=======>12");
        	  ps.setString(1, branch_Id);
        	  ps.setString(2, category);
        	  ps.setString(3, branch_Id);
        	  ps.setString(4, fromDate);
        	  ps.setString(5, toDate);
          } 
            rs = ps.executeQuery();
  
            while (rs.next()) {
                String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
//                String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
//                String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
//                String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
//                String categoryName = rs.getString("CATEGORY_NAME");
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
//                int regionId = rs.getInt("REGION_ID");
//                String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");
                int assetCode = rs.getInt("asset_code");
                double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
                double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
                double imprcostPrice = rs.getDouble("IMPROV_COST");
                double imprNBV = rs.getDouble("IMPROV_NBV");
                _obj = new Asset(assetId, regNo, branchId, "", deptId,
                                 "", sectionId, "",
                                 categoryId, "", 0, "",
                                 description, datePurchased, depRate, make,
                                 assetUser,
                                 accumDep, monthDep, costPrice, depEndDate,
                                 residualValue, raiseEntry, NBV, effDate,
                                 vendorAcct, model,
                                 engineNo, email1, email2, whoToRem1, whoToRem2,
                                 reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                                 subj2Wht, vatableCost);
                                 _obj.setAssetCode(assetCode);
                                 _obj.setImpraccumDep(impraccumDep);
                                 _obj.setImprcost(imprcostPrice);
                                 _obj.setImprmonthDep(imprmonthDep);
                                 _obj.setImprnbv(imprNBV);                                 
                list.add(_obj);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset by Query in getAssetByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
 public ArrayList getAssetByQuery(String query,String branch_Id,String dept_Id,String category,String asset_Id,String regNumber,String fromDate,String toDate, String status, String order) {
//   String selectQuery = "SELECT * FROM AM_ASSET WHERE " + query;
   String selectQuery = "SELECT * FROM AM_ASSET WHERE ASSET_ID IS NOT NULL " +query + order;

   Connection con = null;
   PreparedStatement ps = null;
   ResultSet rs = null;
   ArrayList list = new ArrayList();
   Asset _obj = null;
//System.out.println("getAssetByQuery query: "+query);
 System.out.println("getAssetByQuery selectQuery: "+selectQuery);
   try {
       con = getConnection("legendPlus");
//       ps = con.prepareStatement(selectQuery);
//       ps.setString(1, query);
       
       ps = con.prepareStatement(selectQuery.toString());
//     System.out.println("<========query in getAssetByQuery=======>: "+query.contains("BRANCH_ID"));
     if(query.contains("ASSET_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>0 asset_Id: "+asset_Id);
   	  ps.setString(1, asset_Id);
	  ps.setString(2, status);
     }     
     if(query.contains("BRANCH_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>1 With STatus branch_Id: "+branch_Id);
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, branch_Id);
   	  ps.setString(3, status);
     }  
     if(query.contains("BRANCH_ID") && query.contains("DEPT_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>2");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, branch_Id);
   	  ps.setString(3, dept_Id);
   	  ps.setString(4, status);
     }   
     if(query.contains("BRANCH_ID") && query.contains("ASSET_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>3");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, branch_Id);
   	  ps.setString(3, asset_Id);
   	  ps.setString(4, status);
     }   
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>4");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, category);
   	  ps.setString(3, branch_Id);
   	  ps.setString(4, status);
     }  
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("DATE_PURCHASED") && !query.contains("ASSET_ID")){
//   	  System.out.println("<========getAssetByQuery=======>5");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, category);
   	  ps.setString(3, branch_Id);
   	  ps.setString(4, dept_Id);
   	  ps.setString(5, fromDate);
   	  ps.setString(6, toDate);
     }  
     if(query.contains("BRANCH_ID") && query.contains("DEPT_ID") && query.contains("DATE_PURCHASED") && !query.contains("CATEGORY_ID")){
//   	  System.out.println("<========getAssetByQuery=======>6");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, branch_Id);
   	  ps.setString(3, dept_Id);
   	  ps.setString(4, fromDate);
   	  ps.setString(5, toDate);
     }          
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && !query.contains("ASSET_STATUS") && !query.contains("DEPT_ID")){
//   	  System.out.println("<========getAssetByQuery=======>7");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, category);
   	  ps.setString(3, branch_Id);
   	  ps.setString(4, status);
     }   
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>8");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, category);
   	  ps.setString(3, branch_Id);
   	  ps.setString(4, dept_Id);
   	  ps.setString(5, status);
   	  
     } 
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && query.contains("ASSET_ID") && query.contains("ASSET_STATUS")){
//   	  System.out.println("<========getAssetByQuery=======>9");
   	  ps.setString(1, branch_Id);
   	  ps.setString(2, category);
   	  ps.setString(3, branch_Id);
   	  ps.setString(4, dept_Id);
   	  ps.setString(5, asset_Id);
   	  ps.setString(6, status);
     } 
     
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && query.contains("DEPT_ID") && !query.contains("ASSET_ID") && query.contains("ASSET_STATUS") && query.contains("DATE_PURCHASED")){
//  	  System.out.println("<========getAssetByQuery=======>9");
  	  ps.setString(1, branch_Id);
  	  ps.setString(2, category);
  	  ps.setString(3, branch_Id);
  	  ps.setString(4, dept_Id);
  	  ps.setString(5, fromDate);
  	 ps.setString(6, toDate);
  	  ps.setString(7, status);
    } 
     
     if(query.contains("BRANCH_ID") && query.contains("CATEGORY_ID") && !query.contains("DEPT_ID") && !query.contains("ASSET_ID") && query.contains("ASSET_STATUS") && query.contains("DATE_PURCHASED")){
// 	  System.out.println("<========getAssetByQuery=======>9");
 	  ps.setString(1, branch_Id);
 	  ps.setString(2, category);
 	  ps.setString(3, branch_Id);
 	  ps.setString(4, fromDate);
 	 ps.setString(5, toDate);
 	  ps.setString(6, status);
   } 
     
    
       rs = ps.executeQuery();

       while (rs.next()) {
           String assetId = rs.getString("ASSET_ID");
           String regNo = rs.getString("REGISTRATION_NO");
           int branchId = rs.getInt("BRANCH_ID");
//           String branchName = rs.getString("BRANCH_NAME");
           int deptId = rs.getInt("DEPT_ID");
//           String deptName = rs.getString("DEPT_NAME");
           int sectionId = rs.getInt("SECTION_ID");
//           String sectionName = rs.getString("SECTION_NAME");
           int categoryId = rs.getInt("CATEGORY_ID");
//           String categoryName = rs.getString("CATEGORY_NAME");
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
//           int regionId = rs.getInt("REGION_ID");
//           String regionName = rs.getString("REGION_NAME");
           String whoToRem1 = rs.getString("WHO_TO_REM");
           String whoToRem2 = rs.getString("WHO_TO_REM_2");
           String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
           double vatAmt = rs.getDouble("VAT");
           double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
           String subj2Vat = rs.getString("SUBJECT_TO_VAT");
           String subj2Wht = rs.getString("WH_TAX");
           double vatableCost = rs.getDouble("VATABLE_COST");
           int assetCode = rs.getInt("asset_code");
           double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
           double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
           double imprcostPrice = rs.getDouble("IMPROV_COST");
           double imprNBV = rs.getDouble("IMPROV_NBV");
           _obj = new Asset(assetId, regNo, branchId, "", deptId,
                            "", sectionId, "",
                            categoryId, "", 0, "",
                            description, datePurchased, depRate, make,
                            assetUser,
                            accumDep, monthDep, costPrice, depEndDate,
                            residualValue, raiseEntry, NBV, effDate,
                            vendorAcct, model,
                            engineNo, email1, email2, whoToRem1, whoToRem2,
                            reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                            subj2Wht, vatableCost);
                            _obj.setAssetCode(assetCode);
                            _obj.setImpraccumDep(impraccumDep);
                            _obj.setImprcost(imprcostPrice);
                            _obj.setImprmonthDep(imprmonthDep);
                            _obj.setImprnbv(imprNBV);                                 
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
        String selectQuery = "SELECT b.BRANCH_NAME,c.category_name,d.Dept_name,s.Section_Name,'0' AS REGION_ID, 'NO REGION' AS REGION_NAME, * FROM AM_ASSET a, am_ad_branch b, am_ad_category c, am_ad_department d, am_ad_section s  WHERE a.branch_code = b.BRANCH_CODE and a.DEPT_Id = d.Dept_Id and a.CATEGORY_CODE = c.category_code and a.SECTION_ID = s.Section_Id and ASSET_ID = '" +
                             assetId + "' ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection("legendPlus");
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
                String vendorName = rs.getString("VENDOR_NAME");
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
                int assetCode = rs.getInt("asset_code");
                double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
                double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
                double imprcostPrice = rs.getDouble("IMPROV_COST");
                double imprNBV = rs.getDouble("IMPROV_NBV");
               // int wht_percent = rs.getInt("WHT_PERCENT");
              //  String integrify = rs.getString("INTEGRIFY");
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
                _obj.setImpraccumDep(impraccumDep);
                _obj.setImprcost(imprcostPrice);
                _obj.setImprmonthDep(imprmonthDep);
                _obj.setImprnbv(imprNBV);
                _obj.setAssetCode(assetCode);
                _obj.setRegNo(vendorName);
                
                

              //  _obj.setWht_percent(wht_percent);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Asset BY ID in getAsset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return _obj;

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
              String effDate, String mtid, String wh_tax,double wht_percent, String subject_to_vat,
              String vendorId,String vendorIdOld,String vendorAccOld,String description,String categoryCode, String
branchCode,String lpo,String invoiceNo, double newCP,int assetCode,int usefullife, String location,String projectCode,String sbuCode,String vendorName) {
	 String updateQuery = "";
	 String updateQuery1 = "";
 // nbv += cost;
// costPrice += cost;
//	 System.out.println("usefullife: "+usefullife);
  int i = 0;
if(usefullife==0){
//	System.out.println("About to set query for usefullife equal Zero ");
          updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP =ACCUM_DEP + ?, "+
        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=?, " +
         " WHERE ASSET_ID = ?";
 }else{
//	 System.out.println("About to set query for usefullife not equal Zero ");
	  updateQuery1 = "UPDATE AM_ASSET SET IMPROV_COST = coalesce(IMPROV_COST,0) + ?,TOTAL_NBV = coalesce(TOTAL_NBV,0)+?, " +
		         "IMPROV_NBV = coalesce(IMPROV_NBV,0) + ?, IMPROV_VATABLECOST = coalesce(IMPROV_VATABLECOST,0)+?, VAT = VAT + ?, " +
		         "WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, IMPROV_ACCUMDEP =coalesce(IMPROV_ACCUMDEP,0) + ?, "+
		        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=?, "+
		         "IMPROV_TOTALLIFE = coalesce(IMPROV_TOTALLIFE,0)+?,IMPROV_REMAINLIFE= coalesce(IMPROV_REMAINLIFE,0)+?, "+
		        "IMPROV_EffectiveDate = ?,IMPROV_ENDDATE = SELECT DATEADD(month, "+usefullife+", ?) "+
		         " WHERE ASSET_ID = ?";	 
 }
  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC," +
  "RAISE_ENTRY,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT," +
          "OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,new_cost_price,IMPROV_USEFULLIFE,IMPROVED,asset_code,LOCATION,PROJECT_CODE,SBU_CODE,VENDOR_NAME)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;
  try {
  con = getConnection("legendPlus");

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
  ps.setDouble(23,wht_percent);
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
  ps.setInt(34, usefullife);
  ps.setString(35, "Y");
  ps.setInt(36, assetCode);
  ps.setString(37, location);
  ps.setString(38, projectCode);
  ps.setString(39, sbuCode);
  ps.setString(40, vendorName);
//  System.out.println("costIncrease: "+costIncrease);
  i = ps.executeUpdate();


  ps = con.prepareStatement(updateQuery);
//  System.out.println("updateQuery: "+updateQuery);
  ps.setDouble(1, cost);
  ps.setDouble(2, nbv);
  ps.setDouble(3, vatableCost);
  ps.setDouble(4, vatAmt);
  ps.setDouble(5, whtAmt);
  ps.setDouble(6, accumDep);
  ps.setString(7, wh_tax);
  ps.setDouble(8, wht_percent);
  ps.setString(9, subject_to_vat);
  ps.setString(10,vendorAcct);
  ps.setInt(11, Integer.parseInt(vendorId));
  ps.setString(12, assetId);
 
  i = ps.executeUpdate();
  

  ps = con.prepareStatement(updateQuery1);
//  System.out.println("updateQuery1: "+updateQuery1);
  ps.setDouble(1, cost);
  ps.setDouble(2, cost);
  ps.setDouble(3, nbv);
  ps.setDouble(4, vatableCost);
  ps.setDouble(5, vatAmt);
  ps.setDouble(6, whtAmt);
  ps.setDouble(7, accumDep);
  ps.setString(8, wh_tax);
  ps.setDouble(9, wht_percent);
  ps.setString(10, subject_to_vat);
  ps.setString(11,vendorAcct);
  ps.setInt(12, Integer.parseInt(vendorId));
  ps.setInt(13, usefullife);
  ps.setInt(14, usefullife);
  ps.setDate(15, dateConvert(effDate));
  ps.setDate(16, dateConvert(effDate));
  ps.setString(17, assetId);
//  System.out.println("Update of File Am Asset is about to be done ");
  i = ps.executeUpdate();
//  System.out.println("Update of File Am Asset is done ");
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


 public int insertAssetRevalueMaintananceNoSupervisor(double cost, double nbv, String assetId,
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
branchCode,String lpo,String invoiceNo, double newCP,int assetCode) {

 // nbv += cost;
// costPrice += cost;
  int i = 0;

  String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
         " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP =ACCUM_DEP + ?, "+
        "WH_TAX = ?, WHT_PERCENT = ?, SUBJECT_TO_VAT = ?,VENDOR_AC=?,SUPPLIER_NAME=? "+
         " WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_revaluation(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC," +
  "RAISE_ENTRY,NBV,ACCUM_DEP,OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT," +
          "OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,WHT_PERCENT,SUBJECT_TO_VAT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,new_cost_price,asset_code)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("legendPlus");

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
  ps.setInt(34, assetCode);

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
  String warning = "WARNING:Error inserting am_asset_revaluation Revaluation ->" +
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
              String effDate,String mtid,String wh_tax_cb, double selectTax,String subject_to_vat,
              double newVC,double newCP,double newNBV,
              double newVA,double newWA,String vendorId,String vendorIdOld,
              String vendorAccOld, String description,String categoryCode,String branchCode,String lpo,String invoiceNum,int assetCode,int usefullife,String location,String projectCode,String sbuCode,String vendorName) {

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
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,IMPROV_USEFULLIFE,IMPROVED,asset_code,LOCATION,PROJECT_CODE,SBU_CODE,VENDOR_NAME)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("legendPlus");

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
  ps.setDouble(23, selectTax);
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
 ps.setInt(38, usefullife);
 ps.setString(39, "Y"); 
 ps.setInt(40,assetCode);
 ps.setString(41, location);
 ps.setString(42, projectCode);
 ps.setString(43, sbuCode);
 ps.setString(44, vendorName);
     
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

 public int insertAssetRevalueMaintanance(double cost, double nbv, String assetId,
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
              String vendorAccOld, String description,String categoryCode,String branchCode,String lpo,String invoiceNum,int assetCode,String revalue,String remainlife) {


  int i = 0;

  String insertQuery = 
  "INSERT INTO am_asset_revaluation(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,WH_TAX,"+
  "WHT_PERCENT,SUBJECT_TO_VAT,NEW_VATABLE_COST, NEW_COST_PRICE,NEW_NBV,NEW_VAT_AMOUNT,NEW_WHT_AMOUNT," +
  "R_VENDOR_ID,OLD_VENDOR_ACC,OLD_VENDOR_ID,DESCRIPTION,CATEGORY_CODE,BRANCH_CODE,lpoNum,invoice_no,asset_code,reavlue,Remain_life)"+
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("legendPlus");

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
     ps.setInt(38,assetCode);
     ps.setString(39, revalue);
     ps.setInt(40, Integer.parseInt(remainlife));
  i = ps.executeUpdate();
  } catch (Exception e) {
  String warning = "WARNING:Error inserting am_asset_revaluation Revaluation ->" +
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
              String effDate,String mtid, int assetCode,String location,String projectCode,String sbuCode,String vendorName) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_improvement(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,asset_code,LOCATION,PROJECT_CODE,SBU_CODE,VENDOR_NAME)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("legendPlus");

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
  ps.setInt(22,assetCode);
  ps.setString(23, location);
  ps.setString(24, projectCode);
  ps.setString(25, sbuCode);
  ps.setString(26, vendorName);
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


       public int insertAssetRevalueMaintanance(double cost, double nbv, String assetId,
              double costIncrease, String revalueReason,
              String revalueDate,
              int userId, double vatableCost, double vatAmt,
              double whtAmt,
              String vendorAcct, String raiseEntry,
              double accumDep,
              double oldCost, double oldVatableCost,
              double oldVatAmt, double oldWhtAmt,
              double oldNbv, double oldAccumDep,
              String effDate,String mtid, int assetCode) {

//  nbv += cost;
//  costPrice += cost;
  int i = 0;

 // String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
        // " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

  String insertQuery =
  "INSERT INTO am_asset_revaluation(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
  "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
  "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,asset_code)" +
  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

  Connection con = null;
  PreparedStatement ps = null;

  try {
  con = getConnection("legendPlus");

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
  ps.setInt(22,assetCode);
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
  String warning = "WARNING:Error inserting am_asset_revaluation Revaluation ->" +
         e.getMessage();
  System.out.println(warning);
  e.printStackTrace();
  } finally {
  closeConnection(con, ps);
  }
  return i;
  }

  public int insertAssetDisposalNoSupervisor(String assetId, String reason,
                                   String buyerAcct,
                                   double disposalAmount, String raiseEntry,
                                   double profitLoss,
                                   String disposalDate, String effDate,
                                   int userId,String supervise,int assetCode,
                                   String partialtype,double partialpercent,double partialamt,String disposalId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CurrentDateTime cdt = new CurrentDateTime();
        int i = 0;
        String disposaltype = "";
        String updateFleetMaster =
                "UPDATE FT_FLEET_MASTER SET STATUS=? WHERE ASSET_ID = ?";
        String updateQuery =
                "UPDATE AM_ASSET SET ASSET_STATUS=?,DATE_DISPOSED = ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETDISPOSAL(ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT," +
                "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status," +
                "DISPOSAL_TYPE,DISPOSAL_PERCENT,PARTIAL_AMT,asset_code) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
        	
            if(partialtype.equalsIgnoreCase("P")){disposaltype = "PARTIALLY DISPOSED";}
            else{disposaltype = "DISPOSED";}
           // System.out.println("partialtype: "+partialtype+"     disposaltype:  "+disposaltype);
            con = getConnection("legendPlus");
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
            ps.setString(10, disposalId);
     //       ps.setString(10, new ApplicationHelper().getGeneratedId("AM_ASSETDISPOSAL"));
            ps.setString(11,supervise);
            ps.setString(12, "D");
            ps.setString(13, partialtype);
            ps.setDouble(14, partialpercent);
            ps.setDouble(15, partialamt);            
            ps.setInt(16, assetCode);

            i = ps.executeUpdate();
            if (i > 0) {   
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, disposaltype);
                ps.setDate(2, dateConvert(disposalDate));
                ps.setString(3, assetId);
                i = ps.executeUpdate();
                //update fleet master
                ps = con.prepareStatement(updateFleetMaster);
                ps.setString(1, disposaltype);
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

        public int insertAssetDisposal(String assetId, String reason,
                                   String buyerAcct,
                                   double disposalAmount, String raiseEntry,
                                   double profitLoss,
                                   String disposalDate, String effDate,
                                   int userId,String supervise,int assetCode,String partialtype,double partialpercent,double partialamt,double disposedAccum,String disposalId) {

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
                "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status," +
                "DISPOSAL_TYPE,DISPOSAL_PERCENT,PARTIAL_AMT,DisposedAccum_Dep,asset_code) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            con = getConnection("legendPlus");
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
            ps.setString(10,disposalId);
//        ps.setString(10, new ApplicationHelper()
//					.getGeneratedId("AM_ASSETDISPOSAL"));
            ps.setString(11,supervise);
            ps.setString(12, "P");  
            ps.setString(13, partialtype);
            ps.setDouble(14, partialpercent);
            ps.setDouble(15, partialamt);
            ps.setDouble(16, disposedAccum*partialpercent/100);
            ps.setInt(17,assetCode);
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
                                  String effDate,String mtid,int assetCode) {

        //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in insert asset revalue KKKKKKKKKKKKKKKKKK");
        String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                             " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,asset_code)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection("legendPlus");

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
            ps.setInt(22,assetCode);

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
                                  String effDate,String mtid,int assetCode) {

        //nbv += cost;
        //costPrice += cost;
        int i = 0;
        //System.out.println("i am in insert asset revalue KKKKKKKKKKKKKKKKKK");
        //String updateQuery = "UPDATE AM_ASSET SET COST_PRICE = COST_PRICE + ?," +
                          //   " NBV = NBV + ?,VATABLE_COST = VATABLE_COST + ?, VAT = VAT + ?, WH_TAX_AMOUNT = WH_TAX_AMOUNT + ?, ACCUM_DEP = ACCUM_DEP + ? WHERE ASSET_ID = ?";

        String insertQuery =
                "INSERT INTO AM_ASSETREVALUE(ASSET_ID,COST_INCREASE,REVALUE_REASON," +
                "REVALUE_DATE,USER_ID,COST_PRICE,VATABLE_COST,VAT_AMOUNT,WHT_AMOUNT,R_VENDOR_AC,RAISE_ENTRY,NBV,ACCUM_DEP," +
       "OLD_COST_PRICE,OLD_VATABLE_COST,OLD_VAT_AMOUNT,OLD_WHT_AMOUNT,OLD_NBV,OLD_ACCUM_DEP,EFFDATE,REVALUE_ID,asset_code)" +
       " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection("legendPlus");

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
            ps.setInt(22,assetCode);

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

public int insertWipReclassificationNoSupervisor(String assetId, int oldDept, int newDept,int oldBranch,
                                   int newBranch, String oldUser,String newUser, int oldSection,
                                   int newSection,String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,String email1, String whoToRem2,
                                   String email2, String effDate, String mtid,String newcat_id,
                                   String old_cat_id,String status,
                                   String old_email1,String old_email2,String old_who_rem1,String old_who_rem2,int assetCode
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
                             "old_who_to_rem,old_email1,old_who_to_rem2,old_email2,asset_code)" +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("legendPlus");
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
            ps.setInt(34,assetCode);

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


 public int insertWipReclassificationWithSupervisor
             (
               String assetId, int oldDept, int newDept,int oldBranch,int newBranch, String oldUser,
               String newUser, int oldSection,int newSection,String raiseEntry, String transferDate,
               int userId, String whoToRem1,String email1, String whoToRem2,
               String email2, String effDate,String mtid,String newcat_id,String old_cat_id,String Approval_Status,
               String old_email1,String old_email2,String old_who_rem1,String old_who_rem2,int assetCode
               )
     {
        int i = 0;


        String insertQuery = "INSERT INTO AM_WIP_RECLASSIFICATION(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID," +
                             "NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                             "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                             "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                             ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CAT_ID,NEW_CAT_ID,OLD_CAT_CODE," +
                             "NEW_CAT_CODE,NEW_WHO_TO_REM,NEW_EMAIL1,NEW_WHO_TO_REM2,NEW_EMAIL2,Approval_Status," +
                             "old_who_to_rem,old_email1,old_who_to_rem2,old_email2,asset_code)" +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("legendPlus");
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
//            ps.setDate(11, dateConvert(transferDate));
            ps.setDate(11, dateConvert(new java.util.Date()));
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
            ps.setInt(34,assetCode);
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


 public String[] getNewTransferedDetails(String old_asset_id) {

        String[] result = new String[14];
        //String old_asset_id="";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ArrayList list = new ArrayList();


        try {
           //String query = "select old_branch_id, dept_id, section_id, asset_user from am_asset where asset_id ='"+old_asset_id+"'";
            String query = "select new_branch_id, new_dept_id, new_section, new_sbu_code, new_asset_user,new_who_to_rem,new_who_to_rem2," +
                    "new_email1,new_email2,old_branch_id,old_dept_id,old_section,old_asset_user,old_sbu_code " +
                    "from am_assettransfer where asset_id ='"+old_asset_id+"'";
//           System.out.println("the query for getNewTransferedDetails @@@@@@@@@@@@@@@@@@@ "+query );
           con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


           // rs = ps.executeQuery(query);
  
  
            while (rs.next()) {
                result[0] = Integer.toString(rs.getInt("new_branch_id"));
                result[1] = Integer.toString(rs.getInt("new_dept_id"));
                result[2] = Integer.toString(rs.getInt("new_section"));
                result[3] = rs.getString("new_sbu_code");
                result[4] = rs.getString("new_asset_user");
                result[5] = rs.getString("new_who_to_rem");
                 result[6] = rs.getString("new_who_to_rem2");
                  result[7] = rs.getString("new_email1");
                   result[8] = rs.getString("new_email2");
                   result[9] = Integer.toString(rs.getInt("old_branch_id"));
                result[10] = Integer.toString(rs.getInt("old_dept_id"));
                result[11] = Integer.toString(rs.getInt("old_section"));
                result[12] = rs.getString("old_asset_user");
                result[13] = rs.getString("old_sbu_code");

            }

             for(int j=0; j<result.length;++j){
//                 System.out.println("the value of a is " +result[j]);

             }

        } catch (Exception e) {
            System.out.println(
                    "AssetManager class: getNewTransferedDetails() method: Error fetching All transfered Asset by query ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;
    }

public Transfer getTransferedAsset3(int assetCode) {

        String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
                       " A.OLD_SECTION,D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,NEW_SBU_CODE," +
                       " A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
                       " FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E" +
                       " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID " +
                       " AND A.ASSET_CODE = E.ASSET_CODE AND A.ASSET_CODE = '" + assetCode +
                       "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList list = new ArrayList();
        //declare DTO object
        Transfer transfer = null;

        try {
            con = getConnection("legendPlus");
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
                String newsbucode = rs.getString("NEW_SBU_CODE");
                transfer = new Transfer(transferId, assetId, oldDeptId,
                                        deptName, oldBranchId, branchName,
                                        oldSectionId, sectionName, user,
                                        raiseEntry, transferDate, userId,
                                        categoryName, description, cost,
                                        datePurchased, regNo, effDate,
                                        categoryId, "", "",newsbucode);

                //list.add(transfer);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching All transfered Asset by ID getTransferedAsset3() -> " +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return transfer;
    }


public Asset getAssetByAssetCode(int asseCode) {
        String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_CODE = " + asseCode ;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asset _obj = null;

        try {
            con = getConnection("legendPlus");
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
                int assetCode = rs.getInt("asset_code");
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
                _obj.setAssetCode(assetCode);

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


 public String checkAssetAvalability(String assetID){
 //    System.out.println("\nttttttttttttttt");
ApprovalRecords app = new ApprovalRecords();
    String query = " SELECT transaction_id from am_asset_approval " +
                       " WHERE process_status='P' and asset_id = '"+assetID+"'";
	df = new com.magbel.util.DatetimeFormat();

String transactionId ="";
String process_status ="";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Datefld = df.formatDate(new java.util.Date());
      //  System.out.println("====Datefld== "+Datefld);
        String Month = Datefld.substring(3, 5);
        String Year = Datefld.substring(6, 10);
    //    System.out.println("====Month== "+Month);
    //    System.out.println("====Year== "+Year);  
//        System.out.println("====query== "+query);  
              try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) { 
               transactionId = rs.getString("transaction_id");
//               String AssetId = rs.getString("ASSET_ID");
//                System.out.println("\n>>>>> trans id " + transactionId+"   AssetId: "+AssetId);
 //               System.out.println("\n>>>>> trans id " + transactionId);
               process_status =   transactionId;
            }
// System.out.println(" >>>transactionId>>>  "+transactionId );
 
            if(transactionId.equalsIgnoreCase("")){
 //System.out.println("\n >>>>>> here " );
           // String query1 = " SELECT transaction_id from am_asset_approval " +
             //          " WHERE process_status='A' and asset_id = '"+assetID+"'";
               //-- int tranId = Integer.parseInt(app.getCodeName(query1));
 String query1 = " SELECT asset_code from am_asset WHERE asset_id = ?";
 String asset_Code = app.getCodeName(query1,assetID);
 System.out.println("--asset_Code in checkAssetAvalability--> "+asset_Code);
              int assetCode = Integer.parseInt(app.getCodeName(query1,assetID));
     //     String postFlagQuery="select entryPostFlag from am_raisentry_post where entryPostFlag = 'N' and asset_code="+ assetCode;
// System.out.println("--assetCode--> "+assetCode);
  String queryTest1 = app.getCodeName("select max(trans_id) from am_raisentry_post where  asset_code=?",Integer.toString(assetCode));  
//                 System.out.println("--queryTest1--> "+queryTest1);
//  System.out.println("--queryTest1--> "+queryTest1);
  String queryTest2 = app.getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code=?",Integer.toString(assetCode));
//check is record in am_raisentry_TRANSACTION   

/*  Before legendPlus Upgrade 28/09/2018
  String query2 =app.getCodeName("select asset_code from am_asset_improvement where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"' and approval_status = 'PENDING' ");
  String query3 =app.getCodeName("select asset_code from am_asset_revaluation where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"'");
  //String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100  and DISPOSAL_STATUS ='PD'");
  String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and month(disposal_date) = '"+Month+"' and year(disposal_date) = '"+Year+"'");
 String query5 =app.getCodeName("select count(*) from am_raisentry_post where asset_code = '"+assetCode+"'  and entryPostFlag = 'N' ");
 String query6 =app.getCodeName("select asset_code from am_AcceleratedDepreciation where asset_id = '"+assetID+"' and month(Accelerated_Date) = '"+Month+"' and year(Accelerated_Date) = '"+Year+"'");
*/
  String query2 =app.getCodeName("select asset_code from am_asset_improvement where asset_id = ? and approval_status = 'PENDING' ",assetID);
  if(query2.equals(""))query2 = "0";
  String query3 =app.getCodeName("select asset_code from am_asset_revaluation where asset_id = ? ",assetID);
  //String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100  and DISPOSAL_STATUS ='PD'");
  String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = ? and DISPOSAL_PERCENT = 100 AND disposal_status != 'R' ",assetID);
 String query5 =app.getCodeName("select count(*) from am_raisentry_post where asset_code = ?  and entryPostFlag = 'N' ",Integer.toString(assetCode));
 String query6 =app.getCodeName("select asset_code from am_AcceleratedDepreciation where asset_id = ? and ACCELERATED_STATUS = 'N' ",assetID);
// System.out.println("--query2--> "+query2+"  ---query3---> "+query3+"   ---query4-->  "+query4+"  ---query5-->  "+query5+"  ---query6--> "+query6);
  // For Revaluation Option if(query2.equalsIgnoreCase("") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("") && query5.equalsIgnoreCase("0")){process_status="";}else {process_status="D";}
  if(query2.equalsIgnoreCase("0") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("") && query6.equalsIgnoreCase("")){process_status="";}else {process_status="D";}  
//  if(query3.equalsIgnoreCase("")){process_status="";}else {process_status="D";}
//  System.out.println("--queryTest1--> "+queryTest1+"  ---queryTest2---> "+queryTest2);  
  if(queryTest1.equalsIgnoreCase(queryTest2) && !queryTest1.equalsIgnoreCase("") && !queryTest2.equalsIgnoreCase("")){
      //check if posted succceffuly on am_raisentry_TRANSACTION
String queryTest3 = app.getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code=? and trans_id=? ",Integer.toString(assetCode),queryTest1);
//  System.out.println("--queryTest2--> "+queryTest2+"  ---queryTest3---> "+queryTest3);    
if(process_status.equalsIgnoreCase("")){
if(queryTest3.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
}
//System.out.println("--process_status-1-> "+process_status);
  }
       else
            { 
             String queryTest4 = app.getCodeName("SELECT process_status from am_asset_approval WHERE process_status='A' and asset_id = ?",assetID);
             if(queryTest4.equalsIgnoreCase("A"))
             { 
                 String queryTest5 = app.getCodeName("select max(trans_id) from am_raisentry_post where  asset_code=",Integer.toString(assetCode));
//                 System.out.println("--queryTest1--> "+queryTest1);
                 String queryTest6 = app.getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code=",Integer.toString(assetCode));
//check is record in am_raisentry_TRANSACTION
//     System.out.println("--queryTest2--> "+queryTest2);
//                  System.out.println("--queryTest5--> "+queryTest5+"--queryTest6--> "+queryTest6);
                 if(queryTest5.equalsIgnoreCase(queryTest6) && !queryTest5.equalsIgnoreCase("") && !queryTest6.equalsIgnoreCase(""))
                 {
      //check if posted succceffuly on am_raisentry_TRANSACTION
                 String queryTest7 = app.getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code=? and trans_id=? "+assetCode,queryTest5);
//  System.out.println("--queryTest7--> "+queryTest7);
                 if(process_status.equalsIgnoreCase("")){
                 if(queryTest7.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
                 }
                 }//else {process_status="N";}
             }
//             System.out.println("--process_status-2-> "+process_status);
            }
           //  String   process =  app.getCodeName(postFlagQuery);
 //      System.out.println(" >>>>> process_status main......>>" +process_status);
            }
      

        } catch (Exception e) {
            System.out.println(
                    "AssetManager: checkAssetAvalability(String assetID):INFO:->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
            return process_status;
    }//

 public String checkAssetAvalability(String assetID,String tranType){
 //    System.out.println("\nttttttttttttttt");
ApprovalRecords app = new ApprovalRecords();
    String query = " SELECT transaction_id from am_asset_approval " +
                       " WHERE process_status='P' and asset_id = '"+assetID+"' and tran_type = '"+tranType+"'";
	df = new com.magbel.util.DatetimeFormat();

String transactionId ="";
String process_status ="";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Datefld = df.formatDate(new java.util.Date());
      //  System.out.println("====Datefld== "+Datefld);
        String Month = Datefld.substring(3, 5);
        String Year = Datefld.substring(6, 10);
    //    System.out.println("====Month== "+Month);
    //    System.out.println("====Year== "+Year);  
//        System.out.println("====query== "+query);  
              try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) { 
               transactionId = rs.getString("transaction_id");
//               String AssetId = rs.getString("ASSET_ID");
//                System.out.println("\n>>>>> trans id " + transactionId+"   AssetId: "+AssetId);
 //               System.out.println("\n>>>>> trans id " + transactionId);
               process_status =   transactionId;
            }
// System.out.println(" >>>transactionId>>>  "+transactionId );
 
            if(transactionId.equalsIgnoreCase("")){
 //System.out.println("\n >>>>>> here " );
           // String query1 = " SELECT transaction_id from am_asset_approval " +
             //          " WHERE process_status='A' and asset_id = '"+assetID+"'";
               //-- int tranId = Integer.parseInt(app.getCodeName(query1));
 String query1 = " SELECT asset_code from am_asset WHERE asset_id = ?";
 String asset_Code = app.getCodeName(query1,assetID);
 System.out.println("--asset_Code in checkAssetAvalability--> "+asset_Code);
              int assetCode = Integer.parseInt(app.getCodeName(query1,assetID));
     //     String postFlagQuery="select entryPostFlag from am_raisentry_post where entryPostFlag = 'N' and asset_code="+ assetCode;
// System.out.println("--assetCode--> "+assetCode);
  String queryTest1 = app.getCodeName("select max(trans_id) from am_raisentry_post where  asset_code=?",Integer.toString(assetCode));  
//                 System.out.println("--queryTest1--> "+queryTest1);
//  System.out.println("--queryTest1--> "+queryTest1);
  String queryTest2 = app.getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code=?",Integer.toString(assetCode));
//check is record in am_raisentry_TRANSACTION   

/*  Before legendPlus Upgrade 28/09/2018
  String query2 =app.getCodeName("select asset_code from am_asset_improvement where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"' and approval_status = 'PENDING' ");
  String query3 =app.getCodeName("select asset_code from am_asset_revaluation where asset_id = '"+assetID+"' and month(revalue_date) = '"+Month+"' and year(revalue_date) = '"+Year+"'");
  //String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100  and DISPOSAL_STATUS ='PD'");
  String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and month(disposal_date) = '"+Month+"' and year(disposal_date) = '"+Year+"'");
 String query5 =app.getCodeName("select count(*) from am_raisentry_post where asset_code = '"+assetCode+"'  and entryPostFlag = 'N' ");
 String query6 =app.getCodeName("select asset_code from am_AcceleratedDepreciation where asset_id = '"+assetID+"' and month(Accelerated_Date) = '"+Month+"' and year(Accelerated_Date) = '"+Year+"'");
*/
  String query2 =app.getCodeName("select asset_code from am_asset_improvement where asset_id = ? and approval_status = 'PENDING' ",assetID);
  if(query2.equals(""))query2 = "0";
  String query3 =app.getCodeName("select asset_code from am_asset_revaluation where asset_id = ? ",assetID);
  //String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = '"+assetID+"' and DISPOSAL_PERCENT = 100  and DISPOSAL_STATUS ='PD'");
  String query4 =app.getCodeName("select asset_code from am_AssetDisposal where asset_id = ? and DISPOSAL_PERCENT = 100 AND disposal_status != 'R' ",assetID);
 String query5 =app.getCodeName("select count(*) from am_raisentry_post where asset_code = ?  and entryPostFlag = 'N' ",Integer.toString(assetCode));
 String query6 =app.getCodeName("select asset_code from am_AcceleratedDepreciation where asset_id = ? and ACCELERATED_STATUS = 'N' ",assetID);
// System.out.println("--query2--> "+query2+"  ---query3---> "+query3+"   ---query4-->  "+query4+"  ---query5-->  "+query5+"  ---query6--> "+query6);
  // For Revaluation Option if(query2.equalsIgnoreCase("") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("") && query5.equalsIgnoreCase("0")){process_status="";}else {process_status="D";}
  if(query2.equalsIgnoreCase("0") && query3.equalsIgnoreCase("") && query4.equalsIgnoreCase("") && query6.equalsIgnoreCase("")){process_status="";}else {process_status="D";}  
//  if(query3.equalsIgnoreCase("")){process_status="";}else {process_status="D";}
//  System.out.println("--queryTest1--> "+queryTest1+"  ---queryTest2---> "+queryTest2);  
  if(queryTest1.equalsIgnoreCase(queryTest2) && !queryTest1.equalsIgnoreCase("") && !queryTest2.equalsIgnoreCase("")){
      //check if posted succceffuly on am_raisentry_TRANSACTION
String queryTest3 = app.getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code=? and trans_id=? ",Integer.toString(assetCode),queryTest1);
//  System.out.println("--queryTest2--> "+queryTest2+"  ---queryTest3---> "+queryTest3);    
if(process_status.equalsIgnoreCase("")){
if(queryTest3.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
}
//System.out.println("--process_status-1-> "+process_status);
  }
       else
            { 
             String queryTest4 = app.getCodeName("SELECT process_status from am_asset_approval WHERE process_status='A' and asset_id = ? and tran_type = ? ",assetID,tranType);
             if(queryTest4.equalsIgnoreCase("A"))
             { 
                 String queryTest5 = app.getCodeName("select max(trans_id) from am_raisentry_post where  asset_code=",Integer.toString(assetCode));
//                 System.out.println("--queryTest1--> "+queryTest1);
                 String queryTest6 = app.getCodeName("select max(trans_id)  from am_raisentry_TRANSACTION where asset_code=",Integer.toString(assetCode));
//check is record in am_raisentry_TRANSACTION
//     System.out.println("--queryTest2--> "+queryTest2);
//                  System.out.println("--queryTest5--> "+queryTest5+"--queryTest6--> "+queryTest6);
                 if(queryTest5.equalsIgnoreCase(queryTest6) && !queryTest5.equalsIgnoreCase("") && !queryTest6.equalsIgnoreCase(""))
                 {
      //check if posted succceffuly on am_raisentry_TRANSACTION
                 String queryTest7 = app.getCodeName("select iso  from am_raisentry_TRANSACTION where asset_code=? and trans_id=? "+assetCode,queryTest5);
//  System.out.println("--queryTest7--> "+queryTest7);
                 if(process_status.equalsIgnoreCase("")){
                 if(queryTest7.equalsIgnoreCase("000")){process_status="";}else {process_status="N";}
                 }
                 }//else {process_status="N";}
             }
//             System.out.println("--process_status-2-> "+process_status);
            }
           //  String   process =  app.getCodeName(postFlagQuery);
 //      System.out.println(" >>>>> process_status main......>>" +process_status);
            }
      

        } catch (Exception e) {
            System.out.println(
                    "AssetManager: checkAssetAvalability(String assetID):INFO:->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
            return process_status;
    }//

 public Transfer getWIPReclassificationAsset3(String asset_code)
 {
     //checkOldSection(id);
/*
     String query = " SELECT A.TRANSFER_ID,A.NEW_ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
             "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
             "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
             "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
             " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E," +
             " AM_AD_SECTION D WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
             "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_CODE = E.ASSET_CODE AND A.ASSET_CODE = '" + asset_code + "'";
*/
     String query = " SELECT A.TRANSFER_ID,A.NEW_ASSET_ID,A.OLD_DEPT_ID,A.OLD_BRANCH_ID,C.BRANCH_NAME," +
             "D.SECTION_NAME, A.OLD_SECTION,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.new_dept_id,A.new_section," +
             "new_branch_id, A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,A.NEW_CAT_ID,E.DESCRIPTION,E.CATEGORY_NAME," +
             "E.COST_PRICE,E.DATE_PURCHASED,A.NEW_WHO_TO_REM,A.NEW_EMAIL1,A.NEW_WHO_TO_REM2,A.NEW_EMAIL2,A.NEW_ASSET_USER" +
             " FROM AM_WIP_RECLASSIFICATION A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET_MAIN E," +
             " AM_AD_SECTION D WHERE A.OLD_BRANCH_ID = C.BRANCH_ID AND " +
             "A.OLD_SECTION = D.SECTION_ID AND A.ASSET_CODE = E.ASSET_CODE AND A.ASSET_CODE = '" + asset_code + "'";

//     System.out.println("query >>>> " + query);

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     ArrayList list = new ArrayList();
     //declare DTO object
    Transfer transfer = null;

     try {
         con = getConnection("legendPlus");
         ps = con.prepareStatement(query);
         rs = ps.executeQuery();

         while (rs.next())
         {
             int transferId = rs.getInt("TRANSFER_ID");
             String assetId = rs.getString("ASSET_ID");
             int oldDeptId = rs.getInt("OLD_DEPT_ID");
 //            String deptName = rs.getString("DEPT_NAME");
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
                                     "", oldBranchId, branchName,
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

 public String[] getWIPTransferedOldAssetID3(String asset_code) {

     String[] result = new String[10];
     //String old_asset_id="";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     //ArrayList list = new ArrayList();


     try
     {
        String query = "select Transfer_ID,old_branch_id, old_dept_id, old_section, old_asset_user,new_branch_id, new_dept_id, new_section " +
                       ",new_cat_code,old_cat_code from am_wip_reclassification where asset_code ='"+asset_code+"'";
//        System.out.println("the query >>>> "+query );
        con = getConnection("legendPlus");
         ps = con.prepareStatement(query);
         rs = ps.executeQuery();

         while (rs.next()) {
             result[0] = Integer.toString(rs.getInt("old_branch_id"));
             result[1] = Integer.toString(rs.getInt("old_dept_id"));
             result[2] = Integer.toString(rs.getInt("old_section"));
             result[3] = rs.getString("old_asset_user");
             result[4] = Integer.toString(rs.getInt("new_branch_id"));
             result[5] = Integer.toString(rs.getInt("new_dept_id"));
             result[6] = Integer.toString(rs.getInt("new_section"));
             result[7] = rs.getString("new_cat_code");
             result[8] = rs.getString("old_cat_code");
             result[9] = rs.getString("Transfer_ID");

         }

          for(int j=0; j<result.length;++j){
//              System.out.println("the value of a is " +result[j]);

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

 public Asset getAsset3(String asset_code,String assetId) {
     String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE asset_code = '"+asset_code+"' ";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     Asset _obj = null;

     try {
         con = getConnection("legendPlus");
         ps = con.prepareStatement(selectQuery);
         rs = ps.executeQuery();

         while (rs.next()) {
       //      String assetId = rs.getString("ASSET_ID");
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
             int assetCode = rs.getInt("asset_code");
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
             _obj.setAssetCode(assetCode);

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

 public void updateAssetWip(String asset_code)
 {

     int New_dept_id= 0;
     int New_branch_id= 0;
     String New_Asset_user = "";
     int New_Section = 0;
     java.sql.Date effDate =null;
     String NEW_BRANCH_CODE = "";
     String NEW_SECTION_CODE = "";
     String NEW_DEPT_CODE = "";
     String asset_id = "";
     String  new_asset_id = "";
     int new_cat_id= 0;
     String new_cat_code = "";



         int i = 0;


         String selectQuery = " select New_dept_id,New_branch_id,New_Asset_user,New_Section, " +
 		" effDate,NEW_BRANCH_CODE,NEW_SECTION_CODE,NEW_DEPT_CODE, " +
 		" asset_id,new_asset_id,new_cat_id,new_cat_code " +
 		" from am_wip_reclassification where asset_code = '"+asset_code+"' ";


   	 String updateQuery = 	" update am_asset  set DEPT_ID=?, BRANCH_ID=?, section_id=?, asset_user=?, " +
      		" BRANCH_CODE=?, DEPT_CODE=?, SECTION_CODE=?,CATEGORY_ID=?,CATEGORY_CODE=?,Effective_Date=?, " +
 		" asset_id=?, old_asset_id=? where asset_code ='"+asset_code+"' ";

         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             con = getConnection("legendPlus");


             ps = con.prepareStatement(selectQuery);
               rs = ps.executeQuery();

             while (rs.next())
             {
            	  New_dept_id= rs.getInt(1);
                  New_branch_id= rs.getInt(2);
                  New_Asset_user = rs.getString(3);
                  New_Section = rs.getInt(4);
                  effDate = rs.getDate(5);
                  NEW_BRANCH_CODE = rs.getString(6);
                  NEW_SECTION_CODE = rs.getString(7);
                  NEW_DEPT_CODE = rs.getString(8);
                  asset_id = rs.getString(9);
                  new_asset_id = rs.getString(10);
                  new_cat_id=  rs.getInt(11);
                  new_cat_code =  rs.getString(12);
             }

             ps = con.prepareStatement(updateQuery);

             ps.setInt(1, New_dept_id);
             ps.setInt(2, New_branch_id);
             ps.setInt(3, New_Section);
             ps.setString(4, New_Asset_user);
             ps.setString(5, NEW_BRANCH_CODE);
             ps.setString(6, NEW_DEPT_CODE);
             ps.setString(7, NEW_SECTION_CODE);
             ps.setInt(8, new_cat_id);
             ps.setString(9, new_cat_code);
             ps.setDate(10, effDate);
             ps.setString(11, new_asset_id);
             ps.setString(12, asset_id);
//             System.out.println("-------------------------------------------------------" );

             i = ps.executeUpdate();

         }
         catch (Exception e)
         {
             String warning = " ------ Error inserting Asset   ->"  ;
             System.out.println(warning);
             e.printStackTrace();
         }
         finally
         {
             closeConnection(con, ps,rs);
         }
}
 public void updateAssetReclass2(String asset_code)
 {

	     int new_cat_id=0;
	     double new_dep_rate=0;
	     String new_accum_dep = "";
	     String new_total_life = "";
	     String new_remaining_life = "";
	     java.sql.Date new_dep_end_date = null;
	     String new_monthly_dep = "";
	     String asset_id = "";
	     String new_asset_id = "";
              String new_nbv = "";

	     int i = 0;

	        String query1= "select new_category_id, new_depr_rate, " +
	        		"new_accum_dep,new_total_life,new_remaining_life,new_dep_end_date," +
	        		"new_monthly_dep,asset_id,new_asset_id,nbv from AM_ASSETRECLASSIFICATION where asset_code = '"+asset_code+"'";

	        String updateQuery = "UPDATE AM_ASSET SET category_id = ?," +
	                            " dep_rate = ? , accum_dep = ?, total_life = ?, remaining_life = ?, dep_end_date = ?," +
	        		" monthly_dep = ?,asset_id = ?,old_asset_id = ?,nbv=?,category_code = ? WHERE asset_code = ?";


	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            con = getConnection("legendPlus");


	            ps = con.prepareStatement(query1);
	              rs = ps.executeQuery();

	            while (rs.next()) {
	           new_cat_id= rs.getInt(1);
	           new_dep_rate= rs.getDouble(2);

                    new_accum_dep =  rs.getString(3);
	  	      new_total_life =  rs.getString(4);
	  	      new_remaining_life =  rs.getString(5);
	  	      new_dep_end_date = rs.getDate(6);
	  	     new_monthly_dep =  rs.getString(7);
	  	      asset_id =  rs.getString(8);
	  	     new_asset_id = rs.getString(9);
                    new_nbv = rs.getString(10);

	            }//while
	                 //////update am_asset table with approve values
	              ps = con.prepareStatement(updateQuery);
	              ps.setInt(1, new_cat_id);
	               ps.setDouble(2, new_dep_rate);


	               ps.setString(3,  new_accum_dep);
	               ps.setString(4, new_total_life );
	               ps.setString(5, new_remaining_life );
	               ps.setDate(6, new_dep_end_date);
	               ps.setString(7, new_monthly_dep );
	               ps.setString(8, new_asset_id );
	               ps.setString(9,  asset_id);
                        ps.setString(10, new_nbv);
                          ps.setString(11, code.getCategoryCode(Integer.toString(new_cat_id)));
	               ps.setString(12, asset_code);

	              i = ps.executeUpdate();




	        } catch (Exception e) {
	            String warning = "WARNING:Asset Manager class: updateAssetReclass(): Error inserting Asset Revaluation ->" +
	                             e.getMessage();
	            System.out.println(warning);
	            e.printStackTrace();
	        } finally {
	        	  System.out.println(">>>>>>>>----------------"+i);
	            closeConnection(con, ps,rs);
	        }


}
 public String[] getReclassedOldAssetID3(String asset_code) {

     String[] result = new String[4];
     //String old_asset_id="";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     //ArrayList list = new ArrayList();


     try {
        String query = "select old_category_id, reclassify_reason,old_depr_rate,recalc_depr from am_assetReclassification where asset_code ='"+asset_code+"'";
//         System.out.println("the query for getReclassedOldAssetID3 @@@@@@@@@@@@@@@@@@@ " + query);
         con = getConnection("legendPlus");
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
//              System.out.println("the value of a is " +result[j]);

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

 public int insertAssetTransfer(ArrayList list,String assetId, int oldDept, int newDept,
         int oldBranch,
         int newBranch, String oldUser,
         String newUser, int oldSection,
         int newSection,
         String raiseEntry, String transferDate,
         int userId, String whoToRem1,
         String email1, String whoToRem2,
         String email2, String effDate,String mtid,String categoryCode,
         String description,double cost,double nbv,double monthDep,double accumDep,
         int assetCode,String oldsbucode, String newsbucode,String newAsset_id) {
int i = 0;
ApprovalRecords app = new ApprovalRecords();
magma.AssetRecordsBean bd = null;
for (int j = 0; j < list.size(); j++) {
bd = (magma.AssetRecordsBean) list.get(j);
// public String getIdentity(String bra, String dep, String sec, String cat) throws 	
String assetid = bd.getAsset_id();
newAsset_id = bd.getNewasset_id();
String oldbranch = bd.getBranch_id();
String olddept = bd.getDepartment_id();
String oldsection = bd.getSection_id();
if(olddept==""){olddept = "0";}
String olddeptId = app.getCodeName("select Dept_ID from am_ad_department where  Dept_code=?",olddept);  
if(oldsection==""){oldsection = "0";}
String olduser = bd.getUser();
String oldsbu = bd.getSbu_code();
String olddescription = bd.getDescription();
String oldcat = bd.getCatCode();
String newbra = bd.getNewbranch_id();
String newdep = bd.getNewdepartment_id(); 
String newsec = bd.getNewsection_id();
String newsbu = bd.getNewsbu_code();
String newassetuser = bd.getNewuser();
double assetcost = Double.parseDouble(bd.getCost_price());
double nbvamt = Double.parseDouble(bd.getNbv());
double monthdep = Double.parseDouble(bd.getMonthlydep());
double accumdep = Double.parseDouble(bd.getAccumdep());
int assetcode = bd.getAssetCode();
String registrationNo = bd.getRegistration_no();

String insertQuery = "INSERT INTO AM_ASSETTRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
   "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
   "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE," +
   "NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION," +
   "Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE,NEW_ASSET_ID,Registration_No)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("<<<<<<<<<<<<<<<MATANMI TESTING>>>>>>>>");
Connection con = null;
PreparedStatement ps = null;
try {
	AssetRecordsBean arb = new AssetRecordsBean();	
con = getConnection("legendPlus");
ps = con.prepareStatement(insertQuery);
ps.setString(1, assetid);
ps.setInt(2, Integer.parseInt(olddeptId));
ps.setInt(3, Integer.parseInt(newdep));
ps.setInt(4, Integer.parseInt(oldbranch));
ps.setInt(5, Integer.parseInt(newbra));
ps.setString(6, oldUser);
ps.setString(7, newUser);
ps.setInt(8, Integer.parseInt(oldsection));
ps.setInt(9, Integer.parseInt(newsec));
ps.setString(10, raiseEntry);
ps.setDate(11, dateConvert(transferDate));
ps.setInt(12, userId);
ps.setDate(13, dateConvert(effDate));
ps.setString(14, code.getBranchCode(oldbranch));
ps.setString(15, code.getSectionCode(oldsection));
ps.setString(16, code.getDeptCode(olddeptId));
ps.setString(17, code.getBranchCode(newbra));
ps.setString(18, code.getSectionCode(newsec));
ps.setString(19, code.getDeptCode(newdep));
//System.out.println("insertAssetTransfer New MTID: "+mtid+String.valueOf(assetcode));
ps.setString(20,mtid+String.valueOf(assetcode));
ps.setString(21,oldcat);
ps.setString(22,olddescription);
ps.setDouble(23,assetcost);
ps.setDouble(24,nbvamt);
ps.setDouble(25,monthdep);
ps.setDouble(26,accumdep);
ps.setInt(27,assetcode);
ps.setString(28, oldsbu);
ps.setString(29, newsbu);
ps.setString(30, newAsset_id);
ps.setString(31, registrationNo);
i = ps.executeUpdate();


if (i > 0) {

  
String page1 = "ASSET TRANSFER RAISE ENTRY";
//query = "update am_raisentry_post set entrypostflag='Y' where id=? and page=? ";
//this is were new transfer information is set to am_asset table,
//System.out.println("New Asset Id in insertAssetTransfer: "+newAsset_id);
//29-10-2015   updateAssetTransfer(assetid);
//29-10-2015   String change_id_query2 = "update am_asset set old_asset_id ='" + assetid + "', asset_id ='" + newAsset_id + "' where asset_id ='" + assetid + "'";
// }
//29-10-2015   arb.updateAssetStatusChange(change_id_query2);

appManager = new ApprovalManager();
appManager.infoFromApproval(assetid, page1);
	
/*
ps = con.prepareStatement(updateQuery);
i = ps.executeUpdate();
ps = con.prepareStatement(updateFleetMaster);
i = ps.executeUpdate();
*/
}

} catch (Exception e) {
String warning = "WARNING:AssetManager: insertAssetTransfer() :Error inserting Asset Transfer ->" +
   e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
}
return i;
}

 public Accelerated getAcceleratedAsset(String assetId) {
     String selectQuery = "SELECT * FROM AM_ASSET_MAIN WHERE ASSET_ID = '" +
                          assetId + "' ";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     Accelerated _obj = null;

     try {
         con = getConnection("legendPlus");
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
             int assetCode = rs.getInt("asset_code");
             int usefullife  = rs.getInt("USEFUL_LIFE");
             int totallife  = rs.getInt("TOTAL_LIFE");
             int remainlife  = rs.getInt("REMAINING_LIFE");
            // int wht_percent = rs.getInt("WHT_PERCENT");
           //  String integrify = rs.getString("INTEGRIFY");
             _obj = new Accelerated(assetId, regNo, branchId, branchName, deptId,
                              deptName, sectionId, sectionName,
                              categoryId, categoryName, regionId, regionName,
                              description, datePurchased, depRate, make,
                              assetUser,
                              accumDep, monthDep, costPrice, depEndDate,
                              residualValue, raiseEntry, NBV, effDate,
                              vendorAcct, model,
                              engineNo, email1, email2, whoToRem1, whoToRem2,
                              reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                              subj2Wht, vatableCost,usefullife,totallife,remainlife);
             _obj.setAssetCode(assetCode);

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


 public AcceleratedAsset getAssetAccelerated(String id) {

     String query = "SELECT Accelerated_ID,ASSET_ID,Accelerated_reason,Accelerated_Months,Useful_life,Remaining_life," +
                    "Accelerated_Date,EFFECTIVE_DATE,USER_ID,Supervisor,Monthly_Dep, " +
                    "asset_code,Accum_Dep,NBV,ACCELERATED_AMOUNT,CR_DEPACCOUNT,DR_DEPACCOUNT " +
                    "FROM am_AcceleratedDepreciation WHERE ASSET_ID = '" + id + "'";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     //declare DTO object
     AcceleratedAsset acceleratedAsset = null;
     //DataConnect connect = new DataConnect();
     try {
         con = getConnection("legendPlus");
         ps = con.prepareStatement(query);
         rs = ps.executeQuery();

         while (rs.next()) {
             String acceleratedId = rs.getString("Accelerated_ID");
             String assetId = rs.getString("ASSET_ID");
             String reason = rs.getString("Accelerated_reason");
             int acceleratedmonths = rs.getInt("Accelerated_Months");
             int usefullife = rs.getInt("Useful_life");
             int remainlife = rs.getInt("Remaining_life");
             double accumdep = rs.getDouble("Accum_Dep");
             double nbv = rs.getDouble("NBV");
             double monthlydep = rs.getDouble("Monthly_Dep");
             double depramount = rs.getDouble("ACCELERATED_AMOUNT");
             String accelerateddate = formatDate(rs.getDate("Accelerated_Date"));
             String effDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
             String userId = rs.getString("USER_ID");
//             String narration = rs.getString("narration");
             String cracct =  rs.getString("CR_DEPACCOUNT");
             String dracct =  rs.getString("DR_DEPACCOUNT");
             acceleratedAsset = new AcceleratedAsset(acceleratedId, assetId, reason, acceleratedmonths,
            		 accumdep,usefullife, accelerateddate,
                     effDate, userId,remainlife, "", nbv,monthlydep,depramount,cracct,dracct);

         }

     } catch (Exception e) {
         System.out.println("INFO:Error fetching Accelerate Dereciation Asset by ID->" +
                            e.getMessage());
     } finally {
         closeConnection(con, ps, rs);
     }

     return acceleratedAsset;
 }

 public int insertAcceleratedDepreciationNoSupervisor(String assetId, String reason,
                                  String crAcct,String drAcct,double acceleAmount, String raiseEntry,
                                  double accumdep,double monthlydep,double nbv,
                                  String acceleDate, String effDate,int accelemonths,int usefullife,
                                  int remainlife,int userId,String supervise,int assetCode,int tranIDInt) {

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       CurrentDateTime cdt = new CurrentDateTime();
       int i = 0;
 //      String updateQuery =
//               "UPDATE AM_ASSET SET Accum_Dep='"+accumdep+"',NBV = '"+nbv+"',Useful_Life = '"+usefullife+"',Remaining_Life = '"+remainlife+"' WHERE ASSET_ID = '"+assetId+"'";

       String insertQuery =
               "INSERT INTO am_AcceleratedDepreciation(Accelerated_ID,ASSET_ID,ACCELERATED_REASON,DR_DEPACCOUNT," +
               "CR_DEPACCOUNT,ACCELERATED_AMOUNT,RAISE_ENTRY,ACCELERATED_DATE,EFFECTIVE_DATE,USER_ID,supervisor,ACCELERATED_status," +
               "Accelerated_Months,Useful_life,Remaining_life,Accum_Dep,NBV,Monthly_Dep,asset_code,Transaction_Id) " +
               "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       try {
       	
           con = getConnection("legendPlus");
           ps = con.prepareStatement(insertQuery);
           ps.setString(1, new ApplicationHelper().getGeneratedId("am_AcceleratedDepreciation"));
           ps.setString(2, assetId);
           ps.setString(3, reason);
           ps.setString(4, drAcct);
           ps.setString(5, crAcct);
           ps.setDouble(6, acceleAmount);
           ps.setString(7, raiseEntry);
           ps.setDate(8, dateConvert(acceleDate));
           ps.setDate(9, dateConvert(effDate));
           ps.setInt(10, userId);
           ps.setString(11,supervise);
           ps.setString(12, "N");
           ps.setInt(13, accelemonths);
           ps.setInt(14, usefullife);
           ps.setInt(15, remainlife);
           ps.setDouble(16, accumdep);
           ps.setDouble(17, nbv);   
           ps.setDouble(18, monthlydep);
           ps.setInt(19, assetCode);
           ps.setInt(20, tranIDInt);

           i = ps.executeUpdate();
           
//           if (i > 0) {   
 //              ps = con.prepareStatement(updateQuery);
 //              i = ps.executeUpdate();
 //          }

       } catch (Exception e) {
           System.out.println("AssetManager: insertAcceleratedDepreciationNoSupervisor(): INFO:Error Inserting Accelerated Depreciation Record ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }
       return i;
   }


 public int insertAcceleratedDepreciation(String assetId, String reason,
                                  String crAcct,String drAcct,double acceleAmount, String raiseEntry,
                                  double accumdep,double monthlydep,double nbv,
                                  String acceleDate, String effDate,int accelemonths,int usefullife,
                                  int remainlife,int userId,String supervise,int assetCode, int tranIDInt) {

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       CurrentDateTime cdt = new CurrentDateTime();
       int i = 0;
 //      String updateQuery =
//               "UPDATE AM_ASSET SET Accum_Dep='"+accumdep+"',NBV = '"+nbv+"',Useful_Life = '"+usefullife+"',Remaining_Life = '"+remainlife+"' WHERE ASSET_ID = '"+assetId+"'";

       String insertQuery =
               "INSERT INTO am_AcceleratedDepreciation(Accelerated_ID,ASSET_ID,ACCELERATED_REASON,DR_DEPACCOUNT," +
               "CR_DEPACCOUNT,ACCELERATED_AMOUNT,RAISE_ENTRY,ACCELERATED_DATE,EFFECTIVE_DATE,USER_ID,supervisor,ACCELERATED_status," +
               "Accelerated_Months,Useful_life,Remaining_life,Accum_Dep,NBV,Monthly_Dep,asset_code,Transaction_Id) " +
               "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       try {
       	
           con = getConnection("legendPlus");
           String mtid = new ApplicationHelper().getGeneratedId("am_AcceleratedDepreciation");
           ps = con.prepareStatement(insertQuery);
           ps.setString(1, mtid);
           ps.setString(2, assetId);
           ps.setString(3, reason);
           ps.setString(4, drAcct);
           ps.setString(5, crAcct);
           ps.setDouble(6, acceleAmount);
           ps.setString(7, raiseEntry);
           ps.setDate(8, dateConvert(acceleDate));
           ps.setDate(9, dateConvert(effDate));
           ps.setInt(10, userId);
           ps.setString(11,supervise);
           ps.setString(12, "N");
           ps.setInt(13, accelemonths);
           ps.setInt(14, usefullife);
           ps.setInt(15, remainlife);
           ps.setDouble(16, accumdep);
           ps.setDouble(17, nbv);   
           ps.setDouble(18, monthlydep);
           ps.setInt(19, assetCode);
           ps.setInt(20, Integer.parseInt(mtid));

           i = ps.executeUpdate();
           
//           if (i > 0) {   
 //              ps = con.prepareStatement(updateQuery);
 //              i = ps.executeUpdate();
 //          }

       } catch (Exception e) {
           System.out.println("AssetManager: insertAcceleratedDepreciation(): INFO:Error Inserting Accelerated Depreciation Record ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }
       return i;
   }
 
 public AssetRecordsBean getbulkAssetTransfer(String assetId) {
     String selectQuery = "SELECT * FROM am_gb_bulktransfer WHERE ASSET_ID = '" +
                          assetId + "' ";

     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     AssetRecordsBean aset = null;

     try {
         con = getConnection("legendPlus");
         ps = con.prepareStatement(selectQuery);
         rs = ps.executeQuery();

         while (rs.next()) {
             //String assetId = rs.getString("ASSET_ID");
    /*         String regNo = rs.getString("REGISTRATION_NO");
             int branchId = rs.getInt("OLDBRANCH_ID");
             int deptId = rs.getInt("OLDDEPT_ID");
             int sectionId = rs.getInt("OLDSECTION_ID");
             String oldsbucode = rs.getString("OLDSBU_CODE");
             int newbranchId = rs.getInt("NEWBRANCH_ID");
             int newdeptId = rs.getInt("NEWDEPT_ID");
             int newsectionId = rs.getInt("NEWSECTION_ID");
             int categoryId = rs.getInt("CATEGORY_ID");
             String newsbucode = rs.getString("NEWSBU_CODE");
             
             String description = rs.getString("DESCRIPTION");
             String datePurchased = formatDate(rs.getDate("TRANSFER_DATE"));
             */
//             System.out.println("getbulkAssetTransfer selectQuery: "+selectQuery);
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
           //  _obj.setWht_percent(wht_percent);

         }

     } catch (Exception e) {
         System.out.println("INFO:Error fetching Asset BY ID ->" +
                            e.getMessage());
     } finally {
         closeConnection(con, ps, rs);
     }

     return aset;

 }


 public ArrayList getStockByQuery(String query) {
        String selectQuery = "SELECT * FROM ST_STOCK WHERE " + query;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        Asset _obj = null;
        String branchName = "";
        String deptName = "";
       	String sectionName = "";
       	String categoryName = "";
       	String regionName = "";
       	int regionId = 0;
//       	System.out.println("getStockByQuery selectQuery: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String assetId = rs.getString("ASSET_ID");
                String regNo = rs.getString("REGISTRATION_NO");
                int branchId = rs.getInt("BRANCH_ID");
                //String branchName = rs.getString("BRANCH_NAME");
                int deptId = rs.getInt("DEPT_ID");
               // String deptName = rs.getString("DEPT_NAME");
                int sectionId = rs.getInt("SECTION_ID");
            //    String sectionName = rs.getString("SECTION_NAME");
                int categoryId = rs.getInt("CATEGORY_ID");
              //  String categoryName = rs.getString("CATEGORY_NAME");
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
             //   int regionId = rs.getInt("REGION_ID");
               // String regionName = rs.getString("REGION_NAME");
                String whoToRem1 = rs.getString("WHO_TO_REM");
                String whoToRem2 = rs.getString("WHO_TO_REM_2");
                String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
                double vatAmt = rs.getDouble("VAT");
                double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
                String subj2Vat = rs.getString("SUBJECT_TO_VAT");
                String subj2Wht = rs.getString("WH_TAX");
                double vatableCost = rs.getDouble("VATABLE_COST");
                int assetCode = rs.getInt("asset_code");
                double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
                double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
                double imprcostPrice = rs.getDouble("IMPROV_COST");
                double imprNBV = rs.getDouble("IMPROV_NBV");
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
                                 _obj.setAssetCode(assetCode);
                                 _obj.setImpraccumDep(impraccumDep);
                                 _obj.setImprcost(imprcostPrice);
                                 _obj.setImprmonthDep(imprmonthDep);
                                 _obj.setImprnbv(imprNBV);                                 
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

 public void processImprovement(String assetId, double CalcCost,double CalcNBV, double Calcvatable, double CalcVatAmt, double CalcWhtAmt, int usefullife, double cost, double vatableCost, String nbvresidual, int tranIdInt)
 {
	        String query1= "update am_asset set Cost_Price = "+CalcCost+", Monthly_Dep = 0.00, NBV = "+CalcNBV+", TOTAL_NBV = "+CalcNBV+", Vatable_Cost = "+Calcvatable+", VAT = "+CalcVatAmt+", Wh_Tax_Amount = "+CalcWhtAmt+" where asset_id ='"+assetId+"'";

	        String query2 = "update am_asset set IMPROV_COST = coalesce(IMPROV_COST,0) + "+cost+", IMPROV_NBV = coalesce(IMPROV_NBV,0) + "+cost+", TOTAL_NBV = coalesce(TOTAL_NBV,0) + "+cost+", IMPROV_VATABLECOST = coalesce(IMPROV_VATABLECOST,0) + "+vatableCost+", IMPROV_MONTHLYDEP = 0.00, IMPROV_REMAINLIFE = coalesce(IMPROV_REMAINLIFE,0) + "+usefullife+", IMPROV_TOTALLIFE = coalesce(IMPROV_TOTALLIFE,0) + "+usefullife+" where asset_id ='"+assetId+"'";

	        String query3 = "update FM_MASTER set MAINT_LTD = coalesce(MAINT_LTD,0) + "+cost+", MAINT_PTD = coalesce(MAINT_PTD,0) + "+cost+" where asset_id ='"+assetId+"'";
	        try {
	        	AssetRecordsBean arb = new AssetRecordsBean();
	        	if(Double.parseDouble(nbvresidual) > 10.00){
	              arb.updateAssetStatusChange(query1);
//	             System.out.println("<<<<<<query1 in processImprovement: "+query1); 
	        	} else{
	            	  arb.updateAssetStatusChange(query2);
//	            	  System.out.println("<<<<<<query2 in processImprovement: "+query2);
	              }
	        	arb.updateAssetStatusChange(query3);
	        	arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+assetId+"' AND page = 'ASSET IMPROVEMENT RAISE ENTRY'  and Trans_Id = '"+tranIdInt+"'");
//	        	System.out.println("MMMMMMMMM in processImprovement: ");
	        	arb.updateAssetStatusChange("update am_asset_approval set asset_status = 'ACTIVE', process_status = 'A' where transaction_id='"+tranIdInt+"'");
//	        	System.out.println("WWWWWWWWWWWWWW in processImprovement: ");
	        	arb.updateAssetStatusChange("update am_asset_improvement set IMPROVED = 'P',REVERSED = 'Y' where revalue_Id='"+tranIdInt+"'");
//	        	System.out.println("QQQQQQQQQQ in processImprovement: ");

	        } catch (Exception e) {
	            String warning = "WARNING:Asset Manager class: processImprovement(): Error Processing Improvement ->" +
	                             e.getMessage();
	            System.out.println(warning);
	            e.printStackTrace();
	        } finally {
	        	 // System.out.println(">>>>>>>>----------------"+i);
	           // closeConnection(con, ps,rs);
	        }
}
  
 public java.util.ArrayList getImprovedAssetRecords(String id)
 {
 	java.util.ArrayList _list = new java.util.ArrayList();
 	String date = String.valueOf(dateConvert(new java.util.Date()));
 	//System.out.println("====eschool Server Date-----> "+date);
 	date = date.substring(0, 10);
 	String finacleTransId= null;
 		String query = " SELECT Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date,"+
			"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost,R_VENDOR_ID,"+
			"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep,"+
			"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount,"+
			"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no,"+
			"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST,"+
			"IMPROVED,PROJECT_CODE,LOCATION  "+
			"FROM AM_GROUP_IMPROVEMENT WHERE REVALUE_ID = '"+id+"' AND RAISE_ENTRY = 'N' "; 		
 		
 		
// 	Transaction transaction = null;
//      System.out.println("====query in getNewAssetSqlRecords-----> "+query);
 	Connection c = null;
 	ResultSet rs = null;
 	Statement s = null; 

 	try {
 		//    c = getConnection();
 		   c = getConnection("legendPlus");
 			s = c.createStatement();
 			rs = s.executeQuery(query);
 			while (rs.next())
 			   {	
 				
				String assetid = rs.getString("asset_id");
			//	String description = rs.getString("Description");
				String groupId =  rs.getString("Revalue_ID");
				String costincrease = rs.getString("cost_increase");
				String branchId = rs.getString("branch_code");
				String costprice = rs.getString("cost_price");
				String description = rs.getString("Description");
				String revaluereason = rs.getString("revalue_reason");
				String vatablecost = rs.getString("vatable_cost");
				String vatamount = rs.getString("vat_amount");
				String vatrate = rs.getString("Subject_to_vat");
				String whtAmount = rs.getString("wht_amount");
				String newcostprice = rs.getString("new_cost_price");
				String newnbv = rs.getString("new_nbv");
				String newvatamount = rs.getString("new_vat_amount");
				String newvatablecost = rs.getString("new_vatable_cost");
				String usefullife = rs.getString("IMPROV_USEFULLIFE");
				String newwhtamount = rs.getString("new_wht_amount");
				String oldAccumDep = rs.getString("old_accum_dep");
				String oldCostPrice = rs.getString("old_cost_price");
				String oldimprovAccum = rs.getString("OLDIMPROV_ACCUMDEP");
				String oldimprovCost = rs.getString("OLD_IMPROV_COST");
				String oldimprovNBV = rs.getString("OLD_IMPROV_NBV");
				String oldimprovvatablecost = rs.getString("OLD_IMPROV_VATABLECOST");
				String oldnbv = rs.getString("old_nbv");
				String oldvatamount = rs.getString("old_vat_amount");
				String oldvatablecost = rs.getString("old_vatable_cost");
				String oldwhtAmount = rs.getString("old_wht_amount");
 				String projectCode = rs.getString("PROJECT_CODE");
 				String location = rs.getString("LOCATION");
 				String vendor = rs.getString("R_VENDOR_ID");
 				ExcelAssetImproveBean improvTrans = new ExcelAssetImproveBean();
 				improvTrans.setAsset_id(assetid);
 				improvTrans.setDescription(description);
 				improvTrans.setGid(groupId);
 				improvTrans.setCost_price(costprice);
 				improvTrans.setNewcost_price(newcostprice);
 				improvTrans.setNewvatable_cost(newvatablecost);
 				improvTrans.setNewvat_amount(newvatamount);
 				improvTrans.setNewwht_amount(newwhtamount);
 				improvTrans.setOldcost_price(oldCostPrice);
 				improvTrans.setNewnbv(newnbv);
 				improvTrans.setOldnbv(oldnbv); 
 				improvTrans.setOldaccum_dep(oldAccumDep);
 				improvTrans.setOldIMPROV_ACCUMDEP(oldimprovAccum);
 				improvTrans.setOldIMPROV_COST(oldimprovCost);	
 				improvTrans.setOldIMPROV_NBV(oldimprovNBV);
 				improvTrans.setOldIMPROV_VATABLECOST(oldimprovvatablecost);
 				improvTrans.setOldvat_amount(oldvatamount);
 				improvTrans.setOldvatable_cost(oldvatablecost);
 				improvTrans.setOldwht_amount(oldwhtAmount);
 				improvTrans.setUsefullife(usefullife);
 				improvTrans.setProjectCode(projectCode);
 				improvTrans.setLocation(location);
 				improvTrans.setVendor(vendor);
 				_list.add(improvTrans);
 			   }
 	 }   
 				 catch (Exception e)
 					{
 						e.printStackTrace();
 					}
 					finally
 					{
 						closeConnection(c, s, rs);
 					}
 	return _list;
 }

 public void processGroupImprovement(String assetId, double CalcCost,double CalcNBV, double Calcvatable, double CalcVatAmt, double CalcWhtAmt, int usefullife, double cost, double vatableCost, String nbvresidual, int tranIdInt)
 {
	        String query1= "update am_asset set Cost_Price = "+CalcCost+", Monthly_Dep = 0.00, NBV = "+CalcNBV+", TOTAL_NBV = "+CalcNBV+", Vatable_Cost = "+Calcvatable+", VAT = "+CalcVatAmt+", Wh_Tax_Amount = "+CalcWhtAmt+" where asset_id ='"+assetId+"'";

	        String query2 = "update am_asset set IMPROV_COST = coalesce(IMPROV_COST,0) + "+cost+", IMPROV_NBV = coalesce(IMPROV_NBV,0) + "+cost+", TOTAL_NBV = coalesce(TOTAL_NBV,0) + "+cost+", IMPROV_VATABLECOST = coalesce(IMPROV_VATABLECOST,0) + "+vatableCost+", IMPROV_MONTHLYDEP = 0.00, IMPROV_REMAINLIFE = coalesce(IMPROV_REMAINLIFE,0) + "+usefullife+", IMPROV_TOTALLIFE = coalesce(IMPROV_TOTALLIFE,0) + "+usefullife+" where asset_id ='"+assetId+"'";
	        
	        String query3 = "update FM_MASTER set MAINT_LTD = coalesce(MAINT_LTD,0) + "+cost+", MAINT_PTD = coalesce(MAINT_PTD,0) + "+cost+" where asset_id ='"+assetId+"'";
	        try {
	        	AssetRecordsBean arb = new AssetRecordsBean();
	        	if(Double.parseDouble(nbvresidual) > 10.00){
	              arb.updateAssetStatusChange(query1);
	//             System.out.println("<<<<<<query1: "+query1); 
	        	} else{
	            	  arb.updateAssetStatusChange(query2);
	//            	  System.out.println("<<<<<<query2: "+query2);
	              }
	        	arb.updateAssetStatusChange(query3);
	        	arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+assetId+"' AND page = 'ASSET IMPROVEMENT RAISE ENTRY'  and Trans_Id = '"+tranIdInt+"'");
	//        	System.out.println("MMMMMMMMM: ");
	        	arb.updateAssetStatusChange("update am_asset_approval set asset_status = 'ACTIVE', process_status = 'A' where transaction_id='"+tranIdInt+"'");
	//        	System.out.println("WWWWWWWWWWWWWW: ");
	        	arb.updateAssetStatusChange("update AM_GROUP_IMPROVEMENT set IMPROVED = 'Y', STATUS = 'POSTED' where revalue_Id='"+tranIdInt+"'");
	        	arb.updateAssetStatusChange("update am_asset_improvement set IMPROVED = 'Y', STATUS = 'POSTED' where revalue_Id='"+tranIdInt+"'");
	//        	System.out.println("QQQQQQQQQQ: ");

	        } catch (Exception e) {
	            String warning = "WARNING:Asset Manager class: processGroupImprovement(): Error Processing Improvement ->" +
	                             e.getMessage();
	            System.out.println(warning);
	            e.printStackTrace();
	        } finally {
	        	 // System.out.println(">>>>>>>>----------------"+i);
	           // closeConnection(con, ps,rs);
	        }
}

 
public java.util.ArrayList getUploadAssetRecords(String id)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT GROUP_ID,asset_id,ASSET_CODE,cost_price,"+
			"Vendor_AC,SUPPLIER_NAME,LOCATION, PROJECT_CODE,branch_code,category_code,description "+
			"FROM AM_GROUP_ASSET WHERE GROUP_ID = '"+id+"' AND RAISE_ENTRY = 'N' "; 		
		
		
//	Transaction transaction = null;
//     System.out.println("====query in getNewAssetSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		//    c = getConnection();
		   c = getConnection("legendPlus");
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {	
				
				String assetid = rs.getString("asset_id");
			//	String description = rs.getString("Description");
				String groupId =  rs.getString("GROUP_ID");
				String branchCode = rs.getString("branch_code");
				double costprice = rs.getDouble("cost_price");
				String description = rs.getString("Description");
				String vendorAccount = rs.getString("Vendor_AC");
				String vendorId = rs.getString("SUPPLIER_NAME");
				String assetCode = rs.getString("ASSET_CODE");
				String categoryCode = rs.getString("category_code");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
				GroupAsset groupTrans = new GroupAsset();
				groupTrans.setAssetId(assetid);
				groupTrans.setDescription(description);
				groupTrans.setGroupId(groupId);
				groupTrans.setCostPrice(costprice);
				groupTrans.setBranchCode(branchCode);
				groupTrans.setVendorAccount(vendorAccount);
				groupTrans.setVendorId(vendorId);
				groupTrans.setAsset_code(assetCode);
				groupTrans.setCategory(categoryCode);
				groupTrans.setProjectCode(projectCode);
				groupTrans.setLocation(location);
				_list.add(groupTrans);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}

public java.util.ArrayList getFleetAssetRecords(String id)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT GROUP_ID,TRANS_TYPE,BRANCH,BRANCH_ID,ASSET_ID,BAR_CODE,REGISTRATION_NO,LICENCE_NO,FIRST_DATE_OBTAINED,"+
			"NEXT_DATE_OBTAINED,COST_PRICE,POSTING_DATE,USER_ID,DESCRIPTION,SBU_CODE,VENDOR_ID,VENDOR_ACCT,GLACCOUNT,STATUS "+
			"FROM FT_GROUP_DUE_PERIOD WHERE GROUP_ID = '"+id+"' AND STATUS = 'APPROVED' "; 		
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 
//	 System.out.println("====query in getFleetAssetRecords-----> "+query);
	try {
		   c = getConnection("legendPlus");
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {					
				String assetid = rs.getString("asset_id");
				String branchCode = rs.getString("BRANCH");
				String groupId =  rs.getString("GROUP_ID");
				String costincrease = rs.getString("COST_PRICE");
				String branchId = rs.getString("BRANCH_ID");
				String costprice = rs.getString("COST_PRICE");
				String description = rs.getString("DESCRIPTION");
				String license = rs.getString("LICENCE_NO");
				String barCode = rs.getString("BAR_CODE");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String sbuCode = rs.getString("SBU_CODE");
				String vendorId = rs.getString("VENDOR_ID");
				String vendorAccount = rs.getString("VENDOR_ACCT");
				String glAccount = rs.getString("GLACCOUNT");
				String status = rs.getString("STATUS");
				FleetExcelUploadBean fleetTrans = new FleetExcelUploadBean();
				fleetTrans.setAsset_id(assetid);
				fleetTrans.setBranch_code(branchCode);
				fleetTrans.setDescription(description);
				fleetTrans.setGid(groupId);
				fleetTrans.setCost_price(costprice);
				fleetTrans.setBranch_id(branchId);
				fleetTrans.setBar_code(barCode);
				fleetTrans.setRegistration_no(registrationNo);
				fleetTrans.setSbu_code(sbuCode);
				fleetTrans.setVendorId(vendorId);
				fleetTrans.setVendor_account(vendorAccount);
				fleetTrans.setGlAccount(glAccount);
				_list.add(fleetTrans);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}
 
public void processImprovementReversal(String assetId, double oldCost,double oldNbv, double oldVatableCost, double oldVatAmt, double oldWhtAmt, int usefullife, double oldimprovCost, double oldimprovvatableCost, String nbvresidual, int tranIdInt,double oldimprovNBV,double oldimprovaccum)
{
	        String query1= "update am_asset set Cost_Price = "+oldCost+", Monthly_Dep = 0.00, NBV = "+oldNbv+", TOTAL_NBV = "+oldNbv+", Vatable_Cost = "+oldVatableCost+", VAT = "+oldVatAmt+", Wh_Tax_Amount = "+oldWhtAmt+" where asset_id ='"+assetId+"'";

	        String query2 = "update am_asset set IMPROV_COST = "+oldimprovCost+", IMPROV_NBV = "+oldimprovNBV+", TOTAL_NBV = "+oldimprovNBV+", IMPROV_VATABLECOST = "+oldimprovvatableCost+", IMPROV_MONTHLYDEP = 0.00, IMPROV_REMAINLIFE = coalesce(IMPROV_REMAINLIFE,0) - "+usefullife+", IMPROV_TOTALLIFE = coalesce(IMPROV_TOTALLIFE,0) - "+usefullife+" where asset_id ='"+assetId+"'";
	        
	        String query3 = "update FM_MASTER set MAINT_LTD = "+oldCost+", MAINT_PTD = "+oldCost+" where asset_id ='"+assetId+"'";
	        try {
	        	AssetRecordsBean arb = new AssetRecordsBean();
	        	if(Double.parseDouble(nbvresidual) > 10.00){
	              arb.updateAssetStatusChange(query1);
//	             System.out.println("<<<<<<query1 in processImprovementReversal: "+query1); 
	        	} else{
	            	  arb.updateAssetStatusChange(query2);
//	            	  System.out.println("<<<<<<query2 in processImprovementReversal: "+query2);
	              }
	        	arb.updateAssetStatusChange(query3);
	  //      	arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where id='"+assetId+"' AND page = 'ASSET IMPROVEMENT RAISE ENTRY'  and Trans_Id = '"+tranIdInt+"'");
	   //     	System.out.println("MMMMMMMMM in processImprovementReversal: ");
	  //      	arb.updateAssetStatusChange("update am_asset_approval set asset_status = 'ACTIVE', process_status = 'A' where transaction_id='"+tranIdInt+"'");
	   //     	System.out.println("WWWWWWWWWWWWWW in processImprovementReversal: ");
	        	arb.updateAssetStatusChange("update am_asset_improvement set REVERSED = 'P' where revalue_Id='"+tranIdInt+"'");
//	        	System.out.println("QQQQQQQQQQ in processImprovementReversal: ");

	        } catch (Exception e) {
	            String warning = "WARNING:Asset Manager class: processImprovementReversal(): Error Processing Improvement Reversal ->" +
	                             e.getMessage();
	            System.out.println(warning);
	            e.printStackTrace();
	        } finally {
	        	 // System.out.println(">>>>>>>>----------------"+i);
	           // closeConnection(con, ps,rs);
	        }
}

public ArrayList getAsset2ByQuery(String query) {
       String selectQuery = "SELECT * FROM AM_ASSET2_MAIN WHERE " + query;

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       ArrayList list = new ArrayList();
       Asset _obj = null;
//System.out.println("getAssetByQuery selectQuery: "+selectQuery);
       try {
           con = getConnection("legendPlus");
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
               int assetCode = rs.getInt("asset_code");
               double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
               double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
               double imprcostPrice = rs.getDouble("IMPROV_COST");
               double imprNBV = rs.getDouble("IMPROV_NBV");
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
                                _obj.setAssetCode(assetCode);
                                _obj.setImpraccumDep(impraccumDep);
                                _obj.setImprcost(imprcostPrice);
                                _obj.setImprmonthDep(imprmonthDep);
                                _obj.setImprnbv(imprNBV);                                 
               list.add(_obj);

           }

       } catch (Exception e) {
           System.out.println("INFO:Error fetching Asset by Query in getAsset2ByQuery ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }

       return list;

   }

public Asset getAsset2(String assetId) {
       String selectQuery = "SELECT b.BRANCH_NAME,c.category_name,d.Dept_name,s.Section_Name,'0' AS REGION_ID, 'NO REGION' AS REGION_NAME, * FROM AM_ASSET2 a, am_ad_branch b, am_ad_category c, am_ad_department d, am_ad_section s  WHERE a.branch_code = b.BRANCH_CODE and a.DEPT_CODE = d.Dept_code and a.CATEGORY_CODE = c.category_code and a.SECTION_CODE = s.Section_Code and ASSET_ID = '" +
                            assetId + "' ";
//System.out.println("<<<<<<<selectQuery in getAsset2: "+selectQuery);
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       Asset _obj = null;

       try {
           con = getConnection("legendPlus");
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
               int assetCode = rs.getInt("asset_code");
               double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
               double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
               double imprcostPrice = rs.getDouble("IMPROV_COST");
               double imprNBV = rs.getDouble("IMPROV_NBV");
              // int wht_percent = rs.getInt("WHT_PERCENT");
             //  String integrify = rs.getString("INTEGRIFY");
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
               _obj.setImpraccumDep(impraccumDep);
               _obj.setImprcost(imprcostPrice);
               _obj.setImprmonthDep(imprmonthDep);
               _obj.setImprnbv(imprNBV);
               _obj.setAssetCode(assetCode);

             //  _obj.setWht_percent(wht_percent);

           }

       } catch (Exception e) {
           System.out.println("INFO:Error fetching Asset2 BY ID in getAsset2 ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }

       return _obj;

   }

public int insertAsset2TransferNoSupervisor(String assetId, int oldDept, int newDept,
                                   int oldBranch,
                                   int newBranch, String oldUser,
                                   String newUser, int oldSection,
                                   int newSection,
                                   String raiseEntry, String transferDate,
                                   int userId, String whoToRem1,
                                   String email1, String whoToRem2,
                                   String email2, String effDate, String mtid,String categoryCode, String description,
                                   double cost, double nbv,double monthDep,double accumDep, int assetCode,String oldsbucode, String newsbucode) {
        int i = 0;
        String updateQuery = "UPDATE AM_ASSET2 SET DEPT_ID = '" + newDept +
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
                             "',SBU_CODE = '" +newsbucode +                             
                             "' WHERE ASSET_ID = '" + assetId + "'";


        String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,"+
                "OLD_ASSET_USER,NEW_ASSET_USER," +
                 "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                 "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
                 ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE)" +
                 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection("legendPlus");
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
            ps.setInt(27,assetCode);
 //           System.out.println("insertAssetTransferNoSupervisor oldsbucode: "+oldsbucode+"  newsbucode: "+newsbucode);
            ps.setString(28, oldsbucode);
            ps.setString(29, newsbucode);
            i = ps.executeUpdate();

            if (i > 0) {
                ps = con.prepareStatement(updateQuery);
                i = ps.executeUpdate();
//                ps = con.prepareStatement(updateFleetMaster);
//                i = ps.executeUpdate();

            }    
            closeConnection(con, ps);
        } catch (Exception e) {
            String warning = "WARNING:AssetManager: insertAsset2Transfer() :Error inserting Asset2 Transfer ->" +
                             e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return i;
    }


//insert asset2 transfer
  public int insertAsset2Transfer(String assetId, int oldDept, int newDept,
                                 int oldBranch,
                                 int newBranch, String oldUser,
                                 String newUser, int oldSection,
                                 int newSection,
                                 String raiseEntry, String transferDate,
                                 int userId, String whoToRem1,
                                 String email1, String whoToRem2,
                                 String email2, String effDate,String mtid,String categoryCode,
                                 String description,double cost,double nbv,double monthDep,double accumDep,
                                 int assetCode,String oldsbucode, String newsbucode) {
      int i = 0;

      String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                           "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                           "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE," +
                           "NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION," +
                           "Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("<<<<<<<<<<<<<<<MATANMI TESTING>>>>>>>>");
      Connection con = null;
      PreparedStatement ps = null;
      try {
          con = getConnection("legendPlus");
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
          ps.setInt(27,assetCode);
          ps.setString(28, oldsbucode);
          ps.setString(29, newsbucode);
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
          String warning = "WARNING:AssetManager: insertAsset2Transfer() :Error inserting Asset2 Transfer ->" +
                           e.getMessage();
          System.out.println(warning);
          e.printStackTrace();
      } finally {
          closeConnection(con, ps);
      }
      return i;
  }

  public Transfer getTransferedAssetTwo(String id)
  {
      checkOldSection(id);

      String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,D.SECTION_NAME," +
                     " A.OLD_SECTION,A.OLD_ASSET_USER,A.OLD_SBU_CODE,A.RAISE_ENTRY,A.TRANSFER_DATE,new_dept_id,new_section,new_branch_id," +
                     " A.NEW_SBU_CODE,A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
                     " FROM AM_ASSET2TRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_ASSET2_MAIN E, AM_AD_SECTION D" +
                     " WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID" +
                     " AND A.ASSET_ID = E.ASSET_ID AND A.ASSET_ID = '" + id + "'";
//      System.out.println("<<<<<<<<<<<<<<query IN getTransferedAssetTwo>>>>>>>> "+query);
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      ArrayList list = new ArrayList();
      //declare DTO object
     Transfer transfer = null;

      try {
          con = getConnection("legendPlus");
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
              String newsbucode = rs.getString("NEW_SBU_CODE");
              String oldsbucode = rs.getString("OLD_SBU_CODE");

               transfer = new Transfer(transferId, assetId, oldDeptId,
                                      deptName, oldBranchId, branchName,
                                      oldSectionId,sectionName, user,
                                      raiseEntry, transferDate, userId,
                                      categoryName, description, cost,
                                      datePurchased, regNo, effDate,
                                      categoryId, "", "",newBranch_id,newDept_id,newSection_id,newsbucode,oldsbucode);



              //list.add(transfer);

          }

      } catch (Exception e) {
          System.out.println(
                  "INFO:Error fetching All transfered Asset2 by ID ->" +
                  e.getMessage());
      } finally {
          closeConnection(con, ps, rs);
      }

      return transfer;
  }

  public int insertAsset2TransferNoSupervisor(String assetId, int oldDept, int newDept,
          int oldBranch,
          int newBranch, String oldUser,
          String newUser, int oldSection,
          int newSection,
          String raiseEntry, String transferDate,
          int userId, String whoToRem1,
          String email1, String whoToRem2,
          String email2, String effDate, String mtid) {
int i = 0;

String updateQuery = "UPDATE AM_ASSET2 SET DEPT_ID = '" + newDept +
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


String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID," +
"OLD_ASSET_USER,NEW_ASSET_USER," +
    "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
    "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
Connection con = null;
PreparedStatement ps = null;
try {
con = getConnection("legendPlus");
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

}

} catch (Exception e) {
String warning = "WARNING:AssetManager: insertAsset2Transfer() :Error inserting Asset2 Transfer ->" +
    e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
return i;
}


  //insert asset transfer
  public int insertAsset2Transfer(String assetId, int oldDept, int newDept,
                                 int oldBranch,
                                 int newBranch, String oldUser,
                                 String newUser, int oldSection,
                                 int newSection,
                                 String raiseEntry, String transferDate,
                                 int userId, String whoToRem1,
                                 String email1, String whoToRem2,
                                 String email2, String effDate,String mtid) {
      int i = 0;

      String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
                           "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
                           "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
               ",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      Connection con = null;
      PreparedStatement ps = null;
      try {
          con = getConnection("legendPlus");
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
          String warning = "WARNING:AssetManager: insertAsset2Transfer() :Error inserting Asset2 Transfer ->" +
                           e.getMessage();
          System.out.println(warning);
          e.printStackTrace();
      } finally {
          closeConnection(con, ps);
      }
      return i;
  }


public int insertAsset2TransferNoSupervisor(ArrayList list, int oldDept, int newDept,
        int oldBranch,
        int newBranch, String oldUser,
        String newUser, int oldSection,
        int newSection,
        String raiseEntry, String transferDate,
        int userId, String whoToRem1,
        String email1, String whoToRem2,
        String email2, String effDate, String mtid,String categoryCode, String description,
        double cost, double nbv,double monthDep,double accumDep, int assetCode,String oldsbucode, String newsbucode,String newAsset_id) {
int i = 0;
String query = "";

magma.AssetRecordsBean bd = null;
for (int j = 0; j < list.size(); j++) {
bd = (magma.AssetRecordsBean) list.get(j);
// public String getIdentity(String bra, String dep, String sec, String cat) throws 	
String assetid = bd.getAsset_id();
newAsset_id = bd.getNewasset_id();
String oldbranch = bd.getBranch_id();
String olddept = bd.getDepartment_id();
String oldsection = bd.getSection_id();
if(olddept==""){olddept = "0";}
if(oldsection==""){oldsection = "0";}
String olddescription = bd.getDescription();
String oldcat = bd.getCatCode();
String olduser = bd.getUser();
String oldsbu = bd.getSbu_code();
String newbra = bd.getNewbranch_id();
String newdep = bd.getNewdepartment_id(); 
String newsec = bd.getNewsection_id();
String newsbu = bd.getNewsbu_code();
String newassetuser = bd.getNewuser();
double assetcost = Double.parseDouble(bd.getCost_price());
double nbvamt = Double.parseDouble(bd.getNbv());
double monthdep = Double.parseDouble(bd.getMonthlydep());
double accumdep = Double.parseDouble(bd.getAccumdep());
int assetcode = bd.getAssetCode();
//System.out.println("Asset Id: "+assetid+"  New Branch Id: "+newbra+"  New Dept Id: "+newdep+"  New Section Id: "+newsec+"  New User Name: "+newassetuser);

String updateQuery = "UPDATE AM_ASSET2 SET DEPT_ID = '" + newdep +
  "',BRANCH_ID = '" + newbra + "'," +
  "SECTION_ID = '" + newsec +
  "',ASSET_USER = '" + newassetuser +
  "',WHO_TO_REM = '" + whoToRem1 +
  "',EMAIL1 = '" + email1 +
  "',WHO_TO_REM_2 = '" + whoToRem2 +
  "',EMAIL2 = '" + email2 +
  "',BRANCH_CODE = '" +
  code.getBranchCode(newbra) +
  "',SECTION_CODE = '" +
  code.getSectionCode(newsec) +
  "',DEPT_CODE = '" +
  code.getDeptCode(newdep) +
  "',SBU_CODE = '" +newsbu +   
  "',OLD_ASSET_ID = '" +assetid +   
  "',ASSET_ID = '" +newAsset_id +   
  "' WHERE ASSET_ID = '" + assetid + "'";


String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,"
+
"OLD_ASSET_USER,NEW_ASSET_USER," +
  "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
  "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE" +
",NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION,Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE,NEW_ASSET_ID)" +
" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
Connection con = null;
PreparedStatement ps = null;
try {
	AssetRecordsBean arb = new AssetRecordsBean();
con = getConnection("legendPlus");
ps = con.prepareStatement(insertQuery);
ps.setString(1, assetid);
ps.setInt(2, Integer.parseInt(olddept));
ps.setInt(3, Integer.parseInt(newdep));
ps.setInt(4, Integer.parseInt(oldbranch));
ps.setInt(5, Integer.parseInt(newbra));
ps.setString(6, olduser);  
ps.setString(7, newassetuser);
ps.setInt(8, Integer.parseInt(oldsection));
ps.setInt(9, Integer.parseInt(newsec));
ps.setString(10, raiseEntry);
ps.setDate(11, dateConvert(transferDate));
ps.setInt(12, userId);
ps.setDate(13, dateConvert(effDate));
ps.setString(14, code.getBranchCode(oldbranch));
ps.setString(15, code.getSectionCode(oldsection));
ps.setString(16, code.getDeptCode(olddept));
ps.setString(17, code.getBranchCode(newbra));
ps.setString(18, code.getSectionCode(newsec));
ps.setString(19, code.getDeptCode(newdep));
//System.out.println("insertAsset2TransferNoSupervisor New MTID: "+mtid+assetcode);
ps.setString(20, mtid+assetcode);
ps.setString(21, oldcat);
ps.setString(22, olddescription);
ps.setDouble(23,assetcost);
ps.setDouble(24,nbvamt);
ps.setDouble(25,monthdep);
ps.setDouble(26,accumdep);
ps.setInt(27,assetcode);
//System.out.println("insertAsset2TransferNoSupervisor oldsbucode: "+oldsbucode+"  newsbucode: "+newsbucode);
ps.setString(28, oldsbu);
ps.setString(29, newsbu);
ps.setString(30, newAsset_id);
i = ps.executeUpdate();

if (i > 0) {
ps = con.prepareStatement(updateQuery);
i = ps.executeUpdate();

String page1 = "ASSET2 TRANSFER RAISE ENTRY";

updateAssetTransfer(assetid);
String change_id_query2 = "update am_asset2 set old_asset_id ='" + assetid + "', asset_id ='" + newAsset_id + "' where asset_id ='" + assetid + "'";
// }
arb.updateAssetStatusChange(change_id_query2);

appManager = new ApprovalManager();
appManager.infoFromApproval(assetid, page1);

}    

} catch (Exception e) {
String warning = "WARNING:AssetManager: insertBulkAsset2Transfer() :Error inserting Asset2 Transfer ->" +
  e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
}
return i;
}


public int insertAsset2Transfer(ArrayList list,String assetId, int oldDept, int newDept,
        int oldBranch,
        int newBranch, String oldUser,
        String newUser, int oldSection,
        int newSection,
        String raiseEntry, String transferDate,
        int userId, String whoToRem1,
        String email1, String whoToRem2,
        String email2, String effDate,String mtid,String categoryCode,
        String description,double cost,double nbv,double monthDep,double accumDep,
        int assetCode,String oldsbucode, String newsbucode,String newAsset_id) {
int i = 0;

magma.AssetRecordsBean bd = null;
for (int j = 0; j < list.size(); j++) {
bd = (magma.AssetRecordsBean) list.get(j);
//public String getIdentity(String bra, String dep, String sec, String cat) throws 	
String assetid = bd.getAsset_id();
newAsset_id = bd.getNewasset_id();
String oldbranch = bd.getBranch_id();
String olddept = bd.getDepartment_id();
String oldsection = bd.getSection_id();
if(olddept==""){olddept = "0";}
if(oldsection==""){oldsection = "0";}
String olduser = bd.getUser();
String oldsbu = bd.getSbu_code();
String olddescription = bd.getDescription();
String oldcat = bd.getCatCode();
String newbra = bd.getNewbranch_id();
String newdep = bd.getNewdepartment_id(); 
String newsec = bd.getNewsection_id();
String newsbu = bd.getNewsbu_code();
String newassetuser = bd.getNewuser();
double assetcost = Double.parseDouble(bd.getCost_price());
double nbvamt = Double.parseDouble(bd.getNbv());
double monthdep = Double.parseDouble(bd.getMonthlydep());
double accumdep = Double.parseDouble(bd.getAccumdep());
int assetcode = bd.getAssetCode();

String insertQuery = "INSERT INTO AM_ASSET2TRANSFER(ASSET_ID,OLD_DEPT_ID,NEW_DEPT_ID,OLD_BRANCH_ID,NEW_BRANCH_ID,OLD_ASSET_USER,NEW_ASSET_USER," +
  "OLD_SECTION,NEW_SECTION,RAISE_ENTRY,TRANSFER_DATE,USER_ID,EFFDATE," +
  "OLD_BRANCH_CODE,OLD_SECTION_CODE,OLD_DEPT_CODE,NEW_BRANCH_CODE," +
  "NEW_SECTION_CODE,NEW_DEPT_CODE,TRANSFER_ID,OLD_CATEGORY_CODE,DESCRIPTION," +
  "Cost_Price,NBV,Monthly_Dep,Accum_Dep,asset_code,OLD_SBU_CODE,NEW_SBU_CODE,NEW_ASSET_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//System.out.println("<<<<<<<<<<<<<<<MATANMI TESTING>>>>>>>>");
Connection con = null;
PreparedStatement ps = null;
try {
	AssetRecordsBean arb = new AssetRecordsBean();	
con = getConnection("legendPlus");
ps = con.prepareStatement(insertQuery);
ps.setString(1, assetid);
ps.setInt(2, Integer.parseInt(olddept));
ps.setInt(3, Integer.parseInt(newdep));
ps.setInt(4, Integer.parseInt(oldbranch));
ps.setInt(5, Integer.parseInt(newbra));
ps.setString(6, oldUser);
ps.setString(7, newUser);
ps.setInt(8, Integer.parseInt(oldsection));
ps.setInt(9, Integer.parseInt(newsec));
ps.setString(10, raiseEntry);
ps.setDate(11, dateConvert(transferDate));
ps.setInt(12, userId);
ps.setDate(13, dateConvert(effDate));
ps.setString(14, code.getBranchCode(oldbranch));
ps.setString(15, code.getSectionCode(oldsection));
ps.setString(16, code.getDeptCode(olddept));
ps.setString(17, code.getBranchCode(newbra));
ps.setString(18, code.getSectionCode(newsec));
ps.setString(19, code.getDeptCode(newdep));
//System.out.println("insertAsset2Transfer New MTID: "+mtid+String.valueOf(assetcode));
ps.setString(20,mtid+String.valueOf(assetcode));
ps.setString(21,oldcat);
ps.setString(22,olddescription);
ps.setDouble(23,assetcost);
ps.setDouble(24,nbvamt);
ps.setDouble(25,monthdep);
ps.setDouble(26,accumdep);
ps.setInt(27,assetcode);
ps.setString(28, oldsbu);
ps.setString(29, newsbu);
ps.setString(30, newAsset_id);
i = ps.executeUpdate();


if (i > 0) {

 
String page1 = "ASSET2 TRANSFER RAISE ENTRY";

//System.out.println("New Asset Id in insertAsset2Transfer: "+newAsset_id);

appManager = new ApprovalManager();
appManager.infoFromApproval(assetid, page1);
}

} catch (Exception e) {
String warning = "WARNING:AssetManager: insertAsset2Transfer() :Error inserting Asset2 Transfer ->" +
  e.getMessage();
System.out.println(warning);
e.printStackTrace();
} finally {
closeConnection(con, ps);
}
}
return i;
}

public Disposal getDisposedAsset2(String id) {

    String query = "SELECT DISPOSAL_ID,ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT,DISPOSAL_AMOUNT,RAISE_ENTRY," +
                   "PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,DISPOSAL_STATUS,DISPOSAL_TYPE, " +
                   "DISPOSAL_PERCENT,PARTIAL_AMT " +
                   "FROM AM_ASSET2DISPOSAL WHERE ASSET_ID = '" + id + "'";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    //declare DTO object
    Disposal dispose = null;
    //DataConnect connect = new DataConnect();
    try {
        con = getConnection("legendPlus");
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
            String partialType = rs.getString("DISPOSAL_TYPE");
            double partialPercent = rs.getDouble("DISPOSAL_PERCENT");
            double partialAmt = rs.getDouble("PARTIAL_AMT");
            dispose = new Disposal(disposalId, assetId, reason, buyerAcct,
                                   amount,
                                   raiseEntry, profitLoss, disposalDate,
                                   effDate, userId, status, "", "", "", "",
                                   "", 0, "", "");
            dispose.setPartialAmt(partialAmt);
            dispose.setPartialPercent(partialPercent);
            dispose.setDisposalType(partialType);

        }

    } catch (Exception e) {
        System.out.println("INFO:Error fetching disposed Asset2 by ID->" +
                           e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }

    return dispose;
}

public int insertAsset2Disposal(String assetId, String reason,
                           String buyerAcct,
                           double disposalAmount, String raiseEntry,
                           double profitLoss,
                           String disposalDate, String effDate,
                           int userId,String supervise,int assetCode,String partialtype,double partialpercent,double partialamt,double disposedAccum,String disposalId) {

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
CurrentDateTime cdt = new CurrentDateTime();
int i = 0;

String updateQuery =
        "UPDATE AM_ASSET2 SET ASSET_STATUS=?,DATE_DISPOSED = ? WHERE ASSET_ID = ?";

String insertQuery =
        "INSERT INTO AM_ASSET2DISPOSAL(ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT," +
        "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status," +
        "DISPOSAL_TYPE,DISPOSAL_PERCENT,PARTIAL_AMT,DisposedAccum_Dep,asset_code) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
try {

    con = getConnection("legendPlus");
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
    ps.setString(10,disposalId);
//ps.setString(10, new ApplicationHelper()
//			.getGeneratedId("AM_ASSETDISPOSAL"));
    ps.setString(11,supervise);
    ps.setString(12, "P");  
    ps.setString(13, partialtype);
    ps.setDouble(14, partialpercent);
    ps.setDouble(15, partialamt);
    ps.setDouble(16, disposedAccum*partialpercent/100);
    ps.setInt(17,assetCode);
    i = ps.executeUpdate();

    if (i > 0) {
        ps = con.prepareStatement(updateQuery);
        ps.setString(1, "PENDING");
        ps.setDate(2, dateConvert(disposalDate));
        ps.setString(3, assetId);
        i = ps.executeUpdate();
    }

} catch (Exception e) {
    System.out.println("INFO:Error Inserting Disposal Asset2 ->" +
                       e.getMessage());
} finally {
    closeConnection(con, ps, rs);
}
return i;
}

public int insertAsset2DisposalNoSupervisor(String assetId, String reason,
                                 String buyerAcct,
                                 double disposalAmount, String raiseEntry,
                                 double profitLoss,
                                 String disposalDate, String effDate,
                                 int userId,String supervise,int assetCode,
                                 String partialtype,double partialpercent,double partialamt,String disposalId) {

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      CurrentDateTime cdt = new CurrentDateTime();
      int i = 0;
      String disposaltype = "";
      String updateQuery =
              "UPDATE AM_ASSET2 SET ASSET_STATUS=?,DATE_DISPOSED = ? WHERE ASSET_ID = ?";

      String insertQuery =
              "INSERT INTO AM_ASSET2DISPOSAL(ASSET_ID,DISPOSAL_REASON,BUYER_ACCOUNT," +
              "DISPOSAL_AMOUNT,RAISE_ENTRY,PROFIT_LOSS,DISPOSAL_DATE,EFFECTIVE_DATE,USER_ID,Disposal_ID,supervisor,disposal_status," +
              "DISPOSAL_TYPE,DISPOSAL_PERCENT,PARTIAL_AMT,asset_code) " +
              "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      try {
      	
          if(partialtype.equalsIgnoreCase("P")){disposaltype = "PARTIALLY DISPOSED";}
          else{disposaltype = "DISPOSED";}
          con = getConnection("legendPlus");
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
          ps.setString(10, disposalId);
          ps.setString(11,supervise);
          ps.setString(12, "D");
          ps.setString(13, partialtype);
          ps.setDouble(14, partialpercent);
          ps.setDouble(15, partialamt);            
          ps.setInt(16, assetCode);

          i = ps.executeUpdate();
          if (i > 0) {   
              ps = con.prepareStatement(updateQuery);
              ps.setString(1, disposaltype);
              ps.setDate(2, dateConvert(disposalDate));
              ps.setString(3, assetId);
              i = ps.executeUpdate();
          }

      } catch (Exception e) {
          System.out.println("AssetManager: insertAsset2DisposalNoSupervisor(): INFO:Error Inserting Disposal Asset2 ->" +
                             e.getMessage());
      } finally {
          closeConnection(con, ps, rs);
      }
      return i;
  }


public Asset getAssetforWIP(String assetId) {
       String selectQuery = "SELECT b.BRANCH_NAME,c.category_name,s.Section_Name,'0' AS REGION_ID, 'NO REGION' AS REGION_NAME, * FROM AM_ASSET a, am_ad_branch b, am_ad_category c, am_ad_section s  WHERE a.branch_code = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and a.SECTION_CODE = s.Section_Code and ASSET_ID = '" +
                            assetId + "' ";

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       Asset _obj = null;

       try {
           con = getConnection("legendPlus");
           ps = con.prepareStatement(selectQuery);
           rs = ps.executeQuery();

           while (rs.next()) {
               //String assetId = rs.getString("ASSET_ID");
               String regNo = rs.getString("REGISTRATION_NO");
               int branchId = rs.getInt("BRANCH_ID");
               String branchName = rs.getString("BRANCH_NAME");
//               int deptId = rs.getInt("DEPT_ID");
               int deptId = 0;
               String deptName = "";
//               String deptName = rs.getString("DEPT_NAME");
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
               int assetCode = rs.getInt("asset_code");
               double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
               double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
               double imprcostPrice = rs.getDouble("IMPROV_COST");
               double imprNBV = rs.getDouble("IMPROV_NBV");
              // int wht_percent = rs.getInt("WHT_PERCENT");
             //  String integrify = rs.getString("INTEGRIFY");
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
               _obj.setImpraccumDep(impraccumDep);
               _obj.setImprcost(imprcostPrice);
               _obj.setImprmonthDep(imprmonthDep);
               _obj.setImprnbv(imprNBV);
               _obj.setAssetCode(assetCode);

             //  _obj.setWht_percent(wht_percent);

           }

       } catch (Exception e) {
           System.out.println("INFO:Error fetching Asset BY ID in getAsset ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }

       return _obj;

   }

public ArrayList getDepAdjustmentforAsset(String query_)
{

 String query = "SELECT A.ID,A.ASSET_ID,A.accum_dep,A.new_accum_dep,A.nbv,A.new_nbv,"+
                  " A.reason,A.adjust_date,A.effDate,A.USER_ID,A.RAISE_ENTRY,"+
                  " B.BRANCH_ID,B.DEPT_ID,"+
                  " B.CATEGORY_ID,B.REGISTRATION_NO,B.DESCRIPTION,B.ASSET_USER,B.ASSET_STATUS,A.PAYMENTSTATUS"+
                  " FROM AM_ASSET_DEP_ADJUSTMENT A,AM_ASSET_MAIN B "+
                  " WHERE A.ASSET_ID = B.ASSET_ID "+query_;
// System.out.println(" I am inside the getDepAdjustmentforAsset method: "+query);
   Connection con = null;
   PreparedStatement ps = null;
   ResultSet rs = null; 

   ArrayList list = new ArrayList();
   //declare DTO object
   DepAdjustment adjust = null;

   try
   {
    con = getConnection("legendPlus");
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
     String paymentStatus = rs.getString("PAYMENTSTATUS");

     adjust = new DepAdjustment(adjustId,assetId,accumDep,newAccumDep,nbv,newNbv,reason,
                raiseEntry,adjustDate,effDate,userId,branchId,deptId,
                   categoryId,regNo,desc,assetUser,assetStatus,paymentStatus);

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


public ArrayList getAssetforDepAdjustment() {
    String selectQuery = "SELECT b.BRANCH_NAME,c.category_name,d.Dept_name,s.Section_Name,'0' AS REGION_ID, 'NO REGION' AS REGION_NAME, * FROM AM_ASSET a, am_ad_branch b, am_ad_category c, am_ad_department d, am_ad_section s  WHERE a.branch_code = b.BRANCH_CODE and a.DEPT_Id = d.Dept_Id and a.CATEGORY_CODE = c.category_code and a.SECTION_CODE = s.Section_Code and ASSET_ID = '";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList list = new ArrayList();
    Asset _obj = null;

    try {
        con = getConnection("legendPlus");
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
            int subcategoryId = rs.getInt("SUB_CATEGORY_ID");
            String categoryName = rs.getString("CATEGORY_NAME");
            String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
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
            String integrify = rs.getString("INTEGRIFY");
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
                             subj2Wht, vatableCost, integrify);
            _obj.setSubcategoryId(subcategoryId);
            _obj.setSubcategoryName(subcategoryName);
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


public java.util.ArrayList getUploadAssetDisposalRecords(String id)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;

        String query =
      			"SELECT disposal_ID,asset_id,disposalCost,disposal_reason,Disposal_Date,"+
					"User_ID,raise_entry,buyer_ac,disposalAmount,"+
					"vat_amount,wht_amount,nbv,accum_dep,cost_price,"+
					"effDate,approval_status,WHT_PERCENT,WH_TAX,Subject_to_vat,"+
					"branch_code,category_code,description,lpoNum,invoice_no,"+
					"asset_code,INTEGRIFY,DISPOSED,PROJECT_CODE,LOCATION  "+
					"FROM AM_GROUP_DISPOSAL WHERE DISPOSAL_ID='"+id+"' AND RAISE_ENTRY = 'N' ";
        
//	Transaction transaction = null;
//    System.out.println("====query in getNewAssetSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		//    c = getConnection();
		   c = getConnection("legendPlus");
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {	
				
				String assetid = rs.getString("asset_id");
			//	String description = rs.getString("Description");
				String groupId =  rs.getString("disposal_ID");
				String branchCode = rs.getString("branch_code");
				double costprice = rs.getDouble("disposalCost");
				String description = rs.getString("Description");
				String vendorAccount = rs.getString("buyer_ac");
//				String vendorId = rs.getString("SUPPLIER_NAME");
				String assetCode = rs.getString("ASSET_CODE");
				String categoryCode = rs.getString("category_code");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
			   String dateCreated = formatDate(rs.getDate("DISPOSAL_DATE"));

 			   String whtAmount  = rs.getString("wht_amount");
 			   String vat = rs.getString("vat_amount");
 			   double vatableCost = rs.getDouble("disposalAmount");
 			   String process_flag = rs.getString("DISPOSED");
 			//   String sbu_code = rs.getString("SBU_CODE");
 			   String assetcode = rs.getString("ASSET_CODE");
 			   String integrifyId = rs.getString("INTEGRIFY");

				ExcelAssetDisposalBean disposeTrans = new ExcelAssetDisposalBean();
				disposeTrans.setAsset_id(assetid);
				disposeTrans.setDescription(description);
				disposeTrans.setGid(groupId);
				disposeTrans.setCost_price(String.valueOf(costprice));
				disposeTrans.setBranch_code(branchCode);
				disposeTrans.setVendor_account(vendorAccount);
//				groupTrans.setVendorId(vendorId);
				disposeTrans.setAsset_code(assetCode);
				disposeTrans.setCategory_code(categoryCode);
				disposeTrans.setProjectCode(projectCode);
				disposeTrans.setLocation(location);
				disposeTrans.setDisposalAmount(String.valueOf(vatableCost));
				disposeTrans.setVat_amount(vat);
				disposeTrans.setAsset_code(assetcode);
				disposeTrans.setPosting_date(dateCreated);
				_list.add(disposeTrans);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}


public java.util.ArrayList getUploadUncapitalizedAssetRecords(String id)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
		String query = " SELECT GROUP_ID,asset_id,ASSET_CODE,cost_price,"+
			"Vendor_AC,SUPPLIER_NAME,LOCATION, PROJECT_CODE,branch_code,category_code,description "+
			"FROM AM_ASSET_UNCAPITALIZED WHERE GROUP_ID = '"+id+"' AND RAISE_ENTRY = 'N' "; 		
		
		
//	Transaction transaction = null;
//     System.out.println("====query in getNewAssetSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		//    c = getConnection();
		   c = getConnection("legendPlus");
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {	
				
				String assetid = rs.getString("asset_id");
			//	String description = rs.getString("Description");
				String groupId =  rs.getString("GROUP_ID");
				String branchCode = rs.getString("branch_code");
				double costprice = rs.getDouble("cost_price");
				String description = rs.getString("Description");
				String vendorAccount = rs.getString("Vendor_AC");
				String vendorId = rs.getString("SUPPLIER_NAME");
				String assetCode = rs.getString("ASSET_CODE");
				String categoryCode = rs.getString("category_code");
				String projectCode = rs.getString("PROJECT_CODE");
				String location = rs.getString("LOCATION");
				GroupAsset groupTrans = new GroupAsset();
				groupTrans.setAssetId(assetid);
				groupTrans.setDescription(description);
				groupTrans.setGroupId(groupId);
				groupTrans.setCostPrice(costprice);
				groupTrans.setBranchCode(branchCode);
				groupTrans.setVendorAccount(vendorAccount);
				groupTrans.setVendorId(vendorId);
				groupTrans.setAsset_code(assetCode);
				groupTrans.setCategory(categoryCode);
				groupTrans.setProjectCode(projectCode);
				groupTrans.setLocation(location);
				_list.add(groupTrans);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}


public String checkAssetUploadErrorlog(String assetID,String useId){
//    System.out.println("\nttttttttttttttt");
	String transType = "Asset Upload Disposal Posting";
ApprovalRecords app = new ApprovalRecords();
   String query = " insert into am_uploadCheckErrorLog(USER_ID,TRANSACTION_TYPE,ErrorDescription,errordate) " +
                  " select ?,?,a.Asset_id+'fail',? from am_Asset a, AM_GROUP_DISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_Id = ? and a.Asset_Status != ? and b.STATUS = ?";
   df = new com.magbel.util.DatetimeFormat();

String transactionId ="";
String process_status ="";
       Connection con = null;
       PreparedStatement ps = null;
       PreparedStatement ps2 = null;
       ResultSet rs = null;
       ResultSet rs2 = null;
       String Datefld = df.formatDate(new java.util.Date());
//       System.out.println("====Datefld== "+Datefld);
       String Day = Datefld.substring(0, 2);
       String Month = Datefld.substring(3, 5);
       String Year = Datefld.substring(6, 10);
       String errorDate = Year+"-"+Month+"-"+Day;
//       System.out.println("====errorDate== "+errorDate); 
             try {
           con = getConnection("legendPlus");
           ps = con.prepareStatement(query);
//           System.out.println("====query== "+query);
           ps.setString(1, useId);
//           System.out.println(">>>>> useId " + useId);
           ps.setString(2, transType);
//           System.out.println(">>>>>transType " + transType); 
           ps.setString(3, errorDate);
//           System.out.println(">>>>> errorDate" + errorDate);
           ps.setString(4, assetID);
//           System.out.println(">>>>> assetID " + assetID);
           ps.setString(5, "ACTIVE");
           ps.setString(6, "APPROVED");
           rs = ps.executeQuery();
//           while (rs2.next()) { 
////        	   ps.setString(1, useId);
////              transactionId = rs.getString("transaction_id");
////              String AssetId = rs.getString("ASSET_ID");
////               System.out.println("\n>>>>> trans id " + transactionId+"   AssetId: "+AssetId);
////               System.out.println("\n>>>>> trans id " + transactionId);
//              process_status =   "NOT AVAILABLE FOR DISPOSAL";
//           }
//           System.out.println(" >>>process_status>>>  "+process_status );

       } catch (Exception e) {
           System.out.println(
                   "AssetManager: checkAssetUploadErrorlog(String assetID):INFO:->" +
                   e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }
           return process_status;
   }//

public String checkAssetUploadAvalability(String assetID,String useId){
//    System.out.println("\nttttttttttttttt");
	String transType = "Asset Upload Disposal Posting";
ApprovalRecords app = new ApprovalRecords();
   String query = " select count(*) AS recordCount from am_Asset a, AM_GROUP_DISPOSAL b where a.Asset_id = b.Asset_id and b.disposal_Id = ? and a.Asset_Status != ? and b.STATUS = ?";

   df = new com.magbel.util.DatetimeFormat();

String transactionId ="";
String process_status ="";
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       String Datefld = df.formatDate(new java.util.Date());

             try {
           con = getConnection("legendPlus");
           ps = con.prepareStatement(query);
           ps.setString(1, assetID);
           ps.setString(2, "ACTIVE");
           ps.setString(3, "APPROVED");
           rs = ps.executeQuery();
           while (rs.next()) { 
//        	   ps.setString(1, useId);
              transactionId = rs.getString("recordCount");
//              String AssetId = rs.getString("ASSET_ID");
//               System.out.println("\n>>>>> trans id " + transactionId+"   AssetId: "+AssetId);
//               System.out.println("\n>>>>> trans id " + transactionId);
              process_status =   transactionId;
           }
//           System.out.println(" >>>process_status>>>  "+process_status );

       } catch (Exception e) {
           System.out.println(
                   "AssetManager: checkAssetUploadAvalability(String assetID):INFO:->" +
                   e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }
           return process_status;
   }//

public java.util.ArrayList getBatchPostingExceptionsRecords(String userId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
//	String finacleTransId= null;
//	System.out.println(" >>>userId>>>  "+userId );
		String query = " SELECT ID,GROUP_ID,ID_GROUP_ID,BATCH_NO,BRANCH_CODE,"+
				 "DATE_FIELD,ACCOUNT_NO,COST_PRICE,TRANSTYPE,DECRIPTION,TRANSCODE,"+
				 "CURRENCY,PURPOSE_CODE,MAKER,USER_ID,STATUS "+
				"FROM AM_GB_BATCH_POSTING WHERE USER_ID = ? AND STATUS = 'N' ";		
	Connection c = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Statement s = null; 
//	System.out.println(" >>>query>>>  "+query );
	try { 
		//    c = getConnection();
		   c = getConnection("legendPlus");
           ps = c.prepareStatement(query);
           ps.setString(1, userId);
           rs = ps.executeQuery();
           
			while (rs.next())
			   {	
				
				String id = rs.getString("ID");
				String description = rs.getString("DECRIPTION");
				String idgroupId =  rs.getString("ID_GROUP_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				double costprice = rs.getDouble("cost_price");
				String batchNo = rs.getString("BATCH_NO");
				String dateField = rs.getString("DATE_FIELD");
				String accountNo = rs.getString("ACCOUNT_NO");
				double costPrice = rs.getDouble("COST_PRICE");
				String transType = rs.getString("TRANSTYPE");
				String transCode = rs.getString("TRANSCODE");
				String currency = rs.getString("CURRENCY");
				String purposeCode = rs.getString("PURPOSE_CODE");
				String maker = rs.getString("MAKER");
//				String userId = rs.getString("USER_ID");
				String status = rs.getString("STATUS");
				BatchPosting trans = new BatchPosting(id, idgroupId, branchCode,costprice,
	                     batchNo, dateField, description, accountNo, transType, transCode,
	                     currency, purposeCode, maker, userId, status);
				_list.add(trans);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(c, s, rs);
					}
	return _list;
}

public Transfer getTransferedAssetAcceptance(String id)
{
    checkOldSection(id);

    String query = " SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,D.SECTION_NAME," +
                   " A.OLD_SECTION,A.OLD_ASSET_USER,A.OLD_SBU_CODE,A.RAISE_ENTRY,A.TRANSFER_DATE,new_dept_id,new_section,new_branch_id," +
                   " A.NEW_SBU_CODE,A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED " +
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
        con = getConnection("legendPlus");
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
            String newsbucode = rs.getString("NEW_SBU_CODE");
            String oldsbucode = rs.getString("OLD_SBU_CODE");

             transfer = new Transfer(transferId, assetId, oldDeptId,
                                    deptName, oldBranchId, branchName,
                                    oldSectionId,sectionName, user,
                                    raiseEntry, transferDate, userId,
                                    categoryName, description, cost,
                                    datePurchased, regNo, effDate,
                                    categoryId, "", "",newBranch_id,newDept_id,newSection_id,newsbucode,oldsbucode);



            //list.add(transfer);

        }

    } catch (Exception e) {
        System.out.println(
                "INFO:Error fetching All transfered Asset by ID in getTransferedAsset2 ->" +
                e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }

    return transfer;
}
public Transfer getTransferedAsset2Acceptance(String id) {

    String query = "SELECT A.TRANSFER_ID,A.ASSET_ID,A.OLD_DEPT_ID,B.DEPT_NAME,A.OLD_BRANCH_ID,C.BRANCH_NAME,A.OLD_SECTION,"
    		+ "D.SECTION_NAME,A.OLD_ASSET_USER,A.RAISE_ENTRY,A.TRANSFER_DATE,A.NEW_SBU_CODE,New_branch_id,New_dept_id,New_Section,"
    		+ "NEW_BRANCH_CODE,NEW_DEPT_CODE,NEW_SECTION_CODE,New_Asset_user,A.USER_ID,A.EFFDATE,E.REGISTRATION_NO,E.CATEGORY_ID,"
    		+ "E.CATEGORY_NAME,E.DESCRIPTION,E.COST_PRICE,E.DATE_PURCHASED "
    		+ "FROM AM_ASSETTRANSFER A,AM_AD_BRANCH C,AM_AD_DEPARTMENT B,AM_AD_SECTION D,AM_ASSET_MAIN E "
    		+ "WHERE A.OLD_DEPT_ID = B.DEPT_ID AND A.OLD_BRANCH_ID = C.BRANCH_ID AND A.OLD_SECTION = D.SECTION_ID "
    		+ "AND A.ASSET_ID = E.OLD_ASSET_ID AND  A.ASSET_ID = '" + id +
              "'";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    ArrayList list = new ArrayList();
    //declare DTO object
    Transfer transfer = null;

    try {
        con = getConnection("legendPlus");
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
            String sbucode = rs.getString("NEW_SBU_CODE");
            int newBranchId = rs.getInt("New_branch_id");
            int newSectionId = rs.getInt("New_Section");
            int newDeptId = rs.getInt("New_dept_id");
            String newAssetUser = rs.getString("New_Asset_user");
            String newBranchCode = rs.getString("NEW_BRANCH_CODE");
            String newDeptCode = rs.getString("NEW_DEPT_CODE");
            String newSectionCode = rs.getString("NEW_SECTION_CODE");
            
            //String assetUser = rs.getString("ASSET_USER");
            //String assetStatus = rs.getString("ASSET_STATUS");

            transfer = new Transfer(transferId, assetId, oldDeptId,
                                    deptName, oldBranchId, branchName,
                                    oldSectionId, sectionName, user,
                                    raiseEntry, transferDate, userId,
                                    categoryName, description, cost,
                                    datePurchased, regNo, effDate,
                                    categoryId, "", "",sbucode);
            transfer.setBranchId(newBranchId);
            transfer.setSectionId(newSectionId);
            transfer.setDeptId(newDeptId);
            transfer.setAssetUser(newAssetUser);
            transfer.setBranchCode(newBranchCode);
            transfer.setDeptCode(newDeptCode);
            transfer.setSectionCode(newSectionCode);
            
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


public Asset getAssetAcceptance(String assetId) {
       String selectQuery = "SELECT b.BRANCH_NAME,c.category_name,d.Dept_name,s.Section_Name,'0' AS REGION_ID, 'NO REGION' AS REGION_NAME, * FROM AM_ASSET a, am_ad_branch b, am_ad_category c, am_ad_department d, am_ad_section s  WHERE a.branch_code = b.BRANCH_CODE and a.DEPT_Id = d.Dept_Id and a.CATEGORY_CODE = c.category_code and a.SECTION_ID = s.Section_Id and ASSET_ID = '" +
                            assetId + "' ";

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       Asset _obj = null;

       try {
           con = getConnection("legendPlus");
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
               int assetCode = rs.getInt("asset_code");
               double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
               double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
               double imprcostPrice = rs.getDouble("IMPROV_COST");
               double imprNBV = rs.getDouble("IMPROV_NBV");
              // int wht_percent = rs.getInt("WHT_PERCENT");
             //  String integrify = rs.getString("INTEGRIFY");
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
               _obj.setImpraccumDep(impraccumDep);
               _obj.setImprcost(imprcostPrice);
               _obj.setImprmonthDep(imprmonthDep);
               _obj.setImprnbv(imprNBV);
               _obj.setAssetCode(assetCode);

             //  _obj.setWht_percent(wht_percent);

           }

       } catch (Exception e) {
           System.out.println("INFO:Error fetching Asset BY ID in getAsset ->" +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }

       return _obj;

   }


public ArrayList getAssetByQueryforWIP(String query) {
  String selectQuery = "SELECT * FROM AM_ASSET a, am_ad_category c WHERE a.Category_ID = c.category_ID " + query;
//  String selectQuery = "SELECT * FROM AM_ASSET WHERE ASSET_ID != ? " ;

  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  ArrayList list = new ArrayList();
  Asset _obj = null;
//System.out.println("getAssetByQuery query: "+query);
//System.out.println("getAssetByQuery selectQuery: "+selectQuery);
  try {
      con = getConnection("legendPlus");
      ps = con.prepareStatement(selectQuery);
//      ps.setString(1, query);
      rs = ps.executeQuery();

      while (rs.next()) {
          String assetId = rs.getString("ASSET_ID");
          String regNo = rs.getString("REGISTRATION_NO");
          int branchId = rs.getInt("BRANCH_ID");
//          String branchName = rs.getString("BRANCH_NAME");
          int deptId = rs.getInt("DEPT_ID");
//          String deptName = rs.getString("DEPT_NAME");
          int sectionId = rs.getInt("SECTION_ID");
//          String sectionName = rs.getString("SECTION_NAME");
          int categoryId = rs.getInt("CATEGORY_ID");
//          String categoryName = rs.getString("CATEGORY_NAME");
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
//          int regionId = rs.getInt("REGION_ID");
//          String regionName = rs.getString("REGION_NAME");
          String whoToRem1 = rs.getString("WHO_TO_REM");
          String whoToRem2 = rs.getString("WHO_TO_REM_2");
          String reqRedistbtn = rs.getString("REQ_REDISTRIBUTION");
          double vatAmt = rs.getDouble("VAT");
          double whtAmt = rs.getDouble("WH_TAX_AMOUNT");
          String subj2Vat = rs.getString("SUBJECT_TO_VAT");
          String subj2Wht = rs.getString("WH_TAX");
          double vatableCost = rs.getDouble("VATABLE_COST");
          int assetCode = rs.getInt("asset_code");
          double impraccumDep = rs.getDouble("IMPROV_ACCUMDEP");
          double imprmonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
          double imprcostPrice = rs.getDouble("IMPROV_COST");
          double imprNBV = rs.getDouble("IMPROV_NBV");
          _obj = new Asset(assetId, regNo, branchId, "", deptId,
                           "", sectionId, "",
                           categoryId, "", 0, "",
                           description, datePurchased, depRate, make,
                           assetUser,
                           accumDep, monthDep, costPrice, depEndDate,
                           residualValue, raiseEntry, NBV, effDate,
                           vendorAcct, model,
                           engineNo, email1, email2, whoToRem1, whoToRem2,
                           reqRedistbtn, vatAmt, whtAmt, subj2Vat,
                           subj2Wht, vatableCost);
                           _obj.setAssetCode(assetCode);
                           _obj.setImpraccumDep(impraccumDep);
                           _obj.setImprcost(imprcostPrice);
                           _obj.setImprmonthDep(imprmonthDep);
                           _obj.setImprnbv(imprNBV);                                 
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


 
}
