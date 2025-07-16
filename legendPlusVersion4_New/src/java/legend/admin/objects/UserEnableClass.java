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

/**
 * @author Rahman Oloritun
 * @version 1.00
 */
public class UserEnableClass {

	public String classId;
	public String classDesc;
	public String className;
	public String classStatus;
	public String defaultClassId;
	public int userId;
	public String createDate;
	
	public UserEnableClass() {
		
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	public String getDefaultClassId() {
		return defaultClassId;
	}

	public void setDefaultClassId(String defaultClassId) {
		this.defaultClassId = defaultClassId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	

	
}
