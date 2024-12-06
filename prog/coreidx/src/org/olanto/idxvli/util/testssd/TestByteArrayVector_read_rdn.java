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

package org.olanto.idxvli.util.testssd;

import org.olanto.idxvli.util.ByteArrayVector;
import org.olanto.util.Timer;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * 
 * 
 *
 *
 * @author xtern
 */
public class TestByteArrayVector_read_rdn {

    static ByteArrayVector o;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String volume = "f:/ajeter";
        int size = 124;
        int nb = 27;
        int init = 10000;
        byte[] val = new byte[size];
        val[0] = 13;
        val[1] = 23;
        val[2] = 33;
        Timer t0 = new Timer("open struct");
        o = (new ByteArrayVector_OnDisk()).open(volume, "test", readWriteMode.rw);
        t0.stop();
        //showVector(val);

        for (int j = 17; j < 28; j++) {
            t0 = new Timer("init struct span 2^" + j);
            double span = Math.pow(2, j);
            for (int i = 0; i < init; i++) {
                int rnd = (int) (Math.random() * span);
                val = o.get(rnd);
            }
            t0.stop();
        }
        o.close();

    }
}
