

package com.magbel.ia.servlet;

import java.io.*;

import javax.servlet.*;

import java.util.ArrayList;

import com.magbel.ia.bus.AccountInterfaceServiceBus;
import com.magbel.ia.bus.SupervisorServiceBus;
import com.magbel.ia.vao.Imprest;
import com.magbel.ia.vao.ImprestRef;
import com.magbel.ia.vao.User;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class PayableImprestActionServlet extends HttpServlet
{

    private AccountInterfaceServiceBus serviceBus;
    private SupervisorServiceBus superv;
    public PayableImprestActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new AccountInterfaceServiceBus();
        superv = new SupervisorServiceBus();
    }

    public void destroy()
    {
    }

public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
{ 
        response.setContentType("text/html");
        String mtid = "";
        HttpSession session = request.getSession();
        java.io.PrintWriter out = response.getWriter();
       // User loginId = (User)session.getAttribute("CurrentUser");
        String id = request.getParameter("id");
        String refNumber = request.getParameter("refno");
		String refno = request.getParameter("refno");
		String ItemId = request.getParameter("ItemId");
        String beneficiary = request.getParameter("beneficiary");
        String impAccNumber = request.getParameter("impaccount");
        String benAccNumber = request.getParameter("benacc");
        String purpose = request.getParameter("purpose");
        String expiryDate = request.getParameter("expirydate");
        String isRetired = request.getParameter("");
        String userId = "";//loginId.getUserId();
        String supervisorId = request.getParameter("supervisor");
        String transDate = request.getParameter("transdate");
        String effDate = request.getParameter("effdate");
        String isCash = request.getParameter("isCash");
        String strAmount = request.getParameter("amount");
        String strdescription = request.getParameter("purpose");
        strAmount = strAmount.replaceAll(",","");
        double amount = strAmount == null ? 0.0D : Double.parseDouble(strAmount);
        String isPosted = request.getParameter("isposted");
        String companyCode = request.getParameter("companyCode");
 //       String branchCode = request.getParameter("branchCode");
        String strApproveOfficer = request.getParameter("approveOfficer");
        int approveOfficer=(strApproveOfficer!=null)?Integer.parseInt(strApproveOfficer):0;
        String strApproveStatus = request.getParameter("approveStatus");
        
        String approveStatus = ((strApproveStatus != null)&&(!strApproveStatus.equals(""))) ? strApproveStatus : "U";
          //Print the parameters    out.print("id");
        String cmdRetire = request.getParameter("btnRetire");
//        System.out.println("ID: "+id+"   ItemId: "+ItemId+"  refno: "+refno+"  cmdRetire: "+cmdRetire+"   beneficiary: "+beneficiary);
        int crCurrCode = 0;
        String crAcctNo = request.getParameter("TuitionAcct");
        String drAccount = request.getParameter("drAccount");
        String custaccount = request.getParameter("custaccount");
        String crAcctType = "IM";
        String drAcctType = "IM";
        double drAcctExchRate = 1;
        String strDrSysExchRate = "1";
        double drSysExchRate = Double.parseDouble(strDrSysExchRate);
        String crTranCode= "117";
        String drTranCode= "157";
        String intCrCurrCode = request.getParameter("crCurrCode");
        String crSBU = request.getParameter("crSBU");
        String drSBU = request.getParameter("drSBU");
        String chequeNo = request.getParameter("chqno");
        String bank = request.getParameter("Bank");
        String ledgerNo = request.getParameter("ledgerNo");
        String orderNo = request.getParameter("orderNo");
//        System.out.println("isCash: "+isCash+"     chequeNo: "+chequeNo+"    bank: "+bank+"   ledgerNo: "+ledgerNo);
        String drExchRate = "1";
        String crExchRate = "1";
        int drCurrCode = 0;
        double crAcctExchRate = 0.00;
        String strCrSysExchRate = "1";
       // double crSysExchRate = Double.parseDouble(strCrSysExchRate);
        double crSysExchRate = 0.00;
        java.util.ArrayList list =serviceBus.getPayableImprestItemsRecord(orderNo);
        if(isPosted == null)
            isPosted = "N";
        
        String btnSave = request.getParameter("btnSave");
		
//        System.out.println("btnSave: "+btnSave+"     cmdRetire: "+cmdRetire+"    approveOfficer: "+approveOfficer+"   findRefNumber: "+serviceBus.findRefNumber(refno));
		String beneficiarySend = serviceBus.getCodeName("SELECT ACCOUNT_NAME FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE CUSTOMER_CLASS='STAFF' AND ACCOUNT_No='"+benAccNumber+"'");
		
		String benacc = request.getParameter("benacc");
	
		String  referenceNo = serviceBus.getCodeName("SELECT REF_NUMBER FROM IA_PAYABLE_IMPRESTS WHERE REF_NUMBER='"+refNumber+"'");
		String  actualAmount = serviceBus.getCodeName("SELECT AMOUNT FROM IA_PAYABLE_IMPRESTS WHERE REF_NUMBER='"+refNumber+"'");
		String  sumAmount = serviceBus.getCodeName("SELECT SUM(AMOUNT) FROM IA_PAYABLE_IMPREST_ITEMS GROUP BY REF_NUMBER HAVING REF_NUMBER IN(SELECT REF_NUMBER FROM IA_PAYABLE_IMPRESTS WHERE REF_NUMBER='"+refNumber+"')");
        sumAmount = sumAmount.replace(",","");
		actualAmount = actualAmount.replace(",","");
        double  actualAmountNo = ((actualAmount != null)&&(!actualAmount.equals(""))) ? Double.parseDouble(actualAmount) : 0;
		double  sumAmountNo = ((sumAmount != null)&&(!sumAmount.equals(""))) ? Double.parseDouble(sumAmount) : 0;
        com.magbel.ia.vao.User user = null;
		com.magbel.ia.vao.Imprest imp = null;
        double checkImprest = actualAmountNo - sumAmountNo;
 //    	System.out.println("actualAmountNo: "+actualAmountNo+"   sumAmountNo: "+sumAmountNo+"  checkImprest: "+checkImprest);
    try
    {
	
		if(session.getAttribute("CurrentUser")!=null)
		{
			user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");
		}
			userId = user.getUserId();//to be completed
			int userId_ = Integer.parseInt(user.getUserId());
			int branchNo = Integer.parseInt(user.getBranch());
			String branchCode = serviceBus.getCodeName("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE branch_id = "+branchNo+"");
			
        if(id == null)
        {
	        if(btnSave!=null)
	        {
                if(approveOfficer==-1)
			    {
                  out.print("<script>alert('Approving Officer cannot be empty.')</script>");
                  out.print("<script>window.location='DocumentHelp.jsp?np=newImprest'</script>");
                }
                else
			    { 
			        if(serviceBus.findRefNumber(refno)!=null)
				    {
				    out.println("<script>");
				    out.println("alert('Ref Number Cannot be Empty!')");
				    out.println("</script>");
				    out.print("<script>window.location='DocumentHelp.jsp?np=newImprest'</script>");
				    }
					else if (checkImprest > 0)
                      {
                      out.println("<script>");
				      out.println("alert('Beneficiary Has An Imprest To Retired...')");
				      out.println("</script>");
				      out.print("<script>window.location='DocumentHelp.jsp?np=newImprest'</script>");
				      }
			        else
                    {	 
//                		System.out.println("<<<<<<<<About to Retire the Payment>>>>>>>>");	 		  
//			           if(serviceBus.createImprest(refNumber, beneficiarySend, impAccNumber, benAccNumber, purpose, expiryDate, "N", userId, supervisorId, transDate, effDate, isCash, amount, isPosted,companyCode,branchNo,ledgerNo))
			        	   if(approveOfficer > 0)
			              {
				                 superv.createTransactionApproval(refNumber,"IMPREST","IMPREST ACCOUNT",userId_,approveOfficer,approveStatus,"0000",0);
				          }
				    
				     out.print("<script>alert('Record successfully saved.')</script>");
					 out.print("<script>window.location='DocumentHelp.jsp?np=PayableimprestRecords'</script>");
					 }
			    }                
	        }
        } 
        else
        {
        	String imprestId = "";
        	String imprestAccNumber = "";
        	String description = "";
        	String expglaccount = "";
        	String otheracct = "";
        	double imprestamount = 0.0D;
            if(cmdRetire != null){
            	if (checkImprest > 0){
            		// Imprest balnce amount is not 0
            	}
            	else{
            	serviceBus.PayableretireImprest(id, amount);
            	}
                if(isPosted.equalsIgnoreCase("Y")){
            	    for(int k=0; k < list.size(); k++){  
 //           	    	System.out.println("Record Size>>>>>> "+list.size());
            	    	com.magbel.ia.vao.ImprestItems  imprestrecord = (com.magbel.ia.vao.ImprestItems)list.get(k);
            	    	imprestId = imprestrecord.getMtId();
            	    	imprestAccNumber = imprestrecord.getGlAccount();
            	    	imprestamount = imprestrecord.getAmount();
            	    	imprestId = imprestrecord.getMtId();
            	    	//orderNo = imprestrecord.getOrderNo();
            	    	description = imprestrecord.getDescription();
            	    	expglaccount = imprestrecord.getExpglaccount();
            	       	serviceBus.PayableupdateImprestItemforPosting(orderNo);
            	    }
                }
                else{ 
            	    for(int k=0; k < list.size(); k++){  
 //           	    	System.out.println("Record Size>>>>>> "+list.size());
            	    	com.magbel.ia.vao.ImprestItems  imprestrecord = (com.magbel.ia.vao.ImprestItems)list.get(k);
            	    	imprestId = imprestrecord.getMtId();
            	    	imprestAccNumber = imprestrecord.getGlAccount();
            	    	imprestamount = imprestrecord.getAmount();
            	    	description = imprestrecord.getDescription();
            	    	expglaccount = imprestrecord.getExpglaccount();
            	    	otheracct = imprestrecord.getOtheracct(); 
            	    //	orderNo = imprestrecord.getOrderNo();
  //          	    	System.out.println("isPosted: "+isPosted+"   imprestId: "+imprestId+"   imprestamount: "+imprestamount);
	                   // serviceBus.PayableretireImprest(isPosted, imprestId, imprestamount);
		           		serviceBus.PayableupdateImprestItemforPosting(orderNo);
            	    }
                }
            }
            mtid = id;
           // serviceBus.updateImprestItemforPosting(imprestId);
		     out.print("<script>alert('Record successfully saved.')</script>");
		     response.sendRedirect((new StringBuilder("DocumentHelp.jsp?np=PayableRetireDetails&id=")).append(mtid).toString());		
		     
        }
        
    }
    catch(NullPointerException e)
	{
        response.sendRedirect("sessionTimedOut.jsp");
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}

    public String getServletInfo()
    {
        return "Imprest Action Servlet";
    }
}
