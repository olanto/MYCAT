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
package org.olanto.mycat.lineseg;

import java.io.*;
import java.lang.reflect.*;
import org.olanto.senseos.SenseOS;

/**
 * élimine les documents dont les sources ont disparus. modification des chemins
 * ajout d'une section pour éliminer les -e, -E, -f, -F (pas systématique)
 */
public class RemoveMissingFiles {

    static String COLLECT, DOCEXT, root, docs, targetRoot;
    static int count = 0;
    static final String SEPARATOR = "¦";

    public static void main(String[] args) {
        String _DOCEXT = ".txt";
        String _docs = SenseOS.getMYCAT_HOME() + "/corpus/docs";
        String _root = SenseOS.getMYCAT_HOME() + "/corpus/source";
        RemoveMissingFiles(_docs, _root, _DOCEXT);
    }

    public static void RemoveMissingFiles(String _docs, String _root, String _DOCEXT) {
        DOCEXT = _DOCEXT;
        docs = _docs;
        root = _root;
        System.out.println("start removing for " + docs);
        indexdir(root);
        System.out.println("total removed: " + count);

    }

    public static void indexdir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            //System.out.println("path:" + path);
            if (path.contains(DOCEXT)) {
                indexdoc(path, f);
            }
        } else {
            System.out.println(path + "->");
            if (path != root) {
                String fdirname = docs + "/" + path.substring(root.length() + 1, path.length());
                System.out.println(f + "->" + fdirname);
                File fdocs = new File(fdirname);
                if (!fdocs.exists()) {
                    System.err.println("delete folder:" + f);  // toujours avec un coup de retard
                    f.delete();
                    count++;
                    return;
                }
            }
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                indexdir(path + "/" + lf[i]);

            }
        }
    }

    public static void indexdoc(String name, File f) {

        String fdocname = docs + "/" + name.substring(root.length() + 1, name.length() - 4);
//        System.out.println(f+"->"+fdirname);
        File fdocs = new File(fdocname);
        if (!fdocs.exists()) {
            System.err.println("delete file:" + f);
            f.delete();
            count++;

        }
    }
}
