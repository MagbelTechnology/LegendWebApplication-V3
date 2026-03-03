package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import magma.ApprovalBean;
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
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class GroupAssetServlet extends HttpServlet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupAssetServlet.class);
    private static final String PROPERTIES_PATH = "C:\\Property\\LegendPlus.properties";
    private static final String DESTINATION_PAGE = "DocumentHelp.jsp?np=groupAssetUpdate";
    
    private EmailSmsServiceBus emailService;
    private ApprovalRecords approvalRecords;
    private ApprovalRecords busApprovalRecords;
    private SecurityHandler securityHandler;
    private HtmlUtility htmlUtil;
    private ApplicationHelper appHelper;
    private CompanyHandler companyHandler;
    private AssetRecordsBean assetRecords;
    private ApprovalBean approvalBean;
    private CurrencyNumberformat currencyFormatter;
    
    @Override
    public void init() throws ServletException {
        try {
            emailService = new EmailSmsServiceBus();
            approvalRecords = new ApprovalRecords();
            busApprovalRecords = new ApprovalRecords();
            securityHandler = new SecurityHandler();
            htmlUtil = new HtmlUtility();
            appHelper = new ApplicationHelper();
            companyHandler = new CompanyHandler();
            assetRecords = new AssetRecordsBean();
            approvalBean = new ApprovalBean();
            currencyFormatter = new CurrencyNumberformat();
            
            LOGGER.info("GroupAssetServlet initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize GroupAssetServlet", e);
            throw new ServletException("Initialization failed", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            processRequest(request, out);
        } catch (Exception e) {
            LOGGER.error("Error processing request", e);
            handleError(response, "An error occurred while processing the request");
        } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    private void processRequest(HttpServletRequest request, PrintWriter out) throws Throwable {
        RequestContext context = extractRequestContext(request);
        Properties config = loadProperties();
        
        String groupAssetByAsset = config.getProperty("groupAssetByAsset", "N");
        String singleApproval = config.getProperty("singleApproval", "N");
        String vatRequired = config.getProperty("VATREQUIRED", "N");
        String sbuRequired = config.getProperty("SBUREQUIRED", "N");
        String thirdPartyLabel = config.getProperty("ThirdPartyLabel", "");
        
        UserInfo userInfo = extractUserInfo(request);
        PostingParameters params = extractPostingParameters(request, userInfo);
        
        // Handle save button action
        if (params.saveBtn != null) {
            handleSaveAction(request, out, context, params, userInfo, 
                           groupAssetByAsset, singleApproval, vatRequired);
        }
    }
    
    private void handleSaveAction(HttpServletRequest request, PrintWriter out, 
                                  RequestContext context, PostingParameters params,
                                  UserInfo userInfo, String groupAssetByAsset, 
                                  String singleApproval, String vatRequired) throws Throwable {
        
        int numOfTransactionLevel = assetRecords.getNumOfTransactionLevel("3");
        Properties config = loadProperties();
        
        GroupAssetBean groupAsset = buildGroupAssetBean(request, params, userInfo);
        
        LOGGER.info("Saving group asset with parameters: groupAssetByAsset={}, singleApproval={}", 
                   groupAssetByAsset, singleApproval);
        
        long[] status = groupAsset.insertGroupAssetRecord(groupAssetByAsset, singleApproval, 
                                                         userInfo.branch, userInfo.deptCode, 
                                                         userInfo.userName);
        
        if (status[0] == 0) {
            handleSuccessfulSave(request, out, params, userInfo, groupAsset, 
                               status, numOfTransactionLevel, groupAssetByAsset, vatRequired);
        } else if (status[0] == 1 || status[0] == 2) {
            handleBudgetExceeded(out, status);
        } else {
            handleSaveFailure(out);
        }
    }
    
    private void handleSuccessfulSave(HttpServletRequest request, PrintWriter out,
                                      PostingParameters params, UserInfo userInfo,
                                      GroupAssetBean groupAsset, long[] status,
                                      int numOfTransactionLevel, String groupAssetByAsset,
                                      String vatRequired) throws Exception {
        
        // Handle file upload
        handleFileUpload(request, status);
        
        String assetId = groupAsset.getAsset_id();
        long groupId = groupAsset.getGroupID(assetId);
        String groupIdStr = Long.toString(groupId);
        
        LOGGER.info("Group asset saved successfully: assetId={}, groupId={}", assetId, groupIdStr);
        
        // Insert invoice information
        String invNumber = params.suppliedBy + "-" + params.invoiceNum;
        htmlUtil.insToAm_Invoice_No(groupIdStr, params.lpo, invNumber, "Group Asset Creation");
        
        if (numOfTransactionLevel == 0) {
            handleZeroTransactionLevel(out, params, userInfo, groupAsset, 
                                      groupIdStr, assetId, status, vatRequired);
        } else {
            handleMultiTransactionLevel(out, params, groupIdStr, assetId, 
                                       status, groupAssetByAsset);
        }
    }
    
    private void handleZeroTransactionLevel(PrintWriter out, PostingParameters params,
                                           UserInfo userInfo, GroupAssetBean groupAsset,
                                           String groupIdStr, String assetId, 
                                           long[] status, String vatRequired) throws Exception {
        
        // Update approval status
        boolean updated = assetRecords.updateNewApprovalAssetStatus(groupIdStr, 
                                                                   Integer.parseInt(userInfo.userId));
        
        // Get supervisor name
        String supervisorQuery = "SELECT full_name FROM am_gb_user WHERE user_id = " +
                                "(SELECT supervisor FROM am_group_asset_main WHERE group_id = ?)";
        String supervisorName = busApprovalRecords.getCodeName(supervisorQuery, groupIdStr);
        
        // Insert raise entry
        String pageName = "Group ASSET CREATION RAISE ENTRY";
        String url = DESTINATION_PAGE + "&id=" + groupIdStr + "&pageDirect=Y";
        String name = busApprovalRecords.getCodeName(
            "SELECT full_name FROM am_gb_user WHERE user_id = ?", userInfo.userId);
        String branchName = busApprovalRecords.getCodeName(
            "SELECT branch_name FROM am_ad_branch WHERE branch_id = ?", params.branchId);
        
        String subjectT = groupAsset.subjectToVat(assetId);
        String whT = groupAsset.whTax(assetId);
        
        busApprovalRecords.insertApproval(groupIdStr, groupAsset.getDescription(), 
                                         pageName, "", params.partPay, name, 
                                         branchName, subjectT, whT, url);
        busApprovalRecords.updateRaiseEntry(groupIdStr);
        
        // Send email if enabled
        sendCreationEmail(assetId);
        
        showSuccessAlert(out, "Group Asset creation successful", 
                        DESTINATION_PAGE + "&img=n&id=" + groupIdStr);
    }
    
    private void handleMultiTransactionLevel(PrintWriter out, PostingParameters params,
                                            String groupIdStr, String assetId,
                                            long[] status, String groupAssetByAsset) {
        
        String supervisorQuery = "SELECT full_name FROM am_gb_user WHERE user_id = " +
                                "(SELECT supervisor FROM am_group_asset_main WHERE group_id = ?)";
        String supervisorName = busApprovalRecords.getCodeName(supervisorQuery, groupIdStr);
        
        String message = groupAssetByAsset.equalsIgnoreCase("Y") 
            ? "Group Asset will be submitted for Approval after Final Creation Of Assets"
            : "Group Asset Has Been Sent For Approval";
        
        String redirectUrl = DESTINATION_PAGE + "&id=" + status[1] + 
                            "&tran_status=A&tran_type=Group Asset Update&assetId=" + 
                            assetId + "&reason=&pPage=groupAssetUpdate";
        
        showSuccessAlert(out, message, redirectUrl);
    }
    
    private void handleBudgetExceeded(PrintWriter out, long[] status) {
        String message = "Addition of Assets will overshoot quarterly budget for this category. " +
                        "Please exit and seek supplementary budgetary allocation " + Arrays.toString(status);
        showErrorAlert(out, message, -2);
    }
    
    private void handleSaveFailure(PrintWriter out) {
        showErrorAlert(out, "Records cannot be saved! Please check your entries.", -2);
    }
    
    private void handleFileUpload(HttpServletRequest request, long[] status) throws Exception {
        Properties config = loadProperties();
        String uploadFolder = config.getProperty("imagesUrl");
        
        if (uploadFolder == null || uploadFolder.isEmpty()) {
            LOGGER.warn("Upload folder not configured");
            return;
        }
        
        File uploadDir = new File(uploadFolder);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        
        for (Part part : request.getParts()) {
            if (part == null || part.getSize() == 0) continue;
            
            String fileName = part.getSubmittedFileName();
            if (fileName == null || fileName.endsWith(".php") || fileName.endsWith(".sql")) {
               // LOGGER.warn("Invalid file type: {}", fileName);
                continue;
            }
            
            String ext = FilenameUtils.getExtension(fileName);
            String newFileName = "W" + status[1] + "." + ext;
            String filePath = uploadFolder + File.separator + newFileName;
            
            try (InputStream input = part.getInputStream()) {
                part.write(filePath);
                LOGGER.info("File uploaded successfully: {}", filePath);
            }
        }
    }
    
    private void sendCreationEmail(String assetId) {
        try {
            String mailCode = "201"; // Mail code for asset creation
            if (companyHandler.getEmailStatus(mailCode).equalsIgnoreCase("Active")) {
                Codes codes = new Codes();
                String to = codes.MailTo(mailCode, "Group Asset Creation");
                String subject = "Group Asset Creation";
                String message = "New asset with ID: " + assetId + " was successfully created.";
                emailService.sendMail(to, subject, message);
                LOGGER.info("Creation email sent for asset: {}", assetId);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send creation email", e);
        }
    }
    
    private GroupAssetBean buildGroupAssetBean(HttpServletRequest request, 
                                               PostingParameters params,
                                               UserInfo userInfo) throws Exception {
        GroupAssetBean groupAsset = new GroupAssetBean();
        
        // Basic information
        groupAsset.setGroup_id(params.groupId);
        groupAsset.setNo_of_items(params.noOfItems);
        groupAsset.setDepreciation_start_date(params.deprStartDate);
        groupAsset.setDepreciation_end_date(params.deprEndDate);
        groupAsset.setDepartment_id(params.departmentId);
        groupAsset.setDate_of_purchase(params.dateOfPurchase);
        groupAsset.setSub_category_id(params.subCategoryId);
        groupAsset.setSbu_code(params.sbuCode);
        groupAsset.setBranch_id(params.branchId);
        groupAsset.setCategory_id(params.category);
        
        // Financial information
        groupAsset.setCost_price(params.costPrice);
        groupAsset.setVat_amount(params.vatAmount);
        groupAsset.setVatable_cost(params.vatableCost);
        groupAsset.setWh_tax_amount(params.whTaxAmount);
        groupAsset.setResidual_value(params.residualValue);
        groupAsset.setAmountPTD(params.amountPTD);
        groupAsset.setTransport_cost(params.transportCost);
        groupAsset.setOther_cost(params.otherCost);
        groupAsset.setDepreciation_rate(params.depreciationRate);
        groupAsset.setAccum_dep(params.accumDep);
        
        // Descriptive information
        groupAsset.setDescription(params.description);
        groupAsset.setVendorName(params.vendorName);
        groupAsset.setMake(params.make);
        groupAsset.setLocation(params.location);
        groupAsset.setSection(params.sectionId);
        groupAsset.setSection_id(params.sectionId);
        groupAsset.setState(params.state);
        groupAsset.setDriver(params.driver);
        groupAsset.setBar_code(params.barCode);
        groupAsset.setVendor_account(params.vendorAccount);
        groupAsset.setMaintained_by(params.maintainedBy);
        groupAsset.setReason(params.purchaseReason);
        groupAsset.setUser(params.assetUser);
        groupAsset.setSbu_code(params.sbuCode);
        groupAsset.setLpo(params.lpo);
        groupAsset.setAuthorized_by(params.authorizedBy);
        groupAsset.setSubject_to_vat(params.subjectToVatOld);
        groupAsset.setWh_tax_cb(params.whTaxCb);
        groupAsset.setSupplied_by(params.suppliedBy);
        groupAsset.setWho_to_rem(params.whoToRem1);
        groupAsset.setInvoiceNum(params.invoiceNum);
        groupAsset.setEmail_1(params.email1);
        groupAsset.setWho_to_rem_2(params.whoToRem2);
        groupAsset.setPosting_date(params.postingDate);
        groupAsset.setEmail2(params.email2);
        
        // Spare fields
        groupAsset.setSpare_1(params.spare1);
        groupAsset.setSpare_2(params.spare2);
        groupAsset.setSpare_3(params.spare3);
        groupAsset.setSpare_4(params.spare4);
        groupAsset.setSpare_5(params.spare5);
        groupAsset.setSpare_6(params.spare6);
        
        // Warranty information
        groupAsset.setWarrantyStartDate(params.warrantyStartDate);
        groupAsset.setNoOfMonths(params.noOfMonths);
        groupAsset.setExpiryDate(params.expiryDate);
        
        // Project and flags
        groupAsset.setProjectCode(params.projectCode);
        groupAsset.setRequire_depreciation(params.requireDepreciation);
        groupAsset.setRequire_redistribution(params.requireRedistribution);
        groupAsset.setUser_id(userInfo.userId);
        groupAsset.setFullyPAID(params.fullyPaid);
        groupAsset.setPartPAY(params.partPay);
        groupAsset.setRegistration_no(params.registrationNo);
        
        return groupAsset;
    }
    
    private RequestContext extractRequestContext(HttpServletRequest request) {
        RequestContext context = new RequestContext();
        context.remoteAddr = request.getRemoteAddr();
        
        try {
            InetAddress address = InetAddress.getByName(context.remoteAddr);
            context.workstationName = address.getHostName();
        } catch (Exception e) {
            context.workstationName = "";
           // LOGGER.warn("Could not resolve workstation name for IP: {}", context.remoteAddr);
        }
        
        return context;
    }
    
    private UserInfo extractUserInfo(HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        HttpSession session = request.getSession();
        
        userInfo.userId = (String) session.getAttribute("CurrentUser");
        userInfo.userClass = (String) session.getAttribute("UserClass");
        
        if (userInfo.userId != null) {
            User user = securityHandler.getUserByUserID(userInfo.userId);
            userInfo.userName = user.getUserName();
            userInfo.branchRestrict = user.getBranchRestrict();
            userInfo.deptRestrict = user.getDeptRestrict();
            userInfo.deptCode = user.getDeptCode();
            userInfo.branch = user.getBranch();
            userInfo.branchRestricted = securityHandler.getBranchRestrictedUser(userInfo.userId);
            
            approvalBean.getApprovalDetail(approvalBean.getApprovalLimit(userInfo.userId));
            userInfo.minApproval = approvalBean.getMini_approval();
            userInfo.maxApproval = approvalBean.getMax_approval();
        }
        
        return userInfo;
    }
    
    private PostingParameters extractPostingParameters(HttpServletRequest request, UserInfo userInfo) {
        PostingParameters params = new PostingParameters();
        
        // Basic parameters
        params.id = request.getParameter("id");
        params.categoryId = request.getParameter("categoryId");
        params.groupId = request.getParameter("gid");
        params.saveBtn = request.getParameter("saveBtn");
        params.isNew = request.getParameter("s");
        params.category = request.getParameter("category_id");
        params.payType = getParameterOrDefault(request.getParameter("payType"), "N");
        params.payCode = getParameterOrDefault(request.getParameter("category_id"), "0");
        params.partPay = getParameterOrDefault(request.getParameter("partPay"), "");
        params.acronym = getParameterOrDefault(request.getParameter("acronym"), "");
        params.branchId = request.getParameter("branch_id");
        params.lpo = request.getParameter("lpo");
        params.invoiceNum = request.getParameter("invoiceNum");
        params.suppliedBy = request.getParameter("sb");
        
        // Asset details
        params.assetId = request.getParameter("asset_id");
        params.dateOfPurchase = request.getParameter("date_of_purchase");
        params.subCategoryId = request.getParameter("sub_category_id");
        params.sbuCode = request.getParameter("sbu_code");
        params.costPrice = request.getParameter("cost_price");
        params.vatAmount = request.getParameter("vat_amount");
        params.vatableCost = request.getParameter("vatable_cost");
        params.whTaxAmount = request.getParameter("wh_tax_amount");
        params.residualValue = request.getParameter("residual_value");
        params.amountPTD = request.getParameter("amountPTD");
        params.departmentId = request.getParameter("department_id");
        
        String desc1 = request.getParameter("description1");
        String desc2 = request.getParameter("description2");
        params.description = (desc1 != null && !"0".equals(desc1)) ? desc1 : desc2;
        
        params.region = request.getParameter("region");
        params.make = request.getParameter("make");
        params.location = request.getParameter("location");
        params.sectionId = request.getParameter("section_id");
        params.state = request.getParameter("state");
        params.driver = request.getParameter("driver");
        params.barCode = request.getParameter("bar_code");
        params.vendorAccount = request.getParameter("vendor_accountOld");
        params.vendorName = request.getParameter("vendorName");
        params.maintainedBy = request.getParameter("maintained_by");
        params.purchaseReason = request.getParameter("reason");
        params.authorizedBy = request.getParameter("authorized_by");
        params.noOfItems = request.getParameter("no_of_items");
        params.deprStartDate = request.getParameter("depreciation_start_date");
        params.deprEndDate = request.getParameter("depreciation_end_date");
        params.assetUser = request.getParameter("user");
        params.subjectToVatOld = request.getParameter("subject_to_vatOld");
        params.whTaxCb = request.getParameter("wh_tax_cb");
        params.transportCost = request.getParameter("transport_cost");
        params.otherCost = request.getParameter("other_cost");
        params.depreciationRate = request.getParameter("depreciation_rate");
        params.accumDep = request.getParameter("accum_dep");
        params.nbv = request.getParameter("nbv");
        params.whoToRem1 = request.getParameter("who_to_rem");
        params.email1 = request.getParameter("email_1");
        params.whoToRem2 = request.getParameter("who_to_rem2");
        params.postingDate = request.getParameter("posting_date");
        params.email2 = request.getParameter("email2");
        
        // Spare fields
        params.spare1 = request.getParameter("spare_1");
        params.spare2 = request.getParameter("spare_2");
        params.spare3 = request.getParameter("spare_3");
        params.spare4 = request.getParameter("spare_4");
        params.spare5 = request.getParameter("spare_5");
        params.spare6 = request.getParameter("spare_6");
        
        // Warranty
        params.warrantyStartDate = request.getParameter("warrantyStartDate");
        params.noOfMonths = request.getParameter("noOfMonths");
        params.expiryDate = request.getParameter("expiryDate");
        params.projectCode = getParameterOrDefault(request.getParameter("projectCode"), "");
        params.requireDepreciation = getParameterOrDefault(request.getParameter("require_depreciation"), "");
        params.requireRedistribution = getParameterOrDefault(request.getParameter("require_redistribution"), "");
        params.fullyPaid = request.getParameter("fullyPAID");
        params.registrationNo = request.getParameter("registration_no");
        
        return params;
    }
    
    private String getParameterOrDefault(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }
    
    private Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_PATH)) {
            props.load(input);
        }
        return props;
    }
    
    private void showSuccessAlert(PrintWriter out, String message, String redirectUrl) {
        out.println("<script>");
        out.println("alert('" + message + "');");
        out.println("window.location='" + redirectUrl + "';");
        out.println("</script>");
    }
    
    private void showErrorAlert(PrintWriter out, String message, int historyStep) {
        out.println("<script>");
        out.println("alert('" + message + "');");
        out.println("history.go(" + historyStep + ");");
        out.println("</script>");
    }
    
    private void handleError(HttpServletResponse response, String message) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<script>");
            out.println("alert('" + message + "');");
            out.println("history.go(-1);");
            out.println("</script>");
        }
    }
    
    // ==================== INNER CLASSES ====================
    
    private static class RequestContext {
        String remoteAddr;
        String workstationName;
    }
    
    private static class UserInfo {
        String userId;
        String userClass;
        String userName;
        String branchRestrict;
        String deptRestrict;
        String deptCode;
        String branch;
        String branchRestricted;
        double minApproval;
        double maxApproval;
    }
    
    private static class PostingParameters {
        // Basic parameters
        String id;
        String categoryId;
        String groupId;
        String saveBtn;
        String isNew;
        String category;
        String payType;
        String payCode;
        String partPay;
        String acronym;
        String branchId;
        String lpo;
        String invoiceNum;
        String suppliedBy;
        
        // Asset details
        String assetId;
        String dateOfPurchase;
        String subCategoryId;
        String sbuCode;
        String costPrice;
        String vatAmount;
        String vatableCost;
        String whTaxAmount;
        String residualValue;
        String amountPTD;
        String departmentId;
        String description;
        String region;
        String make;
        String location;
        String sectionId;
        String state;
        String driver;
        String barCode;
        String vendorAccount;
        String vendorName;
        String maintainedBy;
        String purchaseReason;
        String authorizedBy;
        String noOfItems;
        String deprStartDate;
        String deprEndDate;
        String assetUser;
        String subjectToVatOld;
        String whTaxCb;
        String transportCost;
        String otherCost;
        String depreciationRate;
        String accumDep;
        String nbv;
        String whoToRem1;
        String email1;
        String whoToRem2;
        String postingDate;
        String email2;
        
        // Spare fields
        String spare1;
        String spare2;
        String spare3;
        String spare4;
        String spare5;
        String spare6;
        
        // Warranty
        String warrantyStartDate;
        String noOfMonths;
        String expiryDate;
        String projectCode;
        String requireDepreciation;
        String requireRedistribution;
        String fullyPaid;
        String registrationNo;
    }


}