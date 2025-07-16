package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FilePermission;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.io.BufferedInputStream;
import java.math.BigDecimal;
//import oracle.jdbc.OracleResultSet;
//import oracle.sql.BLOB;
import java.util.*;


import com.magbel.util.DatetimeFormat;
import com.magbel.util.CurrencyNumberformat;
import com.magbel.util.HtmlUtilily;

//import magma.util.Codes;
public class ApprovalRecords extends PersistenceServiceDAO {

    private CurrencyNumberformat formata;
    private SimpleDateFormat sdf;
    private DatetimeFormat df;
    ArrayList Alist = new ArrayList();
    private String qryCheck = "";
    ApplicationHelper helper;
    HtmlUtilily HtmlUtilily;
    public ApprovalRecords() {
        super();
    	HtmlUtilily = new HtmlUtilily();
    }
   
    public String userEmail(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from ApplicationForm WHERE user_id = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }
    //delete record from raise entry list base on asset id and page name

    public String userToEmail(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from ApplicationForm WHERE user_id = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }
    //delete record from raise entry list base on asset id and page name
    
    public String userToEmailTransInitiator(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from ApplicationForm WHERE user_id = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }
    //delete record from raise entry list base on asset id and page name
    public String userToEmailTransInitiator(int user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from ApplicationForm WHERE user_id = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setInt(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }
    public String userToEmail2(String user_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";

        String FINDER_QUERY = "SELECT email from ApplicationForm WHERE user_id = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, user_id);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch email->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        System.out.println(">>>>>The user id is " + user_id + " the emial is " + email);
        return email;

    }

    public void updateDRBalances(String AcctNo, String BranchNo,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
  //      System.out.println("updateDRBalances Account No: "+AcctNo+" Branch No: "+BranchNo+" Trans Amount:"+tranAmount);
      //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
 //       String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_code = "+BranchNo+" AND Processed = 'N' ";
        System.out.println("updateDRBalancesUPDATE_QUERY: "+UPDATE_QUERY);
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
 //           ps = con.prepareStatement(UPDATEProcess_QUERY); 
 //           ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_GL_BALANCES+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    public void updateCustomerDRBalances(String AcctNo, String BranchNo,double tranAmount,double acctbalance) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
   //     String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE= "+acctbalance+" - "+tranAmount+", CLEAR_BALANCE = "+acctbalance+" - "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"'";
        String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE= "+acctbalance+"  - "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' ";
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                      
            ps.executeUpdate();
            ps1 = con.prepareStatement(UPDATE_QUERY2); 
            ps1.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
            closeConnection(con, ps1);
        }

    }

    public boolean updateCRBalances(String AcctNo, String BranchNo,double tranAmount) {
    	System.out.println();
      //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        Connection con = null;
        PreparedStatement ps = null; 
 //       System.out.println("updateCRBalances Account No: "+AcctNo+" Branch No: "+BranchNo+" Trans Amount:"+tranAmount);
        String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo.trim()+"' AND BRANCH_CODE = '"+BranchNo+"' ";
//        String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = "+BranchNo+" ";
        System.out.println("updateCRBalances UPDATE_QUERY: "+UPDATE_QUERY);
//        System.out.println("tranAmount: "+tranAmount+" BranchNo: "+BranchNo+" AcctNo: "+AcctNo);
        boolean done;
        done = false;
        try {
           // con = getConnection("eschool");
        	con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);         
            ps.executeUpdate();
 //           ps = con.prepareStatement(UPDATEProcess_QUERY);
  //          ps.executeUpdate();            
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_GL_BALANCES IN updateCRBalances+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean updateCustomerCRBalances(String AcctNo, String BranchNo,double tranAmount,double acctbalance) {
        //String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String UPDATE_QUERY = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE= "+acctbalance+"  + "+tranAmount+", CLEAR_BALANCE = "+acctbalance+"  + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' ";
        String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE= "+acctbalance+"  + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' ";
        System.out.println("<<<UPDATE_QUERY: "+UPDATE_QUERY);
        System.out.println("<<<UPDATE_QUERY2: "+UPDATE_QUERY2);
        boolean done;
        done = false;
        try {
           // con = getConnection("eschool");
        	con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);         
            ps.executeUpdate();
            ps1 = con.prepareStatement(UPDATE_QUERY2);
            ps1.executeUpdate();            
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
            closeConnection(con, ps1);
        }
        return done;
    }

    public boolean DeleteGLTransactions(String familyid) {

        Connection con = null;
        PreparedStatement ps = null;
        String DELETE_QUERY = "DELETE FROM IA_GL_HISTORY WHERE FAMILY_ID = '"+familyid+"' ";
  //      System.out.println("DeleteGLTransactions DELETE_QUERY: "+DELETE_QUERY);
        boolean done;
        done = false;
        try {
            //con = getConnection("eschool");
            con = getConnection();
            ps = con.prepareStatement(DELETE_QUERY);         
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot Delete Record From IA_GL_HISTORY+" + ex.getMessage());
            done = false;
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public boolean DeleteCUST_GLTransactions(String familyid) {

        Connection con = null;
        PreparedStatement ps = null;
        String DELETE_QUERY1 = "DELETE FROM IA_GL_HISTORY WHERE FAMILY_ID = '"+familyid+"' ";
        String DELETE_QUERY2 = "DELETE FROM IA_PAYMENT_HISTORY WHERE FAMILY_ID = '"+familyid+"' ";
        String DELETE_QUERY3 = "DELETE FROM IA_STUDENT_PAYMENT_BREAKDOWN WHERE FAMILY_ID = '"+familyid+"' ";
 //       System.out.println("DeleteCUST_GLTransactions DELETE_QUERY1: "+DELETE_QUERY1);
 //       System.out.println("DeleteCUST_GLTransactions DELETE_QUERY2: "+DELETE_QUERY2);
        boolean done;
        done = false;
        try {
            //con = getConnection("eschool");
            con = getConnection();
            ps = con.prepareStatement(DELETE_QUERY1);         
            ps.executeUpdate();
            ps = con.prepareStatement(DELETE_QUERY2);         
            ps.executeUpdate();   
            ps = con.prepareStatement(DELETE_QUERY3);         
            ps.executeUpdate();            
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot Delete Record From IA_GL_HISTORY+" + ex.getMessage());
            done = false;
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    
    public boolean DeleteCustomerTransactions(String familyId) {

        Connection con = null;
        PreparedStatement ps = null;
        String DELETE_QUERY = "DELETE FROM IA_PURCHASES_PAYMENTS WHERE FAMILY_ID = '"+familyId+"' ";
        boolean done;
        done = false;
        try {
            //con = getConnection("eschool");
            con = getConnection();
            ps = con.prepareStatement(DELETE_QUERY);         
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot Delete Record From IA_PURCHASES_PAYMENTS+" + ex.getMessage());
            done = false;
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public String GetDepositNo(String depocode) {
   // 	System.out.println("depocode==== "+depocode);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String DepositCode = "";
        String FINDER_QUERY = "SELECT Deposit_Code FROM AVAILABLE_DEPOSITSLIP WHERE DEPOSIT_CODE = ? ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, depocode);

            rs = ps.executeQuery();

            while (rs.next()) {
            	DepositCode = rs.getString("Deposit_Code");
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch Deposit Code->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps, rs); 
        }
        System.out.println(">>>>>The Deposit Code is " + depocode + " the DepositCode is " + DepositCode);
        return DepositCode;

    }  
    public void updateDRCustomerBalances(String AcctNo, String BranchId,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
        //String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";        
       // String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_no = "+BranchId+" AND Processed = 'N' ";
        System.out.println("updateDRCustomerBalances tranAmount: "+tranAmount);
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY1);                    
            ps.executeUpdate();
        //    ps = con.prepareStatement(UPDATE_QUERY2); 
        //    ps.executeUpdate();            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    public void updateDStudentBalances(String AcctNo, String BranchId,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",TERM_BALANCE = TERM_BALANCE + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
        String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";        
       // String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_no = "+BranchId+" AND Processed = 'N' ";
//        System.out.println("updateDRCustomerBalances tranAmount: "+tranAmount);
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY1);                    
            ps.executeUpdate();
            ps = con.prepareStatement(UPDATE_QUERY2); 
            ps.executeUpdate();            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY  AND IA_CUSTOMER_ACCOUNT_SETUP +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }    
    public boolean RetireImprestItems(String mtid,String benacc,String refNo) {

        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_IMPREST_ITEMS SET RETIRED = 'Y' WHERE MTID = '"+mtid+"' ";
		String  actualAmount = HtmlUtilily.getCodeName("SELECT AMOUNT FROM IA_IMPRESTS WHERE BEN_ACC_NUMBER='"+benacc+"'");
		String  sumAmount = HtmlUtilily.getCodeName("SELECT SUM(AMOUNT) FROM IA_IMPREST_ITEMS GROUP BY REF_NUMBER HAVING REF_NUMBER IN(SELECT REF_NUMBER FROM IA_IMPRESTS WHERE BEN_ACC_NUMBER='"+benacc+"')");
        sumAmount = sumAmount.replace(",","");
		actualAmount = actualAmount.replace(",","");		
        double  actualAmountNo = ((actualAmount != null)&&(!actualAmount.equals(""))) ? Double.parseDouble(actualAmount) : 0;
		double  sumAmountNo = ((sumAmount != null)&&(!sumAmount.equals(""))) ? Double.parseDouble(sumAmount) : 0;
		double checkbalance = sumAmountNo - actualAmountNo;
//		System.out.println("checkbalance RetireImprestItems: "+checkbalance);
		 String UPDATE_QUERY2 = "UPDATE IA_IMPRESTS SET ISRETIRED = 'Y' WHERE REF_NUMBER = '"+refNo+"' ";
        boolean done;
        done = false;
        try {
            //con = getConnection("eschool");
            con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);         
            ps.executeUpdate();
            if(checkbalance==0){
                ps = con.prepareStatement(UPDATE_QUERY2);         
                ps.executeUpdate();
            }
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot Update Record From IA_IMPREST_ITEMS+" + ex.getMessage());
            done = false;
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public  String getId(String tableName) {
    	//System.out.println("getGeneratedId tableName: "+tableName);
    	//	dateFormat = new DatetimeFormat();
    	    sdf = new SimpleDateFormat("dd-MM-yyyy");

    	    int counter = 0;
    		final String FINDER_QUERY = "SELECT MT_ID + 1 FROM IA_MTID_TABLE "+
    								    "WHERE MT_TABLENAME = '"+tableName+"'";
    		final String UPDATE_QUERY = "UPDATE IA_MTID_TABLE SET MT_ID = MT_ID + 1 "+
    									"WHERE MT_TABLENAME = '"+tableName+"'";
    		String id = "";
//    		System.out.println("getGeneratedId FINDER_QUERY: "+FINDER_QUERY);
    		Connection con = null;
    		ResultSet rs = null;
    		PreparedStatement ps = null;

    		try{

    			con = getConnection();
    			ps = con.prepareStatement(FINDER_QUERY);
    			rs = ps.executeQuery();

    			while(rs.next()){
    				counter = rs.getInt(1);
    			}

    			ps = con.prepareStatement(UPDATE_QUERY);
    			ps.execute();


    		}catch(Exception ex){
    			System.out.println("WARN:Error generating id for table:"+
    			tableName+"\n"+ex);
    		}finally{
    			closeConnection(con,ps,rs);
    		}

    		 id = Integer.toString(counter);
    		return id;

    	   }
    ///all the 4 insertApproval methods below are to ensure that asset code is inserted into am_raisentry_post
    public boolean insertApprovalx(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat, String whTax, String url, int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setInt(11, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done; 
    }

    public boolean insertApprovalx(String id, String description, String page, String flag, String partPay,
            String UserId, String Branch, String subjectToVat, String whTax, String url, int transID, int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,Branch,"
                + "subjectToVat,whTax,url,trans_id,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    /*    System.out.println("=====query for insertApprovalx == "+query);
        System.out.println("=====id for insertApprovalx == "+id+" ==description== "+description);
        System.out.println("=====page for insertApprovalx == "+page+" ==flag== "+flag);
        System.out.println("=====partPay for insertApprovalx == "+partPay+" ==UserId== "+UserId);
        System.out.println("=====Branch for insertApprovalx == "+Branch+" ==subjectToVat== "+subjectToVat);           
        System.out.println("=====url for insertApprovalx == "+url+" ==whTax== "+whTax);   
        System.out.println("=====transID for insertApprovalx == "+transID+" ==assetCode== "+assetCode);
        */           
        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setInt(11, transID);
            ps.setInt(12, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean insertApproval2x(String id, String description, String page, String flag, String partPay, String UserId,
            String Branch, String subjectToVat, String whTax, String url, int transID, int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,UserId,"
                + "Branch,subjectToVat,whTax,url,trans_id,entryPostFlag,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setInt(11, transID);
            ps.setString(12, "N");
            ps.setInt(13, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean insertApprovalx(String id, String description, String page, String flag,
            String partPay, String UserId, String Branch, String subjectToVat, String whTax, String url, String tranId, int assetCode) {
        boolean done = true;
        flag = "Y";
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO [am_raisentry_post](Id,Description,Page,Flag,partPay,"
                + "UserId,Branch,subjectToVat,whTax,url,trans_id,asset_code)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, description);
            ps.setString(3, page);
            ps.setString(4, flag);
            ps.setString(5, partPay);
            ps.setString(6, UserId);
            ps.setString(7, Branch);
            ps.setString(8, subjectToVat);
            ps.setString(9, whTax);
            ps.setString(10, url);
            ps.setString(11, tranId);
            ps.setInt(12, assetCode);
            ps.execute();

        } catch (Exception ex) {
            done = false;
            System.out.println(
                    "WARNING:cannot insert am_raisentry_post->"
                    + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }
    public void updateRaiseEntry(String assetid) {

        Connection con = null;
        PreparedStatement ps = null;
        String NOTIFY_QUERY = "UPDATE am_asset SET raise_entry = ? WHERE ASSET_ID = ?  ";

        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(NOTIFY_QUERY);
            ps.setString(1, "Y");
            ps.setString(2, assetid);
            ps.executeUpdate();  

        } catch (Exception ex) {
            System.out.println("WARNING: cannot update am_asset+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    public String getCodeName(String query) {
        String result = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null; 
//System.out.println("====query=====  "+query);
        try {
            con = getConnection("eschool");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();


            while (rs.next()) {
                result = rs.getString(1) == null ? "" : rs.getString(1);

            }
        } catch (Exception er) {
            System.out.println("Error in Query- getCodeName()... ->" + er);
            er.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return result;
    }
    public boolean updateNewStudentPayment(String AppNo, double tranAmount,String pagename) {
        //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
          Connection con = null;
          PreparedStatement ps = null; 
          String UPDATE_QUERY = "UPDATE IA_NEW_STUDENT_PAYMENT SET AMOUNT_PAID = AMOUNT_PAID + "+tranAmount+",PAGE_NAME = '"+pagename+"' WHERE Form_Id = '"+AppNo+"'";
          boolean done;
          done = false;
          try {
          	con = getConnection();
              ps = con.prepareStatement(UPDATE_QUERY);         
              ps.executeUpdate();         
              done = true;
          } catch (Exception ex) {
              System.out.println("WARNING: cannot update IA_NEW_STUDENT_PAYMENT IN updateNewStudentPayment+" + ex.getMessage());
          } finally {
              closeConnection(con, ps);
          }
          return done;
      }
    public void updatePVSUMMARY(String invoiceno) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_PV_SUMMARY SET STATUS = 'PAY' WHERE INVOICE_NO = '"+invoiceno+"' AND STATUS IS NULL  ";
        System.out.println("updatePVSUMMARY=== "+UPDATE_QUERY);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_PV_SUMMARY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        } 

    }
    
    public void updatePCSUMMARY(String invoiceno) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_PC_SUMMARY SET STATUS = 'PAY' WHERE INVOICE_NO = '"+invoiceno+"' AND STATUS IS NULL  ";
        System.out.println("updatePCSUMMARY=== "+UPDATE_QUERY);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_PC_SUMMARY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    public boolean updateStudentAccountAtStartofTerm(String branchcode,String userid, double sumAmountNo) {
		System.out.println("====updateStudentAccountAtStartofTerm sumAmountNo=====  "+sumAmountNo);
    	   String query = "update IA_CUSTOMER_ACCOUNT_DISPLAY set ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+sumAmountNo+", CLEAR_BALANCE = CLEAR_BALANCE + "+sumAmountNo+", TERM_BALANCE = TERM_BALANCE - "+sumAmountNo+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+sumAmountNo+" WHERE BRANCH_CODE = '"+branchcode+"' AND STATUS = 'ACTIVE' ";
    	   String query2 = "update IA_CUSTOMER_ACCOUNT_SETUP set ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+sumAmountNo+",no_debits_ptd = no_debits_ptd + 1 WHERE BRANCH_CODE = '"+branchcode+"' AND STATUS = 'ACTIVE' ";
//    	   System.out.println("====updateStudentAccountAtStartofTerm query=====  "+query);
//    	   System.out.println("====updateStudentAccountAtStartofTerm query2=====  "+query2);
    	       Connection con = null;
    	       PreparedStatement ps = null;
    	       PreparedStatement ps1 = null;
    	       boolean done = false;                          
    	       try { 
    	               con = getConnection();
    	               ps = con.prepareStatement(query);   
    	               done = (ps.executeUpdate() != -1);     
    	               ps = con.prepareStatement(query2);   
    	               done = (ps.executeUpdate() != -1);                 
    	       } catch (Exception ex) {
    	               done = false;
    	               System.out.println("ERROR Updating IA_CUSTOMER_ACCOUNT_DISPLAY and IA_CUSTOMER_ACCOUNT_SETUP with Student balance " + ex.getMessage());
    	               ex.printStackTrace();
    	       } finally {
    	               closeConnection(con, ps);
    	       }
    	       return done;
    	}   

    public boolean updateStudentAccountAtStartofTermPerAccount(String branchcode,String userid, double sumAmountNo,String adminNo) {
		//System.out.println("====updateStudentAccountAtStartofTermPerAccount sumAmountNo=====  "+sumAmountNo);
    	   String query = "update IA_CUSTOMER_ACCOUNT_DISPLAY set ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+sumAmountNo+", CLEAR_BALANCE = CLEAR_BALANCE - "+sumAmountNo+", TERM_BALANCE = TERM_BALANCE - "+sumAmountNo+",TERM_FEES = TERM_FEES - "+sumAmountNo+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+sumAmountNo+" WHERE ADMIN_NO = '"+adminNo+"' AND STATUS = 'ACTIVE' ";
    	   String query2 = "update IA_CUSTOMER_ACCOUNT_SETUP set ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+sumAmountNo+",TERM_FEES = TERM_FEES - "+sumAmountNo+",no_debits_ptd = no_debits_ptd + 1 WHERE ADMIN_NO = '"+adminNo+"' AND STATUS = 'ACTIVE' ";
    	   System.out.println("====updateStudentAccountAtStartofTerm query=====  "+query);
    	   System.out.println("====updateStudentAccountAtStartofTerm query2=====  "+query2);
    	       Connection con = null;
    	       PreparedStatement ps = null;
    	       PreparedStatement ps1 = null;
    	       boolean done = false;                          
    	       try { 
    	               con = getConnection();
    	               ps = con.prepareStatement(query);   
    	               done = (ps.executeUpdate() != -1);     
    	               ps = con.prepareStatement(query2);   
    	               done = (ps.executeUpdate() != -1);                 
    	       } catch (Exception ex) {
    	               done = false;
    	               System.out.println("ERROR Updating IA_CUSTOMER_ACCOUNT_DISPLAY and IA_CUSTOMER_ACCOUNT_SETUP with Student balance " + ex.getMessage());
    	               ex.printStackTrace();
    	       } finally {
    	               closeConnection(con, ps);
    	       }
    	       return done;
    	}   

    
    
    public void updateStudentCRBalances(String AcctNo, String BranchNo,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",CLEAR_BALANCE = CLEAR_BALANCE + "+tranAmount+",TERM_BALANCE = TERM_BALANCE + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
         System.out.println("updateStudentCRBalances: "+UPDATE_QUERY1);
         String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";         
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY1);                    
            ps.executeUpdate();
            ps = con.prepareStatement(UPDATE_QUERY2);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    public void updateStudentDRBalances(String AcctNo, String BranchNo,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY1 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",TERM_BALANCE = TERM_BALANCE - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
         System.out.println("updateStudentDRBalances: "+UPDATE_QUERY1);
         String UPDATE_QUERY2 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET ACCOUNT_BALANCE = ACCOUNT_BALANCE - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1 WHERE ACCOUNT_NO = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";         
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY1);                    
            ps.executeUpdate();
            ps = con.prepareStatement(UPDATE_QUERY2);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_CUSTOMER_ACCOUNT_DISPLAY For DEBIT+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    
    public boolean updateApplicationFormPayment(String FormNo, String BranchNo,double tranAmount) {
          Connection con = null;
          PreparedStatement ps = null; 
          String UPDATE_QUERY = "UPDATE ApplicationForm SET APPLICATION_FEE = APPLICATION_FEE + "+tranAmount+" WHERE gl_acct_no = '"+FormNo.trim()+"' AND Branch_Code = '"+BranchNo+"' ";
          boolean done;
          done = false;
          try {
          	con = getConnection();
              ps = con.prepareStatement(UPDATE_QUERY);         
              ps.executeUpdate();         
              done = true;
          } catch (Exception ex) {
              System.out.println("WARNING: cannot update IA_GL_BALANCES IN updateCRBalances+" + ex.getMessage());
          } finally {
              closeConnection(con, ps);
          }
          return done;
      }
    public void updateFormRecord(String FormId) {
        Connection con = null;
        PreparedStatement ps = null;
        String query2 = "UPDATE ApplicationForm SET STATUS = 'ACCEPT' WHERE Form_Id = '"+FormId+"'";
        String query = "UPDATE SHORTLISTING SET STATUS = 'ACCEPT' WHERE Form_Id = '"+FormId+"'";        
        try {
       	   con = getConnection();
            ps = con.prepareStatement(query);                    
            ps.executeUpdate(); 
            ps = con.prepareStatement(query2);                    
            ps.executeUpdate();              
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update ApplicationForm+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public void updateFilesForCloseMonth(String companyCode,String schoolCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String query3 = "UPDATE IA_CUSTOMER SET STATUS = 'CLOSED' WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+schoolCode+"'";    
        String query4 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET STATUS = 'CLOSED' WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+schoolCode+"'"; 
        String query5 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET STATUS = 'CLOSED' WHERE STATUS = 'ACTIVE'  AND COMP_CODE = '"+companyCode+"' AND BRANCH_CODE = '"+schoolCode+"'";
        try {
       	   con = getConnection();
            ps = con.prepareStatement(query3);                    
            ps.executeUpdate(); 
            ps1 = con.prepareStatement(query4);                    
            ps1.executeUpdate();      
            ps2 = con.prepareStatement(query5);                    
            ps2.executeUpdate(); 
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update Files for Close Term+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
            closeConnection(con, ps1);
            closeConnection(con, ps2);
        }

    }    
    public boolean DeleteSlipNo(String Bankcode, String DepositNo) {

        Connection con = null;
        PreparedStatement ps = null;
        String DELETE_QUERY = "DELETE FROM AVAILABLE_DEPOSITSLIP WHERE BANK_CODE = '"+Bankcode+"' AND DEPOSIT_CODE = '"+DepositNo+"' ";
  //      System.out.println("DeleteGLTransactions DELETE_QUERY: "+DELETE_QUERY);
        boolean done;
        done = false;
        try {
            //con = getConnection("eschool");
            con = getConnection();
            ps = con.prepareStatement(DELETE_QUERY);         
            ps.executeUpdate();
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot Delete Slip Number From AVAILABLE_DEPOSITSLIP+" + ex.getMessage());
            done = false;
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }    
    public void updateDRBalancesforReversal(String AcctNo, String BranchNo,double tranAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        System.out.println("updateDRBalances Account No: "+AcctNo+" Branch No: "+BranchNo+" Trans Amount:"+tranAmount);
      //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal - "+tranAmount+",no_debits_ptd = no_debits_ptd + 1,amt_debits_ptd = amt_debits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = '"+BranchNo+"' ";
 //       String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_code = "+BranchNo+" AND Processed = 'N' ";
        System.out.println("updateDRBalancesUPDATE_QUERY: "+UPDATE_QUERY);
        try {
           // con = getConnection("eschool");
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
 //           ps = con.prepareStatement(UPDATEProcess_QUERY); 
 //           ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_GL_BALANCES+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }

    public boolean updateCRBalancesforReversal(String AcctNo, String BranchNo,double tranAmount) {
    	System.out.println();
      //  String BranchNo = HtmlUtilily.getCodeName("Select branch_code from MG_AD_Branch where branch_id = "+BranchId+"");
        Connection con = null;
        PreparedStatement ps = null; 
        System.out.println("updateCRBalances Account No: "+AcctNo+" Branch No: "+BranchNo+" Trans Amount:"+tranAmount);
        String UPDATE_QUERY = "UPDATE IA_GL_BALANCES SET current_bal = current_bal + "+tranAmount+",no_credits_ptd = no_credits_ptd + 1,amt_credits_ptd = amt_credits_ptd + "+tranAmount+" WHERE gl_acct_no = '"+AcctNo.trim()+"' AND Branch_Code = '"+BranchNo+"' ";
        String UPDATEProcess_QUERY = "UPDATE IA_GL_HISTORY SET Processed = 'Y' WHERE gl_acct_no = '"+AcctNo+"' AND Branch_Code = "+BranchNo+" ";
        System.out.println("updateCRBalances UPDATE_QUERY: "+UPDATE_QUERY);
//        System.out.println("tranAmount: "+tranAmount+" BranchNo: "+BranchNo+" AcctNo: "+AcctNo);
        boolean done;
        done = false;
        try {
           // con = getConnection("eschool");
        	con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);         
            ps.executeUpdate();
 //           ps = con.prepareStatement(UPDATEProcess_QUERY);
  //          ps.executeUpdate();            
            done = true;
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_GL_BALANCES IN updateCRBalances+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean UpdateNewCreatedStudent(String adminNo,String schoolcode,String companycode) {
    double drAmount = 0.00;
    double crAmount = 0.00;
    int drNo = 0;  int crNo = 0;
    String query = "UPDATE IA_NEW_CUSTOMER SET STATUS = NULL WHERE ADMIN_NO = '"+adminNo+"' AND STATUS = 'NOT PAID' ";
    //System.out.println("UpdateNewCreatedStudent query: "+query);
    Connection con = null;
    PreparedStatement ps = null;
    boolean done = false;
      
    try {
    con = getConnection();
    ps = con.prepareStatement(query);
    done = (ps.executeUpdate() != -1);
    } catch (Exception ex) {
    done = false;
    System.out.println("ERROR Updating IA_NEW_CUSTOMER table " + ex.getMessage());
    ex.printStackTrace();
    } finally {
    closeConnection(con, ps);
    }
    return done;
    }
    public void updateFilesForStartTerm(String schoolCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        String query = "UPDATE SHORTLISTING SET STATUS = 'ACTIVE' WHERE STATUS = 'CLOSE' AND SCHOOL = '"+schoolCode+"'";
        String query2 = "UPDATE ApplicationForm SET STATUS = 'ACTIVE' WHERE STATUS = 'CLOSE' AND SCHOOL = '"+schoolCode+"'";
        String query3 = "UPDATE IA_CUSTOMER SET STATUS = 'ACTIVE' WHERE STATUS = 'CLOSE' AND BRANCH_CODE = '"+schoolCode+"'";    
        String query4 = "UPDATE IA_CUSTOMER_ACCOUNT_DISPLAY SET STATUS = 'ACTIVE' WHERE STATUS = 'CLOSE' AND BRANCH_CODE = '"+schoolCode+"'"; 
        String query5 = "UPDATE IA_CUSTOMER_ACCOUNT_SETUP SET STATUS = 'ACTIVE' WHERE STATUS = 'CLOSE' AND BRANCH_CODE = '"+schoolCode+"'";
        try {
       	   con = getConnection();
            ps = con.prepareStatement(query);                    
            ps.executeUpdate(); 
            ps1 = con.prepareStatement(query2);                    
            ps1.executeUpdate();      
            ps2 = con.prepareStatement(query3);                    
            ps2.executeUpdate();     
            ps3 = con.prepareStatement(query4);                    
            ps3.executeUpdate();     
            ps4 = con.prepareStatement(query5);                    
            ps4.executeUpdate();                 
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update Files for Start Term+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
            closeConnection(con, ps1);
            closeConnection(con, ps2);
            closeConnection(con, ps3);
            closeConnection(con, ps4);
        }

    }    
    public void updateUploadReversalFile(String companyCode,String schoolCode,int id) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE REVERSAL_UPLOAD_TMP SET PROCESS_FLAG = 'Y' WHERE PROCESS_FLAG = 'N' AND COMP_CODE = '"+companyCode+"' AND SCHOOL_CODE = '"+schoolCode+"' AND ID = "+id+"";
     //   System.out.println("updateUploadReversalFile query: "+query);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(query);                    
            ps.executeUpdate(); 
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update Upload File for Start Term+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }    
    public void PassingOutStudent(String companyCode,String schoolCode,String adminNo) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE IA_CUSTOMER SET STATUS = 'PASSEDOUT' WHERE COMP_CODE = '"+companyCode+"' AND SCHOOL = '"+schoolCode+"' AND ADMIN_NO = '"+adminNo+"'";
//        System.out.println("PassingOutStudent query: "+query);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(query);                    
            ps.executeUpdate(); 
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update Student to Pass Out +" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }   
    
    public void updatePVSUMMARY(String invoiceno, String orderNo) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_PV_SUMMARY SET STATUS = 'PAY', ORDER_NO = '"+orderNo+"' WHERE INVOICE_NO = '"+invoiceno+"' ";
        System.out.println("updatePVSUMMARY=== "+UPDATE_QUERY);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_PV_SUMMARY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        } 

    }
    
    public void updatePCSUMMARY(String invoiceno, String orderNo) {
        Connection con = null;
        PreparedStatement ps = null;
        String UPDATE_QUERY = "UPDATE IA_PC_SUMMARY SET STATUS = 'PAY', ORDER_NO = '"+orderNo+"' WHERE INVOICE_NO = '"+invoiceno+"' ";
        System.out.println("updatePCSUMMARY=== "+UPDATE_QUERY);
        try {
       	   con = getConnection();
            ps = con.prepareStatement(UPDATE_QUERY);                    
            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("WARNING: cannot update IA_PC_SUMMARY+" + ex.getMessage());
        } finally {
            closeConnection(con, ps);
        }

    }
    
}


