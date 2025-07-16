/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package legend;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import legend.admin.handlers.CompanyHandler;
import legend.admin.handlers.SecurityHandler;
import magma.AssetRecordsBean;
import magma.GroupAssetToAssetBean;
import magma.asset.manager.AssetManager;
import magma.AssetReclassificationBean;
import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.legend.bus.ApprovalManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import magma.net.manager.BulkUpdateManager;
import magma.net.manager.BulkUpdateUncapitalisedManager;
import  magma.asset.manager.WIPAssetManager;

//import magma.net.manager.AssetManager;
/**
 *
 * @author Olabo
 */
public class ApprovalServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        AssetManager asset_manager = new AssetManager();
        ApprovalRecords aprecords = new ApprovalRecords();
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        MagmaDBConnection dbConnection = new MagmaDBConnection();
        SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
        String disposaltype = "";
        String userClass = (String)request.getSession().getAttribute("UserClass");
        
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");
 //           System.out.println("the assetRaiseEntry is >>>>>>>>>>>>>>>>> " + assetRaiseEntry);
int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
            //--System.out.println("\nthe assetCode is >>>>>>>>>>>>>>>>> " + assetCode);
            ApprovalManager approverManager = new ApprovalManager();
            AssetReclassificationBean asset_reclassification = new AssetReclassificationBean();
            AssetRecordsBean arb = new AssetRecordsBean();
            CompanyHandler comp = new CompanyHandler();
            com.magbel.util.ApplicationHelper appHelper = new com.magbel.util.ApplicationHelper();
            GroupAssetToAssetBean grpAsset = new GroupAssetToAssetBean();
            
            String astatus = request.getParameter("astatus");
            String singleApproval = request.getParameter("singleApproval");
            String newCategoryID = request.getParameter("newCategoryID");
            String newEffDate = request.getParameter("newEffDate");
            String full_status = request.getParameter("full_status");
            String asset_id = request.getParameter("id") == null ? "" : request.getParameter("id");
            String assetId = request.getParameter("assetId") == null ? "" : request.getParameter("assetId");
//            System.out.println("<<<<<<full_status: "+full_status+"   astatus: "+astatus);
            String rr = request.getParameter("reject_reason");
            String tableName = request.getParameter("tableName") == null ? "" : request.getParameter("tableName");
            String columnName = request.getParameter("columnName");
            String category = request.getParameter("category");
            String categoryName = request.getParameter("categoryName");
            String returnPage = "transactionForApprovalList";
            String batchId = request.getParameter("batchId");
            String processStatus = request.getParameter("processStatus");
            System.out.println("here Is >>>>>>batchId: " +batchId);
            
            //for asset transfer
            String newBranch = request.getParameter("newBranch");
            String newDept = request.getParameter("newDept");
            String newSec = request.getParameter("newSection");

            //for asset reclassification
            String branch = request.getParameter("branch_id");
            String dept = request.getParameter("department_id");
            String section = request.getParameter("section_id");
            String newCategory = request.getParameter("new_category");
            String systemIp = request.getRemoteAddr();

            String newAssetID = request.getParameter("newAssetID");
 //           System.out.println("here >>>>>>Approval tranId " +request.getParameter("tranId"));
            int tranId = Integer.parseInt(request.getParameter("tranId"));
 //           System.out.println("here >>>>>>Approval transactionId " +request.getParameter("transactionId"));
            int transactionId = Integer.parseInt(request.getParameter("transactionId"));
//            System.out.println("tranId here >>>>>>>>>>>>>>>>>>>> "+ tranId+" transactionId: "+transactionId);
            String prooftranId = request.getParameter("prooftranId");
//            System.out.println("here >>>>>>>>Approval prooftranId " +prooftranId);
            
            int tranLevel = Integer.parseInt(request.getParameter("tranLevel"));

            int approvalCount = Integer.parseInt(request.getParameter("approvalCount"));

            int userId = Integer.parseInt(request.getParameter("user_id"));
            System.out.println("userId here >>>>> "+ userId+" approvalCount: "+approvalCount+" tranLevel: "+tranLevel);
            String userid = request.getParameter("user_id");
            
            String partialtype = request.getParameter("issuable");
            
            String newAssetId = request.getParameter("asset_id");
            String destination = request.getParameter("destination");
 //           System.out.println("tranLevel ================== "+tranLevel+"    approvalCount: "+approvalCount);
            String old_asset_id = request.getParameter("assetIds");
            String newDeptID = request.getParameter("newDept");
            String newBranchID = request.getParameter("newBranch"); 
            String newSectionID = request.getParameter("newSection");
            String newAssetUser = request.getParameter("newUser");
            String newSbuCode = request.getParameter("newsbucode");
            String newbranchcode = request.getParameter("newbranchcode");
            String newdeptcode = request.getParameter("newdeptcode");
            String newscetioncode = request.getParameter("newscetioncode");
            String Description = request.getParameter("description");
            String costPrice = request.getParameter("COST");
            if(costPrice == null || costPrice.equalsIgnoreCase("")){costPrice = "0";}
            costPrice = costPrice.replaceAll(",",""); 
//            System.out.println("here >>>>>>>costPrice " +costPrice);
            int supervisor = (request.getParameter("supervisor") == null) ? 0 : Integer.parseInt(request.getParameter("supervisor"));
            String alertmessage = "Transaction Approved";
//             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>Approval full_status " +full_status);
//             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>astatus " +astatus);            
             String approvalRequired = request.getParameter("approvalRequired");
//             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>approvalRequired " +approvalRequired);  
            if (supervisor == 0) {
                supervisor = userId;
            }
            int currentSupervisor = approvalCount;
            currentSupervisor += 1;
            String sup = "approval" + currentSupervisor;
            String supervisorUpdate = "update am_asset_approval set " + sup + " = " + supervisor + " where transaction_id = " + tranId;

           //-- String[] raiseEntryInfo = aprecords.raiseEntryInfo(asset_id);

                        String[] raiseEntryInfo=null;

            String branch_id = "";
            String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Integer.toString(tranId));

            if(oldAssetStatus != null && oldAssetStatus.equalsIgnoreCase("RP")){

                raiseEntryInfo = aprecords.raiseEntryInfoRepost(asset_id);
               branch_id = arb.getCodeName("select old_branch_id from am_assettransfer where transfer_id="+Integer.toString(tranId));
            }else{
            raiseEntryInfo = aprecords.raiseEntryInfo(asset_id);
            branch_id = raiseEntryInfo[1];
            }
            String description = raiseEntryInfo[0];
            //String branch_id = raiseEntryInfo[1];
            int branchID = branch_id == null || branch_id.equals("") ? 0 : Integer.parseInt(branch_id);
            String subject_to_vat = raiseEntryInfo[2];
            String wh_tax = raiseEntryInfo[3];
            String flag = "";
            String partPay = "";
            String asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
            String branchName = aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
 //           System.out.println("<<<<<<<<<approveddate: "+approveddate);
            ApprovalRemark approvalRemark = new ApprovalRemark(asset_id, userId, systemIp, tranId, "");
            if (tableName.equals(""))
            {
                if (astatus.equalsIgnoreCase("A"))
                {
//Approval for new asset creation
                    arb.updateAssetStatusApproval(tranId);
                    arb.updateAssetStatus(asset_id);

                }//inner if
            }
            else
            {
                if (astatus.equalsIgnoreCase("A"))
                {
//                     System.out.println(" here>>>>>>>>>>>>>>>>>>>>> 0.0");
                    approvalCount += 1;

                    if (full_status.equalsIgnoreCase("Disposed"))
                    {
                        // approvalCount += 1;

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Disposal");
                        
                        arb.deleteOtherSupervisorswithAssetId(asset_id,userid);
//                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        aprecords.updateRaiseEntry(asset_id);
                        }
                        if (approvalCount == tranLevel && transAvailable != "0")
                        { 
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL

                            String page = "ASSET DISPOSAL RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=disposeAssetRaiseEntry&id=" + asset_id + "&operation=1&prevPage=raiseEntry&pageDirect=Y";
                   //         System.out.println("u");
//                             System.out.println("Disposal in equality ==========================================="+asset_id+"   partialtype: "+partialtype+"  disposaltype: "+disposaltype+"   tranId: "+tranId+"   approveddate: "+approveddate+"   batchId: "+batchId);
                            if(partialtype.equalsIgnoreCase("P")){disposaltype = "PARTIALLY DISPOSED";}
                            else{disposaltype = "DISPOSED";}

                            String q = "update am_asset_approval set process_status='A', asset_status='"+disposaltype+"',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            //--String q2 = "update am_AssetDisposal set disposal_status='D' where asset_id = '"+asset_id+"'";
                            //--arb.updateAssetStatusChange(q2);

                            //--String q3 = "update am_asset set Asset_Status='Disposed' where asset_id = '"+asset_id+"'";
                            //--arb.updateAssetStatusChange(q3);

                           // String q3 = "update am_asset set Asset_Status='PARTIALLY DISPOSED' where asset_id = '" + asset_id + "'";
                            //arb.updateAssetStatusChange(q3);
                            //System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q3);
                            
//                            String q4 = "update am_assetDisposal set disposal_status='PD' where Disposal_ID = '" + batchId + "'";
//                            arb.updateAssetStatusChange(q4);
         //                   System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q4);

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, batchId,assetCode);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                                String qp = "update am_asset set Asset_Status='Disposed' where Asset_id = '" + asset_id + "'";
                                arb.updateAssetStatusChange(qp);
                                String qa = "update am_asset_approval set asset_status='DISPOSED',process_status = 'A' where transaction_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(qa);
                                String qd = "update am_assetDisposal set disposal_status='P' where Disposal_ID = '" + batchId + "'";
                                arb.updateAssetStatusChange(qd);
                                String page1 = "ASSET DISPOSAL RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = "";
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description+" "+asset_id+" disposed";
                                String costDrAcct = ""; String costCrAcct = "";
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "4";
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                     	      aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, batchId,assetCode);
                            }
                            aprecords.updateRaiseEntry(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);

                            //===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET DISPOSAL
                            // if(b){
        /*--
                            legend.admin.handlers.CompanyHandler compHandler = new legend.admin.handlers.CompanyHandler();

                            String[] mailSetUp = compHandler.getEmailStatusAndName("asset disposal");
                            String Status1 = mailSetUp[0];
                            String mail_code = mailSetUp[1];

                            Status1 = Status1.trim();
                            System.out.println("#$$$$$$$$$$$ Status1 "+Status1+" $$$$$$$$$$$$$$$$$");
                            System.out.println("#$$$$$$$$$$$ mail_code "+mail_code+" $$$$$$$$$$$$$$$$$");

                            if(Status1.equalsIgnoreCase("Active"))
                            { //if mail status is active then send email

                            String transaction_type="Asset Disposal";
                            String subject ="Asset Disposal";

                            Codes message= new Codes();

                            EmailSmsServiceBus mail = new EmailSmsServiceBus();

                            String to = message.MailTo(mail_code, transaction_type);  //retrieves recipients from database

                            String msgText1 =message.MailMessage(mail_code, transaction_type);//"New asset with ID: "+id +" was successfully created.";
                            System.out.println("#$$$$$$$$$$$ "+to);
                            mail.sendMail(to,subject,msgText1);


                            }*/
                            //if(compHandler.getEmailStatus(mail_code).equalsIgnoreCase("Active"))

                            // }//if(b)

                            //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET DISPOSAL
                        }
                        else
                        {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                    }//inner if


                    if (full_status.equalsIgnoreCase("Asset2Disposed"))
                    {
                        // approvalCount += 1;

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset2 Disposal");

                        if (approvalCount == tranLevel)
                        {
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL

                            String page = "ASSET2 DISPOSAL RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=disposeAsset2RaiseEntry&id=" + asset_id + "&operation=1&prevPage=raiseEntry&pageDirect=Y";
                            if(partialtype.equalsIgnoreCase("P")){disposaltype = "PARTIALLY DISPOSED";}
                            else{disposaltype = "DISPOSED";}
                            
                            String q = "update am_asset_approval set process_status='A', asset_status='"+disposaltype+"',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            String q4 = "update am_asset2Disposal set disposal_status='PD' where Disposal_ID = '" + batchId + "'";
                            arb.updateAssetStatusChange(q4);
         //                   System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q4);

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, Description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, batchId,assetCode);
                            }

                            aprecords.updateRaiseEntry(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);
                            
                      		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                    		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                    		String subject ="Asset2 Disposal";
                			String msgText1 = "Your Asset with Asset Id: "+ asset_id +" has been Approved. ";
                			mail.sendMail(createdby,subject,msgText1);
                            
                        }
                        else
                        {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                    }//inner if



                    if (full_status.equalsIgnoreCase("accelerated depreciation"))
                    {
  //                  	  System.out.println("Value of tranId is >>>>>> " + tranId+"  Value of userid: "+userid+"   asset_id: "+asset_id);
                    	arb.deleteOtherSupervisors(asset_id,userid);
                        // approvalCount += 1;
//                    	System.out.println("Matanmi: "+full_status);
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Accelerated Depreciation");
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
 //                     System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                      if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}

//                      System.out.println("Value of approvalCount is >>>>>> " + approvalCount+"  Value of tranLevel: "+tranLevel+"    batchId: "+batchId);
                        if (approvalCount == tranLevel)
                        {
//                            if (singleApproval.equalsIgnoreCase("N")) {
//                                arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                                aprecords.updateRaiseEntry(asset_id);
//                                }
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL

                            String page = "ACCELERATED DEPRECIATION RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=acceleratedDeprRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageAcceleratedDepr&pageDirect=Y";
//                            System.out.println("u");
//                            System.out.println("in equality ===========================================");
                                               				
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            String q = "update am_asset_approval set process_status='A', asset_status='ACCELERATED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            }
//                            System.out.println("CHECK 1"+asset_id);
                            aprecords.updateRaiseEntry(asset_id);
//                            System.out.println("CHECK "+approvalRemark);
                            approverManager.createApprovalRemark(approvalRemark);
                            
                        	String subjectr ="Asset Accelerated Depreciation Approval";
                        	String msgText11 ="Your Asset Accelerated Depreciation with ASSET ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){

                                String q = "update am_asset_approval set process_status='A', asset_status='ACCELERATED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(q);
                                   String q3 = "update am_AcceleratedDepreciation set ACCELERATED_STATUS='Y' where Accelerated_ID = '" + Integer.toString(tranId) + "'";
                                   arb.updateAssetStatusChange(q3);
                            	   String updateqry = "UPDATE a SET a.Accum_Dep  = b.Accum_Dep+b.ACCELERATED_AMOUNT,a.nbv = a.nbv-b.ACCELERATED_AMOUNT, a.Useful_Life = b.Useful_life,a.Remaining_Life = b.Remaining_life "                               	   	
                               	   		+ "from  AM_ASSET a, am_AcceleratedDepreciation b where a.Asset_id = b.Asset_id and b.Asset_id = '"+ asset_id + "' ";
//                            	   System.out.println("@@@ the updateqry is  " + updateqry);
                                arb.updateAssetStatusChange(updateqry); 
                                arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                                arb.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
                                String costDrAcct=arb.getCodeName("select Accum_Dep_ledger from am_ad_category WHERE Category_Name = '"+categoryName+"'");
                                String costCrAcct=arb.getCodeName("select Dep_ledger from am_ad_category WHERE Category_Name = '"+categoryName+"'");
                                String page1 = "ACCELERATED DEPRECIATION RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = ""; 
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description+" "+asset_id;
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "40";
                                aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }                            
                            
                        }
                        else
                        {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                    }//inner if


                    if (full_status.equalsIgnoreCase("Reclassified"))
                    {
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Reclassification");
                        String[] pa = new String[12];
                        arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
//                      System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                      String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                      System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                      if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}

                        if (approvalCount == tranLevel && transAvailable != "0")
                        {
                            if (singleApproval.equalsIgnoreCase("N")) {
                              	pa = arb.setApprovalData(asset_id);
                              	pa[9]="Asset Reclassification";
                              arb.setPendingMultiApprTransArchive(pa,"4",Integer.parseInt(batchId),assetCode,userid);
//                              System.out.println("Value of userid is >>>>>> " + userid);
                              aprecords.updateRaiseEntry(asset_id);
                              }
                            String page = "ASSET RECLASSIFICATION RAISE ENTRY";
                            String url = "";//DocumentHelp.jsp?np=assetReclassRaiseEntry&id="+asset_id+"&pageDirect=Y";

                            String q = "update am_asset_approval set process_status='A',asset_status='Reclassified',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            //asset_reclassification.updateAssetReclassification(asset_id);
                            asset_reclassification.updateAssetReclassification(asset_id,tranId);
                            asset_manager.updateAssetReclass(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);

                             String newAsset_id = "";
                            try
                            {

                                AutoIDSetup aid_setup = new AutoIDSetup();

 //                               System.out.print("######### the category id is" + newCategory);
             //                   System.out.print("######### the branch id is" + branch);
           //                     System.out.print("######### the section id is" + section);
          //                      System.out.print("######### the dept id is" + dept);

//String cat_id = aid_setup.getCategoryID(category);
                                newAsset_id = aid_setup.getIdentity(branch, dept, section, newCategory);
 //                               System.out.println("The new asset ID after being reclassified is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                url = "DocumentHelp.jsp?np=assetReclassRaiseEntry&id=" + newAsset_id + "&pageDirect=Y";

                                //stop update on am_asset
                                String change_id_query2 = "";
                              //  String change_id_query2 = "update am_asset set old_asset_id ='" + asset_id + "', asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";

//		System.out.print("######### the query  is " + change_id_query2);
                                arb.updateAssetStatusChange(change_id_query2);
//id=newAsset_id;
                            } catch (Exception e) {
                                System.out.println("Error occurred in approval servlet when reclassifying asset " + e);
                            } catch (Throwable t) {
                                System.out.println("Throwable occurred in approval servlet when reclassifying asset" + t);
                            }
//                            System.out.print("######### the Transaction Id is " + tranId+"    newAsset_id: "+newAsset_id);
                    String change_id_query3 = "update am_assetReclassification set  new_asset_id='"+newAsset_id+"' where Reclassify_ID ="+tranId;
                    arb.updateAssetStatusChange(change_id_query3);
//                    		System.out.print("######### the change_id_query3 is " + change_id_query3);
                           // aprecords.insertApproval(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId);
//                    		System.out.print("######### the assetRaiseEntry value is " + assetRaiseEntry);
                    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                    aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                    }

                    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
//                    	System.out.print("######### assetRaiseEntry value is " + assetRaiseEntry);
                    	
                 	   String updateqry = "UPDATE a SET a.Asset_id=b.new_asset_id,a.OLD_ASSET_ID = b.asset_id,a.Category_ID  = b.new_category_id,a.Dep_Rate = b.new_depr_rate, a.CATEGORY_CODE = c.category_code,a.Total_Life = b.new_total_life,a.Remaining_Life = b.new_remaining_life "
                 	   		+ "from  AM_ASSET a, am_assetReclassification b,am_ad_category c where a.Asset_id = b.Asset_id and b.new_category_id = c.category_ID and a.Asset_id = '"+ asset_id +"' ";
//                	   System.out.println("@@@ the updateqry is  " + updateqry);
                    arb.updateAssetStatusChange(updateqry);                     
                    
                    arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                    arb.updateAssetStatusChange("update am_assetReclassification set STATUS = 'A' where Reclassify_ID = "+tranId+" ");  
                    String page1 = "ASSET RECLASSIFICATION PAYMENT ENTRY";
                    String iso = "000";
                    String costdebitAcctName = ""; 
                    String costcreditAcctName = "";
                    String raiseEntryNarration = Description+" "+asset_id;
                    String costDrAcct = ""; String costCrAcct = "";
                    String finacleTransId = "1";
                    String recType = "";
                    String transId = "16";
                    aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
         	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);

                    }                    
                    // aprecords.updateRaiseEntry(asset_id);
                       //stop update on am_asset
                        //    aprecords.updateRaiseEntry(newAsset_id);

              		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
            		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
            		String subject ="Asset Reclassification";
        			String msgText1 = "Your Asset Reclassification with Asset Id: "+ asset_id +" has been Approved. ";
        			mail.sendMail(createdby,subject,msgText1);

                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);
                    }//inner if

                    if (full_status.equalsIgnoreCase("Revalued"))
                    {
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Revaluation");

                        if (approvalCount == tranLevel) {
                            String page = "ASSET REVALUATION RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=assetRevalueRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageRevaluation&pageDirect=Y";

               //            System.out.println("===============here in revalued=============");
                            String q = "update am_asset_approval set process_status='A',asset_status='Revalued',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            String q3 = "update am_assetrevalue set approval_Status='ACTIVE' where revalue_id = '" + Integer.toString(tranId) + "'";
                            arb.updateAssetStatusChange(q);
                            arb.updateAssetStatusChange(q3);
                            asset_manager.updateAssetRevalue(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            aprecords.updateRaiseEntry(asset_id);
                            
                        	String subjectr ="Asset Revaluation  Approval";
                        	String msgText11 ="Your Asset Revaluation with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Transferred"))
                    {
                     //   System.out.println("===============here in transfered method of approval servlet=============");
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Transfer");
                        String[] pa = new String[12];
                        
                        arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);                      
//                      System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                      String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                      System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                      if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                      
                        if (approvalCount == tranLevel && transAvailable != "0") {
                        	
                            String q = arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '" + tranId + "' and super_Id = '" + userid + "'  and asset_status = 'PENDING' ");
                            String qw = arb.getCodeName("select user_Name from am_gb_User WHERE user_Id = " + q + " ");
//                            String createdUserId = arb.getCodeName("select a.User_Name from am_gb_user a,am_ad_branch b, am_gb_company c where a.Branch = b.BRANCH_ID and b.BRANCH_CODE = c.default_branch and a.user_id = " + q);
     //                       String test = "select distinct a.User_Name from am_gb_user a,am_ad_branch b, am_gb_company c,am_assetTransfer t where a.Branch = b.BRANCH_ID and (a.Branch = t.New_branch_id or b.BRANCH_CODE = c.default_branch) and t.approval_status = 'PARTIAL ACCEPTANCE' and a.user_id = " + q;
               //             System.out.println("=========>TEST: "+test);
                            String createdUserId = arb.getCodeName("select distinct t.asset_id, user_name,a.user_Id , t.user_id,b.BRANCH_CODE , c.default_branch,Branch , b.BRANCH_ID from am_gb_user a,am_assetTransfer t,am_ad_branch b, am_gb_company c "
                            										+ "where a.user_Id = t.user_id and a.Branch = b.BRANCH_ID and b.BRANCH_CODE = c.default_branch and t.user_id = "+q+" and Transfer_ID = '" +tranId +"' ");
//                            System.out.println("Value of InitiatorId is >>>>>> " + q + "   asset_id: " + asset_id + "   tranId: " + tranId + "  InitiatorName: " + qw);
                            String createdby = "update am_raisentry_post set entryPostFlag = 'P' , GroupIdStatus = 'P' where Id = '" + asset_id + "' and  userId != '" + qw + "'";
//                            System.out.println("Value of postq is >>>>>> " + createdby);
                            arb.updateAssetStatusChange(createdby);
                            String partialAcceptance=arb.getCodeName("select count(*) from am_asset_approval a, am_assetTransfer b where a.batch_id = b.Transfer_ID and a.batch_id = '"+tranId+"' and process_status = 'P' and approval_status = 'PARTIAL ACCEPTANCE' and super_id = '"+userid+"' ");
                            if (singleApproval.equalsIgnoreCase("N")) {
                               pa = arb.setApprovalData(asset_id);
                               pa[9] = "Asset Transfer";
                               arb.setPendingMultiApprTransArchive(pa, "6", Integer.parseInt(batchId), assetCode, userid);
                               aprecords.updateRaiseEntry(asset_id);
                            }                     	
                        	//System.out.println("===============here in transfered Date of approval servlet============="+approveddate);
                            //String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Integer.toString(tranId));
                            String newAsset_id = "";
                            String page = "ASSET TRANSFER RAISE ENTRY";
                            String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.asset.manager.AssetManager assetManager = new magma.asset.manager.AssetManager();
                            try {
                                String subjectr = "";
                                String qa = "";
                                String revalueReason = "";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String mailAddress = aid_setup.getCategoryID(category);
                               
                               
                      
                               
                                if(partialAcceptance.equals("1")) {
                                	 newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, mailAddress);
                                	 System.out.println("newAsset_id >>>>>>>" + newAsset_id);
                                    qa = "update a set a.process_status = 'A',a.asset_status = 'Transferred',a.DATE_APPROVED = '" + approveddate + "'  from am_asset_approval a, am_assetTransfer b where a.batch_id = b.Transfer_ID and a.batch_id = '"+tranId+"' and process_status = 'P' and approval_status = 'PARTIAL ACCEPTANCE' and super_id = '"+userid+"'";
                                    arb.updateAssetStatusChange(qa);
                                    qa = "update b set b.approval_status = 'ACTIVE',  b.new_asset_id ='" + newAsset_id + "' from am_asset_approval a, am_assetTransfer b where a.batch_id = b.Transfer_ID and a.batch_id = '"+tranId+"' and process_status = 'A' and approval_status = 'PARTIAL ACCEPTANCE' and super_id = '"+userid+"' ";
                                    arb.updateAssetStatusChange(qa);
                                }
                                else {
                                if (createdUserId.equalsIgnoreCase("")) {
                                    qa = "update am_asset_approval set process_status='WA', asset_status='PENDING',DATE_APPROVED = '" + approveddate + "' where user_id = " + q + " and transaction_id=" + tranId;
                                   arb.updateAssetStatusChange(qa);
                                   revalueReason = "update am_assettransfer set approval_Status='WA' where transfer_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(revalueReason);
                                } else {
                                	 String subject = aid_setup.getIdentity(newBranch, newDept, newSec, mailAddress);
                                	 System.out.println("subject >>>>>>>" + subject);
                                   qa = "update am_asset_approval set process_status='A',asset_status='Transferred',tran_type='Asset Transfer',DATE_APPROVED = '" + approveddate + "' where user_id = " + q + " and transaction_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(qa);
                                   String msgText11 = "update am_assettransfer set approval_Status='APPROVED', new_asset_id ='" + subject + "' where transfer_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(msgText11);
                                   revalueReason = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(revalueReason);
                                }
                                }

                                
//
//                                if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//                                    //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
//                                      aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
//                                  }
//                                if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
//                                      System.out.println("about to send raiseentry for trasfer " + assetCode);
//                                      aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
//                                  }
                                //the four lines below is to take care of scenario where only one entry is maintained in
                                // am_asset table for transferred asset.

                                //--- start block to prevent update to am_asset till transaction is posted----

                               // if(oldAssetStatus != null && oldAssetStatus.equalsIgnoreCase("RP")){

                               //  asset_manager.updateAssetTransfer(asset_id,tranId);
                               // change_id_query2 ="update am_asset set asset_id ='"+newAsset_id+"' ,asset_status ='Active',post_reject_reason='' where old_asset_id ='"+asset_id+"'";

                              //  }else{


                                //    asset_manager.updateAssetTransfer(asset_id);
                               // change_id_query2 = "update am_asset set old_asset_id ='" + asset_id + "', asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";
                               // }
                               // arb.updateAssetStatusChange(change_id_query2);

                                //--- stop block to prevent update to am_asset till transaction is posted----


                                //comment the two lines below to revert to asset transfer scenario where original and transfered assets
                                // will be available in am_asset table.

                                //assetManager.transferAssetsUpdate(asset_id, newAsset_id);
                                //assetManager.updateOldAssetWithApproval(asset_id, newAsset_id,tranId);
                                url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                                //--CHANGE ASSET ID TO NEWLY GENERATED ID
                                //--String change_id_query = "update am_asset set old_asset_id ='" + asset_id + "' where asset_id =' " + newAsset_id + " '";
                                //--arb.updateAssetStatusChange(change_id_query);

                                //--String change_id_query2 = "update am_asset set asset_status ='INACTIVE' where asset_id =' " + asset_id + " '";
                                //--arb.updateAssetStatusChange(change_id_query2);

//                                String msgText1 = "Your transaction for asset transfer with ID " + asset_id + " has been approved. The new asset ID is " + newAsset_id;
//                                mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);

                                System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered asset " + ex);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                              //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
//                            	String change_id_query2 = "update am_asset set old_asset_id ='"+asset_id+"', asset_id ='"+newAsset_id+"' where asset_id ='"+asset_id+"'";
//                            	arb.updateAssetStatusChange(change_id_query2);
                                aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N") && !createdUserId.equalsIgnoreCase("")){
//                            	magma.asset.manager.AssetManager assetMan = new magma.asset.manager.AssetManager();
//                            	magma.asset.dto.Asset asset = (magma.asset.dto.Asset)assetMan.getAssetByAssetCode(assetCode); 
//                            	String cdate = dbConnection.formatDate(new Date());
//                            	  String effDate = cdate;
//                            	  String transferDate = cdate;
//                            	  String newUser = "";
//                            	  String newsbu = "";
//                            	  String oldDept = "";
//                            	  String oldSec = "";
//                            	  
//                            	  double accumDep = asset.getAccumDep();
//                            	  double cost = asset.getCost();
//                            	  //String oldTransferDate = "";
//                            	  int oldBranchId = asset.getBranchId();
//                            	  int oldSectionId = asset.getSectionId();
//                            	  int oldDeptId = asset.getDeptId();
//                            	  
//                            	  String oldBranch = ""; 
//                            	  String oldUser = "";
//                            	  String oldWhoToRem1 = ""; 
//                            	  String oldEmail1 = ""; 
//                            	  String oldWhoToRem2 = ""; 
//                            	  String oldEmail2 = "";
//                            	  String regNo = "";
//                            	  String oldSbu = "";
//                            	   old_asset_id = asset.getAssetId();
//                            	   oldBranch = asset.getBranchName();
//                            	   oldDept = asset.getDeptName();
//                            	   oldSec = asset.getSectionName();
//                            	   oldUser = asset.getAssetUser();
//                            	   oldWhoToRem1 = asset.getWhoToRem1(); 
//                            	   oldEmail1 = asset.getWhoToRem2(); 
//                            	   oldWhoToRem2 = asset.getWhoToRem2(); 
//                            	   oldEmail2 = asset.getEmail2();
//                            	   regNo = asset.getRegNo();
//                            	   category = asset.getCategoryName();
//                            	   description = asset.getDescription(); 
//                            	   asset_id = asset.getAssetId();
//                            	   arb.updateAssetStatusChange("update am_asset set old_asset_id ='" + asset_id + "',asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'");
//                            	   
//                            	   magma.asset.dto.Transfer transfer = (magma.asset.dto.Transfer)assetMan.getTransferedAsset(old_asset_id);
//   //                         	   newAsset_id = transfer.getAssetId();
//                            	   newBranch = new Integer(transfer.getBranchId()).toString();
//                            	   newsbu = transfer.getSbucode();   
//                            	   newDept = new Integer(transfer.getDeptId()).toString();
//                            	   newSec = new Integer(transfer.getSectionId()).toString();
//                            	   newSbuCode = transfer.getSbucode();
//                            	   newbranchcode = transfer.getBranchCode();
//                            	   newdeptcode = transfer.getDeptCode();
//                            	   newscetioncode = transfer.getSectionCode();
//                            	   newAssetUser = transfer.getAssetUser();
//
//                            	   effDate = asset.getEffDate();
//                            	   transferDate = transfer.getTransferDate();
//                            	   newUser = transfer.getUser(); 
//                            	   newAssetUser = transfer.getAssetUser();
                            	   
                                   q = "update am_asset_approval set process_status='A',asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(q);
                                   String q3 = "update am_assettransfer set approval_Status='APPROVED', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
                                   arb.updateAssetStatusChange(q3);
                                   String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                   arb.updateAssetStatusChange(revalue_query);
                                   
//                            	   String updateqry = "update am_asset set old_asset_id ='" + old_asset_id + "', asset_id ='" + newAsset_id + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    where Old_asset_id ='" + old_asset_id + "'";
                            	   String updateqry = "UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Branch_ID = b.New_branch_id,a.Dept_ID = b.New_dept_id,"
                               	   		+ "a.Section_id = b.New_Section,a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE "
                               	   		+ "from  AM_ASSET a, am_assetTransfer b where a.Asset_id = b.Asset_id and a.Asset_id = '"+ asset_id + "'";
//                            	   System.out.println("@@@ the updateqry is  " + updateqry);
                                arb.updateAssetStatusChange(updateqry); 
                                arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                                arb.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
                                String page1 = "ASSET TRANSFER RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = ""; 
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description+" "+asset_id;
                                String costDrAcct = ""; String costCrAcct = "";
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "20";
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }

                            aprecords.updateRaiseEntry(newAsset_id);
                            
                        	String subjectr ="Asset Transfer Approval";
                        	String msgText11 ="Your Asset Transfer with Asset Id: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Asset Transfer Acceptance"))
                    {
                     //   System.out.println("===============here in transfered method of approval servlet=============");
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Transfer Acceptance");
                        returnPage = "BulkTranferAcceptanceList";
                        String[] pa = new String[12];
                        
                        pa[0]=asset_id; 
                        pa[1]= Integer.toString(userId); 
                        pa[2]= Integer.toString(userId);  
                        pa[3]=costPrice; 
                        pa[4]= newEffDate ;
                        pa[5]= description; 
                        pa[6]= newEffDate; 
                        pa[8]="PENDING"; 
                        pa[9]="Asset Transfer";   
                        pa[10]="P"; 
                        pa[11]=costPrice;
//                        System.out.println("Value of approvalRequired is >>>>>> " + approvalRequired);
                        legend.admin.objects.User user = sechanle.getUserByUserID(userid);
                    	String userName = user.getUserName();
                    	String branchRestrict = user.getBranchRestrict();
                    	String UserRestrict = user.getDeptRestrict();
                    	String departCode = user.getDeptCode();
                    	String branch_Id = user.getBranch();                        
                        java.util.ArrayList approvelist =arb.getApprovalsId(branch_Id,departCode,userName);
//                        System.out.println("Value of approvalCount is >>>>>> " + approvalCount+"     tranLevel: "+tranLevel+"     approvalRequired: "+approvalRequired);
                        if (approvalCount == tranLevel) {
                        	
//                            arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
//                          System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                            if(approvalRequired.equals("1")) {   
//                            String	mtid =  appHelper.getGeneratedId("am_asset_approval");
//                            System.out.println("Value of approvelist.size() is >>>>>> " + approvelist.size());
                            	String mtid = String.valueOf(tranId);
                       	   	 for(int j=0;j<approvelist.size();j++)
                    	     {  
                    		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(j);   	 
                    			String supervisorId =  usr.getUserId();
                    			String mailAddress = usr.getEmail();
                    			String supervisorName = usr.getUserName();
                    			String supervisorfullName = usr.getUserFullName();
 //                   			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId);
                    	  		 arb.setPendingTransMultiApp(pa,"87",assetCode,supervisorId,mtid);
                    			 String lastMTID = arb.getCurrentMtid("am_asset_approval");	
                    			 String subjectr ="Asset Transfer Acceptance for Approval";
                    			 String msgText11 ="Your Asset Transfer Acceptance with GROUP ID: "+ asset_id +" has been Accepted for approved.";
                    			 comp.insertMailRecords(mailAddress,subjectr,msgText11);
                    	     	}
                            	
                                String q = "update am_asset_approval set process_status='IAP',asset_status='Transferred' where transaction_id = '" + tranId + "' and process_status='WA'";
                                arb.updateAssetStatusChange(q);                                   
                            	String revalue_query = "update am_assettransfer set approval_Status='PARTIAL ACCEPTANCE' where transfer_id = '"+mtid+"'";
                            	arb.updateAssetStatusChange(revalue_query);                                
                                alertmessage = "Asset Transfer Accepted";                              
                            	
                            	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'ACCEPTED' ");
                            	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");                              
                            }
                            if(approvalRequired.equals("0")) {     
                          String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                            String newAsset_id = "";
                            String page = "ASSET TRANSFER RAISE ENTRY";
                            String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

//                            String q = "update am_asset_approval set process_status='A',asset_status='Transferred' where transaction_id = '" + tranId + "'";
//                             arb.updateAssetStatusChange(q);

                        //    System.out.println("the asset id is ?????????????????????? "+asset_id);
                        //    System.out.println("the transaction id is ?????????????????????? "+tranId);
                            //asset_manager.updateAssetTransfer(asset_id,tranId);

                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.asset.manager.AssetManager assetManager = new magma.asset.manager.AssetManager();
                            try {
                                String change_id_query2="";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String cat_id = aid_setup.getCategoryID(category);

                                newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);

                                String q3 = "update am_assettransfer set approval_Status='APPROVED', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
                                arb.updateAssetStatusChange(q3);

                                String revalue_query = "update am_assettransfer set approval_Status='APPROVED' where transfer_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(revalue_query);

                                url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + newAsset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

                                String msgText1 = "Your transaction for asset transfer with ID " + asset_id + " has been Accepted. The new asset ID is " + newAsset_id;
                                mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);

                                System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered asset " + ex);
                            }

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                              //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                            	magma.asset.manager.AssetManager assetMan = new magma.asset.manager.AssetManager();
                            	magma.asset.dto.Asset asset = (magma.asset.dto.Asset)assetMan.getAssetByAssetCode(assetCode); 
                            	String cdate = dbConnection.formatDate(new Date());
                            	  String effDate = cdate;
                            	  String transferDate = cdate;
                            	  String newUser = "";
                            	  String newsbu = "";
                            	  //String newWhoToRem1 = ""; 
                            	  //String newEmail1 = ""; 
                            	  //String newWhoToRem2 = ""; 
                            	  //String newEmail2 = "";
                            	  String oldDept = "";
                            	  String oldSec = "";
                            	  
                            	  double accumDep = asset.getAccumDep();
                            	  double cost = asset.getCost();
                            	  //String oldTransferDate = "";
                            	  int oldBranchId = asset.getBranchId();
                            	  int oldSectionId = asset.getSectionId();
                            	  int oldDeptId = asset.getDeptId();
                            	  //String oldSbuCode = asset.getSbuCode();
                            	  //String deptId = "";
                            	  //String branchId = "";
                            	  //String sectionId = "";
                            	  
                            	  String oldBranch = ""; 
                            	  String oldUser = "";
                            	  String oldWhoToRem1 = ""; 
                            	  String oldEmail1 = ""; 
                            	  String oldWhoToRem2 = ""; 
                            	  String oldEmail2 = "";
                            	  String regNo = "";
                            	  String oldSbu = "";
                            	   old_asset_id = asset.getAssetId();
                            	   oldBranch = asset.getBranchName();
                            	   oldDept = asset.getDeptName();
                            	   oldSec = asset.getSectionName();
                            	   oldUser = asset.getAssetUser();
                            	   oldWhoToRem1 = asset.getWhoToRem1(); 
                            	   oldEmail1 = asset.getWhoToRem2(); 
                            	   oldWhoToRem2 = asset.getWhoToRem2(); 
                            	   oldEmail2 = asset.getEmail2();
                            	   regNo = asset.getRegNo();
                            	   category = asset.getCategoryName();
                            	   description = asset.getDescription(); 
                            	   asset_id = asset.getAssetId();
                            	   arb.updateAssetStatusChange("update am_asset set old_asset_id ='" + asset_id + "',asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'");
                            	   
                            	   magma.asset.dto.Transfer transfer = (magma.asset.dto.Transfer)assetMan.getTransferedAsset(old_asset_id);
   //                         	   newAsset_id = transfer.getAssetId();
                            	   newBranch = new Integer(transfer.getBranchId()).toString();
                            	   newsbu = transfer.getSbucode();   
                            	   newDept = new Integer(transfer.getDeptId()).toString();
                            	   newSec = new Integer(transfer.getSectionId()).toString();
                            	   newSbuCode = transfer.getSbucode();
                            	   newbranchcode = transfer.getBranchCode();
                            	   newdeptcode = transfer.getDeptCode();
                            	   newscetioncode = transfer.getSectionCode();
                            	   newAssetUser = transfer.getAssetUser();

                            	   effDate = asset.getEffDate();
                            	   transferDate = transfer.getTransferDate();
                            	   newUser = transfer.getUser(); 
                            	   newAssetUser = transfer.getAssetUser();
                            	   String updateqry = "update am_asset set old_asset_id ='" + old_asset_id + "', " + 
                            	   		"asset_id ='" + newAsset_id + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', "+ 
                            	   		"section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',"+ 
                            	   		"BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    "+ 
                            	   		"where Old_asset_id ='" + old_asset_id + "'";
//                            	   System.out.println("@@@ the updateqry is  " + updateqry);
                                arb.updateAssetStatusChange(updateqry); 
                                arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                                arb.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
                                String page1 = "ASSET TRANSFER RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = ""; 
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description+" "+asset_id;
                                String costDrAcct = ""; String costCrAcct = "";
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "20";
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }
                            alertmessage = "Asset Transfer Accepted";
                            aprecords.updateRaiseEntry(newAsset_id);
                            
                        	String subjectr ="Asset Transfer Acceptance";
                        	String msgText11 ="Your Asset Transfer Acceptance with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        }
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Asset Transfer Acceptance Approval"))
                    {
                     //   System.out.println("===============here in transfered method of approval servlet=============");
                    	System.out.println("=====approvalCount: "+approvalCount+"    tranLevel: "+tranLevel);
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Transfer Acceptance Approval");
                        String[] pa = new String[12];
                        
                        if (approvalCount == tranLevel) {
                        	String status = "ACCEPTED";
//                            arb.deleteOtherSupervisorswithAssetId(asset_id,userid);
                            arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid,status);
                          System.out.println("Value of singleApproval in Asset Transfer Acceptance Approval is >>>>>> " + singleApproval+"    tranId: "+tranId);
                          String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                          String tranType=arb.getCodeName("select Transaction_type from Approval_Level_setup WHERE code = '6' ");
                            String newAsset_id = "";
                            String page = "ASSET TRANSFER RAISE ENTRY";
                            String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                            String qIAP = "update am_asset_approval set process_status='PR' where transaction_id = '" + tranId + "' and process_status = 'IAP'";
                            arb.updateAssetStatusChange(qIAP);

                        //    System.out.println("the asset id is ?????????????????????? "+asset_id);
                        //    System.out.println("the transaction id is ?????????????????????? "+tranId);
                            //asset_manager.updateAssetTransfer(asset_id,tranId);

                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.asset.manager.AssetManager assetManager = new magma.asset.manager.AssetManager();
                            try {
                                String change_id_query2="";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String cat_id = aid_setup.getCategoryID(category);

                                newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);

                                String q3 = "update am_assettransfer set approval_Status='APPROVED', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
                                arb.updateAssetStatusChange(q3);

                                String revalue_query = "update am_assettransfer set approval_Status='APPROVED' where transfer_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(revalue_query);

                                url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + newAsset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

                                String msgText1 = "Your transaction for asset transfer with ID " + asset_id + " has been Accepted. The new asset ID is " + newAsset_id;
                                mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);

                                String q = "update am_asset_approval set process_status='A',asset_status='Transferred',tran_type = '"+tranType+"' where transaction_id = '" + tranId + "' and process_status = 'WAA'";
                                arb.updateAssetStatusChange(q);
                                
                                System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered asset " + ex);
                            }

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                              //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                            	magma.asset.manager.AssetManager assetMan = new magma.asset.manager.AssetManager();
                            	magma.asset.dto.Asset asset = (magma.asset.dto.Asset)assetMan.getAssetByAssetCode(assetCode); 
                            	String cdate = dbConnection.formatDate(new Date());
                            	  String effDate = cdate;
                            	  String transferDate = cdate;
                            	  String newUser = "";
                            	  String newsbu = "";
                            	  //String newWhoToRem1 = ""; 
                            	  //String newEmail1 = ""; 
                            	  //String newWhoToRem2 = ""; 
                            	  //String newEmail2 = "";
                            	  String oldDept = "";
                            	  String oldSec = "";
                            	  
                            	  double accumDep = asset.getAccumDep();
                            	  double cost = asset.getCost();
                            	  //String oldTransferDate = "";
                            	  int oldBranchId = asset.getBranchId();
                            	  int oldSectionId = asset.getSectionId();
                            	  int oldDeptId = asset.getDeptId();
                            	  //String oldSbuCode = asset.getSbuCode();
                            	  //String deptId = "";
                            	  //String branchId = "";
                            	  //String sectionId = "";
                            	  
                            	  String oldBranch = ""; 
                            	  String oldUser = "";
                            	  String oldWhoToRem1 = ""; 
                            	  String oldEmail1 = ""; 
                            	  String oldWhoToRem2 = ""; 
                            	  String oldEmail2 = "";
                            	  String regNo = "";
                            	  String oldSbu = "";
                            	   old_asset_id = asset.getAssetId();
                            	   oldBranch = asset.getBranchName();
                            	   oldDept = asset.getDeptName();
                            	   oldSec = asset.getSectionName();
                            	   oldUser = asset.getAssetUser();
                            	   oldWhoToRem1 = asset.getWhoToRem1(); 
                            	   oldEmail1 = asset.getWhoToRem2(); 
                            	   oldWhoToRem2 = asset.getWhoToRem2(); 
                            	   oldEmail2 = asset.getEmail2();
                            	   regNo = asset.getRegNo();
                            	   category = asset.getCategoryName();
                            	   description = asset.getDescription(); 
                            	   asset_id = asset.getAssetId();
                            	   arb.updateAssetStatusChange("update am_asset set old_asset_id ='" + asset_id + "',asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'");
                            	   
                            	   magma.asset.dto.Transfer transfer = (magma.asset.dto.Transfer)assetMan.getTransferedAsset(old_asset_id);
   //                         	   newAsset_id = transfer.getAssetId();
                            	   newBranch = new Integer(transfer.getBranchId()).toString();
                            	   newsbu = transfer.getSbucode();   
                            	   newDept = new Integer(transfer.getDeptId()).toString();
                            	   newSec = new Integer(transfer.getSectionId()).toString();
                            	   newSbuCode = transfer.getSbucode();
                            	   newbranchcode = transfer.getBranchCode();
                            	   newdeptcode = transfer.getDeptCode();
                            	   newscetioncode = transfer.getSectionCode();
                            	   newAssetUser = transfer.getAssetUser();

                            	   effDate = asset.getEffDate();
                            	   transferDate = transfer.getTransferDate();
                            	   newUser = transfer.getUser(); 
                            	   newAssetUser = transfer.getAssetUser();
                            	   String updateqry = "update am_asset set old_asset_id ='" + old_asset_id + "', " + 
                            	   		"asset_id ='" + newAsset_id + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', "+ 
                            	   		"section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',"+ 
                            	   		"BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    "+ 
                            	   		"where Old_asset_id ='" + old_asset_id + "'";
//                            	   System.out.println("@@@ the updateqry is  " + updateqry);
                                arb.updateAssetStatusChange(updateqry); 
                                arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                                arb.updateAssetStatusChange("update am_assetTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" "); 
                                String page1 = "ASSET TRANSFER RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = ""; 
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description+" "+asset_id;
                                String costDrAcct = ""; String costCrAcct = "";
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "20";
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }
                            alertmessage = "Asset Transfer Accepted";
                            aprecords.updateRaiseEntry(newAsset_id);
                            
                        	String subjectr ="Asset Transfer Acceptance Approval";
                        	String msgText11 ="Your Asset Transfer Acceptance with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if



                    if (full_status.equalsIgnoreCase("Asset2 Transferred"))
                    {

                     //   System.out.println("===============here in transfered method of approval servlet=============");
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset2 Transfer");

                        if (approvalCount == tranLevel) {
                        	//System.out.println("===============here in transfered Date of approval servlet============="+approveddate);
                            //String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Integer.toString(tranId));
                            String newAsset_id = "";
                            String page = "ASSET2 TRANSFER RAISE ENTRY";
                            String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

                            String q = "update am_asset_approval set process_status='A',asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                             arb.updateAssetStatusChange(q);

                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.net.manager.AssetManager assetManager = new magma.net.manager.AssetManager();
                            try {
                                String change_id_query2="";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String cat_id = aid_setup.getCategoryID(category);

                                newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);

                                String q3 = "update am_asset2transfer set approval_Status='ACTIVE', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
                                arb.updateAssetStatusChange(q3);

                                String revalue_query = "update am_asset2transfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(revalue_query);
                                String up1 = "update am_asset2 set old_asset_id ='" + old_asset_id + "', asset_id ='" + newAsset_id + "', DEPT_ID = '"+newDeptID+"', BRANCH_ID = '"+newBranchID+"', section_id = '"+newSectionID+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    where asset_id ='" + old_asset_id + "'";
//                                System.out.println("up1 am_asset2 ================== "+up1);
                                arb.updateAssetStatusChange(up1); 
                                String up2 = "update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET2 TRANSFER RAISE ENTRY'";
                                arb.updateAssetStatusChange(up2); 
                                String up3 = "update am_asset2Transfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ";
                                arb.updateAssetStatusChange(up3);  
                               
                                url = "DocumentHelp.jsp?np=asset2TransfersRaiseEntry&id=" + newAsset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers2&pageDirect=Y";
                                String msgText1 = "Your transaction for asset2 transfer with ID " + asset_id + " has been approved. The new asset ID is " + newAsset_id;
                                mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);

                                System.out.println("The new asset2 ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered asset2 " + ex);
                            }
                            assetRaiseEntry = "N";
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                              //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            String up4 = "UPDATE am_asset2 SET raise_entry = 'Y' WHERE ASSET_ID = '" + newAsset_id + "'  ";
                            arb.updateAssetStatusChange(up4);
                            
                        	String subjectr ="Asset2 Transfer Approval";
                        	String msgText11 ="Your Asset Transfer2 with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

if (full_status.equalsIgnoreCase("WIP"))
{
 //   System.out.println("===============here in WIP method of approval servlet=============");
    approvalRemark.setApprovalLevel(approvalCount);
    approvalRemark.setRemark("");
    approvalRemark.setStatus("Approved");
    approvalRemark.setTranType("WIP Reclassification");
//    System.out.println("Value of tranId is >>>>>> " + tranId+"  userid: "+userid);
    arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
//  System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
  String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//  System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
  if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
  
    if (approvalCount == tranLevel && transAvailable != "0")
    {

      String oldId=  aprecords.getCodeName("SELECT old_asset_id from am_asset where old_asset_id = '" + asset_id+"'");
      
        String newAsset_id = "";
        String page = "WIP RECLASSIFICATION";
        String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
        
        String q = "update am_asset_approval set process_status='A',approval_level_count='"+approvalCount+"'" +
                ",asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = " +
                "'" + tranId + "'";

        String q3 = "update AM_WIP_RECLASSIFICATION set approval_Status='ACTIVE' where transfer_id = " +
                "'" + Integer.toString(tranId) + "'";

        arb.updateAssetStatusChange(q);
        arb.updateAssetStatusChange(q3);


        
        //System.out.println("asset_id >>>>>>>>>> " + asset_id);
        //System.out.println("newAssetID >>>>>>>>>> " + newAssetID);

        approverManager.createApprovalRemark(approvalRemark);

        magma.net.manager.AssetManager assetManager = new magma.net.manager.AssetManager();
        WIPAssetManager wipAssetMan = new WIPAssetManager();


        try
        {
            AutoIDSetup aid_setup = new AutoIDSetup();
            String cat_id = aid_setup.getCategoryID(category);
//            System.out.println("@@@ the category id is" + newCategoryID);
//            System.out.println("@@@ the newBranch id is" + newBranch);
//            System.out.println("@@@ the newDept id is" + newDept);
//            System.out.println("@@@ the newSec id is" + newSec);
//            System.out.println("@@@ the newEffDate id is" + newEffDate);
            
            newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, newCategoryID);
            String newDepRate = aprecords.getCodeName("SELECT DEP_RATE FROM AM_AD_CATEGORY WHERE CATEGORY_ID ='"+newCategoryID+"'");
            int Dep_Rate = 0;  
            int totallife = 0;
            double RateValue =  0;
            String dependdate = "";
            url = "DocumentHelp.jsp?np=WipReclassificationRaiseEntry&id=" + newAsset_id + "&pageDirect=Y"; 
            if(newDepRate.equalsIgnoreCase("0.00")){newDepRate = "1";
            	Dep_Rate = Integer.parseInt(newDepRate);
            	RateValue = 0;
            }  
            else{
            	double DepRate = Double.parseDouble(newDepRate);
            	RateValue =(100/DepRate)*12;
            	dependdate = aprecords.getCodeName("SELECT DATEADD(month, "+RateValue+", '"+newEffDate+"')");
            } 
            totallife= (int) Math.round(RateValue);
//            System.out.println("New Category Rate: " + RateValue+"    totallife: "+totallife); 
            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                  aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
              }
              if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                  //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
//                  String change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
//                          " asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";
//                  arb.updateAssetStatusChange(change_id_query2);
//                  asset_manager.updateWIPReclassification(asset_id,newAsset_id,totallife,newDepRate,dependdate);
                  if (newAsset_id.equalsIgnoreCase(asset_id))
                  {
//                	  System.out.println("<<< Value of newAssetID >>>>> "+newAssetID+"    asset_id: "+asset_id+"    assetId: "+assetId+"    newAsset_id: "+newAsset_id);
                      asset_manager.updateWIPReclassification(asset_id,totallife,newDepRate,dependdate);
//                      System.out.println("<<<<<<<<<< Both Values Are the same >>>>>>>>>>>>>>>>>>> ");
                      String change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
                                            " asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";
                      arb.updateAssetStatusChange(change_id_query2);
                  }
                  else
                  {

//                       System.out.println("<<<<<<<<<< Both Values Are not the same >>>>>>>>>>>>>>>>>>> ");
                       asset_manager.updateWIPReclassification(asset_id,newAssetID,totallife,newDepRate,dependdate);
//                      change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
//                                            " asset_id ='" + newAsset_id + "' where asset_id ='" + newAssetID + "'";
//                      System.out.println("change_id_query2 >>>>>>> " +change_id_query2 );
                      raiseEntryInfo = aprecords.raiseEntryInfo(newAssetID);

                      description = raiseEntryInfo[0];
                      branch_id = raiseEntryInfo[1];
                      branchID = branch_id == null || branch_id.equals("") ? 0 : Integer.parseInt(branch_id);
                      subject_to_vat = raiseEntryInfo[2];
                      wh_tax = raiseEntryInfo[3];
                      flag = "";
                      partPay = "";
                      asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
                      branchName = aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);

                  }            	  
                  
                  String page1 = "ASSET TRANSFER RAISE ENTRY";
                  String iso = "000";
                  String costdebitAcctName = ""; 
                  String costcreditAcctName = "";
                  String raiseEntryNarration = Description+" "+asset_id;
                  String costDrAcct = ""; String costCrAcct = "";
                  String finacleTransId = "1";
                  String recType = "";
                  String transId = "20";
                  aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
       	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                                      
                }            
             String change_id_query2="";

//
//             System.out.println(">>>>>>>>>> The oldId is >> "+oldId );
//               System.out.println(">>>>>>>>>> The asset_id is >> "+asset_id );

            if(oldId != null && !oldId.equalsIgnoreCase("") && oldId.equalsIgnoreCase(asset_id)) {
             String repostQuery = " update am_asset set asset_id ='" + asset_id + "', " +
                                      " asset_status='Active',post_reject_reason='' where old_asset_id ='" + asset_id + "'";
            arb.updateAssetStatusChange(repostQuery);
//            change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
//                                      " asset_id ='" + newAsset_id + "' where old_asset_id ='" + asset_id + "'";
            }


                if(wipAssetMan.getWIPInfo(asset_id))
              	//stop update on am_asset
            //  wipAssetMan.updateWIPInfo(asset_id);
                	System.out.println("stop update on am_asset" );
             else
            	//stop update on am_asset
            //  wipAssetMan.updateWIPInfoFuture(asset_id);
            	 System.out.println("stop update on am_asset" );

             // System.out.println("change_id_query2 >>>> " + change_id_query2);
         //stop update on am_asset
         //   arb.updateAssetStatusChange(change_id_query2);


            String revalue_query = " update AM_WIP_RECLASSIFICATION set approval_Status='ACTIVE' " +
                                   " where transfer_id = '" + tranId + "'";

            arb.updateAssetStatusChange(revalue_query);


            //comment the two lines below to revert to asset transfer scenario where original and transfered assets
            // will be available in am_asset table.

            //assetManager.transferAssetsUpdate(asset_id, newAsset_id);
            //assetManager.updateOldAssetWithApproval(asset_id, newAsset_id,tranId);
               //stop update on am_asset continued
       /*     url = "DocumentHelp.jsp?np=WipReclassificationRaiseEntry&id=" + asset_id + "&operation=0&" +
                    "exitPage=DocumentHelp.jsp?np=manageWipTransfers&pageDirect=Y";  */
            url = "DocumentHelp.jsp?np=WipReclassificationRaiseEntry&id=" + newAsset_id + "&operation=0&" +
                    "exitPage=DocumentHelp.jsp?np=manageWipTransfers&pageDirect=Y";
            //--CHANGE ASSET ID TO NEWLY GENERATED ID
            //--String change_id_query = "update am_asset set old_asset_id ='" + asset_id + "' where asset_id =' " + newAsset_id + " '";
            //--arb.updateAssetStatusChange(change_id_query);

            //--String change_id_query2 = "update am_asset set asset_status ='INACTIVE' where asset_id =' " + asset_id + " '";
            //--arb.updateAssetStatusChange(change_id_query2);

          String msgText1 = "Your transaction for asset WIP transfer with ID " + asset_id + " has been approved." +"";
            //stop update on am_asset continued
                //    " The new asset ID is " + newAsset_id;
            mail.sendMailTransactionInitiator(tranId, "Asset WIP Transfer", msgText1);

//            System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

        }
        catch (Throwable ex)
        {
            System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered WIP asset " + ex);
        }
        if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
  //   System.out.println("The "+asset_id+"  <<<<<<<<--------1--------->>>>>>>" + assetRaiseEntry);
      //stop update on am_asset continued
       /* aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName,
                subject_to_vat, wh_tax, url, tranId,assetCode);*/
       boolean a= 	 aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName,
                     subject_to_vat, wh_tax, url, tranId,assetCode);
       //update new asset on AM_WIP_RECLASSIFICATION
       String RECLASSIFICATION = " update AM_WIP_RECLASSIFICATION set new_asset_id ='" + newAsset_id + "'  where asset_Code ='" + assetCode + "'";
       arb.updateAssetStatusChange(RECLASSIFICATION);
 //      System.out.println("The "+asset_id+"  <<<<<<<<--------2--------->>>>>>>" + a);
        }
     //stop update on am_asset continued
      //  aprecords.updateRaiseEntry(newAsset_id);
//        System.out.println("The  <<<<<<<<-------3---------->>>>>>>" + assetRaiseEntry);
        aprecords.updateRaiseEntry(asset_id);  
    }
    else
    {
        arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
        approverManager.createApprovalRemark(approvalRemark);
    }
    //arb.incrementApprovalCount(tranId,approvalCount,supervisor);
    
	String subjectr ="Asset WIP Approval";
	String msgText11 ="Your Asset WIP with GROUP ID: "+ asset_id +" has been approved.";
	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
    comp.insertMailRecords(mailAddress,subjectr,msgText11);
    
}//inner if

                    if (full_status.equalsIgnoreCase("Maintained")) {
             //           System.out.println("===============here in Maintained section of approvalServlet =============");
                    	 magma.asset.manager.AssetManager assetMan = new magma.asset.manager.AssetManager();
//                    	 System.out.println("<<<<--approvalCount----->>>>: " + approvalCount);
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Improvement");
//                        System.out.println("<<<<--tranLevel----->>>>: " + tranLevel); 
                        
                        arb.deleteOtherSupervisorswithBatchIdSupervisor(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
//                        System.out.println("Value of batchId is >>>>>> " + batchId+"Value of assetCode is >>>>>> " + assetCode+"Value of userid is >>>>>> " + userid);
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

                            String page = "ASSET IMPROVEMENT RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=assetRevalueMaintenanceRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageMaitenance&pageDirect=Y";


                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Maintained',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);

                            String q3 = "update am_asset_improvement set approval_Status='ACTIVE' where revalue_id = '" + Integer.toString(tranId) + "'";

                            String q4 = "update am_asset set asset_status='Active',post_reject_reason='' where asset_id ='" + asset_id + "'";

                            arb.updateAssetStatusChange(q);
                            arb.updateAssetStatusChange(q3);
                            arb.updateAssetStatusChange(q4);
                            approverManager.createApprovalRemark(approvalRemark);
//                            System.out.println("<<<<--assetRaiseEntry----->>>>: " + assetRaiseEntry);
                            //asset_manager.updateAssetMaintenance(asset_id, tranId);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                            	
                            	  //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            	  //String cdate = sdf.format(new Date());
                            	  String cdate = dbConnection.formatDate(new Date());

//                            	  System.out.println("<<<<--cdate----->>>>: " + cdate);
                            	  String vendorAcct = "";
                            	  double cost = 0;
                            	  double vatAmt = 0;
                            	  double whtAmt = 0;
                            	  String revalueReason = "";
                            	  String revDate = cdate;
                            	  double vatableCost = 0;
                            	  double nbv = 0;
                            	  double accumDep = 0;
                            	  String effDate = cdate;
                            	  int usefullife = 0;
                            	  //old declaration
                            	  double oldCost = 0;
                            	  double oldVatAmt = 0;
                            	  double oldWhtAmt = 0;
                            	  double oldVatableCost = 0;
                            	  double oldNbv = 0;
                            	  double oldAccumDep = 0;
                            	  double oldimprovCost = 0.00;   
                            	  double oldimprovvatableCost = 0.00; 
                            	  double oldimprovNBV = 0.00;
                            	  double oldimprovaccum = 0.00;
                            	  //new declaration
                            	  double newCost = 0;
                            	  double newVatAmt = 0;
                            	  double newWhtAmt = 0;
                            	  double newVatableCost = 0;
                            	  double newNbv = 0;
                            	  double newAccumDep = 0;
                            	  String sbuCode = "";
                            	  
//                            	     System.out.println("-----------------------------------asset_id---------------------------here 4"+asset_id);
                            	  magma.asset.dto.Asset asset = (magma.asset.dto.Asset)assetMan.getAsset(asset_id);
//                            	  System.out.println("--------------------------------------------------------------here 5");
                            	  oldCost = asset.getCost();
                            	  System.out.println("--------------------------------------------------------------here 6 "+oldCost);
                            	  oldVatAmt = asset.getVatAmt();
//                            	  System.out.println("--------------------------------------------------------------here 7 "+oldVatAmt);
                            	  oldWhtAmt = asset.getWhtAmt();
//                            	  System.out.println("--------------------------------------------------------------here 8 "+oldWhtAmt);
                            	  wh_tax = asset.getSubj2Wht();
//                            	  System.out.println("--------------------------------------------------------------here 9 "+wh_tax);
                            	  subject_to_vat = asset.getSubj2Vat();
//                            	  System.out.println("--------------------------------------------------------------here 10 "+subject_to_vat);
                            	 // wht_percent = asset.getWht_percent();
                            	  oldVatableCost = asset.getVatableCost();
//                            	  System.out.println("--------------------------------------------------------------here 11 "+oldVatableCost);
                            	  oldNbv = asset.getNbv();
//                            	  System.out.println("--------------------------------------------------------------here 12 "+oldNbv);
                            	  oldAccumDep = asset.getAccumDep();

                            	  String  nbvresidual = aprecords.getCodeName("SELECT NBV FROM AM_ASSET WHERE ASSET_ID = '"+asset_id+"' "); 
                            	  
                            	   magma.asset.dto.Improvement revalue = (magma.asset.dto.Improvement)assetMan.getMaintenanceAssetRepost(asset_id,tranId); 
                            	   if(revalue!=null){
                            	   vendorAcct = revalue.getRevVendorAcct();
                            	   cost = revalue.getCost();
                            	   vatAmt = revalue.getVatAmt();
                            	   whtAmt = revalue.getWhtAmt();
                            	   revalueReason = revalue.getRevalueReason();
                            	   revDate = revalue.getRevalueDate();
                            	   vatableCost = revalue.getVatableCost();
//                            	   integrifyid = revalue.getIntegrify();
                            	   nbv = revalue.getNbv();
                            	   accumDep = revalue.getAccumDep();
                            	   effDate = revalue.getEffDate(); 
                            	   sbuCode = revalue.getSbuCode();
                            	   //old assignment
                            	   oldCost = revalue.getOldCost();
                            	   oldVatAmt = revalue.getOldVatAmt();
                            	   oldWhtAmt = revalue.getOldWhtAmt();
                            	   oldVatableCost = revalue.getOldVatableCost();
                            	   oldNbv = revalue.getOldNbv();
                            	   oldAccumDep = revalue.getOldAccumDep(); 
                            	   usefullife = revalue.getUsefullife();
                            	   oldCost = revalue.getOldCost();
                            	   oldimprovCost = revalue.getOldimprovCost();  
                            	   oldimprovvatableCost = revalue.getOldimprovvatableCost();  
                            	   oldimprovNBV  =  revalue.getOldimprovNBV();
                            	   oldimprovaccum = revalue.getOldimprovaccum();
                            	   //new assignment
                            	   newCost = asset.getCost();
                            	   newVatAmt = asset.getVatAmt();
                            	   newWhtAmt = asset.getWhtAmt();
//                            	   System.out.print("=======>newWhtAmt: "+newWhtAmt);
                            	   newVatableCost = asset.getVatableCost();
                            	   newNbv = asset.getNbv();
                            	   newAccumDep = asset.getAccumDep();
                            	    }
//                            	   System.out.print("<<<<oldCost: "+oldCost+"   newCost: "+cost+"   oldNbv: "+oldNbv+"  oldVatableCost: "+vatableCost+"  oldVatAmt: "+oldVatAmt+"   newVatAmt: "+vatAmt);
                            	   double CalcCost = oldCost+cost;
                            	   double CalcNBV = oldNbv + cost;
                            	   double Calcvatable = oldVatableCost+vatableCost;
                            	   double CalcVatAmt = oldVatAmt + vatAmt;
                            	   double CalcWhtAmt = oldWhtAmt + whtAmt;  
                            	   
                            	assetMan.processImprovement(asset_id,CalcCost,CalcNBV,Calcvatable,CalcVatAmt,CalcWhtAmt,usefullife,cost,vatableCost,nbvresidual,tranId);
                            	
//                                String qp = "update am_asset_improvement set Asset_Status='Disposed' where Asset_id = '" + asset_id + "'";
//                                arb.updateAssetStatusChange(qp);
//                                String qa = "update am_asset_approval set asset_status='DISPOSED',process_status = 'A' where transaction_id = '" + tranId + "'";
//                                arb.updateAssetStatusChange(qa);
//                                String qd = "update am_asset_improvement set disposal_status='P' where Disposal_ID = '" + batchId + "'";
//                                arb.updateAssetStatusChange(qd);
//                                String page1 = "ASSET IMPROVEMENT RAISE ENTRY";
                                String iso = "000";
                                String costdebitAcctName = "";
                                String costcreditAcctName = "";
                                String raiseEntryNarration = Description;
                                String costDrAcct = ""; String costCrAcct = "";
                                String finacleTransId = "1";
                                String recType = "";
                                String transId = "26";
                                aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }

                            aprecords.updateRaiseEntry(asset_id);
                            
                        	String subjectr ="Improvement Approval";
                        	String msgText11 ="Your Improvement Asset with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Uncapitalized Improvement")) {
             //           System.out.println("===============here in Maintained section of approvalServlet =============");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Uncapitalized Improvement");

                        arb.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {
                            
                            String page = "ASSET IMPROVEMENT RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=uncapitalizedImprovementRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageUncapImprovement&pageDirect=Y";


                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Maintained',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);

                            String q3 = "update am_Uncapitalized_improvement set approval_Status='ACTIVE' where revalue_id = '" + Integer.toString(tranId) + "'";

                            String q4 = "update AM_ASSET_UNCAPITALIZED set asset_status='Active',post_reject_reason='' where asset_id ='" + asset_id + "'";

                            arb.updateAssetStatusChange(q3);
                            arb.updateAssetStatusChange(q4);

                            approverManager.createApprovalRemark(approvalRemark);
//                            System.out.println("<<<<--assetRaiseEntry----->>>>: " + assetRaiseEntry);
                            //asset_manager.updateAssetMaintenance(asset_id, tranId);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, Description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            alertmessage = "Uncapitalized Improvement Transaction Approved";
                            aprecords.updateRaiseEntry(asset_id);
                        	String subjectr ="Uncapitalized Improvement Approval";
                        	String msgText11 ="Your Uncapitalized Improvement Asset with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

//Matanmi
                    if (full_status.equalsIgnoreCase("Revalued")) {
                 //       System.out.println("===============here in Maintained section of approvalServlet =============");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Revaluation");
                        
                        arb.deleteOtherSupervisorswithBatchIdSupervisor(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

                            String page = "ASSET REVALUATION RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=assetRevalueMaintenanceRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageMaitenance&pageDirect=Y";


                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Revalued',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);

                            String q3 = "update am_asset_revaluation set approval_Status='ACTIVE' where revalue_id = '" + Integer.toString(tranId) + "'";

                            String q4 = "update am_asset set asset_status='Active',post_reject_reason='' where asset_id ='" + asset_id + "'";

                            arb.updateAssetStatusChange(q3);
                            arb.updateAssetStatusChange(q4);

                            approverManager.createApprovalRemark(approvalRemark);

                            //asset_manager.updateAssetMaintenance(asset_id, tranId);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }

                            aprecords.updateRaiseEntry(asset_id);
                            
                        	String subjectr ="Asset Revaluation Approval";
                        	String msgText11 ="Your Asset Revaluation with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//Matanmi

                    

                    if (full_status.equalsIgnoreCase("Depreciation Adjusted")) {

                    	System.out.println("full_status for Adjustment: "+full_status);
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Depreciation Adjustment");
                        System.out.println("approvalCount: "+approvalCount+"   tranLevel: "+tranLevel);
                        if (approvalCount == tranLevel) {

                            String page = "DEPRECIATION ADJUSTMENT RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=assetDepAdjustment&id=" + asset_id + "&operation=1&exitPage=assetDepAdjustment&pageDirect=Y";
                        	
                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Depreciation Adjusted',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            String q3 = "update AM_ASSET_DEP_ADJUSTMENT set approval_Status='ACTIVE',raise_entry = 'Y' where id = '" + Integer.toString(tranId) + "'";
                            arb.updateAssetStatusChange(q3);
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            approverManager.createApprovalRemark(approvalRemark);

                            asset_manager.updateAssetDepreciation(asset_id);


                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Asset Update")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Bulk Asset Update");

                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"15",Integer.parseInt(batchId),assetCode,userid);  
                        }
                        if (approvalCount == tranLevel && transAvailable != "0") {
                            
             //               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Updated',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkUpdate");
                            System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            BulkUpdateManager bum = new BulkUpdateManager();
                            bum.updateBulkAsset(newAssetList);
             //               System.out.println("<<<<<<<<<the After size of sessioned ArrayList is >>>>>>>> ");
                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            // asset_manager.updateAssetDepreciation(asset_id);
                            
                        	String subjectr ="Bulk Asset Update Approval";
                        	String msgText11 ="Your Bulk Asset Update with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Material Retrieval")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Material Retrieval");

                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"15",Integer.parseInt(batchId),assetCode,userid);  
                        }
                        if (approvalCount == tranLevel && transAvailable != "0") {
                            
             //               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");

                            ArrayList newMaterialList = (ArrayList) request.getSession().getAttribute("newMaterialRetrieval");
                            System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newMaterialList.size()+"   batchId: "+batchId);
                            BulkUpdateManager bum = new BulkUpdateManager();
                            bum.materialRetrievalUpdate(newMaterialList,batchId);
                            
                            approverManager.createApprovalRemark(approvalRemark);
                            
                            String q = "update am_asset_approval set process_status='A', asset_status='Material Retrieval',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);
                            
                        	String subjectr ="Material Retrieval Approval";
                        	String msgText11 ="Your Material Retrieval with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Uncapitalized Asset Update")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Bulk Uncapitalized Asset Update");

                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"23",Integer.parseInt(batchId),assetCode,userid);  
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

             //               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='Bulk Uncapitalized Asset Updated',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkUpdate");
                            System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            BulkUpdateUncapitalisedManager bum = new BulkUpdateUncapitalisedManager();
                            bum.updateBulkAsset(newAssetList);
             //               System.out.println("<<<<<<<<<the After size of sessioned ArrayList is >>>>>>>> ");
                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);
                            
                        	String subjectr ="Bulk Uncapitalized Asset Update Approval";
                        	String msgText11 ="Your Bulk Uncapitalized Asset Update with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                            // asset_manager.updateAssetDepreciation(asset_id);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Bulk Asset2 Update")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Bulk Asset2 Update");
                    	
                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
         //               System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
         //               System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

                     //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset2 Update',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkUpdate");
                      //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            BulkUpdateManager bum = new BulkUpdateManager();
                            bum.updateBulkAsset2(newAssetList);
 
                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);
                            
                        	String subjectr ="Bulk Asset2 Update Approval";
                        	String msgText11 ="Your Bulk Asset2 Update Asset Update with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                            // asset_manager.updateAssetDepreciation(asset_id);
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if
                                   
                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer")) {
//                           System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
//                    	String tryme = "select user_id from am_asset_approval WHERE batch_Id = '"+tranId+"' and super_Id = '"+userId+"'  and asset_status = 'PENDING' ";
//                    	System.out.println("Value of test is >>>>>> " + tryme);
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+tranId+"' and super_Id = '"+userId+"'  and asset_status = 'PENDING' ");
                    	System.out.println("the value for the InitiatorId is >>>>>>>>>>>>>>>> " + InitiatorId);
//                    	String test = "select a.User_Name from am_gb_user a,am_ad_branch b, am_gb_company c "
//                    			+ "where a.Branch = b.BRANCH_ID and b.BRANCH_CODE = c.default_branch and a.user_id = "+InitiatorId+"";
//                    	 System.out.println("Value of test is >>>>>> " + test);
                    	String userName = arb.getCodeName("select a.User_Name from am_gb_user a,am_ad_branch b, am_gb_company c "
                    			+ "where a.Branch = b.BRANCH_ID and b.BRANCH_CODE = c.default_branch and a.user_id = "+InitiatorId+"");
                    	System.out.println("the value for the userName is >>>>>>>>>>>>>>>> " + userName);
                    	String page = "BULK ASSET TRANSFER RAISE ENTRY";
                           approvalRemark.setApprovalLevel(approvalCount);
                           approvalRemark.setRemark("");
                           approvalRemark.setStatus("Approved");
                           approvalRemark.setTranType("Bulk Asset Transfer");
                            tableName = "am_gb_bulkTransfer";
                           String url = "DocumentHelp.jsp?np=bulkAssetTransferPosting&id=" + tranId + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                           
                           arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
//                           System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                           String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+tranId+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
         //                  System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                           if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                           if (singleApproval.equalsIgnoreCase("N")) {
//                        	   System.out.println("Value of batchId is >>>>>> " + batchId+" Value of assetCode is >>>>>> " + assetCode+" Value of userid is >>>>>> " + userid);
                           arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"28",Integer.parseInt(batchId),assetCode,userid); 
                           }
                           
                           if (approvalCount == tranLevel && transAvailable != "0") {
                        	   

//                              System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
//                        	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                        	   if(userName.equalsIgnoreCase("")){
                               String q = "update am_asset_approval set process_status='WA', asset_status='Bulk Asset Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                               arb.updateAssetStatusChange(q);
                        	   		}
                        	   if(!userName.equalsIgnoreCase("")){
                                   String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                                   arb.updateAssetStatusChange(q);
                            	   }
                               ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkAssetTransfer");
             //                  System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                               BulkUpdateManager bum = new BulkUpdateManager();
           //                    bum.BulkAssetTransfer(newAssetList);
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                               description = "Bulk Asset Transfer with Batch Id "+tranId+"";
                               int tranlent = String.valueOf(tranId).length();
//                             System.out.println("the for the tranlent is >>>>>>>>>>>>>>>> " + tranlent);                               
                       		String revalue_query = "update am_assettransfer set approval_Status='APPROVED' where substring(CAST(transfer_id AS varchar(38)),1,"+tranlent+") = '"+tranId+"'";
                       		arb.updateAssetStatusChange(revalue_query);
//                          	 System.out.println("the for the revalue_query is >>>>>>>>>>>>>>>> " + revalue_query); 
                                String q3 = "update am_gb_bulkTransfer set Status='APPROVED' where Batch_id = '"+ Integer.toString(tranId)+"'";
                                   arb.updateAssetStatusChange(q3);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y") && !userName.equalsIgnoreCase("")){
                                  System.out.println("about to send Bulk raiseentry for trasfer " + tranId);
                                  aprecords.insertApprovalx(String.valueOf(tranId), description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                              }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N") && !userName.equalsIgnoreCase("")){
                                String query= "UPDATE a SET a.Asset_Id = b.NEW_ASSET_ID,a.OLD_ASSET_ID = b.Asset_Id,a.Branch_ID = b.newbranch_id," +
                                        "a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.SBU_CODE = b.newSBU_CODE,a.Section_id = b.newsection_id," +
                                        "a.dept_id = b.newdept_id,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE," +
                                        "a.Asset_User = b.newAsset_User FROM am_asset a,am_gb_bulkTransfer b WHERE a.Asset_Id = b.Asset_Id " +
                                        "AND b.Batch_Id = '"+Integer.toString(tranId)+"'";
                                arb.updateAssetStatusChange(query);
//                                System.out.println("======query in GroupPrpcess: "+query);
                                
                            	String qb = "update am_gb_bulkTransfer set STATUS = 'APPROVED' where Batch_id = '"+Integer.toString(tranId)+"'";
                            	arb.updateAssetStatusChange(qb);
                            	String aq = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where id = '"+Integer.toString(tranId)+"'";
                            	arb.updateAssetStatusChange(aq);	
                                String qa = "update am_asset_approval set asset_status='Bulk Asset Transfer',process_status = 'A' where transaction_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(qa);

                            	
//                                String page1 = "BULK ASSET TRANSFER RAISE ENTRY";
//                                String iso = "000";
//                                String costdebitAcctName = ""; 
//                                String costcreditAcctName = "";
//                                String raiseEntryNarration = Description+" "+asset_id+" disposed";
//                                String costDrAcct = ""; String costCrAcct = "";
//                                String finacleTransId = "1";
//                                String recType = "";
//                                String transId = "4";
                                aprecords.insertApprovalx(String.valueOf(tranId), description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
//                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }                            
                               approverManager.createApprovalRemark(approvalRemark);

                               // asset_manager.updateAssetDepreciation(asset_id);
                               
                           	String subjectr ="Bulk Asset Transfer Approval";
                           	String msgText11 ="Your Bulk Asset Transfer Update with GROUP ID: "+ asset_id +" has been approved.";
//                           	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                           	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                               comp.insertMailRecords(mailAddress,subjectr,msgText11);
                               
                           } else {
                               arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                               approverManager.createApprovalRemark(approvalRemark);
                           }
                           //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                       }//inner if
                    
                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer Acceptance")) {
//                           System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
//                           System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                    	String page = "BULK ASSET TRANSFER RAISE ENTRY";
                    	 returnPage = "BulkTranferAcceptanceList";
                           approvalRemark.setApprovalLevel(approvalCount);
                           approvalRemark.setRemark("");
                           approvalRemark.setStatus("Approved");
                           approvalRemark.setTranType("Bulk Asset Transfer Acceptance");
                           String url = "DocumentHelp.jsp?np=bulkAssetTransferPosting&id=" + tranId + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
//                           System.out.println("url here >>>>>>>>>>>>>>>>>>>> "+ url+" approvalCount: "+approvalCount);
                           if (approvalCount == tranLevel) {
                        	   
//                               
//                               arb.deleteOtherUsers(String.valueOf(tranId),userid);
//                               System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
//                               String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                               System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
//                               if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
//                               if (singleApproval.equalsIgnoreCase("N")) {
//                               arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                               }
                               
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                        	  
                               String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                               arb.updateAssetStatusChange(q);

                               ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkAssetTransfer");
//                               System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                               BulkUpdateManager bum = new BulkUpdateManager();
           //                    bum.BulkAssetTransfer(newAssetList);
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                               description = "Bulk Asset Transfer with Batch Id "+tranId+"";
                               int tranlent = String.valueOf(tranId).length();
//                             System.out.println("the for the tranlent is >>>>>>>>>>>>>>>> " + tranlent);                               
                       		String revalue_query = "update am_assettransfer set approval_Status='APPROVED' where substring(CAST(transfer_id AS varchar(38)),1,"+tranlent+") = '"+tranId+"'";
                       		arb.updateAssetStatusChange(revalue_query);
//                       	 System.out.println("the for the revalue_query is >>>>>>>>>>>>>>>> " + revalue_query); 
                                String q3 = "update am_gb_bulkTransfer set Status='APPROVED' where Batch_id = '"+ Integer.toString(tranId)+"'";
                                   arb.updateAssetStatusChange(q3);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//                                  System.out.println("about to send Bulk raiseentry for trasfer " + tranId);
                                  aprecords.insertApprovalx(String.valueOf(tranId), description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                              }
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                                String query= "UPDATE a SET a.Asset_Id = b.NEW_ASSET_ID,a.OLD_ASSET_ID = b.Asset_Id,a.Branch_ID = b.newbranch_id," +
                                        "a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.SBU_CODE = b.newSBU_CODE,a.Section_id = b.newsection_id," +
                                        "a.dept_id = b.newdept_id,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE," +
                                        "a.Asset_User = b.newAsset_User FROM am_asset a,am_gb_bulkTransfer b WHERE a.Asset_Id = b.Asset_Id " +
                                        "AND b.Batch_Id = '"+Integer.toString(tranId)+"'";
                                arb.updateAssetStatusChange(query);
//                                System.out.println("======query in GroupPrpcess: "+query);
                                
                            	String qb = "update am_gb_bulkTransfer set STATUS = 'POSTED' where Batch_id = '"+Integer.toString(tranId)+"'";
                            	arb.updateAssetStatusChange(qb);
                            	String aq = "update am_raisentry_post set entryPostFlag = 'Y',GroupIdStatus = 'Y' where id = '"+Integer.toString(tranId)+"'";
                            	arb.updateAssetStatusChange(aq);	
                                String qa = "update am_asset_approval set asset_status='Bulk Asset Transfer',process_status = 'A' where transaction_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(qa);

                                aprecords.insertApprovalx(String.valueOf(tranId), description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);                            	
//                                String page1 = "BULK ASSET TRANSFER RAISE ENTRY";
//                                String iso = "000";
//                                String costdebitAcctName = ""; 
//                                String costcreditAcctName = "";
//                                String raiseEntryNarration = Description+" "+asset_id+" disposed";
//                                String costDrAcct = ""; String costCrAcct = "";
//                                String finacleTransId = "1";
//                                String recType = "";
//                                String transId = "4";
                                
//                     	       arb.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                            }                            
                               approverManager.createApprovalRemark(approvalRemark);

                               // asset_manager.updateAssetDepreciation(asset_id);

                               alertmessage = "Bulk Asset Transfer Accepted";

                           	String subjectr ="Bulk Asset Transfer Acceptance Approval";
                           	String msgText11 ="Your Bulk Asset Transfer Update Asset Update with GROUP ID: "+ asset_id +" has been approved.";
                           	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                           	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                               comp.insertMailRecords(mailAddress,subjectr,msgText11);
                               
                           } else {
                               arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                               approverManager.createApprovalRemark(approvalRemark);
                           }
                           //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                       }//inner if                    
                    
                    if (full_status.equalsIgnoreCase("Bulk Asset2 Transfer")) {
                        //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                           approvalRemark.setApprovalLevel(approvalCount);
                           approvalRemark.setRemark("");
                           approvalRemark.setStatus("Approved");
                           approvalRemark.setTranType("Bulk Asset2 Transfer");

                           arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                           System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                           String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                           System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                           if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                           if (singleApproval.equalsIgnoreCase("N")) {
                           arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                           }
                           if (approvalCount == tranLevel && transAvailable != "0") {
                        //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
//                        	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                               String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset2 Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                               arb.updateAssetStatusChange(q);

                               ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkAsset2Transfer");
//                               System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                               BulkUpdateManager bum = new BulkUpdateManager();
                               bum.BulkAsset2Transfer(newAssetList);
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");

                               int tranlent = String.valueOf(tranId).length();
//                             System.out.println("the for the tranlent is >>>>>>>>>>>>>>>> " + tranlent);                               
                       		String revalue_query = "update am_asset2transfer set approval_Status='ACTIVE' where substring(CAST(transfer_id AS varchar(38)),1,"+tranlent+") = '"+tranId+"'";
                       		arb.updateAssetStatusChange(revalue_query);

                            String up1 = "UPDATE am_asset2 SET am_asset2.old_asset_id = am_gb_bulkAsset2Transfer.asset_id,am_asset2.asset_id = am_gb_bulkAsset2Transfer.new_asset_id,"
                            		+"am_asset2.Branch_ID = am_gb_bulkAsset2Transfer.newbranch_id,am_asset2.Dept_ID = am_gb_bulkAsset2Transfer.newdept_id,"
                            		+ "am_asset2.SBU_CODE = am_gb_bulkAsset2Transfer.newsbu_code,am_asset2.Section_id = am_gb_bulkAsset2Transfer.newsection_id,"
                            		+ "am_asset2.Asset_User = am_gb_bulkAsset2Transfer.newAsset_User,am_asset2.BRANCH_CODE = am_ad_branch.BRANCH_CODE,"
                            		+ "am_asset2.DEPT_CODE = am_ad_department.Dept_code,am_asset2.SECTION_CODE = am_ad_section.Section_Code "
                            		+ "FROM am_gb_bulkAsset2Transfer, am_asset2, am_ad_branch, am_ad_department,am_ad_section "
                            		+ "WHERE am_gb_bulkAsset2Transfer.ASSET_ID = am_asset2.ASSET_ID "
                            		+ "AND am_gb_bulkAsset2Transfer.NEWBRANCH_ID = am_ad_branch.BRANCH_ID "
                            		+ "AND am_gb_bulkAsset2Transfer.NEWDEPT_ID = am_ad_department.Dept_ID "
                            		+ "AND am_gb_bulkAsset2Transfer.NEWSECTION_ID = am_ad_section.Section_ID "
                            		+ "AND am_gb_bulkAsset2Transfer.BATCH_ID = '"+tranId+"'";
//                            System.out.println("up1 am_asset2 ================== "+up1);
                            arb.updateAssetStatusChange(up1); 
   //                         String up2 = "update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET2 TRANSFER RAISE ENTRY'";
//                            arb.updateAssetStatusChange(up2); 
//                            String up3 = "update am_asset2Transfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ";
//                            arb.updateAssetStatusChange(up3);  
                           

                               approverManager.createApprovalRemark(approvalRemark);

                               // asset_manager.updateAssetDepreciation(asset_id);

                              	String subjectr ="Bulk Asset2 Transfer Approval";
                              	String msgText11 ="Your Bulk Asset2 Transfer with GROUP ID: "+ asset_id +" has been approved.";
                              	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                              	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                                  comp.insertMailRecords(mailAddress,subjectr,msgText11);

                           } else {
                               arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                               approverManager.createApprovalRemark(approvalRemark);
                           }
                           //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                       }//inner if


                    if (full_status.equalsIgnoreCase("Asset Verification Workbook")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
                    	
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Verification");

                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

                     //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);
                            String qw = "update am_gb_workbookupdate set PROCESS_STATUS='APPROVED' where BATCH_ID= '"+tranId+"'";
                            arb.updateAssetStatusChange(qw);
                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("assetWorkbook");
                      //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
    //                        BulkUpdateManager bum = new BulkUpdateManager();
   //                         bum.updateBulkAsset(newAssetList);

                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            // asset_manager.updateAssetDepreciation(asset_id);
                     		 String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                   		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                   		 String subject ="Asset Verification Workbook Approved";
                   			String msgText1 = "Your Asset Verification Workbook with Batch Id: "+ tranId +" has been approved.";
                   			System.out.println("#$$$$$$$$$$$ "+createdby);
                   			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                   			mail.sendMail(createdby,subject,msgText1);
                   			msgText1 = "Asset Verification Workbook with Batch Id: "+ tranId +" has been sent to you from Branch '"+branchName+"'.";
                   			String tomail = aprecords.getCodeName("select mail_address from am_mail_statement where mail_code='36'");
                   			mail.sendMail(tomail,subject,msgText1);

                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Asset Verification Selection Processing")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
                    	
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Verification Processing");

                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (singleApproval.equalsIgnoreCase("N")) {
                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
                        }
                        
                        if (approvalCount == tranLevel && transAvailable != "0") {

                     //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);
                            String qw = "update am_gb_workbookselection set PROCESS_STATUS='APPROVED' where BATCH_ID= '"+tranId+"'";
                            arb.updateAssetStatusChange(qw);
                            ArrayList newSelectionList = (ArrayList) request.getSession().getAttribute("assetVerificationSelectionApproval");
                      //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            arb.bulkAssetUpdateFromVerification(tranId);
                            boolean result = arb.bulkAssetUpdateFromVerification(tranId);
                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            // asset_manager.updateAssetDepreciation(asset_id);
                      		 String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                    		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                    		 String subject ="Asset Verification Selection Processing Approved";
                    			String msgText1 = "Your Asset Verification Selection Processing with Batch Id: "+ tranId +" has been approved.";
                    			System.out.println("#$$$$$$$$$$$ "+createdby);
                    			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                    			mail.sendMail(createdby,subject,msgText1);
                    			msgText1 = "Asset Verification Selection Processing with Batch Id: "+ tranId +" has been sent to you from Branch '"+branchName+"'.";
                    			String tomail = aprecords.getCodeName("select mail_address from am_mail_statement where mail_code='37'");
                    			mail.sendMail(tomail,subject,msgText1);

                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if   

                }//outer if

                if (full_status.equalsIgnoreCase("Branch Asset Proof")) {
                    System.out.println("here approvalCount>>>>>>>>>>>>>>: "+approvalCount+"   tranLevel: "+tranLevel+"   full_status: "+full_status);
                	
                    approvalRemark.setApprovalLevel(approvalCount);
                    approvalRemark.setRemark("");
                    approvalRemark.setStatus("Approved");
                    approvalRemark.setTranType("Branch Asset Proof");
        			
                    arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                    System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                    String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                    if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                    if (singleApproval.equalsIgnoreCase("N")) {
                    arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"44",Integer.parseInt(batchId),assetCode,userid);  
                    }

                    if (approvalCount == tranLevel && transAvailable != "0") {
                 //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                    	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+prooftranId+"' and process_status ='P' ");
                        String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranIdApproval;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_Asset_Proof set PROCESS_STATUS='APPROVED', APPROVED_DATE = '"+approveddate+"' where BATCH_ID= '"+prooftranId+"' and PROCESS_STATUS ='WFA' ";
                        arb.updateAssetStatusChange(qw);
                        ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("AssetProofByBranchApproval");
                        
                  //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
//                        BulkUpdateManager bum = new BulkUpdateManager();
//                         bum.updateBulkAsset(newAssetList);

                        // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                        //    arb.updateAssetStatusChange(q3);

                        approverManager.createApprovalRemark(approvalRemark);
 
                        // asset_manager.updateAssetDepreciation(asset_id);
               		 String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
            		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
            		 String subject ="Branch Asset Proof Approved";
            			String msgText1 = "Your Branch Asset Proof with Batch Id: "+ prooftranId +" has been approved.";
            			System.out.println("#$$$$$$$$$$$ "+createdby);
            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);
            			msgText1 = "Branch Asset Proof with Batch Id: "+ prooftranId +" has been sent to you from Branch '"+branchName+"' for Processing.";
            			String tomail = aprecords.getCodeName("select mail_address from am_mail_statement where mail_code='44'");
            			String otherparam = "AssetProofByBranchApproval&tranId="+tranId+"&transaction_level=1&approval_level_count=0";
            			 subject ="Asset Proof From Branch";
            			System.out.println("tomail #$$$$$$$$$$$ "+tomail);
            			otherparam = "bulkAssetProofSelection&tranId="+tranId+"&transaction_level=1&approval_level_count=0";
//            			mail.sendMailwithAddress(tomail, subject, msgText1,otherparam);	
            			mail.sendMailwithMultipeAddress(tomail, subject, msgText1,otherparam);	

                    } else {
                        arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                        approverManager.createApprovalRemark(approvalRemark);
                    }
                    //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                }//inner if

  //              if (full_status.equalsIgnoreCase("Branch Asset Proof Processing")) {
//                	System.out.println("here full_status>>>>>>>>>>>>>>: "+full_status+"    astatus: "+astatus);
            	 if (full_status.equalsIgnoreCase("Asset Proof Processing Approval") && astatus.equalsIgnoreCase("A")) {
//                    System.out.println("here approvalCount>>>>>>>>>>>>>>: "+approvalCount+"   tranLevel: "+tranLevel+"   full_status: "+full_status);
                	
                    approvalRemark.setApprovalLevel(approvalCount);
                    approvalRemark.setRemark("");
                    approvalRemark.setStatus("Approved");
                    alertmessage = "Transaction Approved";
                    approvalRemark.setTranType("Asset Proof Processing Approval");

                    arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                    System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                    String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                    if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                    if (singleApproval.equalsIgnoreCase("N")) {
                    	 arb.setPendingMultiApprTransArchive(arb.setApprovalDataUploadGroup(tranId,tableName),"45",Integer.parseInt(batchId),assetCode,userid);  
                    }
                    
                    if (approvalCount == tranLevel && transAvailable != "0") {

//                        System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                        String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + transactionId;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_Asset_Proof_Selection set SELECT_DATE = '"+approveddate+"',PROCESS_STATUS = 'APPROVED' where BATCH_ID= '"+prooftranId+"'";
                        arb.updateAssetStatusChange(qw);
//                        String qa = "update am_Asset_Proof set PROCESS_STATUS='PROCESSED', SELECT_DATE = '"+approveddate+"' where BATCH_ID= '"+tranId+"'";
//                        arb.updateAssetStatusChange(qa);                        
                        arb.bulkAssetUpdateFromProof(prooftranId);
          //              ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("bulkAssetProofSelectionApproval");
                  //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
//                        BulkUpdateManager bum = new BulkUpdateManager();
//                         bum.updateBulkAsset(newAssetList);

                        // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                        //    arb.updateAssetStatusChange(q3);

                        approverManager.createApprovalRemark(approvalRemark);
                        boolean result = grpAsset.UpdateAssetProofApproval(prooftranId);
                        // asset_manager.updateAssetDepreciation(asset_id);
               		 String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id="+transactionId+" ");
            		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
            		 String subject ="Branch Asset Proof Processing Approved";
            			String msgText1 = "Your Branch Asset Proof Processing with Batch Id: "+ tranId +" and Branch Name '"+branchName+"' has been approved.";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);
            		//	msgText1 = "Bulk Asset Proof Processing with Batch Id: "+ tranId +" has been sent to you from Branch '"+branchName+"'.";
            		//	String tomail = aprecords.getCodeName("select mail_address from am_mail_statement where mail_code='45'");
            		//	String otherparam = "bulkAssetProofSelectionApproval&tranId="+tranId+"&transaction_level=1&approval_level_count=0";
            			//mail.sendMail(tomail,subject,msgText1);
            		//	mail.sendMailwithAddress(tomail, subject, msgText1,otherparam);	

                    } else {
                        arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                        approverManager.createApprovalRemark(approvalRemark);
                    }
                    //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                }//inner if

                 //              if (full_status.equalsIgnoreCase("Branch Asset Proof Processing")) {
//                           	System.out.println("here full_status>>>>>>>>>>>>>>==: "+full_status+"    astatus: "+astatus);
                       	 if (full_status.equalsIgnoreCase("PPMSchedule Approval") && astatus.equalsIgnoreCase("A")) {
//                               System.out.println("here approvalCount>>>>>>>>>>>>>>: "+approvalCount+"   tranLevel: "+tranLevel+"   full_status: "+full_status);
                           	
                               approvalRemark.setApprovalLevel(approvalCount);
                               approvalRemark.setRemark("");
                               approvalRemark.setStatus("Approved");
                               alertmessage = "Transaction Approved";
                               approvalRemark.setTranType("PPMSchedule Approval");
                               String trans_Id = aprecords.getCodeName("select transaction_id from am_asset_approval where asset_id='"+batchId+"' ");

                               arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                               System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                               String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                               System.out.println("Value of transAvailable is >>>>>> " + transAvailable+"   batchId: "+batchId+"     asset_id: "+asset_id+"     tranId: "+tranId);
                               if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                               if (singleApproval.equalsIgnoreCase("N")) {
                               arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",tranId,assetCode,userid); 
                               }
                               
                               if (approvalCount == tranLevel && transAvailable != "0") {

//                                   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.5");
                                   String q = "update am_asset_approval set process_status='A', asset_status='ACTIVE',DATE_APPROVED = '"+approveddate+"' where asset_id='"+batchId+"'";
                                   arb.updateAssetStatusChange(q);
                                   String qinsert = "INSERT INTO FM_PPM (ID,TRANSID,BRANCH_CODE,CATEGORY_CODE,SUB_CATEGORY_CODE,GROUP_ID,VENDOR_CODE,DESCRIPTION,LASTSERVICE_DATE,"+
               										"Q1_DUE_DATE,Q1_STATUS,Q2_DUE_DATE,Q2_STATUS,Q3_DUE_DATE,Q3_STATUS,"+
               										"Q4_DUE_DATE,Q4_STATUS,TYPE,STATUS,POSTING_DATE) (SELECT ID,TRANSID,BRANCH_CODE,CATEGORY_CODE,SUB_CATEGORY_CODE,GROUP_ID,VENDOR_CODE,DESCRIPTION,LASTSERVICE_DATE,"+
               										"Q1_DUE_DATE,Q1_STATUS,Q2_DUE_DATE,Q2_STATUS,Q3_DUE_DATE,Q3_STATUS,"+
               										"Q4_DUE_DATE,Q4_STATUS,TYPE,'ACTIVE','"+approveddate+"' FROM FM_PPM_TMP WHERE TRANSID = '"+batchId+"')";
                                   arb.updateAssetStatusChange(qinsert);
                                   approverManager.createApprovalRemark(approvalRemark);
                                   
                          		 String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where asset_id='"+batchId+"' ");
                       		 String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                       		 String subject ="PPM Schedule Processing Approved";
                       			String msgText1 = "Your PPM Schedule Processing with Batch Id: "+ batchId +" and Branch Name '"+branchName+"' has been approved.";
//                       			System.out.println("#$$$$$$$$$$$ "+createdby);
//                       			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                       			mail.sendMail(createdby,subject,msgText1);

                               } else {
                                   arb.incrementApprovalCount2(Integer.parseInt(trans_Id), approvalCount, supervisor);
                                   approverManager.createApprovalRemark(approvalRemark);
                               }

                           }//inner if
                                                          
                else
                {
                    if (full_status.equalsIgnoreCase("Depreciation Adjusted") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='No Asset  Depreciation Adjusted', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Depreciation Adjustment");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Depreciation Adjusted Rejected";
                    	String msgText11 ="Your Depreciation Adjusted with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Uncapitalized Asset Update") && astatus.equalsIgnoreCase("R")) {
                    	arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='None of BulkUncapitalized Asset Updated ', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Uncapitalized Asset Update");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Bulk Uncapitalized Asset Update Rejected";
                    	String msgText11 ="Your Bulk Uncapitalized Asset Update with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Asset Update") && astatus.equalsIgnoreCase("R")) {
                    	arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='None of Bulk Asset Updated ', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Update");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Bulk Asset Update Rejected";
                    	String msgText11 ="Your Bulk Asset Update with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Material Retrieval") && astatus.equalsIgnoreCase("R")) {
                    	arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Material Retrieval', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        String ap = "UPDATE FT_MAINTENANCE_DETAILS_TMP SET STATUS = 'REJECTED' where BATCH_ID=" + batchId;
                        arb.updateAssetStatusChange(ap);
                        
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Material Retrieval");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Material Retrieval Rejected";
                    	String msgText11 ="Your Material Retrieval with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Maintained") && astatus.equalsIgnoreCase("R")) {
                    	arb.deleteOtherSupervisorswithBatchIdSupervisor(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
//                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Maintenance rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String qr = "update am_asset_improvement set approval_status='REJECTED' where Revalue_ID=" + tranId;
//                        System.out.println("Rejected Query: "+qr);
                        alertmessage = "Transaction Rejected";
                        arb.updateAssetStatusChange(q);
                        arb.updateAssetStatusChange(qr);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Maintenance");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Asset Improvement Rejected";
                    	String msgText11 ="Your Asset Improvement with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if


                    if (full_status.equalsIgnoreCase("Uncapitalized Improvement") && astatus.equalsIgnoreCase("R")) {
                    	arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
//                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Maintenance rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String qr = "update am_Uncapitalized_improvement set approval_status='REJECTED' where Revalue_ID=" + tranId;
//                        System.out.println("Rejected Query: "+qr);
                        alertmessage = "Uncapitalized Improvement Transaction Rejected";
                        arb.updateAssetStatusChange(q);
                        arb.updateAssetStatusChange(qr);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Uncapitalized Improvement");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Uncapitalized Improvement Rejected";
                    	String msgText11 ="Your Uncapitalized Improvement with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Transferred") && astatus.equalsIgnoreCase("R"))
                    {
                    	// arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                    	String deleteQuery = "DELETE FROM am_asset_approval WHERE transaction_id = '"+tranId+"' and super_id != '"+userid+"'";
                    	 arb.updateAssetStatusChange(deleteQuery);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String revalueReason = "update am_assettransfer set approval_Status='REJECTED' where transfer_id = '" + tranId + "'";
                        arb.updateAssetStatusChange(revalueReason);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for asset transfer with ID " + asset_id + " has been Rejected. The new asset ID is " + asset_id;
                        String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_id = '"+tranId+"' ");
                        String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress, "Asset Transfer", msgText1);

                    }//inner if
                    // Bulk Asset Transfer Rejection
                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer") && astatus.equalsIgnoreCase("R"))
                    {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_gb_bulkTransfer where Batch_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(bulkquery);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for Bulk asset transfer with Batch ID " + tranId + " has been Rejected. The new Batch ID is " + tranId;
                       // mail.sendMailTransactionInitiator(tranId, "Bulk Asset Transfer", msgText1);
                        String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_id = '"+tranId+"' ");
                        String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress, "Bulk Asset Transfer", msgText1);
                    }//inner if                    

                    if (full_status.equalsIgnoreCase("Asset Transfer Acceptance") && astatus.equalsIgnoreCase("R"))
                    {
                    	// arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                    	String deleteQuery = "DELETE FROM am_asset_approval WHERE transaction_id = '"+tranId+"' and super_id != '"+userid+"'";
                    	arb.updateAssetStatusChange(deleteQuery);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String revalueReason = "update am_assettransfer set approval_Status='REJECTED' where transfer_id = '" + tranId + "'";
                        arb.updateAssetStatusChange(revalueReason);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        returnPage = "BulkTranferAcceptanceList";
                        String msgText1 = "Your transaction for asset transfer with ID " + asset_id + " has been Rejected. The new asset ID is " + asset_id;
                       // mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);
                        String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_id = '"+tranId+"' ");
                        String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress, "Asset Transfer", msgText1);
                        

                    }//inner if
                    // Bulk Asset Transfer Rejection
                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer Acceptance") && astatus.equalsIgnoreCase("R"))
                    {
                    	arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_gb_bulkTransfer where Batch_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(bulkquery);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for Bulk asset transfer with Batch ID " + tranId + " has been Rejected. The new Batch ID is " + tranId;
                       // mail.sendMailTransactionInitiator(tranId, "Bulk Asset Transfer", msgText1);
                        String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_id = '"+tranId+"' ");
                        String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress, "Bulk Asset Transfer", msgText1);
                    }//inner if                    

                    // Bulk Asset Transfer Rejection
                    if (full_status.equalsIgnoreCase("Bulk Asset2 Transfer") && astatus.equalsIgnoreCase("R"))
                    {
                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
//                        if (singleApproval.equalsIgnoreCase("N")) {
//                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                        }
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_gb_bulkAsset2Transfer where Batch_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(bulkquery);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset2 Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for Bulk asset2 transfer with Batch ID " + tranId + " has been Rejected. The new Batch ID is " + tranId;
                        //mail.sendMailTransactionInitiator(tranId, "Bulk Asset2 Transfer", msgText1);
                        String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_id = '"+tranId+"' ");
                        String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress, "Bulk Asset2 Transfer", msgText1);
                    }//inner if                    

                    
                    if (full_status.equalsIgnoreCase("WIP") && astatus.equalsIgnoreCase("R"))
                    {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        String q = "update am_asset_approval set process_status='R', " +
                                " " +
                                "asset_status='WIP Reclassification Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' " +
                                "where transaction_id=" + tranId;

                       // System.out.println("reject query in WIP >>>>>>>>>>>> " + q);

                        arb.updateAssetStatusChange(q);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("WIP RECLASSIFICATION");
                        approverManager.createApprovalRemark(approvalRemark);

                        String revalue_query = " update am_wip_RECLASSIFICATION set approval_Status='REJECTED' " +
                                   " where transfer_id = '" + tranId + "'";
                       // System.out.println("reject query in WIP Table >>>>>>>>>>>> " + revalue_query);

                        arb.updateAssetStatusChange(revalue_query);
                        
                    	String subjectr ="WIP Asset Rejected";
                    	String msgText11 ="Your WIP Asset with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//inner if

                    if (full_status.equalsIgnoreCase("Revalued") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset revaluation rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Revaluation");
                        approverManager.createApprovalRemark(approvalRemark);

                    }//


                    if (full_status.equalsIgnoreCase("Reclassified") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset reclassification rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        alertmessage = "Transaction Rejected";
                        arb.updateAssetStatusChange(q);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Reclassification");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Asset Reclassified Rejected";
                    	String msgText11 ="Your Asset Reclassified with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                        
                    }//


                    if (full_status.equalsIgnoreCase("Disposed") && astatus.equalsIgnoreCase("R")) {

                        arb.deleteOtherSupervisorswithAssetId(asset_id,userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
//                        if (singleApproval.equalsIgnoreCase("N")) {
//                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                        }
//                        
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set asset_status='Asset disposal rejected'," +
                                "process_status='R', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);

                        String q1 = "update am_asset set Asset_Status='ACTIVE' where asset_id = '" + asset_id + "' ";
                        arb.updateAssetStatusChange(q1);

                        String q2 = "update am_AssetDisposal set disposal_status='R' where asset_id = '" + asset_id + "' and disposal_status='P'";
                        arb.updateAssetStatusChange(q2);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Disposal");
                        approverManager.createApprovalRemark(approvalRemark);

                        //===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                        //EmailSmsServiceBus mail = new EmailSmsServiceBus();
                        String subjectr = "Asset Disposal Rejection";
                        String msgText11 = "Asset with ID: " + asset_id + " was rejected for disposal.";
                        mail.sendMailUser(asset_id, subjectr, msgText11);



//================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                    }//



                    if (full_status.equalsIgnoreCase("Asset2Disposed") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set asset_status='Asset2 disposal rejected'," +
                                "process_status='R', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);

                        String q1 = "update am_asset2 set Asset_Status='ACTIVE' where asset_id = '" + asset_id + "'";
                        arb.updateAssetStatusChange(q1);

                        String q2 = "update am_Asset2Disposal set disposal_status='R' where Disposal_ID = '" + batchId + "'";
                        arb.updateAssetStatusChange(q2);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset2 Disposal");
                        approverManager.createApprovalRemark(approvalRemark);

                        //===============================BEGIN: THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Asset2 Disposal Rejected";
            			String msgText1 = "Your Asset2 Disposal with Asset Id: "+ asset_id +" has been rejected. Reasons: "+ rr +" ";
            			mail.sendMail(createdby,subject,msgText1);
            			//================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                    }//
                    

                    if (full_status.equalsIgnoreCase("Asset Verification Workbook") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Verification Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_gb_workbookupdate set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+tranId+"'";
                        arb.updateAssetStatusChange(qw);
                        ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("AssetProofByBranch");
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Verification Workbook");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Asset Verification Workbook Rejected";
            			String msgText1 = "Your Asset Verification Workbook with Batch Id: "+ tranId +" has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);

                    }//

                    if (full_status.equalsIgnoreCase("Asset Verification Selection Processing") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Verification Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_gb_workbookselection set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+tranId+"'";
                        arb.updateAssetStatusChange(qw);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Verification Selection Processing ");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Asset Verification Selection Processing Rejected";
            			String msgText1 = "Your Asset Verification Selection Processing with Batch Id: "+ tranId +" has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);

                    }//
                    if (full_status.equalsIgnoreCase("Branch Asset Proof Processing") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
              //      	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+prooftranId+"' and process_status ='P' ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
//                        System.out.println("Q in Rejection#$$$$$$$$$$$ "+q);
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_Asset_Proof set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+prooftranId+"' ";
                        arb.updateAssetStatusChange(qw);       
//                        String qm = "update am_Asset_Proof_Selection set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+tranId+"' ";
//                        arb.updateAssetStatusChange(qm);                             
                        ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("AssetProofByBranch");
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Branch Asset Proof Processing");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Branch Asset Proof Processing Rejected";
            			String msgText1 = "Branch Asset Proof Processing with Batch Id: "+ prooftranId +" has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
//            			 System.out.println("here prooftranId>>>>>>>>>>>>>>: "+prooftranId+"   tranId: "+tranId+"   full_status: "+full_status);
            			mail.sendMail(createdby,subject,msgText1);

                    }//

                    if (full_status.equalsIgnoreCase("Asset Proof Processing Approval") && astatus.equalsIgnoreCase("R")) {
//                    	System.out.println("full_status$$$$$$$$$$$ "+full_status+"   astatus"+astatus);
                    	
                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
//                        if (singleApproval.equalsIgnoreCase("N")) {
//                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                        }
                        
              //      	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+prooftranId+"' and process_status ='P' ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + transactionId;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_Asset_Proof set PROCESS_STATUS='R' where BATCH_ID= '"+prooftranId+"' ";
                        arb.updateAssetStatusChange(qw);       
                        String qm = "update am_Asset_Proof_Selection set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+prooftranId+"' ";
                        arb.updateAssetStatusChange(qm);                             
                        ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("AssetProofByBranchApproval");
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Proof Processing Approval");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select USER_ID from am_asset_approval where ASSET_ID='"+prooftranId+"'");
//                  		System.out.println("createdUserId#$$$$$$$$$$$: "+createdUserId);
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Branch Asset Proof Processing Rejected";
            			String msgText1 = "Branch Asset Proof Processing with Batch Id: "+ prooftranId +" and Branch Name '"+branchName+"' has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            //			 System.out.println("here prooftranId>>>>>>>>>>>>>>: "+prooftranId+"   tranId: "+tranId+"   full_status: "+full_status);
            			mail.sendMail(createdby,subject,msgText1);

                    }//


                    if (full_status.equalsIgnoreCase("Branch Asset Proof") && astatus.equalsIgnoreCase("R")) {
                    	 arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                    	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+prooftranId+"' and process_status ='P' ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Proof Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranIdApproval;
                        arb.updateAssetStatusChange(q);
                        String qw = "update am_Asset_Proof set PROCESS_STATUS='REJECTED' where BATCH_ID= '"+prooftranId+"' and PROCESS_STATUS = 'WFA' ";
                        arb.updateAssetStatusChange(qw);
                        
                        Connection con = null;
                        PreparedStatement ps = null;
                        ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("AssetProofByBranchApproval");
//                        System.out.println("<<<<<<<<<<Array List: "+newAssetList.size());
//	                    BulkUpdateManager bd = new BulkUpdateManager();
//	                    bd.updateProofApprovalComments(newAssetList);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Branch Asset Proof");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where transaction_id='"+tranId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="Branch Asset Proof Rejected";
            			String msgText1 = "Your Branch Asset Proof with Batch Id: "+ prooftranId +" has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);

                    }//

                    if (full_status.equalsIgnoreCase("PPMSchedule Approval") && astatus.equalsIgnoreCase("R")) {
                    	
                        arb.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
//                        if (singleApproval.equalsIgnoreCase("N")) {
//                        arb.setPendingMultiApprTransArchive(arb.setApprovalData(asset_id),"1",Integer.parseInt(batchId),assetCode,userid); 
//                        }
                        
                    	String tranIdApproval =  arb.getCodeName("SELECT transaction_id FROM am_asset_approval WHERE asset_id='"+batchId+"' and process_status ='P' ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='PPM Schedule Rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where asset_id='"+batchId+"'";
                        arb.updateAssetStatusChange(q);
                        String qw = "DELETE FROM FM_PPM_TMP WHERE ID = '"+tranId+"' ";
                        arb.updateAssetStatusChange(qw);
//                        System.out.println("#$$$$$$$$$$$ "+qw);
                        
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("PPMSchedule Approval");
                        approverManager.createApprovalRemark(approvalRemark);
                  		String createdUserId = aprecords.getCodeName("select User_Id from am_asset_approval where Asset_Id='"+batchId+"'");
                		String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                		String subject ="PPM Schedule Rejected";
            			String msgText1 = "Your PPM Schedule with Batch Id: "+ batchId +" has been rejected. Reasons: "+ rr +" ";
//            			System.out.println("#$$$$$$$$$$$ "+createdby);
//            			System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
            			mail.sendMail(createdby,subject,msgText1);

                    }//


                    
                }
            }//else
            //arb.updateAssetStatusChange(supervisorUpdate);

            out.println("<script>alert('"+alertmessage+"')</script>");
            out.println("<script>");
            out.println("window.location='DocumentHelp.jsp?np="+ returnPage +"'");
            // out.println("window.location='DocumentHelp.jsp'");
            out.println("</script>");
        }

        } catch (Exception e) {
            System.out.println("ApprovalServlet: The following error occurred " + e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
