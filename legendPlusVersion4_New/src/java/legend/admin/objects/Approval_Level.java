package legend.admin.objects;

public class Approval_Level {
	
	 	private String code;
	 	
	 	private String trans_type;
	 	
	 	private String level;
	 	
	    private String date;
	    
	    private String userid;
	   
	    
	    

public Approval_Level()
{


}
public Approval_Level(String code, String trans_type, String level, String date, String userid)
{
		this.code = code;
		this.trans_type = trans_type;
		this.level =level;
		this.date = date;
		this.userid = userid;


}
			public String getCode() {
				return code;
			}
			public void setCode(String code) {
				this.code = code;
			}
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			public String getLevel() {
				return level;
			}
			public void setLevel(String level) {
				this.level = level;
			}
			public String getTrans_type() {
				return trans_type;
			}
			public void setTrans_type(String trans_type) {
				this.trans_type = trans_type;
			}
			public String getUserid() {
				return userid;
			}
			public void setUserid(String userid) {
				this.userid = userid;
			}
					
		

}
