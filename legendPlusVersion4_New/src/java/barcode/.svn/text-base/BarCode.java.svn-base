package barcode;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class BarCode extends Applet {
    boolean isStandalone = false;
    BorderLayout borderLayout1 = new BorderLayout();
    String var0;
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel1 = new JPanel();
    JButton jButton1 = new JButton();
    GridLayout gridLayout2 = new GridLayout();
    JLabel jLabel1 = new JLabel();
    //Get a parameter value
    public String getParameter(String key, String def) {
        return isStandalone ? System.getProperty(key, def) :
                (getParameter(key) != null ? getParameter(key) : def);
    }

    //Construct the applet
    public BarCode() {
    }

    //Initialize the applet
    public void init() {
        try {
            var0 = "*" + this.getParameter("param0", "000000") + "*";
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        this.setLayout(gridLayout1);
        gridLayout1.setColumns(1);
        gridLayout1.setRows(2);
        jButton1.setText("Print");
        jButton1.addActionListener(new BarCode_jButton1_actionAdapter(this));
        jPanel1.setBackground(Color.white);
        jPanel1.setLayout(gridLayout2);
        gridLayout2.setColumns(1);
        jLabel1.setFont(new java.awt.Font("IDAutomationHC39M", Font.PLAIN, 10));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText(var0);
        this.add(jPanel1);
        jPanel1.add(jLabel1);
        this.add(jButton1);
    }

    //Start the applet
    public void start() {
    }

    //Stop the applet
    public void stop() {
    }

    //Destroy the applet
    public void destroy() {
    }

    //Get Applet information
    public String getAppletInfo() {
        return "Applet Information";
    }

    //Get parameter info
    public String[][] getParameterInfo() {
        java.lang.String[][] pinfo = { {
                                     "param0", "String", ""},
        };
        return pinfo;
    }

    public void jButton1_actionPerformed(ActionEvent e) {
        new BasicPrint(var0).printNow();
    }
}


class BarCode_jButton1_actionAdapter implements ActionListener {
    private BarCode adaptee;
    BarCode_jButton1_actionAdapter(BarCode adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}
