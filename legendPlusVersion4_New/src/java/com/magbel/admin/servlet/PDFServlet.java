package com.magbel.admin.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocumentException;
import com.magbel.admin.bus.BinaryHolder;
import com.magbel.admin.bus.Chap0609;
import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.admin.handlers.ApprovalRecords;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PDFServlet extends HttpServlet {
private Chap0609 chap;
private String fileType;
private byte[] image;
ApprovalRecords aprecords = null;
ServletOutputStream stream = null;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		chap = new Chap0609();
		MagmaDBConnection mgDbCon = null;
	}
	
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) 
			throws ServletException, IOException{
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) 
			throws ServletException, IOException{
		 // PrintWriter out = response.getWriter();
		//response.setContentType("application/pdf"); // Code 1
		//Document document = new Document();
		aprecords = new ApprovalRecords();
		System.out.println("INFO::Entered ImageRendereDBServlet");
        String reqnId = request.getParameter("reqnId");
        System.out.println("****************reqnId*****************"+reqnId);
        String pageName = request.getParameter("pageName");
        System.out.println("*************pageName********************"+pageName);
        System.out.println("about to load class");
        this.image = chap.getImageData(reqnId,pageName);
        fileType = aprecords.getImageFileType(reqnId,pageName);
        //System.out.println(image);
        System.out.println("<<<<<fileType>>>>>> "+fileType);
               
		try{  
			if(this.image != null){ 
				System.out.println("got image");
		    	if (fileType.trim().equalsIgnoreCase("txt"))
		    	{
		    	response.setContentType( "text/plain" );
		    	response.setHeader("Content-disposition", "attachment; filename=" + "Example.txt" );
		    	}
		    	else if (fileType.trim().equalsIgnoreCase("doc"))
		    	{	
		    		response.setContentType("application/doc"); 
		    		response.setHeader("Content-disposition", "attachment; filename=" + "Example.doc" );
		    	}
		    	else if (fileType.trim().equalsIgnoreCase("xls"))
		    	{
		    	response.setContentType( "application/vnd.ms-excel" );
		    	response.setHeader("Content-disposition", "attachment; filename=" + "Example.xls" );
		    	}
		    	else if (fileType.trim().equalsIgnoreCase("pdf"))
		    	{
		    	response.setContentType( "application/pdf" );
		    	response.setHeader("Content-disposition", "attachment; filename=" + "Example.pdf" );
		    	}
			
            stream = response.getOutputStream();
            stream.write(image);
            stream.close();		
		  }
            else{
            	
  
            }
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

    
}

