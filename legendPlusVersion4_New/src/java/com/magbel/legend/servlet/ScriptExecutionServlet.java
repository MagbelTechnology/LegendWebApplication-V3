package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.HtmlUtility;

import jakarta.servlet.http.HttpSession;
import legend.admin.objects.User;
//import javafx.scene.shape.Path;
import magma.AssetRecordsBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.lang.System.out;

public class ScriptExecutionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        HtmlUtility html = new HtmlUtility();
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        AssetRecordsBean ad = null;
   	 	com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
   	 	legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
   	 	legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
        Properties prop = new Properties();
   	 	File file = new File("C:\\Property\\LegendPlus.properties");
   	 	FileInputStream input = new FileInputStream(file);
   	 	prop.load(input);
		
		String userClass = (String) request.getSession().getAttribute("UserClass");
		 String userId = (String) request.getSession().getAttribute("CurrentUser");
		 String singleApproval = prop.getProperty("singleApproval");
//			System.out.println("<<<<<<=====userId: "+userId+"    userClass: "+userClass);
			
		 if (!userClass.equals("NULL") || userClass!=null){
		 String script = request.getParameter("script");
		 String reason = request.getParameter("reason");
		 String confirm = request.getParameter("confirm");
		 String supervisor_id = request.getParameter("supervisor");
		 String tranType = request.getParameter("tran_status");
		 String scriptId = request.getParameter("scriptId");
		 String transaction_level = request.getParameter("transaction_level");
		 if ((tranType == null) || tranType.equalsIgnoreCase("NULL")){tranType =  "";}
		 if ((scriptId == null) || scriptId.equalsIgnoreCase("NULL")){scriptId =  "";}
//		 System.out.println("<<<<<<<<< tranType: " + tranType + " ======= scriptId: " + scriptId);
		 String userid = (String)session.getAttribute("CurrentUser");
		 
		 legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		 String userName = user.getUserName();
		 String branchRestrict = user.getBranchRestrict();
		 String UserRestrict = user.getDeptRestrict();
		 String departCode = user.getDeptCode();
		 String branch = user.getBranch();

		
		

		 
		 	Date getDate = new Date();  
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
		    String date= formatter.format(getDate);  
		
		 if(date != null) {
			 String yyyy = date.substring(0, 4);
				String mm = date.substring(5, 7);
				String dd = date.substring(8, 10);
				date = yyyy+"/"+mm+"/"+dd;
		 }
		 
//		 System.out.println("<<<<<<< Script: " + script + " Reason: " + reason + " Confirm:" + confirm + 
//				 " Supervisor: " + supervisor_id + " Transaction Level: " + transaction_level + " Created Date: " + date);
          try{
        	  String supervisorName = "";
    	 	  String mailAddress = "";
    	 	  
    	 	  ad = new AssetRecordsBean();
    	 	  
    	 	 List<legend.admin.objects.User>  approvelist =ad.getApprovalsId(branch,departCode,userName);
    	 	  
    	 	 int numOfTransactionLevel =  ad.getNumOfTransactionLevel("1");
    	 	  
    	 	 if(numOfTransactionLevel != 0)
   	 	  {
    	 		 
    	 		
    	 	               	   
              if(singleApproval.equalsIgnoreCase("Y"))	{
            	  if(script!=null && reason !=null && confirm!=null && !tranType.equalsIgnoreCase("R")) {
          	 		String tmpId = appHelper.getGeneratedId("am_gb_script");
                	 //  System.out.println("<<<<<<<<< tmpId: " + tmpId);
                	   String query = "insert into am_gb_script (id, user_Id, script, reason, confirm, supervior_id, Create_Date)values(?,?,?,?,?,?,?)";
                	   	con = getConnection();
                       ps = con.prepareStatement(query);
                       ps.setString(1, tmpId);
                       ps.setString(2, userId);
                       ps.setString(3, script);
                       ps.setString(4, reason);
                       ps.setString(5, confirm);
                       ps.setString(6, supervisor_id); 
                       ps.setString(7, date);
                       int i = ps.executeUpdate();
                      // System.out.println("i: >>>>>> " + i);
                       if(i>0) {  
                    	  
                    	   int recId = Integer.valueOf(tmpId);

            	  String[] pa = new String[12];
             	 //  String branchId = html.findObject("select branch from am_gb_User where User_id='"+userId+"'");
             	   String branchId = html.getCodeName("select branch from am_gb_User where User_id=?",userId);
//             	   System.out.println("=====branchId: "+branchId);
             	   //String branchCode = html.findObject("select BRANCH_CODE from am_ad_branch where BRANCH_ID='"+branchId+"'");
             	   String branchCode = html.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID=?",branchId);
//             	   System.out.println("=====branchCode: "+branchCode);
             	   tmpId = "S" + tmpId;
 					pa[0]=tmpId; pa[1]= userId; pa[2]=supervisor_id; pa[3]="0"; pa[4]= "";
 					pa[5]= "Script Execution"; pa[6]= date; pa[7]= branchCode; pa[8]="PENDING"; pa[9]="Script Execution"; pa[10]="P";pa[11]=transaction_level;
// 					System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
 					if(userId.equals("")){userId = "0";}
// 					ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
 					ad.setPendingTrans(pa,"1",recId);
// 					 String lastMTID = ad.getCurrentMtid("am_gb_script");
// 					ad.setPendingTransArchive(ad.setApprovalData(tmpId),"1",Integer.parseInt(lastMTID),recId);

 					 String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor_id);
 						//System.out.println("=====supervisor_name: "+supervisor_name);String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor);
 					 String subjectr ="Script Execution";
 					 String msgText11 ="Asset with ID: "+ recId +" for script execution is waiting for your approval.";
 					 //String supervisorName =html.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
 					 String  approvaltransId  = html.getCodeName("select transaction_id from am_asset_approval where user_id = '"+userId+"' and ASSET_ID= '"+tmpId+"' ");	
 					 //System.out.println("=====approvaltransId: "+approvaltransId);
 					 String otherparam = "scriptExecutionApproval&id="+recId+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+recId;                                                                                                                                                              
 					// System.out.println("=====otherparam: "+otherparam);
 					 mail.sendMailSupervisor(supervisor_id, subjectr, msgText11,otherparam);
 					   out.println("<script type=\"text/javascript\">");
 					   out.println("alert('Your request has been sent to "+supervisor_name+" for approval.');");
 					   out.println("location='DocumentHelp.jsp?np=scriptExecution';");
 					   out.println("</script>");
            	  
              }
            	  }
              }
        	  
        	  
        	  if(singleApproval.equalsIgnoreCase("N")) {
        		  

        		  if(script!=null && reason !=null && confirm!=null && !tranType.equalsIgnoreCase("R")) {
        			  String tmpId = appHelper.getGeneratedId("am_gb_script");
        			  String script_Id = tmpId; // For DB
        			  String displayId = "S" + tmpId;
        			  String recId = tmpId;
        		  for (User usr : approvelist) {
        		      String supervisorId = usr.getUserId();
        		      mailAddress = usr.getEmail();
        		      supervisorName = usr.getUserName();
        		      String supervisorFullName = usr.getUserFullName();

        		      

        		      try (Connection con2 = getConnection();
        		           PreparedStatement ps2 = con2.prepareStatement("INSERT INTO am_gb_script (id, user_Id, script, reason, confirm, supervior_id, Create_Date) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
        		           
        		          ps2.setString(1, script_Id);
        		          ps2.setString(2, userId);
        		          ps2.setString(3, script);
        		          ps2.setString(4, reason);
        		          ps2.setString(5, confirm);
        		          ps2.setString(6, supervisorId);
        		          ps2.setString(7, date);
        		          int i = ps2.executeUpdate();

        		          if (i > 0) {
        		             
        		              if (userId == null || userId.trim().isEmpty()) userId = "0";
        		             

        		              String[] pa = new String[] {
        		            	displayId, userId, supervisorId, "0", "", "Script Execution",
        		                  date, branch, "PENDING", "Script Execution", "P", "0"
        		              };

        		              ad.setPendingTransMultiApp(pa, "1", Integer.parseInt(recId), supervisorId, recId);
        		              comp.insertMailRecords(mailAddress, "Script Execution",
        		                  "Asset with ID: " + displayId + " for script execution is waiting for your approval.");
        		          }

        		      } catch (Exception e) {
        		          e.getMessage();
        		      }
        		  }

        		  out.println("<script type=\"text/javascript\">");
        		  out.println("alert('Your Script Execution Request has been sent for approval.');");
        		  out.println("location='DocumentHelp.jsp?np=scriptExecution';");
        		  out.println("</script>");

        	  }
   	 	  }
   	 	  }
                
//        	  System.out.println("We are here oooooo");
//              out.println("<script type=\"text/javascript\">");
//              out.println("alert('Unable To send request. Check the request sent..');");
//				 out.println("javascript: history.go(-1);");
//				 out.println("</script>");
        	 
//           if(script!=null && reason !=null && confirm!=null && !tranType.equalsIgnoreCase("R")) {
//        	   String tmpId = (new ApplicationHelper()).getGeneratedId("am_gb_script");
////        	   System.out.println("<<<<<<<<< tmpId: " + tmpId);
//        	   String query = "insert into am_gb_script (id, user_Id, script, reason, confirm, supervior_id, Create_Date)values(?,?,?,?,?,?,?)";
//        	   con = getConnection();
//               ps = con.prepareStatement(query);
//               ps.setString(1, tmpId);
//               ps.setString(2, userId);
//               ps.setString(3, script);
//               ps.setString(4, reason);
//               ps.setString(5, confirm);
//               ps.setString(6, supervisor_id); 
//               ps.setString(7, date);
//               int i = ps.executeUpdate();
//               if(i>0) {  
//            	   ad = new AssetRecordsBean();
//            	   int recId = Integer.valueOf(tmpId);
//            	   String[] pa = new String[12];
//            	 //  String branchId = html.findObject("select branch from am_gb_User where User_id='"+userId+"'");
//            	   String branchId = html.getCodeName("select branch from am_gb_User where User_id=?",userId);
////            	   System.out.println("=====branchId: "+branchId);
//            	   //String branchCode = html.findObject("select BRANCH_CODE from am_ad_branch where BRANCH_ID='"+branchId+"'");
//            	   String branchCode = html.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID=?",branchId);
////            	   System.out.println("=====branchCode: "+branchCode);
//            	   tmpId = "S" + tmpId;
//					pa[0]=tmpId; pa[1]= userId; pa[2]=supervisor_id; pa[3]="0"; pa[4]= "";
//					pa[5]= "Script Execution"; pa[6]= date; pa[7]= branchCode; pa[8]="PENDING"; pa[9]="Script Execution"; pa[10]="P";pa[11]=transaction_level;
////					System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
//					if(userId.equals("")){userId = "0";}
////					ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
//					ad.setPendingTransAdmin(pa,"77",recId,"I");
////					ad.setPendingTransArchive(pa,"75",Integer.parseInt(recId),Integer.parseInt(recId));
//					// String supervisor_name = html.findObject("select Full_Name from am_gb_User where User_id='"+supervisor_id+"'");
//					 String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor_id);
//						//System.out.println("=====supervisor_name: "+supervisor_name);String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor);
//					 String subjectr ="Script Execution";
//					 String msgText11 ="Asset with ID: "+ recId +" for script execution is waiting for your approval.";
//					 //String supervisorName =html.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
//					 String  approvaltransId  = html.getCodeName("select transaction_id from am_asset_approval where user_id = '"+userId+"' and ASSET_ID= '"+tmpId+"' ");	
//					 //System.out.println("=====approvaltransId: "+approvaltransId);
//					 String otherparam = "scriptExecutionApproval&id="+recId+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+recId;                                                                                                                                                              
//					// System.out.println("=====otherparam: "+otherparam);
//					 mail.sendMailSupervisor(supervisor_id, subjectr, msgText11,otherparam);
//					out.println("<script type=\"text/javascript\">");
//					   out.println("alert('Your request has been sent to "+supervisor_name+" for approval.');");
//					   out.println("location='DocumentHelp.jsp?np=scriptExecution';");
//					   out.println("</script>");
//					//out.print("<script>alert('Your request has been sent to "+supervisor_id+" for approval.')</script>");
//					//response.sendRedirect("DocumentHelp.jsp?np=scriptExecution");
//               }
//           }
      	 
           if(script!=null && reason !=null && confirm!=null && tranType.equalsIgnoreCase("R")) {
//        	   String tmpId = (new ApplicationHelper()).getGeneratedId("am_gb_script");
//        	   System.out.println("<<<<<<<<< scriptId: " + scriptId);
        	   String query = "UPDATE am_gb_script SET script = ?, reason = ?, confirm = ?, supervior_id = ? where id = ?";
        	   con = getConnection();
               ps = con.prepareStatement(query);
               ps.setString(1, script);
               ps.setString(2, reason);
               ps.setString(3, confirm);
               ps.setString(4, supervisor_id); 
               ps.setString(5, scriptId);
               int i = ps.executeUpdate();
               if(i>0) {
            	   ad = new AssetRecordsBean();
            	   int recId = Integer.valueOf(scriptId);
            	   String[] pa = new String[12];
            	 //  String branchId = html.findObject("select branch from am_gb_User where User_id='"+userId+"'");
            	   String branchId = html.getCodeName("select branch from am_gb_User where User_id=?",userId);
//            	   System.out.println("=====branchId: "+branchId);
            	   //String branchCode = html.findObject("select BRANCH_CODE from am_ad_branch where BRANCH_ID='"+branchId+"'");
            	   String branchCode = html.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID=?",branchId);
//            	   System.out.println("=====branchCode: "+branchCode);
					pa[0]=scriptId; pa[1]= userId; pa[2]=supervisor_id; pa[3]="0"; pa[4]= "";
					pa[5]= "Script Execution"; pa[6]= date; pa[7]= branchCode; pa[8]="PENDING"; pa[9]="Script Execution"; pa[10]="P";pa[11]=transaction_level;
//					System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
					if(userId.equals("")){userId = "0";}
//					ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
					ad.setPendingTransAdmin(pa,"77",recId,"I");
//					ad.setPendingTransArchive(pa,"75",Integer.parseInt(recId),Integer.parseInt(recId));
					// String supervisor_name = html.findObject("select Full_Name from am_gb_User where User_id='"+supervisor_id+"'");
					 String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor_id);
						//System.out.println("=====supervisor_name: "+supervisor_name);String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor);
					 String subjectr ="Script Execution";
					 String msgText11 ="Asset with ID: "+ recId +" for script execution is waiting for your approval.";
					 //String supervisorName =html.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
					 String  approvaltransId  = html.getCodeName("select transaction_id from am_asset_approval where user_id = '"+userId+"' and ASSET_ID= '"+scriptId+"' ");	
//					 System.out.println("=====approvaltransId: "+approvaltransId);
					 String otherparam = "scriptExecutionApproval&id="+recId+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+recId;                                                                                                                                                              
//					 System.out.println("=====otherparam: "+otherparam);
					 mail.sendMailSupervisor(supervisor_id, subjectr, msgText11,otherparam);
					out.println("<script type=\"text/javascript\">");
					   out.println("alert('Your request has been sent to "+supervisor_name+" for approval.');");
					   out.println("location='DocumentHelp.jsp?np=transactionStatusList&status=ACTIVE';");
					   out.println("</script>");
					//out.print("<script>alert('Your request has been sent to "+supervisor_id+" for approval.')</script>");
					//response.sendRedirect("DocumentHelp.jsp?np=scriptExecution");
               }
           }
          }catch (Exception e){
              e.getCause();
          }finally {
        	  closeConnection(con, ps);
          }
		 }
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
    private Connection getConnection() {
		Connection con = null;
		try {
//        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
//        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection ->"
					+ e.getMessage());
		}
		
		return con;
    }
    
    private void closeConnection(Connection con, Statement s)
    {
        try
        {
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error getting connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps)
    {
        try
        {
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, Statement s, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(s != null)
            {
                s.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

    private void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(ps != null)
            {
                ps.close();
            }
            if(con != null)
            {
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
        }
    }

}
