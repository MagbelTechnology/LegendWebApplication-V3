package legend.admin.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.magbel.util.ApplicationHelper;

public class Vendor {

	private String vendorId;
	private String vendorCode;
	private String vendorName;
	private String contactPerson;
	private String contactAddress;
	private String vendorPhone;
	private String vendorFax;
	private String vendorEmail;
	private String vendorState;
	private String aquisitionVendor;
	private String maintenanceVendor;
	private String accountNumber;
	private String vendorStatus;
	private String userId;
	private String createdate;
	private String accountType;
	private String vendorProvince;
	private int vendorBranchId;
	private String vendorCategory;
	private String vendorServiceType;
	private String rcNo;
	private String tin;
	public Vendor() {
		
		
	}

	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the vendorId
	 */
	public String getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorBranchId the vendorBranchId to set
	 */
	public void setVendorBranchId(int vendorBranchId) {
		this.vendorBranchId = vendorBranchId;
	}

	/**
	 * @return the vendorBranchId
	 */
	public int getVendorBranchId() {
		return vendorBranchId;
	}

	/**
	 * @param vendorCode the vendorCode to set
	 */
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}


	/**
	 * @return the vendorCode
	 */
	public String getVendorCode() {
		return vendorCode;
	}

	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param contactPerson the contactPerson to set
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * @return the contactPerson
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * @param contactAddress the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	/**
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}


	/**
	 * @param vendorPhone the vendorPhone to set
	 */
	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	/**
	 * @return the vendorPhone
	 */
	public String getVendorPhone() {
		return vendorPhone;
	}

	/**
	 * @param vendorFax the vendorFax to set
	 */
	public void setVendorFax(String vendorFax) {
		this.vendorFax = vendorFax;
	}

	/**
	 * @return the vendorFax
	 */
	public String getVendorFax() {
		return vendorFax;
	}

	/**
	 * @param vendorEmail the vendorEmail to set
	 */
	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	/**
	 * @return the vendorEmail
	 */
	public String getVendorEmail() {
		return vendorEmail;
	}

	/**
	 * @param vendorState the vendorState to set
	 */
	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}

	/**
	 * @return the vendorState
	 */
	public String getVendorState() {
		return vendorState;
	}

	/**
	 * @param aquisitionVendor the aquisitionVendor to set
	 */
	public void setAquisitionVendor(String aquisitionVendor) {
		this.aquisitionVendor = aquisitionVendor;
	}

	/**
	 * @return the aquisitionVendor
	 */
	public String getAquisitionVendor() {
		return aquisitionVendor;
	}

	/**
	 * @param maintenanceVendor the maintenanceVendor to set
	 */
	public void setMaintenanceVendor(String maintenanceVendor) {
		this.maintenanceVendor = maintenanceVendor;
	}

	/**
	 * @return the maintenanceVendor
	 */
	public String getMaintenanceVendor() {
		return maintenanceVendor;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @param vendorStatus the vendorStatus to set
	 */
	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	/**
	 * @return the vendorStatus
	 */
	public String getVendorStatus() {
		return vendorStatus;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param createdate the createdate to set
	 */
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	/**
	 * @return the createdate
	 */
	public String getCreatedate() {
		return createdate;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param vendorProvince the vendorProvince to set
	 */
	public void setVendorProvince(String vendorProvince) {
		this.vendorProvince = vendorProvince;
	}


	/**
	 * @return the vendorProvince
	 */
	public String getVendorProvince() {
		return vendorProvince;
	}

	/**
	 * @param vendorCategory the vendorCategory to set
	 */
	public void setVendorCategory(String vendorCategory) {
		this.vendorCategory = vendorCategory;
	}

	/**
	 * @return the vendorCategory
	 */
	public String getVendorCategory() {
		return vendorCategory;
	}

	/**
	 * @param vendorServiceType the vendorServiceType to set
	 */
	public void setVendorServiceType(String vendorServiceType) {
		this.vendorServiceType = vendorServiceType;
	}

	/**
	 * @return the vendorServiceType
	 */
	public String getVendorServiceType() {
		return vendorServiceType;
	}	
	
	/**
	 * @param rcNo the rcNo to set
	 */
	public void setRcNo(String rcNo) {
		this.rcNo = rcNo;
	}

	/**
	 * @return the rcNo
	 */
	public String getRcNo() {
		return rcNo;
	}	

	/**
	 * @param tin the TIN to set
	 */
	public void setTin(String tin) {
		this.tin = tin;
	}

	/**
	 * @return the TIN
	 */
	public String getTin() {
		return tin;
	}
	
	public void DeleteExistingUser()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				String id = "";
				
			String query2 = "delete from am_ad_vendor where RCNo in (select RCNo from am_ad_vendor_Upload)";
			String query  = "insert into am_ad_vendor_Upload (Vendor_Name,Contact_Person,Contact_Address,Vendor_Phone,account_number,RCNo,TIN)"
					+ " values(?,?,?,?,?,?,?);";
			try {     
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, vendorName);  
				ps.setString(2, contactPerson);
				ps.setString(3, contactAddress);
				ps.setString(4, vendorPhone);
				ps.setString(5, accountNumber);
				ps.setString(6, rcNo);
				ps.setString(7, tin);
				
			    
				System.out.println("<<<<<<<DeleteExistingStaff vendorName: "+vendorName+"   contactPerson: "+contactPerson+"    rcNo: "+rcNo+"   tin: "+tin);
				done = (ps.executeUpdate() != -1);
				closeConnection(con, ps);
				con = getConnection();  
				ps = con.prepareStatement(query2);
				done = (ps.executeUpdate() != -1);
				closeConnection(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in DeleteExistingStaff creating User Upload>> "
					+ e.getMessage());
			} finally {
				closeConnection(con, ps);
			}

			}
    
	public void createUserUpload()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				String id = "";
				 ApplicationHelper helper = new ApplicationHelper();
		          String vendorID = helper.getGeneratedId("am_ad_vendor");

			String query = "insert into am_ad_vendor (Vendor_Id, Vendor_Code, Vendor_Name,Contact_Person,Contact_Address,Vendor_Phone,account_number,RCNo,TIN) values(?,?,?,?,?,?,?,?,?);";

			try {   
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, vendorID);
				ps.setString(2, vendorID);
				ps.setString(3, vendorName);
				ps.setString(4, contactPerson);
				ps.setString(5, contactAddress);
				ps.setString(6, vendorPhone);
				ps.setString(7, accountNumber);
				ps.setString(8, rcNo);
				ps.setString(9, tin);
				
				
			
				done = (ps.executeUpdate() != -1);
				System.out.println("<<<<<<<createVendorUpload done: "+done);
				closeConnection(con, ps);
			} catch (Exception e) {
 
			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in createVendorUpload creating Vendor Upload>> "
					+ e.getMessage());
			} finally {
				closeConnection(con, ps);
			}

			}
	
	  public int insertUserRecordUpload()  
			    throws Exception, Throwable
			{

			    int DONE = 0;

			    int value = 0;
			    	DeleteExistingUser();
//			        createUserUpload();
			        value = DONE;
			 
			    return value;
			}
	  
	  public ArrayList findVendorUploadByQuery() {

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
          Vendor vendor = null;
          ArrayList finder = new ArrayList();
          String selectQuery =
          			"select Vendor_Name,Contact_Person,Contact_Address,Vendor_Phone,account_number,"
          			+ "RCNo,TIN from am_ad_vendor_Upload";
          try {
              con = getConnection();
              ps = con.prepareStatement(selectQuery);
              rs = ps.executeQuery();

              while (rs.next()) {
                String vendor_name = rs.getString("Vendor_Name");
  				String contact_person = rs.getString("Contact_Person");
  			   String contact_address = rs.getString("Contact_Address");
  			   String vendor_phone = rs.getString("Vendor_Phone");
  			   String account_number = rs.getString("account_number");
  			 String rc_no = rs.getString("RCNo");
  			String tin_no = rs.getString("TIN");
                  vendor = new Vendor();  
                  vendor.setVendorName(vendor_name);
                  vendor.setContactPerson(contact_person);
                  vendor.setContactAddress(contact_address);
                  vendor.setVendorPhone(vendor_phone); 
                  vendor.setAccountNumber(account_number);
                  vendor.setRcNo(rc_no);
                  vendor.setTin(tin_no);
                  finder.add(vendor);

              }

          } catch (Exception e) {
              System.out.println("INFO:GroupStaff -Error Fetching Records ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }
          return finder;
      }
	  
	  
	  
	  public boolean deleteVendorUpload() {

			String UPDATE_QUERY = "delete from am_ad_vendor where RCNo in (select RCNo from am_ad_vendor_Upload) ";

			String UPDATE_QUERY2 = "delete from am_ad_vendor_Upload ";
			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;

			try {
				con = getConnection();
				ps = con.prepareStatement(UPDATE_QUERY);
				done = (ps.executeUpdate() != -1);
//				System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY+"   done: "+done);
				closeConnection(con, ps);
				con = getConnection();
				ps = con.prepareStatement(UPDATE_QUERY2);
				done = (ps.executeUpdate() != -1);
//				System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY2+"   done: "+done);
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
//	        	if(con==null){
	                Context initContext = new InitialContext();
	                String dsJndi = "java:/legendPlus";
	                DataSource ds = (DataSource) initContext.lookup(
	                		dsJndi);
	                con = ds.getConnection();
//	        	}
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
	    
	    public void closeConnection(Connection con, PreparedStatement ps,
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
				System.out.println("WARNING:Error closing Parameter 0 Connection ->" +
				               e.getMessage());
				}
				
				}  
	
}
