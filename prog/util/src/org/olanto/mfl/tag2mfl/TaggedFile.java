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

/** génération de mfl.
 * 
 */
public class TaggedFile {

    static BufferedReader in;
    static final boolean verbose = false;
    static StringBuffer news;
    static String fulltext;
    static int idx;
    static String w = "";

    TaggedFile(String fname) {
        try {
            in = new BufferedReader(new FileReader(fname));
        } catch (Exception e) {
            System.err.println("IO error during TaggedFile= file");
        }
    }

    public void getNext() {
        news = new StringBuffer("");
        try {
            w = in.readLine();
            while (w != null) {
                //System.out.println("in news:"+w);
                news.append(w + "\n");
                w = in.readLine();
            }
        } catch (Exception e) {
            System.err.println("IO error during read TaggedFile :" + w);
            e.printStackTrace();
        }
        fulltext = news.toString();
        idx = 0;
    }

    public String getrecord() {
        int begrec = fulltext.indexOf("<DOC>", idx);
        int endrec = fulltext.indexOf("</DOC>", idx);
        //System.out.println(" begrec:"+begrec+" endrec:"+endrec+" idx:"+idx);
        if (begrec != -1 & endrec != -1) {
            String rec = fulltext.substring(begrec, endrec + 6);
            idx = endrec + 6;
            return rec;
        } else {
            return null;
        }
    }
}
