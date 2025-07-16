package com.magbel.admin.handlers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import audit.AuditTrailGen;

import com.magbel.admin.objects.Approval_Level;
import com.magbel.admin.objects.Aproval_limit;
import com.magbel.admin.objects.AssignSbu;
import com.magbel.admin.objects.Branch;
import com.magbel.admin.objects.ClassPrivilege;
import com.magbel.admin.objects.Leave;
import com.magbel.admin.objects.Resignation;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.DataConnect;
import com.magbel.util.DatetimeFormat;
import com.magbel.admin.objects.Sla;
import com.magbel.admin.objects.Sla2;
import com.magbel.admin.objects.User;
import com.magbel.util.*;
import com.magbel.admin.handlers.MailSender;
import com.magbel.admin.mail.BulkMail;
public class ThreadRun {
	ApplicationHelper applHelper = null;
    MailSender mailSender = null;
    AdminHandler handler;
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date date;
    com.magbel.util.DatetimeFormat df;
    ApplicationHelper help;
    // AdminHandler handler;
    ApplicationHelper apph;
    HtmlUtility util;
    ApprovalRecords aprecords = null; 
    com.magbel.admin.mail.EmailSmsServiceBus mails = null;
    BulkMail mail=null;

    public ThreadRun() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
        help = new ApplicationHelper();
       // mailSender = new MailSender();
        util = new HtmlUtility();
        mails= new com.magbel.admin.mail.EmailSmsServiceBus();
        applHelper = new ApplicationHelper();
        aprecords = new ApprovalRecords();
        
    }

    
    public void ProcessThread()
    {
    	
   /**
    	*am_gb_sla -- sla_id to check select not flag U with sla_response and sla_escalate values
		*HD_COMPLAINT--complaint_id
		*HD_SOLUTION--solution_id
		*HD_PROBLEM--complaint_id
*if(available)
	*{
	*	update due_date and resp_date
    *                    set am_gb_sla flag to U
	*	select am_gb_sla  flag U sla_escalate values check against new due_date and resp_date to send mail
	*	if(success){ set am_gb_sla flag to D}
	*}
    */
    	String Timefld = df.Time();
    	String Timefld1 = Substring(Timefld,0,7);
	     String[] TimeSplit = Timefld.split(":");
	    String TimeSplit2 = TimeSplit[2];
//	    System.out.println("Timefld1=======> "+Timefld1);
//    	System.out.println("Timesplit=======> "+TimeSplit[0]+" "+TimeSplit[1]+" "+Substring(TimeSplit2,0,3));
//    	System.out.println("TimeSplit[2]=======> "+TimeSplit[2]);
    	String [] Split2Time = TimeSplit2.split(" ");
    	String Split2Time0 = Split2Time[0];
    	String Split2Time1 = Split2Time[1];
 //  	System.out.println("Split2Time=======> "+Split2Time0);
//    	System.out.println("Curren Time Is=======> "+Timefld);
    	if(Timefld.equalsIgnoreCase("2:0:0 PM")){
    		updateMailTime();
 //       	System.out.println("Update of Mail Status=======> "+Timefld);
    	}

    	//SELECT UNPROCESSED SLA
 // 	  System.out.println("----TREADRUN Id---");
  	 
    	//  java.util.ArrayList list = (java.util.ArrayList) getSlaList("U");
    	 java.util.ArrayList listMail = (java.util.ArrayList) getMailIssuesList();
    	  for(int i=0; i<listMail.size(); i++)
    	  {
    		  com.magbel.admin.objects.MailRecords comp=(com.magbel.admin.objects.MailRecords)listMail.get(i);
    		  String Id = comp.getid();
    		  String Mail_msg = comp.getmsg();
    		  String Mailrecv_dt = comp.getrecv_dt();
    		  String Mail_Sender = comp.getsender();
    		  String Mail_Status = comp.getStatus();
    		  String Mail_Title = comp.gettitle();    		  
//    		  System.out.println("----MAILER Id---"+Id);
//    		  System.out.println("----MAILER Mail_Sender---"+Mail_Sender);
//    		  System.out.println("---- Mail_Title Id---"+Mail_Title);
          	  if(Mail_Status!="TREATED"){
          		insertIssues(Id,Mail_Sender,Mail_Title,Mailrecv_dt,Mail_msg);          		
          		updateMAILER(Id);
          	  }

          	 
    	  }
   //This thread sends mail on new issues raised  	  
      	//  java.util.ArrayList list = (java.util.ArrayList) getSlaList("U");
     	 java.util.ArrayList listIssues = (java.util.ArrayList) getIssuesList();
     	  for(int i=0; i<listIssues.size(); i++)
     	  {
     		  com.magbel.admin.objects.NewIssueMailRecord comp=(com.magbel.admin.objects.NewIssueMailRecord)listIssues.get(i);
     		  String Id = comp.getid();
     		  String complaintId = comp.getcomplaintId();
     		  String category = comp.getcategory();
     		  String subcategory = comp.getsubcategory();
     		  String assetid = comp.getassetId();  		  
     		  String priority = comp.getpriority();
     		  String Status = comp.getstatus();
     		  String create_date = comp.getcreate_date();
     		  String create_dateTime = comp.getcreate_dateTime();
     	     String complaintType = comp.getcomplaintType();
    		  String userId = comp.getuserId();
       		 System.out.println("=====userId in ArrayList====>>> "+userId);     		  
       		  String user_Id = comp.getuserId();
       		 System.out.println("=====user_Id in ArrayList====>>> "+user_Id);       		  
     		  String requestSubject = comp.getrequestSubject();   
     		  String requestDescription = comp.getrequestDescription();
     		  String companyCode = comp.getcompanyCode();
     		  String requesterId = comp.getrequesterId();
     		  String NewMail_Status = comp.getMailIssue();
     		  String technician = comp.gettechnician(); 
     		  String notifymail = comp.getnotify_Email();     		  
         	   userId = (userId == null || userId.equals(""))?"0":userId;
           	String SenderDeptQry = "SELECT  dept_code   FROM   AM_GB_USER where User_id = " + userId;
           	System.out.println("=====SenderDeptQry====>>> "+SenderDeptQry);
         	String SenderDept = aprecords.getCodeName(SenderDeptQry);  
        	System.out.println("=====SenderDept====>>> "+SenderDept);
              String SenderQry = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = " + SenderDept;
             	System.out.println("=====SenderQry====>>> "+SenderQry);
              String Sender = aprecords.getCodeName(SenderQry); 
              System.out.println("=====Sender====>>> "+Sender);
              System.out.println("=====notifymail====>>> "+notifymail);
           //   Id, category,subcategory,assetId,priority,status,create_date,create_dateTime,complaintType,userId,incidentMode,MailIssue,requesterId,requestDescription,requestSubject,complaintId,companyCode,technician,notifyEmail);              
         	 if(notifymail==null || notifymail.equalsIgnoreCase("")){
            	 notifymail = Sender;
            	 System.out.println("=====notifymail 2====>>> "+notifymail);
            	 } else { 
            	   	 System.out.println("=====notifymail 3====>>> "+notifymail);
                	 notifymail = notifymail + "," +Sender;
                 }       	
         	System.out.println("=====category====>>> "+category);
     	      String pageType = "001";
     	      String pageName = "Incident"; 
     	      String FileName = "HD_COMPLAINT";
     	      String FieldName = "complaint_id";
     	      String TransactionType = "001";  
              String UnitHeadMailQuery = "SELECT UnitMail   FROM   AM_AD_DEPARTMENT where Dept_code = " + category;
              System.out.println("=====UnitHeadMailQuery====>>> "+UnitHeadMailQuery);
              String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);              
              String mailto = UnitHeadMail;
     	      String url = "C:/jboss-4.2.2.GA/server/Oriental/deploy/Oriental.war";
     	      //String url = "C:/jboss-4.0.5.GA/server/default/deploy/Oriental.war";

//              System.out.println("Subject===== "+Subject);
              System.out.println("url===== "+url); 
              System.out.println("NewMail_Status===== "+NewMail_Status);

				String ResolvedBy = "";   
				String Assignee=util.findObject("SELECT technician, request_Subject   FROM   HD_COMPLAINT WHERE complaint_id = '"+complaintId+"'");
				System.out.println("=====Assignee Before ===== "+Assignee);
			  	  if(Assignee == null || Assignee.equalsIgnoreCase("")){
					 ResolvedBy=util.findObject("SELECT UnitHead, email   FROM   AM_AD_DEPARTMENT WHERE Dept_code = '"+category+"'");
					// System.out.println("=====Assignee is Null ===== "+ResolvedBy);
				}else{
				ResolvedBy =util.findObject("select full_name from am_gb_user where user_id  = '"+Assignee+"'");
				// System.out.println("=====Assignee is Not Null ===== "+ResolvedBy);
				}  
//				System.out.println("=====ResolvedBy ===== "+ResolvedBy); 
				String statusMail=util.findObject("select status_description from hd_status where status_code='"+Status+"'");
//				System.out.println("=====statusMail ===== "+statusMail);
				//String	Subject="Subject: Notification for Issue : "+complaintId;
				String tech=util.findObject("select full_name from am_gb_user where user_id='"+userId+"'");		
					tech=tech=="UNKNOWN" ? "work in progress" :tech;
//					System.out.println("=====tech ===== "+tech);
				String dateReported=util.findObject("select create_date from "+FileName+" where "+FieldName+"='"+complaintId+"'");	
				dateReported=dateReported=="UNKNOWN" ? String.valueOf(df.dateConvert(new java.util.Date())) :dateReported;
				Date Date1 = df.dateConvert(new java.util.Date());		 
	              String Subject = requestSubject + " " +complaintId +" "+statusMail;
           	  if(NewMail_Status!="N"){    
           		  System.out.println("url= "+url +" Subject "+Subject+" TransactionType "+TransactionType+" category "+category+" subcategory "+subcategory+" pageType "+pageType+" technician "+technician+" userId "+userId+" requestDescription"+requestDescription+" FileName "+FileName+" Subject "+Subject+" Status "+Status+" notifymail "+notifymail+" userId "+userId+" mailto "+mailto);           		  
           		mails.SimpleMailWithAttachment(url, Subject, mails.getNewEmailMessageOriginal(TransactionType, category,subcategory, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName,Subject,Status,"","","","",Assignee,ResolvedBy,statusMail,tech,dateReported,Date1),notifymail,userId,mailto);
           		NewMail_Status = "Y";
      		  System.out.println("----HD_COMPLAINT NewMail_Status---"+NewMail_Status);           		
           	//	insertIssues(Id,Mail_Sender,Mail_Title,Mailrecv_dt,Mail_msg);          		
      		DeleteMailSent_Record(complaintId);
           	  }
   
           	 
     	  }    	  
    	  
    	// 	System.out.println("----ProcessThread---");
    	//  java.util.ArrayList list = (java.util.ArrayList) getSlaList("U");
    	 java.util.ArrayList list1 = (java.util.ArrayList) getComplaintList();
    	  for(int i=0; i<list1.size(); i++)
    	  {
    		  com.magbel.admin.objects.Sla2 comp=(com.magbel.admin.objects.Sla2)list1.get(i);
    		  String Id = comp.getReqnID();
    		  String Slaid = comp.getSLAId();
    		  String SLA_CODE = comp.getPriority();
    		  String Depart_Code = comp.getDept_Code();
    		  String Issue_Code = comp.getCat_Code(); 
    		  String Complaint_Type = comp.getComplaintType();
    		  String IssueTime = comp.getCreate_Time();
    		  String[] IssueTimeValue = IssueTime.split(":");
          	  String SplitIssueHr = IssueTimeValue[0];
          	  String SplitIssueMin = IssueTimeValue[1];
          	  String SplitIssueSec = IssueTimeValue[2]; 
          	  String IssueTimeSum = SplitIssueHr+SplitIssueMin+SplitIssueSec;
//          	  System.out.println("IssueTimeSum=======> "+IssueTimeSum);
          	  int IssueTimeInt = Integer.parseInt(IssueTimeSum);
//     		  System.out.println("----HD_COMPLAINT Depart_Code---"+Depart_Code);
//    		  System.out.println("----HD_COMPLAINT Issue_Code---"+Issue_Code);
//    		  System.out.println("----HD_COMPLAINT Slaid---"+Slaid);
//    		  System.out.println("---- ProcessThread HD_COMPLAINT Id---"+Id);
          	  int ResponseDatTime = 0;
          	  int ResoluteDayTime = 0;
    		  //GET RESPONSE AND SLA ID
    		//  com.magbel.admin.objects.Sla2 slaid=  getSlaId(Depart_Code,Issue_Code);
    		  //GET RESPONSE AND ESCALATE VALUE
    	        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_ESCALATE where Dept_Code = '"+Depart_Code+"' AND Cat_Code = '"+Issue_Code+"' ";
//    	        System.out.println("----SlaQuery---"+SlaQuery);
    	        boolean EscaAvailable = aprecords.RecordAvailable(SlaQuery); 
//    	        System.out.println("=====RecAvailable 1 === "+EscaAvailable); 
            if (EscaAvailable ==true){
    		  com.magbel.admin.objects.RuleConstraints cons=  getSlaEscalateList(Depart_Code,Issue_Code);
    		  String responseName=cons.getNAME();  
//    		  System.out.println("----responseName---"+responseName);
//         	  String responseDay=cons.getRESOLVE_DAY();
 //         	  String responseHr = cons.getRESOLVE_HOUR();
 //         	  String responseMin = cons.getRESOLVE_MINUTE();
//          	  System.out.println("----responseDay---"+responseDay);
          	  String responseConstraiant=cons.getCONSTRAINT2();
//          	  System.out.println("----responseConstraiant---"+responseConstraiant);
          	  String resolveDay=cons.getRESOLVE_DAY();
          	  String resolveHR = cons.getRESOLVE_HOUR();
          	  String resolveHour = cons.getRESOLVE_HOUR();
          	  String resoveSEC = cons.getRESOLVE_MINUTE();
          	  String SplitHr = TimeSplit[0];
          	  String SplitMin = TimeSplit[1];
          	  String SplitSec = TimeSplit[2];
          	  String Systime = SplitHr+SplitMin+Split2Time0;
//          	System.out.println("Systime=======> "+Systime);
          	  int SystemTime = Integer.parseInt(Systime);
          	  int DueTime = SystemTime - IssueTimeInt;
          	  DueTime = Math.round(DueTime/100);
 //         	System.out.println("SystemTime=======> "+SystemTime);
//          	System.out.println("IssueTimeInt=======> "+IssueTimeInt);
//          	System.out.println("DueTime=======> "+DueTime);
//          	System.out.println("----resolveHR Before Conversion----- "+cons.getRESOLVE_HOUR());
//          	System.out.println("----resolveHour Before Conversion----- "+resolveHour);
//      		 System.out.println("----SplitHr---- "+Integer.parseInt(SplitHr));      		
/*          	if (responseDay.equals("")){
         	  System.out.println("----resolutionDayTime---"+responseDay);} */ 
          	if (resolveDay==null || resolveDay.equalsIgnoreCase("")){resolveDay = "0";}     
          	  if (resolveDay!=null && resolveDay != "0"){
//          		  System.out.println("----resolutionDayTime Is Not Blank");  
          		 ResoluteDayTime = Integer.parseInt(resolveDay);
             	  //CHECK TABLES AND UPDATE 
             	  if(getUpdate(Id,"HD_COMPLAINT","complaint_id","priority")){
             		  updateHdComplaint(Id,ResponseDatTime,ResoluteDayTime);
             	  }          		   
          	  }       
 //         	System.out.println("----resolutionDayTime---"+resolveDay);
 //         	System.out.println("----resolveHR---"+resolveHR);
 //         	System.out.println("----SplitHr---"+SplitHr);
  //        	 if (resolveDay == "0" && Integer.parseInt(SplitHr) > Integer.parseInt(resolveHour) ){
          	  if (resolveDay == "0" && DueTime > Integer.parseInt(resolveHour) ){
          	//if (resolveDay == "0"){          		  
//     		  System.out.println("----resolutionDayTime Is Blank");
//        		System.out.println("----resolveHour Conversion----- "+Integer.parseInt(resolveHour));
//     		 System.out.println("----Integer.parseInt(SplitHr)---- "+Integer.parseInt(SplitHr));
//     		System.out.println("----Integer.parseInt(resolveHour)----- "+Integer.parseInt(resolveHour));
          		  ResoluteDayTime = 0;          	 
          	  //CHECK TABLES AND UPDATE   
          		  if(getUpdate(Id,"HD_COMPLAINT","complaint_id","priority")){
          			  updateHdComplaint(Id,ResponseDatTime,ResoluteDayTime);
          		  }
          	  }
        	            	            	  
/*          	  com.magbel.admin.objects.RuleConstraints con=  getSlaResponseList(Depart_Code,Issue_Code);
          	  String resolutionName=con.getNAME();
        	  String resolutionDay=con.getRESOLVE_DAY();
        	  String resolutionConstraiant=con.getCONSTRAINT2(); */
            } 
//	        String ResolveQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+Depart_Code+"' AND Cat_Code = '"+Issue_Code+"' ";
//	        System.out.println("----ResolveQuery---"+ResolveQuery);
//	        boolean ResolveAvailable = aprecords.RecordAvailable(SlaQuery); 
//	        System.out.println("=====ResolveAvailable 1 === "+ResolveAvailable); 
 //       if (ResolveAvailable ==true){
        	  //GET REPONSE AND RESOLVE TIME VALUE  
/*        	  com.magbel.admin.objects.RuleConstraints co=  getSlaResolveResponseTime(Depart_Code,Issue_Code);
        	  String reponseDayTime=co.getRESPONSE_DAY();
//        	  System.out.println("----reponseDayTime---"+reponseDayTime);
          	  String resolutionDayTime=co.getRESOLVE_DAY();
         	  String resolveHR = co.getRESOLVE_HOUR();
          	  String SplitHr = TimeSplit[0];
          	  String SplitMin = TimeSplit[1];
          	  String SplitSec = TimeSplit[2];*/
 /*         	if (resolutionDayTime.equals("")){
         	  System.out.println("----resolutionDayTime---"+resolutionDayTime);}*/
          	 /* if (reponseDayTime!=null){    
          		  System.out.println("----reponseDayTime Is Blank");
          		  ResponseDatTime = Integer.parseInt(reponseDayTime);   
          	  } */   
         // 	if (resolutionDayTime==null || resolutionDayTime.equalsIgnoreCase("")){resolutionDayTime = "0";}     
         /* 	  if (resolutionDayTime!=null && resolutionDayTime != "0"){
//          		  System.out.println("----resolutionDayTime Is Blank");
          		 ResoluteDayTime = Integer.parseInt(resolutionDayTime);
             	  //CHECK TABLES AND UPDATE 
             	  if(getUpdate(Id,"HD_COMPLAINT","complaint_id","priority")){
             		  updateHdComplaint(Id,ResponseDatTime,ResoluteDayTime);
             	  }          		   
          	  }  */

   //     }  
    	  }
   	  
    	  ProcessThread2();	  

    	if(Timefld.equalsIgnoreCase("2:0:0 PM")){
    	  ProcessThread3();
    	  ProcessThread4();
    	}
    } 

      
    
    private String Substring(String string, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}


	public void ProcessThread2()
    { 
    	File f=new File(""); 
    	String path=f.getPath();
//    	  java.util.ArrayList listMail = (java.util.ArrayList) getSlaMailList("U");
//    	  for(int i=0; i<listMail.size(); i++)
           	  java.util.ArrayList listMail1 = (java.util.ArrayList) getComplaintMailList();
//           	System.out.println("----ProcessThread2---");
    	  for(int i=0; i<listMail1.size(); i++)    		
    	  {   // System.out.println("----Mail Index---"+i);
    		  com.magbel.admin.objects.Sla2 sla2=(com.magbel.admin.objects.Sla2)listMail1.get(i);
     		  String Id = sla2.getReqnID();   
     		  String Slaid = sla2.getSLAId();
     		  String SLA_CODE = sla2.getPriority();
     		  String requestSubject = sla2.getrequestSubject();
     		  String requestDescription = sla2.getrequestDescription();
     		  String Status_Code = sla2.getstatus();
     		  String technician = sla2.getTechnician();
    		  String Depart_Code = sla2.getDept_Code();
    		  String Issue_Code = sla2.getCat_Code(); 
    		  String TransactionType = sla2.getComplaintType();
    		  String complaintId = sla2.getReqnID();
    		  String ResolvedDate = sla2.getResolveDue_Date();
    		  String ResponseDate = sla2.getResponseDue_Date();  
    		  String MailStatus = sla2.getmailstatus();
    		  //GET RESPONSE AND ESCALATE VALUE  
 	        String SlaQuery = "SELECT  criteria_ID   FROM   SLA_ESCALATE where Dept_Code = '"+Depart_Code+"' AND Cat_Code = '"+Issue_Code+"' ";
	        boolean EscalAvailable = aprecords.RecordAvailable(SlaQuery); 
//	        System.out.println("----Mail EscalAvailable---"+EscalAvailable);
            if (EscalAvailable ==true){
    		  com.magbel.admin.objects.RuleConstraints cons=  getSlaEscalateList(Depart_Code,Issue_Code);
    		
    		  String responseName=cons.getNAME();
//    		  System.out.println("----responseName---"+responseName);
          	  String responseDay=cons.getRESPONSE_DAY();
//         	  System.out.println("----responseDay---"+responseDay);
          	  String responseConstraiant=cons.getCONSTRAINT2();
//          	  System.out.println("----responseConstraiant---"+responseConstraiant);
          	  
          	  com.magbel.admin.objects.RuleConstraints con=  getSlaResponseList(Depart_Code,Issue_Code);
          	  String resolutionName=con.getNAME();          	  
//          	  System.out.println("----resolutionName---"+resolutionName);
        	  String resolutionDay=con.getRESOLVE_DAY();
        	  if (resolutionDay==null || resolutionDay.equalsIgnoreCase("")){resolutionDay = "0";}   
//        	  System.out.println("----resolutionDay---"+resolutionDay);
        	  String resolutionConstraiant=con.getCONSTRAINT2();
              User user = getescalateMail(technician);
              String technicianmailaddress = user.getEmail();
              String escalaltemailaddress = con.getNAME();
//        	  System.out.println("----resolutionConstraiant---"+resolutionConstraiant);
//        	  com.magbel.admin.objects.User user=getescalateMail(resolutionName); 
        	//  String escalaltemailaddress=user.getEmail();  
//          	  System.out.println("----emailaddress---"+emailaddress);
        	  //GET REPONSE AND RESOLVE TIME VALUE   
   	        String ReslveQuery = "SELECT  criteria_ID   FROM   SLA_RESPONSE where Dept_Code = '"+Depart_Code+"' AND Cat_Code = '"+Issue_Code+"' ";
	        boolean ResolveAvailable = aprecords.RecordAvailable(ReslveQuery);
            String UnitHeadMailQuery = "SELECT  email   FROM   AM_AD_DEPARTMENT where Dept_code = '"+Depart_Code+"'";
            String UnitHeadMail = aprecords.getCodeName(UnitHeadMailQuery);


            if (ResolveAvailable ==true){

        	  com.magbel.admin.objects.RuleConstraints co=  getSlaResolveResponseTime(Depart_Code,Issue_Code);
        	  String reponseDayTime=co.getRESPONSE_DAY();
//        	  System.out.println("----reponseDayTime---"+reponseDayTime);
          	  String resolutionDayTime=co.getRESOLVE_DAY();
//         	  System.out.println("----resolutionDayTime---"+resolutionDayTime);
            }
          	  //CHECK TABLES AND SEND MAIL
          	  if(getMailCheck(Id,"HD_COMPLAINT","complaint_id")){
          	  String mailRecords=getMail(Id,"HD_COMPLAINT","complaint_id");
          	  String [] records=mailRecords.split(","); 
          	  String Subject = requestSubject;
          	 // String dueDateValue=records[0];  
          	  String dueDateValue = mailRecords.substring(0, 19); 
              	String pageType = "005";
              	String userId = "2";   
              	String notifyEmail = escalaltemailaddress.trim() + UnitHeadMail.trim();     
              	String transactionType = "002";
                String FileName = "HD_COMPLAINT";
                String FieldName = "complaint_id";
          	dueDateValue=dueDateValue=="" ? "" : df.reArrangeDate(dueDateValue) ;
          	String mailto = technicianmailaddress;
        	// System.out.println("*****After reArrangeDate**** "+dueDateValue);    	 
          	String resolveDueDate=df.addDayToDate(df.dateConvert(dueDateValue),Integer.parseInt(resolutionDay));
            com.magbel.admin.objects.ComplaintResponse coms=  getComplaintMailInfo(Id);
            String category = coms.getCategory();
            String SubCategory = coms.getSubCategory();
            String requesttype = coms.getItemType();
            String ResolveDateDue = ResolvedDate.substring(0, 10);
   //         System.out.println("=====resolveDueDate Before Breaking=== "+ResolveDateDue);  
            String Day = ResolvedDate.substring(8, 10);
            String Year = ResolvedDate.substring(0, 4);
            String Month = ResolvedDate.substring(4, 8);
            ResolveDateDue = Day+Month+Year;     
            String statusMail = util.findObject("select status_description from hd_status where status_code='"+Status_Code+"'");
            String tech = util.findObject("select full_name from am_gb_user where user_id='"+technician+"'");
            tech = tech != "UNKNOWN" ? tech : "work in progress";
            String dateReported = util.findObject((new StringBuilder("select create_date from ")).append(FileName).append(" where ").append(FieldName).append("='").append(complaintId).append("'").toString());
            dateReported = dateReported != "UNKNOWN" ? dateReported : String.valueOf(df.dateConvert(new Date()));
          //  System.out.println("=====MailStatus Before Mail=== "+MailStatus);  
            if(MailStatus.equalsIgnoreCase("N")){ 
            if(df.dateConvert(new java.util.Date()).after(df.dateConvert(ResolveDateDue)) || df.dateConvert(new java.util.Date()).equals(df.dateConvert(ResolveDateDue)))
          	{
        //    	System.out.println("+++ System Inside===== ");
            	  
          //		String url = path;    
            	String url = "C:/jboss-4.2.2.GA/server/Oriental/deploy/Oriental.war";
//            	String url = "C:/jboss-4.0.5.GA/server/default/deploy/Oriental.war";
           // 		 System.out.println("<<<<path>>>>>> "+path);
          		//technician = "29";
          	//	mail.getEmailMessageOriginal("001", "016", "016", "001", "29", "2", "Creating guest issue","Req/414","Guest Issue","HD_COMPLAINT","complaint_id");
          		//mail.getEmailMessage("001", "016", "001", "2", "2", "Creating guest issue","Req/412");
 //         		 System.out.println("TransactionType = "+TransactionType+" category = "+category+" SubCategory = "+category+" pageType = "+pageType+" technician = "+technician+" userId = "+userId+" requestDescription = "+requestDescription);
 //         		System.out.println("complaintId = "+complaintId+" requestSubject = "+requestSubject+" FileName = "+FileName+" FieldName = "+FieldName+" notifyEmail = "+notifyEmail+" userId = "+userId);
            	updateMailStatus(complaintId); MailStatus = "Y";
                mails.SimpleMailWithAttachment(url, Subject, mails.getEmailMessageEscalate(transactionType, Depart_Code, Issue_Code, pageType, technician, userId, requestDescription, complaintId, requestSubject, FileName, FieldName, statusMail, tech, dateReported), notifyEmail, userId, mailto);
   
      //    		mails.SimpleMailWithAttachment(url, "ESCALATE", mails.getEmailMessageEscalate(transactionType, Depart_Code, Issue_Code, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId);          		
                //mails.SimpleMailWithAttachment(url, "ISSUES", mails.getEmailMessageThread(TransactionType, category, Issue_Code, pageType, technician, userId, requestDescription,complaintId,requestSubject,FileName,FieldName),notifyEmail,userId);                
          	} 
            }
          	  } 
            } 
                      	 
    	  }
    }
    public void ProcessThread3()
    { 
//     	System.out.println("----ProcessThread3---");
    	File f=new File(""); 
    	String path=f.getAbsolutePath();
           	  java.util.ArrayList listMail1 = (java.util.ArrayList) getTASkMailList();
         //  	System.out.println("----Mail listMail1.size---"+listMail1.size());
    	  for(int i=0; i<listMail1.size(); i++)    		
    	  {   // System.out.println("----Mail Index---"+i);
    		  com.magbel.admin.objects.Sla2 sla2=(com.magbel.admin.objects.Sla2)listMail1.get(i);
     		  String Id = sla2.getReqnID();
     		  String Slaid = sla2.getSLAId();
     		//  String SLA_CODE = sla2.getPriority();
     		  String requestSubject = sla2.getrequestSubject();
     		  String requestDescription = sla2.getrequestDescription();
     		  String Status_Code = sla2.getstatus();
     		  String technician = sla2.getTechnician();
    		  String Depart_Code = sla2.getDept_Code();
    		  String Issue_Code = sla2.getCat_Code(); 
    		  String TransactionType = sla2.getComplaintType();
    		  String complaintId = sla2.getReqnID();
    		  String DueDate = sla2.getResolveDue_Date();
    		  String MailBefore = sla2.getResponseDue_Date().substring(0, 2);

    		  //GET RESPONSE AND ESCALATE VALUE  
           //   	String pageType = "001";
              	String userId = "2";   
              	
              	String notifyEmail = util.findObject("select full_name from am_gb_user where user_id  = '"+technician+"'");
           //   	String transactionType = "002"; 
           //     String FileName = "HD_COMPLAINT";
           //     String FieldName = "complaint_id";
                DueDate=DueDate=="" ? "" : df.reArrangeDate(DueDate) ;  
            if(df.dateConvert(new java.util.Date()).after(df.dateConvert(DueDate)) || df.dateConvert(new java.util.Date()).equals(df.dateConvert(DueDate)))
          	{
            //	System.out.println("+++ System Inside===== ");
          		String url = path;  
                mails.SimpleMailWithAttachment(url, "TASK", "MSG BODY",notifyEmail,technician,"");

          	} 
                      	 
    	  }
    }

    public void ProcessThread4()
    { 
    	File f=new File(""); 
    	String path=f.getAbsolutePath();
//     	System.out.println("----ProcessThread4---");
           	  java.util.ArrayList listMail1 = (java.util.ArrayList) getAnnouncementMailList();
           //	System.out.println("----Mail listMail1.size---"+listMail1.size());
    	  for(int i=0; i<listMail1.size(); i++)    		
    	  {    //System.out.println("----Mail Index---"+i);
    		  com.magbel.admin.objects.Sla2 sla2=(com.magbel.admin.objects.Sla2)listMail1.get(i);
     		  String Id = sla2.getReqnID();
     		  String Slaid = sla2.getSLAId();
     		//  String SLA_CODE = sla2.getPriority();
     		  String requestSubject = sla2.getrequestSubject();
     		  String requestDescription = sla2.getrequestDescription();
     		  String Status_Code = sla2.getstatus();
     		  String technician = sla2.getTechnician();     
    		 // String Depart_Code = sla2.getDept_Code();
    		//  String Issue_Code = sla2.getCat_Code(); 
    		  String TransactionType = sla2.getComplaintType();
    		  String MailCopy = sla2.getComplaintType();    		 
    		  String complaintId = sla2.getReqnID();
    		  String DueDate = sla2.getResolveDue_Date();
    		  String MailTo = sla2.getResponseDue_Date();
//     		  System.out.println("----Mail ProcessThread3 Slaid---"+Slaid);
     	//	  System.out.println("----Mail SLA_CODE---"+SLA_CODE);
//     		  System.out.println("----Mail ProcessThread3 Slaid---"+Slaid);
 //    		  System.out.println("----Mail ProcessThread4 Id---"+Id);
//     		  System.out.println("----Mail ProcessThread3 requestSubject---"+requestSubject);
//     		  System.out.println("----Mail ProcessThread3 requestDescription---"+requestDescription);
//     		  System.out.println("----Mail ProcessThread3 Status_Code---"+Status_Code);
     	//	 System.out.println("----Mail Depart_Code---"+Depart_Code);
     	//	 System.out.println("----Mail Issue_Code---"+Issue_Code);
     		 //System.out.println("----Mail ProcessThread3 technician---"+technician);
//     		 System.out.println("----Mail ProcessThread3 ResolvedDate---"+DueDate);
//     		 System.out.println("----Mail ProcessThread3 MailBefore---"+MailTo);
//     		 System.out.println("----Mail ProcessThread3 TransactionType---"+TransactionType);
    		  //GET RESPONSE AND ESCALATE VALUE  
              	String pageType = "001";
              	String userId = "2";   
              	//String notifyEmail = "lekan_matanmi@lycos.com";     
              	String transactionType = "001";
                String FileName = "HD_ANNOUNCEMENT";
                String FieldName = "Announce_id";
                DueDate=DueDate=="" ? "" : df.reArrangeDate(DueDate) ;  
            if(df.dateConvert(new java.util.Date()).after(df.dateConvert(DueDate)) || df.dateConvert(new java.util.Date()).equals(df.dateConvert(DueDate)))
          	{
//            	System.out.println("+++ System Inside===== ");
          		String url = path;  
                mails.SimpleMailWithAttachment(url, "ANNOUNCEMENT", "MSG BODY",MailCopy,technician,"");                
          	} 
                      	 
    	  }
    }
    
      
    public java.util.List getSlaMailList(String process) 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla sla= null;
        String query = "SELECT *  FROM am_gb_sla where STATUS = 'Y' and (mailed ='"+process+"' or mailed is null)  ";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
 
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String branchId = rs.getString("sla_ID");
                String branchCode = rs.getString("sla_name");
                String branchName = rs.getString("sla_description"); 
                sla = new com.magbel.admin.objects.Sla (branchId,branchCode,branchName,"","","","");

                _list.add(sla);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getSlaMailList Selecting sla ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
    public java.util.List getComplaintMailList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla2 sla2= null;
        String query = "SELECT *  FROM HD_COMPLAINT where (status ='001' OR status = '005') and MAIL_STATUS = 'N' and Due_Date is not null  ";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
 
        try {
            c = getConnection();
            s = c.createStatement(); 
            rs = s.executeQuery(query);
            while (rs.next()) {           
                String ReqnID = rs.getString("complaint_id");
                String SLAId = rs.getString("sla_id");
            	String Priority = rs.getString("Priority");
                String requestSubject = rs.getString("request_Subject");
                String requestDescription = rs.getString("request_Description");
                String Technician = rs.getString("Technician");
                String status = rs.getString("status");
                String Depart_Code = rs.getString("complain_category");
                String Issue_Code = rs.getString("complain_sub_category"); 
                String TransactionType = rs.getString("complaint_Type");
                String ResolvedDueDate = rs.getString("Due_Date");
                String ResponsedDueDate = rs.getString("Resp_Date");
                String createdate = rs.getString("create_date");
                String MailStatus = rs.getString("Mail_Status");
                sla2 = new com.magbel.admin.objects.Sla2 (ReqnID,Priority,SLAId,requestSubject,requestDescription,status,Technician,Depart_Code,Issue_Code,TransactionType,ResolvedDueDate,ResponsedDueDate,createdate,MailStatus,"");
                _list.add(sla2);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getComplaintMailList Selecting sla ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
    public java.util.List getTASkMailList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla2 sla2= null;
        String query = "SELECT *  FROM HD_TASK where status ='001' ";

       //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
 
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {           
                String ReqnID = rs.getString("Task_id");
                String SLAId = rs.getString("id");
                String requestSubject = rs.getString("Task_Title");
                String requestDescription = rs.getString("Task_Content");
                String Technician = rs.getString("Technician");
                String status = rs.getString("status");
                String DueDate = rs.getString("Actual_End_Date");
                String MailBefore = rs.getString("Email_Me_Before");
                
                sla2 = new com.magbel.admin.objects.Sla2 (ReqnID,"",SLAId,requestSubject,requestDescription,status,Technician,"","","",DueDate,MailBefore,"","","");
                _list.add(sla2);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getTASkMailList Selecting HD_TASK ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.List getAnnouncementMailList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla2 sla2= null;
        String query = "SELECT *  FROM HD_ANNOUNCEMENT where SEND_MAIL ='Y' ";

       //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
 
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {           
                String ReqnID = rs.getString("Announce_id");
                String SLAId = rs.getString("ID");
                String requestSubject = rs.getString("Announce_Title");
                String requestDescription = rs.getString("Announce_Content");
              //  String Technician = rs.getString("Technician");
                String status = rs.getString("SEND_MAIL");
                String DueDate = rs.getString("Schedule_End_Date");
                String MailTo = rs.getString("Email_TO");
                String MailCopy = rs.getString("Email_COPY");
                sla2 = new com.magbel.admin.objects.Sla2 (ReqnID,"",SLAId,requestSubject,requestDescription,status,"","","",MailCopy,DueDate,MailTo,"","","");
                _list.add(sla2);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getAnnouncementMailList Selecting HD_ANNOUNCEMENT ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }



    public java.util.List getSlaList(String process) 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla sla= null;
        String query = "SELECT *  FROM am_gb_sla where STATUS = 'Y' and (process !='"+process+"' or process is null) ";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try { 
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String branchId = rs.getString("sla_ID");
                String PriorityId = rs.getString("sla_Code");
                String branchCode = rs.getString("sla_name");
                String branchName = rs.getString("sla_description"); 
                sla = new com.magbel.admin.objects.Sla (branchId,PriorityId,branchCode,branchName,"","","");

                _list.add(sla);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting sla ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list; 

    }
  
    public java.util.List getComplaintList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla2 comp= null;
        String query = "SELECT *  FROM HD_COMPLAINT where status ='001' ";
   //     System.out.println("KKKKKK query KKKKKK>>>> getComplaintList "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try { 
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String Dept_Code = rs.getString("complain_category");
                String Cat_Code = rs.getString("complain_sub_category"); 
//                comp = new com.magbel.admin.objects.Sla2(branchId, PriorityId,SLAId,branchCode);
                String ReqnID = rs.getString("complaint_id");
                String SLAId = rs.getString("sla_id");
            	String Priority = rs.getString("Priority");
                String requestSubject = rs.getString("request_Subject");
                String requestDescription = rs.getString("request_Description");
                String Technician = rs.getString("Technician");
                String status = rs.getString("status");
                String Depart_Code = rs.getString("complain_category");
                String Issue_Code = rs.getString("complain_sub_category"); 
                String TransactionType = rs.getString("complaint_Type");
                String ResolveDate  = rs.getString("Due_Date");
                String ResponseDate  = rs.getString("Resp_Date");
                String ResponseTime  = rs.getString("Create_Time");
                comp = new com.magbel.admin.objects.Sla2 (ReqnID,Priority,SLAId,requestSubject,requestDescription,status,Technician,Depart_Code,Issue_Code,TransactionType,ResolveDate,ResponseDate,"","",ResponseTime);

                _list.add(comp); 
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getComplaintList Selecting HD_COMPLAINT ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
/*    
    public java.util.List getProblemList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sla2 comp= null;
        String query = "SELECT *  FROM HD_PROBLEM where status !='002' ";
//        System.out.println("PPPPPP query PPPPPP>>>> getProblemList "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try { 
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
//               String branchId = rs.getString("complaint_id");
//                String PriorityId = rs.getString("priority");
//                String SLAId = rs.getString("SLA_Id");
//                String branchCode = rs.getString("request_Subject");
//                comp = new com.magbel.admin.objects.Sla2(branchId, PriorityId,SLAId,branchCode);
                String ReqnID = rs.getString("complaint_id");
                String SLAId = rs.getString("sla_id");
            	String Priority = rs.getString("Priority");
                String requestSubject = rs.getString("request_Subject");
                String requestDescription = rs.getString("request_Description");
                String Technician = rs.getString("Technician");
                String status = rs.getString("status");
                comp = new com.magbel.admin.objects.Sla2 (ReqnID,Priority,SLAId,requestSubject,requestDescription,status,Technician);

                _list.add(comp);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting HD_PROBLEM ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }
   */
    
    public com.magbel.admin.objects.RuleConstraints getSlaEscalateList(String DeptCode, String CatCode) 
    {
    //    System.out.println("=====getSlaEscalateList DeptCode =====  "+DeptCode+" ====CatCode==== "+CatCode);
        com.magbel.admin.objects.RuleConstraints cons= null;
        String query = "SELECT *  FROM SLA_ESCALATE where Dept_Code = '"+DeptCode+"' and Cat_Code = '"+CatCode+"'";
   //     System.out.println("=====getSlaEscalateList query =====  "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 
            	String resolveName=rs.getString("NAME");
            	String resolveDay=rs.getString("RESOLVE_DAY"); 
            	String responseConstraiant=rs.getString("CONSTRAINTS2");
            	String resolveHour=rs.getString("RESOLVE_HOUR");
            	String resolveMinute=rs.getString("RESOLVE_MINUTE");
                cons = new com.magbel.admin.objects.RuleConstraints();
                cons.setNAME(resolveName);
                cons.setRESOLVE_DAY(resolveDay);
                cons.setRESOLVE_HOUR(resolveHour);
                cons.setRESOLVE_MINUTE(resolveMinute);
                cons.setCONSTRAINT2(responseConstraiant);

                
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_ESCALATE ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return cons;

    }
    
    public com.magbel.admin.objects.ComplaintResponse getComplaintMailInfo(String id) 
    {
    //    System.out.println("=====getComplaintMailInfo id =====  "+id);
        com.magbel.admin.objects.ComplaintResponse cons= null;
        String query = "SELECT *  FROM HD_COMPLAINT where complaint_id ='"+id+"'";
    //    System.out.println("=====query in getComplaintMailInfo =====  "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	String category=rs.getString("complain_category");
            	String complaint_Type=rs.getString("complaint_Type"); 
            	String technician=rs.getString("technician");
            	int user_id=rs.getInt("user_id");          	
                cons = new com.magbel.admin.objects.ComplaintResponse();
                cons.setCategory(category);
                cons.setComplainType(complaint_Type);
                cons.setTechnician(technician);
                cons.setUserId(user_id);
                
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error in getComplaintMailInfo Selecting HD_COMPLAINT ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return cons;

    }
    
    
    public com.magbel.admin.objects.Sla2 getSlaId(String deptcode, String catcode) 
    {
        com.magbel.admin.objects.Sla2 cons= null;
        String query = "SELECT *  FROM am_gb_sla where Dept_Code ='"+deptcode+"' AND Cat_Code ='"+catcode+"'";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 
            	String responseSlaid=rs.getString("sla_id");
        
                cons = new com.magbel.admin.objects.Sla2();
                cons.setSLAId(responseSlaid);


                
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_ESCALATE ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return cons;

    }
    
    
    
    public com.magbel.admin.objects.RuleConstraints getSlaResponseList(String DeptCode, String CatCode) 
    {
        com.magbel.admin.objects.RuleConstraints cons= null;
        String query = "SELECT *  FROM SLA_ESCALATE where Dept_Code ='"+DeptCode+"' and Cat_Code ='"+CatCode+"'";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 
            	String resolutionName=rs.getString("NAME2");
            	String resolutionDay=rs.getString("RESOLVE_DAY"); 
            	String resolutionConstraiant=rs.getString("CONSTRAINTS");
        
                cons = new com.magbel.admin.objects.RuleConstraints();
                cons.setNAME(resolutionName);
                cons.setRESOLVE_DAY(resolutionDay);
                cons.setCONSTRAINT2(resolutionConstraiant);

                
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_ESCALATE ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return cons;

    }
    
    public com.magbel.admin.objects.User getescalateMail(String id) 
    {    	System.out.println("!!!!!!! id !!!!! "+id);
        com.magbel.admin.objects.User user= null;
        String query = "SELECT *  FROM am_gb_User where user_id ='"+id+"'";
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
    //	System.out.println("!!!!!!! query !!!!! "+query);
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 
            	String emailAddress=rs.getString("email");            
//            	String resolutionDay=rs.getString("RESOLVE_DAY"); 
//            	String resolutionConstraiant=rs.getString("CONSTRAINTS");
        
                user = new com.magbel.admin.objects.User();
                user.setEmail(emailAddress);           
            	System.out.println("!!!!!!! user !!!!! "+user);
            }
  
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting am_gb_User ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return user;

    }
    
    
    public com.magbel.admin.objects.RuleConstraints getSlaResolveResponseTime(String DeptCode, String CatCode) 
    {
      
        com.magbel.admin.objects.RuleConstraints cons= null;
        String query = "SELECT *  FROM SLA_RESPONSE where Dept_Code ='"+DeptCode+"' and Cat_Code ='"+CatCode+"'";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 
            	String reponseDay=rs.getString("RESPONSE_DAY");
            	String resolutionDay=rs.getString("RESOLVE_DAY");  
        
                cons = new com.magbel.admin.objects.RuleConstraints(); 
                cons.setRESPONSE_DAY(reponseDay);
                cons.setRESOLVE_DAY(resolutionDay);
             

                
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_RESOLVE ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return cons;

    }
    
    public boolean updateSla(String value,String id) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "UPDATE am_gb_sla SET process = '"+value+"' WHERE sla_ID ='"+id+"' ";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
 
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
    public boolean updateSlaMail(String value,String id) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "UPDATE am_gb_sla SET mailed = '"+value+"' WHERE sla_ID ='"+id+"' ";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
 
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
    
    public void updateHdComplaint (String id,int response,int resolve) 
    { //System.out.println("=====ID===== "+id+"==response==== "+response+"====resolve== "+resolve);
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE HD_COMPLAINT SET due_date = ? , Resp_Date =? where complaint_id=? ";
        try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            ps.setDate(1, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_COMPLAINT where complaint_id='"+id+"'"))),resolve)));
            ps.setDate(2, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_COMPLAINT where complaint_id='"+id+"'"))),response)));
            ps.setString(3, id);
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
	  }
 //   }
          
    public void updateMailTime () 
    { 
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE HD_COMPLAINT SET MAIL_STATUS = 'N' where status ='001' ";
           try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query updateMailTime ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
	  }

    public void updateMailStatus(String IssueId) 
    { 
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = false; System.out.println("=====IssueId=== "+IssueId);
        String query = "UPDATE HD_COMPLAINT SET MAIL_STATUS = 'Y' where complaint_id ='"+IssueId+"' ";
           try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query updateMailTime ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
	  }

    
    public void updateHdProblem (String id,int response,int resolve,String priority_code) 
    {
//    	System.out.println("------Inside updateHdProblem------");
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String databaseDate=util.findObject("select create_date from HD_PROBLEM where complaint_id='"+id+"'");
//        System.out.println("------Inside updateHdProblem id------"+id);
//        System.out.println("------Inside updateHdProblem priority_code------"+priority_code);
//        System.out.println("------Inside updateHdProblem response------"+response);
//        System.out.println("------Inside updateHdProblem resolve------"+resolve);
        String query = "UPDATE HD_PROBLEM SET due_date = ? , Resp_Date =? where complaint_id=? and priority =? ";
//        System.out.println("------Inside updateHdProblem query------"+query);
        try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            ps.setDate(1, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_PROBLEM where complaint_id='"+id+"'"))),resolve)));
            ps.setDate(2, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_PROBLEM where complaint_id='"+id+"'"))),response)));
            ps.setString(3, id);
            ps.setString(4, priority_code);
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) { 
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    
    public void updateHdSolution (String id,int response,int resolve) 
    {
        
        Connection con = null;
        PreparedStatement ps = null; 
        boolean done = false;
     //   System.out.println("------------->> "+util.findObject("select create_date from HD_SOLUTION where Solution_id='"+id+"'"));
        String query = "UPDATE HD_SOLUTION SET due_date = ? , Resp_Date =? where solution_id=? ";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_SOLUTION where Solution_id='"+id+"'"))),resolve)));
            ps.setDate(2, df.dateConvert(df.addDayToDate(df.dateConvert(df.reArrangeDate(util.findObject("select create_date from HD_SOLUTION where Solution_id='"+id+"'"))),response)));
 
            ps.setString(3, id);
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public boolean getUpdate(String id,String table,String column, String column2) 
    {

    	boolean result=false;
        String query = "SELECT *  FROM "+table+" where "+column+" ='"+id+"' ";
//        System.out.println("====table==== "+table);
//        System.out.println("====column==== "+column);
//        System.out.println("====column2==== "+column2);
//        System.out.println("====id==== "+id);
  //      System.out.println("====query In getUpdate ==== "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 result=true;
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting  ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return result;

    }
    public String getMail(String id,String table,String column) 
    {

    	boolean result=false;
        String query = "SELECT due_date,resp_date,create_date  FROM "+table+" where "+column+" ='"+id+"' ";
     //   System.out.println("====query==== "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        String dueDate="";
    	String respDate="";
    	String createDate = "";
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	  dueDate=rs.getString("due_date");
            	  respDate=rs.getString("resp_date");
            	  createDate=rs.getString("create_date");
            } 

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting  ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return dueDate+","+respDate+","+createDate;

    }
    public boolean getMailCheck(String id,String table,String column) 
    {
    //    System.out.println("===== getMailCheck id ++++ "+id);
   //     System.out.println("===== getMailCheck table ++++ "+table);
   //     System.out.println("===== getMailCheck column ++++ "+column);
    	boolean result=false;
        String query = "SELECT due_date,resp_date  FROM "+table+" where "+column+" ='"+id+"' ";
   //     System.out.println("===== getMailCheck query ++++ "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        String dueDate="";
    	String respDate="";
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	result=true;
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting  ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return result;

    }
    private Connection getConnection() {
        Connection con = null;
        dc = new DataConnect("helpDesk");

        try {
            con = dc.getConnection();
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" + e.getMessage());
        }
        return con;
    }

    private void closeConnection(Connection con, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" + e.getMessage());
        }

    }

    private void closeConnection(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
        }

    }

    private void closeConnection(Connection con, Statement s, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
        }
    }

    /**
     *
     * @param con
     *            Connection
     * @param ps
     *            PreparedStatement
     * @param rs
     *            ResultSet
     */
    private void closeConnection(Connection con, PreparedStatement ps,
            ResultSet rs) {
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
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
        }
    }
     
    
    public java.util.List getMailIssuesList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.MailRecords comp= null;
        String query = "SELECT *  FROM MAILER where status is null ";
   //     System.out.println("TREATED query TREATED>>>> getMailIssuesList "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try { 
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String Id = rs.getString("id");
                String sender = rs.getString("sender");
                String Mail_title = rs.getString("title");
                String recv_dt = rs.getString("recv_dt");
                String Mail_msg = rs.getString("msg"); 
                String Status = rs.getString("Status"); 
                comp = new com.magbel.admin.objects.MailRecords(Id, sender,Mail_title,recv_dt,Mail_msg,Status);
                
                _list.add(comp);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting MAILER ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }      
    public java.util.List getIssuesList() 
    {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.NewIssueMailRecord comp= null;
        String query = "SELECT *  FROM HD_COMPLAINT_MAIL where New_Mail_Issue_Status = 'N' ";
   //     System.out.println("TREATED query TREATED>>>> getMailIssuesList "+query);
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null; 

        try { 
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String Id = rs.getString("ID");
                String category = rs.getString("complain_category");
                String subcategory = rs.getString("complain_sub_category");
                String assetId = rs.getString("asset_id");
                String priority = rs.getString("priority"); 
                String status = rs.getString("status"); 
                String create_date = rs.getString("create_date");
                String create_dateTime = rs.getString("create_dateTime");
                String userId = rs.getString("user_id");
                String complaintType= rs.getString("complaint_Type");
  
              	System.out.println("=====userId in getIssuesList====>>> "+userId);
                String incidentMode = rs.getString("incident_Mode");
                userId = (userId == null || userId.equals(""))?"0":userId;
                String MailIssue = rs.getString("New_Mail_Issue_Status");
               // String requesterName = request.getParameter("requesterName");
               // String requesterContactNum = request.getParameter("requesterContNumber");
                String requestSubject = rs.getString("request_Subject");
                String notifyEmail = rs.getString("notify_Email");
                String requesterId = rs.getString("requester_id");                
                String requestDescription = rs.getString("request_Description");        
//                String requestDescriptionNew = request.getParameter("requestDescriptionNew");
         //       String operation = rs.getString("operation");
      //          String returncode = rs.getString("returncode");
                String companyCode = rs.getString("Company_Code");
                String complaintId = rs.getString("complaint_id");
             //   String requestSubject = rs.getString("request_Subject");
                 //for update of hd complaint
                //subject,description,technician,asset,
                
                if (category == null || category.equals("")){
                	category = rs.getString("complain_category");
                }                
                if (subcategory == null || subcategory.equals("")){
                	subcategory = rs.getString("complain_sub_category");
                }          
                String technician = rs.getString("technician");
                technician = (technician == null || technician.equals(""))?"0":technician;
                //int tech = Integer.parseInt(technician);       
   //             String assetId = request.getParameter("asset"); 
 //               String pageType = request.getParameter("pageType");
 //               String pageName = request.getParameter("pageName");
 //               String FileName = "HD_COMPLAINT";
 //               String FieldName = "complaint_id";
                System.out.println("=====userId in technician====>>> "+technician);
                comp = new com.magbel.admin.objects.NewIssueMailRecord(Id, category,subcategory,assetId,priority,status,create_date,create_dateTime,userId,complaintType,incidentMode,MailIssue,requesterId,requestDescription,requestSubject,complaintId,companyCode,technician,notifyEmail);
                
                _list.add(comp);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting MAILER ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }      
    
    public boolean insertIssues(String id, String sender, String Mail_title, String recv_dt,
            String Mail_msg) {
        boolean done = true;
        //flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String priority = "001"; 
        String SlaQuery = "SELECT  sla_ID   FROM   am_gb_sla where SLA_Code = "+priority+"";
        String SlaIdent = aprecords.getCodeName(SlaQuery);
        int Slaid = Integer.parseInt(SlaIdent);
        Mail_msg = Mail_msg.replaceAll("<DIV>", "");
        Mail_msg = Mail_msg.replaceAll("</DIV>", "");
        Mail_msg = Mail_msg.replaceAll("<BR>", "");
        Mail_msg = Mail_msg.replaceAll("&nbsp;"," ");
        String[] Mail_Body = Mail_msg.split("BODY");
        
     //   System.out.println("=====Mail_Body 0 thread=== "+Mail_Body[0]);
     //   System.out.println("=====Mail_Body 0 1=== "+Mail_Body[1]);
          
        int lent = Mail_Body[1].length();
   //     System.out.println("=====Mail_Body 1=== "+Mail_Body[1]);
   //     System.out.println("=====Mail_Body Substring=== "+Mail_Body[1].substring(1,lent-2));
        
        String[] Mail_Sender = sender.split("<");
        int lentsender = Mail_Sender[0].length();
    //    System.out.println("=====Mail_Description 0=== "+Mail_Sender[0].substring(0,lentsender-1));
    //    System.out.println("=====Mail_Description 1=== "+Mail_Sender[1]);
        String complaintId = "Incd/" + applHelper.getGeneratedId("HD_COMPLAINT");
        String query = "INSERT INTO HD_COMPLAINT(complaint_id,requester_Name,request_Subject,create_date,request_Description,"
                + "Status,incident_Mode,sla_id,complaint_Type,priority)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)";
        try { 
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, complaintId);
            ps.setString(2, Mail_Sender[0].substring(0,lentsender-1));
            ps.setString(3, Mail_title);
            ps.setString(4, recv_dt);
            ps.setString(5, Mail_Body[1].substring(1,lent-2));
            ps.setString(6, "001");
            ps.setString(7, "001");                     
            ps.setInt(8, Slaid);
          //  ps.setString(9, sender);   
            ps.setString(9, "001");  
            ps.setString(10, "001"); 
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert HD_COMPLAINT->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    
    public void updateMAILER (String id) 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE MAILER SET Status = ? where Id=? ";
        try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            ps.setString(1, "TREATED");
            ps.setString(2, id);        
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing updateMAILER Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
	  }
    public void DeleteMailSent_Record (String id) 
    {
        Statement stmt;
        ResultSet rs;
    	Connection con = null;
        PreparedStatement ps = null;
        String query = "Delete from HD_COMPLAINT_MAIL where complaint_id = '"+id+"' ";
  //      System.out.println("Isuue Deleted is "+query);
  //      System.out.println("Isuue Deleted is "+id);
        try {
            con = getConnection();
            ps = con.prepareStatement(query); 
            ps.execute();     
   //         System.out.println("Isuue Deleted is "+id);
        } catch (Exception e) {
            System.out.println("WARNING:Error executing DeleteMailSent_Record Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
	  }
 //   }
             

    
   


}
