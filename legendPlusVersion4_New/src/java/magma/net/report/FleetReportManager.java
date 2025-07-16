package magma.net.report;

import magma.net.dao.MagmaDBConnection;

/**
 * <p>Title: FleetReportManager.java</p>
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
public class FleetReportManager extends MagmaDBConnection {

    final String PDF_OUTPUT = "pdf";
    final String EXCEL_OUTPUT = "excel";
    final String HTML_OUTPUT = "html";

    public FleetReportManager() {
    }

    public static void main(String[] args) {
        FleetReportManager fleetreportmanager = new FleetReportManager();
    }
}
