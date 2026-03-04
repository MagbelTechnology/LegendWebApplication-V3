package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
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
import magma.GroupAssetBean;
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
	public class UncapGroupAssetServlet extends HttpServlet {
	    
	    private static final Logger LOGGER = Logger.getLogger(UncapGroupAssetServlet.class.getName());
	    private static final String PROPERTIES_PATH = "C:\\Property\\LegendPlus.properties";
	    private static final String CURRENT_PAGE = "groupAssetBranch";
	    private static final String DESTINATION_PAGE = "DocumentHelp.jsp?np=groupAssetUpdate";
	    private static final String TRANSACTION_LEVEL = "19";
	    
	    // Service layer dependencies
	    private EmailSmsServiceBus mailService;
	    private ApprovalRecords approvalRecords;
	    private SecurityHandler securityHandler;
	    private HtmlUtility htmlUtil;
	    private ApplicationHelper appHelper;
	    private CompanyHandler companyHandler;
	    private CurrencyNumberformat currencyFormatter;
	    private AssetRecordsBean assetBean;
	    private GroupAssetBean groupAssetBean;
	    
	    public UncapGroupAssetServlet() throws Exception {
	        this.mailService = new EmailSmsServiceBus();
	        this.approvalRecords = new ApprovalRecords();
	        this.securityHandler = new SecurityHandler();
	        this.htmlUtil = new HtmlUtility();
	        this.appHelper = new ApplicationHelper();
	        this.companyHandler = new CompanyHandler();
	        this.currencyFormatter = new CurrencyNumberformat();
	        this.assetBean = new AssetRecordsBean();
	        this.groupAssetBean = new GroupAssetBean();
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
	                handleGroupAssetCreation(request, out, userSession, config);
	            }
	            
	        } catch (Throwable e) {
	            LOGGER.log(Level.SEVERE, "Error in group asset creation process", e);
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
	            
	            // Get workstation info
	            try {
	                InetAddress address = InetAddress.getByName(userSession.getSystemIp());
	                userSession.setWorkstationName(address.getHostName());
	            } catch (Exception e) {
	                LOGGER.warning("Could not resolve workstation name for IP: " + userSession.getSystemIp());
	            }
	            
	            // Get branch restriction info
	            String branchRestrict = approvalRecords.getCodeName(
	                "SELECT branch_restriction FROM am_gb_user WHERE user_id=?", 
	                userSession.getUserId()
	            );
	            userSession.setBranchRestricted(branchRestrict == null ? "Y" : branchRestrict.trim());
	            
	            String branchRestrictCode = approvalRecords.getCodeName(
	                "SELECT branch FROM am_gb_user WHERE user_id=?", 
	                userSession.getUserId()
	            );
	            userSession.setBranchRestrictCode(branchRestrictCode == null ? "0" : branchRestrictCode.trim());
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
	        config.setGroupAssetByAsset(props.getProperty("groupAssetByAsset"));
	        config.setSingleApproval(props.getProperty("singleApproval"));
	        config.setVatRequired(props.getProperty("VATREQUIRED"));
	        config.setSbuRequired(props.getProperty("SBUREQUIRED"));
	        config.setUploadFolder(props.getProperty("imagesUrl"));
	        
	        // Get company-level configurations
	        config.setSubjectToVat(approvalRecords.getCodeName("SELECT VAT_SELECTION FROM am_gb_company"));
	        config.setThirdPartyRequired(approvalRecords.getCodeName("SELECT THIRDPARTY_REQUIRE FROM am_gb_company"));
	        config.setCostThreshold(approvalRecords.getCodeName("SELECT cost_threshold FROM am_gb_company"));
	        
	        LOGGER.info("ThirdPartyLabel: " + config.getThirdPartyLabel());
	        LOGGER.info("groupAssetByAsset: " + config.getGroupAssetByAsset());
	        
	        return config;
	    }
	    
	    private void handleGroupAssetCreation(HttpServletRequest request, PrintWriter out,
	                                           UserSession userSession, SystemConfig config) 
	            throws Throwable {
	        
	        try {
	            // Parse asset data from request
	            GroupAssetData assetData = parseGroupAssetData(request, userSession);
	            
	            // Populate group asset bean
	            populateGroupAssetBean(assetData, userSession);
	            
	            // Insert group asset record
	            long[] result = groupAssetBean.insertGroupAssetRecordUnclassified(
	                config.getGroupAssetByAsset(),
	                config.getSingleApproval(),
	                userSession.getBranch(),
	                userSession.getDeptCode(),
	                userSession.getUserName()
	            );
	            
	            LOGGER.info("Group asset insertion result - status: " + result[0] + ", ID: " + result[1]);
	            
	            // Handle file upload
	            handleFileUpload(request, result[1], config);
	            
	            if (result[0] == 0) {
	                handleSuccessfulCreation(request, out, result[1], userSession, config, assetData);
	            } else if (result[0] == 1 || result[0] == 2) {
	                handleBudgetExceededError(out, result[0]);
	            } else {
	                handleGeneralError(out);
	            }
	            
	        } catch (Exception e) {
	            LOGGER.log(Level.SEVERE, "Error creating group asset", e);
	            out.print("<script>alert('Unable to save. Error encountered');history.go(-1);</script>");
	        }
	    }
	    
	    private GroupAssetData parseGroupAssetData(HttpServletRequest request, UserSession userSession) {
	        GroupAssetData data = new GroupAssetData();
	        
	        // Basic Information
	        data.setAssetId(request.getParameter("asset_id"));
	        data.setGroupId(request.getParameter("gid"));
	        data.setCategoryId(request.getParameter("category_id"));
	        data.setSubCategoryId(request.getParameter("sub_category_id"));
	        data.setDepartmentId(request.getParameter("department_id"));
	        data.setBranchId(request.getParameter("branch_id"));
	        data.setSbuCode(request.getParameter("sbu_code"));
	        data.setRegistrationNo(request.getParameter("registration_no"));
	        
	        // Dates
	        data.setDateOfPurchase(request.getParameter("date_of_purchase"));
	        data.setPostingDate(request.getParameter("posting_date"));
	        data.setWarrantyStartDate(request.getParameter("warrantyStartDate"));
	        data.setExpiryDate(request.getParameter("expiryDate"));
	        data.setNoOfMonths(request.getParameter("noOfMonths"));
	        data.setDepreciationStartDate(request.getParameter("depreciation_start_date"));
	        data.setDepreciationEndDate(request.getParameter("depreciation_end_date"));
	        
	        // Financial Information
	        data.setCostPrice(request.getParameter("cost_price"));
	        data.setVatAmount(request.getParameter("vat_amount"));
	        data.setVatableCost(request.getParameter("vatable_cost"));
	        data.setWhTaxAmount(request.getParameter("wh_tax_amount"));
	        
	        String residualValue = request.getParameter("residual_value");
	        data.setResidualValue(residualValue == null || residualValue.isEmpty() ? "0" : residualValue);
	        
	        data.setAmountPTD(request.getParameter("amountPTD"));
	        data.setTransportCost(request.getParameter("transport_cost"));
	        data.setOtherCost(request.getParameter("other_cost"));
	        data.setDepreciationRate(request.getParameter("depreciation_rate"));
	        data.setAccumDep(request.getParameter("accum_dep"));
	        data.setNbv(request.getParameter("nbv"));
	        
	        // Asset Details
	        data.setDescription(request.getParameter("description"));
	        data.setMemo(request.getParameter("memo"));
	        data.setRegion(request.getParameter("region"));
	        data.setMake(request.getParameter("make"));
	        data.setModel(request.getParameter("model"));
	        data.setLocation(request.getParameter("location"));
	        data.setSerialNumber(request.getParameter("serial_number"));
	        data.setBarCode(request.getParameter("bar_code"));
	        data.setEngineNumber(request.getParameter("engine_number"));
	        data.setState(request.getParameter("state"));
	        data.setDriver(request.getParameter("driver"));
	        data.setSectionId(request.getParameter("section_id"));
	        
	        // Vendor Information
	        data.setVendorAccount(request.getParameter("vendor_accountOld"));
	        data.setVendorName(request.getParameter("vendorName"));
	        data.setSuppliedBy(request.getParameter("sb"));
	        data.setLpo(request.getParameter("lpo"));
	        data.setInvoiceNo(request.getParameter("invNo"));
	        data.setInvoiceNum(request.getParameter("invoiceNum"));
	        
	        // Personnel Information
	        data.setAssetUser(request.getParameter("user"));
	        data.setMaintainedBy(request.getParameter("maintained_by"));
	        data.setAuthorizedBy(request.getParameter("authorized_by"));
	        data.setPurchaseReason(request.getParameter("reason"));
	        
	        // Tax Information
	        data.setSubjectToVat(request.getParameter("subject_to_vatOld"));
	        data.setWhTaxCb(request.getParameter("wh_tax_cb"));
	        
	        // Additional Fields
	        data.setFullyPAID(request.getParameter("fullyPAID"));
	        data.setProjectCode(request.getParameter("projectCode"));
	        data.setRequireDepreciation(request.getParameter("require_depreciation"));
	        
	        String requireRedistribution = request.getParameter("require_redistribution");
	        data.setRequireRedistribution(requireRedistribution == null ? "" : requireRedistribution);
	        
	        String sbuCode = request.getParameter("sbu_code");
	        data.setSbuCode(sbuCode == null ? "" : sbuCode);
	        
	        String projectCode = request.getParameter("projectCode");
	        data.setProjectCode(projectCode == null ? "" : projectCode);
	        
	        // Email and Notification
	        data.setEmail1(request.getParameter("email_1"));
	        data.setEmail2(request.getParameter("email2"));
	        data.setWhoToRemind1(request.getParameter("who_to_rem"));
	        data.setWhoToRemind2(request.getParameter("who_to_rem2"));
	        
	        // Spare Fields
	        data.setSpare1(request.getParameter("spare_1"));
	        data.setSpare2(request.getParameter("spare_2"));
	        data.setSpare3(request.getParameter("spare_3"));
	        data.setSpare4(request.getParameter("spare_4"));
	        data.setSpare5(request.getParameter("spare_5"));
	        data.setSpare6(request.getParameter("spare_6"));
	        
	        // Group specific
	        data.setNoOfItems(request.getParameter("no_of_items"));
	        data.setThreshold(request.getParameter("threshold"));
	        data.setLpoSet(request.getParameter("lposet"));
	        data.setMultiple(request.getParameter("multiple"));
	        data.setPartPay(request.getParameter("partPay"));
	        data.setIntegrifyId(request.getParameter("integrifyId"));
	        
	        return data;
	    }
	    
	    private void populateGroupAssetBean(GroupAssetData data, UserSession userSession) {
	        groupAssetBean.setNo_of_items(data.getNoOfItems());
	        groupAssetBean.setDepreciation_start_date(data.getDepreciationStartDate());
	        groupAssetBean.setDepreciation_end_date(data.getDepreciationEndDate());
	        groupAssetBean.setDepartment_id(data.getDepartmentId());
	        groupAssetBean.setDate_of_purchase(data.getDateOfPurchase());
	        groupAssetBean.setSub_category_id(data.getSubCategoryId());
	        groupAssetBean.setSbu_code(data.getSbuCode());
	        groupAssetBean.setBranch_id(data.getBranchId());
	        groupAssetBean.setCategory_id(data.getCategoryId());
	        groupAssetBean.setCost_price(data.getCostPrice());
	        groupAssetBean.setVat_amount(data.getVatAmount());
	        groupAssetBean.setVatable_cost(data.getVatableCost());
	        groupAssetBean.setWh_tax_amount(data.getWhTaxAmount());
	        groupAssetBean.setResidual_value(data.getResidualValue());
	        groupAssetBean.setAmountPTD(data.getAmountPTD());
	        groupAssetBean.setDescription(data.getDescription());
	        groupAssetBean.setVendorName(data.getVendorName());
	        groupAssetBean.setMake(data.getMake());
	        groupAssetBean.setLocation(data.getLocation());
	        groupAssetBean.setSection(data.getSectionId());
	        groupAssetBean.setSection_id(data.getSectionId());
	        groupAssetBean.setState(data.getState());
	        groupAssetBean.setDriver(data.getDriver());
	        groupAssetBean.setBar_code(data.getBarCode());
	        groupAssetBean.setVendor_account(data.getVendorAccount());
	        groupAssetBean.setMaintained_by(data.getMaintainedBy());
	        groupAssetBean.setReason(data.getPurchaseReason());
	        groupAssetBean.setUser(data.getAssetUser());
	        groupAssetBean.setSupplied_by(data.getSuppliedBy());
	        groupAssetBean.setSbu_code(data.getSbuCode());
	        groupAssetBean.setLpo(data.getLpo());
	        groupAssetBean.setAuthorized_by(data.getAuthorizedBy());
	        groupAssetBean.setSubject_to_vat(data.getSubjectToVat());
	        groupAssetBean.setWh_tax_cb(data.getWhTaxCb());
	        groupAssetBean.setTransport_cost(data.getTransportCost());
	        groupAssetBean.setOther_cost(data.getOtherCost());
	        groupAssetBean.setDepreciation_rate(data.getDepreciationRate());
	        groupAssetBean.setAccum_dep(data.getAccumDep());
	        groupAssetBean.setSupplied_by(data.getSuppliedBy());
	        groupAssetBean.setWho_to_rem(data.getWhoToRemind1());
	        groupAssetBean.setInvoiceNum(data.getInvoiceNum());
	        groupAssetBean.setEmail_1(data.getEmail1());
	        groupAssetBean.setWho_to_rem_2(data.getWhoToRemind2());
	        groupAssetBean.setPosting_date(data.getPostingDate());
	        groupAssetBean.setEmail2(data.getEmail2());
	        groupAssetBean.setSpare_1(data.getSpare1());
	        groupAssetBean.setSpare_2(data.getSpare2());
	        groupAssetBean.setSpare_3(data.getSpare3());
	        groupAssetBean.setSpare_4(data.getSpare4());
	        groupAssetBean.setSpare_5(data.getSpare5());
	        groupAssetBean.setSpare_6(data.getSpare6());
	        groupAssetBean.setWarrantyStartDate(data.getWarrantyStartDate());
	        groupAssetBean.setNoOfMonths(data.getNoOfMonths());
	        groupAssetBean.setExpiryDate(data.getExpiryDate());
	        groupAssetBean.setProjectCode(data.getProjectCode());
	        groupAssetBean.setRequire_depreciation(data.getRequireDepreciation());
	        groupAssetBean.setRequire_redistribution(data.getRequireRedistribution());
	        groupAssetBean.setUser_id(userSession.getUserId());
	        groupAssetBean.setFullyPAID(data.getFullyPAID());
	        groupAssetBean.setPartPAY(data.getPartPay());
	        groupAssetBean.setRegistration_no(data.getRegistrationNo());
	        groupAssetBean.setSerial_number(data.getSerialNumber());
	        groupAssetBean.setIntegrifyId(data.getIntegrifyId());
	        groupAssetBean.setEngine_number(data.getEngineNumber());
	        groupAssetBean.setModel(data.getModel());
	    }
	    
	    private void handleFileUpload(HttpServletRequest request, long assetCode, SystemConfig config) 
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
	            // Process all parts
	            for (Part part : request.getParts()) {
	                if (part.getName().equals("file")) {
	                    part.write(filePath);
	                    LOGGER.info("File uploaded successfully: " + filePath);
	                    break;
	                }
	            }
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
	    
	    private void handleSuccessfulCreation(HttpServletRequest request, PrintWriter out,
	                                           long assetId, UserSession userSession,
	                                           SystemConfig config, GroupAssetData assetData) {
	        
	        String groupId = groupAssetBean.getAsset_id();
	        long groupUncapId = groupAssetBean.getGroupUncapID(groupId);
	        String groupIdStr = Long.toString(groupUncapId);
	        
	        LOGGER.info("Group ID: " + groupIdStr);
	        
	        // Save invoice information
	        String invoiceNumber = assetData.getSuppliedBy() + "-" + assetData.getInvoiceNo();
	        htmlUtil.insToAm_Invoice_No(groupIdStr, assetData.getLpo(), invoiceNumber, 
	                                    "Group Uncapitalised Asset Creation");
	        
	        // Get transaction level
	        int numOfTransactionLevel = assetBean.getNumOfTransactionLevel(TRANSACTION_LEVEL);
	        
	        if (numOfTransactionLevel == 0) {
	            handleNoApprovalRequired(out, groupIdStr, assetId, userSession, config, assetData);
	        } else {
	            handleApprovalRequired(out, groupIdStr, assetId, userSession, config);
	        }
	    }
	    
	    private void handleNoApprovalRequired(PrintWriter out, String groupId, long assetId,
	                                           UserSession userSession, SystemConfig config,
	                                           GroupAssetData assetData) {
	        
	        // Prepare raise entry data
	        String page1 = "Group ASSET CREATION RAISE ENTRY";
	        String url = "DocumentHelp.jsp?np=groupAssetUpdateBranch&id=" + groupId + "&pageDirect=Y";
	        String userName = approvalRecords.getCodeName("SELECT full_name FROM am_gb_user WHERE user_id=?", 
	                                                      userSession.getUserId());
	        String branchName = approvalRecords.getCodeName("SELECT branch_name FROM am_ad_branch WHERE branch_id=?", 
	                                                        assetData.getBranchId());
	        String description = groupAssetBean.getDescription();
	        String subjectT = groupAssetBean.subjectToVat(groupId);
	        String whT = groupAssetBean.whTax(groupId);
	        
	        // Insert raise entry (commented in original, keeping same)
	        // approvalRecords.insertApproval(groupId, description, page1, "", assetData.getPartPay(), 
	        //                                userName, branchName, subjectT, whT, url);
	        // groupAssetBean.updateGroupAssetRaiseEntry(groupId);
	        
	        // Send success response
	        out.print("<script>");
	        out.print("alert('Group Uncapitalized Asset creation successful');");
	        out.print("window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id=" + assetId + "';");
	        out.print("</script>");
	    }
	    
	    private void handleApprovalRequired(PrintWriter out, String groupId, long assetId,
	                                         UserSession userSession, SystemConfig config) {
	        
	        String newURL;
	        String supervisorQuery = "SELECT full_name FROM am_gb_user WHERE user_id=(SELECT supervisor FROM am_group_asset_main WHERE group_id=?)";
	        String supervisorName = approvalRecords.getCodeName(supervisorQuery.toUpperCase(), groupId);
	        
	        if ("Y".equalsIgnoreCase(config.getGroupAssetByAsset())) {
	            newURL = "<script>alert('Group Uncapitalized Asset will be submitted for Approval after Final Creation Of Assets');" +
	                     "window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id=" + assetId + "';</script>";
	        } else {
	            newURL = "<script>alert('Group Uncapitalized Asset Has Been Sent For Approval');" +
	                     "window.location='DocumentHelp.jsp?np=groupAssetUpdateBranch&img=n&id=" + assetId + "';</script>";
	        }
	        
	        out.print(newURL);
	    }
	    
	    private void handleBudgetExceededError(PrintWriter out, long status) {
	        out.print("<script>");
	        out.print("alert('Addition of Assets will overshoot quarterly budget for this category and you are not allowed to do so. Please exit and seek supplementary budgetary Allocation " + status + "');");
	        out.print("history.go(-2);");
	        out.print("</script>");
	    }
	    
	    private void handleGeneralError(PrintWriter out) {
	        out.print("<script>");
	        out.print("alert('Records can not be saved!. Please Check your entries.');");
	        out.print("history.go(-2);");
	        out.print("</script>");
	    }
	    
	    private void sendErrorResponse(PrintWriter out, String message) {
	        out.print("<script>");
	        out.print("alert('" + message + "');");
	        out.print("history.go(-1);");
	        out.print("</script>");
	    }
	    
	    // Inner classes for data structures
	    private static class UserSession {
	        private String userId;
	        private String userClass;
	        private String userName;
	        private String branch;
	        private String deptCode;
	        private String branchRestrict;
	        private String deptRestrict;
	        private String branchRestricted;
	        private String branchRestrictCode;
	        private String systemIp;
	        private String workstationName;
	        
	        // Getters and setters
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
	        public String getBranchRestrict() { return branchRestrict; }
	        public void setBranchRestrict(String branchRestrict) { this.branchRestrict = branchRestrict; }
	        public String getDeptRestrict() { return deptRestrict; }
	        public void setDeptRestrict(String deptRestrict) { this.deptRestrict = deptRestrict; }
	        public String getBranchRestricted() { return branchRestricted; }
	        public void setBranchRestricted(String branchRestricted) { this.branchRestricted = branchRestricted; }
	        public String getBranchRestrictCode() { return branchRestrictCode; }
	        public void setBranchRestrictCode(String branchRestrictCode) { this.branchRestrictCode = branchRestrictCode; }
	        public String getSystemIp() { return systemIp; }
	        public void setSystemIp(String systemIp) { this.systemIp = systemIp; }
	        public String getWorkstationName() { return workstationName; }
	        public void setWorkstationName(String workstationName) { this.workstationName = workstationName; }
	    }
	    
	    private static class SystemConfig {
	        private String thirdPartyLabel;
	        private String groupAssetByAsset;
	        private String singleApproval;
	        private String vatRequired;
	        private String sbuRequired;
	        private String uploadFolder;
	        private String subjectToVat;
	        private String thirdPartyRequired;
	        private String costThreshold;
	        
	        // Getters and setters
	        public String getThirdPartyLabel() { return thirdPartyLabel; }
	        public void setThirdPartyLabel(String thirdPartyLabel) { this.thirdPartyLabel = thirdPartyLabel; }
	        public String getGroupAssetByAsset() { return groupAssetByAsset; }
	        public void setGroupAssetByAsset(String groupAssetByAsset) { this.groupAssetByAsset = groupAssetByAsset; }
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
	        public String getCostThreshold() { return costThreshold; }
	        public void setCostThreshold(String costThreshold) { this.costThreshold = costThreshold; }
	    }
	    
	    private static class GroupAssetData {
	        // Basic Information
	        private String assetId;
	        private String groupId;
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
	        private String depreciationStartDate;
	        private String depreciationEndDate;
	        
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
	        private String sectionId;
	        
	        // Vendor Information
	        private String vendorAccount;
	        private String vendorName;
	        private String suppliedBy;
	        private String lpo;
	        private String invoiceNo;
	        private String invoiceNum;
	        
	        // Personnel Information
	        private String assetUser;
	        private String maintainedBy;
	        private String authorizedBy;
	        private String purchaseReason;
	        
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
	        
	        // Group specific
	        private String noOfItems;
	        private String threshold;
	        private String lpoSet;
	        private String multiple;
	        private String partPay;
	        private String integrifyId;
	        
	        // Getters and setters (generate these - too many to list here)
	        // For brevity, I'm not including all getters/setters but they should be generated
	        
	        public String getAssetId() { return assetId; }
	        public void setAssetId(String assetId) { this.assetId = assetId; }
	        public String getGroupId() { return groupId; }
	        public void setGroupId(String groupId) { this.groupId = groupId; }
	        public String getCategoryId() { return categoryId; }
	        public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
	        public String getSubCategoryId() { return subCategoryId; }
	        public void setSubCategoryId(String subCategoryId) { this.subCategoryId = subCategoryId; }
	        public String getDepartmentId() { return departmentId; }
	        public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
	        public String getBranchId() { return branchId; }
	        public void setBranchId(String branchId) { this.branchId = branchId; }
	        public String getSbuCode() { return sbuCode; }
	        public void setSbuCode(String sbuCode) { this.sbuCode = sbuCode; }
	        public String getRegistrationNo() { return registrationNo; }
	        public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }
	        public String getDateOfPurchase() { return dateOfPurchase; }
	        public void setDateOfPurchase(String dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }
	        public String getPostingDate() { return postingDate; }
	        public void setPostingDate(String postingDate) { this.postingDate = postingDate; }
	        public String getWarrantyStartDate() { return warrantyStartDate; }
	        public void setWarrantyStartDate(String warrantyStartDate) { this.warrantyStartDate = warrantyStartDate; }
	        public String getExpiryDate() { return expiryDate; }
	        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
	        public String getNoOfMonths() { return noOfMonths; }
	        public void setNoOfMonths(String noOfMonths) { this.noOfMonths = noOfMonths; }
	        public String getDepreciationStartDate() { return depreciationStartDate; }
	        public void setDepreciationStartDate(String depreciationStartDate) { this.depreciationStartDate = depreciationStartDate; }
	        public String getDepreciationEndDate() { return depreciationEndDate; }
	        public void setDepreciationEndDate(String depreciationEndDate) { this.depreciationEndDate = depreciationEndDate; }
	        public String getCostPrice() { return costPrice; }
	        public void setCostPrice(String costPrice) { this.costPrice = costPrice; }
	        public String getVatAmount() { return vatAmount; }
	        public void setVatAmount(String vatAmount) { this.vatAmount = vatAmount; }
	        public String getVatableCost() { return vatableCost; }
	        public void setVatableCost(String vatableCost) { this.vatableCost = vatableCost; }
	        public String getWhTaxAmount() { return whTaxAmount; }
	        public void setWhTaxAmount(String whTaxAmount) { this.whTaxAmount = whTaxAmount; }
	        public String getResidualValue() { return residualValue; }
	        public void setResidualValue(String residualValue) { this.residualValue = residualValue; }
	        public String getAmountPTD() { return amountPTD; }
	        public void setAmountPTD(String amountPTD) { this.amountPTD = amountPTD; }
	        public String getTransportCost() { return transportCost; }
	        public void setTransportCost(String transportCost) { this.transportCost = transportCost; }
	        public String getOtherCost() { return otherCost; }
	        public void setOtherCost(String otherCost) { this.otherCost = otherCost; }
	        public String getDepreciationRate() { return depreciationRate; }
	        public void setDepreciationRate(String depreciationRate) { this.depreciationRate = depreciationRate; }
	        public String getAccumDep() { return accumDep; }
	        public void setAccumDep(String accumDep) { this.accumDep = accumDep; }
	        public String getNbv() { return nbv; }
	        public void setNbv(String nbv) { this.nbv = nbv; }
	        public String getDescription() { return description; }
	        public void setDescription(String description) { this.description = description; }
	        public String getMemo() { return memo; }
	        public void setMemo(String memo) { this.memo = memo; }
	        public String getRegion() { return region; }
	        public void setRegion(String region) { this.region = region; }
	        public String getMake() { return make; }
	        public void setMake(String make) { this.make = make; }
	        public String getModel() { return model; }
	        public void setModel(String model) { this.model = model; }
	        public String getLocation() { return location; }
	        public void setLocation(String location) { this.location = location; }
	        public String getSerialNumber() { return serialNumber; }
	        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
	        public String getBarCode() { return barCode; }
	        public void setBarCode(String barCode) { this.barCode = barCode; }
	        public String getEngineNumber() { return engineNumber; }
	        public void setEngineNumber(String engineNumber) { this.engineNumber = engineNumber; }
	        public String getState() { return state; }
	        public void setState(String state) { this.state = state; }
	        public String getDriver() { return driver; }
	        public void setDriver(String driver) { this.driver = driver; }
	        public String getSectionId() { return sectionId; }
	        public void setSectionId(String sectionId) { this.sectionId = sectionId; }
	        public String getVendorAccount() { return vendorAccount; }
	        public void setVendorAccount(String vendorAccount) { this.vendorAccount = vendorAccount; }
	        public String getVendorName() { return vendorName; }
	        public void setVendorName(String vendorName) { this.vendorName = vendorName; }
	        public String getSuppliedBy() { return suppliedBy; }
	        public void setSuppliedBy(String suppliedBy) { this.suppliedBy = suppliedBy; }
	        public String getLpo() { return lpo; }
	        public void setLpo(String lpo) { this.lpo = lpo; }
	        public String getInvoiceNo() { return invoiceNo; }
	        public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }
	        public String getInvoiceNum() { return invoiceNum; }
	        public void setInvoiceNum(String invoiceNum) { this.invoiceNum = invoiceNum; }
	        public String getAssetUser() { return assetUser; }
	        public void setAssetUser(String assetUser) { this.assetUser = assetUser; }
	        public String getMaintainedBy() { return maintainedBy; }
	        public void setMaintainedBy(String maintainedBy) { this.maintainedBy = maintainedBy; }
	        public String getAuthorizedBy() { return authorizedBy; }
	        public void setAuthorizedBy(String authorizedBy) { this.authorizedBy = authorizedBy; }
	        public String getPurchaseReason() { return purchaseReason; }
	        public void setPurchaseReason(String purchaseReason) { this.purchaseReason = purchaseReason; }
	        public String getSubjectToVat() { return subjectToVat; }
	        public void setSubjectToVat(String subjectToVat) { this.subjectToVat = subjectToVat; }
	        public String getWhTaxCb() { return whTaxCb; }
	        public void setWhTaxCb(String whTaxCb) { this.whTaxCb = whTaxCb; }
	        public String getFullyPAID() { return fullyPAID; }
	        public void setFullyPAID(String fullyPAID) { this.fullyPAID = fullyPAID; }
	        public String getProjectCode() { return projectCode; }
	        public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
	        public String getRequireDepreciation() { return requireDepreciation; }
	        public void setRequireDepreciation(String requireDepreciation) { this.requireDepreciation = requireDepreciation; }
	        public String getRequireRedistribution() { return requireRedistribution; }
	        public void setRequireRedistribution(String requireRedistribution) { this.requireRedistribution = requireRedistribution; }
	        public String getEmail1() { return email1; }
	        public void setEmail1(String email1) { this.email1 = email1; }
	        public String getEmail2() { return email2; }
	        public void setEmail2(String email2) { this.email2 = email2; }
	        public String getWhoToRemind1() { return whoToRemind1; }
	        public void setWhoToRemind1(String whoToRemind1) { this.whoToRemind1 = whoToRemind1; }
	        public String getWhoToRemind2() { return whoToRemind2; }
	        public void setWhoToRemind2(String whoToRemind2) { this.whoToRemind2 = whoToRemind2; }
	        public String getSpare1() { return spare1; }
	        public void setSpare1(String spare1) { this.spare1 = spare1; }
	        public String getSpare2() { return spare2; }
	        public void setSpare2(String spare2) { this.spare2 = spare2; }
	        public String getSpare3() { return spare3; }
	        public void setSpare3(String spare3) { this.spare3 = spare3; }
	        public String getSpare4() { return spare4; }
	        public void setSpare4(String spare4) { this.spare4 = spare4; }
	        public String getSpare5() { return spare5; }
	        public void setSpare5(String spare5) { this.spare5 = spare5; }
	        public String getSpare6() { return spare6; }
	        public void setSpare6(String spare6) { this.spare6 = spare6; }
	        public String getNoOfItems() { return noOfItems; }
	        public void setNoOfItems(String noOfItems) { this.noOfItems = noOfItems; }
	        public String getThreshold() { return threshold; }
	        public void setThreshold(String threshold) { this.threshold = threshold; }
	        public String getLpoSet() { return lpoSet; }
	        public void setLpoSet(String lpoSet) { this.lpoSet = lpoSet; }
	        public String getMultiple() { return multiple; }
	        public void setMultiple(String multiple) { this.multiple = multiple; }
	        public String getPartPay() { return partPay; }
	        public void setPartPay(String partPay) { this.partPay = partPay; }
	        public String getIntegrifyId() { return integrifyId; }
	        public void setIntegrifyId(String integrifyId) { this.integrifyId = integrifyId; }
	    }
	
	    
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}