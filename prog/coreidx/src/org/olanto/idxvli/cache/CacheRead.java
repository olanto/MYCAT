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

package org.olanto.idxvli.cache;

/**
 * Comportement d'un cache d'index en lecture uniquement.
 * 
 *
 *
 * Comportement d'un cache d'index en lecture uniquement.
 * Les indices des termes sont ceux utilisés par le dictionnaire 
 * (donc par forcemment ceux du cache de termes).
 *
 * Dans cette version le GC doit être organisée à  l'extérieur.
 *
 * pour l'essentiel, cette classe crée une facade de CacheIdx sur les documents et sur les positions.
 */
public interface CacheRead {

    /**
     * retourne une référence sur le cache du terme i. On travaille donc directement dans le cache!
     * @param i terme
     * @return veteur original
     */
    public int[] getReferenceOnDoc(int i);

    /**
     * retourne une copie du cache du terme i
     * @param i terme
     * @return veteur
     */
    public int[] getCopyOfDoc(int i);

    /**
     * retourne la valeur du cache du terme i à la position j
     * @param i terme
     * @param j position
     * @return valeur
     */
    public int vDoc(int i, int j);

    /**
     * retourne le vecteur des valeurs du cache du terme i de from sur la longueur donnée
     * @param i terme
     * @param from position
     * @param length longeur
     * @return vecteur des valeurs
     */
    public int[] getvDoc(int i, int from, int length);

    /**
     * détermine si le terme i est pas dans le cache
     * @param i terme
     * @return true=le terme i est pas dans le cache
     */
    public boolean isNotLoadedDoc(int i);

    /**
     * détermine si le terme i est  dans le cache
     * @param i terme
     * @return true=le terme i est  dans le cache
     */
    public boolean isLoadedDoc(int i);

    /**
     * retourne la taille du vecteur i du cache
     * @param i terme
     * @return  taille du vecteur i
     */
    public int lengthDoc(int i);

    /**
     * inscrit un nouveau terme dans le cache avec un vecteur initial
     * @param i terme
     * @param reg vecteur initial
     */
    public void registerVectorDoc(int i, int[] reg);

    /**
     * libère le terme i dans le cache
     * @param i terme
     */
    public void releaseVectorDoc(int i);

    /**
     * retourne une référence sur le cache du terme i. On travaille donc directement dans le cache!
     * @param i terme
     * @return veteur original
     */
    public int[] getReferenceOnPos(int i);

    /**
     * retourne une copie du cache du terme i
     * @param i terme
     * @return veteur
     */
    public int[] getCopyOfPos(int i);

    /**
     * retourne la valeur du cache du terme i à la position j
     * @param i terme
     * @param j position
     * @return valeur
     */
    public int vPos(int i, int j);

    /**
     * retourne le vecteur des valeurs du cache du terme i de from sur la longueur donnée
     * @param i terme
     * @param from position
     * @param length longeur
     * @return vecteur des valeurs
     */
    public int[] getvPos(int i, int from, int length);

    /**
     * détermine si le terme i est pas dans le cache
     * @param i terme
     * @return true=le terme i est pas dans le cache
     */
    public boolean isNotLoadedPos(int i);

    /**
     * détermine si le terme i est  dans le cache
     * @param i terme
     * @return true=le terme i est  dans le cache
     */
    public boolean isLoadedPos(int i);

    /**
     * retourne la taille du vecteur i du cache
     * @param i terme
     * @return  taille du vecteur i
     */
    public int lengthPos(int i);

    /**
     * inscrit un nouveau terme dans le cache avec un vecteur initial
     * @param i terme
     * @param reg vecteur initial
     */
    public void registerVectorPos(int i, int[] reg);

    /**
     * libère le terme i dans le cache
     * @param i terme
     */
    public void releaseVectorPos(int i);

    /**
     * retourne la taille total du cache
     * @return la taille total du cache
     */
    public long getCurrentSize();
}
