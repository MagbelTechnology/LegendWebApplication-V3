package com.magbel.mailapp;

import com.magbel.util.ConnectManager;
import java.io.PrintStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.magbel.mailapp:
//            MessageValue, HostDBException, transport

public class HelpDesk
{

    String errorMsg;

    public HelpDesk()
    {
        errorMsg = "";
    }

    private String getUserEmail(int userId)
    {
        Connection con = null;
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String qry = "SELECT EMAIL FROM AM_GB_USER WHERE USER_ID = " + userId;
        String result = "";
        try
        {
            con = (new ConnectManager()).getConnection();
            ps = con.prepareStatement(qry);
            for(rs = ps.executeQuery(); rs.next();)
            {
                result = rs.getString("email");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(rs != null)
                {
                    rs.close();
                }
                if(ps != null)
                {
                    ps.close();
                }
                if(con != null)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("WARNING::Error Closing Connection " + e);
            }
        }
        return result;
    }

    private String msgToSend(String msgBody)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuffer MsgBody = new StringBuffer();
        String title1 = "sir/maddam";
        MsgBody.append("<HTML>");
        MsgBody.append("<HEAD>");
        MsgBody.append("<TITLE>Magbel Technology Limited Notification System</TITLE>");
        MsgBody.append("</HEAD>");
        MsgBody.append("<BODY>");
        MsgBody.append("<table><tr></td>");
        MsgBody.append("<tr><td> Dear " + title1 + "<br>");
        MsgBody.append(" *********************************************************<br>");
        MsgBody.append(" " + sdf.format(new Date()) + "  " + DateFormat.getTimeInstance().format(new Date()) + "<br>");
        MsgBody.append(" ********************************************************* <br>");
        MsgBody.append(" " + msgBody + "<br><br><br>");
        MsgBody.append("  Regards, <br><br>");
        MsgBody.append(" MAGBEL TECH. LTD</td></tr>");
        MsgBody.append("</table>");
        MsgBody.append("</BODY>");
        MsgBody.append("</HTML>");
        String msgToSend = MsgBody.toString();
        return msgToSend;
    }

    private void sendHelpDeskMail(int fromUserId, int toUserId, String subject, String messageBody, String cc)
        throws HostDBException
    {
        String fromEmail_ = getUserEmail(fromUserId).trim();
        String toEmail_ = getUserEmail(toUserId).trim();
        String msgBody = msgToSend(messageBody);
        try
        {
            MessageValue msgValue = new MessageValue();
            msgValue.setMailSubject(subject);
            msgValue.setMailMessage(msgBody);
            msgValue.setMailTo(toEmail_);
            msgValue.setMailCC(cc);
            msgValue.setMailFrom(fromEmail_);
            transport.doSend(msgValue, "magbel.txt");
        }
        catch(HostDBException ex)
        {
            System.out.print("sendHelpDeskMail(????) failed : ");
            ex.getMessage();
        }
    }

    public String helpDeskMailService(int fromUserId, int toUserEmail, String subject, String messageBody, String cc)
    {
        try
        {
            sendHelpDeskMail(fromUserId, toUserEmail, subject, messageBody, cc);
        }
        catch(HostDBException ex)
        {
            System.out.println("Help Desk Notification: " + ex.getMessage());
            errorMsg = ex.getMessage();
            System.exit(-1);
        }
        catch(Exception ex)
        {
            System.out.println("Help Desk Notification: " + ex.getMessage());
            errorMsg = ex.getMessage();
            System.exit(-1);
        }
        return errorMsg;
    }
}