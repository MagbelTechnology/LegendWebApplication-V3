package com.magbel.ia.vao;


public class Banks
{

    private String id;
    private String code;
    private String name;
    private int phone;
    private int fax;
    private String address;
    private int accountNo;
    private int bankAcct;
    private String currency;
    private String description;
    private String branch;
    private String contactPerson;
    private int personno;
    private String status;

    public Banks(String id, String code, String name, int phone, int fax, String address, int accountNo, 
            int bankAcct, String currency, String description, String branch, String contactPerson, int personno, String status)
    {
        setId(id);
        setCode(code);
        setName(name);
        setPhone(phone);
        setFax(fax);
        setAddress(address);
        setAccountNo(accountNo);
        setBankAcct(bankAcct);
        setCurrency(currency);
        setDescription(description);
        setBranch(branch);
        setContactPerson(contactPerson);
        setPersonno(personno);
        setStatus(status);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPhone(int phone)
    {
        this.phone = phone;
    }

    public void setFax(int fax)
    {
        this.fax = fax;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setAccountNo(int accountNo)
    {
        this.accountNo = accountNo;
    }

    public void setBankAcct(int bankAcct)
    {
        this.bankAcct = bankAcct;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setBranch(String branch)
    {
        this.branch = branch;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public void setPersonno(int personno)
    {
        this.personno = personno;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public int getPhone()
    {
        return phone;
    }

    public int getFax()
    {
        return fax;
    }

    public String getAddress()
    {
        return address;
    }

    public int getAccountNo()
    {
        return accountNo;
    }

    public int getBankAcct()
    {
        return bankAcct;
    }

    public String getCurrency()
    {
        return currency;
    }

    public String getDescription()
    {
        return description;
    }

    public String getBranch()
    {
        return branch;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public int getPersonno()
    {
        return personno;
    }

    public String getStatus()
    {
        return status;
    }
}
