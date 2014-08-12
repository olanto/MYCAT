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
package org.olanto.mycat.tmx.support;

import org.olanto.idxvli.doc.PropertiesList;
import java.rmi.*;
import org.olanto.idxvli.util.SetOfBits;
import static org.olanto.util.Messages.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.olanto.util.ExtensionUtils;

/** mise à jour des langues
 *
 * 
 *<p>
 */
public class SetLanguageProperties {

    static IndexService_MyCat is;
    static boolean verbose=false;
 
      public static void main(String[] args) {


        try {

            System.out.println("connect to serveur");

            Remote r = Naming.lookup("rmi://localhost/VLI");

            System.out.println("access to serveur");

            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                String s = is.getInformation();
                System.out.println("server says = " + s);
                System.out.println("send a STOP to" + s);

                updateLanguageProperties(is,"FR","EN");
                
//                is.quit(); // save
//                is.stop(); // stop


                msg("-- end of SetLanguageProperties");
            }

        } catch (Exception e) {
          //  e.printStackTrace();
        }
    }
      
    public static void updateLanguageProperties(IndexService_MyCat ispar, String langso, String langta) {

        is = ispar;

        try {

             int lastdoc = is.getSize(); // taille du corpus

            String[] setOfLang = is.getCorpusLanguages();  // langues du corpus
 
            for (int i = 0; i < setOfLang.length; i++) {
                msg("clear properties for: " + setOfLang[i]);
                is.clearThisProperty("SOURCE." + setOfLang[i]);
             }

            for (int i = 0; i < lastdoc; i+=2) {
               is.setDocumentPropertie(i, "SOURCE." + langso);
               is.setDocumentPropertie(i+1, "SOURCE." + langta);
                if (i  % 10000 == 0) {
                    System.out.print(".");
                }
                if (i  % 1000000 == 0) {
                    System.out.println();
                }
            }
            inventoryOf(setOfLang);
            msg("language properties are updated ...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void updateLanguageProperties(IndexService_MyCat ispar) {
//
//        is = ispar;
//
//        try {
//
//             int lastdoc = is.getSize(); // taille du corpus
//
//            String[] setOfLang = is.getCorpusLanguages();  // langues du corpus
// 
//            for (int i = 0; i < setOfLang.length; i++) {
//                msg("clear properties for: " + setOfLang[i]);
//                is.clearThisProperty("SOURCE." + setOfLang[i]);
//             }
//
//            for (int i = 0; i < lastdoc; i++) {
//                String name = is.getDocName(i);
//                String language = getLangOfDoc(name);
//                is.setDocumentPropertie(i, "SOURCE." + language);
//                if ((i + 1) % 1000 == 0) {
//                    System.out.print(".");
//                }
//                if ((i + 1) % 100000 == 0) {
//                    System.out.println();
//                }
//            }
//            inventoryOf(setOfLang);
//            msg("language properties are updated ...");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
      
      
    public static String getLangOfDoc(String name) {
        return name.substring(3, 5);
    }



    public static void inventoryOf(String[] setOfLang) {
        msg("");
        msg("");
        msg("---------- ");
        try {
            PropertiesList prop;
//            msg("---- all properties:");
//            PropertiesList prop = is.getDictionnary();
//            showVector(prop.result);
            msg("---- all properties SOURCE:");
            prop = is.getDictionnary("SOURCE.");
            showVector(prop.result);
            msg("---- all properties TARGET:");
            prop = is.getDictionnary("TARGET.");
            showVector(prop.result);
            msg("#doc:" + is.getSize());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
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
        try {
            int lastdoc = is.getSize();
            int count = 0;
            SetOfBits sob = is.satisfyThisProperty(setName);
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
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}
