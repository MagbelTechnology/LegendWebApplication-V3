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



/**
 * <p>Title: LEGEND</p>
 * <p>Description: LEGEND Fixed Asset Management System </p>
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 * <p>Company: Magbel Technologies Limited.</p>
*  @author  Bolaji L. Ogeyingbo.
 * @version 1.0
 */
public class AuditTrailGen_14_09_09 extends Bag{
  
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

 
  
 private   int cType = 0;  private int colIndex = 0;
 private Field field ; String tabnm = "";



 // private Connection con = null;
    private String jndiName = "FixedAsset";
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
  public final Connection connect()  throws Exception
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



  //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
   //   con = DriverManager.getConnection("jdbc:sqlserver:"+dsn)

  
 /**
 *  Select all the values of the row on which data is altered into this object
  * @param   updatestatus as int  
 * @param    selectstmnt as String  
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
  public final void select(int updatestatus, String selstmnt) throws Exception
   {
	   try {
			con = connect();  String val = ""; String nullstring = null;
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(selstmnt);
			 ResultSetMetaData rsmd = rs.getMetaData();
	        int numColumns = rsmd.getColumnCount();
			//String tabnam = "General"; //rsmd.getTableName(1);
			//this.setTableName(tabnam); tabnm = tabnam;
            System.out.println("Number of Columns is: "+numColumns+"\t");
			System.out.println("Reference table is: "+this.getTableName());
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
			                 System.out.println("Database warning: String column type with a null value");                                    
                                         } 
			                this.setField(cname, strval ,columntype ,  columnid  );
			                                                System.out.println("\tColumnvalue: "+strval.trim()+"\n");
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
					                                    System.out.println("Database warning: String column type with a null value");} 
                                                                            
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
				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
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
		
	
	
	
	
/**
 *   Logs audit trail : for use within bean
 * @param     AuditInfo    
 * @author  Bolaji L. Ogeyingbo.
  * @version 1.0
 */
	 public  boolean logAuditTrail(AuditInfo ai) throws Exception 
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
					
					System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
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
	
	
	
	public String selectx(String selstmnt) throws Exception
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

  
  
  
  public final void updateLogin(int loginId, boolean loginlogout)
  {
     try
	   {
	     con = connect();
		 String updatequery = "";
		 if(loginlogout == false)
			{   updatequery = "UPDATE   AM_GB_USER SET   login_status = 0 WHERE user_Id = "+loginId;	}
		else
		    {   updatequery = "UPDATE   AM_GB_USER SET   login_status = 1 WHERE user_Id = "+loginId;	}
		prepstmnt = con.prepareStatement(updatequery);
		rowsupdated = prepstmnt.executeUpdate();
		}
	 catch(Exception e) { e.printStackTrace(); 	}
	}
	     
	
	
	
	  public final void updateLogin(int loginId, boolean loginlogout,String querypart1,String querypart2)
  {
     try
	   {
	     con = connect();
		 StringBuffer updatestring = new StringBuffer(1000);
		 updatestring.append(querypart1);
		 if(loginlogout == logout)
			{  updatestring.append(" = 0 "); 	}
		else
		    {   updatestring.append(" = 1 ");	}
		  updatestring.append(querypart2);  
		  updatestring.append(" =  ");
		  updatestring.append(loginId);
		  String updatequery = updatestring.toString();
		prepstmnt = con.prepareStatement(updatequery);
		rowsupdated = prepstmnt.executeUpdate();
		}
	 catch(Exception e) { e.printStackTrace(); 	}
	}
	
	
	
	
  public final void updateLoginAudit(String SessionId, int LoginID)
  {
     try
	   {
	    if(sessionIdExist(SessionId))
			{
	        con = connect();
		    String updatequery = "";
		   updatequery = "UPDATE   GB_USER_LOGIN   SET time_out = getDate() WHERE session_Id = '"+SessionId+"' and   	user_Id = "+LoginID;	
			prepstmnt = con.prepareStatement(updatequery);
			// prepstmnt.setDate(1, new java.util.Date());
			rowsupdated = prepstmnt.executeUpdate();
			}
		}
	 catch(Exception e) { e.printStackTrace(); 	}
}


 public final boolean sessionIdExist(String SessionId)
 {
	String sessionId = "";
      try
	   {
	     con = connect();
		 String selstmnt = "";
		 selstmnt = "SELECT Session_id FROM  GB_USER_LOGIN   WHERE Session_Id = '"+SessionId+"'";
	     stmnt = con.createStatement();
		 rs = stmnt.executeQuery(selstmnt);
		sessionId = rs.getString(1);
		 
		}
	 catch(Exception e) { e.printStackTrace(); 	}
	   if(sessionId == null)
	      { return false ;}
		 else
		    { return true;	}
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
			  System.out.println("NEW VALUE is "+AuditFields[x]);
			  x = x + 1;
			  } //method 2 ends
	}
	     

		 
		 
		public void auditLogin(int loginID, String branchcode, String workstationName, String workstationIp, String sessionId)
		{	 
			 try {
			      
					con = connect();
					System.out.println("Connection >> "+con);
					System.out.println("AuditTrailGen.loginID >> "+loginID);
					System.out.println("AuditTrailGen.branchcode >> "+branchcode);
					System.out.println("AuditTrailGen.workstationName >> "+workstationName);
					System.out.println("AuditTrailGen.workstationIp >> "+workstationIp);
					System.out.println("AuditTrailGen.sessionId >> "+sessionId);

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
		 
		 
		 
		 
		 public String getUserName(int LoginId, String field, String tableName)
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
	
	
public	final void update(String updtstmnt)
    {
	  try{
	     con = connect();
		 prepstmnt = con.prepareStatement(updtstmnt);
		 rowsupdated = prepstmnt.executeUpdate();
		 if ( rowsupdated > 0)
		   { setCheckedChanges(true);  }
		 else {  setCheckedChanges(false); }
		 }
		 catch (Exception e){ e.printStackTrace(); }
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
    public final void updateLoginAudit(String SessionId, int LoginID, boolean loginlogout)
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



public boolean logAuditTrail(String TableName,  String BranchCode,  int LoginId, String RowId, String ActionPerformed) throws Exception
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
			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
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




//Ganiyu's code
public boolean logAuditTrailActionPerformed(String TableName,  String BranchCode,  int LoginId, String RowId,String action_performed) throws Exception
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    {
	   DatetimeFormat df = new DatetimeFormat();

	   try {
			con = connect();  	int loginId = 0;
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
			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
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
				System.out.println("Number of rows updated in the database is:  "+ rowsupdated);
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





}









