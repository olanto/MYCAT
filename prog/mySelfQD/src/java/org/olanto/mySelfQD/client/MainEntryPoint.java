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
package org.olanto.mySelfQD.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Main entry point.
 *
 *
 */
public class MainEntryPoint implements EntryPoint {

    // This is the component of the head of the interface
    // where we can put the query of the TextAligner
    private ParseWidget myParseWidget;
    private myParseServiceAsync rpcM;
    private VerticalPanel mainWidget = new VerticalPanel();

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @SuppressWarnings("static-access")
    @Override
    public void onModuleLoad() {
        rpcM = RpcInit.initRpc();
        RootPanel.get("content").add(mainWidget);
        mainWidget.setWidth("100%");
        mainWidget.setStyleName("mainPage");
        getPropertiesMyParse();
    }

    private void getPropertiesMyParse() {
        rpcM.InitPropertiesFromFile(Cookies.getCookie(CookiesNamespace.InterfaceLanguage), new AsyncCallback<GwtProp>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Couldn't get properties List");
            }

            @Override
            public void onSuccess(GwtProp result) {
                InitProperties(result);
                if (GuiConstant.MAXIMIZE_ON) {
                    Window.moveTo(0, 0);
                    Window.resizeTo(getScreenWidth(), getScreenHeight());
                    maximize();
                }
                setParseWidget();
            }
        });
    }

    public void setParseWidget() {
        Document.get().setTitle(GuiConstant.SELF_QD);
        fixGwtNav();
        initCookiesMyParse();
        myParseWidget = new ParseWidget();
        mainWidget.add(myParseWidget);
        mainWidget.setCellHorizontalAlignment(myParseWidget, HorizontalPanel.ALIGN_CENTER);
        myParseWidget.draWidget();
        myParseWidget.mainWidget.setWidth("100%");
        myParseWidget.statusPanel.setWidth("100%");
        myParseWidget.headPanel.setWidth("100%");
        myParseWidget.adaptSize();

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
//                Window.alert("History item :" + event.getValue());
                myParseWidget.getRefIntoView(event.getValue());
            }
        });
    }

    private void initCookiesMyParse() {
        MyParseCookies.initCookie(CookiesNamespace.ParseMinFreq, "6");
        MyParseCookies.initCookie(CookiesNamespace.ParseMinLength, "3");
        MyParseCookies.initCookie(CookiesNamespace.InterfaceLanguage, "en");
    }

    public static void download(String fileDownloadURL) {
        Frame fileDownloadFrame = new Frame(fileDownloadURL);
        fileDownloadFrame.setSize("0px", "0px");
        fileDownloadFrame.setVisible(false);
        RootPanel panel = RootPanel.get("__gwt_downloadFrame");
        while (panel.getWidgetCount() > 0) {
            panel.remove(0);
        }
        panel.add(fileDownloadFrame);
    }

    private void InitProperties(GwtProp CONST) {
        GuiConstant.SAVE_ON = CONST.SAVE_ON;
        GuiConstant.MAXIMIZE_ON = CONST.MAXIMIZE_ON;
        GuiConstant.EXP_DAYS = CONST.EXP_DAYS;
        GuiConstant.MIN_FREQ = CONST.MIN_FREQ;
        GuiConstant.MAX_FREQ = CONST.MAX_FREQ;
        GuiConstant.MIN_OCCU = CONST.MIN_OCCU;
        GuiConstant.MAX_OCCU = CONST.MAX_OCCU;
        GuiConstant.CHARACTER_WIDTH = CONST.CHARACTER_WIDTH;
        GuiConstant.JOBS_ITEMS = CONST.JOBS_ITEMS;
        GuiConstant.SELF_QD = CONST.SELF_QD;
        GuiConstant.W_OPEN_FEATURES = CONST.W_OPEN_FEATURES;
        GuiConstant.OLANTO_URL = CONST.OLANTO_URL;
        GuiConstant.SQD_HELP_URL = CONST.SQD_HELP_URL;
        GuiConstant.SELFQD_FILE_EXT = CONST.SELFQD_FILE_EXT;
        GuiConstant.LOGO_PATH = CONST.LOGO_PATH;
        GuiConstant.LOGO_URL = CONST.LOGO_URL;
        GuiConstant.WIDGET_BTN_SUBMIT = CONST.WIDGET_BTN_SUBMIT;
        GuiConstant.WIDGET_BTN_HELP = CONST.WIDGET_BTN_HELP;
        GuiConstant.WIDGET_BTN_SQD_SAVE = CONST.WIDGET_BTN_SQD_SAVE;
        GuiConstant.WIDGET_BTN_SQD_LN = CONST.WIDGET_LBL_SQD_LN;
        GuiConstant.WIDGET_BTN_SQD_FRQ = CONST.WIDGET_LBL_SQD_FRQ;
        GuiConstant.FEEDBACK_MAIL = CONST.FEEDBACK_MAIL;
        GuiConstant.CHOOSE_GUI_LANG = CONST.CHOOSE_GUI_LANG;
        GuiConstant.CHOOSE_GUI_LANG_LIST = CONST.CHOOSE_GUI_LANG_LIST;
        GuiConstant.MSG_1 = CONST.MSG_1;
        GuiConstant.MSG_2 = CONST.MSG_2;
        GuiConstant.MSG_3 = CONST.MSG_3;
        GuiConstant.MSG_4 = CONST.MSG_4;
        GuiConstant.MSG_5 = CONST.MSG_5;
        GuiConstant.MSG_6 = CONST.MSG_6;
        GuiConstant.MSG_7 = CONST.MSG_7;
        GuiConstant.MSG_8 = CONST.MSG_8;
        GuiConstant.MSG_9 = CONST.MSG_9;
        GuiConstant.MSG_10 = CONST.MSG_10;
        GuiConstant.MSG_11 = CONST.MSG_11;
        GuiConstant.MSG_12 = CONST.MSG_12;
        GuiConstant.MSG_13 = CONST.MSG_13;
        GuiConstant.MSG_14 = CONST.MSG_14;
        GuiConstant.MSG_15 = CONST.MSG_15;

//        Window.alert(GuiConstant.show());
    }

    public static native void fixGwtNav() /*-{
     $wnd.gwtnav = function(a) {
     var realhref = decodeURI(a.href.split("#")[1].split("?")[0]);
     @com.google.gwt.user.client.History::newItem(Ljava/lang/String;)(realhref);
     return false;
     }
     }-*/;

    // add missing instructions for all navigators + moveTo(0,0)
    public static native void maximize() /*-{
     top.window.moveTo(0,0);
     top.window.resizeTo(screen.availWidth, screen.availHeight);
     }-*/;

    public static native int getScreenWidth() /*-{
     return screen.availWidth;
     }-*/;

    public static native int getScreenHeight() /*-{
     return screen.availHeight;
     }-*/;
}
