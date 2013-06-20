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
import static org.olanto.mycat.lineseg.SegmentationConstant.*;

/**
 * appels les différents segmenteurs pour chaque langue
 * modification des chemins
 * ajout d'une section pour éliminer les -e, -E, -f, -F (pas systématique)
 */
public class RUNSegmentation {

    public static void main(String[] args) {

        SegmentationInit client = new ConfigurationSegGetFromFile(SenseOS.getMYCAT_HOME() + "/config/SEG_fix.xml");
        client.InitPermanent();
        client.InitConfiguration();

        if (REMOVE_FILE.equals("YES"))
            RemoveMissingFiles.RemoveMissingFiles(FOLDER_ORIGINAL, FOLDER_CONVERTED, FILE_EXT);
        for (int i = 0; i < LANGID.length; i++) {
            SegmentLang(LANGID[i], getName(LANGID[i]));  // collect documents
        }
        if (GLOSSARIES.equals("YES")) { // process glossaries
            BY_EXT_glossaries.copy(GLOSS_LANG, FOLDER_CONVERTED_GLOSSARIES, FOLDER_SEGMENTED); // collect glossaries multilingues
        }
        if (EXTRA_SOURCE.equals("YES")) {  // existe un extra folder BY_EXT
            // force le type et root du folder (devrait être repris s'il avait d'autres exceptions ...)
            FILE_ORGANISATION = FileMode.BY_EXT;
            FOLDER_CONVERTED = FOLDER_EXTRA_SOURCE;
            for (int i = 0; i < LANGID.length; i++) {
                SegmentLang(LANGID[i], getName(LANGID[i]));  // collect documents
            }

        }
    }

    static void SegmentLang(String LANG, String LANGUAGE) {

        switch (FILE_ORGANISATION) {
            case BY_FOLDER:
           System.out.println("SEG_BY_FOLDER");
            BY_FOLDER_segmentation.Segmentation(LANG, LANGUAGE, FOLDER_CONVERTED, FOLDER_SEGMENTED);
                break;
            case BY_EXT:
          System.out.println("SEG_BY_EXT");
             BY_EXT_segmentation.Segmentation(LANG, LANGUAGE, FOLDER_CONVERTED, FOLDER_SEGMENTED);
        }


    }

    static String getName(String s) {
        if (s.equals("FR")) {
            return "FRENCH";
        }
        if (s.equals("ZH")) {
            return "CHINESE";
        }
        if (s.equals("AR")) {
            return "ARABIC";
        }
        if (s.equals("ES")) {
            return "SPANISH";
        }
        if (s.equals("RU")) {
            return "RUSSIAN";
        }
        if (s.equals("EN")) {
            return "ENGLISH";
        }
        return "???";
    }
}
