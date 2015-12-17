
/* Copyright © 2010-2012 Olanto Foundation Geneva
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
public class SetOfAbreviations {

    HashMap<String, Integer> entryToReplace = new HashMap<String, Integer>();
    int count = 0;
    /**
     * création d'une liste chargée depuis un fichier.
     *
     * @param fname nom du fichier
     */
    Pattern ps = Pattern.compile("[\\t]");  // le tab

    public SetOfAbreviations(String fname) {
        System.out.println("load list of abreviations from:" + fname);
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                //System.out.println("dont index:"+w);
                w += ".";
                w = w.trim();
                entryToReplace.put(w, 1);
                System.out.println("add to list:" + w);

                w = in.readLine();
            }
        } catch (Exception e) {
            System.err.println("IO error in SetOfAbreviations (missing file ?)");
        }
    }

    public void add(String replace, String by) {
        entryToReplace.put(replace, 1);
    }

    public final boolean endWithAbr(String w) {
        //System.out.println("look for abreviation:" + w);
        int pos = w.lastIndexOf(" ", w.length() - 2);  // last char could be a space
        if (pos == -1) {
            return false;
        }

        String last = w.substring(pos + 1, w.length() - 1);
        //System.out.println("last:" + "<--->" + last + "<--->");
        if (last.startsWith("("))last=last.substring(1);
        //System.out.println("last remove (:" + "<--->" + last + "<--->");
        if (entryToReplace.get(last) == null) {
            return false;
        }
        //     System.out.println("last:"+last+" is a abreviation");    
        return true;

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
