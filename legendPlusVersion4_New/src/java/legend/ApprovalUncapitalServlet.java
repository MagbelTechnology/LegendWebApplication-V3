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
import magma.AssetRecordsBean;
import magma.UncapitalisedRecordsBean;
import magma.asset.manager.AssetManager;
import magma.asset.manager.UncapitaliseManager;
import magma.AssetReclassificationBean;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.legend.bus.ApprovalManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import magma.net.manager.BulkUpdateManager;
import magma.net.manager.BulkUpdateUncapitalisedManager;
import  magma.asset.manager.WIPAssetManager;
import magma.net.dao.MagmaDBConnection;
//import magma.net.manager.AssetManager;
/**
 *
 * @author Olabo
 */
public class ApprovalUncapitalServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 String url = "";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("About to call UncapitaliseManager");
        UncapitaliseManager asset_manager = new UncapitaliseManager();
        System.out.println("About to call ApprovalRecordsd");
        ApprovalRecords aprecords = new ApprovalRecords();
        System.out.println("About to call EmailSmsServiceBus");
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        MagmaDBConnection dbConnection = new MagmaDBConnection();
        String userClass = (String)request.getSession().getAttribute("UserClass");


        try {
        	if (!userClass.equals("NULL") || userClass!=null){
            String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");

int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
            //--System.out.println("\nthe assetCode is >>>>>>>>>>>>>>>>> " + assetCode);
            ApprovalManager approverManager = new ApprovalManager();
            AssetReclassificationBean asset_reclassification = new AssetReclassificationBean();
            UncapitalisedRecordsBean arb = new UncapitalisedRecordsBean();
            CompanyHandler comp = new CompanyHandler();
            AssetRecordsBean ar = new AssetRecordsBean();
            String singleApproval = request.getParameter("singleApproval");
            String astatus = request.getParameter("astatus");
            //String newAsset = request.getParameter("approveBtn");
            String newCategoryID = request.getParameter("newCategoryID");
            String full_status = request.getParameter("full_status");
            System.out.println("<<<<<<full_status: "+full_status+"   astatus: "+astatus);
            String asset_id = request.getParameter("id") == null ? "" : request.getParameter("id");
            String batchId = request.getParameter("batchId");
            System.out.println("<<<<<<asset_id: "+asset_id+"   batchId: "+batchId);
            String rr = request.getParameter("reject_reason");
            String tableName = request.getParameter("tableName") == null ? "" : request.getParameter("tableName");
            String columnName = request.getParameter("columnName");
            String category = request.getParameter("category");

            //for Uncapitalised asset transfer
            String newBranch = request.getParameter("newBranch");
            String newDept = request.getParameter("newDept");
            String newSec = request.getParameter("newSection");
            int userId = Integer.parseInt(request.getParameter("user_id"));
            //for asset reclassification
            String branch = request.getParameter("branch_id");
            String dept = request.getParameter("department_id");
            String section = request.getParameter("section_id");
            String newCategory = request.getParameter("new_category");
            String systemIp = request.getRemoteAddr();
            String Description = request.getParameter("description");
            String costPrice = request.getParameter("COST"); 
            if(costPrice == null || costPrice.equalsIgnoreCase("")){costPrice = "0";}
            costPrice = costPrice.replaceAll(",",""); 
            
            String alertmessage = "Transaction Approved";
            String newAssetID = request.getParameter("newAssetID");

            long tranId = Long.parseLong(request.getParameter("tranId"));

            int tranLevel = Integer.parseInt(request.getParameter("tranLevel"));

            int approvalCount = Integer.parseInt(request.getParameter("approvalCount"));

            String userid = request.getParameter("user_id");
            Timestamp approveddate =  dbConnection.getDateTime(new java.util.Date());
            String asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);

            int supervisor = (request.getParameter("supervisor") == null) ? 0 : Integer.parseInt(request.getParameter("supervisor"));

            // System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>full_status " +full_status);
            // System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>astatus " +astatus);
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
            String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Long.toString(tranId));

            if(oldAssetStatus != null && oldAssetStatus.equalsIgnoreCase("RP")){

                raiseEntryInfo = aprecords.UncapitalizedraiseEntryInfoRepost(asset_id);
               branch_id = arb.getCodeName("select old_branch_id from am_UncapitalizedTransfer where transfer_id="+Long.toString(tranId));
            }else{
            raiseEntryInfo = aprecords.UncapitalizedraiseEntryInfo(asset_id);
            branch_id = raiseEntryInfo[1];
            }

            String description = raiseEntryInfo[0];
            //String branch_id = raiseEntryInfo[1];
            int branchID = branch_id == null || branch_id.equals("") ? 0 : Integer.parseInt(branch_id);
            String subject_to_vat = raiseEntryInfo[2];
            String wh_tax = raiseEntryInfo[3];
            String flag = "";
            String partPay = "";
             asset_User_Name = aprecords.getCodeName("SELECT full_name from am_gb_user where user_id = " + userId);
            String branchName = aprecords.getCodeName(" SELECT branch_name from am_ad_branch where branch_id= " + branchID);

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
                    // System.out.println(" here>>>>>>>>>>>>>>>>>>>>> 1.1");
                    approvalCount += 1;

                    if (full_status.equalsIgnoreCase("Uncapitalized Asset Disposal"))
                    {
                        // approvalCount += 1;

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Uncapitalized Asset Disposal");

                        if (approvalCount == tranLevel)
                        {
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL
//                            ar.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
                            ar.deleteOtherSupervisors(asset_id,userid);
//                          System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                          String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                          System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                          if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                          if (singleApproval.equalsIgnoreCase("N")) {
                        	  System.out.println("Value of asset_id is >>>>>> " + asset_id);
                          ar.setPendingMultiApprTransArchive(ar.setApprovalDataBranch(asset_id),"20",Long.parseLong(batchId),assetCode,userid); 
                          aprecords.updateRaiseEntry(asset_id);
                          }
                            String page = "ASSET DISPOSAL RAISE ENTRY";
                            url = "DocumentHelp.jsp?np=disposeUncapitalizedAssetRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageUncapitalDisposals&pageDirect=Y";
                            String u = "update AM_ASSET_UNCAPITALIZED set asset_status='Disposed' where asset_id = '"+asset_id+"'";
                            arb.updateAssetStatusChange(u);                            
                            System.out.println("u");
                            // System.out.println("in equality ===========================================");
                            String q = "update am_asset_approval set process_status='A', asset_status='DISPOSED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            //--String q2 = "update am_AssetDisposal set disposal_status='D' where asset_id = '"+asset_id+"'";
                            //--arb.updateAssetStatusChange(q2);

                            //--String q3 = "update am_asset set Asset_Status='Disposed' where asset_id = '"+asset_id+"'";
                            //--arb.updateAssetStatusChange(q3);

                           // String q3 = "update am_asset set Asset_Status='PARTIALLY DISPOSED' where asset_id = '" + asset_id + "'";
                            //arb.updateAssetStatusChange(q3);
                            //System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q3);
                            
//                            String q4 = "update am_UncapitalizedDisposal set disposal_status='D' where asset_id = '" + asset_id + "'";
//                            arb.updateAssetStatusChange(q4);
//                            System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q4);
/* 09-03-2012
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
*/
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, batchId,assetCode);
                            }
                            
                            
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                                String qp = "update AM_ASSET_UNCAPITALIZED set Asset_Status='Disposed' where Asset_id = '" + asset_id + "'";
                                arb.updateAssetStatusChange(qp);
                                String qa = "update am_asset_approval set asset_status='DISPOSED',process_status = 'A' where transaction_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(qa);
                                String qd = "update am_UncapitalizedDisposal set disposal_status='P' where Disposal_ID = '" + batchId + "'";
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
                     	       ar.insertRaiseEntryTransaction(String.valueOf(userId),raiseEntryNarration,costDrAcct,costCrAcct,Double.valueOf(costPrice),iso,asset_id,page1,transId,systemIp,String.valueOf(tranId),assetCode,finacleTransId,costdebitAcctName,costcreditAcctName,String.valueOf(userId),recType);
                     	      aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, batchId,assetCode);
                            }
                            
                            aprecords.updateRaiseEntry(asset_id);
                            
                        	String subjectr ="Uncapitalized Asset Disposal Approval";
                        	String msgText11 ="Your Uncapitalized Asset Disposal with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                           
                            approverManager.createApprovalRemark(approvalRemark);




                            //================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET DISPOSAL
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

                        if (approvalCount == tranLevel)
                        {

                            String page = "ASSET RECLASSIFICATION RAISE ENTRY";
                             url = "";//DocumentHelp.jsp?np=assetReclassRaiseEntry&id="+asset_id+"&pageDirect=Y";

                            String q = "update am_asset_approval set process_status='A',asset_status='Reclassified' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                            //asset_reclassification.updateAssetReclassification(asset_id);
                            asset_reclassification.updateAssetReclassification(asset_id,tranId);
                            asset_manager.updateAssetReclass(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);

                             String newAsset_id = "";
                            try
                            {

                                AutoIDSetup aid_setup = new AutoIDSetup();

                                System.out.print("######### the category id is" + newCategory);
                                System.out.print("######### the branch id is" + branch);
                                System.out.print("######### the section id is" + section);
                                System.out.print("######### the dept id is" + dept);

//String cat_id = aid_setup.getCategoryID(category);
                                newAsset_id = aid_setup.getIdentity(branch, dept, section, newCategory);
                                System.out.println("The new asset ID after being reclassified is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                url = "DocumentHelp.jsp?np=assetReclassRaiseEntry&id=" + newAsset_id + "&pageDirect=Y";

                                //stop update on am_asset
                                String change_id_query2 = "";
                              //  String change_id_query2 = "update am_asset set old_asset_id ='" + asset_id + "', asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";

//System.out.print("######### the query  is " + change_id_query2);
                                arb.updateAssetStatusChange(change_id_query2);
//id=newAsset_id;
                            } catch (Exception e) {
                                System.out.println("Error occurred in approval servlet when reclassifying asset " + e);
                            } catch (Throwable t) {
                                System.out.println("Throwable occurred in approval servlet when reclassifying asset" + t);
                            }

                    String change_id_query3 = "update am_assetReclassification set  new_asset_id='"+newAsset_id+"' where Reclassify_ID ="+tranId;
                    arb.updateAssetStatusChange(change_id_query3);

                           // aprecords.insertApproval(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId);

                    if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                    aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                    }
                    // aprecords.updateRaiseEntry(asset_id);
                       //stop update on am_asset
                        //    aprecords.updateRaiseEntry(newAsset_id);


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
                             url = "DocumentHelp.jsp?np=assetRevalueRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageRevaluation&pageDirect=Y";

                            System.out.println("===============here in revalued=============");
                            String q = "update am_asset_approval set process_status='A',asset_status='Revalued' where transaction_id = '" + tranId + "'";
                            String q3 = "update am_assetrevalue set approval_Status='ACTIVE' where revalue_id = '" + Long.toString(tranId) + "'";
                            arb.updateAssetStatusChange(q);
                            arb.updateAssetStatusChange(q3);
                            asset_manager.updateAssetRevalue(asset_id);

                            approverManager.createApprovalRemark(approvalRemark);
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

                    }//inner if


                    if (full_status.equalsIgnoreCase("Transferred"))
                    {
                        System.out.println("===============here in transfered method of approval servlet=============");
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Uncapitalised Asset Transfer");
                        
                        ar.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
//                      System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                      String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                      System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                      if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                      

                        if (approvalCount == tranLevel && transAvailable != "0") {
                        	
 //                          ar.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
//                          System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
//                          String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
//                          System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
//                          if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+tranId+"' and super_Id = '"+userId+"'  and asset_status = 'PENDING' ");
                        	String userName = arb.getCodeName("select a.User_Name from am_gb_user a,am_ad_branch b, am_gb_company c "
                        			+ "where a.Branch = b.BRANCH_ID and b.BRANCH_CODE = c.default_branch and a.user_id = "+InitiatorId+"");
                        	System.out.println("Value of userName is >>>>>> " + userName);                        	
                          if (singleApproval.equalsIgnoreCase("N")) {
                          ar.setPendingMultiApprTransArchive(ar.setApprovalData(asset_id),"20",Integer.parseInt(batchId),assetCode,userid); 
                          aprecords.updateRaiseEntry(asset_id);
                          }

                            //String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Integer.toString(tranId));
                            String newAsset_id = "";
                            String page = "ASSET TRANSFER RAISE ENTRY";
//                            url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

                            String q = "update am_asset_approval set process_status='A',asset_status='Transferred' where transaction_id = '" + tranId + "'";
                             arb.updateAssetStatusChange(q);

                            System.out.println("the asset id is ?????????????????????? "+asset_id);
                            System.out.println("the transaction id is ?????????????????????? "+tranId);
                            //asset_manager.updateAssetTransfer(asset_id,tranId);

                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.net.manager.AssetManager assetManager = new magma.net.manager.AssetManager();
                            try {
                                String change_id_query2="";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String cat_id = aid_setup.getCategoryID(category);
                                System.out.println("@@@ the category id is" + cat_id);
                                System.out.println("@@@ the newBranch id is" + newBranch);
                                System.out.println("@@@ the newDept id is" + newDept);
                                System.out.println("@@@ the newSec id is" + newSec);
                                
                         	   if(userName.equalsIgnoreCase("")){
                                   String qa = "update am_asset_approval set process_status='WA', asset_status='Asset Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                                   arb.updateAssetStatusChange(qa);
                            	   		}

                                //newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);

//                                String q3 = "update am_UncapitalizedTransfer set approval_Status='ACTIVE', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
//                                arb.updateAssetStatusChange(q3);

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




//                                String revalue_query = "update am_UncapitalizedTransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
//                                //String revalue_query = "update am_UncapitalizedTransfer set Approval_Status='ACTIVE',old_asset_id ='" + asset_id + "', asset_id ='" + newAsset_id + "' where asset_id = '" + asset_id + "'";
//                                System.out.println("======revalue_query=====> "+revalue_query);
//                                arb.updateAssetStatusChange(revalue_query);


                                //comment the two lines below to revert to asset transfer scenario where original and transfered assets
                                // will be available in am_asset table.

                                //assetManager.transferAssetsUpdate(asset_id, newAsset_id);
                                //assetManager.updateOldAssetWithApproval(asset_id, newAsset_id,tranId);
                               url = "DocumentHelp.jsp?np=assetUncapitalizedTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageUncapitalTransfers&pageDirect=Y";
                                //--CHANGE ASSET ID TO NEWLY GENERATED ID
                                //--String change_id_query = "update am_asset set old_asset_id ='" + asset_id + "' where asset_id =' " + newAsset_id + " '";
                                //--arb.updateAssetStatusChange(change_id_query);

                                //--String change_id_query2 = "update am_asset set asset_status ='INACTIVE' where asset_id =' " + asset_id + " '";
                                //--arb.updateAssetStatusChange(change_id_query2);

//                                String msgText1 = "Your transaction for Uncapitalised asset transfer with ID " + asset_id + " has been approved. The new asset ID is " + newAsset_id;
//                                mail.sendMailTransactionInitiator(tranId, "Uncapitalised Asset Transfer", msgText1);

                                System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                                if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                                  System.out.println("about to send Transaction to raiseentry for trasfer " + assetCode);
                                  String q3 = "update am_UncapitalizedTransfer set approval_Status='APPROVED' where transfer_id = '" + Long.toString(tranId) + "'";
                                  arb.updateAssetStatusChange(q3);                                  
                                    aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                                }
                                System.out.println("about to send raiseentry for transfer " + assetCode+"   assetRaiseEntry: "+assetRaiseEntry);
                                if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N") && !userName.equalsIgnoreCase("")){
                                    //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                                	newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);
                                    String q3 = "update am_UncapitalizedTransfer set approval_Status='ACTIVE', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Long.toString(tranId) + "'";
                                    arb.updateAssetStatusChange(q3);
                                    
                                    String revalue_query = "update am_UncapitalizedTransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                    //String revalue_query = "update am_UncapitalizedTransfer set Approval_Status='ACTIVE',old_asset_id ='" + asset_id + "', asset_id ='" + newAsset_id + "' where asset_id = '" + asset_id + "'";
                                    System.out.println("======revalue_query=====> "+revalue_query);
                                    arb.updateAssetStatusChange(revalue_query);
                                    
                                       q = "update am_asset_approval set process_status='A',asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                                       arb.updateAssetStatusChange(q);
                                       q3 = "update am_UncapitalizedTransfer set approval_Status='APPROVED', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Long.toString(tranId) + "'";
                                       arb.updateAssetStatusChange(q3);
                                       revalue_query = "update am_UncapitalizedTransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                       arb.updateAssetStatusChange(revalue_query);
//                                	   String updateqry = "update AM_ASSET_UNCAPITALIZED set old_asset_id ='" + old_asset_id + "', asset_id ='" + newAsset_id + "', DEPT_ID = '"+newDept+"', BRANCH_ID = '"+newBranch+"', section_id = '"+newSec+"', asset_user = '"+newAssetUser+"', SBU_CODE = '"+newSbuCode+"',BRANCH_CODE = '"+newbranchcode+"', DEPT_CODE = '"+newdeptcode+"', SECTION_CODE = '"+newscetioncode+"'    where Old_asset_id ='" + old_asset_id + "'";                                       
                                	   String updateqry = "UPDATE a SET a.Asset_id  = b.new_asset_id,a.OLD_ASSET_ID = b.Asset_id, a.Branch_ID = b.New_branch_id,a.Dept_ID = b.New_dept_id,"
                                	   		+ "a.Section_id = b.New_Section,a.BRANCH_CODE = b.NEW_BRANCH_CODE,a.DEPT_CODE = b.NEW_DEPT_CODE,a.SECTION_CODE = b.NEW_SECTION_CODE "
                                	   		+ "from  AM_ASSET_UNCAPITALIZED a, am_UncapitalizedTransfer b where a.Asset_id = b.Asset_id and a.Asset_id = '"+ asset_id + "'";
//                                	   System.out.println("@@@ the updateqry is  " + updateqry);
                                    arb.updateAssetStatusChange(updateqry); 
                                    arb.updateAssetStatusChange("update am_raisentry_post set GroupIdStatus = 'Y',entryPostFlag = 'Y' where asset_code="+assetCode+" AND page = 'ASSET TRANSFER RAISE ENTRY'"); 
                                    arb.updateAssetStatusChange("update am_UncapitalizedTransfer set REVERSED = 'Y' where TRANSFER_ID = "+tranId+" ");  
                                    String page1 = "ASSET TRANSFER RAISE ENTRY";
                                    String iso = "000";
                                    String costdebitAcctName = ""; 
                                    String costcreditAcctName = "";
                                    String raiseEntryNarration = description+" "+asset_id;
                                    String costDrAcct = ""; String costCrAcct = "";
                                    String finacleTransId = "1";
                                    String recType = "";
                                    String transId = "20";

                                      aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                                  }                                
                                aprecords.updateRaiseEntry(newAsset_id);
                                
                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered Uncapitalised asset " + ex);
                            }
                            
                        	String subjectr ="Uncapitalised Asset Transfer Approval";
                        	String msgText11 ="Your Uncapitalised Asset Transfer with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId2 =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId2+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                            
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

if (full_status.equalsIgnoreCase("WIP"))
{
    System.out.println("===============here in WIP method of approval servlet=============");
    approvalRemark.setApprovalLevel(approvalCount);
    approvalRemark.setRemark("");
    approvalRemark.setStatus("Approved");
    approvalRemark.setTranType("WIP Reclassification");

    if (approvalCount == tranLevel)
    {
      String oldId=  aprecords.getCodeName("SELECT old_asset_id from am_asset where old_asset_id = '" + asset_id+"'");
        String newAsset_id = "";
        String page = "WIP RECLASSIFICATION";
        url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

        String q = "update am_asset_approval set process_status='A',approval_level_count='"+approvalCount+"'" +
                ",asset_status='Transferred' where transaction_id = " +
                "'" + tranId + "'";

        String q3 = "update AM_WIP_RECLASSIFICATION set approval_Status='ACTIVE' where transfer_id = " +
                "'" + Long.toString(tranId) + "'";

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
            //String cat_id = aid_setup.getCategoryID(category);
           // System.out.println("@@@ the category id is" + newCategoryID);
            //System.out.println("@@@ the newBranch id is" + newBranch);
           // System.out.println("@@@ the newDept id is" + newDept);
           // System.out.println("@@@ the newSec id is" + newSec);

            newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, newCategoryID);
             String change_id_query2="";
            if (newAssetID.equalsIgnoreCase(asset_id))
            {
                asset_manager.updateWIPReclassification(asset_id);
                //System.out.println("<<<<<<<<<< Both Values Are the same >>>>>>>>>>>>>>>>>>> ");
                change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
                                      " asset_id ='" + newAsset_id + "' where asset_id ='" + asset_id + "'";
            }
            else
            {

                 System.out.println("<<<<<<<<<< Both Values Are not the same >>>>>>>>>>>>>>>>>>> ");
                 asset_manager.updateWIPReclassification(asset_id,newAssetID);
                change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
                                      " asset_id ='" + newAsset_id + "' where asset_id ='" + newAssetID + "'";
                //System.out.println("change_id_query2 >>>>>>> " +change_id_query2 );
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

             System.out.println(">>>>>>>>>> The oldId is >> "+oldId );
               System.out.println(">>>>>>>>>> The asset_id is >> "+asset_id );

            if(oldId != null && !oldId.equalsIgnoreCase("") && oldId.equalsIgnoreCase(asset_id)) {
             String repostQuery = " update am_asset set asset_id ='" + asset_id + "', " +
                                      " asset_status='Active',post_reject_reason='' where old_asset_id ='" + asset_id + "'";
            arb.updateAssetStatusChange(repostQuery);
            change_id_query2 = " update am_asset set old_asset_id ='" + asset_id + "', " +
                                      " asset_id ='" + newAsset_id + "' where old_asset_id ='" + asset_id + "'";

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

            System.out.println("The new asset ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

        }
        catch (Throwable ex)
        {
            System.out.println("ApprovalServlet: Error occured while generating And updating new asset id for transfered WIP asset " + ex);
        }
        if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
     System.out.println("The "+asset_id+"  <<<<<<<<--------1--------->>>>>>>" + assetRaiseEntry);
      //stop update on am_asset continued
       /* aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName,
                subject_to_vat, wh_tax, url, tranId,assetCode);*/
       boolean a= 	 aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName,
                     subject_to_vat, wh_tax, url, tranId,assetCode);
       //update new asset on AM_WIP_RECLASSIFICATION
       String RECLASSIFICATION = " update AM_WIP_RECLASSIFICATION set new_asset_id ='" + newAsset_id + "'  where asset_Code ='" + assetCode + "'";
       arb.updateAssetStatusChange(RECLASSIFICATION);
       System.out.println("The "+asset_id+"  <<<<<<<<--------2--------->>>>>>>" + a);
        }
     //stop update on am_asset continued
      //  aprecords.updateRaiseEntry(newAsset_id);
        System.out.println("The  <<<<<<<<-------3---------->>>>>>>" + assetRaiseEntry);
        aprecords.updateRaiseEntry(asset_id);  
    }
    else
    {
        arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
        approverManager.createApprovalRemark(approvalRemark);
    }
    //arb.incrementApprovalCount(tranId,approvalCount,supervisor);
}//inner if

                    if (full_status.equalsIgnoreCase("Maintained")) {
                        System.out.println("===============here in Maintained section of approvalServlet =============");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Improvement");

                        if (approvalCount == tranLevel) {

                            String page = "ASSET IMPROVEMENT RAISE ENTRY";
                            url = "DocumentHelp.jsp?np=assetRevalueMaintenanceRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageMaitenance&pageDirect=Y";


                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Maintained' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);

                            String q3 = "update am_asset_improvement set approval_Status='ACTIVE' where revalue_id = '" + Long.toString(tranId) + "'";

                            String q4 = "update am_asset set asset_status='Active',post_reject_reason='' where asset_id ='" + asset_id + "'";

                            arb.updateAssetStatusChange(q3);
                            arb.updateAssetStatusChange(q4);

                            approverManager.createApprovalRemark(approvalRemark);

                            //asset_manager.updateAssetMaintenance(asset_id, tranId);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            aprecords.updateRaiseEntry(asset_id);
                            
                        	String subjectr ="Asset Improvement Approval";
                        	String msgText11 ="Your Asset Improvement with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                           
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Depreciation Adjusted")) {


                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Depreciation Adjustment");

                        if (approvalCount == tranLevel) {

                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Depreciation Adjusted' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            String q3 = "update AM_ASSET_DEP_ADJUSTMENT set approval_Status='ACTIVE' where id = '" + Long.toString(tranId) + "'";
                            arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            asset_manager.updateAssetDepreciation(asset_id);


                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Uncapitalise")) {
                        System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Bulk Uncapitalised Update");

                        if (approvalCount == tranLevel) {
                            System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='Bulk Uncapitalised Update' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkUpdate");
                            System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            BulkUpdateUncapitalisedManager bum = new BulkUpdateUncapitalisedManager();
                            bum.updateBulkAsset(newAssetList);

                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            // asset_manager.updateAssetDepreciation(asset_id);
                            
                        	String subjectr ="Bulk Uncapitalise Approval";
                        	String msgText11 ="Your Bulk Uncapitalise with GROUP ID: "+ asset_id +" has been approved.";
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

                        if (approvalCount == tranLevel) {

                            ar.deleteOtherSupervisors(String.valueOf(tranId),userid);
                            System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                            String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                            System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                            if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                            if (singleApproval.equalsIgnoreCase("N")) {
                            ar.setPendingMultiApprTransArchive(ar.setApprovalDataUploadGroup(tranId,tableName),"23",Integer.parseInt(batchId),assetCode,userid);  
                            }
                            
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
                            alertmessage = "Transaction Approved";
                            // asset_manager.updateAssetDepreciation(asset_id);
                            
                        	String subjectr ="Bulk Uncapitalized Asset Update Approval";
                        	String msgText11 ="Your Bulk Uncapitalized Asset Update with GROUP ID: "+ asset_id +" has been approved.";
                        	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                            comp.insertMailRecords(mailAddress,subjectr,msgText11);
                           
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                }//outer if
                else
                {
                    if (full_status.equalsIgnoreCase("Depreciation Adjusted") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='No Asset  Depreciation Adjusted', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Depreciation Adjustment");
                        approverManager.createApprovalRemark(approvalRemark);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Bulk Asset") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='None of Bulk Asset Updated ', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Update");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Bulk Uncapitalized Asset Rejected";
                    	String msgText11 ="Your Bulk Uncapitalized Asset with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//inner if


                    if (full_status.equalsIgnoreCase("Bulk Uncapitalized Asset Update") && astatus.equalsIgnoreCase("R")) {
                    	ar.deleteOtherSupervisors(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='None of BulkUncapitalized Asset Updated ', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Uncapitalized Asset Update");
                        approverManager.createApprovalRemark(approvalRemark);
                        alertmessage = "Transaction Rejected";
                        
                    	String subjectr ="Bulk Uncapitalized Asset Update Rejected";
                    	String msgText11 ="Your Bulk Uncapitalized Asset Update with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//inner if



                    if (full_status.equalsIgnoreCase("Maintained") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Maintenance rejected', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
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


                    if (full_status.equalsIgnoreCase("Transferred") && astatus.equalsIgnoreCase("R"))
                    {
                    	ar.deleteOtherSupervisorswithBatchId(String.valueOf(tranId),userid);
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Asset Transafer Rejected";
                    	String msgText11 ="Your Asset Transfer with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//inner if

                    if (full_status.equalsIgnoreCase("WIP") && astatus.equalsIgnoreCase("R"))
                    {

                        String q = "update am_asset_approval set process_status='R', " +
                                " " +
                                "asset_status='WIP Reclassification Rejected', reject_reason='" + rr + "' " +
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
                        
                    	String subjectr ="WIP Rejected";
                    	String msgText11 ="Your WIP with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//inner if

                    if (full_status.equalsIgnoreCase("Revalued") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset revaluation rejected', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Revaluation");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Revalued Rejected";
                    	String msgText11 ="Your Revalued with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//


                    if (full_status.equalsIgnoreCase("Reclassified") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset reclassification rejected', reject_reason='" + rr + "' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Reclassification");
                        approverManager.createApprovalRemark(approvalRemark);
                        
                    	String subjectr ="Reclassified Rejected";
                    	String msgText11 ="Your Reclassified with GROUP ID: "+ asset_id +" has been Rejected.";
                    	String InitiatorId =arb.getCodeName("select user_id from am_asset_approval WHERE batch_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                    	String mailAddress = arb.getCodeName("select email from am_gb_User WHERE User_id = '"+InitiatorId+"'");
                        comp.insertMailRecords(mailAddress,subjectr,msgText11);
                       
                    }//


                    if (full_status.equalsIgnoreCase("Uncapitalized Asset Disposal") && astatus.equalsIgnoreCase("R")) {
                    	 System.out.println("<<<<About to Reject >>>>>> " );
                        ar.deleteOtherSupervisorswithBatchId(asset_id,userid);
                        System.out.println("Value of singleApproval is >>>>>> " + singleApproval);
                        String transAvailable=arb.getCodeName("select count(*) from am_asset_approval WHERE asset_Id = '"+asset_id+"' and super_Id = '"+userid+"'  and asset_status = 'PENDING' ");
                        System.out.println("Value of transAvailable is >>>>>> " + transAvailable);
                        if (transAvailable == "0") {alertmessage = "Someone already attending to the Transaction";}
                        if (transAvailable != "0") {
                        if (singleApproval.equalsIgnoreCase("N")) {
                        ar.setPendingMultiApprTransArchive(ar.setApprovalData(asset_id),"1",Long.parseLong(batchId),assetCode,userid); 
                        }
                        
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set asset_status='Asset disposal rejected'," +
                                "process_status='R', reject_reason='" + rr + "' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);

                        String q1 = "update am_asset set Asset_Status='ACTIVE' where asset_id = '" + asset_id + "'";
                        arb.updateAssetStatusChange(q1);

                        String q2 = "update am_AssetDisposal set disposal_status='R' where asset_id = '" + asset_id + "'";
                        arb.updateAssetStatusChange(q2);

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
                       
                    }

//================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                    }//

                }
            }//else
            //arb.updateAssetStatusChange(supervisorUpdate);
            
            out.println("<script>alert(" + alertmessage + ")</script>");
            out.println("<script>");
            out.println("window.location='DocumentHelp.jsp?np=transactionForApprovalList'");
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
