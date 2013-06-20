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

/**
 * Classe stockant la carte des positions entre deux traductions.
 * 
 */
public class Map {

    /* les maps  en sentence*/
    public int[] fromMap;
    public int[] toMap;
    public float[] fromSimil;
    public float[] toSimil;
    public int[] fromShift, fromCharLength, fromNbWords, fromIncrement;
    public int[] fromtoCharLength, fromtoNbWords, fromMapping;
    public int fromCountOne;
    public boolean[] fromCertainMap;
    public int[] toShift, toCharLength, toNbWords, toIncrement;
    public int[] tofromCharLength, tofromNbWords, toMapping;
    public int toCountOne;

    public Map(int fromSize, int toSize) {
        fromMap = new int[fromSize];
        toMap = new int[toSize];
        fromSimil = new float[fromSize];
        toSimil = new float[toSize];
        fromShift = new int[fromSize];
        fromCharLength = new int[fromSize];
        fromNbWords = new int[fromSize];
        fromIncrement = new int[fromSize];
        fromtoCharLength = new int[fromSize];
        fromtoNbWords = new int[fromSize];
        fromMapping = new int[fromSize];
        fromCertainMap = new boolean[fromSize];

        toShift = new int[toSize];
        toCharLength = new int[toSize];
        toNbWords = new int[toSize];
        toIncrement = new int[toSize];
        tofromCharLength = new int[toSize];
        tofromNbWords = new int[toSize];
        toMapping = new int[toSize];
    }

    public final void addFromPos(int src, int target, float score, int srccharlength, int srcnbwords, int targcharlength, int targnbwords) {
        fromMap[src] = target;
        fromSimil[src] = score;
        fromCharLength[src] = srccharlength;
        fromNbWords[src] = srcnbwords;
        fromtoCharLength[src] = targcharlength;
        fromtoNbWords[src] = targnbwords;

    }

    public final void addToPos(int src, int target, float score, int srccharlength, int srcnbwords, int targcharlength, int targnbwords) {
        toMap[src] = target;
        toSimil[src] = score;
        toCharLength[src] = srccharlength;
        toNbWords[src] = srcnbwords;
        tofromCharLength[src] = targcharlength;
        tofromNbWords[src] = targnbwords;
    }

    public final void compute() {

        //FROM
        for (int i = 0; i < fromMap.length; i++) {// shift entre les deux phrases
            fromShift[i] = i - fromMap[i];
        }
        fromIncrement[0] = fromMap[0] + 1; // incrï¿½ment dans la cible pour chaque ligne (attendu =1)
        for (int i = 1; i < fromMap.length; i++) {
            fromIncrement[i] = fromMap[i] - fromMap[i - 1]; // shift entre les deux phrases
        }
        fromCountOne = 0;
        for (int i = 0; i < fromMap.length; i++) {// shift entre les deux phrases
            if (fromIncrement[i] == 1) {
                fromCountOne++;
            }
        }
        //TO
        for (int i = 0; i < toMap.length; i++) {// shift entre les deux phrases
            toShift[i] = i - toMap[i];
        }
        toIncrement[0] = toMap[0] + 1; // incrï¿½ment dans la cible pour chaque ligne (attendu =1)
        for (int i = 1; i < toMap.length; i++) {
            toIncrement[i] = toMap[i] - toMap[i - 1]; // shift entre les deux phrases
        }
        toCountOne = 0;
        for (int i = 0; i < toMap.length; i++) {// shift entre les deux phrases
            if (toIncrement[i] == 1) {
                toCountOne++;
            }
        }
        // compute mapping
        int lastmapping = 0;
        for (int i = 0; i < fromMap.length; i++) {// approxime le mapping
            if (fromIncrement[i] == 1) {
                fromMapping[i] = fromShift[i];
                lastmapping = fromShift[i];
            } else {
                fromMapping[i] = lastmapping;
            }
        }
        lastmapping = 0;
        for (int i = 0; i < toMap.length; i++) {// approxime le mapping
            if (toIncrement[i] == 1) {
                toMapping[i] = toShift[i];
                lastmapping = toShift[i];
            } else {
                toMapping[i] = lastmapping;
            }
        }
        // compute sure mapping
        for (int i = 0; i < fromMap.length; i++) {// approxime le mapping
            if (fromIncrement[i] == 1) {
                if (toIncrement[fromMap[i]] == 1) {
                    if (i == toMap[fromMap[i]]) {
                        fromCertainMap[i] = true;
                    }
                }
            }
        }

    }

    public void dump() {
        msg("map from -> to");
        for (int i = 0; i < fromMap.length; i++) {
            msg(i + " --> " + fromMap[i] + " ;" + fromShift[i]
                    + " ;" + fromIncrement[i]
                    + " ;" + fromCharLength[i] + " ;" + fromNbWords[i]
                    + " ;" + fromtoCharLength[i] + " ;" + fromtoNbWords[i]
                    + " ; sim:" + fromSimil[i]
                    + " ;" + fromMapping[i]);
        }
        msg("fromCountOne:" + fromCountOne);
        msg("----------------------------------------------------------------------");
        msg("map to -> from");
        for (int i = 0; i < toMap.length; i++) {
            msg(i + " --> " + toMap[i] + " ;" + toShift[i]
                    + " ;" + toIncrement[i]
                    + " ;" + toCharLength[i] + " ;" + toNbWords[i]
                    + " ;" + tofromCharLength[i] + " ;" + tofromNbWords[i]
                    + " ; sim:" + toSimil[i]
                    + " ;" + toMapping[i]);
        }
        msg("toCountOne:" + toCountOne);
    }
//    public final double average(){
//        double sum=0;
//          for (int i=0;i<size;i++){
//            sum+=adjust[i];
//        }
//      return sum/(double)size;
//    }
//
//    public final int median(){
//       int[] copy=new int[size];
//        System.arraycopy(adjust, 0, copy, 0, size);
//        sort(copy);
//        return copy[size/2];
//    }
//
//    public final double ecartmoy(){
//            double avg=average();
//        double sum=0;
//          for (int i=0;i<size;i++){
//            sum+= Math.sqrt((adjust[i]-avg)*(adjust[i]-avg));
//        }
//      return sum/(double)size;
//    }
//
//
//
//
//
}
