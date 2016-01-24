/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.how2say.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.olanto.mycat.tmx.common.FormatHtmlResult;
import org.olanto.mycat.tmx.common.LangMap;
import org.olanto.senseos.SenseOS;

/**
 *
 * @author simple
 */
public class ServletProcess {

    public static String listLang = "NO LANG";
    public static boolean initOK = false;
    public static boolean verboseContent = true;
    public static boolean useCache = false;

    public static void init() {
        if (!initOK) {

            How2SayInit client = new ConfigurationGetFromFile(SenseOS.getMYCAT_HOME("HOW2SAY") + "/config/SERVLET_fix.xml");
            client.InitPermanent();
            client.InitConfiguration();
            if (How2SayConstant.CACHE.equals("CACHE")) {
                useCache = true;
            }
            if (useCache) {
                CacheQuery.init(SenseOS.getMYCAT_HOME("HOW2SAY") + "/config/ehcache.xml","how2sayCache");
            }
            listLang = How2SayConstant.LIST_OF_LANG;
            initOK = true;
        }
    }

    public static void processQery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long starttime = System.currentTimeMillis();

        HttpSession session = request.getSession();
        System.out.println("-------------------------:");
        System.out.println("getRequestURL:" + request.getRequestURL());
        String typeNode = "html";
        PrintWriter out = null;

        try {
            /* TODO output your page here. You may use following sample code. */
            String preContent = ServletProcess.outPre("", typeNode);
            String postContent = ServletProcess.outPost("", typeNode);
            // generate the node content
            String newContent = "";
            if (verboseContent) {
                System.out.println("before query time: " + (System.currentTimeMillis() - starttime) + " msec");
            }
            newContent += ServletProcess.query("", typeNode, request, session, response);
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
        s.append("<form NAME=\"search\" method=\"get\" action=\"./how2say?\">");
        s.append("<img src=\"olanto.jpg\"/>");
        s.append(" ");
        s.append(" <input type=\"text\" name=\"query\" value=\"" + query + "\" size=\"40\"/>");
        s.append(" <input type=\"submit\" value=\"How2Say ?\"/>");
        s.append(" from ");
        s.append(buildLangSelector("langso", langso));
        s.append(" to ");
        s.append(buildLangSelector("langta", langta));
        s.append(" with " + How2SayConstant.CORPUS_NAME);
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
            LangMap.init(listLang);
            languages = LangMap.decodelang;
            return buildSelector(selectorName, languages, selected);
        } catch (Exception ex) {
            Logger.getLogger(How2Say.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<p>ERROR</p>";
    }

    public static String query(
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
              //  query = new String(request.getParameter("query").getBytes("iso-8859-1"), "UTF-8");
               //query = new String(request.getParameter("query").getBytes("UTF-8"), "UTF-8");
                query = new String(request.getParameter("query").getBytes(How2SayConstant.URL_PARAM_ENCODING), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ServletProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (request.getParameterValues("langso") != null) {
            langso = request.getParameterValues("langso")[0];
        }
        if (request.getParameterValues("langta") != null) {
            langta = request.getParameterValues("langta")[0];
        }

        StringBuffer result = new StringBuffer("");
        String signature = query + "|" + langso + "|" + langta;
        if (useCache) {
            String fromCache = CacheQuery.get(signature);
            if (fromCache != null) {
                return fromCache;
            }
        }

//        result.append("<h1>Servlet How2Say at " + request.getContextPath() + "</h1>");
//        result.append("<p>query: " + query + "</p>");
//        result.append("<p>langso: " + langso + "</p>");
//        result.append("<p>langta: " + langta + "</p>");

        result.append(ServletProcess.getQueryForm(query, langso, langta));
        result.append("<hr/");

        FormatHtmlResult formatter = new FormatHtmlResult();
        result.append(formatter.getHtmlResult(query, langso, langta));
        if (useCache) {
            CacheQuery.put(signature, result.toString());
CacheQuery.info();
        }
        return result.toString();
    }

    /**
     * **********************************
     *
     * OUT PRE
     */
    public static String outPre(String a, String typeNode) {
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
    public static String outPost(String a, String typeNode) {
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
}
