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

package org.olanto.mapman.test;

import org.olanto.senseos.SenseOS;
import org.olanto.mapman.MapArchiveInit;
import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.mapman.MapArchiveConstant.*;

/**
 * Une classe pour initialiser les constantes.
 * 
 * Une classe pour initialiser les constantes. Cette classe doit Ãªtre implÃ©mentÃ©e pour chaque application
 */
public class ConfigurationMapArchive implements MapArchiveInit {

    /** crÃ©e l'attache de cette classe.
     */
    public ConfigurationMapArchive() {
    }

    /** initialisation permanante des constantes.
     * Ces constantes choisies définitivement pour toute la durée de la vie de l'index.
     */
    public void InitPermanent() {

        COMLOG_FILE = SenseOS.getMYCAT_HOME()+"/data/MAP_ARCH/common.log";

        DETLOG_FILE = SenseOS.getMYCAT_HOME()+"/data/MAP_ARCH/detail.log";


        DOC_MAXBIT = 16;
        DOC_MAX = (int) Math.pow(2, DOC_MAXBIT);  // recalcule
        LANGPAIR_MAXBIT = 3;
        LANGPAIR_MAX = (int) Math.pow(2, LANGPAIR_MAXBIT);  // recalcule

        LANGID = new String[LANGPAIR_MAX];


        /* EN est la langue pivot */
        LANGID[ 0] = "EN";
        LANGID[ 1] = "FR";
        LANGID[ 2] = "ES";
        LANGID[ 3] = "BG";
        LANGID[ 4] = "CS";
        LANGID[ 5] = "DA";
        LANGID[ 6] = "DE";
        LANGID[ 7] = "ET";
        LANGID[ 8] = "EL";
        LANGID[ 9] = "GA";
        LANGID[10] = "IT";
        LANGID[11] = "LV";
        LANGID[12] = "LT";
        LANGID[13] = "HU";
        LANGID[14] = "MT";
        LANGID[15] = "NL";
        LANGID[16] = "PL";
        LANGID[17] = "PT";
        LANGID[18] = "RO";
        LANGID[19] = "SK";
        LANGID[20] = "SL";
        LANGID[21] = "FI";
        LANGID[22] = "SV";
        LANGID[23] = "??";
        LANGID[24] = "??";
        LANGID[25] = "??";
        LANGID[26] = "??";
        LANGID[27] = "??";
        LANGID[28] = "??";
        LANGID[29] = "??";
        LANGID[30] = "??";
        LANGID[31] = "??";







        OBJ_IMPLEMENTATION = implementationMode.FAST;

        MAP_COMPRESSION = Compression.YES;

        /** nbre d'object storage actif = 2^OBJ_PW2 */
        OBJ_PW2 = 0;  ///0=>1,1=>2,2=>4,3=>8,4=>16
        OBJ_NB = (int) Math.pow(2, OBJ_PW2);  ///0=>1,1=>2,2=>4,


    }

    /** initialisation des constantes de configuration (modifiable).
     * Ces constantes choisies dÃ©finitivement pour toute la durÃ©e de la vie du processus.
     */
    public void InitConfiguration() {

        // les directoire
        String root = SenseOS.getMYCAT_HOME()+"/data/MAP_ARCH";
        String root0 = SenseOS.getMYCAT_HOME()+"/data/MAP_ARCH/map0";
        COMMON_ROOT = root;
        MAP_ROOT = root;
        SetObjectStoreRoot(root0, 0);

        // paramÃ¨tre de fonctionnement
    }
}
