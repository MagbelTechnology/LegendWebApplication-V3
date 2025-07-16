/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.magbel.admin.dao.PersistenceServiceDAO;


/**
 *
 *  @author Kareem Wasiu Aderemi
 */
public class Report extends PersistenceServiceDAO {

    

    public String getCompanyName() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String comp = "";

        try {
            query = "SELECT COMPANY_NAME FROM am_gb_company";
            con = getConnection("helpDesk");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                comp = rs.getString("COMPANY_NAME");

            }


        } catch (Exception e) {
            System.out.println("WARNING: cannot fetch am_gb_company->" +
                    e.getMessage());

        } finally {
            closeConnection(con, ps, rs);
        }

        return comp;

    }

    

    public ArrayList getReportByColumn(String query, String[] selCol) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        for (int i = 0; i < count; i++) {

         System.out.println("SELECTED COLUMNS IN getReportByColumn : " + i + " " + selCol[i]);

        }

        int rowCount = 0;

        //String[] temp = new String[count];
        ArrayList rows = new ArrayList();


        String FINDER_QUERY = query;

        try {
            con = getConnection("helpDesk");
            ps = con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs = ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

            System.out.println("Report Compilation in Progress.......::: " + selCol.length + "  Columns");


            /*while (rs.next()){
            ArrayList row = new ArrayList();
            for (int i = 1; i <= columnCount ; i++){
            row.add(rs.getString(i));
            }
            rows.add(row);
            }
             */
            while (rs.next()) {
                rowCount++;
                //ArrayList row = new ArrayList();
                String[] temp = new String[count];
                //for(int i = 0; i<count;i++){
                for (int i = 0; i < count; i++) {

                    temp[i] = rs.getString(findColById(selCol[i]));
                    System.out.println("SEE the COLUMNS: " + i + " " + temp[i]);
                   // rows.add(temp[i]);

                }

                rows.add(temp);
                
            }

System.out.println("TOTAL ROW COUNT<<:::>> " + rowCount);

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch the column for the report  <><>>>:  " +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return rows;
    }

  
    public ArrayList getTabletById(
            String query, String[] selCol) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        String[] temp = new String[count];

        String FINDER_QUERY = query;

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs =
                    ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

            while (rs.next()) {
                for (int i = 0; i < count;
                        i++) {
                    temp[i] = rs.getString(findTabById(selCol[i]));

                    System.out.println("SEE the TABLE: " + i + " " + temp[i]);

                }

                collection.add(temp);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch the table by id" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public String[] findTabById(String id, int count) {

        String[] col = new String[count];

        for (int i = 0; i < count;
                i++) {
            col[i] = findTabById(id);
        }

        return col;

    }

    public String findTabById(
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab = "";

        String FINDER_QUERY = "SELECT TABLE_NAME from COL_LOOK_UP WHERE ID =?";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                tab = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return tab;

    }

    public String[] findByIdQuery(String id, int count, String query) {

        String[] col = new String[count];

        for (int i = 0; i < count;
                i++) {
            col[i] = findByIdQuery(id, query);
        }

        return col;

    }

    public String findByIdQuery(String id, String query) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab = "";

        String FINDER_QUERY = query;

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                tab = rs.getString(1);
                System.out.println("Output column by Id Query->" + tab);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Id Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return tab;

    }


   // tabNameFrom =  rep.findByIdQuery1(aliasIdFrom[i],aliasCountF,tabQuery);

    public ArrayList findByQuery1(String query, String[] sel) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab;

        ArrayList list = new ArrayList();
        String FINDER_QUERY = query;

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            rs =
                    ps.executeQuery();

            while (rs.next()) {



                list.add(rs.getString(1));
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findByQuery(String query) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab;

        ArrayList list = new ArrayList();
        String FINDER_QUERY = query;

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            rs =
                    ps.executeQuery();

            while (rs.next()) {

                list.add(rs.getString(1));
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }
    
    
    public boolean isNumeric(String id,String op) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";
        boolean status = false;

        String FINDER_QUERY = "SELECT OPERAND from COL_FILTER WHERE ID =? AND OPERAND =?";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
             ps.setString(2, op);
            rs = ps.executeQuery();

            while (rs.next()) {

               status = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNumeric->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return status;
    }

    public boolean isNumericColumn(String id,String dType) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";
        boolean status = false;

        String FINDER_QUERY = "SELECT DATA_TYPE from COL_LOOK_UP WHERE ID =? AND DATA_TYPE =?";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
             ps.setString(2, dType);
            rs = ps.executeQuery();

            while (rs.next()) {

               status = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNumeric->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return status;
    }


   
    public String[] findColById(String id, int count) {
        String[] col = new String[count];

        for (int i = 0; i < count;
                i++) {
            col[i] = findColById(id);
        }

        return col;

    }

    public String findColById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT COLUMN_NAME from COL_LOOK_UP WHERE ID =?";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return col;
    }

    public String findColDescById(
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT DESCRIPTION from COL_LOOK_UP WHERE ID =?";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return col;
    }

    public ArrayList findApprovalByColumn(
            String query, String[] selCol, String para) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        String[] temp = new String[count];



        String FINDER_QUERY = "SELECT " + query + " from am_raisentry_post";

        try {
            con = getConnection("helpDesk");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs =
                    ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

            while (rs.next()) {
                for (int i = 0; i < count;
                        i++) {
                    temp[i] = rs.getString(selCol[i]);
                }

                collection.add(temp);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }
}
