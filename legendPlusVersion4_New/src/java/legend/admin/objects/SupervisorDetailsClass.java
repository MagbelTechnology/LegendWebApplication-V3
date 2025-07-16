/**
 * 
 */
package legend.admin.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import magma.ScriptExecutionBean;


public class SupervisorDetailsClass {

	public int user_Id;
	public String full_Detail;

	
	public SupervisorDetailsClass() {
		
	}


	public int getUser_Id() {
		return user_Id;
	}


	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}


	public String getFull_Detail() {
		return full_Detail;
	}


	public void setFull_Detail(String full_Detail) {
		this.full_Detail = full_Detail;
	}


	

	

	
}
