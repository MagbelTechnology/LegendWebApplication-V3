package com.magbel.ia.servlet;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.magbel.ia.bus.PurchaseOrderServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.util.CodeGenerator;

public class PurchaseOrderActionServlet extends HttpServlet {

	  /** Initializes the servlet.
	   */
	   private PurchaseOrderServiceBus serviceBus;
           private CodeGenerator cg;
	    private SupervisorServiceBus superv;

	  public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    serviceBus = new PurchaseOrderServiceBus();
            cg = new CodeGenerator();
	      superv = new SupervisorServiceBus();
	  }

	  /** Destroys the servlet.
	   */
	  public void destroy() {

	  }

	  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	   * @param request servlet request
	   * @param response servlet response
	   */
	  public void service(HttpServletRequest request, HttpServletResponse response) throws
	      ServletException, IOException {

		response.setContentType("text/html");

	    HttpSession session = request.getSession();
	    PrintWriter out = response.getWriter();

	      String id=request.getParameter("ID");
	      String orderCode=request.getParameter("orderCode");
	      orderCode = ((orderCode==null) || (orderCode.equals(""))) ? "": orderCode;
	      String desc="";//request.getParameter("desc");
              String vendorCode=request.getParameter("vendorCode");
              String vendorCode_ = request.getParameter("vendorCode_"); 
	      String receiptDate=request.getParameter("receiptDate");
	      //String transDate=request.getParameter("transDate");
	      String carrier=request.getParameter("carrierCode");
	      String freight=request.getParameter("freightCode");
	      String orderedBy="";//request.getParameter("orderBy");
              String advancePymtOpt = "P";//request.getParameter("advancePymtOpt")==null ? "P" : request.getParameter("advancePymtOpt");
	      String strTotalAdvancePymt = request.getParameter("totalAdvancePymt");
	      double totalAdvancePymt=((strTotalAdvancePymt!=null) && (!strTotalAdvancePymt.equals("")))?Double.parseDouble(strTotalAdvancePymt):0; 
	      String warehouse=request.getParameter("warehouseCode");
	      String strTotAmount = request.getParameter("totalAmount");
	      double totalAmount=((strTotAmount!=null)&&(!strTotAmount.equals("")))? Double.parseDouble(strTotAmount):0.0d; 
	      String strApproveOfficer = request.getParameter("approveOfficer");
              int approveOfficer=(strApproveOfficer!=null&&!strApproveOfficer.equals(""))?Integer.parseInt(strApproveOfficer.trim()):0;
	      String strApproveStatus = request.getParameter("approveStatus");
	      String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U";
	      String projectCode = request.getParameter("projectCode");             
              //detail
	      String strQuantity = request.getParameter("quantity");
	      int quantity=((strQuantity!=null) && (!strQuantity.equals("")))?Integer.parseInt(strQuantity):0; 
	      String itemCode=request.getParameter("itemCode");
	      String strUnitPrice = request.getParameter("unitPrice");
	      double unitPrice=((strUnitPrice!=null) && (!strUnitPrice.equals("")))?Double.parseDouble(strUnitPrice):0; 
	      String strAdvancePymt =  request.getParameter("advancePymt");
	      double advancePymt = (strAdvancePymt!=null && !strAdvancePymt.equals("")) ? Double.parseDouble(strAdvancePymt) : 0; 
	      String strAdvancePymtPerc =  request.getParameter("advancePymtPerc");
	      double advancePymtPerc = (strAdvancePymtPerc!=null && !strAdvancePymtPerc.equals("")) ? Double.parseDouble(strAdvancePymtPerc) : 0; 
	      String strDiscount =  request.getParameter("discountAmt");
	      double discount = (strDiscount!=null && !strDiscount.equals("")) ? Double.parseDouble(strDiscount) : 0; 
	      String strDiscountPerc =  request.getParameter("discountAmtPerc");
	      double discountPerc = (strDiscountPerc!=null && !strDiscountPerc.equals("")) ? Double.parseDouble(strDiscountPerc) : 0; 
	      
              String strQuantDeliver= request.getParameter("quantDeliver");
	      int quantDeliver=0;//(strQuantDeliver!=null && !strQuantDeliver.equals(""))?Integer.parseInt(strQuantDeliver):0;
	      String batchCode = request.getParameter("batchCode");
              
              //delivery
               String PODeliveryAddBtn = request.getParameter("PODeliveryAddBtn");
               String PODeliveryUpdateBtn = request.getParameter("PODeliveryUpdateBtn");
               String PODeliveryPostBtn = request.getParameter("PODeliveryPostBtn");
               
	      String[] code_ = null;
	      String[] itemCode_ = null;
	      String[] unitPrice_ = null;
	      String[] amount_ = null;
	      String[] quantity_ = null;
	      String[] quantDeliver_ = null;
	      String[] warehouseCode_ = null;
               
              if((PODeliveryAddBtn != null)||(PODeliveryUpdateBtn != null)||(PODeliveryPostBtn != null)){
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
	      String updateBtn2 = request.getParameter("updateBtn2");
	      String itemAddBtn = request.getParameter("itemAddBtn");
	      String itemUpdateBtn = request.getParameter("itemUpdateBtn");
	      //String PODeliveryItemUpdateBtn = request.getParameter("PODeliveryItemUpdateBtn");
                                      		
		
              String overrideDescription="";//request.getParameter("");
              String strQuantityRecieved = "";//request.getParameter("");
              int quantityReceived=0;//(strQuantityRecieved!=null)?Integer.parseInt(strQuantityRecieved):0; 
              String strQuantityBO = "";//request.getParameter("");
              int quantityBO=0;//(strQuantityRecieved!=null)?Integer.parseInt(strQuantityRecieved):0; 
              String completed="";//request.getParameter("");
              String printed= "";//request.getParameter("");
              String status="";//request.getParameter(""); 
               String isAutoIdGen = "";
            String companyCode = request.getParameter("companyCode");
            com.magbel.ia.vao.User user = null;   
   
            try{
                  if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
                  int userId = Integer.parseInt(user.getUserId());//to be completed
                  
	          if(addBtn != null){
                        isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                    if(isAutoIdGen.trim().equalsIgnoreCase("N")){ //test 4 auto ID setup     
                         if(orderCode.equals("")||orderCode == null){
                               out.print("<script>alert('Order no. cannot be empty')</script>");
                               out.print("<script>history.go(-1);</script>");
                           }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(PORDER_CODE) FROM IA_PURCHASE_ORDER WHERE PORDER_CODE='"+orderCode+"'")){ 
                              out.print("<script>alert('Order Code Already Exist')</script>");
                              out.print("<script>history.go(-1);</script>");
                          }
                          else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_PO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND PORDER_CODE='"+orderCode+"'")){
                                  out.print("<script>alert('Item Already Exist')</script>");
                                  out.print("<script>history.go(-1);</script>");
                              
                          }
                          else if(approveOfficer==-1){
                           out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                           out.print("<script>window.location='DocumentHelp.jsp?np=purchaseOrder'</script>");
                          }
                        else{
                          if(serviceBus.createPurchaseOrder(orderCode,vendorCode,totalAmount,receiptDate, 
	                                orderedBy,warehouse,carrier,freight,desc,userId,advancePymtOpt,projectCode,companyCode)&&
                          serviceBus.createPurchaseOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc,companyCode)){
                                if(approveOfficer > 0){
                                    superv.createTransactionApproval(orderCode,"PURCHASE","PURCHASE ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);    
                                }
                                out.print("<script>alert('Record Succesfully Saved.')</script>");
                                out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDetail&orderCode="+orderCode+"'</script>");
                            }
                          }
                        
                      }
                      
                    else{
                        if(serviceBus.isRecordExisting("SELECT COUNT(PORDER_CODE) FROM IA_PURCHASE_ORDER WHERE PORDER_CODE='"+orderCode+"'")){ 
                            out.print("<script>alert('Order Code Already Exist')</script>");
                            out.print("<script>history.go(-1);</script>");
                        }
                        else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_PO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND PORDER_CODE='"+orderCode+"'")){
                                out.print("<script>alert('Item Already Exist')</script>");
                                out.print("<script>history.go(-1);</script>");
                            
                        }
                        else if(approveOfficer==-1){
                         out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                         out.print("<script>window.location='DocumentHelp.jsp?np=purchaseOrder'</script>");
                        }
                        else{
                            
                        if(serviceBus.createPurchaseOrder(orderCode,vendorCode,totalAmount,receiptDate,orderedBy,warehouse,carrier,freight,desc,userId,advancePymtOpt,projectCode,companyCode))
                          if(serviceBus.createPurchaseOrderItemDetail(serviceBus.auotoGenCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc,companyCode)){
                              if(approveOfficer > 0){
                                superv.createTransactionApproval(serviceBus.auotoGenCode,"PURCHASE","PURCHASE ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);
                              }
                              out.print("<script>alert('Record Succesfully Saved.')</script>");
                              out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDetail&orderCode="+serviceBus.auotoGenCode+"'</script>");
                          }
                        } 
                    }
                  
                  }
	         if(updateBtn != null){
                  if(serviceBus.updatePurchaseOrder(orderCode,vendorCode,totalAmount,receiptDate, 
                                            orderedBy,warehouse,carrier,freight,desc,advancePymtOpt,projectCode)){
                   if(!itemCode.equals("") && quantity > 0 && unitPrice > 0){                         
                     if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_PO_ITEM WHERE ITEM_CODE = '"+itemCode+"' AND PORDER_CODE='"+orderCode+"'")){
                          out.print("<script>alert('Item Already Exist')</script>");
                          out.print("<script>history.go(-1);</script>");
                      }
                     else{
                          serviceBus.createPurchaseOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc,companyCode);
                            if(approveOfficer > 0){
                                superv.createTransactionApproval(orderCode,"PURCHASE","PURCHASE ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);  
                            }
                        }
                   }  
                  }
                     out.print("<script>alert('Record Succesfully Saved.')</script>");
                     out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDetail&orderCode="+orderCode+"'</script>");
                 }
                  if(updateBtn2 != null){
                           if(serviceBus.updatePurchaseOrder(orderCode,vendorCode,totalAmount,receiptDate, 
                                            orderedBy,warehouse,carrier,freight,desc,advancePymtOpt,projectCode)){
                            if(!itemCode.equals("") && quantity > 0 && unitPrice > 0){ 
                                  serviceBus.updatePurchaseOrderItemDetail(id,orderCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc);
                              }
                             out.print("<script>alert('Record Succesfully Saved.')</script>");
                             out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDetail2&orderCode="+orderCode+"&itemCode="+itemCode+"'</script>");
                                   
                         }                                                                  
                     }   
                 if(itemAddBtn != null){
	              if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_SO_ITEM WHERE ITEM_CODE = '"+itemCode+"'")){
	                  out.print("<script>alert('Item Already Exist')</script>");
	                  out.print("<script>history.go(-1);</script>");
	              }
	              else if(approveOfficer==-1){
	                   out.print("<script>alert('Approving Officer cannot be empty.')</script>");
	                   out.print("<script>window.location='DocumentHelp.jsp?np=purchaseOrder'</script>");
                      }
	              if(serviceBus.createPurchaseOrderItemDetail(orderCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc,companyCode))
	                  if(approveOfficer > 0){
	                   superv.createTransactionApproval(orderCode,"PURCHASE","PURCHASE ORDER",userId,approveOfficer,approveStatus,itemCode,quantity);
	                  }	              
                        out.print("<script>self.close();</script>");
	              
	          }
	         if(itemUpdateBtn != null){
	                   serviceBus.updatePurchaseOrderItemDetail(id,orderCode,quantity,unitPrice,advancePymt,itemCode,advancePymtPerc,discount,discountPerc);
	                   out.print("<script>self.close();</script>");
	                   
	               }
                            
                 if(PODeliveryAddBtn != null){
                            
                         /*    if(batchCode.equals("")||batchCode == null){
                                 out.print("<script>alert('Receipt no. cannot be empty')</script>");
                                 out.print("<script>history.go(-1);</script>");
                             }
                             else{
                             if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM IA_PO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"'")){
                                 out.print("<script>alert('Receipt no. Already Exist')</script>");
                                 out.print("<script>history.go(-1);</script>");
                             }
                             else{*/
                                 batchCode = cg.generateCode("PURCHASE ORDER DELIVERY","","","");
                                 serviceBus.createPODeliveryItem(orderCode,batchCode,code_,itemCode_,quantity_,quantDeliver_,userId,unitPrice_,amount_,companyCode);
                                 out.print("<script>alert('Record(s) Succesfully Saved.')</script>");
                                 out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&vendorCode="+vendorCode+"'</script>");
                                                                      
                            // }
                           //  }                        
                         }
                     if(PODeliveryUpdateBtn != null){
                                     
                                     serviceBus.updatePODeliveryItem(orderCode,batchCode,code_,quantity_,quantDeliver_);
                                      out.print("<script>alert('Record Succesfully Saved.')</script>");
                                      out.print("<script>window.document.location='DocumentHelp.jsp?np=purchaseOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&vendorCode="+vendorCode+"'</script>");
                                }
                                  
                      if(PODeliveryPostBtn != null){
                                   if(serviceBus.isRecordExisting("SELECT COUNT(BATCH_CODE) FROM IA_PO_DELIVERY_ITEM WHERE BATCH_CODE = '"+batchCode+"' AND POSTED = 'Y'")){
                                       out.print("<script>alert('Posting already done on Purchase')</script>");
                                       out.print("<script>history.go(-1);</script>");
                                   }
                                   else{
                                       if(serviceBus.postReceipt2InventoryHistory(orderCode,batchCode,"")){
                                       if(serviceBus.postReceipt2InventoryTotals(orderCode,batchCode,"")){
                                        out.print("<script>alert('Posting successfully done.')</script>");
                                        out.print("<script>window.location='DocumentHelp.jsp?np=purchaseOrderDeliveryDetail&batchCode="+batchCode+"&orderCode="+orderCode+"&vendorCode="+vendorCode+"'</script>");
                                            
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

	  /** Returns a short description of the servlet.
	   */
	  public String getServletInfo() {
	    return "Purchase Order Action Servlet";
	  }

	}
