package com.magbel.ia.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magbel.ia.bus.SalesOrderServiceBus;
import com.magbel.ia.bus.DistributionOrderServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.legend.bus.ApprovalManager;

public class DistributionOrderActionServlet extends HttpServlet {

	/**
	 * Initializes the servlet.
	 */
	private SalesOrderServiceBus serviceBus;
	private DistributionOrderServiceBus distributionBus;
        private CodeGenerator cg;
        private SupervisorServiceBus superv;
        private ApprovalManager tagapprove;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		serviceBus = new SalesOrderServiceBus();
	        cg = new CodeGenerator();
	        superv = new SupervisorServiceBus(); 
	        distributionBus = new DistributionOrderServiceBus();
	        tagapprove = new ApprovalManager();
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		String compCode =  request.getParameter("companyCode");	 
		String strbalance = "";
		String id = request.getParameter("ID");
		String dstrbCode = request.getParameter("dstrbCode");
        String reqnDesc = request.getParameter("reqnDesc").toUpperCase();
        String projDesc = request.getParameter("projDesc").toUpperCase();
		String status = "";//request.getParameter("");
		String customerCode = request.getParameter("customerCode");
        String customerCode_ = request.getParameter("customerCode_"); 
		String po = "";//request.getParameter("po");
		String user_Id =  request.getParameter("userId");  
		String shipDate = request.getParameter("shipDate");
		String freight = request.getParameter("freightCode");
		String carrier = request.getParameter("carrierCode");
        String projectCode = request.getParameter("projectCode");
        String reqPersId = request.getParameter("reqPersId")!=null ? request.getParameter("reqPersId") : "Y" ;
	     String strApproveOfficer = request.getParameter("approveOfficer");
	     int approveOfficer=(strApproveOfficer!=null)?Integer.parseInt(strApproveOfficer):0;
	     String strApproveStatus = request.getParameter("approveStatus");
	     String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U";
	     String assetList = request.getParameter("AssetId_List");
	     System.out.println("<<<<<<<=======assetList: "+assetList);
	     String unitCode = request.getParameter("unitCode");
	     String unitName = request.getParameter("unitName");
	     System.out.println("<<<<<<<=======unitCode: "+unitCode+"       unitName: "+unitName);
	     if (approveOfficer==0)
	     {
	    	 strApproveStatus="A";
	     }
	     else
	     {
	    	 strApproveStatus="P";
	     }
	     
	     String reqnID = request.getParameter("reqnID");
	     String itemCD = request.getParameter("itemCD");
	     String distQty= request.getParameter("QtyDist");
	     System.out.println("reqnID >>>>>>>>> " + reqnID+"     distQty: "+distQty);
	     int QtyDist= (distQty!=null)?Integer.parseInt(distQty):0;
	     String reqQty= request.getParameter("QtyReq");
	     int QtyReq= (reqQty!=null)?Integer.parseInt(reqQty):0;
	     
	     	 String strQuantityRequest = request.getParameter("QtyReq");
	     	int QuantityRequest=(strQuantityRequest!=null && !strQuantityRequest.equals(""))?Integer.parseInt(strQuantityRequest):0; 
	         String strQuantity = request.getParameter("quantity");
	         int quantity=(strQuantity!=null && !strQuantity.equals(""))?Integer.parseInt(strQuantity):0; 
	         String itemCode = request.getParameter("itemCode")==null? "" : request.getParameter("itemCode");
	         //System.out.println("itemCode >>>>>>>>> " + itemCode);
	         String warehouseCode = request.getParameter("warehouse")==null? "" : request.getParameter("warehouse");
	         String strUnitPrice = request.getParameter("unitPrice");
	         double unitPrice=(strUnitPrice!=null && !strUnitPrice.equals(""))?Double.parseDouble(strUnitPrice):0.0d; 
	         String strAdvancePymt =  request.getParameter("advancePymt");
	         double advancePymt = (strAdvancePymt==null || strAdvancePymt.equalsIgnoreCase("")) ? 0.0 : Double.parseDouble(strAdvancePymt); 
	         String strQuantDeliver= request.getParameter("quantDeliver");
	         int quantDeliver=(strQuantDeliver!=null && !strQuantDeliver.equals(""))?Integer.parseInt(strQuantDeliver):0;
              int quantityRemain =  QuantityRequest - quantity;
	         String batchCode = request.getParameter("batchCode");
	         	                   
	         //delivery---	TO BE ADDRESSED LATER...
                 String SODeliveryAddBtn = request.getParameter("SODeliveryAddBtn");
	         String SODeliveryUpdateBtn = request.getParameter("SODeliveryUpdateBtn");
	         String SODeliveryPostBtn = request.getParameter("SODeliveryPostBtn");
                 
	         String[] code_ = null;
	         String[] itemCode_ = null;
	         String[] unitPrice_ = null;
	         String[] amount_ = null;
	         String[] quantity_ = null;
	         String[] quantDeliver_ = null;
	         String []stockId = null;
                 String[] warehouseCode_ = null;
                 
                 if((SODeliveryAddBtn != null)||(SODeliveryUpdateBtn != null)||(SODeliveryPostBtn != null)){
                    code_ = request.getParameterValues("code_");
                    quantity_ = new String[code_.length];
                    quantDeliver_ = new String[code_.length];                   
                   for(int x=0; x < quantity_.length; x++)
                   {
                         quantity_[x] = request.getParameter("quantity_"+x);
                         quantDeliver_[x] = request.getParameter("quantDeliver_"+x);
                   }
                    itemCode_ = request.getParameterValues("itemCode_");
                    unitPrice_ =request.getParameterValues("unitPrice_");
                    amount_ = request.getParameterValues("amount_"); 
                    warehouseCode_ = request.getParameterValues("warehouseCode_");
                 
                 }                    
	         String addBtn = request.getParameter("addBtn");
	         String addNewBtn = request.getParameter("addNewBtn");
	         String updateBtn = request.getParameter("updateBtn");
	         String updateBtn2 = request.getParameter("updateBtn2"); //salesOrderDetail2.jsp
	         String itemAddBtn = request.getParameter("itemAddBtn");
	         String itemUpdateBtn = request.getParameter("itemUpdateBtn");
	         String isAutoIdGen = "";
                 
			String companyCode = request.getParameter("companyCode");	 
			String assetId = request.getParameter("Asset_Id");	
		//	legend.admin.objects.User user = null;
	//		System.out.println("<<<<<<<=======stockId length before split: "+assetList+"    addNewBtn: "+addNewBtn);
			//if(addNewBtn.equalsIgnoreCase("Add")){
			stockId = assetList.split(";");
//			System.out.println("<<<<<<<=======stockId length: "+stockId.length);
	//		}
			String stockcode = "";
			 
//			int asstetCount = stockId.length;
		//	 assetId = stockId[0];
		//	 assetCode = stockId[1];
			
	    try{
	 //            if(session.getAttribute("CurrentUser")!=null){user =(legend.admin.objects.User)session.getAttribute("CurrentUser");}
	             int userId = Integer.parseInt(user_Id);//to be completed
                  String processed = "N";   
	             if(addBtn != null)
	             {
                        isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
                        
                      if(isAutoIdGen.trim().equalsIgnoreCase("N"))
                      { //test 4 auto ID setup                                               
                          if(dstrbCode.equals("")||dstrbCode == null)
                          {
		                       out.print("<script>alert('Distribution no. cannot be empty')</script>");
		                       out.print("<script>history.go(-1);</script>");
                          }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(DORDER_CODE) FROM ST_DISTRIBUTION_ORDER " +
                          		"WHERE DORDER_CODE='"+dstrbCode+"'"))
                          { 
                              out.print("<script>alert('Distribution Code Already Exist')</script>");
                              out.print("<script>history.go(-1);</script>");
                          }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM ST_DISTRIBUTION_ITEM " +
                          		"WHERE ITEM_CODE = '"+itemCode+"' AND DORDER_CODE='"+dstrbCode+"'"))
                          {
                              out.print("<script>alert('Item Already Exist')</script>");
                              out.print("<script>history.go(-1);</script>");
                           }
	                       else if(approveOfficer==-1)
	                       {
	                              out.print("<script>alert('Approving Officer cannot be empty.')</script>");
	                              out.print("<script>window.location='DocumentHelp.jsp?np=distributionOrder'</script>");
	                       }   
                           else
                           { 
                        	   if(distributionBus.createDistributionOrder(dstrbCode,customerCode,po,reqPersId,shipDate,
                        			   freight,carrier,projDesc,reqnDesc, userId,approveOfficer,projectCode,companyCode,reqnID,itemCode,quantity,unitPrice,quantityRemain,unitCode))
                        		   {
                                   for(int j=0; j < stockId.length; j++)
                                   {   //  System.out.println("<<<<<<<=======stockId[j]: "+stockId[j]);
                                	   String rfidtag = serviceBus.getCodeName("SELECT BAR_CODE FROM ST_STOCK WHERE ASSET_ID= '"+stockId[j]+"' ");
                                	   String quantvalue = serviceBus.getCodeName("SELECT QUANTITY FROM ST_STOCK WHERE ASSET_ID= '"+stockId[j]+"' ");
                                	   int quantityval = Integer.parseInt(quantvalue);
                                	   if(distributionBus.createDistributionOrderItemDetail(dstrbCode,QuantityRequest,quantity,unitPrice,advancePymt,
                            		   itemCode,warehouseCode,companyCode,reqnID,userId,reqnDesc,stockId[j],quantityRemain,unitCode,rfidtag)){
                        		  	//	distributionBus.updateStockQry(stockId[j]);
                                		   if(quantityval == quantity){distributionBus.updateStockQry(stockId[j]);}
                             	//	   if(!unitName.equalsIgnoreCase("Meter") || !unitName.equalsIgnoreCase("Volume")){distributionBus.updateStockQry(stockId[j]);} 
                             		   if(unitName.equalsIgnoreCase("MTR") || unitName.equalsIgnoreCase("KIT") || unitName.equalsIgnoreCase("SET")){stockcode = stockId[j];}                        		  		
                        		  		processed = "Y";}
                                	   boolean useTagUpdate = tagapprove.deletedistributedafterIssuanceRfidRecord(rfidtag);
                                   }
                                   strbalance = serviceBus.getCodeName("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE ITEM_CODE= '"+itemCode+"'  AND WAREHOUSE_CODE= '"+warehouseCode+"' ");
                        		  if(processed.equalsIgnoreCase("Y")){//serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
	                        	  distributionBus.updateRequisitionQty(reqnID,quantity,QtyDist,QtyReq);
	                        	  if(strbalance.equalsIgnoreCase("0") && !stockcode.equalsIgnoreCase("")){distributionBus.updateStockQry(stockcode);}
                        		  }
                        		   }
	                        	
                               if(approveOfficer > 0)
                               {
                            	   distributionBus.createTransactionApproval(dstrbCode,"DISTRIBUTION","DISTRIBUTION ORDER",
                                		   userId,approveOfficer,approveStatus,itemCode,quantity,request.getRemoteAddr(),compCode);    
                               }
                               
                               out.print("<script>alert('Record Succesfully Saved.')</script>");
                               out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
                               		"distributionOrderDetail&dstrbCode="+dstrbCode+"'</script>");
                               
                           }  
                       }
                       else
                       {
                    	   dstrbCode = cg.generateCode("DISTRIBUTION ORDER","","","");
                    	   
                         if(serviceBus.isRecordExisting("SELECT COUNT(DORDER_CODE) FROM ST_DISTRIBUTION_ORDER " +
                         		"WHERE DORDER_CODE='"+dstrbCode+"'"))
                         { 
                            out.print("<script>alert('Distribution Order Code Already Exist')</script>");
                            out.print("<script>history.go(-1);</script>");
                         }
	                     else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM ST_DISTRIBUTION_ITEM " +
	                     		"WHERE ITEM_CODE = '"+itemCode+"' AND DORDER_CODE='"+dstrbCode+"'"))
	                     {
	                            out.print("<script>alert('Item Already Exist')</script>");
	                            out.print("<script>history.go(-1);</script>");       
	                     }
                         else if(approveOfficer==-1)
                         {
                                 out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                                 out.print("<script>window.location='DocumentHelp.jsp?np=distributionOrder'</script>");
                         }
                         
                         
	                     else
	                     { 
	                        if(distributionBus.createDistributionOrder(dstrbCode,customerCode,po,reqPersId,shipDate,
	                        		freight,carrier,projDesc,reqnDesc, userId,approveOfficer,projectCode,companyCode,reqnID,itemCode,quantity,unitPrice,quantityRemain,unitCode))
	                        		
	                        	{
                                   for(int j=0; j < stockId.length; j++)
                                   {  //   System.out.println("<<<<<<<=======stockId[j]: "+stockId[j]);
                                	   String rfidtag = serviceBus.getCodeName("SELECT BAR_CODE FROM ST_STOCK WHERE ASSET_ID= '"+stockId[j]+"' ");
                                	   String quantvalue = serviceBus.getCodeName("SELECT QUANTITY FROM ST_STOCK WHERE ASSET_ID= '"+stockId[j]+"' ");
                                	   int quantityval = Integer.parseInt(quantvalue);
                                	   if(distributionBus.createDistributionOrderItemDetail(dstrbCode,QuantityRequest,quantity,unitPrice,advancePymt,
                            		   itemCode,warehouseCode,companyCode,reqnID,userId,reqnDesc,stockId[j],quantityRemain,unitCode,rfidtag)){
                                		   if(quantityval == quantity){distributionBus.updateStockQry(stockId[j]);
                                		 //  System.out.println("<<<<<<<=======updateStockQry Processed  ");
                                		   }
                                	//	   if(!unitName.equalsIgnoreCase("Meter") || !unitName.equalsIgnoreCase("Volume")){distributionBus.updateStockQry(stockId[j]);} 
                                		   if(unitName.equalsIgnoreCase("Meter") || unitName.equalsIgnoreCase("Volume")){stockcode = stockId[j];}
                        		  		processed = "Y";}
                                	   boolean useTagUpdate = tagapprove.deletedistributedafterIssuanceRfidRecord(rfidtag);
                                	  // System.out.println("<<<<<<<=======processed: "+processed+"     quantityval: "+quantityval+"    quantity: "+quantity+"   QtyDist: "+QtyDist+"    QtyReq: "+QtyReq);
                                   }
                                 //  System.out.println("<<<<<<<=======itemCode: "+itemCode+"     warehouseCode: "+warehouseCode+"    reqnID: "+reqnID);
                                   strbalance = serviceBus.getCodeName("SELECT BALANCE FROM ST_INVENTORY_TOTALS WHERE ITEM_CODE= '"+itemCode+"'  AND WAREHOUSE_CODE= '"+warehouseCode+"' ");
                        		  if(processed.equalsIgnoreCase("Y")){serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
	                        	  distributionBus.updateRequisitionQty(reqnID,quantity,QtyDist,QtyReq);
	                        	  if(strbalance.equalsIgnoreCase("0") && !stockcode.equalsIgnoreCase("")){distributionBus.updateStockQry(stockcode);}
                        		  }
                        		   }
	                 //       {  Matanmi
	                        	
	                        	if(approveOfficer > 0)
	                        	  {
	                        		distributionBus.createTransactionApproval(dstrbCode,"DISTRIBUTION","DISTRIBUTION ORDER",
	                                		   userId,approveOfficer,approveStatus,itemCode,quantity,request.getRemoteAddr(),compCode);
//		                        	serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
//		                        	distributionBus.updateRequisitionQty(reqnID,quantity,QtyDist,QtyReq);
                                  }
                                 out.print("<script>alert('Record Succesfully Saved.')</script>");
                                 out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
                                  		"distributionOrderDetail&dstrbCode="+dstrbCode+"'</script>");
	                      //  }Matanmi
                         }
                            }  
                         }
                       
	            if(updateBtn != null)
	            {
                  if(distributionBus.updateDistributionOrder(po,freight,carrier,customerCode,dstrbCode,shipDate,reqPersId,
                		  approveOfficer,projectCode))
                  {
	              if(!warehouseCode.equals("") && !itemCode.equals("") && quantity > 0 && unitPrice > 0)
	               { 
                    if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM ST_DISTRIBUTION_ITEM WHERE " +
                    		"ITEM_CODE = '"+itemCode+"' AND DORDER_CODE='"+dstrbCode+"'"))
                    	
                           {
	                           out.print("<script>alert('Item Already Exist')</script>");
	                           out.print("<script>history.go(-1);</script>");
	                       }
	                       else
	                       {
                               for(int j=0; j < stockId.length; j++)
                               {   
                            	   String rfidtag = serviceBus.getCodeName("SELECT BAR_CODE FROM ST_STOCK WHERE ASSET_ID= '"+stockId[j]+"' ");
	                    	   distributionBus.createDistributionOrderItemDetail(dstrbCode,QuantityRequest,quantity,unitPrice,advancePymt,
                            		   itemCode,warehouseCode,companyCode,reqnID,userId,reqnDesc,stockId[j],quantityRemain,unitCode,rfidtag);
                                  //System.out.println("---------------------------------------------");
                                  //serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
	                    	   boolean useTagUpdate = tagapprove.deletedistributedafterIssuanceRfidRecord(rfidtag);
                               }
                                    if(approveOfficer > 0)
                                    {
                                        superv.createTransactionApproval(dstrbCode,"DISTRIBUTION","DISTRIBUTION ORDER",userId,
                                        		approveOfficer,approveStatus,itemCode,quantity);  
                                    }
                                }
	                            
	                 }
	               }
	                      out.print("<script>alert('Record Succesfully Saved.')</script>");
	                      out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
	                      		"distributionOrderDetail&dstrbCode="+dstrbCode+"'</script>");
	                     	                  	                  
	              }
                       
	             if(updateBtn2 != null){
	                     if(serviceBus.updateSalesOrder(po,freight,carrier,customerCode,dstrbCode,shipDate,reqPersId,approveOfficer,projectCode)){
                               if(!warehouseCode.equals("") && !itemCode.equals("") && quantity > 0 && unitPrice > 0){ 
                                   serviceBus.updateSalesOrderItemDetail(id,dstrbCode,quantity,unitPrice,warehouseCode,itemCode);        
	                         }
                                out.print("<script>alert('Record Succesfully Saved.')</script>");
                                out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDetail2&dstrbCode="+dstrbCode+"&itemCode="+itemCode+"&warehouseCode="+warehouseCode+"'</script>");
                                      
                            }
	                                                             
	                }   
                    
                    if(itemAddBtn != null){
	                 if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM ST_DISTRIBUTION_ITEM " +
	                 		"WHERE ITEM_CODE = '"+itemCode+"' AND WAREHOUSE_CODE='"+warehouseCode+"' AND DORDER_CODE='"+dstrbCode+"'")){
	                     out.print("<script>alert('Item Already Exist')</script>");
	                     out.print("<script>history.go(-1);</script>");
	                 }
	                 else if(approveOfficer==-1){
	                      out.print("<script>alert('Approving Officer cannot be empty.')</script>");
	                      out.print("<script>window.location='DocumentHelp.jsp?np=salesOrder'</script>");
                        }
                         else{
	                     if(serviceBus.createSalesOrderItemDetail(dstrbCode,quantity,unitPrice,advancePymt,itemCode,warehouseCode,companyCode))
	                      if(approveOfficer > 0){
                               superv.createTransactionApproval(dstrbCode,"SALES","SALES ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);
                              }
                              out.print("<script>self.close();</script>");
                              
	                 }
	             }
	            if(itemUpdateBtn != null){
	                      serviceBus.updateSalesOrderItemDetail(id,dstrbCode,quantity,unitPrice,warehouseCode,itemCode);
	                      out.print("<script>self.close();</script>");
	                      
	                  }
	                        
	           if(SODeliveryAddBtn != null){
	                       
	                       /*if(batchCode.equals("")||batchCode == null){
	                            out.print("<script>alert('Delivery no. cannot be empty')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                        }
	                        else{
	                        if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM ST_SO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"'")){
	                            out.print("<script>alert('Delivery no. Already Exist')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                        }*/
	                        //else{
                                    batchCode = cg.generateCode("SALES ORDER DELIVERY","","","");
	                            serviceBus.createSODeliveryItem(dstrbCode,batchCode,code_,itemCode_,quantity_,quantDeliver_,userId,unitPrice_,amount_,warehouseCode_);
	                            out.print("<script>alert('Record Succesfully Saved.')</script>");
                                    out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&dstrbCode="+dstrbCode+"&customerCode="+customerCode+"'</script>");
                                            
                          }
	                 if(SODeliveryUpdateBtn != null){
	                                
	                                serviceBus.updateSODeliveryItem(dstrbCode,batchCode,code_,quantity_,quantDeliver_);
	                                  out.print("<script>alert('Record Succesfully Saved.')</script>");
	                                  out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&dstrbCode="+dstrbCode+"&customerCode="+customerCode+"'</script>");
                                         	                                  
	                              }
	             if(SODeliveryPostBtn != null){
	                              if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM ST_SO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"' AND POSTED = 'Y'")){
	                                  out.print("<script>alert('Posting already done on Sales')</script>");
	                                  out.print("<script>history.go(-1);</script>");
	                              }
	                              else{
	                                  if(serviceBus.postShipment2InventoryHistory(dstrbCode,batchCode,companyCode,quantDeliver_,"")){
                                          if(serviceBus.postShipment2InventoryTotals(dstrbCode,batchCode,""))
                                          {
	                                   out.print("<script>alert('Posting successfully done.')</script>");
	                                   out.print("<script>window.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&dstrbCode="+dstrbCode+"&customerCode="+customerCode+"'</script>");
	                                 }
	                              }
	                          }             
	             }                     
	         
	         }
	         catch(NullPointerException e){
	              response.sendRedirect("sessionTimedOut.jsp");
	           }
	         catch(Exception e){
	             e.printStackTrace();
	         }
	         

	     }    

	

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Sales Order Action Servlet";
	}

}
