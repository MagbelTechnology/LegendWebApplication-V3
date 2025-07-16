package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class SupervisorRerouteBean {
   
	public String superId;
	public String new_supervisorId;
	public String reroute_reason;
	public String approved_by;
	public String requested_by;
	public String asset_id;
	public String tran_type;
	public String description;
	public String transactionId;
	

	public String getSuperId() {
		return superId;
	}


	public void setSuperId(String superId) {
		this.superId = superId;
	}


	public String getNew_supervisorId() {
		return new_supervisorId;
	}


	public void setNew_supervisorId(String new_supervisorId) {
		this.new_supervisorId = new_supervisorId;
	}


	public String getReroute_reason() {
		return reroute_reason;
	}


	public void setReroute_reason(String reroute_reason) {
		this.reroute_reason = reroute_reason;
	}


	public String getApproved_by() {
		return approved_by;
	}


	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}


	public String getRequested_by() {
		return requested_by;
	}


	public void setRequested_by(String requested_by) {
		this.requested_by = requested_by;
	}


	public String getAsset_id() {
		return asset_id;
	}


	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}


	public String getTran_type() {
		return tran_type;
	}


	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}


	public String getDescription() {
		return description;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public static ArrayList<SupervisorRerouteBean> getRerouteDetails(String batchId) throws SQLException {
		ArrayList<SupervisorRerouteBean> list = new ArrayList<SupervisorRerouteBean>();
		Connection con = null ;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select tran_type,asset_id,description,transaction_id from am_supervisor_reroute where batch_id=?";
		con = getConnection();
	    ps = con.prepareStatement(query);
	    ps.setString(1, batchId);
	    rs = ps.executeQuery();
	   while(rs.next()) {
	    	SupervisorRerouteBean supervisorReroute = new SupervisorRerouteBean();
	    	String tranType = rs.getString(1);
	    	String assetId = rs.getString(2);
	    	String description = rs.getString(3);
	    	String transactionId = rs.getString(4);
	    	
	    	supervisorReroute.setTran_type(tranType);
	    	supervisorReroute.setAsset_id(assetId);
	    	supervisorReroute.setDescription(description);
	    	supervisorReroute.setTransactionId(transactionId);
	    	
	    	list.add(supervisorReroute);
	    	
	    
	    }
	    }catch(Exception e){
	    	e.getMessage();
	    }finally {
	    	closeConnection(con, ps, rs);
		}

		return list;
	}
	
	
	public static ArrayList<SupervisorRerouteBean> getRerouteExtraDetails(String batchId) throws SQLException {
		ArrayList<SupervisorRerouteBean> list = new ArrayList<SupervisorRerouteBean>();
		Connection con = null ;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    String query = "select distinct super_id, new_super_id, reroute_reason, approved_by, requested_by from am_supervisor_reroute where batch_id=?";
		con = getConnection();
	    ps = con.prepareStatement(query);
	    ps.setString(1, batchId);
	    rs = ps.executeQuery();
	   while(rs.next()) {
	    	SupervisorRerouteBean supervisorReroute = new SupervisorRerouteBean();
	    	String superId = rs.getString(1);
	    	String newSuperId = rs.getString(2);
	    	String rerouteReason = rs.getString(3);
	    	String approvedBy = rs.getString(4);
	    	String requestBy = rs.getString(5);
	    	
	    	supervisorReroute.setSuperId(superId);
	    	supervisorReroute.setNew_supervisorId(newSuperId);
	    	supervisorReroute.setReroute_reason(rerouteReason);
	    	supervisorReroute.setApproved_by(approvedBy);
	    	supervisorReroute.setRequested_by(requestBy);
	    	
	    	list.add(supervisorReroute);
	    	
	    
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
