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

/**
 * Une classe pour définir les token (une définition alpha numérique).
 *
 */
public class TokenAlphaNumeric implements TokenDefinition {

    static final int EOF = -1;

    /** Cr�e une attache
     */
    public TokenAlphaNumeric() {
    }

    /**
     * Cherche le symbole suivant.
     * définition symbole= commence avec une lettre ou des chiffres(au sens unicode)
     * et n'est pas dans un tag html (peut occasioner une erreur d'indexation dans les mauvais html)
     * s'arr�te quand on rencontre autre chose qu'une lettre (au sens unicode)ou des chiffres ou . ou -
     * pour rendre active cette m�thode, il faut la renommer next !
     */
    public final void next(DoParse a) {
        int c = 0;
        char r;
        try {
            while (!((Character.isLetter((char) c) && a.ischar[a.poschar] )
                    || (Character.isDigit((char) c) && a.ischar[a.poschar] )                 
                     || ((char) c == '&' && a.ischar[a.poschar] )                 
                    )
                    && c != EOF
                      // supprime les entités                    
                ) {  // skip non letter
                if ((char) c == '<') { // skip tag
                    while ((c != EOF) && ((char) c != '>')) {
                        c = a.in.read();
                        a.poschar++;
                    }
                }
              c = a.in.read();
                a.poschar++;
            }
            a.begToken = a.poschar;
            a.cw.setLength(0);
            r = (char) c;
            boolean predIsANumber=false;
            boolean predIsEntity=false;
            while ((Character.isLetter(r) 
                    || Character.isDigit(r)
                    || (char) c == '-'
                    || (char) c == '/'
                    || (char) c == '_'
                    || (char) c == '&'
                    || (char) c == '#'
                    || ((char) c == ';'  && predIsEntity)   // fin des entités
                    || ((char) c == ',' && predIsANumber)
                    || ((char) c == '.' && predIsANumber)
                    ) && (c != EOF) && a.ischar[a.poschar] ) {  // get word
                a.cw.append(r);
                if ((char) c == '&')predIsEntity=true;
                if ((char) c == ';')predIsEntity=false;
                predIsANumber=Character.isDigit(r);
                c = a.in.read();
                a.poschar++;
                r = (char) c;
            // System.out.println("next "+r);
            }
            a.endToken = a.poschar;
            if ((char) c == '<') { // skip tag if last char is <
                while ((c != EOF) && ((char) c != '>')) {
                    c = a.in.read();
                    a.poschar++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.EOFflag = (c == EOF);
    }

    /** normalise le mot. actuellement<br>
     * - mis en minuscule<br>
     * - tronquer � la longueur fix�e.<br>
     * - lemmatiser si le stemming est actif.<br>
     * @param id l'indexeur de r�f�rence
     * @param w le mot � normaliser
     * @return un mot normalis�
     */
    public final String normaliseWord(String w) {
        //System.out.print(w+"-->");
        w = w.toLowerCase();
        //System.out.println(w);
        return w;
    }
}
