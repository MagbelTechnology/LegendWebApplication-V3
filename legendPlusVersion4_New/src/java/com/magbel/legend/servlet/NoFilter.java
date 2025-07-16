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

import jxl.Workbook;
import jxl.write.Number;
import jxl.write.*;

import com.magbel.legend.bus.Report;
import com.magbel.dao.PersistenceServiceDAO;



/**
 *
 * @author Kareem Wasiu Aderemi
 */



public class NoFilter extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream outp = null;
        FileOutputStream out;
        //PrintStream out = new PrintStream();
        HttpSession session = request.getSession();
        String[] tableId = (String[])session.getAttribute("tableId");
//        String[] tableIdent = (String[])session.getAttribute("tableId");
        //String[] tableId = (String[]) session.getAttribute("tableId");
        // String alias = (String)session.getAttribute("alias");
        // String myTable = (String)session.getAttribute("myTable");
        //String myTable = request.getParameter("myTable");
        String queryTabTo = (String)session.getAttribute("queryTabTo");
        String myTable = (String)session.getAttribute("myTable");
        String getTablQuery = (String)session.getAttribute("getTabQuery");
        String getColQuery = (String)session.getAttribute("getColQuery");
 //       System.out.println("========tableId: "+tableId);
        String affectedTable =(String)session.getAttribute("queryTabTo");

        int count = 0;

        /*if (tableId == null) {
            session.setAttribute("tableId", tableId);
            response.sendRedirect("reportFilter.jsp");
            count = 0;
        } else {
            count = tableId.length; 
        }
*/
//System.out.println("========Table Id: "+tableId+"   getTablQuery: "+getTablQuery+"    getColQuery: "+getColQuery);
        count = tableId.length;
        
        Report rep = new Report();

        String operation = request.getParameter("operation");
        
    
//        System.out.println("NoFilter tableId @ redirect <><><>:::" + tableId);


// FORM THE QUERY ALIAS

            String a_delim = "  AND ";
            String operand = "";

            
            ArrayList list2 = rep.findByQuery(getColQuery);
 //           System.out.println("NoFilter getColQuery::" + getColQuery);
            String tabQuery = "SELECT TABLE_NAMES FROM COL_FILTER WHERE ID = ?";
            String colQuery = "SELECT COLUMN_NAME FROM COL_FILTER WHERE ID = ?";
           String  getfromTQuery = "SELECT DISTINCT TABLE_NAMES FROM COL_FILTER WHERE PEG =?";

            getColQuery = getColQuery + "AND COLUMN_NAME= ?";
            getTablQuery = getTablQuery + "AND COLUMN_NAME= ?";

            int toColCount = list2.size() + 1;

          
              
            String aliasQuery1 = "";
            String fromCol = "";
            String toCol = "";
            String toTab = "";
            String fromTab = "";
            String join = "  AND   ";
            String aliasQueryBranch = "";
            String aliasQueryDept = "";
            String findBranch = "";
            String findDept = "";
            String findCategory = "";
            String findJoin = "";
            //String affectedTable = "";
            String findJoinValue = "";
            String assetstatus = "ACTIVE";

            ArrayList colList = new ArrayList();
            ArrayList tabList = new ArrayList();

            ArrayList colValueList = new ArrayList();
            ArrayList getOp = new ArrayList();

            ArrayList listTab = new ArrayList();
            ArrayList listCol = new ArrayList();


           

            String[] colNameFrom1 = new String[toColCount];
            String[] tabNameFrom1 = new String[toColCount];
            String holder = "";
            String opr = "";
            String tempValue = "";
            String mainValue = "";
            String aliasQuery = "";



           /* for (int i = 0; i < toColCount; i++) {

                holder = request.getParameter("temp" + aliasIdTo[i]);

                System.out.println("request.getParameter(temp+aliasIdTo[i]) : " + "temp" + aliasIdTo[i]);
                System.out.println("The holder=>*+++ : " + holder);

                //  if(holder!=null&&(!holder.equalsIgnoreCase(""))){

                colValueList.add(holder);

                // }


            }

            for (int i = 0; i < toColCount; i++) {

                opr = request.getParameter("op" + aliasIdFrom[i]);

                System.out.println("request.getParameter(op+aliasIdTo[i]) : " + "op" + aliasIdFrom[i]);
                System.out.println("The holder=>*+++ : " + opr);

                //  if(holder!=null&&(!holder.equalsIgnoreCase(""))){

                getOp.add(opr);

            }

*/


 //   System.out.println("Alias Actual colCountTo  is>>>>> : " + list2.size());

    if (list2.size() < 1) {

  //System.out.println("Processing  report for a single table>>....... ");

       

              aliasQuery = findJoinValue;

      } else {

  //        System.out.println("Processing  report for multiple tables>>....... ");



  //      affectedTable =request.getParameter("queryTabTo");
 //select column_name from col_filter where table_names in(select table_name from mapping where table_name in(select table_name from col_look_up where ID=  '1'   OR ID = '9'   OR ID = '10'   OR ID = '20'   OR ID = '21'   OR ID = '26'   OR ID = '25' ) AND TABLE_NAME!='AM_ASSET')
  //      System.out.println("========affectedTable: "+affectedTable);
String getJoinTab = "select table_names from col_filter where table_names in("+affectedTable+")";
String getJoinCol = "select COLUMN_NAME from col_filter where table_names in("+affectedTable+")";
   listTab = rep.findByQuery(getJoinTab);
    listCol = rep.findByQuery(getJoinCol);

    String fromT = "";
        String status = "From";
        fromT = rep.findByIdQuery(status, getfromTQuery);
for (int i = 0; i < listTab.size(); i++) {

//    System.out.println("The toTab.toCol is=>********** : " + listTab.get(i).toString() + "<..>" + listCol.get(i).toString());
 //           System.out.println("The From.toCol is=>********** : " + fromT + "<..>" + listCol.get(i).toString());

            // findJoin = findJoin + "AM_ASSET." + colList.get(i).toString() + getOp.get(i).toString() + tabList.get(i).toString() + "." + colList.get(i).toString();
            findJoin = findJoin + fromT +"." + listCol.get(i).toString() + "  =  " + listTab.get(i).toString() + "." + listCol.get(i).toString();

            if (i != listTab.size() - 1) {
                findJoin = findJoin + a_delim;
            }


    }





       /* System.out.println("The findJoin query is=>********** : " + findJoin);


        for (int i = 0; i < toColCount; i++) {

            colNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, colQuery);
            tabNameFrom1 = rep.findByIdQuery(aliasIdFrom[i], toColCount, tabQuery);
            //if (colNameFrom1[i].equals("BRANCH_CODE")) {

            System.out.println(" aliasIdFrom[i] is=>>>>> : " + aliasIdFrom[i]);
            //colNameTo = rep.findByIdQuery(aliasIdTo[i], aliasCountF, colQuery);
            //tabNameTo = rep.findByIdQuery(aliasIdTo[i], aliasCountF, tabQuery);
            fromCol = rep.findByIdQuery(aliasIdFrom[i], colQuery);
            fromTab = rep.findByIdQuery(aliasIdFrom[i], tabQuery);
            toCol = rep.findByIdQuery(fromCol, getColQuery);
            toTab = rep.findByIdQuery(toCol, getTablQuery);



            //  if (colList.size()>1) {


            System.out.println("The toTab.toCol is=>********** : " + tabNameFrom1[i] + "<>" + colNameFrom1[i]);
            System.out.println("My colValueList is=>+++++++ : " + colValueList.get(i).toString());

            mainValue = colValueList.get(i).toString();

            if(rep.isNumeric(aliasIdFrom[i],"N")){
            tempValue = mainValue.replaceAll(",", "");

            }else{
            tempValue = "'"+mainValue+"'";

            }
            findJoinValue = findJoinValue + tabNameFrom1[i] + "." + colNameFrom1[i] + getOp.get(i).toString() + tempValue;


            if (i != colValueList.size() - 1) {
                findJoinValue = findJoinValue + a_delim;
            }
        }
        */
        
//        System.out.println("The findJoinValue query is=>********** : " + findJoinValue);

        //aliasQuery = findJoin + join + findJoinValue;
aliasQuery = findJoin;

    }



 //               System.out.println("My Alias String is>>>>> : " + aliasQuery);

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
//                System.out.println("ColQuery output:   " + ColQuery);

        // ADD FROM AND TABLE_NAME(S) TO THE SELECTED COLUMN(S)
                String from = "  FROM   ";
                ColQuery = ColQuery + from + myTable+" WHERE ASSET_STATUS =  '"+assetstatus+"'";

 //               System.out.println("SELECTED COL and MyTable output:>>>   " + ColQuery);

        //ADD WHERE CLAUSE

                if (!aliasQuery.equals("")) {
                    ColQuery = ColQuery + " WHERE ASSET_STATUS = '"+assetstatus+"' AND ";

                } else {
                    ColQuery = ColQuery;

                }




                // ADD FILTER
                ColQuery = ColQuery + " " + aliasQuery;



//                System.out.println("Report Query END:   " + ColQuery);


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

  //                  System.out.println("Invoke the second action here");
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

                    for (int i = 0; i < colCount; i++) {

    //                    System.out.println("SELECTED COLUMNS IN getReportByColumn : " + i + " " + tableId[i]);

                    }

                    int rowCount = 0;

                    //String[] temp = new String[count];
                    ArrayList rows = new ArrayList();


                    String FINDER_QUERY = ColQuery;

                    try {
                        con = mag.getConnection("legendPlus");
                        ps = con.prepareStatement(FINDER_QUERY);

                        //ps.setString(1, "Y");
                        rs = ps.executeQuery();

                        //  String query = "Select +query+ from am_raisentry_post  ";

         //               System.out.println("Report Compilation in Progress for " + tableId.length + "  Columns");



                Number number;


                int startRow = 4;
                while (rs.next()) {
                    rowCount++;
                    //ArrayList row = new ArrayList();
                    String[] temp1 = new String[count];
                    //for(int i = 0; i<count;i++){
                    for (int i = 0; i < count; i++) {

                        temp1[i] = rs.getString(rep.findColById(tableId[i]));
 //                       System.out.println("SEE the COLUMNS: " + i + " " + temp[i]);
                        // rows.add(temp[i]);

                        // s.addCell(new Label(col, startRow, bask[col]));
                         if(rep.isNumericColumn(tableId[i],"N")) {
                            costP = Double.parseDouble(temp1[i]);
                            s.addCell(new Label(i, startRow, temp1[i]));
                            number = new Number(i, startRow, costP, times13format);
                            s.addCell(number);
                        } else {
                            s.addCell(new Label(i, startRow, temp1[i]));
                        }

 //                       System.out.println("Written records are:" + temp1[i]);
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
  //                  System.out.println("Invoke the last action here!");


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



