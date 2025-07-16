package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Salesman {
	public String getSales() {
		String sales="";
	try {
		
		
		String sql = "select count(*) from Registration where UserId='salesman'";
		 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FirstWork", "sa", "Fabregas4");
         PreparedStatement ps = conn.prepareStatement(sql);
             	
         ResultSet rs = ps.executeQuery();
         while(rs.next()) {
        	 sales = rs.getString(1);
        	//System.out.println("Total Count:"+nUsers);

        		
        		 
        	
        	
         }
        
	}
	catch(Exception e) {
		
	}
	return sales;
}
}
