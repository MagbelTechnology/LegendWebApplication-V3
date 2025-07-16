package com.magbel.ia.servlet;

import com.magbel.util.CheckIntegerityContraint;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import java.io.PrintWriter;

import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.CodeGenerator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import  com.magbel.ia.bus.CustomerAccountSetupHandler;
import   com.magbel.ia.vao.CustomerAccountSetup;
import   com.magbel.ia.vao.CustomerAccountDisplay;
import  com.magbel.ia.bus.CustomerAccountDisplayHandler;
import  com.magbel.ia.bus.CustomerHandler;
import   com.magbel.ia.vao.Customer;

import  java.text.DecimalFormat;

import com.magbel.ia.util.CurrencyUnFormatter;
import   com.magbel.ia.bus.QueryUtil;
import com.magbel.util.HtmlUtilily;



import audit.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CustomerAccountSetupAuditServlet extends HttpServlet {
private  ApplicationHelper helper;
public String auotoGenCode;
HtmlUtilily htmlUtil;
CustomerAccountSetupHandler cas;
    public CustomerAccountSetupAuditServlet() {
    	auotoGenCode = "";
    	cas = new CustomerAccountSetupHandler();
    	htmlUtil = new HtmlUtilily();
    }

    /**
     * Initializes the servlet.
     *
     * @param config ServletConfig
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
		 
		String pageAction = request.getParameter("pageAction");
        //String type = request.getParameter("TYPE");
		String errorMessage = null;
		boolean updtst = false;
        //java.sql.Date dt = new java.sql.Date();
		QueryUtil qu = new  QueryUtil();
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");
		//NumberFormat formatter = new DecimalFormat("###,###,###,###,#00.00");
        CurrencyUnFormatter  cuf  =  new  CurrencyUnFormatter();

		/**
     	int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		
		
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}
**/
     //String manualTableCoding = (String)session.getAttribute("manualTableCoding");   
		//String  operMode  =  request.getParameter("operMode");
	//Double d = new  Double("4.35");
	String  prevPage =   request.getParameter("prevPage");
	
	String  returnPage  =   request.getParameter("returnPage");
	
	String  nextPage =  request.getParameter("nextPage");
	
	      String  homePage =  request.getParameter("homePage");
	
	String  batchMode =  request.getParameter("batchMode");
		 if(batchMode == null)
		   {   batchMode =  "N";  }
	 batchMode =  "N"; 	   
	
	String  mtId  =  request.getParameter("mtId");
	 String  customerCode  =  request.getParameter("customerCode");
	 
	 String  customerNo  =  request.getParameter("customerNo");
	 if(customerNo == null) { customerNo =  customerCode;} 

	  if(customerCode == null)
	    {  customerCode =  customerNo;  }
		
		  String  customerClass  =  request.getParameter("customerClass");
	 
	  String  customerType  =  request.getParameter("customerType");
	  
	   String  accountType  =  request.getParameter("accountType");
	    String  accountTypeCode  =  request.getParameter("accountTypeCode");
	if((accountType  == null) ||  (accountType  == ""))
	  {   
	   String qrye = "SELECT   DESCRIPTION   FROM   IA_ACCOUNT_TYPE  WHERE  CODE  = '";
				 accountType  =  qu.execQuery(qrye, accountTypeCode);
	 }
		
	String isAutoGen = htmlUtil.getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");		
	String  accountNo  =  request.getParameter("accountNo");
	String  customerCategory  =  request.getParameter("customerCategory");
	String  customerAccountClass  =  request.getParameter("customerAccountClass");
	String branchCode = request.getParameter("branchcodem");
//	System.out.println("<<<<<<branchCode>>>>>>: "+branchCode);
	String  oldAccountNo  =  request.getParameter("oldAccountNo"); 
	if(oldAccountNo == null){  oldAccountNo = ""; }
	
	String  accountName =  request.getParameter("accountName");
	String  currency  =   request.getParameter("currency");
	String  currencyCode   =   request.getParameter("currencyCode");
	if((currency == null) ||  (currency == ""))
	  {   
	   String qry = "SELECT   DESCRIPTION  FROM   IA_GB_CURRENCY_CODE  WHERE  ISO_CODE  = '";
				 currency =  qu.execQuery(qry, currencyCode);
	 }
	
	double  accountBalance = 0.00;
	String  AccountBalance =  request.getParameter("accountBalance");
	if(AccountBalance == null){ accountBalance = 0.00 ;  }
	else 
	{ 
	//AccountBalance = formatter.format();
	accountBalance = cuf.unFormatCurrencyToDouble(AccountBalance);

	}
	
	double  perctDiscount  =  0.00;
	String  PerctDiscount =  request.getParameter("perctDiscount");
	if(PerctDiscount == null){ perctDiscount = 0.00 ;  }
	else { perctDiscount = Double.valueOf(PerctDiscount);  }
	
	String  industry   =   request.getParameter("industry");
	String  industryCode   =   request.getParameter("industryCode");
	if((industry == null) ||  (industry == ""))
	  {   	
	String qriy = "SELECT  DESCRIPTION    FROM   IA_INDUSTRY  WHERE  CODE = '";
	industry =  qu.execQuery(qriy,industryCode);
		}
	String  lastTransDate    =   request.getParameter("lastTransDate");
	

		String  branch    =   request.getParameter("branch");
		if(branch == null){ branch =  "0"; }
	
	String  status    =   request.getParameter("status");
	String  createDate    =   request.getParameter("createDate");
	String  effectiveDate    =   request.getParameter("effectiveDate");
	//String UserId =  request.getParameter("CurrenctUser");
	String  accountOfficer  =  request.getParameter("accountOfficer");
	int  transactionCount = 0;
	String  TransactionCount  =  request.getParameter("transactionCount");
	if((TransactionCount != null) && (!TransactionCount.equals("")))
	{  transactionCount = Integer.valueOf(TransactionCount);  }  
	
	String  witholding    =   request.getParameter("witholding");
	if((witholding == null) || (witholding.equals("")))  {   witholding = "N";  }
	String  witholdingType   =   request.getParameter("witholdingType");
	String  vat    =   request.getParameter("vat");	
		if((vat == null) || (vat.equals("")))  {   vat = "N";  }
	
	String UserId =  request.getParameter("UserId");
	
	
	String  productCode =   request.getParameter("productCode");
		if((productCode == null) || (productCode == "")) 
		    { //productCode =  String.valueOf(System.currentTimeMillis());
			  productCode = "0024-";
			}
	
	
	 int  userId =  0;
	    if((UserId != null) && (!UserId.equals("")))
	    {   userId = Integer.parseInt(UserId);  }
			
		CustomerAccountSetup   customerAccountSetup  =  new   CustomerAccountSetup();
		CustomerAccountDisplay   customerAccountDisplay  =  new   CustomerAccountDisplay();
		
	//	System.out.println("In Customer Setup Audit Servlet");
		
					customerAccountSetup.setMtId(mtId);
				  customerAccountSetup.setCustomerNo(customerNo);
				
				  customerAccountSetup.setCustomerClass(customerClass);
				  customerAccountSetup.setCustomerType(customerType);
				  customerAccountSetup.setAccountType(accountType);
				  customerAccountSetup.setAccountTypeCode(accountTypeCode);
				  customerAccountSetup.setAccountNo(accountNo);
				   customerAccountSetup.setOldAccountNo(oldAccountNo);
				   customerAccountSetup.setAccountName(accountName);
				    customerAccountSetup.setCurrencyCode(currencyCode);
					 customerAccountSetup.setCurrency(currency);
					  customerAccountSetup.setAccountBalance(accountBalance);
					   customerAccountSetup.setPerctDiscount(perctDiscount);
					    customerAccountSetup.setIndustryCode(industryCode);
						 customerAccountSetup.setIndustry(industry);
						  customerAccountSetup.setLastTransDate(lastTransDate);
						  
				
				customerAccountSetup.setStatus(status);
				customerAccountSetup.setCreateDate(createDate);
				customerAccountSetup.setEffectiveDate(effectiveDate);
				 if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
				customerAccountSetup.setUserId(userId);
				customerAccountSetup.setTransactionCount(transactionCount);
				customerAccountSetup.setAccountOfficer(accountOfficer);
				customerAccountSetup.setWitholding(witholding);
				customerAccountSetup.setWitholdingType(witholdingType);
				customerAccountSetup.setVat(vat);
				
				
				 customerAccountDisplay.setCustomerNo(customerNo);
				 customerAccountDisplay.setCustomerClass(customerClass);
				  customerAccountDisplay.setCustomerType(customerType);
				  customerAccountDisplay.setAccountType(accountType);
				  customerAccountDisplay.setAccountTypeCode(accountTypeCode);
				  customerAccountDisplay.setAccountNo(accountNo);
				  customerAccountDisplay.setOldAccountNo(oldAccountNo);
				  customerAccountDisplay.setAccountName(accountName);
				  customerAccountDisplay.setCurrencyCode(currencyCode);
				  customerAccountDisplay.setCurrency(currency);
				 customerAccountDisplay.setAccountBalance(accountBalance);
				 customerAccountDisplay.setPerctDiscount(perctDiscount);
				 customerAccountDisplay.setIndustryCode(industryCode);
				 customerAccountDisplay.setIndustry(industry);
				 customerAccountDisplay.setLastTransDate(lastTransDate);
						  
				
				customerAccountDisplay.setStatus(status);
				customerAccountDisplay.setCreateDate(createDate);
				customerAccountDisplay.setEffectiveDate(effectiveDate);
				 if((effectiveDate == "") || (effectiveDate == null))
				      {   effectiveDate =  createDate;  }
				customerAccountDisplay.setUserId(userId);
				customerAccountDisplay.setTransactionCount(transactionCount);
				customerAccountDisplay.setAccountOfficer(accountOfficer);
				//customerAccountDisplay.setWitholding(witholding);
				//customerAccountDisplay.setWitholdingType(witholdingType);
				//customerAccountDisplay.setVat(vat);
		
		com.magbel.ia.bus.CustomerAccountSetupHandler  custAcctSetupHandle;
		com.magbel.ia.bus.CustomerAccountDisplayHandler  custAcctDisplayHandle;
														//	= new  CustomerAccountHandler();
           com.magbel.ia.bus.CustomerHandler  custHandle
															= new  CustomerHandler();
           CodeGenerator cg = new CodeGenerator();
	    
      try {
              custAcctSetupHandle   = new  CustomerAccountSetupHandler();
			  custAcctDisplayHandle   = new  CustomerAccountDisplayHandler();
            if(mtId == null)
			  { 
						// accountNo  =  String.valueOf(System.currentTimeMillis());
			              accountNo = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("ACCOUNTS", "", "", "") : accountNo;
			                auotoGenCode = accountNo;
			                accountNo = (new StringBuilder()).append(branchCode).append(accountNo).toString();
			                customerAccountSetup.setAccountNo(accountNo);						 
						 customerAccountSetup.setAccountNo(accountNo); 
						 customerAccountDisplay.setAccountNo(accountNo); 
						
					if(custAcctSetupHandle.createCustomerAccountSetup(customerAccountSetup))
									{
									  System.out.println("Created_Customer_Account");
									   custAcctDisplayHandle.createCustomerAccountDisplay(customerAccountDisplay);
										errorMessage = "Customer_account_record_has_been_successfully_created";									out.println("<script>alert('Record_saved_successfully.');</script>");
										
		response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerNo+"&status="+status+"&currencyCode="+currencyCode+"&customerType="+customerType+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&homePage="+homePage+"&returnPage="+returnPage+"&customerClass="+customerClass+"&customerNo="+customerNo); 
											
					/**
									}
								else
									{
										errorMessage = "Error Occurs while attempting to save record";
										out.print("<script>history.go(-1);</script>");
									}
					**/				
									}  else {  
									 custAcctDisplayHandle.createCustomerAccountDisplay(customerAccountDisplay);
									System.out.println("CustomerAccountSetupAuditServlet_::_Error_Occurs_while_attempting_to_save_record" );      
	errorMessage = "CustomerAccountSetupAuditServlet_:_:_Error_Occurs_while_attempting_to_save_record";
									 response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerNo+"&status="+status+"&currencyCode="+currencyCode+"&customerType="+customerType+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&homePage="+homePage+"&returnPage="+returnPage+"&customerClass="+customerClass+"&customerNo="+customerNo); }
									
					
                    }
                else if(mtId != null)  //mtid is not null so update takes place here
					{
							//audit.select( 1, "SELECT * FROM  IA_CUSTOMER_ACCOUNT   WHERE MTID = '"+ mtId +"'");
                              if(custAcctSetupHandle.updateCustomerAccountSetup(customerAccountSetup))
							    {
								  custAcctDisplayHandle.updateCustomerAccountDisplay(customerAccountDisplay);
								    errorMessage = "Customer_account_record_has_been_successfully_updated";
									// -- Audit section --->
								//	audit.select( 2, "SELECT * FROM  IA_CUSTOMER_ACCOUNT  WHERE MTID = '"+ mtId +"'");
								//	updtst = audit.logAuditTrail("IA_CUSTOMER_ACCOUNT" ,  branch, userId,   mtId, "Update On CustomerAccount");
								if(updtst == true)
										{
										errorMessage = "Changes_on_customer_account_record_has_been_successfully_updated";
										///out.print("<script>alert('Customer record has been successfully updated')</script>");
										// response.sendRedirect("DocumentHelp.jsp?np=CustomerAccount&customerCode="+customerNo+"&status="+status+"&currencyCode="+currencyCode); 
										}
									else 
										{
										errorMessage = "No_changes_made_on_record";
										//out.print("<script>alert('No changes made on record')</script>");
										//out.print("<script>window.location = 'DocumentHelp.jsp?np=editCustomerAccount?mtId="+mtId+"'</script>");
										}
								//**/// -- Audit section --Ends --->
							
							     if(batchMode.equalsIgnoreCase("Y"))
										 {
							          response.sendRedirect("DocumentHelp.jsp?np=CustomerAccount");
											}
										else if(batchMode.equalsIgnoreCase("N"))
										  {
						response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerNo+"&status="+status+"&currencyCode="+currencyCode+"&customerType="+customerType+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&homePage="+homePage+"&returnPage="+returnPage+"&customerNo="+customerNo); 
										  }
								}								 
                            else
                                {
									 errorMessage = "Error_Occurs_while_attempting_to_update_record";
									out.print("<script>history.go(-1);</script>");
                                }								
					      
				   }
				
				
						
			}
			
			catch(Throwable e)
			{
				
				errorMessage = "Ensure unique record entry";
				//out.print("<script>alert('Ensure unique record entry.')</script>");
				//out.print("<script>window.location = 'editCustomerAccount.jsp?mtId="+mtId+"'</script>");
				e.printStackTrace();
				System.err.print(e.getMessage());
			}
		
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return String
     */
    public String getServletInfo() {
        return " Customer Account Setup Audit Servlet";
    }
}
