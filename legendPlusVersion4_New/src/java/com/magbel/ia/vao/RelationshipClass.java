package  com.magbel.ia.vao;
 import  java.io.Serializable;

public class RelationshipClass implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;
	private String mtId;
	private String relationshipClassCode;
	private String relationship;
	private int userId;
	
	
	public  RelationshipClass(){;}
	
	public  RelationshipClass(String MtId,  String RelationshipClassCode,  
										String Relationship, int UserId)
	{
		this.mtId  =  MtId.trim();
		this.relationshipClassCode  =  RelationshipClassCode.trim();
		this.relationship =  Relationship.trim();
		this.userId =  UserId;
	}
	
	
	
   public  void setRelationshipClass(String MtId,  String RelationshipClassCode,  
										String Relationship, int UserId)
	{
		this.mtId  =  MtId.trim();
		this.relationshipClassCode  =  RelationshipClassCode.trim();
		this.relationship =  Relationship.trim();
		this.userId =  UserId;
	}
	
	
	
	
	public static final long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
	public final String getMtId() {
		return mtId;
	}
	
	
	public final void setMtId(String MtId) {
		this.mtId = MtId;
	}
	
	public final String getRelationship() {
		return relationship;
	}
	
	
	public final void setRelationship(String Relationship) {
		this.relationship = Relationship;
	}
	
	
	public final String getRelationshipClassCode() {
		return relationshipClassCode;
	}
	
	public final void setRelationshipClassCode(String RelationshipClassCode) {
		this.relationshipClassCode = RelationshipClassCode;
	}
	
	
	public final int getUserId() {
		return userId;
	}
	
	public final void setUserId(int UserId) {
		this.userId = UserId;
	}

}
