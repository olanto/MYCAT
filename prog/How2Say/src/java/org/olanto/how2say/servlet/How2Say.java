/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.how2say.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.olanto.mycat.tmx.multiun.extractor.FormatHtmlResult;
import org.olanto.mycat.tmx.dgt2014.extract.LangMapDGT2014;

/**
 *
 * @author simple
 */
public class How2Say extends HttpServlet {

    public static boolean verboseContent = true;

    public static void main(String[] args) {  //only to debug

        LangMapDGT2014.init();
        String s = getQueryForm("pomme de terre", "FR", "EN");
        System.out.println(s);
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long starttime = System.currentTimeMillis();

        HttpSession session = request.getSession();
        System.out.println("-------------------------:");
        System.out.println("getRequestURL:" + request.getRequestURL());
        String typeNode = "html";
        PrintWriter out = null;

        try {
            /* TODO output your page here. You may use following sample code. */
            String preContent = outPre("", typeNode);
            String postContent = outPost("", typeNode);
            // generate the node content
            String newContent = "";
            if (verboseContent) {
                System.out.println("before query time: " + (System.currentTimeMillis() - starttime) + " msec");
            }
            newContent += query("", typeNode, request, session, response);
            if (verboseContent) {
                System.out.println("after query time : " + (System.currentTimeMillis() - starttime) + " msec");
            }
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            out.println(preContent);
            out.println(newContent);
            out.println(postContent);

        } finally {
            out.close();
        }
    }

    public static String getQueryForm(String query, String langso, String langta) {
        StringBuilder s = new StringBuilder("");
        // heades
        s.append("<form NAME=\"search\" method=\"post\" action=\"./how2say?\">");
        s.append("<img src=\"olanto.jpg\"/>");
        s.append(" ");
        s.append(" <input type=\"text\" name=\"query\" value=\"" + query + "\" size=\"40\"/>");
        s.append(" <input type=\"submit\" value=\"How2Say ?\"/>");
        s.append(" from ");
        s.append(buildLangSelector("langso", langso));
        s.append(" to ");
        s.append(buildLangSelector("langta", langta));
        s.append(" with DGT2014 ");
        s.append("</form>");
        return s.toString();
    }

    public static String buildSelector(String name, String[] options, String languageSelect) {
        String res = "<select name=\"" + name + "\">";
        for (int i = 0; i < options.length; i++) {
            res += "<option>" + options[i] + "</option>\n";
        }
        res += "</select>";
        res = res.replace(">" + languageSelect + "<", " selected>" + languageSelect + "<");
        return res;
    }

    public static String buildLangSelector(String selectorName, String selected) {
        try {
            String[] languages = null;
            LangMapDGT2014.init();
            languages = LangMapDGT2014.decodelang;
            return buildSelector(selectorName, languages, selected);
        } catch (Exception ex) {
            Logger.getLogger(How2Say.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<p>ERROR</p>";
    }

    String query(
            String action,
            String typeNode,
            HttpServletRequest request,
            HttpSession session,
            HttpServletResponse response) {

        String query = "pommes de terre";
        String langso = "FR";
        String langta = "EN";

        if (request.getParameterValues("query") != null) {
            try {
                //                    try {
                //                        query = URLDecoder.decode(request.getParameterValues("query")[0],"UTF-8");
                //                    } catch (UnsupportedEncodingException ex) {
                //                        Logger.getLogger(How2Say.class.getName()).log(Level.SEVERE, null, ex);
                //                    }
                query = new String(request.getParameter("query").getBytes("iso-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(How2Say.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (request.getParameterValues("langso") != null) {
            langso = request.getParameterValues("langso")[0];
        }
        if (request.getParameterValues("langta") != null) {
            langta = request.getParameterValues("langta")[0];
        }

        StringBuffer result = new StringBuffer("");
//        result.append("<h1>Servlet How2Say at " + request.getContextPath() + "</h1>");
//        result.append("<p>query: " + query + "</p>");
//        result.append("<p>langso: " + langso + "</p>");
//        result.append("<p>langta: " + langta + "</p>");

        result.append(getQueryForm(query, langso, langta));
        result.append("<hr/");

        FormatHtmlResult formatter = new FormatHtmlResult();
        result.append(formatter.getHtmlResult(query, langso, langta));

        return result.toString();
    }

    /**
     * **********************************
     *
     * OUT PRE
     */
    String outPre(String a, String typeNode) {
        StringBuffer out = new StringBuffer("");
        if (typeNode.equals("purexml")) {
            // out.println("<?xml version='1.0'  encoding='ISO-8859-1' ?>");
            out.append("<?xml version='1.0'  encoding='UTF-8' ?>");
        }
        if (typeNode.equals("html")) {
            //out.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
            out.append("<html>");
            out.append("<head>");
            out.append("<LINK rel=stylesheet href=\"basic.css\" type=\"text/css\">");
            out.append("<title>Olanto How2Say</title>");
            out.append("</head>");
            out.append("<body>");
        }

        if (typeNode.equals("xml")) {
        }
        if (typeNode.equals("xslthosted")) {
        }
        if (typeNode.equals("purehtml")) {
        }
        if (typeNode.equals("svg")) {
        }
        if (typeNode.equals("pdf")) {
        }
        return out.toString();
    }

    /**
     * **********************************
     *
     * OUT POST
     */
    String outPost(String a, String typeNode) {
        StringBuffer out = new StringBuffer("");
        if (typeNode.equals("html")) {
            out.append(
                    "<hr/><font size=-1><i>How2Say - 2014 - Version 1.0 - Powered by "
                    + "<a target=\"_blank\" href=\"http://olanto.org\">Olanto</a>"
                    + "</i></font>");
            out.append("</body>");
            out.append("</html>");
        }

        if (typeNode.equals("xml")) {
        }
        if (typeNode.equals("xslthosted")) {
        }
        if (typeNode.equals("purexml")) {
        }
        if (typeNode.equals("purehtml")) {
        }
        if (typeNode.equals("svg")) {
        }
        if (typeNode.equals("pdf")) {
        }
        return out.toString();

    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
