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

import magma.BarCodeHistoryBean;
import magma.net.dao.MagmaDBConnection;

/**
 *
 * @author Olabo
 */
public class BarCodeDisplayServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */



    private MagmaDBConnection dbConnection = new MagmaDBConnection();
         Connection con1 = null;
            PreparedStatement ps1 = null;
            ResultSet rs1 = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String asset_id = request.getParameter("asset_id");
        String branch_id = request.getParameter("branch_id");
        String category = request.getParameter("category");
        String userClass = (String)request.getSession().getAttribute("UserClass");
        System.out.println("asset id ========== " + asset_id);
        System.out.println("branch id ========== " +branch_id);
        System.out.println("category ========== " + category);



        try {
       	 if (!userClass.equals("NULL") || userClass!=null){
            System.out.println("=============in BarCodeDisplayServlet ======================");
      ArrayList al=findBarCodeByQuery(queryFormater(asset_id, branch_id,category));
            System.out.println("the size of the array list is  =======   " + al.size());
         request.setAttribute("slist", al);
           // getServletConfig().getServletContext().setAttribute("slist", slist);
            getServletConfig().getServletContext().getRequestDispatcher("/assetBarCodeDetails.jsp").forward(request, response);



            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BarCodeDisplayServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BarCodeDisplayServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
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



     public ArrayList findBarCodeByQuery(String queryFilter) {
         System.out.println("the content of queryFilter is $$$$$$$$$$$$$$$$$$$$" +queryFilter);
        String selectQuery = "select ASSET_ID,DESCRIPTION,BAR_CODE,BRANCH_CODE,category_code from am_barcode_history where PRINT_FLD = 'N'" + queryFilter;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList barcodelist = new ArrayList();

        try {
        
            con = dbConnection.getConnection("legenPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                   BarCodeHistoryBean bhb = new BarCodeHistoryBean();


            bhb.setAsset_id((rs.getString(1) == null)?"":rs.getString(1));
            bhb.setDescription((rs.getString(2) == null)?"":rs.getString(2));
            bhb.setBar_code((rs.getString(3) == null)?"":rs.getString(3));
            bhb.setBranch_code((rs.getString(4) == null)?"":getBranchName(rs.getString(4)));
            //bhb.setBranch_code((rs.getString(4) == null)?"":rs.getString(4));
            bhb.setCategory((rs.getString(5) == null)?"":getCategoryName(rs.getString(5)));
            //bhb.setCategory((rs.getString(5) == null)?"":rs.getString(5));
            //bhb.setCreate_date((rs.getDate(6) == null)?"":rs.getDate(6).toString());
            barcodelist.add(bhb);

            }
        
        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }

        return barcodelist;

    }


      public String getBranchName(String id){
String branch_name= "";
 try {
     //System.out.println("the value of id received by getBranchName() is " +id);
            con1 = dbConnection.getConnection("fixedasset");

            String query = "select BRANCH_NAME from am_ad_branch where BRANCH_CODE ='"+id+"'";

            ps1 = con1.prepareStatement(query);
            rs1 = ps1.executeQuery();
             //int counter =0;
            while(rs1.next()){



            branch_name = ((rs1.getString(1) == null)?"":rs1.getString(1));


            }//while
            //}//for
        } catch (Exception e) {
            System.out.println("BarCodePrintServlet:=== DB erorr occured in method getBranchName()" +e);
        }
    finally{
      dbConnection.closeConnection(con1, ps1,rs1);
    }

    return branch_name;

}//getBranchName()



   public String getCategoryName(String id){
String category_name= "";
 try {
     //System.out.println("the value of id received by getBranchName() is " +id);
            con1 = dbConnection.getConnection("fixedasset");

            String query = "select category_name from am_ad_category where category_code ='"+id+"'";

            ps1 = con1.prepareStatement(query);
            rs1 = ps1.executeQuery();
             //int counter =0;
            while(rs1.next()){



            category_name = ((rs1.getString(1) == null)?"":rs1.getString(1));


            }//while
            //}//for
        } catch (Exception e) {
            System.out.println("BarCodePrintServlet:=== DB erorr occured in method getCategoryName()" +e);
        }
    finally{
      dbConnection.closeConnection(con1, ps1,rs1);
    }

    return category_name;

}//getCategoryName()

   public String queryFormater(String id, String branch, String category){
   String searchQuery ="";


   if((category != null) && (!category.equals("")) && (!(category.equalsIgnoreCase("ALL")))){
		searchQuery = searchQuery + " AND CATEGORY_CODE = '"+category+"' ";
		}


		if((branch != null) && (!branch.equals("")) && (!(branch.equalsIgnoreCase("ALL")))){
		searchQuery = searchQuery + " AND BRANCH_CODE = '"+branch+"' ";
		}




		if((id != null) && (!id.trim().equals(""))){
		searchQuery = searchQuery + " AND ASSET_ID = '"+id+"' ";
		}






   return searchQuery;
   }//queryFormater()

}
