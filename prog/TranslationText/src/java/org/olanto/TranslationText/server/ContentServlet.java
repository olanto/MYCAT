/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This messageContainer is part of myCAT.
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
package org.olanto.TranslationText.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.olanto.convsrv.server.ConvertService;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.olanto.senseos.SenseOS;

/**
 *
 * @author nizar ghoula
 */
public class ContentServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(ContentServlet.class);
    private static final long serialVersionUID = 1L;
    private static String ext;

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
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        _logger.info("FileName before change:" + request.getParameter("filename"));
        String filename = request.getParameter("filename").replace("$$$$$$", "¦").replace("|||", "+");
        _logger.info("request to get content of file:" + filename);
        System.out.println("request to get content of file:" + filename);

        response.setContentType("text/html");
        ext = getFileExtension();
        if ((filename.toLowerCase().endsWith(".txt")) || (filename.endsWith(ext))) { // charge sous forme de txt
            String content = new String(getBytesFormServer(filename), StandardCharsets.UTF_8);
            response.getWriter().write(cleanConvertedFile(addMissingIds(content)));
            System.out.println("File converted successfully ");
        } else {
            // need conversion
            response.getWriter().write(cleanConvertedFile(convertFileWithRMI(getBytesFormServer(filename), filename)));
            System.out.println("File converted successfully");
        }
    }

    private byte[] getBytesFormServer(String fileName) {
        try {
            Remote r = Naming.lookup("rmi://localhost/VLI");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                System.out.println(is.getInformation());
                return is.getByte(fileName);
            } else {
                return null;
            }

        } catch (NotBoundException | IOException ex) {
            _logger.error(ex);
        }

        return null;
    }

    private String convertFileWithRMI(byte[] bytes, String fileName) {
        String ret = "Warning: System seems to be unavailable, please contact the Translation Support Section";
        _logger.info("Request to convert file: " + fileName);
        System.out.println("Request to convert file: " + fileName);
        try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
                // ret = is.getInformation();

                int pos = fileName.lastIndexOf('/');

                if (pos >= 0) {
                    fileName = fileName.substring(pos + 1);
                }

                ret = is.File2Txt(bytes, fileName);
                
                        System.out.println("Converted file content: " + ret);
                
            } else {
                return "CONVSRV Service not found or not compatible.";
            }

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            _logger.error(ex);
        }

        return ret;
    }

    private String getFileExtension() {
        Properties prop;
        String fileName = SenseOS.getMYCAT_HOME() + "/config/GUI_fix.xml";
        FileInputStream f = null;
        try {
            f = new FileInputStream(fileName);
        } catch (Exception e) {
            System.out.println("cannot find properties file:" + fileName);
            _logger.error(e);
        }
        try {

            prop = new Properties();
            prop.loadFromXML(f);
            return prop.getProperty("QD_FILE_EXT");
        } catch (Exception e) {
            System.out.println("errors in properties file:" + fileName);
            _logger.error(e);
        }
        return null;
    }

    private String addMissingIds(String content) {
        if (content.contains("id=\"ref")) {
            return content;
        } else {
            String regex = "(<a)(\\s+)(href=\")(#)(\\d+)(\")";
            return content.replaceAll(regex, "$1$2$3$4$5$6 id=\"ref$5$6");
        }
    }

    public static String cleanConvertedFile(String s) {
//        System.out.println("-------cst:"+s);
//        for (int i=0; i<s.length();i++){
//            int v=s.charAt(i);
//             System.out.println(i+":"+s.substring(i, i+1)+":"+v);
//        }
//        s = s.replace("\t", " ");
//        char x20 = 0x20;
//        char xa0 = 0xa0;
//        System.out.println("nbsp")
//            System.out.println("nbsp:" + s);
//        s = s.replace("" + xa0 + x20, " ");
//        System.out.println("1e");
        char x1e = 0x1e;
        s = s.replace("" + x1e, " ");
//        System.out.println("1f");
        char x1f = 0x1f;
        s = s.replace("" + x1f, "");
//         System.out.println("02");
        char x02 = 0x02;
        s = s.replace("" + x02, " ");
//          System.out.println("13");
        char x13 = 0x13;
        s = s.replace("" + x13, " ");
//          System.out.println("15");
        char x15 = 0x15;
        s = s.replace("" + x15, " ");
//          System.out.println("00");
        char x00 = 0x00;
        s = s.replace("" + x00, " ");
//          System.out.println("0b");
        char x0b = 0x0b;
        s = s.replace("" + x0b, " ");
//          System.out.println("0c");
        char x0c = 0x0c;
        s = s.replace("" + x0c, " ");
//          System.out.println("double blanc");
//        s = s.replace("  ", " ");
        return s;
    }
}
