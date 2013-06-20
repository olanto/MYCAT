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
package org.olanto.mySelfQD.server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.olanto.idxvli.server.IndexService_MyCat;

/**
 *
 * @author nizar ghoula
 */
public class DownloadServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(DownloadServlet.class);
    private static final long serialVersionUID = 1L;

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
        _logger.info("Execute action.");

        response.setContentType("application/download");
        String filename = request.getParameter("filename").replace("$$$$$$", "¦").replace("|||", "+");
        response.setHeader("Content-Disposition", "attachment; filename= \"" + filename + "\"");

        _logger.info(" request to download file:" + filename);

        System.out.println("request to download file :" + filename);
        ServletContext context = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);
        response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(getBytesFormServer(filename));
            out.flush();
        }
    }

    private byte[] getBytesFormServer(String FileName) {
        try {
            Remote r = Naming.lookup("rmi://localhost/VLI");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                System.out.println(is.getInformation());
                return is.getTemp(FileName);

            } else {
                return null;
            }

        } catch (NotBoundException | IOException ex) {
            _logger.error(ex);
        }

        return null;
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
