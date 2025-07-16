package com.magbel.ia.servlet;

import java.io.FileInputStream;
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
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.CodeGenerator;
//import com.magbel.ia.util.MailSender;
//import com.magbel.legend.mail.MailSender;
import com.magbel.ia.util.MailSender;
import com.magbel.legend.bus.ApprovalRecords;

public class DistributionAcceptanceServlet extends HttpServlet {

	/**
	 * Initializes the servlet.
	 */
	private SalesOrderServiceBus serviceBus;
	private DistributionOrderServiceBus distributionBus;
        private CodeGenerator cg;
        private SupervisorServiceBus superv;
        private ApprovalRecords aprecords;
        ApplicationHelper2 applHelper = null;
        MailSender mailSender = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		serviceBus = new SalesOrderServiceBus();
	        cg = new CodeGenerator();
	        superv = new SupervisorServiceBus(); 
	        distributionBus = new DistributionOrderServiceBus();
	        aprecords = new ApprovalRecords();
	        applHelper = new ApplicationHelper2();
	        mailSender = new MailSender ();
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
		
//		String realPath=getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
		String realPath=getServletConfig().getServletContext().getRealPath("/mailConfig/Inventory.properties");
		System.out.println("realPath >>>>>>>>>>> " + realPath);
		
		FileInputStream fis =  new FileInputStream(realPath);
		
		String compCode =  request.getParameter("companyCode");	 
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
	     
	     if (approveOfficer==0)
	     {
	    	 strApproveStatus="A";
	     }
	     else
	     {
	    	 strApproveStatus="P";
	     }
	     String userID = request.getParameter("userId");	
	     String reqnID = request.getParameter("reqnID");
	     String itemCD = request.getParameter("itemCD");
	     String distQty= request.getParameter("QtyDist");
	     int QtyDist= (distQty!=null)?Integer.parseInt(distQty):0;
	     String reqQty= request.getParameter("QtyReq");
	     int QtyReq= (reqQty!=null)?Integer.parseInt(reqQty):0;
	     boolean done = false;
	     	 String strQuantityRequest = request.getParameter("QtyReq");
	     	int QuantityRequest=(strQuantityRequest!=null && !strQuantityRequest.equals(""))?Integer.parseInt(strQuantityRequest):0; 
	         String strQuantity = request.getParameter("quantity");
	         int quantity=(strQuantity!=null && !strQuantity.equals(""))?Integer.parseInt(strQuantity):0; 
	        // String itemCode = request.getParameter("itemCode")==null? "" : request.getParameter("itemCode");
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
	         String [] stockId = null;
	         String recStockId =  "";
	         String itemCode = "";
	         String supervisorName ="";
	         java.util.ArrayList stockrec =distributionBus.getStockDetailrecords(dstrbCode);
		     for(int i=0;i<stockrec.size();i++)
		     {			
		    	 if(i>0) recStockId = recStockId+";";
		    	 com.magbel.legend.vao.newAssetTransaction  newrectrans = (com.magbel.legend.vao.newAssetTransaction)stockrec.get(i);
					recStockId = recStockId+newrectrans.getIntegrifyId();
					itemCode = newrectrans.getDescription();
		     }
		     System.out.println("<<<<<<=======recStockId: "+recStockId);
	         String strstockTotalNo = aprecords.getCodeName("SELECT COUNT(*) FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE = '"+dstrbCode+"'");
	         int stockTotalNo=(strstockTotalNo!=null && !strstockTotalNo.equals(""))?Integer.parseInt(strstockTotalNo):0;
	//         System.out.println("<<<<<<=======stockTotalNo: "+stockTotalNo);
	         
         //    for(int k=0; k < stockTotalNo; k++)
         //    {
	    //     String infofld = aprecords.getCodeName("SELECT STOCK_CODE FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE = '"+dstrbCode+"'");
		//	 stockId = infofld.split(";");
             //}
			 stockId = recStockId.split(";");
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
	         
	         String rejectBtn = request.getParameter("rejectBtn");
             
			String companyCode = request.getParameter("companyCode");	 
			String assetId = request.getParameter("Asset_Id");	
			String supervisorID = userID;
			legend.admin.objects.User user = null;
			String supervisor = aprecords.getCodeName("SELECT SUPERVISOR FROM am_ad_Requisition WHERE ReqnID = '"+reqnID+"'");
			String userName = aprecords.getCodeName("SELECT Full_name FROM am_gb_user WHERE user_ID = '"+userID+"'");
			String supervisorNameQry = (new StringBuilder()).append("select Full_name,email,approval_limit from am_gb_user where user_ID ='").append(supervisor).append("'").toString();
			String emailAcceptMSg="Materials Issued with Distribution Order No. "+dstrbCode+" and Requisition ID " + reqnID + " has been accepted by "+userName;
			String emailRejectMSg="Materials Issued with Distribution Order No. "+dstrbCode+" and Requisition ID " + reqnID + " has been rejected by "+userName;
	    try{
	             if(addBtn.equalsIgnoreCase("ACCEPTED"))
	             {
	            	 status = addBtn;      
	            	 System.out.println("<<<<<<<<<stockTotalNo: "+stockTotalNo);
                     for(int j=0; j < stockTotalNo; j++)
                     { //System.out.println("<<<<<<<<<stockId Loop: "+stockId[j]);
                     done = distributionBus.updateForAcceptanceItem(dstrbCode,stockId[j],status,itemCode,warehouseCode);
                     }
                     if(done){
                    	 		distributionBus.updateForAcceptanceTotal(dstrbCode,status,itemCode,warehouseCode,QtyDist);
                               out.print("<script>alert('Record Succesfully Accepted.')</script>");
                               out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
                               		"AcceptanceList&dstrbCode="+dstrbCode+"'</script>");
                     		}
		         			supervisorID = request.getParameter("supervisor");
		         			System.out.println("<<<<<<<<<supervisorID: "+supervisorID);
		         			String[] sprvResult=(applHelper.retrieveArray(supervisorNameQry)).split(":");
		       			 	supervisorName = sprvResult[0];
		       			 	System.out.println("<<<<<<<<<supervisorName: "+supervisorName);
			       			if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
			       			{
			       				String mailaddress = sprvResult[1];
			       				System.out.println("<<<<<<<<<fis: "+fis+"   mailaddress: "+mailaddress+"   reqnID: "+reqnID+"    emailAcceptMSg: "+emailAcceptMSg);
			       				mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID,emailAcceptMSg);
			       				//mailSender.sendMailToSupervisor(fis, mailaddress, reqnID);
			       				//System.out.println("<<<<<<<<<After Mail: ");
			       			}
                     else{
                                out.print("<script>alert('Record Not Succesfully Accepted.')</script>");
                                out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
                                "AcceptanceList&dstrbCode="+dstrbCode+"'</script>");
                     		}
                         }
                       
	            if(rejectBtn.equalsIgnoreCase("Reject"))
	            {
	            	status =  rejectBtn;  
//	            	System.out.println("<<<<<<<<<STATUS: "+status);
	            	done = false;
                    for(int i=0; i < stockTotalNo; i++)
                    {
	            	done = distributionBus.updateForRejection(dstrbCode,stockId[i],status,itemCode,warehouseCode);
                    }
                    if(done){
                    	distributionBus.updateTotalForRejection(dstrbCode,status,itemCode,warehouseCode,QtyDist);
		         			supervisorID = request.getParameter("supervisor");
		         			String[] sprvResult=(applHelper.retrieveArray(supervisorNameQry)).split(":");
		       			 	supervisorName = sprvResult[0];
			       			if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
			       			{
			       				//mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID,emailRejectMSg);
			       				mailSender.sendMailToSupervisor(fis, sprvResult[1], reqnID);
			       			}                    	
	                      out.print("<script>alert('Record Succesfully Rejected.')</script>");
	                      out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
	                      		"AcceptanceList&dstrbCode="+dstrbCode+"'</script>");
                    	}
                    else{
	                      out.print("<script>alert('Record Not Succesfully Rejected.')</script>");
	                      out.print("<script>window.document.location='DocumentHelp.jsp?np=" +
	                      		"AcceptanceList&dstrbCode="+dstrbCode+"'</script>");
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
