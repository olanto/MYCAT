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

package org.olanto.mapman.mapper;

import org.olanto.idxvli.IdxStructure;
import org.olanto.idxvli.doc.PropertiesList;
import java.rmi.*;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.util.Timer;
import org.olanto.zahir.align.BiSentence;
import static org.olanto.util.Messages.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.mapman.server.GetMapService;
import org.olanto.mapman.server.MapService;

/**
 * pour récolter des statistiques sur les maps
 * 

 */
public class MapStatistic {

    static IndexService_MyCat is;
    static String rootTxt;
    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");
    private static BiSentence d;
    static MapService ms;

    public static void main(String[] args) {

        try {

            MapService msmain = GetMapService.getServiceMAP("rmi://localhost/MAP");
            MapStat(msmain);
        } catch (Exception e) {
            e.printStackTrace();
        }

        t1.stop();

    }

    public static void MapStat(MapService _ms) {

        ms = _ms;

        //
        try {
            id = new IdxStructure(); // indexeur vide
            id.initComponent(new ConfigurationIDX_dummy()); // pour initialiser le parseur ...
            System.out.println("connect to serveur");



            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");

            inventoryOf();
            detailDoc();

        } catch (Exception ex) {
            Logger.getLogger(MapStatistic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void detailDoc() {
        int newmap = 0;
        int oldmap = 0;
        int nomap = 0;
        try {
            int lastdoc = is.getSize(); // taille du corpus
            for (int i = 0; i < lastdoc; i++) {
                String pivotName = is.getDocName(i);
                System.out.println(i + ": " + pivotName);

            }
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
            Logger.getLogger(MapStatistic.class.getName()).log(Level.SEVERE, null, ex);
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
        countSet("SOURCE.ZH");
        msg("----- TARGET ---------------------------");
        countSet("TARGET.EN");
        countSet("TARGET.FR");
        countSet("TARGET.RU");
        countSet("TARGET.AR");
        countSet("TARGET.ES");
        countSet("TARGET.ZH");
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
