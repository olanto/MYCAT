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

package org.olanto.idxvli.extra;

/**
 * Classe stockant les  bornes des positions d'un vecteur.
 * 
 *
 */
public class FromTo {

    /* la position dï¿½but*/

    /**
     *
     */

    public int from;
    /* la position fin */

    /**
     *
     */

    public int to;

    /** crï¿½e un intervalle
     * @param from debut
     * @param to fin
     */
    public FromTo(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
