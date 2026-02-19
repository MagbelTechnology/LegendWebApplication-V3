package magma.net.manager;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.CurrentDateTime;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;
import magma.net.vao.AssetTransaction;
import magma.net.vao.ComponentDetail;
import magma.net.vao.DistributionDetail;
import magma.net.vao.ProcesingInfo;

// Referenced classes of package magma.net.manager:
//            FleetHistoryManager, DepreciationChecks

public class DepreciationLegendPlusProcessingManager extends MagmaDBConnection
{

    private FleetHistoryManager historyManager;
    private Date currentProcessingDate;
    private Date nextProcessingDate;
    private Date lastDate;
    private CurrencyNumberformat formata;
    private SimpleDateFormat sdf;
    private DatetimeFormat df;
    private CurrentDateTime con;
    private boolean sucessful;
    private ProcesingInfo process;
    private HtmlUtility htmlCombo;
    private ApprovalRecords records;
    ArrayList Alist;

    public DepreciationLegendPlusProcessingManager()
    {
        Alist = new ArrayList();
 //       System.out.println("INFO:Enter Depreciation Transaction Processing ..");
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        formata = new CurrencyNumberformat();
        con = new CurrentDateTime();
        process = getProcessingInfo();
        htmlCombo = new HtmlUtility();
        currentProcessingDate = process.getProcessingDate();
        lastDate = process.getNextProcessingDate();
        nextProcessingDate = process.getNextProcessingDate();
        records = new ApprovalRecords();
    }

    public void setSucessful(boolean sucessful)
    {
        this.sucessful = sucessful;
    }

    public boolean isSucessful()
    {
        return sucessful;
    }

    public Date getDateValue(String s_date)
    {
        return java.sql.Date.valueOf(s_date);
    }

    public void processDepreciation(String userid) {

        String startDate = process.getFinancialStartDate();
        String endDate = process.getFinancialEndDate();
        historyManager = new FleetHistoryManager();
        ApprovalRecords appRecord = new ApprovalRecords();
        // ArrayList asets = historyManager.findAssetForDepreciation(process.getNextProcessingDate());
        String nextdate = df.formatDate(process.getNextProcessingDate());
//        System.out.println("<<<<<<<<<<<<<nextdate: "+nextdate);
        
        String filter = " AND EFFECTIVE_DATE <= '" + process.getNextProcessingDate() + "' ";
//        ArrayList asets = historyManager.findAssetForDepreciation(filter);
        DepreciationChecks depchk = new DepreciationChecks();
        int frequency = getProcessingInfo().getFrequency();

        //System.out.println(" The value of frequency is LLLLLLLLLLLL " + frequency);
        String startMonth = startDate.substring(3, 5);
        String lastMonth = endDate.substring(3, 5);
        String processDate = getCompSystemDate();  
        String systemdate = sdf.format(new java.util.Date());
 //       System.out.println("=====>systemdate: "+systemdate+"    processDate: "+processDate);
        String prodate = df.formatDate(process.getProcessingDate());
        String tableExists = htmlCombo.findObject("IF (EXISTS (SELECT * FROM sys.tables WHERE [name] = 'am_gb_classEnable')) BEGIN SELECT 1 END ELSE BEGIN SELECT 0 END");
//        System.out.println("<<<<<<proYear>>>>>: "+tableExists);
        String depreciationProcessMonth = htmlCombo.findObject("SELECT MONTH(NEXT_PROCESSING_DATE) FROM AM_GB_COMPANY");
        String depreciationProcessDate = htmlCombo.findObject("SELECT NEXT_PROCESSING_DATE FROM AM_GB_COMPANY");
        String depreciationLastProcessDate = htmlCombo.findObject("SELECT PROCESSING_DATE FROM AM_GB_COMPANY");
        depreciationLastProcessDate = depreciationLastProcessDate.substring(0,10);
        depreciationProcessDate = depreciationProcessDate.substring(0,10);
        String nextProcessingDate = depreciationProcessDate.substring(0,10);
        String proDay = processDate.substring(0, 2);
        String proMonth = processDate.substring(3, 5);
        String proYear = processDate.substring(6, 10);
//        System.out.println("<<<<<<proYear>>>>>: "+proYear);
        if(depreciationProcessMonth.equals(12)){
        	int pYear = Integer.parseInt(proYear) + 1;
       	 	proYear = String.valueOf(pYear);
//       	 	System.out.println("<<<<<<proYear for December>>>>>: "+proYear);
        }
        processDate = proYear+"-"+proMonth+"-"+proDay;
//        System.out.println("<<<<<<<<<<<<<processDate: "+processDate);
        String processStartDate = proYear+"-"+proMonth+"-"+"01";
        int proDay0 = Integer.parseInt(proDay)+1;
        String processEndDate = proYear+"-"+proMonth+"-"+String.valueOf(proDay0);
        String lastProcessingDate = proYear+"-"+proMonth+"-"+proDay;
//        System.out.println("======>processEndDate: "+processEndDate);
//        System.out.println("<<<<<<<<<<<<<processStartDate: "+processStartDate+"  <<<<<<<processEndDate: "+processEndDate);
        String processingDay = depreciationProcessDate.substring(0, 2);
        String processingMonth = depreciationProcessDate.substring(3, 5);
        String processingYear = depreciationProcessDate.substring(6, 10);
        String nextprocessDate = processingYear+"-"+processingMonth+"-"+processingDay;
//        System.out.println("<<<<<<<<<<<<<nextprocessDate: "+nextprocessDate);
        
        String threshold = htmlCombo.findObject("SELECT old_threshhold FROM AM_GB_COMPANY");
        double costthreshold = Double.valueOf(threshold);
        
        String endProcessDate = htmlCombo.findObject("SELECT DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,'"+depreciationProcessDate+"')+1,0))");
        endProcessDate = endProcessDate.substring(0, 10);
        String FirstDayProcessDate = htmlCombo.findObject("SELECT DATEFROMPARTS(year('"+depreciationProcessDate+"'),month('"+depreciationProcessDate+"'),1)");
//       System.out.println("<<<<<<<<<<<<<endProcessDate: "+endProcessDate+"    FirstDayProcessDate: "+FirstDayProcessDate);
        String lastYear = endDate.substring(6, 10);
        depchk.insertTempDepreciationEntry();
        depchk.insertTempDistributedAssets(); 
        String lastTransDepreciationDate = htmlCombo.findObject("SELECT DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,'"+depreciationLastProcessDate+"'),0))");
        /*
        0. It Disable Users before starting Depreciation
        1. About to Remove Pending Transactions Before Depreciation for the new month
        2. If Total Life is wrong
        3. If first Month set DPY to Date = 0.00;
        4. if last month, update acc period with months specified.
        5. start from where processing Date less or equals start date.
        6. check if SBU for GL prefix to use.
        7. If depreciation is new i.e ACCUM DEP = 0.00
        8. Determine months based on start date.
        9. if processing month = last depreciation date: dep = NBV - residual
       10. if is fully depreciated NBV = residual
       11. if(distribution required: distribute else skip.
       12. If Assets migrated to Legend, when they become fully depreciated, has cost value equal to accumulated depreciated value. Whereas the NBV is showing N10
 
         */
//        if(tableExists.equals("1")){DisableUsers(systemdate);}  //Disable Users before depreciation commences
        
        // About to Remove Pending Transactions Before Depreciation for the new month
        	boolean pending = pendingTrancations();
        	System.out.println("======>Pending: "+pending);
       // Pending Transactions Removed
        	
        WrongTotalLife(FirstDayProcessDate,endProcessDate);
     //About to Recalculate Depreciation End Date for New Assets
    	RecalculationOfNewAssetDepreciationEndDate(FirstDayProcessDate,endProcessDate,costthreshold);
    //End of Recalculating Depreciation End Date for New Assets	  
    	
//        System.out.println("====proMonth: "+proMonth+"   lastMonth: "+lastMonth+"  proYear: "+proYear+"  lastYear: "+lastYear);
        if (Integer.parseInt(proMonth) == Integer.parseInt(lastMonth) && Integer.parseInt(proYear) == Integer.parseInt(lastYear)) {
            updateYearEnd();
        }
        if (Integer.parseInt(proMonth) == Integer.parseInt(lastMonth) && Integer.parseInt(proYear) == Integer.parseInt(lastYear)) {
            changeFinancialDate(startDate,endDate);
        }
        //Clearing of Finacle_Text Table
        	clearDepreciationLegacyExport();
        // End
	    // Assets that fully depreciated During Monthly Depreciation Last Month 	
        	FullyDepreciated(FirstDayProcessDate,endProcessDate, frequency,processDate);
        // End   
        	//Asset Reclassification Starts
        	reclassificationProcessing(processStartDate, processEndDate); 
        	//Asset Reclassification Ends
    	//About to depreciate Old Assets that has NBV less than monthly Depreciation
	    	OldAssetDepreciationNBVLessMonthly();
	    //End of Old Asset Depreciation that has NBV less than monthly Depreciation  
//	    //About to depreciate Old Assets
//    	OldAssetDepreciationMonthlyCalculation(FirstDayProcessDate,endProcessDate, frequency,processDate);
//	    //End of Old Asset Depreciation  	    	
	    //About to depreciate Old Assets
    	OldAssetDepreciation(FirstDayProcessDate,endProcessDate, frequency,processDate);
	    //End of Old Asset Depreciation  
		// Back Dated Assets Last Month 	
//1	    	BackDateDepreciated(FirstDayProcessDate,endProcessDate, frequency,processDate);
        // End 	  
  	
        //About to depreciate New Assets
        	NewAssetDepreciation(FirstDayProcessDate,endProcessDate, frequency,processDate, costthreshold,nextProcessingDate);
        //End of New Asset Depreciation
            //About to Zerorise assets with Less remaining Life 
        	NewAssetDepreciationLess();
        //End of Zerorising assets with Less remaining Life
       
        //About to Extract Improved assets with Assets that has NBV Ten(10) Naira  
        	extractMonthlyImprovementSingleTransactions(processStartDate, processEndDate);
        	extractMonthlyUploadImprovementTransactions(processStartDate, processEndDate);
        //End of Extract Improved assets with Assets that has NBV Ten(10) Naira
       	
        //About to depreciate Assets that fully depreciate but later improved
        	executeNbvResidual(FirstDayProcessDate,endProcessDate, frequency,processDate,lastProcessingDate);
        //End of Assets that fully depreciate but later improved
       //About to Zerorise assets with NBV ZEROES 
        	AssetDepreciationWithNBVZeros();
        //End of Zerorising assets with NBV ZEROS         	
       //About to Add NBV Balance to Monthly Depreciation for Assets that Fully Depreciated 
        	AdditionOfNBVBalanceToMonthlyDepr(FirstDayProcessDate,endProcessDate, frequency,processDate, costthreshold,nextProcessingDate);
        //End of Add NBV Balance to Monthly Depreciation for Assets that Fully Depreciated    
        	
            //About to Add NBV Balance to Improve NBV to get TOTAL NBV for all Assets 
        	AdditionOfNBVBalanceToIMPROV_NBV();
        	//End of Adding NBV Balance to Improve NBV to get TOTAL NBV for all Assets          	
            //About to Zerorise assets with NBV ZEROES 
        	AssetDepreciationWithMonthlyEqualsResidual();
        	//End of Zerorising assets with NBV ZEROS        
        	
//            //About to Correct Reclassified Transaction with Negative Remaining Life 
//        	AssetReclassificationWithNegativeRemainingLife();
//        	//End of Reclassified Transaction with Negative Remaining Life      
//        	// Assets Reclassified with Old Rate Lower Than New Rate and New Remaining Life is Zero 	
//        	AssetReclassifiedWithOldRateGreaterThanNewRate(FirstDayProcessDate,endProcessDate, frequency,processDate);
//        	// End             	
//            notifyLastDepAsset(depreciationProcessDate,endProcessDate, frequency, userid);
            logDeprecitionTransaction(depreciationProcessDate,endProcessDate, frequency, userid);
            //Start Migrated Asset to Legend when they become fully depreciated, has cost value equal to accumulated depreciated value and NBV is showing N10 
            MigratedAssetWithWrongAccum();
            //End Migrated Asset to Legend when they become fully depreciated, has cost value equal to accumulated depreciated value and NBV is showing N10                
            //About to Correct NBV Value for LAND ASSETS 
            	LANDAssetNBVValue();
        	//End of Correcting NBV Value for LAND ASSETS                  
        logDepreciationTransactionSummary(systemdate, nextdate, userid);
        notifyNextProcessingDate(this.nextProcessingDate, this.lastDate);
        removeCarriageandSpace();
//11/10/2025        monthlyAssetDepreciation(FirstDayProcessDate,endProcessDate, frequency,processDate);
//           System.out.println("About to Backup the AM_ASSET File");
            depdonesap();
            depdoneInsert();  
//            System.out.println("Backup of AM_ASSET File has been completed"); 
//          System.out.println("About to Insert Assets Addition for the month into Addition table ");            
            assetAdditionsSystemRejection(); 
//          System.out.println("Insertion of Assets Addition for the month into Addition table has been completed");  
//            System.out.println("Check--------1");
        depchk.archiveEntrytable();
//        System.out.println("Check--------2");
        depchk.clearEntrytable();
 //       System.out.println("Check--------3");
        depchk.TransfertoEntrytable(userid);
    }

    /**
     * isFullyDepreciated
     *
     * @param cost double
     * @param accumDepreciation double
     * @param residual double
     * @return boolean
     */
    
          
    public boolean isFullyDepreciated(double cost, double accumDepreciation, double residual, int remainLife, int improvremainLife)
    {
        boolean isDepreciated = false;
        double amountDifference = cost - accumDepreciation;
        if(amountDifference == residual || amountDifference < residual || remainLife < 1)
        {
            isDepreciated = true;
        }
        return isDepreciated;
    }

    public boolean isFullyDepreciated(Date depreciationEndDate, Date processingDate, int remainingLife)
    {
        boolean isDepreciated = false;
        if(depreciationEndDate.after(processingDate) && remainingLife > 0)
        {
            isDepreciated = true;
        }
        return isDepreciated;
    }

    public boolean isDepreciatable(Date nextProcessingDate, Date startDate)
    {
        boolean isDepreciatable = false;
        if(nextProcessingDate.after(startDate) || nextProcessingDate.equals(startDate))
        {
            isDepreciatable = true;
        }
        return isDepreciatable;
    }

    public double getCalculatedMonthlyDepreciation(double cost, double nbv, double residue, int remainLife, 
            int totalLife, Date startDate, boolean isNew)
    {
        double calculatedDepreciation = 0.0D;
        if(isNew)
        {
            calculatedDepreciation = nbv / (double)remainLife;
        } else
        {
            calculatedDepreciation = nbv / (double)remainLife;
        }
        String result = formata.formatAmount(calculatedDepreciation);
        result = result.replaceAll(",", "");
        calculatedDepreciation = Double.parseDouble(result);
        return calculatedDepreciation;
    }

    public void WrongTotalLife(String FirstDayProcessDate, String endProcessDate)
    {

        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null; 
        ps = null;  
//        System.out.println("FirstDayProcessDate in FirstDayProcessDate: "+FirstDayProcessDate+"  endProcessDate: "+endProcessDate);
        query = "UPDATE a SET a.Total_Life = FLOOR((100/c.Dep_Rate)*12),a.Remaining_Life = FLOOR((100/c.Dep_Rate)*12) " +
        		 "FROM am_asset a,am_ad_category c WHERE a.Category_ID = c.category_ID " +
                "and a.total_life != (100/c.Dep_Rate)*12 and a.Posting_Date between '"+FirstDayProcessDate+"' and '"+endProcessDate+"' AND c.Dep_Rate > 0";
//        System.out.println("query in OldAssetDepreciation: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing Correct Total Life and Remaining_Life in WrongTotalLife ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }
/*
    public void updateYearEnd()
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;
        ps = null;
        query = "UPDATE AM_GB_COMPANY SET  perform_year_end = 'N'";
try {        
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
    } catch (Exception ex) {  
        System.out.println("WARN: Error updating perform_year_end->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    } 

    }  */
    
    public boolean updateYearEnd()
    {
        boolean exists;
        String qw = "UPDATE AM_GB_COMPANY SET  perform_year_end = 'N' ";
        updateAssetStatusChange(qw);
        exists = true;
        return exists;
    }
    
    public void changeFinancialDate(String date)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        newStartDate = incrementDateYear(date);
        int finMonths = process.getNoOfMonths();
        newEndDate = addMonthToDate(newStartDate, finMonths);
        con = null;
        rs = null;
        ps = null;
        query = "UPDATE AM_GB_COMPANY SET FINANCIAL_START_DATE = ?,FINANCIAL_END_DATE = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setDate(1, dateConvert(newStartDate));
        ps.setDate(2, dateConvert(newEndDate));
        ps.execute();
        process.setFinancialStartDate(newStartDate);
        process.setFinancialEndDate(newEndDate);
    } catch (Exception ex) {  
        System.out.println("WARN: Changing financial Date ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }
    
    public void changeFinancialDate(String startDate,String endDate)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;
        ps = null; 
        String sDay = startDate.substring(0,2);
        String sMonth = startDate.substring(3,5);
        String sYear = startDate.substring(6,10); 
        startDate = sYear+"-"+sMonth+"-"+sDay;
        String eDay = endDate.substring(0,2);
        String eMonth = endDate.substring(3,5);
        String eYear = endDate.substring(6,10); 
        endDate = eYear+"-"+eMonth+"-"+eDay;
//        System.out.println("WARN: startDate: "+startDate+"   endDate: "+endDate);
//        System.out.println("SELECT DATEADD(MM, 12, '"+startDate+"')");
//        System.out.println("SELECT DATEADD(MM, 12, '"+endDate+"')");
        startDate = htmlCombo.findObject("SELECT DATEADD(MM, 12, '"+startDate+"')");
        endDate = htmlCombo.findObject("SELECT DATEADD(MM, 12, '"+endDate+"')");
//        System.out.println("WARN====: startDate: "+startDate+"   endDate: "+endDate);
        query = "UPDATE AM_GB_COMPANY SET FINANCIAL_START_DATE = ?,FINANCIAL_END_DATE = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
//        ps.setDate(1, dateConvert(startDate));
//        ps.setDate(2, dateConvert(endDate));
        ps.setString(1, startDate);
        ps.setString(2, endDate);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Changing financial Date ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }


    public String addMonthToDate(String date, int month)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendarDate = null;
        String added = null;
        if(date == null)
        {
            added = null;
        } else
        {
            try
            {
                Date dDate = sdf.parse(date);
                calendarDate = new GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(2, month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            }
            catch(Exception er)
            {
                System.out.println((new StringBuilder()).append("WARN:Error adding date ->").append(er).toString());
            }
        }
        return added;
    }

    public String incrementDateYear(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendarDate = null;
        String added = null;
        if(date == null)
        {
            added = null;
        } else
        {
            String newStartDate = incrementDateYear(date);
            int finMonths = process.getNoOfMonths();
            String newEndDate = addMonthToDate(newStartDate, finMonths);
            try
            {
                Date dDate = sdf.parse(date);
                calendarDate = new GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(1, 1);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            }
            catch(Exception er)
            {
                System.out.println((new StringBuilder()).append("WARN:Error adding year to date ->").append(er).toString());
            }
        }
        return added;
    }

    public ProcesingInfo getProcessingInfo(){
        ProcesingInfo processingInfo;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        processingInfo = null;
        con = null;
        rs = null;
        ps = null;
        query = "SELECT PROCESSING_DATE,PROCESSING_FREQUENCY,NEXT_PROCESSING_DATE,RESIDUAL_VALUE " +
",    FINANCIAL_START_DATE,FINANCIAL_END_DATE,  FINANCIAL_NO_OFMONTHS FROM AM_GB_" +
"COMPANY"
;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        for(rs = ps.executeQuery(); rs.next();)
        {
            int frequency = rs.getInt("PROCESSING_FREQUENCY");
            double residual = rs.getDouble("RESIDUAL_VALUE");
            Date processingDate = rs.getDate("PROCESSING_DATE");
            Date nextProcessingDate = rs.getDate("NEXT_PROCESSING_DATE");
            String financialStartDate = sdf.format(rs.getDate("FINANCIAL_START_DATE"));
            String financialEndDate = sdf.format(rs.getDate("FINANCIAL_END_DATE"));
            int noOfMonths = rs.getInt("FINANCIAL_NO_OFMONTHS");
            processingInfo = new ProcesingInfo(frequency, residual, processingDate, nextProcessingDate, financialStartDate, financialEndDate, noOfMonths);
        }
    } catch (Exception ex) {  
        System.out.println("WARNING: Error getting Processing Info->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        closeConnection(con, ps, rs);
		return processingInfo;
    }

    public void logDeprecitionTransaction(AssetTransaction assetTran, String nextdate, String userid)
    {
        Connection con;
        PreparedStatement ps;
        String query;
        con = null;
        ps = null;
        query = "INSERT INTO monthly_depreciation_processing (ASSET_ID,MONTHLY_DEP,TRAN_DATE, DEP" +
"_DATE, userid,no_of_month)  VALUES (?,?,?,?,?,?)"
;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, assetTran.getAssetId());
        ps.setDouble(2, assetTran.getMonthlyDepreciation());
        ps.setDate(3, dateConvert(getCompSystemDate()));
        ps.setDate(4, dateConvert(nextdate));
        ps.setString(5, userid);
        ps.setInt(6, assetTran.getFrequency());
        ps.execute();
        }
        catch (Exception ex){
        System.out.println((new StringBuilder()).append("WARNING: Error logging Depreciation transaction->").append(ex.getMessage()).toString());
        } finally {
            closeConnection(con, ps);
        }
    }

    public String executeNbvResidual(AssetTransaction assetTran)
    {
        String output;
        Connection con;
        PreparedStatement ps;
        String query;
//        System.out.println(" in executeNbvResidual method >> ");
        double month = 0.0D;
        output = "fail";
        con = null;
        ps = null;
        query = (new StringBuilder()).append("UPDATE AM_ASSET SET  MONTHLY_DEP =").append(month).append(" ,NBV=RESIDUAL_VALUE WHERE ASSET_ID = ? ").toString();
        try {
        System.out.println(query);
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, assetTran.getAssetId());
        ps.executeUpdate();
        setSucessful(true);
        output = "success";
    } catch (Exception ex) {  
        System.out.println("WARNING: Error executing update on NBV ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }
        return output;
    }

    public void executeProcessDepreciation(AssetTransaction assetTran, String nextdate, String userid, int remainLife, boolean isImprovement, int usefulLIfeFromAmAsset, double oldAccumDep, int improve_usefullife, double oldimprovAccumDep, int improvusefulLifeFromAsset,int improvremainLife,double improvecost,double improvenbv,String isImproved,double newmonthlyDep)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
  //      PreparedStatement ps4;
        PreparedStatement ps5;
        String query;
        String query2;
        String query3;
//        String query4;
        String query5;
//        System.out.println("*****************about to update remaining life*********************** ");
//        System.out.println((new StringBuilder()).append("assetTran.getFrequency() value ").append(assetTran.getFrequency()).toString());
        con = null;
        ps = null;
        ps2 = null;
        ps3 = null;
//        ps4 = null;
        ps5 = null;
        double depreciationmonthly =  0.00;
        query = "UPDATE AM_ASSET  SET  ACCUM_DEP = ACCUM_DEP + ? ,MONTHLY_DEP = ? , NBV = COST_PR" +
"ICE - ?,USEFUL_LIFE = USEFUL_LIFE + ? ,REMAINING_LIFE = REMAINING_LIFE - ?, " + 
 "dep_ytd= dep_ytd + ?,TOTAL_NBV = (COST_PRICE - (ACCUM_DEP + ?))+coalesce(IMPROV_NBV,0)  WHERE ASSET_ID = ? ";
         
        query2 = "UPDATE AM_ASSET  SET  ACCUM_DEP = ACCUM_DEP + ? ,MONTHLY_DEP = ? , NBV = COST_PR" +
"ICE - (ACCUM_DEP + ?),USEFUL_LIFE = USEFUL_LIFE + ? ,REMAINING_LIFE = REMAINING_" +
"LIFE - ?, dep_ytd= dep_ytd + ?, IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + ? ,IMPROV_MONTHLYDEP = ? , IMPROV_NBV = IMPROV_C" +
"OST - (coalesce(IMPROV_ACCUMDEP,0) + ?),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + ? ,IMPROV_REMAINLIFE = IMPROV_REMAIN" +
"LIFE - ?, TOTAL_NBV = (COST_PRICE - (ACCUM_DEP + ?))+(IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + ?))  WHERE ASSET_ID = ? ";

        query3 = "UPDATE AM_ASSET SET IMPROV_MONTHLYDEP = 0.00 WHERE ASSET_ID = ?";
//        query4 = "UPDATE AM_ASSET SET TOTAL_NBV = NBV+coalesce(IMPROV_NBV,0) WHERE ASSET_ID = ?";
        query5 = "UPDATE am_asset_improvement SET IMPROVED = 'N' WHERE ASSET_ID = '"+ assetTran.getAssetId()+"'";
        try {
        con = getConnection("legendPlus");
       // ps = con.prepareStatement(query2);
//        System.out.println("<<<<<<<improvenbv: "+improvenbv+"    improvremainLife: "+improvremainLife+"  >>>>>>>>isImproved: "+isImproved+"  <<<<<<<<<oldAccumDep: "+oldAccumDep);
        if(isImproved.equalsIgnoreCase("P")){depreciationmonthly =  improvenbv/improvremainLife;}
        double zeroMonthlyDep = 0.00;
//        System.out.println("<<<<<<<depreciationmonthly: "+depreciationmonthly+"   <<<<<improve_usefullife: "+improve_usefullife+"  <<<<<<remainLife: "+remainLife+"   <<<<<<isImprovement: "+isImprovement+"   <<<<<<<<<<<improvremainLife: "+improvremainLife+"  <<<<<<Frequency: "+assetTran.getFrequency()+"  <<<<<<<<<<newmonthlyDep: "+newmonthlyDep);
        if(remainLife > 0){
        if(improvremainLife == 0){
        	ps = con.prepareStatement(query);
        if(isImprovement)  
        {
       //     ps.setDouble(1, newmonthlyDep  * (double)assetTran.getFrequency());
            ps.setDouble(1, oldAccumDep + (newmonthlyDep * (double)assetTran.getFrequency()));
        } else
        { 
            ps.setDouble(1, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }
        if(isImprovement)  
        {        
        	ps.setDouble(2, newmonthlyDep  * (double)assetTran.getFrequency());
        }
        else
        {
        	ps.setDouble(2, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());	
        }
        if(isImprovement)
        {
            ps.setDouble(3, oldAccumDep + (newmonthlyDep  * (double)assetTran.getFrequency()));
        } else
        {
            ps.setDouble(3, oldAccumDep + (assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency()));
        }
        ps.setInt(4, assetTran.getFrequency());
        ps.setInt(5, assetTran.getFrequency());
//        System.out.println((new StringBuilder()).append("the value of ImprovMonthlyDepreciation 1 is >>>>>>>> ").append(assetTran.getImprovMonthlyDepreciation()).toString());
        if(isImprovement)
        {
        	ps.setDouble(6, newmonthlyDep * (double)assetTran.getFrequency());
        }
        else
        {
        	ps.setDouble(6, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }
        if(isImprovement)
        {
            ps.setDouble(7, newmonthlyDep * (double)assetTran.getFrequency());
        } else
        {
            ps.setDouble(7, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }        
        ps.setString(8, assetTran.getAssetId());
        ps.executeUpdate();
        }   
        else{
        	ps2 = con.prepareStatement(query2);
//        	System.out.println("<<<<<<<<improve_usefullife 2: "+improve_usefullife);
       	if(isImproved == "P")
        {
            ps2.setDouble(1, oldAccumDep + assetTran.getMonthlyDepreciation());
        } else
        {
//            System.out.println((new StringBuilder()).append("the value of frequency 2 is >>>>>>>> not improvement").append(assetTran.getFrequency()).toString());
            ps2.setDouble(1, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }
        ps2.setDouble(2, assetTran.getMonthlyDepreciation());
       	if(isImproved == "P")
        {
            ps2.setDouble(3, oldAccumDep + assetTran.getMonthlyDepreciation());
        } else
        {
            ps2.setDouble(3, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }
        ps2.setInt(4, assetTran.getFrequency());
        ps2.setInt(5, assetTran.getFrequency());
        ps2.setDouble(6, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
       // ps.setString(7, assetTran.getAssetId());
        if(isImproved == "P"){
//        	System.out.println("<<<<<<<improvAccumDep > 0 Calculated 2 >>>>>> "+(oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation()));
        ps2.setDouble(7, oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation());
        }else{
//        	System.out.println("<<<<<<<improvAccumDep Calculated 2 >>>>>> "+(improvenbv/improvremainLife));
        	ps2.setDouble(7, depreciationmonthly * (double)assetTran.getFrequency());
        }
        
        ps2.setDouble(8, depreciationmonthly * (double)assetTran.getFrequency());
        if(isImproved == "P"){
//        	System.out.println("<<<<<<<<IMPROVE NBV 1: ");
        	ps2.setDouble(9,  oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation());
        }
        else{
//        	System.out.println("<<<<<<<<IMPROVE NBV 2: "+"   depreciationmonthly: "+depreciationmonthly);
        	ps2.setDouble(9, depreciationmonthly * (double)assetTran.getFrequency());
         }
        ps2.setInt(10, assetTran.getFrequency());
        ps2.setInt(11, assetTran.getFrequency());
        if(isImprovement)
        {
            ps2.setDouble(12, oldAccumDep + assetTran.getMonthlyDepreciation());
        } else
        {
            ps2.setDouble(12, assetTran.getMonthlyDepreciation() * (double)assetTran.getFrequency());
        }     
        if(isImproved == "P"){
        	ps2.setDouble(13,  oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation());
        }
        else{
        	ps2.setDouble(13, depreciationmonthly * (double)assetTran.getFrequency());
         }
        ps2.setString(14, assetTran.getAssetId());
        ps2.executeUpdate();
        }
        }
        
	   	if(improvremainLife ==0){
	    ps3 = con.prepareStatement(query3);
	    ps3.setString(1, assetTran.getAssetId());
	    ps3.executeUpdate();
	   	}

        if(isImproved.equalsIgnoreCase("P")){
	    ps5 = con.prepareStatement(query5);
	    ps5.executeUpdate();
	   	}        
        notifyLastDepAsset(assetTran.getAssetId(), nextdate);
        logDeprecitionTransaction(assetTran, nextdate, userid);
        setSucessful(true);        
    } catch (Exception ex) {  
        System.out.println("WARNING: Error executing Processed Depreciation ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps);
        closeConnection(con, ps2);
        closeConnection(con, ps3);
   //     closeConnection(con, ps4);
        closeConnection(con, ps5);
    }
        closeConnection(con, ps);
        closeConnection(con, ps2);
        closeConnection(con, ps3);
 //       closeConnection(con, ps4);
        closeConnection(con, ps5);
    }

    private void notifyNextProcessingDate(Date nextProcessingDate, Date dd)
    {
        Connection con;
        PreparedStatement ps;
        String SLIPT_QUERY;
        con = null;
        ps = null;
        SLIPT_QUERY = "UPDATE AM_GB_COMPANY SET PROCESSING_DATE = ?,NEXT_PROCESSING_DATE = ?    ";
        dd = nextProcessingDate;
        nextProcessingDate = df.getDateAddByMonth(this.nextProcessingDate, process.getFrequency());
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(SLIPT_QUERY);
        ps.setDate(1, dateConvert(dd));
        ps.setDate(2, dateConvert(nextProcessingDate));
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifing processing Date ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public void AdditionOfNBVBalanceToMonthlyDepr(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate, double thresholdcost,String nextProcessingDate)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;  
        ps = null;
        query = "UPDATE AM_ASSET SET Monthly_Dep = Monthly_Dep + NBV, NBV = RESIDUAL_VALUE " +
                "WHERE Dep_End_Date between '"+FirstDayProcessDate+"' AND '"+endProcessDate+"'  AND USEFUL_LIFE = TOTAL_LIFE AND REMAINING_LIFE = 0" ;
   
//        System.out.println("<<<<<<query in AdditionOfNBVBalanceToMonthlyDepr: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing Monthly Depreciation and NBV in AdditionOfNBVBalanceToMonthlyDepr ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }

    public void notifyProcessedAsset(String assetid, String status)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET ASSET_STATUS = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, status);
        ps.setString(2, assetid);
        ps.executeUpdate();

        } catch (Exception ex) {
        	System.out.println(
        			"WARNING: Error notifying processed asset - " + assetid +
        			"->" +
        			ex.getMessage());
        } finally {
        	closeConnection(con, ps);
        }

    }

    public void AdditionOfNBVBalanceToIMPROV_NBV()
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;  
        ps = null;
        query = "UPDATE AM_ASSET SET TOTAL_NBV = NBV + IMPROV_NBV ";
//        System.out.println("<<<<<<query in AdditionOfNBVBalanceToIMPROV_NBV: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing TOTAL NBV in AdditionOfNBVBalanceToIMPROV_NBV ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }

    public void notifyRaisedClassifiedAsset(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE  AM_ASSETRECLASSIFICATION SET RAISE_ENTRY = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, "R");
        ps.setString(2, assetid);
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifying classified asset - " + assetid +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void notifyRaisedAsset(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE  AM_ASSET SET RAISE_ENTRY = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, "R");
        ps.setString(2, assetid);
        ps.executeUpdate();
        } catch (Exception ex) {      
            System.out.println(
                    "WARNING: Error notifying classified asset - " + assetid +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void notifyProcessedAsset(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET ASSET_STATUS = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, "ACTIVE");
        ps.setString(2, assetid);
        ps.executeUpdate();
      
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifying processed asset - " + assetid +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public boolean isAlreadyProcessedDistribution(String distCode)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean alreadyProcessed;
        String PROCESS_QUERY;
        con = null;
        ps = null;
        rs = null;
        alreadyProcessed = false;
        PROCESS_QUERY = "SELECT count(DIST_ID) FROM AM_DEPR_DIST WHERE DIST_ID  = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(PROCESS_QUERY);
        ps.setString(1, distCode);
        rs = ps.executeQuery();

        while (rs.next()) {
            if (rs.getInt(1) > 0) {
                alreadyProcessed = true;
            }
        }

    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error isAlreadyProcessedDistribution - " +
                distCode + "->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
    return alreadyProcessed;
}        
        
  

    public void clearDistributionEntry(String distCode)
    {
        Connection con;
        PreparedStatement ps;
        String CLEAR_QUERY;
        con = null;
        ps = null;
        CLEAR_QUERY = "DELETE FROM AM_DEPR_DIST WHERE DIST_ID = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(CLEAR_QUERY);
        ps.setString(1, distCode);
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error clearDistributionEntry  - " + distCode +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public void splitDistribution(String assetId, String type, String userId, String distributionId, String expenseAccount, String accumAccount, double assignedValue, String counter, String rem)
    {
        Connection con;
        PreparedStatement ps;
        String SLIPT_QUERY;
        con = null;
        ps = null;
        SLIPT_QUERY = "INSERT INTO AM_DEPR_DIST(ASSET_ID,DIST_ID,TYPE,STATUS,USER_ID,CREATE_DT ,DIST_EX" +
"P_ACCT,DIST_ACCUM_ACCT,VALUE_ASSIGNED,SEQUENCE_NO,REMAINDER) VALUES (?,?,?,?,?,?" +
",?,?,?,?,?)   "
;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(SLIPT_QUERY);
        ps.setString(1, assetId);
        ps.setString(2, distributionId);
        ps.setString(3, type);
        ps.setString(4, "ACTIVE");
        ps.setString(5, userId);
        ps.setDate(6, dateConvert(new Date()));
        ps.setString(7, expenseAccount);
        ps.setString(8, accumAccount);
        ps.setDouble(9, assignedValue);
        ps.setString(10, counter);
        if(rem.equalsIgnoreCase(counter))
        {
            ps.setString(11, "Y");
        } else
        {
            ps.setString(11, "N");
        }
        ps.executeUpdate();
        setSucessful(true);
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error Sliptting Depreciation ->" +
                    ex.getMessage());
            ex.printStackTrace();
            this.setSucessful(false);
        } finally {
            closeConnection(con, ps);
        }
    }

    public ArrayList findDistributionByAssetId(String assetId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        DistributionDetail dd = null;
        collection = new ArrayList();
        FINDER_QUERY = "SELECT DIST_ID,TYPE,STATUS,DIST_EXP_ACCT,DIST_ACCUM_ACCT,VALUE_ASSIGNED,SEQUENCE" +
"_NO,REMAINDER    FROM AM_DEPR_DIST  WHERE ASSET_ID = ? ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, assetId);
    //    DistributionDetail dd;
        rs = ps.executeQuery();
        while (rs.next()) {
            String id = rs.getString("DIST_ID");
            String type = rs.getString("TYPE");
            String status = rs.getString("STATUS");
            String expenseAccount = rs.getString("DIST_EXP_ACCT");
            String accumAccount = rs.getString("DIST_ACCUM_ACCT");
            double amount = rs.getDouble("VALUE_ASSIGNED");
            String remainder = rs.getString("REMAINDER");
            dd = new DistributionDetail(id, assetId, type, status, expenseAccount, accumAccount, amount, remainder);
            collection.add(dd);
        }
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error findDistributionByAssetId ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public String getDefaultDistributionAccount(String assetid)
    {
        String distributionAccount;
        String GL_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        distributionAccount = "";
        GL_QUERY = "";
        con = null;
        ps = null;
        rs = null;
        String BRANCH_ACCOUNT_QUERY = "SELECT B.GL_PREFIX+C.ACCUM_DEP_LEDGER  FROM AM_AD_BRANCH B,AM_AD_CATEGORY C,AM_A" +
"SSET A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.BRANCH_ID = B.BRANCH_ID   AND" +
" A.ASSET_ID = ?"
;
        String DEPARTMENT_ACCOUNT_QUERY = "SELECT D.GL_PREFIX+C.ACCUM_DEP_LEDGER FROM sbu_branch_dept D,AM_AD_CATEGORY C,AM" +
"_ASSET A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.DEPT_ID = D.DEPTID  AND  A." +
"BRANCH_ID = D.BRANCHID AND A.ASSET_ID = ?"
;
        String SECTION_ACCOUNT_QUERY = "SELECT D.GL_PREFIX+C.ACCUM_DEP_LEDGER FROM sbu_dept_section D,AM_AD_CATEGORY C,A" +
"M_ASSET A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.DEPT_ID = D.DEPTID  AND  A" +
".BRANCH_ID = D.BRANCHID AND  A.SECTION_ID = D.SECTIONID AND A.ASSET_ID = ?"
;
        String sbuLevel[] = getSUBSupportedLevel();
        String required = sbuLevel[0];
        String level = sbuLevel[1];
        if(required.equalsIgnoreCase("Y"))
        {
            if(level.equalsIgnoreCase("department"))
            {
                GL_QUERY = DEPARTMENT_ACCOUNT_QUERY;
            } else
            if(level.equalsIgnoreCase("section/unit"))
            {
                GL_QUERY = SECTION_ACCOUNT_QUERY;
            }
        } else
        {
            GL_QUERY = BRANCH_ACCOUNT_QUERY;
        }
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(GL_QUERY);
//        System.out.println((new StringBuilder()).append("GL_QUERY___").append(GL_QUERY).toString());
        ps.setString(1, assetid);
        rs = ps.executeQuery();

        while (rs.next()) {
            distributionAccount = rs.getString(1);
        }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(
                    "WARNING: Error getting Default Distribution Account ->\n" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return distributionAccount;
    }

    public String getDefaultGroupDistributionAccount(String groupID)
    {
        String distributionAccount;
        String GL_QUERY;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        distributionAccount = "";
        GL_QUERY = "";
        con = null;
        ps = null;
        rs = null;
        String BRANCH_ACCOUNT_QUERY = "SELECT B.GL_PREFIX+C.ACCUM_DEP_LEDGER  FROM AM_AD_BRANCH B,AM_AD_CATEGORY C,am_g" +
"roup_asset_main A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.BRANCH_ID = B.BRAN" +
"CH_ID   AND A.GROUP_ID = ?"
;
        String DEPARTMENT_ACCOUNT_QUERY = "SELECT D.GL_PREFIX+C.ACCUM_DEP_LEDGER FROM sbu_branch_dept D,AM_AD_CATEGORY C,am" +
"_group_asset_main A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.DEPT_ID = D.DEPT" +
"ID  AND  A.BRANCH_ID = D.BRANCHID AND A.GROUP_ID = ?"
;
        String SECTION_ACCOUNT_QUERY = "SELECT D.GL_PREFIX+C.ACCUM_DEP_LEDGER FROM sbu_dept_section D,AM_AD_CATEGORY C,a" +
"m_group_asset_main A  WHERE A.CATEGORY_ID = C.CATEGORY_ID  AND A.DEPT_ID = D.DEP" +
"TID  AND  A.BRANCH_ID = D.BRANCHID AND  A.SECTION_ID = D.SECTIONID AND A.GROUP_I" +
"D = ?"
;
        String sbuLevel[] = getSUBSupportedLevel();
        String required = sbuLevel[0];
        String level = sbuLevel[1];
        if(required.equalsIgnoreCase("Y"))
        {
            if(level.equalsIgnoreCase("department"))
            {
                GL_QUERY = DEPARTMENT_ACCOUNT_QUERY;
            } else
            if(level.equalsIgnoreCase("section/unit"))
            {
                GL_QUERY = SECTION_ACCOUNT_QUERY;
            }
        } else
        {
            GL_QUERY = BRANCH_ACCOUNT_QUERY;
        }
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(GL_QUERY);
//        System.out.println((new StringBuilder()).append("GL_QUERY___").append(GL_QUERY).toString());
        ps.setString(1, groupID);
        rs = ps.executeQuery();
        while (rs.next()) {
            distributionAccount = rs.getString(1);
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println(
                "WARNING: Error getting Default Distribution Account ->\n" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        return distributionAccount;
    }

    public String getNewDitributionCode()
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String distCode;
        String query;
        con = null;
        ps = null;
        rs = null;
        distCode = "0";
        query = "SELECT COUNT(DISTINCT(ASSET_ID))+1 AS NEW_CODE FROM AM_DEPR_DIST";
        try{
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {
            distCode = rs.getString(1);
        }

    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error getting new distribution code ->\n" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
    if (distCode == null || distCode == "") {
        distCode = "1";
    }
    return distCode;
    }

    private String[] getSUBSupportedLevel()
    {
        String SUBLevel[];
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        SUBLevel = new String[2];
        con = null;
        rs = null;
        ps = null;
        query = "SELECT SBU_REQUIRED,SBU_LEVEL  FROM AM_GB_COMPANY";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {

            String required = rs.getString("SBU_REQUIRED");
            String level = rs.getString("SBU_LEVEL");
            SUBLevel[0] = required;
            SUBLevel[1] = level;
        }

        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error getting SUBSupportedLevel ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return SUBLevel;

    }

    public String[] getTaxVatGLFromCompany()
    {
        String TaxVatGL[];
        Connection con;
        ResultSet rs;
        PreparedStatement ps; 
        String query;
        TaxVatGL = new String[4];
        con = null;
        rs = null;
        ps = null;
        query = "SELECT VAT_ACCOUNT,WHT_ACCOUNT,FED_WHT_ACCOUNT,SELFCHARGEVAT FROM AM_GB_COMPANY";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {

            String vatGL = rs.getString(1);
            String taxGL = rs.getString(2);
            String fedwhtGL = rs.getString(3);
            String selfchrgGL = rs.getString(4);
            TaxVatGL[0] = vatGL;
            TaxVatGL[1] = taxGL;
            TaxVatGL[2] = fedwhtGL;
            TaxVatGL[3] = selfchrgGL;
        }

    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error getting Tax and Vat GL From Company ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        return TaxVatGL;
    }

    public boolean[] getAccountTypeTranCode()
    {
        boolean AccTypeTranCode[];
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        AccTypeTranCode = new boolean[2];
        con = null;
        rs = null;
        ps = null;
        query = "SELECT REQ_ACCTTYPE,REQ_TRANCODE   FROM  AM_AD_LEGACY_SYS_CONFIG";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {
            String accType = rs.getString(1);
            String tranCode = rs.getString(2);
            AccTypeTranCode[0] = accType.equalsIgnoreCase("Y");
            AccTypeTranCode[1] = tranCode.equalsIgnoreCase("Y");
        }
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error getting Account Type and TranCode From SysConfig ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return AccTypeTranCode;
    }

    public void logRaisedEntryTransaction(String debitAccount, String debitAccType, String debitTranCode, String debitNarration, String creditAccount, String creditAccType, String creditTranCode, 
            String creditNarration, String amount, String userId, String supervisor, String legacyId, String batchId, String rejectReason)
    {
        String tranQuery;
        Connection con;
        PreparedStatement ps;
        tranQuery = "INSERT INTO AM_ENTRY_TABLE(DR_ACCT,DR_ACCT_TYPE,DR_TRAN_CODE,DR_NARRATION,CR_ACC" +
"T,CR_ACCT_TYPE,CR_TRAN_CODE,CR_NARRATION,AMOUNT,USER_ID,SUPER_ID,LEGACY_ID,BATCH" +
"_ID,POSTING_DATE,EFFECTIVE_DATE,PROCESS_STATUS ,SUPERVISOR,REJECT_REASON \t) VAL" +
"UES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        if(amount == null || amount.equals(""))
        {
            amount = "0.00";
        }
        if(userId == null || userId.equals(""))
        {
            userId = "0";
        }
        if(supervisor == null || supervisor.equals(""))
        {
            userId = "0";
        }
        con = null;
        ResultSet rs = null;
        ps = null;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(tranQuery);
        ps.setString(1, debitAccount);
        ps.setString(2, debitAccType);
        ps.setString(3, debitTranCode);
        ps.setString(4, debitNarration);
        ps.setString(5, creditAccount);
        ps.setString(6, creditAccType);
        ps.setString(7, creditTranCode);
        ps.setString(8, creditNarration);
        ps.setDouble(9, Double.parseDouble(amount));
        ps.setInt(10, Integer.parseInt(userId));
        ps.setInt(11, Integer.parseInt(supervisor));
        ps.setString(12, legacyId);
        ps.setString(13, batchId);
        ps.setDate(14, dateConvert(new Date()));
        ps.setDate(15, dateConvert(new Date()));
        ps.setString(16, "U");
        ps.setString(17, supervisor);
        ps.setString(18, rejectReason);
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error raising entry  ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public String getProcessingDate()
    {
        String sysDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        sysDate = "";
        con = null;
        rs = null;
        ps = null;
        query = "SELECT PROCESS_DATE FROM  AM_AD_LEGACY_SYS_CONFIG";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                sysDate = df.formatDate(rs.getDate(1));
            }

        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error getting processing date From SysConfig ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return sysDate;
    }

    public String getSystemDate()
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        String sysDate;
        String selectQuery;
        con = null;
        ps = null;
        rs = null;
        sysDate = "";
        selectQuery = "SELECT PROCESS_DATE FROM AM_AD_LEGACY_SYS_CONFIG";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        rs = ps.executeQuery();

        while (rs.next()) {

            sysDate = formatDate(rs.getDate(1));
        }

    } catch (Exception e) {
        System.out.println(
                "WARNING:Error getting SystemDate->" +
                e.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        return sysDate;
    }

    public String getCompSystemDate()
    {
        String sysDate = formatDate(new Date());
        return sysDate;
    }

    public boolean depdone()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
        SimpleDateFormat sdff = new SimpleDateFormat("MM");
        SimpleDateFormat sdff2 = new SimpleDateFormat("yyyy");
        sql = (new StringBuilder()).append("SELECT * FROM month_depprocesing_summary WHERE datepart(month,DEP_DATE) = ").append(sdff.format(df.dateConvert(getProcessingInfo().getNextProcessingDate()))).append(" AND ").append(" datepart(year,DEP_DATE) = ").append(sdff2.format(df.dateConvert(getProcessingInfo().getNextProcessingDate()))).toString();
        System.out.println(sql);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("legendPlus");
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        exists = rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs); 
        }
        return exists;
    }

    public void logRaisedEntryTransaction(String debitAccount, String debitAccType, String debitTranCode, String debitNarration, String creditAccount, String creditAccType, String creditTranCode, 
            String creditNarration, String amount, String userId, String supervisor, String legacyId, String batchId, String rejectReason, 
            String currid, String branchcode)
    {
        String tranQuery;
        Connection con;
        PreparedStatement ps;
        tranQuery = "INSERT INTO AM_ENTRY_TABLE(DR_ACCT,DR_ACCT_TYPE,DR_TRAN_CODE,DR_NARRATION,CR_ACC" +
"T,CR_ACCT_TYPE,CR_TRAN_CODE,CR_NARRATION,AMOUNT,USER_ID,SUPER_ID,LEGACY_ID,BATCH" +
"_ID,POSTING_DATE,EFFECTIVE_DATE,PROCESS_STATUS ,SUPERVISOR,REJECT_REASON,currenc" +
"y_id,branchCode \t) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
        if(amount == null || amount.equals(""))
        {
            amount = "0.00";
        }
        if(userId == null || userId.equals(""))
        {
            userId = "0";
        }
        if(supervisor == null || supervisor.equals(""))
        {
            supervisor = "0";
        }
        if(currid == null || currid.equals(""))
        {
            currid = "1";
        }
        con = null;
        ResultSet rs = null;
        ps = null;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(tranQuery);
        ps.setString(1, debitAccount);
        ps.setString(2, debitAccType);
        ps.setString(3, debitTranCode);
        ps.setString(4, debitNarration);
        ps.setString(5, creditAccount);
        ps.setString(6, creditAccType);
        ps.setString(7, creditTranCode);
        ps.setString(8, creditNarration);
        ps.setDouble(9, Double.parseDouble(amount));
        ps.setInt(10, Integer.parseInt(userId));
        ps.setInt(11, Integer.parseInt(supervisor));
        ps.setString(12, legacyId);
        ps.setString(13, batchId);
        ps.setDate(14, dateConvert(new Date()));
        ps.setDate(15, dateConvert(new Date()));
        ps.setString(16, "U");
        ps.setString(17, supervisor);
        ps.setString(18, rejectReason);
        ps.setString(19, currid);
        ps.setString(20, branchcode);
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error raising entry 2   ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public void logDepreciationTransactionSummary(String procdate, String nextdate, String userid)
    {
        Connection con;
        PreparedStatement ps;
        String query;
        con = null;
        ps = null;
        query = "INSERT INTO [month_depprocesing_summary]([processing_date],[userid],[dep_date]) " +
"VALUES(?,?,?)";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setDate(1, df.dateConvert(procdate));
        ps.setString(2, userid);
        ps.setDate(3, dateConvert(nextdate));
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error logging Depreciation transaction Summary->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void notifyProcessedDistributedAsset(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET REQ_REDISTRIBUTION = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, "Y");
        ps.setString(2, assetid);
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifying processed asset - " +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void notifyLastDepAsset(String assetid, String datex)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET last_dep_date = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setDate(1, df.dateConvert(datex));
        ps.setString(2, assetid);
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifying processed asset - " + assetid +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public boolean isAlreadyProcessedComponent(String componentCode)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean alreadyProcessed;
        String PROCESS_QUERY;
        con = null;
        ps = null;
        rs = null;
        alreadyProcessed = false;
        PROCESS_QUERY = "SELECT count(COMPONENT_ID) FROM AM_COMPONENT_DIST WHERE COMPONENT_ID  = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(PROCESS_QUERY);
        ps.setString(1, componentCode);
        rs = ps.executeQuery();

        while (rs.next()) {
            if (rs.getInt(1) > 0) {
                alreadyProcessed = true;
            }
        }

    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error isAlreadyProcessedDistribution - " +
                componentCode + "->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }      
        return alreadyProcessed;
    }

    public void clearComponentEntry(String componentCode)
    {
        Connection con;
        PreparedStatement ps;
        String CLEAR_QUERY;
        con = null;
        ps = null;
        CLEAR_QUERY = "DELETE FROM AM_COMPONENT_DIST WHERE COMPONENT_ID = ?";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(CLEAR_QUERY);
        ps.setString(1, componentCode);
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error clearDistributionEntry  - " + componentCode +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public ArrayList findComponentByAssetId(String assetId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        ComponentDetail dd = null;
        collection = new ArrayList();
        FINDER_QUERY = "SELECT COMPONENT_ID,TYPE,STATUS,COMPONENT_EXP_ACCT,COST_VALUE,VALUE_ASSIGNED,SEQ" +
"UENCE_NO,REMAINDER ,DEP_ASSIGNED   FROM AM_COMPONENT_DIST  WHERE ASSET_ID = ? "
;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, assetId);
//        ComponentDetail dd;
        rs = ps.executeQuery();

        while (rs.next()) {
            String id = rs.getString("COMPONENT_ID");
            String type = rs.getString("TYPE");
            String status = rs.getString("STATUS");
            String expenseAccount = rs.getString("COMPONENT_EXP_ACCT");
            double costValue = rs.getDouble("COST_VALUE");
            double amount = rs.getDouble("VALUE_ASSIGNED");
            String remainder = rs.getString("REMAINDER");
            double depAssigned = rs.getDouble("DEP_ASSIGNED");
            String serialNumber = rs.getString("SEQUENCE_NO");
            dd = new ComponentDetail(id, assetId, type, status, expenseAccount, costValue, amount, remainder, depAssigned, serialNumber);
            collection.add(dd);
        }

        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error findDistributionByAssetId ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public ArrayList findComponentByAssetIdView(String assetId)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList collection;
        String FINDER_QUERY;
        con = null;
        ps = null;
        rs = null;
        ComponentDetail dd = null;
        collection = new ArrayList();
        FINDER_QUERY = "SELECT COMPONENT_ID,TYPE,STATUS,COMPONENT_EXP_ACCT,COST_VALUE,VALUE_ASSIGNED,SEQ" +
"UENCE_NO,REMAINDER ,DEP_ASSIGNED   FROM AM_COMPONENT_DIST  WHERE ASSET_ID = ? AN" +
"D CREATED = 'N' ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, assetId);
     //   ComponentDetail dd;
        for(rs = ps.executeQuery(); rs.next(); collection.add(dd))
        {
            String id = rs.getString("COMPONENT_ID");
            String type = rs.getString("TYPE");
            String status = rs.getString("STATUS");
            String expenseAccount = rs.getString("COMPONENT_EXP_ACCT");
            double costValue = rs.getDouble("COST_VALUE");
            double amount = rs.getDouble("VALUE_ASSIGNED");
            String remainder = rs.getString("REMAINDER");
            double depAssigned = rs.getDouble("DEP_ASSIGNED");
            String serialNumber = rs.getString("SEQUENCE_NO");
            dd = new ComponentDetail(id, assetId, type, status, expenseAccount, costValue, amount, remainder, depAssigned, serialNumber);
        }
    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error findDistributionByAssetId ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }
        return collection;
    }

    public void insertComponent(String assetId, String type, String userId, String distributionId, String expenseAccount, double costValue, 
            double assignedValue, String counter, String rem, double depAssigned)
    {
        Connection con;
        PreparedStatement ps;
        String SLIPT_QUERY;
        con = null;
        ps = null;
        SLIPT_QUERY = "INSERT INTO AM_COMPONENT_DIST(ASSET_ID,COMPONENT_ID,TYPE,STATUS,USER_ID,CREATE_D" +
"T ,COMPONENT_EXP_ACCT,COST_VALUE,VALUE_ASSIGNED,SEQUENCE_NO,REMAINDER,DEP_ASSIGN" +
"ED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(SLIPT_QUERY);
        ps.setString(1, assetId);
        ps.setString(2, distributionId);
        ps.setString(3, type);
        ps.setString(4, "ACTIVE");
        ps.setString(5, userId);
        ps.setDate(6, dateConvert(new Date()));
        ps.setString(7, expenseAccount);
        ps.setDouble(8, costValue);
        ps.setDouble(9, assignedValue);
        ps.setString(10, counter);
        if(rem.equalsIgnoreCase(counter))
        {
            ps.setString(11, "Y");
        } else
        {
            ps.setString(11, "N");
        }
        ps.setDouble(12, depAssigned);
        ps.executeUpdate();
        updateMultipleComponent(assetId);
        setSucessful(true);
    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error submittting component ->" +
                ex.getMessage());
        ex.printStackTrace();
        this.setSucessful(false);
    } finally {
        closeConnection(con, ps);
    }
    }

    public void updateMultipleComponent(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET Multiple = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.setString(1, "Y");
        ps.setString(2, assetid);
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error updating asset component- " + assetid +
                    "->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public boolean recordInserted(String assetId, String depExpenseValue, double depAccumValueAmount, double depreciationAmount, String sequenceNo)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        boolean done;
        String query;
        con = null;
        ps = null;
        rs = null;
        done = true;
        query = "SELECT * FROM AM_COMPONENT_DIST WHERE ASSET_ID=? AND COMPONENT_EXP_ACCT=? AND CO" +
"ST_VALUE=? AND DEP_ASSIGNED=? SEQUENCE_NO= ? "
;
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, assetId);
        ps.setString(2, depExpenseValue);
        ps.setDouble(3, depAccumValueAmount);
        ps.setDouble(4, depreciationAmount);
        ps.setString(5, sequenceNo);
        for(rs = ps.executeQuery(); rs.next();)
        {
            done = false;
        }
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error getting component ->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return done;
    }

    public double getCalculatedMonthlyDepreciation(double cost, double nbv, double residue, int remainLife, 
            int totalLife, Date startDate, boolean isNew, double monthlyDepreFromAmAsset)
    {
        double calculatedDepreciation = 0.0D;
        if(isNew)
        {
            calculatedDepreciation = nbv / (double)remainLife;
        } else
        {
            calculatedDepreciation = monthlyDepreFromAmAsset;
        }
        String result = formata.formatAmount(calculatedDepreciation);
        result = result.replaceAll(",", "");
        calculatedDepreciation = Double.parseDouble(result);
        return calculatedDepreciation;
    }
    public double getCalculatedImprovMonthlyDepreciation(double improvnbv, int improvremainLife, 
            boolean isNew, double monthlyDepreFromAmAsset)
    {
        double calculatedDepreciation = 0.0D;
        if(isNew)
        {
            calculatedDepreciation = improvnbv / (double)improvremainLife;
        } else
        {
            calculatedDepreciation = monthlyDepreFromAmAsset;
        }
        String result = formata.formatAmount(calculatedDepreciation);
        result = result.replaceAll(",", "");
        calculatedDepreciation = Double.parseDouble(result);
        return calculatedDepreciation;
    }
    public String executeNbvResidualOld(String assetId,boolean isImprovement, int improvremainLife,double improvecost,double improvenbv,int frequency,double oldimprovAccumDep, double improvmonthlyDepreciation,String isImproved )
    {
        String output;
        Connection con;
        PreparedStatement ps,ps1,ps3,ps4,ps5;
        String query;
        String query2 = "";
        String query3 = "";
        String query4 = "";
    //    String query5 = "";
        //boolean setSucessful = false;
//        System.out.println("assetId in executeNbvResidual method >> "+assetId+"   <<<<isImproved: "+isImproved);
        double month = 0.0D;
        output = "fail";
        con = null;
        ps = null;  
        ps1 = null;
        ps3 = null;
        ps4 = null;   
 //       ps5 = null;
        query = (new StringBuilder()).append("UPDATE AM_ASSET SET  MONTHLY_DEP =").append(month).append(" ,NBV=RESIDUAL_VALUE WHERE ASSET_ID = ? ").toString();
        System.out.println(query);
        query2 = "UPDATE AM_ASSET  SET  IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + ? ,IMPROV_MONTHLYDEP = ? , IMPROV_NBV = IMPROV_C" +
        		"OST - (coalesce(IMPROV_ACCUMDEP,0) + ?),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + ? ,IMPROV_REMAINLIFE = IMPROV_REMAIN" +
        		"LIFE - ?,TOTAL_NBV = (NBV + (IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + ?)))  WHERE ASSET_ID = ? ";
//        query4 = "UPDATE AM_ASSET SET TOTAL_NBV = NBV+coalesce(IMPROV_NBV,0) WHERE ASSET_ID = ?";
        query3 = "UPDATE AM_ASSET SET IMPROV_MONTHLYDEP = 0.00 WHERE ASSET_ID = ?";
        //query5 = "UPDATE am_asset_improvement SET IMPROVED = 'N' WHERE ASSET_ID = '"+assetId+"'";
        try {  
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.setString(1, assetId);
        ps.executeUpdate();
        double monthlydep = improvenbv/improvremainLife;
        double zeroMonthlyDep = 0.00;
//        System.out.println("<<<<<<< Fully Depreciated improvremainLife>>>>>>>>> "+improvremainLife+"   <<<<<oldimprovAccumDep: "+oldimprovAccumDep+"    improvenbv: "+improvenbv+"    assetId: "+assetId+"    frequency: "+frequency+"  improvmonthlyDepreciation: "+improvmonthlyDepreciation+"  monthlydep: "+monthlydep);
    	if(improvremainLife !=0){
 //   		closeConnection(con, ps);  
    		double depreciationmonthly =  improvenbv/improvremainLife;
//    		System.out.println("<<<<<<<depreciationmonthly>>>>>>>>> "+depreciationmonthly);
    		ps1 = con.prepareStatement(query2);
//    		if(oldimprovAccumDep==0.00){
    		if(isImproved == "P"){
//    			System.out.println("<<<<<<<Is Improved improvmonthlyDepreciation>>>>>>>>> "+improvmonthlyDepreciation+"    <<<<<<<<<<<<<oldimprovAccumDep: "+oldimprovAccumDep);
    			ps1.setDouble(1, oldimprovAccumDep + improvmonthlyDepreciation);
    	//		ps1.setDouble(1, depreciationmonthly*frequency);
    			
    		}else{
  //  			System.out.println("<<<<<<<Is Not Improved >>>>>>>>> ");
    			ps1.setDouble(1, depreciationmonthly*frequency);
    //			ps1.setDouble(1, oldimprovAccumDep + improvmonthlyDepreciation);
    		}
    		
//    		if(oldimprovAccumDep==0.00){
    		if(isImproved == "P"){
    			ps1.setDouble(2, oldimprovAccumDep + improvmonthlyDepreciation);
    		//	ps1.setDouble(2, depreciationmonthly*frequency);
    		
    		}else{
    			ps1.setDouble(2, depreciationmonthly*frequency);
    		//	ps1.setDouble(2, oldimprovAccumDep + improvmonthlyDepreciation);
    		}
/*    		System.out.println("<<<<<<<<Frequency Difference1: "+(improvremainLife-frequency));
    		if((improvremainLife-frequency) < 0){
    			ps1.setDouble(2, zeroMonthlyDep);
    		}*/
    		if(isImproved == "P"){
    			ps1.setDouble(3, oldimprovAccumDep + improvmonthlyDepreciation);
    		//	ps1.setDouble(3, depreciationmonthly*frequency);
    		}else{
    			ps1.setDouble(3, depreciationmonthly*frequency);
    		//	ps1.setDouble(3, oldimprovAccumDep + improvmonthlyDepreciation);
    		}
    		ps1.setInt(4, frequency);
    		ps1.setInt(5, frequency);
/*            if(isImprovement)
            {
                ps1.setDouble(6, oldimprovAccumDep + improvmonthlyDepreciation);
            } else
            {
                ps1.setDouble(6, improvmonthlyDepreciation*frequency);
            }   */
            if(isImproved == "P"){
            	ps1.setDouble(6,  oldimprovAccumDep + improvmonthlyDepreciation);
           // 	ps1.setDouble(6, depreciationmonthly * frequency);
            }
            else{
            	ps1.setDouble(6, depreciationmonthly * frequency);
          //  	ps1.setDouble(6,  oldimprovAccumDep + improvmonthlyDepreciation);
             }            
    		ps1.setString(7, assetId);
    		ps1.executeUpdate();
    }
        setSucessful(true);
        output = "success";
     /*   
        if(output=="success"){
            ps4 = con.prepareStatement(query4);
            ps4.setString(1, assetId);
            ps4.executeUpdate();	
        }*/
      //  System.out.println("query to update am_asset_improvement: "+query5+"  <<<<<assetId: "+assetId+"  <<<<<isImproved: "+isImproved);
/*        if(isImproved.equalsIgnoreCase("P")){
   //     	System.out.println("Inside query loop: ");
		    ps5 = con.prepareStatement(query5);
		    ps5.executeUpdate();
	//	    System.out.println("Inside query 5: ");
	   	} */   
	   	if(improvremainLife ==0){
	    ps3 = con.prepareStatement(query3);
	    ps3.setString(1, assetId);
	    ps3.executeUpdate();
	   	}
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error executing update on NBV ->" +
                    ex.getMessage());
        } finally {
        	   closeConnection(con, ps);
        	   closeConnection(con, ps1);
        	   closeConnection(con, ps3);
        	   closeConnection(con, ps4);
 //       	   closeConnection(con, ps5);
        	   setSucessful(false);
        }
        return output;
    }

    public void executeFullDepreciated(AssetTransaction assetTran)
    {
        Connection con;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query;
        String query2;
        con = null;
        ps = null;
        ps2 = null;
        //query = "update am_asset set nbv = residual_value where asset_id = ? and monthly_dep > 0 and remaining_life = 0 ";
        query = "UPDATE AM_ASSET SET nbv = residual_value, Accum_Dep = (Accum_Dep+(Cost_Price-Accum_Dep))-Residual_Value WHERE asset_id = ? and remaining_life = 0 ";
        query2 = "UPDATE AM_ASSET SET TOTAL_NBV = residual_value WHERE Useful_Life = Total_Life AND TOTAL_NBV < 20.00 ";
        try {
        con = getConnection("legendPlus");  
        ps = con.prepareStatement(query);
        ps.setString(1, assetTran.getAssetId());
        ps.executeUpdate();
          
        ps2 = con.prepareStatement(query2);
        ps2.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error executing executeFullDepreciated ->" +
                    ex.getMessage());
        } finally {
        	   closeConnection(con, ps);
        	   closeConnection(con, ps2);
        	   setSucessful(false);
        }
    }
    
    public boolean getSapAMASSET_MONTHLY(){
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
  //      String query;
        boolean alreadySap;
        con = null;
        rs = null;   
        ps = null;    	
        alreadySap = false;
 //       System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
    	String Sapquery="delete from AM_ASSET_MONTHLY where asset_id is not null ";
        con = getConnection("legendPlus");
  //       System.out.println("Records Delete from file AM_ASSET_MONTHLY");
    //     String RecInsert = "";
        try {   
            ps = con.prepareStatement(Sapquery);
            ps.executeQuery(Sapquery);
       //     rs = stmt.executeQuery(sql);
            alreadySap = true;
        } catch (Exception ex) {  
        	alreadySap = false;
            System.out.println("WARNING: Error Sapping the am_asset_monthly->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    		return alreadySap;
        }    
    /*
    public boolean depdonesap()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
        System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
        String Sapquery="delete from AM_ASSET_MONTHLY where asset_id is not null ";
 //       System.out.println(Sapquery);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("legendPlus");
        stmt = con.createStatement();
        rs = stmt.executeQuery(Sapquery);
        //exists = true;
        exists = rs.next();
 //       System.out.println("=====Monthly Exists for Deletion=====> "+exists);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return exists;
    }
    */
    public boolean depdonesap()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
//        System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
        String qw = "delete from AM_ASSET_MONTHLY where asset_id is not null ";
        updateAssetStatusChange(qw);
 //       System.out.println(Sapquery);
        exists = true;
/*        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }*/
        return exists;
    }
 /*   
    public boolean depdoneInsert()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
 //       System.out.println("About to Insert records into file AM_ASSET_MONTHLY ");
      	String query="INSERT INTO [AM_ASSET_MONTHLY] SELECT * FROM [am_asset]  where asset_id is not null";
 //       System.out.println(query);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("legendPlus");
        stmt = con.createStatement();
        rs = stmt.executeQuery(query); 
        //exists = true;
        exists = rs.next();
 //       System.out.println("=====Exists for Monthly Insertion=====> "+exists);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return exists;
    } 
    */
    public boolean depdoneInsert()
    {
        boolean exists;
        System.out.println("About to Insert records into file AM_ASSET_MONTHLY ");
        String qw = "INSERT INTO [AM_ASSET_MONTHLY] SELECT * FROM [am_asset]  where asset_id is not null ";
        updateAssetStatusChange(qw);
 //       System.out.println(Sapquery);
        exists = true;
/*        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }*/
        return exists;
    }    
    public boolean Yeardonesap()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
        System.out.println("About to Delete records from file AM_ASSET_YEARLY");
        String Sapquery="delete from AM_ASSET_YEARLY where asset_id is not null ";
//        System.out.println(Sapquery);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("legendPlus");
        stmt = con.createStatement();
        rs = stmt.executeQuery(Sapquery);
        //exists = true;
        exists = rs.next();
 //       System.out.println("=====Yearly Exists for Deletion=====> "+exists);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return exists;
    }
/*
    public boolean YeardoneInsert()
    {
        String sql;
        boolean exists;  
        Connection con;
        Statement stmt;
        ResultSet rs;
        System.out.println("About to Insert records into file AM_ASSET_YEARLY ");
      	String query="INSERT INTO [AM_ASSET_YEARLY] SELECT * FROM [am_asset]  where asset_id is not null";
        System.out.println(query);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("legendPlus");
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        //exists = true;
        exists = rs.next();
 //       System.out.println("=====Exists for Yearly Insertion=====> "+exists);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return exists;
    }
    */
    public boolean YeardoneInsert()
    {
        boolean exists;
        System.out.println("About to Insert records into file AM_ASSET_YEARLY ");
        String qw = "INSERT INTO [AM_ASSET_YEARLY] SELECT * FROM [am_asset]  where asset_id is not null ";
        updateAssetStatusChange(qw);
 //       System.out.println(Sapquery);
        exists = true;
/*        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }*/
        return exists;
    }    
    
    public boolean depdoneInsertStamp(String deprecDate)
    {
        boolean exists;
        String depDay = deprecDate.substring(0,2);
        String depMonth = deprecDate.substring(3,5);
        String depYear = deprecDate.substring(6,10); 
//        System.out.println("Depreciation Date in depdoneInsertStamp: "+deprecDate);
        String closedate = formatDate(dateConvert(new Date()));
        String dDay = closedate.substring(0,2);
        String dMonth = closedate.substring(3,5);
        String dYear = closedate.substring(6,10); 
        closedate = dYear+"-"+dMonth+"-"+dDay;
//        System.out.println("Closing Date in depdoneInsertStamp: "+closedate);
        String qw = "INSERT INTO FIXED_ASSET_SCHEDULE_ARCHIVE SELECT Mtid,class_code,start_date,end_date," +
        		"class_name,cost_open_bal,cost_disposal,cost_additions,cost_additionsUpload,cost_additionsNoRateUpload,cost_additionsImprovement," +
        		"cost_WipReclas,cost_reclass,cost_Transfer," +
        		"dep_open_bal,dep_charge,dep_disposal,dep_reclass,improvement,nbv_open_bal,nbv_closing_bal," +
        "'"+depDay+"','"+depMonth+"','"+depYear+"','"+closedate+"'  " +
        " FROM fixed_asset_schedule WHERE MTID IS NOT NULL  ";
        updateAssetStatusChange(qw);
        String qz = "INSERT INTO FIXED_ASSET_SCHEDULE_TEMP_ARCHIVE SELECT Mtid,class_code,start_date,end_date," +
        		"class_name,cost_open_bal,cost_disposal,cost_additionsNewAsset,cost_additionsGroupAssets," +
        		"cost_additionsPartPayment,cost_additionsImprovement,cost_WipReclassification,cost_reclass," +
        		"dep_open_bal,dep_charge,dep_disposal,dep_reclass,cost_Transfer,improvement,nbv_open_bal," +
        		"nbv_closing_bal, " +
        		"'"+depDay+"','"+depMonth+"','"+depYear+"','"+closedate+"'  " +
        		" FROM fixed_asset_schedule_temp WHERE MTID IS NOT NULL ";
        updateAssetStatusChange(qz);
//        System.out.println(qw);
//        System.out.println(qz);
        exists = true;
        return exists;
    } 
    
    public boolean depdoneArchiveAssetsRegister(String deprecDate)
    {
        boolean exists;
        String depDay = deprecDate.substring(0,2);
        String depMonth = deprecDate.substring(3,5);
        String depYear = deprecDate.substring(6,10); 
//        System.out.println("Depreciation Date in depdoneInsertStamp: "+deprecDate);
        String closedate = formatDate(dateConvert(new Date()));
        String dDay = closedate.substring(0,2);
        String dMonth = closedate.substring(3,5);
        String dYear = closedate.substring(6,10); 
        closedate = dYear+"-"+dMonth+"-"+dDay;
//        System.out.println("Closing Date in depdoneInsertStamp: "+closedate);
        String qw = "INSERT INTO AM_ASSET_DEPRECIATION_ARCHIVE SELECT Asset_id,Registration_No,REGION_CODE,ZONE_CODE,Branch_ID,Dept_ID,Category_ID,Section_id,Description,Vendor_AC," +
        		"Date_purchased,Dep_Rate,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_User,Asset_Maintenance," +
        		"Accum_Dep,Monthly_Dep,Cost_Price,NBV,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution," +
        		"Posting_Date,Effective_Date,Purchase_Reason,Useful_Life,Total_Life,Location,Remaining_Life,Vatable_Cost,Vat," +
        		"Req_Depreciation,Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry,Dep_Ytd,Section,Asset_Status,State," +
        		"Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date," +
        		"BRANCH_CODE, SECTION_CODE,DEPT_CODE,CATEGORY_CODE,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE," +
        		"LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,system_ip,mac_address,asset_code," +
        		"memo,memovalue,Date_Closed,REVALUE_COST,INTEGRIFY,IMPROV_COST,IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV," +
        		"IMPROV_VATABLECOST,TOTAL_NBV,IMPROV_USEFULLIFE,IMPROV_TOTALLIFE,IMPROV_REMAINLIFE,IMPROV_EffectiveDate,IMPROV_EndDate," +
        		"SUB_CATEGORY_ID,SUB_CATEGORY_CODE,SPARE_3,SPARE_4,SPARE_5,SPARE_6,PROJECT_CODE,WAREHOUSE_CODE,ITEM_CODE,ITEMTYPE,QUANTITY," +
        		"UNIT_CODE,PARENT_TAG,INITIATEDBRANCHCODE,FREQUENCY," +
        "'"+depDay+"','"+depMonth+"','"+depYear+"','"+closedate+"'  " +
        " FROM AM_ASSET WHERE ASSET_ID IS NOT NULL  ";
        updateAssetStatusChange(qw);
//        System.out.println(qw);
        exists = true;
        return exists;
    } 


    public void NewAssetDepreciation(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate, double thresholdcost,String nextProcessingDate)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;
        ps = null;
        //"UPDATE AM_ASSET SET Monthly_Dep = ((NBV/Remaining_Life)*1), " +
        query = "UPDATE AM_ASSET SET Monthly_Dep = ((NBV/Remaining_Life)*1)*(SELECT DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1), " +
        		 "NBV = NBV - ((NBV/Remaining_Life)*(SELECT DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1)*"+frequency+"), " +
        		 "Useful_Life = Useful_Life + (SELECT (DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1)*"+frequency+"), " +
        		"Remaining_Life = Remaining_Life - (SELECT DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1)*"+frequency+", " +
        		"Accum_Dep = ((NBV/Remaining_Life)*1)*(SELECT DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1),"+
        		"last_dep_date = '"+nextdate+"', Frequency = (SELECT DATEDIFF(MONTH,Effective_Date,'"+endProcessDate+"')+1) "+
                " WHERE Accum_Dep = 0.00 AND EFFECTIVE_DATE BETWEEN EFFECTIVE_DATE AND DEP_END_DATE AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' " +
                "AND (SELECT (DATEDIFF(MONTH,EFFECTIVE_DATE,DEP_END_DATE))) > -1 " +
                "AND TOTAL_LIFE = REMAINING_LIFE AND COST_PRICE > "+thresholdcost+" AND ACCUM_DEP = 0.00 AND REMAINING_LIFE <> 0 AND Dep_Rate > 0.00 AND Effective_Date < '"+endProcessDate+"'" ;
//                "AND EFFECTIVE_DATE BETWEEN SUBSTRING(CONVERT(varchar, '"+nextProcessingDate+"', 23),1,8)+'01' and SUBSTRING(CONVERT(varchar, '"+nextProcessingDate+"', 23),1,7)+'-'+SUBSTRING(CONVERT(varchar, EOMONTH('"+nextProcessingDate+"'), 23),9,2)";
         
//        System.out.println("<<<<<<query in NewAssetDepreciation: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing New Asset Depreciaon in NewAssetDepreciation ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }

    public void RecalculationOfNewAssetDepreciationEndDate(String FirstDayProcessDate, String endProcessDate, double thresholdcost)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps1;
        PreparedStatement ps2;
        String query;
        con = null;
        rs = null;
        ps = null;
        ps1 = null;
        ps2 = null;
        //"UPDATE AM_ASSET SET Monthly_Dep = ((NBV/Remaining_Life)*1), " +
        query = "UPDATE AM_ASSET SET DEP_END_DATE = (SELECT DATEADD(month, TOTAL_LIFE, EFFECTIVE_DATE)) WHERE Accum_Dep = 0.00 AND EFFECTIVE_DATE BETWEEN EFFECTIVE_DATE AND DEP_END_DATE AND Req_Depreciation = 'Y' AND ASSET_STATUS != 'REJECTED' "
        		+ "AND (SELECT (DATEDIFF(MONTH,EFFECTIVE_DATE,DEP_END_DATE))) > -1 "
        		+ "AND TOTAL_LIFE = REMAINING_LIFE AND COST_PRICE > "+thresholdcost+" AND ACCUM_DEP = 0.00 AND REMAINING_LIFE <> 0 AND Dep_Rate > 0.00 AND Posting_Date between '"+FirstDayProcessDate+"' AND '"+endProcessDate+"' " ;

      String  query2 = "UPDATE a SET DEP_END_DATE = (SELECT DATEADD(month, b.TOTAL_LIFE, b.EFFECTIVE_DATE)) FROM AM_GROUP_ASSET a, AM_ASSET b WHERE a.Asset_id = b.Asset_id and b.Accum_Dep = 0.00 AND b.EFFECTIVE_DATE BETWEEN b.EFFECTIVE_DATE AND b.DEP_END_DATE AND b.Req_Depreciation = 'Y' AND b.ASSET_STATUS != 'REJECTED' "
        		+ "AND (SELECT (DATEDIFF(MONTH,b.EFFECTIVE_DATE,b.DEP_END_DATE))) > -1 "
        		+ "AND b.TOTAL_LIFE = b.REMAINING_LIFE AND b.COST_PRICE > "+thresholdcost+" AND b.ACCUM_DEP = 0.00 AND b.REMAINING_LIFE <> 0 AND b.Dep_Rate > 0.00 AND a.Posting_Date between '"+FirstDayProcessDate+"' AND '"+endProcessDate+"' " ;

      String  wrongDepRatequery= "update a set a.Dep_Rate = c.Dep_rate,a.DEPT_CODE = d.Dept_code, DEP_END_DATE = (SELECT DATEADD(month, TOTAL_LIFE, EFFECTIVE_DATE)) "
      		+ "from am_asset a, am_ad_category c,am_ad_department d where a.Category_ID = c.category_ID and a.Dept_ID = d.Dept_ID "
      		+ "and a.Dep_Rate = 0.00 and c.Dep_rate != 0.0 and a.Asset_Status = 'ACTIVE' " ;
      
//      String query3 = "UPDATE am_asset_improvement SET IMPROVED = 'P' WHERE IMPROVED = 'Y' and IMPROV_USEFULLIFE = 0 and revalue_Date between '"+FirstDayProcessDate+"' AND '"+endProcessDate+"' ";
      
//        System.out.println("<<<<<<query in RecalculationOfNewAssetDepreciation: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(wrongDepRatequery);
        ps.execute();
        ps1 = con.prepareStatement(query);
        ps1.execute();        
        ps2 = con.prepareStatement(query2);
        ps2.execute();  
//        ps3 = con.prepareStatement(query3);
//        ps3.execute();            
    } catch (Exception ex) {  
        System.out.println("WARN: Computing New Asset Depreciaon in RecalculationOfNewAssetDepreciation ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps1, rs);
        closeConnection(con, ps2, rs);
    }        
    }

    public void NewAssetDepreciationLess()
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;  
        ps = null;
        query = "update am_asset set Accum_Dep = Cost_Price - Residual_Value,Monthly_Dep = 0.00,nbv = Residual_Value,"+
        		"TOTAL_NBV = Residual_Value, Useful_Life = Total_Life, Remaining_Life = 0 where Remaining_Life < 0";
//        System.out.println("<<<<<<query in AdditionOfNBVBalanceToIMPROV_NBV: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN:  NBV in NewAssetDepreciationLess ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }
    public void OldAssetDepreciation(String depreciationProcessDate, String endProcessDate, int frequency,String nextdate)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps1;
        PreparedStatement ps2;
        String query;
        String query2;
        String monthlyDepquery;
        con = null;
        rs = null;  
        ps = null;
        ps1 = null;
        ps2 = null;
//      System.out.println("monthlyDepquery in OldAssetDepreciation to recalculate Asset monthly Depreciations: "+monthlyDepquery);
//        System.out.println("FirstDayProcessDate in OldAssetDepreciation: "+depreciationProcessDate+"  endProcessDate: "+endProcessDate+"  frequency: "+frequency);
        query = "UPDATE AM_ASSET SET Accum_Dep = Accum_Dep + ((NBV/Remaining_Life)*"+frequency+"),Monthly_Dep = ((NBV/Remaining_Life)*"+frequency+"), " +
        		 " NBV = NBV - ((NBV/Remaining_Life)*"+frequency+"),Useful_Life = Useful_Life + "+frequency+", " +
        		"Remaining_Life = Remaining_Life - "+frequency+", " +
        		"last_dep_date = '"+depreciationProcessDate+"' "+
                " WHERE Accum_dep > 0.00 AND DEP_RATE > 0.00 AND DEP_END_DATE > '"+depreciationProcessDate+"' AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' " +
                " AND NBV > 10  AND Monthly_Dep > 0.00 AND REMAINING_LIFE <> 0";
//        System.out.println("query in OldAssetDepreciation: "+query);
        query2 = "UPDATE AM_ASSET SET Accum_Dep = Accum_Dep + ((NBV/Remaining_Life)*"+frequency+"), Monthly_Dep = (NBV/Remaining_Life)*"+frequency+", " +
       		 "NBV = NBV - ((NBV/Remaining_Life)*"+frequency+"),Useful_Life = Useful_Life + "+frequency+", " +
       		"Remaining_Life = Remaining_Life - "+frequency+", " +
       		"last_dep_date = '"+depreciationProcessDate+"' "+
               " WHERE Accum_dep > 0.00 AND DEP_RATE > 0.00 AND DEP_END_DATE > '"+depreciationProcessDate+"' AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' " +
               " AND NBV > 10  AND Monthly_Dep = 0.00 AND REMAINING_LIFE > 0";
//       System.out.println("query2 in OldAssetDepreciation to recalculate Asset Improved: "+query2);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();          
        ps1 = con.prepareStatement(query2);
        ps1.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing Old Asset Depreciaon in OldAssetDepreciation ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps1, rs);
    }        
    }


    public void executeNbvResidual(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate,String lastProcessingDate)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps1;
        PreparedStatement ps2;
        PreparedStatement ps3;
        PreparedStatement ps4;
        String query;
        String query1;
        String query2;
        String query3;
        String query4;
        con = null;
        rs = null;
        ps = null;
        ps1 = null;
        ps2 = null;
        ps3 = null;
        ps4 = null;
        query = "UPDATE Am_Improvement_Depreciation SET IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + IMPROV_NBV/(IMPROV_REMAINLIFE),IMPROV_MONTHLYDEP = IMPROV_NBV/(IMPROV_REMAINLIFE),  " +
        		 "IMPROV_NBV = IMPROV_NBV - IMPROV_NBV/(IMPROV_REMAINLIFE),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + "+frequency+",  " +
                "IMPROV_REMAINLIFE = IMPROV_REMAINLIFE - "+frequency+",TOTAL_NBV = (IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + IMPROV_NBV/(IMPROV_REMAINLIFE))),  " +
                "last_dep_date = '"+lastProcessingDate+"' "+
                "WHERE IMPROV_NBV > 0.00 AND IMPROV_REMAINLIFE > 0 AND ASSET_STATUS = 'ACTIVE' ";

        query1 = "UPDATE Am_Improvement_Depreciation SET IMPROV_COST = COST_INCREASE, IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + COST_INCREASE/(IMPROV_REMAINLIFE),IMPROV_MONTHLYDEP = COST_INCREASE/(IMPROV_REMAINLIFE), " +
       		 "IMPROV_NBV = COST_INCREASE - (coalesce(IMPROV_ACCUMDEP,0) + COST_INCREASE/(IMPROV_REMAINLIFE)),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + "+frequency+", " +
               "IMPROV_REMAINLIFE = IMPROV_REMAINLIFE - "+frequency+",TOTAL_NBV = (COST_INCREASE - (coalesce(IMPROV_ACCUMDEP,0) + COST_INCREASE/(IMPROV_REMAINLIFE))), " +
               "last_dep_date = '"+lastProcessingDate+"' "+
               "WHERE POSTING_DATE BETWEEN '"+FirstDayProcessDate+"' AND '"+nextdate+"' AND coalesce(IMPROV_NBV,0) = 0.00 AND IMPROV_REMAINLIFE > 0 AND ASSET_STATUS = 'ACTIVE' ";        

        query2 = "UPDATE AM_ASSET SET IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + IMPROV_NBV/(IMPROV_REMAINLIFE),IMPROV_MONTHLYDEP = IMPROV_NBV/(IMPROV_REMAINLIFE), " +
       		 "IMPROV_NBV = IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + IMPROV_NBV/(IMPROV_REMAINLIFE)),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + "+frequency+", " +
               "  IMPROV_REMAINLIFE = IMPROV_REMAINLIFE - "+frequency+",TOTAL_NBV = (NBV + (IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + IMPROV_NBV/(IMPROV_REMAINLIFE)))), " +
               "last_dep_date = '"+lastProcessingDate+"' "+
               "WHERE Asset_id not in (select Asset_id from Am_Improvement_Depreciation)  and IMPROV_NBV > 0.00 AND IMPROV_REMAINLIFE > 0 AND ASSET_STATUS = 'ACTIVE'  ";        
        
        query3 = "update a SET a.IMPROV_COST = b.IMPROV_COST,a.IMPROV_MONTHLYDEP = b.IMPROV_MONTHLYDEP, "+
        		 "a.IMPROV_ACCUMDEP = b.IMPROV_ACCUMDEP,a.IMPROV_NBV = b.IMPROV_NBV,a.TOTAL_NBV = a.TOTAL_NBV+b.TOTAL_NBV, "+
        		 "last_dep_date = '"+lastProcessingDate+"' "+
        		 "from AM_ASSET a, Asset_Improvement_Depreciation b where a.Asset_Id = b.Asset_Id and b.IMPROV_COST != b.IMPROV_ACCUMDEP ";
        
        query4 = "UPDATE AM_ASSET SET NBV = RESIDUAL_VALUE WHERE NBV < RESIDUAL_VALUE AND ASSET_STATUS = 'ACTIVE' ";
//       System.out.println("query in executeNbvResidual: "+query);
//        System.out.println("query1 in executeNbvResidual: "+query1);
//        System.out.println("query2 in executeNbvResidual: "+query2);
//        System.out.println("query3 in executeNbvResidual: "+query3);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
        ps1 = con.prepareStatement(query1);
        ps1.execute();
        ps2 = con.prepareStatement(query2);
        ps2.execute();
        ps3 = con.prepareStatement(query3);
        ps3.execute();
        ps4 = con.prepareStatement(query4);
        ps4.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Computing Fully Depreciated Asset Depreciaon  in executeNbvResidual ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps1, rs);
        closeConnection(con, ps2, rs);
        closeConnection(con, ps3, rs);
        closeConnection(con, ps4, rs);
    }        
    }

    public void removeCarriageandSpace()
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String query;
        con = null;
        rs = null;  
        ps = null;
        query = "update am_Asset set description = substring(description,len(substring(description,1,2))+1,len(description)) where ASCII(LEFT(description, 1)) = 10" ;
   
  //      System.out.println("<<<<<<query in removeCarriageandSpace: "+query);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Removing Carriage and Spaces from Asset Descriptions in removeCarriageandSpace ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    }        
    }


    public void FullyDepreciated(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        String query; 
        String query2;
        String query3;
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        ps3 = null;
        query = "UPDATE AM_ASSET SET MONTHLY_DEP = 0.00, NBV = 10 WHERE REMAINING_LIFE = 0 ";
        query2 = "UPDATE AM_ASSET SET IMPROV_MONTHLYDEP = 0.00 WHERE IMPROV_ACCUMDEP = IMPROV_COST and IMPROV_MONTHLYDEP > 0 ";
        query3 = "UPDATE Am_Improvement_Depreciation SET IMPROV_MONTHLYDEP = 0.00 WHERE IMPROV_ACCUMDEP = IMPROV_COST and IMPROV_MONTHLYDEP > 0 ";
//        System.out.println("query in FullyDepreciated: "+query);
//        System.out.println("query2 in FullyDepreciated Improved Assets: "+query2);
//        System.out.println("query3 in FullyDepreciated Improved Assets: "+query3);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
        ps2 = con.prepareStatement(query2);
        ps2.execute();
        ps3 = con.prepareStatement(query3);
        ps3.execute();        
    } catch (Exception ex) {  
        System.out.println("WARN: Fully Depreciated Asset Depreciaon  in FullyDepreciated ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
        closeConnection(con, ps3, rs);
    }        
    }
    
    public void AssetReclassifiedWithOldRateGreaterThanNewRate(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        String query2;
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "UPDATE a set a.Accum_Dep = (a.Accum_Dep+b.new_monthly_dep)-Residual_Value,a.monthly_dep=b.new_monthly_dep, a.NBV = a.Residual_Value FROM AM_ASSET a,am_assetReclassification b WHERE a.Asset_id = b.new_asset_id AND a.REMAINING_LIFE = 0 and b.new_remaining_life = 0 "
        		+ "and reclassify_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0))))";
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Asset Reclassified With Old Rate Greater Than New Rate and New Remaining Life is Zero ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }
    
    public void OldAssetDepreciationNBVLessMonthly()
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "update am_asset set Useful_Life = Total_Life,Monthly_Dep = NBV,nbv= Residual_Value where Remaining_Life = 0 and (Total_Life - Useful_Life) = 1  and nbv < Monthly_Dep ";
//        System.out.println("query in FullyDepreciated: "+query);        
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Fully Depreciated Asset Depreciaon With REMAINING_LIFE Zeros  in FullyDepreciated ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }

    public void AssetDepreciationWithNBVZeros()
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "update am_asset set nbv = Residual_Value where nbv = 0.00";
//        System.out.println("query in FullyDepreciated: "+query);        
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Fully Depreciated Asset Depreciaon with zero NBV ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }

    public void AssetDepreciationWithMonthlyEqualsResidual()
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "update am_asset set Monthly_Dep = 0.00 where nbv = Residual_Value and Monthly_Dep = Residual_Value";
//        System.out.println("query in FullyDepreciated: "+query);        
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Fully Depreciated Asset Depreciaon with zero NBV ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }
    

    public void AssetReclassificationWithNegativeRemainingLife()
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        String query2;
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "update a set a.new_remaining_life=0, a.Monthly_Dep=old_nbv,a.new_monthly_dep = old_nbv from am_assetReclassification a, AM_ASSET b where a.new_asset_id = b.Asset_id and new_remaining_life < 0 ";
        query2 = "update b set b.Monthly_Dep=a.old_nbv-Residual_Value,Accum_Dep=Residual_Value from am_assetReclassification a, AM_ASSET b where a.new_asset_id = b.Asset_id and new_remaining_life < 0 ";
//        System.out.println("query in FullyDepreciated: "+query);        
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
        ps2 = con.prepareStatement(query2);
        ps2.execute();        
    } catch (Exception ex) {  
        System.out.println("WARN: Recalssification Correction for Negative Remaining Life ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }
    
    public void BackDateDepreciated(String FirstDayProcessDate, String endProcessDate, int frequency,String nextdate)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        String query; 
        String query2;
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        query = "UPDATE AM_ASSET SET Monthly_Dep = Accum_Dep/FREQUENCY WHERE FREQUENCY > 1 ";
        query2 = "UPDATE AM_ASSET SET FREQUENCY = NULL WHERE FREQUENCY > 0 ";
//        System.out.println("query in FullyDepreciated: "+query);
//        System.out.println("query in FullyDepreciated Improved Assets: "+query2);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
        ps2 = con.prepareStatement(query2);
        ps2.execute();
    } catch (Exception ex) {  
        System.out.println("WARN: Backdating of Assets  in BackDateDepreciated ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
    }        
    }

    public void logDeprecitionTransaction(String depreciationProcessDate, String endProcessDate, int frequency, String userid)
    {
        String newStartDate;
        String newEndDate;
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        String query;
        String query2;
        String query3;
        con = null;
        rs = null;
        ps = null;
        ps2 = null;
        ps3 = null;
        		
//        query = "INSERT INTO monthly_depreciation_processing (ASSET_ID,MONTHLY_DEP,TRAN_DATE, DEP_DATE,userid,no_of_month) " +
//        		" SELECT ASSET_ID, IMPROV_MONTHLYDEP, '"+endProcessDate+"','"+depreciationProcessDate+"',"+userid+","+frequency+" FROM AM_ASSET " +
//                " WHERE IMPROV_NBV <> 0.00 AND IMPROV_MONTHLYDEP <> 0.00  AND ASSET_STATUS = 'ACTIVE'";
//        
		
		query = "INSERT INTO monthly_depreciation_processing (ASSET_ID,MONTHLY_DEP,TRAN_DATE, DEP_DATE,userid,no_of_month) " +
			" SELECT ASSET_ID, IMPROV_MONTHLYDEP, '"+endProcessDate+"','"+depreciationProcessDate+"',"+userid+","+frequency+" FROM AM_ASSET " +
		    " WHERE IMPROV_MONTHLYDEP <> 0.00  AND ASSET_STATUS = 'ACTIVE'";

        query2 = "INSERT INTO monthly_depreciation_processing (ASSET_ID,MONTHLY_DEP,TRAN_DATE, DEP_DATE,userid,no_of_month) " +
        		" SELECT ASSET_ID, Monthly_Dep, '"+endProcessDate+"','"+depreciationProcessDate+"',"+userid+","+frequency+"  FROM AM_ASSET " +
                " WHERE Monthly_Dep <> 0.00 AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' ";
//        System.out.println("query in logDeprecitionTransaction: "+query);
//        System.out.println("query2 in logDeprecitionTransaction: "+query2);
        query3 = "UPDATE a SET a.BRANCH_CODE = b.BRANCH_CODE, a.CATEGORY_CODE = b.CATEGORY_CODE FROM monthly_depreciation_processing a, " +
        		 "AM_ASSET b WHERE a.asset_id = b.Asset_id and a.BRANCH_CODE is null";
        try { 
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps.execute();
        ps2 = con.prepareStatement(query2);
        ps2.execute();
        ps3 = con.prepareStatement(query3);
        ps3.execute();        
    } catch (Exception ex) {  
        System.out.println("WARN: Computing New Asset Depreciaon in logDeprecitionTransaction for Legend Plus ->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
        closeConnection(con, ps2, rs);
        closeConnection(con, ps3, rs);
    }        
    }

    public void notifyLastDepAsset(String depreciationProcessDate, String endProcessDate, int frequency, String userid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE a SET a.last_dep_date = b.dep_date FROM AM_ASSET a  " +
        		" INNER JOIN monthly_depreciation_processing b ON a.Asset_id = b.asset_id " +
                " WHERE b.dep_date = '"+depreciationProcessDate+"' ";
//        System.out.println("NOTIFY_QUERY in notifyLastDepAsset: "+NOTIFY_QUERY);
        try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(NOTIFY_QUERY);
        ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error notifying processed asset for Legend Plus -->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

public void updateAssetStatusChange(String query_r){
//	System.out.println("======query_r in updateAssetStatusChange: "+query_r);
Connection con = null;
        PreparedStatement ps = null;
try {
	con = getConnection("legendPlus");
ps = con.prepareStatement(query_r);
           int i =ps.executeUpdate();
        } catch (Exception ex) {

            System.out.println("DepreciationLegendPlusProcessingManager: updateAssetStatusChange()>>>>>" + ex);
        } finally {
        	closeConnection(con, ps);
        }
}
        

public void DisableUsers (String procdate)
{

    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    PreparedStatement ps1;
    PreparedStatement ps2;
    String query;
    String insertquery;
    String deletequery;
    con = null;
    rs = null; 
    ps = null;  
    ps1 = null;  
    ps2 = null;  
//    System.out.println("FirstDayProcessDate in procdate: "+procdate);
    deletequery = "delete from am_gb_classEnable ";
    query = "UPDATE b SET b.class = a.DefaultClass_Id FROM am_gb_classDisable a,am_gb_user b " +
            "WHERE a.Class_Id = b.Class AND a.class_status = 'Y'";
    insertquery = "insert into am_gb_classEnable(class_id,class_desc,class_name,User_Id,class_status,create_date) " +
            "select a.class_id,a.class_desc,a.class_name,b.User_Id,'N',? from am_gb_classDisable a " +
    		"INNER JOIN am_gb_user b ON b.Class = a.class_id " +
    		"where a.class_status = 'Y'";    
//    System.out.println("query in OldAssetDepreciation: "+query+"      procdate: "+procdate);
    try {
    con = getConnection("legendPlus");
    ps = con.prepareStatement(deletequery);
    ps.execute();
    ps1 = con.prepareStatement(insertquery);
    ps1.setDate(1, df.dateConvert(procdate));
    ps1.execute();
    ps2 = con.prepareStatement(query);
    ps2.execute();

} catch (Exception ex) {  
    System.out.println("WARN: Disable User ->" +
            ex.getMessage());
} finally {
    closeConnection(con, ps, rs);
    closeConnection(con, ps1, rs);
    closeConnection(con, ps2, rs);
}        
}

public void monthlyAssetDepreciation(String depreciationProcessDate, String endProcessDate, int frequency,String nextdate)
{
    String newStartDate;
    String newEndDate;
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    PreparedStatement ps2;
    String query;
    String query2;
    con = null;
    rs = null;
    ps = null;
    ps2 = null;
    System.out.println("FirstDayProcessDate in monthlyAssetDepreciation: "+depreciationProcessDate+"  endProcessDate: "+endProcessDate+"  frequency: "+frequency);
    query = "UPDATE AM_ASSET SET Monthly_Dep = NBV/Remaining_Life"
    		+ " WHERE Accum_dep > 0.00 AND DEP_RATE > 0.00 AND DEP_END_DATE > '"+depreciationProcessDate+"' AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' " +
            " AND NBV > 10  AND Monthly_Dep > 0.00 AND REMAINING_LIFE <> 0";
//    System.out.println("query in OldAssetDepreciation: "+query);

    try {
    con = getConnection("legendPlus");
    ps = con.prepareStatement(query);
    ps.execute();
} catch (Exception ex) {  
    System.out.println("WARN: Computing Old Asset Depreciaon in OldAssetDepreciation ->" +
            ex.getMessage());
} finally {
    closeConnection(con, ps, rs);
    closeConnection(con, ps2, rs);
}        
}

public boolean extractMonthlyImprovementSingleTransactions(String startDate, String endDate)
{
    boolean exists;
    System.out.println("About to Insert Improved Asset records that have NBV Ten(10) naira in the file Am_Improvement_Depreciation ");
    String qw = "insert into Am_Improvement_Depreciation(BRANCH_CODE,CATEGORY_CODE,Asset_id,Description,cost_increase,vatable_cost,vat_amount,wht_amount,Posting_Date,LPO,"+
    		 "IMPROV_TOTALLIFE,IMPROV_REMAINLIFE,IMPROV_EffectiveDate,ASSET_STATUS,IMPROVED) "+
    		 "select branch_code,category_code, Asset_id,Description,cost_increase,vatable_cost,vat_amount,wht_amount,revalue_Date,lpoNum,"+
    		 "IMPROV_USEFULLIFE,IMPROV_USEFULLIFE,effDate,'ACTIVE',IMPROVED from am_asset_improvement where IMPROVED = 'Y' AND APPROVAL_STATUS = 'POSTED' "+
    		 "and IMPROV_USEFULLIFE > 0 and revalue_Date between '"+startDate+"' and '"+endDate+"' ";
    updateAssetStatusChange(qw);
    String query5 = "UPDATE am_asset_improvement SET IMPROVED = 'P' WHERE IMPROVED = 'Y' AND APPROVAL_STATUS = 'POSTED' and IMPROV_USEFULLIFE > 0 and revalue_Date between '"+startDate+"' and '"+endDate+"' ";
    updateAssetStatusChange(query5);
//       System.out.println("qw in extractMonthlyImprovementSingleTransactions: "+qw);
    exists = true;
    return exists;
}    

public boolean extractMonthlyUploadImprovementTransactions(String startDate, String endDate)
{
    boolean exists;
    System.out.println("About to Insert Improved Asset records have NBV Ten(10) naira into file Am_Improvement_Depreciation ");
    String qw = "insert into Am_Improvement_Depreciation(BRANCH_CODE,CATEGORY_CODE,Asset_id,Description,cost_increase,vatable_cost,vat_amount,wht_amount,Posting_Date,LPO,"+
    		 "IMPROV_COST,IMPROV_NBV,"+
    		 "IMPROV_TOTALLIFE,IMPROV_REMAINLIFE,IMPROV_EffectiveDate,ASSET_STATUS,IMPROVED) "+
    		 "select branch_code,category_code, Asset_id,Description,cost_increase,vatable_cost,vat_amount,wht_amount,revalue_Date,lpoNum,"+
    		 "cost_increase,cost_increase,"+
    		 "IMPROV_USEFULLIFE,IMPROV_USEFULLIFE,effDate,'ACTIVE',IMPROVED from am_asset_improvement_Upload where IMPROVED = 'Y' AND APPROVAL_STATUS = 'POSTED' "+
    		 "and IMPROV_USEFULLIFE > 0 and revalue_Date between '"+startDate+"' and '"+endDate+"' ";
    updateAssetStatusChange(qw);
    String query5 = "UPDATE am_asset_improvement_Upload SET IMPROVED = 'P' WHERE IMPROVED = 'Y' AND APPROVAL_STATUS = 'ACTIVE' and IMPROV_USEFULLIFE > 0 and revalue_Date between '"+startDate+"' and '"+endDate+"' ";
    updateAssetStatusChange(query5);
//       System.out.println("qw in extractMonthlyUploadImprovementTransactions: "+qw);
    exists = true;
    return exists;
}    


public void clearDepreciationLegacyExport()
{
    Connection con;
    PreparedStatement ps;
    String CLEAR_QUERY;
    con = null;
    ps = null;
    CLEAR_QUERY = "DELETE FROM FINACLE_EXT ";
    try {
    con = getConnection("legendPlus");
    ps = con.prepareStatement(CLEAR_QUERY);
    ps.execute();
    } catch (Exception ex) {
        System.out.println(
                "WARNING: Error clearDepreciationLegacyExport  --> " + 
                ex.getMessage());
    } finally {
        closeConnection(con, ps);
    }

}
public boolean assetAdditionsSystemRejection()
{
    boolean exists;
    System.out.println("About to Insert records into file AM_ASSETADDITIONS ");
    String qw = "INSERT INTO AM_ASSETADDITIONS select * from am_asset where Posting_Date between (SELECT DATEADD(dd, -( DAY( (SELECT processing_date FROM dbo.am_gb_company) ) -1 ), (SELECT processing_date FROM dbo.am_gb_company))) and (SELECT DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,(SELECT processing_date FROM dbo.am_gb_company))+1,0))) and Asset_Status = 'ACTIVE' ";
    updateAssetStatusChange(qw);
//       System.out.println(Sapquery);
    exists = true;
/*        try {
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        closeConnection(con, stmt, rs);
    }*/
    return exists;
}  

//public boolean pendingTrancations() throws Exception {
public boolean pendingTrancations(){
	boolean exists;
	System.out.println("About to Remove Pending Transactions Before Depreciation for the new month ");
	//AssetRecordsBean arb = new AssetRecordsBean();
    String queryBooking= "update a set a.Asset_Status = 'REJECTED-SYSTEM' from am_asset a, am_asset_approval b where a.asset_id = b.Asset_id "
    		+ "and a.Posting_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.Asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Creation'";
    updateAssetStatusChange(queryBooking);
    String queryBooking1= "update a set a.asset_status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, am_asset b where a.asset_id = b.Asset_id "
    		+ "and b.Posting_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.Asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Creation'";
    updateAssetStatusChange(queryBooking1);	
    String queryGrpAsset= "update a set a.Asset_Status = 'REJECTED-SYSTEM' from AM_GROUP_ASSET a, am_asset_approval b where CONVERT(VARCHAR,a.GROUP_ID) = b.Asset_id and a.Posting_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Group Asset Creation' ";
    updateAssetStatusChange(queryGrpAsset);
    String queryGrpAsset2= "update a set a.Asset_Status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, AM_GROUP_ASSET b where a.Asset_id = CONVERT(VARCHAR,b.GROUP_ID) and b.Posting_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Group Asset Creation' ";
    updateAssetStatusChange(queryGrpAsset2);    
    String query= "update a set a.approval_status = 'REJECTED-SYSTEM' from am_asset_improvement a, am_asset_approval b where a.asset_id = b.Asset_id "
    		+ "and a.revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.approval_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Improvement'";
    updateAssetStatusChange(query);
    String query1= "update a set a.asset_status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, am_asset_improvement b where a.asset_id = b.Asset_id "
    		+ "and b.revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.approval_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Improvement'";
    updateAssetStatusChange(query1);
    String query2= "update a set a.approval_status = 'R' from AM_GROUP_IMPROVEMENT a, am_asset_approval b where CONVERT(VARCHAR,a.Revalue_ID) = b.Asset_id and a.revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Improve Upload' ";
    updateAssetStatusChange(query2);
    String query3= "update a set a.Asset_Status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, AM_GROUP_IMPROVEMENT b where a.Asset_id = CONVERT(VARCHAR,b.Revalue_ID) and b.revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Improve Upload' ";
    updateAssetStatusChange(query3);
    String query4= "update a set a.Asset_Status = 'REJECT-SYSTEM' from am_assetUpdate a, am_asset_approval b where a.asset_id = b.Asset_id and b.posting_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'CloseAsset' ";
    updateAssetStatusChange(query4);
    String query5= "update a set a.Asset_Status = 'REJECT-SYSTEM',process_status = 'R' from am_asset_approval a, am_assetUpdate b where a.asset_id = b.Asset_id and b.posting_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'CloseAsset' ";
    updateAssetStatusChange(query5);
    String query6= "update a set a.Asset_Status = 'ACTIVE' from am_asset a, am_asset_approval b where a.asset_id = b.Asset_id and b.posting_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'CloseAsset' ";
    updateAssetStatusChange(query6);
    String query7= "update a set a.disposal_status = 'R' from am_AssetDisposal a, am_asset_approval b where a.asset_id = b.Asset_id and a.Disposal_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Disposal' ";
    updateAssetStatusChange(query7);
    String query8= "update a set a.Asset_Status = 'REJECT-SYSTEM',process_status = 'R' from am_asset_approval a, am_AssetDisposal b where a.asset_id = b.Asset_id and b.Disposal_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Disposal' ";
    updateAssetStatusChange(query8);
    String query9= "update a set a.approval_status = 'R' from AM_GROUP_DISPOSAL a, am_asset_approval b where CONVERT(VARCHAR,a.disposal_ID) = b.Asset_id and a.Disposal_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Disposal Upload' ";
    updateAssetStatusChange(query9);
    String query10= "update a set a.Asset_Status = 'REJECT-SYSTEM',process_status = 'R' from am_asset_approval a, AM_GROUP_DISPOSAL b where a.Asset_id = CONVERT(VARCHAR,b.disposal_ID) and b.Disposal_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Disposal Upload' ";
    updateAssetStatusChange(query10);
    String query11= "update a set a.status = 'REJECT-SYSTEM' from am_assetReclassification a, am_asset_approval b where a.asset_id = b.Asset_id and a.reclassify_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Reclassification' ";
    updateAssetStatusChange(query11);
    String query12= "update a set a.Asset_Status = 'REJECT-SYSTEM',process_status = 'R' from am_asset_approval a, am_assetReclassification b where a.asset_id = b.Asset_id and b.reclassify_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Reclassification' ";
    updateAssetStatusChange(query12);
    String query13= "update a set a.approval_status = 'REJECTED-SYSTEM' from am_assetTransfer a, am_asset_approval b where a.asset_id = b.Asset_id and a.Transfer_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Transfer' ";
    updateAssetStatusChange(query13);
    String query14= "update a set a.Asset_Status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, am_assetTransfer b where a.asset_id = b.Asset_id and b.Transfer_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Asset Transfer' ";
    updateAssetStatusChange(query14);
    String query15= "update a set a.status = 'REJECTED-SYSTEM' from am_gb_bulkTransfer a, am_asset_approval b where a.Batch_id = b.Asset_id and a.Transfer_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and b.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Bulk Asset Transfer' ";
    updateAssetStatusChange(query15);
    String query16= "update a set a.Asset_Status = 'REJECTED-SYSTEM',process_status = 'R' from am_asset_approval a, am_gb_bulkTransfer b where a.asset_id = b.Asset_id and b.Transfer_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
    		+ "and a.asset_status = 'PENDING' and process_status = 'P' and tran_type = 'Bulk Asset Transfer' ";
    updateAssetStatusChange(query16);
    exists = true;
    return exists;
}


//public boolean pendingTrancations() throws Exception {
public boolean reclassificationProcessing(String startDate, String endDate){
	boolean exists;
//	System.out.println("About to Remove Pending Transactions Before Depreciation for the new month ");
	//AssetRecordsBean arb = new AssetRecordsBean();
	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,a.NBV = a.residual_value, "
 	   		+ "a.Monthly_Dep = b.old_nbv,a.Accum_Dep = a.Accum_Dep+b.old_nbv,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = 0,a.Total_Life = new_total_life,Useful_Life = new_total_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV != 10.00 and b.new_remaining_life < 0 and reclassify_date between '"+startDate+"' and '"+endDate+"'");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,"
 	   		+ "a.Monthly_Dep = b.new_monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV != 10.00 and old_depr_rate <> new_depr_rate and b.new_remaining_life > 0 and reclassify_date between '"+startDate+"' and '"+endDate+"'");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,"
 	   		+ "a.Monthly_Dep = b.monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV != 10.00 and old_depr_rate = new_depr_rate and b.new_remaining_life > 0 and reclassify_date between '"+startDate+"' and '"+endDate+"'");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,"
 	   		+ "a.Monthly_Dep = b.new_monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV != 10.00 and b.new_remaining_life = 0 and reclassify_date between '"+startDate+"' and '"+endDate+"' ");

	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,a.NBV = a.residual_value,a.Accum_Dep=b.Cost_Price-a.residual_value, "
 	   		+ "a.Monthly_Dep = b.old_nbv,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = 0,a.Total_Life = new_total_life,Useful_Life = new_total_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV = 10.00 and b.new_remaining_life < 0 and reclassify_date between '"+startDate+"' and '"+endDate+"' ");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,a.Accum_Dep=b.Cost_Price-a.residual_value,"
 	   		+ "a.Monthly_Dep = b.new_monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV = 10.00 and old_depr_rate <> new_depr_rate and b.new_remaining_life > 0 and reclassify_date between '"+startDate+"' and '"+endDate+"' ");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code, "
 	   		+ "a.Monthly_Dep = b.monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV = 10.00 and old_depr_rate = new_depr_rate and b.new_remaining_life > 0 and reclassify_date between '"+startDate+"' and '"+endDate+"' ");
 	   updateAssetStatusChange("UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Category_ID = b.new_category_id,a.CATEGORY_CODE = c.category_code,"
 	   		+ "a.Monthly_Dep = b.new_monthly_dep,a.nbv = b.NBV,a.Dep_Rate = b.new_depr_rate,a.Dep_End_Date = b.new_dep_end_date,a.Remaining_Life = new_remaining_life ,a.Total_Life = new_total_life,Useful_Life = new_total_life-new_remaining_life "
 	   		+ "from  am_asset a, am_assetReclassification b, am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_Id = c.category_Id and b.status = 'ACTIVE' and a.NBV = 10.00 and b.new_remaining_life = 0 and reclassify_date between '"+startDate+"' and '"+endDate+"' ");
 //	  System.out.println("I have Removed Pending Transactions Before Depreciation for the new month ");
  exists = true;
  return exists;
}

public void MigratedAssetWithWrongAccum()
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    PreparedStatement ps2;
    String query; 
    String query2;
    con = null;
    rs = null;
    ps = null;
    ps2 = null;
    query = "update a set a.Accum_Dep = a.Accum_Dep-c.residual_value from am_asset a,am_ad_category c  where a.NBV = c.residual_value and Cost_Price = Accum_Dep ";
//    System.out.println("query in FullyDepreciated: "+query);        
    try {
    con = getConnection("legendPlus");
    ps = con.prepareStatement(query);
    ps.execute();       
} catch (Exception ex) {  
    System.out.println("WARN: Recalssification Correction for Negative Remaining Life ->" +
            ex.getMessage());
} finally {
    closeConnection(con, ps, rs);
    closeConnection(con, ps2, rs);
}        
}

public void LANDAssetNBVValue()
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    String query; 
    con = null;
    rs = null;
    ps = null;
    query = "UPDATE a SET a.NBV = a.Cost_Price - c.residual_value FROM AM_ASSET a, AM_AD_CATEGORY c WHERE a.CATEGORY_CODE = c.category_code AND  c.residual_value = 0.00 ";
    try {
    con = getConnection("legendPlus");
    ps = con.prepareStatement(query);
    ps.execute();  
} catch (Exception ex) {  
    System.out.println("WARN: Recalssification Correction for Negative Remaining Life ->" +
            ex.getMessage());
} finally {
    closeConnection(con, ps, rs);
}        
}


}
