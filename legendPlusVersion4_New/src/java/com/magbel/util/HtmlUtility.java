// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HtmlUtilily.java

package com.magbel.util;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import legend.ConnectionClass;
import legend.UniqueBean;
import magma.net.vao.Category;

import com.magbel.legend.bus.Report;



public class HtmlUtility{
	com.magbel.util.DatetimeFormat df;
    public HtmlUtility(){   
    	df = new com.magbel.util.DatetimeFormat();
    }

    public String getYesNoForCombo(String selected) {

        String html;
        html = "";
        String id = "";
        if (selected == null) {
            selected = "";
        }

        html = html + "<option " +
               ((selected != null) && (selected.equals("N")) ?
                " selected='true' " : "") +
               " value='N' >No</option> " +
               "<option  " +
               ((selected != null) && (selected.equals("Y")) ?
                " selected='true' " : "") +
               " value='Y'>Yes</option> ";

        return html;
    }
 
    public String getResources(String selected, String query) {

        StringBuilder html = new StringBuilder();
        String id = "";

        if (selected == null || selected.equalsIgnoreCase("null")) {
            selected = "ALL"; // Change this to match your default <option value="ALL">
        }

        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement mps = mcon.prepareStatement(query);
        	ResultSet mrs = mps.executeQuery();

            while (mrs.next()) {
                id = mrs.getString(1);
                String label = mrs.getString(2);

                String selectedAttr = (id != null && id.trim().equalsIgnoreCase(selected.trim())) ? " selected" : "";

                html.append("<option value='")
                    .append(id)
                    .append("'")
                    .append(selectedAttr)
                    .append(">")
                    .append(label)
                    .append("</option>\n");
            }

        } catch (Exception ee) {
            System.out.println("WARN HtmlUtil.getResources error: " + ee);
        } 

        return html.toString();
    }

    
    public String getResources(int selected, String query) {

        String html;

        html = "";
        String id = "";
 //       System.out.println("getResources Parameter query:->>"+query);
//        System.out.println("getResources Parameter selected:->>"+selected);
/*        if (selected == null || selected.equals("null")) {
            selected = "0";
        }*/
        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
            PreparedStatement mps = mcon.prepareStatement(query);

            for (ResultSet mrs = mps.executeQuery(); mrs.next(); ) {

                id = mrs.getString(1);
                html = html + "<option  " +
                       ((id != null) && id.equals(selected) ?
                        " selected='true' " : "") +
                       " value='" + id + "'>" +
                       mrs.getString(2) + "</option> ";
            }
        } catch (Exception ee) {
            System.out.println("WARN This is the error:HtmlUtil:->>" + ee);
        } 
        return html;
    }


    public String getUnique(String epostId) {
        return epostId +
                (new SimpleDateFormat("yyyyMMddhhmmSSSS")).format(new Date()) +
                removeNdot("" + Math.random() * 10000000D);
    }

    private String removeNdot(String tValue) {
        String decimalPart = "";
        String strValue = tValue;
        if (strValue.lastIndexOf(".") != -1) {
            decimalPart = strValue.substring(strValue.lastIndexOf(".") + 1);
            if (decimalPart.length() == 1) {
                decimalPart = decimalPart + "0";
            }
            strValue = strValue.substring(0, strValue.lastIndexOf(".")) +
                       decimalPart;
        } else {
            strValue = strValue + "00";
        }
        return strValue;
    }

    public String findObject(String query)
    {
    	//System.out.println("====findObject query=====  "+query);

        String found = null;

        String finder = "UNKNOWN";

        double sequence = 0.00d;
        try {

        	Connection Con2 = new DataConnect("legendPlus").getConnection();
        	PreparedStatement Stat = Con2.prepareStatement(query);
        	ResultSet result = Stat.executeQuery();

            while (result.next()) {
                finder = result.getString(1);
            }
        } catch (Exception ee2) {
            System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
            ee2.printStackTrace();
        }

        return finder;
    }

    public String findObject(String query,String filter)
    {
    	//System.out.println("====findObject query=====  "+query);

        String found = null;

        String finder = "UNKNOWN";

        double sequence = 0.00d;
        try {

        	Connection Con2 = new DataConnect("legendPlus").getConnection();
        	PreparedStatement Stat = Con2.prepareStatement(query);
    		Stat.setString(1, filter);
    		ResultSet result = Stat.executeQuery();
            while (result.next()) {
                finder = result.getString(1);
            }
        } catch (Exception ee2) {
            System.out.println("WARN:ERROR OBTAINING OBJ ====--> " + ee2);
            ee2.printStackTrace();
        } 

        return finder;
    }

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
            System.out.println("WANR: Error closing connection >>" + e);
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
            System.out.println("WANR: Error closing connection >>" + e);
        }
    }




/*
    public int confirmUnique(String query) {
        int count =0;

        String html ="";
        Connection mcon =null;
        PreparedStatement mps =null;
        ResultSet mrs =null;

        System.out.println("================" + "i am in confirmUnique method" + "================");
        //System.out.println("================" + "the value of component is "+component + "================");
        try {
            mcon = (new DataConnect("legendPlus")).getConnection();
            mps = mcon.prepareStatement(query);

            mrs = mps.executeQuery();
            while(mrs.next()){
            count = mrs.getInt(1);
            }

        } catch (Exception ee) {
            System.out.println("WARN This is the error from confirmUnique():HtmlUtil:->" + ee);
        } finally {
            closeConnection(mcon, mps, mrs);
        }

        System.out.println("================"+"the value of count is " + count +"================");
        //if(count >= 1){
        //html ="window.alert('Asset with same No/Code already exist')" + component + ".value =''";
       // html ="<script type='text/javascript'>window.alert('Asset with same No/Code already exist');</script>" ;

       // }


        return count;

    }
*/

    public String[] getArrayList(){
//ArrayList al = new ArrayList();
        String[] al=null;

try{
String query = "SELECT bar_code,lpo FROM am_asset" ;
Connection mcon = (new DataConnect("legendPlus")).getConnection();
PreparedStatement mps = mcon.prepareStatement(query);

ResultSet rs = mps.executeQuery();

int i = 0;
while(rs.next()){
//UniqueBean ub = new UniqueBean();
 //ub.setBar_code(rs.getString("bar_code"));
//ub.setLpo(rs.getString("lpo"));


//al.add(ub);
//    System.out.println("the count for i is <<<<<<<<<" + ++i);
} //while(rs.next())

}//try

catch(Exception e){
e.printStackTrace();
}//catch
// System.out.println("the size of arraylist is ======="+ al.size() );
return al;
}//

public void insToAm_Invoice_No(String assetID,String lpo,String invoiceNo,String TransType)
    {


        String found = null;

        String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,create_date) values (?,?,?,?,?)";
 
             try
             {
            	 Connection Con2 = new DataConnect("legendPlus").getConnection();
            	 PreparedStatement Stat = Con2.prepareStatement(query);
            Stat.setString(1, assetID);
            Stat.setString(2, lpo);
            Stat.setString(3, invoiceNo);
            Stat.setString(4, TransType);
            Stat.setString(5, String.valueOf(df.dateConvert(new java.util.Date())));
            
            Stat.execute();

        } catch (Exception ee2) {
            System.out.println("WARN:ERROR insToAm_Invoice_No  --> " + ee2);
            ee2.printStackTrace();
        } 

        }

public void insGrpToAm_Invoice_No(String assetID,String lpo,String invoiceNo,String TransType,String grpID)
    {

        String found = null;

        String query="Insert into Am_Invoice_no (asset_id,lpo,invoice_no,trans_type,group_id,create_date) values (?,?,?,?,?,?)";

             try
             {
            	 Connection Con2 = new DataConnect("legendPlus").getConnection();
            	 PreparedStatement Stat = Con2.prepareStatement(query);
            Stat.setString(1, assetID);
            Stat.setString(2, lpo);
            Stat.setString(3, invoiceNo);
            Stat.setString(4, TransType);
            Stat.setString(5, grpID);
            Stat.setString(6, String.valueOf(df.dateConvert(new java.util.Date())));
            Stat.execute();

        } catch (Exception ee2) {
            System.out.println("WARN:ERROR insGrpToAm_Invoice_No  --> " + ee2);
            ee2.printStackTrace();
        } 

        }

public String getResources(String selected, String query,int size) {

        String html;

        html = "";
        String id = "";

        if (selected == null || selected.equals("null")) {
            selected = "0";
        }
        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement mps = mcon.prepareStatement(query);

            for (ResultSet mrs = mps.executeQuery(); mrs.next(); ) {

                id = mrs.getString(1);
                html = html + "<option  " +
                       ((id != null) && id.equals(selected) ?
                        " selected='true' " : "") +
                       " value='" + id + "'>" +
                       getDescription(mrs.getString(2),size) + "</option> ";
            }
        } catch (Exception ee) {
            System.out.println("WARN This is the error:HtmlUtil: 2 --->" + ee);
        }

        return html;
    }

private String getDescription(String description,int reqSize){
String descrip="";
if(description != null && (description.length() >reqSize) ){
    descrip = description.substring(0, reqSize-3);
    descrip = descrip+"...";
}
else descrip=description;

return descrip;
}


public ArrayList findCategoryItems(String category_id, String category_code,String status)
				{
//					System.out.println("============= Parameters Sent ===========");
//					System.out.println("category_id >>>>>>>>>>> " + category_id);
//					System.out.println("category_code >>>>>>>>>>> " + category_code);
//					System.out.println("status >>>>>>>>>>> " + status);

				    Category category = null;
				    ArrayList collection = new ArrayList();

				    String FINDER_QUERY = "Select itemCode,isInventory,itemName from am_ad_categoryItems where category_id=?" +
				    		"  and category_code=? and status=?";

				    try {
				    	Connection con = new DataConnect("legendPlus").getConnection();
				    	PreparedStatement ps = con.prepareStatement(FINDER_QUERY);
				        ps.setString(1, category_id);
				        ps.setString(2, category_code);
				        ps.setString(3, status);
				        ResultSet rs = ps.executeQuery();

				        while (rs.next())
				        {
				        	category = new Category();
				        	category.setCode(rs.getString("itemCode"));
				        	category.setInventoryItem(rs.getString("isInventory"));
				        	category.setName(rs.getString("itemName"));
				            collection.add(category);
				        }
				    } catch (Exception ex) {
				        System.out.println("WARNING: cannot fetch [findCategoryItems]->" +  ex.getMessage());
				    } 

				    return collection;
				}


 public String getResourcesCheckAndDropText(String selectChk, String query) {

        String html;

        html = "";
        String id = "";
        String col ="";
        String htmlD = "";
        String operand = "";
        String htmlN = "";
        String htmlC = "";
        String op = "op";
        String opr = "";

        String temp = "";
        Report rep = new Report();
        String html1 = "";
        String k = "temp";
        String fDate = "fromDate";
        String tDate = "toDate";

 //       System.out.println("getResourcesCheckAndDropText2 query: "+query);

        if (selectChk == null || selectChk.equals("null")) {
            selectChk = "0";
        }





        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement mps = mcon.prepareStatement(query);

           // for (mrs = mps.executeQuery(); mrs.next(); ) {
        	ResultSet mrs = mps.executeQuery();
            while(mrs.next()){


                id = mrs.getString(1);
              col =  mrs.getString(2);
              operand = mrs.getString(3);
//              System.out.println("<<<<<<operand: "+operand+"    <<<<<<<col: "+col+"    <<<<<<Id: "+id);

opr = op+id;
//System.out.println("<<<<<<<<opr: "+opr+"    <<<<<<operand: "+operand+"    <<<<<<<col: "+col+"    <<<<<<Id: "+id);
//getResources(selected, String query)
    htmlC = "<select name='"+opr+"' >"
      // +"<option name=empty value=empty>Select An Operand</option>"
       +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='=' name = A >=</option> "
    +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='<>' name = B ><></option> "
    +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='IN' name = C >IN</option> ";

//    System.out.println("<<<<<<<<htmlC: "+htmlC);

 htmlN = "<select name='"+opr+"' >"
        //   +"<option name=empty value=empty>Select An Operand</option>"
           +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='=' = A >=</option> "
        +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='>' name = B > > </option> "
 +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
    " value='<' = > < </option> ";
 //System.out.println("<<<<<<<<htmlN: "+htmlN);


htmlD = "<select name='"+opr+"'>"
        //   +"<option name=empty value=empty>Select An Operand</option>"
           +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='BETWEEN' name = A >BETWEEN</option> "
       /* +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='>' name = B > > </option> "
 +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='<' = C > < </option> "*/ ;
//System.out.println("<<<<<<<<htmlD: "+htmlD);

              String drop = "";
                 /* if(operand.equals("C")){ drop = htmlC; }
                  if(operand.equals("N")){ drop = htmlN;  }
                  if(operand.equals("D")){ drop = htmlD; }
                  */
              if(operand.equals("C")){ drop = htmlC; }
                  if(operand.equals("N")){ drop = htmlN;  }
                  if(operand.equals("D")){ drop = htmlD; }


    if(rep.isNumeric(id,"N")){

    html1 = "<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=text name='"+k+id+"' id='"+k+id+"' onBlur='getFormatedAmount(this.form."+k+id+");checkValidNumber(this.name)' value =''if(selectTo!=null)"+

    "{out.print('enabled');}else {out.print('disabled');}</td>";

    }else if(rep.isNumeric(id,"D")){

    html1 = "<td>From&nbsp;<input name='"+fDate+id+"' type=text class=label id='"+fDate+id+"' >"
            + " To &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input name='"+tDate+id+"' type=text class=label id='"+tDate+id+"' >   <font color=red>Format:YYYY-MM-YY</font></td>";

//    System.out.println("See the  html Date format Output:->>>" + html);
    }

    else{

     // String dropDown = "SELECT col,getColDes() FROM COL_FILTER WHERE COLUMN_NAME=col  ";
          // html1 = getResources(String selected, String query);


          html1 = "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name='"+k+id+"' type=text class=label id='"+k+id+"' ></td>";

      //<select name=""></select>
          //getResources(ad.getLocation(),"SELECT LOCATION_ID,LOCATION FROM am_gb_location ORDER BY LOCATION ")

    }





           html = html + "<tr><td> <input name ="+ selectChk +
             " value='"+ id +"' type = checkbox checked=checked> </td> <td>" +
                       mrs.getString(2)+"</td>&nbsp; <td> </td><td>"+drop+"</td> <td>&nbsp;&nbsp;&nbsp; </td>"+html1+"</tr>  ";
           //for(int i = 1 i< )

            }
 //           System.out.println("See the  html Output:->>>" + html);
        } catch (Exception ee) {
            System.out.println("WARN getResourcesCheckAndDrop error:HtmlUtil:->" + ee.getMessage());
        }

        return html;
    }


  public String getResourcesCheckAndDropText2(String selectChk, String query) {

        String html;

        html = "";
        String id = "";
        String col ="";
        String htmlD = "";
        String operand = "";
        String htmlN = "";
        String htmlC = "";
        String op = "op";
        String opr = "";

        String temp = "";
        Report rep = new Report();
        String html1 = "";
        String k = "temp";
        String fDate = "fromDate";
        String tDate = "toDate";



        if (selectChk == null || selectChk.equals("null")) {
            selectChk = "0";
        }





        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement mps = mcon.prepareStatement(query);

           // for (mrs = mps.executeQuery(); mrs.next(); ) {
        	ResultSet mrs = mps.executeQuery();
            while(mrs.next()){


                id = mrs.getString(1);
              col =  mrs.getString(2);
              operand = mrs.getString(3);

opr = op+id;
//getResources(selected, String query)
    htmlC = "<select name='"+opr+"'>"
      // +"<option name=empty value=empty>Select An Operand</option>"
       +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='=' name = A >=</option> "
    +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='<>' name = B ><></option> ";



 htmlN = "<select name='"+opr+"'>"
        //   +"<option name=empty value=empty>Select An Operand</option>"
           +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='=' = A >=</option> "
        +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='>' name = B > > </option> "
 +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
    " value='<' = > < </option> ";



htmlD = "<select name='"+opr+"'>"
        //   +"<option name=empty value=empty>Select An Operand</option>"
           +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='BETWEEN' name = A >BETWEEN</option> "
       /* +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
       " value='>' name = B > > </option> "
 +"<option" +((opr != null) && opr.equals(selectChk)?" selected='true' " : "") +
   " value='<' = C > < </option> "*/ ;


              String drop = "";
                 /* if(operand.equals("C")){ drop = htmlC; }
                  if(operand.equals("N")){ drop = htmlN;  }
                  if(operand.equals("D")){ drop = htmlD; }
                  */
              if(operand.equals("C")){ drop = htmlC; }
                  if(operand.equals("N")){ drop = htmlN;  }
                  if(operand.equals("D")){ drop = htmlD; }


    if(rep.isNumeric(id,"N")){

    html1 = "<td> <input type=text name='"+k+id+"' id='"+k+id+"' onBlur='getFormatedAmount(this.form."+k+id+");checkValidNumber(this.name)' value =''if(selectTo!=null)"+

    "{out.print('enabled');}else {out.print('disabled');}</td>";

    }else if(rep.isNumeric(id,"D")){

    html1 = "<td> <input name='"+k+id+"' type=hidden class=label id='"+k+id+"' >"
            + "<input name='"+fDate+id+"' type=text class=label id='"+fDate+id+"' >&nbsp; AND &nbsp;"
            + "<input name='"+tDate+id+"' type=text class=label id='"+tDate+id+"' ></td>";


    }

    else{
String peg = "To";
     String dropQuery = "   SELECT  "+col+",  "+getColDesc(col)+"  FROM  "+getTaName(col)+"  ";
          html1 = "<td> <select name='"+k+id+"'>"+getResources("selected",dropQuery)+" </select></td>";

//System.out.println("See the  dropQuery Output:->>>" + dropQuery);
//System.out.println("See the  html1 Output:->>>" + html1);
         // html1 = "<td><input name='"+k+id+"' type=text class=label id='"+k+id+"' ></td>";

      //<select name=""></select>
          //getResources(ad.getLocation(),"SELECT LOCATION_ID,LOCATION FROM am_gb_location ORDER BY LOCATION ")

    }
     // System.out.println("See the  Numeric Format /Text Output:->>>" + html1);




           html = html + "<tr><td> <input name ="+ selectChk +
             " value='"+ id +"' type = checkbox> </td> <td>" +
                       mrs.getString(2)+"</td>&nbsp; <td> </td><td>"+drop+"</td> <td>&nbsp;&nbsp;&nbsp; </td>"+html1+"</tr>  ";
           //for(int i = 1 i< )

            }
        } catch (Exception ee) {
            System.out.println("WARN getResourcesCheckAndDrop error:HtmlUtil:->" + ee.getMessage());
        } 

        return html;
    }



          public String getTaName(String col){

      String desc = "";
      String filter = "To";

       String FINDER_QUERY = "SELECT TABLE_NAMES from COL_FILTER WHERE COLUMN_NAME =? AND PEG=?";

        try {
        	Connection con  = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, col);
             ps.setString(2, filter);
             ResultSet rs = ps.executeQuery();

      while (rs.next()) {

                desc = rs.getString(1);
   }


        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch OPERAND from COL_LOOK_UP->" +
                    ex.getMessage());
        } 

        return desc;
}

  public String getColDesc(String col){

      String desc = "";
      String filter = "To";

       String FINDER_QUERY = "SELECT COLUMN_DESC from COL_FILTER WHERE COLUMN_NAME =? AND PEG=?";

        try {
        	Connection con  = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, col);
             ps.setString(2, filter);
             ResultSet rs = ps.executeQuery();

      while (rs.next()) {

                desc = rs.getString(1);
   }



        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch OPERAND from COL_LOOK_UP->" +
                    ex.getMessage());
        }

        return desc;
}



  public String getResourcesCheck(String select, String query) {

        String html;

        html = "";
        String id = "";

        if (select == null || select.equals("null")) {
            select = "0";
        }
        try {
        	Connection mcon = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement mps = mcon.prepareStatement(query);

            for (ResultSet mrs = mps.executeQuery(); mrs.next(); ) {

 
                id = mrs.getString(1);
                /*html = html + "<input name = select" +
                       ((select != null) && !select.equals("") ?
                        " checked ='true'" : "") +
                       " value='" + id + "' type = checkbox>" +
                       mrs.getString(2);
              --------------------------------------------
                 html = html + "<tr><td> <input name = select" +
             " value='" + id + "' type = checkbox> </td> <td>" +
                       mrs.getString(2)+"</td> </tr>  ";


                 */



           html = html + "<tr><td> <input name ="+ select +
             " value='" + id + "' type = checkbox> </td> <td>" +
                       mrs.getString(2)+"</td> </tr>  ";

            }
        } catch (Exception ee) {
            System.out.println("WARN Chrck box error:HtmlUtil:->" + ee);
        } 
        
        return html;
    }







public String getOperand(String col){

      String op = "";

       String FINDER_QUERY = "SELECT DISTINCT operand from COL_FILTER WHERE COLUMN_NAME =?";

        try {
        	Connection con  = (new DataConnect("legendPlus")).getConnection();
        	PreparedStatement ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, col);
            ResultSet rs = ps.executeQuery();

      while (rs.next()) {

                op = rs.getString(1);
   }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch OPERAND from COL_LOOK_UP->" +
                    ex.getMessage());
        } 

        return op;
}

public String getCodeName(String query) {
	String result = "";

	int rs1 = 0;

//	System.out.println("query===>> "+query);
	
	try {
		String validate = query.substring(0, 6);
//		con = getConnection();
		Connection con = (new DataConnect("legendPlus")).getConnection();
		PreparedStatement ps = con.prepareStatement(query);
		if(!validate.equalsIgnoreCase("UPDATE")){	
			ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getString(1) == null ? "" : rs.getString(1);
		}
		}
		else{
			rs1 = ps.executeUpdate();
		}
	//	System.out.println("<<<<<<<<<<result: "+result);
        try
        {
 /*           if(con != null)
            {
            	con.close();  
            }  */ 
        }
        
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }			
	} catch (Exception er) {
		System.out.println("Error in " + this.getClass().getName()
				+ "- getCodeName()... ->" + er.getMessage());
		er.printStackTrace();
	} 
	return result;
}     


public String getCodeName(String query,String userId) {
	String result = "";

	int rs1 = 0;

//	System.out.println("query===>> "+query);
	
	try {
		String validate = query.substring(0, 6);
//		con = getConnection();
		Connection con = (new DataConnect("legendPlus")).getConnection();
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, userId);
		if(!validate.equalsIgnoreCase("UPDATE")){	
			ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getString(1) == null ? "" : rs.getString(1);
		}
		}
		else{
			rs1 = ps.executeUpdate();
		}
	//	System.out.println("<<<<<<<<<<result: "+result);
        try
        {
 /*           if(con != null)
            {
            	con.close();  
            }  */ 
        }
        
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }			
	} catch (Exception er) {
		System.out.println("Error in " + this.getClass().getName()
				+ "- getCodeName()... ->" + er.getMessage());
		er.printStackTrace();
	}  
	return result;
}     


public void updateAssetStatusChange(String query_r){

try {
	Connection con = (new DataConnect("legendPlus")).getConnection();
	PreparedStatement ps = con.prepareStatement(query_r);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
        }


}//updateAssetStatus()


public void updateAssetStatusChange(String query_r,Timestamp date,int tranId){

try {
	Connection con = (new DataConnect("legendPlus")).getConnection();
	PreparedStatement ps = con.prepareStatement(query_r.toString());
	  ps.setTimestamp(1, date);
	  ps.setInt(2, tranId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
        } 


}//updateAssetStatus()

public void updateAssetStatusChange(String query_r,String naration,Timestamp date,int tranId){

try {
	Connection con = (new DataConnect("legendPlus")).getConnection();
	PreparedStatement ps = con.prepareStatement(query_r.toString());
	  ps.setString(1, naration);
	  ps.setTimestamp(2, date);
	  ps.setInt(3, tranId);
           int i =ps.executeUpdate();
            //ps.execute();

        } catch (Exception ex) {

            System.out.println("HtmlUtility: updateAssetStatusChange()>>>>>" + ex);
        } 


}//updateAssetStatus()

public String getResources(int selected) {

    String html;

    html = "";
    String id = "";
//       System.out.println("getResources Parameter query:->>"+query);
//    System.out.println("getResources Parameter selected:->>"+selected);
/*        if (selected == null || selected.equals("null")) {
        selected = "0";
    }*/
    String query = "SELECT BRANCH_ID,BRANCH_NAME FROM am_ad_BRANCH where brnch_id = ? ORDER BY BRANCH_NAME";
    try {
    	Connection mcon = (new DataConnect("legendPlus")).getConnection();
    	PreparedStatement mps = mcon.prepareStatement(query);

        for (ResultSet mrs = mps.executeQuery(); mrs.next(); ) {

            id = mrs.getString(1);
            html = html + "<option  " +
                   ((id != null) && id.equals(selected) ?
                    " selected='true' " : "") +
                   " value='" + id + "'>" +
                   mrs.getString(2) + "</option> ";
        }
    } catch (Exception ee) {
        System.out.println("WARN This is the error:HtmlUtil:->>" + ee);
    } 
    return html;
}


public String findObjectParam(String id)
{
	//System.out.println("====findObject query=====  "+query);

    String found = null;

    String finder = "UNKNOWN";
    String query = "select dept_code from am_gb_user where user_id = ? ";
    double sequence = 0.00d;
    try {

    	Connection Con2 = new DataConnect("legendPlus").getConnection();
    	PreparedStatement Stat = Con2.prepareStatement(query);
        Stat.setString(1, id);
        ResultSet result = Stat.executeQuery();

        while (result.next()) {
            finder = result.getString(1);
        }
    } catch (Exception ee2) {
        System.out.println("WARN:ERROR OBTAINING OBJ --> " + ee2);
        ee2.printStackTrace();
    } 
    return finder;
}

public String getUserListResources(String selected, String query) {

    StringBuilder html = new StringBuilder();
    String id = "";

    if (selected == null || selected.equalsIgnoreCase("null")) {
        selected = "ALL"; // Change this to match your default <option value="ALL">
    }

    try {
    	Connection mcon = (new DataConnect("otherDataSource")).getOtherConnection();
    	PreparedStatement mps = mcon.prepareStatement(query);
    	ResultSet mrs = mps.executeQuery();
//        System.out.println("getUserListResources Parameter query:->>"+query);
//        System.out.println("getUserListResources Parameter selected:->>"+selected);
        while (mrs.next()) {
            id = mrs.getString(1);
            String label = mrs.getString(2);
//            System.out.println("getUserListResources Id: "+id+"     label: "+label);
            String selectedAttr = (id != null && id.trim().equalsIgnoreCase(selected.trim())) ? " selected" : "";

            html.append("<option value='")
                .append(id)
                .append("'")
                .append(selectedAttr)
                .append(">")
                .append(label)
                .append("</option>\n");
        }

    } catch (Exception ee) {
        System.out.println("WARN HtmlUtil.getResources error: " + ee);
    } 

    return html.toString();
}


}
