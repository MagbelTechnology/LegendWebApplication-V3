 package  com.magbel.ia.bus;


import java.sql.*;
import java.text.SimpleDateFormat;
import  java.util.ArrayList;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.Obj;



public class QueryUtil extends PersistenceServiceDAO //com.magbel.ia.dao.Config{
  {
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date dat;
   private  ApplicationHelper helper;
			
 
 
  public   QueryUtil(){}
		
 
 
 
 
 
 public  String    execQuery(String  Query, String var) {
     
		
		 Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	 String result = null;
		try {
            
			con = getConnection();
			 
        String SELECT_QUERY  = Query
		                        +var+"'";
		stmt = con.createStatement();
		System.out.println("======SELECT_QUERY====>>>>  "+SELECT_QUERY);
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
                result = rs.getString(1);
               
            }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  result;
}









public final ArrayList   getColumnNames(String TableName) throws Exception
   {
    ArrayList   columnNames   =   new java.util.ArrayList();
	Connection con  = null;
	Statement  stmnt =  null;
	ResultSet  rs = null;
	
	   try {
			
			String  SELECT_QUERY  = "SELECT  *  FROM  "+TableName+"  WHERE  MTID  =  (SELECT  MIN(CONVERT(MTID, int))  FROM  "+TableName+ " )";
			
			con = getConnection();  String val = ""; String nullstring = null;
			
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(SELECT_QUERY);
			
			if(rs == null)
			{
			  String  SELECT_QUERY2  = "SELECT  *  FROM  "+TableName;
			//  con = connect();  String val = ""; String nullstring = null;
			
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(SELECT_QUERY2);
			}
			  
			 ResultSetMetaData rsmd = rs.getMetaData();
	        int numColumns = rsmd.getColumnCount();
			//String tabnam = "General"; //rsmd.getTableName(1);
			//this.setTableName(tabnam); tabnm = tabnam;
            //System.out.println("Number of Columns is: "+numColumns+"\t");
			//System.out.println("Reference table is: "+this.getTableName());
			//String cname = null;
            int columnid = 0;    int columntype= 0;
        
		 while(rs.next())  
		 {
             for (int i=1; i < numColumns+1  ; i++) 
				{
					String cname = rsmd.getColumnName(i); // System.out.println("ColumnName: "+cname+"\n");
					columnid = i; // System.out.println("\tColumnId: "+columnid+"\n");
					columntype = rsmd.getColumnType(i);
					columnNames.add(cname);
				}
		}
		 } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmnt, rs);
        }
       return  columnNames;
    }	   
			   
			   



public final boolean createObjRow(Obj    obj)
	{
	   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		boolean  successfull  =  false;
		String INSERT_QUERY	;
		System.out.println("Creating  "+obj.getTableName()+"  record ... ");
	  try{
	      
        if(obj.extension ==  false)	
			{		
			  INSERT_QUERY	 = "INSERT INTO   "+obj.getTableName()+" ( MTID,  CODE, "
		                    +" NAME, STATUS    )  VALUES (?,?,?,?) ";
			}	
         else
            {
             	INSERT_QUERY	 = "INSERT INTO   "+obj.getTableName()+" ( MTID,  CODE, "
		                    +" NAME , STATUS,  CRITERIA, DESCRIPTION   )  VALUES (?,?,?,?,?,?) ";		
			}
		   String mtId = helper.getGeneratedId(obj.getTableName());
		String  code =  obj.getCode();
String  name =  obj.getName();
String  status =  obj.getStatus();
String  criteria =  obj.getCriteria();
String  description =  obj.getDescription();
                    
          	con = getConnection();
			
		ps = con.prepareStatement(INSERT_QUERY);
		
		 if(obj.extension ==  false)	
			{		
			  	ps.setString(1, mtId);
				ps.setString(2, code);
				ps.setString(3, name);
				ps.setString(4, status);
			}	
         else
            {
             	ps.setString(1, mtId);
				ps.setString(2, code);
					ps.setString(3, name);
					ps.setString(4, status);
					ps.setString(5, criteria);
					ps.setString(6,  description);	
			}
		
				
		successfull =  (ps.executeUpdate() != -1);
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }



	
			   
public final boolean  updateObjRow(Obj    obj)
	{
	  Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
		boolean  successfull = false;
		String UPDATE_QUERY;
		
	  try{
	          	
			if(obj.extension ==  false)
			  {
		         UPDATE_QUERY = " UPDATE     "+obj.getTableName()+"  SET  MTID = ?, CODE = ?, "
					         +" NAME = ? , STATUS = ? ";
		       }
			 else
			   {
			   UPDATE_QUERY = " UPDATE     "+obj.getTableName()+"  SET  MTID = ?, CODE = ?, "
					         +" NAME = ?, STATUS = ?,  CRITERIA = ?, DESCRIPTION = ?  ";
			   
			   }
		            String  mtId  =   obj.getMtId();
                  	String   code  =  obj.getCode();
					String  name  =   obj.getName();
					String  status  =   obj.getStatus();
					String   criteria =  obj.getCriteria();
                   String   description  =  obj.getDescription();
                  
		
		 con = getConnection();
		ps = con.prepareStatement(UPDATE_QUERY);
		
		
       if(obj.extension ==  false)
			  {		
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, mtId);
				ps.setString(4, status);
			  }
		else 
		     {
			    ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, status);
				ps.setString(4, criteria);
				ps.setString(5, description);
				ps.setString(6, mtId);
		     }
		
		
		successfull = (ps.executeUpdate()!=-1);
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
  }
  
  
  
public void closeConnection(Connection c, Statement st, ResultSet rs)
{
  	c = null;  st = null;  rs = null;
	
}
	

public void closeConnection(Connection c, PreparedStatement pst, ResultSet rs)
{
  	c = null;  pst = null;  rs = null;
	
}





  public   Obj    getObjRowById(Obj obj, String MtId) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        // com.magbel.ia.vao.Obj    obj = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	    String  mtId ;
		String  code ;
           String  name; 
		    String  status  = null; 
		   String  criteria  =  null;
         String  description   =null; 
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  "+obj.getTableName()+"  WHERE MTID = '"+MtId+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
             if(obj.extension ==  false)  
               {			 
                 mtId =  rs.getString("MTID");
             code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			   status   =  rs.getString("STATUS");
        	      }
			  else
			    {
				   mtId =  rs.getString("MTID");
              code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			   status   =  rs.getString("STATUS");
           criteria  =   rs.getString("CRITERIA");
           description  =  rs.getString("DESCRIPTION");	
				
				}

                // obj  = new  com.magbel.ia.vao.Obj();
			
if(obj.extension ==  false)  
               {			
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
}
else
{
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
obj.setCriteria(criteria);
obj.setDescription(description);
}
			
		}
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     obj;
}






public   Obj    getObjRowByCode(Obj obj, String Code) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        // com.magbel.ia.vao.Obj    obj = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	    String  mtId ;
		String  code ;
           String  name; 
		       String  status  = null; 
		   String  criteria  =  null;
         String  description   =null; 
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  "+obj.getTableName()+"  WHERE CODE = '"+Code+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
             if(obj.extension ==  false)  
               {			 
                 mtId =  rs.getString("MTID");
             code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			 status   =  rs.getString("STATUS");
        	      }
			  else
			    {
				   mtId =  rs.getString("MTID");
              code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			   status   =  rs.getString("STATUS");
           criteria  =   rs.getString("CRITERIA");
           description  =  rs.getString("DESCRIPTION");	
				
				}

                // obj  = new  com.magbel.ia.vao.Obj();
			
if(obj.extension ==  false)  
               {			
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
}
else
{
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
obj.setCriteria(criteria);
obj.setDescription(description);
}
			
		}
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     obj;
}

  
  
  
  
  
  
  
  public   java.util.ArrayList    getAllObjRow(Obj obj) {
       java.util.ArrayList  records = new java.util.ArrayList();
        // com.magbel.ia.vao.Obj    obj = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	    String  mtId ;
		String  code ;
           String  name; 
		       String  status  = null; 
		   String  criteria  =  null;
         String  description   =null; 
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  "+obj.getTableName();
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
             if(obj.extension ==  false)  
               {			 
                 mtId =  rs.getString("MTID");
             code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			  status   =  rs.getString("STATUS");
        	      }
			  else
			    {
				   mtId =  rs.getString("MTID");
              code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			 	  status   =  rs.getString("STATUS");
           criteria  =   rs.getString("CRITERIA");
           description  =  rs.getString("DESCRIPTION");	
				
				}

                // obj  = new  com.magbel.ia.vao.Obj();
			
if(obj.extension ==  false)  
               {			
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
}
else
{
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
obj.setCriteria(criteria);
obj.setDescription(description);
}
		
records.add(obj);
		}
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     records;
}





public   java.util.ArrayList    getObjRowByStatus(Obj obj) {
       java.util.ArrayList  records = new java.util.ArrayList();
        // com.magbel.ia.vao.Obj    obj = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	    String  mtId ;
		String  code ;
           String  name; 
		       String  status  = null; 
		   String  criteria  =  null;
         String  description   =null; 
	
		try {
           
			con = getConnection();
		 
        String SELECT_QUERY  = " SELECT * FROM  "+obj.getTableName()+"  WHERE  STATUS = '"+obj.getStatus()+"' ";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
             if(obj.extension ==  false)  
               {			 
                 mtId =  rs.getString("MTID");
             code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			  status   =  rs.getString("STATUS");
        	      }
			  else
			    {
				   mtId =  rs.getString("MTID");
              code  =  rs.getString("CODE");
             name   =  rs.getString("NAME");
			 	  status   =  rs.getString("STATUS");
           criteria  =   rs.getString("CRITERIA");
           description  =  rs.getString("DESCRIPTION");	
				
				}

                // obj  = new  com.magbel.ia.vao.Obj();
			
if(obj.extension ==  false)  
               {			
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
}
else
{
obj.setMtId(mtId);
obj.setCode(code);
obj.setName(name);
obj.setStatus(status);
obj.setCriteria(criteria);
obj.setDescription(description);
}
		
records.add(obj);
		}
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     records;
}


}