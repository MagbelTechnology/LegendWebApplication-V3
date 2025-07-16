/**
 * 
 */
package com.magbel.menu.objects;

import java.util.List;

/**
 * @author Rahman o. Oloritun
 * Copyright May 25, 2007
 * 10:07:48 AM
 */
public class Menu {   
	
	private String menuId;
	private String menuName;
	private String menuUrl;
	private String parentId;
	private int level;
	private List<Menu> children;
	
	
	
	
	
	/**
	 * 
	 */
	public Menu() {
		
	}
	/**
	 * @param menuId
	 * @param menuName
	 * @param menuUrl
	 * @param parentId
	 * @param children
	 */
	public Menu(String menuId, String menuName, String menuUrl, String parentId,int level) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.parentId = parentId;
		this.level = level;
	}
	/**
	 * @return the children
	 */
	public List getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	/**
	 * @return the menuId
	 */
	public String getMenuId() {
		return menuId;
	}
	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * @return the menuUrl
	 */
	public String getMenuUrl() {
		return menuUrl;
	}
	/**
	 * @param menuUrl the menuUrl to set
	 */
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
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
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
	
	
	

}
