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
import static org.olanto.util.Messages.*;

/** Test de map manager, création d'un nouveau map manager.
 * 
 *
 */
public class QueryMapArchive1 {

    private static MapArchiveStructure id;
    private static Timer t1 = new Timer("global time");

    public static void main(String[] args) {

        id = new MapArchiveStructure("QUERY", new ConfigurationMapArchive());


        id.Statistic.global();

//        id.addMap(buildMap(50,23,100,33), 129, "FR");
//        id.addMap(buildMap(60,24,200,34), 229, "RU");
//        id.addMap(buildMap(500,25,200,35), 329, "DE");

        IntMap m = id.getMap(329, "EN", "DE");

        msg("from " + m.from.length + " - " + m.from[0] + " - " + m.from[m.from.length - 1]
                + " to " + m.to.length + " - " + m.to[0] + " - " + m.to[m.to.length - 1]);

        m = id.getMap(329, "EN", "EO");  // test error

        m = id.getMap(329, "EN", "RU"); // test error

        id.Statistic.contentSize();

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
