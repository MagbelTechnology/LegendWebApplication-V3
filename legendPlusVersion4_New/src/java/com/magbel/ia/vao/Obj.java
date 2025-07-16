package  com.magbel.ia.vao;




public  class  Obj
 {
    String  mtId  =  null;
	String  code  =  null;
	String name  =  null;
	String criteria  =  null;
	String  description  =  null;
	String  tableName  =  null;
	String  status  =  null;
	public  boolean  extension  =  false;
	
	
	public  final boolean getExtension(){
	if((getDescription() != null)  || (getCriteria() != null))
	   return  true;
	 else  return false;
	}
	
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String Status) {
		this.status = Status;
	}
	
	
	
	public final String getTableName() {
		return tableName;
	}
	public final void setTableName(String TableName) {
		this.tableName = TableName;
	}
	
	
	public final String getCode() {
		return code;
	}
	public final void setCode(String code) {
		this.code = code;
	}
	public final String getCriteria() {
		return criteria;
	}
	public final void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
	public final String getMtId() {
		return mtId;
	}
	public final void setMtId(String mtId) {
		this.mtId = mtId;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	
 }
	