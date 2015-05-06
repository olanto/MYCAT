/**
 * ********
 * Copyright © 2010-2014 Olanto Foundation Geneva
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
package org.olanto.mycat.ngram.generic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.olanto.idxvli.*;
import org.olanto.util.Timer;

/**
 * index le corpus (sans mode serveur)
 */
public class IndexingReader {      // is an application, not an applet !

    static IdxStructure id;
    static Timer t1 = new Timer("global time");

    public static void indexThis(IdxStructure id, String fileso, int limit, String txt_encoding) {
        System.out.println("------------- index corpus: " + fileso);
        int totread = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileso), txt_encoding);
            BufferedReader so = new BufferedReader(isrso);
            String wso = so.readLine();
            String docname = "";
            StringBuilder text = new StringBuilder();
            while (wso != null && totread < limit) {
                if (wso.startsWith("#####")) {
                    if (totread != 0) {
                       id.indexThisContent(docname, text.toString());
                    }
                    docname=wso.replace("#####", "");
                    totread++;
                    text = new StringBuilder();
                } else {
                    text.append(wso+"\n");
                }
                wso = so.readLine();
            }
            id.indexThisContent(docname, text.toString());
            so.close();
            System.out.println("   read entries: " + totread);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
