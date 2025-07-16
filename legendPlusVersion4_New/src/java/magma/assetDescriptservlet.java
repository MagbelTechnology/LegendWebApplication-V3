package magma;

import com.magbel.util.DataConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class assetDescriptservlet extends HttpServlet {
   private static final String CONTENT_TYPE = "text/xml";
   private static final String DOC_TYPE = null;

   public void init() throws ServletException {
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String asset_Id = request.getParameter("asset_Id");
      if (asset_Id == null) {
         asset_Id = "0";
      }

      response.setContentType("text/xml");
      PrintWriter out = response.getWriter();
      out.write("<message>");
      ResultSet rs = null;
      Connection con = null;
      PreparedStatement ps = null;

      try {
         String query = "SELECT DESCRIPTION,DESCRIPTION FROM AM_ASSET WHERE ASSET_ID = ? ";
         con = (new DataConnect("legendPlus")).getConnection();
         ps = con.prepareStatement(query);
         ps.setString(1, asset_Id);
         rs = ps.executeQuery();

         while(rs.next()) {
            System.out.println("About to start Looping");
            out.write("<make>");
            out.write("<id>");
            out.write(rs.getString(1));
            out.write("</id>");
            out.write("<name>");
            out.write(rs.getString(2).replaceAll("&", "&amp;"));
            out.write("</name>");
            out.write("</make>");
            System.out.println("The Bar Code Value is LLLLLLLLLLLLLLLLLL" + rs.getString(2));
         }
      } catch (SQLException var13) {
      } catch (Exception var14) {
      } finally {
         this.closeConnection(con, ps, rs);
      }

      out.write("</message>");
      if (DOC_TYPE != null) {
         out.println(DOC_TYPE);
      }

   }

   public void destroy() {
   }

   private void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) {
      try {
         if (rs != null) {
            rs.close();
         }

         if (ps != null) {
            ps.close();
         }

         if (con != null) {
            con.close();
         }
      } catch (Exception var5) {
         System.out.println("WANR: Error closing connection >>" + var5);
      }

   }
}
