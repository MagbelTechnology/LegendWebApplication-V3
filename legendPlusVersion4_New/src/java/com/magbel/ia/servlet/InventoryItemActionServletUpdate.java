package com.magbel.ia.servlet;

import com.magbel.ia.bus.InventoryServiceBus;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class InventoryItemActionServletUpdate extends HttpServlet
{
 
    private InventoryServiceBus serviceBus;

    public InventoryItemActionServletUpdate()
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
        jakarta.servlet.http.HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("ID");
        String itemNo = request.getParameter("itemCode");
        String status = request.getParameter("Status");
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
        if(strMaxApproveLevel ==null){strMaxApproveLevel = "0";}
        System.out.println("<<<<<<strMaxApproveLevel: "+strMaxApproveLevel);
        int maxApproveLevel = strMaxApproveLevel == null || strMaxApproveLevel.equals("") ? 0 : Integer.parseInt(strMaxApproveLevel);
   //     String strMinAmtLimit = request.getParameter("minAmtLimit").replaceAll(",", "");
        String strMinAmtLimit = request.getParameter("minAmtLimit");
        if(strMinAmtLimit ==null){strMinAmtLimit = "0";}
        System.out.println("<<<<<<strMinAmtLimit: "+strMinAmtLimit);
        double minAmtLimit = strMinAmtLimit == null || strMinAmtLimit.equals("") ? 0.0D : Double.parseDouble(strMinAmtLimit);
      //  String strMaxAmtLimit = request.getParameter("maxAmtLimit").replaceAll(",", "");
        String strMaxAmtLimit = request.getParameter("maxAmtLimit");
        if(strMaxAmtLimit ==null){strMaxAmtLimit = "0";}
        System.out.println("<<<<<<strMaxAmtLimit: "+strMaxAmtLimit);
        double maxAmtLimit = strMaxAmtLimit == null || strMaxAmtLimit.equals("") ? 0.0D : Double.parseDouble(strMaxAmtLimit);
        String strAdjustMaxApproveLevel = request.getParameter("adjustMaxApproveLevel");
        if(strAdjustMaxApproveLevel ==null){strAdjustMaxApproveLevel = "0";}
        System.out.println("<<<<<<strAdjustMaxApproveLevel: "+strAdjustMaxApproveLevel);
        int adjustMaxApproveLevel = strAdjustMaxApproveLevel == null || strAdjustMaxApproveLevel.equals("") ? 0 : Integer.parseInt(strAdjustMaxApproveLevel);
   //     String strAdjustMinAmtLimit = request.getParameter("adjustMinAmtLimit").replaceAll(",", "");
        String strAdjustMinAmtLimit = request.getParameter("adjustMinAmtLimit");
        if(strAdjustMinAmtLimit ==null){strAdjustMinAmtLimit = "0";}
        System.out.println("<<<<<<strAdjustMinAmtLimit: "+strAdjustMinAmtLimit);
        double adjustMinAmtLimit = strAdjustMinAmtLimit == null || strAdjustMinAmtLimit.equals("") ? 0.0D : Double.parseDouble(strAdjustMinAmtLimit);
//        String strAdjustMaxAmtLimit = request.getParameter("adjustMaxAmtLimit").replaceAll(",", "");
        String strAdjustMaxAmtLimit = request.getParameter("adjustMaxAmtLimit");
        if(strAdjustMaxAmtLimit ==null){strAdjustMaxAmtLimit = "0";}
        System.out.println("<<<<<<strAdjustMaxAmtLimit: "+strAdjustMaxAmtLimit);
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
        double weightAverageCost = strWeightAverageCost == null || strWeightAverageCost.equals("") ? 0.0D : Double.parseDouble(strWeightAverageCost);
        String strStandardCost = request.getParameter("standardCost");
        double standardCost = strStandardCost == null || strStandardCost.equals("") ? 0.0D : Double.parseDouble(strStandardCost);
        String vFIFO = request.getParameter("vFIFO");
        double FIFO = vFIFO == null || vFIFO.equals("") ? 0.0D : Double.parseDouble(vFIFO);
        String strWeight = request.getParameter("weight");
        double weight = strWeight == null || strWeight.equals("") ? 0.0D : Double.parseDouble(strWeight);
        String currentUser = request.getParameter("currentUser");
        int userId = Integer.parseInt(currentUser);
        String categoryCode = request.getParameter("categoryCode");
        try
        {
            if(InventoryItemUpdBtn != null && serviceBus.updateInventoryItem(itemNo, status, type, description,
            		taxCode, minimumQuantity, weightAverageCost, standardCost, FIFO, weight, backOrderable, reorderLevel, 
            		salesAcct, COGSoldAcct, inventoryAcct, reqApproval, maxApproveLevel, minAmtLimit, maxAmtLimit,
            		adjustAcct, adjustMaxApproveLevel, adjustMinAmtLimit, adjustMaxAmtLimit, receivable,categoryCode))
            {
                out.print("<script>alert('Record successfully Submitted.')</script>");
                out.print((new StringBuilder()).append("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode=").append(itemNo).append("'</script>").toString());
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