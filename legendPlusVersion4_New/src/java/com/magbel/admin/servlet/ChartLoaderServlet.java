/**
 *
 * Need to produce some chart prior to this action call in a Java bean
 * Need a session attribute named "chartImage";
 *
 */


package com.magbel.admin.servlet;

import java.io.*;
import java.awt.image.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.JFreeChart;

import com.keypoint.PngEncoder;
//import com.keypoint.JPEGImageEncoder;
//import com.keypoint.JPEGEncodeParam;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.DefaultFontMapper;

public class ChartLoaderServlet extends HttpServlet {

	String title;
	String author;

  public void init() throws ServletException {

	  this.title = getInitParameter("title");
      this.author = getInitParameter("author");

  }

  //Process the HTTP Get request
  public void service(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {

   // get the chart from session
   String extension = request.getParameter("ext");
   String type = request.getParameter("t");
   response.setHeader("Pragma", "no-cache");
   response.setHeader("Cache-Control", "no-cache");
   response.setDateHeader("Expires", -1L);

   if(extension == null || extension.equalsIgnoreCase("png")){
	   doPngGet(request,response,type);
   }else if(extension.equalsIgnoreCase("jpg")){
	  doJPegGet(request,response,type);
   }else{
	   doPdfGet(request,response,type);
   }
}

public void doPngGet(HttpServletRequest request, HttpServletResponse response,String type)
         throws ServletException, IOException {

   HttpSession session = request.getSession(false);
   BufferedImage chartImage = (BufferedImage) session.getAttribute("chartImage"+type);

   // set the content type so the browser can see this as a picture
   response.setContentType("image/png");

   // send the picture
   PngEncoder encoder = new PngEncoder(chartImage, false, 0, 9);
   response.getOutputStream().write(encoder.pngEncode());
}

public void doJPegGet(HttpServletRequest request, HttpServletResponse response,String type)
         throws ServletException, IOException {

	 // get the chart from storage
	 HttpSession session = request.getSession(false);
     JFreeChart  chart = (JFreeChart) session.getAttribute("chart"+type);
	 response.setContentType( "image/jpeg" );
	 /*
	 // send the picture
	 BufferedImage buf = chart.createBufferedImage(640, 400, null);
	 JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( response.getOutputStream() );
	 JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam( buf );
	 param.setQuality( 0.75f, true );
     encoder.encode( buf, param );
     */
}

public void doPdfGet(HttpServletRequest request, HttpServletResponse response,String type)
         throws ServletException, IOException {

	HttpSession session = request.getSession(false);
	JFreeChart  chart = (JFreeChart) session.getAttribute("chart"+type);
   // set the content type so the browser can see this as it is
   response.setContentType( "application/pdf" );

   int width = 640;
   int height = 480;
   com.lowagie.text.Rectangle pagesize = new com.lowagie.text.Rectangle( width, height );
   Document document = new Document( pagesize, 50F, 50F, 50F, 50F );
   OutputStream out = response.getOutputStream();
   OutputStream os = new BufferedOutputStream( out );
   try{

	   PdfWriter writer = PdfWriter.getInstance( document, out );
	   document.addAuthor(this.author);
	   document.addSubject(this.title);
	   document.open();
	   PdfContentByte cb = writer.getDirectContent();
	   PdfTemplate tp = cb.createTemplate( width, height );
	   Graphics2D g2 = tp.createGraphics( width, height, new DefaultFontMapper() );
	   Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height );
	   chart.draw(g2, r2D);
	   g2.dispose();
	   cb.addTemplate(tp, 0, 0);
	   document.close();

	}catch(Exception e){
		System.out.println("error generating chart pdf :"+e);
	}
}

  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
    doGet(request, response);
  }

  //Process the HTTP Put request
  public void doPut(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
  }

  //Clean up resources
  public void destroy() {
  }

}