package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.Approval;

import java.io.*;

import magma.AssetRecordsBean;
import magma.net.manager.DepreciationProcessingManager;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.util.*;

public class ComponentSetupAction extends HttpServlet
{

	private DepreciationProcessingManager service;
	
    public ComponentSetupAction()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service = new DepreciationProcessingManager();
     
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
        String userClass = (String) session.getAttribute("UserClass");
        
        PrintWriter out = response.getWriter();
        String[] distributions = request.getParameterValues("DEPRECIATION");
        String type = request.getParameter("type");
        String assetId = request.getParameter("ASSET_ID");
        String userId =(String)session.getAttribute("CurrentUser");
        String distId = request.getParameter("DIST_CODE");
        String remainder =request.getParameter("CONTROL");
        String CAT =request.getParameter("CAT");
        String COST =request.getParameter("COST");
	   
 try
    {
	 if (!userClass.equals("NULL") || userClass!=null){
	// if(service.isAlreadyProcessedComponent(distId))
	  //  {
		// service.clearComponentEntry(distId);
	   // }

	for(int x = 0; x < distributions.length; x ++)
	{
	String distributionName = request.getParameter(distributions[x]);
	String depExpenseField = distributions[x]+"DepExpense";
	String depExpenseValue = request.getParameter(depExpenseField);
	String depAccumField = distributions[x]+"AcummDep";
	String depAccumValue = request.getParameter(depAccumField);
	String depValueField = distributions[x]+"ValASSIGN";
	String depValue = request.getParameter(depValueField);
	String depreciationValueField = distributions[x]+"DepValue";
	String depreciationValue = request.getParameter(depreciationValueField);


	   System.out.println("XXX"+distributions[x]+" "+distributions.length);
	   if(depValue == null || depValue == "")
	        {
		   depValue = "0.00";
	       }
	       double amount = Double.parseDouble(depValue.replaceAll(",", "")); 

	    if(depreciationValue == null || depreciationValue == "")
	       {
		   depreciationValue = "0.00";
	       }
	       double depreciationAmount = Double.parseDouble(depreciationValue.replaceAll(",", ""));

	    if(depAccumValue == null || depAccumValue == "")
	       {
		   depAccumValue = "0.00";
	       }
	       double depAccumValueAmount = Double.parseDouble(depAccumValue.replaceAll(",", "")); 



	     boolean exec = service.recordInserted(assetId,depExpenseValue,depAccumValueAmount,depreciationAmount,Integer.toString(x+1));
	    
	     if(exec)
	      {
		   service.insertComponent( assetId, type, userId, distId, depExpenseValue, depAccumValueAmount,amount,Integer.toString(x+1),remainder,depreciationAmount);
	      }
	 
	  }
	
	
	   out.println("<script>");
	   out.println("window.location='DocumentHelp.jsp?np=componenetSetupDetail&ASSET_ID="+assetId+"&CAT="+CAT+"&COST="+COST+"';");
	   
	   out.println("</script>");
    }
    }
   catch(Exception e)
       {
	   e.printStackTrace();
	   }
  }   
  
    public String getServletInfo()
    {
        return "Confirmation Action Servlet";
    }
}