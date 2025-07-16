/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.magbel.util.DataConnect;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import magma.asset.manager.AssetManager;
/**
 *
 * @author Olabo
 */
public class ApprovalLimitServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;
AssetManager am = new AssetManager();
    //Initialize global variables
    public void init() throws ServletException {
    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ in approvla limit servlet////////////////");
    }

    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
       double asset_cost = Double.parseDouble(request.getParameter("asset_cost"));
        String user_id = request.getParameter("user_id");
        int tranId = (request.getParameter("tranId")==null)?0:Integer.parseInt(request.getParameter("tranId"));
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ in approvla limit servlet////////////////");
        response.setContentType("text/xml");
        //response.setHeader("Cache-Control","no-cache,no-store,must-revalidate" );
        PrintWriter out = response.getWriter();

       
        out.write("<message>");

      
ResultSet rs= null;
Connection con = null;

	     PreparedStatement ps = null;

try
        {

    
    if(tranId ==0){String query ="select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_level=b.level_code where user_id<> '"+ user_id+"' and  b.min_amount< "+asset_cost +" and b.max_amount > "+asset_cost+" and is_supervisor='Y'";
        System.out.println("the value of transaction id is ///@@@@@@@@@@@@@@@@@@" + tranId);
     con = (new DataConnect("fixedasset")).getConnection();
	      ps = con.prepareStatement(query);
	      rs = ps.executeQuery();
           while(rs.next()) {

               System.out.println("fetching recordsssssssssssssssssssssssssssssssssssssssss");
            out.write("<make>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2));
                out.write("</name>");
           out.write("</make>");


    }//end while
    //String query ="select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_level=b.level_code where user_id<> '"+ user_id+"' and  b.min_amount< "+asset_cost +" and b.max_amount > "+asset_cost+" and is_supervisor='Y'";


    
    }//endif


    else{
int[] approvees_id = new int[7];
int counter =0;
String[] approvees_names  = new String [7];

       // System.out.println("I am in the else part of approvallimitservlet//////////////////");
       // System.out.println("//////////////the value of transaction id is///////////"+tranId);
    String query ="select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_level=b.level_code where user_id<> '"+ user_id+"' and  b.min_amount< "+asset_cost +" and b.max_amount > "+asset_cost+" and is_supervisor='Y'";

        //System.out.println("the query for analysis is /////////////////////////"+query);

            int[] sup = am.getUsedSupervisors(tranId);
            //System.out.println("asset user ///////////////////"+sup[0]);
           // System.out.println("asset user ///////////////////"+sup[1]);
            //System.out.println("asset user ///////////////////"+sup[2]);
            //System.out.println("asset user ///////////////////"+sup[3]);
            //System.out.println("asset user ///////////////////"+sup[4]);

       // System.out.println("the array of supervisor contains /////////////"+sup.length);
    con = (new DataConnect("fixedasset")).getConnection();
	      ps = con.prepareStatement(query);
	      rs = ps.executeQuery();

          while(rs.next()) {
             approvees_id[counter] = Integer.parseInt(rs.getString(1));
             approvees_names[counter] = rs.getString(2);
              System.out.println("the value of ///////////////"+ approvees_id[counter]);
             counter += 1;
              
           }//end while

  for(int j=0;j<=approvees_id.length;++j){
  
  if(approvees_id[j]== sup[0]||approvees_id[j]== sup[1]||approvees_id[j]== sup[2]||approvees_id[j]== sup[3] ||approvees_id[j]== sup[4] ||approvees_id[j]== sup[5])
  {//System.out.println("in the true part///////////////////////////////////////");
  }else{
      //System.out.println("in the false part///////////////////////////////////////");
                out.write("<make>");
                out.write("<id>");
                out.write(""+approvees_id[j]);
                out.write("</id>");
                out.write("<name>");
                out.write(""+approvees_names[j]);
                out.write("</name>");
                out.write("</make>");
  
  }//else
  }//for



    }//else




       
        }//try
        //catch(SQLException ex) { } 
        catch(Exception ex) { }
        finally{
       //out.close();
            closeConnection(con,ps,rs);
        }

      

        out.write("</message>");

          
        if(DOC_TYPE != null)
        {
            out.println(DOC_TYPE);
        }
    }

    public void destroy()
    {
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
