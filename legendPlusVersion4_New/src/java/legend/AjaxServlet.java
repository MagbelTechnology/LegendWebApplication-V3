package legend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import magma.net.dao.MagmaDBConnection;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.PrintWriter;


/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Rahman Oloritun
 * @version 1.0
 */
public class AjaxServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/xml";
    private static final String DOC_TYPE = null;

    private MagmaDBConnection dbConnection;
    public AjaxServlet() {
    }

    /**
     * Initializes the servlet.
     *
     * @param config ServletConfig
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
         dbConnection = new MagmaDBConnection();
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response) throws
            ServletException, IOException {
    	String userClass = (String)request.getSession().getAttribute("UserClass");
        response.setHeader("Cache-Control", "no-cache");
        if (!userClass.equals("NULL") || userClass!=null){
        String operation = request.getParameter("op");
//        System.out.println("<<<<<<<====operation: "+operation);
        if (operation.equalsIgnoreCase("DEPT")) {
            getDeptInBranch(request, response);
        } else if (operation.equalsIgnoreCase("BRANCH_CODE")) {
            getDeptFromBranch(request, response);
        } else if (operation.equalsIgnoreCase("CODE")) {
            getCodeOnly(request, response);
        } else if (operation.equalsIgnoreCase("DEPTCODE")) {
        	getDeptInBranchByCode(request, response);
        } else if (operation.equalsIgnoreCase("SECTION")) {
            getSectionInDept(request, response);
         } else if (operation.equalsIgnoreCase("STATE")) {
            getBranchState(request, response);
        }else if (operation.equalsIgnoreCase("REGION")) {
            getBranchRegion(request, response);
        }else if (operation.equalsIgnoreCase("PROVINCE")) {
            getBranchProvince(request, response);
        }else if (operation.equalsIgnoreCase("DEPT2")) {
            getDeptInBranch2(request, response);
        }else if (operation.equalsIgnoreCase("SBU")) {
        	getSbuInBranch(request, response);
        }else if (operation.equalsIgnoreCase("USRDEPT")) {
        	getUsrDeptInBranch(request, response);
        }else if (operation.equalsIgnoreCase("SUPERVISOR")) {
        	getSupervisor(request, response);
        }else if (operation.equalsIgnoreCase("ITEMTYPE")) {
            getItemType(request, response);
        }else if (operation.equalsIgnoreCase("STCAT")) {
        	getSTCategory(request, response);
        }else if (operation.equalsIgnoreCase("SUBCAT")) {
        	getSubCategory(request, response);
        }else if (operation.equalsIgnoreCase("DESC")) {
        	getAssetDesc(request, response);
        }else if (operation.equalsIgnoreCase("BAR")) {
        	getAssetBarCode(request, response);
        }else if (operation.equalsIgnoreCase("DEPTCODE")) {
        	getDeptCodeInBranch(request, response);
        }else if (operation.trim().equalsIgnoreCase("CAT")) {
        	getCAT(request, response);
        }else if (operation.trim().equalsIgnoreCase("TEC")) {
        	getTEC(request, response);
        }else if (operation.trim().equalsIgnoreCase("MAIL")) {
        	getTechMail(request, response);
        }else if (operation.trim().equalsIgnoreCase("UMAIL")) {
        	getUSERID(request, response);
        }else if (operation.equalsIgnoreCase("BANK")) {
        	getBank(request, response); 
        }else if (operation.equalsIgnoreCase("BANK2")) {
        	getBank2(request, response);
        }
        
        
else {}
    }
    }

    /*
    public void getDeptInBranch(HttpServletRequest request,
                                HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchid = request.getParameter("bid");
        //String deptId = request.getParameter("did");
        int branch_ID =0;
        if(branchid != null){
         branch_ID = Integer.parseInt(branchid);
        }

        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query = "SELECT deptId"
                       + " FROM sbu_branch_dept "
                       + " WHERE branchId =" + branch_ID;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        System.out.println("bid "+branch_ID);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String iq ="SELECT [Dept_name] FROM [am_ad_department]"
                           +" WHERE  [Dept_ID]= "+rs.getInt(1)+" order by dept_name asc";
                 System.out.println("did "+rs.getInt(1));
                ps2 = con.prepareStatement(iq);
                rs2 = ps2.executeQuery();
                if(rs2.next()){
                out.write("<department>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs2.getString(1).replaceAll("&", "&amp;"));
                out.write("</name>");
                out.write("</department>");}
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error departments in Branch" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }
*/
    public void getSectionInDept(HttpServletRequest request,
                                 HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
    	String bidSplit[] = new String[2];
        String bidValue = request.getParameter("bid");
//        System.out.println("<<<<bidValue: "+bidValue);
 //       String deptId = request.getParameter("did");
        bidSplit = bidValue.split("_");
//        System.out.println("<<<<bidValue>>>>>: "+bidSplit[0]);
        String branchid = bidSplit[0];
        String deptId = bidSplit[1];
//        System.out.println("<<<<branchid: "+branchid+"  deptId: "+deptId);
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query = "SELECT  sectionId"
                       + " FROM sbu_dept_section "
                       + " WHERE [branchid]='" + branchid +
                       "' AND [deptid]='" + deptId + "'";
 //       System.out.println("query in getSectionInDept: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String iq ="SELECT [Section_Name]+'-'+[Section_Code]"
                           +" FROM [am_ad_section]"
                          +" WHERE  [Section_ID]="+rs.getString(1);
                System.out.println("did "+rs.getString(1));
               ps2 = con.prepareStatement(iq);
               rs2 = ps2.executeQuery();
               if(rs2.next()){
                   out.write("<section>");
                   out.write("<id>");
                   out.write(rs.getString(1));
                   out.write("</id>");
                   out.write("<name>");
            //       out.write(rs2.getString(1));
                   out.write(rs2.getString(1).replaceAll("&", "&amp;"));
   //                System.out.println("rs2.getString(1) "+rs2.getString(1).replaceAll("&", "&amp;"));
                   out.write("</name>");
                   out.write("</section>");
               }
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error Fetching sections in dept" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }

    public void getBranchRegion(HttpServletRequest request,
                               HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchid = request.getParameter("bid");
        //String deptId = request.getParameter("did");
//        System.out.println("<<<<branchid: "+branchid);
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query =
                "SELECT a.REGION_CODE, b.REGION_NAME+'-'+a.REGION_CODE"
                + " FROM  am_ad_branch a, am_ad_region b "
               + " WHERE a.REGION_CODE = b.REGION_CODE AND branch_id='" + branchid + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.write("<region>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2));
                out.write("</name>");
                out.write("</region>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error Fetching Region" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }
    public void getBranchState(HttpServletRequest request,
                                  HttpServletResponse response) throws
               ServletException, IOException {
           // legend.objects.BranchDept dept = null;
           String branchid = request.getParameter("bid");
           //String deptId = request.getParameter("did");
           response.setContentType(CONTENT_TYPE);
           response.setHeader("Cache-Control", "no-cache");
           
           PrintWriter out = response.getWriter();
           out.write("<message>");

           String query =
                "SELECT STATE"

                   + " FROM am_ad_branch  "
                   + " WHERE branch_id='" + branchid + "'";

           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           PreparedStatement ps2 = null;
        ResultSet rs2 = null;
           try {
               con = dbConnection.getConnection("legendPlus");
               ps = con.prepareStatement(query);
               rs = ps.executeQuery();
               while (rs.next()) {
                   String iq ="SELECT [state_name]"
                              +" FROM [am_gb_states]"
                           +" WHERE   [state_ID]="+rs.getString(1);
//                System.out.println("branch id is "+rs.getString(1));
               ps2 = con.prepareStatement(iq);
               rs2 = ps2.executeQuery();
               if(rs2.next()){
                   out.write("<state>");
                   out.write("<id>");
                   out.write(rs.getString(1));
                   out.write("</id>");
                   out.write("<name>");
                   out.write(rs2.getString(1));
                   out.write("</name>");
                   out.write("</state>");
               }
               }
           }

           catch (Exception e) {
               String warning = "WARNING:Error Fetching state" +
                                " ->" + e.getMessage();
               System.out.println(warning);
           } finally {
               dbConnection.closeConnection(con, ps, rs);
           }
           out.write("</message>");

           if (DOC_TYPE != null) {
               out.println(DOC_TYPE);
           }

    }
    public void getBranchProvince(HttpServletRequest request,
                              HttpServletResponse response) throws
           ServletException, IOException {
       // legend.objects.BranchDept dept = null;
       String branchid = request.getParameter("bid");
       //String deptId = request.getParameter("did");
       response.setContentType(CONTENT_TYPE);

       PrintWriter out = response.getWriter();
       out.write("<message>");

       String query = "SELECT PROVINCE "
               + "FROM   am_ad_branch "
               + " WHERE BRANCH_ID='" + branchid + "'";

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       PreparedStatement ps2 = null;
        ResultSet rs2 = null;
       try {
           con = dbConnection.getConnection("legendPlus");
           ps = con.prepareStatement(query);
           rs = ps.executeQuery();
           while (rs.next()) {
               String iq ="SELECT [Province]"
                             +" FROM [am_gb_Province]"
                          +" WHERE   [Province_ID]="+rs.getString(1);
 //              System.out.println("did "+rs.getString(1));
              ps2 = con.prepareStatement(iq);
              rs2 = ps2.executeQuery();
              if(rs2.next()){
                  out.write("<province>");
                  out.write("<id>");
                  out.write(rs.getString(1));
                  out.write("</id>");
                  out.write("<name>");
                  out.write(rs2.getString(1));
                  out.write("</name>");
                  out.write("</province>");
              }
           }
       }

       catch (Exception e) {
           String warning = "WARNING:Error Fetching Province" +
                            " ->" + e.getMessage();
           System.out.println(warning);
       } finally {
           dbConnection.closeConnection(con, ps, rs);
       }
       out.write("</message>");

       if (DOC_TYPE != null) {
           out.println(DOC_TYPE);
       }

}


      public void getDeptInBranch(HttpServletRequest request,
                                HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchid = request.getParameter("bid");
        //String deptId = request.getParameter("did");
        int branch_ID =0;
        if(branchid != null){
         branch_ID = Integer.parseInt(branchid);
        }

        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");  

        String query ="select d.dept_id, d.Dept_name+'-'+d.dept_Code from sbu_branch_dept s, am_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
//       System.out.println("getDeptInBranch query: "+query);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	out.write("<department>");
                out.write("<id>");
                out.write(rs.getString(1));
//                System.out.println("getDeptInBranch String 1: "+rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
//                System.out.println("getDeptInBranch String 2: "+rs.getString(2));
                out.write("</name>");
                out.write("</department>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error departments in Branch" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }


      public void getDeptFromBranch(HttpServletRequest request,
                                HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchCode = request.getParameter("bid");
        //String deptId = request.getParameter("did");

        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");  

        String query ="select d.Dept_code, d.Dept_name+'-'+d.Dept_code from sbu_branch_dept s, am_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchCode ='"+branchCode+"' order by d.Dept_name";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
//       System.out.println("getDeptInBranch query: "+query);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	out.write("<department>");
                out.write("<id>");
                out.write(rs.getString(1));
//                System.out.println("getDeptInBranch String 1: "+rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
//                System.out.println("getDeptInBranch String 2: "+rs.getString(2));
                out.write("</name>");
                out.write("</department>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error departments in Branch" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }


    public void getDeptInBranch2(HttpServletRequest request,
                                HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchid = request.getParameter("bid");
        //String deptId = request.getParameter("did");
        int branch_ID =0;
        if(branchid != null){
         branch_ID = Integer.parseInt(branchid);
        }

        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query ="select d.dept_code, d.Dept_name+'-'+d.dept_code from sbu_branch_dept s, am_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

//        System.out.println("bid "+branch_ID);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                out.write("<department>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
                out.write("</name>");
                out.write("</department>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error departments in Branch" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }

    public void getSbuInBranch(HttpServletRequest request,
                              HttpServletResponse response) throws
          ServletException, IOException {
      // legend.objects.BranchDept dept = null;
      String branchid = request.getParameter("bid");
      //String deptId = request.getParameter("did");
      int branch_ID =0;
      if(branchid != null){
       branch_ID = Integer.parseInt(branchid);
      }

      response.setContentType(CONTENT_TYPE);

      PrintWriter out = response.getWriter();
      out.write("<message>");
//      System.out.println("Inside getSbuInBranch Today 18-02-2015");
   //   String query ="select d.dept_id, d.Dept_name from sbu_branch_dept s, am_ad_department d "+
//"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
      String query = "select sbu_code, SBU_NAME from AM_SBU_ATTACHEMENT where ATTACH_ID = "+branch_ID+" order by sbu_name" ;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

 //     System.out.println("bid "+branch_ID);
      try {
          con = dbConnection.getConnection("legendPlus");
          ps = con.prepareStatement(query);
          rs = ps.executeQuery();
          while (rs.next()) {

              out.write("<newsbu_code>");
              out.write("<id>");
              out.write(rs.getString(1));
              out.write("</id>");
              out.write("<name>");
              out.write(rs.getString(2).replaceAll("&", "&amp;"));
              out.write("</name>");
              out.write("</newsbu_code>");
          }
      }

      catch (Exception e) {
          String warning = "WARNING:Error departments in Branch" +
                           " ->" + e.getMessage();
          System.out.println(warning);
      } finally {
          dbConnection.closeConnection(con, ps, rs);
      }
      out.write("</message>");

      if (DOC_TYPE != null) {
          out.println(DOC_TYPE);
      }

  }

    public void getUsrDeptInBranch(HttpServletRequest request,
                              HttpServletResponse response) throws
          ServletException, IOException {
      // legend.objects.BranchDept dept = null;
      String branchid = request.getParameter("bid");
      //String deptId = request.getParameter("did");
      int branch_ID =0;
      if(branchid != null){
       branch_ID = Integer.parseInt(branchid);
      }

      response.setContentType(CONTENT_TYPE);

      PrintWriter out = response.getWriter();
      out.write("<message>");  
      String query =  "SELECT DEPT_CODE,DEPT_NAME FROM am_ad_department a, sbu_branch_dept b "
      		+ "WHERE a.DEPT_ID = b.DEPTID AND a.DEPT_STATUS='ACTIVE'  AND b.BRANCHID = "+branch_ID+" ";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

//     System.out.println("getDeptInBranch query: "+query);
      try {
          con = dbConnection.getConnection("legendPlus");
          ps = con.prepareStatement(query);
          rs = ps.executeQuery();
          while (rs.next()) {
          	out.write("<department>");
              out.write("<id>");
              out.write(rs.getString(1));
 //             System.out.println("getDeptInBranch String 1: "+rs.getString(1));
              out.write("</id>");
              out.write("<name>");
              out.write(rs.getString(2).replaceAll("&", "&amp;"));
 //             System.out.println("getDeptInBranch String 2: "+rs.getString(2));
              out.write("</name>");
              out.write("</department>");
          }
      }

      catch (Exception e) {
          String warning = "WARNING:Error departments in Branch" +
                           " ->" + e.getMessage();
          System.out.println(warning);
      } finally {
          dbConnection.closeConnection(con, ps, rs);
      }
      out.write("</message>");

      if (DOC_TYPE != null) {
          out.println(DOC_TYPE);
      }

  }


    public void getSupervisor(HttpServletRequest request,
                                HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String branchid = request.getParameter("bid");
        //String deptId = request.getParameter("did");
        int reqnBranchIdInt = branchid ==null?0:Integer.parseInt(branchid);

      //  String userID = request.getParameter("userID");
        HttpSession session = request.getSession();
        String userbrnchid = (String)session.getAttribute("UserCenter");
        String userID = (String)session.getAttribute("CurrentUser");
        int userbrnchidInt = userbrnchid ==null?0:Integer.parseInt(userbrnchid);
        response.setContentType(CONTENT_TYPE);
//        System.out.println("getSupervisor reqnBranchIdInt: "+reqnBranchIdInt+"    userbrnchidInt: "+userbrnchidInt);
        PrintWriter out = response.getWriter();
        out.write("<message>");

//        String query ="select d.dept_code, d.Dept_name from sbu_branch_dept s, am_ad_department d "+
//"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
        String query=
	"select user_id,full_name from am_gb_user where (branch ="+reqnBranchIdInt+" or " +
			"branch ="+userbrnchidInt+") and user_status='Active' and is_supervisor='Y' and user_ID!="+userID+" order by full_name ";
 //       System.out.println("getSupervisor query: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

//        System.out.println("bid "+branchid);
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                out.write("<supervisor>");
                out.write("<id>");
                out.write(rs.getString(1));
  //              System.out.println("getSupervisor String 1: "+rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2).replaceAll("&", "&amp;"));
  //              System.out.println("getSupervisor String 2: "+rs.getString(2));
                out.write("</name>");
                out.write("</supervisor>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error Supervisor in Branch" +
                             " ->" + e.getMessage();
            System.out.println(warning);
        } finally {
            dbConnection.closeConnection(con, ps, rs);
        }
        out.write("</message>");

        if (DOC_TYPE != null) {
            out.println(DOC_TYPE);
        }

    }

public void getItemType(HttpServletRequest request,
            HttpServletResponse response) throws
ServletException, IOException {
// legend.objects.BranchDept dept = null;
String itemTypeCode = request.getParameter("bid");
//String deptId = request.getParameter("did");
//System.out.println("<<<<<<<====itemTypeCode: "+itemTypeCode);
int itemTypeId =0;
if(itemTypeCode != null){
	itemTypeId = Integer.parseInt(itemTypeCode);
}

response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");  

String query ="select ITEM_CODE, DESCRIPTION+'-'+ITEM_CODE from ST_INVENTORY_ITEMS WHERE ITEMTYPE_CODE  ="+itemTypeCode+" order by DESCRIPTION";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

//System.out.println("getDeptInBranch query: "+query);
try {
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<description>");
out.write("<id>");
out.write(rs.getString(1));
//System.out.println("getDeptInBranch String 1: "+rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2).replaceAll("&", "&amp;"));
//System.out.println("getDeptInBranch String 2: "+rs.getString(2));
out.write("</name>");
out.write("</description>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Item Type Code" +
         " ->" + e.getMessage();
System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

public void getSTCategory(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
//legend.objects.BranchDept dept = null;
String categoryCode = request.getParameter("bid");
//String deptId = request.getParameter("did");
//System.out.println("<<<<<<<====itemTypeCode: "+itemTypeCode);

response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");  

String query ="SELECT ITEMTYPE_CODE, NAME FROM ST_ITEMTYPE WHERE STATUS = 'A' AND CATEGORY_CODE = '"+categoryCode+"'";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

//System.out.println("getDeptInBranch query: "+query);
try {
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<itemName>");
out.write("<id>");
out.write(rs.getString(1));
//System.out.println("getDeptInBranch String 1: "+rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2).replaceAll("&", "&amp;"));
//System.out.println("getDeptInBranch String 2: "+rs.getString(2));
out.write("</name>");
out.write("</itemName>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Category Code" +
     " ->" + e.getMessage();
System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

public void getSubCategory(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
String subcategoryId = request.getParameter("bid");

response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");  

String query ="SELECT DESCRIPTION,DESCRIPTION+'-'+SUB_CATEGORY_CODE AS NARRATION  FROM AM_ASSET_DESCRIPTION "+
"WHERE SUB_CATEGORY_CODE IN (Select SUB_CATEGORY_CODE FROM am_ad_sub_category  where SUB_CATEGORY_ID = '"+subcategoryId+"') ORDER BY DESCRIPTION";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

//System.out.println("getDeptInBranch query: "+query);
try {
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<description>");
out.write("<id>");
out.write(rs.getString(1));
//System.out.println("getDeptInBranch String 1: "+rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2).replaceAll("&", "&amp;"));
//System.out.println("getDeptInBranch String 2: "+rs.getString(2));
out.write("</name>");
out.write("</description>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Sub Category Code" +
     " ->" + e.getMessage();
System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

public void getAssetDesc(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
String assetId = request.getParameter("bid");

response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");  

String query ="SELECT DESCRIPTION,DESCRIPTION AS NARRATION  FROM AM_ASSET WHERE ASSET_ID = '"+assetId+"'";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

//System.out.println("getAssetDesc query: "+query);
try {
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<description>");
out.write("<id>");
out.write(rs.getString(1));
//System.out.println("getAssetDesc String 1: "+rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2).replaceAll("&", "&amp;"));
//System.out.println("getAssetDesc String 2: "+rs.getString(2));
out.write("</name>");
out.write("</description>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Asset Description" +
     " ->" + e.getMessage();
System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

public void getAssetBarCode(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
String assetId = request.getParameter("bid");

response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");  

String query ="SELECT BAR_CODE,BAR_CODE  FROM AM_ASSET WHERE ASSET_ID = '"+assetId+"'";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

//System.out.println("getAssetBarCode query: "+query);
try {
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<description>");
out.write("<id>");
out.write(rs.getString(1));
//System.out.println("getAssetBarCode String 1: "+rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2).replaceAll("&", "&amp;"));
//System.out.println("getAssetBarCode String 2: "+rs.getString(2));
out.write("</name>");
out.write("</description>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Asset Description" +
     " ->" + e.getMessage();
System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

public void getDeptCodeInBranch(HttpServletRequest request,
                          HttpServletResponse response) throws
      ServletException, IOException {
  // legend.objects.BranchDept dept = null;
  String branchCode = request.getParameter("bid");
  //String deptId = request.getParameter("did");
  response.setContentType(CONTENT_TYPE);

  PrintWriter out = response.getWriter();
  out.write("<message>");  

  String query ="select d.Dept_code, d.Dept_name+'-'+d.Dept_code from sbu_branch_dept s, am_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchCode ="+branchCode+" order by d.Dept_name";
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

// System.out.println("getDeptInBranch query: "+query);
  try {
      con = dbConnection.getConnection("legendPlus");
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
      	out.write("<department>");
          out.write("<id>");
          out.write(rs.getString(1));
//          System.out.println("getDeptInBranch String 1: "+rs.getString(1));
          out.write("</id>");
          out.write("<name>");
          out.write(rs.getString(2).replaceAll("&", "&amp;"));
//          System.out.println("getDeptInBranch String 2: "+rs.getString(2));
          out.write("</name>");
          out.write("</department>");
      }
  }

  catch (Exception e) {
      String warning = "WARNING:Error departments in Branch" +
                       " ->" + e.getMessage();
      System.out.println(warning);
  } finally {
      dbConnection.closeConnection(con, ps, rs);
  }
  out.write("</message>");

  if (DOC_TYPE != null) {
      out.println(DOC_TYPE);
  }

}

public void getDeptInBranchByCode(HttpServletRequest request,
                          HttpServletResponse response) throws
      ServletException, IOException {
	String branchDeptSplit[] = new String[2];
    String branchDepValue = request.getParameter("bid");
//    System.out.println("<<<<branchDepValue>>>>>: "+branchDepValue);
    branchDeptSplit = branchDepValue.split("_");
//    System.out.println("<<<<branchDepValue>>>>>: "+branchDeptSplit[0]);
    String branchCode = branchDeptSplit[0];
    String deptCode = branchDeptSplit[1];
    
  response.setContentType(CONTENT_TYPE);

  PrintWriter out = response.getWriter();
  out.write("<message>");  

  String query ="select d.dept_Code, d.Dept_name+'-'+d.dept_Code from sbu_branch_dept s, am_ad_department d "+
"where s.deptCode = d.Dept_Code and s.branchCode ="+branchCode+" and s.deptCode = '"+deptCode+"' order by d.Dept_name";
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;  

// System.out.println("getDeptInBranch query: "+query);
  try {
      con = dbConnection.getConnection("legendPlus");
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
      	out.write("<department>");
          out.write("<id>");
          out.write(rs.getString(1));
//          System.out.println("getDeptInBranch String 1: "+rs.getString(1));
          out.write("</id>");
          out.write("<name>");
          out.write(rs.getString(2).replaceAll("&", "&amp;"));
//          System.out.println("getDeptInBranch String 2: "+rs.getString(2));
          out.write("</name>");
          out.write("</department>");
      }
  }

  catch (Exception e) {
      String warning = "WARNING:Error departments in Branch" +
                       " ->" + e.getMessage();
      System.out.println(warning);
  } finally {
      dbConnection.closeConnection(con, ps, rs);
  }
  out.write("</message>");

  if (DOC_TYPE != null) {
      out.println(DOC_TYPE);
  }

}



public void getCodeOnly(HttpServletRequest request,
                          HttpServletResponse response) throws
      ServletException, IOException {
  // legend.objects.BranchDept dept = null;
  String code = request.getParameter("bid");
  //String deptId = request.getParameter("did");

  response.setContentType(CONTENT_TYPE);

  PrintWriter out = response.getWriter();
  out.write("<message>");  

  String query ="SELECT CODE,DESCRIPTION FROM ST_GL_PROJECT "+
"WHERE CODE ='"+code+"' ";
//  System.out.println(" ======>query in getCodeOnly "+query);
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

// System.out.println("getDeptInBranch query: "+query);
  try {
      con = dbConnection.getConnection("legendPlus");
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
      	out.write("<codeDescription>");
          out.write("<id>");
          out.write(rs.getString(1));
//          System.out.println("getDeptInBranch String 1: "+rs.getString(1));
          out.write("</id>");
          out.write("<name>");
          out.write(rs.getString(2).replaceAll("&", "&amp;"));
//          System.out.println("getDeptInBranch String 2: "+rs.getString(2));
          out.write("</name>");
          out.write("</codeDescription>");
      }
  }

  catch (Exception e) {
      String warning = "WARNING:Error Project Code in getCodeOnly servlet" +
                       " ->" + e.getMessage();
      System.out.println(warning);
  } finally {
      dbConnection.closeConnection(con, ps, rs);
  }
  out.write("</message>");

  if (DOC_TYPE != null) {
      out.println(DOC_TYPE);
  }

}

public void getCAT(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
	
 
	//legend.objects.BranchDept dept = null;
	String Category_Code = request.getParameter("bid");  
	//String deptId = request.getParameter("did");
//	  System.out.println("==== Category getCAT=== "+Category_Code);
//	int bankid =0;
//	if(Category_Code != null){
//		bankid = Integer.parseInt(Category_Code);
//	}
//	
//	String query ="SELECT sub_category_code,sub_category_desc FROM HD_COMPLAIN_SUBCATEGORY WHERE   sub_category_code = "+Category_Code+""; 
	String query ="SELECT sub_category_desc,sub_category_desc FROM HD_COMPLAIN_SUBCATEGORY WHERE   category_code = '"+Category_Code+"'";
	
	  
	response.setContentType(CONTENT_TYPE);
	PrintWriter out = response.getWriter();
	out.write("<message>");
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try{
		// con = dbConnection.getConnection("helpDesk"); 
//		   con = mgDbCon.getConnection("helpDesk");
		      con = dbConnection.getConnection("legendPlus");
//	    	  System.out.println("==== query getCAT=== "+query);
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
	while (rs.next()) {
		out.write("<subcategory>");
		out.write("<id>");
		out.write(rs.getString(1));
//		System.out.println("======= "+rs.getString(2));
		out.write("</id>");
		out.write("<name>");
		out.write(rs.getString(2));
//		System.out.println("======= "+rs.getString(2));
		out.write("</name>");
		out.write("</subcategory>"); 
	} 
	    }
	catch (Exception e) {
	String warning = "WARNING:Error Fetching Category" +
	          " ->" + e.getMessage();
	e.printStackTrace();
	//  System.out.println(warning);
	} finally {
	dbConnection.closeConnection(con, ps, rs);
	}
	out.write("</message>");

	if (DOC_TYPE != null) {
	out.println(DOC_TYPE);
	}
	
}

public void getTEC(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
	
    
	//legend.objects.BranchDept dept = null;
	String Category_Code = request.getParameter("bid");  
	//String deptId = request.getParameter("did");
  System.out.println("==== Category getTEC===03/11/2011 "+Category_Code);
//	int bankid =0;
//	if(Category_Code != null){
//		bankid = Integer.parseInt(Category_Code);
//	}
	
	String query ="SELECT Dept_name,UnitHead FROM AM_AD_DEPARTMENT WHERE   Dept_code = '"+Category_Code+"'"; 
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String name = "",head = "";

	try{
		// con = dbConnection.getConnection("helpDesk"); 
		 con = dbConnection.getConnection("legendPlus");
//	  System.out.println("==== query getTEC=== "+query);
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
    	if(rs.next()) {
    		name = rs.getString(1);
    		head = rs.getString(2);	    		
    	} 
	 }catch (Exception e) {
	String warning = "WARNING:Error Fetching Category ->" + e.getMessage();
	e.printStackTrace();
	//  System.out.println(warning);
	} finally {
	dbConnection.closeConnection(con, ps, rs);
	}
	
	response.setContentType(CONTENT_TYPE);
	PrintWriter out = response.getWriter();
	out.write("<technician>");
	out.write("<name>"+name+"</name>");
	out.write("<head>"+head+"</head>");
	out.write("</technician>"); 
	if (DOC_TYPE != null) {
	out.println(DOC_TYPE);
	}

}
public void getTechMail(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
	
      
	//legend.objects.BranchDept dept = null;
	String Category_Code = request.getParameter("bid");  
	//String deptId = request.getParameter("did");
//	//  System.out.println("==== Category getTechMail===07/11/2011 "+Category_Code);
//	int bankid =0;
//	if(Category_Code != null){
//		bankid = Integer.parseInt(Category_Code);
//	}
//	
	String query ="SELECT Dept_name,email FROM AM_AD_DEPARTMENT WHERE   Dept_code = '"+Category_Code+"'"; 
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String name = "",head = "";

	try{
		// con = dbConnection.getConnection("helpDesk"); 
		 con = dbConnection.getConnection("legendPlus");
//	  System.out.println("==== query getTechMail=== "+query);
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
    	if(rs.next()) {
    		name = rs.getString(1);
    		head = rs.getString(2);	    		
    	} 
	 }catch (Exception e) {
	String warning = "WARNING:Error Fetching Unit Head Mail ->" + e.getMessage();
	e.printStackTrace();
	//  System.out.println(warning);
	} finally {
	dbConnection.closeConnection(con, ps, rs);
	}
	
	response.setContentType(CONTENT_TYPE);
	PrintWriter out = response.getWriter();
	out.write("<notifyEmail>");
	out.write("<name>"+name+"</name>");
	out.write("<head>"+head+"</head>");
	out.write("</notifyEmail>"); 
	if (DOC_TYPE != null) {
	out.println(DOC_TYPE);
	}

}
   
public void getUSERID(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
	
      
	//legend.objects.BranchDept dept = null;
	String UserName = request.getParameter("bid");  
	//String deptId = request.getParameter("did");
//	//  System.out.println("==== Category getTechMail===07/11/2011 "+Category_Code);
	String query = "SELECT  User_Id,email   FROM   AM_GB_USER where User_Name = '"+UserName+"'"; 
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String name = "",head = "";

	try{
		// con = dbConnection.getConnection("helpDesk"); 
		 con = dbConnection.getConnection("legendPlus");
//	 System.out.println("==== query getUSERID=== "+query);
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
    	if(rs.next()) {
    		name = rs.getString(1).trim();
    		head = rs.getString(2).trim();	    		
    	} 
	 }catch (Exception e) {
	String warning = "WARNING:Error Fetching Unit Head Mail ->" + e.getMessage();
	e.printStackTrace();
	//  System.out.println(warning);
	} finally {
	dbConnection.closeConnection(con, ps, rs);
	}
	
	response.setContentType(CONTENT_TYPE);
	PrintWriter out = response.getWriter();
	out.write("<returnmail>");
	out.write("<name>"+name+"</name>");
	out.write("<head>"+head+"</head>");
	out.write("</returnmail>"); 
	if (DOC_TYPE != null) {
	out.println(DOC_TYPE);
	}

}    

public void getBank(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
	//  System.out.println("---operation-BANK- ");
//legend.objects.BranchDept dept = null;
String bank_Code = request.getParameter("bid");  
//String deptId = request.getParameter("did");
int bankid =0;
if(bank_Code != null){
bankid = Integer.parseInt(bank_Code);
}
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");
String query ="SELECT bank_code,bank_name FROM am_gb_bank WHERE   bank_code !="+bankid;
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try{
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<Issuance>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2));
out.write("</name>");
out.write("</Issuance>"); 
} 
}
catch (Exception e) {
String warning = "WARNING:Error Fetching bank" +
      " ->" + e.getMessage();
e.printStackTrace();
//System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}


public void getBank2(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
//legend.objects.BranchDept dept = null;
String bank_Code = request.getParameter("bid");  
//String deptId = request.getParameter("did");
int bankid =0;
if(bank_Code != null){
bankid = Integer.parseInt(bank_Code);
}
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");
String query ="SELECT bank_code,bank_name FROM am_gb_bank WHERE   bank_code ="+bankid;
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try{
con = dbConnection.getConnection("legendPlus");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<Issuance>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2));
out.write("</name>");
out.write("</Issuance>"); 
} 
}
catch (Exception e) {
String warning = "WARNING:Error Fetching bank" +
      " ->" + e.getMessage();
e.printStackTrace();
//System.out.println(warning);
} finally {
dbConnection.closeConnection(con, ps, rs);
}
out.write("</message>");

if (DOC_TYPE != null) {
out.println(DOC_TYPE);
}

}

}
