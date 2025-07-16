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
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

public abstract class AbstractXlsServlet extends BaseHttpServlet {
   private static final long serialVersionUID = 10200L;

   public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
      if (jasperPrintList == null) {
         throw new ServletException("No JasperPrint documents found on the HTTP session.");
      } else {
         Boolean isBuffered = Boolean.valueOf(request.getParameter("buffered"));
         if (isBuffered) {
            FileBufferedOutputStream fbos = new FileBufferedOutputStream();
            JRXlsAbstractExporter exporter = this.getXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

            try {
               exporter.exportReport();
               fbos.close();
               if (fbos.size() > 0) {
                  response.setContentType("application/xls");
                  response.setHeader("Content-Disposition", "inline; filename=\"file.xls\"");
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
                        } catch (IOException var40) {
                        }
                     }

                  }
               }
            } catch (JRException var44) {
               throw new ServletException(var44);
            } finally {
               fbos.close();
               fbos.dispose();
            }
         } else {
            response.setContentType("application/xls");
            response.setHeader("Content-Disposition", "inline; filename=\"file.xls\"");
            JRXlsAbstractExporter exporter = this.getXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            OutputStream ouputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

            try {
               exporter.exportReport();
            } catch (JRException var41) {
               throw new ServletException(var41);
            } finally {
               if (ouputStream != null) {
                  try {
                     ouputStream.close();
                  } catch (IOException var39) {
                  }
               }

            }
         }

      }
   }

   protected abstract JRXlsAbstractExporter getXlsExporter();
}
