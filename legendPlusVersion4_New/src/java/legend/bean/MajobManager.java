package legend.bean;

import java.io.PrintStream;
import java.sql.*;

import legend.util.ConnectionClass;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
public class MajobManager
{

    ConnectionClass db;

    public MajobManager()
    {
    }

    public void createMake(String makecode, String make, String category, String status, String user)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_ASSETMAKE(ASSETMAKE_CODE,ASSETMAKE,CATEGORY_ID,STATUS,USERID) " +
"values('"
).append(makecode).append("','").append(make).append("','").append(category).append("','").append(status).append("','").append(user).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save make details ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listMake()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_GB_ASSETMAKE WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listMakes(String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_ASSETMAKE WHERE STATUS = '").append(status).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getMake(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_ASSETMAKE WHERE ASSETMAKE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void updateMake(String id, String makeCode, String make, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = (new StringBuilder()).append("UPDATE AM_GB_ASSETMAKE SET ASSETMAKE_CODE = '").append(makeCode).append("',").append("ASSETMAKE = '").append(make).append("',Status = '").append(status).append("' WHERE ASSETMAKE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not update make ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public void createProvince(String provinceCode, String province, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_PROVINCE(PROVINCE_CODE,PROVINCE,STATUS) values ('").append(provinceCode).append("','").append(province).append("','").append(status).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save Province ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listProvince()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = "SELECT * FROM AM_GB_PROVINCE WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list province ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listProvinces(String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_PROVINCE WHERE STATUS = '").append(status).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list province ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getProvince(String province_id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_PROVINCE WHERE PROVINCE_ID = '").append(province_id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not get province ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void updateProvince(String id, String provinceCode, String province, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = (new StringBuilder()).append("UPDATE AM_GB_PROVINCE SET PROVINCE_CODE = '").append(provinceCode).append("',").append("PROVINCE = '").append(province).append("',Status = '").append(status).append("' WHERE PROVINCE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not update province ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listState()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	 con = getConnection();
            String query = "SELECT * FROM STATE WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list states ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public String getStartDate(String id)
    {
        String startdate = null;
        Connection con = null;
        Statement ps = null;
        try
        {
        	 con = getConnection();
            String query = "SELECT STARTDATE FROM COMPANYPROFILE ";
            Statement don = con.createStatement();
            ResultSet rs = don.executeQuery(query);
            if(rs.next())
            {
                startdate = rs.getString("STARTDATE");
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list startdate ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps);
		}
        return startdate;
    }

    public String getEndDate(String id)
    {
        String enddate = null;
        Connection con = null;
        Statement ps = null;
        try
        {
        	 con = getConnection();
            String query = "SELECT ENDDATE FROM COMPANYPROFILE ";
            Statement don = con.createStatement();
            ResultSet rs = don.executeQuery(query);
            if(rs.next())
            {
                enddate = rs.getString("ENDDATE");
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list enddate ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps);
		}
        return enddate;
    }

    public ResultSet listNoticeAlert()
    {
    	Connection con = null;
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	boolean done = false;
        try
        {
            con = getConnection();
            String query = "SELECT * FROM AM_NOTICEREMINDER WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list notice alert ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getNoticeAlert(String noticerm_id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_NOTICEREMINDER WHERE NOTICERM_ID = '").append(noticerm_id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not get notice alert ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listCategories()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT CATEGORY FROM AM_NOTICEREMINDER WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list category ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listCatego()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT CATEGORY FROM AM_AD_CATEGORY WHERE STATUS = 'A'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list category ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listBranch()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT BARNCH_ID FROM AM_NOTICEREMINDER WHERE STATUS = 'ActiveA'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list branch id ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void createDepDist(String type, String value, String depExp, String depAccum, String sNo, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_DEPR_DIST(TYPE,VALUE_ASSIGNED,DIST_EXP_ACCT,DIST_ACCUM_ACCT,SEQUE" +
"NCE_NO,STATUS) values('"
).append(type).append("','").append(value).append("','").append(depExp).append("','").append(depAccum).append("','").append(sNo).append("','").append(status).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save depriciation distribution details ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listDepDist()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_DEPR_DIST WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list depriciation distribution ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getDepDist(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_DEPR_DIST WHERE DIST_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not get depriciation distribution ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void updateDepDist(String id, String code, String type, String value, String status, String desc)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("UPDATE AM_DEPR_DIST SET CODE = '").append(code).append("',Type = '").append(type).append("',DESCRIPTION = '").append(desc).append("',DIST_AMT = '").append(value).append("',Status = '").append(status).append("' WHERE DIST_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not update depriciation distribution ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public void createLocation(String localCode, String local, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_LOCATION(LOCATION_CODE,LOCATION,STATUS) values('").append(localCode).append("','").append(local).append("','").append(status).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save location details ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listLocation()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_GB_LOCATION WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list location ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listLocations(String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_LOCATION WHERE STATUS = '").append(status).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list location ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getLocation(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_LOCATION WHERE LOCATION_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list location ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void updateLocation(String id, String localCode, String local, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("UPDATE AM_GB_LOCATION SET LOCATION_CODE = '").append(localCode).append("',").append("LOCATION = '").append(local).append("',Status = '").append(status).append("' WHERE LOCATION_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not update location ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public void createPrevMain(String pmCode, String make, String startDate, String endDt)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_PMMAINT(PM_MAINT_ID,PM_MAINT_CODE,MAKE,STARTDT,ENDDT) values('").append(Long.toString(System.currentTimeMillis())).append("','").append(pmCode).append("','").append(make).append("','").append(startDate).append("','").append(endDt).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save preventive maintanance details ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listPrevMain()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_GB_PMMAINT WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getPrevMain(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_PMMAINT WHERE ASSETMAKE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void updatePrevMain(String id, String makeCode, String make, String status)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("UPDATE AM_GB_PMMAINT SET ASSETMAKE_CODE = '").append(makeCode).append("',").append("ASSETMAKE = '").append(make).append("',Status = '").append(status).append("' WHERE ASSETMAKE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not update make ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listCategory()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_AD_CATEGORY";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void createFuelMain(String fmCode, String make, String cost, String startDate, String endDt)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_FUELMAINT(FuelMaint_ID,FuelMaint_Code,MAKE,Cost,STARTDT,ENDDT)" +
" values('"
).append(Long.toString(System.currentTimeMillis())).append("','").append(fmCode).append("','").append(make).append("','").append(cost).append("','").append(startDate).append("','").append(endDt).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save preventive maintanance details ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listFuelMain()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_GB_FUELMAINT WHERE STATUS = 'Active'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getFuelMain(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_FUELMAINT WHERE ASSETMAKE_ID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list makes ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public void createStolenVec(String stolenCode, String branch, String registDt, String category, String assetId, String desc, String dtStolen, 
            String entryDt, String policeNotiDt, String policeSta, String insurDt, String insurCom, String user)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("INSERT INTO AM_GB_STOLENVEC(STOLENCODE,BRANCH,REGISTRATIONNO,CATEGORYID,ASSETID," +
"ASSETDESC,DTSTOLEN,ENTRYDT,POLICENOTICEDT,POLICE_STATION,INSURANCENOTICEDT,INSUR" +
"COMP,USERID) values('"
).append(stolenCode).append("','").append(branch).append("','").append(registDt).append("','").append(category).append("','").append(assetId).append("','").append(desc).append("','").append(dtStolen).append("','").append(entryDt).append("','").append(policeNotiDt).append("','").append(policeSta).append("','").append(insurDt).append("','").append(insurCom).append("','").append(user).append("')").toString();
            Statement don = con.createStatement();
            don.execute(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not save create accident details ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
    }

    public ResultSet listStolenVec()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT * FROM AM_GB_STOLENVEC";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not list group asset ").append(e.getMessage()).toString());
            e.printStackTrace();
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet getStolenVec(String id)
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = (new StringBuilder()).append("SELECT * FROM AM_GB_STOLENVEC WHERE STOLENID = '").append(id).append("'").toString();
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not get group asset ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
    }

    public ResultSet listBranchs()
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
        	con = getConnection();
            String query = "SELECT BRANCH_NAME FROM AM_AD_BRANCH WHERE BRANCH_STATUS = 'A'";
            Statement don = con.createStatement();
            rs = don.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("WARING: Could not lists branch ").append(e.getMessage()).toString());
        }
		finally {
			closeConnection(con, ps, rs);
		}
        return rs;
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
		//finally {
//			closeConnection(con);
//		}
		return con;
	}
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
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
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
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}
}