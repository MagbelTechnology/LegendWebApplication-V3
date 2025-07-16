package com.magbel.mailapp;

import java.io.Serializable;

public class MessageValue
    implements Serializable
{

    private String mailTo;
    private String mailFrom;
    private String mailSubject;
    private String mailCC;
    private String mailBC;
    private String mailMessage;
    private String mailType;
    private String mailCode;
    private String mailNextNotedate;

    public MessageValue()
    {
        mailTo = null;
        mailFrom = null;
        mailSubject = null;
        mailCC = null;
        mailBC = null;
        mailMessage = null;
        mailType = null;
        mailCode = null;
    }

    public void setMailTo(String mailto)
    {
        mailTo = mailto;
    }

    public void setMailFrom(String mailfrom)
    {
        mailFrom = mailfrom;
    }

    public void setMailSubject(String mailsubject)
    {
        mailSubject = mailsubject;
    }

    public void setMailCC(String mailcc)
    {
        mailCC = mailcc;
    }

    public void setMailBC(String mailbc)
    {
        mailBC = mailbc;
    }

    public void setMailMessage(String mailmessage)
    {
        mailMessage = mailmessage;
    }

    public String getMailTo()
    {
        return mailTo;
    }

    public String getMailFrom()
    {
        return mailFrom;
    }

    public String getMailSubject()
    {
        return mailSubject;
    }

    public String getMailCC()
    {
        return mailCC;
    }

    public String getMailBC()
    {
        return mailBC;
    }

    public String getMailMessage()
    {
        return mailMessage;
    }

    public String getMailType()
    {
        return mailType;
    }

    public void setMailType(String mailType)
    {
        this.mailType = mailType;
    }

    public String getMailCode()
    {
        return mailCode;
    }

    public void setMailCode(String mailCode)
    {
        this.mailCode = mailCode;
    }

    public String getMailNextNotedate()
    {
        return mailNextNotedate;
    }

    public void setMailNextNotedate(String mailNextNotedate)
    {
        this.mailNextNotedate = mailNextNotedate;
    }
}