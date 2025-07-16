package magma.net.manager;

//import magma.net.dao.MagmaDBConnection;
import java.sql.*;
import java.util.StringTokenizer;
import magma.net.dao.MagmaDBConnection;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;

import java.text.SimpleDateFormat;

public class EndofYear extends legend.ConnectionClass {
    private DatetimeFormat dateFormat;
    private HtmlUtility htmlCombo;
    java.text.SimpleDateFormat sdf;
    public EndofYear() throws Exception {
        System.out.println("INFO-- Performing End of Year Initialization");
        sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        dateFormat = new DatetimeFormat();
        //dbConnect = new MagmaDBConnection();
        htmlCombo = new HtmlUtility();

    }


    public boolean resetAssetDepytd() {
//        System.out.println("INFO-- Resetting DepYTD");
        String updatesql = "UPDATE AM_ASSET SET DEP_YTD = 0";
        boolean done = false;
       Statement stmt = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
            done = (stmt.executeUpdate(updatesql) != -1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            
                try {


            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
        
           //  freeResource();
        }
        return done;
    }

    public boolean resetFleetMasterValues() {
//        System.out.println("INFO-- Resetting otherYTD");
        String updatesql = "UPDATE FT_FLEET_MASTER SET DEP_YTD = 0," +
                           " PREMIUM_PTD = 0, MAINT_PTD = 0, FUEL_PTD = 0," +
                           " ACCIDENT_COST_PTD = 0, LICENCE_PERMIT_PTD = 0," +
                           "MAINT_Q1 = 0, MAINT_Q2 =0, MAINT_Q3 = 0, MAINT_Q4=0";
        boolean done = false;
        Statement stmt = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
            done = (stmt.executeUpdate(updatesql) != -1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
          //   freeResource();


    try {


            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }

        }

        return done;

    }

    public boolean doEndofYear() {
        int f_start_year = getYear_start();
        int f_start_month = getMonth_start();

         int f_end_year = getYear_end();
        int f_end_month = getMonth_end();

        int[] monthyear = getMonthYear();
        boolean done = false;
        boolean done1 = false;
        try {
            String[] findata = getFinancialInfo();
            java.util.Date stdate = sdf.parse(findata[0]);
            java.util.Date sysdate = new java.util.Date();
            String ldate =findata[2];
            String dd = ldate.substring(0,2);
            String mm = ldate.substring(3,5);
            String yyyy = ldate.substring(6,10);
//            int intyyy = Integer.parseInt(yyyy) - 1;
            int intyyy = Integer.parseInt(yyyy);
            ldate = yyyy+"-"+mm+"-"+dd;
            String currentyyy = String.valueOf(intyyy);
//            System.out.println("System Date ===> " + sysdate);
//            System.out.println("Finacial Date ===> " + stdate);
//            System.out.println("Last Date ===> " + ldate+"     currentyyy: "+currentyyy);
            String newfindata = htmlCombo.findObject("SELECT DATEADD(year,-1,'"+ldate+"')");
//            System.out.println("New Finacial Date ===> " + newfindata);
//            System.out.println("System Month ===> " + monthyear[0]);
//            System.out.println("System Year ===> " + monthyear[1]);
//            System.out.println("financial Start Month  ===> " + f_start_month);
//            System.out.println("financial Start Year ===> " + f_start_year);
//
//             System.out.println("financial End Month  ===> " + f_end_month);
//            System.out.println("financial End Year ===> " + f_end_year);

            if (sysdate.after(stdate) || sysdate.equals(stdate)) {

//               System.out.println(">>> here 1");
                if (checkMonthlyDep(currentyyy,f_end_month)) {
//                     System.out.println(">>> here 2");
                    if (checkYearEndDepPerformed()) {
//                     System.out.println(">>> here 3");
                        //Connection con = getConnection();
                    	done = Yeardonesap();
                    	done = depdoneInsert();
                        done = resetAssetDepytd();
                        done1 = resetFleetMasterValues();
                        archiveBudget();
                        updateYearEnd();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
 //          freeResource();
        }

        return (done == true && done1 == true);
    }

    public int getYear_start() {
    	 Statement  stmt =  null;
    	 ResultSet   rs = null;
        String sql =
                "select datepart(year, financial_start_date) AS SYEAR from am_gb_company";
        int syear = 0;
       // Statement stmt = null;
      //  ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
             rs = stmt.executeQuery(sql);
            while (rs.next()) {
                syear = rs.getInt("SYEAR");
            }
           // freeResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
             freeResource();
             
            try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
            
        }

        return syear;
    }

    /**
     *
     * @return int
     */
    public int getMonth_start() {
        String sql =
                "select datepart(month, financial_start_date) AS SMONTH from am_gb_company";
        int smonth = 0;
       // Statement stmt= null;
       // ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
          Statement   stmt = getStatement();
          ResultSet   rs = stmt.executeQuery(sql);
            while (rs.next()) {
                smonth = rs.getInt("SMONTH");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
        /*
            try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
            */
            // freeResource();
        }

        return smonth;
    }

    public boolean checkMonthlyDep(java.util.Date finst) {
//        System.out.println(
//                "INFO-- checking monthly_depreciation_processing table");
        SimpleDateFormat sdff = new SimpleDateFormat("MM");
        SimpleDateFormat sdff2 = new SimpleDateFormat("yyyy");
        String sql =
            "SELECT * FROM month_depprocesing_summary WHERE datepart(month,DEP_DATE) >= " +
                sdff.format(dateFormat.dateConvert(finst)) +" AND "
                +" datepart(year,DEP_DATE) >= " +sdff2.format(dateFormat.dateConvert(finst));

//         System.out.println(">>> the query in checkMonthlyDep >> "+ sql);
        boolean exists = false;
         Statement stmt= null;
         ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
            stmt = getStatement();
               rs = stmt.executeQuery(sql);
            exists = rs.next();
            //freeResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{

             try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }


        //     freeResource();
        }
        System.out.println(" the value of exists is >>> "+exists );
        return exists;

    }
    
    public boolean checkMonthlyDep(String finst,int month) {
//        System.out.println(
//                "INFO-- checking monthly_depreciation_processing table");
        SimpleDateFormat sdff = new SimpleDateFormat("MM");
        SimpleDateFormat sdff2 = new SimpleDateFormat("yyyy");
        String sql =
            "SELECT * FROM month_depprocesing_summary WHERE datepart(month,DEP_DATE) >= " +
                month +" AND "
                +" datepart(year,DEP_DATE) >= '"+finst+"'";

//         System.out.println(">>> the query in checkMonthlyDep >> "+ sql);
        boolean exists = false;
         Statement stmt= null;
         ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
            stmt = getStatement();
               rs = stmt.executeQuery(sql);
            exists = rs.next();
            //freeResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{

             try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }


        //     freeResource();
        }
        System.out.println(" the value of exists is >>> "+exists );
        return exists;

    }
    
    public boolean checkYearEndDepPerformed() {
//        System.out.println(" here iin  checkYearEndDepPerformed");
        String sql =
                "select perform_year_end  from am_gb_company WHERE perform_year_end='N' ";
        boolean done = false;
       Statement stmt = null;
       ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
                 stmt = getStatement();
             rs = stmt.executeQuery(sql);
            done = rs.next();
           // System.out.println("perform_year_end =======>" +
                               //rs.getString("perform_year_end"));
            //freeResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{

            
             try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
       
         //   freeResource();
        }

        return done;

    }

    public int[] getMonthYear() {
        int[] monthyear = new int[2];

        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        String datex = sd.format(d);
//        System.out.println("system date =======>" + datex);
        StringTokenizer token = new StringTokenizer(datex, "/");
        while (token.hasMoreTokens()) {
            token.nextToken();
            monthyear[0] = Integer.parseInt(token.nextToken());
            monthyear[1] = Integer.parseInt(token.nextToken());
        }
        return monthyear;
    }

    public void archiveBudget() {
//        System.out.println("INFO-- Archiving Old Budget");
        String[] dates = getFinancialInfo();
        String sql = "SELECT LT_ID,BRANCH_ID,BRANCH_NAME,CATEGORY,"
                     + "TOTAL_ALLOCATION,Q1_ALLOCATION,Q1_ACTUAL"
                     + ",Q2_ALLOCATION,Q2_ACTUAL,"
                     + "Q3_ALLOCATION,Q3_ACTUAL,Q4_ALLOCATION"
                     + " ,Q4_ACTUAL,BALANCE_ALLOCATION,TYPE"
                     + ",ACC_START_DATE,ACC_END_DATE"
                     + " FROM AM_ACQUISITION_BUDGET"
                     + " WHERE ACC_START_DATE<>'" +
                     dateFormat.dateConvert(dates[0]) + "' AND "
                     + " ACC_END_DATE<>'" + dateFormat.dateConvert(dates[2]) +
                     "'";
       /*
        String isql = "INSERT INTO AM_ACQUISITION_BUDGET_ARCHIVE"
                      + "(BRANCH_ID,BRANCH_NAME"
                      + ",CATEGORY,TOTAL_ALLOCATION"
                      + ",Q1_ALLOCATION,Q1_ACTUAL"
                      + ",Q2_ALLOCATION,Q2_ACTUAL"
                      + ",Q3_ALLOCATION,Q3_ACTUAL"
                      + ",Q4_ALLOCATION,Q4_ACTUAL"
                      + ",BALANCE_ALLOCATION,TYPE"
                      + ",ACC_START_DATE,ACC_END_DATE)"
                      + "  VALUES"
                      + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        */
//System.out.println("=====sql in archiveBudget: "+sql);        
Statement stmt = null;
//--PreparedStatement ps = null;
ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
           //--  ps = getConnection().prepareStatement(isql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String field0 = rs.getString("LT_ID");
                String field1 = rs.getString("BRANCH_ID");
                String field2 = rs.getString("BRANCH_NAME");
                String field3 = rs.getString("CATEGORY");
                String field4 = rs.getString("TOTAL_ALLOCATION");
                String field5 = rs.getString("Q1_ALLOCATION");
                String field6 = rs.getString("Q1_ACTUAL");
                String field7 = rs.getString("Q2_ALLOCATION");
                String field8 = rs.getString("Q2_ACTUAL");
                String field9 = rs.getString("Q3_ALLOCATION");
                String field10 = rs.getString("Q3_ACTUAL");
                String field11 = rs.getString("Q4_ALLOCATION");
                String field12 = rs.getString("Q4_ACTUAL");
                String field13 = rs.getString("BALANCE_ALLOCATION");
                String field14 = rs.getString("TYPE");
                String field15 = rs.getString("ACC_START_DATE");
                String field16 = rs.getString("ACC_END_DATE");

                // ps.setString(1, field0);
               insertToBudgetArchive(field1,field2,field3,field4,field5,field6,field7,field8,field9,field10,field11,field12,field13,field14,field15,field16);
              /*
               ps.setString(1, field1);
                ps.setString(2, field2);
                ps.setString(3, field3);
                ps.setString(4, field4);
                ps.setString(5, field5);
                ps.setString(6, field6);
                ps.setString(7, field7);
                ps.setString(8, field8);
                ps.setString(9, field9);
                ps.setString(10, field10);
                ps.setString(11, field11);
                ps.setString(12, field12);
                ps.setString(13, field13);
                ps.setString(14, field14);
                ps.setString(15, field15);
                ps.setString(16, field16);

                ps.execute();

                */
            }
            deleteOldBudget();
            //freeResource();
        } catch (Exception ex) {
            System.out.println(" the error occurred here>> #" + ex);
            ex.printStackTrace();
        }
        finally{
                
            try {

              
            if (rs != null) {
                rs.close();
            }
            
            if (stmt != null) {
                stmt.close();
            }



        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }

           
           // freeResource();
        }


    }

    public String[] getFinancialInfo() {
        String[] result = new String[3];
        ResultSet   rs = null;
        String query = "SELECT (SELECT DATEADD(DAY,-1,DATEADD(YEAR,DATEDIFF(YEAR,0,financial_start_date),0))) AS financial_start_date,"
                       + "financial_no_ofmonths,"
                       + "(SELECT DATEADD(DAY,-1,DATEADD(YEAR,DATEDIFF(YEAR,0,financial_end_date),0))) AS financial_end_date"
                       + " FROM am_gb_company";
        try {
          Connection  con = getConnection();
         PreparedStatement   ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result[0] = sdf.format(rs.getDate("financial_start_date"));
                result[1] = rs.getString("financial_no_ofmonths");
                result[2] = sdf.format(rs.getDate("financial_end_date"));
            }
        } catch (Exception e) {
            String warning = "WARNING:Error Fetching Company Details" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            //freeResource(con,ps,rs);
            freeResource();
            
            try {

              if (rs != null) {
                  rs.close();
              }

          } catch (Exception e) {
              System.out.println("WARN:Error WARNING:Error Fetching Company Details closing Connection ->" +
                                 e.getMessage());
          }
        }

        return result;
    }

    /**
     * deleteOldBudget
     */
    public void deleteOldBudget() {
//        System.out.println("INFO-- Deleting Old Budget");
        String[] dates = getFinancialInfo();
        String sql = "DELETE FROM AM_ACQUISITION_BUDGET"
                     + " WHERE ACC_START_DATE<>'" +
                     dateFormat.dateConvert(dates[0]) + "' AND "
                     + " ACC_END_DATE<>'" + dateFormat.dateConvert(dates[2]) +
                     "'";
        Connection con = null;
        PreparedStatement ps = null;
//        System.out.println("=====sql in deleteOldBudget: "+sql); 
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (Exception e) {
            String warning = "WARNING:Error Fetching Company Details" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
//            freeResource();
            
          try {

            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
        }

    }
    public void updateYearEnd() {
           Connection con = null;
           ResultSet rs = null;
           PreparedStatement ps = null;
           boolean done = false;
           String query = "UPDATE AM_GB_COMPANY SET  perform_year_end = 'Y'";
          Statement stmt = null;
           try {
              //-- con = getConnection();
              //-- ps = con.prepareStatement(query);
              //-- rs = ps.executeQuery();
                stmt = getStatement();
            done = (stmt.executeUpdate(query) != -1);
           } catch (Exception ex) {
               System.out.println("WARN: Error updateing perform_year_end->" +
                                  ex.getMessage());
           } finally {

               
               try {

            
            if (stmt != null) {
                stmt.close();
            }
           
        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
          
            //   freeResource();
           }

    }


    public int getYear_end() {
        String sql =
                "select datepart(year, financial_end_date) AS SYEAR from am_gb_company";
        int syear = 0;

         Statement stmt= null;
     //    ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
          ResultSet   rs = stmt.executeQuery(sql);
            while (rs.next()) {
                syear = rs.getInt("SYEAR");
            }
           // freeResource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{

            
            try {

         
         if (stmt != null) {
             stmt.close();
         }
        
     } catch (Exception e) {
         System.out.println("WARN:Error closing Connection ->" +
                            e.getMessage());
     }
            /*
          try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }
          */
            //  freeResource();
        }

        return syear;
    }

    /**
     *
     * @return int
     */
    public int getMonth_end() {
        String sql =
                "select datepart(month, financial_end_date) AS SMONTH from am_gb_company";
        int smonth = 0;
        Statement stmt = null;
          ResultSet rs = null;
        try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
             rs = stmt.executeQuery(sql);
            while (rs.next()) {
                smonth = rs.getInt("SMONTH");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            
          try {

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error closing Connection ->" +
                               e.getMessage());
        }

 //          freeResource();
           
        }

        return smonth;
    }

     public boolean checkMonthlyDep(java.util.Date finst, int endMonth) {
    	 Statement stmt = null;
    	 ResultSet rs = null;
        System.out.println(
                "INFO-- checking monthly_depreciation_processing table");
        SimpleDateFormat sdff = new SimpleDateFormat("MM");
        SimpleDateFormat sdff2 = new SimpleDateFormat("yyyy");
        String sql =
            "SELECT * FROM month_depprocesing_summary WHERE datepart(month,DEP_DATE) >= " +
                endMonth +" AND "
                +" datepart(year,DEP_DATE) >= " +sdff2.format(dateFormat.dateConvert(finst));

 //        System.out.println(">>> the query with Two Parameters in checkMonthlyDep >> "+ sql);
        boolean exists = false;
         try {
            //Connection con = getConnection("legendPlus");
             stmt = getStatement();
             rs = stmt.executeQuery(sql);
            exists = rs.next();
           //  System.out.println(" value of exist is >> "+ exists);
             // System.out.println(" value ofrs.next() is >> "+ rs.next() );
            //freeResource();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
 //            freeResource();
             
             try {

               if (rs != null) {
                   rs.close();
               }
               if (stmt != null) {
                   stmt.close();
               }

           } catch (Exception e) {
               System.out.println("WARN:Error closing Connection ->" +
                                  e.getMessage());
           }
        }
          System.out.println(" value of exist is >> "+ exists);
        return exists;

    }


     public void insertToBudgetArchive(String branchID, String BranchName,String category, String totalAllocation, String q1Allocation, String q1Actual, String q2Allocation, String q2Actual, String q3Allocation, String q3Actual, String q4Allocation, String q4Actual,
             String balanceAllocation, String type, String accStartDate, String accEndDate){

//      System.out.println("INFO 2 -- Archiving Old Budget 2");
        String isql = "INSERT INTO AM_ACQUISITION_BUDGET_ARCHIVE"
                      + "(BRANCH_ID,BRANCH_NAME"
                      + ",CATEGORY,TOTAL_ALLOCATION"
                      + ",Q1_ALLOCATION,Q1_ACTUAL"
                      + ",Q2_ALLOCATION,Q2_ACTUAL"
                      + ",Q3_ALLOCATION,Q3_ACTUAL"
                      + ",Q4_ALLOCATION,Q4_ACTUAL"
                      + ",BALANCE_ALLOCATION,TYPE"
                      + ",ACC_START_DATE,ACC_END_DATE)"
                      + "  VALUES"
                      + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps= null;
         try {

            con = getConnection();
             ps= con.prepareStatement(isql);

             ps.setString(1, branchID);
                ps.setString(2, BranchName);
                ps.setString(3, category);
                ps.setString(4, totalAllocation);
                ps.setString(5, q1Allocation);
                ps.setString(6, q1Actual);
                ps.setString(7, q2Allocation);
                ps.setString(8, q2Actual);
                ps.setString(9, q3Allocation);
                ps.setString(10, q3Actual);
                ps.setString(11, q4Allocation);
                ps.setString(12, q4Actual);
                ps.setString(13, balanceAllocation);
                ps.setString(14, type);
                ps.setString(15, accStartDate);
                ps.setString(16, accEndDate);

                ps.execute();

         } catch (Exception e) {





         } finally{

            try {

                 if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            System.out.println("WARN:Error in insertToBudgetArchive:: closing Connection ->" +
                               e.getMessage());
        }
                }

     }

     public boolean Yeardonesap()
     {
         String sql;
         boolean exists;
         Connection con;
         Statement stmt;
         ResultSet rs;
         System.out.println("About to Delete records from file AM_ASSET_YEARLY");
         String Sapquery="delete from AM_ASSET_MONTHLY_YEAR_END where asset_id is not null ";
         System.out.println(Sapquery);
         exists = false;
         con = null;
         stmt = null;
         rs = null;
         try {;
         con = getConnection();
         stmt = con.createStatement();
         rs = stmt.executeQuery(Sapquery);
         //exists = true;
         exists = rs.next();
  //       System.out.println("=====Yearly Exists for Deletion=====> "+exists);
         if (rs != null) {
             rs.close();
         }
         if (stmt != null) {
             stmt.close();
         }
         } 
         catch (Exception ex) {
             ex.printStackTrace();
         }
         finally{
//              freeResource();
         }
         return exists;
     }

     public boolean depdoneInsert()
     {
         boolean exists;
//         System.out.println("About to Insert records into file AM_ASSET_MONTHLY_YEAR_END ");
         String qw = "INSERT INTO [AM_ASSET_MONTHLY_YEAR_END] SELECT * FROM [am_asset]  where asset_id is not null ";
         updateAssetStatusChange(qw);
         exists = true;
         return exists;
     }    
     

public void updateAssetStatusChange(String query_r){
Connection con = null;
        PreparedStatement ps = null;
try {
	con = getConnection();
ps = con.prepareStatement(query_r);
           int i =ps.executeUpdate();
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
//             freeResource();
            
            try {

              if (ps != null) {
                  ps.close();
              }
              if (con != null) {
                  con.close();
              }

          } catch (Exception e) {
              System.out.println("WARN:Error closing Connection ->" +
                                 e.getMessage());
          }
        }
}
        

}
