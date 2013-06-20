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
package org.olanto.zahir.align.comparable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.zahir.align.SimInformation;

/* 
 * @author Jacques Guyot 2011
 * @version 1.1
 * modify by
 *//**
 * Classe stockant la carte des positions entre deux traductions.
 */
public class CollectAndSave {


    public String savefile;
    static OutputStreamWriter out;
    private int countline = 0;
    private int countfile = 0;

    public CollectAndSave(String savefile) {
 
        this.savefile = savefile;
    
    }

 
    public synchronized void  save(SimInformation score, String from, String to) {
        try {
            if (countline % 100000 == 0) {
                if (countfile != 0) {
                    out.flush();
                    out.close();
                }
                countfile++;
                countline = 0;
                System.out.println("file:"+countfile);
                out = new OutputStreamWriter(new FileOutputStream(savefile + "_" + countfile + ".txt"), "UTF-8");
        }
        countline++;
        //System.out.println("countline:"+countline);
        out.write(score.similarity + "\t"+
                score.countIdent + "\t" +
               score.countNoIdent + "\t" +
               score.sourceCnt + "\t" +
               score.targetCnt + "\t" +
                from + "\t" + to + "\n");
        } catch (Exception ex) {
                Logger.getLogger(CollectAndSave.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    public void reset(){
        close();
       countline = 0;
       countfile = 0;
    }
    
    public void close(){
        if (out!=null){
        try {
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(CollectAndSave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }

}


