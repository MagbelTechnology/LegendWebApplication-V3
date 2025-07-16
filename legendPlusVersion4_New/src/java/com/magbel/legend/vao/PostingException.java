package com.magbel.legend.vao;

public class PostingException {

	public int id;
	public String batch_no;
	public String current_no;
	public String sequence_no;
	public String error_code;
	public String account_no;
	public String error_message;
	public String user_id;
	public String create_date;
	
	public PostingException() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getCurrent_no() {
		return current_no;
	}

	public void setCurrent_no(String current_no) {
		this.current_no = current_no;
	}

	public String getSequence_no() {
		return sequence_no;
	}

	public void setSequence_no(String sequence_no) {
		this.sequence_no = sequence_no;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	
}
