// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HtmlUtilily.java

package com.magbel.ia.util;
import com.magbel.ia.dao.PersistenceServiceDAO;


public class ErrorMessage{

	public final short ALERT = 1;
	public final short ERROR = 2;
	public final short SYSTEM_ERROR = 4;
	public final short DB_CONNECT_ERROR = 8;
	public final short RES_SHARE_ERROR = 16;
    public final short RES_AVAIL_ERROR = 32;

	public final static String INVALID_UPLOAD_RIGHT = "Please you don't have cheque confirmation "+
													  "details upload rights.";
	public final static String INVALID_MODIFICATION_RIGHT = "Please you don't have cheque "+
														"confirmation details modification rights.";
	public final static String INVALID_AUTHORIZATION_RIGHT = "Please you don't have cheque "+
														"confirmation details authorization rights.";
	public final static String INVALID_VIEWING_RIGHT = "Please you don't have cheque confirmation "+
														"status viewing rights.";
	public final static String LOGIN_FAILURE = "Invalid username or password.";
	public final static String DUPLICATION_CONFIRMATION = "Confirmation INFO already exists on the "+
											              "system for Cheque Number: xxxx to be drawn"+
											              "the Account Number:";
	public final static String INVALID_CONFIRMATION = "Confirmation for the supplied Cheque Number "+
													 "to be drawn on this does not exist on the system.";
	public final static String SUCESSFUL_CAPTURE = "Confirmation Details Were Sent Successfully";
	public final static String SUCESSFUL_FAILURE = "Failed posting confirmation details!";

	private short intsysmessgflags = 0;
	private static StringBuffer alertMessage = new StringBuffer();
	private static StringBuffer errorMessage = new StringBuffer();
	private static StringBuffer systemMessage = new StringBuffer();
	private static StringBuffer connectionMessage = new StringBuffer();
	private static StringBuffer resourceMessage = new StringBuffer();
	private static StringBuffer resourceShareMessage = new StringBuffer();

	private static StringBuffer fieldname = new StringBuffer();
    private static StringBuffer fieldvalue = new StringBuffer();

	public static String getAlertMessage(){
	     return null;//this .alertMessage.toString();
	}

    public static void addErrorMessage(String error) {
	  errorMessage.append(error + " \n");
	}

    public static void addSystemErrorMessage(String error) {
	  systemMessage.append(error + " \n");
	}

	public static void addDataConnectErrMessage(String error){
	  connectionMessage.append(error + " \n");
	}

	public static void resetAllMessages(){

	 alertMessage = null;
	 errorMessage =  null;
	 systemMessage = null;
	 connectionMessage =  null;
	 resourceMessage =  null;
	 resourceShareMessage =  null;

	}

}
