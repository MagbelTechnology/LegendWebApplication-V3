package legend;

import java.util.Date;
import java.util.StringTokenizer;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Servletx extends RemoteScripting {
    public void log(String msg) {
        System.out.println("RemoteScripting: " + msg);
    }

    public static String getDateDifference(String val) throws Throwable {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        java.util.Calendar calendarDate = null;
        String added = "notadded";
        StringTokenizer tokens = new StringTokenizer(val, ",");

        String date = tokens.nextToken();
        String ynum = tokens.nextToken();
        String mnum = tokens.nextToken();
        String dnum = tokens.nextToken();
        date = date.replaceAll("-", "/");
        if (date == null) {
            added = "notadate";
        } else {
            Date dDate = sdf2.parse(date);
            calendarDate = new java.util.GregorianCalendar();
            calendarDate.setTime(dDate);
            calendarDate.add(java.util.Calendar.YEAR, Integer.parseInt(ynum));
            calendarDate.add(java.util.Calendar.MONTH, Integer.parseInt(mnum));
            calendarDate.add(java.util.Calendar.DATE, Integer.parseInt(dnum));
            dDate = calendarDate.getTime();
            added = sdf.format(dDate);
        }

        return added;
    }

    /**
     * getNextProcDate
     *
     * @param val String
     * @return String
     * @throws Throwable
     */
    public static String getNextProcDate(String val) throws Throwable {
        return getDateDifference(val);
    }

    /**
     * getNextProcDate
     *
     * @param val String
     * @return String
     * @throws Throwable
     */
    public static String getPerEndDate(String val) throws Throwable {
        return getDateDifference(val);
    }
}
