package net.sf.jasperreports.j2ee.servlets;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JasperPrint;

public abstract class BaseHttpServlet extends HttpServlet {
   private static final long serialVersionUID = 10200L;
   public static final String DEFAULT_JASPER_PRINT_LIST_SESSION_ATTRIBUTE = "net.sf.jasperreports.j2ee.jasper_print_list";
   public static final String DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE = "net.sf.jasperreports.j2ee.jasper_print";
   public static final String JASPER_PRINT_LIST_REQUEST_PARAMETER = "jrprintlist";
   public static final String JASPER_PRINT_REQUEST_PARAMETER = "jrprint";
   public static final String BUFFERED_OUTPUT_REQUEST_PARAMETER = "buffered";

   public static List getJasperPrintList(HttpServletRequest request) {
      String jasperPrintListSessionAttr = request.getParameter("jrprintlist");
      if (jasperPrintListSessionAttr == null) {
         jasperPrintListSessionAttr = "net.sf.jasperreports.j2ee.jasper_print_list";
      }

      String jasperPrintSessionAttr = request.getParameter("jrprint");
      if (jasperPrintSessionAttr == null) {
         jasperPrintSessionAttr = "net.sf.jasperreports.j2ee.jasper_print";
      }

      List jasperPrintList = (List)request.getSession().getAttribute(jasperPrintListSessionAttr);
      if (jasperPrintList == null) {
         JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(jasperPrintSessionAttr);
         if (jasperPrint != null) {
            jasperPrintList = new ArrayList();
            ((List)jasperPrintList).add(jasperPrint);
         }
      }

      return (List)jasperPrintList;
   }
}
