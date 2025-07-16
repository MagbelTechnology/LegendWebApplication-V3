/*
 * CalendarDate.java
 * Created on December 2, 2004, 12:56 PM
 * @author  Jejelowo Festus
 * @author  Ocular-Minds Group(TM)
 * @author  124,Itire road,Mushin.
 * @version 1.0
 */

package legend.objects;

import java.text.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class DatetimeFormat {
    SimpleDateFormat sdf;

    /** Creates a new instance of CalendarDate */
    public DatetimeFormat() {
        sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
    }

    public String formatDate() {

        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int year = date.get(Calendar.YEAR);
        String mt = String.valueOf(month + 1);
        String dy = String.valueOf(day);
        String yr = String.valueOf(year);

        int min = date.get(Calendar.HOUR);
        int sec = date.get(Calendar.MINUTE);

        String mn = String.valueOf(min);
        String sc = String.valueOf(sec);

        if (Integer.parseInt(mt) < 10) {
            mt = "0" + mt;
        }
        if (Integer.parseInt(dy) < 10) {
            dy = "0" + dy;
        }
        String formatedDate = dy + mt + yr;

        System.out.println("Done formating date.");

        return formatedDate;
    }

    public String getStringDateTimeStamp() {

        System.out.println("INFO:Using CalendarDate.formatDate()...");
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int year = date.get(Calendar.YEAR);
        String mt = String.valueOf(month + 1);
        String dy = String.valueOf(day);
        String yr = String.valueOf(year);

        int hrs = date.get(Calendar.HOUR);
        int min = date.get(Calendar.MINUTE);
        int sec = date.get(Calendar.SECOND);

        String hr = String.valueOf(hrs);
        String mn = String.valueOf(min);
        String sc = String.valueOf(sec);

        if (Integer.parseInt(mt) < 10) {
            mt = "0" + mt;
        }
        if (Integer.parseInt(dy) < 10) {
            dy = "0" + dy;
        }
        if (Integer.parseInt(hr) < 10) {
            hr = "0" + hr;
        }
        if (Integer.parseInt(mn) < 10) {
            mn = "0" + mn;
        }
        if (Integer.parseInt(sc) < 10) {
            sc = "0" + sc;
        }
        String formatedDate = dy + "-" + mt + "-" + yr + " " + hr + ":" + mn +
                              ":" + sc;

        System.out.println("Done formating date.");

        return formatedDate;
    }

    public String getDateTimeStamp() {

        System.out.println("INFO:Using CalendarDate.formatDate()...");
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int year = date.get(Calendar.YEAR);
        String mt = String.valueOf(month + 1);
        String dy = String.valueOf(day);
        String yr = String.valueOf(year);

        int min = date.get(Calendar.HOUR);
        int sec = date.get(Calendar.MINUTE);

        String mn = String.valueOf(min);
        String sc = String.valueOf(sec);

        if (Integer.parseInt(mt) < 10) {
            mt = "0" + mt;
        }
        if (Integer.parseInt(dy) < 10) {
            dy = "0" + dy;
        }
        if (Integer.parseInt(mn) < 10) {
            mn = "0" + mn;
        }
        if (Integer.parseInt(sc) < 10) {
            sc = "0" + sc;
        }
        String formatedDate = dy + mt + yr + mn + sc;

        System.out.println("Done formating date.");

        return formatedDate;
    }

    public String getTimeStamp() {

        GregorianCalendar date = new GregorianCalendar();

        int min = date.get(Calendar.HOUR);
        int sec = date.get(Calendar.MINUTE);

        String mn = String.valueOf(min);
        String sc = String.valueOf(sec);

        if (Integer.parseInt(mn) < 10) {
            mn = "0" + mn;
        }
        if (Integer.parseInt(sc) < 10) {
            sc = "0" + sc;
        }
        String formatedTime = mn + ":" + sc;

        return formatedTime;
    }

    public boolean isAValidDate(String date) {

        boolean isValid = true;
        try {
            Date dDate = sdf.parse(date);
            int day = Integer.parseInt(date.substring(0, 2));
            int mon = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 10));

            if ((day > 28) && (mon == 2)) {
                isValid = false;
            } else if ((day > 30) &&
                       (!((mon == 4) || (mon == 6) || (mon == 9) || (mon == 11)))) {
                isValid = false;
            } else if (mon > 12) {
                isValid = false;
            } else if (day > 31) {
                isValid = false;
            } else {
                isValid = true;
            }

        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    public String textDate() {

        System.out.println("INFO: Using CalendarDate.textDate()...");
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int year = date.get(Calendar.YEAR);
        String mt = String.valueOf(month + 1);
        String dy = String.valueOf(day);
        String yr = String.valueOf(year);

        if (Integer.parseInt(mt) < 10) {
            mt = "0" + mt;
        }
        if (Integer.parseInt(dy) < 10) {
            dy = "0" + dy;
        }
        String formatedDate = dy + "/" + mt + "/" + yr;

        System.out.println("Done formating date.");

        return formatedDate;

    }

    public java.sql.Date getSQLFormatedDate(java.util.Date tDate) {

        java.text.DateFormat formatter = java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.ENGLISH);

        String dDate = formatter.format(tDate);
        String strDate = dDate.replaceAll("/", "-");

        int year = Integer.parseInt(strDate.substring(strDate.lastIndexOf("-") +
                1, strDate.length()));
        int mon = Integer.parseInt(strDate.substring(0, strDate.indexOf("-")));
        int day = Integer.parseInt(strDate.substring(strDate.indexOf("-") + 1,
                strDate.lastIndexOf("-")));
        String strDay = "";
        String strMon = "";
        if (year < 1000) {
            year = year + 2000;
        }
        if (mon < 10) {
            strMon = "0" + Integer.toString(mon);
        } else {
            strMon = Integer.toString(mon);
        }
        if (day < 10) {
            strDay = "0" + Integer.toString(day);
        } else {
            strDay = Integer.toString(day);
        }

        strDate = strDay + "-" + strMon + "-" + Integer.toString(year);
        java.sql.Date formatedDate = null;
        try {
            formatedDate = new java.sql.Date(sdf.parse(strDate).getTime());
        } catch (Exception ee) {}
        return formatedDate;
    }

    public boolean isOpenDateExceedBOD(String openDate, String BODDate) {
	        boolean isRight = false;

	        System.out.println("INFO : Date1 = "+openDate+" Date2 = "+BODDate);

	        try{

	        String day1 = openDate.substring(6, 8);
	        String mon1 = openDate.substring(4, 6);
	        String year1 = openDate.substring(0, 4);

	        int dayAdder = Integer.parseInt(day1);
	        int mon = Integer.parseInt(mon1);
	        int year = Integer.parseInt(year1);

	        String temp = openDate;
	        int yearInt = Integer.parseInt((BODDate.substring(0, 4)));
	        int monInt = Integer.parseInt(BODDate.substring(4, 6));
	        int dayInt = Integer.parseInt(BODDate.substring(6, 8));

	        if (Integer.parseInt(temp.substring(0, 4)) > yearInt) {
	            // if&&{
	            isRight = true;

	        } else if (!(Integer.parseInt(temp.substring(0, 4)) < yearInt)) {
	            if (Integer.parseInt(temp.substring(4, 6)) > monInt) {
	                isRight = true;

	            } else if ((Integer.parseInt(mon1) == monInt) && (dayAdder > dayInt)) {
	                isRight = true;

	            } else {

	            }
	        } else {

	            isRight = false;
	        }

		}catch(Exception er){
			isRight = false;
			System.out.println("WARN : Error comparing dates -> "+er);
		}
	        return isRight;
    }

    public boolean isDateExceedOther(String firstDate, String secondDate) {
	        boolean isRight = false;

	        try{

	        String day1 = firstDate.substring(0, 2);
	        String mon1 = firstDate.substring(3, 5);
	        String year1 = firstDate.substring(6, 10);

	        int dayAdder = Integer.parseInt(day1);
	        int mon = Integer.parseInt(mon1);
	        int year = Integer.parseInt(year1);

	        String temp = firstDate;
	        int yearInt = Integer.parseInt((secondDate.substring(6, 10)));
	        int monInt = Integer.parseInt(secondDate.substring(3, 5));
	        int dayInt = Integer.parseInt(secondDate.substring(0, 2));

	        if (Integer.parseInt(temp.substring(6, 10)) > yearInt) {
	            // if&&{
	            isRight = true;

	        } else if (!(Integer.parseInt(temp.substring(6, 10)) < yearInt)) {
	            if (Integer.parseInt(temp.substring(3, 5)) > monInt) {
	                isRight = true;

	            } else if ((Integer.parseInt(mon1) == monInt) && (dayAdder > dayInt)) {
	                isRight = true;

	            } else {

	            }
	        } else {

	            isRight = false;
	        }

		}catch(Exception er){
			isRight = false;
			System.out.println("WARN : Error comparing dates -> "+er);
		}
	        return isRight;
    }

    public boolean isDateBetween(String date1, String date2, String testDate) {
        boolean isYes = false;
        return isYes;
    }

    public java.sql.Date getCurrentSQLDate() {

        java.util.Date transDateObj = null;
        java.sql.Date valDate = null;
        try {
            transDateObj = sdf.parse(textDate().replaceAll("/", "-"));
            valDate = getSQLFormatedDate(transDateObj);
        } catch (Exception ee) {
            System.out.println("WARNING:Error getting current Date->" + ee);
        }

        return valDate;
    }

    public int getDayDifference(String strFirstDate, String strSecondDate) {

        int daysDifference = 0;
        long dayDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
        daysDifference = (int) (Math.abs(dayDiffMills) / (24 * 60 * 60 * 1000));

        return daysDifference;

    }

    public int getMonthDifference(String strFirstDate, String strSecondDate) {

        int monthDifference = 0;
        long yearDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
        monthDifference = (int) ((Math.abs(yearDiffMills) * 12) /
                                 (365 * 24 * 60 * 60 * 1000));

        return monthDifference;

    }


    public int getYearDifference(String strFirstDate, String strSecondDate) {

        int yearDifference = 0;
        long yearDiffMills = getDateDiffrenceMillis(strFirstDate, strSecondDate);
        yearDifference = (int) (Math.abs(yearDiffMills) /
                                (365 * 24 * 60 * 60 * 1000));

        return yearDifference;

    }

    public long getDateDiffrenceMillis(String strFirstDate,
                                       String strSecondDate) {

        long dateDifferencesMills = 0l;
        try {
            Date firstDate = sdf.parse(strFirstDate);
            Date secondDate = sdf.parse(strSecondDate);
            dateDifferencesMills = firstDate.
                                   getTime() -
                                   secondDate.getTime();
        } catch (Exception e) {
            System.out.println("WARNING:Error finding Date - days different :" +
                               e);
        }
        return dateDifferencesMills;

    }

   public String getFirstDateOfMonth() {
	   String date = formatDate(new java.util.Date());
	   return getFirstDateOfMonth(date);
   }

   public String getLastDateOfMonth() {
  	   String date = formatDate(new java.util.Date());
  	   return getLastDateOfMonth(date);
   }

   public String getCurrentDate(){

	   return sdf.format(new java.util.Date());
   }


   public String getFirstDateOfMonth(String date) {

				java.util.Calendar calendarDate1 = null;
			  String endDate = "";
			  int lastDay = 0;
			  if(date == null){date = formatDate(new java.util.Date());}
			  date = date.replaceAll("/","-");
			  try {

						java.util.Date s1Date = sdf.parse(date);

						calendarDate1 = new java.util.GregorianCalendar();
						calendarDate1.setTime(s1Date);
						lastDay = calendarDate1.getActualMinimum(java.util.Calendar.DAY_OF_MONTH);

						String prefix = "";
						if(lastDay < 10){
							prefix = "0"+Integer.toString(lastDay);
						}else{
							prefix = Integer.toString(lastDay);
						}

						endDate = prefix+"-"+date.substring(3,date.length());

					} catch (Exception er) {
						System.out.println("WARN:Error getting month last date ->" + er);
					}

	      return endDate;
    }


	public String getLastDateOfMonth(String date) {

		java.util.Calendar calendarDate1 = null;
		  String endDate = "";
		  int lastDay = 0;
		  if(date == null){date = formatDate(new java.util.Date());}
		  date = date.replaceAll("/","-");
		  try {

					java.util.Date s1Date = sdf.parse(date);

					calendarDate1 = new java.util.GregorianCalendar();
					calendarDate1.setTime(s1Date);
					lastDay = calendarDate1.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

					String prefix = "";
					if(lastDay < 10){
						prefix = "0"+Integer.toString(lastDay);
					}else{
						prefix = Integer.toString(lastDay);
					}

				endDate = prefix+"-"+date.substring(3,date.length());

				} catch (Exception er) {
					System.out.println("WARN:Error getting month last date ->" + er);
				}

	      return endDate;
    }

    public String addMonthToDate(String date, int month) {


        java.util.Calendar calendarDate = null;
        String added = null;
        if (date == null) {
            added = null;
        } else {
            try {
                Date dDate = sdf.parse(date);
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH, month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            } catch (Exception er) {
                System.out.println("WARN:Error adding date ->" + er);
            }

        }
        return added;
    }

    public String addMonthToDate(Date dDate, int month) {

        java.util.Calendar calendarDate = null;
        String added = null;
        if (dDate == null) {
            added = null;
        } else {
            try {
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH, month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            } catch (Exception er) {
                System.out.println("WARN:Error adding date ->" + er);
            }

        }
        return added;
    }

    public Date getDateAddByMonth(Date dDate, int month) {

        java.util.Calendar calendarDate = null;
        Date added = null;
        if (dDate == null) {
            added = null;
        } else {
            try {
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH, month);
                dDate = calendarDate.getTime();
                added = dDate;
            } catch (Exception er) {
                System.out.println("WARN:Error adding date ->" + er);
            }

        }
        return added;
    }

       public String addDayToDate(Date dDate, int day) {

           java.util.Calendar calendarDate = null;
           String added = null;
           if (dDate == null) {
               added = null;
           } else {
               try {
                   calendarDate = new java.util.GregorianCalendar();
                   calendarDate.setTime(dDate);
                   calendarDate.add(java.util.Calendar.DATE, day);
                   dDate = calendarDate.getTime();
                   added = sdf.format(dDate);
               } catch (Exception er) {
                   System.out.println("WARN:Error adding date ->" + er);
               }

           }
           return added;
    }

    public java.sql.Date dateConvert(String strDate) {

        if (strDate == null) {
            strDate = sdf.format(new java.util.Date());
        }
        strDate = strDate.replaceAll("/", "-");
        java.util.Date inputDate = null;
        try {
            inputDate = sdf.parse(strDate);
        } catch (Exception e) {
            System.out.println("WARNING: Error formating Date:" + e.getMessage());
        }
        return this.getSQLFormatedDate(inputDate);

    }

    public java.sql.Date dateConvert(java.util.Date inputDate) {

        return this.getSQLFormatedDate(inputDate);

    }

    public java.sql.Date dateConvert(long longDate) {

        java.util.Date inputDate = new java.util.Date();
        inputDate.setTime(longDate);

        return this.getSQLFormatedDate(inputDate);

    }

    public String formatDate(java.util.Date date) {
        String formated = "";

        if (date == null) {
            date = new java.util.Date();
        }
        try {
            formated = sdf.format(date);
        } catch (Exception e) {
            System.out.println("WARNING:Error formating Date ->" + e.getMessage());
        }

        return formated;
    }

    public long getDateTime(String inputDate){

			long inputTime = 0l;

			try{

			if(inputDate == null){inputDate = sdf.format(new java.util.Date());}

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
			String strDate = dateFormat.format(new java.util.Date());
			String transInputDate = inputDate + strDate.substring(10,strDate.length());

			inputTime = (dateFormat.parse(transInputDate)).getTime();

		}catch(Exception ex){
			System.out.println("WARN : Error getting datetime ->"+ex);
		}

			return inputTime;
	}

	public long getDateTime(java.util.Date inputDate){


	String strDate = "";
	try{
		if(inputDate == null){
			strDate = sdf.format(new java.util.Date());
			}else{
				strDate =  sdf.format(inputDate);
			}
	}catch(Exception ex){
		System.out.println("WARN : error occured -> "+ex);
	}

		return getDateTime(strDate);
	}

    public static void main(String[] args) {
        DatetimeFormat dt = new DatetimeFormat();
    }

}
