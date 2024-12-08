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

package org.olanto.idxvli.knn;

import org.olanto.idxvli.*;

/**
 * Une interface pour effectuer le calcul de la distance entre les documents. 
 * 

 *
 */
public interface KNNManager {

    /**
     * Prépare une structure de calcul de KNN.
     * @param _verbose pour le debuging (true)
     * @param _glue indexation de référence
     * @param minocc minimum d'occurences pour être dans la présélection.
     * @param maxlevel maximum d'occurences  en 0/00 du corpus pour être dans a présélection.
     * @param formulaIDF inverse document frequency formula.
     * @param formulaTF terme frequency formula.
     * @param offset valeur to boost the topN.
     */
    public void initialize(IdxStructure _glue, int minocc, int maxlevel, boolean _verbose, int formulaIDF, int formulaTF, float offset);

    /** Chercher les N premiers voisins du document d, sans formattage.
     * @param doc document
     * @param N nombre de voisins
     * @return réponse
     */
    public int[][] getKNNForDoc(int doc, int N);

    /**
     *
     * @param doc
     * @param N
     * @return
     */
    public KNNResult KNNForDoc(int doc, int N);

    /** Chercher les N premiers voisins du texte request, sans formattage.
     * @param request texte de référence
     * @param N nombre de voisins
     * @return réponse
     */
    public KNNResult getKNN1(String request, int N);

    /** Chercher les N premiers voisins du texte request, sans formattage, avec une liste prédéfinie de document.
     * @param topic liste de doc
     * @param request texte de référence
     * @param N nombre de voisins
     * @return réponse
     */
    public KNNResult getKNNinTopic(int[] topic, String request, int N);

    /**
     *
     * @param request
     * @return
     */
    public float[] getRawKNN(String request);

    /**
     *
     * @param request
     * @param N
     * @return
     */
    public int[][] getKNN(String request, int N);

    /** Chercher la pondération des documents
     * @param request texte de référence
     * @return réponse
     */
    public float[] getSimilarity(String request);

    /** visualiser le résultat d'une réponse knn
     * @param res Résultat d'une requête KNN (getKNN)
     */
    public void showKNN(int[][] res);

    /**
     *
     * @param res
     */
    public void showKNNWithName(int[][] res);
    
    /**
     *
     * @param res
     */
    public void showKNNWithContent(int[][] res);

    /** Chercher les N premiers voisins du texte request
     * @param request texte de référence
     * @param N nombre de voisins
     * @return XML format
     */
    public String searchforKNN(String request, int N);

    /** formatage XML d'une ligne de réponse
     * @param fn nom du fichier
     * @param doc document
     * @param score niveau de similarité
     * @return XML format
     */
    public String XMLrefFNWithScore(String fn, int doc, int score);
}
