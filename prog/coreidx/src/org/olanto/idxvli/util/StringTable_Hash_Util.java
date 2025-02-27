/** ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * myCAT is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.idxvli.util;

/**
 * fonctions pour les hash
 */
public class StringTable_Hash_Util {

    /*
     autorise un autre valeur de hash
     on manipule le string de base
     */
    public static final int clueHash(String s) {  // ok
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            s1.append(s.charAt(i)).append(s.charAt(s.length() - i - 1)); //entrelace and inverse string
        }
        //msg("cluehash "+s+","+s1);
        return s1.hashCode();
    }

}
