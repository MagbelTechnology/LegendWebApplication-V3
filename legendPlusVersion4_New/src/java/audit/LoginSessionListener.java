package audit;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import jakarta.servlet.http.*;

import legend.admin.handlers.AdminHandler;
import legend.admin.handlers.SecurityHandler;
import legend.admin.objects.Branch;
import legend.admin.objects.User;

// Referenced classes of package audit:
//            AuditTrailGen

public class LoginSessionListener implements HttpSessionListener
{

    private HttpSession session;
    public LoginSessionListener()
    {
        session = null;
    }
   
    public void sessionCreated(HttpSessionEvent event)
    {
        session = event.getSession();
        Random rand = new Random();
        long seed = rand.nextLong();
        String SessionID = (session.getId()) + seed;
        session.setAttribute("SessionId", SessionID);
    }
 
    public void sessionDestroyed(HttpSessionEvent event)
    {
        session = event.getSession();
        int loginID = 0;
        AuditTrailGen atg = new AuditTrailGen();
        String sessionId = (String)session.getAttribute("SessionId");
        String branchCode = (String)session.getAttribute("UserCenter");
        String username = (String)session.getAttribute("UserName");
        User identity = (User)session.getAttribute("_user");
        String loginAudit = (String)session.getAttribute("LoginAudit");
        String workstationIp = (String)session.getAttribute("workstationIp");
        String hostName = "";
        String ipAddress = ""; 
        String macAddress = "";
        InetAddress address = null;
        if(workstationIp==null){workstationIp="0:0:0:0:0:0:0:1";}
        try
        {
            address = InetAddress.getByName(workstationIp);
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
        String workstationName = address.getHostName();
        if(identity != null) {
        System.out.println( "INFO:  User Id: "+identity.getUserId());
        if(identity.getUserId()!=null){
        loginID = Integer.parseInt(identity.getUserId());
        loginID = Integer.parseInt(identity.getUserId());
        try
        {
            SecurityHandler usb = new SecurityHandler();
            AdminHandler admin = new AdminHandler();

//            System.out.println("LoginSessionListener.login Name >> "+identity.getUserFullName());
            System.out.println("LoginSessionListener.sessionId >> "+sessionId);
 //           System.out.println("LoginSessionListener.workstationIp >> "+workstationIp );
            if(loginAudit.equals("Y"))
            {
                atg.updateLoginAudit(sessionId, loginID);
            }
            atg.select ( 1, "SELECT * FROM  AM_GB_USER  WHERE userId = '"+ identity.getUserId() +"'");
            atg.getClass();
            atg.updateLogin(loginID, false, "UPDATE AM_GB_USER  SET LOGIN_STATUS= '0' WHERE USER_ID = "+identity.getUserId()+"");
            usb.updateLogins(identity.getUserId(), "0", workstationIp);
            atg.select(2, "SELECT * FROM  AM_GB_USER  WHERE userId = '"+identity.getUserId() +"'");
            atg.logAuditTrail("AM_GB_USER", admin.getBranchByBranchID(identity.getBranch()).getBranchCode(), loginID, identity.getUserId(), hostName, ipAddress,macAddress);
 //           System.out.println( "INFO:User "+identity.getUserName()+" has logged out.");
 //           System.out.println( "INFO:          user branch "+identity.getBranch() );
        } 
        catch(Throwable ex)
        {    
            ex.printStackTrace();
        }
        }
    }
    }
}
