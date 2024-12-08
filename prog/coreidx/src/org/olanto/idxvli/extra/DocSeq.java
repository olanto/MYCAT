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

package org.olanto.idxvli.extra;

import org.olanto.idxvli.*;
import static org.olanto.idxvli.IdxConstant.*;
import static org.olanto.util.Messages.*;

/** Une classe pour stocker la séquence des termes des documents sous forme d'un vecteur d'entier.
 * 
 *  Lors de l'indexation, il faut utiliser, un mode qui active la création
 *  des séquence de terme -- docMore = true;
 *
 *  l'indexeur construit alors automatiquement les vecteurs de séquence de termes.
 *
 *  après coup, on peut recharger la séquence avec getSeqOfDoc dans IdxStructure
 *
 * 
 *
 *
 */
public class DocSeq {

    // pour stocker la séquence des termes
    /** longueur maximum d'un document en termes indexés */
    static int lastseq = 0;
    static int[] seqOfWords = new int[DOC_MAXOCCLENGTH];

    DocSeq() {
    }

    /** initialise la création de séquences (uniquement pour l'indexeur)
     */
    public static void init() {
        seqOfWords = new int[DOC_MAXOCCLENGTH];  // pour la création des séquences
    }

    /** initialise la séquence pour un nouveau document (uniquement pour l'indexeur)
     */
    public static void reset() {
        lastseq = 0; // prépare un nouveau document
    }

    /**
     * ajoute un mot à la séquence (uniquement pour l'indexeur)
     * @param w mot à ajouter
     */
    public static void addWord(int w) {
        seqOfWords[lastseq] = w;
        lastseq++;
    }

    /** retourne  la séquence compactée (uniquement pour l'indexeur.
     * @return la séquence compactée
     */
    public static int[] compact() {
        int[] res = new int[lastseq];
        System.arraycopy(seqOfWords, 0, res, 0, lastseq);
        return res;
    }

    /** convertir une séquence en liste de mots
     * @param seq vecteur de termes
     * @param glue indexeur de référence
     * @return liste de mots
     */
    public static String[] toTextList(int[] seq, IdxStructure glue) {
        String[] res = new String[seq.length];
        for (int i = 0; i < seq.length; i++) {
            res[i] = glue.getStringforW(seq[i]);
        }
        return res;
    }

    /** convertir une séquence en chaine de caractères
     * @param seq vecteur de termes
     * @param glue indexeur de référence
     * @return traduction en caractères
     */
    public static String toText(int[] seq, IdxStructure glue) {
        String res = "";
        for (int i = 0; i < seq.length; i++) {
            res += glue.getStringforW(seq[i]) + " ";
        }
        return res;
    }

    /** extrait le texte correspondant à une sous séquence du document
     * @param seq sequence de référence du document
     * @param glue indexeur de référence
     * @param from début de la séquence à extraire
     * @param to fin de la séquence à extraire
     * @return texte extrait du document
     */
    public static String extract(int[] seq, IdxStructure glue, int from, int to) {
        String res = "";
        for (int i = from; i < to; i++) {
            res += glue.getStringforW(seq[i]) + " ";
        }
        return res;
    }

    /** extrait les id des mots d'une sous séquence du document
     * @param seq sequence de référence du document
     * @param from début de la séquence à extraire
     * @param to fin de la séquence à extraire
     * @return id extrait du document
     */
    public static int[] extract(int[] seq, int from, int to) {
        try {
            int[] res = new int[to - from];
            for (int i = from; i < to; i++) {
                res[i - from] = seq[i];
            }
            return res;
        } catch (Exception e) {
            error("extract position from:" + from + " to:" + to);
            return new int[0];
        }
    }
}
