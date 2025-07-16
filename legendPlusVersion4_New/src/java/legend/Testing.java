
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Testing extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream out = null;

        String[] bas =  {"1","2","3","4","5","6","7","8","9"};
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Demo", 0);

            for(int x = 0; x<=bas.length; x++){
           // s.addCell(new Label(0, 0, "Hefllo World"));
             s.addCell(new Label(x, x, bas[x]));
            }
            w.write();
            w.close();
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

