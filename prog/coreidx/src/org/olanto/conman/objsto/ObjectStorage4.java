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

package org.olanto.conman.objsto;

import static org.olanto.idxvli.IdxEnum.*;

/**  Comportements d'un gestionaire d'objets.
 *
 * version 2.2
 * seul append et read sur les int sont conservés
 *
 * 
 *
 */
public interface ObjectStorage4 {

    /**  crée un ObjectStorage de taille 2^maxSize à l'endroit indiqué par le path,
     *  ObjectStore ne prend en considération que des objets de la minBigSize  exprimé (2^n) en byte,
     *  Exemple create("ici",20,32) crée 2^20 id avec un stockage minimum de 32 bytes au premier niveau soit 8 entiers,
     *  les objets plus petits (16) sont stocké dans une structure simplifiée
     *
     *  modification realSize et StoredSize  //21-11-2005
     * @param implementation
     * @param path
     * @param maxSize
     * @param minBigSize
     * @return 
     */
    public ObjectStorage4 create(implementationMode implementation, String path, int maxSize, int minBigSize);

    /**  ouvre un ObjectStorage  à l'endroit indiqué par le path
     * @param implementation
     * @param path
     * @param _RW
     * @return valeur */
    public ObjectStorage4 open(implementationMode implementation, String path, readWriteMode _RW);

    /**  ferme un ObjectStorage  (et sauve les modifications*/
    public void close();

    /**  écrit des bytes a cet objet, si 0 = OK , l'identifiant est imposé de l'exterieur pour le premier
    realLength indique la longeur réel de l'objet sans compression, l'objet doit déjà être compressé si l'on veut le compresser
     * @param b
     * @param user
     * @param realLength
     * @return */
    public int write(byte[] b, int user, int realLength);

    /**  retourne l'objet stocké completement,si null = erreu
     * @param user
     * @return r*/
    public byte[] read(int user);

    /**  retourne l'objet stocké partiellement de from .. to,si null = erreur
     * @param user
     * @param from
     * @param to
     * @return */
    public byte[] read(int user, int from, int to);

    /**  retourne la taille stockée de l'obje
     * @param user
     * @return t*/
    public int storedSize(int user);

    /**  retourne la taille réel de l'objet sans compressio
     * @param user
     * @return n*/
    public int realSize(int user);

    /**  libére un id (ceux vus par les utilisateurs
     * @param user)*/
    public void releaseId(int user);

    /**  imprime les statistiques */
    public void printStatistic();

    /**  imprime les statistiques */
    public void resetStatistic();

    /**  imprime les info sur un id (USER)
     * @param user */
    public void printNiceId(int user);

    /**  utilisation en byte de l'espace disque (sans l'overhead)
     * @return valeur */
    public long getSpace();
}
