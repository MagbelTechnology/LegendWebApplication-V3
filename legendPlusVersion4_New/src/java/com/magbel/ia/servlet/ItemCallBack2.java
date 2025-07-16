package com.magbel.ia.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import com.magbel.ia.bus.AccountInterfaceServiceBus;


public class ItemCallBack2 extends HttpServlet 
{
    private static final String CONTENT_TYPE = "text/html";

    public void init(ServletConfig config) throws ServletException 
	{
        super.init(config);    
    }
AccountInterfaceServiceBus pos = new AccountInterfaceServiceBus();
    
    ArrayList list = new ArrayList();
   
    int userId = 0;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String description = request.getParameter("description");

 
      
          if ((description != null)||(description != "")) 
		  {
              String ledger_no   = pos.getCodeName("SELECT LEDGER_NO,DESCRIPTION FROM IA_GL_ACCT_LEDGER WHERE DESCRIPTION='"+description+"'");
              
          }
         
  
    }
    

}
