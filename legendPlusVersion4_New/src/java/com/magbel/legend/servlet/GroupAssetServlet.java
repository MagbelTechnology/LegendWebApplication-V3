package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
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

        int numOfTransactionLevel =
                assetRecords.getNumOfTransactionLevel("3");

        GroupAssetBean groupAsset =
                buildGroupAssetBean(request, params, userInfo);

        long[] status =
                groupAsset.insertGroupAssetRecord(
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
        params.lpo = request.getParameter("lpo");
        params.invoiceNum = request.getParameter("invoiceNum");
        params.suppliedBy = request.getParameter("sb");
        params.branchId = request.getParameter("branch_id");

        return params;
    }

    /* =========================================================
       BUILD BEAN
       ========================================================= */

    private GroupAssetBean buildGroupAssetBean(
            HttpServletRequest request,
            PostingParameters params,
            UserInfo userInfo) {

        GroupAssetBean bean = new GroupAssetBean();

        bean.setGroup_id(params.groupId);
        bean.setBranch_id(params.branchId);
        bean.setUser_id(userInfo.userId);

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
    }
}