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

import org.olanto.conman.objsto.ObjectStorage4;
import org.olanto.conman.objsto.ObjectStore4;
import org.olanto.idxvli.IdxEnum.IdxMode;
import org.olanto.idxvli.IdxEnum.readWriteMode;
import org.olanto.idxvli.util.IntArrayVector_InMemory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.olanto.mapman.MapArchiveConstant.*;
import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.util.BytesAndFiles.*;

/**
 * Une classe gérant le controle des chargements et sauvegarde.
 *
 * 
 * - modification JG: réduction de la taille du objectstore (32x trop grand!
 */
class MapArchiveIO {

    ObjectStorage4[] objsto;
    static MapArchiveStructure glue;

    MapArchiveIO(MapArchiveStructure id) { // empty constructor
        glue = id;
    }

    /** taille réel du contenu
     * @param j ième contenu
     * @return taille
     */
    protected final int realSizeOfContent(int j) { // taille du contenu
        return objsto[j % OBJ_NB].realSize(j / OBJ_NB);  // lit depuis objstore ,
    }

    /** taille stockée du contenu
     * @param j ième contenu
     * @return taille
     */
    protected final int storeSizeOfContent(int j) { // taille stock�e du contenu
        return objsto[j % OBJ_NB].storedSize(j / OBJ_NB);  // lit depuis objstore ,
    }

    protected final void printContentStatistic() { // statistique des object store
        for (int i = 0; i < OBJ_NB; i++) {
            objsto[i].printStatistic();
        }
    }

    protected final byte[] loadContent(int j) {
        byte[] res = objsto[j % OBJ_NB].read(j / OBJ_NB);  // lit le conteneur
        return res;
    }

    protected final byte[] loadContent(int j, int from, int to) {
        byte[] res = objsto[j % OBJ_NB].read(j / OBJ_NB, from, to);  // lit le conteneur
        return res;
    }

    protected void saveContent(int j, byte[] content, int realSize) {
        objsto[j % OBJ_NB].write(content, j / OBJ_NB, realSize);
    }

    protected final void loadMapArchive() {
        msg("DOC_MAXBIT+"+DOC_MAXBIT);
        try {
            FileInputStream istream = new FileInputStream(COMMON_ROOT + "/" + currentf);
            ObjectInputStream p = new ObjectInputStream(istream);
            System.err.println("start loading");
            istream.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("IO warning file IDX is not present:" + COMMON_ROOT + "/" + currentf);

            // create idx
            if (MODE_IDX == IdxMode.NEW) {
                for (int i = 0; i < OBJ_NB; i++) {
                    //ObjectStorage4 objsto0=(new ObjectStore4_Async()).create(OBJ_IMPLEMENTATION,
                    ObjectStorage4 objsto0 = (new ObjectStore4()).create(OBJ_IMPLEMENTATION,
                            OBJ_ROOT[i], DOC_MAXBIT - OBJ_PW2, OBJ_SMALL_SIZE);  //  car la langue pivot ne produit pas plus de langue que le nbr de documents
                    objsto0 = null; // fuite m�moire
                }
                MODE_IDX = IdxMode.INCREMENTAL; // maintenant on peut passer dans ce mode
            } else {
                error_fatal("mode must be :IdxMode.NEW");
            }
            // create docs
            glue.mapid = (new IntArrayVector_InMemory()).create(MAP_ROOT, MAP_NAME, DOC_MAXBIT, LANGPAIR_MAX);
        }
        // restore content
        objsto = new ObjectStorage4[OBJ_NB];
        if (MODE_IDX == IdxMode.QUERY) {
            for (int i = 0; i < OBJ_NB; i++) {
                objsto[i] = (new ObjectStore4()).open(OBJ_IMPLEMENTATION, OBJ_ROOT[i], readWriteMode.r);
            }
        }
        if (MODE_IDX == IdxMode.INCREMENTAL || MODE_IDX == IdxMode.DIFFERENTIAL) {
            for (int i = 0; i < OBJ_NB; i++) //objsto[i]=(new ObjectStore4_Async()).open(OBJ_IMPLEMENTATION,OBJ_ROOT[i],readWriteMode.rw);
            {
                objsto[i] = (new ObjectStore4()).open(OBJ_IMPLEMENTATION, OBJ_ROOT[i], readWriteMode.rw);
            }
        }
        // restore docs
        glue.mapid = (new IntArrayVector_InMemory()).open(MAP_ROOT, MAP_NAME);
        glue.lastmap = glue.mapid.get(DOC_MAX - 1, LANGPAIR_MAX - 1); // on stocke le dernier object store attribué dans la dernière valeur des mapid (ne peut pas être utilisée)

    }

    protected final void saveContentManager() {
        try {
            FileOutputStream ostream = new FileOutputStream(COMMON_ROOT + "/" + currentf);
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            System.err.println("end of idx save");
            p.flush();
            ostream.close();
            ostream = null;

            // fermer la gestion des doc
            glue.mapid.set(DOC_MAX - 1, LANGPAIR_MAX - 1, glue.lastmap);
            glue.mapid.close();
            glue.mapid = null;  // pour �viter les fuites m�moires

            for (int i = 0; i < OBJ_NB; i++) {
                msg("close objsto " + i);
                objsto[i].close();
            }
            msg("before gc:" + usedMemory());
            System.gc(); // fait le m�nage avant s'attaquer la phase 2
            msg("after gc:" + usedMemory());
            closeLogger();


        } catch (IOException e) {
            System.err.println("IO error in saveindexdoc");
        }
    }
}
