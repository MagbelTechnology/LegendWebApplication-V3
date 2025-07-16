package com.magbel.ia.vao;


public class Header
{
   
    private String pay1;
    private String pay2;
    private String pay3;
    private String pay4;
    private String pay5;
    private String pay6;
    private String pay7;
    private String pay8;
    private String message;

    public Header()
    {
    }
  
    public Header(String pay1, String pay2, String pay3, String pay4, String pay5, String pay6, String pay7, 
            String pay8, String message)
    {
        this.pay1 = pay1;
        this.pay2 = pay2;
        this.pay3 = pay3;
        this.pay4 = pay4;
        this.pay5 = pay5;
        this.pay6 = pay6;
        this.pay7 = pay7;
        this.pay8 = pay8;
        this.message = message;
    }

    public void setPay1(String pay1)
    {
        this.pay1 = pay1;
    }

    public void setPay2(String pay2)
    {
        this.pay2 = pay2;
    }

    public void setPay3(String pay3)
    {
        this.pay3 = pay3;
    }

    public void setPay4(String pay4)
    {
        this.pay4 = pay4;
    }

    public void setPay5(String pay5)
    {
        this.pay5 = pay5;
    }

    public void setPay6(String pay6)
    {
        this.pay6 = pay6;
    }

    public void setPay7(String pay7)
    {
        this.pay7 = pay7;
    }

    public void setPay8(String pay8)
    {
        this.pay8 = pay8;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getPay1()
    {
        return pay1;
    }

    public String getPay2()
    {
        return pay2;
    }

    public String getPay3()
    {
        return pay3;
    }

    public String getPay4()
    {
        return pay4;
    }

    public String getPay5()
    {
        return pay5;
    }

    public String getPay6()
    {
        return pay6;
    }

    public String getPay7()
    {
        return pay7;
    }

    public String getPay8()
    {
        return pay8;
    }

    public String getMessage()
    {
        return message;
    }

}
