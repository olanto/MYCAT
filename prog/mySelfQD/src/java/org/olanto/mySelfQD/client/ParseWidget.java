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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;

/**
 * modif 2012.4.28 J. Guyot modif du minquote passé de 6 à 3
 */
public class ParseWidget extends Composite {

    public VerticalPanel mainWidget = new VerticalPanel();
    public HorizontalPanel statusPanel = new HorizontalPanel();
    private HorizontalPanel headerPanel = new HorizontalPanel();
    public HorizontalPanel headPanel = new HorizontalPanel();
    private VerticalPanel leftheadPanel = new VerticalPanel();
    private HorizontalPanel contact = new HorizontalPanel();
    public Button GoSrch = new Button(GuiConstant.WIDGET_BTN_SUBMIT);
    public Uploader fileUpload = new Uploader();
    public ListBox minFreq = new ListBox();
    public ListBox minLength = new ListBox();
    private ContentPanel mainContainer = new ContentPanel();
    private ContentPanel headerContainer = new ContentPanel();
    private ContentPanel statusContainer = new ContentPanel();
    private myParseServiceAsync rpcRef;
    public Label msg = new Label();
    public Label minFr = new Label(GuiConstant.WIDGET_BTN_SQD_FRQ);
    public Label minLn = new Label(GuiConstant.WIDGET_BTN_SQD_LN);
    private Label SQDText = new Label();
    private Image im = new Image(GuiConstant.LOGO_PATH);
    public Button help = new Button(GuiConstant.WIDGET_BTN_HELP);
    public Button save = new Button(GuiConstant.WIDGET_BTN_SQD_SAVE);
    private String fileName = null;
    private String fileContent = "";
    private String ref;
    public HtmlContainer refArea = new HtmlContainer();
    public ScrollPanel htmlWrapper = new ScrollPanel(refArea);
    public int refIdx = 0;
    // Height and width units for buttons and text labels
    private final int H_Unit = 30;
    private final int W_Unit = 20;
    private static final int CHAR_W = GuiConstant.CHARACTER_WIDTH;
    private TabSet topJobsSet = new TabSet();
    private final String[] INT_LANG = GuiConstant.CHOOSE_GUI_LANG_LIST.split("\\;");
    private Label chooseLang = new Label(GuiConstant.MSG_9);
    private ListBox langInterface = new ListBox();
    private NodeList<Element> refs;

    public ParseWidget() {
        rpcRef = RpcInit.initRpc();
        setHeader();
    }

    private void setHeader() {
        initWidget(mainWidget);
        mainWidget.setStyleName("mainContent");
        mainWidget.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

        mainWidget.add(headerContainer);
        mainWidget.add(mainContainer);
        mainWidget.add(statusContainer);

        headerContainer.setBodyBorder(true);
        headerContainer.setHeaderVisible(false);
        headerContainer.add(headPanel);
        headerContainer.setStylePrimaryName("searchHeader");
        headPanel.setStylePrimaryName("searchHeader");
        headPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        headerPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        headerPanel.add(fileUpload);
        headerPanel.add(GoSrch);
        headerPanel.add(minLn);
        headerPanel.add(minLength);
        headerPanel.add(minFr);
        headerPanel.add(minFreq);
        headerPanel.add(help);
        headerPanel.add(save);
        if (GuiConstant.CHOOSE_GUI_LANG) {
            headerPanel.add(chooseLang);
            headerPanel.add(langInterface);
        }

        headerPanel.setStylePrimaryName("searchHeader");

        headPanel.add(leftheadPanel);
        headPanel.add(SQDText);
        headPanel.setCellHorizontalAlignment(SQDText, HorizontalPanel.ALIGN_RIGHT);
        if ((!GuiConstant.LOGO_PATH.isEmpty()) && (!GuiConstant.LOGO_PATH.isEmpty())) {
            if ((!GuiConstant.LOGO_PATH.equalsIgnoreCase(" ")) && (!GuiConstant.LOGO_PATH.equalsIgnoreCase(" "))) {
                headPanel.add(im);
                headPanel.setCellHorizontalAlignment(im, HorizontalPanel.ALIGN_RIGHT);
            }
        }

        topJobsSet.setTabBarPosition(Side.TOP);
        topJobsSet.setDefaultHeight(H_Unit);
        topJobsSet.setPaneContainerClassName("searchHeader");
        leftheadPanel.add(topJobsSet);
        leftheadPanel.add(headerPanel);

        mainContainer.setBodyBorder(true);
        mainContainer.setHeaderVisible(false);
        mainContainer.setStylePrimaryName("mainContent");

        statusContainer.setBodyBorder(true);
        statusContainer.setHeaderVisible(false);
        statusContainer.add(statusPanel);
        statusPanel.setHeight(H_Unit + "px");
        statusPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        statusPanel.add(msg);
        statusPanel.setCellHorizontalAlignment(msg, HorizontalPanel.ALIGN_LEFT);
        statusPanel.add(contact);
        statusPanel.setCellHorizontalAlignment(contact, HorizontalPanel.ALIGN_RIGHT);

        contact.setPixelSize(300, 25);
        contact.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        Anchor poweredBy = new Anchor(GuiConstant.OLANTO_URL.substring(0, GuiConstant.OLANTO_URL.indexOf("|")), true,
                GuiConstant.OLANTO_URL.substring(GuiConstant.OLANTO_URL.indexOf("|") + 1, GuiConstant.OLANTO_URL.lastIndexOf("|")),
                GuiConstant.OLANTO_URL.substring(GuiConstant.OLANTO_URL.lastIndexOf("|") + 1));
        contact.add(poweredBy);
        poweredBy.setStylePrimaryName("contactInfo");
        contact.setCellHorizontalAlignment(poweredBy, HorizontalPanel.ALIGN_LEFT);
        contact.add(new HTML("&nbsp;||&nbsp;"));
        Anchor feedback = new Anchor(GuiConstant.FEEDBACK_MAIL.substring(0, GuiConstant.FEEDBACK_MAIL.indexOf("|")));
        contact.add(new HTML("&nbsp;"));
        contact.setCellHorizontalAlignment(feedback, HorizontalPanel.ALIGN_RIGHT);
        contact.add(feedback);
        feedback.setStylePrimaryName("contactInfo");

        statusContainer.setStylePrimaryName("statusPanel");
        statusPanel.setStylePrimaryName("statusPanel");

        mainContainer.add(htmlWrapper);

        refArea.setVisible(true);
        refArea.setStyleName("TextBlock");
        fileUpload.addOnStartUploadHandler(onStartUploaderHandler);
        fileUpload.addOnFinishUploadHandler(onFinishUploaderHandler);
        help.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                Window.open(GuiConstant.SQD_HELP_URL, "", GuiConstant.W_OPEN_FEATURES);
            }
        });
        fileUpload.setAutoSubmit(true);
        setMessage("warning", GuiConstant.MSG_5);
        for (int i = GuiConstant.MIN_FREQ; i < GuiConstant.MAX_FREQ; i++) {
            minFreq.addItem("" + i);
        }
        for (int i = GuiConstant.MIN_OCCU; i < GuiConstant.MAX_OCCU; i++) {
            minLength.addItem("" + i);
        }
        minLength.setSelectedIndex(Integer.parseInt(Cookies.getCookie(CookiesNamespace.ParseMinLength)));
        minFreq.setSelectedIndex(Integer.parseInt(Cookies.getCookie(CookiesNamespace.ParseMinFreq)));

        minFreq.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                MyParseCookies.updateCookie(CookiesNamespace.ParseMinFreq, "" + minFreq.getSelectedIndex());
            }
        });
        minLength.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                MyParseCookies.updateCookie(CookiesNamespace.ParseMinLength, "" + minLength.getSelectedIndex());
            }
        });
        if (GuiConstant.CHOOSE_GUI_LANG) {
            langInterface.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    MyParseCookies.updateCookie(CookiesNamespace.InterfaceLanguage, INT_LANG[langInterface.getSelectedIndex()]);
                    Window.Location.reload();
                }
            });
        }
        if ((!GuiConstant.LOGO_PATH.isEmpty()) && (!GuiConstant.LOGO_PATH.isEmpty())) {
            im.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.open(GuiConstant.LOGO_URL, "", GuiConstant.W_OPEN_FEATURES);
                }
            });
        }
        feedback.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mailto(GuiConstant.FEEDBACK_MAIL.substring(GuiConstant.FEEDBACK_MAIL.lastIndexOf("|") + 1), GuiConstant.MSG_6 + GuiConstant.SELF_QD);
            }
        });
    }

    public void mailto(String address, String subject) {
        Window.open("mailto:" + address + "?subject=" + URL.encode(subject), "_blank", "");
    }

    public void draWidget() {
        setbuttonstyle(GoSrch, GoSrch.getText().length() * 2 * CHAR_W, H_Unit);
        setbuttonstyle(help, help.getText().length() * (CHAR_W + 2), H_Unit);
        setbuttonstyle(save, save.getText().length() * (CHAR_W + 2), H_Unit);
        minFr.setStyleName("gwt-w-label");
        minLn.setStyleName("gwt-w-label");
        SQDText.setText(GuiConstant.SELF_QD);
        SQDText.setStyleName("gwt-im-text");
        chooseLang.setStyleName("gwt-w-label");

        for (int i = 0; i < INT_LANG.length; i++) {
            langInterface.addItem(INT_LANG[i]);
        }
        langInterface.setWidth(2 * W_Unit + "px");
        langInterface.setSelectedIndex(getIndex(INT_LANG, Cookies.getCookie(CookiesNamespace.InterfaceLanguage)));

        htmlWrapper.setPixelSize((Window.getClientWidth() - 2 * W_Unit), (Window.getClientHeight() - 4 * H_Unit));
        refArea.setWidth((Window.getClientWidth() - 3 * W_Unit) + "px");
        if ((GuiConstant.JOBS_ITEMS != null) && (GuiConstant.JOBS_ITEMS.length() > 1)) {
            String jobs = GuiConstant.JOBS_ITEMS;
            String[] tablist = jobs.split("\\;");
            Tab tinv = new Tab();
            topJobsSet.addTab(tinv);
            tinv.setTitle("Select Tab : ");
            tinv.setID("Empty");
            tinv.setDisabled(true);
            if (tablist.length > 0) {
                for (int i = 0; i < tablist.length; i++) {
                    Tab tTab = new Tab();
                    tTab.setIcon("./icons/16/folder_document.png");
                    tTab.setTitle(tablist[i].substring(0, tablist[i].indexOf("|")));
                    tTab.setAttribute("URL", tablist[i].substring(tablist[i].indexOf("|") + 1, tablist[i].lastIndexOf("|")));
                    tTab.setAttribute("MODE", tablist[i].substring(tablist[i].lastIndexOf("|") + 1));
                    topJobsSet.addTab(tTab);
                }
            }
            topJobsSet.setSelectedTab(0);
            topJobsSet.addTabSelectedHandler(new TabSelectedHandler() {
                @Override
                public void onTabSelected(TabSelectedEvent event) {
                    if (!(event.getTab().getID().contains("Empty"))) {
                        Window.open(event.getTab().getAttribute("URL"), event.getTab().getAttribute("MODE"), "");
                    }
                }
            });
            topJobsSet.setWidth(headerPanel.getOffsetWidth());
            topJobsSet.draw();
            topJobsSet.setCursor(Cursor.HAND);
        } else {
            leftheadPanel.remove(topJobsSet);
        }
        adaptSize();
    }

    public void setbuttonstyle(Button b, int w, int h) {
        b.setStyleName("x-btn-click");
        b.setPixelSize(w, h);
    }
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                // need gwtupload version 0.6.3 to use this feature
                final IUploader.UploadedInfo info = uploader.getServerInfo();
                fileName = info.name;
                fileContent = info.message;
                setMessage("info", GuiConstant.MSG_2 + fileName);
                fileUpload.setTitle(GuiConstant.MSG_2 + fileName);
                if (fileName.endsWith(GuiConstant.SELFQD_FILE_EXT)) {
                    setMessage("info", GuiConstant.MSG_4 + fileName);
                    ref = fileContent;
                    setMessage("info", GuiConstant.MSG_3);
                    refArea.setHtml(ref);
                    refs = refArea.getElement().getElementsByTagName("a");
                    save.removeAllListeners();
                    save.addListener(Events.OnClick, new Listener<BaseEvent>() {
                        @Override
                        public void handleEvent(BaseEvent be) {
                            MyParseDownload.downloadFileFromServer(getSavedFileName() + GuiConstant.SELFQD_FILE_EXT, ref);
                            setMessage("info", GuiConstant.MSG_7 + fileName + GuiConstant.MSG_8 + getSavedFileName() + GuiConstant.SELFQD_FILE_EXT);
                        }
                    });
                } else {
                    GoSrch.enable();
                    GoSrch.removeAllListeners();
                    GoSrch.addListener(Events.OnClick, new Listener<BaseEvent>() {
                        @Override
                        public void handleEvent(BaseEvent be) {
                            GoSrch.disable();
                            drawReferences();
                        }
                    });
                }
            } else {
                setMessage("error", GuiConstant.MSG_5);
                GoSrch.enable();
            }
        }
    };
    private IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {
        @Override
        public void onStart(IUploader uploader) {
            setMessage("info", GuiConstant.MSG_11);
            refArea.setHtml("");
            GoSrch.disable();
        }
    };

    public void drawReferences() {
        int consMin = Integer.parseInt(minLength.getValue(minLength.getSelectedIndex()));
        int occMin = Integer.parseInt(minFreq.getValue(minFreq.getSelectedIndex()));
        if (fileName == null) {
            setMessage("error", GuiConstant.MSG_10);
            GoSrch.enable();
        } else {
            setMessage("info", GuiConstant.MSG_4 + fileName);
            rpcRef.getHtmlRef(fileContent, fileName, occMin, consMin, GuiConstant.SELFQD_FILE_EXT, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    setMessage("error", GuiConstant.MSG_13);
                    GoSrch.enable();
                }

                @Override
                public void onSuccess(String result) {
                    if (result != null) {
                        ref = result;
                        setMessage("info", GuiConstant.MSG_3);
                        refArea.setHtml(ref);
                        adaptSize();
                        refs = refArea.getElement().getElementsByTagName("a");
                        save.removeAllListeners();
                        save.addListener(Events.OnClick, new Listener<BaseEvent>() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                MyParseDownload.downloadFileFromServer(getSavedFileName() + GuiConstant.SELFQD_FILE_EXT, ref);
                                setMessage("info", GuiConstant.MSG_7 + fileName + GuiConstant.MSG_8 + getSavedFileName() + GuiConstant.SELFQD_FILE_EXT);
                            }
                        });
                        GoSrch.enable();
                    } else {
                        setMessage("error", GuiConstant.MSG_15);
                        GoSrch.enable();
                    }
                }
            });
        }
    }

    public void getRefIntoView(String RefName) {
        for (int i = 0; i < refs.getLength(); i++) {
            if (refs.getItem(i).getAttribute("name").contains(RefName)) {
//                Window.alert("Found ref item : " + i);
                refArea.getElement().getElementsByTagName("a").getItem(i).scrollIntoView();
                break;
            }
        }
    }

    private void setMessage(String type, String message) {
        msg.setStyleName("gwt-TA-" + type.toLowerCase());
        msg.setText(message);
    }

    public void adaptSize() {
        int width = getMaximumWidth();
        statusPanel.setPixelSize(width, statusPanel.getOffsetHeight());
        headPanel.setPixelSize(width, headPanel.getOffsetHeight());
        mainContainer.setPixelSize(width, mainContainer.getOffsetHeight());
        msg.setWidth((width - contact.getOffsetWidth()) + "px");
    }

    private int getMaximumWidth() {
        int max = mainContainer.getOffsetWidth();
        if (statusPanel.getOffsetWidth() > max) {
            max = statusPanel.getOffsetWidth();
        }
        if (headPanel.getOffsetWidth() > max) {
            max = headPanel.getOffsetWidth();
        }
        return max;
    }

    private String getSavedFileName() {
        int idx = fileName.lastIndexOf("/") + 1;
        if (idx == 0) {
            idx = fileName.lastIndexOf("\\") + 1;
        }
        int ext = fileName.lastIndexOf(".");
        if (ext > idx) {
            return fileName.substring(idx, ext);
        } else if (idx > 0) {
            return fileName.substring(idx);
        }
        return fileName;
    }

    private int getIndex(String[] source, String cookie) {
        int i = 0;
        for (int j = 0; j < source.length; j++) {
            if (source[j].equalsIgnoreCase(cookie)) {
                i = j;
                break;
            }
        }
        return i;
    }
}