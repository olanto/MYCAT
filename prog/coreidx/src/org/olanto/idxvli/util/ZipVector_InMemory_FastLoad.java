/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.idxvli.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.IdxEnum.readWriteMode;
import static org.olanto.util.Messages.*;

/**
 * Comportements d'un vecteur de byte[fixedArraySize] chargé entièrement en
 * mémoire. <p>
 *
 *
 */
public class ZipVector_InMemory_FastLoad implements ZipVector {
    /* constantes d'un gestionnaire du dictionaire -------------------------------------- */

    static final boolean OBJ_COMPRESSION = true; //sinon pas de compression
    static final String CONTENT_ENCODING = "UTF-8"; //toujours des String
    static final String SOFT_VERSION = "ByteArrayVector_InMemory 2.1";
    /* variables du gestionnaire  -------------------------------------- */
    /**
     * definit la version
     */
    private String VERSION;
    /**
     * definit le path pour l'ensemble des fichiers dépendant de cet ObjectStore
     */
    private String pathName;
    /**
     * definit le path pour l'ensemble des fichiers dépendant de cet ObjectStore
     */
    private String fileName;
    private byte[][] v;
    private int[] zipSize;
    private int[] vSize;
    private int size = 0;
    private readWriteMode RW = readWriteMode.rw;

    /**
     * créer une nouvelle instance de repository pour effectuer les create, open
     */
    public ZipVector_InMemory_FastLoad() {
    }

    /**
     * crée un vecteur de taille 2^_maxSize à l'endroit indiqué par le path
     * @param _pathName
     * @param _maxSize
     * @param _fileName
     * @return 
     */
    @Override
    public final ZipVector create(String _pathName, String _fileName, int _maxSize) {
        return (new ZipVector_InMemory_FastLoad(_pathName, _fileName, _maxSize));
    }

    /**
     * ouvre un vecteur à l'endroit indiqué par le _path
     * @param _pathName
     * @param _fileName
     * @param _RW
     * @return 
     */
    @Override
    public final ZipVector open(String _pathName, String _fileName, readWriteMode _RW) {
        return (new ZipVector_InMemory_FastLoad(_pathName, _fileName, _RW));
    }

    /**
     * ferme un vecteur (et sauve les modifications
     */
    @Override
    public final void close() {
        saveMasterFile();
        msg("--- vector is closed now:" + fileName);
    }

    /**
     * créer une nouvelle instance de WordTable à partir des données existantes
     */
    private ZipVector_InMemory_FastLoad(String _pathName, String _fileName, readWriteMode _RW) {  // recharge un gestionnaire
        pathName = _pathName;
        fileName = _fileName;
        RW = _RW;
        loadMasterFile();
        //printMasterFile();
    }

    /**
     * créer une nouvelle instance de WordTable
     */
    private ZipVector_InMemory_FastLoad(String _pathName, String _fileName, int _maxSize) {
        createZipVector_InMemory(_pathName, _fileName, _maxSize);
    }

    private void createZipVector_InMemory(String _pathName, String _fileName, int _maxSize) {
        pathName = _pathName;
        fileName = _fileName;
        size = (int) Math.pow(2, _maxSize);
        VERSION = SOFT_VERSION;
        initFirstTime();
        saveMasterFile();
    }

    private void initFirstTime() { // n'utiliser que la première fois, à la création
        v = new byte[size][];
        zipSize = new int[size];
    }

    private void saveMasterFile() {  // sauver les informations persistante du gestionnaire
        if (RW == readWriteMode.rw) {
            try {
                FileOutputStream ostream = new FileOutputStream(pathName + "/" + fileName);
                ObjectOutputStream p = new ObjectOutputStream(ostream);
                p.writeObject(VERSION); // écrire les flags
                p.writeInt(size);
                p.writeObject(zipSize);
                vSize = new int[size];
                for (int i = 0; i < size; i++) {
                    if (lengthZip(i) != 0) {
                        vSize[i] = v[i].length;
                    }
                }
                p.writeObject(vSize);
                saveZipByte(v, pathName + "/" + fileName + ".byte");
                vSize = null;
                System.out.println("save Zip Vector: " + pathName + "/" + fileName);
                p.flush();
                ostream.close();
            } catch (IOException e) {
                error("IO error in ZipVector_InMemory.saveMasterFile", e);
            }
        } else {
            msg("UnSave Zip Vector: " + pathName + "/" + fileName);
        }
    }

//    void saveZipByte(byte[][] tobesave, String fileName) throws IOException {
//        try {
//            System.out.println("saveZipByte:" + fileName);
//            RandomAccessFile zipbyte;
//            zipbyte = new RandomAccessFile(fileName, "rw");
//            zipbyte.seek(0);
//            for (int i = 0; i < size; i++) {
//                if (vSize[i] != 0) {
//                    zipbyte.write(tobesave[i]);
//                }
//            }
//            zipbyte.close();
//            zipbyte = null;
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ZipVector_InMemory_FastLoad.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    void saveZipByte(byte[][] tobesave, String fileName) throws IOException {
        try {
            System.out.println("saveZipByte:" + fileName);
            RandomAccessFile zipbyte;
            zipbyte = new RandomAccessFile(fileName, "rw");
            zipbyte.seek(0);
            int nbstep = 128;
            int step = size / 128;
            for (int k = 0; k < nbstep; k++) {
                int start = k * step;
                int stepsize = 0;;
                for (int i = start; i < start + step; i++) {
                    stepsize += vSize[i];
                }
                byte[] buffer = new byte[stepsize];
                int current = 0;
                for (int i = start; i < start + step; i++) {
                    if (vSize[i] != 0) {
                        System.arraycopy(tobesave[i], 0, buffer, current, vSize[i]);
                        current += vSize[i];
                    }
                    
                }
                zipbyte.write(buffer);
            }
            zipbyte.close();
            zipbyte = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ZipVector_InMemory_FastLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMasterFile() {
        try {
            FileInputStream istream = new FileInputStream(pathName + "/" + fileName);
            ObjectInputStream p = new ObjectInputStream(istream);
            VERSION = (String) p.readObject();
            size = p.readInt();
            zipSize = (int[]) p.readObject();
            vSize = (int[]) p.readObject();
            // v = (byte[][]) p.readObject();
            v = loadZipByte(pathName + "/" + fileName + ".byte");
            vSize = null;
            System.out.println("load Zip Vector: " + pathName + "/" + fileName);
            printMasterFile();
            istream.close();
        } catch (Exception e) {
            error("IO error file ZipVector_InMemory.loadMasterFile", e);
        }
    }

//    private byte[][] loadZipByte(String fileName) throws IOException {
//        byte[][] b = new byte[size][];
//        try {
//            System.out.println("loadZipByte:" + fileName);
//            RandomAccessFile zipbyte;
//            zipbyte = new RandomAccessFile(fileName, "r");
//            zipbyte.seek(0);
//            for (int i = 0; i < size; i++) {
//                if (vSize[i] != 0) {
//                    byte[] z = new byte[vSize[i]];
//                    zipbyte.read(z);
//                    b[i] = z;
//                }
//            }
//            zipbyte.close();
//            zipbyte = null;
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ZipVector_InMemory_FastLoad.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return b;
//    }
    private byte[][] loadZipByte(String fileName) throws IOException {
        byte[][] b = new byte[size][];
        try {
            System.out.println("loadZipByte:" + fileName);
            RandomAccessFile zipbyte;
            zipbyte = new RandomAccessFile(fileName, "r");
            zipbyte.seek(0);
            int nbstep = 128;
            int step = size / 128;
            for (int k = 0; k < nbstep; k++) {
                int start = k * step;
                int stepsize = 0;;
                for (int i = start; i < start + step; i++) {
                    stepsize += vSize[i];
                }
                byte[] buffer = new byte[stepsize];
                zipbyte.read(buffer);
                int current = 0;
                for (int i = start; i < start + step; i++) {
                    if (vSize[i] != 0) {
                        byte[] z = new byte[vSize[i]];
                        System.arraycopy(buffer, current, z, 0, vSize[i]);
                        current += vSize[i];
                        b[i] = z;
                    }
                }
            }

            zipbyte.close();
            zipbyte = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ZipVector_InMemory_FastLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    private void printMasterFile() {
        msg("--- <Zip Vector parameters, Current version: " + SOFT_VERSION);
        msg("pathName: " + pathName);
        msg("fileName: " + fileName);
        msg("size: " + size);
    }

    /**
     * mets à jour la position pos avec la valeur val
     * @param pos
     * @param txt
     */
    @Override
    public final void set(int pos, String txt) {
        try {
            byte[] b = txt.getBytes(CONTENT_ENCODING);
            v[pos] = compress(b);
            zipSize[pos] = b.length;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZipVector_InMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("ZipManager.set: pos= " + pos + ", v[pos].length=" + v[pos].length + ", zipSize[pos]=" + zipSize[pos]);
    }

    /**
     * mets à  jour la position pos avec le fichier filename
     * @param pos
     * @param filename
     * @param encoding
     */
    @Override
    public void set(int pos, String filename, String encoding) {
        set(pos, BytesAndFiles.file2String(filename, encoding));
    }

    /**
     * cherche la valeur à la position pos
     * @param pos
     * @return 
     */
    @Override
    public final String get(int pos) {
        if (pos == -1) {
            return null;
        }
        if (zipSize[pos] == 0) {
            return null;
        }
        String res;
        try {
            res = new String(decompress(zipSize[pos], v[pos]), CONTENT_ENCODING);
            return res;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZipVector_InMemory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * retourne la taille du vecteur
     * @return 
     */
    @Override
    public final int length() {
        return size;
    }

    /**
     * retourne la taille du vecteur à la position pos
     * @param pos
     * @return 
     */
    @Override
    public final int lengthZip(int pos) {
        if (v[pos] != null) {
            return v[pos].length;
        }
        return 0;
    }

    /**
     * retourne la taille du vecteur décompressé à  la position pos
     * @param pos
     * @return 
     */
    @Override
    public int lengthString(int pos) {
        return zipSize[pos];
    }

    ;


    private int countNotEmpty() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (zipSize[i] != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * retourne la taille total des vecteurs stockés
     * @return 
     */
    @Override
    public long totalLengthZip() {
        long count = 0;
        for (int i = 0; i < size; i++) {
            if (v[i] != null) {
                count += v[i].length;
            }
        }
        return count;
    }

    ;
   /**  retourne la taille total des vecteurs stocké
     * @return s*/
    @Override
    public long totalLengthString() {
        long count = 0;
        for (int i = 0; i < size; i++) {
            count += zipSize[i];
        }
        return count;
    }

    ;

    
    /**
     * imprime des statistiques
     */
    @Override
    public final void printStatistic() {
        msg("Byte Array Vector statistics -> " + pathName + "/" + fileName);
        msg(" size: " + size);
        msg(" not Empty: " + countNotEmpty());
        msg("totalLengthZip: " + totalLengthZip());
        msg("totalLengthString: " + totalLengthString());
    }

    @Override
    public final void clear(int pos) {
        v[pos] = null;
        zipSize[pos] = 0;
    }

    /**
     *
     * @param realSize
     * @param bb
     * @return
     */
    public static byte[] decompress(int realSize, byte[] bb) {
        if (OBJ_COMPRESSION == true) {
            return BytesAndFiles.decompress(realSize, bb);
        } else {
            return bb;
        }
    }

    /**
     *
     * @param bb
     * @return
     */
    public static byte[] compress(byte[] bb) {
        if (OBJ_COMPRESSION == true) {
            return BytesAndFiles.compress(bb);
        } else {
            return bb;
        }
    }
}
