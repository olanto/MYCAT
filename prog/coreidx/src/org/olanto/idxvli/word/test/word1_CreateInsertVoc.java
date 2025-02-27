/**********
    Copyright © 2010-2025 Olanto Foundation Geneva

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

package org.olanto.idxvli.word.test;

/* générique test pour les différentes implémenations remplacer le nom de l'implémentation .... */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.IdxEnum.implementationMode;
import org.olanto.idxvli.IdxEnum.readWriteMode;
import org.olanto.idxvli.word.Word1;
import org.olanto.idxvli.word.WordManager;
import static org.olanto.util.Messages.msg;
import org.olanto.util.Timer;

/**
 *
 * <p>
 * <b>JG-2008
 * <p>
 * author: Jacques Guyot
 * <p>
 * copyright Jacques Guyot 2008
 * <p>
 * l'utilisation de cette classe est strictement limitée aux ayant droit
 * <p>
 * l'utilisation de cette classe nécessite une autorisation explicite de son
 * auteur JG-2008</b>
 * <p>
 *
 *
 */
public class word1_CreateInsertVoc {

    static WordManager is;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        InputStreamReader isr = null;
        try {
            msg("create");
            is = (new Word1()).create(implementationMode.BIG, "C:/test/dictio", "test", 22, 50);
            is.printStatistic();
            is.close();
            msg("open");
            is = (new Word1()).open(implementationMode.BIG, readWriteMode.rw, "C:/test/dictio", "test");
            Timer t0 = new Timer("global put");
            Timer t1 = new Timer("put 0");
            isr = new InputStreamReader(new FileInputStream("C:/test/dictio/voc2term.txt"), "UTF-8");
            BufferedReader invoc = new BufferedReader(isr);
            String w = invoc.readLine();
            int count = 0;
            int collision = 0;
            int maxcoll=12;
            int[] vectcol=new int[maxcoll];
            while (w != null) {
//            if (count % 100 == 0) {
//                t1.stop();
//                t1 = new Timer("put:" + (count / 1000 + 1));
//            }
                int res = is.get(w);
                if (res > 0) {
                    //msg(count + " collision " + w+" len= "+w.length()+ " id rec="+res+" content rec="+is.get(res));
                    vectcol[w.length()]++;
                    collision++;
                }
                is.put(w);
                count++;
                w = invoc.readLine();
            }
            for (int i=1;i<maxcoll;i++)msg(i+"\t"+vectcol[i]);
            t0.stop();
            msg("totcollision= "+collision);
            is.printStatistic();
            is.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(word1_CreateInsertVoc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isr.close();
            } catch (IOException ex) {
                Logger.getLogger(word1_CreateInsertVoc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
