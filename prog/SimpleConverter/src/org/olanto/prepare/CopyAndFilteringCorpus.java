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

package org.olanto.prepare;

import java.io.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** traitement spécial du corpus ...
 *
 * modification des chemins
 * pour filtrer les .htm (si 2 ième ligne est un commentaire)
 */
public class CopyAndFilteringCorpus {

    static String COLLECT, DOCEXT, EXT, root, targetRoot;
    static int count = 0;
    static final String SEPARATOR = "¦";

    public static void main(String[] args) {
        DOCEXT = ".txt";
        root = "C:/MYCAT/corpus/docs_CDT";
        targetRoot = "C:/MYCAT/corpus/docs/";
        System.out.println("start filtering for ");
        indexdir(root);
        System.out.println("totsave for  :" + count);


    }

    public static void indexdir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            //System.out.println("path:" + f);
            if (path.endsWith(".htm")) {
                indexdoc(path, f.getName());
            }
        } else {
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                indexdir(path + "/" + lf[i]);
            }
        }
    }

    public static void indexdoc(String f, String name) {
        String target = targetRoot + name;
        //System.out.println(f + "   " + target);
        File tryToOpen = new File(target);

        copy(f, target, "UTF-8");
        count++;
        if (count % 100 == 0) {
            System.out.println(count);
        }
    }

    private static void copy(String f, String target, String targetEncoding) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), targetEncoding));
            String newtarget = f.replace("/docs_CDT/", "/docs/");
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newtarget), targetEncoding));
            String w;
            System.out.println("--------------" + f + " -> " + newtarget);
            w = in.readLine();
            output.write(w + "\n");
            System.out.println("1:" + w);
            w = in.readLine();
            System.out.println("2:" + w);
            if (!w.startsWith("<!--")) {
                output.write(w + "\n");
            } else {
                System.out.println("skip comment in:" + f);
            }
            w = in.readLine();
            while (w != null) {
                output.write(w + "\n");
                w = in.readLine();
            }
            output.close();
            output = null;
            in.close();
            in = null;

        } catch (Exception ex) {
            Logger.getLogger(CopyAndFilteringCorpus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
