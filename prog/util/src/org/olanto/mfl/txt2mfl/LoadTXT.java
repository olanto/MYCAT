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

package org.olanto.mfl.txt2mfl;

import java.io.*;

/** génération de mfl.
 * 
 */
public class LoadTXT {

    static BufferedReader in;

    LoadTXT(String fname) {
        try {
            in = new BufferedReader(new FileReader(fname));
        } catch (Exception e) {
            System.err.println("IO error during open txt file");
        }
    }

    public String getNext() {
        StringBuffer txt = new StringBuffer("");
        try {
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }
            return txt.toString();
        } catch (Exception e) {
            System.err.println("IO error during read TXT file");
        }
        return txt.toString();
    }
}
