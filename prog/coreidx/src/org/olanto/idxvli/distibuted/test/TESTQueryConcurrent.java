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

package org.olanto.idxvli.distibuted.test;

import org.olanto.idxvli.*;
import org.olanto.idxvli.ql.QLManager;
import org.olanto.idxvli.ql.QL_Basic;
import org.olanto.idxvli.server.QLResultAndRank;
import org.olanto.util.Timer;
import static org.olanto.util.Messages.*;

/** tests des indexeurs distribués
 *  * 
  */
public class TESTQueryConcurrent {

    // classe chargée de stresser les query
    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");

    /**
     * application de test
     * @param args sans
     */
    public static void main(String[] args) {

        id = new IdxStructure("QUERY");
        // création de la racine de l'indexation
        id.createComponent(new ConfigurationNative1());

        // charge l'index (si il n'existe pas alors il est vide)
        id.loadIndexDoc();

        id.Statistic.global();


        Stress s = new Stress(id, "A ", 0);
        s.start();
//        Stress t = new Stress(id, "B ", 0);
//        t.start();

        //while (true) sleep(1000L);




        t1.stop();
    }
}

class Stress extends Thread {

    IdxStructure z;
    int lastword;
    int pause;
    String name;

    Stress(IdxStructure idx, String name, int pause) {
        z = idx;
        this.name = name;
        this.pause = pause;
    }

    public void run() {
        int countQuery = 0;
        int r1 = 0;
        String s1 = null;
        int r2 = 0;
        String s2 = null;
        try {
            long start = System.nanoTime();
            long resSize = 0;
            long nbquery = 1000;
            QLManager ql = new QL_Basic();
            while (true) {
                {
                    r1 = (int) (Math.random() * 30000000);
                    s1 = z.getStringforW(r1);
                    r2 = (int) (Math.random() * 30000000);
                    s2 = z.getStringforW(r2);
//                    msg (name + "  random=" +r1+", "+r2 +"  q=" +s1+", "+s2);
                    QLResultAndRank qres = ql.getMore(s1 + " AND " + s2, z);
                    if (qres == null||qres.result==null) {
                        msg("result is null:" + name + "  random=" + r1 + ", " + r2 + "  q=" + s1 + ", " + s2);
                    } else {
                        resSize += qres.result.length;
                    }
                    countQuery++;
                }
//                {
//                    String s1 = z.getStringforW((int) (Math.random() * 10000));
//                    String s2 = z.getStringforW((int) (Math.random() * z.lastUpdatedWord));
//                    resSize += z.executeRequest("NEAR(" + s1 + "," + s2 + ")").length;
//                    countQuery++;
//                }
                if (countQuery % nbquery == 0) {
                    start = System.nanoTime() - start;
                    Runtime runtime = Runtime.getRuntime();
                    msg(name + ":" + countQuery + ",  queries/sec:" + (nbquery * 1000000000 / start) + " cacheSize:" + z.indexread.getCurrentSize() / 1024 + "[Kb]" + " resSize:" + resSize +
                            " totalMemory Mb:" + runtime.totalMemory() / 1024 / 1024 + "freeMemory Mb:" + runtime.freeMemory() / 1024 / 1024);
                    start = System.nanoTime();
                    resSize = 0;
                }
                sleep((long) pause);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg(name + "  random=" + r1 + ", " + r2 + "  q=" + s1 + ", " + s2);
            error_fatal("problem in Stress");
        //System.exit(1);
        }
    }
}
    
