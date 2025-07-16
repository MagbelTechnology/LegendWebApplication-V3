package legend;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <p>Title: Magma.net System</p>
 *
 * <p>Description: Fixed Assets Manager</p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 *
 * @author Charles Ayoola Ayodele-Peters.
 * modified by Rahman Oloritun 28-Nov-2006
 * @version 1.0
 */
public class ManageSecurities extends ConnectionClass {
    private String[] privileges = new String[4];
    public ManageSecurities() throws Exception {
    }

    /**
     * selectAvailable
     *
     * @param cls String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectAvailable(String cls) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("select count(*)from am_ad_privileges");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append("select role_uuid, role_name"
                  + " from am_ad_privileges"
                  + " order by role_name asc");
        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][2];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 2; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * selectAssigned
     *
     * @param cls String
     * @return String[][]
     * @throws Throwable
     */
    public String checkAssigned(String cls) {
        StringBuffer sq = new StringBuffer(100);
        String val = new String();
        sq.append(
                "select am_ad_privileges.role_uuid, role_name, role_view, role_addn, role_edit"
                +
                " from am_ad_privileges left outer join am_ad_class_privileges"
                +
                " on am_ad_privileges.role_uuid = am_ad_class_privileges.role_uuid"
                + " where clss_uuid = '" + cls + "'"
                + " order by role_name asc ");
        try {
            ResultSet rv = getStatement().executeQuery(sq.toString());

            rv.next();

            val = rv.getString(1);
            freeResource();
        } catch (Throwable t) {}

        return val;
    }

    /**
     * selectAssigned
     *
     * @param cls String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectAssigned(String cls) throws Throwable {
        StringBuffer cq = new StringBuffer(100);
        cq.append("select count(am_ad_privileges.role_uuid)"
                  +
                  " from am_ad_privileges left outer join am_ad_class_privileges"
                  +
                  " on am_ad_privileges.role_uuid=am_ad_class_privileges.role_uuid"
                  + " where clss_uuid ='" + cls + "'");
        ResultSet rc = getStatement().executeQuery(
                cq.toString());

        StringBuffer sq = new StringBuffer(100);
        sq.append(
                "select am_ad_privileges.role_uuid, role_name, role_view, role_addn, role_edit"
                +
                " from am_ad_privileges left outer join am_ad_class_privileges"
                +
                " on am_ad_privileges.role_uuid = am_ad_class_privileges.role_uuid"
                + " where clss_uuid = '" + cls + "'"
                + " order by role_name asc ");

        ResultSet rv = getStatement().executeQuery(
                sq.toString());

        rc.next();
        String[][] values = new String[rc.getInt(1)][5];

        for (int x = 0; x < values.length; x++) {
            rv.next();
            for (int y = 0; y < 5; y++) {
                values[x][y] = rv.getString(y + 1);
            }
        }
        freeResource();
        return values;
    }

    /**
     * assignPrivileges
     *
     * @param clss String
     * @param role String[]
     * @param assg String[]
     * @param addn String[]
     * @param edit String[]
     * @return boolean
     * @throws Exception
     */
    public boolean assignPrivileges(String clss, String[] role, String[] assg,
                                    String[] addn, String[] edit) throws
            Exception {
        StringBuffer aq = new StringBuffer(1024);
        Statement stmt = getStatement();

        for (int i = 0; i < role.length; i++) {
            aq.append("insert into am_ad_class_privileges("
                      + "clss_uuid, role_uuid, role_view, role_addn, role_edit"
                      + ") values(");

            /* aq.append("exec am_msp_assign_privileges");*/
            aq.append("'");
            aq.append(clss);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(role[i]);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(assg[i]);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(addn[i]);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(edit[i]);
            aq.append("')");
            //System.out.println("[1]ManageSecurities-->AssignP-->" + aq.toString());
            stmt.addBatch(aq.toString());
            aq = new StringBuffer(1024);

        }
        int update[] = stmt.executeBatch();
        /* for (int i = 0; i < role.length; i++) {
         System.out.println("[2]ManageSecurities-->AssignP-->" + update[i]);
         }*/
        freeResource();
        return (update.length > 0);
        //getStatement().executeUpdate(
        //aq.toString()) == -1);
    }

    /**
     * removePrivileges
     *
     * @param clss String
     * @param role String[]
     * @param assg String[]
     * @param addn String[]
     * @param edit String[]
     * @return boolean
     * @throws Exception
     */
    public boolean updatePrivileges(String clss, String[] role, String[] assg,
                                    String[] addn, String[] edit) throws
            Exception {
        StringBuffer aq = new StringBuffer(1024);
        Statement stmt = getStatement();
        for (int i = 0; i < role.length; i++) {
            aq.append("update am_ad_class_privileges set"
                      + " role_view ='" + assg[i] + "',"
                      + " role_addn ='" + addn[i] + "',"
                      + "  role_edit ='" + edit[i] + "'"
                      + " where "
                      + " clss_uuid ='" + clss + "'"
                      + "  and "
                      + " role_uuid = '" + role[i] + "'");
            //System.out.println("[1]ManageSecurities-->UpdateP-->" + aq.toString());
            if(checkAlreadyAssigned(role[i], clss)){
                stmt.addBatch(aq.toString());
                continue;
            }
            else{
                assignnewPrivileges(clss,role[i],assg[i],addn[i],edit[i]);
                //continue;
            }
            aq = new StringBuffer(1024); ;

            /* aq.append("exec am_msp_update_privileges");
             aq.append("'");
             aq.append(clss);
             aq.append("'");
             aq.append(",");
             aq.append("'");
             aq.append(role[i]);
             aq.append("'");
             aq.append(",");
             aq.append("'");
             aq.append(assg[i]);
             aq.append("'");
             aq.append(",");
             aq.append("'");
             aq.append(addn[i]);
             aq.append("'");
             aq.append(",");
             aq.append("'");
             aq.append(edit[i]);
             aq.append("'");*/
        }
        int update[] = stmt.executeBatch();
        /* for (int i = 0; i < role.length; i++) {
         System.out.println("[2]ManageSecurities-->UpdateP-->" + update[i]);
         }*/
        freeResource();
        return (update.length > 0);
        // return (getStatement().executeUpdate(
        //   aq.toString()) == -1);
    }
    public boolean assignnewPrivileges(String clss, String role, String assg,
                                    String addn, String edit) throws
            Exception {
        StringBuffer aq = new StringBuffer(1024);
        Statement stmt = getStatement();

            aq.append("insert into am_ad_class_privileges("
                      + "clss_uuid, role_uuid, role_view, role_addn, role_edit"
                      + ") values(");

            /* aq.append("exec am_msp_assign_privileges");*/
            aq.append("'");
            aq.append(clss);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(role);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(assg);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(addn);
            aq.append("'");
            aq.append(",");
            aq.append("'");
            aq.append(edit);
            aq.append("')");
            System.out.println("[1]ManageSecurities-->AssignP-->" + aq.toString());
           // stmt.addBatch(aq.toString());
            //aq = new StringBuffer(1024);


        int update = stmt.executeUpdate(aq.toString() );
        /* for (int i = 0; i < role.length; i++) {
         System.out.println("[2]ManageSecurities-->AssignP-->" + update[i]);
         }*/
        //freeResource();
        return (update != -1);
        //getStatement().executeUpdate(
        //aq.toString()) == -1);
    }
public boolean checkAlreadyAssigned(String roleid, String cls)
    {
       boolean exist = false;
        String sql = "SELECT * FROM am_ad_class_privileges WHERE "
                     +"role_uuid='"+roleid+"' AND clss_uuid='"+cls+"'";
        try {
           ResultSet rv = getStatement().executeQuery(sql);

           exist= rv.next();

          //freeResource();
       }
       catch (Throwable t) { t.printStackTrace();}
       return exist;
    }
}
