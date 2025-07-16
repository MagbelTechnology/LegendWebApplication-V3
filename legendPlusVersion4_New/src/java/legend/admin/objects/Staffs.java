package legend.admin.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import magma.DateManipulations; 

public class Staffs { 
	  
    private String staffId;
    private String fullName;
    private String deptCode;
    private String branchCode;
    private String createdBy;
    private String deptName;
    private String branchName;
    private String staffStatus;
    
    
    public Staffs() {
        // TODO Auto-generated constructor stub
    }

    
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getDeptName() {
		return deptName;
	}


	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public String getStaffStatus() {
		return staffStatus;
	}


	public void setStaffStatus(String staffStatus) {
		this.staffStatus = staffStatus;
	}


	public void DeleteExistingUser()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				String id = "";

			String query2 = "delete from am_gb_Staff where StaffId in (select StaffId from am_gb_staff_Upload)";
			String query  = "insert into am_gb_Staff_Upload (StaffId, Full_Name, dept_code, branch_code, UserId) values(?,?,?,?,?);";
			try {     
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, staffId);
				ps.setString(2, fullName);
				ps.setString(3, deptCode);
				ps.setString(4, branchCode);
				ps.setString(5, createdBy);
			    
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

			String query = "insert into am_gb_Staff (StaffId, Full_Name, dept_code, branch_code, UserId) values(?,?,?,?,?);";

			try {   
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, staffId);
				ps.setString(2, fullName);
				ps.setString(3, deptCode);
				ps.setString(4, branchCode);
				ps.setString(5, createdBy);
				
				
			
				done = (ps.executeUpdate() != -1);
				System.out.println("<<<<<<<createStaffUpload done: "+done);
				closeConnection(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in createStaffUpload creating Staff Upload>> "
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
	  
	  
	  public ArrayList findStaffUploadByQuery() {

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
          Staffs staff = null;
          ArrayList finder = new ArrayList();
          String selectQuery =
          			"select StaffId, Full_Name, dept_code, branch_code, UserId from am_gb_Staff_Upload ";
          try {
              con = getConnection();
              ps = con.prepareStatement(selectQuery);
              rs = ps.executeQuery();

              while (rs.next()) {
                String staffId = rs.getString("StaffId");
  				String fullName = rs.getString("Full_Name");
  			   String deptCode = rs.getString("dept_code");
  			   String branchCode = rs.getString("branch_code");
  			   String userId = rs.getString("UserId");
                  staff = new Staffs();
                  staff.setStaffId(staffId);
                  staff.setFullName(fullName);
                  staff.setBranchCode(branchCode);
                  staff.setDeptCode(deptCode);
                  staff.setCreatedBy(userId);
                
                  finder.add(staff);

              }

          } catch (Exception e) {
              System.out.println("INFO:GroupStaff -Error Fetching Records ->" +
                                 e.getMessage());
          } finally {
              closeConnection(con, ps, rs);
          }
          return finder;
      }
	  
	  public boolean deleteStaffUpload() {

			String UPDATE_QUERY = "delete from am_gb_Staff where StaffId in (select StaffId from am_gb_Staff_Upload) ";

			String UPDATE_QUERY2 = "delete from am_gb_Staff_Upload ";
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
