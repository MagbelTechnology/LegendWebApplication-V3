package com.magbel.admin.servlet;
/**
This class should be mapped to word.doc
*/
import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.magbel.admin.bus.BinaryHolder;
public class WordDocServlet extends HttpServlet{

    public void init(ServletConfig config) throws ServletException{
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1L);
        HttpSession session = request.getSession();
        BinaryHolder sb = (BinaryHolder)session.getAttribute("binary");
        byte[] image = sb.toByteArray();

        response.setContentType("application/msword");
        ServletOutputStream sos = response.getOutputStream();
        sos.write(image);

    }
}

