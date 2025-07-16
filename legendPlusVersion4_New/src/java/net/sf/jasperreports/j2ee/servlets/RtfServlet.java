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
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

public class RtfServlet extends BaseHttpServlet {
   private static final long serialVersionUID = 10200L;

   public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
      if (jasperPrintList == null) {
         throw new ServletException("No JasperPrint documents found on the HTTP session.");
      } else {
         Boolean isBuffered = Boolean.valueOf(request.getParameter("buffered"));
         if (isBuffered) {
            FileBufferedOutputStream fbos = new FileBufferedOutputStream();
            JRRtfExporter exporter = new JRRtfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

            try {
               exporter.exportReport();
               fbos.close();
               if (fbos.size() > 0) {
                  response.setContentType("application/rtf");
                  response.setHeader("Content-Disposition", "inline; filename=\"file.rtf\"");
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
                        } catch (IOException var39) {
                        }
                     }

                  }
               }
            } catch (JRException var43) {
               throw new ServletException(var43);
            } finally {
               fbos.close();
               fbos.dispose();
            }
         } else {
            response.setContentType("application/rtf");
            response.setHeader("Content-Disposition", "inline; filename=\"file.rtf\"");
            JRRtfExporter exporter = new JRRtfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            OutputStream ouputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

            try {
               exporter.exportReport();
            } catch (JRException var41) {
               throw new ServletException(var41);
            } finally {
               if (ouputStream != null) {
                  try {
                     ouputStream.close();
                  } catch (IOException var40) {
                  }
               }

            }
         }

      }
   }
}
