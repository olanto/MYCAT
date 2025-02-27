/**********
    Copyright © 2010-2025 Olanto Foundation Geneva

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

package org.olanto.idxvli.word.test;

import org.olanto.idxvli.IdxEnum.implementationMode;
import org.olanto.idxvli.IdxEnum.readWriteMode;
import org.olanto.idxvli.word.Word1;
import org.olanto.idxvli.word.WordManager;
import static org.olanto.util.Messages.msg;
import org.olanto.util.Timer;

/* générique test pour les différentes implémenations remplacer le nom de l'implémentation .... */


/**
<p>author: Jacques Guyot
 */
public class word1_CreateInsert {
static WordManager    is;
    public static void main(String[] args) {



                msg("create");
                is = (new Word1()).create(implementationMode.BIG, "C:/test/dictio", "test", 24, 48);
                is.printStatistic();
                is.close();
                
                msg("open");
                is= (new Word1()).open(implementationMode.BIG, readWriteMode.rw, "C:/test/dictio", "test");
                Timer t0 = new Timer("global put");
                Timer t1 = new Timer("put 0");
                int max = 8000000;
                for (int i = 1; i < max+1; i++) {
                    if (i % 100000 == 0) {
                        t1.stop();
                        t1 = new Timer("put:" + (i / 1000 + 1));
                    }
//                    int res=is.get(""+i);
//                    if (res>0) msg("collision "+i);
                    is.put("x"+i);
                }
                t0.stop();
                is.printStatistic();
                is.close();




    }
   
}
