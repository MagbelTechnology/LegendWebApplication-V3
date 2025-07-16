package com.magbel.ia.servlet;

import audit.AuditTrailGen;

import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.bus.SecurityServiceBus;
import com.magbel.ia.vao.Company;
import com.magbel.ia.vao.User;
import com.magbel.util.Cryptomanager;
import com.magbel.ia.util.*;

import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class UserAuditServlet extends HttpServlet
{

    public UserAuditServlet()
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
        User user = new User();
        SecurityServiceBus security = new SecurityServiceBus();
        AdminServiceBus company = new AdminServiceBus();
        ApplicationHelper2 applHelper = new ApplicationHelper2();
        int min = company.findCompany().getMinimumPassword();
        int passexpiry = company.findCompany().getPasswordExpiry();
        AuditTrailGen audit = new AuditTrailGen();
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserName();
        }
        String branchcode = loginId.getBranch();
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String RowId = request.getParameter("userId");
        String RecordId = request.getParameter("userName");
        String RecordValue = request.getParameter("fullName");
        Cryptomanager cm = new Cryptomanager();
        String userId = request.getParameter("userId");
        String password = new String();
        try
        {
            Cryptomanager _tmp = cm;
            password = Cryptomanager.encrypt(request.getParameter("password"));
        }
        catch(Exception e) { }
        String buttSave = request.getParameter("buttSave");
        String userName = request.getParameter("userName");
        String fullname = request.getParameter("fullName");
        String legacyid = request.getParameter("legacyId");
        String userclass = request.getParameter("userClass");
        String passwords = password;
        String userBranch = request.getParameter("userBranch");
        String phoneNo = request.getParameter("phoneNo");
        String isSupervisor = request.getParameter("isSupervisor");
        String passwordMust = request.getParameter("passwordMust");
        String expiry = String.valueOf(passexpiry);
        String loginStatus = request.getParameter("loginStatus");
        String userStatus = request.getParameter("userStatus");
        String userid = ((User)session.getAttribute("CurrentUser")).getUserName();
        String email = request.getParameter("email");
        String approveLevel = request.getParameter("approveLevel");
        String companyCode = request.getParameter("companyCode");
        String isStorekeeper= request.getParameter("is_StoreKeeper");
        String branchRestrict= request.getParameter("branchRestrict");
        String userRestrict= request.getParameter("userRestrict");
        String isStockAdministrator = request.getParameter("is_StockAdministrator");
        System.out.println(">>>>>isStockAdministrator: "+isStockAdministrator+"   isStorekeeper: "+isStorekeeper);
        user.setUserName(userName);
        user.setUserFullName(fullname);
        user.setLegacySystemId(legacyid);
        user.setUserClass(userclass);
        user.setPassword(passwords);
        user.setBranch(userBranch);
        user.setPhoneNo(phoneNo);
        user.setIsSupervisor(isSupervisor);
        user.setMustChangePwd(passwordMust);
        user.setPwdExpiry(expiry);
        user.setLoginStatus(loginStatus);
        user.setUserStatus(userStatus);
        user.setCreatedBy(userid);
        user.setEmail(email);
        user.setApproveLevel(approveLevel);
        user.setCompanyCode(companyCode);
        user.setIsStorekeeper(isStorekeeper);
        user.setBranchRestrict(branchRestrict);
        user.setUserRestrict(userRestrict);
        user.setIsStockAdministrator(isStockAdministrator);
        try
        {
            if(request.getParameter("password").length() >= min)
            {
                if(userId.equals(""))
                {
                    if(security.findUserByUserName(userName) == null)
                    {
                        if(applHelper.createUser(user)) 
                        {
                            out.print("<script>alert('Record saved successfully.')</script>");
                            out.print("<script>window.location = 'DocumentHelp.jsp?np=admin/systemUsers&userId="+security.findUserByUserName(userName).getUserId()+"&PC=11'</script>");
                        } else
                        {
                            System.out.println((new StringBuilder()).append("Error saving record: New record \nfor 'user'  with user name  ").append(userName).append(" could not be created").toString());
                            out.print("<script>window.history.back()</script>");
                        }
                    } else
                    {
                        out.print((new StringBuilder()).append("<script>alert('Username [").append(request.getParameter("userName").trim()).append("] Exists Already.')</script>").toString());
                        out.print("<script>window.history.back()</script>");
                    }
                } else
                if(!userId.equals(""))
                {
                	System.out.println("userId >>>>>>>>>>> " + userId);
                    user.setUserId(userId);
                    audit.select(1, ("SELECT * FROM  MG_GB_USER  WHERE user_Id = '"+userId+"'"));
                    boolean isupdt = applHelper.updateUser(user);
                    audit.select(2, ("SELECT * FROM  MG_GB_USER  WHERE user_Id = '"+userId+"'"));
                    updtst = audit.logAuditTrail("MG_GB_USER ", branchcode,Integer.parseInt(loginID) , RowId, RecordId, RecordValue);
                    if(updtst)
                    {
                        out.print("<script>alert('Update on record is successfull')</script>");
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=admin/systemUsers&userId=").append(userId).append("&PC=11'</script>").toString());
                    } else
                    {
                        out.print("<script>alert('No changes made on record')</script>");
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=admin/systemUsers&userId=").append(userId).append("&PC=11'</script>").toString());
                    }
                }
            } else
            {
                out.print((new StringBuilder()).append("<script>alert('Minimum user password length is ").append(min).append("')</script>").toString());
                out.print("<script>window.history.back()</script>");
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            System.err.print(e.getMessage());
            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=admin/systemUsers&userId=").append(userId).append("&PC=11'</script>").toString());
        }
    }
}