package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.net.manager.DepreciationProcessingManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.magbel.legend.bus.FinancialExchangeServiceBus;

import magma.net.manager.RaiseEntryManager;

import com.magbel.legend.mail.EmailSmsServiceBus;

import magma.util.Codes;

import java.util.*;

public class RFAReversalAuditServlet extends HttpServlet
{

	private DepreciationProcessingManager service;
	private FinancialExchangeServiceBus bus;
	private RaiseEntryManager raiseMan;
	private EmailSmsServiceBus mail ;
	private Codes message;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
    public RFAReversalAuditServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service = new DepreciationProcessingManager();
        raiseMan = new RaiseEntryManager();
        bus = new FinancialExchangeServiceBus();
        mail= new EmailSmsServiceBus();
        message= new Codes();
        ad = new AssetRecordsBean();
        records = new ApprovalRecords();
        }
        catch(Exception et)
        {et.printStackTrace();}
    }
    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
       
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        
        String userClass = (String) session.getAttribute("UserClass");
        
        String userId =(String)session.getAttribute("CurrentUser");
        String id = request.getParameter("id");
        String page1 = "ASSET DISPOSAL RAISE ENTRY";
        String iso;
        String msgText1 ="";
        String to ="";
        String mail_code="4";
        String subject = "Raise Entry";
        String transaction_type="Asset Disposal";
        String costdebitAcctName = "";
        String costcreditAcctName = "";
        
		 String  integrifyId = request.getParameter("integrifyId");
		 String  categoryCode = request.getParameter("categoryCode");
		 String  subCategoryCode = request.getParameter("subCategoryCode");
		 String  sbuCode = request.getParameter("sbuCode");
		 double  amount = Double.parseDouble(request.getParameter("amtvalue"));  
          
         System.out.println(" <<<<<************>>>>>>>>> integrifyId  " +integrifyId+"    categoryCode: "+categoryCode+"    subCategoryCode: "+subCategoryCode+"     sbuCode: "+sbuCode+"   amount: "+amount);
 try    
    {   
	 if (!userClass.equals("NULL") || userClass!=null){
		 String q = "UPDATE AM_ACQUISITION_BUDGET SET TEMP_BALANCE = TEMP_BALANCE - "+amount+" WHERE CATEGORY_CODE ='"+categoryCode+"' AND SUB_CATEGORY_CODE = '"+subCategoryCode+"' AND SBU_CODE = '"+sbuCode+"'";
		 System.out.println(" <<<<<==>>>>>>>>> q  " +q);
           ad.updateAssetStatusChange(q);	  
           System.out.println(" <<<<<==>>>>>>>>>=====  ");
//	            msgText1 = message.MailMessage(mail_code, transaction_type)+", \n";
//				msgText1 += "For asset id "+id+".\n";
//				to=message.MailTo(mail_code, transaction_type);
//				mail.sendMail(to,subject,msgText1);

	    out.print( "<script>alert(Reversal for Integrify Id '"+integrifyId+"' is Done);</script>");
//	    out.print("<script>window.close('RequestForAssetReversal.jsp');</script>");
//	    out.println("window.location='DocumentHelp.jsp?np=RequestForAssetReversal'" );
	    out.println("<script>window.location='DocumentHelp.jsp?np=RequestForAssetReversal&IntegrifyId="+integrifyId+"'</script>");
	  // session.setAttribute("assetid", id);
	  // session.setAttribute("page1", page1);
	  // out.print("<script>window.open('raiseEntryList.jsp','raiseasset4','width=850,height=350,scrollbars=Yes,resizable=Yes,align=center');</script>");
    }else {
		out.print("<script>alert('You have No Right')</script>");
	}   
    }

   catch(Exception e)
       {
	     e.printStackTrace();
	   }
  }   
  
    public String getServletInfo()
    {
        return "Process Action Servlet";
    }
}