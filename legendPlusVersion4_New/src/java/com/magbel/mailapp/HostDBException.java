package com.magbel.mailapp;


public class HostDBException extends Throwable
{

    public HostDBException()
    {
    }

    public HostDBException(int x, String y)
    {
    }

    public String getMessage()
    {
        return "Cannot Connect to Host Database";
    }
}