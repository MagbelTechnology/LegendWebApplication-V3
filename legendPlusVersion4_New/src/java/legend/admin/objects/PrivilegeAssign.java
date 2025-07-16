package legend.admin.objects;

public class PrivilegeAssign {
	private String roleId;
	private String roleName;		
	private String roleUrl;
	private String menuType;
	private String Priority;
	private String parentId;
	private String Level;
	private String createDate;

	
	public PrivilegeAssign(){
		
	}	


	public PrivilegeAssign(String roleId, String roleName, String roleUrl, String menuType, String Priority, String parentId, String Level)
	{
			this.roleId = roleId;
			this.roleName = roleName;
			this.roleUrl = roleUrl;
			this.menuType = menuType;
			this.Priority =Priority;
			this.parentId =parentId;
			this.Level =Level;

	}
	/**
	 * @return the reasonId
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param RoleUrl the RoleUrl to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the reasonId
	 */
	public String getRoleUrl() {
		return roleUrl;
	}

	/**
	 * @param RoleUrl the RoleUrl to set
	 */
	public void setRoleUrld(String roleUrl) {
		this.roleUrl = roleUrl;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}


	/**
	 * @param menuType the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}


	/**
	 * @return the description
	 */
	public String getPriority() {
		return Priority;
	}

	/**
	 * @param Priority the Priority to set
	 */
	public void setPriority(String Priority) {
		this.Priority = Priority;
	}


	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}


	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	/**
	 * @return the Level
	 */
	public String getLevel() {
		return Level;
	}


	/**
	 * @param Level the Level to set
	 */
	public void setLevel(String Level) {
		this.Level = Level;
	}


	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}


	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}	

}
