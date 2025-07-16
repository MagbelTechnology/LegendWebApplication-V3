package com.magbel.ia.servlet;


import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

//import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.bus.InventoryServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;


public class WareHouseActionServlet extends HttpServlet {

  /** Initializes the servlet.
   */
   private InventoryServiceBus serviceBus;
   private SupervisorServiceBus superv;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    serviceBus = new InventoryServiceBus();
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
    String id = request.getParameter("ID");
    String warehouseCode = request.getParameter("warehouseCode");
    String name = request.getParameter("wareHouseName");
    String address = request.getParameter("address");
    String branchCode = request.getParameter("branchCode");
    String status = request.getParameter("status");
    String strApproveOfficer = request.getParameter("approveOfficer");
    int approveOfficer=(strApproveOfficer!=null)?Integer.parseInt(strApproveOfficer):0;
    String strApproveStatus = request.getParameter("approveStatus");
    String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U";
    String itemCode = request.getParameter("itemCode");
    String quantity = request.getParameter("quantity");
    int quantity_ = ((quantity!=null)&&(!quantity.equals("")))?Integer.parseInt(quantity):0;
    String fromWarehouse = request.getParameter("fromWarehouse");
    String toWarehouse = request.getParameter("toWarehouse");
    String fromBranch = request.getParameter("fromBranch");
    String toBranch = request.getParameter("toBranch");
      
    String addWarehouseBtn = request.getParameter("addWarehouseBtn");
    String updateWarehouseBtn = request.getParameter("updateWarehouseBtn");
    String addWarehouseTransBtn = request.getParameter("addWarehouseTransBtn");
	String companyCode = request.getParameter("companyCode");
//    com.magbel.ia.vao.User user = null;
    String isAutoIdGen = "";
    
	String fromWareHouseName = serviceBus.getCodeName("select distinct(b.name) from ST_INVENTORY_TOTALS a,ST_WAREHOUSE b where a.warehouse_code=b.warehouse_code and a.balance > 0 and a.warehouse_code='"+fromWarehouse+"'");
	String toWareHouseName = serviceBus.getCodeName("SELECT NAME FROM ST_WAREHOUSE where WAREHOUSE_CODE='"+toWarehouse+"'");
	String fromBranchName = serviceBus.getCodeName("SELECT BRANCH_NAME FROM am_ad_branch WHERE BRANCH_STATUS='ACTIVE' and BRANCH_CODE='"+fromBranch+"'"); 
	String toBranchName = serviceBus.getCodeName("SELECT BRANCH_NAME FROM am_ad_branch WHERE BRANCH_STATUS='ACTIVE' and BRANCH_CODE='"+toBranch+"'"); 
	
      try{
   	   legend.admin.objects.User  user = (legend.admin.objects.User)session.getAttribute("_user");
	   	 int userId = Integer.parseInt(user.getUserId());
 //         if(session.getAttribute("CurrentUser")!=null){user =(legend.admin.objects.User)session.getAttribute("CurrentUser");}
//          int userId = Integer.parseInt(user.getUserId());//to be completed
           
          if(addWarehouseBtn != null){
             isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
               if(isAutoIdGen.trim().equalsIgnoreCase("N")){ //test 4 auto ID setup
                if(warehouseCode.equals("")||warehouseCode == null){
                     out.print("<script>alert('Warehouse Code. cannot be empty')</script>");
                     out.print("<script>history.go(-1);</script>");
                 }
                else if(serviceBus.isRecordExisting("SELECT COUNT(WAREHOUSE_CODE) FROM ST_WAREHOUSE WHERE WAREHOUSE_CODE='"+warehouseCode+"'")){ 
                    out.print("<script>alert('Warehouse Code Already Exist')</script>");
                    out.print("<script>history.go(-1);</script>");
                }
                else{
                    if(serviceBus.createWareHouse(warehouseCode,name,userId,address,branchCode,status,companyCode))
                        out.print("<script>alert('Record successfully saved.')</script>");                                
                       out.print("<script>window.document.location='DocumentHelp.jsp?np=wareHouseDetail&warehouseCode="+warehouseCode+"'</script>");

                   }
                }
                else{//isAutogen = Y
                       if(serviceBus.createWareHouse(warehouseCode,name,userId,address,branchCode,status,companyCode))
                           out.print("<script>alert('Record successfully saved.')</script>");                                
                          out.print("<script>window.document.location='DocumentHelp.jsp?np=wareHouseDetail&warehouseCode="+serviceBus.auotoGenCode+"'</script>");

                 }
           
           }
          else if(updateWarehouseBtn != null){
                serviceBus.updateWareHouse(warehouseCode,name,address,branchCode,status);
                out.print("<script>window.document.location='DocumentHelp.jsp?np=wareHouseDetail&warehouseCode="+warehouseCode+"'</script>");
               
            }
          else if(addWarehouseTransBtn != null){
              if(approveOfficer==-1){
                  out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                  out.print("<script>window.location='DocumentHelp.jsp?np=wareHouseTransfer'</script>");
                }
              else{
                  if(serviceBus.createWarehouseTransfer(itemCode,quantity_,fromWareHouseName,toWareHouseName,userId,fromBranchName,toBranchName,companyCode)){
                  if(approveOfficer > 0){
                     superv.createTransactionApproval(serviceBus.auotoGenCode,"TRANSFER","TRANSFER",userId,approveOfficer,approveStatus,itemCode,quantity_);
                 }
                 else{
                  //add transfer update inventory
                  serviceBus.processWarehouseTransfer(itemCode,"TRANSFER",quantity,fromWarehouse,toWarehouse,userId);
                  
                 }
                } 
                out.print("<script>alert('Record successfully transfered.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=warehouseTransferList'</script>"); 
              }
                    
          }
          else{
                
            }
      }
      catch(NullPointerException e){
           response.sendRedirect("sessionTimeOut.jsp");
        }
      catch(Exception e){
          e.printStackTrace();
      }

    

  }

  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Item Type Action Servlet";
  }

}
