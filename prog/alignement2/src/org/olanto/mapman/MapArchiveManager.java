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

package org.olanto.mapman;

import org.olanto.idxvli.util.BytesAndFiles;
import org.olanto.mapman.server.IntMap;
import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.mapman.MapArchiveConstant.*;
import static org.olanto.util.Messages.*;

/**
 * Une classe pour implémenter les fonctions du map serveur.
 * 
 * 
 */
public class MapArchiveManager {

    MapArchiveStructure glue;

    MapArchiveManager(MapArchiveStructure id) {
        glue = id;
    }

    /** ajoute une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    protected void addMap(IntMap map, int docid, String lang) {

        int langid = getLangID(lang);
        if (langid > 0) {  // pas la langue pivot, ni une langue inconnue
            glue.lastmap++; // attribue une nouvelle carte
            int sfrom = map.from.length;
            int sto = map.to.length;
            int[] compact = new int[sfrom + sto + 2]; // + 2 pour copier à la fin des longueurs de from et to
            for (int i = 0; i < sfrom; i++) { // copie from
                compact[i] = map.from[i];
            }
            for (int i = 0; i < sto; i++) { // copie to
                compact[i + sfrom] = map.to[i];
            }
            compact[sfrom + sto] = sfrom;
            compact[sfrom + sto + 1] = sto;
            int sb = compact.length * 4; // convertir en byte
            byte[] b = new byte[sb];
            BytesAndFiles.intTobyte(compact, sb, b);

            glue.IO.saveContent(glue.lastmap, compress(b), sb); // sauver la map dans le objstore

            glue.mapid.set(docid, langid, glue.lastmap);

        } else {
            msg("MapArchiveManager --> try to add a map for pivot language or unknown :"
                    + lang);
        }

    }

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    public boolean existMap(int docid, String lang) {
        int langid = getLangID(lang);
        if (langid > 0) {  // pas la langue pivot, ni une langue inconnue
            int idobj = glue.mapid.get(docid, langid);  // id de la carte
            if (idobj > 0) {  // exist une map
                byte[] b = decompress(glue.IO.realSizeOfContent(idobj), glue.IO.loadContent(idobj)); // récupère la map dans le objstore
                return true;
            } else {
//                msg("MapArchiveManager --> no map was store for this doc :" + docid+ " and this language " + lang);
            }
        } else {
//            msg("MapArchiveManager --> try to get a map for pivot language or unknown :"+ lang);
        }

        return false;
    }

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    synchronized public IntMap getMap(int docid, String lang) {

        int langid = getLangID(lang);
//        System.out.println("getMap for:"+docid+" lang:"+lang+" langid:"+langid);
        if (langid > 0) {  // pas la langue pivot, ni une langue inconnue
            int idobj = glue.mapid.get(docid, langid);  // id de la carte
            if (idobj > 0) {  //  exist une map
                byte[] b = decompress(glue.IO.realSizeOfContent(idobj), glue.IO.loadContent(idobj)); // récupère la map dans le objstore
                int sb = b.length;
                int[] compact = new int[sb / 4];
                BytesAndFiles.byteToint(compact, sb, b);

                int sfrom = compact[compact.length - 2];
                int sto = compact[compact.length - 1];

                int[] from = new int[sfrom];
                int[] to = new int[sto];

                for (int i = 0; i < sfrom; i++) { // copie from
                    from[i] = compact[i];
                }
                for (int i = 0; i < sto; i++) { // copie to
                    to[i] = compact[i + sfrom];
                }
                return new IntMap(from, to);
            } else {
                msg("MapArchiveManager --> no map was store for this doc :" + docid
                        + " and this language " + lang);
            }
        } else {
            msg("MapArchiveManager --> try to get a map for pivot language or unknown :"
                    + lang);
        }

        return null;

    }

    public int getLangID(String lang) {
        for (int i = 0; i
                < LANGPAIR_MAX; i++) {
            if (LANGID[i].equals(lang)) {
                return i;


            }
        }
        return -1;


    }

    public static final byte[] decompress(int realSize, byte[] bb) {
        if (MAP_COMPRESSION == Compression.YES) {
            return BytesAndFiles.decompress(realSize, bb);


        } else {
            return bb;


        }
    }

    public static final byte[] compress(byte[] bb) {
        if (MAP_COMPRESSION == Compression.YES) {
            return BytesAndFiles.compress(bb);


        } else {
            return bb;

        }
    }
}
