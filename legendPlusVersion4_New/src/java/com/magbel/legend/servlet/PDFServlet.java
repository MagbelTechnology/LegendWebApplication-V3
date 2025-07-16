package com.magbel.legend.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocumentException;
import com.magbel.legend.bus.Chap0609;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PDFServlet extends HttpServlet {
private Chap0609 chap;
private byte[] image;
ServletOutputStream stream = null;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		chap = new Chap0609();
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
		System.out.println("INFO::Entered ImageRendereDBServlet");
        String assetId = request.getParameter("asset_id");
        System.out.println("****************assetId*****************"+assetId);
        String pageName = request.getParameter("pageName");
        System.out.println("*************pageName********************"+pageName);
        System.out.println("about to load class");
        this.image = chap.getImageData(assetId,pageName);
        System.out.println(image);
        
		try{
			if(this.image != null){ 
				System.out.println("got image");
			response.setContentType("application/pdf"); 
			response.setHeader("Content-disposition", "attachment; filename=" + "Example.pdf" );
			//FileOutputStream fis = new FileOutputStream("c://test.pdf"); 
            //fis.write(image);
            stream = response.getOutputStream();
            stream.write(image);
            stream.close();
		  }
            else{

            //--- out.println("<script>alert('Image does not exist for this asset!')</script>");
            //--out.println("<script>");
//		out.println("window.location='DocumentHelp.jsp?np=assetDetails'");
		//--out.println("window.location='DocumentHelp.jsp?np=updateAsset&id="+assetId+"'");
	   // out.println("window.location='DocumentHelp.jsp'");
		//--out.println("</script>");


            }
		/*  test
			response.setContentType("application/pdf");  
			
			 if(this.image != null){
				 System.out.println("about to load byte image");
	        	 Document doc = new Document();
	             ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
	             PdfWriter docWriter = null;
	          
	               docWriter = PdfWriter.getInstance(doc, baosPDF);
	               baosPDF.write(image);
	               doc.close();
	               docWriter.close();
			       } 
	           
			*/
			
			
			/*reading loading image to pdf
			    Image jpg1 = Image.getInstance("E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war/devi.jpg");   
		        //float imgHeight = jpg1.getPlainHeight() + 100;   
		        //float imgWidth = jpg1.getPlainWidth() + 100;   
		    
		        Rectangle pageSize = new Rectangle(600, 700);   
		        Document document = new Document(pageSize);           
		    
		        response.setContentType("application/pdf");   
		        PdfWriter.getInstance(document, response.getOutputStream());   
		            
		        document.open();   
		        document.add(jpg1);   
		        document.close();   
		        */
			
			/*  generating table
			PdfWriter.getInstance(document, 
				response.getOutputStream()); // Code 2
			document.open();
			// Code 3
			PdfPTable table = new PdfPTable(2);
			table.addCell("1");
			table.addCell("2");
			table.addCell("3");
			table.addCell("4");
			table.addCell("5");
			table.addCell("6");		
			
			// Code 4
			document.add(table);		
			document.close(); 
			*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

    
}

