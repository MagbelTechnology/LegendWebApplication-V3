package legend;

import java.sql.PreparedStatement;

public class AuditTrail extends ConnectionClass {
    public AuditTrail() throws Throwable {}

    public void logAuditInfo(AuditElement auditElement /*,
                                          java.sql.Date effectiveDate*/) throws
            Exception {
        PreparedStatement ps = null;
        String index = Integer.toString((int) (Math.random() * 8000) + 1000);
        try {
            final String audit_query = "INSERT INTO AM_AD_AUDIT_TRAIL(" +
                                       "AUDIT_INDEX,ACT_PERFMD,OBJ_NAME,PRE_VALUE,CUR_VALUE," +
                                       "EFF_DATE,LOGIN_ID)  values(?,?,?,?,?,getDate(),?)";
            ps = getConnection().prepareStatement(audit_query);
            ps.setString(1, index);
            ps.setString(2, auditElement.getActionPerformed());
            ps.setString(3, auditElement.getName());
            ps.setString(4, auditElement.getOldValue());
            ps.setString(5, auditElement.getNewValue());
            //ps.setDate(6,effectiveDate);
            ps.setString(6, auditElement.getUser());
            ps.execute();

        } catch (Exception ex) {
            System.out.println("WARNING::Error log audit info -->" + ex);
        }
    }
}
