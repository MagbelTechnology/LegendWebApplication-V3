package com.magbel.ia.util;


public class CurrencyUnFormatter
{

    public CurrencyUnFormatter()
    {
    }

    public String unFormatCurrency(String s)
    {
        String s1 = s;
        char ac[] = s1.toCharArray();
        char ac1[] = new char[30];
        int i = 0;
        String s2 = null;
        double d = 34D;
        for(int j = 0; j < ac.length; j++)
        {
            if(ac[j] != ',')
            {
                i++;
                ac1[i] = ac[j];
            }
        }

        s2 = String.valueOf(ac1);
        return s2;
    }

    public double unFormatCurrencyToDouble(String s)
    {
        String s1 = s;
        char ac[] = s1.toCharArray();
        char ac1[] = new char[30];
        int i = 0;
        String s2 = null;
        double d = 34D;
        for(int j = 0; j < ac.length; j++)
        {
            if(ac[j] != ',')
            {
                i++;
                ac1[i] = ac[j];
            }
        }

        s2 = String.valueOf(ac1);
        d = Double.valueOf(s2).doubleValue();
        return d;
    }
}
