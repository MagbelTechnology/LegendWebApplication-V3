package com.magbel.legend.servlet;



import com.magbel.legend.bus.ApprovalRecords;


import java.io.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import magma.net.dao.MagmaDBConnection;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.magbel.util.DatetimeFormat;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.CurrentDateTime;

public class ImageGroupActionServlet extends HttpServlet
{


	private ApprovalRecords service;

 private String pName="";
 private String grpId="";
 private String fName="";
 private int pgNum = 0;
 private String urlLink="";
 private String urlLink2="";
 private MagmaDBConnection dbConnection;
    public ImageGroupActionServlet()
    {
    	dbConnection = new MagmaDBConnection();
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
              
        String userId="";
        
        String branch = "";

        String file = request.getParameter("file");
        
        legend.admin.objects.User user = null;
   	 if(session.getAttribute("_user")!=null)
   	 {
   		 user =(legend.admin.objects.User)session.getAttribute("_user");
   	     userId=user.getUserId();
   	  System.out.println(" userId ::::: "+userId);
   	     branch=user.getBranch();
   	  System.out.println(" branch ::::: "+branch);

   	 }
   	 //check image not existing
        //captureData(request);
        
       
        
        if(!isGroupImageExisting(grpId, pName))
        {
       // boolean output = service.insertBlob(assetId, userId, pageName, file);
            
            boolean output = insertBlob(grpId, userId, pName, fName);
            if ((pgNum==1)&&(output==true))
            {
            	urlLink = "window.location='DocumentHelp.jsp?np=groupAssetUpdate&image=y&id="+grpId+"'";
            }
            else if ((pgNum==2)&&(output==true))
            {
            	urlLink = "window.location='DocumentHelp.jsp?np=groupAssetRepost&image=y&id="+grpId+"'";
            }
            else if ((pgNum==1)&&(output==false))
            {
            	urlLink2= "window.location='DocumentHelp.jsp?np=groupAssetUpdate&image=n&id="+grpId+"'";
            }
            else if ((pgNum==2)&&(output==false))
            {
            	urlLink2= "window.location='DocumentHelp.jsp?np=groupAssetRepost&image=n&id="+grpId+"'";;
            }	
	        if(output==true)
	        {
		        out.println("<script>alert('Image successfully saved!')</script>");
				out.println("<script>");
				out.println(urlLink);
				out.println("</script>");
	        }
	        else
	        {
		        out.println("<script>alert('Image not saved!')</script>");
				out.println("<script>");
				out.println(urlLink2);
				out.println("</script>");
			}
        }
        else
        {
        	out.println("<script>alert('Image already exist!')</script>");
        	out.println("<script>");
        	out.println(urlLink2);
        	out.println("</script>");}
    	}
    
    public String getServletInfo()
    {
        return "Confirmation For Group Action Servlet";
    }

//    private void captureData(HttpServletRequest request)
//    { 
//    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//
//        if(isMultipart)
//        {
//        	try
//        	{
//        		FileItemFactory factory = new DiskFileItemFactory();
//
//        		ServletFileUpload upload = new ServletFileUpload(factory);
//
//        		List  items = upload.parseRequest(request); /* FileItem */
//
//        		System.out.println("The size of list is "+items.size());
//
//        		Iterator iter = items.iterator();
//
//        		while (iter.hasNext()) 
//        		{
//			    FileItem item = (FileItem) iter.next();
//			    
//			    InputStream stream = item.getInputStream();
//
//			    if(item.isFormField())
//			    {
//                 String fieldname = item.getFieldName();
//                 
//                 System.out.println("The value of fieldname : "+ item.getFieldName());
//        
//                 if(fieldname.equals("pageName"))
//		         {
//		          pName=Streams.asString(stream);
//		          
//		          System.out.println(" PName : "+ pName  );
//		         }
//                 if(fieldname.equals("grpId"))
//                 {
//                   	grpId=Streams.asString(stream);
//                   	
//                    System.out.println(" GroupID >>>>>  "+ grpId  );
//                 }
//                 if(fieldname.equals("pageNo"))
//                 {
//                	 pgNum=Integer.parseInt(Streams.asString(stream));
//                   	
//                    System.out.println(" PageNumber >>>>>  "+ pgNum  );
//                 }
//        }
//
//        else{
//
//        //System.out.println("..................about to retrieve image 1");
//      if(item.getFieldName().equalsIgnoreCase("file")){
//          // System.out.println("..................about to retrieve image 2");
//
//          //fName=item.getName();
//       File fullfile = new File(item.getName());
//       File savedFile = new File(getServletContext().getRealPath("/"),fullfile.getName());
//       // File savedFile = new File(getServletContext().getRealPath(fullfile.getName()));
//        item.write(savedFile);
//      //  System.out.println("@@@@@@@@@@@@@@@@the full file name is "+fullfile);
//       // System.out.println("@@@@@@@@@@@@@the content of savedFile  is " + savedFile);
//          fName=savedFile.toString();
//          System.out.println("FName >>>>>>>>>>  " + fName);
//      }
//     
//    }
//}
//
//
//    }
//    catch(FileUploadException fue){
//
//        System.out.println("error occured in ImageGroupActionServlet: captureData() method" + fue);
//    }
//    catch(Exception e){
//
//        System.out.println("there is an error in ImageGroupActionServlet: captureData() method" + e);
//    }
//}
//
//}

    public boolean insertBlob(String grpId,String userId ,String pageName, String fileName)
	{
	   boolean done=true;
		Connection conn = null;
		try {
			conn = dbConnection.getConnection("legendPlus");
			if (!fileName.equals(""))
			    {
				FileInputStream fis = new FileInputStream(fileName);
				PreparedStatement ps = conn.prepareStatement("INSERT INTO am_group_image VALUES(?,?,?,?,?)");
				ps.setString(1,userId);
				ps.setDate(2,dbConnection.dateConvert(new java.util.Date()));
				ps.setBinaryStream(3, fis, fis.available());
				ps.setString(4,pageName);
				ps.setString(5,grpId);
				ps.execute();
				ps.close();

				}
			 conn.close();
			}
		catch (Exception e)
		    {
			e.printStackTrace();
			done=false;
			}
		return done;
		}

//for checking against duplicate
   public boolean isGroupImageExisting(String grpId,String pageName) {
	 	boolean done=false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "select image from am_group_image where group_id=? and pageName=?";

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, grpId);
            ps.setString(2, pageName);
            rs = ps.executeQuery();

            while (rs.next())
            {
               done=true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_group_image]->" +
                    ex.getMessage());
        } finally {
        	dbConnection.closeConnection(con, ps, rs);
        }

        return done;

    }


public byte[] retrieveImageData(String pathname) {
	   ArrayList imageArray = new ArrayList();
	   File file;
	   BufferedInputStream bis;
	   byte[] imageData = null;

	   file = new File(pathname);
	   try {
		   bis = new BufferedInputStream(new FileInputStream(file));
	   } catch (FileNotFoundException fe) {
		   System.out.println("INFO: <<CheckImageDAO:retrieveImageData>>\n" +
							  " Image file does not Exist.");
		   return null;
	   }

	   int data;
	   try {
		   while ((data = bis.read()) != -1) {
			   imageArray.add(new Byte((byte) data));
		   }

		   imageData = new byte[imageArray.size()];
		   for (int i = 0; i < imageData.length; i++) {
			   imageData[i] = ((Byte) imageArray.get(i)).byteValue();
		   }
	   } catch (Exception ioe) {
		   System.out.println(
				   "INFORMATION, <<CheckImageDAO:retrieveImageData>> Could not retrieve image.");
		   System.out.println(ioe);
	   }

	   return imageData;
    }


public void getImageData(String grpId,String pageName)
{

      byte[] fileBytes;
     // File file;
     // file = new File(pathname);
      Connection conn = null;
      conn = dbConnection.getConnection("legendPlus");
      String query;
      try
      {
              query = "select image from am_group_image where group_id='"+grpId+"' and pageName='"+pageName+"'";
              Statement state = conn.createStatement();
              ResultSet rs = state.executeQuery(query);
              if (rs.next())
             {
                       fileBytes = rs.getBytes(1);
                       OutputStream targetFile = new FileOutputStream("d://filepath//new.JPG");
//                       OutputStream targetFile = new FileOutputStream(file);

                       targetFile.write(fileBytes);
                       targetFile.close();
             }

      }
      catch (Exception e)
      {
              e.printStackTrace();
      }
}


}