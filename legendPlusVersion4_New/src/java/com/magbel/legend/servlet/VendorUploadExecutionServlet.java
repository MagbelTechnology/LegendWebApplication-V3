package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager;
import com.magbel.util.HtmlUtility;

import audit.AuditTrailGen;
//import javafx.scene.shape.Path;
import magma.AssetRecordsBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.objects.Staffs;
import legend.admin.objects.Vendor;

import javax.sql.DataSource;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class VendorUploadExecutionServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null ;
        PreparedStatement ps = null;
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String statusMessage = "";
		boolean updtst = false;


		ApprovalRecords aprecords = new ApprovalRecords();
		String userClass = (String)request.getSession().getAttribute("UserClass");
		int loginID;
		String loginId = (String) session.getAttribute("CurrentUser");
		String password0 = request.getParameter("password");
		
		if (loginId == null) {
			loginID = 0;
		} else {
			loginID = Integer.parseInt(loginId);
		}

		String branchcode = (String) session.getAttribute("UserCenter");
		if (branchcode == null) {
			branchcode = "not set";
		}

		 if (!userClass.equals("NULL") || userClass!=null){
		boolean tokenRequired =false;
		String ColQuery ="select *from am_ad_vendor_Upload where RCNo not in (select RCNo from am_ad_vendor)";
		  java.util.ArrayList list =getUploadVendorSqlRecords(ColQuery);
//			try {		

//				  System.out.println("list.size() ============================ "+list.size());
				  int listValue = list.size();
				for(int k=0;k<listValue;k++)
		     {
			System.out.println("K ============== "+k+"   list.size() =========== "+list.size());	
			legend.admin.objects.Vendor  vendor = (legend.admin.objects.Vendor)list.get(k);    	 					
//			String buttSave = request.getParameter("buttSave");
			// String buttAssg = request.getParameter("buttAssg");
//	       String RowId = request.getParameter("userId");
//	       String RecordId = usr.getUserName();
			 String vendor_name = vendor.getVendorName();
				String contact_person = vendor.getContactPerson();
			   String contact_address = vendor.getContactAddress();
			   String vendor_phone = vendor.getVendorPhone();
			   String account_number = vendor.getAccountNumber();
			 String rc_no = vendor.getRcNo();
			String tin_no = vendor.getTin();
         
         
         vendor = new legend.admin.objects.Vendor();
         vendor.setVendorName(vendor_name);
         vendor.setContactPerson(contact_person);
         vendor.setContactAddress(contact_address);
         vendor.setVendorPhone(vendor_phone);
         vendor.setAccountNumber(account_number);
         vendor.setRcNo(rc_no);
         vendor.setTin(tin_no);
		
	       
         //  System.out.println("staffId ================= "+staffId+"  fullname: "+fullname+"  createdBy: "+user_Id);
				
           boolean value = VendorUpload(vendor);

           if(value == true) {
        	   out.print("<script>alert('Upload Done Successfully!!!')</script>");
				out.print("<script>window.location = 'DocumentHelp.jsp?np=manageVendors&status=ACTIVE'</script>");
           }
		     }
				
		 }
		 out.print("<script>alert('Upload unsuccessful..')</script>");
		out.print("<script>window.location = 'DocumentHelp.jsp?np=manageStaffsList'</script>");
		/*	}  catch (Throwable e) {
				e.printStackTrace();
				// statusMessage = "Ensure unique record entry";
				out.print("<script>alert('Ensure unique record entry.')</script>");
				System.err.print(e.getMessage());
				out.print("<script>window.location = 'DocumentHelp.jsp?np=systemUsers&userId="
						+ userId + "&PC=11'</script>");
			}	*/
	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doPost(request, response);
    }
    
    
    public java.util.ArrayList getUploadVendorSqlRecords(String query)
    {
    	java.util.ArrayList _list = new java.util.ArrayList();
//         System.out.println("====query in getUploadUserSqlRecords-----> "+query);
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	 legend.admin.objects.Vendor vendor = null;
    	try {
    		    c = getConnection();
    			s = c.createStatement();
    			rs = s.executeQuery(query);
    			while (rs.next())
    			   {				
    				 String vendor_name = rs.getString("Vendor_Name");
    	  				String contact_person = rs.getString("Contact_Person");
    	  			   String contact_address = rs.getString("Contact_Address");
    	  			   String vendor_phone = rs.getString("Vendor_Phone");
    	  			   String account_number = rs.getString("account_number");
    	  			 String rc_no = rs.getString("RCNo");
    	  			String tin_no = rs.getString("TIN");
                    
                    
                    vendor = new legend.admin.objects.Vendor();
                    vendor.setVendorName(vendor_name);
                    vendor.setContactPerson(contact_person);
                    vendor.setContactAddress(contact_address);
                    vendor.setVendorPhone(vendor_phone);
                    vendor.setAccountNumber(account_number);
                    vendor.setRcNo(rc_no);
                    vendor.setTin(tin_no);
                  
                    _list.add(vendor);				
    			   }
    	 }    
    				 catch (Exception e)
    					{
    						e.printStackTrace();
    					}
    					finally
    					{
    						closeConnection(c, s, rs);
    					}
    	return _list;
    }
    
    public boolean VendorUpload(Vendor vendor) {
    	 String status ="ACTIVE";
    	 Date getDate = new Date();  
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
		    String date= formatter.format(getDate);  
		
		 if(date != null) {
			 String yyyy = date.substring(0, 4);
				String mm = date.substring(5, 7);
				String dd = date.substring(8, 10);
				date = yyyy+"/"+mm+"/"+dd;
		 }

		String UPDATE_QUERY = "insert into am_ad_vendor (Vendor_Id, Vendor_Code, Vendor_Name,Contact_Person,"
				+ "Contact_Address,Vendor_Phone,account_number,RCNo,TIN,Vendor_Status,Create_date) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		String UPDATE_QUERY2 = "delete from am_ad_vendor_Upload ";
		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		 ApplicationHelper helper = new ApplicationHelper(); 
         String vendorID = helper.getGeneratedId("am_ad_vendor");
		
		try {
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, vendorID);
			ps.setString(2, vendorID);
			ps.setString(3, vendor.getVendorName());
			ps.setString(4, vendor.getContactPerson());
			ps.setString(5, vendor.getContactAddress());
			ps.setString(6, vendor.getVendorPhone());
			ps.setString(7, vendor.getAccountNumber());
			ps.setString(8, vendor.getRcNo());
			ps.setString(9, vendor.getTin());
			ps.setString(10, status);
			ps.setString(11, date);
			done = ps.execute();
			//System.out.println("<<<<====Query: "+UPDATE_QUERY+"   done: "+done);
			closeConnection(con, ps);
			con = getConnection();
			ps = con.prepareStatement(UPDATE_QUERY2);
			done = (ps.executeUpdate() != -1);
			//System.out.println("<<<<====DELETE_QUERY in deleteUserUpload: "+UPDATE_QUERY2+"   done: "+done);
			closeConnection(con, ps);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;
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
