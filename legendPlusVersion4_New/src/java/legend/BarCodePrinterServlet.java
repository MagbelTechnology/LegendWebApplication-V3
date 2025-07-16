/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

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
import magma.net.dao.MagmaDBConnection;
import magma.BarCodeHistoryBean;

/**
 *
 * @author Olabo
 */
public class BarCodePrinterServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


     private MagmaDBConnection dbConnection = new MagmaDBConnection();
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList mylist = null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BarCodePrinterServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BarCodePrinterServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
           //ArrayList hik = (ArrayList)request.getAttribute("plist");
          ArrayList hik = (ArrayList)getServletConfig().getServletContext().getAttribute("plist");
        //System.out.println("?????????????the value of plist arraylist is?????????? " + hik.size());
            setPrtField(hik);
             getServletConfig().getServletContext().getRequestDispatcher("/DocumentHelp.jsp").forward(request, response);

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
        processRequest(request, response);
    } 

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

    public void setArrayList(ArrayList al){

mylist = al;

    }

      public void setPrtField(ArrayList alist){

    ArrayList barcodelist = alist;
    BarCodeHistoryBean bhb = null;
   // for(int j =0; j<barcodelist.size();j++)

try {

             con = dbConnection.getConnection("fixedasset");
for(int j =0; j<barcodelist.size();j++){
   bhb = ( BarCodeHistoryBean)barcodelist.get(j);
   String query = "update am_barcode_history set PRINT_FLD = 'Y' where ASSET_ID ='"+ bhb.getAsset_id() +"'";
   //if(bhb.getChecked().equalsIgnoreCase("Y")){

            ps = con.prepareStatement(query);
            ps.execute();
   // }//if
}//for

        } catch (Exception e) {
            System.out.println("BarCodePrinterServlet:=== DB erorr occured in method setPrtField()" +e);
        }
    finally{
      dbConnection.closeConnection(con, ps);
    }


}//setPrtField()
}
