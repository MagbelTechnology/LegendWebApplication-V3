package legend;

import java.sql.*;
import magma.net.dao.MagmaDBConnection;
import audit.*;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * @version 1.0
 */
public class ProvinceSetupBean extends ConnectionClass {
  private String[] provinces = new String[5];
  private String[] provan = new String[5];
     private AuditInfo ai = new AuditInfo();
  private AuditTrailGen atg = new AuditTrailGen();

 public final void setAuditInfo(AuditInfo AI)
 {
   this.ai = AI;
  }
  
public final void setAuditInfo(String TableName,  String BranchCode,  int LoginId, String RowId, boolean 

ReqInsertAudit)
	{
		ai.setTableName(TableName);
		ai.setBranchCode(BranchCode);
		ai.setLoginId(LoginId);
		ai.setRowId(RowId);
		ai.setReqInsertAudit(ReqInsertAudit);
	}
  
  public final AuditInfo getAuditInfo()
  {
     return this.ai;
  }


  public ProvinceSetupBean() throws Throwable {
  }

  public void setProvinces(String[] provinces) {
     if(provinces != null){
       this.provinces = provinces;
     }
  }

  public String[] getProvinces() {
    return provinces;
  }
   public void setProvan(String[] provan) {
         if(provan != null){
           this.provan = provan;
         }
      }

      public String[] getProvan() {
        return provan;
    }


    /**
     * selectProvinces
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectProvinces(String con, String sta) throws Throwable {
    StringBuffer cq = new StringBuffer(100);

    String statusQuery = "SELECT COUNT(*) FROM AM_GB_PROVINCE WHERE STATUS = '"+sta+"'";
		String idQuery = "SELECT COUNT(*) FROM AM_GB_PROVINCE WHERE PROVINCE_ID = "+con;

		String selQuerySatus = "SELECT * FROM AM_GB_PROVINCE WHERE STATUS = '"+sta+"' "+
								"ORDER BY PROVINCE ASC";

		String selQueryId = "SELECT * FROM AM_GB_PROVINCE WHERE PROVINCE_ID = "+con;


	    ResultSet rc = null;
	    if(con.equals("0")){
			rc = getStatement().executeQuery(statusQuery);
		}else if(con != ""){
			rc = getStatement().executeQuery(idQuery);
		}else{}

	    ResultSet rv = null;

	    if(con.equals("0")){
				rv = getStatement().executeQuery(selQuerySatus);
			}else if(con != ""){
				rv = getStatement().executeQuery(selQueryId);
		}else{}

	    rc.next();
	    String[][] values = new String[rc.getInt(1)][5];

	    for (int x = 0; x < values.length; x++) {
	      rv.next();
	      for (int y = 0; y < 5; y++) {
	        values[x][y] = rv.getString(y + 1);
	      }
	    }

	    return values;
	  }

  /**
   * updateProvinces
   *
   * @param con String
   * @return boolean
   * @throws Throwable
   */
  public boolean updateProvinces(String con) throws Throwable {
    StringBuffer iq = new StringBuffer(100);
    String str = "update am_gb_province set province_code = '"+provinces[0]+"',"+
    			"province = '"+provinces[1]+"',status = '"+provinces[2]+"'"+
    			"where province_id = "+con;

    int ret = getStatement().executeUpdate(str);
	 if(ret > 0)
		{  return true;	}
	 else
		{ return false;	}
  }
  
  
  
  
  
    public String[][] selectProvinceCode(String provinceCode) throws Throwable {

    		Connection mor = null;
    		PreparedStatement ps = null;
    		ResultSet rs = null;
    		System.out.print("Action performed");

    		MagmaDBConnection dbConn = new MagmaDBConnection();

               String count = "";
               String select = "";
               count = "SELECT COUNT(*) FROM AM_GB_province "+
    						"WHERE province_CODE = ?";

               select = "SELECT * FROM AM_GB_province WHERE "+
    						"province_CODE = ?  ";

    						String[][] values = new String[500][5];
                      int j = 0;

    		 try {
                      mor = dbConn.getConnection("fixedasset");

    			ps = mor.prepareStatement(count);
    			ps.setString(1,provinceCode);
    			rs = ps.executeQuery();
                while(rs.next()){j = rs.getInt(1);}

    			ps = mor.prepareStatement(select);
    			ps.setString(1,provinceCode);

    			rs = ps.executeQuery();


    			values = new String[j][5];

    			for (int x = 0; x < values.length; x++) {
    			rs.next();
    				for (int y = 0; y < 5; y++) {
    				values[x][y] = rs.getString(y + 1);
    				}
    			}


            }
    		catch (Exception e) {
             System.out.println("INFO:Error fetching location Code->" +e.getMessage());
            }
    		finally {
             dbConn.closeConnection(mor, ps,rs);
    		}
    		return values;
        }

       public boolean isUniqueCode(String provinceCode) throws Throwable {
    	 String query = "SELECT PROVINCE_CODE FROM AM_GB_PROVINCE "+
    							"WHERE PROVINCE_CODE = ? ";
    		Connection mor = null;
    		PreparedStatement ps = null;
    		ResultSet rs = null;
    		boolean confirm = false;

    		MagmaDBConnection dbConn = new MagmaDBConnection();

    	try {
    		mor = dbConn.getConnection("fixedasset");
    		ps = mor.prepareStatement(query);


    			ps.setString(1,provinceCode);

    			rs = ps.executeQuery();
    			while (rs.next()){
    				confirm = true;
    			}

    		}
    		catch (Exception e) {
             System.out.println("INFO:Error checking for duplicate code->" +e.getMessage());

            }
    		finally {
                dbConn.closeConnection( mor, ps,rs);
           }

    		  return confirm;
	}

  /**
   * insertProvinces
   *
   * @return boolean
   * @throws Throwable
   */
  public boolean insertProvinces() throws Throwable {
    StringBuffer iq = new StringBuffer(100);

    String str = "INSERT INTO AM_GB_PROVINCE("+
				"PROVINCE_CODE,PROVINCE,STATUS,[USER_ID],CREATE_DATE )	VALUES(";




iq.append(str);
    iq.append("'");
    iq.append(provinces[0]);
    iq.append("'");

    for (int i = 1; i <= 3; i++) {
      switch (i) {
        case -1:
          iq.append(", ");
          iq.append(provinces[i]);
          break;
        default:
          iq.append(",'");
          iq.append(provinces[i]);
          iq.append("'");
      }
    }
    iq.append(",getDate())");

     if(ai.reqInsertAudit() == true)
      {
        atg.select(1, "SELECT * FROM AM_GB_PROVINCE WHERE province_Id = (SELECT MIN(province_Id) FROM AM_GB_PROVINCE)");
		atg.captureAuditFields(provinces);
		atg.logAuditTrail(ai);	
      }
    boolean done = (getStatement().executeUpdate(iq.toString()) == -1 );
    System.out.println("INFO:Return variable is "+done);
    return true;
  }
}











