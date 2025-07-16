package com.magbel.ia.servlet;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.util.DataConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ayomide
 */
public class AyoMarkov6 {
               
    /**
     * @param args the command line arguments
     */
	
    public static void main(String[] args) {
    	
//             String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
//                String url="jdbc:sqlserver://MATANMI;databaseName=Connection";
//                String user="sa";
//                String pass="Fabregas4";
                Connection con;
                PreparedStatement ps;
                    try{
//                Connection con=DriverManager.getConnection(url, user, pass);
                con = null;
 //               Statement stmt = null;
                ResultSet rs = null;
                String SELECT_QUERY  = "Select * from Conn";
               
             int[] id = new int[14];
             int[] row = new int[14];
             int[] column = new int[14];
             double[] value = new double[14];
             double[][] transition = new double[14][14];
             con = getConnection();
             ps = con.prepareStatement(SELECT_QUERY);
    //            stmt = con.createStatement();
                rs = ps.executeQuery(SELECT_QUERY);
                
                   int coi = 0;

int N = 7;                        // number of states
int state = N - 1;                // current state
int steps = 0;                    // number of transitions                
                while (rs.next()) {
        
                    id[coi] =  rs.getInt("ID");
                    row[coi] = rs.getInt("ROW");
                    column[coi] =  rs.getInt("COLUMN");
                    value[coi] = rs.getDouble("VALUE");    
                    coi++;
                }
System.out.println("Column Count: "+coi);
          	     for(int j=0;j < coi+1;j++)
        	     {
		       	     for(int i=0;i < coi+1;i++)
		    	     {
		       	    	 transition[i][j] = value[i]; 
		    	     }
        	     }
// run Markov chain
                   
while (state > 0) {
   try{
boolean done = false;
insertResult(steps,state);
System.out.println(" STEPERS: "+steps+"      CURRENT STATE: "+state);
steps++;
double r = Math.random();
double sum = 0.0;

// determine next state
for (int j = 0; j < N; j++) {
sum += transition[state][j];
System.out.println("<<<<sum: "+sum);
if (r <= sum) {
state = j;
break;
}
}
   }catch(Exception e){
       System.out.println("Error! Check your Program" + e);
   }
}

   System.out.println("The number of steps =  " + steps);
}
                    
      catch (Exception ex) {
			System.out.println("WARN: Error Message  ->" + ex);
		} finally {
			//closeConnection(c, s, rs);
		}
		//return result;
	}

    


    public static boolean insertResult(int steps, int state)
      {
        Connection con;
        PreparedStatement ps;	
        
	    int done = 0;
 //        String Sql = "INSERT INTO TRANSITION  (Year, TRANSITION) values("+steps+","+state+")";
         String Sql = "INSERT INTO TRANSITION  (Year, TRANSITION) values(?,?)";
          try{  
         con = null; 
         con = getConnection();
         ps = con.prepareStatement(Sql);
          ps.setInt(1, state);
          ps.setInt(2, steps);
          ps = con.prepareStatement(Sql);
          done = ps.executeUpdate() ;  
          System.out.println("<<<<<done: "+done);
          con.commit();
      	}catch(Exception ex){
          System.out.println((new StringBuilder()).append("ERROR Creating Output Transition.. ").append(ex.getMessage()).toString());
          ex.printStackTrace();
      	} finally{
      	}
          return done>0;
      } 
    public static Connection getConnection()
    {
        Connection con = null; 
        try
        {
          String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
          String url="jdbc:sqlserver://MATANMI;databaseName=ias";
          String user="sa";
          String pass="magbel";
            con = (new DataConnect("ias")).getConnection();
        }
        catch(Exception conError)
        {
            System.out.println((new StringBuilder()).append("WARNING:Error getting connection - >").append(conError).toString());
        }
        return con;
    }    

    
}