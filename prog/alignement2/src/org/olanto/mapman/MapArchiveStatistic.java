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

import static org.olanto.util.Messages.*;
import static org.olanto.mapman.MapArchiveConstant.*;

/**
 * Une classe pour collecter des informations statistiques sur l'indexeur.
 * <p>
 * <p>
 * 
 *<p>
 */
public class MapArchiveStatistic {

    MapArchiveStructure glue;

    MapArchiveStatistic(MapArchiveStructure id) {
        glue = id;
    }

    /** Affiche dans la console des statistiques sur le serveur de map.
     */
    public void global() {
        msg("MAP ARCHIVE STATISTICS global:");
        msg("Map for mapmax: " + DOC_MAX + ", Map now: " + (glue.lastmap) + ", used: " + ((glue.lastmap * 100) / DOC_MAX) + "%");
    }

    public String getGlobal() {
        return "KMap: " + DOC_MAX / 1024 + "/" + (glue.lastmap / 1024) + " " + ((glue.lastmap * 100) / DOC_MAX) + "%";
    }

    public void mapid() {
        glue.mapid.printStatistic();
    }

    public void content() {
        glue.IO.printContentStatistic();
    }

    public void contentSize() {
        msg("STATISTICS map content size:");
        msg("mapNow: " + glue.lastmap);
        long real = 0;
        long stored = 0;
        for (int i = 0; i < glue.lastmap; i++) {
            real += glue.IO.realSizeOfContent(i);
            stored += glue.IO.storeSizeOfContent(i);
        }

        msg("realSize: " + (real / MEGA)
                + " [Mb], storedSize: " + (stored / MEGA)
                + " Compression: " + (stored * 100) / (real + 1) + "%");
    }
}
