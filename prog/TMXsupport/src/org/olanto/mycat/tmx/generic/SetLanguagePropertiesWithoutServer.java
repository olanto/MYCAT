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
package org.olanto.mycat.tmx.generic;

import java.util.Arrays;
import java.util.List;
import org.olanto.idxvli.IdxConstant;
import org.olanto.idxvli.IdxStructure;
import org.olanto.idxvli.doc.PropertiesList;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.mycat.tmx.common.LangMap;
import org.olanto.senseos.SenseOS;
import static org.olanto.util.Messages.*;

/**
 * mise à jour des langues
 *
 *
 * <p>
 */
public class SetLanguagePropertiesWithoutServer {

    static IdxStructure id;
    static boolean verbose = false;

    public static void main(String[] args) {
       IdxStructure idmain = new IdxStructure("INCREMENTAL", new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME("HOW2SAY") + "/config/IDX_fix.xml"));
       LangMap.init(IdxConstant.ROOT_CORPUS_LANG);

     
        updateLanguageProperties(idmain);
        id.flushIndexDoc();  //  vide les buffers       
        id.Statistic.global();
        id.close();


        msg("-- end of SetLanguageProperties");



        //  e.printStackTrace();
    }

    public static void updateLanguageProperties(IdxStructure idpar) {

        id = idpar;
        int lastdoc = id.lastRecordedDoc; // taille du corpus
        System.out.println("lastdoc:"+lastdoc);
        int step = LangMap.size();
       System.out.println("step:"+step);

        for (int i = 0; i < step; i++) {
            msg("clear properties for: " + LangMap.getlang(i));
            id.clearThisProperty("SOURCE." + LangMap.getlang(i));
            id.clearThisProperty("TARGET." + LangMap.getlang(i));
        }
        for (int i = 0; i < lastdoc; i += step) {
            int first = i;
            for (int j = 0; j < step; j++) {
//              System.out.println("length" + id.getLengthOfD(i + j));
                if (id.getLengthOfD(i + j) > 1) { // test if not empty
                    id.setDocumentPropertie(i + j, "SOURCE." + LangMap.getlang(j));
                    for (int k = 0; k < step; k++) {
                       id.setDocumentPropertie(first + k, "TARGET." + LangMap.getlang(j));
                    }
                }
            }
            if (i % 10000 == 0) {
                System.out.print(".");
            }
            if (i % 1000000 == 0) {
                System.out.println();
            }
        }
    //    inventoryOf(setOfLang);
        msg("language properties are updated ...");
    }

    public static PropertiesList getDictionnary(String prefix) {

        String[] res;
        List<String> p = id.getDictionnary(prefix);
        if (p != null) {
            int l = p.size();
            res = new String[l];
            for (int i = 0; i < l; i++) {
                res[i] = p.get(i);
            }
            Arrays.sort(res);    // tri des collections          
            return new PropertiesList(res);
        } else {
            return null;
        }

    }

    public static String getLangOfDoc(String name) {
        return name.substring(3, 5);
    }

    public static void inventoryOf(String[] setOfLang) {
        msg("");
        msg("");
        msg("---------- ");
        PropertiesList prop;
//            msg("---- all properties:");
//            PropertiesList prop = is.getDictionnary();
//            showVector(prop.result);
        msg("---- all properties SOURCE:");
        prop = getDictionnary("SOURCE.");
        showVector(prop.result);
        msg("#doc:" + id.lastRecordedDoc);
        msg("----- GLOSSARY ---------------------------");
        countSet("SOURCE." + "XX");
        msg("----- SOURCE ---------------------------");
        for (int i = 0; i < setOfLang.length; i++) {
            countSet("SOURCE." + setOfLang[i]);
        }
        msg("----- TARGET ---------------------------");
        for (int i = 0; i < setOfLang.length; i++) {
            countSet("TARGET." + setOfLang[i]);
        }
    }

    static void countSet(String setName) {
        int lastdoc = id.lastRecordedDoc;
        int count = 0;
        SetOfBits sob = id.satisfyThisProperty(setName);
        if (sob == null) {
            msg("no property for " + setName + " :" + count);
            return;
        }

        for (int i = 0; i < lastdoc; i++) {
            if (sob.get(i)) {
                count++;
            }
        }
        msg("count for " + setName + " :" + count);
    }
}
