package legend;

import java.sql.ResultSet;
import com.magbel.util.*;

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
public class TechEmployeesBean extends ConnectionClass {
  private String[] employees = new String[14];
  DatetimeFormat df ;

  public TechEmployeesBean() throws Exception {
	  df= new DatetimeFormat();
  }

  public void setEmployees(String[] employees) {
    if(employees != null){
      this.employees = employees;
    }
  }

  public String[] getEmployees() {
    return employees;
  }

    /**
     * selectemployees
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectEmployees(String con, String sta) throws Throwable {
    StringBuffer cq = new StringBuffer(100);

        String statusQuery = "SELECT COUNT(*) FROM AM_AD_EMPLOYEE WHERE EMPLOYEE_STATUS = '"+sta+"'";
	   	String idQuery = "SELECT COUNT(*) FROM AM_AD_EMPLOYEE WHERE EMPLOYEE_ID = "+con;

	   	String selQuerySatus = "SELECT * FROM AM_AD_EMPLOYEE WHERE EMPLOYEE_STATUS = '"+sta+"' "+
	   							"ORDER BY EMPLOYEE_NAME ASC";

	   	String selQueryId = "SELECT * FROM AM_AD_EMPLOYEE WHERE EMPLOYEE_ID = "+con;


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
	    String[][] values = new String[rc.getInt(1)][14];


	   for (int x = 0; x < values.length; x++) {
	      rv.next();
	      for (int y = 0; y < 14; y++) {
	        values[x][y] = rv.getString(y + 1);
	      }
	    }
            freeResource();
	    return values;
  }

    /*
    cq.append("am_msp_count_employees"
              +" '"+con+"','"+sta+"'");
    ResultSet rc = getStatement().executeQuery(
      cq.toString());

    StringBuffer sq = new StringBuffer(100);
    sq.append("am_msp_select_employees"
              +" '"+con+"','"+sta+"'");
    ResultSet rv = getStatement().executeQuery(
      sq.toString());

    rc.next();
    String[][] values = new String[rc.getInt(1)][14];

    for (int x = 0; x < values.length; x++) {
      rv.next();
      for (int y = 0; y < 14; y++) {
        values[x][y] = rv.getString(y + 1);
      }
    }

    return values;
  }
*/
  /**
   * updateemployees
   *
   * @param con String
   * @return boolean
   * @throws Throwable
   */
  public boolean updateEmployees(String con) throws Throwable {
    StringBuffer iq = new StringBuffer(100);

    String str = "UPDATE AM_AD_EMPLOYEE SET EMPLOYEE_CODE = '"+employees[0]+"',  "+
    			  "EMPLOYEE_NAME = '"+employees[1]+"',CONTACT_ADDRESS = '"+employees[2]+"',  "+
    			  "EMPLOYEE_STATE = '"+employees[3]+"',EMPLOYEE_PHONE = '"+employees[4]+"',EMPLOYEE_FAX = '"+employees[5]+"',  "+
    			  "EMPLOYEE_EMAIL = '"+employees[6]+"',EMPLOYEE_DEPT = '"+employees[7]+"',ACCOUNT_TYPE = '"+employees[8]+"',  "+
    			  "ACCOUNT_NUMBER = '"+employees[9]+"',EMPLOYEE_STATUS = '"+employees[10]+"',EMPLOYEE_PROVINCE = '"+employees[11]+"'  "+
    			  "WHERE EMPLOYEE_ID = "+con;

    boolean done = (getStatement().executeUpdate(str) == -1);
     freeResource();
    return true;
  }

  /**
   * insertemployees
   *
   * @return boolean
   * @throws Throwable
   */
  public boolean insertEmployees() throws Throwable {
    StringBuffer iq = new StringBuffer(100);
	String str = "insert into am_ad_employee("+
				 "employee_code,employee_name,contact_address,employee_state,"+
				 "employee_phone,employee_fax,employee_email,employee_dept,account_type,"+
				 "account_number,employee_status,employee_province,user_id,create_date,Employee_ID "+
				 ") values(";

    iq.append(str);
    iq.append("'");
    iq.append(employees[0]);
    iq.append("'");

    for (int i = 1; i <= 12; i++) {
      switch (i) {
        case -1:
          iq.append(", ");
          iq.append(employees[i]);
          break;
        default:
          iq.append(",'");
          iq.append(employees[i]);
          iq.append("'");
      }
    }
    iq.append(",'"+df.dateConvert(new java.util.Date())+"',"+System.currentTimeMillis()+")");
    System.out.println("INFO:Return variable is "+iq.toString());
    boolean done = (getStatement().executeUpdate(iq.toString()) == -1 );
     freeResource();
    System.out.println("INFO:Return variable is "+done);
    return true;
  }
}


    /*
    iq.append(str);
    iq.append("'");
    iq.append(employees[0]);
    iq.append("'");

    for (int i = 1; i <= 12; i++) {
      switch (i) {
        case -1:
          iq.append(", ");
          iq.append(employees[i]);
          break;
        default:
          iq.append(",'");
          iq.append(employees[i]);
          iq.append("'");
      }
    }

    return (getStatement().executeUpdate(
        iq.toString()) == -1);
  }
}*/
