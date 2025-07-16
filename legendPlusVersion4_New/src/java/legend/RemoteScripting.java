package legend;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

/**
 * REMOTE SCRIPTING SOFTWARE LICENSE, Version 1.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by Steve Fan,
 * http://www.forperfect.com Alternately, this acknowledgment may
 * appear in the software itself, if and wherever such third-party
 * acknowledgments normally appear.
 *
 * 4. Back link to http://www.forperfect.com is recommended, but not
 * required. If you do, we really appreciate it.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * For more information please contact
 * Steve Fan (sfan@forperfect.com)
 *        http://www.forperfect.com
 *
 */

public abstract class RemoteScripting extends HttpServlet {

    private final static String CONTENT_TYPE = "text/html";

    /**
     * Dispatch the methods, send back content.
     * @param request request object.
     * @param response response object.
     * @throws IOException IOException standard for servlet.
     * @throws ServletException ServletException standard for servlet.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {

        //get possible client parameters from query string.
        String funcname = request.getParameter("F");
        String callback = request.getParameter("C");

        if (funcname == null) {
            dumpOutput(response,
                       "You must specify the server side method name.",
                       callback, true);
            return;
        }

        //get parameters
        Vector vparams = new Vector();
        int pct = 0;
        String p = request.getParameter("P0");
        while (p != null) {

            p = p.substring(1, p.length() - 1);
            try {
                p = URLDecoder.decode(p, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //non fatal in most of cases.
                log("UnsupportedEncodingException seen in decoding the paramenters. Parameter value is passed without decoding.");
            }

            vparams.add(p);
            pct++;
            p = request.getParameter("P" + pct);

        }

        String result;
        boolean error = false;
        try {
            Class c = this.getClass();
            if (vparams.size() == 0) {
                Method m = c.getMethod(funcname, null);
                result = (String) m.invoke(null, null);
            } else {
                Class[] paracs = new Class[vparams.size()];
                String[] params = new String[vparams.size()];

                Class sc = Class.forName("java.lang.String");

                for (int i = 0; i < paracs.length; i++) {
                    paracs[i] = sc;
                    params[i] = (String) vparams.get(i);
                }

                Method m = c.getMethod(funcname, paracs);
                result = (String) m.invoke(null, params);
            }

        } catch (Exception e) {
            //get real error message.
            if (e instanceof InvocationTargetException) {
                e = (Exception) ((InvocationTargetException) e).
                    getTargetException();
            }
            result = e.toString();
            error = true;
            log("RemoteScripting: invoke method " + funcname +
                " caused exception." + e.getMessage());
            e.printStackTrace();
        }

        //we do not deal with IOException.
        dumpOutput(response, result, callback, error);
    }

    /**
     * doPost standard method for servlet.
     * @param request request object.
     * @param response response object.
     * @throws IOException IOException handled by JSP engine.
     * @throws ServletException ServletException handled by JSP engine.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        doGet(request, response);
    }

    /**
     * Send output (function call result) to the client side.
     * @param response response object.
     * @param msg function call result value.
     * @param callbackName callback function name, the client javascript function name.
     * @param error a flag to tell if function encounter errors.
     * @throws IOException IOException object handled by JSP engine.
     */
    private void dumpOutput(HttpServletResponse response, String msg,
                            String callbackName, boolean error) throws
            IOException {

        StringBuffer output = new StringBuffer(
                "<html><head></head><body onload=\"p=document.layers?parentLayer:window.parent;");
        if (error) {
            output.append("p.jsrsError('").append(callbackName).append(
                    "','jsrsError: ");
            output.append(encode(msg));
            output.append("');\">jsrsError: ").append(escape(msg));

        } else {
            output.append("p.jsrsLoaded('").append(callbackName);
            output.append("');\">jsrsPayload:<br><form name=\"jsrs_Form\"><textarea name=\"jsrs_Payload\">");
            output.append(escape(msg)).append("</textarea></form>");
        }

        output.append("</body></html>");

        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println(output.toString());

    }

    /**
     * Encode the string. URL encoding, plus the + sign.
     * @param str string to be encoded.
     * @return the encoded string.
     */
    public String encode(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8");
            str = str.replaceAll("\\+", "%2B");
            return str;
        } catch (UnsupportedEncodingException ue) {
            //system logging should go here.
            System.err.println("RemoteScripting: Encoding error.");
            return str;
        }

    }

    /**
     * Escape the string. ' " and /
     * @param str string to be escaped.
     * @return escaped string.
     */
    public String escape(String str) {
        str = str.replaceAll("\'", "\\'");
        str = str.replaceAll("\"", "\\\"");
        str = str.replaceAll("/", "\\/");
        return str;
    }

    /**
     * an abstract method to do logging. Implement this function
     * in the concrete subclass.
     * @param msg log message.
     */
    public abstract void log(String msg);

}
