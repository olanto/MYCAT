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
package org.olanto.zahir.run.comparable;

import org.olanto.senseos.SenseOS;

/**
 * Une classe pour déclarer des constantes de l'alignement bitext
 *
 */
public class AlignComparableConstant {

    public static final int NOT_FOUND = -1;
    /** un méga */
    public static final long MEGA = 1024 * 1024;
    /** un kilo */
    public static final int KILO = 1024;
    /** pour avoir plus de renseignements sur IdxIO */
    public static boolean VERBOSE_IO = false;
    /*************************************************************************************/
    /** path de la racine des fichiers communs */
    public static String FOLDER_SEGMENTED = SenseOS.getMYCAT_HOME() + "/corpus/txt";
    public static String FOLDER_COMPARABLE_RESULT = SenseOS.getMYPREP_HOME() + "/COMP";
    public static String IDX_DONTINDEXTHIS = SenseOS.getMYCAT_HOME() + "/config/dontindexthiswords.txt";
    public static String LIST_OF_COMPARABLE_LANG = "XXYY XXYYY";
    public static String[] COMPARABLE;
    public static float MIN_DICT_LEVEL =0.1f;

    /*************************************************************************************/
    public static void show() {
        System.out.println(
                "SEGMENTATION"
                + "\nFILES"
                + "\n    FOLDER_SGEMENTED: " + FOLDER_SEGMENTED
                + "\n    FOLDER_TMX: " + FOLDER_COMPARABLE_RESULT
                + "\n    IDX_DONTINDEXTHIS: " + IDX_DONTINDEXTHIS
                + "\n    LIST_OF_BITEXT_LANG: " + LIST_OF_COMPARABLE_LANG
                + "\n    MIN_DICT_LEVEL: " + MIN_DICT_LEVEL
                + "\nVALUES");
    }
}
