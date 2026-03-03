package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.HtmlUtility;

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
import magma.util.Codes;



@MultipartConfig(
	    fileSizeThreshold = 0x300000,  // 3MB
	    maxFileSize = 0xA00000,        // 10MB
	    maxRequestSize = 0x3200000     // 50MB
	)
	public class NewAssetServlet extends HttpServlet {
	    
	    private static final Logger LOGGER = Logger.getLogger(NewAssetServlet.class.getName());
	    private static final String PROPERTIES_PATH = "C:\\Property\\LegendPlus.properties";
	    private static final String DESTINATION_PAGE = "DocumentHelp.jsp?np=updateAssetView";
	    private static final String CURRENT_PAGE = "updateAssetView";
	    private static final String TRANSACTION_TYPE = "Asset Update";
	    private static final String ROOT = "new";
	    
	    private EmailSmsServiceBus mailService;
	    private ApprovalRecords approvalRecords;
	    private SecurityHandler securityHandler;
	    private HtmlUtility htmlUtil;
	    private CompanyHandler companyHandler;
	    private ApplicationHelper appHelper;
	    
	    public NewAssetServlet() {
	        this.mailService = new EmailSmsServiceBus();
	        this.approvalRecords = new ApprovalRecords();
	        this.securityHandler = new SecurityHandler();
	        this.htmlUtil = new HtmlUtility();
	        this.companyHandler = new CompanyHandler();
	        this.appHelper = new ApplicationHelper();
	    }
	    
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {
	        
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	       
	        
	        try {
	            UserSession userSession = extractUserSession(request);
	            SystemConfiguration config = loadSystemConfiguration();
	            
	            if (request.getParameter("saveBtn") != null) {
	                try {
						handleAssetSave(request, response, out, userSession, config);
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            
	        } catch (Exception e) {
	            LOGGER.log(Level.SEVERE, "Error processing asset creation", e);
	            handleError(out, "An error occurred while processing your request");
	        }
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
	        }
	        
	        return userSession;
	    }
	    
	    private SystemConfiguration loadSystemConfiguration() throws IOException {
	        Properties props = new Properties();
	        try (FileInputStream input = new FileInputStream(new File(PROPERTIES_PATH))) {
	            props.load(input);
	        }
	        
	        SystemConfiguration config = new SystemConfiguration();
	        config.setThirdPartyLabel(props.getProperty("ThirdPartyLabel"));
	        config.setSingleApproval(props.getProperty("singleApproval"));
	        config.setVatRequired(props.getProperty("VATREQUIRED"));
	        config.setSbuRequired(props.getProperty("SBUREQUIRED"));
	        config.setUploadFolder(props.getProperty("imagesUrl"));
	        
	        LOGGER.info("ThirdPartyLabel: " + config.getThirdPartyLabel());
	        
	        return config;
	    }
	    
	    private void handleAssetSave(HttpServletRequest request, HttpServletResponse response, 
	                                 PrintWriter out, UserSession userSession, SystemConfiguration config) 
	            throws Throwable {
	        
	        AssetRecordsBean assetBean = initializeAssetBean(request, userSession);
	        HttpSession session = request.getSession();
	        session.setAttribute("newAsset", assetBean);
	        
	        // Populate asset bean from request parameters
	        populateAssetFromRequest(assetBean, request);
	        assetBean.setUser_id(userSession.getUserId());
	        
	        // Insert asset record
	        int status = assetBean.insertAssetRecord(userSession.getBranch());
	        LOGGER.info("Asset insertion status: " + status);
	        
	        if (status == 0) {
	            handleSuccessfulAssetCreation(request, response, out, assetBean, userSession, config);
	        } else if (status == 1 || status == 2) {
	            handleBudgetExceededError(out);
	        } else {
	            handleGeneralError(out);
	        }
	    }
	    
	    private AssetRecordsBean initializeAssetBean(HttpServletRequest request, UserSession userSession) throws Exception {
	        AssetRecordsBean bean = new AssetRecordsBean();
	        HttpSession session = request.getSession();
	        
	        if (session.getAttribute("newAsset") != null && "n".equals(request.getParameter("s"))) {
	            session.setAttribute("newAsset", null);
	        }
	        
	        if (session.getAttribute("newAsset") != null) {
	            bean = (AssetRecordsBean) session.getAttribute("newAsset");
	        }
	        
	        bean.setMultiple(request.getParameter("multiple"));
	        bean.setSection_id(request.getParameter("section_id"));
	        bean.setMemo(request.getParameter("memo"));
	        
	        return bean;
	    }
	    
	    private void populateAssetFromRequest(AssetRecordsBean bean, HttpServletRequest request) throws Exception {
	        // Basic Information
	        bean.setAsset_id(request.getParameter("asset_id"));
	        bean.setIntegrifyId(request.getParameter("integrifyId"));
	        bean.setCategory_id(request.getParameter("category_id"));
	        bean.setSub_category_id(request.getParameter("sub_category_id"));
	        bean.setDepartment_id(request.getParameter("department_id"));
	        bean.setBranch_id(request.getParameter("branch_id"));
	        bean.setSbu_code(request.getParameter("sbu_code"));
	        
	        // Dates
	        bean.setDate_of_purchase(request.getParameter("date_of_purchase"));
	        bean.setPosting_date(request.getParameter("posting_date"));
	        bean.setWarrantyStartDate(request.getParameter("warrantyStartDate"));
	        bean.setExpiryDate(request.getParameter("expiryDate"));
	        bean.setNoOfMonths(request.getParameter("noOfMonths"));
	        
	        // Financial Information
	        bean.setCost_price(request.getParameter("cost_price"));
	        bean.setVat_amount(request.getParameter("vat_amount"));
	        bean.setVatable_cost(request.getParameter("vatable_cost"));
	        bean.setWh_tax_amount(request.getParameter("wh_tax_amount"));
	        bean.setResidual_value(request.getParameter("residual_value"));
	        bean.setAmountPTD(request.getParameter("amountPTD"));
	        bean.setTransport_cost(request.getParameter("transport_cost"));
	        bean.setOther_cost(request.getParameter("other_cost"));
	        bean.setDepreciation_rate(request.getParameter("depreciation_rate"));
	        bean.setAccum_dep(request.getParameter("accum_dep"));
	        bean.setNbv(request.getParameter("nbv"));
	        
	        // Description (handle both description1 and description2)
	        String description = request.getParameter("description1");
	        if ("0".equals(description)) {
	            description = request.getParameter("description2");
	        }
	        bean.setDescription(description);
	        
	        // Asset Details
	        bean.setRegionCode(request.getParameter("regionCode"));
	        bean.setMake(request.getParameter("make"));
	        bean.setModel(request.getParameter("model"));
	        bean.setLocation(request.getParameter("location"));
	        bean.setSerial_number(request.getParameter("serial_number"));
	        bean.setBar_code(request.getParameter("bar_code"));
	        bean.setEngine_number(request.getParameter("engine_number"));
	        bean.setState(request.getParameter("state"));
	        bean.setDriver(request.getParameter("driver"));
	        
	        // Vendor Information
	        bean.setVendor_account(request.getParameter("vendor_accountOld"));
	        bean.setVendorName(request.getParameter("vendorName"));
	        bean.setSupplied_by(request.getParameter("sb"));
	        bean.setLpo(request.getParameter("lpo"));
	        
	        // Personnel Information
	        bean.setUser(request.getParameter("user"));
	        bean.setMaintained_by(request.getParameter("maintained_by"));
	        bean.setAuthorized_by(request.getParameter("authorized_by"));
	        bean.setReason(request.getParameter("reason"));
	        
	        // Tax Information
	        bean.setSubject_to_vat(request.getParameter("subject_to_vatOLD"));
	        bean.setWh_tax_cb(request.getParameter("wh_tax_cb"));
	        
	        // Additional Fields
	        bean.setFullyPAID(request.getParameter("fullyPAID"));
	        bean.setProjectCode(request.getParameter("projectCode"));
	        bean.setRequire_depreciation(request.getParameter("require_depreciation"));
	        bean.setRequire_redistribution(request.getParameter("require_redistribution"));
	        
	        // Email and Notification
	        bean.setEmail_1(request.getParameter("email_1"));
	        bean.setEmail2(request.getParameter("email2"));
	        
	        // Spare Fields
	        bean.setSpare_1(request.getParameter("spare_1"));
	        bean.setSpare_2(request.getParameter("spare_2"));
	        bean.setSpare_3(request.getParameter("spare_3"));
	        bean.setSpare_4(request.getParameter("spare_4"));
	        bean.setSpare_5(request.getParameter("spare_5"));
	        bean.setSpare_6(request.getParameter("spare_6"));
	    }
	    
	    private void handleSuccessfulAssetCreation(HttpServletRequest request, HttpServletResponse response,
	                                               PrintWriter out, AssetRecordsBean assetBean,
	                                               UserSession userSession, SystemConfiguration config) 
	            throws NumberFormatException, Exception {
	        
	        String assetId = assetBean.getAsset_id();
	        int assetCode = assetBean.getAssetCode();
	        LOGGER.info("Asset Code: " + assetCode);
	        
	        // Handle file upload
	        handleFileUpload(request, assetCode, config);
	        
	        // Update invoice information
	        String invoiceNo = request.getParameter("sb") + "-" + request.getParameter("invNo");
	        htmlUtil.insToAm_Invoice_No(assetId, request.getParameter("lpo"), invoiceNo, "Asset Creation");
	        
	        // Get transaction levels
	        int numOfTransactionLevel = assetBean.getNumOfTransactionLevel("1");
	        
	        if (numOfTransactionLevel == 0) {
	            handleNoApprovalRequired(out, assetBean, assetId, assetCode, userSession, config);
	        } else {
	            handleApprovalRequired(request, out, assetBean, assetId, assetCode, userSession, config);
	        }
	    }
	    
	    private void handleFileUpload(HttpServletRequest request, int assetCode, SystemConfiguration config) 
	            throws IOException, ServletException {
	        
	    	Part filePart = request.getPart("file");
	    	if (filePart == null || filePart.getSize() == 0) {
	    	    LOGGER.info("No file uploaded or file is empty");
	    	    return; // Exit gracefully without exception
	    	}

	    	String fileName = filePart.getSubmittedFileName();
	    	LOGGER.info("Uploading file: " + fileName);

	    	// Check if fileName is null or empty
	    	if (fileName == null || fileName.trim().isEmpty()) {
	    	    LOGGER.warning("File name is null or empty");
	    	    return;
	    	}

	    	// Check for invalid file types
	    	if (isInvalidFile(fileName)) {
	    	    LOGGER.warning("Invalid file type: " + fileName);
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
	            LOGGER.log(Level.SEVERE, "Error uploading file", e);
	        }
	    }
	    
	    private boolean isInvalidFile(String fileName) {
	        if (fileName == null) {
	            return true; // Consider null filenames as invalid
	        }
	        
	        String lowerFileName = fileName.toLowerCase();
	        return lowerFileName.endsWith(".php") || 
	               lowerFileName.endsWith(".sql") || 
	               lowerFileName.endsWith(".jsp") ||  // Add other dangerous extensions
	               lowerFileName.endsWith(".exe") ||
	               lowerFileName.endsWith(".sh") ||
	               lowerFileName.endsWith(".bat");
	    }
	    
	    private void handleNoApprovalRequired(PrintWriter out, AssetRecordsBean assetBean,
	                                          String assetId, int assetCode,
	                                          UserSession userSession, SystemConfiguration config) throws NumberFormatException, Exception {
	        
	        if (assetBean.updateNewAssetStatux(assetId)) {
	            // Set up pending transactions
	            assetBean.setPendingTrans(assetBean.setApprovalData(assetId), "1", assetCode);
	            String lastMTID = assetBean.getCurrentMtid("am_asset_approval");
	            assetBean.setPendingTransArchive(assetBean.setApprovalData(assetId), "1", 
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
	            String url = "DocumentHelp.jsp?np=updateAsset&amp;id=" + assetId + "&pageDirect=Y";
	            String userName = approvalRecords.getCodeName("SELECT full_name FROM am_gb_user WHERE user_id=?", 
	                                                          userSession.getUserId());
	            String branchName = approvalRecords.getCodeName("SELECT branch_name FROM am_ad_branch WHERE branch_id=?", 
	                                                            assetBean.getBranch_id());
	            
	            approvalRecords.insertApprovalx(assetId, assetBean.getDescription(), page1, "", 
	                                           assetBean.getFullyPAID(), userName, branchName,
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
	            message = "New asset with ID: " + assetId + " was successfully created.";
	            
	            mailService.sendMail(to, "Asset Creation", message);
	        }
	    }
	    
	    private void handleApprovalRequired(HttpServletRequest request, PrintWriter out,
	                                        AssetRecordsBean assetBean, String assetId, int assetCode,
	                                        UserSession userSession, SystemConfiguration config) {
	        
	        String supervisorID = request.getParameter("supervisor");
	        ArrayList approvers = assetBean.getApprovalsId(userSession.getBranch(), 
	                                                        userSession.getDeptCode(), 
	                                                        userSession.getUserName());
	        
	        if ("Y".equalsIgnoreCase(config.getSingleApproval())) {
	            handleSingleApproval(out, assetBean, assetId, assetCode, supervisorID);
	        } else if ("N".equalsIgnoreCase(config.getSingleApproval())) {
	            handleMultipleApproval(out, assetBean, assetId, assetCode, approvers);
	        }
	    }
	    
	    private void handleSingleApproval(PrintWriter out, AssetRecordsBean assetBean,
	                                      String assetId, int assetCode, String supervisorID) {
	        
	        assetBean.setPendingTrans(assetBean.setApprovalData(assetId), "1", assetCode);
	        String lastMTID = assetBean.getCurrentMtid("am_asset_approval");
	        assetBean.setPendingTransArchive(assetBean.setApprovalData(assetId), "1", 
	                                         Integer.parseInt(lastMTID), assetCode);
	        
	        // Send approval email
	        String subject = "Asset Creation Approval";
	        String message = "Asset with ID: " + assetId + " is waiting for your approval.";
	        String approvalTransId = approvalRecords.getCodeName(
	            "SELECT transaction_id FROM am_asset_approval WHERE ASSET_ID=?", assetId);
	        
	        String supervisorName = approvalRecords.getCodeName(
	            "SELECT full_name FROM am_gb_user WHERE user_id=?", supervisorID);
	        
	        companyHandler.insertMailRecords(supervisorID, subject, message);
	        
	        out.print("<script>");
	        out.print("alert('Asset creation submitted to " + supervisorName + " for approval');");
	        out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId=" + assetId + 
	                  "&previousPage=" + CURRENT_PAGE + "&tran_type=" + TRANSACTION_TYPE + 
	                  "&root=" + ROOT + "&InitPage=New&assetCode=" + assetCode + "';");
	        out.print("</script>");
	    }
	    
	    private void handleMultipleApproval(PrintWriter out, AssetRecordsBean assetBean,
	                                        String assetId, int assetCode, ArrayList approvers) {
	        
	        String mtid = appHelper.getGeneratedId("am_asset_approval");
	        
	        for (int i = 0; i < approvers.size(); i++) {
	            User approver = (User) approvers.get(i);
	            String supervisorId = approver.getUserId();
	            String email = approver.getEmail();
	            
	            assetBean.setPendingTransMultiApp(assetBean.setApprovalData(assetId), "1", 
	                                              assetCode, supervisorId, mtid);
	            
	            String subject = "Asset Creation Approval";
	            String message = "Asset with ID: " + assetId + " is waiting for your approval.";
	            companyHandler.insertMailRecords(email, subject, message);
	        }
	        
	        out.print("<script>");
	        out.print("alert('Asset creation submitted for approval');");
	        out.print("window.location='" + DESTINATION_PAGE + "&id=" + assetId + 
	                  "&tran_status=A&tran_type=" + TRANSACTION_TYPE + 
	                  "&assetId=" + assetId + "&reason=&pPage=" + CURRENT_PAGE + "';");
	        out.print("</script>");
	    }
	    
	    private void handleBudgetExceededError(PrintWriter out) {
	        out.print("<script>");
	        out.print("alert('Addition of Asset will overshoot quarterly budget for this category and you are not allowed to do so. Please exit and seek supplementary budgetary Allocation');");
	        out.print("window.location='DocumentHelp.jsp?np=newAsset&id';");
	        out.print("</script>");
	    }
	    
	    private void handleGeneralError(PrintWriter out) {
	        out.print("<script>");
	        out.print("alert('Records cannot be saved! Your Asset does not fall in any budget quarter.');");
	        out.print("window.location='DocumentHelp.jsp?np=newAsset&id';");
	        out.print("</script>");
	    }
	    
	    private void handleError(PrintWriter out, String message) {
	        out.print("<script>");
	        out.print("alert('" + message + "');");
	        out.print("window.history.back();");
	        out.print("</script>");
	    }
	    
	    private void redirectToUploadPage(PrintWriter out, String assetId, int assetCode) {
	        out.print("<script>");
	        out.print("alert('Asset creation successful');");
	        out.print("window.location='DocumentHelp.jsp?np=uploadImage&assetId=" + assetId + 
	                  "&previousPage=" + CURRENT_PAGE + "&tran_type=" + TRANSACTION_TYPE + 
	                  "&root=" + ROOT + "&InitPage=New&assetCode=" + assetCode + "';");
	        out.print("</script>");
	    }
	    
	    // Inner classes for better organization
	    private static class UserSession {
	        private String userId;
	        private String userClass;
	        private String userName;
	        private String branch;
	        private String deptCode;
	        private String branchRestrict;
	        private String deptRestrict;
	        private String branchRestricted;
	        private String systemIp;
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			public String getUserClass() {
				return userClass;
			}
			public void setUserClass(String userClass) {
				this.userClass = userClass;
			}
			public String getUserName() {
				return userName;
			}
			public void setUserName(String userName) {
				this.userName = userName;
			}
			public String getBranch() {
				return branch;
			}
			public void setBranch(String branch) {
				this.branch = branch;
			}
			public String getDeptCode() {
				return deptCode;
			}
			public void setDeptCode(String deptCode) {
				this.deptCode = deptCode;
			}
			public String getBranchRestrict() {
				return branchRestrict;
			}
			public void setBranchRestrict(String branchRestrict) {
				this.branchRestrict = branchRestrict;
			}
			public String getDeptRestrict() {
				return deptRestrict;
			}
			public void setDeptRestrict(String deptRestrict) {
				this.deptRestrict = deptRestrict;
			}
			public String getBranchRestricted() {
				return branchRestricted;
			}
			public void setBranchRestricted(String branchRestricted) {
				this.branchRestricted = branchRestricted;
			}
			public String getSystemIp() {
				return systemIp;
			}
			public void setSystemIp(String systemIp) {
				this.systemIp = systemIp;
			}
	        
	        
	    }
	    
	    private static class SystemConfiguration {
	        private String thirdPartyLabel;
	        private String singleApproval;
	        private String vatRequired;
	        private String sbuRequired;
	        private String uploadFolder;
	        
	        
			public String getThirdPartyLabel() {
				return thirdPartyLabel;
			}
			public void setThirdPartyLabel(String thirdPartyLabel) {
				this.thirdPartyLabel = thirdPartyLabel;
			}
			public String getSingleApproval() {
				return singleApproval;
			}
			public void setSingleApproval(String singleApproval) {
				this.singleApproval = singleApproval;
			}
			public String getVatRequired() {
				return vatRequired;
			}
			public void setVatRequired(String vatRequired) {
				this.vatRequired = vatRequired;
			}
			public String getSbuRequired() {
				return sbuRequired;
			}
			public void setSbuRequired(String sbuRequired) {
				this.sbuRequired = sbuRequired;
			}
			public String getUploadFolder() {
				return uploadFolder;
			}
			public void setUploadFolder(String uploadFolder) {
				this.uploadFolder = uploadFolder;
			}
	        
	        
	    }
	
	    
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}