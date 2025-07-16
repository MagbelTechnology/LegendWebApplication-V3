package audit;
import java.sql.*;
import java.util.*;
import audit.*;
import magma.net.dao.MagmaDBConnection;


//import com.microsoft.sqlserver.jdbc.*;

public class Collate extends Bag{
  
  String dbname = "accounts";
  String dsn = "accounts";
  Connection con = null;
  ResultSet rs = null;
  PreparedStatement prepstmnt = null;
  Statement stmnt = null;
  int count = 0;
  
  int rowsupdated = 0;
  boolean checkupdate = false;

 private MagmaDBConnection dbConnection = new MagmaDBConnection();
  
 private   int cType = 0;  private int colIndex = 0;
 private Field field ; String tabnm = "";





  public final Connection connect()  throws Exception
   {
   try {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      con = DriverManager.getConnection("jdbc:odbc:"+dsn);
      } catch (Exception e) {
				System.out.println("StandardQuery.connect() -> Exception " + e + " occured");
			}
     return con;
  }



  //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
   //   con = DriverManager.getConnection("jdbc:sqlserver:"+dsn)


  public final void select(int updatestatus, String selstmnt) throws Exception
   {
	   try {
			con = connect();  String val;
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
                  cname = rsmd.getColumnName(i);  System.out.println("ColumnName: "+cname+"\n");
		       columnid = i;  System.out.println("\tColumnId: "+columnid+"\n");
			   columntype = rsmd.getColumnType(i);  System.out.println("\tColumnType: "+columntype+"\n\n");
			   
			   switch(columntype)
			    {
			      case 4  :
				  case -6 :     int intval = rs.getInt(i); //sqltype for tinyint =  -6,  int = 4
						        val = String.valueOf(intval);
						        this.setField(cname, val ,columntype, columnid);
						        break;
			   
                  case 2 :
				  case 3 :  	double dblval = rs.getDouble(i); //sqltype for numeric = 2 , sqltype for decimal = 3	
								val = String.valueOf(dblval);
								this.setField(cname, val ,columntype, columnid);
								break;
								
			     //  else if(columntype ==  16 )//{}//sqltype for boolean = 16
				 
				  case 1  :
				  case 12 :     String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
								this.setField(cname, strval ,columntype ,  columnid  );
								System.out.println("\tColumnvalue: "+strval+"\n");
								break;
								
				  default  :    String vals = rs.getString(i); //sqltype for varchar =  12,  char = 1
								this.setField(cname, vals ,columntype ,  columnid  );
								System.out.println("\tColumnvalue: "+vals+"\n");			
				}
			   System.out.println("\t\t\t\t"+this.getOldValue(cname)+"  "+String.valueOf(this.getColumnType(cname)));  
		    }
		}
		
		else if( updatestatus == this.AFTER_UPDATE )
		     {
			  while(rs.next())  
               for (int i=1; i < numColumns+1  ; i++) 
		           {
                     cname = rsmd.getColumnName(i);  System.out.println("ColumnName: "+cname+"\n");
					columnid = i;  System.out.println("\tColumnId: "+columnid+"\n");
					columntype = rsmd.getColumnType(i);  System.out.println("\tColumnType: "+columntype+"\n\n");
			   
			        switch(columntype)
					      {
						    case 4  :
							case -6 :  int intval = rs.getInt(i); //sqltype for tinyint =  -6,  int = 4
										  val = String.valueOf(intval);
										  this.setValue(cname, val );
										  break;
										  
					        case 2 :
							case 3 :    double dblval = rs.getDouble(i); //sqltype for numeric = 2 , sqltype for decimal = 3
										   val = String.valueOf(dblval);
										   this.setValue(cname, val );
										   break;
				     							
					     	//else if(columntype ==  16 ){}//sqltype for boolean = 16
			   
			                case 1 :
							case 12 :  String strval = rs.getString(i); //sqltype for varchar =  12,  char = 1
										  this.setValue(cname, strval );
										  System.out.println("\tColumnvalue: "+strval+"\n");
										  break;
										  
							default :     String vals = rs.getString(i); //sqltype for varchar =  12,  char = 1
										  this.setValue(cname, vals );
										  System.out.println("\tColumnvalue: "+vals+"\n");
										  
						   }			
					}
			 System.out.println("\t\t\t\t"+this.getValue(cname)+"  "+String.valueOf(this.getColumnType(cname)));  
			
		     }

	     } 
		catch (Exception e) 
			{
            System.out.println("StandardQuery.select() -> Exception " + e + " occured");
            }
     
  }


	
  
  
  
  
  
  public final boolean checkUpdate()
     {
	     if(checkupdate == true) // checks for updates in the relevant database table
		   { setCheckedChanges(true); return true;  }
		 else return false;
	 }
  
  
  
 
   public final void logAuditTrail(String TableName, String ActPerf, String BranchCode, String Description, int LoginId, int RowId) throws Exception 
	//updateAuditLog  IN  parameters  are  actPerf,  branchcode,  description
    {
	   try {
			con = connect();  	int loginId = 0;
			String branchcode, description, actPerf, auditindex;
            actPerf = ActPerf; branchcode = BranchCode; description = Description; loginId = LoginId;			
			Iterator it = this.getContainer().values().iterator();                                     // setCheckedChanges(true);
			//if (checkedChanges() == true) 
			while(it.hasNext())
			 {
			  Field fld = (Field)it.next(); 
              if(fld.isChanged() == true)
                {	
                String tablename, columnName,  preval, newval;	int rowid = RowId;			
			    columnName = fld.getName(); preval = fld.getOldValue(); newval = fld.getNewValue(); tablename = TableName;
			    System.out.print(preval+"\t\t");     System.out.println(newval);
			   // if(preval.equals(curval)){}
				//else
				//   {
					System.out.println(columnName +"  "+preval+"   "+newval+"\n");
					//auditindex = Integer.toString((int)(Math.random()*8000) + 1000);
					//  String auditstr = "insert into (AUDIT_INDEX,  TABLE_NAME, OBJ_NAME, ACT_PERFMD, PRE_VALUE, CUR_VALUE, EFF_DATE, LOGIN_ID, 		BRANCH_CODE, 					ASSET_DESCRIPTION) 		AM_AD_AUDIT_TRAIL values (? , tableName, objName, actPerf, preval, curval, getDate(),loginId,   branchcode, getDate(), description)";
					String auditstr = "insert into AM_AD_AUDIT_TRAIL values ( ?, ?,  ?, ?,  ?, ?, getDate(), ?,   ?, ?, ?)";
					prepstmnt = con.prepareStatement(auditstr);
			 
					//prepstmnt.setString(1, auditindex);
					prepstmnt.setString(1, tablename);
					prepstmnt.setString(2, columnName);
					prepstmnt.setInt(3, rowid);
					prepstmnt.setString(4, actPerf);
					prepstmnt.setString(5, preval);
					prepstmnt.setString(6, newval);
					prepstmnt.setInt(7, loginId);
					prepstmnt.setString(8, branchcode);
				     prepstmnt.setDate(9,dbConnection.dateConvert(new java.util.Date()));
					prepstmnt.setString(10, description);
			   	   
			   	 
					       
					rowsupdated = prepstmnt.executeUpdate();
				//	}
			/**
				} else { System.out.println("\t\t-> no changes made....");   }    
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
				**/
				}
			 }	
		}catch(Exception e){ e.printStackTrace(); }
	}//end method block
	
	
	
	
	
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
		 
		 
		 
		 
		 
	
	
public static void main(String[] args)
  {
   try{
    Collate ct = new Collate();
     ct.connect();
    ct.select(1,"select * from general where Id = 3");//where Id = 3 
	ct.update("update general set category = 'Excel T' where id = 3");
	ct.select(2,"select * from general  where Id = 3");
    ct.logAuditTrail( "AM_AD_AUDIT_TRAIL", "Test", "318", "testing if Audit trail log is working", 8, 3);

    }catch(Exception e){ e.printStackTrace();}

  }
  
  
  
}









