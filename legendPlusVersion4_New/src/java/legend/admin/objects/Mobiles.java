package legend.admin.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Mobiles {
	private String mobileUserId;
	private String userName;
	private String fullName;
	private String mobileStatus;
	private String userId;
	private String createDate;
	private String macAddress;
	private String staffEnabled;

	
	public Mobiles(){
		
	}	


	/**
	 * @return the mobileUserId
	 */
	public String getMobileUserId() {
		return mobileUserId;
	}

	/**
	 * @param mobileUserId the mobileUserId to set
	 */
	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return the mobileStatus
	 */
	public String getMobileStatus() {
		return mobileStatus;
	}


	/**
	 * @param mobileStatus the mobileStatus to set
	 */
	public void setMobileStatus(String mobileStatus) {
		this.mobileStatus = mobileStatus;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}


	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}	

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}


	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	
	
	
	public String getStaffEnabled() {
		return staffEnabled;
	}


	public void setStaffEnabled(String staffEnabled) {
		this.staffEnabled = staffEnabled;
	}


	public void DeleteExistingMobile()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				String id = "";

			String query2 = "delete from AM_GB_REGMAC where USER_NAME in (select USER_NAME from AM_GB_REGMAC_UPLOAD)";
			String query  = "insert into AM_GB_REGMAC_UPLOAD (USER_NAME, MAC_ADDRESS, STATUS, CREATE_DATE, USER_ID, Staff_Enabled) values(?,?,?,?,?,?);";
			try {     
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, userName);
				ps.setString(2, macAddress);
				ps.setString(3, mobileStatus);
				ps.setString(4, createDate);
				ps.setString(5, userId);
				ps.setString(6, staffEnabled);
			    
				//System.out.println("<<<<<<<DeleteExistingStaff staffId: "+staffId+"   fullName: "+fullName+"    branchCode: "+branchCode+"   createdBy: "+createdBy);
				done = (ps.executeUpdate() != -1);
				closeConnection(con, ps);
				con = getConnection();  
				ps = con.prepareStatement(query2);
				done = (ps.executeUpdate() != -1);
				closeConnection(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in DeleteExistingMobileReg creating Mobile Registration Upload>> "
					+ e.getMessage());
			} finally {
				closeConnection(con, ps);
			}

			}
    
	public void createMobileRegUpload()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				String id = "";

			String query = "insert into AM_GB_REGMAC_UPLOAD (USER_NAME, MAC_ADDRESS, STATUS, CREATE_DATE, USER_ID, Staff_Enabled) values(?,?,?,?,?,?);";

			try {   
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, userName);
				ps.setString(2, macAddress);
				ps.setString(3, mobileStatus);
				ps.setString(4, createDate);
				ps.setString(5, userId);
				ps.setString(6, staffEnabled);
				
				
			
				done = (ps.executeUpdate() != -1);
				System.out.println("<<<<<<<createMobileRegUpload done: "+done);
				closeConnection(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in createMobileRegUpload creating Mobile Registration Upload>> "
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
			    DeleteExistingMobile();
//			        createUserUpload();
			        value = DONE;
			 
			    return value;
			}
	  
	  
	  public ArrayList findMobileUploadByQuery() {

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
          Mobiles mobile = null;
          ArrayList finder = new ArrayList();
          String selectQuery =
          			"select USER_NAME, MAC_ADDRESS, STATUS, CREATE_DATE, USER_ID, Staff_Enabled from AM_GB_REGMAC_UPLOAD ";
          try {
              con = getConnection();
              ps = con.prepareStatement(selectQuery);
              rs = ps.executeQuery();

              while (rs.next()) {
                String userName = rs.getString("USER_NAME");
  				String macAddress = rs.getString("MAC_ADDRESS");
  			   String userStatus = rs.getString("STATUS");
  			   String createDate = rs.getString("CREATE_DATE");
  			   String userId = rs.getString("USER_ID");
  			   String staffEnabled = rs.getString("Staff_Enabled");
  			   	  mobile = new Mobiles();
  			   	mobile.setUserName(userName);
  			   	mobile.setMacAddress(macAddress);
  			   	mobile.setMobileStatus(userStatus);
  			   	mobile.setCreateDate(createDate);
  			   	mobile.setUserId(userId);
  			   	mobile.setStaffEnabled(staffEnabled);
                
                  finder.add(mobile);

              }

          } catch (Exception e) {
              System.out.println("INFO:GroupMobileReg -Error Fetching Records ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }
          return finder;
      }
	  
	  public boolean deleteMobileRegUpload() {

			String UPDATE_QUERY = "delete from AM_GB_REGMAC where USER_NAME in (select USER_NAME from AM_GB_REGMAC_UPLOAD) ";

			String UPDATE_QUERY2 = "delete from AM_GB_REGMAC_UPLOAD ";
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
