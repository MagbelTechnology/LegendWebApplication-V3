package legend.admin.objects;

import java.sql.Date;

public class Memo {
	private String id; 
	private String branch ;
	private double cost   ;
	private Date date  ;
	public Memo() {
		super();
	}
	public Memo(String id, String branch, double cost, Date date) {
		super();
		this.id = id;
		this.branch = branch;
		this.cost = cost;
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
