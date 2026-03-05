package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
import magma.net.dao.MagmaDBConnection;
import magma.util.Codes;

import com.magbel.legend.bus.ApprovalRecords;
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

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GroupAssetServlet.class);

    private static final String PROPERTIES_PATH =
            "C:\\Property\\LegendPlus.properties";

    private static final String DESTINATION_PAGE =
            "DocumentHelp.jsp?np=groupAssetUpdate";

    private Properties config;

    /* =========================================================
       INIT
       ========================================================= */

    @Override
    public void init() throws ServletException {

        try {
            config = loadProperties();
            LOGGER.info("GroupAssetServlet initialized successfully");

        } catch (Exception e) {
            LOGGER.error("Failed to initialize GroupAssetServlet", e);
            throw new ServletException(e);
        }
    }

    /* =========================================================
       HTTP HANDLERS
       ========================================================= */

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            processRequest(request, out);

        } catch (Throwable e) {

            LOGGER.error("Error processing request", e);
            handleError(response,
                    "An error occurred while processing the request");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    /* =========================================================
       MAIN REQUEST PROCESSOR
       ========================================================= */

    private void processRequest(HttpServletRequest request,
                                PrintWriter out) throws Throwable {

        /* SERVICES CREATED PER REQUEST (prevents connection leaks) */

        EmailSmsServiceBus emailService = new EmailSmsServiceBus();
        ApprovalRecords approvalRecords = new ApprovalRecords();
        ApprovalRecords busApprovalRecords = new ApprovalRecords();
        SecurityHandler securityHandler = new SecurityHandler();
        HtmlUtility htmlUtil = new HtmlUtility();
        ApplicationHelper appHelper = new ApplicationHelper();
        CompanyHandler companyHandler = new CompanyHandler();
        AssetRecordsBean assetRecords = new AssetRecordsBean();
        ApprovalBean approvalBean = new ApprovalBean();
        CurrencyNumberformat currencyFormatter = new CurrencyNumberformat();

        String groupAssetByAsset =
                config.getProperty("groupAssetByAsset", "N");

        String singleApproval =
                config.getProperty("singleApproval", "N");

        String vatRequired =
                config.getProperty("VATREQUIRED", "N");

        UserInfo userInfo =
                extractUserInfo(request, securityHandler, approvalBean);

        PostingParameters params =
                extractPostingParameters(request);

        if (params.saveBtn != null) {

            handleSaveAction(
                    request,
                    out,
                    params,
                    userInfo,
                    groupAssetByAsset,
                    singleApproval,
                    vatRequired,
                    assetRecords,
                    busApprovalRecords,
                    htmlUtil,
                    emailService,
                    companyHandler
            );
        }
    }

    /* =========================================================
       SAVE ACTION
       ========================================================= */

    private void handleSaveAction(
            HttpServletRequest request,
            PrintWriter out,
            PostingParameters params,
            UserInfo userInfo,
            String groupAssetByAsset,
            String singleApproval,
            String vatRequired,
            AssetRecordsBean assetRecords,
            ApprovalRecords busApprovalRecords,
            HtmlUtility htmlUtil,
            EmailSmsServiceBus emailService,
            CompanyHandler companyHandler
    ) throws Throwable {
    	MagmaDBConnection dbConn = new MagmaDBConnection();
    	 try (Connection c = dbConn.getConnection("legendPlus")) {
        int numOfTransactionLevel =
                assetRecords.getNumOfTransactionLevel(c,"3");

        GroupAssetBean groupAsset =
                buildGroupAssetBean(request, params, userInfo);

        long[] status =
                groupAsset.insertGroupAssetRecord(c,
                        groupAssetByAsset,
                        singleApproval,
                        userInfo.branch,
                        userInfo.deptCode,
                        userInfo.userName);

        if (status[0] == 0) {

            handleSuccessfulSave(
                    request,
                    out,
                    params,
                    userInfo,
                    groupAsset,
                    status,
                    numOfTransactionLevel,
                    groupAssetByAsset,
                    vatRequired,
                    busApprovalRecords,
                    htmlUtil,
                    emailService,
                    companyHandler
            );

        } else if (status[0] == 1 || status[0] == 2) {

            handleBudgetExceeded(out, status);

        } else {

            handleSaveFailure(out);
        }
    	 }
    }

    /* =========================================================
       SUCCESS HANDLER
       ========================================================= */

    private void handleSuccessfulSave(
            HttpServletRequest request,
            PrintWriter out,
            PostingParameters params,
            UserInfo userInfo,
            GroupAssetBean groupAsset,
            long[] status,
            int numOfTransactionLevel,
            String groupAssetByAsset,
            String vatRequired,
            ApprovalRecords busApprovalRecords,
            HtmlUtility htmlUtil,
            EmailSmsServiceBus emailService,
            CompanyHandler companyHandler
    ) throws Exception {

        handleFileUpload(request, status);

        String assetId = groupAsset.getAsset_id();
        long groupId = groupAsset.getGroupID(assetId);
        String groupIdStr = Long.toString(groupId);

        String invNumber =
                params.suppliedBy + "-" + params.invoiceNum;

        htmlUtil.insToAm_Invoice_No(
                groupIdStr,
                params.lpo,
                invNumber,
                "Group Asset Creation"
        );

        sendCreationEmail(assetId, emailService, companyHandler);

        showSuccessAlert(
                out,
                "Group Asset creation successful",
                DESTINATION_PAGE + "&img=n&id=" + groupIdStr
        );
    }

    /* =========================================================
       FILE UPLOAD
       ========================================================= */

    private void handleFileUpload(
            HttpServletRequest request,
            long[] status) throws Exception {

        String uploadFolder =
                config.getProperty("imagesUrl");

        if (uploadFolder == null || uploadFolder.isEmpty()) {

            LOGGER.warn("Upload folder not configured");
            return;
        }

        File uploadDir = new File(uploadFolder);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (Part part : request.getParts()) {

            if (part == null || part.getSize() == 0) {
                continue;
            }

            String fileName = part.getSubmittedFileName();

            if (fileName == null) {
                continue;
            }

            String ext =
                    FilenameUtils.getExtension(fileName);

            String newFileName =
                    "W" + status[1] + "." + ext;

            String filePath =
                    uploadFolder + File.separator + newFileName;

            part.write(filePath);

            LOGGER.info("File uploaded: {}", filePath);
        }
    }

    /* =========================================================
       EMAIL
       ========================================================= */

    private void sendCreationEmail(
            String assetId,
            EmailSmsServiceBus emailService,
            CompanyHandler companyHandler) {

        try {

            String mailCode = "201";

            if (companyHandler
                    .getEmailStatus(mailCode)
                    .equalsIgnoreCase("Active")) {

                Codes codes = new Codes();

                String to =
                        codes.MailTo(mailCode, "Group Asset Creation");

                String subject = "Group Asset Creation";

                String message =
                        "New asset with ID: "
                                + assetId
                                + " was successfully created.";

                emailService.sendMail(to, subject, message);

                LOGGER.info("Creation email sent for asset: {}",
                        assetId);
            }

        } catch (Exception e) {

            LOGGER.error("Failed to send creation email", e);
        }
    }

    /* =========================================================
       UTILITIES
       ========================================================= */

    private Properties loadProperties() throws IOException {

        Properties props = new Properties();

        try (FileInputStream input =
                     new FileInputStream(PROPERTIES_PATH)) {

            props.load(input);
        }

        return props;
    }

    private void showSuccessAlert(
            PrintWriter out,
            String message,
            String redirectUrl) {

        out.println("<script>");
        out.println("alert('" + message + "');");
        out.println("window.location='" + redirectUrl + "';");
        out.println("</script>");
    }

    private void showErrorAlert(
            PrintWriter out,
            String message,
            int historyStep) {

        out.println("<script>");
        out.println("alert('" + message + "');");
        out.println("history.go(" + historyStep + ");");
        out.println("</script>");
    }

    private void handleError(
            HttpServletResponse response,
            String message) throws IOException {

        try (PrintWriter out = response.getWriter()) {

            out.println("<script>");
            out.println("alert('" + message + "');");
            out.println("history.go(-1);");
            out.println("</script>");
        }
    }

    /* =========================================================
       USER INFO
       ========================================================= */

    private UserInfo extractUserInfo(
            HttpServletRequest request,
            SecurityHandler securityHandler,
            ApprovalBean approvalBean) {

        UserInfo userInfo = new UserInfo();

        HttpSession session = request.getSession(false);

        if (session == null) {
            return userInfo;
        }

        userInfo.userId =
                (String) session.getAttribute("CurrentUser");

        if (userInfo.userId != null) {

            User user =
                    securityHandler.getUserByUserID(userInfo.userId);

            userInfo.userName = user.getUserName();
            userInfo.branch = user.getBranch();
            userInfo.deptCode = user.getDeptCode();
        }

        return userInfo;
    }

    /* =========================================================
       PARAMETER EXTRACTION
       ========================================================= */

    private PostingParameters extractPostingParameters(
            HttpServletRequest request) {

        PostingParameters params = new PostingParameters();

        params.saveBtn = request.getParameter("saveBtn");
        params.groupId = request.getParameter("gid");
        System.out.println("groupId in servlet: " + params.groupId);
        params.lpo = request.getParameter("lpo");
        params.invoiceNum = request.getParameter("invoiceNum");
        params.suppliedBy = request.getParameter("sb");
        params.branchId = request.getParameter("branch_id");
        
        params.id = request.getParameter("id");
        params.categoryId = request.getParameter("categoryId");
        params.isNew = request.getParameter("s");
        params.category = request.getParameter("category_id");
        params.payType = getParameterOrDefault(request.getParameter("payType"), "N");
        params.payCode = getParameterOrDefault(request.getParameter("category_id"), "0");
        params.partPay = getParameterOrDefault(request.getParameter("partPay"), "");
        params.acronym = getParameterOrDefault(request.getParameter("acronym"), "");
       
        
        // Asset details
        params.assetId = request.getParameter("asset_id");
        
        params.dateOfPurchase = request.getParameter("date_of_purchase");
        params.subCategoryId = request.getParameter("sub_category_id");
        System.out.println("subCategoryId in servlet: " + params.subCategoryId);
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

    /* =========================================================
       BUILD BEAN
       ========================================================= */

    private GroupAssetBean buildGroupAssetBean(
            HttpServletRequest request,
            PostingParameters params,
            UserInfo userInfo) throws Exception {

        GroupAssetBean bean = new GroupAssetBean();

        bean.setGroup_id(params.groupId);
        bean.setBranch_id(params.branchId);
        bean.setUser_id(userInfo.userId);
        bean.setGroup_id(params.groupId);
        bean.setNo_of_items(params.noOfItems);
        bean.setDepreciation_start_date(params.deprStartDate);
        bean.setDepreciation_end_date(params.deprEndDate);
        bean.setDepartment_id(params.departmentId);
        bean.setDate_of_purchase(params.dateOfPurchase);
        bean.setSub_category_id(params.subCategoryId);
        bean.setSbu_code(params.sbuCode);
        bean.setBranch_id(params.branchId);
        bean.setCategory_id(params.category);
        
        // Financial information
        bean.setCost_price(params.costPrice);
        bean.setVat_amount(params.vatAmount);
        bean.setVatable_cost(params.vatableCost);
        bean.setWh_tax_amount(params.whTaxAmount);
        bean.setResidual_value(params.residualValue);
        bean.setAmountPTD(params.amountPTD);
        bean.setTransport_cost(params.transportCost);
        bean.setOther_cost(params.otherCost);
        bean.setDepreciation_rate(params.depreciationRate);
        bean.setAccum_dep(params.accumDep);
        
        // Descriptive information
        bean.setDescription(params.description);
        bean.setVendorName(params.vendorName);
        bean.setMake(params.make);
        bean.setLocation(params.location);
        bean.setSection(params.sectionId);
        bean.setSection_id(params.sectionId);
        bean.setState(params.state);
        bean.setDriver(params.driver);
        bean.setBar_code(params.barCode);
        bean.setVendor_account(params.vendorAccount);
        bean.setMaintained_by(params.maintainedBy);
        bean.setReason(params.purchaseReason);
        bean.setUser(params.assetUser);
        bean.setSbu_code(params.sbuCode);
        bean.setLpo(params.lpo);
        bean.setAuthorized_by(params.authorizedBy);
        bean.setSubject_to_vat(params.subjectToVatOld);
        bean.setWh_tax_cb(params.whTaxCb);
        bean.setSupplied_by(params.suppliedBy);
        bean.setWho_to_rem(params.whoToRem1);
        bean.setInvoiceNum(params.invoiceNum);
        bean.setEmail_1(params.email1);
        bean.setWho_to_rem_2(params.whoToRem2);
        bean.setPosting_date(params.postingDate);
        bean.setEmail2(params.email2);
        
        // Spare fields
        bean.setSpare_1(params.spare1);
        bean.setSpare_2(params.spare2);
        bean.setSpare_3(params.spare3);
        bean.setSpare_4(params.spare4);
        bean.setSpare_5(params.spare5);
        bean.setSpare_6(params.spare6);
        
        // Warranty information
        bean.setWarrantyStartDate(params.warrantyStartDate);
        bean.setNoOfMonths(params.noOfMonths);
        bean.setExpiryDate(params.expiryDate);
        
        // Project and flags
        bean.setProjectCode(params.projectCode);
        bean.setRequire_depreciation(params.requireDepreciation);
        bean.setRequire_redistribution(params.requireRedistribution);
        bean.setUser_id(userInfo.userId);
        bean.setFullyPAID(params.fullyPaid);
        bean.setPartPAY(params.partPay);
        bean.setRegistration_no(params.registrationNo);
        

        return bean;
    }

    /* =========================================================
       ERROR HANDLERS
       ========================================================= */

    private void handleBudgetExceeded(
            PrintWriter out,
            long[] status) {

        String message =
                "Addition of Assets will overshoot quarterly budget. "
                        + Arrays.toString(status);

        showErrorAlert(out, message, -2);
    }

    private void handleSaveFailure(PrintWriter out) {

        showErrorAlert(
                out,
                "Records cannot be saved! Please check your entries.",
                -2);
    }

    /* =========================================================
       INNER CLASSES
       ========================================================= */

    private static class UserInfo {

        String userId;
        String userName;
        String deptCode;
        String branch;
    }

    private static class PostingParameters {

        String saveBtn;
        String groupId;
        String branchId;
        String lpo;
        String invoiceNum;
        String suppliedBy;
        String id;
        String categoryId;
        String isNew;
        String category;
        String payType;
        String payCode;
        String partPay;
        String acronym;
       
        
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