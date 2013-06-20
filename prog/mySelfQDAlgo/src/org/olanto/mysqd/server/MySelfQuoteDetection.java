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
package org.olanto.mysqd.server;

import java.util.List;
import java.util.Vector;
import org.olanto.mysqd.util.NGramList;
import org.olanto.mysqd.util.TermList;
import static org.olanto.mysqd.util.Utils.file2String;

/**
 * Classe pour trouver les passages semblables
 *
 */
public class MySelfQuoteDetection {

    public String fileName;  // le contenu initia html
    public String toBeProcess;  // le contenu initia html
    public String htmlWithLinks;  // le contenu initial html avec les liens
    public TermList terms; // la liste des termes parsés
    public int minFreq; // fréquence min
    public int minLength; // fréquence min
    public List<NGramList> ngl; // Liste des listes de ngram
    public boolean verbose = false;
    public static ConstStringManager MsgManager;

    public MySelfQuoteDetection(String fileName, int minFreq, int minLength) {  // to debug
        this.fileName = fileName;
        this.minFreq = minFreq;
        this.minLength = minLength;
        toBeProcess = file2String(fileName, "UTF-8");
        init();
    }

    public MySelfQuoteDetection(String fileName, String toBeProcess, int minFreq, int minLength, boolean verbose, ConstStringManager messageMan) {
        //  System.out.println(toBeProcess);
        MySelfQuoteDetection.MsgManager = messageMan;
        this.fileName = fileName;
        this.minFreq = minFreq;
        this.minLength = minLength;
        this.toBeProcess = toBeProcess.replace("&amp;", "&");  // remet les & modifié par gwt

        init();
    }

    private void init() {
        terms = new TermList(toBeProcess); // cherche la liste des termes
        System.out.println("Nbr terms:" + terms.size());
        if (verbose) {
            terms.show();
        }
        ngl = new Vector<NGramList>();
        initFirstNGL(terms, minLength, minFreq);
        NGramList current = initFirstNGL(terms, minLength, minFreq);  // init
        int currentlevel = minLength;
        while (current.size() > 0) { // calcul tous les set jusqu a épuissement
            ngl.add(current);
            if (verbose) {
                System.out.println("record level:" + currentlevel);
            }
            currentlevel++;
            current = computeNextNGL(terms, current, currentlevel, minFreq);
            if (verbose) {
                System.out.println("current size:" + current.size());
            }
            current.RemoveIncludedNgram(ngl.get(ngl.size() - 1)); // éliminer les ngram du niveau précédent
            //ngl.get(ngl.size() - 1).show(); // montre les ngram restants après filtrage
        }
        terms.TryToMarkUrl(ngl, fileName, minLength, minFreq); // place des marqueurs sur les termes
        //terms.show();
        htmlWithLinks = terms.getHTML(toBeProcess);
    }

    public String getHTML() {
        // return toBeProcess;
        return htmlWithLinks;
    }

    /**
     * Initialise le premier set de N-gram
     *
     * @param words
     * @param level
     * @param minocc
     * @return
     */
    private NGramList initFirstNGL(TermList words, int level, int minocc) {
        NGramList first = new NGramList(level);
        first.initFromContent(words, level);
        if (verbose) {
//            System.out.println("----------- raw ngram");
//            first.show();
        }
        first.ReduceToMinOcc(minocc);
        if (verbose) {
//            System.out.println("----------- ngram after filtering");
//            first.show();
        }
        return first;
    }

    /**
     * Calcule les set suivants
     *
     * @param words
     * @param predng
     * @param level
     * @param minocc
     * @return
     */
    private NGramList computeNextNGL(TermList words, NGramList predng, int level, int minocc) {
        NGramList next = new NGramList(level);
        next.computeNextLevel(words, predng, level);
        if (verbose) {
//            System.out.println("----------- next raw ngram");
//            next.show();
        }
        if (verbose) {
            System.out.println("before reduce current size:" + next.size());
        }
        next.ReduceToMinOcc(minocc);
        if (verbose) {
            System.out.println("before after current size:" + next.size());
        }
        if (verbose) {
//            System.out.println("----------- next ngram after filtering");
//            next.show();
        }
        return next;
    }
}
