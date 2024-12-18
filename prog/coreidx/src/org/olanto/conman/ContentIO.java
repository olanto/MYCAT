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

package org.olanto.conman;

import java.io.*;
import org.olanto.conman.objsto.*;
import org.olanto.idxvli.doc.*;
import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.util.BytesAndFiles.*;
import static org.olanto.conman.ContentConstant.*;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * Une classe gérant le controle des chargements et sauvegarde.
 *
 * 
 *
 */
class ContentIO {

    ObjectStorage4[] objsto;
    static ContentStructure glue;

    ContentIO(ContentStructure id) { // empty constructor
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
    protected final int storeSizeOfContent(int j) { // taille stockée du contenu
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

    protected final void loadContentManager() {
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
                            OBJ_ROOT[i], DOC_MAXBIT + 0 - OBJ_PW2, OBJ_SMALL_SIZE);  // +0 car seulement idx -OBJ_PW2 car 2^n obsto
                    objsto0 = null; // fuite mémoire
                }
                MODE_IDX = IdxMode.INCREMENTAL; // maintenant on peut passer dans ce mode
            } else {
                error_fatal("mode must be :IdxMode.NEW");
            }
            // create docs
            glue.docstable = (new Documents1()).create(DOC_IMPLEMENTATION, DOC_LANGUAGE, DOC_COLLECTION,
                    DOC_ROOT, DOC_NAME, DOC_MAXBIT, DOC_SIZE_NAME);
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
        glue.docstable = (new Documents1()).open(DOC_IMPLEMENTATION, DOC_LANGUAGE, DOC_COLLECTION,
                MODE_IDX, DOC_ROOT, DOC_NAME);
        glue.lastdoc = glue.docstable.getCount();

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
            glue.docstable.close();
            glue.docstable = null;  // pour éviter les fuites mémoires

            for (int i = 0; i < OBJ_NB; i++) {
                msg("close objsto " + i);
                objsto[i].close();
            }
            msg("before gc:" + usedMemory());
            System.gc(); // fait le ménage avant s'attaquer la phase 2
            msg("after gc:" + usedMemory());
            closeLogger();


        } catch (IOException e) {
            System.err.println("IO error in saveindexdoc");
        }
    }
}
