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


public class SecurityRerouteClass {

	public int transaction_Id;
	public String asset_Id;
	public String tran_type;
	public String description;

	
	public SecurityRerouteClass() {
		
	}


	public int getTransaction_Id() {
		return transaction_Id;
	}


	public void setTransaction_Id(int transaction_Id) {
		this.transaction_Id = transaction_Id;
	}


	public String getAsset_Id() {
		return asset_Id;
	}


	public void setAsset_Id(String asset_Id) {
		this.asset_Id = asset_Id;
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


	public void setDescription(String description) {
		this.description = description;
	}


	

	

	
}
