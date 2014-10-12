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
package org.olanto.mycat.tmx.multiun.extract;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jg
 */
public class extractMULTIUN {

    static String sourceTMX, targetMFLF;
    static long totkeep, toterror;
    static OutputStreamWriter outmflf;
    static HashMap<String, String[]> TMX = new HashMap();
    static String[] emptyTMX = {".", ".", ".", ".", ".", ".", "."};
    static int limit = Integer.MAX_VALUE;
    /*   
     */

    public static void main(String[] args) {
        try {
            //  procesADir("h:/PATDB/XML/docdb");
            String drive = "C:";
            sourceTMX = drive + "/CORPUS/MULTI-UN/UNIFY/MultiUN.";
            targetMFLF = "C:" + "/CORPUS/MULTI-UN/" + "MULTIUN" + ".mflf";
            outmflf = new OutputStreamWriter(new FileOutputStream(targetMFLF), "UTF-8");
            LangMap.init();
            Process();
            outmflf.close();
            System.out.println("Keep:" + TMX.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void Process() {

        InitPivotEntry("AR");
        InitPivotEntry("DE");
        InitPivotEntry("ES");
        InitPivotEntry("FR");
        InitPivotEntry("RU");
        InitPivotEntry("ZH");
        
       addEntry("AR");
        addEntry("DE");
        addEntry("ES");
        addEntry("FR");
        addEntry("RU");
        addEntry("ZH");
 
        dumpTMX();
    }

    static void InitPivotEntry(String TA) {
        String fileName = sourceTMX + "en-" + TA + ".en";

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
        int posTA=LangMap.getpos(TA);
         String fileNameSO = sourceTMX + "en-" + TA + ".en";
      String fileNameTA = sourceTMX + "en-" + TA + "."+ TA ;
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
                String[] entry=TMX.get(wso);
                entry[posTA]=wta;
                wso = so.readLine();
                wta = ta.readLine();
            }
            so.close();
            ta.close();
            System.out.println("   read entries: " + totdictEntry );
        } catch (Exception e) {
            e.printStackTrace();
        }
   
    }

    public static void dumpTMX() {

        System.out.println("DUMP");
        for (String key : TMX.keySet()) {
             String[] entry=TMX.get(key);
            try {
                outmflf.append(entry[0] + "\t" + entry[1]+ "\t" + key+ "\t" + entry[3]+ "\t" + entry[4]+ "\t" + entry[5]+ "\t" + entry[6]+"\n");
            } catch (IOException ex) {
                Logger.getLogger(extractMULTIUN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
