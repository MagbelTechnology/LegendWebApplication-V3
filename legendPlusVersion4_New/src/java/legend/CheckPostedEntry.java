package legend;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

import   magma.asset.manager.*;
import   magma.net.manager.RaiseEntryManager;
import magma.AssetRecordsBean;

import com.magbel.legend.bus.ApprovalRecords;

import  magma.asset.manager.WIPAssetManager;
/**
 *
 * @author Ganiyu
 */
public class CheckPostedEntry extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/xml";
    private static final String DOC_TYPE = null;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    //@Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  
//        System.out.println("here in checkPostedEntry servlet >>>>>>>>>>>>");
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();

        AssetManager assManager = new AssetManager();
        RaiseEntryManager raiseMan = new RaiseEntryManager();
          WIPAssetManager wipAssetMan = new WIPAssetManager();
          String userClass = (String)request.getSession().getAttribute("UserClass");
          
        String asset_id = request.getParameter("asset_id");
        String page1 = request.getParameter("page1");
        String transactionIdCost = request.getParameter("transactionIdCost");
        String transactionIdVendor = request.getParameter("transactionIdVendor");
        String transactionIdWitholding = request.getParameter("transactionIdWitholding");
        String transactionIdVat = request.getParameter("transactionIdVat");
        String transId = request.getParameter("tranId");
//        System.out.println("Transaction Id: "+transId);
        int tranId = request.getParameter("tranId")==null?0:Integer.parseInt(request.getParameter("tranId"));
        String uselife = request.getParameter("usefullife");
        if(uselife==null || uselife ==""){uselife = "0";}
        int useful_life = Integer.parseInt(uselife);
//        int useful_life = request.getParameter("usefullife")==null?0:Integer.parseInt(request.getParameter("usefullife"));
        //======================= for disposal =================================
        String transactionIdAccum = request.getParameter("transactionIdAccum");
        String transactionIdDisposal = request.getParameter("transactionIdDisposal");
        String transactionIdProfit = request.getParameter("transactionIdProfit");
        String AssetCode= request.getParameter("AssetCode");
        String transactionIdVendor2 = request.getParameter("transactionIdVendor2");
        String dispopercent = request.getParameter("dispopercent");
        String acceleratedCost = request.getParameter("acceleratedCost");
        String remainlife = request.getParameter("remainlife");
        String usefullife = request.getParameter("usefullife");
//        System.out.println("here in >>>>>>>>>dispopercent "+dispopercent);
//         System.out.println("here in >>>>>>>>>AssetCode"+AssetCode);
//          System.out.println("here in >>>>>>>>>transactionIdCost"+transactionIdCost);
//           System.out.println("here in >>>>>>>>>transactionIdVendor"+transactionIdVendor);
//           System.out.println("here in >>>>>>>>>transactionIdVendor2"+transactionIdVendor2);
//             System.out.println("here in >>>>>>>>>page1"+page1);
//              System.out.println("here in >>>>>>>>>tranId"+tranId);
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            AssetRecordsBean ad = new AssetRecordsBean();
            ApprovalRecords app = new ApprovalRecords();

            if ((transactionIdCost != null && transactionIdCost.equals("1")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("2")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("3"))) {

//                System.out.println("here in >>>>>>>>>cost,vendor,witholding");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
                 //  System.out.println("here in >>>>>>>>>posting for uncapitalized 123");
                app.NewAssetRaiseEntryPost(asset_id, page1, prcessCost, prcessCost2, prcessWithTax);


            }
            if ((transactionIdCost != null && transactionIdCost.equals("1")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("2")) &&
                    (transactionIdVat != null && transactionIdVat.equals("25"))) {
//                System.out.println("here in >>>>>>>>>cost,vendor,vat");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String processCreateVat = ad.isoCheck(asset_id, page1, transactionIdVat);
//                System.out.println("here are the Parameters in >>>>>>>>> Asset Id: "+asset_id+"  page1: "+page1+"  prcessCost: "+prcessCost+"  prcessCost2: "+prcessCost2+"  processCreateVat: "+processCreateVat);
                app.updateRaiseEntryPost(asset_id, page1, prcessCost, prcessCost2, processCreateVat);

            }

            if ((transactionIdCost != null && transactionIdCost.equals("1")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("2")) &&
                    (transactionIdWitholding == null)) {
                System.out.println("here in >>>>>>>>>cost,vendor");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
               //   System.out.println("here in >>>>>>>>>posting for uncapitalized 12");
                app.updateRaiseEntryPost(asset_id, page1, prcessCost, prcessCost2);

            }
  
            //for disposal
            if ((transactionIdAccum != null && transactionIdAccum.equals("5")) &&
                    (transactionIdDisposal != null && transactionIdDisposal.equals("6")) &&
                    (transactionIdProfit != null && transactionIdProfit.equals("7")) &&
                    (transactionIdCost != null && transactionIdCost.equals("4"))) {
//                System.out.println("here in >>>>>>>>>disposal segement");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessAccum = ad.isoCheck(asset_id, page1, transactionIdAccum);
                String prcessDisposal = ad.isoCheck(asset_id, page1, transactionIdDisposal);
                String prcessProfit = ad.isoCheck(asset_id, page1, transactionIdProfit);
                app.updateRaiseEntryPost(asset_id, page1, prcessCost, prcessAccum, prcessDisposal, prcessProfit,dispopercent);

            }

            // for advance payment
            if ((transactionIdCost != null && transactionIdCost.equals("11")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("12")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("13"))) {

//                System.out.println("here in >>>>>>>>>cost,vendor,witholding of advance payment");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
                app.updateRaiseEntryPost(asset_id, page1, prcessCost, prcessCost2, prcessWithTax);


            }
            // for Detail payment for Group
            if ((transactionIdCost != null && transactionIdCost.equals("21")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("22")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("23"))) {

//                System.out.println("here in >>>>>>>>>cost,vendor,witholding of Group payment");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
                app.updateRaiseEntryPostforGroup(asset_id, page1, prcessCost, prcessCost2, prcessWithTax);


            }
         // for Summary payment for Group
            if ((transactionIdVendor != null && transactionIdVendor.equals("22")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("23"))) {
            	String prcessCost = "";
 //               System.out.println("here in >>>>>>>>>cost,vendor,witholding of Group payment for two parameters");
//                System.out.println("transactionIdCost>>>>: "+transactionIdCost+" asset_id: "+asset_id+" page1: "+page1);
                //String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
 //               System.out.println("transactionIdVendor>>>>: "+transactionIdVendor+" asset_id: "+asset_id+" page1: "+page1);
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
 //               System.out.println("<<<prcessCost2>>>>: "+prcessCost2);
 //               System.out.println("transactionIdWitholding>>>>: "+transactionIdWitholding+" asset_id: "+asset_id+" page1: "+page1);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
 //               System.out.println("<<<prcessWithTax>>>>: "+prcessWithTax);
 //               System.out.println("transactionIdCost>>>>: "+transactionIdCost+" asset_id: "+asset_id+" page1: "+page1);
 //               System.out.println("prcessCost>>>>: "+prcessCost+" prcessCost2: "+prcessCost2+" prcessWithTax: "+prcessWithTax);
                prcessCost = prcessCost2;
                app.updateRaiseEntryPostforGroup(asset_id, page1, prcessCost, prcessCost2, prcessWithTax);
            }            

            // for asset improvement
            if ((transactionIdCost != null && transactionIdCost.equals("26")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("27")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("28"))) {

//                System.out.println("here in >>>>>>>>>cost,vendor,witholding of asset improvement "+" and Transaction Id: "+tranId);

                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost,String.valueOf(tranId));
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
//                System.out.println("here in >>>>>>>>>cost,vendor,witholding of asset improvement"+prcessCost+"  "+prcessCost2+"  "+prcessWithTax);
                // app.updateRaiseEntryPostEmail(asset_id, page1, prcessCost, prcessCost2, prcessWithTax, "improvement");
                app.updateRaiseEntryPostEmail(asset_id, page1, prcessCost, prcessCost2, prcessWithTax, "improvement",tranId,useful_life);

            }
  
            // for asset revaluation
            if ((transactionIdCost != null && transactionIdCost.equals("36")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("37")) &&
                    (transactionIdWitholding != null && transactionIdWitholding.equals("38"))) {

//                System.out.println("here in >>>>>>>>>cost,vendor,witholding of asset revaluation");

                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost,String.valueOf(tranId));
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
                // app.updateRaiseEntryPostEmail(asset_id, page1, prcessCost, prcessCost2, prcessWithTax, "improvement");
                app.updateRevaluationRaiseEntryPostEmail(asset_id, page1, prcessCost, prcessCost2, prcessWithTax, "revaluation",tranId);

            }

//for asset transfer
            if ((transactionIdCost != null && transactionIdCost.equals("20")) &&
                    (transactionIdAccum != null && transactionIdAccum.equals("21"))) {
                 String newAssetId = app.getCodeName("select new_asset_id from AM_ASSETTRANSFER where asset_id='" + asset_id + "'");
               // System.out.println("\nhere in >>>>>>>>>cost,vendor,witholding of asset transfer");
              //  System.out.println("\nhere in >>>>>>>>>transactionIdCost " +transactionIdCost);
               // System.out.println("\nhere in >>>>>>>>>transactionIdAccum " +transactionIdAccum);
                // System.out.println("\nhere in >>>>>>>>>asset_id " +asset_id);
                String prcessCost = ad.isoCheck(newAssetId, page1, transactionIdCost);
                String prcessAccum = ad.isoCheck(newAssetId, page1, transactionIdAccum);
                app.updateRaiseEntryPostTransfer(asset_id, page1, prcessCost, prcessAccum,"transfer");

            }

            if ((transactionIdCost != null && transactionIdCost.equals("A")) &&
                    (transactionIdAccum != null && transactionIdAccum.equals("B")))
            {
//                System.out.println("here in >>>>>>>>>cost,vendor,witholding for WIP");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
 //               System.out.println("prcessCost >>>>> " + prcessCost);
                if (prcessCost.equals("000"))
                {
                    //update am_wipassettransfer set raise_entry to R where asset_id=old_asset_id
                    //and
                    String upd_am_wip_reclassification_Qry="update am_wip_reclassification set raise_entry='Y'" +
                            " where asset_id='"+assManager.getTransferedOldAssetDetails(asset_id)+"'";
                    //System.out.println("upd_am_wip_reclassification_Qry >>>>>>> " + upd_am_wip_reclassification_Qry);
                    raiseMan.setRaiseEntryStatus(upd_am_wip_reclassification_Qry);
                     String query = "update am_raisentry_post set entrypostflag='Y' where trans_id="+tranId+" and page='"+page1+"' ";
                    int result3  = app.updateUtil(query);
                }
                String prcessAccum = "000";
                app.updateRaiseEntryPostTransfer(asset_id, page1, prcessCost, prcessAccum,"WIP");
            }

            // For new asset part payment
             if ((transactionIdCost != null && transactionIdCost.equals("30"))) {

//                System.out.println("here in >>>>>>>>>cost payment");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost);
                //String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                //String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
               // app.updateRaiseEntryPost(asset_id, page1, prcessCost, prcessCost2, prcessWithTax);
               if(prcessCost.equals("000")){
                 String updateAssetStatus="update am_asset set asset_status ='ACTIVE' where asset_id='"+asset_id+"' and asset_status='APPROVED'";
	 ad.updateAssetStatusChange(updateAssetStatus);
               }

            }


          // for asset wip reclass 1
            if ((transactionIdCost != null && transactionIdCost.equals("32")) &&
                    (transactionIdAccum != null && transactionIdAccum.equals("33"))  ) {

//                System.out.println("here in >>>>>>>>> wip reclass 1");

                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost,String.valueOf(tranId));
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor,String.valueOf(tranId));
                if(prcessCost.equals("000"))
                { 
                      assManager.updateAssetWip(AssetCode);
                    String query = "update am_raisentry_post set entrypostflag='Y' where trans_id="+tranId;
                    int result3  = app.updateUtil(query);
                     wipAssetMan.updateWIPInfoFuture(asset_id);
                     
                }

            }
                  // for asset reclass 1
            if ((transactionIdCost != null && transactionIdCost.equals("15")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("16"))  ) {

//                System.out.println("here in >>>>>>>>>reclass 1");
   System.out.println("---"+asset_id+"---------"+page1+"-------"+transactionIdCost+""+String.valueOf(tranId)+"-------- -----");
                String prcessCost = ad.isoCheck(asset_id, page1, transactionIdCost,String.valueOf(tranId));
                String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor,String.valueOf(tranId)); 
                if(prcessCost.trim().equals("000"))
                {
                      String query = "update am_raisentry_post set entrypostflag='Y' where trans_id="+tranId ;
                   // System.out.println("\n\n the query " +query);
                      int result3  = app.updateUtil(query);
                     assManager.updateAssetReclass2(AssetCode);
                    
                }

            }

            // for Accelerated Depreciation
         //   System.out.println("here in >>>>>>>>>Accelerated>>>>>: "+transactionIdCost);
            if ((transactionIdCost != null && transactionIdCost.equals("40"))) {

    //            System.out.println("here in >>>>>>>>>Accelerated Depreciation Tarnsaction Posting and transId: "+transId);

                String ISOprcessCost = ad.isoCheck(asset_id, page1, transactionIdCost,transId);
     //           System.out.println("here in >>>>>>>>>Accelerated>>>>>: "+ISOprcessCost);
               // String prcessCost2 = ad.isoCheck(asset_id, page1, transactionIdVendor);
                
             //   String prcessWithTax = ad.isoCheck(asset_id, page1, transactionIdWitholding);
                // app.updateRaiseEntryPostEmail(asset_id, page1, prcessCost, prcessCost2, prcessWithTax, "improvement");
                app.updateRaiseEntryAccelerated(asset_id, page1, ISOprcessCost, acceleratedCost, remainlife, usefullife, tranId);

            }
            
          // for asset reclass 2
            if ((transactionIdCost != null && transactionIdCost.equals("15")) &&
                    (transactionIdVendor != null && transactionIdVendor.equals("16")) &&
                      (transactionIdVendor != null && transactionIdVendor2.equals("31"))) {

                //System.out.println("\n >>>>>>>>>bbb reclass 2");
                // System.out.println("\n >>>>>>>>>asset_id " +asset_id);
                 // System.out.println("\n >>>>>>>>>page1 " +page1);
                 //  System.out.println("\n >>>>>>>>>tranId" + tranId);


                  String prcessCost = ad.isoCheck(  asset_id, page1,transactionIdCost,String.valueOf(tranId));

                  //String prcessVendor = ad.isoCheck(  asset_id, page1,transactionIdVendor,String.valueOf(tranId));
                 // String prcessVendor2 = ad.isoCheck(  asset_id, page1,transactionIdVendor2,String.valueOf(tranId));

                 // System.out.println("\n the value of prcessCost >>>>>>  " + prcessCost);
                  if(prcessCost.trim().equals("000"))
                {
                   // System.out.println("\n asset Code >>>>>>>>> "+AssetCode);
                    // System.out.println("\n tran id  >>>>>>>>> "+tranId);
                    //  System.out.println("\n page1  >>>>>>>>> "+page1);
                       String query = "update am_raisentry_post set entrypostflag='Y' where trans_id="+tranId;
                    //System.out.println("\n\n the query " +query);
                       int result3  = app.updateUtil(query);
                     assManager.updateAssetReclass2(AssetCode);
                   

                }

            }
        }
        } catch (Exception e) {

            System.out.println(">>>>>>>>>>>>Error occured in CheckPostedEntry " + e);
        } finally {
        }

    }
}

