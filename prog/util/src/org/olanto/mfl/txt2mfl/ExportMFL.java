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
class ExportMFL {

    static FileWriter out;

    public static void init(String filename) {

        try {
            out = new FileWriter(filename + ".mfl");
        } catch (Exception e) {
            System.err.println("IO error open mfl");
            e.printStackTrace();
        }
        System.out.println("open " + filename);
    }

    public static void close() {

        try {
            out.write("#####ENDOFFILE#####\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("IO error close mfl");
            e.printStackTrace();
        }
    }

    public synchronized static void addFile(String filename, String s) {
        try {
            out.write("#####" + filename + "#####\n");
            out.write(s);
            out.write("\n");
        } catch (Exception e) {
            System.err.println("IO error addFile");
            e.printStackTrace();
        }

    }
}
