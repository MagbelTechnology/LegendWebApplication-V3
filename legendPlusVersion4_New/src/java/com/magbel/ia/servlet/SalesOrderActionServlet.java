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
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.util.CodeGenerator;

public class SalesOrderActionServlet extends HttpServlet {

	/**
	 * Initializes the servlet.
	 */
	private SalesOrderServiceBus serviceBus;
        private CodeGenerator cg;
        private SupervisorServiceBus superv;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		serviceBus = new SalesOrderServiceBus();
	        cg = new CodeGenerator();
	        superv = new SupervisorServiceBus();  
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

		String id = request.getParameter("ID");
		String orderCode = request.getParameter("orderCode");
                String desc = "";//request.getParameter("desc");
		String status = "";//request.getParameter("");
		String customerCode = request.getParameter("customerCode");
	        String customerCode_ = request.getParameter("customerCode_"); 
		String po = "";//request.getParameter("po");
		String shipDate = request.getParameter("shipDate");
		String freight = request.getParameter("freightCode");
		String carrier = request.getParameter("carrierCode");
                String projectCode = request.getParameter("projectCode");
                String reqPersId = request.getParameter("reqPersId")!=null ? request.getParameter("reqPersId") : "Y" ;
	         String strApproveOfficer = request.getParameter("approveOfficer");
	         int approveOfficer=(strApproveOfficer!=null)?Integer.parseInt(strApproveOfficer):0;
	         String strApproveStatus = request.getParameter("approveStatus");
	         String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U";
                //String codegen = request.getParameter("codegen");
                
	         //detail
	         String strQuantity = request.getParameter("quantity");
	         int quantity=(strQuantity!=null && !strQuantity.equals(""))?Integer.parseInt(strQuantity):0; 
	         String itemCode = request.getParameter("itemCode")==null? "" : request.getParameter("itemCode");
	         String warehouseCode = request.getParameter("warehouseCode")==null? "" : request.getParameter("warehouseCode");
	         String strUnitPrice = request.getParameter("unitPrice");
	         double unitPrice=(strUnitPrice!=null && !strUnitPrice.equals(""))?Double.parseDouble(strUnitPrice):0.0d; 
	         String strAdvancePymt =  request.getParameter("advancePymt");
	         double advancePymt = (strAdvancePymt==null || strAdvancePymt.equalsIgnoreCase("")) ? 0.0 : Double.parseDouble(strAdvancePymt); 
	         String strQuantDeliver= request.getParameter("quantDeliver");
	         int quantDeliver=(strQuantDeliver!=null && !strQuantDeliver.equals(""))?Integer.parseInt(strQuantDeliver):0;
                 
	         String batchCode = request.getParameter("batchCode");
	         	                   
	         //delivery
                 String SODeliveryAddBtn = request.getParameter("SODeliveryAddBtn");
	         String SODeliveryUpdateBtn = request.getParameter("SODeliveryUpdateBtn");
	         String SODeliveryPostBtn = request.getParameter("SODeliveryPostBtn");
                 
	         String[] code_ = null;
	         String[] itemCode_ = null;
	         String[] unitPrice_ = null;
	         String[] amount_ = null;
	         String[] quantity_ = null;
	         String[] quantDeliver_ = null;
                 String[] warehouseCode_ = null;
                 
                 if((SODeliveryAddBtn != null)||(SODeliveryUpdateBtn != null)||(SODeliveryPostBtn != null)){
                    code_ = request.getParameterValues("code_");
                    quantity_ = new String[code_.length];
                    quantDeliver_ = new String[code_.length];                   
                   for(int x=0; x < quantity_.length; x++){
                         quantity_[x] = request.getParameter("quantity_"+x);
                         quantDeliver_[x] = request.getParameter("quantDeliver_"+x);
                      }
                    itemCode_ = request.getParameterValues("itemCode_");
                    unitPrice_ =request.getParameterValues("unitPrice_");
                    amount_ = request.getParameterValues("amount_"); 
                    warehouseCode_ = request.getParameterValues("warehouseCode_");
                 
                 }                    
	         String addBtn = request.getParameter("addBtn");
	         String updateBtn = request.getParameter("updateBtn");
	         String updateBtn2 = request.getParameter("updateBtn2"); //salesOrderDetail2.jsp
	         String itemAddBtn = request.getParameter("itemAddBtn");
	         String itemUpdateBtn = request.getParameter("itemUpdateBtn");
	         String isAutoIdGen = "";
                 
			String companyCode = request.getParameter("companyCode");	 
	         com.magbel.ia.vao.User user = null;
		
	    try{
	             if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
	             int userId = Integer.parseInt(user.getUserId());//to be completed
                     
	             if(addBtn != null){
                        isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                      if(isAutoIdGen.trim().equalsIgnoreCase("N"))
                      { //test 4 auto ID setup                                               
                          if(orderCode.equals("")||orderCode == null){
	                       out.print("<script>alert('Order no. cannot be empty')</script>");
	                       out.print("<script>history.go(-1);</script>");
	                   }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(SORDER_CODE) FROM IA_SALES_ORDER WHERE SORDER_CODE='"+orderCode+"'"))
                          { 
                              out.print("<script>alert('Order Code Already Exist')</script>");
                              out.print("<script>history.go(-1);</script>");
                          }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_SO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND SORDER_CODE='"+orderCode+"'"))
                          {
                                  out.print("<script>alert('Item Already Exist')</script>");
                                  out.print("<script>history.go(-1);</script>");
                           }
                         else if(approveOfficer==-1)
                         {
                              out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                              out.print("<script>window.location='DocumentHelp.jsp?np=salesOrder'</script>");
                         }   
                          else
                          { 
                          
                          if(serviceBus.createSalesOrder(orderCode,customerCode,po,reqPersId,shipDate,freight,carrier,desc,userId,approveOfficer,projectCode,companyCode)&&
                               serviceBus.createSalesOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,warehouseCode,companyCode))
                               if(approveOfficer > 0){
                                   superv.createTransactionApproval(orderCode,"SALES","SALES ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);    
                               }
                               
                               out.print("<script>alert('Record Succesfully Saved.')</script>");
                               out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDetail&orderCode="+orderCode+"'</script>");
                               
                           }  
                         }
                        else{
                             if(serviceBus.isRecordExisting("SELECT COUNT(SORDER_CODE) FROM IA_SALES_ORDER WHERE SORDER_CODE='"+orderCode+"'")){ 
	                            out.print("<script>alert('Order Code Already Exist')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                     }
	                     else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_SO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND SORDER_CODE='"+orderCode+"'")){
	                            out.print("<script>alert('Item Already Exist')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                            
	                     }
                             else if(approveOfficer==-1){
                                     out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                                     out.print("<script>window.location='DocumentHelp.jsp?np=salesOrder'</script>");
                             }
	                     else{ 
	                        if(serviceBus.createSalesOrder(orderCode,customerCode,po,reqPersId,shipDate, freight, carrier,desc,userId,approveOfficer,projectCode,companyCode)&&
	                            serviceBus.createSalesOrderItemDetail(serviceBus.auotoGenCode,quantity,unitPrice,advancePymt,itemCode,warehouseCode,companyCode))
	                        {
	                        	serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
	                        	if(approveOfficer > 0)
	                        	  {
                                    superv.createTransactionApproval(serviceBus.auotoGenCode,"SALES","SALES ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);
                                  }
                                 out.print("<script>alert('Record Succesfully Saved.')</script>");
	                         out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDetail&orderCode="+serviceBus.auotoGenCode+"'</script>");
	                        }
	                        }
                            }  
                         }
                       
	            if(updateBtn != null)
	            {
                  if(serviceBus.updateSalesOrder(po,freight,carrier,customerCode,orderCode,shipDate,reqPersId,approveOfficer,projectCode))
                  {
	              if(!warehouseCode.equals("") && !itemCode.equals("") && quantity > 0 && unitPrice > 0)
	               { 
                    if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_SO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND SORDER_CODE='"+orderCode+"'"))
                           {
	                           out.print("<script>alert('Item Already Exist')</script>");
	                           out.print("<script>history.go(-1);</script>");
	                       }
	                       else{
                                  serviceBus.createSalesOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,warehouseCode,companyCode);
                                  System.out.println("---------------------------------------------");
                                  serviceBus.updateInventoryTotals(quantity, warehouseCode, itemCode);
                                    if(approveOfficer > 0)
                                    {
                                        superv.createTransactionApproval(orderCode,"SALES","SALES ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);  
                                    }
                                }
	                            
	                 }
	               }
	                      out.print("<script>alert('Record Succesfully Saved.')</script>");
	                      out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDetail&orderCode="+orderCode+"'</script>");
	                     	                  	                  
	              }
                       
	             if(updateBtn2 != null){
	                     if(serviceBus.updateSalesOrder(po,freight,carrier,customerCode,orderCode,shipDate,reqPersId,approveOfficer,projectCode)){
                               if(!warehouseCode.equals("") && !itemCode.equals("") && quantity > 0 && unitPrice > 0){ 
                                   serviceBus.updateSalesOrderItemDetail(id,orderCode,quantity,unitPrice,warehouseCode,itemCode);        
	                         }
                                out.print("<script>alert('Record Succesfully Saved.')</script>");
                                out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDetail2&orderCode="+orderCode+"&itemCode="+itemCode+"&warehouseCode="+warehouseCode+"'</script>");
                                      
                            }
	                                                             
	                }   
                    
                    if(itemAddBtn != null){
	                 if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_SO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND WAREHOUSE_CODE='"+warehouseCode+"' AND SORDER_CODE='"+orderCode+"'")){
	                     out.print("<script>alert('Item Already Exist')</script>");
	                     out.print("<script>history.go(-1);</script>");
	                 }
	                 else if(approveOfficer==-1){
	                      out.print("<script>alert('Approving Officer cannot be empty.')</script>");
	                      out.print("<script>window.location='DocumentHelp.jsp?np=salesOrder'</script>");
                        }
                         else{
	                     if(serviceBus.createSalesOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,warehouseCode,companyCode))
	                      if(approveOfficer > 0){
                               superv.createTransactionApproval(orderCode,"SALES","SALES ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);
                              }
                              out.print("<script>self.close();</script>");
                              
	                 }
	             }
	            if(itemUpdateBtn != null){
	                      serviceBus.updateSalesOrderItemDetail(id,orderCode,quantity,unitPrice,warehouseCode,itemCode);
	                      out.print("<script>self.close();</script>");
	                      
	                  }
	                        
	           if(SODeliveryAddBtn != null){
	                       
	                       /*if(batchCode.equals("")||batchCode == null){
	                            out.print("<script>alert('Delivery no. cannot be empty')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                        }
	                        else{
	                        if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM IA_SO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"'")){
	                            out.print("<script>alert('Delivery no. Already Exist')</script>");
	                            out.print("<script>history.go(-1);</script>");
	                        }*/
	                        //else{
                                    batchCode = cg.generateCode("SALES ORDER DELIVERY","","","");
	                            serviceBus.createSODeliveryItem(orderCode,batchCode,code_,itemCode_,quantity_,quantDeliver_,userId,unitPrice_,amount_,warehouseCode_);
	                            out.print("<script>alert('Record Succesfully Saved.')</script>");
                                    out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&customerCode="+customerCode+"'</script>");
                                            
                          }
	                 if(SODeliveryUpdateBtn != null){
	                                
	                                serviceBus.updateSODeliveryItem(orderCode,batchCode,code_,quantity_,quantDeliver_);
	                                  out.print("<script>alert('Record Succesfully Saved.')</script>");
	                                  out.print("<script>window.document.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&customerCode="+customerCode+"'</script>");
                                         	                                  
	                              }
	             if(SODeliveryPostBtn != null){
	                              if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM IA_SO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"' AND POSTED = 'Y'")){
	                                  out.print("<script>alert('Posting already done on Sales')</script>");
	                                  out.print("<script>history.go(-1);</script>");
	                              }
	                              else{
	                                  if(serviceBus.postShipment2InventoryHistory(orderCode,batchCode,companyCode,quantDeliver_,"")){
                                          if(serviceBus.postShipment2InventoryTotals(orderCode,batchCode,""))
                                          {
	                                   out.print("<script>alert('Posting successfully done.')</script>");
	                                   out.print("<script>window.location='DocumentHelp.jsp?np=salesOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&customerCode="+customerCode+"'</script>");
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
