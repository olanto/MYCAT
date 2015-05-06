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
package org.olanto.mycat.ngram.extract.news;

import java.io.*;
import org.olanto.util.StringManipulation;

/**
 *
 * @author jg
 */
public class extractNews {

    static String source;
    static long totkeep=0;
    static int limit = Integer.MAX_VALUE;
    /*   
     */

    public static void main(String[] args) {
        try {
            //  procesADir("h:/PATDB/XML/docdb");
            String drive = "C:";
            source = drive + "/CORPUS/NEWS/2004-2005/DN20040117_NML.DAT";
            Process(source);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void Process(String fileName) {

        System.out.println("------------- entry dictionary: " + fileName);
        int totdictEntry = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            BufferedReader so = new BufferedReader(isrso);
            OutputStreamWriter outmflf = new OutputStreamWriter(new FileOutputStream(source + ".mflf"), "UTF-8");
            String wso = so.readLine();
            String datenews = "";
            boolean innews = false;
            while (wso != null && totdictEntry < limit) {
                if (wso.equals("</body>")) {
                    innews = false;
                }
                if (wso.startsWith("<doc-retransmission docdate=\"")) {
                    datenews=wso.substring(29, 37);
                    //System.out.println(datenews);
                }
                if (innews) {
                    outmflf.append(cleanText(wso) + "\n");
                }
                if (wso.equals("<body>")) {
                    innews = true;
                    totdictEntry++;
                    totkeep++;
                     outmflf.append("#####"+datenews +"-"+totkeep+"#####"+"\n");
                }
               wso = so.readLine();
            }
            so.close();
            outmflf.close();
            System.out.println("   read entries: " + totdictEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static String cleanText(String s){
        s=s.replace("&apos;", "'");
       s=s.replace("&amp;", " ");
      s=s.replace("&quot;", "\"");
      s=s.replace("&lt;", " ");
      s=s.replace("&gt;", " ");
      s=s.replace("<p>", "");
      s=s.replace("</p>", "");
      s=s.replace("<pre>", "");
      s=s.replace("</pre>", "");
      s=s.replace("<text>", "");
      s=s.replace("</text>", "");
      s=s.replace("<headline>", "");
      s=s.replace("</headline>", "");
      s=s.replace("(MORE)", "");
     s=s.replace("(END)", "");
    s=s.trim();
        return s;
    }
    
    
}
