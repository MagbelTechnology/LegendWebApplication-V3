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

import magma.StockRecordsBean;
import magma.asset.manager.StockManager;
import magma.AssetReclassificationBean;
import magma.net.dao.MagmaDBConnection;

import com.magbel.ia.bus.InventoryAdjustmtServiceBus;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.legend.bus.ApprovalManager;
import com.magbel.ia.bus.InventoryServiceBus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import magma.net.manager.BulkStockUpdateManager;
import magma.StockRecordsBean;
import  magma.asset.manager.WIPAssetManager;

//import magma.net.manager.AssetManager;
/**
 *
 * @author Olabo
 */
public class StockApprovalServlet extends HttpServlet {
	private InventoryAdjustmtServiceBus serviceBus;

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
        serviceBus = new InventoryAdjustmtServiceBus();
        StockManager asset_manager = new StockManager();
        ApprovalRecords aprecords = new ApprovalRecords();
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        MagmaDBConnection dbConnection = new MagmaDBConnection();
        String disposaltype = "";
        String userClass = (String)request.getSession().getAttribute("UserClass");
        
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");

int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
            //--System.out.println("\nthe assetCode is >>>>>>>>>>>>>>>>> " + assetCode);
            ApprovalManager approverManager = new ApprovalManager();
            AssetReclassificationBean asset_reclassification = new AssetReclassificationBean();
            StockRecordsBean arb = new StockRecordsBean();

            String astatus = request.getParameter("astatus");
            System.out.println("astatus for  Stocks>>> "+astatus);
            String newCategoryID = request.getParameter("newCategoryID");
            String full_status = request.getParameter("full_status");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>full_status " +full_status);
            String asset_id = request.getParameter("id") == null ? "" : request.getParameter("id");

            String rr = request.getParameter("reject_reason");
            String tableName = request.getParameter("tableName") == null ? "" : request.getParameter("tableName");
            String columnName = request.getParameter("columnName");
            String category = request.getParameter("category");

            //for asset transfer
            String newBranch = request.getParameter("newBranch");
            String newDept = request.getParameter("newDept");
            String newSec = request.getParameter("newSection");
            String compCode = request.getParameter("compCode");
            String userid = request.getParameter("userid");
            String oldbranchId = request.getParameter("branch");
            System.out.println("oldbranchId is >>>>>>>>>>>>>>>>> " + oldbranchId+"  userid: "+userid+"  compCode: "+compCode+"  newSection: "+newSec+"   newDept: "+newDept+"  newBranch: "+newBranch);
            //for asset reclassification
            String branch = request.getParameter("branch_id");
            String dept = request.getParameter("department_id");
            String section = request.getParameter("section_id");
            String newCategory = request.getParameter("new_category");
            String systemIp = request.getRemoteAddr();
            System.out.println("branch is >>>>>>>>>>>>>>>>> " + branch+"  dept: "+dept+"  section: "+section+"  newCategory: "+newCategory+"   newDept: "+newDept+"  systemIp: "+systemIp);
            String newAssetID = request.getParameter("newAssetID");
            System.out.println("newAssetID is >>>>>>>>>>>>>>>>> " + newAssetID);
            int tranId = Integer.parseInt(request.getParameter("tranId"));
            System.out.println("tranId is >>>>>>>>>>>>>>>>> " + tranId);
            int tranLevel = Integer.parseInt(request.getParameter("tranLevel"));
            String transact_level = request.getParameter("transaction_level");
            int transaction_level = 0;
            if(transact_level!=null){transaction_level = Integer.parseInt(transact_level);}
            System.out.println("tranLevel is >>>>>>>>>>>>>>>>> " + tranLevel+"    transaction_level: "+transaction_level);
            int approvalCount = Integer.parseInt(request.getParameter("approvalCount"));
            System.out.println("tranId is >>>>>>>>>>>>>>>>> " + tranId+"  tranLevel: "+tranLevel+"  approvalCount: "+approvalCount+"  newCategory: "+newCategory+"   newDept: "+newDept+"  systemIp: "+systemIp);
            int userId = Integer.parseInt(request.getParameter("userid"));
            System.out.println("userId is >>>>>>>>>>>>>>>>> " + userId);
            String user_Id = request.getParameter("user_id");
            String partialtype = request.getParameter("issuable");

            int supervisor = (request.getParameter("supervisor") == null) ? 0 : Integer.parseInt(request.getParameter("supervisor"));
            String supervisorName =  aprecords.getCodeName(" select Full_name from am_gb_user where user_ID ="+userId+"");

             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>Approval full_status " +full_status+"    supervisor:"+supervisor);
            // System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>astatus " +astatus);
            if (supervisor == 0) {
                supervisor = userId;
            }
            int currentSupervisor = approvalCount;
            currentSupervisor += 1;
            String sup = "approval" + currentSupervisor;
 //           String supervisorUpdate = "update am_asset_approval set " + sup + " = " + supervisor + " where transaction_id = " + tranId;

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
            ApprovalRemark approvalRemark = new ApprovalRemark(asset_id, userId, systemIp, tranId, "");
            if (tableName.equals(""))
            {
                if (astatus.equalsIgnoreCase("A"))
                {
       			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND PROCESS_STATUS != 'A'";
    			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
    			 String apprLevelQry = "select Level from Approval_Level_Setup where code='35'"; //Bulk Stock Transfer Requisition
    			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));		
    			 
//Approval for new asset creation
                	if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
                		arb.updateAssetStatusApproval(tranId,userid);
                	}
                	else{
                    arb.updateAssetStatusApproval(tranId,userid);
                    arb.updateAssetStatus(asset_id);
                	}

                }//inner if
            }
            else
            {
                if (astatus.equalsIgnoreCase("A"))
                {
                    // System.out.println(" here>>>>>>>>>>>>>>>>>>>>> 1.1");
                    approvalCount += 1;

                    if (full_status.equalsIgnoreCase("Disposed"))
                    {
                        // approvalCount += 1;

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Disposal");

                        if (approvalCount == tranLevel)
                        {
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL

                            String page = "ASSET DISPOSAL RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=disposeAssetRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageDisposals&pageDirect=Y";
                   //         System.out.println("u");
                             System.out.println("Disposal in equality ==========================================="+asset_id+"   partialtype: "+partialtype+"  disposaltype: "+disposaltype+"   tranId: "+tranId+"   approveddate: "+approveddate);
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
                            
                            String q4 = "update am_assetDisposal set disposal_status='PD' where asset_id = '" + asset_id + "'";
                            arb.updateAssetStatusChange(q4);
         //                   System.out.println("the for the query is >>>>>>>>>>>>>>>> " + q4);

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
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


                    if (full_status.equalsIgnoreCase("accelerated depreciation"))
                    {
                        // approvalCount += 1;
//                    	System.out.println("Matanmi: "+full_status);
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Accelerated Depreciation");

                        if (approvalCount == tranLevel)
                        {
                            //NOTE: UNCOMMENT ALL THE LINES WITH //-- BELOW TO REVERT TO U-CASE WHERE ASSET STATUS CHANGES TO DISPOSED AFTER APPROVAL

                            String page = "ACCELERATED DEPRECIATION RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=acceleratedDeprRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageAcceleratedDepr&pageDirect=Y";
                   //         System.out.println("u");
//                            System.out.println("in equality ===========================================");
                            
                            String q = "update am_asset_approval set process_status='A', asset_status='ACCELERATED',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);
                   				
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
//                            System.out.println("CHECK 1"+asset_id);
                            aprecords.updateRaiseEntry(asset_id);
//                            System.out.println("CHECK "+approvalRemark);
                            approverManager.createApprovalRemark(approvalRemark);
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
//                            System.out.print("######### the Transaction Id is " + tranId+"    newAsset_id: "+newAsset_id);
                    String change_id_query3 = "update am_assetReclassification set  new_asset_id='"+newAsset_id+"' where Reclassify_ID ="+tranId;
                    arb.updateAssetStatusChange(change_id_query3);
//                    		System.out.print("######### the change_id_query3 is " + change_id_query3);
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

                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if


                    if (full_status.equalsIgnoreCase("Transferred"))
                    {
           			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND PROCESS_STATUS != 'A'";
        			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
        			 String apprLevelQry = "select Level from Approval_Level_Setup where code='6'";
        			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));		
        			 
                        System.out.println("===============here in transfered method of approval servlet=============");
                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Stock Transfer");
                        if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
                            String q = "update am_asset_approval set process_status='A',asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "' and super_id = '"+userid+"'";
                            arb.updateAssetStatusChange(q);
                        }
                        else{
                        if (approvalCount == tranLevel) {
                        	//System.out.println("===============here in transfered Date of approval servlet============="+approveddate);
                            //String oldAssetStatus = arb.getCodeName("select asset_status from am_asset_approval where transaction_id="+Integer.toString(tranId));
                            String newAsset_id = "";
                            String page = "STOCK TRANSFER RAISE ENTRY";
                            String url = ""; //DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + asset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";

                            String q = "update am_asset_approval set process_status='A',asset_status='Transferred',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                             arb.updateAssetStatusChange(q);

                        //    System.out.println("the asset id is ?????????????????????? "+asset_id);
                        //    System.out.println("the transaction id is ?????????????????????? "+tranId);
                            //asset_manager.updateAssetTransfer(asset_id,tranId);

                            approverManager.createApprovalRemark(approvalRemark);
                            //comment off for trouble shooting starts

                            magma.net.manager.AssetManager assetManager = new magma.net.manager.AssetManager();
                            try {
                                String change_id_query2="";
                                AutoIDSetup aid_setup = new AutoIDSetup();
                                String cat_id = aid_setup.getCategoryID(category);

                                newAsset_id = aid_setup.getIdentity(newBranch, newDept, newSec, cat_id);

                                String q3 = "update am_assettransfer set approval_Status='ACTIVE', new_asset_id ='"+newAsset_id+"' where transfer_id = '" + Integer.toString(tranId) + "'";
                                arb.updateAssetStatusChange(q3);

                                String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where transfer_id = '" + tranId + "'";
                                arb.updateAssetStatusChange(revalue_query);
                                
                                if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("N")){
                                	String newbranchCode = arb.getCodeName("select BRANCH_CODE from AM_AD_BRANCH where BRANCH_ID="+newBranch+"");
                                	String newdeptCode = arb.getCodeName("select DEPT_CODE from AM_AD_DEPARTMENT where DEPT_ID="+newDept+"");
                                	String newsectCode = arb.getCodeName("select SECTION_CODE from AM_AD_SECTION where SECTION_ID="+newSec+"");
                                	String concatCode = arb.getCodeName("select WAREHOUSE_CODE+'#'+ITEM_CODE+'#'+ItemType from ST_STOCK where asset_id ='"+asset_id+"'");
                                	String[] cncatarray = concatCode.split("#");
                                	String oldwarehouseCode = cncatarray[0];
                                	String itemCode = cncatarray[1];
                                	String ItemType = cncatarray[1];
                                	String branchCode = arb.getCodeName("select BRANCH_CODE from AM_AD_BRANCH where BRANCH_ID ="+newBranch+"");
                                	String newwarehouseCode = arb.getCodeName("select WAREHOUSE_CODE from ST_WAREHOUSE where BRANCH_CODE ='"+branchCode+"'");
                                	oldbranchId = arb.getCodeName("select BRANCH_ID from ST_STOCK where asset_id ='"+asset_id+"'");
                                	//String ItemType = arb.getCodeName("select ItemType from ST_STOCK where asset_id ='"+asset_id+"'");
                                	String quantity = arb.getCodeName("select quantity from ST_STOCK where asset_id ='"+asset_id+"'");

                                  	 change_id_query2 = "update ST_STOCK set old_asset_id ='"+asset_id+"', asset_id ='"+newAsset_id+"', branch_id = "+newBranch+", dept_id = "+newDept+", section_id = "+newSec+", "
                                  	 + "BRANCH_CODE ='"+newbranchCode+"',DEPT_CODE = '"+newdeptCode+"',SECTION_CODE = '"+newsectCode+"' where asset_id ='"+asset_id+"'";
                                  	arb.updateAssetStatusChange(change_id_query2);
              //                    	System.out.println("<<<<<<<<<<<=====quantity: "+quantity+"  warehouseCode: "+newwarehouseCode+"   itemCode: "+itemCode+"   ItemType: "+ItemType+"    oldbranchId: "+oldbranchId);
                                  	String inventTotal =aprecords.getCodeName("SELECT COUNT(*) FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+newwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
                                  	String frompreviousBal =aprecords.getCodeName("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+oldwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
                                  	String topreviousBal =aprecords.getCodeName("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE COMP_CODE = '"+compCode+"' AND WAREHOUSE_CODE = '"+newwarehouseCode+"' AND ITEM_CODE = '"+itemCode+"'");
                 //                 	System.out.println("<<<<<<<<<<<=====inventTotal: "+inventTotal+"  frompreviousBal: "+frompreviousBal+"   topreviousBal: "+topreviousBal);
                                  	if(inventTotal.equalsIgnoreCase("0")){approverManager.createInventoryTotal(compCode, itemCode, newwarehouseCode,  Integer.parseInt(quantity), Integer.parseInt(userid),Integer.parseInt(newBranch));
                                  	serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",Integer.parseInt(quantity),newwarehouseCode,userId,topreviousBal,compCode);
                                  	}
                            		else{approverManager.updateInventoryTotal(compCode, itemCode, newwarehouseCode,  Integer.parseInt(quantity),Integer.parseInt(newBranch));
                            			serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",Integer.parseInt(quantity),newwarehouseCode,userId,topreviousBal,compCode);
                            			}	
                                  	approverManager.reductionOfInventoryTotal(compCode, itemCode, oldwarehouseCode,  Integer.parseInt(quantity),Integer.parseInt(oldbranchId));
                                  	serviceBus.postStockInventoryHistory(itemCode,"TRANSFER",(Integer.parseInt(quantity)*-1),oldwarehouseCode,userId,frompreviousBal,compCode);
                                  	
                                }
                                //comment the two lines below to revert to asset transfer scenario where original and transfered assets
                                // will be available in am_asset table.

                                url = "DocumentHelp.jsp?np=assetTransfersRaiseEntry&id=" + newAsset_id + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                                String msgText1 = "Your transaction for stock transfer with ID " + asset_id + " has been approved. The new asset ID is " + newAsset_id;
                                mail.sendMailTransactionInitiator(tranId, "Asset Transfer", msgText1);

                                System.out.println("The new stock ID after being transferred is <<<<<<<<<>>>>>>>>>>>>>>>>" + newAsset_id);

                                // //comment off for trouble shooting ends

                            } catch (Throwable ex) {
                                System.out.println("ApprovalServlet: Error occured while generating And updating new stock id for transfered asset " + ex);
                            }

                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                              //--  System.out.println("about to send raiseentry for trasfer " + assetCode);
                                aprecords.insertApprovalx(newAsset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            else{
                            	String change_id_query2 = "update ST_STOCK set old_asset_id ='"+asset_id+"', asset_id ='"+newAsset_id+"' where asset_id ='"+asset_id+"'";
                            	arb.updateAssetStatusChange(change_id_query2);
                              }                            
                            aprecords.updateRaiseEntry(newAsset_id);

                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
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

    if (approvalCount == tranLevel)
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

      //       System.out.println(">>>>>>>>>> The oldId is >> "+oldId );
    //           System.out.println(">>>>>>>>>> The asset_id is >> "+asset_id );

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
}//inner if

                    if (full_status.equalsIgnoreCase("Maintained")) {
             //           System.out.println("===============here in Maintained section of approvalServlet =============");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Asset Improvement");

                        if (approvalCount == tranLevel) {

                            String page = "ASSET IMPROVEMENT RAISE ENTRY";
                            String url = "DocumentHelp.jsp?np=assetRevalueMaintenanceRaiseEntry&id=" + asset_id + "&operation=1&exitPage=manageMaitenance&pageDirect=Y";


                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Maintained',DATE_APPROVED = '"+approveddate+"' where transaction_id = '" + tranId + "'";
                            arb.updateAssetStatusChange(q);

                            String q3 = "update am_asset_improvement set approval_Status='ACTIVE' where revalue_id = '" + Integer.toString(tranId) + "'";

                            String q4 = "update am_asset set asset_status='Active',post_reject_reason='' where asset_id ='" + asset_id + "'";

                            arb.updateAssetStatusChange(q3);
                            arb.updateAssetStatusChange(q4);

                            approverManager.createApprovalRemark(approvalRemark);

                            //asset_manager.updateAssetMaintenance(asset_id, tranId);
                            if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
                            aprecords.insertApprovalx(asset_id, description, page, flag, partPay, asset_User_Name, branchName, subject_to_vat, wh_tax, url, tranId,assetCode);
                            }
                            aprecords.updateRaiseEntry(asset_id);
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

                        if (approvalCount == tranLevel) {

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
                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        // arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//Matanmi

                    

                    if (full_status.equalsIgnoreCase("Depreciation Adjusted")) {


                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Depreciation Adjustment");

                        if (approvalCount == tranLevel) {

                            String q = "update am_asset_approval set process_status='A', asset_status='Asset Depreciation Adjusted',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            String q3 = "update AM_ASSET_DEP_ADJUSTMENT set approval_Status='ACTIVE' where id = '" + Integer.toString(tranId) + "'";
                            arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            asset_manager.updateAssetDepreciation(asset_id);


                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("Bulk Asset")) {
                     //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");

                        approvalRemark.setApprovalLevel(approvalCount);
                        approvalRemark.setRemark("");
                        approvalRemark.setStatus("Approved");
                        approvalRemark.setTranType("Bulk Asset Update");

                        if (approvalCount == tranLevel) {
                     //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                            String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Updated',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                            arb.updateAssetStatusChange(q);

                            ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkUpdate");
                      //      System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                            BulkStockUpdateManager bum = new BulkStockUpdateManager();
                            bum.updateBulkAsset(newAssetList);

                            // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                            //    arb.updateAssetStatusChange(q3);

                            approverManager.createApprovalRemark(approvalRemark);

                            // asset_manager.updateAssetDepreciation(asset_id);


                        } else {
                            arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                            approverManager.createApprovalRemark(approvalRemark);
                        }
                        //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                    }//inner if

                    if (full_status.equalsIgnoreCase("BulkStockTransfer")) {
//                           System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
                           
                           approvalRemark.setApprovalLevel(approvalCount);
                           approvalRemark.setRemark("");
                           approvalRemark.setStatus("Approved");
                           approvalRemark.setTranType("Bulk Stock Transfer");
                           String reqnId =  aprecords.getCodeName(" SELECT asset_id from am_asset_approval  where transaction_id=" + tranId);
                           if(transaction_level==0){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+tranId+"' and super_id != '"+userid+"'");}
 //                          System.out.println("tranLevel here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.0: "+tranLevel+"   approvalCount: "+approvalCount);
              			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE ASSET_ID = '"+tranId+"' AND PROCESS_STATUS != 'A'";
            			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
            			 String apprLevelQry = "select Level from Approval_Level_Setup where code='54'";
            			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));		
            			 
                           if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
                               String q = "update am_asset_approval set process_status='W', asset_status='Bulk Stock Transfer',DATE_APPROVED = '"+approveddate+"' where ASSET_ID=" + tranId+" and super_id = '"+userid+"'";
                               arb.updateAssetStatusChange(q);
                           }
                           else{
                           if (tranLevel==approvalCount) {
//                               System.out.println("tranLevel here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3: "+approvalCount);
                      //  	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                               String q = "update am_asset_approval set process_status='W', asset_status='Bulk Stock Transfer',DATE_APPROVED = '"+approveddate+"' where ASSET_ID='"+tranId+"' and super_id = '"+userid+"'";
                               arb.updateAssetStatusChange(q);
//                               System.out.println("Update Script: "+q);
                               ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkStockTransfer");
//                               System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                               StockRecordsBean strec = new StockRecordsBean();
                               strec.bulkStockIssuance(newAssetList,String.valueOf(tranId),compCode,userid,"","");
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>reqnId>>>>>>>>>>>>>>> 1.4: "+reqnId);

                               int tranlent = String.valueOf(tranId).length();
//                             System.out.println("the for the tranlent is >>>>>>>>>>>>>>>> " + tranlent);                               
                       		String revalue_query = "update ST_STOCKTRANSFER set approval_Status='ACTIVE' where substring(CAST(transfer_id AS varchar(38)),1,"+tranlent+") = '"+tranId+"'";
                       		arb.updateAssetStatusChange(revalue_query);
//                       	 System.out.println("the for the revalue_query is >>>>>>>>>>>>>>>> " + revalue_query); 
                               // String q3 = "update AM_GB_BULKUPDATE set approval_Status='ACTIVE' where id = '"+ Integer.toString(tranId)+"'";
                               //    arb.updateAssetStatusChange(q3);

                               approverManager.createApprovalRemark(approvalRemark);

                               // asset_manager.updateAssetDepreciation(asset_id);


                           } else {
                               arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                               approverManager.createApprovalRemark(approvalRemark);
                           }
                    		}
                           //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                       }//inner if
                    if (full_status.equalsIgnoreCase("BulkStockTransferRequisition")) {
    //                     System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.22");
                    	String approverUser = approverManager.SupervisorMailAddressList(String.valueOf(tranId));
                    	String []SupervisorId = approverUser.split("#");
                    	int No = SupervisorId.length;
                    	
            			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND PROCESS_STATUS != 'A'";
            			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
            			 String apprLevelQry = "select Level from Approval_Level_Setup where code='53'";
            			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));		
     //       			 System.out.println("TransactionLevelCount for BulkStockTransferRequisition >>>>>>: "+TransactionLevelCount+"   numOfTransactionLevel: "+numOfTransactionLevel+"    userid: "+userid+"    tranId: "+tranId);
                         approvalRemark.setApprovalLevel(approvalCount);
                         approvalRemark.setRemark("");
                         approvalRemark.setStatus("Approved");
                         approvalRemark.setTranType("Bulk Transfer Requisition");
                         String approvedby = aprecords.getCodeName("select full_name from am_gb_User where User_ID="+userid+" ");
                         String reqnId =  aprecords.getCodeName(" SELECT asset_id from am_asset_approval  where transaction_id=" + tranId);
//                         if(tranLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnId+"' and super_id != '"+userid+"'");}
                         if(numOfTransactionLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnId+"' and super_id != '"+userid+"'");}
                         if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
                             String q = "update am_asset_approval set process_status='A', asset_status='Stock Transfer Requisition',DATE_APPROVED = '"+approveddate+"' where transaction_id="+tranId+"  and super_id = '"+userid+"' ";
                             arb.updateAssetStatusChange(q);
                         }else{
          //              	 System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                         if (TransactionLevelCount==1) {
                     //        System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                    //  	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                             String q = "update am_asset_approval set process_status='A', asset_status='Stock Transfer Requisition',DATE_APPROVED = '"+approveddate+"' where transaction_id="+tranId+"  and super_id = '"+userid+"' ";
                             arb.updateAssetStatusChange(q);

                             ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkStockTransfer");
                             
                             String p = "update am_ad_TransferRequisition set process_status='A' where Batch_id=" + tranId;
                             arb.updateAssetStatusChange(p);

                             approverManager.createApprovalRemark(approvalRemark);
                             String createdUserId = aprecords.getCodeName("select User_ID from am_asset_approval where Batch_id='"+tranId+"'");
                             String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                             String subject = "Stock Transactions Approval";
                             String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Approved by "+approvedby+".";
                    		System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1+"  createdby: "+createdby);
                    		mail.sendMail(createdby,subject,msgText1);
                			String othermail = aprecords.getCodeName("select MAIL_ADDRESS from am_mail_statement where MAIL_CODE = 'APP'");
                			System.out.println("#%%%%%%%%% msgText1: "+msgText1+"   othermail: "+othermail);
                			mail.sendMail(othermail,subject,msgText1);
                    		
                         } else {
                             arb.incrementApprovalCount3(tranId, approvalCount, supervisor);
                             String p = "update am_ad_TransferRequisition set process_status='A' where Batch_id=" + tranId;
                             arb.updateAssetStatusChange(p);
                             approverManager.createApprovalRemark(approvalRemark);
                         }
                         }
                         //arb.incrementApprovalCount(tranId,approvalCount,supervisor);
             			for(int j=0;j<No;j++){
                            String approvalMail = aprecords.getCodeName("select email from am_gb_User where user_id="+SupervisorId[j]+"");
                            String subject = "Stock Transactions Approval";
                            String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Approved by "+approvedby+".";
                            System.out.println("#&&&&&&&&&&& msgText1: "+msgText1+"  approvalMail: "+approvalMail);
                   		mail.sendMail(approvalMail,subject,msgText1);
            			}
                     }//inner if
                    
                    if (full_status.equalsIgnoreCase("BulkStockTransferApproval")) {
    //                     System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.22");
                    	String approverUser = approverManager.SupervisorMailAddressList(String.valueOf(tranId));
                    	String []SupervisorId = approverUser.split("#");
                    	int No = SupervisorId.length;
                    	
            			 String apprLevelCountQry = "SELECT COUNT(*) FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND PROCESS_STATUS != 'A'";
            			 int TransactionLevelCount = Integer.parseInt(aprecords.getCodeName(apprLevelCountQry));
            			 String apprLevelQry = "select Level from Approval_Level_Setup where code='54'";
            			 int numOfTransactionLevel = Integer.parseInt(aprecords.getCodeName(apprLevelQry));		
     //       			 System.out.println("TransactionLevelCount for BulkStockTransferRequisition >>>>>>: "+TransactionLevelCount+"   numOfTransactionLevel: "+numOfTransactionLevel+"    userid: "+userid+"    tranId: "+tranId);
                         approvalRemark.setApprovalLevel(approvalCount);
                         approvalRemark.setRemark("");
                         approvalRemark.setStatus("Approved");
                         approvalRemark.setTranType("Bulk Stock Transfer");
                         String approvedby = aprecords.getCodeName("select full_name from am_gb_User where User_ID="+userid+" ");
                         String reqnId =  aprecords.getCodeName(" SELECT asset_id from am_asset_approval  where  where transaction_id="+tranId+" and tran_type = 'Bulk Stock Transfer'");
//                         if(tranLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnId+"' and super_id != '"+userid+"'");}
                         if(numOfTransactionLevel==1){aprecords.getCodeName("delete from am_asset_approval where asset_id = '"+reqnId+"' and super_id != '"+userid+"' and tran_type = 'Bulk Stock Transfer' ");}
                         if((TransactionLevelCount != 1) && numOfTransactionLevel != 1){
                             String q = "update am_asset_approval set process_status='WP', asset_status='Bulk Stock Transfer Approval',DATE_APPROVED = '"+approveddate+"' where transaction_id="+tranId+"  and super_id = '"+userid+"' and tran_type = 'Bulk Stock Transfer'";
                             arb.updateAssetStatusChange(q);
                         }else{
          //              	 System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                         if (TransactionLevelCount==1) {
                     //        System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                    //  	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                             String q = "update am_asset_approval set process_status='WP', asset_status='Bulk Stock Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id="+tranId+"  and super_id = '"+userid+"' and tran_type = 'Bulk Stock Transfer' ";
                             arb.updateAssetStatusChange(q);

                //             ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkStockTransfer");
                             
             //                String p = "update am_ad_TransferRequisition set process_status='A' where Batch_id=" + tranId;
             //                arb.updateAssetStatusChange(p);

                             approverManager.createApprovalRemark(approvalRemark);
                             String createdUserId = aprecords.getCodeName("select User_ID from am_asset_approval where Batch_id='"+tranId+"' and tran_type = 'Bulk Stock Transfer'");
                             String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                             String subject = "Stock Transactions Approval";
                             String msgText2 = aprecords.getCodeName("select mail_description from am_mail_statement where MAIL_CODE = 'APP'");
                             String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Approved by "+approvedby+".";
                    		System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1+"  createdby: "+createdby);
                    		mail.sendMail(createdby,subject,msgText1);
                			String othermail = aprecords.getCodeName("select MAIL_ADDRESS from am_mail_statement where MAIL_CODE = 'APP'");
                			System.out.println("#%%%%%%%%% msgText1: "+msgText1+"   othermail: "+othermail);
                			mail.sendMail(othermail,subject,msgText2);
                    		
                         } else {
                             arb.incrementApprovalCount3(tranId, approvalCount, supervisor);
             //                String p = "update am_ad_TransferRequisition set process_status='A' where Batch_id=" + tranId;
             //                arb.updateAssetStatusChange(p);
                             approverManager.createApprovalRemark(approvalRemark);
                         }
                         }
                         //arb.incrementApprovalCount(tranId,approvalCount,supervisor);
                     }//inner if
                    
                }//outer if
                else
                {
                	
                    if (full_status.equalsIgnoreCase("BulkStockTransferRequisition") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='No Asset  Depreciation Adjusted', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String p = "update am_ad_TransferRequisition set process_status='R' where Batch_id=" + tranId;
                        arb.updateAssetStatusChange(p);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Transfer Requisition");
                        approverManager.createApprovalRemark(approvalRemark);
                        String createdUserId = aprecords.getCodeName("select User_ID from am_asset_approval where Batch_id='"+tranId+"'");
                        String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                        String subject = "SCRN Transactions Approval";
                        String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Rejected by '"+supervisorName+"'. Reason: "+rr+"";
                        System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                        mail.sendMail(createdby,subject,msgText1);                        
                    }//inner if
                    
                    if (full_status.equalsIgnoreCase("BulkStockTransferApproval") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Bulk Stock Transaction Rejection', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String p = "update am_ad_TransferRequisition set process_status='R' where Batch_id=" + tranId;
                        arb.updateAssetStatusChange(p);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Stock Transfer Approval");
                        approverManager.createApprovalRemark(approvalRemark);
                        String createdUserId = aprecords.getCodeName("select User_ID from am_asset_approval where Batch_id='"+tranId+"'");
                        String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                        String subject = "Stock Transactions Approval";
                        String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Rejected by '"+supervisorName+"'. Reason: "+rr+"";
                        System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                        mail.sendMail(createdby,subject,msgText1);                        


                    }//inner if
                	
                    if (full_status.equalsIgnoreCase("Depreciation Adjusted") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='No Asset  Depreciation Adjusted', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Depreciation Adjustment");
                        approverManager.createApprovalRemark(approvalRemark);


                    }//inner if


                    if (full_status.equalsIgnoreCase("Bulk Asset") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='None of Bulk Asset Updated ', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                        String delete_query = "DELETE FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND USER_ID != '"+userId+"'";
                   		arb.updateAssetStatusChange(delete_query);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Update");
                        approverManager.createApprovalRemark(approvalRemark);
                        String createdUserId = aprecords.getCodeName("select User_ID from am_asset_approval where Batch_id='"+tranId+"'");
                        String createdby = aprecords.getCodeName("select email from am_gb_User where user_id="+createdUserId+"");
                        String subject = "SCRN ISSUANCE Transactions Approval";
                        String msgText1 = "Your Transaction with Group Id: "+ tranId +" has been Rejected by '"+supervisorName+"'. Reason: "+rr+"";
                        System.out.println("#$$$$$$$$$$$ msgText1: "+msgText1);
                        mail.sendMail(createdby,subject,msgText1);   

                    }//inner if




                    if (full_status.equalsIgnoreCase("Maintained") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
//                        String q = "update am_asset_approval set process_status='R', asset_status='Asset Maintenance rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='REJECTED', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        String qr = "update am_asset_improvement set approval_status='REJECTED' where Revalue_ID=" + tranId;
//                        System.out.println("Rejected Query: "+qr);
                        arb.updateAssetStatusChange(q);
                        arb.updateAssetStatusChange(qr);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Maintenance");
                        approverManager.createApprovalRemark(approvalRemark);


                    }//inner if

                    if (full_status.equalsIgnoreCase("Transferred") && astatus.equalsIgnoreCase("R"))
                    {
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        arb.updateAssetStatusChange(q);
                   		String delete_query = "DELETE FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND USER_ID != '"+userId+"'";
                   		arb.updateAssetStatusChange(delete_query);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Stock Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for stock transfer with ID " + asset_id + " has been approved. The new asset ID is " + asset_id;
                        mail.sendMailTransactionInitiator(tranId, "Stock Transfer", msgText1);

                    }//inner if
                    // Bulk Asset Transfer Rejection
                    
                    if (full_status.equalsIgnoreCase("BulkStockTransfer") && astatus.equalsIgnoreCase("R"))
                    {
                    	System.out.println("Bulk Transfer for  Reject of Stocks>>> ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where batch_id=" + tranId+" AND USER_ID = '"+userId+"' ";
                        System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_gb_bulkTransfer where Batch_id=" + tranId;
                        System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(bulkquery);
                   		String delete_query = "DELETE FROM am_asset_approval WHERE batch_id = '"+tranId+"' AND USER_ID != '"+userId+"'";
                   		arb.updateAssetStatusChange(delete_query);
                   		System.out.println("<<<<<<<delete_query: "+delete_query);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Stock Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for Bulk stock transfer with Batch ID " + tranId + " has been Rejected by '"+supervisorName+"'. Reason: "+rr+"  ";
                        mail.sendMailTransactionInitiator(tranId, "Bulk Stock Transfer", msgText1);
                    }//inner if                    

                    if (full_status.equalsIgnoreCase("BulkStockTransferRequisition") && astatus.equalsIgnoreCase("R"))
                    {
                    	System.out.println("Bulk Transfer Requesition for  Reject >>> ");
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                        System.out.println("Query for Requesition Reject >>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_ad_TransferRequisition where Batch_id=" + tranId;
                        System.out.println("Query for Requesition Reject >>> "+q);
                        arb.updateAssetStatusChange(bulkquery);
                   		String delete_query = "DELETE FROM am_asset_approval WHERE BATCH_ID = '"+tranId+"' AND USER_ID != '"+userId+"'";
                   		arb.updateAssetStatusChange(delete_query);
                   		
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Stock Transfer Requesition");
                        approverManager.createApprovalRemark(approvalRemark);
                     //   String msgText1 = "Your transaction for Bulk stock transfer Requesition with Batch ID " + tranId + " has been Rejected. The new Batch ID is " + tranId;
                        String msgText1 = "Your transaction for Bulk stock transfer with Batch ID " + tranId + " has been Rejected by '"+supervisorName+"'. Reason: "+rr+"  ";
                        mail.sendMailTransactionInitiator(tranId, "Bulk Stock Transfer", msgText1);
                    }//inner if                    
                    
                    if (full_status.equalsIgnoreCase("WIP") && astatus.equalsIgnoreCase("R"))
                    {

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

                    }//inner if

                    if (full_status.equalsIgnoreCase("Revalued") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset revaluation rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);

                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Revaluation");
                        approverManager.createApprovalRemark(approvalRemark);

                    }//


                    if (full_status.equalsIgnoreCase("Reclassified") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset reclassification rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;

                        arb.updateAssetStatusChange(q);
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Asset Reclassification");
                        approverManager.createApprovalRemark(approvalRemark);
                    }//


                    if (full_status.equalsIgnoreCase("Disposed") && astatus.equalsIgnoreCase("R")) {

                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set asset_status='Asset disposal rejected'," +
                                "process_status='R', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
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



//================================END:  THIS SEGMENT IS TO SEND E-MAIL TO RECEPIENT AFTER ASSET CREATION
                    }//

                }
            }//else
            //arb.updateAssetStatusChange(supervisorUpdate);

            out.println("<script>alert('Approval status saved')</script>");
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
