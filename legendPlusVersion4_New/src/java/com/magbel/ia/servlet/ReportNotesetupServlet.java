package com.magbel.ia.servlet;

import audit.AuditTrailGen;
import com.magbel.ia.bus.AdminServiceBus;
import com.magbel.ia.vao.NoteMapping;
import com.magbel.ia.vao.User;
import com.magbel.util.CheckIntegerityContraint;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ReportNotesetupServlet extends HttpServlet
{

    public ReportNotesetupServlet()
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
        User loginId = (User)session.getAttribute("CurrentUser");
        String loginID;
        String userId = "";
        if(loginId == null)
        {
            loginID = "Unkown";
        } else
        {
            loginID = loginId.getUserName();
            userId = loginId.getUserId();
        }
        String branchcode = loginId.getBranch();
        if(branchcode == null)
        {
            branchcode = "not set";
        }
        String noteCode = request.getParameter("noteCode");
        String noteId = request.getParameter("noteId");
        String noteStatus = request.getParameter("noteStatus");
        String buttSave = request.getParameter("buttSave");
        String reportName = request.getParameter("reportName");
        String noteName = request.getParameter("noteName");
        String ledgerNo = request.getParameter("ledgerNo");
        String noteNo = request.getParameter("noteNo");
        System.out.println("noteCode: "+noteCode+" ledgerNo: "+ledgerNo);
 //       if(acronym != null)
 //       {
 //           acronym = acronym.toUpperCase();
 //       }
        NoteMapping note = new NoteMapping();
 //       String reportName = request.getParameter("reportname");
        String notedescription = request.getParameter("reportname");
        String status = request.getParameter("noteStatus");
        String companyCode = request.getParameter("companyCode");
        note.setNoteCode(noteCode);
        note.setNoteId(noteId);
        note.setNotedescription(notedescription);
        note.setNoteNo(noteNo);
        note.setReportName(reportName);
        note.setNoteName(noteName);
        note.setStatus(status);
        note.setCompanyCode(companyCode);
        note.setUserId(userId);
        note.setLedgerNo(ledgerNo);
        AdminServiceBus admin = new AdminServiceBus();
        try
        {
            if(buttSave != null)
            {
                if(noteId.equals(""))
                {
                    if(admin.findNoteByNoteCode(noteCode) != null)
                    {
                        out.print("<script>alert('The branch code already exists .');</script>");
                        out.print("<script>history.go(-1);</script>");
                    } else
                    if(admin.createNoteMapping(note))
                    {
                        out.print("<script>alert('Record saved successfully.');</script>");
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=ReportNoteSetup&noteId=").append(admin.findNoteByNoteCode(noteCode).getNoteId()).append("&PC=8';</script>").toString());
                    }
                } else
                if(!noteId.equals(""))
                {
                    note.setNoteId(noteId);
                    CheckIntegerityContraint intCont = new CheckIntegerityContraint();
                    if(intCont.checkReferenceConstraint("IA_GL_BALANCE_NOTES", "NOTE_CODE", note.getNoteCode(), noteStatus))
                    {
                        out.print("<script>alert('This Note Code is being referenced by other records it thus can" +
"not by closed.')</script>"
);
                        out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=ReportNoteSetup&noteId=").append(noteId).append("&PC=8'</script>").toString());
                    } else
                    {
                        audit.select(1, (new StringBuilder()).append("SELECT * FROM  IA_GL_BALANCE_NOTES   WHERE ID = '").append(noteId).append("'").toString());
                        boolean isupdt = admin.updateNoteMapping(note);
                        audit.select(2, (new StringBuilder()).append("SELECT * FROM  IA_GL_BALANCE_NOTES  WHERE ID = '").append(noteId).append("'").toString());
                        updtst = audit.logAuditTrail("IA_GL_BALANCE_NOTES ", noteCode, loginID, noteId, noteCode, notedescription);
                        if(updtst)
                        {
                            out.print("<script>alert('Update on record is successfull')</script>");
                            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=ReportNoteSetup&noteId=").append(noteId).append("&PC=8'</script>").toString());
                        } else
                        {
                            out.print("<script>alert('No changes made on record')</script>");
                            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=ReportNoteSetup&noteId=").append(noteId).append("&PC=8'</script>").toString());
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("<script>alert('Ensure unique record entry.')</script>");
            out.print((new StringBuilder()).append("<script>window.location = 'DocumentHelp.jsp?np=ReportNoteSetup&noteId=").append(noteId).append("&PC=8'</script>").toString());
            System.err.print(e.getMessage());
        }
    }
}
