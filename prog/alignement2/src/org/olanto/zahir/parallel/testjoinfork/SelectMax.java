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
package org.olanto.zahir.parallel.testjoinfork;

/**
 *
 * only a small test for Fork-Join
 */
public class SelectMax {

    private final int[] v;
    public final int begin;
    public final int end;
    public final int size;

    public SelectMax(int size) {
        this.size = size;
        v = new int[size];
        begin = 0;
        end = size;
        init();
    }

    public SelectMax(int[] v, int begin, int end) {
        this.v = v;
        this.begin = begin;
        this.end = end;
        size = end - begin;
    }

    public int maxOfSeqMethod() {
        int res = -1;
        for (int i = begin; i < end; i++) {
            if (res < v[i]) {
                res = v[i];
            }
        }
        return res;
    }

    public SelectMax subProblem(int subbegin, int subend) {
        return new SelectMax(v, subbegin, subend);
    }

    void init() {
        for (int i = 0; i < size; i++) {
            v[i] = (int) (Math.random() * 1000000000.0);
        }
    }
}
