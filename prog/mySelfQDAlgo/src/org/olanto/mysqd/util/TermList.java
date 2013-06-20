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
package org.olanto.mysqd.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.olanto.mysqd.server.MySelfQuoteDetection;

/**
 * Une classe pour garder les informations sur un terme
 *
 *
 *
 */
public class TermList {

    Term[] t;
    String statallref;

    public TermList(String htmlText) {  // construit la liste des termes
        DoParse parser = new DoParse(htmlText);
        t = parser.tokenizeString();
    }

    public void TryToMarkUrl(List<NGramList> ngl, String fileName, int minlen, int minocc) {
        List<Ref> allref = new Vector<Ref>();
        for (int i = ngl.size() - 1; i >= 0; i--) { // commence par les plus grands
            NGramList current = ngl.get(i);
            int N = current.N;
            int count = 0;
            for (Iterator<String> iter = current.ng.keySet().iterator(); iter.hasNext();) { // pour chaque n-gram
                String key = iter.next();
                //System.out.println("mark:"+key);
                Occurences occ = current.ng.get(key);
                count++;
                allref.add(new Ref( "N_" + N + "_" + count + "_0", key, N, occ.size()));
                for (int k = 0; k < occ.size(); k++) { // pour chaque occurence du n-gram
                    int firstpos = occ.o.get(k); // premier terme
                    int lastpos = occ.o.get(k) + N - 1; // dernier terme
                    String anchor = "N_" + N + "_" + count + "_" + k;
                    String next = "N_" + N + "_" + count + "_" + (k + 1) % occ.size();  // prochain ngram
                    markBeg(firstpos, anchor, next);
                    markEnd(lastpos, anchor);

                }
            }
        }
        RefStatistic refstat = new RefStatistic(allref);
        statallref = refstat.getStatByRef(fileName, minlen, minocc);
    }

    private boolean isBegMarked(int beg) {
        return t[beg].begmarked;
    }

    private boolean isEndMarked(int end) {
        return t[end].endmarked;
    }

    private void markBeg(int beg, String anchor, String next) {
        if (isBegMarked(beg)) {
            System.out.println("markBeg: this is impossible!");
        } else {
            t[beg].begmarked = true;
            t[beg].anchor = anchor;
            t[beg].nextHlink = next;
        }
    }

    private void markEnd(int end, String close) {
        if (isEndMarked(end)) {
            System.out.println("markEnd:this is impossible!");
        } else {
            t[end].endmarked = true;
            t[end].closeHlink = close;
        }
    }

    public String getHTML(String textforhtml) {
        boolean debug = false;
        StringBuilder s = new StringBuilder();
        int lastcopy = 0;
        int openRef = 0;  // pour gérer les intersections de références
        for (int i = 0; i < t.length; i++) {
            if (isBegMarked(i)) {
                String addtohtml;
                addtohtml = textforhtml.substring(lastcopy, t[i].start - 1);
                s.append(addtohtml);
                lastcopy = t[i].start - 1;
                if (isEndMarked(i)) {  // les deux sont marquéa
                    openRef--;
                    String targetTxt = "";
                    if (debug) {
                        targetTxt = "[E" + t[i].closeHlink + "]";
                    }

                    if (openRef >= 1) {
                        s.append(targetTxt);
                    } else {
                        s.append(targetTxt).append("</a>");
                    }
                } // if
                // marque le début

                openRef++;
                String targetTxt = "";
                if (debug) {
                    targetTxt = "[R" + t[i].anchor + "]";
                }
                if (openRef > 1) {
                    s.append("</a>");
                }
                s.append("<a name=\"#").append(t[i].anchor).append("\"/>");
                s.append("<a href=\"#").append(t[i].nextHlink).append("\" onClick=\"return gwtnav(this);\">").append(targetTxt);

            } else {
                if (isEndMarked(i)) {  // seule la fin est marquée
                    openRef--;
                    String targetTxt = "";
                    if (debug) {
                        targetTxt = "[E" + t[i].closeHlink + "]";
                    }
                    String addtohtml = textforhtml.substring(lastcopy, t[i].end - 1);
                    lastcopy = t[i].end - 1;
                    s.append(addtohtml);
                    s.append(targetTxt).append("</a>");
                }
            }
        } // for
        // si la référence va jusqu'à la fin
        String addtohtml = textforhtml.substring(lastcopy, textforhtml.length());
        s.append(addtohtml);

        //timing.stop();
        int bodypos = s.indexOf("</BODY>");
        if (bodypos != -1) {
            s.insert(bodypos, statallref);
        } else {  // à la fin !
            s.append(statallref);
        }

        return s.toString();
    }

    public void show() {
        for (int i = 0; i < t.length; i++) {
            t[i].show();
        }
    }

    public int size() {
        return t.length;
    }

    public String getNgram(int from, int N) {
        StringBuilder res = new StringBuilder(t[from].term);
        for (int i = from + 1; i < from + N; i++) {
            res.append(" ");
            res.append(t[i].term);

        }
        return res.toString();
    }
}
