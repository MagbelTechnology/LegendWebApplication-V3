/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;

import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.HtmlUtility;

import magma.AssetRecordsBean;
import magma.BarCodeHistoryBean;
import magma.net.dao.MagmaDBConnection;
/**
 *
 * @author ayomide matanmi
 */
@SuppressWarnings("serial")
public class SupervisorRerouteListServlet extends HttpServlet {
	 legend.admin.handlers.SecurityHandler sechanle = new legend.admin.handlers.SecurityHandler();
	 com.magbel.util.HtmlUtility htmlUtil  = new com.magbel.util.HtmlUtility();
	 com.magbel.legend.bus.ApprovalRecords aprecords  = new com.magbel.legend.bus.ApprovalRecords();
	 com.magbel.util.ApplicationHelper appHelper  = new com.magbel.util.ApplicationHelper();
	 legend.admin.handlers.CompanyHandler comp  = new legend.admin.handlers.CompanyHandler();
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            MagmaDBConnection dbConnection = new MagmaDBConnection();
            Timestamp created_date =  dbConnection.getDateTime(new java.util.Date());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	
    } 

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	
    	   try {
    	Connection con = null ;
        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        HtmlUtility html = new HtmlUtility();
        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        AssetRecordsBean ad = new AssetRecordsBean();
        HttpSession session = request.getSession();
        Properties prop = new Properties();
		 File file = new File("C:\\Property\\LegendPlus.properties");
		 FileInputStream input = new FileInputStream(file);
		 prop.load(input);
       
        String userid = (String)session.getAttribute("CurrentUser");
        String userId = request.getParameter("user_id");
    	String super_id = request.getParameter("super_id");
    	//String id = request.getParameter("id");
    	String TranId = request.getParameter("tranId");
    	String reroute_reason = request.getParameter("reroute_reason");
    	String approved_by = request.getParameter("approved_by");
    	String requested_by = request.getParameter("requested_by");
    	String new_supervisor = request.getParameter("new_supervisor");
    	String supervisor = request.getParameter("supervisor");
    	String s = request.getParameter("size");
    	String [] status = request.getParameterValues("status");
    	 String tmpId = (new ApplicationHelper()).getGeneratedId("am_supervisor_reroute");
    	 tmpId = 'R'+tmpId;
    	 String transaction_level = request.getParameter("transaction_level");
    	String assetId="";
		String tranType="";
		String desc="";
		String tranId="";
		int resCount=0;
		String date= String.valueOf(created_date);
		
		 String singleApproval = prop.getProperty("singleApproval");
		 
		 legend.admin.objects.User user = sechanle.getUserByUserID(userid);
		 String userName = user.getUserName();
		 System.out.println("userName: " + userName);
		 String branchRestrict = user.getBranchRestrict();
		 String UserRestrict = user.getDeptRestrict();
		 String departCode = user.getDeptCode();
		 System.out.println("departCode: " + departCode);
		 String branch = user.getBranch();
		 System.out.println("branch: " + branch);
		 
		 ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		 
		 System.out.println("approvelist: " + approvelist.size());
	
    	
//    	System.out.println("<<< super_id: " + super_id + " userId: " + userId + " reroute_reason: " + reroute_reason
//    			+ " approved_by: " + approved_by + " requested_by: " + requested_by + " supervisor: " + supervisor +
//    			" created_date: " + date + " new_supervisor: " + new_supervisor +
//    			" \n");
    	 
    	for(String selectedItem : status) {
//   	System.out.println("<<<<< status length: " + status.length);	
  //  		System.out.println("<<<<< Detail: " + selectedItem);
        	
    		String asset_id = request.getParameter("asset_id"+selectedItem);
        	String tran_type = request.getParameter("tran_type"+selectedItem);
        	String description = request.getParameter("description"+selectedItem);
        	String tran_id = request.getParameter("transaction_Id"+selectedItem);
        	
    		assetId = asset_id;
    		tranType = tran_type;
    		desc = description;
    		tranId = tran_id;
    		
    		//System.out.println("<<<< assetId: " + assetId + " tranType: " + tranType + " description: "
   			//	+ desc + " tranId: " + tranId);
    		
    		 String count = html.getCodeName("select COUNT(*) from am_supervisor_reroute where  transaction_id=?",tranId);
    		//	System.out.println("<<<<< count: " + count);	
    		 
    		 //int userCount = Integer.parseInt(count)
    			if(Integer.parseInt(count) == 0) {
    			String query="Insert into am_supervisor_reroute (transaction_id, user_id, super_id, new_super_id, approved_by, requested_by, reroute_reason, "
    					+ "		asset_id, tran_type, description, created_date, supervisor, batch_id) "
    					+ "values (?,?,?,?,?,?,?,?,?,?,?,?, ?)";
    			
              
                	 con = getConnection();
					ps = con.prepareStatement(query);
					ps.setString(1, tranId);
					ps.setString(2, userId);
					ps.setString(3, super_id);
					ps.setString(4, new_supervisor);
					ps.setString(5, approved_by);
					ps.setString(6, requested_by);
					ps.setString(7, reroute_reason);
					ps.setString(8, assetId);
					ps.setString(9, tranType);
					ps.setString(10, desc);
					ps.setString(11, date);
					ps.setString(12, supervisor);
					ps.setString(13, tmpId);
					ps.executeUpdate();
					resCount=resCount + 1;
    	} else if (Integer.parseInt(count) > 0) {
    		String query="update am_supervisor_reroute set user_id=?, super_id=?, new_super_id=?, "
    				+ "approved_by=?, requested_by=?, reroute_reason=?, asset_id=?, created_date=?, supervisor=?, batch_id=? where transaction_id=? ";
    		 	con = getConnection();
				ps = con.prepareStatement(query);
				ps.setString(1, userId);
				ps.setString(2, super_id);
				ps.setString(3, new_supervisor);
				ps.setString(4, approved_by);
				ps.setString(5, requested_by);
				ps.setString(6, reroute_reason);
				ps.setString(7, assetId);
				ps.setString(8, date);
				ps.setString(9, supervisor);
				ps.setString(10, tmpId);
				ps.setString(11, tranId);
				ps.executeUpdate();
				resCount=resCount + 1;
				//System.out.println("<<<<< Update successful... ");
    	}
    	}
    	
    	//System.out.println("<<<<< resCount: " + resCount);
         if(resCount == status.length) {
        	 
        	 
        	 if(singleApproval.equalsIgnoreCase("Y")){
			 ad = new AssetRecordsBean();
//        	   int recId = Integer.valueOf(tmpId);
        	   int recId = 0;
        	   String[] pa = new String[12];
        	   String branchId = html.getCodeName("select branch from am_gb_User where User_id=?",userId);
        	 //  System.out.println("=====branchId: "+branchId);
        	   //String branchCode = html.findObject("select BRANCH_CODE from am_ad_branch where BRANCH_ID='"+branchId+"'");
        	   String branchCode = html.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID=?",branchId);
        	//   System.out.println("=====branccreateManageUser2hCode: "+branchCode);
				pa[0]=tmpId; pa[1]= userId; pa[2]=supervisor; pa[3]="0"; pa[4]= "";
				pa[5]= "Supervisor Reroute"; pa[6]= date; pa[7]= branchCode; pa[8]="PENDING"; pa[9]="Supervisor Reroute"; pa[10]="P";
			//	System.out.println("=====recId: "+recId+"     =====userId: "+userId);	
				if(userId.equals("")){userId = "0";}
//				ad.setPendingTrans(ad.setApprovalData(venId),"1",Integer.parseInt(venId));
				ad.setPendingTransAdmin(pa,"79",recId,"I");
			 String supervisor_name = html.getCodeName("select Full_Name from am_gb_User where User_id=?",supervisor);
			 String subjectr ="Supervisor Reroute";
			 String msgText11 ="Asset with ID: "+ recId +" for Reroute is waiting for your approval.";
			 //String supervisorName =html.getCodeName(" SELECT full_name from am_gb_user where user_id=?",supervisor);
			 String  approvaltransId  = html.getCodeName("select transaction_id from am_asset_approval where user_id = '"+userId+"' and ASSET_ID= '"+tmpId+"' ");	
			 //System.out.println("=====approvaltransId: "+approvaltransId);
			 String otherparam = "manageSupervisorRerouteApproval&id="+recId+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+recId;                                                                                                                                                              
			// System.out.println("=====otherparam: "+otherparam);
			 mail.sendMailSupervisor(supervisor, subjectr, msgText11,otherparam);
			out.println("<script type=\"text/javascript\">");
			   out.println("alert('Your request has been sent to "+supervisor_name+" for approval.');");
			   out.println("location='DocumentHelp.jsp?np=manageSupervisorReroute';");
			   out.println("</script>");
        	 }else {
     	  		String mtid =  appHelper.getGeneratedId("am_asset_approval");
	 	  		//System.out.println("mtid >>>>>> " + mtid);
//	 	   		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
	 	   	 for(int i=0;i<approvelist.size();i++)
	 	     {  
	 		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(i);   	 
	 			String supervisorId =  usr.getUserId();
	 			//System.out.println("supervisorId >>>>>> " + supervisorId);
	 			String mailAddress = usr.getEmail();
	 			String supervisorName = usr.getUserName();
	 			String supervisorfullName = usr.getUserFullName();
	 			 ad = new AssetRecordsBean();
//	        	   int recId = Integer.valueOf(tmpId);
	        	   int recId = 0;
	        	   String[] pa = new String[12];
	        	   String branchId = html.getCodeName("select branch from am_gb_User where User_id=?",userId);
	        	 //  System.out.println("=====branchId: "+branchId);
	        	   
	        	   String branchCode = html.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID=?",branchId);
	 			pa[0]=tmpId; pa[1]= userId; pa[2]=supervisorId; pa[3]="0"; pa[4]= "";
				pa[5]= "Supervisor Reroute"; pa[6]= date; pa[7]= branchCode; pa[8]="PENDING"; pa[9]="Supervisor Reroute"; pa[10]="P";
		
	 	  		 ad.setPendingTransMultiApp(pa,"79",recId,supervisorId,mtid);
	 	  	//	System.out.println("We are here 2 >>>>>> ");
	 			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
	 			String subjectr ="LEGEND SUPERVISOR REROUTE";
	 			String msgText11 ="Asset with ID: "+ recId +" for Reroute is waiting for your approval.";
	  	//	String  approvaltransId  = html.getCodeName("select transaction_id from am_asset_approval where user_id = '"+userId+"' and ASSET_ID= '"+tmpId+"' ");		  
	
	  		
	 				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
	 	     	}	
	 	   
          
	 	   	out.print("<script>");
	 	   out.print("alert('Supervisor Reroute submitted for approval');");
		    out.print("window.location='DocumentHelp.jsp?np=manageSupervisorReroute';");
		 	
		 	
		 	out.print("</script>");
        	 }
		}
    	   } catch (Exception e) {
				e.printStackTrace();
			}
    	   finally {
    		   closeConnection(con, ps);
    	   }
    	
    }

    private Connection getConnection() {
  		Connection con = null;
  		try {
//          	if(con==null){
                  Context initContext = new InitialContext();
                  String dsJndi = "java:/legendPlus";
                  DataSource ds = (DataSource) initContext.lookup(
                  		dsJndi);
                  con = ds.getConnection();
//          	}
  		} catch (Exception e) {
  			System.out.println("WARNING: Error 1 getting connection ->"
  					+ e.getMessage());
  		}
  		
  		return con;
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

}
