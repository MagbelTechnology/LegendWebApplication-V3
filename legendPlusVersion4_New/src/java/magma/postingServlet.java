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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import magma.AssetRecordsBean;
import com.magbel.legend.mail.EmailSmsServiceBus;
/**
 *
 * @author Olabo
 */
public class postingServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String asset_id = request.getParameter("asset_id");
       String page = request.getParameter("page");
       String result="";
       String emailsent="";
        System.out.println("===========the value of asset_id is "+ asset_id);
        System.out.println("===========the value of page is "+ page);


        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

ResultSet rs= null;
Connection con = null;

	     PreparedStatement ps = null;
try
        {

           String query = "select entrypostflag,email_sent from am_raisentry_post where id='"+asset_id+"' and page='"+page+"'" ;
              con = (new DataConnect("fixedasset")).getConnection();

              ps = con.prepareStatement(query);
              //ps.setString(1, asset_id);
              //ps.setString(2, page);

	      rs = ps.executeQuery();
          System.out.println("================ the query for execution is "+ query);
           while(rs.next()) {
            result = rs.getString("entrypostflag");
            emailsent = rs.getString("email_sent");
               System.out.println("The value of entrypostflag is LLLLLLLLLLLLLLLLLL "+result);
           }



        }
        catch(SQLException ex) { }
        catch(Exception ex) { }
        finally{
       //out.close();
            closeConnection(con,ps,rs);
        }

        if(DOC_TYPE != null)
        {
            out.println(DOC_TYPE);
        }



        try{
            if(result.equalsIgnoreCase("Y") && emailsent.equalsIgnoreCase("N")){
        AssetRecordsBean arb = new AssetRecordsBean();
        sendMailAfterPosting(asset_id,page);
         arb.updateNewAssetStatus(asset_id);
            }
        }catch(Exception ex){
            System.out.println("Error occured in postingServlet "+ex);
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
            System.out.println("WANR:postingServlet Error closing connection >>" + e);
        }
    }



     private void sendMailAfterPosting(String asset_id,String page1){

		 String query="update am_raisentry_post set email_sent='Y' where id='"+asset_id+"' and page='"+page1+"'";
         Connection con = null;

	     PreparedStatement ps = null;
         ResultSet rs= null;
         
         try {


        EmailSmsServiceBus mail = new EmailSmsServiceBus();
        String subjectr ="Payment for new asset";
		String msgText11 ="Customer has been paid for asset with ID: "+ asset_id +".";
            System.out.println("=========================the message for email is " + msgText11);
        mail.sendMailUser(asset_id, subjectr, msgText11);



             con = (new DataConnect("fixedasset")).getConnection();

              ps = con.prepareStatement(query);
              boolean b =ps.execute();


                    }

	         catch (Exception ex) {
		            System.out.println("WARNING: cannot fetch [sendMailAfterPosting]- > " +
		                    ex.getMessage());
		        } finally {
		           closeConnection(con, ps,rs);
		        }
     }


}
