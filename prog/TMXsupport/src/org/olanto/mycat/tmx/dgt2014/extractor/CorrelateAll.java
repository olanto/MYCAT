/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.mycat.tmx.dgt2014.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.olanto.mycat.tmx.common.ItemsCorrelation;
import org.olanto.mysqd.util.Ref;

/**
 * Classe pour la mise à jour des maps.
 *
 */
public class CorrelateAll {

    public List<Ref> ref;
    public String termso;
    public String langso;
    public String langta;
    public List<ItemsCorrelation> list;

    public CorrelateAll(List<Ref> _ref, String _termso, String _langso, String _langta) {
        ref = _ref;
        termso = _termso;
        langso = _langso;
        langta = _langta;
        list = new ArrayList<>();
    }

    public synchronized void addResult(ItemsCorrelation res){
        list.add(res);
    }
    
    public List<ItemsCorrelation> computePAR() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(16);  // nb proc could be put into a parameters
            for (int i = 0; i < ref.size(); i++) { // pour chaque n-gram
                CorrelateProcess mp = new CorrelateProcess(this, i);
                    executor.execute(mp);
            }
            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();
            // Wait until all threads are finish
            while (!executor.isTerminated()) {
            }
            System.out.println("Finished all threads");
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
return list;
    }
}
