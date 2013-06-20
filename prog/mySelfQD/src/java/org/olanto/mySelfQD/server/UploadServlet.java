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
package org.olanto.mySelfQD.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.olanto.convsrv.server.ConvertService;

/**
 * Cette servlet est utilisée uniquement pour l'upload de fichier. Elle répond à
 * un upload avec les informations du fichier.
 *
 */
public class UploadServlet extends UploadAction {

    private static final long serialVersionUID = 1L;
//    HashMap<String, String> receivedContentTypes = new HashMap<String, String>();
    /**
     * * Maintain a list with received files and their content types.
     */
//    HashMap<String, File> receivedFiles = new HashMap<String, File>();
    private static final Logger _logger = Logger.getLogger(UploadServlet.class);

    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = "";
        _logger.info("Execute action.");

        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {
                try {
                    if ((item.getName().toLowerCase().endsWith(".txt")) || (item.getName().toLowerCase().endsWith(".html"))) { // charge sous forme de txt
                        response += UtilsFiles.file2String(item.getInputStream(), "UTF-8");
                    } else {
                        // need conversion
                        response += convertFileWithRMI(item.get(), item.getName());
                    }
                    System.out.println("File converted successfully");
                } catch (Exception ex2) {
                    _logger.error(ex2);
                }
            }
        }
        // Remove files from session because we have a copy of them    
        removeSessionFileItems(request);
        // Send your customized message to the client.    
        return response;
    }

    private String convertFileWithRMI(byte[] bytes, String fileName) {
        String ret = "MYCAT: Seems not working, RMI server down?";
        _logger.info("Request to convert file: " + fileName);
        System.out.println("Request to convert file: " + fileName);

        try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;

                int pos = fileName.lastIndexOf('\\');
                if (pos >= 0) {
                    fileName = fileName.substring(pos + 1);
                }

                ret = is.File2HtmlUTF8(bytes, fileName);
            } else {
                return "CONVSRV Service not found or not compatible.";
            }
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            _logger.error(ex);
        }
        return ret;
    }
}
