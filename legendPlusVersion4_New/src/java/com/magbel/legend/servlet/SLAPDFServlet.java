package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileViewer
 */
@WebServlet("/SLAPDFServlet")
public class SLAPDFServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SLAPDFServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		//String value = request.getParameter("viewImage");
		String slaId = request.getParameter("slaId");
		if(slaId != null) {
//		String fileName = slaId;
			slaId =  "SLA"+slaId;
		System.out.println("slaId: "+slaId);
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
        String UPLOAD_FOLDER = prop.getProperty("imagesUrl");
        System.out.println((new StringBuilder("The url is : ")).append(UPLOAD_FOLDER).toString());
        String uploadPath = UPLOAD_FOLDER + slaId + ".pdf";
        System.out.println("=====>>>>Upload Path: "+uploadPath);
		
		response.setContentType("application/pdf");
		//String filepath = "/home/jsp.pdf";
		//response.setHeader("Content-Disposition", "inline; filename=’jsp.pdf’");
		response.setHeader("Content-Disposition","inline;filename=\"" + slaId + ".pdf" + "\"");   
		try (FileInputStream in = new FileInputStream(uploadPath)) {
		    int content;
		    while ((content = in.read()) != -1) {
		        out.write(content);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
