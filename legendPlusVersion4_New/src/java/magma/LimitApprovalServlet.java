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
import com.magbel.util.DataConnect;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import magma.asset.manager.AssetManager;
import legend.admin.objects.User;
import com.magbel.legend.vao.ApprovalRemark;
import com.magbel.util.HtmlUtility;

/**
 *
 * @author Olabo
 */
public class LimitApprovalServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/xml";
    //@todo set DTD
    private static final String DOC_TYPE = null;
    AssetManager am = new AssetManager();
    //Initialize global variables
    HtmlUtility htmlutil = new HtmlUtility();
    public void init() throws ServletException {
        // System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ in approvla limit servlet////////////////");
    }
 
    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String cost =request.getParameter("asset_cost");
    	if(cost==null || cost.equals("")){cost = "0";}
//        double asset_cost = (request.getParameter("asset_cost") == null) ? 0 :Double.parseDouble(request.getParameter("asset_cost"));
        double asset_cost = Double.parseDouble(cost);
        String user_id = request.getParameter("user_id");
        long tranId = (request.getParameter("tranId") == null) ? 0 : Long.parseLong(request.getParameter("tranId"));
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ tranId////////////////" + tranId);
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        PrintWriter out = response.getWriter();
        String tranType = request.getParameter("tranType");
        String reqnId = request.getParameter("reqnId");
        
        String apprvLevel = request.getParameter("apprvLevel");
        String userId = request.getParameter("userId");
        if(tranType == null)tranType ="";
        double min_asset_cost = (request.getParameter("min_asset_cost") == null) ? 0 :Double.parseDouble(request.getParameter("min_asset_cost"));
        double max_asset_cost = (request.getParameter("max_asset_cost") == null) ? 0 :Double.parseDouble(request.getParameter("max_asset_cost"));
//        String deptCode = htmlutil.findObject("select dept_code from am_gb_user where user_id ='"+user_id+"' ");
        String deptCode = htmlutil.findObjectParam(user_id);
//        System.out.println("=====>>>>deptCode: "+deptCode);
        out.write("<message>");

        ResultSet rs = null;
        Connection con = null;

        PreparedStatement ps = null;

        try {
            String query="";

            // if(tranId ==0){String query ="select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_level=b.level_code where user_id<> '"+ user_id+"' and  b.min_amount< "+asset_cost +" and b.max_amount > "+asset_cost+" and is_supervisor='Y'";
            if (tranId == 0) {

                if(tranType.equalsIgnoreCase("bulkUpdate")){
//                    System.out.println("here in bulkupdate section <<<<<<<<<<<< ");
                query = "select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on" +
                        " a.approval_limit=b.level_code where user_id<> ? and  b.min_amount<= ? and " +
                        "b.max_amount >= ? and is_supervisor='Y' and upper(User_Status) = 'ACTIVE'  and dept_code = ? ";

                }else{
                 query = "select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on" +
                        " a.approval_limit=b.level_code where user_id<> ? and  b.min_amount<= ? and " +
                        "b.max_amount >= ? and is_supervisor='Y'  and upper(User_Status) = 'ACTIVE'  and dept_code = ? ";
                // System.out.println("the value of transaction id is ///@@@@@@@@@@@@@@@@@@" + tranId);
 
                }
//                System.out.println("the value of query is ///@@@@@@@@@@@@@@@@@@" + query);
//                System.out.println("the value for user_id: " + user_id+"   min_asset_cost: "+min_asset_cost+"   max_asset_cost: "+max_asset_cost+"    deptCode: "+deptCode+"      tranType: "+tranType);
                con = (new DataConnect("legendPlus")).getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, user_id);
                ps.setDouble(2, min_asset_cost);
                ps.setDouble(3, max_asset_cost);
                ps.setString(4, deptCode);
                rs = ps.executeQuery();
                while (rs.next()) {

//                     System.out.println("fetching recordsssssssssssssssssssssssssssssssssssssssss");
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
            else {
            	if(tranType.equalsIgnoreCase("")){
//                System.out.println("in new limit approval servlet{{{{{{{{{{{{{{{{{{{{%%");
                ArrayList _list2 = new ArrayList();
                ArrayList freeSupList =null;
                User user = null;
                ApprovalRemark usedSupervisor = null;

                int counter = 0;

                  if(tranType.equalsIgnoreCase("bulkUpdate")){
//                      System.out.println("here in bulkupdate section <<<<<<<<<<<<<<<< ");
                  query = "select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_limit=b.level_code where user_id<> ? and  b.min_amount <= ? and b.max_amount > ? and is_supervisor='Y'  and upper(User_Status) = 'ACTIVE' ";
                  }
                  else{

                query = "select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_limit=b.level_code where user_id<> ? and  b.min_amount< ? and b.max_amount > ? and is_supervisor='Y'  and upper(User_Status) = 'ACTIVE' ";

                  }

                // System.out.println("the query for analysis is /////////////////////////" + query);
                HashSet usedSupSet = am.getUsedSupervisors2(tranId);
                HashSet unUsedSupSet = new HashSet();
//                System.out.println("the list of supervisor contains ///////////// " + usedSupSet.size());
                con = (new DataConnect("legendPlus")).getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, user_id);
                ps.setDouble(2, min_asset_cost);
                ps.setDouble(3, max_asset_cost);
                rs = ps.executeQuery();


                while (rs.next()) {
                    user = new User();

                    user.setUserId(rs.getString(1));
                    user.setUserName(rs.getString(2));
                    _list2.add(user);
                    unUsedSupSet.add(Integer.parseInt(rs.getString(1)));

                }//end while

 //               System.out.println("the number  of supervisor is ///////////////" + unUsedSupSet.size());


                unUsedSupSet.removeAll(usedSupSet);
          ArrayList processedList = new ArrayList(unUsedSupSet);

          for(int u=0; u < processedList.size();++u){
        Integer intObj =  (Integer)processedList.get(u);
            int userID = intObj.intValue();

            out.write("<make>");
                            out.write("<id>");
                            out.write("" + userID);
                            out.write("</id>");
                            out.write("<name>");
                            out.write("" + am.findUserName(userID));
                            out.write("</name>");
                          out.write("</make>");
       }
            }else{
                if(tranType.equalsIgnoreCase("supervisor")){
//                  System.out.println("here in bulkupdate section <<<<<<<<<<<< ");
              query = "select user_id,Full_name from am_gb_user a, approval_limit b where user_status='Active' and is_supervisor='Y' "+
              		  "and user_id!= (select distinct supervisor from PR_REQUISITION where reqnID=? and status='PENDING') "+
              		  "and user_id not in (select supervisorid from am_Approval_Remark where asset_id=? "+
              		  "and status='Approved') and user_id != (select distinct userID from PR_REQUISITION "+
              		  "where reqnID=?) and a.approval_limit=b.level_code and a.user_id<> ? and  b.min_amount<= ? "+
              		  "and b.max_amount >= ? and is_supervisor='Y' and upper(a.User_Status) = 'ACTIVE'  and dept_code = ? "+
              		  "order by full_name";
          	
//              System.out.println("the value of query is ///@@@@@@@@@@@@@@@@@@" + query);
//              System.out.println("the value for user_id: " + user_id+"   min_asset_cost: "+min_asset_cost+"   max_asset_cost: "+max_asset_cost+"    deptCode: "+deptCode+"      tranType: "+tranType+"    reqnId: "+reqnId);
              con = (new DataConnect("legendPlus")).getConnection();
              ps = con.prepareStatement(query);
              ps.setString(1, reqnId);
              ps.setString(2, reqnId);
              ps.setString(3, reqnId);
              ps.setString(4, user_id);
              ps.setDouble(5, min_asset_cost);
              ps.setDouble(6, max_asset_cost);
              ps.setString(7, deptCode);
              rs = ps.executeQuery();
              while (rs.next()) {

//                   System.out.println("fetching recordsssssssssssssssssssssssssssssssssssssssss");
                  out.write("<make>");
                  out.write("<id>");
                  out.write(rs.getString(1));
                  out.write("</id>");
                  out.write("<name>");
                  out.write(rs.getString(2));
                  out.write("</name>");
                  out.write("</make>");


              }//end while
              
              }else{
            	  if(tranType.equalsIgnoreCase("prevSupervisor")){
               query = "select DISTINCT(a.supervisorid),b.full_name from am_approval_remark a, am_gb_user b "+
               		   "where a.supervisorid=b.user_id and a.asset_id=? and a.approvallevel <= ?  "+
               		   "and b.user_id <> ? union select a.userid,b.full_name from PR_REQUISITION a, am_gb_user b "+
               		   "where a.userid=b.user_id and a.reqnid=?";
            	  }
              	
//                  System.out.println("the value of query is ///@@@@@@@@@@@@@@@@@@" + query);
//                  System.out.println("the value for reqnId: " + reqnId+"   apprvLevel: "+apprvLevel+"   user_id: "+user_id+"    deptCode: "+deptCode+"      tranType: "+tranType+"     reqnId: "+reqnId);
                  con = (new DataConnect("legendPlus")).getConnection();
                  ps = con.prepareStatement(query);
                  ps.setString(1, reqnId);
                  ps.setInt(2, Integer.parseInt(apprvLevel));
                  ps.setString(3, user_id);
                  ps.setString(4, reqnId);
                  rs = ps.executeQuery();
                  while (rs.next()) {

//                       System.out.println("fetching recordsssssssssssssssssssssssssssssssssssssssss");
                      out.write("<make>");
                      out.write("<id>");
                      out.write(rs.getString(1));
                      out.write("</id>");
                      out.write("<name>");
                      out.write(rs.getString(2));
                      out.write("</name>");
                      out.write("</make>");


                  }//end while
                  
              }

            }
            }//else

        }//try
        //catch(SQLException ex) { }
        catch (Exception ex) {
        } finally {
            //out.close();
            closeConnection(con, ps, rs);
        }



        out.write("</message>");


        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }
    }

    public void destroy() {
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

    private int getRowCount(String user_id, double asset_cost) {
        int rowCount = 0;
        ResultSet rs = null;
        Connection con = null;

        PreparedStatement ps = null;

        try {



            String query = "select a.user_id, a.user_name from am_gb_user a inner join approval_limit b on a.approval_limit=b.level_code where user_id<> ? and  b.min_amount< ? and b.max_amount > ? and is_supervisor='Y'  and upper(User_Status) = 'ACTIVE' ";

            con = (new DataConnect("legendPlus")).getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, user_id);
            ps.setDouble(2, asset_cost);
            ps.setDouble(3, asset_cost);
            rs = ps.executeQuery();


            while (rs.next()) {
                // Process the row.
                rowCount++;
            }


//            System.out.println("the size of the record set is //////////////////" + rowCount);

        }//try
        //catch(SQLException ex) { }
        catch (Exception ex) {
            System.out.println("Error occurred in limimapprovalservlet class:getRowCount(String user_id, int asset_cost )");
        } finally {
            //out.close();
            closeConnection(con, ps, rs);
        }

        return rowCount;
    }
}
