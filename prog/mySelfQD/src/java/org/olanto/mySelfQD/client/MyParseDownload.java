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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author simple
 */
public class MyParseDownload {

    private static final String fileDownloadURLBase = GWT.getModuleBaseURL() + "/DownloadServlet?filename=";
    private static final AsyncCallback<String> fileGetCallBack = new AsyncCallback<String>() {

        @Override
        public void onFailure(Throwable caught) {
            Window.alert(GuiConstant.MSG_1);
        }

        @Override
        public void onSuccess(String result) {
            if (result != null) {
                String fileDownloadURL = fileDownloadURLBase + result.replace("¦", "$$$$$$").replace("+", "|||") + "&mode=file";
                MainEntryPoint.download(fileDownloadURL);
            } else {
                Window.alert(GuiConstant.MSG_1);
            }
        }
    };

    public static myParseServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(myParseService.class);
    }

    public MyParseDownload() {
    }

    public static void downloadFileFromServer(String FileName, String Content) {
        getService().createTempFile(FileName, Content, fileGetCallBack);
    }
}
