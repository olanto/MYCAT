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

package org.olanto.idxvli.mapio;

import java.io.IOException;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * gère une structure de fichiers directement mappé en mémoire.
 * 

 *
 * 
 * gère des fichiers directement mappés en mémoire. les fichiers s'utilisent comme des fichiers
 * standard de java, seuls les i/o utilisées sont reprisent dans cette interface, si besoin elle 
 * être augmentée.
 *
 * l'exemple suivant montre une utilisation
 *
 */
//private static void testWrite(DirectIOFile file, String fileName, MappingMode mapMode)
//throws IOException {
//Timer t=new Timer ("----- "+file.getClass().getName());
//file.open(fileName, mapMode, readWriteMode.rw, 10, fileSize);
//file.seek(0);
//for (int i = 0; i < fileSize/4; i++) {
//file.writeInt(i);
//}
//msg("taille max:"+file.getMaxLength());
//file.close();
//t.stop();
//}
//private static void testRead(DirectIOFile file, String fileName, MappingMode mapMode)
//throws IOException {
//Timer t=new Timer ("----- "+file.getClass().getName());
//file.open(fileName, mapMode, readWriteMode.r, 10, fileSize);
//file.seek(0);
//for (int i=0; i<fileSize/4; i++) {
//int res=file.readInt();
//if (res!=i) error("re-read wait:"+i+" read:"+res);
//}
//file.close();
//t.stop();
//}

public interface DirectIOFile {

    /**
     * ouvrir le fichier.
     *  <p> Le fichier est posisionner sur 0, les paramètres des caches sont pris par défaut.
     * @param fileName nom du fichier
     * @param mapType (NOMAP, FULL, CACHE)
     * @param _RW (ecriture/lecture)
     * @throws java.io.IOException exections levées
     */
    public void open(String fileName, MappingMode mapType, readWriteMode _RW) throws IOException;

    /**
     * ouvrir le fichier, spécifiant les paramètres du mapping.
     *  Le fichier est posisionner sur 0
     * @param fileName nom du fichier
     * @param mapType (NOMAP, FULL, CACHE)
     * @param _RW (ecriture/lecture)
     * @param _slice2n taille des buffers=2^_slice2n
     * @param _maxLength taille du fichier en bytes
     * @throws java.io.IOException exections levées
     */
    public void open(String fileName, MappingMode mapType, readWriteMode _RW,
            int _slice2n, long _maxLength) throws IOException;

    /**
     * fermer le fichier.
     * @throws java.io.IOException exections levées
     */
    public void close() throws IOException;

    /**
     *  lire ces bytes
     * @param data vecteur à remplir de bytes lus
     * @throws java.io.IOException exections levées
     */
    public void read(byte[] data) throws IOException;

    /**
     * écrire ces bytes
     * @param data vecteur à écrire
     * @throws java.io.IOException exections levées
     */
    public void write(byte[] data) throws IOException;

    /**
     *  lire un int
     * @throws java.io.IOException exections levées
     * @return valeur lue
     */
    public int readInt() throws IOException;

    /**
     * écrire un long
     * @param data à écrire
     * @throws java.io.IOException exections levées
     */
    public void writeInt(int data) throws IOException;

    /**
     *  lire un long
     * @throws java.io.IOException exections levées
     * @return valeur lue
     */
    public long readLong() throws IOException;

    /**
     * écrire un int
     * @param data à écrire
     * @throws java.io.IOException exections levées
     */
    public void writeLong(long data) throws IOException;

    /**
     * se positioner à cette position dans le fichier
     * @param pos position (en byte!)
     * @throws java.io.IOException exections levées
     */
    public void seek(long pos) throws IOException;

    /**
     * taille maximum possible du fichier
     * @throws java.io.IOException exections levées
     * @return taille maximum du fichiers
     */
    public long getMaxLength() throws IOException;
}
