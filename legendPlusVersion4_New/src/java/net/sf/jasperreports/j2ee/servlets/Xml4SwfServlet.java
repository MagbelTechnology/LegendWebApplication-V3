package net.sf.jasperreports.j2ee.servlets;

import net.sf.jasperreports.engine.export.JRXml4SwfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;

public class Xml4SwfServlet extends XmlServlet {
   private static final long serialVersionUID = 10200L;

   public JRXmlExporter getExporter() {
      return new JRXml4SwfExporter();
   }
}
