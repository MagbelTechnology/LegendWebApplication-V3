package com.magbel.admin.mail;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;

public class EmailStamp{

private String from;
private String title;
private String content;
private Date date;

public EmailStamp(String from,String title,String content,Date date){

	this.from = from;
	this.title = title;
	this.content = content;
	this.date = date;
}

public String getFrom(){
	return this.from;
} 

public String getTitle(){
	return this.title;
}

public String getContent(){
	return this.content;
}

public Date getDate(){
	return this.date;
}

}
