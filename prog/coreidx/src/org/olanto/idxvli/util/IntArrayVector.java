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

package org.olanto.idxvli.util;

/**
 *  Comportements d'un vecteur de int[fixedArraySize].
 *
 * 
 *
 *
 *  Comportements d'un vecteur de int[fixedArraySize].
 *
 */
public interface IntArrayVector {

    /**  crÃ©e un vecteur 2^_maxSize par dÃ©faut Ã  l'endroit indiquÃ© par le path, (maximum=2^31), objet=int[fixedArraySize]
     * @param _path
     * @param _file
     * @param _maxSize
     * @param _fixedArraySize
     * @return valeur */
    public IntArrayVector create(String _path, String _file, int _maxSize, int _fixedArraySize);

    /**  ouvre un vecteur Ã  l'endroit indiquÃ© par le path
     * @param _path
     * @param _file
     * @return valeur */
    public IntArrayVector open(String _path, String _file);

    /**  ferme un gestionnaire de vecteurs  (et sauve les modifications*/
    public void close();

    /** mets Ã  jour la position pos avec la valeur val
     * @param pos
     * @param val */
    public void set(int pos, int[] val);

    /** mets Ã  jour la position pos avec la valeur val
     * @param pos
     * @param val
     * @param i */
    public void set(int pos, int i, int val);

    /** Ã©limine le vecteur a la position pos
     * @param pos */
    public void clear(int pos);

    /**  cherche la valeur Ã  la position pos
     * @param pos
     * @return valeur */
    public int[] get(int pos);

    /**  cherche la valeur Ã  la position pos, la iÃ¨me valeur
     * @param pos
     * @param i
     * @return valeur */
    public int get(int pos, int i);

    /**  retourne la taille du vecteur
     * @return valeur */
    public int length();

    /**  imprime des statistiques */
    public void printStatistic();
}
