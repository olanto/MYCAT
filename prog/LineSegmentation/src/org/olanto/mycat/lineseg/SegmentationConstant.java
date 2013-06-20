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
package org.olanto.mycat.lineseg;

import org.olanto.mycat.lineseg.SegmentationEnum.FileMode;
import org.olanto.senseos.SenseOS;

/**
 * Une classe pour déclarer des constantes du conteneur de MAP.
 *
 */
public class SegmentationConstant {//extends org.olanto.idxvli.IdxConstant {

    public static final int NOT_FOUND = -1;
    /** un méga */
    public static final long MEGA = 1024 * 1024;
    /** un kilo */
    public static final int KILO = 1024;
    /** pour avoir plus de renseignements sur IdxIO */
    public static boolean VERBOSE_IO = false;
    /** pour eliminer les fichiers ayant disparus de docs */
    public static String REMOVE_FILE = "NO";
    /*************************************************************************************/
    /** path de la racine des fichiers communs */
    public static String FOLDER_ORIGINAL = SenseOS.getMYCAT_HOME() + "/corpus/docs";
    public static String FOLDER_CONVERTED = SenseOS.getMYCAT_HOME() + "/corpus/source";
    public static String FOLDER_CONVERTED_GLOSSARIES = SenseOS.getMYCAT_HOME() + "/corpus/source/Glossaries";
    public static String FOLDER_SEGMENTED = SenseOS.getMYCAT_HOME() + "/corpus/txt";
    public static String EXTRA_SOURCE = "NO";
    public static String FOLDER_EXTRA_SOURCE = SenseOS.getMYCAT_HOME() + "/corpus/source/BY_EXT";
    public static FileMode FILE_ORGANISATION = FileMode.BY_EXT;
    public static String FILE_EXT = ".txt";
    public static String FILE_REPLACE = SenseOS.getMYCAT_HOME() + "/config/ReplaceChar.txt";
    public static String SEPARATOR = "¦";
    public static String GLOSSARIES = "NO";
    public static String GLOSS_LANG = "XX";
    public static String GLOSS_NAME = "Glossaries";
    public static String LIST_OF_SEG_LANG = "XX YY ZZ";
    public static String[] LANGID;

    /*************************************************************************************/
    public static void show() {
        System.out.println(
                "SEGMENTATION"
                + "\nFILES"
                + "\n    REMOVE_FILE: " + REMOVE_FILE
                + "\n    FOLDER_ORIGINAL: " + FOLDER_ORIGINAL
                + "\n    FOLDER_CONVERTED: " + FOLDER_CONVERTED
                + "\n    FOLDER_CONVERTED_GLOSSARIES: " + FOLDER_CONVERTED_GLOSSARIES
                + "\n    FOLDER_SGEMENTED: " + FOLDER_SEGMENTED
                + "\n    EXTRA_SOURCE: " + EXTRA_SOURCE
                + "\n    FOLDER_EXTRA_SOURCE: " + FOLDER_EXTRA_SOURCE
                + "\n    FILE_ORGANISATION: " + FILE_ORGANISATION
                + "\n    FILE_EXT: " + FILE_EXT
                + "\n    FILE_REPLACE: " + FILE_REPLACE
                + "\n    SEPARATOR: " + SEPARATOR
                + "\n    GLOSSARIES: " + GLOSSARIES
                + "\n    GLOSS_LANG: " + GLOSS_LANG
                + "\n    GLOSS_NAME: " + GLOSS_NAME
                + "\n    LIST_OF_SEG_LANG: " + LIST_OF_SEG_LANG);
    }
}
