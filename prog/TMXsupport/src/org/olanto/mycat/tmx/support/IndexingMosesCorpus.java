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
package org.olanto.mycat.tmx.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.olanto.idxvli.*;
import org.olanto.util.Timer;
import static org.olanto.util.Messages.*;

/**
 * index le corpus (sans mode serveur)
 */
public class IndexingMosesCorpus {      // is an application, not an applet !

    static IdxStructure id;
    static Timer t1 = new Timer("global time");

    public static void indexThis(IdxStructure id, String name, String fileso, String fileta, String langso, String langta, int limit, String txt_encoding) {
        System.out.println("------------- source: " + name + ", " + langso + ", " + langta);
        System.out.println("------------- index corpus: " + fileso + ", " + fileta);
        int totread = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileso), txt_encoding);
            BufferedReader so = new BufferedReader(isrso);
            InputStreamReader isrta = new InputStreamReader(new FileInputStream(fileta), txt_encoding);
            BufferedReader ta = new BufferedReader(isrta);
            String wso = so.readLine();
            String wta = ta.readLine();
            while (wso != null && totread < limit) {
                totread++;
                if (totread%10000==0)System.out.println(totread);
                String docnameso = name + "_" + langso + "_" + totread;
                String docnameta = name + "_" + langta + "_" + totread;
                id.indexThisContent(docnameso, wso);
                id.indexThisContent(docnameta, wta);
                wso = so.readLine();
                wta = ta.readLine();
            }
            so.close();
            ta.close();
            System.out.println("   read entries: " + totread);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
