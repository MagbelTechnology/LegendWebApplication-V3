

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

public class ImprestActionServlet extends HttpServlet
{

    private AccountInterfaceServiceBus serviceBus;
    private SupervisorServiceBus superv;
    public ImprestActionServlet()
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
//        System.out.println("cmdRetire: "+cmdRetire+"   beneficiary: "+beneficiary);
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
        String orderNo = request.getParameter("orderNo");
        String bank = request.getParameter("Bank");
        String ledgerNo = request.getParameter("ledgerNo");
//        System.out.println("isCash: "+isCash+"     chequeNo: "+chequeNo+"    bank: "+bank+"   ledgerNo: "+ledgerNo);
        String drExchRate = "1";
        String crExchRate = "1";
        int drCurrCode = 0;
       // String strCrAcctExchRate = request.getParameter("crExchRate");
       // double crAcctExchRate = Double.parseDouble(strCrAcctExchRate);
        double crAcctExchRate = 0.00;
        String strCrSysExchRate = "1";
       // double crSysExchRate = Double.parseDouble(strCrSysExchRate);
        double crSysExchRate = 0.00;
        java.util.ArrayList list =serviceBus.getImprestItemsRecord();
        if(isPosted == null)
            isPosted = "N";
        
        String btnSave = request.getParameter("btnSave");
		
//        System.out.println("btnSave: "+btnSave+"     cmdRetire: "+cmdRetire+"    approveOfficer: "+approveOfficer+"   findRefNumber: "+serviceBus.findRefNumber(refno));
		String beneficiarySend = serviceBus.getCodeName("SELECT ACCOUNT_NAME FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE CUSTOMER_CLASS='STAFF' AND ACCOUNT_No='"+benAccNumber+"'");
		
		String benacc = request.getParameter("benacc");
	
		String  referenceNo = serviceBus.getCodeName("SELECT REF_NUMBER FROM IA_IMPRESTS WHERE BEN_ACC_NUMBER='"+benacc+"'");
		String  actualAmount = serviceBus.getCodeName("SELECT AMOUNT FROM IA_IMPRESTS WHERE BEN_ACC_NUMBER='"+benacc+"'");
		String  sumAmount = serviceBus.getCodeName("SELECT SUM(AMOUNT) FROM IA_IMPREST_ITEMS GROUP BY REF_NUMBER HAVING REF_NUMBER IN(SELECT REF_NUMBER FROM IA_IMPRESTS WHERE BEN_ACC_NUMBER='"+benacc+"')");
        sumAmount = sumAmount.replace(",","");
		actualAmount = actualAmount.replace(",","");
    	String  balAmount = serviceBus.getCodeName("SELECT ACCOUNT_BALANCE FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE STATUS = 'ACTIVE' AND ACCOUNT_NO = '"+benAccNumber+"'");
    	balAmount = balAmount.replace(",","");
    	String custType = serviceBus.getCodeName("SELECT CUSTOMER_TYPE FROM IA_CUSTOMER_ACCOUNT_DISPLAY WHERE STATUS = 'ACTIVE' AND ACCOUNT_NO = '"+benAccNumber+"'");
		double  accountBalance = ((balAmount != null)&&(!balAmount.equals(""))) ? Double.parseDouble(balAmount) : 0;		
        double  actualAmountNo = ((actualAmount != null)&&(!actualAmount.equals(""))) ? Double.parseDouble(actualAmount) : 0;
		double  sumAmountNo = ((sumAmount != null)&&(!sumAmount.equals(""))) ? Double.parseDouble(sumAmount) : 0;
        com.magbel.ia.vao.User user = null;
		com.magbel.ia.vao.Imprest imp = null;
        double checkImprest = actualAmountNo - sumAmountNo;
     	
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
//			        	System.out.println("checkImprest: "+checkImprest+"  impAccNumber: "+impAccNumber+"   benAccNumber:"+benAccNumber);
                    	String refNum = serviceBus.createImprest(refNumber, beneficiarySend, impAccNumber, benAccNumber, purpose, expiryDate, "N", userId, supervisorId, transDate, effDate, isCash, amount, isPosted,companyCode,branchNo,ledgerNo,orderNo);		 		  
			           //if(serviceBus.createImprest(refNumber, beneficiarySend, impAccNumber, benAccNumber, purpose, expiryDate, "N", userId, supervisorId, transDate, effDate, isCash, amount, isPosted,companyCode,branchNo,ledgerNo))
//			        	   System.out.println("INSERTION DONE: ");
//                    	System.out.println("reference Number: "+refNum);
                    	if(refNum!=""){
			        	   serviceBus.updateDRBalances(ledgerNo,branchCode,amount);  // Dedit Cash OR Cheque Account
			          	serviceBus.createGLHistory(branchCode, drCurrCode, drAcctType, impAccNumber, drTranCode, drSBU, purpose, amount, refno, drAcctExchRate, drSysExchRate, branchCode, drCurrCode, drAcctType, ledgerNo, drTranCode, crSBU, purpose, amount, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", impAccNumber, refNum);
			           	   serviceBus.updateCRBalances(impAccNumber,branchCode,amount);  // Credit Imprest Account
				          	serviceBus.createGLHistory(branchCode, crCurrCode, crAcctType, benAccNumber, crTranCode, crSBU, purpose, amount, refno, drAcctExchRate, drSysExchRate, branchCode, crCurrCode, crAcctType, impAccNumber, crTranCode, crSBU, purpose, amount, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", ledgerNo, refNum);
			        	   serviceBus.updateStudentCRBalances(benAccNumber,branchNo,amount);
			           		serviceBus.createStudentHistory(refNumber, branchCode, crAcctType, benAccNumber, custType, strdescription, amount, drAcctExchRate, drSysExchRate, userId, companyCode, "", accountBalance, crTranCode, refno, "", refno, refno,impAccNumber);
			           		serviceBus.updateDRBalances(impAccNumber,branchCode,amount);  // Debit Imprest Account
			           		serviceBus.createGLHistory(branchCode, drCurrCode, drAcctType, impAccNumber, drTranCode, drSBU, purpose, amount, refno, drAcctExchRate, drSysExchRate, branchCode, drCurrCode, drAcctType, impAccNumber, drTranCode, drSBU, purpose, amount, drAcctExchRate, drSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", benAccNumber, refNum);
			//           		serviceBus.updateDRBalances(impAccNumber,branchCode,amount);  
			    //       		serviceBus.createGLHistory(branchCode, drCurrCode, drAcctType, impAccNumber, drTranCode, drSBU, strdescription, amount, refNumber, drAcctExchRate, drSysExchRate, branchCode, drCurrCode, drAcctType, impAccNumber, drTranCode, crSBU, strdescription, amount, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", benAccNumber, refNum);
                    	}
			        	   if(approveOfficer > 0)
			              {
				                 superv.createTransactionApproval(refNumber,"IMPREST","IMPREST ACCOUNT",userId_,approveOfficer,approveStatus,"0000",0);
				          }
						  
					 
//			        	   System.out.println("AFTER INSERTION DONE: ");
				    
				     out.print("<script>alert('Record successfully saved.')</script>");
				     //mtid = serviceBus.findImprestByRefNo(refNumber).getMtId();	
                     //response.sendRedirect((new StringBuilder("DocumentHelp.jsp?np=imprestDetails&id=")).append(mtid).toString());		 					 
     			     //response.sendRedirect("DocumentHelp.jsp?np=imprestRecords");   
					 out.print("<script>window.location='DocumentHelp.jsp?np=imprestRecords'</script>");
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
                if(isPosted.equalsIgnoreCase("Y")){
            	    for(int k=0; k < list.size(); k++){  
            	    	com.magbel.ia.vao.ImprestItems  imprestrecord = (com.magbel.ia.vao.ImprestItems)list.get(k);
 //           	    	System.out.println("Expense Account: "+imprestrecord.getExpglaccount());
            	    	imprestId = imprestrecord.getMtId();
            	    	imprestAccNumber = imprestrecord.getGlAccount();
            	    	imprestamount = imprestrecord.getAmount();
            	    	description = imprestrecord.getDescription();
            	    	expglaccount = imprestrecord.getExpglaccount();
 //           	    	System.out.println("imprestAccNumber1: "+imprestAccNumber+"  expglaccount: "+expglaccount);
			       	    serviceBus.updateStudentDRBalances(benAccNumber,branchNo,imprestamount);
			       	    serviceBus.createStudentHistory(referenceNo, branchCode, drAcctType, benAccNumber, custType, description, imprestamount, drAcctExchRate, drSysExchRate, userId, companyCode, "", accountBalance, drTranCode, refno, "", refno, refno,imprestAccNumber);
			          	serviceBus.updateCRBalances(expglaccount,branchCode,imprestamount);  
			          	serviceBus.createGLHistory(branchCode, crCurrCode, crAcctType, expglaccount, drTranCode, drSBU, description, imprestamount, imprestId, drAcctExchRate, drSysExchRate, branchCode, crCurrCode, crAcctType, expglaccount, crTranCode, crSBU, description, imprestamount, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", benAccNumber, refno);
			          	serviceBus.updateImprestItemforPosting(imprestId);
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
//            	    	System.out.println("imprestAccNumber2: "+imprestAccNumber+"  expglaccount: "+expglaccount+"    description: "+description+"    otheracct: "+otheracct);
 //           	    	System.out.println("INSIDE LOOP  WHEN ISPOSETD IS NO DONE: "+imprestId);                	
	                    serviceBus.retireImprest(isPosted, imprestId, imprestamount);
		        	    serviceBus.updateStudentDRBalances(benAccNumber,branchNo,imprestamount);
		        	    serviceBus.createStudentHistory(referenceNo, branchCode, drAcctType, benAccNumber, custType, description, imprestamount, drAcctExchRate, drSysExchRate, userId, companyCode, "", accountBalance, drTranCode, referenceNo, "", referenceNo, referenceNo,benAccNumber);
		           		serviceBus.updateCRBalances(expglaccount,branchCode,imprestamount); 
		           		serviceBus.createGLHistory(branchCode, crCurrCode, crAcctType, expglaccount, drTranCode, drSBU, description, imprestamount, imprestId, drAcctExchRate, drSysExchRate, branchCode, crCurrCode, crAcctType, expglaccount, crTranCode, crSBU, description, imprestamount, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode, "", benAccNumber, referenceNo);
		           		serviceBus.updateImprestItemforPosting(imprestId);
            	    }
                }
            }
            mtid = id;
           // serviceBus.updateImprestItemforPosting(imprestId);
		     out.print("<script>alert('Record successfully saved.')</script>");
		     response.sendRedirect((new StringBuilder("DocumentHelp.jsp?np=imprestDetails&id=")).append(mtid).toString());		
		     
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
