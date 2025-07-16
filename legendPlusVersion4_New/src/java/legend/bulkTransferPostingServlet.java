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

import magma.AssetRecordsBean;
import magma.asset.manager.AssetManager;
import magma.AssetReclassificationBean;
import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.legend.bus.ApprovalManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import magma.net.manager.BulkUpdateManager;
import  magma.asset.manager.WIPAssetManager;

//import magma.net.manager.AssetManager;
/**
 *
 * @author Olabo
 */
public class bulkTransferPostingServlet extends HttpServlet {

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
        String disposaltype = "";
        String userClass = (String)request.getSession().getAttribute("UserClass");
        
        try {
        	if (!userClass.equals("NULL") || userClass!=null){
            String assetRaiseEntry =aprecords.getCodeName(" SELECT raise_entry from am_gb_company ");

int assetCode = request.getParameter("assetCode") == null?0:Integer.parseInt(request.getParameter("assetCode"));
            //--System.out.println("\nthe assetCode is >>>>>>>>>>>>>>>>> " + assetCode);
            ApprovalManager approverManager = new ApprovalManager();
            AssetReclassificationBean asset_reclassification = new AssetReclassificationBean();
            AssetRecordsBean arb = new AssetRecordsBean();

            String astatus = request.getParameter("astatus");
            //String newAsset = request.getParameter("approveBtn");
            String newCategoryID = request.getParameter("newCategoryID");
            String full_status = request.getParameter("full_status");
            String asset_id = request.getParameter("id") == null ? "" : request.getParameter("id");

            String rr = request.getParameter("reject_reason");
            String tableName = request.getParameter("tableName") == null ? "" : request.getParameter("tableName");
            String columnName = request.getParameter("columnName");
            String category = request.getParameter("category");

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
            System.out.println("here >>>>>>Approval tranId " +request.getParameter("tranId"));
            int tranId = Integer.parseInt(request.getParameter("tranId"));

            String prooftranId = request.getParameter("prooftranId");
            System.out.println("here >>>>>>>>Approval prooftranId " +prooftranId);
            
            int tranLevel = Integer.parseInt(request.getParameter("tranLevel"));

            int approvalCount = Integer.parseInt(request.getParameter("approvalCount"));

            int userId = Integer.parseInt(request.getParameter("user_id"));

            String partialtype = request.getParameter("issuable");
            
            String batchId = request.getParameter("batchId");

            String newAssetId = request.getParameter("asset_id");
            String destination = request.getParameter("destination");
//            System.out.println("destination ================== "+destination);
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
            
            int supervisor = (request.getParameter("supervisor") == null) ? 0 : Integer.parseInt(request.getParameter("supervisor"));
            String alertmessage = "Transaction Approved";
//             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>Approval full_status " +full_status);
//             System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>astatus " +astatus);
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
//            System.out.println("<<<<<<<<<approveddate: "+approveddate);
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

                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer")) {
                        //   System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.2");
                    	String page = "BULK ASSET TRANSFER RAISE ENTRY";
                           approvalRemark.setApprovalLevel(approvalCount);
                           approvalRemark.setRemark("");
                           approvalRemark.setStatus("Posted");
                           approvalRemark.setTranType("Bulk Asset Transfer");
                //           String url = "DocumentHelp.jsp?np=bulkAssetTransferPosting&id=" + tranId + "&operation=0&exitPage=DocumentHelp.jsp?np=manageTransfers&pageDirect=Y";
                           if (approvalCount == tranLevel) {
                        //       System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
//                        	   System.out.println("here >>>>>>>>>>>>>>>>>>>> "+ tranLevel+" approvalCount: "+approvalCount);
                               String q = "update am_asset_approval set process_status='A', asset_status='Bulk Asset Transfer',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                               arb.updateAssetStatusChange(q);

                               ArrayList newAssetList = (ArrayList) request.getSession().getAttribute("newBulkAssetTransfer");
//                               System.out.println("the size of sessioned ArrayList is >>>>>>>> " + newAssetList.size());
                               BulkUpdateManager bum = new BulkUpdateManager();
                               bum.BulkAssetTransfer(newAssetList);
//                               System.out.println("here >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1.3");
                               description = "Bulk Asset Transfer with Batch Id "+tranId+"";
                               int tranlent = String.valueOf(tranId).length();
//                             System.out.println("the for the tranlent is >>>>>>>>>>>>>>>> " + tranlent);                               
                       		String revalue_query = "update am_assettransfer set approval_Status='ACTIVE' where substring(CAST(transfer_id AS varchar(38)),1,"+tranlent+") = '"+tranId+"'";
                       		arb.updateAssetStatusChange(revalue_query);
//                       	 System.out.println("the for the revalue_query is >>>>>>>>>>>>>>>> " + revalue_query); 
                                String q3 = "update am_gb_bulkTransfer set Status='APPROVED' where Batch_id = '"+ Integer.toString(tranId)+"'";
                                   arb.updateAssetStatusChange(q3);
                                   String q4 = "update am_raisentry_post set entryPostFlag='Y', GroupIdStatus = 'Y' where Id = '"+ Integer.toString(tranId)+"'";
                                   arb.updateAssetStatusChange(q4);
                               approverManager.createApprovalRemark(approvalRemark);

                               String msgText1 = "Your transaction for Bulk asset transfer with Batch ID " + tranId + " has been Completed. The new Batch ID is " + tranId;
                               mail.sendMailTransactionInitiator(tranId, "Bulk Asset Transfer", msgText1);

                           } else {
                               arb.incrementApprovalCount2(tranId, approvalCount, supervisor);
                               approverManager.createApprovalRemark(approvalRemark);
                           }
                           //arb.incrementApprovalCount(tranId,approvalCount,supervisor);

                       }//inner if
                  
                }//outer if

                else
                {

                    // Bulk Asset Transfer Rejection
                    if (full_status.equalsIgnoreCase("Bulk Asset Transfer") && astatus.equalsIgnoreCase("R"))
                    {
                        // String q= "update am_asset_approval set asset_status='Asset Depreciation Adjusted' where asset_id = '"+asset_id+"'and transaction_id="+tranId;
                        String q = "update am_asset_approval set process_status='R', asset_status='Asset transfer rejected', reject_reason='" + rr + "',DATE_APPROVED = '"+approveddate+"' where transaction_id=" + tranId;
                  //      System.out.println("Query for  Reject of Assets>>> "+q);
                        arb.updateAssetStatusChange(q);
                        String bulkquery = "delete from  am_gb_bulkTransfer where Batch_id=" + tranId;
                        arb.updateAssetStatusChange(bulkquery);
                        String bulkquery1 = "delete from  am_gb_bulkTransfer where Batch_id=" + tranId;
                        arb.updateAssetStatusChange(bulkquery1);
                        alertmessage = "Transaction Rejected";
                        approvalRemark.setApprovalLevel(approvalCount += 1);
                        approvalRemark.setRemark(rr);
                        approvalRemark.setStatus("Rejected");
                        approvalRemark.setTranType("Bulk Asset Transfer");
                        approverManager.createApprovalRemark(approvalRemark);
                        String msgText1 = "Your transaction for Bulk asset transfer with Batch ID " + tranId + " has been Rejected. The new Batch ID is " + tranId;
                        mail.sendMailTransactionInitiator(tranId, "Bulk Asset Transfer", msgText1);
                    }//inner if                    
                }
            }//else
            //arb.updateAssetStatusChange(supervisorUpdate);

            out.println("<script>alert('"+alertmessage+"')</script>");
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
