package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
//import javax.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.*;



import com.magbel.legend.bus.Report;

import com.magbel.dao.PersistenceServiceDAO;

public class FormatReport extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream outp = null;
        FileOutputStream out;
        //PrintStream out = new PrintStream();
        HttpSession session = request.getSession();
        //String[] tableId = request.getParameterValues("select");
        String[] tableId = (String[]) session.getAttribute("tableId");
        // String alias = (String)session.getAttribute("alias");
        // String myTable = (String)session.getAttribute("myTable");
        String myTable = request.getParameter("myTable");
        int count = 0;
        if (tableId == null) {
            session.setAttribute("tableId", tableId);
            response.sendRedirect("reportFilter.jsp");
            count = 0;
        } else {
            count = tableId.length;
        }

        Report rep = new Report();

        String operation = request.getParameter("operation");
        String[] aliasIdFrom = request.getParameterValues("selectFrom");
        //String[] aliasIdFrom = request.getParameterValues("selectTo");



        //System.out.println("aliasIdFrom @ redirect <><><>:::" + aliasIdFrom);

        if (aliasIdFrom == null ) {

            String status = null;

 //           System.out.println("aliasIdFrom @ aliasIdFrom <><><>:::" + aliasIdFrom);
            session.setAttribute("tableId", tableId);

            response.sendRedirect("connect.jsp?filterStatus=" + status);

        } else {



            int aliasCountF = 0;

            String aliasQuery = "";

            if (aliasIdFrom == null) {
                aliasCountF = 0;
            } else {
                aliasCountF = aliasIdFrom.length;
            }


  //          System.out.print("aliasCountF  >>>" + aliasCountF);






// FORM THE QUERY ALIAS

            String a_delim = "  AND ";
            String operand = "";

            String getTablQuery = request.getParameter("getTabQuery");
            String getColQuery = request.getParameter("getColQuery");
            ArrayList list2 = rep.findByQuery(getColQuery);

            String tabQuery = "SELECT TABLE_NAMES FROM COL_FILTER WHERE ID = ?";
            String colQuery = "SELECT COLUMN_NAME FROM COL_FILTER WHERE ID = ?";
            String  getfromTQuery = "SELECT DISTINCT TABLE_NAMES FROM COL_FILTER WHERE PEG =?";

            getColQuery = getColQuery + "AND COLUMN_NAME= ?";
            getTablQuery = getTablQuery + "AND COLUMN_NAME= ?";

            int toColCount = list2.size() + 1;

            String[] colNameTo = new String[aliasCountF];
            String[] tabNameTo = new String[aliasCountF];
            String[] colNameFrom = new String[toColCount];
            String[] tabNameFrom = new String[toColCount];


            String aliasQuery1 = "";
            String fromCol = "";
            String toCol = "";
            String toTab = "";
            String fromTab = "";
            String join = "  AND   ";


            String findJoin = "";
            String affectedTable = "";
            String findJoinValue = "";

            ArrayList colList = new ArrayList();
            ArrayList tabList = new ArrayList();

            ArrayList colValueList = new ArrayList();
            ArrayList getOp = new ArrayList();

            ArrayList listTab = new ArrayList();
            ArrayList listCol = new ArrayList();


            toColCount = aliasIdFrom.length;

            String[] colNameFrom1 = new String[toColCount];
            String[] tabNameFrom1 = new String[toColCount];
            String holder = "";
            String opr = "";
            String tempValue = "";
            String mainValue = "";
            String frDate = "";
            String TDate = "";



            for (int i = 0; i < toColCount; i++) {

                holder = request.getParameter("temp" + aliasIdFrom[i]);
                holder = (holder==null)? "":holder;
 //               System.out.println("request.getParameter(temp+aliasIdFrom[i]) : " + "temp" + aliasIdFrom[i]);
 //               System.out.println("The holder=>*+++ : " + holder);

                //  if(holder!=null&&(!holder.equalsIgnoreCase(""))){

                colValueList.add(holder);

                // }


            }

            for (int i = 0; i < toColCount; i++) {

                opr = request.getParameter("op" + aliasIdFrom[i]);

  //              System.out.println("request.getParameter(op+aliasIdFrom[i]) : " + "op" + aliasIdFrom[i]);
   //             System.out.println("The holder=>*+++ : " + opr);

                //  if(holder!=null&&(!holder.equalsIgnoreCase(""))){

                getOp.add(opr);

            }


//    System.out.println("Join Count is>>>>> : " + list2.size());

    if (list2.size() < 1) {

//  System.out.println("Processing  report for a single table>>....... ");

        toColCount = aliasIdFrom.length;


        for (int i = 0; i < toColCount; i++) {

            colNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, colQuery);
            tabNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, tabQuery);
            //if (colNameFrom1[i].equals("BRANCH_CODE")) {

 //           System.out.println(" aliasIdFrom[i] is=>>>>> : " + aliasIdFrom[i]);
            //colNameTo = rep.findByIdQuery(aliasIdFrom[i], aliasCountF, colQuery);
            //tabNameTo = rep.findByIdQuery(aliasIdFrom[i], aliasCountF, tabQuery);
            fromCol = rep.findByIdQuery(aliasIdFrom[i], colQuery);
            fromTab = rep.findByIdQuery(aliasIdFrom[i], tabQuery);
            toCol = rep.findByIdQuery(fromCol, getColQuery);
            toTab = rep.findByIdQuery(toCol, getTablQuery);

             mainValue = colValueList.get(i).toString();

            if(rep.isNumeric(aliasIdFrom[i],"N")){
            tempValue = mainValue.replaceAll(",", "");

            }else if(rep.isNumeric(aliasIdFrom[i],"D")){
             frDate = request.getParameter("fromDate" + aliasIdFrom[i]);
              TDate = request.getParameter("toDate"+ aliasIdFrom[i]);
           // tempValue ="  "+ frDate+ "   AND  " +TDate;
               tempValue ="  '"+ frDate+ "'   AND  '" +TDate+ "' ";
 //           System.out.println("The tempValue @ Date =>********** : " + tempValue);
            }else{
            tempValue = "  '"+mainValue+"' ";
            }

  //          System.out.println("The toTab.toCol is=>********** : " + tabNameFrom1[i] + "<>" + colNameFrom1[i]);
  //          System.out.println("My colValueList is=>+++++++ : " + colValueList.get(i).toString());


            findJoinValue = findJoinValue + tabNameFrom1[i] + "." + colNameFrom1[i] +" "+ getOp.get(i).toString() + tempValue;


            if (i != toColCount - 1) {
                findJoinValue = findJoinValue + a_delim;
            }
        }

              aliasQuery = findJoinValue;

      } else {

 //         System.out.println("Processing  report for multiple tables>>....... ");



        affectedTable =request.getParameter("queryTabTo");

String getJoinTab = "select table_names from col_filter where table_names in("+affectedTable+")";
String getJoinCol = "select COLUMN_NAME from col_filter where table_names in("+affectedTable+")";

   listTab = rep.findByQuery(getJoinTab);
    listCol = rep.findByQuery(getJoinCol);

    String fromT = "";
        String status = "From";
        fromT = rep.findByIdQuery(status, getfromTQuery);
for (int i = 0; i < listTab.size(); i++) {

//    System.out.println("The toTab.toCol is=>********** : " + listTab.get(i).toString() + "<..>" + listCol.get(i).toString());
//            System.out.println("The From.toCol is=>********** : " + fromT + "<..>" + listCol.get(i).toString());


            //findJoin = findJoin + fromT +"." + listCol.get(i).toString() + getOp.get(i).toString() + listTab.get(i).toString() + "." + listCol.get(i).toString();
            findJoin = findJoin + fromT +"." + listCol.get(i).toString() + "  =  " + listTab.get(i).toString() + "." + listCol.get(i).toString();

            if (i != listTab.size() - 1) {
                findJoin = findJoin + a_delim;
            }


    }





 //       System.out.println("The findJoin query is=>********** : " + findJoin);


        for (int i = 0; i < toColCount; i++) {

            colNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, colQuery);
            tabNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, tabQuery);
            //if (colNameFrom1[i].equals("BRANCH_CODE")) {

 //           System.out.println(" aliasIdFrom[i] is=>>>>> : " + aliasIdFrom[i]);
            //colNameTo = rep.findByIdQuery(aliasIdFrom[i], aliasCountF, colQuery);
            //tabNameTo = rep.findByIdQuery(aliasIdFrom[i], aliasCountF, tabQuery);
            fromCol = rep.findByIdQuery(aliasIdFrom[i], colQuery);
            fromTab = rep.findByIdQuery(aliasIdFrom[i], tabQuery);
            toCol = rep.findByIdQuery(fromCol, getColQuery);
            toTab = rep.findByIdQuery(toCol, getTablQuery);



            //  if (colList.size()>1) {


 //           System.out.println("The toTab.toCol is=>********** : " + tabNameFrom1[i] + "<>" + colNameFrom1[i]);
            //.out.println("My colValueList is=>+++++++ : " + colValueList.get(i).toString());

            mainValue = colValueList.get(i).toString();
//System.out.println("<<<<<<<<<<<<<<<mainValue: "+mainValue);
            if(rep.isNumeric(aliasIdFrom[i],"N")){
            tempValue = mainValue.replaceAll(",", "");

            }else{
            String oprs = getOp.get(i).toString();  
//            System.out.println("<<<<<<<<<<<<<<<oprs: "+oprs);
            if(oprs.equalsIgnoreCase("IN")){
            	tempValue = "";
            	String[] filtervalue =mainValue.split(",");
            	int fldlength = filtervalue.length;
            	for (int j = 0; j < fldlength; j++) {
            		if(!tempValue.equals("") && (j!= fldlength)){tempValue = "("+tempValue+",";}
            		tempValue = tempValue+"'"+filtervalue[j].trim()+"'";
 //           		System.out.println("<<<fldlength with IN: "+fldlength+"   J: "+j);
            		if(j== fldlength-1){tempValue = tempValue+")";}
            	}
           /*     String filtvalue1 = filtervalue[0];
                String filtvalue2 = filtervalue[1];
                String filtvalue3 = filtervalue[2];
                String filtvalue4 = filtervalue[3];
                String filtvalue5 = filtervalue[4];
                if(filtvalue1!=""){tempValue = tempValue+"'"+filtvalue1+"'";}
                if(filtvalue2!=""){tempValue = tempValue+"'"+filtvalue2+"'";}
                if(filtvalue3!=""){tempValue = tempValue+"'"+filtvalue3+"'";}
                if(filtvalue4!=""){tempValue = tempValue+"'"+filtvalue4+"'";}
                if(filtvalue5!=""){tempValue = tempValue+"'"+filtvalue5+"'";}*/
 //               System.out.println("<<<<<<<<<<<<<<<tempValue with IN: "+tempValue);
            }
            else{
            tempValue = "'"+mainValue+"'"; 
            }
       //     System.out.println("<<<<<<<<<<<<<<<tempValue: "+tempValue);
            }
            findJoinValue = findJoinValue + tabNameFrom1[i] + "." + colNameFrom1[i] + " "+ getOp.get(i).toString() +" "+ tempValue;


            if (i != colValueList.size() - 1) {
                findJoinValue = findJoinValue + a_delim;
            }
        }
    //    System.out.println("The findJoinValue query is=>********** : " + findJoinValue);

        aliasQuery = findJoin + join + findJoinValue;


    }



   //             System.out.println("<<<<<<My aliasQuery String is>>>>> : " + aliasQuery);

                String[] colNameAll = new String[count];
                String[] tabNameAll = new String[count];




                String delim = ",";
                // CONSTRUCT COLUMNS AND ALLIAS
                String ColQuery = "";
                for (int i = 0; i < count; i++) {
                    colNameAll = (String[]) rep.findColById(tableId[i], count);
                    tabNameAll = rep.findTabById(tableId[i], count);

                    ColQuery = ColQuery + tabNameAll[i] + "." + colNameAll[i];

                    if (i != count - 1) {
                        ColQuery = ColQuery + delim;

                    }
                }
                //ADD SELECT STMT

                ColQuery = "SELECT " + ColQuery;
  //              System.out.println("ColQuery output:   " + ColQuery);

        // ADD FROM AND TABLE_NAME(S) TO THE SELECTED COLUMN(S)
                String from = "  FROM   ";
                ColQuery = ColQuery + from + myTable;

//                System.out.println("<<<<SELECTED COL and MyTable output:>>>   " + ColQuery);

        //ADD WHERE CLAUSE

                if (!aliasQuery.equals("")) {
                    ColQuery = ColQuery + " WHERE ";

                } else {
                    ColQuery = ColQuery;

                }




                // ADD FILTER
                ColQuery = ColQuery + " " + aliasQuery;



                System.out.println("Complete Report Query END:   " + ColQuery);


                String[] temp = new String[count];

                try {


                    String comp = rep.getCompanyName();

  
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment; filename=dynamicReport.xls");
                    WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
                    WritableSheet s = w.createSheet("Demo", 0);

                    /* Format the Font */
                    WritableFont times16font = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD, true);
                    WritableCellFormat times16format = new WritableCellFormat(times16font);

                    WritableFont times13font = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD, true);
                    WritableCellFormat times13format = new WritableCellFormat(times13font);



                    s.addCell(new Label(3, 0, comp, times16format));

//                    System.out.println("Invoke the second action here");
                    for (int col = 0; col < count; col++) {

                    s.addCell(new Label(col, 2, rep.findColDescById(tableId[col]).toUpperCase(), times13format));
  

                    }


                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    PersistenceServiceDAO mag = new PersistenceServiceDAO();
                    // Approval app = null;
                    ArrayList list = new ArrayList();
                    int colCount = tableId.length;
                    double costP = 0.00;
                    String dateFormat ="";
                    for (int i = 0; i < colCount; i++) {

//                        System.out.println("SELECTED COLUMNS IN getReportByColumn : " + i + " " + tableId[i]);

                    }

                    int rowCount = 0;

                    //String[] temp = new String[count];
                    ArrayList rows = new ArrayList();


                    String FINDER_QUERY = ColQuery;
                  System.out.println("======>>>>>>>FINDER_QUERY: "+FINDER_QUERY);
                    try {
                        con = mag.getConnection("legendPlus");
                        ps = con.prepareStatement(FINDER_QUERY);

                        //ps.setString(1, "Y");
                        rs = ps.executeQuery();

                        //  String query = "Select +query+ from am_raisentry_post  ";

    //                    System.out.println("Report Compilation in Progress for " + tableId.length + "  Columns");



                Number number;


                int startRow = 4;
                while (rs.next()) {
                    rowCount++;
                    //ArrayList row = new ArrayList();
                    String[] temp1 = new String[count];
                    //for(int i = 0; i<count;i++){
                    for (int i = 0; i < count; i++) {

                        temp1[i] = rs.getString(rep.findColById(tableId[i]));
                        //System.out.println("SEE the COLUMNS: " + i + " " + temp[i]);
                        // rows.add(temp[i]);

                        // s.addCell(new Label(col, startRow, bask[col]));
                         //if (rep.findColById(tableId[i]).equalsIgnoreCase("cost_price")) {
                        if(rep.isNumericColumn(tableId[i],"N")) {
                            costP = Double.parseDouble(temp1[i]);
                            s.addCell(new Label(i, startRow, temp1[i]));
                            number = new Number(i, startRow, costP, times13format);
                            s.addCell(number);
                        }  
                       
                        else {   
                               if(rep.isNumericColumn(tableId[i],"D")) {

                            dateFormat =  temp1[i].substring(0, 10);
                            s.addCell(new Label(i, startRow, dateFormat));
                        }else
                            s.addCell(new Label(i, startRow, temp1[i]));
                        }

                     // System.out.println("Written records are:" + temp1[i]);
                    }
                    startRow++;
                    //rows.add(temp);

                }

 //               System.out.println("TOTAL ROW COUNT<<:::>> " + rowCount);

            } catch (Exception ex) {
                System.out.println("WARNING: cannot fetch the column for the report  <><>>>:  " +
                        ex.getMessage());
            } finally {
                mag.closeConnection(con, ps, rs);
            }
   //                 System.out.println("Invoked the last action here!");
   //                  System.out.println("Generatin report in excel format. Please wait .... ");



                    w.write();
                    w.close();


                } catch (Exception e) {
                    throw new ServletException("Exception in Excel Report Servlet", e);
                } finally {
                    if (outp != null) {
                        outp.close();

                    }
                }


        }
    }
}


