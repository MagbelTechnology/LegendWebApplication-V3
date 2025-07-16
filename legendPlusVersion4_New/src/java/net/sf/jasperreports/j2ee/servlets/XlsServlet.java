package net.sf.jasperreports.j2ee.servlets;

import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

public class XlsServlet extends AbstractXlsServlet {
   private static final long serialVersionUID = 10200L;

   protected JRXlsAbstractExporter getXlsExporter() {
      return new JRXlsExporter();
   }
}
