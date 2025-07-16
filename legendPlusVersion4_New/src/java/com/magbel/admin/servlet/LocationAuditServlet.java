/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import audit.*;
import com.magbel.admin.handlers.CompanyHandler;
import com.magbel.admin.objects.Locations;

public class LocationAuditServlet extends HttpServlet {
    public LocationAuditServlet() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        response.setContentType("text/html");
        response.setDateHeader("Expires", -1);

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        AuditTrailGen  audit = new AuditTrailGen();


		String statusMessage = "";
		boolean updtst = false;


		int userID;
		 String userId = (String)session.getAttribute("CurrentUser");
		 if(userId == null) {  userID = 0; }
			else { userID = Integer.parseInt(userId);	}


		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

		String locationId = request.getParameter("locationId");
		if(locationId == null) { locationId = "not set"; 	}


        String buttSave = request.getParameter("buttSave");


         String locationCode =  request.getParameter("locationCode");
          String locationName =  request.getParameter("locationName");
          String locationStatus =  request.getParameter("locationStatus");
		  		            //            (String)session.getAttribute("CurrentUser")
		com.magbel.admin.objects.Locations  location = new  com.magbel.admin.objects.Locations();

		//location.setLocationId(locationId);
        location.setLocationCode(locationCode);
		location.setLocation(locationName);
		location.setStatus(locationStatus);
		location.setUserId(userId);
		
       com.magbel.admin.handlers.CompanyHandler   locationhandle = new   com.magbel.admin.handlers.CompanyHandler();

        try{

            if(buttSave != null)
				{
                 if(locationId.equals(""))
					{
                	 if (locationhandle.getLocationByLocCode(locationCode)!=null) {
 						out.print("<script>alert('The Location with code already exists .')</script>");
 						out.print("<script>history.go(-1);</script>");
 					} else {
						if(locationhandle.createLocation(location))
							{
								//statusMessage = "Record saved successfully";
								out.print("<script>alert('Record saved successfully.')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=locationSetup&locationId="+(locationhandle.getLocationByLocCode(locationCode)).getLocationId()+"&PC=62;'</script>");
							}
						else
						   	 {
								System.out.println("Error saving record: New record \nfor 'location'  with location name "+location.getLocation()+" could not be created");
								out.print("<script>history.back()</script>");
							}}
					}
                 if(!locationId.equals(""))
					{
                	 location.setLocationId(locationId);
						//System.out.print(locationId+"2");
						audit.select( 1, "SELECT * FROM  AM_GB_LOCATION   WHERE location_Id = '"+ locationId +"'");
						boolean isupdt = locationhandle.updateLocation(location);
						audit.select( 2, "SELECT * FROM  AM_GB_LOCATION  WHERE location_Id = '"+ locationId +"'");
						updtst = audit.logAuditTrail("AM_GB_LOCATION" ,  branchcode, userID, locationId,"","");
                        if(updtst == true)
							{
								//statusMessage = "Update on record is successfull";
								out.print("<script>alert('Update on record is successfull')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=locationSetup&locationId="+locationId+"&PC=62'</script>");
								//out.print("<script>window.location = 'manageLocations.jsp?status=A'</script>");
							}
						else
								{
								//statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								 out.print("<script>window.location = 'DocumentHelp.jsp?np=locationSetup&locationId="+locationId+"&PC=62'</script>");
								}
					}
				}
			}
		catch(Throwable e)
		{
			e.printStackTrace();
			//statusMessage = "Ensure unique record entry";
            out.print("<script>alert('Ensure unique record entry.')</script>");
			out.print("<script>history.back()</script>");
            System.err.print(e.getMessage());
        }
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return String
     */
    public String getServletInfo() {
        return "Company Audit Servlet";
    }

}
