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
package org.olanto.mysqd.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.mysqd.server.MySelfQuoteDetection;
import org.olanto.mysqd.util.Utils;

/**
 *
 * @author simple
 */
public class TestFromFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        String FN = "C:/MYCAT/test/bug/Final_Act_FR.html";
//        test(FN, 2, 6, "C:/MYCAT/test/bug/Final_Act_FR.doc.res.html");
        String FN = "C:/MYCAT/test/bug/HR_Declaration_RU.html";
        test(FN, 2, 6, "C:/MYCAT/test/bug/HR_Declaration_RU.html.res.html");
//        String FN = "C:/MYCAT/test/G245-UTF8.html";
//        test(FN, 2, 6, "C:/MYCAT/test/G245-UTF8.res.html");
//              String FN = "C:/MYCAT/test/test simple.html";
//        test(FN, 2, 3, "C:/MYCAT/test/test simple.res.html");
 
    }

    public static void test(String fileName, int minFreq, int minLength, String resultName) {
        MySelfQuoteDetection mysqd = new MySelfQuoteDetection(fileName, minFreq, minLength);
        try {
            FileOutputStream out = new FileOutputStream(resultName);
            out.write(mysqd.getHTML().getBytes());
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(TestFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
