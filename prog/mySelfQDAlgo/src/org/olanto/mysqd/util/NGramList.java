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
package org.olanto.mysqd.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Une classe pour garder les informations sur le ngram d'un document
 * 
 *
 *
 */
public class NGramList {

    public int N; // N-gram, le nombre de termes
    public HashMap<String, Occurences> ng;

    public NGramList(int N) {
        this.N = N;
        ng = new HashMap<String, Occurences>();
    }

    public void initFromContent(TermList words, int level) {
        for (int i = 0; i <= words.size() - level; i++) {
            String key = words.getNgram(i, level);
            //System.out.println(key);
            add(key, i);
        }
    }

    public void computeNextLevel(TermList words, NGramList predng, int level) {
        for (Iterator<String> iter = predng.ng.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            Occurences occ = predng.ng.get(key);
            for (int i = 0; i < occ.size(); i++) { // pour chaque occurence
                int nextpos = occ.o.get(i) + level - 1;
                if (nextpos < words.size()) {  // pas à la fin
                    String newkey = key + " " + words.t[nextpos].term;
                    add(newkey, occ.o.get(i));
                }

            }
        }
    }

    public void RemoveIncludedNgram(NGramList predng) {
       // System.out.println("all Ngram:" + getAllNGram());
        WildCharExpander tester = new WildCharExpander(getAllNGram());
        HashMap<String, Occurences> newNg = new HashMap<String, Occurences>();
        for (Iterator<String> iter = predng.ng.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            Occurences occ = predng.ng.get(key);
            boolean notKeep = tester.Contains("* " + key + " *");
            //System.out.println("search: " + key + " find:" + notKeep + " in: " + tester.getFirstExpand("* " + key + " *"));
            if (!notKeep) {
                newNg.put(key, occ);
            }
        }
        //System.out.println(" reduce from:"+predng.ng.size()+" to:"+newNg.size());
        predng.ng = newNg;  
    }

    public void ReduceToMinOcc(int minocc) {
        HashMap<String, Occurences> newNg = new HashMap<String, Occurences>();
        for (Iterator<String> iter = ng.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            Occurences occ = ng.get(key);
            if (occ.size() >= minocc) {
                newNg.put(key, occ);
            }
        }
        ng = newNg;
    }

    public StringBuilder getAllNGram() {
        StringBuilder b = new StringBuilder();  
        b.append(WildCharExpander.ITEM_START);// pour que wordExpander puisse retrouver le premier mot
        for (Iterator<String> iter = ng.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            //System.out.println("key:" + key);
            b.append(" ").append(key).append(" ").append(WildCharExpander.ITEM_STOP).append(WildCharExpander.ITEM_START);
        }
        return b;
    }

    private void add(String key, int occ) {
        Occurences okey = ng.get(key);
        if (okey == null) {
            ng.put(key, new Occurences(occ));
        } else {
            okey.add(occ);
        }
    }

    public void show() {
        System.out.println("Ngram N:" + N);
        System.out.println("size:" + ng.size());
        for (Iterator<String> iter = ng.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            System.out.println(key);
            ng.get(key).show();
        }
    }

    public int size() {
        return ng.size();
    }
}
