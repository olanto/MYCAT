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

package org.olanto.idxvli.ql.test;

import org.olanto.idxvli.*;
import org.olanto.util.Timer;

/**Test de l'indexeur, mode incrémental et différentiel
 * *
 * <p>
 * 
 *<p>
 *
 *
 * 
 */
public class MajIndex {

    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");

    /**
     * application de test
     * @param args sans
     */
    public static void main(String[] args) {
        maj1();

    }

    private static void maj1() {
        id = new IdxStructure(
                //        "INCREMENTAL", // mode
                "DIFFERENTIAL", // mode
                new ConfigurationQL());
        id.indexdir("C:/OMC_corpus/BISD/S01");
        id.flushIndexDoc();
        id.Statistic.global();
        id.close();
        t1.stop();
    }
}
