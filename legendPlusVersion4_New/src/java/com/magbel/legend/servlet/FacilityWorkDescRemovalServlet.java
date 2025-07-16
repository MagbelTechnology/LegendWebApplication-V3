package com.magbel.legend.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;
import com.magbel.legend.bus.GenerateList;
import com.magbel.legend.bus.FacilityManager;

import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class AjaxRequisitionServlet
 */
public class FacilityWorkDescRemovalServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/xml";
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
        FacilityManager facilityManager = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacilityWorkDescRemovalServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userClass = (String)request.getSession().getAttribute("UserClass");
	
try {
	 if (!userClass.equals("NULL") || userClass!=null){                  
                facilityManager = new FacilityManager();
                 GenerateList genList = new GenerateList();

                 
                int workId = request.getParameter("workid") == null?0:Integer.parseInt(request.getParameter("workid"));
                String ReqnID = request.getParameter("ReqnID");

                String isReposted = (String)request.getSession().getAttribute("isReposted");


                if(isReposted != null && isReposted.equalsIgnoreCase("Y")){
                String deleteWorkDescr_query ="delete from FM_WORK_DESCRIPTION_TEMP where work_id = "+workId;
                genList.updateTable(deleteWorkDescr_query);

                }else if(isReposted != null && isReposted.equalsIgnoreCase("YY")){
                String deleteWorkDescr_query ="delete from FM_WORK_DESCRIPTION_TEMP where work_id = "+workId;
                genList.updateTable(deleteWorkDescr_query);

               String updateMaintenanceDueQuery=" update FM_MAITENANCE_DUE set work_description_updated ='N' where ReqnID='"+ ReqnID+"'";
                genList.updateTable(updateMaintenanceDueQuery);

                }
                else{
                ArrayList workDesList= facilityManager.getWorkDescriptionsBackup(workId,"");

                
                if(workDesList != null && workDesList.size()>0){
                    System.out.println(">>>>>>>>>>> in description removal servlet workDesList "+workDesList.size() );
                System.out.println(">>>>>>>>>>> in description removal servlet workId "+workId );
                    System.out.println("<<<<< about to recreate work descriptions for when incomplete edit is done>>>");

                    facilityManager.creatWorkDescriptionRepost(workDesList,workId);

                String updateMaintenanceDue_query ="update FM_MAITENANCE_DUE set No_Work_Description = "+workDesList.size()+" where ReqnID ='"+ReqnID+"'";
                genList.updateTable(updateMaintenanceDue_query);

                String deleteWorkDescr_query ="delete from FM_WORK_DESCRIPTION_TEMP where work_id = "+workId;
                genList.updateTable(deleteWorkDescr_query);
                }else{

                System.out.println(" No entry in FM_WORK_DESCRIPTION_TEMP");
                }


                }

                request.getSession().setAttribute("isReposted",null);
                 request.getSession().setAttribute("WorkDescBackupTaken",null);



}
            } catch (Exception e) {
                 System.out.println("Error occurred in FacilityWorkDescRemovalServlet Get method " +e);
            }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	try {

                facilityManager = new FacilityManager();
                 GenerateList genList = new GenerateList();

                String IDType = request.getParameter("ID");
                int workId = request.getParameter("workid") == null?0:Integer.parseInt(request.getParameter("workid"));
                String ReqnID = request.getParameter("ReqnID");

                String isReposted = (String)request.getSession().getAttribute("isReposted");


                if(isReposted != null && isReposted.equalsIgnoreCase("Y")){
                String deleteWorkDescr_query ="delete from FM_WORK_DESCRIPTION_TEMP where work_id="+workId;
                genList.updateTable(deleteWorkDescr_query);

                }
                else{
                ArrayList workDesList= facilityManager.getWorkDescriptionsBackup(workId,"");

               
                if(workDesList != null && workDesList.size()>0){
                     System.out.println(">>>>>>>>>>> in description removal servlet workDesList "+workDesList.size() );
                System.out.println(">>>>>>>>>>> in description removal servlet workId "+workId );
                    System.out.println("<<<<< about to recreate work descriptions for when incomplete edit is done>>>");


                facilityManager.creatWorkDescriptionRepost(workDesList,workId);

                String updateMaintenanceDue_query ="update FM_MAITENANCE_DUE set No_Work_Description = "+workDesList.size()+" where ReqnID ='"+ReqnID+"'";
                genList.updateTable(updateMaintenanceDue_query);

                String deleteWorkDescr_query ="delete from FM_WORK_DESCRIPTION_TEMP where work_id="+workId;
                genList.updateTable(deleteWorkDescr_query);


                
                }else{
                    System.out.println(" No entry in FM_WORK_DESCRIPTION_TEMP");

                }
                
                }


		request.getSession().setAttribute("isReposted",null);
                request.getSession().setAttribute("WorkDescBackupTaken",null);


               

            
            
            } catch (Exception e) {
                 System.out.println("Error occurred in FacilityWorkDescRemovalServlet  Post method " +e);
            }
	
            
	}

	
	 
}








