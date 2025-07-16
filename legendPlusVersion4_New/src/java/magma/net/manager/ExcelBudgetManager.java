/*
 * Festus Jejelowo
 *
 * Copyright (c) 2005-2006 MagBel Technology LTD. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of MagBel
 * Technology LTD. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Magbel.
 *
 * MAGBEL MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package magma.net.manager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import magma.net.vao.RFAReversal;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.RFID;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.CurrencyNumberformat;

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.*;

/**
 * <p>Title: ExcelBudgetManager.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Matanmi Lekan
 * @version 1.0
 * @see magma.net.dao.MagmaDBConnection
 */
public class ExcelBudgetManager extends MagmaDBConnection {
    /**
     * affectedRow int
     * No of rows processing affected.
     */
    private int affectedRow;
    private boolean tranSucessful;

    private CurrencyNumberformat formata;
    private SimpleDateFormat sdf;
    private ApprovalRecords records;
    
    public ExcelBudgetManager() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        formata = new CurrencyNumberformat();
        records= new ApprovalRecords();
    }

    public void setAffectedRow(int affectedRow) {
        this.affectedRow = affectedRow;
    }

    public void setTranSucessful(boolean tranSucessful) {
        this.tranSucessful = tranSucessful;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public boolean isTranSucessful() {
        return tranSucessful;
    }
    
    public void allocateQauertMaintenanceBudget(String categoryCode,
                                                String makeCode,
                                                double formYearBelow1,
                                                double formYearLG2,
                                                double formYearLG3,
                                                double formYearLG4,
                                                double formYearAbove4,
                                                int QAUTER_TYPE) {

        FleetHistoryManager fleetHistory = new FleetHistoryManager();
        ArrayList list = fleetHistory.findAssetByMakeCategory(categoryCode,
                makeCode);
        String financialStartDate = getFinancialStartDate();
        int columnCode = 0;

        for (int x = 0; x < list.size(); x++) {
            Asset aset = (Asset) list.get(x);

            String id = "";
            String assetId = aset.getId();
            String assetName = aset.getDescription();
            String make = makeCode;
            String category = aset.getCategory();
            String purchaseDate = aset.getDatePurchased();
            int age = calculateAssetAge(financialStartDate, purchaseDate);

            double yearBelow1 = 0.00d;
            double yearLG2 = 0.00d;
            double yearLG3 = 0.00d;
            double yearLG4 = 0.00d;
            double yearAbove4 = 0.00d;

            double allocatedAmount = 0.00d;

            if (age < 12) {
                allocatedAmount = formYearBelow1; //Less than 1year
                columnCode = 1;
            } else if (age > 12 && (age < 24 || age == 24)) {
                allocatedAmount = formYearLG2; // 1 < age <= 2years
                columnCode = 2;
            } else if (age > 24 && (age < 36 || age == 36)) {
                allocatedAmount = formYearLG3; // 2 < age <= 3years
                columnCode = 3;
            } else if (age > 36 && (age < 48 || age == 48)) {
                allocatedAmount = formYearLG4; // 3 < age <= 4 years
                columnCode = 4;
            } else {
                allocatedAmount = formYearAbove4; // above 4 years
                columnCode = 5;
            }
            logQaurterlyBudgetTransaction(assetId, allocatedAmount, QAUTER_TYPE);
        }

    }


    /**
     * copyBudgetToAll
     *
     * @param categoryCode String
     * @param makeCode String
     */
    public void allocateMaintenanceBudget(String categoryCode, String makeCode,
                                          double formYearBelow1,
                                          double formYearLG2,
                                          double formYearLG3,
                                          double formYearLG4,
                                          double formYearAbove4, String userid) {

        FleetHistoryManager fleetHistory = new FleetHistoryManager();
        AssetMakeAllocation makeAllocation = new AssetMakeAllocation("",
                "", "",
                makeCode, "", categoryCode, formYearBelow1, formYearLG2,
                formYearLG3,
                formYearLG4, formYearAbove4);
        allocateBudget(makeAllocation, userid);

        ArrayList list = fleetHistory.findAssetByMakeCategory(categoryCode,
                makeCode);
        String financialStartDate = getFinancialStartDate();
        int columnCode = 0;

        for (int x = 0; x < list.size(); x++) {
            Asset aset = (Asset) list.get(x);

            String id = "";
            String assetId = aset.getId();
            String assetName = aset.getDescription();
            String make = makeCode;
            String category = aset.getCategory();
            String purchaseDate = aset.getDatePurchased();
            int age = calculateAssetAge(financialStartDate, purchaseDate);

            double yearBelow1 = 0.00d;
            double yearLG2 = 0.00d;
            double yearLG3 = 0.00d;
            double yearLG4 = 0.00d;
            double yearAbove4 = 0.00d;

            double allocatedAmount = 0.00d;

            if (age < 12) {
                allocatedAmount = formYearBelow1; //Less than 1year
                columnCode = 1;
            } else if (age > 12 && (age < 24 || age == 24)) {
                allocatedAmount = formYearLG2; // 1 < age <= 2years
                columnCode = 2;
            } else if (age > 24 && (age < 36 || age == 36)) {
                allocatedAmount = formYearLG3; // 2 < age <= 3years
                columnCode = 3;
            } else if (age > 36 && (age < 48 || age == 48)) {
                allocatedAmount = formYearLG4; // 3 < age <= 4 years
                columnCode = 4;
            } else {
                allocatedAmount = formYearAbove4; // above 4 years
                columnCode = 5;
            }
            logAnnualMaintenanceBudgetTransaction(assetId, allocatedAmount);
        }
    }

    private int calculateAssetAge(String financialStartDate,
                                  String purchaseDate) {
        int monthDifference = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date finDate = null;
        Date purDate = null;

        try {
            finDate = sdf.parse(financialStartDate);
            purDate = sdf.parse(purchaseDate);

            long longFinDate = finDate.getTime();
            long longPurDate = purDate.getTime();

            long dateDiff = longFinDate - longPurDate;
            long yearMonth = (long) 1000 * 60 * 60 * 24 * 365;
            monthDifference = ((int) ((dateDiff / yearMonth))) * 12;

        } catch (Exception e) {
            System.out.println("WARNING :Error calculating Asset Age ->" +
                               e.getMessage());
        }

        return monthDifference;
    }

    /**
     * allocateBudget
     *
     * @param makeAllocation AssetMakeAllocation
     * @see <a href = "../vao/AssetMakeAllocation.html">magma.net.vao.AssetMakeAllocation</href>
     */
    public void allocateMaintenanceBudget(AssetMakeAllocation makeAllocation) {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO AM_MAINTENANCE_BUDGET(" +
                       "ASSET_ID,ASSET_DESC,ASSET_MAKE," +
                       "MAKE_CODE,CATEGORY,YEAR_BELOW1,YEAR_BETWEEN2," +
                       "YEAR_BETWEEN3,YEAR_BETWEEN4,YEAR_ABOVE4,ACC_START_DATE," +
                       "ACC_END_DATE)  " +
                       "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] financialDates = this.getFinancialDates();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.setString(1, makeAllocation.getAssetId());
            ps.setString(2, makeAllocation.getAssetName());
            ps.setString(3, makeAllocation.getMake());
            ps.setString(4, makeAllocation.getMakeCode());
            ps.setString(5, makeAllocation.getCategory());
            ps.setDouble(6, makeAllocation.getYearBelow1());
            ps.setDouble(7, makeAllocation.getYearLG2());
            ps.setDouble(8, makeAllocation.getYearLG3());
            ps.setDouble(9, makeAllocation.getYearLG4());
            ps.setDouble(10, makeAllocation.getYearAbove4());
            ps.setDate(11, dateConvert(financialDates[0]));
            ps.setDate(12, dateConvert(financialDates[1]));

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Maintenance Budget ->" +
                               e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }

    public void allocateBudget(AssetMakeAllocation makeAllocation,
                               String userid) {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO AM_MAINTENANCE_BUDGET(" +
                       "MAKE_CODE,CATEGORY,YEAR_BELOW1,YEAR_BETWEEN2," +
                       "YEAR_BETWEEN3,YEAR_BETWEEN4,YEAR_ABOVE4,ACC_START_DATE," +
                       "ACC_END_DATE,USER_ID)  " +
                       "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String[] financialDates = this.getFinancialDates();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.setString(1, makeAllocation.getMakeCode());
            ps.setString(2, makeAllocation.getCategory());
            ps.setDouble(3, makeAllocation.getYearBelow1());
            ps.setDouble(4, makeAllocation.getYearLG2());
            ps.setDouble(5, makeAllocation.getYearLG3());
            ps.setDouble(6, makeAllocation.getYearLG4());
            ps.setDouble(7, makeAllocation.getYearAbove4());
            ps.setDate(8, dateConvert(financialDates[0]));
            ps.setDate(9, dateConvert(financialDates[1]));
            ps.setString(10, userid);

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Maintenance Budget ->" +
                               e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }


    public void allocateFuelBudget(String category, String makeCode,
                                   String location, double amount) {
        String[] finDates = this.getFinancialDates();
        logFuelTransaction(category, makeCode, location, amount,
                           finDates[0]);
        String branchCode_Qry = "select DISTINCT branch_Id from am_ad_branch where BRANCH_NAME = '"+location+"' ";
    //    System.out.println("====>branchCode_Qry: "+branchCode_Qry);
        String locationId = records.getCodeName(branchCode_Qry);
        
        copyFuelBudgetToAll(category, makeCode, locationId, amount);
    }

    private void logFuelTransaction(String category, String makeCode,
                                    String location,
                                    double amount, String financialStartDate) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO AM_FUEL_BUDGET(" +
                       "MAKE_CODE,CATEGORY,AMOUNT,FIN_START_DATE,LOCATION)  " +
                       "VALUES (?,?,?,?,?)";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.setString(1, makeCode);
            ps.setString(2, category);
            ps.setDouble(3, amount);
            ps.setDate(4, dateConvert(financialStartDate));
            ps.setString(5, location);

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Fuel Budget ->" +
                               e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }

    public void copyFuelBudgetToAll(String category, String makeCode,
                                    String locationId, double amount) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE FT_FLEET_MASTER " +
                       "SET FUEL_BUDGET = FUEL_BUDGET + ?  " +
                       "WHERE ASSET_MAKE = ? AND LOCATION = ? " +
                       "AND CATEGORY_ID = ? ";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setString(2, makeCode);
            ps.setInt(3, Integer.parseInt(locationId));
            ps.setInt(4, Integer.parseInt(category));

            int affectedRows = ps.executeUpdate();
            this.setAffectedRow(affectedRows);

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Copying Fuel Budget ->" +
                               e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }

    private void logAnnualMaintenanceBudgetTransaction(String assetid,
            double amount) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE FT_FLEET_MASTER    " +
                       "SET MAINT_BUDGET =  MAINT_BUDGET + ? " +
                       "WHERE ASSET_ID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.setDouble(1, amount);
            ps.setString(2, assetid);

            ps.execute();

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error logging AnnualMaintenanceBudgetTransaction ->" +
                    e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }

    private void logQaurterlyBudgetTransaction(String assetid,
                                               double quarterAmount,
                                               int quaterType) {
        Connection con = null;
        PreparedStatement ps = null;

        String amountColumn = "";
        if (quaterType == 1) {
            amountColumn = " MAINT_Q1 = MAINT_Q1 + ?  ";
        } else if (quaterType == 2) {
            amountColumn = " MAINT_Q2 = MAINT_Q2 + ?  ";
        } else if (quaterType == 3) {
            amountColumn = " MAINT_Q3 = MAINT_Q3 + ?  ";
        } else {
            amountColumn = " MAINT_Q4 = MAINT_Q4 + ?  ";
        }

        String query = "UPDATE FT_FLEET_MASTER    " +
                       "SET " + amountColumn +
                       " ,MAINT_BUDGET = MAINT_BUDGET + ?  " +
                       "WHERE ASSET_ID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.setDouble(1, quarterAmount);
            ps.setDouble(2, quarterAmount);
            ps.setString(3, assetid);

            ps.execute();
            setAffectedRow(affectedRow + 1);
        } catch (Exception e) {
            System.out.println(
                    "INFO:Error logging QaurterlyBudgetTransaction ->" +
                    e.getMessage());
            setAffectedRow(affectedRow - 1);
        } finally {
            closeConnection(con, ps);
        }

    }


    /**
     * allocateBudget
     *
     * @param branchCode String
     * @param branchName String
     * @param firstQuaterAmount double
     * @param secondQuaterAmount double
     * @param thirdQuaterAmount double
     * @param totalAmount double
     * @param balanceAmount double
     */
    

    public void allocateBudget(String branchCode, String branchName,
    						   String categoryName,String subcategoryName,String sbuCode,
                               String category,String subcategory, 
                               double janAmount,double febAmount,double marAmount,double aprAmount,
                               double mayAmount,double junAmount,double julAmount,double augAmount,
                               double sepAmount,double octAmount,double novAmount,double decAmount,
                               double firstQuaterAmount,                               
                               double secondQuaterAmount,
                               double thirdQuaterAmount,
                               double fourthQuaterAmount,
                               double totalAmount,
                               double balanceAmount, String type,String groupId, String extrabudget) {
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "INSERT INTO AM_ACQUISITION_BUDGET_TMP(" +
                             "BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                             "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
                             "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION," +
                             "Q4_ALLOCATION,BALANCE_ALLOCATION,TYPE," +
                             "ACC_START_DATE,ACC_END_DATE,GROUP_ID,EXTRA_BUDGET) " +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  
        String[] financialDates = this.getFinancialDates();
 //       System.out.println("<<<<<<<<<<< INSIDE allocateBudget >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            ps.setString(3, subcategory);
            ps.setString(4, branchName);
            ps.setString(5, categoryName);
            ps.setString(6, subcategoryName);
            ps.setString(7, sbuCode);
            ps.setDouble(8, totalAmount);
            ps.setDouble(9, janAmount);
            ps.setDouble(10, febAmount);
            ps.setDouble(11, marAmount);
            ps.setDouble(12, aprAmount);
            ps.setDouble(13, mayAmount);
            ps.setDouble(14, junAmount);
            ps.setDouble(15, julAmount);
            ps.setDouble(16, augAmount);
            ps.setDouble(17, sepAmount);
            ps.setDouble(18, octAmount);
            ps.setDouble(19, novAmount);
            ps.setDouble(20, decAmount);
            ps.setDouble(21, firstQuaterAmount);
            ps.setDouble(22, secondQuaterAmount);
            ps.setDouble(23, thirdQuaterAmount);
            ps.setDouble(24, fourthQuaterAmount);
            ps.setDouble(25, (firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount));
            ps.setString(26, type);
            ps.setDate(27, dateConvert(financialDates[0]));
            ps.setDate(28, dateConvert(financialDates[1]));
            ps.setString(29, groupId);
            ps.setString(30, extrabudget);
            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public void allocateBudget(String branchCode, String branchName,
    						   String categoryName,String sbuCode,
                               String category, 
                               double firstQuaterAmount,                               
                               double secondQuaterAmount,
                               double thirdQuaterAmount,
                               double fourthQuaterAmount,
                               double totalAmount,
                               double balanceAmount, String type,String groupId,String extrabudget) {
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "INSERT INTO AM_ACQUISITION_BUDGET_TMP(" +
                             "BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                             "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION," +
                             "Q4_ALLOCATION,BALANCE_ALLOCATION,TYPE," +
                             "ACC_START_DATE,ACC_END_DATE,GROUP_ID,EXTRA_BUDGET) " +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  
        String[] financialDates = this.getFinancialDates();
 //       System.out.println("<<<<<<<<<<< INSIDE allocateBudget >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            ps.setString(3, branchName);
            ps.setString(4, categoryName);
            ps.setString(5, sbuCode);
            ps.setDouble(6, totalAmount);
            ps.setDouble(7, firstQuaterAmount);
            ps.setDouble(8, secondQuaterAmount);
            ps.setDouble(9, thirdQuaterAmount);
            ps.setDouble(10, fourthQuaterAmount);
            ps.setDouble(11, (firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount));
            ps.setString(12, type);
            ps.setDate(13, dateConvert(financialDates[0]));
            ps.setDate(14, dateConvert(financialDates[1]));
            ps.setString(15, groupId);
            ps.setString(16, extrabudget);
            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
 

    public void allocateBudgetafterApproval(String branchCode, String branchName,
			   String categoryName,String subcategoryName,String sbuCode,
            String category,String subcategory, 
            double janAmount,double febAmount,double marAmount,double aprAmount,
            double mayAmount,double junAmount,double julAmount,double augAmount,
            double sepAmount,double octAmount,double novAmount,double decAmount,
            double firstQuaterAmount,
            double secondQuaterAmount,
            double thirdQuaterAmount,
            double fourthQuaterAmount,
            double totalAmount,
            double balanceAmount, String type,String groupId) {
Connection con = null;
PreparedStatement ps = null;
String createQuery = "INSERT INTO AM_ACQUISITION_BUDGET(" +
          "BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
          "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
          "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION," +
          "Q4_ALLOCATION,BALANCE_ALLOCATION,TYPE," +
          "ACC_START_DATE,ACC_END_DATE,GROUP_ID) " +
          " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String[] financialDates = this.getFinancialDates();
System.out.println("<<<<<<<<<<< INSIDE allocateBudgetafterApproval >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName+"  category: "+category);
try {
con = getConnection("legendPlus");
ps = con.prepareStatement(createQuery);
ps.setString(1, branchCode);
ps.setString(2, category);
ps.setString(3, subcategory);
ps.setString(4, branchName);
ps.setString(5, categoryName);
ps.setString(6, subcategoryName);
ps.setString(7, sbuCode);
ps.setDouble(8, totalAmount);
ps.setDouble(9, janAmount);
ps.setDouble(10, febAmount);
ps.setDouble(11, marAmount);
ps.setDouble(12, aprAmount);
ps.setDouble(13, mayAmount);
ps.setDouble(14, junAmount);
ps.setDouble(15, julAmount);
ps.setDouble(16, augAmount);
ps.setDouble(17, sepAmount);
ps.setDouble(18, octAmount);
ps.setDouble(19, novAmount);
ps.setDouble(20, decAmount);
ps.setDouble(21, firstQuaterAmount);
ps.setDouble(22, secondQuaterAmount);
ps.setDouble(23, thirdQuaterAmount);
ps.setDouble(24, fourthQuaterAmount);
ps.setDouble(25, (firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount));
ps.setString(26, type);
ps.setDate(27, dateConvert(financialDates[0]));
ps.setDate(28, dateConvert(financialDates[1]));
ps.setString(29, groupId);
ps.execute();

} catch (Exception e) {
System.out.println("INFO:Error allocating Budget ->" + e.getMessage());
} finally {
closeConnection(con, ps);
}

}

    public void extralBudgetaryforApproval(String branchCode, String branchName,
  		   String categoryName,String sbuCode,
  	        String category,String subcategory, 
  	        double janAmount, double febAmount, double marAmount, double aprAmount, 
  	        double mayAmount, double junAmount, double julAmount, double augAmount, 
  	      	double sepAmount, double octAmount, double novAmount, double decAmount, 
  	        double firstQuaterAmount, double secondQuaterAmount, double thirdQuaterAmount,
  	        double fourthQuaterAmount) {
     	double totalAmount = firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount;
 /*        String updateQuery = " UPDATE AM_ACQUISITION_BUDGET " +
                              "SET TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                              "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                              "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                              " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SBU_CODE = ?";
  */
         String updateQuery = " UPDATE AM_ACQUISITION_BUDGET " +
        		 "SET JAN = JAN + ?,FEB = FEB + ?,MAR = MAR + ?,APR = APR + ?,MAY = MAY + ?,JUN = JUN + ?,"+
        		 " JUL = JUL + ?,AUG = AUG + ?,SEP = SEP + ?,OCT = OCT + ?,NOV = NOV + ?,DEC = DEC + ?," +
                 " TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                 "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                 "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                 " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ?  AND SUB_CATEGORY_CODE = ? AND SBU_CODE = ?";        
         Connection con = null;
         PreparedStatement ps = null;
//         System.out.println("<<<<<<<<<<< INSIDE extralBudgetaryforApproval >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName);
         try {

             con = getConnection("legendPlus");
             ps = con.prepareStatement(updateQuery);
             ps.setDouble(1, janAmount);
             ps.setDouble(2, febAmount);
             ps.setDouble(3, marAmount);
             ps.setDouble(4, aprAmount);
             ps.setDouble(5, mayAmount);
             ps.setDouble(6, junAmount);
             ps.setDouble(7, julAmount);
             ps.setDouble(8, augAmount);
             ps.setDouble(9, sepAmount);
             ps.setDouble(10, octAmount);
             ps.setDouble(11, novAmount);
             ps.setDouble(12, decAmount);
             ps.setDouble(13, totalAmount);
             ps.setDouble(14, firstQuaterAmount);
             ps.setDouble(15, secondQuaterAmount);
             ps.setDouble(16, thirdQuaterAmount);
             ps.setDouble(17, fourthQuaterAmount);
             ps.setDouble(18, totalAmount);
             ps.setString(19, branchCode);
             ps.setString(20, category);
             ps.setString(21, subcategory);
             ps.setString(22, sbuCode);

             ps.execute();

         } catch (Exception e) {
             System.out.println("INFO:Error Extral Budgetary in extralBudgetaryforApproval of Class extralBudgetary->" + e.getMessage());
         } finally {
             closeConnection(con, ps);
         }
     }

    
    /**
     * updateBudget
     *
     * @param branchCode String
     * @param firstQauterAmount double
     * @param secondQuaterAmount double
     * @param thirdQuaterAmount double
     * @param totalAmount double
     * @param balanceAmount double
     *  @param type String
     */
    public void updateBudget(String id, 
				  	        double janAmount, double febAmount, double marAmount, double aprAmount, 
				  	        double mayAmount, double junAmount, double julAmount, double augAmount, 
				  	      	double sepAmount, double octAmount, double novAmount, double decAmount, 				
    						 double firstQauterAmount,
                             double secondQuaterAmount,
                             double thirdQuaterAmount, double totalAmount,
                             double fourthQuarterAmount, double balanceAmount
            ) {

        String updateQuery = " UPDATE AM_ACQUISITION_BUDGET_TMP " +
				       		 "SET JAN = JAN + ?,FEB = FEB + ?,MAR = MAR + ?,APR = APR + ?,MAY = MAY + ?,JUN = JUN + ?,"+
				       		 " JUL = JUL + ?,AUG = AUG + ?,SEP = SEP + ?,OCT = OCT + ?,NOV = NOV + ?,DEC = DEC + ?," +
                             " TOTAL_ALLOCATION = ?,Q1_ALLOCATION = ?,Q2_ALLOCATION = ?,Q3_ALLOCATION = ?," +
                             "Q4_ALLOCATION = ?, BALANCE_ALLOCATION = ? " +
                             " WHERE LT_ID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setDouble(1, janAmount);
            ps.setDouble(2, febAmount);
            ps.setDouble(3, marAmount);
            ps.setDouble(4, aprAmount);
            ps.setDouble(5, mayAmount);
            ps.setDouble(6, junAmount);
            ps.setDouble(7, julAmount);
            ps.setDouble(8, augAmount);
            ps.setDouble(9, sepAmount);
            ps.setDouble(10, octAmount);
            ps.setDouble(11, novAmount);
            ps.setDouble(12, decAmount);
            
            ps.setDouble(13, totalAmount);
            ps.setDouble(14, firstQauterAmount);
            ps.setDouble(15, secondQuaterAmount);
            ps.setDouble(16, thirdQuaterAmount);
            ps.setDouble(17, fourthQuarterAmount);
            ps.setDouble(18, balanceAmount);
            ps.setInt(19, Integer.parseInt(id));

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error updating Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public boolean isDuplicateAcquisitionEntry(String branchCode,
                                               String category) {
        boolean isDuplicate = false;
        String searchQuery = "SELECT COUNT(TOTAL_ALLOCATION)    " +
                             "FROM AM_ACQUISITION_BUDGET   " +
                             "Where BRANCH_ID = ? and CATEGORY = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(searchQuery);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                int Counter = rs.getInt(1);
                if (Counter > 0) {
                    isDuplicate = true;
                }
            }

        } catch (Exception e) {
            System.out.println("WARNING:Error checking for duplicate entry ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return isDuplicate;

    }

    public boolean isDuplicateMaintenanceEntry(String makeCode, String category) {
        boolean isDuplicate = false;
        String searchQuery = "SELECT COUNT(TOTAL_ALLOCATION)    " +
                             "FROM AM_ACQUISITION_BUDGET   " +
                             "Where BRANCH_ID = ? and CATEGORY = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(searchQuery);
            ps.setString(1, makeCode);
            ps.setString(2, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                int Counter = rs.getInt(1);
                if (Counter > 0) {
                    isDuplicate = true;
                }
            }

        } catch (Exception e) {
            System.out.println("WARNING:Error checking for duplicate entry ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return isDuplicate;

    }


    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findAllBranchAllocation() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQueryQtr = "SELECT BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                             "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                             "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_ACQUISITION_BUDGET_TMP  " +
                             "GROUP BY BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME ";

        String selectQueryMonth = "SELECT BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME," +
                "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN," +
                "SUM(FEB) AS FEB," +
                "SUM(MAR) AS MAR," +
                "SUM(APR) AS APR," +
                "SUM(MAY) AS MAY," +
                "SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL," +
                "SUM(AUG) AS AUG," +
                "SUM(AUG) AS AUG," +
                "SUM(SEP) AS SEP," +
                "SUM(OCT) AS OCT," +
                "SUM(NOV) AS NOV," +
                "SUM(DEC) AS DEC," +
                "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION, EXTRA_BUDGET  " +
                "FROM AM_ACQUISITION_BUDGET_TMP  " +
                "GROUP BY BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,EXTRA_BUDGET ";
        
//        System.out.println("<<<<<<< selectQuery in findAllBranchAllocation of Class ExcelBudgetManager: "+selectQueryMonth);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQueryMonth);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String extrabudget = rs.getString("EXTRA_BUDGET");
                String type = "";

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,subcategory,sbuCode,branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type,extrabudget);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public ArrayList findAllBranchAllocation(String groupId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQueryQtr = "SELECT BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                             "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                             "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_ACQUISITION_BUDGET_TMP  " +
                             "GROUP BY BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME ";

        String selectQueryMonth = "SELECT BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,EXTRA_BUDGET," +
                "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN," +
                "SUM(FEB) AS FEB," +
                "SUM(MAR) AS MAR," +
                "SUM(APR) AS APR," +
                "SUM(MAY) AS MAY," +
                "SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL," +
                "SUM(AUG) AS AUG," +
                "SUM(AUG) AS AUG," +
                "SUM(SEP) AS SEP," +
                "SUM(OCT) AS OCT," +
                "SUM(NOV) AS NOV," +
                "SUM(DEC) AS DEC," +
                "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                "FROM AM_ACQUISITION_BUDGET_TMP  " +
                "WHERE GROUP_ID = '"+groupId+"'" +
                "GROUP BY BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,EXTRA_BUDGET ";
        
//        System.out.println("<<<<<<< selectQuery in findAllBranchAllocation of Class ExcelBudgetManager: "+selectQueryMonth);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQueryMonth);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";
                String extrabudget = rs.getString("EXTRA_BUDGET");
                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,subcategory,sbuCode,branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type,extrabudget);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    
    public ArrayList findFuelAllocationByMakeCategory(String assetMake,
            String category) {
        ArrayList finder = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,ASSET_ID,ASSET_DESC,ASSET_MAKE,MAKE_CODE," +
                "CATEGORY,AMOUNT,UPPER(LOCATION) AS LOCATION,FIN_START_DATE   " +
                "FROM AM_FUEL_BUDGET     " +
                "WHERE ASSET_MAKE = ? AND CATEGORY = ?  ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, assetMake);
            ps.setString(2, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                int categoryCount = 0;
                String location = rs.getString("LOCATION");
                String financialStartDate = rs.getString("FIN_START_DATE");
                double amount = rs.getDouble("AMOUNT");

                FuelAllocation allocation = new FuelAllocation(assetMake,
                        category, categoryCount, financialStartDate, amount,
                        location);

                finder.add(allocation);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findFuelAllocationByMakeCategory ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;
    }

    public double getAssetDistributionAmount(String assetId) {
        double distributionAmount = 0.00d;
        FleetHistoryManager historyManager = new FleetHistoryManager();
        Asset asset = historyManager.findAssetById(assetId);

        if (asset == null) {
            /*
             Skip and return nothing.
             */
        } else {
            double amountDifference = asset.getCost() - asset.getResidualValue();
            distributionAmount = (double) (amountDifference /
                                           (double) (asset.getTotalLife()));
        }
        return distributionAmount;
    }

    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findAllBranchAllocationByType(String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,CATEGORY   " +
                "FROM AM_ACQUISITION_BUDGET WHERE TYPE = '" + type + "'";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");                
                String sbuCode = rs.getString("SBU_CODE");
                String category = rs.getString("CATEGORY_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                // String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,subcategory,sbuCode,branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type,"");
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findAllBranchAllocationByType ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findBranchAllocation(String branchCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String branchQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                        "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
                		"Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,EXTRA_BUDGET   " +
                "FROM AM_ACQUISITION_BUDGET_TMP WHERE BRANCH_ID = '" +
                branchCode +
                "'";


//System.out.println("<<<<<<< selectQuery in findBranchAllocation: "+branchQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(branchQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String categoryCode = rs.getString("BRANCH_ID"); 
                String branchName = rs.getString("BRANCH_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = rs.getString("TYPE");
                String extrabudget = rs.getString("EXTRA_BUDGET");
                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,subcategory,sbuCode,branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        "","");
//                allocation.setCategory(category);
                list.add(allocation);
            }
 
        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findBranchAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findBranchCategoryAllocation(String branchCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
/*        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,TYPE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE BRANCH_ID = '" +
                branchCode +
                "'";
*/
        String selectQuery = "SELECT CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_ID,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME," +
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION,SUM(Q1_ALLOCATION) AS Q1_ALLOCATION,SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION,SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                "FROM AM_ACQUISITION_BUDGET WHERE BRANCH_ID = '" +branchCode +"'" +
                "GROUP BY CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_ID,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME ORDER BY CATEGORY_NAME";
        
//System.out.println("<<<<<<< selectQuery in findBranchCategoryAllocation: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
  //              String id = rs.getString("LT_ID");
                String categoryCode = rs.getString("CATEGORY_CODE"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
//                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
//                String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation("",
                        branchCode,categoryCode,subcategory,"",branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,                        
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        "","");
//                allocation.setCategory(category);
                list.add(allocation);
            }
 
        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findBranchCategoryAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findBranchAllocationByCode
     *
     * @param branchCode String
     * @return BranchAllocation
     */
    public BranchAllocation findBranchAllocationById(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BranchAllocation allocation = null;
        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                "BALANCE_ALLOCATION,TYPE,CATEGORY_CODE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE LT_ID = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String id = rs.getString("SysID");
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
//                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                String categoryCode = rs.getString("CATEGORY_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = rs.getString("TYPE");
                String category = rs.getString("CATEGORY_CODE");

                allocation = new BranchAllocation(id,branchCode,categoryCode,sbuCode,branchName,categoryName,
                        subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount, 
                        firstQauterAmount,secondQuaterAmount,thirdQauterAmount,fourthQauterAmount,
                        totalAmount, balanceAmount,type);
                allocation.setCategory(category);
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching  Budget allocation for Branch ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return allocation;

    }

    public void deleteBranchCategoryAllocation(String branchCode,
                                               String category,String subcategory) {
        String query = "DELETE FROM AM_ACQUISITION_BUDGET " +
                       "WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SUB_CATEGORY_CODE = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            ps.setString(3, subcategory);
            ps.execute();

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Deleting Category Allocation ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }


    public boolean isExistingBranchAllocation(String branchCode,
                    String category,String subcategory) {
        boolean isExisting = false;
        String query = "SELECT COUNT(CATEGORY) FROM AM_ACQUISITION_BUDGET " +
                       "WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SUB_CATEGORY_CODE = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            ps.setString(3, subcategory);
            rs = ps.executeQuery();

            while (rs.next()) {
                int counter = rs.getInt(1);
                if (counter > 0) {
                    isExisting = true;
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Checking existence of Category Allocation ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return isExisting;
    }

    public boolean isExistingFuelAllocation(String assetMake, String category) {
        boolean isExisting = false;
        String query = "SELECT COUNT(CATEGORY) FROM AM_FUEL_BUDGET " +
                       "WHERE ASSET_MAKE = ? AND CATEGORY = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, assetMake);
            ps.setString(2, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                int counter = rs.getInt(1);
                if (counter > 0) {
                    isExisting = true;
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Checking existence of FUEL Allocation ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return isExisting;
    }

    public void deleteFuelAllocation(String assetMake, String category) {
        String query = "DELETE FROM AM_FUEL_BUDGET " +
                       "WHERE ASSET_MAKE = ? AND CATEGORY = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setString(1, assetMake);
            ps.setString(2, category);
            ps.execute();

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error removing FUEL Allocation ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    /**
     * AssetMakeAllocation
     *
     * findBranchAllocationByCode
     *
     * @param branchCode String
     * @return BranchAllocation
     */
    public ArrayList findMaintenanceAllocationByCategory(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList allocations = new ArrayList();
        String selectQuery =
                "SELECT ASSET_MAKE,COUNT(MAKE_CODE) AS COUNTER,SUM(YEAR_BELOW1) AS YEAR_BELOW1," +
                "SUM(YEAR_BETWEEN2) AS YEAR_BETWEEN2,SUM(YEAR_BETWEEN3) AS YEAR_BETWEEN3," +
                "SUM(YEAR_BETWEEN4) AS YEAR_BETWEEN4,SUM(YEAR_ABOVE4) AS YEAR_ABOVE4   " +
                "FROM AM_MAINTENANCE_BUDGET  " +
                "GROUP BY ASSET_MAKE       " +
                "ORDER BY 1";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            // ps.setString(1, categoryCode);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = "";
                String assetId = "";
                String assetMake = rs.getString("ASSET_MAKE");
                String assetName = rs.getString("ASSET_MAKE");
                String makeCode = rs.getString("ASSET_MAKE"); ;
                String make = rs.getString("ASSET_MAKE");
                String category = "";
                int makeCount = rs.getInt("COUNTER");
                double yearBelow1 = rs.getDouble("YEAR_BELOW1");
                double yearLG2 = rs.getDouble("YEAR_BETWEEN2");
                double yearLG3 = rs.getDouble("YEAR_BETWEEN3");
                double yearLG4 = rs.getDouble("YEAR_BETWEEN4");
                double yearAbove4 = rs.getDouble("YEAR_ABOVE4");

                AssetMakeAllocation makeAllocation = new AssetMakeAllocation(id,
                        assetId, assetName, makeCode, make, category,
                        yearBelow1,
                        yearLG2, yearLG3, yearLG4, yearAbove4);
                makeAllocation.setMakeCount(makeCount);

                allocations.add(makeAllocation);
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching  make allocation list ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return allocations;

    }

    /**
     * AssetMakeAllocation
     *
     * findBranchAllocationByCode
     *
     * @param branchCode String
     * @return BranchAllocation
     */
    public ArrayList findMaintenanceSummaryAllocation() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList allocations = new ArrayList();
        String selectQuery =
                "SELECT B.ASSETMAKE,A.MAKE_CODE,COUNT(A.MAKE_CODE) AS COUNTER," +
                "SUM(A.YEAR_BELOW1) AS YEAR_BELOW1," +
                "SUM(A.YEAR_BETWEEN2) AS YEAR_BETWEEN2," +
                "SUM(A.YEAR_BETWEEN3) AS YEAR_BETWEEN3," +
                "SUM(A.YEAR_BETWEEN4) AS YEAR_BETWEEN4," +
                "SUM(A.YEAR_ABOVE4) AS YEAR_ABOVE4      " +
                "FROM AM_MAINTENANCE_BUDGET A, AM_GB_ASSETMAKE B  " +
                "WHERE A.MAKE_CODE = B.ASSETMAKE_ID      " +
                "GROUP BY B.ASSETMAKE,A.MAKE_CODE ORDER BY 1       ";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = "";
                String assetId = "";
                String assetMake = rs.getString("ASSETMAKE");
                String assetName = assetMake;
                String makeCode = rs.getString("MAKE_CODE"); ;
                String make = rs.getString("ASSETMAKE");
                String category = "";
                int makeCount = rs.getInt("COUNTER");
                double yearBelow1 = rs.getDouble("YEAR_BELOW1");
                double yearLG2 = rs.getDouble("YEAR_BETWEEN2");
                double yearLG3 = rs.getDouble("YEAR_BETWEEN3");
                double yearLG4 = rs.getDouble("YEAR_BETWEEN4");
                double yearAbove4 = rs.getDouble("YEAR_ABOVE4");

                AssetMakeAllocation makeAllocation = new AssetMakeAllocation(id,
                        assetId, assetName, makeCode, make, category,
                        yearBelow1,
                        yearLG2, yearLG3, yearLG4, yearAbove4);
                makeAllocation.setMakeCount(makeCount);

                allocations.add(makeAllocation);
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching  Maintenance Summary Allocation ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return allocations;

    }


    /**
     * AssetMakeAllocation
     *
     * findBranchAllocationByCode
     *
     * @param branchCode String
     * @return BranchAllocation
     */
    public ArrayList findFuelBudgetSummary() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList allocations = new ArrayList();
        String selectQuery =
                "SELECT MAKE_CODE,CATEGORY,LOCATION ,COUNT(MAKE_CODE) AS COUNTER," +
                "SUM(amount) AS AMOUNT  " +
                "FROM AM_FUEL_BUDGET    " +
                "GROUP BY MAKE_CODE, CATEGORY, LOCATION  " +
                "ORDER BY 1, 2, 3  ";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String assetMake = rs.getString("MAKE_CODE");
                String category = rs.getString("CATEGORY");
                int categoryCount = rs.getInt("COUNTER");
                String financialStartDate = "";
                double amount = rs.getDouble("AMOUNT");
                String location = rs.getString("LOCATION");

                FuelAllocation fuelAllocation = new FuelAllocation(
                        assetMake, category, categoryCount,
                        financialStartDate, amount, location);

                allocations.add(fuelAllocation);
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching fuel allocation Summary ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return allocations;

    }


    /**
     * describeCategory
     *
     * @param categoryCode String
     * @return String
     */
    public String describeCategory(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT CATEGORY_NAME FROM AM_AD_CATEGORY  " +
                             "WHERE CATEGORY_ID = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(categoryCode));
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Category->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }

    /**
     * describeCategory
     *
     * @param categoryCode String
     * @return String
     */
    public String describeMake(String makeid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT ASSETMAKE FROM am_gb_assetmake  " +
                             "WHERE ASSETMAKE_ID = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(makeid));
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Asset Make->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }


    /**
     * describeCategory
     *
     * @param categoryCode String
     * @return String
     */
    public String describeCategoryCode(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT CATEGORY_NAME FROM AM_AD_CATEGORY  " +
                             "WHERE CATEGORY_CODE = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, categoryCode);
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Category->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }
    public String getBranchName(String branchCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT BRANCH_NAME FROM am_ad_branch  " +
                             "WHERE BRANCH_ID = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(branchCode));
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }
 
        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Branch->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }



    /**
     * describeCategory
     *
     * @param categoryCode String
     * @return String
     */
    public String getCategoryName(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT CATEGORY_NAME FROM am_ad_category  " +
                             "WHERE CATEGORY_CODE = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(categoryCode));
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Category->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }
    public String getSubCategoryNameName(String subCategoryName) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String description = "";
        String selectQuery = "SELECT SUB_CATEGORY_NAME FROM AM_AD_SUB_CATEGORY  " +
                             "WHERE SUB_CATEGORY_CODE = ?";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(subCategoryName));
            rs = ps.executeQuery();

            while (rs.next()) {
                description = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error Describing Branch->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return description;

    }


    /**
     * describeCategory
     *
     * @param categoryCode String
     * @return String
     */
    public String getFinancialStartDate() {
        String dates[] = this.getFinancialDates();
        return dates[0];
    }

    public String[] getFinancialDates() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String[] finDates = new String[2];
        String selectQuery = "SELECT FINANCIAL_START_DATE,FINANCIAL_END_DATE " +
                             "FROM AM_GB_COMPANY";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                finDates[0] = sdf.format(rs.getDate(1));
                finDates[1] = sdf.format(rs.getDate(2));
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error fetching financial Dates ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finDates;

    }

    public void performYearEnd() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String UPDATE_QUERY = "UPDATE FT_FLEET_MASTER  " +
                              "SET PREMIUM_PTD = 0.0,MAINT_PTD = 0.0,FUEL_PTD = 0.0" +
                              ",ACCIDENT_COST_PTD = 0.0,LICENCE_PERMIT_PTD = 0.0" +
                              ",INSURANCE_COST_PTD = 0.0,MAINT_BUDGET = 0.0" +
                              ",MAINT_ACTUAL = 0.0,MAINT_Q1 = 0.0" +
                              ",MAINT_A1 = 0.0,MAINT_Q2 = 0.0,MAINT_A2 = 0.0" +
                              ",MAINT_Q3 = 0.0,MAINT_A3 = 0.0,MAINT_Q4 = 0.0" +
                              ",MAINT_A4 = 0.0,FUEL_BUDGET = 0.0,FUEL_ACTUAL = 0.0";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(UPDATE_QUERY);
            ps.execute();

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error performing year end ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }


    public boolean isQuarterlyPMRequired() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isRequired = false;
        String selectQuery = "SELECT require_quarterly_pm FROM AM_GB_COMPANY";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String required = rs.getString(1);
                if (required != null && required.equalsIgnoreCase("Y")) {
                    isRequired = true;
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error determing if PM is required ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return isRequired;

    }


    public String[] getAssetLocations() {

        String[] locations = null;
 //       String query = "SELECT DISTINCT(LOCATION) FROM am_gb_location ";
        String query = "SELECT DISTINCT(BRANCH_NAME) AS LOCATION FROM am_ad_branch  ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error fetching Asset Location ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        locations = (String[]) list.toArray(new String[list.size()]);
        return locations;

    }

    /**
     * extralBudgetary
     *
     * @param branchCode String
     * @param firstQauterAmount double
     * @param secondQuaterAmount double
     * @param thirdQuaterAmount double
     * @param totalAmount double
     * @param balanceAmount double
     *  @param type String
     */
    public void extralBudgetary(String branchCode, String branchName,
 		   String categoryName,String subcategoryName,String sbuCode,
 	        String category, String subcategory,
 	        double janAmount, double febAmount,  double marAmount,  double aprAmount,  double mayAmount,  double junAmount,
 	        double julAmount,  double augAmount,  double sepAmount,  double octAmount,  double novAmount,  double decAmount,
 	        double firstQuaterAmount,double secondQuaterAmount,double thirdQuaterAmount,
 	        double fourthQuaterAmount) {
    	double totalAmount = firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount;
/*        String updateQuery = " UPDATE AM_ACQUISITION_BUDGET " +
                             "SET TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                             "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                             "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                             " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SBU_CODE = ?";
 */
        String updateQuery = " UPDATE AM_ACQUISITION_BUDGET_TMP " +
        		"SET JAN = JAN + ?,FEB = FEB + ?,MAR = MAR + ?,APR = APR + ?,MAY = MAY + ?,JUN = JUN + ?," +
        		"JUL = JUL + ?,AUG = AUG + ?,SEP = SEP + ?,OCT = OCT + ?,NOV = NOV + ?,DEC = DEC + ?,"+
                "TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SUB_CATEGORY_CODE = ? AND SBU_CODE = ?";        
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(updateQuery);
            ps.setDouble(1, janAmount);
            ps.setDouble(2, febAmount);
            ps.setDouble(3, marAmount);
            ps.setDouble(4, aprAmount);
            ps.setDouble(5, mayAmount);
            ps.setDouble(6, junAmount);
            ps.setDouble(7, julAmount);
            ps.setDouble(8, augAmount);
            ps.setDouble(9, sepAmount);
            ps.setDouble(10, octAmount);
            ps.setDouble(11, novAmount);
            ps.setDouble(12, decAmount);
            ps.setDouble(13, totalAmount);
            ps.setDouble(14, firstQuaterAmount);
            ps.setDouble(15, secondQuaterAmount);
            ps.setDouble(16, thirdQuaterAmount);
            ps.setDouble(17, fourthQuaterAmount);
            ps.setDouble(18, totalAmount);
            ps.setString(19, branchCode);
            ps.setString(20, category);
            ps.setString(21, subcategory);
            ps.setString(22, sbuCode);

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error Extral Budgetary in extralBudgetary of Class ExcelBudgetManager ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findCategorySBUAllocation(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
        		"Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,TYPE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE CATEGORY_CODE = '" +
                categoryCode +
                "'";
//        System.out.println("<<<<<<< selectQuery in findCategorySBUAllocation: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String branchCode = rs.getString("BRANCH_ID"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategoryCode = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");

                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,categoryCode,subcategoryCode,sbuCode,branchName,categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,type,"");
//                allocation.setCategory(category);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findCategorySBUAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findAllBranchAllocationforView() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,BRANCH_NAME,SBU_CODE," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                             "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                             "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                             "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_ACQUISITION_BUDGET  " +
                             "GROUP BY BRANCH_ID,BRANCH_NAME,SBU_CODE ";
//        System.out.println("<<<<<<< selectQuery in findAllBranchAllocationforView: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
//                String categoryName = rs.getString("CATEGORY_NAME");
   //             String category = rs.getString("CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");

                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,"","",sbuCode,branchName,"", 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocationforView ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findBranchCategoryAllocationforView(String branchCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
/*        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,TYPE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE BRANCH_ID = '" +
                branchCode +
                "'";
*/
        String selectQuery = "SELECT CATEGORY_CODE,BRANCH_ID,SBU_CODE,BRANCH_NAME,CATEGORY_NAME," +
                "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                "FROM AM_ACQUISITION_BUDGET WHERE BRANCH_ID = '" +branchCode +"'" +
                "GROUP BY CATEGORY_CODE,BRANCH_ID,SBU_CODE,BRANCH_NAME,CATEGORY_NAME ORDER BY CATEGORY_NAME";
        
//System.out.println("<<<<<<< selectQuery in findBranchCategoryAllocationforView: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
  //              String id = rs.getString("LT_ID");
                String categoryCode = rs.getString("CATEGORY_CODE"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
//                String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation("",
                        branchCode,categoryCode,sbuCode,branchName,categoryName,subcategoryName,
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        "");
//                allocation.setCategory(category);
                list.add(allocation);
            }
 
        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findBranchCategoryAllocationforView ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllBranchAllocation
     *
     * @return ArrayList
     */
    public ArrayList findCategorySBUAllocationforView(String categoryCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,TYPE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE CATEGORY_CODE = '" +
                categoryCode +
                "'";
//        System.out.println("<<<<<<< selectQuery in findCategorySBUAllocationforView: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String branchCode = rs.getString("BRANCH_ID"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,categoryCode,sbuCode,branchName,categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount, 
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type);
//                allocation.setCategory(category);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations in findCategorySBUAllocationforView ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    public  java.util.ArrayList findFuelBudgetforApproval(String filter) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
  //      ArrayList list = new ArrayList();
        java.util.ArrayList list = new java.util.ArrayList();
/*        String selectQuery =
                "SELECT LT_ID,BRANCH_ID,CATEGORY_CODE,BRANCH_NAME,CATEGORY_NAME,SBU_CODE,TOTAL_ALLOCATION," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION," +
                " BALANCE_ALLOCATION,TYPE,TYPE   " +
                "FROM AM_ACQUISITION_BUDGET WHERE BRANCH_ID = '" +
                branchCode +
                "'";
*/
        String selectQuery = "SELECT LT_ID, CATEGORY_CODE,BRANCH_ID,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,DESCRIPTION," +
                "TOTAL_ALLOCATION,"+
                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION,BALANCE_ALLOCATION " +
                "FROM AM_FUEL_BUDGET_TMP WHERE STATUS IS NULL " +filter +" ";
        
//System.out.println("<<<<<<< selectQuery in findFuelBudgetforApproval: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) { 
                String id = rs.getString("LT_ID");
                String branchCode = rs.getString("BRANCH_ID"); 
                String categoryCode = rs.getString("CATEGORY_CODE"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String description = rs.getString("DESCRIPTION");
//                String type = rs.getString("TYPE");

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,categoryCode,sbuCode,branchName,categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount, 
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,"");
                allocation.setDescription(description);
                list.add(allocation);
            }
 
        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Fuel Budget allocations in findFuelBudgetforApproval ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    
    public ArrayList findAllBranchAllocationViewforApproval() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,SBU_CODE,BRANCH_NAME," +
			                "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
			                "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +        
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION," +
                             "SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION," +
                             "SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_ACQUISITION_BUDGET_TMP  " +
                             "GROUP BY BRANCH_ID,SBU_CODE,BRANCH_NAME ";
//        System.out.println("<<<<<<< selectQuery in findAllBranchAllocationViewforApproval: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
//                String categoryName = rs.getString("CATEGORY_NAME");
   //             String category = rs.getString("CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,"","",sbuCode,branchName,"", 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocationViewforApproval ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    public void allocateFuelBudget(String branchCode, String branchName,
    						   String categoryName,
                               String category,String sbuCode, double firstQuaterAmount,
                               double secondQuaterAmount,
                               double thirdQuaterAmount,
                               double fourthQuaterAmount,
                               double totalAmount,
                               double balanceAmount, String type,String groupId) {
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "INSERT INTO AM_FUEL_BUDGET_TMP(" +
                             "BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,TOTAL_ALLOCATION," +
                             "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
                             "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION," +
                             "Q4_ALLOCATION,BALANCE_ALLOCATION,TYPE," +
                             "ACC_START_DATE,ACC_END_DATE,GROUP_ID) " +
                             " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] financialDates = this.getFinancialDates();
//        System.out.println("<<<<<<<<<<< INSIDE allocateBudget >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName);
        try { 
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, branchCode);
            ps.setString(2, category);
            ps.setString(3, sbuCode);
            ps.setString(4, branchName);
            ps.setString(5, categoryName);
            ps.setDouble(6, totalAmount);
            ps.setDouble(7, firstQuaterAmount);
            ps.setDouble(8, secondQuaterAmount);
            ps.setDouble(9, thirdQuaterAmount);
            ps.setDouble(10, fourthQuaterAmount);
            ps.setDouble(11, (firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount));
            ps.setString(12, type);
            ps.setDate(13, dateConvert(financialDates[0]));
            ps.setDate(14, dateConvert(financialDates[1]));
            ps.setString(15, groupId);
            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error allocating Fuel Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
 
    public void extralFuelBudgetary(String branchCode, String branchName,
  		   String categoryName,String category,String sbuCode, double firstQuaterAmount,
  	        double secondQuaterAmount,
  	        double thirdQuaterAmount,
  	        double fourthQuaterAmount) {
     	double totalAmount = firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount;
 /*        String updateQuery = " UPDATE AM_ACQUISITION_BUDGET " +
                              "SET TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                              "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                              "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                              " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SBU_CODE = ?";
  */
         String updateQuery = " UPDATE AM_FUEL_BUDGET_TMP " +
                 "SET TOTAL_ALLOCATION = TOTAL_ALLOCATION + ?,Q1_ALLOCATION = Q1_ALLOCATION + ?," +
                 "Q2_ALLOCATION = Q2_ALLOCATION + ?,Q3_ALLOCATION = Q3_ALLOCATION + ?," +
                 "Q4_ALLOCATION = Q4_ALLOCATION + ?, BALANCE_ALLOCATION = BALANCE_ALLOCATION + ? " +
                 " WHERE BRANCH_ID = ? AND CATEGORY_CODE = ? AND SBU_CODE = ? ";        
         Connection con = null;
         PreparedStatement ps = null;
         String createQuery = "";

         try {

             con = getConnection("legendPlus");
             ps = con.prepareStatement(updateQuery);
             ps.setDouble(1, totalAmount);
             ps.setDouble(2, firstQuaterAmount);
             ps.setDouble(3, secondQuaterAmount);
             ps.setDouble(4, thirdQuaterAmount);
             ps.setDouble(5, fourthQuaterAmount);
             ps.setDouble(6, totalAmount);
             ps.setString(7, branchCode);
             ps.setString(8, category);
             ps.setString(9, sbuCode);

             ps.execute();

         } catch (Exception e) {
             System.out.println("INFO:Error Extral Budgetary in extralFuelBudgetary ->" + e.getMessage());
         } finally {
             closeConnection(con, ps);
         }
     }
    
    public ArrayList findAllBranchAllocationforFuelBudget() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                             "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION,SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION,SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION,EXTRA_BUDGET   " +
                             "FROM AM_FUEL_BUDGET_TMP  " +
                             "GROUP BY BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,EXTRA_BUDGET ";
//        System.out.println("<<<<<<< selectQuery in findAllBranchAllocationforFuelBudget: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";
                String extrabudget = rs.getString("EXTRA_BUDGET");
                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,subcategory,sbuCode,branchName,categoryName,subcategoryName,
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type,extrabudget);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    
    public void allocateFuelBudgetafterApproval(String branchCode, String branchName,
			   String make,String category, double firstQuaterAmount,
            double secondQuaterAmount,
            double thirdQuaterAmount, 
            double fourthQuaterAmount,
            double totalAmount,
            double balanceAmount, String type,String groupId) {
Connection con = null;
PreparedStatement ps = null;
String createQuery = "INSERT INTO AM_FUEL_BUDGET(" +
          "CATEGORY,LOCATION,MAKE_CODE,AMOUNT,FIN_START_DATE) " +
          " VALUES(?,?,?,?,?)";
String[] financialDates = this.getFinancialDates();
//System.out.println("<<<<<<<<<<< INSIDE allocateFuelBudgetafterApproval >>>>>>>> "+branchCode+"    branchName: "+branchName+"  Make: "+make+"  category: "+category);
try {
con = getConnection("legendPlus");
ps = con.prepareStatement(createQuery);
ps.setString(1, category);
ps.setString(2, branchName);
ps.setString(3, make);
ps.setDouble(4, totalAmount);
ps.setDate(5, dateConvert(financialDates[0]));
ps.execute();

} catch (Exception e) {
System.out.println("INFO:Error allocating Fuel Budget ->" + e.getMessage());
} finally {
closeConnection(con, ps);
}

}
    
    public void extralFuelBudgetaryforApproval(String branchCode, String branchName,
  		   String make,String category, double firstQuaterAmount,
  	        double secondQuaterAmount,
  	        double thirdQuaterAmount,
  	        double fourthQuaterAmount) {
     	double totalAmount = firstQuaterAmount+secondQuaterAmount+thirdQuaterAmount+fourthQuaterAmount;
         String updateQuery = " UPDATE AM_FUEL_BUDGET " +
                 "SET AMOUNT = AMOUNT + ?  WHERE LOCATION = ? AND CATEGORY = ? ";        
         Connection con = null;
         PreparedStatement ps = null;
//         System.out.println("<<<<<<<<<<< INSIDE extralFuelBudgetaryforApproval >>>>>>>> "+branchCode+"    branchName: "+branchName+"  categoryName: "+categoryName);
         try {
             con = getConnection("legendPlus");
             ps = con.prepareStatement(updateQuery);
             ps.setDouble(1, totalAmount);
             ps.setString(2, branchCode);
             ps.setString(3, category);
             ps.execute();

         } catch (Exception e) {
             System.out.println("INFO:Error Extral Budgetary in extralFuelBudgetaryforApproval ->" + e.getMessage());
         } finally {
             closeConnection(con, ps);
         }
     }

    
    public ArrayList findAllBranchAllocationView() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                             "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION,SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION,SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_ACQUISITION_BUDGET  " +
                             "GROUP BY BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME ";
//        					 System.out.println("<<<<<<< selectQuery in findAllBranchAllocationView: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,sbuCode,branchName,categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocationView ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }



    public ArrayList findAllBranchAllocationView(String filter)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList list;
        String selectQuery;
        con = null;
        ps = null;
        rs = null;
        list = new ArrayList();
        selectQuery = (new StringBuilder("SELECT BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUM(TOTAL_ALLO" +
"CATION) AS TOTAL_ALLOCATION,"+
"SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
"SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +
"SUM(Q1_ALLOCATION) AS Q1_ALLOCATION,SUM(Q2_ALLOCATIO" +
"N) AS Q2_ALLOCATION,SUM(Q3_ALLOCATION) AS Q3_ALLOCATION,SUM(Q4_ALLOCATION) AS Q4" +
"_ALLOCATION,SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   FROM AM_ACQUISITION_" +
"BUDGET "
)).append(filter).append(" ").append("GROUP BY BRANCH_ID,CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME ORDER BY BRA" +
"NCH_NAME "
).toString();
//        System.out.println((new StringBuilder("<<<<<<< selectQuery in findAllBranchAllocationView: ")).append(selectQuery).toString());
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            BranchAllocation allocation;
            for(rs = ps.executeQuery(); rs.next(); list.add(allocation))
            {
                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";
                allocation = new BranchAllocation(id, branchCode, category, sbuCode, branchName, categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                		firstQauterAmount, secondQuaterAmount, thirdQauterAmount, fourthQauterAmount, totalAmount, balanceAmount, type);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocationView ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
            
        }
        return list;
    }
    
    
    public ArrayList findAllBranchAllocationViewforFuel() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,SBU_CODE,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME," +
                             "SUM(TOTAL_ALLOCATION) AS TOTAL_ALLOCATION," +
                             "SUM(JAN) AS JAN,SUM(FEB) AS FEB,SUM(MAR) AS MAR,SUM(APR) AS APR,SUM(MAY) AS MAY,SUM(JUN) AS JUN," +
                             "SUM(JUL) AS JUL,SUM(AUG) AS AUG,SUM(SEP) AS SEP,SUM(OCT) AS OCT,SUM(NOV) AS NOV,SUM(DEC) AS DEC," +        		
                             "SUM(Q1_ALLOCATION) AS Q1_ALLOCATION,SUM(Q2_ALLOCATION) AS Q2_ALLOCATION," +
                             "SUM(Q3_ALLOCATION) AS Q3_ALLOCATION,SUM(Q4_ALLOCATION) AS Q4_ALLOCATION," +
                             "SUM(BALANCE_ALLOCATION) AS BALANCE_ALLOCATION   " +
                             "FROM AM_FUEL_BUDGET  " +
                             "GROUP BY BRANCH_ID,SBU_CODE,BRANCH_NAME ";
//        					 System.out.println("<<<<<<< selectQuery in findAllBranchAllocationViewforFuel: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String category = rs.getString("CATEGORY_CODE");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String type = "";

                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,category,sbuCode,branchName,categoryName,subcategoryName, 
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,
                        thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,
                        type);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Budget allocations findAllBranchAllocationViewforFuel ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    public  java.util.ArrayList findBudgetforApproval(String filter) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
  //      ArrayList list = new ArrayList();
        java.util.ArrayList list = new java.util.ArrayList();

        String selectQuery = "SELECT LT_ID, CATEGORY_CODE,SUB_CATEGORY_CODE,BRANCH_ID,BRANCH_NAME,CATEGORY_NAME,SUB_CATEGORY_NAME,SBU_CODE,DESCRIPTION," +
                "TOTAL_ALLOCATION,"+
                "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC," +
                "Q1_ALLOCATION,Q2_ALLOCATION,Q3_ALLOCATION,Q4_ALLOCATION,BALANCE_ALLOCATION,EXTRA_BUDGET " +
                "FROM AM_ACQUISITION_BUDGET_TMP WHERE STATUS IS NULL " +filter +" ";
        
//System.out.println("<<<<<<< selectQuery in findBudgetforApproval in ExcelBudgetManager: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) { 
                String id = rs.getString("LT_ID");
                String branchCode = rs.getString("BRANCH_ID"); 
                String categoryCode = rs.getString("CATEGORY_CODE"); 
                String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("CATEGORY_NAME");
                String subcategoryName = rs.getString("SUB_CATEGORY_NAME");
                String subcategory = rs.getString("SUB_CATEGORY_CODE");
                String sbuCode = rs.getString("SBU_CODE");
                double janAmount = rs.getDouble("JAN");
                double febAmount = rs.getDouble("FEB");
                double marAmount = rs.getDouble("MAR");
                double aprAmount = rs.getDouble("APR");
                double mayAmount = rs.getDouble("MAY");
                double junAmount = rs.getDouble("JUN");
                double julAmount = rs.getDouble("JUL");
                double augAmount = rs.getDouble("AUG");
                double sepAmount = rs.getDouble("SEP");
                double octAmount = rs.getDouble("OCT");
                double novAmount = rs.getDouble("NOV");
                double decAmount = rs.getDouble("DEC");
                double firstQauterAmount = rs.getDouble("Q1_ALLOCATION");
                double secondQuaterAmount = rs.getDouble("Q2_ALLOCATION");
                double thirdQauterAmount = rs.getDouble("Q3_ALLOCATION");
                double fourthQauterAmount = rs.getDouble("Q4_ALLOCATION");
                double totalAmount = rs.getDouble("TOTAL_ALLOCATION");
                double balanceAmount = rs.getDouble("BALANCE_ALLOCATION");
                String description = rs.getString("DESCRIPTION");
//                String type = rs.getString("TYPE");
                String extrabudget = rs.getString("EXTRA_BUDGET");
                BranchAllocation allocation = new BranchAllocation(id,
                        branchCode,categoryCode,subcategory,sbuCode,branchName,categoryName,subcategoryName,
                        janAmount,febAmount,marAmount,aprAmount,mayAmount,
                        junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
                        firstQauterAmount, secondQuaterAmount,thirdQauterAmount, fourthQauterAmount,
                        totalAmount, balanceAmount,"",extrabudget);
                allocation.setDescription(description);
                list.add(allocation);
            }
 
        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Fuel Budget allocations in findBudgetforApproval in ExcelBudgetManager ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
/*
	public static void main(String[] args) {
		branchCode,category,subcategory,sbuCode,branchName,categoryName,
		subcategoryName,janAmount,febAmount,marAmount,aprAmount,mayAmount,
		junAmount,julAmount,augAmount,sepAmount,octAmount,novAmount,decAmount,
		firstQauterAmount, secondQuaterAmount,
		thirdQauterAmount, fourthQauterAmount,
		totalAmount, balanceAmount,
	}
   */ 
        

    public RFAReversal findRFAById(String id)
    {
    	RFAReversal pro = null;
        String filter = (new StringBuilder()).append(" WHERE INTEGRIFY_ID = '").append(id).append("'").toString();
        ArrayList list = findRfaByQuery(filter);
        if(list.size() > 0)
        {
            pro = (RFAReversal)list.get(0);
        }
        return pro;
    }

    public ArrayList findRfaByQuery(String filter)
    {
        ArrayList _list;
        String query;
        _list = new ArrayList();
        RFAReversal pro = null;
        query = "SELECT ID,INTEGRIFY_ID,CATEGORY_CODE,SUB_CATEGORY_CODE,REQUEST_DATE,REQUESTER_NAME,SBU_CODE,DEPT_NAME," +
        "AMOUNT_PAYABLE,ITEM_REQUEST,QUANTITY,BUDGET_STATUS,AMOUNT FROM INTEGRIFY_BUDGET " ;
        query = (new StringBuilder()).append(query).append(filter).toString();
//        System.out.println("=====query in findRfaByQuery: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) { 
            String id = String.valueOf(rs.getInt("ID"));
            String code = rs.getString("INTEGRIFY_ID");
//            System.out.println("=====code in findRfaByQuery: "+code);
            String desc = rs.getString("ITEM_REQUEST");
            String requestName = rs.getString("REQUESTER_NAME");
            String cCode = rs.getString("CATEGORY_CODE");
            String scCode = rs.getString("SUB_CATEGORY_CODE");
            String startDt = sdf.format(rs.getDate("REQUEST_DATE"));
            String amountPayable = rs.getString("AMOUNT_PAYABLE");
            String quantity = rs.getString("QUANTITY");
            String amount = rs.getString("AMOUNT");
            String status = rs.getString("BUDGET_STATUS");
            String deptName = rs.getString("DEPT_NAME");
            String sbuCode = rs.getString("SBU_CODE");
            pro = new RFAReversal(id, code, desc, cCode, startDt, amountPayable, amount, scCode, status);
            pro.setDeptName(deptName);
            pro.setSbuCode(sbuCode);
            pro.setQuantity(quantity); 
            pro.setRequestName(requestName); 
            _list.add(pro);
        }
        
	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error Selecting  Request for Budget Asset Reversal ->" + e.getMessage());
	} finally {
		closeConnection(con, ps, rs);
	}
        return _list;
    }


}
