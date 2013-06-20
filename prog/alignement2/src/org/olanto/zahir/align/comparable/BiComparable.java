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
package org.olanto.zahir.align.comparable;

import static org.olanto.util.Messages.*;
import org.olanto.idxvli.IdxStructure;
import org.olanto.zahir.align.DocumentSentence;
import org.olanto.zahir.align.Global;
import org.olanto.zahir.align.LexicalTranslation;
import org.olanto.zahir.align.SimInformation;

/* 
 * @author Jacques Guyot 2011
 * @version 1.1
 * modify by Jacques Guyot Sept - 2011
 * 
 *  la version précédante - 125 secondes pour 34 documents
 *  la nouvelle version en mode séquentiele - 26 sec
 *  en mode // 8 htreads 9 Secondes ( avec si peu de documents le nombre de proc n'est pas significatifs)
 * 
 *  en mode // 8 htreads 27 Secondes ( 219 doc)
 *  en mode seq  103 Secondes ( 219 doc) -> facteur 4 
 * 
 * à tester sur un hexacore ?
 * 
 *  13min 20 sec pour aligner WikiUNDL 10000 docs
 * 
 *  modification 
 *      passage en hashmap
 *      ajout de filtre
 *      ajout du séparateur 
 *  correction d'un bug (si ident!=0 alors on ne calculait plus le reste !)
 */
/**
 * Classe stockant la carte des positions entre deux traductions.
 */
public class BiComparable {

    public String encoding;
    public String fromfile, tofile, savefile;
    public DocumentSentence fromdoc, todoc;
    public int fromnblines, tonblines;
    public int countTMX = 0;
    public boolean verbose = true;
    public boolean writefile = true;
    public LexicalTranslation s2t;
    public CollectAndSave saveFile;
    public int countalign, countloop1, iddoc;
    public long counttested;

    public BiComparable(int iddoc, boolean _verbose, String fromfile, String tofile, String encoding,
            float limit, LexicalTranslation _s2t, CollectAndSave saveFile, boolean writefile) {
        verbose = _verbose;
        this.iddoc = iddoc;
        this.fromfile = fromfile;
        this.tofile = tofile;
        this.encoding = encoding;
        this.saveFile = saveFile;
        this.writefile = writefile;
        s2t = _s2t;
        //msg("open fromfile:"+fromfile);
        fromdoc = new DocumentSentence(fromfile, encoding);
        fromdoc.convert2id(s2t);
        //msg("open tofile:"+tofile);
        todoc = new DocumentSentence(tofile, encoding);
        todoc.convert2idWithScore(s2t);
        fromnblines = fromdoc.nblines;
        tonblines = todoc.nblines;
        scanfromGetPair3(limit);

    }

    public void scanfromGetPair3(float limit) {
        counttested = 0;
        SimInformation[] resAlign = new SimInformation[fromnblines];
        int[] fromto = new int[fromnblines];
        int[] tofrom = new int[tonblines];
        float[] maxtofrom = new float[tonblines];
        for (int i = 0; i < fromnblines; i++) {
            fromto[i] = -1;
        }
        for (int j = 0; j < tonblines; j++) {
            tofrom[j] = -1;
            maxtofrom[j] = -1;
        }
        for (int i = 0; i < fromnblines; i++) {
//            if (i % 1000 == 0) {
//                System.out.println(i);
//            }
            float maxscore = -1;
            for (int kkk = 0; kkk < tonblines; kkk++) {
                counttested += fromdoc.lines[i].iw.length * todoc.lines[kkk].iw.length;
                //               SimInformation sim = new SimInformation(s2t, fromdoc.lines[i].iw, todoc.lines[kkk].iw);
                SimInformation sim = new SimInformation(fromdoc.lines[i].iw, todoc.lines[kkk].iw, fromdoc.lines[i].id, todoc.lines[kkk].id, todoc.lines[kkk].score, false, s2t);
//                if (iddoc==13 &&sim.similarity >0)
//                    System.out.println("i:"+i+ "j:"+kkk+" sim:"+sim.similarity + " ; " + sim.countIdent + ";" + sim.countNoIdent);
                if (sim.countNoIdent >= 2 && sim.similarity > maxscore) {
                    if (sim.similarity > maxtofrom[kkk]) { // plus grand qu'un éventuel alignement déjà trouvé
                        countloop1++;
                        maxscore = sim.similarity;
                        resAlign[i] = sim;
                        fromto[i] = kkk;
                        tofrom[kkk] = i;
                    }
                }
            }
        }
        for (int i = 0; i < fromnblines; i++) {
            if (fromto[i] != -1 && tofrom[fromto[i]] == i && resAlign[i].similarity > limit) { // bien connecté
                if (verbose) {
                    System.out.println(resAlign[i].similarity + " ; " + fromdoc.lines[i].s + "\n     ---> " + todoc.lines[fromto[i]].s);
                }
                if (writefile) {
                    saveFile.save(resAlign[i], fromdoc.lines[i].s, todoc.lines[fromto[i]].s);
                    countalign++;
                }
            }
        }

    }

    public void getInfo() {
        msg("___________________________________");
        msg("From:" + fromfile + " #line:" + fromnblines);
        msg("To:" + tofile + " #line:" + tonblines);
        msg("One-one:" + countTMX + " - " + (countTMX * 100) / Math.min(fromnblines, tonblines) + " %");
    }

    public String getInformation() {
        return fromfile + "\t#line:\t" + fromnblines + "\tTo:\t" + tofile + "\t#line:\t" + tonblines + "\tOne-one:\t" + countTMX + "\t-\t" + (countTMX * 100) / Math.min(fromnblines, tonblines) + "\t%";
    }

    {
        Global.REDUCE = true;
        Global.NUMBERS = false;
        Global.NUMBERS = true;
        Global.TEST_IDENT_LIMIT = 2;
    }
}
