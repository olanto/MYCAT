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
package org.olanto.mycat.tmx.multiun.extract;

import java.io.*;
import org.olanto.util.StringManipulation;

/**
 *
 * @author jg
 */
public class extractZHCorrection {

    static String sourceTMX, targetMFLF;
    static long totkeep, toterror;
    static OutputStreamWriter outmflf;
   static OutputStreamWriter outmflfsmallbebore;
    static StringManipulation stringManip = new StringManipulation();
   static int limit =Integer.MAX_VALUE;
    /*   
     */

    public static void main(String[] args) {
        try {
            //  procesADir("h:/PATDB/XML/docdb");
            String drive = "C:";
           sourceTMX = drive + "/CORPUS/MULTI-UN/MULTIUN.x7";
            targetMFLF = "C:" + "/CORPUS/MULTI-UN/MULTIUN.mflf.ZH";
             outmflf = new OutputStreamWriter(new FileOutputStream(targetMFLF), "UTF-8");
            Process(sourceTMX);
            outmflf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void Process(String fileName) {
   
        System.out.println("------------- entry dictionary: " + fileName);
        int totdictEntry = 0;
        try {
            InputStreamReader isrso = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            BufferedReader so = new BufferedReader(isrso);
            String wso = so.readLine();
            while (wso != null && totdictEntry < limit) {
                totdictEntry++;
                String [] entry=wso.split("\t");
                              entry[6] = stringManip.addSpace(entry[6]);
                                outmflf.append(entry[0] + "\t" + entry[1]+ "\t" + entry[2]+ "\t" + entry[3]+ "\t" + entry[4]+ "\t" + entry[5]+ "\t" + entry[6]+"\n");

                wso = so.readLine();
            }
            so.close();
            System.out.println("   read entries: " + totdictEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
 
}
