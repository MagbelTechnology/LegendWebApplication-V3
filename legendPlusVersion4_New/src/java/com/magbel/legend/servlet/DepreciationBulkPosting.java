package com.magbel.legend.servlet;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.newAssetTransaction;

import au.com.bytecode.opencsv.CSVWriter;
import magma.AssetRecordsBean;
import ng.com.magbel.token.ZenithTokenClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import com.jcraft.jsch.*;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class DepreciationBulkPosting extends HttpServlet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DepreciationBulkPosting.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final SimpleDateFormat COMPACT_DATE_FORMAT = new SimpleDateFormat("ddMMyyyyHHmmss");
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
    
    private Properties configProperties;
    private EmailSmsServiceBus emailService;
    private ApprovalRecords approvalRecords;
    private AssetRecordsBean assetRecords;
    private String batchFolder;
    private String bank;
    private String batchApiUrl;
    
    @Override
    public void init() throws ServletException {
        try {
            configProperties = loadProperties();
            validateConfiguration();
            
            batchFolder = configProperties.getProperty("BatchFolder");
            bank = configProperties.getProperty("bank");
            batchApiUrl = configProperties.getProperty("BatchApiUrl", "");
            
            emailService = new EmailSmsServiceBus();
            approvalRecords = new ApprovalRecords();
            assetRecords = new AssetRecordsBean();
            
            LOGGER.info("DepreciationBulkPosting initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize servlet", e);
            throw new ServletException("Initialization failed", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestContext context = new RequestContext(request, response);
        
        try {
            processRequest(context);
        } catch (Exception e) {
            LOGGER.error("Error processing request", e);
            handleError(context, "An error occurred while processing the request: " + e.getMessage());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    private void processRequest(RequestContext context) throws Exception {
        UserInfo userInfo = extractUserInfo(context);
        
        if (!userInfo.hasRequiredRights()) {
            showAlertAndRedirect(context, "User does not have right to post. Contact the Administrator for help.");
            return;
        }
        
        String batchNo = generateBatchNumber();
        PostingParameters params = extractPostingParameters(context, userInfo);
        
        Report report = new Report();
        String narration = buildNarration(params.processingDate);
        String prefix = getAccountPrefix(params.type);
        
        ArrayList<?> transactions = report.getDepreciationPostingRecords(prefix, narration);
        
        if (transactions.isEmpty()) {
            showAlertAndRedirect(context, "No records found for processing");
            return;
        }
        
        processTransactions(context, transactions, params, batchNo);
    }
    
    private void processTransactions(RequestContext context, ArrayList<?> transactions, 
                                    PostingParameters params, String batchNo) throws Exception {
        
        String appName = approvalRecords.getCodeName(
            "SELECT APP_NAME+'-'+VERSION FROM AM_AD_LEGACY_SYS_CONFIG");
        
        String currency = approvalRecords.getCodeName(
            "SELECT iso_code FROM AM_GB_CURRENCY_CODE WHERE local_currency = 'Y'");
        
        String fileName = generateFileName(params, appName);
        String currentDate = COMPACT_DATE_FORMAT.format(new Date());
        String dateField = DISPLAY_DATE_FORMAT.format(new Date());
        
        // Special case for FLEXICUBE-10.2.18 with batch API
        if (appName.equalsIgnoreCase("FLEXICUBE-10.2.18") && batchApiUrl != null && !batchApiUrl.isEmpty()) {
            processFlexcubeBatch(context, transactions, params, batchNo, currency, fileName, currentDate, dateField);
        } 
        // Regular Excel download for other cases
        else {
            WorkbookCreatorFactory factory = new WorkbookCreatorFactory();
            WorkbookCreator creator = factory.getCreator(appName, currency);
            
            if (creator == null) {
                throw new ServletException("No workbook creator found for application: " + appName);
            }
            
            processExcelDownload(context, transactions, creator, fileName);
        }
    }
    
    private void processFlexcubeBatch(RequestContext context, ArrayList<?> transactions,
                                      PostingParameters params, String batchNo, String currency,
                                      String fileName, String currentDate, String dateField) throws Exception {
        
        String fullPath = batchFolder + File.separator + fileName;
        String sourceCode = "LEGEND";
        
        // Create the batch file
        Flexcube1018BatchCreator batchCreator = new Flexcube1018BatchCreator(
            currency, batchNo, sourceCode, params.maker, params.checker, 
            params.userId, params.type, params.processingDate, dateField, approvalRecords
        );
        
        boolean fileCreated = batchCreator.createBatchFile(fullPath, transactions);
        
        if (!fileCreated) {
            showAlertAndRedirect(context, "Failed to create batch file");
            return;
        }
        
        // Upload the file via SFTP
        boolean uploadSuccess = flexCubeFileUpload(fullPath);
        
        if (uploadSuccess) {
            // Update status in database
            assetRecords.updateAssetStatusChange(
                "update AM_GB_BATCH_POSTING set Legacy_status='Y' where GROUP_ID=?", 
                params.processingDate);
            
            LOGGER.info("Batch successfully uploaded for processing date: {}", params.processingDate);
            showAlertAndRedirect(context, "Successfully dropped for execution");
        } else {
            // Rollback - delete records if upload failed
            assetRecords.updateAssetStatusChange(
                "delete from AM_GB_BATCH_POSTING where GROUP_ID=?", 
                params.processingDate);
            
            LOGGER.error("Failed to upload batch file for processing date: {}", params.processingDate);
            showAlertAndRedirect(context, "File could not be dropped for execution");
        }
    }
    
    private void processExcelDownload(RequestContext context, ArrayList<?> transactions,
                                      WorkbookCreator creator, String fileName) throws IOException {
        
        context.response.setContentType("application/vnd.ms-excel");
        context.response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        try (Workbook workbook = creator.createWorkbook()) {
            creator.populateWorkbook(workbook, transactions);
            workbook.write(context.response.getOutputStream());
            LOGGER.info("Excel file generated successfully: {}", fileName);
        }
    }
    
    private UserInfo extractUserInfo(RequestContext context) {
        UserInfo userInfo = new UserInfo();
        userInfo.userId = (String) context.request.getSession().getAttribute("CurrentUser");
        userInfo.userClass = (String) context.request.getSession().getAttribute("UserClass");
        userInfo.userName = approvalRecords.getCodeName(
            "SELECT USER_NAME FROM am_gb_User WHERE USER_ID = '" + userInfo.userId + "'");
        userInfo.deptCode = approvalRecords.getCodeName(
            "select dept_code from am_gb_User where USER_ID = '" + userInfo.userId + "'");
        userInfo.maker = approvalRecords.getCodeName(
            "select Legacy_Sys_id from am_gb_User where USER_ID = '" + userInfo.userId + "'");
        userInfo.checker = userInfo.maker;
        
        return userInfo;
    }
    
    private PostingParameters extractPostingParameters(RequestContext context, UserInfo userInfo) {
        PostingParameters params = new PostingParameters();
        params.branchId = context.request.getParameter("branch_id");
        params.processingDate = context.request.getParameter("processingDate");
        params.type = context.request.getParameter("type");
        params.userId = userInfo.userId;
        params.maker = userInfo.maker;
        params.checker = userInfo.checker;
        params.userName = userInfo.userName;
        
        if (params.branchId == null || params.branchId.isEmpty() || "***".equals(params.branchId)) {
            params.branchId = approvalRecords.getCodeName(
                "select BRANCH from am_gb_User where USER_ID = '" + userInfo.userId + "'");
        }
        
        params.branchCode = approvalRecords.getCodeName(
            "select BRANCH_CODE from am_ad_branch where BRANCH_ID = '" + params.branchId + "'");
        
        return params;
    }
    
    private String generateBatchNumber() {
        if (batchApiUrl == null || batchApiUrl.isEmpty()) {
            return "123456";
        }
        
        try {
            String status = ZenithTokenClass.validation();
            JSONObject json = new JSONObject(status);
            return json.getString("batchId");
        } catch (Exception e) {
            LOGGER.error("Failed to generate batch number", e);
            return "123456";
        }
    }
    
    private String buildNarration(String processingDate) {
        if (processingDate == null || processingDate.isEmpty()) {
            return "DEPRECIATION POSTING";
        }
        return "DEPRECIATON FOR THE MONTH OF " + 
               getMonthPartOfDate(processingDate).toUpperCase() + " " + 
               getYearPartOfDate(processingDate);
    }
    
    private String getAccountPrefix(String type) {
        if (type == null || type.isEmpty()) {
            return "";
        }
        return approvalRecords.getCodeName(
            "select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = '" + bank + 
            "' AND TYPE = '" + type + "'");
    }
    
    private String generateFileName(PostingParameters params, String appName) {
        String currentDate = COMPACT_DATE_FORMAT.format(new Date());
        
        if (batchApiUrl != null && !batchApiUrl.isEmpty()) {
            return "DE_UPLOAD_LEGENDDEPR_" + currentDate + ".csv";
        }
        
        String branchCode = params.branchCode != null ? params.branchCode : "XXX";
        String userName = params.userName != null ? params.userName : "USER";
        return branchCode + "By" + userName + "FinacleAssetExport.xls";
    }
    
    private void showAlertAndRedirect(RequestContext context, String message) throws IOException {
        PrintWriter out = context.response.getWriter();
        out.println("<script type='text/javascript'>alert('" + message + "');</script>");
        out.println("<script> window.location ='DocumentHelp.jsp?np=legacyExporter&s=n'</script>");
    }
    
    private void handleError(RequestContext context, String message) throws IOException {
        PrintWriter out = context.response.getWriter();
        out.println("<script type='text/javascript'>alert('" + message + "');</script>");
        out.println("<script> window.location ='DocumentHelp.jsp?np=legacyExporter&s=n'</script>");
    }
    
    private Properties loadProperties() throws IOException {
        Properties props = new Properties();
        String propPath = System.getProperty("legend.properties.path", "C:\\Property\\LegendPlus.properties");
        try (FileInputStream input = new FileInputStream(propPath)) {
            props.load(input);
        }
        return props;
    }
    
    private void validateConfiguration() throws ServletException {
        List<String> missingProps = new ArrayList<>();
        
        if (!configProperties.containsKey("BatchFolder") || configProperties.getProperty("BatchFolder") == null) {
            missingProps.add("BatchFolder");
        }
        if (!configProperties.containsKey("bank") || configProperties.getProperty("bank") == null) {
            missingProps.add("bank");
        }
        
        if (!missingProps.isEmpty()) {
            throw new ServletException("Missing required properties: " + missingProps);
        }
    }
    
    public String getMonthPartOfDate(String date) {
        if (date == null || date.isEmpty()) {
            LOGGER.warn("Date is null or empty");
            return "";
        }
        try {
            Date myDate = DISPLAY_DATE_FORMAT.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            String[] monthName = {"January", "February", "March", "April", "May", "June",
                                 "July", "August", "September", "October", "November", "December"};
            return monthName[cal.get(Calendar.MONTH)];
        } catch (Exception e) {
            LOGGER.error("Error parsing date for month: {}", date, e);
            return "";
        }
    }
    
    public String getYearPartOfDate(String date) {
        if (date == null || date.isEmpty()) {
            LOGGER.warn("Date is null or empty");
            return "";
        }
        try {
            Date myDate = DISPLAY_DATE_FORMAT.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            return Integer.toString(cal.get(Calendar.YEAR));
        } catch (Exception e) {
            LOGGER.error("Error parsing date for year: {}", date, e);
            return "";
        }
    }
    
    public boolean flexCubeFileUpload(String fullPath) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;
        
        try {
            String server = configProperties.getProperty("FlexCube-Upload-Address");
            int port = Integer.parseInt(configProperties.getProperty("FlexCube-Upload-Port", "22"));
            String user = configProperties.getProperty("FlexCube-Upload-User");
            String pass = configProperties.getProperty("FlexCube-Upload-Password");
            String folder = configProperties.getProperty("FlexCube-Upload-Folder");
            
            if (server == null || user == null || pass == null || folder == null) {
                LOGGER.error("Missing SFTP configuration properties");
                return false;
            }
            
            session = jsch.getSession(user, server, port);
            session.setPassword(pass);
            
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(folder);
            
            File file = new File(fullPath);
            if (!file.exists()) {
                LOGGER.error("File does not exist: {}", fullPath);
                return false;
            }
            
            try (FileInputStream fis = new FileInputStream(file)) {
                channelSftp.put(fis, file.getName());
            }
            
            LOGGER.info("File uploaded successfully: {}", fullPath);
            return true;
            
        } catch (Exception e) {
            LOGGER.error("SFTP upload failed for file: {}", fullPath, e);
            return false;
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
    

    
    // ==================== INNER CLASSES ====================
    
    /**
     * Request context wrapper
     */
    private static class RequestContext {
        final HttpServletRequest request;
        final HttpServletResponse response;
        
        RequestContext(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }
    }
    
    /**
     * User information container
     */
    private static class UserInfo {
        String userId;
        String userClass;
        String userName;
        String deptCode;
        String maker;
        String checker;
        
        boolean hasRequiredRights() {
            return maker != null && !maker.isEmpty() && 
                   checker != null && !checker.isEmpty();
        }
    }
    
    /**
     * Posting parameters container
     */
    private static class PostingParameters {
        String branchId;
        String branchCode;
        String processingDate;
        String type;
        String userId;
        String maker;
        String checker;
        String userName;
    }
    
    // ==================== WORKBOOK CREATOR INTERFACE ====================
    
    /**
     * Interface for workbook creators
     */
    public interface WorkbookCreator {
        Workbook createWorkbook();
        void populateWorkbook(Workbook workbook, ArrayList<?> transactions);
    }
    
    // ==================== WORKBOOK CREATOR FACTORY ====================
    
    /**
     * Factory class for creating appropriate workbook creators
     */
    public class WorkbookCreatorFactory {
        
        public WorkbookCreator getCreator(String appName, String currency) {
            if (appName == null) {
                throw new IllegalArgumentException("Application name cannot be null");
            }
            
            if (appName.equalsIgnoreCase("FINACLE-7.0.9")) {
                return new Finacle709WorkbookCreator();
            } else if (appName.equalsIgnoreCase("FINACLE-10.2.18")) {
                if (currency == null) {
                    throw new IllegalArgumentException("Currency cannot be null for FINACLE-10.2.18");
                }
                return new Finacle1018WorkbookCreator(currency);
            } else if (appName.equalsIgnoreCase("FLEXCUBE-7.0.9")) {
                return new Flexcube709WorkbookCreator();
            } else if (appName.equalsIgnoreCase("FLEXICUBE-10.2.18")) {
                // This is handled separately in processTransactions method
                return null;
            } else {
                throw new IllegalArgumentException("Unsupported application: " + appName);
            }
        }
    }
    
    // ==================== FINACLE 7.0.9 WORKBOOK CREATOR ====================
    
    /**
     * Workbook creator for FINACLE-7.0.9
     */
    public class Finacle709WorkbookCreator implements WorkbookCreator {
        
        @Override
        public Workbook createWorkbook() {
            return new HSSFWorkbook();
        }
        
        @Override
        public void populateWorkbook(Workbook workbook, ArrayList<?> transactions) {
            Sheet sheet = workbook.createSheet("Finacle Asset Export Record");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Account No.", "DR CR", "Amount", "Description", "E", 
                "F", "Asset Id", "H", "I", "SBU CODE"
            };
            
            // Style header row
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Object obj : transactions) {
                newAssetTransaction trans = (newAssetTransaction) obj;
                Row row = sheet.createRow(rowNum++);
                
                // Account No.
                row.createCell(0).setCellValue(safeString(trans.getVendorAC()));
                
                // DR CR
                row.createCell(1).setCellValue(safeString(trans.getBarCode()));
                
                // Amount
                Cell amountCell = row.createCell(2);
                amountCell.setCellValue(trans.getCostPrice());
                amountCell.setCellType(CellType.NUMERIC);
                
                // Description
                row.createCell(3).setCellValue(safeString(trans.getDescription()));
                
                // E (Asset User)
                row.createCell(4).setCellValue(safeString(trans.getAssetUser()));
                
                // F (Asset Code)
                row.createCell(5).setCellValue(safeString(trans.getAssetCode()));
                
                // Asset Id
                row.createCell(6).setCellValue(safeString(trans.getAssetId()));
                
                // H (Category Code)
                row.createCell(7).setCellValue(safeString(trans.getCategoryCode()));
                
                // I (Integrify Id)
                row.createCell(8).setCellValue(safeString(trans.getIntegrifyId()));
                
                // SBU CODE
                row.createCell(9).setCellValue(safeString(trans.getSbuCode()));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        
        private String safeString(String value) {
            return value != null ? value : "";
        }
    }
    
    // ==================== FINACLE 10.2.18 WORKBOOK CREATOR ====================
    
    /**
     * Workbook creator for FINACLE-10.2.18
     */
    public class Finacle1018WorkbookCreator implements WorkbookCreator {
        
        private final String currency;
        
        public Finacle1018WorkbookCreator(String currency) {
            this.currency = currency;
        }
        
        @Override
        public Workbook createWorkbook() {
            return new HSSFWorkbook();
        }
        
        @Override
        public void populateWorkbook(Workbook workbook, ArrayList<?> transactions) {
            Sheet sheet = workbook.createSheet("Finacle Asset Export Record");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Account No.", "Currency", "Narration (30 xters max)", "Remarks1 (30 xters max)",
                "Remarks2 (50 xters max)", "Amount", "ValueDate(DD/MM/YYYY)", 
                "ReportCode", "Narration Checker", "Remarks1 Checker"
            };
            
            // Style header row
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create date cell style
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            
            // Create data rows
            int rowNum = 1;
            for (Object obj : transactions) {
                newAssetTransaction trans = (newAssetTransaction) obj;
                Row row = sheet.createRow(rowNum++);
                
                // Account No.
                row.createCell(0).setCellValue(safeString(trans.getVendorAC()));
                
                // Currency
                row.createCell(1).setCellValue(safeString(currency));
                
                // Narration (30 xters max) - truncate if longer than 30
                String narration = safeString(trans.getDescription());
                if (narration.length() > 30) {
                    narration = narration.substring(0, 27) + "...";
                }
                row.createCell(2).setCellValue(narration);
                
                // Remarks1 (30 xters max) - Asset ID
                String remarks1 = safeString(trans.getAssetId());
                if (remarks1.length() > 30) {
                    remarks1 = remarks1.substring(0, 27) + "...";
                }
                row.createCell(3).setCellValue(remarks1);
                
                // Remarks2 (50 xters max) - Asset User
                String remarks2 = safeString(trans.getAssetUser());
                if (remarks2.length() > 50) {
                    remarks2 = remarks2.substring(0, 47) + "...";
                }
                row.createCell(4).setCellValue(remarks2);
                
                // Amount
                Cell amountCell = row.createCell(5);
                amountCell.setCellValue(trans.getCostPrice());
                amountCell.setCellType(CellType.NUMERIC);
                
                // ValueDate(DD/MM/YYYY)
                Cell dateCell = row.createCell(6);
                dateCell.setCellValue(new Date());
                dateCell.setCellStyle(dateStyle);
                
                // ReportCode (SBU Code)
                row.createCell(7).setCellValue(safeString(trans.getSbuCode()));
                
                // Narration Checker (Integrify ID)
                row.createCell(8).setCellValue(safeString(trans.getIntegrifyId()));
                
                // Remarks1 Checker (Category Code)
                row.createCell(9).setCellValue(safeString(trans.getCategoryCode()));
            }
            
            // Auto-size columns for better readability
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        
        private String safeString(String value) {
            return value != null ? value : "";
        }
    }
    
    // ==================== FLEXCUBE 7.0.9 WORKBOOK CREATOR ====================
    
    /**
     * Workbook creator for FLEXCUBE-7.0.9
     */
    public class Flexcube709WorkbookCreator implements WorkbookCreator {
        
        @Override
        public Workbook createWorkbook() {
            return new HSSFWorkbook();
        }
        
        @Override
        public void populateWorkbook(Workbook workbook, ArrayList<?> transactions) {
            Sheet sheet = workbook.createSheet("Finacle Asset Export Record");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Account No.", "DR CR", "Amount", "Description", "E", 
                "F", "Asset Id", "H", "I", "SBU CODE"
            };
            
            // Style header row
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Object obj : transactions) {
                newAssetTransaction trans = (newAssetTransaction) obj;
                Row row = sheet.createRow(rowNum++);
                
                // Account No.
                row.createCell(0).setCellValue(safeString(trans.getVendorAC()));
                
                // DR CR
                row.createCell(1).setCellValue(safeString(trans.getBarCode()));
                
                // Amount
                Cell amountCell = row.createCell(2);
                amountCell.setCellValue(trans.getCostPrice());
                amountCell.setCellType(CellType.NUMERIC);
                
                // Description
                row.createCell(3).setCellValue(safeString(trans.getDescription()));
                
                // E (Asset User)
                row.createCell(4).setCellValue(safeString(trans.getAssetUser()));
                
                // F (Asset Code)
                row.createCell(5).setCellValue(safeString(trans.getAssetCode()));
                
                // Asset Id
                row.createCell(6).setCellValue(safeString(trans.getAssetId()));
                
                // H (Category Code)
                row.createCell(7).setCellValue(safeString(trans.getCategoryCode()));
                
                // I (Integrify Id)
                row.createCell(8).setCellValue(safeString(trans.getIntegrifyId()));
                
                // SBU CODE - First occurrence (column 9)
                row.createCell(9).setCellValue(safeString(trans.getSbuCode()));
                
                // Extra SBU CODE column (column 10) as in original
                row.createCell(10).setCellValue(safeString(trans.getSbuCode()));
            }
            
            // Auto-size columns for better readability (0-10 = 11 columns)
            for (int i = 0; i <= 10; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        
        private String safeString(String value) {
            return value != null ? value : "";
        }
    }
    
    // ==================== FLEXCUBE 10.2.18 BATCH CREATOR ====================
    
    /**
     * Batch creator for FLEXICUBE-10.2.18 (CSV format)
     */
    public class Flexcube1018BatchCreator {
        
        private final String currency;
        private final String batchNo;
        private final String sourceCode;
        private final String maker;
        private final String checker;
        private final String userId;
        private final String type;
        private final String processingDate;
        private final String dateField;
        private final ApprovalRecords approvalRecords;
        
        public Flexcube1018BatchCreator(String currency, String batchNo, String sourceCode, 
                                        String maker, String checker, String userId, 
                                        String type, String processingDate, String dateField,
                                        ApprovalRecords approvalRecords) {
            this.currency = currency;
            this.batchNo = batchNo;
            this.sourceCode = sourceCode;
            this.maker = maker;
            this.checker = checker;
            this.userId = userId;
            this.type = type;
            this.processingDate = processingDate;
            this.dateField = dateField;
            this.approvalRecords = approvalRecords;
        }
        
        /**
         * Creates a CSV batch file for Flexcube 10.2.18
         * @param fullPath The full path where the CSV file will be created
         * @param transactions List of transactions to process
         * @return true if successful, false otherwise
         */
        public boolean createBatchFile(String fullPath, ArrayList<?> transactions) {
            CSVWriter csvWriter = null;
            
            try {
                FileWriter fileWriter = new FileWriter(fullPath);
                csvWriter = new CSVWriter(fileWriter, 
                    CSVWriter.DEFAULT_SEPARATOR, 
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END);
                
                // Write header
                writeHeader(csvWriter);
                
                // Write data rows and save to database
                int recordCount = 0;
                for (Object obj : transactions) {
                    recordCount++;
                    newAssetTransaction transaction = (newAssetTransaction) obj;
                    
                    writeRecord(csvWriter, transaction, recordCount);
                    saveToDatabase(transaction, recordCount);
                }
                
                LOGGER.info("CSV batch file created successfully with {} records: {}", recordCount, fullPath);
                return true;
                
            } catch (Exception e) {
                LOGGER.error("Failed to create CSV batch file", e);
                return false;
            } finally {
                if (csvWriter != null) {
                    try {
                        csvWriter.close();
                    } catch (IOException e) {
                        LOGGER.error("Error closing CSV writer", e);
                    }
                }
            }
        }
        
        private void writeHeader(CSVWriter writer) {
            writer.writeNext(new String[]{
                "SRLNO", "UPLOAD_DATE", "ACC NUMBER", "AMT", "TRANSACTION TYPE",
                "TRANSACTION DESC", "TRANSACTION CODE", "CURRENCY", "BRANCH CODE",
                "PURPOSE CODE", "BATCH NUMBER", "SOURCE CODE", "MAKER_ID", "CHECKER_ID"
            });
        }
        
        private void writeRecord(CSVWriter writer, newAssetTransaction transaction, int recordNumber) {
            
            String description = transaction.getAssetId() + "**" + 
                                safeString(transaction.getDescription()) + "**LGD";
            
            writer.writeNext(new String[]{
                String.valueOf(recordNumber),
                dateField,
                safeString(transaction.getVendorAC()),
                DECIMAL_FORMAT.format(transaction.getCostPrice()),
                safeString(transaction.getAssetType()),
                description,
                "LGD",
                safeString(currency),
                safeString(transaction.getBranchCode()),
                "", // Purpose Code (empty as in original)
                batchNo,
                sourceCode,
                maker,
                checker
            });
        }
        
        private void saveToDatabase(newAssetTransaction transaction, int recordNumber) throws Exception {
            
            String status = "N";
            String description = transaction.getAssetId() + "**" + 
                                safeString(transaction.getDescription()) + "**LGD";
            
            // Check if record already exists
            String recordExists = approvalRecords.getCodeName(
                "select count(*) from AM_GB_BATCH_POSTING where id = " + recordNumber + 
                " AND GROUP_ID = '" + processingDate + "'");
            
            if (!"1".equals(recordExists)) {
                approvalRecords.insertBatchTransactions(
                    recordNumber, 
                    processingDate, 
                    dateField,
                    safeString(transaction.getVendorAC()), 
                    transaction.getCostPrice(),
                    safeString(transaction.getAssetType()), 
                    description, 
                    "LGD", 
                    safeString(currency),
                    safeString(transaction.getBranchCode()), 
                    userId, 
                    "", 
                    maker,
                    batchNo, 
                    checker, 
                    status, 
                    type, 
                    safeString(transaction.getAssetId())
                );
                
                LOGGER.debug("Saved batch transaction record {} to database", recordNumber);
            }
        }
        
        private String safeString(String value) {
            return value != null ? value : "";
        }
    }
}