package com.magbel.mailapp;

import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;

// Referenced classes of package com.magbel.mailapp:
//            HostDBException, PropertiesFileReader, MessageValue

public class transport
    implements ConnectionListener, TransportListener
{

    static String msgText = "This is a message body.\nHere's the second line.";
    static String msgText2 = "\nThis was sent by transport.java demo program.";
    static String from;
    static String filename = "magbel.txt";

    public static synchronized boolean doSend(MessageValue msgValue, String sFileName)
        throws HostDBException
    {
        Properties props = new Properties();
        InternetAddress addrs[] = null;
        String smtpuser = "";
        String smtphost = "";
        String smtpport = "";
        try
        {
            props = PropertiesFileReader.getProperties();
            smtphost = props.getProperty("mail.smtp.host");
            smtpuser = props.getProperty("mail.smtp.user");
            smtpport = props.getProperty("mail.smtp.port");
        }
        catch(Exception Ex)
        {
            System.out.println("Error = " + Ex.getMessage());
        }
        filename = sFileName;
        boolean debug = false;
        props.put("mail.smtp.user", smtpuser);
        props.put("mail.smtp.host", smtphost);
        props.put("mail.smtp.port", smtpport);
        try
        {
            addrs = InternetAddress.parse(msgValue.getMailTo(), false);
        }
        catch(AddressException aex)
        {
            System.out.println("Invalid Address");
            aex.printStackTrace();
            boolean flag = false;
            boolean flag1 = flag;
            return flag1;
        }
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        transport t = new transport();
        try
        {
            t.go(session, addrs, msgValue);
        }
        catch(HostDBException e)
        {
            throw new HostDBException(201, "transport: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return true;
    }

    public transport()
    {
    }

    public void go(Session session, InternetAddress toAddr[], MessageValue msgValue)
        throws HostDBException
    {
label0:
        {
            Transport trans = null;
            InternetAddress ccAaddrs[] = null;
            try
            {
                try
                {
                    try
                    {
                        ccAaddrs = InternetAddress.parse(msgValue.getMailCC(), false);
                    }
                    catch(AddressException aex)
                    {
                        System.out.println("Invalid Address");
                        aex.printStackTrace();
                        throw new HostDBException(201, "transport: ".concat(String.valueOf(String.valueOf(aex.getMessage()))));
                    }
                    Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(msgValue.getMailFrom()));
                    msg.setRecipients(javax.mail.Message.RecipientType.TO, toAddr);
                    if(ccAaddrs != null)
                    {
                        msg.addRecipients(javax.mail.Message.RecipientType.CC, ccAaddrs);
                    }
                    msg.setSubject(msgValue.getMailSubject());
                    msg.setSentDate(new Date());
                    msg.setContent(msgValue.getMailMessage(), "text/html");
                    msg.saveChanges();
                    trans = session.getTransport(toAddr[0]);
                    trans.addConnectionListener(this);
                    trans.addTransportListener(this);
                    try
                    {
                        trans.connect();
                    }
                    catch(Exception ex)
                    {
                        throw new HostDBException(201, "transport: ".concat(String.valueOf(String.valueOf(ex.getMessage()))));
                    }
                    trans.sendMessage(msg, toAddr);
                    try
                    {
                        Thread.sleep(5L);
                    }
                    catch(InterruptedException interruptedexception) { }
                }
                catch(MessagingException mex)
                {
                    try
                    {
                        Thread.sleep(5L);
                    }
                    catch(InterruptedException e)
                    {
                        throw new HostDBException(201, "transport: ".concat(String.valueOf(String.valueOf(mex.getMessage()))));
                    }
                    mex.printStackTrace();
                    System.out.println();
                    Exception ex = mex;
                    do
                    {
                        if(ex instanceof SendFailedException)
                        {
                            SendFailedException sfex = (SendFailedException)ex;
                            javax.mail.Address invalid[] = sfex.getInvalidAddresses();
                            if(invalid != null)
                            {
                                System.out.println("    ** Invalid Addresses");
                                if(invalid != null)
                                {
                                    for(int i = 0; i < invalid.length; i++)
                                    {
                                        System.out.println("         ".concat(String.valueOf(String.valueOf(invalid[i]))));
                                    }

                                }
                            }
                            javax.mail.Address validUnsent[] = sfex.getValidUnsentAddresses();
                            if(validUnsent != null)
                            {
                                System.out.println("    ** ValidUnsent Addresses");
                                if(validUnsent != null)
                                {
                                    for(int i = 0; i < validUnsent.length; i++)
                                    {
                                        System.out.println("         ".concat(String.valueOf(String.valueOf(validUnsent[i]))));
                                    }

                                }
                            }
                            javax.mail.Address validSent[] = sfex.getValidSentAddresses();
                            if(validSent != null)
                            {
                                System.out.println("    ** ValidSent Addresses");
                                if(validSent != null)
                                {
                                    for(int i = 0; i < validSent.length; i++)
                                    {
                                        System.out.println("         ".concat(String.valueOf(String.valueOf(validSent[i]))));
                                    }

                                }
                            }
                        }
                        System.out.println();
                        if(ex instanceof MessagingException)
                        {
                            ex = ((MessagingException)ex).getNextException();
                        } else
                        {
                            ex = null;
                        }
                    } while(ex != null);
                    break label0;
                }
                break label0;
            }
            finally
            {
                try
                {
                    trans.close();
                }
                catch(MessagingException messagingexception) { }
            }
        }
    }

    public void opened(ConnectionEvent e)
    {
        System.out.println(">>> ConnectionListener.opened()");
    }

    public void disconnected(ConnectionEvent connectionevent1)
    {
    }

    public void closed(ConnectionEvent e)
    {
        System.out.println(">>> ConnectionListener.closed()");
    }

    public void messageDelivered(TransportEvent e)
    {
        System.out.print(">>> TransportListener.messageDelivered().");
        System.out.println(" Valid Addresses:");
        javax.mail.Address valid[] = e.getValidSentAddresses();
        if(valid != null)
        {
            for(int i = 0; i < valid.length; i++)
            {
                System.out.println("    ".concat(String.valueOf(String.valueOf(valid[i]))));
            }

        }
    }

    public void messageNotDelivered(TransportEvent e)
    {
        System.out.print(">>> TransportListener.messageNotDelivered().");
        System.out.println(" Invalid Addresses:");
        javax.mail.Address invalid[] = e.getInvalidAddresses();
        if(invalid != null)
        {
            for(int i = 0; i < invalid.length; i++)
            {
                System.out.println("    ".concat(String.valueOf(String.valueOf(invalid[i]))));
            }

        }
    }

    public void messagePartiallyDelivered(TransportEvent transportevent1)
    {
    }

}