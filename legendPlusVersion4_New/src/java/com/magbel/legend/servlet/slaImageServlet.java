package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class slaImageServlet {
	
	public String value="";
	
	public boolean imageInsertion(HttpServletRequest request, HttpServletResponse response, String slaID) throws IOException {
		
		boolean resp = false;
		String status = "";
		File newFile = null;
       
        if(!ServletFileUpload.isMultipartContent(request))
        {
            response.getWriter().println("Does not support!");
           
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
//        factory.setSizeThreshold(0x300000);
//        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
       ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(0xa00000L);
        upload.setSizeMax(0x3200000L);
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
        System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
        String uploadPath = UPLOAD_FOLDER;
        
        //String previousPage = request.getParameter("previousPage");
//        String assetId = request.getParameter("assetId");
//        System.out.println("=====>>>>uploadPath in slaImageServlet: "+uploadPath);
        
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
        {
            uploadDir.mkdir();
        }
        try
        {
            List formItems = upload.parseRequest(request);
//            System.out.println("formItems.iterator() =>: " + formItems.iterator());  
            for(Iterator iter = formItems.iterator(); iter.hasNext();)
            {
//            	System.out.println(" =========iter.hasNext(): "+ iter.hasNext());
                FileItem item = (FileItem)iter.next();
                
               
              
               if(item.isFormField())
                {
              
                		 String fieldName = item.getFieldName();
                         if(fieldName.equals("Status")) {
                        	 value=item.getString();
//                             System.out.println("st is =>: " + value);                        
                         }     
                }else  {
                	
                	 String fieldName = item.getFieldName();
                	 if(fieldName.equals("file")) {
                          String fileName = (new File(item.getName())).getName();
//			System.out.println("The file name is =>: " + fileName);
						                    
			String filePath = uploadPath + "SLA" + fileName.toString();
//			System.out.println("The file path is ==: " + filePath);
						                    
			String newPath = uploadPath + "SLA" + slaID +".pdf";
//			System.out.println("The new file path is >=: " + newPath);
                         
                        
                         File oldFile = new File(filePath);
                         
                          newFile = new File(newPath);
                    
                         item.write(newFile);
                     } 
                }
             
               //item.write(newFilePath);
            }
//            String path = slaID + ".pdf";
//            System.out.println("Path is : " +path);
            Path yourFile = Paths.get(newFile.toString());
//            System.out.println("Your File Path is : " + yourFile);
            String path = "SLA" + slaID + ".pdf";
//            System.out.println("Path is : " +path);
            Files.move(yourFile, yourFile.resolveSibling(path));
//            System.out.println("Upload has been done successfully!");
           status = newFile.toString();
           if(status != null) {
        	   resp=true;
           }
            
        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("There was an error: ")).append(ex.getMessage()).toString());
            ex.printStackTrace();
        }
		
		
		
		return resp;
		
	}

}
