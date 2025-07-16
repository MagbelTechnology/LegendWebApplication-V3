package legend;

import audit.AuditTrailGen;

import com.magbel.util.CheckIntegerityContraint;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.Branch;
import legend.admin.objects.BranchManager;

public class BranchMgrAuditServlet extends HttpServlet
{

    public BranchMgrAuditServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void destroy()
    {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setDateHeader("Expires", -1L);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String statusMessage = "";
        boolean updtst = false;
        boolean updtstatus = false;
        AuditTrailGen audit = new AuditTrailGen();
        String loginId = (String)session.getAttribute("CurrentUser");
        int loginID;
        if(loginId == null)
        {
            loginID = 0;
        } else
        {
            loginID = Integer.parseInt(loginId);
        }
        String branchcode = (String)session.getAttribute("UserCenter");
        
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String mtid = request.getParameter("mtid");
        String status = request.getParameter("Status");
        String buttSave = request.getParameter("buttSave");
        String managerName = request.getParameter("managerName");
        BranchManager branch = new BranchManager();
        String branchCode = request.getParameter("branchCode");
 //       String branchName = request.getParameter("branchName").toUpperCase();
         managerName = managerName.toUpperCase();
        String username = (String)session.getAttribute("CurrentUser");
        String userClass = (String)session.getAttribute("UserClass");
        //Vickie - create a new field
        String emailAddress = request.getParameter("emailAddress");
        branch.setManagerName(managerName);
        branch.setBranchCode(branchCode);
 //       branch.setBranchName(branchName);
        branch.setStatus(status);
        branch.setMtid(mtid);  
        branch.setUserId(username);
        branch.setEmailAddress(emailAddress);
        AdminHandler admin = new AdminHandler();
        
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
        System.out.println("inetAddress: " + inetAddress);
        computerName = inetAddress.getHostName();
        System.out.println("computerName: " + computerName);
        if (computerName.equalsIgnoreCase("localhost")) {
            computerName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
        } 
       String hostName = "";
       
       if (hostName.equals(request.getRemoteAddr())) {
           InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
           hostName = addr.getHostName();
           
	        }
	
	        if (InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr())) {
	                hostName = "Local Host";
	        }
	        
	        InetAddress ip;
			ip = InetAddress.getLocalHost();
			String ipAddress = ip.getHostAddress();
			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	        byte[] mac = network.getHardwareAddress();
	        if(mac == null){
	               String value = "";
	               mac = value.getBytes();
	        }
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			String macAddress = sb.toString();
			System.out.println(sb.toString());
			
        try
        {
        	if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
            {
                if(mtid.equals("")) 
                {
                    if(admin.getBranchMgrByBranchCode(branchCode) != null)
                    {
                        out.print("<script>alert('The branch code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(admin.createBranchManager(branch))
                    { 
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchManagerSetup&mtid=")).append(admin.getBranchMgrByBranchCode(branchCode).getMtid()).append("&PC=8';</script>").toString());
                    }
                } else
                if(!mtid.equals(""))
                {
                    branch.setMtid(mtid);
               //     CheckIntegerityContraint intCont = new CheckIntegerityContraint();
           //         if(intCont.checkReferenceConstraint("AM_GB_USERS", "BRANCH_CODE", branch.getBranchCode(), status))
              //      {
             //       out.print("<script>alert('This Branch Code is being referenced by other records it thus cannot by closed.')</script>");
             //           out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchManagerSetup&branchId=")).append(mtid).append("&PC=8'</script>").toString());
             //       } else
                 //   {
                        audit.select(1, (new StringBuilder("SELECT * FROM  am_branch_Manager   WHERE MTID = '")).append(mtid).append("'").toString());
                        boolean isupdt = admin.updateBranchManager(branch);
                        audit.select(2, (new StringBuilder("SELECT * FROM  am_branch_Manager  WHERE MTID = '")).append(mtid).append("'").toString());
                        updtst = audit.logAuditTrail("am_branch_Manager ", branchcode, loginID, mtid,hostName,ipAddress,macAddress);
                        if(updtst)
                        {
                            out.print("<script>alert('Update on record is successfull')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchManagerSetup&mtid=")).append(mtid).append("&PC=8'</script>").toString());
                        } else
                        {
                            out.print("<script>alert('No changes made on record')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchManagerSetup&mtid=")).append(mtid).append("'</script>").toString());
                        }
                  //  }
                }
            }
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchManagerSetup&branchId=")).append(mtid).append("'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}
