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
import java.util.*;
import java.util.regex.Pattern;
import org.olanto.senseos.SenseOS;
import org.olanto.util.Timer;

/**
 * Une classe pour gérer des listes des remplacements lors de la segmentation
 *
 *
 */
public class SetOfReplacements {

    HashMap<String, String> entryToReplace = new HashMap<String, String>();
    int count = 0;
    /**
     * création d'une liste chargée depuis un fichier. le fichier a la forme t
     * TAB t CR t TAB t (format utf-8)
     *
     * @param fname nom du fichier
     */
    Pattern ps = Pattern.compile("[\\t]");  // le tab

    public static void main(String[] args) {  // only for test and debug
        String fname = SenseOS.getMYCAT_HOME() + "/config/ReplaceChar.txt";
        SetOfReplacements replace = new SetOfReplacements(fname);
        String test = "123456 l'éléphsnt";
        for (int i = 0; i < 16; i++) {
            test += test;
        }
        Timer t1 = new Timer("replace length : " + test.length());
        String res = replace.replaceAll(test);
        //System.out.println(test+" -> "+res);
        t1.stop();
        t1 = new Timer("replace with builder length : " + test.length());
        res = replace.replaceAll2(test);
        //System.out.println(test+" -> "+res);
        t1.stop();
    }

    public SetOfReplacements(String fname) {
        System.out.println("load list of replace from:" + fname);
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                //System.out.println("dont index:"+w);
                String[] s = ps.split(w);
                if (s.length != 2) {
                    System.out.println("error in list of replacements:" + w);
                } else {
                    entryToReplace.put(s[0], s[1]);
                    System.out.println("add to list:" + w);
                }
                w = in.readLine();
            }
        } catch (Exception e) {
            System.err.println("IO error in SetOfReplacements (missing file ?)");
        }
    }

    /**
     * applique tous les remplacements
     *
     * @param w à traiter
     * @return remplacement effectuer
     */
    public final String replaceAll(String w) {
        String[] toberep = getListOfEntry();
        for (int i = 0; i < toberep.length; i++) {
            String rep = entryToReplace.get(toberep[i]);
            //System.out.println("process: " + toberep[i] + " -> " + rep);
            w = w.replace(toberep[i], rep);
        }
        return w;
    }

    public final String replaceAll2(String w) {
        String[] toberep = getListOfEntry();
        StringBuilder res = new StringBuilder(w);
        for (int i = 0; i < toberep.length; i++) {
            String rep = entryToReplace.get(toberep[i]);
            System.out.println("process: " + toberep[i] + " -> " + rep);
            repBuilder(res, toberep[i], rep);
        }
        return res.toString();
    }

    public static void repBuilder(StringBuilder builder, String from, String to) {
        // plus lent que String.replace
        // sans doute en calculant tous les remplacements à l'avance, on peut tout transformer en un append ... à explorer
        int idx = builder.lastIndexOf(from);
        while (idx != -1) {
            builder.replace(idx, idx + from.length(), to);
            idx += to.length();
            idx = builder.lastIndexOf(from, idx);
        }
    }

    /**
     * liste des termes à remplacer.
     *
     * @return la liste
     */
    public final String[] getListOfEntry() {
        String[] result = new String[entryToReplace.size()];
        entryToReplace.keySet().toArray(result);
        return result;
    }
}
