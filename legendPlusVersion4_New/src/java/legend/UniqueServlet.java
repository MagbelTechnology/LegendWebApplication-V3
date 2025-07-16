/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

import com.magbel.util.DataConnect;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Olabo
 */
public class UniqueServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ArrayList list = new ArrayList();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
          response.setDateHeader("Expires", -1);
           response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        String userClass = (String)request.getSession().getAttribute("UserClass");
        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UniqueServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UniqueServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */

            //System.out.println("============inside processrequest method========");
           String bar_code = request.getParameter("bar_code");
           String lpo = request.getParameter("lpo");
	  //String role = request.getParameter("role");
           String invNo = request.getParameter("inv");
       //    String vendorNo = request.getParameter("sb");
           System.out.println("invNo >>>>>>>>> " + invNo);
	  String operation = request.getParameter("OPT").trim();
	if(operation.equals("1"))
      {
          if ((bar_code != null)||(bar_code != ""))
          {
        	  String bar_codeFound = getCodeName("SELECT bar_code  FROM am_asset WHERE bar_code='"+bar_code+"'");
              String result = bar_codeFound;
              processBarCode(request, response, result);
          }
      }


if(operation.equals("2"))
      {
          if ((lpo != null)||(lpo != ""))
          {
        	  String lpoFound = getCodeName("SELECT lpo  FROM am_asset WHERE lpo='"+lpo+"'");
              String result = lpoFound;
              processBarCode(request, response, result);
          }
      }

if(operation.equals("3"))
      {
          if ((invNo != null)||(invNo != ""))
          {
          System.out.println("InvoiceNumber >>>>>>>> " +invNo );
        	  String invFound = getCodeName("SELECT INVOICE_NO  FROM am_invoice_no WHERE INVOICE_NO='"+invNo+"'");
                  //System.out.println("invFound >>>>>>>> " +invFound );
              String result = invFound;
              processBarCode(request, response, result);
          }
      }
        }
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
 response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

    
        String userClass = (String)request.getSession().getAttribute("UserClass");
    
     try {
    	 if (!userClass.equals("NULL") || userClass!=null){  
            System.out.println("============inside processrequest method========");
           String bar_code = request.getParameter("bar_code");
           String lpo = request.getParameter("lpo");
	  //String role = request.getParameter("role");
	  String operation = request.getParameter("OPT").trim();
          String invNo = request.getParameter("inv");
          String assetID=request.getParameter("id");
          String vendorNo = request.getParameter("sb");
	if(operation.equals("1"))
      {
          if ((bar_code != null)||(bar_code != ""))
          {
        	  String bar_codeFound = getCodeName("SELECT bar_code  FROM am_asset WHERE bar_code='"+bar_code+"'");
              String result = bar_codeFound;
              processBarCode(request, response, result);
          }
      }

if(operation.equals("2"))
      {
          if ((lpo != null)||(lpo != ""))
          {
        	  String lpoFound = getCodeName("SELECT lpo  FROM am_asset WHERE lpo='"+lpo+"'");
              String result = lpoFound;
              processBarCode(request, response, result);
          }
      }

if(operation.equals("3"))
      {
          if ((invNo != null)||(invNo != ""))
          {
          System.out.println("InvoiceNumber <<<<<<<<<<< " +invNo );
        	  String invFound = getCodeName("SELECT INVOICE_NO  FROM am_invoice_no WHERE INVOICE_NO='"+invNo+"'");
                 System.out.println("invFound <<<<<<<< " +invFound );
              String result = invFound;
              processBarCode(request, response, result);
          }
      }
if(operation.equals("4"))
      {
          if (((invNo != null)||(invNo != ""))&&((lpo != null)||(lpo != "")))
          {
              String invQry="Select invoice_no from am_invoice_no where INVOICE_NO='"+invNo+"' and lpo='"+lpo+"'" +
                      " and asset_id='"+assetID+"'";
              System.out.println("invQry >>>>>>>>>> " + invQry);
              String invFound = getCodeName(invQry);
                  System.out.println("invFound >>>>>>>> " +invFound );
              String result = invFound;
              processBarCode(request, response, result);
          }
      }
     }
        }
    
    finally { 
            out.close();
        }
    
    
    }//doGet()

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);

    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void processBarCode(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
             IOException, ServletException {
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");
         response.getWriter().write(result);
//System.out.println("===================inside processBarCode============================");
     }



    public String getCodeName(String query)
	    {
	     String result = "";
	     Connection con = null;
	     ResultSet rs = null;
	     PreparedStatement ps = null;

	     try
	     {
	      con = (new DataConnect("legendPlus")).getConnection();
	      ps = con.prepareStatement(query);
	      rs = ps.executeQuery();


	      while(rs.next())
	      {

             // System.out.println("inside getcodename iteration");
	       result = rs.getString(1) == null ? "" : rs.getString(1);
//System.out.println("===========inside getcodename iteration=======================" + result);
	      }
	     }
	     catch(Exception er)
	     {
	        System.out.println("Error in Query- getCodeName()... ->"+er);
	        er.printStackTrace();
	     }finally{
	        closeConnection(con,ps,rs);
	      }
	      return result;
	     }
private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
        try {
//System.out.println("=============inside closeConnection==========");
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            System.out.println("WANR: Error closing connection >>" + e);
        }
    }
}
