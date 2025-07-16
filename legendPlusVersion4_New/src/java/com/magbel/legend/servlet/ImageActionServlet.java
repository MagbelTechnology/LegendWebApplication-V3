package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;


import java.io.*;



import java.util.Iterator;
import java.util.List;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;



public class ImageActionServlet extends HttpServlet
{


	private ApprovalRecords service;

 private String pName="";
 private String assetID="";
  private      String fName="";
    public ImageActionServlet()
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
        /*
        String assetId = request.getParameter("assetId");
        System.out.println("@@@@@@@@@@@@@@@@@ assetId "+assetId);
        String pageName = request.getParameter("pageName");
       System.out.println("@@@@@@@@@@@@@@@@ pageName "+pageName);
       */
       String userId="";
        String branch = "";

        String file = request.getParameter("file");
        
        legend.admin.objects.User user = null;
   	 if(session.getAttribute("_user")!=null)
   	 {
   		 user =(legend.admin.objects.User)session.getAttribute("_user");
   	     userId=user.getUserId();
   	     branch=user.getBranch();

   	 }
   	 //check image not existing
        captureData(request);

        //System.out.println("@@@@@@@@@@@@@the asset id is " +assetID);
        //System.out.println("@@@@@@@@@@@@@the pageName is " +pName);
        //System.out.println("@@@@@@@@@@@@@the fName is " +fName);
        if(!service.isImageExisting(assetID, pName))
        {
       // boolean output = service.insertBlob(assetId, userId, pageName, file);
            
            boolean output = service.insertBlob(assetID, userId, pName, fName);
        //System.out.println("Stauts of image "+output);
        if(output==true)
        {
           //  FileOutputStream to= new FileOutputStream(fName);
         //    to.flush();
        //     to.close();
         //   FilePermission pm = new FilePermission(fName,"delete");
         ///   File filed = new File("E:/jboss-4.0.5.GA/server/testserver/deploy/legend2.net.war/0001594.pdf");
         //   System.out.println(filed.canRead());
        //    System.out.println(filed.canWrite());

        //    System.out.println(filed.delete());
        //    System.out.println(filed.exists());
            //System.out.println(pm.getActions());
         //String command = "cmd /C start C:/test.bat ";
         //System.out.println("del "+fName);
        // Runtime rt = Runtime.getRuntime();
       //  Process pr = rt.exec(command);
        out.println("<script>alert('Image successfully saved!')</script>");
		out.println("<script>");
//		out.println("window.location='DocumentHelp.jsp?np=assetDetails'");
		out.println("window.location='DocumentHelp.jsp?np=updateAssetView&id="+assetID+"'");

	    //out.println("window.location='DocumentHelp.jsp?np=updateAsset&id='"+assetId+"';'");
		out.println("</script>");
        }
        else
        {
        out.println("<script>alert('Image not saved!')</script>");
		out.println("<script>");
//		out.println("window.location='DocumentHelp.jsp?np=assetDetails'");
		out.println("window.location='DocumentHelp.jsp?np=updateAssetView&id="+assetID+"'");
	   // out.println("window.location='DocumentHelp.jsp'");
		out.println("</script>");
		}
        }
        else{out.println("<script>alert('Image already exist!')</script>");
		out.println("<script>");
//		out.println("window.location='DocumentHelp.jsp?np=assetDetails'");
		out.println("window.location='DocumentHelp.jsp?np=updateAssetView&id="+assetID+"'");
	   // out.println("window.location='DocumentHelp.jsp'");
		out.println("</script>");}
    }
    public String getServletInfo()
    {
        return "Confirmation Action Servlet";
    }

    private void captureData(HttpServletRequest request){
        //System.out.println("@@@@@@@@@@@@@@@@in capture data method");
    boolean isMultipart = ServletFileUpload.isMultipartContent((javax.servlet.http.HttpServletRequest) request);

        //System.out.println("@@@@@@@@@@@the value of ismultipart is " + isMultipart);
    if(isMultipart){
// Create a factory for disk-based file items

    try{
    FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

// Parse the request
List  items = upload.parseRequest((javax.servlet.http.HttpServletRequest) request); /* FileItem */

      //  System.out.println("@@@@@@@@@@@@@@the size of list is "+items.size());

Iterator iter = items.iterator();

while (iter.hasNext()) {
    FileItem item = (FileItem) iter.next();
    InputStream stream = item.getInputStream();

    if(item.isFormField()){
            //System.out.println("here 8888888888888888888888888");
            String fieldname = item.getFieldName();
           // System.out.println("the value of ^^^^^^^^^^^^^^^"+ item.getFieldName());
        if(fieldname.equals("pageName")){
           // System.out.println("@@@@@@@@@@@@@the value of field name "+ fieldname);
              pName=Streams.asString(stream);//item.getName();//request.getParameter(fieldname);

               // System.out.println("here999999999999999999999999999999999999 pName "+ pName  );
        }
        if(fieldname.equals("assetId")){
            // System.out.println("@@@@@@@@@@@@@the value of field name "+ fieldname);
            assetID=Streams.asString(stream);//item.getName();//request.getParameter(fieldname);
              // System.out.println("here999999999999999999999999999999999999 assetID "+ assetID  );
        }
        
        }

        else{

        //System.out.println("..................about to retrieve image 1");
      if(item.getFieldName().equalsIgnoreCase("file")){
          // System.out.println("..................about to retrieve image 2");

          //fName=item.getName();
       File fullfile = new File(item.getName());
       
         String fileLocation=fullfile.toString().substring(0,1)+":/image";
       File fileLocationCheck = new File(fileLocation);
       if(!fileLocationCheck.exists())fileLocationCheck.mkdir();
      // System.out.println("--------------------------- "+ fileLocation  );
       File savedFile = new File(fileLocation+"/",fullfile.getName());
       
       
    //   File savedFile = new File(getServletContext().getRealPath("/"),fullfile.getName());
       // File savedFile = new File(getServletContext().getRealPath(fullfile.getName()));
        item.write(savedFile);

      //  System.out.println("@@@@@@@@@@@@@@@@the full file name is "+fullfile);
       // System.out.println("@@@@@@@@@@@@@the content of savedFile  is " + savedFile);
          fName=savedFile.toString();
         // System.out.println("@@@@@@@@@@@@@@@@@the fName is " + fName);
      }
     
    }
}


    }
    catch(FileUploadException fue){

        System.out.println("error occured in ImageActionServlet: captureData() method" + fue);
    }
    catch(Exception e){

        System.out.println("there is an error in ImageActionServlet: captureData() method" + e);
    }




}

    }




    

}