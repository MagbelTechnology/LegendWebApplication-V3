
package com.magbel.ia.servlet;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

//import com.magbel.ia.bus.InventoryItemServiceBus;
import com.magbel.ia.bus.InventoryServiceBus;

public class InventoryItemActionsServlet extends HttpServlet {

  /** Initializes the servlet.
   */
   private InventoryServiceBus serviceBus;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    serviceBus = new InventoryServiceBus();
  }

  /** Destroys the servlet.
   */
  public void destroy() {

  }

  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

	response.setContentType("text/html");
    System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> A" );
    
    HttpSession session = request.getSession();
    PrintWriter out = response.getWriter();
   // user = (User)session.getAttribute("CurrentUser");
 //  	User loginId = (com.magbel.ia.vao.User) session.getAttribute("CurrentUser");
   //	if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
//	String compCode = user.getCompanyCode();
  
    String id = request.getParameter("ID");
    String itemNo = request.getParameter("itemCode");
    String status = request.getParameter("status");
    String type = request.getParameter("itemTypeCode");
    String description = request.getParameter("desc");
    String taxCode = request.getParameter("taxCode");
 //   String userid = request.getParameter("userid");
    System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> b" ); 
	String strMinimumQuantity = request.getParameter("minQuantity");
    System.out.println("<<<<<<<<<<<<<<< Got Here strMinimumQuantity  >>>>>>>>>>>>>> b" +strMinimumQuantity);
     strMinimumQuantity = (strMinimumQuantity == null || strMinimumQuantity.equals(""))?"0":strMinimumQuantity;	        
	 int minimumQuantity = (strMinimumQuantity != null|| strMinimumQuantity != "") ? Integer.parseInt(strMinimumQuantity) : 0;
      String strReorderLevel = request.getParameter("reorderLevel"); 
      System.out.println("<<<<<<<<<<<<<<< Got Here strReorderLevel  >>>>>>>>>>>>>> b: " +strReorderLevel);      
      strReorderLevel = (strReorderLevel == null || strReorderLevel.equals(""))?"0":strReorderLevel;
      int  reorderLevel = (strReorderLevel != null) ? Integer.parseInt(strReorderLevel) : 0;
	String strWeightAverageCost = request.getParameter("weightAvgCost");
	double weightAverageCost = 0.00;	
    System.out.println("<<<<<<<<<<<<<<< Got Here strWeightAverageCost  >>>>>>>>>>>>>> b: " +strWeightAverageCost);
    strWeightAverageCost = (strWeightAverageCost == null || strWeightAverageCost.equals(""))?"0":strWeightAverageCost;
    if((strWeightAverageCost != null)||(strWeightAverageCost != "")){ 	
	weightAverageCost = (strWeightAverageCost != null) ? Double.parseDouble(strWeightAverageCost.trim().replace(",","")) : 0.00d;
    }
    System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> c" );
	String  strStandardCost = request.getParameter("standardCost");
	strStandardCost = (strStandardCost == null || strStandardCost.equals(""))?"0":strStandardCost;	
	double  standardCost = (strStandardCost != null) ? Double.parseDouble(strStandardCost) : 0.00d;	
	
	String vFIFO = request.getParameter("vFIFO");
	vFIFO = (vFIFO == null || vFIFO.equals(""))?"0":vFIFO;		
	double FIFO =(vFIFO != null) ? Double.parseDouble(vFIFO.trim().replace(",","")) : 0.00d;
    System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> d" );
	String  strWeight = request.getParameter("weight");
	strWeight = (strWeight == null || strWeight.equals(""))?"0":strWeight;		
	double  weight = (strWeight != null) ? Double.parseDouble(strWeight.trim().replace(",","")) : 0.00d;
	
	  
		String  backOrderable = request.getParameter("backOrder");
        String  strReqApproval = request.getParameter("reqApproval");
        String strOrganPosition = request.getParameter("organPosition");
        String strConcurrence = request.getParameter("concurrence");
        
        int organPosition = 0;
        int concurrence = 0;
        System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> e" );
        
        String reqApproval = ((strReqApproval==null)||strReqApproval.equals("")) ? "N" : strReqApproval;
        
        organPosition = ((strOrganPosition==null)||strOrganPosition.equals("")) ? 0 : Integer.parseInt(strOrganPosition);
        concurrence = ((strConcurrence==null)||strConcurrence.equals("")) ? 0 : Integer.parseInt(strConcurrence);
       
               
        String  strMaxApproveLevel = request.getParameter("maxApproveLevel");
        strMaxApproveLevel = (strMaxApproveLevel == null || strMaxApproveLevel.equals(""))?"0":strMaxApproveLevel;        
        int  maxApproveLevel = ((strMaxApproveLevel != null)&&(!strMaxApproveLevel.equals(""))) ? Integer.parseInt(strMaxApproveLevel) : 0;
        String strMinAmtLimit = request.getParameter("minAmtLimit");
        strMinAmtLimit = (strMinAmtLimit == null || strMinAmtLimit.equals(""))?"0":strMinAmtLimit;        
        double  minAmtLimit = ((strMinAmtLimit != null)&&(!strMinAmtLimit.equals(""))) ? Double.parseDouble(strMinAmtLimit.replace(",", "")) : 0;
        String strMaxAmtLimit = request.getParameter("maxAmtLimit");
        strMaxAmtLimit = (strMaxAmtLimit == null || strMaxAmtLimit.equals(""))?"0":strMaxAmtLimit;        
        double  maxAmtLimit = ((strMaxAmtLimit != null)&&(!strMaxAmtLimit.equals(""))) ? Double.parseDouble(strMaxAmtLimit.replace(",", "")) : 0;
        
        System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> f" );
        
      String  strAdjustMaxApproveLevel = request.getParameter("adjustMaxApproveLevel");
      strAdjustMaxApproveLevel = (strAdjustMaxApproveLevel == null || strAdjustMaxApproveLevel.equals(""))?"0":strAdjustMaxApproveLevel;	      
      int  adjustMaxApproveLevel = ((strAdjustMaxApproveLevel != null)&&(!strAdjustMaxApproveLevel.equals(""))) ? Integer.parseInt(strAdjustMaxApproveLevel) : 0;
      String strAdjustMinAmtLimit = request.getParameter("adjustMinAmtLimit");
      strAdjustMinAmtLimit = (strAdjustMinAmtLimit == null || strAdjustMinAmtLimit.equals(""))?"0":strAdjustMinAmtLimit;	      
      double  adjustMinAmtLimit = ((strAdjustMinAmtLimit != null)&&(!strAdjustMinAmtLimit.equals(""))) ? Double.parseDouble(strAdjustMinAmtLimit.replace(",", "")) : 0;
      String strAdjustMaxAmtLimit = request.getParameter("adjustMaxAmtLimit");
      strAdjustMaxAmtLimit = (strAdjustMaxAmtLimit == null || strAdjustMaxAmtLimit.equals(""))?"0":strAdjustMaxAmtLimit;	      
      double  adjustMaxAmtLimit = ((strAdjustMaxAmtLimit != null)&&(!strAdjustMaxAmtLimit.equals(""))) ? Double.parseDouble(strAdjustMaxAmtLimit.replace(",", "")) : 0;
            
	  String companyCode = request.getParameter("companyCode");
      System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> g"+"    itemNo: "+itemNo );
      
        String salesAcct = request.getParameter("salesAcct");
        String COGSoldAcct = request.getParameter("COGSoldAcct");
        String inventoryAcct = request.getParameter("inventoryAcct");
        String adjustAcct = request.getParameter("adjustAcct");
        String adjustOpt = request.getParameter("adjustOpt");
        
        System.out.println("<<<<<<<<minAmtLimit<<<< "+minAmtLimit+"  maxAmtLimit: "+maxAmtLimit+"   adjustMinAmtLimit: "+adjustMinAmtLimit+"  adjustMaxAmtLimit: "+adjustMaxAmtLimit+"   standardCost: "+standardCost);
        
       String InventoryItemAddBtn = request.getParameter("InventoryItemAddBtn");
       String InventoryItemUpdBtn = request.getParameter("InventoryItemUpdBtn");
       String ItemAppLevelAddBtn = request.getParameter("ItemAppLevelAddBtn");
       String ItemAppLevelUpdBtn = request.getParameter("ItemAppLevelUpdBtn");
       String receivableNo = request.getParameter("receivable");
       String categoryCode = request.getParameter("categoryCode");
       String unitcode = request.getParameter("unitcode");
       String unitname = request.getParameter("unitname");
        
        String isAutoIdGen = "";
  //      com.magbel.ia.vao.User user = null;
      
        System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> 1" ); 
//        com.magbel.ia.vao.User user = null;
      try
      {
   /*       if(session.getAttribute("CurrentUser")!=null){user =(com.magbel.ia.vao.User)session.getAttribute("CurrentUser");}
           int userId = Integer.parseInt(user.getUserId()); 
           */
    	//   legend.admin.objects.User  user = (legend.admin.objects.User)session.getAttribute("_user");
    	//   	 int userId = Integer.parseInt(user.getUserId());
    	  String userid =(String)session.getAttribute("CurrentUser");
    	  System.out.println("<<<<<<<<<<<<<<< NEXT Here  >>>>>>>>>>>>>> 1 "+"    userid: "+userid ); 
    	  int userId = Integer.parseInt(userid); 
          if(InventoryItemAddBtn != null){
        	  System.out.println("<<<<<<<<<<<<<<< NEXT Here  >>>>>>>>>>>>>> 2"+"    userId: "+userId ); 
          isAutoIdGen = serviceBus.getCodeName("SELECT auto_generate_ID FROM AM_GB_COMPANY");
          if(isAutoIdGen.trim().equalsIgnoreCase("N")){ //test 4 auto ID setup
        	  System.out.println("<<<<<<<<<<<<<<< NEXT Here  >>>>>>>>>>>>>> 3"+"      isAutoIdGen: "+isAutoIdGen ); 
           if(itemNo.equals("")||itemNo == null){
                out.print("<script>alert('Item Code. cannot be empty')</script>");
                out.print("<script>history.go(-1);</script>");
            }
           else if(serviceBus.isRecordExisting("SELECT COUNT(ITEM_CODE) FROM IA_INVENTORY_ITEMS WHERE ITEM_CODE='"+itemNo+"'")){ 
               out.print("<script>alert('Item Code Already Exist')</script>");
               out.print("<script>history.go(-1);</script>");
           }  
           else{
               if(serviceBus.createInventoryItem(itemNo,status,type,description,taxCode,minimumQuantity,
                                               weightAverageCost,standardCost,FIFO,weight,backOrderable,userId,
                                               reorderLevel,salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,
                                               minAmtLimit,maxAmtLimit,
                                               adjustAcct,adjustMaxApproveLevel,adjustMinAmtLimit,adjustMaxAmtLimit,companyCode,receivableNo,categoryCode,unitcode)){
                   out.print("<script>alert('Record successfully Submitted.')</script>");                                
                   out.print("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode="+itemNo+"'</script>");                               
                }
              }
           }
           else{//isAutogen = Y
              if(serviceBus.createInventoryItem(itemNo,status,type,description,taxCode,minimumQuantity,
                                              weightAverageCost,standardCost,FIFO,weight,backOrderable,userId,reorderLevel,
                                              salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,minAmtLimit,maxAmtLimit,
                                              adjustAcct,adjustMaxApproveLevel,adjustMinAmtLimit,adjustMaxAmtLimit,companyCode,receivableNo,categoryCode,unitcode)) {
                 out.print("<script>alert('Record successfully Submitted.')</script>");                                
                 out.print("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode="+serviceBus.auotoGenCode+"'</script>");                               
                }       
            
            }
               
          } 
          System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> 2" );  
          if(InventoryItemUpdBtn!=null){
                if(serviceBus.updateInventoryItem(itemNo,status,type,description,taxCode,minimumQuantity,
                                               weightAverageCost,standardCost,FIFO,weight,backOrderable,reorderLevel,
                                               salesAcct,COGSoldAcct,inventoryAcct,reqApproval,maxApproveLevel,minAmtLimit,maxAmtLimit,
                                               adjustAcct,adjustMaxApproveLevel,adjustMinAmtLimit,adjustMaxAmtLimit,receivableNo,categoryCode)){
                    out.print("<script>alert('Record successfully Submitted.')</script>");                                
                    out.print("<script>window.location='DocumentHelp.jsp?np=inventoryItemDetail&itemCode="+itemNo+"'</script>");                               
                
                }
             
            }
          System.out.println("ItemAppLevelAddBtn >>>>>>>>>>>>>> " + ItemAppLevelAddBtn);
          if(ItemAppLevelAddBtn!=null)
          {
        	  System.out.println("serviceBus >>>>>>>>>>>>>> " + serviceBus);
              if(serviceBus.isRecordExisting("SELECT COUNT(ORGAN_POSITION) FROM IA_ITEM_APPROVAL_DETAIL " +
              		"WHERE ORGAN_POSITION="+organPosition))
              { 
                 out.print("<script>alert('Approval Level Already Exist')</script>");
                 out.print("<script>history.go(-1);</script>");
              }
              else
              {
	              if(serviceBus.createItemApprovalDetail (itemNo,organPosition,concurrence,userId,minAmtLimit,
	            		  maxAmtLimit,companyCode))
	              {
	                  out.print("<script>alert('Record successfully Submitted.')</script>"); 
	                  out.print("<script>window.location='DocumentHelp.jsp?np=itemApprovingLevel&itemCode="+itemNo+"'</script>");     
	              }
              }
          }
          if(ItemAppLevelUpdBtn!=null){
              /*if(serviceBus.isRecordExisting("SELECT COUNT(ORGAN_POSITION) FROM IA_ITEM_APPROVAL_DETAIL WHERE ORGAN_POSITION="+organPosition)){ 
                             out.print("<script>alert('Approval Level Already Exist')</script>");
                             out.print("<script>history.go(-1);</script>");
                         }  */                    
                //else{
                if(serviceBus.updateItemApprovalDetail(itemNo,organPosition,concurrence,id,minAmtLimit,maxAmtLimit)){
                    out.print("<script>alert('Record successfully Submitted.')</script>"); 
                    out.print("<script>window.location='DocumentHelp.jsp?np=itemApprovingLevelDetail&itemCode="+itemNo+"&ID="+id+"'</script>");
                      
                }
            //}
          }
      
      }
      catch(NullPointerException e)
      {
    	  System.out.println("<<<<<<<<<<<<<<< Got Here  >>>>>>>>>>>>>> 4" );  
//    	  response.sendRedirect("sessionTimedOut.jsp");
        }
      catch(Exception e){
          e.printStackTrace();
      }
  
  
  
  }

  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "InventoryItem Action Servlet";
  }

}
