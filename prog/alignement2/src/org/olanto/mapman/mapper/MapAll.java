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
package org.olanto.mapman.mapper;

import org.olanto.idxvli.IdxStructure;
import org.olanto.idxvli.doc.PropertiesList;
import java.rmi.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.util.Timer;
import org.olanto.zahir.align.BiSentence;
import org.olanto.zahir.align.LexicalTranslation;
import static org.olanto.util.Messages.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.mapman.server.GetMapService;
import org.olanto.mapman.server.IntMap;
import org.olanto.mapman.server.MapService;
import org.olanto.senseos.SenseOS;
import static org.olanto.mapman.MapArchiveConstant.*;
import org.olanto.zahir.parallel.runable.RunableProcess;

/**
 * Classe pour la mise à jour des maps.
 *
 */
public class MapAll {

    static IndexService_MyCat is;
    static String rootTxt;
    //private static IdxStructure id;
    // private static Timer t1 = new Timer("global time");
    private static BiSentence d;
    static MapService ms;

    public static void main(String[] args) {

        try {

            MapService msmain = GetMapService.getServiceMAP("rmi://localhost/MAP");
            updateMap(msmain);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  t1.stop();

    }

    public static void updateMap(MapService _ms) {
        ms = _ms;
        //   updateMapSEQ();
        updateMapPAR();
    }

    public static void updateMapPAR() {
        try {
            System.out.println("connect to serveur");
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
            msg("properties lang before update: ");
            inventoryOf();
            
            String[] setOfLang = is.getCorpusLanguages();  // langues du corpus
            for (int i = 1; i < setOfLang.length; i++) {
                msg("\nbuild maps for: " + setOfLang[i]);
                MapProcess.init(is, ms, setOfLang[0], setOfLang[i]);  // to init static
                ExecutorService executor = Executors.newFixedThreadPool(8);  // nb proc could be put into a parameters
                int lastdoc = is.getSize();
                for (int idDoc = 0; idDoc < lastdoc; idDoc++) {
                    MapProcess mp = new MapProcess(idDoc);
                    executor.execute(mp);
                }
                // This will make the executor accept no new threads
                // and finish all existing threads in the queue
                executor.shutdown();
                // Wait until all threads are finish
                while (!executor.isTerminated()) {
                }
                System.out.println("Finished all threads");
                MapProcess.statistic();
            }
            msg("properties lang after update: ");
            inventoryOf();
            System.out.println(".");
            msg("End build Map ...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //    t1.stop();

    }

    public static void updateMapSEQ() {



        //
        try {
            //   id = new IdxStructure(); // indexeur vide
            //   id.initComponent(new ConfigurationIDX_dummy()); // pour initialiser le parseur ...
            System.out.println("connect to serveur");



            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");

            msg("properties lang before update: ");
            inventoryOf();
            rootTxt = is.getROOT_CORPUS_TXT();
            String[] setOfLang = is.getCorpusLanguages();  // langues du corpus
            for (int i = 1; i < setOfLang.length; i++) {
                msg("\nbuild maps for: " + setOfLang[i]);
                createMap(setOfLang[0], setOfLang[i]);


            }
            msg("properties lang after update: ");
            inventoryOf();



            System.out.println(".");
//            msg("Save new map");
//            ms.saveAndRestart();
            msg("End build Map ...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //    t1.stop();

    }

    public static void createMap(String source, String target) {
        int newmap = 0;
        int oldmap = 0;
        int nomap = 0;
        try {
            //System.out.println("Map from " + source + " to " + target);
            int lastdoc = is.getSize(); // taille du corpus
            SetOfBits pivotLanguage = is.satisfyThisProperty("SOURCE." + source);
            SetOfBits targetLanguage = is.satisfyThisProperty("TARGET." + target);
            LexicalTranslation s2t = null;
            try {
                s2t = new LexicalTranslation(SenseOS.getMYCAT_HOME() + "/map/" + source + target + "/lex.e2f", "UTF-8", 0.1f);
            } catch (Exception ex) {
                System.out.println(" !!! no Dictionnary from " + source + " to " + target);
                return;  // quitte sans faire de map
            }
            for (int i = 0; i < lastdoc; i++) {
                if (pivotLanguage.get(i) && targetLanguage.get(i)) {
                    if (!ms.existMap(i, source, target)) {
                        // existe pas de carte
                        String pivotName = is.getDocName(i);
                        String targetName = getNameOfDocForThisLang(pivotName, target);
                        if (GET_TXT_FROM_ZIP_CACHE) { // par le zip
                            String fromContent = is.getDoc(i);
                            String toContent = is.getDoc(is.getDocId(targetName));
                            d = new BiSentence(true, 5, 10, false, fromContent, toContent, 200, 3, s2t);
                        } else { // par les fichiers
                            d = new BiSentence(true, 5, 10, false, pivotName, targetName, "UTF-8", 200, 3, s2t);
                        }
                        ms.addMap(new IntMap(d.buildIntegerMapSO2TA(), d.buildIntegerMapTA2SO()), i, source, target);
                        newmap++;
                    } else {
                        oldmap++;
                    }
                } else {
                    if (pivotLanguage.get(i)) {
                        nomap++;
                    }
                }
                if ((i + 1) % 100 == 0) {
                    System.out.print(".");
                }
                if ((i + 1) % 10000 == 0) {
                    System.out.println();
                }
            }
            System.out.println("\nnew:" + newmap + " old:" + oldmap + " nomap:" + nomap);
        } catch (RemoteException ex) {
            Logger.getLogger(MapAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getLangOfDoc(String name) {
        int lenRootTxt = rootTxt.length();
        return name.substring(lenRootTxt + 1, lenRootTxt + 3);
    }

    public static String getNameOfDocForThisLang(String name, String Lang) {
        int lenRootTxt = rootTxt.length();
        return rootTxt + "/" + Lang + name.substring(lenRootTxt + 3);
    }

    public static void setTargetLang(int isource, String name, String Lang) {
        try {
            String nameTarget = getNameOfDocForThisLang(name, Lang);
            if (is.getDocId(nameTarget) > 0) {
                // existe un document target
                is.setDocumentPropertie(isource, "TARGET." + Lang);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(MapAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void inventoryOf() {
        msg("");
        msg("");
        msg("---------- ");
        try {
            msg("---- all properties:");
            PropertiesList prop = is.getDictionnary();
            showVector(prop.result);
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
        msg("----- SOURCE ---------------------------");
        countSet("SOURCE.EN");
        countSet("SOURCE.FR");
        countSet("SOURCE.RU");
        countSet("SOURCE.AR");
        countSet("SOURCE.ES");
        countSet("SOURCE.PO");
        msg("----- TARGET ---------------------------");
        countSet("TARGET.EN");
        countSet("TARGET.FR");
        countSet("TARGET.RU");
        countSet("TARGET.AR");
        countSet("TARGET.ES");
        countSet("TARGET.PO");
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
