package magma.net.image;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * <p>Title:Image Displayer </p>
 *
 * <p>Description: This displays already scanned image/signature
 * <br>
 * to be previewed before actual saving to ePostmanage DB
 * <br>
 * scanner interface.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies Limited</p>
 *
 * @author Jejelowo B. Festus
 * @author Java System Architect
 * @author text_festus@yahoo.com
 * @version 1.0
 * @see epostmanager.dao.ePostAppletScanner
 */

class DisplayPanel extends JPanel {
    private Image frontImage;
    private Image sideImage;
    private Image areaImage;
    private Image backImage;
    private JFrame frame;
    private Graphics graphics;

    public DisplayPanel(Image frontView, Image sideView, Image BackView,
                        Image areaView) {
        this.frontImage = frontView;
        this.sideImage = sideView;
        this.backImage = BackView;
        this.areaImage = areaView;
        repaint();
    }

    public void clearGraphics() {
        this.frontImage = null;
        this.sideImage = null;
        this.areaImage = null;
        this.backImage = null;
        revalidate();
        repaint();
        this.graphics.dispose();
    }

    public void paintComponent(Graphics g) {
        this.graphics = g;
        graphics.setColor(Color.BLACK);
        if (this.frontImage != null) {
            graphics.drawImage(this.frontImage, 20, 10, this);
        }
        if (this.areaImage != null) {
            graphics.drawImage(this.areaImage, 140,
                               getPreferredSize().height, this);
        }
        if (this.backImage != null) {
            graphics.drawImage(this.backImage, 260,
                               getPreferredSize().height, this);
        }
        if (this.sideImage != null) {
            graphics.drawImage(this.sideImage, 280, getPreferredSize().height, this);
        }
        // if (this.sideImage != null) graphics.drawImage(this.sideImage, getPreferredSize().width,
        //getPreferredSize().height, this);
        if ((this.backImage == null) && (this.frontImage == null)) {
            graphics.drawString("Oops! NO IMAGE LOADED. ", 20, 20);
        }

    }

    public Dimension getPreferredSize() {
        if (this.frontImage == null || this.frontImage.getHeight(null) <= 0) {
            return new Dimension(20, 10);
        } else {
            return new Dimension(this.frontImage.getWidth(null) + 40, 10);
        }
    }


}


public class ImageDisplayer extends JDialog {

    JPanel displayPanel;
    private Container cp;
    private ScrollPane scroller;
    JButton closeButton = new JButton();
    private JPanel buttonPanel;
    Image frontView, sideView, backView, areaView;
    private String title;
    BorderLayout borderLayout1 = new BorderLayout();


    public ImageDisplayer(JFrame parent) {
        super(parent, "ePostmanager Image Preview", true);
    }

    private void jbInit() throws Exception {

        cp = this.getContentPane();
        displayPanel = new DisplayPanel(this.frontView, this.sideView,
                                        this.backView, this.areaView);
        buttonPanel = new JPanel(new GridBagLayout());
        closeButton.setText("OK");
        closeButton.setVerticalAlignment(SwingConstants.BOTTOM);
        closeButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDisplay();
            }

        });

        cp.setLayout(borderLayout1);
        scroller = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroller.add(displayPanel);
        cp.add(scroller, java.awt.BorderLayout.CENTER);

        buttonPanel.add(closeButton,
                        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
        cp.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void closeDisplay() {

        this.frontView = null;
        this.sideView = null;
        this.backView = null;
        this.areaView = null;

        ((DisplayPanel) displayPanel).clearGraphics();
        dispose(); // Closes the dialog

    }

    public void showImage(Image frontView, Image sideView, Image BackView,
                          Image areaView) {

        this.frontView = frontView;
        this.sideView = sideView;
        this.backView = BackView;
        this.areaView = areaView;
        repaint();
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MediaTracker tracker = new MediaTracker(this);
        if (this.frontView != null) {
            tracker.addImage(this.frontView, 1);
        }
        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.sideView != null) {
            tracker.addImage(this.sideView, 2);
            //tracker.
        }

        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.backView != null) {
            tracker.addImage(this.backView, 3);
            //tracker.
        }

        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.areaView != null) {
            tracker.addImage(this.areaView, 4);
            //tracker.
        }

        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        packComponents();
        this.setVisible(true);
    }

    private void packComponents() {
        if (this.frontView != null) {
            setSize(470, this.frontView.getHeight(null) + 80);
        } else {
            setSize(470, this.sideView.getHeight(null) + 80);
        }
    }

}
