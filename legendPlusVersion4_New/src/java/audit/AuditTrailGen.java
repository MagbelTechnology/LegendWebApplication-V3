package audit;
import java.sql.*;
import java.util.*;

import magma.net.dao.MagmaDBConnection;
//import audit.*;





import java.security.*;

//import java.sql.*;
//import java.util.*;
//import java.util.Date;
import legend.*;
import sun.misc.*;

import javax.naming.*;
import javax.sql.DataSource;




//import com.microsoft.sqlserver.jdbc.*;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;



/**
 * <p>Title: LEGEND</p>
 * <p>Description: LEGEND Fixed Asset Management System </p>
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 * <p>Company: Magbel Technologies Limited.</p>
*  @author  Bolaji L. Ogeyingbo.
 * @version 1.0
 */

public class AuditTrailGen extends Bag{ 
   
  //String dbname = "accounts";
  //String dsn = "accounts";
  Connection con = null;
  ResultSet rs = null;
  PreparedStatement prepstmnt = null;
  Statement stmnt = null;
  public final boolean login = true;
  public final boolean logout = false;
  int count = 0;
  int rowsupdated = 0;
  boolean checkupdate = false;
  boolean isupdate = false;
  HtmlUtility htmlUtil = new HtmlUtility();
  DatetimeFormat df = new DatetimeFormat();
  
 private   int cType = 0;  private int colIndex = 0;
 private Field field ; String tabnm = "";



 // private Connection con = null;
    private String jndiName = "legendPlusP";
    private int record_start = 0;
    private int record_count = 2;
    private Context ic = null;
	
	private MagmaDBConnection dbConnection = new MagmaDBConnection();

	/**
    public ConnectionClass() throws Exception {
        ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" + this.jndiName);
        conn = ds.getConnection();
      //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      //this.conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=legend;user=sa;password=oddeseyus");
    }
	**/
	
	

 /**
 *  Connects to the database
  * @return    Connection 
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
  public final Connection connectOld()  throws Exception
   {  
   try {
   /**
       ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/" + this.jndiName);
        con = ds.getConnection();
     **/
	    legend.ConnectionClass cc = new legend.ConnectionClass();
		con = cc.getConnection();
     // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
     // con = DriverManager.getConnection("jdbc:odbc:"+dsn);
      } catch (Exception e) {
				System.out.println("StandardQuery.connect() -> Exception " + e + " occured");
			}
     return con;
  }

  
  public final Connection connect() throws SQLException {
	    Connection con = null;
	    try {
	        // Use your Legend connection class
	        legend.ConnectionClass cc = new legend.ConnectionClass();
	        con = cc.getConnection();
	        
	        if (con == null || con.isClosed()) {
	            throw new SQLException("Failed to obtain a valid connection");
	        }
	    } catch (Exception e) {
	        System.out.println("StandardQuery.connect() -> Exception occurred: " + e);
	        e.printStackTrace();
	        throw new SQLException("Error connecting to DB", e);
	    }
	    return con;
	}

  public void closeConnection(Connection con, PreparedStatement ps)
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
      catch(Exception ex)
      {
          System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
      }
  }

  public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
  {
      try
      {
          if(ps != null)
          {
              ps.close();
          }
          if(rs != null)
          {
              rs.close();
          }
          if(con != null)
          {
              con.close();
          }
      }
      catch(Exception ex)
      {
          System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
      }
  }

  public void closeConnection(Connection con, Statement ps, ResultSet rs)
  {
      try
      {
          if(ps != null)
          {
              ps.close();
          }
          if(rs != null)
          {
              rs.close();
          }
          if(con != null)
          {
              con.close();
          }
      }
      catch(Exception ex)
      {
          System.out.println((new StringBuilder()).append("WARNING:Error closing Connection ->").append(ex).toString());
      }
  }

  //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
   //   con = DriverManager.getConnection("jdbc:sqlserver:"+dsn)

  
 /**
 *  Select all the values of the row on which data is altered into this object
  * @param   updatestatus as int  
 * @param    selectstmnt as String  
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
  public final void selectOld(int updatestatus, String selstmnt) throws Exception
   {
	   try {
			con = connect();  String val = ""; String nullstring = null;
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(selstmnt);
			 ResultSetMetaData rsmd = rs.getMetaData();
	        int numColumns = rsmd.getColumnCount();
			//String tabnam = "General"; //rsmd.getTableName(1);
			//this.setTableName(tabnam); tabnm = tabnam;
//            System.out.println("Number of Columns is: "+numColumns+"\t");
//			System.out.println("Reference table is: "+this.getTableName());
			String cname = null;
            int columnid = 0;    int columntype= 0;
          // Get the column names; column indices start from 1
	   if( updatestatus == this.BEFORE_UPDATE)
	    {
		 while(rs.next())  
             for (int i=1; i < numColumns+1  ; i++) 
		   {
                  cname = rsmd.getColumnName(i); // System.out.println("ColumnName: "+cname+"\n");
		       columnid = i; // System.out.println("\tColumnId: "+columnid+"\n");
			   columntype = rsmd.getColumnType(i);  //System.out.println("\tColumnType: "+columntype+"\n\n");
          	  
			    String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
                                if(strval==null)
			            { 
			                 strval = "null";  
			 //                System.out.println("Database warning: String column type with a null value");                                    
                                         } 
			                this.setField(cname, strval ,columntype ,  columnid  );
			                    //                            System.out.println("\tColumnvalue: "+strval.trim()+"\n");
			                                                                    //break;
                  
                  /*
                   * 
                   the main code b4 modification
                   switch(columntype)
			    {
			      case 4  :
				  case -6 :     int intval = rs.getInt(i); //sqltype for tinyint =  -6,  int = 4
								Integer intVal = Integer.valueOf(intval);
								if((intVal.toString()) == null )
									{
									intval = 0; 
									System.out.println("Database design error: Integer or tinyint column type cannot contain a null value");} 					else {  val = String.valueOf(intval); 		}
								val = String.valueOf(intval);
						        this.setField(cname, val.trim() ,columntype, columnid);
						        break;
			   
                  case 2 :
				  case 3 :  	double dblval = rs.getDouble(i); //sqltype for numeric = 2 , sqltype for decimal = 3	
				                Double dblVal = Double.valueOf(dblval);
								if(dblVal == null )
									{ 
									  dblval = 0; 
									  System.out.println("Database design error: Numeric or decimal column type can contain a null value");} 
									else { val = String.valueOf(dblval); }
									val = String.valueOf(dblval);
								this.setField(cname, val.trim() ,columntype, columnid);
								break;
								
			     //  else if(columntype ==  16 )//{}//sqltype for boolean = 16
				 
				  case 1  :
				  case 12 :     String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
								if(strval==null)
									{ 
										strval = "null"; 
										 System.out.println("Database warning: String column type with a null value");                                    } 
								this.setField(cname, strval ,columntype ,  columnid  );
								System.out.println("\tColumnvalue: "+strval.trim()+"\n");
								break;
								
				  default  :    Object vals = rs.getObject(i); //sqltype for varchar =  12,  char = 1
						System.out.println("am her@@@");
                                                		if( vals == null)
									{ 
									    System.out.println("am her@@@if");
									val = null; 
									System.out.println("Database warning: Object column type with a null value");}
								else 
									{ 
										val = vals.toString(); 
									    System.out.println("am her@@@else");
									}
								 val = vals.toString();
				               	this.setField(cname, val.trim() ,columntype ,  columnid  );
								System.out.println("\tColumnvalue: "+val.trim()+"\n");			
				}*/
			   //System.out.println("\t\t\t\t"+this.getOldValue(cname)+"  "+String.valueOf(this.getColumnType(cname)));  
		 
            }
		}
		
		else if( updatestatus == this.AFTER_UPDATE )
		     {
			  while(rs.next())  
               for (int i=1; i < numColumns+1  ; i++) 
		           {
                     cname = rsmd.getColumnName(i); // System.out.println("ColumnName: "+cname+"\n");
					columnid = i;  //System.out.println("\tColumnId: "+columnid+"\n");
					columntype = rsmd.getColumnType(i);  //System.out.println("\tColumnType: "+columntype+"\n\n");
			
                            String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
					                            if(strval==null)
					                            { 
					                                    strval = "null"; 
					          //                          System.out.println("Database warning: String column type with a null value");
					                                    } 
                                                                            
					                    this.setValue(cname, strval.trim() );
					                            //System.out.println("\tColumnvalue: "+strval.trim()+"\n");
					                            //break;
                                             
                        /*
                         * main b4 modification   
			        switch(columntype)
					      {
						    case 4  :
							case -6 :  int intval = rs.getInt(i); //sqltype for tinyint =  -6,  int = 4
										Integer intVal = new Integer(intval);
									   if(intVal==null)
										{
											intval = 0; 
											System.out.println("Database design error: Integer or tinyint column type cannot contain a null value");} 				else {  val = String.valueOf(intval); 		}
									   val = String.valueOf(intval);
										this.setValue(cname, val.trim() );
										break;
										  
					        case 2 :
							case 3 :    double dblval = rs.getDouble(i); //sqltype for numeric = 2 , sqltype for decimal = 3
										Double dblVal = Double.valueOf(dblval);
										if(dblVal==null)
											{ 
											dblval = 0; 
											System.out.println("Database design error: Numeric or decimal column type can contain a null value");} 
									else { val = String.valueOf(dblval); }
										val = String.valueOf(dblval);
										this.setValue(cname, val.trim() );
										break;
				     							
					     	//else if(columntype ==  16 ){}//sqltype for boolean = 16
			   
			                case 1 :
							case 12 :  String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
										if(strval==null)
										{ 
											strval = "null"; 
											System.out.println("Database warning: String column type with a null value");                                    		} 
		    							this.setValue(cname, strval.trim() );
										//System.out.println("\tColumnvalue: "+strval.trim()+"\n");
										break;
										  
							default :   Object vals = rs.getObject(i); //sqltype for varchar =  12,  char = 1
										if(vals == null)
											{ 
											val = null; 
											System.out.println("Database warning: Object column type with a null value");
											} 
										else 
											{ 
												val = vals.toString();  
											}
										val = vals.toString();
							            this.setValue(cname, val.trim() );
										//System.out.println("\tColumnvalue: "+val.trim()+"\n");
										  
						   }		*/	
					}
			// System.out.println("\t\t\t\t"+this.getValue(cname)+"  "+String.valueOf(this.getColumnType(cname)));  
			
		     }

	     } 
		catch (Exception e) 
			{
            System.out.println("StandardQuery.select() -> Exception " + e + " occured");
            }
     finally { con.close(); dbConnection.closeConnection(con,prepstmnt, rs);}
  }
  
  public final void select(int updatestatus, String selstmnt) throws Exception {
	    try (Connection con = connect();
	         Statement stmnt = con.createStatement();
	         ResultSet rs = stmnt.executeQuery(selstmnt)) {

	        ResultSetMetaData rsmd = rs.getMetaData();
	        int numColumns = rsmd.getColumnCount();

	        while (rs.next()) {
	            for (int i = 1; i <= numColumns; i++) {
	                String cname = rsmd.getColumnName(i);
	                int columntype = rsmd.getColumnType(i);

	                Object valObj = rs.getObject(i);
	                String val = valObj == null ? "null" : valObj.toString().trim();

	                if (updatestatus == this.BEFORE_UPDATE) {
	                    this.setField(cname, val, columntype, i);
	                } else if (updatestatus == this.AFTER_UPDATE) {
	                    this.setValue(cname, val);
	                }
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("StandardQuery.select() -> Exception occurred: " + e);
	        e.printStackTrace();
	        throw e;
	    }
	}
  
  public final boolean checkUpdate()
     {
	     if(checkupdate == true) // checks for updates in the relevant database table
		   { setCheckedChanges(true); return true;  }
		 else return false;
	 }
  
  
  
/**
 *   Logs audit trail : for use within servlet
 * @param     TableName as String  
 * @param     BranchCode as String  
 * @param     LoginId as int  
 * @param     RowId as String  
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
 
  /*
  public boolean logAuditTrail(String TableName,  String BranchCode,  int Login_Id, String RowId) throws Exception
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
	 
    {
	    DatetimeFormat df = new DatetimeFormat();
            
	   try {
			con = connect();
            int loginId = 0;
			String branchcode,   auditindex;

            branchcode = BranchCode; 
            loginId = Login_Id;
			Iterator it = this.getContainer().values().iterator();                                     // setCheckedChanges(true);
			//if (checkedChanges() == true) 
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next(); 
              if(fld.isChanged() == true)
                {	
		 isupdate = true;
                String tablename, columnName,  preval, newval;	String rowid = RowId;			
			    columnName = fld.getName(); preval = fld.getOldValue(); newval = fld.getNewValue(); tablename = TableName;
			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_UPDATE_AUDIT"
                            +"(AUDIT_INDEX,TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,LOGIN_ID,BRANCH_CODE,[DATE])"
                            +" values (?,?,?,?,?,?,?,?,?,?)";
					prepstmnt = con.prepareStatement(auditstr);
			 
					prepstmnt.setLong(1,System.currentTimeMillis());
					prepstmnt.setString(2, tablename);
					prepstmnt.setString(3, columnName);
					prepstmnt.setString(4, rowid);
					prepstmnt.setString(5, preval);
					prepstmnt.setString(6, newval);
                    prepstmnt.setDate(7, dbConnection.dateConvert(new java.util.Date()));
				    prepstmnt.setInt(8, loginId);
                    prepstmnt.setString(9, branchcode);
				    prepstmnt.setDate(10,dbConnection.dateConvert(new java.util.Date()));
				      
			   	   
					       
					rowsupdated = prepstmnt.executeUpdate();
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }    
			
				**/
			//	System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				//}
			// }
	

	/*
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally 
		{
		   dbConnection.closeConnection(con, prepstmnt,rs);
		}
		
		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;
	}//end method block
	*/	
  /**
   *   Logs audit trail : for use within servlet
   * @param     TableName as String  
   * @param     BranchCode as String  
   * @param     LoginId as int  
   * @param     RowId as String  
   * @author  Joshua O. Aruno.
    * @version 1.0
   */
	
  public boolean logAuditTrailOld(String TableName,  String BranchCode,  int Login_Id, String RowId,String hostName,String ipAddress,String macAddress) throws Exception 
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  descriptio 
 
  {
	  
//	  System.out.println("==================Enter audit=============");
	    DatetimeFormat df = new DatetimeFormat();
          
	   try {
//		   System.out.println("==================Enter audit=============0");
			con = connect();
//			System.out.println("==================Enter audit=============1");
          int loginId = 0;
			String branchcode,   auditindex;

          branchcode = BranchCode; 
          loginId = Login_Id;
			Iterator it = this.getContainer().values().iterator();                                     // setCheckedChanges(true);
			//if (checkedChanges() == true) 
//			System.out.println("==================Enter audit============= 2");
			while(it.hasNext())
			 {
//				System.out.println("==================Enter audit============= 3");
			  Field fld = (Field)it.next(); 
//			  System.out.println("==================Enter audit=============4");
            if(fld.isChanged() == true)
              {	
          	  
//          	  System.out.println("==================Enter audit============= 5");
          	  		isupdate = true;

 //               	  System.out.println("==================Enter audit============= 6");
                	  
              String tablename, columnName,  preval, newval;	String rowid = RowId;			
			    columnName = fld.getName(); 
			    preval = fld.getOldValue(); 
			    newval = fld.getNewValue();
			    tablename = TableName;
			    
//			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
//					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					//String auditstr = "insert into AM_AD_UPDATE_AUDIT"
                         // +"(AUDIT_INDEX,TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,LOGIN_ID,BRANCH_CODE,[DATE],ACT_PERFMD)"
                          //+" values (?,?,?,?,?,?,?,?,?,?,?)";
					
					String auditstr = "insert into AM_AD_UPDATE_AUDIT"
                      +"(TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,LOGIN_ID,BRANCH_CODE,[DATE],ACT_PERFMD,MACHINE_NAME,IP_ADDRESS,MAC_ADDRESS)"
                      +" values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					prepstmnt = con.prepareStatement(auditstr);
			 
					//prepstmnt.setLong(1,System.currentTimeMillis());
					prepstmnt.setString(1, tablename);
//					System.out.println("tablename ===="+tablename);
					prepstmnt.setString(2, columnName);
//					System.out.println("columnName ===="+columnName);
					prepstmnt.setString(3, rowid);
//					System.out.println("rowid ===="+rowid);
					prepstmnt.setString(4, preval);
//					System.out.println("preval ===="+preval);
					prepstmnt.setString(5, newval);
//					System.out.println("newval ===="+newval);
                    prepstmnt.setDate(6, dbConnection.dateConvert(new java.util.Date()));
 //                 System.out.println("EFF_DATE ===="+dbConnection.dateConvert(new java.util.Date()));
				    prepstmnt.setInt(7, loginId);
//					System.out.println("loginId ===="+loginId);
                    prepstmnt.setString(8, branchcode);
//                  System.out.println("branchcode ===="+branchcode);
				    prepstmnt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
//				    System.out.println("DATE ===="+dbConnection.dateConvert(new java.util.Date()));
				    prepstmnt.setString(10,"Updated");
//				    System.out.println("ACT_PERFMD ===="+"Updated");
				    prepstmnt.setString(11,hostName);  
				    prepstmnt.setString(12,ipAddress);  
				    prepstmnt.setString(13,macAddress);  
//				    System.out.println("about to be sucesss ");
					       
					rowsupdated = prepstmnt.executeUpdate();
					
//					System.out.println("update Sucessful "+rowsupdated);
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }    
			
				**/
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				}
			 }	
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
   			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally 
		{
		   dbConnection.closeConnection(con, prepstmnt,rs);
		}
		
		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;
	}//end method block
  
  
  public boolean logAuditTrail(String tableName, String branchCode, int loginId,
          String rowId, String hostName, String ipAddress, String macAddress) throws Exception {

boolean isUpdate = false;
java.sql.Date today = dbConnection.dateConvert(new java.util.Date());

String auditSQL = "INSERT INTO AM_AD_UPDATE_AUDIT " +
"(TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, LOGIN_ID, BRANCH_CODE, [DATE], ACT_PERFMD, MACHINE_NAME, IP_ADDRESS, MAC_ADDRESS) " +
"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection con = connect()) {

Iterator it = this.getContainer().values().iterator();

while (it.hasNext()) {
Field fld = (Field) it.next();
if (fld.isChanged()) {
isUpdate = true;

try (PreparedStatement ps = con.prepareStatement(auditSQL)) {
 ps.setString(1, tableName);
 ps.setString(2, fld.getName());
 ps.setString(3, rowId);
 ps.setString(4, fld.getOldValue());
 ps.setString(5, fld.getNewValue());
 ps.setDate(6, today);
 ps.setInt(7, loginId);
 ps.setString(8, branchCode);
 ps.setDate(9, today);
 ps.setString(10, "Updated");
 ps.setString(11, hostName);
 ps.setString(12, ipAddress);
 ps.setString(13, macAddress);

 ps.executeUpdate();
}
}
}

} catch (SQLException ex) {
System.out.println("SQLException in logAuditTrail:");
while (ex != null) {
System.out.println("Message: " + ex.getMessage());
System.out.println("SQLState: " + ex.getSQLState());
System.out.println("ErrorCode: " + ex.getErrorCode());
ex = ex.getNextException();
System.out.println();
}
throw ex; // rethrow to let caller handle
}

return isUpdate;
}
	
/**
 *   Logs audit trail : for use within bean
 * @param     AuditInfo    
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
	 public  boolean logAuditTrailOld(AuditInfo ai) throws Exception 
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    {
	   try {
			con = connect();  	int loginId = 0;
			String branchcode,   auditindex;
            branchcode = ai.getBranchCode(); loginId = ai.getLoginId();			
			Iterator it = this.getContainer().values().iterator();                                     // setCheckedChanges(true);
			//if (checkedChanges() == true) 
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next(); 
              if(fld.isChanged() == true)
                {	
				isupdate = true;
                String tablename , columnName,  preval, newval;	String rowid = ai.getRowId();			
			    columnName = fld.getName(); preval = ""; newval = fld.getNewValue(); tablename = ai.getTableName();
			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_INSERT_AUDIT values ( ?,  ?, ?,  ?, ?, ?, ?,   ?,getDate())";
					prepstmnt = con.prepareStatement(auditstr);
			 
					//prepstmnt.setString(1, auditindex);
					prepstmnt.setString(1, tablename);
					prepstmnt.setString(2, columnName);
					prepstmnt.setString(3, rowid);
					prepstmnt.setString(4, preval);
					prepstmnt.setString(5, newval);
					prepstmnt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
					prepstmnt.setInt(7, loginId);
					prepstmnt.setString(8, branchcode);
				    rowsupdated = prepstmnt.executeUpdate();
					
//					System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
					}
					
			/**      
				} else { System.out.println("\t\t-> no changes made....");   }    
			
				**/
				
				//}
			 }	
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally 
		{
		   dbConnection.closeConnection(con,prepstmnt, rs);
		}
		
		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;
	}//end method block
	
	
	 public boolean logAuditTrail(AuditInfo ai) throws Exception {
		    boolean isUpdate = false;
		    java.sql.Date today = dbConnection.dateConvert(new java.util.Date());

		    String auditSQL = "INSERT INTO AM_AD_INSERT_AUDIT " +
		            "(TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, LOGIN_ID, BRANCH_CODE) " +
		            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		    try (Connection con = connect()) {

		        Iterator it = this.getContainer().values().iterator();

		        while (it.hasNext()) {
		            Field fld = (Field) it.next();
		            if (fld.isChanged()) {
		                isUpdate = true;

		                try (PreparedStatement ps = con.prepareStatement(auditSQL)) {
		                    ps.setString(1, ai.getTableName());
		                    ps.setString(2, fld.getName());
		                    ps.setString(3, ai.getRowId());
		                    ps.setString(4, ""); // previous value is empty for insert
		                    ps.setString(5, fld.getNewValue());
		                    ps.setDate(6, today);
		                    ps.setInt(7, ai.getLoginId());
		                    ps.setString(8, ai.getBranchCode());

		                    ps.executeUpdate();
		                }
		            }
		        }

		    } catch (SQLException ex) {
		        System.out.println("\n--- SQLException in logAuditTrail ---\n");
		        while (ex != null) {
		            System.out.println("Message:   " + ex.getMessage());
		            System.out.println("SQLState:  " + ex.getSQLState());
		            System.out.println("ErrorCode: " + ex.getErrorCode());
		            ex = ex.getNextException();
		            System.out.println();
		        }
		        throw ex; 
		    }

		    return isUpdate;
		}
	
	public String selectxOld(String selstmnt) throws Exception
   {
	String strval = "";
	   try {
			con = connect();  String val; String nullstring = null;
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(selstmnt);
			int j = rs.getInt(2); 
			 strval = String.valueOf(j);   
		    }
		catch (Exception e) 
			{
            System.out.println("StandardQuery.select() -> Exception " + e + " occured");
            }
     finally { con.close(); dbConnection.closeConnection(con,prepstmnt, rs);}
	 return strval;
  }

  
	public String selectx(String selstmnt) throws Exception {
	    String strval = "";
	    
	    try (Connection con = connect();
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(selstmnt)) {

	        if (rs.next()) { 
	            int j = rs.getInt(2); 
	            strval = String.valueOf(j);
	        }

	    } catch (SQLException e) {
	        System.out.println("StandardQuery.selectx() -> SQLException: " + e.getMessage());
	        throw e; // propagate exception
	    }

	    return strval;
	}
	
  
  public final void updateLoginOld(int loginId, boolean loginlogout)
  {
     try
	   {
	     con = connect();
		 String updatequery = "";
	//	 System.out.println("loginId == "+loginId + "loginlogout == "+loginlogout);
		 if(loginlogout == false)		 
			{   updatequery = "UPDATE   AM_GB_USER SET   login_status = 0 WHERE user_Id = "+loginId;	
		//	System.out.println("I AM INSIDE LOGINLOGOUT FALSE");
			}
		else
		    {   updatequery = "UPDATE   AM_GB_USER SET   login_status = 1 WHERE user_Id = "+loginId;	
		//    System.out.println("I AM INSIDE LOGINLOGOUT TRUE");
		    }
		prepstmnt = con.prepareStatement(updatequery);
		rowsupdated = prepstmnt.executeUpdate();
		}
	 catch(Exception e) { e.printStackTrace(); 	} finally{ closeConnection(con,prepstmnt);}
	}
  
	     
  public final void updateLogin(int loginId, boolean loginlogout) {
	    String updateQuery = "UPDATE AM_GB_USER SET login_status = ? WHERE user_Id = ?";

	    try (Connection con = connect();
	         PreparedStatement ps = con.prepareStatement(updateQuery)) {

	        ps.setInt(1, loginlogout ? 1 : 0);
	        ps.setInt(2, loginId);

	        int rowsUpdated = ps.executeUpdate();
	       

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	  public final void updateLogin(int loginId, boolean loginlogout,String querypart1,String querypart2)
  {  // System.out.println("I AM INSIDE UPDATE LOGIN 2");
     try
	   {
	     con = connect();
		 StringBuffer updatestring = new StringBuffer(1000);
		 updatestring.append(querypart1);
		 if(loginlogout == logout)
			{  updatestring.append(" = 0 "); 	
//			System.out.println("I AM INSIDE LOGINLOGOUT 0 FALSE = "+querypart1);
			}
		else
		    {   updatestring.append(" = 1 ");	
//		    System.out.println("I AM INSIDE LOGINLOGOUT querypart1 TRUE = "+querypart1);
		    }
		  updatestring.append(querypart2);  
		  updatestring.append(" =  ");
		  updatestring.append(loginId);
		  String updatequery = updatestring.toString();
//		  System.out.println("I AM INSIDE LOGINLOGOUT updatequery TRUE = "+updatequery);
//		  System.out.println("I AM INSIDE LOGINLOGOUT querypart2 TRUE = "+querypart2);
		prepstmnt = con.prepareStatement(updatequery);
		rowsupdated = prepstmnt.executeUpdate();
		}
	 catch(Exception e) { e.printStackTrace(); 	} finally{ closeConnection(con,prepstmnt);}
	}
	
	
	public final void updateLogin(int loginId, boolean loginlogout,String querypart1 )
  {
     try
	   {
	     con = connect();
		 StringBuffer updatestring = new StringBuffer(1000);
//		 System.out.println("I AM INSIDE LOGINLOGOUT 3 TRUE = "+querypart1);
		  String updatequery = querypart1.toString();
		prepstmnt = con.prepareStatement(updatequery);
		rowsupdated = prepstmnt.executeUpdate();
		}
	 catch(Exception e) { e.printStackTrace(); 	} finally{ closeConnection(con,prepstmnt);}
	}


public void updateLoginAuditOld(int userid, String mtid) {

		PreparedStatement ps = null;
		boolean done = false;
		boolean done2 = false;
//	  	String sessionTimeOut = htmlUtil.getCodeName("select session_timeout from am_gb_company");
//		String queryTimeOut = "update gb_user_login set time_out = '"+df.getDateTime().substring(10)+"' where time_out is null and datediff(minute, time_in, '"+df.getDateTime().substring(10)+"') / 60.0 > "+sessionTimeOut+"";
//		System.out.print("<<<<<sessionTimeOut: "+sessionTimeOut+"   queryTimeOut: "+queryTimeOut);
		
//		String mtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  gb_user_login where USER_ID = "+userid+" ");
		String query = "UPDATE gb_user_login SET time_out = ? WHERE user_id =? AND MTID = ?";
		try {
			con = connect();
//			con = getConnection();
			ps = con.prepareStatement(query);
//			System.out.print("<<<<<getDateTime(): "+df.getDateTime().substring(10)+"   mtid: "+mtid);
			ps.setString(1,df.getDateTime().substring(10));
			ps.setInt(2, userid);
			ps.setString(3, mtid);
			done=( ps.executeUpdate()!=-1);
//			ps = con.prepareStatement(queryTimeOut);
//			done=( ps.executeUpdate()!=-1);
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating ab_user_login timeout ->"
					+ e.getMessage());
		}finally{ closeConnection(con,ps);} 
}


public void updateLoginAudit(int userId, String mtid) {
    String query = "UPDATE gb_user_login SET time_out = ? WHERE user_id = ? AND MTID = ?";

    
    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(query)) {

        
        
        ps.setString(1,df.getDateTime().substring(10));
        ps.setInt(2, userId);
        ps.setString(3, mtid);

        int updatedRows = ps.executeUpdate();
        if (updatedRows == 0) {
            System.out.println("No rows updated for userId=" + userId + ", MTID=" + mtid);
        }

    } catch (Exception e) {
        System.out.println(this.getClass().getName()
            + " ERROR: Error updating gb_user_login timeout -> " + e.getMessage());
        e.printStackTrace();
    }
}

	
  public final void updateLoginAuditOld(String SessionId, int LoginID)
  {
 	 String session_Id = htmlUtil.getCodeName("SELECT Session_id FROM  GB_USER_LOGIN   WHERE Session_Id = '"+SessionId+"' ");
 	 System.out.println("======>sessionId: "+session_Id);
 	 if(session_Id==""){
 	 String sessionIdMtid = htmlUtil.getCodeName("SELECT MAX(mtid) FROM  GB_USER_LOGIN   WHERE USER_ID = "+LoginID+" ");
 	 updateLoginAudit(LoginID, sessionIdMtid);
 	 System.out.println("======>sessionIdMtid: "+sessionIdMtid);
 	 } 

//	  System.out.println("I AM INSIDE updateLoginAudit SessionId 1 TRUE = "+SessionId+"     LoginID: "+LoginID);
     try
	   {
//    	 System.out.println("I AM INSIDE updateLoginAudit SessionId 2 TRUE = "+SessionId);
    	
//	    if(sessionIdExist(SessionId))
    	 if(session_Id!="")
			{
	        con = connect();
		    String updatequery = "";
		   updatequery = "UPDATE   GB_USER_LOGIN   SET time_out = getDate() WHERE session_Id = '"+SessionId+"' and   	user_Id = "+LoginID;
		   System.out.println("I AM INSIDE updateLoginAudit updatequery TRUE = "+updatequery);
			prepstmnt = con.prepareStatement(updatequery);
			// prepstmnt.setDate(1, new java.util.Date());
			rowsupdated = prepstmnt.executeUpdate();
			con.close();
			}
//	    dbConnection.closeConnection(con,prepstmnt, rs);
//	    con.close();
		}
	 catch(Exception e) { e.printStackTrace(); 	}finally{ closeConnection(con,prepstmnt);}
}
  
  
  public final void updateLoginAudit(String sessionId, int loginId) {
	    if (sessionId == null || sessionId.isEmpty()) return;

	    String selectSession = "SELECT Session_id FROM GB_USER_LOGIN WHERE Session_Id = ?";
	    String selectMaxMtid = "SELECT MAX(mtid) FROM GB_USER_LOGIN WHERE USER_ID = ?";
	    String updateQuery = "UPDATE GB_USER_LOGIN SET time_out = GETDATE() WHERE Session_Id = ? AND User_Id = ?";

	    try (Connection con = connect();
	         PreparedStatement psSelect = con.prepareStatement(selectSession)) {

	        
	        psSelect.setString(1, sessionId);
	        try (ResultSet rs = psSelect.executeQuery()) {
	            if (!rs.next()) {
	                
	                try (PreparedStatement psMax = con.prepareStatement(selectMaxMtid)) {
	                    psMax.setInt(1, loginId);
	                    try (ResultSet rsMax = psMax.executeQuery()) {
	                        if (rsMax.next()) {
	                            String mtid = rsMax.getString(1);
	                            updateLoginAudit(loginId, mtid); 
	                        }
	                    }
	                }
	                return; 
	            }
	        }

	     
	        try (PreparedStatement psUpdate = con.prepareStatement(updateQuery)) {
	            psUpdate.setString(1, sessionId);
	            psUpdate.setInt(2, loginId);
	            int rowsUpdated = psUpdate.executeUpdate();
	           
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


 public final boolean sessionIdExistOld(String SessionId)
 {
	String sessionId = "";
      try
	   {
	     con = connect();
		 String selstmnt = "";
		 selstmnt = "SELECT Session_id FROM  GB_USER_LOGIN   WHERE Session_Id = '"+SessionId+"'";
		 System.out.println("I AM INSIDE sessionIdExist SessionId TRUE = "+SessionId);
		 System.out.println("Querry selstmnt = "+selstmnt);
	     stmnt = con.createStatement();
		 rs = stmnt.executeQuery(selstmnt);
		sessionId = rs.getString(1);
//		 dbConnection.closeConnection(con,prepstmnt, rs);
		con.close();
		
		}  
	 catch(Exception e) { e.printStackTrace(); 	}
      System.out.println("======>>>>sessionId: "+sessionId);
	   if((sessionId == null) || (sessionId.equals("")) || (sessionId.equals(null)))
	      { return false ;}
		 else
		    { return true;	}
}
	
	
 public final boolean sessionIdExist(String sessionId) {
	    if (sessionId == null || sessionId.isEmpty()) return false;

	    String query = "SELECT 1 FROM GB_USER_LOGIN WHERE Session_Id = ?";
	    boolean exists = false;

	    try (Connection con = connect();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, sessionId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                exists = true; 
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    System.out.println("Session exists: " + exists + " for SessionId: " + sessionId);
	    return exists;
	}
	
	
	public final void captureAuditFields(String[] AuditFields) throws Exception 
	{
	   int x = 0;
	    // method 1
		/**
		for( x = 0; x < AuditFields.length; x++)
			{
			  this.setValue( ""  ,AuditField[x]);
			 } //method 1 ends
		**/
		
		//method 2 >> I think this method is better cause it get's the column name first
		//                  this method iterates on the field objects whose columname is already captured using a select statement
		Iterator it = this.getContainer().values().iterator();                                     
		while(it.hasNext())
			 {
			  Field fld = (Field)it.next(); 
			  String columnName = fld.getName();
			  this.setValue(columnName, AuditFields[x].trim() );
//			  System.out.println("NEW VALUE is "+AuditFields[x]);
			  x = x + 1;
			  } //method 2 ends
	}
	     

		 
		 
		public void auditLoginOld(int loginID, String branchcode, String workstationName, String workstationIp, String sessionId)
		{	 
			 try {
			      
					con = connect();
//					System.out.println("Connection >> "+con);
//					System.out.println("AuditTrailGen.loginID >> "+loginID);
//					System.out.println("AuditTrailGen.branchcode >> "+branchcode);
//					System.out.println("AuditTrailGen.workstationName >> "+workstationName);
//					System.out.println("AuditTrailGen.workstationIp >> "+workstationIp);
//					System.out.println("AuditTrailGen.sessionId >> "+sessionId);

					String insertqry = "INSERT INTO gb_user_login VALUES (?,?,?, getDate(),null, ?,?,?)";
					prepstmnt = con.prepareStatement(insertqry);
			 		
					prepstmnt.setDate(1,dbConnection.dateConvert(new java.util.Date()));
					prepstmnt.setInt(2, loginID);
					prepstmnt.setString(3, branchcode);
					//prepstmnt.setDate(6,dbConnection.dateConvert(""));
					prepstmnt.setString(4, workstationName);
					prepstmnt.setString(5, workstationIp);
					prepstmnt.setString(6, sessionId);
										
					 int rowsupdated2 = prepstmnt.executeUpdate();
		
				} catch(Exception e) 
				 { 
				 System.out.println("ERROR: auditLogin -:START:-");
				 e.printStackTrace(); 
				 System.out.println("ERROR: auditLogin -:STOP:-");
				 } 	
		
		finally 
		{
		   dbConnection.closeConnection(con,prepstmnt, rs);
		}
	}	 
		 
		 
		public void auditLogin(int loginID, String branchCode, String workstationName, String workstationIp, String sessionId) {
		    String insertQuery = "INSERT INTO gb_user_login " +
		            "(login_date, user_id, branch_code, time_in, time_out, workstation_name, ip_address, session_id) " +
		            "VALUES (?, ?, ?, GETDATE(), NULL, ?, ?, ?)";

		    try (Connection con = connect();
		         PreparedStatement ps = con.prepareStatement(insertQuery)) {

		        ps.setDate(1, dbConnection.dateConvert(new java.util.Date())); 
		        ps.setInt(2, loginID);                                         
		        ps.setString(3, branchCode);                                   
		        ps.setString(4, workstationName);                              
		        ps.setString(5, workstationIp);                                
		        ps.setString(6, sessionId);                                    

		        int rowsInserted = ps.executeUpdate();
		        System.out.println("auditLogin: rows inserted = " + rowsInserted);

		    } catch (SQLException e) {
		        System.out.println("ERROR: auditLogin failed");
		        e.printStackTrace();
		    }
		}
		 
		 public String getUserNameOld(int LoginId, String field, String tableName)
		 {
		   String username = "";
			try
			  {
			   con = connect(); 
			   String selstmnt = "SELECT "+field+" FROM "+tableName+ "WHERE login_id = "+LoginId;
				stmnt = con.createStatement();
				rs = stmnt.executeQuery(selstmnt);
				username = rs.getString(1);
			 } catch(Exception e) {e.printStackTrace();  } 	
          		 
		  finally 
		{
		   dbConnection.closeConnection(con,prepstmnt, rs);
		}
		 return username;	
	}	
				
		
		 public String getUserName(int loginId, String field, String tableName) {
			    String username = "";
			    
			    if (!field.matches("[a-zA-Z0-9_]+") || !tableName.matches("[a-zA-Z0-9_]+")) {
			        throw new IllegalArgumentException("Invalid field or table name");
			    }

			    String query = "SELECT " + field + " FROM " + tableName + " WHERE login_id = ?";

			    try (Connection con = connect();
			         PreparedStatement ps = con.prepareStatement(query)) {

			        ps.setInt(1, loginId);
			        try (ResultSet rs = ps.executeQuery()) {
			            if (rs.next()) {
			                username = rs.getString(1);
			            }
			        }

			    } catch (SQLException e) {
			        e.printStackTrace();
			    }

			    return username;
			}
		 
		 
    //overloaded method to take arraylist	
	/**
	public final void captureAuditFields(ArrayList AuditFields) throws Exception 
	{
	   int x = 0;
	    // method 1
		/**
		for( x = 0; x < AuditFields.length; x++)
			{
			  this.setValue( ""  ,AuditField[x]);
			 } //method 1 ends
		**/
		
		//method 2 >> I think this method is better cause it get's the column name first
		//                  this method iterates on the field objects whose columname is already captured using a select statement
		/**
		Iterator it = this.getContainer().values().iterator();                                     
		while(it.hasNext())
			 {
			  Field fld = (Field)it.next(); 
			  String columnName = fld.getName();
			  this.setValue(columnName, AuditFields[x].trim() );
			  x = x + 1;
			  } //method 2 ends
	}
	**/
	
	
public	final void updateOld(String updtstmnt)
    {
	  try{
	     con = connect();
		 prepstmnt = con.prepareStatement(updtstmnt);
		 rowsupdated = prepstmnt.executeUpdate();
		 if ( rowsupdated > 0)
		   { setCheckedChanges(true);  }
		 else {  setCheckedChanges(false); }
		 con.close();
		 }
	  
		 catch (Exception e){ e.printStackTrace(); }
	}
		 
		 
public final void update(String updtstmnt) {
    rowsupdated = 0; 

    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(updtstmnt)) {

        rowsupdated = ps.executeUpdate();
        setCheckedChanges(rowsupdated > 0);

    } catch (SQLException e) {
        e.printStackTrace();
        setCheckedChanges(false);
    }
}	 
		 
		 
	
/**
public static void main(String[] args)
  {
   try{
    AuditTrailGen ct = new AuditTrailGen();
     ct.connect();
    ct.select(1,"select * from general where Id = 3");//where Id = 3 
	ct.update("update general set category = 'Boise' where id = 3");
	ct.select(2,"select * from general  where Id = 3");
    ct.logAuditTrail( "Test", "318", "testing if Audit trail log is working", 8);

    }catch(Exception e){ e.printStackTrace();}

  }
 **/ 
    public final void updateLoginAuditOld(String SessionId, int LoginID, boolean loginlogout)
  {
     try
	   {
	    	con = connect();
			String updatequery = "";
			updatequery = "UPDATE   GB_USER_LOGIN   SET time_out = getDate() WHERE session_Id = '"+SessionId+"' and   	user_Id = "+LoginID;	
			prepstmnt = con.prepareStatement(updatequery);
			// prepstmnt.setDate(1, new java.util.Date());
			rowsupdated = prepstmnt.executeUpdate();
			
			}
	 catch(Exception e) { e.printStackTrace(); 	}
}

    
    public final void updateLoginAudit(String sessionId, int loginID, boolean loginlogout) {
        String updateQuery = "UPDATE GB_USER_LOGIN SET time_out = getDate() WHERE session_Id = ? AND user_Id = ?";
        rowsupdated = 0;

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(updateQuery)) {

            ps.setString(1, sessionId);
            ps.setInt(2, loginID);

            rowsupdated = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating login audit:");
            e.printStackTrace();
        }
    }


public boolean logAuditTrailOld(String TableName,  String BranchCode,  int LoginId, String RowId, String RecordId,String ActionPerformed, String hostName,String ipAddress, String macAddress) throws Exception
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    { 
	   try {
		   Connection con = dbConnection.getConnection("legendPlus");  	int loginId = 0;
			String branchcode,   auditindex; 
            branchcode = BranchCode.trim(); loginId = LoginId;
			Iterator it = this.getContainer().values().iterator();
			// setCheckedChanges(true);
			//if (checkedChanges() == true)
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next();
              if(fld.isChanged() == true)
                {
		 isupdate = true;
                String tablename, columnName,  preval, newval;	String rowid = RowId.trim();
			    columnName = fld.getName(); preval = fld.getOldValue(); newval = fld.getNewValue(); tablename = TableName.trim();
				String actionPerformed = ActionPerformed.trim();
//			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
//					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_UPDATE_AUDIT(TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,LOGIN_ID,BRANCH_CODE,DATE,ACT_PERFMD,MACHINE_NAME,IP_ADDRESS,MAC_ADDRESS) values ( ?,  ?, ?,  ?, ?, ?, ?,   ?, ?, ?, ?, ?, ?)";
					prepstmnt = con.prepareStatement(auditstr);

					//prepstmnt.setString(1, auditindex);
					prepstmnt.setString(1, tablename);
					prepstmnt.setString(2, columnName);
					prepstmnt.setString(3, rowid);
					prepstmnt.setString(4, preval);
					prepstmnt.setString(5, newval);
					prepstmnt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
					prepstmnt.setInt(7, loginId);
					prepstmnt.setString(8, branchcode);
				    prepstmnt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
					prepstmnt.setString(10, actionPerformed);
				    prepstmnt.setString(11,hostName);  
				    prepstmnt.setString(12,ipAddress);  
				    prepstmnt.setString(13,macAddress);  



					rowsupdated = prepstmnt.executeUpdate();
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }

				**/
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				}
			 }
			con.close();
			   dbConnection.closeConnection(con,prepstmnt, rs);
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally
		{
		   dbConnection.closeConnection(con, prepstmnt,rs);
		}

		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;
	}//end method block


public boolean logAuditTrail(String tableName, String branchCode, int loginId, String rowId,
        String recordId, String actionPerformed, String hostName,
        String ipAddress, String macAddress) {

boolean isUpdate = false;
String insertSQL = "INSERT INTO AM_AD_UPDATE_AUDIT("
+ "TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, "
+ "LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD, MACHINE_NAME, IP_ADDRESS, MAC_ADDRESS) "
+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection con = dbConnection.getConnection("legendPlus")) {

Iterator<Field> it = this.getContainer().values().iterator();

while (it.hasNext()) {
Field fld = it.next();
if (fld.isChanged()) {
isUpdate = true;

try (PreparedStatement ps = con.prepareStatement(insertSQL)) {
ps.setString(1, tableName.trim());
ps.setString(2, fld.getName());
ps.setString(3, rowId.trim());
ps.setString(4, fld.getOldValue());
ps.setString(5, fld.getNewValue());
ps.setDate(6, dbConnection.dateConvert(new java.util.Date()));
ps.setInt(7, loginId);
ps.setString(8, branchCode.trim());
ps.setDate(9, dbConnection.dateConvert(new java.util.Date()));
ps.setString(10, actionPerformed.trim());
ps.setString(11, hostName);
ps.setString(12, ipAddress);
ps.setString(13, macAddress);

ps.executeUpdate();
}
}
}

} catch (SQLException e) {
System.out.println("Error in logAuditTrail:");
e.printStackTrace();
}

return isUpdate;
}



//Ganiyu's code
public boolean logAuditTrailActionPerformedOld(String TableName,  String BranchCode,  int LoginId, String RowId,String action_performed) throws Exception
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    {
	   DatetimeFormat df = new DatetimeFormat();

	   try {
		   Connection con = dbConnection.getConnection("legendPlus");  	int loginId = 0;
			String branchcode,   auditindex;
            branchcode = BranchCode; loginId = LoginId;
			Iterator it = this.getContainer().values().iterator();                                     // setCheckedChanges(true);
			//if (checkedChanges() == true)
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next();
              if(fld.isChanged() == true)
                {
		 isupdate = true;
                String tablename, columnName,  preval, newval;	String rowid = RowId;
                String actionPerformed =action_performed;
			    columnName = fld.getName(); preval = fld.getOldValue(); newval = fld.getNewValue(); tablename = TableName;
//			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
//					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_UPDATE_AUDIT values (?,?,?,?,?,?,?,?,?,?,?)";
					prepstmnt = con.prepareStatement(auditstr);

					prepstmnt.setLong(1,System.currentTimeMillis());
					prepstmnt.setString(2, tablename);
					prepstmnt.setString(3, columnName);
					prepstmnt.setString(4, rowid);
					prepstmnt.setString(5, preval);
					prepstmnt.setString(6, newval);
                    prepstmnt.setDate(7, dbConnection.dateConvert(new java.util.Date()));
				    prepstmnt.setLong(8, loginId);
                    prepstmnt.setString(9, branchcode);
				    prepstmnt.setDate(10,dbConnection.dateConvert(new java.util.Date()));
				    prepstmnt.setString(11, actionPerformed);


					rowsupdated = prepstmnt.executeUpdate();
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }

				**/
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				}
			 }
			   dbConnection.closeConnection(con,prepstmnt, rs);
			   con.close();
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally
		{
		   dbConnection.closeConnection(con, prepstmnt,rs);
		}

		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;  
	}//end method block


public boolean logAuditTrailActionPerformed(String tableName, String branchCode, int loginId, 
        String rowId, String actionPerformed) {

boolean isUpdate = false;
String insertSQL = "INSERT INTO AM_AD_UPDATE_AUDIT("
+ "AUDIT_INDEX, TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, "
+ "EFF_DATE, LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD) "
+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection con = dbConnection.getConnection("legendPlus")) {

Iterator<Field> it = this.getContainer().values().iterator();

while (it.hasNext()) {
Field fld = it.next();
if (fld.isChanged()) {
isUpdate = true;

try (PreparedStatement ps = con.prepareStatement(insertSQL)) {
ps.setLong(1, System.currentTimeMillis());          
ps.setString(2, tableName.trim());
ps.setString(3, fld.getName());
ps.setString(4, rowId.trim());
ps.setString(5, fld.getOldValue());
ps.setString(6, fld.getNewValue());
ps.setDate(7, dbConnection.dateConvert(new java.util.Date()));
ps.setInt(8, loginId);
ps.setString(9, branchCode.trim());
ps.setDate(10, dbConnection.dateConvert(new java.util.Date()));
ps.setString(11, actionPerformed.trim());

ps.executeUpdate();
}
}
}

} catch (SQLException e) {
System.out.println("Error in logAuditTrailActionPerformed:");
e.printStackTrace();
}

return isUpdate;
}

public String getAuditDescriptionOld(String role_id) throws Exception{
	//Connection con = null;
//	System.out.println("\n\n\n\n it entered  in getAuditDescription");
	
	String result = "";
	try {
		con = connect();
		String selstatement = "";
		//selstatement = "SELECT Session_id FROM  GB_USER_LOGIN   WHERE Session_Id = '"
			//	+ SessionId + "'";
		selstatement= "select convert(varchar,role_uuid) +' - '+role_name "+
		"from am_ad_privileges " +
		"where role_uuid  in (select role_uuid  from am_ad_class_privileges where role_uuid = "+Integer.parseInt(role_id)+")";
		
		stmnt = con.createStatement();
		rs = stmnt.executeQuery(selstatement);
		while(rs.next()){
		result = rs.getString(1);
		}
		   dbConnection.closeConnection(con,stmnt, rs);
	} catch (Exception e) {
		System.out.println("Error occured in getAuditDescription>>>>>>>>>>>>>>" );
		e.printStackTrace();
	}
	finally{
		
		if(rs != null)
		{
			rs.close();
		}
		
		if(stmnt != null)
		{
			stmnt.close();
		}
		
		if(con != null)
		{
			con.close();
		}
		 dbConnection.closeConnection(con,stmnt, rs);	
		
	}
//	System.out.println("the description for audit is >>>>>>>>>>>>>>>>>>>>>>>>>> " +result );
		return result;
	
}

public String getAuditDescription(String roleId) {
    String result = "";
    String query = "SELECT CONVERT(varchar, role_uuid) + ' - ' + role_name " +
                   "FROM am_ad_privileges " +
                   "WHERE role_uuid IN (SELECT role_uuid FROM am_ad_class_privileges WHERE role_uuid = ?)";

    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setInt(1, Integer.parseInt(roleId));
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        }

    } catch (Exception e) {
        System.out.println("Error occurred in getAuditDescription:");
        e.printStackTrace();
    }

    return result;
}

public String getAuditDescriptionDeptOld(String dept_code) throws Exception{
	//Connection con = null;
//	System.out.println("\n\n\n\n it entered  in getAuditDescription");
//	System.out.println("\n\n\n\n it entered  in getAuditDescription");
//	System.out.println("\n\n\n\n it entered  in getAuditDescription");
//	System.out.println("\n\n\n\n it entered  in getAuditDescription");
	String result = "";
	try {
		con = connect();
		String selstatement = "";
		
		selstatement= "select convert(varchar,dept_Code) +' - '+Dept_name"+
		"from am_ad_department"+
		"where dept_Code in (select deptCode from sbu_branch_dept where deptCode ="+Integer.parseInt(dept_code)+")";
		stmnt = con.createStatement();
		rs = stmnt.executeQuery(selstatement);
		
		
		//selstatement = "SELECT Session_id FROM  GB_USER_LOGIN   WHERE Session_Id = '"
		//	+ SessionId + "'";
		//selstatement= "select convert(varchar,role_uuid) +' - '+role_name "+
		//"from am_ad_privileges " +
		//"where role_uuid  in (select role_uuid  from am_ad_class_privileges where role_uuid = "+Integer.parseInt(role_id)+")";
		while(rs.next()){
		result = rs.getString(1);
		}
		 dbConnection.closeConnection(con,stmnt, rs);		
	} catch (Exception e) {
		System.out.println("Error occured in getAuditDescription>>>>>>>>>>>>>>" );
		e.printStackTrace();
	}
	finally{
		
		if(rs != null)
		{
			rs.close();
		}
		
		if(stmnt != null)
		{
			stmnt.close();
		}
		
		if(con != null)
		{
			con.close();
		}
		 dbConnection.closeConnection(con,stmnt, rs);
		
	}
//	System.out.println("the description for audit is >>>>>>>>>>>>>>>>>>>>>>>>>> " +result );
		return result;
	
}

public String getAuditDescriptionDept(String deptCode) {
    String result = "";
    String query = "SELECT CONVERT(varchar, dept_Code) + ' - ' + Dept_name " +
                   "FROM am_ad_department " +
                   "WHERE dept_Code IN (SELECT deptCode FROM sbu_branch_dept WHERE deptCode = ?)";

    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setInt(1, Integer.parseInt(deptCode));

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        }

    } catch (NumberFormatException nfe) {
        System.out.println("Invalid department code: " + deptCode);
    } catch (Exception e) {
        System.out.println("Error occurred in getAuditDescriptionDept:");
        e.printStackTrace();
    }

    return result;
}


public void logAuditTrailSecurityCompOld(String TableName, String BranchCode,int LoginId,String eff_date,String role_id,String oldvalue,String newvalue, String coulmname) 
throws Exception {
	
//	System.out.println("=========Entered logAuditTrailSecurityComp METHOD==========");
    PreparedStatement pstmt = null;
    Connection con2 = null;

	try {
		con2 = connect();
		//String loginId = "UnKnown";
		
		int loginId = 0;
        loginId = LoginId;
        
		String branchcode =BranchCode;
	
		String description = getAuditDescription(role_id);
//		System.out.println("=========Entered getAuditDescription METHOD=========="+description);
				
				
				String auditstr = "insert into AM_AD_UPDATE_AUDIT("+
				  "TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,"+
				  "LOGIN_ID,BRANCH_CODE,DATE,ACT_PERFMD,DESCRIPTION)values ( "+
			      "?, ?, ?,  ?, ?, ?, ?, ?, ?,?,?)";
				pstmt = con2.prepareStatement(auditstr);
				System.out.println("checking... Preparedstatement .."+pstmt+"con2"+con2);
				
				pstmt.setString(1, TableName);
//				System.out.println("Table name"+TableName);
				pstmt.setString(2, coulmname);
//				System.out.println("ColumnName name"+coulmname);
				pstmt.setString(3, role_id);
//				System.out.println("Row ID "+role_id);
				pstmt.setString(4, oldvalue);
//				System.out.println("Previous Value "+oldvalue);
				pstmt.setString(5, newvalue);
//				System.out.println("new Value "+newvalue);
				pstmt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Effective Date "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setInt(7, loginId);
//				System.out.println("Login Id"+loginId);
				pstmt.setString(8, BranchCode);
//				System.out.println("BranchCode "+branchcode);
				pstmt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Dates VALUES "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setString(10, "Updated");
				pstmt.setString(11,description );
//				System.out.println("DESCRIPTION "+description);
				rowsupdated = pstmt.executeUpdate();
				
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				 dbConnection.closeConnection(con2,pstmt, rs);			
		
	}catch (SQLException ex)
	    {
		// start catch block
		System.out.println("\n--- SQLException caught ---\n"+ex);
	}finally {
			if(con2 != null)
			{
				con2.close();
			}
			if(pstmt != null)
			{
			    pstmt.close();
			}
			dbConnection.closeConnection(con2,pstmt, rs);
	  }

}


public void logAuditTrailSecurityComp(
        String tableName,
        String branchCode,
        int loginId,
        String effDate,
        String roleId,
        String oldValue,
        String newValue,
        String columnName) {

    String description = "";
    String auditQuery = "INSERT INTO AM_AD_UPDATE_AUDIT (" +
            "TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, " +
            "LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD, DESCRIPTION) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = connect();
         PreparedStatement pstmt = con.prepareStatement(auditQuery)) {

        // Fetch description safely
        description = getAuditDescription(roleId);

        pstmt.setString(1, tableName);
        pstmt.setString(2, columnName);
        pstmt.setString(3, roleId);        
        pstmt.setString(4, oldValue);
        pstmt.setString(5, newValue);
        pstmt.setDate(6, dbConnection.dateConvert(new java.util.Date())); 
        pstmt.setInt(7, loginId);
        pstmt.setString(8, branchCode);
        pstmt.setDate(9, dbConnection.dateConvert(new java.util.Date())); 
        pstmt.setString(10, "Updated");    
        pstmt.setString(11, description);

        int rowsUpdated = pstmt.executeUpdate();
        

    } catch (SQLException ex) {
        System.out.println("SQL Exception in logAuditTrailSecurityComp: " + ex.getMessage());
        ex.printStackTrace();
    } catch (Exception e) {
        System.out.println("Exception in logAuditTrailSecurityComp: " + e.getMessage());
        e.printStackTrace();
    }
}

public void logAuditTrailSecurityCompOld(String TableName, String BranchCode,int LoginId,String eff_date,String role_id,String oldvalue,String newvalue, String coulmname, String description) 
throws Exception {
	
//	System.out.println("=========Entered logAuditTrailSecurityComp METHOD==========");
    PreparedStatement pstmt = null;
    Connection con2 = null;

	try {
		con2 = connect();
		//String loginId = "UnKnown";
		
		int loginId = 0;
        loginId = LoginId;
        
		String branchcode =BranchCode;
	
//		System.out.println("=========Entered getAuditDescription METHOD=========="+description);
				
				
				String auditstr = "insert into AM_AD_UPDATE_AUDIT("+
				  "TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,"+
				  "LOGIN_ID,BRANCH_CODE,DATE,ACT_PERFMD,DESCRIPTION)values ( "+
			      "?, ?, ?,  ?, ?, ?, ?, ?, ?,?,?)";
				pstmt = con2.prepareStatement(auditstr);
				System.out.println("checking... Preparedstatement .."+pstmt+"con2"+con2);
				
				pstmt.setString(1, TableName);
//				System.out.println("Table name"+TableName);
				pstmt.setString(2, coulmname);
//				System.out.println("ColumnName name"+coulmname);
				pstmt.setString(3, role_id);
//				System.out.println("Row ID "+role_id);
				pstmt.setString(4, oldvalue);
//				System.out.println("Previous Value "+oldvalue);
				pstmt.setString(5, newvalue);
//				System.out.println("new Value "+newvalue);
				pstmt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Effective Date "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setInt(7, loginId);
//				System.out.println("Login Id"+loginId);
				pstmt.setString(8, BranchCode);
//				System.out.println("BranchCode "+branchcode);
				pstmt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Dates VALUES "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setString(10, "Updated");
				pstmt.setString(11,description );
//				System.out.println("DESCRIPTION "+description);
				rowsupdated = pstmt.executeUpdate();
				
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				 dbConnection.closeConnection(con2,pstmt, rs);			
		
	}catch (SQLException ex)
	    {
		// start catch block
		System.out.println("\n--- SQLException caught ---\n"+ex);
	}finally {
			if(con2 != null)
			{
				con2.close();
			}
			if(pstmt != null)
			{
			    pstmt.close();
			}
			dbConnection.closeConnection(con2,pstmt, rs);
	  }

}

public void logAuditTrailSecurityComp(
        String tableName,
        String branchCode,
        int loginId,
        String effDate,
        String roleId,
        String oldValue,
        String newValue,
        String columnName,
        String description) {

    String auditSql = "INSERT INTO AM_AD_UPDATE_AUDIT (" +
            "TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, " +
            "LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD, DESCRIPTION) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = connect();
         PreparedStatement pstmt = con.prepareStatement(auditSql)) {

        pstmt.setString(1, tableName);
        pstmt.setString(2, columnName);
        pstmt.setString(3, roleId);
        pstmt.setString(4, oldValue);
        pstmt.setString(5, newValue);
        pstmt.setDate(6, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setInt(7, loginId);
        pstmt.setString(8, branchCode);
        pstmt.setDate(9, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setString(10, "Updated");
        pstmt.setString(11, description);

        pstmt.executeUpdate();

    } catch (SQLException ex) {
        System.out.println("SQL Exception in logAuditTrailSecurityComp:");
        ex.printStackTrace();
    }
}

public void logAuditTrailSecurityComp_DeptOld(String TableName, String BranchCode,int LoginId,String eff_date,String dept_code,String oldvalue,String newvalue, String coulmname) 
throws Exception {
	
//	System.out.println("=========Entered logAuditTrailSecurityComp_Dept METHOD==========");
    PreparedStatement pstmt = null;
    Connection con2 = null;

	try {
		con2 = connect();
		//String loginId = "UnKnown";
		
		int loginId = 0;
        loginId = LoginId;
        
		String branchcode =BranchCode;
	
		String description = getAuditDescription2(dept_code);
//		System.out.println("=========Entered getAuditDescriptionDept METHOD=========="+description);
				
				
				String auditstr = "insert into AM_AD_UPDATE_AUDIT("+
				  "TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,"+
				  "LOGIN_ID,BRANCH_CODE,DATE,ACT_PERFMD,DESCRIPTION)values ( "+
			      "?, ?, ?,  ?, ?, ?, ?, ?, ?,?,?)";
				pstmt = con2.prepareStatement(auditstr);
//				System.out.println("checking... Preparedstatement .."+pstmt+"con2"+con2);
				
				pstmt.setString(1, TableName);
//				System.out.println("Table name"+TableName);
				pstmt.setString(2, coulmname);
//				System.out.println("ColumnName name"+coulmname);
				pstmt.setString(3, dept_code);
//				System.out.println("Row ID "+dept_code);
				pstmt.setString(4, oldvalue);
//				System.out.println("Previous Value "+oldvalue);
				pstmt.setString(5, newvalue);
//				System.out.println("new Value "+newvalue);
				pstmt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Effective Date "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setInt(7, loginId);
//				System.out.println("Login Id"+loginId);
				pstmt.setString(8, BranchCode);
//				System.out.println("BranchCode "+branchcode);
				pstmt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Dates VALUES "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setString(10, "Updated");
				pstmt.setString(11,description );
//				System.out.println("DESCRIPTION "+description);
				rowsupdated = pstmt.executeUpdate();
				
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
			
				dbConnection.closeConnection(con2,pstmt, rs);
	}catch (SQLException ex)
	    {
		// start catch block
		System.out.println("\n--- SQLException caught ---\n"+ex);
	}finally {
			if(con2 != null)
			{
				con2.close();
			}
			if(pstmt != null)
			{
			    pstmt.close();
			}
			dbConnection.closeConnection(con2,pstmt, rs);
	  }

}


public void logAuditTrailSecurityComp_Dept(
        String tableName,
        String branchCode,
        int loginId,
        String effDate,
        String deptCode,
        String oldValue,
        String newValue,
        String columnName) {

    String auditSql = "INSERT INTO AM_AD_UPDATE_AUDIT (" +
            "TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, " +
            "LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD, DESCRIPTION) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = connect();
         PreparedStatement pstmt = con.prepareStatement(auditSql)) {

        String description = getAuditDescription2(deptCode);

        pstmt.setString(1, tableName);
        pstmt.setString(2, columnName);
        pstmt.setString(3, deptCode);
        pstmt.setString(4, oldValue);
        pstmt.setString(5, newValue);
        pstmt.setDate(6, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setInt(7, loginId);
        pstmt.setString(8, branchCode);
        pstmt.setDate(9, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setString(10, "Updated");
        pstmt.setString(11, description);

        pstmt.executeUpdate();

    } catch (SQLException ex) {
        System.out.println("SQL Exception in logAuditTrailSecurityComp_Dept:");
        ex.printStackTrace();
    } catch (Exception e) {
        System.out.println("Error fetching department description:");
        e.printStackTrace();
    }
}


public void logAuditTrailSecurityComp_SbuOld(String TableName, String BranchCode,int LoginId,String eff_date,String sbu_code,String oldvalue,String newvalue, String coulmname) 
throws Exception { 
	
//	System.out.println("=========Entered logAuditTrailSecurityComp_Sbu METHOD==========");
    PreparedStatement pstmt = null;
    Connection con2 = null;

	try {
		con2 = connect();
		//String loginId = "UnKnown";
		
		int loginId = 0;
        loginId = LoginId;
        
		String branchcode =BranchCode;
	
		String description = getAuditDescriptionSbu(sbu_code);
//		System.out.println("=========Entered logAuditTrailSecurityComp_Sbu METHOD=========="+description);
				
				
				String auditstr = "insert into AM_AD_UPDATE_AUDIT("+
				  "TABLE_NAME,COLUMN_NAME,ROW_ID,PREV_VAL,NEW_VAL,EFF_DATE,"+
				  "LOGIN_ID,BRANCH_CODE,DATE,ACT_PERFMD,DESCRIPTION)values ( "+
			      "?, ?, ?,  ?, ?, ?, ?, ?, ?,?,?)";
				pstmt = con2.prepareStatement(auditstr);
//				System.out.println("checking... Preparedstatement .."+pstmt+"con2"+con2);
				
				pstmt.setString(1, TableName);
//				System.out.println("Table name"+TableName);
				pstmt.setString(2, coulmname);
//				System.out.println("ColumnName name"+coulmname);
				pstmt.setString(3, sbu_code);
//				System.out.println("Row ID "+sbu_code);
				pstmt.setString(4, oldvalue);
//				System.out.println("Previous Value "+oldvalue);
				pstmt.setString(5, newvalue);
//				System.out.println("new Value "+newvalue);
				pstmt.setDate(6,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Effective Date "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setInt(7, loginId);
//				System.out.println("Login Id"+loginId);
				pstmt.setString(8, BranchCode);
//				System.out.println("BranchCode "+branchcode);
				pstmt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
//				System.out.println("Dates VALUES "+dbConnection.dateConvert(new java.util.Date()));
				pstmt.setString(10, "Updated");
				pstmt.setString(11,description );
//				System.out.println("DESCRIPTION "+description);
				rowsupdated = pstmt.executeUpdate();
				
//				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				dbConnection.closeConnection(con2,pstmt, rs);	
		
	}catch (SQLException ex)
	    {
		// start catch block
		System.out.println("\n--- SQLException caught ---\n"+ex);
	}finally {
			if(con2 != null)
			{
				con2.close();
			}
			if(pstmt != null)
			{
			    pstmt.close();
			}
			dbConnection.closeConnection(con2,pstmt, rs);
	  }

}

public void logAuditTrailSecurityComp_Sbu(
        String tableName,
        String branchCode,
        int loginId,
        String effDate,
        String sbuCode,
        String oldValue,
        String newValue,
        String columnName) {

    String auditSql = "INSERT INTO AM_AD_UPDATE_AUDIT (" +
            "TABLE_NAME, COLUMN_NAME, ROW_ID, PREV_VAL, NEW_VAL, EFF_DATE, " +
            "LOGIN_ID, BRANCH_CODE, DATE, ACT_PERFMD, DESCRIPTION) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = connect();
         PreparedStatement pstmt = con.prepareStatement(auditSql)) {

        String description = getAuditDescriptionSbu(sbuCode);

        pstmt.setString(1, tableName);
        pstmt.setString(2, columnName);
        pstmt.setString(3, sbuCode);
        pstmt.setString(4, oldValue);
        pstmt.setString(5, newValue);
        pstmt.setDate(6, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setInt(7, loginId);
        pstmt.setString(8, branchCode);
        pstmt.setDate(9, dbConnection.dateConvert(new java.util.Date()));
        pstmt.setString(10, "Updated");
        pstmt.setString(11, description);

        pstmt.executeUpdate();

    } catch (SQLException ex) {
        System.out.println("SQL Exception in logAuditTrailSecurityComp_Sbu:");
        ex.printStackTrace();
    } catch (Exception e) {
        System.out.println("Error getting SBU description:");
        e.printStackTrace();
    }
}



public String getAuditDescription2Old(String dept_code) throws Exception{
	//Connection con = null;

	
	String result = "";
	try {
		con = connect();
		String selstatement = "";
		
		
		selstatement = "select convert(varchar,dept_Code) +' - '+Dept_name from am_ad_department where dept_Code in (select deptCode from sbu_branch_dept where deptCode ='"+dept_code+"')";  
		
		stmnt = con.createStatement();
		rs = stmnt.executeQuery(selstatement);
		while(rs.next()){
		result = rs.getString(1);
		}
		dbConnection.closeConnection(con,stmnt, rs);
	} catch (Exception e) {
		System.out.println("Error occured in getAuditDescription>>>>>>>>>>>>>>" );
		e.printStackTrace();
	}
	finally{
		
		if(rs != null)
		{
			rs.close();
		}
		
		if(stmnt != null)
		{
			stmnt.close();
		}
		
		if(con != null)
		{
			con.close();
		}
		dbConnection.closeConnection(con,stmnt, rs);
		
	}
//	System.out.println("the description for audit is >>>>>>>>>>>>>>>>>>>>>>>>>> " +result );
		return result;
	
}

public String getAuditDescription2(String deptCode) {

    String result = "";
    String query = "SELECT CONVERT(varchar, dept_Code) + ' - ' + Dept_name " +
                   "FROM am_ad_department " +
                   "WHERE dept_Code IN (" +
                   "    SELECT deptCode FROM sbu_branch_dept WHERE deptCode = ?" +
                   ")";

    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, deptCode);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error in getAuditDescription2:");
        e.printStackTrace();
    }

    return result;
}

public String getAuditDescriptionSbuOld(String sbu_code) throws Exception{
	//Connection con = null;


//	System.out.println("Eneterd in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
//	System.out.println("Entered in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
//	System.out.println("Entered in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
//	System.out.println("Entered  in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
//	System.out.println("Entered  in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
	String result = "";
	try {
		con = connect();
		String selstatement = "";
		//selstatement = "select convert(varchar,dept_Code) +' - '+Dept_name from am_ad_department where dept_Code in (select deptCode from sbu_branch_dept where deptCode ='"+dept_code+"')";  
		selstatement ="select convert(varchar,Sbu_Code) +' - '+Sbu_name from Sbu_SetUp where Sbu_Code in (select Sbu_Code from AM_SBU_ATTACHEMENT where Sbu_Code ='"+sbu_code+"')"; 
		stmnt = con.createStatement();
		rs = stmnt.executeQuery(selstatement);
		while(rs.next()){
		result = rs.getString(1);
		}
		dbConnection.closeConnection(con,stmnt, rs);
	} catch (Exception e) {
		System.out.println("Error occured in getAuditDescriptionSbu>>>>>>>>>>>>>>" );
		e.printStackTrace();
	}
	finally{
		
		if(rs != null)
		{
			rs.close();
		}
		
		if(stmnt != null)
		{
			stmnt.close();
		}
		
		if(con != null)
		{
			con.close();
		}
		dbConnection.closeConnection(con,stmnt, rs);
		
	}
//	System.out.println("the description for audit is >>>>>>>>>>>>>>>>>>>>>>>>>> " +result );
		return result;
	
}

public String getAuditDescriptionSbu(String sbuCode) {

    String result = "";

    String query = "SELECT CONVERT(varchar, Sbu_Code) + ' - ' + Sbu_name " +
                   "FROM Sbu_SetUp " +
                   "WHERE Sbu_Code IN (" +
                   "    SELECT Sbu_Code FROM AM_SBU_ATTACHEMENT WHERE Sbu_Code = ?" +
                   ")";

    try (Connection con = connect();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, sbuCode);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error in getAuditDescriptionSbu:");
        e.printStackTrace();
    }

    return result;
}

public boolean logAuditTrailOld(String TableName,  String BranchCode,  int LoginId, String RowId, String RecordId,String ActionPerformed) throws Exception
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    { 
	   try {
			con = connect();  	int loginId = 0;
			String branchcode,   auditindex; 
            branchcode = BranchCode.trim(); loginId = LoginId;
			Iterator it = this.getContainer().values().iterator();
			// setCheckedChanges(true);
			//if (checkedChanges() == true)
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next();
              if(fld.isChanged() == true)
                {
		 isupdate = true;
                String tablename, columnName,  preval, newval;	String rowid = RowId.trim();
			    columnName = fld.getName(); preval = fld.getOldValue(); newval = fld.getNewValue(); tablename = TableName.trim();
				String actionPerformed = ActionPerformed.trim();
//			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
//					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_UPDATE_AUDIT values ( ?,  ?, ?,  ?, ?, getDate(), ?,   ?, ?, ?)";
					prepstmnt = con.prepareStatement(auditstr);

					//prepstmnt.setString(1, auditindex);
					prepstmnt.setString(1, tablename);
					prepstmnt.setString(2, columnName);
					prepstmnt.setString(3, rowid);
					prepstmnt.setString(4, preval);
					prepstmnt.setString(5, newval);
					prepstmnt.setInt(6, loginId);
					prepstmnt.setString(7, branchcode);
				     prepstmnt.setDate(8,dbConnection.dateConvert(new java.util.Date()));
					 prepstmnt.setString(9, actionPerformed);



					rowsupdated = prepstmnt.executeUpdate();
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }

				**/
				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
				}
			 }
			con.close();
			   dbConnection.closeConnection(con,prepstmnt, rs);
			 } catch(SQLException ex) {//start catch block
					System.out.println("\n--- SQLException caught ---\n");
					while (ex != null) {//start while block
								System.out.println("Message:   "
														+ ex.getMessage ());
								System.out.println("SQLState:  "
														+ ex.getSQLState ());
								System.out.println("ErrorCode: "
														+ ex.getErrorCode ());
								ex = ex.getNextException();
								System.out.println("");
							}//end while block
     			}//end catch block
		//}catch(Exception e){ e.printStackTrace(); }
		finally
		{
		   dbConnection.closeConnection(con, prepstmnt,rs);
		}

		//if(rowsupdated > 0)
		//	{ return true;	}
		//else	{ return false;	}
		return isupdate;
	}//end method block


public boolean logAuditTrail(String tableName,
        String branchCode,
        int loginId,
        String rowId,
        String recordId,
        String actionPerformed) {

boolean isUpdate = false;

String auditSql = "INSERT INTO AM_AD_UPDATE_AUDIT " +
"(TABLE_NAME, COLUMN_NAME, ROW_ID, PRE_VALUE, CUR_VALUE, " +
" LOGIN_ID, BRANCH_CODE, EFF_DATE, ACTION_PERFORMED) " +
"VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";

try (Connection con = connect();
PreparedStatement ps = con.prepareStatement(auditSql)) {

con.setAutoCommit(false);  

Iterator<Field> it = this.getContainer().values().iterator();

while (it.hasNext()) {

Field fld = it.next();

if (fld.isChanged()) {

isUpdate = true;

ps.setString(1, tableName.trim());
ps.setString(2, fld.getName());
ps.setString(3, rowId.trim());
ps.setString(4, fld.getOldValue());
ps.setString(5, fld.getNewValue());
ps.setInt(6, loginId);
ps.setString(7, branchCode.trim());
ps.setString(8, actionPerformed.trim());

ps.addBatch();   
}
}

ps.executeBatch();   
con.commit();

} catch (SQLException ex) {
ex.printStackTrace();
}

return isUpdate;
}



}
