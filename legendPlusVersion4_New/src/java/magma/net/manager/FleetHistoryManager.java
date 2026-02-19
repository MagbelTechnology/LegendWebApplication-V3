package magma.net.manager;

import magma.net.vao.Stock;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.mail.MailSender;
import com.magbel.legend.vao.newAssetTransaction;
import com.magbel.util.DataConnect;
import legend.admin.handlers.CompanyHandler;
import legend.admin.handlers.SecurityHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File; 

import jxl.Workbook; 
import jxl.write.*; 

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import magma.AssetRecordsBean;
import magma.BarCodeHistoryBean;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;
import magma.net.vao.AssetDetail;
import magma.net.vao.BranchVisit;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.FleetTransaction;
import magma.net.vao.FleetManatainanceRecord;
import magma.net.vao.FleetLicencePermit;
import magma.net.vao.FleetInsurranceRecord;
import magma.net.vao.FleetAccidentRecord;
import magma.net.vao.FleetFuelRecord;
import magma.net.vao.AccidentImage;
import magma.net.vao.VendorAssessment;

import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.CurrentDateTime;


/**
 * <p>Title: FleetHistoryManager.java</p>
 *
 * <p>Description: Contains method for creating Fleet transactions<br>
 * and retrievals. Also contains method for retrieving data types
 * as defines in the system configuration</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class FleetHistoryManager extends MagmaDBConnection {

    FleetTransactManager tranManager;
    DatetimeFormat df;
    CurrentDateTime dfs;
    SimpleDateFormat sdf;
    private boolean overFlow;
    private double minCost =0;
    private double maxCost = 0;
    private ApplicationHelper applHelper ;
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	private CompanyHandler cdb;
	private SecurityHandler sechanle;
	SimpleDateFormat timer = null;
	MailSender mailSender = null;

    public FleetHistoryManager() {
        super();
        tranManager = new FleetTransactManager();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        dfs = new com.magbel.util.CurrentDateTime();
        mail= new EmailSmsServiceBus();
        cdb= new CompanyHandler();
        sechanle= new SecurityHandler();
        records= new ApprovalRecords();
        applHelper= new ApplicationHelper();
        timer = new SimpleDateFormat("kk:mm:ss");
    }

    public void setOverFlow(boolean overFlow) {
        this.overFlow = overFlow;
    }

    public boolean isOverFlow() {
        return overFlow;
    }

  private MagmaDBConnection dbConnection = new MagmaDBConnection();
         Connection con1 = null;
            PreparedStatement ps1 = null;
            ResultSet rs1 = null;


    /**
     * createMaintenanceRecord
     *
     * @param type String
     * @param dateOfRepair String
     * @param technicianType String
     * @param milleageBeforeMaintenace double
     * @param milleageAfterMaintenace double
     * @param details String
     * @param cost double
     * @param componentReplaced String
     * @param lastPerformedDate String
     * @param nextPerformedDate String
     * @param maintenanceType String
     */
    //modified by olabo to add invoiceNo and hist_id columns
    public void createMaintenanceRecord(String assetId, String registrationNo,
                                        String branchId, String deptId,
                                        String category, String assetMake,
                                        String datePurchased,
                                        String createDate, String effectiveDate,
                                        String type, String dateOfRepair,
                                        String technicianType, double fuelCost,
                                        double milleageBeforeMaintenace,
                                        double milleageAfterMaintenace,
                                        String details, double cost,
                                        String componentReplaced,
                                        String lastPerformedDate,
                                        String nextPerformedDate,
                                        String techName,
                                        String maintenanceType, String userid,
                                        String firstNoticeDate, String freq,
                                        String invoiceNo, String histId,
                                        double vatAmt, double whtAmt,String projectCode,int assetCode,String selectWht,String selectVat) {
    	String dd = effectiveDate.substring(0,2);   
    	String mm = effectiveDate.substring(3,5);  
    	String yyyy = effectiveDate.substring(6,10);
    	effectiveDate = yyyy+"-"+mm+"-"+dd;
    	if(datePurchased==""){datePurchased = effectiveDate;}
    	
        if (lastPerformedDate == null || lastPerformedDate.equals("")) {
            lastPerformedDate = sdf.format(new Date()); 
        }
        if (nextPerformedDate == null || nextPerformedDate.equals("")) {
            nextPerformedDate = sdf.format(new Date());
        }  
        java.sql.Date noticeDate = null;  
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        //if (freq == null) {
        intFreq = Integer.parseInt(freq);
        // } 
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_MAINTENANCE_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                             " ,REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                             ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                             ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                             ",NEXT_PM_DATE,MAINTENANCE_TYPE,USER_ID," +
                             "FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,"+
                             "HIST_ID,VAT_AMT,WHT_AMT,PROJECT_CODE,CREATE_DATE,ASSET_CODE,BRANCH_ID,subject_TO_WHT,subject_TO_Vat,TECHNICIAN_CODE) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        if (isTransactionOverFlow(cost, assetMake, category)) {
            setOverFlow(true);
        } else {

            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(createQuery);
//                System.out.println("=======techName: "+techName);
                ps.setString(1, type);
                ps.setDouble(2, cost);
                ps.setString(3, assetId);
                ps.setString(4, registrationNo);
                ps.setDate(5, dateConvert(dateOfRepair));
                ps.setString(6, technicianType);
                ps.setString(7, techName);
                ps.setDouble(8, milleageBeforeMaintenace);
                ps.setDouble(9, milleageAfterMaintenace);
                ps.setString(10, details);
                ps.setString(11, componentReplaced);
                ps.setDate(12, dateConvert(lastPerformedDate));
                ps.setDate(13, dateConvert(nextPerformedDate));
                ps.setString(14, maintenanceType);
                ps.setString(15, userid);
                ps.setDate(16, noticeDate);
                ps.setInt(17, intFreq);
                ps.setString(18, invoiceNo);
                ps.setString(19, histId);
                ps.setDouble(20, vatAmt);
                ps.setDouble(21, whtAmt);
                ps.setString(22, projectCode);
                ps.setTimestamp(23, dbConnection.getDateTime(new java.util.Date()));
                ps.setInt(24, assetCode);
                ps.setString(25, branchId);
                ps.setString(26, selectWht);
                ps.setString(27, selectVat); 
                ps.setString(28, techName);
                ps.execute();
                int isCreated = ps.getUpdateCount();

                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = cost;
                double maintPeriodToDate = cost;
                double fuelLiveToDate = fuelCost;
                double fuelPeriodToDate = fuelCost;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

//System.out.println("<<<<createDate: "+createDate+"   effectiveDate: "+effectiveDate+"  datePurchased: "+datePurchased+"  premimumPeriodToDate: "+premimumPeriodToDate);
                tranManager.logFleetTransaction(fleetTran);

            } catch (Exception e) {
                System.out.println("INFO:Error creating MAINTENANCE_HISTORY ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
        }
    }

    public void reverseMaintenanceRecord(String assetId, String registrationNo,
                                         String branchId, String deptId,
                                         String category, String assetMake,
                                         String datePurchased,
                                         String createDate,
                                         String effectiveDate,
                                         String type, String dateOfRepair,
                                         String technicianType, double fuelCost,
                                         double milleageBeforeMaintenace,
                                         double milleageAfterMaintenace,
                                         String details, double cost,
                                         String componentReplaced,
                                         String lastPerformedDate,
                                         String nextPerformedDate,
                                         String techName,
                                         String maintenanceType, String userid,
                                         String firstNoticeDate, String freq,
                                         String invoiceNo) {
        if (lastPerformedDate == null || lastPerformedDate.equals("")) {
            lastPerformedDate = sdf.format(new Date());
        }
        if (nextPerformedDate == null || nextPerformedDate.equals("")) {
            nextPerformedDate = sdf.format(new Date());
        }
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = Integer.parseInt(freq);
        }
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_MAINTENANCE_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                             " ,REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                             ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                             ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                             ",NEXT_PM_DATE,MAINTENANCE_TYPE,USER_ID," +
                             "FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,STATUS) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        if (isTransactionOverFlow(cost, assetMake, category)) {
            setOverFlow(true);
        } else {
            cost = cost * ( -1);
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(createQuery);

                ps.setString(1, type);
                ps.setDouble(2, cost);
                ps.setString(3, assetId);
                ps.setString(4, registrationNo);
                ps.setDate(5, dateConvert(dateOfRepair));
                ps.setString(6, technicianType);
                ps.setString(7, techName);
                ps.setDouble(8, milleageBeforeMaintenace);
                ps.setDouble(9, milleageAfterMaintenace);
                ps.setString(10, details);
                ps.setString(11, componentReplaced);
                ps.setDate(12, dateConvert(lastPerformedDate));
                ps.setDate(13, dateConvert(nextPerformedDate));
                ps.setString(14, maintenanceType);
                ps.setString(15, userid);
                ps.setDate(16, noticeDate);
                ps.setInt(17, intFreq);
                ps.setString(18, invoiceNo);
                ps.setString(19, "REVERSED");

                ps.execute();
                int isCreated = ps.getUpdateCount();

                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = cost;
                double maintPeriodToDate = cost;
                double fuelLiveToDate = fuelCost;
                double fuelPeriodToDate = fuelCost;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);

            } catch (Exception e) {
                System.out.println("INFO:Error creating MAINTENANCE_HISTORY ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
        }
    }


    /**
     * createInssuranceRecord
     *
     * @param registrationNo String
     * @param company String
     * @param cost double
     * @param dateObtained String
     * @param dateExpired String
     */
    public void createInssuranceRecord(String assetId, String registrationNo,
                                       String branchId, String deptId,
                                       String category, String assetMake,
                                       String datePurchased,
                                       String createDate, String effectiveDate,
                                       String company,
                                       double cost, String dateObtained,
                                       String dateExpired, String userid,
                                       String firstNoticeDate, String freq,
                                       String tranId, String invoiceNo,String projectCode,String batchId) {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = 0;
        } else {
            intFreq = Integer.parseInt(freq);
        }

        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_INSURANCE_HISTORY(" +
                             "TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO" +
                             ",COMPANY,DATE_OBTAINED,EXPIRY_DATE,USER_ID, " +
                             " FIRST_NOT_DATE,NOTIFICATION_FREQ,NEXT_NOT_DATE,HIST_ID,INVOICE_NO,PROJECT_CODE,BATCH_ID) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "I");
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setString(5, company);
            ps.setDate(6, dateConvert(dateObtained));
            ps.setDate(7, dateConvert(dateExpired));
            ps.setString(8, userid);
            ps.setDate(9, noticeDate);
            ps.setInt(10, intFreq);
            ps.setDate(11, noticeDate);
            ps.setString(12, tranId);
            ps.setString(13, invoiceNo);
            ps.setString(14, projectCode);
            ps.setString(15, batchId);
            ps.execute(); 
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);
                fleetTran.setInsuranceLiveToDate(cost);
                fleetTran.setInsurancePremiumToDate(cost);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating INSURANCE_HISTORY ->" +
                               e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void reverseInssuranceRecord(String assetId, String registrationNo,
                                        String branchId, String deptId,
                                        String category, String assetMake,
                                        String datePurchased,
                                        String createDate, String effectiveDate,
                                        String company,
                                        double cost, String dateObtained,
                                        String dateExpired, String userid,
                                        String firstNoticeDate, String freq,
                                        String tranId) {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = Integer.parseInt(freq);
        }

        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_INSURANCE_HISTORY(" +
                             ",TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO" +
                             ",COMPANY,DATE_OBTAINED,EXPIRY_DATE,USER_ID, " +
                             " FIRST_NOT_DATE,NOTIFICATION_FREQ,STATUS,NEXT_NOT_DATE,HIST_ID) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        cost = cost * ( -1);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "I");
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setString(5, company);
            ps.setDate(6, dateConvert(dateObtained));
            ps.setDate(7, dateConvert(dateExpired));
            ps.setString(8, userid);
            ps.setDate(9, noticeDate);
            ps.setInt(10, intFreq);
            ps.setString(11, "REVERSED");
            ps.setDate(12, noticeDate);
            ps.setString(13, tranId);

            ps.execute();
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);
                fleetTran.setInsuranceLiveToDate(cost);
                fleetTran.setInsurancePremiumToDate(cost);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating INSURANCE_HISTORY ->" +
                               e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }


    /**
     * createFuelRecord
     *
     * @param registrationNo String
     * @param company String
     * @param cost double
     * @param dateObtained String
     * @param dateExpired String
     */
    public void createFuelRecord(String assetId, String registrationNo,
                                 String branchId, String deptId,
                                 String category, String assetMake,
                                 String datePurchased, String invoice,
                                 String createDate, String effectiveDate,
                                 double volume, double cost, String fuelType,
                                 String entryDate, String userid,
                                 String firstNoticeDate, String freq,
                                 String tranId, double unitPrice,String vendorId,String batchId) {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        //if (freq == null) {
        intFreq = Integer.parseInt(freq);
        //}

        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_FUEL_HISTORY(" +
                             "TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO," +
                             "VOLUME,FUEL_TYPE,INVOICE_NO,EFFECTIVE_DATE,ENTRY_DATE," +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,NEXT_NOT_DATE,HIST_ID,UNIT_PRICE,VENDOR_ID,BATCH_ID)  " +
                             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "F");
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setDouble(5, volume);
            ps.setString(6, fuelType);
            ps.setString(7, invoice);
            ps.setDate(8, dateConvert(effectiveDate));
            ps.setDate(9, dateConvert(entryDate));
            ps.setString(10, userid);
            ps.setDate(11, noticeDate);
            ps.setInt(12, intFreq);
            ps.setDate(13, noticeDate);
            ps.setString(14, tranId);
            ps.setDouble(15, unitPrice);
            ps.setString(16, vendorId);
            ps.setString(17, batchId);
            ps.execute();
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "0";
                String location = "0";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = cost;
                double fuelPeriodToDate = cost;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";   

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating FUEL_RECORDS ->" +
                               e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public void reverseFuelRecord(String assetId, String registrationNo,
                                  String branchId, String deptId,
                                  String category, String assetMake,
                                  String datePurchased, String invoice,
                                  String createDate, String effectiveDate,
                                  double volume, double cost, String fuelType,
                                  String entryDate, String userid,
                                  String firstNoticeDate, String freq,
                                  String tranId) {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = Integer.parseInt(freq);
        }

        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_FUEL_HISTORY(" +
                             "TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO," +
                             "VOLUME,FUEL_TYPE,INVOICE_NO,EFFECTIVE_DATE,ENTRY_DATE," +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,STATUS,NEXT_NOT_DATE,HIST_ID)  " +
                             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "F");
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setDouble(5, volume);
            ps.setString(6, fuelType);
            ps.setString(7, invoice);
            ps.setDate(8, dateConvert(effectiveDate));
            ps.setDate(9, dateConvert(entryDate));
            ps.setString(10, userid);
            ps.setDate(11, noticeDate);
            ps.setInt(12, intFreq);
            ps.setString(13, "REVERSED");
            ps.setDate(14, noticeDate);
            ps.setString(15, tranId);

            ps.execute();
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "--";
                String location = "--";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = cost;
                double fuelPeriodToDate = cost;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating FUEL_RECORDS ->" +
                               e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }


    /**
     * createLicencePermitRecord
     *
     * @param registrationNo String
     * @param type String
     * @param cost double
     * @param assetCode String
     * @param dateObtained String
     * @param dateExpired String
     */
    public void createLicencePermitRecord(String assetId, String registrationNo,
                                          String branchId, String deptId,
                                          String category, String assetMake,
                                          String datePurchased,
                                          String createDate,
                                          String effectiveDate,
                                          String type, double cost,
                                          String dateObtained,
                                          String dateExpired, String userid,
                                          String firstNoticeDate, String freq,
                                          String tranId, String invoiceNo) {
        if (dateExpired == null || dateExpired.equals("")) {
            dateExpired = sdf.format(new Date());
        }
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = Integer.parseInt(freq);
        } else {
            intFreq = Integer.parseInt(freq);
        }

        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_LICENCE_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID " +
                             ",REGISTRATION_NO,DATE_OBTAINED,EXPIRY_DATE," +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,NEXT_NOT_DATE,HIST_ID,INVOICE_NO) " +
                             "VALUES( ?,?,?,?,?,?,?,?,?,?,?,?) ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, type);
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setDate(5, dateConvert(createDate));
            ps.setDate(6, dateConvert(dateExpired));
            ps.setString(7, userid);
            ps.setDate(8, noticeDate);
            ps.setInt(9, intFreq);
            ps.setDate(10, noticeDate);
            ps.setString(11, tranId);
            ps.setString(12, invoiceNo);

            ps.execute();
            int isCreated = ps.getUpdateCount();

            if (isCreated > 0) {
                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = cost;
                double licencePermitPeriodToDate = cost;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating LICENCE_HISTORY in createLicencePermitRecord ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void reverseLicencePermitRecord(String assetId,
                                           String registrationNo,
                                           String branchId, String deptId,
                                           String category, String assetMake,
                                           String datePurchased,
                                           String createDate,
                                           String effectiveDate,
                                           String type, double cost,
                                           String dateObtained,
                                           String dateExpired, String userid,
                                           String firstNoticeDate, String freq) {
        if (dateExpired == null || dateExpired.equals("")) {
            dateExpired = sdf.format(new Date());
        }
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        if (freq == null) {
            intFreq = Integer.parseInt(freq);
        }
        cost = cost * ( -1);
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_LICENCE_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID " +
                             ",REGISTRATION_NO,DATE_OBTAINED,EXPIRY_DATE," +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,STATUS) " +
                             "VALUES( ?,?,?,?,?,?,?,?,?,?) ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, type);
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setDate(5, dateConvert(createDate));
            ps.setDate(6, dateConvert(dateExpired));
            ps.setString(7, userid);
            ps.setDate(8, noticeDate);
            ps.setInt(9, intFreq);
            ps.setString(10, "REVERSED");

            ps.execute();
            int isCreated = ps.getUpdateCount();

            if (isCreated > 0) {
                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = cost;
                double licencePermitPeriodToDate = cost;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating LICENCE_HISTORY in reverseLicencePermitRecord ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    /**
     * createAccidentRecord
     *
     * @param registrationNo String
     * @param assetCode String
     * @param driveInvolved String
     * @param location String
     * @param accidentDate String
     * @param policeReportObtained String
     * @param reportDate String
     * @param repairCost double
     * @param costBorneByCompany double
     * @param costBorneByInssurance double
     * @param duration int
     * @param otherDetails String
     * @param insurranceNotified String
     * @param notificationDate String
     * @param replaceRequired String
     */
   
    public void createAccidentRecord(String assetId, String registrationNo,
                                     String branchId, String deptId,
                                     String category, String assetMake,
                                     String datePurchased,
                                     String createDate, String effectiveDate,
                                     String driverInvolved, String location,
                                     String accidentDate,
                                     String policeReportObtained,
                                     String reportDate, double repairCost,
                                     double costBorneByCompany,
                                     double costBorneByInssurance, int duration,
                                     String otherDetails,
                                     String insurranceNotified,
                                     String notificationDate,
                                     String replaceRequired, String userid,
                                     String firstNoticeDate, String freq,
                                     String invoiceNo, String tranId,
                                     String insurer,String transDate,String batchId) 
    {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        //if (freq == null) {
        intFreq = Integer.parseInt(freq);
        //}
  
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_ACCIDENT_HISTORY(" +
                             " TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                             " ,DRIVER_INVOLVED,LOCATION,ACCIDENT_DATE " +
                             " ,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE," +
                             "COST_COMPANY_BORNE " +
                             " ,COST_INSURANCE_BORNE,DURATION,OTHER_DETAILS " +
                             ",NOTIFIED_INSURANCE,NOFTIFIED_DATE,REQUIRED_REPLACEMENT, " +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,HIST_ID,INSURER,TRANS_DATE,BATCH_ID  " +
                             ") VALUES( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "A");
            ps.setDouble(2, repairCost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setString(5, driverInvolved);
            ps.setString(6, location);
            ps.setDate(7, dateConvert(accidentDate));
            ps.setString(8, policeReportObtained);
            ps.setDate(9, dateConvert(reportDate));
            ps.setDouble(10, costBorneByCompany);
            ps.setDouble(11, costBorneByInssurance);
            ps.setInt(12, duration);
            ps.setString(13, otherDetails);
            ps.setString(14, insurranceNotified);
            ps.setDate(15, dateConvert(notificationDate));
            ps.setString(16, replaceRequired);
            ps.setString(17, userid);
            ps.setDate(18, noticeDate);
            ps.setInt(19, intFreq);
            ps.setString(20, invoiceNo);
            ps.setString(21, tranId);
            ps.setString(22, insurer);
            ps.setTimestamp(23,  dbConnection.getDateTime(new java.util.Date()));
            ps.setString(24, batchId);
            
            ps.execute();
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = 0.00d;
                double premimumPeriodToDate = 0.00d;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 1;
                double accidentCostLiveToDate = repairCost;
                double accidentCostPeriodToDate = repairCost;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating ACCIDENT_HISTORY in createAccidentRecord ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public void reverseAccidentRecord(String assetId, String registrationNo,
                                      String branchId, String deptId,
                                      String category, String assetMake,
                                      String datePurchased,
                                      String createDate, String effectiveDate,
                                      String driverInvolved, String location,
                                      String accidentDate,
                                      String policeReportObtained,
                                      String reportDate, double repairCost,
                                      double costBorneByCompany,
                                      double costBorneByInssurance,
                                      int duration,
                                      String otherDetails,
                                      String insurranceNotified,
                                      String notificationDate,
                                      String replaceRequired, String userid,
                                      String firstNoticeDate, String freq) {
        java.sql.Date noticeDate = null;
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        //if (freq == null) {
        intFreq = Integer.parseInt(freq);
        //}
        repairCost = repairCost * ( -1);
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_ACCIDENT_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                             " ,DRIVER_INVOLVED,LOCATION,ACCIDENT_DATE " +
                             " ,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE," +
                             "COST_COMPANY_BORNE " +
                             " ,COST_INSURANCE_BORNE,DURATION,OTHER_DETAILS " +
                             ",NOTIFIED_INSURANCE,NOFTIFIED_DATE,REQUIRED_REPLACEMENT, " +
                             "USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,STATUS  " +
                             ") VALUES( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, "A");
            ps.setDouble(2, repairCost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setString(5, driverInvolved);
            ps.setString(6, location);
            ps.setDate(7, dateConvert(accidentDate));
            ps.setString(8, policeReportObtained);
            ps.setDate(9, dateConvert(reportDate));
            ps.setDouble(10, costBorneByCompany);
            ps.setDouble(11, costBorneByInssurance);
            ps.setInt(12, duration);
            ps.setString(13, otherDetails);
            ps.setString(14, insurranceNotified);
            ps.setDate(15, dateConvert(notificationDate));
            ps.setString(16, replaceRequired);
            ps.setString(17, userid);
            ps.setDate(18, noticeDate);
            ps.setInt(19, intFreq);
            ps.setString(20, "REVERSED");

            ps.execute();
            int isCreated = ps.getUpdateCount();
            if (isCreated > 0) {

                String assetMaintenance = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = 0.00d;
                double premimumPeriodToDate = 0.00d;
                double maintLiveToDate = 0.00d;
                double maintPeriodToDate = 0.00d;
                double fuelLiveToDate = 0.00d;
                double fuelPeriodToDate = 0.00d;
                int accidentCount = 1;
                double accidentCostLiveToDate = repairCost;
                double accidentCostPeriodToDate = repairCost;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

                tranManager.logFleetTransaction(fleetTran);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating ACCIDENT_HISTORY in reverseAccidentRecord ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }


    public void removeAccidentRecordById(String id) {
        String query = "DELETE FROM FT_ACCIDENT_HISTORY WHERE LT_ID = " + id;
        this.excuteSQLCode(query);
    }

    public void removeFuelRecordById(String id) {
        String query = "DELETE FROM FT_FUEL_HISTORY WHERE LT_ID = " + id;
        excuteSQLCode(query);
    }

    public void removeLicenseRecordById(String id) {
        String query = "DELETE FROM FT_LICENCE_HISTORY WHERE LT_ID = " + id;
        excuteSQLCode(query);
    }

    public void removeMaintenanceRecordById(String id) {
        String query = "DELETE FROM FT_MAINTENANCE_HISTORY WHERE LT_ID = " + id;
        excuteSQLCode(query);
    }

    public void removeInssuranceRecordById(String id) {
        String query = "DELETE FROM FT_INSURANCE_HISTORY WHERE LT_ID = " + id;
        excuteSQLCode(query);

    }

    public void removeExpensesRecordById(String id) {
        String query = "DELETE FROM PR_EXPENSES_HISTORY WHERE LT_ID = " + id;
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

    /**
     * findAllMaintenaceRecord
     *
     * @return ArrayList
     */
    public ArrayList findAllMaintenaceRecord() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PERFORMED_DATE " +
                ",NEXT_PERFORMED_DATE,MAINTENANCE_TYPE,USER_ID,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT, VAT_RATE,WHT_RATE " +
                "FROM FT_MAINTENANCE_HISTORY ";
//System.out.println("selectQuery in findAllMaintenaceRecord: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                String projectCode = rs.getString("PROJECT_CODE");
                FleetManatainanceRecord fm = new FleetManatainanceRecord(id,
                        type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);

                list.add(fm);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Fecthing Maintenance Record Details ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findFleetMaintenanceRecordByQuery
     *
     * @param queryFilter String
     * @return ArrayList
     */
    public ArrayList findFleetMaintenanceRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PERFORMED_DATE " +
                ",NEXT_PERFORMED_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,VAT_RATE,WHT_RATE " +
                "FROM FT_MAINTENANCE_HISTORY " + queryFilter;
//        System.out.println("selectQuery in findFleetMaintenanceRecordByQuery: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                String projectCode = rs.getString("PROJECT_CODE");
                FleetManatainanceRecord fm = new FleetManatainanceRecord(id,
                        type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate,
                        maintenanceType, status, histId, invoiceNo, vatAmt,
                        whtAmt,projectCode,vatRate,whtRate);

                list.add(fm);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Fecthing Maintenance Record Details ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findMaintenaceRecordById
     *
     * @param id String
     * @return FleetManatainanceRecord
     */
    public FleetManatainanceRecord findMaintenaceRecordById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE,VAT_RATE,WHT_RATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT " +
                "FROM FT_MAINTENANCE_HISTORY WHERE HIST_ID = ?";
//        System.out.println("selectQuery in findMaintenaceRecordById: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                maintenanceRecord = new FleetManatainanceRecord(id, type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findMaintenaceRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public FleetManatainanceRecord findMaintenaceRecordById(String tranId,
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,VAT_RATE,WHT_RATE " +
                "FROM FT_MAINTENANCE_HISTORY WHERE HIST_ID = ?";
//        System.out.println("selectQuery in findMaintenaceRecordById2: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_AMT");
                double whtRate = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findMaintenaceRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    /**
     * findMaintenaceRecordById
     *
     * @param id String
     * @return FleetManatainanceRecord
     */
    public ArrayList findMaintenaceRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,VAT_RATE,WHT_RATE " +
                "FROM FT_MAINTENANCE_HISTORY WHERE LT_ID != ''  " + queryFilter;
//        System.out.println("<<<<selectQuery in findMaintenaceRecordByQuery====: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                String projectCode = rs.getString("PROJECT_CODE");
                maintenanceRecord = new FleetManatainanceRecord(id, type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);
                finder.add(maintenanceRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findMaintenaceRecordByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    /**
     * findAllLicencePermitRecord
     *
     * @return ArrayList
     */
    public ArrayList findLicencePermitRecordByQuery(String filter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,STATUS " +
                             ",REGISTRATION_NO,DATE_OBTAINED,EXPIRY_DATE,INVOICE_NO " +
                             "FROM FT_LICENCE_HISTORY WHERE ASSET_ID != '' " +
                             filter;
//        System.out.println("<<<<<selectQuery in findLicencePermitRecordByQuery: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String invoiceNo = rs.getString("INVOICE_NO");
                FleetLicencePermit licence = new FleetLicencePermit(id, type,
                        cost, assetCode, registrationNo, dateObtained,
                        dateExpired, status, "", "", invoiceNo);
                list.add(licence);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findLicencePermitById
     *
     * @param id String
     * @return FleetLicencePermit
     */
    public FleetLicencePermit findLicencePermitById(String id) {
        FleetLicencePermit licence = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             ",REGISTRATION_NO,LICENCE_NO,DATE_OBTAINED " +
                             ",EXPIRY_DATE,STATUS,FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO " +
                             "FROM FT_LICENCE_HISTORY WHERE BATCH_ID =?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String id = rs.getString("LICENCE_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String noteDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String freq = rs.getString("NOTIFICATION_FREQ");
                String invoiceNo = rs.getString("INVOICE_NO");
                licence = new FleetLicencePermit(id, type, cost, assetCode,
                                                 registrationNo, dateObtained,
                                                 dateExpired, status, freq,
                                                 noteDate, invoiceNo);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching license records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return licence;
    }

    public FleetLicencePermit findLicencePermitById(String tranId, String id) {
        FleetLicencePermit licence = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             ",REGISTRATION_NO,LICENCE_NO,DATE_OBTAINED " +
                             ",EXPIRY_DATE,STATUS,FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO " +
                             "FROM FT_LICENCE_HISTORY WHERE BATCH_ID =?";
        DataConnect connect = new DataConnect();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String freq = rs.getString("NOTIFICATION_FREQ");
                String noticeDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String invoiceNo = rs.getString("INVOICE_NO");

                licence = new FleetLicencePermit(ltId, type, cost, assetCode,
                                                 registrationNo, dateObtained,
                                                 dateExpired, status, freq,
                                                 noticeDate, invoiceNo);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching license records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return licence;
    }

    /**
     * findLicencePermitByRegNo
     *
     * @param registrationNo String
     * @return FleetLicencePermit
     */
    public FleetLicencePermit findLicencePermitByRegNo(String registrationNo) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             ",REGISTRATION_NO,LICENCE_NO,DATE_OBTAINED " +
                             ",EXPIRY_DATE,STATUS,INVOICE_NO " +
                             "FROM FT_LICENCE_HISTORY WHERE REGISTRATION_NO =?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, registrationNo);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String invoiceNo = rs.getString("INVOICE_NO");
                FleetLicencePermit licence = new FleetLicencePermit(id, type,
                        cost, assetCode, registrationNo, dateObtained,
                        dateExpired, status, "", "", invoiceNo);
                //list.add(licence);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error creating Budget ->" + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return null;
    }

    /**
     * findAllAccidentRecord
     *
     * @return ArrayList
     */
    public ArrayList findAccidentRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             " ,REGISTRATION_NO,DRIVER_INVOLVED,LOCATION " +
                             ",ACCIDENT_DATE,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE " +
                             " ,COST_COMPANY_BORNE,COST_INSURANCE_BORNE " +
                             " ,DURATION,OTHER_DETAILS,NOTIFIED_INSURANCE " +
                             " ,NOFTIFIED_DATE,REQUIRED_REPLACEMENT,STATUS,INVOICE_NO " +
                             "FROM FT_ACCIDENT_HISTORY   " +
                             "WHERE ASSET_ID != '' " + queryFilter;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String driverInvolved = rs.getString("DRIVER_INVOLVED");
                String location = rs.getString("LOCATION");
                String accidentDate = formatDate(rs.getDate("ACCIDENT_DATE"));
                String report = rs.getString("POLICE_REPORT_OBTAINED");
                boolean policeReportObtained = ((report == null) ? true : false);
                String reportDate = formatDate(rs.getDate("POLICE_REPORT_DATE"));
                double costBorneByCompany = rs.getDouble("COST_COMPANY_BORNE");
                double costBorneByInssurance = rs.getDouble(
                        "COST_INSURANCE_BORNE");
                int duration = rs.getInt("DURATION");
                String otherDetails = rs.getString("OTHER_DETAILS");
                String ins = rs.getString("NOTIFIED_INSURANCE");
                boolean issuranceNotified = ((ins == null) ? true : false);
                String notificationDate = formatDate(rs.getDate(
                        "NOFTIFIED_DATE"));
                String rep = rs.getString("REQUIRED_REPLACEMENT");
                boolean replaceRequired = ((rep == null) ? true : false);
                String status = rs.getString("STATUS");
                String invoiceNo = rs.getString("INVOICE_NO");
                FleetAccidentRecord accident = new FleetAccidentRecord(id, type,
                        cost,
                        assetCode, registrationNo, driverInvolved, location,
                        accidentDate, policeReportObtained, reportDate,
                        costBorneByCompany, costBorneByInssurance, duration,
                        otherDetails, issuranceNotified,
                        notificationDate, replaceRequired, status, invoiceNo,
                        "");
                list.add(accident);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Accident Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }


    /**
     * findAllAccidentRecord
     *
     * @return ArrayList
     */
    public ArrayList findAllAccidentRecord() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             " ,REGISTRATION_NO,DRIVER_INVOLVED,LOCATION " +
                             ",ACCIDENT_DATE,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE " +
                             " ,COST_COMPANY_BORNE,COST_INSURANCE_BORNE " +
                             " ,DURATION,OTHER_DETAILS,NOTIFIED_INSURANCE " +
                             " ,NOFTIFIED_DATE,REQUIRED_REPLACEMENT,STATUS " +
                             "FROM FT_ACCIDENT_HISTORY ";

        ;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ACCIDENT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String driverInvolved = rs.getString("DRIVER_INVOLVED");
                String location = rs.getString("LOCATION");
                String accidentDate = formatDate(rs.getDate("ACCIDENT_DATE"));
                String report = rs.getString("POLICE_REPORT_OBTAINED");
                boolean policeReportObtained = ((report == null) ? true : false);
                String reportDate = formatDate(rs.getDate("POLICE_REPORT_DATE"));
                double costBorneByCompany = rs.getDouble("COST_COMPANY_BORNE");
                double costBorneByInssurance = rs.getDouble(
                        "COST_INSURANCE_BORNE");
                int duration = rs.getInt("DURATION");
                String otherDetails = rs.getString("OTHER_DETAILS");
                String ins = rs.getString("NOTIFIED_INSURANCE");
                boolean issuranceNotified = ((ins == null) ? true : false);
                String notificationDate = formatDate(rs.getDate(
                        "NOFTIFIED_DATE"));
                String rep = rs.getString("REQUIRED_REPLACEMENT");
                boolean replaceRequired = ((rep == null) ? true : false);
                String status = rs.getString("STATUS");
                FleetAccidentRecord accident = new FleetAccidentRecord(id, type,
                        cost,
                        assetCode, registrationNo, driverInvolved, location,
                        accidentDate, policeReportObtained, reportDate,
                        costBorneByCompany, costBorneByInssurance, duration,
                        otherDetails, issuranceNotified,
                        notificationDate, replaceRequired, status, "", "");
                list.add(accident);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching accident history ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findAccidentRecordByRegCode
     *
     * @param registrationNo String
     * @return FleetAccidentRecord
     */
    public FleetAccidentRecord findAccidentRecordById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             " ,REGISTRATION_NO,DRIVER_INVOLVED,LOCATION " +
                             ",ACCIDENT_DATE,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE " +
                             " ,COST_COMPANY_BORNE,COST_INSURANCE_BORNE " +
                             " ,DURATION,OTHER_DETAILS,NOTIFIED_INSURANCE " +
                             " ,NOFTIFIED_DATE,REQUIRED_REPLACEMENT,STATUS,INVOICE_NO,INSURER " +
                             "FROM FT_ACCIDENT_HISTORY WHERE BATCH_ID = '" + id +
                             "'";

        FleetAccidentRecord accidentRecord = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                //String id = rs.getString("ACCIDENT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String driverInvolved = rs.getString("DRIVER_INVOLVED");
                String location = rs.getString("LOCATION");
                String accidentDate = formatDate(rs.getDate("ACCIDENT_DATE"));
                String report = rs.getString("POLICE_REPORT_OBTAINED");
                boolean policeReportObtained = ((report == null) ? true : false);
                String reportDate = formatDate(rs.getDate("POLICE_REPORT_DATE"));
                double costBorneByCompany = rs.getDouble("COST_COMPANY_BORNE");
                double costBorneByInssurance = rs.getDouble(
                        "COST_INSURANCE_BORNE");
                int duration = rs.getInt("DURATION");
                String otherDetails = rs.getString("OTHER_DETAILS");
                String ins = rs.getString("NOTIFIED_INSURANCE");
                boolean issuranceNotified = ((ins == null) ? true : false);
                String notificationDate = formatDate(rs.getDate(
                        "NOFTIFIED_DATE"));
                String rep = rs.getString("REQUIRED_REPLACEMENT");
                boolean replaceRequired = ((rep == null) ? true : false);
                String status = rs.getString("STATUS");
                String invoiceNo = rs.getString("INVOICE_NO");
                String insurer = rs.getString("INSURER");

                accidentRecord = new FleetAccidentRecord(id, type, cost,
                        assetCode, registrationNo, driverInvolved, location,
                        accidentDate, policeReportObtained, reportDate,
                        costBorneByCompany, costBorneByInssurance, duration,
                        otherDetails, issuranceNotified, notificationDate,
                        replaceRequired, status, invoiceNo, insurer);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fecthing accidents ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return accidentRecord;
    }

    public FleetAccidentRecord findAccidentRecordById(String tranId, String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID " +
                             " ,REGISTRATION_NO,DRIVER_INVOLVED,LOCATION " +
                             ",ACCIDENT_DATE,POLICE_REPORT_OBTAINED,POLICE_REPORT_DATE " +
                             " ,COST_COMPANY_BORNE,COST_INSURANCE_BORNE " +
                             " ,DURATION,OTHER_DETAILS,NOTIFIED_INSURANCE " +
                             " ,NOFTIFIED_DATE,REQUIRED_REPLACEMENT,STATUS,INVOICE_NO,INSURER " +
                             "FROM FT_ACCIDENT_HISTORY WHERE BATCH_ID = '" +
                             tranId +
                             "'";

        FleetAccidentRecord accidentRecord = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String driverInvolved = rs.getString("DRIVER_INVOLVED");
                String location = rs.getString("LOCATION");
                String accidentDate = formatDate(rs.getDate("ACCIDENT_DATE"));
                String report = rs.getString("POLICE_REPORT_OBTAINED");
                boolean policeReportObtained = ((report == null) ? true : false);
                String reportDate = formatDate(rs.getDate("POLICE_REPORT_DATE"));
                double costBorneByCompany = rs.getDouble("COST_COMPANY_BORNE");
                double costBorneByInssurance = rs.getDouble(
                        "COST_INSURANCE_BORNE");
                int duration = rs.getInt("DURATION");
                String otherDetails = rs.getString("OTHER_DETAILS");
                String ins = rs.getString("NOTIFIED_INSURANCE");
                boolean issuranceNotified = ((ins == null) ? true : false);
                String notificationDate = formatDate(rs.getDate(
                        "NOFTIFIED_DATE"));
                String rep = rs.getString("REQUIRED_REPLACEMENT");
                boolean replaceRequired = ((rep == null) ? true : false);
                String status = rs.getString("STATUS");
                String invoiceNo = rs.getString("INVOICE_NO");
                String insurer = rs.getString("INSURER");

                accidentRecord = new FleetAccidentRecord(ltId, type, cost,
                        assetCode, registrationNo, driverInvolved, location,
                        accidentDate, policeReportObtained, reportDate,
                        costBorneByCompany, costBorneByInssurance, duration,
                        otherDetails, issuranceNotified, notificationDate,
                        replaceRequired, status, invoiceNo, insurer);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fecthing accidents ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return accidentRecord;
    }


    /**
     * findAccidentRecordByRegCode
     *
     * @param registrationNo String
     * @return FleetAccidentRecord
     */
    public FleetInsurranceRecord findInsurranceRecordById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                " ,COMPANY,DATE_OBTAINED,EXPIRY_DATE,STATUS,HIST_ID,PROJECT_CODE " +
                ",FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,RAISED_ENTRY " +
                " FROM FT_INSURANCE_HISTORY WHERE LT_ID = ?";
//System.out.println("<<<<<<<findInsurranceRecordById selectQuery: "+selectQuery);
        FleetInsurranceRecord insRecord = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String company = rs.getString("COMPANY");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String tranId = rs.getString("HIST_ID");
                String ltId = rs.getString("LT_ID");
                String noteDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String freq = rs.getString("NOTIFICATION_FREQ");
                String invoiceNo = rs.getString("INVOICE_NO");
                String raised_Entry = rs.getString("RAISED_ENTRY");
                String projectCode = rs.getString("PROJECT_CODE");
                insRecord = new FleetInsurranceRecord(id, type, cost,
                        assetCode, registrationNo, company, dateObtained,
                        dateExpired, status, tranId, noteDate, freq, invoiceNo);
                insRecord.setHistId(tranId);
                insRecord.setLtId(ltId);
                insRecord.setRaised_Entry(raised_Entry);
                insRecord.setProjectCode(projectCode);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching insurance record in findInsurranceRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return insRecord;
    }

    //added by Olabo
    public FleetInsurranceRecord findInsurranceRecordById(String tranId,
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                " ,COMPANY,DATE_OBTAINED,EXPIRY_DATE,STATUS,HIST_ID " +
                ",FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,PROJECT_CODE " +
                " FROM FT_INSURANCE_HISTORY WHERE HIST_ID = ?";

        FleetInsurranceRecord insRecord = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String company = rs.getString("COMPANY");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String noteDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String freq = rs.getString("NOTIFICATION_FREQ");
                String invoiceNo = rs.getString("INVOICE_NO");
                String projectCode = rs.getString("PROJECT_CODE");

                insRecord = new FleetInsurranceRecord(ltId, type, cost,
                        assetCode, registrationNo, company, dateObtained,
                        dateExpired, status, tranId, noteDate, freq, invoiceNo);
                insRecord.setProjectCode(projectCode);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching insurance record in findInsurranceRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return insRecord;
    }

    /**
     * findInsuranceRecordByQuery
     *
     * @param queryFilter String
     * @return ArrayList
     */
    public ArrayList findInsuranceRecordByQuery(String queryFilter) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO, PROJECT_CODE " +
                " ,COMPANY,DATE_OBTAINED,EXPIRY_DATE,STATUS,HIST_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ " +
                " FROM FT_INSURANCE_HISTORY WHERE LT_ID !='' " + queryFilter;

        ArrayList list = new ArrayList();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String company = rs.getString("COMPANY");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String tranId = rs.getString("HIST_ID");
                String noteDate = rs.getString("FIRST_NOT_DATE");
                String freq = rs.getString("NOTIFICATION_FREQ");
                String projectCode = rs.getString("PROJECT_CODE");
                FleetInsurranceRecord fir = new FleetInsurranceRecord(id, type,
                        cost,
                        assetCode, registrationNo, company, dateObtained,
                        dateExpired, status, tranId, noteDate, freq, "");
                fir.setProjectCode(projectCode);

                list.add(fir);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Insurance Record ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllInsurrancetRecord
     * @return ArrayList
     */
    public ArrayList findAllInsurranceRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT a.LT_ID,a.TYPE,a.COST_PRICE,a.ASSET_ID,a.REGISTRATION_NO,a.PROJECT_CODE " +
                " ,b.INSURANCE_NAME AS COMPANY,a.DATE_OBTAINED,a.EXPIRY_DATE,a.STATUS,a.INVOICE_NO " +
                " FROM FT_INSURANCE_HISTORY a,AM_AD_INSURANCE b WHERE a.COMPANY = b.INSURANCE_ID AND a.ASSET_ID != '' " +
                queryFilter;

        ArrayList list = new ArrayList();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String company = rs.getString("COMPANY");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                //String tranId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                String projectCode = rs.getString("PROJECT_CODE");
                FleetInsurranceRecord fir = new FleetInsurranceRecord(id, type,
                        cost, assetCode, registrationNo, company, dateObtained,
                        dateExpired, status, "", "", "", invoiceNo);
                		fir.setProjectCode(projectCode);

                list.add(fir);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all Insurance Record ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findAllFuelRecords
     *
     * @return ArrayList
     */
    public ArrayList findFuelRecordsByQuery(String filter) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE, " +
                "VOLUME,FUEL_TYPE,INVOICE_NO,EFFECTIVE_DATE,ENTRY_DATE,STATUS,UNIT_PRICE  " +
                "FROM FT_FUEL_HISTORY WHERE ASSET_ID != ''  " + filter;

        ArrayList list = new ArrayList();
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                double volume = rs.getDouble("VOLUME");
                String fuelType = rs.getString("FUEL_TYPE");
                String invoice = rs.getString("INVOICE_NO");
                String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String entryDate = formatDate(rs.getDate("ENTRY_DATE"));
                String status = rs.getString("STATUS");
                double unitPrice = rs.getDouble("UNIT_PRICE");
                String projectCode = rs.getString("PROJECT_CODE");
                FleetFuelRecord fuelRecord = new FleetFuelRecord(id, type, cost,
                        assetCode, registrationNo, volume, fuelType, invoice,
                        effectiveDate, entryDate, status, unitPrice);
                fuelRecord.setProjectCode(projectCode);
                list.add(fuelRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Fuel ALL Records  ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    /**
     * findFuelRecordById
     *
     * @param id String
     * @return FleetFuelRecord
     */
    public FleetFuelRecord findFuelRecordById(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetFuelRecord fuelRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE," +
                "VOLUME,FUEL_TYPE,INVOICE_NO,EFFECTIVE_DATE,ENTRY_DATE,STATUS,UNIT_PRICE, VENDOR_ID  " +
                "FROM FT_FUEL_HISTORY WHERE BATCH_ID = '" + id + "'";
      
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                //String id = rs.getString("MT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                double volume = rs.getDouble("VOLUME");
                String fuelType = rs.getString("FUEL_TYPE");
                String invoiceNo = rs.getString("INVOICE_NO");
                String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String entryDate = formatDate(rs.getDate("ENTRY_DATE"));
                String status = rs.getString("STATUS");
                double unitPrice = rs.getDouble("UNIT_PRICE");
                String projectCode = rs.getString("PROJECT_CODE");
                String vendorId = rs.getString("VENDOR_ID");
                fuelRecord = new FleetFuelRecord(id, type, cost, assetCode,
                                                 registrationNo, volume,
                                                 fuelType, invoiceNo,
                                                 effectiveDate, entryDate,
                                                 status, unitPrice);
                fuelRecord.setProjectCode(projectCode);
                fuelRecord.setVendorId(vendorId);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Fuel Record By ID in FleetFuelRecord ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return fuelRecord;
    }

    //added by Olabo
    public FleetFuelRecord findFuelRecordById(String tranId, String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetFuelRecord fuelRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE," +
                "VOLUME,FUEL_TYPE,INVOICE_NO,EFFECTIVE_DATE,ENTRY_DATE,STATUS,UNIT_PRICE  " +
                "FROM FT_FUEL_HISTORY WHERE BATCH_ID = '" + tranId + "'";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                double volume = rs.getDouble("VOLUME");
                String fuelType = rs.getString("FUEL_TYPE");
                String invoiceNo = rs.getString("INVOICE_NO");
                String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
                String entryDate = formatDate(rs.getDate("ENTRY_DATE"));
                String status = rs.getString("STATUS");
                double unitPrice = rs.getDouble("UNIT_PRICE");
                String projectCode = rs.getString("PROJECT_CODE");
                fuelRecord = new FleetFuelRecord(ltId, type, cost, assetCode,
                                                 registrationNo, volume,
                                                 fuelType, invoiceNo,
                                                 effectiveDate, entryDate,
                                                 status, unitPrice);
                fuelRecord.setProjectCode(projectCode);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Fuel Record By ID in findFuelRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return fuelRecord;
    }

    
    public void createAsset(String assetid, String regno, String branchId,
                            String deptId,
                            String sectionId, String categoryId, String descr,
                            String vendorAccount, String datePurchased,
                            String depRate, String assetMake, String assetModel,
                            String serialNo, String engineNo,
                            String supplierName, String assetUser,
                            String maintenance, String accumDep, String monDep,
                            String costPrice, String nbv, String depEnddate,
                            String residualVal, String authorizedBy,
                            String postDate, String effDate,
                            String purchaseReason, String usefulLife,
                            String totalLife,
                            String location, String remLife, String vatCost,
                            String vat, String whTax, String whTaxAmount,
                            String reqDep, String reqDist, String subToTax,
                            String toRemind1, String email1, String toRemind2,
                            String email2, String raiseEntry, String depYTD,
                            String section, String state, String driver,
                            String spare1, String spare2, String status,
                            String userId) {

        if (datePurchased.equals("")) {
            datePurchased = null;
        }
        if (depRate.equals("")) {
            depRate = "0.00";
        }
        if (assetMake.equals("")) {
            assetMake = "0";
        }
        if (supplierName.equals("")) {
            supplierName = "0";
        }
        if (accumDep.equals("")) {
            accumDep = "0.00";
        }
        if (monDep.equals("")) {
            monDep = "0.00";
        }
        if (costPrice.equals("")) {
            costPrice = "0.00";
        }
        if (nbv.equals("")) {
            nbv = "0.00";
        }
        if (depEnddate.equals("")) {
            depEnddate = null;
        }
        if (residualVal.equals("")) {
            residualVal = "0.00";
        }
        if (postDate.equals("")) {
            postDate = null;
        }
        if (effDate.equals("")) {
            effDate = null;
        }
        if (usefulLife.equals("")) {
            usefulLife = "0";
        }
        if (totalLife.equals("")) {
            totalLife = "0";
        }
        if (location.equals("")) {
            location = "0";
        }
        if (remLife.equals("")) {
            remLife = "0";
        }
        if (vatCost.equals("")) {
            vatCost = "0";
        }
        if (whTaxAmount.equals("")) {
            whTaxAmount = "0.00";
        }
        if (depYTD.equals("")) {
            depYTD = "0.00";
        }
        accumDep = accumDep.replaceAll(",", "");
        monDep = monDep.replaceAll(",", "");
        costPrice = costPrice.replaceAll(",", "");
        nbv = nbv.replaceAll(",", "");
        residualVal = residualVal.replaceAll(",", "");
        vatCost = vatCost.replaceAll(",", "");
        whTaxAmount = whTaxAmount.replaceAll(",", "");
        depYTD = depYTD.replaceAll(",", "");

        String CREATE_QUERY =
                "INSERT INTO AM_ASSET" +
                "(" +
                "ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "SECTION_ID,CATEGORY_ID,DESCRIPTION,VENDOR_AC," +
                "DATE_PURCHASED,DEP_RATE,ASSET_MAKE," +
                "ASSET_MODEL,ASSET_SERIAL_NO,ASSET_ENGINE_NO," +
                "SUPPLIER_NAME,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,NBV," +
                "DEP_END_DATE,RESIDUAL_VALUE,AUTHORIZED_BY," +
                "POSTING_DATE,EFFECTIVE_DATE,PURCHASE_REASON," +
                "USEFUL_LIFE,TOTAL_LIFE,LOCATION,REMAINING_LIFE," +
                "VATABLE_COST,VAT,WH_TAX,WH_TAX_AMOUNT," +
                "REQ_DEPRECIATION,REQ_REDISTRIBUTION," +
                "SUBJECT_TO_VAT,WHO_TO_REM,EMAIL1,WHO_TO_REM_2," +
                "EMAIL2,RAISE_ENTRY,DEP_YTD,SECTION," +
                "STATE,DRIVER,SPARE_1,SPARE_2,ASSET_STATUS," +
                "USER_ID" +
                ") VALUES(" +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?" +
                ")";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (assetid == null || assetid.equals("")) {
            try {

                new legend.ConnectionClass().getIdentity().substring(24);

            } catch (Exception er) {
                System.out.println("WARN: Error Creating assetid " + er);
            }
        }

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(CREATE_QUERY);

            ps.setString(1, assetid);
            ps.setString(2, regno);
            ps.setInt(3, Integer.parseInt(branchId));
            ps.setInt(4, Integer.parseInt(deptId));
            ps.setInt(5, Integer.parseInt(sectionId));
            ps.setInt(6, Integer.parseInt(categoryId));
            ps.setString(7, descr);
            ps.setString(8, vendorAccount);
            ps.setDate(9, dateConvert(datePurchased));
            ps.setDouble(10, Double.parseDouble(depRate));
            ps.setInt(11, Integer.parseInt(assetMake));
            ps.setString(12, assetModel);
            ps.setString(13, serialNo);
            ps.setString(14, engineNo);
            ps.setInt(15, Integer.parseInt(supplierName));
            ps.setString(16, assetUser);
            ps.setInt(17, Integer.parseInt(maintenance));
            ps.setDouble(18, Double.parseDouble(accumDep));
            ps.setDouble(19, Double.parseDouble(monDep));
            ps.setDouble(20, Double.parseDouble(costPrice));
            ps.setDouble(21, Double.parseDouble(nbv));
            ps.setDate(22, dateConvert(depEnddate));
            ps.setDouble(23, Double.parseDouble(residualVal));
            ps.setString(24, authorizedBy);
            ps.setDate(25, dateConvert(postDate));
            ps.setDate(26, dateConvert(effDate));
            ps.setString(27, purchaseReason);
            ps.setInt(28, Integer.parseInt(usefulLife));
            ps.setInt(29, Integer.parseInt(totalLife));
            ps.setInt(30, Integer.parseInt(location));
            ps.setInt(31, Integer.parseInt(remLife));
            ps.setDouble(32, Double.parseDouble(vatCost));
            ps.setString(33, vat);
            ps.setString(34, whTax);
            ps.setDouble(35, Double.parseDouble(whTaxAmount));
            ps.setString(36, reqDep);
            ps.setString(37, reqDist);
            ps.setString(38, subToTax);
            ps.setString(39, toRemind1);
            ps.setString(40, email1);
            ps.setString(41, toRemind2);
            ps.setString(42, email2);
            ps.setString(43, raiseEntry);
            ps.setDouble(44, Double.parseDouble(depYTD));
            ps.setString(45, section);
            ps.setInt(46, Integer.parseInt(state));
            ps.setInt(47, Integer.parseInt(driver));
            ps.setString(48, spare1);
            ps.setString(49, spare2);
            ps.setString(50, status);
            ps.setInt(51, Integer.parseInt(userId));

 //           System.out.println(ps.toString());

            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error creating Asset ->" +
                               e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps, rs);
        }
    }


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAllAsset() {

        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET  ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAllAsset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findAssetForDepreciation() {

        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION_ID , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE, Asset_Status,REQ_REDISTRIBUTION   " +
                "FROM AM_ASSET  WHERE Req_Depreciation = 'Y'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION_ID");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String astatus = rs.getString("Asset_Status");
                String req_distribution = rs.getString("REQ_REDISTRIBUTION");

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(astatus);
                aset.setReq_distribution(req_distribution);

                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetForDepreciation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetByCategory(String categoryCode) {

        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET WHERE CATEGORY_ID = ? ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, categoryCode);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByCategory ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAssetByMakeCategory
     *
     * @param categoryCode String
     * @param makeCode String
     * @return ArrayList
     */
    public ArrayList findAssetByMakeCategory(String categoryCode,
                                             String makeCode) {

        String selectQuery =
                "SELECT A.ASSET_ID,A.REGISTRATION_NO,A.BRANCH_ID,A.DEPT_ID," +
                "A.CATEGORY_ID, A.DESCRIPTION,A.DATE_PURCHASED,A.DEP_RATE," +
                "A.ASSET_MAKE,A.ASSET_USER,A.ASSET_MAINTENANCE," +
                "A.ACCUM_DEP,A.MONTHLY_DEP,A.COST_PRICE,A.DEP_END_DATE," +
                "A.RESIDUAL_VALUE,A.POSTING_DATE,A.RAISE_ENTRY,A.DEP_YTD,A.SECTION , " +
                "A.NBV,A.REMAINING_LIFE,A.TOTAL_LIFE,A.EFFECTIVE_DATE,   " +
                "C.REQUIRED_FOR_FLEET " +
                "FROM AM_ASSET A,AM_AD_CATEGORY C " +
                "WHERE A.CATEGORY_ID = C.CATEGORY_ID " +
                " AND A.CATEGORY_ID = ? AND A.ASSET_MAKE = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, Integer.parseInt(categoryCode));
            ps.setInt(2, Integer.parseInt(makeCode));
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String isFleetEnabled = rs.getString("REQUIRED_FOR_FLEET");

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setIsFleetEnabled(isFleetEnabled);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error fetching ALL Asset By Make Category\n ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAssetById
     *
     * @param id String
     * @return Asset
     */
    public Asset findAssetById(String id) {

        Asset finder = null;
        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION," +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REQ_REDISTRIBUTION   " +
                "FROM AM_ASSET  WHERE ASSET_ID = ?";
//        System.out.println("<<<<<<<selectQuery in findAssetById: "+selectQuery+"         Asset Id: "+id);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = formatDate(rs.getDate("POSTING_DATE"));
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String req_distribution = rs.getString("REQ_REDISTRIBUTION");
                finder = new Asset(id, registrationNo, branchId,
                                   departmentId, section, category,
                                   description,
                                   datePurchased, depreciationRate,
                                   assetMake,
                                   assetUser, assetMaintenance,
                                   accumulatedDepreciation,
                                   monthlyDepreciation, cost,
                                   depreciationEndDate,
                                   residualValue, postingDate, entryRaised,
                                   depreciationYearToDate);
                finder.setNbv(nbv);
                finder.setRemainLife(remainingLife);
                finder.setTotalLife(totalLife);
                finder.setEffectiveDate(effectiveDate);
                finder.setReq_distribution(req_distribution);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching assetDetail ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;
    }

    /**
     * findAssetById
     *
     * @param id String
     * @return Asset
     */
    public Asset findAssetFleetById(String id) {

        Asset finder = null;
        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION," +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REQ_REDISTRIBUTION   " +
                "FROM AM_ASSET  WHERE ASSET_ID = ?" +
                " UNION "+
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION," +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REQ_REDISTRIBUTION   " +
                "FROM AM_ASSET2  WHERE ASSET_ID = ?";
        System.out.println("<<<<<<<selectQuery in findAssetFleetById: "+selectQuery+"         Asset Id: "+id);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            ps.setString(2, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = formatDate(rs.getDate("POSTING_DATE"));
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String req_distribution = rs.getString("REQ_REDISTRIBUTION");
                finder = new Asset(id, registrationNo, branchId,
                                   departmentId, section, category,
                                   description,
                                   datePurchased, depreciationRate,
                                   assetMake,
                                   assetUser, assetMaintenance,
                                   accumulatedDepreciation,
                                   monthlyDepreciation, cost,
                                   depreciationEndDate,
                                   residualValue, postingDate, entryRaised,
                                   depreciationYearToDate);
                finder.setNbv(nbv);
                finder.setRemainLife(remainingLife);
                finder.setTotalLife(totalLife);
                finder.setEffectiveDate(effectiveDate);
                finder.setReq_distribution(req_distribution);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching assetDetail ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;
    }


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
        String selectQuery_old = 
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID NOT IN (SELECT ASSET_ID FROM am_assetReclassification) " +
                "AND ASSET_ID IS NOT NULL ?";
  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/*
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+
        queryFilter;
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !=(?)";
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, queryFilter);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQuery First ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetByQueryFleet(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
        String selectQuery_old =  
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ? " +
        		" UNION "+
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ? ";
  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/* 
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
        String selectQuery = "select 'ASSET1' AS TableType, * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+ 
        					  queryFilter+
        					  " UNION "+
        				      "select 'ASSET2' AS TableType, * from am_asset2 where Asset_id !='' "+
        queryFilter;
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !=(?)";
//       System.out.println("<<<<<<<selectQuery in findAssetByQueryFleet: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQueryFleet: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, queryFilter);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String tableType = rs.getString("TableType");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setItemType(tableType);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQueryFleet->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetByQuery(String queryFilter,String branch,String deptCode,String categoryId,
    		String registrationNumber,String assetId,String user_name,String from_price,String to_price,String dFromDate,
    		String dToDate,String status) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
        String selectQuery_old = 
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ?";
  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/*
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+
//        queryFilter;
       String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+queryFilter;
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery.toString());
            if(queryFilter.contains("ASSET_STATUS")){
          	  ps.setString(1, status);
            }    
          if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("ASSET_ID") && !queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID")){  
//        	  System.out.println("<========getAssetByQuery=======>0 asset_Id: "+assetId);
        	  ps.setString(1, status);
        	  ps.setString(2, assetId);
        	  ps.setString(3, assetId);
          }     
          if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("BRANCH_ID") && !queryFilter.contains("asset_user") && !queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>1 branch_Id: "+branch+"    status: "+status);
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
          }       
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID")){
 //       	  System.out.println("<========getAssetByQuery=======>2");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>3");
        	  ps.setString(1, status);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("REGISTRATION_NO") && queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>4");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
        	  ps.setString(5, registrationNumber);
        	  ps.setString(6, assetId);
          }  
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("ASSET_ID")){
 //       	  System.out.println("<========getAssetByQuery=======>5");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
        	  ps.setString(5, dFromDate);
        	  ps.setString(6, dToDate);
          }  
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("CATEGORY_ID")){
 //       	  System.out.println("<========getAssetByQuery=======>6");
        	  ps.setString(1, branch);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
        	  ps.setString(4, dFromDate);
        	  ps.setString(5, dToDate);
          }          
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>7");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, dFromDate);
        	  ps.setString(4, dToDate);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>8");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, assetId);
        	  ps.setString(4, assetId);
          } 
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>9");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, assetId);
          } 
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>3");
        	  ps.setString(1, status);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
          }   
          if(queryFilter.contains("DATE_PURCHASED")){
//        	  System.out.println("<========getAssetByQuery=======>7");
        	  ps.setString(3, dFromDate);
        	  ps.setString(4, dToDate);
          }    
          if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("CATEGORY_ID")  && queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED")){
//        	  System.out.println("<========getAssetByQuery=======>7");
        	  ps.setString(1, status);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, dFromDate);
        	  ps.setString(5, dToDate);
          }            
            rs = ps.executeQuery();  

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQuery second ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
//    public ArrayList findAssetByQuery(String queryFilter,String branch,String deptCode,String categoryId,
//    		String registrationNumber,String assetId,String user_name,String from_price,String to_price,String dFromDate,
//    		String dToDate,String status,String userId, String barCode) {
////       System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
//        double minCost =0;
//        double maxCost =0;
//        Statement stmt = null;
//        CallableStatement cstmt = null;
//        String selectQuery_old = 
//                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
//                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
//                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
//                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
//                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
//                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
//                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ?";
//  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
///*
//         String selectQuery =
//                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
//                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
//                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
//                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
//                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
//                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
//                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
//                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
//        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
//  */      
////        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+
////        queryFilter;
//       String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+queryFilter;
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
//        Connection con = null;
//        PreparedStatement ps = null;
//
//        ResultSet rs = null;
//        ArrayList list = new ArrayList();
//        
//        try {
//            con = getConnection("legendPlus");
//            ps = con.prepareStatement(selectQuery.toString());
//            if(queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("DEPT_ID") && !queryFilter.contains("BRANCH_ID")){  
////          	  System.out.println("<========getAssetByQuery=======>00 status: "+status);
//          	  ps.setString(1, status);
//            }            
//          if(queryFilter.contains("USER_ID") && queryFilter.contains("BRANCH_ID") && !queryFilter.contains("ASSET_STATUS")){  
////        	  System.out.println("<========getAssetByQuery=======>0 status: "+status);
//        	  ps.setString(1, userId);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, status);
//          }     
//          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>1 branch_Id: "+branch+"    status: "+status);
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, status);
//          }       
//          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("asset_user") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>2");
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, user_name);
//        	  ps.setString(4, status);
//          }   
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, status);
//        	  ps.setString(2, categoryId);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, deptCode);
//          }   
//          if(queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>4");
//        	  ps.setString(1, categoryId);
//        	  ps.setString(2, deptCode);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, status);
//          }  
//          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>5");
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, assetId);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, status); 
//          }  
//          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>6");
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, dFromDate);
//        	  ps.setString(4, dToDate);
//        	  ps.setString(5, status);
//          }          
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>7");
//        	  ps.setString(1, status);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, dFromDate);
//        	  ps.setString(4, dToDate);
//          }   
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>8");
//        	  ps.setString(1, status);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, assetId);
//        	  ps.setString(4, assetId);
//          } 
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("ASSET_ID")){
////        	  System.out.println("<========getAssetByQuery=======>9");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, categoryId);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, assetId);
//          } 
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, branch);
//        	  ps.setString(2, status);
//          }     
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS") && queryFilter.contains("DEPT_ID")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, deptCode);        	  
//        	  ps.setString(2, branch);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, status);
//          }           
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS") && queryFilter.contains("DEPT_ID")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, categoryId);
//        	  ps.setString(2, deptCode);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, branch);
//        	  ps.setString(5, status);
//          }     
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS") && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_ID ")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, assetId);
//        	  ps.setString(3, branch);
//        	  ps.setString(4, branch);
//        	  ps.setString(5, status);
//          }                       
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")  && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_ID ") && queryFilter.contains("CATEGORY_ID  ")){
////        	  System.out.println("<========getAssetByQuery=======>3");
//        	  ps.setString(1, categoryId);
//        	  ps.setString(2, deptCode);
//        	  ps.setString(3, assetId);
//        	  ps.setString(4, branch);
//        	  ps.setString(5, branch);
//        	  ps.setString(6, status);
//          }                       
//          
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//
//                String id = rs.getString("ASSET_ID");
//                String registrationNo = rs.getString("REGISTRATION_NO");
//                String branchId = rs.getString("BRANCH_ID");
//                String departmentId = rs.getString("DEPT_ID");
//                String section = rs.getString("SECTION");
//                String category = rs.getString("CATEGORY_ID");
//                String description = rs.getString("DESCRIPTION");
//                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
//                double depreciationRate = rs.getDouble("DEP_RATE");
//                String assetMake = rs.getString("ASSET_MAKE");
//                String assetUser = rs.getString("ASSET_USER");
//                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
//                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
//                double cost = rs.getDouble("COST_PRICE");
//                String depreciationEndDate = formatDate(rs.getDate(
//                        "DEP_END_DATE"));
//                double residualValue = rs.getDouble("RESIDUAL_VALUE");
//                String postingDate = rs.getString("POSTING_DATE");
//                String entryRaised = rs.getString("RAISE_ENTRY");
//                double depreciationYearToDate = rs.getDouble("DEP_YTD");
//                double nbv = rs.getDouble("NBV");
//                double revalue_cost = rs.getDouble("REVALUE_COST");
//                int remainingLife = rs.getInt("REMAINING_LIFE");
//                int totalLife = rs.getInt("TOTAL_LIFE");
//                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
//                String asset_status=rs.getString("ASSET_STATUS");
//                int assetCode = rs.getInt("asset_code");
//                double improvcost = rs.getDouble("IMPROV_COST");
//                double improvnbv = rs.getDouble("IMPROV_NBV");
//                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
//                
//                Asset aset = new Asset(id, registrationNo, branchId,
//                                       departmentId, section, category,
//                                       description,
//                                       datePurchased, depreciationRate,
//                                       assetMake,
//                                       assetUser, assetMaintenance,
//                                       accumulatedDepreciation,
//                                       monthlyDepreciation, cost,
//                                       depreciationEndDate,
//                                       residualValue, postingDate, entryRaised,
//                                       depreciationYearToDate);
//                aset.setNbv(nbv);
//                aset.setRemainLife(remainingLife);
//                aset.setTotalLife(totalLife);
//                aset.setEffectiveDate(effectiveDate);
//                aset.setAsset_status(asset_status);
//                aset.setAssetCode(assetCode);
//                aset.setRevalue_cost(revalue_cost);
//                aset.setImprovcost(improvcost);
//                aset.setImprovnbv(improvnbv);
//                aset.setImprovaccumulatedDepreciation(improveAccumdep);
//                aset.setImprovmonthlyDepreciation(improveMonthlydep);
//                aset.setImprovTotalNbv(improveTotalNbv);
//                
//                list.add(aset);
//
//                minCost =Math.min(minCost, cost);
//                maxCost = Math.max(maxCost, cost);
//
//
//                //getMinMaxAssetCost(minCost,maxCost);
//                setMinCost(minCost);
//                setMaxCost(maxCost);
//            }
//
//        } catch (Exception e) {
//            System.out.println("INFO:Error fetching ALL Asset in findAssetByQuery Third ->" +
//                               e.getMessage());
//        } finally {
//            closeConnection(con, ps, rs);
//        }
//
//        return list;
//
//    }
//

    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetFacilityByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
 
        String selectQuery_old = 
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/*
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
//        String selectQuery = "select *from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' " +
//        					queryFilter;
        String selectQuery = "select *from am_asset where Asset_id !='' " +
		queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetFacilityByQuery: "+selectQuery);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetFacilityByQuery->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAccidentImagesByRegistration
     *
     * @param registrationNo String
     * @return AccidentImage
     */
    public AccidentImage findAccidentImagesByRegistration(String registrationNo) {
        AccidentImage finder = null;
        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,IMAGE_ID," +
                "FRONT_VIEW,SIDE_VIEW,AREA_VIEW,BACK_VIEW   " +
                "FROM FT_ACCIDENT_IMAGE  WHERE REGISTRATION_NO = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, registrationNo);
            rs = ps.executeQuery();

            while (rs.next()) {

                String branchId = rs.getString("BRANCH_ID");
                String assetId = rs.getString("ASSET_ID");
                String imageId = rs.getString("IMAGE_ID");
                byte[] frontView = rs.getBytes("FRONT_VIEW");
                byte[] sideView = rs.getBytes("SIDE_VIEW");
                byte[] areaView = rs.getBytes("AREA_VIEW");
                byte[] backView = rs.getBytes("BACK_VIEW");

                finder = new AccidentImage(assetId, registrationNo, imageId,
                                           frontView, sideView, areaView,
                                           backView);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching Accident Image ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;

    }

    /**
     * findAssetDetailById
     *
     * @param id String
     * @return AssetDetail
     */
    public AssetDetail findAssetDetailById(String id) {
        AssetDetail finder = null;
        String selectQuery =
                "SELECT A.ASSET_ID,A.REGISTRATION_NO,B.BRANCH_NAME," +
                "A.DESCRIPTION,A.COST_PRICE,A.DATE_PURCHASED,A.Accum_dep," +
                "A.dep_end_date,F.MAINT_PTD,F.FUEL_PTD," +
                "F.ACCIDENT_COST_PTD,F.LICENCE_PERMIT_PTD,F.INSURANCE_COST_PTD," +
                "F.MAINT_LTD,F.FUEL_LTD,F.ACCIDENT_COST_LTD,F.LICENCE_PERMIT_LTD," +
                "F.INSURANCE_COST_LTD   " +
                "FROM AM_ASSET A,FT_FLEET_MASTER F,AM_AD_BRANCH B   " +
                "WHERE A.ASSET_ID = F.ASSET_ID   " +
                "AND A.BRANCH_ID = B.BRANCH_ID   " +
                "AND A.ASSET_ID = ? ";
//System.out.println("<<<<<selectQuery in findAssetDetailById: "+selectQuery);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String branchName = rs.getString("BRANCH_NAME");
                double costPrice = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double maintenancePTD = rs.getDouble("MAINT_PTD");
                double fuelPTD = rs.getDouble("FUEL_PTD");
                double accidentPTD = rs.getDouble("ACCIDENT_COST_PTD");
                double licencePTD = rs.getDouble("LICENCE_PERMIT_PTD");
                double insurancePTD = rs.getDouble("INSURANCE_COST_PTD");
                double maintenanceLTD = rs.getDouble("MAINT_LTD");
                double fuelLTD = rs.getDouble("FUEL_LTD");
                double accidentLTD = rs.getDouble("ACCIDENT_COST_LTD");
                double licenceLTD = rs.getDouble("LICENCE_PERMIT_LTD");
                double insuranceLTD = rs.getDouble("INSURANCE_COST_LTD");
                double accumulatedDepreciation = rs.getDouble("Accum_dep");
                String depreciationEndDate = formatDate(rs.getDate(
                        "dep_end_date"));

                finder = new AssetDetail(id, registrationNo,
                                         description,
                                         branchName, costPrice,
                                         datePurchased,
                                         maintenancePTD, fuelPTD,
                                         accidentPTD,
                                         licencePTD, insurancePTD,
                                         maintenanceLTD, fuelLTD,
                                         accidentLTD,
                                         licenceLTD, insuranceLTD,
                                         accumulatedDepreciation,
                                         depreciationEndDate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching findAssetDetailById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    /**
     * findAssetDetailById
     *
     * @param id String
     * @return AssetDetail
     */
    public AssetDetail findAssetFleetDetailById(String id) {
        AssetDetail finder = null;
        String selectQuery =
                "SELECT A.ASSET_ID,A.REGISTRATION_NO,B.BRANCH_NAME," +
                "A.DESCRIPTION,A.COST_PRICE,A.DATE_PURCHASED,A.Accum_dep," +
                "A.dep_end_date,F.MAINT_PTD,F.FUEL_PTD," +
                "F.ACCIDENT_COST_PTD,F.LICENCE_PERMIT_PTD,F.INSURANCE_COST_PTD," +
                "F.MAINT_LTD,F.FUEL_LTD,F.ACCIDENT_COST_LTD,F.LICENCE_PERMIT_LTD," +
                "F.INSURANCE_COST_LTD, 'N' AS MEMO   " +
                "FROM AM_ASSET A,FT_FLEET_MASTER F,AM_AD_BRANCH B   " +
                "WHERE A.ASSET_ID = F.ASSET_ID   " +
                "AND A.BRANCH_ID = B.BRANCH_ID   " +
                "AND A.ASSET_ID = ? " +
                " UNION "+
                "SELECT A.ASSET_ID,A.REGISTRATION_NO,B.BRANCH_NAME," +
                "A.DESCRIPTION,A.COST_PRICE,A.DATE_PURCHASED,A.Accum_dep," +
                "A.dep_end_date,F.MAINT_PTD,F.FUEL_PTD," +
                "F.ACCIDENT_COST_PTD,F.LICENCE_PERMIT_PTD,F.INSURANCE_COST_PTD," +
                "F.MAINT_LTD,F.FUEL_LTD,F.ACCIDENT_COST_LTD,F.LICENCE_PERMIT_LTD," +
                "F.INSURANCE_COST_LTD, 'Y' AS MEMO  " +
                "FROM AM_ASSET2 A,FT_FLEET_MASTER F,AM_AD_BRANCH B   " +
                "WHERE A.ASSET_ID = F.ASSET_ID   " +
                "AND A.BRANCH_ID = B.BRANCH_ID   " +
                "AND A.ASSET_ID = ? ";                
//System.out.println("<<<<<selectQuery in findAssetFleetDetailById: "+selectQuery);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            ps.setString(2, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String branchName = rs.getString("BRANCH_NAME");
                double costPrice = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double maintenancePTD = rs.getDouble("MAINT_PTD");
                double fuelPTD = rs.getDouble("FUEL_PTD");
                double accidentPTD = rs.getDouble("ACCIDENT_COST_PTD");
                double licencePTD = rs.getDouble("LICENCE_PERMIT_PTD");
                double insurancePTD = rs.getDouble("INSURANCE_COST_PTD");
                double maintenanceLTD = rs.getDouble("MAINT_LTD");
                double fuelLTD = rs.getDouble("FUEL_LTD");
                double accidentLTD = rs.getDouble("ACCIDENT_COST_LTD");
                double licenceLTD = rs.getDouble("LICENCE_PERMIT_LTD");
                double insuranceLTD = rs.getDouble("INSURANCE_COST_LTD");
                double accumulatedDepreciation = rs.getDouble("Accum_dep");
                String memoField = rs.getString("MEMO");
                String depreciationEndDate = formatDate(rs.getDate(
                        "dep_end_date"));

                finder = new AssetDetail(id, registrationNo,
                                         description,
                                         branchName, costPrice,
                                         datePurchased,
                                         maintenancePTD, fuelPTD,
                                         accidentPTD,
                                         licencePTD, insurancePTD,
                                         maintenanceLTD, fuelLTD,
                                         accidentLTD,
                                         licenceLTD, insuranceLTD,
                                         accumulatedDepreciation,
                                         depreciationEndDate);
                finder.setMemo(memoField);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching findAssetFleetDetailById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }


    /**
     * findAssetDetailById
     *
     * @param id String
     * @return AssetDetail
     */
    public AssetDetail findFacilityAssetDetailById(String id) {
        AssetDetail finder = null;
        String selectQuery =
                "SELECT A.ASSET_ID,A.REGISTRATION_NO,B.BRANCH_NAME," +
                "A.DESCRIPTION,A.COST_PRICE,A.DATE_PURCHASED,A.Accum_dep," +
                "A.dep_end_date,F.MAINT_PTD,F.FUEL_PTD," +
                "F.ACCIDENT_COST_PTD,F.LICENCE_PERMIT_PTD,F.INSURANCE_COST_PTD," +
                "F.MAINT_LTD,F.FUEL_LTD,F.ACCIDENT_COST_LTD,F.LICENCE_PERMIT_LTD," +
                "F.INSURANCE_COST_LTD   " +
                "FROM AM_ASSET A,FM_MASTER F,AM_AD_BRANCH B   " +
                "WHERE A.ASSET_ID = F.ASSET_ID   " +
                "AND A.BRANCH_ID = B.BRANCH_ID   " +
                "AND A.ASSET_ID = ? ";
//System.out.println("<<<<<selectQuery in findAssetDetailById: "+selectQuery);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String branchName = rs.getString("BRANCH_NAME");
                double costPrice = rs.getDouble("COST_PRICE");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double maintenancePTD = rs.getDouble("MAINT_PTD");
                double fuelPTD = rs.getDouble("FUEL_PTD");
                double accidentPTD = rs.getDouble("ACCIDENT_COST_PTD");
                double licencePTD = rs.getDouble("LICENCE_PERMIT_PTD");
                double insurancePTD = rs.getDouble("INSURANCE_COST_PTD");
                double maintenanceLTD = rs.getDouble("MAINT_LTD");
                double fuelLTD = rs.getDouble("FUEL_LTD");
                double accidentLTD = rs.getDouble("ACCIDENT_COST_LTD");
                double licenceLTD = rs.getDouble("LICENCE_PERMIT_LTD");
                double insuranceLTD = rs.getDouble("INSURANCE_COST_LTD");
                double accumulatedDepreciation = rs.getDouble("Accum_dep");
                String depreciationEndDate = formatDate(rs.getDate(
                        "dep_end_date"));

                finder = new AssetDetail(id, registrationNo,
                                         description,
                                         branchName, costPrice,
                                         datePurchased,
                                         maintenancePTD, fuelPTD,
                                         accidentPTD,
                                         licencePTD, insurancePTD,
                                         maintenanceLTD, fuelLTD,
                                         accidentLTD,
                                         licenceLTD, insuranceLTD,
                                         accumulatedDepreciation,
                                         depreciationEndDate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching findFacilityAssetDetailById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }


    /**
     * findAssetDetailById
     *
     * @param id String
     * @return AssetDetail
     */
    public void copyAssetDataToFleet(String id) {

        String selectQuery = this.getDataFleetMigrationQuery();

        Connection con = null;
        PreparedStatement ps = null;

        try {  
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            ps.setString(2, id);
            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error Copying Asset Data to Fleet ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }
    
    public void copyAssetDataToFM(String id) {

        String selectQuery = this.getDataFMMigrationQuery();

        Connection con = null;
        PreparedStatement ps = null;

        try {  
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            ps.execute();

        } catch (Exception e) {
            System.out.println("INFO:Error Copying Asset Data to FM ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }
    
    /**
     * findAssetDetailById
     *
     * @param id String
     * @return AssetDetail
     */
    public boolean isRequiredForFleet(String id) {

        String selectQuery = "SELECT B.REQUIRED_FOR_FLEET        " +
                             "FROM AM_ASSET A, AM_AD_CATEGORY B " +
                             "WHERE A.CATEGORY_ID = B.CATEGORY_ID  " +
                             "AND A.ASSET_ID = ?";

        boolean isRequired = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                String required = rs.getString(1);
                isRequired = (required != null && required.equalsIgnoreCase("Y")) ? true : false;
            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error validating fleet requirement for asset ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

        return isRequired;
    }

    private boolean isTransactionOverFlow(double amount, String makeId,
                                          String categoryId) {
        return ((getTransactionBalance(amount, makeId, categoryId) < 0.00) ? true : false);
    }


    private double getTransactionBalance(double amount, String makeId,
                                         String categoryId) {
        double balance = 0.00d;
        int qaurter = 0;
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
        if (month < 4) {
            qaurter = 1;
        } else if (month < 7) {
            qaurter = 2;
        } else if (month < 10) {
            qaurter = 3;
        } else {
            qaurter = 4;
        }
        String TRANS_QUERY = getTransactionQuery(qaurter);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " + TRANS_QUERY + " FROM FT_FLEET_MASTER " +
                       "WHERE CATEGORY_ID = ? AND ASSET_MAKE = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setString(2, categoryId);
            ps.setString(3, makeId);
            rs = ps.executeQuery();

            while (rs.next()) {
                balance = rs.getDouble(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error getting transaction Balance " + ex);
        } finally {
            closeConnection(con, ps, rs);
            System.out.println(query);
        }

        return balance;
    }

    private String getTransactionQuery(int qaurter) {

        String query = "MAINT_BUDGET - MAINT_ACTUAL + ?";
        final int FIRST_QAURTER = 1;
        final int SECOND_QAURTER = 2;
        final int THIRD_QAURTER = 3;
        final int FOURTH_QAURTER = 4;

        if (isQaurterlyRequired()) {

            if (isQaurterlyOverFlowAllowed()) {
                if (qaurter == FIRST_QAURTER) {
                    query = "(MAINT_Q1 - MAINT_A1) + ?";
                } else if (qaurter == SECOND_QAURTER) {
                    query = "(MAINT_Q2 - MAINT_A2) + ?";
                } else if (qaurter == THIRD_QAURTER) {
                    query = "(MAINT_Q3 - MAINT_A3) + ?";
                } else {
                    query = "(MAINT_Q4 - MAINT_A4) + ?";
                }
            }
        } else {
            if (qaurter == FIRST_QAURTER) {
                query = "(MAINT_Q1 - MAINT_A1) + ?";
            } else if (qaurter == SECOND_QAURTER) {
                query =
                        "  MAINT_Q1 + MAINT_Q2 - MAINT_A1 + MAINT_A2 + ?";
            } else if (qaurter == THIRD_QAURTER) {
                query = "   (MAINT_Q1 + MAINT_Q2 + MAINT_Q3 -  " +
                        " MAINT_Q1 + MAINT_Q2 + MAINT_A3) + ? ";
            } else {
                query = "(MAINT_Q1 + MAINT_Q2 + MAINT_Q3 +" +
                        " MAINT_Q4 - MAINT_Q1 + MAINT_Q2 + " +
                        "MAINT_Q3 + MAINT_A4) + ?  ";
            }
        }

        return query;
    }

    public boolean isQaurterlyRequired() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isAllowedQuarter = false;

        String query = "SELECT require_quarterly_pm FROM am_gb_company ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                isAllowedQuarter = (rs.getString(1).equalsIgnoreCase("Y")) ? true : false;
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error validing qaurterly PM " + ex);
        } finally {
            closeConnection(con, ps, rs);
        }

        return true;
    }

    public boolean isQaurterlyOverFlowAllowed() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isAllowedOverFlow = false;

        String query = "SELECT quarterly_surplus_cf FROM am_gb_company ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                isAllowedOverFlow = (rs.getString(1).equalsIgnoreCase("Y")) ? true : false;
            }

        } catch (Exception ex) {
            System.out.println(
                    "WARN: Error getting isQaurterlyOverFlowAllowed() " + ex);
        } finally {
            closeConnection(con, ps, rs);
        }

        return true;
    }


    public String[] getPMCycleParams(String category) {
        String[] notificationParams = new String[3];
        String SELECT_QUERY = "SELECT PM_CYCLE_PERIOD,NOTIFY_MAINT_DAYS,   " +
                              "NOTIFY_EVERY_DAYS  " +
                              "FROM AM_AD_CATEGORY     " +
                              "WHERE CATEGORY_ID = ?    ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            ps.setString(1, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificationParams[0] = rs.getString(1);
                notificationParams[1] = rs.getString(2);
                notificationParams[2] = rs.getString(3);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching PMCycleParams ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return notificationParams;
    }

    public String[] getLicenseCycleParams(String licenseid) {
        String[] notificationParams = new String[3];
        String SELECT_QUERY =
                "SELECT '0' AS PM_CYCLE_PERIOD,NOTIFY_DAYS,EVERY_DAYS  " +
                "FROM AM_AD_LICENSETYPE     " +
                "WHERE LICENSE_ID = ?  ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            ps.setString(1, licenseid);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificationParams[0] = rs.getString(1);
                notificationParams[1] = rs.getString(2);
                notificationParams[2] = rs.getString(3);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching PMCycleParams ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return notificationParams;
    }

    public String[] getInsuranceCycleParams(String insuranceid) {
        String[] notificationParams = new String[3];
        String SELECT_QUERY =
                "SELECT '0' AS PM_CYCLE_PERIOD,NOTIFY_DAYS,EVERY_DAYS   " +
                "FROM AM_AD_INSURANCE    " +
                "WHERE INSURANCE_ID = ?    ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            ps.setString(1, insuranceid);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificationParams[0] = rs.getString(1);
                notificationParams[1] = rs.getString(2);
                notificationParams[2] = rs.getString(3);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching InsuranceCycleParams ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return notificationParams;
    }

    public String getAssetStatusForCombo(String status) {
        String comboString = "";
        StringBuffer comboBuffer = new StringBuffer();
        String SELECT_QUERY = "SELECT DISTINCT(ASSET_STATUS) FROM AM_ASSET";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String val = "";
        String desc = "";
        String sel = "";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            rs = ps.executeQuery();

            while (rs.next()) {
                val = rs.getString(1);
                if (val.equalsIgnoreCase("A")) {
                    desc = "Active";
                } else if (val.equalsIgnoreCase("Z")) {
                    desc = "Distribution";
                } else if (val.equalsIgnoreCase("C")) {
                    desc = "Closed";
                } else if (val.equalsIgnoreCase("D")) {
                    desc = "Disposed";
                } else {
                    desc = "Stolen";
                }

                if (val.equalsIgnoreCase(status)) {
                    sel = " selected";
                }

                comboString = "<option value='" + val + "' " + sel + ">" + desc +
                              "</option>";
                comboBuffer.append(comboString);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching AssetStatusForCombo ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return comboBuffer.toString();
    }
  
    private String getDataFMMigrationQuery() {
        return "INSERT INTO FM_MASTER(" +
                "ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID" +
                ",CATEGORY_ID,DATE_PURCHASED,ASSET_MAKE,ASSET_USER" +
                ",ASSET_MAINTENANCE,CREATE_DATE,EFFECTIVE_DATE,LOCATION" +
                ",RAISE_ENTRY,DEP_YTD,PREMIUM_LTD,PREMIUM_PTD,MAINT_LTD" +
                ",MAINT_PTD,FUEL_LTD,FUEL_PTD,ACCIDENT_COUNT,ACCIDENT_COST_LTD" +
                ",ACCIDENT_COST_PTD,LICENCE_PERMIT_LTD,LICENCE_PERMIT_PTD" +
                ",INSURANCE_COST_LTD,INSURANCE_COST_PTD,LAST_UPDATE_DATE" +
                ",STATUS,USER_ID,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION" +
                ")      " +
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID " +
                ",DEPT_ID,CATEGORY_ID,DATE_PURCHASED ,ASSET_MAKE,ASSET_USER," +
                "ASSET_MAINTENANCE,POSTING_DATE AS CREATE_DATE,EFFECTIVE_DATE," +
                "LOCATION,RAISE_ENTRY,DEP_YTD,0.00 AS PREMIUM_LTD,0.00 ASPREMIUM_PTD," +
                "0.00 AS MAINT_LTD,0.00 AS MAINT_PTD,0.00 AS FUEL_LTD,0.00 AS FUEL_PTD," +
                "0 AS ACCIDENT_COUNT,0.00 AS ACCIDENT_COST_LTD,0.00 AS ACCIDENT_COST_PTD," +
                "0.00 AS LICENCE_PERMIT_LTD,0.00 AS LICENCE_PERMIT_PTD, " +
                "0.00 AS INSURANCE_COST_LTD,0.00 AS INSURANCE_COST_PTD," +
                "POSTING_DATE AS LAST_UPDATE_DATE,ASSET_STATUS,USER_ID,BRANCH_CODE" +
                ",SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION   FROM AM_ASSET    " +
                "WHERE ASSET_ID = ?";

    }
    
    private String getDataFleetMigrationQuery() {
        return "INSERT INTO FT_FLEET_MASTER(" +
                "ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID" +
                ",CATEGORY_ID,DATE_PURCHASED,ASSET_MAKE,ASSET_USER" +
                ",ASSET_MAINTENANCE,CREATE_DATE,EFFECTIVE_DATE,LOCATION" +
                ",RAISE_ENTRY,DEP_YTD,PREMIUM_LTD,PREMIUM_PTD,MAINT_LTD" +
                ",MAINT_PTD,FUEL_LTD,FUEL_PTD,ACCIDENT_COUNT,ACCIDENT_COST_LTD" +
                ",ACCIDENT_COST_PTD,LICENCE_PERMIT_LTD,LICENCE_PERMIT_PTD" +
                ",INSURANCE_COST_LTD,INSURANCE_COST_PTD,LAST_UPDATE_DATE" +
                ",STATUS,USER_ID,BRANCH_CODE,SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION" +
                ")      " +
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID " +
                ",DEPT_ID,CATEGORY_ID,DATE_PURCHASED ,ASSET_MAKE,ASSET_USER," +
                "ASSET_MAINTENANCE,POSTING_DATE AS CREATE_DATE,EFFECTIVE_DATE," +
                "LOCATION,RAISE_ENTRY,DEP_YTD,0.00 AS PREMIUM_LTD,0.00 ASPREMIUM_PTD," +
                "0.00 AS MAINT_LTD,0.00 AS MAINT_PTD,0.00 AS FUEL_LTD,0.00 AS FUEL_PTD," +
                "0 AS ACCIDENT_COUNT,0.00 AS ACCIDENT_COST_LTD,0.00 AS ACCIDENT_COST_PTD," +
                "0.00 AS LICENCE_PERMIT_LTD,0.00 AS LICENCE_PERMIT_PTD, " +
                "0.00 AS INSURANCE_COST_LTD,0.00 AS INSURANCE_COST_PTD," +
                "POSTING_DATE AS LAST_UPDATE_DATE,ASSET_STATUS,USER_ID,BRANCH_CODE" +
                ",SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION   FROM AM_ASSET    " +
                "WHERE ASSET_ID = ?" +
                " UNION " +
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID " +
                ",DEPT_ID,CATEGORY_ID,DATE_PURCHASED ,ASSET_MAKE,ASSET_USER," +
                "ASSET_MAINTENANCE,POSTING_DATE AS CREATE_DATE,EFFECTIVE_DATE," +
                "LOCATION,RAISE_ENTRY,DEP_YTD,0.00 AS PREMIUM_LTD,0.00 ASPREMIUM_PTD," +
                "0.00 AS MAINT_LTD,0.00 AS MAINT_PTD,0.00 AS FUEL_LTD,0.00 AS FUEL_PTD," +
                "0 AS ACCIDENT_COUNT,0.00 AS ACCIDENT_COST_LTD,0.00 AS ACCIDENT_COST_PTD," +
                "0.00 AS LICENCE_PERMIT_LTD,0.00 AS LICENCE_PERMIT_PTD, " +
                "0.00 AS INSURANCE_COST_LTD,0.00 AS INSURANCE_COST_PTD," +
                "POSTING_DATE AS LAST_UPDATE_DATE,ASSET_STATUS,USER_ID,BRANCH_CODE" +
                ",SECTION_CODE,DEPT_CODE,CATEGORY_CODE,DESCRIPTION   FROM AM_ASSET2    " +
                "WHERE ASSET_ID = ?" ;

    }


    public static void main(String[] args) {
        FleetHistoryManager ft = new FleetHistoryManager();
        //FleetLicencePermit maint = ft.findLicencePermitById("MAG/SFT/MV/HO/FIN/10/LI/1","");
        //System.out.println(maint.getDateObtained());
        //ft.removeMaintenanceRecordById("56");
    }




    public ArrayList findBarCodeByQuery(String queryFilter) {

        String selectQuery =
                "SELECT ASSET_ID,DESCRIPTION,BRANCH_CODE,BAR_CODE," +
                "category_code" +
                "FROM AM_BARCODE_HISTORY WHERE ASSET_ID IS NOT NULL && PRINT_FLD = 'N'" + queryFilter;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList barcodelist = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                   BarCodeHistoryBean bhb = new BarCodeHistoryBean();


            bhb.setAsset_id((rs.getString(1) == null)?"":rs.getString(1));
            bhb.setDescription((rs.getString(2) == null)?"":rs.getString(2));
            bhb.setBar_code((rs.getString(3) == null)?"":rs.getString(3));
            bhb.setBranch_code((rs.getString(4) == null)?"":getBranchName(rs.getString(4)));
            //bhb.setBranch_code((rs.getString(4) == null)?"":rs.getString(4));
            bhb.setCategory((rs.getString(5) == null)?"":getCategoryName(rs.getString(5)));
            //bhb.setCategory((rs.getString(5) == null)?"":rs.getString(5));
            //bhb.setCreate_date((rs.getDate(6) == null)?"":rs.getDate(6).toString());
            barcodelist.add(bhb);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findBarCodeByQuery->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return barcodelist;

    }


      public String getBranchName(String id){
String branch_name= "";
 try {
     //System.out.println("the value of id received by getBranchName() is " +id);
            con1 = dbConnection.getConnection("legendPlus");

            String query = "select BRANCH_NAME from am_ad_branch where BRANCH_CODE ='"+id+"'";

            ps1 = con1.prepareStatement(query);
            rs1 = ps1.executeQuery();
             //int counter =0;
            while(rs1.next()){



            branch_name = ((rs1.getString(1) == null)?"":rs1.getString(1));


            }//while
            //}//for
        } catch (Exception e) {
            System.out.println("BarCodePrintServlet:=== DB erorr occured in method getBranchName()" +e);
        }
    finally{
      dbConnection.closeConnection(con1, ps1,rs1);
    }

    return branch_name;

}//getBranchName()



   public String getCategoryName(String id){
String category_name= "";
 try {
     //System.out.println("the value of id received by getBranchName() is " +id);
            con1 = dbConnection.getConnection("legendPlus");

            String query = "select category_name from am_ad_category where category_code ='"+id+"'";

            ps1 = con1.prepareStatement(query);
            rs1 = ps1.executeQuery();
             //int counter =0;
            while(rs1.next()){



            category_name = ((rs1.getString(1) == null)?"":rs1.getString(1));


            }//while
            //}//for
        } catch (Exception e) {
            System.out.println("BarCodePrintServlet:=== DB erorr occured in method getCategoryName()" +e);
        }
    finally{
      dbConnection.closeConnection(con1, ps1,rs1);
    }

    return category_name;

}//getCategoryName()
//modification by lanre
   public ArrayList findAssetForDepreciation(String filter) {  

		String selectQuery =
			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
			"CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
			"ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
			"ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
			"RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION_ID , " +
			"NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE, " +
			"Asset_Status,REQ_REDISTRIBUTION,   " +
			"IMPROV_USEFULLIFE,IMPROV_TOTALLIFE,IMPROV_REMAINLIFE,IMPROV_COST,"+
			"IMPROV_MONTHLYDEP,IMPROV_ACCUMDEP,IMPROV_NBV,IMPROV_VATABLECOST,TOTAL_NBV,IMPROV_EffectiveDate,IMPROV_EndDate "+
			"FROM AM_ASSET  WHERE ASSET_ID NOT IN (SELECT ASSET_ID FROM DEPRECIATION_PROCESSING_VALIDATION) AND Req_Depreciation = 'Y' AND ASSET_STATUS = 'ACTIVE' " + filter;
//		System.out.println("<<<<<<<<selectQuery: "+selectQuery);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(selectQuery);
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String branchId = rs.getString("BRANCH_ID");
				String departmentId = rs.getString("DEPT_ID");
				String section = rs.getString("SECTION_ID");
				String category = rs.getString("CATEGORY_ID");
				String description = rs.getString("DESCRIPTION");
				String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
				double depreciationRate = rs.getDouble("DEP_RATE");
				String assetMake = rs.getString("ASSET_MAKE");
				String assetUser = rs.getString("ASSET_USER");
				String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
				double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
				double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
				double cost = rs.getDouble("COST_PRICE");
				String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
				String depreciationEndDate2 = rs.getString("DEP_END_DATE");
				double residualValue = rs.getDouble("RESIDUAL_VALUE");
				String postingDate = rs.getString("POSTING_DATE");
				String entryRaised = rs.getString("RAISE_ENTRY");
				double depreciationYearToDate = rs.getDouble("DEP_YTD");
				double nbv = rs.getDouble("NBV");
				int remainingLife = rs.getInt("REMAINING_LIFE");
				int totalLife = rs.getInt("TOTAL_LIFE");
				java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
				String astatus = rs.getString("Asset_Status");
				String req_distribution = rs.getString("REQ_REDISTRIBUTION");
				int improvusefulLife = rs.getInt("IMPROV_USEFULLIFE");
				int improvremainingLife = rs.getInt("IMPROV_REMAINLIFE");
				int improvtotalLife = rs.getInt("IMPROV_TOTALLIFE");
				double improvaccumulatedDepreciation = rs.getDouble("IMPROV_ACCUMDEP");
				double improvmonthlyDepreciation = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvcost = rs.getDouble("IMPROV_COST");
				double improvTotalNbv = rs.getDouble("TOTAL_NBV");
				double improvnbv = rs.getDouble("IMPROV_NBV");
				java.util.Date improveffectiveDate = rs.getDate("IMPROV_EffectiveDate");
				String improveEndDate = rs.getString("IMPROV_EndDate");
				
				Asset aset = new Asset(id, registrationNo, branchId,
						departmentId, section, category,
						description,
						datePurchased, depreciationRate,
						assetMake,
						assetUser, assetMaintenance,
						accumulatedDepreciation,
						monthlyDepreciation, cost,
						depreciationEndDate,
						residualValue, postingDate, entryRaised,
						depreciationYearToDate);
				aset.setNbv(nbv);
				aset.setRemainLife(remainingLife);
				aset.setTotalLife(totalLife);
				aset.setEffectiveDate(effectiveDate);
				aset.setAsset_status(astatus);
				aset.setReq_distribution(req_distribution);
				aset.setDepreciationEndDate(depreciationEndDate2);
				aset.setImprovusefulLife(improvusefulLife);
				aset.setImprovremainingLife(improvremainingLife);
				aset.setImprovaccumulatedDepreciation(improvaccumulatedDepreciation);
				aset.setImprovmonthlyDepreciation(improvmonthlyDepreciation);
				aset.setImprovcost(improvcost);
				aset.setImprovTotalNbv(improvTotalNbv);
				aset.setImprovnbv(improvnbv);
				aset.setImprovEffectiveDate(improveffectiveDate);
				aset.setImprovEndDate(improveEndDate);
//				System.out.println("findAssetForDepreciation accumulatedDepreciation:   "+accumulatedDepreciation);
//				System.out.println("findAssetForDepreciation improvmonthlyDepreciation:   "+improvmonthlyDepreciation);
//				System.out.println("findAssetForDepreciation improvremainingLife:   "+improvremainingLife);
				list.add(aset);

			}

		} catch (Exception e) {
			System.out.println("INFO:Error fetching ALL Asset in findAssetForDepreciation ->" +
					e.getMessage());
		} finally {
			closeConnection(con, ps, rs);
		}
		closeConnection(con, ps, rs);
		return list;

	}

    /**
     * @return the minCost
     */
    public double getMinCost() {
        return minCost;
    }

    /**
     * @param minCost the minCost to set
     */
    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

    /**
     * @return the maxCost
     */
    public double getMaxCost() {
        return maxCost;
    }

    /**
     * @param maxCost the maxCost to set
     */
    public void setMaxCost(double maxCost) {
        this.maxCost = maxCost;
    }

   /*
   public double[] getMinMaxAssetCost(double min, double max){
   double [] result = {min,max};
   return result;
   }
  */



    public ArrayList findAssetBIDByQuery(String queryFilter){

        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
                "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
                "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
                "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
                "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
                "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
                "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
                "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
                "system_ip,mac_address,Bid_Period,BID,BID_TAG,STAFFID,BASE_PRICE,LOCATION_CODE " +
                "FROM AM_ASSET_BID  WHERE ASSET_ID IS NOT NULL " + queryFilter;
//        System.out.println("the query in findAssetBIDByQuery is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String assetcode = rs.getString("ASSET_CODE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String branchcode = rs.getString("BRANCH_CODE");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String categorycode = rs.getString("CATEGORY_CODE");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String effectiveDate2 = rs.getString("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String sectionID = rs.getString("Section_id");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Supplier_Name = rs.getInt("Supplier_Name");
                int Asset_Maintenance = rs.getInt("Asset_Maintenance");
                String Authorized_By = rs.getString("Authorized_By");
                String Wh_Tax = rs.getString("Wh_Tax");
                double Wh_Tax_Amount = rs.getDouble("Wh_Tax_Amount");
                String Req_Redistribution = rs.getString("Req_Redistribution");
                String Purchase_Reason = rs.getString("Purchase_Reason");
                int Useful_Life = rs.getInt("Useful_Life");
                int Location = rs.getInt("Location");
                double Vatable_Cost = rs.getDouble("Vatable_Cost");
                double Vat = rs.getDouble("Vat");
                String Req_Depreciation = rs.getString("Req_Depreciation");
                String Subject_TO_Vat = rs.getString("Subject_TO_Vat");
                String Who_TO_Rem = rs.getString("Who_TO_Rem");
                String email1 = rs.getString("Email1");
                String Who_To_Rem_2 = rs.getString("Who_To_Rem_2");
                String email2 = rs.getString("Email2");
                int State = rs.getInt("State");
                int Driver = rs.getInt("Driver");
                String Spare_1 = rs.getString("Spare_1");
                String Spare_2 = rs.getString("Spare_2");
                int User_ID = rs.getInt("User_ID");
                String Date_Disposed = rs.getString("Date_Disposed");
                int PROVINCE = rs.getInt("PROVINCE");
                String Multiple = rs.getString("Multiple");
                String WAR_START_DATE = rs.getString("WAR_START_DATE");
                int WAR_MONTH = rs.getInt("WAR_MONTH");
                String WAR_EXPIRY_DATE = rs.getString("WAR_EXPIRY_DATE");
                String Last_Dep_Date = rs.getString("Last_Dep_Date");
                String SECTION_CODE = rs.getString("SECTION_CODE");
                String DEPT_CODE = rs.getString("DEPT_CODE");
                double AMOUNT_PTD = rs.getDouble("AMOUNT_PTD");
                double AMOUNT_REM = rs.getDouble("AMOUNT_REM");
                String PART_PAY = rs.getString("PART_PAY");
                String FULLY_PAID = rs.getString("FULLY_PAID");
                String GROUP_ID = rs.getString("GROUP_ID");
                String BAR_CODE = rs.getString("BAR_CODE");
                String SBU_CODE = rs.getString("SBU_CODE");
                String LPO = rs.getString("LPO");
                String supervisor = rs.getString("supervisor");
                String defer_pay = rs.getString("defer_pay");
                String OLD_ASSET_ID = rs.getString("OLD_ASSET_ID");
                int WHT_PERCENT = rs.getInt("WHT_PERCENT");
                String Post_reject_reason = rs.getString("Post_reject_reason");
                String Finacle_Posted_Date = rs.getString("Finacle_Posted_Date");
                String system_ip = rs.getString("system_ip");
                String mac_address = rs.getString("mac_address");
                String bid_period = rs.getString("Bid_Period");
                String bidcheck = rs.getString("BID");
                String bidTag = rs.getString("BID_TAG");
                String staffId = rs.getString("STAFFID");
                double basePrice = rs.getDouble("BASE_PRICE");
                String locationCode = rs.getString("LOCATION_CODE");
                
              //  DatetimeFormat dd = new DatetimeFormat();
                //dd.dateConvert(strDate)

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetmodel(Asset_Model);
                aset.setSectionId(sectionID);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setSupplierName(Supplier_Name);
                aset.setAssetMaintain(Asset_Maintenance);
                aset.setAuthorizeBy(Authorized_By);
                aset.setWh_tax(Wh_Tax);
                aset.setWh_Tax_Amount(Wh_Tax_Amount);
                aset.setReq_Depreciation(Req_Redistribution);
                aset.setPurchaseReason(Purchase_Reason);
                aset.setUseful_Life(Useful_Life);
                aset.setLocation(Location);
                aset.setVatable_Cost(Vatable_Cost);
                aset.setVat(Vat);
                aset.setReq_Redistribution(Req_Redistribution);
                aset.setSubject_TO_Vat(Subject_TO_Vat);
                aset.setWho_TO_Rem(Who_TO_Rem);
                aset.setEmail1(email1);
                aset.setWho_To_Rem_2(Who_To_Rem_2);
                aset.setEmail2(email2);
                aset.setState(State);
                aset.setDriver(Driver);
                aset.setSpare1(Spare_1);
                aset.setSpare2(Spare_2);
                aset.setUserid(User_ID);
                aset.setDate_Disposed(Date_Disposed);
                aset.setPROVINCE(PROVINCE);
                aset.setMultiple(Multiple);
                aset.setWAR_START_DATE(WAR_START_DATE);
                aset.setWAR_MONTH(WAR_MONTH);
                aset.setWAR_EXPIRY_DATE(WAR_EXPIRY_DATE);
                aset.setLast_Dep_Date(Last_Dep_Date);
                aset.setSECTION_CODE(SECTION_CODE);
                aset.setDepcode(DEPT_CODE);
                aset.setAMOUNT_PTD(AMOUNT_PTD);
                aset.setAMOUNT_REM(AMOUNT_REM);
                aset.setPART_PAY(PART_PAY);
                aset.setFULLY_PAID(FULLY_PAID);
                aset.setGROUP_IDd(String.valueOf(GROUP_ID));
                aset.setBarcode2(BAR_CODE);
                aset.setSbuCode(SBU_CODE);
                aset.setLPO(LPO);
                aset.setSupervisor(supervisor);
                aset.setDefer_pay(defer_pay);
                aset.setOLD_ASSET_ID(OLD_ASSET_ID);
                aset.setWHT_PERCENT(WHT_PERCENT);
                aset.setPost_reject_reason(Post_reject_reason);
                aset.setFinacle_Posted_Date(Finacle_Posted_Date);
                aset.setSystem_ip(system_ip);
                aset.setMac_address(mac_address);
                aset.setVendorAc(vendor_acct);
                aset.setBranchCode(branchcode);
                aset.setCategoryCode(categorycode);
                aset.setEffectivedate2(effectiveDate2);
                aset.setAssetcode(assetcode);
                aset.setBidPeriod(bid_period) ;
                aset.setChkBid(bidcheck);
                aset.setBidTag(bidTag);
                aset.setStaffId(staffId);
                aset.setBasePrice(basePrice);
                aset.setLocationCode(locationCode);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }
            closeConnection(con, ps, rs);
        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetBIDByQuery ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public boolean InsertBidSelection(ArrayList list) {
        boolean re = false;
        //com.magbel.util.DatetimeFormat df = new com.magbel.util.DatetimeFormat();


        String query = "insert into AM_ASSET_BID_SELECTION("+
                "Asset_id,Registration_No,Description,Vendor_AC," +
                "Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No," +
                "Asset_Engine_No,Location,Bid_Value,Useage_Years,Asset_Status," +
                "First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Phone_No,Email_Address,Initials," +
                "POSTING_DATE,BID_TAG,STAFFID,BASE_PRICE)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.net.vao.Asset bd = null;

        int[] d = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for(int i = 0; i < list.size(); i++) {
                bd = (magma.net.vao.Asset) list.get(i);


                String assetId = bd.getId();
                String registration = bd.getRegistrationNo();
                String description = bd.getDescription();
                String datePurchased = bd.getDatePurchased();

                if(datePurchased.trim() == null  || datePurchased.trim().equalsIgnoreCase("null"))
				 {
                		datePurchased = df.getDateTime();

	              }

                String asset_mark = bd.getAssetMake();

                String asset_model = bd.getAssetmodel();
				String  Serino = bd.getSerialNo();

				String engno = bd.getEngineNo();
				if(engno == null || engno.equals("")){
	                	engno = "0";
	            }
				int location = bd.getLocation();

				String  assetStatus = bd.getAsset_status();


				String vendorAc = bd.getVendorAc();

				int usageLifes = bd.getUseageYears();

				double bidval = bd.getBid();

				int categoryId = bd.getCategoryId();

				String phone = bd.getPhoneNo();
				if(phone==null){

					phone="";
				}
				String email = bd.getEmail1();
				if(email==null){
					email="";
				}

				String address1 = bd.getAddress1();
				if(address1 == null || address1.equals("")){
					address1 = "";
	                }
				String address2 = bd.getAddress2();
				if(address2 == null || address2.equals("")){
					address2 = "";
	                }
				String bidcode = bd.getBidcode();
				if(bidcode == null || bidcode.equals("")){
					bidcode = "";
	                }

				String firstname = bd.getFirstname();
				 if(firstname == null || firstname.equals("")){
					 firstname = "";
	                }
				String surname = bd.getSurname();
				 if(surname == null || surname.equals("")) {
					 	surname = "";
				 }

				 String initials = bd.getInitials();
				 if(initials==null)
				 {
					 initials="";
				 }

              if (assetId == null || assetId.equals("")) {
                	assetId = "";
                }
              if (registration == null || registration.equals("")) {
              	assetId = "0";
              }
                if (description == null || description.equals("")) {
                	description = "";
                }

                if(asset_mark == null || asset_mark.equals("")) {
                	asset_mark = "0";
                }
                if (asset_model == null || asset_model.equals("")) {
                	asset_model = "0";
                }
                if (Serino == null || Serino.equals("")) {
                	Serino = "0";
                }

                if (assetStatus == null || assetStatus.equals("")) {
                	assetStatus= "";
                }

                if (vendorAc == null || vendorAc.equals("")) {
                	vendorAc = "0";
                }

                if (vendorAc == null || vendorAc.equals("")) {
                	vendorAc = "0";
                }

				String bidTag = bd.getBidTag();
				 if(bidTag == null || bidTag.equals("")){
					 bidTag = "";
	                }

				String staffId = bd.getStaffId();
				 if(staffId == null || staffId.equals("")){
					 staffId = "";
	                }
				 double basePrice = bd.getCost();
				 
                ps.setString(1, assetId);
                ps.setString(2, registration);
                ps.setString(3, description);
                ps.setString(4, vendorAc);
                ps.setDate(5, df.dateConvert(datePurchased));
                ps.setString(6, asset_mark);
                ps.setString(7, asset_model);
                ps.setString(8,Serino);
                ps.setString(9, engno);
                ps.setInt(10, location);
                ps.setDouble(11, bidval);
                ps.setInt(12, usageLifes);
                ps.setString(13, assetStatus);
                ps.setString(14, firstname);
                ps.setString(15, surname);
                ps.setInt(16, categoryId);
                ps.setString(17, bidcode);
                ps.setString(18,address1);
                ps.setString(19,address2);
                ps.setString(20, phone);
                ps.setString(21, email);
                ps.setString(22, initials);
                ps.setDate(23, df.dateConvert(new java.util.Date()));
                ps.setString(24, bidTag);
                ps.setString(25, staffId);
                ps.setDouble(26, basePrice);

                ps.addBatch();
            }
            d = ps.executeBatch();
 //          System.out.println("Executed Successfully ");

        }catch(Exception ex) {
            System.out.println("Error InsertBIDSelection() In FleetHistoryManager -> " + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }


public boolean findExistedBidLimit(magma.net.vao.Asset asset,ArrayList lists){


    String selectQuery = "SELECT * FROM AM_BID_LIMIT WHERE ASSET_ID='"+asset.getId()+"' ";

   Connection con = null;
   PreparedStatement ps = null;
   ResultSet rs = null;
   ArrayList list = new ArrayList();
   boolean done = false;

   try{
       con = getConnection("legendPlus");
       ps = con.prepareStatement(selectQuery);
       rs = ps.executeQuery();
      if(rs.next())
      {
   	  // System.out.print("  Record existed...");
   	   updatebidLimit(lists);
   	   done = true;
      }else{
    	  	InsertBidLimit(lists);
    	  	 done = true;
      }

   }catch (Exception e) {
       System.out.println("INFO:Error Finding Existed Records. " +
                          e.getMessage());
   } finally {
       closeConnection(con, ps, rs);
   }

   return done;

}
public boolean updatebidLimit(java.util.ArrayList list) {

	String query = "UPDATE AM_BID_LIMIT "+
			"SET Bid_Limit = ?,Bid_Tag = ?,Description = ?,LOCATION_CODE = ? "
		+ " WHERE Asset_id=? ";
	String query2 = "UPDATE AM_ASSET_BID "+
			"SET Bid_Tag = ?,Description = ?,BASE_PRICE = ?,LOCATION_CODE = ? "
		+ " WHERE Asset_id=? ";
	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	ResultSet rs = null;
	int[] d = null;
	boolean done = false;
	magma.net.vao.Asset asset = null;
	try {


		con = getConnection("legendPlus");
		ps = con.prepareStatement(query);
		ps2 = con.prepareStatement(query2);
		for(int i = 0; i <list.size(); i++){
			asset = (magma.net.vao.Asset)list.get(i);

			ps.setDouble(1,asset.getBid());
			System.out.print("Bid-->>>: "+asset.getBid()+"   Bid Tag-->>: "+asset.getBidTag()+"  Bid Description -->: "+asset.getDescription());
			ps.setString(2,asset.getBidTag());
			ps.setString(3,asset.getDescription());
			ps.setString(4,asset.getLocationCode());
			ps.setString(5,asset.getId());
//			ps.addBatch();
			done=( ps.executeUpdate()!=-1);
			
			ps2.setString(1, asset.getBidTag());
			ps2.setString(2, asset.getDescription());
			ps2.setDouble(3, asset.getBid());
			ps2.setString(4, asset.getLocationCode());
			ps2.setString(5, asset.getId());
			done=( ps2.executeUpdate()!=-1);			
		}
//		d = ps.executeBatch();

	}catch (Exception ex) {
		System.out.println("WARN: Error Upating Asset Bid limit  ->" + ex);
	} finally {
		closeConnection(con, ps);
		closeConnection(con, ps2);
	}
	return (d.length > 0);
}
public boolean InsertBidLimit(ArrayList list) {
    boolean re = false;
    boolean done = false;
    //com.magbel.util.DatetimeFormat df = new com.magbel.util.DatetimeFormat();


    String query = "insert into AM_BID_LIMIT("+
            "Asset_id,Registration_No,Description," +
            "Bid_Limit,Category_Id,User_Id,Asset_Status," +
            "Create_Date,Bid_Tag,LOCATION_CODE)" +
            "values(?,?,?,?,?,?,?,?,?,?)";

	String query2 = "UPDATE AM_ASSET_BID "+
			"SET Bid_Tag = ?,Description = ?,BASE_PRICE = ?,LOCATION_CODE = ? "
		+ " WHERE Asset_id=? ";
	
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    //ResultSet rs = null;
    magma.net.vao.Asset bd = null;

    int[] d = null;
    magma.net.vao.Asset asset = null;
    try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(query);
        ps2 = con.prepareStatement(query2);
        for(int i = 0; i < list.size(); i++) {
            bd = (magma.net.vao.Asset) list.get(i);


            String assetId = bd.getId();
            String registration = bd.getRegistrationNo();
            String description = bd.getDescription();
            String datePurchased = bd.getDatePurchased();
            String bidTag = bd.getBidTag();
            String locationCode = bd.getLocationCode();

			String  assetStatus = bd.getAsset_status();


			double bidval = bd.getBid();

			int categoryId = bd.getCategoryId();

			int userId = bd.getUserid();

          if(registration == null || registration.equals("")) {
          	assetId = "0";
          }
          if (description == null || description.equals("")) {
            	description = "";
          }
          
          if (bidTag == null || bidTag.equals("")) {
        	  bidTag = "";
        }

          if (assetStatus == null || assetStatus.equals("")) {
            	assetStatus= "";
            }


            ps.setString(1, assetId);
            ps.setString(2, registration);
            ps.setString(3, description);
            ps.setDouble(4, bidval);
            ps.setInt(5, categoryId);
            ps.setInt(6, userId);
            ps.setString(7, assetStatus);
            ps.setDate(8, df.dateConvert(new java.util.Date()));
            ps.setString(9, bidTag);
            ps.setString(10, locationCode);
 //           ps.addBatch();
            done=(ps.executeUpdate()!=-1);
            
			ps2.setString(1,bidTag);
			ps2.setString(2,description);
			ps2.setDouble(3, bidval);
			ps2.setString(4, locationCode);
			ps2.setString(5,assetId);
			done=( ps2.executeUpdate()!=-1);
			 
        }

//        d = ps.executeBatch();

    //   System.out.println("Executed Successfully ");

    }catch(Exception ex) {
        System.out.println("Error InsertBidLimit In FleetHistoryManager -> " + ex);
    } finally {
        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps2);
    }

    return (d.length > 0);
}
public ArrayList findAssetByQueryy(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
                "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
                "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
                "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
                "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
                "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
                "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
                "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
                "system_ip,mac_address " +
                "FROM AM_ASSET  WHERE ASSET_ID not in (SELECT ASSET_ID FROM AM_ASSET_TRANSFER) AND ASSET_ID IS NOT NULL " + queryFilter;
 //       System.out.println("the query in findAssetByQueryy is <<<<<<<<<<<<< "+ selectQuery);


       String query =
    	"SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
        "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
        "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
        "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
        "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION ," +
        "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status ," +
        "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance ," +
        "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason ," +
        "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation," +
        "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2," +
        "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE," +
        "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE," +
        "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE," +
        "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date," +
        "system_ip,mac_address " +
        "FROM AM_ASSET  where ASSET_ID NOT IN (select Asset_Id from am_asset_bid) "+queryFilter;


        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                int assetcode = rs.getInt("ASSET_CODE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String branchcode = rs.getString("BRANCH_CODE");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String categorycode = rs.getString("CATEGORY_CODE");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String effectiveDate2 = rs.getString("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String sectionID = rs.getString("Section_id");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Supplier_Name = rs.getInt("Supplier_Name");
                int Asset_Maintenance = rs.getInt("Asset_Maintenance");
                String Authorized_By = rs.getString("Authorized_By");
                String Wh_Tax = rs.getString("Wh_Tax");
                double Wh_Tax_Amount = rs.getDouble("Wh_Tax_Amount");
                String Req_Redistribution = rs.getString("Req_Redistribution");
                String Purchase_Reason = rs.getString("Purchase_Reason");
                int Useful_Life = rs.getInt("Useful_Life");
                int Location = rs.getInt("Location");
                double Vatable_Cost = rs.getDouble("Vatable_Cost");
                double Vat = rs.getDouble("Vat");
                String Req_Depreciation = rs.getString("Req_Depreciation");
                String Subject_TO_Vat = rs.getString("Subject_TO_Vat");
                String Who_TO_Rem = rs.getString("Who_TO_Rem");
                String email1 = rs.getString("Email1");
                String Who_To_Rem_2 = rs.getString("Who_To_Rem_2");
                String email2 = rs.getString("Email2");
                int State = rs.getInt("State");
                int Driver = rs.getInt("Driver");
                String Spare_1 = rs.getString("Spare_1");
                String Spare_2 = rs.getString("Spare_2");
                int User_ID = rs.getInt("User_ID");
                String Date_Disposed = rs.getString("Date_Disposed");
                int PROVINCE = rs.getInt("PROVINCE");
                String Multiple = rs.getString("Multiple");
                String WAR_START_DATE = rs.getString("WAR_START_DATE");
                int WAR_MONTH = rs.getInt("WAR_MONTH");
                String WAR_EXPIRY_DATE = rs.getString("WAR_EXPIRY_DATE");
                String Last_Dep_Date = rs.getString("Last_Dep_Date");
                String SECTION_CODE = rs.getString("SECTION_CODE");
                String DEPT_CODE = rs.getString("DEPT_CODE");
                double AMOUNT_PTD = rs.getDouble("AMOUNT_PTD");
                double AMOUNT_REM = rs.getDouble("AMOUNT_REM");
                String PART_PAY = rs.getString("PART_PAY");
                String FULLY_PAID = rs.getString("FULLY_PAID");
                String GROUP_ID = rs.getString("GROUP_ID");
                String BAR_CODE = rs.getString("BAR_CODE");
                String SBU_CODE = rs.getString("SBU_CODE");
                String LPO = rs.getString("LPO");
                String supervisor = rs.getString("supervisor");
                String defer_pay = rs.getString("defer_pay");
                String OLD_ASSET_ID = rs.getString("OLD_ASSET_ID");
                int WHT_PERCENT = rs.getInt("WHT_PERCENT");
                String Post_reject_reason = rs.getString("Post_reject_reason");
                String Finacle_Posted_Date = rs.getString("Finacle_Posted_Date");
                String system_ip = rs.getString("system_ip");
                String mac_address = rs.getString("mac_address");

              //  DatetimeFormat dd = new DatetimeFormat();
                //dd.dateConvert(strDate)

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetmodel(Asset_Model);
                aset.setSectionId(sectionID);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setSupplierName(Supplier_Name);
                aset.setAssetMaintain(Asset_Maintenance);
                aset.setAuthorizeBy(Authorized_By);
                aset.setWh_tax(Wh_Tax);
                aset.setWh_Tax_Amount(Wh_Tax_Amount);
                aset.setReq_Depreciation(Req_Redistribution);
                aset.setPurchaseReason(Purchase_Reason);
                aset.setUseful_Life(Useful_Life);
                aset.setLocation(Location);
                aset.setVatable_Cost(Vatable_Cost);
                aset.setVat(Vat);
                aset.setReq_Redistribution(Req_Redistribution);
                aset.setSubject_TO_Vat(Subject_TO_Vat);
                aset.setWho_TO_Rem(Who_TO_Rem);
                aset.setEmail1(email1);
                aset.setWho_To_Rem_2(Who_To_Rem_2);
                aset.setEmail2(email2);
                aset.setState(State);
                aset.setDriver(Driver);
                aset.setSpare1(Spare_1);
                aset.setSpare2(Spare_2);
                aset.setUserid(User_ID);
                aset.setDate_Disposed(Date_Disposed);
                aset.setPROVINCE(PROVINCE);
                aset.setMultiple(Multiple);
                aset.setWAR_START_DATE(WAR_START_DATE);
                aset.setWAR_MONTH(WAR_MONTH);
                aset.setWAR_EXPIRY_DATE(WAR_EXPIRY_DATE);
                aset.setLast_Dep_Date(Last_Dep_Date);
                aset.setSECTION_CODE(SECTION_CODE);
                aset.setDepcode(DEPT_CODE);
                aset.setAMOUNT_PTD(AMOUNT_PTD);
                aset.setAMOUNT_REM(AMOUNT_REM);
                aset.setPART_PAY(PART_PAY);
                aset.setFULLY_PAID(FULLY_PAID);
                aset.setGROUP_IDd(String.valueOf(GROUP_ID));
                aset.setBarcode2(BAR_CODE);
                aset.setSbuCode(SBU_CODE);
                aset.setLPO(LPO);
                aset.setSupervisor(supervisor);
                aset.setDefer_pay(defer_pay);
                aset.setOLD_ASSET_ID(OLD_ASSET_ID);
                aset.setWHT_PERCENT(WHT_PERCENT);
                aset.setPost_reject_reason(Post_reject_reason);
                aset.setFinacle_Posted_Date(Finacle_Posted_Date);
                aset.setSystem_ip(system_ip);
                aset.setMac_address(mac_address);
                aset.setVendorAc(vendor_acct);
                aset.setBranchCode(branchcode);
                aset.setCategoryCode(categorycode);
                aset.setEffectivedate2(effectiveDate2);
                aset.setAssetCode(assetcode);
               // aset.setCategoryId(categoryId)




                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQueryy  ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
public boolean findExistedBidTransfer(magma.net.vao.Asset asset,ArrayList lists){


        String selectQuery = "SELECT * FROM AM_ASSET_BID WHERE ASSET_ID='"+asset.getId()+"' ";

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       ArrayList list = new ArrayList();
       boolean done = false;

       try{
           con = getConnection("legendPlus");
           ps = con.prepareStatement(selectQuery);
           rs = ps.executeQuery();
          if(rs.next())
          {
       	   done = true;
//       	   	System.out.println("asset.getId() for updateBidTransfer =====>: "+asset.getId());
       	   		updateBidTransfer(lists);
          }else{
//        	  System.out.println("asset.getId() for insertBID =====>: "+asset.getId());
        	  	insertBID(lists);

          }

       }catch (Exception e) {
           System.out.println("INFO:Error Finding Existed Records. " +
                              e.getMessage());
       } finally {
           closeConnection(con, ps, rs);
       }

       return done;

    }
public boolean updateBidTransfer(java.util.ArrayList list) {

    	String query = "UPDATE AM_ASSET_BID "+
    			"SET Asset_id = ?,LOCATION_CODE = ? "
    		+ " WHERE Asset_id=? ";
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	int[] d = null;
    	boolean done = false;
    	magma.net.vao.Asset asset = null;
    	try {


    		con = getConnection("legendPlus");
    		ps = con.prepareStatement(query);
    		for(int i = 0; i <list.size(); i++){
    			asset = (magma.net.vao.Asset)list.get(i);

    			ps.setString(1,asset.getId());
    			ps.setString(2,asset.getLocationCode());
    			System.out.print("Bid-->>>"+asset.getBid());
    			ps.setString(3,asset.getId());
    			ps.addBatch();
    			//done=( ps.executeUpdate()!=-1);
    		}
    		d = ps.executeBatch();
    		closeConnection(con, ps);
    	}catch (Exception ex) {
    		System.out.println("WARN: Error Upating Asset Bid limit  ->" + ex);
    	} finally {
    		closeConnection(con, ps);
    	}
    	return (d.length > 0);
    }
public boolean insertBID(ArrayList list) {
        boolean re = false;
        //com.magbel.util.DatetimeFormat df = new com.magbel.util.DatetimeFormat();

       // DateFormat dfs = new SimpleDateFormat("dd/MM/yyyy");
        String query = "insert into AM_ASSET_BID("+
                "ASSET_CODE,ASSET_ID,REGISTRATION_NO,BRANCH_ID,CATEGORY_ID," +
                "DESCRIPTION,COST_PRICE," +
                "NBV,DATE_PURCHASED,BRANCH_CODE,CATEGORY_CODE,DEPT_ID,DEP_RATE,ASSET_MAKE," +
                "Section_id,Vendor_AC,Asset_Model,Asset_Serial_No," +
                "Supplier_Name,Asset_User," +
                "Asset_Maintenance,Accum_Dep,Monthly_Dep,Dep_End_Date,Residual_Value,Authorized_By,Wh_Tax," +
                "Wh_Tax_Amount,Req_Redistribution,Posting_Date,Effective_Date,Purchase_Reason," +
                "Useful_Life,Total_Life,Location,Remaining_Life,Vatable_Cost,Vat,Req_Depreciation," +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,Raise_Entry,Dep_Ytd," +
                "Section,Asset_Status,State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed," +
                "PROVINCE,Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date," +
                "SECTION_CODE,DEPT_CODE,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID," +
                "BAR_CODE,SBU_CODE,LPO,supervisor,defer_pay," +
                "OLD_ASSET_ID,WHT_PERCENT," +
                "Post_reject_reason,Finacle_Posted_Date,system_ip,mac_address,Bid_Start_Date,Bid_End_Date,BID,Creation_Date,LOCATION_CODE)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection con = null;
        PreparedStatement ps = null;
        //ResultSet rs = null;
        magma.net.vao.Asset bd = null;

        int[] d = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (magma.net.vao.Asset) list.get(i);

                int assetcode = bd.getAssetCode();
                String assetId = bd.getId();
                String registration = bd.getRegistrationNo();
                String branch = bd.getBranchId();
                String cateory = bd.getCatID();
                String description = bd.getDescription();
                double costprice = bd.getCost();
                double nbv = bd.getNbv();
                String datePurchased = bd.getDatePurchased();
              //  String checkBid = bd.getChkBid();
                String checkBid = "N";
                //String bid_Period = bd.getBidPeriod();

                if(datePurchased.trim() == null  || datePurchased.trim().equalsIgnoreCase("null"))
				 {
                		datePurchased = df.getDateTime();

	              }

                String branchcode = bd.getBarCode();
                String categorycode = bd.getCategoryCode();
                String deptID  = bd.getDepartmentId();
                double depRate = bd.getDepreciationRate();
                String asset_mark = bd.getAssetMake();
                //start here
                String asset_model = bd.getAssetmodel();
				String  Serino = bd.getSerialNo();
				int sectionID = bd.getSectionId();
				String engno = bd.getEngineNo();
				   if(engno == null || engno.equals("")){
					   engno = "0";
	                }
				int suppliername = bd.getSupplierName();
				String asserUser = bd.getAssetUser();
				int  assetmaintain = bd.getAssetMaintain();
				double acumulatedep = bd.getAccumulatedDepreciation();
				double monthdep = bd.getMonthlyDepreciation();
				String depEnddate = bd.getDepreciationEndDate();
				//System.out.print("Depreciateion End DATE................"+depEnddate);
				//System.out.print("DATE AMD Time here................"+df.getDateTime());
				 if(depEnddate.trim() == null  || depEnddate.trim().equalsIgnoreCase("null"))
				 {
	                	depEnddate = df.getDateTime();
//	                	System.out.print("actuall values for date................"+depEnddate);
	              }
				double resdiualval = bd.getResidualValue();
				String authorizby = bd.getAuthorizeBy();
				String whtax = bd.getWh_tax();
				String postDate = bd.getPostingDate();
				 if(postDate.trim() == null  || postDate.trim().equalsIgnoreCase("null"))
				 {
					 postDate = df.getDateTime();

	              }
//    			System.out.println("  bd.getUseful_Life(): "+bd.getUseful_Life()+"  bd.getWh_Tax_Amount(): "+bd.getWh_Tax_Amount()+"  bd.getTotalLife(): "+bd.getTotalLife()+"  bd.getRemainingLife(): "+bd.getRemainingLife()+"   bd.getLocation(): "+bd.getLocation());
				double  whtax_amount = bd.getWh_Tax_Amount();
				int usefulLife = bd.getUseful_Life();
				int totallife = bd.getTotalLife();
				int remininlife = bd.getRemainingLife();
				int location = bd.getLocation();
				//location = Integer.parseInt(records.getCodeName("select location_Id from AM_GB_LOCATION where Location_Code = '"+bd.getLocationCode()+"'"));
				String reqdistribution = bd.getReq_Redistribution();
				String reqdepreciation = bd.getReq_Depreciation();
				String  subjectToval = bd.getSubject_TO_Vat();
				String  whoTorem = bd.getWho_TO_Rem();
				String  email1 = bd.getEmail1();
				String  whoTorem2 = bd.getWho_To_Rem_2();
				String email2 = bd.getEmail2();
				String  raiseEntry = bd.getEntryRaised();
				double  depYearToDate = bd.getDepreciationYearToDate();
				int states = bd.getState();
				String  assetStatus = bd.getAsset_status();
				String section = bd.getSection();
				int drivers = bd.getDriver();
				String spare1 = bd.getSpare1();
				String spare2 = bd.getSpare2();
				int userID = bd.getUserid();
				String disposeDate = bd.getDate_Disposed();
				int provinces = bd.getPROVINCE();
				String multiple = bd.getMultiple();
				String warStart = bd.getWAR_START_DATE();
				 if(warStart.trim() == null  || warStart.trim().equalsIgnoreCase("null"))
				 {
					 warStart = df.getDateTime();

	              }
				int warMonth = bd.getWAR_MONTH();
				String warExpire = bd.getWAR_EXPIRY_DATE();
				 if(warExpire.trim() == null  || warExpire.trim().equalsIgnoreCase("null"))
				 {
					 warExpire = df.getDateTime();

	              }
				String lastDepDate = bd.getLast_Dep_Date();
				 if(lastDepDate.trim() == null  || lastDepDate.trim().equalsIgnoreCase("null"))
				 {
					 lastDepDate = df.getDateTime();

	              }
				String branchcodde = bd.getBranchCode();
				String sectioncodes = bd.getSECTION_CODE();
				String depCode = bd.getDepcode();
				String sbuCode = bd.getSbuCode();
				String CateCode = bd.getCategoryCode();
				double amountPtd = bd.getAMOUNT_PTD();
				double amountRem = bd.getAMOUNT_REM();
				String partyPay = bd.getPART_PAY();
				String fullyPay = bd.getFULLY_PAID();
				String gruppId = bd.getGROUP_IDd();
				String barcode = bd.getBarcode2();
				String Lpo = bd.getLPO();
				String supervisors = bd.getSupervisor();
				String deferPay = bd.getDefer_pay();
				String oldAssetId = bd.getOLD_ASSET_ID();
				int whtPercent = bd.getWHT_PERCENT();
				String postRejection = bd.getPost_reject_reason();
				String finaclepost = bd.getFinacle_Posted_Date();
				String vendorAc = bd.getVendorAc();
				String systemIp = bd.getSystem_ip();
				String macAdress = bd.getMac_address();
				String purchaseReason = bd.getPurchaseReason();
				double vatableCost = bd.getVatable_Cost();
				double vat = bd.getVat();
                String locationCode = bd.getLocationCode();

				String effectiveD = bd.getEffectivedate2();
				 if(effectiveD.trim() == null  || effectiveD.trim().equalsIgnoreCase("null"))
				 {
					 effectiveD = df.getDateTime();

	              }



                if (registration == null || registration.equals("")) {
                    registration = "0";
                }
                if (description == null || description.equals("")) {
                    description = "";
                }

                if (assetId == null || assetId.equals("")) {
                	assetId = "";
                }
                if (branch == null || branch.equals("")) {
                	branch = "0";
                }
                if (cateory == null || cateory.equals("")) {
                	cateory = "0";
                }
                if (description == null || description.equals("")) {
                	description = "";
                }
                if (branchcode == null || branchcode.equals("")) {
                	branchcode = "0";
                }
                if (categorycode == null || categorycode.equals("")) {
                	categorycode = "0";
                }
                if (asset_mark == null || asset_mark.equals("")) {
                	asset_mark = "0";
                }
                if (asset_model == null || asset_model.equals("")) {
                	asset_model = "0";
                }

                if(disposeDate.trim() == null  || disposeDate.trim().equalsIgnoreCase("null"))
				 {
                	disposeDate = df.getDateTime();

	             }
                if(warStart.trim() == null  || warStart.trim().equalsIgnoreCase("null"))
				 {
                	warStart = df.getDateTime();

	             }
                if(warExpire.trim() == null  || warExpire.trim().equalsIgnoreCase("null"))
				 {
                	warExpire = df.getDateTime();

	             }
                if(lastDepDate.trim() == null  || lastDepDate.trim().equalsIgnoreCase("null"))
				 {
                	lastDepDate = df.getDateTime();


				 }
                if(finaclepost.trim() == null  || finaclepost.trim().equalsIgnoreCase("null"))
				 {
                	finaclepost = df.getDateTime();

	             }
                if (locationCode == null || locationCode.equals("")) {
                	locationCode = "0";
                }
 //               System.out.print("Start Date values... "+bd.getStart_Date()+" End Date Value.. "+bd.getEnd_Date());
                String bid_start_date = bd.getStart_Date().substring(0,10);
				String bid_end_date = bd.getEnd_Date().substring(0,10);
                ps.setInt(1, assetcode);
                ps.setString(2, assetId);
                ps.setString(3, registration);
                ps.setString(4, branch);
                ps.setString(5, cateory);
                ps.setString(6, description);
                ps.setDouble(7, costprice);
                ps.setDouble(8, nbv);
                ps.setDate(9, df.dateConvert(datePurchased));
                ps.setString(10, branchcode);
                ps.setString(11, categorycode);
                ps.setString(12, deptID);
                ps.setDouble(13,depRate);
                ps.setString(14, asset_mark);
                ps.setInt(15, sectionID);
                ps.setString(16, vendorAc);
                ps.setString(17, asset_model);
                ps.setString(18,Serino);
                ps.setInt(19, suppliername);
                ps.setString(20,asserUser);
                ps.setInt(21, assetmaintain);
                ps.setDouble(22, acumulatedep);
                ps.setDouble(23, monthdep);
                ps.setDate(24,df.dateConvert(depEnddate));
                ps.setDouble(25, resdiualval);
                ps.setString(26,authorizby);
                ps.setString(27,whtax);
                ps.setDouble(28, whtax_amount);
                ps.setString(29,reqdistribution);
                ps.setDate(30, df.dateConvert(postDate));
//              ps.setDate(25, dateConvert(postDate));
                ps.setDate(31,dateConvert(effectiveD));
                ps.setString(32,purchaseReason);
                ps.setInt(33, usefulLife);
                ps.setInt(34, totallife);
                ps.setInt(35, location);
                ps.setInt(36,remininlife);
                ps.setDouble(37, vatableCost);
                ps.setDouble(38, vat);
                ps.setString(39,reqdepreciation);
                ps.setString(40, subjectToval);
                ps.setString(41,whoTorem);
                ps.setString(42,email1);
                ps.setString(43,whoTorem2);
                ps.setString(44, email2);
                ps.setString(45,raiseEntry);
                ps.setDouble(46, depYearToDate);
                ps.setString(47, section);
                ps.setString(48, assetStatus);
                ps.setInt(49,states);
                ps.setInt(50,drivers);
                ps.setString(51,spare1);
                ps.setString(52, spare2);
                ps.setInt(53, userID);
                ps.setDate(54,df.dateConvert(disposeDate));
                ps.setInt(55,provinces);
                ps.setString(56, multiple);
                ps.setDate(57, df.dateConvert(warStart));
                ps.setInt(58, warMonth);
                ps.setDate(59, df.dateConvert(warExpire));
                ps.setDate(60, df.dateConvert(lastDepDate));
                ps.setString(61, sectioncodes);
                ps.setString(62, depCode);
                ps.setDouble(63, amountPtd);
                ps.setDouble(64, amountRem);
                ps.setString(65, partyPay);
                ps.setString(66, fullyPay);
                ps.setString(67, gruppId);
                ps.setString(68, barcode);
                ps.setString(69, sbuCode);
                ps.setString(70, Lpo);
                ps.setString(71, supervisors);
                ps.setString(72, deferPay);
                ps.setString(73, oldAssetId);
                ps.setInt(74, whtPercent);
                ps.setString(75, postRejection);
                ps.setDate(76,df.dateConvert(finaclepost));
                ps.setString(77, systemIp);
                ps.setString(78, macAdress);
                ps.setDate(79,df.dateConvert(java.sql.Date.valueOf(bid_start_date)));
                ps.setDate(80, df.dateConvert(java.sql.Date.valueOf(bid_end_date)));
                ps.setString(81, checkBid);
                ps.setDate(82, df.dateConvert(new java.util.Date()));
                ps.setString(83, locationCode);
                ps.addBatch();
            }
            d = ps.executeBatch();
           // System.out.println("Executed Successfully ");
            dbConnection.closeConnection(con, ps);
        } catch (Exception ex) {
            System.out.println("Error insertBIDING() In FleetHistoryManager -> "+ex.getMessage()) ;
            		ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }
/*public ArrayList findAssetBIDQuery(String queryFilter){

        double minCost =0;
        double maxCost =0;



         String selectQuery =
                "SELECT Asset_id,Registration_No,Description,Vendor_AC,Date_purchased," +
                "Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value," +
                "Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Phone_No,Email_Address " +
                "FROM AM_ASSET_BID_SELECTION WHERE ASSET_ID IS NOT NULL " + queryFilter;
       // System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String assetMake = rs.getString("ASSET_MAKE");
                String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Location = rs.getInt("Location");
                int useageYears = rs.getInt("Useage_Years");
                double bidval = rs.getDouble("Bid_Value");
                String fisrtname = rs.getString("First_Name");
                String surname = rs.getString("Sur_Name");
                String category = rs.getString("Category_Id");
                String bidcode = rs.getString("Bid_Code");
                String address1 = rs.getString("Address_1");
                String address2 = rs.getString("Address_2");
                String phone = rs.getString("Phone_No");
                String email = rs.getString("Email_Address");

                Asset aset = new Asset();
                aset.setId(id);
                aset.setRegistrationNo(registrationNo);
                aset.setDescription(description);
                aset.setDatePurchased(purchaseDate);
                aset.setAsset_status(asset_status);
                aset.setAssetMake(assetMake);
                aset.setAssetmodel(Asset_Model);
                aset.setVendorAc(vendor_acct);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setLocation(Location);
                aset.setUseageYears(useageYears);
                aset.setBid(bidval);
                aset.setFirstname(fisrtname);
                aset.setSurname(surname);
                aset.setCategory(category);
                aset.setBidcode(bidcode);
                aset.setAddress1(address1);
                aset.setAddress2(address2);
                aset.setPhoneNo(phone);
                aset.setEmail1(email);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL AM_ASSET_BID_SELECTIONt ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }*/

public boolean updatebid(java.util.ArrayList list) {

		String query = "UPDATE AM_ASSET_BID_SELECTION "+
				"SET Bid_Value = ? "
			+ " WHERE Asset_id=? AND First_Name=? ";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int[] d = null;
		boolean done = false;
		magma.net.vao.Asset asset = null;
		try {


			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			for(int i = 0; i <list.size(); i++){
				asset = (magma.net.vao.Asset)list.get(i);

				ps.setDouble(1,asset.getBid());
				System.out.print("Bid-->>>"+asset.getBid());
				ps.setString(2,asset.getId());
				System.out.print("ID-->>>"+asset.getBid());
				ps.setString(3,asset.getFirstname());
				System.out.print("FIRSTNAME-->>>"+asset.getFirstname());
				ps.addBatch();
				//done=( ps.executeUpdate()!=-1);
			}
			d = ps.executeBatch();

		}catch (Exception ex) {
			System.out.println("WARN: Error Upating Asset Bid  ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}

public boolean deleteBid(java.util.ArrayList list) {
		String query = "DELETE FROM AM_ASSET_BID_SELECTION"
				+ " WHERE Asset_id=? AND First_Name=? ";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		magma.net.vao.Asset asset = null;
		int[] d = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			for (int i = 0; i < list.size(); i++) {
				asset = (magma.net.vao.Asset)list.get(i);

				ps.setString(1, asset.getId());
				ps.setString(2, asset.getFirstname());

				ps.addBatch();
			}
			d = ps.executeBatch();

		} catch (Exception ex) {
			System.out.println("WARN: Error removing Bid Records ->" + ex);
		} finally {
			closeConnection(con, ps);
		}
		return (d.length > 0);
	}

 public ArrayList findAssetBIDQuery(String queryFilter){

        double minCost =0;
        double maxCost =0;



         String selectQuery =
                "SELECT Asset_id,Registration_No,Description,Vendor_AC,Date_purchased," +
                "Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value," +
                "Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Phone_No,Email_Address " +
                "FROM AM_ASSET_BID_SELECTION WHERE ASSET_ID IS NOT NULL " + queryFilter;
       // System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String description = rs.getString("DESCRIPTION");
                String assetMake = rs.getString("ASSET_MAKE");
                String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Location = rs.getInt("Location");
                int useageYears = rs.getInt("Useage_Years");
                double bidval = rs.getDouble("Bid_Value");
                String fisrtname = rs.getString("First_Name");
                String surname = rs.getString("Sur_Name");
                String category = rs.getString("Category_Id");
                String bidcode = rs.getString("Bid_Code");
                String address1 = rs.getString("Address_1");
                String address2 = rs.getString("Address_2");
                String phone = rs.getString("Phone_No");
                String email = rs.getString("Email_Address");

                Asset aset = new Asset();
                aset.setId(id);
                aset.setRegistrationNo(registrationNo);
                aset.setDescription(description);
                aset.setDatePurchased(purchaseDate);
                aset.setAsset_status(asset_status);
                aset.setAssetMake(assetMake);
                aset.setAssetmodel(Asset_Model);
                aset.setVendorAc(vendor_acct);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setLocation(Location);
                aset.setUseageYears(useageYears);
                aset.setBid(bidval);
                aset.setFirstname(fisrtname);
                aset.setSurname(surname);
                aset.setCategory(category);
                aset.setBidcode(bidcode);
                aset.setAddress1(address1);
                aset.setAddress2(address2);
                aset.setPhoneNo(phone);
                aset.setEmail1(email);
                list.add(aset);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL AM_ASSET_BID_SELECTIONt ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
public java.util.ArrayList findAppUsers(String usern,String passwords){



  String selectQuery ="select user_name,password from am_gb_User where user_name='"+usern+"' AND password='"+passwords+"' ";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    java.util.ArrayList list = new java.util.ArrayList();
    magma.net.vao.Asset asset = null;

    boolean done = false;

    try{
        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        rs = ps.executeQuery();
//        System.out.print("DID IT GOT HERE ??????????? "+selectQuery);
        while(rs.next()){
//        	 System.out.print("DID IT GOT HERE ??????????? PART 2 "+selectQuery);

            String username = rs.getString("user_name");
            String password = rs.getString("password");
//            System.out.println("getting Current USER NAME "+username+"AND PASSWORD "+password);
            asset = new  magma.net.vao.Asset();
            asset.setUsername(username);
            asset.setPassword(password);
            list.add(asset);


        }

    } catch (Exception e) {
        System.out.println("INFO:Error fetching and FINDING User Details ->>>> ");
        e.printStackTrace();
    } finally {
        closeConnection(con, ps, rs);
    }

    return list;

}
 public boolean createBidSorting(magma.net.vao.Asset asset,String bittitle,String biddate,String userId,String workstationIp,String workstationAddName) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps0 = null;
		PreparedStatement psd = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement psdete = null; 
		
		boolean done = false;
		boolean done0 = false;
		boolean done1 = false;
		boolean done2 = false;
		boolean done3 = false;
		boolean done4 =false;
		boolean doned =false;
		boolean deleted =false;
		String bidHeader = records.getCodeName("SELECT BID_TITLE FROM AM_BID_Period_SetUp WHERE MTID ='"+bittitle+"'");
		/*String query1 = "insert into AM_BID_RECORD_DETAIL(Asset_id,Registration_No,Description,Vendor_AC,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
+"Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Email_Address,Phone_No,Initials)"
+" Select Asset_id,Registration_No,Description,Vendor_AC,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
+"Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Email_Address,Phone_No,Initials from AM_ASSET_BID_SELECTION order by ASSET_ID,Description,BID_Value,Useage_Years,Date_purchased";
		    */
		 /* String query2  = "insert into AM_BID_RESULT(Asset_id,Registration_No,Description,Vendor_AC,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
			  +"Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Email_Address,Phone_No,Initials,Bid_Title,Bid_Date)"
			  +" Select Asset_id,Registration_No,Description,Vendor_AC,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
			  +"Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Email_Address,Phone_No,Initials,Bid_Title,Bid_Date from AM_BID_RECORD_DETAIL order by BID_Value ";
			*/

		  //String query2  = "insert into AM_BID_RESULT(Asset_id,Registration_No,Description,Vendor_AC,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
			 // +"Useage_Years,Asset_Status,First_Name,Sur_Name,Category_Id,Bid_Code,Address_1,Address_2,Email_Address,Phone_No,Initials,Bid_Title,Bid_Date)"
			 // +" select s.asset_id,bid_value,bid_limit from AM_ASSET_BID_SELECTION s,AM_BID_LIMIT l where s.asset_id = l.asset_id and bid_value >=bid_limit ";
		 String EXIST_TABLE_QUERY = "IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[BID_RESULT_TEMP]') AND type in (N'U')) "+
				 "BEGIN DROP TABLE BID_RESULT_TEMP PRINT 'Exists Table Deleted' END";
		 String DELETE_RESULT_QUERY = "DELETE FROM AM_BID_RESULT";		 
		  String CREATE_TABLE_QUERY = "SELECT TOP 0 * INTO BID_RESULT_TEMP FROM AM_ASSET_BID_SELECTION";
		  String BID_TEMP_QUERY = "INSERT INTO BID_RESULT_TEMP select top 1 with ties *from AM_ASSET_BID_SELECTION "+
		  		"order by row_number() over (partition by BID_TAG order by bid_value desc)";
		  String query2 ="insert into am_bid_result(Bid_Code,Asset_id,Registration_No,Description,Vendor_Ac,Date_Purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Bid_Value, "
		  		+"Category_Id,Useage_Years,Asset_Status,First_Name,Sur_Name,Address_1,Address_2,Email_Address,Phone_No,Initials,Creation_Date,Bid_Title,Bid_Date,User_Id,Ip_Address,Mac_Address,BID_TAG,STAFFID,BASE_PRICE)"
		  		+"select s.Bid_Code,s.asset_id,s.Registration_No,s.Description,s.Vendor_Ac,s.Date_Purchased,s.Asset_Make,s.Asset_Model,s.Asset_Serial_No,s.Asset_Engine_No,s.Location,s.Bid_Value,s.Category_Id, "
		  		+"s.Useage_Years,s.Asset_Status,s.First_Name,s.Sur_Name,s.Address_1,s.Address_2,s.Email_Address,s.Phone_No,s.Initials, "
		  		+"s.Creation_Date,'"+bidHeader+"','"+df.dateConvert(new java.util.Date())+"',"+userId+",'"+workstationIp+"','"+workstationAddName+"',l.BID_TAG,STAFFID,BASE_PRICE from BID_RESULT_TEMP s,AM_BID_LIMIT l where s.asset_id = l.asset_id and bid_value >=bid_limit and s.Asset_id = l.Asset_id and s.Bid_Period = '"+bittitle+"' and s.BID is null";
  

		  String UPDATE_QUERY = "UPDATE AM_ASSET_BID_SELECTION SET Bid =?,Bid_Date=? WHERE BID IS NULL";
		  
		  String UPDATE_PERIOD_QUERY = "UPDATE AM_BID_Period_SetUp SET STATUS = 'PROCESSED' WHERE MTID = '"+bittitle+"' ";
		  
	//	  String UPDATE_QUERY = "UPDATE AM_ASSET_BID_SELECTION SET Bid = 'YES',Bid_Date='"+biddate+"' WHERE BID IS NULL";

		  String UPDATE_QUERY2 = "UPDATE AM_ASSET_BID SET BIDPROCESS=?,BIDPROCESS_DATE=?,BID=?,Ip_Address=?,Mac_Address=? WHERE BIDPROCESS IS NULL";

//		  System.out.print("BID EXIST_TABLE_QUERY HERE ??????????? "+EXIST_TABLE_QUERY);
//		  System.out.print("BID CREATE_TABLE_QUERY HERE ??????????? "+CREATE_TABLE_QUERY);
//		  System.out.print("BID BID_TEMP_QUERY HERE ??????????? "+BID_TEMP_QUERY);
//		  System.out.print("BID query2 HERE ??????????? "+query2);
//		  System.out.print("BID UPDATE_QUERY HERE ??????????? "+UPDATE_QUERY2);
//		  System.out.print("BID UPDATE_PERIOD_QUERY HERE ??????????? "+UPDATE_PERIOD_QUERY);
			try{ 
				con = getConnection("legendPlus");
				ps = con.prepareStatement(EXIST_TABLE_QUERY);
				done =(ps.executeUpdate()!=-1);
				psdete = con.prepareStatement(DELETE_RESULT_QUERY);
				deleted =(psdete.executeUpdate()!=-1);
				ps0 = con.prepareStatement(CREATE_TABLE_QUERY);
				done0 =(ps0.executeUpdate()!=-1);
				ps1 = con.prepareStatement(BID_TEMP_QUERY);
				done1 =(ps1.executeUpdate()!=-1);
				ps2 = con.prepareStatement(query2); 
				done2 =(ps2.executeUpdate()!=-1);
				ps3 = con.prepareStatement(UPDATE_QUERY);
				ps3.setString(1, "YES");
				ps3.setString(2,biddate);
				done3 =(ps3.executeUpdate()!=-1);
				
				psd = con.prepareStatement(UPDATE_PERIOD_QUERY);
				doned =(psd.executeUpdate()!=-1);
				
				ps4 = con.prepareStatement(UPDATE_QUERY2);
				ps4.setString(1, "YES");
				ps4.setDate(2, df.dateConvert(new java.util.Date()));
				ps4.setString(3, "Y");
				ps4.setString(4, workstationIp);
				ps4.setString(5, workstationAddName);
				done4 =(ps4.executeUpdate()!=-1);


		}catch(Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating bidsorting ->" + e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
			closeConnection(con, ps0);
			closeConnection(con, psd);
			closeConnection(con, psdete);
			closeConnection(con, ps1);
			closeConnection(con, ps2);
			closeConnection(con, ps3);
			closeConnection(con, ps4);

		}
		return done2;

	}
 public ArrayList findBidByGroup(){


    String selectQuery ="select asset_id,Registration_No,Description,Vendor_Ac,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,max(Bid_Value)Bid_Value,Category_Id,Useage_Years,Asset_Status,Bid_Date,Creation_Date,User_Id,Ip_Address,Mac_Address from AM_BID_RESULT group by asset_id, "
	+"Asset_Model,Registration_No,Description,Vendor_Ac,Date_purchased,Asset_Make,Asset_Model,Asset_Serial_No,Asset_Engine_No,Location,Category_Id,Useage_Years,Asset_Status,Bid_Date,Creation_Date,User_Id,Ip_Address,Mac_Address ";


      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      ArrayList list = new ArrayList();

      try {
    	  con = getConnection("legendPlus");
          ps = con.prepareStatement(selectQuery);
          rs = ps.executeQuery();

          while(rs.next()){

              String id = rs.getString("asset_id");
              String Registration = rs.getString("Registration_No");
              String description = rs.getString("Description");
              String vendorAc =rs.getString("Vendor_Ac");
              String  datePurchase = rs.getString("Date_purchased");
              String assetMake = rs.getString("Asset_Make");
              String assetModel = rs.getString("Asset_Model");
              String assetSerial = rs.getString("Asset_Serial_No");
              String assetEng = rs.getString("Asset_Engine_No");
              int location = rs.getInt("Location");
              double bidval = rs.getDouble("Bid_Value");
              int cateGoryId = rs.getInt("Category_Id");
              int useageY = rs.getInt("Useage_Years");
              String assetStatus = rs.getString("Asset_Status");
              //String bidTitle = rs.getString("Bid_Title");
              String bidDate= rs.getString("Bid_Date");
              String createDate= rs.getString("Creation_Date");
              int Userid= rs.getInt("User_Id");
              String Ip= rs.getString("Ip_Address");
              String macAddress = rs.getString("Mac_Address");

              Asset aset = new Asset();
              aset.setId(id);
              aset.setRegistrationNo(Registration);
              aset.setDescription(description);
              aset.setVendorAc(vendorAc);
              aset.setDatePurchased(datePurchase);
              aset.setAssetMake(assetMake);
              aset.setAssetmodel(assetModel);
              aset.setSerialNo(assetSerial);
              aset.setEngineNo(assetEng);
              aset.setLocation(location);
              aset.setBid(bidval);
              aset.setCategoryId(cateGoryId);
              aset.setUseageYears(useageY);
              aset.setAsset_status(assetStatus);
              //aset.setBidtitle(bidTitle);
              aset.setBiddate(bidDate);
              aset.setUserid(Userid);
              aset.setMac_address(macAddress);
              list.add(aset);

          }

      } catch (Exception e) {
          System.out.println("INFO:Error fetching ALL AM_ASSET_BID_Result findBidByGroup ->>>> " +
                             e.getMessage());
          e.printStackTrace();

      } finally {
          closeConnection(con, ps, rs);
      }

      return list;

  }
 public ArrayList findAssetBIDRESULT(String queryFilter){

      double minCost =0;
      double maxCost =0;

   // String selectQuery ="select asset_id,Asset_Model,Asset_Status,Description,Bid_Date,Bid_Title," +
    		//"max(bid_Value)Bid_Value from AM_BID_RESULT group by asset_id," +
    		//"Asset_Model,Asset_Status,Description,Bid_Date,Bid_Title order by asset_id desc "+queryFilter;

    String selectQuery ="select asset_id,coalesce(Asset_Model,0) AS Asset_Model,Asset_Status,Description,Bid_Date,Bid_Title," +
	"max(bid_Value)Bid_Value,BID_TAG,STAFFID from AM_BID_RESULT group by asset_id, " +
	"coalesce(Asset_Model,0),Asset_Status,Description,Bid_Date,Bid_Title,BID_TAG,STAFFID "+queryFilter;
//     System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
      Connection con = null;
      PreparedStatement ps = null;

      ResultSet rs = null;
      ArrayList list = new ArrayList();

      try {
          con = getConnection("legendPlus");
          ps = con.prepareStatement(selectQuery);
          rs = ps.executeQuery();

          while (rs.next()) {

              String id = rs.getString("ASSET_ID");
              String description = rs.getString("DESCRIPTION");
              String asset_status=rs.getString("ASSET_STATUS");
              String Asset_Model = rs.getString("Asset_Model");
              double bidval = rs.getDouble("Bid_Value");
              String bidtitle = rs.getString("Bid_Title");
              String biddate = rs.getString("Bid_Date");
              String bidTag = rs.getString("BID_TAG");
              String staffId = rs.getString("STAFFID");

              Asset aset = new Asset();
              aset.setId(id);
              aset.setDescription(description);
              aset.setAsset_status(asset_status);
              aset.setAssetmodel(Asset_Model);
              aset.setBid(bidval);
              aset.setBidtitle(bidtitle);
              aset.setBiddate(biddate);
              aset.setBidTag(bidTag);
              aset.setStaffId(staffId);
              list.add(aset);

          }

      } catch (Exception e) {
          System.out.println("INFO:Error fetching ALL AM_ASSET_BID_Result Menue ->>>> " +
                             e.getMessage());
      } finally {
          closeConnection(con, ps, rs);
      }

      return list;

  }
 public boolean createBIDPeriod(magma.net.vao.Bid_Period period) {

	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String query = "INSERT INTO AM_BID_Period_SetUp(BID_Start_Date,BID_End_Date"
			+ " ,BID_Title,User_Id,create_date,Status,location)"
			+ " VALUES(?,?,?,?,?,?,?)";

	try {
		con = getConnection("legendPlus");
		ps = con.prepareStatement(query);
		ps.setString(1, period.getStartDate());
		ps.setString(2, period.getEndDate());
		ps.setString(3,period.getBid_title());
		ps.setInt(4,period.getUserId());
		ps.setDate(5, df.dateConvert(new java.util.Date()));
		ps.setString(6,period.getStatus());
		ps.setString(7,period.getLocation());
		done=( ps.executeUpdate()!=-1);

	} catch (Exception e) {
		System.out.println(this.getClass().getName()
				+ " ERROR:Error creating Bid Period ->" + e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
	return done;

}
 public java.util.List getBidPeriodByQuery(String filter) {
	java.util.List _list = new java.util.ArrayList();
	magma.net.vao.Bid_Period period = null;
	String query = "SELECT MTID,BID_Start_Date,BID_End_Date,BID_Title,User_Id"
			+ "  ,Create_Date,Status,Location"
			+ " FROM AM_BID_Period_SetUp ";

	query = query + filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection("legendPlus");
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String mtId = rs.getString("MTID");
			String bid_State_Dat = rs.getString("BID_Start_Date");
			String bid_End_Dat = rs.getString("BID_End_Date");
			String bid_title =rs.getString("BID_Title");
			int userId = rs.getInt("User_Id");
			String createDate = rs.getString("create_date");
			String stateStatus = rs.getString("Status");
			String location = rs.getString("Location");
			period = new magma.net.vao.Bid_Period(bid_State_Dat, bid_End_Dat,bid_title,userId, createDate,stateStatus,location);
			period.setId(mtId);
			_list.add(period);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}
 
 public java.util.List getBidPeriodListByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		magma.net.vao.Bid_Period period = null;
		String query = "SELECT MTID,BID_Start_Date,BID_End_Date,BID_Title,User_Id"
				+ "  ,Create_Date,Status,Location"
				+ " FROM AM_BID_Period_SetUp ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection("legendPlus");
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String mtId = rs.getString("MTID");
				String bid_State_Dat = rs.getString("BID_Start_Date");
				String bid_End_Dat = rs.getString("BID_End_Date");
				String bid_title = rs.getString("BID_Title");
				int userId = rs.getInt("User_Id");
				String createDate = rs.getString("create_date");
				String status = rs.getString("Status");
				String location = rs.getString("Location");
				period = new magma.net.vao.Bid_Period(bid_State_Dat, bid_End_Dat,bid_title,userId, createDate,status,location);
				period.setId(mtId);
				_list.add(period);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}


 
 public void BidPeriodExpired(String query_r)
{

	Connection con = null;
    PreparedStatement ps = null;

	try {
	    con = dbConnection.getConnection("legendPlus");

	    	ps = con.prepareStatement(query_r);
	           int i =ps.executeUpdate();

	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: updateAssetStatusChange()>>>>>" + ex);
	        } finally {
	            dbConnection.closeConnection(con, ps);
	        }


	}
 public java.util.ArrayList getBidPeriodByStatus(String status){
	String filter = " WHERE Status='" + status +"'";
	java.util.ArrayList _list = (java.util.ArrayList)getBidPeriodByQuery(filter);
	return _list;
}
public ArrayList findAssetBIDRESULT(){

    double minCost =0;
    double maxCost =0;

 // String selectQuery ="select asset_id,Asset_Model,Asset_Status,Description,Bid_Date,Bid_Title," +
  		//"max(bid_Value)Bid_Value from AM_BID_RESULT group by asset_id," +
  		//"Asset_Model,Asset_Status,Description,Bid_Date,Bid_Title order by asset_id desc "+queryFilter;

  String selectQuery ="select asset_id,Asset_Model,Description," +
	"max(bid_Value)Bid_Value from AM_BID_RESULT group by asset_id, " +
	"Asset_Model,Description order by BID_Value desc ";


    Connection con = null;
    PreparedStatement ps = null;

    ResultSet rs = null;
    ArrayList list = new ArrayList();

    try {
        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        rs = ps.executeQuery();

        while(rs.next()){

            String id = rs.getString("ASSET_ID");
            String description = rs.getString("DESCRIPTION");
            String Asset_Model = rs.getString("Asset_Model");
            double bidval = rs.getDouble("Bid_Value");


            Asset aset = new Asset();
            aset.setId(id);
            aset.setDescription(description);
            aset.setAssetmodel(Asset_Model);
            aset.setBid(bidval);
            list.add(aset);

        }

    }catch (Exception e){
        System.out.println("INFO:Error fetching ALL AM_ASSET_BID_Result Menue ->>>> " +
                           e.getMessage());
    }finally{
        closeConnection(con, ps, rs);
    }

    return list;

}
public java.util.List getBidPeriodByQuery() {
	java.util.List _list = new java.util.ArrayList();
	magma.net.vao.Bid_Period period = null;
	String query = "SELECT MTID,BID_Start_Date,BID_End_Date,BID_Title,User_Id"
			+ "  ,Create_Date,Status,Location"
			+ " FROM AM_BID_Period_SetUp ";

	//query = query + filter;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null;

	try {
		c = getConnection("legendPlus");
		s = c.createStatement();
		rs = s.executeQuery(query);
		while (rs.next()) {
			String mtId = rs.getString("MTID");
			String bid_State_Dat = rs.getString("BID_Start_Date");
			String bid_End_Dat = rs.getString("BID_End_Date");
			String bid_title = rs.getString("BID_Title");
			int userId = rs.getInt("User_Id");
			String createDate = rs.getString("create_date");
			String stateStatus = rs.getString("Status");
			String location = rs.getString("Location");
			period = new magma.net.vao.Bid_Period(bid_State_Dat, bid_End_Dat,bid_title,userId, createDate,stateStatus,location);
			period.setId(mtId);
			_list.add(period);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	finally {
		closeConnection(c, s, rs);
	}
	return _list;

}


    public ArrayList findAssetByQueryUncapitalized(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code   " +
                "FROM AM_ASSET_UNCAPITALIZED  WHERE ASSET_ID IS NOT NULL " + queryFilter;
 //       System.out.println("the query in findAssetByQueryUncapitalized is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQueryUncapitalized ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public ArrayList findAssetTransferByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

         String selectQuery =
                "SELECT a.ASSET_ID,a.REGISTRATION_NO,a.BRANCH_ID,a.DEPT_ID," +
                "a.CATEGORY_ID, a.DESCRIPTION,a.DATE_PURCHASED,a.DEP_RATE," +
                "a.ASSET_MAKE,a.ASSET_USER,a.ASSET_MAINTENANCE," +
                "a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,a.DEP_END_DATE," +
                "a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.SECTION , " +
                "a.NBV,a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.Asset_Status,a.asset_code,a.REVALUE_COST   " +
                "FROM AM_ASSET a, am_gb_bulkTransfer b WHERE a.ASSET_ID IS NOT NULL  AND a.Asset_id <> b.asset_id " +
                " AND b.STATUS = 'N' " + queryFilter;
 //       System.out.println("the query for findAssetTransferByQuery is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {   
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetTransferByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public ArrayList findStockByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery = "select *from ST_STOCK where  Asset_id !='' " +
        					queryFilter;
        		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findStockByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findStockByQueryy(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
                "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
                "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
                "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
                "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
                "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
                "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
                "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
                "system_ip,mac_address " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;
//        System.out.println("the query in findStockByQueryy is <<<<<<<<<<<<< "+ selectQuery);


       String query =
    	"SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
        "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
        "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
        "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
        "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION ," +
        "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status ," +
        "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance ," +
        "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason ," +
        "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation," +
        "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2," +
        "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE," +
        "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE," +
        "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE," +
        "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date," +
        "system_ip,mac_address " +
        "FROM ST_STOCK  where ASSET_ID NOT IN (select Asset_Id from am_asset_bid) "+queryFilter;

 
       String selectstockQuery =
    		   "SELECT DISTINCT a.ASSET_ID,a.ASSET_CODE,a.REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
    		              "a.CATEGORY_ID,SECTION,CATEGORY_CODE,a.DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
    		              "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
    		              "a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,DEP_END_DATE," +
    		              "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
    		              "a.NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
    		              "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
    		              "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
    		              "Useful_Life,a.Location,Vatable_Cost,Vat,Req_Depreciation,  " +
    		              "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
    		              "State,Driver,Spare_1,Spare_2,a.User_ID,Date_Disposed,PROVINCE,  " +
    		              "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
    		              "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
    		              "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date, ITEMTYPE, " +
    		              "system_ip,mac_address,a.Quantity,b.QUANTITY AS REQQUANTITY,b.project_code,b.NEWASSET_USER,b.REQUESTBRANCH " +
    		              "FROM ST_STOCK a, am_ad_TransferRequisition b, ST_STOCK_DISTRBUTE c  WHERE a.ASSET_ID IS NOT NULL "+
    					   "AND b.Asset_id = 'null' AND b.PROCESS_STATUS = 'A' AND a.BAR_CODE = c.RFID_TAG  " + queryFilter;
//      System.out.println("the query in findStockByQueryy is <<<<<<<<<<<<< "+ selectstockQuery);
 
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectstockQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                int assetcode = rs.getInt("ASSET_CODE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String branchcode = rs.getString("BRANCH_CODE");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String categorycode = rs.getString("CATEGORY_CODE");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
              //  String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String effectiveDate2 = rs.getString("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String sectionID = rs.getString("Section_id");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Supplier_Name = rs.getInt("Supplier_Name");
                int Asset_Maintenance = rs.getInt("Asset_Maintenance");
                String Authorized_By = rs.getString("Authorized_By");
                String Wh_Tax = rs.getString("Wh_Tax");
                double Wh_Tax_Amount = rs.getDouble("Wh_Tax_Amount");
                String Req_Redistribution = rs.getString("Req_Redistribution");
                String Purchase_Reason = rs.getString("Purchase_Reason");
                int Useful_Life = rs.getInt("Useful_Life");
                int Location = rs.getInt("Location");
                double Vatable_Cost = rs.getDouble("Vatable_Cost");
                double Vat = rs.getDouble("Vat");
                String Req_Depreciation = rs.getString("Req_Depreciation");
                String Subject_TO_Vat = rs.getString("Subject_TO_Vat");
                String Who_TO_Rem = rs.getString("Who_TO_Rem");
                String email1 = rs.getString("Email1");
                String Who_To_Rem_2 = rs.getString("Who_To_Rem_2");
                String email2 = rs.getString("Email2");
                int State = rs.getInt("State");
                int Driver = rs.getInt("Driver");
                String Spare_1 = rs.getString("Spare_1");
                String Spare_2 = rs.getString("Spare_2");
                int User_ID = rs.getInt("User_ID");
                String Date_Disposed = rs.getString("Date_Disposed");
                int PROVINCE = rs.getInt("PROVINCE");
                String Multiple = rs.getString("Multiple");
                String WAR_START_DATE = rs.getString("WAR_START_DATE");
                int WAR_MONTH = rs.getInt("WAR_MONTH");
                String WAR_EXPIRY_DATE = rs.getString("WAR_EXPIRY_DATE");
                String Last_Dep_Date = rs.getString("Last_Dep_Date");
                String SECTION_CODE = rs.getString("SECTION_CODE");
                String DEPT_CODE = rs.getString("DEPT_CODE");
                double AMOUNT_PTD = rs.getDouble("AMOUNT_PTD");
                double AMOUNT_REM = rs.getDouble("AMOUNT_REM");
                String PART_PAY = rs.getString("PART_PAY");
                String FULLY_PAID = rs.getString("FULLY_PAID");
                String GROUP_ID = rs.getString("GROUP_ID");
                String BAR_CODE = rs.getString("BAR_CODE");
                String SBU_CODE = rs.getString("SBU_CODE");
                String LPO = rs.getString("LPO");
                String supervisor = rs.getString("supervisor");
                String defer_pay = rs.getString("defer_pay");
                String OLD_ASSET_ID = rs.getString("OLD_ASSET_ID");
                int WHT_PERCENT = rs.getInt("WHT_PERCENT");
                String Post_reject_reason = rs.getString("Post_reject_reason");
                String Finacle_Posted_Date = rs.getString("Finacle_Posted_Date");
                String system_ip = rs.getString("system_ip");
                String mac_address = rs.getString("mac_address");
                int quantity =  rs.getInt("QUANTITY");
                int req_quantity =  rs.getInt("REQQUANTITY");
                String projectCode = rs.getString("PROJECT_CODE");
                String assetUser = rs.getString("NEWASSET_USER");
                int reqBranch =  rs.getInt("REQUESTBRANCH");
                String itemType = rs.getString("ITEMTYPE");
              //  DatetimeFormat dd = new DatetimeFormat();
                //dd.dateConvert(strDate)

                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetmodel(Asset_Model);
                aset.setSectionId(sectionID);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setSupplierName(Supplier_Name);
                aset.setAssetMaintain(Asset_Maintenance);
                aset.setAuthorizeBy(Authorized_By);
                aset.setWh_tax(Wh_Tax);
                aset.setWh_Tax_Amount(Wh_Tax_Amount);
                aset.setReq_Depreciation(Req_Redistribution);
                aset.setPurchaseReason(Purchase_Reason);
                aset.setUseful_Life(Useful_Life);
                aset.setLocation(Location);
                aset.setVatable_Cost(Vatable_Cost);
                aset.setVat(Vat);
                aset.setReq_Redistribution(Req_Redistribution);
                aset.setSubject_TO_Vat(Subject_TO_Vat);
                aset.setWho_TO_Rem(Who_TO_Rem);
                aset.setEmail1(email1);
                aset.setWho_To_Rem_2(Who_To_Rem_2);
                aset.setEmail2(email2);
                aset.setState(State);
                aset.setDriver(Driver);
                aset.setSpare1(Spare_1);
                aset.setSpare2(Spare_2);
                aset.setUserid(User_ID);
                aset.setDate_Disposed(Date_Disposed);
                aset.setPROVINCE(PROVINCE);
                aset.setMultiple(Multiple);
                aset.setWAR_START_DATE(WAR_START_DATE);
                aset.setWAR_MONTH(WAR_MONTH);
                aset.setWAR_EXPIRY_DATE(WAR_EXPIRY_DATE);
                aset.setLast_Dep_Date(Last_Dep_Date);
                aset.setSECTION_CODE(SECTION_CODE);
                aset.setDepcode(DEPT_CODE);
                aset.setAMOUNT_PTD(AMOUNT_PTD);
                aset.setAMOUNT_REM(AMOUNT_REM);
                aset.setPART_PAY(PART_PAY);
                aset.setFULLY_PAID(FULLY_PAID);
                aset.setGROUP_IDd(String.valueOf(GROUP_ID));
                aset.setBarcode2(BAR_CODE);
                aset.setSbuCode(SBU_CODE);
                aset.setLPO(LPO);
                aset.setSupervisor(supervisor);
                aset.setDefer_pay(defer_pay);
                aset.setOLD_ASSET_ID(OLD_ASSET_ID);
                aset.setWHT_PERCENT(WHT_PERCENT);
                aset.setPost_reject_reason(Post_reject_reason);
                aset.setFinacle_Posted_Date(Finacle_Posted_Date);
                aset.setSystem_ip(system_ip);
                aset.setMac_address(mac_address);
                aset.setVendorAc(vendor_acct);
                aset.setBranchCode(branchcode);
                aset.setCategoryCode(categorycode);
                aset.setEffectivedate2(effectiveDate2);
                aset.setAssetCode(assetcode);
                aset.setQuantity(quantity);
                aset.setUseageYears(req_quantity);
                aset.setProjectCode(projectCode);
                aset.setAssetUser(assetUser);
                aset.setDriver(reqBranch);
                aset.setItemType(itemType);
  //              System.out.println("reqBranch: "+reqBranch+""+aset.getDriver());



                list.add(aset);

//                minCost =Math.min(minCost, cost);
//                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
//                setMinCost(minCost);
//                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Stock in findStockByQueryy ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public ArrayList findStockByQueryforreq(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
                "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
                "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
                "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
                "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
                "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
                "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
                "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
                "system_ip,mac_address " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;
//        System.out.println("the query in findStockByQueryforreq is <<<<<<<<<<<<< "+ selectQuery);


       String query =
    	"SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
        "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
        "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
        "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
        "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION ," +
        "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status ," +
        "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance ," +
        "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason ," +
        "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation," +
        "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2," +
        "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE," +
        "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE," +
        "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE," +
        "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date," +
        "system_ip,mac_address " +
        "FROM ST_STOCK  where ASSET_ID NOT IN (select Asset_Id from am_asset_bid) "+queryFilter;


       String selectstockQuery =
    		   "SELECT a.ASSET_ID,a.ASSET_CODE,a.REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
    		              "a.CATEGORY_ID,SECTION,CATEGORY_CODE,a.DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
    		              "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
    		              "a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,DEP_END_DATE," +
    		              "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
    		              "a.NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
    		              "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
    		              "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
    		              "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
    		              "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
    		              "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
    		              "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
    		              "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
    		              "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
    		              "system_ip,mac_address,a.Quantity,b.QUANTITY AS REQQUANTITY,b.project_code,b.NEWASSET_USER " +
    		              "FROM ST_STOCK a, am_ad_TransferRequisition b  WHERE a.ASSET_ID IS NOT NULL "+
    					   "AND a.ASSET_ID = b.ASSET_ID AND b.PROCESS_STATUS = 'A' " + queryFilter;
     // System.out.println("the query in findStockByQueryforreq is <<<<<<<<<<<<< "+ selectstockQuery);

       String selectQuerybyGroup =
               "SELECT DESCRIPTION, ASSET_STATUS,BRANCH_ID,CATEGORY_ID,UNIT_PRICE " +
               "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter +
               "  GROUP BY DESCRIPTION, ASSET_STATUS,BRANCH_ID,CATEGORY_ID,UNIT_PRICE";
//      System.out.println("the query in findStockByQueryforreq is <<<<<<<<<<<<< "+ selectQuerybyGroup);       
 
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuerybyGroup);
            rs = ps.executeQuery();

            while (rs.next()) {

                String branchId = rs.getString("BRANCH_ID");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String asset_status=rs.getString("ASSET_STATUS");
                double unitPrice = rs.getDouble("UNIT_PRICE");

//                Asset aset = new Asset(branchId,category,
//                        description,asset_status);
                Asset aset = new Asset();
                aset.setBranchId(branchId);
                aset.setCategory(category);
                aset.setDescription(description);
                aset.setAsset_status(asset_status); 
//                System.out.println("======>>>>>>>>unitPrice: "+unitPrice);
                aset.setUnitPrice(unitPrice);
                list.add(aset);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Stock in findStockByQueryforreq ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public boolean notifyRaisedEntry(String id, String tableName)
    {
        Connection con;
        PreparedStatement ps;
        String createQuery;
        boolean done;
        con = null;
        ps = null;
        createQuery = (new StringBuilder("UPDATE ")).append(tableName).append(" SET RAISED_ENTRY=?").append(" WHERE HIST_ID=?").toString();
        done = false;
        try
        {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, "R");
            ps.setString(2, id);
            done = ps.executeUpdate() != -1;
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("INFO:Error RAISED ENTRY IN ")).append(tableName).append("  ->").append(e.getMessage()).toString());
        }
        finally {
        	closeConnection(con, ps);
        }        
        return done;
    }
    public FleetInsurranceRecord findInsurranceRecordByIdApproval(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                " ,COMPANY,DATE_OBTAINED,EXPIRY_DATE,STATUS,HIST_ID " +
                ",FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,RAISED_ENTRY,PROJECT_CODE " +
                " FROM FT_INSURANCE_HISTORY WHERE ASSET_ID = ? AND RAISED_ENTRY IS NULL";

        FleetInsurranceRecord insRecord = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String company = rs.getString("COMPANY");
                String dateObtained = formatDate(rs.getDate("DATE_OBTAINED"));
                String dateExpired = formatDate(rs.getDate("EXPIRY_DATE"));
                String status = rs.getString("STATUS");
                String tranId = rs.getString("HIST_ID");
                String noteDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String freq = rs.getString("NOTIFICATION_FREQ");
                String invoiceNo = rs.getString("INVOICE_NO");
                String ltId = rs.getString("LT_ID");
                String raised_Entry = rs.getString("RAISED_ENTRY");
                String projectCode = rs.getString("PROJECT_CODE");
                insRecord = new FleetInsurranceRecord(id, type, cost,
                        assetCode, registrationNo, company, dateObtained,
                        dateExpired, status, tranId, noteDate, freq, invoiceNo);
                insRecord.setHistId(tranId);
                insRecord.setLtId(ltId);
                insRecord.setRaised_Entry(raised_Entry);
                insRecord.setProjectCode(projectCode);
                
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching insurance record in findInsurranceRecordByIdApproval ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return insRecord;
    }
    public ArrayList findAssetVerificationByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
 
        String selectQuery =
                "SELECT a.ASSET_ID,b.EXIST_BRANCH_CODE,a.REGISTRATION_NO,BRANCH_ID,a.DEPT_ID," +
                "CATEGORY_ID, a.DESCRIPTION,a.DATE_PURCHASED,DEP_RATE," +
                "a.ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,a.DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,a.RAISE_ENTRY,DEP_YTD,SECTION," +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,a.EFFECTIVE_DATE,REVALUE_COST,ASSET_STATUS," +
                "a.ASSET_CODE,IMPROV_COST,IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET a, AM_GROUP_RECONCILIATION b  WHERE a.ASSET_ID = b.ASSET_ID AND b.BRANCH_CODE != b.EXIST_BRANCH_CODE AND b.STATUS = 'ACTIVE' " + queryFilter;

    //    String selectQuery = "select  *from am_asset where Asset_id in (select Asset_id from AM_GROUP_RECONCILIATION where STATUS = 'ACTIVE') and Asset_id !='' " +
 //       					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetVerificationByQuery: "+selectQuery);		
        Connection con = null; 
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String existBranchCode = rs.getString("EXIST_BRANCH_CODE");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setExistBranchCode(existBranchCode);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetVerificationByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public FleetManatainanceRecord findMaintenaceRecordByAssetId(String Id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE,NEXT_NOT_DATE,VAT_RATE,WHT_RATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,TECHNICIAN_CODE " +
                "FROM FT_MAINTENANCE_HISTORY WHERE HIST_ID = '"+Id+"' ";
//        System.out.println("selectQuery in findMaintenaceRecordByAssetId: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
//                String technicianName = rs.getString("TECHNICIAN_NAME");
                String technicianName = rs.getString("TECHNICIAN_CODE");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String nextNotificationDate = formatDate(rs.getDate(
                        "NEXT_NOT_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                String projectCode = rs.getString("PROJECT_CODE");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);
                maintenanceRecord.setNextNotificationDate(nextNotificationDate);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findMaintenaceRecordByAssetId ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }
    
    public ArrayList findAssetProofByBranchByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery =
                "SELECT a.ASSET_ID,b.EXIST_BRANCH_CODE,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, a.DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,a.RAISE_ENTRY,DEP_YTD,SECTION," +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST,ASSET_STATUS," +
                "a.ASSET_CODE,IMPROV_COST,IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET a, am_Asset_Proof_Selection b  WHERE a.ASSET_ID = b.ASSET_ID AND b.STATUS = 'ACTIVE' " + queryFilter;

    //    String selectQuery = "select  *from am_asset where Asset_id in (select Asset_id from AM_GROUP_RECONCILIATION where STATUS = 'ACTIVE') and Asset_id !='' " +
 //       					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetProofByBranchByQuery: "+selectQuery);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String existBranchCode = rs.getString("EXIST_BRANCH_CODE");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setExistBranchCode(existBranchCode);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetVerificationByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public boolean Write_to_excel_file_directly(ArrayList list, String bacthId,String branchId,String userName,String mailaddress) throws IOException {
        	WritableWorkbook wworkbook;
        	HSSFWorkbook workbook = new HSSFWorkbook();
            InputStream in = null;
            String[] args = null;
            FileOutputStream out = null;
        	boolean Result = false;
        	
        	 magma.AssetRecordsBean bd = null;

            try {
            	
            	if(list.size()!=0){
	                HSSFSheet sheet = workbook.createSheet("workbook");
	                HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell((short) 0).setCellValue("S/NO");
                    rowhead.createCell((short) 1).setCellValue("ASSET_ID");
                    rowhead.createCell((short) 2).setCellValue("DESCRITION");
                    rowhead.createCell((short) 3).setCellValue("BRANCH ID");
                    rowhead.createCell((short) 4).setCellValue("SUB CATEGORY CODE");
                    rowhead.createCell((short) 5).setCellValue("DEPARTMENT");
                    rowhead.createCell((short) 6).setCellValue("SECTION");
                    rowhead.createCell((short) 7).setCellValue("LOCATION");
                    rowhead.createCell((short) 8).setCellValue("STATE");
                    rowhead.createCell((short) 9).setCellValue("BAR CODE");
                    rowhead.createCell((short) 10).setCellValue("SBU CODE");
                    rowhead.createCell((short) 11).setCellValue("SERIAL NO.");
                    rowhead.createCell((short) 12).setCellValue("REGISTRATION NO");
                    rowhead.createCell((short) 13).setCellValue("ENGINE NO");
                    rowhead.createCell((short) 14).setCellValue("ASSET MODEL");
                    rowhead.createCell((short) 15).setCellValue("ASSET MAKE");
                    rowhead.createCell((short) 16).setCellValue("VENDOR NAME");
                    rowhead.createCell((short) 17).setCellValue("VENDOR ACCOUNT");
                    rowhead.createCell((short) 18).setCellValue("MAINTAINED BY");
                    rowhead.createCell((short) 19).setCellValue("LPO");
                    rowhead.createCell((short) 20).setCellValue("COMPONENT");
                    rowhead.createCell((short) 21).setCellValue("COMPONENT BARCODE");
                    rowhead.createCell((short) 22).setCellValue("SPARE FIELD 1");
                    rowhead.createCell((short) 23).setCellValue("SPARE FIELD 2");
                    rowhead.createCell((short) 24).setCellValue("SPARE FIELD 3");
                    rowhead.createCell((short) 25).setCellValue("SPARE FIELD 4");
                    rowhead.createCell((short) 26).setCellValue("ASSET USER");
                    rowhead.createCell((short) 27).setCellValue("COST PRICE");
                    rowhead.createCell((short) 28).setCellValue("MONTHLY DEPR.");
                    rowhead.createCell((short) 29).setCellValue("ACCUM DEPR.");
                    rowhead.createCell((short) 30).setCellValue("NBV");
                    rowhead.createCell((short) 31).setCellValue("IMPROV. COST PRICE");
                    rowhead.createCell((short) 32).setCellValue("IMPROV. MONTHLY DEPR.");
                    rowhead.createCell((short) 33).setCellValue("IMPROV. ACCUM DEPR.");
                    rowhead.createCell((short) 34).setCellValue("IMPROV. NBV");
                    rowhead.createCell((short) 35).setCellValue("PURCHASE DATE");
                    rowhead.createCell((short) 36).setCellValue("PURCHASE REASON");
                    rowhead.createCell((short) 37).setCellValue("DEPREC. START DATE");
                    rowhead.createCell((short) 38).setCellValue("DEPREC. END DATE");
                    rowhead.createCell((short) 39).setCellValue("COMMENTS");
                    rowhead.createCell((short) 40).setCellValue("ASSET AVAILABLE");
                    rowhead.createCell((short) 41).setCellValue("ASSET FUNCTIONING");
                    rowhead.createCell((short) 42).setCellValue("CATEGORY ID");
                    rowhead.createCell((short) 43).setCellValue("BATCH ID");
	                

	                int i = 1;
                  for (int j = 0; j < list.size(); j++) {
                      bd = (magma.AssetRecordsBean) list.get(j);
                      String registration = bd.getRegistration_no();
                      String Description = bd.getDescription();
                      String assetuser = bd.getUser();
                      String reason1 = bd.getReason();
                      String sbu = bd.getSbu_code();
                      String dept = bd.getDepartment_id();
                      String comments = bd.getComments();
                      String sighted = bd.getAssetsighted();
                      String function = bd.getAssetfunction();
                      int assetcode = bd.getAssetCode();
                      String categoryId = bd.getCategory_id();                
                      String barcode = bd.getBar_code();
                      String assetId = bd.getAsset_id();
                      String subcat = bd.getSub_category_id();
                      String batchId = bd.getProjectCode();
                      String sectionId = bd.getSection_id();
                      String location = bd.getLocation();
                      String state = bd.getState();
                      String subcatcode = bd.getSubcatCode();
                      String costprice = bd.getCost_price();
                      String monthlyDep = bd.getMonthlydep();
                      String accumDep = bd.getAccumdep();
                      String nbv = bd.getNbv();
                      String improvcost = bd.getImproveCost();
                      String improvmonthlydep = bd.getImproveMonthlydep();
       				  String improveaccumdep = bd.getImproveAccumdep();
	  				  String improvnbv = bd.getImproveNbv();
                      String lpo = bd.getLpo();
       				  String make = bd.getMake();
	  				  String model = bd.getModel();
	  				  String spare1 = bd.getSpare_1();
	  				  String spare2 = bd.getSpare_2();
		  			  String spare3 = bd.getSpare_3();
		  			  String spare4 = bd.getSpare_4();
		  			  String spare5 = bd.getSpare_5();
		  			  String spare6 = bd.getSpare_6();
	  				  String registrationNo = bd.getRegistration_no();
	  				  String sbucode = bd.getSbu_code();
	  				  String serialNo = bd.getSerial_number();
	  				  String engineNo = bd.getEngine_number();
	  				  String purchaseDate = bd.getDate_of_purchase();
	  				  String deprecStartDate = bd.getDepreciation_start_date();
	  				  String deprecEndDate = bd.getDepreciation_end_date();
	  				  String purchaseReason = bd.getReason();
	  				  String vendorAcct = bd.getVendor_account();
	  				  String suppliedBy = bd.getSupplied_by();
	  				  String maintainBy = bd.getMaintained_by();

                      HSSFRow row = sheet.createRow((short) i);
                      row.createCell((short) 1).setCellValue(assetId);
                      row.createCell((short) 2).setCellValue(Description);
                      row.createCell((short) 3).setCellValue(branchId);
                      row.createCell((short) 4).setCellValue(subcatcode);
                      row.createCell((short) 5).setCellValue(dept);
                      row.createCell((short) 6).setCellValue(sectionId);
                      row.createCell((short) 7).setCellValue(location);
                      row.createCell((short) 8).setCellValue(state);
                      row.createCell((short) 9).setCellValue(barcode);
                      row.createCell((short) 10).setCellValue(sbucode);
                      row.createCell((short) 11).setCellValue(serialNo);
                      row.createCell((short) 12).setCellValue(registrationNo);
                      row.createCell((short) 13).setCellValue(engineNo);
                      row.createCell((short) 14).setCellValue(model);
                      row.createCell((short) 15).setCellValue(make);
                      row.createCell((short) 16).setCellValue(suppliedBy);
                      row.createCell((short) 17).setCellValue(vendorAcct);
                      row.createCell((short) 18).setCellValue(maintainBy);
                      row.createCell((short) 19).setCellValue(lpo);
                      row.createCell((short) 20).setCellValue(spare1);
                      row.createCell((short) 21).setCellValue(spare2);
                      row.createCell((short) 22).setCellValue(spare3);
                      row.createCell((short) 23).setCellValue(spare4);
                      row.createCell((short) 24).setCellValue(spare5);
                      row.createCell((short) 25).setCellValue(spare6);
                      row.createCell((short) 26).setCellValue(assetuser);
                      row.createCell((short) 27).setCellValue(costprice);
                      row.createCell((short) 28).setCellValue(monthlyDep);
                      row.createCell((short) 29).setCellValue(accumDep);
                      row.createCell((short) 30).setCellValue(nbv);
                      row.createCell((short) 31).setCellValue(improvcost);
                      row.createCell((short) 32).setCellValue(improvmonthlydep);
                      row.createCell((short) 33).setCellValue(improveaccumdep);
                      row.createCell((short) 34).setCellValue(improvnbv);
                      row.createCell((short) 35).setCellValue(purchaseDate);
                      row.createCell((short) 36).setCellValue(purchaseReason);
                      row.createCell((short) 37).setCellValue(deprecStartDate);
                      row.createCell((short) 38).setCellValue(deprecEndDate);
                      row.createCell((short) 39).setCellValue(comments);
                      row.createCell((short) 40).setCellValue(sighted);
                      row.createCell((short) 41).setCellValue(function);
                      row.createCell((short) 42).setCellValue(categoryId);
                      row.createCell((short) 43).setCellValue(batchId);
                      i++;
      	     }
/*
                  System.out.println("Starting download");
                  long t1 = System.currentTimeMillis();
                  URL url = new URL(args[0]);
                  // Open the input and out files for the streams
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  in = conn.getInputStream();
                  out = new FileOutputStream("AssetDownLoadForProof.xls");
                  // Read data into buffer and then write to the output file
                  byte[] buffer = new byte[8192];
                  int bytesRead;
                  while ((bytesRead = in.read(buffer)) != -1) {
                      out.write(buffer, 0, bytesRead);
                  }
                  long t2 = System.currentTimeMillis();
                  System.out.println("Time for download & save file in millis:"+(t2-t1));
                */  
                
                  String fileName = branchId+"By"+userName+"AssetDownLoadForProof.xls";
                  String filePath = System.getProperty("user.home")+"\\Downloads";
//                  String filePath = System.getProperty("user.dir");
                  File file =    new File(filePath+"\\"+fileName);
//                  System.out.print("Location Path:>>> "+file);
//                  System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
                  String dir = System.getProperty("user.dir");
 //                 System.out.print("Directory Location Path:>>> "+dir);
                  Path currentWorkingDir = Paths.get("").toAbsolutePath();
//                  System.out.println(currentWorkingDir.normalize().toString());
                  File file2 =    new File(currentWorkingDir+"\\"+fileName);
//                  System.out.println("file2."+file2);
                  
                  FileOutputStream fileOut = new FileOutputStream(file);
                  workbook.write(fileOut);
                  fileOut.close();
//                  System.out.println("File  Successfully Generated.");
                  String subject = "Asset DownLoad For Proof";
                  String msgText = "See the attached file for  Asset Proof DownLoad"; 

//                  System.out.print("AssetProof mails: :>>> "+mailaddress+"   subject: "+subject+"   msgText: "+msgText);
                  mail.sendMailWithAttachmentPersonal(mailaddress,subject,msgText,fileName);
          /*         
                  System.out.println("Starting download");
                  //URL("http://localhost:8080/legendPlus.net/Downloads/AssetDownLoadForProof.xls");
                  URL url = new URL("http://localhost:8080/legendPlus.net/Downloads/AssetDownLoadForProof.xls");
                  
                  long t1 = System.currentTimeMillis();
                  //URL url = new URL(args[0]);
                  // Open the input and out files for the streams
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  in = conn.getInputStream();
                  out = new FileOutputStream("AssetDownLoadForProof.xls");
                  // Read data into buffer and then write to the output file
                  byte[] buffer = new byte[8192];
                  int bytesRead;
                  while ((bytesRead = in.read(buffer)) != -1) {
                      out.write(buffer, 0, bytesRead);
                  }
                  long t2 = System.currentTimeMillis();
                  System.out.println("Time for download & save file in millis:"+(t2-t1));
                  System.out.println("Download  Successfully.");
                  FTPClient ftpClient = new FTPClient();
                  String remoteFile1 = filePath+"\\"+fileName;
                  String home = System.getProperty("user.home");
                  File file1 = new File(home+"/Downloads/" + fileName); 
                  File downloadFile1 = new File("c:/Downloads/video.mp4");
                  OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                  boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
                  outputStream1.close();
                 
                 System.out.println("home Property: "+home+"   downloadFile1: "+downloadFile1);
                 */
            }
            } catch (Exception e) {
            System.out.println("Erorr while execting the program: "
                    + e.getMessage());
			} /* finally {
                // Close the resources correctly
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }*/

            Result = true;
			return Result;
        }
//}

    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetforBranchProofByQuery(String queryFilter,String userId) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery = "select *from am_asset where Asset_id not in (select Asset_id from AM_ASSET_PROOF WHERE GROUP_ID = ('"+userId+"'+SUBSTRING((SELECT CONVERT(VARCHAR(10), getdate(), 112)),0,7))) and Asset_id !='' " +
        					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetforBranchProofByQuery: "+selectQuery+"     userId: "+userId);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String lpo = rs.getString("LPO");
                String vendorAc = rs.getString("vendor_ac");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setLPO(lpo);
                aset.setVendorAc(vendorAc);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetforBranchProofByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllAsset2
     *
     * @return ArrayList
     */
    public ArrayList findAsset2ByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET2  WHERE ASSET_ID IS NOT NULL " + queryFilter;

        String selectQuery = "select *from am_asset2 where Asset_id not in (select Asset_id from am_gb_bulkAsset2Transfer) and Asset_id !='' " +
        					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset2 ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    /**
     * findAllAsset2
     *
     * @return ArrayList
     */
    public ArrayList findBulkAsset2UpdateByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET2  WHERE ASSET_ID IS NOT NULL " + queryFilter;

        String selectQuery = "select *from am_asset2 where Asset_id !='' " +
        					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String spare1=rs.getString("SPARE_1");
                String spare2=rs.getString("SPARE_2");
                String spare3=rs.getString("SPARE_3");
                String spare4=rs.getString("SPARE_4");
                String spare5=rs.getString("SPARE_5");
                String spare6=rs.getString("SPARE_6");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setSpare1(spare1);
                aset.setSpare2(spare2);
                aset.setSpare3(spare3);
                aset.setSpare4(spare4);
                aset.setSpare5(spare5);
                aset.setSpare6(spare6);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset2 ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findStockByQueryy2(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

       String selectstockQuery =
    		   "SELECT DISTINCT a.ASSET_ID,a.ASSET_CODE,a.REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
    		              "a.CATEGORY_ID,SECTION,CATEGORY_CODE,a.DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
    		              "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
    		              "a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,DEP_END_DATE," +
    		              "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
    		              "a.NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
    		              "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
    		              "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
    		              "Useful_Life,a.Location,Vatable_Cost,Vat,Req_Depreciation,  " +
    		              "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
    		              "State,Driver,Spare_1,Spare_2,a.User_ID,Date_Disposed,PROVINCE,  " +
    		              "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
    		              "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,ITEMTYPE,  " +
    		              "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
    		              "system_ip,mac_address,a.Quantity,b.QUANTITY AS REQQUANTITY,b.project_code,b.NEWASSET_USER,b.REQUESTBRANCH " +
    		              "FROM ST_STOCK a, am_ad_TransferRequisition b WHERE a.ASSET_ID IS NOT NULL AND a.DESCRIPTION = b.DESCRIPTION "+
    					   "AND b.PROCESS_STATUS = 'A' " + queryFilter;
//      System.out.println("the query in findStockByQueryy2 is <<<<<<<<<<<<< "+ selectstockQuery);
 
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectstockQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                int assetcode = rs.getInt("ASSET_CODE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String branchcode = rs.getString("BRANCH_CODE");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String categorycode = rs.getString("CATEGORY_CODE");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
              //  String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String effectiveDate2 = rs.getString("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String sectionID = rs.getString("Section_id");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Supplier_Name = rs.getInt("Supplier_Name");
                int Asset_Maintenance = rs.getInt("Asset_Maintenance");
                String Authorized_By = rs.getString("Authorized_By");
                String Wh_Tax = rs.getString("Wh_Tax");
                double Wh_Tax_Amount = rs.getDouble("Wh_Tax_Amount");
                String Req_Redistribution = rs.getString("Req_Redistribution");
                String Purchase_Reason = rs.getString("Purchase_Reason");
                int Useful_Life = rs.getInt("Useful_Life");
                int Location = rs.getInt("Location");
                double Vatable_Cost = rs.getDouble("Vatable_Cost");
                double Vat = rs.getDouble("Vat");
                String Req_Depreciation = rs.getString("Req_Depreciation");
                String Subject_TO_Vat = rs.getString("Subject_TO_Vat");
                String Who_TO_Rem = rs.getString("Who_TO_Rem");
                String email1 = rs.getString("Email1");
                String Who_To_Rem_2 = rs.getString("Who_To_Rem_2");
                String email2 = rs.getString("Email2");
                int State = rs.getInt("State");
                int Driver = rs.getInt("Driver");
                String Spare_1 = rs.getString("Spare_1");
                String Spare_2 = rs.getString("Spare_2");
                int User_ID = rs.getInt("User_ID");
                String Date_Disposed = rs.getString("Date_Disposed");
                int PROVINCE = rs.getInt("PROVINCE");
                String Multiple = rs.getString("Multiple");
                String WAR_START_DATE = rs.getString("WAR_START_DATE");
                int WAR_MONTH = rs.getInt("WAR_MONTH");
                String WAR_EXPIRY_DATE = rs.getString("WAR_EXPIRY_DATE");
                String Last_Dep_Date = rs.getString("Last_Dep_Date");
                String SECTION_CODE = rs.getString("SECTION_CODE");
                String DEPT_CODE = rs.getString("DEPT_CODE");
                double AMOUNT_PTD = rs.getDouble("AMOUNT_PTD");
                double AMOUNT_REM = rs.getDouble("AMOUNT_REM");
                String PART_PAY = rs.getString("PART_PAY");
                String FULLY_PAID = rs.getString("FULLY_PAID");
                String GROUP_ID = rs.getString("GROUP_ID");
                String BAR_CODE = rs.getString("BAR_CODE");
                String SBU_CODE = rs.getString("SBU_CODE");
                String LPO = rs.getString("LPO");
                String supervisor = rs.getString("supervisor");
                String defer_pay = rs.getString("defer_pay");
                String OLD_ASSET_ID = rs.getString("OLD_ASSET_ID");
                int WHT_PERCENT = rs.getInt("WHT_PERCENT");
                String Post_reject_reason = rs.getString("Post_reject_reason");
                String Finacle_Posted_Date = rs.getString("Finacle_Posted_Date");
                String system_ip = rs.getString("system_ip");
                String mac_address = rs.getString("mac_address");
                int quantity =  rs.getInt("QUANTITY");
                int req_quantity =  rs.getInt("REQQUANTITY");
                String projectCode = rs.getString("PROJECT_CODE");
                String assetUser = rs.getString("NEWASSET_USER");
                int reqBranch =  rs.getInt("REQUESTBRANCH");
                String oldAssetId = rs.getString("NEWASSET_USER");
                String itemType = rs.getString("ITEMTYPE");
      //          String projectCode = rs.getString("ITEMTYPE");
              //  DatetimeFormat dd = new DatetimeFormat();
                //dd.dateConvert(strDate)

                Stock aset = new Stock(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetmodel(Asset_Model);
                aset.setSectionId(sectionID);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setSupplierName(Supplier_Name);
                aset.setAssetMaintain(Asset_Maintenance);
                aset.setAuthorizeBy(Authorized_By);
                aset.setWh_tax(Wh_Tax);
                aset.setWh_Tax_Amount(Wh_Tax_Amount);
                aset.setReq_Depreciation(Req_Redistribution);
                aset.setPurchaseReason(Purchase_Reason);
                aset.setUseful_Life(Useful_Life);
                aset.setLocation(Location);
                aset.setVatable_Cost(Vatable_Cost);
                aset.setVat(Vat);
                aset.setReq_Redistribution(Req_Redistribution);
                aset.setSubject_TO_Vat(Subject_TO_Vat);
                aset.setWho_TO_Rem(Who_TO_Rem);
                aset.setEmail1(email1);
                aset.setWho_To_Rem_2(Who_To_Rem_2);
                aset.setEmail2(email2);
                aset.setState(State);
                aset.setDriver(Driver);
                aset.setSpare1(Spare_1);
                aset.setSpare2(Spare_2);
                aset.setUserid(User_ID);
                aset.setDate_Disposed(Date_Disposed);
                aset.setPROVINCE(PROVINCE);
                aset.setMultiple(Multiple);
                aset.setWAR_START_DATE(WAR_START_DATE);
                aset.setWAR_MONTH(WAR_MONTH);
                aset.setWAR_EXPIRY_DATE(WAR_EXPIRY_DATE);
                aset.setLast_Dep_Date(Last_Dep_Date);
                aset.setSECTION_CODE(SECTION_CODE);
                aset.setDepcode(DEPT_CODE);
                aset.setAMOUNT_PTD(AMOUNT_PTD);
                aset.setAMOUNT_REM(AMOUNT_REM);
                aset.setPART_PAY(PART_PAY);
                aset.setFULLY_PAID(FULLY_PAID);
                aset.setGROUP_IDd(String.valueOf(GROUP_ID));
                aset.setBarcode2(BAR_CODE);
                aset.setSbuCode(SBU_CODE);
                aset.setLPO(LPO);
                aset.setSupervisor(supervisor);
                aset.setDefer_pay(defer_pay);
                aset.setOLD_ASSET_ID(OLD_ASSET_ID);
                aset.setWHT_PERCENT(WHT_PERCENT);
                aset.setPost_reject_reason(Post_reject_reason);
                aset.setFinacle_Posted_Date(Finacle_Posted_Date);
                aset.setSystem_ip(system_ip);
                aset.setMac_address(mac_address);
                aset.setVendorAc(vendor_acct);
                aset.setBranchCode(branchcode);
                aset.setCategoryCode(categorycode);
                aset.setEffectivedate2(effectiveDate2);
                aset.setAssetCode(assetcode);
                aset.setQuantity(quantity);
                aset.setUseageYears(req_quantity);
                aset.setProjectCode(projectCode);
                aset.setAssetUser(assetUser);
                aset.setDriver(reqBranch);
                aset.setItemType(itemType);
  //              System.out.println("reqBranch: "+reqBranch+""+aset.getDriver());



                list.add(aset);

//                minCost =Math.min(minCost, cost);
//                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
//                setMinCost(minCost);
//                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Stock in findStockByQueryy2 ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public boolean Write_to_excel_file_BulkAssetProofSelection(ArrayList list, String bacthId,String branchId,String userName,String mailaddress) {
        	WritableWorkbook wworkbook;
        	HSSFWorkbook workbook = new HSSFWorkbook();

        	boolean Result = false;
        	
        	 magma.AssetRecordsBean bd = null;
            try {
            	if(list.size()!=0){
	                HSSFSheet sheet = workbook.createSheet("workbook");
	                HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell((short) 0).setCellValue("S/NO");
                    rowhead.createCell((short) 1).setCellValue("ASSET_ID");
                    rowhead.createCell((short) 2).setCellValue("DESCRITION");
                    rowhead.createCell((short) 3).setCellValue("BAR CODE");
                    rowhead.createCell((short) 4).setCellValue("SBU CODE");
                    rowhead.createCell((short) 5).setCellValue("SUB CATEGORY CODE");
                    rowhead.createCell((short) 6).setCellValue("DEPARTMENT CODE");
                    rowhead.createCell((short) 7).setCellValue("SECTION CODE");
                    rowhead.createCell((short) 8).setCellValue("LOCATION");
                    rowhead.createCell((short) 9).setCellValue("STATE CODE");
                    rowhead.createCell((short) 10).setCellValue("SERIAL NO.");
                    rowhead.createCell((short) 11).setCellValue("REGISTRATION NO");
                    rowhead.createCell((short) 12).setCellValue("ENGINE NO");
                    rowhead.createCell((short) 13).setCellValue("ASSET MODEL");
                    rowhead.createCell((short) 14).setCellValue("ASSET MAKE");
                    rowhead.createCell((short) 15).setCellValue("VENDOR NAME");
                    rowhead.createCell((short) 16).setCellValue("VENDOR ACCOUNT");
                    rowhead.createCell((short) 17).setCellValue("MAINTAINED BY");
                    rowhead.createCell((short) 18).setCellValue("COST PRICE");
                    rowhead.createCell((short) 19).setCellValue("PURCHASE DATE");
                    rowhead.createCell((short) 20).setCellValue("LPO");
                    rowhead.createCell((short) 21).setCellValue("COMPONENT");
                    rowhead.createCell((short) 22).setCellValue("COMPONENT BARCODE");
                    rowhead.createCell((short) 23).setCellValue("SPARE FIELD1");
                    rowhead.createCell((short) 24).setCellValue("SPARE FIELD2");
                    rowhead.createCell((short) 25).setCellValue("SPARE FIELD3");
                    rowhead.createCell((short) 26).setCellValue("SPARE FIELD4");
                    rowhead.createCell((short) 27).setCellValue("BRANCH ID");
                    rowhead.createCell((short) 28).setCellValue("INITIATED BY");
                    rowhead.createCell((short) 29).setCellValue("ASSET USER");
                    rowhead.createCell((short) 30).setCellValue("COMMENTS");
                    rowhead.createCell((short) 31).setCellValue("ASSET SIGHTED");
                    rowhead.createCell((short) 32).setCellValue("ASSET FUNCTIONING");
                    rowhead.createCell((short) 33).setCellValue("ASSET CODE");
                    rowhead.createCell((short) 34).setCellValue("CATEGORY ID");
                    rowhead.createCell((short) 35).setCellValue("BATCH ID");
                   

	                int i = 1;
                  for (int j = 0; j < list.size(); j++) {
                      bd = (magma.AssetRecordsBean) list.get(j);
                      String registration = bd.getRegistration_no();
                      String Description = bd.getDescription();
                      String assetuser = bd.getUser();
                      String reason1 = bd.getReason();
                      String sbu = bd.getSbu_code();
                      String dept = bd.getDepartment_id();
                      String subcatId = bd.getSub_category_id();
                      String subcatcode = bd.getSubcatCode();
                      String location = bd.getLocation();
                      String sectionId = bd.getSection_id();
                      String state = bd.getState();
                      String lpo = bd.getLpo();
                      String vendorAccount = bd.getVendor_account();
                      String vendorName = bd.getSupplied_by();
                      String maintainedby = bd.getMaintained_by();
                      String comments = bd.getComments();
                      String sighted = bd.getAssetsighted();
                      String function = bd.getAssetfunction();
                      int assetcode = bd.getAssetCode();
                      String categoryId = bd.getCategory_id();                
                      String barcode = bd.getBar_code();
                      String assetId = bd.getAsset_id();
                      String subcat = bd.getSub_category_id();
                      String batchId = bd.getProjectCode();
                      String costprice = bd.getCost_price();
                      String monthlyDep = bd.getMonthlydep();
                      String accumDep = bd.getAccumdep();
                      String nbv = bd.getNbv();
                      String improvcost = bd.getImproveCost();
                      String improvmonthlydep = bd.getImproveMonthlydep();
       				  String improveaccumdep = bd.getImproveAccumdep();
	  				  String improvnbv = bd.getImproveNbv();
	  				  String model = bd.getModel();
	  				  String make = bd.getMake();
	  				  String spare1 = bd.getSpare_1();
	  				  String spare2 = bd.getSpare_2();
	  				  String spare3 = bd.getSpare_3();
	  				  String spare4 = bd.getSpare_4();
	  				  String spare5 = bd.getSpare_5();
	  				  String spare6 = bd.getSpare_6();	  				  
	  				  String registrationNo = bd.getRegistration_no();
	  				  String sbucode = bd.getSbu_code();
	  				  String serialNo = bd.getSerial_number();
	  				  String engineNo = bd.getEngine_number();
	  				  String purchaseDate = bd.getDate_of_purchase();
	  				  String purchaseReason = bd.getReason();
	  				  String deprecStartDate = bd.getDepreciation_start_date();
	  				  String deprecEndDate = bd.getDepreciation_end_date();
	  				  String vendorAcct = bd.getVendor_account();
	  				  String suppliedBy = bd.getSupplied_by();
	  				  String maintainBy = bd.getMaintained_by();
	  				  String initiator = bd.getUser_id();
	  				  
                      HSSFRow row = sheet.createRow((short) i);
                      row.createCell((short) 0).setCellValue(j);
                      row.createCell((short) 1).setCellValue(assetId);
                      row.createCell((short) 2).setCellValue(Description);
                      row.createCell((short) 3).setCellValue(barcode);
                      row.createCell((short) 4).setCellValue(sbucode);
                      row.createCell((short) 5).setCellValue(subcat);
                      row.createCell((short) 6).setCellValue(dept);
                      row.createCell((short) 7).setCellValue(sectionId);
                      row.createCell((short) 8).setCellValue(location);
                      row.createCell((short) 9).setCellValue(state);
                      row.createCell((short) 10).setCellValue(serialNo);
                      row.createCell((short) 11).setCellValue(registrationNo);
                      row.createCell((short) 12).setCellValue(engineNo);
                      row.createCell((short) 13).setCellValue(model);
                      row.createCell((short) 14).setCellValue(make);
                      row.createCell((short) 15).setCellValue(suppliedBy);
                      row.createCell((short) 16).setCellValue(vendorAcct);
                      row.createCell((short) 17).setCellValue(maintainBy);
                      row.createCell((short) 18).setCellValue(costprice);
                      row.createCell((short) 19).setCellValue(purchaseDate);
                      row.createCell((short) 20).setCellValue(lpo);
                      row.createCell((short) 21).setCellValue(spare1);
                      row.createCell((short) 22).setCellValue(spare2);
                      row.createCell((short) 23).setCellValue(spare3);
                      row.createCell((short) 24).setCellValue(spare4);
                      row.createCell((short) 25).setCellValue(spare5);
                      row.createCell((short) 26).setCellValue(spare6);
                      row.createCell((short) 27).setCellValue(branchId);
                      row.createCell((short) 28).setCellValue(initiator);
                      row.createCell((short) 29).setCellValue(assetuser);
                      row.createCell((short) 30).setCellValue(comments);
                      row.createCell((short) 31).setCellValue(sighted);
                      row.createCell((short) 32).setCellValue(function);
                      row.createCell((short) 33).setCellValue(assetcode);
                      row.createCell((short) 34).setCellValue(categoryId);
                      row.createCell((short) 35).setCellValue(batchId);
                     
                      i++;
      	     }
                  String fileName = branchId+"By"+userName+"AssetDownLoadForverification.xls";
                  String filePath = System.getProperty("user.home")+"\\Downloads";
//                  String filePath = System.getProperty("user.dir");
                  File file =    new File(filePath+"\\"+fileName);
//                  System.out.print("Location Path:>>> "+file);
//                  System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
                  String dir = System.getProperty("user.dir");
//                  System.out.print("Directory Location Path:>>> "+dir);
                  Path currentWorkingDir = Paths.get("").toAbsolutePath();
//                  System.out.println(currentWorkingDir.normalize().toString());
                  
                  FileOutputStream fileOut = new FileOutputStream(file);
                  workbook.write(fileOut);
                  fileOut.close();
                  
                  String subject = "Asset DownLoad For Proof";
                  String msgText = "See the attached file for  Asset Proof DownLoad"; 

//                  System.out.print("AssetProof mails: :>>> "+mailaddress+"   subject: "+subject+"   msgText: "+msgText+"    fileName: "+fileName);
                  mail.sendMailWithAttachmentPersonal(mailaddress,subject,msgText,fileName);                  
            }
            } catch (Exception e) {
            System.out.println(e);
			}
            Result = true;
			return Result;
        }

    public java.util.ArrayList findMaintenaceDetailRecordById(String histId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT LT_ID,HIST_ID,TYPE,COST_PRICE,QUANTITY,DESCRIPTION " +
    				"FROM FT_MAINTENANCE_DETAILS WHERE HIST_ID = '"+histId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	
    				int ltId = rs.getInt("LT_ID");
    				String type = rs.getString("TYPE");
                    String description = rs.getString("DESCRIPTION");
                    String costprice = rs.getString("COST_PRICE");
                    int qty = rs.getInt("QUANTITY");
    				AssetRecordsBean trans = new AssetRecordsBean();
    				trans.setDescription(description);
    				trans.setCost_price(costprice);
    				trans.setQuantity(qty);
    				trans.setAssetCode(ltId);
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

    public ArrayList findAssetforFleetByQuery(String queryFilter,String userId) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery = "select *from am_asset where category_id in (select category_id from am_ad_category where Required_for_fleet = 'Y') " +
        					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetforFleetByQuery: "+selectQuery+"     userId: "+userId);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery(); 

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String lpo = rs.getString("LPO");
                String vendorAc = rs.getString("vendor_ac");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setLPO(lpo);
                aset.setVendorAc(vendorAc);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Fleet Asset ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findAssetforFleetSumInsuredByQuery(String queryFilter,String userId) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        //String selectQuery = "SELECT '' AS sum_insured,a.Asset_id,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID, a.CATEGORY_ID,a.DESCRIPTION,a.REGISTRATION_NO,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,a.LPO,a.vendor_ac,a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,a.REVALUE_COST,a.REMAINING_LIFE,a.TOTAL_LIFE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV FROM AM_ASSET a, am_ad_category c where a.category_id = c.category_id AND ASSET_STATUS = 'ACTIVE' "+queryFilter;
        String selectQuery = "SELECT f.sum_insured,a.Asset_id,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID, a.CATEGORY_ID,a.DESCRIPTION,a.REGISTRATION_NO,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,a.LPO,a.vendor_ac,a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,a.REVALUE_COST,a.REMAINING_LIFE,a.TOTAL_LIFE,f.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV FROM AM_ASSET a, am_ad_category c, FT_SUMINSURED f where a.category_id = c.category_id and  a.Asset_id = f.Asset_Id and Required_for_fleet = 'Y'  AND(ASSET_STATUS = 'ACTIVE' AND f.TRAN_TYPE = 'I' ) "+queryFilter;
   //     		+ " UNION SELECT DISTINCT 0.00 AS sum_insured,a.Asset_id,a.BRANCH_ID,a.DEPT_ID,a.SECTION,a.CATEGORY_ID,a.CATEGORY_ID,a.DESCRIPTION,a.REGISTRATION_NO,a.DATE_PURCHASED,a.DEP_RATE,a.ASSET_MAKE,a.LPO,a.vendor_ac,a.ASSET_USER,a.ASSET_MAINTENANCE,a.ACCUM_DEP,a.MONTHLY_DEP,a.DEP_END_DATE,a.RESIDUAL_VALUE,a.POSTING_DATE,a.RAISE_ENTRY,a.DEP_YTD,a.NBV,a.REVALUE_COST,a.REMAINING_LIFE,a.TOTAL_LIFE,a.EFFECTIVE_DATE,a.ASSET_STATUS,a.asset_code,a.IMPROV_COST,a.IMPROV_NBV,a.IMPROV_ACCUMDEP,a.IMPROV_MONTHLYDEP,a.TOTAL_NBV FROM AM_ASSET a, am_ad_category c, FLEET_SUMINSURED f where a.category_id = c.category_id and  a.Asset_id != f.Asset_Id and Required_for_fleet = 'Y'  AND(ASSET_STATUS = 'ACTIVE' AND f.TRAN_TYPE = 'I' )  AND BRANCH_ID = '43' " +
   //     		queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetforFleetSumInsuredByQuery: "+selectQuery+"     userId: "+userId);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
            	
                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String lpo = rs.getString("LPO");
                String vendorAc = rs.getString("vendor_ac");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("sum_insured");
//                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setLPO(lpo);
                aset.setVendorAc(vendorAc);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Fleet Asset for Insurance and VIO ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    public int createEnvironmenteRecord(String itemType,String branchCode, String deptCode,
                                        String sectCode, String sbuCode, String transDate,
                                        String detailNarration,double cost,String userid,
                                        String suppliedBy, String invoiceNo, String tranId,
                                        double vatAmt, double whtAmt,String projectCode,String subjectToVat,String subjectToWHT,String batchId) {
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        java.sql.Date transactionDate = null;  
        int isCreated = 0;
        if (transDate == null) {
            transactionDate = null; 
        } else {
        	transactionDate = dateConvert(transDate);
        }        
        String createQuery = "INSERT INTO FM_SOCIALENVIRONMENT_SUMMARY( " +
                             " HIST_ID,ITEM_TYPE,BRANCH_ID,DEPT_ID,SECT_ID,SBU_CODE,COST_PRICE," +
                             " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                             ",SUPPLIED_BY,TRANS_DATE,CREATE_DATE,PROJECT_CODE,SUBJECT_TO_VAT,SUBJECT_TO_WHT,BATCH_ID) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
/*
        if (isTransactionOverFlow(cost, assetMake, category)) {
            setOverFlow(true);
        } else {
*/
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(createQuery);
                ps.setString(1, tranId);
                ps.setString(2, itemType);
                ps.setString(3, branchCode);
                ps.setString(4, deptCode);
                ps.setString(5, sectCode);
                ps.setString(6, sbuCode);
                ps.setDouble(7, cost);
                ps.setString(8, detailNarration);
                ps.setDouble(9, vatAmt);
                ps.setDouble(10, whtAmt);
                ps.setString(11, invoiceNo);
                ps.setString(12, userid);
                ps.setString(13, suppliedBy);
                ps.setDate(14, transactionDate);
                ps.setTimestamp(15, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(16, projectCode);
                ps.setString(17, subjectToVat);
                ps.setString(18, subjectToWHT);
                ps.setString(19, batchId);
                ps.execute();
                isCreated = ps.getUpdateCount();
            } catch (Exception e) {
                System.out.println("INFO:Error creating FM_SOCIALENVIRONMENT_SUMMARY in createEnvironmenteRecord ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
     //   }
			return isCreated;
    }

    public FleetManatainanceRecord findFMRecordById(String tranId,
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,HIST_ID,TYPE,BRANCH_ID,COST_PRICE," +
                 " DETAILS,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID, " +
                 " COMPONENT_REPLACED,NOTIFICATION_FREQ,LAST_PM_DATE,FIRST_NOT_DATE, " +
                 " NEXT_PM_DATE,MILLEAGE_AFTER_MAINT,MILLEAGE_BEFORE_MAINT, " +
                 " subject_TO_Vat,subject_TO_WHT, TECHNICIAN_CODE," +
                 " TECHNICIAN_TYPE,TECHNICIAN_NAME,CREATE_DATE,STATUS,PROJECT_CODE,REGISTRATION_NO,REPAIRED_DATE,VAT_RATE,WHT_RATE " +
                "FROM FM_MAINTENANCE_HISTORY WHERE HIST_ID = ?";
//        System.out.println("selectQuery in findFMRecordById: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId); 
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("CREATE_DATE"));
                String details = rs.getString("DETAILS");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                String suppliedBy = rs.getString("TECHNICIAN_CODE");
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                double milleageAfterMaint = rs.getDouble("MILLEAGE_AFTER_MAINT");
                double milleageBeforeMaint = rs.getDouble("MILLEAGE_BEFORE_MAINT");
                String notificationFreq = rs.getString("NOTIFICATION_FREQ");
                String lastPMDate = rs.getString("LAST_PM_DATE");
                String firstNotDate = rs.getString("FIRST_NOT_DATE");
                String nextPMDate = rs.getString("NEXT_PM_DATE");
                String subjectTOVat = rs.getString("subject_TO_Vat");
                String subjectToWhTax = rs.getString("subject_TO_WHT");
                String dateOfRepair = rs.getString("REPAIRED_DATE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,suppliedBy,projectCode,vatRate,whtRate);
                maintenanceRecord.setTechnicianType(technicianType);
                maintenanceRecord.setComponentReplaced(componentReplaced);
                maintenanceRecord.setMilleageBeforeMaintenance(milleageBeforeMaint);
                maintenanceRecord.setMilleageAfterMaintenance(milleageAfterMaint);
                maintenanceRecord.setNotificationFreq(notificationFreq);
                maintenanceRecord.setLastPerformedDate(lastPMDate);  
                maintenanceRecord.setFirstNotificationDate(firstNotDate);
                maintenanceRecord.setNextPerformedDate(nextPMDate);
                maintenanceRecord.setSubjectTOVat(subjectTOVat);
                maintenanceRecord.setSubjectToWhTax(subjectToWhTax);
                maintenanceRecord.setDateOfRepair(dateOfRepair);
                maintenanceRecord.setRegistrationNo(registrationNo);
                maintenanceRecord.setVatRate(vatRate);
                maintenanceRecord.setWhtRate(whtRate);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Facility Maintenace Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public ArrayList findFMRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,HIST_ID,TYPE,BRANCH_ID,COST_PRICE," +
                " DETAILS,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                ",TECHNICIAN_NAME,CREATE_DATE,CREATE_DATE,STATUS,PROJECT_CODE,VAT_RATE,WHT_RATE " +
                "FROM FM_MAINTENANCE_HISTORY WHERE LT_ID != ''  " + queryFilter;
//        System.out.println("<<<<selectQuery in findFMRecordByQuery====: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE"); 
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("CREATE_DATE"));
                String details = rs.getString("DETAILS");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                String projectCode = rs.getString("PROJECT_CODE");
                String suppliedBy = rs.getString("TECHNICIAN_NAME");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,suppliedBy,projectCode,vatRate,whtRate);
                finder.add(maintenanceRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findFMRecordByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    /**
     * findAllAssetforVerifications
     *
     * @return ArrayList
     */
    public ArrayList findAssetforVerificationsByQuery(String queryFilter,String userId) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
 
        String selectQuery = "select *from am_asset where Asset_id not in (select Asset_id from AM_ASSET_PROOF WHERE GROUP_ID = ('"+userId+"'+SUBSTRING((SELECT CONVERT(VARCHAR(10), getdate(), 112)),0,5))) and Asset_id !='' " +
        					queryFilter;
//        System.out.println("<<<<<<<selectQuery in findAssetforBranchProofByQuery: "+selectQuery+"     userId: "+userId);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String lpo = rs.getString("LPO");
                String vendorAc = rs.getString("vendor_ac");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setLPO(lpo);
                aset.setVendorAc(vendorAc);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetforVerificationsByQuery->" +
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
    public ArrayList findAllPPMAllocation() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery = "SELECT BRANCH_ID,BRANCH_NAME,VENDOR_CODE,VENDOR_NAME,DESCRIPTION" +
                             "Q1_DATE,Q2_DATE,Q3_DATE,Q4_DATE" +
                             "FROM FM_PPM_TMP  ";
 //       System.out.println("<<<<<<< selectQuery in findAllPPMAllocation: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = "";
                String branchCode = rs.getString("BRANCH_ID");
                String branchName = rs.getString("BRANCH_NAME");
                String vendorCode = rs.getString("VENDOR_CODE");
                String vendorName = rs.getString("VENDOR_NAME");
                String description = rs.getString("DESCRIPTION");
                String firstQauterDate = rs.getString("Q1_DATE");
                String secondQuaterDate = rs.getString("Q2_DATE");
                String thirdQauterDate = rs.getString("Q3_DATE");
                String fourthQauterDate = rs.getString("Q4_DATE");
                String type = "";

                FMppmAllocation allocation = new FMppmAllocation(id,
                        branchCode,vendorCode,"",
                        branchName,vendorName, description,firstQauterDate, secondQuaterDate,
                        thirdQauterDate, fourthQauterDate,type);
                list.add(allocation);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching all PPM Schedule findAllPPMAllocation ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public String createBulkFacilityRequisitionRecord(String compCode, String assetId, String reqnBranch,
                                        String branchId, String reqnSection, String reqnDepartment,
                                        String reqnUserID, String remark,
                                        String userID, String reqMeans, String categoryOfWork,
                                        String maintenanceNature, String priority,
                                        int NoOfWorkDes, String facLocation, String supervisor, String txnLevel,String groupId,String transId,String reqnsId,String singleApproval,String mtid) {
    	
        String reqnID = "FMREQN/" + applHelper.getGeneratedId("FM_Requisition");

//        String mtid = applHelper.getGeneratedId("am_asset_approval");
//        System.out.println("====>reqnID: "+reqnID+"    mtid: "+mtid+"   transId: "+transId+"    userID: "+userID);
        String supervisorID = supervisor;
        remark = remark.toUpperCase();
        facLocation = facLocation.toUpperCase();
        String status = "P";
        String reqnStatus = "PENDING";
        String adm_Approv_Lvl_Qry = "select level from Approval_Level_Setup where transaction_type='Facility Mgt Requisition'";

        String branchCode_Qry = "select branch_Code from am_ad_branch where branch_id=" + reqnBranch;
//        System.out.println("====>branchCode_Qry: "+branchCode_Qry);
        String branchCode = records.getCodeName(branchCode_Qry);

//        System.out.println("the value of >>>>>>>>>> is aprecords.getCodeName(adm_Approv_Lvl_Qry) "+ records.getCodeName(adm_Approv_Lvl_Qry) );

        int var = Integer.parseInt(records.getCodeName(adm_Approv_Lvl_Qry));
        int approvalLevelLimit = Integer.parseInt(txnLevel);

        legend.admin.objects.User user = sechanle.getUserByUserID(userID);
        String userName = user.getUserName();
        String departCode = user.getDeptCode();
        String branch = user.getBranch();
//        System.out.println("userID >>>>>>>>> " +userID+"    userName: "+userName+"    departCode: "+departCode+"      branch: "+branch); 
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

         String ReqnInsertQry = "insert into FM_Requisition (ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
                " ReqnUserID,Status,ApprovalLevel,ApprovalLevelLimit,Supervisor,Image," +
                "Remark,workStationIP,Asset_id,req_Means,category_Work,Maintenance_Nature,Priority,No_Work_Description,"+
                " Location,Request_Time,reqnDate,GROUP_ID,TRANSID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        boolean done = false;
        boolean result = false; 
        try {
        	ad = new AssetRecordsBean();
        	con = getConnection("legendPlus");
            pstmt = con.prepareStatement(ReqnInsertQry);
            pstmt.setString(1, reqnID);
            pstmt.setString(2, userID);
            pstmt.setString(3, reqnBranch);
            pstmt.setString(4, reqnSection);
            pstmt.setString(5, reqnDepartment);
            pstmt.setString(6, reqnUserID);
            //pstmt.setString(7, itemType);
            //pstmt.setString(8, itemRequested);
            pstmt.setString(7, status);
            pstmt.setInt(8, 0);
            pstmt.setInt(9, approvalLevelLimit);
            pstmt.setString(10, supervisorID);
           // pstmt.setString(13, compCode);
            pstmt.setString(11, "N");
            pstmt.setString(12, remark);
            pstmt.setString(13, "");
           // pstmt.setString(17, itemQty);
            pstmt.setString(14, assetId);
            pstmt.setString(15, reqMeans);
            pstmt.setString(16, categoryOfWork);
            pstmt.setString(17, maintenanceNature);
            pstmt.setString(18, priority);
            pstmt.setInt(19, NoOfWorkDes);
            pstmt.setString(20, facLocation);
            pstmt.setString(21, timer.format(new java.util.Date()));
            pstmt.setTimestamp(22, dbConnection.getDateTime(new java.util.Date()));
            pstmt.setString(23, reqnID);
            pstmt.setString(24, transId);
            done = (pstmt.executeUpdate() == -1);
            System.out.println("done >>>>>>>>> " + done);

            if (!done && singleApproval.equalsIgnoreCase("Y"))//successful
            {
                

                String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                        "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                pstmt.setString(1, reqnID);
                pstmt.setString(2, userID);
                pstmt.setString(3, supervisorID);
                pstmt.setTimestamp(4, dbConnection.getDateTime(new java.util.Date()));
                pstmt.setString(5, remark);
                pstmt.setTimestamp(6,dbConnection.getDateTime(new java.util.Date()));
                pstmt.setString(7, branchCode);
                pstmt.setString(8, reqnStatus); //asset_status
                pstmt.setString(9, "Facility Mgt Requisition");
                pstmt.setString(10, status);
                pstmt.setString(11, timer.format(new java.util.Date()));
                pstmt.setString(12, mtid);
                pstmt.setString(13, mtid);
                pstmt.setInt(14, var);
                result = (pstmt.executeUpdate() == -1);
            }

            if (!done && singleApproval.equalsIgnoreCase("N"))//successful
            {
//            	System.out.println("singleApproval #$$$$$$$$$$$ "+singleApproval);
            	  java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
//                  System.out.println("approvelist.size >>>>>>>>> " + approvelist.size()); 
       	   	 for(int j=0;j<approvelist.size();j++)
    	     {  
//    		 	System.out.println("J #$$$$$$$$$$$ "+j);
    		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
    			String supervisorId =  usr.getUserId();
    			String mailAddress = usr.getEmail();
    			String supervisorName = usr.getUserName();
    			String supervisorfullName = usr.getUserFullName();                
//    			System.out.println("supervisorId #$$$$$$$$$$$ "+supervisorId);
    			
                String ins_am_asset_approval_qry = "insert into am_asset_approval(asset_id,user_id,super_id,posting_date,description," +
                        "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time,transaction_id,batch_id," +
                        "transaction_level) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pstmt = con.prepareStatement(ins_am_asset_approval_qry);
                pstmt.setString(1, reqnID);
                pstmt.setString(2, userID);
                pstmt.setString(3, supervisorId);
                pstmt.setTimestamp(4, dbConnection.getDateTime(new java.util.Date()));
                pstmt.setString(5, remark);
                pstmt.setTimestamp(6,dbConnection.getDateTime(new java.util.Date()));
                pstmt.setString(7, branchCode);
                pstmt.setString(8, reqnStatus); //asset_status
                pstmt.setString(9, "Facility Mgt Requisition");
                pstmt.setString(10, status);
                pstmt.setString(11, timer.format(new java.util.Date()));
                pstmt.setString(12, mtid);
                pstmt.setString(13, mtid);
                pstmt.setInt(14, var);
                result = (pstmt.executeUpdate() == -1);
            	String subjectr ="Request for Bulk Requisition for Approval";
            	String msgText11 ="Request for Bulk Requisition Approval with GROUP ID: "+ mtid +" is waiting for your approval.";	
                if(result==true) {cdb.insertMailRecords(mailAddress,subjectr,msgText11);}
            }
            }           
            } catch (Exception e) {
                System.out.println("INFO:Error creating FM_Requisition ->" +
                                   e.getMessage());
            } finally {
            	closeConnection(con, pstmt, rs);
            }
        return reqnID;
    }
 
    public ArrayList findvendorAssessmentCriteriaByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT *FROM VENDOR_ASSESSMENT_CRITERIA WHERE ID IS NOT NULL  " + queryFilter;
 //       System.out.println("<<<<selectQuery====: "+selectQuery);
        try {  
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ID");
                String criteria = rs.getString("CRITERIA"); 
                String category = rs.getString("STATUS");
                VendorAssessment criteriaDetail = new VendorAssessment(id, criteria,category);
                
                finder.add(criteriaDetail);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Assessment Criterial Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

public void createAssessmentHeaderRecord(String groupId,String branchCode, String departCode,String vendorCode, String serviceType,String transDate,String batchId) {
java.sql.Date noticeDate = null;  
int intFreq = 0;

Connection con = null;
PreparedStatement ps = null;
FleetTransaction fleetTran = null;
String createQuery = "INSERT INTO VENDOR_ASSESSMENT(GROUP_ID," +
 " BRANCH_CODE,DEPT_CODE,VENDOR_CODE,SERVICE_TYPE,ASSESSMENT_DATE,CREATE_DATE,BATCH_ID) " +
 "VALUES(?,?,?,?,?,?,?,?) ";
//System.out.print("<<<<<<<<======transDate: "+transDate);
String dd = transDate.substring(0,2);
String mm = transDate.substring(3,5);
String yyyy = transDate.substring(6,10);
//System.out.print("<<<<<<<<======>>>>yyyy: "+yyyy+"    mm: "+mm+"    dd: "+dd);
transDate = yyyy+"-"+mm+"-"+dd;

try {
con = getConnection("legendPlus");
ps = con.prepareStatement(createQuery);
ps.setString(1, groupId);
ps.setString(2, branchCode);
ps.setString(3, departCode);
ps.setString(4, vendorCode);
ps.setString(5, serviceType);
ps.setString(6, transDate);
ps.setTimestamp(7,dbConnection.getDateTime(new java.util.Date()));
ps.setString(8, batchId);

ps.execute();


} catch (Exception e) {
System.out.println("INFO:Error creating VENDOR_ASSESSMENT in createAssessmentHeaderRecord ->" +
       e.getMessage());
} finally {
closeConnection(con, ps);
}

}


    public void createVisitHeaderRecord(String groupId,String branchCode, String inspectedBy,
            String dateInspect, String visitSummary, String transDate) {
java.sql.Date noticeDate = null;  
int intFreq = 0;
//System.out.println("<<<transDate: "+transDate);
String yyyy = transDate.substring(0,2);
String mm = transDate.substring(3,5);
String dd = transDate.substring(6,10);
//System.out.println("<<<mm: "+mm+"   DD: "+dd+"   YY: "+yyyy);
transDate = dd+"-"+mm+"-"+yyyy;
Connection con = null;
PreparedStatement ps = null;
FleetTransaction fleetTran = null;
String createQuery = "INSERT INTO FM_BRANCH_VISIT(GROUP_ID," +
 " BRANCH_CODE,INSPECTED_BY,INSPECT_DATE,VISIT_SUMMARY,TRANS_DATE) " +
 "VALUES(?,?,?,?,?,?) ";
//System.out.println("<<<groupId: "+groupId+"  <<<inspectedBy: "+inspectedBy+"   <<dateInspect: "+dateInspect+"  <<visitSummary: "+visitSummary+"  <<transDate: "+transDate);
try {
con = getConnection("legendPlus");
ps = con.prepareStatement(createQuery);
ps.setString(1, groupId);
ps.setString(2, branchCode);
ps.setString(3, inspectedBy);
ps.setString(4, dateInspect);
ps.setString(5, visitSummary);
ps.setString(6, transDate);
ps.execute();


} catch (Exception e) {
System.out.println("INFO:Error creating FM_BRANCH_VISIT in createVisitHeaderRecord ->" +
       e.getMessage());
} finally {
closeConnection(con, ps);
}

}

    public java.util.ArrayList findBranchVisitDetailRecordById(String groupId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT ID,GROUP_ID,SNO,ELEMENT,CONDITION,REMARK,ACTION,DUEDATE " +
    				"FROM FM_BRANCH_VISIT_DETAILS WHERE GROUP_ID = '"+groupId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	
    				String id = rs.getString("ID");
    				String sNo = rs.getString("SNO");
                    String element = rs.getString("ELEMENT");
                    String condition = rs.getString("CONDITION");
                    String remark = rs.getString("REMARK");
                    String actionby = rs.getString("ACTION");
                    String dueDate = rs.getString("DUEDATE");
                    BranchVisit trans = new BranchVisit();
                    trans.setSNo(sNo);
                    trans.setElement(element);
                    trans.setCondition(condition);
    				trans.setRemark(remark);
    				trans.setActionby(actionby);
    				trans.setDueDate(dueDate);
    				trans.setId(id);
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

    public BranchVisit findBranchVisitRecordByGroupId(String Id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BranchVisit visitRecord = null;
        String selectQuery =
                "SELECT ID,BRANCH_CODE,INSPECTED_BY,INSPECT_DATE,VISIT_SUMMARY,TRANS_DATE " +
                "FROM FM_BRANCH_VISIT WHERE GROUP_ID = '"+Id+"' ";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ID");
                String branchCode = rs.getString("BRANCH_CODE");
                String inspectedBy = rs.getString("INSPECTED_BY");
                String inspectionDate = rs.getString("INSPECT_DATE");
                String visitSummary = rs.getString("VISIT_SUMMARY");
                String transDate = rs.getString("TRANS_DATE");
                visitRecord = new BranchVisit(id,  branchCode, inspectionDate, inspectedBy, transDate, visitSummary);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Baranch Visit Records in findBranchVisitRecordByGroupId ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return visitRecord;
    }

    public ArrayList findStockIssuanceByQueryy(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE   " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
                "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
                "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
                "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation,  " +
                "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
                "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE,  " +
                "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
                "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE,  " +
                "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date,  " +
                "system_ip,mac_address " +
                "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter;
//        System.out.println("the query in findStockByQueryy is <<<<<<<<<<<<< "+ selectQuery);


       String query =
    	"SELECT ASSET_ID,ASSET_CODE,REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,DEPT_ID," +
        "CATEGORY_ID,SECTION,CATEGORY_CODE,DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
        "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
        "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
        "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION ," +
        "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status ," +
        "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance ," +
        "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason ," +
        "Useful_Life,Location,Vatable_Cost,Vat,Req_Depreciation," +
        "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2," +
        "State,Driver,Spare_1,Spare_2,User_ID,Date_Disposed,PROVINCE," +
        "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE," +
        "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,SBU_CODE," +
        "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date," +
        "system_ip,mac_address " +
        "FROM ST_STOCK  where ASSET_ID NOT IN (select Asset_Id from am_asset_bid) "+queryFilter;

 
       String selectstockQuery =
    		   "SELECT DISTINCT a.ASSET_ID,a.ASSET_CODE,a.REGISTRATION_NO,BRANCH_CODE,BRANCH_ID,a.DEPT_ID," +
    		              "a.CATEGORY_ID,SECTION,CATEGORY_CODE,a.DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
    		              "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
    		              "a.ACCUM_DEP,a.MONTHLY_DEP,a.COST_PRICE,DEP_END_DATE," +
    		              "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
    		              "a.NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status , " +
    		              "Asset_Model,Section_id,Vendor_AC,Asset_Serial_No,Asset_Engine_No,Supplier_Name,Asset_Maintenance , " +
    		              "Authorized_By,Wh_Tax,Wh_Tax_Amount,Req_Redistribution,Purchase_Reason , " +
    		              "Useful_Life,a.Location,Vatable_Cost,Vat,Req_Depreciation,  " +
    		              "Subject_TO_Vat,Who_TO_Rem,Email1,Who_To_Rem_2,Email2,  " +
    		              "State,Driver,Spare_1,Spare_2,a.User_ID,Date_Disposed,PROVINCE,  " +
    		              "Multiple,WAR_START_DATE,WAR_MONTH,WAR_EXPIRY_DATE,Last_Dep_Date,SECTION_CODE,DEPT_CODE,  " +
    		              "AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,GROUP_ID,BAR_CODE,a.SBU_CODE,  " +
    		              "LPO,supervisor,defer_pay,OLD_ASSET_ID,WHT_PERCENT,Post_reject_reason,Finacle_Posted_Date, ITEMTYPE, " +
    		              "system_ip,mac_address,a.Quantity,b.QUANTITY AS REQQUANTITY,b.project_code,b.NEWASSET_USER,b.REQUESTBRANCH " +
    		              "FROM ST_STOCK a, am_ad_TransferRequisition b WHERE a.ASSET_ID IS NOT NULL "+
    					   "AND b.PROCESS_STATUS = 'A' " + queryFilter;
//      System.out.println("the query in findStockIssuanceByQueryy is <<<<<<<<<<<<< "+ selectstockQuery);
 
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectstockQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                int assetcode = rs.getInt("ASSET_CODE");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String branchcode = rs.getString("BRANCH_CODE");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String categorycode = rs.getString("CATEGORY_CODE");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
              //  String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String effectiveDate2 = rs.getString("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                String Asset_Model = rs.getString("Asset_Model");
                String sectionID = rs.getString("Section_id");
                String vendor_acct = rs.getString("Vendor_AC");
                String Asset_Serial_No = rs.getString("Asset_Serial_No");
                String Asset_Engine_No = rs.getString("Asset_Engine_No");
                int Supplier_Name = rs.getInt("Supplier_Name");
                int Asset_Maintenance = rs.getInt("Asset_Maintenance");
                String Authorized_By = rs.getString("Authorized_By");
                String Wh_Tax = rs.getString("Wh_Tax");
                double Wh_Tax_Amount = rs.getDouble("Wh_Tax_Amount");
                String Req_Redistribution = rs.getString("Req_Redistribution");
                String Purchase_Reason = rs.getString("Purchase_Reason");
                int Useful_Life = rs.getInt("Useful_Life");
                int Location = rs.getInt("Location");
                double Vatable_Cost = rs.getDouble("Vatable_Cost");
                double Vat = rs.getDouble("Vat");
                String Req_Depreciation = rs.getString("Req_Depreciation");
                String Subject_TO_Vat = rs.getString("Subject_TO_Vat");
                String Who_TO_Rem = rs.getString("Who_TO_Rem");
                String email1 = rs.getString("Email1");
                String Who_To_Rem_2 = rs.getString("Who_To_Rem_2");
                String email2 = rs.getString("Email2");
                int State = rs.getInt("State");
                int Driver = rs.getInt("Driver");
                String Spare_1 = rs.getString("Spare_1");
                String Spare_2 = rs.getString("Spare_2");
                int User_ID = rs.getInt("User_ID");
                String Date_Disposed = rs.getString("Date_Disposed");
                int PROVINCE = rs.getInt("PROVINCE");
                String Multiple = rs.getString("Multiple");
                String WAR_START_DATE = rs.getString("WAR_START_DATE");
                int WAR_MONTH = rs.getInt("WAR_MONTH");
                String WAR_EXPIRY_DATE = rs.getString("WAR_EXPIRY_DATE");
                String Last_Dep_Date = rs.getString("Last_Dep_Date");
                String SECTION_CODE = rs.getString("SECTION_CODE");
                String DEPT_CODE = rs.getString("DEPT_CODE");
                double AMOUNT_PTD = rs.getDouble("AMOUNT_PTD");
                double AMOUNT_REM = rs.getDouble("AMOUNT_REM");
                String PART_PAY = rs.getString("PART_PAY");
                String FULLY_PAID = rs.getString("FULLY_PAID");
                String GROUP_ID = rs.getString("GROUP_ID");
                String BAR_CODE = rs.getString("BAR_CODE");
                String SBU_CODE = rs.getString("SBU_CODE");
                String LPO = rs.getString("LPO");
                String supervisor = rs.getString("supervisor");
                String defer_pay = rs.getString("defer_pay");
                String OLD_ASSET_ID = rs.getString("OLD_ASSET_ID");
                int WHT_PERCENT = rs.getInt("WHT_PERCENT");
                String Post_reject_reason = rs.getString("Post_reject_reason");
                String Finacle_Posted_Date = rs.getString("Finacle_Posted_Date");
                String system_ip = rs.getString("system_ip");
                String mac_address = rs.getString("mac_address");
                int quantity =  rs.getInt("QUANTITY");
                int req_quantity =  rs.getInt("REQQUANTITY");
                String projectCode = rs.getString("PROJECT_CODE");
                String assetUser = rs.getString("NEWASSET_USER");
                int reqBranch =  rs.getInt("REQUESTBRANCH");
                String itemType = rs.getString("ITEMTYPE");
              //  DatetimeFormat dd = new DatetimeFormat();
                //dd.dateConvert(strDate)

                Stock aset = new Stock(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetmodel(Asset_Model);
                aset.setSectionId(sectionID);
                aset.setSerialNo(Asset_Serial_No);
                aset.setEngineNo(Asset_Engine_No);
                aset.setSupplierName(Supplier_Name);
                aset.setAssetMaintain(Asset_Maintenance);
                aset.setAuthorizeBy(Authorized_By);
                aset.setWh_tax(Wh_Tax);
                aset.setWh_Tax_Amount(Wh_Tax_Amount);
                aset.setReq_Depreciation(Req_Redistribution);
                aset.setPurchaseReason(Purchase_Reason);
                aset.setUseful_Life(Useful_Life);
                aset.setLocation(Location);
                aset.setVatable_Cost(Vatable_Cost);
                aset.setVat(Vat);
                aset.setReq_Redistribution(Req_Redistribution);
                aset.setSubject_TO_Vat(Subject_TO_Vat);
                aset.setWho_TO_Rem(Who_TO_Rem);
                aset.setEmail1(email1);
                aset.setWho_To_Rem_2(Who_To_Rem_2);
                aset.setEmail2(email2);
                aset.setState(State);
                aset.setDriver(Driver);
                aset.setSpare1(Spare_1);
                aset.setSpare2(Spare_2);
                aset.setUserid(User_ID);
                aset.setDate_Disposed(Date_Disposed);
                aset.setPROVINCE(PROVINCE);
                aset.setMultiple(Multiple);
                aset.setWAR_START_DATE(WAR_START_DATE);
                aset.setWAR_MONTH(WAR_MONTH);
                aset.setWAR_EXPIRY_DATE(WAR_EXPIRY_DATE);
                aset.setLast_Dep_Date(Last_Dep_Date);
                aset.setSECTION_CODE(SECTION_CODE);
                aset.setDepcode(DEPT_CODE);
                aset.setAMOUNT_PTD(AMOUNT_PTD);
                aset.setAMOUNT_REM(AMOUNT_REM);
                aset.setPART_PAY(PART_PAY);
                aset.setFULLY_PAID(FULLY_PAID);
                aset.setGROUP_IDd(String.valueOf(GROUP_ID));
                aset.setBarcode2(BAR_CODE);
                aset.setSbuCode(SBU_CODE);
                aset.setLPO(LPO);
                aset.setSupervisor(supervisor);
                aset.setDefer_pay(defer_pay);
                aset.setOLD_ASSET_ID(OLD_ASSET_ID);
                aset.setWHT_PERCENT(WHT_PERCENT);
                aset.setPost_reject_reason(Post_reject_reason);
                aset.setFinacle_Posted_Date(Finacle_Posted_Date);
                aset.setSystem_ip(system_ip);
                aset.setMac_address(mac_address);
                aset.setVendorAc(vendor_acct);
                aset.setBranchCode(branchcode);
                aset.setCategoryCode(categorycode);
                aset.setEffectivedate2(effectiveDate2);
                aset.setAssetCode(assetcode);
                aset.setQuantity(quantity);
                aset.setUseageYears(req_quantity);
                aset.setProjectCode(projectCode);
                aset.setAssetUser(assetUser);
                aset.setDriver(reqBranch);
                aset.setItemType(itemType);
  //              System.out.println("reqBranch: "+reqBranch+""+aset.getDriver());



                list.add(aset);

//                minCost =Math.min(minCost, cost);
//                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
//                setMinCost(minCost);
//                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Stock in findStockIssuanceByQueryy ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }        

    public ArrayList findStockByQueryforAdjustment(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

       String selectQuery =
               "SELECT ITEM_CODE,WAREHOUSE_CODE, DESCRIPTION, ASSET_STATUS,BRANCH_ID,CATEGORY_ID,ASSET_ID,QUANTITY " +
               "FROM ST_STOCK  WHERE ASSET_ID IS NOT NULL " + queryFilter +
               "  GROUP BY ITEM_CODE,WAREHOUSE_CODE,DESCRIPTION, ASSET_STATUS,BRANCH_ID,CATEGORY_ID,ASSET_ID,QUANTITY";
//      System.out.println("the selectQuery in findStockByQueryforAdjustment is <<<<<<<<<<<<< "+ selectQuery);       
 
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String itemCode = rs.getString("ITEM_CODE");
                String warehouseCode = rs.getString("WAREHOUSE_CODE");
                String branchId = rs.getString("BRANCH_ID");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String asset_status=rs.getString("ASSET_STATUS");
                String assetId=rs.getString("ASSET_ID");
                int quantity=rs.getInt("QUANTITY");
 //               System.out.println("=====assetId: "+assetId);
//                Asset aset = new Asset(branchId,category,
//                        description,asset_status);
                Asset aset = new Asset();
                aset.setBranchId(branchId);
                aset.setCategory(category);
                aset.setDescription(description);
                aset.setAsset_status(asset_status);
                aset.setAssetId(assetId);
                aset.setItemCode(itemCode);
                aset.setWarehouseCode(warehouseCode);
 //               System.out.println("=====getAssetId: "+aset.getAssetId());
                aset.setQuantity(quantity);
                list.add(aset);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Stock in findStockByQueryforAdjustment ->>>> " +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }    

    public ArrayList findSMRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT ID,HIST_ID,ITEM_TYPE,BRANCH_ID,DEPT_ID,SECT_ID,SBU_CODE,COST_PRICE," +
                " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                ",SUPPLIED_BY,TRANS_DATE,CREATE_DATE,STATUS,PROJECT_CODE " +
                "FROM FM_SOCIALENVIRONMENT_SUMMARY WHERE ID != ''  " + queryFilter;
//        System.out.println("<<<<selectQuery====: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("ID");
                String type = rs.getString("ITEM_TYPE"); 
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("TRANS_DATE"));
                String details = rs.getString("DESCRIPTION");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                String suppliedBy = rs.getString("SUPPLIED_BY");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,suppliedBy,projectCode);
                finder.add(maintenanceRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findSMRecordByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    public FleetManatainanceRecord findSMRecordById(String tranId,String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT ID,HIST_ID,ITEM_TYPE,BRANCH_ID,DEPT_ID,SECT_ID,SBU_CODE,COST_PRICE," +
                 " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                 ",SUPPLIED_BY,TRANS_DATE,CREATE_DATE,STATUS,PROJECT_CODE " +
                "FROM FM_SOCIALENVIRONMENT_SUMMARY WHERE BATCH_ID = ?";

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("ID");
                String type = rs.getString("ITEM_TYPE");
                String branchId = rs.getString("BRANCH_ID");
                String deptId = rs.getString("DEPT_ID");
                String sectId = rs.getString("SECT_ID");
                String sbuCode = rs.getString("SBU_CODE");
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("TRANS_DATE"));
                String details = rs.getString("DESCRIPTION");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                String suppliedBy = rs.getString("SUPPLIED_BY");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,suppliedBy,projectCode);
                maintenanceRecord.setBranchId(branchId);
                maintenanceRecord.setDeptId(deptId);
                maintenanceRecord.setSectId(sectId);
                maintenanceRecord.setSbuCode(sbuCode);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Facility ESMS Records in findSMRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public void removeESMSRecordById(String id) {
        String query = "DELETE FROM FM_SOCIALENVIRONMENT_SUMMARY WHERE LT_ID = " + id;
        excuteSQLCode(query);
    }

    public java.util.ArrayList findESMSDetailRecordById(String histId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT ID,HIST_ID,ITEM_TYPE,AMOUNT,QUANTITY,DESCRIPTION " +
    				"FROM FM_SOCIALENVIRONMENT_DETAILS WHERE HIST_ID = '"+histId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	
    				int id = rs.getInt("ID");
    				String ltId = rs.getString("HIST_ID");
    				String type = rs.getString("ITEM_TYPE");
                    String description = rs.getString("DESCRIPTION");
                    String costprice = rs.getString("AMOUNT");
                    int qty = rs.getInt("QUANTITY");
    				AssetRecordsBean trans = new AssetRecordsBean();
    				trans.setDescription(description);
    				trans.setCost_price(costprice);
    				trans.setQuantity(qty);
    				trans.setAssetCode(id);
    				trans.setItemCode(ltId);
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


    public void createCorrectMaintenanceRecord(String assetId, String registrationNo, String branchId, String deptId, String category, String assetMake, String datePurchased, 
            String createDate, String effectiveDate, String type, String dateOfRepair, String technicianType, double fuelCost, 
            double milleageBeforeMaintenace, double milleageAfterMaintenace, String details, double cost, 
            String componentReplaced, String lastPerformedDate, String nextPerformedDate, String techName, String maintenanceType, String userid, String firstNoticeDate, 
            String freq, String invoiceNo, String histId, double vatAmt, double whtAmt, 
            String projectCode, int assetCode,String subjectTOVat,String subjectTOWHT,String batchId)
    {
        java.sql.Date noticeDate;
        int intFreq;
        Connection con;
        PreparedStatement ps;
        String createQuery;
        if(lastPerformedDate == null || lastPerformedDate.equals(""))
        {
            lastPerformedDate = sdf.format(new Date());
        }
        if(nextPerformedDate == null || nextPerformedDate.equals(""))
        {
            nextPerformedDate = sdf.format(new Date());
        }
        noticeDate = null;
        intFreq = 0;
        if(firstNoticeDate == null)
        {
            noticeDate = null;
        } else
        {
            noticeDate = dateConvert(firstNoticeDate);
        }
        intFreq = Integer.parseInt(freq);
        con = null;
        ps = null;
        createQuery = "INSERT INTO FM_MAINTENANCE_HISTORY(  TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO  ," +
"REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME ,MILLEAGE_BEFORE_MAINT,MILLEAGE_AF" +
"TER_MAINT ,DETAILS,COMPONENT_REPLACED,LAST_PM_DATE ,NEXT_PM_DATE,MAINTENANCE_TYP" +
"E,USER_ID,FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,HIST_ID,VAT_AMT,WHT_AMT,PR" +
"OJECT_CODE,CREATE_DATE,ASSET_CODE,BRANCH_ID,subject_TO_Vat,subject_TO_WHT,TECHNICIAN_CODE,BATCH_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
"?,?,?,?,?,?,?,?,?,?,?,?) "
;
        if(isTransactionOverFlow(cost, assetMake, category))
        {
            setOverFlow(true);
        }
        try
        { 
            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);
            ps.setString(1, type);
            ps.setDouble(2, cost);
            ps.setString(3, assetId);
            ps.setString(4, registrationNo);
            ps.setDate(5, dateConvert(dateOfRepair));
            ps.setString(6, technicianType);
            ps.setString(7, techName);
            ps.setDouble(8, milleageBeforeMaintenace);
            ps.setDouble(9, milleageAfterMaintenace);
            ps.setString(10, details);
            ps.setString(11, componentReplaced);
            ps.setDate(12, dateConvert(lastPerformedDate));
            ps.setDate(13, dateConvert(nextPerformedDate));
            ps.setString(14, maintenanceType);
            ps.setString(15, userid);
            ps.setDate(16, noticeDate);
            ps.setInt(17, intFreq);
            ps.setString(18, invoiceNo);
            ps.setString(19, histId);
            ps.setDouble(20, vatAmt);
            ps.setDouble(21, whtAmt);
            ps.setString(22, projectCode);
            ps.setTimestamp(23, dbConnection.getDateTime(new Date()));
            ps.setInt(24, assetCode);
            ps.setString(25, branchId);
            ps.setString(26, subjectTOVat);
            ps.setString(27, subjectTOWHT);
            ps.setString(28, techName);
            ps.setString(29, batchId);
            ps.execute();
            int isCreated = ps.getUpdateCount();
            String assetMaintenance = "";
            String location = "";
            boolean entryRaised = false;
            double depreciation = 0.0D;
            double premiumLiveToDate = cost;
            double premimumPeriodToDate = cost;
            double maintLiveToDate = cost;
            double maintPeriodToDate = cost;
            double fuelLiveToDate = fuelCost;
            double fuelPeriodToDate = fuelCost;
            int accidentCount = 0;
            double accidentCostLiveToDate = 0.0D;
            double accidentCostPeriodToDate = 0.0D;
            double licencePermitLiveToDate = 0.0D;
            double licencePermitPeriodToDate = 0.0D;
            String lastUpdateDate = sdf.format(new Date());
            String status = "A";
            FleetTransaction fleetTran = new FleetTransaction(assetId, registrationNo, branchId, deptId, category, datePurchased, assetMake, userid, assetMaintenance, createDate, effectiveDate, location, entryRaised, depreciation, premiumLiveToDate, premimumPeriodToDate, maintLiveToDate, maintPeriodToDate, fuelLiveToDate, fuelPeriodToDate, accidentCount, accidentCostLiveToDate, accidentCostPeriodToDate, licencePermitLiveToDate, licencePermitPeriodToDate, lastUpdateDate, status, userid);
//            System.out.println((new StringBuilder("<<<<createDate: ")).append(createDate).append("   effectiveDate: ").append(effectiveDate).append("  datePurchased: ").append(datePurchased).append("  premimumPeriodToDate: ").append(premimumPeriodToDate).toString());
            tranManager.logFacilityTransaction(fleetTran);
        } catch (Exception e) {
            System.out.println("INFO:Error creating FM_MAINTENANCE_HISTORY in createCorrectMaintenanceRecord ->" +
                               e.getMessage());
        } finally {
        	closeConnection(con, ps);
        }
            
        }



    public java.util.ArrayList findvendorAssessmentDetailsByQuery(String filter)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT ID,GROUP_ID,CRITERIA,CRITERIONA,CRITERIONB,CRITERIONC,CRITERIOND,CRITERIONE,STATUS " +
    				"FROM VENDOR_ASSESSMENT_DETAILS WHERE ID IS NOT NULL "+filter+" ";
//    		System.out.println("<<<<<query in findvendorAssessmentDetailsByQuery: "+query);
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	
    				String id = rs.getString("ID");
    				String criteria = rs.getString("CRITERIA");
                    String criteriaA = rs.getString("CRITERIONA");
                    String criteriaB = rs.getString("CRITERIONB");
                    String criteriaC = rs.getString("CRITERIONC");
                    String criteriaD = rs.getString("CRITERIOND");
                    String criteriaE = rs.getString("CRITERIONE");
                    String status = rs.getString("STATUS");
                    VendorAssessment trans = new VendorAssessment();
                    trans.setId(id);
                    trans.setCriteria(criteria);
                    trans.setCriteriaA(criteriaA);
                    trans.setCriteriaB(criteriaB);
                    trans.setCriteriaC(criteriaC);
                    trans.setCriteriaD(criteriaD);
                    trans.setCriteriaE(criteriaE);
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

    public ArrayList findFMMaintenaceDetailRecordById(String histId)
    {
        ArrayList _list;
        String query;
        Connection c;
        ResultSet rs;
        Statement s;
        _list = new ArrayList();
        String finacleTransId = null;
        query = " SELECT ID,HIST_ID,ITEM_TYPE,COST_PRICE,QUANTITY,DESCRIPTION FROM FM_MAINTENANCE" +
        		"_DETAILS WHERE HIST_ID = '"+histId+"' ";
        c = null;
        rs = null;
        s = null;
//        System.out.println("<<<<<query in findFMMaintenaceDetailRecordById: "+query);
        try
        {
            c = getConnection("legendPlus");
            s = c.createStatement();
            rs = s.executeQuery(query);
            AssetRecordsBean trans;
            while (rs.next())
//            for(rs = s.executeQuery(query); rs.next(); _list.add(trans))
            {
                int ltId = rs.getInt("ID");
                String type = rs.getString("ITEM_TYPE");
                String description = rs.getString("DESCRIPTION");
                String costprice = rs.getString("COST_PRICE");
                int qty = rs.getInt("QUANTITY");
                trans = new AssetRecordsBean();
                trans.setDescription(description);
                trans.setCost_price(costprice);
                trans.setQuantity(qty);
                trans.setAssetCode(ltId);
                _list.add(trans);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        closeConnection(c, s, rs);
        return _list;
    }

    /**
     * findExpensesRecordById
     *
     * @param id String
     * @return FleetExpensesRecord
     */
    public FleetManatainanceRecord findExpensesRecordById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;  
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,HIST_ID,ITEM_TYPE,BRANCH_ID,SBU_CODE,COST_PRICE," +
                " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID, " +
                "TRANSACTION_DATE,CREATE_DATE,STATUS,PROJECT_CODE " +
                "FROM PR_EXPENSES_HISTORY WHERE HIST_ID = ?";
//        System.out.println("========>selectQuery: "+selectQuery+"      ====Id: "+id);
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("LT_ID");
                String branchId = rs.getString("BRANCH_ID"); 
                String sbuCode = rs.getString("SBU_CODE"); 
                String type = rs.getString("ITEM_TYPE"); 
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("TRANSACTION_DATE"));
//                System.out.println("========>transDate: "+transDate);
                String details = rs.getString("DESCRIPTION");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
//                System.out.println("========>projectCode: "+projectCode);
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,"",projectCode);
                maintenanceRecord.setBranchId(branchId);
                maintenanceRecord.setSbuCode(sbuCode);
                
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findExpensesRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public ArrayList findExpensesRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,HIST_ID,ITEM_TYPE,BRANCH_ID,SBU_CODE,COST_PRICE," +
                " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                ",TRANSACTION_DATE,CREATE_DATE,STATUS,PROJECT_CODE " +
                "FROM PR_EXPENSES_HISTORY WHERE LT_ID != ''  " + queryFilter;
//        System.out.println("<<<<selectQuery====: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ltId = rs.getString("LT_ID");
                String type = rs.getString("ITEM_TYPE"); 
                double cost = rs.getDouble("COST_PRICE");
                String transDate = formatDate(rs.getDate("TRANSACTION_DATE"));
                String details = rs.getString("DESCRIPTION");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,transDate,  details,status, histId, invoiceNo, vatAmt, whtAmt,"",projectCode);
                finder.add(maintenanceRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findExpensesRecordByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }


    public int createExpensesRecord(String itemType,String branchCode,String sbuCode, String transDate,
                                        String detailNarration,double cost,String userid,
                                        String invoiceNo, String tranId,
                                        double vatAmt, double whtAmt,String projectCode) {
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        java.sql.Date transactionDate = null;  
        int isCreated = 0;
        if (transDate == null) {
            transactionDate = null;
        } else {
        	transactionDate = dateConvert(transDate);
        }        
        String createQuery = "INSERT INTO PR_EXPENSES_HISTORY( " +
                             " HIST_ID,ITEM_TYPE,BRANCH_ID,SBU_CODE,COST_PRICE," +
                             " DESCRIPTION,VAT_AMT,WHT_AMT,INVOICE_NO,USER_ID " +
                             ",TRANSACTION_DATE,CREATE_DATE,PROJECT_CODE) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
/*
        if (isTransactionOverFlow(cost, assetMake, category)) {
            setOverFlow(true);
        } else {
*/
            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(createQuery);
                ps.setString(1, tranId);
                ps.setString(2, itemType);
                ps.setString(3, branchCode);
                ps.setString(4, sbuCode);
                ps.setDouble(5, cost);
                ps.setString(6, detailNarration);
                ps.setDouble(7, vatAmt);
                ps.setDouble(8, whtAmt);
                ps.setString(9, invoiceNo);  
                ps.setString(10, userid);
//                System.out.println("=====>transactionDate: "+transactionDate);
                ps.setDate(11, transactionDate);
                ps.setTimestamp(12, dbConnection.getDateTime(new java.util.Date()));
                ps.setString(13, projectCode);
                ps.execute();
                isCreated = ps.getUpdateCount();
            } catch (Exception e) {
                System.out.println("INFO:Error creating PR_EXPENSES_HISTORY in createExpensesRecord ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
     //   }
			return isCreated;
    }

    public java.util.ArrayList findExpensesDetailRecordById(String histId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT ID,HIST_ID,ITEM_TYPE,COST_PRICE,QUANTITY,DESCRIPTION " +
    				"FROM PR_EXPENSES_DETAILS WHERE HIST_ID = '"+histId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
 //   	System.out.println("======query in findExpensesDetailRecordById: "+query);
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	
    				int id = rs.getInt("ID");
    				String ltId = rs.getString("HIST_ID");
    				String type = rs.getString("ITEM_TYPE");
                    String description = rs.getString("DESCRIPTION");
                    String costprice = rs.getString("COST_PRICE");
                    int qty = rs.getInt("QUANTITY");
    				AssetRecordsBean trans = new AssetRecordsBean();
    				trans.setDescription(description);
    				trans.setCost_price(costprice);
    				trans.setQuantity(qty);
    				trans.setAssetCode(id);
    				trans.setItemCode(ltId);
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

    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetByQuery(String queryFilter,String branch,String deptCode,String categoryId,
    		String registrationNumber,String assetId,String dFromDate,String dToDate,String payType,String approveStatus,String status,String order,String blank) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
        String selectQuery_old = 
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ?";
  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/*
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+
//        queryFilter;
       String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+queryFilter;
       selectQuery = selectQuery+" "+order;
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {  
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery.toString());
          if(queryFilter.contains("PART_PAY") && queryFilter.contains("ASSET_STATUS") && queryFilter.contains("ASSET_STATUS")){  
//        	  System.out.println("<========getAssetByQuery=======>0 asset_Id: "+assetId);
        	  ps.setString(1, payType);
        	  ps.setString(2, approveStatus);
        	  ps.setString(3, approveStatus);
          }     
          if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("BRANCH_ID") && !queryFilter.contains("asset_user") && !queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>1 branch_Id: "+branch+"    status: "+status);
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
          }       
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID")){
//        	  System.out.println("<========getAssetByQuery=======>2");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_STATUS")){
 //       	  System.out.println("<========getAssetByQuery=======>3");
        	  ps.setString(1, status);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("REGISTRATION_NO") && queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>4");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
        	  ps.setString(5, registrationNumber);
        	  ps.setString(6, assetId);
          }  
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>5");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
//        	  ps.setString(5, dFromDate);
//        	  ps.setString(6, dToDate);
          }  
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("CATEGORY_ID")){
//        	  System.out.println("<========getAssetByQuery=======>6");
        	  ps.setString(1, branch);
        	  ps.setString(2, branch);
        	  ps.setString(3, deptCode);
//        	  ps.setString(4, dFromDate);
//        	  ps.setString(5, dToDate);
          }          
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>7");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
//        	  ps.setString(3, dFromDate);
//        	  ps.setString(4, dToDate);
          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery=======>8");
        	  ps.setString(1, status);
        	  ps.setString(2, branch);
        	  ps.setString(3, assetId);
        	  ps.setString(4, assetId);
          } 
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("ASSET_ID")){
//        	  System.out.println("<========getAssetByQuery=======>9");
        	  ps.setString(1, branch);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, assetId);
          } 
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    public ArrayList findAssetByQuery(String queryFilter,String branch,String deptCode,String categoryId,
    		String registrationNumber,String assetId,String user_name,String from_price,String to_price,String dFromDate,
    		String dToDate,String status,String userId, String barCode) {
//       System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        String selectQuery_old = 
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL ?";
  //      "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
/*
         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST, " +
                "IMPROV_NBV,IMPROV_ACCUMDEP,IMPROV_COST,IMPROV_MONTHLYDEP,TOTAL_NBV " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
  */      
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+
//        queryFilter;
       String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !='' "+queryFilter;
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery.toString());
            if(!queryFilter.contains("asset_user") && !queryFilter.contains("Cost_Price") && !queryFilter.contains("DATE_PURCHASED") 
            		&& !queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){  
        	  System.out.println("<========getAssetByQuery=======>Asset Status: ");
        	  ps.setString(1, status);
          }        
           if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("BRANCH_ID") && !queryFilter.contains("Cost_Price")
        		   && !queryFilter.contains("DATE_PURCHASED")){  
          	 System.out.println("<========getAssetByQuery Branch and Asset Status =======> ");
              ps.setString(1, branch);
          	  ps.setString(2, status);
            }   
           if(queryFilter.contains("USER_ID") && !queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){  
        	   System.out.println("<========getAssetByQuery User Id and Asset Status=======>");
         	  ps.setString(1, userId);
         	  ps.setString(2, status);
           }  
           if(queryFilter.contains("ASSET_STATUS") && queryFilter.contains("asset_user")  && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && !queryFilter.contains("BRANCH_ID")&& !queryFilter.contains("Cost_Price") && !queryFilter.contains("DATE_PURCHASED") ){  
         	  System.out.println("<========getAssetByQuery Asset User and Asset Status =======> ");
         	 ps.setString(1, user_name);
         	  ps.setString(2, status);
           }   
//          if(queryFilter.contains("USER_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){  
////        	  System.out.println("<========getAssetByQuery=======>0 status: "+status);
//        	  ps.setString(1, userId);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, status);
//          }     
          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery Branch and Department and Asset Status=======>");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, branch);
        	  ps.setString(3, status);
          }       
//          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("asset_user") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery Branch and Department and Asset User and Asset Status=======>");
//        	  ps.setString(1, deptCode);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, user_name);
//        	  ps.setString(4, status);
//          }   
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("DATE_PURCHASED")){
       	  System.out.println("<========getAssetByQuery Branch, Category and Department and Asset Status=======>3");
        	  ps.setString(1, status);
        	  ps.setString(2, categoryId);
        	  ps.setString(3, branch);
        	  ps.setString(4, deptCode);
          }   
          if(queryFilter.contains("CATEGORY_ID")  && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("DEPT_ID") && queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("Cost_Price")){
        	  System.out.println("<========getAssetByQuery Category, Branch, Date Purchased, Asset Status=======>4");
        	  ps.setString(1, categoryId);
        	  ps.setString(2, branch);
        	  ps.setString(3, dFromDate);
        	  ps.setString(4, dToDate);
        	  ps.setString(5, status);
          }  
          if(queryFilter.contains("CATEGORY_ID") && queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS") && queryFilter.contains("DATE_PURCHASED")){
       	  System.out.println("<========getAssetByQuery Category, Department, Branch, Date Purchased,  Asset Status=======>4B");
        	  ps.setString(1, categoryId);
        	  ps.setString(2, deptCode);
        	  ps.setString(3, branch);
        	  ps.setString(4, dFromDate);
        	  ps.setString(5, dToDate);
        	  ps.setString(6, status);
          }  
          
          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery=======>5");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, assetId);
        	  ps.setString(3, branch);
        	  ps.setString(4, status); 
          }  
          if(queryFilter.contains("DEPT_ID") && queryFilter.contains("BRANCH_ID") && !queryFilter.contains("Cost_Price") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("CATEGORY_ID")){
        	  System.out.println("<========getAssetByQuery Department, Branch, Date Purchased and Asset Status=======>6");
        	  ps.setString(1, deptCode);
        	  ps.setString(2, branch);
        	  ps.setString(3, dFromDate);
        	  ps.setString(4, dToDate);
        	  ps.setString(5, status);
          }            
          
          if(!queryFilter.contains("DEPT_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("Cost_Price") && queryFilter.contains("BRANCH_ID") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery Branch, Date-Purchased, Asset-Status=======>7");
        	  ps.setString(1, branch);
        	  ps.setString(2, dFromDate);
        	  ps.setString(3, dToDate);
        	  ps.setString(4, status);
          }   
//          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS")){
//        	  System.out.println("<========getAssetByQuery Branch, Asset-Id, Asset Status=======>8");
//        	  ps.setString(1, assetId);
//        	  ps.setString(2, branch);
//        	  ps.setString(3, status);
//          } 
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("CATEGORY_ID") && queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery Branch, Category, Asset-Id and Asset Status=======>9");
        	  ps.setString(1, categoryId);
        	  ps.setString(2, assetId);
        	  ps.setString(3, branch);
        	  ps.setString(4, status);
          } 
          if(queryFilter.contains("BRANCH_ID") && queryFilter.contains("Cost_Price") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery For Branch, Cost-Price and Asset Status=======>10");
        	  ps.setString(1, branch);
        	  ps.setString(2, from_price);
        	  ps.setString(3, to_price);
        	  ps.setString(4, status);
          } 
          if(queryFilter.contains("DATE_PURCHASED") && !queryFilter.contains("Cost_Price") && queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("BRANCH_ID")){
        	  System.out.println("<========getAssetByQuery Date-Purchased, Asset-Status=======>11");
        	  ps.setString(1, dFromDate);
        	  ps.setString(2, dToDate);
        	  ps.setString(3, status);
          }   
          if(queryFilter.contains("ASSET_ID") && queryFilter.contains("ASSET_STATUS") && !queryFilter.contains("Cost_Price") && !queryFilter.contains("BRANCH_ID") && !queryFilter.contains("CATEGORY_ID") && !queryFilter.contains("DEPT_ID") && !queryFilter.contains("DATE_PURCHASED") ){
        	  System.out.println("<========getAssetByQuery Asset-Id, Asset Status=======>12");
        	  ps.setString(1, assetId);
        	  ps.setString(2, status);
          } 
          if(!queryFilter.contains("BRANCH_ID") && queryFilter.contains("Cost_Price") && queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery For Cost-Price and Date and Asset Status=======>14");
        	  ps.setString(1, from_price);
        	  ps.setString(2, to_price);
        	  ps.setString(3, dFromDate);
        	  ps.setString(4, dToDate);
        	  ps.setString(5, status);
          } 
          if(!queryFilter.contains("BRANCH_ID") && queryFilter.contains("Cost_Price") && !queryFilter.contains("DATE_PURCHASED") && queryFilter.contains("ASSET_STATUS")){
        	  System.out.println("<========getAssetByQuery For Cost-Price and Asset Status=======>15");
        	  ps.setString(1, from_price);
        	  ps.setString(2, to_price);
        	  ps.setString(3, status);
          } 
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetByQuery Third ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findAssetforVerificationsViewByQuery(String groupId) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
 
        String selectQuery = "select * from AM_ASSET_PROOF WHERE GROUP_ID = '"+groupId+"' ";
//        System.out.println("<<<<<<<selectQuery in findAssetforVerificationsViewByQuery: "+selectQuery+"     userId: "+userId);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String lpo = rs.getString("LPO");
                String vendorAc = rs.getString("vendor_ac");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setLPO(lpo);
                aset.setVendorAc(vendorAc);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetforVerificationsViewByQuery->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list; 

    }


    public FleetManatainanceRecord findMaterialRecordById(String tranId,
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,VAT_RATE,WHT_RATE " +
                "FROM FT_MATERIALRETRIEVAL_HISTORY WHERE HIST_ID = ?";
//        System.out.println("selectQuery in findMaintenaceRecordById2: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, tranId);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_AMT");
                double whtRate = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Material Records in findMaterialRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }


    /**
     * createMaterialRecord
     *
     * @param type String
     * @param dateOfRepair String
     * @param technicianType String
     * @param milleageBeforeMaintenace double
     * @param milleageAfterMaintenace double
     * @param details String
     * @param cost double
     * @param componentReplaced String
     * @param lastPerformedDate String
     * @param nextPerformedDate String
     * @param maintenanceType String
     */
    //modified by olabo to add invoiceNo and hist_id columns
    public void createMaterialRecord(String assetId, String registrationNo,
                                        String branchId, String deptId,
                                        String category, String assetMake,
                                        String datePurchased,
                                        String createDate, String effectiveDate,
                                        String type, String dateOfRepair,
                                        String technicianType, double fuelCost,
                                        double milleageBeforeMaintenace,
                                        double milleageAfterMaintenace,
                                        String details, double cost,
                                        String componentReplaced,
                                        String lastPerformedDate,
                                        String nextPerformedDate,
                                        String techName,
                                        String maintenanceType, String userid,
                                        String firstNoticeDate, String freq,
                                        String invoiceNo, String histId,
                                        double vatAmt, double whtAmt,String projectCode,int assetCode,String selectWht,String selectVat) {
    	String dd = effectiveDate.substring(0,2);   
    	String mm = effectiveDate.substring(3,5);  
    	String yyyy = effectiveDate.substring(6,10);
    	effectiveDate = yyyy+"-"+mm+"-"+dd;
    	if(datePurchased==""){datePurchased = effectiveDate;}
    	
        if (lastPerformedDate == null || lastPerformedDate.equals("")) {
            lastPerformedDate = sdf.format(new Date()); 
        }
        if (nextPerformedDate == null || nextPerformedDate.equals("")) {
            nextPerformedDate = sdf.format(new Date());
        }  
        java.sql.Date noticeDate = null;  
        int intFreq = 0;
        if (firstNoticeDate == null) {
            noticeDate = null;
        } else {
            noticeDate = dateConvert(firstNoticeDate);
        }
        //if (freq == null) {
        intFreq = Integer.parseInt(freq);
        // } 
        Connection con = null;
        PreparedStatement ps = null;
        FleetTransaction fleetTran = null;
        String createQuery = "INSERT INTO FT_MATERIALRETRIEVAL_HISTORY( " +
                             " TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO " +
                             " ,REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                             ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                             ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                             ",NEXT_PM_DATE,MAINTENANCE_TYPE,USER_ID," +
                             "FIRST_NOT_DATE,NOTIFICATION_FREQ,INVOICE_NO,"+
                             "HIST_ID,VAT_AMT,WHT_AMT,PROJECT_CODE,CREATE_DATE,ASSET_CODE,BRANCH_ID,subject_TO_WHT,subject_TO_Vat,TECHNICIAN_CODE) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        if (isTransactionOverFlow(cost, assetMake, category)) {
            setOverFlow(true);
        } else {

            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(createQuery);
//                System.out.println("=======techName: "+techName);
                ps.setString(1, type);
                ps.setDouble(2, cost);
                ps.setString(3, assetId);
                ps.setString(4, registrationNo);
                ps.setDate(5, dateConvert(dateOfRepair));
                ps.setString(6, technicianType);
                ps.setString(7, techName);
                ps.setDouble(8, milleageBeforeMaintenace);
                ps.setDouble(9, milleageAfterMaintenace);
                ps.setString(10, details);
                ps.setString(11, componentReplaced);
                ps.setDate(12, dateConvert(lastPerformedDate));
                ps.setDate(13, dateConvert(nextPerformedDate));
                ps.setString(14, maintenanceType);
                ps.setString(15, userid);
                ps.setDate(16, noticeDate);
                ps.setInt(17, intFreq);
                ps.setString(18, invoiceNo);
                ps.setString(19, histId);
                ps.setDouble(20, vatAmt);
                ps.setDouble(21, whtAmt);
                ps.setString(22, projectCode);
                ps.setTimestamp(23, dbConnection.getDateTime(new java.util.Date()));
                ps.setInt(24, assetCode);
                ps.setString(25, branchId);
                ps.setString(26, selectWht);
                ps.setString(27, selectVat); 
                ps.setString(28, techName);
                ps.execute();
                int isCreated = ps.getUpdateCount();

                String assetMaintenance = "";
                String location = "";
                boolean entryRaised = false;
                double depreciation = 0.00d;
                double premiumLiveToDate = cost;
                double premimumPeriodToDate = cost;
                double maintLiveToDate = cost;
                double maintPeriodToDate = cost;
                double fuelLiveToDate = fuelCost;
                double fuelPeriodToDate = fuelCost;
                int accidentCount = 0;
                double accidentCostLiveToDate = 0.00d;
                double accidentCostPeriodToDate = 0.00d;
                double licencePermitLiveToDate = 0.00d;
                double licencePermitPeriodToDate = 0.00d;
                String lastUpdateDate = sdf.format(new java.util.Date());
                String status = "A";

                fleetTran = new FleetTransaction(assetId, registrationNo,
                                                 branchId, deptId, category,
                                                 datePurchased,
                                                 assetMake, userid,
                                                 assetMaintenance,
                                                 createDate, effectiveDate,
                                                 location, entryRaised,
                                                 depreciation,
                                                 premiumLiveToDate,
                                                 premimumPeriodToDate,
                                                 maintLiveToDate,
                                                 maintPeriodToDate,
                                                 fuelLiveToDate,
                                                 fuelPeriodToDate,
                                                 accidentCount,
                                                 accidentCostLiveToDate,
                                                 accidentCostPeriodToDate,
                                                 licencePermitLiveToDate,
                                                 licencePermitPeriodToDate,
                                                 lastUpdateDate,
                                                 status, userid);

//System.out.println("<<<<createDate: "+createDate+"   effectiveDate: "+effectiveDate+"  datePurchased: "+datePurchased+"  premimumPeriodToDate: "+premimumPeriodToDate);
                tranManager.logFleetTransaction(fleetTran);

            } catch (Exception e) {
                System.out.println("INFO:Error creating MATERIALRETRIEVAL_HISTORY ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
        }
    }


    public FleetManatainanceRecord findMaterialRetrievalRecordByAssetId(String Id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE,NEXT_NOT_DATE,VAT_RATE,WHT_RATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,TECHNICIAN_CODE " +
                "FROM FT_MAINTENANCE_HISTORY WHERE HIST_ID = '"+Id+"' ";
//        System.out.println("selectQuery in findMaterialRetrievalRecordByAssetId: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String ltId = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
//                String technicianName = rs.getString("TECHNICIAN_NAME");
                String technicianName = rs.getString("TECHNICIAN_CODE");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String nextNotificationDate = formatDate(rs.getDate(
                        "NEXT_NOT_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                String projectCode = rs.getString("PROJECT_CODE");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                maintenanceRecord = new FleetManatainanceRecord(ltId, type,
                        cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);
                maintenanceRecord.setNextNotificationDate(nextNotificationDate);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Material Retrieval Records in findMaterialRetrievalRecordByAssetId ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public void removeMaterialRetrievalRecordById(String id) {
        String query = "DELETE FROM FT_MATERIALRETRIEVAL_HISTORY WHERE LT_ID = " + id;
        excuteSQLCode(query);
    }    

    /**
     * findMaterialRetrievalRecordById
     *
     * @param id String
     * @return findMaterialRetrievalRecordById
     */
    public ArrayList findMaterialRetrievalRecordByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT,VAT_RATE,WHT_RATE " +
                "FROM FT_MATERIALRETRIEVAL_HISTORY WHERE LT_ID != ''  " + queryFilter;
//        System.out.println("<<<<selectQuery in findMaterialRetrievalRecordByQuery====: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("LT_ID");
                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                String projectCode = rs.getString("PROJECT_CODE");
                maintenanceRecord = new FleetManatainanceRecord(id, type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);
                finder.add(maintenanceRecord);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching MaterialRetrieval Records in findMaterialRetrievalRecordByQuery ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    public java.util.ArrayList findMaterialRetrievalDetailRecordById(String histId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT LT_ID,HIST_ID,TYPE,COST_PRICE,QUANTITY,DESCRIPTION,RET_SERIAL_NO,RET_MAKE,RET_QUANTITY,QUANTITY_SOLD " +
    				"FROM FT_MAINTENANCE_DETAILS WHERE HIST_ID = '"+histId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
//    	 System.out.println("the Query  in findMaterialRetrievalDetailRecordById is <<<<<<<<<<<<< " + query);
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	 
    				int ltId = rs.getInt("LT_ID");
    				String type = rs.getString("TYPE");
                    String description = rs.getString("DESCRIPTION");
                    String costprice = rs.getString("COST_PRICE");
                    int qtyRet = rs.getInt("RET_QUANTITY");
                    int qtySold = rs.getInt("QUANTITY_SOLD");
                    String retMake = rs.getString("RET_MAKE");
                    String retSerialNo = rs.getString("RET_SERIAL_NO");
    				AssetRecordsBean trans = new AssetRecordsBean();
    				trans.setDescription(description);
    				trans.setCost_price(costprice);
    				trans.setQuantity(qtyRet);
    				trans.setQty(String.valueOf(qtySold));
    				trans.setSpare_1(retSerialNo);
    				trans.setSpare_2(retMake);
    				trans.setAssetCode(ltId);
//    				System.out.println("ltId in findMaterialRetrievalDetailRecordById is <<<<<<<<<<<<< " + query);
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


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findMaterialRetrievalByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
   
        String selectQuery = "select b.LT_ID,a.HIST_ID,ASSET_ID,a.TYPE,DESCRIPTION,a.DETAILS,a.COST_PRICE AS TOTAL_COST,b.COST_PRICE,QUANTITY,REGISTRATION_NO,REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME,MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT,COMPONENT_REPLACED,INVOICE_NO,USER_ID,ASSET_CODE,a.STATUS,b.SERIAL_NO, b.MAKE,a.BRANCH_ID,b.RET_SERIAL_NO,B.RET_MAKE,b.RET_QUANTITY,QUANTITY_SOLD,AMOUNT_SOLD from FT_MAINTENANCE_HISTORY a, FT_MAINTENANCE_DETAILS b where a.HIST_ID = b.HIST_ID "
        		+queryFilter;
//        System.out.println("<<<<<<<selectQuery in findMaterialRetrievalByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findMaterialRetrievalByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, queryFilter);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("LT_ID");
                String histId = rs.getString("HIST_ID");
                String assetId = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
//                String departmentId = rs.getString("DEPT_ID");
//                String section = rs.getString("SECTION");
//                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
//                String datePurchased = formatDate(rs.getDate("REPAIRED_DATE"));
//                double totalCost = rs.getDouble("TOTAL_COST");
                String componetReplace = rs.getString("COMPONENT_REPLACED");
                String assetUser = rs.getString("USER_ID");
                String assetMake = rs.getString("MAKE");
                String serialNo = rs.getString("SERIAL_NO");
                String assetMaintenance = rs.getString("DETAILS");
//                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
//                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
//                String depreciationEndDate = formatDate(rs.getDate(
//                        "DEP_END_DATE"));
//                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                  String postingDate = rs.getString("REPAIRED_DATE");
//                String entryRaised = rs.getString("RAISE_ENTRY");
//                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                  double nbv = rs.getDouble("TOTAL_COST");
//                double revalue_cost = rs.getDouble("REVALUE_COST");
                int quantity = rs.getInt("QUANTITY");
//                int totalLife = rs.getInt("TOTAL_LIFE");
//                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("STATUS");
                int assetCode = rs.getInt("asset_code");
//                double improvcost = rs.getDouble("IMPROV_COST");
//                double improvnbv = rs.getDouble("IMPROV_NBV");
//                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
//                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
//                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String invoiceNo = rs.getString("INVOICE_NO"); 
                String retAssetMake = rs.getString("RET_MAKE");
                String retSerialNo = rs.getString("RET_SERIAL_NO");
                int retquantity = rs.getInt("RET_QUANTITY");
                int quantitySold = rs.getInt("QUANTITY_SOLD");
                double amountSold = rs.getDouble("AMOUNT_SOLD");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       "", "", "",
                                       description,
                                       "", 0.00,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       0.00,
                                       0.00, cost,
                                       "",
                                       0.00, postingDate, "",
                                       0.00);
                aset.setNbv(nbv);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setQuantity(quantity);
                aset.setOLD_ASSET_ID(assetId);
                aset.setSpare2(histId);
                aset.setRegistrationNo(registrationNo);
                aset.setAssetMake(assetMake);
                aset.setSerialNo(serialNo);
                aset.setSpare1(componetReplace);
                aset.setTotalLife(retquantity);               
                aset.setSpare3(retAssetMake);
                aset.setSpare4(retSerialNo);
                aset.setAMOUNT_PTD(amountSold);
                aset.setDriver(quantitySold);
                list.add(aset);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Material Retrieval in findMaterialRetrievalByQuery First ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAllAssetMaterialList
     *
     * @return ArrayList
     */
    public ArrayList findAssetMaterialByQueryFleet(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
        String selectQuery = "select 'ASSET1' AS TableType,*from am_asset a, FT_MAINTENANCE_HISTORY b where a.Asset_id = b.ASSET_ID and a.Asset_id !='' "+ 
				  queryFilter;       
//       System.out.println("<<<<<<<selectQuery in findAssetByQueryFleet: "+selectQuery);		
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, queryFilter);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                String tableType = rs.getString("TableType");
                String histId = rs.getString("HIST_ID");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                aset.setItemType(tableType);
                aset.setSpare1(histId);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetMaterialByQueryFleet->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    /**
     * findAssetMaterialById
     *
     * @param id String
     * @return Asset
     */
    public ArrayList findAssetMaterialFleetById(String assetId) {

        String selectQuery =
                "SELECT ASSET_ID,LT_ID,HIST_ID,REGISTRATION_NO,BRANCH_ID, DETAILS,CREATE_DATE,VAT_RATE,"
                + "COST_PRICE,TECHNICIAN_TYPE,TECHNICIAN_NAME,MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT,"
                + "COMPONENT_REPLACED,LAST_PM_DATE,FIRST_NOT_DATE,NEXT_PM_DATE,"
                + "ASSET_CODE,TECHNICIAN_CODE,subject_TO_Vat,subject_TO_WHT "
                + "FROM FT_MAINTENANCE_HISTORY  WHERE ASSET_ID = ?";
//        System.out.println("<<<<<<<selectQuery in findAssetFleetById: "+selectQuery+"         Asset Id: "+id);
        Connection con = null;
        PreparedStatement ps = null;
        FleetManatainanceRecord maintenanceRecord = null;
        ArrayList finder = new ArrayList();
        ResultSet rs = null;

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, assetId);
            rs = ps.executeQuery();

            while (rs.next()) {

                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String type = rs.getString("TYPE");
                String ltid = rs.getString("LT_ID");
                String histId = rs.getString("HIST_ID");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String createDate = formatDate(rs.getDate("CREATE_DATE"));
                double vatrate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String technicianType = rs.getString("TECHNICIAN_TYPE");  
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");  
                String techName = rs.getString("TECHNICIAN_NAME");   
                double millageBefore = rs.getDouble("MILLEAGE_BEFORE_MAINT"); 
                double millageAfter = rs.getDouble("MILLEAGE_AFTER_MAINT");    
                String techCode = rs.getString("TECHNICIAN_CODE"); 
                String invoiceNo = rs.getString("INVOICE_NO"); 
                String status = rs.getString("STATUS"); 
                String projectCode = rs.getString("STATUS"); 
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_CODE"); 
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String lastPMdate = formatDate(rs.getDate("LAST_PM_DATE"));
                String firstNotDate = formatDate(rs.getDate("FIRST_NOT_DATE"));
                String nextPMdate = formatDate(rs.getDate("NEXT_PM_DATE"));               
                maintenanceRecord = new FleetManatainanceRecord(ltid, type, cost,
                        assetCode, registrationNo, createDate, technicianType,
                        techName, millageBefore,
                        millageAfter, details, componentReplaced,
                        lastPMdate, nextPMdate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatrate,whtRate);               
                finder.add(maintenanceRecord);                      
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching assetDetail ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;
    }

    /**
     * findMaintenaceRecordById
     *
     * @param id String
     * @return FleetManatainanceRecord
     */
    public FleetManatainanceRecord findMatRecordById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FleetManatainanceRecord maintenanceRecord = null;
        String selectQuery =
                "SELECT LT_ID,TYPE,COST_PRICE,ASSET_ID,REGISTRATION_NO,PROJECT_CODE " +
                ",REPAIRED_DATE,TECHNICIAN_TYPE,TECHNICIAN_NAME " +
                ",MILLEAGE_BEFORE_MAINT,MILLEAGE_AFTER_MAINT " +
                ",DETAILS,COMPONENT_REPLACED,LAST_PM_DATE,VAT_RATE,WHT_RATE " +
                ",NEXT_PM_DATE,MAINTENANCE_TYPE,STATUS,HIST_ID,INVOICE_NO,VAT_AMT,WHT_AMT " +
                "FROM FT_MAINTENANCE_HISTORY WHERE HIST_ID = ?";
//        System.out.println("selectQuery in findMaintenaceRecordById: "+selectQuery);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                String type = rs.getString("TYPE");
                double cost = rs.getDouble("COST_PRICE");
                String assetCode = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String dateOfRepair = formatDate(rs.getDate("REPAIRED_DATE"));
                String technicianType = rs.getString("TECHNICIAN_TYPE");
                String technicianName = rs.getString("TECHNICIAN_NAME");
                double milleageBeforeMaintenance = rs.getDouble(
                        "MILLEAGE_BEFORE_MAINT");
                double milleageAfterMaintenance = rs.getDouble(
                        "MILLEAGE_AFTER_MAINT");
                String details = rs.getString("DETAILS");
                String componentReplaced = rs.getString("COMPONENT_REPLACED");
                String lastPerformedDate = formatDate(rs.getDate(
                        "LAST_PM_DATE"));
                String nextPerformedDate = formatDate(rs.getDate(
                        "NEXT_PM_DATE"));
                String maintenanceType = rs.getString("MAINTENANCE_TYPE");
                String status = rs.getString("STATUS");
                String histId = rs.getString("HIST_ID");
                String invoiceNo = rs.getString("INVOICE_NO");
                double vatAmt = rs.getDouble("VAT_AMT");
                double whtAmt = rs.getDouble("WHT_AMT");
                String projectCode = rs.getString("PROJECT_CODE");
                double vatRate = rs.getDouble("VAT_RATE");
                double whtRate = rs.getDouble("WHT_RATE");
                maintenanceRecord = new FleetManatainanceRecord(id, type, cost,
                        assetCode, registrationNo, dateOfRepair, technicianType,
                        technicianName, milleageBeforeMaintenance,
                        milleageAfterMaintenance, details, componentReplaced,
                        lastPerformedDate, nextPerformedDate, maintenanceType,
                        status, histId, invoiceNo, vatAmt, whtAmt,projectCode,vatRate,whtRate);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error Fetching Maintenace Records in findMaintenaceRecordById ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return maintenanceRecord;
    }

    public java.util.ArrayList findMaterialRetrievalSoldDetailRecordById(String histId)
    { 
    	java.util.ArrayList _list = new java.util.ArrayList();
    	String finacleTransId= null;
    		String query = " SELECT LT_ID,HIST_ID,TYPE,AMOUNT_SOLD,QUANTITY,DESCRIPTION,RET_SERIAL_NO,RET_MAKE,RET_QUANTITY,QUANTITY_SOLD " +
    				"FROM FT_MAINTENANCE_DETAILS_TMP WHERE HIST_ID = '"+histId+"' ";
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
//    	 System.out.println("the Query  in findMaterialRetrievalDetailRecordById is <<<<<<<<<<<<< " + query);
    	try {
    		    c = getConnection("legendPlus");
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {	 
    				int ltId = rs.getInt("LT_ID");
    				String type = rs.getString("TYPE");
                    String description = rs.getString("DESCRIPTION");
                    String costprice = rs.getString("AMOUNT_SOLD");
                    int qtyRet = rs.getInt("RET_QUANTITY");
                    int qtySold = rs.getInt("QUANTITY_SOLD");
                    String retMake = rs.getString("RET_MAKE");
                    String retSerialNo = rs.getString("RET_SERIAL_NO");
    				AssetRecordsBean trans = new AssetRecordsBean();
    				trans.setDescription(description);
    				trans.setCost_price(costprice);
    				trans.setQuantity(qtyRet);
    				trans.setQty(String.valueOf(qtySold));
    				trans.setSpare_1(retSerialNo);
    				trans.setSpare_2(retMake);
    				trans.setAssetCode(ltId);
//    				System.out.println("ltId in findMaterialRetrievalDetailRecordById is <<<<<<<<<<<<< " + query);
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


    /**
     * findAllAsset
     *
     * @return ArrayList
     */
    public ArrayList findAssetReclassificationByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;
        Statement stmt = null;
        CallableStatement cstmt = null;
//        String selectQuery_old = 
//                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
//                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
//                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
//                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
//                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
//                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
//                "FROM AM_ASSET  WHERE ASSET_ID NOT IN (SELECT ASSET_ID FROM am_assetReclassification) " +
//                "AND ASSET_ID IS NOT NULL ?";
 
        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_assetReclassification where Status != 'REJECTED' OR Status IS NULL ) and Asset_id !='' "+
        queryFilter;
//        String selectQuery = "select * from am_asset where Asset_id not in (select Asset_id from am_gb_bulkTransfer) and Asset_id !=(?)";
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+selectQuery);	
//       System.out.println("<<<<<<<selectQuery in findAssetByQuery: "+queryFilter);	
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
//            ps.setString(1, queryFilter);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                double improvcost = rs.getDouble("IMPROV_COST");
                double improvnbv = rs.getDouble("IMPROV_NBV");
                double improveAccumdep = rs.getDouble("IMPROV_ACCUMDEP");
                double improveMonthlydep = rs.getDouble("IMPROV_MONTHLYDEP");
                double improveTotalNbv = rs.getDouble("TOTAL_NBV");
                
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                aset.setImprovcost(improvcost);
                aset.setImprovnbv(improvnbv);
                aset.setImprovaccumulatedDepreciation(improveAccumdep);
                aset.setImprovmonthlyDepreciation(improveMonthlydep);
                aset.setImprovTotalNbv(improveTotalNbv);
                
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);


                //getMinMaxAssetCost(minCost,maxCost);
                setMinCost(minCost);
                setMaxCost(maxCost);
            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset in findAssetReclassificationByQuery First ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }



   
}
