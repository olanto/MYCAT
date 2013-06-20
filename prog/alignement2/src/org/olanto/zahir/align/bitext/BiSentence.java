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
package org.olanto.zahir.align.bitext;

import static org.olanto.util.Messages.*;
import org.olanto.idxvli.IdxStructure;
import org.olanto.zahir.align.DocumentSentence;
import org.olanto.zahir.align.Global;
import org.olanto.zahir.align.LexicalTranslation;
import org.olanto.zahir.align.Map;
import org.olanto.zahir.align.SimInformation;
import org.olanto.zahir.align.WriteTMX;

/**
 * Classe stockant la carte des positions entre deux traductions.
 * init -> add wordpos -> compact -> translate charpos
 */
public class BiSentence {

    public String encoding;
    public int windows, windows2, minauto, autopct;
    public float ratiofromto, ratiotofrom;
    private float lastsim;  // global
    public String fromfile, tofile;
    public DocumentSentence fromdoc, todoc;
    public int fromnblines, tonblines;
    public int countTMX = 0;
    public Map map0;
    public Map map1;
    public Map map2;
    public boolean error = false;
//    public static float RATIOMAX=1.6f;
//    public static float RATIOMIN=1/RATIOMAX;
    public static float RATIOMAX = 1.5f;  //english
    public static float RATIOMIN = 0.66f;
//    public static float RATIOMAX = 100.0f;  //english
//    public static float RATIOMIN = 0.1f;
    public static int MIN_LENGTH_TORATIO = 20;
    static boolean ontoalign = false;
    static boolean verbose = false;
    static boolean auto = false;
    static LexicalTranslation s2t;
    public long counttested;

    public BiSentence(
            boolean _auto,  // taille automatique de la fenêtre d'exploration
            int autopct,    // en pour cent de la taille du document (4%)
            int minauto,    // minimum taille en mode auto
            boolean _verbose,
            String fromfile, 
            String tofile, 
            String encoding,
            int windows, // taille manuelle du premier passage (ou maximum si auto)
            int windows2, // taille manuelle du deuxième passage
            LexicalTranslation _s2t) {
        verbose = _verbose;
        this.fromfile = fromfile;
        this.tofile = tofile;
        this.windows = windows;
        this.windows2 = windows2;
        this.autopct = autopct;
        this.minauto = minauto;
        this.encoding = encoding;
        s2t = _s2t;
        fromdoc = new DocumentSentence(fromfile, encoding);
        fromdoc.sumNumbers();  // pour l'ajustement de la fenêtre
        fromdoc.convert2id(s2t);
        todoc = new DocumentSentence(tofile, encoding);
        todoc.convert2idWithScore(s2t);
        fromnblines = fromdoc.nblines;
        tonblines = todoc.nblines;
        if (todoc.lines.length == 0) {
            if(verbose)msg("not a bitext : " + tofile);
            error = true;
            return;
        }
        if (fromdoc.lines.length == 0) {
            if(verbose)msg("source text is empty : " + tofile);
            error = true;
            return;
        }
        if (_auto) {
            this.windows = Math.min(Math.max(fromnblines * autopct / 100, minauto), windows);
        } else {
            this.windows = windows;
        }
        ratiofromto = (float) tonblines / (float) fromnblines;
        ratiotofrom = (float) fromnblines / (float) tonblines;

//        getmaxfrom(7);
        buildMap();
//        dump();

    }

//

    public void getInfo() {
        msg("___________________________________");
        msg("From:" + fromfile + " #line:" + fromnblines);
        msg("To:" + tofile + " #line:" + tonblines);
        msg("One-one:" + countTMX + " - " + (countTMX * 100) / Math.min(fromnblines, tonblines) + " %");
    }

    public String getInformation() {
        return windows + "\t" + fromfile + "\t#line:\t" + fromnblines + "\tTo:\t" + tofile + "\t#line:\t" + tonblines + "\tOne-one:\t" + countTMX + "\t-\t" + (countTMX * 100) / Math.min(fromnblines, tonblines) + "\t%";
    }

    public void buildMap() {
        map1 = new Map(fromnblines, tonblines);
        map2 = new Map(fromnblines, tonblines);
        adjustWindow();

        buildMapFrom();
        buildMapTo();
        if (verbose) {
            map1.dump();
        }
        map1.compute();
        if (verbose) {
            map1.dump();
        }
        buildMap2From();
        buildMap2To();
        map2.compute();
        if (verbose) {
            map2.dump();
        }
    }

    public void buildCertainMap(String fname, String _srcLanguage, String _targetLanguage) {



        WriteTMX tmx = new WriteTMX(fname, _srcLanguage, _targetLanguage);

        for (int i = 0; i < fromnblines; i++) {
            if (map2.fromCertainMap[i]) {
                countTMX++;
                int toline = map2.fromMap[i];

                if (verbose) {
                    msg("ok for," + i + ": " + fromdoc.lines[i].s + "\n"
                            + "-->," + toline + ": " + todoc.lines[toline].s + "\n");
                }
                tmx.add2Sentence(fromdoc.lines[i].s, todoc.lines[toline].s);
            }
        }
        tmx.tmxClose();
    }

    public void findHoleMap2(String fname, String _srcLanguage, String _targetLanguage) {

        WriteTMX tmx = new WriteTMX(fname, _srcLanguage, _targetLanguage);

        for (int i = 2; i < fromnblines - 2; i++) {
            if (map2.fromMap[i] == 0
                    && map2.fromShift[i - 1] == map2.fromShift[i - 2]
                    && map2.fromShift[i + 1] == map2.fromShift[i - 2]
                    && map2.fromShift[i + 2] == map2.fromShift[i - 2]) {
                int toline = map2.fromMap[i - 1] + 1;

                msg("could be ok for," + i + ": " + fromdoc.lines[i].s + "\n"
                        + "-->," + toline + ": " + todoc.lines[toline].s + "\n");
                tmx.add2Sentence(fromdoc.lines[i].s, todoc.lines[toline].s);

            }
        }
        tmx.tmxClose();
    }

    public void findHoleMap(String fname, String _srcLanguage, String _targetLanguage) {

        WriteTMX tmx = new WriteTMX(fname, _srcLanguage, _targetLanguage);

        for (int i = 2; i < fromnblines - 2; i++) {
            if (map2.fromMap[i] == 0
                    && map2.fromShift[i - 1] == map2.fromShift[i + 1]) {
                int toline = map2.fromMap[i - 1] + 1;

                msg("could be ok for," + i + ": " + fromdoc.lines[i].s + "\n"
                        + "-->," + toline + ": " + todoc.lines[toline].s + "\n");
                tmx.add2Sentence(fromdoc.lines[i].s, todoc.lines[toline].s);

            }
        }
        tmx.tmxClose();
    }

    public void adjustWindow() {


        int maxnumber = 0;
        for (int i = 0; i < fromnblines; i++) { // cherche les nombres les plus complexes
            if (maxnumber < fromdoc.lines[i].countNumbers) {
                maxnumber = fromdoc.lines[i].countNumbers;
            }
        }
        if (verbose) {
            msg("maxnumbers:" + maxnumber);
        }
        int maxecart = 0;
        int nbmeasure = 0;
        for (int k = maxnumber; k > 2; k--) { // cherche les nombres les plus complexes
            if (verbose) {
                msg("explore:" + k);
            }
            for (int i = 0; i < fromnblines; i++) { //
                if (k == fromdoc.lines[i].countNumbers) {
                    for (int j = 0; j < tonblines; j++) {
                        if (fromdoc.lines[i].numbers.equals(todoc.lines[j].numbers)) {
                            int todiag = (int) ((float) i * ratiofromto);
                            if (maxecart < Math.abs(todiag - j)) {
                                maxecart = Math.abs(todiag - j);
                            }
                            if (verbose) {
                                msg(k + " ," + maxecart + " ," + i + " ," + j + " " + fromdoc.lines[i].numbers);
                            }
                            nbmeasure++;
                        }
                    }

                }
            }
        }
        if (maxecart < windows) {  // écart plus petit
            if (fromnblines > 100) {   // un bon nombre de lign
                if (nbmeasure > 10) {   // assez de mesure
                    if(verbose)msg("new windows:" + windows + " -> " + Math.max(Math.min(maxecart, windows),minauto));
                    windows = Math.max(Math.min(maxecart + 3, windows),minauto);

                }
            }
        }

    }

    public void buildMapFrom() {

        //Timer t1 = new Timer("build map FROM-->TO");
        for (int i = 0; i < fromnblines; i++) {
            //   for(int i=0;i<40;i++){
            int toline = getmaxfrom(i);

            if (verbose) {
                msg("i:" + i + " toline:" + toline);
                msg("max for," + i + ": " + fromdoc.lines[i].s + "\n"
                        + "-->," + toline + ": " + todoc.lines[toline].s + "\n");
            }

            map1.addFromPos(i, toline, lastsim,
                    fromdoc.lines[i].s.length(), fromdoc.lines[i].iw.length,
                    todoc.lines[toline].s.length(), todoc.lines[toline].iw.length);
        }
        //t1.stop();
        // StatSimilarity.statistic();
    }

    public void buildMap2From() {

        //Timer t1 = new Timer("build map2 FROM-->TO");
        for (int i = 0; i < fromnblines; i++) {
            //   for(int i=0;i<40;i++){
            int toline = getmaxfrom(i, i - map1.fromMapping[i]);

            if (verbose) {
                msg("max for," + i + ": " + fromdoc.lines[i].s + "\n"
                        + "-->," + toline + ": " + todoc.lines[toline].s + "\n");
            }

            map2.addFromPos(i, toline, lastsim,
                    fromdoc.lines[i].s.length(), fromdoc.lines[i].iw.length,
                    todoc.lines[toline].s.length(), todoc.lines[toline].iw.length);
        }
        //t1.stop();
        // StatSimilarity.statistic();
    }

    public int getmaxfrom(int fromline) {
        boolean verbose = false;
        lastsim = -1;
        int maxline = 0;
        int begwindow = Math.max((int) ((float) fromline * ratiofromto) - windows, 0);
        int endwindow = Math.min((int) ((float) fromline * ratiofromto) + windows, tonblines);
        if (verbose) {
            msg("getmaxfrom max for:" + fromline + " Windows " + begwindow + ".." + endwindow + ": " + fromdoc.lines[fromline].s);
        }
        for (int i = begwindow; i < endwindow; i++) {
            if (verbose) {
                msg("getmaxfrom max for," + fromline + " to " + i + ": " + todoc.lines[i].s);
            }
            counttested += fromdoc.lines[fromline].iw.length * todoc.lines[i].iw.length;
            if (similarlength(fromline, i)) {
                // float sim = StatSimilarity.statSimilar(s2t, fromdoc.lines[fromline].iw, todoc.lines[i].iw, ratioLength(fromline, i));
                SimInformation sim = new SimInformation(fromdoc.lines[fromline].iw, todoc.lines[i].iw, fromdoc.lines[fromline].id, todoc.lines[i].id, todoc.lines[i].score, false, s2t);
                if (sim.similarity > lastsim) {
                    lastsim = sim.similarity;
                    maxline = i;
                }
            } else {
                if (verbose) {
                    msg("getmaxfrom no similar in length");
                }
            }
        }
        if (verbose) {
            msg("  max," + maxline);
        }
        return maxline;
    }

    public int getmaxfrom(int fromline, int mapping) {
        lastsim = -1;
        int maxline = 0;
        int begwindow = Math.max(mapping - windows2, 0);
        int endwindow = Math.min(mapping + windows2, tonblines);
        //msg("max for,"+fromline+" Windows "+begwindow+".."+endwindow);
        for (int i = begwindow; i < endwindow; i++) {
            //msg("max for,"+fromline+" to "+i+ ": "+ todoc.lines[i].s);
            counttested += fromdoc.lines[fromline].iw.length * todoc.lines[i].iw.length;
            if (similarlength(fromline, i)) {
                SimInformation sim = new SimInformation(fromdoc.lines[fromline].iw, todoc.lines[i].iw, fromdoc.lines[fromline].id, todoc.lines[i].id, todoc.lines[i].score, false, s2t);
//                float simprodcart = StatSimilarity.statSimilar(s2t, fromdoc.lines[fromline].iw, todoc.lines[i].iw, ratioLength(fromline, i), false);
//                if (Math.abs(simprodcart - sim.similarity) > 0.001) {
//                    msg("simprodcart:" + simprodcart + " new sim:" + sim.similarity);
//                    simprodcart = StatSimilarity.statSimilar(s2t, fromdoc.lines[fromline].iw, todoc.lines[i].iw, ratioLength(fromline, i), true);
//                    sim = new SimInformation(fromdoc.lines[fromline].iw, todoc.lines[i].iw, fromdoc.lines[fromline].id, todoc.lines[i].id, todoc.lines[i].score, true, s2t);
//                }
                if (sim.similarity > lastsim) {
                    lastsim = sim.similarity;
                    maxline = i;
                }
            }
        }
        //msg("max,"+fromline+", "+maxline);
        return maxline;
    }

    public void buildMapTo() {

        //Timer t1 = new Timer("build map TO-->FROM");
        for (int i = 0; i < tonblines; i++) {
            //   for(int i=0;i<40;i++){
            int fromline = getmaxto(i);

            if (verbose) {
                msg("max for," + i + ": " + todoc.lines[i].s + "\n"
                        + "-->," + fromline + ": " + fromdoc.lines[fromline].s + "\n");
            }

            map1.addToPos(i, fromline, lastsim,
                    todoc.lines[i].s.length(), todoc.lines[i].iw.length,
                    fromdoc.lines[fromline].s.length(), fromdoc.lines[fromline].iw.length);
        }
        //t1.stop();
        //StatSimilarity.statistic();
    }

    public void buildMap2To() {

        //Timer t1 = new Timer("build map2 TO-->FROM");
        for (int i = 0; i < tonblines; i++) {
            //   for(int i=0;i<40;i++){
            int fromline = getmaxto(i, i - map1.toMapping[i]);

            if (verbose) {
                msg("max for," + i + ": " + todoc.lines[i].s + "\n"
                        + "-->," + fromline + ": " + fromdoc.lines[fromline].s + "\n");
            }

            map2.addToPos(i, fromline, lastsim,
                    todoc.lines[i].s.length(), todoc.lines[i].iw.length,
                    fromdoc.lines[fromline].s.length(), fromdoc.lines[fromline].iw.length);
        }
        //t1.stop();
        //StatSimilarity.statistic();
    }

    public int getmaxto(int toline) {
        lastsim = -1;
        int maxline = 0;
        int begwindow = Math.max((int) ((float) toline * ratiotofrom) - windows, 0);
        int endwindow = Math.min((int) ((float) toline * ratiotofrom) + windows, fromnblines);
        //msg("max for,"+toline+" Windows "+begwindow+".."+endwindow);
        for (int i = begwindow; i < endwindow; i++) {
            //msg("max for,"+toline+" to "+i+ ": "+ fromdoc.lines[i].s);
            counttested += fromdoc.lines[i].iw.length * todoc.lines[toline].iw.length;
            if (similarlength(i, toline)) {
                //   float sim = StatSimilarity.statSimilar(s2t, fromdoc.lines[i].iw, todoc.lines[toline].iw, ratioLength(i, toline));
                SimInformation sim = new SimInformation(fromdoc.lines[i].iw, todoc.lines[toline].iw, fromdoc.lines[i].id, todoc.lines[toline].id, todoc.lines[toline].score, false, s2t);
                if (sim.similarity > lastsim) {
                    lastsim = sim.similarity;
                    maxline = i;
                }
            }
        }
        if (verbose) {
            msg("max," + toline + ", " + maxline);
        }
        return maxline;
    }

    public int getmaxto(int toline, int mapping) {
        lastsim = -1;
        int maxline = 0;
        int begwindow = Math.max(mapping - windows2, 0);
        int endwindow = Math.min(mapping + windows2, fromnblines);
        //msg("max for,"+toline+" Windows "+begwindow+".."+endwindow);
        for (int i = begwindow; i < endwindow; i++) {
            //msg("max for,"+toline+" to "+i+ ": "+ fromdoc.lines[i].s);
            counttested += fromdoc.lines[i].iw.length * todoc.lines[toline].iw.length;
            if (similarlength(i, toline)) {
                //   float sim = StatSimilarity.statSimilar(s2t, fromdoc.lines[i].iw, todoc.lines[toline].iw, ratioLength(i, toline));
                SimInformation sim = new SimInformation(fromdoc.lines[i].iw, todoc.lines[toline].iw, fromdoc.lines[i].id, todoc.lines[toline].id, todoc.lines[toline].score, false, s2t);
                if (sim.similarity > lastsim) {
                    lastsim = sim.similarity;
                    maxline = i;
                }
            }
        }
        if (verbose) {
            msg("max," + toline + ", " + maxline);
        }
        return maxline;
    }

    public boolean similarlength(int linesrc, int linetarget) {

        if (fromdoc.lines[linesrc].iw.length < MIN_LENGTH_TORATIO && todoc.lines[linetarget].iw.length < MIN_LENGTH_TORATIO) {  // trop petit pour un ratio
            return true;
        } else {
            float ratioline = ratioLength(linesrc, linetarget);
            if (verbose) {
                msg("ratioline:" + ratioline + " from:" + fromdoc.lines[linesrc].s.length() + " from:" + todoc.lines[linetarget].s.length());
            }
            if (ratioline > RATIOMIN && ratioline < RATIOMAX) {
                return true;
            }
            return false;
        }
    }

    public float ratioLength(int linesrc, int linetarget) {
        float ratioline = (float) fromdoc.lines[linesrc].iw.length / (float) todoc.lines[linetarget].iw.length;
        if (ratioline > 1.0) {
            return ratioline;
        } else {
            return 1 / ratioline;
        }
    }

    {
        Global.REDUCE = true;
        Global.NUMBERS = true;
        Global.FILTERS = false;
        Global.TEST_IDENT_LIMIT=0;

    }
}
