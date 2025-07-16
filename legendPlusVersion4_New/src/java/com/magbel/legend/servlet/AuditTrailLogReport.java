package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class AuditTrailLogReport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String userId = (String) request.getSession().getAttribute("CurrentUser");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
    String branch_Code = request.getParameter("initiatorSOLID");
    String branch_Id = request.getParameter("branch");
    String user = request.getParameter("user");
    String FromDate = request.getParameter("FromDate");
    String ToDate = request.getParameter("ToDate");
    if(!FromDate.equals("")){
	String yyyy = FromDate.substring(6, 10);
	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	String Tyyyy = ToDate.substring(6, 10);
	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
    System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";
//    if(report.equalsIgnoreCase("rptMenuBCRList")){fileName = branchCode+"By"+userName+"AssetRegisterListReport.xlsx";}
//    if(report.equalsIgnoreCase("rptMenuBCLDTL")){fileName = branchCode+"By"+userName+"AssetDetailReport.xlsx";}
    if(report.equalsIgnoreCase("rptMenuLOGAudit")){fileName = branchCode+"By"+userName+"AuditTrailLogReport.xlsx";}
    
    String filePath = System.getProperty("user.home")+"\\Downloads";
    System.out.println("<<<<<<filePath: "+filePath);
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	
/*	if(exists==true){
		File f1 = new File(filePath);
		if (f1.delete()) {
		    System.out.println("File " + f1.getName() + " is deleted.");
		} else {
		    System.out.println("File " + f1.getName() + " not found.");
		}
	}*/
	response.setIntHeader("Content-Length", -1);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    USER: "+user+"  branchCode: "+branchCode);
     String ColQuery = "";
     if(branch_Id.equals("0")  && user.equals("0") && FromDate.equals("") && ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and User Not Selected: "+branch_Id+"   USER: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);	     
	     ColQuery ="SELECT "
	     		+ "am_gb_User.Full_Name AS Full_Name,"
	     		+ "am_gb_company.company_name AS company_name,"
	     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "am_gb_User.User_Name AS User_Name,"
	     		+ "gb_user_login.create_date AS create_date,"
	     		+ "gb_user_login.user_id AS user_id,"
	     		+ "gb_user_login.branch_code AS branch_code,"
	     		+ "gb_user_login.mtid AS mtid,"
	     		+ "gb_user_login.time_in AS time_in,"
	     		+ "gb_user_login.time_out AS time_out,"
	     		+ "gb_user_login.workstation_name AS workstation_name,"
	     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
	     		+ "gb_user_login.session_id AS session_id "
	     		+ "FROM "
	     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
	     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id, "
	     		+ "dbo.am_gb_company am_gb_company "
	     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code "
	     		+ "ORDER BY "
	     		+ "gb_user_login.branch_code ASC,"
	     		+ "gb_user_login.user_id ASC ";       
	}      
     if(branch_Id.equals("0")  && user.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>Branch and User Not Selected: "+branch_Id+"   USER: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);	     
	     ColQuery ="SELECT "
	     		+ "am_gb_User.Full_Name AS Full_Name,"
	     		+ "am_gb_company.company_name AS company_name,"
	     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
	     		+ "am_gb_User.User_Name AS User_Name,"
	     		+ "gb_user_login.create_date AS create_date,"
	     		+ "gb_user_login.user_id AS user_id,"
	     		+ "gb_user_login.branch_code AS branch_code,"
	     		+ "gb_user_login.mtid AS mtid,"
	     		+ "gb_user_login.time_in AS time_in,"
	     		+ "gb_user_login.time_out AS time_out,"
	     		+ "gb_user_login.workstation_name AS workstation_name,"
	     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
	     		+ "gb_user_login.session_id AS session_id "
	     		+ "FROM "
	     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
	     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id, "
	     		+ "dbo.am_gb_company am_gb_company "
	     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code and gb_user_login.CREATE_DATE BETWEEN ? AND ? "
	     		+ "ORDER BY "
	     		+ "gb_user_login.branch_code ASC,"
	     		+ "gb_user_login.user_id ASC ";       
	}      
	 if(!branch_Id.equals("0")  && !user.equals("0")){	   
	   System.out.println("======>>>>>>>Category Selected: "+branch_Id+"   User: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="SELECT "
		     		+ "am_gb_User.Full_Name AS Full_Name,"
		     		+ "am_gb_company.company_name AS company_name,"
		     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "am_gb_User.User_Name AS User_Name,"
		     		+ "gb_user_login.create_date AS create_date,"
		     		+ "gb_user_login.user_id AS user_id,"
		     		+ "gb_user_login.branch_code AS branch_code,"
		     		+ "gb_user_login.mtid AS mtid,"
		     		+ "gb_user_login.time_in AS time_in,"
		     		+ "gb_user_login.time_out AS time_out,"
		     		+ "gb_user_login.workstation_name AS workstation_name,"
		     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
		     		+ "gb_user_login.session_id AS session_id "
		     		+ "FROM "
		     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
		     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id, "
		     		+ "dbo.am_gb_company am_gb_company "
		     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code and gb_user_login.branch_code = ?  and gb_user_login.User_id = ? and gb_user_login.CREATE_DATE BETWEEN ? AND ? "
		     		+ "ORDER BY "
		     		+ "gb_user_login.branch_code ASC,"
		     		+ "gb_user_login.user_id ASC ";   
		}      
	 if(!branch_Id.equals("0")  && user.equals("0")){	   
	   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   User: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="SELECT "
		     		+ "am_gb_User.Full_Name AS Full_Name,"
		     		+ "am_gb_company.company_name AS company_name,"
		     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "am_gb_User.User_Name AS User_Name,"
		     		+ "gb_user_login.create_date AS create_date,"
		     		+ "gb_user_login.user_id AS user_id,"
		     		+ "gb_user_login.branch_code AS branch_code,"
		     		+ "gb_user_login.mtid AS mtid,"
		     		+ "gb_user_login.time_in AS time_in,"
		     		+ "gb_user_login.time_out AS time_out,"
		     		+ "gb_user_login.workstation_name AS workstation_name,"
		     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
		     		+ "gb_user_login.session_id AS session_id "
		     		+ "FROM "
		     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
		     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id,"
		     		+ "dbo.am_gb_company am_gb_company "
		     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code and gb_user_login.branch_code = ? and gb_user_login.CREATE_DATE BETWEEN ? AND ? "
		     		+ "ORDER BY "
		     		+ "gb_user_login.branch_code ASC, "
		     		+ "gb_user_login.user_id ASC ";     
	     }      
   if(branch_Id.equals("0")  && !user.equals("0")){
	   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   User: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
	     ColQuery ="SELECT "
		     		+ "am_gb_User.Full_Name AS Full_Name,"
		     		+ "am_gb_company.company_name AS company_name,"
		     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
		     		+ "am_gb_User.User_Name AS User_Name,"
		     		+ "gb_user_login.create_date AS create_date,"
		     		+ "gb_user_login.user_id AS user_id,"
		     		+ "gb_user_login.branch_code AS branch_code,"
		     		+ "gb_user_login.mtid AS mtid,"
		     		+ "gb_user_login.time_in AS time_in,"
		     		+ "gb_user_login.time_out AS time_out,"
		     		+ "gb_user_login.workstation_name AS workstation_name,"
		     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
		     		+ "gb_user_login.session_id AS session_id "
		     		+ "FROM "
		     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
		     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id, "
		     		+ "dbo.am_gb_company am_gb_company "
		     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code  and gb_user_login.User_id = ? and gb_user_login.CREATE_DATE BETWEEN ? AND ? "
		     		+ "ORDER BY "
		     		+ "gb_user_login.branch_code ASC, "
		     		+ "gb_user_login.user_id ASC ";    
		}  
	 if(!branch_Id.equals("0")  && user.equals("0") && FromDate.equals("") && ToDate.equals("")){	   
		   System.out.println("======>>>>>>>Branch Selected: "+branch_Id+"   User: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
		     ColQuery ="SELECT "
			     		+ "am_gb_User.Full_Name AS Full_Name,"
			     		+ "am_gb_company.company_name AS company_name,"
			     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
			     		+ "am_gb_User.User_Name AS User_Name,"
			     		+ "gb_user_login.create_date AS create_date,"
			     		+ "gb_user_login.user_id AS user_id,"
			     		+ "gb_user_login.branch_code AS branch_code,"
			     		+ "gb_user_login.mtid AS mtid,"
			     		+ "gb_user_login.time_in AS time_in,"
			     		+ "gb_user_login.time_out AS time_out,"
			     		+ "gb_user_login.workstation_name AS workstation_name,"
			     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
			     		+ "gb_user_login.session_id AS session_id "
			     		+ "FROM "
			     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
			     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id,"
			     		+ "dbo.am_gb_company am_gb_company "
			     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code and gb_user_login.branch_code = ?  "
			     		+ "ORDER BY "
			     		+ "gb_user_login.branch_code ASC, "
			     		+ "gb_user_login.user_id ASC ";     
		     } 
	   if(branch_Id.equals("0")  && !user.equals("0") && FromDate.equals("") && ToDate.equals("")){
		   System.out.println("======>>>>>>>No Selection: "+branch_Id+"   User: "+user+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
		     ColQuery ="SELECT "
			     		+ "am_gb_User.Full_Name AS Full_Name,"
			     		+ "am_gb_company.company_name AS company_name,"
			     		+ "am_ad_branch.BRANCH_NAME AS BRANCH_NAME,"
			     		+ "am_gb_User.User_Name AS User_Name,"
			     		+ "gb_user_login.create_date AS create_date,"
			     		+ "gb_user_login.user_id AS user_id,"
			     		+ "gb_user_login.branch_code AS branch_code,"
			     		+ "gb_user_login.mtid AS mtid,"
			     		+ "gb_user_login.time_in AS time_in,"
			     		+ "gb_user_login.time_out AS time_out,"
			     		+ "gb_user_login.workstation_name AS workstation_name,"
			     		+ "gb_user_login.System_ip AS IP_ADDRESS,"
			     		+ "gb_user_login.session_id AS session_id "
			     		+ "FROM "
			     		+ "dbo.gb_user_login gb_user_login INNER JOIN dbo.am_ad_branch am_ad_branch ON gb_user_login.branch_code = am_ad_branch.BRANCH_ID "
			     		+ "INNER JOIN dbo.am_gb_User am_gb_User ON gb_user_login.user_id = am_gb_User.User_id, "
			     		+ "dbo.am_gb_company am_gb_company "
			     		+ "WHERE am_ad_branch.BRANCH_ID = gb_user_login.branch_code  and gb_user_login.User_id = ?  "
			     		+ "ORDER BY "
			     		+ "gb_user_login.branch_code ASC, "
			     		+ "gb_user_login.user_id ASC ";    
			} 
	   
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getAuditLogRecords(ColQuery,branch_Id,user,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuLOGAudit")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("User Name");
         rowhead.createCell( 2).setCellValue("Full Name");
         rowhead.createCell( 3).setCellValue("Branch Code");
         rowhead.createCell( 4).setCellValue("Branch Name");
         rowhead.createCell( 5).setCellValue("Creation Date");
         rowhead.createCell( 6).setCellValue("Time In");
         rowhead.createCell( 7).setCellValue("Time Out");
         rowhead.createCell( 8).setCellValue("Workstation Ip");

     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 

			String user_Name =  newassettrans.getOldassetId();
			String fullName =  newassettrans.getDescription();
			String branch_code =  newassettrans.getBranchCode();
			String branchName =  newassettrans.getBranchName();
			String ipAddress = newassettrans.getSystemIp();   
			String timeIn = newassettrans.getAssetUser();
			String timeOut = newassettrans.getAssetType();
			String creationDate = newassettrans.getDatepurchased() != null ? getDate(newassettrans.getDatepurchased()) : "";

			Row row = sheet.createRow((int) i);

			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(user_Name);
            row.createCell((short) 2).setCellValue(fullName);
            row.createCell((short) 3).setCellValue(branch_code);
            row.createCell((short) 4).setCellValue(branchName);
            row.createCell((short) 5).setCellValue(creationDate);
            row.createCell((short) 6).setCellValue(timeIn);
            row.createCell((short) 7).setCellValue(timeOut);
            row.createCell((short) 8).setCellValue(ipAddress);
		
            //	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     }
 }
   
    } catch (Exception e)
    {
     e.getMessage();
    } 
    //finally
   // {
//     if (out != null)
//      out.close();
   // }
   }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
   
   public String getDate(String date) {
		   //System.out.println("<<<<<<<<<<<< Date: " + date);
		   String yyyy = date.substring(0, 4);
			String mm = date.substring(5, 7);
			String dd = date.substring(8, 10);
			date = dd+"/"+mm+"/"+yyyy;
		   
			
		   return date;
	   }
}