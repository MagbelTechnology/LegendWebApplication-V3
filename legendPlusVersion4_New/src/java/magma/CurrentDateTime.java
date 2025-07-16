package magma;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class CurrentDateTime { //implements java.io.Serializable
    private int myDay;
    private int myMonth;
    private int myYear;
    private static String myD;
    public CurrentDateTime() {
        DateFormat dt = DateFormat.getDateInstance();
        myD = dt.format(new Date());
    }

    public String getCurrentDate() {
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat(
                "dd/MM/yyyy");
        String curr_date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new
                Date());
        return curr_date;
    }

    public void setDate(String value) {
        myD = value;
    }

    public String getDate() {
        return myD;
    }

    public static int getDay() {
        String[] tokens = tokenize(myD, " ");
        String daypart = tokens[1].substring(0, tokens[1].length() - 1);
        return Integer.parseInt(daypart);
    }

    public static int getMonth() {
        System.out.println(myD);

        String[] tokens = tokenize(myD, " ");
        // System.out.println(tokens[0]);
        return toInt(tokens[0]);
    }

    public static int getYear() {
        String[] tokens = tokenize(myD, " ");
        // System.out.println(tokens[2]);
        return Integer.parseInt(tokens[2]);
    }

    public static String[] tokenize(String dt, String delimiter) {
        String[] tokens = new String[3];
        StringTokenizer st = new StringTokenizer(dt, delimiter);
        int i = 0;
        while (st.hasMoreTokens()) {
            tokens[i++] = st.nextToken();
        }

        return tokens;
    }

    public static int toInt(String m) {
        if (m.equalsIgnoreCase("Jan")) {
            return 1;
        } else
        if (m.equalsIgnoreCase("Feb")) {
            return 2;
        } else
        if (m.equalsIgnoreCase("Mar")) {
            return 3;
        } else
        if (m.equalsIgnoreCase("Apr")) {
            return 4;
        } else
        if (m.equalsIgnoreCase("May")) {
            return 5;
        } else
        if (m.equalsIgnoreCase("Jun")) {
            return 6;
        } else
        if (m.equalsIgnoreCase("Jul")) {
            return 7;
        } else
        if (m.equalsIgnoreCase("Aug")) {
            return 8;
        } else
        if (m.equalsIgnoreCase("Sep")) {
            return 9;
        } else
        if (m.equalsIgnoreCase("Oct")) {
            return 10;
        } else
        if (m.equalsIgnoreCase("Nov")) {
            return 11;
        } else
        if (m.equalsIgnoreCase("Dec")) {
            return 12;
        }

        return 0;
    }

//convert string date to date
    public java.util.Date getDateValue(String s_date) {
        return java.sql.Date.valueOf(s_date);
    }

    //convert to calendar/sql format
    public String toCAL(String dt) {
        //year,mth,day to SQL
        String[] result = tokenize(dt.substring(0, 10), "/");

        // System.out.println("Year : in toCaL : " + result[2] + " " + result[1] + " " + result[0]);

        return result[2] + "/" + result[1] + "/" + result[0];
    }

    public String toSQL(String dt) {
        //year,mth,day to SQL
        String[] result = tokenize(dt.substring(0, 10), "-");

        // System.out.println("Year : in toSQL : " + result[2] + " " + result[1] + " " + result[0]);

        return result[2] + "/" + result[1] + "/" + result[0];
    }


    public boolean isLessThan(CurrentDateTime dt) {

        if (this.getYear() < dt.getYear()) {
            System.out.println("is lesss than this  " + this.getDate());
            return true;
        } else
        if (this.getYear() == dt.getYear()) {
            if (this.getMonth() < dt.getMonth()) {
                return true;
            } else
            if (this.getMonth() == dt.getMonth()) {
                if (this.getDay() < dt.getDay()) {
                    return true;
                }
            }
        }

        return false;
    }

    private String getPrefix(long diff) {
        String returnValue = "";
        for (int i = 0; i < diff; i++) {
            returnValue += "0";
        }

        return returnValue;
    }

    public String formatCode(long id) {
        String idString = Long.toString(id);
        long diff = 9 - idString.length();

        return getPrefix(diff) + idString;
    }

    public String toSQLFormat() {
        return Integer.toString(getYear()) + "-" + Integer.toString(getMonth()) +
                "-" + Integer.toString(getDay());
    }

    public boolean equalsGivenDate(String dt) {
        CurrentDateTime D = new CurrentDateTime();
        D.setDate(dt);

        if (this.getYear() == D.getYear()) {
            if (this.getMonth() == D.getMonth()) {
                if (this.getDay() == D.getDay()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getSystemDate() {

        return getYear() + "-" + getMonth() + "-" + getDay();

    }

    public String timeInstance() {
        DateFormat t = DateFormat.getTimeInstance();
        return t.format(new Date());

    }

    /*  public static void main(String args[])
      {
        CurrentDateTime m = new CurrentDateTime();
       System.out.println("System date "+ m.toSQL(getSystemDate()));
//   m.getYear();
      }*/
}
