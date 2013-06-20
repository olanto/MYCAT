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
package org.olanto.zahir.run.bitext;

import org.olanto.senseos.SenseOS;
import org.olanto.util.Timer;
import org.olanto.zahir.align.LexicalTranslation;
import org.olanto.zahir.align.bitext.AlignASetOfBiTexts;
import static org.olanto.zahir.run.bitext.AlignBiTextConstant.*;
import static org.olanto.mapman.MapArchiveConstant.SOF;
import org.olanto.mapman.ParseSetOfWords;

/**
 * 
 * une classe pour lancer l'alignement sur un corpus
 */
public class AlignBITEXT_classic {

  //  private static IdxStructure id;
    private static Timer t1 = new Timer("global time");
    static String SO_Folder, EXT, TA_Folder, TMX_SOTA;
    private static LexicalTranslation s2t;
    private static AlignASetOfBiTexts alignSet;

    public static void main(String[] args) {

        BiTextInit client = new ConfigurationAlignBiTextFromFile(SenseOS.getMYPREP_HOME() + "/config/ALN_fix.xml");
        client.InitPermanent();
        client.InitConfiguration();
        
        t1= new Timer("global Time ");

        for (int i = 0; i < BITEXT.length; i++) {

            String SO = BITEXT[i].substring(0, 2);
            String TA = BITEXT[i].substring(2, 4);

            System.out.println("------------------ Alignement Source:" + SO + ", target:" + TA);

            SO_Folder = FOLDER_SEGMENTED + "/" + SO;
            TA_Folder = FOLDER_SEGMENTED + "/" + TA;
            TMX_SOTA = FOLDER_TMX + "/" + SO + TA;

            EXT = ".txt";
            SOF=new ParseSetOfWords(IDX_DONTINDEXTHIS);
            s2t = new LexicalTranslation(SenseOS.getMYCAT_HOME() + "/map/" + SO + TA + "/lex.e2f", "UTF-8", MIN_DICT_LEVEL);
            alignSet = new AlignASetOfBiTexts(
                    SO,
                    TA,
                    true,
                    false,
                    SO_Folder,
                    TA_Folder,
                    "UTF-8",
                    0.2f,
                    s2t,
                    TMX_SOTA,
                    true,
                    EXT);
            System.out.println(alignSet.getInfo());
            alignSet.alignSeqMethod();
            alignSet.close();
        }
        t1.stop();
        System.out.println("end ...");
    }
}

