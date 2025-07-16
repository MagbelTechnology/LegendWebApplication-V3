package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import magma.net.dao.MagmaDBConnection;
import com.magbel.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MonthlyRegisterBackup extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/xml";
    MagmaDBConnection mgDbCon = null;
    ApplicationHelper applHelper = null;
    Connection con = null;
    Connection cn = null;
    Statement stmt = null;
    ResultSet rs = null;
    Statement s = null; 
    PreparedStatement ps = null;
    PreparedStatement ds = null;
  //  boolean rs = false;
    boolean done = false;
        
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonthlyRegisterBackup() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        signatureRequest(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        signatureRequest(request, response);
    }

    private void signatureRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String records = request.getParameter("record");
        String pagecode = request.getParameter("pagecode");
        String RecInsert = request.getParameter("RecInsert");
  //  	System.out.println("====pagecode form JSP ====> "+pagecode);
  //  	System.out.println("====RecInsert From JSP ====> "+RecInsert);
  //      if(RecInsert==""){
   // 	System.out.println("====pagecode First ====> "+pagecode);
  //  	System.out.println("====RecInsert firts ====> "+RecInsert);
        //String signature2 = request.getParameter("signature2");
        int i = 0;
        System.out.println("About to Delete records from file AM_ASSET_MONTHLY");
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
        PrintWriter out = response.getWriter();
    	String Sapquery="delete from AM_ASSET_MONTHLY where asset_id is not null ";
        cn = mgDbCon.getConnection("");  
    	// cn = mgDbCon.getConnection("");
         System.out.println("Records Delete from file AM_ASSET_MONTHLY");
    //     String RecInsert = "";
        try { 
        	ds = cn.prepareStatement(Sapquery);
         	  done = ds.executeUpdate() != -1;            	   
        } catch (Exception e) {
            e.printStackTrace();
        } //finally { 
   //        try {
            	 /*         if (cn != null) {
                    cn.close();
                } 
              if (ds != null) {
                	ds.close();
                }   
               if (rs != null) {
                    rs.close();
                }*/  
             //   con.close();
  //          } catch (Exception ex) {
 //               ex.printStackTrace();
 //           } 
  //      }        
     //   mgDbCon = new MagmaDBConnection();
     //   applHelper = new ApplicationHelper();
    //   System.out.println("====records 0 ====> "+records);
   // 	System.out.println("====pagecode Before If ====> "+pagecode);
  //  	System.out.println("====RecInsert Before If ====> "+RecInsert);
  //   	if(pagecode.trim()!="1" && RecInsert==""){ 
 //    	  RecInsert = "No";
     // int reccount = Integer.parseInt(records);       
     //   INSERT INTO [INWARD_CHEQUE_COMMENT] SELECT * FROM [INWARD_CHEQUE] WHERE MTID =?";
    	String query="INSERT INTO [AM_ASSET_MONTHLY] SELECT * FROM [am_asset]  where asset_id is not null";
        con = mgDbCon.getConnection("");
        System.out.println("About to Insert records into file AM_ASSET_MONTHLY " +query);
        try { //System.out.println("====reccount 00 ====> "+reccount);
        //    if(records.trim().equalsIgnoreCase("1") && records.trim()!="2"){
       // while (reccount== 1){
        for( i=i; i<3; i++)    {
 //       System.out.println("====records Before If ====> "+i);
        if(i==1){  
        	
      //  		reccount = reccount + 2;  
     //      	System.out.println("====i 1 ====> "+i); 
   //        	System.out.println("====pagecode Inside If ====> "+pagecode);
   //     	System.out.println("====RecInsert Inside If ====> "+RecInsert);
        	 ps = cn.prepareStatement(query);
        	  done = ps.executeUpdate() != -1;      
        	//  done = ps.execute();
  //            pagecode = "1";
 //             RecInsert = "No";
       	 System.out.println(" Insert records Done");  
 //            System.out.println("====records 2 ====> "+reccount);
 //            System.out.println("====i 2 ====> "+i);
             i = i + 1;
             pagecode = "1";
       
         } 
     //    	  reccount = reccount + 2;
            }     
    //    reccount = reccount + 2;
           // records = "2";
          //  con.close();
 //       pagecode = "1";
 //       RecInsert = "No";  
  //     	System.out.println("====pagecode  ====> "+pagecode);
   // 	System.out.println("====RecInsert ====> "+RecInsert);
      //  out.print("<script>alert('Response saved successfully.');</script>");
       //out.print("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing&RecInsert="+RecInsert+"&pagecode="+pagecode+"'</script>");
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {   
          try {   
     /*       if (con != null) { 
                  con.close();
              }*/
               if (con != null) {  
                   con.close();  
               }              
 //             if (ps != null) {
 //                 ps.close();
 //               }
 //               if (ds != null) {
 //                   ds.close(); 
 //               }
    //          	System.out.println("====pagecode Window ====> "+pagecode);
   //         	System.out.println("====RecInsert Window ====> "+RecInsert);
               out.print("<script>window.location='DocumentHelp.jsp?np=depreciationProcessing'</script>");               
           } 
        catch (Exception ex) {
                ex.printStackTrace();
            }
        }
   //  	}
  //      }
    }
}
