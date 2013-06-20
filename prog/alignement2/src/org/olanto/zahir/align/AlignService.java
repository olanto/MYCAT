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

package org.olanto.zahir.align;

import org.olanto.mapman.server.IntMap;
import org.olanto.util.Timer;

/** Exemple d'initialisation d'un appel a alignement
 *  
 */
public class AlignService {

 //   private static IdxStructure id;
    private static Timer t1 = new Timer("global time");
    private static BiSentence d;

    public AlignService() {
    }

    public static IntMap getAlign(String fileSO, String fileTA, String langSO, String langTA) {

//        id = new IdxStructure(); // indexeur vide
//        id.initComponent(new ConfigurationIDX_dummy()); // pour initialiser le parseur ...

        LexicalTranslation s2t = new LexicalTranslation("C:/JG/prog/alignement2/data/xxx/lex.e2f", "UTF-8", 0.1f);
        d = new BiSentence(
                true, 5, 10,
                false,
     //           id,
                fileSO,
                fileTA,
                "UTF-8",
                200, 3,
                s2t);

        return new IntMap(d.buildIntegerMapSO2TA(), d.buildIntegerMapTA2SO());
    }
}
