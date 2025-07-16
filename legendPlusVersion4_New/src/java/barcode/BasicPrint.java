package barcode;

import java.awt.*;
import java.awt.print.*;

import javax.swing.JLabel;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * @version 1.0
 */
public class BasicPrint extends JLabel implements Printable {
    String var0;

    public BasicPrint(String var0) {
        this.var0 = var0;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setColor(Color.black);

        String family = "IDAutomationHC39M";
        int style = Font.PLAIN;
        int size = 8;
        Font font = new Font(family, style, size);
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.drawString(var0, 0, 0 + fontMetrics.getAscent());

        return Printable.PAGE_EXISTS;
    }

    public void printNow() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        PageFormat pf = pjob.defaultPage();
        pjob.setPrintable(new BasicPrint(var0), pf);
        try {
            pjob.print();
        } catch (PrinterException ex) {
        }
    }
}
