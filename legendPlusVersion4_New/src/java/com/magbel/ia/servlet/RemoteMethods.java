package com.magbel.ia.servlet;
import javax.servlet.*;
import com.magbel.util.DataConnect;
import com.magbel.util.NumberToWordConverter;
import javax.servlet.http.*;

import java.io.*;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import account.ConnectionClass;

// Referenced classes of package com.magbel.ia.servlet:
//            RemoteScripting

public class RemoteMethods extends RemoteScripting
{
	 private static ConnectionClass conClass;
    public RemoteMethods()
    {
    }

    public void log(String msg)
    {
        System.out.println((new StringBuilder()).append("RemoteScripting: ").append(msg).toString());
    }

    public static String getAmountInWords(String amount)
    {
        return NumberToWordConverter.wordValue(amount);
    }

    public static String getVendorAC(String vendor)
    {
        return "Pending";
    }

    public static String getDepEndDate(String vals)
    {
        if(vals != null)
        {
            StringTokenizer st1 = new StringTokenizer(vals, ",");
            if(st1.countTokens() == 2)
            {
                String s1 = st1.nextToken();
                String s2 = st1.nextToken();
                Float rate = Float.valueOf(Float.parseFloat(s1));
                StringTokenizer st2 = new StringTokenizer(s2, "/");
                if(st2.countTokens() == 3)
                {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();
                    if(year.length() == 4 && day.length() > 0 && day.length() < 3 && month.length() > 0 && month.length() < 3)
                    {
                        Calendar c = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                        int months = (int)((100F / rate.floatValue()) * 12F);
                        c.add(2, months);
                        c.add(6, -1 * Integer.parseInt(day));
                        int endDay = c.get(7);
                        int endMonth = c.get(2) + 1;
                        int endYear = c.get(1);
                        return (new StringBuilder()).append(endDay).append("/").append(endMonth).append("/").append(endYear).toString();
                    }
                }
            }
        }
        return "Error";
    }

    public static String addMonthToDate(String vals)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendarDate = null;
        vals = vals.replaceAll("%", "");
        vals = vals.trim();
        System.out.println((new StringBuilder()).append("Val received is :").append(vals).toString());
        StringTokenizer st1 = new StringTokenizer(vals, ",");
        String date = st1.nextToken();
        String strMonth = st1.nextToken();
        int month = (new Integer(strMonth)).intValue();
        String added = "";
        if(date == null)
        {
            added = "";
        } else
        {
            try
            {
                java.util.Date dDate = sdf.parse(date);
                calendarDate = new GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(2, month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            }
            catch(Exception er)
            {
                System.out.println((new StringBuilder()).append("WARN:Error adding date ->").append(er).toString());
            }
        }
        return added;
    }
    public static String getPaymentAccount(String payCode) {

        ResultSet rs = null;
        Connection con = null;
        String query = "SELECT LEDGER_NO FROM IA_EMPLOYEE_GRADE " +
                       "WHERE PAY_CODE = '" + payCode + "'";
        System.out.println("getPaymentAccount query: "+query);
        try {
            con = (new DataConnect("ias")).getConnection();
            conClass = new ConnectionClass();
            rs = conClass.getStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException ex) {

        } catch (Exception exr) {
            System.out.println("WAR: RemoteMethod >>Error getting Account Number " +
                               exr);
        } finally {
            conClass.freeResource();
        }
        return "";

    }    
}
