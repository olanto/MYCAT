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
import static org.olanto.util.Messages.*;

/** pour extraction des termes de wordnet
 */
public class WrapNoun {

    private static OutputStreamWriter out;

    /**
     * application de test
     * @param args sans
     */
    public static void main(String[] args) {


        String filename = "C:/AAA/WN/NOUN.DAT";
        try {
            out = new OutputStreamWriter(new FileOutputStream(filename + ".dic"));
            readNativeWN(filename);
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void write(String id, String form, String hyper) {
        try {
            out.write(id + " " + form + " " + hyper + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void readNativeWN(String fname) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            String w = in.readLine();
            msg("read:" + w);
            while (w != null) {
                String id = "WSD" + w.substring(0, 8) + "-" + w.substring(12, 13);
                int lastchar = w.indexOf(" ", 17);
                String form = "";
                if (lastchar == -1) {
                    msg("no forms term in:" + w);
                } else {
                    form = w.substring(17, lastchar);
                }
                String hyper = "NHT";
                int firsthyper = w.indexOf("@ ", lastchar);
                if (firsthyper == -1) {
                    msg("no hyper term in:" + w);
                } else {
                    hyper = "WSD" + w.substring(firsthyper + 2, firsthyper + 10) + "-" + w.substring(firsthyper + 11, firsthyper + 12);
                }


                write(id, form, hyper);
                //msg(id + " " + form + " " + hyper);
                w = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
