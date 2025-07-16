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
public class UploadAssetDetailServlet extends HttpServlet
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
		   int AssetCode = 0;
		   String errorlog = "";
		   String ttype = "Upload";
		   double minAssetCost = 1.00;
		   double maxAssetCost = 5000.00;
		   String mtid= "";
		   //groupid = approvalRec.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'am_group_asset_main'");
		   groupid = groupid+usid;
		   String catRate = approvalRec.getCodeName("select dep_rate from AM_GROUP_ASSET where Group_id = '"+groupid+"'");
		    
		   //System.out.print("groupid==>>>>>>>>: "+groupid+"    catRate: "+catRate);
		   String c = request.getParameter("init");
		   int co=0;
		   if(c!=null)  co = Integer.parseInt(c);
		   if(request.getParameter("btnDelete")!=null){

		   for(int i=0;i<=co;i++){
		   String asid = request.getParameter("asset"+i);
		   if(asid!=null){
		   groupM.deleteUploadGAsset(asid,groupid);
		   }
		   }
		   out.print("<script>");
		   out.print("alert('Records deleted successfully');");
		   out.print("window.location='DocumentHelp.jsp?np=groupExcelUpload'");
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
		   String id = request.getParameter("id");
		   int numOfTransactionLevel =  ad.getNumOfTransactionLevel("24");
		   //System.out.print("numOfTransactionLevel==  "+numOfTransactionLevel);
		   String fromDate = request.getParameter("FromDate");
		   String toDate = request.getParameter("ToDate");
		   String strStages = (String)session.getAttribute("GCurrentStage");
		   String Code = "26";   
		   String returnPage = "uploadAssetDetail";   
		   String tranType = "Asset Upload";
		   String branchcode = ""; 

		   String UserBranch =approvalRec.getCodeName("select branch from am_gb_User where user_id='"+userid+"'");
		   System.out.print("====UserBranch: "+UserBranch);
		   String total = approvalRec.getCodeName("SELECT SUM(COST_PRICE) FROM AM_GROUP_ASSET WHERE GROUP_ID='"+groupid+"' AND USER_ID="+userid+" AND PROCESS_FLAG='N'");
		   System.out.print("====total: "+total);
		   if(total==""){total =  "0.00";}
		   double totalcost = Double.parseDouble(total);
		   //System.out.print("====totalcost: "+totalcost);
		   String errorNo = approvalRec.getCodeName("select count(*) from am_uploadCheckErrorLog where TRANSACTION_TYPE='Asset Creation'");
		   //System.out.print("====groupid Next 1: "+groupid);
		
		   // System.out.print("====groupid Next 2: "+groupid);
		   String ProcessFlag =approvalRec.getCodeName("Select batch_id from am_asset_approval where BATCH_ID='"+groupid+"' and user_id = "+userid+"");
		   //System.out.print("====groupid Next 3: "+groupid);
		   String dp ="DocumentHelp.jsp?np=uploadCheckError&groupid="+groupid+"&returnPage="+returnPage+"&userid="+userid+"&tranType="+tranType+"&returnPage="+returnPage; 
		   //String dp ="DocumentHelp.jsp?np=uploadCheckError&groupid="+groupid+"&groupid="+returnPage+"&returnPage="+userid; 
		   //System.out.print("userid=== "+userid);
		   //System.out.print("ProcessFlag=== "+ProcessFlag);
		   //System.out.print("UserBranch=== "+UserBranch);
		   //System.out.print("supervisorID=== "+supervisor);
		   //String sup_name_qry="select full_name from am_gb_user where user_id=(select supervisor from am_group_asset_main where group_id="+ last+")";
		   //String Supervisor_name =approvalRec.getCodeName("select FULL_NAME from am_gb_User where user_id='"+supervisorID+"'");
		   //String Supervisor_name = adGroup.getUnprocessedGroupAsset(sup_name_qry).toUpperCase();
		   String searchQuery = "";
		    String init = "0";
		   boolean  Forward = false;

		    legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		   String userName = user.getUserName();
		   String branchRestrict = user.getBranchRestrict();
		   String UserRestrict = user.getDeptRestrict();
		   String departCode = user.getDeptCode();
		   branch = user.getBranch();

		    EmailSmsServiceBus mail = new EmailSmsServiceBus();
		    
		     java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		     
		     com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
		     
		    System.out.print("INSIDE LOOP btnApply 1 "+btnApply);
		   if(strStages == null){strStages = "0";}
		   if(status == null){status = "ACTIVE";}
		   if(fromDate == null){fromDate = "";}
		   if(toDate == null){toDate ="";}
		   if(assetId == null){assetId = "";}
		   if(branch == null){branch = (String)session.getAttribute("UserCenter");}
		   
		   if(btnApply != null){   System.out.print("INSIDE LOOP btnApply 2 "+btnApply);
//			if(Forward != true){Forward = adGroup.RecordProcess(groupid);}
//			System.out.print("catRate Value: "+catRate);
//			System.out.print("=====groupid Value: "+groupid);
			if(Forward != true){Forward = adGroup.RecordProcess(groupid,catRate);}
//			System.out.print("BEFORE Update: "+id);
//			int AssetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
//			System.out.print("ASSET CODE GENERATED: "+AssetCode);
			boolean Update = adGroup.UploadRecordsSelection(Code,id,AssetCode);
//			System.out.print("AFTER Update: "+Update);
//				System.out.print("INSIDE LOOP 0"+Forward);
				try{ //System.out.print("INSIDE LOOP 1");
				//String  last ="N";
					if(Forward == true)
					{ // System.out.print("INSIDE LOOP 2");
				 	String newAssetID = adGroup.getAsset_id_new();
					String vatable_cost = adGroup.getVatable_cost();
					String last_id = adGroup.getGroup_id();
					String vat_cost_bal = adGroup.getVatable_cost_balance();
					String count_qry ="select count(*) from am_group_asset where process_flag='N' and Group_id = "+ last_id;
					String unprocessedGrpAssets = adGroup.getUnprocessedGroupAsset(count_qry);
					//adGroup.updateVatableCostBalance(vat_cost_bal,vatable_cost,last_id,unprocessedGrpAssets);
//					System.out.println("New asset_ID : " + newAssetID);
					int chkCount=Integer.parseInt(unprocessedGrpAssets);
//					System.out.println("chkCount >>>>> " + chkCount);
					String Supervisor_name = "";
					if ((numOfTransactionLevel != 0)&&(Forward == true))
					{   
					 if(singleApproval.equalsIgnoreCase("Y")){
					 Supervisor_name =approvalRec.getCodeName("select FULL_NAME from am_gb_User where user_id='"+supervisor+"'"); 
//						System.out.println("Asset Id >>>>> " + id);
//						System.out.println("supervisor Id >>>>> " + supervisor);
					//	ad.setUploadPendingTrans(Integer.parseInt(SuperId),Code,id,AssetCode);
//						System.out.println("groupid Id /////>>>>> " + groupid);
						ad.updateUncapitalizedAssetPendingTrans(groupid);  				  
						String subjectr ="Capitalized Asset Upload Approval";
						String msgText11 ="Capitalized Asset with GROUP ID: "+ last +" is waiting for your approval.";
						String otherprogram = "";
//						mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherprogram);
								  
					out.print("<script>");
					out.print("alert('Group Asset Capitalized Has Been Sent To  "+ Supervisor_name + "  For Approval');");
					out.print("window.location='DocumentHelp.jsp?np=uploadAssetDetail' ");
//					out.print("window.location='DocumentHelp.jsp?np=uploadAssetDetail&pageDirect=d&gid="+last_id+"&root=group&id="+newAssetID+"';");	
//					out.print("window.location='DocumentHelp.jsp?np=uploadImage&previousPage="+returnPage+"&assetCode="+groupid+"';");		
					
					out.print("</script>");
					//out.print(msg); 
					ad.setUpdateUploadTrans(groupid,userid);
					}
					}
					else{
						ad.setUploadCompleteTrans(id);
						ad.setUpdateUploadTrans(groupid,userid);
					out.print("alert('Upload Assets Have Been Created);");
					}
					}
							}catch(Exception e){
				e.printStackTrace();
					out.print("<script>alert('Unable to create group assets');history.go(-1)</script>");
				}
				}  	

		   Part filePart = request.getPart("file");
		   
		   String destination = "DocumentHelp.jsp?np=updateAssetView";
		 	
		 	
		 	//System.out.println("The destination is ==: " + destination);
		 	
			 String fileName = filePart.getSubmittedFileName();
			 System.out.println("fileName: " + fileName);


		           if(!btnApply.equals(null)){   
		   //  System.out.println("groupid Id >>>>> " + groupid);
		        	   System.out.println("btnApply >>>>> " + btnApply);
		       double Amount = Double.parseDouble(approvalRec.getCodeName("select sum(cost_price) from AM_GROUP_ASSET where group_id = '"+groupid+"'"));
//		   	System.out.println("Amount Id >>>>> " + Amount);
		   	//double formatAmount = formata.formatAmount(Double.toString(Amount));
		   	Amount = 0.00;
		     int quantity = Integer.parseInt(approvalRec.getCodeName("select count(asset_id) from AM_GROUP_ASSET where group_id = '"+groupid+"'"));  
		   //  System.out.println("quantity  >>>>> " + quantity);
		     String description = "Capitalized Asset Upload";
		     String Tran_Type = "Asset Upload";
		     
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
		   /*  ad.setUploadUncapitalPendingTrans(Integer.parseInt(supervisor),Code,groupid,AssetCode,Integer.parseInt(userid),Amount,description,UserBranch,Tran_Type);	
		    ad.updateamgroupassetmainTotal(quantity,groupid,Amount, Integer.parseInt(supervisor),branchcode,Integer.parseInt(userid),deptcode);  */
		    if(singleApproval.equalsIgnoreCase("Y")){
		     String branchId =approvalRec.getCodeName("select branch from am_gb_User where user_id='"+supervisor+"'"); 
		      branchcode =approvalRec.getCodeName("select BRANCH_CODE from am_ad_branch where branch_id = '"+branch+"'");
		   //  String deptcode =approvalRec.getCodeName("select dept_code from am_gb_User where user_id='"+supervisor+"'");	
		   //  System.out.println("Amount >>>>> "+Amount+" supervisor: "+supervisor);
		      
		      
		       
		   ad.setUploadPendingTrans(Integer.parseInt(supervisor),Code,groupid,AssetCode,Integer.parseInt(userid),Amount,description,UserBranch,Tran_Type);	
		    ad.updateamgroupassetmainTotal(quantity,groupid,Amount, Integer.parseInt(supervisor),branchcode,Integer.parseInt(userid),departCode);
		   String Supervisor_name =approvalRec.getCodeName("select FULL_NAME from am_gb_User where user_id=? ",supervisor);
		   	String subjectr ="Capitalized Asset Upload Approval";
		   	String msgText11 ="Capitalized Asset with GROUP ID: "+ last +" is waiting for your approval.";
		    			String  approvaltransId  = approvalRec.getCodeName("select transaction_id from am_asset_approval where batch_id = ?",groupid);	  
		   			String otherparam = "UploadAssetApproval&operation=1&id=0&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode=0";				
		   				mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);
		   						  
		   			out.print("<script>");
		   			out.print("alert('Upload Asset Has Been Sent To  "+ Supervisor_name + "  For Approval');");
		   		//	out.print("window.location='DocumentHelp.jsp?np=uploadAssetDetail' ");	
		   			out.print("window.location='DocumentHelp.jsp?np=groupExcelUpload';");
		   			out.print("</script>");
		   		//	out.print("window.location='DocumentHelp.jsp?np=uploadImage&previousPage="+returnPage+"&assetCode="+groupid+"';");				
		   	}
		   				
		   		 if(singleApproval.equalsIgnoreCase("N")){
		   			  String tableName = "AM_GROUP_ASSET";  
		   	  		mtid =  appHelper.getGeneratedId("am_asset_approval");
		   	branchcode =approvalRec.getCodeName("select BRANCH_CODE from am_ad_branch where branch_id = '"+branch+"'");
//		   	   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
		   	   	 for(int j=0;j<approvelist.size();j++)
		   	     {  
//		   		 	System.out.println("J #$$$$$$$$$$$ "+j);
		   		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
		   			String supervisorId =  usr.getUserId();
		   			String mailAddress = usr.getEmail();
		   			String supervisorName = usr.getUserName();
		   			String supervisorfullName = usr.getUserFullName();
//		   			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
		   	  	group.setGroupPendingMultipleTrans(ad.setApprovalDataUploadGroup(Long.parseLong(groupid),tableName),"24",AssetCode,supervisorId,mtid);
		   			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//		   			 System.out.println("lastMTID#$$$$$$$$$$$ "+lastMTID);
		   			 if(supervisor==null){supervisor =  "0";}
		    ad.updateamgroupassetmainTotal(quantity,groupid,Amount, Integer.parseInt(supervisor),branchcode,Integer.parseInt(userid),departCode);
		    		
		   	String subjectr ="Capitalized Asset Upload Approval";
		   	String msgText11 ="Capitalized Asset with GROUP ID: "+ last +" is waiting for your approval.";	
//		   			System.out.println("mailAddress#$$$$$$$$$$$ "+mailAddress);		
		   				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
		   				 ad.setUpdateUploadTrans(groupid,userid);
		   				}
//		   			out.print("<script>");
//		   			out.print("alert('Upload Asset with ID: "+ last +" Has Been Sent For Approval');");
//		   			out.print("window.location='DocumentHelp.jsp?np=uploadAssetDetail' ");	
		   			
		   			out.print("<script>");
		   			out.print("alert('Upload Asset with ID: "+ groupid +" Has Been Sent For Approval');");
		   			out.print("window.location='DocumentHelp.jsp?np=groupExcelUpload';");
		   	//		out.print("window.location='DocumentHelp.jsp?np=uploadAssetDetail&pageDirect=d&gid="+groupid+"&root=group&id="+groupid+"';");
//		   			out.print("window.location='DocumentHelp.jsp?np=uploadImage&previousPage="+returnPage+"&assetCode="+groupid+"';");			
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