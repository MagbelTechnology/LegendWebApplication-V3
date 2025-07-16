package com.magbel.ia.servlet;

import com.magbel.ia.bus.*;
import com.magbel.ia.vao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class SupervisorActionServlet extends HttpServlet
{

    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private SupervisorServiceBus serviceBus;
    private InventoryServiceBus inv;
    private InventoryAdjustmtServiceBus adjustmt;
    private AccountInterfaceServiceBus acctInterface;
    private SalesOrderServiceBus service;
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new SupervisorServiceBus();
        adjustmt = new InventoryAdjustmtServiceBus();
        inv = new InventoryServiceBus();
        acctInterface = new AccountInterfaceServiceBus();
        service = new SalesOrderServiceBus();
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String updateAdjustmtAppBtn = request.getParameter("updateAdjustmtAppBtn");
        String updateSOAppBtn = request.getParameter("updateSOAppBtn");
        String updatePOAppBtn = request.getParameter("updatePOAppBtn");
        String updateWarehouseTransAppBtn = request.getParameter("updateWarehouseTransAppBtn");
        String updateImprestAppBtn = request.getParameter("updateImprestAppBtn");
        String strApproveOfficer = request.getParameter("approveOfficer");
        int approveOfficer = strApproveOfficer == null || !strApproveOfficer.equals("") ? 0 : Integer.parseInt(strApproveOfficer);
        String status = request.getParameter("approveStatus");
        String IDD = request.getParameter("ID");
        String transType = request.getParameter("TRANSTYPE");
        String reason = request.getParameter("reason");
        
        String quantityValue =request.getParameter("quantityValue");
        String strQuantityShow = "";
        
        User user = null;
        ArrayList list = new ArrayList();
        try
        {
            if(session.getAttribute("CurrentUser") != null)
            {
                user = (User)session.getAttribute("CurrentUser");
            }
            int userId = Integer.parseInt(user.getUserId());
            if(updateAdjustmtAppBtn != null && serviceBus.updateTransactionApproval(IDD, transType, approveOfficer, status, reason))
            {
                if(approveOfficer == 0 && status.equals("A"))
                {
                    String strQuantity = "";
                    int quantity = 0;
                    list = serviceBus.findTransactionApprovalByQuery((new StringBuilder()).append(" AND MTID='").append(IDD).append("'").toString());
                    TransactionApproval ta = (TransactionApproval)list.get(0);
                    String itemCode = ta.getItemCode();
                    Integer iquantity = new Integer(ta.getQuantity());
                    strQuantity = iquantity.toString(); 
                    String adjustID = ta.getTransCode();
                    InventoryAdjustment invObj = null;
                    invObj = adjustmt.findInventoryAdjustmentById(adjustID);
                    String warehouse = invObj.getWarehouse() == null ? "" : invObj.getWarehouse();
                    String adjustOpt = invObj.getAdjustOpt() == null ? "" : invObj.getAdjustOpt().trim();
                    strQuantity = adjustOpt.equals("REMOVE") ? (new StringBuilder()).append("-").append(strQuantity).toString() : strQuantity;
                    quantity = Integer.parseInt(strQuantity);
                    if(adjustmt.postAdjustment2InventoryHistory(itemCode, "ADJUSTMENT", quantity, warehouse, userId))
                    {
                        out.print("<script>alert('Inventory successfully updated.')</script>");
                    }
                }
                out.print("<script>alert('Record successfully done.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=TransactionApprovalList'</script>");
            }
            if(updateWarehouseTransAppBtn != null && serviceBus.updateTransactionApproval(IDD, transType, approveOfficer, status, reason))
            {
            	System.out.println("------------------update-----successs----------------");
            	System.out.println("--approveOfficer------"+approveOfficer);
            	System.out.println("----status------"+status);
                if(approveOfficer == 0 && status.equals("A"))
                {
                    String strQuantity = "";
                    int qquantity = 0;
                    list = serviceBus.findTransactionApprovalByQuery((new StringBuilder()).append(" AND MTID='").append(IDD).append("'").toString());
                	System.out.println("----list------"+list.size());
                    TransactionApproval ta = (TransactionApproval)list.get(0);
                    String itemCode = ta.getItemCode();
                    Integer iquantity = new Integer(ta.getQuantity());
                    strQuantity = iquantity.toString(); 
                    String transferID = ta.getTransCode();
                    WarehouseTransfer invObj = null;
                    invObj = inv.findWarehouseTransferById(transferID);
                    String fromWarehouse = invObj.getFromWarehouse() == null ? "" : invObj.getFromWarehouse();
                    String toWarehouse = invObj.getToWarehouse() == null ? "" : invObj.getToWarehouse().trim();
                    
                    System.out.println("----fromWarehouse------"+fromWarehouse);
                    System.out.println("----itemCode------"+itemCode);
                    System.out.println("----strQuantity------"+strQuantity);
                    System.out.println("----toWarehouse------"+toWarehouse);
                    System.out.println("----userId------"+userId); 
                    String fromWarehouse1 = inv.getCodeName("SELECT WAREHOUSE_CODE FROM IA_WAREHOUSE WHERE  NAME = '"+fromWarehouse+"'");
                    String toWarehouse1 = inv.getCodeName("SELECT WAREHOUSE_CODE FROM IA_WAREHOUSE WHERE  NAME = '"+toWarehouse+"'");
                    if(inv.processWarehouseTransfer(itemCode, "TRANSFER", strQuantity, fromWarehouse1, toWarehouse1, userId))
                    {System.out.println("------------------update--2--successs----------------");
                        out.print("<script>alert('Inventory successfully updated.')</script>");
                    }
                }
                out.print("<script>alert('Record successfully done.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=TransactionApprovalList'</script>");
            }
            if(updateSOAppBtn != null && serviceBus.updateTransactionApproval(IDD, transType, approveOfficer, status, reason))
            { 
             
            	 list = serviceBus.findTransactionApprovalByQuery((new StringBuilder()).append(" AND MTID='").append(IDD).append("'").toString());
            	 System.out.println("list "+list.size());
            	 TransactionApproval ta = (TransactionApproval)list.get(0);
            	 
                 String itemCode = ta.getItemCode();
                 System.out.println("itemCode "+itemCode);
                 String adjustID = ta.getTransCode();
                 System.out.println("adjustID "+adjustID);
                 String warehouse = serviceBus.getCodeName(" SELECT warehouse_code FROM IA_so_item where sorder_code= '"+adjustID+"' ");
                 
                 
                 System.out.println(quantityValue+"w-"+warehouse+"i-"+itemCode); 
            	 
            	 service.updateInventoryTotals2(Integer.parseInt(quantityValue), warehouse, itemCode);
                out.print("<script>alert('Record successfully done.')warehouse</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=TransactionApprovalList'</script>");
            }
            if(updatePOAppBtn != null && serviceBus.updateTransactionApproval(IDD, transType, approveOfficer, status, reason))
            { 
                out.print("<script>alert('Record successfully done.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=TransactionApprovalList'</script>");
            }
            if(updateImprestAppBtn != null && serviceBus.updateTransactionApproval(IDD, transType, approveOfficer, status, reason))
            {
                if(approveOfficer == 0 && status.equals("A"))
                {
                    list = serviceBus.findTransactionApprovalByQuery((new StringBuilder()).append(" AND MTID='").append(IDD).append("'").toString());
                    TransactionApproval ta = (TransactionApproval)list.get(0);
                    String refNo = ta.getTransCode();
                    if(!acctInterface.updateImprestApproveStatus(refNo, "A"));
                }
                out.print("<script>alert('Record successfully done.')</script>");
                out.print("<script>window.location='DocumentHelp.jsp?np=TransactionApprovalList'</script>");
            }
        }
        catch(NullPointerException e)
        {
            response.sendRedirect("sessionTimedOut.jsp");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public SupervisorActionServlet()
    {
    }

}
