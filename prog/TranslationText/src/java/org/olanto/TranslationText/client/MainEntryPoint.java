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
package org.olanto.TranslationText.client;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main entry point.
 *
 *
 */
public class MainEntryPoint implements EntryPoint {

    public static final String VERSION = "3.2.4";
    // This is the component of the head of the interface
    // where we can put the query of the TextAligner
    private ResearchWidget textAlignerWidget;
    private QuoteWidget quoteDetectorWidget;
    private String fileName = "";
    String searchQuery = "";
    private BitextWidget tS;
    private TranslateServiceAsync rpcM;
    public static ArrayList<String> stopWords;
    public static String QUERY = "";
    private CollectionTreeWidget collectionWidgetTA = new CollectionTreeWidget();
    private com.smartgwt.client.widgets.Window collPopupWindowTA = new com.smartgwt.client.widgets.Window();
    private Button setColl;
    private Button clearColl;
    private Button closeColl;
    private VerticalPanel mainWidget = new VerticalPanel();
    private HorizontalPanel collTreeContainerTA = new HorizontalPanel();
    private CollectionTreeWidget collectionWidgetQD = new CollectionTreeWidget();
    private com.smartgwt.client.widgets.Window collPopupWindowQD = new com.smartgwt.client.widgets.Window();
    private Button setColl1;
    private Button clearColl1;
    private Button closeColl1;
    private HorizontalPanel collTreeContainerQD = new HorizontalPanel();
    private final int H_Unit = 30;
    private static String[] languages;
    public static ArrayList<String> words;
    public static String beforeWildTerm;
    public static String afterWildTerm;
    public static InterfaceMeasures IMeasures = new InterfaceMeasures();
    private static boolean isExternalForQD = false;
    private static boolean isExternalForSearchQuery = false;
    private static final String fileContentURLBase = GWT.getModuleBaseURL() + "ContentServlet?filename=";

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @SuppressWarnings("static-access")
    @Override
    public void onModuleLoad() {
        rpcM = RpcInit.initRpc();
        getStopWdMyCat();
        if (Window.Location.getQueryString().length() == 0) {
            RootPanel.get("content").add(mainWidget);
            mainWidget.setWidth("100%");
            mainWidget.setStyleName("mainPage");
            mainWidget.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
            getCollections();
            getPropertiesMyCat();
        } else {
            String QDfile = "undefined";
            String qdfile = "undefined";
            String file = "undefined";
            String searchQry = "undefined";
            final Map<String, List<String>> params = Window.Location.getParameterMap();

            if (params.containsKey("QDfile")) {
                QDfile = Window.Location.getParameter("QDfile");
            }
            if (params.containsKey("qdfile")) {
                qdfile = Window.Location.getParameter("qdfile");
            }
            if (params.containsKey("file")) {
                file = Window.Location.getParameter("file");
            }
            if (params.containsKey("searchquery")) {
                searchQry = Window.Location.getParameter("searchquery");
            }
            if (!searchQry.equalsIgnoreCase("undefined")) {
                isExternalForSearchQuery = true;
                searchQuery = searchQry;
                setMainWidgetPropertiesFomExternalCall();
            } else if (!QDfile.equalsIgnoreCase("undefined")) {
                isExternalForQD = true;
                fileName = QDfile;
                setMainWidgetPropertiesFomExternalCall();
            } else if (!qdfile.equalsIgnoreCase("undefined")) {
                isExternalForQD = true;
                fileName = qdfile;
                setMainWidgetPropertiesFomExternalCall();
            } else if (!file.equalsIgnoreCase("undefined")) {
                isExternalForQD = true;
                fileName = file;
                setMainWidgetPropertiesFomExternalCall();
            } else {
                rpcM.InitPropertiesFromFile("en", new AsyncCallback<GwtProp>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Couldn't get properties List, problem loading the properties files. Check if all the services are started." + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(GwtProp result) {
                        InitProperties(result);
                        if (GuiConstant.MAXIMIZE_ON) {
                            Window.moveTo(0, 0);
                            Window.resizeTo(getScreenWidth(), getScreenHeight());
                            maximize();
                        }
                        initCookies();
                        if (MyCatCookies.areInterfaceMeasuresSaved() && GuiConstant.AUTO_ON) {
                            IMeasures.setMeasuresfromCookies();
                        } else {
                            IMeasures.setDefaultMeasures();
                        }
                        String source = "undefined";
                        String query = "undefined";
                        String lS = "undefined";
                        String lT = "undefined";
                        if (params.containsKey("source")) {
                            source = Window.Location.getParameter("source");
                        }
                        if (params.containsKey("query")) {
                            query = Window.Location.getParameter("query");
                        }
                        if (params.containsKey("LSrc")) {
                            lS = Window.Location.getParameter("LSrc");
                        }
                        if (params.containsKey("LSTgt")) {
                            lT = Window.Location.getParameter("LTgt");
                        }
                        setFormWidgetFromCall(source, query, lS, lT);
                    }
                });
            }
        }
    }

    private void setFormWidgetFromCall(String source, String query, String lS, String lT) {
        final FormCallWidget FC = new FormCallWidget(source, query, lS, lT);
        RootPanel.get("call").add(FC);
        FC.pWidget.setWidth("100%");
        rpcM.getStopWords(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Warning: Could not get the list of stopwords. If the problem persists then restart the servers.");
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                FC.draWidget(result);
            }
        });
    }

    public void setMainWidgetPropertiesFomExternalCall() {
        RootPanel.get("content").add(mainWidget);
        mainWidget.setWidth("100%");
        mainWidget.setStyleName("mainPage");
        mainWidget.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        getCollections();
        getPropertiesMyCat();
    }

    private void getStopWdMyCat() {
        rpcM.getStopWords(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Warning: Could not get the list of stopwords");
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                stopWords = result;
            }
        });
    }

    private void getPropertiesMyCat() {
        rpcM.InitPropertiesFromFile(Cookies.getCookie(CookiesNamespace.InterfaceLanguage), new AsyncCallback<GwtProp>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Warning: Could not get the list of properties: " + caught.getMessage());
            }

            @Override
            public void onSuccess(GwtProp result) {
                InitProperties(result);
                if (GuiConstant.MAXIMIZE_ON) {
                    Window.moveTo(0, 0);
                    Window.resizeTo(getScreenWidth(), getScreenHeight());
                    maximize();
                }
                if (!Window.Navigator.isCookieEnabled()) {
                    Window.alert(GuiMessageConst.MSG_63);
                }
                initCookies();
                if (MyCatCookies.areInterfaceMeasuresSaved() && GuiConstant.AUTO_ON) {
                    IMeasures.setMeasuresfromCookies();
                } else {
                    IMeasures.setDefaultMeasures();
                }
                setSettingsColMycat();
                setSettingsColMyQuote();
                getLanguages();
            }
        });
    }

    private void getLanguages() {
        rpcM.getCorpusLanguages(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Warning: Could not get the list of languages");
            }

            @Override
            public void onSuccess(String[] result) {
                languages = result;
                if (isExternalForQD) {
                    setMyQuoteWidget(languages);
                    getContentListMyQuoteByFileName();
                } else if (!GuiConstant.REMOVE_TA_INTERFACE) {
                    if (isExternalForSearchQuery) {
                        setMyCatWidget();
                        getcontentlistMyCat();
                    } else {
                        setMyCatWidget();
                    }
                } else { // QD
                    setMyQuoteWidget(languages);
                }
            }
        });
    }

    private void setLanguagesQD() {
        int s = 0, t = 0;
        quoteDetectorWidget.langS.clear();
        quoteDetectorWidget.langT.clear();
        for (int i = 0; i < languages.length; i++) {
            quoteDetectorWidget.langS.addItem(languages[i]);
            quoteDetectorWidget.langT.addItem(languages[i]);
            if (languages[i].equalsIgnoreCase(Cookies.getCookie(CookiesNamespace.MyQuotelangS))) {
                s = i;
            }
            if (languages[i].equalsIgnoreCase(Cookies.getCookie(CookiesNamespace.MyQuotelangT))) {
                t = i;
            }
        }
        quoteDetectorWidget.langS.setSelectedIndex(s);
        quoteDetectorWidget.langT.setSelectedIndex(t);
    }

    private void setLanguagesTA() {
        int s = 0, t = 0;
        textAlignerWidget.langS.clear();
        textAlignerWidget.langT.clear();
        for (int i = 0; i < languages.length; i++) {
            textAlignerWidget.langS.addItem(languages[i]);
            textAlignerWidget.langT.addItem(languages[i]);
            if (languages[i].equalsIgnoreCase(Cookies.getCookie(CookiesNamespace.MyCatlangS))) {
                s = i;
            }
            if (languages[i].equalsIgnoreCase(Cookies.getCookie(CookiesNamespace.MyCatlangT))) {
                t = i;
            }
        }
        textAlignerWidget.langS.setSelectedIndex(s);
        textAlignerWidget.langT.setSelectedIndex(t);
    }

    private void getCollections() {
        rpcM.SetCollection(new AsyncCallback<CollectionTree>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Warning: Could not get the list of collections. If the problem persists then restart the servers");
            }

            @Override
            public void onSuccess(CollectionTree result) {
                collectionWidgetTA.Draw(result);
                collectionWidgetQD.Draw(result);
            }
        });
    }

    public void setSettingsColMycat() {
        collTreeContainerTA.setHeight("30px");
        collPopupWindowTA.setHeight(500);
        collPopupWindowTA.setWidth(370);
        setColl = new Button(GuiMessageConst.WIDGET_COLL_SET);
        closeColl = new Button(GuiMessageConst.WIDGET_COLL_CLOSE);
        clearColl = new Button(GuiMessageConst.WIDGET_COLL_CLEAR);

        collPopupWindowTA.setTitle(GuiMessageConst.WIDGET_COLL_WND);
        collPopupWindowTA.setCanDragReposition(true);
        collPopupWindowTA.setCanDragResize(true);
        collPopupWindowTA.setAnimateMinimize(true);
        collPopupWindowTA.setShowBody(true);
        collPopupWindowTA.setShowCloseButton(true);
        collPopupWindowTA.setShowHeader(true);
        collPopupWindowTA.setAutoCenter(true);
        collPopupWindowTA.setDismissOnOutsideClick(true);
        collPopupWindowTA.setShowTitle(true);
        collPopupWindowTA.setShowMaximizeButton(true);
        collPopupWindowTA.setShowMinimizeButton(true);
        collPopupWindowTA.addItem(collTreeContainerTA);
        collPopupWindowTA.addResizedHandler(new ResizedHandler() {
            @Override
            public void onResized(ResizedEvent event) {
                collTreeContainerTA.setWidth(collPopupWindowTA.getWidthAsString());
                collectionWidgetTA.collectionTreeGrid.setHeight(collPopupWindowTA.getHeight() - 2 * H_Unit - 10);
                collectionWidgetTA.collectionTreeGrid.setWidth(collPopupWindowTA.getWidth() - 25);
            }
        });

        collTreeContainerTA.setWidth(collPopupWindowTA.getWidthAsString());
        collectionWidgetTA.collectionTreeGrid.setHeight(collPopupWindowTA.getHeight() - 2 * H_Unit - 10);
        collectionWidgetTA.collectionTreeGrid.setWidth(collPopupWindowTA.getWidth() - 25);
        collTreeContainerTA.add(setColl);
        collTreeContainerTA.setCellHorizontalAlignment(setColl, HorizontalPanel.ALIGN_LEFT);
        collTreeContainerTA.add(closeColl);
        collTreeContainerTA.setCellHorizontalAlignment(closeColl, HorizontalPanel.ALIGN_CENTER);
        collTreeContainerTA.add(clearColl);
        collTreeContainerTA.setCellHorizontalAlignment(clearColl, HorizontalPanel.ALIGN_RIGHT);

        setbuttonstyle(setColl);
        setbuttonstyle(clearColl);
        setbuttonstyle(closeColl);
    }

    public void setSettingsColMyQuote() {
        collTreeContainerQD.setHeight("30px");
        collPopupWindowQD.setHeight(500);
        collPopupWindowQD.setWidth(370);
        setColl1 = new Button(GuiMessageConst.WIDGET_COLL_SET);
        closeColl1 = new Button(GuiMessageConst.WIDGET_COLL_CLOSE);
        clearColl1 = new Button(GuiMessageConst.WIDGET_COLL_CLEAR);

        collPopupWindowQD.setTitle(GuiMessageConst.WIDGET_COLL_WND);
        collPopupWindowQD.setCanDragReposition(true);
        collPopupWindowQD.setCanDragResize(true);
        collPopupWindowQD.setAnimateMinimize(true);
        collPopupWindowQD.setShowBody(true);
        collPopupWindowQD.setShowCloseButton(true);
        collPopupWindowQD.setShowHeader(true);
        collPopupWindowQD.setAutoCenter(true);
        collPopupWindowQD.setDismissOnOutsideClick(true);
        collPopupWindowQD.setShowTitle(true);
        collPopupWindowQD.setShowMaximizeButton(true);
        collPopupWindowQD.setShowMinimizeButton(true);
        collPopupWindowQD.addItem(collTreeContainerQD);
        collPopupWindowQD.addResizedHandler(new ResizedHandler() {
            @Override
            public void onResized(ResizedEvent event) {
                collTreeContainerQD.setWidth(collPopupWindowQD.getWidthAsString());
                collectionWidgetQD.collectionTreeGrid.setHeight(collPopupWindowQD.getHeight() - 2 * H_Unit - 10);
                collectionWidgetQD.collectionTreeGrid.setWidth(collPopupWindowQD.getWidth() - 25);
            }
        });

        collTreeContainerQD.setWidth(collPopupWindowQD.getWidthAsString());
        collectionWidgetQD.collectionTreeGrid.setHeight(collPopupWindowQD.getHeight() - 2 * H_Unit - 10);
        collectionWidgetQD.collectionTreeGrid.setWidth(collPopupWindowQD.getWidth() - 25);
        collTreeContainerQD.add(setColl1);
        collTreeContainerQD.setCellHorizontalAlignment(setColl1, HorizontalPanel.ALIGN_LEFT);
        collTreeContainerQD.add(closeColl1);
        collTreeContainerQD.setCellHorizontalAlignment(closeColl1, HorizontalPanel.ALIGN_CENTER);
        collTreeContainerQD.add(clearColl1);
        collTreeContainerQD.setCellHorizontalAlignment(clearColl1, HorizontalPanel.ALIGN_RIGHT);

        setbuttonstyle(setColl1);
        setbuttonstyle(clearColl1);
        setbuttonstyle(closeColl1);
    }

    public void setbuttonstyle(Button b) {
        b.setStyleName("x-btn-click");
        b.setPixelSize(b.getText().length() * GuiConstant.CHARACTER_WIDTH, H_Unit);
    }

    public void getcontentlistMyCat() {
        textAlignerWidget.msg.setStyleName("gwt-TA-warning");
        if (isExternalForSearchQuery) {
            QUERY = Utility.replaceAll2(searchQuery).trim();
            textAlignerWidget.search.setText(QUERY);
        } else {
            QUERY = Utility.replaceAll2(textAlignerWidget.search.getText()).trim();
        }
        if (textAlignerWidget.search.getText().equals("AUTO_ON")) {
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_15);
            GuiConstant.AUTO_ON = true;
            clearAll();
        } else if (textAlignerWidget.search.getText().equals("ORIGINAL_ON")) {
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_16);
            GuiConstant.ORIGINAL_ON = true;
            clearAll();
        } else if (textAlignerWidget.search.getText().equals("PATH_ON")) {
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_17);
            GuiConstant.PATH_ON = true;
            clearAll();
        } else if (textAlignerWidget.search.getText().equals("SAVE_ON")) {
            GuiConstant.SAVE_ON = true;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_18);
        } else if (textAlignerWidget.search.getText().equals("FNR_ON")) {
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_19);
            GuiConstant.FILE_NAME_RIGHT = true;
            clearAll();
        } else if (textAlignerWidget.search.getText().equals("AUTO_OFF")) {
            GuiConstant.AUTO_ON = false;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_20);
        } else if (textAlignerWidget.search.getText().equals("ORIGINAL_OFF")) {
            GuiConstant.ORIGINAL_ON = false;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_21);
        } else if (textAlignerWidget.search.getText().equals("PATH_OFF")) {
            GuiConstant.PATH_ON = false;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_22);
        } else if (textAlignerWidget.search.getText().equals("FNR_OFF")) {
            GuiConstant.FILE_NAME_RIGHT = false;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_62);
        } else if (textAlignerWidget.search.getText().equals("SAVE_OFF")) {
            GuiConstant.SAVE_ON = false;
            clearAll();
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_23);
        } else {
            textAlignerWidget.msg.setText(GuiMessageConst.MSG_24);
            tS.reset();
            tS.DrawEffects();
            if (GuiConstant.REMOVE_AGLUTINATED_SPACE) {
                QUERY = Utility.addSpace(QUERY);
            }
            if ((QUERY.length() == 0) || (QUERY.startsWith("/"))) {
                GuiConstant.EXACT_FLG = false;
                GuiConstant.EXACT_NBR_FLG = false;
                GuiConstant.EXACT_CLOSE = false;
                GuiConstant.MULTI_WILD_CARD_FLG = false;
                String Query = Utility.browseRequest(QUERY);
                tS.words = null;
                words = null;
                textAlignerWidget.search.setText("/" + Query);
                textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_25 + Query);
                textAlignerWidget.DrawDocumentBrowseList(Query, tS, collectionWidgetTA.Selection);
            } else {
                if ((QUERY.contains("*"))) {
                    GuiConstant.EXACT_FLG = false;
                    GuiConstant.EXACT_NBR_FLG = false;
                    GuiConstant.EXACT_CLOSE = false;
                    GuiConstant.MULTI_WILD_CARD_FLG = false;
                    rpcM.filterWildCardQuery(QUERY, new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(GuiMessageConst.MSG_26);
                        }

                        @Override
                        public void onSuccess(String result) {
                            QUERY = result;
                            String wildCard = Utility.processWildCard(QUERY.toLowerCase());
                            rpcM.getExpandTerms(wildCard, new AsyncCallback<String[]>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert(GuiMessageConst.MSG_26);
                                }

                                @Override
                                public void onSuccess(String[] result) {
                                    words = null;
                                    String Query = Utility.wildCharQueryParser(result, textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                                    words = Utility.getWildCardQueryWords(result, stopWords);
                                    tS.words = words;
                                    tS.queryLength = QUERY.length() + 20;
                                    textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query);
                                    textAlignerWidget.DrawDocumentList(Query, tS, collectionWidgetTA.Selection);
                                }
                            });
                        }
                    });
                } else {
                    words = null;
                    if ((QUERY.startsWith("\"")) && ((QUERY.contains("\" CLOSE \"")) || (QUERY.contains("\" close \"")))) {
                        GuiConstant.EXACT_CLOSE = true;
                        GuiConstant.MULTI_WILD_CARD_FLG = false;
                        words = Utility.getexactClose(QUERY);
                        String Query1 = Utility.ExactCloseQueryBuilder(words.get(0), textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                        String Query2 = Utility.ExactCloseQueryBuilder(words.get(1), textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                        tS.queryLength = QUERY.length() - 9;
                        tS.words = words;
                        //                        Window.alert(tS.words.get(0) + " " + tS.words.get(1));
                        textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query1 + " CLOSE " + Query2);
                        textAlignerWidget.DrawDocumentList(Query1 + "---CLOSE---" + Query2, tS, collectionWidgetTA.Selection);
                    } else {
                        if (QUERY.startsWith("\"")) {
                            GuiConstant.EXACT_CLOSE = false;
                            GuiConstant.EXACT_FLG = true;
                            GuiConstant.MULTI_WILD_CARD_FLG = false;
                            words = Utility.getexactWords(QUERY);
                            String Query = Utility.queryParser(QUERY, textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                            tS.queryLength = QUERY.length();
                            tS.words = words;
                            textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query);
                            textAlignerWidget.DrawDocumentList(Query, tS, collectionWidgetTA.Selection);
                            //                        GuiConstant.EXACT_NBR_FLG = false;
                        } else if (QUERY.startsWith("#\"")) {
                            GuiConstant.EXACT_FLG = true;
                            GuiConstant.EXACT_CLOSE = false;
                            GuiConstant.MULTI_WILD_CARD_FLG = false;
                            words = Utility.getexactWords(QUERY);
                            String Query = Utility.queryParser(QUERY, textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                            tS.queryLength = QUERY.length();
                            tS.words = words;
                            textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query);
                            textAlignerWidget.DrawDocumentList(Query, tS, collectionWidgetTA.Selection);
                            //                        GuiConstant.EXACT_NBR_FLG = true;
                        } else {
                            GuiConstant.EXACT_FLG = false;
                            GuiConstant.EXACT_NBR_FLG = false;
                            GuiConstant.EXACT_CLOSE = false;
                            GuiConstant.MULTI_WILD_CARD_FLG = false;
                            if (QUERY.contains("QL(")) {
                                String Query = Utility.queryParser(QUERY, textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                                words = Utility.getQueryWords(QUERY + " ", stopWords);
                                tS.queryLength = QUERY.length();
                                tS.words = words;
                                textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query);
                                textAlignerWidget.DrawDocumentList(Query, tS, collectionWidgetTA.Selection);
                            } else {
                                rpcM.filterQuery(QUERY, new AsyncCallback<String>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Window.alert(GuiMessageConst.MSG_26);
                                    }

                                    @Override
                                    public void onSuccess(String result) {
                                        QUERY = result;
                                        words = null;
                                        String Query = Utility.queryParser(QUERY, textAlignerWidget.langS.getItemText(textAlignerWidget.langS.getSelectedIndex()), textAlignerWidget.langT.getItemText(textAlignerWidget.langT.getSelectedIndex()), stopWords, collectionWidgetTA.Selection);
                                        words = Utility.getQueryWords(QUERY + " ", stopWords);
                                        tS.words = words;
                                        tS.queryLength = QUERY.length();
                                        textAlignerWidget.GoSrch.setToolTip(GuiMessageConst.MSG_27 + Query);
                                        textAlignerWidget.DrawDocumentList(Query, tS, collectionWidgetTA.Selection);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private void clearAll() {
        textAlignerWidget.search.setText("");
        textAlignerWidget.search.setFocus(true);
        textAlignerWidget.staticTreeWrapper.clear();
        tS.DrawEffects();
        textAlignerWidget.GoSrch.enable();
        textAlignerWidget.docListContainer.setHeading(GuiMessageConst.MSG_59);
    }

    public void setMyCatWidget() {
        initCookies();
        Document.get().setTitle(GuiConstant.TEXT_ALIGNER_LBL);
        mainWidget.clear();
        textAlignerWidget = new ResearchWidget();
        mainWidget.add(textAlignerWidget);
        textAlignerWidget.draWidget();
        tS = new BitextWidget(textAlignerWidget.msg);
        textAlignerWidget.resultsPanel.add(tS);
        textAlignerWidget.search.setFocus(true);
        setLanguagesTA();
        textAlignerWidget.adaptSize();
        textAlignerWidget.coll.removeAllListeners();
        textAlignerWidget.coll.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                collectionWidgetTA.collectionTreeGrid.setHeight(collPopupWindowTA.getHeight() - 2 * H_Unit - 10);
                collectionWidgetTA.collectionTreeGrid.setWidth(collPopupWindowTA.getWidth() - 25);
                collPopupWindowTA.addItem(collectionWidgetTA.collectionTreeGrid);
                collPopupWindowTA.show();
                collectionWidgetTA.setCurrentSelection();
            }
        });

        setColl.removeAllListeners();
        setColl.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collectionWidgetTA.OrderSelection();
                collectionWidgetTA.setSelection();
                if (!collectionWidgetTA.Selection.isEmpty()) {
                    textAlignerWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_ON);
                    setbuttonstyle(textAlignerWidget.coll);
//                    String Selected = collectionWidgetTA.Selection.toString();
//                    Window.alert(Selected);
                    collPopupWindowTA.hide();
                } else {
                    textAlignerWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_OFF);
                    textAlignerWidget.coll.setWidth(GuiMessageConst.WIDGET_BTN_COLL_OFF.length() * GuiConstant.CHARACTER_WIDTH);
                    Window.alert(GuiMessageConst.MSG_28);
                }
            }
        });

        clearColl.removeAllListeners();
        clearColl.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collectionWidgetTA.clearSelection();
                textAlignerWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_OFF);
                textAlignerWidget.coll.setWidth(GuiMessageConst.WIDGET_BTN_COLL_OFF.length() * GuiConstant.CHARACTER_WIDTH);
                collectionWidgetTA.Selection.clear();
                collectionWidgetTA.selectedRec.clear();
                collectionWidgetTA.selected.clear();
                collectionWidgetTA.collectionTreeGrid.deselectAllRecords();
            }
        });

        closeColl.removeAllListeners();
        closeColl.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collPopupWindowTA.hide();
            }
        });

        textAlignerWidget.GoSrch.removeAllListeners();
        textAlignerWidget.GoSrch.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                isExternalForSearchQuery = false;
                searchQuery = "";
                textAlignerWidget.docListContainer.setHeading(GuiMessageConst.MSG_41 + "0" + GuiMessageConst.MSG_52);
                textAlignerWidget.GoSrch.disable();
                getcontentlistMyCat();
            }
        });

        textAlignerWidget.resize.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                resizeAll();
                resizeAll();
                if (!QUERY.isEmpty()) {
                    textAlignerWidget.reselectDocument(tS, QUERY);
                }
            }
        });

        textAlignerWidget.search.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                textAlignerWidget.docListContainer.setHeading(GuiMessageConst.MSG_41 + "0" + GuiMessageConst.MSG_52);
                textAlignerWidget.GoSrch.disable();
                QUERY = textAlignerWidget.search.getText();
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    isExternalForSearchQuery = false;
                    searchQuery = "";
                    getcontentlistMyCat();
                } else {
                    textAlignerWidget.GoSrch.enable();
                }
            }
        });
        textAlignerWidget.myQuote.removeAllListeners();
        textAlignerWidget.myQuote.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                setMyQuoteWidget(languages);
            }
        });
    }

    public void setMyQuoteWidget(final String[] languages) {
        initCookies();
        Document.get().setTitle(GuiConstant.QUOTE_DETECTOR_LBL);
        fixGwtNav();
        quoteDetectorWidget = new QuoteWidget();
        mainWidget.clear();
        mainWidget.add(quoteDetectorWidget);
        setLanguagesQD();
        quoteDetectorWidget.draWidget();
        quoteDetectorWidget.coll.removeAllListeners();
        quoteDetectorWidget.coll.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                collectionWidgetQD.collectionTreeGrid.setHeight(collPopupWindowQD.getHeight() - 2 * H_Unit - 10);
                collectionWidgetQD.collectionTreeGrid.setWidth(collPopupWindowQD.getWidth() - 25);
                collPopupWindowQD.addItem(collectionWidgetQD.collectionTreeGrid);
                collPopupWindowQD.show();
                collectionWidgetQD.setCurrentSelection();
            }
        });

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
//                Window.alert("History item :" + event.getValue());
                quoteDetectorWidget.getRefHitContent(Integer.parseInt(event.getValue()));
            }
        });

        setColl1.removeAllListeners();
        setColl1.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collectionWidgetQD.OrderSelection();
                collectionWidgetQD.setSelection();
                if (!collectionWidgetQD.Selection.isEmpty()) {
                    quoteDetectorWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_ON);
                    setbuttonstyle(quoteDetectorWidget.coll);
                    collPopupWindowQD.hide();
                } else {
                    quoteDetectorWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_OFF);
                    quoteDetectorWidget.coll.setWidth(GuiMessageConst.WIDGET_BTN_COLL_OFF.length() * GuiConstant.CHARACTER_WIDTH);
                    Window.alert(GuiMessageConst.MSG_28);
                }
            }
        });

        clearColl1.removeAllListeners();
        clearColl1.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collectionWidgetQD.clearSelection();
                quoteDetectorWidget.coll.setText(GuiMessageConst.WIDGET_BTN_COLL_OFF);
                quoteDetectorWidget.coll.setWidth(GuiMessageConst.WIDGET_BTN_COLL_OFF.length() * GuiConstant.CHARACTER_WIDTH);
                collectionWidgetQD.Selection.clear();
                collectionWidgetQD.selectedRec.clear();
                collectionWidgetQD.selected.clear();
                collectionWidgetQD.collectionTreeGrid.deselectAllRecords();
            }
        });

        closeColl1.removeAllListeners();
        closeColl1.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collPopupWindowQD.hide();
            }
        });

        quoteDetectorWidget.GoSrch.removeAllListeners();
        quoteDetectorWidget.GoSrch.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                quoteDetectorWidget.GoSrch.disable();
                getcontentlistMyQuote();
            }
        });

        quoteDetectorWidget.TextAligner.removeAllListeners();
        quoteDetectorWidget.TextAligner.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                setMyCatWidget();
            }
        });
    }

    public void getcontentlistMyQuote() {
        quoteDetectorWidget.refArea.setHtml("");
        quoteDetectorWidget.refIndic.setText("/");
        quoteDetectorWidget.drawReferences(collectionWidgetQD.Selection);
    }

    public void getContentListMyQuoteByFileName() {
        quoteDetectorWidget.refArea.setHtml("");
        quoteDetectorWidget.refIndic.setText("/");
        String link = fileContentURLBase + fileName.replace("¦", "$$$$$$").replace("+", "|||");
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, link);
        try {
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        quoteDetectorWidget.drawReferencesCall(collectionWidgetQD.Selection, fileName, response.getText());
                    } else if (response.getStatusCode() == 404) {
                        Window.alert("Service not available.");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert("Service not available.");
                }
            });
        } catch (RequestException re) {
            GWT.log("Error", re);
        }
    }

    public void resizeAll() {
        IMeasures.calculateMeasures(Window.getClientHeight(), Window.getClientWidth());
        tS.updateSize();
        textAlignerWidget.updateSize();
        textAlignerWidget.adaptSize();
        IMeasures.saveMeasuresInCookies();
    }

    // initialise all cookies in the client navigator if not existing
    private void initCookies() {
        MyCatCookies.initCookie(CookiesNamespace.MyCatlangS, "EN");
        MyCatCookies.initCookie(CookiesNamespace.MyCatlangT, "FR");
        MyCatCookies.initCookie(CookiesNamespace.SortBy, "NAME");
        MyCatCookies.initCookie(CookiesNamespace.MyQuotelangS, "EN");
        MyCatCookies.initCookie(CookiesNamespace.MyQuotelangT, "FR");
        MyCatCookies.initCookie(CookiesNamespace.MyQuoteMinLength, "3");
        MyCatCookies.initCookie(CookiesNamespace.InterfaceLanguage, "en");
        MyCatCookies.initCookie(CookiesNamespace.TA_TEXTAREA_WIDTH, "" + GuiConstant.TA_TEXTAREA_WIDTH);
        MyCatCookies.initCookie(CookiesNamespace.TA_TEXTAREA_HEIGHT, "" + GuiConstant.TA_TEXTAREA_HEIGHT);
        MyCatCookies.initCookie(CookiesNamespace.QD_TEXTAREA_HEIGHT, "" + GuiConstant.QD_TEXTAREA_HEIGHT);
        MyCatCookies.initCookie(CookiesNamespace.QD_HTMLAREA_HEIGHT, "" + GuiConstant.QD_HTMLAREA_HEIGHT);
        MyCatCookies.initCookie(CookiesNamespace.DOC_LIST_WIDTH, "" + GuiConstant.DOC_LIST_WIDTH);
        MyCatCookies.initCookie(CookiesNamespace.DOC_LIST_HEIGHT, "" + GuiConstant.DOC_LIST_HEIGHT);
        MyCatCookies.initCookie(CookiesNamespace.QD_DOC_LIST_HEIGHT, "" + GuiConstant.QD_DOC_LIST_HEIGHT);
    }

    public static void download(String fileDownloadURL, final Label msg) {
        Frame fileDownloadFrame = new Frame(fileDownloadURL);
        fileDownloadFrame.setSize("0px", "0px");
        fileDownloadFrame.setVisible(false);
        RootPanel panel = RootPanel.get("__gwt_downloadFrame");
        while (panel.getWidgetCount() > 0) {
            panel.remove(0);
        }
        panel.add(fileDownloadFrame);
        msg.setText(GuiMessageConst.MSG_32);
    }

    private void InitProperties(GwtProp CONST) {
        GuiConstant.ORIGINAL_ON = CONST.ORIGINAL_ON;
        GuiConstant.DEBUG_ON = CONST.DEBUG_ON;
        GuiConstant.PATH_ON = CONST.PATH_ON;
        GuiConstant.AUTO_ON = CONST.AUTO_ON;
        GuiConstant.SAVE_ON = CONST.SAVE_ON;
        GuiConstant.MAXIMIZE_ON = CONST.MAXIMIZE_ON;
        GuiConstant.TA_HILITE_OVER_CR = CONST.TA_HILITE_OVER_CR;
        GuiConstant.CHOOSE_GUI_LANG = CONST.CHOOSE_GUI_LANG;
        GuiConstant.REMOVE_AGLUTINATED_SPACE = CONST.REMOVE_AGLUTINATED_SPACE;
        GuiConstant.CHOOSE_GUI_LANG_LIST = CONST.CHOOSE_GUI_LANG_LIST;
        GuiConstant.AGLUTINATED_LANG_LIST = CONST.AGLUTINATED_LANG_LIST;
        GuiConstant.TOKENIZE_LIST = CONST.TOKENIZE_LIST;
        GuiConstant.TA_LINE_HEIGHT = CONST.TA_LINE_HEIGHT;
        GuiConstant.TA_TEXTAREA_WIDTH = CONST.TA_TEXTAREA_WIDTH;
        GuiConstant.TA_TEXTAREA_HEIGHT = CONST.TA_TEXTAREA_HEIGHT;
        GuiConstant.QD_TEXTAREA_HEIGHT = CONST.QD_TEXTAREA_HEIGHT;
        GuiConstant.QD_HTMLAREA_HEIGHT = CONST.QD_HTMLAREA_HEIGHT;
        GuiConstant.DOC_LIST_WIDTH = CONST.DOC_LIST_WIDTH;
        GuiConstant.DOC_LIST_HEIGHT = CONST.DOC_LIST_HEIGHT;
        GuiConstant.QD_DOC_LIST_HEIGHT = CONST.QD_DOC_LIST_HEIGHT;
        GuiConstant.TA_TEXTAREA_WIDTH_MIN = CONST.TA_TEXTAREA_WIDTH_MIN;
        GuiConstant.TA_TEXTAREA_HEIGHT_MIN = CONST.TA_TEXTAREA_HEIGHT_MIN;
        GuiConstant.QD_TEXTAREA_HEIGHT_MIN = CONST.QD_TEXTAREA_HEIGHT_MIN;
        GuiConstant.QD_HTMLAREA_HEIGHT_MIN = CONST.QD_HTMLAREA_HEIGHT_MIN;
        GuiConstant.DOC_LIST_WIDTH_MIN = CONST.DOC_LIST_WIDTH_MIN;
        GuiConstant.DOC_LIST_HEIGHT_MIN = CONST.DOC_LIST_HEIGHT_MIN;
        GuiConstant.QD_DOC_LIST_HEIGHT_MIN = CONST.QD_DOC_LIST_HEIGHT_MIN;
        GuiConstant.TA_TEXTAREA_WIDTH_MAX = CONST.TA_TEXTAREA_WIDTH_MAX;
        GuiConstant.TA_TEXTAREA_HEIGHT_MAX = CONST.TA_TEXTAREA_HEIGHT_MAX;
        GuiConstant.QD_TEXTAREA_HEIGHT_MAX = CONST.QD_TEXTAREA_HEIGHT_MAX;
        GuiConstant.QD_HTMLAREA_HEIGHT_MAX = CONST.QD_HTMLAREA_HEIGHT_MAX;
        GuiConstant.DOC_LIST_WIDTH_MAX = CONST.DOC_LIST_WIDTH_MAX;
        GuiConstant.DOC_LIST_HEIGHT_MAX = CONST.DOC_LIST_HEIGHT_MAX;
        GuiConstant.QD_DOC_LIST_HEIGHT_MAX = CONST.QD_DOC_LIST_HEIGHT_MAX;
        GuiConstant.TA_OVERHEAD_MAX_H = CONST.TA_OVERHEAD_MAX_H;
        GuiConstant.TA_OVERHEAD_MAX_L = CONST.TA_OVERHEAD_MAX_L;
        GuiConstant.QD_OVERHEAD_MAX_H = CONST.QD_OVERHEAD_MAX_H;
        GuiConstant.TA_OVERHEAD_H = CONST.TA_OVERHEAD_H;
        GuiConstant.TA_CHAR_WIDTH = CONST.TA_CHAR_WIDTH;
        GuiConstant.PER_DOC_LIST_W = CONST.PER_DOC_LIST_W;
        GuiConstant.PER_QD_HTMLAREA_H = CONST.PER_QD_HTMLAREA_H;
        GuiConstant.EXP_DAYS = CONST.EXP_DAYS;
        GuiConstant.MAX_RESPONSE = CONST.MAX_RESPONSE;
        GuiConstant.MAX_BROWSE = CONST.MAX_BROWSE;
        GuiConstant.MAX_SEARCH_SIZE = CONST.MAX_SEARCH_SIZE;
        GuiConstant.JOBS_ITEMS = CONST.JOBS_ITEMS;
        GuiConstant.MIN_OCCU = CONST.MIN_OCCU;
        GuiConstant.MAX_OCCU = CONST.MAX_OCCU;
        GuiConstant.TEXT_ALIGNER_LBL = CONST.TEXT_ALIGNER_LBL;
        GuiConstant.QUOTE_DETECTOR_LBL = CONST.QUOTE_DETECTOR_LBL;
        GuiConstant.FILE_NAME_RIGHT = CONST.FILE_NAME_RIGHT;
        GuiConstant.ONLY_ON_FILE_NAME = CONST.ONLY_ON_FILE_NAME;
        GuiConstant.BITEXT_ONLY = CONST.BITEXT_ONLY;
        GuiConstant.QD_FILE_EXT = CONST.QD_FILE_EXT;
        GuiConstant.QD_GENERAL_EXT = CONST.QD_GENERAL_EXT;
        GuiConstant.QD_HELP_URL = CONST.QD_HELP_URL;
        GuiConstant.TA_HELP_URL = CONST.TA_HELP_URL;
        GuiConstant.W_OPEN_FEATURES = CONST.W_OPEN_FEATURES;
        GuiConstant.OLANTO_URL = CONST.OLANTO_URL;
        GuiConstant.LOGO_PATH = CONST.LOGO_PATH;
        GuiConstant.LOGO_URL = CONST.LOGO_URL;
        GuiConstant.CHARACTER_WIDTH = CONST.CHARACTER_WIDTH;
        GuiConstant.TA_DL_SORTBY = CONST.TA_DL_SORTBY;
        GuiConstant.FEEDBACK_MAIL = CONST.FEEDBACK_MAIL;
        GuiConstant.REF_FACTOR = CONST.REF_FACTOR;
        GuiConstant.REF_MIN_LN = CONST.REF_MIN_LN;
        GuiConstant.PP_H_MIN = CONST.PP_H_MIN;
        GuiConstant.PP_H_MAX = CONST.PP_H_MAX;
        GuiConstant.TA_NEAR_AVG_TERM_CHAR = CONST.TA_NEAR_AVG_TERM_CHAR;
        GuiConstant.NEAR_DISTANCE = CONST.NEAR_DISTANCE;
        GuiConstant.entryToReplace = CONST.entryToReplace;
        GuiConstant.SHOW_GUI_FAST = CONST.SHOW_GUI_FAST;
        GuiConstant.CHOOSE_GUI_FAST_DEFAULT = CONST.CHOOSE_GUI_FAST_DEFAULT;
        GuiConstant.SHOW_REMOVE_FIRST = CONST.SHOW_REMOVE_FIRST;
        GuiConstant.REMOVE_FIRST_DEFAULT = CONST.REMOVE_FIRST_DEFAULT;
        GuiConstant.REMOVE_TA_INTERFACE = CONST.REMOVE_TA_INTERFACE;
        /**
         * client interface parameters
         * **********************************************************************************
         */
        GuiMessageConst.BTN_RESIZE = CONST.BTN_RESIZE;
        GuiMessageConst.TA_BTN_SRCH = CONST.TA_BTN_SRCH;
        GuiMessageConst.TA_BTN_NXT = CONST.TA_BTN_NXT;
        GuiMessageConst.TA_BTN_PVS = CONST.TA_BTN_PVS;
        GuiMessageConst.TA_BTN_OGN = CONST.TA_BTN_OGN;
        GuiMessageConst.TA_BTN_ALGN = CONST.TA_BTN_ALGN;
        GuiMessageConst.TA_BTN_SAVE = CONST.TA_BTN_SAVE;
        GuiMessageConst.TA_BTN_SEARCH = CONST.TA_BTN_SEARCH;
        GuiMessageConst.TA_BTN_CCL = CONST.TA_BTN_CCL;
        GuiMessageConst.WIDGET_BTN_SUBMIT = CONST.WIDGET_BTN_SUBMIT;
        GuiMessageConst.WIDGET_BTN_COLL_ON = CONST.WIDGET_BTN_COLL_ON;
        GuiMessageConst.WIDGET_BTN_COLL_OFF = CONST.WIDGET_BTN_COLL_OFF;
        GuiMessageConst.WIDGET_BTN_QD = CONST.WIDGET_BTN_QD;
        GuiMessageConst.WIDGET_BTN_HELP = CONST.WIDGET_BTN_HELP;
        GuiMessageConst.WIDGET_BTN_TA = CONST.WIDGET_BTN_TA;
        GuiMessageConst.WIDGET_BTN_QD_NXT = CONST.WIDGET_BTN_QD_NXT;
        GuiMessageConst.WIDGET_BTN_QD_PVS = CONST.WIDGET_BTN_QD_PVS;
        GuiMessageConst.WIDGET_LBL_QD_LN = CONST.WIDGET_LBL_QD_LN;
        GuiMessageConst.WIDGET_BTN_QD_SAVE = CONST.WIDGET_BTN_QD_SAVE;
        GuiMessageConst.WIDGET_BTN_TA_SAVE = CONST.WIDGET_BTN_TA_SAVE;
        GuiMessageConst.WIDGET_LIST_TA_SBY = CONST.WIDGET_LIST_TA_SBY;
        GuiMessageConst.WIDGET_COLL_WND = CONST.WIDGET_COLL_WND;
        GuiMessageConst.WIDGET_COLL_SET = CONST.WIDGET_COLL_SET;
        GuiMessageConst.WIDGET_COLL_CLOSE = CONST.WIDGET_COLL_CLOSE;
        GuiMessageConst.WIDGET_COLL_CLEAR = CONST.WIDGET_COLL_CLEAR;
        GuiMessageConst.QD_CHECKBOX_FAST = CONST.QD_CHECKBOX_FAST;
        GuiMessageConst.QD_CHECKBOX_REMOVE_FIRST = CONST.QD_CHECKBOX_REMOVE_FIRST;
        /**
         * **********************************MESSAGES****************************************
         */
        GuiMessageConst.MSG_1 = CONST.MSG_1;
        GuiMessageConst.MSG_2 = CONST.MSG_2;
        GuiMessageConst.MSG_3 = CONST.MSG_3;
        GuiMessageConst.MSG_4 = CONST.MSG_4;
        GuiMessageConst.MSG_5 = CONST.MSG_5;
        GuiMessageConst.MSG_6 = CONST.MSG_6;
        GuiMessageConst.MSG_7 = CONST.MSG_7;
        GuiMessageConst.MSG_8 = CONST.MSG_8;
        GuiMessageConst.MSG_9 = CONST.MSG_9;
        GuiMessageConst.MSG_10 = CONST.MSG_10;
        GuiMessageConst.MSG_11 = CONST.MSG_11;
        GuiMessageConst.MSG_12 = CONST.MSG_12;
        GuiMessageConst.MSG_13 = CONST.MSG_13;
        GuiMessageConst.MSG_14 = CONST.MSG_14;
        GuiMessageConst.MSG_15 = CONST.MSG_15;
        GuiMessageConst.MSG_16 = CONST.MSG_16;
        GuiMessageConst.MSG_17 = CONST.MSG_17;
        GuiMessageConst.MSG_18 = CONST.MSG_18;
        GuiMessageConst.MSG_19 = CONST.MSG_19;
        GuiMessageConst.MSG_20 = CONST.MSG_20;
        GuiMessageConst.MSG_21 = CONST.MSG_21;
        GuiMessageConst.MSG_22 = CONST.MSG_22;
        GuiMessageConst.MSG_23 = CONST.MSG_23;
        GuiMessageConst.MSG_24 = CONST.MSG_24;
        GuiMessageConst.MSG_25 = CONST.MSG_25;
        GuiMessageConst.MSG_26 = CONST.MSG_26;
        GuiMessageConst.MSG_27 = CONST.MSG_27;
        GuiMessageConst.MSG_28 = CONST.MSG_28;
        GuiMessageConst.MSG_29 = CONST.MSG_29;
        GuiMessageConst.MSG_30 = CONST.MSG_30;
        GuiMessageConst.MSG_31 = CONST.MSG_31;
        GuiMessageConst.MSG_32 = CONST.MSG_32;
        GuiMessageConst.MSG_33 = CONST.MSG_33;
        GuiMessageConst.MSG_34 = CONST.MSG_34;
        GuiMessageConst.MSG_35 = CONST.MSG_35;
        GuiMessageConst.MSG_36 = CONST.MSG_36;
        GuiMessageConst.MSG_37 = CONST.MSG_37;
        GuiMessageConst.MSG_38 = CONST.MSG_38;
        GuiMessageConst.MSG_39 = CONST.MSG_39;
        GuiMessageConst.MSG_40 = CONST.MSG_40;
        GuiMessageConst.MSG_41 = CONST.MSG_41;
        GuiMessageConst.MSG_42 = CONST.MSG_42;
        GuiMessageConst.MSG_43 = CONST.MSG_43;
        GuiMessageConst.MSG_44 = CONST.MSG_44;
        GuiMessageConst.MSG_45 = CONST.MSG_45;
        GuiMessageConst.MSG_46 = CONST.MSG_46;
        GuiMessageConst.MSG_47 = CONST.MSG_47;
        GuiMessageConst.MSG_48 = CONST.MSG_48;
        GuiMessageConst.MSG_49 = CONST.MSG_49;
        GuiMessageConst.MSG_50 = CONST.MSG_50;
        GuiMessageConst.MSG_51 = CONST.MSG_51;
        GuiMessageConst.MSG_52 = CONST.MSG_52;
        GuiMessageConst.MSG_53 = CONST.MSG_53;
        GuiMessageConst.MSG_54 = CONST.MSG_54;
        GuiMessageConst.MSG_55 = CONST.MSG_55;
        GuiMessageConst.MSG_56 = CONST.MSG_56;
        GuiMessageConst.MSG_57 = CONST.MSG_57;
        GuiMessageConst.MSG_58 = CONST.MSG_58;
        GuiMessageConst.MSG_59 = CONST.MSG_59;
        GuiMessageConst.MSG_60 = CONST.MSG_60;
        GuiMessageConst.MSG_61 = CONST.MSG_61;
        GuiMessageConst.MSG_62 = CONST.MSG_62;
        GuiMessageConst.MSG_63 = CONST.MSG_63;
        GuiMessageConst.MSG_64 = CONST.MSG_64;
        GuiMessageConst.MSG_65 = CONST.MSG_65;
        GuiMessageConst.MSG_66 = CONST.MSG_66;
        GuiMessageConst.MSG_67 = CONST.MSG_67;
        GuiMessageConst.MSG_68 = CONST.MSG_68;
        GuiMessageConst.MSG_69 = CONST.MSG_69;
        GuiMessageConst.MSG_70 = CONST.MSG_70;
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
