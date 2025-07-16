package com.magbel.legend.servlet;



import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Properties;
public class LegendImageServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    			doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
{
	String Code = request.getParameter("barCode");

        response.setContentType("image/jpeg");
        ServletOutputStream out;
        out = response.getOutputStream();   
        File file1;
        File file = new File("C:\\Property\\LegendPlus.properties");
        String filePath;
        FileInputStream fin;
        BufferedInputStream bin;
        BufferedOutputStream bout;
        Properties prop = new Properties();

        FileInputStream input = new FileInputStream(file);
        prop.load(input);
        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
        createFolderIfNotExists(UPLOAD_FOLDER);
        filePath = UPLOAD_FOLDER + Code + ".jpg";
        System.out.println("The file path is  : " + filePath);
        file1 = new File(filePath);
        if (!file1.exists()) {
            System.out.println("The file is not available!!!");
	    response.sendRedirect("imageError.jsp");
            out.close();
        }else{

            fin = new FileInputStream(filePath);
            bin = new BufferedInputStream(fin);
            bout = new BufferedOutputStream(out);
            int ch = 0;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
            bin.close();
            fin.close();
            bout.close();
            out.close();
        System.out.println("The file is not available!!!");
	}
    }
    
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}
