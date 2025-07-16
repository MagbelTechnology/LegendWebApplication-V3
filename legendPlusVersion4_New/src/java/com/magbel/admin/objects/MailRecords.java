package com.magbel.admin.objects;

public class MailRecords { 
	private String id;

	private String sender;
	
	private String title;
	 
	private String recv_dt;
	
	private String msg;
	
	private String Status;
	  
	public MailRecords() {

	}     
  
	public MailRecords(String id, String sender,String title,String recv_dt, String msg, String Status) {
		super();
		this.id = id;
		this.sender = sender;
		this.title = title;
		this.recv_dt = recv_dt;
		this.msg = msg;
		this.Status = Status;
	} 

	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	public String getsender() {
		return sender;
	}

	public void setsender(String sender) {
		this.sender = sender;
	}

	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}


	public String getmsg() {
		return msg;
	}

	public void setmsg(String msg) {
		this.msg = msg;
	}
    public String getrecv_dt() {
		return recv_dt;
	}

	public void setrecv_dt(String recv_dt) {
		this.recv_dt = recv_dt;
	}
	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

}
