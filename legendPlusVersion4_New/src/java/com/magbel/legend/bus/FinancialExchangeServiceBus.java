package com.magbel.legend.bus;


 
import java.io.*;
import java.util.Date;
import java.util.Properties;

import javax.mail.Session;

import com.mindcom.webbfront.WebbManager;
import com.mindcom.webbfront.util.HostParameters;
import com.mindcom.webbfront.util.Sequencer;


public class FinancialExchangeServiceBus
{
	public FinancialExchangeServiceBus()
	{}
public String transferFund(String AccName,String Acct2,String Acct1,double Amt) throws Exception
{
	String responseCode="";
    HostParameters hp = new HostParameters();
    hp.setHostIpAddress("172.27.12.26");
//  hp.setPort(27601);
    hp.setPort(4441);//FIX port
 // hp.setPort(14441);
    hp.setTimeout(60);
    hp.setAquirerId("000000");   
    hp.setForwardId("000000");
    hp.setCardAccId("00000000");
    hp.setCardAccName(AccName);
    hp.setTerminalId("12345678");
    hp.setCurrency("566");
//  hp.setControllerId("STT");
//  hp.setControllerId("ETZ");
    hp.setControllerId("FIX");
    WebbManager wm = new WebbManager(hp);
    wm.setDebug(true);
    String custAccountNumber = Acct1;
    String accountNumber2 = Acct2;
//  String defaultPan = "6280090000000000000";
    String defaultPan = "6272140000000000000";//FIX defaultpan
    double amount = Amt;
    try
    {
        java.util.Date mydate  = new java.util.Date();
        responseCode = wm.doFundsTranfer(Sequencer.next(), defaultPan, custAccountNumber, accountNumber2, amount,mydate);
        if(responseCode.equals("000"))
        {
            System.out.println("Funds Transfer Successfull !");
        }
        else
        {
        System.out.println("Funds Transfer Failed !");
        }
    }
    catch(Exception e)
    {  
	   System.out.println("Sequencer.next()*******->"+Sequencer.next());
	   System.out.println("defaultPan*******->"+defaultPan);
	   System.out.println("custAccountNumber*******->"+custAccountNumber);
	   System.out.println("accountNumber2*******->"+accountNumber2);
	   System.out.println("amount*******->"+amount);
	   System.out.println((new StringBuilder("Error message********->")).append(e.getMessage()).toString());
	   e.printStackTrace();   
    }
    return responseCode;
 }
 
public String transferFund(String AccName, String Acct2, String Acct1, double Amt,String field124, String field125)
	throws Exception
	{
	Properties prop = new Properties();
	File file = new File("C:\\Property\\LegendPlus.properties");
	System.out.print((new StringBuilder("Absolute Path:>>> ")).append(file.getAbsolutePath()).toString());
	System.out.print("Able to load file ");
	FileInputStream in = new FileInputStream(file);
	prop.load(in);
	System.out.print("Able to load properties into prop");
	String hostIp = prop.getProperty("host.Ip");
	String hostPort = prop.getProperty("host.Port");
	String atm = prop.getProperty("Connect.field124");
	String hostController = prop.getProperty("host.Controller");
	String hostDefaultPan = prop.getProperty("host.Default.Pan");
	Session session = Session.getDefaultInstance(prop, null);
	boolean sessionDebug = true;
	if(atm!=""){field124=atm;}
	Properties props = System.getProperties();
	System.out.println("*****testprop******");
	System.out.println((new StringBuilder("hostIp ")).append(hostIp).toString());
	System.out.println((new StringBuilder("hostPort ")).append(hostPort).toString());
	System.out.println((new StringBuilder("hostController ")).append(hostController).toString());
	System.out.println((new StringBuilder("hostDefaultPan ")).append(hostDefaultPan).toString());
	System.out.println("****testprop*****");
	System.out.println("****field125*****"+field125+"      field124: "+field124);
	String responseCode = "";
	HostParameters hp = new HostParameters();
	hp.setHostIpAddress(hostIp);
	hp.setPort(Integer.parseInt(hostPort));
	hp.setTimeout(60);
	hp.setAquirerId("000000");
	hp.setForwardId("000000");
	hp.setCardAccId("00000000");
	hp.setCardAccName(AccName);
	hp.setTerminalId("12345678");
	hp.setCurrency("566");
	hp.setControllerId(hostController);
	WebbManager wm = new WebbManager(hp);
	wm.setDebug(true);
	String custAccountNumber = Acct1;
	String accountNumber2 = Acct2;
	String defaultPan = hostDefaultPan;
	double amount = Amt;
	try
	{    
		int seqNo = 227939;
		 System.out.println("Sequencer.next()*******->"+Sequencer.next());
	    Date mydate = new Date(); 
	    
	    responseCode = wm.doFundsTranfer(seqNo, defaultPan, custAccountNumber, accountNumber2, amount, mydate, field124, field125);
//	    responseCode = wm.doFundsTranfer(Sequencer.next(), defaultPan, custAccountNumber, accountNumber2, amount, mydate,field124);
	    if(responseCode.equals("000"))
	    {
	        System.out.println((new StringBuilder("Sequencer.next()*******->")).append(Sequencer.next()).toString());
	        System.out.println("Funds Transfer Successfull !");
	    } else
	    {
	        System.out.println("Funds Transfer Failed !");
	    }
	}
	catch(Exception e)
	{
	    System.out.println((new StringBuilder("Sequencer.next()*******->")).append(Sequencer.next()).toString());
	    System.out.println((new StringBuilder("defaultPan*******->")).append(defaultPan).toString());
	    System.out.println((new StringBuilder("custAccountNumber*******->")).append(custAccountNumber).toString());
	    System.out.println((new StringBuilder("accountNumber2*******->")).append(accountNumber2).toString());
	    System.out.println((new StringBuilder("amount*******->")).append(amount).toString());
	    System.out.println((new StringBuilder("Error message********->")).append(e.getMessage()).toString());
	    e.printStackTrace();
	}
	return responseCode;
	}
/*
public String interaccounttransferFund(String AccName, String Acct2, String Acct1, double Amt,String field124, String field125, String field126)
throws Exception
{ 
Properties prop = new Properties();
File file = new File("C:\\Property\\FixedAsset.properties");
System.out.print((new StringBuilder("Absolute Path:>>> ")).append(file.getAbsolutePath()).toString());
System.out.print("Able to load file ");
FileInputStream in = new FileInputStream(file);
prop.load(in); 
System.out.print("Able to load properties into prop");
String hostIp = prop.getProperty("host.Ip");
String hostPort = prop.getProperty("host.Port");
String hostController = prop.getProperty("host.Controller");
String hostDefaultPan = prop.getProperty("host.Default.Pan");
Session session = Session.getDefaultInstance(prop, null);
boolean sessionDebug = true;
Properties props = System.getProperties();
System.out.println("*****testprop******");
System.out.println((new StringBuilder("hostIp ")).append(hostIp).toString());
System.out.println((new StringBuilder("hostPort ")).append(hostPort).toString());
System.out.println((new StringBuilder("hostController ")).append(hostController).toString());
System.out.println((new StringBuilder("hostDefaultPan ")).append(hostDefaultPan).toString());
System.out.println("****testprop*****");
System.out.println("****field125*****"+field125+"      field124: "+field124+"   field125: "+field125+"   field126: "+field126);
String responseCode = "";
HostParameters hp = new HostParameters();
hp.setHostIpAddress(hostIp); 
hp.setPort(Integer.parseInt(hostPort));
hp.setTimeout(60);
hp.setAquirerId("000000");
hp.setForwardId("000000");
hp.setCardAccId("00000000");
hp.setCardAccName(AccName);
hp.setTerminalId("12345678");
hp.setCurrency("566");
hp.setControllerId(hostController);
WebbManager wm = new WebbManager(hp);
wm.setDebug(true);
String custAccountNumber = Acct1;
String accountNumber2 = Acct2;
String defaultPan = hostDefaultPan;
double amount = Amt;
try
{  
    Date mydate = new Date();
    responseCode = wm.interaccounttransferFund(Sequencer.next(), defaultPan, custAccountNumber, accountNumber2, amount, mydate, field124, field125, field126);
//    responseCode = wm.doFundsTranfer(Sequencer.next(), defaultPan, custAccountNumber, accountNumber2, amount, mydate,field124);
    if(responseCode.equals("000"))
    {
        System.out.println((new StringBuilder("Sequencer.next()*******->")).append(Sequencer.next()).toString());
        System.out.println("Funds Transfer Successfull !");
    } else
    {
        System.out.println("Funds Transfer Failed !");
    }
}
catch(Exception e)
{
    System.out.println((new StringBuilder("Sequencer.next()*******->")).append(Sequencer.next()).toString());
    System.out.println((new StringBuilder("defaultPan*******->")).append(defaultPan).toString());
    System.out.println((new StringBuilder("custAccountNumber*******->")).append(custAccountNumber).toString());
    System.out.println((new StringBuilder("accountNumber2*******->")).append(accountNumber2).toString());
    System.out.println((new StringBuilder("amount*******->")).append(amount).toString());
    System.out.println((new StringBuilder("Error message********->")).append(e.getMessage()).toString());
    e.printStackTrace();
}
return responseCode;
}
*/
}
