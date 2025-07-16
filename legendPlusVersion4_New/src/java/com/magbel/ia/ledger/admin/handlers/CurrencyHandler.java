package legend.admin.handlers;


import java.sql.*;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;

public class CurrencyHandler {
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date date;
    com.magbel.util.DatetimeFormat df;
    public CurrencyHandler() {

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
        System.out.println("USING_ " + this.getClass().getName());
    }

    public java.util.ArrayList getAllExchangeRate() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.ExchangeRate exchange = null;
        String query = "SELECT MTID,currency_id,create_dt"
                       + ",effective_dt,exchg_rate"
                       + ",user_id ,status,method"
                       + " FROM AM_GB_EXCH_RATE";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c= getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String rate = rs.getString("exchg_rate");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String effdate = sdf.format(rs.getDate("effective_dt"));
                String userid = rs.getString("user_id");
                String mtid = rs.getString("MTID");
                String status = rs.getString("status");
                String method = rs.getString("method");
                exchange = new legend.admin.objects.ExchangeRate();
                exchange.setCreate_dt(createdt);
                exchange.setCurrency_id(currid);
                exchange.setEffective_dt(effdate);
                exchange.setExchg_rate(rate);
                exchange.setMTID(mtid);
                exchange.setStatus(status);
                exchange.setUser_id(userid);
                exchange.setMethod(method);
                _list.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getExchangeRateByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.ExchangeRate exchange = null;
        String query = "SELECT MTID,currency_id,create_dt"
                       + ",effective_dt,exchg_rate"
                       + ",user_id ,status,method"
                       +
                       " FROM AM_GB_EXCH_RATE WHERE currency_id IS NOT NULL ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String rate = rs.getString("exchg_rate");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String effdate = sdf.format(rs.getDate("effective_dt"));
                String userid = rs.getString("user_id");
                String mtid = rs.getString("MTID");
                String status = rs.getString("status");
                 String method = rs.getString("method");
                exchange = new legend.admin.objects.ExchangeRate();
                exchange.setCreate_dt(createdt);
                exchange.setCurrency_id(currid);
                exchange.setEffective_dt(effdate);
                exchange.setExchg_rate(rate);
                exchange.setMTID(mtid);
                exchange.setStatus(status);
                exchange.setMethod(method);
                 exchange.setUser_id(userid);
                _list.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.ExchangeRate getExchangeRateByCurrID(String curryid) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.ExchangeRate exchange = null;
        String query = "SELECT MTID,currency_id,create_dt"
                       + ",effective_dt,exchg_rate"
                       + ",user_id ,status,method"
                       + " FROM AM_GB_EXCH_RATE WHERE currency_id=" + curryid;

        //query = query+filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String rate = rs.getString("exchg_rate");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String effdate = sdf.format(rs.getDate("effective_dt"));
                String userid = rs.getString("user_id");
                String mtid = rs.getString("MTID");
                String status = rs.getString("status");
                String method = rs.getString("method");
                exchange = new legend.admin.objects.ExchangeRate();
                exchange.setCreate_dt(createdt);
                exchange.setCurrency_id(currid);
                exchange.setEffective_dt(effdate);
                exchange.setExchg_rate(rate);
                exchange.setMTID(mtid);
                exchange.setStatus(status);
                exchange.setUser_id(userid);
                 exchange.setMethod(method);
                // _list.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return exchange;

    }

    public legend.admin.objects.ExchangeRate getExchangeRateByMTID(String curryid) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.ExchangeRate exchange = null;
        String query = "SELECT MTID,currency_id,create_dt"
                       + ",effective_dt,exchg_rate"
                       + ",user_id ,status,method"
                       + " FROM AM_GB_EXCH_RATE WHERE MTID=" + curryid;

        //query = query+filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String rate = rs.getString("exchg_rate");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String effdate = sdf.format(rs.getDate("effective_dt"));
                String userid = rs.getString("user_id");
                String mtid = rs.getString("MTID");
                String status = rs.getString("status");
                String method = rs.getString("method");
                exchange = new legend.admin.objects.ExchangeRate();
                exchange.setCreate_dt(createdt);
                exchange.setCurrency_id(currid);
                exchange.setEffective_dt(effdate);
                exchange.setExchg_rate(rate);
                exchange.setMTID(mtid);
                exchange.setStatus(status);
                exchange.setUser_id(userid);
                 exchange.setMethod(method);
                // _list.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return exchange;

    }

    public java.util.ArrayList getAllCurrency() {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       + " FROM AM_GB_CURRENCY_CODE";

        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
           c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String isocode = rs.getString("iso_code");
                String currsymbol = rs.getString("currency_symbol");
                String description = rs.getString("description");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String localcurr = rs.getString("local_currency");
                String userid = rs.getString("user_id");
                String mtid = rs.getString("mtid");
                String status = rs.getString("status");

                curcode = new legend.admin.objects.CurrencyCode(currid, isocode,
                        currsymbol, description, status);
                curcode.setCreate_dt(createdt);
                curcode.setMtid(mtid);
                curcode.setLocal_currency(localcurr);
                curcode.setUser_id(userid);
                _list.add(curcode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getCurrencyByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       +
                " FROM AM_GB_CURRENCY_CODE WHERE currency_id IS NOT NULL ";
        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s =c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String isocode = rs.getString("iso_code");
                String currsymbol = rs.getString("currency_symbol");
                String description = rs.getString("description");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String localcurr = rs.getString("local_currency");
                String userid = rs.getString("user_id");
                String mtid = rs.getString("mtid");
                String status = rs.getString("status");

                curcode = new legend.admin.objects.CurrencyCode(currid, isocode,
                        currsymbol, description, status);
                curcode.setCreate_dt(createdt);
                curcode.setMtid(mtid);
                curcode.setLocal_currency(localcurr);
                curcode.setUser_id(userid);
                _list.add(curcode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public legend.admin.objects.CurrencyCode getCurrencyByCurrID(String currencyid) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       + " FROM AM_GB_CURRENCY_CODE WHERE currency_id=" +
                       currencyid;
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String isocode = rs.getString("iso_code");
                String currsymbol = rs.getString("currency_symbol");
                String description = rs.getString("description");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String localcurr = rs.getString("local_currency");
                String userid = rs.getString("user_id");
                String mtid = rs.getString("mtid");
                String status = rs.getString("status");

                curcode = new legend.admin.objects.CurrencyCode(currid, isocode,
                        currsymbol, description, status);
                curcode.setCreate_dt(createdt);
                curcode.setMtid(mtid);
                curcode.setLocal_currency(localcurr);
                curcode.setUser_id(userid);
                // _list.add(curcode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return curcode;

    }

    public legend.admin.objects.CurrencyCode findLocalCurrency() {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       +
                       " FROM AM_GB_CURRENCY_CODE WHERE local_currency='Y'";
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String isocode = rs.getString("iso_code");
                String currsymbol = rs.getString("currency_symbol");
                String description = rs.getString("description");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String localcurr = rs.getString("local_currency");
                String userid = rs.getString("user_id");
                String mtid = rs.getString("mtid");
                String status = rs.getString("status");

                curcode = new legend.admin.objects.CurrencyCode(currid, isocode,
                        currsymbol, description, status);
                curcode.setCreate_dt(createdt);
                curcode.setMtid(mtid);
                curcode.setLocal_currency(localcurr);
                curcode.setUser_id(userid);
                // _list.add(curcode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return curcode;

    }

    public legend.admin.objects.CurrencyCode getCurrencyByMTID(String mtidy) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       + " FROM AM_GB_CURRENCY_CODE WHERE mtid=" + mtidy;
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String currid = rs.getString("currency_id");
                String isocode = rs.getString("iso_code");
                String currsymbol = rs.getString("currency_symbol");
                String description = rs.getString("description");
                String createdt = sdf.format(rs.getDate("create_dt"));
                String localcurr = rs.getString("local_currency");
                String userid = rs.getString("user_id");
                String mtid = rs.getString("mtid");
                String status = rs.getString("status");

                curcode = new legend.admin.objects.CurrencyCode(currid, isocode,
                        currsymbol, description, status);
                curcode.setCreate_dt(createdt);
                curcode.setMtid(mtid);
                curcode.setLocal_currency(localcurr);
                curcode.setUser_id(userid);
                // _list.add(curcode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return curcode;

    }

    public boolean CurrencyIDExists(String mtidy) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT currency_id,iso_code,currency_symbol"
                       +
                       " ,description,status,create_dt,local_currency"
                       + " ,user_id,mtid"
                       + " FROM AM_GB_CURRENCY_CODE WHERE currency_id=" +
                       mtidy;
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        boolean exists = false;
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            exists = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return exists;

    }

    public boolean ExCurrencyIDExists(String mtidy) {
        //java.util.ArrayList _list = new java.util.ArrayList();
        legend.admin.objects.CurrencyCode curcode = null;
        String query = "SELECT *"
                       + " FROM AM_GB_EXCH_RATE WHERE currency_id=" + mtidy;
        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        boolean exists = false;
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            exists = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(c, s, rs);
        }
        return exists;

    }

    private Connection getConnection() {
        Connection con = null;
        dc = new DataConnect("fixedasset");
        try {
            con = dc.getConnection();
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }
        return con;
    }

    private void closeConnection(Connection con, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" +
                               e.getMessage());
        }

    }

    private void closeConnection(Connection con, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }

    }

    /**
     *
     * @param con Connection
     * @param s Statement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, Statement s, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    /**
     *
     * @param con Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    private void closeConnection(Connection con, PreparedStatement ps,
                                 ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" +
                               e.getMessage());
        }
    }

    private boolean executeQuery(String query) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    /**
     * createCurrency
     *
     */
    /*
    public boolean createCurrency(legend.admin.objects.CurrencyCode ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query =
                "INSERT INTO AM_GB_CURRENCY_CODE(currency_id,iso_code"
                + ",currency_symbol ,description,status,create_dt,"
                + "local_currency ,user_id,MTID) "
                + " VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getCurrency_id());
            ps.setString(2, ccode.getIso_code());
            ps.setString(3, ccode.getCurrency_symbol());
            ps.setString(4, ccode.getDescription());
            ps.setString(5, ccode.getStatus());
            ps.setDate(6, df.dateConvert(new java.util.Date()));
            ps.setString(7, ccode.getLocal_currency());
            ps.setString(8, ccode.getUser_id());
            ps.setLong(9, System.currentTimeMillis());
            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
*/
    public boolean updateCurrency(legend.admin.objects.CurrencyCode ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

       
        String query = "UPDATE AM_GB_CURRENCY_CODE"
                       + " SET currency_id = ?,iso_code = ?"
                       + ",currency_symbol = ?,description = ?"
                       + ",status = ?"
                       + ",local_currency = ?"
                       + " WHERE mtid = ?";
        
               
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getCurrency_id());
            ps.setString(2, ccode.getIso_code());
            ps.setString(3, ccode.getCurrency_symbol());
            ps.setString(4, ccode.getDescription());
            ps.setString(5, ccode.getStatus());
            // ps.setDate(6,df.dateConvert(new java.util.Date()));
            ps.setString(6, ccode.getLocal_currency());
            ps.setString(7, ccode.getMtid());
            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    /*
    public boolean createExchangeRate(legend.admin.objects.ExchangeRate ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO AM_GB_EXCH_RATE"
                       + "(currency_id,create_dt,effective_dt"
                       + ",exchg_rate,user_id,status,method,MTID)"
                       + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getCurrency_id());
            ps.setDate(2, df.dateConvert(new java.util.Date()));
            ps.setDate(3, df.dateConvert(ccode.getEffective_dt()));
            ps.setString(4, ccode.getExchg_rate());
            ps.setString(5, ccode.getUser_id());
            ps.setString(6, ccode.getStatus());
            ps.setString(7, ccode.getMethod());
            ps.setLong(8, System.currentTimeMillis());
            
            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }
*/
    public boolean updateExchangeRate(legend.admin.objects.ExchangeRate ccode,
                                      boolean dirty) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        if (dirty) {
            logExchangeRate(ccode.getCurrency_id());
        }
        String query = "UPDATE AM_GB_EXCH_RATE"
                       +
                       " SET effective_dt = ?,exchg_rate = ?,status = ?"
                       + ",method = ?  WHERE currency_id =?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            //ps.setDate(1,df.dateConvert(new java.util.Date()));
            ps.setDate(1, df.dateConvert(ccode.getEffective_dt()));
            ps.setString(2, ccode.getExchg_rate());
            //ps.setString(4,ccode.getUser_id());
            ps.setString(3, ccode.getStatus());
             ps.setString(4, ccode.getMethod());
            ps.setString(5, ccode.getCurrency_id());

            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean logExchangeRate(String cid) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        legend.admin.objects.ExchangeRate ccode = new legend.admin.objects.ExchangeRate();
        ccode = this.getExchangeRateByCurrID(cid);
        String query = "INSERT INTO AM_EXCH_RATE_HISTORY"
                       + "(currency_id,create_dt,effective_dt"
                       + ",exchg_rate,user_id,status,mtid)"
                       + " VALUES (?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getCurrency_id());
            ps.setDate(2, df.dateConvert(new java.util.Date()));
            ps.setDate(3, df.dateConvert(ccode.getEffective_dt()));
            ps.setString(4, ccode.getExchg_rate());
            ps.setString(5, ccode.getUser_id());
            ps.setString(6, ccode.getStatus());
            ps.setString(7, ccode.getMTID());

            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }




public boolean createExchangeRate(legend.admin.objects.ExchangeRate ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO AM_GB_EXCH_RATE"
                       + "(currency_id,create_dt,effective_dt"
                       + ",exchg_rate,user_id,status,method)"
                       + " VALUES (?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getCurrency_id());
            ps.setDate(2, df.dateConvert(new java.util.Date()));
            ps.setDate(3, df.dateConvert(ccode.getEffective_dt()));
            ps.setString(4, ccode.getExchg_rate());
            ps.setString(5, ccode.getUser_id());
            ps.setString(6, ccode.getStatus());
            ps.setString(7, ccode.getMethod());
           // ps.setLong(8, System.currentTimeMillis());

            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query createExchangeRate() ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }



 public boolean createCurrency(legend.admin.objects.CurrencyCode ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        
        String query =
                "INSERT INTO AM_GB_CURRENCY_CODE(currency_id,iso_code"
                + ",currency_symbol ,description,status,create_dt,"
                + "local_currency ,user_id) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(ccode.getCurrency_id()));
            ps.setString(2, ccode.getIso_code());
            ps.setString(3, ccode.getCurrency_symbol());
            ps.setString(4, ccode.getDescription());
            ps.setString(5, ccode.getStatus());
            ps.setDate(6, df.dateConvert(new java.util.Date()));
            ps.setString(7, ccode.getLocal_currency());
            ps.setString(8, ccode.getUser_id());
            //ps.setLong(9, System.currentTimeMillis());
            done = (ps.executeUpdate()!=-1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }



}
