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
package org.olanto.mycat.ngram.generic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.IdxStructure;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.mysqd.server.MySelfQuoteDetection;
import org.olanto.mysqd.util.Ref;
import org.olanto.senseos.SenseOS;
import static org.olanto.util.Messages.*;
import org.olanto.util.Timer;

/**
 * test une recherche
 *
 */
public class TestExtractPeriod {

    static HashMap<String, String> stopword;
    static HashMap<String, String> focusword;
    static IdxStructure id;
    static int nbword;
    static int minocc = 100;
    static OutputStreamWriter outexport;

    public static void main(String[] args) {
        try {
            id = new IdxStructure("INCREMENTAL", new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME("NGRAM") + "/config/IDX_fix.xml"));
            nbword = id.lastRecordedWord;
            System.out.println("nbword:" + nbword);
            String drive = "C:";
            String export = drive + "/NGRAM/export/export.txt";
            outexport = new OutputStreamWriter(new FileOutputStream(export), "UTF-8");

            getAllPeriod();
            outexport.close();
        } catch (IOException ex) {
            Logger.getLogger(TestExtractPeriod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void getAllPeriod() {
        for (int yyyy = 2004; yyyy < 2005; yyyy++) {
            String syyyy = "" + yyyy;
            for (int mm = 1; mm < 13; mm++) {
                String smm = "" + mm;
                if (mm < 10) {
                    smm = "0" + mm;
                }
                for (int dd = 1; dd < 32; dd++) {
                    String sdd = "" + dd;
                    if (dd < 10) {
                        sdd = "0" + dd;
                    }
                    processPeriod(syyyy + smm + sdd);
                }
            }
        }
    }

    static int processPeriod(String per) {
        int[] docid = getDocIdPeriod(per);
        if (docid.length == 0) {
            return docid.length;
        }
        System.out.println("periode:" + per + ", nbdoc=" + docid.length);
        Timer t1 = new Timer("--- get source");
        String source = getSource(docid);
        t1.stop();
        t1 = new Timer("--- get ngram");
        List<Ref> ng = getRawNGram(source, 1, 1, 2);
        System.out.println("raw:" + ng.size());
        List<Ref> ngKeep = getNGramFiltered(ng);
        System.out.println("filtered:" + ngKeep.size());
        //showNGram(ngKeep);
        exportNGram(ngKeep,per);
        t1.stop();
        return docid.length;
    }

    public static List<Ref> getRawNGram(String content, int minFreq, int minLength, int maxLength) {
        MySelfQuoteDetection mysqd = new MySelfQuoteDetection(content, minFreq, minLength, maxLength);
        return mysqd.getNGram();
    }

    public static List<Ref> getNGramFiltered(List<Ref> allref) {
        List<Ref> reducedRef = new Vector<>();
        for (int i = 0; i < allref.size(); i++) { // pour chaque n-gram
            Ref r = allref.get(i);

            if (checkNotStopWord(r.ngram)) {
                //System.out.println(r.ngram + ", " + r.nbocc + ", " + checkFistAndLastNotStopWord(r.ngram));
                reducedRef.add(r);
            }
        }
        return reducedRef;

    }

    private static void initStopWord() {
        stopword = new HashMap<>(2000);
        String[] stoplist = id.getStopWords();
        for (int i = 0; i < stoplist.length; i++) {
            stopword.put(stoplist[i], "OK");
        }
    }

    private static void initFocusWord() {
        focusword = new HashMap<>(10000000);
        for (int i = 0; i < nbword; i++) {
            if (id.getOccOfW(i) >= minocc) {
                focusword.put(id.getStringforW(i), "");
            }
        }
        System.out.println("nbterm keep:" + focusword.size() + " at minocc:" + minocc);
    }

    private static boolean checkNotStopWord(String ngram) {
        if (stopword == null) {
            initStopWord();
        }
        if (focusword == null) {
            initFocusWord();
        }
        String[] terms = ngram.split(" ");
        for (int i = 0; i < terms.length; i++) {
            if (stopword.get(terms[0]) != null || focusword.get(terms[0]) == null) {
                return false;

            }
        }

        return true;
    }

    public static void showNGram(List<Ref> allref) {
        for (int i = 0; i < allref.size(); i++) { // pour chaque n-gram
            Ref r = allref.get(i);
            System.out.println(r.ngram + "\t" + r.nbocc + "\t" + r.len);
        }
    }
    
   public static void exportNGram(List<Ref> allref,String per) {
        for (int i = 0; i < allref.size(); i++) { // pour chaque n-gram
            Ref r = allref.get(i);
           try {
               outexport.append(per + "\t"+r.ngram + "\t" + r.nbocc + "\t" + r.len+"\n");
           } catch (IOException ex) {
               Logger.getLogger(TestExtractPeriod.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }

    public static String getSource(int[] docid) {
        StringBuilder sourceTXT = new StringBuilder("");
        for (int i = 0; i < docid.length; i++) {
            try {
                // 
                sourceTXT.append(getDoc(docid[i])).append("\n");
            } catch (RemoteException ex) {
                Logger.getLogger(TestExtractPeriod.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        msg("length source:" + sourceTXT.length());
        return sourceTXT.toString();


    }

    public static String getDoc(int docId) throws RemoteException {

        return id.zipCache.get(docId);

    }

    static int[] getDocIdPeriod(String query) {
        //            Timer t1 = new Timer("------------- " + query);

        SetOfBits yyyy = id.satisfyThisProperty("YYYY." + query.substring(0, 4));
//            System.out.println(yyyy.countTrue());
        SetOfBits mm = id.satisfyThisProperty("MM." + query.substring(4, 6));
//            System.out.println(mm.countTrue());
        SetOfBits dd = id.satisfyThisProperty("DD." + query.substring(6, 8));
//            System.out.println(dd.countTrue()); 

        SetOfBits res = new SetOfBits(yyyy);
        res.and(mm, SetOfBits.ALL);
        res.and(dd, SetOfBits.ALL);
        int nbtrue = res.countTrue();
//            System.out.println(nbtrue);
        int[] docid = new int[nbtrue];
        int count = 0;
        for (int i = 0; i < res.length(); i++) {
            if (res.get(i)) {
                docid[count] = i;
                count++;
            }
        }
        //          System.out.println(count); 
//            t1.stop();

        return docid;

    }
}
