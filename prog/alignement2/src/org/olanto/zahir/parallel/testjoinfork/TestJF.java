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

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * only a small test for Fork-Join
 */
public class TestJF extends RecursiveAction {

    static final int MAX = 200_000_000;
    static int SMALL = 1;
    SelectMax m;
    int result;

    public static void main(String[] args) {
        SelectMax m = new SelectMax(MAX);
        compare(m, 2, 4);
        compare(m, 2, 8);
        compare(m, 2, 16);
        compare(m, 2, 32);
        compare(m, 2, 1024);
        compare(m, 4, 4);
        compare(m, 4, 8);
        compare(m, 4, 16);
        compare(m, 4, 32);
    }

    public TestJF(SelectMax m) {
        this.m = m;
    }

    public TestJF(SelectMax m, int SMALL) {
        this.m = m;
        this.SMALL = SMALL;
    }

    protected void compute() {
        if (m.size < SMALL) {
            result = m.maxOfSeqMethod();
        } else {
            int middle = m.begin + m.size / 2;
            TestJF left = new TestJF(m.subProblem(m.begin, middle));
            TestJF right = new TestJF(m.subProblem(middle + 1, m.end));
            invokeAll(left, right);
            result = Math.max(left.result, right.result);

        }
    }

    public static void compare(SelectMax m, int nbThread, int smallRatio) {
        double tFJ = (double) FJMethod(m, nbThread, smallRatio);
        double tSEQ = (double) SEQMethod(m);
        System.out.println("ratio:" + tSEQ / tFJ + " nbThread:" + nbThread + " smallRatio:" + smallRatio);

    }

    public static long FJMethod(SelectMax m, int nbThread, int smallRatio) {
        TestJF init = new TestJF(m, m.size / smallRatio);
        long start = System.nanoTime();
        ForkJoinPool fjPool = new ForkJoinPool(nbThread);
        fjPool.invoke(init);
        long duration = System.nanoTime() - start;
        //   System.out.println("FJ max:"+init.result+ " duration:"+duration/1000000+" [ms]");
        return duration;
    }

    public static long SEQMethod(SelectMax m) {
        long start = System.nanoTime();
        int max = m.maxOfSeqMethod();
        long duration = System.nanoTime() - start;
        System.out.println("max:" + max + " duration:" + duration / 1000000 + " [ms]");
        return duration;
    }
}
