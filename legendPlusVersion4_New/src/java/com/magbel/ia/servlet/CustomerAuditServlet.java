package com.magbel.ia.servlet;

import 	com.magbel.util.CheckIntegerityContraint;

import 	javax.servlet.http.HttpSession;
import 	javax.servlet.http.HttpServletRequest;

import 	java.io.IOException;

import 	javax.servlet.ServletException;
import 	javax.servlet.ServletConfig;

import 	java.io.PrintWriter;

import 	com.magbel.ia.util.ApplicationHelper;

import 	javax.servlet.http.HttpServletResponse;
import 	javax.servlet.http.HttpServlet;

import   com.magbel.ia.vao.Customer;
import   com.magbel.ia.bus.CustomerHandler;
import   com.magbel.ia.bus.QueryUtil;

import audit.*;

import com.magbel.ia.bus.PurchaseOrderServiceBus;
import com.magbel.ia.util.CodeGenerator;

//import com.magbel.ia.vao.Customer;

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
public class CustomerAuditServlet extends HttpServlet {
    public String auotoGenCode;
    CustomerHandler ch;
    public CustomerAuditServlet() {
        ch = new CustomerHandler();
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
     private  ApplicationHelper helper;
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

     	/**
		int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		**/
		
		//String branchcode = (String)session.getAttribute("UserCenter");
		//if(branchcode == null) { branchcode = "not set";	}
		//com.magbel.ia.vao.User    user =  ((com.magbel.ia.vao.User) session.getAttribute("CurrentUser")).getUserName();
/**
String loginID;

if (user == null) {
			loginID = "Unknown";
		} else {
			loginID = user.getUserName();
		}



String branchcode = loginId.getBranch();
		if (branchcode == null) {
			branchcode = "not set";
		}
**/
        String  operMode =  request.getParameter("operMode");
		
		 String  nextPage =  request.getParameter("nextPage");
		
		 String  homePage =  request.getParameter("homePage");
		 
		 String  batchMode =  request.getParameter("batchMode");
		 if(batchMode == null)
		   {   batchMode =  "N";  }
		   
		
		  
		String customerCode  =  request.getParameter("customerNo");
		//if(customerCode == null) { customerCode = "0"; }
		
		String  manualTableCoding  =  (String)session.getAttribute("manualTableCoding");
		
		String  mtId =  request.getParameter("mtId");
		
		String  productCode =   request.getParameter("productCode");
		if((productCode == null) || (productCode == "")) 
		    { //productCode =  String.valueOf(System.currentTimeMillis());
			  productCode = "0012-";
			}
		
		String  oldCustomerNo =  request.getParameter("oldCustomerNo");
		//if(oldCustomerNo == null) { oldCustomerNo = "0"; }
		
		
		
		//String  customerName =   request.getParameter("customerName");
		//if(customerName == null) {customerName = "not set"; }
		String  customerName = request.getParameter("customerName");
		
		session.setAttribute("customerName", customerName);
		
		String  residency =   request.getParameter("residency");
		
		if(residency == null)
		  {  residency = "N"; }
		
		
		String  title =   request.getParameter("title");
		
		
		String  customerType =   request.getParameter("customerType");
	
		
		String  customerCategory  =  request.getParameter("customerCategory");
	
		
    	String  address1  =  request.getParameter("address1");
		
		
		String  address2  =  request.getParameter("address2");
		
		
		String  country =   request.getParameter("country");
		 //if(country == null) {country = "not set"; }
		 
		String  contactCode =   request.getParameter("contactCode");
		//if(contactCode == null) {contactCode = "not set"; }
		
		String  state =   request.getParameter("state");
		//System.out.println("state  >>>>>>>>>>>>> "+state);
		
		if((state == null) || (state == "")){
		String  stateCode =   request.getParameter("stateCode");
		
        // String querys = "SELECT CODE,NAME   FROM   IA_STATE  WHERE  CRITERIA = '"+CountryCode+"'   ";
			  
      // 	state =  qu.execQuery(querys,stateCode);
		}
		
		String  countryCode =   request.getParameter("countryCode");
		String  companyCode =   request.getParameter("companyCode");
		
		if((country == null) ||  (country == ""))
	    {
		String qrist = "SELECT   DESCRIPTION   FROM   IA_COUNTRY  WHERE  CODE = '";
		country =  qu.execQuery(qrist,countryCode);
		}
		String isAutoGen = ch.getCodeName("SELECT auto_generate_ID FROM MG_GB_COMPANY");
		String  branch  =  request.getParameter("branch");
//		String companycode = ch.getCodeName("SELECT COMP_CODE FROM MG_GB_COMPANY");
		//if(branch == null) {branch = "not set"; }
		
		String  branchCode  =  request.getParameter("branchCode");
		//System.out.println("Branch Code >>> "+branchCode);
		/**
		if((branch == null) || (branch == ""))
		{
		String qrid = "SELECT   BRANCH_NAME   FROM   IA_ad_branch  WHERE  BRANCH_CODE = '";
		branch =  qu.execQuery(qrid, branchCode);
		}
		**/
		
		String  industry  =  request.getParameter("industry");
		//if(industry  == null) {industry  = "not set"; }
		
		String  industryCode  =  request.getParameter("industryCode");
		//System.out.println("Industry Code >>> "+industryCode);
		
		if((industry == null) ||  (industry == ""))
	  {   	
			String qriy = "SELECT  CODE, DESCRIPTION    FROM   IA_INDUSTRY  WHERE  CODE = '";
			industry =  qu.execQuery(qriy,industryCode);
		}
		
		
		String  relationshipCode  =  request.getParameter("relationshipCode");
		//if(relationshipCode == null) {relationshipCode = "not set"; }
		
		String  phone  =  request.getParameter("phone");
		//	if(phone == null) {phone = "not set"; }
			
		String  fax   =  request.getParameter("fax");
		//	if(fax == null) {fax = "not set"; }
			
		String  email  = request.getParameter("email");
			//if(email == null) {email = "not set"; }
			
		String  city  = request.getParameter("city");
			//if(city == null) {city = "not set"; }
			
		String  gender  = request.getParameter("gender");
			//if(gender == null) {gender = "not set"; }
			
		String  relationship  =  request.getParameter("relationship");
		//if(relationship == null) {relationship = "not set"; }
		
		String  createDate  =  request.getParameter("createDate");
		//if(createDate == null) {createDate = "not set"; }
		
		String  effectiveDate  =  request.getParameter("effectiveDate");
		//if(effectiveDate == null) {effectiveDate = "not set"; }
		
		String  status  =  request.getParameter("status");
		//if(status == null) {status = "not set"; }
		String[] custNames =  customerName.split("\\s");
		
		String  lastName =  custNames[0];
		
		String  middleName = "";   String firstName = "";
		
		if(custNames.length == 2)
		  {
			 lastName =  custNames[0];
			middleName =  custNames[1];
		 }
		
		if(custNames.length == 3)
		  {
			  lastName =  custNames[0];
			 middleName =  custNames[1];
			  firstName =  custNames[2];
		 }
		
		
		session.setAttribute("CUSTOMER_NAME", customerName);
		
			 // String  UserId    =  request.getParameter("CurrentUser");
	String  UserId    =  request.getParameter("userId");
	 int  userId =  0;
	   if((UserId != null) && (!UserId.equals("")))
	    {   userId = Integer.parseInt(UserId);  }
		
		Customer   cust  = new Customer();
				cust.setMtId(mtId);
				cust.setCustomerCode(customerCode);
				cust.setCustomerCategory(customerCategory);
				//cust.setCustomerType(customerType);
				cust.setOldCustomerNo(oldCustomerNo);
				cust.setLastName(lastName);
				cust.setFirstName(firstName);
				cust.setMiddleName(middleName);
				cust.setCustomerName(customerName);
				cust.setGender(gender);
				cust.setResidency(residency);
				cust.setTitle(title);
				//
				//cust.setContactCode(contactCode);
				//cust.setCustomerClass(customerClass);
				//cust.setCustomerClassCode(customerClassCode);
				//cust.setRelationshipCode(relationshipCode);
				//cust.setRelationship(relationship);
				cust.setAddress1(address1);
				cust.setAddress2(address2);
				cust.setPhone(phone);
				cust.setFax(fax);
				cust.setEmail(email);
				cust.setCity(city);
				cust.setState(state);
				cust.setCountry(country);
				cust.setCountryCode(countryCode);
				cust.setBranch(branch);
				cust.setBranchCode(branchCode);
				cust.setIndustry(industry);
				cust.setIndustryCode(industryCode);
				cust.setCreateDate(createDate);
				cust.setEffectiveDate(effectiveDate);
				cust.setStatus(status);
				cust.setUserId(userId);
	
					   
					   
		
		com.magbel.ia.bus.CustomerHandler  custHandle  = new  CustomerHandler();
                PurchaseOrderServiceBus serviceBus = new PurchaseOrderServiceBus();
                CodeGenerator cg = new CodeGenerator();
                String isAutoIdGen = "";
        //System.out.println("In Customer  Audit Servlet");
	  //  System.out.println("mtId  is =  "+mtId);
        try{
            
            //    if((cust.getMtId() == null)  ||  (cust.getMtId().equals("") )) //if mtid is null create new customer record
			if(cust.getMtId() == null) 
					{
					
					
					
						 if(custHandle.isCustomerCodeExisting(customerCode)) 
								{ 
								errorMessage = "Customer_Code_>>_'"+customerCode+"'_already_exists";
								//out.print("<script>alert(Customer Code >> '"+customerCode+"'  already exists);</script>");
								out.print("<script>history.go(-1);</script>");
								}
							else
								{
						/**/
						/**
					        isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
						
						    if(isAutoIdGen == null){  isAutoIdGen = "Y"; }
                                               
                                               if((isAutoIdGen.trim()).equalsIgnoreCase("Y"))
                                                	{  
								customerCode  =  cg.generateCode("CUSTOMER","","","");//String.valueOf(System.currentTimeMillis());		
							      }
								
**/								
								System.out.println("Customer Code 1: "+customerCode);
//							customerCode  = String.valueOf(System.currentTimeMillis());
							 customerCode = isAutoGen.trim().equalsIgnoreCase("Y") ? cg.generateCode2("CUSTOMERS", "", "", "") : customerCode;
							System.out.println("Customer Code 2 1: "+customerCode);
								cust.setCustomerCode(customerCode); 
								/*String  serialCustCode = null;
								serialCustCode =  helper.getGeneratedId("IA_CUSTOMER");
								customerCode =  productCode+branchCode+"-"+serialCustCode;//cg.generateCode("CUSTOMER","","","");
								cust.setCustomerCode(customerCode); */
							//}
					
								if (custHandle.createCustomer(cust,companyCode))
									{
									 System.out.println("Customer  created");
										errorMessage = "Customer_record_has_been_saved";
										//out.print("<script>alert('Record saved successcustomery.');</script>");
										
							         response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerNo="+customerCode+"&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage); 
											
									}
								else
									{
										errorMessage = "Error_Occurs_while_attempting_to_save_record";
										out.print("<script>history.go(-1);</script>");
									}
								}
					
                    }
                 else  //mtid is not null so update takes place here
					{
                	  // audit.select( 1, "SELECT * FROM  IA_CUSTOMER  WHERE MTID = '"+ mtId +"'");
                              if(custHandle.updateCustomer(cust))
							    {
								    errorMessage = "Customer_record_has_been_successfully_updated";
									// -- Audit section --->
								/**	
								//	audit.select( 2, "SELECT * FROM  IA_CUSTOMER  WHERE MTID = '"+ mtId +"'");
									updtst = audit.logAuditTrail("IA_CUSTOMER" ,  branch, userId,   mtId);
									/**
									if(updtst == true)
										{
										errorMessage = "Changes_on_customer_record_has_been_successfully_updated";
										//out.print("<script>alert('Customer_record_has_been_successfully_updated')</script>");
										out.print("<script>window.location = 'editCustomer.jsp?mtId="+mtId+"'</script>");
										}
									else 
										{
										errorMessage = "No changes made on record";
										//out.print("<script>alert('No changes made on record')</script>");
										out.print("<script>window.location = 'editCustomer.jsp?mtId="+mtId+"'</script>");
										}
										//// -- Audit section  Ends --->
										**/
									if(batchMode.equalsIgnoreCase("Y"))
										 {
							          response.sendRedirect("DocumentHelp.jsp?np=newCustomer&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage);
											}
										else if(batchMode.equalsIgnoreCase("N"))
										  {
									  response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerNo="+customerCode+"&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage); 
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
				out.print("<script>window.location = 'editCustomer.jsp?mtId="+mtId+"'</script>");
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
        return "Customer Audit Servlet";
    }
 
}
