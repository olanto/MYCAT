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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.olanto.mySelfQD.client.GwtProp;
import org.olanto.mySelfQD.client.myParseService;
import org.olanto.mysqd.server.ConstStringManager;
import org.olanto.mysqd.server.MySelfQuoteDetection;
import org.olanto.senseos.SenseOS;

/**
 *
 * @author simple
 */
public class myParseServiceImpl extends RemoteServiceServlet implements myParseService {

    private static final long serialVersionUID = 1L;
    public static IndexService_MyCat is;
    public static String home = SenseOS.getMYCAT_HOME();
    public static Properties prop;
    public static ConstStringManager stringMan;
    public static GwtProp CONST = null;
    public static boolean RELOAD_PARAM_ON = true;

    @Override
    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.

        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            s += " / " + is.getInformation();
        } catch (RemoteException ex) {
            Logger.getLogger(myParseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Server says: Alive";
    }

    @Override
    public String createTempFile(String FileName, String Content) {
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            return is.createTemp(FileName, Content);
        } catch (RemoteException ex) {
            Logger.getLogger(myParseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public GwtProp InitPropertiesFromFile(String cookieLang) {
        if ((CONST == null) || (RELOAD_PARAM_ON)) {
            String fileName = SenseOS.getMYCAT_HOME() + "/config/GUI_fix.xml";
            System.out.println("found properties file:" + fileName);
            FileInputStream f = null;
            try {
                f = new FileInputStream(fileName);
            } catch (Exception e) {
                System.out.println("cannot find properties file:" + fileName);
                Logger.getLogger(myParseServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                prop = new Properties();
                prop.loadFromXML(f);
                RELOAD_PARAM_ON = Boolean.valueOf(prop.getProperty("RELOAD_PARAM_ON", "false"));
//                prop.list(System.out);
                InitProperties(cookieLang);
            } catch (Exception e) {
                System.out.println("errors in properties file:" + fileName);
                Logger.getLogger(myParseServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
//        System.out.println("Success getting all properties, object sent to the client");
            return CONST;
        } else {
            return CONST;
        }
    }

    private void InitProperties(String lastLang) {
        CONST = new GwtProp();
        String propPath = prop.getProperty("INTERFACE_MESSAGE_PATH");
        CONST.SAVE_ON = Boolean.valueOf(prop.getProperty("SAVE_ON"));
        CONST.MAXIMIZE_ON = Boolean.valueOf(prop.getProperty("MAXIMIZE_ON"));
        CONST.EXP_DAYS = Integer.parseInt(prop.getProperty("EXP_DAYS"));
        CONST.MIN_FREQ = Integer.parseInt(prop.getProperty("MIN_FREQ"));
        CONST.MAX_FREQ = Integer.parseInt(prop.getProperty("MAX_FREQ"));
        CONST.MIN_OCCU = Integer.parseInt(prop.getProperty("MIN_OCCU"));
        CONST.MAX_OCCU = Integer.parseInt(prop.getProperty("MAX_OCCU"));
        CONST.CHARACTER_WIDTH = Integer.parseInt(prop.getProperty("CHARACTER_WIDTH"));
        CONST.JOBS_ITEMS = prop.getProperty("JOBS_ITEMS");
        CONST.SELF_QD = prop.getProperty("SELF_QD_LBL");
        CONST.SELFQD_FILE_EXT = prop.getProperty("SELFQD_FILE_EXT");
        CONST.SQD_HELP_URL = prop.getProperty("SQD_HELP_URL");
        CONST.W_OPEN_FEATURES = prop.getProperty("W_OPEN_FEATURES");
        CONST.OLANTO_URL = prop.getProperty("OLANTO_URL");
        CONST.LOGO_PATH = prop.getProperty("LOGO_PATH");
        CONST.LOGO_URL = prop.getProperty("LOGO_URL");
        CONST.FEEDBACK_MAIL = prop.getProperty("FEEDBACK_MAIL");
        CONST.CHOOSE_GUI_LANG = Boolean.valueOf(prop.getProperty("CHOOSE_GUI_LANG", "false"));
        CONST.CHOOSE_GUI_LANG_LIST = prop.getProperty("CHOOSE_GUI_LANG_LIST", "en;fr");
        /**
         * **********************************************************************************
         */
        String interLang;

        if (CONST.CHOOSE_GUI_LANG) {
            interLang = lastLang;
        } else {
            interLang = prop.getProperty("INTERFACE_MESSAGE_LANG");
        }
        String messagesPropFile;
        try {
            if ((interLang == null)) {
                messagesPropFile = home + propPath + ".properties";
            } else {
                messagesPropFile = home + propPath + "_" + interLang + ".properties";
                File prp = new File(messagesPropFile);
                if (!(prp.exists())) {
                    messagesPropFile = home + propPath + ".properties";
                }
            }
            stringMan = new ConstStringManager(messagesPropFile);
            CONST.WIDGET_BTN_SUBMIT = stringMan.get("widget.btn.submit");
            CONST.WIDGET_BTN_HELP = stringMan.get("widget.btn.help");
            CONST.WIDGET_BTN_SQD_SAVE = stringMan.get("widget.btn.sqd.save");
            CONST.WIDGET_LBL_SQD_LN = stringMan.get("widget.label.sqd.length");
            CONST.WIDGET_LBL_SQD_FRQ = stringMan.get("widget.label.sqd.freq");
            CONST.MSG_1 = stringMan.get("widget.sqd.MSG_1");
            CONST.MSG_2 = stringMan.get("widget.sqd.MSG_2");
            CONST.MSG_3 = stringMan.get("widget.sqd.MSG_3");
            CONST.MSG_4 = stringMan.get("widget.sqd.MSG_4");
            CONST.MSG_5 = stringMan.get("widget.sqd.MSG_5");
            CONST.MSG_6 = stringMan.get("widget.sqd.MSG_6");
            CONST.MSG_7 = stringMan.get("widget.sqd.MSG_7");
            CONST.MSG_8 = stringMan.get("widget.sqd.MSG_8");
            CONST.MSG_9 = stringMan.get("widget.sqd.MSG_9");
            CONST.MSG_10 = stringMan.get("widget.sqd.MSG_10");
            CONST.MSG_11 = stringMan.get("widget.sqd.MSG_11");
            CONST.MSG_12 = stringMan.get("widget.sqd.MSG_12");
            CONST.MSG_13 = stringMan.get("widget.sqd.MSG_13");
            CONST.MSG_14 = stringMan.get("widget.sqd.MSG_14");
            CONST.MSG_15 = stringMan.get("widget.sqd.MSG_15");
        } catch (IOException ex) {
            Logger.getLogger(myParseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getHtmlRef(String Content, String fileName, int minOcc, int minCons, String SQDSaveExt) {
        if (fileName.contains(SQDSaveExt)) {
            return Content;
        } else {
            UploadedFile up = new UploadedFile(Content, fileName);
            return new MySelfQuoteDetection(up.getFileName(), up.getContentString(), minOcc, minCons, false, stringMan).getHTML();
        }
    }
}
