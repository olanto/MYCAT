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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * pour stocker les propriétés
 */
public class InterfaceMeasures implements IsSerializable {

    /**
     * client basic parameters
     * **********************************************************************************
     */
    public int TA_TEXTAREA_WIDTH;
    public int TA_TEXTAREA_HEIGHT;
    public int QD_HTMLAREA_HEIGHT;
    public int QD_TEXTAREA_HEIGHT;
    public int DOC_LIST_WIDTH;
    public int DOC_LIST_HEIGHT;
    public int QD_DOC_LIST_HEIGHT;
    public int utilWidth;
    private int TAutilHeight;
    private int QDutilHeight;

    public void checkAndSetMinMeasures() {
        this.TA_TEXTAREA_WIDTH = (this.TA_TEXTAREA_WIDTH > GuiConstant.TA_TEXTAREA_WIDTH_MIN) ? this.TA_TEXTAREA_WIDTH : GuiConstant.TA_TEXTAREA_WIDTH_MIN;
        this.TA_TEXTAREA_HEIGHT = (this.TA_TEXTAREA_HEIGHT > GuiConstant.TA_TEXTAREA_HEIGHT_MIN) ? this.TA_TEXTAREA_HEIGHT : GuiConstant.TA_TEXTAREA_HEIGHT_MIN;
        this.QD_HTMLAREA_HEIGHT = (this.QD_HTMLAREA_HEIGHT > GuiConstant.QD_HTMLAREA_HEIGHT_MIN) ? this.QD_HTMLAREA_HEIGHT : GuiConstant.QD_HTMLAREA_HEIGHT_MIN;
        this.QD_TEXTAREA_HEIGHT = (this.QD_TEXTAREA_HEIGHT > GuiConstant.QD_TEXTAREA_HEIGHT_MIN) ? this.QD_TEXTAREA_HEIGHT : GuiConstant.QD_TEXTAREA_HEIGHT_MIN;
        this.DOC_LIST_WIDTH = (this.DOC_LIST_WIDTH > GuiConstant.DOC_LIST_WIDTH_MIN) ? this.DOC_LIST_WIDTH : GuiConstant.DOC_LIST_WIDTH_MIN;
        this.DOC_LIST_HEIGHT = (this.DOC_LIST_HEIGHT > GuiConstant.DOC_LIST_HEIGHT_MIN) ? this.DOC_LIST_HEIGHT : GuiConstant.DOC_LIST_HEIGHT_MIN;
        this.QD_DOC_LIST_HEIGHT = (this.QD_DOC_LIST_HEIGHT > GuiConstant.QD_DOC_LIST_HEIGHT_MIN) ? this.QD_DOC_LIST_HEIGHT : GuiConstant.QD_DOC_LIST_HEIGHT_MIN;
    }

    public void checkAndSetMaxMeasures() {
        this.TA_TEXTAREA_WIDTH = (this.TA_TEXTAREA_WIDTH < GuiConstant.TA_TEXTAREA_WIDTH_MAX) ? this.TA_TEXTAREA_WIDTH : GuiConstant.TA_TEXTAREA_WIDTH_MAX;
        this.TA_TEXTAREA_HEIGHT = (this.TA_TEXTAREA_HEIGHT < GuiConstant.TA_TEXTAREA_HEIGHT_MAX) ? this.TA_TEXTAREA_HEIGHT : GuiConstant.TA_TEXTAREA_HEIGHT_MAX;
        this.QD_HTMLAREA_HEIGHT = (this.QD_HTMLAREA_HEIGHT < GuiConstant.QD_HTMLAREA_HEIGHT_MAX) ? this.QD_HTMLAREA_HEIGHT : GuiConstant.QD_HTMLAREA_HEIGHT_MAX;
        this.QD_TEXTAREA_HEIGHT = (this.QD_TEXTAREA_HEIGHT < GuiConstant.QD_TEXTAREA_HEIGHT_MAX) ? this.QD_TEXTAREA_HEIGHT : GuiConstant.QD_TEXTAREA_HEIGHT_MAX;
        this.DOC_LIST_WIDTH = (this.DOC_LIST_WIDTH < GuiConstant.DOC_LIST_WIDTH_MAX) ? this.DOC_LIST_WIDTH : GuiConstant.DOC_LIST_WIDTH_MAX;
        this.DOC_LIST_HEIGHT = (this.DOC_LIST_HEIGHT < GuiConstant.DOC_LIST_HEIGHT_MAX) ? this.DOC_LIST_HEIGHT : GuiConstant.DOC_LIST_HEIGHT_MAX;
        this.QD_DOC_LIST_HEIGHT = (this.QD_DOC_LIST_HEIGHT < GuiConstant.QD_DOC_LIST_HEIGHT_MAX) ? this.QD_DOC_LIST_HEIGHT : GuiConstant.QD_DOC_LIST_HEIGHT_MAX;
    }

    public void setDefaultMeasures() {
        this.TA_TEXTAREA_WIDTH = GuiConstant.TA_TEXTAREA_WIDTH;
        this.TA_TEXTAREA_HEIGHT = GuiConstant.QD_DOC_LIST_HEIGHT;
        this.QD_HTMLAREA_HEIGHT = GuiConstant.QD_HTMLAREA_HEIGHT;
        this.QD_TEXTAREA_HEIGHT = GuiConstant.QD_TEXTAREA_HEIGHT;
        this.DOC_LIST_WIDTH = GuiConstant.DOC_LIST_WIDTH;
        this.DOC_LIST_HEIGHT = GuiConstant.DOC_LIST_HEIGHT;
        this.QD_DOC_LIST_HEIGHT = GuiConstant.QD_DOC_LIST_HEIGHT;
        this.utilWidth = Window.getClientWidth();
    }

    public void setMeasuresfromCookies() {
        this.TA_TEXTAREA_WIDTH = Integer.parseInt(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_WIDTH));
        this.TA_TEXTAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT));
        this.QD_HTMLAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT));
        this.QD_TEXTAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT));
        this.DOC_LIST_WIDTH = Integer.parseInt(Cookies.getCookie(CookiesNamespace.DOC_LIST_WIDTH));
        this.DOC_LIST_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.DOC_LIST_HEIGHT));
        this.QD_DOC_LIST_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT));
        this.utilWidth = Window.getClientWidth();
    }

    public void saveMeasuresInCookies() {
        MyCatCookies.updateCookie(CookiesNamespace.TA_TEXTAREA_WIDTH, "" + this.TA_TEXTAREA_WIDTH);
        MyCatCookies.updateCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT, "" + this.TA_TEXTAREA_HEIGHT);
        MyCatCookies.updateCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT, "" + this.QD_TEXTAREA_HEIGHT);
        MyCatCookies.updateCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT, "" + this.QD_HTMLAREA_HEIGHT);
        MyCatCookies.updateCookie(CookiesNamespace.DOC_LIST_WIDTH, "" + this.DOC_LIST_WIDTH);
        MyCatCookies.updateCookie(CookiesNamespace.DOC_LIST_HEIGHT, "" + this.DOC_LIST_HEIGHT);
        MyCatCookies.updateCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT, "" + this.QD_DOC_LIST_HEIGHT);
    }

    public void calculateMeasures(int wHeight, int wWidth) {
        utilWidth = wWidth - GuiConstant.TA_OVERHEAD_MAX_L;
        TAutilHeight = wHeight - GuiConstant.TA_OVERHEAD_MAX_H;
        QDutilHeight = wHeight - GuiConstant.QD_OVERHEAD_MAX_H;
        this.DOC_LIST_WIDTH = utilWidth * GuiConstant.PER_DOC_LIST_W / 100;
        this.DOC_LIST_HEIGHT = TAutilHeight;
        this.TA_TEXTAREA_WIDTH = (utilWidth - this.DOC_LIST_WIDTH) / (2 * GuiConstant.TA_CHAR_WIDTH);
        this.TA_TEXTAREA_HEIGHT = (this.DOC_LIST_HEIGHT - GuiConstant.TA_OVERHEAD_H) / GuiConstant.TA_LINE_HEIGHT;
        this.QD_HTMLAREA_HEIGHT = QDutilHeight * GuiConstant.PER_QD_HTMLAREA_H / 100;
        this.QD_DOC_LIST_HEIGHT = QDutilHeight - this.QD_HTMLAREA_HEIGHT;
        this.QD_TEXTAREA_HEIGHT = (this.QD_DOC_LIST_HEIGHT - GuiConstant.TA_OVERHEAD_H) / GuiConstant.TA_LINE_HEIGHT;
        checkAndSetMaxMeasures();
        checkAndSetMinMeasures();
        if (GuiConstant.DEBUG_ON) {
            Window.alert("TA_TEXTAREA_WIDTH = " + this.TA_TEXTAREA_WIDTH
                    + "\n TA_TEXTAREA_HEIGHT  = " + this.TA_TEXTAREA_HEIGHT
                    + "\n QD_HTMLAREA_HEIGHT  = " + this.QD_HTMLAREA_HEIGHT
                    + "\n QD_TEXTAREA_HEIGHT = " + this.QD_TEXTAREA_HEIGHT
                    + "\n DOC_LIST_WIDTH  = " + this.DOC_LIST_WIDTH
                    + "\n DOC_LIST_HEIGHT  = " + this.DOC_LIST_HEIGHT
                    + "\n QD_DOC_LIST_HEIGHT = " + this.QD_DOC_LIST_HEIGHT
                    + "\n Util Height  = " + TAutilHeight
                    + "\n Util Width  = " + utilWidth);
        }
    }

    public void calculateMeasuresCall(int wHeight, int wWidth) {
        utilWidth = wWidth - GuiConstant.TA_OVERHEAD_MAX_L - 40;
        TAutilHeight = wHeight - (GuiConstant.TA_OVERHEAD_MAX_H / 4);
        this.TA_TEXTAREA_WIDTH = utilWidth / (2 * GuiConstant.TA_CHAR_WIDTH);
        this.TA_TEXTAREA_HEIGHT = TAutilHeight / GuiConstant.TA_LINE_HEIGHT;
        if (GuiConstant.DEBUG_ON) {
            Window.alert("TA_TEXTAREA_WIDTH = " + this.TA_TEXTAREA_WIDTH
                    + "\n TA_TEXTAREA_HEIGHT  = " + this.TA_TEXTAREA_HEIGHT
                    + "\n Util Height  = " + TAutilHeight
                    + "\n Util Width  = " + utilWidth);
        }
    }
}
