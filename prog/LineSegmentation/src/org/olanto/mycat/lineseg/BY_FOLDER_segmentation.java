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
package org.olanto.mycat.lineseg;

import java.io.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.olanto.mycat.lineseg.SegmentationConstant.*;

/**
 *
 * author: Olanto Foundation Geneva
 * ajout d'une section pour éliminer les -e, -E, -f, -F (pas systématique)
 */
public class BY_FOLDER_segmentation {

    static String COLLECT, EXT, targetRoot, sourceRoot;
    static int count, already, news;

    public static void Segmentation(String LANG, String LANGUAGE, String _sourceRoot, String _targetRoot) {
        count = 0;
        already = 0;
        sourceRoot = _sourceRoot + "/" + LANG;
        EXT = ".txt";
        targetRoot = _targetRoot + "/" + LANG + "/";
        System.out.println("____________________________________________________________________");
        System.out.println(sourceRoot + " -> " + targetRoot);
        FileSegmentation.init(LANGUAGE);
        indexdir(sourceRoot);

        System.out.println("Total already Segmented:" + already);
        System.out.println("Total new Segmented:" + already);
        System.out.println("Total Segmented:" + count);


    }

    public static void indexdir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            //System.out.println("path:" + f);
            if (path.endsWith(EXT)) {
                indexdoc(path, f.getName());
            }
        } else {
            String[] lf = f.list();
            if (lf != null) {
                int ilf = Array.getLength(lf);
                for (int i = 0; i < ilf; i++) {
                    if (!path.startsWith(FOLDER_CONVERTED_GLOSSARIES)) { // sauf les glossaires
                        indexdir(path + "/" + lf[i]);
                    }
                }
            }
        }
    }

    public static void indexdoc(String f, String name) {
        if (f.contains(SEPARATOR)) {
            System.err.println("FATAL ERROR filename contains SEPARATOR:" + f);
            System.exit(0);
        }
        String flatPath = f.substring(sourceRoot.length() + 1, f.length() - name.length());
        flatPath = flatPath.replace("/", SEPARATOR);
        //name = name.substring(0, name.length() - 7) + EXT;
        String target = targetRoot + flatPath + name;
        //System.out.println(f + "   " + target);
        File tryToOpen = new File(target);
        if (tryToOpen.exists()) {
            //System.out.println("already exist:   " + target);
            already++;
            ;
        } else {
            //System.out.println("new:   " + target);
            List<String> seg = FileSegmentation.readFile(f, "UTF-8", false);
            copy(seg, target, "UTF-8");
            //System.out.println((new SimpleDateFormat("dd/MM/yyyy")).format(FileSegmentation.getLastModified(f)));
            FileSegmentation.setLastModified(target, FileSegmentation.getLastModified(f));  // progagation des dates
            news++;
        }
        count++;
        if (count % 1000 == 0) {
            System.out.println("processed: " + count);
        }
    }

    private static void copy(List<String> seg, String target, String targetEncoding) {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), targetEncoding));
            for (String s : seg) {
                output.append(s + "\n");
            }

            output.close();
            output = null;
        } catch (Exception ex) {
            Logger.getLogger(BY_FOLDER_segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
