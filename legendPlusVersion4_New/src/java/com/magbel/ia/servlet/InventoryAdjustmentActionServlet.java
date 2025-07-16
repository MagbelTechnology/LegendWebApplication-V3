
 package com.magbel.ia.servlet;

import com.magbel.ia.bus.InventoryAdjustmtServiceBus;

import java.io.*;
 import javax.servlet.*;
 import javax.servlet.http.*;

//import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.bus.InventoryServiceBus;
import com.magbel.ia.bus.SalesOrderServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;


public class InventoryAdjustmentActionServlet extends HttpServlet {

   /** Initializes the servlet.
    */
    private InventoryAdjustmtServiceBus serviceBus;
    private SupervisorServiceBus superv;
    private SalesOrderServiceBus sos;

   public void init(ServletConfig config) throws ServletException {
     super.init(config);
     serviceBus = new InventoryAdjustmtServiceBus();
     superv = new SupervisorServiceBus();
     sos = new SalesOrderServiceBus();
   }

   /** Destroys the servlet.
    */
   public void destroy() {

  }

  /**
  *
  */

  public void service(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {

  	response.setContentType("text/html");

      HttpSession session = request.getSession();
      PrintWriter out = response.getWriter();

      String id = request.getParameter("ID");
      String itemNo = request.getParameter("itemCode");
      //String date = request.getParameter("transDate");
      String adjustOpt = request.getParameter("adjustOpt");
      
  	//int period = (strPeriod != null) ? Integer.parseInt(strPeriod) : 0;
      String warehouse = request.getParameter("wareHouseCode");
  	String description = request.getParameter("desc");
  	String strQuantity = request.getParameter("quantity");
  	int quantity=((strQuantity!=null)&&(!strQuantity.equals("")))?Integer.parseInt(strQuantity):0;
        String posted = "Y";//request.getParameter("posted");
         String strApproveOfficer = request.getParameter("approveOfficer");
        int approveOfficer=(strApproveOfficer!=null)?Integer.parseInt(strApproveOfficer):0;
        String strApproveStatus = request.getParameter("approveStatus");
        String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U"; 
        
      String addAdjustmtBtn = request.getParameter("addAdjustmtBtn");
      String updateAdjustmtBtn = request.getParameter("updateAdjustmtBtn");
	  String companyCode = request.getParameter("companyCode");
              
      legend.admin.objects.User user = null;
            
      try{
    	  String userid = (String)session.getAttribute("CurrentUser");
        //  if(session.getAttribute("CurrentUser")!=null){user =(legend.admin.objects.User)session.getAttribute("CurrentUser");}
 //         int userId = Integer.parseInt(user.getUserId());//to be completed
          int userId = Integer.parseInt(userid);
           
          if(addAdjustmtBtn != null){
                if(approveOfficer==-1){
                    out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=inventoryAdjustment'</script>");
                  }
                else{  
                   if(serviceBus.createInventoryAdjustment(itemNo,warehouse,description,quantity,userId,posted,adjustOpt,companyCode))
                      if(approveOfficer > 0){
                      superv.createTransactionApproval(serviceBus.id,"ADJUST","ADJUSTMENT",userId,approveOfficer,approveStatus,itemNo,quantity);
                      }
                      else{//post to inventory history and totals
                       strQuantity = adjustOpt.equals("REMOVE") ? "-"+strQuantity : strQuantity;
                       quantity = Integer.parseInt(strQuantity);
                       serviceBus.postAdjustment2InventoryHistory(itemNo,"ADJUSTMENT",quantity,warehouse,userId);
                       /*if(serviceBus.postAdjustment2InventoryHistory(itemNo,"ADJUSTMENT",quantity,warehouse,userId)){
                          serviceBus.postAdjustment2InventoryTotals(itemNo,quantity,warehouse,userId);                       
                       }*/
                      }
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=inventoryAdjustment'</script>");
              }      
                                
            }
            if(updateAdjustmtBtn != null){
                 if(serviceBus.updateInventoryAdjustment(id,itemNo,warehouse,description,quantity,posted)){
                  out.print("<script>alert('Record successfully saved.')</script>");
                  out.print("<script>window.location='DocumentHelp.jsp?np=inventoryAdjustmentDetail&ID="+id+"'</script>");
                 }
                //response.sendRedirect("DocumentHelp.jsp?np=inventoryAdjustmentDetail&ID="+id);
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
      return "Inventory Adjustment Action Servlet";
    }

  }
