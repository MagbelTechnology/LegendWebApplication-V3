package legend.util;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import sun.misc.BASE64Encoder;

public class ConnectionClass
{

    private Connection conn;
    private String jndiName; 

    public ConnectionClass()
        throws Exception
    {
        conn = null;
        jndiName = "legendPlus";
        Context ic = new InitialContext();
        DataSource ds = (DataSource)ic.lookup((new StringBuilder()).append("java:comp/env/jdbc/").append(jndiName).toString());
        conn = ds.getConnection();
        System.out.println("Using DBConnection ...");
    }

    public Connection getConnection()
        throws Exception
    {
        return conn;
    }

    public Statement getStatement()
        throws Exception
    {
        return conn.createStatement();
    }

    public String getIdentity()
    {
        return UUID.randomUUID().toString();
    }

    public String getProperties(String sele, String vals[][])
    {
        String html = new String();
        if(sele == null)
        {
            sele = " ";
        }
        for(int i = 0; i < vals.length; i++)
        {
            html = (new StringBuilder()).append(html).append("<option value='").append(vals[i][0]).append("' ").append(vals[i][0].equalsIgnoreCase(sele) ? " selected " : "").append(">").append(vals[i][2]).append("</option> ").toString();
        }

        return html;
    }

    public String getProperties(String sele, String iden[], String vals[])
    {
        String html = new String();
        if(sele == null)
        {
            sele = " ";
        }
        for(int i = 0; i < iden.length; i++)
        {
            html = (new StringBuilder()).append(html).append("<option value='").append(iden[i]).append("' ").append(iden[i].equalsIgnoreCase(sele) ? " selected " : "").append(">").append(vals[i]).append("</option> ").toString();
        }

        return html;
    }

    public String getEncrypted(String input)
        throws Throwable
    {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.toString().getBytes("UTF-16"));
        return (new BASE64Encoder()).encode(md.digest());
    }
}