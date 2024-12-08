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
 *  Comportements d'un vecteur de Long.
 *
 * 
 *
 *  Comportements d'un vecteur de Long.
 *
 */
public interface LongVector {

    /**  crée un vecteur 2^_maxSize par défaut à  l'endroit indiqué par le path, (maximum=2^31)
     * @param _path
     * @param _file
     * @param _maxSize
     * @return valeur */
    public LongVector create(String _path, String _file, int _maxSize);

    /**  ouvre un vecteur à  l'endroit indiqué par le path
     * @param _path
     * @param _file
     * @return valeur */
    public LongVector open(String _path, String _file);

    /**  ferme un gestionnaire de vecteurs  (et sauve les modifications*/
    public void close();

    /** mets à  jour la position pos avec la valeur val
     * @param pos
     * @param val */
    public void set(int pos, long val);

    /**  cherche la valeur à  la position pos
     * @param pos
     * @return valeur */
    public long get(int pos);

    /**  retourne la taille du vecteur
     * @return valeur */
    public int length();

    /**  imprime des statistiques */
    public void printStatistic();

    /**  retourne des statistiques
     * @return valeur */
    public String getStatistic();
}
