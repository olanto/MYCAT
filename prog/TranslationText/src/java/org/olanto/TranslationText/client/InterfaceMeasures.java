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

    public void updateMeasures(int taWidth, int taHeight, int qdHaHeight, int qdTaHeigt, int dlWidth, int dlHeight, int qdDlHeight) {
        this.TA_TEXTAREA_WIDTH = taWidth;
        this.TA_TEXTAREA_HEIGHT = taHeight;
        this.QD_HTMLAREA_HEIGHT = qdHaHeight;
        this.QD_TEXTAREA_HEIGHT = qdTaHeigt;
        this.DOC_LIST_WIDTH = dlWidth;
        this.DOC_LIST_HEIGHT = dlHeight;
        this.QD_DOC_LIST_HEIGHT = qdDlHeight;
    }

    public void checkAndSetMinMeasures() {
        this.TA_TEXTAREA_WIDTH = (this.TA_TEXTAREA_WIDTH > GuiConstant.TA_TEXTAREA_WIDTH_MIN) ? this.TA_TEXTAREA_WIDTH : GuiConstant.TA_TEXTAREA_WIDTH_MIN;
        this.TA_TEXTAREA_HEIGHT = (this.TA_TEXTAREA_HEIGHT > GuiConstant.TA_TEXTAREA_HEIGHT_MIN) ? this.TA_TEXTAREA_HEIGHT : GuiConstant.TA_TEXTAREA_HEIGHT_MIN;
        this.QD_HTMLAREA_HEIGHT = (this.QD_HTMLAREA_HEIGHT > GuiConstant.QD_HTMLAREA_HEIGHT_MIN) ? this.QD_HTMLAREA_HEIGHT : GuiConstant.QD_HTMLAREA_HEIGHT_MIN;
        this.QD_TEXTAREA_HEIGHT = (this.QD_TEXTAREA_HEIGHT > GuiConstant.QD_TEXTAREA_HEIGHT_MIN) ? this.QD_TEXTAREA_HEIGHT : GuiConstant.QD_TEXTAREA_HEIGHT_MIN;
        this.DOC_LIST_WIDTH = (this.DOC_LIST_WIDTH > GuiConstant.DOC_LIST_WIDTH_MIN) ? this.DOC_LIST_WIDTH : GuiConstant.DOC_LIST_WIDTH_MIN;
        this.DOC_LIST_HEIGHT = (this.DOC_LIST_HEIGHT > GuiConstant.DOC_LIST_HEIGHT_MIN) ? this.DOC_LIST_HEIGHT : GuiConstant.DOC_LIST_HEIGHT_MIN;
        this.QD_DOC_LIST_HEIGHT = (this.QD_DOC_LIST_HEIGHT > GuiConstant.QD_DOC_LIST_HEIGHT_MIN) ? this.QD_DOC_LIST_HEIGHT : GuiConstant.QD_DOC_LIST_HEIGHT_MIN;
    }
    
    public void recalculate(int clientWidth, int clientheight, int charWidth, int lineHeight, int qdDlHeight) {
    }

    public void setDefaultMeasures() {
        this.TA_TEXTAREA_WIDTH = GuiConstant.TA_TEXTAREA_WIDTH;
        this.TA_TEXTAREA_HEIGHT = GuiConstant.QD_DOC_LIST_HEIGHT;
        this.QD_HTMLAREA_HEIGHT = GuiConstant.QD_HTMLAREA_HEIGHT;
        this.QD_TEXTAREA_HEIGHT = GuiConstant.QD_TEXTAREA_HEIGHT;
        this.DOC_LIST_WIDTH = GuiConstant.DOC_LIST_WIDTH;
        this.DOC_LIST_HEIGHT = GuiConstant.DOC_LIST_HEIGHT;
        this.QD_DOC_LIST_HEIGHT = GuiConstant.QD_DOC_LIST_HEIGHT;
    }

    public void setMeasuresfromCookies() {
        this.TA_TEXTAREA_WIDTH = Integer.parseInt(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_WIDTH));
        this.TA_TEXTAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT));
        this.QD_HTMLAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT));
        this.QD_TEXTAREA_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT));
        this.DOC_LIST_WIDTH = Integer.parseInt(Cookies.getCookie(CookiesNamespace.DOC_LIST_WIDTH));
        this.DOC_LIST_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.DOC_LIST_HEIGHT));
        this.QD_DOC_LIST_HEIGHT = Integer.parseInt(Cookies.getCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT));
    }
}
