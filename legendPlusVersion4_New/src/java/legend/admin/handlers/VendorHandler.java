package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import legend.ConnectionClass;

import com.magbel.util.DataConnect;
import com.magbel.util.ApplicationHelper;

public class VendorHandler { 
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date date;
    com.magbel.util.DatetimeFormat df;
    public VendorHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllVendors() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Vendor vendor = null;
        String query = "SELECT Vendor_ID,VendorBranchId,Vendor_Code,Vendor_Name"
                       + ",Contact_Person,Contact_Address"
                       + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                       + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                       + ",account_number ,Vendor_Status,User_ID"
                       + ",Create_date ,account_type "
                       + " FROM am_ad_vendor";

      

        try (Connection c = getConnection();
   	         PreparedStatement s = c.prepareStatement(query)) {
         try(ResultSet  rs = s.executeQuery(query)){
            while (rs.next()) {
                String vendorId = rs.getString("Vendor_ID");
                int vendorBranchId = rs.getInt("VendorBranchId");
                String vendorCode = rs.getString("Vendor_Code");
                String vendorName = rs.getString("Vendor_Name");
                String contactPerson = rs.getString("Contact_Person");
                String contactAddress = rs.getString("Contact_Address");
                String vendorPhone = rs.getString("Vendor_Phone");
                String vendorFax = rs.getString("Vendor_fax");
                String vendorEmail = rs.getString("Vendor_email");
                String vendorState = rs.getString("Vendor_State");
                String aquisitionVendor = rs.getString("Acquisition_Vendor");
                String maintenanceVendor = rs.getString("Maintenance_Vendor");
                String accountNumber = rs.getString("account_number");
                String vendorStatus = rs.getString("Vendor_Status");
                String userId = rs.getString("User_ID");
                String createdate = sdf.format(rs.getDate("Create_date"));
                String accountType = rs.getString("account_type");
//                String vendorProvince = rs.getString("vendor_province");
                vendor = new legend.admin.objects.Vendor();
                vendor.setVendorId(vendorId);
				vendor.setVendorCode(vendorCode);
				vendor.setVendorName(vendorName);
				vendor.setContactPerson(contactPerson);
				vendor.setContactAddress(contactAddress);
				vendor.setVendorPhone(vendorPhone);
				vendor.setVendorFax(vendorFax);
				vendor.setVendorEmail(vendorEmail);
				vendor.setVendorState(vendorState);
				vendor.setAquisitionVendor(aquisitionVendor);
				vendor.setMaintenanceVendor(maintenanceVendor);
				vendor.setAccountNumber(accountNumber);
				vendor.setVendorStatus(vendorStatus);
				vendor.setUserId(userId);
				vendor.setCreatedate(createdate);
				vendor.setAccountType(accountType);
//				vendor.setVendorProvince(vendorProvince);
				vendor.setVendorBranchId(vendorBranchId);

                _list.add(vendor);
            }
         }

        } catch (Exception e) {
            e.printStackTrace();
        }

       
        return _list;

    }

    public java.util.ArrayList getVendorByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.Vendor vendor = null;
        String query = "SELECT Vendor_ID,Vendor_Code,Vendor_Name,VendorBranchId"
                       + ",Contact_Person,Contact_Address"
                       + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                       + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                       + ",account_number ,Vendor_Status,User_ID"
                       + ",Create_date ,account_type "
                       + " FROM am_ad_vendor WHERE Vendor_ID IS NOT NULL ";

        query = query+filter;
//        query = query + filter;

//        System.out.println("################the query for getting vendor is " +query );

        try (Connection c = getConnection();
   	         PreparedStatement s = c.prepareStatement(query)) {
         try(ResultSet  rs = s.executeQuery(query)){
            while (rs.next()) {
                String vendorId = rs.getString("Vendor_ID");
                int vendorBranchId = rs.getInt("VendorBranchId");
                String vendorCode = rs.getString("Vendor_Code");
                String vendorName = rs.getString("Vendor_Name");
                String contactPerson = rs.getString("Contact_Person");
                String contactAddress = rs.getString("Contact_Address");
                String vendorPhone = rs.getString("Vendor_Phone");
                String vendorFax = rs.getString("Vendor_fax");
                String vendorEmail = rs.getString("Vendor_email");
                String vendorState = rs.getString("Vendor_State");
                String aquisitionVendor = rs.getString("Acquisition_Vendor");
                String maintenanceVendor = rs.getString("Maintenance_Vendor");
                String accountNumber = rs.getString("account_number");
                String vendorStatus = rs.getString("Vendor_Status");
                String userId = rs.getString("User_ID");
                String createdate = sdf.format(rs.getDate("Create_date"));
                String accountType = rs.getString("account_type");
//                String vendorProvince = rs.getString("vendor_province");
                vendor = new legend.admin.objects.Vendor();
                vendor.setVendorId(vendorId);
				vendor.setVendorCode(vendorCode);
				vendor.setVendorName(vendorName);
				vendor.setContactPerson(contactPerson);
				vendor.setContactAddress(contactAddress);
				vendor.setVendorPhone(vendorPhone);
				vendor.setVendorFax(vendorFax);
				vendor.setVendorEmail(vendorEmail);
				vendor.setVendorState(vendorState);
				vendor.setAquisitionVendor(aquisitionVendor);
				vendor.setMaintenanceVendor(maintenanceVendor);
				vendor.setAccountNumber(accountNumber);
				vendor.setVendorStatus(vendorStatus);
				vendor.setUserId(userId);
				vendor.setCreatedate(createdate);
				vendor.setAccountType(accountType);
//				vendor.setVendorProvince(vendorProvince);
				vendor.setVendorBranchId(vendorBranchId);
                _list.add(vendor);
            }
         }

        } catch (Exception e) {
            e.printStackTrace();
        }

       
       // System.out.println("the size of the array list is ================== "+_list.size());
        return _list;

    }



    public legend.admin.objects.Vendor getVendorByVendorID(String vendid) {
        legend.admin.objects.Vendor vendor = null;
        String query = "SELECT Vendor_ID,VendorBranchId,Vendor_Code,Vendor_Name"
                       + ",Contact_Person,Contact_Address"
                       + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                       + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                       + ",account_number ,Vendor_Status,User_ID"
                       + ",Create_date ,account_type, VENDOR_CATEGORY,SERVICE_TYPE,RCNo,TIN "
                       + " FROM am_ad_vendor WHERE Vendor_ID= ?";

     
//		System.out.println("query in getVendorByVendorID: "+query); 
        try (Connection c = getConnection();
      	         PreparedStatement s = c.prepareStatement(query)) {
            s.setString(1, vendid);
         try(ResultSet   rs = s.executeQuery()){
            while (rs.next()) {
                String vendorId = rs.getString("Vendor_ID");
                int vendorBranchId = rs.getInt("VendorBranchId");
                String vendorCode = rs.getString("Vendor_Code");
                String vendorName = rs.getString("Vendor_Name");
                String contactPerson = rs.getString("Contact_Person");
                String contactAddress = rs.getString("Contact_Address");
                String vendorPhone = rs.getString("Vendor_Phone");
                String vendorFax = rs.getString("Vendor_fax");
                String vendorEmail = rs.getString("Vendor_email");
                String vendorState = rs.getString("Vendor_State");
                String aquisitionVendor = rs.getString("Acquisition_Vendor");
                String maintenanceVendor = rs.getString("Maintenance_Vendor");
                String accountNumber = rs.getString("account_number");
                String vendorStatus = rs.getString("Vendor_Status");
                String userId = rs.getString("User_ID");
                String createdate = sdf.format(rs.getDate("Create_date"));
                String accountType = rs.getString("account_type");
                String vendorServiceType = rs.getString("SERVICE_TYPE");
                String vendorCategory= rs.getString("VENDOR_CATEGORY");
                String rcNo= rs.getString("RCNo");
                String tin= rs.getString("TIN");
                vendor = new legend.admin.objects.Vendor();
                vendor.setVendorId(vendorId);
				vendor.setVendorCode(vendorCode);
				vendor.setVendorName(vendorName);
				vendor.setContactPerson(contactPerson);
				vendor.setContactAddress(contactAddress);
				vendor.setVendorPhone(vendorPhone);
				vendor.setVendorFax(vendorFax);
				vendor.setVendorEmail(vendorEmail);
				vendor.setVendorState(vendorState);
				vendor.setAquisitionVendor(aquisitionVendor);
				vendor.setMaintenanceVendor(maintenanceVendor);
				vendor.setAccountNumber(accountNumber);
				vendor.setVendorStatus(vendorStatus);
				vendor.setUserId(userId);
				vendor.setCreatedate(createdate);
				vendor.setAccountType(accountType);
				vendor.setVendorServiceType(vendorServiceType);
				vendor.setVendorBranchId(vendorBranchId);
				vendor.setVendorCategory(vendorCategory);
				vendor.setRcNo(rcNo);
				vendor.setTin(tin);

            }
         }
        } catch (Exception e) {
            e.printStackTrace();
        }

       
        return vendor;

    }


    public legend.admin.objects.Vendor getVendorByVendorCode(String vendcode) {
        legend.admin.objects.Vendor vendor = null;
        String query = "SELECT Vendor_ID,VendorBranchId,Vendor_Code,Vendor_Name"
                       + ",Contact_Person,Contact_Address"
                       + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                       + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                       + ",account_number ,Vendor_Status,User_ID"
                       + ",Create_date ,account_type,VENDOR_CATEGORY "
                       + " FROM am_ad_vendor WHERE Vendor_Code= ? ";


        try (Connection c = getConnection();
      	         PreparedStatement s = c.prepareStatement(query)) {
            s.setString(1, vendcode);
         try(ResultSet  rs = s.executeQuery()){
            while (rs.next()) {
                String vendorId = rs.getString("Vendor_ID");
                int vendorBranchId = rs.getInt("VendorBranchId");
                String vendorCode = rs.getString("Vendor_Code");
                String vendorName = rs.getString("Vendor_Name");
                String contactPerson = rs.getString("Contact_Person");
                String contactAddress = rs.getString("Contact_Address");
                String vendorPhone = rs.getString("Vendor_Phone");
                String vendorFax = rs.getString("Vendor_fax");
                String vendorEmail = rs.getString("Vendor_email");
                String vendorState = rs.getString("Vendor_State");
                String aquisitionVendor = rs.getString("Acquisition_Vendor");
                String maintenanceVendor = rs.getString("Maintenance_Vendor");
                String accountNumber = rs.getString("account_number");
                String vendorStatus = rs.getString("Vendor_Status");
                String userId = rs.getString("User_ID");
                String createdate = sdf.format(rs.getDate("Create_date"));
                String accountType = rs.getString("account_type");
//                String vendorProvince = rs.getString("vendor_province");
                String vendorCategory= rs.getString("VENDOR_CATEGORY");
                vendor = new legend.admin.objects.Vendor();
                vendor.setVendorId(vendorId);
				vendor.setVendorCode(vendorCode);
				vendor.setVendorName(vendorName);
				vendor.setContactPerson(contactPerson);
				vendor.setContactAddress(contactAddress);
				vendor.setVendorPhone(vendorPhone);
				vendor.setVendorFax(vendorFax);
				vendor.setVendorEmail(vendorEmail);
				vendor.setVendorState(vendorState);
				vendor.setAquisitionVendor(aquisitionVendor);
				vendor.setMaintenanceVendor(maintenanceVendor);
				vendor.setAccountNumber(accountNumber);
				vendor.setVendorStatus(vendorStatus);
				vendor.setUserId(userId);
				vendor.setCreatedate(createdate);
				vendor.setAccountType(accountType);
//				vendor.setVendorProvince(vendorProvince);
				vendor.setVendorBranchId(vendorBranchId);
				vendor.setVendorCategory(vendorCategory);
                
            }
         }

        } catch (Exception e) {
            e.printStackTrace();
        }

       
        return vendor;

    }


    private Connection getConnection() throws Exception {
        DataConnect dc = new DataConnect("legendPlus");
        return dc.getConnection();
    }
    private void closeConnection(Connection con, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }

    }

    private void closeConnection(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }

    }

    /**
     *
     * @param con Connection
     * @param s Statement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, Statement s, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    /**
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    private boolean executeQuery(String query) {
        
        boolean done = false;
        try (Connection c = getConnection();
   	         PreparedStatement ps = c.prepareStatement(query)) {
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } 
        return done;
    }


    public String createVendorTmp(legend.admin.objects.Vendor vendor) {

       
        boolean done = false;
        String result = "";
/*        String query = "INSERT INTO am_ad_vendorTmp"
                       + "(Vendor_Code,Vendor_Name"
                       + ",Contact_Person,Contact_Address"
                       + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                       + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                       + ",account_number ,Vendor_Status,User_ID"
                       + ",Create_date ,account_type,Vendor_ID,VendorBranchId,VENDOR_CATEGORY,SERVICE_TYPE)"
                       + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
        String query = "INSERT INTO am_ad_vendorTmp"
                + "(Vendor_Id,Vendor_Code,Vendor_Name"
                + ",Contact_Person,Contact_Address"
                + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                + ",account_number ,Vendor_Status,User_ID"
                + ",Create_date ,account_type,VendorBranchId,VENDOR_CATEGORY,SERVICE_TYPE,RECORD_TYPE,vendor_date,RCNo,TIN)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection c = getConnection();
   	         PreparedStatement ps = c.prepareStatement(query)) {
          ApplicationHelper helper = new ApplicationHelper();
          String vendorID = helper.getGeneratedId("am_ad_vendorTmp");

          ps.setInt(1, Integer.parseInt(vendorID));
          ps.setString(2, vendor.getVendorCode());
          ps.setString(3, vendor.getVendorName());
          ps.setString(4, vendor.getContactPerson());
          ps.setString(5, vendor.getContactAddress());
          ps.setString(6, vendor.getVendorPhone());
          ps.setString(7, vendor.getVendorFax());
          ps.setString(8, vendor.getVendorEmail());
          ps.setString(9, vendor.getVendorState());
          ps.setString(10, vendor.getAquisitionVendor());
          ps.setString(11, vendor.getMaintenanceVendor());
          ps.setString(12, vendor.getAccountNumber());
          ps.setString(13, vendor.getVendorStatus());
          ps.setString(14, vendor.getUserId());
          ps.setDate(15, df.dateConvert(new java.util.Date()));
          ps.setString(16, vendor.getAccountType());
          ps.setInt(17, vendor.getVendorBranchId());
          ps.setString(18, vendor.getVendorCategory());
          ps.setString(19, vendor.getVendorServiceType());
          ps.setString(20, "I");
          System.out.println("df.dateConvert(vendor.getCreatedate())===: "+df.dateConvert(vendor.getCreatedate()));
          ps.setDate(21, df.dateConvert(vendor.getCreatedate()));
          System.out.println("vendor.getRcNo()===: "+vendor.getRcNo());
          ps.setString(22, vendor.getRcNo());
          ps.setString(23, vendor.getTin());
          done = (ps.executeUpdate()!=-1);

            if(done) result = vendorID;

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query in createVendorTmp ->" +
                               e.getMessage());
        } 
        return result;

    } 

    public java.util.ArrayList getVendorByStatus(String status){
    	//String filter = " AND Vendor_Status='" + status + "'order by Vendor_Name";
        String filter = " AND Vendor_Status='" + status + "' order by Vendor_Code";
    	java.util.ArrayList _list = getVendorByQuery(filter);
    	return _list;    	
    } 
  
    public java.util.ArrayList getVendorByStatus(String status, String branchId, String branchRestriction){
    	String filter =  "";
 //   	 System.out.println("<<<<<<<<branchId: "+branchId+"   branchRestriction: "+branchRestriction+"    status: "+status);
    	//String filter = " AND Vendor_Status='" + status + "'order by Vendor_Name";
    	if(branchRestriction.equalsIgnoreCase("Y")){filter = " AND VendorBranchId = "+branchId+" " + status + " ";}
    	else{filter = " " + status + " ";}
//    	 System.out.println("<<<<<<<<filter: "+filter);
    	java.util.ArrayList _list = getVendorByQuery(filter);
    	return _list;    	
    }  
    
    
    public boolean updateVendor(legend.admin.objects.Vendor vendor) {


        boolean done = false;

        String query = "UPDATE am_ad_vendor"
                       +" SET Vendor_Code = ?,Vendor_Name = ?"
                       +", Contact_Person = ?,Contact_Address = ?,Vendor_Phone = ?"
                       +" , Vendor_fax = ?,Vendor_email = ?,Vendor_State = ?"
                       +" , Acquisition_Vendor = ?,Maintenance_Vendor = ?,account_number = ?"
                       +" , Vendor_Status = ?"
                       +" , account_type = ?,VendorBranchId = ?,VENDOR_CATEGORY = ?,SERVICE_TYPE = ?,RCNo = ?,TIN = ? "
                       +" WHERE Vendor_ID =?";

        try (Connection c = getConnection();
   	         PreparedStatement ps = c.prepareStatement(query)) {

			
            ps.setString(1, vendor.getVendorCode());
            ps.setString(2, vendor.getVendorName());
            ps.setString(3, vendor.getContactPerson());
            ps.setString(4, vendor.getContactAddress());
            ps.setString(5, vendor.getVendorPhone());
            ps.setString(6, vendor.getVendorFax());
            ps.setString(7, vendor.getVendorEmail());
            ps.setString(8, vendor.getVendorState());
            ps.setString(9, vendor.getAquisitionVendor());
            ps.setString(10, vendor.getMaintenanceVendor());
            ps.setString(11, vendor.getAccountNumber());
            ps.setString(12, vendor.getVendorStatus());
            ps.setString(13, vendor.getAccountType());
 //           ps.setString(14, vendor.getVendorProvince());
            ps.setInt(14, vendor.getVendorBranchId());
            ps.setString(15, vendor.getVendorCategory());
            ps.setString(16, vendor.getVendorServiceType());
            ps.setString(17, vendor.getRcNo());
            ps.setString(18, vendor.getTin());
            ps.setString(19, vendor.getVendorId());
            
            done = (ps.executeUpdate()!=-1);
            
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query in getVendorByStatus->" +
                               e.getMessage());
        } 
        return done;

    }

    public boolean requireAccttype()  {
    	
    	String query = "select req_accttype from am_ad_legacy_sys_config";
    	boolean flag = false;
    	 try (Connection c = getConnection();
    	         PreparedStatement stmt = c.prepareStatement(query)) {
    	try(ResultSet rs = stmt.executeQuery()){
        rs.next();
        flag = rs.getString(1).trim().equalsIgnoreCase("Y");
    	} 
    	 }
    	catch(Exception ex){
    		System.out.println("Exception occurred while trying to retrieve requireAccttype ... " + ex.getMessage());    		
    	}
    	
        return flag;
    }

     public boolean accountValidOld(String accountNumber)
     {


    	boolean flag = false;
        String query = "select oldacct from custom.acctmap where oldacct = '"+accountNumber+"' ";
        //ResultSet rs = null;
        //con = getConnectionOracle();
        System.out.println(query);
        try (Connection c = getConnectionOracle();
   	         PreparedStatement stmt = c.prepareStatement(query)) {
        try(ResultSet rs = stmt.executeQuery()){
            if (rs.next()) {
                 flag = true;
            }
        }
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
        return flag;
    }
     public String accountValidNew(String accountNumber)  
     {
    	 
    	String account = "Invalid";
    	String query = "select newacct from custom.acctmap where newacct = '"+accountNumber+"' ";
    	  try (Connection c = getConnectionOracle();
    	   	         PreparedStatement stmt = c.prepareStatement(query)) {
    	        try(ResultSet rs = stmt.executeQuery()){
         while (rs.next())
	{
        account=rs.getString("newacct");
         }
    	}
    	  }
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
        return account ;
    }


     private Connection getConnectionOracle() throws SQLException, NamingException {
    	    Context ctx = new InitialContext();
    	    DataSource ds = (DataSource) ctx.lookup("java:/FinacleDataHouse");
    	    return ds.getConnection();
    	}
      public String accountValid(String accountNumber)
     {
    	String account="Invalid";
    	System.out.println("<<<<<<<<<accountNumber in accountValid: "+accountNumber);
    	try{
            //check digit size
            int len = accountNumber.trim().length();
    	if(len==10)
        {
            // System.out.println("10 digit new account checking gam");
            //if == 10 check gam
            account=AccountCheck(accountNumber);
        }
    	else{
              //  System.out.println("old account checking mapping accout");
            //else
    		if(accountValidOld(accountNumber))
                {
                 //   System.out.println("old available get new");
                    account=accountNew(accountNumber);
  //                  System.out.println("new account ="+account);
                }
                else 
                {
               //     System.out.println("old account checking gam ");
                 //    System.out.println("old available not available checking gam");
                     account=AccountCheck(accountNumber); 
                   //old  account=accountValidNew(accountNumber);
 //                    System.out.println("new account2 ="+account);
                }
            }
    	}
    	catch(Exception ex)
        {
                 account="Invalid";
    		ex.printStackTrace();
              
    	}

        return account;
    }
      public String accountNew(String accountNumber) throws SQLException, NamingException
     {
    		
    	  System.out.println("<<<<<<<<<accountNumber in accountNew: "+accountNumber);
        String account  = "Invalid";
        String query = "select newacct from custom.acctmap where oldacct='"+accountNumber+"' ";
        System.out.println("<<<<<<<<<query in accountNew: "+query);
//        con = getConnectionOracle();

        try (Connection c = getConnectionOracle();
	   	         PreparedStatement stmt = c.prepareStatement(query)) {
	        try(ResultSet rs = stmt.executeQuery()){
            if (rs.next()) {    
                 account = rs.getString("newacct");
                 if(account==null || account.equalsIgnoreCase("")){account="Invalid";}
            }
           
    	}
        }
    	
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
        return account;
      }

       public String AccountCheck(String acctNo)
			 {
    	   System.out.println("<<<<<<<<<acctNo in AccountCheck: "+acctNo);
			       String accountNumber = "" ;
					//String query = " SELECT FORACID FROM tbaadm.GAM  WHERE FORACID = '"+acctNo+"' ";
//			     String query = " SELECT FORACID+'#'+ACCT_NAME FROM tbaadm.GAM  WHERE FORACID = '"+acctNo+"' ";
//			     String query = " SELECT FORACID||'#'||ACCT_NAME FROM tbaadm.GAM  WHERE FORACID = '"+acctNo+"' ";
			     String query = " SELECT FORACID FROM tbaadm.GAM  WHERE FORACID = '"+acctNo+"' ";
//					System.out.println("<<<<<<<<<query in AccountCheck: "+query);
//					Connection c = null;
//					ConnectionClass connection = null; 
//					ResultSet rs = null;
//					PreparedStatement ps = null;
					 try (Connection c = getConnectionOracle();
				   	         PreparedStatement ps = c.prepareStatement(query)) {
			        ps.setString(1, acctNo);
					try(ResultSet rs = ps.executeQuery()){
					if (rs.next()) {
						accountNumber = rs.getString("FORACID");
					}else accountNumber="Invalid";
					System.out.println("<<<<<<<<<accountNumber in AccountCheck: "+accountNumber);
					
					}
					} catch (Exception e) {
						e.printStackTrace();
					}

					
					return accountNumber;
					
				}

       public boolean updateVendorTmp(legend.admin.objects.Vendor vendor) {

         
           boolean done = false;
           String result = "";
           String query = "INSERT INTO am_ad_vendorTmp"
                          + "(Vendor_Id,Vendor_Code,Vendor_Name"
                          + ",Contact_Person,Contact_Address"
                          + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                          + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                          + ",account_number ,Vendor_Status,User_ID"
                          + ",Create_date ,account_type,VendorBranchId,VENDOR_CATEGORY,SERVICE_TYPE,RECORD_TYPE,vendor_date)"
                          + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

           try (Connection c = getConnection();
      	         PreparedStatement ps = c.prepareStatement(query)) {
             ApplicationHelper helper = new ApplicationHelper();
             String vendorID = helper.getGeneratedId("am_ad_vendorTmp");
//             System.out.println("=====Create Date in updateVendorTmp: "+vendor.getCreatedate());
//             System.out.println("=====Create Date in updateVendorTmp>: "+df.dateConvert(vendor.getCreatedate()));
               ps.setInt(1, Integer.parseInt(vendorID));
               ps.setString(2, vendor.getVendorCode());
               ps.setString(3, vendor.getVendorName());
               ps.setString(4, vendor.getContactPerson());
               ps.setString(5, vendor.getContactAddress());
               ps.setString(6, vendor.getVendorPhone());
               ps.setString(7, vendor.getVendorFax());
               ps.setString(8, vendor.getVendorEmail());
               ps.setString(9, vendor.getVendorState());
               ps.setString(10, vendor.getAquisitionVendor());
               ps.setString(11, vendor.getMaintenanceVendor());
               ps.setString(12, vendor.getAccountNumber());
               ps.setString(13, vendor.getVendorStatus());
               ps.setString(14, vendor.getUserId());
               ps.setDate(15, df.dateConvert(new java.util.Date()));
               ps.setString(16, vendor.getAccountType());
               ps.setInt(17, vendor.getVendorBranchId());
               ps.setString(18, vendor.getVendorCategory());
               ps.setString(19, vendor.getVendorServiceType());
               ps.setString(20, "U");
               ps.setDate(21, df.dateConvert(vendor.getCreatedate()));
               done = (ps.executeUpdate()!=-1);

//               if(done) result = vendorID;

           } catch (Exception e) {
               System.out.println("WARNING:Error executing Query in updateVendortmp ->" +
                                  e.getMessage());
           } 
           
           return done;

       }

       private String SUBSTRING(String createdate, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createVendor(legend.admin.objects.Vendor vendor)
       {
          
           String result;
           String query;
           boolean done = false;
           result = "";
           query = "INSERT INTO am_ad_vendor(Vendor_Id,Vendor_Code,Vendor_Name,Contact_Person,Contact_Address," +
   "Vendor_Phone ,Vendor_fax,Vendor_email,Vendor_State ,Acquisition_Vendor,Maintenan" +
   "ce_Vendor,account_number ,Vendor_Status,User_ID,Create_date ,account_type," +
   "VendorBranchId,VENDOR_CATEGORY,SERVICE_TYPE,RCNo,TIN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?" +
   ",?,?,?,?,?,?,?,?)"
   ;
           try (Connection c = getConnection();
      	         PreparedStatement ps = c.prepareStatement(query)) {
               ApplicationHelper helper = new ApplicationHelper();
               String vendorID = helper.getGeneratedId("am_ad_vendor");
               String createDate = vendor.getCreatedate();
//               System.out.println("======= Vendor Date: "+vendor.getCreatedate()+"   vendorID: "+vendorID);
               if(createDate == ""){createDate = String.valueOf(df.dateConvert(new Date()));}
               ps.setInt(1, Integer.parseInt(vendorID));
//               ps.setString(2, vendor.getVendorCode());
               ps.setString(2, vendorID);
               ps.setString(3, vendor.getVendorName());
               ps.setString(4, vendor.getContactPerson());
               ps.setString(5, vendor.getContactAddress());
               ps.setString(6, vendor.getVendorPhone());
               ps.setString(7, vendor.getVendorFax());
               ps.setString(8, vendor.getVendorEmail());
               ps.setString(9, vendor.getVendorState());
               ps.setString(10, vendor.getAquisitionVendor());
               ps.setString(11, vendor.getMaintenanceVendor());
               ps.setString(12, vendor.getAccountNumber());
               ps.setString(13, vendor.getVendorStatus());
               ps.setString(14, vendor.getUserId());
               ps.setString(15, createDate);
               ps.setString(16, vendor.getAccountType());
//               ps.setInt(17, Integer.parseInt(vendorID));
               ps.setInt(17, vendor.getVendorBranchId());
               ps.setString(18, vendor.getVendorCategory());
               ps.setString(19, vendor.getVendorServiceType());
               ps.setString(20, vendor.getRcNo());
               ps.setString(21, vendor.getTin());
                done = ps.executeUpdate() != -1;
               if(done)
               {
                   result = vendorID;
               }
           
       } catch (Exception e) {
           System.out.println("WARNING:Error executing Query in createVendor ->" +
                              e.getMessage());
       } 
       return result;
       }

       public legend.admin.objects.Vendor getVendorByVendorTmpID(String vendid) {
           legend.admin.objects.Vendor vendor = null;
           String query = "SELECT Vendor_ID,VendorBranchId,Vendor_Code,Vendor_Name"
                          + ",Contact_Person,Contact_Address"
                          + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                          + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                          + ",account_number ,Vendor_Status,User_ID"
                          + ",Create_date ,account_type, VENDOR_CATEGORY,SERVICE_TYPE,RCNo,TIN "
                          + " FROM am_ad_vendorTMP WHERE Vendor_ID= ?";

        
//           Statement s = null;
       
//   System.out.println("query in getVendorByVendorTmpID: "+query); 
           try (Connection c = getConnection();
      	         PreparedStatement s = c.prepareStatement(query)) {
               s.setString(1, vendid);
            try(ResultSet  rs = s.executeQuery()){
               while (rs.next()) {
                   String vendorId = rs.getString("Vendor_ID");
                   int vendorBranchId = rs.getInt("VendorBranchId");
                   String vendorCode = rs.getString("Vendor_Code");
                   String vendorName = rs.getString("Vendor_Name");
                   String contactPerson = rs.getString("Contact_Person");
                   String contactAddress = rs.getString("Contact_Address");
                   String vendorPhone = rs.getString("Vendor_Phone");
                   String vendorFax = rs.getString("Vendor_fax");
                   String vendorEmail = rs.getString("Vendor_email");
                   String vendorState = rs.getString("Vendor_State");
                   String aquisitionVendor = rs.getString("Acquisition_Vendor");
                   String maintenanceVendor = rs.getString("Maintenance_Vendor");
                   String accountNumber = rs.getString("account_number");
                   String vendorStatus = rs.getString("Vendor_Status");
                   String userId = rs.getString("User_ID");
                   String createdate = sdf.format(rs.getDate("Create_date"));
                   String accountType = rs.getString("account_type");
                   String vendorServiceType = rs.getString("SERVICE_TYPE");
                   String vendorCategory= rs.getString("VENDOR_CATEGORY");
                   String rcNo= rs.getString("RCNO");
                   String tin= rs.getString("TIN");
                   vendor = new legend.admin.objects.Vendor();
                   vendor.setVendorId(vendorId);
   				vendor.setVendorCode(vendorCode);
   				vendor.setVendorName(vendorName);
   				vendor.setContactPerson(contactPerson);
   				vendor.setContactAddress(contactAddress);
   				vendor.setVendorPhone(vendorPhone);
   				vendor.setVendorFax(vendorFax);
   				vendor.setVendorEmail(vendorEmail);
   				vendor.setVendorState(vendorState);
   				vendor.setAquisitionVendor(aquisitionVendor);
   				vendor.setMaintenanceVendor(maintenanceVendor);
   				vendor.setAccountNumber(accountNumber);
   				vendor.setVendorStatus(vendorStatus);
   				vendor.setUserId(userId);
   				vendor.setCreatedate(createdate);
   				vendor.setAccountType(accountType);
   				vendor.setVendorServiceType(vendorServiceType);
   				vendor.setVendorBranchId(vendorBranchId);
   				vendor.setVendorCategory(vendorCategory);
   				vendor.setRcNo(rcNo);
   				vendor.setTin(tin);

               }
            }

           } catch (Exception e) {
               e.printStackTrace();
           }

          
           return vendor;

       }

       public legend.admin.objects.Vendor getVendorByVendorIDTMP(String vendid) {
           legend.admin.objects.Vendor vendor = null;
           String query = "SELECT Vendor_ID,VendorBranchId,Vendor_Code,Vendor_Name"
                          + ",Contact_Person,Contact_Address"
                          + ",Vendor_Phone ,Vendor_fax,Vendor_email"
                          + ",Vendor_State ,Acquisition_Vendor,Maintenance_Vendor"
                          + ",account_number ,Vendor_Status,User_ID"
                          + ",Create_date ,account_type, VENDOR_CATEGORY,SERVICE_TYPE,RCNo,TIN "
                          + " FROM am_ad_vendorTmp WHERE Vendor_Id= ?";

     
//   		System.out.println("query in getVendorByVendorID: "+query); 
           try (Connection c = getConnection();
      	         PreparedStatement s = c.prepareStatement(query)) {
               s.setString(1, vendid);
            try(ResultSet rs = s.executeQuery()){
               while (rs.next()) {
                   String vendorId = rs.getString("Vendor_ID");
                   int vendorBranchId = rs.getInt("VendorBranchId");
                   String vendorCode = rs.getString("Vendor_Code");
                   String vendorName = rs.getString("Vendor_Name");
                   String contactPerson = rs.getString("Contact_Person");
                   String contactAddress = rs.getString("Contact_Address");
                   String vendorPhone = rs.getString("Vendor_Phone");
                   String vendorFax = rs.getString("Vendor_fax");
                   String vendorEmail = rs.getString("Vendor_email");
                   String vendorState = rs.getString("Vendor_State");
                   String aquisitionVendor = rs.getString("Acquisition_Vendor");
                   String maintenanceVendor = rs.getString("Maintenance_Vendor");
                   String accountNumber = rs.getString("account_number");
                   String vendorStatus = rs.getString("Vendor_Status");
                   String userId = rs.getString("User_ID");
                   String createdate = sdf.format(rs.getDate("Create_date"));
                   String accountType = rs.getString("account_type");
                   String vendorServiceType = rs.getString("SERVICE_TYPE");
                   String vendorCategory= rs.getString("VENDOR_CATEGORY");
                   String rcNo= rs.getString("RCNo");
                   String tin= rs.getString("TIN");
                   vendor = new legend.admin.objects.Vendor();
                   vendor.setVendorId(vendorId);
   				vendor.setVendorCode(vendorCode);
   				vendor.setVendorName(vendorName);
   				vendor.setContactPerson(contactPerson);
   				vendor.setContactAddress(contactAddress);
   				vendor.setVendorPhone(vendorPhone);
   				vendor.setVendorFax(vendorFax);
   				vendor.setVendorEmail(vendorEmail);
   				vendor.setVendorState(vendorState);
   				vendor.setAquisitionVendor(aquisitionVendor);
   				vendor.setMaintenanceVendor(maintenanceVendor);
   				vendor.setAccountNumber(accountNumber);
   				vendor.setVendorStatus(vendorStatus);
   				vendor.setUserId(userId);
   				vendor.setCreatedate(createdate);
   				vendor.setAccountType(accountType);
   				vendor.setVendorServiceType(vendorServiceType);
   				vendor.setVendorBranchId(vendorBranchId);
   				vendor.setVendorCategory(vendorCategory);
   				vendor.setRcNo(rcNo);
   				vendor.setTin(tin);

               }
            }

           } catch (Exception e) {
               e.printStackTrace();
           }

           
           return vendor;

       }

       public boolean deleteVendorRec(String vendorCode)
       {
          
           String NOTIFY_QUERY;
           NOTIFY_QUERY = "delete from  am_ad_vendor  WHERE Vendor_Code = ? ";
           boolean done;
           System.out.println("==NOTIFY_QUERY in deleteVendorRec: "+NOTIFY_QUERY);
           try (Connection c = getConnection();
      	         PreparedStatement ps = c.prepareStatement(NOTIFY_QUERY)) {
               ps.executeUpdate();
               done = true;
           } catch (Exception ex) {
               done = false;
               System.out.println("WARNING: cannot delete deleteVendorRec+" + ex.getMessage());
           } 
           
           return done;
       }

       public boolean VendorRecApproval(String vendorCode)
       {
          
           String NOTIFY_QUERY;
           NOTIFY_QUERY = "Update am_asset_approval set process_status = 'A' WHERE Asset_Id = ? ";
           boolean done;
           try (Connection c = getConnection();
        	         PreparedStatement ps = c.prepareStatement(NOTIFY_QUERY)) {
               ps.setString(1, vendorCode);
               ps.executeUpdate();
               done = true;
           } catch (Exception ex) {
               done = false;
               System.out.println("WARNING: cannot VendorRecApproval+" + ex.getMessage());
           } 
           
           return done;
       }

       public String createVendorFromTmp(legend.admin.objects.Vendor vendor)
       {
          
           String result;
           String query;
           boolean done = false;
           result = "";
           query = "INSERT INTO am_ad_vendor(Vendor_Id,Vendor_Code,Vendor_Name,Contact_Person,Contact_Address," +
   "Vendor_Phone ,Vendor_fax,Vendor_email,Vendor_State ,Acquisition_Vendor,Maintenan" +
   "ce_Vendor,account_number ,Vendor_Status,User_ID,Create_date ,account_type," +
   "VendorBranchId,VENDOR_CATEGORY,SERVICE_TYPE,RCNo,TIN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
   ",?,?,?,?,?,?,?)"
   ;
           try (Connection c = getConnection();
      	         PreparedStatement ps = c.prepareStatement(query)) {
               ApplicationHelper helper = new ApplicationHelper();
               String vendorID = helper.getGeneratedId("am_ad_vendor");
               String createDate = vendor.getCreatedate();
               System.out.println("======= Vendor Date: "+vendor.getCreatedate()+"  getVendorCode: "+vendor.getVendorCode());
               if(createDate == "null" || createDate == ""){createDate = String.valueOf(df.dateConvert(new Date()));}
               ps.setInt(1, Integer.parseInt(vendor.getVendorId()));
               ps.setString(2, vendor.getVendorCode());
               ps.setString(3, vendor.getVendorName());
               ps.setString(4, vendor.getContactPerson());
               ps.setString(5, vendor.getContactAddress());
               ps.setString(6, vendor.getVendorPhone());
               ps.setString(7, vendor.getVendorFax());
               ps.setString(8, vendor.getVendorEmail());
               ps.setString(9, vendor.getVendorState());
               ps.setString(10, vendor.getAquisitionVendor());
               ps.setString(11, vendor.getMaintenanceVendor());
               ps.setString(12, vendor.getAccountNumber());
               ps.setString(13, vendor.getVendorStatus());
               ps.setString(14, vendor.getUserId());
               ps.setDate(15, df.dateConvert(createDate));
               ps.setString(16, vendor.getAccountType());
               ps.setInt(17, vendor.getVendorBranchId());
               ps.setString(18, vendor.getVendorCategory());
               ps.setString(19, vendor.getVendorServiceType());
               ps.setString(20, vendor.getRcNo());
               ps.setString(21, vendor.getTin());
                done = ps.executeUpdate() != -1;
               if(done)
               {
                   result = vendor.getVendorId();
               }
           
       } catch (Exception e) {
           System.out.println("WARNING:Error executing Query in createVendorFromTmp ->" +
                              e.getMessage());
       } 
           
       return result;
       }

       
       public boolean updateVendorFromVendorTMP(legend.admin.objects.Vendor vendor) {

          
           boolean done = false;

           String query = "UPDATE am_ad_vendor"
                          +" SET Vendor_Name = ?"
                          +", Contact_Person = ?,Contact_Address = ?,Vendor_Phone = ?"
                          +" , Vendor_fax = ?,Vendor_email = ?,Vendor_State = ?"
                          +" , Acquisition_Vendor = ?,Maintenance_Vendor = ?,account_number = ?"
                          +" , Vendor_Status = ?"
                          +" , account_type = ?,VendorBranchId = ?,VENDOR_CATEGORY = ?,SERVICE_TYPE = ?,RCNo = ?,TIN = ? "
                          +" WHERE Vendor_Code =?";

           try (Connection c = getConnection();
      	         PreparedStatement ps = c.prepareStatement(query)) {

//               ps.setString(1, vendor.getVendorCode());
               ps.setString(1, vendor.getVendorName());
               ps.setString(2, vendor.getContactPerson());
               ps.setString(3, vendor.getContactAddress());
               ps.setString(4, vendor.getVendorPhone());
               ps.setString(5, vendor.getVendorFax());
               ps.setString(6, vendor.getVendorEmail());
               ps.setString(7, vendor.getVendorState());
               ps.setString(8, vendor.getAquisitionVendor());
               ps.setString(9, vendor.getMaintenanceVendor());
               ps.setString(10, vendor.getAccountNumber());
               ps.setString(11, vendor.getVendorStatus());
               ps.setString(12, vendor.getAccountType());
    //           ps.setString(14, vendor.getVendorProvince());
               ps.setInt(13, vendor.getVendorBranchId());
               ps.setString(14, vendor.getVendorCategory());
               ps.setString(15, vendor.getVendorServiceType());
               ps.setString(16, vendor.getRcNo());
               ps.setString(17, vendor.getTin());
               ps.setString(18, vendor.getVendorCode());
               
               done = (ps.executeUpdate()!=-1);
               
           } catch (Exception e) {
               System.out.println("WARNING:Error executing Query in updateVendorFromVendorTMP->" +
                                  e.getMessage());
           } 
           
           return done;

       }

       
}
