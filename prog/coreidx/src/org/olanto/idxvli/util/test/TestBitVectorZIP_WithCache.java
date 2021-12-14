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

package org.olanto.idxvli.util.test;

import org.olanto.idxvli.util.*;
import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * 
 * 
 *
 *
 */
public class TestBitVectorZIP_WithCache {

    static BitArrayVector o;

    public static void main(String[] args) {
        implementationMode imp = implementationMode.BIG;
        String s;
        int multi=1024;
        o = (new BitArrayVector_ZIP_WithCache()).create(imp, "C:/AJETER", "test", 8, 1 * 1024 * 1024*multi);
        o = (new BitArrayVector_ZIP_WithCache()).open(imp, "C:/AJETER", "test", readWriteMode.rw);
        msg("12,123:" + o.get(12, 123));
        o.set(12, 123, true);
        msg("12,123:" + o.get(12, 123));

        o.close();
        o = (new BitArrayVector_ZIP_WithCache()).open(imp, "C:/AJETER", "test", readWriteMode.rw);
        msg("12,123:" + o.get(12, 123));
        o.set(12, 123, false);
        msg("12,123:" + o.get(12, 123));
//        Timer t1=new Timer("set eval");
//        for (int i=0;i<100;i++)
//            for (int j=0;j<1000;j++)
//                o.set(i,j,true);
//        t1.stop();
        o.printStatistic();

        for (int j = 0; j < 8 * 1024; j++) {
            o.set(100, (int) (Math.random() * 1024 * 1024*multi), true);
        }
        o.close();

        o = (new BitArrayVector_ZIP_WithCache()).open(imp, "C:/AJETER", "test", readWriteMode.rw);
        o.printStatistic();
        o.close();


    }
}
