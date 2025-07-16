package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;
import  legend.admin.objects.CategoryClass;
import com.magbel.util.DataConnect;





public class CategoryClassHandler {
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
	
    public CategoryClassHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllCategoryClass() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CategoryClass  catclass = null;
        String query = "SELECT Class_ID, Class_Code, Class_Name"
                       + ", Class_Status, User_Id, Create_Date"
                      + " FROM [AM_AD_CATEGORY_CLASS]";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c= getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
               String classId = rs.getString("Class_Id");
                String classCode = rs.getString("Class_Code");                
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                catclass = new legend.admin.objects.CategoryClass();
                catclass.setClassId(classId);
                catclass.setClassCode(classCode);
                catclass.setClassName(className);
                catclass.setUserId(userId);
				catclass.setCreateDate(createDate);
				_list.add(catclass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.List getCategoryClassByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        legend.admin.objects.CategoryClass  catclass = null;
		
        String query =  "SELECT Class_Id, Class_Code, Class_Name"
                       + ", Class_Status, User_Id, Create_Date"
                      + " FROM [AM_AD_CATEGORY_CLASS] WHERE Class_Id IS NOT NULL ";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        query+=filter;
        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
				String classId = rs.getString("Class_Id");
                String classCode = rs.getString("Class_Code");                
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                catclass = new legend.admin.objects.CategoryClass();
                catclass.setClassId(classId);
                catclass.setClassCode(classCode);
                catclass.setClassName(className);
                catclass.setUserId(userId);
				catclass.setCreateDate(createDate);
				catclass.setClassStatus(classStatus);
				_list.add(catclass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.CategoryClass   getCategoryClassByClassID(String CatClassID) {
        legend.admin.objects.CategoryClass    catclass = null;
        String query = "SELECT Class_Id, Class_Code, Class_Name"
                       + ", Class_Status, User_Id, Create_Date"
                      + " FROM [AM_AD_CATEGORY_CLASS] WHERE Class_ID = '" + CatClassID+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String classId = rs.getString("Class_Id");
                String classCode = rs.getString("Class_Code");                
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                catclass = new legend.admin.objects.CategoryClass();
                catclass.setClassId(classId);
                catclass.setClassCode(classCode);
                catclass.setClassName(className);
                catclass.setUserId(userId);
                catclass.setClassStatus(classStatus);
				catclass.setCreateDate(createDate);
			
				
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return catclass;
    }
	
	

    public boolean isUniqueCode(String CatClassCode) {
	  
	   boolean unique = false;
        
        String query = "SELECT Class_Code"
                        + " FROM [AM_AD_CATEGORY_CLASS] WHERE  Class_Code = '" +CatClassCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
		//PreparedStatement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
			int x = 0;
            while (rs.next()) {
			    x++;
                String locationCode = rs.getString("Location_Code");
                }
            if (x == 1) unique = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return unique;

    }

    
    private Connection getConnection() {
        Connection con = null;
        dc = new DataConnect("fixedasset");
        try {
            con = dc.getConnection();
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }
        return con;
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
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    /**
     * createCurrency
     */
    public boolean createCategoryClass(legend.admin.objects.CategoryClass  catgclass)
	{

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query =
                "INSERT INTO [AM_AD_CATEGORY_CLASS]( Class_Code, Class_Name"
                       + ", Class_Status, User_Id, Create_Date)"
                         + " VALUES (?,?,?,?,?, getDate())";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
           
            ps.setString(1, catgclass.getClassCode());
            ps.setString(2, catgclass.getClassName());
            ps.setString(3, catgclass.getClassStatus());
            ps.setString(4, catgclass.getUserId());
           
			
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateCategoryClass(legend.admin.objects.CategoryClass  catgclass) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE [AM_AD_CATEGORY_CLASS]"
                       + " SET [Class_Id] = ?"
                       + ",[Class_Name] = ?,[Class_Status] = ?"
                      + " WHERE  Class_ID = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
           ps.setString(1, catgclass.getClassId());
            ps.setString(2, catgclass.getClassName());
            ps.setString(3, catgclass.getClassStatus());
            ps.setString(4, catgclass.getUserId());
			ps.setString(5, catgclass.getClassId());
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


  public legend.admin.objects.CategoryClass getCategoryClassByClassCode(String CatClassCode) {
        
        legend.admin.objects.CategoryClass  catclass = null;
        String query = "SELECT Class_ID, Class_Code, Class_Name"
                       + ", Class_Status, User_Id, Create_Date"
                       + " FROM [AM_AD_CATEGORY_CLASS] WHERE Class_Code = '" +CatClassCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
               String classId = rs.getString("Class_Id");
                String classCode = rs.getString("Class_Code");                
				String className = rs.getString("Class_Name");
				String classStatus = rs.getString("Class_Status");
				String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");
				
                catclass = new legend.admin.objects.CategoryClass();
                catclass.setClassId(classId);
                catclass.setClassCode(classCode);
                catclass.setClassName(className);
                catclass.setUserId(userId);
				catclass.setCreateDate(createDate);
				
                		
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return catclass;

    }	
	
	
	
	

	 public boolean exist(String CatClassCode) {
        
        boolean itexist = false;
        String query = "SELECT  Class_Code, "
                       + " FROM [AM_AD_CATEGORY_CLASS] WHERE Class_Code = '" +CatClassCode+"'";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            con = getConnection();
            s = con.createStatement();
            rs = s.executeQuery(query);
			
            if( rs == null)
				{ itexist = false;	} 
			else 
				{ itexist = true;	}
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return itexist;

    }	
	
	
}
