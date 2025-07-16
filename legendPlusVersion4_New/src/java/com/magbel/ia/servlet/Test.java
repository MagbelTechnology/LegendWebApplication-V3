package com.magbel.ia.servlet;

import com.magbel.ia.bus.InventoryServiceBus;
import com.magbel.ia.vao.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class InventoryItemActionServlet extends HttpServlet
{

    private InventoryServiceBus serviceBus;

    public InventoryItemActionServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new InventoryServiceBus();
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
        String id = request.getParameter("ID");
        String itemNo = request.getParameter("itemCode");
        String status = request.getParameter("status");
        String type = request.getParameter("itemTypeCode");
        String description = request.getParameter("desc");
        String taxCode = request.getParameter("taxCode");
        String strMinimumQuantity = request.getParameter("minQuantity");
        int minimumQuantity = strMinimumQuantity == null ? 0 : Integer.parseInt(strMinimumQuantity);
        String strReorderLevel = request.getParameter("reorderLevel");
        int reorderLevel = strReorderLevel == null ? 0 : Integer.parseInt(strReorderLevel);
        String backOrderable = request.getParameter("backOrder");
        String strReqApproval = request.getParameter("reqApproval");
        String strOrganPosition = request.getParameter("organPosition");
        String strConcurrence = request.getParameter("concurrence");
        int organPosition = 0;
        int concurrence = 0;
        String reqApproval = strReqApproval != null && !strReqApproval.equals("") ? strReqApproval : "N";
        organPosition = strOrganPosition != null && !strOrganPosition.equals("") ? Integer.parseInt(strOrganPosition) : 0;
        concurrence = strConcurrence != null && !strConcurrence.equals("") ? Integer.parseInt(strConcurrence) : 0;
        String strMaxApproveLevel = request.getParameter("maxApproveLevel");
        int maxApproveLevel = strMaxApproveLevel == null || strMaxApproveLevel.equals("") ? 0 : Integer.parseInt(strMaxApproveLevel);
        String strMinAmtLimit = request.getParameter("minAmtLimit");
        double minAmtLimit = strMinAmtLimit == null || strMinAmtLimit.equals("") ? 0.0D : Double.parseDouble(strMinAmtLimit.replaceAll(",", ""));
        String strMaxAmtLimit = request.getParameter("maxAmtLimit");
        double maxAmtLimit = strMaxAmtLimit == null || strMaxAmtLimit.equals("") ? 0.0D : Double.parseDouble(strMaxAmtLimit.replaceAll(",", ""));
        String strAdjustMaxApproveLevel = request.getParameter("adjustMaxApproveLevel");
        int adjustMaxApproveLevel = strAdjustMaxApproveLevel == null || strAdjustMaxApproveLevel.equals("") ? 0 : Integer.parseInt(strAdjustMaxApproveLevel);
        String strAdjustMinAmtLimit = request.getParameter("adjustMinAmtLimit");
        double adjustMinAmtLimit = strAdjustMinAmtLimit == null || strAdjustMinAmtLimit.equals("") ? 0.0D : Double.parseDouble(strAdjustMinAmtLimit);
        String strAdjustMaxAmtLimit = request.getParameter("adjustMaxAmtLimit");
        double adjustMaxAmtLimit = strAdjustMaxAmtLimit == null || strAdjustMaxAmtLimit.equals("") ? 0.0D : Double.parseDouble(strAdjustMaxAmtLimit);
        String companyCode = request.getParameter("companyCode");
        String salesAcct = request.getParameter("salesAcct");
        String COGSoldAcct = request.getParameter("COGSoldAcct");
        String inventoryAcct = request.getParameter("inventoryAcct");
        String adjustAcct = request.getParameter("adjustAcct");
        String adjustOpt = request.getParameter("adjustOpt");
        String receivable = request.getParameter("receivable");
        String InventoryItemAddBtn = request.getParameter("InventoryItemAddBtn");
        String InventoryItemUpdBtn = request.getParameter("InventoryItemUpdBtn");
        String ItemAppLevelAddBtn = request.getParameter("ItemAppLevelAddBtn");
        String ItemAppLevelUpdBtn = request.getParameter("ItemAppLevelUpdBtn");
        String strWeightAverageCost = request.getParameter("weightAvgCost");
        double weightAverageCost = strWeightAverageCost == null || strWeightAverageCost.equals("") ? 0.0D : Double.parseDouble(strWeightAverageCost.replaceAll(",", ""));
        String strStandardCost = request.getParameter("standardCost");
        double standardCost = strStandardCost == null ? 0.0D : Double.parseDouble(strStandardCost.replaceAll(",", ""));
        String vFIFO = request.getParameter("vFIFO");
        double FIFO = vFIFO == null ? 0.0D : Double.parseDouble(vFIFO.trim().replaceAll(",", ""));
        String strWeight = request.getParameter("weight");
        double weight = strWeight == null ? 0.0D : Double.parseDouble(strWeight.trim());
        String isAutoIdGen = "";
        User user = null;
        try
        {
            if(session.getAttribute("CurrentUser") != null)
            {
                user = (User)session.getAttribute("CurrentUser");
            }
            int userId = Integer.parseInt(user.getUserId());
            if(InventoryItemAddBtn != null)
            {
                isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM IA_GB_COMPANY");
                if(isAutoIdGen.trim().equalsIgnoreCase("N"))
                {
                    if(itemNo.equals("") || itemNo == null)
                    {
                        out.print("<script>alert('Item Code. cannot be empty')</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(serviceBus.isRecordExisting((new StringBuilder()).append("SELECT COUNT(ITEM_CODE) FROM IA_INVENTORY_ITEMS WHERE ITEM_CODE='").append(itemNo).append("'").toString()))
                    {
                        out.print("<script>alert('Item Code Already Exist')</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(serviceBus.createInventoryItem(itemNo, status, type, description, taxCode, minimumQuantity, weightAverageCost, standardCost, FIFO, weight, backOrderable, userId, reorderLevel, salesAcct, COGSoldAcct, inventoryAcct, reqApproval, maxApproveLevel, minAmtLimit, maxAmtLimit, adjustAcct, adjustMaxApproveLevel, adjustMinAmtLimit, adjustMaxAmtLimit, companyCode, receivable))
                    {
                        out.print("<script>alert('Record successfully saved.')</script>");
                        out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode=").append(itemNo).append("'</script>").toString());
                    }
                } else
                if(serviceBus.createInventoryItem(itemNo, status, type, description, taxCode, minimumQuantity, weightAverageCost, standardCost, FIFO, weight, backOrderable, userId, reorderLevel, salesAcct, COGSoldAcct, inventoryAcct, reqApproval, maxApproveLevel, minAmtLimit, maxAmtLimit, adjustAcct, adjustMaxApproveLevel, adjustMinAmtLimit, adjustMaxAmtLimit, companyCode, receivable))
                {
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode=").append(serviceBus.auotoGenCode).append("'</script>").toString());
                }
            }
            if(InventoryItemUpdBtn != null)
            {
                out.print("<script>alert('Record successfully saved.')</script>");
                if(serviceBus.updateInventoryItem(itemNo, status, type, description, taxCode, minimumQuantity, weightAverageCost, standardCost, FIFO, weight, backOrderable, reorderLevel, salesAcct, COGSoldAcct, inventoryAcct, reqApproval, maxApproveLevel, minAmtLimit, maxAmtLimit, adjustAcct, adjustMaxApproveLevel, adjustMinAmtLimit, adjustMaxAmtLimit, receivable))
                {
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode=").append(itemNo).append("'</script>").toString());
                }
            }
            if(ItemAppLevelAddBtn != null)
            {
                if(serviceBus.isRecordExisting((new StringBuilder()).append("SELECT COUNT(ORGAN_POSITION) FROM IA_ITEM_APPROVAL_DETAIL WHERE ORGAN_POSITION=").append(organPosition).toString()))
                {
                    out.print("<script>alert('Approval Level Already Exist')</script>");
                    out.print("<script>history.go(-1);</script>");
                } else
                if(serviceBus.createItemApprovalDetail(itemNo, organPosition, concurrence, userId, minAmtLimit, maxAmtLimit, companyCode))
                {
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=itemApprovingLevel&itemCode=").append(itemNo).append("'</script>").toString());
                }
            }
            if(ItemAppLevelUpdBtn != null)
            {
                if(serviceBus.isRecordExisting((new StringBuilder()).append("SELECT COUNT(ORGAN_POSITION) FROM IA_ITEM_APPROVAL_DETAIL WHERE ORGAN_POSITION=").append(organPosition).toString()))
                {
                    out.print("<script>alert('Approval Level Already Exist')</script>");
                    out.print("<script>history.go(-1);</script>");
                }
                if(serviceBus.updateItemApprovalDetail(itemNo, organPosition, concurrence, id, minAmtLimit, maxAmtLimit))
                {
                    out.print("<script>alert('Record successfully saved.')</script>");
                    out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=itemApprovingLevelDetail&itemCode=").append(itemNo).append("&ID=").append(id).append("'</script>").toString());
                }
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

    public String getServletInfo()
    {
        return "InventoryItem Action Servlet";
    }
}