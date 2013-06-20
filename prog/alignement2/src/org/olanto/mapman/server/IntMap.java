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
package org.olanto.mapman.server;

import java.io.IOException;
import java.io.Serializable;

/**
 * Pour le stockages des maps
 *
 */
public class IntMap implements Serializable {

    public int[] from;
    public int[] to;

    public IntMap(int[] from, int[] to) {
        this.from = from;
        this.to = to;
    }

    /**
     * construit une map identitÃ©
     */
    public IntMap(int size) {
        from = new int[size];
        to = new int[size];
        for (int i = 0; i < from.length; i++) {
            from[i] = i + 1;
            to[i] = i + 1;
        }
    }

    /**
     * construit une map identité
     */
    public IntMap(int sizefrom, int sizeto) {
        from = new int[sizefrom];
        to = new int[sizeto];
        for (int i = 0; i < from.length; i++) {
            from[i] = ((i + 1) * sizeto) / sizefrom;
        }
        for (int i = 0; i < to.length; i++) {
            to[i] = ((i + 1) * sizefrom) / sizeto;
        }
    }

    /**
     * modify map to skip line between paragraph
     */
    public IntMap skipLine() {
        IntMap skipmap = new IntMap(
                new int[2 * from.length],
                new int[2 * to.length]);
        for (int i = 0; i < from.length; i++) {
            int newi = i * 2;
            skipmap.from[newi] = (from[i]) * 2;
            skipmap.from[newi + 1] = skipmap.from[newi] + 1;
        }
  //      skipmap.from[skipmap.from.length-] = (from[from.length) * 2; // last line

        for (int i = 0; i < to.length; i++) {
            int newi = i * 2;
            skipmap.to[newi] = (to[i]) * 2;
            skipmap.to[newi + 1] = skipmap.to[newi] + 1;
        }
  //      skipmap.to[skipmap.to.length-1] = (to[to.length-1]) * 2; // last line

        return skipmap;
    }

    /**
     * construit une map transitive
     */
    public IntMap(IntMap sopi, IntMap pita) {
        from = new int[sopi.from.length];
        to = new int[pita.to.length];
        for (int i = 0; i < from.length; i++) {
            from[i] = pita.from[sopi.from[i]];
        }
        for (int i = 0; i < to.length; i++) {
            to[i] = sopi.to[pita.to[i]];
        }
    }

    /**
     * pour échanger les map
     */
    public IntMap swap() {

        int[] tempo = from;
        from = to;
        to = tempo;
        return this;
    }

    public void dump(String s) {
        
        System.out.println(s);
        System.out.print("from:");
        for (int i = 0; i < from.length; i++) {
            System.out.print(i + " " + from[i] + "; ");
        }
        System.out.println();
        System.out.print("to:");
        for (int i = 0; i < to.length; i++) {
            System.out.print(i + " " + to[i] + "; ");
        }
        System.out.println();
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(from);
        out.writeObject(to);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        from = (int[]) in.readObject();
        to = (int[]) in.readObject();
    }
}
