package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Properties;
 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;



@WebServlet(name = "ImageUploadServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
) 
  
public class LegendImageUploadServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 System.out.println("Called Upload Image");
    	 
    	 String tagId = request.getParameter("tagId");
    	 
    	 Part filePart = request.getPart("file");
         
         System.out.println("The tag id is : " + tagId);
         System.out.println("The file part is : " + filePart);
    		
    	 String inFile = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
         InputStream fileContent = filePart.getInputStream();
         
        

         File fileDetail = new File(inFile);

         System.out.println("The file detail is : " + fileDetail);
         
         if(fileDetail.length() > 0) {

         Properties prop = new Properties();
         File file = new File("C:\\Property\\legendPlus.properties");
         FileInputStream input = new FileInputStream(file);
         prop.load(input);

         String UPLOAD_FOLDER = prop.getProperty("imagesUrl");


         // create our destination folder, if it not exists
         try {
             createFolderIfNotExists(UPLOAD_FOLDER);
         } catch (SecurityException se) {
             System.out.println("Can not create destination folder on server...");
         }
         // String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
         String uploadedFileLocation = UPLOAD_FOLDER + tagId + ".jpg";
         System.out.println("The Uploaded Location is : " + uploadedFileLocation);
//    	         try {
         if(uploadedFileLocation.endsWith(".jpg")) {
             saveToFile(fileContent, uploadedFileLocation);
             System.out.println("File saved to " +uploadedFileLocation + " Successfully..");
             response.sendRedirect("ImageUpload.jsp");
         }
         }else {
        	 PrintWriter printWriter = response.getWriter();
 	    	//   printWriter.println("<script>alert('No File Available!')</script>");
 	    	  // printWriter.println("<script>window.open='alert('No File Available!')'</script>");
 	    	   printWriter.print("<script>alert('Incorrect File Size!');</script>");
 	    	   printWriter.print("<script>history.go(-1);</script>");
         }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    


    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }


    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

}
