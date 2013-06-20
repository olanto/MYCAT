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

package org.olanto.zahir.align;

import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import org.olanto.idxvli.IdxStructure;
import org.olanto.mapman.DoParse;
import static org.olanto.util.Messages.*;

/**
 * Une classe pour stocker une phrase.
 * 
 */
public class Sentence {

    public String s;
    public String[] iw;
    public int[] id;
    public float[] score;
    public String numbers;
    public int countNumbers;

    public Sentence(String s) {
        parse(s);
    }

    public void parse(String s) {
        this.s = s;
        DoParse a = new DoParse(s + " .");
        iw = a.tokenizeString();
        if (Global.REDUCE) {
            reduce();
        }
        if (Global.NUMBERS) {
            getNumbers(s);
        }


    }

    public void reduce() {

//        dump();
        Arrays.sort(iw);  // triÃ©e
        int countdiff = iw.length;
        for (int i = 1; i < iw.length; i++) {  // compte les diffÃ©rents
            if (iw[i - 1].equals(iw[i])) {
                countdiff--;
            }
        }
        if (countdiff == iw.length) {
            return; // pas de diffÃ©rent
        } else {
            String[] old = iw;
            iw = new String[countdiff];
            int count = 1;
            iw[0] = old[0];
            for (int i = 1; i < old.length; i++) {  // recopie en compressant
                if (!old[i - 1].equals(old[i])) {
                    iw[count] = old[i];
                    count++;
                }
            }
//            msg("reduce");
//            dump();
        }
    }

    public void convert2id(LexicalTranslation lex) {
        int[] conv = new int[iw.length];
        int countconv = 0;
        Integer idso;
        for (int i = 0; i < iw.length; i++) {
            idso = lex.lexentryso.get(iw[i]);
            if (idso != null) {
                conv[countconv] = idso;
                countconv++;
            }
            id = new int[countconv]; // compresse
            System.arraycopy(conv, 0, id, 0, countconv);
            Arrays.sort(id); // trie
        }
    }

    public void convert2idWithScore(LexicalTranslation lex) {
        ArrayList[] conv = new ArrayList[iw.length];
        int countconv = 0;
        ArrayList idso;
        for (int i = 0; i < iw.length; i++) {
            idso = lex.lexreverse.get(iw[i]);
            if (idso != null) {
                conv[countconv] = idso;
                countconv++;
            }
        }
        int sizeid = 0;
        for (int i = 0; i < countconv; i++) { // cherche la taille du vecteur id
            sizeid += conv[i].size();
        }
        int count = 0;
        double[] d = new double[sizeid];
        for (int i = 0; i < countconv; i++) { // extract id + score
            for (int j = 0; j < conv[i].size(); j++) {
                d[count] = (Double) conv[i].get(j);
                count++;
            }
        }
        Arrays.sort(d); // trie
        id = new int[sizeid]; // sÃ©pare id et score
        score = new float[sizeid];
        for (int i = 0; i < sizeid; i++) {
            id[i] = (int) d[i];
            score[i] = (float) (d[i] - (double) ((int) d[i]));
        }
    }

    public void getNumbers(String s) {
        numbers = "";
        countNumbers = 0;
        boolean inNumber = false;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                if (!inNumber) {
                    countNumbers++;
                    inNumber = true;
                }
                numbers += s.charAt(i);
            } else if (inNumber) {
                numbers += ".";
                inNumber = false;
            }
        }
        if (inNumber) {  // quand cela se termine par un nombre
            numbers += ".";
        }
    }

    public void dump() {
        msg("Sentence:" + s);
        showVector(iw);
        showVector(id);
        showVector(score);

    }

    public void dumpNumbers() {
        msg("#" + countNumbers + " " + numbers);

    }

    public static int countLines(String s) {
        int count = 0;
        try {
            Reader r = new StringReader(s);
            BufferedReader in = new BufferedReader(r);
            String w = in.readLine();

            while (w != null) {
                count++;

                w = in.readLine();
            }
            in.close();
            r.close();

        } catch (Exception e) {
            //error("countLines", e);
        }
        return count;
    }

    public synchronized static Sentence[] getLines(String s, int nblines) {  // doit Ãªtre exclusif !
        Sentence[] l = new Sentence[nblines];
        int count = 0;
        try {
            Reader r = new StringReader(s);
            BufferedReader in = new BufferedReader(r);
            String w = in.readLine();

            while (w != null) {
                l[count] = new Sentence(w);
                count++;
                ;
                w = in.readLine();
            }
            in.close();
            r.close();

        } catch (Exception e) {
           //msg("no txt:"+s);
            //error("countLines", e);
        }
        return l;
    }
}
