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

import java.rmi.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.zahir.align.BiSentence;
import org.olanto.zahir.align.LexicalTranslation;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.mapman.MapArchiveConstant;
import org.olanto.mapman.server.IntMap;
import org.olanto.mapman.server.MapService;
import org.olanto.senseos.SenseOS;
import static org.olanto.mapman.MapArchiveConstant.*;

/**
 * Classe pour la mise à jour des maps.
 *
 */
public class MapProcess implements Runnable {

    static IndexService_MyCat is;
    static String rootTxt;
    static MapService ms;
    static SetOfBits pivotLanguage;
    static SetOfBits targetLanguage;
    static LexicalTranslation s2t;
    static AtomicInteger newmap;
    static AtomicInteger oldmap;
    static AtomicInteger nomap;
    static int lastdoc;
    static String source;
    static String target;
    private int alignThis;

    /**
     * utiliser cette méthode pour lancer un alignement (celui de iddoc)
     * @param iddoc
     */
    public MapProcess(int iddoc) {
        alignThis = iddoc; // impossible
    }

    /**
     * utiliser cette méthode pour initialiser les var statiques
     * @param _is
     * @param _ms
     * @param _source
     * @param _target
     */
    public static void init(IndexService_MyCat _is, MapService _ms, String _source, String _target) {
        is = _is;
        ms = _ms;
        source = _source;
        target = _target;
        newmap = new AtomicInteger(0);
        oldmap = new AtomicInteger(0);
        nomap = new AtomicInteger(0);
        try {
            //System.out.println("Map from " + source + " to " + target);
            lastdoc = is.getSize(); // taille du corpus
            pivotLanguage = is.satisfyThisProperty("SOURCE." + source);
            targetLanguage = is.satisfyThisProperty("TARGET." + target);
            rootTxt = is.getROOT_CORPUS_TXT();
            try {
                s2t = new LexicalTranslation(SenseOS.getMYCAT_HOME() + "/map/" + source + target + "/lex.e2f", "UTF-8", 0.1f);
            } catch (Exception ex) {
                System.out.println(" !!! no Dictionnary from " + source + " to " + target);
                return;  // quitte sans faire de map
            }

        } catch (RemoteException ex) {
            Logger.getLogger(MapProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public static void statistic() {
        System.out.println("\nnew:" + newmap.intValue() + " old:" + oldmap.intValue() + " nomap:" + nomap.intValue());
    }

    public void run() {
        BiSentence d;
        //  for (int i = 0; i < lastdoc; i++) {
        if (pivotLanguage.get(alignThis) && targetLanguage.get(alignThis)) {
            try {
                String pivotName = is.getDocName(alignThis);
                if (!pivotName.toLowerCase().contains("/xx/"+MapArchiveConstant.GLOSS_NAME.toLowerCase()+"¦")) {  // not a glossary
                    if (!ms.existMap(alignThis, source, target)) {
                        //System.out.print("alignThis:"+alignThis);
                        String targetName = getNameOfDocForThisLang(pivotName, target);
                        //System.out.println("pivotName:"+pivotName+"targetName:"+targetName);
                        if (GET_TXT_FROM_ZIP_CACHE) { // par le zip
                            String fromContent = is.getDoc(alignThis);
                            String toContent = is.getDoc(is.getDocId(targetName));
                            d = new BiSentence(true, 10, 10, false, fromContent, toContent, 4000, 3, s2t);
                        } else { // par les fichiers
                            d = new BiSentence(true, 10, 10, false, pivotName, targetName, "UTF-8", 4000, 3, s2t);
                        }
                        ms.addMap(new IntMap(d.buildIntegerMapSO2TA(), d.buildIntegerMapTA2SO()), alignThis, source, target);
                        newmap.incrementAndGet();
                    } else {
                        oldmap.incrementAndGet();
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(MapProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (pivotLanguage.get(alignThis)) {
                nomap.incrementAndGet();
            }
        }
        if ((alignThis + 1) % 100 == 0) {
            System.out.print(".");
        }
        if ((alignThis + 1) % 10000 == 0) {
            System.out.println();
        }

    }

    /**
     *
     * @param name
     * @param Lang
     * @return
     */
    public static String getNameOfDocForThisLang(String name, String Lang) {
        int lenRootTxt = rootTxt.length();
        return rootTxt + "/" + Lang + name.substring(lenRootTxt + 3);
    }
}
