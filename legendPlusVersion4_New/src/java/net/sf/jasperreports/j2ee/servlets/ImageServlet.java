package net.sf.jasperreports.j2ee.servlets;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRTypeSniffer;

public class ImageServlet extends BaseHttpServlet {
   private static final long serialVersionUID = 10200L;
   public static final String IMAGE_NAME_REQUEST_PARAMETER = "image";

   public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      byte[] imageData = null;
      String imageMimeType = null;
      String imageName = request.getParameter("image");
      if ("px".equals(imageName)) {
         try {
            JRRenderable pxRenderer = JRImageRenderer.getInstance("net/sf/jasperreports/engine/images/pixel.GIF");
            imageData = pxRenderer.getImageData();
            imageMimeType = "image/gif";
         } catch (JRException var11) {
            throw new ServletException(var11);
         }
      } else {
         List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
         if (jasperPrintList == null) {
            throw new ServletException("No JasperPrint documents found on the HTTP session.");
         }

         JRPrintImage image = JRHtmlExporter.getImage(jasperPrintList, imageName);
         JRRenderable renderer = image.getRenderer();
         if (((JRRenderable)renderer).getType() == 1) {
            renderer = new JRWrappingSvgRenderer((JRRenderable)renderer, new Dimension(image.getWidth(), image.getHeight()), ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null);
         }

         imageMimeType = JRTypeSniffer.getImageMimeType(((JRRenderable)renderer).getImageType());

         try {
            imageData = ((JRRenderable)renderer).getImageData();
         } catch (JRException var10) {
            throw new ServletException(var10);
         }
      }

      if (imageData != null && imageData.length > 0) {
         if (imageMimeType != null) {
            response.setHeader("Content-Type", imageMimeType);
         }

         response.setContentLength(imageData.length);
         ServletOutputStream ouputStream = response.getOutputStream();
         ouputStream.write(imageData, 0, imageData.length);
         ouputStream.flush();
         ouputStream.close();
      }

   }
}
