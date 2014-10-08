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
package org.olanto.mycat.tmx.dgt2014.extract;

import java.io.*;
import java.lang.reflect.Array;
import java.util.HashMap;

/**
 *
 * @author jg
 */
public class extractDGT2014 {

    static String sourceTMX, targetMFLF;
    static long totkeep, toterror;
    static OutputStreamWriter outmflf;
    static HashMap<String, Integer> lang = new HashMap();
    /*   
     */

    public static void main(String[] args) {
        try {
            //  procesADir("h:/PATDB/XML/docdb");
            String drive = "K:";
            sourceTMX = drive + "/CORPUS/DGT-TMX/Corpus2014/";
            targetMFLF = "C:" + "/CORPUS/DGT-TMX/" + "DGT2014" + ".mflf";
            outmflf = new OutputStreamWriter(new FileOutputStream(targetMFLF), "UTF-8");
            LangMap.init();
            procesADir(sourceTMX);
            outmflf.close();
            System.out.println("Keep:" + totkeep);
            System.out.println("Error:" + toterror);
            System.out.println("Final:-----------------------------------");
            dumpLang();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void procesADir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            System.out.println("path:"+f);
            String UPATH = path.toUpperCase();
            if (UPATH.endsWith(".TMX")) {
                processAFile(path);
            }
        } else {
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                procesADir(path + "/" + lf[i]);
            }
        }
    }

    public static void dumpLang() {
        int count = 0;
        System.out.println("______________________");
        for (String key : lang.keySet()) {
            System.out.println(count + "\t" + key + "\t" + lang.get(key));
            count++;
        }
    }

    public static void processAFile(String fileName) {

        try {
            //System.out.println("open :" + fileName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-16");
            BufferedReader insource = new BufferedReader(isr);
            String[] allw = new String[LangMap.size()];
            String w = insource.readLine();
            
            while (w != null) {
                //System.out.println(w);
                if (w.startsWith("<tu>")) { // reset
                    //System.out.println("reset"+w);
                    allw = new String[LangMap.size()];
                }
                if (w.startsWith("</tu>")) { // flush
                    for (int i = 0; i < allw.length; i++) {
                        if (allw[i] != null) {
                            outmflf.append(allw[i]);
                        } else {
                            outmflf.append(".");
                        }
                        if (i != allw.length - 1) {
                            outmflf.append("\t");
                        } else {
                            outmflf.append("\n");
                        }
                    }
                }
                if (w.startsWith("<tuv lang=")) { // count
                    totkeep++;
                    if (totkeep % 100000 == 0) {
                        System.out.println(totkeep);
                        dumpLang();
                    }
                    String langcode = extract(w, "<tuv lang=\"", "\">").substring(0, 2);
                    Integer count = lang.get(langcode);
                    if (count == null) {
                        count = 0;
                    }
                    count++;
                    lang.put(langcode, count);
                    // read seg
                    w = insource.readLine();
                    String seg = extract(w, "<seg>", "</seg>");
                    allw[LangMap.getpos(langcode)] = seg;
                }
                w = insource.readLine();


                /*
                if (!txtcat.equals("ERROR")) {
                outmflf.append("#####" + iddoc + "#####\n");
                outmflf.append(txtdoc + "\n");
                outcat.append(iddoc + txtcat + "\n");
                //System.out.println(iddoc + ": " + txtdoc);
                totkeep++;
                } else {
                toterror++;
                }
                txtdoc = "";
                iddoc = lang + totkeep;
                }
                /*              if (w.startsWith("<cat")) {
                txtcat = getCategories(w);
                }
                if (w.startsWith("<tit")) {
                txtdoc += " " + getText(w);
                }
                if (w.startsWith("<abs")) {
                txtdoc += " " + getText(w);
                }
                
                 */
            }


            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public String extract(String s, String start, String end) {
        //System.out.println(" start:"+start+" end:"+end);
        int begrec = s.indexOf(start);
        int endrec = s.indexOf(end, begrec + start.length());
        //System.out.println(" begrec:"+begrec+" endrec:"+endrec);
        if (begrec != -1 & endrec != -1) {
            String rec = s.substring(begrec + start.length(), endrec);
            return rec;
        } else {
            return null;
        }

    }
}
