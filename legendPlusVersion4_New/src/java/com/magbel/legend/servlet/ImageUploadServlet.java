package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;



import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;



public class ImageUploadServlet extends HttpServlet
{


	private ApprovalRecords service;

 private String pName="";
 private String assetID="";
  private      String fName="";
    public ImageUploadServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
        service = new ApprovalRecords();

        }
        catch(Exception et)
        {et.printStackTrace();}
    }
    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
      

        String tagId = request.getParameter("assetId");
        String filePath = request.getParameter("file");
        System.out.println("tagId form data..."+tagId);
 //       System.out.println("filePath form data..."+filePath);
        
        File inFile = new File(filePath);

        
   	 System.out.println("Called Upload Image");
		
//		final String UPLOAD_FOLDER = "c:/uploadedFiles/";
		 
		Properties prop = new Properties();
     File file = new File("C:\\Property\\legendPlus.properties");
     FileInputStream input = new FileInputStream(file);
     prop.load(input);
	
     String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
		 
		 FileInputStream uploadedInputStream = null;
		
		try {
			System.out.println("The file detail is : " + inFile);
			uploadedInputStream = new FileInputStream(inFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.getMessage();
		}
		 
		
		
		
      // check if all form parameters are provided
     // if (uploadedInputStream == null || fileDetail == null)
      //System.out.println("Invalid form data...");
      
      // create our destination folder, if it not exists
      try {
          createFolderIfNotExists(UPLOAD_FOLDER);
      } catch (SecurityException se) {
     	 System.out.println("Can not create destination folder on server...");
      }
     // String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();  
      String uploadedFileLocation = UPLOAD_FOLDER + tagId + ".jpg";
      System.out.println("The Uploaded Location is : " + uploadedFileLocation);
//      try {
     	 if(uploadedFileLocation.endsWith(".jpg")) {
     		 saveToFile(uploadedInputStream, uploadedFileLocation);
     		 System.out.println("File saved to " +uploadedFileLocation + " Successfully..");
     	 }
//     	 else {
//     		 System.out.println("Can not save non jpg file...");
//	        	 data.put("status",503);
//		         data.put("message","Can not save non jpg file"); 
//     	 }
     
    
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

    
    public String getServletInfo()
    {
        return "Confirmation Action Servlet";
    }

//    private void captureData(HttpServletRequest request){
//        //System.out.println("@@@@@@@@@@@@@@@@in capture data method");
//    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//
//        //System.out.println("@@@@@@@@@@@the value of ismultipart is " + isMultipart);
//    if(isMultipart){
//// Create a factory for disk-based file items
//
//    try{
//    FileItemFactory factory = new DiskFileItemFactory();
//
//// Create a new file upload handler
//ServletFileUpload upload = new ServletFileUpload(factory);
//
//// Parse the request
//List  items = upload.parseRequest(request); /* FileItem */
//
//      //  System.out.println("@@@@@@@@@@@@@@the size of list is "+items.size());
//
//Iterator iter = items.iterator();
//
//while (iter.hasNext()) {
//    FileItem item = (FileItem) iter.next();
//    InputStream stream = item.getInputStream();
//
//    if(item.isFormField()){
//            //System.out.println("here 8888888888888888888888888");
//            String fieldname = item.getFieldName();
//           // System.out.println("the value of ^^^^^^^^^^^^^^^"+ item.getFieldName());
//        if(fieldname.equals("pageName")){
//           // System.out.println("@@@@@@@@@@@@@the value of field name "+ fieldname);
//              pName=Streams.asString(stream);//item.getName();//request.getParameter(fieldname);
//
//               // System.out.println("here999999999999999999999999999999999999 pName "+ pName  );
//        }
//        if(fieldname.equals("assetId")){
//            // System.out.println("@@@@@@@@@@@@@the value of field name "+ fieldname);
//            assetID=Streams.asString(stream);//item.getName();//request.getParameter(fieldname);
//              // System.out.println("here999999999999999999999999999999999999 assetID "+ assetID  );
//        }
//        
//        }
//
//        else{
//
//        //System.out.println("..................about to retrieve image 1");
//      if(item.getFieldName().equalsIgnoreCase("file")){
//          // System.out.println("..................about to retrieve image 2");
//
//          //fName=item.getName();
//       File fullfile = new File(item.getName());
//       
//         String fileLocation=fullfile.toString().substring(0,1)+":/image";
//       File fileLocationCheck = new File(fileLocation);
//       if(!fileLocationCheck.exists())fileLocationCheck.mkdir();
//      // System.out.println("--------------------------- "+ fileLocation  );
//       File savedFile = new File(fileLocation+"/",fullfile.getName());
//       
//       
//    //   File savedFile = new File(getServletContext().getRealPath("/"),fullfile.getName());
//       // File savedFile = new File(getServletContext().getRealPath(fullfile.getName()));
//        item.write(savedFile);
//
//      //  System.out.println("@@@@@@@@@@@@@@@@the full file name is "+fullfile);
//       // System.out.println("@@@@@@@@@@@@@the content of savedFile  is " + savedFile);
//          fName=savedFile.toString();
//         // System.out.println("@@@@@@@@@@@@@@@@@the fName is " + fName);
//      }
//     
//    }
//}
//
//
//    }
//    catch(FileUploadException fue){
//
//        System.out.println("error occured in ImageActionServlet: captureData() method" + fue);
//    }
//    catch(Exception e){
//
//        System.out.println("there is an error in ImageActionServlet: captureData() method" + e);
//    }
//
//
//
//
//}
//
//    }




    

}