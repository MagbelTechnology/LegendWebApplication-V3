package magma.net.image;

// for the print event
//for the table to display ResultSet
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import magma.net.vao.AccidentImage;

public class AccidentSceneScanner extends JApplet {

//jarsigner -keystore "D:\j2sdk1.4.2_08\jre\lib\security\cacerts" LedgendScanner.jar ePostmanager
    JPanel centerPanel, controlPanel, titlePanel;
    Container borderPane;
    GridBagLayout gbl;
    GridBagConstraints gbc;

    JOptionPane jop;
    JButton btScan, btPreview, btSave;
    JLabel lbAssetId, lbReg, lbDriver, lbImage, lbNorthImage;
    JTextField txtAssetId, txtReg, txtDriver, txtImage;

    byte[] frontView, areaView, backView, sideView;
    Image imgFrontView, imgAreaView, imgBackView, imgSideView;
    String[] statusArray = {"FRONT VIEW", "AREA VIEW", "BACK VIEW", "SIDE VIEW"};
    JComboBox optionSelect;

    String bannerLocation = "resources/ledgend.jpg";
    String scanDLLLocation = "resources/AspriseJTwain.dll";
    String imageURL = "";
    String fileName;
    ImageDisplayer imagePreviewer;

    final String SAVING_SUCCESSFUL = "DONE SAVING IMAGE !\n" +
                                     "STATUS:Sucessful.";
    final String SAVING_DUPLICATED = "Error saving image !\n" +
                                     "STATUS : Already saved.";
    final String SAVING_ERROR = "Can not save image due to one or more\n" +
                                "of the following reasons :         \n " +
                                "1. Failed to connect to remote Database.\n " +
                                "2. Irrelivant data entered          \n" +
                                "3. System busy with another process.";
    private boolean alreadySaved;

    public void init() {
        try {
            //String javaHome = System.getProperty("java.home");
            URL codeBase = null; //this.getCodeBase();
            //URL source = new URL(codeBase + "resources/AspriseJTwain.dll");
            imageURL = (this.getClass().getResource(bannerLocation)).toString();
            URL source = this.getClass().getResource(scanDLLLocation);
            String fileDir = "C:\\scannerdll";
            File file = new File(fileDir);

            if (!(file.exists())) {
                installPolicyFile(source, fileDir + "\\AspriseJTwain.dll");
            }

        } catch (Exception error) {
            //getAppletContext().showStatus(error.getMessage());
        }
        /*
         * Uncomment for Standalone Frame application
         *For Frame Application
         *setTitle("Accident Scene Images");
         */

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        borderPane = this.getContentPane();
        borderPane.setLayout(new BorderLayout());
        borderPane.add(getTitlePanel(), BorderLayout.NORTH);
        borderPane.add(getCenterPanel(), BorderLayout.CENTER);
        borderPane.add(getControlPanel(), BorderLayout.SOUTH);
        /*
         * Uncomment for standalone Frame Application
         * setDefaultCloseOperation(EXIT_ON_CLOSE);
         * setSize(550, 500);
         * setVisible(true);
         */

    }

    private boolean isFrontView() {
        boolean selection = false;
        String option = (String) optionSelect.getSelectedItem();
        if (option.equalsIgnoreCase("FRONT VIEW")) {
            selection = true;
        }

        return selection;
    }

    private boolean isSideView() {
        boolean selection = false;
        String option = (String) optionSelect.getSelectedItem();
        if (option.equalsIgnoreCase("SIDE VIEW")) {
            selection = true;
        }

        return selection;

    }

    private boolean isBackView() {
        boolean selection = false;
        String option = (String) optionSelect.getSelectedItem();
        if (option.equalsIgnoreCase("BACK VIEW")) {
            selection = true;
        }

        return selection;

    }

    private boolean isAreaView() {
        boolean selection = false;
        String option = (String) optionSelect.getSelectedItem();
        if (option.equalsIgnoreCase("AREA VIEW")) {
            selection = true;
        }

        return selection;

    }

    /**
     * getTitlePanel
     *
     * @return JPanel
     */
    public JPanel getTitlePanel() {

        titlePanel = new JPanel();
        titlePanel.setBorder(new TitledBorder(new EtchedBorder(),
                                              "Accident Images", 1, 3, null, null));
        titlePanel.setLayout(new BorderLayout());
        lbNorthImage = new JLabel("", JLabel.CENTER);
        lbNorthImage.setIcon(new ImageIcon(imageURL));
        titlePanel.add(lbNorthImage);

        return titlePanel;

    }

    /**
     * getControlPanel
     *
     * @return JPanel
     */
    public JPanel getControlPanel() {

        controlPanel = new JPanel();

        controlPanel.setLayout(new GridLayout(1, 5, 0, 1));
        //controlPanel.add(new JLabel());

        btScan = new JButton("Scan");
        btPreview = new JButton("Preview");
        btSave = new JButton("Save");

        controlPanel.add(btScan);
        controlPanel.add(btPreview);
        controlPanel.add(btSave);

        btScan.addActionListener(new ButtonScanListener());
        btPreview.addActionListener(new ButtonPreviewListener());
        btSave.addActionListener(new ButtonSaveListener());

        return controlPanel;

    }

    /**
     * getCenterPanel
     *
     * @return JPanel
     */
    public JPanel getCenterPanel() {

        centerPanel = new JPanel();

        centerPanel.setLayout(gbl);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 1;
        gbc.gridy = 1;
        lbAssetId = new JLabel("Asset Id ");
        gbl.setConstraints(lbAssetId, gbc);
        centerPanel.add(lbAssetId);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.ipady = 10;
        txtAssetId = new JTextField(16);
        txtAssetId.setText(getParameter("ASSETID"));
        gbl.setConstraints(txtAssetId, gbc);
        centerPanel.add(txtAssetId);

        gbc.ipadx = 20;
        gbc.gridx = 1;
        gbc.gridy = 2;
        lbReg = new JLabel("Registration No");
        gbl.setConstraints(lbReg, gbc);
        centerPanel.add(lbReg);

        gbc.gridx = 2;
        gbc.gridy = 2;
        txtReg = new JTextField(16);
        gbl.setConstraints(txtReg, gbc);
        txtReg.setText(getParameter("REGNO"));
        centerPanel.add(txtReg);

        gbc.gridx = 1;
        gbc.gridy = 3;
        lbDriver = new JLabel("Driver Involved");
        gbl.setConstraints(lbDriver, gbc);
        centerPanel.add(lbDriver);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.ipady = 10;
        txtDriver = new JTextField(16);
        txtDriver.setText(getParameter("DRIVERID"));
        gbl.setConstraints(txtDriver, gbc);
        centerPanel.add(txtDriver);

        gbc.gridx = 1;
        gbc.gridy = 4;
        lbImage = new JLabel("Image Type");
        gbl.setConstraints(lbImage, gbc);
        centerPanel.add(lbImage);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.ipady = 10;
        optionSelect = new JComboBox(statusArray);
        gbl.setConstraints(optionSelect, gbc);
        centerPanel.add(optionSelect);

        return centerPanel;
    }

    public boolean isAlreadySaved() {
        return alreadySaved;
    }

    public void setAssetId(String id) {
        txtAssetId.setText(id);
    }

    public void setRegistrationNo(String no) {
        txtReg.setText(no);
    }

    public void setDriverIvolved(String name) {
        txtDriver.setText(name);
    }

    public void logComplaint(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setAlreadySaved(boolean alreadySaved) {
        this.alreadySaved = alreadySaved;
    }

    /**
     *  Method: installPolicyFile demonstrates the first step of the two-step application
     *  installation. It downloads the java.policy
     *  to the lib/security  subdirectory of the clients JRE.
     *  In order to get access to the java.home system property, this applet has
     *  to be signed.
     */

    public void installPolicyFile(URL sourceUrl, String destFileName) {

        File destFile = new File(destFileName);

        try {
            //getAppletContext().showStatus("installing file " + destFileName);
            destFile.getParentFile().mkdirs();
            URLConnection connection = sourceUrl.openConnection();
            InputStream is = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buff = new byte[1024];
            BufferedInputStream in = new BufferedInputStream(is, buff.length);
            BufferedOutputStream out = new BufferedOutputStream(fos,
                    buff.length);
            int i;
            int count = 0;
            while ((i = in.read(buff, 0, buff.length)) != -1) {
                out.write(buff, 0, i);
                count += i;
            }
            /*
             * Comment this for Standalone Frame application
             *getAppletContext().showStatus(count + " bytes copied ...");
             */
            in.close();
            out.close();

        } catch (Exception exception) {
            //exception.printStackTrace();
            /*
             *Comment for Standalone Apllication
             *getAppletContext().showStatus(exception.toString());
             */
        }

    }


    /**
     * retrieveImageData<br>
     * Convertes a JPEG image from a specified file<br>
     * path(pathname) to a a byte value.<br>
     *
     * @param pathname String
     * @return byte[]
     */
    private byte[] retrieveImageData(String pathname) {
        ArrayList imageArray = new ArrayList();
        File file;
        BufferedInputStream bis;
        byte[] imageData = null;

        file = new File(pathname);
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException fe) {
            System.out.println("INFO: <<SignatureCardDAO:retrieveImageData>>\n" +
                               " Image file does not Exist.");
            return null;
        }

        int data;
        try {
            while ((data = bis.read()) != -1) {
                imageArray.add(new Byte((byte) data));
            }

            imageData = new byte[imageArray.size()];
            for (int i = 0; i < imageData.length; i++) {
                imageData[i] = ((Byte) imageArray.get(i)).byteValue();
            }
        } catch (IOException ioe) {
            System.out.println(
                    "INFORMATION, <<SignatureCardDAO:retrieveImageData>> Could not retrieve image.");
            System.out.println(ioe);
        }

        return imageData;
    }

    private void acquireImageFromScanner() {
        BufferedImage bufferedImage = null;
        try {

            SourceManager.setLibraryPath(
                    "C:\\scannerdll\\AspriseJTwain.dll");
            Source source = SourceManager.instance().getDefaultSource();
            if (source == null) {
                reportMessage("No source has been selected!");
                return;
            } else {
                //statusLabel.setText("Accessing the scanner port...");
            }
            if (source.isUIEnabled()) {
                //source.setUIEnabled(true);
            }

            source.open();
            //Image image = source.acquireImage();
            //byte[] bytes = image.
            bufferedImage = source.acquireImageAsBufferedImage();

            /*
             *Save the image temporarily to a disk
             *Save it as a JPEG file
             */
            String randomName = Long.toString(System.currentTimeMillis());
            String fileDir = "C:\\ScannedImages";
            File file = new File(fileDir);

            if (!(file.exists())) {
                file.mkdirs();
            }

            fileName = file + "\\" + randomName + ".jpg";
            File tempFile = new File(fileName);
            ImageIO.write(bufferedImage, "jpg",
                          tempFile);

            if (isFrontView()) {
                frontView = retrieveImageData(fileName);
                boolean isDeleted = file.delete();
            } else if (isBackView()) {
                backView = retrieveImageData(fileName);
                boolean isDeleted = file.delete();
            } else if (isSideView()) {
                sideView = retrieveImageData(fileName);
                boolean isDeleted = file.delete();
            } else {
                areaView = retrieveImageData(fileName);
                boolean isDeleted = file.delete();
            }

            //statusLabel.setText("Done obtaining image.");
            Image acquiredImage = ((Image) bufferedImage);
            if (isFrontView()) {
                imgFrontView = acquiredImage;
            } else if (isSideView()) {
                imgSideView = acquiredImage;
            } else if (isBackView()) {
                imgBackView = acquiredImage;
            } else {
                imgAreaView = acquiredImage;
            }

        } catch (Exception e) {
            reportMessage("WARNING:System Failed to Open Scanner PORT");
            System.out.println(
                    "<< WARNING::Error occured while aquiring image >>\n" +
                    e.getMessage());
        } finally {
            SourceManager.closeSourceManager();
            imagePreviewer = new ImageDisplayer(null);
            imagePreviewer.showImage(imgFrontView, imgSideView, imgBackView,
                                     imgAreaView);

        }

    }

    public void reportMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "INFORMATION",
                                      JOptionPane.ERROR_MESSAGE);

    }

    class ButtonScanListener implements ActionListener {

        public void actionPerformed(ActionEvent buttonImageEvent) {
            acquireImageFromScanner();
        }
    }


    class ButtonPreviewListener implements ActionListener {

        public void actionPerformed(ActionEvent buttonCloseEvent) {
            if (fileName == null) {
                reportMessage("WARNING:No image scanned!");
            } else {
                Image selected = Toolkit.getDefaultToolkit().getImage(fileName);
                showScannedImage(selected);
            }
        }

        private void showScannedImage(Image selectedImage) {
            ImageDisplayer displayer = null;
            displayer = new ImageDisplayer(null);
            displayer.showImage(imgFrontView, imgSideView, imgBackView,
                                imgAreaView);
        }
    }


    public Connection getDBConnection() throws Exception {

        Connection dbCon = null;
        String dbUser = getParameter("DBUser");
        String dbPassword = getParameter("DBPassword");
        String serverAddress = getParameter("ServerAddress");

        /*
         *SQL 2000
         Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
         String dbURL = "jdbc:microsoft:sqlserver://" + serverAddress + ":1433";
         */
        /*
         * SQL 2005
         */
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String dbURL = "jdbc:sqlserver://" + serverAddress +
                       ":1433;database=FixedAsset";
        dbCon = DriverManager.getConnection(dbURL, dbUser, dbPassword);

        return dbCon;
    }

    public void closeConnection(Connection con, PreparedStatement ps) {

        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception ex) {
            System.out.println("INFO:Error closing connection ->" +
                               ex.getMessage());
        }
    }


    class ButtonSaveListener implements ActionListener {

        public void actionPerformed(ActionEvent btEvent) {
            String assetId = txtAssetId.getText();
            String registrationNo = txtReg.getText();
            String imageId = Long.toString(System.currentTimeMillis());
            AccidentImage accidentImage = new AccidentImage(assetId,
                    registrationNo, imageId, frontView, sideView, areaView,
                    backView);
            if (isAlreadySaved()) {
                logComplaint(SAVING_DUPLICATED);
            } else {
                saveImage(accidentImage);
            }
        }
    }


    public void saveImage(AccidentImage accidentImage) {
        Connection con = null;
        PreparedStatement ps = null;
        final String imageQuery = "INSERT INTO FT_ACCIDENT_IMAGE(" +
                                  "ASSET_ID,REGISTRATION_NO,IMAGE_ID,FRONT_VIEW," +
                                  "SIDE_VIEW,AREA_VIEW,BACK_VIEW) VALUES(?,?,?,?,?,?,?)";
        try {

            con = getDBConnection();
            ps = con.prepareStatement(imageQuery);

            ps.setString(1, accidentImage.getAssetId());
            ps.setString(2, accidentImage.getRegistrationNo());
            ps.setString(3, accidentImage.getImageId());
            ps.setBytes(4, accidentImage.getFrontView());
            ps.setBytes(5, accidentImage.getSideView());
            ps.setBytes(6, accidentImage.getAreaView());
            ps.setBytes(7, accidentImage.getBackView());

            ps.execute();
            logComplaint(SAVING_SUCCESSFUL);
            this.setAlreadySaved(true);

        } catch (Exception e) {
            logComplaint(SAVING_ERROR);
            reportMessage("WARN:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
    }

    public static void main(String[] args) {

        JApplet accidentInfoApplet = new AccidentSceneScanner();
        JFrame frame = new JFrame("Accident Images");
        frame.getContentPane().add(accidentInfoApplet);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        accidentInfoApplet.init();
        accidentInfoApplet.start();

        frame.setVisible(true);
    }

}
