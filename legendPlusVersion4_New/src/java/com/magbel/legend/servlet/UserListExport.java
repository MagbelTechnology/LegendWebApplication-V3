package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import legend.admin.objects.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
   
public class UserListExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
	   String userClass = (String) request.getSession().getAttribute("UserClass");
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
		mail= new EmailSmsServiceBus();
        records = new ApprovalRecords();
        String userName = request.getParameter("user");
        String start_Date = request.getParameter("FromDate");
        String end_Date = request.getParameter("ToDate");
        String filter = "";
        System.out.println("====>>userName====: "+userName+"   start_Date: "+start_Date+"    end_Date: "+end_Date);
        if(!start_Date.equals("")){start_Date = start_Date.substring(6,10)+"-"+start_Date.substring(3,5)+"-"+start_Date.substring(0,2);}
        if(!end_Date.equals("")){end_Date = end_Date.substring(6,10)+"-"+end_Date.substring(3,5)+"-"+end_Date.substring(0,2);}  
        
        System.out.println("====>>userName: "+userName+"   start_Date: "+start_Date+"    end_Date: "+end_Date);
        if((start_Date != "")&&(end_Date != ""))
        { 
        	filter = " AND a.Create_Date BETWEEN '"+start_Date+"' AND '"+end_Date+"'";
         //alert(result);
        }
        if((!userName.equals("***"))&&(start_Date != "")&&(end_Date != ""))
        {
        	filter = " AND a.User_Name ='"+userName+"' AND a.Create_Date BETWEEN '"+start_Date+"' AND '"+end_Date+"'"; 
        }
        System.out.println("====>>filter: "+filter);
//        String userName = request.getParameter("userName");
        String fileName = "LegendUserList.xls";
        String filePath = System.getProperty("user.home")+"\\Downloads";
        File tmpDir = new File(filePath);
        boolean exists = tmpDir.exists();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment; filename="+fileName+"");
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
            ad = new AssetRecordsBean();

            Report rep = new Report();
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryId: "+categoryId+"  branchCode: "+branchCode);
            String ColQuery = "";
            if(filter.equals(""))
            ColQuery ="select distinct a.user_id,a.userId, a.Supervisor_Id, a.User_Name,a.Full_Name,a.login_date,a.Login_Status,a.User_Status,a.Create_Date, a.class,a.branch_restriction,a.Approval_Limit,a.email,UserId,Branch,dept_code from am_gb_User a, am_gb_class b " +
           		 "where a.Class = b.Class_Id ";
            else{
            ColQuery ="select distinct a.user_id,a.userId, a.Supervisor_Id, a.User_Name,a.Full_Name,a.login_date,a.Login_Status,a.User_Status,a.Create_Date, a.class,a.branch_restriction,a.Approval_Limit,a.email,UserId,Branch,dept_code from am_gb_User a, am_gb_class b " +
            		 "where a.Class = b.Class_Id "+filter+" ";
            }
            System.out.println("======>>>>>>>ColQuery: "+ColQuery);
            java.util.ArrayList list =rep.getUserListExportRecords(ColQuery);
            if(list.size()!=0){
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("User List Export");
                HSSFRow rowhead = sheet.createRow((int) 0);
                rowhead.createCell((int) 0).setCellValue("User Id");
                rowhead.createCell((int) 1).setCellValue("User Name");
                rowhead.createCell((int) 2).setCellValue("Full Name");
                rowhead.createCell((int) 3).setCellValue("Branch Code");
                rowhead.createCell((int) 4).setCellValue("Branch Name");
                rowhead.createCell((int) 5).setCellValue("Department Name");
                rowhead.createCell((int) 6).setCellValue("Created By");
                rowhead.createCell((int) 7).setCellValue("Created Date");
                rowhead.createCell((int) 8).setCellValue("Login Date");
                rowhead.createCell((int) 9).setCellValue("Login Status");
                rowhead.createCell((int) 10).setCellValue("Branch Restriction)");
                rowhead.createCell((int) 11).setCellValue("Approval Limit Max Amount");
                rowhead.createCell((int) 12).setCellValue("email Address");
                rowhead.createCell((int) 13).setCellValue("Last Login Date");
                rowhead.createCell((int) 14).setCellValue("User Status");
                rowhead.createCell((int) 15).setCellValue("Security Class");
                rowhead.createCell((int) 16).setCellValue("Approved By"); 
                int i = 1;

//     System.out.println("<<<<<<list.size(): "+list.size());
                for(int k=0;k<list.size();k++)
                {
                    legend.admin.objects.User  users = (legend.admin.objects.User)list.get(k);
                    String userId =  users.getUserId();
                    String fullName = users.getUserFullName();
                    String branchId = users.getBranch();
                    String branchRestrict = users.getBranchRestrict();
                    String createDate = users.getCreateDate();
                    String createdBy = users.getCreatedBy();
                    String loginStatus = users.getLoginStatus();
                    if(loginStatus.equals("0")){loginStatus = "Successfully Sign on";}
                    if(loginStatus.equals("1")){loginStatus = "User already connected to Legend";}
                    if(loginStatus.equals("3")){loginStatus = "You have exceded logon limits";}
                    if(loginStatus.equals("4")){loginStatus = "You can not Logon to Legend by this Time";}
                    String loginDate = users.getLastLogindate();
                    String userStatus = users.getUserStatus();
                    String deptCode = users.getDeptCode();
                    String limitApproval = users.getApprvLimit();
                    String email = users.getEmail();
                    String lastLoginDate = users.getLastLogindate();
                    String userloginName = users.getUserName();
                    String classId = users.getUserClass();
                    String approvedBy = users.getIsSupervisor();
                    if(deptCode==null){deptCode = "0";}
//                    System.out.println("=====>>>>deptCode: "+deptCode);}
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
                    String approvalLimitAmt = records.getCodeName("select Max_Amount from Approval_Limit where Level_Code = "+limitApproval+" ");
                    String createdByName = records.getCodeName("select Full_Name from am_gb_User where User_Id = "+createdBy+" ");
                    String securityClass = records.getCodeName("select Class_Name from am_gb_class where Class_Id = "+classId+" ");
                    String branchCode = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+" ");
                    String branchName = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = "+branchId+" ");
                    String test = "select DEPT_NAME from am_ad_department where DEPT_CODE = '"+deptCode+"' ";
 //                   System.out.println("=====>>>>Test: "+test);
                    String deptName = records.getCodeName("select DEPT_NAME from am_ad_department where DEPT_CODE = '"+deptCode+"' ");
//                    String postedBy = newassettrans.getPostedBy();
//                    String transactionDate = newassettrans.getTransDate();
//                    String initiatorId = newassettrans.getInitiatorId();
//                    String supervisorId = newassettrans.getSupervisorId();
                    approvedBy = records.getCodeName("select USER_NAME from am_gb_User where User_Id = "+approvedBy+" ");

                    HSSFRow row = sheet.createRow((int) i);

                    row.createCell((int) 0).setCellValue(userId);
                    row.createCell((int) 1).setCellValue(userloginName);
                    row.createCell((int) 2).setCellValue(fullName);
                    row.createCell((int) 3).setCellValue(branchCode);
                    row.createCell((int) 4).setCellValue(branchName);
                    row.createCell((int) 5).setCellValue(deptName);
                    row.createCell((int) 6).setCellValue(createdByName);
                    row.createCell((int) 7).setCellValue(createDate);
                    row.createCell((int) 8).setCellValue(loginDate);
                    row.createCell((int) 9).setCellValue(loginStatus);
                    row.createCell((int) 10).setCellValue(branchRestrict);
                    row.createCell((int) 11).setCellValue(approvalLimitAmt);
                    row.createCell((int) 12).setCellValue(email);
                    row.createCell((int) 13).setCellValue(lastLoginDate);
                    row.createCell((int) 14).setCellValue(userStatus);
                    row.createCell((int) 15).setCellValue(securityClass);
                    row.createCell((int) 16).setCellValue(approvedBy);
                    i++;
                }
                OutputStream stream = response.getOutputStream();
//              new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
              workbook.write(stream);
              stream.close();
              System.out.println("Data is saved in excel file.");

            }
        }
        } catch (Exception e)
        {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally
        {
//     if (out != null)
//      out.close();
        }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
}