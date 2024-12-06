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

import static org.olanto.idxvli.IdxEnum.*;

/**
 *
 *
 *  Comportements d'un vecteur de byte[fixedArraySize].
 *
 * 
 *
 *
 */
public interface ByteArrayVector {

    /**  crÃ©e un vecteur 2^_maxSize par dÃ©faut Ã  l'endroit indiquÃ© par le path, (maximum=2^31), objet=byte[fixedArraySize]
     * @param _path
     * @param _file
     * @param _fixedArraySize
     * @param _maxSize
     * @return valeur */
    public ByteArrayVector create(String _path, String _file, int _maxSize, int _fixedArraySize);

    /**  ouvre un vecteur Ã  l'endroit indiquÃ© par le path
     * @param _path
     * @param _RW
     * @param _file
     * @return valeur */
    public ByteArrayVector open(String _path, String _file, readWriteMode _RW);

    /**  ferme un gestionnaire de vecteurs  (et sauve les modifications*/
    public void close();

    /** mets Ã  jour la position pos avec la valeur val
     * @param pos
     * @param val */
    public void set(int pos, byte[] val);

    /** Ã©limine le vecteur a la position pos
     * @param pos */
    public void clear(int pos);

    /**  cherche la valeur Ã  la position pos
     * @param pos
     * @return valeur */
    public byte[] get(int pos);

    /**  cherche la valeur Ã  la position pos, la iÃ¨me valeur
     * @param pos
     * @param i
     * @return valeur */
    public byte get(int pos, int i);

    /**  retourne la taille du vecteur
     * @return valeur */
    public int length();

    /**  retourne la taille du vecteur Ã  la position n
     * @param pos n
     * @return */
    public int length(int pos);

    /**  retourne la taille maximum des vecteurs stockÃ
     * @return ©*/
    public int maxUsedlength();

    /**  imprime des statistiques */
    public void printStatistic();
}
