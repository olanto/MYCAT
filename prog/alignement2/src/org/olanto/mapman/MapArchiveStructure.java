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

import org.olanto.idxvli.util.*;
import static org.olanto.util.Messages.*;
import static org.olanto.mapman.MapArchiveConstant.*;
import org.olanto.idxvli.*;
import org.olanto.mapman.server.IntMap;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * Une classe pour gérer les cartes d'alignement dans les bi-textes.
 *
 * 
 */
public class MapArchiveStructure {

    /** dictionnaire de maps (document->indice ligne) (indice colonne->paire de langue)*/
    public IntArrayVector mapid;
    /** la structure comprend un module de statistique*/
    public MapArchiveStatistic Statistic;
    /** la structure comprend un module de gestion des IO*/
    public MapArchiveIO IO;
    /** la structure comprend un module de gestion des IO*/
    public MapArchiveManager MAM;
    /** indice du dernier map utilisé (on doit incrémenter avant d'utiliser un nouvel id, 0= pas de map*/
    public int lastmap = 0;

    /** Crée une structure d'archivage
     */
    public MapArchiveStructure() {
    }

    /** Crée une structure d'archivage et initialise la.
     * @param _mode (NEW,QUERY,INCREMENTAL,DIFFERENTIAL,)
     * @param client la configuration client
     */
    public MapArchiveStructure(String _mode, MapArchiveInit client) {
        MODE_IDX = IdxMode.valueOf(_mode);
        createComponent(client);
        loadMapArchive();
    }

    /** initialise la structure */
    public final void loadMapArchive() {
        IO.loadMapArchive();
    }

    /** sauvegarder les données encore dans les caches et ferme l'archive
     */
    public final void close() {
        IO.saveContentManager();
    }

    /** ajoute une map.
     * @param map map
     * @param docid la référence du document
     * @param lang lanque (pivot/cette langue)
     */
    synchronized public void addMap(IntMap map, int docid, String langfrom, String langto) {
        if (langfrom.equals(LANGID[0])) {
            MAM.addMap(map, docid, langto);
        } else {
            msg("not implemented for non pivot language");
        }
    }

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    synchronized public IntMap getMap(int docid, String langfrom, String langto) {
        if (langfrom.equals(LANGID[0])) {  // la langue source est la langue pivot
            return MAM.getMap(docid, langto);
        } else if (langto.equals(LANGID[0])) { // la langue cible est la langue pivot
            IntMap map = MAM.getMap(docid, langfrom);
            if (map != null) {
                map.swap();
            }// renverse la map
            return map;
        } else { // la langue cible et la langue source ne sont pas pivot
            // msg("implemented by transitivity");
            IntMap sopi = getMap(docid, langfrom, LANGID[0]);
            IntMap pita = getMap(docid, LANGID[0], langto);
            if (sopi != null && pita != null) { // les deux cartes existent
                return new IntMap(sopi, pita);
            }

        }
        return null;
    }

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    synchronized public boolean existMap(int docid, String langfrom, String langto) {
        if (langfrom.equals(LANGID[0])) {
            return MAM.existMap(docid, langto);
        } else {
            msg("not implemented for non pivot language");
        }
        return false;
    }

    /**
     * crée un composant d'archivage avec une racine pour les fichiers d'archive
     */
    public void createComponent(MapArchiveInit client) {
        // initialise les constantes et la configuration
        client.InitPermanent();
        client.InitConfiguration();
        MapArchiveConstant.openLogger();
        MapArchiveConstant.show();

        // create reference
        Statistic = new MapArchiveStatistic(this);
        IO = new MapArchiveIO(this);
        MAM = new MapArchiveManager(this);

        if (MODE_IDX == IdxMode.NEW || MODE_IDX == IdxMode.INCREMENTAL || MODE_IDX == IdxMode.DIFFERENTIAL) {
            InitUpdateMode();
        }
        if (MODE_IDX == IdxMode.QUERY) {
            InitReadOnly();
        }
    }

    void InitReadOnly() {
        msg("init mode:" + MODE_IDX.name());
    }

    void InitUpdateMode() {
        msg("docdmax:" + DOC_MAX);
        msg("init mode:" + MODE_IDX.name());

    }
}
