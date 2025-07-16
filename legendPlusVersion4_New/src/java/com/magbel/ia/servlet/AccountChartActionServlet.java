package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtilily;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class AccountChartActionServlet extends HttpServlet
{

    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    AccountChartServiceBus serviceBus;
    ApprovalRecords Apprecord;
    HtmlUtilily htmlUtil;
    public AccountChartActionServlet()
    {
    }
  
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        serviceBus = new AccountChartServiceBus();
        Apprecord = new ApprovalRecords();
        htmlUtil = new HtmlUtilily();
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html; charset=windows-1252");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String id = request.getParameter("ID");
        String companyCode = request.getParameter("companyCode");
        String drBranch = request.getParameter("drBranch");
        String intDrCurrCode = request.getParameter("drCurrCode");
        intDrCurrCode = serviceBus.getCodeName((new StringBuilder()).append("SELECT CURRENCY_ID FROM IA_GB_CURRENCY_CODE WHERE ISO_CODE='").append(intDrCurrCode).append("'").toString());
        intDrCurrCode = intDrCurrCode == null || intDrCurrCode.equals("") ? "0" : intDrCurrCode;
        int drCurrCode = Integer.parseInt(intDrCurrCode);
        String drAcctNo = request.getParameter("drAcctNo");
        String drAcctType = request.getParameter("drAcctType");
        String drTranCode = request.getParameter("drTranCode"); 
        String drSBU = request.getParameter("drSBU");
        String drNarration = request.getParameter("drNarration");
        String strDrAmount = request.getParameter("drAmount");
        strDrAmount = strDrAmount.replaceAll(",", "");
        double drAmount = Double.parseDouble(strDrAmount);
        String drReference = request.getParameter("drReference");
        String strDrAcctExchRate = request.getParameter("drExchRate");
        double drAcctExchRate = Double.parseDouble(strDrAcctExchRate);
        String strDrSysExchRate = "0";
        double drSysExchRate = Double.parseDouble(strDrSysExchRate);
        String crBranch = request.getParameter("crBranch");
        String intCrCurrCode = request.getParameter("crCurrCode");
        intCrCurrCode = serviceBus.getCodeName((new StringBuilder()).append("SELECT CURRENCY_ID FROM IA_GB_CURRENCY_CODE WHERE ISO_CODE='").append(intCrCurrCode).append("'").toString());
        intCrCurrCode = intCrCurrCode == null || intCrCurrCode.equals("") ? "0" : intCrCurrCode;
        int crCurrCode = Integer.parseInt(intCrCurrCode);
        String crAcctNo = request.getParameter("crAcctNo");
        String crAcctType = request.getParameter("crAcctType");
        String crTranCode = request.getParameter("crTranCode");
        String crSBU = request.getParameter("crSBU");
        String crNarration = request.getParameter("crNarration");
        String strCrAmount = request.getParameter("crAmount");
        strCrAmount = strCrAmount.replaceAll(",", "");
        double crAmount = Double.parseDouble(strCrAmount);
        String crReference = request.getParameter("crReference");
        String strCrAcctExchRate = request.getParameter("crExchRate");
        double crAcctExchRate = Double.parseDouble(strCrAcctExchRate);
        String strCrSysExchRate = "0";
        double crSysExchRate = Double.parseDouble(strCrSysExchRate);
        String effDate = request.getParameter("effDate");
        String transDate = request.getParameter("transDate");
        String addGL2GLPostBtn = request.getParameter("addGL2GLPostBtn");
        String drbranchCode =htmlUtil.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = '"+drBranch+"' ");
        String crbranchCode =htmlUtil.findObject("SELECT BRANCH_CODE FROM MG_AD_Branch WHERE BRANCH_ID = '"+crBranch+"' ");
        User user = null;
        try
        {
            if(session.getAttribute("CurrentUser") != null)
            {
                user = (User)session.getAttribute("CurrentUser");
            }
 //           System.out.println("<<<<<<<<<<<<<<<<<<<USER: "+user);
            int userId = Integer.parseInt(user.getUserId());
            if(addGL2GLPostBtn != null && serviceBus.createGL2GLPosting(drBranch, drCurrCode, drAcctType, 
            drAcctNo, drTranCode, drSBU, drNarration, drAmount, drReference, drAcctExchRate, drSysExchRate, 
            crBranch, crCurrCode, crAcctType, crAcctNo, crTranCode, crSBU, crNarration, drAmount, 
            crAcctExchRate, crSysExchRate, effDate, userId,companyCode,transDate))
            {// System.out.println("<<<<<<<<<<<<<<<<<<<addGL2GLPostBtn: "+addGL2GLPostBtn);
            	Apprecord.updateDRBalances(drAcctNo,drbranchCode,drAmount);
            	Apprecord.updateCRBalances(crAcctNo,crbranchCode,crAmount);
                out.print("<script>alert('Record Succesfully Saved.')</script>");
                out.print("<script>window.document.location='DocumentHelp.jsp?np=GL2GLPost'</script>");
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
}
