package magma.net.manager;

import java.sql.*;
import java.util.ArrayList;

import magma.net.dao.MagmaDBConnection;
import magma.net.vao.ClassFunction;
import magma.net.vao.Function;

/**
 * <p>Title: SystemsManager.java</p>
 *
 * <p>Description: An implementation class<br>
       for managing application security.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class SytemsManager extends MagmaDBConnection {
    public SytemsManager() {
    }

    public ArrayList findAllFunctions() {

        ArrayList finder = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(this.getFunctionQuery(""));
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString(1);
                String code = rs.getString(2);
                String url = rs.getString(3);
                String description = rs.getString(4);
                String type = rs.getString(5);
                Function function = new Function(id, code, url, description,
                                                 type);
                finder.add(function);
            }

        } catch (Exception er) {
            System.out.println("WARN:Error connecting to DB \n" + er);
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

    public ArrayList findFunctionsBySecurityClass(String classid) {
        ArrayList finder = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String CLASS_FUNCTION_QUERY =
                    "SELECT A.ROLE_UUID,'000' AS CODE ,A.ROLE_WURL," +
                    "A.ROLE_NAME,A.MENU_TYPE " +
                    "FROM AM_AD_CLASS_PRIVILEGES B," +
                    "AM_AD_PRIVILEGES A   " +
                    "WHERE B.ROLE_UUID = A.ROLE_UUID   " +
                    "AND B.ROLE_VIEW = 'Y' " +
                    "AND B.CLSS_UUID = ? ORDER BY A.PRIORITY";

            con = getConnection("legendPlus");
//            ps = con.prepareStatement(this.getClassFunctionQuery(classid));
            ps = con.prepareStatement(CLASS_FUNCTION_QUERY);
            ps.setString(1, classid);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString(1);
                String code = rs.getString(2);
                String url = rs.getString(3);
                String description = rs.getString(4);
                String type = rs.getString(5);
                Function function = new Function(id, code, url, description,
                                                 type);
                finder.add(function);
            }

        } catch (Exception er) {
            String message = "WARN: Can not select user's available\n" +
                             " functions due to the following:\n";
            System.out.println(message + er);
        } finally {
            closeConnection(con, ps, rs);
        }

        return finder;

    }

    private String getFunctionQuery(String filter) {
        String SELCT_QUERY = "SELECT ROLE_UUID,'000' as CODE, ROLE_WURL," +
                             "ROLE_NAME,MENU_TYPE  " +
                             "FROM AM_AD_PRIVILEGES ORDER BY PRIORITY";
        return SELCT_QUERY;
    }

    public ArrayList findClassFunctionsById(String classid) {
        ArrayList finder = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String SELECT_QUERY = "SELECT CLSS_UUID,ROLE_UUID,ROLE_ADDN," +
                              "ROLE_EDIT FROM AM_AD_CLASS_PRIVILEGES  " +
                              "WHERE CLSS_UUID = ?  ";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY);
            ps.setString(1, classid);
            rs = ps.executeQuery();

            while (rs.next()) {

                String classCode = rs.getString(1);
                String functionCode = rs.getString(2);
                boolean addable = ((rs.getString(3).equalsIgnoreCase("Y")) ? true : false);
                boolean editable = ((rs.getString(4).equalsIgnoreCase("Y")) ? true : false);
                ClassFunction cFunction = new ClassFunction(classCode,
                        functionCode,
                        editable, addable);

                finder.add(cFunction);
            }

        } catch (Exception er) {
            String message = "WARN: Can not select user's priviledges\n" +
                             " functions due to the following:\n";
            System.out.println(message + er);
        }
        finally
        {
            closeConnection(con,ps,rs);
        }
        return finder;

    }

    public boolean isEditable(String objectid, ArrayList classFunctions) {
        boolean editable = false;
        for (int x = 0; x < classFunctions.size(); x++) {
            ClassFunction cf = (ClassFunction) classFunctions.get(x);
            if (cf.getFunctionCode().equals(objectid)) {
                editable = cf.isEditable();
                break;
            }
        }

        return editable;

    }

    public boolean isAddable(String objectid, ArrayList classFunctions) {
        boolean addable = false;
        for (int x = 0; x < classFunctions.size(); x++) {
            ClassFunction cf = (ClassFunction) classFunctions.get(x);
            if (cf.getFunctionCode().equals(objectid)) {
                addable = cf.isAddable();
                break;
            }
        }

        return addable;
    }


    private String getClassFunctionQuery(String classCode) {
        String CLASS_FUNCTION_QUERY =
                "SELECT A.ROLE_UUID,'000' AS CODE ,A.ROLE_WURL," +
                "A.ROLE_NAME,A.MENU_TYPE " +
                "FROM AM_AD_CLASS_PRIVILEGES B," +
                "AM_AD_PRIVILEGES A   " +
                "WHERE B.ROLE_UUID = A.ROLE_UUID   " +
                "AND B.ROLE_VIEW = 'Y' " +
                "AND B.CLSS_UUID = '" + classCode + "' ORDER BY A.PRIORITY";

        return CLASS_FUNCTION_QUERY;
    }

    public boolean isIdAutogenerated() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean idAutogenerated = false;
        String selectQuery = "SELECT auto_generate_id FROM AM_GB_COMPANY";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                idAutogenerated = ((rs.getString(1).equalsIgnoreCase("Y")) ? true : false);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error determining autogenerateid ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return idAutogenerated;

    }

    public String getDefaultResidualValue() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String residualValue = "0.0";
        String selectQuery = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                residualValue = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(
                    "WARNING:Error getting DefaultResidualValue->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return residualValue;

    }


}
