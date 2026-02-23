
package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import magma.net.dao.MagmaDBConnection;

public class AccountPrefixCache {

	 static MagmaDBConnection dbConnection = new MagmaDBConnection();
    private static final Map<String, String> prefixCache = new ConcurrentHashMap<>();

    
    private AccountPrefixCache() {}

    /**
     * Get the SQL prefix script for a given type from cache.
     * If not present, fetch from DB and cache it.
     *
     * @param type e.g., "LI", "AS3", "AS4"
     * @return the SQL prefix string
     */
    public static String get(String type) {
        if (prefixCache.containsKey(type)) {
            return prefixCache.get(type);
        }
        String prefix = fetchPrefixFromDB(type);
        if (prefix != null) {
            prefixCache.put(type, prefix);
            return prefix;
        }
        return ""; 
    }
    /**
     * Fetch prefix from DB
     */
    private static String fetchPrefixFromDB(String type) {
        String result = null;
        String query = "SELECT PREFIX FROM ACCOUNT_GLPREFIX_PARAM WHERE type = ?";
        try (Connection con = dbConnection.getConnection("legendPlus");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString("PREFIX");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error fetching prefix for type " + type + ": " + ex);
        }

        return result;
    }

    
    public static void clear() {
        prefixCache.clear();
    }
}
