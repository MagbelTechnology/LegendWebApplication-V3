package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class ScriptExecutionBeans {
   
	public String script;
	public String reason;
	public String confirm;
	
	public String getScript() {  
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	public static ArrayList<ScriptExecutionBeans> getScriptExecDetails(String userId) throws SQLException {
		ArrayList<ScriptExecutionBeans> list = new ArrayList<ScriptExecutionBeans>();
		Connection con = null ;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select script, reason, confirm from am_gb_script where id=?";
		con = getConnection();
	    ps = con.prepareStatement(query);
	    ps.setString(1, userId);
	    rs = ps.executeQuery();
	    if(rs.next()) {
	    	ScriptExecutionBeans scriptExecutionBean = new ScriptExecutionBeans();
	    	String script = rs.getString(1);
	    	String reason = rs.getString(2);
	    	String confirm = rs.getString(3);
	    	
	    	scriptExecutionBean.setScript(script);
	    	scriptExecutionBean.setReason(reason);
	    	scriptExecutionBean.setConfirm(confirm);
	    	
	    	list.add(scriptExecutionBean);
	    	
	    
	    }
	    }catch(Exception e){
	    	e.getMessage();
	    }finally {
	    	closeConnection(con, ps, rs);
		}

		return list;
	}
	
	
	private static Connection getConnection() {
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
		
		return con;
    }
	
	  private static void closeConnection(Connection con, PreparedStatement ps, ResultSet rs)
	    {
	        try
	        {
	            if(rs != null)
	            {
	                rs.close();
	            }
	            if(ps != null)
	            {
	                ps.close();
	            }
	            if(con != null)
	            {
	                con.close();
	            }
	        }
	        catch(Exception e)
	        {
	            System.out.println((new StringBuilder()).append("WARNING: Error closing connection ->").append(e.getMessage()).toString());
	        }
	    }

}
