package com.magbel.legend.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
public class UploadFileValidation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY = "E:\\Export Files";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFileValidation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
		
		try {
            
        	Part filePart = request.getPart("file");
        	 for (Part part : request.getParts()) {
    	    	 String name = filePart.getSubmittedFileName();
                    System.out.println("The name is : " + name);
                    if(name.endsWith(".xlsx") || name.endsWith(".xls")) {
                    	 System.out.println(name.endsWith(".xls"));
                    	 String fileName = UPLOAD_DIRECTORY + File.separator + name;
                   	 part.write(fileName);
                   	 request.setAttribute("message", "File Uploaded Successfully");
                   	 request.getRequestDispatcher("/AssetReconciliationUpload.jsp").forward(request, response);
                    }else {
                    	 System.out.println("File must be an excel file");
                   	  request.setAttribute("message", "File must be an excel file...");
                   	  request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
                    }
                    //item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                }
            
       
           //File uploaded successfully
          // request.setAttribute("message", "File Uploaded Successfully");
           //response.sendRedirect("/index.jsp");
           //request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception ex) {
           request.setAttribute("message", "File Upload Failed due to " + ex);
           //response.sendRedirect("/index.jsp");
        }          

		  
//		if(ServletFileUpload.isMultipartContent(request)){
//            try {
//                List<FileItem> multiparts = new ServletFileUpload(
//                                         new DiskFileItemFactory()).parseRequest(request);
//              
//                for(FileItem item : multiparts){
//                    if(!item.isFormField()){
//                        String name = new File(item.getName()).getName();
//                        System.out.println("The name is : " + name);
//                        if(name.endsWith(".xlsx") || name.endsWith(".xls")) {
//                        	 System.out.println(name.endsWith(".xls"));
//                       	 item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
//                       	 request.setAttribute("message", "File Uploaded Successfully");
//                       	 request.getRequestDispatcher("/AssetReconciliationUpload.jsp").forward(request, response);
//                        }else {
//                        	 System.out.println("File must be an excel file");
//                       	  request.setAttribute("message", "File must be an excel file...");
//                       	  request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
//                        }
//                        //item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
//                    }
//                }
//           
//                     } catch (Exception ex) {
//               request.setAttribute("message", "File Upload Failed due to " + ex);
//               //response.sendRedirect("/index.jsp");
//            }          
//         
//        }else{
//            request.setAttribute("message",
//                                 "Sorry this Servlet only handles file upload request");
//        }
//    
        
	}

}
