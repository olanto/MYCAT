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

package org.olanto.mapman.test;

import org.olanto.util.Timer;
import org.olanto.mapman.MapArchiveStructure;
import org.olanto.mapman.server.IntMap;

/** Test de map manager, création d'un nouveau map manager
 * 
 *
 *
 */
public class UpdateMapArchive {

    private static MapArchiveStructure id;
    private static Timer t1 = new Timer("global time");

    public static void main(String[] args) {

        id = new MapArchiveStructure("INCREMENTAL", new ConfigurationMapArchive());


        id.Statistic.global();

        id.addMap(buildMap(50, 23, 100, 33), 129, "EN", "FR");
        id.addMap(buildMap(60, 24, 200, 34), 229, "EN", "RU");
        id.addMap(buildMap(500, 25, 200, 35), 329, "EN", "DE");



        id.close();
        t1.stop();

    }

    static IntMap buildMap(int s1, int v1, int s2, int v2) {
        return new IntMap(vectconst(s1, v1), vectconst(s2, v2));
    }

    static int[] vectconst(int size, int value) {
        int[] v = new int[size];
        for (int i = 0; i < size; i++) {
            v[i] = value;
        }
        return v;
    }
}
