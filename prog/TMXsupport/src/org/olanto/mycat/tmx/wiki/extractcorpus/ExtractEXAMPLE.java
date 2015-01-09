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
package org.olanto.mycat.tmx.wiki.extractcorpus;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author jg
 */
public class ExtractEXAMPLE {

    static OutputStreamWriter outmflf;
    static int limit = Integer.MAX_VALUE;
    /*   
     */

    public static void main(String[] args) {
        try {
            //  procesADir("h:/PATDB/XML/docdb");
            String drive = "C:";
            String sourceTMX_EN = drive + "/CORPUS/WIKI/FREN/CORPUS/corpus.EN";
            String sourceTMX_FR = drive + "/CORPUS/WIKI/FREN/CORPUS/corpus.FR";
            String sourceTMX_MFLF = drive + "/CORPUS/WIKI/FREN/corpus.mflf";
            outmflf = new OutputStreamWriter(new FileOutputStream(sourceTMX_MFLF), "UTF-8");
            Process(sourceTMX_EN, sourceTMX_FR);
            outmflf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void Process(String lang1, String lang2) {
        int totdictEntry = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(lang1), "UTF-8");
            BufferedReader so = new BufferedReader(isrso);
            String wso = so.readLine();
            InputStreamReader isrta = new InputStreamReader(new FileInputStream(lang2), "UTF-8");
            BufferedReader ta = new BufferedReader(isrta);
            String wta = ta.readLine();
            while (wso != null && totdictEntry < limit) {
                totdictEntry++;
                outmflf.append(wso + "\t" + wta + "\n");
                wso = so.readLine();
                wta = ta.readLine();
            }
            so.close();
            ta.close();
            System.out.println("   read entries: " + totdictEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
