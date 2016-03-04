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
package org.olanto.mycat.tmx.generic;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.senseos.SenseOS;
import static org.olanto.idxvli.IdxConstant.*;
import org.olanto.mycat.tmx.common.LangMap;

/**
 *
 * @author jg
 */
public class extractCorpusFromMyPREP {

    static String sourceTMX, targetMFLF;
    static long totkeep, toterror;
    static OutputStreamWriter outmflf;
    static HashMap<String, String[]> TMX = new HashMap();
    static String[] emptyTMX = {".", ".", ".", ".", ".", ".", "."};
    static String fnpivot, fntarget;
    static int limit = Integer.MAX_VALUE;
    static String pivotLang;
    /*   
     */

    public static void main(String[] args) {
        try {
            String drive = "C:";
            sourceTMX = drive + "/MYPREP/SMT/";
            targetMFLF = drive + "/MYPREP/HOW2SAY/" + "corpus" + ".mflf";
            fnpivot = "corpus.so";
            fntarget = "corpus.ta";
            ConfigurationIndexingGetFromFile config = new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME("HOW2SAY") + "/config/IDX_fix.xml");
            config.InitPermanent();
            config.InitConfiguration();

            System.out.println("=============================");
            System.out.println(ROOT_CORPUS_LANG);

            LangMap.init(ROOT_CORPUS_LANG);
            emptyTMX = new String[LangMap.size()];
            for (int i = 0; i < LangMap.size(); i++) {
                emptyTMX[i] = ".";
            }
            pivotLang = LangMap.getlang(0);
            outmflf = new OutputStreamWriter(new FileOutputStream(targetMFLF), "UTF-8");

            Process();
            outmflf.close();
            System.out.println("Keep:" + TMX.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void Process() {
        for (int i = 1; i < LangMap.size(); i++) {
            InitPivotEntry(LangMap.getlang(i));
        }
        for (int i = 1; i < LangMap.size(); i++) {
            addEntry(LangMap.getlang(i));
        }


        dumpTMX();
    }

    static void InitPivotEntry(String TA) {
        String fileName = sourceTMX + pivotLang + TA + "/" + fnpivot;

        System.out.println("------------- entry dictionary: " + fileName);
        int totdictEntry = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            BufferedReader so = new BufferedReader(isrso);
            String wso = so.readLine();
            while (wso != null && totdictEntry < limit) {
                totdictEntry++;
                TMX.put(wso, emptyTMX.clone());
                wso = so.readLine();
            }
            so.close();
            System.out.println("   read entries: " + totdictEntry + ", keep entries: " + TMX.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addEntry(String TA) {
        int posTA = LangMap.getpos(TA);
        String fileNameSO = sourceTMX + pivotLang + TA + "/" + fnpivot;
        String fileNameTA = sourceTMX + pivotLang + TA + "/" + fntarget;
        System.out.println("------------- build sentence dictionary: " + fileNameSO + ", " + fileNameTA);
        int totdictEntry = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileNameSO), "UTF-8");
            BufferedReader so = new BufferedReader(isrso);
            InputStreamReader isrta = new InputStreamReader(new FileInputStream(fileNameTA), "UTF-8");
            BufferedReader ta = new BufferedReader(isrta);
            String wso = so.readLine();
            String wta = ta.readLine();
            while (wso != null && totdictEntry < limit) {
                totdictEntry++;
                String[] entry = TMX.get(wso);
                entry[posTA] = wta;
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

    public static void dumpTMX() {

        System.out.println("DUMP");
        for (String key : TMX.keySet()) {
            String[] entry = TMX.get(key);
//            System.out.println(key);
            try {
                outmflf.append(key);  // entry[i] is empty
                for (int i = 1; i < LangMap.size(); i++) {
                    outmflf.append("\t" + entry[i]);
                }
                outmflf.append("\n");
            } catch (IOException ex) {
                Logger.getLogger(extractCorpusFromMyPREP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
