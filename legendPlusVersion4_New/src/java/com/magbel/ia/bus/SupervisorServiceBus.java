package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.util.ApplicationHelper2;
import com.magbel.ia.util.CodeGenerator;
import com.magbel.ia.vao.InventoryItem;
import com.magbel.ia.vao.ItemApprovalDetail;
import com.magbel.ia.vao.SalesOrderDeliveryItem;
import com.magbel.ia.vao.TransactionApproval;
import com.magbel.ia.vao.User;
import com.magbel.util.HtmlUtilily;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SupervisorServiceBus extends PersistenceServiceDAO{
    SimpleDateFormat sdf;

    final String space = "  ";
    final String comma = ",";
    public String auotoGenCode = "";

    java.util.Date date;

    com.magbel.util.DatetimeFormat df;
    ApplicationHelper helper;
    ApplicationHelper2 applhelper;
    CodeGenerator cg;
    HtmlUtilily htmlUtil;
    InventoryServiceBus inventItem;
    
    public SupervisorServiceBus() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        helper = new ApplicationHelper();
        applhelper = new ApplicationHelper2();
        cg = new CodeGenerator();
        htmlUtil = new HtmlUtilily();
        inventItem = new InventoryServiceBus();
        
    }
    
    public boolean createTransactionApproval(String transCode,String transType, String desc,int userId,
                                             int approveOfficer,String status,String itemCode,int quantity) {

                    String query = "INSERT INTO ST_TRANSACTION_APPROVAL (MTID,TRANS_CODE,TRANS_TYPE,DESCRIPTION,USERID,APPROVE_OFFICER,TRANS_DATE,STATUS,MAX_APPROVE_LEVEL,CONCURRENCE,ITEM_CODE,QUANTITY)"+
                                                                        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                
                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;
                int maxApproveLevel = 0;
                int organPosition = 0;
                int concurrence = 0;
                    if(quantity > 0){
                        maxApproveLevel = Integer.parseInt(getCodeName("SELECT MAX_APPROVE_LEVEL FROM ST_INVENTORY_ITEMS WHERE " +
                        		"ITEM_CODE='"+itemCode+"'"));
                        organPosition = Integer.parseInt(getCodeName("SELECT APPROVE_LEVEL FROM AM_GB_User WHERE USER_ID='"+userId+"'"));
                        String strConcurrence = getCodeName("SELECT CONCURRENCE FROM ST_ITEM_APPROVAL_DETAIL WHERE " +
                        		"ITEM_CODE='"+itemCode+"' AND APPROVE_LEVEL='"+organPosition+"'"); 
                        concurrence = ((strConcurrence==null)||strConcurrence.equals("")) ? 0 :Integer.parseInt(strConcurrence);
                    }
                    
                    String id = helper.getGeneratedId("ST_TRANSACTION_APPROVAL");
                                       
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);
                            ps.setString(1,id);
                            ps.setString(2,transCode);
                            ps.setString(3,transType);
                            ps.setString(4,desc);
                            ps.setInt(5,userId);
                            ps.setInt(6,approveOfficer);
                            ps.setDate(7,dateConvert(new java.util.Date()));
                            ps.setString(8,status);
                            ps.setInt(9,maxApproveLevel);
                            ps.setInt(10,concurrence);
                            ps.setString(11,itemCode);
                            ps.setInt(12,quantity);

                            done = (ps.executeUpdate() != -1);
                    } catch (Exception ex) {
                            done = false;
                            System.out.println("ERROR Creating Transaction Approval " + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps);
                    }
                    return done;
            }
      
    public boolean updateTransactionApproval(String ID,String transType,int approveOfficer,String status,String reason) {
                ArrayList list = new ArrayList();
                TransactionApproval transApp = null; 
                transApp = (TransactionApproval)findTransactionApprovalByQuery(" AND MTID='"+ID+"'").get(0);
                
                 int maxApproveLevel = transApp.getMaxApproveLevel();
                 int concurrence = transApp.getConcurrence();
                 int maxAppIncrement = 0;
                 int concurIncrement = 0;
                 
                 String query = "";
                               
                    if(concurrence > 0){
                        query = "UPDATE ST_TRANSACTION_APPROVAL SET APPROVE_OFFICER=?,STATUS=?,TRANS_DATE=?,REASON=?,MAX_APPROVE_LEVEL=MAX_APPROVE_LEVEL-?,CONCURRENCE=CONCURRENCE-? WHERE MTID = ? AND TRANS_TYPE = ?";
                        maxAppIncrement = 0;
                        concurIncrement = 1;
                    }
                    else{
                        query = "UPDATE ST_TRANSACTION_APPROVAL SET APPROVE_OFFICER=?,STATUS=?,TRANS_DATE=?,REASON=?,MAX_APPROVE_LEVEL=MAX_APPROVE_LEVEL-?,CONCURRENCE=CONCURRENCE-? WHERE MTID = ? AND TRANS_TYPE = ?";
                        concurIncrement = 0;
                        maxAppIncrement = 1;
                    }
                
                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;
                    //String id = helper.getGeneratedId("ST_TRANSACTION_APPROVAL");
                                       
                    try {
                            con = getConnection();
                            ps = con.prepareStatement(query);
                            ps.setInt(1,approveOfficer);
                            ps.setString(2,status);
                            ps.setDate(3,dateConvert(new java.util.Date()));
                            ps.setString(4,reason);
                            ps.setInt(5,maxAppIncrement);
                            ps.setInt(6,concurIncrement);
                            ps.setString(7,ID);
                            ps.setString(8,transType);
                                                      
                            done = (ps.executeUpdate() != -1);
                    } catch (Exception ex) {
                            done = false;
                            System.out.println("ERROR Updating Transaction Approval " + ex.getMessage());
                            ex.printStackTrace();
                    } finally {
                            closeConnection(con, ps);
                    }
                    return done;
            }        
    public ArrayList findTransactionApprovalByQuery(String filter){

            ArrayList records = new ArrayList();
            String SELECT_QUERY = "SELECT MTID,TRANS_CODE,TRANS_TYPE,DESCRIPTION,USERID,APPROVE_OFFICER," +
            		"TRANS_DATE,STATUS,REASON,ITEM_CODE,MAX_APPROVE_LEVEL,CONCURRENCE,QUANTITY " + 
                                  "FROM ST_TRANSACTION_APPROVAL WHERE MTID IS NOT NULL "+filter;
                                  
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            System.out.println("findTransactionApprovalByQuery SELECT_QUERY: "+SELECT_QUERY);
            try{
                    con = getConnection();
                    ps = con.prepareStatement(SELECT_QUERY);
                    rs = ps.executeQuery();
                    while(rs.next()){
                       String mtId = rs.getString("MTID");
                       String transCode = rs.getString("TRANS_CODE");
                       String transType = rs.getString("TRANS_TYPE");
                       String desc = rs.getString("DESCRIPTION");
                       int userId = rs.getInt("USERID");
                       int approveOfficer = rs.getInt("APPROVE_OFFICER");
                       String transDate = formatDate(rs.getDate("TRANS_DATE"));
                       String status = rs.getString("STATUS");
                       String reason = rs.getString("REASON")==null ? "" : rs.getString("REASON");
                       String itemCode = rs.getString("ITEM_CODE");
                       int maxApproveLevel = rs.getInt("MAX_APPROVE_LEVEL");
                       int concurrence = rs.getInt("CONCURRENCE");
                       int quantity = rs.getInt("QUANTITY");
                      
                       TransactionApproval transApp = new TransactionApproval(mtId,transCode,transType,desc,userId,approveOfficer,
                                                     transDate,status,reason,itemCode,maxApproveLevel,concurrence,quantity);
                       records.add(transApp);
                    }

            }catch(Exception er){
                    System.out.println("Error finding All findTransactionApprovalByQuery...->"+er.getMessage());
                    //er.printStackTrace();
            }finally{
                    closeConnection(con,ps,rs);
            }

            return records ;
    }
    public String href(String transType_,String IDD)
    {
     String transType = transType_.trim();
     StringBuffer result = new StringBuffer(100);
     if(transType.equals("ADJUST"))
     {
      result.append("<a href=\"DocumentHelp.jsp?np=inventoryAdjustmentApp&ID="+IDD+"\">");
      }
     else if(transType.equals("SALES"))
     {
       result.append("<a href=\"DocumentHelp.jsp?np=salesOrderApp&ID="+IDD+"\">");
     }
     else if(transType.equals("PURCHASE"))
     {
       result.append("<a href=\"DocumentHelp.jsp?np=purchaseOrderApp&ID="+IDD+"\">");
     }
     else if(transType.equals("TRANSFER"))
     {
      result.append("<a href=\"DocumentHelp.jsp?np=wareHouseTransferApp&ID="+IDD+"\">");
     }
	 else if(transType.equals("IMPREST"))
	 {
	    result.append("<a href=\"DocumentHelp.jsp?np=newImprestApp&ID="+IDD+"\">");
	 }  
	 else if(transType.equals("REQN"))
	 {
		 IDD = applhelper.descCode("select trans_code from ia_transaction_approval where mtid='"+IDD+"' and trans_type='REQN'");
	    result.append("<a href=\"DocumentHelp.jsp?np=RequisitionApprovalUpdate&ID="+IDD+"\">");
	 } 
     else
     {
       result.append("");
     }
      return result.toString();      
    }
    
   public ArrayList getNextApprovingOfficer(int userId,String ID,String transType,String itemCode,double amount){
    
    String result = "";
    int concurrence = 0;
    int remainConcur = 0;
    int nextOrganPosition = 0;
    int userOrganPosition = 0;
    int maxApprovalLevel = 0;
    
    String strRemainConcur = this.getCodeName("SELECT CONCURRENCE FROM ST_TRANSACTION_APPROVAL WHERE MTID='"+ID+"' AND TRANS_TYPE='"+transType+"'");
    String strUserOrganPosition = this.getCodeName("SELECT APPROVE_LEVEL FROM AM_GB_User WHERE USER_ID='"+userId+"'");
    remainConcur = ((strRemainConcur!=null) && !strRemainConcur.equals("")) ? Integer.parseInt(strRemainConcur) : 0;
    userOrganPosition = ((strUserOrganPosition!=null) && !strUserOrganPosition.equals("")) ? Integer.parseInt(strUserOrganPosition) : 0;
    
    String strConcurrence = this.getCodeName("SELECT CONCURRENCE FROM ST_TRANSACTION_APPROVAL WHERE MTID='"+ID+"' AND TRANS_TYPE='"+transType+"'");
    String strMaxApprovalLevel = getCodeName("SELECT MAX_APPROVE_LEVEL FROM ST_TRANSACTION_APPROVAL WHERE MTID='"+ID+"' AND TRANS_TYPE='"+transType+"'");
    maxApprovalLevel = Integer.parseInt(strMaxApprovalLevel);
    concurrence = Integer.parseInt(strConcurrence);
  
   /*check if there is no more available approving officer*/
    String availableUser = htmlUtil.getResources("","SELECT USER_ID,FULL_NAME FROM AM_GB_User WHERE IS_SUPERVISOR = 'Y' AND APPROVE_LEVEL = (SELECT "+nextOrganPosition+" FROM AM_GB_User WHERE USER_ID = '"+userId+"') AND USER_ID != "+userId+" ORDER BY 2 ");
    
    if(((maxApprovalLevel<=0) && (concurrence<=0)) || availableUser.equals("")){
       result=("SELECT '0' AS USER_ID,'Approval Exhausted!' AS FULL_NAME FROM AM_GB_User WHERE USER_ID='"+userId+"'");
        
    }
    else{
        if(remainConcur > 0){ //if(concurrence > 0){//test 4 concurrence
        /*return all users in the same position if concurrence is never exceeded*/
        result=("SELECT USER_ID,FULL_NAME FROM AM_GB_User WHERE IS_SUPERVISOR = 'Y' AND APPROVE_LEVEL = (SELECT APPROVE_LEVEL FROM AM_GB_User WHERE USER_ID = '"+userId+"') AND USER_ID != '"+userId+"' ORDER BY 2 ");
        // }
        }
        else{
        
         nextOrganPosition = amount <= 0 ? getNextOrganPosition(itemCode,userOrganPosition) : getNextOrganPosition(itemCode,userOrganPosition,amount);
         result=("SELECT USER_ID,FULL_NAME FROM AM_GB_User WHERE IS_SUPERVISOR = 'Y' AND APPROVE_LEVEL = "+nextOrganPosition+" AND USER_ID != '"+userId+"' ORDER BY 2");
        
        }   
     }
        
        String SELECT_QUERY = result;
        ArrayList records = new ArrayList(); 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        
        try{
           con = getConnection();
           ps = con.prepareStatement(SELECT_QUERY);
           rs = ps.executeQuery();
           while(rs.next()){
            String userId_ = (rs.getString("USER_ID"));
            String fullName_ = rs.getString("FULL_NAME");
            user = new User(userId_,"",fullName_,"","","","","","","","","","","","","","","","","","");
            records.add(user);
           }

        }catch(Exception er){
                System.out.println("Error finding getNextApprovingOfficer...->"+er.getMessage());
                er.printStackTrace();
        }finally{
                closeConnection(con,ps,rs);
        }
       return records;
    
    }
    
    public ArrayList getCurrentApprovingOfficer(int userId,String itemCode,double itemTotAmount){
     
     int nextOrganPosition = 0;
     int currOrganPosition = 0;
     double itemUnitPrice = 0;
     int itemQuantity = 0;
     double minAmtLimit = 0;
     double maxAmtLimit = 0;
     
     InventoryItem itemDetail = ((itemCode != null)&& (!itemCode.equals(""))) ? inventItem.findInventoryItemById(itemCode) : null;
     if(itemDetail != null)
     {
	     minAmtLimit = itemDetail.getMaxAmtLimit();
	     maxAmtLimit = itemDetail.getMaxAmtLimit();
     }
     if(itemTotAmount <= 0)
     {
       currOrganPosition = getCurrentApprovalLevel(itemCode);
     }
     else{
       currOrganPosition = getCurrentApprovalLevel(itemCode,itemTotAmount);
     }
     
     String SELECT_QUERY="SELECT USER_ID,FULL_NAME FROM AM_GB_User WHERE IS_SUPERVISOR = 'Y' AND " +
     		"APPROVE_LEVEL = "+currOrganPosition+" AND USER_ID != "+userId+" ORDER BY 2";
     
     System.out.println("SELECT_QUERY >>>>>> " + SELECT_QUERY);
       
     ArrayList records = new ArrayList(); 
         
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     User user = null;
         
     try{
      con = getConnection();
      ps = con.prepareStatement(SELECT_QUERY);
      rs = ps.executeQuery();
      while(rs.next()){
        String userId_ = (rs.getString("USER_ID"));
        String fullName_ = rs.getString("FULL_NAME");
        user = new User(userId_,"",fullName_,"","","","","","","","","","","","","","","","","","");
        records.add(user);
      }
      }
      catch(Exception er){
       System.out.println("Error finding getCurrentApprovingOfficer...->"+er.getMessage());
       er.printStackTrace();
      }finally{
        closeConnection(con,ps,rs);
       }
      return records ;
     
     }
    
    InventoryServiceBus invent = new InventoryServiceBus();
    
    private int getNextOrganPosition(String itemCode,int organPosition_){
        
    ArrayList list = new ArrayList();
    int nextOrganPosition = 0;
    
    list = invent.findAllItemApprovalDetail(itemCode);
    
    for(int i=0; i < list.size(); i++){
      nextOrganPosition = ((ItemApprovalDetail)list.get(i)).getOrganPosition();
      if(organPosition_ > nextOrganPosition){
          break;
      }
    }
        return nextOrganPosition;
    }
    private int getNextOrganPosition(String itemCode,int organPosition_,double amount){
    
    /*String query = "SELECT a.MtId,a.APPROVE_LEVEL,a.CONCURRENCE,a.USERID,a.TRANS_DATE,b.MIN_AMOUNT,b.MAX_AMOUNT " + 
                   "FROM ST_ITEM_APPROVAL_DETAIL a,ST_APPROVAL_LEVEL b WHERE a.APPROVE_LEVEL=b.LEVEL_CODE AND a.ITEM_CODE='"+itemCode+"'"; */
            
     String query = "SELECT MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT,DESCRIPTION,CONCURRENCE,TRANS_DATE,USERID FROM ST_APPROVAL_LEVEL ORDER BY MAX_AMOUNT ASC"; 
        
    /*String currUserMaxAmount = "SELECT a.MtId,a.APPROVE_LEVEL,a.CONCURRENCE,a.USERID,a.TRANS_DATE,b.MIN_AMOUNT,b.MAX_AMOUNT " + 
                               "FROM ST_ITEM_APPROVAL_DETAIL a,ST_APPROVAL_LEVEL b WHERE "+
                               "a.APPROVE_LEVEL=b.LEVEL_CODE AND a.ITEM_CODE='"+itemCode+"' AND a.APPROVE_LEVEL="+organPosition_; 
           */ 
     String currUserMaxAmount = "SELECT MAX_AMOUNT FROM ST_APPROVAL_LEVEL WHERE LEVEL_CODE="+organPosition_; 
                
    ArrayList list = new ArrayList();
     int nextOrganPosition = 0;
     double minAmtLimit = 0;
     double maxAmtLimit = 0;
     //double amount_ = 0;
     String strMaxAmount = getCodeName(currUserMaxAmount);
     amount = (strMaxAmount!=null && !strMaxAmount.equals(""))? Double.parseDouble(strMaxAmount):0;
     list = findItemApprovalDetailByQuery(query);//invent.findAllItemApprovalDetail(itemCode);
     
     if((list!=null) && (list.size()>0)){
     for(int i=0;i<list.size();i++){
        //minAmtLimit = ((ItemApprovalDetail)list.get(i)).getMinAmtLimit();
        maxAmtLimit = ((ItemApprovalDetail)list.get(i)).getMaxAmtLimit();
        if(maxAmtLimit>=amount){
            nextOrganPosition = ((ItemApprovalDetail)list.get(i)).getOrganPosition();
            break;
            
        }
      }
     }
    
     return nextOrganPosition;
    }
    
    public boolean isItemRequireApproval(String itemCode)
     {
      boolean result = false;
      String requireApproval = this.getCodeName("SELECT REQ_APPROVAL FROM ST_INVENTORY_ITEMS WHERE ITEM_CODE='"+itemCode+"'");
      String strApprovalLevelNum = this.getCodeName("SELECT COUNT(*) FROM ST_ITEM_APPROVAL_DETAIL WHERE ITEM_CODE='"+itemCode+"'");
      
      int approvalLevelNum =  ((strApprovalLevelNum != null) && (!strApprovalLevelNum.equals(""))) ? Integer.parseInt(strApprovalLevelNum) : 0;
      
      if((requireApproval.equals("Y"))&&(approvalLevelNum > 0)){
          result = true;
      }
      return result;     
      }
    
    public int countPendingTransaction(int superId)
     {
      String query = "SELECT ISNULL(count(*),0) FROM ST_TRANSACTION_APPROVAL WHERE APPROVE_OFFICER = ? AND APPROVE_OFFICER != ?";
                            
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      
      ArrayList list = new ArrayList();
      int rowNum = 0;
      try 
      {
       con = getConnection();
       ps = con.prepareStatement(query);
       ps.setInt(1,superId);
       ps.setInt(2,0);
       rs = ps.executeQuery();
       while (rs.next()) 
       {
        rowNum = rs.getInt(1);
       }
      }
      catch (Exception er) 
      {
        System.out.println("Error getting Number Of Pending Transaction...->"+er.getMessage());       
      }
      finally 
      {
          closeConnection(con,ps,rs);
      }
      return rowNum;     
      }
    
    public String getCodeName(String query)
    {
     String result = "";
     Connection con = null;
     ResultSet rs = null; 
     PreparedStatement ps = null;
     
     try
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while(rs.next())
      {
       result = rs.getString(1) == null ? "" : rs.getString(1);
      }
     }
     catch(Exception er)
     {
        System.out.println("Error in getCodeName()... ->"+er);
        er.printStackTrace();
     }finally{
        closeConnection(con,ps);
      }   
      return result;
     }
     
    public ArrayList findItemApprovalDetailByQuery(String query){

     String SELECT_QUERY = query;
     
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     ArrayList records = new java.util.ArrayList();
     try
      {
       con = getConnection();
       ps = con.prepareStatement(SELECT_QUERY);
       rs  = ps.executeQuery();
       while(rs.next())
       {
        String mtId = rs.getString("MTID");
        int organPosition = rs.getInt("LEVEL_CODE");
        String itemCode = "";//rs.getString("ITEM_CODE");
        int concurrence = rs.getInt("CONCURRENCE");
        int userId = rs.getInt("USERID");
        String transDate = formatDate(rs.getDate("TRANS_DATE"));
        double minAmtLimit = rs.getDouble("MIN_AMOUNT");
        double maxAmtLimit = rs.getDouble("MAX_AMOUNT");
        ItemApprovalDetail itemApprove = new ItemApprovalDetail(mtId,itemCode,organPosition,userId,transDate,concurrence,minAmtLimit,maxAmtLimit);
        records.add(itemApprove);

        }
      }
      catch(Exception er){
       System.out.println("Error RETRIEVING APPROVAL LEVEL... ->"+er.getMessage());
       er.printStackTrace();
       }
       finally{
         closeConnection(con,ps,rs);
        }
       return records ;
      }
     
     private int getCurrentApprovalLevel(String itemCode,double amount){
       ArrayList list = new ArrayList(); 
       /*String query = "SELECT a.ITEM_CODE,a.MtId,a.APPROVE_LEVEL,a.CONCURRENCE,a.USERID,a.TRANS_DATE,b.MIN_AMOUNT,b.MAX_AMOUNT " + 
                      "FROM ST_ITEM_APPROVAL_DETAIL a,ST_APPROVAL_LEVEL b WHERE a.APPROVE_LEVEL=b.LEVEL_CODE AND a.ITEM_CODE='"+itemCode+"'"; 
       */
        String query = "SELECT MTID,LEVEL_CODE,MIN_AMOUNT,MAX_AMOUNT,DESCRIPTION,CONCURRENCE,TRANS_DATE,USERID " +
        		"FROM ST_APPROVAL_LEVEL ORDER BY MAX_AMOUNT ASC"; 
               
       list = findItemApprovalDetailByQuery(query);
       int nextOrganPosition = 0;
       //double minAmtLimit = 0;
       double maxAmtLimit = 0;
       if((list!=null) && (list.size()>0)){
       for(int i=0;i<list.size();i++){
          //minAmtLimit = ((ItemApprovalDetail)list.get(i)).getMinAmtLimit();
          maxAmtLimit = ((ItemApprovalDetail)list.get(i)).getMaxAmtLimit();
          if((maxAmtLimit>=amount)){
             
              nextOrganPosition = ((ItemApprovalDetail)list.get(i)).getOrganPosition();
              break;
              
          }
           
       }
       }
       return nextOrganPosition;
      }
      
    private int getCurrentApprovalLevel(String itemCode){
      
      int nextOrganPosition = 0;
      String strNextOrganPosition = getCodeName("SELECT MAX(APPROVE_LEVEL) AS APPROVE_LEVEL FROM ST_ITEM_APPROVAL_DETAIL WHERE ITEM_CODE='"+itemCode+"'");
      nextOrganPosition=((strNextOrganPosition!=null) && !strNextOrganPosition.equals("")) ? Integer.parseInt(strNextOrganPosition) : 0;
      return nextOrganPosition;
     }
    
    public boolean getNewYearCalendar(String companyCode, String financeStartYear, String financeEndYear)
    {
//    	String financeStartYear = getCodeName("select financial_start_date from MG_gb_company where company_code='"+companyCode+"'");
//    	String financeEndYear = getCodeName("select financial_end_date from MG_gb_company where company_code='"+companyCode+"'");
    	String query = "drop table MG_GB_CALENDAR " +
    					"declare @start_date datetime, @end_date datetime, @cur_date datetime "+
    					"set @start_date = '"+financeStartYear+"' "+
    					"set @end_date = '"+financeEndYear+"' "+
    					"set @cur_date = @start_date "+
    					"create table MG_GB_CALENDAR "+
    					"(ID int IDENTITY(1,1) NOT NULL, weekday varchar(10), "+
    					"day int, "+
    					"month varchar(10), "+
    					"year int, Public_Holiday varchar(15), "+
    					"Process_Status char(1),Calendar_Date datetime,"+
    					"GL_Closing_Status char(1),CloseDate datetime) "+
    					"while @cur_date <= @end_date "+
    					"begin "+
    					"insert into MG_GB_CALENDAR "+
    					"select datename(dw, @cur_date), datepart(day, @cur_date), datename(month, @cur_date), datepart(year, @cur_date), '' AS Public_Holiday, 'N' AS Process_Status, @cur_date AS Calendar_Date,'N' AS GL_Closing_Status, "+
    					"(SELECT DATEADD(dd, -DATEPART(WEEKDAY, DATEADD(dd, -DAY(DATEADD(mm, 1, @cur_date)), DATEADD(mm, 1, @cur_date))) + 1,"+
    					"DATEADD(dd, -DAY(DATEADD(mm, 1, @cur_date)), DATEADD(mm, 1, @cur_date)))) AS CloseDate  "+
    					"set @cur_date = dateadd(dd, 1, @cur_date) "+
    					"update MG_GB_CALENDAR set Public_Holiday = 'Yes' where weekday = 'Saturday' or weekday = 'Sunday' or (month = 'December' and (day = '25' or day = '26')) or (month = 'January' and day = '1') "+
    					"UPDATE MG_GB_CALENDAR set GL_Closing_Status = 'Y' WHERE CALENDAR_DATE  = CloseDate "+
    					"end";
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     boolean result = true;
     ArrayList list = new ArrayList();
     int rowNum = 0;
     System.out.println("<<<<<<<getNewYearCalendar query: "+query);
     try 
     {
      con = getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      result = true;
      while (rs.next()) 
      {
       rowNum = rs.getInt(1);
      }
     }
     catch (Exception er) 
     {
       System.out.println("Error getting New Yaer Calendar for the new processing Year..->"+er.getMessage());       
     }
     finally 
     {
         closeConnection(con,ps,rs);
     }
     System.out.println("<<<<<<result: "+result);
     return result;     
     }
    
}
