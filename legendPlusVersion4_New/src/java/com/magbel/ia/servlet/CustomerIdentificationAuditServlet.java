package com.magbel.ia.servlet;

import com.magbel.util.CheckIntegerityContraint;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import com.magbel.ia.util.ApplicationHelper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import  com.magbel.ia.bus.CustomerHandler;
import  com.magbel.ia.bus.CustomerIdentificationHandler;
import   com.magbel.ia.vao.Customer;
import   com.magbel.ia.vao.CustomerIdentification;
//import  com.magbel.ia.bus.QueryUtil;
import  com.magbel.ia.util.QueryUtil;
import   com.magbel.ia.vao.Obj;
import  java.text.DecimalFormat;
import java.util.ArrayList;

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
public class CustomerIdentificationAuditServlet extends HttpServlet {
 private  ApplicationHelper helper;
    public CustomerIdentificationAuditServlet() {
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
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

		/**
     	int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}
		
		
		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}
		**/
        
		//String  operMode  =  request.getParameter("operMode");
		String  prevPage =   request.getParameter("prevPage");
		String  returnPage =   request.getParameter("returnPage");	
		 String  nextPage =  request.getParameter("nextPage");
		
		String  homePage =  request.getParameter("homePage");
		 
		 String  batchMode =  request.getParameter("batchMode");
		 if(batchMode == null)
		   {   batchMode =  "N";  }
		String  customerCategory  =  request.getParameter("customerCategory");  
		
				
	String  mtId  =  request.getParameter("mtId");
	 String  customerCode  =  request.getParameter("customerNo");
	 //System.out.println("CustomerCode is:"+customerCode);
	String  identificationCode  =  request.getParameter("identificationCode");
	String  identificationType  =  request.getParameter("identificationType");
	String  dateOfExpiry  =  request.getParameter("dateOfExpiry");
	String  dateOfIssue  =  request.getParameter("dateOfIssue"); 
	String  cityOfIssuance =  request.getParameter("cityOfIssuance");
	String  cityOfRegistration  =   request.getParameter("cityOfRegistration");
	String  citizenship   =   request.getParameter("citizenship");
	String  coyRegNo  =  request.getParameter("coyRegNo"); 
	String  coyRegOffice =  request.getParameter("coyRegOffice");
	String  coyRegOfficeCode  =   request.getParameter("coyRegOfficeCode");
	String  UserId  =   request.getParameter("userId");
	String  status  =   request.getParameter("status");
	String  createDate  =   request.getParameter("createDate");
	String  effectiveDate  =   request.getParameter("effectiveDate");
	String  Volume  =   request.getParameter("volume");
	String  Page  =    request.getParameter("page");
	
	int   volume  =  0;
	if((Volume != null) && (!Volume.equals("")))
	   {  volume =  Integer.valueOf(Volume);  }
	   
	 	int   page  =  0;
	if((Page != null) && (!Page.equals("")))
	   {  page =  Integer.valueOf(Page);  }
	
	String  productCode =   request.getParameter("productCode");
		if((productCode == null) || (productCode == "")) 
		    { //productCode =  String.valueOf(System.currentTimeMillis());
			  productCode = "0024-";
			}
	
	
	 int  userId =  0;
	   if((UserId != null) && (!UserId.equals("")))
	    {   userId = Integer.parseInt(UserId);  }
			
		CustomerIdentification   customerIdentification  = new  com.magbel.ia.vao.CustomerIdentification();
				
				customerIdentification.setMtId(mtId);
				customerIdentification.setCustomerCode(customerCode);
					customerIdentification.setIdentificationCode(identificationCode);
					customerIdentification.setIdentificationType(identificationType);
					customerIdentification.setDateOfExpiration(dateOfExpiry);
					customerIdentification.setDateOfIssue(dateOfIssue);
					customerIdentification.setCityOfIssuance(cityOfIssuance);
					customerIdentification.setCityOfRegistration(cityOfRegistration);
					customerIdentification.setCitizenship(citizenship);
					customerIdentification.setCompanyRegNo(coyRegNo);
					customerIdentification.setCompanyRegOffice(coyRegOffice);
					customerIdentification.setCoyRegOfficeCode(coyRegOfficeCode);
					customerIdentification.setUserId(userId);
					customerIdentification.setStatus(status);
					customerIdentification.setCreateDate(createDate);
					customerIdentification.setEffectiveDate(effectiveDate);
					customerIdentification.setVolume(volume);
					customerIdentification.setPage(page);
					
		com.magbel.ia.bus.CustomerIdentificationHandler  custIdentifHandle
															= new  CustomerIdentificationHandler();
      
	    com.magbel.ia.bus.CustomerHandler  custHandle
															= new  CustomerHandler();
															
      // try{
            
            if(mtId == null ) //if mtid is null create new customer record
					{
					  
					 				     
						identificationCode  =  String.valueOf(System.currentTimeMillis());
						customerIdentification.setIdentificationCode(identificationCode); 
							
						
					  
					
								if (custIdentifHandle.createCustomerIdentification(customerIdentification))
									{
										errorMessage = "Customer_identification_record_has_been_successfully_created";
										//out.print("<script>alert('Record saved successfully.');</script>");
										//System.out.print(custIdentifHandle.getCustomerIdentificationByIdentifCode(identificationCode).getMtId());
							response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerCode+"&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&returnPage="+returnPage);
											
									}
								else
									{
										errorMessage = "Error_Occurs_while_attempting_to_save_record";
										out.print("<script>history.go(-1);</script>");
									}
						//		}
					
                    }
                 else  //mtid is not null so update takes place here
					{
                	   // audit.select( 1, "SELECT * FROM  IA_CUSTOMER_IDENTIFICATION   WHERE MTID = '"+ mtId +"'");
					   
                              if(custIdentifHandle.updateCustomerIdentification(customerIdentification))
							    {
								    errorMessage = "Customer_identification_record_has_been_successfully_updated";
								/**	audit.select( 2, "SELECT * FROM   IA_CUSTOMER_IDENTIFICATION   WHERE MTID = '"+ mtId +"'");
									updtst = audit.logAuditTrail("IA_CUSTOMER_IDENTIFICATION" ,  branchcode, loginID,   mtId, "Update On Customer Identification");
									if(updtst == true)
										{
										errorMessage = "Changes on customer Identification  record has been successfully updated";
										//out.print("<script>alert('Customer record has been successfully updated')</script>");
										out.print("<script>window.location = 'customerIdentificationRecords.jsp'</script>");
										}
									else 
										{
										errorMessage = "No changes made on record";
										//out.print("<script>alert('No changes made on record')</script>");
										out.print("<script>window.location = 'editCustomerIdentification.jsp?mtId="+mtId+"'</script>");
										}
										**/
										//	out.print("<script>window.location = 'CustomerAccount.jsp'</script>");
										if(batchMode.equalsIgnoreCase("Y")) 
										 {
							         response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerCode+"&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&returnPage="+returnPage);
											}
										else if(batchMode.equalsIgnoreCase("N")) 
										  {
								response.sendRedirect("DocumentHelp.jsp?np="+nextPage+"&customerCode="+customerCode+"&homePage="+homePage+"&customerCategory="+customerCategory+"&errorMessage="+errorMessage+"&returnPage="+returnPage);											}
								}								 
                            else
                                {
									 errorMessage = "Error_Occurs_while_attempting_to_update_record";
									 out.println("Error_Occurs_while_attempting_to_update_record");
									out.print("<script>history.go(-1);</script>");
                                }								
					      
					}
				
				
			//}
		/**	
			catch(Throwable e)
			{
				
				errorMessage = "Ensure unique record entry";
				//out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'editCustomerIdentification.jsp?mtId="+mtId+"'</script>");
				e.printStackTrace();
				System.err.print(e.getMessage());
			}
			**/
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return String
     */
    public String getServletInfo() {
        return "Customer Identification Audit Servlet";
    }
}
