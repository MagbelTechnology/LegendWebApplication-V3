package legend;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import magma.asset.manager.*;
import magma.net.manager.RaiseEntryManager;
import magma.AssetRecordsBean;

import com.magbel.legend.bus.ApprovalRecords;

/**
 *
 * @author Ganiyu
 */
public class CheckDeletedEntry extends HttpServlet {

    private static final String CONTENT_TYPE = "text/xml";
    private static final String DOC_TYPE = null;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    //@Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("here in checkDeletedEntry servlet >>>>>>>>>>>>");
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();

        AssetManager assManager = new AssetManager();
        RaiseEntryManager raiseMan = new RaiseEntryManager();

        String asset_id = request.getParameter("asset_id");
        String page1 = request.getParameter("page1");
        String transId = request.getParameter("transId");
        String userClass = (String)request.getSession().getAttribute("UserClass");

        try {
        	 if (!userClass.equals("NULL") || userClass!=null){
            //AssetRecordsBean ad = new AssetRecordsBean();
            ApprovalRecords app = new ApprovalRecords();
            String query = "";
            //String result="";
            if (page1 != null && page1.equalsIgnoreCase("Asset Creation")) {
                query = "select asset_id from am_asset where asset_id ='" + asset_id + "'";
            }

            if (page1 != null && page1.equalsIgnoreCase("Asset Disposal")) {
                query = "select id from am_Raisentry_post where trans_id =" + transId;
            }
            if (page1 != null && page1.equalsIgnoreCase("Asset Transfer")) {

                query = "select id from am_Raisentry_post where trans_id =" + transId;
                String found = app.getCodeName(query);
                if (found != null && found.equalsIgnoreCase("")) {
                String delAssetTransfer="delete from AM_ASSETTRANSFER where transfer_id="+transId;
               String delApproval="delete from am_asset_approval where transaction_id="+ transId;
                app.deleteQuery(delAssetTransfer);
                app.deleteQuery(delApproval);

                }

            }

            out.print(app.getCodeName(query));
        }
        } catch (Exception e) {

            System.out.println(">>>>>>>>>>>>Error occured in CheckDeletedEntry " + e);
        } finally {
        }

    }
}

