package legend;

import java.util.*;

public class DateManipulations {
    public static String CalendarToDate(Calendar c) {
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        return "" + day + "/" + month + "/" + year;
    }

    public static String CalendarToDb(Calendar c) {
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        return "" + month + "/" + day + "/" + year;
    }

    public static Calendar DateToCalendar(String s) {
        Calendar c = null;
        StringTokenizer t = new StringTokenizer(s, "/");
        if (t.countTokens() == 3) {
            String d = t.nextToken();
            String m = t.nextToken();
            String y = t.nextToken();

            int day = Integer.parseInt(d);
            int month = Integer.parseInt(m) - 1;
            int year = Integer.parseInt(y);

            c = new GregorianCalendar(year, month, day);
        }

        return c;
    }
}
