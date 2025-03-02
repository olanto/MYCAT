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

import java.io.*;
import static org.olanto.util.Messages.*;

/**
 *  Comportements d'un vecteur de int[fixedArraySize] chargé en mémoire.
 *
 * 
 *
 */
public class IntArrayVector_InMemory implements IntArrayVector {
    /* constantes d'un gestionnaire du dictionaire -------------------------------------- */

    static final String SOFT_VERSION = "IntArrayVector_InMemory 2.1";
    /* variables du gestionnaire  -------------------------------------- */
    /** definit la version */
    private String VERSION;
    /** definit le path pour l'ensemble des fichiers dépendant de cet ObjectStore */
    private String pathName;
    /** definit le path pour l'ensemble des fichiers dépendant de cet ObjectStore */
    private String fileName;
    private int[][] v;
    private int size = 0;
    private int fixedArraySize;

    /** créer une nouvelle instance de repository pour effectuer les create, open*/
    public IntArrayVector_InMemory() {
    }

    /**  crée un vecteur de taille 2^_maxSize à l'endroit indiqué par le path
     * @param _pathName
     * @param _maxSize
     * @param _fileName
     * @param _fixedArraySize
     * @return valeur */
    public final IntArrayVector create(String _pathName, String _fileName, int _maxSize, int _fixedArraySize) {
        return (new IntArrayVector_InMemory(_pathName, _fileName, _maxSize, _fixedArraySize));
    }

    /**  ouvre un vecteur  à l'endroit indiqué par le _path
     * @param _pathName
     * @param _fileName
     * @return valeur */
    public final IntArrayVector open(String _pathName, String _fileName) {
        return (new IntArrayVector_InMemory(_pathName, _fileName));
    }

    /**  ferme un vecteur  (et sauve les modifications*/
    public final void close() {
        saveMasterFile();
        msg("--- vector is closed now:" + fileName);
    }

    /** créer une nouvelle instance de WordTable à partir des données existantes*/
    private IntArrayVector_InMemory(String _pathName, String _fileName) {  // recharge un gestionnaire
        pathName = _pathName;
        fileName = _fileName;
        loadMasterFile();
        //printMasterFile();
    }

    /** créer une nouvelle instance de WordTable*/
    private IntArrayVector_InMemory(String _pathName, String _fileName, int _maxSize, int _fixedArraySize) {
        createIntVector_InMemory(_pathName, _fileName, _maxSize, _fixedArraySize);
    }

    private final void createIntVector_InMemory(String _pathName, String _fileName, int _maxSize, int _fixedArraySize) {
        pathName = _pathName;
        fileName = _fileName;
        fixedArraySize = _fixedArraySize;
        size = (int) Math.pow(2, _maxSize);
        VERSION = SOFT_VERSION;
        initFirstTime();
        saveMasterFile();
    }

    private final void initFirstTime() { // n'utiliser que la première fois, à la création
        v = new int[size][];
    }

    private final void saveMasterFile() {  // sauver les informations persistante du gestionnaire
        try {
            FileOutputStream ostream = new FileOutputStream(pathName + "/" + fileName);
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(VERSION); // écrire les flags
            p.writeInt(size);
            p.writeInt(fixedArraySize);
            p.writeObject(v);
            System.out.println("save Int Vector: " + pathName + "/" + fileName);
            p.flush();
            ostream.close();
        } catch (IOException e) {
            error("IO error in IntVector_InMemory.saveMasterFile", e);
        }
    }

    private final void loadMasterFile() {
        try {
            FileInputStream istream = new FileInputStream(pathName + "/" + fileName);
            ObjectInputStream p = new ObjectInputStream(istream);
            VERSION = (String) p.readObject();
            size = p.readInt();
            fixedArraySize = p.readInt();
            v = (int[][]) p.readObject();
            System.out.println("load Long Vector: " + pathName + "/" + fileName);
            istream.close();
        } catch (Exception e) {
            error("IO error file IntVector_InMemory.loadMasterFile", e);
        }
    }

    private final void printMasterFile() {
        msg("--- Int Array Vector parameters, Current version: " + SOFT_VERSION);
        msg("pathName: " + pathName);
        msg("fileName: " + fileName);
        msg("size: " + size);
        msg("fixedArraySize: " + fixedArraySize);
    }

    /** mets à jour la position pos avec la valeur val
     * @param pos
     * @param val */
    public final void set(int pos, int[] val) {
        if (val.length <= fixedArraySize) {
            v[pos] = val;
        } else {
            error("int array is too big length:" + val.length + " limit:" + fixedArraySize);
        }
    }

    /** mets à jour la position pos avec la valeur val
     * @param pos
     * @param i
     * @param val */
    public void set(int pos, int i, int val) {
        if (i < fixedArraySize) {
            if (v[pos] == null) { // pas encore alloués
                v[pos] = new int[fixedArraySize];
            }
            v[pos][i] = val;
        } else {
            error("int array is too big length:" + i + " limit:" + fixedArraySize);
        }

    }

    /**  cherche la valeur à la position pos
     * @param pos
     * @return valeur */
    public final int[] get(int pos) {
        return v[pos];
    }

    /**  cherche la valeur à la position pos, la ième valeur
     * @param pos
     * @param i
     * @return valeur */
    public final int get(int pos, int i) {
        if (v[pos] == null) { // pas encore alloués
            return 0;
        }
        return v[pos][i];
    }

    /**  retourne la taille du vecteur
     * @return valeur */
    public final int length() {
        return size;
    }

    /**  imprime des statistiques */
    public final void printStatistic() {
        msg("IntArrayVector_InMemory" + pathName + "/" + fileName + "statistics -> ");
        msg(" size: " + size);
        msg("fixedArraySize: " + fixedArraySize);
    }

    public final void clear(int pos) {
        v[pos] = null;
    }
}
