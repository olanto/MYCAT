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
package org.olanto.mycat.deploy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.*;
import org.olanto.senseos.SenseOS;
import org.olanto.util.Timer;

/**
 * listes tous les mots indexés
 */
public class OpenIndexWithoutServerGetVocabulary {      // is an application, not an applet !

    static IdxStructure id;
    static Timer t1 = new Timer("global time");

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        OutputStreamWriter out = null;
        try {
            String filename = "C:/MYCAT/logs/voc2term.txt";
            out = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
            id = new IdxStructure("INCREMENTAL", new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME() + "/config/IDX_fix.xml"));

            int lastw = 1200000;//id.lastUpdatedWord;
            for (int i = 0; i < lastw; i++) {
//                out.write(i + "\t" + id.getStringforW(i) + "\t" + id.getOccCorpusOfW(i) + "\n");
                out.write(id.getStringforW(i) + "\n");
            }
            t1.stop();
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OpenIndexWithoutServerGetVocabulary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(OpenIndexWithoutServerGetVocabulary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
