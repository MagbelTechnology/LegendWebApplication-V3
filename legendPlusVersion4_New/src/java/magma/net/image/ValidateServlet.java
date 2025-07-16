package magma.net.image;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import com.magbel.util.CurrencyNumberformat;

import magma.net.manager.FleetTransactManager;

/**
 * <p>Title: ValidateServlet.java</p>
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
public class ValidateServlet extends HttpServlet {

    private ServletContext context;
    private HashMap users = new HashMap();

    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {

        /*
         * First obtain the process request
         */

        String processRequest = request.getParameter("PR");
        String formType = request.getParameter("FT");

        if (processRequest != null) {
            if (processRequest.equalsIgnoreCase("DATE")) {
                processDateAddRequest(request, response);
            } else if (processRequest.equalsIgnoreCase("PMDATE")) {
                if (formType != null) {
                    processNoticeDates(request, response, formType);
                } else {
                    processPMDateRequest(request, response);
                }
            } else if (processRequest.equalsIgnoreCase("VATWHTAMOUNT")) { //process vat amount  and WHT amount
                processVatWhtAmount(request, response);
            } else if (processRequest.equalsIgnoreCase("VATAMOUNT")) { //process vat amount 
                processVatAmount(request, response);
            } else if (processRequest.equalsIgnoreCase("WHTAMOUNT")) { //process WHT amount
                processWhtAmount(request, response);
            } 
            else if (processRequest.equalsIgnoreCase("ALERTER")) { //alert service
                processAlertService(request, response);
            } else {
                processFormValidation(request, response);
            }
        }

        /*
         if ((targetId != null) && !accounts.containsKey(targetId.trim())) {
                     accounts.put(targetId.trim(), "account data");
                     request.setAttribute("targetId", targetId);
         context.getRequestDispatcher("/success.jsp").forward(request, response);
                 } else {
         context.getRequestDispatcher("/error.jsp").forward(request, response);
                 }
         */
    }

    private void processPMDateRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        String period = request.getParameter("PERIOD");
        String DB4N = request.getParameter("DB4N");

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        String tempDate = sdf.format(new Date());
        int periodDays = Integer.parseInt(period);
        int intDB4N = ( -1) * (Integer.parseInt(DB4N));

        String nextPMDate = addDaysToDate(tempDate, periodDays);
        String firstDate = addDaysToDate(nextPMDate, intDB4N);

        sb.append("<CycleDate>\n");
        sb.append("<firstDate>" + firstDate + "</firstDate>\n");
        sb.append("<nextPMDate>" + nextPMDate + "</nextPMDate>\n");
        sb.append("</CycleDate>");
        response.getWriter().write(sb.toString());

    }

    private void processNoticeDates(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String formType) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");

        String expiredDate = request.getParameter("ED");
        String classID = request.getParameter("CID");
        String[] params = getDateParameters(formType, classID);

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        int intDB4N = ( -1) * (Integer.parseInt(params[1]));
        String nextPMDate = expiredDate;
        String firstDate = addDaysToDate(nextPMDate, intDB4N);

        sb.append("<CycleDate>\n");
        sb.append("<firstDate>" + firstDate + "</firstDate>\n");
        sb.append("<nextPMDate>" + nextPMDate + "</nextPMDate>\n");
        sb.append("<DaysBeforeNotice>" + params[1] + "</DaysBeforeNotice>\n");
        sb.append("<FREQ>" + params[2] + "</FREQ>\n");
        sb.append("</CycleDate>");
        response.getWriter().write(sb.toString());

    }


    private void processFormValidation(HttpServletRequest request,
                                       HttpServletResponse response) throws
            IOException, ServletException {

        String targetId = request.getParameter("id");
        if ((targetId != null) && !users.containsKey(targetId.trim())) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<message>valid</message>");
        } else {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<message>invalid</message>");
        }

    }

    private void processDateAddRequest(HttpServletRequest request,
                                       HttpServletResponse response) throws
            IOException, ServletException {
        String formDate = request.getParameter("DT");
        String monthToAdd = request.getParameter("INTMONTH");
        /*
                 Subract 1 from the resulting date
                 if this date is for financial end period
         */
        String FN = request.getParameter("FN");
        if (FN != null) {
            formDate = addDaysToDate(formDate, -1);
        }
        int month = Integer.parseInt(monthToAdd);
        String calculatedMonth = "";

        if (month < 0) {
            calculatedMonth = deductMonthFromDate(formDate, month);
        } else {
            calculatedMonth = addMonthToDate(formDate, month);
        }
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("<message>" + calculatedMonth + "</message>");

    }

    public String addDaysToDate(String date, int days) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        java.util.Calendar calendarDate = null;
        String added = null;
        if (date == null) {
            added = null;
        } else {
            try {
                Date dDate = sdf.parse(date);
                long longDays = (long) days;
                long toAdd = (long) (longDays * 24 * 60 * 60 * 1000);
                long addValue = toAdd + (long) (dDate.getTime());
                dDate.setTime(addValue);
                added = sdf.format(dDate);
            } catch (Exception er) {
                System.out.println("WARN:Error adding days to date ->" + er);
            }

        }
        return added;
    }


    public String deductMonthFromDate(String date, int month) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
        java.util.Calendar calendarDate = null;
        String added = null;
        if (date == null) {
            added = null;
        } else {
            try {
                Date dDate = sdf.parse(date);
                calendarDate = new java.util.GregorianCalendar();
                calendarDate.setTime(dDate);
                calendarDate.add(java.util.Calendar.MONTH, ( -1) * month);
                dDate = calendarDate.getTime();
                added = sdf.format(dDate);
            } catch (Exception er) {
                System.out.println("WARN:Error adding date ->" + er);
            }

        }
        return added;
    }


    public String addMonthToDate(String date, int month) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd-MM-yyyy");
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

    private String[] getDateParameters(String formType, String classID) {
        magma.net.manager.FleetHistoryManager fm = new magma.net.manager.
                FleetHistoryManager();
        String[] params = new String[3];
        if (formType.equalsIgnoreCase("INS")) {
            params = fm.getInsuranceCycleParams(classID);
        } else if (formType.equalsIgnoreCase("FUL")) {
            //params = //fm.
        } else {
            params = fm.getLicenseCycleParams(classID);
        }

        return params;
    }

    private void processVatWhtAmount(HttpServletRequest request,
                                     HttpServletResponse response) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        //get vatwht amount
        CurrencyNumberformat fm = new CurrencyNumberformat();
        String costValue = request.getParameter("MAINTCOST");
//        String subj2Vat = request.getParameter("SUBJ2VAT");
        String subj2Wht = request.getParameter("SUBJ2WHT");
        String rate = request.getParameter("Rate");

        double maintCost_ = Double.parseDouble(costValue);
        double result[] = new double[2];
        result = new FleetTransactManager().getVatWhtAmount(maintCost_, subj2Wht, rate);

        String whtAmount = fm.toDecimal(result[0]);
         whtAmount = fm.toDecimal(result[1]);

        sb.append("<VATWHTAMOUNT>\n");
        sb.append("<whtAmount>" + whtAmount + "</whtAmount>\n");
        sb.append("<whtAmount>" + whtAmount + "</whtAmount>\n");
        sb.append("</VATWHTAMOUNT>");
        response.getWriter().write(sb.toString());

    }

    private void processAlertService(HttpServletRequest request,
                                     HttpServletResponse response) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        String superId = request.getParameter("SUPERID");
        String result = "0";
        result = new FleetTransactManager().countPendingTransaction(superId);
        response.getWriter().write(result);

    }

    private void processVatAmount(HttpServletRequest request,
                                     HttpServletResponse response) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        //get vatwht amount
        CurrencyNumberformat fm = new CurrencyNumberformat();
        String costValue = request.getParameter("MAINTCOST");
        String subj2Vat = request.getParameter("SUBJ2VAT");
 //       String subj2Wht = request.getParameter("SUBJ2WHT");
        String rate = request.getParameter("Rate");

        double maintCost_ = Double.parseDouble(costValue);
        double result[] = new double[2];
        result = new FleetTransactManager().getVatAmount(maintCost_, subj2Vat, rate);

        String vatAmount = fm.toDecimal(result[0]);
         vatAmount = fm.toDecimal(result[1]);

        sb.append("<VATAMOUNT>\n");
        sb.append("<vatAmount>" + vatAmount + "</vatAmount>\n");
        sb.append("<vatAmount>" + vatAmount + "</vatAmount>\n");
        sb.append("</VATAMOUNT>");
        response.getWriter().write(sb.toString());

    }

    private void processWhtAmount(HttpServletRequest request,
                                     HttpServletResponse response) throws
            IOException, ServletException {
        StringBuffer sb = new StringBuffer();

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        //get vatwht amount
        CurrencyNumberformat fm = new CurrencyNumberformat();
        String costValue = request.getParameter("MAINTCOST");
//        String subj2Vat = request.getParameter("SUBJ2VAT");
        String subj2Wht = request.getParameter("SUBJ2WHT");
        String rate = request.getParameter("Rate");

        double maintCost_ = Double.parseDouble(costValue);
        double result[] = new double[2];
        result = new FleetTransactManager().getWhtAmount(maintCost_, subj2Wht, rate);

        String whtAmount = fm.toDecimal(result[0]);
        whtAmount = fm.toDecimal(result[1]);

        sb.append("<WHTAMOUNT>\n");
        sb.append("<whtAmount>" + whtAmount + "</whtAmount>\n");
        sb.append("<whtAmount>" + whtAmount + "</whtAmount>\n");
        sb.append("</WHTAMOUNT>");
        response.getWriter().write(sb.toString());

    }


}
