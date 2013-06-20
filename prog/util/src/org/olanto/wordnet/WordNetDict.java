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

package org.olanto.wordnet;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import static org.olanto.util.Messages.*;

/**
pour extraction des termes de wordnet
 */
public class WordNetDict {

    private static Hashtable<String, String> id2gen = new Hashtable<String, String>();
    private static Hashtable<String, String> id2form = new Hashtable<String, String>();
    private static Pattern p = Pattern.compile("[\\s\\.+~*,\"\\(\\)]");  // les fins de mots

    /**
     * application de test
     * @param args sans
     */
    public WordNetDict() {
        readNativeWN("C:/AAA/WN/ADJ.DAT.dic");
        readNativeWN("C:/AAA/WN/NOUN.DAT.dic");
    }

    public static void main(String[] args) {  // only for test

        WordNetDict wn = new WordNetDict();
        String wsd = "WSD10749669-n";
        msg(wsd + "-f->" + wn.getForm(wsd));
        msg(wsd + "-g->" + wn.getGen(wsd) + "-f->" + wn.getForm(wn.getGen(wsd)));
        wsd = "WSD02726367-a";
        msg(wsd + "-f->" + wn.getForm(wsd));
        msg(wsd + "-g->" + wn.getGen(wsd) + "-f->" + wn.getForm(wn.getGen(wsd)));

    }

    public String getForm(String wsd) {
        return id2form.get(wsd);
    }

    public String getGen(String wsd) {
        return id2gen.get(wsd);
    }

    private static void readNativeWN(String fname) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            String w = in.readLine();
            //msg("read:" + w);
            while (w != null) {
                String[] words = p.split(w);
                id2gen.put(words[0], words[2]);
                id2form.put(words[0], words[1]);
                w = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
