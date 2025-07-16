package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import magma.AssetRecordsBean;
import magma.GroupAssetBean;
import magma.GroupAssetToAssetBean;
import magma.net.manager.GroupAssetManager;
import magma.net.manager.GroupAssetPaymentManager;
import magma.net.manager.SytemsManager;
import magma.util.Codes;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DatetimeFormat;


@MultipartConfig(
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class UploadAssetDisposalDetailServlet extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	
	private ApprovalRecords records;
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords approvalRec  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	   try {
		   Properties prop = new Properties();
		   File file = new File("C:\\Property\\LegendPlus.properties");
		   FileInputStream input = new FileInputStream(file);
		   prop.load(input);

		   HttpSession session = request.getSession();
		   PrintWriter out = response.getWriter();
		   AssetRecordsBean ad = new AssetRecordsBean();
		   GroupAssetToAssetBean adGroup = new GroupAssetToAssetBean();
		   String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
		   String singleApproval = prop.getProperty("singleApproval");
		   System.out.println("singleApproval: " + singleApproval);

		   String userid = (String)session.getAttribute("CurrentUser");
		   System.out.print("userid: "+userid);
		   GroupAssetManager groupManager = new GroupAssetManager();
		   GroupAssetPaymentManager groupM = new GroupAssetPaymentManager();
		   GroupAssetBean group = new GroupAssetBean();
		   String usid = (String) session.getAttribute("CurrentUser");
		   DatetimeFormat dateFormat = new DatetimeFormat();
		   java.util.ArrayList list = new java.util.ArrayList();
		   String last = request.getParameter("gid");
		   String groupid = request.getParameter("gid");
		   System.out.print("groupid: "+groupid);
		   int AssetCode = Integer.valueOf(request.getParameter("assetCode")) == 0 ? 0 : Integer.valueOf(request.getParameter("assetCode"));
		   String errorlog = "";
		   String ttype = "Upload";
		   double minAssetCost = 1.00;
		   double maxAssetCost = 5000.00;
		   String mtid= "";
		 //groupid = approvalRec.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'am_group_asset_main'");
		 //String catRate = approvalRec.getCodeName("select dep_rate from AM_GROUP_ASSET where Group_id = '"+groupid+"'");
		 //System.out.print("groupid==>>>>>>>>: "+groupid+"    catRate: "+catRate);
		// groupid = groupid+usid;
		 String c = request.getParameter("init");
		 int co=0;
		 if(c!=null)  co = Integer.parseInt(c);
		 if(request.getParameter("btnDelete")!=null){

		 for(int i=0;i<=co;i++){
		 String asid = request.getParameter("asset"+i);
		 if(asid!=null){
		 groupM.deleteUploadGAssetDisposal(asid,groupid);
		 }
		 }
		 out.print("<script>");
		 out.print("alert('Records deleted successfully');");
		 out.print("window.location='DocumentHelp.jsp?np=groupExcelDisposalUpload' ");
		 out.print("</script>");
		 }
		 String supervisor = request.getParameter("supervisor");
		 String category = request.getParameter("CATEGORY");
		 String operation = request.getParameter("operation");
		 String direction = request.getParameter("DIR");
		 String assetId = request.getParameter("ASSET_ID");
		 String branch = request.getParameter("BRANCH_ID");
		 String deptCode = request.getParameter("DEPT_CODE");
		 String registrationNumber = request.getParameter("REG_NO");
		 String status = request.getParameter("STATUS");
		 String btnApply = request.getParameter("btnApply");
		 int numOfTransactionLevel =  ad.getNumOfTransactionLevel("24");
		 //System.out.print("numOfTransactionLevel==  "+numOfTransactionLevel);
		 String fromDate = request.getParameter("FromDate");
		 String toDate = request.getParameter("ToDate");
		 String strStages = (String)session.getAttribute("GCurrentStage");
		 String Code = "74";   
		 String Supervisor_name = ""; 
		 String returnPage = "uploadAssetDisposalDetail";   
		 String tranType = "Asset Disposal Upload";
		 String UserBranch =approvalRec.getCodeName("select branch from am_gb_User where user_id= ?",userid);
		 String errorNo = approvalRec.getCodeName("select count(*) from am_uploadCheckErrorLog where TRANSACTION_TYPE='Asset Disposal Upload'");
		 if(!errorNo.equalsIgnoreCase("0")){
		  errorlog = "There is Error. Click to Check Error Log";
		  }
		 String ProcessFlag =approvalRec.getCodeName("Select batch_id from am_asset_approval where BATCH_ID='"+groupid+"' and user_id = "+userid+"");
		 String dp ="DocumentHelp.jsp?np=uploadCheckError&groupid="+groupid+"&returnPage="+returnPage+"&userid="+userid+"&tranType="+tranType; 
		    legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		   String userName = user.getUserName();
		   String branchRestrict = user.getBranchRestrict();
		   String UserRestrict = user.getDeptRestrict();
		   String departCode = user.getDeptCode();
		   branch = user.getBranch();

		    EmailSmsServiceBus mail = new EmailSmsServiceBus();
		    
		     java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		     
		     com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
		     
		     	Part filePart = request.getPart("file");
			 	
				 String fileName = filePart.getSubmittedFileName();
				 System.out.println("fileName: " + fileName);
		     
		     if(btnApply != null)
		     {   

		    	 prop.load(input);
	            String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
	            String uploadPath = UPLOAD_FOLDER;
	            
	            File uploadDir = new File(uploadPath);
	            if(!uploadDir.exists())
	            {
	                uploadDir.mkdir();
	            }
	            try
	            {
	            	 if(!fileName.endsWith(".php") || !fileName.endsWith(".sql")) {
	            	 for (Part part : request.getParts()) {
	                        
	                        String ext = FilenameUtils.getExtension(fileName);
	                    //    System.out.println("The extension is =>: " + ext);
	                        
	                        String filePath = uploadPath + "W" + fileName.toString();
	                       // System.out.println("The file path is ==: " + filePath);
	                        
	                        String newPath = uploadPath + "W" + groupid + "." + ext;
//	                        System.out.println("The new file path is >=: " + newPath);
	                        
	                       
	                        File oldFile = new File(filePath);
	                        
	                        File newFile = new File(newPath);
	                        
	                        String newFileName = newFile.getName().substring(0, newFile.getName().lastIndexOf("."));
	                        
	                      //  System.out.println("newFileName >=: " + newFileName);
	                        
	                        File folder = new File(UPLOAD_FOLDER);
	            	        File[] listOfFiles = folder.listFiles();
	            	        
	            	       String listFiles = Arrays.toString(listOfFiles);
	            	       	       
	                        
	                        oldFile.renameTo(newFile);

	                        part.write(newPath);
	                        
	                       // System.out.println("uploaded successfully.");
	                  
	        	    }
	            	 
	               // System.out.println("The destination is ==: " + destination);
	               System.out.println("File Upload was successful!");

	                
	                }else {
	                	System.out.println("Incorrect File..");
	                }
	            }
	            catch(Exception ex)
	            {
	                System.out.println((new StringBuilder("There was an error: ")).append(ex.getMessage()).toString());
	              
	            }
	            double Amount = Double.parseDouble(approvalRec.getCodeName("select sum(disposalCost) from AM_GROUP_DISPOSAL where DISPOSAL_ID = '"+groupid+"'"));
	        	//double formatAmount = formata.formatAmount(Double.toString(Amount));
	        	Amount = 0.00;
	          int quantity = Integer.parseInt(approvalRec.getCodeName("select count(asset_id) from AM_GROUP_DISPOSAL where disposal_ID = '"+groupid+"'"));  
	            String description = "Asset Disposal Upload";
	          String Tran_Type = "Asset Disposal Upload";
	          if(singleApproval.equalsIgnoreCase("Y")){
	          String branchId =approvalRec.getCodeName("select branch from am_gb_User where user_id='"+supervisor+"'");
	            String branchcode =approvalRec.getCodeName("select BRANCH_CODE from am_ad_branch where branch_id = ?",branchId);
	          String deptcode =approvalRec.getCodeName("select dept_code from am_gb_User where user_id=?",supervisor);	
	        //  System.out.println("Amount >>>>> "+Amount+" supervisor: "+supervisor);

//	        				System.out.println("singleApproval Is Yes >>>>> "+singleApproval);
	          ad.setUploadPendingTrans(Integer.parseInt(supervisor),Code,groupid,AssetCode,Integer.parseInt(userid),Amount,description,UserBranch,Tran_Type);	
	        				ad.updateAssetPendingTrans(groupid);  
	        				ad.updateamgroupassetDisposal(groupid);				  
	        			//	mail.sendMailSupervisor(supervisor, subjectr, msgText11);								  
	        			out.print("<script>");
	        			out.print("alert('Upload Asset Disposal Has Been Sent To  "+ Supervisor_name + "  For Approval');");
//	        			out.print("window.location='DocumentHelp.jsp?np=uploadAssetDisposalDetail&pageDirect=d&gid="+groupid+"&root=group&id="+groupid+"';");
	        			//out.print("window.location='DocumentHelp.jsp?np=uploadImage&previousPage="+returnPage+"&assetCode="+groupid+"';");			
	        			out.print("window.location='DocumentHelp.jsp?np=groupExcelDisposalUpload';");
	        			out.print("</script>");
	        			//out.print(msg); 
	        			ad.setUpdateUploadTrans(groupid,userid);
	        			
	        				}
	        				
	        		 if(singleApproval.equalsIgnoreCase("N")){
	        	 		  ad.updateAssetPendingTrans(groupid);
	        			  String tableName = "AM_GROUP_DISPOSAL";
	        	  		mtid =  appHelper.getGeneratedId("am_asset_approval");
//	        	   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
	        	   	 for(int j=0;j<approvelist.size();j++)
	        	     {  
//	        		 	System.out.println("J #$$$$$$$$$$$ "+j);
	        		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
	        			String supervisorId =  usr.getUserId();
	        			String mailAddress = usr.getEmail();
	        			String supervisorName = usr.getUserName();
	        			String supervisorfullName = usr.getUserFullName();
//	        			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
	        	  	group.setGroupPendingMultipleTrans(ad.setApprovalDataUploadGroup(Long.parseLong(groupid),tableName),"74",AssetCode,supervisorId,mtid);
	        			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//	        		 ad.setPendingMultiApprTransArchive(ad.setApprovalData(id),"1",Integer.parseInt(lastMTID),assetCode,supervisorId);
	        //   String changeStatus="update am_asset_approval set tran_type = 'Asset Disposal' where asset_id='"+id+"'";
	        //   ad.updateAssetStatusChange(changeStatus);
	        		   	ad.updateamgroupassetDisposal(groupid);
	        			String subjectr ="Upload Asset Disposal Approval";
	        			String msgText11 ="Upload Asset with ID: "+ groupid +" for Disposal is waiting for your approval.";
	         
	        //String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
	        				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
	        				ad.setUpdateUploadTrans(groupid,userid);
	        				}
	        			out.print("<script>");
	        			out.print("alert('Upload Asset Disposal with ID: "+ groupid +" Has Been Sent For Approval');");
//	        			out.print("window.location='DocumentHelp.jsp?np=uploadAssetDisposalDetail&pageDirect=d&gid="+groupid+"&root=group&id="+groupid+"';");
	        			//out.print("window.location='DocumentHelp.jsp?np=uploadImage&previousPage="+returnPage+"&assetCode="+groupid+"';");
	        			out.print("window.location='DocumentHelp.jsp?np=groupExcelDisposalUpload';");
	        			out.print("</script>");
	        						 
	        	     
	        	  	 }		
	        	}
		   		 
	 }catch(Throwable e) {
		 e.getMessage();
	 }
	 
	
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}