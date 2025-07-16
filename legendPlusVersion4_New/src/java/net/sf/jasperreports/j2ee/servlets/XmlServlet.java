package net.sf.jasperreports.j2ee.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

public class XmlServlet extends BaseHttpServlet {
   private static final long serialVersionUID = 10200L;
   public static final String START_PAGE_INDEX_REQUEST_PARAMETER = "startPage";
   public static final String END_PAGE_INDEX_REQUEST_PARAMETER = "endPage";
   public static final String PAGE_INDEX_REQUEST_PARAMETER = "page";

   public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
      if (jasperPrintList == null) {
         throw new ServletException("No JasperPrint documents found on the HTTP session.");
      } else {
         int startPageIndex = -1;
         String startPageStr = request.getParameter("startPage");

         try {
            startPageIndex = Integer.parseInt(startPageStr);
         } catch (Exception var59) {
         }

         int endPageIndex = -1;
         String endPageStr = request.getParameter("endPage");

         try {
            endPageIndex = Integer.parseInt(endPageStr);
         } catch (Exception var58) {
         }

         int pageIndex = -1;
         String pageStr = request.getParameter("page");

         try {
            pageIndex = Integer.parseInt(pageStr);
         } catch (Exception var57) {
         }

         if (pageIndex >= 0) {
            startPageIndex = pageIndex;
            endPageIndex = pageIndex;
         }

         Boolean isBuffered = Boolean.valueOf(request.getParameter("buffered"));
         if (isBuffered) {
            FileBufferedOutputStream fbos = new FileBufferedOutputStream();
            JRXmlExporter exporter = this.getExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            if (startPageIndex >= 0) {
               exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, startPageIndex);
            }

            if (endPageIndex >= 0) {
               exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, endPageIndex);
            }

            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

            try {
               exporter.exportReport();
               fbos.close();
               if (fbos.size() > 0) {
                  response.setContentType("text/xml");
                  response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");
                  response.setContentLength(fbos.size());
                  ServletOutputStream ouputStream = response.getOutputStream();

                  try {
                     fbos.writeData(ouputStream);
                     fbos.dispose();
                     ouputStream.flush();
                  } finally {
                     if (ouputStream != null) {
                        try {
                           ouputStream.close();
                        } catch (IOException var55) {
                        }
                     }

                  }
               }
            } catch (JRException var62) {
               throw new ServletException(var62);
            } finally {
               fbos.close();
               fbos.dispose();
            }
         } else {
            response.setContentType("text/xml");
            response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");
            JRXmlExporter exporter = this.getExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            if (startPageIndex >= 0) {
               exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, startPageIndex);
            }

            if (endPageIndex >= 0) {
               exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, endPageIndex);
            }

            OutputStream ouputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

            try {
               exporter.exportReport();
            } catch (JRException var56) {
               throw new ServletException(var56);
            } finally {
               if (ouputStream != null) {
                  try {
                     ouputStream.close();
                  } catch (IOException var54) {
                  }
               }

            }
         }

      }
   }

   public JRXmlExporter getExporter() {
      return new JRXmlExporter();
   }
}
