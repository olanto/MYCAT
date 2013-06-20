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

import static org.olanto.util.Messages.*;


/*  Calcul de la similiratité
 i* 
 */
public final class SimInformation {

    public float similarity;
    public int sourceCnt;
    public int targetCnt;
    public int countIdent;
    public int countNoIdent;

//    public SimInformation(LexicalTranslation lex, String[] fromSeq, String[] toSeq) {
//        float sum = 0;
//        int countident = 0;
//        int countnoident = 0;
//        if (fromSeq.length < 3 || toSeq.length < 3) { // trop petit
//            this.sourceCnt = fromSeq.length;
//            this.targetCnt = toSeq.length;
//            return;
//        }
//        float ratio = (float) Math.max(fromSeq.length, toSeq.length) / (float) Math.min(fromSeq.length, toSeq.length);
//        if (ratio > 2.0) { // trop différent
//            this.sourceCnt = fromSeq.length;
//            this.targetCnt = toSeq.length;
//            return;
//        }
//        for (int i = 0; i < fromSeq.length; i++) {
//            if (lex.lexentryso.get(fromSeq[i]) != null) { // évite de faire des comparaisons si l'entrée n'est pas dans le dictionnaire
//                for (int j = 0; j < toSeq.length; j++) {
//                    Float val = lex.lexmap.get(fromSeq[i] + " " + toSeq[j]);
//                    if (val != null) {
//                        sum += val;
//                        countnoident++;
//                    }
//                }
//            }
//        }
////        if (countnoident >= 2) { // éviter de faire du travail inutile
////            for (int i = 0; i < fromSeq.length; i++) {
////                for (int j = 0; j < toSeq.length; j++) {
////                    if (fromSeq[i].equals(toSeq[j])) {
////                        sum += 0.5;
////                        countident++;
////                        break;
////                    }
////                }
////            }
////        }
//        float sim = 2 * sum / (float) (fromSeq.length + toSeq.length);
//        this.similarity = sim;
//        this.sourceCnt = fromSeq.length;
//        this.targetCnt = toSeq.length;
//        this.countIdent = countident;
//        this.countNoIdent = countnoident;
//
//    }
    public SimInformation(String[] fromSeq, String[] toSeq, int[] fromId, int[] toId, float[] score, boolean verbose, LexicalTranslation s2t) {

        float sum = 0;
        int countident = 0;
        int countnoident = 0;
        if (fromId == null || toId == null) { // trop petit
            this.sourceCnt = fromSeq.length;
            this.targetCnt = toSeq.length;
            return;
        }
        if (Global.FILTERS) {
            if (fromSeq.length < 3 || toSeq.length < 3) { // trop petit
                this.sourceCnt = fromSeq.length;
                this.targetCnt = toSeq.length;
                return;
            }
            if (fromId.length < 2 || toId.length < 2) { // trop petit
                this.sourceCnt = fromSeq.length;
                this.targetCnt = toSeq.length;
                return;
            }
            float ratio = (float) Math.max(fromSeq.length, toSeq.length) / (float) Math.min(fromSeq.length, toSeq.length);
            if (ratio > 2.0) { // trop différent
                this.sourceCnt = fromSeq.length;
                this.targetCnt = toSeq.length;
                return;
            }
        }
        // merge sort
        int il1 = fromId.length;
        int il2 = toId.length;
        int wc1 = 0;
        int wc2 = 0;
        while (true) { // merge sort  fromId and toId must be ordered !!!!!
            if (wc1 >= il1) {
                break;
            }
            if (wc2 >= il2) {
                break;
            }
            // System.out.println(wc1+", "+fromId[wc1]+", "+wc2+", "+toId[wc2]);
            if (fromId[wc1] == toId[wc2]) { // and ok
                sum += score[wc2]; // sum score
                if (verbose) {
                    msg("mergesort:" + score[wc2]);
                    msg("   from vector:" + s2t.display(fromId));
                    msg("   to vector  :" + s2t.display(toId));
                    showVector(score);
                }
                countnoident++;
                // wc1++; la liste toId peut comporter des doublons ! (sinon OK)
                wc2++;
            } else if (fromId[wc1] < toId[wc2]) {
                wc1++;
            } else {
                wc2++;
            }
        }

        if (countnoident >= Global.TEST_IDENT_LIMIT) { // éviter de faire du travail inutile
            for (int i = 0; i < fromSeq.length; i++) { // tenir compte des entités identiques               
                for (int j = 0; j < toSeq.length; j++) {
                    if (fromSeq[i].equals(toSeq[j])) {
                        sum += 0.5;            // peuvent déjà avoir été comptées dans le dictionnaire
                        countident++;
                        break;
                    }
                }
            }
        }

        float sim = 2 * sum / (float) (fromSeq.length + toSeq.length);
        this.similarity = sim;
        this.sourceCnt = fromSeq.length;
        this.targetCnt = toSeq.length;
        this.countIdent = countident;
        this.countNoIdent = countnoident;

    }
}
