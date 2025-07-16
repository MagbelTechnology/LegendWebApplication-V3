package com.magbel.ia.vao;

/**
 * <p>Title: GLStructure.java</p>
 *
 * <p>Description: GLStructure Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */

public class GLStructure {

	private String id;
	private String position;
	private String name;
	private String mask;
    private String structureType;
	private String delimiter;
	private String effectiveDate;
    private String status;
    
    
	public GLStructure(String id, String position, String name, String mask, String structureType,
                       String delimiter, String effectiveDate, String status) {
		super();
		this.id = id;
		this.position = position;
		this.name = name;
		this.mask = mask;
		this.structureType = structureType;
		this.delimiter = delimiter;
		this.effectiveDate = effectiveDate;
		this.status = status;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStructureType() {
		return structureType;
	}
	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

}