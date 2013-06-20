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

package org.olanto.dtk;

import java.io.*;
import java.util.Hashtable;
import org.olanto.util.Timer;

/**
 * Une classe pour construire un automate de Markov.
 *
 */
public class MAutomata {

    /** timer pour les tests de performance */
    private static Timer t1;
    /** flag d'impression pour les tests */
    private static boolean verbose = false;
    /** flag d'impression pour les tests */
    // variable de l'automate
    /* si true, on applique une normalisation des caractères du training, actuellement lowercase() */
    private boolean normalise = false;
    /** nbr de token pour l'apprentissage */
    private long sampleSize = 1000000;
    /** ordre de l'automate 1  char-char, 2 bigram-bigram, etc */
    private int tokenSize = 3;
    /** fichier de d'apprentissage */
    private FileReader in;
    /** dètermine si l'on est en train de contruire l'automate (true), sinon on est en consultation */
    private boolean buildMode = true;
    /** structure de l'automate , un n-gram est associè è un noeud */
    private Hashtable<String, MNode> automata = new Hashtable<String, MNode>();
    /** nbr de noeud dèja activè */
    private int totalNode = 0;

    /**
     * crèation d'un automate è partir d'un fichier
     * @param trainingFile fichier contenant le texte pour construire l'automate
     * @param _sampleSize taille de l'èchantillon
     * @param _tokenSize taille des tokens
     */
    public MAutomata(String trainingFile, int _sampleSize, int _tokenSize) {
        sampleSize = _sampleSize;
        tokenSize = _tokenSize;
        char rec[] = new char[tokenSize];
        t1 = new Timer("train:" + trainingFile + " on:" + sampleSize + " token size:" + tokenSize);
        String s;
        MNode lasti = new MNode(".");  // start node
        automata.put(".", lasti);
        totalNode = 1;
        try {
            System.out.println("open file");
            in = new FileReader(trainingFile);
            long sample = sampleSize;
            while (in.ready()) {
                in.read(rec, 0, tokenSize);
                s = new String(rec);
                if (isNormalise()) {
                    s = normalise(s);
                }
                lasti.addLink(s);
                lasti = get_ref(s);
                //   System.out.print (s);
                if (--sample == 0) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buildMode = false;
        t1.stop();
    }

    /**
     * crèation d'un automate è partir d'un fichier
     * @param trainingFile Fichier contenant le texte pour crèer l'automate
     */
    public MAutomata(String trainingFile) {
        char rec[] = new char[tokenSize];
        t1 = new Timer("train:" + trainingFile + " on:" + sampleSize + " token size:" + tokenSize);
        String s;
        MNode lasti = new MNode(".");  // start node
        automata.put(".", lasti);
        totalNode = 1;
        try {
            System.out.println("open file");
            in = new FileReader(trainingFile);
            long sample = sampleSize;
            while (in.ready()) {
                in.read(rec, 0, tokenSize);
                s = new String(rec);
                if (isNormalise()) {
                    s = normalise(s);
                }
                lasti.addLink(s);
                lasti = get_ref(s);
                //   System.out.print (s);
                if (--sample == 0) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buildMode = false;
        t1.stop();
    }

    private String normalise(String w) {
        return w.toLowerCase();
    }

    private MNode get_ref(String c) {
        MNode node = automata.get(c);
        if (node != null) {// il existe dèja
            return node;
        } else {
            if (buildMode) {// c'est un nouveau
                node = new MNode(c);
                automata.put(c, node);
                totalNode++;
                return node;
            } else {
                return null;
            }
        }
    }

    /**
     * èvalue la probabilitè d'appartenance è cette automate
     * @param s texte è èvaluer
     * @return probabilitè (exp(x))
     */
    public double probOfSentence(String s) {
        if (verbose) {
            t1 = new Timer("probOfSentence");
        }
        if (isNormalise()) {
            s = normalise(s);
        }
        double t = 0.0;
        MNode lasti = automata.get(".");
        String from = "", to = ".";
        for (int i = 0; i < s.length() - tokenSize; i += tokenSize) {
            from = to;
            to = s.substring(i, i + tokenSize);
            if (lasti != null) {
                t += lasti.probOfNext(to);
                //System.out.println(from+"->"+to+"/"+lasti.probOfNext(to));
            } else {
                t += -0;
            } // mark unknow
            lasti = automata.get(to);
        }
        if (verbose) {
            t1.stop();
        }
        return t;
    }

    /**
     * gènèration d'une phrase è partir de l'automate
     * @param size taille de la phrase
     * @return phrase gènèrèe
     */
    public String generateText(int size) {
        StringBuffer res = new StringBuffer(size);
        MNode lasti = automata.get(".");
        for (int i = 0; i < size; i++) {
            String s = lasti.getnext();
            res.append(s);
            lasti = get_ref(s);
        }
        return res.toString();
    }

    /** Retourne le nombre de noeuds de cette automate de Markov 
     * @return le nombre de noeuds de cette automate de Markov
     */
    public int getTotalNode() {
        return totalNode;
    }

    /** Retourne la hashtable de cette automate de Markov (uniquement pour les tests et debug)
     * @return la hashtable de cette automate de Markov (uniquement pour les tests et debug)
     */
    public Hashtable<String, MNode> getAutomata() {
        return automata;
    }

    /** Retourne le switch indiquant si la normalisation est active
     * @return le switch indiquant si la normalisation est active
     */
    public boolean isNormalise() {
        return normalise;
    }

    /** active ou dèsactive la normalisation
     * @param aNormalise false=texte brut, true=texte normalisè
     */
    public void setNormalise(boolean aNormalise) {
        normalise = aNormalise;
    }
}
