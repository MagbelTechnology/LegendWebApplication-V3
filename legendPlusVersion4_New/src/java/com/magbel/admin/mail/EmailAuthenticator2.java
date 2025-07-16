package com.magbel.admin.mail;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailAuthenticator2 extends Authenticator {

private String username;
private String password;

public EmailAuthenticator2(String username,String password){
	this.username = username;
	this.password = password;
}

  public PasswordAuthentication getPasswordAuthentication(){
    return new PasswordAuthentication(this.username, this.password);
  }

}
