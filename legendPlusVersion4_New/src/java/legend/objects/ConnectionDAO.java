package legend.objects;
import java.sql.*;

/**
 * <p>Title: ConnectionDAO.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technology.</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */
public interface ConnectionDAO {

    /**
     * getConnection
     *
     * @param jndiName String
     * @return Connection
     */
    public Connection getConnection(String jndiName);

    /**
     * closeConnection
     *
     * @param con Connection
     * @param rs ResultSet
     * @param ps PreparedStatement
     */
    public void closeConnection(Connection con, PreparedStatement ps,ResultSet rs);
    /**
    * closeConnection
    *
    * @param con Connection
    * @param ps PreparedStatement
    */
   public void closeConnection(Connection con,PreparedStatement ps);


    /**
     * executeQueryString
     *
     * @param query String
     */
    public void executeQueryString(String query,String jndiName);

}
