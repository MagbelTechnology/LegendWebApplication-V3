package legend;

import magma.net.dao.MagmaDBConnection;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Rahman Oloritun
 * @version 1.0
 */
public class ManageSbuDepartments {
    private MagmaDBConnection dbConnection;
    public ManageSbuDepartments() {
        dbConnection = new MagmaDBConnection();

    }

    public java.util.ArrayList getDepartments() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.objects.Department dept = null;

        String query = "SELECT [Dept_ID],[Dept_code],[Dept_name]"
                       + ",[Dept_acronym],[Branch],[GL_Prefix]"
                       + ",[Dept_Status],[user_id],[CREATE_DATE]"
                       + " FROM [am_ad_department]";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String deptid = rs.getString("Dept_ID");
                String deptcode = rs.getString("Dept_code");
                String deptname = rs.getString("Dept_name");
                String deptacronym = rs.getString("Dept_acronym");
                String statux = rs.getString("Dept_Status");

                dept = new legend.objects.Department();
                dept.setDept_acronym(deptacronym);
                dept.setDept_code(deptcode);
                dept.setDept_id(deptid);
                dept.setDept_name(deptname);
                dept.setDept_status(statux);
                _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return _list;
    }

    public java.util.ArrayList getDepartmentSections() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.objects.Section dept = null;

        String query = "SELECT [Section_ID],[Section_Code],[Section_Name]"
                       + ",[section_acronym],[GL_Prefix]"
                       + ",[Section_Status]  ,[User_ID]"
                       + " FROM [am_ad_section]";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String deptid = rs.getString("Section_ID");
                String deptcode = rs.getString("Section_Code");
                String deptname = rs.getString("Section_Name");
                String deptacronym = rs.getString("section_acronym");
                String statux = rs.getString("Section_Status");

                dept = new legend.objects.Section();
                dept.setSection_acromyn(deptacronym);
                dept.setSection_code(deptcode);
                dept.setSection_id(deptid);
                dept.setSection_name(deptname);
                dept.setSection_status(statux);
                _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return _list;
    }

    public legend.objects.BranchDept getDeptInBranch(String branchid,
            String deptId) {
        legend.objects.BranchDept dept = null;

        String query = "SELECT [branchCode],[deptCode],[gl_prefix]"
                       + ",[gl_suffix],[branchId],[deptId],[mtid]"
                       + " FROM [sbu_branch_dept] WHERE [branchId]='"
                       + branchid + "' AND [deptId]='" + deptId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");

                dept = new legend.objects.BranchDept();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);

                //_list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return dept;

    }

    public legend.objects.DeptSection getSectionInDept(String branchid,
            String deptId, String sectionId) {
        legend.objects.DeptSection dept = null;

        String query =
                "SELECT [branchCode],[deptCode],[gl_prefix],[sectionCode]"
                + ",[gl_suffix],[branchId],[deptId],[sectionId],[mtid]"
                + " FROM [sbu_dept_section] WHERE [branchId]='"
                + branchid + "' AND [deptId]='" + deptId + "'"
                + " AND [sectionId]='" + sectionId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");
                String sectioncode = rs.getString("sectionCode");
                String sectiondi = rs.getString("sectionId");

                dept = new legend.objects.DeptSection();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);
                dept.setSectionCode(sectioncode);
                dept.setSectionId(sectiondi);
                //_list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return dept;

    }

    public boolean insertDeptForBranch(java.util.ArrayList list) {

        String query = "INSERT INTO [sbu_branch_dept]([branchCode]"
                       + ",[deptCode],[branchId]"
                       + ",[deptId],[gl_prefix]"
                       + ",[gl_suffix])"
                       + " VALUES(?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = dbConnection.getConnection("fixedasset");
            for (int i = 0; i < list.size(); i++) {
                bd = (legend.objects.BranchDept) list.get(i);
                ps = con.prepareStatement(query);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getBranchId());
                ps.setString(4, bd.getDeptId());
                ps.setString(5, bd.getGl_prefix());
                ps.setString(6, bd.getGl_suffix());
                ps.addBatch();
            }
            d = ps.executeBatch();
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
        return (d.length > 0);
    }

    public boolean updateDeptForBranch(java.util.ArrayList list) {
        String query = "UPDATE [sbu_branch_dept] SET [branchCode] = ?"
                       + ",[deptCode] = ?,[gl_prefix] = ?,[gl_suffix] = ?"
                       + " WHERE [branchId]=?"
                       + " AND [deptId]=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = dbConnection.getConnection("fixedasset");
            for (int i = 0; i < list.size(); i++) {
                bd = (legend.objects.BranchDept) list.get(i);
                ps = con.prepareStatement(query);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getGl_prefix());
                ps.setString(4, bd.getGl_suffix());
                ps.setString(5, bd.getBranchId());
                ps.setString(6, bd.getDeptId());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean insertSectionForDept(java.util.ArrayList list) {
        String query = "INSERT INTO [sbu_dept_section]([branchCode]"
                       + ",[deptCode],[sectionCode],[branchId]"
                       + ",[deptId],[sectionId],[gl_prefix]"
                       + ",[gl_suffix])"
                       + " VALUES(?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.objects.DeptSection bd = null;
        int[] d = null;
        try {
            con = dbConnection.getConnection("fixedasset");
            for (int i = 0; i < list.size(); i++) {
                bd = (legend.objects.DeptSection) list.get(i);
                ps = con.prepareStatement(query);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getSectionCode());
                ps.setString(4, bd.getBranchId());
                ps.setString(5, bd.getDeptId());
                ps.setString(6, bd.getSectionId());
                ps.setString(7, bd.getGl_prefix());
                ps.setString(8, bd.getGl_suffix());
                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean updateSectionForDept(java.util.ArrayList list) {
        String query = "UPDATE [sbu_dept_section] SET [branchCode] = ?"
                       + ",[deptCode] = ?,[sectionCode]=?,[gl_prefix] = ?"
                       + ",[gl_suffix] = ?"
                       + " WHERE [branchId]=?"
                       + " AND [deptId]=?"
                       + " AND [sectionId]=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        legend.objects.DeptSection bd = null;
        int[] d = null;
        try {
            con = dbConnection.getConnection("fixedasset");
            for (int i = 0; i < list.size(); i++) {
                bd = (legend.objects.DeptSection) list.get(i);
                ps = con.prepareStatement(query);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getSectionCode());
                ps.setString(4, bd.getGl_prefix());
                ps.setString(5, bd.getGl_suffix());
                ps.setString(6, bd.getBranchId());
                ps.setString(7, bd.getDeptId());
                ps.setString(8, bd.getSectionId());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public java.util.ArrayList getAllDeptInBranch(String branchid) {
        java.util.ArrayList _list = new java.util.ArrayList();
           legend.objects.BranchDept dept = null;

           String query = "SELECT [branchCode],[deptCode],[gl_prefix]"
                          + ",[gl_suffix],[branchId],[deptId],[mtid]"
                          + " FROM [sbu_branch_dept] WHERE [branchId]='"
                          + branchid +  "'";

           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;

           try {
               con = dbConnection.getConnection("fixedasset");
               ps = con.prepareStatement(query);
               rs = ps.executeQuery();
               while (rs.next()) {
                   String branchcode = rs.getString("branchCode");
                   String deptcode = rs.getString("deptCode");
                   String glprefix = rs.getString("gl_prefix");
                   String glsuffix = rs.getString("gl_suffix");
                   String branchId = rs.getString("branchId");
                   String deptid = rs.getString("branchId");
                   String mtid = rs.getString("mtid");

                   dept = new legend.objects.BranchDept();
                   dept.setBranchCode(branchcode);
                   dept.setBranchId(branchId);
                   dept.setDeptId(deptid);
                   dept.setDeptCode(deptcode);
                   dept.setGl_prefix(glprefix);
                   dept.setGl_suffix(glsuffix);
                   dept.setMtid(mtid);

                   _list.add(dept);
               }
           } catch (Exception ex) {
               System.out.println("WARN: Error fetching all asset ->" + ex);
           } finally {
               dbConnection.closeConnection(con, ps);
           }
           return _list;

       }
       public java.util.ArrayList getAllSectionInDept(String branchid,
                   String deptId) {
               legend.objects.DeptSection dept = null;
       java.util.ArrayList _list = new java.util.ArrayList();
               String query =
                       "SELECT [branchCode],[deptCode],[gl_prefix],[sectionCode]"
                       + ",[gl_suffix],[branchId],[deptId],[sectionId],[mtid]"
                       + " FROM [sbu_dept_section] WHERE [branchId]='"
                       + branchid + "' AND [deptId]='" + deptId + "'";

               Connection con = null;
               PreparedStatement ps = null;
               ResultSet rs = null;

               try {
                   con = dbConnection.getConnection("fixedasset");
                   ps = con.prepareStatement(query);
                   rs = ps.executeQuery();
                   while (rs.next()) {
                       String branchcode = rs.getString("branchCode");
                       String deptcode = rs.getString("deptCode");
                       String glprefix = rs.getString("gl_prefix");
                       String glsuffix = rs.getString("gl_suffix");
                       String branchId = rs.getString("branchId");
                       String deptid = rs.getString("branchId");
                       String mtid = rs.getString("mtid");
                       String sectioncode = rs.getString("sectionCode");
                       String sectiondi = rs.getString("sectionId");

                       dept = new legend.objects.DeptSection();
                       dept.setBranchCode(branchcode);
                       dept.setBranchId(branchId);
                       dept.setDeptId(deptid);
                       dept.setDeptCode(deptcode);
                       dept.setGl_prefix(glprefix);
                       dept.setGl_suffix(glsuffix);
                       dept.setMtid(mtid);
                       dept.setSectionCode(sectioncode);
                       dept.setSectionId(sectiondi);
                       _list.add(dept);
                   }
               } catch (Exception ex) {
                   System.out.println("WARN: Error fetching all asset ->" + ex);
               } finally {
                   dbConnection.closeConnection(con, ps);
               }
               return _list;

           }

}
