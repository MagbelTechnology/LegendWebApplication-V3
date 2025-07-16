package com.magbel.ia.vao;


public class Column
{

    private String id;
    private String bank;
    private String column;
    private String sort;
    private String status;

    public Column(String id, String bank, String column, String sort, String status)
    {
        setId(id);
        setBank(bank);
        setColumn(column);
        setSort(sort);
        setStatus(status);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getId()
    {
        return id;
    }

    public String getBank()
    {
        return bank;
    }

    public String getColumn()
    {
        return column;
    }

    public String getSort()
    {
        return sort;
    }

    public String getStatus()
    {
        return status;
    }
}
