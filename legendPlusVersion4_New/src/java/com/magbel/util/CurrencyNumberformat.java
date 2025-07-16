/*
 * CurrencyNumberformat.java
 *
 * Created on June 2, 2005, 12:39 PM
 */

package com.magbel.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author  Jejelowo Festus
 * @author  MagBel Technologies LTD
 * @author  Joseph Street Eleganza House Lagos.
 * @version 1.0
 */
public class CurrencyNumberformat {

    /** Creates a new instance of CurrencyNumberformat */
    public CurrencyNumberformat() {
    }

    public String formatAmount(String amount) {
System.out.println("===Amount=====>>>> "+amount);
        double myAmount = Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        String formated = formatter.format(myAmount);
        return formated;
    }

    public String formatAmount(double myAmount) {
        String formated = formatAmount(Double.toString(myAmount));
        return formated;
    }

    public String toDecimal(double amount) {
        DecimalFormat formatta = new DecimalFormat("##0.00");
        String formatted = formatta.format(amount);
        return formatted;
    }
     public String toBigDecimal(BigDecimal  amount) {
        DecimalFormat formatta = new DecimalFormat("##0.00");
        String formatted = formatta.format(amount);
        return formatted;
    }


public String formatNumber(String num) {
        boolean isnegative = false;
        if(num.startsWith("-")){
          num = num.substring(1, num.length());
            isnegative = true;
        }
        String result = "";
        int len = num.length();
        String formated = "";
        String last = "";
        while (len > 3) {
            num = num.substring(0, len);
            result = num.substring(num.length() - 3, num.length());

            formated = result + "," + formated;
            len -= 3;

        }
        last = num.substring(0, len);
        formated = last + "," + formated;
        if (formated.endsWith(",")) {
            formated = formated.substring(0, formated.length() - 1);
        }
        if(formated.startsWith(",")){
        formated = formated.substring(1, formated.length());
        }
        //System.out.println("<<<<<<<<<<<< final result " + formated);
       if(isnegative){formated = "-"+formated;}
        return formated;
    }
}
