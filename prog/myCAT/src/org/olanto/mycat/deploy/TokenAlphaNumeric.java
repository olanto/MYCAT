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
package org.olanto.mycat.deploy;

import org.olanto.idxvli.*;
import org.olanto.idxvli.extra.*;
import static org.olanto.idxvli.IdxConstant.*;

/**
 * Une classe pour définir les token (une définition alpha numérique).
 *
 */
public class TokenAlphaNumeric implements TokenDefinition {

    static final int EOF = -1;

    /**
     * Crée une attache
     */
    public TokenAlphaNumeric() {
    }

    /**
     * Cherche le symbole suivant. définition symbole= commence avec une lettre
     * ou des chiffres(au sens unicode) et n'est pas dans un tag html (peut
     * occasioner une erreur d'indexation dans les mauvais html) s'arrête quand
     * on rencontre autre chose qu'une lettre (au sens unicode)ou des chiffres
     * ou . ou - pour rendre active cette méthode, il faut la renommer next !
     */
    public final void next(DoParse a) {
        //         System.out.println("debug token alphanumeric:");
        int c = 0;
        char r;
        try {
            while (!(Character.isLetter((char) c) || Character.isDigit((char) c)) && (c != EOF)) {  // skip non letter
 /* tous les fichiers sont convertis en txt donc utile seulement si la source est du html
                 if ((char) c == '<') { // skip tag
                 while ((c != EOF) && ((char) c != '>')) {
                 c = a.in.read();
                 a.poschar++;
                 }
                 }
                 */
                c = a.in.read();
                a.poschar++;
            }
            a.cw.setLength(0);
            r = (char) c;
            while ((Character.isLetter(r) || Character.isDigit(r) || (char) c == '-' || (char) c == '/' || (char) c == '_' || (char) c == '-') && (c != EOF)) {  // get word
                a.cw.append(r);
                c = a.in.read();
                a.poschar++;
                r = (char) c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.EOFflag = (c == EOF);
//        System.out.println("debug token:"+a.cw.toString());
    }

    /**
     * normalise le mot. actuellement<br> - mis en minuscule<br> - tronquer à la
     * longueur fixée.<br> - lemmatiser si le stemming est actif.<br>
     *
     * @param id l'indexeur de référence
     * @param w le mot à normaliser
     * @return un mot normalisé
     */
    public final String normaliseWord(IdxStructure id, String w) {
        //System.out.print(w+"-->");
        w = w.toLowerCase();
        if (w.length() > WORD_MAXLENGTH / 2) {  // WORD_MAXLENGTH = nb de byte pour arabe,russe, chinois /2
            w = w.substring(0, WORD_MAXLENGTH / 2);
        }
        if (WORD_USE_STEMMER) {
            w = Stemmer.stemmingOfW(w);
        }
        //System.out.println(w);
        return w;
    }
}
