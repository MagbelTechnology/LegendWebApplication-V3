/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package legend;

//import legend.admin.objects.Company;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;

import java.sql.SQLException;

import magma.net.dao.MagmaDBConnection;

//import java.beans.*;
//import java.io.Serializable;

/**
 *  
 * @author Ganiyu
 */
public class ExportLegacyBean extends ConnectionClass {

    private String processing_status;
    private String legacyExportStatus;
    private MagmaDBConnection dbConnection;

public ExportLegacyBean()throws Exception{
    super();
    
}

    /**
     * @return the processing_status
     */
    public boolean export(String ThirdPartyLabel) {  
        boolean outcome = false;
         try{  ConnectionClass cc = new ConnectionClass();
        processing_status = cc.getProcessingStatus();
        legacyExportStatus = cc.getLegacyExportStatus();
        System.out.println("+++++++++ legacyExportStatus: "+legacyExportStatus);
        if(legacyExportStatus.equals("0")) {
        	System.out.println("++++++++++++++++++++ just About Populate Depreciation Export Transactions");
        	outcome = cc.populateFinacleTemp(Integer.parseInt(processing_status),ThirdPartyLabel);
        }
       // System.out.println("++++++++++++++++++++ just before populating the finacle temp");
         }catch(Exception e){e.printStackTrace();}
        
        return outcome;
    }


/*
    public ArrayList getArrayList(){
 ArrayList al = new ArrayList();
 ExportComponentBean ecb = new ExportComponentBean();

 try{
  String query = "SELECT unique_id,dr_acct, cr_acct, amount, narration, narration2 FROM finacle_ext" ;

  ResultSet rs = getStatement().executeQuery(query);
//ResultSetMetaData rsmd = rs.getMetaData();
//ecb.setIdColumnName(rsmd.getColumnName(1));
//ecb.setDrColumnName(rsmd.getColumnName(2));

//ecb.setCrColumnName(rsmd.getColumnName(3));
//ecb.setCrColumnName(rsmd.getColumnName(4));
//ecb.setNarrationColumnName(rsmd.getColumnName(5));
//ecb.setNarration2ColumnName(rsmd.getColumnName(6));

//al.add(ecb);

int i = 0;
if(rs.next()){
ecb.setId(rs.getInt("unique_id"));
ecb.setDr_acct(rs.getString("dr_acct"));
ecb.setCr_acct(rs.getString("cr_acct"));
ecb.setAmount(rs.getDouble("amount"));
ecb.setNarration(rs.getString("narration"));
ecb.setNarration2(rs.getString("narration2"));
al.add(ecb);
    System.out.println("the count for i is <<<<<<<<<" + ++i);
}
freeResource();
 }catch(Exception e){e.printStackTrace();}

    System.out.println("the size of arraylist is ======="+ al.size() );
return al;
}


*/
 

public ArrayList getArrayList(){
	dbConnection = new MagmaDBConnection();
ArrayList al = new ArrayList();
// ExportComponentBean ecb = new ExportComponentBean();
String query = "SELECT unique_id,BRANCH_CODE,SBU_CODE,dr_acct, cr_acct, amount, narration, narration2,record_processed,value_date FROM finacle_ext where amount > 0" ;
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
try{ 
    con = dbConnection.getConnection("legendPlus");
    ps = con.prepareStatement(query);
    rs = ps.executeQuery();
//ResultSet rs = getStatement().executeQuery(query);
int i = 0;
while(rs.next()){
ExportComponentBean ecb = new ExportComponentBean();
String res = rs.getString("record_processed");
//System.out.println("ArrayList==res: "+rs.getString("record_processed"));
if(res.equalsIgnoreCase("N") ){
//System.out.println("ArrayList==unique_id: "+rs.getInt("unique_id")+"  record_processed: "+rs.getString("record_processed")+"  Amount: "+rs.getDouble("amount")+"  SBU_CODE: "+rs.getString("SBU_CODE"));
ecb.setRecord_processed(rs.getString("record_processed"));
ecb.setId(rs.getInt("unique_id"));
ecb.setBranchCode(rs.getString("BRANCH_CODE"));
ecb.setSbuCode(rs.getString("SBU_CODE"));
ecb.setDr_acct(rs.getString("dr_acct"));
ecb.setCr_acct(rs.getString("cr_acct"));
ecb.setAmount(rs.getDouble("amount"));
ecb.setNarration(rs.getString("narration"));
ecb.setNarration2(rs.getString("narration2"));
ecb.setValueDate(rs.getString("value_date"));

al.add(ecb);
}
    //System.out.println("the count for i is <<<<<<<<<" + ++i);
} //while(rs.next())
dbConnection.closeConnection(con, ps, rs);

}//try


catch(Exception e){
e.printStackTrace();
dbConnection.closeConnection(con, ps, rs);
}//catch

// System.out.println("the size of arraylist is ======="+ al.size() );
return al;
}//

    /**
     * @param processing_status the processing_status to set
     */
    public void setProcessing_status(String processing_status) {
        this.processing_status = processing_status;
    }



//Transfered from jsp
    public int getIndividualTransaction(String sbuCode,String dr_acct,String cr_acct,double amount,String narration, String narration2,String value_date,String processed_flg) {
    	int x = -1;		
 /*   	String query3 = "";
//    String query3 = "insert into custom.fxd_asset(dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) values(?,?,?,?,?,?,?)";
//    String query3 = "insert into custom.fxd_asset(dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) "
//    		+ "values('"+dr_acct+"','"+cr_acct+"',"+amount+",'"+narration+"','"+narration2+"','"+value_date+"','"+processed_flg+"')";
//    System.out.print("The value of the amount is ---------" +amount+"   SBU CODE: "+sbuCode);
//    System.out.print("The query3 value is ---------" +query3);
    try{   
//    	System.out.println("<<<<<< value Date before restructuring  >>>>>>> " + value_date);	
    	if(value_date != null){
    		String dd = value_date.substring(0,2);
    		String mm = value_date.substring(3,5);
    		String yyyy = value_date.substring(6,10);
    		value_date = "TO_DATE('"+yyyy+"-"+mm+"-"+dd+"', "+"'YYYY-MM-DD')";	 		
    		
 //   		System.out.println("<<<<<< value Date after restructuring >>>>>>> " + value_date);	
    		}else{
    			value_date = "";
    		}	
        query3 = "insert into custom.fxd_asset(sbu_code,dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) "
        		+ "values('"+sbuCode+"','"+dr_acct+"','"+cr_acct+"',"+amount+",'"+narration+"','"+narration2+"',"+value_date+",'"+processed_flg+"')";
        System.out.print("The query3 value after computation is ---------" +query3);
    ConnectionClass connection = new ConnectionClass();
    PreparedStatement ps = connection.getPreparedStatementOracle(query3);
    */
    /*
    ps.setString(1,dr_acct );
    ps.setString(2,cr_acct );  
    ps.setDouble(3,amount);
    ps.setString(4,narration);
    ps.setString(5,narration2);
    ps.setString(6, value_date);
    ps.setString(7,processed_flg);
    */
//     x = ps.executeUpdate();
//     System.out.print("The value of x is ------------==================............." +x);

        String query3 = "insert into custom.fxd_asset(sbu_code,dr_acct,cr_acct,amount,narration,narration2,value_date,processed_flg) values(?,?,?,?,?,?,?,?)";

        try{
        	Connection conn = null;
        ConnectionClass connection = new ConnectionClass();
        	conn = connection.getOracleConnection();
        //PreparedStatement ps = conn.getPreparedStatementOracle(query3);
        	 PreparedStatement ps = conn.prepareStatement(query3);
        System.out.print("The value of sbuCode is ----======....." +sbuCode);
        ps.setString(1,sbuCode);
        System.out.print("The value of dr_acct is ----======....." +dr_acct);
        ps.setString(2,dr_acct);
        System.out.print("The value of cr_acct is ----======....." +cr_acct);
        ps.setString(3,cr_acct);
        System.out.print("The value of amount is ----======....." +amount);
        ps.setDouble(4,amount);
        System.out.print("The value of narration is ----======....." +narration);
        ps.setString(5,narration);
        System.out.print("The value of narration2 is ----======....." +narration2);
        ps.setString(6,narration2);
        System.out.print("The value of value_date is ----======....." +value_date);
        ps.setString(7, value_date);
        ps.setString(8,processed_flg);
         x = ps.executeUpdate();
         conn.close();
   // connection.freeResource();
    }//try
    catch(Exception e){ 
    e.getMessage();
    }
        //catch
    System.out.print("The value of x is ------------==================............." + x);
    return x;
    }	//		getIndividualTransaction()	

    public void updateRecordProcessed(int id) throws SQLException{
    String r = "Y";
    System.out.print("The value of id in updateRecordProcessed is ------------==================............." + id);
    //ConnectionClass con= null;
    PreparedStatement ps = null;
    Connection con= null;
    try{
     //con = new ConnectionClass();
    	 con = getConnection();
    	 ps = con.prepareStatement("update finacle_ext set record_processed = '"+ r +"' where unique_id = '" + id + "'");
    //int i = con.getStatement().executeUpdate("update finacle_ext set record_processed = '"+ r +"' where unique_id = '" + id + "'" );
    	 int i = ps.executeUpdate();
    }catch(Exception e){
    e.getMessage();
    }finally{
    	//con.freeResource();
    	con.close();
    	}

    }//updateRecordProcessed
    public void updateCompanySetup(String status) throws SQLException{
        PreparedStatement ps = null;
        Connection con= null;
        try{
         con = getConnection();
         ps = con.prepareStatement("update am_gb_company set processing_status = '"+status+"'" );
//        int i = con.getStatement().executeUpdate("update am_gb_company set processing_status = '"+status+"'" );
         int i = ps.executeUpdate();
        }catch(Exception e){
        e.getMessage();
        }finally{
//        	con.freeResource();
        	con.close();
        	}

        }//updateRecordProcessed
    
}//ExportLegacyBean class;
