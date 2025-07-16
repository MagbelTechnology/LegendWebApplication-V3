/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.servlet;

import com.magbel.util.CheckIntegerityContraint;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

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
public class RegionAuditServlet extends HttpServlet {
    public RegionAuditServlet() {
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

        //String type = request.getParameter("TYPE");
		String statusMessage = "";
		boolean updtst = false;
        //java.sql.Date dt = new java.sql.Date();
        AuditTrailGen  audit = new AuditTrailGen();
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");

     	int loginID;
		 String loginId = (String)session.getAttribute("CurrentUser");
		 if(loginId == null) {  loginID = 0; }
			else { loginID = Integer.parseInt(loginId);	}


		String branchcode = (String)session.getAttribute("UserCenter");
		if(branchcode == null) { branchcode = "not set";	}

        String buttSave = request.getParameter("buttSave");
com.magbel.admin.handlers.AdminHandler admin = new com.magbel.admin.handlers.AdminHandler();
        String acronym = request.getParameter("regionAcronym");
        if(acronym != null){
            acronym = acronym.toUpperCase();
        }
com.magbel.admin.objects.Region region = new com.magbel.admin.objects.Region();

        String regionId = request.getParameter("regionId");
        //if(regionId == null){regionId = "";}

		//String regionStatus = request.getParameter("regionStatus");
        //String user = (String)session.getAttribute("CurrentUser");

		String regionCode = request.getParameter("regionCode");
		String regionName = request.getParameter("regionName");
		String regionAcronym  = acronym;
		String regionAddress  = request.getParameter("regionAddress");
		String regionPhone = request.getParameter("regionPhone");
		String regionFax = request.getParameter("regionFax");
		String regionStatus = request.getParameter("regionStatus");
		String userId =(String)session.getAttribute("CurrentUser");

       region.setRegionCode(regionCode);
       region.setRegionName(regionName);
       region.setRegionAcronym(regionAcronym);
       region.setRegionAddress(regionAddress);
       region.setRegionPhone(regionPhone);
       region.setRegionFax(regionFax);
       region.setRegionStatus(regionStatus);
       region.setUserId(userId);
        try{

            if(buttSave != null)
				{
				 if(regionId.equals(""))
					{
                    //System.out.print(regionId+"3");
					 if (admin.getRegionByCode(regionCode) != null) {
							out.print("<script>alert('The Region code already exists .');</script>");
							out.print("<script>history.go(-1);</script>");
						} else {

							if (admin.createRegion(region)) {
								out.print("<script>alert('Record saved successfully.');</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=regionSetup&regionId="
												+ admin.getRegionByCode(regionCode).getRegionId()
												+ "&PC=5';</script>");
							}
						}
					}
                 if(!regionId.equals(""))
					{

                	 region.setRegionId(regionId);
					CheckIntegerityContraint intCont = new CheckIntegerityContraint();
          if(intCont.checkReferenceConstraint("AM_AD_BRANCH","REGION_CODE",regionCode,regionStatus))
            {
             out.print("<script>alert('Region Code is being referenced,Integerity Constraint would be violated.')</script>");
			out.print("<script>window.location = 'DocumentHelp.jsp?np=regionSetup&regionId="+regionId+"&PC=5'</script>");
            }
	 else{
                    //System.out.print(regionId+"2");
					audit.select( 1, "SELECT * FROM  AM_AD_REGION   WHERE region_Id = '"+ regionId +"'");
                    boolean isupdt = admin.updateRegion(region);
					audit.select( 2, "SELECT * FROM  AM_AD_REGION  WHERE region_Id = '"+ regionId +"'");
					 updtst = audit.logAuditTrail("AM_AD_REGION" ,  branchcode, loginID, regionId,"","");
                        if(updtst == true)
							{
								//statusMessage = "Update on record is successfull";
								out.print("<script>alert('Update on record is successfull')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=regionSetup&regionId="+regionId+"&PC=5'</script>");
							//out.print("<script>window.location = 'manageRegions.jsp?status=A'</script>");
							}
						else
							{
								//statusMessage = "No changes made on record";
								 out.print("<script>alert('No changes made on record')</script>");
								out.print("<script>window.location = 'DocumentHelp.jsp?np=regionSetup&regionId="+regionId+"&PC=5'</script>");
							}
					}
				}}
			}
			catch(Throwable e)
			{

				//statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=regionSetup&regionId="+regionId+"&PC=5'</script>");
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
        return "Region Audit Servlet";
    }

}
