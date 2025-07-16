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
import com.magbel.ia.util.MailSender;
import com.magbel.legend.bus.ApprovalRecords;

public class ReturnedAcceptanceServlet extends HttpServlet {

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
		
		String realPath=getServletConfig().getServletContext().getRealPath("/mailConfig/legend.properties");
		System.out.println("realPath >>>>>>>>>>> " + realPath);
		
		FileInputStream fis =  new FileInputStream(realPath);
		
		String compCode =  request.getParameter("companyCode");	 
		String id = request.getParameter("ID");
		String dstrbCode = request.getParameter("dstrbCode");
        String reqnDesc = request.getParameter("reqnDesc").toUpperCase();
        String projDesc = request.getParameter("projDesc").toUpperCase();
		String status = "";//request.getParameter("");
		String customerCode = request.getParameter("customerCode");
		String warehouseCode = request.getParameter("warehouse")==null? "" : request.getParameter("warehouse");
		String po = "";//request.getParameter("po");
		String user_Id =  request.getParameter("userId");  
        String projectCode = request.getParameter("projectCode");
	     String userID = request.getParameter("userId");	
	     String reqnID = request.getParameter("reqnID");
	     String itemCD = request.getParameter("itemCD");
	     String distQty= request.getParameter("QtyDist");
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

	     int QtyDist= (distQty!=null)?Integer.parseInt(distQty):0;
	     String reqQty= request.getParameter("no_of_items");
	     int QtyReq= (reqQty!=null)?Integer.parseInt(reqQty):0;
	     boolean done = false;
	         String [] stockId = null;
	         String recStockId =  "";
	         String itemCode = "";
	         String stockReturned = request.getParameter("stockReturned");
	         String supervisorName ="";
	         java.util.ArrayList stockrec =distributionBus.getStockDetailrecords(dstrbCode);
	/*	     for(int i=0;i<stockrec.size();i++)
		     {			
		    	 if(i>0) recStockId = recStockId+";";
		    	 com.magbel.legend.vao.newAssetTransaction  newrectrans = (com.magbel.legend.vao.newAssetTransaction)stockrec.get(i);
					recStockId =  newrectrans.getIntegrifyId();
					itemCode = newrectrans.getDescription();
		     }
	*/
	         String strstockTotalNo = aprecords.getCodeName("SELECT COUNT(*) FROM ST_DISTRIBUTION_ITEM WHERE DORDER_CODE = '"+dstrbCode+"'");
	         int stockTotalNo=(strstockTotalNo!=null && !strstockTotalNo.equals(""))?Integer.parseInt(strstockTotalNo):0;
	//         System.out.println("<<<<<<=======stockTotalNo: "+stockTotalNo);

			 stockId = stockReturned.split(";");
           
	         String addBtn = request.getParameter("addBtn");
	         
	         String rejectBtn = request.getParameter("rejectBtn");
             
			String companyCode = request.getParameter("companyCode");	 
			String assetId = request.getParameter("Asset_Id");	
			String supervisorID = userID;
			legend.admin.objects.User user = null;
			String supervisor = aprecords.getCodeName("SELECT SUPERVISOR FROM am_ad_Requisition WHERE ReqnID = '"+reqnID+"'");
			String userName = aprecords.getCodeName("SELECT Full_name FROM am_gb_user WHERE user_ID = '"+userID+"'");
			String supervisorNameQry = (new StringBuilder()).append("select Full_name,email,approval_limit from am_gb_user where user_ID ='").append(supervisor).append("'").toString();
			String emailAcceptMSg="Materials Issued with Distribution Order No. "+dstrbCode+" and Stock ID " + reqnID + " has been accepted for Return by "+userName;
			String emailRejectMSg="Materials Issued with Distribution Order No. "+dstrbCode+" and Stock ID " + reqnID + " has been rejected by "+userName;
	    try{
	             if(addBtn.equalsIgnoreCase("Accept"))
	             {
	            	 status = addBtn;      
	//            	 System.out.println("<<<<<<<<<stockTotalNo: "+stockTotalNo);
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
		         			String[] sprvResult=(applHelper.retrieveArray(supervisorNameQry)).split(":");
		       			 	supervisorName = sprvResult[0];
			       			if ((sprvResult[1] != null)&&(sprvResult[1] != ""))
			       			{
			       				mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID,emailAcceptMSg);
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
			       				mailSender.sendMailToSupervisor(fis,sprvResult[1],reqnID,emailRejectMSg);
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
