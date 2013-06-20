/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.mfl.tag2mfl;

import java.io.*;
import java.lang.reflect.*;

/** génération de mfl.
 * 
 */
public class CLEF_EN {

    static int totnull = 0, totdollar = 0, idSeq = 0;
    static MFLFile all;
    static String LANG, EXT;

    public static void main(String[] args) {
        LANG = "EN";
        String FN = "C:/AAA/CLEF2005/DOC/" + LANG;
        all = new MFLFile("C:/D/clef_" + LANG + ".mfl");
        EXT = ".sgml";
        indexdir(FN);
        all.close();
        System.out.println("totsave:" + idSeq);
        System.out.println("totnull:" + totnull);
        System.out.println("totdollar:" + totdollar);

    }

    public static void indexdir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            //System.out.println("path:"+f);
            // if (path.endsWith(EXT)) 
            indexdoc(path);
        } else {
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                indexdir(path + "/" + lf[i]);
            }
        }
    }

    public static void indexdoc(String f) {
        try {
            TaggedFile LF = new TaggedFile(f);
            LF.getNext();
            String full = LF.getrecord();

            while (full != null) {
                String pn = extract(full, "<DOCID>", "</DOCID>");

                // System.out.println(pn+" = "+idSeq);

                all.writeInDir("_$_" + pn + "_$_" + LANG, full);
                if ((idSeq % 1000) == 0) {
                    System.out.println(pn + " = " + idSeq);
                }
                idSeq++;
                full = LF.getrecord();
            } //try
        } catch (Exception e) {
            System.err.println("IO error");
            System.out.println(e.toString());
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
;
}
