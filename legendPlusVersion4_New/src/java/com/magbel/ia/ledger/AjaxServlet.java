package com.magbel.ia.ledger;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
//import magma.net.dao.MagmaDBConnection;
import com.magbel.ia.dao.PersistenceServiceDAO;
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

    private PersistenceServiceDAO dbConnection;
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
         dbConnection = new PersistenceServiceDAO();
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

        response.setHeader("Cache-Control", "no-cache");

        String operation = request.getParameter("op");

        if (operation.equalsIgnoreCase("DEPT")) {
            getDeptInBranch(request, response);
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
        }
        else if (operation.equalsIgnoreCase("OTH")) {
        	getOtherPayments(request, response);
        }
        else if (operation.equalsIgnoreCase("LOC")) {
        	getStateCode(request, response);
        }
        else if (operation.equalsIgnoreCase("ADN")) {
        	getAdminNo(request, response);
        }
        else if (operation.equalsIgnoreCase("SOC")) {
        	getFormGLAcct(request, response);
        }        
        else if (operation.equalsIgnoreCase("AOC")) {
        	getFormFee(request, response);
        }        
else {}

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
            con = dbConnection.getConnection("eschool");
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
        String branchid = request.getParameter("bid");
        String deptId = request.getParameter("did");
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query = "SELECT  sectionId"
                       + " FROM sbu_dept_section "
                       + " WHERE [branchid]='" + branchid +
                       "' AND [deptid]='" + deptId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            con = dbConnection.getConnection("eschool");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String iq ="SELECT [Section_Name]"
                           +" FROM [mg_ad_section]"
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
                   out.write(rs2.getString(1));
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
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query =
                "SELECT mg_ad_branch.REGION"
                + " FROM  mg_ad_branch "
               + " WHERE branch_id='" + branchid + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("eschool");
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

                   + " FROM mg_ad_branch  "
                   + " WHERE branch_id='" + branchid + "'";

           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           PreparedStatement ps2 = null;
        ResultSet rs2 = null;
           try {
               con = dbConnection.getConnection("eschool");
               ps = con.prepareStatement(query);
               rs = ps.executeQuery();
               while (rs.next()) {
                   String iq ="SELECT [state_name]"
                              +" FROM [mg_gb_states]"
                           +" WHERE   [state_ID]="+rs.getString(1);
                System.out.println("branch id is "+rs.getString(1));
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
               + "FROM   mg_ad_branch "
               + " WHERE BRANCH_ID='" + branchid + "'";

       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       PreparedStatement ps2 = null;
        ResultSet rs2 = null;
       try {
           con = dbConnection.getConnection("eschool");
           ps = con.prepareStatement(query);
           rs = ps.executeQuery();
           while (rs.next()) {
               String iq ="SELECT [Province]"
                             +" FROM [mg_gb_Province]"
                          +" WHERE   [Province_ID]="+rs.getString(1);
               System.out.println("did "+rs.getString(1));
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

        String query ="select d.dept_id, d.Dept_name from ia_sbu_branch_dept s, mg_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("eschool");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();  
            while (rs.next()) {  
                System.out.println("getDeptInBranch Query: "+query);
                System.out.println("getDeptInBranch bid "+branch_ID);
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

        String query ="select d.dept_code, d.Dept_name from ia_sbu_branch_dept s, mg_ad_department d "+
"where s.deptId = d.Dept_ID and s.branchId ="+branch_ID+" order by d.Dept_name";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        System.out.println("bid "+branch_ID);
        try {
            con = dbConnection.getConnection("eschool");
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

    public void getOtherPayments(HttpServletRequest request,
                               HttpServletResponse response) throws
            ServletException, IOException {
        // legend.objects.BranchDept dept = null;
        String othercode = request.getParameter("bid");
        //String deptId = request.getParameter("did");
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.write("<message>");

        String query =
                "SELECT GL_ACCOUNT FROM IA_OTHER_BREAKDOWN_DETAILS WHERE BREAK_CODE ='" + othercode + "'";
//System.out.println("query In getOtherPayments: "+query);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
        try {
            con = dbConnection.getConnection("eschool");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.write("<account>");
                out.write("<id>");
                out.write(rs.getString(1));
                out.write("</id>");
                out.write("<name>");
                out.write(rs.getString(2));
                out.write("</name>");
                out.write("</account>");
            }
        }

        catch (Exception e) {
            String warning = "WARNING:Error Fetching Other Payment" +
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
    public void getStateCode(HttpServletRequest request,
            HttpServletResponse response) throws
ServletException, IOException {
// legend.objects.BranchDept dept = null;
String Scode = request.getParameter("bid");
//String deptId = request.getParameter("did");
//System.out.println("getStateCode Scode: "+Scode);
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");

String query ="select LOCALGVT_CODE, LOCALGVT_NAME from MG_GB_LOCALGOVT "+
"where STATE_CODE = "+Scode+" order by LOCALGVT_NAME";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
con = dbConnection.getConnection("eschool");
ps = con.prepareStatement(query);
rs = ps.executeQuery();  
while (rs.next()) {  
//System.out.println("getStateCode Query: "+query);
//System.out.println("getStateCode bid "+Scode);
out.write("<localgovt>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2));
out.write("</name>");
out.write("</localgovt>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Local Government in Branch" +
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
    

    public void getAdminNo(HttpServletRequest request,
            HttpServletResponse response) throws
ServletException, IOException {
// legend.objects.BranchDept dept = null;
String SchoolCode = request.getParameter("bid");
//String deptId = request.getParameter("did");
//System.out.println("getAdmin Number: "+SchoolCode);
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");

String query ="select ADMIN_NO, CUSTOMER_NAME from IA_CUSTOMER "+
"where STATUS = 'ACTIVE' AND SCHOOL = '"+SchoolCode+"' order by CUSTOMER_NAME";
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
con = dbConnection.getConnection("eschool");
ps = con.prepareStatement(query);
rs = ps.executeQuery();  
while (rs.next()) {  
//System.out.println("getAdminNo Query: "+query);
//System.out.println("getAdminNo bid "+SchoolCode);
out.write("<ADMIN_NO>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<name>");
out.write(rs.getString(2));
out.write("</name>");
out.write("</ADMIN_NO>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Student Admin No in IA_CUSTOMER table" +
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
public void getFormGLAcct(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
// legend.objects.BranchDept dept = null;
String schoolcode = request.getParameter("bid");
//String deptId = request.getParameter("did");
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");

String query =
"SELECT SALES_ACCT FROM IA_INVENTORY_ITEMS WHERE ITEMTYPE_CODE = '1' AND BRANCH_CODE = '"+schoolcode+"' ";
//System.out.println("query In getFormGLAcct: "+query);
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
con = dbConnection.getConnection("eschool");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<drAccount>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<name>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Fetching Form GL Account" +
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
public void getFormFee(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {
// legend.objects.BranchDept dept = null;
String schoolcode = request.getParameter("bid");
//String deptId = request.getParameter("did");
response.setContentType(CONTENT_TYPE);

PrintWriter out = response.getWriter();
out.write("<message>");

String query =
"SELECT WEIGHT_AVG_COST FROM IA_INVENTORY_ITEMS WHERE ITEMTYPE_CODE = '1' AND BRANCH_CODE = '"+schoolcode+"' ";
//System.out.println("query In getFormGLAcct: "+query);
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
con = dbConnection.getConnection("eschool");
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
out.write("<FatherIncome>");
out.write("<id>");
out.write(rs.getString(1));
out.write("</id>");
out.write("<FatherIncome>");
}
}

catch (Exception e) {
String warning = "WARNING:Error Fetching Form GL Amount" +
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
    
    
}
