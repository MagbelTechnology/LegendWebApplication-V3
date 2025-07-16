package com.magbel.admin.bus;

/*
 * $Id: Chap0609.java,v 1.4 2003/06/25 07:36:33 blowagie Exp $
 * $Name:  $
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001 by Bruno Lowagie & Paulo Soares <--
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://www.lowagie.com/iText/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//import magma.net.dao.MagmaDBConnection;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.magbel.admin.dao.MagmaDBConnection;

public class Chap0609  extends MagmaDBConnection
{
	MagmaDBConnection mgDbCon = null;
	
    public  void Test(byte imextOut[])
    {
        
        System.out.println("Chapter 6 example 9: bytes() / Raw Image");
        
        // step 1: creation of a document-object
        Document document = new Document();
  //      System.out.println("  Raw Image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>1");
        try {
            
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
 //       	 System.out.println("  Raw Image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>2");
            PdfWriter.getInstance(document, new FileOutputStream("E:/jboss-4.0.5.GA/server/epostserver/deploy/legend2.net.war/page37b.pdf"));
 //           System.out.println("  Raw Image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3");
            // step 3: we open the document
            document.open();
 //           System.out.println("  Raw Image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>4");
            // step 4: we add content (example by Paulo Soares)
            
            // creation a jpeg passed as an array of bytes to the Image
            //RandomAccessFile rf = new RandomAccessFile("myKids.jpg", "r");
            //int size = (int)rf.length();
            
            
           
          
        //    System.out.println("  Raw Image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>5");
            //byte imext[] = new byte[size];
            //rf.readFully(imext);
            //rf.close();
            
            if(imextOut != null)
            {
       //      System.out.println("Got data>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");	
            Image img1 = Image.getInstance(imextOut);
            img1.setAbsolutePosition(50, 500);
            document.add(img1);
            }
            // creation of an image of 100 x 100 pixels (x 3 bytes for the Red, Green and Blue value)
            byte data[] = new byte[100*100*3];
            for (int k = 0; k < 100; ++k) {
                for (int j = 0; j < 300; j += 3) {
                    data[k * 300 + j] = (byte)(255 * Math.sin(j * .5 * Math.PI / 300));
                    data[k * 300 + j + 1] = (byte)(256 - j * 256 / 300);
                    data[k * 300 + j + 2] = (byte)(255 * Math.cos(k * .5 * Math.PI / 100));
                }
            }
            Image img2 = Image.getInstance(100, 100, 3, 8, data);
            img2.setAbsolutePosition(200, 200);
            document.add(img2);
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        // step 5: we close the document
        document.close();
    }
    
   
    
    
    
    public byte[] getImageData2(String reqnId,String pageName) 
	   {
    	  byte[] fileBytes=null;
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        String user_id = "";
	       
	        String FINDER_QUERY = "select image from am_ad_image where Id='"+reqnId+"' and pageName='"+pageName+"'";

	        try {
	            con = getConnection("helpDesk");
	            ps = con.prepareStatement(FINDER_QUERY);
	           
	            
	            rs = ps.executeQuery();

	            while (rs.next()) 
	            {
	    //        	System.out.println("About to fetch data>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    fileBytes = rs.getBytes(1);
                    //OutputStream targetFile = new FileOutputStream("d://filepath//new.JPG");
                    OutputStream targetFile = new FileOutputStream("");
//                    OutputStream targetFile = new FileOutputStream(file);
                    
                    targetFile.write(fileBytes);
                    targetFile.close();
	            }

	        } catch (Exception ex) {
	            System.out.println("WARNING: cannot fetch user id ->" +
	                    ex.getMessage());
	        } finally {
	            closeConnection(con, ps, rs);
	        }

	        return fileBytes;

	    }
    
//	sql server image retrieve
    public byte[] getImageData(String reqnId,String pageName)
    {
    	mgDbCon = new MagmaDBConnection();
             byte[] fileBytes=null;
             Connection conn = null;  
             System.out.println("Check Me Please");
             conn = mgDbCon.getConnection("helpDesk");
            // conn = getConnection("HelpDesk");
             String query;
             try  
             {
          //  	 System.out.println("====About to Get ImagereqnId== "+reqnId);
                     //query = "select image from am_ad_image where assetId='"+reqnId+"' and pageName='"+pageName+"'";
            	    query = "select image from am_ad_image where Id='"+reqnId+"' ";
                     Statement state = conn.createStatement();
                     ResultSet rs = state.executeQuery(query);
                     if (rs.next())
                    {
                              fileBytes = rs.getBytes(1);
                    }   else{
               //     	System.out.println("==No Image Found Please====");
                    }     
                    
             }
             catch (Exception e)
             {
                     e.printStackTrace();
             }
             finally {
     			
     			try {
     				if(conn != null)
     				{
     					conn.close();
     				}
     			}
     		       catch(Exception d){d.printStackTrace();}
     			}
             return fileBytes;
    }
    public boolean setIncidentImage(Blob incBlob, String complaintId) {

        Connection con = null;
        PreparedStatement ps = null;
        String setBlobQuery = "UPDATE HD_COMPLAINT SET image = ? WHERE complaint_id = ?  ";
        boolean doneUpdate = false;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(setBlobQuery);

            ps.setBlob(1, incBlob);
            ps.setString(2, complaintId);
            doneUpdate = ps.executeUpdate() == -1;


        } catch (Exception ex) {
            System.out.println("WARNING: cannot update setIncidentImage+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return doneUpdate;
    }

    
    
    
    public byte[] getGroupImageData(String group_id,String pageName)
    {
            
             byte[] fileBytes=null;
             Connection conn = null;  
             conn = getConnection("fixedasset");
             String query;
             try
             {
                     query = "select image from am_group_image where group_id='"+group_id+"' and pageName='"+pageName+"'";
                     Statement state = conn.createStatement();
                     ResultSet rs = state.executeQuery(query);
                     if (rs.next())
                    {
                              fileBytes = rs.getBytes(1);
                    }        
                    
             }
             catch (Exception e)
             {
                     e.printStackTrace();
             }
             finally {
     			
     			try {
     				if(conn != null)
     				{
     					conn.close();
     				}
     			}
     		       catch(Exception d){d.printStackTrace();}
     			}
             return fileBytes;
    }
//	sql server image retrieve
    public byte[] getImageDataOutput(String assetId,String pageName)
    {
            
             byte[] fileBytes=null;
             Connection conn = null;  
             conn = getConnection("fixedasset");
             String query;
             try
             {
                     query = "select image from am_ad_image where assetId='"+assetId+"' and pageName='"+pageName+"'";
                     Statement state = conn.createStatement();
                     ResultSet rs = state.executeQuery(query);
                     if (rs.next())
                    {
                              fileBytes = rs.getBytes(1);
                              
                    }        
                    
             }
             catch (Exception e)
             {
                     e.printStackTrace();
             }
             finally {
     			
     			try {
     				if(conn != null)
     				{
     					conn.close();
     				}
     			}
     		       catch(Exception d){d.printStackTrace();}
     			}
             return fileBytes;
    }
}

