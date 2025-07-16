package com.magbel.admin.mail;


import javax.mail.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MailAuthenticator extends Authenticator 
{


  public MailAuthenticator(String username,String password) 
  {

    
  }



  

  public PasswordAuthentication getPasswordAuthentication(String username,String password) 
  {
    return new PasswordAuthentication(username, password);

  }
}

   