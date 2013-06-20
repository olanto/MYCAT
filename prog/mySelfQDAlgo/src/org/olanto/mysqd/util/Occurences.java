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
package org.olanto.mysqd.util;

import java.util.HashMap;
import java.util.Vector;

/**
 * Une classe pour garder les occurences des ngram d'un document
 * 
 *
 *
 */
public class Occurences {

    public Vector<Integer> o;

    public Occurences(int first) {
        o = new Vector<Integer>();
        o.add(first);
    }

    public void show() {
        System.out.print("  occ:"+o.size()+" ->");
        for (int i=0;i<o.size();i++){
          System.out.print(" "+o.get(i));
        }
        System.out.println();
    }

    public void add(int next) {
        o.add(next);
    }

    public int size() {
        return o.size();
    }
}
