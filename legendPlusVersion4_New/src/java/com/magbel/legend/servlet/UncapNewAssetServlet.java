package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import legend.admin.handlers.CompanyHandler;
import legend.admin.handlers.SecurityHandler;
import legend.admin.objects.User;
import magma.AssetRecordsBean;
import magma.net.manager.SytemsManager;
import magma.util.Codes;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.HtmlUtility;


@MultipartConfig(
	    fileSizeThreshold = 0x300000,  // 3MB
	    maxFileSize = 0xA00000,        // 10MB
	    maxRequestSize = 0x3200000     // 50MB
	)
	public class UncapNewAssetServlet extends HttpServlet {
	    
	    private static final Logger LOGGER = Logger.getLogger(UncapNewAssetServlet.class.getName());
	    private static final String PROPERTIES_PATH = "C:\\Property\\LegendPlus.properties";
	    private static final String CURRENT_PAGE = "DocumentHelp.jsp?np=updateAssetViewBranch";
	    private static final String TRANSACTION_TYPE = "Asset Update";
	    private static final String ROOT = "new";
	    private static final String TRANSACTION_LEVEL = "18";
	    
	    // Service layer dependencies
	    private EmailSmsServiceBus mailService;
	    private ApprovalRecords approvalRecords;
	    private SecurityHandler securityHandler;
	    private HtmlUtility htmlUtil;
	    private ApplicationHelper appHelper;
	    private CompanyHandler companyHandler;
	    private CurrencyNumberformat currencyFormatter;
	    
	    public UncapNewAssetServlet() {
	        this.mailService = new EmailSmsServiceBus();
	        this.approvalRecords = new ApprovalRecords();
	        this.securityHandler = new SecurityHandler();
	        this.htmlUtil = new HtmlUtility();
	        this.appHelper = new ApplicationHelper();
	        this.companyHandler = new CompanyHandler();
	        this.currencyFormatter = new CurrencyNumberformat();
	    }
	    
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {
	        
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        
	        try {
	            UserSession userSession = extractUserSession(request);
	            SystemConfig config = loadSystemConfiguration();
	            
	            if (isSaveOperation(request)) {
	                handleAssetCreation(request, out, userSession, config);
	            }
	            
	        } catch (Throwable e) {
	            LOGGER.log(Level.SEVERE, "Error in asset creation process", e);
	            sendErrorResponse(out, "An error occurred while processing your request. Please try again.");
	        }
	    }
	    
	    private boolean isSaveOperation(HttpServletRequest request) {
	        return request.getParameter("saveBtn") != null;
	    }
	    
	    private UserSession extractUserSession(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        UserSession userSession = new UserSession();
	        
	        userSession.setUserId((String) session.getAttribute("CurrentUser"));
	        userSession.setUserClass((String) session.getAttribute("UserClass"));
	        userSession.setSystemIp(request.getRemoteAddr());
	        
	        if (userSession.getUserId() != null) {
	            User user = securityHandler.getUserByUserID(userSession.getUserId());
	            userSession.setUserName(user.getUserName());
	            userSession.setBranch(user.getBranch());
	            userSession.setDeptCode(user.getDeptCode());
	            userSession.setBranchRestrict(user.getBranchRestrict());
	            userSession.setDeptRestrict(user.getDeptRestrict());
	            userSession.setBranchRestricted(securityHandler.getBranchRestrictedUser(userSession.getUserId()));
	            
	            // Get department ID from department code
	            String deptId = approvalRecords.getCodeName(
	                "SELECT dept_Id FROM am_ad_department WHERE dept_code = ?", 
	                userSession.getDeptCode()
	            );
	            userSession.setDeptId(deptId);
	        }
	        
	        return userSession;
	    }
	    
	    private SystemConfig loadSystemConfiguration() throws IOException {
	        Properties props = new Properties();
	        try (FileInputStream input = new FileInputStream(new File(PROPERTIES_PATH))) {
	            props.load(input);
	        }
	        
	        SystemConfig config = new SystemConfig();
	        config.setThirdPartyLabel(props.getProperty("ThirdPartyLabel"));
	        config.setSingleApproval(props.getProperty("singleApproval"));
	        config.setVatRequired(props.getProperty("VATREQUIRED"));
	        config.setSbuRequired(props.getProperty("SBUREQUIRED"));
	        config.setUploadFolder(props.getProperty("imagesUrl"));
	        
	        // Get company-level configurations
	        config.setSubjectToVat(approvalRecords.getCodeName("SELECT VAT_SELECTION FROM am_gb_company"));
	        config.setThirdPartyRequired(approvalRecords.getCodeName("SELECT THIRDPARTY_REQUIRE FROM am_gb_company"));
	        
	        LOGGER.info("ThirdPartyLabel: " + config.getThirdPartyLabel());
	        
	        return config;
	    }
	    
	    private void handleAssetCreation(HttpServletRequest request, PrintWriter out, 
	                                      UserSession userSession, SystemConfig config) 
	            throws Exception, Throwable {
	        
	        // Parse and validate asset data from request
	        AssetData assetData = parseAssetData(request, userSession);
	        
	        // Create asset record
	        AssetRecordsBean assetBean = createAssetBean(assetData, userSession, request);
	        
	        // Save to session for potential recovery
	        request.getSession().setAttribute("newAsset", assetBean);
	        
	        // Insert asset record
	        int status = assetBean.insertAssetRecordUnclassified(userSession.getBranch());
	        LOGGER.info("Asset insertion status: " + status);
	        
	        if (status == 0) {
	            handleSuccessfulCreation(request, out, assetBean, userSession, config, assetData);
	        } else {
	            handleCreationError(out, status);
	        }
	    }
	    
	    private AssetData parseAssetData(HttpServletRequest request, UserSession userSession) {
	        AssetData data = new AssetData();
	        
	        // Basic Information
	        data.setAssetId(request.getParameter("asset_id"));
	        data.setLegacyId(request.getParameter("integrifyId"));
	        data.setCategoryId(request.getParameter("category_id"));
	        data.setSubCategoryId(request.getParameter("sub_category_id"));
	        data.setDepartmentId(request.getParameter("department_id"));
	        data.setBranchId(request.getParameter("branch_id2"));
	        data.setSbuCode(request.getParameter("sbu_code"));
	        data.setRegistrationNo(request.getParameter("registration_no"));
	        
	        // Dates
	        data.setDateOfPurchase(request.getParameter("date_of_purchase"));
	        data.setPostingDate(request.getParameter("posting_date"));
	        data.setWarrantyStartDate(request.getParameter("warrantyStartDate"));
	        data.setExpiryDate(request.getParameter("expiryDate"));
	        data.setNoOfMonths(request.getParameter("noOfMonths"));
	        
	        // Financial Information
	        data.setCostPrice(request.getParameter("cost_price"));
	        data.setVatAmount(request.getParameter("vat_amount"));
	        data.setVatableCost(request.getParameter("vatable_cost"));
	        data.setWhTaxAmount(request.getParameter("wh_tax_amount"));
	        data.setResidualValue(request.getParameter("residual_value"));
	        data.setAmountPTD(request.getParameter("amountPTD"));
	        data.setTransportCost(request.getParameter("transport_cost"));
	        data.setOtherCost(request.getParameter("other_cost"));
	        data.setDepreciationRate(request.getParameter("depreciation_rate"));
	        data.setAccumDep(request.getParameter("accum_dep"));
	        data.setNbv(request.getParameter("nbv"));
	        
	        // Asset Details
	        data.setDescription(request.getParameter("description"));
	        data.setMemo(request.getParameter("memo"));
	        data.setRegion(request.getParameter("regionCode"));
	        data.setMake(request.getParameter("make"));
	        data.setModel(request.getParameter("model"));
	        data.setLocation(request.getParameter("location"));
	        data.setSerialNumber(request.getParameter("serial_number"));
	        data.setBarCode(request.getParameter("bar_code"));
	        data.setEngineNumber(request.getParameter("engine_number"));
	        data.setState(request.getParameter("state"));
	        data.setDriver(request.getParameter("driver"));
	        
	        // Vendor Information
	        data.setVendorAccount(request.getParameter("vendor_accountOld"));
	        data.setVendorName(request.getParameter("vendorName"));
	        data.setSuppliedBy(request.getParameter("sb"));
	        data.setLpo(request.getParameter("lpo"));
	        data.setInvoiceNo(request.getParameter("invNo"));
	        
	        // Personnel Information
	        data.setAssetUser(request.getParameter("user"));
	        data.setMaintainedBy(request.getParameter("maintained_by"));
	        data.setAuthorizedBy(request.getParameter("authorized_by"));
	        data.setPurchaseReason(request.getParameter("reason"));
	        data.setSupervisorId(request.getParameter("supervisor"));
	        
	        // Tax Information
	        data.setSubjectToVat(request.getParameter("subject_to_vatOLD"));
	        data.setWhTaxCb(request.getParameter("wh_tax_cb"));
	        
	        // Additional Fields
	        data.setFullyPAID(request.getParameter("fullyPAID"));
	        data.setProjectCode(request.getParameter("projectCode"));
	        data.setRequireDepreciation(request.getParameter("require_depreciation"));
	        data.setRequireRedistribution(request.getParameter("require_distribution"));
	        
	        // Email and Notification
	        data.setEmail1(request.getParameter("email_1"));
	        data.setEmail2(request.getParameter("email2"));
	        data.setWhoToRemind1(request.getParameter("who_to_remind"));
	        data.setWhoToRemind2(request.getParameter("who_to_rem2"));
	        
	        // Spare Fields
	        data.setSpare1(request.getParameter("spare_1"));
	        data.setSpare2(request.getParameter("spare_2"));
	        data.setSpare3(request.getParameter("spare_3"));
	        data.setSpare4(request.getParameter("spare_4"));
	        data.setSpare5(request.getParameter("spare_5"));
	        data.setSpare6(request.getParameter("spare_6"));
	        
	        // Multiple/Section
	        data.setMultiple(request.getParameter("multiple"));
	        data.setSectionId(request.getParameter("section_id"));
	        
	        // Asset Code
	        String assetCodeParam = request.getParameter("assetCode");
	        data.setAssetCode(assetCodeParam == null ? 0 : Integer.parseInt(assetCodeParam));
	        
	        return data;
	    }
	    
	    private AssetRecordsBean createAssetBean(AssetData data, UserSession userSession, HttpServletRequest request) throws Exception {
	        AssetRecordsBean bean = new AssetRecordsBean();
	        
	        HttpSession session = request.getSession();
	        if (session.getAttribute("newAsset") != null && "n".equals(request.getParameter("s"))) {
	            session.setAttribute("newAsset", null);
	        }
	        
	        if (session.getAttribute("newAsset") != null) {
	            bean = (AssetRecordsBean) session.getAttribute("newAsset");
	        }
	        
	        // Set all properties
	        bean.setAsset_id(data.getAssetId());
	        bean.setIntegrifyId(data.getLegacyId());
	        bean.setCategory_id(data.getCategoryId());
	        bean.setDepartment_id(data.getDepartmentId());
	        bean.setDate_of_purchase(data.getDateOfPurchase());
	        bean.setSub_category_id(data.getSubCategoryId());
	        bean.setSbu_code(data.getSbuCode());
	        bean.setBranch_id(data.getBranchId());
	        bean.setCost_price(data.getCostPrice());
	        bean.setVat_amount(data.getVatAmount());
	        bean.setVatable_cost(data.getVatableCost());
	        bean.setWh_tax_amount(data.getWhTaxAmount());
	        bean.setResidual_value(data.getResidualValue());
	        bean.setAmountPTD(data.getAmountPTD());
	        bean.setDescription(data.getDescription());
	        bean.setMemo(data.getMemo());
	        bean.setRegionCode(data.getRegion());
	        bean.setMake(data.getMake());
	        bean.setModel(data.getModel());
	        bean.setLocation(data.getLocation());
	        bean.setSerial_number(data.getSerialNumber());
	        bean.setBar_code(data.getBarCode());
	        bean.setEngine_number(data.getEngineNumber());
	        bean.setState(data.getState());
	        bean.setDriver(data.getDriver());
	        bean.setVendor_account(data.getVendorAccount());
	        bean.setVendorName(data.getVendorName());
	        bean.setMaintained_by(data.getMaintainedBy());
	        bean.setReason(data.getPurchaseReason());
	        bean.setUser(data.getAssetUser());
	        bean.setSupplied_by(data.getSuppliedBy());
	        bean.setLpo(data.getLpo());
	        bean.setAuthorized_by(data.getAuthorizedBy());
	        bean.setSubject_to_vat(data.getSubjectToVat());
	        bean.setWh_tax_cb(data.getWhTaxCb());
	        bean.setTransport_cost(data.getTransportCost());
	        bean.setOther_cost(data.getOtherCost());
	        bean.setDepreciation_rate(data.getDepreciationRate());
	        bean.setAccum_dep(data.getAccumDep());
	        bean.setNbv(data.getNbv());
	        bean.setEmail_1(data.getEmail1());
	        bean.setEmail2(data.getEmail2());
	        bean.setSpare_1(data.getSpare1());
	        bean.setSpare_2(data.getSpare2());
	        bean.setSpare_3(data.getSpare3());
	        bean.setSpare_4(data.getSpare4());
	        bean.setSpare_5(data.getSpare5());
	        bean.setSpare_6(data.getSpare6());
	        bean.setWarrantyStartDate(data.getWarrantyStartDate());
	        bean.setNoOfMonths(data.getNoOfMonths());
	        bean.setExpiryDate(data.getExpiryDate());
	        bean.setProjectCode(data.getProjectCode());
	        bean.setRequire_depreciation(data.getRequireDepreciation());
	        bean.setRequire_redistribution(data.getRequireRedistribution());
	        bean.setFullyPAID(data.getFullyPAID());
	        bean.setMultiple(data.getMultiple());
	        bean.setSection_id(data.getSectionId());
	        bean.setUser_id(userSession.getUserId());
	        
	        return bean;
	    }
	    
	    private void handleSuccessfulCreation(HttpServletRequest request, PrintWriter out,
	                                           AssetRecordsBean assetBean, UserSession userSession,
	                                           SystemConfig config, AssetData assetData) 
	            throws NumberFormatException, Exception {
	        
	        String assetId = assetBean.getAsset_id();
	        int assetCode = assetBean.getAssetCode();
	        
	        // Handle file upload
	        handleFileUpload(request, assetCode, config);
	        
	        // Save invoice information
	        String invoiceNumber = assetData.getSuppliedBy() + "-" + assetData.getInvoiceNo();
	        htmlUtil.insToAm_Invoice_No(assetId, assetData.getLpo(), invoiceNumber, "Asset Creation");
	        
	        // Get approvers list
	        ArrayList<User> approvers = assetBean.getApprovalsId(
	            userSession.getBranch(), 
	            userSession.getDeptCode(), 
	            userSession.getUserName()
	        );
	        
	        // Get transaction level configuration
	        int numOfTransactionLevel = assetBean.getNumOfTransactionLevel(TRANSACTION_LEVEL);
	        
	        if (numOfTransactionLevel == 0) {
	            handleNoApprovalRequired(out, assetBean, assetId, assetCode, userSession, config);
	        } else {
	            handleApprovalRequired(out, assetBean, assetId, assetCode, userSession, config, approvers, assetData);
	        }
	    }
	    
	    private void handleFileUpload(HttpServletRequest request, int assetCode, SystemConfig config) 
	            throws IOException, ServletException {
	        
	        Part filePart = request.getPart("file");
	        
	        // Handle case when no file is uploaded
	        if (filePart == null || filePart.getSize() == 0) {
	            LOGGER.info("No file uploaded or file is empty");
	            return;
	        }
	        
	        String fileName = filePart.getSubmittedFileName();
	        
	        // Handle case when filename is null or empty
	        if (fileName == null || fileName.trim().isEmpty()) {
	            LOGGER.warning("File submitted with no name");
	            return;
	        }
	        
	        LOGGER.info("Processing file: " + fileName);
	        
	        // Validate file type
	        if (isInvalidFile(fileName)) {
	            LOGGER.warning("Rejected file with invalid type: " + fileName);
	            return;
	        }
	        
	        String uploadPath = config.getUploadFolder();
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
	        
	        String extension = FilenameUtils.getExtension(fileName);
	        String newFileName = "W" + assetCode + "." + extension;
	        String filePath = uploadPath + newFileName;
	        
	        try {
	            filePart.write(filePath);
	            LOGGER.info("File uploaded successfully: " + filePath);
	        } catch (Exception e) {
	            LOGGER.log(Level.SEVERE, "Error uploading file: " + filePath, e);
	        }
	    }
	    
	    private boolean isInvalidFile(String fileName) {
	        if (fileName == null) return true;
	        
	        String lowerFileName = fileName.toLowerCase();
	        return lowerFileName.endsWith(".php") || 
	               lowerFileName.endsWith(".sql") || 
	               lowerFileName.endsWith(".jsp") ||
	               lowerFileName.endsWith(".exe") ||
	               lowerFileName.endsWith(".sh") ||
	               lowerFileName.endsWith(".bat");
	    }
	    
	    private void handleNoApprovalRequired(PrintWriter out, AssetRecordsBean assetBean,
	                                           String assetId, int assetCode,
	                                           UserSession userSession, SystemConfig config) throws NumberFormatException, Exception {
	        
	        if (assetBean.updateNewAssetStatuxUncapitalized(assetId)) {
	            // Set up pending transactions
	            assetBean.setPendingTrans(assetBean.setApprovalDataBranch(assetId), "1", assetCode);
	            String lastMTID = assetBean.getCurrentMtid("am_asset_approval");
	            assetBean.setPendingTransArchive(assetBean.setApprovalDataBranch(assetId), "1", 
	                                             Integer.parseInt(lastMTID), assetCode);
	            assetBean.updateNewApprovalAssetStatus(assetId, Integer.parseInt(userSession.getUserId()));
	            
	            // Handle raise entry if configured
	            handleRaiseEntry(assetBean, assetId, userSession, assetCode, lastMTID);
	            
	            // Send email notification
	            sendCreationEmail(assetId);
	            
	            // Redirect to upload page
	            redirectToUploadPage(out, assetId, assetCode);
	        }
	    }
	    
	    private void handleRaiseEntry(AssetRecordsBean assetBean, String assetId, 
	                                   UserSession userSession, int assetCode, String lastMTID) throws Exception {
	        
	        String assetRaiseEntry = approvalRecords.getCodeName("SELECT raise_entry FROM am_gb_company");
	        
	        if ("Y".equalsIgnoreCase(assetRaiseEntry)) {
	            String page1 = "ASSET CREATION RAISE ENTRY";
	            String url = "DocumentHelp.jsp?np=updateAssetBranch&amp;id=" + assetId + "&pageDirect=Y";
	            String userName = approvalRecords.getCodeName("SELECT full_name FROM am_gb_user WHERE user_id=?", 
	                                                          userSession.getUserId());
	            String branchName = approvalRecords.getCodeName("SELECT branch_name FROM am_ad_branch WHERE branch_id=?", 
	                                                            assetBean.getBranch_id());
	            
	            approvalRecords.insertApprovalx(assetId, assetBean.getDescription(), page1, "", 
	                                           "", userName, branchName,
	                                           assetBean.subjectToVat(assetId), assetBean.whTax(assetId),
	                                           url, Integer.parseInt(lastMTID), assetCode);
	        } else if ("N".equalsIgnoreCase(assetRaiseEntry)) {
	            activateAsset(assetId);
	        }
	        
	        approvalRecords.updateRaiseEntry(assetId);
	    }
	    
	    private void activateAsset(String assetId) throws Exception {
	    	AssetRecordsBean assetBean = new AssetRecordsBean();
	        String updateQuery = "UPDATE am_asset SET asset_status='ACTIVE' WHERE asset_id=?";
	        assetBean.updateAssetStatusChange(updateQuery, assetId);
	        assetBean.updateAssetStatusChange(updateQuery.replace("am_asset", "am_asset_archive"), assetId);
	        
	        String approvalUpdate = "UPDATE am_asset_approval SET process_status='A', asset_status='ACTIVE' WHERE asset_id=?";
	        assetBean.updateAssetStatusChange(approvalUpdate, assetId);
	        assetBean.updateAssetStatusChange(approvalUpdate.replace("am_asset_approval", "am_asset_approval_archive"), assetId);
	    }
	    
	    private void sendCreationEmail(String assetId) {
	        String[] mailSetup = companyHandler.getEmailStatusAndName("asset creation");
	        String status = mailSetup[0].trim();
	        String mailCode = mailSetup[1];
	        
	        if ("Approved".equalsIgnoreCase(status)) {
	            String to = new Codes().MailTo(mailCode, "Asset Creation");
	            String message = new Codes().MailMessage(mailCode, "Asset Creation");
	            message = "Uncapitalized Asset with ID: " + assetId + " was successfully created.";
	            
	            mailService.sendMail(to, "Uncapitalized Asset Creation", message);
	        }
	    }
	    
	    private void handleApprovalRequired(PrintWriter out, AssetRecordsBean assetBean,
	                                         String assetId, int assetCode,
	                                         UserSession userSession, SystemConfig config,
	                                         ArrayList<User> approvers, AssetData assetData) {
	        
	        if ("Y".equalsIgnoreCase(config.getSingleApproval())) {
	            handleSingleApproval(out, assetBean, assetId, assetCode, assetData.getSupervisorId());
	        } else if ("N".equalsIgnoreCase(config.getSingleApproval())) {
	            handleMultipleApproval(out, assetBean, assetId, assetCode, approvers);
	        }
	    }
	    
	    private void handleSingleApproval(PrintWriter out, AssetRecordsBean assetBean,
	                                       String assetId, int assetCode, String supervisorId) {
	        
	        assetBean.setPendingTrans(assetBean.setApprovalDataBranch(assetId), "1", assetCode);
	        String lastMTID = assetBean.getCurrentMtid("am_asset_approval");
	        assetBean.setPendingTransArchive(assetBean.setApprovalDataBranch(assetId), "1", 
	                                         Integer.parseInt(lastMTID), assetCode);
	        
	        // Prepare email
	        String subject = "Uncapitalized Asset Creation Approval";
	        String message = "Uncapitalized Asset with ID: " + assetId + " is waiting for your approval.";
	        
	        String supervisorEmail = approvalRecords.getCodeName(
	            "SELECT email FROM am_gb_user WHERE user_id=?", supervisorId);
	        
	        companyHandler.insertMailRecords(supervisorEmail, subject, message);
	        
	        // Send response
	        out.print("<script>");
	        out.print("alert('Uncapitalized Asset creation submitted for approval');");
	        out.print("window.location='" + CURRENT_PAGE + "&id=" + assetId + 
	                  "&tran_status=A&tran_type=" + TRANSACTION_TYPE + 
	                  "&assetId=" + assetId + "&reason=&pPage=" + CURRENT_PAGE + "';");
	        out.print("</script>");
	    }
	    
	    private void handleMultipleApproval(PrintWriter out, AssetRecordsBean assetBean,
	                                         String assetId, int assetCode, ArrayList<User> approvers) {
	        
	        String mtid = appHelper.getGeneratedId("am_asset_approval");
	        
	        for (User approver : approvers) {
	            String supervisorId = approver.getUserId();
	            String email = approver.getEmail();
	            
	            assetBean.setPendingTransMultiApp(assetBean.setApprovalDataBranch(assetId), "1", 
	                                              assetCode, supervisorId, mtid);
	            
	            String subject = "Uncapitalized Asset Creation Approval";
	            String message = "Uncapitalized Asset with ID: " + assetId + " is waiting for your approval.";
	            companyHandler.insertMailRecords(email, subject, message);
	        }
	        
	        out.print("<script>");
	        out.print("alert('Uncapitalized Asset creation submitted for approval');");
	        out.print("window.location='" + CURRENT_PAGE + "&id=" + assetId + 
	                  "&tran_status=A&tran_type=" + TRANSACTION_TYPE + 
	                  "&assetId=" + assetId + "&reason=&pPage=" + CURRENT_PAGE + "';");
	        out.print("</script>");
	    }
	    
	    private void handleCreationError(PrintWriter out, int status) {
	        if (status == 1 || status == 2) {
	            sendErrorResponse(out, "Addition of Asset will overshoot quarterly budget for this category. " +
	                              "Please seek supplementary budgetary allocation.");
	        } else {
	            sendErrorResponse(out, "Records cannot be saved! Your Asset does not fall in any budget quarter.");
	        }
	    }
	    
	    private void sendErrorResponse(PrintWriter out, String message) {
	        out.print("<script>");
	        out.print("alert('" + message + "');");
	        out.print("window.location='DocumentHelp.jsp?np=newUncapAsset&id';");
	        out.print("</script>");
	    }
	    
	    private void redirectToUploadPage(PrintWriter out, String assetId, int assetCode) {
	        out.print("<script>");
	        out.print("alert('Uncapitalized Asset creation successful');");
	        out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId=" + assetId + 
	                  "&previousPage=" + CURRENT_PAGE + "&tran_type=" + TRANSACTION_TYPE + 
	                  "&root=" + ROOT + "&assetCode=" + assetCode + "';");
	        out.print("</script>");
	    }
	    
	    // Inner classes for data structures
	    private static class UserSession {
	        private String userId;
	        private String userClass;
	        private String userName;
	        private String branch;
	        private String deptCode;
	        private String deptId;
	        private String branchRestrict;
	        private String deptRestrict;
	        private String branchRestricted;
	        private String systemIp;
	        
	        // Getters and setters (generate these)
	        public String getUserId() { return userId; }
	        public void setUserId(String userId) { this.userId = userId; }
	        public String getUserClass() { return userClass; }
	        public void setUserClass(String userClass) { this.userClass = userClass; }
	        public String getUserName() { return userName; }
	        public void setUserName(String userName) { this.userName = userName; }
	        public String getBranch() { return branch; }
	        public void setBranch(String branch) { this.branch = branch; }
	        public String getDeptCode() { return deptCode; }
	        public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
	        public String getDeptId() { return deptId; }
	        public void setDeptId(String deptId) { this.deptId = deptId; }
	        public String getBranchRestrict() { return branchRestrict; }
	        public void setBranchRestrict(String branchRestrict) { this.branchRestrict = branchRestrict; }
	        public String getDeptRestrict() { return deptRestrict; }
	        public void setDeptRestrict(String deptRestrict) { this.deptRestrict = deptRestrict; }
	        public String getBranchRestricted() { return branchRestricted; }
	        public void setBranchRestricted(String branchRestricted) { this.branchRestricted = branchRestricted; }
	        public String getSystemIp() { return systemIp; }
	        public void setSystemIp(String systemIp) { this.systemIp = systemIp; }
	    }
	    
	    private static class SystemConfig {
	        private String thirdPartyLabel;
	        private String singleApproval;
	        private String vatRequired;
	        private String sbuRequired;
	        private String uploadFolder;
	        private String subjectToVat;
	        private String thirdPartyRequired;
	        
	        // Getters and setters (generate these)
	        public String getThirdPartyLabel() { return thirdPartyLabel; }
	        public void setThirdPartyLabel(String thirdPartyLabel) { this.thirdPartyLabel = thirdPartyLabel; }
	        public String getSingleApproval() { return singleApproval; }
	        public void setSingleApproval(String singleApproval) { this.singleApproval = singleApproval; }
	        public String getVatRequired() { return vatRequired; }
	        public void setVatRequired(String vatRequired) { this.vatRequired = vatRequired; }
	        public String getSbuRequired() { return sbuRequired; }
	        public void setSbuRequired(String sbuRequired) { this.sbuRequired = sbuRequired; }
	        public String getUploadFolder() { return uploadFolder; }
	        public void setUploadFolder(String uploadFolder) { this.uploadFolder = uploadFolder; }
	        public String getSubjectToVat() { return subjectToVat; }
	        public void setSubjectToVat(String subjectToVat) { this.subjectToVat = subjectToVat; }
	        public String getThirdPartyRequired() { return thirdPartyRequired; }
	        public void setThirdPartyRequired(String thirdPartyRequired) { this.thirdPartyRequired = thirdPartyRequired; }
	    }
	    
	    private static class AssetData {
	        // Basic Information
	        private String assetId;
	        private String legacyId;
	        private String categoryId;
	        private String subCategoryId;
	        private String departmentId;
	        private String branchId;
	        private String sbuCode;
	        private String registrationNo;
	        
	        // Dates
	        private String dateOfPurchase;
	        private String postingDate;
	        private String warrantyStartDate;
	        private String expiryDate;
	        private String noOfMonths;
	        
	        // Financial Information
	        private String costPrice;
	        private String vatAmount;
	        private String vatableCost;
	        private String whTaxAmount;
	        private String residualValue;
	        private String amountPTD;
	        private String transportCost;
	        private String otherCost;
	        private String depreciationRate;
	        private String accumDep;
	        private String nbv;
	        
	        // Asset Details
	        private String description;
	        private String memo;
	        private String region;
	        private String make;
	        private String model;
	        private String location;
	        private String serialNumber;
	        private String barCode;
	        private String engineNumber;
	        private String state;
	        private String driver;
	        
	        // Vendor Information
	        private String vendorAccount;
	        private String vendorName;
	        private String suppliedBy;
	        private String lpo;
	        private String invoiceNo;
	        
	        // Personnel Information
	        private String assetUser;
	        private String maintainedBy;
	        private String authorizedBy;
	        private String purchaseReason;
	        private String supervisorId;
	        
	        // Tax Information
	        private String subjectToVat;
	        private String whTaxCb;
	        
	        // Additional Fields
	        private String fullyPAID;
	        private String projectCode;
	        private String requireDepreciation;
	        private String requireRedistribution;
	        
	        // Email and Notification
	        private String email1;
	        private String email2;
	        private String whoToRemind1;
	        private String whoToRemind2;
	        
	        // Spare Fields
	        private String spare1;
	        private String spare2;
	        private String spare3;
	        private String spare4;
	        private String spare5;
	        private String spare6;
	        
	        // Multiple/Section
	        private String multiple;
	        private String sectionId;
	        
	        // Other
	        private int assetCode;

			public String getAssetId() {
				return assetId;
			}

			public void setAssetId(String assetId) {
				this.assetId = assetId;
			}

			public String getLegacyId() {
				return legacyId;
			}

			public void setLegacyId(String legacyId) {
				this.legacyId = legacyId;
			}

			public String getCategoryId() {
				return categoryId;
			}

			public void setCategoryId(String categoryId) {
				this.categoryId = categoryId;
			}

			public String getSubCategoryId() {
				return subCategoryId;
			}

			public void setSubCategoryId(String subCategoryId) {
				this.subCategoryId = subCategoryId;
			}

			public String getDepartmentId() {
				return departmentId;
			}

			public void setDepartmentId(String departmentId) {
				this.departmentId = departmentId;
			}

			public String getBranchId() {
				return branchId;
			}

			public void setBranchId(String branchId) {
				this.branchId = branchId;
			}

			public String getSbuCode() {
				return sbuCode;
			}

			public void setSbuCode(String sbuCode) {
				this.sbuCode = sbuCode;
			}

			public String getRegistrationNo() {
				return registrationNo;
			}

			public void setRegistrationNo(String registrationNo) {
				this.registrationNo = registrationNo;
			}

			public String getDateOfPurchase() {
				return dateOfPurchase;
			}

			public void setDateOfPurchase(String dateOfPurchase) {
				this.dateOfPurchase = dateOfPurchase;
			}

			public String getPostingDate() {
				return postingDate;
			}

			public void setPostingDate(String postingDate) {
				this.postingDate = postingDate;
			}

			public String getWarrantyStartDate() {
				return warrantyStartDate;
			}

			public void setWarrantyStartDate(String warrantyStartDate) {
				this.warrantyStartDate = warrantyStartDate;
			}

			public String getExpiryDate() {
				return expiryDate;
			}

			public void setExpiryDate(String expiryDate) {
				this.expiryDate = expiryDate;
			}

			public String getNoOfMonths() {
				return noOfMonths;
			}

			public void setNoOfMonths(String noOfMonths) {
				this.noOfMonths = noOfMonths;
			}

			public String getCostPrice() {
				return costPrice;
			}

			public void setCostPrice(String costPrice) {
				this.costPrice = costPrice;
			}

			public String getVatAmount() {
				return vatAmount;
			}

			public void setVatAmount(String vatAmount) {
				this.vatAmount = vatAmount;
			}

			public String getVatableCost() {
				return vatableCost;
			}

			public void setVatableCost(String vatableCost) {
				this.vatableCost = vatableCost;
			}

			public String getWhTaxAmount() {
				return whTaxAmount;
			}

			public void setWhTaxAmount(String whTaxAmount) {
				this.whTaxAmount = whTaxAmount;
			}

			public String getResidualValue() {
				return residualValue;
			}

			public void setResidualValue(String residualValue) {
				this.residualValue = residualValue;
			}

			public String getAmountPTD() {
				return amountPTD;
			}

			public void setAmountPTD(String amountPTD) {
				this.amountPTD = amountPTD;
			}

			public String getTransportCost() {
				return transportCost;
			}

			public void setTransportCost(String transportCost) {
				this.transportCost = transportCost;
			}

			public String getOtherCost() {
				return otherCost;
			}

			public void setOtherCost(String otherCost) {
				this.otherCost = otherCost;
			}

			public String getDepreciationRate() {
				return depreciationRate;
			}

			public void setDepreciationRate(String depreciationRate) {
				this.depreciationRate = depreciationRate;
			}

			public String getAccumDep() {
				return accumDep;
			}

			public void setAccumDep(String accumDep) {
				this.accumDep = accumDep;
			}

			public String getNbv() {
				return nbv;
			}

			public void setNbv(String nbv) {
				this.nbv = nbv;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getMemo() {
				return memo;
			}

			public void setMemo(String memo) {
				this.memo = memo;
			}

			public String getRegion() {
				return region;
			}

			public void setRegion(String region) {
				this.region = region;
			}

			public String getMake() {
				return make;
			}

			public void setMake(String make) {
				this.make = make;
			}

			public String getModel() {
				return model;
			}

			public void setModel(String model) {
				this.model = model;
			}

			public String getLocation() {
				return location;
			}

			public void setLocation(String location) {
				this.location = location;
			}

			public String getSerialNumber() {
				return serialNumber;
			}

			public void setSerialNumber(String serialNumber) {
				this.serialNumber = serialNumber;
			}

			public String getBarCode() {
				return barCode;
			}

			public void setBarCode(String barCode) {
				this.barCode = barCode;
			}

			public String getEngineNumber() {
				return engineNumber;
			}

			public void setEngineNumber(String engineNumber) {
				this.engineNumber = engineNumber;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}

			public String getDriver() {
				return driver;
			}

			public void setDriver(String driver) {
				this.driver = driver;
			}

			public String getVendorAccount() {
				return vendorAccount;
			}

			public void setVendorAccount(String vendorAccount) {
				this.vendorAccount = vendorAccount;
			}

			public String getVendorName() {
				return vendorName;
			}

			public void setVendorName(String vendorName) {
				this.vendorName = vendorName;
			}

			public String getSuppliedBy() {
				return suppliedBy;
			}

			public void setSuppliedBy(String suppliedBy) {
				this.suppliedBy = suppliedBy;
			}

			public String getLpo() {
				return lpo;
			}

			public void setLpo(String lpo) {
				this.lpo = lpo;
			}

			public String getInvoiceNo() {
				return invoiceNo;
			}

			public void setInvoiceNo(String invoiceNo) {
				this.invoiceNo = invoiceNo;
			}

			public String getAssetUser() {
				return assetUser;
			}

			public void setAssetUser(String assetUser) {
				this.assetUser = assetUser;
			}

			public String getMaintainedBy() {
				return maintainedBy;
			}

			public void setMaintainedBy(String maintainedBy) {
				this.maintainedBy = maintainedBy;
			}

			public String getAuthorizedBy() {
				return authorizedBy;
			}

			public void setAuthorizedBy(String authorizedBy) {
				this.authorizedBy = authorizedBy;
			}

			public String getPurchaseReason() {
				return purchaseReason;
			}

			public void setPurchaseReason(String purchaseReason) {
				this.purchaseReason = purchaseReason;
			}

			public String getSupervisorId() {
				return supervisorId;
			}

			public void setSupervisorId(String supervisorId) {
				this.supervisorId = supervisorId;
			}

			public String getSubjectToVat() {
				return subjectToVat;
			}

			public void setSubjectToVat(String subjectToVat) {
				this.subjectToVat = subjectToVat;
			}

			public String getWhTaxCb() {
				return whTaxCb;
			}

			public void setWhTaxCb(String whTaxCb) {
				this.whTaxCb = whTaxCb;
			}

			public String getFullyPAID() {
				return fullyPAID;
			}

			public void setFullyPAID(String fullyPAID) {
				this.fullyPAID = fullyPAID;
			}

			public String getProjectCode() {
				return projectCode;
			}

			public void setProjectCode(String projectCode) {
				this.projectCode = projectCode;
			}

			public String getRequireDepreciation() {
				return requireDepreciation;
			}

			public void setRequireDepreciation(String requireDepreciation) {
				this.requireDepreciation = requireDepreciation;
			}

			public String getRequireRedistribution() {
				return requireRedistribution;
			}

			public void setRequireRedistribution(String requireRedistribution) {
				this.requireRedistribution = requireRedistribution;
			}

			public String getEmail1() {
				return email1;
			}

			public void setEmail1(String email1) {
				this.email1 = email1;
			}

			public String getEmail2() {
				return email2;
			}

			public void setEmail2(String email2) {
				this.email2 = email2;
			}

			public String getWhoToRemind1() {
				return whoToRemind1;
			}

			public void setWhoToRemind1(String whoToRemind1) {
				this.whoToRemind1 = whoToRemind1;
			}

			public String getWhoToRemind2() {
				return whoToRemind2;
			}

			public void setWhoToRemind2(String whoToRemind2) {
				this.whoToRemind2 = whoToRemind2;
			}

			public String getSpare1() {
				return spare1;
			}

			public void setSpare1(String spare1) {
				this.spare1 = spare1;
			}

			public String getSpare2() {
				return spare2;
			}

			public void setSpare2(String spare2) {
				this.spare2 = spare2;
			}

			public String getSpare3() {
				return spare3;
			}

			public void setSpare3(String spare3) {
				this.spare3 = spare3;
			}

			public String getSpare4() {
				return spare4;
			}

			public void setSpare4(String spare4) {
				this.spare4 = spare4;
			}

			public String getSpare5() {
				return spare5;
			}

			public void setSpare5(String spare5) {
				this.spare5 = spare5;
			}

			public String getSpare6() {
				return spare6;
			}

			public void setSpare6(String spare6) {
				this.spare6 = spare6;
			}

			public String getMultiple() {
				return multiple;
			}

			public void setMultiple(String multiple) {
				this.multiple = multiple;
			}

			public String getSectionId() {
				return sectionId;
			}

			public void setSectionId(String sectionId) {
				this.sectionId = sectionId;
			}

			public int getAssetCode() {
				return assetCode;
			}

			public void setAssetCode(int assetCode) {
				this.assetCode = assetCode;
			}
	        
	       
	    }
	
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}