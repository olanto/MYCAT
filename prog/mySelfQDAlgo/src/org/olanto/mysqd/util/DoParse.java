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

import java.io.Reader;
import java.io.StringReader;
import static org.olanto.mysqd.util.Utils.*;

/**
 * Une classe pour effectuer le parsing des documents.
 * 
 *Une classe pour effectuer le parsing des documents.<br>
 *
 * On peut changer le tokenizer en modifiant l'initialisation de WORD_DEF
 * et associant le bon code.<br>
 *
 *
 */
public class DoParse {

    static final int EOF = -1;
    int nbToken = 0;
    /** variable contenant le symbole courant*/
    public StringBuilder cw = new StringBuilder();
    /** la source de caractère à parser */
    public SimReader in;
    public boolean[] ischar;
    /** indique si la fin du fichier est atteinte */
    public boolean EOFflag = true;
    /** posistion dans la source de caractàre */
    public int poschar = 0;
    public int begToken = 0;
    public int endToken = 0;
    public boolean acceptedToken = true; // pour le Tokeniser
    TokenDefinition WORD_DEFINITION = new TokenAlphaNumeric();

    /** Crée un analyseur lexical. sur la source (Reader) 
     * Se place sur la première occurence de la source
     * @param s chaîne de caractères à parser
     */
    public DoParse(String s) {
            in = new SimReader(s);
            ischar= removeEntities(s);
        poschar = 0;
        acceptedToken = false;
        EOFflag = false;
        WORD_DEFINITION.next(this);
    }

    /** utilise ce parseur pour récupérer une liste des termes 
     * (construire la liste des mots). Les indices ne sont pris que dans l'intervalle de lecture
     */
    public final Term[] tokenizeString() {
        int MAXWORD = 1000000;
        String word;
        int lastscan = 0;
        Term[] idxW = new Term[MAXWORD]; // max de terme dans un document
        
        while (!EOFflag) {
            //System.out.println(cw);
            word = WORD_DEFINITION.normaliseWord(cw.toString());

            //System.out.println("scan:"+word);
            if (!word.equals("nbsp")){  // pour contourner un probleme de conversion en utf8
            if (lastscan < MAXWORD) {
                idxW[lastscan]= new Term(word,begToken,endToken);
                lastscan++;
            }
            }

            WORD_DEFINITION.next(this);
        }  

        Term[] res = new Term[lastscan];
        System.arraycopy(idxW, 0, res, 0, lastscan);
        return res;
    }
}
