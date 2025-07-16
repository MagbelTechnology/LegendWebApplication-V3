package com.magbel.ia.servlet;

import com.magbel.ia.bus.AccountChartServiceBus;
import com.magbel.ia.bus.ApprovalRecords;
import com.magbel.ia.vao.ItemApprovalDetail;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtilily;
import com.magbel.ia.bus.CustomerAccountSetupHandler;
import com.magbel.ia.sms.SMSClient;
import com.magbel.ia.bus.CustomerHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class StartDayActionServletextends HttpServlet
{
	ApprovalRecords Apprecord;
	AccountChartServiceBus serviceBus;
	HtmlUtilily htmlUtil;
	CustomerAccountSetupHandler custASHandle;
	CustomerHandler customerHandle;
	//SMSClient smsrecord;
  //  private SupervisorServiceBus serviceBus;

    public StartDayActionServlet()
    {
    }  

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        //serviceBus = new SupervisorServiceBus();
        Apprecord = new ApprovalRecords();
      //  smsrecord = new SMSClient(1);
        serviceBus = new AccountChartServiceBus();
        htmlUtil = new HtmlUtilily();
        custASHandle = new CustomerAccountSetupHandler();
        customerHandle = new CustomerHandler();
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
//        System.out.println("branchCode In StartTermActionServlet: "+branchCode);
        String Narration = request.getParameter("narration");
        String term = request.getParameter("Semester");
        String termCode = request.getParameter("Semester");
        String tranCode = request.getParameter("tranCode");
        String termstartdate =  request.getParameter("StartTermDate");
        System.out.println("StartTermDate: "+termstartdate);
        String termenddate = request.getParameter("EndTermDate");
        System.out.println("termenddate: "+termenddate);
        String stdd = termstartdate.substring(1,3);
		String stmm = termstartdate.substring(4,6);
		String styyyy = termstartdate.substring(7,11);
		String startdate = styyyy+'-'+stmm+'-'+stdd;
		
        String enddd = termenddate.substring(1,3);
		String endmm = termenddate.substring(4,6);
		String endyyyy = termenddate.substring(7,11);
		String enddate = endyyyy+'-'+endmm+'-'+enddd;
		
        String Session = request.getParameter("session");
        String InventType = "0";
//        System.out.println("Term Start Date : "+termstartdate);
//        System.out.println("Term End Date : "+termenddate);
        term = htmlUtil.findObject("SELECT PERIOD_CODE FROM IA_PERIOD WHERE STATUS = 'A' AND NAME = '"+term+"'");        	
        String suspenseacct = htmlUtil.findObject("SELECT SUSPENSE_ACCOUNT FROM MG_AD_Branch WHERE STATUS = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"'");
        String advanceAcct = htmlUtil.findObject("SELECT ADVANCE_ACCOUNT FROM MG_gb_company WHERE STATUS = 'A' AND company_code = '"+companyCode+"'");
 //       String userId = loginID;
//        String companyCode = companyCODE;
        String strCrAcctExchRate = "1";
        double crAcctExchRate = Double.parseDouble(strCrAcctExchRate);
        String strCrSysExchRate = "1";
        double crSysExchRate = Double.parseDouble(strCrSysExchRate);
		String crAcctType = "ST";
		String crtranCode = "117";
		String drtranCode = "157";
		int drCurrCode = 0;
		int crCurrCode = 0;
		String drReference = "";
		String reference = "";
		String drSBU = "";
		 String effDate = "";
        ArrayList list = new ArrayList();
        ArrayList list_ = new ArrayList();
        ArrayList listc = new ArrayList();
        String recNumber = htmlUtil.getCodeName(" select COUNT(*) from IA_CUSTOMER where COMP_CODE = '"+companyCode+"' AND SCHOOL =  '"+branchCode+"' AND  STATUS = 'CLOSED'");
        if(recNumber == null){recNumber = "0";}
    	int recNo = Integer.parseInt(recNumber);
        String rebateAmount = htmlUtil.getCodeName("SELECT DISCOUNT_AMT FROM IA_INVENTORY_ITEMDETAILS WHERE PRIORITY = '9' AND BRANCH_CODE = '"+branchCode+"' AND PARENT_ID = '2'");
     //   String termalreadystart = htmlUtil.getCodeName("SELECT COUNT(*) FROM  MG_GB_SCHOOL_START WHERE SCHOOL = '"+branchCode+"' AND SESSION = '"+Session+"' AND TERM = '"+termCode+"' AND START_DATE = '"+startdate+"' AND CLOSE_DATE = '"+enddate+"'");
      System.out.println("Term Start Date : "+startdate);
      System.out.println("Term End Date : "+enddate);
        String termalreadystart = htmlUtil.getCodeName("SELECT COUNT(*) FROM  MG_GB_SCHOOL_START WHERE COMP_CODE = '"+companyCode+"' AND SCHOOL = '"+branchCode+"' AND START_DATE = '"+startdate+"' AND CLOSE_DATE = '"+enddate+"'");
   //   System.out.println("Term Already Start: "+termalreadystart);
        // String termalreadystart = htmlUtil.getCodeName("SELECT COUNT(*) FROM  MG_GB_SCHOOL_START WHERE SCHOOL = '"+branchCode+"' AND SESSION = '"+Session+"' AND TERM = '"+termCode+"' ");
        rebateAmount = rebateAmount.replace(",","");
        double Amountrebate = ((rebateAmount != null)&&(!rebateAmount.equals(""))) ? Double.parseDouble(rebateAmount) : 0;                    	
        if(buttSave != null)
        {
        	if(!termalreadystart.equalsIgnoreCase("0")){
       // 		System.out.println("Term Already Start in the Test: "+termalreadystart);
                    out.println("<script>");
                    out.println("alert('Term Already Started Before!....')");
                    out.println("window.location='DocumentHelp.jsp?np=StartNewTerm'");
                    out.println("</script>");        		
            	}        		
        	else{
        //	Apprecord.updateFilesForStartTerm(branchCode); 	
        	list_ = custASHandle.getStudentPayTotalByQuery(branchCode);
        	for(int j=0; j < list_.size(); j++){ 
        		com.magbel.ia.vao.PaymentCategory  item = (com.magbel.ia.vao.PaymentCategory)list_.get(j);        		
        		String IncomeSuspenseAccount = item.getIncomesuspenseacct();
        		String IncomeAccount = item.getSalesacct();
        		String description = item.getDescription();
        		String ItemCode = item.getItemcode();
        		//drReference = item.getItemcode();
        		//String reference = item.getItemcode();
        		String refCode = item.getItemcode();
        		description = description + " FOR THE TERM";
        	String familyid = Apprecord.getId("IA_FAMILY_ID");
        	String sumAmount = "";
        	String query = "WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+branchCode+"'";
        	listc = custASHandle.getAllCustomers(query);
        	double  sumAmountNo = 0.00;
        	double  totalAmount = 0.00;
        	double TotalFee = 0.00;
        	double TotalFeeBoarding = 0.00;
        	double OthersumAmountNo = 0.00;
        	double terminalsumAmt = 0.00;
        	double accountBalance = 0.00;
        	double advanceAmt = 0.00;
        	double tranAmount = 0.00;
        	//String reference = "000";
        	/*   Commented because this has been handled in Registration of Returning students 
        	for(int i=0; i < listc.size(); i++){  
        	com.magbel.ia.vao.Customer  cust = (com.magbel.ia.vao.Customer)listc.get(i);
        	String residency = cust.getResidency();
        	String rebate = cust.getRebate();
        	String AdminNo = cust.getAdmin_no();
        	String courses = cust.getCourses();
        	String semester = cust.getSemester();
        	drReference = refCode+cust.getMtId();
        	reference = refCode+cust.getMtId();
//        	System.out.println("AdminNo  >>>>>>: "+AdminNo+"  courses:  "+courses+"   rebate==>>>: "+rebate);
        	if(rebate==null){rebate = "No";}
        	if(residency.equalsIgnoreCase("Boarding")){
            	String StrSchoolFee = htmlUtil.getCodeName("SELECT SUM(WEIGHT) FROM IA_INVENTORY_ITEMS WHERE BRANCH_CODE = '"+branchCode+"' AND ITEM_CODE = '"+ItemCode+"' AND BRANCH_BALANCE = 'Y' AND ITEMTYPE_CODE = '2' ");
            	StrSchoolFee = StrSchoolFee.replace(",","");
        		double SchoolFeeAmount = ((StrSchoolFee != null)&&(!StrSchoolFee.equals(""))) ? Double.parseDouble(StrSchoolFee) : 0;
            	if(rebate.equalsIgnoreCase("Yes")){  
//            		System.out.println("SchoolFeeAmount Before Amortisation No rebate>>>>>> "+SchoolFeeAmount);
            		SchoolFeeAmount = SchoolFeeAmount - Amountrebate;	
//            		System.out.println("sumAmountNo After Amortisation No rebate>>>>>> "+SchoolFeeAmount+"   Amountrebate:  "+Amountrebate);
            		Amountrebate = 0.00;
            	}                            		
            	String StrBoardingFee = htmlUtil.getCodeName("SELECT SUM(WEIGHT) FROM IA_INVENTORY_ITEMS WHERE BRANCH_CODE = '"+branchCode+"' AND ITEM_CODE = '"+ItemCode+"' AND BRANCH_BALANCE = 'Y' AND ITEMTYPE_CODE = '3' ");
            	StrBoardingFee = StrBoardingFee.replace(",","");
        		double BoardingFeeAmount = ((StrBoardingFee != null)&&(!StrBoardingFee.equals(""))) ? Double.parseDouble(StrBoardingFee) : 0;                      		
        		TotalFeeBoarding = SchoolFeeAmount+ BoardingFeeAmount; 
//        		System.out.println("TotalFeeBoarding With Boarding>>>>>> "+TotalFeeBoarding);
        		sumAmountNo = TotalFeeBoarding;
        		//Narration = "Boarding Fees for the Term";
//        		System.out.println("TotalFeeBoarding With Boarding>>>>>> "+sumAmountNo);
            	} else{ 
                	String StrSchoolFee = htmlUtil.getCodeName("SELECT SUM(WEIGHT) FROM IA_INVENTORY_ITEMS WHERE BRANCH_CODE = '"+branchCode+"' AND ITEM_CODE = '"+ItemCode+"' AND BRANCH_BALANCE = 'Y' AND ITEMTYPE_CODE = '2' ");
                	StrSchoolFee = StrSchoolFee.replace(",","");
            		double SchoolFeeAmount = ((StrSchoolFee != null)&&(!StrSchoolFee.equals(""))) ? Double.parseDouble(StrSchoolFee) : 0;
            		//TotalFee = SchoolFeeAmount;            		
                	if(rebate.equalsIgnoreCase("Yes")){ 
//                		System.out.println("SchoolFeeAmount Before Amortisation with rebate>>>>>> "+SchoolFeeAmount);
                		SchoolFeeAmount = SchoolFeeAmount - Amountrebate;	
//                		System.out.println("sumAmountNo After Amortisation >>>>>> "+SchoolFeeAmount+"   Amountrebate:  "+Amountrebate);
                		Amountrebate = 0.00;
                	}                                  		
            		sumAmountNo = SchoolFeeAmount;
            		//Narration = "Tuition Fees for the Term";
//            		System.out.println("TotalFeeBoarding Without Boarding>>>>>> "+TotalFee);
            	}
        	    double terminalAmount = sumAmountNo;
        		sumAmountNo = OthersumAmountNo + sumAmountNo;
          //      	System.out.println("CustomerAccounServlet courses >>>>>> "+courses+"   semester:  "+semester+"   sumAmountNo: "+sumAmountNo);
            	if((courses.equals("003")) || (courses.equals("006"))){
            		sumAmountNo = terminalAmount*1.5;
            	//	System.out.println("sumAmountNo After Terminal Class >>>>>> "+sumAmountNo);
            	}
            	if(termCode.equals("003")){sumAmountNo = 0.00;
//            	System.out.println("No Payment for Terminal Class >>>>>> "+sumAmountNo);
            	}

        	String query2 = "WHERE STATUS = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"' AND ADMIN_NO = '"+AdminNo+"' ";
    //    	String query2 = "WHERE STATUS = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"' ";         	
        	list = custASHandle.getCustomerAccountSetupByQuery(query2);
        	//int recno = htmlUtil.findintegerObject("SELECT count(*) FROM IA_CUSTOMER WHERE STATUS = 'ACTIVE' AND BRANCH_CODE = '"+branchCode+"'");
//        	System.out.println("Record List=== "+list.size()+" branchCode: "+branchCode+"  AdminNo: "+AdminNo);
        	for(int k=0; k < list.size(); k++){    
        		com.magbel.ia.vao.CustomerAccountSetup  cash = (com.magbel.ia.vao.CustomerAccountSetup)list.get(k);
        		String AccountNo = cash.getAccountNo();
        		String adminNo = cash.getAdmin_no();
        		String drAcctType = cash.getAccountType();
        		String custType = cash.getCustomerType(); 
        		accountBalance = cash.getAccountBalance();
        		double clearbalance = cash.getClearbalance();
        		double termbalance = cash.getTermbalance();
//        		System.out.println("Index Counter: "+k+"  AccountNo: "+AccountNo+"  adminNo:  "+adminNo+"    Account Balance: "+accountBalance);
        		tranAmount = sumAmountNo*-1;
        		//if(accountBalance != 0){tranAmount = sumAmountNo*-1;}
       		serviceBus.createStudentHistory(adminNo,branchCode, drAcctType, AccountNo,  custType, description, tranAmount, crAcctExchRate, crSysExchRate, userId, companyCode,term,accountBalance,drtranCode,familyid,InventType,"",reference);
                	Apprecord.updateStudentAccountAtStartofTermPerAccount(branchCode,userId,sumAmountNo,AdminNo);
        	}
        	
        	Apprecord.updateCRBalances(IncomeSuspenseAccount,branchCode,sumAmountNo);
 //       	System.out.println("sumAmountNo >>>>>> "+sumAmountNo+" Income Account Suspense: "+IncomeSuspenseAccount);                	
        	serviceBus.createGLHistory(branchCode, drCurrCode, crAcctType, IncomeSuspenseAccount, crtranCode, drSBU, description, sumAmountNo, drReference, crAcctExchRate, crSysExchRate, branchCode, crCurrCode, crAcctType, IncomeSuspenseAccount, crtranCode, drSBU, description, sumAmountNo, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode,familyid,term,"","");
//			Advance Postings Entries 
        	if(accountBalance > 0){
        	if(sumAmountNo > accountBalance){
        		advanceAmt = accountBalance;} 
        	else{advanceAmt = accountBalance - sumAmountNo;}
//			Debiting Advance Account with the balance        	
        	Apprecord.updateDRBalances(advanceAcct,branchCode,advanceAmt);                	
        	serviceBus.createGLHistory(branchCode, drCurrCode, crAcctType, advanceAcct, drtranCode, drSBU, Narration, advanceAmt, drReference, crAcctExchRate, crSysExchRate, branchCode, crCurrCode, crAcctType, IncomeSuspenseAccount, drtranCode, drSBU, Narration, advanceAmt, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode,familyid,term,"","");
//			Crediting Income Suspense Account with the balance 
        	Apprecord.updateCRBalances(IncomeSuspenseAccount,branchCode,sumAmountNo);                	
        	serviceBus.createGLHistory(branchCode, drCurrCode, crAcctType, IncomeSuspenseAccount, crtranCode, drSBU, Narration, advanceAmt, drReference, crAcctExchRate, crSysExchRate, branchCode, crCurrCode, crAcctType, advanceAcct, crtranCode, drSBU, Narration, advanceAmt, crAcctExchRate, crSysExchRate, effDate, Integer.parseInt(userId), companyCode,familyid,term,"","");
        	}
        	}
        	End of Commented because this has been handled in Registration of Returning students */
        	}
       // }
        	serviceBus.createTermStartRecord(companyCode, branchCode, termCode, Session, Integer.parseInt(userId), termstartdate, termenddate,recNo);
        	boolean done = serviceBus.UpdateStartSchool(companyCode, branchCode,startdate, enddate);
            out.println("<script>");
            out.println("alert('Term Starts!....')");
            out.println("window.location='DocumentHelp.jsp?np=StartNewTerm'");
            out.println("</script>");
        	}

        }
    }

    public String getServletInfo()
    {
        return "Start New Term Action Servlet";
    }
}
