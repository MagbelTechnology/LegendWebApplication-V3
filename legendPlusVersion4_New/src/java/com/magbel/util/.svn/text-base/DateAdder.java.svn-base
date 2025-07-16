package com.magbel.util;
import java.util.*;
/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class DateAdder {
    public DateAdder() {
    }

    public String addMonthToDate(String date,int month){
        java.text.SimpleDateFormat sdf = new  java.text.SimpleDateFormat("dd-MM-yyyy");
        java.util.Calendar calendarDate = null;
        String added = null;
        if(date == null){
            added = null;
        }else{
            try{
                Date dDate = sdf.parse(date) ;
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH,month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            }catch(Exception er){
                System.out.println("WARN:Error adding date ->"+er);
            }

        }
        return added;
    }

    public static void main(String[] args) {
        DateAdder dateadder = new DateAdder();
        System.out.println("5 months to 22-03-2006 = "+dateadder.addMonthToDate("22-03-2006",5));
    }
}
