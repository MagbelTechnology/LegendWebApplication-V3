package magma.net.dao;

import java.sql.*;

/**
 * <p>Title: eConnection.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public interface eConnection {
    /**
     * getConnection
     *
     * @return Connection
     */
    public Connection getConnection(String jndi);

    /**
     * closeConnection
     *
     * @param con Connection
     * @param ps PreparedStatement
     */
    public void closeConnection(Connection con, PreparedStatement ps);

    /**
     * closeConnection
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    public void closeConnection(Connection con, PreparedStatement ps,
                                ResultSet rs);

}
