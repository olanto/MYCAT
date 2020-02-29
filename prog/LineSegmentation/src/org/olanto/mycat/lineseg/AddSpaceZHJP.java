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
package org.olanto.mycat.lineseg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.olanto.senseos.SenseOS;

/**
 * test segmentation
 *
 */
public class AddSpaceZHJP {

    public static void main(String[] args) {

        ZHJP(SenseOS.getMYCAT_HOME() + "/corpus/source/wipo_test/TrainingENJP/corpus_JP.txt.txt", "UTF-8", 10000000);

    }

    public static final void ZHJP(String path, String txt_encoding, int limit) {
        StringBuffer txt = new StringBuffer("");
        int countLine = 0;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), txt_encoding);
            BufferedReader in = new BufferedReader(isr);
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + ".process"), txt_encoding));
            String w = in.readLine();
            while (w != null && countLine < limit) {
                output.append(addSpace(w) + "\n");
                countLine++;
                w = in.readLine();
            }
            in.close();
            output.close();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static String addSpace(String s) {
        StringBuilder res = new StringBuilder("");

        for (int i = 0; i < s.length(); i++) {
            res.append(s.charAt(i));
            if (isCJKChar(s.charAt(i))) {
                res.append(" ");
            }
        }
        return res.toString();
    }

    public static boolean isCJKChar(char s) {
        if (s > 0x0370) {
            return true;
        }
        return false;
    }
}
