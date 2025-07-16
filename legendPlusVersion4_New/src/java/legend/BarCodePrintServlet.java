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
public class BarCodePrintServlet extends HttpServlet {
   
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

             Connection con1 = null;
            PreparedStatement ps1 = null;
            ResultSet rs1 = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String[] cbx_values= request.getParameterValues("cb_barcode");




        // String cmdprint = request.getParameter("print");
       // String cmdview = request.getParameter("view");
       // String asset_id = request.getParameter("asset_id");
        //String branch_id = request.getParameter("branch_id");
        //String category = request.getParameter("category");
        //String option = request.getParameter("option");



       //System.out.println("the value of print hidden field is=======++++++++++ " + cmdprint);
        //ArrayList mylist = new ArrayList();


        //if(cbx_values != null ){
        try {
            System.out.println("responding to ajax call ");

            /*
            if(option.equals("1")){
                System.out.println("responding to ajax call with option of value &&&&&&&&&&&&&" +option);
        ArrayList mlist=   findBarCodeByQuery(formQuery(asset_id,branch_id,category));

         request.setAttribute("mlist", mlist);
            getServletConfig().getServletContext().setAttribute("mlist", mlist);
            getServletConfig().getServletContext().getRequestDispatcher("/assetBarCodeDetails.jsp").forward(request, response);


            }
            
            else
           */
         if(cbx_values != null){
           // System.out.println("==the number of selected items are=== " +cbx_values.length);
            


             ArrayList plist =getSelectedAsset(cbx_values);
           
        BarCodePrinterServlet bcps = new BarCodePrinterServlet();
        bcps.setArrayList(plist);

            request.setAttribute("plist", plist);
            getServletConfig().getServletContext().setAttribute("plist", plist);
            getServletConfig().getServletContext().getRequestDispatcher("/ViewBarCodePrintout.jsp").forward(request, response);
//}

 /*
if(cmdview !=null){
    ArrayList plist =getSelectedAsset(mylist);
            //setPrtField( mylist);

        
            request.setAttribute("plist", plist);
            getServletConfig().getServletContext().getRequestDispatcher("/ViewBarCodePrintout.jsp").forward(request, response);
            
}//if
else{



}//else
 
*/

            /* TODO output your page here

             out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BarCodePrintServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BarCodePrintServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
         }else{
         out.println("<script>alert('No bar code history to print');</script>");
         out.println("<script>window.location ='DocumentHelp.jsp?np=assetBarCodeDetails.jsp'</script>");


         }
     
        }


        finally {
            out.close();
            }
        //}//outer if

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

/*
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
            System.out.println("BarCodeServlet:=== DB erorr occured in method setPrtField()" +e);
        }
    finally{
      dbConnection.closeConnection(con, ps);
    }


}//setPrtField()
*/

     public ArrayList getAssetList(){

    ArrayList barcodelist = new ArrayList();
    //BarCodeHistoryBean bhb = new BarCodeHistoryBean();

        try {
            String query = "select ASSET_ID,DESCRIPTION,BAR_CODE,BRANCH_CODE,category_code from am_barcode_history where PRINT_FLD = 'N'";
             con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
             //int counter =0;
            while(rs.next()){
                 BarCodeHistoryBean bhb = new BarCodeHistoryBean();
            //result = rs.getString("PRINT_FLD");
               // if(rs.getString(1).equalsIgnoreCase(asset_id))barCode_status = true;
                //System.out.println("====barcode status is " + barCode_status);
               // found = rs.getInt(1);

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
            System.out.println("BarCodeServlet:=== DB erorr occured in method getAssetList()" +e);
        }
    finally{
      dbConnection.closeConnection(con, ps);
    }

    return barcodelist;
    }//getAssetList();



      public ArrayList getSelectedAsset(String[] list){

    ArrayList myarraylist = new ArrayList();
    String[] barcodelist = list;
     BarCodeHistoryBean bhb = null;


        try {

            con = dbConnection.getConnection("fixedasset");
            for(int j = 0; j<barcodelist.length;j++){
                 //bhb = ( BarCodeHistoryBean)barcodelist[j];

            String query = "select ASSET_ID,DESCRIPTION,BAR_CODE,BRANCH_CODE,category_code from am_barcode_history where asset_id ='"+barcodelist[j]+"'";

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
             //int counter =0;
            while(rs.next()){
                 BarCodeHistoryBean bhp = new BarCodeHistoryBean();
            

            bhp.setAsset_id((rs.getString(1) == null)?"":rs.getString(1));
            bhp.setDescription((rs.getString(2) == null)?"":rs.getString(2));
            bhp.setBar_code((rs.getString(3) == null)?"":rs.getString(3));
            bhp.setBranch_code((rs.getString(4) == null)?"":getBranchName(rs.getString(4)));
            //bhp.setBranch_code((rs.getString(4) == null)?"":rs.getString(4));
            bhp.setCategory((rs.getString(5) == null)?"":getCategoryName(rs.getString(5)));
            //bhp.setCategory((rs.getString(5) == null)?"":rs.getString(5));
            //bhb.setCreate_date((rs.getDate(6) == null)?"":rs.getDate(6).toString());
          myarraylist.add(bhp);

            }//while
            }//for
        } catch (Exception e) {
            System.out.println("BarCodeServlet:=== DB erorr occured in method getAssetList()" +e);
        }
    finally{
      dbConnection.closeConnection(con, ps);
    }

    return myarraylist;
    }//getAssetList();


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



   public String formQuery(String id, String branch, String cat){
   String searchQuery ="";
   if((cat != null) && (!cat.equals("")) && (!(cat.equalsIgnoreCase("ALL")))){
		searchQuery = searchQuery + " AND CATEGORY_ID = '"+cat+"' ";
		}

		//System.out.println("in jsp ==========================The content of searchQuery is=======operation search=============== " + searchQuery );
		if((branch != null) && (!branch.equals("")) && (!(branch.equalsIgnoreCase("ALL")))){
		searchQuery = searchQuery + " AND BRANCH_CODE = '"+branch+"' ";
		}




		if((id != null) && (!id.trim().equals(""))){
		searchQuery = searchQuery + " AND ASSET_ID = '"+id+"' ";
		}

       System.out.println("the query sent to findBarCodeByQuery" + searchQuery);
   return searchQuery;
   }



        public ArrayList findBarCodeByQuery(String queryFilter) {
       //  System.out.println("the content of queryFilter is $$$$$$$$$$$$$$$$$$$$" +queryFilter);
        String selectQuery = "select ASSET_ID,DESCRIPTION,BAR_CODE,BRANCH_CODE,category_code from am_barcode_history where PRINT_FLD = 'N'" + queryFilter;

        //Connection con = null;
        //PreparedStatement ps = null;
        //ResultSet rs = null;
        ArrayList barcodelist = new ArrayList();

        try {
            con = dbConnection.getConnection("fixedasset");
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






}
