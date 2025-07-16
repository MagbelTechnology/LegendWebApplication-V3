package com.magbel.util;


// Referenced classes of package com.magbel.util:
//            CurrentDateTime

public class Converter
{

    public Converter() 
    { 
    }

    public static long abs(long n)
    {
        return n < 0L ? -n : n;
    }

    private static String toEng999(int k)
    {
        String units[] = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
        };
        String tens[] = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
        };
        int u = k % 10;
        int c = k / 100;
        int tu = k % 100;
        int t = (tu - u) / 10;
        String result = c != 0 ? (new StringBuilder()).append(units[c]).append(" Hundred").append(tu <= 0 ? "" : " ").toString() : "";
        return result = (new StringBuilder()).append(result).append(tu >= 20 ? (new StringBuilder()).append(tens[t]).append(u <= 0 ? "" : "-").append(units[u]).toString() : units[tu]).toString();
    }

    public static String toEnglish(long arg)
    {
        String grpName[] = {
            "", " Thousand", " Million", " Billion", " Trillion", " Quadrillion", " Quintillion"
        };
        String result = "";
        long n = abs(arg);
        for(int group_ctr = 0; n > 0L; group_ctr++)
        {
            int group = (int)(n % 1000L);
            n /= 1000L;
            if(group != 0)
            {
                result = (new StringBuilder()).append(toEng999(group)).append(grpName[group_ctr]).append(result.length() <= 0 ? "" : ",").append(result).toString();
            }
        }

        return arg == 0L ? "zero" : result;
    }

    public static String convert2Word(double arg)
    {
        CurrentDateTime cdt = new CurrentDateTime();
        String result = "";
        String x = (new Double(arg)).toString();
        String z[] = new String[2];
        CurrentDateTime _tmp = cdt;
        z = CurrentDateTime.tokenize(x, ".");
        String decimal_ = z[0];
        String fraction_ = z[1];
        String space_ = " ";
        if(Integer.parseInt(decimal_) > 0)
        {
            result = (new StringBuilder()).append(result).append(toEnglish(Integer.parseInt(decimal_))).toString();
        }
        if(Integer.parseInt(fraction_) > 0)
        {
            result = (new StringBuilder()).append(result).append(space_).append(toEnglish(Integer.parseInt(fraction_))).toString();
        }
        return result;
    }
}
