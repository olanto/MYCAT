/**
 * ********
 * Copyright © 2010-2014 Olanto Foundation Geneva
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
package org.olanto.mycat.ngram.generic;

import java.util.Arrays;
import java.util.List;
import org.olanto.idxvli.*;
import org.olanto.idxvli.doc.PropertiesList;
import org.olanto.senseos.SenseOS;
import org.olanto.util.Timer;

/**
 * index le corpus (sans mode serveur)
 */
public class ContentIndexingWithoutServer {      // is an application, not an applet !

    static IdxStructure id;
    static Timer t1 = new Timer("global time");

    public static void main(String[] args) {
        id = new IdxStructure("INCREMENTAL", new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME("NGRAM") + "/config/IDX_fix.xml"));
        IndexingReader.indexThis(id, SenseOS.getMYCAT_HOME("NGRAM") + "/corpus/corpus.mflf", Integer.MAX_VALUE, "UTF-8");
        id.flushIndexDoc();  //  vide les buffers       
        id.Statistic.global();


        updateProperties(id);


        id.close();
        t1.stop();

    }

    public static void updateProperties(IdxStructure idpar) {

        int lastdoc = id.lastRecordedDoc; // taille du corpus
        System.out.println("lastdoc:" + lastdoc);

        for (int i = 1990; i < 2031; i++) {
            String property = "YYYY." + i;
            System.out.println("clear properties for: " + property);
            id.clearThisProperty(property);
        }
        for (int i = 1; i < 13; i++) {
            String property = "MM." + i;
            if (i<10) property = "MM.0" + i;
            System.out.println("clear properties for: " + property);
            id.clearThisProperty(property);
        }
        for (int i = 1; i < 32; i++) {
            String property = "DD." + i;
             if (i<10) property = "DD.0" + i;
             System.out.println("clear properties for: " + property);
            id.clearThisProperty(property);
        }
        for (int i = 0; i < lastdoc; i++) {
            String docname = id.getFileNameForDocument(i);
            //System.out.println(docname.substring(0, 4) + "-" + docname.substring(4, 6) + "-" + docname.substring(6, 8));
            id.setDocumentPropertie(i, "YYYY." + docname.substring(0, 4));
            id.setDocumentPropertie(i, "MM." + docname.substring(4, 6));
            id.setDocumentPropertie(i, "DD." + docname.substring(6, 8));
            if (i % 10000 == 0) {
                System.out.print(".");
            }
            if (i % 1000000 == 0) {
                System.out.println();
            }
        }
        //    inventoryOf(setOfLang);
        System.out.println("language properties are updated ...");
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
}
