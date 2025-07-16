/* Decompiler 10ms, total 1091ms, lines 102 */
package legend.process;

import audit.AuditTrailGen;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.AssetMake;

public class MakeAuditServlet extends HttpServlet {
   public void init(ServletConfig config) throws ServletException {
      super.init(config);
   }

   public void destroy() {
   }

   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setDateHeader("Expires", -1L);
      HttpSession session = request.getSession();
      PrintWriter out = response.getWriter();
      String statusMessage = "";
      boolean updtst = false;
      AuditTrailGen audit = new AuditTrailGen();
      String loginId = (String)session.getAttribute("CurrentUser");
      int loginID;
      if (loginId == null) {
         loginID = 0;
      } else {
         loginID = Integer.parseInt(loginId);
      }

      String branchcode = (String)session.getAttribute("UserCenter");
      if (branchcode == null) {
         branchcode = "not set";
      }

      String buttSave = request.getParameter("buttSave");
      String makeId = request.getParameter("makeId");
      String assetMakeCode = request.getParameter("makeCode");
      String assetMake = request.getParameter("makeName");
      String status = request.getParameter("makeStatus");
      String category = request.getParameter("makeCart");
      String userid = (String)session.getAttribute("CurrentUser");
      AssetMake am = new AssetMake();
      am.setAssetMakeCode(assetMakeCode);
      am.setAssetMake(assetMake);
      am.setCategory(category);
      am.setStatus(status);
      am.setUserid(userid);

      try {
         CompanyHandler ch = new CompanyHandler();
         if (buttSave != null) {
            if (makeId.equals("")) {
               if (ch.getAssetMakeByCode(assetMakeCode) != null) {
                  out.print("<script>alert('Asset Code is in Use.')</script>");
                  out.print("<script>history.go(-1)</script>");
               } else if (ch.createAssetMake(am)) {
                  out.print("<script>alert('Record saved successfully.')</script>");
                  out.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId=" + ch.getAssetMakeByCode(assetMakeCode).getAssetMakeId() + "&PC=61'</script>");
               } else {
                  System.out.println("Error saving record: New record \nfor 'asset make'  with make " + am.getAssetMake() + " could not be created");
                  out.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup'</script>");
               }
            }

            if (!makeId.equals("")) {
               am.setAssetMakeId(makeId);
               audit.select(1, "SELECT * FROM  AM_GB_ASSETMAKE   WHERE assetmake_Id = '" + makeId + "'");
               ch.updateAssetMake(am);
               audit.select(2, "SELECT * FROM  AM_GB_ASSETMAKE   WHERE assetmake_Id = '" + makeId + "'");
               updtst = audit.logAuditTrail("AM_GB_ASSETMAKE", branchcode, loginID, makeId);
               if (updtst) {
                  out.print("<script>alert('Update on record is successfull')</script>");
                  out.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId=" + makeId + "&PC=61'</script>");
               } else {
                  out.print("<script>alert('No changes made on record')</script>");
                  out.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup&makeId=" + makeId + "&PC=61'</script>");
               }
            }
         }
      } catch (Throwable var21) {
         var21.printStackTrace();
         out.print("<script>alert('Ensure unique record entry.')</script>");
         out.print("<script>window.location = 'DocumentHelp.jsp?np=assetmakeSetup'</script>");
         System.err.print(var21.getMessage());
      }

   }

   public String getServletInfo() {
      return "Make Audit Servlet";
   }
}
