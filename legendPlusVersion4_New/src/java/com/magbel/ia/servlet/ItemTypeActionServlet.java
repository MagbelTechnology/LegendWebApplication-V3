
package com.magbel.ia.servlet;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import com.magbel.ia.bus.InventoryServiceBus;


public class ItemTypeActionServlet extends HttpServlet 
{

  /** Initializes the servlet.
   */
   private InventoryServiceBus serviceBus;

  public void init(ServletConfig config) throws ServletException 
  {
    super.init(config);
    serviceBus = new InventoryServiceBus();    
  }

  /** Destroys the servlet.
   */
  public void destroy() 
  {

  }

  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {

	response.setContentType("text/html");

    HttpSession session = request.getSession();
    PrintWriter out = response.getWriter();
    String id = request.getParameter("ID");
    String itemTypeCode = request.getParameter("itemTypeCode");
    String name = request.getParameter("itemName");
    String status = request.getParameter("status");
    String inventory = request.getParameter("inventory_");
    if(inventory.equals("")||inventory==null)
	{
        inventory = "Y";
    }
      String inventory_ = request.getParameter("inventory");
      String costMethod = request.getParameter("costMethod");
	  String companyCode = request.getParameter("companyCode");
	  String categoryCode = request.getParameter("categoryCode");
//	  System.out.println("<<<<<<<ItemTypeActionServlet categoryCode: "+categoryCode);
      String itemTypeAddBtn = request.getParameter("itemTypeAddBtn");
      String itemTypeUpdateBtn = request.getParameter("itemTypeUpdateBtn");
      String unitMeasurment = request.getParameter("unitMeasurment");
      
        String isAutoIdGen = "";

      try
	  {
    	   legend.admin.objects.User  user = (legend.admin.objects.User)session.getAttribute("_user");
    	   	 int userId = Integer.parseInt(user.getUserId());
           if(itemTypeCode == null)
		    {
			
             /*  isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
               if(isAutoIdGen.trim().equalsIgnoreCase("N"))
			   { //test 4 auto ID setup
                     if(itemTypeCode.equals("")||itemTypeCode == null)
					 {
                     out.print("<script>alert('Item Type Code. cannot be empty')</script>");
                     out.print("<script>history.go(-1);</script>");
                     }
                     else if(serviceBus.isRecordExisting("SELECT COUNT(ITEMTYPE_CODE) FROM IA_ITEMTYPE WHERE ITEMTYPE_CODE='"+itemTypeCode+"'"))
					    { 
                         out.print("<script>alert('Item Type Code Already Exist')</script>");
                         out.print("<script>history.go(-1);</script>");
                        }
                     else
					 {
                        if(serviceBus.createItemType(itemTypeCode,name,userId,inventory,costMethod,"A"))
                        out.print("<script>alert('Record successfully saved.')</script>");                                
                        out.print("<script>window.document.location='DocumentHelp.jsp?np=itemTypeDetail&itemTypeCode="+itemTypeCode+"'</script>");
                     }
                }
                else */
				{//isAutogen = Y
                           if(serviceBus.createItemType(itemTypeCode,companyCode,name,userId,inventory,costMethod,"A",categoryCode,unitMeasurment))
                           out.print("<script>alert('Record successfully saved..')</script>");                                
                           out.print("<script>window.document.location='DocumentHelp.jsp?np=itemTypeDetail&itemTypeCode="+serviceBus.auotoGenCode+"'</script>");
                    }  
           }  
            else if(itemTypeCode != null)
			{
			
                if(serviceBus.updateItemType(name,itemTypeCode,inventory,costMethod,status,categoryCode,unitMeasurment))
                 out.print("<script>alert('Record Updated successfully .')</script>");
                 out.print("<script>window.document.location='DocumentHelp.jsp?np=itemTypeDetail&itemTypeCode="+itemTypeCode+"'</script>");    
            }
			  
      }
      catch(NullPointerException e)
	    {
           response.sendRedirect("sessionTimeOut.jsp");
        }
      catch(Exception e)
	  {
          e.printStackTrace();
      }
  }

  /** Returns a short description of the servlet.
   */
  public String getServletInfo() 
  {
    return "Item Type Action Servlet";
  }

}
