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

package org.olanto.mapman;

import java.io.Reader;
import java.io.StringReader;
//import org.olanto.idxvli.IdxStructure;
import static org.olanto.mapman.MapArchiveConstant.WORD_DEFINITION;
import static org.olanto.mapman.MapArchiveConstant.WORD_MINLENGTH;
import static org.olanto.mapman.MapArchiveConstant.SOF;

/**
 * Une classe pour effectuer le parsing des documents.
 * 
 *Une classe pour effectuer le parsing des documents.<br>
 *
 * On peut changer le tokenizer en modifiant l'initialisation de WORD_DEF
 * et associant le bon code.<br>
 *
 * On peut penser que chaque client a un méthode next() et normalise () et définir un type
 * associé à  un case pour sélectionner le bon next et normalise (à  faire)
 *
 */
public class DoParse {

    static final int EOF = -1;
    // attention les variables sont statiques pour accélerer mais si des accés concurrents sont possibles
    // alors il faut protéger le parsing
    private ParseSetOfWords DontIndexThis;

    /**
     *
     */
    public int nbToken = 0;
//    public IdxStructure glue;
    /** variable contenant le symbole courant*/
    public  StringBuilder cw = new StringBuilder();
    /** la source de caractère à parser */
    public  Reader in;
    /** indique si la fin du fichier est atteinte */
    public  boolean EOFflag = true;
    /** posistion dans la source de caractère */
    public  int poschar = 0;
    
    // supporte pas la concurrence

    /**
     *
     */
    public static boolean acceptedToken = true; // pour le Tokeniser

    /**
     *
     */
    public static boolean conceptFound = false; // pour le Tokeniser attention ne supporte pas la concurrence

    /**
     *
     */
    public static boolean wordFound = false; // pour le Tokeniser attention ne supporte pas la concurrence

    /**
     *
     */
    public static boolean wordExternal = false; // pour le Tokeniser attention ne supporte pas la concurrence

    /** crée un analyseur lexical. sur la source (Reader) avec la liste de mots
     * à ne pas indexer SOF. Se place sur la première occurence de la source
     * @param s chaîne de caractères à parser
     */
    public DoParse(String s) {
        //msg("doparse:"+s);
        try {

            in = new StringReader(s);
        } catch (Exception e) {
            System.err.println("IO error in SetOfWords");
        }
        poschar = 0;
        acceptedToken = false;
        EOFflag = false;
        DontIndexThis = SOF;
        WORD_DEFINITION.next(this);
    }






    /** utilise ce parseur pour récupérer une liste d'id de termes (construire la liste des mots). Les indices ne sont pris que dans l'intervalle de lecture
     * la méthode est protégée pour séquentialiser les parsing concurrent

     * @return 
     *
     */
    synchronized public final String[] tokenizeString() {  // utilisé par QL
        int MAXWORD = 1000;
        String word;
        int lastscan = 0, n;
        String[] idxW = new String[MAXWORD]; // max de terme dans une phrase

        while (!EOFflag) {
            //System.out.println(cw);
            word = WORD_DEFINITION.normaliseWord(cw.toString());
            if (word.length() >= WORD_MINLENGTH && !DontIndexThis.check(word)) {
                 //System.out.println("scan:"+word);
                if (lastscan < MAXWORD) {
                    idxW[lastscan] = word;
                    lastscan++;

                }
            }
            WORD_DEFINITION.next(this);
        }  // while

        String[] res = new String[lastscan];
        System.arraycopy(idxW, 0, res, 0, lastscan);
        return res;
    }
}
