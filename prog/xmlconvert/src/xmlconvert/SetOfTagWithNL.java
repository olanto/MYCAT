
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
package xmlconvert;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Une classe pour gérer des listes des tags avec newline
 *
 *
 */
public class SetOfTagWithNL {

    HashMap<String, String> tagWithNewLine = new HashMap<String, String>();
    int count = 0;
    /**
     * création d'une liste chargée depuis un fichier.
     *
     * @param fname nom du fichier
     */
    Pattern ps = Pattern.compile("[\\t]");  // le tab
    
    public static void main(String[] args) {
       String fileNL = "C:\\MYCAT\\plugins\\XmlConvertion\\runpack\\tagWithNL.txt";
       SetOfTagWithNL nl=  new SetOfTagWithNL(fileNL);
       System.out.println("caption:" + nl.checkTagAction("caption"));
       System.out.println("bold:" + nl.checkTagAction("bold"));
    }

    public SetOfTagWithNL(String fname) {
        System.out.println("load list of tag with NL from:" + fname);
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                //System.out.println("dont index:"+w);
                w = w.trim();
                String[] part = ps.split(w);
                if (part.length == 2) {
                    tagWithNewLine.put(part[0], part[1]);
                    //System.out.println("add to list:" + part[0] + "->" + part[1]);
                } else {
                    System.out.println("error in line:" + w);
                }

                w = in.readLine();
            }
        } catch (Exception e) {
            System.err.println("IO error in tag with NL (missing file ?)");
        }
    }

    public void add(String tag, String action) {
        tagWithNewLine.put(tag, action);
    }

    public String checkTagAction(String tag) {
        return tagWithNewLine.get(tag);
  
    }


    /**
     * liste des tags.
     *
     * @return la liste
     */
    public final String[] getListOfEntry() {
        String[] result = new String[tagWithNewLine.size()];
        tagWithNewLine.keySet().toArray(result);
        return result;
    }
}
