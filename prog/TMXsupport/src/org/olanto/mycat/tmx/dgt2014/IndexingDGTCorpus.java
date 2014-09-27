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
package org.olanto.mycat.tmx.dgt2014;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.olanto.idxvli.*;
import org.olanto.util.Timer;

/**
 * index le corpus (sans mode serveur)
 */
public class IndexingDGTCorpus {      // is an application, not an applet !

    static IdxStructure id;
    static Timer t1 = new Timer("global time");

    public static void indexThis(IdxStructure id, String name, String fileso, int limit, String txt_encoding) {
        System.out.println("------------- index corpus: " + fileso );
        int totread = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileso), txt_encoding);
            BufferedReader so = new BufferedReader(isrso);
            String wso = so.readLine();
            while (wso != null && totread < limit) {
                String[] x24 = wso.split("\t");
                if (x24.length == 24) {
                    for (int i = 0; i < 24; i++) {
                        totread++;
                        String docnameso = "" + totread;
                        if (totread % 10000 == 0) {
                            System.out.println(totread);
                        }
                        id.indexThisContent(docnameso, x24[i]);
                    }
                } else System.out.println("  erronr X24 " + wso);

                wso = so.readLine();
            }
            so.close();
            System.out.println("   read entries: " + totread);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
