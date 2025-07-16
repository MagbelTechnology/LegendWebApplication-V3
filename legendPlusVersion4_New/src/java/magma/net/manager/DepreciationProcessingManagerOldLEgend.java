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

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;
import magma.net.vao.AssetTransaction;
import magma.net.vao.ComponentDetail;
import magma.net.vao.DistributionDetail;
import magma.net.vao.ProcesingInfo;

// Referenced classes of package magma.net.manager:
//            FleetHistoryManager, DepreciationChecks

public class DepreciationProcessingManagerOldLEgend extends MagmaDBConnection
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
    ArrayList Alist;

    public DepreciationProcessingManagerOldLEgend()
    {
        Alist = new ArrayList();
        System.out.println("INFO:Enter Depreciation Transaction Processing ..");
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new DatetimeFormat();
        formata = new CurrencyNumberformat();
        con = new CurrentDateTime();
        process = getProcessingInfo();
        htmlCombo = new HtmlUtility();
        currentProcessingDate = process.getProcessingDate();
        lastDate = process.getNextProcessingDate();
        nextProcessingDate = process.getNextProcessingDate();
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

        String filter = " AND EFFECTIVE_DATE <= '" + process.getNextProcessingDate() + "' ";
        ArrayList asets = historyManager.findAssetForDepreciation(filter);
        DepreciationChecks depchk = new DepreciationChecks();
        int frequency = getProcessingInfo().getFrequency();

        //System.out.println(" The value of frequency is LLLLLLLLLLLL " + frequency);
        String startMonth = startDate.substring(3, 5);
        String lastMonth = endDate.substring(3, 5);
        String processDate = getCompSystemDate();

        String prodate = df.formatDate(process.getProcessingDate());



        String proMonth = processDate.substring(3, 5);
        String proYear = processDate.substring(7, 10);
        String lastYear = endDate.substring(7, 10);
        depchk.insertTempDepreciationEntry();
        depchk.insertTempDistributedAssets();
        boolean isNew = false;
        boolean isImprovement = false;
        double monthlyDepreFromAmAsset = 0.00;
        int usefulLIfeFromAmAsset =0;
        int improvusefulLifeFromAsset = 0;
        double oldAccumDep =0.00;
        double oldimprovAccumDep =0.00;
        /*
        1. If first Month set DPY to Date = 0.00;
        2. if last month, update acc period with months specified.
        3. start from where processing Date less or equals start date.
        4. check if SBU for GL prefix to use.
        5. If depreciation is new i.e ACCUM DEP = 0.00
        6. Determine months based on start date.
        7. if processing month = last depreciation date: dep = NBV - residual
        8. if is fully depreciated NBV = residual
        9. if(distribution required: distribute else skip.
         */

        if (Integer.parseInt(proMonth) == Integer.parseInt(lastMonth) && Integer.parseInt(proYear) == Integer.parseInt(lastYear)) {
            updateYearEnd();
        }
        if (Integer.parseInt(proMonth) == Integer.parseInt(lastMonth) && Integer.parseInt(proYear) == Integer.parseInt(lastYear)) {
            changeFinancialDate(startDate);
        }
       //-- System.out.println("size  " + asets.size());
        for (int x = 0; x < asets.size(); x++) {

            Asset aset = (Asset) asets.get(x);

            //begin new conditions
            isImprovement = false;
           //isNew = false;
            aset.getCost();
            aset.getDepreciationRate();
            aset.getReq_distribution();
            double costThreshold = Double.parseDouble(htmlCombo.findObject("select cost_threshold from am_gb_company"));
            if (aset.getCost() > costThreshold) {
                if (aset.getDepreciationRate() != 0) {
                    //


                    String assetId = aset.getId();
                    System.out.println(">>>>>> asset id >>>> " + assetId+"  <<<<<<<<OldCost Price: "+aset.getCost()+"  <<<<<<<OldNBV: "+aset.getNbv());
                    double cost = aset.getCost();
                    cost = cost - 10.00;
                    double monthlyDepreciation = 0.00d;
                    double improvmonthlyDepreciation = 0.00d;
                    double accumDep = aset.getAccumulatedDepreciation();
                    System.out.println("<<<<<<<<<<<<<<<<accumDep>>>>>> "+accumDep);
                    double residual = aset.getResidualValue();
                    System.out.println("  first entrance of nbv from database >> " +aset.getNbv());
                    double nbv = aset.getNbv();
                    oldAccumDep = cost - nbv;
                    int remainLife = aset.getRemainingLife();
                    int totalLife = aset.getTotalLife();
                    monthlyDepreFromAmAsset = aset.getMonthlyDepreciation();
                    usefulLIfeFromAmAsset = totalLife - remainLife;
                    int improvremainLife = aset.getImprovremainingLife();
                    int improvtotalLife = aset.getImprovtotalLife();
                    double improvecost = aset.getImprovcost();
                    double improvenbv = aset.getImprovnbv();
                    improvusefulLifeFromAsset = improvtotalLife - improvremainLife;
                    improvmonthlyDepreciation = aset.getImprovmonthlyDepreciation();
                   //System.out.println("....... the useful life from  am_asset is .............. " + usefulLIfeFromAmAsset);
                   // System.out.println("....... the monthly depreciation from am_asset is .............. " + monthlyDepreFromAmAsset);
                    String branchCode = aset.getBranchId();
                    String departmentCode = aset.getDepartmentId();
                    String sectorCode = aset.getSection();
                    String astatus = aset.getAsset_status();
//                    System.out.println(" The value nbv >>>>>>>>> before depreciation " + nbv);
//                    System.out.println(" The value accumDep >>>>>>>>> before depreciation " + accumDep);
//                    System.out.println(" The value monthlyDepreciation >>>>>>>>> before depreciation " + monthlyDepreciation);
//                    System.out.println(" The value remainLife >>>>>>>>> before depreciation " + aset.getRemainingLife());
//                    System.out.println(" The value improvremainLife >>>>>>>>> before depreciation " + aset.getImprovremainingLife());
//                    System.out.println(" The value improvtotalLife >>>>>>>>> before depreciation " + aset.getImprovtotalLife());
//                    System.out.println(" The value improvusefulLifeFromAsset >>>>>>>>> before depreciation " + improvusefulLifeFromAsset);
//                    System.out.println(" The value improvmonthlyDepreciation >>>>>>>>> before depreciation " + improvmonthlyDepreciation);
//                    System.out.println(" The value Improvcost >>>>>>>>> before depreciation " + improvecost);
//                    System.out.println(" The value improvenbv >>>>>>>>> before depreciation " + improvenbv);
//                    System.out.println(" The value Status >>>>>>>>> before depreciation " + astatus);
                    if (astatus.equalsIgnoreCase("ACTIVE")) {
                        String improv_remainLife =  appRecord.getCodeName("select IMPROV_REMAINLIFE from am_asset where asset_Id = '"+assetId+"'");
                        if(improv_remainLife != null && !improv_remainLife.equalsIgnoreCase("")){
                        	improvremainLife=Integer.parseInt(improv_remainLife);
                        }   
                        String isimproved =  appRecord.getCodeName("select IMPROVED from am_asset_improvement where revalue_id = (select max(revalue_id)from am_asset_improvement where asset_Id = '"+assetId+"')");
                        if(isimproved.equalsIgnoreCase("P")){
                        	improvenbv =  appRecord.getValue("select IMPROV_NBV from am_asset where asset_Id = '"+assetId+"'");
                     //   	improvenbv =  appRecord.getValue("select COST_PRICE from am_asset_improvement where revalue_id = (select max(revalue_id)from am_asset_improvement where asset_Id = '"+assetId+"')");
                     //   	improvenbv =  improvenbv + appRecord.getValue("select COST_PRICE from am_asset_improvement where revalue_id = (select max(revalue_id)from am_asset_improvement where asset_Id = '"+assetId+"')");
                        	System.out.println("<<<<<<<<improvenbv when isimproved is P >>>>>: "+improvenbv+"  isimproved: "+isimproved);
                        }
                        
                        oldimprovAccumDep =  appRecord.getValue("select IMPROV_ACCUMDEP from am_asset where asset_Id = '"+assetId+"'");
                   /*     if(oldimprovAccumDepreciation != null && !oldimprovAccumDepreciation.equalsIgnoreCase("")){
                      	  oldimprovAccumDep=Double.parseDouble(oldimprovAccumDepreciation);
                        } */  
                        System.out.println("<<<<<<<<improvenbv >>>>>: "+improvenbv+"   <<<<<<<<<oldimprovAccumDep: "+oldimprovAccumDep+"   getImprovEffectiveDate: "+aset.getImprovEffectiveDate()+"  nextProcessingDate: "+this.nextProcessingDate);
                        int monthlyDiff = 0;
                        if(aset.getImprovEffectiveDate() != null){
                        monthlyDiff = df.getDayDifferences(sdf.format(this.nextProcessingDate), sdf.format(aset.getImprovEffectiveDate()));
                //        System.out.println("<<<<<<<<monthlyDiff>>>>>: "+monthlyDiff);
                        }
                        if (monthlyDiff == 0) {
                            monthlyDiff = 1; 
                        }           
                        if (isFullyDepreciated(cost, accumDep, residual, remainLife, improvremainLife)) {

                            executeNbvResidual(assetId, isImprovement,improvremainLife,improvecost,improvenbv,frequency,oldimprovAccumDep,improvmonthlyDepreciation,isimproved);
                            System.out.println(">> skipping depreciation>> ");
                            //Skip processing
                        } else {
//                        	System.out.println(">>  depreciating 111111111 accumDep>> "+accumDep+"  Asset Id: "+assetId);
                            if (accumDep == 0.0) {

//                                System.out.println("process date "+process.getProcessingDate());
//                                System.out.println("next process date "+process.getNextProcessingDate());

//                                	System.out.println("asset id "+aset.getId());
//                                	System.out.println("accumDep "+accumDep);
//                                	System.out.println("totalLife for new "+totalLife);

                                isNew = true;
                                monthlyDepreciation = getCalculatedMonthlyDepreciation(
                                        cost, nbv,
                                        residual, remainLife, totalLife,
                                        aset.getEffectiveDate(), isNew,monthlyDepreFromAmAsset);
//                                  System.out.println("monthlyDepreciation  for new asset   "+monthlyDepreciation);
                                System.out.println("monthlyDepreciation  for new asset   "+monthlyDepreciation);
                                  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>1    "+frequency);

                            } else {
                                // if(remainLife > 0){
                                isNew = false;
                                monthlyDepreciation = getCalculatedMonthlyDepreciation(
                                        cost, nbv,
                                        residual, remainLife, totalLife,
                                        aset.getEffectiveDate(), isNew,monthlyDepreFromAmAsset);
                                System.out.println("monthlyDepreciation  for old asset   "+monthlyDepreciation);
                                System.out.println(">> depreciating monthlyDepreciation 22222222>> "+monthlyDepreciation);
                                //}

                            }

                            AssetTransaction asetTran = new AssetTransaction(assetId,
                                    frequency,
                                    cost, monthlyDepreciation, branchCode,
                                    departmentCode,
                                    sectorCode);
                            asetTran.setImprovMonthlyDepreciation(improvmonthlyDepreciation);
                            asetTran.setMonthlyDepreciation(monthlyDepreciation);
                            
                            String oldAccumDepreciation =  appRecord.getCodeName("select accum_dep from am_asset where asset_Id = '"+assetId+"'");
                            if(oldAccumDepreciation != null && !oldAccumDepreciation.equalsIgnoreCase("")){
         //                   oldAccumDep=Double.parseDouble(oldAccumDepreciation);
                            
                            if (isNew == true) {
                               System.out.println(">>>>>>>>>>>>>>>>>>improvmonthlyDepreciation>>>>>>>>>>>>>>    "+improvmonthlyDepreciation);
                                // int monDiff = df.getDayDifference(sdf.format(this.nextProcessingDate),sdf.format(aset.getEffectiveDate()))/30;
                                if (monthlyDepreFromAmAsset == 0) {
                                    int monDiff = df.getDayDifferences(sdf.format(this.nextProcessingDate), sdf.format(aset.getEffectiveDate()));
                                  //--   System.out.println(" date  sdf.format(this. nextProcessingDate)   "+sdf.format(this.nextProcessingDate));
                                    //  System.out.println(" date  sdf.format(aset.getEffectiveDate())) / 30  "+sdf.format(aset.getEffectiveDate()));
                                    if (monDiff == 0) {
                                        monDiff = 1;
                                    }
                                    asetTran.setFrequency(monDiff);
                                    //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3    " + monDiff);
                                } else {
                                 //--   System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>4    " + frequency);
                                    isImprovement = true;
//by Matanmi 05-01-2016          String oldAccumDepreciation =  appRecord.getCodeName("select old_accum_dep from am_asset_improvement where revalue_id = (select max(revalue_id)from dbo.am_asset_improvement where asset_Id = '"+assetId+"')");
                                  }
                                  /*
                                  String oldimprovAccumDepreciation =  appRecord.getCodeName("select IMPROV_USEFULLIFE from am_asset_improvement where revalue_id = (select max(revalue_id)from dbo.am_asset_improvement where asset_Id = '"+assetId+"')");
                                  if(oldimprovAccumDepreciation != null && !oldimprovAccumDepreciation.equalsIgnoreCase("")){
                                	  oldimprovAccumDep=Double.parseDouble(oldimprovAccumDepreciation);
                                  }                                  
                                  */

                                    asetTran.setFrequency(frequency);
                                    //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3    "+monDiff);
                                }

                            } else {
                               //-- System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>5    " + frequency);
                                asetTran.setFrequency(frequency);

                            }



//                          System.out.println("The rem,MMMMMMM aset.getRemainingLife() " + aset.getRemainingLife());
                           // remainLife = aset.getRemainingLife();
                          double newmonthlyDep = aset.getNbv()/remainLife;
                            if (aset.getRemainingLife() == 0) {
                                executeNbvResidual(asetTran);
                            } else {
                                System.out.println(" The value of processing frequency is KKKKKKK " + asetTran.getFrequency());
//                                System.out.println("<<<<<<<<<Monthly Depreciation: "+asetTran.getMonthlyDepreciation()+"  <<<<<<<Branch Code: "+asetTran.getBranchCode()+" <<<<<<< improvenbv:"+improvenbv+"  <<<<<<<<<<<monthlyDepreciation: "+monthlyDepreciation);
                                String improve_usefullife =  appRecord.getCodeName("select IMPROV_USEFULLIFE from am_asset_improvement where revalue_id = (select max(revalue_id)from am_asset_improvement where asset_Id = '"+assetId+"')");
                                if(improve_usefullife==""){improve_usefullife = "0";}
                                executeProcessDepreciation(asetTran, nextdate, userid, remainLife, isImprovement,usefulLIfeFromAmAsset,oldAccumDep,Integer.parseInt(improve_usefullife),oldimprovAccumDep,improvusefulLifeFromAsset,improvremainLife,improvecost,improvenbv,isimproved,monthlyDepreciation);
                                executeFullDepreciated(asetTran);
                                depchk.updateDepTemp(aset, monthlyDepreciation * asetTran.getFrequency(), asetTran.getFrequency());
                            }
                        }
                    } else if (astatus.equalsIgnoreCase("DISPOSED")) {
                        Alist.add(aset);
                       //-- System.out.println(aset.getId());
                    } else {
                    }
                    //New condition added
                }

            }




        }
        logDepreciationTransactionSummary(processDate, nextdate, userid);
        notifyNextProcessingDate(this.nextProcessingDate, this.lastDate);
            System.out.println("About to Backup the AM_ASSET File");
            depdonesap();
            depdoneInsert();  
            System.out.println("Backup of AM_ASSET File has been completed");          
        depchk.archiveEntrytable();
        depchk.clearEntrytable();
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
        con = getConnection("fixedasset");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
    } catch (Exception ex) {  
        System.out.println("WARN: Error updating perform_year_end->" +
                ex.getMessage());
    } finally {
        closeConnection(con, ps, rs);
    } 

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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        System.out.println(" in executeNbvResidual method >> ");
        double month = 0.0D;
        output = "fail";
        con = null;
        ps = null;
        query = (new StringBuilder()).append("UPDATE AM_ASSET SET  MONTHLY_DEP =").append(month).append(" ,NBV=RESIDUAL_VALUE WHERE ASSET_ID = ? ").toString();
        try {
        System.out.println(query);
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
       // ps = con.prepareStatement(query2);
        System.out.println("<<<<<<<improvenbv: "+improvenbv+"    improvremainLife: "+improvremainLife+"  >>>>>>>>isImproved: "+isImproved+"  <<<<<<<<<oldAccumDep: "+oldAccumDep);
        if(isImproved.equalsIgnoreCase("P")){depreciationmonthly =  improvenbv/improvremainLife;}
        double zeroMonthlyDep = 0.00;
        System.out.println("<<<<<<<depreciationmonthly: "+depreciationmonthly+"   <<<<<improve_usefullife: "+improve_usefullife+"  <<<<<<remainLife: "+remainLife+"   <<<<<<isImprovement: "+isImprovement+"   <<<<<<<<<<<improvremainLife: "+improvremainLife+"  <<<<<<Frequency: "+assetTran.getFrequency()+"  <<<<<<<<<<newmonthlyDep: "+newmonthlyDep);
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
        System.out.println((new StringBuilder()).append("the value of ImprovMonthlyDepreciation 1 is >>>>>>>> ").append(assetTran.getImprovMonthlyDepreciation()).toString());
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
        	System.out.println("<<<<<<<<improve_usefullife 2: "+improve_usefullife);
       	if(isImproved == "P")
        {
            ps2.setDouble(1, oldAccumDep + assetTran.getMonthlyDepreciation());
        } else
        {
            System.out.println((new StringBuilder()).append("the value of frequency 2 is >>>>>>>> not improvement").append(assetTran.getFrequency()).toString());
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
        	System.out.println("<<<<<<<improvAccumDep > 0 Calculated 2 >>>>>> "+(oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation()));
        ps2.setDouble(7, oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation());
        }else{
        	System.out.println("<<<<<<<improvAccumDep Calculated 2 >>>>>> "+(improvenbv/improvremainLife));
        	ps2.setDouble(7, depreciationmonthly * (double)assetTran.getFrequency());
        }
        
        ps2.setDouble(8, depreciationmonthly * (double)assetTran.getFrequency());
        if(isImproved == "P"){
        	System.out.println("<<<<<<<<IMPROVE NBV 1: ");
        	ps2.setDouble(9,  oldimprovAccumDep + assetTran.getImprovMonthlyDepreciation());
        }
        else{
        	System.out.println("<<<<<<<<IMPROVE NBV 2: "+"   depreciationmonthly: "+depreciationmonthly);
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
        con = getConnection("fixedasset");
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

    public void notifyProcessedAsset(String assetid, String status)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE AM_ASSET SET ASSET_STATUS = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("fixedasset");
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

    public void notifyRaisedClassifiedAsset(String assetid)
    {
        Connection con;
        PreparedStatement ps;
        String NOTIFY_QUERY;
        con = null;
        ps = null;
        NOTIFY_QUERY = "UPDATE  AM_ASSETRECLASSIFICATION SET RAISE_ENTRY = ? WHERE ASSET_ID = ?  ";
        try {
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
        ps = con.prepareStatement(FINDER_QUERY);
        ps.setString(1, assetId);
    //    DistributionDetail dd;
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
        con = getConnection("fixedasset");
        ps = con.prepareStatement(GL_QUERY);
        System.out.println((new StringBuilder()).append("GL_QUERY___").append(GL_QUERY).toString());
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
        con = getConnection("fixedasset");
        ps = con.prepareStatement(GL_QUERY);
        System.out.println((new StringBuilder()).append("GL_QUERY___").append(GL_QUERY).toString());
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        TaxVatGL = new String[2];
        con = null;
        rs = null;
        ps = null;
        query = "SELECT VAT_ACCOUNT,WHT_ACCOUNT FROM AM_GB_COMPANY";
        try {
        con = getConnection("fixedasset");
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {

            String vatGL = rs.getString(1);
            String taxGL = rs.getString(2);
            TaxVatGL[0] = vatGL;
            TaxVatGL[1] = taxGL;
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
            con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
        ps = con.prepareStatement(query);
        ps.setDate(1, df.dateConvert(procdate));
        ps.setString(2, userid);
        ps.setDate(3, dateConvert(nextdate));
        ps.execute();
        } catch (Exception ex) {
            System.out.println(
                    "WARNING: Error logging Depreciation transaction->" +
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
    public String executeNbvResidual(String assetId,boolean isImprovement, int improvremainLife,double improvecost,double improvenbv,int frequency,double oldimprovAccumDep, double improvmonthlyDepreciation,String isImproved )
    {
        String output;
        Connection con;
        PreparedStatement ps,ps1,ps3,ps4,ps5;
        String query;
        String query2 = "";
        String query3 = "";
        String query4 = "";
        String query5 = "";
        //boolean setSucessful = false;
//        System.out.println("assetId in executeNbvResidual method >> "+assetId+"   <<<<isImproved: "+isImproved);
        double month = 0.0D;
        output = "fail";
        con = null;
        ps = null;  
        ps1 = null;
        ps3 = null;
        ps4 = null;   
        ps5 = null;
        query = (new StringBuilder()).append("UPDATE AM_ASSET SET  MONTHLY_DEP =").append(month).append(" ,NBV=RESIDUAL_VALUE WHERE ASSET_ID = ? ").toString();
        System.out.println(query);
        query2 = "UPDATE AM_ASSET  SET  IMPROV_ACCUMDEP = coalesce(IMPROV_ACCUMDEP,0) + ? ,IMPROV_MONTHLYDEP = ? , IMPROV_NBV = IMPROV_C" +
        		"OST - (coalesce(IMPROV_ACCUMDEP,0) + ?),IMPROV_USEFULLIFE = coalesce(IMPROV_USEFULLIFE,0) + ? ,IMPROV_REMAINLIFE = IMPROV_REMAIN" +
        		"LIFE - ?,TOTAL_NBV = (NBV + (IMPROV_COST - (coalesce(IMPROV_ACCUMDEP,0) + ?)))  WHERE ASSET_ID = ? ";
//        query4 = "UPDATE AM_ASSET SET TOTAL_NBV = NBV+coalesce(IMPROV_NBV,0) WHERE ASSET_ID = ?";
        query3 = "UPDATE AM_ASSET SET IMPROV_MONTHLYDEP = 0.00 WHERE ASSET_ID = ?";
        query5 = "UPDATE am_asset_improvement SET IMPROVED = 'N' WHERE ASSET_ID = '"+assetId+"'";
        try {  
        con = getConnection("fixedasset");
        ps = con.prepareStatement(query);
        ps.setString(1, assetId);
        ps.executeUpdate();
        double monthlydep = improvenbv/improvremainLife;
        double zeroMonthlyDep = 0.00;
        System.out.println("<<<<<<< Fully Depreciated improvremainLife>>>>>>>>> "+improvremainLife+"   <<<<<oldimprovAccumDep: "+oldimprovAccumDep+"    improvenbv: "+improvenbv+"    assetId: "+assetId+"    frequency: "+frequency+"  improvmonthlyDepreciation: "+improvmonthlyDepreciation+"  monthlydep: "+monthlydep);
    	if(improvremainLife !=0){
 //   		closeConnection(con, ps);  
    		double depreciationmonthly =  improvenbv/improvremainLife;
    		System.out.println("<<<<<<<depreciationmonthly>>>>>>>>> "+depreciationmonthly);
    		ps1 = con.prepareStatement(query2);
//    		if(oldimprovAccumDep==0.00){
    		if(isImproved == "P"){
    			System.out.println("<<<<<<<Is Improved improvmonthlyDepreciation>>>>>>>>> "+improvmonthlyDepreciation+"    <<<<<<<<<<<<<oldimprovAccumDep: "+oldimprovAccumDep);
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
        if(isImproved.equalsIgnoreCase("P")){
   //     	System.out.println("Inside query loop: ");
		    ps5 = con.prepareStatement(query5);
		    ps5.executeUpdate();
	//	    System.out.println("Inside query 5: ");
	   	}    
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
        	   closeConnection(con, ps5);
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
        con = getConnection("fixedasset");  
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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

    public boolean Yeardonesap()
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
        System.out.println("About to Delete records from file AM_ASSET_YEARLY");
        String Sapquery="delete from AM_ASSET_YEARLY where asset_id is not null ";
        System.out.println(Sapquery);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("fixedasset");
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
        con = getConnection("fixedasset");
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
    public boolean depdoneInsertStamp(String deprecDate)
    {
        String sql;
        boolean exists;
        Connection con;
        Statement stmt;
        ResultSet rs;
        String depDay = deprecDate.substring(0,2);
        String depMonth = deprecDate.substring(3,5);
        String depYear = deprecDate.substring(6,10); 
        System.out.println("Depreciation Date in depdoneInsertStamp: "+deprecDate);
        String closedate = formatDate(dateConvert(new Date()));
        /*
        String closeDay = closedate.substring(0,2);
        String closeMonth = closedate.substring(3,5);
        String closeYear = closedate.substring(6,10);      
        */   
        System.out.println("About to Insert records into file FIXED_ASSET_SCHEDULE_ARCHIVE & FIXED_ASSET_SCHEDULE_TEMP_ARCHIVE ");
        
        String query = "INSERT INTO FIXED_ASSET_SCHEDULE_ARCHIVE SELECT Mtid,class_code,start_date," +
        		"end_date,class_name,cost_open_bal,cost_disposal,cost_additions,cost_WipReclas," +
        		"cost_reclass,dep_open_bal,dep_charge,dep_disposal,dep_reclass,revaluation," +
        		"improvement,nbv_open_bal,nbv_closing_bal," +
        "'"+depDay+"','"+depMonth+"','"+depYear+"','"+closedate+"'  " +
        " FROM fixed_asset_schedule WHERE MTID IS NOT NULL  ";
        
        String query2 = "INSERT INTO FIXED_ASSET_SCHEDULE_TEMP_ARCHIVE SELECT Mtid,class_code,start_date,end_date," +
        		"class_name,cost_open_bal,cost_disposal,cost_additionsNewAsset,cost_additionsGroupAssets," +
        		"cost_additionsPartPayment,cost_additionsImprovement,cost_WipReclassification,cost_reclass," +
        		"dep_open_bal,dep_charge,dep_disposal,dep_reclass,revaluation,improvement,nbv_open_bal," +
        		"nbv_closing_bal, " +
        		"'"+depDay+"','"+depMonth+"','"+depYear+"','"+closedate+"'  " +
        		" FROM fixed_asset_schedule_temp WHERE MTID IS NOT NULL  ";        
        
      	//String query="INSERT INTO [AM_ASSET_MONTHLY] SELECT * FROM [am_asset]  where asset_id is not null";
        System.out.println(query);
        System.out.println(query2);
        exists = false;
        con = null;
        stmt = null;
        rs = null;
        try {
        con = getConnection("fixedasset");
        stmt = con.createStatement();
        rs = stmt.executeQuery(query); 
        stmt = con.createStatement();
        rs = stmt.executeQuery(query2);         
        //exists = true;
        exists = rs.next();
    //    System.out.println("=====Exists for Monthly Insertion=====> "+exists);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return exists;
    } 
}
