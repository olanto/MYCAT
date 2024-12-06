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
package org.olanto.idxvli.consistent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.IdxConstant;
import static org.olanto.util.Messages.*;

/**
 * Une classe gérant le controle de la modification de l'index et de sa
 * sauvegarde. lors d'une modification (service indexdir), une marque est posée.
 * elle est effacée lors d'un close un redemarrage avec cette marque est un
 * signe d'inconsistance.
 *
 */
public class CheckConsistency {

    String rootpath;
    String markName;
    String FN;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {  // only for test

        IdxConstant.CHECK_CONSISTENT=true;
        msg("init a new mark:");
        CheckConsistency a = new CheckConsistency("C:/MYCAT/TEMP", "NotClose");
        msg("   is consistent:" + a.isConsistent());
        msg("mark some change:");
        a.markSomeChange();
        msg("   is consistent:" + a.isConsistent());
        msg("clear mark:");
       a.clearMark();
        msg("   is consistent:" + a.isConsistent());

    }

    /**
     *
     * @param rootpath
     * @param markName
     */
    public CheckConsistency(String rootpath, String markName) { // empty constructor
        msg("init CheckConsistency:"+IdxConstant.CHECK_CONSISTENT);
        this.rootpath = rootpath;
        this.markName = markName;
        FN = rootpath + "/" + markName;
    }

    /**
     *
     * @return
     */
    public boolean isConsistent() {     
            File f = new File(FN);
            return !f.exists();
      }

    /**
     *
     */
    public void markSomeChange() {
        if (IdxConstant.CHECK_CONSISTENT) {
            File f = new File(FN);
            try {
                msg("create:"+FN);
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(CheckConsistency.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    public void clearMark() {
        if (IdxConstant.CHECK_CONSISTENT) {
            File f = new File(FN);
            if (f.exists()) {
                f.delete();
            } else {
                msg("Warning: try to clear a mark (no change recorded)");
            }
        }
    }
}
