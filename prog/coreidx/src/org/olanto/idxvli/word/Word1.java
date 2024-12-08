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

package org.olanto.idxvli.word;

import org.olanto.idxvli.util.*;
import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.IdxEnum.*;
import java.util.concurrent.locks.*;

/**
 *  gestionnaire de dictionnaire.
 *
 * 
 *
 * <p>
 * *  <pre>
 *  concurrence:
 *   - // pour les lecteurs
 *   - écrivain en exclusion avec tous
 *  doit être le point d'accés pour toutes les structures utilisées !
 *  </pre>
 *   */
public class Word1 implements WordManager {

    private StringRepository dictionnary;
    private readWriteMode RW = readWriteMode.rw;

    /** créer une nouvelle instance de repository pour effectuer les create, open*/
    public Word1() {
    }

    /**  crée un gestionnaire de documents (la taille et la longueur) à l'endroit indiqué par le path
     * @param _ManagerImplementation
     * @param _path
     * @param _idxName
     * @param _lengthString
     * @param _maxSize */
    public final WordManager create(implementationMode _ManagerImplementation,
            String _path, String _idxName, int _maxSize, int _lengthString) {
        return (new Word1(_ManagerImplementation,
                _path, _idxName, _maxSize, _lengthString));
    }

    /**  ouvre un gestionnaire de documents  à l'endroit indiqué par le _path
     * @param _ManagerImplementation
     * @param _RW
     * @param _idxName
     * @param _path */
    public final WordManager open(implementationMode _ManagerImplementation,
            readWriteMode _RW, String _path, String _idxName) {
        return (new Word1(_ManagerImplementation,
                _RW, _path, _idxName));
    }

    /**  ferme un gestionnaire de mots  (et sauve les modifications*/
    public final void close() {
        dictionnary.close();
        msg("--- WordManager is closed now ");
    }

    /** créer une nouvelle instance de WordManager à partir des données existantes*/
    private Word1(implementationMode _ManagerImplementation,
            readWriteMode _RW, String _pathName, String _idxName) {  // recharge un gestionnaire
        RW = _RW;
        switch (_ManagerImplementation) {
            case FAST:
                dictionnary = (new StringTable_HomeHash_InMemory()).open(_pathName, _idxName + "_dict");
                break;
            case BIG:
                dictionnary = (new StringTable_OnDisk_WithCache_MapIO()).open(_pathName, _idxName + "_dict");
                break;
            case XL:
                dictionnary = (new StringTable_OnDisk_WithCache_MapIO_XL()).open(_pathName, _idxName + "_dict");
                break;
            case XXL:
                dictionnary = (new StringTable_OnDisk_WithCache_XXL()).open(_pathName, _idxName + "_dict");
                break;
        }
    }

    /** créer une nouvelle instance de Word Manager*/
    private Word1(implementationMode _ManagerImplementation,
            String _pathName, String _idxName, int _maxSize, int _lengthString) {
        switch (_ManagerImplementation) {
            case FAST:
                dictionnary = (new StringTable_HomeHash_InMemory()).create(_pathName, _idxName + "_dict", _maxSize, -1);
                break;
            case BIG:
                dictionnary = (new StringTable_OnDisk_WithCache_MapIO()).create(_pathName, _idxName + "_dict", _maxSize, _lengthString);
                break;
            case XL:
                dictionnary = (new StringTable_OnDisk_WithCache_MapIO_XL()).create(_pathName, _idxName + "_dict", _maxSize, _lengthString);
                break;
            case XXL:
                dictionnary = (new StringTable_OnDisk_WithCache_XXL()).create(_pathName, _idxName + "_dict", _maxSize, _lengthString);
                break;
        }
    }

    /**  imprime des statistiques */
    public final void printStatistic() {
        msg("------------------------------------------------------------");
        msg("- WORDS TABLE STAT                                         -");
        msg("------------------------------------------------------------");
        msg("DICTIONNARY:");
        dictionnary.printStatistic();
        msg("");
    }
    /** opération sur documentName verrous ------------------------------------------*/
    private final ReentrantReadWriteLock dictionnaryRW = new ReentrantReadWriteLock();
    private final Lock dictionnaryR = dictionnaryRW.readLock();
    private final Lock dictionnaryW = dictionnaryRW.writeLock();

    /**  ajoute un document au gestionnaire retourne le numéro du document
     * @param d document name*/
    public final int put(String d) {
        dictionnaryW.lock();
        try {
            //msg("add this:"+d);
            int id = dictionnary.put(d);
            return id;
        } finally {
            dictionnaryW.unlock();
        }
    }

    /**  cherche le numéro du document, retourne EMPTY s'il n'est pas dans le dictionnaire
     * @param d */
    public final int get(String d) {
        dictionnaryR.lock();
        try {
            return dictionnary.get(d);
        } finally {
            dictionnaryR.unlock();
        }
    }

    /**  cherche le document associé à un numéro, retourne NOTINTHIS s'il n'est pas dans le dictionnaire*/
    public final String get(int i) {
        dictionnaryR.lock();
        try {
            return dictionnary.get(i);
        } finally {
            dictionnaryR.unlock();
        }
    }

    /**  retourne le nbr de mots dans le dictionnaire */
    public final int getCount() {
        dictionnaryR.lock();
        try {
            return dictionnary.getCount();
        } finally {
            dictionnaryR.unlock();
        }
    }
}
