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
 * Classe de calcul statitisque sur la similarité.
 *  *
 */
public class StatSimilarity {

    private static long getincache, get;

    /**
     *
     */
    public StatSimilarity() {
    }

    /**
     *
     * @param lex
     * @param fromSeq
     * @param toSeq
     * @param ratioline
     * @param verbose
     * @return
     */
    public static float statSimilar(LexicalTranslation lex, String[] fromSeq, String[] toSeq, float ratioline, boolean verbose) {
        float sum = 0;
        //System.out.println("fromSeq.length:"+fromSeq.length+"  toSeq.length:"+toSeq.length);
        for (int i = 0; i < fromSeq.length; i++) {
            boolean isNotIdentity = true;
//            for (int j = 0; j < toSeq.length; j++) {
//                if (fromSeq[i].equals(toSeq[j])) {
//                    sum += 0.5;
//                    isNotIdentity = false;
//                    break;
//                }
//            }
            if (isNotIdentity) {
                for (int j = 0; j < toSeq.length; j++) {
                    sum += statWSWC(lex, fromSeq[i], toSeq[j], verbose);
                }
            }
        }
        //msg("simil:"+sum/fromSeq.length);
        //return sum / (float) fromSeq.length;
        return 2 * sum / (float) (fromSeq.length + toSeq.length);
    }

    /**
     *
     * @param lex
     * @param iwsource
     * @param iwcible
     * @param verbose
     * @return
     */
    public static float statWSWC(LexicalTranslation lex, String iwsource, String iwcible, boolean verbose) {
        get++;

        Float val = lex.lexmap.get(iwsource + " " + iwcible);
        if (val == null) {
            // msg(iwsource+" "+iwcible+" - not found");
            return 0;
        }
        if (verbose) {
            msg("prodcart:" + iwsource + " " + iwcible + " - " + val);
        }
        getincache++;
        return val;
    }

    /**
     *
     */
    public static void statistic() {
        msg("Statistic from StatSimilarity:");
        msg("tested :" + get + " getinLex:" + getincache + " findinLex:" + ((float) getincache / (float) get) + "%");
    }
}
