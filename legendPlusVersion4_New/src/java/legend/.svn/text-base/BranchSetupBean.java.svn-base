package legend;

import java.sql.*;

import audit.AuditInfo;
import audit.AuditTrailGen;

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
 * @version 1.0
 */
public class BranchSetupBean extends ConnectionClass {
    private String[] branches = new String[12];
    private AuditInfo ai = new AuditInfo();
    private AuditTrailGen atg = new AuditTrailGen();

    public final void setAuditInfo(AuditInfo AI) {
        this.ai = AI;
    }

    public final void setAuditInfo(String TableName, String BranchCode,
                                   int LoginId, String RowId,
                                   boolean ReqInsertAudit) {
        ai.setTableName(TableName);
        ai.setBranchCode(BranchCode);
        ai.setLoginId(LoginId);
        ai.setRowId(RowId);
        ai.setReqInsertAudit(ReqInsertAudit);
    }

    public final AuditInfo getAuditInfo() {
        return this.ai;
    }

    public BranchSetupBean() throws Throwable {
    }

    public void setBranches(String[] branches) {
        if (branches != null) {
            this.branches = branches;
        }
    }

    public String[] getBranches() {
        return branches;
    }

    /**
     * selectbranches
     *
     * @param con String
     * @param sta String
     * @return String[][]
     * @throws Throwable
     */
    public String[][] selectBranches(String con, String sta) throws Throwable {
        String statusQuery = "SELECT COUNT(*) FROM AM_AD_BRANCH WHERE BRANCH_STATUS = '"+sta+"'";
         String idQuery = "SELECT COUNT(*) FROM AM_AD_BRANCH WHERE BRANCH_ID = "+con;

         String selQuerySatus = "select * from am_ad_branch where branch_status = '"+sta+"' "+
                                                         "ORDER BY BRANCH_NAME ASC";


         String selQueryId = "SELECT * FROM AM_AD_BRANCH WHERE BRANCH_ID = "+con;
         PreparedStatement ps = null;
         ResultSet rc = null;
         if(con.equals("0")){
                 ps = getConnection().prepareStatement(statusQuery);
                 rc = ps.executeQuery();
         }else if(con != ""){
             ps = getConnection().prepareStatement(idQuery);
             rc = ps.executeQuery();
              
         }else{}

         ResultSet rv = null;

         if(con.equals("0")){
             ps = getConnection().prepareStatement(selQuerySatus);
             rv = ps.executeQuery();
             
         }else if(con != ""){
             ps = getConnection().prepareStatement(selQueryId);
             rv = ps.executeQuery();
              //  rv = getStatement().executeQuery(selQueryId);
         }else{}

            rc.next();
         String[][] values = new String[rc.getInt(1)][12];

         for (int x = 0; x < values.length; x++) {
           rv.next();
             for (int y = 0; y < 12; y++) {
               values[x][y] = rv.getString(y + 1);
               //System.out.println("value in branch : "+values[x][y]);
            }
         }

        return values;
    }

    /**
     * updatebranches
     *
     * @param con String
     * @return boolean
     * @throws Throwable
     */
    public boolean updateBranches(String con) throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_update_branches");
        iq.append("'");
        iq.append(con);
        iq.append("'");

        for (int i = 0; i < 12; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(branches[i]);
                break;
            default:
                iq.append(",'");
                iq.append(branches[i]);
                iq.append("'");
            }
        }

        int ret = getStatement().executeUpdate(
                iq.toString());
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * insertbranches
     *
     * @return boolean
     * @throws Throwable
     */
    public boolean insertBranches() throws Throwable {
        StringBuffer iq = new StringBuffer(100);
        iq.append("am_msp_insert_branches");
        iq.append("'");
        iq.append(branches[0]);
        iq.append("'");

        for (int i = 1; i <= 11; i++) {
            switch (i) {
            case -1:
                iq.append(", ");
                iq.append(branches[i]);
                break;
            default:
                iq.append(",'");
                iq.append(branches[i]);
                iq.append("'");
            }
        }
        if (ai.reqInsertAudit() == true) {
            atg.select(1, "SELECT * FROM AM_AD_BRANCH WHERE branch_Id = (SELECT MIN(branch_Id) FROM AM_AD_BRANCH)");
            atg.captureAuditFields(branches);
            atg.logAuditTrail(ai);
        }
        return (getStatement().executeUpdate(
                iq.toString()) == -1);
    }
}
