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

/**
 * Une classe pour garder les informations sur un terme
 * 
 *
 *
 */
public class Term {

    public String term; // terme
    public int start; // position du début
    public int end; // position de la fin
    public boolean begmarked=false; // hilited
   public boolean endmarked=false; // hilited
    public String anchor; // nom de l'ancre
    public String nextHlink; // destination
    public String closeHlink; // nom de l'ancre

    public Term(String term, int start, int end) {
        this.term = term;
        this.start = start;
        this.end = end;
    }

    public void show() {
        System.out.print("Term:" + term + " (" + start + "," + end + ")");
      if(begmarked) System.out.print(" " + anchor + " -> " + nextHlink );
      if(endmarked) System.out.print(" lastof " + closeHlink );
      System.out.println();
    }
}
