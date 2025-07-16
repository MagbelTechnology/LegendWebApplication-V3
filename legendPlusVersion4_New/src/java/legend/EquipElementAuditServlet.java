package legend;

import audit.AuditTrailGen;

import com.magbel.util.ApplicationHelper;
import com.magbel.util.Cryptomanager;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.equipmentElement;
import legend.admin.objects.vendorCriteria;

public class EquipElementAuditServlet extends HttpServlet
{

    public EquipElementAuditServlet()
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
        AuditTrailGen audit = new AuditTrailGen();
        String statusMessage = "";
        boolean updtst = false;
        String loginId = (String)session.getAttribute("CurrentUser");
        
        String userClass = (String)session.getAttribute("UserClass");
        
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
        Cryptomanager cm = new Cryptomanager();
        String reasonId = request.getParameter("Id");
        String elementId = request.getParameter("Id");
        CompanyHandler ch = new CompanyHandler();
        String buttSave = request.getParameter("buttSave");
        String description = request.getParameter("description");
        System.out.println("=====description: "+description);
        String status = request.getParameter("status");
        System.out.println("=====status: "+status);
        String userId = (String)session.getAttribute("CurrentUser");
        equipmentElement crite = new equipmentElement();
        crite.setDescription(description);
        crite.setStatus(status);
        crite.setUserId(userId);
        String computerName = null;
        String remoteAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddress);
 //       System.out.println((new StringBuilder("inetAddress: ")).append(inetAddress).toString());
        computerName = inetAddress.getHostName();
//        c(new StringBuilder("computerName: ")).append(computerName).toString());
        if(computerName.equalsIgnoreCase("localhost"))
        {
            computerName = InetAddress.getLocalHost().getCanonicalHostName();
        }
        String hostName = "";
        if(hostName.equals(request.getRemoteAddr()))
        {
            InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
            hostName = addr.getHostName();
        }
        if(InetAddress.getLocalHost().getHostAddress().equals(request.getRemoteAddr()))
        {
            hostName = "Local Host";
        }
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        System.out.println((new StringBuilder("Current IP address : ")).append(ip.getHostAddress()).toString());
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte mac[] = network.getHardwareAddress();
        if(mac == null){
            String value = "";
            mac = value.getBytes();
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mac.length; i++)
        {
            sb.append(String.format("%02X%s", new Object[] {
                Byte.valueOf(mac[i]), i >= mac.length - 1 ? "" : "-"
            }));
        }

        String macAddress = sb.toString();
        System.out.println(sb.toString());
 //       System.out.println((new StringBuilder("======>hostName: ")).append(hostName).append("   ipAddress: ").append(ipAddress).toString());
        try
        { 
         if (!userClass.equals("NULL") || userClass!=null){
            if(buttSave != null)
            {
            	System.out.println("========>>>>>>elementId: "+elementId);
                if(elementId.equals(""))
                {
                    if(reasonId.equals("")){reasonId = new ApplicationHelper().getGeneratedId("FM_EQUIPMENT_ELEMENT");}
                     System.out.println("========>>>>>>reasonId: "+reasonId);
                    if(ch.getequipmentElementById(reasonId) != null)
                    {
                        out.print("<script>alert('Reason Code is in Use')</script>");
                       out.print("<script>history.go(-1)</script>");
                    } else
                    if(ch.createequipmentElement(crite,reasonId))
                    {
                        out.print("<script>alert('Record saved successfully.')</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=equipmentElementSetup&reasonId=")).append(ch.getequipmentElementById(reasonId).getId()).append("'</script>").toString());
                    } else
                    {
                        System.out.println((new StringBuilder("Error saving record: New record \nfor 'Equipment Element'  with reason code ")).append(crite.getId()).append(" could not be created").toString());
                        out.print("<script>history.go(-1)</script>");
                    }
                } else
                if(!elementId.equals(""))
                {
                    crite.setId(reasonId);
                    audit.select(1, (new StringBuilder("SELECT * FROM  FM_EQUIPMENT_ELEMENT   WHERE Id = '")).append(reasonId).append("'").toString());
                    boolean isupdt = ch.updateequipmentElement(crite);
                    audit.select(2, (new StringBuilder("SELECT * FROM  FM_EQUIPMENT_ELEMENT   WHERE Id = '")).append(reasonId).append("'").toString());
                    updtst = audit.logAuditTrail("FM_EQUIPMENT_ELEMENT", branchcode, loginID, reasonId, hostName, ipAddress, macAddress);
                    if(updtst)
                    {
                        out.print("<script>alert('Update on record is successfull')</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=equipmentElementSetup&Id=")).append(reasonId).append("'</script>").toString());
                    } else
                    {
                        out.print("<script>alert('No changes made on record')</script>");
                        out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=equipmentElementSetup&Id=")).append(reasonId).append("'</script>").toString());
                    }
                }
            }
        }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder("<script>window.location = 'DocumentHelp.jsp?np=equipmentElementSetup&Id=")).append(reasonId).append("'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}
