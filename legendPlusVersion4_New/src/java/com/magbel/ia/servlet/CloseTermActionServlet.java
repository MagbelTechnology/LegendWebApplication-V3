package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.vao.ItemApprovalDetail;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtilily;
import com.magbel.ia.bus.CustomerAccountSetupHandler;
import com.magbel.ia.bus.GLAccountServiceBus;

import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class CloseTermActionServlet extends HttpServlet
{
	ApprovalRecords Apprecord;
	AccountChartServiceBus serviceBus;
	HtmlUtilily htmlUtil;
	CustomerAccountSetupHandler custASHandle;
	GLAccountServiceBus GLHandler;
	com.magbel.util.DatetimeFormat df;
	SimpleDateFormat sdf;
  //  private SupervisorServiceBus serviceBus;

    public CloseTermActionServlet()
    {
    }  

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        //serviceBus = new SupervisorServiceBus();
        Apprecord = new ApprovalRecords();
        serviceBus = new AccountChartServiceBus();
        htmlUtil = new HtmlUtilily();
        custASHandle = new CustomerAccountSetupHandler();
        GLHandler = new GLAccountServiceBus();
        df = new com.magbel.util.DatetimeFormat();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        String mtid = "";
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        String companyCODE;
        if(loginId == null)
        {
            loginID = "Unkown";
            companyCODE = "Unkown";
        } else
        {
            loginID = loginId.getUserId();
            companyCODE = loginId.getCompanyCode();
        }
        String buttSave = request.getParameter("buttSave");
        String branchCode = request.getParameter("branchCode");
        String userId = request.getParameter("userId");
        String companyCode = request.getParameter("companyCode");
        String Narration = request.getParameter("narration");
        String term = request.getParameter("Semester");
        String Session = request.getParameter("session");
        String tranCode = request.getParameter("tranCode");  
        String termenddate = request.getParameter("EndTermDate");
        String todaysdate = sdf.format(new java.util.Date());
        
        String enddd = termenddate.substring(1,3);
		String endmm = termenddate.substring(4,6);
		String endyyyy = termenddate.substring(7,11);
		String enddate = endyyyy+'-'+endmm+'-'+enddd;
        
        System.out.println("enddate>>>>>: "+enddate);
        String confirmclosed = htmlUtil.getCodeName("SELECT COUNT(*) FROM MG_GB_SCHOOL_CLOSED WHERE COMP_CODE = '"+companyCode+"' AND SCHOOL = '"+branchCode+"' AND CLOSE_DATE = '"+enddate+"'");
        String recNumber = htmlUtil.getCodeName(" select COUNT(*) from IA_CUSTOMER where COMP_CODE = '"+companyCode+"' AND SCHOOL =  '"+branchCode+"' AND  STATUS = 'ACTIVE'");
        if(recNumber == null){recNumber = "0";}
   //     System.out.println("recNumber>>>>>: "+recNumber+"  userId: "+userId);
    	int recNo = Integer.parseInt(recNumber);
    	System.out.println("confirmclosed>>>>>: "+confirmclosed);
        ArrayList list = new ArrayList();
        if(!confirmclosed.equalsIgnoreCase("0")){
                out.println("<script>");
                out.println("alert('Term Has Already been Closed Before !....')");
                out.println("window.location='DocumentHelp.jsp?np=CloseTerm'");
                out.println("</script>");        	
            }
        else{
        if(buttSave != null)
        {
        //	if(term.equalsIgnoreCase("THIRD")){
        		serviceBus.insertBalancesForCloseTerm(branchCode,companyCode,termenddate);
        //	}
        	serviceBus.insertResultForCloseTerm(branchCode,companyCode,termenddate);        	
        	Apprecord.updateFilesForCloseTerm(companyCode,branchCode); 	
        	if(term.equalsIgnoreCase("THIRD")){
        		serviceBus.updateStudentStatusClose(companyCode,branchCode);
        		serviceBus.updateFinalYearStudentStatus(companyCode,branchCode);
        	}   
        	serviceBus.DeleteSessionRecords(branchCode,companyCode);
        	serviceBus.updateTermBalanceforStudent(companyCode,branchCode,term);
        	boolean done = serviceBus.UpdateCloseSchool(companyCode, branchCode);
        	serviceBus.createTermCloseRecord(companyCode, branchCode, term, Session, Integer.parseInt(userId), termenddate,recNo);
            out.println("<script>");
            out.println("alert('Term Closes!....')");
            out.println("window.location='DocumentHelp.jsp?np=CloseTerm'");
            out.println("</script>");
        }
    }

    }

    public String getServletInfo()
    {
        return "Start New Term Action Servlet";
    }
}
