package legend.admin.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AssetDescription {
	private String descriptionId;
	private String subCategoryCode;
	private String description;
	private String descriptionStatus;
	private String userId;
	private String createDate;

	
	public AssetDescription(){
		
	}	


	/**
	 * @return the descriptionId
	 */
	public String getDescriptionId() {
		return descriptionId;
	}

	/**
	 * @param descriptionId the descriptionId to set
	 */
	public void setDescriptionId(String descriptionId) {
		this.descriptionId = descriptionId;
	}

	/**
	 * @return the subCategoryCode
	 */
	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	/**
	 * @param subCategoryCode the subCategoryCode to set
	 */
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the descriptionStatus
	 */
	public String getDescriptionStatus() {
		return descriptionStatus;
	}


	/**
	 * @param descriptionStatus the descriptionStatus to set
	 */
	public void setDescriptionStatus(String descriptionStatus) {
		this.descriptionStatus = descriptionStatus;
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
	
	
	  public ArrayList findAssetDescriptionUploadByQuery() {

          Connection con = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
          AssetDescription descriptionDetail = null;
          ArrayList finder = new ArrayList();
          String selectQuery =
          			"select sub_category_code, description, status, user_id, create_date from AM_ASSET_DESCRIPTION_UPLOAD";
          try {
              con = getConnection();
              ps = con.prepareStatement(selectQuery);
              rs = ps.executeQuery();

              while (rs.next()) {
                String subCategoryCode = rs.getString("sub_category_code");
  				String description = rs.getString("description");
  			   String status = rs.getString("status");
  			   String userId = rs.getString("user_id");
  			   String createDate = rs.getString("create_date");
  			 descriptionDetail = new AssetDescription();
  			descriptionDetail.setSubCategoryCode(subCategoryCode);
  			descriptionDetail.setDescription(description);
  			descriptionDetail.setDescriptionStatus(status);
  			descriptionDetail.setUserId(userId);
  			descriptionDetail.setCreateDate(createDate);
                
                  finder.add(descriptionDetail);

              }

          } catch (Exception e) {
              System.out.println("INFO:GroupStaff -Error Fetching Records ->" +
                                 e.getMessage());
          } finally {
        	  closeConnect(con, ps, rs);
          }
          return finder;
      }
	  
	  public boolean deleteAssetDescriptionUpload() {

			String UPDATE_QUERY = "delete from AM_ASSET_DESCRIPTION where description in (select description from AM_ASSET_DESCRIPTION_UPLOAD) ";

			String UPDATE_QUERY2 = "delete from AM_ASSET_DESCRIPTION_UPLOAD ";
			Connection con = null;
			PreparedStatement ps = null;
			boolean done = false;

			try {
				con = getConnection();
				ps = con.prepareStatement(UPDATE_QUERY);
				done = (ps.executeUpdate() != -1);
//				System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY+"   done: "+done);
				closeConnect(con, ps);
				con = getConnection();
				ps = con.prepareStatement(UPDATE_QUERY2);
				done = (ps.executeUpdate() != -1);
//				System.out.println("<<<<====UPDATE_QUERY in deleteUserUpload: "+UPDATE_QUERY2+"   done: "+done);
				closeConnect(con, ps);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				closeConnect(con, ps);
			}
			return done;
		}
	  
	  
	
	public void DeleteExistingUser()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				//String id = "";

			String query2 = "delete from AM_ASSET_DESCRIPTION where DESCRIPTION in (select DESCRIPTION from AM_ASSET_DESCRIPTION_UPLOAD)";
			String query  = ""
					+ "insert into AM_ASSET_DESCRIPTION_UPLOAD (SUB_CATEGORY_CODE, DESCRIPTION, STATUS, USER_ID, CREATE_DATE) values(?,?,?,?,?);";
			try {     
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, subCategoryCode);
				ps.setString(2, description);
				ps.setString(3, descriptionStatus);
				ps.setString(4, userId);
				ps.setString(5, createDate);
			    
				System.out.println("<<<<<<<DeleteExisting: subCategory_Code: "+subCategoryCode+"    status: "+descriptionStatus+"   createdBy: "+userId);
				done = (ps.executeUpdate() != -1);
				closeConnect(con, ps);
				con = getConnection();  
				ps = con.prepareStatement(query2);
				done = (ps.executeUpdate() != -1);
				closeConnect(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in DeleteExistingDescription creating User Upload>> "
					+ e.getMessage());
			} finally {
				closeConnect(con, ps);
			}

			}

	public void createUserUpload()
			throws Exception
			{
				Connection con = null;
				PreparedStatement ps = null;
				boolean done = false;
				//String id = "";

			String query = "insert into AM_ASSET_DESCRIPTION (SUB_CATEGORY_CODE, DESCRIPTION, STATUS, USER_ID, CREATE_DATE) values(?,?,?,?,?);";

			try {   
				con = getConnection();  
				ps = con.prepareStatement(query);
				ps.setString(1, subCategoryCode);
				ps.setString(2, description);
				ps.setString(3, descriptionStatus);
				ps.setString(4, userId);
				ps.setString(5, createDate);
				
				
			
				done = (ps.executeUpdate() != -1);
				System.out.println("<<<<<<<createStaffUpload done: "+done);
				closeConnect(con, ps);
			} catch (Exception e) {

			e.printStackTrace();
			System.out.println(this.getClass().getName()
				+ " INFO: Error in createStaffUpload creating Staff Upload>> "
					+ e.getMessage());
			} finally {
				closeConnect(con, ps);
			}

			}

	  public int insertDescriptionRecordUpload()  
			    throws Exception, Throwable
			{

			    int DONE = 0;

			    int value = 0;
			    	DeleteExistingUser();
//			        createUserUpload();
			        value = DONE;
			 
			    return value;
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
	  
	  public void closeConnect(Connection con, PreparedStatement ps) {
		    try {
		        if (ps != null) {
		            ps.close();
		        }
		        if (con != null) {
		            con.close();
		        }
		    } catch (Exception e) {
		        System.out.println("WARNING:Error closing Connection ->" +
		                           e.getMessage());
		    }

		}
	  
	    public void closeConnect(Connection con, PreparedStatement ps,
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
