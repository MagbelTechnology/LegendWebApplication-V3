
package com.magbel.util;
import java.util.*;
import java.text.*;

public class NumberToWordConverter {

 public static long abs (final long n){
	 return  n >= 0 ? n : -n;
 }

 private  static String toEng999 (int k){

  final   String[] units = {""   ,"One"    ,"Two"      ,"Three"   ,"Four",
                           "Five","Six"    ,"Seven"    ,"Eight"   ,"Nine",
                            "Ten","Eleven" ,"Twelve"   ,"Thirteen","Fourteen",
                        "Fifteen","Sixteen","Seventeen","Eighteen","Nineteen"};
  final   String[] tens =  {""   , ""      ,"Twenty"   ,"Thirty"  ,"Forty" ,
                          "Fifty","Sixty"  ,"Seventy"  ,"Eighty"  ,"Ninety"};

  int    u =  k % 10;                    //  Separate the
  int    c =  k / 100;                   //     values of the
  int    tu=  k % 100;                   //        three digits
  int    t = (tu-u) / 10;

  String result = (c == 0)  ? ""         //  Insert hundreds portion
                            : units[c] + " Hundred"
                            + (tu > 0 ? " " : "");

  return result += (tu < 20) ? units[tu] //  Insert tens and units portion
                             : tens [t]
                               + ( u > 0 ? "-" : "")
                               + units[u];
}

public static  String toEnglish (long arg){
	final  String[] grpName = {""," Thousand"," Million"," Billion",
                               " Trillion"," Quadrillion"," Quintillion"};

 String result = "";
 long n = abs(arg);
 for (int   group_ctr=0; n>0; ++group_ctr) {
	 int   group = (int) (n % 1000);
      n /= 1000;     //      right to left
      if  (group != 0)
         result = toEng999(group)
         + grpName[group_ctr]
         +(result.length() > 0 ? "," : "")
         + result;
     }
 return arg != 0 ? result : "zero";
}

public static String[] tokenize(String dt,String delimiter){

   String[] tokens = new String[3];
   StringTokenizer st = new StringTokenizer(dt, delimiter);
   int i=0;
   while(st.hasMoreTokens())
      tokens[i++] = st.nextToken();

   return tokens;

  }

public static String wordValue(String amount){

	   String result = "";
	   String[] z = new String[2];
	   amount = amount.replaceAll(",","");
	   z = tokenize(amount,".");

	   String decimal_ = z[0];
	   String fraction_ = z[1];

	   String space_ = " ";

	  if(Integer.parseInt(decimal_) > 0){
	       result = result + toEnglish(Integer.parseInt(decimal_))+" naira";
	   }
	    if(Integer.parseInt(fraction_) > 0){
	        result = result + " and " + toEnglish(Integer.parseInt(fraction_)) +" kobo";
	    }

   return result;
}

public static String wordValue(double amount){
   String strAmount = (new Double(amount)).toString();
   return wordValue(strAmount);
}

public static String wordValue(int amount){
  String strAmount = (new Integer(amount)).toString();
   return wordValue(strAmount);
}

}

