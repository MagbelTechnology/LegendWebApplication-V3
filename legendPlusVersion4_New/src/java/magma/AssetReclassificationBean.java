package magma;

import magma.net.dao.MagmaDBConnection;
import com.magbel.util.DatetimeFormat;
import java.sql.*;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import magma.util.Codes;
import com.magbel.util.ApplicationHelper;

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
 * @version 1.0
 */
public class AssetReclassificationBean extends legend.ConnectionClass {

    private String date_of_purchase = "";
    private String depreciation_start_date = "";
    private String depreciation_end_date = "";
    private String posting_date = "";
    private String make = "0";
    private String subject_to_vat = "N";
    private String accumulated_depreciation = "0";
    private String asset_status = "";
    private String monthly_depreciation = "";
    private String maintained_by = "0";
    private String authorized_by = "";
    private String supplied_by = "0";
    private String asset_id = "";
    private String description = "";
    private String vendor_account = "";
    private String cost_price = "0";
    private String vatable_cost = "0";
    private String vat_amount = "0";
    private String serial_number = "";
    private String engine_number = "";
    private String model = "";
    private String user = "";
    private String depreciation_rate = "100";
    private String nbv = "10";
    private String brought_forward = "0";
    private String reason = "0";
    private String scrap_value = "";
    private String new_category = "0";
    private String reclassification_date = new java.text.SimpleDateFormat(
            "dd-MM-yyyy").format(new java.util.Date());
    private String new_code = "";
    private String new_depreciation_rate = "0";
    private String reclassification_reason = "";
    private String branch = "0";
    private String department = "0";
    private String category = "0";
    private String registration_no = "";
    private String user_id = "1";
    private String raise_entry = "N";
    private String recalculate_depreciation = "N";
    private MagmaDBConnection dbConnect;
    private DatetimeFormat dateFormat;
    java.text.SimpleDateFormat sdf;
    private Codes code;
    private String supervisor = "";
    private String oldCatCode = "";
    private String oldBranchCode = "";
    private String oldNBV = "";
    private int assetCode;

    public AssetReclassificationBean() throws Exception {
        super();
        sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

        dbConnect = new MagmaDBConnection();
        dateFormat = new DatetimeFormat();
        code = new Codes();
    }

    /**
     * insertAssetReclassification
     *
     * @return boolean
     * 
     */
    public boolean insertAssetReclassification(boolean budget,String mtid) throws Exception {

        /*
        String updateQuery = " UPDATE AM_ASSET  " +
        "SET CATEGORY_ID = ?, DEP_RATE = ?, TOTAL_LIFE = ?," +
        "REMAINING_LIFE = ? , DEP_YTD = DEP_YTD + ?, DEP_END_DATE = ?,NBV = ?," +
        " ACCUM_DEP = ?,CATEGORY_CODE=?" +
        "  WHERE ASSET_ID = '" + asset_id + "'";
        String updateQuery2 = " UPDATE AM_ASSET  " +
        "SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?" +
        "  WHERE ASSET_ID = '" + asset_id + "'";
        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET CATEGORY_ID = ?,"+
        "CATEGORY_CODE=? WHERE ASSET_ID = '" + asset_id + "'";
         */

        String insertQuery = "INSERT INTO AM_ASSETRECLASSIFICATION(" +
                "ASSET_ID, OLD_CATEGORY_ID, NEW_CATEGORY_ID," +
                "OLD_DEPR_RATE, NEW_DEPR_RATE, OLD_ACCUM_DEP," +
                "NEW_ACCUM_DEP, RECLASSIFY_DATE, RECLASSIFY_REASON," +
                "USER_ID,recalc_difference,recalc_depr,raise_entry,cost_price," +
                "Reclassify_ID,description,old_category_code,old_branch_code," +
                "Monthly_Dep,nbv,old_nbv,new_total_life,new_remaining_life," +
                "new_dep_end_date,old_total_life,old_remaining_life,new_monthly_dep,asset_code)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (category == null || category.equals("")) {
            category = "0";
        }
        if (user_id == null || user_id.equals("")) {
            user_id = "0";
        }

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = true;

        int[] totalAndRemainingLife = getTotalAndRemainLife(asset_id);
        int old_total_life = totalAndRemainingLife[0];
        int old_remaining_life = totalAndRemainingLife[1];
        /* The old formulae used for calculating reclassification information before revised on 20-04-2010
        int totalLife = (int) ((100 /
        Double.parseDouble(getDepreciationRate(
        new_category))) *
        12);
        int remainingLife = totalLife - getUsedLife(asset_id);
        String depEndDate = getDepreciationEndDate(getDepreciationRate(
        new_category) + "," + depreciation_start_date);
        double oldAccumDep = getOldAccumDep(asset_id);
        double newAccumDep = getNewAccumDep(asset_id,
        getDepreciationRate(new_category));
        double amountDifference = newAccumDep - oldAccumDep;
        monthly_depreciation = (monthly_depreciation == null || monthly_depreciation.equals(""))?"0":monthly_depreciation;
        double  nbv1 = (nbv == null || nbv.equals(""))?0.0:Double.parseDouble(nbv);
        double  oldNBV1 = (oldNBV == null || oldNBV.equals(""))?0.0:Double.parseDouble(oldNBV);
         */

        double newMonthlyDep = 0.0;
         double amountDifference = 0.00;
       // double vatableCost = (vatable_cost == null || vatable_cost.equals("")) ? 0.0 : Double.parseDouble(vatable_cost.replaceAll(",", ""));
         
         double costPrice = (cost_price == null || cost_price.equals("")) ? 0.0 : Double.parseDouble(cost_price.replaceAll(",", ""));
         int totalLife = 0;
         int remainingLife = 0;
         double oldNBV1 = 0.0;
         String depEndDate = "";
         double reclassrate = Double.parseDouble(getDepreciationRate(new_category));
 //        System.out.println("<<<<<<<====reclassrate: "+reclassrate);
         if(reclassrate>0){
         totalLife = (int) ((100 / Double.parseDouble(getDepreciationRate(new_category))) * 12);
         oldNBV1 = (oldNBV == null || oldNBV.equals("")) ? 0.0 : Double.parseDouble(oldNBV);
         remainingLife = totalLife - (old_total_life - old_remaining_life);
         }else{totalLife = old_total_life; remainingLife = old_remaining_life;}

        double newAccumDep =0.0;
        if(reclassrate>0){
        depEndDate = getDepreciationEndDate(getDepreciationRate(new_category) + "," + depreciation_start_date);
        }else{depEndDate=depreciation_start_date;}
//        System.out.println("New_category---"+new_category+"-----depreciation_start_date-----"+depreciation_start_date+"----"+depEndDate+"--");
       String reArrangeDate = reArrange(depEndDate);
//         System.out.println("reArrangeDate---"+reArrangeDate);
        double oldAccumDep = getOldAccumDep(asset_id);
        //double newAccumDep = getNewAccumDep(asset_id,getDepreciationRate(new_category));

        monthly_depreciation = (monthly_depreciation == null || monthly_depreciation.equals("")) ? "0" : monthly_depreciation;
        double nbv1 = (nbv == null || nbv.equals("")) ? 0.0 : Double.parseDouble(nbv);
//        System.out.println("<<<<<===recalculate_depreciation: "+recalculate_depreciation);
//        System.out.println("<<<<<===oldNBV1: "+oldNBV1);
//        System.out.println("<<<<<===remainingLife: "+remainingLife);
//        System.out.println("<<<<<===getResidualVal(asset_id): "+getResidualVal(asset_id)+"    oldAccumDep: "+oldAccumDep);
        if(oldAccumDep > 0){
        if (recalculate_depreciation.equalsIgnoreCase("Y")) {
            //System.out.println("here in insert recalculate depreciation >>>>>>>>>> Yes ");
           // newMonthlyDep = (vatableCost - getResidualVal(asset_id)) / (double) totalLife;
             newMonthlyDep = (costPrice - getResidualVal(asset_id)) / (double) totalLife;
            newAccumDep = newMonthlyDep *(old_total_life - old_remaining_life);
           // nbv1= vatableCost - newAccumDep;
            nbv1= costPrice - newAccumDep;

        } else {
            newMonthlyDep = (oldNBV1 - getResidualVal(asset_id))/ (double) remainingLife;
            newAccumDep=oldAccumDep;
            nbv1=oldNBV1;
        }

        amountDifference = newAccumDep - oldAccumDep;
    }else{
    newAccumDep=0.00;
    newMonthlyDep=0.00;

    }


        try {
            con = dbConnect.getConnection("legendPlus");
            /*
            if (recalculate_depreciation.equals("Y")) {
            ps = con.prepareStatement(updateQuery);

            ps.setInt(1, Integer.parseInt(new_category));
            ps.setDouble(2,
            Double.parseDouble(getDepreciationRate(
            new_category)));
            ps.setInt(3, totalLife);
            ps.setInt(4, remainingLife);
            ps.setDouble(5, amountDifference);
            ps.setString(6, depEndDate);
            double nbv = Double.parseDouble(cost_price.replaceAll(",", "")) -
            newAccumDep;
            ps.setDouble(7, nbv);
            ps.setDouble(8, newAccumDep);
            ps.setString(9, code.getCategoryCode(category));
            ps.execute();
            } else {

            ps = con.prepareStatement(updateQuery2);
            ps.setInt(1, Integer.parseInt(new_category));
            ps.setDouble(2,
            Double.parseDouble(getDepreciationRate(
            new_category)));
            ps.setString(3, code.getCategoryCode(new_category));
            ps.execute();
            amountDifference = 0.00d;

            }
            //Update Fleet Master Table Due To Reclassification
            ps = con.prepareStatement(updateFleetMaster);
            ps.setString(1, new_category);
            ps.setString(2, code.getCategoryCode(new_category));
            ps.execute();
            //////////////////////////////////////Fleet Update Ends/////////////////////////

             */


            ps = con.prepareStatement(insertQuery);
//            System.out.println("<<<<<===asset_id: "+asset_id);
            ps.setString(1, asset_id);
//            System.out.println("<<<<<===category: "+Integer.parseInt(category));
            ps.setInt(2, Integer.parseInt(category));
//            System.out.println("<<<<<===new_category: "+Integer.parseInt(new_category));
            ps.setInt(3, Integer.parseInt(new_category));
//            System.out.println("<<<<<===category: "+Double.parseDouble(getDepreciationRate(category)));
            ps.setDouble(4, Double.parseDouble(getDepreciationRate(category)));
//            System.out.println("<<<<<===new_category: "+Double.parseDouble(getDepreciationRate(new_category)));
            ps.setDouble(5, Double.parseDouble(getDepreciationRate(new_category)));
//            System.out.println("<<<<<===oldAccumDep: "+oldAccumDep);
            ps.setDouble(6, oldAccumDep);
//            System.out.println("<<<<<===newAccumDep: "+newAccumDep);
            ps.setDouble(7, newAccumDep);
//            System.out.println("<<<<<===Date: "+dbConnect.dateConvert(new java.util.Date()));
            ps.setDate(8, dbConnect.dateConvert(new java.util.Date()));
//            System.out.println("<<<<<===reclassification_reason: "+reclassification_reason);
            ps.setString(9, reclassification_reason);
//            System.out.println("<<<<<===user_id: "+Integer.parseInt(user_id));
            ps.setInt(10, Integer.parseInt(user_id));
//            System.out.println("<<<<<===Math.abs(amountDifference): "+amountDifference);
            ps.setDouble(11, Math.abs(amountDifference));
//            System.out.println("<<<<<===recalculate_depreciation: "+recalculate_depreciation);
            ps.setString(12, recalculate_depreciation);
//            System.out.println("<<<<<===raise_entry: "+raise_entry);
            ps.setString(13, raise_entry);
//            System.out.println("<<<<<===cost_price: "+Double.parseDouble(cost_price.replaceAll(",", "")));
            ps.setDouble(14, Double.parseDouble(cost_price.replaceAll(",", "")));
//            System.out.println("<<<<<===AM_ASSETRECLASSIFICATION: "+new ApplicationHelper().getGeneratedId("AM_ASSETRECLASSIFICATION"));
//            ps.setString(15, new ApplicationHelper().getGeneratedId("AM_ASSETRECLASSIFICATION"));
            ps.setString(15, mtid);
//            System.out.println("<<<<<===description: "+description);
            ps.setString(16, description);
//            System.out.println("<<<<<===oldCatCode: "+oldCatCode);
            ps.setString(17, oldCatCode);
//            System.out.println("<<<<<===oldBranchCode: "+oldBranchCode);
            ps.setString(18, oldBranchCode);
//            System.out.println("<<<<<===monthly_depreciation: "+Double.parseDouble(monthly_depreciation));
            ps.setDouble(19, Double.parseDouble(monthly_depreciation.replaceAll(",", "")));
//            System.out.println("<<<<<===nbv1: "+nbv1);
            ps.setDouble(20, nbv1);
 //           System.out.println("<<<<<===oldNBV1: "+oldNBV1);
            ps.setDouble(21, oldNBV1);
 //           System.out.println("<<<<<===totalLife: "+totalLife);
            ps.setInt(22, totalLife);
 //           System.out.println("<<<<<===remainingLife: "+remainingLife);
            ps.setInt(23, remainingLife);
//            System.out.println("<<<<<===reArrangeDate: "+dbConnect.dateConvert(java.sql.Date.valueOf(reArrangeDate)));
            ps.setDate(24, dbConnect.dateConvert(java.sql.Date.valueOf(reArrangeDate)));
//            System.out.println("<<<<<===old_total_life: "+old_total_life);
            ps.setInt(25, old_total_life);
//            System.out.println("<<<<<===old_remaining_life: "+old_remaining_life);
            ps.setInt(26, old_remaining_life);
 //           System.out.println("<<<<<===newMonthlyDep: "+newMonthlyDep);
            if(Double.parseDouble(getDepreciationRate(new_category)) == Double.parseDouble(getDepreciationRate(category))) {newMonthlyDep = 0.0;}
            ps.setDouble(27, newMonthlyDep);
  //          System.out.println("<<<<<===assetCode: "+assetCode);
            ps.setLong(28,assetCode);
            
            ps.execute();
//            if (budget) { //only if it is new reclassification not an update
//                //updateBudget(); //update aqcuisition budget
//            }
//            if (raise_entry.equals("Y")) {
//                logDrepreciationEntries(asset_id);
//            }

        } catch (Exception ex) {

            done = false;
            System.out.println("WARN:AssetReclassificationBean:insertAssetReclassification() Failed reclassifying asset ->" + ex);
        } finally {
            dbConnect.closeConnection(con, ps);
        }

        return done;
    }
	public String reArrange(String date)
	{
		String year="";
		String month="";
		String day="";
		String[] form ={};
		form=date.split("-");
		  year=form[2];
		  if(form[0].length()==1)
		  {month="0"+form[0];}
		  else{
		  month=form[0];}
		  if(form[1].length()==1)
		  {day="0"+form[1];}
		  else{
		  day=form[1];}

		  date = year+"-"+month+"-"+day;
		return date;
	}
    //ganiyu
    /*
    public void updateAssetReclassification(String id) {

        int new_category = 0;
        double new_depr_rate = 0;
        double new_accum_dep = 0;
        int total_life = 0;
        int remaining_life = 0;
        //String recalc_dep ="";
        //String raise_ent ="";
        double recalc_difference = 0;

        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");



        String updateQuery = " UPDATE AM_ASSET  " +
                "SET CATEGORY_ID = ?, DEP_RATE = ?, TOTAL_LIFE = ?," +
                "REMAINING_LIFE = ? , DEP_YTD = ?, DEP_END_DATE = ?,NBV = ?," +
                " ACCUM_DEP = ?,CATEGORY_CODE=?" +
                "  WHERE ASSET_ID = '" + id + "'";

        String updateQuery2 = " UPDATE AM_ASSET  " +
                "SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?,ASSET_STATUS=?,POST_REJECT_REASON=?" +
                "  WHERE ASSET_ID = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET CATEGORY_ID = ?," +
                "CATEGORY_CODE=? WHERE ASSET_ID = '" + id + "'";


        String query1 = "select NEW_CATEGORY_ID,NEW_DEPR_RATE,NEW_ACCUM_DEP,recalc_difference,recalc_depr,"+
                "raise_entry, "+
                "from AM_ASSETRECLASSIFICATION where asset_id = '" + id + "'";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dbConnect.getConnection("legendPlus");


            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next()) {
                new_category = rs.getInt(1);
                new_depr_rate = rs.getDouble(2);
                new_accum_dep = rs.getDouble(3);
                recalc_difference = rs.getDouble(4);
                recalculate_depreciation = rs.getString(5);
                raise_entry = rs.getString(6);


            }//while



            ///////////Begin updates here/////////////////////////////////////////



            total_life = (int) ((100 / Double.parseDouble(getDepreciationRate(Integer.toString(new_category)))) * 12);
            remaining_life = total_life - getUsedLife(id);
            String depEndDate = getDepreciationEndDate(getDepreciationRate(Integer.toString(new_category)) + "," + depreciation_start_date);
            //double oldAccumDep = getOldAccumDep(id);
            // double newAccumDep = getNewAccumDep(id,getDepreciationRate(Integer.toString(new_category)));
            //double amountDifference = newAccumDep - oldAccumDep;





            System.out.println("the values of >>>>>>>>>> total_life " + total_life);
            System.out.println("the values of >>>>>>>>>> remaining_life " + remaining_life);
            System.out.println("the values of >>>>>>>>>> depEndDate " + depEndDate);
            System.out.println("the values of >>>>>>>>>> recalculate_depreciation " + recalculate_depreciation);
            //////update am_asset table with approve values

            if (recalculate_depreciation.equalsIgnoreCase("Y")) {
                ps = con.prepareStatement(updateQuery);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setInt(3, total_life);
                ps.setInt(4, remaining_life);
                ps.setDouble(5, recalc_difference);
                ps.setString(6, depEndDate);
                double nbv = Double.parseDouble(cost_price.replaceAll(",", "")) - new_accum_dep;
                ps.setDouble(7, nbv);
                ps.setDouble(8, new_accum_dep);
                ps.setString(9, code.getCategoryCode(Integer.toString(new_category)));

                i = ps.executeUpdate();

            } else {

                ps = con.prepareStatement(updateQuery2);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setString(3, code.getCategoryCode(Integer.toString(new_category)));
                ps.setString(4, "ACTIVE");
                ps.setString(5, "");
                ps.execute();

            }

            //where fleet master is updated
            ps = con.prepareStatement(updateFleetMaster);
            ps.setInt(1, new_category);
            ps.setString(2, code.getCategoryCode(Integer.toString(new_category)));
            ps.execute();

            updateBudget();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: updateAssetReclassification(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            dbConnect.closeConnection(con, ps, rs);
        }







    }

    */
    //This method will retreived already calculated reclassification details from table. 21-April-2010
    public void updateAssetReclassification(String id) {

        int new_category = 0;
        double new_depr_rate = 0;
        double new_accum_dep = 0;
        int total_life = 0;
        int remaining_life = 0;
        //String recalc_dep ="";
        //String raise_ent ="";
        double recalc_difference = 0;
        //int newTotallife =0;
        //int new_remaining_life=0;
        double new_monthly_dep =0.0;
        double netBookValue =0.0;
        java.sql.Date new_dep_end_date = null;

        int i = 0;
        //System.out.println("i am in update asset revalue UUUUUUUUUUUUUUUUUUUU");



        String updateQuery = " UPDATE AM_ASSET  " +
                "SET CATEGORY_ID = ?, DEP_RATE = ?, TOTAL_LIFE = ?," +
                "REMAINING_LIFE = ? , DEP_YTD = ?, DEP_END_DATE = ?,NBV = ?," +
                " ACCUM_DEP = ?,CATEGORY_CODE=?,Monthly_Dep=?" +
                "  WHERE ASSET_ID = '" + id + "'";

        String updateQuery2 = " UPDATE AM_ASSET  " +
                "SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?,ASSET_STATUS=?,POST_REJECT_REASON=?," +
                " Monthly_Dep=?,REMAINING_LIFE=?,TOTAL_LIFE=?"+
                "  WHERE ASSET_ID = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET CATEGORY_ID = ?," +
                "CATEGORY_CODE=? WHERE ASSET_ID = '" + id + "'";


        String query1 = "select NEW_CATEGORY_ID,NEW_DEPR_RATE,NEW_ACCUM_DEP,recalc_difference,recalc_depr,"+
                "raise_entry,new_total_life,new_remaining_life,new_monthly_dep,NBV,new_dep_end_date "+
                "from AM_ASSETRECLASSIFICATION where asset_id = '" + id + "'";


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dbConnect.getConnection("legendPlus");


            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next()) {
                new_category = rs.getInt(1);
                new_depr_rate = rs.getDouble(2);
                new_accum_dep = rs.getDouble(3);
                recalc_difference = rs.getDouble(4);
                recalculate_depreciation = rs.getString(5);
                raise_entry = rs.getString(6);
                total_life= rs.getInt(7);
                remaining_life = rs.getInt(8);
                new_monthly_dep=  rs.getDouble(9);
                netBookValue= rs.getDouble(10);
                new_dep_end_date = rs.getDate(11);

            }//while



            ///////////Begin updates here/////////////////////////////////////////



            //--total_life = (int) ((100 / Double.parseDouble(getDepreciationRate(Integer.toString(new_category)))) * 12);
           //-- remaining_life = total_life - getUsedLife(id);
          //String depEndDate = getDepreciationEndDate(getDepreciationRate(Integer.toString(new_category)) + "," + depreciation_start_date);
            //double oldAccumDep = getOldAccumDep(id);
            // double newAccumDep = getNewAccumDep(id,getDepreciationRate(Integer.toString(new_category)));
            //double amountDifference = newAccumDep - oldAccumDep;





            //System.out.println("the values of >>>>>>>>>> total_life " + total_life);
           // System.out.println("the values of >>>>>>>>>> remaining_life " + remaining_life);
            //System.out.println("the values of >>>>>>>>>> depEndDate " + depEndDate);
           // System.out.println("the values of >>>>>>>>>> recalculate_depreciation " + recalculate_depreciation);
            //////update am_asset table with approve values

            if (recalculate_depreciation.equalsIgnoreCase("Y")) {
               // System.out.println(" here in Y part of update asset reclassification >>>>>>>>>>>");
                ps = con.prepareStatement(updateQuery);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setInt(3, total_life);
                ps.setInt(4, remaining_life);
                ps.setDouble(5, recalc_difference);
                ps.setDate(6, new_dep_end_date);
               //-- double nbv = Double.parseDouble(cost_price.replaceAll(",", "")) - new_accum_dep;
                ps.setDouble(7, netBookValue);
                ps.setDouble(8, new_accum_dep);
                ps.setString(9, code.getCategoryCode(Integer.toString(new_category)));
                ps.setDouble(10,new_monthly_dep);

                i = ps.executeUpdate();

            } else {

                ps = con.prepareStatement(updateQuery2);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setString(3, code.getCategoryCode(Integer.toString(new_category)));
                ps.setString(4, "ACTIVE");
                ps.setString(5, "");
                ps.setDouble(6,new_monthly_dep);
                ps.setInt(7, remaining_life);
                 ps.setInt(8, total_life);
                ps.execute();

            }

            //where fleet master is updated
            ps = con.prepareStatement(updateFleetMaster);
            ps.setInt(1, new_category);
            ps.setString(2, code.getCategoryCode(Integer.toString(new_category)));
            ps.execute();

            updateBudget();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: updateAssetReclassification(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            dbConnect.closeConnection(con, ps, rs);
        }







    }




    /**
     * updateAssetReclassification
     *
     * @return boolean
     */
    public boolean updateAssetReclassification() {
        return false;
    }

    /**
     * getAssetReclassifications
     */
    public void getAssetReclassifications() throws Exception {

        Connection con = null;
         PreparedStatement ps = null;
        ResultSet rs = null;


        String query = "SELECT A.ASSET_ID,A.REGISTRATION_NO,B.BRANCH_NAME," +
                "D.DEPT_NAME,C.CATEGORY_NAME,A.DESCRIPTION," +
                "A.VENDOR_AC,A.DATE_PURCHASED,A.DEP_RATE," +
                "A.ASSET_MAKE,A.ASSET_MODEL,A.ASSET_SERIAL_NO," +
                "A.ASSET_ENGINE_NO,V.VENDOR_NAME,A.ASSET_USER," +
                "M.MAINT_NAME,A.ACCUM_DEP,A.MONTHLY_DEP," +
                "A.COST_PRICE,A.NBV,A.DEP_END_DATE,A.RESIDUAL_VALUE," +
                "A.AUTHORIZED_BY,A.POSTING_DATE,A.EFFECTIVE_DATE," +
                "A.PURCHASE_REASON,A.USEFUL_LIFE,A.TOTAL_LIFE," +
                "L.LOCATION,A.REMAINING_LIFE,A.VATABLE_COST," +
                "A.VAT,A.REQ_DEPRECIATION,A.SUBJECT_TO_VAT," +
                "A.WHO_TO_REM,A.EMAIL1,A.EMAIL2,A.RAISE_ENTRY," +
                "A.DEP_YTD,S.SECTION_NAME,A.ASSET_STATUS," +
                "C.ASSET_LEDGER,	 A.DATE_DISPOSED,A.[USER_ID]," +
                "A.WHO_TO_REM_2  " +
                "FROM AM_ASSET A " +
                "LEFT OUTER JOIN AM_AD_BRANCH B " +
                "	ON A.BRANCH_ID = B.BRANCH_ID " +
                "LEFT OUTER JOIN AM_AD_CATEGORY C " +
                "	ON A.CATEGORY_ID = C.CATEGORY_ID " +
                "LEFT OUTER JOIN AM_AD_DEPARTMENT D " +
                "	ON A.DEPT_ID = D.DEPT_ID " +
                "LEFT OUTER JOIN AM_AD_VENDOR V " +
                "	ON A.SUPPLIER_NAME = V.VENDOR_ID " +
                "LEFT OUTER JOIN AM_VW_MAINTNANCE_VENDORS M " +
                "	ON A.ASSET_MAINTENANCE = M.MAINT_ID " +
                "LEFT OUTER JOIN AM_AD_SECTION S " +
                "	ON A.SECTION_ID = S.SECTION_ID " +
                "LEFT OUTER JOIN AM_GB_LOCATION L " +
                "	ON A.LOCATION = L.LOCATION_ID " +
                "WHERE ASSET_ID = '" + asset_id + "'  ";


        try {


            con = dbConnect.getConnection("legendPlus");
            



            if (asset_id != "") {

                //ps = con.prepareStatement(query);
                //rs = ps.executeQuery();
                rs = getStatement().executeQuery(query);



                if (rs.next()) {
                    date_of_purchase = dbConnect.formatDate(rs.getDate(
                            "DATE_PURCHASED"));
                    depreciation_start_date = dbConnect.formatDate(rs.getDate(
                            "EFFECTIVE_DATE"));
                    depreciation_end_date = dbConnect.formatDate(rs.getDate(
                            "DEP_END_DATE"));
                    depreciation_end_date = dbConnect.formatDate(rs.getDate(
                            "EFFECTIVE_DATE"));
                    posting_date = dbConnect.formatDate(rs.getDate("POSTING_DATE"));
                    make = rs.getString("ASSET_MAKE");
                    subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                    accumulated_depreciation = rs.getString("ACCUM_DEP");
                    monthly_depreciation = rs.getString("MONTHLY_DEP");
                    maintained_by = rs.getString("MAINT_NAME");
                    authorized_by = rs.getString("AUTHORIZED_BY");
                    supplied_by = rs.getString("VENDOR_NAME");
                    asset_id = rs.getString(1);
                    description = rs.getString("DESCRIPTION");
                    vendor_account = rs.getString("VENDOR_AC");
                    cost_price = rs.getString("COST_PRICE");
                    vatable_cost = rs.getString("VATABLE_COST");
                    //vat_amount = rs.getString(32);
                    vat_amount = rs.getString("VAT");
                    serial_number = rs.getString("ASSET_SERIAL_NO");
                    engine_number = rs.getString("ASSET_ENGINE_NO");
                    model = rs.getString("ASSET_MODEL");
                    user = rs.getString("ASSET_USER");
                    depreciation_rate = rs.getString("DEP_RATE");
                    nbv = rs.getString("NBV");
                    brought_forward = "0.00"; //rs.getString(39);
                    scrap_value = rs.getString("RESIDUAL_VALUE");
                    branch = rs.getString("BRANCH_NAME");
                    department = rs.getString("DEPT_NAME");
                    category = rs.getString("CATEGORY_NAME");
                    registration_no = rs.getString("REGISTRATION_NO");
                }
            }

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getAssetReclassifications(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
         //   dbConnect.closeConnection(con,rs);
            dbConnect.closeConnection(con, ps, rs);
        }


    }

    public void setDate_of_purchase(String date_of_purchase) {
        if (date_of_purchase != null) {
            this.date_of_purchase = date_of_purchase;
        }
    }

    public void setDepreciation_start_date(String depreciation_start_date) {
        if (depreciation_start_date != null) {
            this.depreciation_start_date = depreciation_start_date;
        }
    }

    public void setDepreciation_end_date(String depreciation_end_date) {
        if (depreciation_end_date != null) {
            this.depreciation_end_date = depreciation_end_date;
        }
    }

    public void setPosting_date(String posting_date) {
        if (posting_date != null) {
            this.posting_date = posting_date;
        }
    }

    public void setMake(String make) {
        if (make != null) {
            this.make = make;
        }
    }

    public void setSubject_to_vat(String subject_to_vat) {
        if (subject_to_vat != null) {
            this.subject_to_vat = subject_to_vat;
        }
    }

    public void setAccumulated_depreciation(String accumulated_depreciation) {
        if (accumulated_depreciation != null) {
            this.accumulated_depreciation = accumulated_depreciation;
        }
    }

    public void setAsset_status(String asset_status) {
        if (asset_status != null) {
            this.asset_status = asset_status;
        }
    }

    public void setMonthly_depreciation(String monthly_depreciation) {
        if (monthly_depreciation != null) {
            this.monthly_depreciation = monthly_depreciation;
        }
    }

    public void setMaintained_by(String maintained_by) {
        if (maintained_by != null) {
            this.maintained_by = maintained_by;
        }
    }

    public void setAuthorized_by(String authorized_by) {
        if (authorized_by != null) {
            this.authorized_by = authorized_by;
        }
    }

    public void setSupplied_by(String supplied_by) {
        if (supplied_by != null) {
            this.supplied_by = supplied_by;
        }
    }

    public void setAsset_id(String asset_id) {
        if (asset_id != null) {
            this.asset_id = asset_id;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setVendor_account(String vendor_account) {
        if (vendor_account != null) {
            this.vendor_account = vendor_account;
        }
    }

    public void setCost_price(String cost_price) {
        if (cost_price != null) {
            this.cost_price = cost_price;
        }
    }

    public void setVatable_cost(String vatable_cost) {
        if (vatable_cost != null) {
            this.vatable_cost = vatable_cost;
        }
    }

    public void setVat_amount(String vat_amount) {
        if (vat_amount != null) {
            this.vat_amount = vat_amount;
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

    public void setDepreciation_rate(String depreciation_rate) {
        if (depreciation_rate != null) {
            this.depreciation_rate = depreciation_rate;
        }
    }

    public void setNbv(String nbv) {
        if (nbv != null) {
            this.nbv = nbv;
        }
    }

    public void setBrought_forward(String brought_forward) {
        if (brought_forward != null) {
            this.brought_forward = brought_forward;
        }
    }

    public void setReason(String reason) {
        if (reason != null) {
            this.reason = reason;
        }
    }

    public void setScrap_value(String scrap_value) {
        if (scrap_value != null) {
            this.scrap_value = scrap_value;
        }
    }

    public void setNew_category(String new_category) {
        if (new_category != null) {
            this.new_category = new_category;
        }
    }

    public void setReclassification_date(String reclassification_date) {
        if (reclassification_date != null) {
            this.reclassification_date = reclassification_date;
        }
    }

    public void setNew_code(String new_code) {
        if (new_code != null) {
            this.new_code = new_code;
        }
    }

    public void setNew_depreciation_rate(String new_depreciation_rate) {
        if (new_depreciation_rate != null) {
            this.new_depreciation_rate = new_depreciation_rate;
        }
    }

    public void setReclassification_reason(String reclassification_reason) {
        if (reclassification_reason != null) {
            this.reclassification_reason = reclassification_reason;
        }
    }

    public void setBranch(String branch) {
        if (branch != null) {
            this.branch = branch;
        }
    }

    public void setDepartment(String department) {
        if (department != null) {
            this.department = department;
        }
    }

    public void setCategory(String category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setRegistration_no(String registration_no) {
        if (registration_no != null) {
            this.registration_no = registration_no;
        }
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public void setRaise_entry(String raise_entry) {
        if (raise_entry != null) {
            this.raise_entry = raise_entry;
        }
    }

    public void setRecalculate_depreciation(String recalculate_depreciation) {
        if (recalculate_depreciation != null) {
            //System.out.println("Here oooooooooooooooooooooooo J " + recalculate_depreciation);
            this.recalculate_depreciation = recalculate_depreciation;
        }
    }

    public String getDate_of_purchase() {
        return date_of_purchase;
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

    public String getSubject_to_vat() {
        return subject_to_vat;
    }

    public String getAccumulated_depreciation() {
        return accumulated_depreciation;
    }

    public String getAsset_status() {
        return asset_status;
    }

    public String getMonthly_depreciation() {
        return monthly_depreciation;
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

    public String getAsset_id() {
        return asset_id;
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

    public String getVatable_cost() {
        return vatable_cost;
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

    public String getDepreciation_rate() {
        return depreciation_rate;
    }

    public String getNbv() {
        return nbv;
    }

    public String getBrought_forward() {
        return brought_forward;
    }

    public String getReason() {
        return reason;
    }

    public String getScrap_value() {
        return scrap_value;
    }

    public String getNew_category() {
        return new_category;
    }

    public String getReclassification_date() {
        return reclassification_date;
    }

    public String getNew_code() {
        return new_code;
    }

    public String getNew_depreciation_rate() {
        return new_depreciation_rate;
    }

    public String getReclassification_reason() {
        return reclassification_reason;
    }

    public String getBranch() {
        return branch;
    }

    public String getDepartment() {
        return department;
    }

    public String getCategory() {
        return category;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRaise_entry() {
        return raise_entry;
    }

    public String getRecalculate_depreciation() {
        return recalculate_depreciation;
    }

    /**
     * removeOldClassification
     *
     * @param assetid String
     * @return void
     */
    public void removeOldClassification(String assetid) throws Exception {

        String query = "DELETE FROM am_assetReclassification " +
                "WHERE ASSET_ID = '" + assetid + "'";
        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = dbConnect.getConnection("legendPlus");
            ps = con.prepareStatement(query);

            ps.execute();

        } catch (Exception er) {
        } finally {
            dbConnect.closeConnection(con, ps);
        }

    }

    /**
     * getCategoryId
     *
     * @param categoryName String
     * @return String
     */
    public String getCategoryId(String categoryName) {

        String query =
                "SELECT CATEGORY_ID FROM AM_AD_CATEGORY WHERE UPPER(CATEGORY_NAME) = UPPER('" +
                categoryName + "')";
        String catID = "0";

        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

        try {

            con = dbConnect.getConnection("legendPlus");

            ps = con.prepareStatement(query);
            //rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {
                catID = rs.getString(1);
            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getCategoryId(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps, rs);
        }
        return catID;
    }

    /**
     * getResidualValue
     *
     * @param categoryid String
     * @return double
     */
    public double getResidualValue(String categoryid) {

        String query = "SELECT RESIDUAL_VALUE FROM AM_AD_CATEGORY " +
                "WHERE CATEGORY_ID = " + categoryid;
        double catID = 0.00d;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            rs = getStatement().executeQuery(query);

            con = dbConnect.getConnection("legendPlus");

            ps = con.prepareStatement(query);
            // rs = ps.executeQuery();

            while (rs.next()) {
                catID = rs.getDouble(1);
            }
            // freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getResidualValue(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
             dbConnect.closeConnection(con, ps, rs);
        }
        return catID;
    }

    public int getUsedLife(String assetid) {

        String query = "SELECT USEFUL_LIFE FROM AM_ASSET WHERE ASSET_ID = '" +
                assetid + "'";
        int usedLife = 0;
        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

        try {

            ResultSet rs = getStatement().executeQuery(query);
            con = dbConnect.getConnection("legendPlus");
            //ps = con.prepareStatement(query);
            // rs = ps.executeQuery();
            while (rs.next()) {
                usedLife = rs.getInt(1);
            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getUsedLife(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }
        return usedLife;
    }

    /**
     * getDepreciationRate
     *
     * @param categoryId String
     * @return String
     */
    public String getDepreciationRate(String categoryId) throws Exception {

        String query =
                "SELECT DEP_RATE FROM AM_AD_CATEGORY WHERE CATEGORY_ID = " +
                categoryId;
        String depRate = "0.00";
        Connection con = null;
        //PreparedStatement ps = null;
        // ResultSet rs = null;
        try {

            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);


            while (rs.next()) {

                depRate = Double.toString(rs.getDouble(1));

            }
            freeResource();
        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getDepreciationRate(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }
        return depRate;

    }

    public String getAssetLedger(String categoryId) {

        String query = "SELECT ASSET_GL FROM AM_AD_CATEGORY " +
                "WHERE CATEGORY_ID = " + categoryId;
        String assetGL = "";
        Connection con = null;
        // PreparedStatement ps = null;
        // ResultSet rs = null;

        try {

            ResultSet rs = getStatement().executeQuery(query);

            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //  rs = ps.executeQuery();

            while (rs.next()) {

                assetGL = rs.getString(1);

            }

            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getAssetLedger(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }

        return assetGL;

    }

    public String getAccumDepreciationLedger(String categoryId) {

        String query = "SELECT ACCUM_DEP_LEDGER FROM AM_AD_CATEGORY " +
                "WHERE CATEGORY_ID = " + categoryId;
        String accumGL = "";
        Connection con = null;
        // PreparedStatement ps = null;
        //ResultSet rs = null;


        try {

            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {

                accumGL = rs.getString(1);

            }



        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getAccumDepreciationLedger(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            freeResource();
            // dbConnect.closeConnection(con, ps,rs);
        }

        return accumGL;

    }

    public double getOldAccumDep(String assetid) {

        String query = "select ACCUM_DEP FROM AM_ASSET WHERE ASSET_ID = '" +
                assetid + "'";
        double accum = 0.00d;
        Connection con = null;
        // PreparedStatement ps = null;
        //ResultSet rs = null;

        try {
            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();
            ResultSet rs = getStatement().executeQuery(query);

            while (rs.next()) {
                accum = rs.getDouble(1);
            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getOldAccumDep(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }
        return accum;

    }

    public double getNewAccumDep(String assetid, String rate) {

        String query = "select DEP_RATE, ACCUM_DEP" +
                " FROM AM_ASSET WHERE ASSET_ID ='" + assetid + "'";
        double newr = Double.parseDouble(rate);
        double newacc = 0.00d;

        Connection con = null;
        //PreparedStatement ps = null;
        //ResultSet rs = null;

        try {
            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //rs = ps.executeQuery();
            ResultSet rs = getStatement().executeQuery(query);

            while (rs.next()) {
                double oldr = rs.getDouble("DEP_RATE");
                //double newr = Double.parseDouble(newrate);
                double accum = rs.getDouble("ACCUM_DEP");

                if (newr == oldr) {
                    newacc = 0.0;
                    //option = "E";
                } else if (newr > oldr) {
                    newacc = (((newr - oldr) / oldr) + 1) * accum;
                    //amount = newacc - accum;
                    //option = "G";
                } else if (newr < oldr) {
                    newacc = (1 - ((oldr - newr) / oldr)) * accum;
                    //amount = accum - newacc;
                    //option = "L";
                }

            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getNewAccumDep(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }

        return newacc;
    }

    public boolean isReclassDateGreaterThanProcesing(String assetId) {

        boolean isGreater = false;
        String reclassDate = "";
        String processDate = "";
        String query = "SELECT R.RECLASSIFY_DATE,C.NEXT_PROCESSING_DATE   " +
                "FROM AM_ASSETRECLASSIFICATION R,AM_GB_COMPANY C   " +
                "WHERE R.ASSET_ID = '" + assetId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();

            rs = getStatement().executeQuery(query);

            while (rs.next()) {

                reclassDate = dbConnect.formatDate(rs.getDate(1));
                processDate = dbConnect.formatDate(rs.getDate(2));
            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: isReclassDateGreaterThanProcesing(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
             dbConnect.closeConnection(con, ps,rs);
        }
        isGreater = dateFormat.isDateExceedOther(reclassDate, processDate);
        return isGreater;

    }

    public boolean isEntryRaised(String assetId, String id) {
        boolean isRaised = false;
        // String query = "SELECT count(transaction_id) FROM AM_ENTRY_TABLE " +
        //   "WHERE DR_NARRATION LIKE '%" + assetId + "%'";
        String query = "SELECT raise_entry FROM am_assetReclassification " +
                "WHERE asset_id='" + assetId + "'";

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        //ResultSet rsr = null;
        //PreparedStatement psr = null;
        try {

            con = dbConnect.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            //psr = con.prepareStatement(query);
            // rsr = ps.executeQuery();
            while (rs.next()) {

                String raise = rs.getString("raise_entry");
                isRaised = (raise.equalsIgnoreCase("R")) ? true : false;
            }

            /* while (rs.next()) {
            int count = rs.getInt(1);
            isRaised = (count > 0) ? true : false;
            }*/

        } catch (Exception e) {
            System.out.println("WARN :Error isEntryRaised -> " + e);
        } finally {
            dbConnect.closeConnection(con, ps, rs);
        }

        return isRaised;
    }

    public void logDrepreciationEntries(String asset_id) {
    }

    public static String getDepreciationEndDate(String vals) {

        String endDate = "ERROR";

        if (vals != null) {

            StringTokenizer st1 = new StringTokenizer(vals, ",");
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
                        // c.add(Calendar.DAY_OF_YEAR, -1*Integer.parseInt(day));

                        int endDay = c.get(Calendar.DAY_OF_MONTH);
                        int endMonth = c.get(Calendar.MONTH) + 1;
                        int endYear = c.get(Calendar.YEAR);

                        endDate = endMonth + "-" + endDay + "-" + endYear;
                    }

                }
            }
        }

        return endDate;
    }

    public String getNewCatID(String assetid) {

        String query =
                "SELECT NEW_CATEGORY_ID  FROM am_assetReclassification  " +
                "WHERE asset_id = '" + assetid + "' ";
        String accumGL = "0";
        Connection con = null;
        //PreparedStatement ps = null;
        // ResultSet rs = null;

        try {

            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {

                accumGL = rs.getString(1);

            }

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getNewCatID(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            freeResource();
            //dbConnect.closeConnection(con, ps,rs);
        }

        return accumGL;

    }

    public String getCatCode(String cateid) {

        String query =
                "SELECT CATEGORY_CODE  FROM am_ad_category  " +
                "WHERE category_id = '" + cateid + "' ";
        String catid = "0";
        Connection con = null;
        //PreparedStatement ps = null;
        //ResultSet rs = null;

        try {
            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            //   rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {

                catid = rs.getString(1);

            }

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getCatCode(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            freeResource();
            // dbConnect.closeConnection(con, ps,rs);
        }

        return catid;

    }

    public void updateBudget() {
        String companyQuery = "SELECT financial_start_date" + ",financial_no_ofmonths,financial_end_date" + " FROM am_gb_company";
        String fisdate = "";
        int finomonth = 0;
        String fiedate = "";
        Statement stmt = null;
        Statement stmt1 = null;
        Connection conn = dbConnect.getConnection("legendPlus");
        ;
        //Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt1 = getStatement();
            ResultSet rs = stmt1.executeQuery(companyQuery);
            while (rs.next()) {

                fisdate = sdf.format(rs.getDate("financial_start_date"));
                finomonth = rs.getInt("financial_no_ofmonths");
                fiedate = sdf.format(rs.getDate("financial_end_date"));

            }
            System.out.println("pdate  " + date_of_purchase);
            System.out.println("financial start " +fisdate);
            double q1 = (double) (finomonth / 4); //value of first quarter months
            System.out.println("value of 1st Q" + q1 + "fs " + fisdate);
            int month = (dateFormat.getDayDifference(date_of_purchase, fisdate)) / 30;
             System.out.println("pdate  " + date_of_purchase);
            System.out.println("financial start and pdate diff months " + month);
            boolean btw = dateFormat.isDateBetween(fisdate, fiedate,
                    date_of_purchase);
            if (btw) {
                System.out.println(
                        "Commencing update of Aquicisition Budget due to Reclassification");
                // conn = this.getConnection();
                //conn.setAutoCommit(false);
                System.out.println(category);
                System.out.println(this.new_category);
                String old_category = getCatCode(category);
                String new_category = getCatCode(this.new_category);

                if ((double) month <= q1) {
                    String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q1_ACTUAL = (Q1_ACTUAL - " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + old_category +
                            "'";
                    String budgetUpdate2 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q1_ACTUAL = (Q1_ACTUAL + " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + new_category +
                            "'";
                    System.out.println(budgetUpdate1);
                    System.out.println(budgetUpdate2);
                    stmt.addBatch(budgetUpdate1);
                    stmt.addBatch(budgetUpdate2);
                    stmt.executeBatch();
                    System.out.println("Updated 1st Quarter");
                } else if ((double) month > q1 && (double) month <= (q1 * 2.0)) {
                    String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q2_ACTUAL = (Q2_ACTUAL - " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + old_category +
                            "'";
                    String budgetUpdate2 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q2_ACTUAL = (Q2_ACTUAL + " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + new_category +
                            "'";
                    System.out.println(budgetUpdate1);
                    System.out.println(budgetUpdate2);
                    stmt.addBatch(budgetUpdate1);
                    stmt.addBatch(budgetUpdate2);
                    stmt.executeBatch();
                    System.out.println("Updated 2nd Quarter");
                } else if ((double) month > (q1 * 2.0) &&
                        (double) month <= (q1 * 3.0)) {
                    String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q3_ACTUAL =(Q3_ACTUAL - " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + old_category +
                            "'";
                    String budgetUpdate2 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q3_ACTUAL = (Q3_ACTUAL + " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + new_category +
                            "'";

                    System.out.println(budgetUpdate1);
                    System.out.println(budgetUpdate2);
                    stmt.addBatch(budgetUpdate1);
                    stmt.addBatch(budgetUpdate2);
                    stmt.executeBatch();
                    System.out.println("Updated 3rd Quarter");
                } else if (month > (q1 * 3.0)) {
                    String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q4_ACTUAL = (Q4_ACTUAL - " +
                            vatable_cost.replaceAll(",", "") +
                            ") WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + old_category +
                            "'";
                    String budgetUpdate2 = "UPDATE AM_ACQUISITION_BUDGET " +
                            " SET Q4_ACTUAL = (Q4_ACTUAL + " +
                            vatable_cost.replaceAll(",", "") +
                            ")  WHERE BRANCH_ID='" + getBranchCode() +
                            "' AND CATEGORY='" + new_category +
                            "'";
                    System.out.println(budgetUpdate1);
                    System.out.println(budgetUpdate2);
                    stmt.addBatch(budgetUpdate1);
                    stmt.addBatch(budgetUpdate2);
                    stmt.executeBatch();
                    System.out.println("Updated 4th Quarter");
                }
                //stmt.executeBatch();
                //conn.commit();
            }
        } catch (Exception ex) {
            System.out.println("ERROR_ " + this.getClass().getName() + "---" +
                    ex.getMessage() + "--" + "Commencing Rollback");
            ex.printStackTrace();  /*try {
            conn.rollback();
            } catch (SQLException ex1) {
            System.out.println("ERROR_ " + this.getClass().getName() +
            "---" + ex1.getMessage() + "--" +
            "Rollback Failed");
            }*/
        } finally {
            freeResource();
            dbConnect.closeConnection(conn, stmt);
        }
        System.out.println(
                "Exiting update of Aquicisition Budget due to Reclassification");
    }

    public String getBranchCode() {

        String query =
                "SELECT BRANCH_ID  FROM AM_ASSET  " +
                "WHERE asset_id = '" + asset_id + "' ";
        String catid = "0";

        Connection con = null;
        //PreparedStatement ps = null;
        //ResultSet rs = null;

        try {

            con = dbConnect.getConnection("legendPlus");

            //ps = con.prepareStatement(query);
            // rs = ps.executeQuery();

            ResultSet rs = getStatement().executeQuery(query);
            while (rs.next()) {

                catid = rs.getString(1);

            }

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getBranchCode(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            freeResource();
            //dbConnect.closeConnection(con, ps,rs);
        }

        return catid;

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

    /**
     * @return the oldCatCode
     */
    public String getOldCatCode() {
        return oldCatCode;
    }

    /**
     * @param oldCatCode the oldCatCode to set
     */
    public void setOldCatCode(String oldCatCode) {
        this.oldCatCode = oldCatCode;
    }

    /**
     * @return the oldBranchCode
     */
    public String getOldBranchCode() {
        return oldBranchCode;
    }

    /**
     * @param oldBranchCode the oldBranchCode to set
     */
    public void setOldBranchCode(String oldBranchCode) {
        this.oldBranchCode = oldBranchCode;
    }

    /**
     * @return the oldNBV
     */
    public String getOldNBV() {
        return oldNBV;
    }

    /**
     * @param oldNBV the oldNBV to set
     */
    public void setOldNBV(String oldNBV) {
        this.oldNBV = oldNBV;
    }

    public int[] getTotalAndRemainLife(String assetid) {

        String query = "SELECT Total_Life,Remaining_Life FROM AM_ASSET WHERE ASSET_ID = '" +
                assetid + "'";
        System.out.println("<=======getTotalAndRemainLife query: "+query);
        int[] result = new int[2];
        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

        try {   

            ResultSet rs = getStatement().executeQuery(query);
            con = dbConnect.getConnection("legendPlus");

            while (rs.next()) {
                result[0] = rs.getInt(1);
                result[1] = rs.getInt(2);
            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getTotalAndRemainLife():  ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }
        return result;
    }

    public double getResidualVal(String assetid) {

        String query = "SELECT Residual_Value FROM AM_ASSET WHERE ASSET_ID = '" +
                assetid + "'";
        double result = 0.0;
        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;

        try {

            ResultSet rs = getStatement().executeQuery(query);
            con = dbConnect.getConnection("legendPlus");

            while (rs.next()) {
                result = rs.getDouble(1);

            }
            freeResource();

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: getResidualVal():  ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            //dbConnect.closeConnection(con, ps,rs);
        }
        return result;
    }

    public void updateAssetReclassification(String id,Long tranId) {

        int new_category = 0;
        double new_depr_rate = 0;
        double new_accum_dep = 0;
        int total_life = 0;
        int remaining_life = 0;
        double recalc_difference = 0;
        double new_monthly_dep =0.0;
        double netBookValue =0.0;
        java.sql.Date new_dep_end_date = null;

        int i = 0;

        String updateQuery = " UPDATE AM_ASSET  " +
                "SET CATEGORY_ID = ?, DEP_RATE = ?, TOTAL_LIFE = ?," +
                "REMAINING_LIFE = ? , DEP_YTD = ?, DEP_END_DATE = ?,NBV = ?," +
                " ACCUM_DEP = ?,CATEGORY_CODE=?,Monthly_Dep=?" +
                "  WHERE ASSET_ID = '" + id + "'";

        String updateQuery2 = " UPDATE AM_ASSET  " +
                "SET   CATEGORY_ID = ?, DEP_RATE = ?,CATEGORY_CODE=?,ASSET_STATUS=?,POST_REJECT_REASON=?," +
                " Monthly_Dep=?,REMAINING_LIFE=?,TOTAL_LIFE=?"+
                "  WHERE ASSET_ID = '" + id + "'";

        String updateFleetMaster = "UPDATE FT_FLEET_MASTER SET CATEGORY_ID = ?," +
                "CATEGORY_CODE=? WHERE ASSET_ID = '" + id + "'";


        String query1 = "select NEW_CATEGORY_ID,NEW_DEPR_RATE,NEW_ACCUM_DEP,recalc_difference,recalc_depr,"+
                "raise_entry,new_total_life,new_remaining_life,new_monthly_dep,NBV,new_dep_end_date "+
                "from AM_ASSETRECLASSIFICATION where asset_id = '" + id + "' AND Reclassify_ID =" +tranId;


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dbConnect.getConnection("legendPlus");


            ps = con.prepareStatement(query1);
            rs = ps.executeQuery();

            while (rs.next()) {
                new_category = rs.getInt(1);
                new_depr_rate = rs.getDouble(2);
                new_accum_dep = rs.getDouble(3);
                recalc_difference = rs.getDouble(4);
                recalculate_depreciation = rs.getString(5);
                raise_entry = rs.getString(6);
                total_life= rs.getInt(7);
                remaining_life = rs.getInt(8);
                new_monthly_dep=  rs.getDouble(9);
                netBookValue= rs.getDouble(10);
                new_dep_end_date = rs.getDate(11);
                System.out.println("the values of >>>>>>>>>> new_category " + new_category);
            }//while



            ///////////Begin updates here/////////////////////////////////////////



            //--total_life = (int) ((100 / Double.parseDouble(getDepreciationRate(Integer.toString(new_category)))) * 12);
           //-- remaining_life = total_life - getUsedLife(id);
          //String depEndDate = getDepreciationEndDate(getDepreciationRate(Integer.toString(new_category)) + "," + depreciation_start_date);
            //double oldAccumDep = getOldAccumDep(id);
            // double newAccumDep = getNewAccumDep(id,getDepreciationRate(Integer.toString(new_category)));
            //double amountDifference = newAccumDep - oldAccumDep;





            //System.out.println("the values of >>>>>>>>>> total_life " + total_life);
           // System.out.println("the values of >>>>>>>>>> remaining_life " + remaining_life);
            //System.out.println("the values of >>>>>>>>>> depEndDate " + depEndDate);
           // System.out.println("the values of >>>>>>>>>> recalculate_depreciation " + recalculate_depreciation);
            //////update am_asset table with approve values

            if (recalculate_depreciation.equalsIgnoreCase("Y")) {
               // System.out.println(" here in Y part of update asset reclassification >>>>>>>>>>>");
                ps = con.prepareStatement(updateQuery);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setInt(3, total_life);
                ps.setInt(4, remaining_life);
                ps.setDouble(5, recalc_difference);
                ps.setDate(6, new_dep_end_date);
               //-- double nbv = Double.parseDouble(cost_price.replaceAll(",", "")) - new_accum_dep;
                ps.setDouble(7, netBookValue);
                ps.setDouble(8, new_accum_dep);
                ps.setString(9, code.getCategoryCode(Integer.toString(new_category)));
                ps.setDouble(10,new_monthly_dep);
 //stop update on am_asset
               // i = ps.executeUpdate(); 

            } else {

                ps = con.prepareStatement(updateQuery2);
                ps.setInt(1, new_category);
                ps.setDouble(2, new_depr_rate);
                ps.setString(3, code.getCategoryCode(Integer.toString(new_category)));
                ps.setString(4, "ACTIVE");
                ps.setString(5, "");
                ps.setDouble(6,new_monthly_dep);
                ps.setInt(7, remaining_life);
                 ps.setInt(8, total_life);
                   //stop update on am_asset
               // ps.execute();

            }

            //where fleet master is updated
            ps = con.prepareStatement(updateFleetMaster);
            ps.setInt(1, new_category);
            ps.setString(2, code.getCategoryCode(Integer.toString(new_category)));
            ps.execute();

       //     updateBudget(); //This is required when budget is enforced

        } catch (Exception e) {
            String warning = "WARNING:AssetReclassificationBean class: updateAssetReclassification(): Error reclassifying asset ->" +
                    e.getMessage();
            System.out.println(warning);
            e.printStackTrace();
        } finally {
            dbConnect.closeConnection(con, ps, rs);
        }
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
    
}
