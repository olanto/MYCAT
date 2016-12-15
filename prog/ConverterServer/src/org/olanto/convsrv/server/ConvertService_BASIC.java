/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.convsrv.server;

import org.olanto.converter.Document;
import org.olanto.converter.SimpleConverterApplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.locks.*;
import org.apache.log4j.Logger;
import org.olanto.converter.ConfigUtil;
import org.olanto.converter.Constants;

/**
 *  service de conversion.
 *
 * 
 *
 * <p>
 * *  <pre>
 *  concurrence:
 *   - // pour les lecteurs
 *   - Ã©crivain en exclusion avec tous
 *  doit Ãªtre le point d'accÃ©s pour toutes les structures utilisÃ©es !
 *  </pre>
 *   
 */
public class ConvertService_BASIC extends UnicastRemoteObject implements ConvertService {

    private final static Logger _logger = Logger.getLogger(ConvertService_BASIC.class);
    public static String TEMPDIR = System.getProperty("java.io.tmpdir");

    public ConvertService_BASIC() throws RemoteException {
        super();
        TEMPDIR= ConfigUtil.getTempPath();
        File tmpDir = new File(TEMPDIR);

        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
    }

    public String getInformation() throws RemoteException {
        return "this service is alive ... :ConverterService_BASIC";
    }
    /** opÃ©ration sur les verrous ------------------------------------------*/
    private final ReentrantReadWriteLock serverRW = new ReentrantReadWriteLock();
    private final Lock serverR = serverRW.readLock();
    private final Lock serverW = serverRW.writeLock();

    public String File2Txt(byte[] content, String fileName) throws RemoteException {
        serverW.lock();

        _logger.info("Try to convert file:" + fileName);

        String ret = "Seems not working. ";

        try {

            File f = File.createTempFile("toConvert", fileName, new File(TEMPDIR));
            FileOutputStream out = new FileOutputStream(f);
            out.write(content);
            out.flush();
            out.close();
            ret += " File: " + f.getAbsolutePath();

            Document src = new Document(f.getAbsolutePath());
            Document dest = new Document(f.getAbsolutePath() + ".txt");

            SimpleConverterApplication converter = SimpleConverterApplication.getInstance();
            converter.setMaxRetry(ConfigUtil.getMaxRetry());
            converter.setOutputFormat(ConfigUtil.getTargetFormat());

            converter.convertObject(src, dest, null);
            ret = UtilsFiles.file2String(new FileInputStream(dest), ConfigUtil.getOutputEncoding());

            return ret;
        } catch (Exception e) {
            _logger.error("Failed to convert file " + fileName + ": " + e.getMessage());
        } finally {
            serverW.unlock();
        }
        return ret;
    }

     public String File2Txt(String fileName) throws RemoteException {  // No temp
        serverW.lock();

        _logger.info("Try to convert file:" + fileName);

        String ret = "Seems not working. ";

        try {

            File f = new File(fileName);
//            FileOutputStream out = new FileOutputStream(f);
//            out.write(content);
//            out.flush();
//            out.close();
            ret += " File: " + f.getAbsolutePath();

            Document src = new Document(f.getAbsolutePath());
            Document dest = new Document(f.getAbsolutePath() + ".txt");

            SimpleConverterApplication converter = SimpleConverterApplication.getInstance();
            converter.setMaxRetry(ConfigUtil.getMaxRetry());
            converter.setOutputFormat(ConfigUtil.getTargetFormat());

            converter.convertObject(src, dest, null);
            ret = UtilsFiles.file2String(new FileInputStream(dest), ConfigUtil.getOutputEncoding());

            return ret;
        } catch (Exception e) {
            _logger.error("Failed to convert file " + fileName + ": " + e.getMessage());
        } finally {
            serverW.unlock();
        }
        return ret;
    }
  
    
    public byte[] File2UTF8(byte[] content, String fileName) throws RemoteException {
        serverW.lock();
        String ret = "Seems not working. ";

        _logger.info("Try to convert file:" + fileName);

        try {
            File f = File.createTempFile("toConvert", fileName, new File(TEMPDIR));
            FileOutputStream out = new FileOutputStream(f);
            out.write(content);
            out.flush();
            out.close();
            ret += " File is saved for conversion: " + f.getAbsolutePath();

            Document src = new Document(f.getAbsolutePath());
            Document dest = new Document(f.getAbsolutePath() + "."+Constants.TXT);

            SimpleConverterApplication converter = SimpleConverterApplication.getInstance();
            converter.setMaxRetry(ConfigUtil.getMaxRetry());
            converter.setOutputFormat(Constants.TXT);
            converter.convertObject(src, dest, src.getParentFile().getAbsolutePath());
            ret = UtilsFiles.file2String(new FileInputStream(dest), "UTF-8");

            return ret.getBytes();
        } catch (Exception e) {
            _logger.error("Failed to convert file " + fileName + ": " + e.getMessage());
        } finally {
            serverW.unlock();
        }
        return ret.getBytes();

    }
    public String File2HtmlUTF8(byte[] content, String fileName) throws RemoteException {
        serverW.lock();

        _logger.info("Try to convert file:" + fileName);

        String ret = "Seems not working. ";

        try {

            File f = File.createTempFile("toConvert", fileName, new File(TEMPDIR));
            FileOutputStream out = new FileOutputStream(f);
            out.write(content);
            out.flush();
            out.close();
            ret += " File: " + f.getAbsolutePath();

            Document src = new Document(f.getAbsolutePath());
            Document dest = new Document(f.getAbsolutePath() + ".html");

            SimpleConverterApplication converter = SimpleConverterApplication.getInstance();
            converter.setMaxRetry(3);
            converter.setOutputFormat("html");

            converter.convertObject(src, dest, null);
            ret = UtilsFiles.file2String(new FileInputStream(dest), "UTF-8");

            return ret;
        } catch (Exception e) {
            _logger.error("Failed to convert file " + fileName + ": " + e.getMessage());
        } finally {
            serverW.unlock();
        }
        return ret;
    }

    public byte[] File2File(byte[] content, String fileName, String targetFormat) throws RemoteException {
        serverW.lock();

        _logger.info("Try to convert file:" + fileName);

        String ret = "Seems not working. ";

        try {

            File f = File.createTempFile("toConvert", fileName, new File(TEMPDIR));
            FileOutputStream out = new FileOutputStream(f);
            out.write(content);
            out.flush();
            out.close();
            ret += " File: " + f.getAbsolutePath();

            Document src = new Document(f.getAbsolutePath());
            Document dest = new Document(f.getAbsolutePath() + "."+targetFormat);

            SimpleConverterApplication converter = SimpleConverterApplication.getInstance();
            converter.setMaxRetry(ConfigUtil.getMaxRetry());
            converter.setOutputFormat(targetFormat);

            converter.convertObject(src, dest, null);
            
            return UtilsFiles.file2byte(dest);

        } catch (Exception e) {
            _logger.error("Failed to convert file " + fileName + ": " + e.getMessage());
        } finally {
            serverW.unlock();
        }
        return ret.getBytes();

    }
}
