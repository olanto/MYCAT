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

package org.olanto.mfl.tag2mfl;

import java.io.*;

/** génération de mfl.
 * 
 */
public class MFLFile {

    static FileWriter out;

    MFLFile(String filename) {
        try {
            out = new FileWriter(filename);
        } catch (Exception e) {
            System.err.println("ERROR IN init file:" + filename);
        }
    }

    public void writeInDir(String filename, String news) {
        try {
            out.write("\n#####" + filename + "#####\n");
            out.write(news);
        } catch (Exception e) {
            System.err.println("ERROR IN writeInDir:" + filename);
        }
    }

    public void writeInDir(String filenameAndCat) {
        try {
            out.write(filenameAndCat + "\n");
        } catch (Exception e) {
            System.err.println("ERROR IN writeInDir:" + filenameAndCat);
        }
    }

    public void close() {
        try {
            out.write("\n#####ENDOFFILE#####\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("ERROR IN close file:");
        }
    }
}
