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
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.olanto.mycat.lineseg.SegmentationConstant.*;
import org.olanto.util.ExtensionUtils;

/**
 * pour permettre l'intégration des glossaires dans myCAT
 * @author JG
 */
public class BY_EXT_glossaries {

    static String COLLECT, targetRoot, sourceRoot;
    static int count, already, news, noext;
    static ExtensionUtils extutil;
    static boolean verbose = false;

    public static void copy(String LANG, String _sourceRoot, String _targetRoot) {
        count = 0;
        already = 0;
        noext = 0;
        sourceRoot = _sourceRoot;
        targetRoot = _targetRoot;

      extutil=new ExtensionUtils(LANGID);

        System.out.println("____________________________________________________________________");
        System.out.println(sourceRoot + " -> " + targetRoot + " (glosssaries)");
        indexdir(sourceRoot);

        System.out.println("Total no Extension:" + noext);
        System.out.println("Total already Segmented:" + already);
        System.out.println("Total new Segmented:" + news);
        System.out.println("Total Segmented:" + count);


    }

    private static void indexdir(String path) {
//      System.out.println("path:" + path);
        File f = new File(path);
        if (f.isFile()) {
            String EXT = extutil.getNExt(f.getName());
            if (verbose) {
                System.out.println("path:" + f + " ext:" + extutil.getNExt(f.getName()));
            }

            if (EXT.length() == 3) {  // une mono extension
                indexdoc1Ext(path, f.getName(), EXT);
            }
            if (EXT.length() > 3) {  // une multi extension
                indexdocNExt(path, f.getName(), EXT);
            }
            noext++;
        } else {
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                indexdir(path + "/" + lf[i]);
            }
        }
    }
    
/** multi extension */
     private static void indexdocNExt(String f, String name, String EXT) {
        if (f.contains(SEPARATOR)) {
            System.err.println("FATAL ERROR filename contains SEPARATOR:" + f);
            System.exit(0);
        }
        String flatPath = f.substring(sourceRoot.length() + 1, f.length() - name.length());
        flatPath = flatPath.replace("/", SEPARATOR);

  
        String target = targetRoot
                + "/" + GLOSS_LANG
                + "/" + GLOSS_NAME + SEPARATOR + flatPath + name;
        if (verbose) {
            System.out.println(f + "   " + target);
        }
        File tryToOpen = new File(target);
        if (tryToOpen.exists()) {
            if (verbose) {
                System.out.println("already exist:   " + target);
            }
            already++;
            ;
        } else {
            if (verbose)System.out.println("new:   " + target);
            copyFile(f, target, "UTF-8");
            //System.out.println((new SimpleDateFormat("dd/MM/yyyy")).format(FileSegmentation.getLastModified(f)));
            FileSegmentation.setLastModified(target, FileSegmentation.getLastModified(f));  // progagation des dates
            news++;
        }
        count++;
        if (count % 1000 == 0) {
            System.out.println("processed: " + count);
        }
    }
  
    /** mono extension */
    private static void indexdoc1Ext(String f, String name, String EXT) {
        if (f.contains(SEPARATOR)) {
            System.err.println("FATAL ERROR filename contains SEPARATOR:" + f);
            System.exit(0);
        }
        String flatPath = f.substring(sourceRoot.length() + 1, f.length() - name.length());
        flatPath = flatPath.replace("/", SEPARATOR);

        int posOflang = name.lastIndexOf(EXT);

        name = name.substring(0, posOflang) + name.substring(posOflang + 3);
        String target = targetRoot
                + "/" + EXT.substring(1, 3)
                + "/" + GLOSS_NAME + SEPARATOR + flatPath + name;
        if (verbose) {
            System.out.println(f + "   " + target);
        }
        File tryToOpen = new File(target);
        if (tryToOpen.exists()) {
            if (verbose) {
                System.out.println("already exist:   " + target);
            }
            already++;
            ;
        } else {
            if (verbose)System.out.println("new:   " + target);
            copyFile(f, target, "UTF-8");
            //System.out.println((new SimpleDateFormat("dd/MM/yyyy")).format(FileSegmentation.getLastModified(f)));
            FileSegmentation.setLastModified(target, FileSegmentation.getLastModified(f));  // progagation des dates
            news++;
        }
        count++;
        if (count % 1000 == 0) {
            System.out.println("processed: " + count);
        }
    }

    private static void copyFile(String source, String target, String Encoding) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(source), Encoding));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), Encoding));
            String w = in.readLine();
            out.write(w + "\n");
             while (w != null) {
                 out.write(w + "\n");
                 w = in.readLine();
             }
            in.close();
            in = null;
            out.close();
            out = null;
        } catch (Exception ex) {
            Logger.getLogger(BY_EXT_glossaries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 
}
