package com.magbel.ia.vao;


public class Account
{

    private String id;
    private String no;

    public Account()
    {
    }

    public Account(String id, String no)
    {
        this.id = id;
        this.no = no;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setNo(String no)
    {
        this.no = no;
    }

    public String getNo()
    {
        return no;
    }
}
