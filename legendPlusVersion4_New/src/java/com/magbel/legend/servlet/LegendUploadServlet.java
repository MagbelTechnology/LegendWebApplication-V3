package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


@MultipartConfig(
		  fileSizeThreshold = 0x300000, 
		  maxFileSize = 0xa00000,    
		  maxRequestSize = 0x3200000
		)
public class LegendUploadServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    public LegendUploadServlet()
    {
    } 

   
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
    	   PrintWriter out = response.getWriter();
           Properties prop = new Properties();
           Part filePart = request.getPart("file");
    	
        	String assetId = request.getParameter("assetId");
        	String assetCode = request.getParameter("assetCode");
        	String tran_status = request.getParameter("tran_status");
        	String tran_type = request.getParameter("tran_type");
        	String destination = request.getParameter("destination");
        	
        	System.out.println("The Asset Code is ==: " + assetCode);
        	System.out.println("The destination is ==: " + destination);
        	
       	 	String fileName = filePart.getSubmittedFileName();
//          System.out.println("The file name is =>: " + fileName);
       	 	
         
            File file = new File("C:\\Property\\LegendPlus.properties");
            FileInputStream input = new FileInputStream(file);
            prop.load(input);
            String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
           // System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
            String uploadPath = UPLOAD_FOLDER;
            
            String previousPage = request.getParameter("previousPage");
            String currentPage = request.getParameter("currentPage"); 
//            String assetId = request.getParameter("assetId"); 
 //           System.out.println("=====>>>>assetId in LegendUploadServlet: "+assetId+"     previousPage: "+previousPage);
            
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists())
            {
                uploadDir.mkdir();
            }
            try
            {
            	 if(!fileName.endsWith(".php") || !fileName.endsWith(".sql")) {
            	 for (Part part : request.getParts()) {
                        
                        String ext = FilenameUtils.getExtension(fileName);
                    //    System.out.println("The extension is =>: " + ext);
                        
                        String filePath = uploadPath + "W" + fileName.toString();
                       // System.out.println("The file path is ==: " + filePath);
                        
                        String newPath = uploadPath + "W" + assetCode + "." + ext;
//                        System.out.println("The new file path is >=: " + newPath);
                        
                       
                        File oldFile = new File(filePath);
                        
                        File newFile = new File(newPath);
                        
                        String newFileName = newFile.getName().substring(0, newFile.getName().lastIndexOf("."));
                        
                      //  System.out.println("newFileName >=: " + newFileName);
                        
                        File folder = new File(UPLOAD_FOLDER);
            	        File[] listOfFiles = folder.listFiles();
            	        
            	       String listFiles = Arrays.toString(listOfFiles);
            	       	       
//            	       for(int a=0; a<listOfFiles.length; a++) {
//            	    	   String value = String.valueOf(listOfFiles[a]);
//            	    	   File extType = new File(value);
//            	    	   if(extType.getName().contains(newFileName+".xls") || extType.getName().contains(newFileName+".xlsx")) {
//                  	    	   	extType.delete();
//                              	System.out.println(extType.getName() + " deleted");
//               	    	   }
//
//            	       }
                        
                        oldFile.renameTo(newFile);

                        part.write(newPath);
                        
                        //System.out.println("uploaded successfully.");
                        
                        //response.sendRedirect("upload.jsp");

        	     // part.write("C:\\upload\\" + fileName);
        	    }
            	 
                System.out.println("The destination is ==: " + destination);
                System.out.println("Upload has been done successfully!");
//                if(previousPage.equals("newAsset")) {
                out.println("<script>alert('Upload has been done successfully!')</script>");
                out.print("<script>window.location='"+destination+"&id="+assetId +"&tran_status="+tran_status+"&tran_type="+tran_type+"&assetId="+assetId+"&reason=&pPage="+previousPage+"'</script>");
//                }else if(previousPage.equals("uploadAssetDetail")) {
//                	 out.println("<script>alert('Upload has been done successfully!')</script>");
//                     out.print("<script>window.location='DocumentHelp.jsp?np=uploadAssetDetail'</script>");
//                }else if(previousPage.equals("updateAssetView")) {
//               	 out.println("<script>alert('Upload has been done successfully!')</script>");
//                 out.print("<script>window.location='DocumentHelp.jsp?np=uploadAssetDetail'</script>");
//            }
                
                }else {
                	out.println("<script type=\"text/javascript\">");
 				   out.println("alert('Incorrect File..');");
 				// out.println("location='DocumentHelp.jsp?np=groupExcelImproveUpload';");
 				   out.println("</script>");
                }
            }
            catch(Exception ex)
            {
                System.out.println((new StringBuilder("There was an error: ")).append(ex.getMessage()).toString());
                out.println((new StringBuilder("<script>alert('There was an error: ")).append(ex.getMessage()).append(" ')</script>").toString());
                ex.printStackTrace();
            }
        }


    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
    }
}
