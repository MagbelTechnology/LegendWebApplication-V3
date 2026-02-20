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
    public void getSectionInDeptOld(HttpServletRequest request,
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
    
    public void getSectionInDept(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");

PrintWriter out = response.getWriter();
out.write("<message>");

String bidValue = request.getParameter("bid");

if (bidValue == null || !bidValue.contains("_")) {
out.write("</message>");
return;
}

String[] bidSplit = bidValue.split("_");
String branchId = bidSplit[0];
String deptId = bidSplit[1];

String query =
"SELECT s.sectionId, a.Section_Name + '-' + a.Section_Code AS SectionFullName " +
"FROM sbu_dept_section s " +
"JOIN am_ad_section a ON s.sectionId = a.Section_ID " +
"WHERE s.branchid = ? AND s.deptid = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchId);
ps.setString(2, deptId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {

String sectionId = rs.getString("sectionId");
String sectionName = rs.getString("SectionFullName");

if (sectionName != null) {
   sectionName = sectionName
           .replace("&", "&amp;")
           .replace("<", "&lt;")
           .replace(">", "&gt;");
}

out.write("<section>");
out.write("<id>" + sectionId + "</id>");
out.write("<name>" + sectionName + "</name>");
out.write("</section>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error Fetching sections in dept -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
}

    public void getBranchRegionOld(HttpServletRequest request,
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
    
    public void getBranchRegion(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
PrintWriter out = response.getWriter();
out.write("<message>");

String branchId = request.getParameter("bid");

if (branchId == null || branchId.trim().isEmpty()) {
out.write("</message>");
return;
}

String query =
"SELECT a.REGION_CODE, b.REGION_NAME + '-' + a.REGION_CODE AS REGION_FULL " +
"FROM am_ad_branch a " +
"JOIN am_ad_region b ON a.REGION_CODE = b.REGION_CODE " +
"WHERE a.branch_id = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {

String regionCode = rs.getString("REGION_CODE");
String regionName = rs.getString("REGION_FULL");

if (regionName != null) {
    regionName = regionName
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
}

out.write("<region>");
out.write("<id>" + regionCode + "</id>");
out.write("<name>" + regionName + "</name>");
out.write("</region>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error Fetching Region -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
}
    
    public void getBranchStateOld(HttpServletRequest request,
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
    
    public void getBranchState2Old(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchId = request.getParameter("bid");

if (branchId == null || branchId.trim().isEmpty()) {
out.write("</message>");
return;
}

String query =
"SELECT b.STATE, s.state_name " +
"FROM am_ad_branch b " +
"JOIN am_gb_states s ON b.STATE = s.state_ID " +
"WHERE b.branch_id = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {

 String stateId = rs.getString("STATE");
 String stateName = rs.getString("state_name");

 if (stateName != null) {
     stateName = stateName
             .replace("&", "&amp;")
             .replace("<", "&lt;")
             .replace(">", "&gt;");
 }

 out.write("<state>");
 out.write("<id>" + stateId + "</id>");
 out.write("<name>" + stateName + "</name>");
 out.write("</state>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error Fetching state -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
}
    
    public void getBranchState(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchId = request.getParameter("bid");

if (branchId == null || branchId.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query =
"SELECT b.STATE, s.state_name " +
"FROM am_ad_branch b " +
"JOIN am_gb_states s ON b.STATE = s.state_ID " +
"WHERE b.branch_id = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {
 String stateId = rs.getString("STATE");
 String stateName = escapeXml(rs.getString("state_name"));

 out.write("<state>");
 out.write("<id>" + stateId + "</id>");
 out.write("<name>" + stateName + "</name>");
 out.write("</state>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error Fetching state -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}
    
    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
    
    public void getBranchProvinceOld(HttpServletRequest request,
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
    
    public void getBranchProvince(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchId = request.getParameter("bid");

if (branchId == null || branchId.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query =
"SELECT b.PROVINCE, p.Province " +
"FROM am_ad_branch b " +
"JOIN am_gb_Province p ON b.PROVINCE = p.Province_ID " +
"WHERE b.BRANCH_ID = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {

String provinceId = rs.getString("PROVINCE");
String provinceName = escapeXml(rs.getString("Province"));

out.write("<province>");
out.write("<id>" + provinceId + "</id>");
out.write("<name>" + provinceName + "</name>");
out.write("</province>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error Fetching Province -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}


      public void getDeptInBranchOld(HttpServletRequest request,
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

      public void getDeptInBranch(HttpServletRequest request,
              HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchIdParam = request.getParameter("bid");

if (branchIdParam == null) {
out.write("</message>");
out.flush();
out.close();
return;
}

int branchId;
try {
branchId = Integer.parseInt(branchIdParam);
} catch (NumberFormatException e) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query =
"SELECT d.dept_id, d.Dept_name + '-' + d.dept_Code AS DeptFullName " +
"FROM sbu_branch_dept s " +
"JOIN am_ad_department d ON s.deptId = d.Dept_ID " +
"WHERE s.branchId = ? " +
"ORDER BY d.Dept_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, branchId);

try (ResultSet rs = ps.executeQuery()) {

while (rs.next()) {

  String deptId = rs.getString("dept_id");
  String deptName = escapeXml(rs.getString("DeptFullName"));

  out.write("<department>");
  out.write("<id>" + deptId + "</id>");
  out.write("<name>" + deptName + "</name>");
  out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching departments in Branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}

      public void getDeptFromBranchOld(HttpServletRequest request,
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
      public void getDeptFromBranch(HttpServletRequest request,
              HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchCode = request.getParameter("bid");
if (branchCode == null || branchCode.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query =
"SELECT d.Dept_code, d.Dept_name + '-' + d.Dept_code AS DeptFullName " +
"FROM sbu_branch_dept s " +
"JOIN am_ad_department d ON s.deptId = d.Dept_ID " +
"WHERE s.branchCode = ? " +
"ORDER BY d.Dept_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchCode);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String deptId = rs.getString("Dept_code");
String deptName = escapeXml(rs.getString("DeptFullName"));

out.write("<department>");
out.write("<id>" + deptId + "</id>");
out.write("<name>" + deptName + "</name>");
out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching departments in Branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}


    public void getDeptInBranch2Old(HttpServletRequest request,
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
    
    public void getDeptInBranch2(HttpServletRequest request,
            HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchIdStr = request.getParameter("bid");
if (branchIdStr == null || branchIdStr.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

int branchId = 0;
try {
branchId = Integer.parseInt(branchIdStr);
} catch (NumberFormatException e) {
System.out.println("WARNING: Invalid branch ID -> " + branchIdStr);
out.write("</message>");
out.flush();
out.close();
return;
}

String query =
"SELECT d.dept_code, d.Dept_name + '-' + d.dept_code AS DeptFullName " +
"FROM sbu_branch_dept s " +
"JOIN am_ad_department d ON s.deptId = d.Dept_ID " +
"WHERE s.branchId = ? " +
"ORDER BY d.Dept_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, branchId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String deptCode = rs.getString("dept_code");
String deptName = escapeXml(rs.getString("DeptFullName"));

out.write("<department>");
out.write("<id>" + deptCode + "</id>");
out.write("<name>" + deptName + "</name>");
out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching departments in Branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}


    public void getSbuInBranchOld(HttpServletRequest request,
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
    
    public void getSbuInBranch(HttpServletRequest request,
            HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchIdStr = request.getParameter("bid");
if (branchIdStr == null || branchIdStr.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

int branchId = 0;
try {
branchId = Integer.parseInt(branchIdStr);
} catch (NumberFormatException e) {
System.out.println("WARNING: Invalid branch ID -> " + branchIdStr);
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT sbu_code, sbu_name " +
   "FROM am_sbu_attachement " +
   "WHERE attach_id = ? " +
   "ORDER BY sbu_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, branchId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String sbuCode = rs.getString("sbu_code");
String sbuName = escapeXml(rs.getString("sbu_name"));

out.write("<newsbu_code>");
out.write("<id>" + sbuCode + "</id>");
out.write("<name>" + sbuName + "</name>");
out.write("</newsbu_code>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching SBU in Branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



    public void getUsrDeptInBranchOld(HttpServletRequest request,
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
    
    
    public void getUsrDeptInBranch(HttpServletRequest request,
            HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchIdStr = request.getParameter("bid");
if (branchIdStr == null || branchIdStr.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

int branchId = 0;
try {
branchId = Integer.parseInt(branchIdStr);
} catch (NumberFormatException e) {
System.out.println("WARNING: Invalid branch ID -> " + branchIdStr);
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT a.DEPT_CODE, a.DEPT_NAME " +
"FROM am_ad_department a " +
"JOIN sbu_branch_dept b ON a.DEPT_ID = b.DEPTID " +
"WHERE a.DEPT_STATUS = 'ACTIVE' AND b.BRANCHID = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, branchId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String deptCode = rs.getString("DEPT_CODE");
String deptName = escapeXml(rs.getString("DEPT_NAME"));

out.write("<department>");
out.write("<id>" + deptCode + "</id>");
out.write("<name>" + deptName + "</name>");
out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching user departments in branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



    public void getSupervisorOld(HttpServletRequest request,
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
    
    
    public void getSupervisor(HttpServletRequest request,
            HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

// Parse request branch ID safely
int reqnBranchId = 0;
String branchid = request.getParameter("bid");
if (branchid != null) {
try {
reqnBranchId = Integer.parseInt(branchid);
} catch (NumberFormatException ignored) {}
}

// Get session attributes safely
HttpSession session = request.getSession();
int userBranchId = 0;
int currentUserId = 0;
try {
String userbrnchid = (String) session.getAttribute("UserCenter");
userBranchId = userbrnchid != null ? Integer.parseInt(userbrnchid) : 0;
String userID = (String) session.getAttribute("CurrentUser");
currentUserId = userID != null ? Integer.parseInt(userID) : 0;
} catch (NumberFormatException ignored) {}

String query = "SELECT user_id, full_name " +
     "FROM am_gb_user " +
     "WHERE (branch = ? OR branch = ?) " +
     "AND user_status = 'Active' " +
     "AND is_supervisor = 'Y' " +
     "AND user_ID != ? " +
     "ORDER BY full_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, reqnBranchId);
ps.setInt(2, userBranchId);
ps.setInt(3, currentUserId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
  String supervisorId = rs.getString("user_id");
  String supervisorName = escapeXml(rs.getString("full_name"));

  out.write("<supervisor>");
  out.write("<id>" + supervisorId + "</id>");
  out.write("<name>" + supervisorName + "</name>");
  out.write("</supervisor>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching supervisors -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getItemTypeOld(HttpServletRequest request,
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

public void getItemType(HttpServletRequest request,
        HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String itemTypeCode = request.getParameter("bid");
int itemTypeId = 0;
if (itemTypeCode != null) {
try {
itemTypeId = Integer.parseInt(itemTypeCode);
} catch (NumberFormatException ignored) {}
}

String query = "SELECT ITEM_CODE, DESCRIPTION + '-' + ITEM_CODE " +
   "FROM ST_INVENTORY_ITEMS " +
   "WHERE ITEMTYPE_CODE = ? " +
   "ORDER BY DESCRIPTION";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setInt(1, itemTypeId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = rs.getString(1);
String name = escapeXml(rs.getString(2));

out.write("<description>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</description>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching item types -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getSTCategoryOld(HttpServletRequest request,
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


public void getSTCategory(HttpServletRequest request,
        HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String categoryCode = request.getParameter("bid");
if (categoryCode == null || categoryCode.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT ITEMTYPE_CODE, NAME " +
 "FROM ST_ITEMTYPE " +
 "WHERE STATUS = 'A' AND CATEGORY_CODE = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, categoryCode);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = rs.getString("ITEMTYPE_CODE");
String name = escapeXml(rs.getString("NAME"));

out.write("<itemName>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</itemName>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching category items -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}


public void getSubCategoryOld(HttpServletRequest request,
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


public void getSubCategory(HttpServletRequest request,
        HttpServletResponse response) 
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String subcategoryId = request.getParameter("bid");
if (subcategoryId == null || subcategoryId.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT DESCRIPTION, DESCRIPTION + '-' + SUB_CATEGORY_CODE AS NARRATION " +
"FROM AM_ASSET_DESCRIPTION " +
"WHERE SUB_CATEGORY_CODE IN (SELECT SUB_CATEGORY_CODE FROM am_ad_sub_category WHERE SUB_CATEGORY_ID = ?) " +
"ORDER BY DESCRIPTION";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, subcategoryId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("DESCRIPTION"));
String name = escapeXml(rs.getString("NARRATION"));

out.write("<description>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</description>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching subcategory -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getAssetDescOld(HttpServletRequest request,
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

public void getAssetDesc(HttpServletRequest request,
        HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String assetId = request.getParameter("bid");
if (assetId == null || assetId.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT DESCRIPTION, DESCRIPTION AS NARRATION " +
  "FROM AM_ASSET WHERE ASSET_ID = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, assetId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("DESCRIPTION"));
String name = escapeXml(rs.getString("NARRATION"));

out.write("<description>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</description>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching Asset Description -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getAssetBarCodeOld(HttpServletRequest request,
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

public void getAssetBarCode(HttpServletRequest request,
        HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String assetId = request.getParameter("bid");
if (assetId == null || assetId.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT BAR_CODE, BAR_CODE AS NARRATION FROM AM_ASSET WHERE ASSET_ID = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, assetId);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("BAR_CODE"));
String name = escapeXml(rs.getString("NARRATION"));

out.write("<description>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</description>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching Asset BarCode -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getDeptCodeInBranchOld(HttpServletRequest request,
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

public void getDeptCodeInBranch(HttpServletRequest request,
        HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchCode = request.getParameter("bid");
if (branchCode == null || branchCode.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT d.Dept_code, d.Dept_name + '-' + d.Dept_code AS DEPT_NARRATION " +
"FROM sbu_branch_dept s " +
"JOIN am_ad_department d ON s.deptId = d.Dept_ID " +
"WHERE s.branchCode = ? " +
"ORDER BY d.Dept_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchCode);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("Dept_code"));
String name = escapeXml(rs.getString("DEPT_NARRATION"));

out.write("<department>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching departments in Branch -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getDeptInBranchByCodeOld(HttpServletRequest request,
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


public void getDeptInBranchByCode(HttpServletRequest request,
        HttpServletResponse response)
throws ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String branchDepValue = request.getParameter("bid");
if (branchDepValue == null || !branchDepValue.contains("_")) {
out.write("</message>");
out.flush();
out.close();
return;
}

String[] branchDeptSplit = branchDepValue.split("_");
String branchCode = branchDeptSplit[0];
String deptCode = branchDeptSplit[1];

String query = "SELECT d.dept_Code, d.Dept_name + '-' + d.dept_Code AS DEPT_NARRATION " +
"FROM sbu_branch_dept s " +
"JOIN am_ad_department d ON s.deptCode = d.Dept_Code " +
"WHERE s.branchCode = ? AND s.deptCode = ? " +
"ORDER BY d.Dept_name";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, branchCode);
ps.setString(2, deptCode);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("dept_Code"));
String name = escapeXml(rs.getString("DEPT_NARRATION"));

out.write("<department>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</department>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching department by code -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getCodeOnlyOld(HttpServletRequest request,
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

public void getCodeOnly(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String code = request.getParameter("bid");
if (code == null || code.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT CODE, DESCRIPTION FROM ST_GL_PROJECT WHERE CODE = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, code);

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
String id = escapeXml(rs.getString("CODE"));
String name = escapeXml(rs.getString("DESCRIPTION"));

out.write("<codeDescription>");
out.write("<id>" + id + "</id>");
out.write("<name>" + name + "</name>");
out.write("</codeDescription>");
}
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching Project Code in getCodeOnly -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getCATOld(HttpServletRequest request,
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

public void getCAT(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<message>");

String categoryCode = request.getParameter("bid");
if (categoryCode == null || categoryCode.trim().isEmpty()) {
out.write("</message>");
out.flush();
out.close();
return;
}

String query = "SELECT sub_category_desc, sub_category_desc " +
        "FROM HD_COMPLAIN_SUBCATEGORY " +
        "WHERE category_code = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, categoryCode);

try (ResultSet rs = ps.executeQuery()) {
 while (rs.next()) {
     String id = escapeXml(rs.getString(1));
     String name = escapeXml(rs.getString(2));

     out.write("<subcategory>");
     out.write("<id>" + id + "</id>");
     out.write("<name>" + name + "</name>");
     out.write("</subcategory>");
 }
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching Category -> " + e.getMessage());
e.printStackTrace();
}

out.write("</message>");
out.flush();
out.close();
}



public void getTECOld(HttpServletRequest request,
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


public void getTEC(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {

String categoryCode = request.getParameter("bid");

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<technician>");

if (categoryCode == null || categoryCode.trim().isEmpty()) {
out.write("<name></name>");
out.write("<head></head>");
out.write("</technician>");
out.flush();
out.close();
return;
}

String query = "SELECT Dept_name, UnitHead FROM AM_AD_DEPARTMENT WHERE Dept_code = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, categoryCode);

try (ResultSet rs = ps.executeQuery()) {
 String name = "";
 String head = "";
 if (rs.next()) {
     name = escapeXml(rs.getString("Dept_name"));
     head = escapeXml(rs.getString("UnitHead"));
 }
 out.write("<name>" + name + "</name>");
 out.write("<head>" + head + "</head>");
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching technician -> " + e.getMessage());
e.printStackTrace();
}

out.write("</technician>");
out.flush();
out.close();
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
   
public void getUSERIDOld(HttpServletRequest request,
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


public void getUSERID(HttpServletRequest request,
        HttpServletResponse response) throws
ServletException, IOException {

String userName = request.getParameter("bid");

response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");

PrintWriter out = response.getWriter();
out.write("<returnmail>");

if (userName == null || userName.trim().isEmpty()) {
out.write("<name></name>");
out.write("<head></head>");
out.write("</returnmail>");
out.flush();
out.close();
return;
}

String query = "SELECT User_Id, email FROM AM_GB_USER WHERE User_Name = ?";

try (Connection con = dbConnection.getConnection("legendPlus");
PreparedStatement ps = con.prepareStatement(query)) {

ps.setString(1, userName);

try (ResultSet rs = ps.executeQuery()) {
String userId = "";
String email = "";
if (rs.next()) {
  userId = escapeXml(rs.getString("User_Id").trim());
  email = escapeXml(rs.getString("email").trim());
}

out.write("<name>" + userId + "</name>");
out.write("<head>" + email + "</head>");
}

} catch (Exception e) {
System.out.println("WARNING: Error fetching UserID -> " + e.getMessage());
e.printStackTrace();
}

out.write("</returnmail>");
out.flush();
out.close();
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
