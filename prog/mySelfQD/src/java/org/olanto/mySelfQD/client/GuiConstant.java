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
package org.olanto.mySelfQD.client;

/**
 * Une classe pour déclarer des constantes du conteneur de MAP.
 *
 */
public class GuiConstant {

    public static String JOBS_ITEMS;
    public static String SELFQD_FILE_EXT;
    public static String SQD_HELP_URL;
    public static String OLANTO_URL;
    public static String W_OPEN_FEATURES;
    public static int EXP_DAYS;
    public static int MIN_FREQ;
    public static int MAX_FREQ;
    public static int MIN_OCCU;
    public static int MAX_OCCU;
    public static int CHARACTER_WIDTH;
    public static String SELF_QD;
    public static String LOGO_PATH;
    public static String LOGO_URL;
    public static String WIDGET_BTN_SUBMIT;
    public static String WIDGET_BTN_HELP;
    public static String WIDGET_BTN_SQD_SAVE;
    public static String WIDGET_BTN_SQD_LN;
    public static String WIDGET_BTN_SQD_FRQ;
    public static String FEEDBACK_MAIL;
    public static boolean SAVE_ON;
    public static boolean MAXIMIZE_ON;
    public static boolean CHOOSE_GUI_LANG;
    public static String CHOOSE_GUI_LANG_LIST;
    public static String MSG_1;
    public static String MSG_2;
    public static String MSG_3;
    public static String MSG_4;
    public static String MSG_5;
    public static String MSG_6;
    public static String MSG_7;
    public static String MSG_8;
    public static String MSG_9;
    public static String MSG_10;
    public static String MSG_11;
    public static String MSG_12;
    public static String MSG_13;
    public static String MSG_14;
    public static String MSG_15;

    /**
     * **********************************************************************************
     */
    public static String show() {
        return "GUI"
                + "\nPARAMETERS"
                + "\n    JOBS_ITEMS: " + JOBS_ITEMS
                + "\n    SELFQD_FILE_EXT: " + SELFQD_FILE_EXT
                + "\n    SQD_HELP_URL: " + SQD_HELP_URL
                + "\n    OLANTO_URL: " + OLANTO_URL
                + "\n    LOGO_PATH: " + LOGO_PATH
                + "\n    LOGO_URL: " + LOGO_URL
                + "\n    W_OPEN_FEATURES: " + W_OPEN_FEATURES
                + "\n    EXP_DAYS: " + EXP_DAYS
                + "\n    MIN_FREQ: " + MIN_FREQ
                + "\n    MAX_FREQ: " + MAX_FREQ
                + "\n    MIN_OCCU: " + MIN_OCCU
                + "\n    MAX_OCCU: " + MAX_OCCU
                + "\n    SELF_QD: " + SELF_QD
                + "\n    WIDGET_BTN_SQD_SAVE: " + WIDGET_BTN_SQD_SAVE
                + "\n    WIDGET_BTN_SQD_LN: " + WIDGET_BTN_SQD_LN
                + "\n    WIDGET_BTN_SQD_FRQ: " + WIDGET_BTN_SQD_FRQ
                + "\n    FEEDBACK_MAIL: " + FEEDBACK_MAIL;
    }
}
