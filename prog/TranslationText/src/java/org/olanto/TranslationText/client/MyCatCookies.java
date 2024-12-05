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
package org.olanto.TranslationText.client;

import com.google.gwt.user.client.Cookies;
import java.util.Date;

/**
 *
 * @author simple
 */
public class MyCatCookies {

    public MyCatCookies() {
    }

    public static void initCookie(String name, String value) {
        Date expires = new Date(System.currentTimeMillis() + (1000L * 3600L * 24L * (long) GuiConstant.EXP_DAYS));
        if ((Cookies.getCookie(name) == null) || (Cookies.getCookie(name).equalsIgnoreCase("null"))) {
            Cookies.setCookie(name, value, expires);
        }
    }

    public static void updateCookie(String name, String value) {
        Date expires = new Date(System.currentTimeMillis() + (1000L * 3600L * 24L * (long) GuiConstant.EXP_DAYS));
        Cookies.removeCookie(name);
        Cookies.setCookie(name, value, expires);
    }

    public static boolean areInterfaceMeasuresSaved() {
        if (((Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_WIDTH) != null) || !(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_WIDTH).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT) != null) || !(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT) != null) || !(Cookies.getCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT) != null) || !(Cookies.getCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.DOC_LIST_WIDTH) != null) || !(Cookies.getCookie(CookiesNamespace.DOC_LIST_WIDTH).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.DOC_LIST_HEIGHT) != null) || !(Cookies.getCookie(CookiesNamespace.DOC_LIST_HEIGHT).equalsIgnoreCase("null")))
                && ((Cookies.getCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT) != null) || !(Cookies.getCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT).equalsIgnoreCase("null")))) {
            return true;
        }
        return false;
    }
}
