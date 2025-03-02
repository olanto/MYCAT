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
import static org.olanto.idxvli.util.BytesAndFiles.*;

/**
 * gestionaire de mots géré sur disque.
 * <p>
 * 
 *
 */
public class StringTable_HomeHash_OnDisk implements StringRepository {

    /* constantes d'un gestionnaire du dictionaire -------------------------------------- */
    static final String SOFT_VERSION = "StringTable_HomeHash_OnDisk 2.1";
    static final int minSize = 10;  // 2^n; taille des blocs d'initialisation
    static final String ENCODE = "UTF-8";   // encodage utilisé
    /* variables d'un gestionnaire du dictionaire -------------------------------------- */
    /** definit la version */
    String VERSION;
    /** definit le nom générique des fichiers */
    String GENERIC_NAME;
    /** definit le path pour l'ensemble des fichiers dépendant de ce Dictionnaire */
    String pathName;
    /** definit le fichier */
    String idxName;
    private int lengthString = 128;  // longueur fixe occupée par un String
    private int maxSize = 12; //  2^n;
    private int comp32 = 32 - maxSize;
    private int utilSize = (int) Math.pow(2, maxSize) - 1;
    private long collision = 0;
    /** nbr de mots actuellement dans le dictionnaire */
    private int count = 0;
    /** fichier associé avec les documents */
    private RandomAccessFile rdoc;
    private RandomAccessFile hdoc;

    /**
     * default constructor
     */
    public StringTable_HomeHash_OnDisk() {
    }

    /**  crée une word table de taille 2^_maxsize par défaut à l'endroit indiqué par le path
     * @param _pathName
     * @param _idxName
     * @param _maxSize
     * @param _lengthString
     * @return valeur */
    public final StringRepository create(String _pathName, String _idxName, int _maxSize, int _lengthString) {
        return (new StringTable_HomeHash_OnDisk(_pathName, _idxName, "ext", _maxSize, _lengthString));
    }

    /**  ouvre un gestionnaire de mots  à l'endroit indiqué par le _path
     * @param _path
     * @param _idxName
     * @return valeur */
    public final StringRepository open(String _path, String _idxName) {
        return (new StringTable_HomeHash_OnDisk(_path, _idxName));
    }

    /** créer une nouvelle instance de StringTable*/
    private StringTable_HomeHash_OnDisk(String _pathName, String _idxName, String _generic_name, int _maxSize, int _lengthString) {
        createStringTable_HomeHash_OnDisk(_pathName, _idxName, _generic_name, _maxSize, _lengthString);
    }

    /** créer une nouvelle instance de StringTable à partir des données existantes*/
    private StringTable_HomeHash_OnDisk(String _pathName, String _idxName) {  // recharge un gestionnaire
        pathName = _pathName;
        idxName = _idxName;
        loadMasterFile();
        //printMasterFile();
    }

    private final void createStringTable_HomeHash_OnDisk(
            String _pathName, String _idxName, String _generic_name,
            int _maxSize, int _lengthString) {
        pathName = _pathName;
        idxName = _idxName;
        GENERIC_NAME = _generic_name;
        VERSION = SOFT_VERSION;
        maxSize = _maxSize;
        comp32 = 32 - maxSize;
        lengthString = _lengthString;
        utilSize = (int) Math.pow(2, maxSize) - 1;
        try {
            rdoc = new RandomAccessFile(pathName + "/" + idxName + ".rnddoc", "rw");
            hdoc = new RandomAccessFile(pathName + "/" + idxName + ".hshdoc", "rw");
            initFirstTime();
            saveMasterFile();
        } catch (Exception e) {
            error("IO error in createStringTable_HomeHash_OnDisk()", e);
        }

    }

    private final void initFirstTime() {
        try {
            int[] initblock = new int[(int) Math.pow(2, minSize)];
            for (int i = 0; i < (int) Math.pow(2, minSize); i++) {
                initblock[i] = -1;
            }
            byte[] b = new byte[(int) Math.pow(2, minSize) * 4];
            intTobyte(initblock, initblock.length * 4, b);

            hdoc.seek(0);
            for (int i = 0; i < (int) Math.pow(2, maxSize - minSize); i++) {
                hdoc.write(b);
            }
            // marque la fin
            rdoc.seek((long) utilSize * (long) lengthString);
            rdoc.writeInt(-1);
        } catch (Exception e) {
            error("IO error in initFirstTime()", e);
        }
    }

    private final void saveMasterFile() {  // sauver les informations persistante du gestionnaire
        try {
            FileOutputStream ostream = new FileOutputStream(pathName + "/" + idxName);
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(VERSION); // écrire les flags
            p.writeObject(GENERIC_NAME);
            p.writeInt(maxSize);
            p.writeInt(lengthString);
            p.writeInt(comp32);
            p.writeInt(count);
            p.writeInt(utilSize);
            p.writeLong(collision);
            msg("save Master String Table File for: " + pathName + "/" + idxName + " id: " + GENERIC_NAME);
            p.flush();
            ostream.close();
            rdoc.close();
            hdoc.close();
        } catch (IOException e) {
            error("IO error in StringTable.saveMasterFile", e);
        }
    }

    private final void loadMasterFile() {
        try {
            FileInputStream istream = new FileInputStream(pathName + "/" + idxName);
            ObjectInputStream p = new ObjectInputStream(istream);
            VERSION = (String) p.readObject();
            GENERIC_NAME = (String) p.readObject();
            maxSize = p.readInt();
            lengthString = p.readInt();
            comp32 = p.readInt();
            count = p.readInt();
            utilSize = p.readInt();
            collision = p.readLong();
            msg("load Master String Table File for: " + pathName + "/" + idxName + " id: " + GENERIC_NAME);
            istream.close();
        } catch (Exception e) {
            error("IO error file StringTable.loadMasterFile");
        }
        try {
            rdoc = new RandomAccessFile(pathName + "/" + idxName + ".rnddoc", "rw");
            hdoc = new RandomAccessFile(pathName + "/" + idxName + ".hshdoc", "rw");
        } catch (Exception e) {
            error("IO error in RND WordTable()", e);
        }

    }

    private void printMasterFile() {
        msg("--- String Table parameters, Current version: " + SOFT_VERSION);
        msg("pathName: " + pathName);
        msg("idxName: " + idxName);
        msg("GENERIC_NAME: " + GENERIC_NAME);
        msg("maxSize: " + maxSize);
        msg("lengthString: " + lengthString);
        msg("comp32: " + comp32);
        msg("count: " + count);
        msg("utilSize: " + utilSize);
        msg("collision: " + collision);
    }

    /**  ferme un gestionnaire de mots  (et sauve les modifications*/
    public final void close() {
        saveMasterFile();
        msg("--- StringTable is closed now ");
    }

    /**  ajoute un terme au gestionnaire retourne le numéro du terme, retourne EMPTY s'il y a une erreur,
     * retourne son id s'il existe déja
     * @param w
     * @return 
     */
    public final int put(String w) {
        //  msg(w);
        int iget = get(w);
        if (iget != EMPTY) {
            return iget;
        }  // existe déjà

        if (count >= utilSize) { // on doit garder un trou pour le get()
            error("*** error StringTable is full");
            return EMPTY;
        }
        int h = hash(w);
        int indirecth = readInt(h * 4, hdoc);
        //msg(w+" h init:"+h);
        while (indirecth != -1) { // cherche seulement un trou
            h = ((h + 1) << comp32) >>> comp32;
            indirecth = readInt(h * 4, hdoc);
            collision++;  // est pas tout à fait juste si plusieurs occurences similaires
        }
        // déjà dans la table ou on a trouvé un trou
        //msg(w+" h final:"+h);
        writeInt(count, h * 4, hdoc);
        writeString(w, (long) count * (long) lengthString, ENCODE, lengthString, rdoc);
        count++;
        return count - 1;
    }

    public final int get(String w) {
        int h = hash(w);
        int indirecth = readInt(h * 4, hdoc);
        //msg(w+" h init:"+h);
        while (indirecth != -1 && !w.equals(readString((long) indirecth * (long) lengthString, ENCODE, rdoc))) { // cherche un trou
            h = ((h + 1) << comp32) >>> comp32;
            indirecth = readInt(h * 4, hdoc);
        }
        if (indirecth == -1) {
            return EMPTY;
        } // pas trouvé
        else {
            return indirecth;
        }
    }

    public final String get(int i) {
        if (i < 0 || i > count) {
            return NOTINTHIS;
        }
        return readString((long) i * (long) lengthString, ENCODE, rdoc);
    }

    private final int hash(String s) {  // ok
        return (s.hashCode() << comp32) >>> comp32;
    }

    /**  imprime des statistiques */
    public final void printStatistic() {
        msg(getStatistic());
    }

    /**  imprime des statistiques
     * @return valeur */
    public final String getStatistic() {
        return "String Table statistics :" + pathName + "/" + idxName + " id: " + GENERIC_NAME
                + "\n  Current version: " + SOFT_VERSION
                + "\n  utilSize: " + utilSize + " count: " + count + " collision: " + collision;
    }

    /**  retourne le nbr de mots dans le dictionnaire
     * @return valeur */
    public final int getCount() {
        return count;
    }

    public final void modify(int i, String newValue) {
        writeString(newValue, (long) i * (long) lengthString, ENCODE, lengthString, rdoc);
    }
}
