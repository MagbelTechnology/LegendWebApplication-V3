/* Decompiler 4ms, total 891ms, lines 50 */
package legend.process;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.AssignSbu;

public class saveAvaliableSbuServlet extends HttpServlet {
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
      String assign = request.getParameter("assign");
      String name = request.getParameter("glprefix");
      String contact = request.getParameter("glsuffix");
      AssignSbu approve = new AssignSbu();
      approve.setSbuname(assign);
      approve.setGlprifix(name);
      approve.setGlsurfix(contact);
      AdminHandler admin = new AdminHandler();

      try {
         if (admin.SaveAvaliabe_SBU(assign, name, contact)) {
            out.print("<script>alert('Record saved successfully.');</script>");
            out.print("<script>window.location = 'DocumentHelp.jsp?np=sbu_name'</script>");
         } else {
            out.print("<script>alert('Record cant not be save....');</script>");
            out.print("<script>window.location = 'DocumentHelp.jsp?np=sbu_name'</script>");
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }
}
