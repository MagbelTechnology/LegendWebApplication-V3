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

public class BranchAuditServlet extends HttpServlet
{

    public BranchAuditServlet()
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
        String userClass = (String) session.getAttribute("UserClass");
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String branchId = request.getParameter("branchId");
        String branchStatus = request.getParameter("branchStatus");
        String buttSave = request.getParameter("buttSave");
        String acronym = request.getParameter("branchAcronym");
        if(acronym != null)
        {
            acronym = acronym.toUpperCase();
        }
        Branch branch = new Branch();
        String branchCode = request.getParameter("branchCode");
        String branchName = encodeForHTML(request.getParameter("branchName").toUpperCase());
        String branchAcronym = acronym.toUpperCase();
        String glPrefix = encodeForHTML(request.getParameter("glPrefix").toUpperCase());
        String branchAddress = request.getParameter("branchAddress").toUpperCase();
        String state = request.getParameter("branchState");
        String phoneNo = encodeForHTML(request.getParameter("branchPhone"));
        String faxNo = encodeForHTML(request.getParameter("branchFax"));
        String region = request.getParameter("branchRegion");
        String zoneCode = request.getParameter("branchZone");
        String province = request.getParameter("branchProvince");
        String username = (String)session.getAttribute("CurrentUser");
        //Vickie - create a new field
        String emailAddress = request.getParameter("emailAddress").toUpperCase();
        //System.out.println("VIIIIICKKKKIEEEEE: Its getting the value from the field " + emailAddress);
        String Uncapitalized = encodeForHTML(request.getParameter("Uncapitalized"));

        branch.setBranchAcronym(branchAcronym);
        branch.setBranchAddress(branchAddress);
        branch.setBranchCode(branchCode);
        branch.setBranchName(branchName);
        branch.setBranchStatus(branchStatus);
        branch.setFaxNo(faxNo);
        branch.setGlPrefix(glPrefix);
        branch.setPhoneNo(phoneNo);
        branch.setProvince(province);
        branch.setRegion(region);
        branch.setState(state);
        branch.setUsername(username);
        branch.setEmailAddress(emailAddress);
        branch.setUnClassified(Uncapitalized);
        branch.setZoneCode(zoneCode);
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
                if(branchId.equals("")) 
                {
                    if(admin.getBranchByBranchCode(branchCode) != null)
                    {
                        out.print("<script>alert('The branch code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(admin.createBranch(branch))
                    {
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(admin.getBranchByBranchCode(branchCode).getBranchId()).append("&PC=8';</script>").toString());
                    }
                } else
                if(!branchId.equals(""))
                {
                    branch.setBranchId(branchId);
                    CheckIntegerityContraint intCont = new CheckIntegerityContraint();
                    if(intCont.checkReferenceConstraint("AM_GB_USERS", "BRANCH_CODE", branch.getBranchCode(), branchStatus))
                    {
                        out.print("<script>alert('This Branch Code is being referenced by other records it thus can" +
"not by closed.')</script>"
);
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("&PC=8'</script>").toString());
                    } else
                    {
                        audit.select(1, (new StringBuilder("SELECT * FROM  AM_AD_BRANCH   WHERE branch_Id = '")).append(branchId).append("'").toString());
                        boolean isupdt = admin.updateBranch(branch);
                        audit.select(2, (new StringBuilder("SELECT * FROM  AM_AD_BRANCH  WHERE branch_Id = '")).append(branchId).append("'").toString());
                        updtst = audit.logAuditTrail("AM_AD_BRANCH ", branchcode, loginID, branchId,hostName,ipAddress,macAddress);
                        if(updtst)
                        {
                            out.print("<script>alert('Update on record is successful')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("&PC=8'</script>").toString());
                        } else
                        {
                            out.print("<script>alert('No changes made on record')</script>");
                            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("'</script>").toString());
                        }
                    }
                }
            }
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=branchSetup&branchId=")).append(branchId).append("'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
    
    private String encodeForHTML(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
}
}
