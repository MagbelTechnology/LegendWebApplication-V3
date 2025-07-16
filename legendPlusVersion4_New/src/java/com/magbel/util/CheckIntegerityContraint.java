package com.magbel.util;
import java.sql.*;

public class CheckIntegerityContraint 
{
  public CheckIntegerityContraint()
  {
  }
  
  public boolean checkReferenceConstraint(String tableName,String columnName,String checkValue,String status)
  {
     Connection con = null;
   	    		PreparedStatement ps = null;
   	    		ResultSet rs = null;
   	    		boolean confirm = false;

   	    		DataConnect dbConn = new DataConnect("legendPlus");
            
            String query = "SELECT "+columnName+" FROM "+tableName +" WHERE "+columnName+" = '"+checkValue+"'";
       if(status.equalsIgnoreCase("C"))
       {
   	    	try {
   	    		con = dbConn.getConnection();
   	    		ps = con.prepareStatement(query);
 	    			rs = ps.executeQuery();
   	    			while (rs.next()){
   	    				confirm = true;
   	    			}

   	    		}
   	    		catch (Exception e) 
            {
   	          System.out.println("INFO:Error Checking Integrity constraint->" +e.getMessage());
   	        }
   	    		finally {
            dbConn.closeOpenConnection(rs,ps,con);
           }

   	    		 
   		}
       return confirm;
     }
  }
  
